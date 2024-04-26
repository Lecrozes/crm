package crm.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import crm.management.entity.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {

	@Query(value = "SELECT * FROM customer c WHERE c.identifier = :uuid", nativeQuery = true)
	CustomerEntity getByUUID(@Param("uuid") String uuid);

	@Modifying
	@Transactional
	@Query(value = "UPDATE customer SET name = :name, adresse = :adresse, plz = :plz, ort = :ort, kommentar = :kommentar, email = :email, telefonnummer = :telefonnummer WHERE identifier = :uuid", nativeQuery = true)
	void updateCustomer(String uuid, String name, String email, String telefonnummer, String adresse, String plz,
			String ort, String kommentar);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO customer(name, adresse, plz, ort, kommentar, email, telefonnummer) VALUES(:name, :adresse, :plz, :ort, :kommentar, :email, :telefonnummer)", nativeQuery = true)
	void insertCustomer(String name, String email, String telefonnummer, String adresse, String plz, String ort,
			String kommentar);
}
