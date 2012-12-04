package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEConditions;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;
import br.com.abril.nds.util.export.fiscal.nota.NFEExportType;
import br.com.abril.nds.util.export.fiscal.nota.NFEWhen;
import br.com.abril.nds.util.export.fiscal.nota.NFEWhens;

@Embeddable
public class IdentificacaoDestinatario implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3558149602330018787L;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "PESSOA_DESTINATARIO_ID_REFERENCIA")
	private Pessoa pessoaDestinatarioReferencia;
	
	
	/**
	 * CNPJ CPF
	 */
	@Column(name="DOCUMENTO_DESTINATARIO", nullable=false, length=14)
	@NFEWhens(value = {
			@NFEWhen(condition = NFEConditions.CPF, export = @NFEExport(secao = TipoSecao.E03, posicao = 0)),
			@NFEWhen(condition = NFEConditions.CNPJ, export = @NFEExport(secao = TipoSecao.E02, posicao = 0))
	})
	private String documento;
	
	/**
	 * xNome
	 */	
	@Column(name="NOME_DESTINATARIO", nullable=false, length=60)
	@NFEExport(secao=TipoSecao.E, posicao=0, tamanho=60)
	private String nome;
	
	/**
	 * xFant
	 */
	@Column(name="NOME_FANTASIA_DESTINATARIO", nullable=true, length=60)
	private String nomeFantasia;
	
	/**
	 * IE
	 */
	@Column(name="IE_DESTINATARIO", nullable=true, length=14)
	@NFEExport(secao=TipoSecao.E, posicao=1, tamanho=14)
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
	
	@OneToOne(optional=false)
	@JoinColumn(name="ENDERECO_ID_DESTINATARIO")
	@NFEExportType
	private Endereco endereco;
	
	@OneToOne(optional=true)
	@JoinColumn(name="TELEFONE_ID_DESTINATARIO")
	@NFEExportType
	private Telefone telefone;
	
	
	
	/**
	 * Construtor padrão.
	 */
	public IdentificacaoDestinatario() {
	}

	
	public Pessoa getPessoaDestinatarioReferencia() {
		return pessoaDestinatarioReferencia;
	}

	
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

}
