package br.com.abril.nfe.model;

import java.io.Serializable;
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
import javax.persistence.Table;

import br.com.abril.nfe.enums.NotaFiscalTipoOperacao;

@Entity
@Table(name = "NOTA_FISCAL")
public class NotaFiscal implements Serializable {

	private static final long serialVersionUID = 2732018921335153522L;
	
	@Id
	@GeneratedValue(generator = "NOTA_FISCAL_SEQ")
	@Column(name="ID")
	private Long id;
	
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
	
	@ManyToOne
	@JoinColumn(name = "NOTA_FISCAL_EMISSOR_PESSOA_ID")
	private NotaFiscalPessoaJuridica emissor;
	
	@OneToOne(mappedBy="emitenteDestinario")
	@JoinColumn(name="EMITENTE_DESTINARIO_PESSOA_ID", unique=true)
	private NotaFiscalPessoa emitenteDestinario;
	
	@Column(name = "INFORMACOES_COMPLEMENTARES")
	private	String informacoesComplementares;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "NOTA_FISCAL_TIPO_OPERACAO")
	private NotaFiscalTipoOperacao notaFiscalTipoOperacao;	
	
	@ManyToOne
	private List<NotaFiscalFatura> notaFiscalFatura;
	
	@OneToOne(mappedBy="notaFiscalTransportador")
	@JoinColumn(name="NOTA_FISCAL_TRANSPORTADOR_PESSOA_ID", unique=true)
	private NotaFiscalPessoa notaFiscalTransportador;

	@OneToOne(mappedBy="notaFiscalValorCalculado")
	@JoinColumn(name="VALORES_CALCULADOS_ID", unique=true)
	private NotaFiscalValorCalculado notaFiscalValoresCalculados;
	
	@OneToMany()
	private List<NotaFiscalItem> notaFiscalitens;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public NotaFiscalPessoaJuridica getEmissor() {
		return emissor;
	}

	public void setEmissor(NotaFiscalPessoaJuridica emissor) {
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

	public NotaFiscalTipoOperacao getNotaFiscalTipoOperacao() {
		return notaFiscalTipoOperacao;
	}

	public void setNotaFiscalTipoOperacao(
			NotaFiscalTipoOperacao notaFiscalTipoOperacao) {
		this.notaFiscalTipoOperacao = notaFiscalTipoOperacao;
	}

	public List<NotaFiscalFatura> getNotaFiscalFatura() {
		return notaFiscalFatura;
	}

	public void setNotaFiscalFatura(List<NotaFiscalFatura> notaFiscalFatura) {
		this.notaFiscalFatura = notaFiscalFatura;
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

	public List<NotaFiscalItem> getNotaFiscalitens() {
		return notaFiscalitens;
	}

	public void setNotaFiscalitens(List<NotaFiscalItem> notaFiscalitens) {
		this.notaFiscalitens = notaFiscalitens;
	}
	
}