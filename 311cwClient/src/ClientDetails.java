import java.io.Serializable;

/**
 * Here all the varibles needed to create a user are made
 * these are then given get and set methods for other classes to invoke.
 * @author Lewis
 *
 */
public class ClientDetails implements Serializable{

	private String email;
	private String password;
	private String username;
	
	public ClientDetails(String username,String email, String password){
		
		this.username =username;
		this.email= email;
		this.password =password;
	}
	
	public String getEmail(){
		return email;
	}
	
	public String getPassword(){
		return password;
	}
	
	public String getUsername(){
		return username;
	}

}
