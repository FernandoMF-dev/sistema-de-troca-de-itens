package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.recurso;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.LoginServico;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.LoginDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.UsuarioDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginRecurso {

	private final LoginServico loginServico;

	@GetMapping("/{token}")
	public ResponseEntity<UsuarioDto> obterPorToken(@PathVariable("token") String token) {
		UsuarioDto usuarioDto = loginServico.obterPorToken(token);
		return new ResponseEntity<>(usuarioDto, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<UsuarioDto> logar(@Valid @RequestBody LoginDto dto) {
		UsuarioDto usuarioDto = loginServico.logar(dto);
		return new ResponseEntity<>(usuarioDto, HttpStatus.OK);
	}
}
