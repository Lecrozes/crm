package crm.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import crm.management.entity.Contact_Customer_Entity;

public interface Contact_Customer_Repository extends JpaRepository<Contact_Customer_Entity, Integer> {

	@Query(value = "SELECT contact_identifier  FROM contact_customer WHERE customer_identifier = :customerUUID", nativeQuery = true)
	List<String> getContacts(String customerUUID);

	@Query(value = "SELECT kommentar FROM contact_customer WHERE customer_identifier = :customerUUID AND contact_identifier = :contactUUID", nativeQuery = true)
	String getCommentFromRelation(String customerUUID, String contactUUID);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO contact_customer(contact_identifier, customer_identifier) VALUES(:contact_identifier, :customer_identifier)", nativeQuery = true)
	void insertRelation(String contact_identifier, String customer_identifier);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM contact_customer WHERE customer_identifier = :customer_identifier AND contact_identifier = :contact_identifier ", nativeQuery = true)
	void deleteRelation(String contact_identifier, String customer_identifier);

	@Modifying
	@Transactional
	@Query(value = "UPDATE contact_customer SET kommentar = :comment WHERE customer_identifier = :customerUUID AND contact_identifier = :contactUUID", nativeQuery = true)
	void setComment(String customerUUID, String contactUUID, String comment);

}
