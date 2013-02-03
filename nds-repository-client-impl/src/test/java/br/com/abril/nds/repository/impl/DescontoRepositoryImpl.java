package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DescontoRepository;

/**
 * Classe de implementação referente a acesso de dados
 * para as pesquisas de desconto do distribuidor
 * 
 * @author Discover Technology
 */
@Repository
public class DescontoRepositoryImpl extends AbstractRepositoryModel<Desconto, Long> implements DescontoRepository {

	public DescontoRepositoryImpl() {
		super(Desconto.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Fornecedor> buscarFornecedoresQueUsamDescontoGeral(Desconto desconto) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select f ");
		hql.append("from HistoricoDescontoFornecedor hdf join hdf.fornecedor f ");
		hql.append("where f.id = hdf.fornecedor.id ");
		hql.append("and hdf.desconto.id = :idDesconto ");
		hql.append("order by hdf.dataAlteracao desc ");
		
		Query q = getSession().createQuery(hql.toString());
		
		q.setParameter("idDesconto", desconto.getId());
		
		return q.list();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Cota> buscarCotasQueUsamDescontoEspecifico(Desconto desconto) {

		StringBuilder hql = new StringBuilder();
		
		hql.append("select distinct c ");
		hql.append("from Cota c, HistoricoDescontoCotaProdutoExcessao hdcpe ");
		hql.append("where c.id = hdcpe.cota.id ");
		hql.append("and hdcpe.desconto.id = :idDesconto ");
		
		Query q = getSession().createQuery(hql.toString());
		
		q.setParameter("idDesconto", desconto.getId());
		
		return q.list();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Fornecedor> buscarFornecedoresQueUsamDescontoEspecifico(
			Desconto desconto) {

		StringBuilder hql = new StringBuilder();
		
		hql.append("select f ");
		hql.append("from Fornecedor f, HistoricoDescontoCotaProdutoExcessao hdcpe ");
		hql.append("where f.id = hdcpe.fornecedor.id ");
		hql.append("and hdcpe.desconto.id = :idDesconto ");
		
		Query q = getSession().createQuery(hql.toString());
		
		q.setParameter("idDesconto", desconto.getId());
		
		return q.list();
		
	}
	
	@Override
	public Desconto buscarUltimoDescontoValido(Long idDesconto, Fornecedor fornecedor) {

		StringBuilder hql = new StringBuilder();
		
		hql.append("select d ");
		hql.append("from Fornecedor as f join f.desconto as d, HistoricoDescontoFornecedor as hdf ");
		hql.append("where hdf.desconto.id = d.id ");
		hql.append("and d.dataAlteracao = (select max(descontoSub.dataAlteracao) from HistoricoDescontoFornecedor descontoSub ");
		hql.append("join descontoSub.fornecedor fornecedorSub ");
		hql.append("where fornecedorSub.id = :idFornecedor ");
		
		if(idDesconto != null){
			hql.append(" and descontoSub.desconto.id <> (:idUltimoDesconto) ");
		}
		
		hql.append(" ) ");
		hql.append(" AND f.id = :idFornecedor ");
	
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idFornecedor", fornecedor.getId());
		
		if(idDesconto != null) {
			
			query.setParameter("idUltimoDesconto", idDesconto);
		}
		
		query.setMaxResults(1);
		
		return (Desconto) query.uniqueResult();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Produto> buscarProdutosQueUsamDescontoProduto(Desconto desconto) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select p ");
		hql.append("from Produto p, HistoricoDescontoProduto hdp ");
		hql.append("where p.id = hdp.produto.id ");
		hql.append("and hdp.desconto.id = :idDesconto ");
		
		Query q = getSession().createQuery(hql.toString());
		
		q.setParameter("idDesconto", desconto.getId());
		
		return q.list();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoEdicao> buscarProdutosEdicoesQueUsamDescontoProduto(Desconto desconto) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select pe ");
		hql.append("from ProdutoEdicao pe, HistoricoDescontoProdutoEdicao hdpe ");
		hql.append("where pe.id = hdpe.produtoEdicao.id ");
		hql.append("and hdpe.desconto.id = :idDesconto ");
		
		Query q = getSession().createQuery(hql.toString());
		
		q.setParameter("idDesconto", desconto.getId());
		
		return q.list();
		
	}

}
