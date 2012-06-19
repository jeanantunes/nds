package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.util.export.fiscal.nota.NFEExport;

@Embeddable
public class Identificacao implements Serializable {
	
	public enum FormaPagamento {
		A_VISTA, A_PRAZO, OUTROS;
	}
	
	public enum TipoOperacao{
		ENTRADA, SAIDA
	}

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3614623505646574143L;

	/**
	 * cNF
	 */
	@Column(name = "CODIGO_CHAVE_ACESSO", length = 8, nullable = false, columnDefinition = "Código Numérico que compõe a Chave de Acesso")
	@NFEExport(secao="B", posicao=1, tamanho=9)
	private Integer codigoChaveAcesso;

	/**
	 * cDV
	 */
	@Column(name = "DV_CHAVE_ACESSO", length = 1, nullable = false)
	@NFEExport(secao="B", posicao=13, tamanho=1)
	private Integer digitoVerificadorChaveAcesso;

	/**
	 * tpNF
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "TIPO_OPERACAO", length = 1, nullable = false)
	@NFEExport(secao="B", posicao=9, tamanho=1)
	private TipoOperacao tipoOperacao;

	/**
	 * natOp
	 */
	@Column(name="DESCRICAO_NATUREZA_OPERACAO", length=60,nullable=false ,columnDefinition="Descrição da Natureza da Operação")
	private String descricaoNaturezaOperacao;
	
	
	/**
	 * indPag
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name="INDICADOR_FORMA_PAGAMENTO", length=1, nullable=false, columnDefinition="Indicador da forma de pagamento")
	private FormaPagamento formaPagamento;
	
		
	/**
	 * serie
	 */
	@Column(name = "SERIE", length = 3, nullable = false, columnDefinition = "Série do Documento Fiscal")
	private Integer serie;
	
	
	/**
	 * nNF
	 */
	@Column(name = "NUMERO_DOCUMENTO_FISCAL", length = 9, nullable = false, columnDefinition = "Número do Documento Fiscal")
	private Long numeroDocumentoFiscal;
	
	/**
	 * dEmi
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "dataEmissao", nullable = false, columnDefinition = "Data de emissão do Documento Fiscal")
	private Date dataEmissao;
	
	
	/**
	 * dSaiEnt

	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "dataSaidaEntrada", nullable = true, columnDefinition = "Data de Saída ou da Entrada da Mercadoria/Produto")
	private Date dataSaidaEntrada;
	
	/**
	 * dEmi
	 */
	@Temporal(TemporalType.TIME)
	@Column(name = "horaSaidaEntrada", nullable = true, columnDefinition = "Hora de Saída ou da Entrada da Mercadoria/Produto")
	private Date horaSaidaEntrada;
	
	
	@OneToMany(mappedBy="pk.notaFiscal")
	private List<NotaFiscalReferenciada> listReferenciadas;
	
	
	/**
	 * dhCont
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATA_ENTRADA_CONTIGENCIA", nullable=true, columnDefinition="Data hora da entrada em Contigencia")
	private Date dataEntradaContigencia;
	
	/**
	 * xJust
	 */
	@Column(name="JUSTIFICATIVA_ENTRADA_CONTIGENCIA", nullable=true, length=256, columnDefinition="Justificativa da entrada em contigência")
	private String justificativaEntradaContigencia;
	
	
	/**
	 * Construtor padrão.
	 */
	public Identificacao() {

	}

	/**
	 * @return the codigoChaveAcesso
	 */
	public Integer getCodigoChaveAcesso() {
		return codigoChaveAcesso;
	}

	/**
	 * @param codigoChaveAcesso
	 *            the codigoChaveAcesso to set
	 */
	public void setCodigoChaveAcesso(Integer codigoChaveAcesso) {
		this.codigoChaveAcesso = codigoChaveAcesso;
	}

	/**
	 * @return the digitoVerificadorChaveAcesso
	 */
	public Integer getDigitoVerificadorChaveAcesso() {
		return digitoVerificadorChaveAcesso;
	}

	/**
	 * @param digitoVerificadorChaveAcesso
	 *            the digitoVerificadorChaveAcesso to set
	 */
	public void setDigitoVerificadorChaveAcesso(
			Integer digitoVerificadorChaveAcesso) {
		this.digitoVerificadorChaveAcesso = digitoVerificadorChaveAcesso;
	}

	/**
	 * @return the tipoOperacao
	 */
	public TipoOperacao getTipoOperacao() {
		return tipoOperacao;
	}

