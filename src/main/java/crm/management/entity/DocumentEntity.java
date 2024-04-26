package crm.management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "document")
public class DocumentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int dokument_id;

	@Column(name = "name")
	private String name;
	
	@Column(name = "owner_identifier")
	private String owner_identifier;
	
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String identifier;
	
	@Column(name = "file")
	private byte[] file;
	
	public String getName() {
		return name;
	}

	public String getOwnerIdentifier() {
		return owner_identifier;
	}

	public String getIdentifier() {
		return identifier;
	}

	public byte[] getFile() {
		return file;
	}
}
