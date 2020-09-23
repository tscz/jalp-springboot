package com.github.tscz.jalp.vocable;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
class VocableRepositoryIntegrationTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private VocableRepository vocableRepo;

	@Test
	final void testFindByValue() {

		// given
		var hello = new Vocable();
		hello.setValue("hello");
		hello.setTranslation("안녕하세요");
		entityManager.persist(hello);
		entityManager.flush();

		// when
		var found = vocableRepo.findByValue(hello.getValue());

		// then
		assertThat(found).isPresent();
		assertThat(found.get()).usingRecursiveComparison().isEqualTo(hello);

	}
}
