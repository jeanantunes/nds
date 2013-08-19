package br.com.abril.nds.model.estoque;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalhePK;

@Entity
@Table(name = "FECHAMENTO_ENCALHE")
public class FechamentoEncalhe implements Serializable {

	private static final long serialVersionUID = 5694490390709384624L;

	@EmbeddedId
	private FechamentoEncalhePK fechamentoEncalhePK;
	
	@Column(name="QUANTIDADE", nullable=true)
	private Long quantidade;
	
	
	@OneToMany
	@JoinColumns({
        @JoinColumn(name="DATA_ENCALHE", referencedColumnName="DATA_ENCALHE"),
        @JoinColumn(name="PRODUTO_EDICAO_ID", referencedColumnName="PRODUTO_EDICAO_ID")
    })
	private List<FechamentoEncalheBox> listFechamentoEncalheBox;
	
	public FechamentoEncalhe() {
	}
	
	public FechamentoEncalhe(ProdutoEdicao produtoEdicao, Date data) {
	    FechamentoEncalhePK pk = new FechamentoEncalhePK();
	    pk.setDataEncalhe(data);
	    pk.setProdutoEdicao(produtoEdicao);
	    this.fechamentoEncalhePK = pk;
	}

	
	
	public FechamentoEncalhePK getFechamentoEncalhePK() {
		return fechamentoEncalhePK;
	}

	public void setFechamentoEncalhePK(FechamentoEncalhePK fechamentoEncalhePK) {
		this.fechamentoEncalhePK = fechamentoEncalhePK;
	}

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

	public List<FechamentoEncalheBox> getListFechamentoEncalheBox() {
		return listFechamentoEncalheBox;
	}

	public void setListFechamentoEncalheBox(List<FechamentoEncalheBox> listFechamentoEncalheBox) {
		this.listFechamentoEncalheBox = listFechamentoEncalheBox;
	}
}
