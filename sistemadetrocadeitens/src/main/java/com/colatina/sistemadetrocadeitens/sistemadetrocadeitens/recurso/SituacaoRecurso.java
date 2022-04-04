package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.recurso;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.SituacaoServico;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.SituacaoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/situacao")
@RequiredArgsConstructor
public class SituacaoRecurso {
	private final SituacaoServico situacaoServico;

	@GetMapping
	public ResponseEntity<List<SituacaoDto>> listar() {
		List<SituacaoDto> situacoes = situacaoServico.listar();
		return new ResponseEntity<>(situacoes, HttpStatus.OK);
	}
}
