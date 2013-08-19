package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


/**
 * Entidade base com informações de código e descrição, 
 * facilitando o mapeamento de entidades que são informações
 * populados com informações oriundas de sistemas legados
 * 
 * @author francisco.garcia
 *
 */
@MappedSuperclass
public abstract class CodigoDescricao implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Identificação de informação legada
	 */
	@Column(name = "CODIGO", nullable = false)
	private Long codigo;
	
	/**
	 * Descrição da informação
	 */
	@Column(name = "DESCRICAO", nullable = false)
	private String descricao;

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
		CodigoDescricao other = (CodigoDescricao) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}
