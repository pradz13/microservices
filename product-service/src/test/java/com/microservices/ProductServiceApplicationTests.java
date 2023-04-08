package com.microservices;

import com.microservices.dto.ProductRequest;
import com.microservices.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
	static MySQLContainer mySQLContainer = new MySQLContainer("mysql:5.5");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProductRepository productRepository;

	private ObjectMapper objectMapper = new ObjectMapper();

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
		dynamicPropertyRegistry.add("spring.datasource.username", mySQLContainer::getUsername);
		dynamicPropertyRegistry.add("spring.datasource.password", mySQLContainer::getPassword);
		dynamicPropertyRegistry.add("spring.datasource.driver-class-name", mySQLContainer::getDriverClassName);
	}

	@Test
	void testCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productRequest)))
				.andExpect(status().isCreated());

		assertEquals(productRepository.findAll().size(), 1);
	}

	@Test
	void testAllProducts() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
				.andExpect(status().isOk());

	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("Iphone 14")
				.description("Apple phone")
				.price(BigDecimal.valueOf(120000))
				.build();

	}
}
