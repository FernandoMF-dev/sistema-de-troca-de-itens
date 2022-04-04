package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.repositorio;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Item;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.ItemDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepositorio extends JpaRepository<Item, Long> {
	@Query("SELECT new com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.ItemDto" +
			"(i.id, i.nome, i.imagem, i.descricao, i.disponibilidade, ic.id, iu.id) FROM Item i " +
			"JOIN i.categoria ic JOIN i.usuario iu")
	List<ItemDto> listarItem();

	List<Item> findAllByUsuarioId(Long id);

	@Query("SELECT new com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.ItemDto" +
			"(i.id, i.nome, i.imagem, i.descricao, i.disponibilidade, ic.id, iu.id) FROM Item i " +
			"JOIN i.categoria ic JOIN i.usuario iu WHERE i.disponibilidade = :disponibilidade")
	List<ItemDto> listarItemDisponivel(@Param("disponibilidade") Boolean disponibilidade);

	@Query("SELECT new com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.ItemDto" +
			"(i.id, i.nome, i.imagem, i.descricao, i.disponibilidade, ic.id, iu.id) FROM Item i " +
			"JOIN i.categoria ic JOIN i.usuario iu WHERE i.disponibilidade = :disponibilidade AND NOT iu.id = :usuarioId")
	List<ItemDto> listarItemDisponivelExcluirUsuario(@Param("disponibilidade") Boolean disponibilidade, @Param("usuarioId") Long usuarioId);

	@Query("SELECT new com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.ItemDto" +
			"(i.id, i.nome, i.imagem, i.descricao, i.disponibilidade, ic.id, iu.id) FROM Item i " +
			"JOIN i.categoria ic JOIN i.usuario iu WHERE iu.id = :usuarioId")
	List<ItemDto> listarItemPorUsuario(@Param("usuarioId") Long usuarioId);

	@Query("SELECT new com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.ItemDto" +
			"(i.id, i.nome, i.imagem, i.descricao, i.disponibilidade, ic.id, iu.id) FROM Item i " +
			"JOIN i.categoria ic JOIN i.usuario iu WHERE i.id = :id")
	ItemDto obterItemPorId(@Param("id") Long id);
}
