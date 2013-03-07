package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ParcialVendaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.HistoricoLancamento;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.LancamentoParcial;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.StatusLancamentoParcial;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.HistoricoLancamentoRepository;
import br.com.abril.nds.repository.LancamentoParcialRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.PeriodoLancamentoParcialRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.ParciaisService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;

@Service
public class ParciaisServiceImpl implements ParciaisService{
	
	@Autowired
	private LancamentoParcialRepository lancamentoParcialRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private HistoricoLancamentoRepository historicoLancamentoRepository;
	
	@Autowired
	private PeriodoLancamentoParcialRepository periodoLancamentoParcialRepository;
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Transactional
	public void gerarPeriodosParcias(Long idProdutoEdicao, Integer qtdePeriodos, Usuario usuario) {
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		
		gerarPeriodosParcias(produtoEdicao, qtdePeriodos, usuario);
	}
	
	@Override
	public void atualizarReparteDoProximoLancamentoParcial(Lancamento lancamento) {
		
		Lancamento proximoLancamento = 
				periodoLancamentoParcialRepository.obterLancamentoPosterior(lancamento.getProdutoEdicao().getId(), 
																			lancamento.getDataRecolhimentoDistribuidor());
		
		if(proximoLancamento!= null){
			
			proximoLancamento.setReparte(lancamento.getReparte());
			
			lancamentoRepository.alterar(proximoLancamento);
		}
	}

	@Override
	@Transactional
	public void gerarPeriodosParcias(ProdutoEdicao produtoEdicao, Integer qtdePeriodos, Usuario usuario) {
		
		validarProdutoEdicao(produtoEdicao);
		
		LancamentoParcial lancamentoParcial = obterLancamentoParcialValidado(produtoEdicao, qtdePeriodos);		
		
		Long qntPeriodosNaoBalanceados = periodoLancamentoParcialRepository.obterQntPeriodosAposBalanceamentoRealizado(lancamentoParcial.getId());
		
		Integer peb = produtoEdicao.getPeb();
		
		if(this.isLimparPeriodos(qtdePeriodos, lancamentoParcial,qntPeriodosNaoBalanceados)){
			
			peb = (produtoEdicao.getPeb() / (qtdePeriodos));
		
			qtdePeriodos = qtdePeriodos - qntPeriodosNaoBalanceados.intValue();
		}
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		Integer fatorRelancamentoParcial = distribuidor.getFatorRelancamentoParcial();
		
		Integer qntDiasUltrapassaPEB = this.qntDiasQueUltrapassamPEB(qtdePeriodos, fatorRelancamentoParcial, 
																	  lancamentoParcial.getRecolhimentoFinal(), 
																	  lancamentoParcial.getLancamentoInicial(),peb);
		if(qntDiasUltrapassaPEB!= null){
			
			if(qntDiasUltrapassaPEB < ( peb / 2 )){

				peb = peb + (qntDiasUltrapassaPEB / qtdePeriodos);
			}
		}
		
		this.processarDadosLancamentoParcial(produtoEdicao, qtdePeriodos, usuario,
											 lancamentoParcial, peb, fatorRelancamentoParcial);
	}

	private void processarDadosLancamentoParcial(ProdutoEdicao produtoEdicao,
												 Integer qtdePeriodos, Usuario usuario,
												 LancamentoParcial lancamentoParcial, 
												 Integer peb,Integer fatorRelancamentoParcial) {
		Date dtLancamento = null;
		Date dtRecolhimento = null;
		
		Lancamento ultimoLancamento = lancamentoRepository.obterUltimoLancamentoDaEdicao(produtoEdicao.getId());
		
		for(int i=0; i<qtdePeriodos; i++) {
		
			if(ultimoLancamento == null) {
				dtLancamento = lancamentoParcial.getLancamentoInicial();			
			} else {
				dtLancamento = calendarioService.adicionarDiasRetornarDiaUtil(ultimoLancamento.getDataRecolhimentoDistribuidor(), fatorRelancamentoParcial) ;
			}
			
			if(DateUtil.obterDiferencaDias(lancamentoParcial.getRecolhimentoFinal(), dtLancamento) > 0) {
				break;
			}			
			
			dtRecolhimento =  calendarioService.adicionarDiasRetornarDiaUtil(dtLancamento,peb); 
			
			if(DateUtil.obterDiferencaDias(lancamentoParcial.getRecolhimentoFinal(), dtRecolhimento) > 0) {
				
				Long qntDiasRestante = DateUtil.obterDiferencaDias(dtLancamento,lancamentoParcial.getRecolhimentoFinal());
				
				if(qntDiasRestante < (peb/2)){
					break;
				}
				
				i = qtdePeriodos;	
				dtRecolhimento = lancamentoParcial.getRecolhimentoFinal();
			}
			
			Lancamento novoLancamento =  gerarLancamento(produtoEdicao, dtLancamento, dtRecolhimento);
			
			HistoricoLancamento novoHistorico = gerarHistoricoLancamento(novoLancamento, usuario);
			
			PeriodoLancamentoParcial novoPeriodo = gerarPeriodoParcial(novoLancamento, lancamentoParcial);
			
			lancamentoRepository.adicionar(novoLancamento);
			
			historicoLancamentoRepository.adicionar(novoHistorico);
			
			periodoLancamentoParcialRepository.adicionar(novoPeriodo);
			
			ultimoLancamento = novoLancamento;
		}
	}
	
