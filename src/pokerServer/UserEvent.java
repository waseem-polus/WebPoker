package pokerServer;

public class UserEvent {
    public UserEventType event;
    public int playerID;
    public String roomID;
    public Object msg;
    public Object[] msgArr;
    
    public enum UserEventType {
        RAISE, CALL, CHECK, FOLD, CREATE_ROOM, JOIN_ROOM, START_MATCH, EXCHANGE_CARDS, RESTART_MATCH
    }

    public UserEvent () {
    }
}
