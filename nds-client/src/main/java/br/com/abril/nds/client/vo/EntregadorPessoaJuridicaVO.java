package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.Entregador;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProcuracaoEntregador;

public class EntregadorPessoaJuridicaVO implements Serializable {

	private static final long serialVersionUID = 1500282702636525652L;
	
	private String inicioAtividadeFormatada;
	
	private Entregador entregador;

	private ProcuracaoEntregador procuracaoEntregador;
	
	private PessoaJuridica pessoaJuridica;

	/**
	 * @return the inicioAtividadeFormatada
	 */
	public String getInicioAtividadeFormatada() {
		return inicioAtividadeFormatada;
	}

	/**
	 * @param inicioAtividadeFormatada the inicioAtividadeFormatada to set
	 */
	public void setInicioAtividadeFormatada(String inicioAtividadeFormatada) {
		this.inicioAtividadeFormatada = inicioAtividadeFormatada;
	}

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

	
}
