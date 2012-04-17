package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.Entregador;
import br.com.abril.nds.model.cadastro.PessoaFisica;

public class EntregadorPessoaFisicaVO implements Serializable {

	private static final long serialVersionUID = -8851435607582630245L;

	private Entregador entregador;
	
	private PessoaFisica pessoaFisica;

	/**
	 * @return the entregador
	 */
	public Entregador getEntregador() {
		return entregador;
	}

	/**
	 * @param entregador the entregador to set
	 */
	public void setEntregador(Entregador entregador) {
		this.entregador = entregador;
	}

	/**
	 * @return the pessoaFisica
	 */
	public PessoaFisica getPessoaFisica() {
		return pessoaFisica;
	}

	/**
	 * @param pessoaFisica the pessoaFisica to set
	 */
	public void setPessoaFisica(PessoaFisica pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}
	
//	private Long id;
//	
//	private String codigo;
//	
//	private String email;
//	
//	private String inicioAtividade;
//	
//	private boolean comissionado;
//	
//	private String percentualComissao;
//
//	private boolean procuracao;
//
//	private String nome;
//	
//	private String cpf;
//	
//	private String rg;
//	
//	private String orgaoEmissor;
//	
//	private String ufOrgaoEmissor;
//	
//	private Date dataNascimento;
//	
//	private EstadoCivil estadoCivil;
//	
//	private Sexo sexo;
//	
//	private String nacionalidade;
//	
//	private String natural;
//	
//	private String apelido;
	
}
