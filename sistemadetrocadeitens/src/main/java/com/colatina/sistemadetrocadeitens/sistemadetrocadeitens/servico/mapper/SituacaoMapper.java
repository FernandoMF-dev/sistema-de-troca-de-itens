package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.mapper;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Situacao;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.SituacaoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SituacaoMapper extends EntityMapper<SituacaoDto, Situacao> {
}