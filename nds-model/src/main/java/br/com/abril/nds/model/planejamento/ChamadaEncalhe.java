package br.com.abril.nds.model.planejamento;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

/**
 * Entidades para as possíveis chamadas de encalhe
 * dos produtos
 * 
 * @author francisco.garcia
 *
 */
@Entity
@Table(name = "CHAMADA_ENCALHE")
@SequenceGenerator(name="CHAMADA_ENCALHE_SEQ", initialValue = 1, allocationSize = 1)
public class ChamadaEncalhe implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CHAMADA_ENCALHE_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_RECOLHIMENTO", nullable = false)
	private Date dataRecolhimento;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_CHAMADA_ENCALHE", nullable = false)
	private TipoChamadaEncalhe tipoChamadaEncalhe;
	
	@OneToMany(mappedBy = "chamadaEncalhe", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
	private Set<ChamadaEncalheCota> chamadaEncalheCotas = new HashSet<ChamadaEncalheCota>();
	
	@ManyToMany
	@JoinTable(name = "CHAMADA_ENCALHE_LANCAMENTO", joinColumns = {@JoinColumn(name = "CHAMADA_ENCALHE_ID")}, 
	inverseJoinColumns = {@JoinColumn(name = "LANCAMENTO_ID")})
	private Set<Lancamento> lancamentos = new HashSet<Lancamento>();
	
	@Column(name = "SEQUENCIA", nullable = true)
	private Integer sequencia;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return the produtoEdicao
	 */
	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}
	
	/**
	 * @param produtoEdicao the produtoEdicao to set
	 */
	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	public TipoChamadaEncalhe getTipoChamadaEncalhe() {
		return tipoChamadaEncalhe;
	}
	
	public void setTipoChamadaEncalhe(TipoChamadaEncalhe tipoChamadaEncalhe) {
		this.tipoChamadaEncalhe = tipoChamadaEncalhe;
	}

	/**
	 * @return the dataRecolhimento
	 */
	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	/**
	 * @param dataRecolhimento the dataRecolhimento to set
	 */
	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}

	/**
	 * @return the chamadaEncalheCotas
	 */
	public Set<ChamadaEncalheCota> getChamadaEncalheCotas() {
		return chamadaEncalheCotas;
	}

	/**
	 * @param chamadaEncalheCotas the chamadaEncalheCotas to set
	 */
	public void setChamadaEncalheCotas(Set<ChamadaEncalheCota> chamadaEncalheCotas) {
		this.chamadaEncalheCotas = chamadaEncalheCotas;
	}

	/**
	 * @return the lancamentos
	 */
	public Set<Lancamento> getLancamentos() {
		return lancamentos;
	}

	/**
	 * @param lancamentos the lancamentos to set
	 */
	public void setLancamentos(Set<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}

	/**
	 * @return the sequencia
	 */
	public Integer getSequencia() {
		return sequencia;
	}

	/**
	 * @param sequencia the sequencia to set
	 */
	public void setSequencia(Integer sequencia) {
		this.sequencia = sequencia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		result = prime * result + ((this.getSequencia() == null) ? 0 : this.getSequencia().hashCode());
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
		ChamadaEncalhe other = (ChamadaEncalhe) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		if (this.getSequencia() == null) {
			if (other.getSequencia() != null)
				return false;
		} else if (!this.getSequencia().equals(other.getSequencia()))
			return false;
		return true;
	}
}
