package br.com.abril.nds.model.envio.nota;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.Telefone;
@Embeddable
public class IdentificacaoEmitente implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8280374566083682182L;

	@ManyToOne(optional = true)
	@JoinColumn(name = "PESSOA_EMITENTE_ID_REFERENCIADA")
	private Pessoa pessoaEmitenteReferencia;
	
	/**
	 * CNPJ CPF
	 */
	@Column(name="DOCUMENTO_EMITENTE", nullable=false, length=14)

	private String documento;
	

	@Column(name="NOME_EMITENTE", nullable=false, length=60)
	private String nome;
	
	
	
	/**
	 * IE
	 */
	@Column(name="IE_EMITENTE", nullable=false, length=14)
	private String inscricaoEstual;
	
	
	@OneToOne(optional=false)
	@JoinColumn(name="ENDERECO_ID_EMITENTE")
	private Endereco endereco;
	
	@OneToOne(optional=true)
	@JoinColumn(name="TELEFONE_ID_EMITENTE")
	private Telefone telefone;

	/**
	 * @return the pessoaEmitenteReferencia
	 */
	public Pessoa getPessoaEmitenteReferencia() {
		return pessoaEmitenteReferencia;
	}

	/**
	 * @param pessoaEmitenteReferencia the pessoaEmitenteReferencia to set
	 */
	public void setPessoaEmitenteReferencia(Pessoa pessoaEmitenteReferencia) {
		this.pessoaEmitenteReferencia = pessoaEmitenteReferencia;
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
}
