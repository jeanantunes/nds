package br.com.abril.nds.ftfutil.test;

import br.com.abril.nds.ftfutil.FTFBaseDTO;
import br.com.abril.nds.ftfutil.FTFfield;


public class PessoaDTO extends FTFBaseDTO {

	@FTFfield(tamanho=11, tipo="numeric", ordem=1)
	private String rg;
	
	@FTFfield(tamanho=10, tipo="char", ordem=2)
	private String nome;
	
	@FTFfield(tamanho=11, tipo="numeric", ordem=3)
	private String cpf;
	
	@FTFfield(tamanho=50, tipo="char", ordem=4)
	private String email;

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
