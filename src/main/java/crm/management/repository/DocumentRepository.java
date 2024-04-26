package crm.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import crm.management.entity.DocumentEntity;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Integer> {
	
	@Query(value = "SELECT *  FROM document WHERE  owner_identifier = :ownerIdentifier", nativeQuery = true)
	List<DocumentEntity> getDocsForOwner(String ownerIdentifier);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO document(name, file, owner_identifier) VALUES(:name, :file, :ownerIdentifier)", nativeQuery = true)
	void insertDocument(String name, byte[] file, String ownerIdentifier);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM document WHERE identifier = :identifier", nativeQuery = true)
	void deleteByIdentifier(String identifier);
	
}
