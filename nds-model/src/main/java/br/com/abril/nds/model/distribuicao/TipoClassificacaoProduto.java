package br.com.abril.nds.model.distribuicao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TIPO_CLASSIFICACAO_PRODUTO")
@SequenceGenerator(name = "TIPO_CLASSIFICACAO_PRODUTO_SEQ", initialValue = 1, allocationSize = 1)
public class TipoClassificacaoProduto implements Serializable {
		
	private static final long serialVersionUID = 5272002928979766079L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "TIPO_CLASSIFICACAO_PRODUTO_SEQ")
	private Long id;

	@Column(name = "DESCRICAO", nullable = false)
	private String descricao;
	
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
		TipoClassificacaoProduto other = (TipoClassificacaoProduto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
