(ns clojupyter-plugin.widgets.control
  (:require [clojupyter.kernel.comm-atom :as ca]
            [clojupyter.log :as log]
            [clojupyter.util-actions :refer [uuid]]
            [clojupyter-plugin.widgets  :as widget]))

;;; ------------------------------------------------------------------------------------------------------------------------
;;; INTERACT
;;; ------------------------------------------------------------------------------------------------------------------------

(defn interactive
  [output-widget f wmap]
  (let [agent-key (gensym)
        cur (atom (into {} (for [[k w] wmap] [k (get @w :value)])))
        set-value! (fn [] (swap! output-widget assoc :value (f @cur)))]
    (doseq [[k w] wmap]
      (assert (ca/comm-atom? w))
      (ca/watch w agent-key
                (fn [_ _ _ new]
                  (try (swap! cur assoc k (get new :value))
                       (set-value!)
                       (catch Exception e
                         (log/error (str "Error in interactive-agent@" agent-key ": " e)
                                    (log/ppstr {:cur @cur :k k :new new})))))))
    (set-value!)
    (widget/v-box {:children (vec (concat (vals wmap) [output-widget]))})))

(defn interact!
  [f w0 & ws]
  (let [widgs (vec (cons w0 ws))
        out (widget/label {:value (str (apply f (map (comp :value deref) widgs)))})
        observe! (fn [idx] (ca/watch (nth widgs idx) :value
                              (fn [k _ {o-val :value} {n-val :value}]
                                (when (not= o-val n-val)
                                  (let [v (apply f (map #(if (= %1 idx) n-val (get @%2 :value)) (range) widgs))]
                                    (swap! out assoc :value (str v)))))))]
    (doseq [i (range (count widgs))] (observe! i))
    (widget/v-box {:children (conj widgs out)})))

(defn tie!
  ([f source target] (tie! f source target :value :value))
  ([f source target source-attr target-attr]
   (add-watch source (gensym :on-change)
     (fn [_ _ old new]
       (when (not= (get old source-attr)
                   (get new source-attr))
         (swap! target assoc target-attr
           (f (get new source-attr))))))))
