package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.repository.DescontoLogisticaRepository;
import br.com.abril.nds.service.DescontoLogisticaService;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.DescontoLogistica}
 * 
 * @author Discover Technology
 */
@Service
public class DescontoLogisticaServiceImpl implements DescontoLogisticaService  {
	
	@Autowired
	private DescontoLogisticaRepository descontoLogisticaRepository;

	/**
	 * Obtem Desconto Logistica por tipoDesconto
	 * @param tipoDesconto
	 * @return DescontoLogistica
	 */
	@Transactional(readOnly=true)
	@Override
	public DescontoLogistica obterPorTipoDesconto(Integer tipoDesconto) {
		return descontoLogisticaRepository.obterPorTipoDesconto(tipoDesconto);
	}

	@Transactional(readOnly=true)
	@Override
	public List<DescontoLogistica> obterTodos() {
		return descontoLogisticaRepository.buscarTodos();
	}

}
