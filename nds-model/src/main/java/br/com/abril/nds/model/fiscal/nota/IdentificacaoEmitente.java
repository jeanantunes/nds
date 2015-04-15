package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Type;

import br.com.abril.nds.integracao.persistence.PersistentEnum;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalEndereco;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalPessoa;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalTelefone;
import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;
import br.com.abril.nds.util.export.fiscal.nota.NFEExportType;

@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="emit")
public class IdentificacaoEmitente implements Serializable {
	
	
	public enum RegimeTributario implements NotaFiscalEnum, PersistentEnum {
	
		/**
		 * 1 – Simples Nacional
		 */
		SIMPLES_NACIONAL(1), 
		/**
		 * 2 – Simples Nacional – excesso de sublimite de receita bruta
		 */
		SIMPLES_NACIONAL_EXECESSO(2),
		/**
		 * 3 – Regime Normal. (v2.0)
		 */
		REGIME_NORMAL(3);
		
		private Integer regimeTributario;
		
		RegimeTributario(Integer regimeTributario) {
			this.regimeTributario = regimeTributario;
		}

		@Override
		public Integer getIntValue() {
			return regimeTributario.intValue();
		}

		@Override
		public int getId() {
			return regimeTributario.intValue();
		}
		
	}

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4715921368300274189L;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "PESSOA_EMITENTE_ID_REFERENCIADA")
	private NotaFiscalPessoa pessoaEmitenteReferencia;
	
	/**
	 * CNPJ CPF
	 */
	@Embedded
	@Column(name="DOCUMENTO_EMITENTE", nullable=false, length=14)
	@XmlElements(value = {
        @XmlElement(name="CPF", type=CPFEmitente.class),
        @XmlElement(name="CNPJ", type=CNPJEmitente.class)
    })
	private DocumentoEmitente documento;
	
	/**
	 * xNome
	 */	
	@Column(name="NOME_EMITENTE", nullable=false, length=60)
	@NFEExport(secao=TipoSecao.C, posicao = 0, tamanho=60)
	@XmlElement(name="xNome")
	private String nome;
	
	/**
	 * xFant
	 */
	@Column(name="NOME_FANTASIA_EMITENTE", nullable=true, length=60)
	@NFEExport(secao=TipoSecao.C, posicao = 1, tamanho=60)
	@XmlElement(name="xFant")
	private String nomeFantasia;
	
	@OneToOne(optional=false, fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="ENDERECO_ID_EMITENTE")
	@NFEExportType
	@XmlElement(name="enderEmit")
	private NotaFiscalEndereco endereco;
	
	/**
	 * IE
	 */
	@Column(name="IE_EMITENTE", nullable=false, length=14)
	@NFEExport(secao=TipoSecao.C, posicao=2, tamanho=14)
	@XmlElement(name="IE")
	private String inscricaoEstadual;
	
	/**
	 * IEST
	 */
	@Column(name="IE_SUBSTITUTO_TRIBUTARIO_EMITENTE", length=14, nullable=true)
	@NFEExport(secao=TipoSecao.C, posicao=3, tamanho=14)
	@XmlElement(name="IEST")
	private String inscricaoEstadualSubstituto;
	
	/**
	 * IM
	 */	
	@Column(name="IM_EMITENTE", length=15, nullable=true)
	@NFEExport(secao=TipoSecao.C, posicao=4, tamanho=14)
	@XmlElement(name="IM")
	private String inscricaoMunicipal;
	
	/**
	 * CNAE
	 */
	@Column(name="CNAE_EMITENTE", length=1, nullable=true)
	@NFEExport(secao=TipoSecao.C, posicao=5, tamanho=7)
	@XmlElement(name="CNAE")
	private String cnae;
	
	/**
	 * CRT
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name="CRT_EMITENTE",length=1, nullable=true)
	@XmlTransient
	@Type(type="br.com.abril.nds.model.fiscal.notafiscal.enums.RegimeTributarioUserType") //Permite persistir como int (valores no XSD)
	private RegimeTributario regimeTributario;
	
	@Transient
	@XmlElement(name="CRT")
	private Integer regimeTributarioXML;
	
	@OneToOne(optional=true, fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="TELEFONE_ID_EMITENTE", updatable=true, insertable=true)
	@NFEExportType
	@XmlTransient
	private NotaFiscalTelefone telefone;
	
	
	/**
	 * Construtor padrão.
	 */
	public IdentificacaoEmitente() {
		
	}



	/**
	 * @return the pessoaEmitenteReferencia
	 */
	public NotaFiscalPessoa getPessoaEmitenteReferencia() {
		return pessoaEmitenteReferencia;
	}



	/**
	 * @param pessoaEmitenteReferencia the pessoaEmitenteReferencia to set
	 */
	public void setPessoaEmitenteReferencia(NotaFiscalPessoa pessoaEmitenteReferencia) {
		this.pessoaEmitenteReferencia = pessoaEmitenteReferencia;
	}



	/**
	 * @return the documento
	 */
	public DocumentoEmitente getDocumento() {
		return documento;
	}



	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(DocumentoEmitente documento) {
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
	 * @return the inscricaoEstadualSubstituto
	 */
	public String getInscricaoEstadualSubstituto() {
		return inscricaoEstadualSubstituto;
	}



	/**
	 * @param inscricaoEstadualSubstituto the inscricaoEstadualSubstituto to set
	 */
	public void setInscricaoEstadualSubstituto(String inscricaoEstadualSubstituto) {
		this.inscricaoEstadualSubstituto = inscricaoEstadualSubstituto;
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
	 * @return the cnae
	 */
	public String getCnae() {
		return cnae;
	}



	/**
	 * @param cnae the cnae to set
	 */
	public void setCnae(String cnae) {
		this.cnae = cnae;
	}



	/**
	 * @return the regimeTributario
	 */
	public RegimeTributario getRegimeTributario() {
		return regimeTributario;
	}



	/**
	 * @param regimeTributario the regimeTributario to set
	 */
	public void setRegimeTributario(RegimeTributario regimeTributario) {
		this.regimeTributario = regimeTributario;
		this.regimeTributarioXML = regimeTributario != null ? regimeTributario.getIntValue() : null;
	}



	/**
	 * @return the endereco
	 */
	public NotaFiscalEndereco getEndereco() {
		return endereco;
	}



	/**
	 * @param endereco the endereco to set
	 */
	public void setEndereco(NotaFiscalEndereco endereco) {
		this.endereco = endereco;
	}



	/**
	 * @return the telefone
	 */
	public NotaFiscalTelefone getTelefone() {
		return telefone;
	}



	/**
	 * @param telefone the telefone to set
	 */
	public void setTelefone(NotaFiscalTelefone telefone) {
		this.telefone = telefone;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getCnae() == null) ? 0 : this.getCnae().hashCode());
		result = prime * result + ((this.getDocumento() == null) ? 0 : this.getDocumento().hashCode());
		result = prime * result + ((this.getEndereco() == null) ? 0 : this.getEndereco().hashCode());
		result = prime * result + ((this.getInscricaoEstadual() == null) ? 0 : this.getInscricaoEstadual().hashCode());
		result = prime * result + ((this.getInscricaoEstadualSubstituto() == null) ? 0 : this.getInscricaoEstadualSubstituto().hashCode());
		result = prime * result + ((this.getInscricaoMunicipal() == null) ? 0 : this.getInscricaoMunicipal().hashCode());
		result = prime * result + ((this.getNome() == null) ? 0 : this.getNome().hashCode());
		result = prime * result + ((this.getNomeFantasia() == null) ? 0 : this.getNomeFantasia().hashCode());
		result = prime * result + ((this.getPessoaEmitenteReferencia() == null) ? 0 : this.getPessoaEmitenteReferencia().hashCode());
		result = prime * result + ((this.getRegimeTributario() == null) ? 0 : this.getRegimeTributario().hashCode());
		result = prime * result + ((this.getTelefone() == null) ? 0 : this.getTelefone().hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		IdentificacaoEmitente other = (IdentificacaoEmitente) obj;
		if (this.getCnae() == null) {
			if (other.getCnae() != null) {
				return false;
			}
		} else if (!this.getCnae().equals(other.getCnae())) {
			return false;
		}
		
		if (this.getDocumento() == null) {
			if (other.getDocumento() != null) {
				return false;
			}
		} else if (!this.getDocumento().equals(other.getDocumento())) {
			return false;
		}
		
		if (this.getEndereco() == null) {
			if (other.getEndereco() != null) {
				return false;
			}
		} else if (!this.getEndereco().equals(other.getEndereco())) {
			return false;
		}

		if (this.getInscricaoEstadual() == null) {
			if (other.getInscricaoEstadual() != null) {
				return false;
			}
		} else if (!this.getInscricaoEstadual().equals(other.getInscricaoEstadual())) {
			return false;
		}
		
		if (this.getInscricaoEstadualSubstituto() == null) {
			if (other.getInscricaoEstadualSubstituto() != null) {
				return false;
			}
		} else if (!this.getInscricaoEstadualSubstituto()
				.equals(other.getInscricaoEstadualSubstituto())) {
			return false;
		}
		
		if (this.getInscricaoMunicipal() == null) {
			if (other.getInscricaoMunicipal() != null) {
				return false;
			}
		} else if (!this.getInscricaoMunicipal().equals(other.getInscricaoMunicipal())) {
			return false;
		}
		
		if (this.getNome() == null) {
			if (other.getNome() != null) {
				return false;
			}
		} else if (!this.getNome().equals(other.getNome())) {
			return false;
		}
		
		if (this.getNomeFantasia() == null) {
			if (other.getNomeFantasia() != null) {
				return false;
			}
		} else if (!this.getNomeFantasia().equals(other.getNomeFantasia())) {
			return false;
		}
		
		if (this.getPessoaEmitenteReferencia() == null) {
			if (other.getPessoaEmitenteReferencia() != null) {
				return false;
			}
		} else if (!this.getPessoaEmitenteReferencia()
				.equals(other.getPessoaEmitenteReferencia())) {
			return false;
		}
		
		if (this.getRegimeTributario() != other.getRegimeTributario()) {
			return false;
		}
		
		if (this.getTelefone() == null) {
			if (other.getTelefone() != null) {
				return false;
			}
		} else if (!this.getTelefone().equals(other.getTelefone())) {
			return false;
		}
		
		return true;
	}


	@Override
	public String toString() {
		return "IdentificacaoEmitente ["
				+ (pessoaEmitenteReferencia != null ? "pessoaEmitenteReferencia="
				+ pessoaEmitenteReferencia + ", ": "")
				+ (documento != null ? "documento=" + documento + ", " : "")
				+ (nome != null ? "nome=" + nome + ", " : "")
				+ (nomeFantasia != null ? "nomeFantasia=" + nomeFantasia + ", ": "")
				+ (inscricaoEstadual != null ? "inscricaoEstadual="
				+ inscricaoEstadual + ", " : "")
				+ (inscricaoEstadualSubstituto != null ? "inscricaoEstadualSubstituto="
				+ inscricaoEstadualSubstituto + ", ": "")
				+ (inscricaoMunicipal != null ? "inscricaoMunicipal="
				+ inscricaoMunicipal + ", " : "")
				+ (cnae != null ? "cnae=" + cnae + ", " : "")
				+ (regimeTributario != null ? "regimeTributario="
				+ regimeTributario + ", " : "")
				+ (endereco != null ? "endereco=" + endereco + ", " : "")
				+ (telefone != null ? "telefone=" + telefone : "") + "]";
	}

}
