package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.TipoDescontoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.cadastro.desconto.HistoricoDescontoCotaProdutoExcessao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.HistoricoDescontoCotaProdutoRepository;

/**
 * Classe de implementação referente a acesso de dados
 * para as pesquisas de desconto do distribuidor
 * 
 * @author Discover Technology
 */
@Repository
public class HistoricoDescontoCotaProdutoRepositoryImpl extends AbstractRepositoryModel<HistoricoDescontoCotaProdutoExcessao, Long> implements HistoricoDescontoCotaProdutoRepository {

	public HistoricoDescontoCotaProdutoRepositoryImpl() {
		super(HistoricoDescontoCotaProdutoExcessao.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoDescontoDTO> buscarDescontos(FiltroTipoDescontoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select 0L as sequencial ");
		hql.append(", 1L as idTipoDesconto ");
		hql.append(", 'Anonimo' as usuario ");
		hql.append(", f.desconto as desconto ");
		hql.append(", f.juridica.razaoSocial as fornecedor ");
		hql.append(", 'Geral' as descTipoDesconto ");
		hql.append("from Fornecedor f ");
		
		if(filtro.getIdFornecedores()!=null && !filtro.getIdFornecedores().isEmpty()) {
			hql.append(" where f.id in (:idFornecedores) ");
		}
		
		hql.append(getOrdenacao(filtro));
		
		Query query  = getSession().createQuery(hql.toString());
		
		//ResultTransformer resultTransformer = new AliasToBeanResultTransformer(TipoDescontoDTO.class);

		query.setResultTransformer(Transformers.aliasToBean(TipoDescontoDTO.class));
		
		if(filtro.getIdFornecedores()!=null && !filtro.getIdFornecedores().isEmpty()) {
			query.setParameterList("idFornecedores", filtro.getIdFornecedores());
		}
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
		
		//hql.append(" group by desconto.id ");
		
		/*hql.append(" select desconto.id as sequencial, ")
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
			.append("else null end) as fornecedor, 	")
		    .append(" 'Geral' as descTipoDesconto 	");
		
		hql.append(" from DescontoDistribuidor desconto		")
			.append(" JOIN desconto.fornecedores fornecedor ")
			.append(" JOIN fornecedor.juridica pessoa 		");
			
		
		if(filtro.getIdFornecedores()!=null && !filtro.getIdFornecedores().isEmpty()) {
			hql.append(" where fornecedor.id in (:idFornecedores) ");
		}
		
		hql.append(" group by desconto.id ");*/
		
		//hql.append(getOrdenacao(filtro));
		
	}
	

	@Override
	public Integer buscarQuantidadeDescontos(FiltroTipoDescontoDTO filtro) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HistoricoDescontoCotaProdutoExcessao buscarUltimoDescontoValido(Desconto desconto, Cota cota, Fornecedor fornecedor) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select hdcpe ")
			.append("from HistoricoDescontoCotaProdutoExcessao hdcpe ")
			.append("where hdcpe.desconto.id = :idDesconto ")
			.append("and hdcpe.cota.id = :idCota ")
			.append("and hdcpe.fornecedor.id = :idFornecedor ");
	
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idDesconto", desconto.getId());
		query.setParameter("idCota", cota.getId());
		query.setParameter("idFornecedor", fornecedor.getId());
		
		query.setMaxResults(1);
		
		return (HistoricoDescontoCotaProdutoExcessao)  query.uniqueResult();
		
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
