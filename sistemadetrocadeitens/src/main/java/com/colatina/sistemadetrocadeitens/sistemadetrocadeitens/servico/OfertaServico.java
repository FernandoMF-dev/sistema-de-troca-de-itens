package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Oferta;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.repositorio.OfertaRepositorio;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.ItemDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.OfertaDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.OfertaListagemDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.UsuarioDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.exception.RegraNegocioException;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.mapper.OfertaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OfertaServico {

	private final OfertaRepositorio ofertaRepositorio;
	private final OfertaMapper ofertaMapper;
	private final ItemServico itemServico;
	private final EmailServico emailServico;
	private final UsuarioServico usuarioServico;

	private final Long ABERTA = 1L;
	private final Long ACEITAR = 2L;
	private final Long CANCELAR = 3L;
	private final Long RECUSAR = 4L;

	public List<OfertaListagemDto> listar() {
		return ofertaRepositorio.listarOferta();
	}

	public List<OfertaListagemDto> listarPorOfertante(Long ofertanteId) {
		return ofertaRepositorio.listarOfertaPorOfertante(ofertanteId, ABERTA);
	}

	public List<OfertaListagemDto> listarPorOfertado(Long ofertadoId) {
		return ofertaRepositorio.listarOfertaPorOfertado(ofertadoId, ABERTA);
	}

	public OfertaDto obterPorId(Long id) {
		Oferta oferta = ofertaRepositorio.findById(id).orElseThrow(() -> new RegraNegocioException("Oferta não encontrada"));
		return ofertaMapper.toDto(oferta);
	}

	public OfertaDto alterar(OfertaDto ofertaDto) {
		validarOferta(ofertaDto);
		obterPorId(ofertaDto.getId());
		return ofertaDtoSave(ofertaDto);
	}

	public OfertaDto salvar(OfertaDto dto) {
		dto.setSituacaoId(ABERTA);
		validarOferta(dto);
		OfertaDto ofertaDto = ofertaDtoSave(dto);
		UsuarioDto usuarioDtoDisponivel = itemServico.obterDono(ofertaDto.getItemId());
		UsuarioDto usuarioDtoOfertante = usuarioServico.obterPorId(ofertaDto.getUsuarioOfertanteId());
		emailServico.enviarEmailNovaOferta(dto, itemServico.obterPorId(dto.getItemId()), usuarioDtoDisponivel, usuarioDtoOfertante);
		return ofertaDto;
	}

	public void deletar(Long id) {
		Oferta oferta = ofertaMapper.toEntity(obterPorId(id));
		ofertaRepositorio.delete(oferta);
	}

	public OfertaDto mudarSituacao(Long id, Long novaSituacao) {
		OfertaDto ofertaDto = obterPorId(id);
		if (!ABERTA.equals(ofertaDto.getSituacaoId())) {
			throw new RegraNegocioException("Apenas ofertas em ABERTO podem ser ACEITAS/CANCELADAS/RECUSADAS");
		}
		if (ACEITAR.equals(novaSituacao)) {
			trocarItemOfertado(ofertaDto);
			trocarItemDisponivel(ofertaDto);
			cancelarOfertasComItem(ofertaDto.getItemId());
			ofertaDto.getItensOfertados().forEach(itemOfertado -> {
				cancelarOfertasComItem(itemOfertado);
			});
		}
		ofertaDto.setSituacaoId(novaSituacao);
		ofertaRepositorio.save(ofertaMapper.toEntity(ofertaDto));
		enviarEmailsSituacao(ofertaDto, novaSituacao);
		return ofertaDto;
	}

	public List<OfertaDto> salvarVarios(List<OfertaDto> ofertaDtos) {
		List<Oferta> ofertas = ofertaMapper.toEntity(ofertaDtos);
		ofertaRepositorio.saveAll(ofertas);
		return ofertaMapper.toDto(ofertas);
	}

	private OfertaDto ofertaDtoSave(OfertaDto ofertaDto) {
		Oferta oferta = ofertaMapper.toEntity(ofertaDto);
		ofertaRepositorio.save(oferta);
		return ofertaMapper.toDto(oferta);
	}

	private void validarOferta(OfertaDto ofertaDto) {
		validarSituacao(ofertaDto.getSituacaoId());
		validarDisponibilidade(ofertaDto.getItemId());
		validarDonoDoItemDisponivel(ofertaDto);
		validarDonoDosItensOfertados(ofertaDto);
	}

	private void validarSituacao(Long situacao) {
		List<Long> situacaoValida = new ArrayList<>();
		situacaoValida.add(ABERTA);
		situacaoValida.add(ACEITAR);
		situacaoValida.add(CANCELAR);
		situacaoValida.add(RECUSAR);

		if (!situacaoValida.contains(situacao)) {
			throw new RegraNegocioException("Situação da oferta inválida");
		}
	}

	private void validarDisponibilidade(Long itemDisponivelId) {
		ItemDto itemDto = itemServico.obterPorId(itemDisponivelId);
		if (!itemDto.getDisponibilidade()) {
			throw new RegraNegocioException("Você só pode fazer uma oferta por um item caso ele esta disponível");
		}
	}

	private void validarDonoDoItemDisponivel(OfertaDto ofertaDto) {
		UsuarioDto usuarioDto = itemServico.obterDono(ofertaDto.getItemId());
		if (ofertaDto.getUsuarioOfertanteId().equals(usuarioDto.getId())) {
			throw new RegraNegocioException("Um usuario não pode propor uma troca por um item que já o pertence");
		}
	}

	private void validarDonoDosItensOfertados(OfertaDto ofertaDto) {
		ofertaDto.getItensOfertados().forEach(itemOfertado -> {
			UsuarioDto usuarioDto = itemServico.obterDono(itemOfertado);
			if (!ofertaDto.getUsuarioOfertanteId().equals(usuarioDto.getId())) {
				throw new RegraNegocioException("Um ou mais dos itens ofertados não pertencem ao usuario ofertante");
			}
		});
	}

	private void trocarItemOfertado(OfertaDto ofertaDto) {
		UsuarioDto usuarioDto = itemServico.obterDono(ofertaDto.getItemId());
		List<ItemDto> itensDto = new ArrayList<>();
		ofertaDto.getItensOfertados().forEach(itemOfertado -> {
			ItemDto dto = itemServico.obterPorId(itemOfertado);
			dto.setDisponibilidade(false);
			dto.setUsuarioId(usuarioDto.getId());
			itensDto.add(dto);
		});
		itemServico.salvarVarios(itensDto);

	}

	private void trocarItemDisponivel(OfertaDto ofertaDto) {
		ItemDto itemDto = itemServico.obterPorId(ofertaDto.getItemId());
		itemDto.setDisponibilidade(false);
		itemDto.setUsuarioId(ofertaDto.getUsuarioOfertanteId());
		itemServico.salvar(itemDto);
	}

	private void cancelarOfertasComItem(Long idItem) {
		List<OfertaDto> ofertaDtos = ofertaMapper.toDto(ofertaRepositorio.findAllBySituacao_Id(ABERTA));
		List<OfertaDto> ofertaDtosCanceladas = new ArrayList<>();
		ofertaDtos.forEach(oferta -> {
			if (idItem.equals(oferta.getItemId()) || oferta.getItensOfertados().contains(idItem)) {
				oferta.setSituacaoId(CANCELAR);
				ofertaDtosCanceladas.add(oferta);
			}
		});
		if (!ofertaDtosCanceladas.isEmpty()) {
			salvarVarios(ofertaDtosCanceladas);
		}
	}

	private void enviarEmailsSituacao(OfertaDto ofertaDto, Long situacaoId) {
		UsuarioDto usuarioDtoDisponivel = itemServico.obterDono(ofertaDto.getItemId());
		UsuarioDto usuarioDtoOfertante = usuarioServico.obterPorId(ofertaDto.getUsuarioOfertanteId());
		if (ACEITAR.equals(situacaoId)) {
			emailServico.enviarEmailOfertaAceita(usuarioDtoDisponivel, usuarioDtoOfertante);
		} else if (CANCELAR.equals(situacaoId)) {
			emailServico.enviarEmailOfertaCancelada(usuarioDtoDisponivel, usuarioDtoOfertante);
		} else if (RECUSAR.equals(situacaoId)) {
			emailServico.enviarEmailOfertaRecusada(usuarioDtoDisponivel, usuarioDtoOfertante);
		}
	}
}
