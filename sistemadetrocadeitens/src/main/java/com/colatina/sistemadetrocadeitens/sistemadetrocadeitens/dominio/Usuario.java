package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "tb_usuario")
public class Usuario {
	@Id
	@Column(name = "id")
	@SequenceGenerator(allocationSize = 1, sequenceName = "seq_tb_usuario", name = "seq_tb_usuario")
	@GeneratedValue(generator = "seq_tb_usuario", strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "nome", nullable = false)
	private String nome;

	@Column(name = "cpf", nullable = false, unique = true)
	private String cpf;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "dt_nascimento", nullable = false)
	private LocalDate data;

	@Column(name = "token", nullable = false)
	private String token;
}
