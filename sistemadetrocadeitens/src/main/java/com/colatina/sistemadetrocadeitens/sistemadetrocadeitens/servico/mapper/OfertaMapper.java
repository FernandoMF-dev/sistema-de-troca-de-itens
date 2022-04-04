package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.mapper;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Item;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Oferta;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.OfertaDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", uses = {ItemMapper.class})
public interface OfertaMapper extends EntityMapper<OfertaDto, Oferta> {

	@AfterMapping
	default void mapearToEntity(OfertaDto ofertaDto, @MappingTarget Oferta oferta) {
		List<Item> itens = new ArrayList<>();
		ofertaDto.getItensOfertados().forEach(itemId -> {
			Item item = new Item();
			item.setId(itemId);
			itens.add(item);
		});
		oferta.setItensOfertados(itens);
	}

	@Override
	@Mapping(source = "usuarioOfertanteId", target = "usuarioOfertante.id")
	@Mapping(source = "itemId", target = "item.id")
	@Mapping(source = "situacaoId", target = "situacao.id")
	@Mapping(target = "itensOfertados", ignore = true)
	Oferta toEntity(OfertaDto dto);

	@AfterMapping
	default void mapearToDto(Oferta oferta, @MappingTarget OfertaDto ofertaDto) {
		ofertaDto.setItensOfertados(oferta.getItensOfertados().stream().map(itemOfertado -> itemOfertado.getId()).collect(Collectors.toList()));
	}

	@Override
	@InheritInverseConfiguration
	@Mapping(target = "itensOfertados", ignore = true)
	OfertaDto toDto(Oferta dto);

}
