import java.io.*;
import java.security.*;

public class GenSig {
		
	PublicKey kpub;
	PrivateKey kpri;
	KeyPair pair;
		
	byte[] realSig;
	
	public void KeyGen(){
	         //generates the keys.
		try {

	        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
	        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
	        	
	        keyGen.initialize(2048, random);
	        KeyPair kp = keyGen.generateKeyPair();
	        kpri =kp.getPrivate();
	        kpub = kp.getPublic();
	        	
	    }catch(Exception e){
	        System.err.println("caught exception "+ e.toString());
	    }
	}    
	         //create signature into byte array.
	public byte[] Sig(int randomNo) throws InvalidKeyException, SignatureException, UnsupportedEncodingException, NoSuchAlgorithmException {
		   
		String data = Integer.toString(randomNo);
	        
		/* Generate a DSA signature */
	        	
		Signature rsa = Signature.getInstance("MD5withRSA");  	
		rsa.initSign(kpri);  	
		rsa.update(data.getBytes("UTF8")); 	
		byte[] signature = rsa.sign();
	        	
	    return signature;	
	   
	}
	   
	public PublicKey getpubkey(){
		return kpub;  
	}
	     
	public PrivateKey getprikey(){ 
		return kpri;
	}   
}
