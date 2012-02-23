package br.com.abril.nds.model.planejamento;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
@Entity
@Table(name = "ESTUDO")
@SequenceGenerator(name="ESTUDO_SEQ", initialValue = 1, allocationSize = 1)
public class Estudo {

	@Id
	@GeneratedValue(generator = "ESTUDO_SEQ")
	private Long id;
	private double qtdeReparte;
	private Date data;
	@OneToOne
	private Lancamento lancamento;
	@ManyToOne
	private ProdutoEdicao produtoEdicao;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public double getQtdeReparte() {
		return qtdeReparte;
	}
	
	public void setQtdeReparte(double qtdeReparte) {
		this.qtdeReparte = qtdeReparte;
	}
	
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public Lancamento getLancamento() {
		return lancamento;
	}
	
	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}
	
	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}
	
	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

}