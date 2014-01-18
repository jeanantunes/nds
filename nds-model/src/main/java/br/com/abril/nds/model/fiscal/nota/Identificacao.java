package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;

import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;
import br.com.abril.nds.util.export.fiscal.nota.NFEExportType;

@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="ide")
public class Identificacao implements Serializable {
	
	@Transient
	@XmlTransient
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public enum FormaPagamento implements NotaFiscalEnum {
		
		A_VISTA(0), 
		A_PRAZO(1), 
		OUTROS(2);
		
		Integer valor;
		
		FormaPagamento(int valor) {
			this.valor = valor;
		}
		
		@Override
		public Integer getIntValue() {
			return this.valor.intValue();
		}

		public int getValor() {
			return valor;
		}
		
	}	
	
	public enum TipoEmissao implements NotaFiscalEnum {
		
		NORMAL(1), 
		CONTINGENCIA(2);

		private Integer indcador;
		
		TipoEmissao(Integer indcador) {
			this.indcador = indcador;
		}
		
		@Override
		public Integer getIntValue() {
			return indcador.intValue();
		}
	}	
	
	public enum FinalidadeEmissaoNFe implements NotaFiscalEnum {
		
		NORMAL(1), 
		COMPLEMENTAR(2), 
		AJUSTE(3);

		private Integer finalidadeEmissao;
		
		FinalidadeEmissaoNFe(Integer finalidadeEmissao) {
			this.finalidadeEmissao = finalidadeEmissao;
		}
		
		public Integer getIntValue() {
			return finalidadeEmissao.intValue();
		}
	}	
	
	public enum FormatoImpressao implements NotaFiscalEnum {
		
		RETRATO(1), 
		PAISAGEM(2);

		private Integer tipoImpressao;
		
		FormatoImpressao(Integer tipoImpressao) {
			this.tipoImpressao = tipoImpressao;
		}
		
		@Override
		public Integer getIntValue() {
			return tipoImpressao.intValue();
		}
	}	
	
	public enum TipoAmbiente implements NotaFiscalEnum {
		
		PRODUCAO(1), 
		HOMOLOGACAO(2);

		private Integer tipoAmbiente;
		
		TipoAmbiente(Integer tipoAmbiente) {
			this.tipoAmbiente = tipoAmbiente;
		}
		
		@Override
		public Integer getIntValue() {
			return tipoAmbiente.intValue();
		}
	}
	
	public enum ProcessoEmissao implements NotaFiscalEnum {
		
		EMISSAO_NFE_APLICATIVO_CONTRIBUINTE(0), 
		EMISSAO_NFE_AVULSA_PELO_FISCO(1),
		EMISSAO_NFE_AVULSA_PELO_FISCO_COM_CERTIFICADO(2),
		EMISSAO_NFE_APLICATIVO_FORNECIDO_PELO_FISCO(3);

		private Integer processoEmissao;
		
		ProcessoEmissao(Integer processoEmissao) {
			this.processoEmissao = processoEmissao;
		}
		
		@Override
		public Integer getIntValue() {
			return processoEmissao.intValue();
		}
	}
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3614623505646574143L;

	@Column(name="NOTA_FISCAL_CODIGO_UF")
	@XmlElement(name="cUF")
	private Long codigoUf;
	
	@Column(name="NOTA_FISCAL_CODIGO_NF", length=9)
	@XmlElement(name="cNF")
	private String codigoNF;
	
	/**
	 * natOp
	 */
	@Column(name="DESCRICAO_NATUREZA_OPERACAO", length=60, nullable=false)
	@NFEExport(secao=TipoSecao.B, posicao=2, tamanho=60)
	@XmlElement(name="natOp")
	private String descricaoNaturezaOperacao;
	
	/**
	 * indPag
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name="INDICADOR_FORMA_PAGAMENTO", length=1, nullable=false)
	@NFEExport(secao = TipoSecao.B, posicao = 3, tamanho = 1)
	@XmlTransient
	private FormaPagamento formaPagamento;
	
	@Transient
	@XmlElement(name="indPag")
	private String formaPagamentoXML;
	
	/**
	 * mod
	 */
	@Column(name="MODELO_DOCUMENTO_FISCAL", length=2, nullable=false)
	@NFEExport(secao=TipoSecao.B, posicao=5)
	@XmlElement(name="mod")
	private String modeloDocumentoFiscal;
	
	/**
	 * serie
	 */
	@Column(name = "SERIE", length = 3, nullable = false)
	@NFEExport(secao=TipoSecao.B, posicao=5)
	@XmlElement(name="serie")
	private Integer serie;

	/**
	 * nNF
	 */
	@Column(name = "NUMERO_DOCUMENTO_FISCAL", length = 9, nullable = false)
	@NFEExport(secao=TipoSecao.B, posicao=6 , tamanho=9)
	@XmlElement(name="nNF")
	private Long numeroDocumentoFiscal;
	
