package br.com.abril.nds.model.estoque.pk;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

@Embeddable
public class FechamentoEncalhePK implements Serializable {

	private static final long serialVersionUID = 3211046281342484465L;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_ENCALHE", nullable = false)
	private Date dataEncalhe;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;


	public Date getDataEncalhe() {
		return dataEncalhe;
	}

	public void setDataEncalhe(Date dataEncalhe) {
		this.dataEncalhe = dataEncalhe;
	}

	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}
}
