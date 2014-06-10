package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.repository.DescontoLogisticaRepository;
import br.com.abril.nds.service.DescontoLogisticaService;
import br.com.abril.nds.service.integracao.DistribuidorService;

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
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Transactional(readOnly=true)
	@Override
	public DescontoLogistica obterDescontoLogistica(Integer tipoDesconto, Long idFornecedor, Date dataVigencia, BigDecimal percentualDesconto) {
	    
		return descontoLogisticaRepository.obterDescontoLogistica(tipoDesconto, idFornecedor, dataVigencia, percentualDesconto);
	}
	
	@Transactional(readOnly=true)
    @Override
    public DescontoLogistica obterDescontoLogisticaVigente(Integer tipoDesconto, Long idFornecedor, Date dataVigencia) {
        
        return descontoLogisticaRepository.obterDescontoLogisticaVigente(tipoDesconto, idFornecedor, dataVigencia);
    }
	
	@Transactional(readOnly=true)
	@Override
	public List<DescontoLogistica> obterTodos() {
	    
		return descontoLogisticaRepository.buscarTodos();
	}
	
}
