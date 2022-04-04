package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Usuario;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.repositorio.UsuarioRepositorio;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.LoginDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.UsuarioDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.exception.RegraNegocioException;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginServico {
	private final UsuarioRepositorio usuarioRepositorio;
	private final UsuarioMapper usuarioMapper;

	public UsuarioDto obterPorToken(String token) {
		Usuario usuario = usuarioRepositorio.findByToken(token);
		if (usuario == null) {
			throw new RegraNegocioException("Token não encontrado");
		}
		return usuarioMapper.toDto(usuario);
	}

	public UsuarioDto logar(LoginDto loginDto) {
		Usuario usuario = usuarioRepositorio.findByEmailAndToken(loginDto.getEmail(), loginDto.getToken());
		if (usuario == null) {
			throw new RegraNegocioException("Email e/ou Token invalido(s)");
		}
		return usuarioMapper.toDto(usuario);
	}
}
