(ns koddsson.age
  (:require
    [clojure.string :as str]
    [om.core :as om :include-macros true]
    [om.dom :as dom :include-macros true]))

;;;;
;;;;  TODO: Refactor moment.js out.
;;;;

(defn calculate-age [birthday & {:keys [format decimal-points] 
                                 :or {format "YYYYMMDD" decimal-points 8}}]
  ;; Calculates age from a given birthday
  (let [birthday (js/moment birthday format)]
    (.toFixed (.diff (js/moment) birthday "years" true) decimal-points)))

(defn age-counter [app owner]
  ;; Displays age in a presentable fashion
  (reify
    om/IWillMount
    (will-mount [_]
      (js/setInterval (fn [] (om/transact! app :age #(calculate-age (:birthday @app)))) 10))
    om/IRenderState
    (render-state [_ state]
      (dom/div #js {}
        (dom/div #js {} (str "Age: " (:age app)))))))
