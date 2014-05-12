package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroNaturezaOperacaoDTO;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.TipoDestinatario;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.TipoUsuarioNotaFiscal;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.NaturezaOperacaoRepository;

@Repository
public class NaturezaOperacaoRepositoryImpl extends AbstractRepositoryModel<NaturezaOperacao, Long> 
										  implements NaturezaOperacaoRepository {

	public NaturezaOperacaoRepositoryImpl() {
		super(NaturezaOperacao.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<NaturezaOperacao> obterNaturezasOperacoes() {

		String hql = " from NaturezaOperacao no group by no.id ";
		
		Query query = getSession().createQuery(hql);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<NaturezaOperacao> obterNaturezasOperacoes(TipoOperacao tipoOperacao) {
		
		StringBuilder hql = new StringBuilder("");
		hql.append("from NaturezaOperacao no ");
		hql.append("where 1=1 ");
		
		if(tipoOperacao != null)
			hql.append("and no.tipoOperacao = :tipoOperacao ");
		
		hql.append("order by no.descricao ");
		
		Query query = getSession().createQuery(hql.toString()).setCacheable(true);
		
		if(tipoOperacao != null)
			query.setParameter("tipoOperacao", tipoOperacao);
		
		return query.list();
	}

	@Override
	public NaturezaOperacao obterNaturezaOperacao(GrupoNotaFiscal grupoNotaFiscal) {
		
		String hql = " from NaturezaOperacao no " +
				     " where no.grupoNotaFiscal = :grupoNotaFiscal " +
				     " and no.destinatario = :destinatario " +
				     " and no.emitente = :emitente " +
				     " group by no.id  ";
		
		Query query = getSession().createQuery(hql);
		
		query.setParameter("grupoNotaFiscal", grupoNotaFiscal);
		query.setParameter("destinatario", TipoUsuarioNotaFiscal.TREELOG);
		query.setParameter("emitente", TipoUsuarioNotaFiscal.DISTRIBUIDOR);
		
		return (NaturezaOperacao) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NaturezaOperacao> consultarNaturezaOperacao(FiltroNaturezaOperacaoDTO filtro){
		
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
	public Integer obterQuantidadeTiposNotasFiscais(FiltroNaturezaOperacaoDTO filtro) {
		
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
	public List<NaturezaOperacao> obterNaturezasOperacoesCotasNaoContribuintesPor(TipoAtividade tipoAtividade) {
		
		String hql = " from NaturezaOperacao no where no.tipoAtividade = :tipoAtividade group by no.id ";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("tipoAtividade", tipoAtividade);
		
		return query.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NaturezaOperacao> obterNaturezasOperacoesPorTipoAtividadeDistribuidor(TipoAtividade tipoAtividade) {
		
		String hql = " from NaturezaOperacao tipoNotaFiscal where tipoNotaFiscal.tipoAtividade = :tipoAtividade group by tipoNotaFiscal.id ";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("tipoAtividade", tipoAtividade);
		
		return query.list();
	}
	
	private String getHqlConsulta(FiltroNaturezaOperacaoDTO filtro, boolean count){
		
		StringBuilder hql  = new StringBuilder();
		
		if(count){
			
			hql.append(" select no.id from NaturezaOperacao no ");
		}
		else{
			hql.append(" select no from NaturezaOperacao no ");
		}
		
		if((filtro.getTipoNota()!= null && !filtro.getTipoNota().trim().isEmpty()) 
				|| filtro.getTipoAtividade()!= null){
			
			hql.append(" where ");
		}
		
		if(filtro.getTipoNota()!= null && !filtro.getTipoNota().trim().isEmpty() ){
			hql.append(" upper(no.descricao) like(:tipoDescNota) ");
		}
		
		if(filtro.getTipoAtividade()!= null 
				&& (filtro.getTipoNota()!= null && !filtro.getTipoNota().trim().isEmpty())){
			hql.append(" AND ");
		}
		
		if(filtro.getTipoAtividade()!= null){
			hql.append("  no.tipoAtividade = :tipoAtividade ");
		}
		
		hql.append(" group by no.descricao, no.tipoAtividade, no.cfopEstado, no.cfopOutrosEstados  ");
		
		return hql.toString();
	}
	
	private String getOrdenacaoConsulta(FiltroNaturezaOperacaoDTO filtro){
		
		if (filtro == null || filtro.getOrdenacaoColuna() == null) {
			return "";
		}

		StringBuilder hql = new StringBuilder();

		switch (filtro.getOrdenacaoColuna()) {
			
			case OPERACAO:
				hql.append(" order by no.tipoAtividade  ");
				break;
			case DESCRICAO:
				hql.append(" order by no.descricao  ");
				break;
			case CFOP_ESTADO :
				hql.append(" order by no.cfopEstado  ");
				break;
			case CFOP_OUTROS_ESTADOS:
				hql.append(" order by no.cfopOutrosEstados ");
				break;
			default:
				hql.append(" order by no.tipoAtividade ");
		}

		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append(filtro.getPaginacao().getOrdenacao().toString());
		}

		return hql.toString();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List <NaturezaOperacao> obterTiposNotaFiscal(GrupoNotaFiscal grupoNotaFiscal) {
		
		String hql = " from TipoNotaFiscal tipoNotaFiscal where tipoNotaFiscal.grupoNotaFiscal = :grupoNotaFiscal group by tipoNotaFiscal.id  ";
		
		Query query = getSession().createQuery(hql);
		
		query.setParameter("grupoNotaFiscal", grupoNotaFiscal);
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NaturezaOperacao> obterNaturezasOperacoes(TipoOperacao tipoOperacao,
			TipoUsuarioNotaFiscal tipoDestinatario, TipoUsuarioNotaFiscal tipoEmitente,
			GrupoNotaFiscal[] grupoNotaFiscal) {

		StringBuilder hql = new StringBuilder("");
		hql.append("from TipoNotaFiscal tipoNotaFiscal ");
		hql.append("where 1=1 ");
		
		if(tipoOperacao != null)
			hql.append("and tipoNotaFiscal.tipoOperacao = :tipoOperacao ");
		
		if(tipoDestinatario != null)
			hql.append("and tipoNotaFiscal.destinatario = :tipoDestinatario ");
		
		if(tipoEmitente != null)
			hql.append("and tipoNotaFiscal.emitente = :tipoEmitente ");
		
		if(grupoNotaFiscal != null && grupoNotaFiscal.length > 0)
			hql.append("and tipoNotaFiscal.grupoNotaFiscal IN (:grupoNotaFiscal) ");
		
		hql.append("order by tipoNotaFiscal.descricao ");
		
		Query query = getSession().createQuery(hql.toString());
		
		if(tipoOperacao != null)
			query.setParameter("tipoOperacao", tipoOperacao);
		
		if(tipoDestinatario != null)
			query.setParameter("tipoDestinatario", tipoDestinatario);
		
		if(tipoEmitente != null)
			query.setParameter("tipoEmitente", tipoEmitente);
		
		if(grupoNotaFiscal != null && grupoNotaFiscal.length > 0)
			query.setParameterList("grupoNotaFiscal", grupoNotaFiscal);
		
		return query.list();
		
	}
	
	@Override
	public NaturezaOperacao obterNaturezaOperacao(Long idNaturezaOperacao) {
		String hql = " from NaturezaOperacao no where no.id = :id ";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("id", idNaturezaOperacao);
		
		return (br.com.abril.nds.model.fiscal.NaturezaOperacao) query.uniqueResult();
		
	}

	@Override
	public NaturezaOperacao obterNaturezaOperacao(TipoAtividade tipoAtividade, TipoDestinatario tipoDestinatario, TipoOperacao tipoOperacao) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" from NaturezaOperacao nat ")
		   .append(" where nat.tipoAtividade = :tipoAtividade ")
		   .append(" and nat.tipoDestinatario = :tipoDestinatario ")
		   .append(" and nat.tipoOperacao = :tipoOperacao ");
		
		
		Query query = getSession().createQuery(sql.toString());
		query.setParameter("tipoAtividade", tipoAtividade);
		query.setParameter("tipoDestinatario", tipoDestinatario);
		query.setParameter("tipoOperacao", tipoOperacao);
		
		return (NaturezaOperacao) query.uniqueResult();
	}

	@Override
	public NaturezaOperacao obterNaturezaOperacaoDevolucaoSimbolica(TipoAtividade tipoAtividade, TipoDestinatario tipoDestinatario) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" from NaturezaOperacao nat ")
		   .append(" where nat.tipoAtividade = :tipoAtividade ")
		   .append(" and nat.tipoDestinatario = :tipoDestinatario ")
		   .append(" and nat.notaFiscalDevolucaoSimbolica = :true ");
		
		Query query = getSession().createQuery(sql.toString());
		query.setParameter("true", true);
		query.setParameter("tipoAtividade", tipoAtividade);
		query.setParameter("tipoDestinatario", tipoDestinatario);
		
		return (NaturezaOperacao) query.uniqueResult();
	}

	@Override
	public NaturezaOperacao obterNaturezaOperacaoVendaConsignado(TipoAtividade tipoAtividade, TipoDestinatario tipoDestinatario) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" from NaturezaOperacao nat ")
		   .append(" where nat.tipoAtividade = :tipoAtividade ")
		   .append(" and nat.tipoDestinatario = :tipoDestinatario ");
		
		
		Query query = getSession().createQuery(sql.toString());
		query.setParameter("tipoAtividade", tipoAtividade);
		query.setParameter("tipoDestinatario", tipoDestinatario);;
		
		return (NaturezaOperacao) query.uniqueResult();
	}
}