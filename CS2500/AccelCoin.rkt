;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-intermediate-lambda-reader.ss" "lang")((modname brooklyn_schmidt_jane_kamata_hw_a) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))
;"AAAAB3NzaC1yc2EAAAADAQABAAAAQQDAD7Qu8rHV5QklzNEyPF6AkY9/G3p5Lv/ZoLIGhJlo1MDi6EKggZXzAhNVYD7FA4L6IibTscRooy1BkQx3kd1h"
(require "./crypto-extras.rkt")
(require "./hashtable-extras.rkt")
(require "./http-extras.rkt")

(define MY-SECRET
  (string-append
   "MIIBOwIBAAJBAMAPtC7ysdXlCSXM0TI8XoCRj38benku/9mgsgaEmWjUwOLoQqCBlfMCE1VgPsUDgvoiJtO"
   "xxGijLUGRDHeR3WECAwEAAQJAAmBvX2FbeAUkjtYxBIkgtkCW4QDLDDLbsaq+aTw81cI+u/tjoQdNZ57O5XqCfwTn5J9y/DE0X7jloadlsRVYXQ"
   "IhAO7q0EOs7ybjCPIGqTvNsTmK2je0ozhO2y5a3U8MTSdzAiEAzcs7KCvH9Y7NFx2o0Jv4CTBbUCRc98OFR6UJLLH26tsCIQCteBB/ErERqwZz"
   "tCJsKYzkGs0WZTGdUKQTpCXLn0LlwQIgdNpqe9PoVdHBONf5jIKTo7wWjXooY/NckaLvg7aFyzMCIQDnDNX2W5ZMg3GuaexmQ9qnqOoAsDfqK"
   "RMh4jEMdytsng=="))

(define MY-PUBLIC (secret->public MY-SECRET))

#| Immutable Hash Tables:

- A map from keys to values.

;; A [Hash-Table-of X Y] is a (make-hash (list (list X Y) ...))
;; where X is the key and Y is the value that the key maps to

;; make-hash : [List-of [List-of-Two X Y]] -> [Hash-table-of X Y]
;; Creates a hash table from a list of pairs.

;; hash-has-key? : [Hash-table-of X Y] X -> Boolean
;; Checks if a hash table has a key. Returns #true if it does, #false otherwise.

;; hash-ref : [Hash-table-of X Y] X -> Y
;; Returns the value associated with the key in the hash table.

;; hash-set : [Hash-table-of X Y] X Y -> [Hash-table-of X Y]
;; Returns a new hash table with the key mapped to the value.

;; hash-remove : [Hash-table-of X Y] X -> [Hash-table-of X Y]
;; Returns a new hash table with the key removed

;; hash-keys : [Hash-table-of X Y] -> [List-of X]
;; Returns a list of all the keys in the hash table

;; hash-values : [Hash-table-of X Y] -> [List-of Y]
;; Returns a list of all the values in the hash table |#

;; hash-update : [Hash-table-of X Y] X (Y -> Y) Y
;; updates entry using function if present, else default

(define (hash-update h k upd def)
  (hash-set h k (if (hash-has-key? h k) (upd (hash-ref h k)) def)))

#| Cryptography

;; A PrivateKey is a String that represents a 512-bit RSA private key.

;; A PublicKey is a String that represents a 512-bit RSA public key.

;; A Signature is a String that represents a 512-bit RSA signature.

;; digest : String -> Nat
;; Produces the SHA256 digest of the given      . SHA256 is a cryptographic
;; hash function that is used in many blockchains, and we will use it too.

;; secret->public : PrivateKey -> PublicKey
;; Generates a public key from a private key.

;; make-signature : String PrivateKey -> Signature
;; Signs a string with a private key.

;; check-signature : PublicKey String Signature -> Boolean
;; Checks if the given string was signed by the given public key. |#

#| Auxiliary Data Definitions:

;; An [Optional X] is one of:
;; - X
;; - #false
;;
;; Interpretation: Either a value of type X or #false |#

(define-struct transaction [serial unique-string sender-sig sender-key receiver-key amount])
;; A Transaction is a (make-transaction Nat String Signature PublicKey PublicKey Nat)
;;
;; (make-transaction serial unique-string sender-sig sender-key receiver-key amount) represents a
;; single transaction that moves amount accelcoin from sender-key to receiver-key.
;; Moreover:
;;
;; 1. The amount must be positive;
;; 2. The unique-string must be globally unique;
;; 2. The signature signs the string
;;      (string-append unique-string receiver-key ":" (number->string amount))
;;    with the private key corresponding to sender-key.
;; 3. the unique-string is a string that is unique to this transaction.

