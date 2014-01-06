package br.com.abril.nds.model.fiscal.nfe;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.abril.nfe.model.NaturezaOperacaoNds;
import br.com.abril.nfe.model.NotaFicalEndereco;
import br.com.abril.nfe.model.NotaFiscalFatura;
import br.com.abril.nfe.model.NotaFiscalPessoa;
import br.com.abril.nfe.model.NotaFiscalPessoaJuridica;

@Entity
@Table(name = "NOTA_FISCAL")
public class NotaFiscalNds {

	@Embedded
	private br.com.abril.nfe.model.NotaFiscalBase notaFiscal;
	
	@Id
	@GeneratedValue()
	@Column(name="ID")
	private Long id;
	
	@OneToOne
	private NaturezaOperacaoNds naturezaOperacao;
	
	@OneToMany
	private List<NotaFiscalItem> notaFiscalItens; 
	
	@OneToMany
	private List<NotaFiscalFatura> notaFiscalFatura; 
	
	public NotaFiscalNds() {
		if(notaFiscal == null){
			this.notaFiscal = new br.com.abril.nfe.model.NotaFiscalBase();
		}
		
		if(notaFiscal.getEmissor() == null) {
			notaFiscal.setEmissor(new NotaFiscalPessoaJuridica());
		}
		
		if(notaFiscal.getEmissor().getNotaFicalEndereco() == null) {
			notaFiscal.getEmissor().setNotaFicalEndereco(new NotaFicalEndereco());
		}
	}
	
	@Id
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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
	
	public Long getNumero() {
		return this.notaFiscal.getNumero();
	}
	
	public void setNumero(Long numero) {
		this.notaFiscal.setNumero(numero);
	}
	
	public String getSerie() {
		return this.notaFiscal.getSerie();
	}
	
	public void setSerie(String serie) {
		this.notaFiscal.setSerie(serie);
	}
	
	public String getChaveAcesso() {
		return this.notaFiscal.getChaveAcesso();
	}
	
	public void setChaveAcesso(String chaveAcesso) {
		this.notaFiscal.setChaveAcesso(chaveAcesso);
	}
	
	public Date getDataEmissao() {
		return this.notaFiscal.getDataEmissao();
	}
	
	public void setDataEmissao(Date dataEmissao) {
		this.notaFiscal.setDataEmissao(dataEmissao);
	}
	
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

	public String getInformacoesComplementares() {
		return this.getInformacoesComplementares();
	}

	public void setInformacoesComplementares(String informacoesComplementares) {
		this.setInformacoesComplementares(informacoesComplementares);
	}

	public NaturezaOperacaoNds getNaturezaOperacao() {
		return this.naturezaOperacao;
	}

	public void setNaturezaOperacao(NaturezaOperacaoNds naturezaOperacao) {
		this.naturezaOperacao = naturezaOperacao;
	}
	
	
	public List<NotaFiscalItem> getNotaFiscalItens() {
		return notaFiscalItens;
	}

	public void setNotaFiscalItens(List<NotaFiscalItem> notaFiscalItens) {
		this.notaFiscalItens = notaFiscalItens;
	}
	
	public List<NotaFiscalFatura> getNotaFiscalFatura() {
		return notaFiscalFatura;
	}

	public void setNotaFiscalFatura(List<NotaFiscalFatura> notaFiscalFatura) {
		this.notaFiscalFatura = notaFiscalFatura;
	}
	
	
	public NotaFiscalPessoa getNotaFiscalTransportador() {
		return this.notaFiscal.getNotaFiscalTransportador();
	}

	public void setNotaFiscalTransportador(NotaFiscalPessoa notaFiscalTransportador) {
		this.notaFiscal.setNotaFiscalTransportador(notaFiscalTransportador);
	}

}