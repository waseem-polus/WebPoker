package pokerServer;

public class UserEvent {
    public UserEvenType event;
    public int playerID;
    public String roomID;
    public String msg;
    
    public enum UserEvenType {
        RAISE, CALL, CHECK, FOLD, CREATE_ROOM, JOIN_ROOM, START_MATCH
    }

    public UserEvent () {

    }
}
