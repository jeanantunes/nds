package br.com.abril.nfe.model.interfaces;


public interface Pessoa {

	Long getId();
	
	String getNome();
	
	Telefone getTelefone();
	
	Endereco getEndereco();
}
