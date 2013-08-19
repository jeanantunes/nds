package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;
import br.com.abril.nds.util.export.fiscal.nota.NFEExportType;

@Embeddable
public class Identificacao implements Serializable {
	
	public enum FormaPagamento implements NotaFiscalEnum {
		A_VISTA, 
		A_PRAZO, 
		OUTROS;
		
		@Override
		public Integer getIntValue() {
			return this.ordinal();
		}
	}	
	
	public enum TipoEmissao implements NotaFiscalEnum {
		NORMAL(1), 
		CONTINGENCIA(2);

		private Integer indcador;
		
		TipoEmissao(Integer indcador){
			this.indcador = indcador;
		}
		
		@Override
		public Integer getIntValue() {
			return indcador;
		}
	}	
	
	public enum FinalidadeEmissao implements NotaFiscalEnum {
		NORMAL(1), 
		COMPLEMENTAR(2), 
		AJUSTE(3);

		private Integer indcador;
		
		FinalidadeEmissao(Integer indcador){
			this.indcador = indcador;
		}
		
		@Override
		public Integer getIntValue() {
			return indcador;
		}
	}	
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3614623505646574143L;

	@ManyToOne
	@JoinColumn(name = "TIPO_NOTA_FISCAL_ID")
	private TipoNotaFiscal tipoNotaFiscal;

	/**
	 * tpNF
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "TIPO_OPERACAO", length = 1, nullable = false)
	@NFEExport(secao = TipoSecao.B, posicao = 10, tamanho = 1)
	private TipoOperacao tipoOperacao;

	/**
	 * natOp
	 */
	@Column(name="DESCRICAO_NATUREZA_OPERACAO", length=60,nullable=false)
	@NFEExport(secao=TipoSecao.B, posicao=2, tamanho=60)
	private String descricaoNaturezaOperacao;
	
	
	/**
	 * indPag
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name="INDICADOR_FORMA_PAGAMENTO", length=1, nullable=false)
	@NFEExport(secao = TipoSecao.B, posicao = 3, tamanho = 1)
	private FormaPagamento formaPagamento;
	
		
	/**
	 * serie
	 */
	@Column(name = "SERIE", length = 3, nullable = false)
	@NFEExport(secao=TipoSecao.B, posicao=5)
	private Integer serie;
	
	
	/**
	 * nNF
	 */
	@Column(name = "NUMERO_DOCUMENTO_FISCAL", length = 9, nullable = false)
	@NFEExport(secao=TipoSecao.B, posicao=6 , tamanho=9)
	private Long numeroDocumentoFiscal;
	
	/**
	 * dEmi
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_EMISSAO", nullable = false)
	@NFEExport(secao=TipoSecao.B, posicao=7)
	private Date dataEmissao;
	
	
	/**
	 * dSaiEnt
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_SAIDA_ENTRADA", nullable = true)
	@NFEExport(secao=TipoSecao.B, posicao=9)
	private Date dataSaidaEntrada;
	
	/**
	 * dEmi
	 */
	@Temporal(TemporalType.TIME)
	@Column(name = "HORA_SAIDA_ENTRADA", nullable = true)
	private Date horaSaidaEntrada;
	
	
	@OneToMany(mappedBy="pk.notaFiscal")
	@NFEExportType
	private List<NotaFiscalReferenciada> listReferenciadas;
	
	/**
	 * tpEmis
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "TIPO_EMISSAO", nullable = true)
	@NFEExport(secao = TipoSecao.B, posicao = 13, tamanho = 1)
	private TipoEmissao tipoEmissao;
	
	/**
	 * finNFe
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "FINALIDADE_EMISSAO", nullable = true)
	@NFEExport(secao = TipoSecao.B, posicao = 16, tamanho = 1)
	private FinalidadeEmissao finalidadeEmissao;
	
	/**
	 * dhCont
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATA_ENTRADA_CONTIGENCIA", nullable=true)
	private Date dataEntradaContigencia;
	
	/**
	 * xJust
	 */
	@Column(name="JUSTIFICATIVA_ENTRADA_CONTIGENCIA", nullable=true, length=256)
	private String justificativaEntradaContigencia;
	
	
	/**
	 * Construtor padrão.
	 */
	public Identificacao() {

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

	/**
	 * @return the tipoEmissao
	 */
	public TipoEmissao getTipoEmissao() {
		return tipoEmissao;
	}

	/**
	 * @param tipoEmissao the tipoEmissao to set
	 */
	public void setTipoEmissao(TipoEmissao tipoEmissao) {
		this.tipoEmissao = tipoEmissao;
	}

	/**
	 * @return the finalidadeEmissao
	 */
	public FinalidadeEmissao getFinalidadeEmissao() {
		return finalidadeEmissao;
	}

	/**
	 * @param finalidadeEmissao the finalidadeEmissao to set
	 */
	public void setFinalidadeEmissao(FinalidadeEmissao finalidadeEmissao) {
		this.finalidadeEmissao = finalidadeEmissao;
	}

	/**
	 * @return the tipoNotaFiscal
	 */
	public TipoNotaFiscal getTipoNotaFiscal() {
		return tipoNotaFiscal;
	}

	/**
	 * @param tipoNotaFiscal the tipoNotaFiscal to set
	 */
	public void setTipoNotaFiscal(TipoNotaFiscal tipoNotaFiscal) {
		this.tipoNotaFiscal = tipoNotaFiscal;
	}
}
