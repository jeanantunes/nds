package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroNaturezaOperacaoDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = -733782325727284635L;
	
	
	private TipoAtividade tipoAtividade;
	
	@Export(label="Tipo Nota")
	private String tipoNota;

	private OrdenacaoColunaConsulta ordenacaoColuna;
	
	public FiltroNaturezaOperacaoDTO() {
	}
	
	public enum OrdenacaoColunaConsulta {
		
		OPERACAO("operacao"),
		PROCESSO("processo"),
		DESCRICAO("descricao"),
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
	
	public String getTipoNota() {
		return tipoNota;
	}

	public void setTipoNota(String tipoNota) {
		this.tipoNota = tipoNota;
	}

	/**
	 * @return the tipoAtividade
	 */
	public TipoAtividade getTipoAtividade() {
		return tipoAtividade;
	}

	/**
	 * @param tipoAtividade the tipoAtividade to set
	 */
	public void setTipoAtividade(TipoAtividade tipoAtividade) {
		this.tipoAtividade = tipoAtividade;
	}
	
	@Export(label="Operação")
	public String getOperacao(){
		if(tipoAtividade == null){
			return "Todos";
		}else{
			return tipoAtividade.getDescricao();
		}
	}
	
	

}
