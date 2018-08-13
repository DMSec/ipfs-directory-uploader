package br.com.dmsec.idup.models;

import java.time.LocalTime;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;


@Table
public class ListHashs {
	
		
	@PrimaryKey
	private String hash;
	
	private String name;
	
	private Integer size;
	
	private String largeSize;
	
	private Integer type;
		
	@NotNull
	private LocalTime horario;

	
	
	
	public LocalTime getHorario() {
		return horario;
	}

	public void setHorario(LocalTime horario) {
		this.horario = horario;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getLargeSize() {
		return largeSize;
	}

	public void setLargeSize(String largeSize) {
		this.largeSize = largeSize;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	
	
}
