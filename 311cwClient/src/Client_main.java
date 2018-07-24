import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;


public class Client_main {
	
	public static void main(String[] args) throws NotBoundException, IOException, InvalidKeyException, SignatureException, NoSuchAlgorithmException{
	
	Server_interface Conserver = (Server_interface) Naming.lookup("//localhost:8080/service"); //associating a name to the remote object(server)
															//.lookup is used for returning a stub for the remote object linking with the naming we use.
	Client client = new Client();
	
	client.Menu();
	
	}
}