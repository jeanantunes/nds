package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;

import br.com.abril.nds.model.cadastro.Processo;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalValorCalculado;
import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEExportType;

@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
public class NotaFiscalInformacoes implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -580456077685816545L;
	
	@Transient
	@XmlAttribute(name="Id")
	private String idNFe;
	
	@XmlAttribute
	private static String versao = "3.10";
		
	/**
	 * IDE
	 */
	@Embedded
	@NFEExportType
	@XmlElement(name="ide")
	private Identificacao identificacao;
	
	/**
	 * EMIT
	 */
	@Embedded
	@NFEExportType
	@XmlElement(name="emit")
	private IdentificacaoEmitente identificacaoEmitente;
	
	/**
	 * DEST
	 */
	@Embedded
	@NFEExportType
	@XmlElement(name="dest")
	private IdentificacaoDestinatario identificacaoDestinatario;
	
	/**
	 * DET -> PROD
	 */
	@OneToMany(mappedBy="produtoServicoPK.notaFiscal", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@NFEExportType
	@XmlElements(value={ @XmlElement(name="det") })
	private List<DetalheNotaFiscal> detalhesNotaFiscal;

	/**
	 * TOTAL
	 */
	@Embedded
	@NFEExportType(secaoPadrao = TipoSecao.W)
	private InformacaoValoresTotais informacaoValoresTotais;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="NOTA_FISCAL_VALOR_CALCULADO_ID", unique = true)
	@XmlElement(name="total")
	private NotaFiscalValorCalculado notaFiscalValoresCalculados = new NotaFiscalValorCalculado();
	
	/**
	 * TRANSP
	 */
	@Embedded
	@NFEExportType
	@XmlElement(name="transp")
	private InformacaoTransporte informacaoTransporte;
	
		
	/**
	 * INFADIC
	 */
	@Embedded
	@NFEExportType
	private InformacaoAdicional informacaoAdicional;
	
	/**
	 * Informações da comunicação eletrônica.
	 */
	@Embedded
	@NFEExportType
	@XmlTransient
	private InformacaoEletronica informacaoEletronica;
	
	/**
	 * Status de processamento interno da nota fiscal
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS_PROCESSAMENTO")
	private StatusProcessamento statusProcessamento;
	
	@OneToMany
	@JoinTable(name = "NOTA_FISCAL_PROCESSO", 
			joinColumns = {@JoinColumn(table="NATUREZA_OPERACAO", name = "PROCESSO_NFE_ID", referencedColumnName="ID")},
			inverseJoinColumns = {@JoinColumn(table="PROCESSO", name = "PROCESSO", referencedColumnName="ID")})
	private Set<Processo> processos;
	
	@Column(name = "CONDICAO", nullable = true)
	@Enumerated(EnumType.STRING)
	private Condicao condicao;
	
	@XmlTransient
	@Column(name = "NOTA_IMPRESSA", nullable = false)
	private boolean notaImpressa;
	
	@Column(name = "INFORMACOES_ADICIONAIS")
	@XmlElement(name="infAdic")
	@Embedded
	private	InfAdicWrapper infAdicWrapper;
	
	
	@XmlTransient
	@Column(name = "QTD_VOLUME_PALLET", nullable = false)
	private Long qtdVolumePallet;
	
	@XmlTransient
	@Column(name = "PESO_BRUTO_LIQUIDO", nullable = false)
	private BigDecimal pesoBrutoLiquido;
	
	@Column(name = "BOLETO_NFE_GERADO")
	private boolean boletoNfeGerado;
	
	/**
	 * Construtor padrão.
	 */
	public NotaFiscalInformacoes() {
		
	}

	public String getIdNFe() {
		return idNFe;
	}

	public void setIdNFe(String idNFe) {
		this.idNFe = idNFe;
	}

	public static String getVersao() {
		return versao;
	}

	public static void setVersao(String versao) {
		NotaFiscalInformacoes.versao = versao;
	}

	/**
	 * @return the identificacao
	 */
	public Identificacao getIdentificacao() {
		return identificacao;
	}

	/**
	 * @param identificacao the identificacao to set
	 */
	public void setIdentificacao(Identificacao identificacao) {
		this.identificacao = identificacao;
	}

	/**
	 * @return the identificacaoEmitente
	 */
	public IdentificacaoEmitente getIdentificacaoEmitente() {
		return identificacaoEmitente;
	}

	/**
	 * @param identificacaoEmitente the identificacaoEmitente to set
	 */
	public void setIdentificacaoEmitente(IdentificacaoEmitente identificacaoEmitente) {
		this.identificacaoEmitente = identificacaoEmitente;
	}

	/**
	 * @return the identificacaoDestinatario
	 */
	public IdentificacaoDestinatario getIdentificacaoDestinatario() {
		return identificacaoDestinatario;
	}

	/**
	 * @param identificacaoDestinatario the identificacaoDestinatario to set
	 */
	public void setIdentificacaoDestinatario(
			IdentificacaoDestinatario identificacaoDestinatario) {
		this.identificacaoDestinatario = identificacaoDestinatario;
	}

	/**
	 * @return
	 */
	public List<DetalheNotaFiscal> getDetalhesNotaFiscal() {
		return detalhesNotaFiscal;
	}

	/**
	 * @param detalhesNotaFiscal
	 */
	public void setDetalhesNotaFiscal(List<DetalheNotaFiscal> detalhesNotaFiscal) {
		this.detalhesNotaFiscal = detalhesNotaFiscal;
	}

	/**
	 * @return the informacaoValoresTotais
	 */
	public InformacaoValoresTotais getInformacaoValoresTotais() {
		return informacaoValoresTotais;
	}

	/**
	 * @param informacaoValoresTotais the informacaoValoresTotais to set
	 */
	public void setInformacaoValoresTotais(
			InformacaoValoresTotais informacaoValoresTotais) {
		this.informacaoValoresTotais = informacaoValoresTotais;
	}

	/**
	 * @return the informacaoTransporte
	 */
	public InformacaoTransporte getInformacaoTransporte() {
		return informacaoTransporte;
	}

	/**
	 * @param informacaoTransporte the informacaoTransporte to set
	 */
	public void setInformacaoTransporte(InformacaoTransporte informacaoTransporte) {
		this.informacaoTransporte = informacaoTransporte;
	}

	

	/**
	 * @return the informacaoAdicional
	 */
	public InformacaoAdicional getInformacaoAdicional() {
		return informacaoAdicional;
	}

	/**
	 * @param informacaoAdicional the informacaoAdicional to set
	 */
	public void setInformacaoAdicional(InformacaoAdicional informacaoAdicional) {
		this.informacaoAdicional = informacaoAdicional;
	}

	/**
	 * @return the informacaoEletronica
	 */
	public InformacaoEletronica getInformacaoEletronica() {
		return informacaoEletronica;
	}

	/**
	 * @param informacaoEletronica the informacaoEletronica to set
	 */
	public void setInformacaoEletronica(InformacaoEletronica informacaoEletronica) {
		this.informacaoEletronica = informacaoEletronica;
	}

	/**
	 * @return the statusProcessamentoInterno
	 */
	public StatusProcessamento getStatusProcessamento() {
		return statusProcessamento;
	}

	/**
	 * @param statusProcessamentoInterno the statusProcessamentoInterno to set
	 */
	public void setStatusProcessamento(
			StatusProcessamento statusProcessamento) {
		this.statusProcessamento = statusProcessamento;
	}

	/**
	 * @return the condicao
	 */
	public Condicao getCondicao() {
		return condicao;
	}

	/**
	 * @param condicao the condicao to set
	 */
	public void setCondicao(Condicao condicao) {
		this.condicao = condicao;
	}

	/**
	 * @return
	 */
	public boolean isNotaImpressa() {
		return notaImpressa;
	}

	/**
	 * @param notaImpressa
	 */
	public void setNotaImpressa(boolean notaImpressa) {
		this.notaImpressa = notaImpressa;
	}
	
	public InfAdicWrapper getInfAdicWrapper() {
		return infAdicWrapper;
	}

	public void setInfAdicWrapper(InfAdicWrapper infAdicWrapper) {
		this.infAdicWrapper = infAdicWrapper;
	}

	public NotaFiscalValorCalculado getNotaFiscalValoresCalculados() {
		return notaFiscalValoresCalculados;
	}

	public void setNotaFiscalValoresCalculados(
			NotaFiscalValorCalculado notaFiscalValoresCalculados) {
		this.notaFiscalValoresCalculados = notaFiscalValoresCalculados;
	}

	public Set<Processo> getProcessos() {
		return processos;
	}

	public void setProcessos(Set<Processo> processos) {
		this.processos = processos;
	}
	

	public Long getqtdVolumePallet() {
		return qtdVolumePallet;
	}

	public void setqtdVolumePallet(Long qtdVolumePallet) {
		this.qtdVolumePallet = qtdVolumePallet;
	}

	public BigDecimal getPesoBrutoLiquido() {
		return pesoBrutoLiquido;
	}

	public void setPesoBrutoLiquido(BigDecimal pesoBrutoLiquido) {
		this.pesoBrutoLiquido = pesoBrutoLiquido;
	}

	public boolean isBoletoNfeGerado() {
		return boletoNfeGerado;
	}

	public void setBoletoNfeGerado(boolean boletoNfeGerado) {
		this.boletoNfeGerado = boletoNfeGerado;
	}
}