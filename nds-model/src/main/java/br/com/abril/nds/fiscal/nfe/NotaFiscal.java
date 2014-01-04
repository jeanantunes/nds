package br.com.abril.nds.fiscal.nfe;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nfe.model.NotaFicalEndereco;
import br.com.abril.nfe.model.NotaFiscalPessoa;
import br.com.abril.nfe.model.NotaFiscalPessoaJuridica;

public class NotaFiscal {

	private br.com.abril.nfe.model.NotaFiscal notaFiscal;

	private NaturezaOperacao naturezaOperacao;
	
	private List<NotaFiscalItem> notaFiscalItens; 
	
	public NotaFiscal() {
		if(notaFiscal == null){
			this.notaFiscal = new br.com.abril.nfe.model.NotaFiscal();
		}
		
		if(notaFiscal.getEmissor() == null) {
			notaFiscal.setEmissor(new NotaFiscalPessoaJuridica());
		}
		
		if(notaFiscal.getEmissor().getNotaFicalEndereco() == null) {
			notaFiscal.getEmissor().setNotaFicalEndereco(new NotaFicalEndereco());
		}
	}

	public Long getId() {
		return this.notaFiscal.getId();
	}

	public void setId(Long id) {
		this.notaFiscal.setId(id);
	}

	public String getAmbiente() {
		return this.notaFiscal.getAmbiente();
	}

	public void setAmbiente(String ambiente) {
		this.notaFiscal.setAmbiente(ambiente);
	}

	public String getProtocolo() {
		return this.notaFiscal.getProtocolo();
	}

	public void setProtocolo(String protocolo) {
		this.notaFiscal.setProtocolo(protocolo);
	}

	public String getVersao() {
		return this.notaFiscal.getVersao();
	}

	public void setVersao(String versao) {
		this.notaFiscal.setVersao(versao);
	}
	/*
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
*/
	public NotaFiscalPessoaJuridica getEmissor() {
		return this.notaFiscal.getEmissor();
	}

	public void setEmissor(NotaFiscalPessoaJuridica emissor) {
		this.notaFiscal.setEmissor(emissor);
	}

	public NotaFiscalPessoa getEmitenteDestinario() {
		return this.notaFiscal.getEmitenteDestinario();
	}

	public void setEmitenteDestinario(NotaFiscalPessoa emitenteDestinario) {
		this.notaFiscal.setEmitenteDestinario(emitenteDestinario);
	}
/*
	public String getInformacoesComplementares() {
		return informacoesComplementares;
	}

	public void setInformacoesComplementares(String informacoesComplementares) {
		this.informacoesComplementares = informacoesComplementares;
	}
*/
	public NaturezaOperacao getNaturezaOperacao() {
		return this.naturezaOperacao;
	}

	public void setNaturezaOperacao(NaturezaOperacao naturezaOperacao) {
		this.naturezaOperacao = naturezaOperacao;
	}
/*
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

	*/

	public Date getDataEmissao() {
		return this.notaFiscal.getDataEmissao();
	}

	public void setDataEmissao(Date dataEmissao) {
		this.notaFiscal.setDataEmissao(dataEmissao);
	}
	
	public List<NotaFiscalItem> getNotaFiscalItens() {
		return notaFiscalItens;
	}

	public void setNotaFiscalItens(List<NotaFiscalItem> notaFiscalItens) {
		this.notaFiscalItens = notaFiscalItens;
	}
	
}