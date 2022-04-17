package uta.cse3310.cards;

public enum Suite {
    HEARTS("H", "♥"), CLUBS("C", "♣"), DIAMONDS("D", "♦"), SPADES("S", "♠");

    public String key;
    public String emoji;

    private Suite(String key, String emoji) {
        this.key = key;
        
        if (this.key.equals("H") || this.key.equals("D")) {
            this.emoji = "<FONT COLOR=\"#ff0000\">" + emoji + "</FONT>";
        } else {
            this.emoji = emoji;
        }
    }
}
