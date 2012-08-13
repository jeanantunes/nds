package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ExpedicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroResumoExpedicaoDTO;
import br.com.abril.nds.repository.ExpedicaoRepository;
import br.com.abril.nds.service.ExpedicaoService;

/**
 * Classe responsável por implementar as funcionalidades referente a expedição de lançamentos de produtos.
 * 
 * @author Discover Technology
 *
 */

@Service
public class ExpedicaoServiceImpl implements ExpedicaoService {
	
	@Autowired
	private ExpedicaoRepository expedicaoRepository;
	
	@Transactional(readOnly = true)
	@Override
	public List<ExpedicaoDTO> obterResumoExpedicaoPorProduto(FiltroResumoExpedicaoDTO filtro) {
		
		return expedicaoRepository.obterResumoExpedicaoPorProduto(filtro);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<ExpedicaoDTO> obterResumoExpedicaoPorBox(FiltroResumoExpedicaoDTO filtro) {
		
		return expedicaoRepository.obterResumoExpedicaoPorBox(filtro);
	}

	
	@Transactional(readOnly = true)
	@Override
	public List<ExpedicaoDTO> obterResumoExpedicaoProdutosDoBox(FiltroResumoExpedicaoDTO filtro) {
		
		return expedicaoRepository.obterResumoExpedicaoProdutosDoBox(filtro);
		
	}

	@Transactional(readOnly = true)
	@Override
	public Long obterQuantidadeResumoExpedicaoProdutosDoBox(FiltroResumoExpedicaoDTO filtro) {
		
		return expedicaoRepository.obterQuantidadeResumoExpedicaoProdutosDoBox(filtro);
		
	}
	
	
	@Transactional(readOnly = true)
	@Override
	public Long obterQuantidadeResumoExpedicaoPorProduto(FiltroResumoExpedicaoDTO filtro) {
		
		return expedicaoRepository.obterQuantidadeResumoExpedicaoPorProduto(filtro);
	}

	@Override
	@Transactional(readOnly = true)
	public Date obterDataUltimaExpedicaoDia(Date dataOperacao) {
		return expedicaoRepository.obterUltimaExpedicaoDia(dataOperacao);
	}

	@Override
	@Transactional(readOnly = true)
	public Date obterDataUltimaExpedicao() {
		return expedicaoRepository.obterDataUltimaExpedicao();
	}

}
