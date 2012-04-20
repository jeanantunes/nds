package br.com.abril.nds.model.cadastro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entidade para os tipos de estabelecimentos
 * que em que um PDV pode estar associado.
 * 
 * Indica os tipos de estabelecimentos em que um 
 * PDV pode estar "inserido" 
 * 
 * @author francisco.garcia
 *
 */
@Entity
@Table(name = "TIPO_ESTABELECIMENTO_ASSOCIACAO_PDV")
public class TipoEstabelecimentoAssociacaoPDV {
	
	@Id
	private Long id;
	
	/**
	 * Código interno do estabelecimento
	 */
	@Column(name = "CODIGO", nullable = false, unique = true)
	private Long codigo; 
	
	/**
	 * Descrição do tipo de estabelecimento
	 */
	@Column(name = "DESCRICAO", nullable = false)
	private String descricao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
		TipoEstabelecimentoAssociacaoPDV other = (TipoEstabelecimentoAssociacaoPDV) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}
