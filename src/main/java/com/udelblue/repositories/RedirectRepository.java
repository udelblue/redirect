package com.udelblue.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.udelblue.entities.Redirect;

@Repository
public interface RedirectRepository extends CrudRepository<Redirect, Long> {

	Redirect findByGuid(String guid);

}
