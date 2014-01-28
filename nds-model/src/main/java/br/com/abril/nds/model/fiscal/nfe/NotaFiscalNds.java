package br.com.abril.nds.model.fiscal.nfe;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalBase;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalFatura;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalPessoa;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalPessoaFisica;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalPessoaJuridica;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalValorCalculado;

@Entity
@Table(name = "NOTA_FISCAL")
public class NotaFiscalNds {

	@Embedded
	private NotaFiscalBase notaFiscal;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "HORA_SAIDA")
	private	String horaSaida;

	@Column(name = "DATA_EXPEDICAO", nullable = true)
	private Date dataEntradaSaida;
	
	@OneToMany(cascade=CascadeType.MERGE, mappedBy="notaFiscal")
	private List<NotaFiscalItem> notaFiscalItens; 
	
	@OneToMany(cascade=CascadeType.MERGE)
	private List<NotaFiscalFatura> notaFiscalFatura; 
	
	@OneToOne(cascade=CascadeType.MERGE)
	@JoinColumn(name="NOTA_FISCAL_VALOR_CALCULADO_ID")
	private NotaFiscalValorCalculado notaFiscalValoresCalculados;
	
	public NotaFiscalNds() {
		if(notaFiscal == null){
			this.notaFiscal = new NotaFiscalBase();
		}
		
		if(notaFiscal.getEmitenteDestinario() == null) {
			notaFiscal.setEmitenteDestinario(new NotaFiscalPessoaFisica());
		}
		
		if(notaFiscal.getEmissor() == null) {
			notaFiscal.setEmissor(new NotaFiscalPessoaJuridica());
		}
		
		if(notaFiscalValoresCalculados == null) {
			notaFiscalValoresCalculados = new NotaFiscalValorCalculado();
		}
	}
	
	public Long getId() {
		return id;
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
	
	public NotaFiscalPessoa getEmissor() {
		return this.notaFiscal.getEmissor();
	}

	public void setEmissor(NotaFiscalPessoa emissor) {
		this.notaFiscal.setEmissor(emissor);
	}

	public NotaFiscalPessoa getEmitenteDestinario() {
		return this.notaFiscal.getEmitenteDestinario();
	}

	public void setEmitenteDestinario(NotaFiscalPessoa emitenteDestinario) {
		this.notaFiscal.setEmitenteDestinario(emitenteDestinario);
	}

	public String getInformacoesComplementares() {
		return this.notaFiscal.getInformacoesComplementares();
	}

	public void setInformacoesComplementares(String informacoesComplementares) {
		this.notaFiscal.setInformacoesComplementares(informacoesComplementares);
	}
	
	public NotaFiscalPessoa getNotaFiscalTransportador() {
		return this.notaFiscal.getNotaFiscalTransportador();
	}

	public void setNotaFiscalTransportador(NotaFiscalPessoa notaFiscalTransportador) {
		this.notaFiscal.setNotaFiscalTransportador(notaFiscalTransportador);
	}

	public List<NotaFiscalItem> getNotaFiscalItens() {
		return notaFiscalItens;
	}

	public void setNotaFiscalItens(List<NotaFiscalItem> notaFiscalItens) {
		this.notaFiscalItens = notaFiscalItens;
	}

	public NotaFiscalValorCalculado getNotaFiscalValoresCalculados() {
		return notaFiscalValoresCalculados;
	}

	public void setNotaFiscalValoresCalculados(
			NotaFiscalValorCalculado notaFiscalValoresCalculados) {
		this.notaFiscalValoresCalculados = notaFiscalValoresCalculados;
	}
	
	public List<NotaFiscalFatura> getNotaFiscalFatura() {
		return notaFiscalFatura;
	}

	public void setNotaFiscalFatura(List<NotaFiscalFatura> notaFiscalFatura) {
		this.notaFiscalFatura = notaFiscalFatura;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
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
		NotaFiscalNds other = (NotaFiscalNds) obj;
		if (this.getId() == null) {
			if (other.id != null)
				return false;
		} else if (!this.getId().equals(other.id))
			return false;
		return true;
	}
	
	
	
}