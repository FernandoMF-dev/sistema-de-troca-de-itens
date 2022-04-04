package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OfertaDto {

	private Long id;

	private Long situacaoId;

	@NotNull(message = "ID do usuário ofertante inválido")
	private Long usuarioOfertanteId;

	@NotNull(message = "ID do item desejado inválido")
	private Long itemId;

	@NotNull(message = "É preciso ofertar pelo menos 1 (um) item para trocar com outro")
	@NotEmpty(message = "É preciso ofertar pelo menos 1 (um) item para trocar com outro")
	private List<Long> itensOfertados;

}