(define-struct block [transactions nonce miner-key])
;; A Block is a (make-block [List-of Transaction] Nat PublicKey)
;;
;; (make-block transactions nonce miner-key) represents a block of transactions mined by miner-key.
;; The transactions are processed left-to-right. Thus (first transactions) occurs before
;; (second transactions).

;; A Blockchain is a [NE-List-of Block]
;;
;; The first element of a Blockchain is the latest block and the last element is the first
;; block or the *genesis block*. The genesis block has zero transactions and all other blocks have
;; three or more transactions.

#| Transactions Problem #1 |#

;; build-transaction: Nat PrivateKey PublicKey Nat -> Transaction
;; (build-transaction serial sender-private-key receiver-public-key amount) builds a transaction
;; that sends amount from the sender to the receiver.

(define (build-transaction serial sender-private-key receiver-public-key amount)
  (local [(define unique-str (unique-string))]
         (make-transaction
          serial
          unique-str
          (make-signature (string-append unique-str receiver-public-key ":" (number->string amount))
                          sender-private-key)
          (secret->public sender-private-key)
          receiver-public-key
          amount)))

#| Transactions Problem #2 ||#

;; transaction->string : Transaction -> String
;; Serializes a transaction into a string with the format
;; "serial:transaction:unique-string:sender-sig:sender-key:receiver-key,amount"

(define (transaction->string tr)
  (string-append (number->string (transaction-serial tr))
                 ":"
                 "transaction:"
                 (transaction-unique-string tr)
                 ":"
                 (transaction-sender-sig tr)
                 ":"
                 (transaction-sender-key tr)
                 ":"
                 (transaction-receiver-key tr)
                 ","
                 (number->string (transaction-amount tr))))

#| Blocks, Digests, and Mining |#

;; BLOCK EXAMPLES

#| Blocks, Digests, and Mining Problem #2 |#

;; block-digest: Digest Block -> Digest
;; (block-digest prev-digest block) computes the digest of block, given the digest
;; of the previous block.
;;
;; The digest must be the digest of the following strings concatenated in order:
;;
;; 1. prev-digest as a string
;; 2. The transactions as strings (using transaction->string) concatenated in order
;; 3. The nonce as a string

(define DIGEST-LIMIT (expt 2 (* 8 30)))

(define (block-digest prev-digest block)
  (digest (string-append (number->string prev-digest)
                         (foldr string-append "" (map transaction->string (block-transactions block)))
                         (number->string (block-nonce block)))))

#| Blocks, Digests, and Mining |#

;; mine-block : Digest PublicKey [List-of Transaction] Nat -> [Optional Block]
;; (mine-block prev-digest miner-public-key transactions trials)
;; tries to mine a block, but gives up after trials attempts.
;;
;; The produced block has a digest that is less than DIGEST-LIMIT.

