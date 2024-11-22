(ns advent-of-code.2016.day5-md5
  (:import
   [java.security MessageDigest]
   [java.math BigInteger]))

;; https://adventofcode.com/2016/day/5

;; The eight-character password for the door is generated one character at a time by finding the MD5
;; hash of some Door ID (your puzzle input) and an increasing integer index (starting with 0).

;; A hash indicates the next character in the password if its hexadecimal representation starts
;; with five zeroes. If it does, the sixth character in the hash is the next character of the password.

;; For example, if the Door ID is abc:

;; The first index which produces a hash that starts with five zeroes is 3231929, which we find
;; by hashing abc3231929; the sixth character of the hash, and thus the first character of the
;; password, is 1.
;; 5017308 produces the next interesting hash, which starts with 000008f82..., so the second
;; character of the password is 8.
;; The third time a hash starts with five zeroes is for abc5278568, discovering the character f.
;; In this example, after continuing this search a total of eight times, the password is 18f47a30.

(defn md5 [^String s]
  ;; https://gist.github.com/jizhang/4325757
  (let [algorithm (MessageDigest/getInstance "MD5")
        raw (.digest algorithm (.getBytes s))]
    (format "%032x" (BigInteger. 1 raw))))

(md5 "abc3231929")
(md5 "abc5017308")
(md5 "abc5278568")

(def door-id "abc")
(def door-id "wtnhxymk")

(comment
  ;; part 1
  (->> (range)
       (map #(str door-id %))
       (map md5)
       (keep #(second (re-matches #"00000(.).+" %)))
       (take 8)
       (reduce str)
       time)

  "2414bc77"
  ;; 46sec
  ;;
  )

;; Instead of simply filling in the password from left to right, the hash now also indicates
;; the position within the password to fill. You still look for hashes that begin with five zeroes;
;; however, now, the sixth character represents the position (0-7), and the seventh character is
;; the character to put in that position.

;; A hash result of 000001f means that f is the second character in the password. Use only the first
;; result for each position, and ignore invalid positions.

;; For example, if the Door ID is abc:

;; The first interesting hash is from abc3231929, which produces 0000015...; so, 5 goes in
;; position 1: _5______.
;; In the previous method, 5017308 produced an interesting hash; however, it is ignored, because
;; it specifies an invalid position (8).
;; The second interesting hash is at index 5357525, which produces 000004e...; so, e goes in
;; position 4: _5__e___.
;; You almost choke on your popcorn as the final character falls into place, producing the
;; password 05ace8e3.

(comment
  (->> (range)
       (map #(str door-id %))
       (map md5)
       #_["000002cc"
          "asdasdasd"
          "000000aa"
          "000001bb"
          "000003bb"
          "0000044bb"
          "0000055bb"
          "0000066bb"
          "0000077bb"
          "0000066bb"
          "0000086bb"
          "0000096bb"
          "000001dd"]
       (keep #(next (re-matches #"00000([0-7])(.).+" %)))
       (reduce (fn [acc [pos char]]
                 (if (= 8 (count acc))
                   (reduced acc)
                   (if (get acc pos)
                     acc
                     (assoc acc pos char))))
               {})
       (sort-by first)
       (map second)
       (apply str)
       time)

  ;; "437e60fc"
  ;;
  ;; 156sec
  ;;
  )
