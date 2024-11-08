(ns advent-of-code.2023.day1-trebuchet
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

;; The newly-improved calibration document consists of lines of text; each line originally contained a specific
;; calibration value that the Elves now need to recover. On each line, the calibration value can be found by combining
;; the first digit and the last digit (in that order) to form a single two-digit number.

;; For example:

;; 1abc2
;; pqr3stu8vwx
;; a1b2c3d4e5f
;; treb7uchet
;; In this example, the calibration values of these four lines are 12, 38, 15, and 77. Adding these together produces 142.

;; Consider your entire calibration document. What is the sum of all of the calibration values?

(defn get-number [line]
  (let [numbers (re-seq #"\d" line)
        nums (str (first numbers) (last numbers))]
    (parse-long nums)))

;; Part 1 Example input
(->> ["1abc2one"
      "pqr3stu8vwx"
      "a1b2c3d4e5f"
      "treb7uchet"]
     (map get-number)
     (apply +))
;; 142

;; Part 1 Test input
(->> (io/resource "day1.txt")
     (slurp)
     (str/split-lines)
     (map get-number)
     (apply +))

;; 54159

;; --- Part Two ---
;; Your calculation isn't quite right. It looks like some of the digits are actually spelled out with letters: one, two,
;; three, four, five, six, seven, eight, and nine also count as valid "digits".

(def words-to-digits
  {"six" 6,
   "three" 3,
   "two" 2,
   "seven" 7,
   "five" 5,
   "eight" 8,
   "one" 1,
   "nine" 9,
   "four" 4})

(defn word-to-num [num]
  (get words-to-digits num num))

(defn get-key [line f1 f2]
  (->> ["six" "three" "two" "seven" "five" "eight" "one" "nine" "four" "1" "2" "3" "4" "5" "6" "7" "8" "9"]
       (map #(vector % (f1 line %)))
       (filter #(some? (second %)))
       (sort-by second f2)
       first
       first))

(defn get-first-number [line]
  (get-key line str/index-of <))

(defn get-last-number [line]
  (get-key line str/last-index-of >))

;; we could use regex for parsing, but it cannot handle correctly overlapping words like "xtwonex"
;; numbers (re-seq #"\d|two|one|three|four|five|six|seven|eight|nine" line)

(defn get-string-number [line]
  (let [nums (->> [(get-first-number line) (get-last-number line)]
                  (map word-to-num)
                  (str/join))]
    (parse-long nums)))

;; Part 2 Example input
(->> ["two1nine"
      "eightwothree"
      "abcone2threexyz"
      "xtwone3four"
      "4nineeightseven2"
      "zoneight234"
      "7pqrstsixteen"]
     (map get-string-number)
     (apply +))

;; 281

;; Part 2 test input
(->> (io/resource "day1.txt")
     (slurp)
     (str/split-lines)
     (map str/lower-case)
     (map get-string-number)
     (apply +))

;; 53866
