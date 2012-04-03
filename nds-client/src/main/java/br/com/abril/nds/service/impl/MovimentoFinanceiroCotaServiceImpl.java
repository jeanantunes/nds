package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.financeiro.HistoricoMovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.repository.HistoricoMovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;

@Service
public class MovimentoFinanceiroCotaServiceImpl implements
		MovimentoFinanceiroCotaService {

	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;

	@Autowired
	private HistoricoMovimentoFinanceiroCotaRepository historicoMovimentoFinanceiroCotaRepository;

	@Override
	@Transactional
	public void gerarMovimentoFinanceiroDebitoCredito(
			MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO) {

		MovimentoFinanceiroCota movimentoFinanceiroCota = null;

		if (movimentoFinanceiroCotaDTO.getIdMovimentoFinanceiroCota() != null) {

			movimentoFinanceiroCota = this.movimentoFinanceiroCotaRepository.buscarPorId(
					movimentoFinanceiroCotaDTO.getIdMovimentoFinanceiroCota());
		
		} else {

			movimentoFinanceiroCota = new MovimentoFinanceiroCota();
		}

		TipoMovimentoFinanceiro tipoMovimentoFinanceiro =
					movimentoFinanceiroCotaDTO.getTipoMovimentoFinanceiro();

		if (tipoMovimentoFinanceiro != null) {

			if (tipoMovimentoFinanceiro.isAprovacaoAutomatica()) {

				movimentoFinanceiroCota.setAprovadoAutomaticamente(
						movimentoFinanceiroCotaDTO.isAprovacaoAutomatica());
				movimentoFinanceiroCota.setAprovador(
						movimentoFinanceiroCotaDTO.getUsuario());
				movimentoFinanceiroCota.setDataAprovacao(
						movimentoFinanceiroCotaDTO.getDataAprovacao());
				movimentoFinanceiroCota.setStatus(
						StatusAprovacao.APROVADO);

			} else {

				movimentoFinanceiroCota.setStatus(StatusAprovacao.PENDENTE);
			}

			movimentoFinanceiroCota.setCota(
					movimentoFinanceiroCotaDTO.getCota());
			movimentoFinanceiroCota.setTipoMovimento(
					tipoMovimentoFinanceiro);
			movimentoFinanceiroCota.setData(
					movimentoFinanceiroCotaDTO.getDataVencimento());
			movimentoFinanceiroCota.setDataCriacao(
					movimentoFinanceiroCotaDTO.getDataCriacao());
			movimentoFinanceiroCota.setUsuario(
					movimentoFinanceiroCotaDTO.getUsuario());
			movimentoFinanceiroCota.setValor(
					movimentoFinanceiroCotaDTO.getValor());
			movimentoFinanceiroCota.setLancamentoManual(
					movimentoFinanceiroCotaDTO.isLancamentoManual());
			movimentoFinanceiroCota.setBaixaCobranca(
					movimentoFinanceiroCotaDTO.getBaixaCobranca());
			movimentoFinanceiroCota.setObservacao(
					movimentoFinanceiroCotaDTO.getObservacao());
			
			MovimentoFinanceiroCota movimentoFinanceiroCotaMerged = 
					this.movimentoFinanceiroCotaRepository.merge(movimentoFinanceiroCota);

			gerarHistoricoMovimentoFinanceiroCota(movimentoFinanceiroCotaMerged, movimentoFinanceiroCotaDTO.getTipoEdicao());
		}
	}

	/**
	 * @see br.com.abril.nds.service.MovimentoFinanceiroCotaService#obterMovimentosFinanceiroCota()
	 */
	@Override
	@Transactional
	public List<MovimentoFinanceiroCota> obterMovimentosFinanceiroCota(
			FiltroDebitoCreditoDTO filtroDebitoCreditoDTO) {

		return this.movimentoFinanceiroCotaRepository.obterMovimentosFinanceiroCota(
					filtroDebitoCreditoDTO
				);
	}

	/**
	 * @see br.com.abril.nds.service.MovimentoFinanceiroCotaService#obterContagemMovimentosFinanceiroCota(br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO)
	 */
	@Override
	@Transactional
	public Integer obterContagemMovimentosFinanceiroCota(
			FiltroDebitoCreditoDTO filtroDebitoCreditoDTO) {

		return this.movimentoFinanceiroCotaRepository.obterContagemMovimentosFinanceiroCota(
					filtroDebitoCreditoDTO
				);
	}

	/**
	 * @see br.com.abril.nds.service.MovimentoFinanceiroCotaService#removerMovimentoFinanceiroCota(java.lang.Long)
	 */
	@Override
	@Transactional
	public void removerMovimentoFinanceiroCota(Long idMovimento) {
		
		MovimentoFinanceiroCota movimentoFinanceiroCota = this.movimentoFinanceiroCotaRepository.buscarPorId(idMovimento);
		
		this.movimentoFinanceiroCotaRepository.remover(movimentoFinanceiroCota);
	}

	/**
	 * @see br.com.abril.nds.service.MovimentoFinanceiroCotaService#obterMovimentoFinanceiroCotaPorId(java.lang.Long)
	 */
	@Override
	@Transactional
	public MovimentoFinanceiroCota obterMovimentoFinanceiroCotaPorId(Long idMovimento) {
		
		return this.movimentoFinanceiroCotaRepository.buscarPorId(idMovimento);
	}
	
	/**
	 * @see br.com.abril.nds.service.MovimentoFinanceiroCotaService#obterSomatorioValorMovimentosFinanceiroCota(br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO)
	 */
	@Override
	@Transactional
	public BigDecimal obterSomatorioValorMovimentosFinanceiroCota(
			FiltroDebitoCreditoDTO filtroDebitoCreditoDTO) {

		return this.movimentoFinanceiroCotaRepository.obterSomatorioValorMovimentosFinanceiroCota(filtroDebitoCreditoDTO);
	}

	private void gerarHistoricoMovimentoFinanceiroCota(MovimentoFinanceiroCota movimentoFinanceiroCota, TipoEdicao tipoEdicao) {
		
		HistoricoMovimentoFinanceiroCota historicoMovimentoFinanceiroCota = 
				new HistoricoMovimentoFinanceiroCota();

		historicoMovimentoFinanceiroCota.setCota(movimentoFinanceiroCota.getCota());
		historicoMovimentoFinanceiroCota.setResponsavel(movimentoFinanceiroCota.getUsuario());
		historicoMovimentoFinanceiroCota.setTipoEdicao(tipoEdicao);
		historicoMovimentoFinanceiroCota.setTipoMovimento(movimentoFinanceiroCota.getTipoMovimento());
		historicoMovimentoFinanceiroCota.setValor(movimentoFinanceiroCota.getValor());
		historicoMovimentoFinanceiroCota.setMovimentoFinanceiroCota(movimentoFinanceiroCota);
		historicoMovimentoFinanceiroCota.setDataEdicao(new Date());
		historicoMovimentoFinanceiroCota.setData(movimentoFinanceiroCota.getData());
		
		this.historicoMovimentoFinanceiroCotaRepository.adicionar(historicoMovimentoFinanceiroCota);
	}
}
