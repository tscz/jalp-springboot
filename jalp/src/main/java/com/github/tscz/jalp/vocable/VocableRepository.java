package com.github.tscz.jalp.vocable;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VocableRepository extends JpaRepository<Vocable, Long> {

	Optional<VocableTo> findByValue(String value);

}
