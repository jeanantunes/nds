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

	public static EMS0107MessageProcessor getInstance() {
		return instance;
	}
	
	private EMS0107MessageProcessor() {

	}
	
	@Override
	public void processMessage(Message message) {
		
		EMS0107Input input = (EMS0107Input) message.getBody();
		
		if (input != null) {
			
			String codigoPublicacao = input.getCodigoPublicacao();
			Long edicao = input.getEdicao();
			Long codigoCota = input.getCodigoCota();
			
			List<ProdutoEdicao> listaProdutoEdicao = 
				this.obterProdutoEdicaoPor(codigoPublicacao, edicao);
			
			if (listaProdutoEdicao == null || listaProdutoEdicao.isEmpty()) {
				return;
			}

			Cota cota = obterCota(codigoCota);

			Lancamento lancamento = 
				this.getLancamento(codigoPublicacao, edicao);

			List<Estudo> listaEstudos = 
				this.getEstudosSalvos(
					lancamento.getProdutoEdicao().getId(), 
					lancamento.getDataLancamentoPrevista());
			
			if (listaEstudos == null || listaEstudos.isEmpty()) {
				this.ndsiLoggerFactory.getLogger().logError(
					message, EventoExecucaoEnum.HIERARQUIA, "NAO ENCONTROU Produto/Edição e Estudos");
			} else {
				
				for (Estudo estudo : listaEstudos) {
					
					EstudoCota estudoCota = new EstudoCota();
					
					estudoCota.setCota(cota);
					estudoCota.setEstudo(estudo);
					estudoCota.setQtdeEfetiva( BigInteger.valueOf(input.getQuantidadeReparte()) );
					estudoCota.setQtdePrevista( BigInteger.valueOf(input.getQuantidadeReparte()) );
					
					this.getSession().persist(estudoCota);
				}
			}
			
		} else {
			this.ndsiLoggerFactory.getLogger().logError(
				message, EventoExecucaoEnum.ERRO_INFRA, "NAO ENCONTROU o Arquivo");
		}
	}	
	
	@SuppressWarnings("unchecked")
	private List<ProdutoEdicao> obterProdutoEdicaoPor(String codigoPublicacao, Long edicao) {
		
		try {
			
			Criteria criteria = 
				this.getSession().createCriteria(ProdutoEdicao.class, "produtoEdicao");

			criteria.createAlias("produtoEdicao.produto", "produto");
			criteria.setFetchMode("produto", FetchMode.JOIN);
			
			criteria.add(Restrictions.eq("produto.codigo", codigoPublicacao));
			criteria.add(Restrictions.eq("produtoEdicao.numeroEdicao", edicao));

			return criteria.list();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private Cota obterCota(Long codigoCota) {
		
		return (Cota) this.getSession().get(Cota.class, codigoCota);
	}

	private Lancamento getLancamento(String codigoPublicacao, Long edicao) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT lcto FROM Lancamento lcto ");
		sql.append("			JOIN FETCH lcto.produtoEdicao pe ");
		sql.append("			JOIN FETCH pe.produto p ");
		sql.append("WHERE ");
		sql.append("	pe.numeroEdicao = :numeroEdicao ");
		sql.append("	AND p.codigo = :codigoProduto ");
		sql.append("	AND lcto.dataLancamentoPrevista >= current_date() ");
		sql.append("ORDER BY lcto.dataLancamentoPrevista DESC");

		Query query = getSession().createQuery(sql.toString());
		
		query.setMaxResults(1);

		query.setParameter("numeroEdicao", edicao);
		query.setParameter("codigoProduto", codigoPublicacao);

		return (Lancamento) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	private List<Estudo> getEstudosSalvos(Long idProdutoEdicao, Date dataLancamentoPrevista) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT e ");
		sql.append("FROM Estudo e ");
		sql.append("	JOIN FETCH e.produtoEdicao pe ");
		sql.append("WHERE ");
		sql.append("	pe.id = :produtoEdicaoId ");
		sql.append("	AND e.dataLancamento = :dataLancamento ");

		Query query = getSession().createQuery(sql.toString());

		query.setParameter("produtoEdicaoId", idProdutoEdicao);
		query.setParameter("dataLancamento", dataLancamentoPrevista);

		return query.list();
	}
	
}