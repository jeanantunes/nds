package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.TipoDescontoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.cadastro.desconto.HistoricoDescontoFornecedor;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.HistoricoDescontoFornecedorRepository;

/**
 * Classe de implementação referente a acesso de dados
 * para as pesquisas de desconto do distribuidor
 * 
 * @author Discover Technology
 */
@Repository
public class HistoricoDescontoFornecedorRepositoryImpl extends AbstractRepositoryModel<HistoricoDescontoFornecedor, Long> implements HistoricoDescontoFornecedorRepository {

	public HistoricoDescontoFornecedorRepositoryImpl() {
		super(HistoricoDescontoFornecedor.class);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HistoricoDescontoFornecedor buscarUltimoDescontoValido(Fornecedor fornecedor) {

		StringBuilder hql = new StringBuilder();
		
		hql.append("select hdf ")
			.append("from HistoricoDescontoFornecedor hdf ")
			.append("where hdf.dataAlteracao=(select max(hdfSub.dataAlteracao) from HistoricoDescontoFornecedor hdfSub where  hdfSub.fornecedor.id = :fornecedorId) ")
			.append("and hdf.fornecedor.id = :fornecedorId ");
		
		
		Query query  = getSession().createQuery(hql.toString());
		
		query.setParameter("fornecedorId", fornecedor.getId());
		
		query.setMaxResults(1);
		
		Object resultado = query.uniqueResult();
		
		return resultado==null? null : (HistoricoDescontoFornecedor) query.uniqueResult();
		
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
	public HistoricoDescontoFornecedor buscarHistoricoDescontoFornecedorPor(Desconto desconto, Fornecedor fornecedor) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select hdf ")
			.append("from HistoricoDescontoFornecedor hdf ")
			.append("where hdf.desconto.id = :descontoId ")
			.append("and hdf.fornecedor.id = :fornecedorId ");
		
		
		Query query  = getSession().createQuery(hql.toString());
		
		query.setParameter("descontoId", desconto.getId());
		query.setParameter("fornecedorId", fornecedor.getId());
		
		return (HistoricoDescontoFornecedor) query.uniqueResult();
		
	}

}
