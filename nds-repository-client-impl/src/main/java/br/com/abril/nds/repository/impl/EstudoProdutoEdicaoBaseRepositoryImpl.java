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
		hql.append("     distinct ");
		hql.append("         codigo as codigoProduto, ");
		hql.append("         nome as nomeProduto, ");
		hql.append("         numero_Edicao as numeroEdicao, ");
		hql.append("         estudo_produto_edicao_base.peso as peso    ");
		hql.append("     FROM ");
		hql.append("         estudo_produto_edicao_base     ");
		hql.append("     INNER JOIN ");
		hql.append("         produto_edicao  ");
		hql.append("             ON produto_edicao.id = estudo_produto_edicao_base.PRODUTO_EDICAO_ID  ");
		hql.append("     INNER JOIN  ");
		hql.append("         produto  ");
		hql.append("             ON produto_edicao.produto_id = produto.id ");
		hql.append("     where ");
		hql.append("        ESTUDO_ID = ");
		hql.append(estudoId);

		SQLQuery query = this.getSession().createSQLQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(EdicaoBaseEstudoDTO.class));
		 
		return query.list();
	}

}
