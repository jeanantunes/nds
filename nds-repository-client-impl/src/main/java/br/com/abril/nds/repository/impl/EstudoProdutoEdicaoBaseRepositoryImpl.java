package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.EdicaoBaseEstudoDTO;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstudoProdutoEdicaoBaseRepository;

@Repository
@SuppressWarnings("rawtypes")
public class EstudoProdutoEdicaoBaseRepositoryImpl extends AbstractRepositoryModel implements
		EstudoProdutoEdicaoBaseRepository {
	
	@SuppressWarnings("unchecked")
	public EstudoProdutoEdicaoBaseRepositoryImpl(){
		super(Object.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EdicaoBaseEstudoDTO> obterEdicoesBase(Long estudoId) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		hql.append(" distinct ");
		hql.append("     codigo as codigoProduto, ");
		hql.append("     nome as nomeProduto, ");
		hql.append("     numero_Edicao as numeroEdicao, ");
		hql.append("     estudo_produto_edicao_base.parcial as isParcial, ");
		hql.append("     produto_edicao.id as idProdutoEdicao, ");
		hql.append("     estudo_produto_edicao_base.isConsolidado as isParcialConsolidado, ");
		hql.append("     estudo_produto_edicao_base.periodo_parcial as periodoParcial, ");
		hql.append("     estudo_produto_edicao_base.edicao_aberta as isEdicaoAberta, ");
		hql.append("     estudo_produto_edicao_base.peso as peso    ");
		hql.append(" FROM estudo_produto_edicao_base     ");
		hql.append(" INNER JOIN produto_edicao ON produto_edicao.id = estudo_produto_edicao_base.PRODUTO_EDICAO_ID  ");
		hql.append(" INNER JOIN produto ON produto_edicao.produto_id = produto.id ");
		hql.append(" where ESTUDO_ID = :estudoId");
		hql.append(" order by produto_edicao.numero_edicao desc, estudo_produto_edicao_base.periodo_parcial desc ");

		SQLQuery query = this.getSession().createSQLQuery(hql.toString());
		
		query.setParameter("estudoId", estudoId);
		
		query.addScalar("codigoProduto", StandardBasicTypes.STRING);
		query.addScalar("nomeProduto", StandardBasicTypes.STRING);
		query.addScalar("numeroEdicao", StandardBasicTypes.BIG_INTEGER); 
		query.addScalar("isParcial", StandardBasicTypes.BOOLEAN);
		query.addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
		query.addScalar("isParcialConsolidado", StandardBasicTypes.BOOLEAN);
		query.addScalar("isEdicaoAberta", StandardBasicTypes.BOOLEAN);
		query.addScalar("periodoParcial", StandardBasicTypes.LONG);
		query.addScalar("peso", StandardBasicTypes.BIG_INTEGER);
		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(EdicaoBaseEstudoDTO.class));
		 
		return query.list();
	}
	
	@Override
	public EdicaoBaseEstudoDTO obterEdicoesBaseEstudoOrigemCopiaEstudo(Long estudoId) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select   ");
		sql.append(" 	eg.produto_edicao_id as idProdutoEdicao,  ");
		sql.append(" 	1 as peso,  ");
		sql.append(" 	(case when lc.periodo_lancamento_parcial_id is not null then 1 else 0 end) as isparcial,  ");
		sql.append("  	(case when lc.status = 'FECHADO' or lc.status = 'RECOLHIDO' then 0 else 1 end) as isEdicaoAberta,  ");
		sql.append(" 	(case when lc.periodo_lancamento_parcial_id is not null then plp.numero_periodo else null end) as periodoParcial  ");
		sql.append(" from estudo_gerado eg   ");
		sql.append(" join produto_edicao pe  ");
		sql.append(" 	on eg.produto_edicao_id = pe.id  ");
		sql.append(" join produto pd  ");
		sql.append(" 	on pe.produto_id = pd.id  ");
		sql.append(" join lancamento lc   ");
		sql.append("  	on lc.produto_edicao_id = pe.id   ");
		sql.append(" join periodo_lancamento_parcial plp  ");
		sql.append("  	on lc.periodo_lancamento_parcial_id = plp.id  ");
		sql.append(" where eg.id = :estudoId and lc.data_lcto_distribuidor = eg.data_lancamento  ");
		
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("estudoId", estudoId);
		
		query.addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
		query.addScalar("peso", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("isParcial", StandardBasicTypes.BOOLEAN);
		query.addScalar("isEdicaoAberta", StandardBasicTypes.BOOLEAN);
		query.addScalar("periodoParcial", StandardBasicTypes.LONG);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(EdicaoBaseEstudoDTO.class));
		 
		return (EdicaoBaseEstudoDTO) query.uniqueResult();
	}

	@Override
	public void copiarEdicoesBase(Long idOrigem, Long estudoDividido) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" INSERT INTO estudo_produto_edicao_base ")
		.append(" (ESTUDO_ID, PRODUTO_EDICAO_ID, COLECAO, PARCIAL, EDICAO_ABERTA, PESO, VENDA_CORRIGIDA) ") 
		.append(" ( select ").append(estudoDividido).append(" ,e.PRODUTO_EDICAO_ID,e.colecao,e.PARCIAL,e.EDICAO_ABERTA,e.peso,e.venda_corrigida from estudo_produto_edicao_base e where e.ESTUDO_ID = ")
		.append(idOrigem).append(")");
		
		
		SQLQuery query = this.getSession().createSQLQuery(hql.toString());
		/*query.setParameter("idEstudoCopia", estudoDividido);
		query.setParameter("idOrigem", idOrigem);*/
		
		query.executeUpdate();
		
		//(ESTUDO_ID, PRODUTO_EDICAO_ID, COLECAO, PARCIAL, EDICAO_ABERTA, PESO, VENDA_CORRIGIDA);

		
	}

}
