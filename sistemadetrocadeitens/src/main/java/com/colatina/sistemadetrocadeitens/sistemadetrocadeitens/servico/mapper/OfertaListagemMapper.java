package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.mapper;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Oferta;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.OfertaListagemDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OfertaListagemMapper extends EntityMapper<OfertaListagemDto, Oferta> {

	@Override
	@Mapping(source = "usuarioOfertanteId", target = "usuarioOfertante.id")
	@Mapping(source = "itemId", target = "item.id")
	@Mapping(source = "situacaoId", target = "situacao.id")
	Oferta toEntity(OfertaListagemDto dto);

	@Override
	@InheritInverseConfiguration
	OfertaListagemDto toDto(Oferta dto);
}
