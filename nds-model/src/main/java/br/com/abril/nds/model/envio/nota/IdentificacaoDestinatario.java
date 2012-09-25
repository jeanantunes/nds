package br.com.abril.nds.model.envio.nota;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Telefone;
@Embeddable
public class IdentificacaoDestinatario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5209755233911222853L;
	
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "PESSOA_DESTINATARIO_ID_REFERENCIA")
	private Pessoa pessoaDestinatarioReferencia;
	
	@Column(name = "NUMERO_COTA", nullable = false)
	private Integer numeroCota;
	
	
	/**
	 * CNPJ CPF
	 */
	@Column(name="DOCUMENTO_DESTINATARIO", nullable=false, length=14)	
	private String documento;
	
	/**
	 * xNome
	 */	
	@Column(name="NOME_DESTINATARIO", nullable=false, length=60)
	private String nome;
	

	
	/**
	 * IE
	 */
	@Column(name="IE_DESTINATARIO", nullable=true, length=14)
	private String inscricaoEstual;
	
	
	@ManyToOne(optional= true)
	@JoinColumn(name = "BOX_DESTINATARIO_ID_REFERENCIA")
	private Box boxReferencia;
	@Column(name = "CODIGO_BOX")
	private Integer codigoBox;
	@Column(name = "NOME_BOX")
	private String nomeBox;
	
	@ManyToOne(optional= true)
	@JoinColumn(name = "ROTA_DESTINATARIO_ID_REFERENCIA")
	private Rota rotaReferencia;
	
	@Column(name = "CODIGO_ROTA")
	private String codigoRota;
	
	@Column(name = "DESCRICAO_ROTA")
	private String descricaoRota;
	
	
	@OneToOne(optional=false)
	@JoinColumn(name="ENDERECO_ID_DESTINATARIO")
	private Endereco endereco;
	
	@OneToOne(optional=true)
	@JoinColumn(name="TELEFONE_ID_DESTINATARIO")
	private Telefone telefone;

	/**
	 * @return the pessoaDestinatarioReferencia
	 */
	public Pessoa getPessoaDestinatarioReferencia() {
		return pessoaDestinatarioReferencia;
	}

	/**
	 * @param pessoaDestinatarioReferencia the pessoaDestinatarioReferencia to set
	 */
	public void setPessoaDestinatarioReferencia(Pessoa pessoaDestinatarioReferencia) {
		this.pessoaDestinatarioReferencia = pessoaDestinatarioReferencia;
	}

	/**
	 * @return the documento
	 */
	public String getDocumento() {
		return documento;
	}

	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(String documento) {
		this.documento = documento;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the inscricaoEstual
	 */
	public String getInscricaoEstual() {
		return inscricaoEstual;
	}

	/**
	 * @param inscricaoEstual the inscricaoEstual to set
	 */
	public void setInscricaoEstual(String inscricaoEstual) {
		this.inscricaoEstual = inscricaoEstual;
	}

	/**
	 * @return the endereco
	 */
	public Endereco getEndereco() {
		return endereco;
	}

	/**
	 * @param endereco the endereco to set
	 */
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	/**
	 * @return the telefone
	 */
	public Telefone getTelefone() {
		return telefone;
	}

	/**
	 * @param telefone the telefone to set
	 */
	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	/**
	 * @return the boxReferencia
	 */
	public Box getBoxReferencia() {
		return boxReferencia;
	}

	/**
	 * @param boxReferencia the boxReferencia to set
	 */
	public void setBoxReferencia(Box boxReferencia) {
		this.boxReferencia = boxReferencia;
	}

	/**
	 * @return the codigoBox
	 */
	public Integer getCodigoBox() {
		return codigoBox;
	}

	/**
	 * @param codigoBox the codigoBox to set
	 */
	public void setCodigoBox(Integer codigoBox) {
		this.codigoBox = codigoBox;
	}

	/**
	 * @return the nomeBox
	 */
	public String getNomeBox() {
		return nomeBox;
	}

	/**
	 * @param nomeBox the nomeBox to set
	 */
	public void setNomeBox(String nomeBox) {
		this.nomeBox = nomeBox;
	}

	/**
	 * @return the rotaReferencia
	 */
	public Rota getRotaReferencia() {
		return rotaReferencia;
	}

	/**
	 * @param rotaReferencia the rotaReferencia to set
	 */
	public void setRotaReferencia(Rota rotaReferencia) {
		this.rotaReferencia = rotaReferencia;
	}

	/**
	 * @return the codigoRota
	 */
	public String getCodigoRota() {
		return codigoRota;
	}

	/**
	 * @param codigoRota the codigoRota to set
	 */
	public void setCodigoRota(String codigoRota) {
		this.codigoRota = codigoRota;
	}

	/**
	 * @return the descricaoRota
	 */
	public String getDescricaoRota() {
		return descricaoRota;
	}

	/**
	 * @param descricaoRota the descricaoRota to set
	 */
	public void setDescricaoRota(String descricaoRota) {
		this.descricaoRota = descricaoRota;
	}

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
	
	
}
