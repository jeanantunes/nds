package br.com.abril.nds.model.estoque.pk;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;

@Embeddable
public class FechamentoEncalheBoxPK implements Serializable {

	private static final long serialVersionUID = 7832563570603777070L;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_ENCALHE", nullable = false)
	private Date dataEncalhe;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "BOX_ID")
	private Box box;


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

	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((box == null) ? 0 : box.hashCode());
		result = prime * result
				+ ((dataEncalhe == null) ? 0 : dataEncalhe.hashCode());
		result = prime * result
				+ ((produtoEdicao == null) ? 0 : produtoEdicao.hashCode());
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
		FechamentoEncalheBoxPK other = (FechamentoEncalheBoxPK) obj;
		if (box == null) {
			if (other.box != null)
				return false;
		} else if (!box.equals(other.box))
			return false;
		if (dataEncalhe == null) {
			if (other.dataEncalhe != null)
				return false;
		} else if (!dataEncalhe.equals(other.dataEncalhe))
			return false;
		if (produtoEdicao == null) {
			if (other.produtoEdicao != null)
				return false;
		} else if (!produtoEdicao.equals(other.produtoEdicao))
			return false;
		return true;
	}
}
