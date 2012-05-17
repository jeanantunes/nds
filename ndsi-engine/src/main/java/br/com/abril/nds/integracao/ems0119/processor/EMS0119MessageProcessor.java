package br.com.abril.nds.integracao.ems0119.processor;


import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.model.canonic.EMS0119Input;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;

import com.ancientprogramming.fixedformat4j.format.FixedFormatManager;


@Component
public class EMS0119MessageProcessor implements MessageProcessor{

	@PersistenceContext
	private EntityManager entityManager;
	
	public EMS0119MessageProcessor() {

	}
	
	@Override
	public void processMessage(Message message) {
		
		EMS0119Input input = (EMS0119Input) message.getBody();
	
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT pe ");
		sql.append("FROM ProdutoEdicao pe ");
		sql.append("JOIN FETCH pe.produto p");
		sql.append("WHERE p.codigo = :codigoProduto");
		//sql.append("AND  ");
		
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("codigoProduto", input.getCodigoDaPublicacao());
		
		try {
			
			ProdutoEdicao produtoEdicao = (ProdutoEdicao) query.getSingleResult();

			if(produtoEdicao.getDesconto() != input.getDesconto() ||
				produtoEdicao.getProduto().getNome().equals(input.getNomeDaPublicacao()) || 
				produtoEdicao.getProduto().getPeriodicidade().toString().equals(input.getPeriodicidade()) ||
				produtoEdicao.getProduto().getTipoProduto().getId() == input.getTipoDePublicacao() ||
				produtoEdicao.getProduto().getEditor().getCodigo() == input.getCodigoDoEditor() ||
				produtoEdicao.getPacotePadrao() == input.getPacotePadrao()){
				
				produtoEdicao.setDesconto(input.getDesconto());
				produtoEdicao.getProduto().setNome(input.getNomeDaPublicacao());
				
				produtoEdicao.getProduto().setPeriodicidade(PeriodicidadeProduto.valueOf(input.getPeriodicidade()));
				produtoEdicao.getProduto().getTipoProduto().setId(input.getTipoDePublicacao());
				produtoEdicao.getProduto().getEditor().setCodigo(input.getCodigoDoEditor());
				produtoEdicao.setPacotePadrao(input.getPacotePadrao());
			}
			
		} catch (NoResultException e) {
			
		}
	}

}
