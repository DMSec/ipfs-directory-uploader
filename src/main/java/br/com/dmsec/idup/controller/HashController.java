package br.com.dmsec.idup.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import br.com.dmsec.idup.models.ListHashs;
import br.com.dmsec.idup.repository.ListHashRepository;

@RestController
public class HashController {

	@Autowired 
	ListHashRepository listHashRepository;
	
	
	@RequestMapping(method=RequestMethod.POST, path="/criarlista")
	public ListHashs criarListaRest(@RequestBody ListHashs listHashs) {
		listHashs.setId(UUID.randomUUID());
		return listHashRepository.save(listHashs);
	}
	
	
	
	@RequestMapping(method=RequestMethod.GET, path="/listagem")
	public Iterable<?> findAll() {
		return listHashRepository.findAll();
	}
	
}
