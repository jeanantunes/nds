package br.com.abril.nds.service;

import java.util.Date;

import br.com.abril.nds.client.vo.ResultadoEdicoesFechadasVO;

public interface EdicoesFechadasService {

	public ResultadoEdicoesFechadasVO obterResultadoEdicoesFechadas(Date dataDe, Date dateAte, String codigoFornecedor);

}
