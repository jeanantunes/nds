package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroInterfacesDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = 6820210049063162567L;
	
	private OrdenacaoColunaConsulta ordenacaoColuna;
	
	public enum OrdenacaoColunaConsulta {
		
		CODIGO_PRODUTO("idLogProcessamento"),
		NOME_PRODUTO("nome"),
		EDICAO_PRODUTO("extensaoArquivo"),
		NOME_FORNECEDOR("status"),
		DATA_LANCAMENTO("dataProcessmento"),
		DATA_RECOLHIMENTO("horaProcessamento");
		
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
