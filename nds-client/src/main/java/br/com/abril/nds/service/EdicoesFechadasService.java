package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.client.vo.RegistroEdicoesFechadasVO;

/**
 * Classe de implementação de serviços referentes a entidade
 * @author infoA2
 */
public interface EdicoesFechadasService {
	
	public List<RegistroEdicoesFechadasVO> obterResultadoEdicoesFechadas(Date dataDe, Date dateAte, String codigoFornecedor);
	public BigDecimal obterTotalResultadoEdicoesFechadas(Date dataDe, Date dateAte, String codigoFornecedor);

}
