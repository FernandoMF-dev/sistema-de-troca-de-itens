package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.recurso;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.SistemadetrocadeitensApplication;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.util.IntTestComum;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SistemadetrocadeitensApplication.class)
@Transactional
public class SituacaoRecursoIT extends IntTestComum {

	@Test
	public void listar() throws Exception {
		getMockMvc().perform(get("/api/situacao"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(4)));
	}

}
