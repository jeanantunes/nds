package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.TipoDescontoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.desconto.DescontoDistribuidor;
import br.com.abril.nds.repository.AbstractRepositoryModel;
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
		
		hql.append("select hdf.id as sequencial ");
		hql.append(", hdf.desconto.id as descontoId ");
		hql.append(", hdf.usuario.nome as usuario ");
		hql.append(", hdf.desconto.valor as desconto ");
		hql.append(", hdf.desconto.dataAlteracao as dataAlteracao ");
		hql.append(", (case ");
		hql.append("when (select count(hdf1.desconto.id) from HistoricoDescontoFornecedor hdf1 ");
		hql.append("where hdf1.desconto.id = hdf.desconto.id ");
		hql.append("group by hdf1.desconto.id) > 1 ");
		hql.append("then 'Diversos' ");
		hql.append("when (select count(hdf1.desconto.id) from HistoricoDescontoFornecedor hdf1 ");
		hql.append("where hdf1.desconto.id = hdf.desconto.id ");
		hql.append("group by hdf1.desconto.id) = 1 then pessoa.razaoSocial ");
		hql.append("else null end) as fornecedor");
		hql.append(", 'Geral' as descTipoDesconto ");
		hql.append("from HistoricoDescontoFornecedor hdf join hdf.fornecedor f join f.juridica as pessoa ");
		hql.append("where 1=1 ");
		
		if(filtro.getIdFornecedores()!=null && !filtro.getIdFornecedores().isEmpty()) {
			hql.append(" and f.id in (:idFornecedores) ");
		}
		
		hql.append("group by hdf.desconto, hdf.dataAlteracao ");
		
		if(filtro.getPaginacao()!=null){
			
			if (filtro.getPaginacao().getSortColumn() != null && 
				!filtro.getPaginacao().getSortColumn().trim().isEmpty()) {
				
				hql.append(" ORDER BY ");
				hql.append(filtro.getPaginacao().getSortColumn());		
			
				if ( filtro.getPaginacao().getOrdenacao() != null ) {
					
					hql.append(" ");
					hql.append( filtro.getPaginacao().getOrdenacao().toString());
				}
			}
		}

		hql.append(getOrdenacao(filtro));
		
		Query query  = getSession().createQuery(hql.toString());
		
		query.setResultTransformer(Transformers.aliasToBean(TipoDescontoDTO.class));
		
		if(filtro.getIdFornecedores()!=null && !filtro.getIdFornecedores().isEmpty()) {
			query.setParameterList("idFornecedores", filtro.getIdFornecedores());
		}
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
				
	}
	
	@Override
	public Integer buscarQuantidadeDescontos(FiltroTipoDescontoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select f ");
		hql.append("from HistoricoDescontoFornecedor hdf join hdf.fornecedor f join f.juridica as pessoa ");
		hql.append("where 1=1 ");
		
		if(filtro.getIdFornecedores() != null && !filtro.getIdFornecedores().isEmpty()) {
			hql.append(" and f.id in (:idFornecedores) ");
		}
		
		hql.append("group by hdf.desconto, hdf.dataAlteracao ");
		
		Query query  = getSession().createQuery(hql.toString());
		
		if(filtro.getIdFornecedores() != null && !filtro.getIdFornecedores().isEmpty()) {
			query.setParameterList("idFornecedores", filtro.getIdFornecedores());
		}
		
		return query.list().size();
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
	
	@Override
	public DescontoDistribuidor buscarUltimoDescontoValido(Fornecedor fornecedor) {
		
		return obterUltimoDescontoValido(null,fornecedor);
	}
	
	@Override
	public DescontoDistribuidor buscarUltimoDescontoValido(Long idUltimoDesconto, Fornecedor fornecedor) {
		
		return obterUltimoDescontoValido(idUltimoDesconto, fornecedor);
	}
	
	private DescontoDistribuidor obterUltimoDescontoValido(Long idUltimoDesconto, Fornecedor fornecedor){
			
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select desconto from DescontoDistribuidor desconto JOIN desconto.fornecedores fornecedor  ")
			.append("where desconto.dataAlteracao = ")
			.append(" ( select max(descontoSub.dataAlteracao) from DescontoDistribuidor descontoSub  ")
				.append(" JOIN descontoSub.fornecedores fornecedorSub ")
				.append(" where fornecedorSub.id =:idFornecedor ");
				if(idUltimoDesconto!= null){
					hql.append(" and descontoSub.id not in (:idUltimoDesconto) ");
				}
		hql.append(" ) ")
			.append(" AND fornecedor.id =:idFornecedor ");
	
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idFornecedor",fornecedor.getId());
		
		if(idUltimoDesconto!= null){
			
			query.setParameter("idUltimoDesconto", idUltimoDesconto);
		}
		
		query.setMaxResults(1);
		
		return (DescontoDistribuidor)  query.uniqueResult();
	}
}
