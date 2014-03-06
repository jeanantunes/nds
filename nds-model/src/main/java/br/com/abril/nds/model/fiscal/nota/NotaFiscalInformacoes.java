package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
	
	@XmlAttribute(name="Id")
	private static String idNFe = "NFe00000000000000000000000000000000000000000000";
	
	@XmlAttribute
	private static String versao = "2.00";
		
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
	private InformacaoEletronica informacaoEletronica;
	
	/**
	 * Status de processamento interno da nota fiscal
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS_PROCESSAMENTO_INTERNO")
	private StatusProcessamentoInterno statusProcessamentoInterno;
	
	@ElementCollection
	@JoinTable(name = "NOTA_FISCAL_PROCESSO", 
			joinColumns = {@JoinColumn(name = "NOTA_FISCAL_ID", referencedColumnName="ID")})
	@Column(name = "PROCESSO")
	@Enumerated(EnumType.STRING)
	private Set<Processo> processos;
	
	@Column(name = "CONDICAO", nullable = true)
	@Enumerated(EnumType.STRING)
	private Condicao condicao;
	
	@XmlTransient
	@Column(name = "NOTA_IMPRESSA", nullable = false)
	private boolean notaImpressa;
	
	@Column(name = "INFORMACOES_ADICIONAIS")
	private	String informacoesAdicionais;
	
	/**
	 * Construtor padrão.
	 */
	public NotaFiscalInformacoes() {
		
	}

	public static String getIdNFe() {
		return idNFe;
	}

	public static void setIdNFe(String idNFe) {
		NotaFiscalInformacoes.idNFe = idNFe;
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
	public StatusProcessamentoInterno getStatusProcessamentoInterno() {
		return statusProcessamentoInterno;
	}

	/**
	 * @param statusProcessamentoInterno the statusProcessamentoInterno to set
	 */
	public void setStatusProcessamentoInterno(
			StatusProcessamentoInterno statusProcessamentoInterno) {
		this.statusProcessamentoInterno = statusProcessamentoInterno;
	}

	/**
	 * @return the processos
	 */
	public Set<Processo> getProcessos() {
		return processos;
	}

	/**
	 * @param processos the processos to set
	 */
	public void setProcessos(Set<Processo> processos) {
		this.processos = processos;
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
	
	public NotaFiscalValorCalculado getNotaFiscalValoresCalculados() {
		return notaFiscalValoresCalculados;
	}

	public void setNotaFiscalValoresCalculados(
			NotaFiscalValorCalculado notaFiscalValoresCalculados) {
		this.notaFiscalValoresCalculados = notaFiscalValoresCalculados;
	}

	public String getInformacoesAdicionais() {
		return informacoesAdicionais;
	}

	public void setInformacoesAdicionais(String informacoesAdicionais) {
		this.informacoesAdicionais = informacoesAdicionais;
	}
	
}
