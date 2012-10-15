package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroCadastroTipoNotaDTO;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.repository.TipoNotaFiscalRepository;

@Repository
public class TipoNotaFiscalRepositoryImpl extends AbstractRepositoryModel<TipoNotaFiscal, Long> 
										  implements TipoNotaFiscalRepository {

	public TipoNotaFiscalRepositoryImpl() {
		super(TipoNotaFiscal.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TipoNotaFiscal> obterTiposNotasFiscais() {

		String hql = " from TipoNotaFiscal tipoNotaFiscal group by tipoNotaFiscal.id ";
		
		Query query = getSession().createQuery(hql);
		
		return query.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<TipoNotaFiscal> obterTiposNotasFiscais(TipoOperacao tipoOperacao) {

		String hql = " from TipoNotaFiscal tipoNotaFiscal where tipoNotaFiscal.tipoOperacao = :tipoOperacao group by tipoNotaFiscal.id ";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("tipoOperacao", tipoOperacao);
		
		return query.list();
	}

	@Override
	public TipoNotaFiscal obterTipoNotaFiscal(GrupoNotaFiscal grupoNotaFiscal) {
		
		String hql = " from TipoNotaFiscal tipoNotaFiscal where tipoNotaFiscal.grupoNotaFiscal = :grupoNotaFiscal group by tipoNotaFiscal.id  ";
		
		Query query = getSession().createQuery(hql);
		
		query.setParameter("grupoNotaFiscal", grupoNotaFiscal);
		
		return (TipoNotaFiscal) query.uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TipoNotaFiscal obterTipoNotaFiscal(GrupoNotaFiscal grupoNotaFiscal, TipoAtividade tipoAtividade, boolean isContribuinte) {

		StringBuilder hql = new StringBuilder();

		hql.append(" from TipoNotaFiscal tipoNotaFiscal ")
		   .append(" where tipoNotaFiscal.grupoNotaFiscal = :grupoNotaFiscal ")
		   .append(" and tipoNotaFiscal.contribuinte = :isContribuinte ")
		   .append(" and tipoNotaFiscal.tipoAtividade = :tipoAtividade ");
		
		Query query = getSession().createQuery(hql.toString());

		query.setParameter("grupoNotaFiscal", grupoNotaFiscal);
		query.setParameter("isContribuinte", isContribuinte);
		query.setParameter("tipoAtividade", tipoAtividade);

		return (TipoNotaFiscal) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoNotaFiscal> consultarTipoNotaFiscal(FiltroCadastroTipoNotaDTO filtro){
		
		StringBuilder hql  = new StringBuilder();
		
		hql.append(getHqlConsulta(filtro, false));
		
		hql.append(getOrdenacaoConsulta(filtro));
		
		Query query = getSession().createQuery(hql.toString());
		
		if(filtro.getTipoAtividade()!= null){
			query.setParameter("tipoAtividade", filtro.getTipoAtividade());
		}
		
		if(filtro.getTipoNota()!= null && !filtro.getTipoNota().trim().isEmpty() ){
			query.setParameter("tipoDescNota", "%" + filtro.getTipoNota() + "%");
		}
		
		if (filtro.getPaginacao() != null) {

			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}

			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao()
						.getQtdResultadosPorPagina());
			}
		}
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Integer obterQuantidadeTiposNotasFiscais(FiltroCadastroTipoNotaDTO filtro) {
		
		String hql  = getHqlConsulta(filtro, true);
		
		Query query = getSession().createQuery(hql.toString());
		
		if(filtro.getTipoAtividade()!= null){
			query.setParameter("tipoAtividade", filtro.getTipoAtividade());
		}
		
		if(filtro.getTipoNota()!= null && !filtro.getTipoNota().trim().isEmpty() ){
			query.setParameter("tipoDescNota", "%" + filtro.getTipoNota() + "%");
		}
		
		List<Long>list = query.list();
		
		return  (list!= null)? list.size():0 ;
	}
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.TipoNotaFiscalRepository#obterTiposNotasFiscaisCotasNaoContribuintesPor(br.com.abril.nds.model.cadastro.TipoAtividade)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<TipoNotaFiscal> obterTiposNotasFiscaisCotasNaoContribuintesPor(TipoAtividade tipoAtividade) {
		
		String hql = " from TipoNotaFiscal tipoNotaFiscal where tipoNotaFiscal.tipoAtividade = :tipoAtividade group by tipoNotaFiscal.id ";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("tipoAtividade", tipoAtividade);
		
		return query.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoNotaFiscal> obterTiposNotasFiscaisPorTipoAtividadeDistribuidor(TipoAtividade tipoAtividade) {
		
		String hql = " from TipoNotaFiscal tipoNotaFiscal where tipoNotaFiscal.tipoAtividade = :tipoAtividade group by tipoNotaFiscal.id ";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("tipoAtividade", tipoAtividade);
		
		return query.list();
	}
	
	private String getHqlConsulta(FiltroCadastroTipoNotaDTO filtro, boolean count){
		
		StringBuilder hql  = new StringBuilder();
		
		if(count){
			
			hql.append(" select tipoNota.id from TipoNotaFiscal tipoNota ");
		}
		else{
			hql.append(" select tipoNota from TipoNotaFiscal tipoNota ");
		}
		
		if((filtro.getTipoNota()!= null && !filtro.getTipoNota().trim().isEmpty()) 
				|| filtro.getTipoAtividade()!= null){
			
			hql.append(" where ");
		}
		
		if(filtro.getTipoNota()!= null && !filtro.getTipoNota().trim().isEmpty() ){
			hql.append(" upper(tipoNota.descricao) like(:tipoDescNota) ");
		}
		
		if(filtro.getTipoAtividade()!= null 
				&& (filtro.getTipoNota()!= null && !filtro.getTipoNota().trim().isEmpty())){
			hql.append(" AND ");
		}
		
		if(filtro.getTipoAtividade()!= null){
			hql.append("  tipoNota.tipoAtividade=:tipoAtividade ");
		}
		
		hql.append(" group by tipoNota ");
		
		return hql.toString();
	}
	
	private String getOrdenacaoConsulta(FiltroCadastroTipoNotaDTO filtro){
		
		if (filtro == null || filtro.getOrdenacaoColuna() == null) {
			return "";
		}

		StringBuilder hql = new StringBuilder();

		switch (filtro.getOrdenacaoColuna()) {
			
			case OPERACAO:
				hql.append(" order by tipoNota.tipoAtividade  ");
				break;
			case DESCRICAO:
				hql.append(" order by tipoNota.descricao  ");
				break;
			case CFOP_ESTADO :
				hql.append(" order by tipoNota.cfopEstado  ");
				break;
			case CFOP_OUTROS_ESTADOS:
				hql.append(" order by tipoNota.cfopOutrosEstados ");
				break;
			default:
				hql.append(" order by  tipoNota.tipoAtividade ");
		}

		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append(filtro.getPaginacao().getOrdenacao().toString());
		}

		return hql.toString();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List <TipoNotaFiscal> obterTiposNotaFiscal(GrupoNotaFiscal grupoNotaFiscal) {
		
		String hql = " from TipoNotaFiscal tipoNotaFiscal where tipoNotaFiscal.grupoNotaFiscal = :grupoNotaFiscal group by tipoNotaFiscal.id  ";
		
		Query query = getSession().createQuery(hql);
		
		query.setParameter("grupoNotaFiscal", grupoNotaFiscal);
		
		return query.list();
	}

}
