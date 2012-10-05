package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.RegistroEdicoesFechadasVO;
import br.com.abril.nds.repository.EdicoesFechadasRepository;
import br.com.abril.nds.service.EdicoesFechadasService;

/**
 * Classe de implementação de serviços referentes a entidade
 * @author infoA2
 */
@Service
public class EdicoesFechadasServiceImpl implements EdicoesFechadasService {

	@Autowired
	private EdicoesFechadasRepository edicoesFechadasRepository;
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.EdicoesFechadasService#obterResultadoEdicoesFechadas(java.util.Date, java.util.Date, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<RegistroEdicoesFechadasVO> obterResultadoEdicoesFechadas(Date dataDe, Date dateAte, Long idFornecedor) {	
		
		return edicoesFechadasRepository.obterResultadoEdicoesFechadas(dataDe, dateAte, idFornecedor, null, null, null, null);
	}
	
	
	

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.EdicoesFechadasService#obterTotalResultadoEdicoesFechadas(java.util.Date, java.util.Date, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public BigDecimal obterTotalResultadoEdicoesFechadas(
			Date dataDe, Date dateAte, Long idFornecedor) {
		
		// Filtra por fornecedor específico
		return edicoesFechadasRepository.obterResultadoTotalEdicoesFechadas(dataDe, dateAte, idFornecedor);
	}
	
	
	




	/**
	 * @param dataDe
	 * @param dateAte
	 * @param idFornecedor
	 * @param sortorder
	 * @param sortname
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @see br.com.abril.nds.repository.EdicoesFechadasRepository#obterResultadoEdicoesFechadas(java.util.Date, java.util.Date, Long, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<RegistroEdicoesFechadasVO> obterResultadoEdicoesFechadas(
			Date dataDe, Date dateAte, Long idFornecedor,
			String sortorder, String sortname, Integer firstResult,
			Integer maxResults) {
		return edicoesFechadasRepository.obterResultadoEdicoesFechadas(dataDe,
				dateAte, idFornecedor, sortorder, sortname, firstResult,
				maxResults);
	}




	/**
	 * @param dataDe
	 * @param dataAte
	 * @param idFornecedor
	 * @return
	 * @see br.com.abril.nds.repository.EdicoesFechadasRepository#quantidadeResultadoEdicoesFechadas(java.util.Date, java.util.Date, java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Long quantidadeResultadoEdicoesFechadas(Date dataDe, Date dataAte,
			Long idFornecedor) {
		return edicoesFechadasRepository.quantidadeResultadoEdicoesFechadas(
				dataDe, dataAte, idFornecedor);
	}
	
}
