package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDto {

	private Long id;

	@NotNull(message = "Nome invalido")
	@NotEmpty(message = "Nome invalido")
	private String nome;

	@NotNull(message = "CPF invalido")
	@CPF(message = "CPF invalido")
	private String cpf;

	@NotNull(message = "Email invalido")
	@Email(message = "Email invalido")
	private String email;

	@NotNull(message = "Data de Nascimento invalida")
	@Past(message = "Data de Nascimento invalida")
	private LocalDate data;
}