(define (mine-block prev-digest miner-public-key transactions trials)
  (local
   ;; RANDNONCE : A random variable between 1-4294967097 (non-inclusive)
   [(define RANDNONCE (random 4294967087))]
   (cond
     [(= trials 0) #false]
     [(< (block-digest prev-digest (make-block transactions RANDNONCE miner-public-key)) DIGEST-LIMIT)
      (make-block transactions RANDNONCE miner-public-key)]
     [else (mine-block prev-digest miner-public-key transactions (- trials 1))])))

#| Blockchain Validation |#

;; A Ledger is a [Hash-Table-of PublicKey Nat]
;; A ledger maps wallet IDs (public keys) to the number of accelcoins they have.

;; reward : PublicKey Ledger -> Ledger
;; Grants the miner the reward for mining a block.

(define (reward pub-key ld)
  (if (hash-has-key? ld pub-key)
      (hash-set ld pub-key (+ 100 (hash-ref ld pub-key)))
      (hash-set ld pub-key 100)))

;; update-ledger/transaction: Transaction Ledger -> [Optional Ledger]
;; Updates the ledger with a single transaction. Produces #false if
;; the sender does not have enough accelcoin to send.

(define (update-ledger/transaction tr ld)
  (cond
    [(boolean? ld) #false]
    [(not (hash-has-key? ld (transaction-sender-key tr))) #false]
    [(or (> (transaction-amount tr) (hash-ref ld (transaction-sender-key tr)))
         (<= (transaction-amount tr) 0))
     #false]
    [(string=? (transaction-sender-key tr) (transaction-receiver-key tr)) ld]
    [(hash-has-key? ld (transaction-receiver-key tr))
     (hash-set (hash-set ld
                         (transaction-sender-key tr)
                         (- (hash-ref ld (transaction-sender-key tr)) (transaction-amount tr)))
               (transaction-receiver-key tr)
               (+ (hash-ref ld (transaction-receiver-key tr)) (transaction-amount tr)))]
    [else
     (hash-set (hash-set ld
                         (transaction-sender-key tr)
                         (- (hash-ref ld (transaction-sender-key tr)) (transaction-amount tr)))
               (transaction-receiver-key tr)
               (transaction-amount tr))]))

;; update-ledger/block : Block Ledger -> [Optional Ledger]
;; Updates the ledger with the transactions in a block, and rewards the miner.

(define (update-ledger/block b ld)
  ;; Constant that produces a ledger by folding update-ledger/transaction onto all transactions in a block
  (local [(define FOLD-LIST->LEDGER (foldl update-ledger/transaction ld (block-transactions b)))]
         (if (boolean? FOLD-LIST->LEDGER) #false (reward (block-miner-key b) FOLD-LIST->LEDGER))))

#| Validating Transactions |#

(define-struct validator-state [ledger pending all-u last-dig])
;; A ValidatorState is a (make-validatorstate [Hash-Table-of PublicKey Nat]
;; [HashTable-of Nat Transaction] [HashTable-of String Boolean] Nat)
;;
;; (make-validatorstate ledger pending all-u last-dig) represents the current ledger,
;; a hashtable of serial numbers that map to pending transactions
;; a hashtable of the unique-strings of each transaction that map to an arbitrary Boolean value (#true)
;; and the digest of the most recent block
;; Note: The last item in the Blockchain is the Genesis-block and the first item is the latest block.

;; EXAMPLES OF VALIDATOR-STATES

(define ACCEL-CHAIN
  (make-validator-state
   (make-hash (list (list (string-append "AAAAB3NzaC1yc2EAAAADAQABAAAAQQDbXz4rfbrR"
                                         "rXYQJbwuCkIyIsccHRpxhxqxgKeneVF4eUXof6e2"
                                         "nLvdXkGA0Y6uBAQ6N7qKxasVTR/2s1N2OBWF")
                          100)))
   (make-hash '())
   (make-hash '())
   0))

;; handle-transaction : ValidatorState Transaction -> [Optional ValidatorState]
;; returns false if a signature is invalid or the transaction is a duplicate (based on unique-string)
;; otherwise, produces a new ValidatorState that records the transaction.
;; Note: the new transaction is not processed until it appears in a block.

(define (handle-transaction vs tr)
  (cond
    [(false? (update-ledger/transaction tr (validator-state-ledger vs))) #false]
    [(and (check-signature (transaction-sender-key tr)
                           (string-append (transaction-unique-string tr)
                                          (transaction-receiver-key tr)
                                          ":"
                                          (number->string (transaction-amount tr)))
                           (transaction-sender-sig tr))
          (not (hash-has-key? (validator-state-all-u vs) (transaction-unique-string tr))))
     (make-validator-state (validator-state-ledger vs)
                           (hash-set (validator-state-pending vs) (transaction-serial tr) tr)
                           (validator-state-all-u vs)
                           (validator-state-last-dig vs))]
    [else #false]))

;; handle-block : ValidatorState Block -> [Optional ValidatorState]
;; Produces a new ValidatorState that adds the Block to the blockchain.
;; returns false if the block digest is invalid,
;; the block has < 3 transactions,
;; update-ledger/block returns false
;; or if any of the transactions are duplicated or altered.

(define (handle-block vs bl)
  (local
   ;; CONSTANT: BLOCK-T : The [List-of Transaction] in a Block
   [(define BLOCK-T (block-transactions bl))
    ;; remove-pending : [List-of Transaction] [HashTable-of Nat Transaction] -> [HashTable-of Nat Transaction]
    ;; removes transactions from the hashtable of pending transactions after they are validated
    (define (remove-pending lot pend-acc)
      (cond
        [(empty? lot) pend-acc]
        [(cons? lot)
         (remove-pending (rest lot) (hash-remove pend-acc (transaction-serial (first lot))))]))
    ;; make-new-all-u : [List-of Transaction] [HashTable-of String Boolean] -> [HashTable-of String Boolean]
    ;; updates the current hash-table of unique strings with the unique-strings from the transactions in a block
    (define (make-new-all-u b-trans acc)
      (cond
        [(empty? b-trans) acc]
        [(cons? b-trans)
         (make-new-all-u (rest b-trans)
                         (hash-set acc (transaction-unique-string (first b-trans)) #true))]))]
   (cond
     [(and (< (block-digest (validator-state-last-dig vs) bl) DIGEST-LIMIT)
           (not (< (length BLOCK-T) 3))
           (not (false? (update-ledger/block bl (validator-state-ledger vs))))
           (andmap (λ (tr) (hash-has-key? (validator-state-pending vs) (transaction-serial tr)))
                   (block-transactions bl)))
      (make-validator-state (update-ledger/block bl (validator-state-ledger vs))
                            (remove-pending BLOCK-T (validator-state-pending vs))
                            (make-new-all-u BLOCK-T (validator-state-all-u vs))
                            (block-digest (validator-state-last-dig vs) bl))]
     [else #false])))

(define (go init-state)
  (blockchain-big-bang init-state [on-transaction handle-transaction] [on-block handle-block]))

(require racket/string)

#| Mining and Sending Blocks |#

;; block->string : Block -> String
;; Serializes a block into a string with the format.

(define (block->string blk)
  (local [(define transactions (block-transactions blk))
          (define transaction-strings
            (map (lambda (t) (string-replace (transaction->string t) ":" ";")) transactions))
          (define transaction-string (string-join transaction-strings ":"))]
         (format "block:~a:~a:~a" (block-nonce blk) (block-miner-key blk) transaction-string)))

#| Mining & Validating |#

;; filter-transactions : ValidatorState -> Transaction
;; Creates a temporary ledger, and filters out all the transactions in the pending-transactions
;; that return false from update-ledger/transaction, while updating the temporary ledger.

(define (filter-transactions st)
  (local [(define txs (validator-state-pending st)) (define txs-values (hash-values txs))]
         (first (foldl (lambda (tx acc)
                         (local [(define lst (first acc))
                                 (define ledger (second acc))
                                 (define updated-ledger (update-ledger/transaction tx ledger))]
                                (if (false? updated-ledger) acc (list (cons tx lst) updated-ledger))))
                       (list '() (validator-state-ledger st))
                       txs-values))))

;; mine+validate : ValidatorState PublicKey Number -> Boolean
;;
;; (mine+validate state miner-key retries)
;;
;; Uses mine-block (from Part 1) to mine the pending transactions in
;; the validator state.
;;
;; Produces #false if the retries are exhausted or if the number of pending
;; transactions is less than three.
;;
;; If mining succeeds, sends the serialized block using post-data and produces
;; #true.

(define (mine+validate vs miner-key retries)
  (local
   ;; CONSTANT: FILTERED : A [HashTable-of Transaction] that filters out all transactions
   ;; that update-ledger/transaction produces #false
   [(define FILTERED (filter-transactions vs))]
   (if (< (length FILTERED) 3)
       #false
       ;; CONSTANT: TRIAL : A [Optional Block] (either #false or a Block)
       (local ((define TRIAL (mine-block (validator-state-last-dig vs) miner-key FILTERED retries)))
              (cond
                [(false? TRIAL) #false]
                [else
                 (post-data
                  "accelchain.api.breq.dev"
                  "/"
                  (block->string
                   (mine-block (validator-state-last-dig vs) miner-key FILTERED retries)))])))))

;; go-miner : ValidatorState PublicId Number -> ValidatorState
;;
;; (go-miner state miner-key retries) mines the pending transactions in state
;; uses `go` to validate the current blockchain, and then recurs indefinitely.

(define (go-miner vs miner-key retries)
  (local [;; CONSTANT: NEW-VS : Represents the new ValidatorState
          (define NEW-VS (go vs))
          ;; CONSTANT: RESULT : Represents either #false or #true (sends block if #true)
          (define RESULT (mine+validate NEW-VS miner-key retries))]
         (go-miner NEW-VS miner-key retries)))
