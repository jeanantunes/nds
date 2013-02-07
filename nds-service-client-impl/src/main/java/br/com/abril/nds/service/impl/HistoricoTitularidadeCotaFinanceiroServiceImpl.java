package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFinanceiro;
import br.com.abril.nds.repository.HistoricoTitularidadeCotaFinanceiroRepository;
import br.com.abril.nds.service.HistoricoTitularidadeCotaFinanceiroService;

@Service
public class HistoricoTitularidadeCotaFinanceiroServiceImpl implements HistoricoTitularidadeCotaFinanceiroService {

	@Autowired
	private HistoricoTitularidadeCotaFinanceiroRepository historicoTitularidadeCotaFinanceiroRepository;
	

	@Override
	@Transactional(readOnly = true)
	public List<HistoricoTitularidadeCotaFinanceiro> pesquisarTodos(){
		return this.historicoTitularidadeCotaFinanceiroRepository.pesquisarTodos();
	}

	
}