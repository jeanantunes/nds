package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.Entregador;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.ProcuracaoEntregador;

public class EntregadorPessoaFisicaVO implements Serializable {

	private static final long serialVersionUID = -8851435607582630245L;

	private Entregador entregador;
	
	private ProcuracaoEntregador procuracaoEntregador;
	
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
	 * @return the procuracaoEntregador
	 */
	public ProcuracaoEntregador getProcuracaoEntregador() {
		return procuracaoEntregador;
	}

	/**
	 * @param procuracaoEntregador the procuracaoEntregador to set
	 */
	public void setProcuracaoEntregador(ProcuracaoEntregador procuracaoEntregador) {
		this.procuracaoEntregador = procuracaoEntregador;
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
}
