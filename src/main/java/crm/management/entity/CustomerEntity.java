package crm.management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer")
public class CustomerEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int kunden_id;

	@Column(name = "name")
	private String name;

	@Column(name = "adresse")
	private String adresse;

	@Column(name = "plz")
	private String plz;

	@Column(name = "ort")
	private String ort;

	@Column(name = "kommentar")
	private String kommentar;

	@Column(name = "email")
	private String email;

	@Column(name = "telefonnummer")
	private String telefonnummer;
	
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String identifier;

	public CustomerEntity() {

	}

	public CustomerEntity(String name, String adresse, String plz, String ort, String kommentar, String email,
			String telefonnummer) {
		this.name = name;
		this.email = email;
		this.telefonnummer = telefonnummer;
		this.adresse = adresse;
		this.plz = plz;
		this.ort = ort;
		this.kommentar = kommentar;
		}

	public int getKunden_Id() {
		return kunden_id;
	}

	public String getName() {
		return name;
	}

	public String getAdresse() {
		return adresse;
	}

	public String getPlz() {
		return plz;
	}

	public String getOrt() {
		return ort;
	}

	public String getKommentar() {
		return kommentar;
	}

	public String getEmail() {
		return email;
	}

	public String getTelefonnummer() {
		return telefonnummer;
	}
	
	public String getIdentifier() {
		return identifier;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}

	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setTelefonnummer(String telefonnummer) {
		this.telefonnummer = telefonnummer;
	}

	@Override
	public String toString() {
		return "Customer [kunden_id=" + kunden_id + ", name=" + name + ", adresse=" + adresse + ", plz=" + plz
				+ ", ort=" + ort + ", kommentar=" + kommentar + ", email=" + email + ", telefonnummer=" + telefonnummer
				+ "]";
	}

}
