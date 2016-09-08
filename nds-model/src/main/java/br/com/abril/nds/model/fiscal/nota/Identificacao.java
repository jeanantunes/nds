package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Type;

import br.com.abril.nds.integracao.persistence.PersistentEnum;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;

@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="ide")
public class Identificacao implements Serializable {
	
	// @Transient
	// @XmlTransient
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Transient
	@XmlTransient
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ") {
		
	private static final long serialVersionUID = -3751355831694201574L;

		public StringBuffer format(Date date, StringBuffer toAppendTo, java.text.FieldPosition pos) {
            StringBuffer toFix = super.format(date, toAppendTo, pos);
            return toFix.insert(toFix.length()-2, ':');
        };
	};
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
		
		public static FormaPagamento getByValue(Integer valor) {
			
			switch(valor) {
				case 0:
					return A_VISTA;
				case 1:
					return A_PRAZO;
				case 2:
					return OUTROS;
				default:
					return null;
			}
			
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
		AJUSTE(3),
		DEVOLUCAO_RETORNO(4);

		private Integer finalidadeEmissao;
		
		FinalidadeEmissaoNFe(Integer finalidadeEmissao) {
			this.finalidadeEmissao = finalidadeEmissao;
		}
		
		public Integer getIntValue() {
			return finalidadeEmissao.intValue();
		}

	}	
	
	public enum FormatoImpressao implements NotaFiscalEnum {
		
		RETRATO(1, "Restrato"), 
		PAISAGEM(2, "Paisagem");

		private Integer tipoImpressao;
		private String descricao;
		
		FormatoImpressao(Integer tipoImpressao, String descricao) {
			this.tipoImpressao = tipoImpressao;
			this.descricao = descricao;
		}
		
		@Override
		public Integer getIntValue() {
			return tipoImpressao.intValue();
		}

		public String getDescricao() {
			return descricao;
		}
		
	}	
	
	public enum TipoAmbiente implements NotaFiscalEnum, PersistentEnum {
		
		PRODUCAO(1, "Produção"), 
		HOMOLOGACAO(2, "Homologação");

		private Integer id;
		private String descricao;
		
		TipoAmbiente(Integer id, String descricao) {
			this.id = id;
			this.descricao = descricao;
		}
		
		@Override
		public Integer getIntValue() {
			return id.intValue();
		}

		@Override
		public int getId() {
			return id.intValue();
		}

		public String getDescricao() {
			return descricao;
		}
		
		public static TipoAmbiente getTipoAmbiente(int value){  
            
            for (TipoAmbiente tipoAmb : TipoAmbiente.values()){  
                if(tipoAmb.getIntValue() == value){  
                    return tipoAmb;  
                }  
            }  
              
            return null;  
        } 
		
	}
	
	// Ajuste info Fisco
	public enum ProcessoEmissao implements NotaFiscalEnum {
		
		EMISSAO_NFE_APLICATIVO_CONTRIBUINTE(0, "Aplicativo do Contribuinte"), 
		EMISSAO_NFE_AVULSA_PELO_FISCO(1, "Avulsa pelo Fisco"),
		EMISSAO_NFE_AVULSA_PELO_FISCO_COM_CERTIFICADO(2, "Avulsa pelo Fisco com Certificado"),
		EMISSAO_NFE_APLICATIVO_FORNECIDO_PELO_FISCO(3, "Aplicativo fornecido pelo fisco"),
		EMISSAO_NFE_INFO_FISCO(4, "Aplicativo info fisco");
		
		private Integer processoEmissao;
		private String descricao; 
		
		ProcessoEmissao(Integer processoEmissao, String descricao) {
			this.processoEmissao = processoEmissao;
			this.descricao = descricao;
			
		}
		
		@Override
		public Integer getIntValue() {
			return processoEmissao.intValue();
		}

		public String getDescricao() {
			return descricao;
		}

	}
	
	public enum LocalDestinoOperacao implements NotaFiscalEnum {
		
		INTERNA(1, "Interna"),
		INTERESTADUAL(2, "Interestadual"),
		EXTERIOR(3, "Exterior");
		
		private Integer localDestinoOperacao;
		private String descricao;
		
		LocalDestinoOperacao(Integer localDestinoOperacao, String descricao) {
			this.localDestinoOperacao = localDestinoOperacao;
			this.setDescricao(descricao); 
		}
		
		@Override
		public Integer getIntValue() {
			return localDestinoOperacao.intValue();
		}

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}

	}
	
	public enum OperacaoConsumidorFinal implements NotaFiscalEnum {
		
		NAO(0), 
		CONSUMIDOR_FINAL(1);

		private Integer operacaoConsumidorFinal;
		
		OperacaoConsumidorFinal(Integer operacaoConsumidorFinal) {
			this.operacaoConsumidorFinal = operacaoConsumidorFinal;
		}
		
		@Override
		public Integer getIntValue() {
			return operacaoConsumidorFinal.intValue();
		}

	}
	
	public enum PresencaConsumidor implements NotaFiscalEnum {
		
		NAO_SE_APLICA(0), 
		OPERACAO_PRESENCIAL(1),
		NAO_PRESENCIAL_INTERNET(2),
		NAO_PRESENCIAL_TELEATENTDIMENTO(3),
		NFC_E_EM_DOMICILIO(4),
		NAO_PRESENCIAL_OUTROS(9);

		private Integer presencaConsumidor;
		
		PresencaConsumidor(Integer operacaoConsumidorFinal) {
			this.presencaConsumidor = operacaoConsumidorFinal;
		}
		
		@Override
		public Integer getIntValue() {
			return presencaConsumidor.intValue();
		}

	}
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3614623505646574143L;

	@Column(name="NOTA_FISCAL_CODIGO_UF")
	@XmlElement(name="cUF")
	private Integer codigoUf;
	
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
	private Long serie;

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
	@XmlElement(name="dhEmi")
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
	@Column(name = "TIPO_OPERACAO", length = 1, nullable = false)
	@NFEExport(secao = TipoSecao.B, posicao = 10, tamanho = 1)
	@XmlTransient
	private TipoOperacao tipoOperacao;
	
	@Transient
	@XmlElement(name = "tpNF")
	private int tipoOperacaoXML;
	
	@Column(name = "LOCAL_DESTINO_OPERACAO", nullable = false)
	@NFEExport(secao=TipoSecao.B, posicao=7)
	@XmlTransient
	private LocalDestinoOperacao localDestinoOperacao;
	
	@Transient
	@XmlElement(name="idDest")
	// @XmlTransient // Usado na versao 3.10
	private Integer localDestinoOperacaoXML;
	
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
	@Column(name = "TIPO_AMBIENTE", nullable = false)
	@NFEExport(secao = TipoSecao.B, posicao = 13, tamanho = 1)
	@XmlTransient
	@Type(type="br.com.abril.nds.model.fiscal.notafiscal.enums.TipoAmbienteUserType") //Permite persistir como int (valores no XSD)
	private TipoAmbiente tipoAmbiente;

	@Transient
	@XmlElement(name="tpAmb")
	private Integer tipoAmbienteXML;
	
	/**
	 * finNFe
	 */
	@Column(name = "FINALIDADE_EMISSAO", nullable = false)
	@NFEExport(secao = TipoSecao.B, posicao = 16, tamanho = 1)
	@XmlTransient
	private FinalidadeEmissaoNFe finalidadeEmissaoNFe;
	
	@Transient
	@XmlElement(name="finNFe")
	private Integer finalidadeEmissaoNFeXML;
	
	@Column(name = "OPERACAO_CONSUMIDOR_FINAL", nullable = false)
	@NFEExport(secao = TipoSecao.B, posicao = 16, tamanho = 1)
	@XmlTransient
	private OperacaoConsumidorFinal operacaoConsumidorFinal;
	
	@Column(name = "PRESENCA_CONSUMIDOR", nullable = false)
	@NFEExport(secao = TipoSecao.B, posicao = 16, tamanho = 1)
	@XmlTransient
	private PresencaConsumidor presencaConsumidor;
	
	@Transient
	@XmlElement(name="indFinal")
	// @XmlTransient // Usado na versao 3.10
	private Integer operacaoConsumidorFinalXML;
	
	@Transient
	@XmlElement(name="indPres")
	// @XmlTransient // Usado na versao 3.10
	private Integer presencaConsumidorXML;
	
	/**
	 * finNFe
	 */
	@Column(name = "PROCESSO_EMISSAO", nullable = false)
	@NFEExport(secao = TipoSecao.B, posicao = 16, tamanho = 1)
	@XmlTransient
	private ProcessoEmissao processoEmissao;
	
	@Transient
	@XmlElement(name="procEmi")
	private Integer processoEmissaoXML;
	
	@Column(name = "VERSAO_SISTEMA_EMISSAO", nullable = false)
	@NFEExport(secao = TipoSecao.B, posicao = 16, tamanho = 1)
	@XmlElement(name="verProc")
	private String versaoSistemaEmissao;
	
	@ManyToOne
	@JoinColumn(name = "NATUREZA_OPERACAO_ID", updatable=true, insertable=true)
	@XmlTransient
	private NaturezaOperacao naturezaOperacao;
	
	/**
	 * dEmi
	 */
	@Temporal(TemporalType.TIME)
	@Column(name = "HORA_SAIDA_ENTRADA", nullable = true)
	private Date horaSaidaEntrada;
	
	@OneToMany(mappedBy="pk.notaFiscal", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@XmlElements(value={ @XmlElement(name="NFref") })
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
	public Long getSerie() {
		return serie;
	}

	/**
	 * @param serie the serie to set
	 */
	public void setSerie(Long serie) {
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

	public List<NotaFiscalReferenciada> getListReferenciadas() {
		return listReferenciadas;
	}

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
		this.finalidadeEmissaoNFeXML = finalidadeEmissaoNFe.getIntValue();
	}

	public OperacaoConsumidorFinal getOperacaoConsumidorFinal() {
		return operacaoConsumidorFinal;
	}

	public void setOperacaoConsumidorFinal(
			OperacaoConsumidorFinal operacaoConsumidorFinal) {
		this.operacaoConsumidorFinal = operacaoConsumidorFinal;
		this.operacaoConsumidorFinalXML = operacaoConsumidorFinal.getIntValue();
	}

	public PresencaConsumidor getPresencaConsumidor() {
		return presencaConsumidor;
	}

	public void setPresencaConsumidor(PresencaConsumidor presencaConsumidor) {
		this.presencaConsumidor = presencaConsumidor;
		this.presencaConsumidorXML = presencaConsumidor.getIntValue();
	}

	public String getVersaoSistemaEmissao() {
		return versaoSistemaEmissao;
	}

	public void setVersaoSistemaEmissao(String versaoSistemaEmissao) {
		this.versaoSistemaEmissao = versaoSistemaEmissao;
	}

	/**
	 * @return the tipoNotaFiscal
	 */
	public NaturezaOperacao getNaturezaOperacao() {
		return naturezaOperacao;
	}

	/**
	 * @param naturezaOperacao the tipoNotaFiscal to set
	 */
	public void setNaturezaOperacao(NaturezaOperacao naturezaOperacao) {
		this.naturezaOperacao = naturezaOperacao;
	}

	public Integer getCodigoUf() {
		return codigoUf;
	}

	public void setCodigoUf(Integer codigoUf) {
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
	
	public void setCodigoNF(String codigoNF) {
		this.codigoNF = codigoNF;
	}
	
	public String getCodigoNF() {
		return codigoNF;
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

	public LocalDestinoOperacao getLocalDestinoOperacao() {
		return localDestinoOperacao;
	}

	public void setLocalDestinoOperacao(LocalDestinoOperacao localDestinoOperacao) {
		this.localDestinoOperacao = localDestinoOperacao;
		this.localDestinoOperacaoXML = localDestinoOperacao.getIntValue();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getNumeroDocumentoFiscal() == null) ? 0 : this.getNumeroDocumentoFiscal().hashCode());
		result = prime * result + ((this.getSerie()  == null) ? 0 : this.getSerie().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Identificacao other = (Identificacao) obj;
		if (this.getNumeroDocumentoFiscal() == null) {
			if (other.getNumeroDocumentoFiscal() != null)
				return false;
		} else if (!this.getNumeroDocumentoFiscal().equals(other.getNumeroDocumentoFiscal()))
			return false;
		if (this.getSerie() == null) {
			if (other.getSerie() != null)
				return false;
		} else if (!this.getSerie().equals(other.getSerie()))
			return false;
		return true;
	}
}