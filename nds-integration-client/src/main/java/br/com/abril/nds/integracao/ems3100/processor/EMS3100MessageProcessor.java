package br.com.abril.nds.integracao.ems3100.processor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.lightcouch.CouchDbClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.Extratificacao;
import br.com.abril.nds.integracao.model.canonic.ExtratificacaoItem;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Component
public class EMS3100MessageProcessor extends AbstractRepository implements MessageProcessor  {

	private static final Logger LOGGER = LoggerFactory.getLogger(EMS3100MessageProcessor.class);
	
	@Autowired
    private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		List<Object> objs = new ArrayList<Object>();
		Object dummyObj = new Object();
		objs.add(dummyObj);

		tempVar.set(objs);
	}
	
	@Override
	public void processMessage(Message message) {
		
		String codigoDistribuidor = distribuidorService.obter().getCodigoDistribuidorDinap();
		
		Date dataOperacao = distribuidorService.obter().getDataOperacao();
		
		Extratificacao extratificacao = new Extratificacao();
		
		extratificacao.setCodigoDistribuidor(codigoDistribuidor);
		
		List<ExtratificacaoItem> itens = this.obterMovimentoEstoqueCota(codigoDistribuidor, dataOperacao);
		
		CouchDbClient cdbc = null;
		
		try {
			
			cdbc = this.getCouchDBClient(codigoDistribuidor, true);
			
			extratificacao.setTipoDocumento("EMS3100");
			
			extratificacao.setDataOperacao(dataOperacao);
			
			extratificacao.setCodigoDistribuidor(codigoDistribuidor);
			
			extratificacao.setDataCriacao(new Date());
			
			extratificacao.setItens(itens);
			
			cdbc.save(extratificacao);
			
		} catch(Exception e) {
            LOGGER.error("Erro executando importação do Extificado do movimento estoque cota.", e);
		} finally {
			if (cdbc != null) {
				cdbc.shutdown();
			}			
		}		
	}

	@Override
	public void posProcess(Object tempVar) {

	}
			
	@SuppressWarnings("unchecked")
	private List<ExtratificacaoItem> obterMovimentoEstoqueCota(String distribuidor, Date dataOperacao) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select c.id as idCota ")
		.append(", c.numero_cota as numeroCota ")
		.append(", p.codigo as codigoProduto ")
		.append(", p.nome as nomeProduto ")
		.append(", pe.numero_edicao as numeroEdicao ")
		.append(", mec.data_aprovacao as dataAprovacao")
		.append(", (sum(case when tm.OPERACAO_ESTOQUE = 'ENTRADA' then mec.qtde else 0 end) ")
		.append("	- sum(case when (tm.OPERACAO_ESTOQUE = 'SAIDA' AND tm.GRUPO_MOVIMENTO_ESTOQUE <> 'ENVIO_ENCALHE') then mec.qtde else 0 end)) as qtdReparte ")
	 	.append(", sum(case when (tm.OPERACAO_ESTOQUE = 'SAIDA' AND tm.GRUPO_MOVIMENTO_ESTOQUE = 'ENVIO_ENCALHE') then mec.qtde else 0 end) as qtdEncalhe ")
		.append(", (sum(case when tm.OPERACAO_ESTOQUE = 'ENTRADA' then mec.qtde else 0 end)  ")
		.append("	- sum(case when (tm.OPERACAO_ESTOQUE = 'SAIDA' AND tm.GRUPO_MOVIMENTO_ESTOQUE <> 'ENVIO_ENCALHE') then mec.qtde else 0 end) ") 
		.append("		- sum(case when (tm.OPERACAO_ESTOQUE = 'SAIDA' AND tm.GRUPO_MOVIMENTO_ESTOQUE = 'ENVIO_ENCALHE') then mec.qtde else 0 end)) as qtdVenda ")
		.append(", sum(case when tm.OPERACAO_ESTOQUE = 'SAIDA' then mec.qtde else 0 end) / sum(case when tm.OPERACAO_ESTOQUE = 'ENTRADA' then mec.qtde else 0 end) as percentualDesconto ")
		.append(" from movimento_estoque_cota mec ")
		.append(" inner join tipo_movimento tm on tm.id = mec.TIPO_MOVIMENTO_ID ")
		.append(" inner join lancamento l on l.PRODUTO_EDICAO_ID = mec.PRODUTO_EDICAO_ID ")
		.append(" inner join cota c on c.id = mec.COTA_ID ")
		.append(" inner join produto_edicao pe on pe.id = mec.PRODUTO_EDICAO_ID ")
		.append(" inner join produto p on p.id = pe.PRODUTO_ID ")
		.append(" where 1 = 1 ")
		.append("and l.DATA_REC_DISTRIB between '2014-12-01' and '2015-01-22' ")
		// .append("and l.DATA_REC_DISTRIB = :dataOperacao ")
		.append(" and l.status = 'FECHADO'")
		.append(" group by p.codigo, pe.NUMERO_EDICAO, l.id ")
		.append(" order by c.NUMERO_COTA, p.codigo, pe.NUMERO_EDICAO ");
		
		Query query = this.getSession().createSQLQuery(hql.toString());
		// query.setParameter("dataOperacao", dataOperacao);
		query.setResultTransformer(new AliasToBeanResultTransformer(ExtratificacaoItem.class));
		
		return query.list();
	}
	
	protected boolean closeSessionIcd(Session session) {

		try {
			if(session != null && session.isOpen()) {
				session.close();
				return true;
			}
		} catch(Exception e) {
            LOGGER.error("Erro ao obter sessão do Hibernate.", e);
		}
		return false;
	}
}