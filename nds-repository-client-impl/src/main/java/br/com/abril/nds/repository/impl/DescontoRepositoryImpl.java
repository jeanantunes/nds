package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.TipoDescontoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.cadastro.desconto.DescontoDTO;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DescontoRepository;

/**
 * Classe de implementação referente a acesso de dados para as pesquisas de desconto do distribuidor
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
	public List<Long> buscarProdutosQueUsamDescontoProduto(Desconto desconto) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("	select p.id	");
		hql.append("	from HistoricoDescontoProduto hdp	");
		hql.append("	inner join hdp.produto p			");
		hql.append("	inner join hdp.desconto desconto	");
		hql.append("	where desconto.id = :idDesconto 	");
		hql.append("   	group by p.id	");
		
		Query q = getSession().createQuery(hql.toString());
		
		q.setParameter("idDesconto", desconto.getId());
		
		return q.list();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> buscarProdutosEdicoesQueUsamDescontoProduto(Desconto desconto) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("	select pe.id	");
		hql.append("	from ProdutoEdicao pe, HistoricoDescontoProdutoEdicao hdpe ");
		hql.append(" 	inner join hdpe.produtoEdicao pe	");
		hql.append("	inner join hdpe.desconto desconto	");
		hql.append("	where desconto.id = :idDesconto		");
		hql.append(" 	group by pe.id						");
		
		Query q = getSession().createQuery(hql.toString());
		
		q.setParameter("idDesconto", desconto.getId());
		
		return q.list();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoDescontoDTO> obterMergeDescontosEspecificosEGerais(
			Cota cota, String sortorder, String sortname) {
		

		StringBuilder sql = new StringBuilder("");
		
		sql.append(" SELECT  ");
		sql.append("    coalesce(pf.nome, pf.RAZAO_SOCIAL) as fornecedor, ");
		sql.append("    dados.VALOR as desconto, ");
		sql.append("    dados.TIPO as descTipoDesconto, ");
		sql.append("    DATA_ALTERACAO as dataAlteracao ");
		sql.append(" FROM ");
		sql.append("    (SELECT  ");
		sql.append("        f.ID as FORNECEDOR_ID, ");
		sql.append("            D.VALOR as VALOR, ");
		sql.append("            'Geral' as TIPO, ");
        sql.append("            HDF.DATA_ALTERACAO ");
		sql.append("    FROM ");
		sql.append("        fornecedor f ");
        sql.append("    JOIN DESCONTO D ON (D.ID = F.DESCONTO_ID)");
        sql.append("     JOIN HISTORICO_DESCONTOS_FORNECEDORES HDF ON HDF.DESCONTO_ID = D.ID ");
		sql.append("    JOIN COTA_FORNECEDOR CF ON (CF.FORNECEDOR_ID = F.ID) ");
		sql.append("    WHERE ");
		sql.append("        CF.COTA_ID = :idCota ");
		sql.append("            AND F.DESCONTO_ID is not null ");
		sql.append("            and F.ID not in (SELECT  ");
		sql.append("                h.FORNECEDOR_ID ");
		sql.append("            FROM ");
		sql.append("                desconto_cota_produto_excessoes h ");
		sql.append("            WHERE ");
		sql.append("                h.COTA_ID = :idCota AND h.PRODUTO_ID is NULL ");
        sql.append("            GROUP BY h.FORNECEDOR_ID) group by f.id ");
		sql.append(" UNION ALL ");
		sql.append(" SELECT  ");
		sql.append("        h.FORNECEDOR_ID, ");
		sql.append("            d.VALOR as VALOR, ");
        sql.append("            'Específico' as TIPO, ");
        sql.append("            hdcpe.DATA_ALTERACAO ");
		sql.append("    FROM ");
		sql.append("        desconto_cota_produto_excessoes h ");
		sql.append("		inner join desconto d on d.id = h.desconto_id ");
        sql.append("        join historico_desconto_cota_produto_excessoes hdcpe on hdcpe.desconto_id = d.id ");
		sql.append("    WHERE ");
		sql.append("        h.COTA_ID = :idCota AND h.PRODUTO_ID is NULL ");
		sql.append("            AND d.DATA_ALTERACAO = (SELECT  ");
		sql.append("                MAX(d.DATA_ALTERACAO) ");
		sql.append("            FROM ");
		sql.append("                desconto_cota_produto_excessoes i inner join desconto d on d.id = i.desconto_id ");
		sql.append("            WHERE ");
		sql.append("                i.COTA_ID = :idCota AND i.PRODUTO_ID is NULL ");
		sql.append("                    AND i.FORNECEDOR_ID = h.FORNECEDOR_ID) ");
		sql.append("    GROUP BY h.FORNECEDOR_ID) as dados ");
		sql.append("        join ");
		sql.append("    FORNECEDOR f ON (f.ID = dados.FORNECEDOR_ID) ");
		sql.append("        join ");
		sql.append("    PESSOA pf ON (f.JURIDICA_ID = pf.ID) ");

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

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> buscarProximosLancamentosQueUsamDescontoProduto(Desconto desconto) {
		
		if(desconto == null || desconto.getId() == null) {
			return null;
		}
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select dpl.id ");
		hql.append(" from DescontoProximosLancamentos dpl ");
		hql.append(" where dpl.desconto.id = :idDesconto ");
		hql.append(" group by dpl.id ");
		
		Query q = getSession().createQuery(hql.toString());
		
		q.setParameter("idDesconto", desconto.getId());
		
		return q.list();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Cota> buscarCotasQueUsamDescontoEditor(Desconto desconto) {
		
		if(desconto == null || desconto.getId() == null) {
			return null;
		}
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("	select c ");
		hql.append("	from DescontoCotaProdutoExcessao dcpe ");
		hql.append(" 	inner join dcpe.cota c	");
		hql.append("	inner join dcpe.desconto desconto	");
		hql.append("	where desconto.id = :idDesconto		");
		hql.append(" 	group by c.id						");
		
		Query q = getSession().createQuery(hql.toString());
		
		q.setParameter("idDesconto", desconto.getId());
		
		return q.list();
	}

	@Override
	public Editor buscarEditorUsaDescontoEditor(Desconto desconto) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select e ");
		hql.append("from Editor e  ");
		hql.append("where 1 = 1 ");
		hql.append("and e.desconto.id = :idDesconto ");
		hql.append("order by e.desconto.dataAlteracao desc ");
		
		Query q = getSession().createQuery(hql.toString());
		
		q.setParameter("idDesconto", desconto.getId());
		
		return (Editor) q.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DescontoDTO> buscarDescontosAssociadosACota(Integer numeroCota){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select distinct   ");
		sql.append(" 	T.desconto_id as id, ");
		sql.append(" 	T.tipo as tipoDesconto,  ");
		sql.append(" 	T.idEspecifico as idEspecifico  ");
		sql.append(" from  ");
		sql.append("   (select   ");
		sql.append("       dpl.DESCONTO_ID,   ");
		sql.append("       'desconto_proximos_lancamentos' as tipo,  ");
		sql.append("       dpl.id as idEspecifico  ");
		sql.append("   from desconto_proximos_lancamentos dpl  ");
		sql.append("   join produto p    ");
		sql.append("     on dpl.PRODUTO_ID = p.ID  ");
		sql.append("   join desconto_lancamento_cota dlc  ");
		sql.append("     on dlc.DESCONTO_LANCAMENTO_ID = dpl.ID  ");
		sql.append("   join cota c  ");
		sql.append("     on dlc.COTA_ID = c.ID  ");
		sql.append("   join desconto dc  ");
		sql.append("     on dpl.DESCONTO_ID = dc.ID  ");
		sql.append("   where c.NUMERO_COTA = :numCota and dc.TIPO_DESCONTO = 'PRODUTO'  ");
		sql.append(" union all   ");
		sql.append("   select   ");
		sql.append("       dcpe.DESCONTO_ID,   ");
		sql.append("       'desconto_cota_produto_excessoes' as tipo,  ");
		sql.append("       dcpe.id as idEspecifico  ");
		sql.append("   from desconto_cota_produto_excessoes dcpe   ");
		sql.append("   join cota c  ");
		sql.append("     on dcpe.COTA_ID = c.ID  ");
		sql.append("   join produto p   ");
		sql.append("     on dcpe.PRODUTO_ID = p.ID  ");
		sql.append("   join desconto dc  ");
		sql.append("     on dcpe.DESCONTO_ID = dc.ID  ");
		sql.append("   where c.NUMERO_COTA = :numCota and dc.TIPO_DESCONTO = 'PRODUTO'  ");
		sql.append(" union all   ");
		sql.append("   select   ");
		sql.append("       hd.DESCONTO_ID,   ");
		sql.append("       'historico_desconto_cota_produto_excessoes' as tipo,  ");
		sql.append("       hd.id as idEspecifico  ");
		sql.append("   from historico_desconto_cota_produto_excessoes hd   ");
		sql.append("   join cota c  ");
		sql.append("     on hd.COTA_ID = c.ID  ");
		sql.append("   join desconto dc  ");
		sql.append("     on hd.DESCONTO_ID = dc.ID  ");
		sql.append("   where c.NUMERO_COTA = :numCota and dc.TIPO_DESCONTO = 'PRODUTO'  ");
		sql.append("   and hd.PRODUTO_ID is not null) T   ");
		sql.append(" order by DESCONTO_ID;  ");
		
		Query query = getSession().createSQLQuery(sql.toString()); 
		
		query.setParameter("numCota", numeroCota);
		
		((SQLQuery) query).addScalar("id", StandardBasicTypes.LONG);
		((SQLQuery) query).addScalar("tipoDesconto", StandardBasicTypes.STRING);
		((SQLQuery) query).addScalar("idEspecifico", StandardBasicTypes.LONG);
		
		query.setResultTransformer(Transformers.aliasToBean(DescontoDTO.class));
		
		return query.list();
	}
}