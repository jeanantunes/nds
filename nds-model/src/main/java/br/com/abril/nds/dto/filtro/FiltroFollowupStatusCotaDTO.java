package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.dto.filtro.FiltroFollowupChamadaoDTO.FollowupOrdenacaoChamadao;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroFollowupStatusCotaDTO  extends FiltroDTO implements Serializable  {

	
	private static final long serialVersionUID = -215785775108353228L;
	
	private PaginacaoVO paginacao;
	
	private FollowupOrdenacaoChamadao followupordenacaoColuna;
	
	public enum FollowupOrdenacaoStatusCota {

		COTA("cota");
		
		private String nomeColuna;
		
		private FollowupOrdenacaoStatusCota(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
		
		public static FollowupOrdenacaoChamadao getPorDescricao(String descricao) {
			for(FollowupOrdenacaoChamadao coluna: FollowupOrdenacaoChamadao.values()) {
				if(coluna.toString().equals(descricao))
					return coluna;
			}
			return null;
		}
	}

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

	public FollowupOrdenacaoChamadao getFollowupordenacaoColuna() {
		return followupordenacaoColuna;
	}

	public void setFollowupordenacaoColuna(
			FollowupOrdenacaoChamadao followupordenacaoColuna) {
		this.followupordenacaoColuna = followupordenacaoColuna;
	}
	
}
