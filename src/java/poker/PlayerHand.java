package java
  import java.lang.* ;
import javax.swing.* ;
public class playerHand extends hand {
    int NO_PAIR = 0 ;
    int PAIR = 1 ;
    int TWO_PAIR = 2 ;
     int THREE_KIND = 3 ;
     int STRAIGHT = 4 ;
      int FLUSH = 5 ;
	 int FULL_HOUSE = 6 ;
	 int FOUR_KIND = 7 ;
	 int STRAIGHT_FLUSH = 8 ;
	 int ROYAL_FLUSH = 9 ;
	 int HAND = 5 ;

}

private card[] Hand;
public Hand()
{
    Hand = new Card[HAND] ;
	for (int i = 0 ; i < HAND; i++) {
		Hand[i] = new Card(1, 1);
    }


}

public void exchangeCard(int index) {
		int temp= this.draw() ;         
		temp= this.checkExists(temp) ;  
		Hand[i] = this.getCard(temp) ;  
	} 

}
