package br.com.dmsec.idup.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import br.com.dmsec.idup.models.ListHashs;

public interface ListHashRepository extends CrudRepository<ListHashs, UUID>{

}
