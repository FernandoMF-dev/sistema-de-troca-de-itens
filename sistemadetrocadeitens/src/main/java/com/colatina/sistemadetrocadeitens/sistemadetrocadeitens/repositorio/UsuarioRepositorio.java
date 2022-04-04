package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.repositorio;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Usuario;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.UsuarioDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.UsuarioListagemDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
	@Query("SELECT new com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.UsuarioListagemDto" +
			"(u.id, u.nome, u.email, u.data) FROM Usuario u")
	List<UsuarioListagemDto> listarUsuario();

	@Query("SELECT new com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.UsuarioDto" +
			"(u.id, u.nome, u.cpf, u.email, u.data) FROM Usuario u " +
			"WHERE u.id = :id")
	UsuarioDto obterUsuarioPorId(@Param("id") Long id);

	@Query("SELECT u.token FROM Usuario u WHERE u.id = :id")
	String obterToken(@Param("id") Long id);

	List<Usuario> findByCpfOrEmail(String cpf, String email);

	Usuario findByToken(String token);

	Usuario findByEmailAndToken(String email, String token);
}
