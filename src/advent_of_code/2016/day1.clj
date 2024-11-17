(ns advent-of-code.2016.day1
  (:require [clojure.string :as str]))

;; The Document indicates that you should start at the given coordinates (where you just landed) and face North.
;; Then, follow the provided sequence: either turn left (L) or right (R) 90 degrees, then walk forward the given
;; number of blocks, ending at a new intersection.

;; There's no time to follow such ridiculous instructions on foot, though, so you take a moment and work out the destination.
;; Given that you can only walk on the street grid of the city, how far is the shortest path to the destination?

;; For example:

;; Following R2, L3 leaves you 2 blocks East and 3 blocks North, or 5 blocks away.
;; R2, R2, R2 leaves you 2 blocks due South of your starting position, which is 2 blocks away.
;; R5, L5, R5, R3 leaves you 12 blocks away.
;; How many blocks away is Easter Bunny HQ?

(def moving-map {:top {:right :right
                       :left :left}
                 :right {:right :bottom
                         :left :top}
                 :bottom {:right :left
                          :left :right}
                 :left {:right :top
                        :left :bottom}})

(def input-str "R2, R2, R2")
(def part1-test-input "R3, R1, R4, L4, R3, R1, R1, L3, L5, L5, L3, R1, R4, L2, L1, R3, L3, R2, R1, R1, L5, L2, L1, R2, L4, R1, L2, L4, R2, R2, L2, L4, L3, R1, R4, R3, L1, R1, L5, R4, L2, R185, L2, R4, R49, L3, L4, R5, R1, R1, L1, L1, R2, L1, L4, R4, R5, R4, L3, L5, R1, R71, L1, R1, R186, L5, L2, R5, R4, R1, L5, L2, R3, R2, R5, R5, R4, R1, R4, R2, L1, R4, L1, L4, L5, L4, R4, R5, R1, L2, L4, L1, L5, L3, L5, R2, L5, R4, L4, R3, R3, R1, R4, L1, L2, R2, L1, R4, R2, R2, R5, R2, R5, L1, R1, L4, R5, R4, R2, R4, L5, R3, R2, R5, R3, L3, L5, L4, L3, L2, L2, R3, R2, L1, L1, L5, R1, L3, R3, R4, R5, L3, L5, R1, L3, L5, L5, L2, R1, L3, L1, L3, R4, L1, R3, L2, L2, R3, R3, R4, R4, R1, L4, R1, L5")

(def steps [{:direction :right
             :moves 2}
            {:direction :left
             :moves 3}])

(defn calc-position [steps]
  (->> steps
       (reduce (fn [acc {:keys [direction moves]}]
                 (let [prev-direction (:direction acc)
                       curr-direction (get-in moving-map [prev-direction direction])]
                   (-> (assoc acc :direction curr-direction)
                       (update-in [:position (get-in moving-map [prev-direction direction])] + moves))))
               {:direction :top
                :position {:top 0
                           :right 0
                           :left 0
                           :bottom 0}})))

(defn calc-distance [{:keys [position]}]
  (let [{:keys [top right left bottom]} position
        left-right (abs (- left right))
        top-bottom (abs (- top bottom))]
    (+ left-right top-bottom)))

(defn calc-blocks [steps]
  (-> steps
      (str/split #",")
      (->> (map str/trim)
           (map (fn [move]
                  (let [[_ c num] (re-matches #"([LR])(\d+)" move)
                        num (parse-long num)
                        direction (if (= c "L")
                                    :left
                                    :right)]
                    {:direction direction
                     :moves num})))
           (calc-position)
           (calc-distance))))

(calc-blocks part1-test-input)
;; 300
