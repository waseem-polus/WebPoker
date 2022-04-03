package uta.cse3310.cards;

public enum Suite {
    HEARTS("H"), CLUBS("C"), DIAMONDS("D"), SPADES("S");

    public String key;

    private Suite(String key) {
        this.key = key;
    }
}
