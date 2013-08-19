package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.dto.filtro.FiltroFollowupChamadaoDTO.FollowupOrdenacaoChamadao;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroFollowupPendenciaNFeDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = -1309474395088652759L;	
	
	private String dataOperacao;
	
	private PaginacaoVO paginacao;
	
	private FollowupOrdenacaoPendenciaNFe followupOrdenacaoPendenciaNFe;
	
	public enum FollowupOrdenacaoPendenciaNFe {

		COTA("cota");
		
		private String nomeColuna;
		
		private FollowupOrdenacaoPendenciaNFe(String nomeColuna) {
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
	
	public FiltroFollowupPendenciaNFeDTO() {}	
	
	public FiltroFollowupPendenciaNFeDTO(Date dt) {
		setDataOperacao(DateUtil.formatarData(dt, Constantes.DATA_FMT_PESQUISA_MYSQL));
	}
	
	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}
	
	public FollowupOrdenacaoPendenciaNFe getFollowupOrdenacaoPendenciaNFe() {
		return followupOrdenacaoPendenciaNFe;
	}

	public void setFollowupOrdenacaoPendenciaNFe(
			FollowupOrdenacaoPendenciaNFe followupOrdenacaoPendenciaNFe) {
		this.followupOrdenacaoPendenciaNFe = followupOrdenacaoPendenciaNFe;
	}

	public String getDataOperacao() {
		return dataOperacao;
	}

	public void setDataOperacao(String dataOperacao) {
		this.dataOperacao = dataOperacao;
	}
}
