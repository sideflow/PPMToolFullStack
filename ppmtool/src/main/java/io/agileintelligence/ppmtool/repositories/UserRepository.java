package io.agileintelligence.ppmtool.repositories;

import org.springframework.data.repository.CrudRepository;

import io.agileintelligence.ppmtool.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {

	User findByUsername(String username);
	
	User getById(Long id);
}
