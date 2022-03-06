package me.toddbensmiller.documentapi;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import me.toddbensmiller.documentapi.controller.SpringController;

@SpringBootTest
class DocumentapiApplicationTests {

	@Autowired
	private SpringController controller;

	@Test
	void contextLoads() {
		Assertions.assertThat(controller).isNotNull();
	}
}
