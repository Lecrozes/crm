package crm.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import crm.management.entity.ContactEntity;

public interface UserRepository extends JpaRepository<ContactEntity, Integer> {

}
