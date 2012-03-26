package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.ControleBaixaBancaria;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.repository.ControleBaixaBancariaRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class GerarCobrancaServiceImpl implements GerarCobrancaService {

	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	@Autowired
	private ControleConferenciaEncalheRepository controleConferenciaEncalheRepository;
	
	@Autowired
	private ConsolidadoFinanceiroRepository consolidadoFinanceiroRepository;
	
	@Autowired
	private PoliticaCobrancaRepository politicaCobrancaRepository;
	
	@Autowired
	private ControleBaixaBancariaRepository controleBaixaBancariaRepository;
	
	@Override
	@Transactional
	public void gerarCobranca(Long idCota) {
		
		// verificar se a operação de conferencia ja foi concluida
		StatusOperacao statusOperacao = this.controleConferenciaEncalheRepository.obterStatusConferenciaDataOperacao();
		
		if (statusOperacao == null || !StatusOperacao.CONCLUIDO.equals(statusOperacao)){
			throw new ValidacaoException(TipoMensagem.ERROR, "A conferência de box de encalhe deve ser concluída antes de gerar dívidas.");
		}
		
		//Caso esteja gerando cobrança para uma única cota
		if (idCota != null){
			boolean existeCobranca = 
					this.consolidadoFinanceiroRepository.verificarConsodidadoCotaPorData(idCota, new Date());
			
			if (existeCobranca){
				throw new ValidacaoException(TipoMensagem.WARNING, "Já foi gerada cobrança para esta cota na data de hoje.");
			}
		}
		
		//Buscar politica de cobrança e forma de cobrança do distribuidor
		PoliticaCobranca politicaCobranca = this.politicaCobrancaRepository.buscarPoliticaCobrancaPorDistribuidor();
		if (politicaCobranca == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Politica de cobrança não encontrada.");
		} else if (politicaCobranca.getFormaCobranca() == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Forma de cobrança não encontrada.");
		}
		
		//Caso o principal modo de cobrança seja boleto a baixa automática deve ter sido executada
		if (TipoCobranca.BOLETO.equals(politicaCobranca.getFormaCobranca().getTipoCobranca())){
			ControleBaixaBancaria controleBaixaBancaria = this.controleBaixaBancariaRepository.obterPorData(new Date());
			
			if (!StatusControle.CONCLUIDO_SUCESSO.equals(controleBaixaBancaria.getStatus())){
				throw new ValidacaoException(TipoMensagem.ERROR, "Baixa Automática ainda não executada.");
			}
		}
		
		// buscar movimentos financeiros da cota para a data de operação em andamento
		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterMovimentoFinanceiroCotaDataOperacao(idCota, new Date());
		
		if (listaMovimentoFinanceiroCota != null &&
				!listaMovimentoFinanceiroCota.isEmpty()){
			
			//Varre todos os movimentos encontrados, agrupando por cota
			Cota ultimaCota = listaMovimentoFinanceiroCota.get(0).getCota();
			BigDecimal valorMovimentoFinanceiro = BigDecimal.ZERO;
			List<MovimentoFinanceiroCota> movimentos = new ArrayList<MovimentoFinanceiroCota>();
			for (MovimentoFinanceiroCota movimentoFinanceiroCota : listaMovimentoFinanceiroCota){
				if (movimentoFinanceiroCota.getCota().equals(ultimaCota)){
					valorMovimentoFinanceiro.add(movimentoFinanceiroCota.getValor());
					movimentos.add(movimentoFinanceiroCota);
				} else {
					
					//Decide se gera movimento consolidado ou postergado para a cota
					BigDecimal valorMinimo = 
							ultimaCota.getParametroCobranca().getValorMininoCobranca() != null ?
									ultimaCota.getParametroCobranca().getValorMininoCobranca() :
										politicaCobranca.getFormaCobranca().getValorMinimoEmissao();
					
					this.inserirConsolidadoFinanceiro(ultimaCota, valorMovimentoFinanceiro, movimentos,
							valorMinimo);
					
					//Limpa dados para contabilizar próxima cota
					ultimaCota = movimentoFinanceiroCota.getCota();
					valorMovimentoFinanceiro = BigDecimal.ZERO;
					movimentos = new ArrayList<MovimentoFinanceiroCota>();
				}
			}
			
			//Decide se gera movimento consolidado ou postergado para a ultima cota
			BigDecimal valorMinimo = 
					ultimaCota.getParametroCobranca().getValorMininoCobranca() != null ?
							ultimaCota.getParametroCobranca().getValorMininoCobranca() :
								politicaCobranca.getFormaCobranca().getValorMinimoEmissao();
			
			this.inserirConsolidadoFinanceiro(ultimaCota, valorMovimentoFinanceiro, movimentos, valorMinimo);
		}
	}
	
	private void inserirConsolidadoFinanceiro(Cota cota, BigDecimal valorMovimentoFinanceiro, 
			List<MovimentoFinanceiroCota> movimentos, BigDecimal valorMinino){
		
		ConsolidadoFinanceiroCota consolidadoFinanceiroCota = new ConsolidadoFinanceiroCota();
		consolidadoFinanceiroCota.setCota(cota);
		consolidadoFinanceiroCota.setDataConsolidado(new Date());
		if (valorMovimentoFinanceiro.compareTo(valorMinino) >= 0){
			//gerar consolidado
			consolidadoFinanceiroCota.setTotal(valorMovimentoFinanceiro);
		} else {
			//gerar postergado
			consolidadoFinanceiroCota.setValorPostergado(valorMovimentoFinanceiro);
		}
		consolidadoFinanceiroCota.setMovimentos(movimentos);
		
		this.consolidadoFinanceiroRepository.adicionar(consolidadoFinanceiroCota);
	}
}