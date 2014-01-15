package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFicalEndereco;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalPessoa;
import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;
import br.com.abril.nds.util.export.fiscal.nota.NFEExportType;

@Embeddable
public class IdentificacaoEmitente implements Serializable {
	
	
	public enum RegimeTributario implements NotaFiscalEnum {
	
		/**
		 * 1 – Simples Nacional
		 */
		SIMPLES_NACIONAL, 
		/**
		 * 2 – Simples Nacional – excesso de sublimite de receita bruta
		 */
		SIMPLES_NACIONAL_EXECESSO,
		/**
		 * 3 – Regime Normal. (v2.0)
		 */
		REGINE_NORMAL;

		@Override
		public Integer getIntValue() {
			return this.ordinal();
		}
	}

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4715921368300274189L;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "PESSOA_EMITENTE_ID_REFERENCIADA")
	private NotaFiscalPessoa pessoaEmitenteReferencia;
	
	/**
	 * CNPJ CPF
	 */
	@Column(name="DOCUMENTO_EMITENTE", nullable=false, length=14)
	private String documento;
	
	/**
	 * xNome
	 */	
	@Column(name="NOME_EMITENTE", nullable=false, length=60)
	@NFEExport(secao=TipoSecao.C, posicao = 0, tamanho=60)
	private String nome;
	
	/**
	 * xFant
	 */
	@Column(name="NOME_FANTASIA_EMITENTE", nullable=true, length=60)
	@NFEExport(secao=TipoSecao.C, posicao = 1, tamanho=60)
	private String nomeFantasia;
	
	/**
	 * IE
	 */
	@Column(name="IE_EMITENTE", nullable=false, length=14)
	@NFEExport(secao=TipoSecao.C, posicao=2, tamanho=14)
	private String inscricaoEstadual;
	
	/**
	 * IEST
	 */
	@Column(name="IE_SUBSTITUTO_TRIBUTARIO_EMITENTE", length=14, nullable=true)
	@NFEExport(secao=TipoSecao.C, posicao=3, tamanho=14)
	private String inscricaoEstadualSubstituto;
	
	/**
	 * IM
	 */	
	@Column(name="IM_EMITENTE", length=15, nullable=true)
	@NFEExport(secao=TipoSecao.C, posicao=4, tamanho=14)
	private String inscricaoMunicipal;
	
	/**
	 * CNAE
	 */
	@Column(name="CNAE_EMITENTE", length=1, nullable=true)
	@NFEExport(secao=TipoSecao.C, posicao=5, tamanho=7)
	private String cnae;
	
	/**
	 * CRT
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name="CRT_EMITENTE",length=1, nullable=true)
	private RegimeTributario regimeTributario;
	
	@OneToOne(optional=false)
	@JoinColumn(name="ENDERECO_ID_EMITENTE")
	@NFEExportType
	private NotaFicalEndereco endereco;
	
	@OneToOne(optional=true)
	@JoinColumn(name="TELEFONE_ID_EMITENTE")
	@NFEExportType
	private Telefone telefone;
	
	
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



	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cnae == null) ? 0 : cnae.hashCode());
		result = prime * result
				+ ((documento == null) ? 0 : documento.hashCode());
		result = prime * result
				+ ((endereco == null) ? 0 : endereco.hashCode());
		result = prime * result
				+ ((inscricaoEstadual == null) ? 0 : inscricaoEstadual.hashCode());
		result = prime
				* result
				+ ((inscricaoEstadualSubstituto == null) ? 0
						: inscricaoEstadualSubstituto.hashCode());
		result = prime
				* result
				+ ((inscricaoMunicipal == null) ? 0 : inscricaoMunicipal
						.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result
				+ ((nomeFantasia == null) ? 0 : nomeFantasia.hashCode());
		result = prime
				* result
				+ ((pessoaEmitenteReferencia == null) ? 0
						: pessoaEmitenteReferencia.hashCode());
		result = prime
				* result
				+ ((regimeTributario == null) ? 0 : regimeTributario.hashCode());
		result = prime * result
				+ ((telefone == null) ? 0 : telefone.hashCode());
		return result;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
		if (cnae == null) {
			if (other.cnae != null) {
				return false;
			}
		} else if (!cnae.equals(other.cnae)) {
			return false;
		}
		if (documento == null) {
			if (other.documento != null) {
				return false;
			}
		} else if (!documento.equals(other.documento)) {
			return false;
		}
		if (endereco == null) {
			if (other.endereco != null) {
				return false;
			}
		} else if (!endereco.equals(other.endereco)) {
			return false;
		}
		if (inscricaoEstadual == null) {
			if (other.inscricaoEstadual != null) {
				return false;
			}
		} else if (!inscricaoEstadual.equals(other.inscricaoEstadual)) {
			return false;
		}
		if (inscricaoEstadualSubstituto == null) {
			if (other.inscricaoEstadualSubstituto != null) {
				return false;
			}
		} else if (!inscricaoEstadualSubstituto
				.equals(other.inscricaoEstadualSubstituto)) {
			return false;
		}
		if (inscricaoMunicipal == null) {
			if (other.inscricaoMunicipal != null) {
				return false;
			}
		} else if (!inscricaoMunicipal.equals(other.inscricaoMunicipal)) {
			return false;
		}
		if (nome == null) {
			if (other.nome != null) {
				return false;
			}
		} else if (!nome.equals(other.nome)) {
			return false;
		}
		if (nomeFantasia == null) {
			if (other.nomeFantasia != null) {
				return false;
			}
		} else if (!nomeFantasia.equals(other.nomeFantasia)) {
			return false;
		}
		if (pessoaEmitenteReferencia == null) {
			if (other.pessoaEmitenteReferencia != null) {
				return false;
			}
		} else if (!pessoaEmitenteReferencia
				.equals(other.pessoaEmitenteReferencia)) {
			return false;
		}
		if (regimeTributario != other.regimeTributario) {
			return false;
		}
		if (telefone == null) {
			if (other.telefone != null) {
				return false;
			}
		} else if (!telefone.equals(other.telefone)) {
			return false;
		}
		return true;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "IdentificacaoEmitente ["
				+ (pessoaEmitenteReferencia != null ? "pessoaEmitenteReferencia="
						+ pessoaEmitenteReferencia + ", "
						: "")
				+ (documento != null ? "documento=" + documento + ", " : "")
				+ (nome != null ? "nome=" + nome + ", " : "")
				+ (nomeFantasia != null ? "nomeFantasia=" + nomeFantasia + ", "
						: "")
				+ (inscricaoEstadual != null ? "inscricaoEstadual="
						+ inscricaoEstadual + ", " : "")
				+ (inscricaoEstadualSubstituto != null ? "inscricaoEstadualSubstituto="
						+ inscricaoEstadualSubstituto + ", "
						: "")
				+ (inscricaoMunicipal != null ? "inscricaoMunicipal="
						+ inscricaoMunicipal + ", " : "")
				+ (cnae != null ? "cnae=" + cnae + ", " : "")
				+ (regimeTributario != null ? "regimeTributario="
						+ regimeTributario + ", " : "")
				+ (endereco != null ? "endereco=" + endereco + ", " : "")
				+ (telefone != null ? "telefone=" + telefone : "") + "]";
	}

	

}
