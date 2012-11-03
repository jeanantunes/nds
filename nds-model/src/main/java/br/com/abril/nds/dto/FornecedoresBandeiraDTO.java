package br.com.abril.nds.dto;

import java.io.Serializable;

public class FornecedoresBandeiraDTO implements Serializable {

	private static final long serialVersionUID = -4616938126984688275L;

	/**
	 * Nome do Fornecedor
	 */
	private String nome;
		
	/**
	 * Codigo utilizado apenas para obter "codigoPracaNoProdin", não é apresentado na bandeira
	 */
	private Integer codigoInterface;
	
	/**
	 * Código retirado de parâmetros do Distribuidor, 
	 * caso Fornecedor seja Dinap é um, caso seja FC é outro.
	 */
	private String codigoPracaNoProdin;
	
	/**
	 * Número da semana
	 */
	private Integer semana;
	
	/**
	 * Nome da Cidade (Distribuidor)
	 */
	private String praca;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCodigoPracaNoProdin() {
		return codigoPracaNoProdin;
	}

	public void setCodigoPracaNoProdin(String codigoPracaNoProdin) {
		this.codigoPracaNoProdin = codigoPracaNoProdin;
	}

	public Integer getSemana() {
		return semana;
	}

	public void setSemana(Integer semana) {
		this.semana = semana;
	}

	public String getPraca() {
		return praca;
	}

	public void setPraca(String praca) {
		this.praca = praca;
	}

	public Integer getCodigoInterface() {
		return codigoInterface;
	}

	public void setCodigoInterface(Integer codigoInterface) {
		this.codigoInterface = codigoInterface;
	}
	
	
}
