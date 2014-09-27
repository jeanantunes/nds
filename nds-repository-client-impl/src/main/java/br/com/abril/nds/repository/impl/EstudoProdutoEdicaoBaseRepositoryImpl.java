package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
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
		hql.append("     produto_edicao.parcial as isParcial, ");
		hql.append("     estudo_produto_edicao_base.isConsolidado as isParcialConsolidado, ");
		hql.append("     estudo_produto_edicao_base.peso as peso    ");
		hql.append(" FROM estudo_produto_edicao_base     ");
		hql.append(" INNER JOIN produto_edicao ON produto_edicao.id = estudo_produto_edicao_base.PRODUTO_EDICAO_ID  ");
		hql.append(" INNER JOIN produto ON produto_edicao.produto_id = produto.id ");
		hql.append(" where ESTUDO_ID = :estudoId");

		SQLQuery query = this.getSession().createSQLQuery(hql.toString());
		
		query.setParameter("estudoId", estudoId);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(EdicaoBaseEstudoDTO.class));
		 
		return query.list();
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
