package br.com.abril.nds.integracao.engine.test;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class TestFixedModelHeader {
	private Integer number;
	private String nome;
	
	@Field(offset=2, length=4)
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	
	@Field(offset=6, length=8)
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}
