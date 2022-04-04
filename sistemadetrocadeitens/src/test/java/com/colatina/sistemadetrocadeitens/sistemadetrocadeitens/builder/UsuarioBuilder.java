package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.builder;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Usuario;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.UsuarioServico;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.UsuarioDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.mapper.UsuarioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UsuarioBuilder extends ConstrutorEntidade<Usuario> {

	@Autowired
	private UsuarioServico usuarioServico;

	@Autowired
	private UsuarioMapper usuarioMapper;

	@Override
	public Usuario construirEntidade() {
		Usuario usuario = new Usuario();
		usuario.setNome("Usuario Teste");
		usuario.setCpf("16466121089");
		usuario.setEmail("teste@gmail.com");
		usuario.setData(LocalDate.now().minusDays(2L));
		return usuario;
	}

	@Override
	public Usuario persistir(Usuario entidade) {
		UsuarioDto dto = usuarioMapper.toDto(entidade);
		return usuarioMapper.toEntity(usuarioServico.salvar(dto));
	}
}
