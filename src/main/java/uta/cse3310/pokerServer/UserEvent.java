package main.java.uta.cse3310.pokerServer;

public class UserEvent {
    public UserEventType event;
    public int playerID;
    public String roomID;
    public Object msg; //Can be cast down based on event
    public Object[] msgArr; //Can be cast down basec on event
    
    public enum UserEventType {
        RAISE, CALL, CHECK, FOLD, CREATE_ROOM, JOIN_ROOM, START_MATCH, EXCHANGE_CARDS, RESTART_MATCH
    }

    public UserEvent () {
    }
}