	private Integer qntDiasQueUltrapassamPEB(Integer qtdePeriodos,Integer fatorRelancamentoParcial, 
										     Date dataLancamentoFinal,Date dataLancamentoInicial,
										     Integer peb){
		Date dtLancamento = null;
		Date dtRecolhimento = null;
		
		for(int i=0; i<qtdePeriodos; i++) {
			
			if(dtLancamento == null){
				
				dtLancamento = dataLancamentoInicial;
			}
			else{
				
				dtLancamento = calendarioService.adicionarDiasRetornarDiaUtil(dtRecolhimento, fatorRelancamentoParcial) ;
			}
			
			if(DateUtil.obterDiferencaDias(dataLancamentoFinal, dtLancamento) > 0) {
				break;
			}			
			
			dtRecolhimento =  calendarioService.adicionarDiasRetornarDiaUtil(dtLancamento,peb); 
			
			if(DateUtil.obterDiferencaDias(dataLancamentoFinal, dtRecolhimento) > 0) {
				
				return  (int) DateUtil.obterDiferencaDias(dtLancamento, dataLancamentoFinal);
			}
		}
		
		return null;
	}
	
	private boolean isLimparPeriodos(int qtdePeriodos, LancamentoParcial lancamentoParcial,long qntPeriodosNaoBalanceados){
		
		if (qntPeriodosNaoBalanceados <= qtdePeriodos) {

			if (lancamentoParcial.getPeriodos() != null && lancamentoParcial.getPeriodos().size() > 0) {

				for (PeriodoLancamentoParcial item : lancamentoParcial.getPeriodos()) {

					if( Arrays.asList(StatusLancamento.PLANEJADO, 
									  StatusLancamento.CONFIRMADO)
									  .contains(item.getLancamento().getStatus())){
						
						periodoLancamentoParcialRepository.remover(item);
					}
				}
				
				return true;
			}
			
			return false;
		}
		else {

			throw new ValidacaoException(TipoMensagem.WARNING,"Quantidade de períodos é menor que a quantidade já programada para lançamento");
		}
	}

	private void validarProdutoEdicao(ProdutoEdicao produtEdicao) {
		
		if(produtEdicao == null)
			throw new ValidacaoException(TipoMensagem.WARNING, "ProdutoEdicao não deve ser nulo.");
	}

	private PeriodoLancamentoParcial gerarPeriodoParcial(Lancamento lancamento, LancamentoParcial lancamentoParcial) {
		
		PeriodoLancamentoParcial periodo = new PeriodoLancamentoParcial();
		periodo.setLancamento(lancamento);
		periodo.setLancamentoParcial(lancamentoParcial);
		periodo.setTipo(TipoLancamentoParcial.PARCIAL);
		periodo.setStatus(StatusLancamentoParcial.PROJETADO);
		periodo.setNumeroPeriodo(lancamentoParcial.getPeriodos().size());
		
		return periodo;
	}

	private HistoricoLancamento gerarHistoricoLancamento(Lancamento lancamento, Usuario usuario) {

		HistoricoLancamento historico = new HistoricoLancamento();
		historico.setLancamento(lancamento);
		historico.setTipoEdicao(TipoEdicao.INCLUSAO);
		historico.setStatus(lancamento.getStatus());
		historico.setDataEdicao(new Date());
		historico.setResponsavel(usuario);
		
		return historico;		
	}

