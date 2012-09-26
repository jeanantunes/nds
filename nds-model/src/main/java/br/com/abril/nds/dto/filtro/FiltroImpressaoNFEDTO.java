package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroImpressaoNFEDTO implements Serializable {

	private static final long serialVersionUID = -750037999779899243L;
	
	private Long idRoteiro;
	
	private PaginacaoVO paginacao;
	
	private ColunaOrdenacaoImpressaoNFE ordenacaoColuna;
	
	public enum ColunaOrdenacaoImpressaoNFE {

		COTA("cota");
		
		private String nomeColuna;
		
		private ColunaOrdenacaoImpressaoNFE(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
		
		public static ColunaOrdenacaoImpressaoNFE getPorDescricao(String descricao) {
			for(ColunaOrdenacaoImpressaoNFE coluna: ColunaOrdenacaoImpressaoNFE.values()) {
				if(coluna.toString().equals(descricao))
					return coluna;
			}
			return null;
		}
	}

	public Long getIdRoteiro() {
		return idRoteiro;
	}

	public void setIdRoteiro(Long idRoteiro) {
		this.idRoteiro = idRoteiro;
	}

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

	public ColunaOrdenacaoImpressaoNFE getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	public void setOrdenacaoColuna(ColunaOrdenacaoImpressaoNFE ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}
	
}
