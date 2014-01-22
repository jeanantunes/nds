package br.com.abril.nds.model.distribuicao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
@Entity
@Table(name = "TIPO_SEGMENTO_PRODUTO")
@SequenceGenerator(name = "TIPO_SEGMENTO_PRODUTO_SEQ", initialValue = 1, allocationSize = 1)
public class TipoSegmentoProduto implements Serializable {

	private static final long serialVersionUID = 5272002928979766079L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "TIPO_SEGMENTO_PRODUTO_SEQ")
	private Long id;

	@Export(label = "Segmento", alignment=Alignment.LEFT, exhibitionOrder = 1)
	@Column(name = "DESCRICAO", nullable = false)
	private String descricao;
	
	@OneToMany(mappedBy = "tipoSegmentoProduto", fetch = FetchType.LAZY)
	protected List<Produto> produtos = new ArrayList<Produto>();
	
	public TipoSegmentoProduto() {
	}

	public TipoSegmentoProduto(Long id) {
	    this.id = id;
	}

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

	public static long getSerialversionuid() {
		return serialVersionUID;
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
		TipoSegmentoProduto other = (TipoSegmentoProduto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}
	
}
