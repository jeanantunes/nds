package br.com.abril.nds.integracao.ems0126.processor;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0126Input;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;

/**
 * @author Jones.Costa
 * @version 1.0
 */
@Component
public class EMS0126MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void processMessage(Message message) {

		EMS0126Input input = (EMS0126Input) message.getBody();
		
		if(input.getCodigoProduto()==null || input.getCodigoProduto().trim().equals("")){
		
			ndsiLoggerFactory.getLogger().logError(
				message,
				EventoExecucaoEnum.RELACIONAMENTO,
				"Código do Produto Nulo ou Vazio."
				+ " Produto "+ input.getCodigoProduto());
			return;
		}
		
		if(input.getEdicao()==null || input.getEdicao().intValue()==0){
			
			ndsiLoggerFactory.getLogger().logError(
				message,
				EventoExecucaoEnum.RELACIONAMENTO,
				"Edição do Produto Nula ou Vazia."
						+ " Produto "+ input.getCodigoProduto() 
						+ " Edição "+ input.getEdicao());
			
			return;
		}

		// Localizar Produto/Edicao
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT prodEdicao FROM ProdutoEdicao prodEdicao ");
		sql.append("			JOIN FETCH prodEdicao.produto prodCod");
		sql.append(" WHERE ");
		sql.append("	prodCod.codigo = :codigoProduto ");
		sql.append(" AND	prodEdicao.numeroEdicao = :numeroEdicao ");

		Query query = getSession().createQuery(sql.toString());
		query.setParameter("codigoProduto", input.getCodigoProduto());
		query.setParameter("numeroEdicao", input.getEdicao());

		ProdutoEdicao produtoEdicao = (ProdutoEdicao) query.uniqueResult();

		if (null != produtoEdicao) {

			
			 if(input.getCodigoBarras()!=null && !input.getCodigoBarras().trim().equals("") ){
				
				
				if(produtoEdicao.getCodigoDeBarras() ==null || produtoEdicao.getCodigoDeBarras().trim().equals("")){
					produtoEdicao.setCodigoDeBarras("0");
				}
				
				if(new BigInteger(input.getCodigoBarras()).longValue() >0 && new BigInteger(produtoEdicao.getCodigoDeBarras()).longValue()!= new BigInteger(input.getCodigoBarras()).longValue()){
			
				String codigoBarras =input.getCodigoBarras();
				if (produtoEdicao.getProduto().getTipoSegmentoProduto() == null || !"IMPORTADAS".equals(produtoEdicao.getProduto().getTipoSegmentoProduto().getDescricao()))
					 codigoBarras = new BigInteger(input.getCodigoBarras()).toString();
					
				ndsiLoggerFactory.getLogger().logInfo(message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Alteração do Código de Barras"
						+" de "+produtoEdicao.getCodigoDeBarras()
						+" para "+codigoBarras
						+" Produto "+input.getCodigoProduto()
						+" Edição " + input.getEdicao() );
				
				
				produtoEdicao.setCodigoDeBarras(codigoBarras);
				this.getSession().merge(produtoEdicao);
				}
			 }

		} else {
			ndsiLoggerFactory.getLogger().logError(
					message,
					EventoExecucaoEnum.RELACIONAMENTO,
					" Produto "+ input.getCodigoProduto() 
					+ " Edição "+ input.getEdicao()
					+" não encontrado.");

		}

	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}
