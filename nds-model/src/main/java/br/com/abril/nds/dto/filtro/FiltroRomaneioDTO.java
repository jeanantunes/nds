package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroRomaneioDTO implements Serializable {

	private static final long serialVersionUID = -3783996689743491442L;
	
	private Long idBox;
	private Long idRoteiro;
	private Long idRota;
	private String nomeRota;	
	
	private PaginacaoVO paginacao;
	
	private ColunaOrdenacaoRomaneio ordenacaoColuna;
	
	public enum ColunaOrdenacaoRomaneio {

		COTA("cota");
		
		private String nomeColuna;
		
		private ColunaOrdenacaoRomaneio(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
		
		public static ColunaOrdenacaoRomaneio getPorDescricao(String descricao) {
			for(ColunaOrdenacaoRomaneio coluna: ColunaOrdenacaoRomaneio.values()) {
				if(coluna.toString().equals(descricao))
					return coluna;
			}
			return null;
		}
	}

	public Long getIdBox() {
		return idBox;
	}

	public void setIdBox(Long idBox) {
		this.idBox = idBox;
	}

	public Long getIdRoteiro() {
		return idRoteiro;
	}

	public void setIdRoteiro(Long idRoteiro) {
		this.idRoteiro = idRoteiro;
	}

	public Long getIdRota() {
		return idRota;
	}

	public void setIdRota(Long idRota) {
		this.idRota = idRota;
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
	
	@Export(label = "Roteiro" , exhibitionOrder = 1, alignment = Alignment.LEFT)
	public String getRoteiro() {
		if(getIdRoteiro() != null){
			if(getIdRoteiro() == -1){
				return "Todos";
		}
			return   "("+ getIdRoteiro() + ")";
			
		}else{
			return "-";
		}
		
	}
	
	@Export(label = "Entrega / Box" , exhibitionOrder = 2, alignment = Alignment.LEFT)
	public String getBox() {
		if(getIdBox() != null){
			if(getIdBox() == -1){
				return "Todos";
			}			
			return getIdBox().toString();
		}else{
			return "-";
		}
	}

	@Export(label = "Rota" , exhibitionOrder = 3, alignment = Alignment.LEFT)
	public String getRota() {
		if(getIdRota() != null){
			if(getIdRota() == -1){
				return "Todos";
			}			
			return "(" + getIdRota() + ") " + getNomeRota();		
		}else{
			return "-";
		}
		
	}
	
	@Export(label = "Data Geração" , exhibitionOrder = 4, alignment = Alignment.LEFT)
	public String getDataAtual(){
		Date dataAtual = new Date();		
		return DateUtil.formatarData(dataAtual, Constantes.DATE_PATTERN_PT_BR);
	}

	public String getNomeRota() {
		return nomeRota;
	}

	public void setNomeRota(String nomeRota) {
		this.nomeRota = nomeRota;
	}

}
