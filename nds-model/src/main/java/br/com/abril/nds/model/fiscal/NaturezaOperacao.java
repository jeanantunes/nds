package br.com.abril.nds.model.fiscal;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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

import br.com.abril.nds.model.cadastro.Processo;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.movimentacao.TipoMovimento;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
@Entity
@Table(name = "NATUREZA_OPERACAO")
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY, region="naturezaOperacao")
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
	@Column(name = "TIPO_DESTINATARIO", nullable = false)
	private TipoDestinatario tipoDestinatario;
	
	@ElementCollection(targetClass = Processo.class,fetch=FetchType.EAGER) 
	@CollectionTable(name = "PROCESSO_NFE",
	    joinColumns = @JoinColumn(name = "PROCESSO_NFE_ID"))
	@Column(name = "PROCESSO")
	private Set<Processo> processo;

	@Column(name = "CONTRIBUINTE", nullable = false)
	private boolean contribuinte;
	
	@OneToMany
	@JoinTable(
	            name="NATUREZA_OPERACAO_TIPO_MOVIMENTO",
	            joinColumns={
	            		@JoinColumn(table="NATUREZA_OPERACAO", name="NATUREZA_OPERACAO_ID", referencedColumnName="id", nullable=false)
	                    },
	            inverseJoinColumns=@JoinColumn(table="TIPO_MOVIMENTO",name="TIPO_MOVIMENTO_ID", referencedColumnName="id"))
	private List<TipoMovimento> tipoMovimento;
	
	
	@Column(name = "NOTA_FISCAL_SERIE", length=60)
	private Long notaFiscalSerie;
	
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

	public TipoDestinatario getTipoDestinatario() {
		return tipoDestinatario;
	}

	public void setTipoDestinatario(TipoDestinatario tipoDestinatario) {
		this.tipoDestinatario = tipoDestinatario;
	}

	public boolean isContribuinte() {
		return contribuinte;
	}

	public void setContribuinte(boolean contribuinte) {
		this.contribuinte = contribuinte;
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
	
	public Long getNotaFiscalSerie() {
		return notaFiscalSerie;
	}

	public void setNotaFiscalSerie(Long notaFiscalSerie) {
		this.notaFiscalSerie = notaFiscalSerie;
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