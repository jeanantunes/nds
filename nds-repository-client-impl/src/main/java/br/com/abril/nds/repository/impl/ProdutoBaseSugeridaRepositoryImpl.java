package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ProdutoBaseSugeridaDTO;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ProdutoBaseSugeridaRepository;

@Repository
@SuppressWarnings("rawtypes")
public class ProdutoBaseSugeridaRepositoryImpl extends AbstractRepositoryModel implements
		ProdutoBaseSugeridaRepository {
	
	@SuppressWarnings("unchecked")
	public ProdutoBaseSugeridaRepositoryImpl(){
		super(Object.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoBaseSugeridaDTO> obterBaseSugerida(Long estudoId) {
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");
		sql.append("     distinct ");
		sql.append("         prod.CODIGO as codigoProduto, ");
		sql.append("         prod.NOME as nomeProduto, ");
		sql.append("         prodEd.NUMERO_EDICAO as numeroEdicao, ");
		sql.append("         estrat.peso as peso ");
		sql.append("     FROM ");
		sql.append("         estrategia_base_distribuicao estrat ");
		sql.append("     INNER JOIN ");
		sql.append("         produto_edicao prodEd  ");
		sql.append("             ON estrat.PRODUTO_EDICAO_ID = prodEd.ID ");
		sql.append("     INNER JOIN  ");
		sql.append("         produto prod  ");
		sql.append("             ON prodEd.produto_id = prod.id ");
		sql.append("     where ");
		sql.append("         estrat.produto_edicao_id in (select est.PRODUTO_EDICAO_ID from estudo est where id = ");
		sql.append(estudoId);
		sql.append(" ); ");

		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoBaseSugeridaDTO.class));
		 
		return query.list();
	}
//	public List<EdicaoBaseEstudoDTO> obterEdicoesBase(Long estudoId) {
//		
//		StringBuilder hql = new StringBuilder();
//		
//		hql.append(" select ");
//		hql.append("     distinct ");
//		hql.append("         codigo as codigoProduto, ");
//		hql.append("         nome as nomeProduto, ");
//		hql.append("         numero_Edicao as numeroEdicao, ");
//		hql.append("         estudo_produto_edicao_base.peso as peso    ");
//		hql.append("     FROM ");
//		hql.append("         estudo_produto_edicao_base     ");
//		hql.append("     INNER JOIN ");
//		hql.append("         produto_edicao  ");
//		hql.append("             ON produto_edicao.id = estudo_produto_edicao_base.PRODUTO_EDICAO_ID  ");
//		hql.append("     INNER JOIN  ");
//		hql.append("         produto  ");
//		hql.append("             ON produto_edicao.produto_id = produto.id ");
//		hql.append("     where ");
//		hql.append("        ESTUDO_ID = ");
//		hql.append(estudoId);
//
//		SQLQuery query = this.getSession().createSQLQuery(hql.toString());
//		
//		query.setResultTransformer(new AliasToBeanResultTransformer(EdicaoBaseEstudoDTO.class));
//		 
//		return query.list();
//	}


}
