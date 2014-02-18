package br.com.abril.nds.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.HistoricoDescontoLogistica;
import br.com.abril.nds.repository.HistoricoDescontoLogisticaRepository;
import br.com.abril.nds.service.HistoricoDescontoLogisticaService;

@Service
public class HistoricoDescontoLogisticaServiceImpl implements HistoricoDescontoLogisticaService {
	
	@Autowired
	private HistoricoDescontoLogisticaRepository historicoDescontoLogisticaRepository;
	
	@Transactional
	public HistoricoDescontoLogistica obterDesconto(Integer tipoDesconto, Date dataInicioVigencia){
		
		return historicoDescontoLogisticaRepository.obterHistoricoDesconto(tipoDesconto, dataInicioVigencia);
	}
}
