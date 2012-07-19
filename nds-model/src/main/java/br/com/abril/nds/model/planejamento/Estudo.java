package br.com.abril.nds.model.planejamento;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
@Entity
@Table(name = "ESTUDO", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"DATA_LANCAMENTO", "PRODUTO_EDICAO_ID" }) })
@SequenceGenerator(name = "ESTUDO_SEQ", initialValue = 1, allocationSize = 1)
public class Estudo implements Serializable {

	private static final long serialVersionUID = -1896990365355368745L;

	@Id
	@GeneratedValue(generator = "ESTUDO_SEQ")
	@Column(name = "ID")
	private Long id;
	@Column(name = "QTDE_REPARTE", nullable = false)
	private BigDecimal qtdeReparte;
	@Column(name = "DATA_LANCAMENTO", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataLancamento;
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID", nullable = false)
	private ProdutoEdicao produtoEdicao;
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(mappedBy = "estudo")
	private Set<Lancamento> lancamentos = new HashSet<Lancamento>();
	
	@OneToMany(mappedBy = "estudo", cascade = {CascadeType.REMOVE, CascadeType.MERGE})
	private Set<EstudoCota> estudoCotas = new HashSet<EstudoCota>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public BigDecimal getQtdeReparte() {
		return qtdeReparte;
	}
	
	public void setQtdeReparte(BigDecimal qtdeReparte) {
		this.qtdeReparte = qtdeReparte;
	}
	
	public Date getDataLancamento() {
		return dataLancamento;
	}
	
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}
	
	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}
	
	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}
	
	public Lancamento getLancamento() {
		return lancamentos.isEmpty() ? null : lancamentos.iterator().next();
	}

	/**
	 * @return the estudoCotas
	 */
	public Set<EstudoCota> getEstudoCotas() {
		return estudoCotas;
	}

	/**
	 * @param estudoCotas the estudoCotas to set
	 */
	public void setEstudoCotas(Set<EstudoCota> estudoCotas) {
		this.estudoCotas = estudoCotas;
	}

}
