package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.dto.filtro.FiltroRomaneioDTO.ColunaOrdenacaoRomaneio;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroFechamentoCEIntegracaoDTO implements Serializable {

	private static final long serialVersionUID = 4133061212097872582L;
	
	private long idFornecedor;
	
	private long semana;
	
	private PaginacaoVO paginacao;
	
	private ColunaOrdenacaoRomaneio ordenacaoColuna;
	
	public enum ColunaOrdenacaoFechamentoCEIntegracao {

		COTA("cota");
		
		private String nomeColuna;
		
		private ColunaOrdenacaoFechamentoCEIntegracao(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
		
		public static ColunaOrdenacaoFechamentoCEIntegracao getPorDescricao(String descricao) {
			for(ColunaOrdenacaoFechamentoCEIntegracao coluna: ColunaOrdenacaoFechamentoCEIntegracao.values()) {
				if(coluna.toString().equals(descricao))
					return coluna;
			}
			return null;
		}
	}

	public long getIdFornecedor() {
		return idFornecedor;
	}

	public void setIdFornecedor(long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	public long getSemana() {
		return semana;
	}

	public void setSemana(long semana) {
		this.semana = semana;
	}

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

	public ColunaOrdenacaoRomaneio getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	public void setOrdenacaoColuna(ColunaOrdenacaoRomaneio ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}
	
	

}
