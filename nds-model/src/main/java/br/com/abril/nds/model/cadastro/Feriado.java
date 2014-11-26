package br.com.abril.nds.model.cadastro;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import br.com.abril.nds.model.Origem;

/**
 * Cadastro de Feriados
 */
@Entity
@Table(name = "FERIADO", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"DATA", "LOCALIDADE", "UFE_SG", "TIPO_FERIADO" }) })
@SequenceGenerator(name = "FERIADO_SEQ", initialValue = 1, allocationSize = 1)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable(value="feriado", key="#id")
public class Feriado {

	@Id
	@GeneratedValue(generator = "FERIADO_SEQ")
	@Column(name = "ID")
	private Long id;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA", nullable = false)
	private Date data;

	@Column(name = "DESCRICAO", nullable = false)
	private String descricao;

	@Column(name = "UFE_SG")
	private String unidadeFederacao;
	
	@Column(name = "LOCALIDADE")
	private String localidade;

	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_FERIADO")
	private TipoFeriado tipoFeriado;

	@Column(name = "IND_REPETE_ANUALMENTE")
	private boolean indRepeteAnualmente;
	
	@Column(name = "IND_OPERA")
	private boolean indOpera;
	
	@Column(name = "IND_EFETUA_COBRANCA")
	private boolean indEfetuaCobranca;

	@Enumerated(EnumType.STRING)
	@Column(name = "ORIGEM")
	private Origem origem;
	
	
	/**
	 * Obtém id
	 *
	 * @return Long
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Atribuí id
	 * @param id 
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Obtém data
	 *
	 * @return Date
	 */
	public Date getData() {
		return data;
	}

	/**
	 * Atribuí data
	 * @param data 
	 */
	public void setData(Date data) {
		this.data = data;
	}

	/**
	 * Obtém descricao
	 *
	 * @return String
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * Atribuí descricao
	 * @param descricao 
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * Obtém localidade
	 *
	 * @return Localidade
	 */
	public String getLocalidade() {
		return localidade;
	}

	/**
	 * Atribuí localidade
	 * @param localidade 
	 */
	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	/**
	 * Obtém tipoFeriado
	 *
	 * @return TipoFeriado
	 */
	public TipoFeriado getTipoFeriado() {
		return tipoFeriado;
	}

	/**
	 * Atribuí tipoFeriado
	 * @param tipoFeriado 
	 */
	public void setTipoFeriado(TipoFeriado tipoFeriado) {
		this.tipoFeriado = tipoFeriado;
	}

	/**
	 * Obtém indRepeteAnualmente
	 *
	 * @return boolean
	 */
	public boolean isIndRepeteAnualmente() {
		return indRepeteAnualmente;
	}

	/**
	 * Atribuí indRepeteAnualmente
	 * @param indRepeteAnualmente 
	 */
	public void setIndRepeteAnualmente(boolean indRepeteAnualmente) {
		this.indRepeteAnualmente = indRepeteAnualmente;
	}

	/**
	 * Obtém indOpera
	 *
	 * @return boolean
	 */
	public boolean isIndOpera() {
		return indOpera;
	}

	/**
	 * Atribuí indOpera
	 * @param indOpera 
	 */
	public void setIndOpera(boolean indOpera) {
		this.indOpera = indOpera;
	}

	/**
	 * Obtém indEfetuaCobranca
	 *
	 * @return boolean
	 */
	public boolean isIndEfetuaCobranca() {
		return indEfetuaCobranca;
	}

	/**
	 * Atribuí indEfetuaCobranca
	 * @param indEfetuaCobranca 
	 */
	public void setIndEfetuaCobranca(boolean indEfetuaCobranca) {
		this.indEfetuaCobranca = indEfetuaCobranca;
	}

	/**
	 * Obtém unidadeFederacao
	 *
	 * @return UnidadeFederacao
	 */
	public String getUnidadeFederacao() {
		return unidadeFederacao;
	}

	/**
	 * Atribuí unidadeFederacao
	 * @param unidadeFederacao 
	 */
	public void setUnidadeFederacao(String unidadeFederacao) {
		this.unidadeFederacao = unidadeFederacao;
	}

	/**
	 * Obtém origem
	 *
	 * @return Origem
	 */
	public Origem getOrigem() {
		return origem;
	}

	/**
	 * Atribuí origem
	 * @param origem 
	 */
	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Feriado other = (Feriado) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}