package pokerServer;

public class UserEvent {
    public UserEvenType eventType;
    public int playerID;
    public String roomID;
    
    public enum UserEvenType {
        RAISE, CALL, CHECK, FOLD, CREATE_ROOM, JOIN_ROOM
    }

    public UserEvent () {
        
    }
}
