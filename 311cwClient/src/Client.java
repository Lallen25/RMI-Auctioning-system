import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * This class is a menu for an auction system, this allows users to enter their email and password which is stored on the server
 * Users can make Auction's which are stored on the server
 * They can also make bids here or on a bidding client which are stored on to a server
 * This is done by instantiating methods made and sending them to be stored in an array on the server,
 * to receive the values they are send back and ran through a for loop array.
 * 
 *@author Lewis
 */
public class Client {

	public void Menu() throws NotBoundException, IOException, InvalidKeyException, SignatureException, NoSuchAlgorithmException{
	
		Server_interface Conserver = (Server_interface) Naming.lookup("//localhost:8080/service"); //connection to server
		
		ArrayList<ClientDetails> clients = new ArrayList<ClientDetails>();
		
		VerSig versig = new VerSig();		//verify signature class
		
		int serverChallenge = GetRand();	//get random number method.
		
		byte[] sig = Conserver.receiveChallenge(serverChallenge); //input random number into byte array
		
		int serverVerified = versig.Versig(sig, Conserver.getKey(), serverChallenge);	//sends signature and key and random number to server to verify, if matches it's verified. 
		
		if(serverVerified == 0){
			System.out.println("**SERVER VERIFIED**");
		}else{
			System.out.println("**SERVER NOT VERIFIED**");
		}
		
		Scanner scan = new Scanner(System.in);// scanner to used to read lines.
		
		System.out.println("Welcome to the Auction system, what would you like to do?\n");		
		  
		System.out.println("What would you like to do?");
		System.out.println("LOGIN");
		System.out.println("REGISTER");
		System.out.println("EXIT");
		
		String Option = scan.nextLine();  
		  
		//LOGIN
		if(Option.startsWith("log".toLowerCase())){
			
			//Set email
			System.out.println("Please enter your email: \n");
			String loginEmail = scan.nextLine();
			
			//Set password
			System.out.println("Please enter your password: \n");
			String loginPassword = scan.nextLine();
			
			//Get list of clients from server
			clients = Conserver.getClients();
			
			//Loop though list to check details	
			if(!clients.isEmpty()){
				
				for(ClientDetails cli : clients){
					
					if(cli.getEmail().equals(loginEmail)){
						
						if(cli.getPassword().equals(loginPassword)){
							
							System.out.println("Welcome " + cli.getUsername() + ", you are now logged in.");
							
							int challenge = Conserver.GetRand();
							
							GenSig gensig = new GenSig();
							
							gensig.KeyGen();
							
							byte[] signature = gensig.Sig(challenge);
							
							PublicKey pubkey = gensig.getpubkey();
							
							int verified = Conserver.sendSignature(signature, pubkey, challenge);
							//here we use our key signature and verification from the server to test whether they have the same key to match the client
							//if so the verification is true and the user can access, if not they will not be able to access the auciton system.
							if(verified == 1){
								clientLoggedIn(loginEmail);
								System.out.println("**VERIFIED**");
								System.out.println("Welcome " + cli.getUsername() + ", you are now logged in.");
								clientLoggedIn(loginEmail);
								break;
							}else{
								System.out.println("**NOT VERIFIED**");
								Menu();
							}
							
						}else{
							System.out.println("Your details are incorrect.");
							Menu();
						}
					}
				}
			}else{
				System.out.println("Your details are incorrect");
				Menu();
			}
		}
		  
		//REGISTER
		if(Option.startsWith("reg".toLowerCase())){
			  
		  System.out.println("Please enter an email and password to register\n");
		  
		  System.out.println("Enter name:");
		  String name = scan.nextLine();
		  
		  System.out.println("Enter email:"); 
		  String email = scan.nextLine();
		  
		  System.out.println("Enter password:");  
		  String password = scan.nextLine();
		
		  ClientDetails register = new ClientDetails(name, email, password); 	  

		  Conserver.getDetails(register);//here we make an instance of the client class with parameters and we send this data to the server.
		  
		  Menu();
		}      
		
		//EXIT
		if(Option.startsWith("ex".toLowerCase())){
			System.exit(0);
		}
	
	}     
	//if the client is logged in they can access this stage of the code.
	public void clientLoggedIn(String userEmail) throws RemoteException, MalformedURLException, NotBoundException{	
		
		Server_interface Conserver = (Server_interface) Naming.lookup("//localhost:8080/service"); //connection to server
		
		//Set logged in users email
		String currentEmail = userEmail;
		
		Scanner scan = new Scanner(System.in);
		
		while(true){
	    	    
	      System.out.println("\nChoose an option\n");
	      System.out.println("Sell - Sell items");
	      System.out.println("Bid - Bid for an item");
	      System.out.println("View - See auctions");
	      System.out.println("Close - Close an auction");
	      System.out.println("Exit - Quit");
	      
	      System.out.println("Please select an option");
	      
	   	  String Option = scan.nextLine();
	    switch(Option){	//switch case is used instead of if and else ifs here for a better structure.
	    
	    case "sell":if (Option.startsWith("Sell".toLowerCase())){	
	   	  		
	    		  System.out.println("What do you want to sell?");
	    		  String ItemName = scan.nextLine();
	    		  
	    		  System.out.println("Please describe the item");
	    		  String ItemDesc = scan.nextLine();
	    		  
	    		  System.out.println("Whats the staring price?");
	    		  double startingprice = scan.nextDouble();
	    		  
	    		  System.out.println("Whats the reserved price?");
	    		  double reservedvalue = scan.nextDouble();
	    		  scan.nextLine();
	    		     		  
	    		  ItemDetails auction = new ItemDetails(ItemName,ItemDesc,startingprice, reservedvalue); //here the parameters are sent to the server
	    		  auction.setSeller(currentEmail);
	    		  
	    		  Conserver.CreateAuc(auction);		//here to send the parameters now filled to the server.
	    		  
	    		  System.out.println("You have successfully uploaded an auction");
	    		  
	    	  }break;
	    	  
	    	  
	    case "bid":if(Option.startsWith("Bid".toLowerCase())){
   		 boolean found = false;
		  ArrayList<ItemDetails> auctions = Conserver.viewAuction();
		  	
		  for(ItemDetails item : auctions){
		  		
		  	System.out.println("ITEM ID: " + item.getitemId());
		  	System.out.println("ITEM: " + item.getName());
		  	System.out.println("START PRICE: " + item.getStart());
		  	System.out.println("CURRENT BID: " + item.getCurrentvalue());
		  	System.out.println("\n");
		  			
		  }
		  
		  System.out.println("Please enter Item ID you wish to bid on: ");
		  int itemID = scan.nextInt();
		  
		  //loop through item array list
		  for(ItemDetails item : auctions){
			  found = false;
			  if(item.getitemId() == itemID){
				  
				  //owner cannot bid on their auctions.
				  if(item.getSeller().equals(currentEmail)){
					  
					  System.out.println("You cannot bid on your own item.");
					  clientLoggedIn(currentEmail);
					  
				  }
				  
				  else{
				
					   System.out.println("How much would you like to bid?");
					   double bid = scan.nextDouble();
					  
					   if(item.getReserved() > bid){
						  System.out.println("You cant bid lower than the reserve price.");
						  clientLoggedIn(currentEmail);
					   }else if(item.getReserved() >= bid){
						  System.out.println("You cant bid lower/equal to than the current bid.");
						  clientLoggedIn(currentEmail);
					   }else if(item.getCurrentvalue() < bid){
						  System.out.println("Your bid has been made");
						  //sets new item price.
						  item.setCurrentBid(bid);
						  System.out.println(item.getCurrentvalue());
						  item.setWinner(currentEmail);
						  found=true;
						
					   } 
					   /*if(Option.startsWith("Bid".toLowerCase())){
				   		  System.out.println("Please enter bid");
				   		  double bid = scan.nextDouble();
				   		  System.out.println("Please enter item id");
				   		  int itemID = scan.nextInt();
				   		  
				   		  Conserver.bidding(bid, itemID);
				   	  }*/
					   	   
					 else if(found ==false){
						   
						   
					 }
					//System.out.println(Conserver.bidding(bid, itemID));
				  }
			  }else{
				  System.out.println("This item does not exist...");
				  clientLoggedIn(currentEmail);
			  }
			  
		  }
		  
		  //Conserver.bidding(bid, itemID);
		  Conserver.updateAuctions(auctions); //the auction is updated to the new price.
	  }
	    break;
	    
	    case "view": if (Option.startsWith("View".toLowerCase())){		//this shows all the listings in the server.
	    		  
	    		  ArrayList<ItemDetails> auctions = Conserver.viewAuction();
	    		 
	    		  if(auctions.isEmpty()){
	    			  System.out.println("There are currently no auctions to bid on.");
	    		  }
	    		  //loops through item array to see all available auctions.
	    		  for(ItemDetails item : auctions){
	    		  		
	    		  	System.out.println("ITEM ID: " + item.getitemId());
	    		  	System.out.println("ITEM: " + item.getName());
	    		  	System.out.println("START PRICE: " + item.getStart());
	    		  	System.out.println("CURRENT BID: " + item.getCurrentvalue());
	    		  	System.out.println("\n");
	    		  			
	    		  }
	    	  }break;
	    	  
	    case"close":if (Option.startsWith("close".toLowerCase())){
	    		  
	    		  ArrayList<ItemDetails> currentAuctions = Conserver.viewAuction();		//creating a currentvalue arraylist referencing the same array list as the auctions.
	    		  
	    		  System.out.println("Please enter the auction you wish to end...");
	    		  int ID = Integer.parseInt(scan.nextLine());	//auction id entered to end.
	    		  
	    		  if(!currentAuctions.isEmpty()){//if not empty...
	    			  
	    			  for(ItemDetails Item : currentAuctions){//loop item auctions
	    				  
	    				  if(Item.getitemId() == ID){//if the item has the inputted ID...
	    					 
	    					  if(Item.getSeller().equals(currentEmail)){// and set email is the owner...
	    					  
	    						  System.out.println("You have ended the auction for: " + Item.getName());
	    						  System.out.println("The final price for this item was: " + Item.getCurrentvalue());
	    					  
	    						  if(Item.getCurrentvalue() >= Item.getReserved()){
	    							  System.out.println("The reserve price was met.");
	    							  System.out.println("The winner is: " + Item.getWinner());
	    						  }else{
	    							  System.out.println("The reserve price was not met.");
	    						  }
	    					  
	    						  currentAuctions.remove(Item);
	    						  break;
	    					  }else{
	    						  System.out.println("You cannot end an auction that is not yours.");//email doesnt match that of the seller.
	    					  }
	    				  }else{
	    					  System.out.println("This item does not exist"); //ID is not in auction loop.
	    				  }
	    			  }  
	    			  
	    			  Conserver.updateAuctions(currentAuctions);		//this then updates the arraylist to remove auctions on the list.
	    			  
	    		  }else if(currentAuctions.isEmpty()){
	    			  System.out.println("There are no current auctions to view right now.");
	    		  }
	    	  }break;
	    	  
	    case "exit": if (Option.startsWith("Exit".toLowerCase())){
	    		  System.exit(0);
	    	  }
   }
	   	  	
	   	  	

	    	  
	    }	
	}
	
	
	//this creates a random integer.
	public int GetRand(){
	
		Random randNo  = new Random();
		
		int pickedNo = randNo.nextInt(100) + 1;
		
		return pickedNo;
		
	}
}

	
