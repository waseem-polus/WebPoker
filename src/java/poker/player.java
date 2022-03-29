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
		this.setCurrentBet();
       		this.setBalance();
		this.setHand(new ArrayList<Hand>());
	                     }
    public double getCurrentBet()
    {
        return currentBet;
    }
    public void setCurrentBet(double currentBet) 
    {
	this.currentBet = currentBet;
    }
    public double getBalance()
    {
        return balance;
    }
    public void setBalance(double balance)
    {
	this.balance = balance;
    }   

    public String getName()
    {
	return name;
    }

     public void setName(String name)
     {
        this.name = name;
     }

    public Hand getHand()
    {
        return hand;
    }
    public void setHand(Hand hand)
    {
	this.hand = hand;
    }
@Override
	public String toString()
	{
		return getName();
	}
}


