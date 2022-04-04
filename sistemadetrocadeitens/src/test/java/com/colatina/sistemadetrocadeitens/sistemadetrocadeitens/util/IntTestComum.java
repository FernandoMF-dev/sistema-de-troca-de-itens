package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.util;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.SistemadetrocadeitensApplication;
import org.junit.Before;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;

@SpringBootTest(classes = SistemadetrocadeitensApplication.class)
@ExtendWith(SpringExtension.class)
@TestConfiguration
@WebAppConfiguration
public abstract class IntTestComum {

	@Autowired
	private EntityManager em;

	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	protected MockMvc getMockMvc() {
		return mockMvc;
	}

	protected EntityManager getEm() {
		return em;
	}

	@Autowired
	public void setWebApplicationContext(WebApplicationContext pWebApplicationContext) {
		webApplicationContext = pWebApplicationContext;
	}

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
}
