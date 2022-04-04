package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {

	private String destinatario;

	private String assunto;

	private List<String> copias;

	private String texto;
}
