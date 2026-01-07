/** 
The card class creates and defines the basic components of a generic playing card, designating the suit and
rank in integer value with an additional toString and compareTo methods that allow the game to occur in an 
accurate, user friendly manner. 
*/

public class Card implements Comparable<Card> {

    private int suit; // 1..4
    private int rank; // 1..13 where Ace is 1, Jack 11, Queen 12, King 13

    // named constants for clarity
    public static final int ACE = 1;
    public static final int JACK = 11;
    public static final int QUEEN = 12;
    public static final int KING = 13;

    public Card(int s, int r) {
        this.suit = s;
        this.rank = r;
    }

    public int getSuit() {
        return suit;
    }

    public int getRank() {
        return rank;
    }

    /**
     * Compare first by suit then by rank. This is useful for stable sorting.
     */
    @Override
    public int compareTo(Card c) {
        if (this.suit < c.suit) return -1;
        if (this.suit > c.suit) return 1;
        if (this.rank < c.rank) return -1;
        if (this.rank > c.rank) return 1;
        return 0;
    }

    /**
     * Human-friendly string for the card, e.g., "Ace of Spades" or "Ten of Hearts"
     */
	@Override
    public String toString() {
        String rankString;
        String suitString;

        switch (rank) {
            case 1:  rankString = "Ace"; break;
            case 2:  rankString = "Two"; break;
            case 3:  rankString = "Three"; break;
            case 4:  rankString = "Four"; break;
            case 5:  rankString = "Five"; break;
            case 6:  rankString = "Six"; break;
            case 7:  rankString = "Seven"; break;
            case 8:  rankString = "Eight"; break;
            case 9:  rankString = "Nine"; break;
            case 10: rankString = "Ten"; break;
            case 11: rankString = "Jack"; break;
            case 12: rankString = "Queen"; break;
            case 13: rankString = "King"; break;
            default: rankString = "Unknown Rank"; break;
        }

        switch (suit) {
            case 1: suitString = "Hearts"; break;
            case 2: suitString = "Diamonds"; break;
            case 3: suitString = "Clubs"; break;
            case 4: suitString = "Spades"; break;
            default: suitString = "Unknown Suit"; break;
        }

        return rankString + " of " + suitString;
    }
}
