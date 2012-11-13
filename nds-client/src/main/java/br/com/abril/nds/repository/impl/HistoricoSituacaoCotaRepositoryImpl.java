package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.repository.HistoricoSituacaoCotaRepository;

/**
 * Repositório de histórico de status de cotas.
 * 
 * @author Discover Technology
 *
 */
@Repository
public class HistoricoSituacaoCotaRepositoryImpl extends AbstractRepositoryModel<HistoricoSituacaoCota, Long> implements HistoricoSituacaoCotaRepository {

	/**
	 * Construtor.
	 */
	public HistoricoSituacaoCotaRepositoryImpl() {
		
		super(HistoricoSituacaoCota.class);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.HistoricoSituacaoCotaRepository#obterHistoricosStatusCota(br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO)
	 */
	@SuppressWarnings("unchecked")
	public List<HistoricoSituacaoCota> obterHistoricoStatusCota(FiltroStatusCotaDTO filtro) {
		
		return obterHistorico(filtro,false,false);
	}

	private List<HistoricoSituacaoCota> obterHistorico(FiltroStatusCotaDTO filtro,boolean totalizarResultados  ,boolean filtrarUltimaDataHistorico) {
		
		String hql = this.criarQueryHistoricoStatusCota(filtro, totalizarResultados,filtrarUltimaDataHistorico);
		
		hql = this.adicionarOrdenacaoQueryHistoricoStatusCota(hql, filtro);
		
		Query query = super.getSession().createQuery(hql);
		
		this.configurarParametrosQueryHistoricoStatusCota(query, filtro);
		
		this.configurarPaginacaoQueryHistoricoStatusCota(query, filtro);
		
		return query.list();
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.HistoricoSituacaoCotaRepository#obterTotalHistoricoStatusCota(br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO)
	 */
	public Long obterTotalHistoricoStatusCota(FiltroStatusCotaDTO filtro) {
		
		return obterTotalHistorico(filtro,true,false);
	}

	private Long obterTotalHistorico(FiltroStatusCotaDTO filtro,boolean totalizarResultados  ,boolean filtrarUltimaDataHistorico) {
		
		String hql = this.criarQueryHistoricoStatusCota(filtro, totalizarResultados,filtrarUltimaDataHistorico);
		
		hql = this.adicionarOrdenacaoQueryHistoricoStatusCota(hql, filtro);
		
		Query query = super.getSession().createQuery(hql);
		
		this.configurarParametrosQueryHistoricoStatusCota(query, filtro);
		
		return (Long) query.uniqueResult();
	}
	
	/*
	 * Cria a query para a busca de histórico de status de cota.
	 *  
	 * @param filtro - filtro de pesquisa
	 * @param totalizarResultados - flag para totalizar ou não os resultados
	 * 
	 * @return Query string
	 */
	private String criarQueryHistoricoStatusCota(FiltroStatusCotaDTO filtro, boolean totalizarResultados, boolean filtrarUltimaDataHistorico) {
		
		String hql = "select ";
		
		hql += totalizarResultados ? " count(hsc) " : " hsc ";
		
		hql += " from HistoricoSituacaoCota hsc ";
		hql += " join hsc.cota c ";
		hql += " join c.pessoa p ";
		
		if (filtro != null) {
			
			boolean useWhere = true;
			
			if (filtro.getNumeroCota() != null) {
				
				hql += useWhere ? " where " : " and ";
				
				hql += " hsc.cota.numeroCota = :numeroCota ";
				
				useWhere = false;
			}
			
			if (filtro.getStatusCota() != null) {
				
				hql += useWhere ? " where " : " and ";
				
				hql += " hsc.novaSituacao = :statusCota ";
				
				useWhere = false;
			}
			
			if (filtro.getPeriodo() != null) {
				
				if (filtro.getPeriodo().getDataInicial() != null
						&& filtro.getPeriodo().getDataFinal() != null) {
					
					hql += useWhere ? " where " : " and ";
					
					hql += " hsc.dataInicioValidade between :dataInicial and :dataFinal ";
					
					useWhere = false;
					
				} else if (filtro.getPeriodo().getDataInicial() != null) {
					
					hql += useWhere ? " where " : " and ";
					
					hql += " hsc.dataInicioValidade >= :dataInicial ";
					
					useWhere = false;
					
				} else if (filtro.getPeriodo().getDataFinal() != null) {
					
					hql += useWhere ? " where " : " and ";
					
					hql += " hsc.dataInicioValidade <= :dataFinal ";
					
					useWhere = false;
				}
			}
			
			if (filtro.getMotivoStatusCota() != null) {
				
				hql += useWhere ? " where " : " and ";
				
				hql += " hsc.motivo = :motivo ";
				
				useWhere = false;
			}
			
			if(filtrarUltimaDataHistorico){
				
				hql += useWhere ? " where " : " and ";
				
				hql += " hsc.dataInicioValidade = ( select max(hs.dataInicioValidade) from HistoricoSituacaoCota hs where hs.cota.numeroCota = hsc.cota.numeroCota )  ";
				
				useWhere = false;
			}
		}
		
		return hql;
	}
	
	/*
	 * Adiciona ordenação (se houver) à query de histórico de status de cota.
	 * 
	 * @param hql - query
	 * @param filtro - filtro da pesquisa
	 * 
	 * @return Query com ordenação (se houver)
	 */
	private String adicionarOrdenacaoQueryHistoricoStatusCota(String hql, FiltroStatusCotaDTO filtro) {
		
		if (filtro != null && filtro.getOrdenacaoColuna() != null) {
			
			switch (filtro.getOrdenacaoColuna()) {
			
				case NUMERO_COTA :
					hql += "order by c.numeroCota ";
					break;
				case NOME_COTA :
					hql += "order by CASE WHEN p.class = 'J' THEN p.razaoSocial else p.nome END ";
					break;
				case DATA:
					hql += "order by hsc.dataInicioValidade ";
					break;
				case DESCRICAO:
					hql += "order by hsc.descricao ";
					break;
				case MOTIVO:
					hql += "order by hsc.motivo ";
					break;
				case STATUS_ANTERIOR:
					hql += "order by hsc.situacaoAnterior ";
					break;
				case STATUS_ATUALIZADO:
					hql += "order by hsc.novaSituacao ";
					break;
				case USUARIO:
					hql += "order by hsc.responsavel ";
					break;
				default:
					break;
			}
			
			if (filtro.getPaginacao().getOrdenacao() != null) {
				
				hql += filtro.getPaginacao().getOrdenacao().toString();
			}
		}
		
		return hql;
	}
	
	/*
	 * Configura os parâmetros da query de acordo com o filtro de pesquisa.
	 * 
	 * @param query - query
	 * @param filtro - filtro de pesquisa
	 */
	private void configurarParametrosQueryHistoricoStatusCota(Query query, FiltroStatusCotaDTO filtro) {
		
		if (filtro != null) {
			
			if (filtro.getNumeroCota() != null) {
				
				query.setParameter("numeroCota", filtro.getNumeroCota());
			}
			
			if (filtro.getStatusCota() != null) {
				
				query.setParameter("statusCota", filtro.getStatusCota());
			}
			
			if (filtro.getPeriodo() != null) {
				
				if (filtro.getPeriodo().getDataInicial() != null) {
					
					query.setParameter("dataInicial", filtro.getPeriodo().getDataInicial());
				}
				
				if (filtro.getPeriodo().getDataFinal() != null) {
					
					query.setParameter("dataFinal", filtro.getPeriodo().getDataFinal());
				}
			}
			
			if (filtro.getMotivoStatusCota() != null) {
				
				query.setParameter("motivo", filtro.getMotivoStatusCota());
			}
		}
	}
	
	/*
	 * Configura a paginação na query de histórico de status de cota.
	 * 
	 * @param query- query
	 * @param filtro - filtro de pesquisa
	 */
	private void configurarPaginacaoQueryHistoricoStatusCota(Query query, FiltroStatusCotaDTO filtro) {
		
		if (filtro != null) {
			
			if (filtro.getPaginacao() != null) {
				
				if (filtro.getPaginacao().getPosicaoInicial() != null) {
				
					query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
				}
				
				if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				
					query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
				}
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.HistoricoSituacaoCotaRepository#obterUltimoHistoricoInativo(Long)
	 */
	public HistoricoSituacaoCota obterUltimoHistoricoInativo(Integer numeroCota){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select h from  HistoricoSituacaoCota h JOIN h.cota cota ")
		.append(" where cota.numeroCota =:numeroCota")
		.append(" and cota.situacaoCadastro = h.novaSituacao ")
		.append(" and cota.situacaoCadastro =:situacaoCadastro ")
		.append(" order by cota.inicioAtividade, h.dataEdicao desc ");
		
		Query query  = getSession().createQuery(hql.toString());
		query.setParameter("numeroCota", numeroCota);
		query.setParameter("situacaoCadastro", SituacaoCadastro.INATIVO);
		query.setMaxResults(1);
		
		return (HistoricoSituacaoCota) query.uniqueResult();
		
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.HistoricoSituacaoCotaRepository#buscarUltimaSuspensaoCotasDia(java.util.Date)
	 */
	@Override
	public Date buscarUltimaSuspensaoCotasDia(Date dataOperacao) {
		Criteria criteria = getSession().createCriteria(HistoricoSituacaoCota.class);
		criteria.setProjection(Projections.max("dataEdicao"));
		criteria.add(Restrictions.eq("novaSituacao", SituacaoCadastro.SUSPENSO));
		criteria.add(Restrictions.eq("dataEdicao", dataOperacao));
		return (Date) criteria.uniqueResult();
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.HistoricoSituacaoCotaRepository#buscarDataUltimaSuspensaoCotas()
	 */
	@Override
	public Date buscarDataUltimaSuspensaoCotas() {
		Criteria criteria = getSession().createCriteria(HistoricoSituacaoCota.class);
		criteria.setProjection(Projections.max("dataEdicao"));
		return (Date) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<HistoricoSituacaoCota> obterUltimoHistoricoStatusCota(FiltroStatusCotaDTO filtro) {
		
		return obterHistorico(filtro, false, true);
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Long obterTotalUltimoHistoricoStatusCota(FiltroStatusCotaDTO filtro) {
		
		return obterTotalHistorico(filtro, true, true);
	}
}
