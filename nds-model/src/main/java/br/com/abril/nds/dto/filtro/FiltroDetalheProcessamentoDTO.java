package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

public class FiltroDetalheProcessamentoDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = -3105477104866376867L;

	private OrdenacaoColunaConsulta ordenacaoColuna;
	
	public enum OrdenacaoColunaConsulta {
		
		TIPO_ERRO("tipoErro"),
		MENSAGEM("mensagem"),
		NUMERO_LINHA("numeroLinha");
		
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
