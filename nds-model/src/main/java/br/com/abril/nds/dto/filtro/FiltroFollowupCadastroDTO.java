package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroFollowupCadastroDTO  extends FiltroDTO implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7542916887411093736L;

	public FiltroFollowupCadastroDTO() {}
	
	public FiltroFollowupCadastroDTO(Date dt, String paramsd) {
		setDataOperacao(DateUtil.formatarData(dt, Constantes.DATA_FMT_PESQUISA_MYSQL));
		setParametrosDiversos(paramsd);
	}
	
	private String dataOperacao;
	private String parametrosDiversos; 

	public String getDataOperacao() {
		return dataOperacao;
	}
	public void setDataOperacao(String dataOperacao) {
		this.dataOperacao = dataOperacao;
	}
	public String getParametrosDiversos() {
		return parametrosDiversos;
	}
	public void setParametrosDiversos(String parametrosDiversos) {
		this.parametrosDiversos = parametrosDiversos;
	}

}
