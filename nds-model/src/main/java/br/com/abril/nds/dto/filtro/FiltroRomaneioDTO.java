package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO.ColunaOrdenacao;
import br.com.abril.nds.vo.PaginacaoVO;

public class FiltroRomaneioDTO implements Serializable {

	private static final long serialVersionUID = -3783996689743491442L;
	
	
	
	private Long idBox;
	private Long idRoteiro;
	private Long idRota;
	
	private PaginacaoVO paginacao;
	
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
		
		public static ColunaOrdenacao getPorDescricao(String descricao) {
			for(ColunaOrdenacao coluna: ColunaOrdenacao.values()) {
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
	
}
