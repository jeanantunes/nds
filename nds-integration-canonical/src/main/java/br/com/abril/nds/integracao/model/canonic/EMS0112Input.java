package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatBoolean;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0112Input extends IntegracaoDocument implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codigoDistribuidor;
	private String tipoOperacao;
	private Long codigoEditor;
	private String nomeEditor;
	
	private String tipoLogradouroEditor;
	private String logradouroEditor;
	private String numeroEditor;
	private String complementoEditor;
	private String cidadeEditor;
	private String ufEditor;
	private String cepEditor;
	
	private String tipoLogradouroEntrega;
	private String logradouroEntrega;
	private String numeroEntrega;
	private String complementoEntrega;
	private String cidadeEntrega;
	private String ufEntrega;
	private String cepEntrega;
	
	
	private String dddContato;
	private String telefoneContato;
	
	
	private String dddEditor;
	private String telefoneEditor;
	

	private String dddFax;
	private String telefoneFax;
	
	
	private String inscricaoMunicipal;
	private String tipoPessoa;
	private String nomeContato;
	private boolean status;
	private String cpf;//IGNORAR
	private String rg; //IGNORAR
	private String orgaoEmissor;//IGNORAR
	private String ufOrgaoEmissor;//IGNORAR
	private String cnpj;
	private String inscricaoEstadual;
	private String bairroEditor;
	private String bairroEntrega;
	
	@Field(offset=1, length=7)
	public Integer getCodigoDistribuidor() {
		return codigoDistribuidor;
	}
	public void setCodigoDistribuidor(Integer codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}
	
	@Field(offset=26, length=1)
	public String getTipoOperacao() {
		return tipoOperacao;
	}
	public void setTipoOperacao(String tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}
	
	@Field(offset=28, length=7)	
	public Long getCodigoEditor() {
		return codigoEditor;
	}
	public void setCodigoEditor(Long codigoEditor) {
		this.codigoEditor = codigoEditor;
	}
	
	@Field(offset=35, length=35)	
	public String getNomeEditor() {
		return nomeEditor;
	}
	public void setNomeEditor(String nomeEditor) {
		this.nomeEditor = nomeEditor;
	}
	
	@Field(offset=73, length=5)	
	public String getTipoLogradouroEditor() {
		return tipoLogradouroEditor;
	}
	public void setTipoLogradouroEditor(String tipoLogradouroEditor) {
		this.tipoLogradouroEditor = tipoLogradouroEditor;
	}
	
	@Field(offset=78, length=30)	
	public String getLogradouroEditor() {
		return logradouroEditor;
	}
	public void setLogradouroEditor(String logradouroEditor) {
		this.logradouroEditor = logradouroEditor;
	}
	
	@Field(offset=108, length=5)	
	public String getNumeroEditor() {
		return numeroEditor;
	}
	public void setNumeroEditor(String numeroEditor) {
		this.numeroEditor = numeroEditor;
	}
	
	@Field(offset=113, length=10)	
	public String getComplementoEditor() {
		return complementoEditor;
	}
	public void setComplementoEditor(String complementoEditor) {
		this.complementoEditor = complementoEditor;
	}
	
	@Field(offset=133, length=30)	
	public String getCidadeEditor() {
		return cidadeEditor;
	}
	public void setCidadeEditor(String cidadeEditor) {
		this.cidadeEditor = cidadeEditor;
	}
	
	@Field(offset=163, length=2)	
	public String getUfEditor() {
		return ufEditor;
	}
	public void setUfEditor(String ufEditor) {
		this.ufEditor = ufEditor;
	}
	
	@Field(offset=165, length=8)	
	public String getCepEditor() {
		return cepEditor;
	}
	public void setCepEditor(String cepEditor) {
		this.cepEditor = cepEditor;
	}
	
	@Field(offset=173, length=5)	
	public String getTipoLogradouroEntrega() {
		return tipoLogradouroEntrega;
	}
	public void setTipoLogradouroEntrega(String tipoLogradouroEntrega) {
		this.tipoLogradouroEntrega = tipoLogradouroEntrega;
	}
	
	@Field(offset=178, length=20)	
	public String getLogradouroEntrega() {
		return logradouroEntrega;
	}
	public void setLogradouroEntrega(String logradouroEntrega) {
		this.logradouroEntrega = logradouroEntrega;
	}
	
	@Field(offset=208, length=5)	
	public String getNumeroEntrega() {
		return numeroEntrega;
	}
	public void setNumeroEntrega(String numeroEntrega) {
		this.numeroEntrega = numeroEntrega;
	}
	
	@Field(offset=213, length=20)	
	public String getComplementoEntrega() {
		return complementoEntrega;
	}
	public void setComplementoEntrega(String complementoEntrega) {
		this.complementoEntrega = complementoEntrega;
	}
	
	@Field(offset=233, length=30)	
	public String getCidadeEntrega() {
		return cidadeEntrega;
	}
	public void setCidadeEntrega(String cidadeEntrega) {
		this.cidadeEntrega = cidadeEntrega;
	}
	
	@Field(offset=263, length=2)	
	public String getUfEntrega() {
		return ufEntrega;
	}
	public void setUfEntrega(String ufEntrega) {
		this.ufEntrega = ufEntrega;
	}
	
	@Field(offset=265, length=8)	
	public String getCepEntrega() {
		return cepEntrega;
	}
	public void setCepEntrega(String cepEntrega) {
		this.cepEntrega = cepEntrega;
	}
	
	@Field(offset=273, length=4)	
	public String getDddContato() {
		return dddContato;
	}
	public void setDddContato(String dddContato) {
		this.dddContato = dddContato;
	}
	
	@Field(offset=277, length=8)	
	public String getTelefoneContato() {
		return telefoneContato;
	}
	public void setTelefoneContato(String telefoneContato) {
		this.telefoneContato = telefoneContato;
	}
	
	@Field(offset=285, length=4)	
	public String getDddEditor() {
		return dddEditor;
	}
	public void setDddEditor(String dddEditor) {
		this.dddEditor = dddEditor;
	}
	
	@Field(offset=289, length=8)	
	public String getTelefoneEditor() {
		return telefoneEditor;
	}
	public void setTelefoneEditor(String telefoneEditor) {
		this.telefoneEditor = telefoneEditor;
	}
	
	@Field(offset=297, length=4)	
	public String getDddFax() {
		return dddFax;
	}
	public void setDddFax(String dddFax) {
		this.dddFax = dddFax;
	}
	
	@Field(offset=301, length=8)	
	public String getTelefoneFax() {
		return telefoneFax;
	}
	public void setTelefoneFax(String telefoneFax) {
		this.telefoneFax = telefoneFax;
	}
	
	@Field(offset=315, length=12)	
	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}
	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}
	
	@Field(offset=327, length=1)	
	public String getTipoPessoa() {
		return tipoPessoa;
	}
	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}
	
	@Field(offset=328, length=30)	
	public String getNomeContato() {
		return nomeContato;
	}
	public void setNomeContato(String nomeContato) {
		this.nomeContato = nomeContato;
	}
	
	@Field(offset=361, length=1)
	@FixedFormatBoolean(falseValue="N", trueValue="S")
	public boolean getStatus() {
		return status;
	}	
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	@Field(offset=367, length=12)	
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	@Field(offset=379, length=15)	
	public String getRg() {
		return rg;
	}
	public void setRg(String rg) {
		this.rg = rg;
	}
	
	@Field(offset=394, length=10)	
	public String getOrgaoEmissor() {
		return orgaoEmissor;
	}
	public void setOrgaoEmissor(String orgaoEmissor) {
		this.orgaoEmissor = orgaoEmissor;
	}
	
	@Field(offset=404, length=2)	
	public String getUfOrgaoEmissor() {
		return ufOrgaoEmissor;
	}
	public void setUfOrgaoEmissor(String ufOrgaoEmissor) {
		this.ufOrgaoEmissor = ufOrgaoEmissor;
	}
	
	@Field(offset=406, length=14)	
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	
	@Field(offset=420, length=20)	
	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}
	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}
	
	@Field(offset=440, length=5)	
	public String getBairroEditor() {
		return bairroEditor;
	}
	public void setBairroEditor(String bairroEditor) {
		this.bairroEditor = bairroEditor;
	}
	
	@Field(offset=445, length=5)	
	public String getBairroEntrega() {
		return bairroEntrega;
	}
	public void setBairroEntrega(String bairroEntrega) {
		this.bairroEntrega = bairroEntrega;
	}
	
	
	
	
}