package br.com.abril.nfe.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nfe.enums.TipoNotaFiscal;

@Entity
@Table(name = "NOTA_FISCAL")
@SequenceGenerator(name = "NOTA_FISCAL_SEQ", initialValue = 1, allocationSize = 1)
public class NotaFiscal implements Serializable {

	private static final long serialVersionUID = 2732018921335153522L;
	
	@Id
	@GeneratedValue(generator = "NOTA_FISCAL_SEQ")
	@Column(name="ID")
	private Long id;
	
	@Column(name = "FORMA_PAGAMENTO")
	private	String formaPagamento;
	
	@Column(name = "AMBIENTE")
	private	String ambiente;
	
	@Column(name = "PROTOCOLO")
	private	String protocolo;
	
	@Column(name = "VERSAO")
	private	String versao;
	
	@Column(name = "EMISSOR_INSCRICAO_ESTADUAL_SUBSTITUTO")
	private	String emissorInscricaoEstadualSubstituto;
	
	@Column(name = "EMISSOR_INSCRICAO_MUNICIPAL")
	private	String emissorInscricaoMunicipal;
	
	@Column(name = "INFORMACOES_COMPLEMENTARES")
	private	String informacoesComplementares;
	
	@Column(name = "NUMERO_FATURA")
	private	String numeroFatura;
	
	@Column(name = "VALOR_FATURA")
	private	BigDecimal valorFatura;
	
	@Column(name = "NUMERO")
	protected Long numeroNotaFiscal;
	
	@Column(name = "SERIE")
	protected String serie;
	
	@Column(name = "CHAVE_ACESSO")
	protected String chaveAcesso;
	
	@Column(name = "VALOR_BRUTO", nullable = false, precision=18, scale=4)
	protected BigDecimal valorBruto;
	
	@Column(name = "VALOR_LIQUIDO", nullable = false, precision=18, scale=4)
	protected BigDecimal valorLiquido;
	
	@Column(name = "VALOR_INFORMADO", nullable = true, precision=18, scale=4)
	protected BigDecimal valorInformado;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_NotaFiscal")
	private TipoNotaFiscal tipoNotaFiscal;	
	
	@OneToOne(mappedBy="emitenteDestinario")
	@JoinColumn(name="EMITENTE_DESTINARIO_ID", unique=true)
	private EmitenteDestinario emitenteDestinario;
	
	@OneToOne(mappedBy="notaFiscalFatura")
	@JoinColumn(name="NOTA_FISCAL_FATURA_ID", unique=true)
	private NotaFiscalFatura notaFiscalFatura;
	
	@OneToOne(mappedBy="notaFiscalTransportador")
	@JoinColumn(name="NOTA_FISCAL_TRANSPORTADOR_ID", unique=true)
	private NotaFiscalTransportador notaFiscalTransportador;

	@OneToOne(mappedBy="notaFiscalValorCalculado")
	@JoinColumn(name="VALOR_CALCULADOS_ID", unique=true)
	private NotaFiscalValorCalculado notaFiscalValorCalculado;
	
	@ManyToOne
	@JoinColumn(name = "PESSOA_ID")
	private NotaFiscalPessoa pessoa;
	
	@OneToMany()
	private List<NotaFiscalItem> notaFiscalitens;
	
	public Long getId() {
		return id;
	}	
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getNumeroNotaFiscal() {
		return numeroNotaFiscal;
	}

	public void setNumeroNotaFiscal(Long numeroNotaFiscal) {
		this.numeroNotaFiscal = numeroNotaFiscal;
	}
	
	public String getSerie() {
		return serie;
	}
	
	public void setSerie(String serie) {
		this.serie = serie;
	}
	
	public String getChaveAcesso() {
		return chaveAcesso;
	}
	
	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}
	
	public BigDecimal getValorBruto() {
		return valorBruto;
	}

	public void setValorBruto(BigDecimal valorBruto) {
		this.valorBruto = valorBruto;
	}

	public BigDecimal getValorLiquido() {
		return valorLiquido;
	}

	public void setValorLiquido(BigDecimal valorLiquido) {
		this.valorLiquido = valorLiquido;
	}
	
	public String getFormaPagamento() {
		return formaPagamento;
	}
	
	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public String getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public String getVersao() {
		return versao;
	}

	public void setVersao(String versao) {
		this.versao = versao;
	}

	public String getEmissorInscricaoEstadualSubstituto() {
		return emissorInscricaoEstadualSubstituto;
	}

	public void setEmissorInscricaoEstadualSubstituto(
			String emissorInscricaoEstadualSubstituto) {
		this.emissorInscricaoEstadualSubstituto = emissorInscricaoEstadualSubstituto;
	}

	public String getEmissorInscricaoMunicipal() {
		return emissorInscricaoMunicipal;
	}

	public void setEmissorInscricaoMunicipal(String emissorInscricaoMunicipal) {
		this.emissorInscricaoMunicipal = emissorInscricaoMunicipal;
	}
	
	public String getInformacoesComplementares() {
		return informacoesComplementares;
	}

	public void setInformacoesComplementares(String informacoesComplementares) {
		this.informacoesComplementares = informacoesComplementares;
	}

	public String getNumeroFatura() {
		return numeroFatura;
	}

	public void setNumeroFatura(String numeroFatura) {
		this.numeroFatura = numeroFatura;
	}

	public BigDecimal getValorFatura() {
		return valorFatura;
	}

	public void setValorFatura(BigDecimal valorFatura) {
		this.valorFatura = valorFatura;
	}
	
	public BigDecimal getValorInformado() {
		return valorInformado;
	}

	public void setValorInformado(BigDecimal valorInformado) {
		this.valorInformado = valorInformado;
	}


	public TipoNotaFiscal getTipoNotaFiscal() {
		return tipoNotaFiscal;
	}


	public void setTipoNotaFiscal(TipoNotaFiscal tipoNotaFiscal) {
		this.tipoNotaFiscal = tipoNotaFiscal;
	}


	public EmitenteDestinario getEmitenteDestinario() {
		return emitenteDestinario;
	}


	public void setEmitenteDestinario(EmitenteDestinario emitenteDestinario) {
		this.emitenteDestinario = emitenteDestinario;
	}


	public NotaFiscalFatura getNotaFiscalFatura() {
		return notaFiscalFatura;
	}


	public void setNotaFiscalFatura(NotaFiscalFatura notaFiscalFatura) {
		this.notaFiscalFatura = notaFiscalFatura;
	}
	
	public NotaFiscalValorCalculado getNotaFiscalValorCalculado() {
		return notaFiscalValorCalculado;
	}

	public void setNotaFiscalValorCalculado(
			NotaFiscalValorCalculado notaFiscalValorCalculado) {
		this.notaFiscalValorCalculado = notaFiscalValorCalculado;
	}

	public NotaFiscalTransportador getNotaFiscalTransportador() {
		return notaFiscalTransportador;
	}


	public void setNotaFiscalTransportador(
			NotaFiscalTransportador notaFiscalTransportador) {
		this.notaFiscalTransportador = notaFiscalTransportador;
	}


	public NotaFiscalPessoa getPessoa() {
		return pessoa;
	}


	public void setPessoa(NotaFiscalPessoa pessoa) {
		this.pessoa = pessoa;
	}


	public List<NotaFiscalItem> getNotaFiscalitens() {
		return notaFiscalitens;
	}


	public void setNotaFiscalitens(List<NotaFiscalItem> notaFiscalitens) {
		this.notaFiscalitens = notaFiscalitens;
	}
}
