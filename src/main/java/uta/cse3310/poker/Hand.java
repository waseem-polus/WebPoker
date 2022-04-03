package main.java.uta.cse3310.poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import main.java.uta.cse3310.cards.Card;
import main.java.uta.cse3310.cards.CardDeck;
import main.java.uta.cse3310.cards.Value;
import main.java.uta.cse3310.cards.CardDeck.OutOfCardsException;

public class Hand {
    private Card[] cards;
    private HandType handType;
    public enum HandType {
        HIGH_CARD, PAIR, TWO_PAIR, THREE_OF_A_KIND, STRAIGHT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH,
        ROYAL_FLUSH;
    }

    public Hand() {
        this.cards = new Card[5];
    }

    public Hand(Card[] cards) {
        this.cards = cards;
        this.handType = check_type();
    }

    /* Author: Waseem Alkasbutrus
     * Last Updated: 04/01/2022
     * 
     * exchangeCards(index, newCards): exchange the specified cards with new ones dealt from the card deck
     * 
     * Parameters:
     *      int[] index: contains the indexes of all cards that need to be exchanged
     *      CardDeck carddeck: the deck object to allow drawing new cards
     */
    public void exchangeCards(int[] index, CardDeck cardDeck) throws IndexOutOfBoundsException {
        for(int i = 0; i < index.length; i++) {
            if (index[i] < 0 || index[i] > 4) {
                throw new IndexOutOfBoundsException("[Error] Card index must be between 0-4");
            }

            try {
                this.cards[index[i]] = cardDeck.drawCard();
            } catch (OutOfCardsException e) {
                System.out.println(e.getMessage());
            }
        }
        
        this.handType = check_type();
    }

