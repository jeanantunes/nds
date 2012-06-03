package br.com.abril.nds.repository.impl;
 
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.RegistroEdicoesFechadasVO;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.repository.EdicoesFechadasRepository;

@Repository
public class EdicoesFechadasRepositoryImpl extends AbstractRepository<MovimentoEstoque, Long>  implements EdicoesFechadasRepository {

	/**
	 * Construtor padrão.
	 */
	public EdicoesFechadasRepositoryImpl() {
		super(MovimentoEstoque.class);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.EdicoesFechadasRepository#obterResultadoTotalEdicoesFechadas(java.util.Date, java.util.Date)
	 */
	@Override
	public BigDecimal obterResultadoTotalEdicoesFechadas(Date dataDe, Date dataAte) {
		return obterResultadoTotalEdicoesFechadas(dataDe, dataAte, "");
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.EdicoesFechadasRepository#obterResultadoTotalEdicoesFechadas(java.util.Date, java.util.Date, java.lang.String)
	 */
	@Override
	public BigDecimal obterResultadoTotalEdicoesFechadas(Date dataDe, Date dataAte, String codigoFornecedor) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT ( sum(movimentoEstoque.qtde) ) ");
		
		hql.append(" FROM MovimentoEstoque AS movimentoEstoque ")
			.append(" LEFT JOIN movimentoEstoque.estoqueProduto.produtoEdicao AS produtoEdicao ")
			.append(" LEFT JOIN produtoEdicao.produto AS produto ")
			.append(" LEFT JOIN produto.fornecedores AS fornecedores ")
			.append(" LEFT JOIN fornecedores.juridica AS juridica ");

		//.append(" LEFT JOIN produtoEdicao.lancamentos AS lancamentos ")

		hql.append(" WHERE ( produtoEdicao.dataDesativacao BETWEEN :dataDe AND :dataAte ) ");
		
		if (!codigoFornecedor.isEmpty()) {
			hql.append(" AND fornecedores.id = :codigoFornecedor ");
		}

		Query query = this.getSession().createQuery(hql.toString());

    	query.setParameter("dataDe", dataDe);
    	query.setParameter("dataAte", dataAte);

    	if(!codigoFornecedor.isEmpty()){
	    	query.setParameter("codigoFornecedor", Long.parseLong(codigoFornecedor));
	    }
		
		return (BigDecimal) query.uniqueResult();
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.EdicoesFechadasRepository#obterResultadoEdicoesFechadas(java.util.Date, java.util.Date)
	 */
	@Override
	public List<RegistroEdicoesFechadasVO> obterResultadoEdicoesFechadas(Date dataDe, Date dataAte) {
		return obterResultadoEdicoesFechadas(dataDe, dataAte, "");
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.EdicoesFechadasRepository#obterResultadoEdicoesFechadas(java.util.Date, java.util.Date, java.lang.String)
	 */
	@Override
	public List<RegistroEdicoesFechadasVO> obterResultadoEdicoesFechadas(Date dataDe, Date dataAte, String codigoFornecedor) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT new ").append(RegistroEdicoesFechadasVO.class.getCanonicalName())
			.append(" ( produto.codigo , ")
			.append("   produto.nome , ")
			.append("   produtoEdicao.numeroEdicao, " )
			.append("   juridica.nomeFantasia, " )
			.append("   (SELECT min(lancamentos.dataRecolhimentoDistribuidor) from Lancamento AS lancamentos WHERE lancamentos.produtoEdicao = movimentoEstoque.estoqueProduto.produtoEdicao) , ")
			.append("   produtoEdicao.parcial, ")
			.append("   (SELECT max(lancamentos.dataRecolhimentoDistribuidor) from Lancamento AS lancamentos WHERE lancamentos.produtoEdicao = movimentoEstoque.estoqueProduto.produtoEdicao) , ")
			.append("   ( sum(movimentoEstoque.qtde) ) ) ");

		/*.append("   min(lancamentos.dataRecolhimentoDistribuidor), ")
		.append("   max(lancamentos.dataRecolhimentoDistribuidor), ")*/

		hql.append(" FROM MovimentoEstoque AS movimentoEstoque ")
			.append(" LEFT JOIN movimentoEstoque.estoqueProduto.produtoEdicao AS produtoEdicao ")
			.append(" LEFT JOIN produtoEdicao.produto AS produto ")
			.append(" LEFT JOIN produto.fornecedores AS fornecedores ")
			.append(" LEFT JOIN fornecedores.juridica AS juridica ");

		//.append(" LEFT JOIN produtoEdicao.lancamentos AS lancamentos ")

		hql.append(" WHERE ( produtoEdicao.dataDesativacao BETWEEN :dataDe AND :dataAte ) ");
		
		if (!codigoFornecedor.isEmpty()) {
			hql.append(" AND fornecedores.id = :codigoFornecedor ");
		}

		hql.append(" GROUP BY produto.codigo , ")
			.append("   produto.nome , ")
			.append("   produtoEdicao.numeroEdicao, " )
			.append("   juridica.nomeFantasia, " )
			.append("   produtoEdicao.parcial ");

		hql.append(" HAVING ( sum(movimentoEstoque.qtde) ) > 0 ");

		Query query = this.getSession().createQuery(hql.toString());

    	query.setParameter("dataDe", dataDe);
    	query.setParameter("dataAte", dataAte);

    	if(!codigoFornecedor.isEmpty()){
	    	query.setParameter("codigoFornecedor", Long.parseLong(codigoFornecedor));
	    }
		
		return query.list();
	}

}
