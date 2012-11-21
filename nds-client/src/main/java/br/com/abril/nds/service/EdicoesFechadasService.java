package br.com.abril.nds.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.client.vo.RegistroEdicoesFechadasVO;

/**
 * Classe de implementação de serviços referentes a entidade
 * @author infoA2
 */
public interface EdicoesFechadasService {
	
	public List<RegistroEdicoesFechadasVO> obterResultadoEdicoesFechadas(Date dataDe, Date dateAte, Long idFornecedor);
	public BigInteger obterTotalResultadoEdicoesFechadas(Date dataDe, Date dateAte, Long idFornecedor);
	public abstract List<RegistroEdicoesFechadasVO> obterResultadoEdicoesFechadas(Date dataDe,
			Date dateAte, Long idFornecedor, String sortorder, String sortname, Integer firstResult,
			Integer maxResults);
	public abstract Long quantidadeResultadoEdicoesFechadas(Date dataDe,
			Date dataAte, Long idFornecedor);

}
