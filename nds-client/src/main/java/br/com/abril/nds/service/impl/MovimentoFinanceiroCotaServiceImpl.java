package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.DebitoCreditoDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.financeiro.BaixaCobranca;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class MovimentoFinanceiroCotaServiceImpl implements
		MovimentoFinanceiroCotaService {

	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	@Transactional
	public void gerarMovimentoFinanceiroDebitoCredito(
								Cota cota, GrupoMovimentoFinaceiro grupoMovimentoFinanceiro,
								Usuario usuario, BigDecimal valor, Date dataOperacao,
								BaixaCobranca baixaCobranca, Date dataNovoMovimento) {

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
			movimentoFinanceiroCota.setBaixaCobranca(baixaCobranca);

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
	@Transactional
	public void cadastrarMovimentoFincanceiroCota(DebitoCreditoDTO debitoCredito) {

		Long idMovimento = debitoCredito.getId();

		MovimentoFinanceiroCota movimentoFinanceiroCota = null;
		
		if (idMovimento != null) {
		
			movimentoFinanceiroCota = this.movimentoFinanceiroCotaRepository.buscarPorId(idMovimento);
		} 

		if (movimentoFinanceiroCota == null) {

			movimentoFinanceiroCota = new MovimentoFinanceiroCota();
		}

		if (debitoCredito.getDataLancamento() == null) {
			
			movimentoFinanceiroCota.setDataCriacao(DateUtil.removerTimestamp(new Date()));
		}

		if (debitoCredito.getDataVencimento() != null) {

			Date dataVencimento = DateUtil.parseDataPTBR(debitoCredito.getDataVencimento());
			
			movimentoFinanceiroCota.setData(dataVencimento);
		}

		try {

			BigDecimal valor = new BigDecimal(debitoCredito.getValor());

			movimentoFinanceiroCota.setValor(valor);

		} catch(NumberFormatException e) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Valor inválido para o campo [Valor].");
		}

		movimentoFinanceiroCota.setObservacao(debitoCredito.getObservacao());

		TipoMovimentoFinanceiro tipoMovimentoFinanceiro =
				this.tipoMovimentoFinanceiroRepository.buscarPorId(debitoCredito.getTipoMovimentoFinanceiro().getId());

		movimentoFinanceiroCota.setTipoMovimento(tipoMovimentoFinanceiro);
		
		Cota cota = this.cotaRepository.obterPorNumerDaCota(debitoCredito.getNumeroCota());
		
		movimentoFinanceiroCota.setCota(cota);

		Usuario usuario = this.usuarioRepository.buscarPorId(debitoCredito.getIdUsuario());
		
		movimentoFinanceiroCota.setUsuario(usuario);
		
		this.movimentoFinanceiroCotaRepository.merge(movimentoFinanceiroCota);
	}
}
