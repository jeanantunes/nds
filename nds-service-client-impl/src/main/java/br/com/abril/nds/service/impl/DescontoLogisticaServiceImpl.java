package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.model.cadastro.HistoricoDescontoLogistica;
import br.com.abril.nds.repository.DescontoLogisticaRepository;
import br.com.abril.nds.repository.HistoricoDescontoLogisticaRepository;
import br.com.abril.nds.service.DescontoLogisticaService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;

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
	
	@Autowired
	private HistoricoDescontoLogisticaRepository historicoDescontoLogisticaRepository;

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
	
	public void alterarDescontoLogistica(){
		
		Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		if(dataOperacao == null){
			dataOperacao = new Date();
		}
		
		List<HistoricoDescontoLogistica> historicoDescontoLogistica = 
				historicoDescontoLogisticaRepository.obterProximosDescontosVigente(DateUtil.adicionarDias(dataOperacao, 1)); 
		
		if(!historicoDescontoLogistica.isEmpty()){
			
			for(HistoricoDescontoLogistica item : historicoDescontoLogistica ){
			
				DescontoLogistica descontoLogistica = descontoLogisticaRepository.obterPorTipoDesconto(item.getTipoDesconto());
				
				if (descontoLogistica == null ) {
					descontoLogistica = new DescontoLogistica();
					descontoLogistica.setTipoDesconto(item.getTipoDesconto());	
				}
				
				descontoLogistica.setPercentualDesconto(item.getPercentualDesconto());
				descontoLogistica.setPercentualPrestacaoServico(item.getPercentualPrestacaoServico());
				descontoLogistica.setDataInicioVigencia(item.getDataInicioVigencia());
				
				descontoLogisticaRepository.merge(descontoLogistica);
				
				item.setDataProcessamento(dataOperacao);
				historicoDescontoLogisticaRepository.merge(item);
			}
		}
	}
}
