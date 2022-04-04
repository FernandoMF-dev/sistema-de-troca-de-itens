package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Situacao;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.repositorio.SituacaoRepositorio;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.SituacaoDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.mapper.SituacaoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SituacaoServico {
	private final SituacaoRepositorio situacaoRepositorio;
	private final SituacaoMapper situacaoMapper;

	public List<SituacaoDto> listar() {
		List<Situacao> situacaos = situacaoRepositorio.findAll();
		return situacaoMapper.toDto(situacaos);
	}
}
