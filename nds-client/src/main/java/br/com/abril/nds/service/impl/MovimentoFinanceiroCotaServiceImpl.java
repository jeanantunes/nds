package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaFaturamentoDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.BaseCalculo;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
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
	public MovimentoFinanceiroCota gerarMovimentoFinanceiroDebitoCredito(
										MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO) {
		
		//TODO: REMOVER APOS REFACTOR
		
		return null;
	}
	
	@Override
	@Transactional
	public List<MovimentoFinanceiroCota> gerarMovimentosFinanceirosDebitoCredito(
			MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO) {

		Map<Long, List<MovimentoEstoqueCota>> mapaMovimentoEstoqueCotaPorFornecedor = 
			this.gerarMapaMovimentoEstoqueCotaPorFornecedor(movimentoFinanceiroCotaDTO.getMovimentos());
		
		List<MovimentoFinanceiroCota> movimentosFinanceirosCota = new ArrayList<MovimentoFinanceiroCota>();
		
		if (mapaMovimentoEstoqueCotaPorFornecedor.isEmpty()) {
			
			movimentosFinanceirosCota.add(
				gerarMovimentoFinanceiroCota(movimentoFinanceiroCotaDTO, null));
			
		} else {
		
			for (Map.Entry<Long, List<MovimentoEstoqueCota>> entry : mapaMovimentoEstoqueCotaPorFornecedor.entrySet()) {
				
				movimentosFinanceirosCota.add(
					gerarMovimentoFinanceiroCota(movimentoFinanceiroCotaDTO, entry.getValue()));
			}
		}
		
		return movimentosFinanceirosCota;
	}

	/*
	 * Gera o movimento financeiro da cota.
	 */
	private MovimentoFinanceiroCota gerarMovimentoFinanceiroCota(MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO,
										  					 	 List<MovimentoEstoqueCota> movimentosEstoqueCota) {
		
		MovimentoFinanceiroCota movimentoFinanceiroCota = null;
		MovimentoFinanceiroCota movimentoFinanceiroCotaMerged = null;
		
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

				movimentoFinanceiroCota.setAprovadoAutomaticamente(Boolean.TRUE);
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
			
			movimentoFinanceiroCota.setMovimentos(movimentosEstoqueCota);
			
			movimentoFinanceiroCotaMerged = 
					this.movimentoFinanceiroCotaRepository.merge(movimentoFinanceiroCota);

			gerarHistoricoMovimentoFinanceiroCota(movimentoFinanceiroCotaMerged, movimentoFinanceiroCotaDTO.getTipoEdicao());

		}
		
		return movimentoFinanceiroCotaMerged;
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
		historicoMovimentoFinanceiroCota.setTipoMovimento((TipoMovimentoFinanceiro) movimentoFinanceiroCota.getTipoMovimento());
		historicoMovimentoFinanceiroCota.setValor(movimentoFinanceiroCota.getValor());
		historicoMovimentoFinanceiroCota.setMovimentoFinanceiroCota(movimentoFinanceiroCota);
		historicoMovimentoFinanceiroCota.setDataEdicao(new Date());
		historicoMovimentoFinanceiroCota.setData(movimentoFinanceiroCota.getData());
		
		this.historicoMovimentoFinanceiroCotaRepository.adicionar(historicoMovimentoFinanceiroCota);
	}

    
	/**
	 * Obtém valores dos faturamentos bruto ou liquido das cotas no período
	 * @param cotas
	 * @param baseCalculo
	 * @param dataInicial
	 * @param dataFinal
	 * @return Map<Long,BigDecimal>: Faturamentos das cotas
	 */
	@Override
	@Transactional(readOnly=true)
	public Map<Long,BigDecimal> obterFaturamentoCotasPeriodo(List<Cota> cotas, BaseCalculo baseCalculo, Date dataInicial, Date dataFinal)
    {
		
		Map<Long,BigDecimal> res = null;
		
		List<CotaFaturamentoDTO> cotasFaturamento = this.movimentoFinanceiroCotaRepository.obterFaturamentoCotasPorPeriodo(cotas, dataInicial, dataFinal);

		if(cotasFaturamento!=null && cotasFaturamento.size()>0){
			res = new HashMap<Long,BigDecimal>();
			for(CotaFaturamentoDTO item:cotasFaturamento){
				if (baseCalculo == BaseCalculo.FATURAMENTO_BRUTO){
	        	    res.put(item.getIdCota(), item.getFaturamentoBruto());
				}
				if (baseCalculo == BaseCalculo.FATURAMENTO_LIQUIDO){
	        	    res.put(item.getIdCota(), item.getFaturamentoLiquido()); 
				}
	        }
			
	    }
				
		return res;
	}
	
	/*
	 * Gera um mapa de movimentos de estoque da cota por fornecedor. 
	 */
	private Map<Long, List<MovimentoEstoqueCota>> gerarMapaMovimentoEstoqueCotaPorFornecedor(
																List<MovimentoEstoqueCota> movimentosEstoqueCota) {
		
		Map<Long, List<MovimentoEstoqueCota>> mapaMovimentoEstoqueCotaPorFornecedor = 
			new HashMap<Long, List<MovimentoEstoqueCota>>();
		
		if (movimentosEstoqueCota == null
				|| movimentosEstoqueCota.isEmpty()) {
			
			return mapaMovimentoEstoqueCotaPorFornecedor;
		}
		
		for (MovimentoEstoqueCota movimentoEstoqueCota : movimentosEstoqueCota) {
			
			Fornecedor fornecedor =
				movimentoEstoqueCota.getProdutoEdicao().getProduto().getFornecedor();
			
			if (fornecedor != null) {
				
				List<MovimentoEstoqueCota> movimentosEstoqueCotaFornecedor = 
					mapaMovimentoEstoqueCotaPorFornecedor.get(fornecedor.getId());
				
				if (movimentosEstoqueCotaFornecedor == null) {
				
					movimentosEstoqueCotaFornecedor = new ArrayList<MovimentoEstoqueCota>();
				}
				
				movimentosEstoqueCotaFornecedor.add(movimentoEstoqueCota);
				
				mapaMovimentoEstoqueCotaPorFornecedor.put(fornecedor.getId(), movimentosEstoqueCotaFornecedor);
			}
		}
		
		return mapaMovimentoEstoqueCotaPorFornecedor;
	}
	
}
