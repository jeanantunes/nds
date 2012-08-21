package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.TipoDescontoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoDTO;
import br.com.abril.nds.model.cadastro.desconto.DescontoDistribuidor;
import br.com.abril.nds.repository.DescontoDistribuidorRepository;

/**
 * Classe de implementação referente a acesso de dados
 * para as pesquisas de desconto do distribuidor
 * 
 * @author Discover Technology
 */
@Repository
public class DescontoDistribuidorRepositoryImpl extends AbstractRepositoryModel<DescontoDistribuidor, Long> implements DescontoDistribuidorRepository {

	public DescontoDistribuidorRepositoryImpl() {
		super(DescontoDistribuidor.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoDescontoDTO> buscarDescontos(FiltroTipoDescontoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select desconto.id as sequencial, ")
			.append(" desconto.id as idTipoDesconto ,")
			.append(" desconto.usuario.nome as usuario ,")
			.append(" desconto.desconto as desconto ,")
			.append(" desconto.dataAlteracao as dataAlteracao , ")
			.append("(case ")
				.append("when (select count(fornecedor.id) from DescontoDistribuidor descontoFor JOIN descontoFor.fornecedores fornecedor  ")
					.append(" where descontoFor.id = desconto.id ) > 1 ")
				.append("then 'Diversos' ")
				.append("when (select count(fornecedor.id) from DescontoDistribuidor descontoFor JOIN descontoFor.fornecedores fornecedor  ")
					.append("where descontoFor.id = desconto.id ) = 1 then pessoa.razaoSocial ")
			.append("else null end) as fornecedor, ")
		    .append(" 'Geral' as descTipoDesconto ");
		
		hql.append(" from DescontoDistribuidor desconto")
			.append(" JOIN desconto.fornecedores fornecedor ")
			.append(" JOIN fornecedor.juridica pessoa ")
			.append(" group by desconto.id");
		
		hql.append(getOrdenacao(filtro));
		
		Query query  = getSession().createQuery(hql.toString());

		ResultTransformer resultTransformer = new AliasToBeanResultTransformer(TipoDescontoDTO.class);

		query.setResultTransformer(resultTransformer);
		
		return query.list();
	}
	
	@Override
	public Integer buscarQuantidadeDescontos(FiltroTipoDescontoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(desconto.id) from DescontoDistribuidor desconto ");
		
		Query query  = getSession().createQuery(hql.toString());
		
		return ((Long)  query.uniqueResult()).intValue();
	}
	
	private String getOrdenacao(FiltroTipoDescontoDTO filtro){
		
		if (filtro == null || filtro.getOrdenacaoColuna() == null) {
			return "";
		}

		StringBuilder hql = new StringBuilder();

		switch (filtro.getOrdenacaoColuna()) {
			
			case DATA_ALTERACAO:
				hql.append(" order by dataAlteracao ");
				break;
				
			case DESCONTO:
				hql.append(" order by desconto  ");
				break;
					
			case SEQUENCIAL:
				hql.append(" order by sequencial  ");
				break;
			
			case USUARIO:
				hql.append(" order by usuario ");
				break;
				
			case FORNECEDORES:
				hql.append(" order by fornecedor ");
				break;	
				
			case TIPO_DESCONTO:
				hql.append(" order by descTipoDesconto ");
				break;		
				
			default:
				hql.append(" order by sequencial ");
		}

		if (filtro.getPaginacao()!= null && filtro.getPaginacao().getOrdenacao() != null) {
			hql.append(filtro.getPaginacao().getOrdenacao().toString());
		}

		return hql.toString();
	}

}
