/**
Creates a sandard deck of cards via iteration with a shuffle helper method.
Deal method created for practical implementation in for Game.java. 
*/
import java.util.Random;

public class Deck {

    private Card[] cards = new Card[52];
    private int top; // index of next card to deal (0..51)
    private Random rand;

    public Deck() {
        rand = new Random();
        int idx = 0;
        for (int suit = 1; suit <= 4; suit++) {
            for (int rank = 1; rank <= 13; rank++) {
                cards[idx++] = new Card(suit, rank);
            }
        }
        top = 0;
        shuffle();
    }

    /**
     * Standard shuffle using a temp. Resets top to 0.
     */
    public void shuffle() {
        for (int i = cards.length - 1; i > 0; i--) {
            int randomIndex = rand.nextInt(i + 1);
            Card temp = cards[i];
            cards[i] = cards[randomIndex];
            cards[randomIndex] = temp;
        }
        top = 0;
    }

    /**
     * Deal the top card and advance the top pointer.
     */
    public Card deal() {
        if (top >= cards.length) {
            throw new IllegalStateException("Deck is empty");
        }
        Card c = cards[top];
        cards[top] = null; // optional: help GC
        top++;
        return c;
    }
}
