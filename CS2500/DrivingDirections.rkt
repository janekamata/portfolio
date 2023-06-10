;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-intermediate-lambda-reader.ss" "lang")((modname brooklyn_schmidt_jane_kamata_hw_9) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))
#| Part 1. Data Definition |#

(define-struct edge [direction end])
;; An Edge is a (make-edge String String String)
;; A (make-edge direction start end) represents the direction to get from start to end,
;; an intersection that represents the starting value,
;; and an intersection that represents the end destination.

;; edge-temp : Edge -> ?

(define (edge-temp e)
  (... (edge-direction e) ... (edge-end e) ...))

(define EDGE-1 (make-edge "north" "Fundies Street & Accel Street"))
(define EDGE-2 (make-edge "east" "Khoury Avenue & Hescott Street"))
(define EDGE-3 (make-edge "west" "Fundies Street & Khoury Avenue"))
(define EDGE-4 (make-edge "north" "Hescott Street & Accel Street"))
(define EDGE-5 (make-edge "south" "Khoury Avenue & Hescott Street"))
(define EDGE-6 (make-edge "west" "Fundies Street & Accel Street"))
(define EDGE-7 (make-edge "south" "Fundies Street & Khoury Avenue"))
(define EDGE-8 (make-edge "east" "Hescott Street & Accel Street"))

(define-struct node [intersection connected])
;; An Node is a (make-node String [List-of Edge])
;; A (make-node intersection connected) represents a street intersection and a
;; list of Edges, that represent all the neighbors of an intersection.

(define (node-temp n)
  (... (node-intersection n) ... (list-template (node-connected n)) ...))

(define NODE-1 (make-node "Fundies Street & Khoury Avenue" (list EDGE-1 EDGE-2)))
(define NODE-2 (make-node "Khoury Avenue & Hescott Street" (list EDGE-3 EDGE-4)))
(define NODE-3 (make-node "Hescott Street & Accel Street" (list EDGE-5 EDGE-6)))
(define NODE-4 (make-node "Fundies Street & Accel Street" (list EDGE-7 EDGE-8)))

;; An ELGraph is a [List-of Node]
;; Represents a list of Nodes that contain every street intersection and its neighbors,
;; as well as directions to get from one intersection to another.

;; graph-temp : ELGraph -> ?

(define (graph-temp g)
  (cond
    [(empty? g) ...]
    [(cons? g) (... (first g) ... (graph-temp (rest g)) ...)]))

(define ELGRAPH1 (list NODE-1 NODE-2 NODE-3 NODE-4))
(define ELGRAPH2
  (list (make-node "Testing Way & Check-Expect Drive"
                   (list (make-edge "north" "Check-Expect Drive & Steast Ave")
                         (make-edge "east" "Testing Way & Steast Ave")))
        (make-node "Testing Way & Steast Drive"
                   (list (make-edge "west" "Testing Way & Check-Expect Drive")
                         (make-edge "northwest" "Testing Way & Check-Expect Drive")))
        (make-node "Check-Expect Drive & Steast Ave"
                   (list (make-edge "southeast" "Testing Way & Steast Ave")
                         (make-edge "south" "Testing Way & Check-Expect Drive")))))

#| Part 2. given-street-graph & my-street-graph |#

;; CONSTANTS

(define given-street-graph
  (list (make-node "Forsyth Way & Hemenway St"
                   (list (make-edge "south" "Forsyth Way & Huntington Ave")
                         (make-edge "east" "Hemenway St & Forsyth St")))
        (make-node "Forsyth Way & Huntington Ave"
                   (list (make-edge "north" "Forsyth Way & Hemenway St")
                         (make-edge "east" "Huntington Ave & Forsyth St")))
        (make-node "Huntington Ave & Forsyth St"
                   (list (make-edge "west" "Forsyth Way & Huntington Ave")
                         (make-edge "north" "Hemenway St & Forsyth St")
                         (make-edge "east" "Huntington Ave & Opera Place")))
        (make-node "Hemenway St & Forsyth St"
                   (list (make-edge "south" "Huntington Ave & Forsyth St")
                         (make-edge "west" "Forsyth Way & Hemenway St")
                         (make-edge "east" "Hemenway St & Gainsborough St")))
        (make-node "Hemenway St & Gainsborough St"
                   (list (make-edge "west" "Hemenway St & Forsyth St")
                         (make-edge "south" "Gainsborough St & St. Stephen St")))
        (make-node "Gainsborough St & St. Stephen St"
                   (list (make-edge "north" "Hemenway St & Gainsborough St")
                         (make-edge "west" "St. Stephen St & Opera Place")))
        (make-node "St. Stephen St & Opera Place"
                   (list (make-edge "east" "Gainsborough St & St. Stephen St")
                         (make-edge "south" "Huntington Ave & Opera Place")))
        (make-node "Huntington Ave & Opera Place"
                   (list (make-edge "north" "St. Stephen St & Opera Place")
                         (make-edge "west" "Huntington Ave & Forsyth St")))))

