package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroCadastroTipoNotaDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = -733782325727284635L;

	private OrdenacaoColunaConsulta ordenacaoColuna;
	
	private String cfop;
	
	private String tipoNota;
	
	public enum OrdenacaoColunaConsulta {
		
		NOP_DESCRICAO("nopDescricao"),
		CFOP_ESTADO("cfopEstado"),
		CFOP_OUTROS_ESTADOS("cfopOutrosEstados");
		
		private String nomeColuna;
		
		private OrdenacaoColunaConsulta(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
	}

	public OrdenacaoColunaConsulta getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	public void setOrdenacaoColuna(OrdenacaoColunaConsulta ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}

	public String getCfop() {
		return cfop;
	}

	public void setCfop(String cfop) {
		this.cfop = cfop;
	}

	public String getTipoNota() {
		return tipoNota;
	}

	public void setTipoNota(String tipoNota) {
		this.tipoNota = tipoNota;
	}

}
