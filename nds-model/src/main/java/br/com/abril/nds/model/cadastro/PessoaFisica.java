package br.com.abril.nds.model.cadastro;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
public class Fisica extends Pessoa {

	private String nome;
	private String cpf;
	private String rg;
	private String orgaoEmissor;
	private Date dataNascimento;
	public EstadoCivil estadoCivil;
	public Sexo sexo;

	public Fisica(){

	}

}