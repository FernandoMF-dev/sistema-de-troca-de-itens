package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.recurso;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.SistemadetrocadeitensApplication;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.builder.ItemBuilder;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.builder.UsuarioBuilder;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Categoria;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Item;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Usuario;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.repositorio.ItemRepositorio;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.repositorio.UsuarioRepositorio;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.mapper.ItemMapper;
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

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SistemadetrocadeitensApplication.class)
@Transactional
public class ItemRecursoIT extends IntTestComum {

	private final Long CATEGORIA_MAX = 20L;

	@Autowired
	private ItemBuilder itemBuilder;

	@Autowired
	private ItemMapper itemMapper;

	@Autowired
	private ItemRepositorio itemRepositorio;

	@Autowired
	private UsuarioBuilder usuarioBuilder;

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;

	@BeforeEach
	public void inicializar() {
		usuarioRepositorio.deleteAll();
		usuarioBuilder.setCustomizacao(null);
		itemRepositorio.deleteAll();
		itemBuilder.setCustomizacao(null);
	}

	@Test
	public void listar() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		itemBuilder.customizar(entidade -> entidade.setUsuario(usuario)).construir();
		getMockMvc().perform(get("/api/item"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(1)));
	}

	@Test
	public void listar2() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		itemBuilder.customizar(entidade -> {
			entidade.setNome("Item Teste A");
			entidade.setUsuario(usuario);
		}).construir();
		itemBuilder.customizar(entidade -> {
			entidade.setNome("Item Teste B");
			entidade.setUsuario(usuario);
		}).construir();
		getMockMvc().perform(get("/api/item"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)));
	}

	@Test
	public void listarVazio() throws Exception {
		getMockMvc().perform(get("/api/item"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(0)));
	}

	@Test
	public void obterPorId() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		Item item = itemBuilder.customizar(entidade -> entidade.setUsuario(usuario)).construir();
		getMockMvc().perform(get("/api/item/" + item.getId()))
				.andExpect(status().isOk());
	}

	@Test
	public void obterPorId2() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		itemBuilder.customizar(entidade -> {
			entidade.setNome("Item Teste A");
			entidade.setUsuario(usuario);
		}).construir();
		Item item = itemBuilder.customizar(entidade -> {
			entidade.setNome("Item Teste B");
			entidade.setUsuario(usuario);
		}).construir();
		itemBuilder.customizar(entidade -> {
			entidade.setNome("Item Teste C");
			entidade.setUsuario(usuario);
		}).construir();
		getMockMvc().perform(get("/api/item/" + item.getId()))
				.andExpect(status().isOk());
	}

	@Test
	public void obterPorIdInvalido() throws Exception {
		getMockMvc().perform(get("/api/item/1"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvar() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		Item item = itemBuilder.construirEntidade();
		item.setUsuario(usuario);
		getMockMvc().perform(post("/api/item")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(itemMapper.toDto(item))))
				.andExpect(status().isCreated());
	}

	@Test
	public void salvarNomeInvalido() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		Item item = itemBuilder.construirEntidade();
		item.setNome("");
		item.setUsuario(usuario);
		getMockMvc().perform(post("/api/item")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(itemMapper.toDto(item))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvarNomeInvalido2() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		Item item = itemBuilder.construirEntidade();
		item.setNome(null);
		item.setUsuario(usuario);
		getMockMvc().perform(post("/api/item")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(itemMapper.toDto(item))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvarDisponibilidadeInvalido() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		Item item = itemBuilder.construirEntidade();
		item.setDisponibilidade(null);
		item.setUsuario(usuario);
		getMockMvc().perform(post("/api/item")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(itemMapper.toDto(item))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvarDescricaoeInvalido() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		Item item = itemBuilder.construirEntidade();
		item.setDescricao("");
		item.setUsuario(usuario);
		getMockMvc().perform(post("/api/item")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(itemMapper.toDto(item))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvarDescricaoeInvalido2() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		Item item = itemBuilder.construirEntidade();
		item.setDescricao(null);
		item.setUsuario(usuario);
		getMockMvc().perform(post("/api/item")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(itemMapper.toDto(item))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvarImagemInvalido() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		Item item = itemBuilder.construirEntidade();
		item.setImagem(null);
		item.setUsuario(usuario);
		getMockMvc().perform(post("/api/item")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(itemMapper.toDto(item))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvarImagemInvalido2() throws Exception {
		byte[] imagemInvalida = "".getBytes(StandardCharsets.UTF_8);

		Usuario usuario = usuarioBuilder.construir();
		Item item = itemBuilder.construirEntidade();
		item.setImagem(imagemInvalida);
		item.setUsuario(usuario);
		getMockMvc().perform(post("/api/item")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(itemMapper.toDto(item))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvarCategoriaInvalido() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		Item item = itemBuilder.construirEntidade();
		item.setCategoria(null);
		item.setUsuario(usuario);
		getMockMvc().perform(post("/api/item")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(itemMapper.toDto(item))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvarCategoriaInvalido2() throws Exception {
		Categoria categoria = new Categoria();
		categoria.setId(-5L);
		Usuario usuario = usuarioBuilder.construir();
		Item item = itemBuilder.construirEntidade();
		item.setCategoria(categoria);
		item.setUsuario(usuario);
		getMockMvc().perform(post("/api/item")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(itemMapper.toDto(item))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvarCategoriaInvalido3() throws Exception {
		Categoria categoria = new Categoria();
		categoria.setId(CATEGORIA_MAX + 5L);
		Usuario usuario = usuarioBuilder.construir();
		Item item = itemBuilder.construirEntidade();
		item.setCategoria(categoria);
		item.setUsuario(usuario);
		getMockMvc().perform(post("/api/item")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(itemMapper.toDto(item))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvarUsuarioInvalido() throws Exception {
		Item item = itemBuilder.construirEntidade();
		item.setUsuario(null);
		getMockMvc().perform(post("/api/item")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(itemMapper.toDto(item))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterar() throws Exception {
		byte[] imagemAlterada = "Byte array de imagem alterada".getBytes(StandardCharsets.UTF_8);

		Categoria categoriaAlterada = new Categoria();
		categoriaAlterada.setId(10L);

		Usuario usuario = usuarioBuilder.customizar(entidade -> {
			entidade.setCpf("24603471033");
			entidade.setEmail("testeA@gmail.com");
		}).construir();
		Usuario usuarioAlterado = usuarioBuilder.customizar(entidade -> {
			entidade.setCpf("24877455094");
			entidade.setEmail("testeB@gmail.com");
		}).construir();

		Item item = itemBuilder.customizar(entidade -> entidade.setUsuario(usuario)).construir();
		item.setNome("Nome Item Alterado");
		item.setDisponibilidade(false);
		item.setDescricao("Descrição Alterada");
		item.setImagem(imagemAlterada);
		item.setCategoria(categoriaAlterada);
		item.setUsuario(usuarioAlterado);

		getMockMvc().perform(put("/api/item")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(itemMapper.toDto(item))))
				.andExpect(status().isOk());
	}

	@Test
	public void alterarNomeInvalido() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		Item item = itemBuilder.customizar(entidade -> entidade.setUsuario(usuario)).construir();
		item.setNome("");
		getMockMvc().perform(put("/api/item")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(itemMapper.toDto(item))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterarDisponibilidadeInvalido() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		Item item = itemBuilder.customizar(entidade -> entidade.setUsuario(usuario)).construir();
		item.setDisponibilidade(null);
		getMockMvc().perform(put("/api/item")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(itemMapper.toDto(item))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterarDescricaoInvalido() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		Item item = itemBuilder.customizar(entidade -> entidade.setUsuario(usuario)).construir();
		item.setDescricao("");
		getMockMvc().perform(put("/api/item")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(itemMapper.toDto(item))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterarImagemInvalido() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		Item item = itemBuilder.customizar(entidade -> entidade.setUsuario(usuario)).construir();
		item.setImagem(null);
		getMockMvc().perform(put("/api/item")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(itemMapper.toDto(item))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterarImagemInvalido2() throws Exception {
		byte[] imagemInvalida = "".getBytes(StandardCharsets.UTF_8);

		Usuario usuario = usuarioBuilder.construir();
		Item item = itemBuilder.customizar(entidade -> entidade.setUsuario(usuario)).construir();
		item.setImagem(imagemInvalida);
		getMockMvc().perform(put("/api/item")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(itemMapper.toDto(item))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterarCategoriaInvalido() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		Item item = itemBuilder.customizar(entidade -> entidade.setUsuario(usuario)).construir();
		item.setCategoria(null);
		getMockMvc().perform(put("/api/item")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(itemMapper.toDto(item))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterarCategoriaInvalido2() throws Exception {
		Categoria categoria = new Categoria();
		categoria.setId(-5L);
		Usuario usuario = usuarioBuilder.construir();
		Item item = itemBuilder.customizar(entidade -> entidade.setUsuario(usuario)).construir();
		item.setCategoria(categoria);
		getMockMvc().perform(put("/api/item")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(itemMapper.toDto(item))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterarCategoriaInvalido3() throws Exception {
		Categoria categoria = new Categoria();
		categoria.setId(CATEGORIA_MAX + 5L);
		Usuario usuario = usuarioBuilder.construir();
		Item item = itemBuilder.customizar(entidade -> entidade.setUsuario(usuario)).construir();
		item.setCategoria(categoria);
		getMockMvc().perform(put("/api/item")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(itemMapper.toDto(item))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterarUsuarioInvalido() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		Item item = itemBuilder.customizar(entidade -> entidade.setUsuario(usuario)).construir();
		item.setUsuario(null);
		getMockMvc().perform(put("/api/item")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(itemMapper.toDto(item))))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void deletar() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		Item item = itemBuilder.customizar(entidade -> entidade.setUsuario(usuario)).construir();
		getMockMvc().perform(delete("/api/item/" + item.getId()))
				.andExpect(status().isOk());
	}

	@Test
	public void deletarInvalido() throws Exception {
		Usuario usuario = usuarioBuilder.construir();
		Item item = itemBuilder.customizar(entidade -> entidade.setUsuario(usuario)).construir();
		item.setId(item.getId() + 5L);
		getMockMvc().perform(delete("/api/item/" + item.getId()))
				.andExpect(status().isBadRequest());
	}
}
