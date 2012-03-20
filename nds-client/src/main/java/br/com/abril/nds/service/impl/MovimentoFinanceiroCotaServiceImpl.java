package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.service.FeriadoService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;

@Service
public class MovimentoFinanceiroCotaServiceImpl implements
		MovimentoFinanceiroCotaService {

	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	@Autowired
	private FeriadoService feriadoService;
	
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
			movimentoFinanceiroCota.setDataInclusao(feriadoService.obterProximoDiaUtil(dataOperacao));
			movimentoFinanceiroCota.setUsuario(usuario);
			movimentoFinanceiroCota.setValor(valor);

			movimentoFinanceiroCotaRepository.adicionar(movimentoFinanceiroCota);
		}
	}

}
