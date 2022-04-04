package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.mapper;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Item;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.ItemDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface ItemMapper extends EntityMapper<ItemDto, Item> {

	@Override
	@Mapping(source = "categoriaId", target = "categoria.id")
	@Mapping(source = "usuarioId", target = "usuario.id")
	Item toEntity(ItemDto dto);

	@Override
	@InheritInverseConfiguration
	ItemDto toDto(Item entity);
}
