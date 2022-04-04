package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.recurso;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.SistemadetrocadeitensApplication;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.builder.ItemBuilder;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.builder.OfertaBuilder;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.builder.UsuarioBuilder;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Item;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Oferta;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Usuario;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.repositorio.ItemRepositorio;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.repositorio.OfertaRepositorio;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.repositorio.UsuarioRepositorio;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.ItemServico;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.OfertaServico;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.ItemDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.OfertaDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.mapper.ItemMapper;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.mapper.OfertaMapper;
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

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SistemadetrocadeitensApplication.class)
@Transactional
public class OfertaRecursoIT extends IntTestComum {

	@Autowired
	private OfertaBuilder ofertaBuilder;
	@Autowired
	private OfertaMapper ofertaMapper;
	@Autowired
	private OfertaRepositorio ofertaRepositorio;
	@Autowired
	private OfertaServico ofertaServico;

	@Autowired
	private ItemBuilder itemBuilder;
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemServico itemServico;
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
		ofertaRepositorio.deleteAll();
		ofertaBuilder.customizar(null);
	}

	@Test
	public void listar() throws Exception {
		salvarOferta();
		getMockMvc().perform(get("/api/oferta"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(1)));
	}

	@Test
	public void listar2() throws Exception {
		Oferta oferta = salvarOferta();
		salvarOfertaExtra(oferta);
		getMockMvc().perform(get("/api/oferta"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)));
	}

	@Test
	public void listarVazio() throws Exception {
		getMockMvc().perform(get("/api/oferta"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(0)));
	}

	@Test
	public void obterPorId() throws Exception {
		Oferta oferta = salvarOferta();
		getMockMvc().perform(get("/api/oferta/" + oferta.getId()))
				.andExpect(status().isOk());
	}

	@Test
	public void obterPorIdInvalido() throws Exception {
		getMockMvc().perform(get("/api/oferta/1"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvar() throws Exception {
		Oferta oferta = criarOferta();
		getMockMvc().perform(post("/api/oferta")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(ofertaMapper.toDto(oferta))))
				.andExpect(status().isCreated());
	}

	@Test
	public void salvarItemOfertadoInvalido() throws Exception {
		OfertaDto ofertaDto = ofertaMapper.toDto(criarOferta());
		ofertaDto.setItensOfertados(null);
		getMockMvc().perform(post("/api/oferta")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(ofertaDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvarItemOfertadoInvalido2() throws Exception {
		Usuario usuario = usuarioBuilder.customizar(entidade -> {
			entidade.setCpf("65761446014");
			entidade.setEmail("testeC@gmail.com");
		}).construir();

		OfertaDto ofertaDto = ofertaMapper.toDto(criarOferta());
		ItemDto itemOfertanteDto = itemServico.obterPorId(ofertaDto.getItensOfertados().get(0));
		itemOfertanteDto.setUsuarioId(usuario.getId());
		itemServico.salvar(itemOfertanteDto);

		getMockMvc().perform(post("/api/oferta")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(ofertaDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvarItemDisponivelInvalido() throws Exception {
		OfertaDto ofertaDto = ofertaMapper.toDto(criarOferta());
		ofertaDto.setItemId(null);
		getMockMvc().perform(post("/api/oferta")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(ofertaDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvarItemDisponivelInvalido2() throws Exception {
		OfertaDto ofertaDto = ofertaMapper.toDto(criarOferta());
		ItemDto itemDisponivelDto = itemServico.obterPorId(ofertaDto.getItemId());
		itemDisponivelDto.setUsuarioId(ofertaDto.getUsuarioOfertanteId());
		itemServico.salvar(itemDisponivelDto);

		getMockMvc().perform(post("/api/oferta")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(ofertaDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void salvarItemDisponivelInvalido3() throws Exception {
		OfertaDto ofertaDto = ofertaMapper.toDto(criarOferta());
		ItemDto itemDisponivelDto = itemServico.obterPorId(ofertaDto.getItemId());
		itemDisponivelDto.setDisponibilidade(false);
		itemServico.salvar(itemDisponivelDto);

		getMockMvc().perform(post("/api/oferta")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(ofertaDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterar() throws Exception {
		Oferta oferta = criarOfertaAlterada(salvarOferta());
		getMockMvc().perform(put("/api/oferta")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(ofertaMapper.toDto(oferta))))
				.andExpect(status().isOk());
	}

	@Test
	public void alterarItemDisponivelInvalido() throws Exception {
		OfertaDto ofertaDto = ofertaMapper.toDto(salvarOferta());
		ofertaDto.setItemId(null);

		getMockMvc().perform(put("/api/oferta")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(ofertaDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterarItemDisponivelInvalido2() throws Exception {
		OfertaDto ofertaDto = ofertaMapper.toDto(salvarOferta());
		ItemDto itemDisponivelDto = itemServico.obterPorId(ofertaDto.getItemId());
		itemDisponivelDto.setUsuarioId(ofertaDto.getUsuarioOfertanteId());
		itemServico.salvar(itemDisponivelDto);

		getMockMvc().perform(put("/api/oferta")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(ofertaDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterarItemDisponivelInvalido3() throws Exception {
		OfertaDto ofertaDto = ofertaMapper.toDto(salvarOferta());
		ItemDto itemDisponivelDto = itemServico.obterPorId(ofertaDto.getItemId());
		itemDisponivelDto.setDisponibilidade(false);
		itemServico.salvar(itemDisponivelDto);

		getMockMvc().perform(put("/api/oferta")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(ofertaDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterarSituacaoInvalido() throws Exception {
		OfertaDto ofertaDto = ofertaMapper.toDto(salvarOferta());
		ofertaDto.setSituacaoId(null);

		getMockMvc().perform(put("/api/oferta")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(ofertaDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterarSituacaoInvalido2() throws Exception {
		OfertaDto ofertaDto = ofertaMapper.toDto(salvarOferta());
		ofertaDto.setSituacaoId(100L);

		getMockMvc().perform(put("/api/oferta")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(ofertaDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterarSituacaoInvalido3() throws Exception {
		OfertaDto ofertaDto = ofertaMapper.toDto(salvarOferta());
		ofertaServico.mudarSituacao(ofertaDto.getId(), 3L);

		getMockMvc().perform(patch("/api/oferta/aceitar/" + ofertaDto.getId())
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(ofertaDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterarUsuarioOfertanteInvalido() throws Exception {
		OfertaDto ofertaDto = ofertaMapper.toDto(salvarOferta());
		ofertaDto.setUsuarioOfertanteId(null);

		getMockMvc().perform(put("/api/oferta")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(ofertaDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterarItemOfertadoInvalido() throws Exception {
		OfertaDto ofertaDto = ofertaMapper.toDto(salvarOferta());
		ofertaDto.setItensOfertados(null);

		getMockMvc().perform(put("/api/oferta")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(ofertaDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void alterarItemOfertadoInvalido2() throws Exception {
		OfertaDto ofertaDto = ofertaMapper.toDto(salvarOferta());

		Usuario usuario = usuarioBuilder.customizar(entidade -> {
			entidade.setCpf("65761446014");
			entidade.setEmail("testeC@gmail.com");
		}).construir();

		Item itemOfertado = itemBuilder.customizar(entidade -> {
			entidade.setNome("Novo Item Disponivel");
			entidade.setUsuario(usuario);
		}).construir();

		ofertaDto.getItensOfertados().add(itemOfertado.getId());

		getMockMvc().perform(put("/api/oferta")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(ofertaDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void deletar() throws Exception {
		Oferta oferta = salvarOferta();
		getMockMvc().perform(delete("/api/oferta/" + oferta.getId()))
				.andExpect(status().isOk());
	}

	@Test
	public void deletarInvalido() throws Exception {
		getMockMvc().perform(delete("/api/oferta/1"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void aceitar() throws Exception {
		Oferta oferta = salvarOferta();
		getMockMvc().perform(patch("/api/oferta/aceitar/" + oferta.getId()))
				.andExpect(status().isOk());
	}

	@Test
	public void aceitarInvalido() throws Exception {
		getMockMvc().perform(patch("/api/oferta/aceitar/1"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void cancelar() throws Exception {
		Oferta oferta = salvarOferta();
		getMockMvc().perform(patch("/api/oferta/cancelar/" + oferta.getId()))
				.andExpect(status().isOk());
	}

	@Test
	public void cancelarInvalido() throws Exception {
		getMockMvc().perform(patch("/api/oferta/cancelar/1"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void recusar() throws Exception {
		Oferta oferta = salvarOferta();
		getMockMvc().perform(patch("/api/oferta/recusar/" + oferta.getId()))
				.andExpect(status().isOk());
	}

	@Test
	public void recusarInvalido() throws Exception {
		getMockMvc().perform(patch("/api/oferta/recusar/1"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void segundaRegraDeNegocio() throws Exception {
		Oferta oferta = salvarOferta();
		salvarOfertaExtra(oferta);
		getMockMvc().perform(patch("/api/oferta/aceitar/" + oferta.getId()))
				.andExpect(status().isOk());
	}

	@Test
	public void alterarDisponibilidadeDoItem() throws Exception {
		Oferta oferta = salvarOferta();
		Item itemDisponivel = itemMapper.toEntity(itemServico.obterPorId(oferta.getItem().getId()));
		itemDisponivel.setDisponibilidade(false);
		itemServico.salvar(itemMapper.toDto(itemDisponivel));
		getMockMvc().perform(get("/api/oferta/" + oferta.getId()))
				.andExpect(status().isOk());
	}

	private Oferta criarOferta() {
		Usuario usuarioDisponivel = usuarioBuilder.customizar(entidade -> {
			entidade.setCpf("24603471033");
			entidade.setEmail("testeA@gmail.com");
		}).construir();

		Usuario usuarioOfertante = usuarioBuilder.customizar(entidade -> {
			entidade.setCpf("24877455094");
			entidade.setEmail("testeB@gmail.com");
		}).construir();

		Item itemDisponivel = itemBuilder.customizar(entidade -> {
			entidade.setNome("Item Disponivel");
			entidade.setUsuario(usuarioDisponivel);
		}).construir();

		Item itemOfertado1 = itemBuilder.customizar(entidade -> {
			entidade.setNome("Item Ofertado 1");
			entidade.setUsuario(usuarioOfertante);
		}).construir();
		Item itemOfertado2 = itemBuilder.customizar(entidade -> {
			entidade.setNome("Item Ofertado 2");
			entidade.setUsuario(usuarioOfertante);
		}).construir();

		List<Item> itensOfertados = new ArrayList<>();
		itensOfertados.add(itemOfertado1);
		itensOfertados.add(itemOfertado2);

		Oferta oferta = ofertaBuilder.construirEntidade();
		oferta.setUsuarioOfertante(usuarioOfertante);
		oferta.setItem(itemDisponivel);
		oferta.setItensOfertados(itensOfertados);

		return oferta;
	}

	private Oferta criarOfertaAlterada(Oferta oferta) {
		Usuario novoUsuarioOfertante = usuarioBuilder.customizar(entidade -> {
			entidade.setCpf("65761446014");
			entidade.setEmail("testeC@gmail.com");
		}).construir();

		Item novoItemOfertado = itemBuilder.customizar(entidade -> {
			entidade.setNome("Novo Item Ofertado");
			entidade.setUsuario(novoUsuarioOfertante);
		}).construir();

		Item novoItemDisponivel = oferta.getItensOfertados().get(0);

		List<Item> novoItensOfertados = new ArrayList<>();
		novoItensOfertados.add(novoItemOfertado);

		oferta.setItem(novoItemDisponivel);
		oferta.setUsuarioOfertante(novoUsuarioOfertante);
		oferta.setItensOfertados(novoItensOfertados);

		return oferta;
	}

	private Oferta salvarOferta() {
		Oferta oferta = criarOferta();
		oferta = ofertaMapper.toEntity(ofertaServico.salvar(ofertaMapper.toDto(oferta)));

		return oferta;
	}

	private Oferta salvarOfertaExtra(Oferta oferta) {

		Usuario usuarioDisponivel = usuarioBuilder.customizar(entidade -> {
			entidade.setCpf("65761446014");
			entidade.setEmail("testeC@gmail.com");
		}).construir();

		Item itemDisponivel = itemBuilder.customizar(entidade -> {
			entidade.setNome("Item Disponivel Extra");
			entidade.setUsuario(usuarioDisponivel);
		}).construir();

		Oferta novaOferta = ofertaBuilder.construirEntidade();
		novaOferta.setItem(itemDisponivel);
		novaOferta.setUsuarioOfertante(oferta.getUsuarioOfertante());
		novaOferta.setItensOfertados(oferta.getItensOfertados());
		novaOferta = ofertaMapper.toEntity(ofertaServico.salvar(ofertaMapper.toDto(novaOferta)));

		return novaOferta;
	}

}