	private Lancamento gerarLancamento(ProdutoEdicao produtoEdicao, Date dtLancamento, Date dtRecolhimento) {
		
		Lancamento lancamento = new Lancamento();
		lancamento.setTipoLancamento(TipoLancamento.PARCIAL);
		lancamento.setProdutoEdicao(produtoEdicao);
		lancamento.setDataLancamentoPrevista(dtLancamento);
		lancamento.setDataRecolhimentoPrevista(dtRecolhimento);
		lancamento.setDataLancamentoDistribuidor(dtLancamento);
		lancamento.setDataRecolhimentoDistribuidor(dtRecolhimento);
		lancamento.setReparte(BigInteger.ZERO);
		lancamento.setSequenciaMatriz(null);
		lancamento.setDataCriacao(new Date());
		lancamento.setDataStatus(new Date());
		lancamento.setStatus(StatusLancamento.PLANEJADO);
		
		return lancamento;		
	}

	private LancamentoParcial obterLancamentoParcialValidado(ProdutoEdicao produtoEdicao, Integer qtdePeriodos) {
		
		if(qtdePeriodos == null || qtdePeriodos<=0)
			throw new ValidacaoException(TipoMensagem.WARNING, "Quantidade de periodos informada deve ser maior que zero.");
		
		if(produtoEdicao == null)
			throw new ValidacaoException(TipoMensagem.WARNING, "ProdutoEdicao não deve ser nulo.");
		
		if(!produtoEdicao.isParcial())
			throw new ValidacaoException(TipoMensagem.WARNING, "ProdutoEdicao não é parcial.");
		
		LancamentoParcial lancamentoParcial = lancamentoParcialRepository.obterLancamentoPorProdutoEdicao(produtoEdicao.getId());
		
		if(lancamentoParcial == null)
			throw new ValidacaoException(TipoMensagem.WARNING, "Não há LancamentoParcial para o ProdutoEdicao " + produtoEdicao.getId() +".");
				
		return lancamentoParcial;		
	}

	@Override
	@Transactional
	public void alterarPeriodo(Long idLancamento, Date dataLancamento,
			Date dataRecolhimento) {
		
		if(dataLancamento.compareTo(dataRecolhimento) >= 0 ){
			throw new ValidacaoException(TipoMensagem.WARNING,"Data de recolhimeno não pode ser menor ou igual a data de lançamento");
		}
		
		if(idLancamento == null) 
			throw new ValidacaoException(TipoMensagem.WARNING, "Id do Lancamento não deve ser nulo.");
			
		Lancamento lancamento = lancamentoRepository.buscarPorId(idLancamento);
		
		if(lancamento == null)
			throw new ValidacaoException(TipoMensagem.WARNING, "Lancamento não deve ser nulo.");
				
		if(!lancamento.getStatus().equals(StatusLancamento.PLANEJADO))
			throw new ValidacaoException(TipoMensagem.WARNING, "Lancamento já foi realizado, não pode ser alterado.");
						
		PeriodoLancamentoParcial periodo = periodoLancamentoParcialRepository.obterPeriodoPorIdLancamento(idLancamento);
		
		if(DateUtil.isDataInicialMaiorDataFinal(periodo.getLancamentoParcial().getLancamentoInicial(), dataLancamento))
			throw new ValidacaoException(TipoMensagem.WARNING, "A data de Lançamento é inferior ao lançamento inicial da parcial.");
		
		if(DateUtil.isDataInicialMaiorDataFinal(dataRecolhimento, periodo.getLancamentoParcial().getRecolhimentoFinal()))
			throw new ValidacaoException(TipoMensagem.WARNING, "A data de Recolhimento ultrapassa  o recolhimento final da parcial.");
				
		Boolean idMudancaPeriodoValida = periodoLancamentoParcialRepository.
				verificarValidadeNovoPeriodoParcial(idLancamento, dataLancamento, dataRecolhimento);
		
		if(!idMudancaPeriodoValida)
			throw new ValidacaoException(TipoMensagem.WARNING, "A nova data ultrapassa lançamentos e/ou recolhimentos de outro período.");
		
		Long diferencaLancamento = DateUtil.obterDiferencaDias(lancamento.getDataLancamentoDistribuidor(), dataLancamento);
		Long diferencaRecolhimento = DateUtil.obterDiferencaDias(dataRecolhimento, lancamento.getDataRecolhimentoDistribuidor());
		
		
		lancamento.setDataLancamentoDistribuidor(dataLancamento);
		lancamento.setDataLancamentoPrevista(dataLancamento);
		lancamento.setDataRecolhimentoDistribuidor(dataRecolhimento);
		lancamento.setDataRecolhimentoPrevista(dataRecolhimento);
		
		lancamentoRepository.merge(lancamento);
		
		if( !diferencaLancamento.equals(0) )
			ajustarPeriodoAnterior(lancamento.getProdutoEdicao().getId(), dataLancamento, diferencaLancamento.intValue());
		
		if( !diferencaRecolhimento.equals(0) )
			ajustarPeriodoProximo(lancamento.getProdutoEdicao().getId(), dataRecolhimento, diferencaRecolhimento.intValue());
		
	}
	
