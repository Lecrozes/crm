package crm.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import crm.management.entity.ContactEntity;

public interface ContactRepository extends JpaRepository<ContactEntity, Integer> {

	@Query(value = "Select * FROM contact WHERE identifier = :uuid", nativeQuery = true)
	ContactEntity getByUUID(String uuid);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO contact(name, vorname, pronomen, email, telefonnummer, intern, kommentar) VALUES(:name, :vorname, :pronomen, :email, :telefonnummer, :intern, :kommentar)", nativeQuery = true)
	void insertContact(String name, String vorname, String pronomen, String email, String telefonnummer, boolean intern,
			String kommentar);

	@Modifying
	@Transactional
	@Query(value = "UPDATE contact SET name = :name, vorname = :vorname, pronomen = :pronomen, email = :email, telefonnummer = :telefonnummer, intern = :intern, kommentar = :kommentar WHERE identifier = :identifier", nativeQuery = true)
	void updateCustomer(String identifier, String name, String vorname, String pronomen, String email,
			String telefonnummer, boolean intern, String kommentar);
}
