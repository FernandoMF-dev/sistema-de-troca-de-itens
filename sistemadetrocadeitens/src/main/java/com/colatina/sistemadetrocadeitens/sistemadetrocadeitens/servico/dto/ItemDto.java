package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Lob;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

	private Long id;

	@NotNull(message = "Nome invalido")
	@NotEmpty(message = "Nome invalido")
	private String nome;

	@NotNull(message = "Imagem invalida")
	@NotEmpty(message = "Imagem invalida")
	@Lob
	private byte[] imagem;

	@NotNull(message = "Descricao invalida")
	@NotEmpty(message = "Descricao invalida")
	private String descricao;

	@NotNull(message = "Disponibilidade invalida")
	private Boolean disponibilidade;

	@NotNull(message = "ID da categoria invalido")
	private Long categoriaId;

	@NotNull(message = "ID da categoria invalido")
	private Long usuarioId;

}
