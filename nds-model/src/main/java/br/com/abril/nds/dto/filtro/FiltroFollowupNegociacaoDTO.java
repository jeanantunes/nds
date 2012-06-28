package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;

public class FiltroFollowupNegociacaoDTO extends FiltroDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 571813987103374384L;

	public FiltroFollowupNegociacaoDTO() {}
	
	public FiltroFollowupNegociacaoDTO(Date dataOperacao) {
		setDataOperacao(DateUtil.formatarData(dataOperacao, Constantes.DATA_FMT_PESQUISA_MYSQL));
	}
	
	private String dataOperacao;

	public String getDataOperacao() {
		return dataOperacao;
	}

	public void setDataOperacao(String dataOperacao) {
		this.dataOperacao = dataOperacao;
	}
	
}