    /* Author: Waseem Alkasbutrus
     * Last Update: 04/1/2022
     * 
     * dealHand(deck): deals 5 cards to the current hand from deck
     * 
     * Parameters:
     *      CardDeck deck: the deck that it will draw from
     */
    public void dealHand(CardDeck deck) {
        try {
            this.cards = deck.dealHand();
        } catch (OutOfCardsException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean is_equal(Hand h) {
        boolean is_equal = true;

        for (int i = 0; i < 5; i++) {
            if (this.cards[i].compareTo(h.cards[i]) != 0) {
                is_equal = false;
                break;
            }
        }

        return is_equal;
    }

    public boolean is_better_than(Hand h) {
        boolean is_better = false;

        if (!this.is_equal(h)) {
            HandType this_type = this.check_type();
            HandType h_type = h.check_type();

            if (this_type != h_type) {
                is_better = this.check_type().ordinal() > h.check_type().ordinal();
            } else {
                if (this_type == HandType.HIGH_CARD || this_type == HandType.STRAIGHT || this_type == HandType.FLUSH
                        || this_type == HandType.STRAIGHT_FLUSH || this_type == HandType.ROYAL_FLUSH) {
                    is_better = this.cards[0].value.ordinal() > h.cards[0].value.ordinal();
                } else if (this_type == HandType.FOUR_OF_A_KIND) {
                    is_better = this.compare_4_kind(h);
                } else if (this_type == HandType.FULL_HOUSE) {
                    is_better = this.compare_full_house(h);
                } else if (this_type == HandType.THREE_OF_A_KIND) {
                    is_better = this.compare_3_kind(h);
                } else if (this_type == HandType.TWO_PAIR) {
                    is_better = this.compare_2_pair(h);
                } else {
                    is_better = this.compare_pair(h);
                }
            }
        }

        return is_better;
    }

    private boolean compare_4_kind(Hand h) {
        Card this_4_kind = this.cards[0];
        Card this_single = this.cards[4];
        if (this.cards[0].compareTo(this.cards[1]) != 0) {
            this_4_kind = this.cards[4];
            this_single = this.cards[0];
        }

        Card h_4_kind = h.cards[0];
        Card h_single = h.cards[1];
        if (h.cards[0].compareTo(h.cards[1]) != 0) {
            h_4_kind = h.cards[4];
            h_single = h.cards[0];
        }

        boolean is_better = this_4_kind.compareTo(h_4_kind) > 0;
        if (this_4_kind.compareTo(h_4_kind) == 0) {
            is_better = this_single.compareTo(h_single) > 0;
        }

        return is_better;
    }

    private boolean compare_full_house(Hand h) {
        Card this_3_kind = this.cards[0];
        Card this_pair = this.cards[4];
        if (this.cards[0].compareTo(this.cards[2]) != 0) {
            this_3_kind = this.cards[4];
            this_pair = this.cards[0];
        }

        Card h_3_kind = h.cards[0];
        Card h_pair = h.cards[4];
        if (h.cards[0].compareTo(h.cards[2]) != 0) {
            h_3_kind = h.cards[4];
            h_pair = h.cards[0];
        }

        boolean is_better = this_3_kind.compareTo(h_3_kind) > 0;
        if (this_3_kind.compareTo(h_3_kind) == 0) {
            is_better = this_pair.compareTo(h_pair) > 0;
        }

        return is_better;
    }

    private boolean compare_3_kind(Hand h) {
        Card this_3_kind = this.cards[2];
        Card this_top_single = this.cards[3];
        Card this_bottom_single = this.cards[4];
        if (this.cards[0].compareTo(this_3_kind) != 0 && this.cards[1].compareTo(this_3_kind) == 0) {
            this_top_single = this.cards[0];
            this_bottom_single = this.cards[4];
        } else if (this.cards[0].compareTo(this_3_kind) != 0 && this.cards[4].compareTo(this_3_kind) == 0) {
            this_top_single = this.cards[0];
            this_bottom_single = this.cards[1];
        }

        Card h_3_kind = h.cards[2];
        Card h_top_single = h.cards[3];
        Card h_bottom_single = h.cards[4];
        if (h.cards[0].compareTo(h_3_kind) != 0 && h.cards[1].compareTo(h_3_kind) == 0) {
            h_top_single = h.cards[0];
            h_bottom_single = h.cards[4];
        } else if (h.cards[0].compareTo(h_3_kind) != 0 && h.cards[4].compareTo(h_3_kind) == 0) {
            h_top_single = h.cards[0];
            h_bottom_single = h.cards[1];
        }

        boolean is_better = this_3_kind.compareTo(h_3_kind) > 0;
        if (this_3_kind.compareTo(h_3_kind) == 0 && this_top_single.compareTo(h_top_single) != 0) {
            is_better = this_top_single.compareTo(h_top_single) > 0;
        } else if (this_3_kind.compareTo(h_3_kind) == 0 && this_top_single.compareTo(h_top_single) == 0) {
            is_better = this_bottom_single.compareTo(h_bottom_single) > 0;
        }

        return is_better;
    }

    private boolean compare_2_pair(Hand h) {
        Card this_top_pair = this.cards[0];
        Card this_bottom_pair = this.cards[2];
        Card this_single = this.cards[4];
        if (this.cards[0].compareTo(this.cards[1]) == 0 && this.cards[2].compareTo(this.cards[3]) != 0) {
            this_bottom_pair = this.cards[4];
            this_single = this.cards[2];
        } else if (this.cards[0].compareTo(this.cards[1]) != 0) {
            this_top_pair = this.cards[2];
            this_bottom_pair = this.cards[4];
            this_single = this.cards[0];
        }

        Card h_top_pair = h.cards[2];
        Card h_bottom_pair = h.cards[3];
        Card h_single = h.cards[4];
        if (h.cards[0].compareTo(h.cards[1]) == 0 && h.cards[2].compareTo(h.cards[3]) != 0) {
            h_bottom_pair = h.cards[4];
            h_single = h.cards[2];
        } else if (h.cards[0].compareTo(h.cards[1]) != 0) {
            h_top_pair = h.cards[2];
            h_bottom_pair = h.cards[4];
            h_single = h.cards[0];
        }

        boolean is_better = this_top_pair.compareTo(h_top_pair) > 0;
        if (this_top_pair.compareTo(h_top_pair) == 0 && this_bottom_pair.compareTo(h_bottom_pair) != 0) {
            is_better = this_bottom_pair.compareTo(h_bottom_pair) > 0;
        } else if (this_top_pair.compareTo(h_top_pair) == 0 && this_bottom_pair.compareTo(h_bottom_pair) == 0) {
            is_better = this_single.compareTo(h_single) > 0;
        }

        return is_better;
    }

    // 6 6 4 3 2
    // 6 4 3 3 2
    // 6 4 3 2 2
    private boolean compare_pair(Hand h) {
        Card this_pair = this.cards[0];
        Card[] this_singles = { this.cards[2], this.cards[3], this.cards[4] };
        if (this.cards[1].compareTo(this.cards[2]) == 0) {
            this_pair = this.cards[1];
            this_singles[0] = this.cards[0];
        } else if (this.cards[2].compareTo(this.cards[3]) == 0) {
            this_pair = this.cards[2];
            this_singles[0] = this.cards[0];
            this_singles[1] = this.cards[1];
        } else if (this.cards[3].compareTo(this.cards[4]) == 0) {
            this_pair = this.cards[3];
            this_singles[0] = this.cards[0];
            this_singles[1] = this.cards[1];
            this_singles[2] = this.cards[2];
        }

        Card h_pair = h.cards[0];
        Card[] h_singles = { h.cards[2], h.cards[3], h.cards[4] };
        if (h.cards[1].compareTo(h.cards[2]) == 0) {
            h_pair = h.cards[1];
            h_singles[0] = h.cards[0];
        } else if (h.cards[2].compareTo(h.cards[3]) == 0) {
            h_pair = h.cards[2];
            h_singles[0] = h.cards[0];
            h_singles[1] = h.cards[1];
        } else if (h.cards[3].compareTo(h.cards[4]) == 0) {
            h_pair = h.cards[3];
            h_singles[0] = h.cards[0];
            h_singles[1] = h.cards[1];
            h_singles[2] = h.cards[2];
        }

        boolean is_better = this_pair.compareTo(h_pair) > 0;
        if (this_pair.compareTo(h_pair) == 0) {
            for (int i = 0; i < 3; i++) {
                if (this_singles[i].compareTo(h_singles[i]) != 0 || i == 2) {
                    is_better = this_singles[i].compareTo(h_singles[i]) > 0;
                    break;
                }
            }
        }

        return is_better;
    }

    protected ArrayList<Card> highest_unique_card() {
        ArrayList<Card> unique_cards = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            if (this.cards[i].compareTo(this.cards[i + 1]) != 0) {
                unique_cards.add(this.cards[i]);
                if (i == 3) {
                    unique_cards.add(this.cards[4]);
                }
            }
        }

        return unique_cards;
    }

    public HandType check_type() {
        Arrays.sort(this.cards, Collections.reverseOrder());

        HandType type = HandType.HIGH_CARD;

        boolean straight = true;
        boolean flush = true;

        int[] kinds = { 1, 1 };
        int kind_index = 0;

        for (int i = 0; i < 4; i++) {
            if (this.cards[i].compareTo(this.cards[i + 1]) != 1) {
                straight = false;
            }

            if (this.cards[i].compareTo(this.cards[i + 1]) == 0) {
                kinds[kind_index]++;
            } else if (kinds[0] > 1 && this.cards[i].compareTo(this.cards[i + 1]) != 0) {
                kind_index++;
            }

            if (!this.cards[i].compareSuiteTo(this.cards[i + 1])) {
                flush = false;
            }
        }

        if (straight && flush && (this.cards[0].value == Value.ACE)) {
            type = HandType.ROYAL_FLUSH;
        } else if (straight && flush) {
            type = HandType.STRAIGHT_FLUSH;
        } else if (kinds[0] == 4) {
            type = HandType.FOUR_OF_A_KIND;
        } else if ((kinds[0] == 2 && kinds[1] == 3) || (kinds[0] == 3 && kinds[1] == 2)) {
            type = HandType.FULL_HOUSE;
        } else if (!straight && flush) {
            type = HandType.FLUSH;
        } else if (straight && !flush) {
            type = HandType.STRAIGHT;
        } else if (kinds[0] == 3 && kinds[1] == 1) {
            type = HandType.THREE_OF_A_KIND;
        } else if (kinds[0] == 2 && kinds[1] == 2) {
            type = HandType.TWO_PAIR;
        } else if (kinds[0] == 2 && kinds[1] == 1) {
            type = HandType.PAIR;
        } else {
            type = HandType.HIGH_CARD;
        }

        return type;
    }

    public Hand duplicate() {
        return new Hand(this.cards);
    }

    @Override
    public String toString() {
        StringBuilder hand = new StringBuilder(
                this.check_type().toString() + " (" + this.check_type().ordinal() + ")\n");

        for (Card c : this.cards) {
            hand.append(c.toString() + "\n");
        }

        return hand.toString();
    }
}