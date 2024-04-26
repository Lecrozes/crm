package crm.management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "contact")
public class ContactEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int kontakt_id;

	@Column(name = "intern")
	private boolean intern;

	@Column(name = "name")
	private String name;

	@Column(name = "vorname")
	private String vorname;

	@Column(name = "pronomen")
	private String pronomen;

	@Column(name = "email")
	private String email;

	@Column(name = "telefonnummer")
	private String telefonnummer;

	@Column(name = "kommentar")
	private String kommentar;

	@GeneratedValue(strategy = GenerationType.AUTO)
	private String identifier;

	public ContactEntity() {

	}

	public ContactEntity(boolean intern, String name, String vorname, String pronomen, String email,
			String telefonnummer, String kommentar) {
		this.intern = intern;
		this.name = name;
		this.vorname = vorname;
		this.pronomen = pronomen;
		this.email = email;
		this.telefonnummer = telefonnummer;
		this.kommentar = kommentar;
	}

	public int getKontakt_id() {
		return kontakt_id;
	}

	public boolean isIntern() {
		return intern;
	}

	public String getName() {
		return name;
	}

	public String getVorname() {
		return vorname;
	}

	public String getPronomen() {
		return pronomen;
	}

	public String getEmail() {
		return email;
	}

	public String getTelefonnummer() {
		return telefonnummer;
	}

	public String getKommentar() {
		return kommentar;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIntern(boolean intern) {
		this.intern = intern;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public void setPronomen(String pronomen) {
		this.pronomen = pronomen;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setTelefonnummer(String telefonnummer) {
		this.telefonnummer = telefonnummer;
	}

	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else {
			return this.identifier.equals(((ContactEntity) obj).getIdentifier());
		}
	}

	@Override
	public String toString() {
		return vorname + " " + name + " | " + email + " | Intern: " + intern;
	}

}
