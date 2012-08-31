package br.com.abril.nds.integracao.ems0107.processor;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0107.inbound.EMS0107Input;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.impl.AbstractRepository;

@Component
public class EMS0107MessageProcessor extends AbstractRepository implements MessageProcessor  {
	
	private static EMS0107MessageProcessor instance = new EMS0107MessageProcessor();
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@Autowired
	private DistribuidorService distribuidorService;
	
	public static EMS0107MessageProcessor getInstance() {
		return instance;
	}
	
	private EMS0107MessageProcessor() {

	}
	
	@Override
	public void preProcess() {
		// TODO Auto-generated method stub
	}

	@Override
	public void preProcess(Message message) {
		// TODO Auto-generated method stub
	}
		
	@Override
	public void processMessage(Message message) {
		
		EMS0107Input input = (EMS0107Input) message.getBody();
		if (input == null) {
			this.ndsiLoggerFactory.getLogger().logError(
					message, EventoExecucaoEnum.ERRO_INFRA, "NAO ENCONTROU o Arquivo");
			return;
		}
		
		String codigoPublicacao = input.getCodigoPublicacao();
		Long edicao = input.getEdicao();
		ProdutoEdicao produtoEdicao = this.obterProdutoEdicao(codigoPublicacao,
				edicao);
		if (produtoEdicao == null) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.HIERARQUIA,
					"NAO ENCONTROU ProdutoEdicao");
			return;
		}
			
		Lancamento lancamento = this.getLancamentoPrevistoMaisProximo(
				produtoEdicao);
		if (lancamento == null) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.HIERARQUIA,
					"NAO ENCONTROU Lancamento");
			return;
		}
		
		Estudo estudo = lancamento.getEstudo();
		if (estudo == null) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.HIERARQUIA,
					"NAO ENCONTROU Estudo");
			return;
		}
		
		Integer numeroCota = input.getCodigoCota();
		Cota cota = this.obterCota(numeroCota);
		boolean hasEstudoCota = this.hasEstudoCota(estudo, cota);
		if (hasEstudoCota) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.HIERARQUIA,
					"JA EXISTE EstudoCota para a numero de Cota: " + numeroCota);
			return;
		}
		
		// Novo EstudoCota:
		BigInteger qtdReparte = BigInteger.valueOf(input.getQuantidadeReparte());
		EstudoCota eCota = new EstudoCota();
		eCota.setEstudo(estudo);
		eCota.setCota(cota);
		eCota.setQtdePrevista(qtdReparte);
		eCota.setQtdeEfetiva(qtdReparte);
		
		this.getSession().persist(eCota);
	}	
	
	/**
	 * Obtém o Produto Edição cadastrado previamente.
	 * 
	 * @param codigoPublicacao Código da Publicação.
	 * @param edicao Número da Edição.
	 * 
	 * @return
	 */
	private ProdutoEdicao obterProdutoEdicao(String codigoPublicacao,
			Long edicao) {

		try {

			Criteria criteria = this.getSession().createCriteria(
					ProdutoEdicao.class, "produtoEdicao");

			criteria.createAlias("produtoEdicao.produto", "produto");
			criteria.setFetchMode("produto", FetchMode.JOIN);

			criteria.add(Restrictions.eq("produto.codigo", codigoPublicacao));
			criteria.add(Restrictions.eq("produtoEdicao.numeroEdicao", edicao));

			return (ProdutoEdicao) criteria.uniqueResult();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private Cota obterCota(Integer numeroCota) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT co FROM Cota co ");
		sql.append(" WHERE co.numeroCota = :numeroCota ");

		Query query = getSession().createQuery(sql.toString());
		query.setParameter("numeroCota", numeroCota);
		query.setMaxResults(1);

		return (Cota) query.uniqueResult();		
	}

	/**
	 * Obtém o Lançamento com data de lançamento mais próximo do dia corrente.
	 *  
	 * @param produtoEdicao
	 * @return
	 */
	private Lancamento getLancamentoPrevistoMaisProximo(
			ProdutoEdicao produtoEdicao) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT lcto FROM Lancamento lcto ");
		sql.append("      JOIN FETCH lcto.produtoEdicao pe ");
		sql.append("    WHERE pe = :produtoEdicao ");
		sql.append("      AND lcto.dataLancamentoPrevista >= :dataOperacao ");
		sql.append(" ORDER BY lcto.dataLancamentoPrevista ASC");
		
		Query query = getSession().createQuery(sql.toString());
		
		Date dataOperacao = distribuidorService.obter().getDataOperacao();
		query.setParameter("produtoEdicao", produtoEdicao);
		query.setDate("dataOperacao", dataOperacao);
		
		query.setMaxResults(1);
		query.setFetchSize(1);
		
		return (Lancamento) query.uniqueResult();
	}
	
	/**
	 * Verifica se já existe EstudoCota cadastrado.
	 * 
	 * @param estudo
	 * @param cota
	 * @return true: Já existe pelo menos 1 EstudoCota cadastrado;<br>
	 * false: Não existe nenhum EstudoCota para o Estudo e Cota passado;
	 */
	private boolean hasEstudoCota(Estudo estudo, Cota cota) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT COUNT(ec) FROM EstudoCota ec " );
		hql.append("  WHERE ec.estudo = :estudo AND ec.cota = :cota");
		
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("estudo", estudo);
		query.setParameter("cota", cota);
		
		Long qtd = (Long) query.uniqueResult();
		return (qtd != null && qtd.intValue() > 0);
	}
	
	@Override
	public void posProcess() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void posProcess(Message message) {
		
		/*
		 * Regras de validação para EMS-107:
		 * 
		 * 01) Não deve existir Estudo sem EstudoCota:
		 * Todo Estudo deve possuir pelo menos um (ou mais) EstudoCota;
		 * 
		 * 02) A soma de todos os EstudoCota de um Estudo deve ser igual ao 
		 * valor contido na "quantidade Efetiva" do respectivo Estudo.
		 */
		
		// 01) Verificar se existe algum Estudo sem EstudoCota;
		StringBuilder hqlEstudoSemEstudoCota = new StringBuilder();
		hqlEstudoSemEstudoCota.append(" SELECT e FROM Estudo e " );
		hqlEstudoSemEstudoCota.append("  WHERE NOT EXISTS( FROM EstudoCota ec WHERE ec.estudo = e)");
		
		Query query = getSession().createQuery(hqlEstudoSemEstudoCota.toString());
		List<Estudo> lstEstudos = query.list();
		if (lstEstudos != null && !lstEstudos.isEmpty()) {
			for (Estudo estudo : lstEstudos) {
				
				this.ndsiLoggerFactory.getLogger().logError(message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"NAO EXISTE EstudoCota para a publicacao: " + estudo.getProdutoEdicao());
				this.getSession().delete(estudo);
			}
		}
		
		
		// 02) Verificar se a soma de todos os qtdeEfetiva e qtdePrevista de um
		// EstudoCota batem com a qtdeReparte do respectivo Estudo
		
	}
	
}