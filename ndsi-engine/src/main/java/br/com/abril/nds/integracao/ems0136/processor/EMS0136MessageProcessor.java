package br.com.abril.nds.integracao.ems0136.processor;

import java.math.BigInteger;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
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
	public void preProcess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void processMessage(Message message) {
		
		EMS0136Input input = (EMS0136Input) message.getBody();
		if (input == null) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.ERRO_INFRA, "NAO ENCONTROU o Arquivo");
			return;
		}
		
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
			
			this.gerarNovoLancamentoParcial(input, produtoEdicao);
		} else {
			
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
				}
			}
			
			if (hasAlteracao) {
				this.getSession().update(lancamentoParcial);
			}		
		}
		
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
	 * Salva um novo Lançamento Parcial, gerando o Lançamento e Periodo 
	 * Lançamento Parcial associados.
	 * 
	 * @param input
	 * @param produtoEdicao
	 */
	private void gerarNovoLancamentoParcial(EMS0136Input input, 
			ProdutoEdicao produtoEdicao) {
		
		StatusLancamentoParcial status = this.obterStatusLancamentoParcial(
				input);	
		
		// Novo Lançamento Parcial:
		LancamentoParcial lancamentoParcial = new LancamentoParcial();
		lancamentoParcial.setProdutoEdicao(produtoEdicao);
		lancamentoParcial.setLancamentoInicial(input.getDataLancamento());
		lancamentoParcial.setRecolhimentoFinal(input.getDataRecolhimento());
		lancamentoParcial.setStatus(status);		
		
		// Novo Lançamento:
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
		
		TipoLancamentoParcial tipoLancamentoParcial = 
				this.obterTipoLancamentoParcial(input);
		
		// Novo Período Lançamento Parcial:
		PeriodoLancamentoParcial pLancamentoParcial = new PeriodoLancamentoParcial();
		pLancamentoParcial.setLancamentoParcial(lancamentoParcial);
		pLancamentoParcial.setLancamento(lancamento);
		pLancamentoParcial.setStatus(status);
		pLancamentoParcial.setNumeroPeriodo(input.getNumeroPeriodo());
		pLancamentoParcial.setTipo(tipoLancamentoParcial);
		
		// Persistir os novos objetos:
		this.getSession().persist(lancamentoParcial);
		this.getSession().persist(lancamento);
		this.getSession().persist(pLancamentoParcial);
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
	 * Obtém  o Lançamento associado ao produto Edição.
	 * 
	 * @param produtoEdicao
	 * @return
	 */
	private Lancamento obterLancamento(ProdutoEdicao produtoEdicao) {

		Criteria criteria = getSession().createCriteria(Lancamento.class);
		criteria.add(Restrictions.eq("produtoEdicao", produtoEdicao));
		criteria.addOrder(Order.asc("dataLancamentoPrevista"));
		
		Lancamento lancamento = (Lancamento) criteria.list().get(0);
		
		return lancamento;
	}
		
	
	@Override
	public void posProcess() {
		// TODO Auto-generated method stub

	}

}
