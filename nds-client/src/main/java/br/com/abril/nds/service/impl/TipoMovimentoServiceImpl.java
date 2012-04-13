package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.repository.TipoMovimentoRepository;
import br.com.abril.nds.service.TipoMovimentoService;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.movimentacao.TipoMovimento}
 * 
 * @author Discover Technology
 */
@Service
public class TipoMovimentoServiceImpl implements TipoMovimentoService {

	@Autowired
	private TipoMovimentoRepository tipoMovimentoRepository;
	
	@Override
	@Transactional(readOnly = true)
	public List<TipoMovimento> obterTiposMovimento() {
		
		return tipoMovimentoRepository.buscarTodos();
	}
	
}
