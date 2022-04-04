package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.recurso;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.SistemadetrocadeitensApplication;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.builder.UsuarioBuilder;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Usuario;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.repositorio.UsuarioRepositorio;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.mapper.UsuarioMapper;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.util.IntTestComum;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.util.TestUtil;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SistemadetrocadeitensApplication.class)
@Transactional
public class UsuarioRecursoIT extends IntTestComum {

	@Autowired
	private UsuarioBuilder usuarioBuilder;

	@Autowired
	private UsuarioMapper usuarioMapper;

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;

	@BeforeEach
	public void inicializar() {
		usuarioRepositorio.deleteAll();
		usuarioBuilder.setCustomizacao(null);
	}

	@Test
	public void listar() throws Exception {
		usuarioBuilder.construir();
		getMockMvc().perform(get("/api/usuario"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(1)));
	}

	@Test
	public void listar2() throws Exception {
		usuarioBuilder.customizar(entidade -> {
			entidade.setCpf("54268604081");
			entidade.setEmail("savio.pagung@hotmail.com");
		}).construir();
		usuarioBuilder.customizar(entidade -> {
			entidade.setCpf("43543543534");
			entidade.setEmail("fmf.prisma@gmail.com");
		}).construir();
		getMockMvc().perform(get("/api/usuario"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)));
	}

	@Test
	public void listarVazio() throws Exception {
		getMockMvc().perform(get("/api/usuario"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(0)));
	}

	@Test
	public void obterPorId() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		getMockMvc().perform(get("/api/usuario/" + usuario.getId()))
				.andExpect(status().isOk());
	}

	@Test
	public void obterPorId2() throws Exception {
		usuarioBuilder.customizar(entidade -> {
			entidade.setCpf("54268604081");
			entidade.setEmail("testeA@gmail.com");
		}).construir();
		Usuario usuario = usuarioBuilder.customizar(entidade -> {
			entidade.setCpf("24603471033");
			entidade.setEmail("testeB@gmail.com");
		}).construir();
		usuarioBuilder.customizar(entidade -> {
			entidade.setCpf("24877455094");
			entidade.setEmail("testeC@gmail.com");
		}).construir();
		getMockMvc().perform(get("/api/usuario/" + usuario.getId()))
				.andExpect(status().isOk());
	}

	@Test
	public void obterPorIdInvalido() throws Exception {
		getMockMvc().perform(get("/api/usuario/1"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvar() throws Exception {
		Usuario usuario = usuarioBuilder.construirEntidade();
		getMockMvc().perform(post("/api/usuario")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(usuarioMapper.toDto(usuario))))
				.andExpect(status().isCreated());
	}

	@Test
	public void salvarNomeInvalido() throws Exception {
		Usuario usuario = usuarioBuilder.construirEntidade();
		usuario.setNome("");
		getMockMvc().perform(post("/api/usuario")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(usuarioMapper.toDto(usuario))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvarNomeInvalido2() throws Exception {
		Usuario usuario = usuarioBuilder.construirEntidade();
		usuario.setNome(null);
		getMockMvc().perform(post("/api/usuario")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(usuarioMapper.toDto(usuario))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvarCpfInvalido() throws Exception {
		Usuario usuario = usuarioBuilder.construirEntidade();
		usuario.setCpf("11122233344");
		getMockMvc().perform(post("/api/usuario")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(usuarioMapper.toDto(usuario))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvarCpfInvalido2() throws Exception {
		Usuario usuario = usuarioBuilder.construirEntidade();
		usuario.setCpf(null);
		getMockMvc().perform(post("/api/usuario")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(usuarioMapper.toDto(usuario))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvarCpfInvalido3() throws Exception {
		Usuario usuario = usuarioBuilder.construirEntidade();
		usuario.setCpf("");
		getMockMvc().perform(post("/api/usuario")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(usuarioMapper.toDto(usuario))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvarEmailInvalido() throws Exception {
		Usuario usuario = usuarioBuilder.construirEntidade();
		usuario.setEmail("abcdefg");
		getMockMvc().perform(post("/api/usuario")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(usuarioMapper.toDto(usuario))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvarEmailInvalido2() throws Exception {
		Usuario usuario = usuarioBuilder.construirEntidade();
		usuario.setEmail(null);
		getMockMvc().perform(post("/api/usuario")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(usuarioMapper.toDto(usuario))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvarEmailInvalido3() throws Exception {
		Usuario usuario = usuarioBuilder.construirEntidade();
		usuario.setEmail("");
		getMockMvc().perform(post("/api/usuario")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(usuarioMapper.toDto(usuario))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvarDataInvalido() throws Exception {
		usuarioBuilder.construir();
		Usuario usuario = usuarioBuilder.construirEntidade();
		usuario.setData(LocalDate.now().plusDays(5L));
		getMockMvc().perform(post("/api/usuario")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(usuarioMapper.toDto(usuario))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvarCpfDuplicado() throws Exception {
		usuarioBuilder.customizar(entidade -> entidade.setEmail("teste2@gmail.com")).construir();
		Usuario usuario = usuarioBuilder.construirEntidade();
		getMockMvc().perform(post("/api/usuario")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(usuarioMapper.toDto(usuario))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvarEmailDuplicado() throws Exception {
		usuarioBuilder.customizar(entidade -> entidade.setCpf("24877455094")).construir();
		Usuario usuario = usuarioBuilder.construirEntidade();
		getMockMvc().perform(post("/api/usuario")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(usuarioMapper.toDto(usuario))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterar() throws Exception {
		Usuario usuarioSalvo = usuarioBuilder.construir();
		usuarioSalvo.setNome("Teste Usuario 2");
		usuarioSalvo.setEmail("Teste2@gmail.com");
		usuarioSalvo.setCpf("54268604081");
		usuarioSalvo.setData(LocalDate.now().minusDays(5L));
		getMockMvc().perform(put("/api/usuario")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(usuarioMapper.toDto(usuarioSalvo))))
				.andExpect(status().isOk());
	}

	@Test
	public void alterarNomeInvalido() throws Exception {
		Usuario usuarioSalvo = usuarioBuilder.construir();
		usuarioSalvo.setNome("");
		getMockMvc().perform(put("/api/usuario")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(usuarioMapper.toDto(usuarioSalvo))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterarCpfInvalido() throws Exception {
		Usuario usuarioSalvo = usuarioBuilder.construir();
		usuarioSalvo.setCpf("11122233344");
		getMockMvc().perform(put("/api/usuario")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(usuarioMapper.toDto(usuarioSalvo))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterarCpfInvalido2() throws Exception {
		Usuario usuarioSalvo = usuarioBuilder.construir();
		usuarioSalvo.setCpf(null);
		getMockMvc().perform(put("/api/usuario")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(usuarioMapper.toDto(usuarioSalvo))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterarCpfInvalido3() throws Exception {
		Usuario usuarioSalvo = usuarioBuilder.construir();
		usuarioSalvo.setCpf("");
		getMockMvc().perform(put("/api/usuario")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(usuarioMapper.toDto(usuarioSalvo))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterarEmailInvalido() throws Exception {
		Usuario usuarioSalvo = usuarioBuilder.construir();
		usuarioSalvo.setEmail("abcdefg");
		getMockMvc().perform(put("/api/usuario")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(usuarioMapper.toDto(usuarioSalvo))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterarEmailInvalido2() throws Exception {
		Usuario usuarioSalvo = usuarioBuilder.construir();
		usuarioSalvo.setEmail(null);
		getMockMvc().perform(put("/api/usuario")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(usuarioMapper.toDto(usuarioSalvo))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterarEmailInvalido3() throws Exception {
		Usuario usuarioSalvo = usuarioBuilder.construir();
		usuarioSalvo.setEmail("");
		getMockMvc().perform(put("/api/usuario")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(usuarioMapper.toDto(usuarioSalvo))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterarDataInvalida() throws Exception {
		Usuario usuarioSalvo = usuarioBuilder.construir();
		usuarioSalvo.setData(LocalDate.now().plusDays(5L));
		getMockMvc().perform(put("/api/usuario")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(usuarioMapper.toDto(usuarioSalvo))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterarCpfDuplicado() throws Exception {
		usuarioBuilder.customizar(entidade -> {
			entidade.setCpf("54268604081");
			entidade.setEmail("teste1@gmail.com");
		}).construir();
		Usuario usuarioSalvo = usuarioBuilder.customizar(entidade -> {
			entidade.setCpf("24877455094");
			entidade.setEmail("teste2@gmail.com");
		}).construir();
		usuarioSalvo.setCpf("54268604081");
		getMockMvc().perform(put("/api/usuario")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(usuarioMapper.toDto(usuarioSalvo))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterarEmailDuplicado() throws Exception {
		usuarioBuilder.customizar(entidade -> {
			entidade.setCpf("54268604081");
			entidade.setEmail("teste1@gmail.com");
		}).construir();
		Usuario usuarioSalvo = usuarioBuilder.customizar(entidade -> {
			entidade.setCpf("24877455094");
			entidade.setEmail("teste2@gmail.com");
		}).construir();
		usuarioSalvo.setEmail("teste1@gmail.com");
		getMockMvc().perform(put("/api/usuario")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(usuarioMapper.toDto(usuarioSalvo))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void deletar() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		getMockMvc().perform(delete("/api/usuario/" + usuario.getId()))
				.andExpect(status().isOk());
	}

	@Test
	public void deletarInvalido() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		usuario.setId(usuario.getId() + 5L);
		getMockMvc().perform(delete("/api/usuario/" + usuario.getId()))
				.andExpect(status().isBadRequest());
	}
}
