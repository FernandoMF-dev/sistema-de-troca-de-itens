package com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.recurso;

import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.ItemServico;
import com.colatina.sistemadetrocadeitens.sistemadetrocadeitens.servico.dto.ItemDto;
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
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemRecurso {

	private final ItemServico itemServico;

	@GetMapping
	public ResponseEntity<List<ItemDto>> listar() {
		List<ItemDto> itemDtos = itemServico.listar();
		return new ResponseEntity<>(itemDtos, HttpStatus.OK);
	}

	@GetMapping("/inventario-{id}")
	public ResponseEntity<List<ItemDto>> obterPorUsuario(@PathVariable("id") Long id) {
		List<ItemDto> itemDtos = itemServico.listarInventario(id);
		return new ResponseEntity<>(itemDtos, HttpStatus.OK);
	}

	@GetMapping("/disponivel")
	public ResponseEntity<List<ItemDto>> listarDisponivel() {
		List<ItemDto> itemDtos = itemServico.listarDisponivel();
		return new ResponseEntity<>(itemDtos, HttpStatus.OK);
	}

	@GetMapping("/disponivel/{id}")
	public ResponseEntity<List<ItemDto>> listarDisponivelExcluirUsuario(@PathVariable("id") Long usuarioId) {
		List<ItemDto> itemDtos = itemServico.listarDisponivelExcluirUsuario(usuarioId);
		return new ResponseEntity<>(itemDtos, HttpStatus.OK);
	}

	@GetMapping("/dono/{id}")
	public ResponseEntity<List<ItemDto>> listarPorDono(@PathVariable("id") Long usuarioId) {
		List<ItemDto> itemDtos = itemServico.listarPorDono(usuarioId);
		return new ResponseEntity<>(itemDtos, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ItemDto> obterPorId(@PathVariable("id") Long id) {
		ItemDto itemDto = itemServico.obterPorId(id);
		return new ResponseEntity<>(itemDto, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ItemDto> salvar(@Valid @RequestBody ItemDto dto) {
		ItemDto itemDto = itemServico.salvar(dto);
		return new ResponseEntity<>(itemDto, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<ItemDto> alterar(@Valid @RequestBody ItemDto dto) {
		ItemDto itemDto = itemServico.salvar(dto);
		return new ResponseEntity<>(itemDto, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable("id") Long id) {
		itemServico.deletar(id);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

}
