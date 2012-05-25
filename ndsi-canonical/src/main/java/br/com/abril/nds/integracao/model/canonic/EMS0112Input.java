package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.math.BigDecimal;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatBoolean;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatDecimal;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0112Input extends IntegracaoDocument implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long codigoEditor;
	private String nomeEditor;
	private String logradouroEditor;
	private Integer numeroEditor;
	private String complementoEditor;
	private String cidadeEditor;
	private String ufEditor;
	private String cepEditor;
	private String logradouroEntrega;
	private Integer numeroEntrega;
	private String complementoEntrega;
	private String cidadeEntrega;
	private String ufEntregar;
	private String cepEntrega;
	private String dddContato;
	private String telefoneContato;
	private String dddEditor;
	private String telefoneEditor;
	private String dddFax;
	private String telefoneFax;
	private String inscricaoMunicipal;
	private String nomeContato;
	private String status;
	private String cnpj;
	private String inscricaoEstadual;
	private String bairroEditor;
	private String bairroEntrega;
	
	
	public Long getCodigoEditor() {
		return codigoEditor;
	}
	public void setCodigoEditor(Long codigoEditor) {
		this.codigoEditor = codigoEditor;
	}
	
	
	public String getNomeEditor() {
		return nomeEditor;
	}
	public void setNomeEditor(String nomeEditor) {
		this.nomeEditor = nomeEditor;
	}
	
	
	public String getLogradouroEditor() {
		return logradouroEditor;
	}
	public void setLogradouroEditor(String logradouroEditor) {
		this.logradouroEditor = logradouroEditor;
	}
	
	
	public Integer getNumeroEditor() {
		return numeroEditor;
	}
	public void setNumeroEditor(Integer numeroEditor) {
		this.numeroEditor = numeroEditor;
	}
	
	
	public String getComplementoEditor() {
		return complementoEditor;
	}
	public void setComplementoEditor(String complementoEditor) {
		this.complementoEditor = complementoEditor;
	}
	
	
	public String getCidadeEditor() {
		return cidadeEditor;
	}
	public void setCidadeEditor(String cidadeEditor) {
		this.cidadeEditor = cidadeEditor;
	}
	
	
	public String getUfEditor() {
		return ufEditor;
	}
	public void setUfEditor(String ufEditor) {
		this.ufEditor = ufEditor;
	}
	
	
	public String getCepEditor() {
		return cepEditor;
	}
	public void setCepEditor(String cepEditor) {
		this.cepEditor = cepEditor;
	}
	
	
	public String getLogradouroEntrega() {
		return logradouroEntrega;
	}
	public void setLogradouroEntrega(String logradouroEntrega) {
		this.logradouroEntrega = logradouroEntrega;
	}
	
	
	public Integer getNumeroEntrega() {
		return numeroEntrega;
	}
	public void setNumeroEntrega(Integer numeroEntrega) {
		this.numeroEntrega = numeroEntrega;
	}
	
	
	public String getComplementoEntrega() {
		return complementoEntrega;
	}
	public void setComplementoEntrega(String complementoEntrega) {
		this.complementoEntrega = complementoEntrega;
	}
	
	
	public String getCidadeEntrega() {
		return cidadeEntrega;
	}
	public void setCidadeEntrega(String cidadeEntrega) {
		this.cidadeEntrega = cidadeEntrega;
	}
	
	
	public String getUfEntregar() {
		return ufEntregar;
	}
	public void setUfEntregar(String ufEntregar) {
		this.ufEntregar = ufEntregar;
	}
	
	
	public String getCepEntrega() {
		return cepEntrega;
	}
	public void setCepEntrega(String cepEntrega) {
		this.cepEntrega = cepEntrega;
	}
	
	
	public String getDddContato() {
		return dddContato;
	}
	public void setDddContato(String dddContato) {
		this.dddContato = dddContato;
	}
	
	
	public String getTelefoneContato() {
		return telefoneContato;
	}
	public void setTelefoneContato(String telefoneContato) {
		this.telefoneContato = telefoneContato;
	}
	
	
	public String getDddEditor() {
		return dddEditor;
	}
	public void setDddEditor(String dddEditor) {
		this.dddEditor = dddEditor;
	}
	
	
	public String getTelefoneEditor() {
		return telefoneEditor;
	}
	public void setTelefoneEditor(String telefoneEditor) {
		this.telefoneEditor = telefoneEditor;
	}
	
	
	public String getDddFax() {
		return dddFax;
	}
	public void setDddFax(String dddFax) {
		this.dddFax = dddFax;
	}
	
	
	public String getTelefoneFax() {
		return telefoneFax;
	}
	public void setTelefoneFax(String telefoneFax) {
		this.telefoneFax = telefoneFax;
	}
	
	
	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}
	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}
	
	
	public String getNomeContato() {
		return nomeContato;
	}
	public void setNomeContato(String nomeContato) {
		this.nomeContato = nomeContato;
	}
	
	
	public String getStatus() {
		return status;
	}	
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	
	
	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}
	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}
	
	
	public String getBairroEditor() {
		return bairroEditor;
	}
	public void setBairroEditor(String bairroEditor) {
		this.bairroEditor = bairroEditor;
	}
	
	
	public String getBairroEntrega() {
		return bairroEntrega;
	}
	public void setBairroEntrega(String bairroEntrega) {
		this.bairroEntrega = bairroEntrega;
	}
	
	
	
	
}