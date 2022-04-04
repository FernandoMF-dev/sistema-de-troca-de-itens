package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tb_oferta")
public class Oferta {
	@Id
	@Column(name = "id")
	@SequenceGenerator(allocationSize = 1, sequenceName = "seq_tb_oferta", name = "seq_tb_oferta")
	@GeneratedValue(generator = "seq_tb_oferta", strategy = GenerationType.SEQUENCE)
	private Long id;

	@ManyToMany
	@JoinTable(name = "tb_item_ofertado",
			joinColumns = @JoinColumn(name = "id_oferta"),
			inverseJoinColumns = @JoinColumn(name = "id_item_ofertado"))
	private List<Item> itensOfertados;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_situacao")
	private Situacao situacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_ofertante")
	private Usuario usuarioOfertante;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_item")
	private Item item;
}