	private void ajustarPeriodoProximo(Long idProdutoEdicao,
			Date dataRecolhimento, Integer diferencaRecolhimento) {

		Lancamento lancamentoPosterior = periodoLancamentoParcialRepository.obterLancamentoPosterior(idProdutoEdicao,dataRecolhimento);
		
		if(lancamentoPosterior==null)
			return;
		
		if(!lancamentoPosterior.getStatus().equals(StatusLancamento.PLANEJADO))
				return;
		
		lancamentoPosterior.setDataLancamentoDistribuidor(
				DateUtil.subtrairDias(lancamentoPosterior.getDataLancamentoDistribuidor(), diferencaRecolhimento));
		
		lancamentoRepository.merge(lancamentoPosterior);
	}

	private void ajustarPeriodoAnterior(Long idProdutoEdicao,
			Date dataLancamento, Integer diferencaLancamento) {
		
		Lancamento lancamentoAnterior = periodoLancamentoParcialRepository.obterLancamentoAnterior(idProdutoEdicao,dataLancamento);
		
		if(lancamentoAnterior==null)
			return;
		
		if(!lancamentoAnterior.getStatus().equals(StatusLancamento.PLANEJADO))
				return;
		
		lancamentoAnterior.setDataRecolhimentoDistribuidor(
				DateUtil.adicionarDias(lancamentoAnterior.getDataRecolhimentoDistribuidor(), diferencaLancamento));
		
		lancamentoRepository.merge(lancamentoAnterior);
	}

	@Override
	@Transactional
	public void excluirPeriodo(Long idLancamento) {
		
		if(idLancamento == null) 
			throw new ValidacaoException(TipoMensagem.WARNING, "Id do Lancamento não deve ser nulo.");
			
		Lancamento lancamento = lancamentoRepository.buscarPorId(idLancamento);		
		
		if(lancamento == null)
			throw new ValidacaoException(TipoMensagem.WARNING, "Lancamento não deve ser nulo.");
		
		if( !lancamento.getStatus().equals(StatusLancamento.PLANEJADO)) 
			throw new ValidacaoException(TipoMensagem.WARNING, "Lancamento já foi realizado, não pode ser excluido.");

		PeriodoLancamentoParcial periodo = periodoLancamentoParcialRepository.obterPeriodoPorIdLancamento(idLancamento);
		
		if ( periodo.getLancamentoParcial()!= null 
				&&  periodo.getLancamentoParcial().getPeriodos()!= null 
				&& periodo.getLancamentoParcial().getPeriodos().size() == 1 ){
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Para excluir todos os lançamentos parciais deve ser alterado o tipo de lançamento no cadastro de edição");
		}
		
		periodoLancamentoParcialRepository.remover(periodo);
	}

	/**
	 * Obtem detalhes das vendas do produtoEdição nas datas de Lancamento e Recolhimento
	 * @param dataLancamento
	 * @param dataRecolhimento
	 * @param idProdutoRdicao
	 * @return List<ParcialVendaDTO>
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ParcialVendaDTO> obterDetalhesVenda(Date dataLancamento,
			Date dataRecolhimento, Long idProdutoEdicao) {
		return periodoLancamentoParcialRepository.obterDetalhesVenda(dataLancamento, dataRecolhimento, idProdutoEdicao);
	}

	
}
