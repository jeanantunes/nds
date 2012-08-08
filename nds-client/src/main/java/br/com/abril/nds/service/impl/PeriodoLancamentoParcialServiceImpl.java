package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ParcialVendaDTO;
import br.com.abril.nds.dto.PeriodoParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroParciaisDTO;
import br.com.abril.nds.repository.PeriodoLancamentoParcialRepository;
import br.com.abril.nds.service.PeriodoLancamentoParcialService;

@Service
public class PeriodoLancamentoParcialServiceImpl implements PeriodoLancamentoParcialService{

	@Autowired
	private PeriodoLancamentoParcialRepository periodoLancamentoParcialRepository;

	@Transactional
	public List<PeriodoParcialDTO> obterPeriodosParciais(FiltroParciaisDTO filtro) {
		
		return periodoLancamentoParcialRepository.obterPeriodosParciais(filtro);
		
	}
	
	@Transactional
	public Integer totalObterPeriodosParciais(FiltroParciaisDTO filtro) {
		return periodoLancamentoParcialRepository.totalObterPeriodosParciais(filtro);
	}

	/**
	 * Obtem detalhes das vendas do produtoEdição nas datas de Lancamento e Recolhimento
	 * @param dataLancamento
	 * @param dataRecolhimento
	 * @param idProdutoRdicao
	 * @return List<ParcialVendaDTO>
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ParcialVendaDTO> obterDetalhesVenda(Date dataLancamento,
			Date dataRecolhimento, Long idProdutoEdicao) {
		return periodoLancamentoParcialRepository.obterDetalhesVenda(dataLancamento, dataRecolhimento, idProdutoEdicao);
	}

	
}
