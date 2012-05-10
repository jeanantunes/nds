package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.cadastro.SituacaoCadastro;

public class CotaDTO implements Serializable {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Atributos utilizados no grid da consulta
	 */
	private Integer numeroCota;
	
	private String nomePessoa;
	
	private String numeroCpfCnpj;
	
	private String contato;
	
	private String telefone;
	
	private String email;
	
	private SituacaoCadastro status; 

	/**
	 * Atributos utilizados no cadastro da cota
	 */
	
	private Date dataInclusao;
	
	private String razaoSocial;
	
	private String nomeFantasia;
	
	private String numeroCnpj;
	
	private String inscricaoEstadual;
	
	private String inscricaoMunicipal;
	
	private String emailNF;
	
	private boolean emiteNFE;
	
	private Date inicioPeriodo;
	
	private Date fimPeriodo;
	
	private Integer historicoPrimeiraCota;

	private Integer historicoSegundaCota;
	
	private Integer historicoterceiraCota;
	
	private BigDecimal historicoPrimeiraPorcentagem;
	
	private BigDecimal historicoSegundaPorcentagem;
	
	private BigDecimal historicoTerceiraPorcentagem;
	
	private String classificacaoSelecionada;
	
	
	/**
	 * @return the numeroCota
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}

	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * @return the nomePessoa
	 */
	public String getNomePessoa() {
		return nomePessoa;
	}

	/**
	 * @param nomePessoa the nomePessoa to set
	 */
	public void setNomePessoa(String nomePessoa) {
		this.nomePessoa = nomePessoa;
	}

	/**
	 * @return the numeroCpfCnpj
	 */
	public String getNumeroCpfCnpj() {
		return numeroCpfCnpj;
	}

	/**
	 * @param numeroCpfCnpj the numeroCpfCnpj to set
	 */
	public void setNumeroCpfCnpj(String numeroCpfCnpj) {
		this.numeroCpfCnpj = numeroCpfCnpj;
	}

	/**
	 * @return the contato
	 */
	public String getContato() {
		return contato;
	}

	/**
	 * @param contato the contato to set
	 */
	public void setContato(String contato) {
		this.contato = contato;
	}

	/**
	 * @return the telefone
	 */
	public String getTelefone() {
		return telefone;
	}

	/**
	 * @param telefone the telefone to set
	 */
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the status
	 */
	public SituacaoCadastro getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(SituacaoCadastro status) {
		this.status = status;
	}

	/**
	 * @return the dataInclusao
	 */
	public Date getDataInclusao() {
		return dataInclusao;
	}

	/**
	 * @param dataInclusao the dataInclusao to set
	 */
	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	/**
	 * @return the razaoSocial
	 */
	public String getRazaoSocial() {
		return razaoSocial;
	}

	/**
	 * @param razaoSocial the razaoSocial to set
	 */
	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	/**
	 * @return the nomeFantasia
	 */
	public String getNomeFantasia() {
		return nomeFantasia;
	}

	/**
	 * @param nomeFantasia the nomeFantasia to set
	 */
	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	/**
	 * @return the numeroCnpj
	 */
	public String getNumeroCnpj() {
		return numeroCnpj;
	}

	/**
	 * @param numeroCnpj the numeroCnpj to set
	 */
	public void setNumeroCnpj(String numeroCnpj) {
		this.numeroCnpj = numeroCnpj;
	}

