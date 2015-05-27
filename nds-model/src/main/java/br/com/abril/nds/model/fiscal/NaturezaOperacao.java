package br.com.abril.nds.model.fiscal;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.Processo;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.cadastro.Tributo;
import br.com.abril.nds.model.movimentacao.TipoMovimento;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
@Entity
@Table(name = "NATUREZA_OPERACAO")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="naturezaOperacao")
@XmlAccessorType(XmlAccessType.FIELD)
public class NaturezaOperacao implements Serializable {

	private static final long serialVersionUID = -5552879848986513495L;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "DESCRICAO", nullable = false)
	private String descricao;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_OPERACAO", nullable = false)
	private TipoOperacao tipoOperacao;
	
	@Column(name = "CFOP_ESTADO")
	private String cfopEstado;
	
	@Column(name = "CFOP_OUTROS_ESTADOS")
	private String cfopOutrosEstados;
	
	@Column(name = "CFOP_EXTERIOR")
	private String cfopExterior;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_ATIVIDADE", nullable = false)
	private TipoAtividade tipoAtividade;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_EMITENTE", nullable = false)
	private TipoDestinatario tipoEmitente;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_DESTINATARIO", nullable = false)
	private TipoDestinatario tipoDestinatario;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinTable(name="PROCESSO_NFE", joinColumns={
				@JoinColumn(name="PROCESSO_NFE_ID", referencedColumnName="id", nullable=false)
	                    },
	            inverseJoinColumns=@JoinColumn(name="PROCESSO", referencedColumnName="id"))
	private Set<Processo> processo;

	@Column(name = "NOTA_FISCAL_VENDA_CONSIGNADO")
	private boolean notaFiscalVendaConsignado;
	
	@Column(name = "NOTA_FISCAL_DEVOLUCAO_SIMBOLICA")
	private boolean notaFiscalDevolucaoSimbolica;
	
	@Column(name = "GERAR_COTA_CONTRIBUINTE_ICMS")
	private boolean gerarCotaContribuinteICMS;
	
	@Column(name = "GERAR_COTA_EXIGE_NFE")
	private boolean gerarCotaExigeNFe;
	
	@Column(name = "GERAR_COTA_NAO_EXIGE_NFE")
	private boolean gerarCotaNaoExigeNFe;
	
	@Column(name = "GERAR_NOTAS_REFERENCIADAS")
	private boolean gerarNotasReferenciadas;
	
	@OneToMany
	@JoinTable(
	            name="NATUREZA_OPERACAO_TIPO_MOVIMENTO",
	            joinColumns={
	            		@JoinColumn(table="NATUREZA_OPERACAO", name="NATUREZA_OPERACAO_ID", referencedColumnName="id", nullable=false)
	                    },
	            inverseJoinColumns=@JoinColumn(table="TIPO_MOVIMENTO", name="TIPO_MOVIMENTO_ID", referencedColumnName="id"))
	private List<TipoMovimento> tipoMovimento;
	
	@OneToMany
	@JoinTable(
	            name="NATUREZA_OPERACAO_TRIBUTO",
	            joinColumns={
	            		@JoinColumn(table="NATUREZA_OPERACAO", name="NATUREZA_OPERACAO_ID", referencedColumnName="id", nullable=false)
	                    },
	            inverseJoinColumns=@JoinColumn(table="TRIBUTOS", name="TRIBUTO_ID", referencedColumnName="id"))
	private List<Tributo> tributosNaturezaOperacao;
	
	@Column(name = "NOTA_FISCAL_NUMERO_NF", length=60)
	private Long notaFiscalNumeroNF;
	
	@Column(name = "NOTA_FISCAL_SERIE", length=60)
	private Long notaFiscalSerie;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "FORMA_COMERCIALIZACAO", nullable = false)
	private FormaComercializacao formaComercializacao;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public TipoOperacao getTipoOperacao() {
		return tipoOperacao;
	}

	public String getCfopEstado() {
		return cfopEstado;
	}

	public void setCfopEstado(String cfopEstado) {
		this.cfopEstado = cfopEstado;
	}

	public String getCfopOutrosEstados() {
		return cfopOutrosEstados;
	}

	public void setCfopOutrosEstados(String cfopOutrosEstados) {
		this.cfopOutrosEstados = cfopOutrosEstados;
	}

	public String getCfopExterior() {
		return cfopExterior;
	}

	public void setCfopExterior(String cfopExterior) {
		this.cfopExterior = cfopExterior;
	}

	public TipoAtividade getTipoAtividade() {
		return tipoAtividade;
	}

	public void setTipoAtividade(TipoAtividade tipoAtividade) {
		this.tipoAtividade = tipoAtividade;
	}

	public TipoDestinatario getTipoEmitente() {
		return tipoEmitente;
	}

	public void setTipoEmitente(TipoDestinatario tipoEmitente) {
		this.tipoEmitente = tipoEmitente;
	}

	public TipoDestinatario getTipoDestinatario() {
		return tipoDestinatario;
	}

	public void setTipoDestinatario(TipoDestinatario tipoDestinatario) {
		this.tipoDestinatario = tipoDestinatario;
	}
	
	public boolean isNotaFiscalVendaConsignado() {
		return notaFiscalVendaConsignado;
	}

	public void setNotaFiscalVendaConsignado(boolean notaFiscalVendaConsignado) {
		this.notaFiscalVendaConsignado = notaFiscalVendaConsignado;
	}

	public boolean isNotaFiscalDevolucaoSimbolica() {
		return notaFiscalDevolucaoSimbolica;
	}

	public void setNotaFiscalDevolucaoSimbolica(boolean notaFiscalDevolucaoSimbolica) {
		this.notaFiscalDevolucaoSimbolica = notaFiscalDevolucaoSimbolica;
	}

	public boolean isGerarCotaContribuinteICMS() {
		return gerarCotaContribuinteICMS;
	}

	public void setGerarCotaContribuinteICMS(boolean gerarCotaContribuinteICMS) {
		this.gerarCotaContribuinteICMS = gerarCotaContribuinteICMS;
	}

	public boolean isGerarCotaExigeNFe() {
		return gerarCotaExigeNFe;
	}

	public void setGerarCotaExigeNFe(boolean gerarCotaExigeNFe) {
		this.gerarCotaExigeNFe = gerarCotaExigeNFe;
	}

	public boolean isGerarCotaNaoExigeNFe() {
		return gerarCotaNaoExigeNFe;
	}

	public void setGerarCotaNaoExigeNFe(boolean gerarCotaNaoExigeNFe) {
		this.gerarCotaNaoExigeNFe = gerarCotaNaoExigeNFe;
	}

	public boolean isGerarNotasReferenciadas() {
		return gerarNotasReferenciadas;
	}

	public void setGerarNotasReferenciadas(boolean gerarNotasReferenciadas) {
		this.gerarNotasReferenciadas = gerarNotasReferenciadas;
	}

	public void setTipoOperacao(TipoOperacao tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

	/**
	 * @return the processo
	 */
	public Set<Processo> getProcesso() {
		return processo;
	}

	/**
	 * @param processo the processo to set
	 */
	public void setProcesso(Set<Processo> processo) {
		this.processo = processo;
	}
	
	public List<TipoMovimento> getTipoMovimento() {
		return tipoMovimento;
	}

	public void setTipoMovimento(List<TipoMovimento> tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}
	
	public Long getNotaFiscalNumeroNF() {
		return notaFiscalNumeroNF;
	}

	public void setNotaFiscalNumeroNF(Long notaFiscalNumeroNF) {
		this.notaFiscalNumeroNF = notaFiscalNumeroNF;
	}

	public Long getNotaFiscalSerie() {
		return notaFiscalSerie;
	}

	public void setNotaFiscalSerie(Long notaFiscalSerie) {
		this.notaFiscalSerie = notaFiscalSerie;
	}
	
	public FormaComercializacao getFormaComercializacao() {
		return formaComercializacao;
	}

	public void setFormaComercializacao(FormaComercializacao formaComercializacao) {
		this.formaComercializacao = formaComercializacao;
	}
	
	public List<Tributo> getTributosNaturezaOperacao() {
		return tributosNaturezaOperacao;
	}
	
	public void setTributosNaturezaOperacao(List<Tributo> tributosNaturezaOperacao) {
		this.tributosNaturezaOperacao = tributosNaturezaOperacao;
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
		NaturezaOperacao other = (NaturezaOperacao) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}
}