package app.core.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.core.entities.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
	
	Company findByNameIgnoreCase(String name);

	Company findByEmail(String email);
	
	Company findByEmailAndIdIsNot(String email,int id);
	
	Company findByEmailIgnoreCaseAndPassword(String email,String password);
	
}
