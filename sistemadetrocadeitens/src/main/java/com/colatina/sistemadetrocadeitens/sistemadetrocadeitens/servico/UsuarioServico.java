package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Usuario;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.repositorio.UsuarioRepositorio;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.UsuarioDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.UsuarioListagemDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.exception.RegraNegocioException;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServico {
	private final UsuarioRepositorio usuarioRepositorio;
	private final UsuarioMapper usuarioMapper;
	private final EmailServico emailServico;

	public List<UsuarioListagemDto> listar() {
		return usuarioRepositorio.listarUsuario();
	}

	public UsuarioDto obterPorId(Long id) {
		UsuarioDto usuarioDto = usuarioRepositorio.obterUsuarioPorId(id);
		if (usuarioDto == null) {
			throw (new RegraNegocioException("Usuario nao encontrado"));
		}
		return usuarioDto;
	}

	public UsuarioDto salvar(UsuarioDto usuarioDto) {
		validarDadosDuplicados(usuarioDto);
		Usuario usuario = usuarioMapper.toEntity(usuarioDto);
		if (usuario.getId() == null) {
			usuario.setToken(UUID.randomUUID().toString().replace("-", ""));
			emailServico.enviarEmailNovoUsuario(usuario);
		} else {
			Usuario usuarioSalvo = findById(usuario.getId());
			usuario.setToken(usuarioSalvo.getToken());
		}
		usuarioRepositorio.save(usuario);
		return usuarioMapper.toDto(usuario);
	}

	public void deletar(Long id) {
		Usuario usuario = findById(id);
		usuarioRepositorio.delete(usuario);
	}

	private void validarDadosDuplicados(UsuarioDto dto) {
		List<Usuario> usuarios = usuarioRepositorio.findByCpfOrEmail(dto.getCpf(), dto.getEmail());
		usuarios.forEach(usuario -> {
			if (usuario != null && !usuario.getId().equals(dto.getId())) {
				if (usuario.getCpf().equals(dto.getCpf())) {
					throw new RegraNegocioException("Email já Cadastrado.");
				} else if (usuario.getEmail().equals(dto.getEmail())) {
					throw new RegraNegocioException("Email já Cadastrado.");
				}
			}
		});
	}

	private Usuario findById(Long id) {
		return usuarioRepositorio.findById(id).orElseThrow(() -> new RegraNegocioException("Usuario nao encontrado"));
	}
}
