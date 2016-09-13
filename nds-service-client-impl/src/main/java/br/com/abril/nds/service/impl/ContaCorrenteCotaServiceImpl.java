package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.MovimentoFinanceiroDTO;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.movimentacao.DebitoCreditoCota;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.service.ContaCorrenteCotaService;

@Service
public class ContaCorrenteCotaServiceImpl implements ContaCorrenteCotaService {

	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	@Override
	@Transactional(readOnly=true)
	public List<DebitoCreditoCota> consultarDebitoCreditoCota(Long idConsolidado, Date data,
			Integer numeroCota, String sortorder, String sortname){
		
		if (idConsolidado != null){
			
			data = null;
		}
		
		List<TipoMovimentoFinanceiro> tiposDebitoCredito = 
				this.tipoMovimentoFinanceiroRepository.buscarTiposMovimentoFinanceiro(
					Arrays.asList(
						GrupoMovimentoFinaceiro.CREDITO, 
						GrupoMovimentoFinaceiro.DEBITO,
						GrupoMovimentoFinaceiro.DEBITO_SOBRE_FATURAMENTO,
						GrupoMovimentoFinaceiro.POSTERGADO_NEGOCIACAO,
						GrupoMovimentoFinaceiro.VENDA_TOTAL,
						GrupoMovimentoFinaceiro.CREDITO_SOBRE_FATURAMENTO,
						GrupoMovimentoFinaceiro.COMPRA_NUMEROS_ATRAZADOS,
						GrupoMovimentoFinaceiro.LANCAMENTO_CAUCAO_LIQUIDA,
						GrupoMovimentoFinaceiro.RESGATE_CAUCAO_LIQUIDA,
						GrupoMovimentoFinaceiro.NEGOCIACAO_COMISSAO,
						GrupoMovimentoFinaceiro.DEBITO_COTA_TAXA_DE_ENTREGA_ENTREGADOR,
						GrupoMovimentoFinaceiro.DEBITO_COTA_TAXA_DE_ENTREGA_TRANSPORTADOR,
						GrupoMovimentoFinaceiro.TAXA_EXTRA
					)
				);

		return this.movimentoFinanceiroCotaRepository.obterCreditoDebitoCota(
				idConsolidado, data, numeroCota, tiposDebitoCredito, sortorder, sortname);
	}
	
	@Override
	@Transactional(readOnly = true)
	public BigDecimal consultarJurosCota(Long idConsolidado, Date data, Integer numeroCota){
		
		if (idConsolidado != null){
			
			data = null;
		}
		
		List<TipoMovimentoFinanceiro> tiposMovimento = Arrays.asList(
				this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
						GrupoMovimentoFinaceiro.JUROS)
		);
		
		return this.movimentoFinanceiroCotaRepository.
				obterSomatorioTipoMovimentoPorConsolidado(
						idConsolidado, data, numeroCota, tiposMovimento);
	}
	
	@Override
	@Transactional(readOnly = true)
	public BigDecimal consultarMultaCota(Long idConsolidado, Date data, Integer numeroCota){
		
		if (idConsolidado != null){
			
			data = null;
		}
		
		List<TipoMovimentoFinanceiro> tiposMovimento = Arrays.asList(
				this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
						GrupoMovimentoFinaceiro.MULTA)
		);
		
		return this.movimentoFinanceiroCotaRepository.
				obterSomatorioTipoMovimentoPorConsolidado(
						idConsolidado, data, numeroCota, tiposMovimento);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<MovimentoFinanceiroDTO> consultarValorVendaDia(
			Integer numeroCota, Long idConsolidado, Date data){
		
		if (idConsolidado != null){
			
			data = null;
		}
		
		List<Long> tiposMovimento = 
			this.tipoMovimentoFinanceiroRepository.buscarIdsTiposMovimentoFinanceiro(
				Arrays.asList(GrupoMovimentoFinaceiro.ENVIO_ENCALHE,
						GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE
						));

		return this.movimentoFinanceiroCotaRepository.obterDetalhesVendaDia(
				numeroCota, idConsolidado, tiposMovimento,	data);
	}
}