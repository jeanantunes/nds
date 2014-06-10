package br.com.abril.nds.model.movimentacao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

@Entity
@Table(name = "CONTROLE_CONTAGEM_DEVOLUCAO", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"DATA", "PRODUTO_EDICAO_ID" })})
@SequenceGenerator(name="CTRL_CONT_DEVOLUCAO_SEQ", initialValue = 1, allocationSize = 1)
public class ControleContagemDevolucao {
	
	@Id
	@GeneratedValue(generator = "CTRL_CONT_DEVOLUCAO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA", nullable = false)
	private Date data;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private StatusOperacao status;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public StatusOperacao getStatus() {
		return status;
	}
	
	public void setStatus(StatusOperacao status) {
		this.status = status;
	}

	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

}
