/**
Creates the primary functions of a player of Video Poker with the ability to add/remove cards and use bets to 
calculate winnings or losses. Further, the class allows the player to replace a card based on an index of one
rather than zero. 
*/ 
import java.util.ArrayList;

public class Player {

    private ArrayList<Card> hand; // the player's cards
    private double bankroll;
    private double bet;
    public static final int HAND_SIZE = 5;
    public double oneWin;

    public Player(){
        bankroll = 50.0;
        this.hand = new ArrayList<>(HAND_SIZE);
    }

    public void addCard(Card c){
        // add the card c to the player's hand
        if (c != null && hand.size() < HAND_SIZE) {
            hand.add(c);
        }
    }

    public void removeCard(Card c){
        // remove the card c from the player's hand
        if (hand.contains(c)) {
            hand.remove(c);
        }
    }

    public void bets(double amt){
        // player makes a bet
        if (amt >= 0 && amt <= bankroll) {
            bet = amt;
            bankroll -= bet;
        }
    }

    public void winnings(double odds){
        //  adjust bankroll if player wins
        oneWin = bet * odds;
        bankroll += oneWin;
        bet = 0;
    }

    public double getBankroll(){
        // return current balance of bankroll
        return bankroll;
    }

    public double getOneWin(){
        return oneWin;
    }

    /**
     * Return the player's hand as an ArrayList (modifiable)
     */
    public ArrayList<Card> getHand() {
        return hand;
    }

    /**
     * Replace card at 1-based position pos (1..HAND_SIZE)
     */
    public void setCardAtIndexOneBased(int pos, Card c) {
        if (pos < 1 || pos > HAND_SIZE) {
            throw new IndexOutOfBoundsException("pos out of range");
        } if (pos <= hand.size()) {
            hand.set(pos - 1, c);
        } else if (hand.size() < HAND_SIZE) {
            // If hand was shorter (shouldn't normally happen), fill
            hand.add(c);
        }
    }

    /**
     * Return card at 1-based position
     */
    public Card getCardAtIndexOneBased(int pos) {
        if (pos < 1 || pos > HAND_SIZE) {
            throw new IndexOutOfBoundsException("pos out of range");
        }
        return hand.get(pos - 1);
    }

    /**
     * Clear the hand
     */
    public void clearHand() {
        hand.clear();
    }
}


