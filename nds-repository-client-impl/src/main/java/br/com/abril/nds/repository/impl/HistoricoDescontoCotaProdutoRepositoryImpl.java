package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroConsultaHistoricoDescontoDTO;
import br.com.abril.nds.model.cadastro.desconto.HistoricoDescontoCotaProdutoExcessao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.HistoricoDescontoCotaProdutoRepository;

/**
 * Classe de implementação referente a acesso de dados
 * para as pesquisas de desconto do distribuidor
 * 
 * @author Discover Technology
 */
@Repository
public class HistoricoDescontoCotaProdutoRepositoryImpl extends AbstractRepositoryModel<HistoricoDescontoCotaProdutoExcessao, Long> implements HistoricoDescontoCotaProdutoRepository {

	public HistoricoDescontoCotaProdutoRepositoryImpl() {
		super(HistoricoDescontoCotaProdutoExcessao.class);
	}
	
	@Override
	public HistoricoDescontoCotaProdutoExcessao buscarUltimoDescontoValido(FiltroConsultaHistoricoDescontoDTO filtro) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select hdcpe ");
		hql.append(" from HistoricoDescontoCotaProdutoExcessao hdcpe ");
		
		boolean indWhere = false;
		
		if(filtro.getIdCota() != null) {
			hql.append( " where hdcpe.cota.id = :idCota " );
			indWhere = true;
		}
		
		if(filtro.getIdDesconto() != null) {
			hql.append( indWhere ? " and " : " where " );
			hql.append(" hdcpe.desconto.id = :idDesconto ");
			indWhere = true;
		}
		
		if(filtro.getIdFornecedor() != null) {
			hql.append( indWhere ? " and " : " where " );
			hql.append(" hdcpe.fornecedor.id = :idFornecedor ");
			indWhere = true;
		}
		
		if(filtro.getIdProduto() != null) {
			hql.append( indWhere ? " and " : " where " );
			hql.append(" hdcpe.produto.id = :idProduto ");
			indWhere = true;
		}
		
		if(filtro.getIdProdutoEdicao() != null) {
			hql.append( indWhere ? " and " : " where " );
			hql.append(" hdcpe.produtoEdicao.id = :idProdutoEdicao ");
			indWhere = true;
		}
	
		hql.append(" order by hdcpe.dataAlteracao desc ");
		
		Query query = getSession().createQuery(hql.toString());
		
		if(filtro.getIdCota() != null) {
			query.setParameter("idCota", filtro.getIdCota());
		}
		
		if(filtro.getIdDesconto() != null) {
			query.setParameter("idDesconto", filtro.getIdDesconto());
		}
		
		if(filtro.getIdFornecedor() != null) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		if(filtro.getIdProduto() != null) {
			query.setParameter("idProduto", filtro.getIdProduto());
		}
		
		if(filtro.getIdProdutoEdicao() != null) {
			query.setParameter("idProdutoEdicao", filtro.getIdProdutoEdicao());
		}
		
		query.setMaxResults(1);
		
		return (HistoricoDescontoCotaProdutoExcessao)  query.uniqueResult();
		
	}
	
	@Override
	public List<HistoricoDescontoCotaProdutoExcessao> buscarListaHistoricoDescontoCotaProdutoExcessao(FiltroConsultaHistoricoDescontoDTO filtro) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select hdcpe ");
		hql.append(" from HistoricoDescontoCotaProdutoExcessao hdcpe ");
		
		boolean indWhere = false;
		
		if(filtro.getIdCota() != null) {
			hql.append( " where hdcpe.cota.id = :idCota " );
			indWhere = true;
		}
		
		if(filtro.getIdDesconto() != null) {
			hql.append( indWhere ? " and " : " where " );
			hql.append(" hdcpe.desconto.id = :idDesconto ");
			indWhere = true;
		}
		
		if(filtro.getIdFornecedor() != null) {
			hql.append( indWhere ? " and " : " where " );
			hql.append(" hdcpe.fornecedor.id = :idFornecedor ");
			indWhere = true;
		}
		
		if(filtro.getIdProduto() != null) {
			hql.append( indWhere ? " and " : " where " );
			hql.append(" hdcpe.produto.id = :idProduto ");
			indWhere = true;
		}
		
		if(filtro.getIdProdutoEdicao() != null) {
			hql.append( indWhere ? " and " : " where " );
			hql.append(" hdcpe.produtoEdicao.id = :idProdutoEdicao ");
			indWhere = true;
		}
	
		hql.append(" group by hdcpe.id order by hdcpe.dataAlteracao desc ");
		
		Query query = getSession().createQuery(hql.toString());
		
		if(filtro.getIdCota() != null) {
			query.setParameter("idCota", filtro.getIdCota());
		}
		
		if(filtro.getIdDesconto() != null) {
			query.setParameter("idDesconto", filtro.getIdDesconto());
		}
		
		if(filtro.getIdFornecedor() != null) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		if(filtro.getIdProduto() != null) {
			query.setParameter("idProduto", filtro.getIdProduto());
		}
		
		if(filtro.getIdProdutoEdicao() != null) {
			query.setParameter("idProdutoEdicao", filtro.getIdProdutoEdicao());
		}
		
		return query.list();
		
	}	
	
	
}
