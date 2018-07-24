import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Server_main{
	
	
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		
		LocateRegistry.createRegistry(8080); //this is used to obtain a reference to a remote object to local host 
		
		try {
			
			Serverimplement ServImp = new Serverimplement();
			Naming.rebind("//localhost:8080/service", ServImp);	//class that provides methods for storing references to a remote object,takes argument address , port,name
			System.out.println("Server is ready");// port takes calls , name is a string, 
			ServImp.GetRand();
			
		}catch(Exception e) {
			System.out.println("Server Error: " + e);
		}
		
		
	}
}