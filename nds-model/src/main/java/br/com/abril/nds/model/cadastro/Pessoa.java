package br.com.abril.nds.model.cadastro;

/**
 * @author jfonseca
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
public abstract class Pessoa {

	private Long id;
	private String email;
	public List<Endereco> enderecos;
	public List<Telefone> telefones;

	public Pessoa(){

	}

}