package crm.management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "contact")
public class UserEntitiy {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int user_id;

	@Column(name = "name")
	private String name;

	@Column(name = "vorname")
	private String vorname;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "admin")
	private boolean admin;
	
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String identifier;

	public UserEntitiy() {

	}

	public UserEntitiy(String username, String name, String vorname, String password, boolean admin) {
		this.username = username;
		this.name = name;
		this.vorname = vorname;
		this.password = password;
		this.admin = admin;
		
	}
	
	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public String toString() {
		return "ContactEntity [kontakt_id=" + user_id + ", name=" + name + ", vorname="
				+ vorname + ", username=" + username;
	}

}