	/**
	 * @return the inscricaoEstadual
	 */
	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	/**
	 * @param inscricaoEstadual the inscricaoEstadual to set
	 */
	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	/**
	 * @return the inscricaoMunicipal
	 */
	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}

	/**
	 * @param inscricaoMunicipal the inscricaoMunicipal to set
	 */
	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}

	/**
	 * @return the emailNF
	 */
	public String getEmailNF() {
		return emailNF;
	}

	/**
	 * @param emailNF the emailNF to set
	 */
	public void setEmailNF(String emailNF) {
		this.emailNF = emailNF;
	}

	/**
	 * @return the emiteNFE
	 */
	public boolean isEmiteNFE() {
		return emiteNFE;
	}

	/**
	 * @param emiteNFE the emiteNFE to set
	 */
	public void setEmiteNFE(boolean emiteNFE) {
		this.emiteNFE = emiteNFE;
	}

	/**
	 * @return the inicioPeriodo
	 */
	public Date getInicioPeriodo() {
		return inicioPeriodo;
	}

	/**
	 * @param inicioPeriodo the inicioPeriodo to set
	 */
	public void setInicioPeriodo(Date inicioPeriodo) {
		this.inicioPeriodo = inicioPeriodo;
	}

	/**
	 * @return the fimPeriodo
	 */
	public Date getFimPeriodo() {
		return fimPeriodo;
	}

	/**
	 * @param fimPeriodo the fimPeriodo to set
	 */
	public void setFimPeriodo(Date fimPeriodo) {
		this.fimPeriodo = fimPeriodo;
	}

	/**
	 * @return the historicoPrimeiraCota
	 */
	public Integer getHistoricoPrimeiraCota() {
		return historicoPrimeiraCota;
	}

	/**
	 * @param historicoPrimeiraCota the historicoPrimeiraCota to set
	 */
	public void setHistoricoPrimeiraCota(Integer historicoPrimeiraCota) {
		this.historicoPrimeiraCota = historicoPrimeiraCota;
	}

	/**
	 * @return the historicoSegundaCota
	 */
	public Integer getHistoricoSegundaCota() {
		return historicoSegundaCota;
	}

	/**
	 * @param historicoSegundaCota the historicoSegundaCota to set
	 */
	public void setHistoricoSegundaCota(Integer historicoSegundaCota) {
		this.historicoSegundaCota = historicoSegundaCota;
	}

	/**
	 * @return the historicoterceiraCota
	 */
	public Integer getHistoricoterceiraCota() {
		return historicoterceiraCota;
	}

	/**
	 * @param historicoterceiraCota the historicoterceiraCota to set
	 */
	public void setHistoricoterceiraCota(Integer historicoterceiraCota) {
		this.historicoterceiraCota = historicoterceiraCota;
	}

	/**
	 * @return the historicoPrimeiraPorcentagem
	 */
	public BigDecimal getHistoricoPrimeiraPorcentagem() {
		return historicoPrimeiraPorcentagem;
	}

	/**
	 * @param historicoPrimeiraPorcentagem the historicoPrimeiraPorcentagem to set
	 */
	public void setHistoricoPrimeiraPorcentagem(
			BigDecimal historicoPrimeiraPorcentagem) {
		this.historicoPrimeiraPorcentagem = historicoPrimeiraPorcentagem;
	}

	/**
	 * @return the historicoSegundaPorcentagem
	 */
	public BigDecimal getHistoricoSegundaPorcentagem() {
		return historicoSegundaPorcentagem;
	}

	/**
	 * @param historicoSegundaPorcentagem the historicoSegundaPorcentagem to set
	 */
	public void setHistoricoSegundaPorcentagem(
			BigDecimal historicoSegundaPorcentagem) {
		this.historicoSegundaPorcentagem = historicoSegundaPorcentagem;
	}

	/**
	 * @return the historicoTerceiraPorcentagem
	 */
	public BigDecimal getHistoricoTerceiraPorcentagem() {
		return historicoTerceiraPorcentagem;
	}

	/**
	 * @param historicoTerceiraPorcentagem the historicoTerceiraPorcentagem to set
	 */
	public void setHistoricoTerceiraPorcentagem(
			BigDecimal historicoTerceiraPorcentagem) {
		this.historicoTerceiraPorcentagem = historicoTerceiraPorcentagem;
	}

	/**
	 * @return the classificacaoSelecionada
	 */
	public String getClassificacaoSelecionada() {
		return classificacaoSelecionada;
	}

	/**
	 * @param classificacaoSelecionada the classificacaoSelecionada to set
	 */
	public void setClassificacaoSelecionada(String classificacaoSelecionada) {
		this.classificacaoSelecionada = classificacaoSelecionada;
	}

	
	
	
}
