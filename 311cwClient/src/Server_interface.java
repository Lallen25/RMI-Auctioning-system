import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;
import java.util.ArrayList;


/**
 * This is the server interface, this contains a list of methods and constructors used to connect client to sever.
 * @author Lewis		
 *
 */
public interface Server_interface extends Remote {
	
	//Add clients to an array list for all clients.
	public void getDetails(ClientDetails details) throws RemoteException;
	
	//Returns the list containing all clients
	public ArrayList<ClientDetails> getClients() throws RemoteException;
	
	//Request random number
	public int GetRand()throws RemoteException;
	
	//Send signature
	public int sendSignature(byte[] signature, PublicKey publicKey, int randomNo)throws RemoteException;
	
	//Receive verification challenge
	public byte[] receiveChallenge(int randomNo) throws RemoteException;
	
	//Get server public key
	public PublicKey getKey() throws RemoteException;
	
	public ItemDetails CreateAuc(ItemDetails item) throws RemoteException;	//gets auction details
	
	public ArrayList<ItemDetails> viewAuction() throws RemoteException; 	//sends auction details
	
	public String bidding(double amount, int itemID) throws RemoteException;	//allows bidding to pass.

	public void updateAuctions(ArrayList<ItemDetails> updatedAuctions) throws RemoteException;		//updates arraylist
	
}