;; LINK TO MY MY-STREET-GRAPH
;; https://drive.google.com/drive/folders/146ZiNQcyIr1RBwjkmb3b0FHnGEk_ivij?usp=share_link
;; black line = not included (there's no street name for that small road)

(define my-street-graph
  (list (make-node "E Grinnell Dr & N 6th St"
                   (list (make-edge "south" "E Grinnell Dr & N 5th St")
                         (make-edge "east" "N 6th St & E Harvard Rd")))
        (make-node "E Grinnell Dr & N 5th St"
                   (list (make-edge "north" "E Grinnell Dr & N 6th St")
                         (make-edge "east" "N 5th St & E Harvard Rd")))
        (make-node "N 5th St & E Harvard Rd"
                   (list (make-edge "west" "E Grinnell Dr & N 5th St")
                         (make-edge "north" "N 6th St & E Harvard Rd")
                         (make-edge "east" "N 5th St & E Cypress Ave")))
        (make-node "N 6th St & E Harvard Rd"
                   (list (make-edge "south" "N 5th St & E Harvard Rd")
                         (make-edge "west" "E Grinnell Dr & N 6th St")
                         (make-edge "east" "N 6th St & E Cypress Ave")))
        (make-node "N 6th St & E Cypress Ave"
                   (list (make-edge "west" "N 6th St & E Harvard Rd")
                         (make-edge "south" "N 5th St & E Cypress Ave")))
        (make-node "N 5th St & E Cypress Ave"
                   (list (make-edge "north" "N 6th St & E Cypress Ave")
                         (make-edge "west" "N 5th St & E Harvard Rd")))))

(define DFS-TEST-GRAPH
  (list (make-node "1" (list (make-edge "east" "2") (make-edge "west" "4")))
        (make-node "2" (list (make-edge "west" "1") (make-edge "south" "3")))
        (make-node "3" (list (make-edge "north" "2")))
        (make-node "4" (list (make-edge "east" "1") (make-edge "south" "5")))
        (make-node "5" (list (make-edge "north" "4")))))

(define UNCONNECTED-GRAPH
  (list (make-node "1" (list (make-edge "east" "2")))
        (make-node "2" (list (make-edge "west" "1")))
        (make-node "3" (list (make-edge "south" "4")))
        (make-node "4" (list (make-edge "north" "3")))))

#| Part 3: Driving Directions |#

;; An [Optional X] is one of:
;; - X
;; - #false

;; get-node : ELGraph String -> [Optional Node]
;; Retrieves the node that has the given intersection name from the ELGraph.
;; Produces false if the intersection does not exist in the ELGraph.

(check-expect (get-node given-street-graph "Huntington Ave & Opera Place")
              (make-node "Huntington Ave & Opera Place"
                         (list (make-edge "north" "St. Stephen St & Opera Place")
                               (make-edge "west" "Huntington Ave & Forsyth St"))))

(check-expect (get-node my-street-graph "N 6th St & E Cypress Ave")
              (make-node "N 6th St & E Cypress Ave"
                         (list (make-edge "west" "N 6th St & E Harvard Rd")
                               (make-edge "south" "N 5th St & E Cypress Ave"))))

(check-expect (get-node given-street-graph "Arjun Guha Drive & GOAT Professor Avenue") #false)

(check-expect (get-node '() "Graph Drive & DFS Hill") #false)

(define (get-node g name)
  (foldr (λ (n acc) (if (and (node? n) (string=? (node-intersection n) name)) n acc)) #false g))

;; neighbors-of : ELGraph String -> [List-of Node]
;; Produces a list of the neighbor nodes of a node given the intersection of a node in the given ELGraph.

(check-expect (neighbors-of given-street-graph "Forsyth Way & Huntington Ave")
              (list (make-node "Forsyth Way & Hemenway St"
                               (list (make-edge "south" "Forsyth Way & Huntington Ave")
                                     (make-edge "east" "Hemenway St & Forsyth St")))
                    (make-node "Huntington Ave & Forsyth St"
                               (list (make-edge "west" "Forsyth Way & Huntington Ave")
                                     (make-edge "north" "Hemenway St & Forsyth St")
                                     (make-edge "east" "Huntington Ave & Opera Place")))))

(check-expect (neighbors-of my-street-graph "N 5th St & E Cypress Ave")
              (list (make-node "N 6th St & E Cypress Ave"
                               (list (make-edge "west" "N 6th St & E Harvard Rd")
                                     (make-edge "south" "N 5th St & E Cypress Ave")))
                    (make-node "N 5th St & E Harvard Rd"
                               (list (make-edge "west" "E Grinnell Dr & N 5th St")
                                     (make-edge "north" "N 6th St & E Harvard Rd")
                                     (make-edge "east" "N 5th St & E Cypress Ave")))))

(define (neighbors-of g name)
  (map (lambda (l) (get-node g (edge-end l))) (node-connected (get-node g name))))

;; driving-directions : ELGraph String String -> [Optional [List-of String]]
;; Produces a list of directions from the starting intersection to the destination intersection.
;; The variable directions is an accumulator of directions from intersection to intersection.
;; Produces #false if the beginning intersection or ending intersection does not exist in the graph.

(check-expect (driving-directions given-street-graph
                                  "Forsyth Way & Huntington Ave"
                                  "Gainsborough St & St. Stephen St") ;; graph 1
              (list "north to Forsyth Way & Hemenway St"
                    "east to Hemenway St & Forsyth St"
                    "south to Huntington Ave & Forsyth St"
                    "east to Huntington Ave & Opera Place"
                    "north to St. Stephen St & Opera Place"
                    "east to Gainsborough St & St. Stephen St"))

(check-expect
 (driving-directions my-street-graph "E Grinnell Dr & N 6th St" "N 5th St & E Cypress Ave") ;; graph 2
 (list "south to E Grinnell Dr & N 5th St"
       "east to N 5th St & E Harvard Rd"
       "north to N 6th St & E Harvard Rd"
       "east to N 6th St & E Cypress Ave"
       "south to N 5th St & E Cypress Ave"))

(check-expect (driving-directions given-street-graph "Fundies Drive" "Forsyth Way & Huntington Ave")
              #false)
;; begin doesn't exist

(check-expect (driving-directions my-street-graph "E Grinnell Dr & N 6th St" "Fake Alley")
              #false) ;; end doesn't exist

(check-expect
 (driving-directions given-street-graph "Forsyth Way & Huntington Ave" "Forsyth Way & Huntington Ave")
 '()) ;; same begin & end, empty list as you are already at place

(check-expect
 (driving-directions DFS-TEST-GRAPH "1" "5") ;; goes down one path, resets, goes down second
 (list "west to 4" "south to 5"))

(check-expect (driving-directions '() "intersection 1" "intersection 2") #false) ;; empty graph

(define (driving-directions g beginning destination)
  ;; traverse : [List-of Node] [List-of Node] [List-of [List-of String]] -> [Optional [List-of String]]
  (local ((define (traverse pending visited dirs)
            (cond
              [(empty? pending) #false]
              [(false? (first pending)) #false]
              [(cons? pending)
               (local ((define label (node-intersection (first pending)))
                       (define neighbors (node-connected (first pending)))
                       (define dirs-so-far (first dirs))
                       (define (dir-string e)
                         (append dirs-so-far
                                 (list (string-append (edge-direction e) " to " (edge-end e))))))
                      (cond
                        [(member? label visited) (traverse (rest pending) visited (rest dirs))]
                        [(string=? destination label) dirs-so-far]
                        [else
                         (traverse (append (neighbors-of g label) (rest pending))
                                   (cons label visited)
                                   (append (map dir-string neighbors) (rest dirs)))]))])))
         (traverse (list (get-node g beginning)) '() (list '()))))

#| Part 4: fully-connected? |#

(check-expect (path-exists? given-street-graph
                            (make-node "Forsyth Way & Hemenway St"
                                       (list (make-edge "south" "Forsyth Way & Huntington Ave")
                                             (make-edge "east" "Hemenway St & Forsyth St")))
                            (make-node "Forsyth Way & Huntington Ave"
                                       (list (make-edge "north" "Forsyth Way & Hemenway St")
                                             (make-edge "east" "Huntington Ave & Forsyth St"))))
              #true) ;; path exists
(check-expect
 (path-exists?
  given-street-graph
  (make-node "hi" (list (make-edge "northwestwestwest" "computer street")))
  (make-node "Forsyth Way & Huntington Ave"
             (list (make-edge "north" "Forsyth Way & Hemenway St")
                   (make-edge "east" "Huntington Ave & Forsyth St")))) ;; beginning not in g
 #false)

(check-expect (path-exists? given-street-graph
                            (make-node "Forsyth Way & Huntington Ave"
                                       (list (make-edge "north" "Forsyth Way & Hemenway St")
                                             (make-edge "east" "Huntington Ave & Forsyth St")))
                            (make-node "test"
                                       (list (make-edge "arbritrary direction"
                                                        "doesn't exist")))) ;; destination not in g
              #false)

(check-expect
 (path-exists? '()
               (make-node "Forsyth Way & Huntington Ave"
                          (list (make-edge "north" "Forsyth Way & Hemenway St")
                                (make-edge "east" "Huntington Ave & Forsyth St")))
               (make-node "Forsyth Way & Hemenway St"
                          (list (make-edge "south" "Forsyth Way & Huntington Ave")
                                (make-edge "east" "Hemenway St & Forsyth St")))) ;; empty graph
 #false)

(check-expect (path-exists? given-street-graph
                            (make-node "Forsyth Way & Huntington Ave"
                                       (list (make-edge "north" "Forsyth Way & Hemenway St")
                                             (make-edge "east" "Huntington Ave & Forsyth St")))
                            (make-node "Forsyth Way & Huntington Ave"
                                       (list (make-edge "north" "Forsyth Way & Hemenway St")
                                             (make-edge "east" "Huntington Ave & Forsyth St"))))
              #true) ;; same place

(check-expect (path-exists? UNCONNECTED-GRAPH
                            (make-node "1" (list (make-edge "east" "2")))
                            (make-node "4" (list (make-edge "north" "3"))))
              #false) ;; no path exists

;; path-exists? : ELGraph Node Node -> Boolean
;; checks if a path exists between two nodes in the given ELGraph

(define (path-exists? g beginning destination)
  (local ;; traverse : [List-of Node] [List-of String] -> Boolean
   ((define (traverse pending visited)
      (cond
        [(empty? pending) #false]
        [(not (member? beginning g)) #false]
        [(cons? pending)
         (local ((define label (node-intersection (first pending)))
                 (define neighbors (node-connected (first pending))))
                (cond
                  [(member? label visited) (traverse (rest pending) visited)]
                  [(string=? label (node-intersection destination)) #true]
                  [else
                   (traverse (append (rest pending) (neighbors-of g label))
                             (cons label visited))]))])))
   (traverse (list beginning) '())))

;; fully-connected? : ELGraph -> Boolean
;; checks if a path exists between all pairs of points in an ELGraph

(check-expect (fully-connected? given-street-graph) #true) ;; graph 1
(check-expect (fully-connected? my-street-graph) #true) ;; graph 2
(check-expect (fully-connected? UNCONNECTED-GRAPH) #false) ;; unconnected
(check-expect (fully-connected? '()) #true) ;; empty
(check-expect (fully-connected? DFS-TEST-GRAPH) #true) ;; node with 1 neighbor, need to go back up
(check-expect (fully-connected? (list (make-node "By itself" (list '())))) #true) ;; lone node

(define (fully-connected? g)
  (andmap (λ (node1) (andmap (λ (node2) (path-exists? g node1 node2)) g)) g))
