package br.com.abril.nds.integracao.ems3100.processor;

import java.util.ArrayList;
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

import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.ExtratificacaoItem;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Component
public class EMS3100MessageProcessor extends AbstractRepository implements MessageProcessor {

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
		
		String codigoDistribuidor = (String) message.getHeader().get(MessageHeaderProperties.CODIGO_DISTRIBUIDOR.getValue());
		
		Date dataOperacao = distribuidorService.obter().getDataOperacao();
						
		List<ExtratificacaoItem> itens = this.obterMovimentoEstoqueCota(codigoDistribuidor);
		
		if(itens == null || itens.isEmpty()) {
			return;
		}
		
		CouchDbClient cdbc = null;
		
		try {
			
			cdbc = this.getCouchDBClient(codigoDistribuidor, true);
			Date dataCriacao = new Date();
			for(ExtratificacaoItem item : itens) {
				
				item.setTipoDocumento("EMS3100");
				item.setDataOperacao(dataOperacao);
				item.setCodigoDistribuidor(codigoDistribuidor);
				item.setDataCriacao(dataCriacao);
			}
			
			cdbc.bulk(itens, true);
			
		} catch(Exception e) {
			
            LOGGER.error("Erro executando importação do Extratificado do movimento estoque cota.", e);
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
	private List<ExtratificacaoItem> obterMovimentoEstoqueCota(String codigoDistribuidor) {

		StringBuilder hql = new StringBuilder();
		
		hql.append("select c.id as idCota ")
		.append(", c.numero_cota as numeroCota ")
		.append(", p.codigo as codigoProduto ")
		.append(", p.nome as nomeProduto ")
		.append(", pe.numero_edicao as numeroEdicao ")
		.append(", (sum(case when tm.OPERACAO_ESTOQUE = 'ENTRADA' then mec.qtde else 0 end) ")
		.append("	- sum(case when (tm.OPERACAO_ESTOQUE = 'SAIDA' AND tm.GRUPO_MOVIMENTO_ESTOQUE <> 'ENVIO_ENCALHE') then mec.qtde else 0 end)) as qtdReparte ")
	 	.append(", sum(case when (tm.OPERACAO_ESTOQUE = 'SAIDA' AND tm.GRUPO_MOVIMENTO_ESTOQUE = 'ENVIO_ENCALHE') then mec.qtde else 0 end) as qtdEncalhe ")
		.append(", (sum(case when tm.OPERACAO_ESTOQUE = 'ENTRADA' then mec.qtde else 0 end)  ")
		.append("	- sum(case when (tm.OPERACAO_ESTOQUE = 'SAIDA' AND tm.GRUPO_MOVIMENTO_ESTOQUE <> 'ENVIO_ENCALHE') then mec.qtde else 0 end) ") 
		.append("		- sum(case when (tm.OPERACAO_ESTOQUE = 'SAIDA' AND tm.GRUPO_MOVIMENTO_ESTOQUE = 'ENVIO_ENCALHE') then mec.qtde else 0 end)) as qtdVenda ")
		.append(", (select count(0) from pdv pdv where pdv.cota_id = mec.cota_id) as qtdPDV ")
		.append(" from movimento_estoque_cota mec ")
		.append(" inner join tipo_movimento tm on tm.id = mec.TIPO_MOVIMENTO_ID ")
		.append(" inner join lancamento l on l.PRODUTO_EDICAO_ID = mec.PRODUTO_EDICAO_ID ")
		.append(" inner join cota c on c.id = mec.COTA_ID ")
		.append(" inner join produto_edicao pe on pe.id = mec.PRODUTO_EDICAO_ID ")
		.append(" inner join produto p on p.id = pe.PRODUTO_ID ")
		.append(" where 1 = 1 ")
		.append(" and (select cod_distribuidor_dinap from distribuidor) = :codigoDistribuidor ")
		.append(" and l.DATA_REC_DISTRIB = (select max(data_fechamento) data_fechamento ")
		.append(" 		from fechamento_diario fd ")
		.append(" 		where fd.data_fechamento < (select data_operacao from distribuidor)) ")
		.append(" and l.status = :statusFechado ")
		.append(" group by c.id, p.codigo, pe.NUMERO_EDICAO")
		.append(" order by c.NUMERO_COTA, p.codigo, pe.NUMERO_EDICAO ");
		
		Query query = this.getSession().createSQLQuery(hql.toString());
		query.setParameter("codigoDistribuidor", codigoDistribuidor);
		query.setParameter("statusFechado", StatusLancamento.FECHADO.name());
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