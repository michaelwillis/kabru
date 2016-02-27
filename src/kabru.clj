(ns kabru (:gen-class))

(defn travel [state new-place]
  (assoc state :place new-place))

(defn quit [state]
  (assoc state :playing false))

(defn message [state text]
  (println text)
  (println)
  state)

(def places
  {:town    {:description (str "Ahh, Fairview, an idealic place to grow up!\n"
                             "Just don't step outside of the city walls,\n"
                             "or you're likely to be eaten by a grue.")

             :options {"I" {:description "Walk into the Fairview Inn"
                            :action [travel :inn]}

                       "F" {:description "Go to the forest"
                            :action [travel :forest]}

                       "Q" {:description "Quit Game"
                            :action [quit]}}}

   :forest  {:description (str "You have entered the dark forest\n"
                               "Didn't anybody warn you to stay in the city?")

             :options {"T" {:description "Walk back to the town of Fairview"
                            :action [travel :town]}

                       "F" {:description "Find a monster to fight"
                            :action [message "You can't find anything!"]}}}

   :inn     {:description (str "Fairview Inn, dining and bedding since 1257")

             :options {"X" {:description "Exit the Inn"
                            :action [travel :town]}

                       "R" {:description "Get a room"
                            :action [message "You take a room for the night and wake up feeling refreshed"]}
                       
                       "T" {:description "Talk to the Boris the inn keeper"
                            :action [message "Boris curtly tells you to \"GET LOST, HOSER.\""]}}}})

(defn display-place [state]
  (let [place (-> state :place places)]
    (println (place :description))
    (println)
    (doseq [[key option] (place :options)]
      (println (str "  " key " - " (option :description))))
    (println)))

(defn -main []
  (let [scanner (new java.util.Scanner System/in)
        initial-state {:place :town, :playing true}]
    (display-place initial-state)
    (loop [state initial-state]
      (let [input (-> scanner .next .toUpperCase)
            action (-> state :place places :options (get input) :action)]
        (if action
          (let [action-function (partial (first action) state)
                action-params (rest action)
                new-state (apply action-function action-params)]
            (when (new-state :playing)
              (display-place new-state)
              (recur new-state)))
          (do
            (println "Try again, hoser")
            (recur state)))))))
