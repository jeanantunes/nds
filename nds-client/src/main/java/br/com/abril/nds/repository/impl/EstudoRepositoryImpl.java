package br.com.abril.nds.repository.impl;

import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.repository.EstudoRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.planejamento.Estudo}.
 * 
 * @author Discover Technology
 *
 */
@Repository
public class EstudoRepositoryImpl extends AbstractRepository<Estudo, Long> implements EstudoRepository {
	
	/**
	 * Construtor.
	 */
	public EstudoRepositoryImpl() {
		
		super(Estudo.class);
	}

	@Override
	public Estudo obterEstudoDoLancamentoMaisProximo(Date dataReferencia, String codigoProduto, Long numeroEdicao) {
		
		String hql = " from Estudo estudo"
				   + " where estudo.lancamento.dataLancamentoDistribuidor >= :dataReferencia"
				   + " and estudo.lancamento.produtoEdicao.produto.codigo = :codigoProduto"
				   + " and estudo.lancamento.produtoEdicao.numeroEdicao = :numeroEdicao"
				   + " order by estudo.lancamento.dataLancamentoDistribuidor";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("dataReferencia", dataReferencia);
		
		query.setParameter("codigoProduto", codigoProduto);
		
		query.setParameter("numeroEdicao", numeroEdicao);
		
		query.setMaxResults(1);
		
		return (Estudo) query.uniqueResult();
	}
	
	@Override
	public Estudo obterEstudoDoLancamentoPorDataProdutoEdicao(Date dataReferencia, Long idProdutoEdicao) {
		
		String hql = " from Estudo estudo"
				   + " where estudo.lancamento.dataLancamentoDistribuidor = :dataReferencia"
				   + " and estudo.lancamento.produtoEdicao.id = :idProdutoEdicao"
				   + " order by estudo.lancamento.dataLancamentoDistribuidor";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("dataReferencia", dataReferencia);
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		query.setMaxResults(1);
		
		return (Estudo) query.uniqueResult();
	}

}
