package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "tb_item")
@AllArgsConstructor
@NoArgsConstructor
public class Item implements Serializable {
	@Id
	@Column(name = "id")
	@SequenceGenerator(allocationSize = 1, sequenceName = "seq_tb_item", name = "seq_tb_item")
	@GeneratedValue(generator = "seq_tb_item", strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "nome", nullable = false)
	private String nome;

	@Lob
	@Column(name = "imagem", nullable = false)
	private byte[] imagem;

	@Column(name = "descricao", nullable = false)
	private String descricao;

	@Column(name = "disponibilidade", nullable = false)
	private Boolean disponibilidade;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_categoria")
	private Categoria categoria;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;
}
