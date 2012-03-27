package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.CobrancaCheque;
import br.com.abril.nds.model.financeiro.CobrancaDeposito;
import br.com.abril.nds.model.financeiro.CobrancaDinheiro;
import br.com.abril.nds.model.financeiro.CobrancaTransferenciaBancaria;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.ControleBaixaBancaria;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.repository.ControleBaixaBancariaRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheRepository;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
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
	
	@Autowired
	private DividaRepository dividaRepository;
	
	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	@Autowired
	private CobrancaRepository cobrancaRepository;
	
	@Override
	@Transactional
	public void gerarCobranca(Long idCota, Long idUsuario) {
		
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
							valorMinimo, politicaCobranca.isAcumulaDivida(), idUsuario, 
							politicaCobranca.getFormaCobranca().getTipoCobranca());
					
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
			
			this.inserirConsolidadoFinanceiro(ultimaCota, valorMovimentoFinanceiro, movimentos, valorMinimo,
					politicaCobranca.isAcumulaDivida(), idUsuario, politicaCobranca.getFormaCobranca().getTipoCobranca());
		}
	}
	
	private void inserirConsolidadoFinanceiro(Cota cota, BigDecimal valorMovimentoFinanceiro, 
			List<MovimentoFinanceiroCota> movimentos, BigDecimal valorMinino, boolean acumulaDivida, Long idUsuario,
			TipoCobranca tipoCobranca){
		
		ConsolidadoFinanceiroCota consolidadoFinanceiroCota = new ConsolidadoFinanceiroCota();
		consolidadoFinanceiroCota.setCota(cota);
		consolidadoFinanceiroCota.setDataConsolidado(new Date());
		consolidadoFinanceiroCota.setMovimentos(movimentos);
		consolidadoFinanceiroCota.setTotal(valorMovimentoFinanceiro);
		
		Usuario usuario = new Usuario();
		usuario.setId(idUsuario);
		
		Divida divida = null;
		
		MovimentoFinanceiroCota movimentoFinanceiroCota = null;
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = null;
		
		//se existe divida
		if (valorMovimentoFinanceiro.compareTo(BigDecimal.ZERO) < 0){
			
			if (valorMovimentoFinanceiro.compareTo(valorMinino) < 0){
				//gerar postergado
				consolidadoFinanceiroCota.setValorPostergado(valorMovimentoFinanceiro);
			} else {
				//se o distribuidor acumula divida
				if (acumulaDivida){
					divida = this.dividaRepository.obterUltimaDividaPorCota(cota.getId());
					
					//caso não tenha divida anterior, ou tenha sido quitada
					if (divida == null || StatusDivida.QUITADA.equals(divida.getStatus())){
						divida = new Divida();
					}
					
					divida.setValor(
							valorMovimentoFinanceiro.add(divida.getValor() == null ? BigDecimal.ZERO : divida.getValor()));
				} else {
					//se o distribuidor não acumula divida cria uma nova
					divida = new Divida();
					divida.setValor(valorMovimentoFinanceiro);
					divida.setData(new Date());
				}
				
				divida.setConsolidado(consolidadoFinanceiroCota);
				divida.setCota(cota);
				divida.setStatus(StatusDivida.EM_ABERTO);
				divida.setResponsavel(usuario);
			}
		} else {
			//gera movimento financeiro cota
			movimentoFinanceiroCota = new MovimentoFinanceiroCota();
			movimentoFinanceiroCota.setMotivo("Valor mínimo para dívida não atingido.");
			movimentoFinanceiroCota.setData(new Date());
			movimentoFinanceiroCota.setDataCriacao(new Date());
			movimentoFinanceiroCota.setUsuario(usuario);
			movimentoFinanceiroCota.setValor(valorMovimentoFinanceiro);
			movimentoFinanceiroCota.setLancamentoManual(false);
			movimentoFinanceiroCota.setCota(cota);
			
			tipoMovimentoFinanceiro = new TipoMovimentoFinanceiro();
			tipoMovimentoFinanceiro.setAprovacaoAutomatica(false);
			tipoMovimentoFinanceiro.setGrupoMovimentoFinaceiro(GrupoMovimentoFinaceiro.DEBITO);
			
			movimentoFinanceiroCota.setTipoMovimento(tipoMovimentoFinanceiro);
		}
		
		this.consolidadoFinanceiroRepository.adicionar(consolidadoFinanceiroCota);
		
		if (divida != null){
			if (divida.getId() == null){
				this.dividaRepository.adicionar(divida);
			} else {
				this.dividaRepository.alterar(divida);
			}
			
			Cobranca cobranca = null;
			
			switch (tipoCobranca){
				case BOLETO:
					cobranca = new Boleto();
				break;
				case CHEQUE:
					cobranca = new CobrancaCheque();
				break;
				case DINHEIRO:
					cobranca = new CobrancaDinheiro();
				break;
				case DEPOSITO:
					cobranca = new CobrancaDeposito();
				case TRANSFERENCIA_BANCARIA:
					cobranca = new CobrancaTransferenciaBancaria();
				break;
			}
			
			cobranca.setCota(cota);
			cobranca.setDataEmissao(new Date());
			cobranca.setDivida(divida);
			cobranca.setStatusCobranca(StatusCobranca.NAO_PAGO);
			
			if (cobranca != null){
				this.cobrancaRepository.adicionar(cobranca);
			}
		}
		
		if (movimentoFinanceiroCota != null){
			this.tipoMovimentoFinanceiroRepository.adicionar(tipoMovimentoFinanceiro);
			this.movimentoFinanceiroCotaRepository.adicionar(movimentoFinanceiroCota);
		}
	}
}