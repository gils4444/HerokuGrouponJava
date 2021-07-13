package app.core.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.core.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	Customer findByEmailIsNotAndId(String email, int id);
	
	Customer findByEmailIgnoreCaseAndIdIsNot(String email, int id);

	Customer findByEmailIgnoreCaseAndPassword(String email, String password);

	Customer findByEmailIgnoreCase(String email);
	
}
