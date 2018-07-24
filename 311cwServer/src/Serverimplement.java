import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.jgroups.*;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.util.RspList;
/**
 * This class is where the information from the client is taken and interpreted into the server.
 * @author Lewis
 *
 */
public class Serverimplement extends UnicastRemoteObject implements Server_interface{
	
	public static int auctionCount = 0;
	
	GenSig gensig = new GenSig();
	
	PublicKey pubkey;
	
	RpcDispatcher rpcDisp;
	RequestOptions reqOpts;
	protected Serverimplement() throws Exception{
		gensig.KeyGen();//generate key
		pubkey = gensig.getpubkey();//get public key
		/*Channel chan = new JChannel();
		chan.connect("BidderServer");
		rpcDisp = new RpcDispatcher(chan,new Serverimplement());
		reqOpts = new RequestOptions(ResponseMode.GET_ALL, 5000);*/
	}
	
	
	
	public ArrayList<ItemDetails> list = new ArrayList<ItemDetails>();
	public ArrayList<ClientDetails> Clist = new ArrayList<ClientDetails>();	
	
	//gets client details from client.
	public void getDetails(ClientDetails details) throws RemoteException {
		
		Clist.add(details);
		/*try{
			rpcDisp.callRemoteMethods(	null, "registerUser", new Object[]{details}, new Class[]{ClientDetails.class}, reqOpts);
		}catch(Exception e){
			e.printStackTrace();
		}*/
	
	}
	//gets items from client.
	public ItemDetails CreateAuc(ItemDetails item) throws RemoteException {
		
		list.add(item);
		
		int ID = auctionCount++;
		
		item.setItemID(ID);
		return item;
		
	}
	
	//sent to server when they have been updated and called.
	public ArrayList<ItemDetails> viewAuction() throws RemoteException{
		return list;
	}
	
	public ArrayList<ClientDetails> getClients() throws RemoteException{
		return Clist;
	}

	public String bidding(double amount, int itemID) throws RemoteException {
		
		//ClientDetails client = Clist.get(clientId);
		//String email = client.getEmail();
		//System.out.println(email);
		ItemDetails details = list.get(itemID);
		details.setCurrentBid(amount);
		double currentvalue = details.getCurrentvalue();
		
		return "bid set to" + currentvalue;
		
	}
	
	public void updateAuctions(ArrayList<ItemDetails> updatedAuctions) throws RemoteException {

		list = updatedAuctions;
		System.out.println(list);
		
	}
	//generates random number
	public int GetRand(){
		
		Random randNo  = new Random();
		
		int pickedNo = randNo.nextInt(100) + 1;
		
		return pickedNo;
		
	}
	
	//calls verify class to take these parameters and returns a result if it can be verified or not.
	public int sendSignature(byte[] signature, PublicKey publicKey, int randomNo){
		
		int result = 0;
		
		VerSig versig = new VerSig();
		
		result = versig.Versig(signature, publicKey, randomNo);
		
		return result;
	}
	
	public boolean verify(byte[] signature, String Data, PublicKey publicKey){
		return false;
		
	}
		//try signature to send to client.
	public byte[] receiveChallenge(int randomNo) throws RemoteException {
		
		byte[] signature = null;
		
		try {
			signature = gensig.Sig(randomNo);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return signature;
	}
		//sends server pubkey to match with client private key.
	public PublicKey getKey() throws RemoteException {
		return pubkey;
	}

}
