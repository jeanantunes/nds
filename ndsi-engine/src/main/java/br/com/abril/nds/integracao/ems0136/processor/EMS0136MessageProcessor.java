package br.com.abril.nds.integracao.ems0136.processor;

import java.math.BigInteger;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0136Input;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.LancamentoParcial;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.StatusLancamentoParcial;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.repository.impl.AbstractRepository;

@Component
public class EMS0136MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@Autowired
	private DistribuidorService distribuidorService;
	
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processMessage(Message message) {
		
		EMS0136Input input = (EMS0136Input) message.getBody();
		
		// Validar código do distribuidor:
		Distribuidor distribuidor = this.distribuidorService.obter();
		if(!distribuidor.getCodigoDistribuidorDinap().equals(
				input.getCodigoDistribuidor())){			
			this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.RELACIONAMENTO, 
					"Código do distribuidor do arquivo não é o mesmo do Sistema.");
			return;
		}
		
		// Validar Produto/Edicao
		final String codigoProduto = input.getCodigoProduto();
		final Long numeroEdicao = input.getEdicaoCapa();
		ProdutoEdicao produtoEdicao = this.obterProdutoEdicao(codigoProduto,
				numeroEdicao);
		if (produtoEdicao == null) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"Impossivel realizar Insert/update - Nenhum resultado encontrado para Produto: "
							+ codigoProduto + " e Edicao: " + numeroEdicao
							+ " na tabela produto_edicao");
			return;
		}
		
		LancamentoParcial lancamentoParcial = this.obterLancalmentoParcial(
				produtoEdicao);
		if (lancamentoParcial == null) {
			
			// Novo Lançamento Parcial:
			lancamentoParcial = this.gerarNovoLancamentoParcial(input, 
					produtoEdicao);			
		} else {		
			
			this.atualizarLancamentoParcial(input, lancamentoParcial);			
		}
		
		// Novo Lançamento:
		Lancamento lancamento = this.gerarNovoLancamento(input, produtoEdicao);
		
		// Novo Período Lançamento Parcial;
		this.gerarNovoPeriodoLancamentoParcial(input, lancamentoParcial, 
				lancamento);
		
		
	}
	
	/**
	 * Obtém o Produto Edição cadastrado previamente.
	 * 
	 * @param codigoProduto Código da Publicação.
	 * @param numeroEdicao Número da Edição.
	 * 
	 * @return
	 */
	private ProdutoEdicao obterProdutoEdicao(String codigoProduto,
			Long numeroEdicao) {

		try {

			Criteria criteria = this.getSession().createCriteria(
					ProdutoEdicao.class, "produtoEdicao");

			criteria.createAlias("produtoEdicao.produto", "produto");
			criteria.setFetchMode("produto", FetchMode.JOIN);

			criteria.add(Restrictions.eq("produto.codigo", codigoProduto));
			criteria.add(Restrictions.eq("produtoEdicao.numeroEdicao", numeroEdicao));

			return (ProdutoEdicao) criteria.uniqueResult();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Obtém o Lançamento Parcial já cadastrado para o ProdutoEdição informado.
	 * 
	 * @param produtoEdicao
	 * 
	 * @return
	 */
	private LancamentoParcial obterLancalmentoParcial(
			ProdutoEdicao produtoEdicao) {
		
		Criteria criteria = getSession().createCriteria(LancamentoParcial.class);
		criteria.add(Restrictions.eq("produtoEdicao", produtoEdicao));
		
		return (LancamentoParcial) criteria.uniqueResult();
	}
	
	/**
	 * Gera um novo Lançamento Parcial.
	 * 
	 * @param input
	 * @param produtoEdicao
	 */
	private LancamentoParcial gerarNovoLancamentoParcial(EMS0136Input input, 
			ProdutoEdicao produtoEdicao) {
		
		StatusLancamentoParcial status = this.obterStatusLancamentoParcial(
				input);	
		
		// Novo Lançamento Parcial:
		LancamentoParcial lancamentoParcial = new LancamentoParcial();
		lancamentoParcial.setProdutoEdicao(produtoEdicao);
		lancamentoParcial.setLancamentoInicial(input.getDataLancamento());
		lancamentoParcial.setRecolhimentoFinal(input.getDataRecolhimento());
		lancamentoParcial.setStatus(status);		
		
		// Persistir:
		this.getSession().persist(lancamentoParcial);
		
		return lancamentoParcial;
	}

	/**
	 * Obtém o "Status Lançamento Parcial" verificando se a "Data de Operação"
	 * do sistema é maior que a "Data de Recolhimento" vinda do arquivo.<br>
	 * Em caso positivo o status é "RECOLHIDO", senão é "PROJETADO".
	 * 
	 * @param input
	 * @return
	 */
	private StatusLancamentoParcial obterStatusLancamentoParcial(
			EMS0136Input input) {
		/*
		 * Se a "Data de Operação" > "Data de Recolhimento" então o status é
		 * RECOLHIDO, senão, é PROJETADO
		 */
		Date dataOperacao = distribuidorService.obter().getDataOperacao();
		StatusLancamentoParcial status = dataOperacao.after(
			input.getDataRecolhimento())
				? StatusLancamentoParcial.RECOLHIDO
				: StatusLancamentoParcial.PROJETADO;
		return status;
	}
	
	/**
	 * Gera um novo Lançamento.
	 * 
	 * @param input
	 * @param produtoEdicao
	 * 
	 * @return
	 */
	private Lancamento gerarNovoLancamento(EMS0136Input input,
			ProdutoEdicao produtoEdicao) {
		
		Date dtAgora = new Date();
		Lancamento lancamento = new Lancamento();
		lancamento.setDataCriacao(dtAgora);
		lancamento.setDataLancamentoPrevista(input.getDataLancamento());
		lancamento.setDataLancamentoDistribuidor(input.getDataLancamento());
		lancamento.setDataRecolhimentoDistribuidor(input.getDataRecolhimento());
		lancamento.setDataRecolhimentoPrevista(input.getDataRecolhimento());
		lancamento.setProdutoEdicao(produtoEdicao);
		lancamento.setTipoLancamento(TipoLancamento.PARCIAL);
		lancamento.setDataStatus(dtAgora);
		lancamento.setReparte(BigInteger.ZERO);
		lancamento.setRepartePromocional(BigInteger.ZERO);
		lancamento.setSequenciaMatriz(Integer.valueOf(0));
		lancamento.setStatus(StatusLancamento.TRANSMITIDO);
		
		this.getSession().persist(lancamento);
		
		return lancamento;
	}

	/**
	 * Gerar novo Período Lançamento Parcial.
	 * 
	 * @param input
	 * @param lancamentoParcial
	 * @param lancamento
	 * @return
	 */
	private PeriodoLancamentoParcial gerarNovoPeriodoLancamentoParcial(
			EMS0136Input input, LancamentoParcial lancamentoParcial, 
			Lancamento lancamento) {
		
		PeriodoLancamentoParcial pLancamentoParcial = new PeriodoLancamentoParcial();
		pLancamentoParcial.setLancamentoParcial(lancamentoParcial);
		pLancamentoParcial.setLancamento(lancamento);
		pLancamentoParcial.setStatus(this.obterStatusLancamentoParcial(input));
		pLancamentoParcial.setNumeroPeriodo(input.getNumeroPeriodo());
		pLancamentoParcial.setTipo(this.obterTipoLancamentoParcial(input));
		pLancamentoParcial.setStatus((input.getDataRecolhimento().compareTo(new Date()) < 0 ? StatusLancamentoParcial.RECOLHIDO :  StatusLancamentoParcial.PROJETADO ));
		
		this.getSession().persist(pLancamentoParcial);
		
		return pLancamentoParcial;
	}

	/**
	 * Retorna o Tipo de Lançamento Parcial que esta definido no arquivo.
	 * 
	 * @param input
	 * @return
	 */
	private TipoLancamentoParcial obterTipoLancamentoParcial(EMS0136Input input) {
		
		return "F".equalsIgnoreCase(input.getTipoRecolhimento()) 
					? TipoLancamentoParcial.FINAL 
					: TipoLancamentoParcial.PARCIAL;
	}
	
	/**
	 * Atualiza os dados do Lançamento Parcial, além do Período Lançamento 
	 * Parcial e Lançamento.
	 * 
	 * @param input
	 * @param lancamentoParcial
	 */
	private void atualizarLancamentoParcial(EMS0136Input input,
			LancamentoParcial lancamentoParcial) {
		boolean hasAlteracao = false;

		// Update da Data Inicial de lançamento:
		Date dtLancamento = input.getDataLancamento();
		if (dtLancamento.before(lancamentoParcial.getLancamentoInicial())) {
			lancamentoParcial.setLancamentoInicial(dtLancamento);
			hasAlteracao = true;
		}
		
		// Update da Data Final de recolhimento:
		Date dtRecolhimento = input.getDataRecolhimento();
		if (dtRecolhimento.after(lancamentoParcial.getRecolhimentoFinal())) {
			lancamentoParcial.setRecolhimentoFinal(dtRecolhimento);
			hasAlteracao = true;
		}
		
		if (hasAlteracao) {
			this.getSession().update(lancamentoParcial);
		}
		
		// Update do Período:
		Integer numeroPeriodo = input.getNumeroPeriodo();
		for (PeriodoLancamentoParcial periodo : lancamentoParcial.getPeriodos()) {
			if (numeroPeriodo.equals(periodo.getNumeroPeriodo())) {
				
				periodo.setStatus(this.obterStatusLancamentoParcial(input));
				periodo.setTipo(this.obterTipoLancamentoParcial(input));
				
				Lancamento lancamento = periodo.getLancamento();
				lancamento.setDataLancamentoPrevista(input.getDataLancamento());
				lancamento.setDataRecolhimentoPrevista(input.getDataRecolhimento());
				
				this.getSession().update(periodo);
				this.getSession().update(lancamento);
				
				return;
			}
		}
			
	}
	
	
	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub

	}

}
