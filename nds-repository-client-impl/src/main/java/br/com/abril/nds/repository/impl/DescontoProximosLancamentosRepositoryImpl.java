package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.desconto.DescontoDTO;
import br.com.abril.nds.model.financeiro.DescontoProximosLancamentos;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DescontoProximosLancamentosRepository;

@Repository
public class DescontoProximosLancamentosRepositoryImpl extends AbstractRepositoryModel<DescontoProximosLancamentos,Long> 
implements DescontoProximosLancamentosRepository{

	public DescontoProximosLancamentosRepositoryImpl() {
		super(DescontoProximosLancamentos.class);
	}

	
	@Override
	public DescontoProximosLancamentos obterDescontoProximosLancamentosPor(Long idProduto, Date dataLancamento) {
		
		StringBuilder jpql = new StringBuilder();
			jpql.append(" SELECT dpl FROM DescontoProximosLancamentos dpl ")
				.append(" WHERE dpl.produto.id = :idProduto and ")
				.append(" dpl.quantidadeProximosLancamaentos > 0 and ")
				.append(" dpl.dataInicioDesconto <= :dataLancamento ");

		Query query = this.getSession().createQuery(jpql.toString());	
		
		query.setParameter("idProduto", idProduto);
		query.setParameter("dataLancamento", dataLancamento);
		
		try {
			return (DescontoProximosLancamentos) query.uniqueResult();
		} catch(HibernateException e) {
			return null;
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DescontoDTO> obterDescontosProximosLancamentos(Date dataLancamento) {
		
		StringBuilder hql = new StringBuilder("")
			.append(" SELECT cota_id AS cotaId ")
			.append(" 		, null AS fornecedorId ")
			.append(" 		, null AS produtoEdicaoId ")
			.append(" 		, produto_id AS produtoId ")
			.append(" 		, d.valor ")
			.append("		, true as proximoLancamento ")
			.append("		, d.predominante ")
			.append(" FROM desconto_proximos_lancamentos AS dpl ")
			.append(" INNER JOIN desconto d ON d.id = dpl.desconto_id ")
			.append(" LEFT OUTER JOIN desconto_lancamento_cota dlc ON dlc.DESCONTO_LANCAMENTO_ID = dpl.ID ")
			.append(" LEFT OUTER JOIN cota c ON c.ID = dlc.COTA_ID ")
			.append(" WHERE dpl.QUANTIDADE_PROXIMOS_LANCAMENTOS > 0 ")
			.append(" AND dpl.DATA_INICIO_DESCONTO <= :dataLancamento ");

		SQLQuery query = getSession().createSQLQuery(hql.toString());
		query.setResultTransformer(new AliasToBeanResultTransformer(DescontoDTO.class));
		
		query.setParameter("dataLancamento", dataLancamento);
		
		query.addScalar("cotaId", StandardBasicTypes.LONG);
		query.addScalar("produtoEdicaoId", StandardBasicTypes.LONG);
		query.addScalar("produtoId", StandardBasicTypes.LONG);
		query.addScalar("fornecedorId", StandardBasicTypes.LONG);
		query.addScalar("valor", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("proximoLancamento", StandardBasicTypes.BOOLEAN);
		query.addScalar("predominante", StandardBasicTypes.BOOLEAN);
		
		return query.list();
	}

}
