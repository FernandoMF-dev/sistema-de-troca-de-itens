package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.recurso;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.SistemadetrocadeitensApplication;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.builder.UsuarioBuilder;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Usuario;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.repositorio.UsuarioRepositorio;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.LoginDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.util.IntTestComum;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.util.TestUtil;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SistemadetrocadeitensApplication.class)
@Transactional
public class LoginRecursoIT extends IntTestComum {

	@Autowired
	private UsuarioBuilder usuarioBuilder;

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;

	@BeforeEach
	public void inicializar() {
		usuarioRepositorio.deleteAll();
		usuarioBuilder.setCustomizacao(null);
	}

	@Test
	public void logar() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		LoginDto loginDto = novoLogin(usuario.getEmail(), usuarioRepositorio.obterToken(usuario.getId()));
		getMockMvc().perform(post("/api/login")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(loginDto)))
				.andExpect(status().isOk());
	}

	@Test
	public void logarEmailInvalido() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		LoginDto loginDto = novoLogin("invalido" + usuario.getEmail(), usuarioRepositorio.obterToken(usuario.getId()));
		getMockMvc().perform(post("/api/login")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(loginDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void logarTokenInvalido() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		LoginDto loginDto = novoLogin(usuario.getEmail(), usuarioRepositorio.obterToken(usuario.getId()) + "invalido");
		getMockMvc().perform(post("/api/login")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(loginDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void obterPorToken() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		getMockMvc().perform(get("/api/login/" + usuarioRepositorio.obterToken(usuario.getId())))
				.andExpect(status().isOk());
	}

	@Test
	public void obterPorTokenInvalido() throws Exception {
		getMockMvc().perform(get("/api/login/tokeninvalido"))
				.andExpect(status().isBadRequest());
	}

	private LoginDto novoLogin(String email, String token) {
		LoginDto loginDto = new LoginDto();
		loginDto.setEmail(email);
		loginDto.setToken(token);
		return loginDto;
	}
}
