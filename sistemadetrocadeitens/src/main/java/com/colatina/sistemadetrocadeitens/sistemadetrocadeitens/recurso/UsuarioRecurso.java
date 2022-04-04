package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.recurso;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.UsuarioServico;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.UsuarioDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.UsuarioListagemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
public class UsuarioRecurso {

	private final UsuarioServico usuarioServico;


	@GetMapping
	public ResponseEntity<List<UsuarioListagemDto>> listar() {
		List<UsuarioListagemDto> usuarioListagemDto = usuarioServico.listar();
		return new ResponseEntity<>(usuarioListagemDto, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UsuarioDto> obterPorId(@PathVariable("id") Long id) {
		UsuarioDto usuarioDto = usuarioServico.obterPorId(id);
		return new ResponseEntity<>(usuarioDto, HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<UsuarioDto> alterar(@Valid @RequestBody UsuarioDto dto) {
		UsuarioDto usuarioDto = usuarioServico.salvar(dto);
		return new ResponseEntity<>(usuarioDto, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<UsuarioDto> salvar(@Valid @RequestBody UsuarioDto dto) {
		UsuarioDto usuarioDto = usuarioServico.salvar(dto);
		return new ResponseEntity<>(usuarioDto, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable("id") Long id) {
		usuarioServico.deletar(id);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
}
