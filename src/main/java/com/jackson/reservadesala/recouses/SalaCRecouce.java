package com.jackson.reservadesala.recouses;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jackson.reservadesala.domain.SalaC;
import com.jackson.reservadesala.dto.SalaCDTO;
import com.jackson.reservadesala.service.SalaCService;

@RestController
@RequestMapping(value="/resevasDaSalaC")
public class SalaCRecouce {
	
	@Autowired
	private SalaCService service;
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<SalaC> find(@PathVariable Integer id){
		SalaC obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@RequestMapping(value="/dataDaReserva", method=RequestMethod.GET)
	public ResponseEntity<SalaC> find(@RequestParam(value="value") Date dataDaReserva){
		SalaC obj = service.findByData(dataDaReserva);
		return ResponseEntity.ok().body(obj);
	}
	
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> insert(@Valid @RequestBody SalaC obj){
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody SalaCDTO objDto, @PathVariable Integer id){
		SalaC obj = service.fromDto(objDto);
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<SalaCDTO>> findAll() {
		List<SalaC> list = service.findAll();
		List<SalaCDTO> listDto = list.stream().map(obj -> new SalaCDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}
	
	@RequestMapping(value="/page", method=RequestMethod.GET)
	public ResponseEntity<Page<SalaCDTO>> findPag(
			@RequestParam(value="descricao", defaultValue="") String nome,
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue="descricao") String orderBy,
			@RequestParam(value="direction", defaultValue="ASC") String direction) {
		Page<SalaC> list = service.findPage(page, linesPerPage, orderBy, direction);
		Page<SalaCDTO> listDto = list.map(obj -> new SalaCDTO(obj));
		return ResponseEntity.ok().body(listDto);
	}

}
