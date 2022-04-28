package uta.cse3310.cards;

public class Card implements Comparable<Card> {
   public final Suite suite;
   public final Value value;
   public final String svg;
   public final String emoji;

   /*
    * Protected constructor to prohibit creating new cards manually.
    * 
    * To get cards use the draw method found in CardDeck.java.
    * 
    * This is done to only allow the standard 52 cards in the game and avoid
    * accidentally creating duplicates.
    */
   protected Card(Value value, Suite suite) {
      this.value = value;
      this.suite = suite;
      this.svg = value.key + suite.key + ".svg";
      this.emoji = value.key + suite.emoji;
   }

   public Card(Value value, Suite suite, String srcCaller) {
      this(value, suite);
      
      System.out.println("[WARNING] " + srcCaller + " created a card outside of a cardDeck. Ignore if used for testing.");
   }

   /*
    * compareSuiteTo(Card): compares suites between two cards
    * 
    * Parameters:
    * Card c: the card to compare against
    * 
    * Returns:
    * true if the two suites match
    * false if the two suites don't match
    */
   public boolean compareSuiteTo(Card c) {
      return this.suite == c.suite;
   }

   /*
    * compareTo(Card c): compares two cards based on their value
    * 
    * Parameters:
    * Card c: the card to compare against
    *
    * Returns:
    * < 0 if this card is higher than c
    * 0 if the two cards are equal
    * > 0 if this card is lower than c
    */
   @Override
   public int compareTo(Card c) {
      return this.value.ordinal() - c.value.ordinal();
   }

   /*
    * toString(): returns the string representation of this card
    * 
    * Returns:
    * "[value] of [suite]" (e.g. "TWO of DIAMONDS")
    */
   @Override
   public String toString() {
      return this.value + " of " + this.suite;
   }
}
