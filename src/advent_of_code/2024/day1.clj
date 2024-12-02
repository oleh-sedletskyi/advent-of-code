(ns advent-of-code.2024.day1
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

;; Throughout the Chief's office, the historically significant locations are listed not by name but
;; by a unique number called the location ID. To make sure they don't miss anything, The Historians split
;; into two groups, each searching the office and trying to create their own complete list of location IDs.

;; There's just one problem: by holding the two lists up side by side (your puzzle input), it quickly
;; becomes clear that the lists aren't very similar. Maybe you can help The Historians reconcile their lists?

;; Example input:
;; 3   4
;; 4   3
;; 2   5
;; 1   3
;; 3   9
;; 3   3

;;Maybe the lists are only off by a small amount! To find out, pair up the numbers and measure
;;how far apart they are. Pair up the smallest number in the left list with the smallest number
;;in the right list, then the second-smallest left number with the second-smallest right number,
;;and so on.

;;Within each pair, figure out how far apart the two numbers are; you'll need to add up all of
;;those distances. For example, if you pair up a 3 from the left list with a 7 from the right
;;list, the distance apart is 4; if you pair up a 9 with a 3, the distance apart is 6.

;; Steps:
;; sort
;; pair
;; calc distances
;; sum up

(def input
  "3   4
   4   3
   2   5
   1   3
   3   9
   3   3")

(def test-input
  (->> (io/resource "2024/day1.txt")
       (slurp)
       #_input
       (str/split-lines)
       (map #(->> (re-matches #"\s*(\d+)\s+(\d+)\s*" %)
                  (take-last 2)))))

(let [sorted-lists (->> [first second]
                        (map (fn [f]
                               (->> test-input
                                    (map #(f %))
                                    (map parse-long))))
                        (map sort))]
  (->> (map #(vector %1 %2) (first sorted-lists) (second sorted-lists))
       (map #(apply - %))
       (map abs)
       (apply +)))
