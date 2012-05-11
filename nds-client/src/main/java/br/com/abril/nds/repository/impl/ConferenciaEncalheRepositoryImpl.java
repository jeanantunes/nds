package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.repository.ConferenciaEncalheRepository;

@Repository
public class ConferenciaEncalheRepositoryImpl extends
		AbstractRepository<ConferenciaEncalhe, Long> implements
		ConferenciaEncalheRepository {

	public ConferenciaEncalheRepositoryImpl() {
		super(ConferenciaEncalhe.class);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.ConferenciaEncalheRepository#obterListaConferenciaEncalheDTO(java.lang.Long)
	 */
	public List<ConferenciaEncalheDTO> obterListaConferenciaEncalheDTO(Long idControleConferenciaEncalheCota) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		
		hql.append(" conferenciaEncalhe.movimentoEstoqueCota.qtde as qtdExemplar,  ");
		hql.append(" conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.codigoDeBarras as codigoDeBarras, 	");
		hql.append(" conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.codigoSM as codigoSM, 				");
		hql.append(" conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.produto.codigo as codigo, 			");
		hql.append(" conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.produto.nome as nomeProduto, 		");
		hql.append(" conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.numeroEdicao as numeroEdicao, 		");
		hql.append(" conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.precoVenda as precoCapa, 			");
		
		hql.append(" sum( conferenciaEncalhe.movimentoEstoqueCota.qtde * 								");
		hql.append(" ( conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.precoVenda  				");
		hql.append(" - conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.desconto) ) as valorTotal, ");
		
		hql.append(" conferenciaEncalhe.observacao as observacao, ");
		hql.append(" conferenciaEncalhe.juramentada as juramentada ");
		
		hql.append(" from ConferenciaEncalhe conferenciaEncalhe ");
		
		hql.append(" where ");
		
		hql.append(" conferenciaEncalhe.controleConferenciaEncalheCota.id = :idControleConferenciaEncalheCota ");
		
		hql.append(" group by ");

		hql.append(" conferenciaEncalhe.movimentoEstoqueCota.qtde,  						");
		hql.append(" conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.codigoDeBarras, 	");
		hql.append(" conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.codigoSM, 		");
		hql.append(" conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.produto.codigo, 	");
		hql.append(" conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.produto.nome, 	");
		hql.append(" conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.numeroEdicao, 	");
		hql.append(" conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.precoVenda, 		");
		hql.append(" conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.desconto, 		");
		hql.append(" conferenciaEncalhe.observacao, ");
		hql.append(" conferenciaEncalhe.juramentada ");
		
		Query query =  this.getSession().createQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(ConferenciaEncalheDTO.class));
		
		query.setParameter("idControleConferenciaEncalheCota", idControleConferenciaEncalheCota);
		
		return query.list();
		
		
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.ConferenciaEncalheRepository#obterValorTotalConferenciaEncalheCota(java.lang.Long)
	 */
	public BigDecimal obterValorTotalConferenciaEncalheCota(Long idControleConferenciaEncalheCota) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		
		hql.append(" sum( conferenciaEncalhe.movimentoEstoqueCota.qtde * 				 ");
		hql.append(" ( conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.precoVenda  ");
		hql.append(" - conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.desconto) ) ");
		
		hql.append(" from ConferenciaEncalhe conferenciaEncalhe ");
		
		hql.append(" where ");
		
		hql.append(" conferenciaEncalhe.controleConferenciaEncalheCota.id = :idControleConferenciaEncalheCota ");
		
		Query query =  this.getSession().createQuery(hql.toString());
		
		query.setParameter("idControleConferenciaEncalheCota", idControleConferenciaEncalheCota);
		
		return (BigDecimal) query.uniqueResult();
		
	}
	
}
