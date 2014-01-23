package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFicalEndereco;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalPessoa;
import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;
import br.com.abril.nds.util.export.fiscal.nota.NFEExportType;

@Embeddable
@XmlType(name="NotaFiscalIdentificacaoDestinatario")
@XmlAccessorType(XmlAccessType.FIELD)
public class IdentificacaoDestinatario implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3558149602330018787L;
	
	@ManyToOne(optional = true, fetch=FetchType.LAZY)
	@JoinColumn(name = "PESSOA_DESTINATARIO_ID_REFERENCIA")
	private NotaFiscalPessoa pessoaDestinatarioReferencia;
	
	
	/**
	 * CNPJ CPF
	 */
	@Embedded
	@XmlElements(value = {
        @XmlElement(name="CPF", type=CPFDestinatario.class),
        @XmlElement(name="CNPJ", type=CNPJDestinatario.class)
    })
	private DocumentoDestinatario documento;
	
	/**
	 * xNome
	 */	
	@Column(name="NOME_DESTINATARIO", nullable=false, length=60)
	@NFEExport(secao=TipoSecao.E, posicao=0, tamanho=60)
	@XmlElement(name="xNome")
	private String nome;
	
	/**
	 * xFant
	 */
	@Column(name="NOME_FANTASIA_DESTINATARIO", nullable=true, length=60)
	@XmlElement(name="xFant")
	private String nomeFantasia;
	
	@OneToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="ENDERECO_ID_DESTINATARIO")
	@NFEExportType
	@XmlElement(name="enderDest")
	private NotaFicalEndereco endereco;
	
	/**
	 * IE
	 */
	@Column(name="IE_DESTINATARIO", nullable=true, length=14)
	@NFEExport(secao=TipoSecao.E, posicao=1, tamanho=14)
	@XmlElement(name="IE")
	private String inscricaoEstadual;
	
	/**
	 * ISUF
	 */
	@Column(name="ISUF_DESTINATARIO", nullable=true, length=9)
	@NFEExport(secao=TipoSecao.E, posicao=2, tamanho=9)
	private String inscricaoSuframa;
	
	/**
	 * EMAIL
	 */
	@Column(name="EMAIL_DESTINATARIO", nullable=true, length=60)
	private String email;
	
	@OneToOne(optional=true, fetch=FetchType.LAZY)
	@JoinColumn(name="TELEFONE_ID_DESTINATARIO")
	@NFEExportType
	private Telefone telefone;
	
	
	
	/**
	 * Construtor padrão.
	 */
	public IdentificacaoDestinatario() {
	}

	
	public NotaFiscalPessoa getPessoaDestinatarioReferencia() {
		return pessoaDestinatarioReferencia;
	}

	
	public void setPessoaDestinatarioReferencia(NotaFiscalPessoa pessoaDestinatarioReferencia) {
		this.pessoaDestinatarioReferencia = pessoaDestinatarioReferencia;
	}


	/**
	 * @return the documento
	 */
	public DocumentoDestinatario getDocumento() {
		return documento;
	}


	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(DocumentoDestinatario documento) {
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
	 * @return the inscricaoSuframa
	 */
	public String getInscricaoSuframa() {
		return inscricaoSuframa;
	}


	/**
	 * @param inscricaoSuframa the inscricaoSuframa to set
	 */
	public void setInscricaoSuframa(String inscricaoSuframa) {
		this.inscricaoSuframa = inscricaoSuframa;
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
	 * @return the endereco
	 */
	public NotaFicalEndereco getEndereco() {
		return endereco;
	}


	/**
	 * @param endereco the endereco to set
	 */
	public void setEndereco(NotaFicalEndereco endereco) {
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

}
