package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.util.List;

public class TotalizadorProdutoDistribuicaoVO  implements Serializable {

	private static final long serialVersionUID = 2186060384671120600L;
	
	private List<ProdutoDistribuicaoVO> listProdutoDistribuicao;

	private Integer totalEstudosLiberados;
	
	private Integer totalEstudosGerados;
	
	private boolean matrizFinalizada;
	
	public List<ProdutoDistribuicaoVO> getListProdutoDistribuicao() {
		return listProdutoDistribuicao;
	}

	public void setListProdutoDistribuicao(
			List<ProdutoDistribuicaoVO> listProdutoDistribuicao) {
		this.listProdutoDistribuicao = listProdutoDistribuicao;
	}

	public Integer getTotalEstudosLiberados() {
		return totalEstudosLiberados;
	}

	public void setTotalEstudosLiberados(Integer totalEstudosLiberados) {
		this.totalEstudosLiberados = totalEstudosLiberados;
	}

	public Integer getTotalEstudosGerados() {
		return totalEstudosGerados;
	}

	public void setTotalEstudosGerados(Integer totalEstudosGerados) {
		this.totalEstudosGerados = totalEstudosGerados;
	}

	public boolean isMatrizFinalizada() {
		return matrizFinalizada;
	}

	public void setMatrizFinalizada(boolean matrizFinalizada) {
		this.matrizFinalizada = matrizFinalizada;
	}
	
}
