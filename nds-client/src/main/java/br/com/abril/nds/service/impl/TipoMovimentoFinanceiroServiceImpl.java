package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.service.TipoMovimentoFinanceiroService;

@Service
public class TipoMovimentoFinanceiroServiceImpl implements TipoMovimentoFinanceiroService {

	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	@Override
	@Transactional
	public List<TipoMovimentoFinanceiro> obterTodosTiposMovimento() {

		return this.tipoMovimentoFinanceiroRepository.buscarTodos();
	}

}
