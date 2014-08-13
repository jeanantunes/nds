package br.com.abril.nds.model.fiscal.notafiscal;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Embeddable
public class NotaFiscalBase implements Serializable {

	private static final long serialVersionUID = 2732018921335153522L;
	
	@Column(name = "AMBIENTE")
	private	String ambiente;
	
	@Column(name = "PROTOCOLO")
	private	String protocolo;
	
	@Column(name = "VERSAO")
	private	String versao;
	
	@Column(name = "NUMERO")
	protected Long numero;
	
	@Column(name = "SERIE")
	protected String serie;
	
	@Column(name = "CHAVE_ACESSO")
	protected String chaveAcesso;
	
	@Column(name = "DATA_EMISSAO", nullable = false)
	private Date dataEmissao;
	
	@JoinColumn(name = "NOTA_FISCAL_EMISSOR_PESSOA_ID")
	private NotaFiscalPessoa emissor;
	
	@JoinColumn(name="EMITENTE_DESTINARIO_PESSOA_ID", unique=true)
	private NotaFiscalPessoa emitenteDestinario;
	
	@JoinColumn(name="NOTA_FISCAL_TRANSPORTADOR_PESSOA_ID", unique=true)
	private NotaFiscalPessoa notaFiscalTransportador;

	@OneToOne(cascade=CascadeType.MERGE)
	@JoinColumn(name="NOTA_FISCAL_VALOR_CALCULADO_ID", unique=true, insertable=false, updatable=false)
	private NotaFiscalValorCalculado notaFiscalValoresCalculados;
	
	@Column(name = "INFORMACOES_COMPLEMENTARES")
	private	String informacoesComplementares;

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

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
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

	public NotaFiscalPessoa getEmissor() {
		return emissor;
	}

	public void setEmissor(NotaFiscalPessoa emissor) {
		this.emissor = emissor;
	}

	public NotaFiscalPessoa getEmitenteDestinario() {
		return emitenteDestinario;
	}

	public void setEmitenteDestinario(NotaFiscalPessoa emitenteDestinario) {
		this.emitenteDestinario = emitenteDestinario;
	}

	public String getInformacoesComplementares() {
		return informacoesComplementares;
	}

	public void setInformacoesComplementares(String informacoesComplementares) {
		this.informacoesComplementares = informacoesComplementares;
	}

	public NotaFiscalPessoa getNotaFiscalTransportador() {
		return notaFiscalTransportador;
	}

	public void setNotaFiscalTransportador(NotaFiscalPessoa notaFiscalTransportador) {
		this.notaFiscalTransportador = notaFiscalTransportador;
	}

	public NotaFiscalValorCalculado getNotaFiscalValoresCalculados() {
		return notaFiscalValoresCalculados;
	}

	public void setNotaFiscalValoresCalculados(
			NotaFiscalValorCalculado notaFiscalValoresCalculados) {
		this.notaFiscalValoresCalculados = notaFiscalValoresCalculados;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	@Override
	public String toString() {
		return "NotaFiscalBase [ambiente=" + ambiente + ", protocolo="
				+ protocolo + ", versao=" + versao + ", numero=" + numero
				+ ", serie=" + serie + ", chaveAcesso=" + chaveAcesso
				+ ", dataEmissao=" + dataEmissao + ", emissor=" + emissor
				+ ", emitenteDestinario=" + emitenteDestinario
				+ ", informacoesComplementares=" + informacoesComplementares
				+ "]";
	}
}
