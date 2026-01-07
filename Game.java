/**
Primary class for Video Poker. Creates a test hand and a default constructor, with a play method that
facilitates the game accoriding to the provided outline with the printHand (formats the hand property for 
the user), evaluateHand (calculates the winnings of the hand), and getMultiplier (finds the multiplier 
according to the hand's value) helper methods. The test constuctor utilizes a parseCardCode method to 
successfully make a hand for the player with the desired conditions.
*/
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Game {

    private Deck deck;
    private Player player;
    private Scanner input;

    /**
     * Default constructor
     */
    public Game() {
        this.deck = new Deck();
        this.player = new Player();
        this.input = new Scanner(System.in);
    }

    /**
     * This constructor is to help test your code.
     * use the contents of testHand to
     * make a hand for the player
     * encoding:
     * c = clubs
     * d = diamonds
     * h = hearts
     * s = spades
     * 1-13 correspond to ace-king
     * example: s1 = ace of spades
     * example: testhand = {s1, s13, s12, s11, s10} = royal flush
     */
    public Game(String[] testHand){
        this.deck = new Deck();
        this.player = new Player();
        this.input = new Scanner(System.in);

        // ensure deck shuffled
        deck.shuffle();

        // parse provided hand codes up to HAND_SIZE
        if (testHand != null) {
            int toUse = Math.min(Player.HAND_SIZE, testHand.length);
            for (int i = 0; i < toUse; i++) {
                Card c = parseCardCode(testHand[i]);
                if (c != null) {
                    player.addCard(c);
                }
            }
        }

        // fill remaining spots from deck
        while (player.getHand().size() < Player.HAND_SIZE) {
            player.addCard(deck.deal());
        }
    }

    /**
     * Parse a two+ character code like "s1" "h13" into a Card. Returns null if invalid.
     */
    private Card parseCardCode(String code) {
        if (code == null) return null;
        code = code.trim().toLowerCase();
        if (code.length() < 2) return null;
        char suitChar = code.charAt(0);
        String rankStr = code.substring(1);
        int suit;
        if (suitChar == 'h') suit = 1;
        else if (suitChar == 'd') suit = 2;
        else if (suitChar == 'c') suit = 3;
        else if (suitChar == 's') suit = 4;
        else return null;

        int rank;
        try {
            rank = Integer.parseInt(rankStr);
        } catch (NumberFormatException e) {
            return null;
        }
        if (rank < 1 || rank > 13) return null;
        return new Card(suit, rank);
    }

    /**
     * Print player's hand in the required format.
     */
    public void printHand() {
        System.out.print("The hand is:   ");
        ArrayList<Card> hand = player.getHand();
        for (int i = 0; i < hand.size(); i++) {
            System.out.print(hand.get(i).toString());
            if (i < hand.size() - 1) System.out.print("  ");
        }
        System.out.println();
    }

    /**
     * Evaluate an ArrayList<Card> hand and return classification string.
     * Uses only arrays and ArrayList (no Maps).
     */
    public static String evaluateHand(ArrayList<Card> handList) {
        // Convert to array
        Card[] hand = handList.toArray(new Card[0]);

        // Sort by rank (Card.compareTo should compare by rank first)
        Arrays.sort(hand);

        // counts of ranks 1..13
        int[] counts = new int[14]; // ignore index 0
        for (int i = 0; i < hand.length; i++) {
            counts[hand[i].getRank()]++;
        }

        // flush check: all suits same?
        boolean isFlush = true;
        int suit0 = hand[0].getSuit();
        for (int i = 1; i < hand.length; i++) {
            if (hand[i].getSuit() != suit0) {
                isFlush = false;
                break;
            }
        }

        // straight check: careful with Ace-low (1,2,3,4,5)
        boolean isStraight = true;
        // Normal consecutive check
        for (int i = 1; i < hand.length; i++) {
            int prev = hand[i-1].getRank();
            int cur = hand[i].getRank();
            if (cur != prev + 1) {
                isStraight = false;
                break;
            }
        }
        // Ace-low special (1,2,3,4,5) - since sorted ascending, check explicitly
        if (!isStraight) {
            boolean aceLow = (hand[0].getRank() == 1 &&
                              hand[1].getRank() == 2 &&
                              hand[2].getRank() == 3 &&
                              hand[3].getRank() == 4 &&
                              hand[4].getRank() == 5);
            if (aceLow) isStraight = true;
        }

        // Count how many of each multiplicity
        int four = 0, three = 0, pairs = 0;
        for (int r = 1; r <= 13; r++) {
            if (counts[r] == 4) four++;
            if (counts[r] == 3) three++;
            if (counts[r] == 2) pairs++;
        }

        // Royal flush detection: flush + straight + ranks include 10,11,12,13,1
        boolean isRoyal = false;
        if (isFlush && isStraight) {
            boolean has10 = counts[10] > 0;
            boolean has11 = counts[11] > 0;
            boolean has12 = counts[12] > 0;
            boolean has13 = counts[13] > 0;
            boolean has1  = counts[1]  > 0;
            if (has10 && has11 && has12 && has13 && has1) {
                isRoyal = true;
            }
        }

        // Decide classification by hierarchy
        if (isRoyal) return "Royal Flush";
        if (isStraight && isFlush) return "Straight Flush";
        if (four > 0) return "Four of a Kind";
        if (three > 0 && pairs > 0) return "Full House";
        if (isFlush) return "Flush";
        if (isStraight) return "Straight";
        if (three > 0) return "Three of a Kind";
        if (pairs == 2) return "Two pairs";
        if (pairs == 1) return "One pair";
        return "No pair";
    }

    /**
     * Map classification to multiplier using only if/else.
     */
    public static int getMultiplier(String classification) {
        if (classification.equals("Royal Flush")) return 250;
        else if (classification.equals("Straight Flush")) return 50;
        else if (classification.equals("Four of a Kind")) return 25;
        else if (classification.equals("Full House")) return 6;
        else if (classification.equals("Flush")) return 5;
        else if (classification.equals("Straight")) return 4;
        else if (classification.equals("Three of a Kind")) return 3;
        else if (classification.equals("Two pairs")) return 2;
        else if (classification.equals("One pair")) return 1;
        else return 0; // "No pair" and default
    }

    public void play() {
        System.out.println("Welcome to Video Poker!");
        System.out.printf("YOUR TOKENS: %.1f%n", player.getBankroll());
        System.out.print("Would you like to play a round? (y/n): ");
        String keepPlaying = input.nextLine();
        while (keepPlaying.equals("y")) {
            int bet = 0; // resets the bet each time
            while (true) {
                System.out.print("How many tokens to bet this hand? (1 to 5): ");
                try {
                    String line = input.nextLine().trim();
                    bet = Integer.parseInt(line);
                    if (bet >= 1 && bet <= 5 && bet <= (int)player.getBankroll()) break;
                } catch (Exception e) {}
            }

            // prepare new hand from deck
            player.clearHand();
            for (int i = 0; i < Player.HAND_SIZE; i++) {
                player.addCard(deck.deal());
            }

            printHand();

            // exchanges
            System.out.print("How many cards (0-5) would you like to exchange? ");
            int exchCount = 0;
            while (true) {
                try {
                    String s = input.nextLine().trim();
                    exchCount = Integer.parseInt(s);
                    if (exchCount >= 0 && exchCount <= 5) break;
                } catch (Exception e) {}
            }

            for (int e = 0; e < exchCount; e++) {
                System.out.print("Which card (1-5) would you like to exchange? ");
                int cardIndex = 0;
                while (true) {
                    try {
                        String s = input.nextLine().trim();
                        cardIndex = Integer.parseInt(s);
                        if (cardIndex >= 1 && cardIndex <= 5) break;
                    } catch (Exception ex) {}
                }
                // replace that card
                player.setCardAtIndexOneBased(cardIndex, deck.deal());
            }

            // final hand
            printHand();

            // evaluate
            String classification = evaluateHand(player.getHand());
            int multiplier = getMultiplier(classification);
            int payout = bet * multiplier;

            // update bankroll: subtract bet then add payout (same net as bet * (multiplier - 1) if you prefer)
            player.bets(bet); // set bet
            player.winnings(multiplier); // winnings will compute bet*odds and add to bankroll

            System.out.printf("You got a %s!%n", classification);
            System.out.printf("PAYOUT: %d tokens%n", payout);
            System.out.printf("YOUR TOKENS: %.1f%n", player.getBankroll());
            System.out.print("Would you like to play a round? (y/n): ");
            keepPlaying = input.nextLine();
        }

        System.out.println("Thank you for playing Video Poker!");
    }
}
