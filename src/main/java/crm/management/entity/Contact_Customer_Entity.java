package crm.management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "contact_customer")
public class Contact_Customer_Entity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "contact_identifier")
	private String contact_identifier;

	@Column(name = "customer_identifier")
	private String customer_identifier;
	
	@Column(name = "kommentar")
	private String kommentar;
	
	
	public Contact_Customer_Entity(String contact_identifier, String customer_identifier, String kommentar) {
		this.contact_identifier = contact_identifier;
		this.customer_identifier = customer_identifier;
		this.kommentar = kommentar;
	}

	public String getContact_identifier() {
		return contact_identifier;
	}

	public String getCustomer_identifier() {
		return customer_identifier;
	}

	public String getKommentar() {
		return kommentar;
	}

	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}

	@Override
	public String toString() {
		return "Contact_Customer_Entity [contact_identifier=" + contact_identifier + ", customer_identifier="
				+ customer_identifier + "]";
	}

}
