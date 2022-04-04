package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.builder;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Categoria;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Item;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.ItemServico;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.ItemDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.mapper.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class ItemBuilder extends ConstrutorEntidade<Item> {

	@Autowired
	private ItemServico itemServico;

	@Autowired
	private ItemMapper itemMapper;

	@Override
	public Item construirEntidade() {
		Item item = new Item();
		item.setNome("Item Teste");
		item.setDisponibilidade(true);
		item.setDescricao("Descrição do Item Teste");
		item.setImagem("Byte array de imagem para Item teste".getBytes(StandardCharsets.UTF_8));
		item.setCategoria(iniciarCategoria());
		return item;
	}

	@Override
	public Item persistir(Item entidade) {
		ItemDto dto = itemMapper.toDto(entidade);
		return itemMapper.toEntity(itemServico.salvar(dto));
	}

	private Categoria iniciarCategoria() {
		Categoria categoria = new Categoria();
		categoria.setId(1L);
		return categoria;
	}
}
