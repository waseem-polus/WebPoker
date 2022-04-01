package pokerServer;



public class UserEvent {
    public UserEventType event;
    public int playerID;
    public String roomID;
    public String msg;
    
    public enum UserEventType {
        RAISE, CALL, CHECK, FOLD, CREATE_ROOM, JOIN_ROOM, START_MATCH, EXCHANGE_CARDS
    }

    public UserEvent () {
    }

    // public int[] msgToIntArray() {
    //     return int[];
    // }

    // public double msgToDouble() {

    // }
}
