import java.util.ArrayList;
import java.util.List;

public class Player

{


    private String name;
	private  Hand hand;
	private double currentBet;
	private double balance;
	private int id;

}
public Player(int id) {
		this.setName("Player " + id);
		
		this.setHand(new ArrayList<Hand>());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public Hand getHand()
    {
        return hand;
    }
    public void setHand(Hand hand) {
		this.hand = hand;
	}
}

