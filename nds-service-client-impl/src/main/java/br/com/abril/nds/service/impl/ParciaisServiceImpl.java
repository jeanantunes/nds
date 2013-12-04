package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ParcialVendaDTO;
import br.com.abril.nds.dto.RedistribuicaoParcialDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
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
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.UsuarioService;
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
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Transactional
	public void gerarPeriodosParcias(Long idProdutoEdicao, Integer qtdePeriodos, Usuario usuario) {
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		
		gerarPeriodosParcias(produtoEdicao, qtdePeriodos, usuario);
	}
	
	@Override
	public void atualizarReparteDoProximoLancamentoParcial(Lancamento lancamento, Usuario usuario) {
		
		Lancamento proximoLancamento = 
				periodoLancamentoParcialRepository.obterLancamentoPosterior(lancamento.getProdutoEdicao().getId(), 
																			lancamento.getDataRecolhimentoDistribuidor());
		
		if(proximoLancamento!= null){
			
			proximoLancamento.setReparte(lancamento.getReparte());
			
			proximoLancamento.setUsuario(usuario);
			
			lancamentoRepository.alterar(proximoLancamento);
		}
	}
	
	@Transactional(readOnly=true)
	public Integer calcularPebParcial(String codigoProduto, Long edicaoProduto, Integer qtdePeriodos){
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, edicaoProduto);
		
		return getPebProduto(produtoEdicao, qtdePeriodos);
	}
	
	private Integer getPebProduto(ProdutoEdicao produtoEdicao, Integer qntPeriodos){
		
		//TODO Ajuste alterações PARCIAIS
		//Adequar o calculo da PEB para obedecer a nova regra especificada pelo genesio
		
		Integer fatorRelancamentoParcial = this.distribuidorService.fatorRelancamentoParcial();
		
		if(produtoEdicao == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Produto Edição não encontrado!");
		}
		
		if(fatorRelancamentoParcial == null ){
			return produtoEdicao.getPeb();
		}
		
		if(qntPeriodos == null || qntPeriodos <= 1){
			return produtoEdicao.getPeb();
		}
		
		return ((produtoEdicao.getPeb() - (fatorRelancamentoParcial*(qntPeriodos-1))) / qntPeriodos);
	}

	@Override
	@Transactional
	public void gerarPeriodosParcias(ProdutoEdicao produtoEdicao, Integer qtdePeriodos, Usuario usuario) {
		
		validarProdutoEdicao(produtoEdicao);
		
		LancamentoParcial lancamentoParcial = obterLancamentoParcialValidado(produtoEdicao, qtdePeriodos);		
		
		Long qntPeriodosNaoBalanceados = periodoLancamentoParcialRepository.obterQntPeriodosAposBalanceamentoRealizado(lancamentoParcial.getId());
		
		Integer peb = getPebProduto(produtoEdicao, qtdePeriodos);
		
		if(this.isLimparPeriodos(qtdePeriodos, lancamentoParcial,qntPeriodosNaoBalanceados)){
			
			qtdePeriodos = qtdePeriodos - qntPeriodosNaoBalanceados.intValue();
		}
				
		this.processarDadosLancamentoParcial(produtoEdicao, qtdePeriodos, usuario,
											 lancamentoParcial, peb);
	}

	private void processarDadosLancamentoParcial(ProdutoEdicao produtoEdicao,
												 Integer qtdePeriodos, Usuario usuario,
												 LancamentoParcial lancamentoParcial, 
												 Integer peb) {
		Date dtLancamento = null;
		Date dtRecolhimento = null;
		
		Lancamento ultimoLancamento = lancamentoRepository.obterUltimoLancamentoDaEdicao(produtoEdicao.getId());
		
		Integer fatorRelancamentoParcial = this.distribuidorService.fatorRelancamentoParcial();
		
		for(int numeroPeriodo=0, numeroLancametoPeriodo = 1; numeroPeriodo<qtdePeriodos; numeroPeriodo++ , numeroLancametoPeriodo++) {
		
			if(ultimoLancamento == null) {
				dtLancamento = lancamentoParcial.getLancamentoInicial();			
			} else {
				
				dtLancamento = calendarioService.adicionarDiasRetornarDiaUtil(ultimoLancamento.getDataRecolhimentoDistribuidor(),fatorRelancamentoParcial) ;
			}
			
			if(DateUtil.obterDiferencaDias(lancamentoParcial.getRecolhimentoFinal(), dtLancamento) > 0) {
				break;
			}			
			
			dtRecolhimento =  calendarioService.adicionarDiasRetornarDiaUtil(dtLancamento,peb); 
			
			if(DateUtil.obterDiferencaDias(lancamentoParcial.getRecolhimentoFinal(), dtRecolhimento) > 0) {
				
				numeroPeriodo = qtdePeriodos;	
				dtRecolhimento = lancamentoParcial.getRecolhimentoFinal();
			}
			
			PeriodoLancamentoParcial novoPeriodo = gerarPeriodoParcial(lancamentoParcial,numeroLancametoPeriodo);
			
			novoPeriodo = periodoLancamentoParcialRepository.merge(novoPeriodo);
			
			Lancamento novoLancamento =  gerarLancamento(novoPeriodo,produtoEdicao, dtLancamento, dtRecolhimento, usuario);
			
			lancamentoRepository.adicionar(novoLancamento);
			
			ultimoLancamento = novoLancamento;
		}
	}
	
	private boolean isLimparPeriodos(int qtdePeriodos, LancamentoParcial lancamentoParcial,long qntPeriodosNaoBalanceados){
		
		if (qntPeriodosNaoBalanceados <= qtdePeriodos) {

			if (lancamentoParcial.getPeriodos() != null && lancamentoParcial.getPeriodos().size() > 0) {

				for (PeriodoLancamentoParcial item : lancamentoParcial.getPeriodos()) {
					
					Lancamento lancamento = item.getLancamentoPeriodoParcial();
					
					if(lancamento!= null && 
							Arrays.asList(StatusLancamento.PLANEJADO, 
									  StatusLancamento.CONFIRMADO)
									  .contains(lancamento.getStatus())){
						
						periodoLancamentoParcialRepository.remover(item);
						// TODO Ajsute Parcial 
						//remover as redistribuição
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

	private PeriodoLancamentoParcial gerarPeriodoParcial(LancamentoParcial lancamentoParcial,int numeroPeriodo) {
		
		PeriodoLancamentoParcial periodo = new PeriodoLancamentoParcial();
		periodo.setLancamentoParcial(lancamentoParcial);
		periodo.setTipo(TipoLancamentoParcial.PARCIAL);
		periodo.setStatus(StatusLancamentoParcial.PROJETADO);
		periodo.setNumeroPeriodo(numeroPeriodo);
		
		return periodo;
	}

	@SuppressWarnings("unused")
	private HistoricoLancamento gerarHistoricoLancamento(Lancamento lancamento, Usuario usuario) {

		HistoricoLancamento historico = new HistoricoLancamento();
		historico.setLancamento(lancamento);
		historico.setTipoEdicao(TipoEdicao.INCLUSAO);
		historico.setStatusNovo(lancamento.getStatus());
		historico.setDataEdicao(new Date());
		historico.setResponsavel(usuario);
		
		return historico;		
	}

	private Lancamento gerarLancamento(PeriodoLancamentoParcial novoPeriodo,ProdutoEdicao produtoEdicao, Date dtLancamento, Date dtRecolhimento, Usuario usuario) {
		
		Lancamento lancamento = new Lancamento();
		lancamento.setTipoLancamento(TipoLancamento.LANCAMENTO);
		lancamento.setPeriodoLancamentoParcial(novoPeriodo);
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
		lancamento.setNumeroLancamento(BigInteger.ONE.intValue());
		lancamento.setUsuario(usuario);
		
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
			Date dataRecolhimento, Usuario usuario) {
		
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
		
		//TODO Ajuste alterações PARCIAIS
		// Adequar as novas regras de validação a ser definidas
		
		/*Boolean idMudancaPeriodoValida = periodoLancamentoParcialRepository.
				verificarValidadeNovoPeriodoParcial(idLancamento, dataLancamento, dataRecolhimento);
		
		if(!idMudancaPeriodoValida)
			throw new ValidacaoException(TipoMensagem.WARNING, "A nova data ultrapassa lançamentos e/ou recolhimentos de outro período.");
		*/
		Long diferencaLancamento = DateUtil.obterDiferencaDias(lancamento.getDataLancamentoDistribuidor(), dataLancamento);
		Long diferencaRecolhimento = DateUtil.obterDiferencaDias(dataRecolhimento, lancamento.getDataRecolhimentoDistribuidor());
		
		
		lancamento.setDataLancamentoDistribuidor(dataLancamento);
		lancamento.setDataLancamentoPrevista(dataLancamento);
		lancamento.setDataRecolhimentoDistribuidor(dataRecolhimento);
		lancamento.setDataRecolhimentoPrevista(dataRecolhimento);
		lancamento.setUsuario(usuario);
		
		lancamentoRepository.merge(lancamento);
		
		if( !diferencaLancamento.equals(0) )
			ajustarPeriodoAnterior(lancamento.getProdutoEdicao().getId(), dataLancamento, diferencaLancamento.intValue(), usuario);
		
		if( !diferencaRecolhimento.equals(0) )
			ajustarPeriodoProximo(lancamento.getProdutoEdicao().getId(), dataRecolhimento, diferencaRecolhimento.intValue(), usuario);
		
	}
	
	private void ajustarPeriodoProximo(Long idProdutoEdicao,
			Date dataRecolhimento, Integer diferencaRecolhimento, Usuario usuario) {

		Lancamento lancamentoPosterior = periodoLancamentoParcialRepository.obterLancamentoPosterior(idProdutoEdicao,dataRecolhimento);
		
		if(lancamentoPosterior==null)
			return;
		
		if(!lancamentoPosterior.getStatus().equals(StatusLancamento.PLANEJADO))
				return;
		
		lancamentoPosterior.setDataLancamentoDistribuidor(
				DateUtil.subtrairDias(lancamentoPosterior.getDataLancamentoDistribuidor(), diferencaRecolhimento));
		
		lancamentoPosterior.setUsuario(usuario);
		
		lancamentoRepository.merge(lancamentoPosterior);
	}

	private void ajustarPeriodoAnterior(Long idProdutoEdicao,
			Date dataLancamento, Integer diferencaLancamento, Usuario usuario) {
		
		Lancamento lancamentoAnterior = periodoLancamentoParcialRepository.obterLancamentoAnterior(idProdutoEdicao,dataLancamento);
		
		if(lancamentoAnterior==null)
			return;
		
		if(!lancamentoAnterior.getStatus().equals(StatusLancamento.PLANEJADO))
				return;
		
		lancamentoAnterior.setDataRecolhimentoDistribuidor(
				DateUtil.adicionarDias(lancamentoAnterior.getDataRecolhimentoDistribuidor(), diferencaLancamento));
		
		lancamentoAnterior.setUsuario(usuario);
		
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

	@Override
	@Transactional(readOnly = true)
	public List<RedistribuicaoParcialDTO> obterRedistribuicoesParciais(Long idPeriodo) {
		
		return this.periodoLancamentoParcialRepository.obterRedistribuicoesParciais(idPeriodo);
	}
	
	@Override
	@Transactional
	public void incluirRedistribuicaoParcial(RedistribuicaoParcialDTO redistribuicaoParcialDTO) {
		
		Long idPeriodo = redistribuicaoParcialDTO.getIdPeriodo();
		
		PeriodoLancamentoParcial periodoLancamentoParcial =
			this.periodoLancamentoParcialRepository.buscarPorId(idPeriodo);
		
		if (periodoLancamentoParcial == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"Período Lançamento Parcial não encontrado!");
		}
		
		ProdutoEdicao produtoEdicao = periodoLancamentoParcial.getLancamentoParcial().getProdutoEdicao();
		
		Lancamento lancamento = new Lancamento();
		
		lancamento.setDataLancamentoDistribuidor(redistribuicaoParcialDTO.getDataLancamento());
		lancamento.setDataLancamentoPrevista(redistribuicaoParcialDTO.getDataLancamento());
		lancamento.setDataRecolhimentoDistribuidor(redistribuicaoParcialDTO.getDataRecolhimento());
		lancamento.setDataRecolhimentoPrevista(redistribuicaoParcialDTO.getDataRecolhimento());
		lancamento.setDataCriacao(new Date());
		lancamento.setDataStatus(new Date());
		lancamento.setStatus(StatusLancamento.CONFIRMADO);
		lancamento.setTipoLancamento(TipoLancamento.REDISTRIBUICAO);
		lancamento.setProdutoEdicao(produtoEdicao);
		lancamento.setUsuario(this.usuarioService.getUsuarioLogado());
		
		lancamento.setNumeroLancamento(
			this.produtoEdicaoService.obterNumeroLancamento(produtoEdicao.getId()));
		
		lancamento.setPeriodoLancamentoParcial(periodoLancamentoParcial);
		
		this.lancamentoRepository.adicionar(lancamento);
	}
	
	@Override
	@Transactional
	public void salvarRedistribuicaoParcial(RedistribuicaoParcialDTO redistribuicaoParcialDTO) {
		
		Long idLancamento = redistribuicaoParcialDTO.getIdLancamentoRedistribuicao();
		
		Lancamento lancamento = this.lancamentoRepository.buscarPorId(idLancamento);
		
		if (lancamento == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"Lançamento não encontrado!");
		}
		
		lancamento.setDataLancamentoDistribuidor(redistribuicaoParcialDTO.getDataLancamento());
		lancamento.setDataLancamentoPrevista(redistribuicaoParcialDTO.getDataLancamento());
		lancamento.setUsuario(this.usuarioService.getUsuarioLogado());
		
		this.lancamentoRepository.alterar(lancamento);
	}
	
	@Override
	@Transactional
	public void excluirRedistribuicaoParcial(Long idLancamentoRedistribuicao) {
		
		Lancamento lancamento = this.lancamentoRepository.buscarPorId(idLancamentoRedistribuicao);
		
		if (lancamento == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"Lançamento não encontrado!");
		}
		
		List<HistoricoLancamento> historicoLancamentos = lancamento.getHistoricos();
		
		for (HistoricoLancamento historicoLancamento : historicoLancamentos) {
			this.historicoLancamentoRepository.remover(historicoLancamento);
		}
		
		this.lancamentoRepository.removerPorId(idLancamentoRedistribuicao);
	}
	
}
