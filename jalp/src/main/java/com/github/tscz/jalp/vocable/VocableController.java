package com.github.tscz.jalp.vocable;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tscz.spring.platform.exception.ResourceNotFoundException;

@RestController
public class VocableController {

	@Autowired
	private VocableRepository vocableRepository;

	@Autowired
	private ObjectMapper mapper;

	@GetMapping("/vocabulary")
	public List<Vocable> getVocabulary() {
		return vocableRepository.findAll();
	}

	@PostMapping("/vocabulary")
	public VocableTo addVocable(@Valid @RequestBody VocableTo vocable) {

		return mapper.convertValue(//
				vocableRepository.save(mapper.convertValue(vocable, Vocable.class)), VocableTo.class);
	}

	@PutMapping("/vocabulary/{value}")
	public VocableTo updateVocable(@PathVariable String value, @Valid @RequestBody VocableTo vocable) {
		if (!vocableRepository.findByValue(value).isEmpty()) {
			throw new ResourceNotFoundException("Vocable not found with value " + value);
		}

		return addVocable(vocable);
	}

	@DeleteMapping("/vocabulary/{value}")
	public ResponseEntity<Void> deleteVocable(@PathVariable long id) {
		var vocable = vocableRepository.findById(id);

		if (vocable.isEmpty()) {
			throw new ResourceNotFoundException("Vocable not found with id " + id);
		}

		vocableRepository.delete(vocable.orElseThrow());
		return ResponseEntity.ok().build();

	}
}
