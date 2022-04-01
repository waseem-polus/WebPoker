package poker;

public class Player {
    private String name;
    private double balance;
    private Hand hand;
    private double currentBet;
    private final int id;

    public Player(int id) {
        this.name = "";
        this.balance = 2000;
        this.hand = new Hand();
        this.currentBet = 0;
        this.id = id;
    }

    public void placeBet(double amount) {
        if (amount <= balance) {
            this.currentBet += amount;
            this.balance -= amount;
        }
    }

    public void setName(String name) {
        this.name = name;
    }
}