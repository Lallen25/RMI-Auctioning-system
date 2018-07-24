import java.io.*;
import java.security.*;
import java.security.spec.*;

public class VerSig {
		//takes in all the parameters to check if they are correct and verify.
	public int Versig(byte[] signature, PublicKey publicKey, int randomNo) {

	    int verified = 0;
			//verify Rsa signature
		try {
	    	   
			String data = Integer.toString(randomNo);
	        	
	    	Signature sig = Signature.getInstance("MD5withRSA");
	        	
	    	sig.initVerify(publicKey);
	        	
	    	sig.update(data.getBytes());
	    	   
	    	boolean verifies = sig.verify(signature);
	    	   
	    	if(verifies){
	    		verified = 1;// set to true
	    	}
	    	   
	    }catch (Exception e){
	        System.err.println("Caught exception " + e.toString());
	    }
	   
	    return verified;
	}
}