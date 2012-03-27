package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.DebitoCreditoDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.financeiro.BaixaAutomatica;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.util.DateUtil;

@Service
public class MovimentoFinanceiroCotaServiceImpl implements
		MovimentoFinanceiroCotaService {

	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Override
	@Transactional
	public void gerarMovimentoFinanceiroDebitoCredito(
								Cota cota, GrupoMovimentoFinaceiro grupoMovimentoFinanceiro,
								Usuario usuario, BigDecimal valor, Date dataOperacao,
								BaixaAutomatica baixaAutomatica, Date dataNovoMovimento) {

		TipoMovimentoFinanceiro tipoMovimentoFinanceiro =
			tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(grupoMovimentoFinanceiro);

		if (tipoMovimentoFinanceiro != null) {

			MovimentoFinanceiroCota movimentoFinanceiroCota = new MovimentoFinanceiroCota();

			if (tipoMovimentoFinanceiro.isAprovacaoAutomatica()) {

				movimentoFinanceiroCota.setAprovadoAutomaticamente(true);
				movimentoFinanceiroCota.setAprovador(usuario);
				movimentoFinanceiroCota.setDataAprovacao(new Date());
				
				movimentoFinanceiroCota.setStatus(StatusAprovacao.APROVADO);

			} else {

				movimentoFinanceiroCota.setStatus(StatusAprovacao.PENDENTE);
			}

			movimentoFinanceiroCota.setCota(cota);
			movimentoFinanceiroCota.setTipoMovimento(tipoMovimentoFinanceiro);
			movimentoFinanceiroCota.setData(dataNovoMovimento);
			movimentoFinanceiroCota.setUsuario(usuario);
			movimentoFinanceiroCota.setValor(valor);
			movimentoFinanceiroCota.setLancamentoManual(false);
			movimentoFinanceiroCota.setBaixaAutomatica(baixaAutomatica);

			movimentoFinanceiroCotaRepository.adicionar(movimentoFinanceiroCota);
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
	 * @see br.com.abril.nds.service.MovimentoFinanceiroCotaService#cadastrarMovimentoFincanceiroCota(br.com.abril.nds.dto.DebitoCreditoDTO)
	 */
	@Override
	public void cadastrarMovimentoFincanceiroCota(DebitoCreditoDTO debitoCredito) {

		Long idMovimento = debitoCredito.getId();

		MovimentoFinanceiroCota movimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.buscarPorId(idMovimento);

		if (movimentoFinanceiroCota != null) {

			movimentoFinanceiroCota.setDataCriacao(DateUtil.parseDataPTBR(debitoCredito.getDataLancamento()));
			movimentoFinanceiroCota.setData(DateUtil.parseDataPTBR(debitoCredito.getDataVencimento()));
			movimentoFinanceiroCota.setTipoMovimento(debitoCredito.getTipoMovimentoFinanceiro());
			movimentoFinanceiroCota.setObservacao(debitoCredito.getObservacao());
			movimentoFinanceiroCota.setValor(debitoCredito.getValor());
			
			Cota cota = this.cotaRepository.obterPorNumerDaCota(debitoCredito.getNumeroCota());
			
			movimentoFinanceiroCota.setCota(cota);

			this.movimentoFinanceiroCotaRepository.alterar(movimentoFinanceiroCota);
		}
	}
}
