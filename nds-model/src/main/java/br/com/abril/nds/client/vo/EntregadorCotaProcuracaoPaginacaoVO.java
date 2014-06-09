package br.com.abril.nds.client.vo;

import java.util.List;

public class EntregadorCotaProcuracaoPaginacaoVO {

	private List<EntregadorCotaProcuracaoVO> listaVO;
	
	private int totalRegistros;

	public List<EntregadorCotaProcuracaoVO> getListaVO() {
		return listaVO;
	}

	public void setListaVO(List<EntregadorCotaProcuracaoVO> listaVO) {
		this.listaVO = listaVO;
	}

	public int getTotalRegistros() {
		return totalRegistros;
	}

	public void setTotalRegistros(int totalRegistros) {
		this.totalRegistros = totalRegistros;
	}
}