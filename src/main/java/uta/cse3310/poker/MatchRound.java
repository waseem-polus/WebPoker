package uta.cse3310.poker;

public enum MatchRound {
    WAITING, FIRST_BETTING, DRAWING, SECOND_BETTING, SHOWDOWN;

    private MatchRound() {}

    /* Author: Waseem Alkasbutrus
     * Last Updated: 4/4/2022
     *
     * next(): returns the next round.
     * 
     * Returns:
     *      MatchRound that follows the current one
     */
    public MatchRound next() {
        return MatchRound.values()[(this.ordinal() + 1)%MatchRound.values().length];
    }
}