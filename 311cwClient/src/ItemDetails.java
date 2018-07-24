import java.io.Serializable;
/**
 * Here is where all the auction varibles are made, this is also creating all the getters and setters the server and client need to invoke.
 * @author Lewis
 *
 */

	public class ItemDetails implements Serializable {
		String ItemName;
		String ItemDesc;
		double startingprice;
		double reservedvalue;
		double Currentvalue;
		int itemId;
		int Idnext=0;
		
		String seller;
		String currentWinner;

	    public ItemDetails(String name, String description, double startprice, double reservedvalue) {
	        this.ItemName = name;
	        this.ItemDesc = description;
	        this.startingprice = startprice;
	        this.reservedvalue = reservedvalue;
	        this.Currentvalue = 0;
	        this.itemId += 1; 	        
	    }
	    
		 public String getName(){
	    	return ItemName;
	    }
		 
	     public String setName(String name){
	    	return this.ItemName = name;
	    }
	     
	     public String getDesc(){
	    	 return ItemDesc;
	     }
	     
	     public String setDesc(String description){
	    	 return this.ItemDesc = description;
	     }
	     
	     public double getStart(){
	    	return startingprice;
	    }
	   
	    public double setStart(int startprice){
	    	return  this.startingprice = startprice;
	    }
	    
	    public double getReserved(){
			return reservedvalue;
	    }
	    
	    public double setReserved(int Resprice){
	    	return this.reservedvalue=Resprice;
	    }
	    
	    public double setCurrentBid(double Currentvalue){
	    	return this.Currentvalue=Currentvalue;
	    }
	    
	    public double getCurrentvalue(){
	    	return Currentvalue;
	    }
	    
	    public int getitemId(){
	    	return itemId;  }
	    
	    public int setItemID(int itemId){
	    	return this.itemId=itemId;
	    }
	    
	    public void setSeller(String seller){
	    	this.seller = seller;
	    }
	    
	    public String getSeller(){
	    	return seller;
	    }
	    
	    public void setWinner(String winner){
	    	this.currentWinner = winner;
	    }
	    
	    public String getWinner(){
	    	return currentWinner;
	    }
	    
}


