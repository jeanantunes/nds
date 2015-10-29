package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.TipoMovimentoFinanceiroService;

@Service
public class TipoMovimentoFinanceiroServiceImpl implements TipoMovimentoFinanceiroService {

	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
	
	@Override
	@Transactional
	public List<TipoMovimentoFinanceiro> obterTipoMovimentosFinanceirosCombo(boolean flag) {
		
		List <GrupoMovimentoFinaceiro>  grupoMovimentoFinaceiro = this.movimentoFinanceiroCotaService.getGrupoMovimentosFinanceirosDebitosCreditos();
		
		if ( flag )
			grupoMovimentoFinaceiro.add(GrupoMovimentoFinaceiro.POSTERGADO_NEGOCIACAO);
		return this.tipoMovimentoFinanceiroRepository.buscarTiposMovimentoFinanceiro(
				grupoMovimentoFinaceiro);
		
	}


	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.TipoMovimentoFinanceiroService#obterTipoMovimentoFincanceiroPorId(java.lang.Long)
	 */
	@Override
	@Transactional
	public TipoMovimentoFinanceiro obterTipoMovimentoFincanceiroPorId(
			Long idTipoMovimentoFinanceiro) {
		return this.tipoMovimentoFinanceiroRepository.buscarPorId(idTipoMovimentoFinanceiro);
	}

	/**
	 * Obtem tipo de movimento financeiro por GrupoMovimentoFinanceiro e OperacaoFinanceira
	 * @param grupo
	 * @param operacao
	 * @return TipoMovimentoFinanceiro
	 */
	@Override
	@Transactional(readOnly = true)
	public TipoMovimentoFinanceiro obterTipoMovimentoFincanceiroPorGrupoFinanceiroEOperacaoFinanceira(
			GrupoMovimentoFinaceiro grupo, OperacaoFinaceira operacao) {
		
		return this.tipoMovimentoFinanceiroRepository.obterTipoMovimentoFincanceiroPorGrupoFinanceiroEOperacaoFinanceira(grupo,operacao);
	}
	
	@Override
	@Transactional(readOnly=true)
	public TipoMovimentoFinanceiro buscarPorGrupoMovimento(GrupoMovimentoFinaceiro grupoMovimento) {
		
		return tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(grupoMovimento);
	}
}