package br.com.abril.nds.integracao.ems0126.processor;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0126Input;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
/**
 * @author Jones.Costa
 * @version 1.0
 */
@Component
public class EMS0126MessageProcessor implements MessageProcessor{

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	
	@Override
	public void processMessage(Message message) {
	
		EMS0126Input input = (EMS0126Input) message.getBody();
		
		//Localizar Produto/Edicao
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT prodEdicao FROM ProdutoEdicao prodEdicao ");
		sql.append("			JOIN FETCH prodEdicao.produto prodCod");
		sql.append(" WHERE ");
		sql.append("	prodCod.codigo = :codigoProduto ");
		sql.append(" AND	prodEdicao.numeroEdicao = :numeroEdicao ");
		
		Query query = entityManager.createQuery(sql.toString());		
		query.setParameter("codigoProduto", input.getCodigoProduto());
		query.setParameter("numeroEdicao", input.getEdicao());
		
		try{
		ProdutoEdicao produtoEdicao = (ProdutoEdicao) query.getSingleResult();
		
		//Inserir codigo de barras
		produtoEdicao.setCodigoDeBarras(input.getCodigoBarras());
		
		}
		catch(NoResultException e){
		    ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.RELACIONAMENTO,"Nenhum resultado encontrado para Produto: "+input.getCodigoProduto()+" e Edição: "+input.getEdicao()+" na tabela produto_edicao");
			e.printStackTrace();
			
			
			
		}
			
		
		
	}

}
