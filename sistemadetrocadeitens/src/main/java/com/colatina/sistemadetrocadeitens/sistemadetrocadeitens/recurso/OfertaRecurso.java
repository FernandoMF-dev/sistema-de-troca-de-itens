package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.recurso;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.OfertaServico;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.OfertaDto;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.OfertaListagemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/oferta")
@RequiredArgsConstructor
public class OfertaRecurso {

	private final OfertaServico ofertaServico;

	private final Long ACEITAR = 2L;
	private final Long CANCELAR = 3L;
	private final Long RECUSAR = 4L;


	@GetMapping("/{id}")
	public ResponseEntity<OfertaDto> obter(@PathVariable("id") Long id) {
		OfertaDto ofertaDto = ofertaServico.obterPorId(id);
		return new ResponseEntity<>(ofertaDto, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<OfertaListagemDto>> listar() {
		List<OfertaListagemDto> ofertaListagemDto = ofertaServico.listar();
		return new ResponseEntity<>(ofertaListagemDto, HttpStatus.OK);
	}

	@GetMapping("/ofertante/{id}")
	public ResponseEntity<List<OfertaListagemDto>> listarPorOfertante(@PathVariable("id") Long usuarioOfertanteId) {
		List<OfertaListagemDto> ofertaListagemDto = ofertaServico.listarPorOfertante(usuarioOfertanteId);
		return new ResponseEntity<>(ofertaListagemDto, HttpStatus.OK);
	}

	@GetMapping("/ofertado/{id}")
	public ResponseEntity<List<OfertaListagemDto>> listarPorOfertado(@PathVariable("id") Long usuarioOfertadoId) {
		List<OfertaListagemDto> ofertaListagemDto = ofertaServico.listarPorOfertado(usuarioOfertadoId);
		return new ResponseEntity<>(ofertaListagemDto, HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<OfertaDto> alterar(@Valid @RequestBody OfertaDto dto) {
		OfertaDto ofertaDto = ofertaServico.alterar(dto);
		return new ResponseEntity<>(ofertaDto, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<OfertaDto> salvar(@Valid @RequestBody OfertaDto dto) {
		OfertaDto ofertaDto = ofertaServico.salvar(dto);
		return new ResponseEntity<>(ofertaDto, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable("id") Long id) {
		ofertaServico.deletar(id);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@PatchMapping("/aceitar/{id}")
	public ResponseEntity<OfertaDto> aceitar(@PathVariable("id") Long id) {
		OfertaDto ofertaDto = ofertaServico.mudarSituacao(id, ACEITAR);
		return new ResponseEntity<>(ofertaDto, HttpStatus.OK);
	}

	@PatchMapping("/cancelar/{id}")
	public ResponseEntity<OfertaDto> cancelar(@PathVariable("id") Long id) {
		OfertaDto ofertaDto = ofertaServico.mudarSituacao(id, CANCELAR);
		return new ResponseEntity<>(ofertaDto, HttpStatus.OK);
	}

	@PatchMapping("/recusar/{id}")
	public ResponseEntity<OfertaDto> recusar(@PathVariable("id") Long id) {
		OfertaDto ofertaDto = ofertaServico.mudarSituacao(id, RECUSAR);
		return new ResponseEntity<>(ofertaDto, HttpStatus.OK);
	}
}
