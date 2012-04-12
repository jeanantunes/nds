package br.com.abril.nds.model.planejamento;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

@Entity
@Table(name = "CONTROLE_RECOLHIMENTO")
@SequenceGenerator(name="CONTROLE_RECOLHIMENTO_SEQ", initialValue = 1, allocationSize = 1)
public class ControleRecolhimento {
	
	@Id
	@GeneratedValue(generator = "CONTROLE_RECOLHIMENTO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "INICIO_RECOLHIMENTO", nullable = false)
	private Date inicioRecolhimento;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "FINAL_RECOLHIMENTO", nullable = false)
	private Date finalRecolhimento;
	
	@Column(name = "RECOLHIMENTO_ANTECIPADO", nullable = false)
	private boolean recolhimentoAntecipado;
	
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
	
	/**
	 * @return the inicioRecolhimento
	 */
	public Date getInicioRecolhimento() {
		return inicioRecolhimento;
	}
	
	/**
	 * @param inicioRecolhimento the inicioRecolhimento to set
	 */
	public void setInicioRecolhimento(Date inicioRecolhimento) {
		this.inicioRecolhimento = inicioRecolhimento;
	}
	
	/**
	 * @return the finalRecolhimento
	 */
	public Date getFinalRecolhimento() {
		return finalRecolhimento;
	}
	
	/**
	 * @param finalRecolhimento the finalRecolhimento to set
	 */
	public void setFinalRecolhimento(Date finalRecolhimento) {
		this.finalRecolhimento = finalRecolhimento;
	}
	
	/**
	 * @return the recolhimentoAntecipado
	 */
	public boolean isRecolhimentoAntecipado() {
		return recolhimentoAntecipado;
	}
	
	/**
	 * @param recolhimentoAntecipado the recolhimentoAntecipado to set
	 */
	public void setRecolhimentoAntecipado(boolean recolhimentoAntecipado) {
		this.recolhimentoAntecipado = recolhimentoAntecipado;
	}
	
	

}
