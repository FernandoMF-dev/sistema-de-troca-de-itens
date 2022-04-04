package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OfertaListagemDto {

	private Long id;

	private Long situacaoId;

	private Long usuarioOfertanteId;

	private Long itemId;
}
