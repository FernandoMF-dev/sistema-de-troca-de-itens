package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Item;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Oferta;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.repositorio.ItemRepositorio;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.repositorio.OfertaRepositorio;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.ItemDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.OfertaDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.UsuarioDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.exception.RegraNegocioException;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.mapper.ItemMapper;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.mapper.OfertaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServico {

	private final ItemRepositorio itemRepositorio;
	private final ItemMapper itemMapper;
	private final UsuarioServico usuarioServico;

	private final OfertaRepositorio ofertaRepositorio;
	private final OfertaMapper ofertaMapper;

	private final Long SITUACAO_ABERTA = 1L;
	private final Long SITUACAO_CANCELAR = 3L;
	private final Long CATEGORIA_MAX = 25L;

	public List<ItemDto> listar() {
		return itemRepositorio.listarItem();
	}

	public List<ItemDto> listarDisponivel() {
		return itemRepositorio.listarItemDisponivel(true);
	}

	public List<ItemDto> listarDisponivelExcluirUsuario(Long usuarioId) {
		return itemRepositorio.listarItemDisponivelExcluirUsuario(true, usuarioId);
	}

	public List<ItemDto> listarPorDono(Long usuarioId) {
		return itemRepositorio.listarItemPorUsuario(usuarioId);
	}

	public ItemDto obterPorId(Long id) {
		ItemDto itemDto = itemRepositorio.obterItemPorId(id);
		if (itemDto == null) {
			throw (new RegraNegocioException("Item nao encontrado"));
		}
		return itemDto;
	}

	public ItemDto salvar(ItemDto itemDto) {
		validarCategoria(itemDto);
		usuarioServico.obterPorId(itemDto.getUsuarioId());
		Item item = itemMapper.toEntity(itemDto);
		if (item.getId() != null) {
			if (!itemDto.getDisponibilidade() && obterPorId(itemDto.getId()).getDisponibilidade()) {
				cancelarOfertasComItem(itemDto);
			}
		}
		itemRepositorio.save(item);
		return itemMapper.toDto(item);
	}

	public List<ItemDto> salvarVarios(List<ItemDto> itensDto) {
		List<Item> itens = itemMapper.toEntity(itensDto);
		itemRepositorio.saveAll(itens);
		return itemMapper.toDto(itens);
	}

	public List<ItemDto> listarInventario(Long id) {
		List<Item> items = itemRepositorio.findAllByUsuarioId(id);
		List<ItemDto> itemDtos;

		itemDtos = itemMapper.toDto(items);

		if (itemDtos == null) {
			throw (new RegraNegocioException("Item nao encontrado"));
		}
		return itemDtos;
	}


	public void deletar(Long id) {
		Item item = itemMapper.toEntity(obterPorId(id));
		itemRepositorio.delete(item);
	}

	public UsuarioDto obterDono(Long id) {
		ItemDto itemDto = obterPorId(id);
		return usuarioServico.obterPorId(itemDto.getUsuarioId());
	}

	private void cancelarOfertasComItem(ItemDto itemDto) {
		List<OfertaDto> ofertaDtos = ofertaMapper.toDto(ofertaRepositorio.findAllBySituacao_Id(SITUACAO_ABERTA));
		List<OfertaDto> ofertaDtosCanceladas = new ArrayList<>();
		ofertaDtos.forEach(oferta -> {
			if (itemDto.getId().equals(oferta.getItemId()) || oferta.getItensOfertados().contains(itemDto.getId())) {
				oferta.setSituacaoId(SITUACAO_CANCELAR);
				ofertaDtosCanceladas.add(oferta);
			}
		});
		if (!ofertaDtosCanceladas.isEmpty()) {
			List<Oferta> ofertas = ofertaMapper.toEntity(ofertaDtos);
			ofertaRepositorio.saveAll(ofertas);
		}
	}

	private void validarCategoria(ItemDto itemDto) {
		if (itemDto.getCategoriaId() < 1L || itemDto.getCategoriaId() > CATEGORIA_MAX) {
			throw new RegraNegocioException("ID da categoria invalido");
		}
	}

}
