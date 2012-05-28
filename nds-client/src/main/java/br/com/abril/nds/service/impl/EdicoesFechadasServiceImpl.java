package br.com.abril.nds.service.impl;

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
	public List<RegistroEdicoesFechadasVO> obterResultadoEdicoesFechadas(Date dataDe, Date dateAte, String codigoFornecedor) {
		
		if (codigoFornecedor == null || codigoFornecedor.isEmpty() || codigoFornecedor.equalsIgnoreCase("Todos")) {
			// Retorna todos os fornecedores
			return edicoesFechadasRepository.obterResultadoEdicoesFechadas(dataDe, dateAte);
		}
		// Filtra por fornecedor específico
		return edicoesFechadasRepository.obterResultadoEdicoesFechadas(dataDe, dateAte, codigoFornecedor);
	}
	
}
