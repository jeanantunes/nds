package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ParcialVendaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
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
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;

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
	public void gerarPeriodosParcias(Long idProdutoEdicao, Integer qtdePeriodos, Usuario usuario, Integer peb) {
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		gerarPeriodosParcias(produtoEdicao, qtdePeriodos, usuario, peb, distribuidor);
	}
	
	@Override
	@Transactional
	public void gerarPeriodosParcias(ProdutoEdicao produtoEdicao, Integer qtdePeriodos, Usuario usuario, Integer peb, Distribuidor distribuidor) {
		
		validarProdutoEdicao(produtoEdicao);
		
		LancamentoParcial lancamentoParcial = obterLancamentoParcialValidado(produtoEdicao, qtdePeriodos);		
		
		Lancamento ultimoLancamento = lancamentoRepository.obterUltimoLancamentoDaEdicao(produtoEdicao.getId());
		
		Integer fatorRelancamentoParcial = distribuidor.getFatorRelancamentoParcial();
		
		if(peb==null)
			peb = produtoEdicao.getPeb(); 
		
		Date dtLancamento = null;
		Date dtRecolhimento = null;
		
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
		lancamento.setSequenciaMatriz(0);
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

		if(idLancamento == null) 
			throw new ValidacaoException(TipoMensagem.WARNING, "Id do Lancamento não deve ser nulo.");
			
		Lancamento lancamento = lancamentoRepository.buscarPorId(idLancamento);
		
		
		if(lancamento == null)
			throw new ValidacaoException(TipoMensagem.WARNING, "Lancamento não deve ser nulo.");
				
		PeriodoLancamentoParcial periodo = periodoLancamentoParcialRepository.obterPeriodoPorIdLancamento(idLancamento);
		
		if(DateUtil.isDataInicialMaiorDataFinal(periodo.getLancamentoParcial().getLancamentoInicial(), dataLancamento))
			throw new ValidacaoException(TipoMensagem.WARNING, "A data de Lançamento é inferior ao lançamento inicial da parcial.");
		
		if(DateUtil.isDataInicialMaiorDataFinal(dataRecolhimento, periodo.getLancamentoParcial().getRecolhimentoFinal()))
			throw new ValidacaoException(TipoMensagem.WARNING, "A de Recolhimento ultrapassa  o recolhimento final da parcial.");
				
		Boolean idMudancaPeriodoValida = periodoLancamentoParcialRepository.
				verificarValidadeNovoPeriodoParcial(idLancamento, dataLancamento, dataRecolhimento);
		
		if(!idMudancaPeriodoValida)
			throw new ValidacaoException(TipoMensagem.WARNING, "A nova data ultrapassa lançamentos e/ou recolhimentos de outro período.");
		
		lancamento.setDataLancamentoDistribuidor(dataLancamento);
		lancamento.setDataLancamentoPrevista(dataLancamento);
		lancamento.setDataRecolhimentoDistribuidor(dataRecolhimento);
		lancamento.setDataRecolhimentoPrevista(dataRecolhimento);
		
		lancamentoRepository.merge(lancamento);
		

		ajustarPeriodoAnteriorProximo(idLancamento, dataLancamento, dataRecolhimento);
				
	}

	private void ajustarPeriodoAnteriorProximo(Long idLancamento,
			Date dtLancamento, Date dtRecolhimento) {
	
		//TODO

		Lancamento lancamento = lancamentoRepository.buscarPorId(idLancamento);
		
		PeriodoLancamentoParcial periodo = periodoLancamentoParcialRepository.obterPeriodoPorIdLancamento(idLancamento);
		/*
		lancamento.setDataLancamentoDistribuidor(dataLancamento);
		lancamento.setDataLancamentoPrevista(dataLancamento);
		lancamento.setDataRecolhimentoDistribuidor(dataRecolhimento);
		lancamento.setDataRecolhimentoPrevista(dataRecolhimento);
		*/
		lancamentoRepository.merge(lancamento);
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
			throw new ValidacaoException(TipoMensagem.WARNING, "Lancamento já está em uso, não pode ser excluido.");
		
		PeriodoLancamentoParcial periodo = periodoLancamentoParcialRepository.obterPeriodoPorIdLancamento(idLancamento);
				
		periodoLancamentoParcialRepository.remover(periodo);
		
		lancamentoRepository.remover(lancamento);
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
