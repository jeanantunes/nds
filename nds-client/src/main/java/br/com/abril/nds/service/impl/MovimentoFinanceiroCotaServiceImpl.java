package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;

@Service
public class MovimentoFinanceiroCotaServiceImpl implements
		MovimentoFinanceiroCotaService {

	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Override
	@Transactional
	public void gerarMovimentoFinanceiroDebitoCredito(
								Cota cota, GrupoMovimentoFinaceiro grupoMovimentoFinanceiro,
								Usuario usuario, BigDecimal valor, Date dataOperacao) {

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
			movimentoFinanceiroCota.setData(calendarioService.obterProximoDiaUtil(dataOperacao));
			movimentoFinanceiroCota.setUsuario(usuario);
			movimentoFinanceiroCota.setValor(valor);
			movimentoFinanceiroCota.setLancamentoManual(false);

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
}
