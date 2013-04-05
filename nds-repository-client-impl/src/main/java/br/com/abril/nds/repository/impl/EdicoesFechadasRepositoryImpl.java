package br.com.abril.nds.repository.impl;
 
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.RegistroEdicoesFechadasVO;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EdicoesFechadasRepository;

@Repository
public class EdicoesFechadasRepositoryImpl extends AbstractRepositoryModel<MovimentoEstoque, Long>  implements EdicoesFechadasRepository {

	/**
	 * Construtor padrão.
	 */
	public EdicoesFechadasRepositoryImpl() {
		super(MovimentoEstoque.class);
	}

	

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.EdicoesFechadasRepository#obterResultadoTotalEdicoesFechadas(java.util.Date, java.util.Date, java.lang.String)
	 */
	@Override
	public BigInteger obterResultadoTotalEdicoesFechadas(Date dataDe, Date dataAte, Long idFornecedor) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT ( sum(movimentoEstoque.qtde) ) ");
		
		hql.append(" FROM MovimentoEstoque AS movimentoEstoque ")
			.append(" LEFT JOIN movimentoEstoque.estoqueProduto.produtoEdicao AS produtoEdicao ")
			.append(" LEFT JOIN produtoEdicao.produto AS produto ")
			.append(" LEFT JOIN produto.fornecedores AS fornecedores ")
			.append(" LEFT JOIN fornecedores.juridica AS juridica ");

		//.append(" LEFT JOIN produtoEdicao.lancamentos AS lancamentos ")

		hql.append(" WHERE ( produtoEdicao.dataDesativacao BETWEEN :dataDe AND :dataAte ) ");
		
		if (idFornecedor!=null) {
			hql.append(" AND fornecedores.id = :idFornecedor ");
		}

		Query query = this.getSession().createQuery(hql.toString());

    	query.setParameter("dataDe", dataDe);
    	query.setParameter("dataAte", dataAte);

    	if(idFornecedor!=null){
	    	query.setParameter("idFornecedor", idFornecedor);
	    }
		
		return (BigInteger) query.uniqueResult();
	}

	

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.EdicoesFechadasRepository#obterResultadoEdicoesFechadas(java.util.Date, java.util.Date, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RegistroEdicoesFechadasVO> obterResultadoEdicoesFechadas(Date dataDe, Date dataAte, Long idFornecedor, String sortorder, String sortname, Integer firstResult, Integer maxResults) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT produtoEdicao.id as idProdutoEdicao, produto.codigo as codigoProduto , ")
			.append("   produto.nome as nomeProduto, ")
			.append("   produtoEdicao.numeroEdicao as edicaoProduto, " )
			.append("   juridica.nomeFantasia as nomeFornecedor, " )
			.append("   (SELECT min(lancamentos.dataRecolhimentoDistribuidor) from Lancamento AS lancamentos WHERE lancamentos.produtoEdicao.id = fechamentoEncalhe.fechamentoEncalhePK.produtoEdicao.id) as dataLancamento , ")
			.append("   produtoEdicao.parcial as parcial, ")
			.append("   (SELECT max(lancamentos.dataRecolhimentoDistribuidor) from Lancamento AS lancamentos WHERE lancamentos.produtoEdicao.id = fechamentoEncalhe.fechamentoEncalhePK.produtoEdicao.id)  as dataRecolhimento, ")
			.append("    cast( sum(fechamentoEncalhe.quantidade) as big_integer ) as saldo");
		
		hql.append(" FROM FechamentoEncalhe AS fechamentoEncalhe ")
			.append(" inner JOIN fechamentoEncalhe.fechamentoEncalhePK.produtoEdicao AS produtoEdicao ")
			.append(" inner JOIN produtoEdicao.produto AS produto ")
			.append(" inner JOIN produto.fornecedores AS fornecedores ")
			.append(" inner JOIN fornecedores.juridica AS juridica ");
		hql.append(" WHERE ( fechamentoEncalhe.fechamentoEncalhePK.dataEncalhe BETWEEN :dataDe AND :dataAte ) ");
		
		if (idFornecedor !=  null) {
			hql.append(" AND fornecedores.id = :idFornecedor ");
		}
		
		hql.append(" GROUP BY produtoEdicao.id  ");
		
		if(sortname != null){
			hql.append("ORDER BY ").append(sortname).append(" ").append(sortorder);
		}

		Query query = this.getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(RegistroEdicoesFechadasVO.class));

    	query.setParameter("dataDe", dataDe);
    	query.setParameter("dataAte", dataAte);

    	if(idFornecedor !=  null){
	    	query.setParameter("idFornecedor", idFornecedor);
	    }		
    	
    	if (firstResult!=null) {
			query.setFirstResult(firstResult);
		}
		if (maxResults!=null) {
			query.setMaxResults(maxResults);
		}
		return query.list();
	}
	
	@Override
	public Long quantidadeResultadoEdicoesFechadas(Date dataDe, Date dataAte, Long idFornecedor){
		
		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT produtoEdicao.id ");

		hql.append(" FROM FechamentoEncalhe AS fechamentoEncalhe ")
				.append(" inner JOIN fechamentoEncalhe.fechamentoEncalhePK.produtoEdicao AS produtoEdicao ")
				.append(" inner JOIN produtoEdicao.produto AS produto ")
				.append(" inner JOIN produto.fornecedores AS fornecedores ")
				.append(" inner JOIN fornecedores.juridica AS juridica ");
		hql.append(" WHERE ( fechamentoEncalhe.fechamentoEncalhePK.dataEncalhe BETWEEN :dataDe AND :dataAte ) ");

		if (idFornecedor != null) {
			hql.append(" AND fornecedores.id = :idFornecedor ");
		}

		hql.append(" GROUP BY produtoEdicao.id ");

		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("dataDe", dataDe);
		query.setParameter("dataAte", dataAte);

		if (idFornecedor != null) {
			query.setParameter("idFornecedor", idFornecedor);
		}

		Integer quantidade = query.list().size();
		
		return quantidade.longValue();
		
	}

}