	/**
	 * @param tipoOperacao the tipoOperacao to set
	 */
	public void setTipoOperacao(TipoOperacao tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

	/**
	 * @return the descricaoNaturezaOperacao
	 */
	public String getDescricaoNaturezaOperacao() {
		return descricaoNaturezaOperacao;
	}

	/**
	 * @param descricaoNaturezaOperacao the descricaoNaturezaOperacao to set
	 */
	public void setDescricaoNaturezaOperacao(String descricaoNaturezaOperacao) {
		this.descricaoNaturezaOperacao = descricaoNaturezaOperacao;
	}

	/**
	 * @return the formaPagamento
	 */
	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	/**
	 * @param formaPagamento the formaPagamento to set
	 */
	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	/**
	 * @return the serie
	 */
	public Integer getSerie() {
		return serie;
	}

	/**
	 * @param serie the serie to set
	 */
	public void setSerie(Integer serie) {
		this.serie = serie;
	}

	/**
	 * @return the numeroDocumentoFiscal
	 */
	public Long getNumeroDocumentoFiscal() {
		return numeroDocumentoFiscal;
	}

	/**
	 * @param numeroDocumentoFiscal the numeroDocumentoFiscal to set
	 */
	public void setNumeroDocumentoFiscal(Long numeroDocumentoFiscal) {
		this.numeroDocumentoFiscal = numeroDocumentoFiscal;
	}

	/**
	 * @return the dataEmissao
	 */
	public Date getDataEmissao() {
		return dataEmissao;
	}

	/**
	 * @param dataEmissao the dataEmissao to set
	 */
	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	/**
	 * @return the dataSaidaEntrada
	 */
	public Date getDataSaidaEntrada() {
		return dataSaidaEntrada;
	}

	/**
	 * @param dataSaidaEntrada the dataSaidaEntrada to set
	 */
	public void setDataSaidaEntrada(Date dataSaidaEntrada) {
		this.dataSaidaEntrada = dataSaidaEntrada;
	}

	/**
	 * @return the horaSaidaEntrada
	 */
	public Date getHoraSaidaEntrada() {
		return horaSaidaEntrada;
	}

	/**
	 * @param horaSaidaEntrada the horaSaidaEntrada to set
	 */
	public void setHoraSaidaEntrada(Date horaSaidaEntrada) {
		this.horaSaidaEntrada = horaSaidaEntrada;
	}

	/**
	 * @return the listReferenciadas
	 */
	public List<NotaFiscalReferenciada> getListReferenciadas() {
		return listReferenciadas;
	}

	/**
	 * @param listReferenciadas the listReferenciadas to set
	 */
	public void setListReferenciadas(List<NotaFiscalReferenciada> listReferenciadas) {
		this.listReferenciadas = listReferenciadas;
	}

	/**
	 * @return the dataEntradaContigencia
	 */
	public Date getDataEntradaContigencia() {
		return dataEntradaContigencia;
	}

	/**
	 * @param dataEntradaContigencia the dataEntradaContigencia to set
	 */
	public void setDataEntradaContigencia(Date dataEntradaContigencia) {
		this.dataEntradaContigencia = dataEntradaContigencia;
	}

	/**
	 * @return the justificativaEntradaContigencia
	 */
	public String getJustificativaEntradaContigencia() {
		return justificativaEntradaContigencia;
	}

	/**
	 * @param justificativaEntradaContigencia the justificativaEntradaContigencia to set
	 */
	public void setJustificativaEntradaContigencia(
			String justificativaEntradaContigencia) {
		this.justificativaEntradaContigencia = justificativaEntradaContigencia;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((codigoChaveAcesso == null) ? 0 : codigoChaveAcesso
						.hashCode());
		result = prime * result
				+ ((dataEmissao == null) ? 0 : dataEmissao.hashCode());
		result = prime
				* result
				+ ((dataEntradaContigencia == null) ? 0
						: dataEntradaContigencia.hashCode());
		result = prime
				* result
				+ ((dataSaidaEntrada == null) ? 0 : dataSaidaEntrada.hashCode());
		result = prime
				* result
				+ ((descricaoNaturezaOperacao == null) ? 0
						: descricaoNaturezaOperacao.hashCode());
		result = prime
				* result
				+ ((digitoVerificadorChaveAcesso == null) ? 0
						: digitoVerificadorChaveAcesso.hashCode());
		result = prime * result
				+ ((formaPagamento == null) ? 0 : formaPagamento.hashCode());
		result = prime
				* result
				+ ((horaSaidaEntrada == null) ? 0 : horaSaidaEntrada.hashCode());
		result = prime
				* result
				+ ((justificativaEntradaContigencia == null) ? 0
						: justificativaEntradaContigencia.hashCode());
		result = prime
				* result
				+ ((listReferenciadas == null) ? 0 : listReferenciadas
						.hashCode());
		result = prime
				* result
				+ ((numeroDocumentoFiscal == null) ? 0 : numeroDocumentoFiscal
						.hashCode());
		result = prime * result + ((serie == null) ? 0 : serie.hashCode());
		result = prime * result
				+ ((tipoOperacao == null) ? 0 : tipoOperacao.hashCode());
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
		Identificacao other = (Identificacao) obj;
		if (codigoChaveAcesso == null) {
			if (other.codigoChaveAcesso != null) {
				return false;
			}
		} else if (!codigoChaveAcesso.equals(other.codigoChaveAcesso)) {
			return false;
		}
		if (dataEmissao == null) {
			if (other.dataEmissao != null) {
				return false;
			}
		} else if (!dataEmissao.equals(other.dataEmissao)) {
			return false;
		}
		if (dataEntradaContigencia == null) {
			if (other.dataEntradaContigencia != null) {
				return false;
			}
		} else if (!dataEntradaContigencia.equals(other.dataEntradaContigencia)) {
			return false;
		}
		if (dataSaidaEntrada == null) {
			if (other.dataSaidaEntrada != null) {
				return false;
			}
		} else if (!dataSaidaEntrada.equals(other.dataSaidaEntrada)) {
			return false;
		}
		if (descricaoNaturezaOperacao == null) {
			if (other.descricaoNaturezaOperacao != null) {
				return false;
			}
		} else if (!descricaoNaturezaOperacao
				.equals(other.descricaoNaturezaOperacao)) {
			return false;
		}
		if (digitoVerificadorChaveAcesso == null) {
			if (other.digitoVerificadorChaveAcesso != null) {
				return false;
			}
		} else if (!digitoVerificadorChaveAcesso
				.equals(other.digitoVerificadorChaveAcesso)) {
			return false;
		}
		if (formaPagamento != other.formaPagamento) {
			return false;
		}
		if (horaSaidaEntrada == null) {
			if (other.horaSaidaEntrada != null) {
				return false;
			}
		} else if (!horaSaidaEntrada.equals(other.horaSaidaEntrada)) {
			return false;
		}
		if (justificativaEntradaContigencia == null) {
			if (other.justificativaEntradaContigencia != null) {
				return false;
			}
		} else if (!justificativaEntradaContigencia
				.equals(other.justificativaEntradaContigencia)) {
			return false;
		}
		if (listReferenciadas == null) {
			if (other.listReferenciadas != null) {
				return false;
			}
		} else if (!listReferenciadas.equals(other.listReferenciadas)) {
			return false;
		}
		if (numeroDocumentoFiscal == null) {
			if (other.numeroDocumentoFiscal != null) {
				return false;
			}
		} else if (!numeroDocumentoFiscal.equals(other.numeroDocumentoFiscal)) {
			return false;
		}
		if (serie == null) {
			if (other.serie != null) {
				return false;
			}
		} else if (!serie.equals(other.serie)) {
			return false;
		}
		if (tipoOperacao != other.tipoOperacao) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Identificacao ["
				+ (codigoChaveAcesso != null ? "codigoChaveAcesso="
						+ codigoChaveAcesso + ", " : "")
				+ (digitoVerificadorChaveAcesso != null ? "digitoVerificadorChaveAcesso="
						+ digitoVerificadorChaveAcesso + ", "
						: "")
				+ (tipoOperacao != null ? "tipoOperacao=" + tipoOperacao + ", "
						: "")
				+ (descricaoNaturezaOperacao != null ? "descricaoNaturezaOperacao="
						+ descricaoNaturezaOperacao + ", "
						: "")
				+ (formaPagamento != null ? "formaPagamento=" + formaPagamento
						+ ", " : "")
				+ (serie != null ? "serie=" + serie + ", " : "")
				+ (numeroDocumentoFiscal != null ? "numeroDocumentoFiscal="
						+ numeroDocumentoFiscal + ", " : "")
				+ (dataEmissao != null ? "dataEmissao=" + dataEmissao + ", "
						: "")
				+ (dataSaidaEntrada != null ? "dataSaidaEntrada="
						+ dataSaidaEntrada + ", " : "")
				+ (horaSaidaEntrada != null ? "horaSaidaEntrada="
						+ horaSaidaEntrada + ", " : "")
				+ (listReferenciadas != null ? "listReferenciadas="
						+ listReferenciadas + ", " : "")
				+ (dataEntradaContigencia != null ? "dataEntradaContigencia="
						+ dataEntradaContigencia + ", " : "")
				+ (justificativaEntradaContigencia != null ? "justificativaEntradaContigencia="
						+ justificativaEntradaContigencia
						: "") + "]";
	}

	

}
