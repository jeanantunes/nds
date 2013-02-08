package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroInterfacesDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = 6820210049063162567L;
	
	private OrdenacaoColunaConsulta ordenacaoColuna;
	
	public enum OrdenacaoColunaConsulta {
		
		NOME("nome"),
		EXTENSAO_ARQUIVO("extensaoArquivo"),
		STATUS("status"),
		DATA_PROCESSAMENTO("dataProcessmento"),
		HORA_PROCESSAMENTO("horaProcessamento");
		
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
