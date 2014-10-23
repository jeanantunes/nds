package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.List;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroTipoDescontoDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Export(label="Tipo de Desconto", exhibitionOrder=1)
	private final String tipoDesconto = "Geral";

	private OrdenacaoColunaConsulta ordenacaoColuna;
	
	private List<Long> idFornecedores;
	
	private List<Long> idsEditores;
	
	public enum OrdenacaoColunaConsulta {
		
		SEQUENCIAL("sequencial"),
		DESCONTO("desconto"),
		FORNECEDORES("fornecedor"),
		DATA_ALTERACAO("dataAlteracao"),
		USUARIO("usuario"),
		TIPO_DESCONTO("descTipoDesconto");
		
		private String nomeColuna;
		
		private OrdenacaoColunaConsulta(String nomeColuna) {
			
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			
			return this.nomeColuna;
		}
	}
	
	/**
	 * Obtém idFornecedores
	 *
	 * @return List<Long>
	 */
	public List<Long> getIdFornecedores() {
		return idFornecedores;
	}

	/**
	 * Atribuí idFornecedores
	 * @param idFornecedores 
	 */
	public void setIdFornecedores(List<Long> idFornecedores) {
		this.idFornecedores = idFornecedores;
	}

	public List<Long> getIdsEditores() {
		return idsEditores;
	}

	public void setIdsEditores(List<Long> idsEditores) {
		this.idsEditores = idsEditores;
	}

	/**
	 * @return the tipoDesconto
	 */
	public String getTipoDesconto() {
		return tipoDesconto;
	}

	/**
	 * @return the ordenacaoColuna
	 */
	public OrdenacaoColunaConsulta getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	/**
	 * @param ordenacaoColuna the ordenacaoColuna to set
	 */
	public void setOrdenacaoColuna(OrdenacaoColunaConsulta ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((ordenacaoColuna == null) ? 0 : ordenacaoColuna.hashCode());
		result = prime * result
				+ ((tipoDesconto == null) ? 0 : tipoDesconto.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FiltroTipoDescontoDTO other = (FiltroTipoDescontoDTO) obj;
		if (ordenacaoColuna != other.ordenacaoColuna)
			return false;
		if (tipoDesconto == null) {
			if (other.tipoDesconto != null)
				return false;
		} else if (!tipoDesconto.equals(other.tipoDesconto))
			return false;
		return true;
	}

}