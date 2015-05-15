package br.com.abril.nds.model.envio.nota;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import br.com.abril.nds.model.cadastro.Pessoa;
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
	
	@Column(name="DOCUMENTO_DESTINATARIO", nullable=false, length=14)	
	private String documento;
	
	@Column(name="NOME_DESTINATARIO", nullable=false, length=60)
	private String nome;
	
	@Column(name="IE_DESTINATARIO", nullable=true, length=20)
	private String inscricaoEstadual;
	
	@Column(name = "CODIGO_BOX")
	private Integer codigoBox;
	
	@Column(name = "NOME_BOX")
	private String nomeBox;
	
	@Column(name = "CODIGO_ROTA")
	private String codigoRota;
	
	@Column(name = "DESCRICAO_ROTA")
	private String descricaoRota;
	
	@ManyToOne(optional=false, cascade=CascadeType.ALL)
	@JoinColumn(name="ENDERECO_ID_DESTINATARIO")
	private NotaEnvioEndereco endereco;
	
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
	 * @return the endereco
	 */
	public NotaEnvioEndereco getEndereco() {
		return endereco;
	}

	/**
	 * @param endereco the endereco to set
	 */
	public void setEndereco(NotaEnvioEndereco endereco) {
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