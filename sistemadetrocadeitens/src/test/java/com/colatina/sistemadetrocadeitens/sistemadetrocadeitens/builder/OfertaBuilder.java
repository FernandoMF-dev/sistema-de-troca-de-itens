package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.builder;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Oferta;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.OfertaServico;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.OfertaDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.mapper.OfertaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OfertaBuilder extends ConstrutorEntidade<Oferta> {

	@Autowired
	private OfertaServico ofertaServico;

	@Autowired
	private OfertaMapper ofertaMapper;

	@Override
	public Oferta construirEntidade() {
		Oferta oferta = new Oferta();
		return oferta;
	}

	@Override
	public Oferta persistir(Oferta entidade) {
		OfertaDto dto = ofertaMapper.toDto(entidade);
		return ofertaMapper.toEntity(ofertaServico.salvar(dto));
	}
}
