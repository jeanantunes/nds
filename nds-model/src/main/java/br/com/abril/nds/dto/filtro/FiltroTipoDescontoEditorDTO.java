package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroTipoDescontoEditorDTO extends FiltroDTO implements Serializable {
	
	private static final long serialVersionUID = 2281254940257591061L;
	
	@Export(label="Tipo de Desconto", exhibitionOrder=1)
	private final String tipoDesconto = "Específico";

	@Export(label="Editor" , exhibitionOrder=2)
	private Long codigoEditor;
	
	@Export(label="Nome" , exhibitionOrder=3)
	private String nomeEditor;
	
	private Long idEditor;
		
	private OrdenacaoColunaConsulta ordenacaoColuna;
	
	public enum OrdenacaoColunaConsulta {
		
		CODIGO_EDITOR("codigoEditor"),
		NOME_EDITOR("nomeCota"),
		DESCONTO("desconto"),
		FORNECEDORES("fornecedor"),
		DATA_ALTERACAO("dataAlteracao"),
		USUARIO("nomeUsuario"),
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
	 * @return the tipoDesconto
	 */
	public String getTipoDesconto() {
		return tipoDesconto;
	}

	public Long getCodigoEditor() {
		return codigoEditor;
	}

	public void setCodigoEditor(Long codigoEditor) {
		this.codigoEditor = codigoEditor;
	}

	public String getNomeEditor() {
		return nomeEditor;
	}

	public void setNomeEditor(String nomeEditor) {
		this.nomeEditor = nomeEditor;
	}

	public Long getIdEditor() {
		return idEditor;
	}

	public void setIdEditor(Long idEditor) {
		this.idEditor = idEditor;
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
				+ ((nomeEditor == null) ? 0 : nomeEditor.hashCode());
		result = prime * result
				+ ((codigoEditor == null) ? 0 : codigoEditor.hashCode());
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
		FiltroTipoDescontoEditorDTO other = (FiltroTipoDescontoEditorDTO) obj;
		if (nomeEditor == null) {
			if (other.nomeEditor != null)
				return false;
		} else if (!nomeEditor.equals(other.nomeEditor))
			return false;
		if (codigoEditor == null) {
			if (other.codigoEditor != null)
				return false;
		} else if (!codigoEditor.equals(other.codigoEditor))
			return false;
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