package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

public class FiltroDetalheInterfaceDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = 4614430590776846020L;

	private OrdenacaoColunaConsulta ordenacaoColuna;
	
	public enum OrdenacaoColunaConsulta {
		
		ACAO("acao"),
		LOCAL("local"),
		DETALHE("detalhe");
		
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
	
}
