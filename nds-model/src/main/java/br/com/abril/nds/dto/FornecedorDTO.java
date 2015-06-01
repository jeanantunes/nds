package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.CanalDistribuicao;

/**
 * DTO utilizado para transporte de dados referentes a entidade Fornecedor. 
 * 
 * @author Discover Technology
 *
 */
public class FornecedorDTO implements Serializable {

	private static final long serialVersionUID = 6259806300095481171L;
	
	private Long idFornecedor;
	
	private Integer codigoInterface;
	
	private String razaoSocial;
	
	private String cnpj;
	
	private String responsavel;
	
	private String telefone;
	
	private String email;
	
	private String nomeFantasia;
	
	private String inscricaoEstadual;
	
	private Long tipoFornecedor;
	
	private Long idBanco;

	private String inicioAtividade;
	
	private boolean possuiContrato;
	
	private String validadeContrato;

	private Origem origem;

	private String emailNfe;
	
	private String praca;
	
	private CanalDistribuicao canalDistribuicao;
	
	private boolean integraGFS;
	
	private boolean destinacaoEncalhe;
	
	/**
	 * @return the idFornecedor
	 */
	public Long getIdFornecedor() {
		return idFornecedor;
	}

	/**
	 * @param idFornecedor the idFornecedor to set
	 */
	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	/**
	 * @return the codigoInterface
	 */
	public Integer getCodigoInterface() {
		return codigoInterface;
	}

	/**
	 * @param codigoInterface the codigoInterface to set
	 */
	public void setCodigoInterface(Integer codigoInterface) {
		this.codigoInterface = codigoInterface;
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
	 * @return the cnpj
	 */
	public String getCnpj() {
		return cnpj;
	}

	/**
	 * @param cnpj the cnpj to set
	 */
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	/**
	 * @return the responsavel
	 */
	public String getResponsavel() {
		return responsavel;
	}

	/**
	 * @param responsavel the responsavel to set
	 */
	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
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
	 * @return the tipoFornecedor
	 */
	public Long getTipoFornecedor() {
		return tipoFornecedor;
	}

	/**
	 * @param tipoFornecedor the tipoFornecedor to set
	 */
	public void setTipoFornecedor(Long tipoFornecedor) {
		this.tipoFornecedor = tipoFornecedor;
	}

	/**
	 * @return the idBanco
	 */
	public Long getIdBanco() {
		return idBanco;
	}

	/**
	 * @param idBanco the idBanco to set
	 */
	public void setIdBanco(Long idBanco) {
		this.idBanco = idBanco;
	}

	/**
	 * @return the inicioAtividade
	 */
	public String getInicioAtividade() {
		return inicioAtividade;
	}

	/**
	 * @param inicioAtividade the inicioAtividade to set
	 */
	public void setInicioAtividade(String inicioAtividade) {
		this.inicioAtividade = inicioAtividade;
	}

	/**
	 * @return the possuiContrato
	 */
	public boolean isPossuiContrato() {
		return possuiContrato;
	}

	/**
	 * @param possuiContrato the possuiContrato to set
	 */
	public void setPossuiContrato(boolean possuiContrato) {
		this.possuiContrato = possuiContrato;
	}

	/**
	 * @return the validadeContrato
	 */
	public String getValidadeContrato() {
		return validadeContrato;
	}

	/**
	 * @param validadeContrato the validadeContrato to set
	 */
	public void setValidadeContrato(String validadeContrato) {
		this.validadeContrato = validadeContrato;
	}

	/**
	 * @return the origem
	 */
	public Origem getOrigem() {
		return origem;
	}

	/**
	 * @param origem the origem to set
	 */
	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	/**
	 * @return the emailNfe
	 */
	public String getEmailNfe() {
		return emailNfe;
	}

	/**
	 * @param emailNfe the emailNfe to set
	 */
	public void setEmailNfe(String emailNfe) {
		this.emailNfe = emailNfe;
	}

	public String getPraca() {
		return praca;
	}

	public void setPraca(String praca) {
		this.praca = praca;
	}

	public CanalDistribuicao getCanalDistribuicao() {
		return canalDistribuicao;
	}

	public void setCanalDistribuicao(CanalDistribuicao canalDistribuicao) {
		this.canalDistribuicao = canalDistribuicao;
	}

	public boolean isIntegraGFS() {
		return integraGFS;
	}

	public void setIntegraGFS(boolean integraGFS) {
		this.integraGFS = integraGFS;
	}

	public boolean isDestinacaoEncalhe() {
		return destinacaoEncalhe;
	}

	public void setDestinacaoEncalhe(boolean destinacaoEncalhe) {
		this.destinacaoEncalhe = destinacaoEncalhe;
	}
	
}