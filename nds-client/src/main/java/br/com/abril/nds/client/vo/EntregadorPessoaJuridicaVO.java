package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.Entregador;
import br.com.abril.nds.model.cadastro.PessoaJuridica;

public class EntregadorPessoaJuridicaVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1500282702636525652L;

	private Entregador entregador;
	
	private PessoaJuridica pessoaJuridica;

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
	 * @return the pessoaJuridica
	 */
	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	/**
	 * @param pessoaJuridica the pessoaJuridica to set
	 */
	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}
	
//	private String codigoBox;
//
//	private Pessoa pessoa;
//	
//	private List<PDV> pdvs = new ArrayList<PDV>();
//
//	private SituacaoCadastro situacaoCadastro;
//	
//	private BigDecimal fatorDesconto;
//	
//	private Set<EnderecoCota> enderecos = new HashSet<EnderecoCota>();
//	
//	private ContratoCota contratoCota;
//
//	private String numeroPermissao;
//	
//	private String procurador;
//	
//	private EstadoCivil estadoCivil;
//
//	private String endereco;
//	
//	private String rg;
//	
//	private String profissao;
//	
//	private String nacionalidade;
//	
//	private ProcuracaoEntregador procuracaoEntregador;

	
}
