;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-intermediate-lambda-reader.ss" "lang")((modname homework5-MiaYim-JaneKamata) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;   Homework 4 (Part 1): Several Helper Functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(require 2htdp/batch-io)
(require "io-extra.rkt")

;; A [List-of X] is one of
;; - '()
;; - (cons X [List-of X])
;; Interpretation: A list of elements of type X.

;; list-template : [List-of X] -> ?
(define (list-template alist)
  (cond
    [(empty? alist) ...]
    [(cons? alist) (... (first alist) (list-template (rest alist)) ...)]))

;; take-n : Int [List-of X] -> [List-of X]
;; (take-n n alist) produces the first n elements of the list in the order they appear.
;; n must be non-negative, and it produces the whole list if n exceeds the length of the
;; list.

(define (take-n n lox)
  (cond
    [(empty? lox) null]
    [(cons? lox) (if (= n 0) null (cons (first lox) (take-n (- n 1) (rest lox))))]))

(check-expect (take-n 0 (list 21 23 25)) (list))
(check-expect (take-n 4 (list 0 0 0 3 4 5 6 7 8 9 10)) (list 0 0 0 3))
(check-expect (take-n 20 (list 0 1 2 3 4 5 6 7 8 9 10)) (list 0 1 2 3 4 5 6 7 8 9 10))
(check-expect (take-n 5 '()) '())

;; drop-n : Int [List-of X] -> [List-of X]
;; (drop-n n alist) produces alist but with the first n elements removed.
;; n must be non-negative, and drop-n produces the empty list if n exceeds the length of the
;; list.

(define (drop-n n lox)
  (cond
    [(empty? lox) null]
    [(cons? lox) (if (= n 0) lox (drop-n (- n 1) (rest lox)))]))

(check-expect (drop-n 0 (list 21 23 25)) (list 21 23 25))
(check-expect (drop-n 4 (list 0 1 2 3 4 5 6 7 8 9 10)) (list 4 5 6 7 8 9 10))
(check-expect (drop-n 20 (list 0 1 2 3 4 5 6 7 8 9 10)) '())
(check-expect (drop-n 5 '()) '())

;; take-while : (X -> Bool) [List-of X] -> [List-of X]
;; (take-while pred alist) produces the longest prefix of alist, where all
;; elements satisfy pred.

(define (take-while pred lox)
  (cond
    [(empty? lox) null]
    [(cons? lox) (if (not (pred (first lox))) null (cons (first lox) (take-while pred (rest lox))))]))

;; less-than-3? : Number -> Boolean
;; Determines if a number is less than three (true) or not (false)

(define (less-than-3? n)
  (> 3 n))

(check-expect (less-than-3? -2) #true)
(check-expect (less-than-3? 0) #true)
(check-expect (less-than-3? 2) #true)
(check-expect (less-than-3? 3) #false)
(check-expect (less-than-3? 5) #false)

(check-expect (take-while less-than-3? (list 0 1 2 3 4)) (list 0 1 2))
(check-expect (take-while even? (list 2 4 6 8 9)) (list 2 4 6 8))
(check-expect (take-while odd? (list 2 4 6 8)) (list))
(check-expect (take-while less-than-3? (list)) (list))

;; drop-while : (X -> Bool) [List-of X] -> [List-of X]
;; (drop-while pred alist) produces a suffix of alist that we get by dropping
;; the longest prefix of alist that satisfies pred.

(define (drop-while pred lox)
  (cond
    [(empty? lox) null]
    [(cons? lox) (if (not (pred (first lox))) lox (drop-while pred (rest lox)))]))

(check-expect (drop-while less-than-3? (list 0 1 2 3 4)) (list 3 4))
(check-expect (drop-while even? (list 2 4 6 8 9 10)) (list 9 10))
(check-expect (drop-while odd? (list 2 4 6 8)) (list 2 4 6 8))
(check-expect (drop-while less-than-3? (list)) (list))

;; group-by : (X X -> Boolean) [List-of X] -> [List-of [NE-List-of X]]
;; (group-by same-group? alist) splits alist into groups by comparing
;; consecutive elements to check if they should be in the same group.

(define (group-by same-group? lox)
  (local
   ;; prev : X
   ;; curr : [X]
   ;; lolox : [[X]]
   ((define-struct acc [prev curr lolox])
    (define (help x acc)
      (if (empty? (acc-curr acc))
          (make-acc x (list x) (acc-lolox acc))
          (if (same-group? x (acc-prev acc))
              (make-acc x (cons x (acc-curr acc)) (acc-lolox acc))
              (make-acc x (list x) (cons (acc-curr acc) (acc-lolox acc))))))
    (define folded (foldr help (make-acc #f null null) lox)))
   (if (empty? (acc-curr folded)) (acc-lolox folded) (cons (acc-curr folded) (acc-lolox folded)))))

(check-expect (group-by = (list 10 10 20 20 20 10)) (list (list 10 10) (list 20 20 20) (list 10)))
(check-expect (group-by = (list 5 6 7 8)) (list (list 5) (list 6) (list 7) (list 8)))
(check-expect (group-by = (list 9 9 9 9 9)) (list (list 9 9 9 9 9)))
(check-expect
 (group-by = (list 5 10 11 10 10 10 11 11 5 10 5 5))
 (list (list 5) (list 10) (list 11) (list 10 10 10) (list 11 11) (list 5) (list 10) (list 5 5)))
(check-expect (group-by = (list)) (list))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;   Homework 5 (Part 2): Text Processing
;   Mia Yim and Jane Kamata
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Example of txt:
(define TEST.txt
  (list "The Complete Works of William Shakespeare"
        ""
        "by William Shakespeare"
        ""
        "                    Contents"
        ""
        "    THE SONNETS"
        "    ALL’S WELL THAT ENDS WELL"
        "    THE TRAGEDY OF ANTONY AND CLEOPATRA"
        "    AS YOU LIKE IT"
        ""
        ""
        "THE SONNETS"
        "                    1"
        ""
        "From fairest creatures we desire increase,"
        ""
        "ALL’S WELL THAT ENDS WELL"
        ""
        "COUNTESS."
        "In delivering my son from me, I bury a second husband."
        ""
        "THE TRAGEDY OF ANTONY AND CLEOPATRA"
        ""
        "PHILO."
        "Nay, but this dotage of our general’s"
        ""
        "AS YOU LIKE IT"
        ""
        "  ORLANDO. As I remember, Adam, it was upon this fashion bequeathed me "
        ""
        "FINIS"))

;; delete-spaces : String -> String
;;removes 4 spaces in front of a string

(define (delete-spaces str)
  (if (string-contains? "    " str) (substring str 4) str))

(check-expect (delete-spaces "    hello") "hello")
(check-expect (delete-spaces "  hello") "  hello")
(check-expect (delete-spaces "") "")

;; table-of-contents : [List-of String] -> [List-of String]
;; Extracts the table of contents from the lines of text of a book, formatted
;; similarly to pg100.txt.

(define (table-of-contents los)
  (local [; find-contents : String -> Boolean
          ; finds if the string does not contain "Contents"
          ; so that when implemented into drop-while the not will be negated to find
          ; the string "contents" to find where the table of contents begins
          (define (find-contents s)
            (not (string-contains? "Contents" s)))
          ; find-empty : String -> Boolean
          ; determines if the string is not an empty string
          (define (find-empty s)
            (not (string=? "" s)))
          ; delete-empty : [List-of String] -> [List-of String]
          ; removes the first empty string of the list
          (define (delete-empty alist)
            (remove "" alist))]
         (map delete-spaces
              (take-while find-empty
                          (delete-empty (drop-while find-empty (drop-while find-contents los)))))))

(check-expect (table-of-contents (read-lines "pg100.txt"))
              (list "THE SONNETS"
                    "ALL’S WELL THAT ENDS WELL"
                    "THE TRAGEDY OF ANTONY AND CLEOPATRA"
                    "AS YOU LIKE IT"
                    "THE COMEDY OF ERRORS"
                    "THE TRAGEDY OF CORIOLANUS"
                    "CYMBELINE"
                    "THE TRAGEDY OF HAMLET, PRINCE OF DENMARK"
                    "THE FIRST PART OF KING HENRY THE FOURTH"
                    "THE SECOND PART OF KING HENRY THE FOURTH"
                    "THE LIFE OF KING HENRY THE FIFTH"
                    "THE FIRST PART OF HENRY THE SIXTH"
                    "THE SECOND PART OF KING HENRY THE SIXTH"
                    "THE THIRD PART OF KING HENRY THE SIXTH"
                    "KING HENRY THE EIGHTH"
                    "KING JOHN"
                    "THE TRAGEDY OF JULIUS CAESAR"
                    "THE TRAGEDY OF KING LEAR"
                    "LOVE’S LABOUR’S LOST"
                    "THE TRAGEDY OF MACBETH"
                    "MEASURE FOR MEASURE"
                    "THE MERCHANT OF VENICE"
                    "THE MERRY WIVES OF WINDSOR"
                    "A MIDSUMMER NIGHT’S DREAM"
                    "MUCH ADO ABOUT NOTHING"
                    "THE TRAGEDY OF OTHELLO, MOOR OF VENICE"
                    "PERICLES, PRINCE OF TYRE"
                    "KING RICHARD THE SECOND"
                    "KING RICHARD THE THIRD"
                    "THE TRAGEDY OF ROMEO AND JULIET"
                    "THE TAMING OF THE SHREW"
                    "THE TEMPEST"
                    "THE LIFE OF TIMON OF ATHENS"
                    "THE TRAGEDY OF TITUS ANDRONICUS"
                    "THE HISTORY OF TROILUS AND CRESSIDA"
                    "TWELFTH NIGHT: OR, WHAT YOU WILL"
                    "THE TWO GENTLEMEN OF VERONA"
                    "THE TWO NOBLE KINSMEN"
                    "THE WINTER’S TALE"
                    "A LOVER’S COMPLAINT"
                    "THE PASSIONATE PILGRIM"
                    "THE PHOENIX AND THE TURTLE"
                    "THE RAPE OF LUCRECE"
                    "VENUS AND ADONIS"))
(check-expect (table-of-contents '()) '())

(define-struct work [title contents])
;; A Work is a (make-work String [List-of String])
;; A (make-work title lines) represents a work of literature with the given
;; title and lines of text.

;; work-template : Work -> ?
(define (work-template w)
  (... (work-title w) ... (work-contents w) ...))

(define EX-WORK-1 (make-work "Hamlet" (list "To be or not to be.")))
(define EX-WORK-2 (make-work "Romeo and Juliet" (list "What's in a name?")))

;; next-title : [List-of String] String -> String
;; finds the next string in the list after the given string
;; given that the string is in the list and the list is not empty
(define (next-title toc t)
  (local
   [(define (check-title s)
      (not (string=? s t)))]
   (if (> (length (drop-while check-title toc)) 1) (second (drop-while check-title toc)) "FINIS")))

(check-expect (next-title (table-of-contents (read-lines "pg100.txt")) "VENUS AND ADONIS") "FINIS")
(check-expect (next-title (table-of-contents (read-lines "pg100.txt")) "THE SONNETS")
              "ALL’S WELL THAT ENDS WELL")
(check-expect
 (next-title (table-of-contents (read-lines "pg100.txt")) "THE TRAGEDY OF ANTONY AND CLEOPATRA")
 "AS YOU LIKE IT")

;; extract-one-work : String [List-of String] [List-of String] -> Work
;; (extract-one-work title toc lines) produces one work in a collection, given the list
;; of works extracted from the table of contents. The lines are formatted
;; similar to pg100.txt.
(define (extract-one-work title toc lines)
  (local [;; find-title : String -> Boolean
          ;; determines if the string is not the given title
          (define (find-title s)
            (not (string=? (delete-spaces title) s)))
          ;; drop-title : String -> String
          ;; checks if the string is not an empty string
          (define (drop-title s)
            (not (string=? "" s)))
          ;; find-next-title : String -> Boolean
          ;; Determines if the string is not the next title from the toc
          (define (find-next-title s)
            (not (string-contains? (next-title toc title) s)))]
         (make-work (delete-spaces title)
                    (take-while find-next-title
                                (drop-while drop-title (drop-while find-title lines))))))

(check-expect (extract-one-work "THE SONNETS" (table-of-contents TEST.txt) TEST.txt)
              (make-work "THE SONNETS" (list "" "From fairest creatures we desire increase," "")))

(check-expect
 (extract-one-work "AS YOU LIKE IT" (table-of-contents TEST.txt) TEST.txt)
 (make-work "AS YOU LIKE IT"
            (list "" "  ORLANDO. As I remember, Adam, it was upon this fashion bequeathed me " "")))

(check-expect (extract-one-work "" '() '()) (make-work "" '()))

;; extract-works : [List-of String] [List-of String] -> [List-of Work]
;; (extract-works toc lines) produces the works in a collection, given the list
;; of works extracted from the table of contents. The lines are formatted
;; similar to pg100.txt.

(define (extract-works toc lines)
  (local [; extract-work : String -> Work
          ; creates a work of the given title and
          ; it's corresponding contents
          (define (extract-work title)
            (extract-one-work title toc lines))]
         (map extract-work toc)))

(check-expect
 (extract-works (table-of-contents TEST.txt) TEST.txt)
 (list (make-work "THE SONNETS" (list "" "From fairest creatures we desire increase," ""))
       (make-work "ALL’S WELL THAT ENDS WELL"
                  (list "" "COUNTESS." "In delivering my son from me, I bury a second husband." ""))
       (make-work "THE TRAGEDY OF ANTONY AND CLEOPATRA"
                  (list "" "PHILO." "Nay, but this dotage of our general’s" ""))
       (make-work
        "AS YOU LIKE IT"
        (list "" "  ORLANDO. As I remember, Adam, it was upon this fashion bequeathed me " ""))))
(check-expect (extract-works '() '()) '())

;; extract-works-to-files : String -> [List-of String]
;; Given the path to a collected works, writes several files -- one for each
;; work -- and produces the list of file names.

(define (extract-works-to-files str)
  (local [;; extract-one-work-to-file : String -> String
          ;; Given the one work, writes one file and produces file name
          (define (extract-one-work-to-file work)
            (write-lines (string-append (work-title work) ".txt") (work-contents work)))]
         (map extract-one-work-to-file
              (extract-works (table-of-contents (read-lines str)) (read-lines str)))))

;; Examples of [List-of String]
(define LIST-1
  (list "Hi"
        "Nice"
        "to"
        "meet"
        "you"
        "My"
        "name"
        "is"
        "John"
        "Smith"
        "I"
        "am"
        "19"
        "and"
        "a"
        "student"
        "in"
        "college"
        "I"
        "go"
        "to"
        "college"
        "in"
        "New"
        "York"
        "My"
        "favorite"
        "courses"
        "are"
        "Geometry"
        "French"
        "and"
        "History"
        "English"
        "is"
        "my"
        "hardest"
        "course"
        "My"
        "professors"
        "are"
        "very"
        "friendly"
        "and"
        "smart"
        "It's"
        "my"
        "second"
        "year"
        "in"
        "college"
        "now"
        "I"
        "love"
        "it"
        "college"
        "my"
        "and"
        "College"))

;; Word Frequency of LIST-1 up to 11:
;; my 6
;; college 5
;; and 4
;; in 3
;; i 3
;; are 2
;; is 2
;; to 2
;; love 1
;; hi 1
;; very 1

(define LIST-2
  (list "George"
        "is"
        "at"
        "the"
        "pet"
        "store"
        "looking"
        "at"
        "what"
        "kind"
        "of"
        "pet"
        "he"
        "might"
        "want"
        "to"
        "get"
        "for"
        "his"
        "birthday"
        "George"
        "asked"
        "if"
        "he"
        "could"
        "have"
        "a"
        "horse"
        "but"
        "his"
        "parents"
        "said"
        "no"
        "because"
        "horses"
        "are"
        "too"
        "big"
        "First"
        "he"
        "sees"
        "dogs"
        "and"
        "cats"
        "Baby"
        "dogs"
        "are"
        "called"
        "puppies"
        "Baby"
        "cats"
        "are"
        "called"
        "kittens"
        "George"
        "likes"
        "them"
        "because"
        "they"
        "are"
        "easy"
        "to"
        "take"
        "care"
        "of"
        "and"
        "can"
        "play"
        "a"
        "lot"
        "but"
        "they"
        "will"
        "get"
        "bigger"
        "George"
        "wants"
        "a"
        "small"
        "pet"
        "Are"
        "are"
        "George"
        "A"
        "called"))

;; Word Frequency of LIST-2 up to 11:
;; are 6
;; george 5
;; a 4
;; called 3
;; pet 3
;; he 3
;; but 2
;; his 2
;; dogs 2
;; because 2
;; they 2

(define LIST-3
  (list "The"
        "deadliest"
        "virus"
        "in"
        "modern"
        "history"
        "perhaps"
        "of"
        "all"
        "time"
        "was"
        "the"
        "1918"
        "Spanish"
        "Flu"
        "It"
        "killed"
        "about"
        "20"
        "to"
        "50"
        "million"
        "people"
        "worldwide"
        "perhaps"
        "more"
        "The"
        "total"
        "death"
        "toll"
        "is"
        "unknown"
        "because"
        "medical"
        "records"
        "were"
        "not"
        "kept"
        "in"
        "many"
        "areas"
        "The"
        "pandemic"
        "hit"
        "during"
        "world"
        "War"
        "I"
        "and"
        "devastated"
        "military"
        "troops"
        "In"
        "the"
        "United"
        "States"
        "for"
        "instance"
        "more"
        "servicemen"
        "were"
        "killed"
        "from"
        "the"
        "flu"
        "than"
        "from"
        "the"
        "war"
        "itself"
        "The"
        "Spanish"
        "flu"
        "was"
        "fatal"
        "to"
        "a"
        "higher"
        "proportion"
        "of"
        "young"
        "adults"
        "than"
        "most"
        "flu"
        "viruses"
        "FLU"
        "flu"
        "fLu"
        "in"
        "IN"
        "iN"
        "than"
        "THAN"
        "tHaN"
        "from"
        "from"
        "were"))

;; Word Frequency of LIST-3 up to 11:
;; the 8
;; flu 7
;; in 6
;; than 5
;; from 4
;; were 3
;; perhaps 2
;; of 2
;; spanish 2
;; more 2
;; was 2

(define LIST-4 (list))

;; top-n-words : Int [List-of String] -> [List-of String]
;; (top-n-words n word-list) produces the n most frequent words in the given
;; word-list

(define (top-n-words n word-list)
  (local [;; comp-length : [List-of String] [List-of String] -> Boolean
          ;; Outputs true if the first list is longer than the next list
          (define (comp-length los1 los2)
            (cond
              [(> (length los1) (length los2)) #true]
              [(< (length los1) (length los2)) #false]
              [(= (length los1) (length los2)) #false]))]
         (cond
           [(empty? word-list) '()]
           [(cons? word-list)
            (take-n n
                    (map first
                         (sort (group-by string=? (sort (map string-upcase word-list) string<?))
                               comp-length)))])))

(check-expect (top-n-words 0 LIST-1) (list))
(check-expect (top-n-words 1 LIST-1) (list "MY"))
(check-expect (top-n-words 4 LIST-1) (list "MY" "COLLEGE" "AND" "I"))
(check-expect (top-n-words 11 LIST-1)
              (list "MY" "COLLEGE" "AND" "I" "IN" "ARE" "IS" "TO" "19" "A" "AM"))
(check-expect (top-n-words 0 LIST-2) (list))
(check-expect (top-n-words 1 LIST-2) (list "ARE"))
(check-expect (top-n-words 4 LIST-2) (list "ARE" "GEORGE" "A" "CALLED"))
(check-expect (top-n-words 11 LIST-2)
              (list "ARE" "GEORGE" "A" "CALLED" "HE" "PET" "AND" "AT" "BABY" "BECAUSE" "BUT"))
(check-expect (top-n-words 0 LIST-3) (list))
(check-expect (top-n-words 1 LIST-3) (list "THE"))
(check-expect (top-n-words 4 LIST-3) (list "THE" "FLU" "IN" "THAN"))
(check-expect (top-n-words 11 LIST-3)
              (list "THE" "FLU" "IN" "THAN" "FROM" "WERE" "KILLED" "MORE" "OF" "PERHAPS" "SPANISH"))
(check-expect (top-n-words 0 LIST-4) (list))
(check-expect (top-n-words 1 LIST-4) (list))
(check-expect (top-n-words 4 LIST-4) (list))
(check-expect (top-n-words 11 LIST-4) (list))

;; word-frequency : [List-of String] [List-of String] -> [List-of Int]
;; (word-frequency top-words word-list) produces a list that counts
;; the number of occurrences of each word in top-words in word-list.
;; The list.

(define (word-frequency top-words word-list)
  (local [;; top-word? : String -> Boolean
          ;; Checks if s is in word-list
          (define (top-word? s)
            (member? s (map string-upcase word-list)))
          ;; count-freq : String -> Int
          ;; Counts how often s appears in word-list if s is in word-list
          (define (count-freq s)
            (cond
              [(top-word? s) (length (filter (λ (x) (string=? x s)) (map string-upcase word-list)))]
              [(empty? s) '()]
              [else 0]))]
         (map count-freq (map string-upcase top-words))))

(check-expect (word-frequency (list "my" "IN" "" "lollipop" "cOllEge") LIST-1) (list 6 3 0 0 5))
(check-expect (word-frequency (list "hi" "bye" "GEOMETRY" "10" "And" "hardest" "are") LIST-1)
              (list 1 0 1 0 4 1 2))
(check-expect (word-frequency (list "pumpkin") LIST-1) (list 0))
(check-expect (word-frequency (list "college") LIST-1) (list 5))
(check-expect (word-frequency (list) LIST-1) (list))
(check-expect
 (word-frequency (list "GEORGE" "george" "thwack" "roasted" "they" "kittens" "bobsled") LIST-2)
 (list 5 5 0 0 2 1 0))
(check-expect (word-frequency (list "pickle" "are" "George" "a" "called" "pet" "p") LIST-2)
              (list 0 6 5 4 3 3 0))
(check-expect (word-frequency (list "pumpkin") LIST-2) (list 0))
(check-expect (word-frequency (list "dogs") LIST-2) (list 2))
(check-expect (word-frequency (list) LIST-2) (list))
(check-expect (word-frequency (list "20" "was" "more" "than" "list" "square" "50") LIST-3)
              (list 1 2 2 5 0 0 1))
(check-expect (word-frequency (list "unknown" "slay" "SERVE" "work" "FROM" "PERhaps" "lolz") LIST-3)
              (list 1 0 0 0 4 2 0))
(check-expect (word-frequency (list "pumpkin") LIST-3) (list 0))
(check-expect (word-frequency (list "iN") LIST-3) (list 6))
(check-expect (word-frequency (list) LIST-3) (list))
(check-expect (word-frequency (list "20" "was" "more" "than" "list" "square" "50") LIST-4)
              (list 0 0 0 0 0 0 0))
(check-expect (word-frequency (list "unknown" "slay" "SERVE" "work" "FROM" "PERhaps" "lolz") LIST-4)
              (list 0 0 0 0 0 0 0))
(check-expect (word-frequency (list "pumpkin") LIST-4) (list 0))
(check-expect (word-frequency (list "iN") LIST-4) (list 0))
(check-expect (word-frequency (list) LIST-4) (list))

;; document-distance : [List-of String] [List-of String] [List-of String] -> Number
;; (document-distance top-words word-list-1 word-list-2) calculates the
;; distance between the word frequency lists of the given files.

(define (document-distance top-words word-list-1 word-list-2)
  (local [(define word-frequency-1 (word-frequency top-words word-list-1))
          (define word-frequency-2 (word-frequency top-words word-list-2))]
         (sqrt (foldr + 0 (map (λ (x y) (expt (- x y) 2)) word-frequency-1 word-frequency-2)))))

(check-within (document-distance (list "THE" "IN" "A" "AND" "MY") LIST-1 LIST-1) 0 0)
(check-within (document-distance (list "THE" "IN" "A" "AND" "MY") LIST-1 (append LIST-1 (list "THE")))
              1
              0)
(check-within
 (document-distance (list "THE" "IN" "A" "AND" "MY") LIST-1 (append LIST-1 (list "pizza")))
 0
 0)
(check-within (document-distance (list "THE" "IN" "A" "AND" "MY") LIST-1 LIST-2)
              #i7.681145747868608
              0)
(check-within (document-distance (list "THE" "IN" "A" "AND" "MY") LIST-1 LIST-3)
              #i10.862780491200215
              0)
(check-within (document-distance (list "THE" "IN" "A" "AND" "MY") LIST-1 LIST-4)
              #i7.874007874011811
              0)
(check-within (document-distance (list "THE" "IN" "A" "AND" "MY") LIST-2 LIST-1)
              #i7.681145747868608
              0)
(check-within (document-distance (list "THE" "IN" "A" "AND" "MY") LIST-2 LIST-2) 0 0)
(check-within
 (document-distance (list "THE" "IN" "A" "AND" "MY") LIST-2 (append LIST-2 (list "A" "AND" "IN" "A")))
 #i2.449489742783178
 0)
(check-within (document-distance (list "THE" "IN" "A" "AND" "MY") LIST-2 LIST-3)
              #i9.746794344808963
              0)
(check-within (document-distance (list "THE" "IN" "A" "AND" "MY") LIST-2 LIST-4) #i4.58257569495584 0)
(check-within (document-distance (list "MORE" "THAN" "CALLED" "THEY" "FROM") LIST-3 LIST-1)
              #i6.708203932499369
              0)
(check-within (document-distance (list "MORE" "THAN" "CALLED" "THEY" "FROM") LIST-3 LIST-2)
              #i7.615773105863909
              0)
(check-within (document-distance (list "MORE" "THAN" "CALLED" "THEY" "FROM") LIST-3 LIST-3) 0 0)
(check-within (document-distance (list "MORE" "THAN" "CALLED" "THEY" "FROM") LIST-3 LIST-4)
              #i6.708203932499369
              0)
(check-within (document-distance (list "MORE" "THAN" "CALLED" "THEY" "FROM") LIST-4 LIST-1) 0 0)
(check-within (document-distance (list "MORE" "THAN" "CALLED" "THEY" "FROM") LIST-4 LIST-2)
              #i3.605551275463989
              0)
(check-within (document-distance (list "MORE" "THAN" "CALLED" "THEY" "FROM") LIST-4 LIST-3)
              #i6.708203932499369
              0)
(check-within (document-distance (list "MORE" "THAN" "CALLED" "THEY" "FROM") LIST-4 LIST-4) 0 0)

;; top-n-words/file : Int PathString -> [List-of String]
;; Applies top-n-words to the contents of a file.

(define (top-n-words/file n path)
  (top-n-words n (read-words path)))

(check-expect (top-n-words/file 0 "THE COMEDY OF ERRORS.txt") (list))
(check-expect (top-n-words/file 1 "THE COMEDY OF ERRORS.txt") (list "OF"))
(check-expect (top-n-words/file 5 "THE COMEDY OF ERRORS.txt") (list "OF" "AND" "THE" "I" "TO"))
(check-expect (top-n-words/file 10 "THE COMEDY OF ERRORS.txt")
              (list "OF" "AND" "THE" "I" "TO" "MY" "A" "YOU" "SYRACUSE." "IN"))
(check-expect (top-n-words/file 0 "VENUS AND ADONIS.txt") (list))
(check-expect (top-n-words/file 1 "VENUS AND ADONIS.txt") (list "THE"))
(check-expect (top-n-words/file 5 "VENUS AND ADONIS.txt") (list "THE" "AND" "TO" "OF" "A"))
(check-expect (top-n-words/file 10 "VENUS AND ADONIS.txt")
              (list "THE" "AND" "TO" "OF" "A" "IN" "HIS" "WITH" "SHE" "HER"))
(check-expect (top-n-words/file 0 "THE TRAGEDY OF MACBETH.txt") (list))
(check-expect (top-n-words/file 1 "THE TRAGEDY OF MACBETH.txt") (list "THE"))
(check-expect (top-n-words/file 5 "THE TRAGEDY OF MACBETH.txt") (list "THE" "AND" "TO" "OF" "I"))
(check-expect (top-n-words/file 10 "THE TRAGEDY OF MACBETH.txt")
              (list "THE" "AND" "TO" "OF" "I" "A" "MACBETH." "IN" "THAT" "MY"))

;; document-distance/file : [List-of String] PathString PathString -> Number
;; Uses document-distance and word-frequency to calculate the distance between
;; two files.

(define (document-distance/file top-words path-1 path-2)
  (document-distance top-words (read-words path-1) (read-words path-2)))

;; A play has a distance of 0 from itself
(check-within (document-distance/file (top-n-words/file 10 "THE TEMPEST.txt")
                                      "MEASURE FOR MEASURE.txt"
                                      "MEASURE FOR MEASURE.txt")
              0
              0)
;; A play has a distance of 0 from itself
(check-within
 (document-distance/file (top-n-words/file 10 "THE TEMPEST.txt") "KING JOHN.txt" "KING JOHN.txt")
 0
 0)
;; A play has a distance of 0 from itself
(check-within (document-distance/file (top-n-words/file 10 "MEASURE FOR MEASURE.txt")
                                      "MEASURE FOR MEASURE.txt"
                                      "MEASURE FOR MEASURE.txt")
              0
              0)
(check-within (document-distance/file (top-n-words/file 10 "THE TEMPEST.txt")
                                      "THE TEMPEST.txt"
                                      "MEASURE FOR MEASURE.txt")
              #i371.74319092620914
              0)
(check-within (document-distance/file (top-n-words/file 20 "KING JOHN.txt")
                                      "THE TEMPEST.txt"
                                      "MEASURE FOR MEASURE.txt")
              #i538.8265026889453
              0)
(check-within (document-distance/file (top-n-words/file 20 "THE TEMPEST.txt")
                                      "THE TEMPEST.txt"
                                      "MEASURE FOR MEASURE.txt")
              #i525.594901040716
              0)
(check-within (document-distance/file (top-n-words/file 10 "THE TEMPEST.txt")
                                      "KING JOHN.txt"
                                      "MEASURE FOR MEASURE.txt")
              #i307.01954335188503
              0)
(check-within (document-distance/file (top-n-words/file 10 "THE TEMPEST.txt")
                                      "THE FIRST PART OF HENRY THE SIXTH.txt"
                                      "THE SECOND PART OF KING HENRY THE SIXTH.txt")
              #i360.39284121635933
              0)
;; Another historical play is more similar to a historical play than a comedy
(check-within (document-distance/file (top-n-words/file 10 "THE TEMPEST.txt")
                                      "THE FIRST PART OF HENRY THE SIXTH.txt"
                                      "AS YOU LIKE IT.txt")
              #i1471.0615214871198
              0)
(check-within (document-distance/file (top-n-words/file 10 "CYMBELINE.txt")
                                      "THE FIRST PART OF HENRY THE SIXTH.txt"
                                      "AS YOU LIKE IT.txt")
              #i1487.5281509941249
              0)
(check-within (document-distance/file (top-n-words/file 25 "THE COMEDY OF ERRORS.txt")
                                      "THE COMEDY OF ERRORS.txt"
                                      "KING HENRY THE EIGHTH.txt")
              #i790.5232444400355
              0)
;; A tragedy is closer to a comedy than a historical play
(check-within (document-distance/file (top-n-words/file 25 "THE COMEDY OF ERRORS.txt")
                                      "THE COMEDY OF ERRORS.txt"
                                      "THE TRAGEDY OF ROMEO AND JULIET.txt")
              #i742.8754942788192
              0)

;; distance-from-others : PathString PathString PathString Number -> [List-of String]
;; Calculates the distance between w1 and the other works (w2 and w3)
;; Uses the top n words in w1 to compare w1 and w2 & w1 and w3
;; Produces the list of works' paths sorted by distance order from closest to farthest

(define (distance-from-others path1 path2 path3 n)
  (local [(define (comp-distance x1 x2)
            (< (document-distance/file (top-n-words/file n path1) path1 x1)
               (document-distance/file (top-n-words/file n path1) path1 x2)))]
         (sort (list path1 path2 path3) comp-distance)))

;; The closest play to a play is itself
(check-expect (distance-from-others "AS YOU LIKE IT.txt" "AS YOU LIKE IT.txt" "KING JOHN.txt" 10)
              (list "AS YOU LIKE IT.txt" "AS YOU LIKE IT.txt" "KING JOHN.txt"))
(check-expect (distance-from-others "AS YOU LIKE IT.txt" "AS YOU LIKE IT.txt" "AS YOU LIKE IT.txt" 10)
              (list "AS YOU LIKE IT.txt" "AS YOU LIKE IT.txt" "AS YOU LIKE IT.txt"))
;; Two historical plays are closer in distance
(check-expect (distance-from-others "THE FIRST PART OF HENRY THE SIXTH.txt"
                                    "AS YOU LIKE IT.txt"
                                    "THE SECOND PART OF KING HENRY THE SIXTH.txt"
                                    10)
              (list "THE FIRST PART OF HENRY THE SIXTH.txt"
                    "THE SECOND PART OF KING HENRY THE SIXTH.txt"
                    "AS YOU LIKE IT.txt"))
(check-expect (distance-from-others "KING JOHN.txt"
                                    "CYMBELINE.txt"
                                    "THE TRAGEDY OF HAMLET, PRINCE OF DENMARK.txt"
                                    10)
              (list "KING JOHN.txt" "CYMBELINE.txt" "THE TRAGEDY OF HAMLET, PRINCE OF DENMARK.txt"))
;; Changing n did not change the outcome
(check-expect (distance-from-others "KING JOHN.txt"
                                    "CYMBELINE.txt"
                                    "THE TRAGEDY OF HAMLET, PRINCE OF DENMARK.txt"
                                    20)
              (list "KING JOHN.txt" "CYMBELINE.txt" "THE TRAGEDY OF HAMLET, PRINCE OF DENMARK.txt"))
(check-expect
 (distance-from-others "THE COMEDY OF ERRORS.txt"
                       "KING HENRY THE EIGHTH.txt"
                       "THE TRAGEDY OF ROMEO AND JULIET.txt"
                       25)
 (list "THE COMEDY OF ERRORS.txt" "THE TRAGEDY OF ROMEO AND JULIET.txt" "KING HENRY THE EIGHTH.txt"))
;; Changing n did not change the outcome
(check-expect
 (distance-from-others "THE COMEDY OF ERRORS.txt"
                       "KING HENRY THE EIGHTH.txt"
                       "THE TRAGEDY OF ROMEO AND JULIET.txt"
                       5)
 (list "THE COMEDY OF ERRORS.txt" "THE TRAGEDY OF ROMEO AND JULIET.txt" "KING HENRY THE EIGHTH.txt"))

;; distance-from-others-2 : String PathString Number -> [List-of String]
;; (distance-from-others-2 title path-main n) calculates the distance
;; between w1 and the other works in the path using the top n words in w1
;; Produces the list of titles of works sorted by distance order from closest to farthest

(define (distance-from-others-2 title path-main n)
  (local
   [(define-struct work2 [title contents distance])
    (define extract-main
      (extract-works (table-of-contents (read-lines path-main)) (read-lines path-main)))
    (define extract-title
      (first (filter (λ (x) (string=? (work-title x) (string-upcase title))) extract-main)))
    (define (distance-from x)
      (document-distance (top-n-words n (work-contents extract-title))
                         (work-contents extract-title)
                         (work-contents x)))
    (define (list-titles low)
      (map (λ (x) (work2-title x)) low))
    (define add-distance
      (map (λ (x) (make-work2 (work-title x) (work-contents x) (distance-from x))) extract-main))
    (define (comp-distance x1 x2)
      (< (work2-distance x1) (work2-distance x2)))]
   (list-titles (sort add-distance comp-distance))))

(check-expect (distance-from-others-2 "THE COMEDY OF ERRORS" "pg100.txt" 15)
              (list "THE COMEDY OF ERRORS"
                    "KING RICHARD THE SECOND"
                    "A MIDSUMMER NIGHT’S DREAM"
                    "THE MERCHANT OF VENICE"
                    "THE TEMPEST"
                    "PERICLES, PRINCE OF TYRE"
                    "THE TRAGEDY OF MACBETH"
                    "THE WINTER’S TALE"
                    "THE LIFE OF KING HENRY THE FIFTH"
                    "THE TWO NOBLE KINSMEN"
                    "THE SONNETS"
                    "THE FIRST PART OF KING HENRY THE FOURTH"
                    "THE TRAGEDY OF JULIUS CAESAR"
                    "THE TRAGEDY OF ROMEO AND JULIET"
                    "CYMBELINE"
                    "TWELFTH NIGHT: OR, WHAT YOU WILL"
                    "ALL’S WELL THAT ENDS WELL"
                    "THE RAPE OF LUCRECE"
                    "THE TAMING OF THE SHREW"
                    "KING RICHARD THE THIRD"
                    "THE TRAGEDY OF CORIOLANUS"
                    "THE FIRST PART OF HENRY THE SIXTH"
                    "VENUS AND ADONIS"
                    "THE MERRY WIVES OF WINDSOR"
                    "THE SECOND PART OF KING HENRY THE SIXTH"
                    "THE SECOND PART OF KING HENRY THE FOURTH"
                    "THE THIRD PART OF KING HENRY THE SIXTH"
                    "KING HENRY THE EIGHTH"
                    "AS YOU LIKE IT"
                    "THE LIFE OF TIMON OF ATHENS"
                    "MEASURE FOR MEASURE"
                    "THE TRAGEDY OF TITUS ANDRONICUS"
                    "KING JOHN"
                    "THE TWO GENTLEMEN OF VERONA"
                    "THE TRAGEDY OF KING LEAR"
                    "LOVE’S LABOUR’S LOST"
                    "A LOVER’S COMPLAINT"
                    "THE HISTORY OF TROILUS AND CRESSIDA"
                    "THE TRAGEDY OF HAMLET, PRINCE OF DENMARK"
                    "THE PASSIONATE PILGRIM"
                    "THE TRAGEDY OF OTHELLO, MOOR OF VENICE"
                    "THE PHOENIX AND THE TURTLE"
                    "THE TRAGEDY OF ANTONY AND CLEOPATRA"
                    "MUCH ADO ABOUT NOTHING"))

(check-expect (distance-from-others-2 "THE TRAGEDY OF ROMEO AND JULIET" "pg100.txt" 20)
              (list "THE TRAGEDY OF ROMEO AND JULIET"
                    "CYMBELINE"
                    "TWELFTH NIGHT: OR, WHAT YOU WILL"
                    "ALL’S WELL THAT ENDS WELL"
                    "THE TRAGEDY OF JULIUS CAESAR"
                    "THE TAMING OF THE SHREW"
                    "THE TWO NOBLE KINSMEN"
                    "THE LIFE OF KING HENRY THE FIFTH"
                    "THE FIRST PART OF KING HENRY THE FOURTH"
                    "THE WINTER’S TALE"
                    "THE TRAGEDY OF MACBETH"
                    "PERICLES, PRINCE OF TYRE"
                    "THE TRAGEDY OF KING LEAR"
                    "THE HISTORY OF TROILUS AND CRESSIDA"
                    "THE TRAGEDY OF HAMLET, PRINCE OF DENMARK"
                    "THE MERCHANT OF VENICE"
                    "THE TEMPEST"
                    "THE TRAGEDY OF OTHELLO, MOOR OF VENICE"
                    "KING RICHARD THE SECOND"
                    "THE COMEDY OF ERRORS"
                    "A MIDSUMMER NIGHT’S DREAM"
                    "THE TRAGEDY OF ANTONY AND CLEOPATRA"
                    "THE SONNETS"
                    "THE RAPE OF LUCRECE"
                    "KING RICHARD THE THIRD"
                    "THE TRAGEDY OF CORIOLANUS"
                    "THE FIRST PART OF HENRY THE SIXTH"
                    "VENUS AND ADONIS"
                    "THE MERRY WIVES OF WINDSOR"
                    "THE SECOND PART OF KING HENRY THE SIXTH"
                    "THE SECOND PART OF KING HENRY THE FOURTH"
                    "THE THIRD PART OF KING HENRY THE SIXTH"
                    "KING HENRY THE EIGHTH"
                    "AS YOU LIKE IT"
                    "THE LIFE OF TIMON OF ATHENS"
                    "MEASURE FOR MEASURE"
                    "THE TRAGEDY OF TITUS ANDRONICUS"
                    "KING JOHN"
                    "THE TWO GENTLEMEN OF VERONA"
                    "LOVE’S LABOUR’S LOST"
                    "A LOVER’S COMPLAINT"
                    "THE PASSIONATE PILGRIM"
                    "THE PHOENIX AND THE TURTLE"
                    "MUCH ADO ABOUT NOTHING"))

(check-expect (distance-from-others-2 "KING HENRY THE EIGHTH" "pg100.txt" 25)
              (list "KING HENRY THE EIGHTH"
                    "AS YOU LIKE IT"
                    "THE LIFE OF TIMON OF ATHENS"
                    "THE THIRD PART OF KING HENRY THE SIXTH"
                    "THE SECOND PART OF KING HENRY THE FOURTH"
                    "MEASURE FOR MEASURE"
                    "THE TRAGEDY OF TITUS ANDRONICUS"
                    "THE SECOND PART OF KING HENRY THE SIXTH"
                    "KING JOHN"
                    "THE TWO GENTLEMEN OF VERONA"
                    "THE MERRY WIVES OF WINDSOR"
                    "THE FIRST PART OF HENRY THE SIXTH"
                    "VENUS AND ADONIS"
                    "THE TRAGEDY OF CORIOLANUS"
                    "LOVE’S LABOUR’S LOST"
                    "KING RICHARD THE THIRD"
                    "THE RAPE OF LUCRECE"
                    "A LOVER’S COMPLAINT"
                    "THE PASSIONATE PILGRIM"
                    "THE PHOENIX AND THE TURTLE"
                    "THE SONNETS"
                    "A MIDSUMMER NIGHT’S DREAM"
                    "THE COMEDY OF ERRORS"
                    "KING RICHARD THE SECOND"
                    "THE MERCHANT OF VENICE"
                    "THE TEMPEST"
                    "PERICLES, PRINCE OF TYRE"
                    "THE TRAGEDY OF MACBETH"
                    "THE WINTER’S TALE"
                    "THE LIFE OF KING HENRY THE FIFTH"
                    "THE TWO NOBLE KINSMEN"
                    "THE FIRST PART OF KING HENRY THE FOURTH"
                    "THE TRAGEDY OF JULIUS CAESAR"
                    "THE TRAGEDY OF ROMEO AND JULIET"
                    "CYMBELINE"
                    "TWELFTH NIGHT: OR, WHAT YOU WILL"
                    "ALL’S WELL THAT ENDS WELL"
                    "THE TAMING OF THE SHREW"
                    "THE TRAGEDY OF KING LEAR"
                    "THE HISTORY OF TROILUS AND CRESSIDA"
                    "THE TRAGEDY OF HAMLET, PRINCE OF DENMARK"
                    "THE TRAGEDY OF OTHELLO, MOOR OF VENICE"
                    "THE TRAGEDY OF ANTONY AND CLEOPATRA"
                    "MUCH ADO ABOUT NOTHING"))
