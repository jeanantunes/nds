package br.com.abril.nds.dto;

import java.io.Serializable;
import java.sql.Date;

public class NotaFiscalDTO implements Serializable {
	
	private static final long serialVersionUID = -3086270158463917395L;
	
	private Long id;
	
	private	String ambiente;
	
	private	String protocolo;
	
	private	String versao;
	
	protected Long numero;
	
	protected Long serie;
	
	protected String chaveAcesso;

	protected String modelo;
	
	protected String cnpj;
	
	protected Date dataEmissao;
	
	protected Long codigoUF;	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public String getVersao() {
		return versao;
	}

	public void setVersao(String versao) {
		this.versao = versao;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public Long getSerie() {
		return serie;
	}

	public void setSerie(Long serie) {
		this.serie = serie;
	}

	public String getChaveAcesso() {
		return chaveAcesso;
	}

	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Long getCodigoUF() {
		return codigoUF;
	}

	public void setCodigoUF(Long codigoUF) {
		this.codigoUF = codigoUF;
	}
}
