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
		sql.append("         estrat.produto_edicao_id in (select est.PRODUTO_EDICAO_ID from estudo est where id = :estudoId) ");

		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("estudoId", estudoId);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoBaseSugeridaDTO.class));
		 
		return query.list();
	}
}
