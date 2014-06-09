package br.com.abril.nds.client.vo;

import java.io.Serializable;

public class ProcessamentoFinanceiroCotaVO implements Serializable {

	private static final long serialVersionUID = 112346987L;
	
	private String numeroCota;
	
	private String nomeCota;
	
	private String valorConsignado;
	
	private String valorAVista;
	
	private String debitos;
	
	private String creditos;
	
	private String saldo;

	/**
	 * @param numeroCota
	 * @param nomeCota
	 * @param valorConsignado
	 * @param valorAVista
	 * @param debitos
	 * @param creditos
	 * @param saldo
	 */
	public ProcessamentoFinanceiroCotaVO(String numeroCota, 
			                             String nomeCota,
			                             String valorConsignado, 
			                             String valorAVista,
			                             String debitos, 
			                             String creditos, 
			                             String saldo) {
		super();
		this.numeroCota = numeroCota;
		this.nomeCota = nomeCota;
		this.valorConsignado = valorConsignado;
		this.valorAVista = valorAVista;
		this.debitos = debitos;
		this.creditos = creditos;
		this.saldo = saldo;
	}

	public String getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(String numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public String getValorConsignado() {
		return valorConsignado;
	}

	public void setValorConsignado(String valorConsignado) {
		this.valorConsignado = valorConsignado;
	}

	public String getValorAVista() {
		return valorAVista;
	}

	public void setValorAVista(String valorAVista) {
		this.valorAVista = valorAVista;
	}

	public String getDebitos() {
		return debitos;
	}

	public void setDebitos(String debitos) {
		this.debitos = debitos;
	}

	public String getCreditos() {
		return creditos;
	}

	public void setCreditos(String creditos) {
		this.creditos = creditos;
	}

	public String getSaldo() {
		return saldo;
	}

	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}
}
