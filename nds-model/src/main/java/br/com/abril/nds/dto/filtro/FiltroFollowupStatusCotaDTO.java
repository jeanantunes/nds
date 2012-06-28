package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;

public class FiltroFollowupStatusCotaDTO  extends FiltroDTO implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -215785775108353228L;

	public FiltroFollowupStatusCotaDTO() {		
	}

	public FiltroFollowupStatusCotaDTO(Date dt) {		
		setDataOperacao(DateUtil.formatarData(dt, Constantes.DATA_FMT_PESQUISA_MYSQL));
	}

	private String dataOperacao;

	public String getDataOperacao() {
		return dataOperacao;
	}

	public void setDataOperacao(String dataOperacao) {
		this.dataOperacao = dataOperacao;
	}


}
