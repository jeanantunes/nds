package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
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

	@Override
	public List<Fornecedor> buscarFornecedoresQueUsam(Desconto desconto) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select f ");
		hql.append("from Fornecedor f, HistoricoDescontoFornecedor hdf ");
		hql.append("where f.desconto.id = hdf.desconto.id ");
		hql.append("and f.id = hdf.fornecedor.id ");
		hql.append("and f.desconto.id = :idDesconto ");
		
		Query q = getSession().createQuery(hql.toString());
		
		q.setParameter("idDesconto", desconto.getId());
		
		return q.list();
		
	}

	@Override
	public Desconto buscarUltimoDescontoValido(Long idDesconto, Fornecedor fornecedor) {

		StringBuilder hql = new StringBuilder();
		
		hql.append("select desconto ");
		hql.append("from Fornecedor f join f.desconto desconto, HistoricoDescontoFornecedor hdf ");
		hql.append("where hdf.desconto.id = desconto.id ");
		hql.append("desconto.dataAlteracao = (select max(descontoSub.dataAlteracao) from HistoricoDescontoFornecedor descontoSub  ");
		hql.append(" JOIN descontoSub.fornecedor fornecedorSub ");
		hql.append(" where fornecedorSub.id = :idFornecedor ");
		
		if(idDesconto!= null){
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
	

}
