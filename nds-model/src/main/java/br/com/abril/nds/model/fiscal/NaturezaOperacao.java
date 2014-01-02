package br.com.abril.nds.model.fiscal;

import java.io.Serializable;
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
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Processo;
import br.com.abril.nds.model.cadastro.TipoAtividade;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
@Entity
@Table(name = "NATUREZA_OPERACAO")
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
	
	@ElementCollection(targetClass = Processo.class,fetch=FetchType.EAGER) 
	@CollectionTable(name = "PROCESSO_NFE",
	    joinColumns = @JoinColumn(name = "PROCESSO_NFE_ID"))
	@Column(name = "PROCESSO")
	private Set<Processo> processo;
	
	@Column(name = "CONTRIBUINTE", nullable = false)
	private boolean contribuinte;
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}