	/**
	 * dEmi
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_EMISSAO", nullable = false)
	@NFEExport(secao=TipoSecao.B, posicao=7)
	@XmlTransient
	private Date dataEmissao;

	@Transient
	@XmlElement(name="dEmi")
	private String dataEmissaoXML;

	/**
	 * dSaiEnt
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_SAIDA_ENTRADA", nullable = true)
	@NFEExport(secao=TipoSecao.B, posicao=9)
	private Date dataSaidaEntrada;
	
	@Transient
	@XmlElement(name="dSaiEnt")
	private String dataSaidaEntradaXML;

	/**
	 * tpNF
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "TIPO_OPERACAO", length = 1, nullable = false)
	@NFEExport(secao = TipoSecao.B, posicao = 10, tamanho = 1)
	@XmlTransient
	private TipoOperacao tipoOperacao;
	
	@Transient
	@XmlElement(name = "tpNF")
	private int tipoOperacaoXML;
	
	@Column(name="NOTA_FISCAL_CODIGO_MUNICIPIO")
	@XmlElement(name="cMunFG")
	private Long codigoMunicipio;
	
	@Column(name="NOTA_FISCAL_FORMATO_IMPRESSAO")
	@XmlTransient
	private FormatoImpressao formatoImpressao;
	
	@Transient
	@XmlElement(name = "tpImp")
	private Integer formatoImpressaoXML;
	
	/**
	 * tpEmis
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "TIPO_EMISSAO", nullable = true)
	@NFEExport(secao = TipoSecao.B, posicao = 13, tamanho = 1)
	@XmlTransient
	private TipoEmissao tipoEmissao;
	
	@Transient
	@XmlElement(name="tpEmis")
	private Integer tipoEmissaoXML;
	
	@Column(name="NOTA_FISCAL_DV_CHAVE_ACESSO")
	@XmlElement(name="cDV")
	private Long digitoVerificadorChaveAcesso;
	
	/**
	 * tpAmb
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_AMBIENTE", nullable = false)
	@NFEExport(secao = TipoSecao.B, posicao = 13, tamanho = 1)
	@XmlTransient
	private TipoAmbiente tipoAmbiente;

	@Transient
	@XmlElement(name="tpAmb")
	private Integer tipoAmbienteXML;
	

	/**
	 * finNFe
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "FINALIDADE_EMISSAO", nullable = false)
	@NFEExport(secao = TipoSecao.B, posicao = 16, tamanho = 1)
	@XmlTransient
	private FinalidadeEmissaoNFe finalidadeEmissaoNFe;
	
	@Transient
	@XmlElement(name="finNFe")
	private Integer finalidadeEmissaoNFeXML;
	
	/**
	 * finNFe
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "PROCESSO_EMISSAO", nullable = false)
	@NFEExport(secao = TipoSecao.B, posicao = 16, tamanho = 1)
	@XmlTransient
	private ProcessoEmissao processoEmissao;
	
	@Transient
	@XmlElement(name="procEmi")
	private Integer processoEmissaoXML;
	
	@ManyToOne
	@JoinColumn(name = "TIPO_NOTA_FISCAL_ID")
	private NaturezaOperacao tipoNotaFiscal;
	
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
		this.tipoOperacaoXML = tipoOperacao.getTipoOperacaoNumerico();
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
		this.formaPagamentoXML = formaPagamento.getIntValue().toString();
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
		this.dataEmissaoXML = sdf.format(dataEmissao);
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
		this.dataSaidaEntradaXML = sdf.format(dataSaidaEntrada);
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
		this.tipoEmissaoXML = tipoEmissao.getIntValue();
	}

	/**
	 * @return the finalidadeEmissao
	 */
	public FinalidadeEmissaoNFe getFinalidadeEmissaoNFe() {
		return finalidadeEmissaoNFe;
	}

	/**
	 * @param finalidadeEmissaoNFe the finalidadeEmissao to set
	 */
	public void setFinalidadeEmissaoNFe(FinalidadeEmissaoNFe finalidadeEmissaoNFe) {
		this.finalidadeEmissaoNFe = finalidadeEmissaoNFe;
	}

	/**
	 * @return the tipoNotaFiscal
	 */
	public NaturezaOperacao getTipoNotaFiscal() {
		return tipoNotaFiscal;
	}

	/**
	 * @param tipoNotaFiscal the tipoNotaFiscal to set
	 */
	public void setTipoNotaFiscal(NaturezaOperacao tipoNotaFiscal) {
		this.tipoNotaFiscal = tipoNotaFiscal;
	}

	public Long getCodigoUf() {
		return codigoUf;
	}

	public void setCodigoUf(Long codigoUf) {
		this.codigoUf = codigoUf;
	}

	public Long getCodigoMunicipio() {
		return codigoMunicipio;
	}

	public void setCodigoMunicipio(Long codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}

	public FormatoImpressao getFormatoImpressao() {
		return formatoImpressao;
	}

	public void setFormatoImpressao(FormatoImpressao formatoImpressao) {
		this.formatoImpressao = formatoImpressao;
		this.formatoImpressaoXML = formatoImpressao.getIntValue();
	}

	public TipoAmbiente getTipoAmbiente() {
		return tipoAmbiente;
	}

	public void setTipoAmbiente(TipoAmbiente tipoAmbiente) {
		this.tipoAmbiente = tipoAmbiente;
		this.tipoAmbienteXML = tipoAmbiente.getIntValue();
	}

	public Long getCodigoNF() {
		return Long.parseLong(codigoNF);
	}

	public void setCodigoNF(Long codigoNF) {
		this.codigoNF = StringUtils.leftPad(codigoNF != null ? codigoNF.toString() : "", 8, '0') ;
	}

	public String getModeloDocumentoFiscal() {
		return modeloDocumentoFiscal;
	}

	public void setModeloDocumentoFiscal(String modeloDocumentoFiscal) {
		this.modeloDocumentoFiscal = modeloDocumentoFiscal;
	}

	public Long getDigitoVerificadorChaveAcesso() {
		return digitoVerificadorChaveAcesso;
	}

	public void setDigitoVerificadorChaveAcesso(Long digitoVerificadorChaveAcesso) {
		this.digitoVerificadorChaveAcesso = digitoVerificadorChaveAcesso;
	}

	public ProcessoEmissao getProcessoEmissao() {
		return processoEmissao;
	}

	public void setProcessoEmissao(ProcessoEmissao processoEmissao) {
		this.processoEmissao = processoEmissao;
		this.processoEmissaoXML = processoEmissao.getIntValue();
	}
	
}