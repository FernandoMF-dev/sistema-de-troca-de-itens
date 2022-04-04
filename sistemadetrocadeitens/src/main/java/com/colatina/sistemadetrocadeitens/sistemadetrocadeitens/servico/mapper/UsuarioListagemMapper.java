package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.mapper;


import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Usuario;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.UsuarioListagemDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioListagemMapper extends EntityMapper<UsuarioListagemDto, Usuario> {

}
