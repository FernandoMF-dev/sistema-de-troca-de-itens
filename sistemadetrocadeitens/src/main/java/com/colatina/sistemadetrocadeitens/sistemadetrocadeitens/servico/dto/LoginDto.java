package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
	@NotNull(message = "Email invalido")
	@Email(message = "Email invalido")
	private String email;

	@NotNull(message = "Token invalido")
	@NotEmpty(message = "Token invalido")
	private String token;
}
