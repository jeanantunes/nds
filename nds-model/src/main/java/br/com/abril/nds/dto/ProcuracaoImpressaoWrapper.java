package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

public class ProcuracaoImpressaoWrapper implements Serializable {

	private static final long serialVersionUID = 8692064841351918124L;

	private List<ProcuracaoImpressaoDTO> listaProcuracaoImpressao;

	/**
	 * @return the listaProcuracaoImpressao
	 */
	public List<ProcuracaoImpressaoDTO> getListaProcuracaoImpressao() {
		return listaProcuracaoImpressao;
	}

	/**
	 * @param listaProcuracaoImpressao the listaProcuracaoImpressao to set
	 */
	public void setListaProcuracaoImpressao(
			List<ProcuracaoImpressaoDTO> listaProcuracaoImpressao) {
		this.listaProcuracaoImpressao = listaProcuracaoImpressao;
	}
}
