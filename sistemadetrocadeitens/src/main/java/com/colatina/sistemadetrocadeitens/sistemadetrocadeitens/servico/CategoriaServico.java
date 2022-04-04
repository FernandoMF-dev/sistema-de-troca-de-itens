package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.dominio.Categoria;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.repositorio.CategoriaRepositorio;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.CategoriaDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.exception.RegraNegocioException;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.mapper.CategoriaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServico {
	private final CategoriaRepositorio categoriaRepositorio;
	private final CategoriaMapper categoriaMapper;

	public List<CategoriaDto> listar() {
		List<Categoria> categorias = categoriaRepositorio.findAll();
		return categoriaMapper.toDto(categorias);
	}

	public CategoriaDto obterPorId(Long id) {
		Categoria categoria = categoriaRepositorio.findById(id).orElseThrow(() -> new RegraNegocioException("Usuario nao encontrado"));
		return categoriaMapper.toDto(categoria);
	}
}
