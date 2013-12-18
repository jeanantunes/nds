package br.com.abril.nfe.model;

public interface Pessoa {

	Long getId();
	
	String getNome();
	
	Telefone getTelefone();
	
	Endereco getEndereco();
}
