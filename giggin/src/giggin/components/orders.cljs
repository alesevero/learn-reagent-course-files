(ns giggin.components.orders
  (:require [giggin.state :as state]
            [giggin.helpers :refer [format-price]]))

(defn items-view
  [id quant]
  (let [remove-item #(swap! state/orders dissoc %)]
    [:div.item {:key id}
     [:div.img
      [:img {:src (get-in @state/gigs [id :img])
             :alt (get-in @state/gigs [id :title])}]]
     [:div.content
      [:p.title (str (get-in @state/gigs [id :title]) " \u00D7 " quant)]]
     [:div.action
      [:div.price (format-price (* (get-in @state/gigs [id :price]) quant))]
      [:button.btn.btn--link.tooltip
       {:data-tooltip "Remove"
        :on-click #(remove-item id)}
       [:i.icon.icon--cross]]]]))

(defn empty-orders
  []
  [:div.empty
   [:div.title "You don't have any orders"]
   [:div.subtitle "Click on a + to add an order"]])

(defn total-view
  []
  (let [total #(->> @state/orders
                    (map  (fn [[id quant]] (* quant (get-in @state/gigs [id :price]))))
                    (reduce + 0))
        remove-all #(reset! state/orders {})]
    [:div.total
     [:hr]
     [:div.item
      [:div.content "Total"]
      [:div.action
       [:div.price (format-price (total))]]
      [:button.btn.btn--link.tooltip
       {:data-tooltip "Remove All"
        :on-click #(remove-all)}
       [:i.icon.icon--delete]]]]))

(defn content
  []
  [:aside
   [:div.order
    [:div.body
     (for [[id quant] @state/orders]
       (items-view id quant))]
    [total-view]]])

(defn orders
  []
  (if (empty? @state/orders)
    (empty-orders)
    (content)))