import java.util.ArrayList;
import java.util.List;

public class Player

{


    private String name;
	private Hand hand;
	private double currentBet;
	private double balance;
	private int id;

}
public Player(int id) {
		this.setName("Player " + id;
		
		this.hand(new ArrayList<Hand>());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    public void hand(List<Hand> arrayList) {
		this.hand = arrayList;
	}
                 
}
