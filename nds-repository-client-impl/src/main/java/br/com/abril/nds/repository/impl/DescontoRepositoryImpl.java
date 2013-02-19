package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.TipoDescontoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
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

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoDescontoDTO> obterMergeDescontosEspecificosEGerais(
			Cota cota, String sortorder, String sortname) {
		

		StringBuilder sql = new StringBuilder("");
		
		sql.append("SELECT coalesce(pf.nome,pf.RAZAO_SOCIAL) as fornecedor,dados.VALOR as desconto, dados.TIPO as descTipoDesconto, DATA_ALTERACAO as dataAlteracao FROM( "
				 + " SELECT  f.ID as FORNECEDOR_ID, D.VALOR as VALOR, 'Geral' as TIPO, D.DATA_ALTERACAO FROM fornecedor f "
				 + " JOIN DESCONTO D on (D.ID=F.DESCONTO_ID) "
				 + " JOIN COTA_FORNECEDOR CF on (CF.FORNECEDOR_ID=F.ID) "
				 + " WHERE CF.COTA_ID=:idCota "
				 + " 	AND F.DESCONTO_ID is not null "
				 + " 	and F.ID not in (SELECT h.FORNECEDOR_ID FROM historico_desconto_cota_produto_excessoes h "
				 + " 								WHERE h.COTA_ID=:idCota AND h.PRODUTO_ID is NULL GROUP BY h.FORNECEDOR_ID) " 	
				 + " 	UNION ALL "
				 
				 + " 		SELECT h.FORNECEDOR_ID, h.VALOR as VALOR, 'Específico' as TIPO, h.DATA_ALTERACAO   FROM historico_desconto_cota_produto_excessoes h "
				 + " 		WHERE h.COTA_ID=:idCota "
				 + " 		AND h.PRODUTO_ID is NULL "
				 + " 		AND h.DATA_ALTERACAO=(SELECT MAX(i.DATA_ALTERACAO) FROM historico_desconto_cota_produto_excessoes i "
				 + " 		            WHERE i.COTA_ID=:idCota "
				 + " 		            AND i.PRODUTO_ID is NULL " 
				 + " 		            AND i.FORNECEDOR_ID=h.FORNECEDOR_ID) "
				 + " 		GROUP BY h.FORNECEDOR_ID) "
				 + " 	as dados "
				 + " 	join  FORNECEDOR f on (f.ID=dados.FORNECEDOR_ID) "
				 + " 	join  PESSOA pf on (f.JURIDICA_ID=pf.ID) ");
		
		sql = addOrderByMergeDescontosEspecificosEGerais(sql, sortname, sortorder);		
		
		Query query = getSession().createSQLQuery(sql.toString()); 
		
		query.setParameter("idCota", cota.getId());
		
		query.setResultTransformer(Transformers.aliasToBean(TipoDescontoDTO.class));
		
		return query.list();
	}

	private StringBuilder addOrderByMergeDescontosEspecificosEGerais(StringBuilder sql, String sortname, String sortorder) {
		
		if(sortname==null)
			return sql;
				
		if(sortname.equals("fornecedor"))
			return sql.append(" order by fornecedor " + sortorder + " ");
		
		if(sortname.equals("desconto"))
			return sql.append(" order by desconto " + sortorder + " ");
		
		if(sortname.equals("descTipoDesconto"))
			return sql.append(" order by descTipoDesconto " + sortorder + " ");
		
		if(sortname.equals("dataAlteracao"))
			return sql.append(" order by dataAlteracao " + sortorder + " ");
		
		return sql;
	}

}
	@Override
	public BigDecimal obterMediaDescontosFornecedoresCota(Integer numeroCota) {
		
		StringBuilder hql = new StringBuilder("select ");
		hql.append(" (sum(d.valor) / count(d.id)) ")
		   .append(" from Cota c ")
		   .append(" join c.fornecedores f ")
		   .append(" join f.desconto d ")
		   .append(" where c.numeroCota = :numeroCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("numeroCota", numeroCota);
		
		return (BigDecimal) query.uniqueResult();
	}
}