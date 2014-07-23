package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.HistoricoSituacaoCotaVO;
import br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.repository.AbstractRepositoryModel;
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
	public List<HistoricoSituacaoCotaVO> obterHistoricoStatusCota(FiltroStatusCotaDTO filtro) {
		
		return obterHistorico(filtro,false,false);
	}

	@SuppressWarnings("unchecked")
	private List<HistoricoSituacaoCotaVO> obterHistorico(FiltroStatusCotaDTO filtro,boolean totalizarResultados  ,boolean filtrarUltimaDataHistorico) {
		
		String hql = this.criarQueryHistoricoStatusCota(filtro, totalizarResultados,filtrarUltimaDataHistorico);
		
		hql = this.adicionarOrdenacaoQueryHistoricoStatusCota(hql, filtro);
		
		Query query = super.getSession().createQuery(hql);
		
		this.configurarParametrosQueryHistoricoStatusCota(query, filtro);
		
		this.configurarPaginacaoQueryHistoricoStatusCota(query, filtro);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(HistoricoSituacaoCotaVO.class));
		
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
	@SuppressWarnings("deprecation")
	private String criarQueryHistoricoStatusCota(FiltroStatusCotaDTO filtro, boolean totalizarResultados, boolean filtrarUltimaDataHistorico) {
		
		String hql = "select ";
		
		if (totalizarResultados){
			
			hql += " count(hsc) ";
		} else {
			
			hql += "c.numeroCota as numeroCota, ";
			hql += "coalesce(p.nome, p.razaoSocial) as nomeCota, ";
			hql += "hsc.dataInicioValidade as data, ";
			hql += "hsc.situacaoAnterior as statusAnterior, ";
			hql += "hsc.novaSituacao as statusAtualizado, ";
			hql += "responsavel.nome as usuario, ";
			hql += "hsc.motivo as motivo, ";
			hql += "hsc.descricao as descricao, ";
			hql += "case when hsc.processado is null then false else hsc.processado end as processado ";
		}
		
		hql += " from HistoricoSituacaoCota hsc ";
		hql += " join hsc.cota c ";
		hql += " join c.pessoa p ";
		hql += " join hsc.responsavel responsavel ";
		hql += " where 1 = 1 ";
		
		if (filtro != null) {
			
			if (filtro.getNumeroCota() != null) {

				hql += " and hsc.cota.numeroCota = :numeroCota ";
			}
			
			if (filtro.getStatusCota() != null) {
				
				hql += " and hsc.novaSituacao = :statusCota ";
			}
			
			if (filtro.getPeriodo() != null) {
				
				if (filtro.getPeriodo().getDataInicial() != null
						&& filtro.getPeriodo().getDataFinal() != null) {
					
					hql += " and hsc.dataInicioValidade between :dataInicial and :dataFinal ";
					
				} else if (filtro.getPeriodo().getDataInicial() != null) {
					
					hql += " and hsc.dataInicioValidade >= :dataInicial ";
					
				} else if (filtro.getPeriodo().getDataFinal() != null) {
					
					hql += " and hsc.dataInicioValidade <= :dataFinal ";
				}
			}
			
			if (filtro.getMotivoStatusCota() != null) {
				
				hql += " and hsc.motivo = :motivo ";
			}
			
			if (filtrarUltimaDataHistorico) {
				
				hql += " and hsc.id = (";
				hql += " select max(_h.id) from HistoricoSituacaoCota _h ";
				hql += " where _h.processado = true ";
				hql += " and _h.cota.id = hsc.cota.id ";
				hql += ")";
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
					hql += "order by numeroCota ";
					break;
				case NOME_COTA :
					hql += "order by nomeCota ";
					break;
				case DATA:
					hql += "order by data  ";
					break;
				case DESCRICAO:
					hql += "order by descricao  ";
					break;
				case MOTIVO:
					hql += "order by motivo ";
					break;
				case STATUS_ANTERIOR:
					hql += "order by statusAnterior ";
					break;
				case STATUS_ATUALIZADO:
					hql += "order by statusAtualizado ";
					break;
				case USUARIO:
					hql += "order by usuario ";
					break;
				default:
					break;
			}
			
			if (filtro.getPaginacao().getOrdenacao() != null) {
				
				if(filtro.getOrdenacaoColuna().equals(FiltroStatusCotaDTO.OrdenacaoColunasStatusCota.DATA)){
					hql += filtro.getPaginacao().getOrdenacao().toString() + " ,hsc.dataEdicao desc ";
				}
				else{
					hql += filtro.getPaginacao().getOrdenacao().toString();
				}
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
	@SuppressWarnings("deprecation")
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
	 * @see br.com.abril.nds.repository.HistoricoSituacaoCotaRepository#obterUltimoHistorico(Long, SituacaoCadastro)
	 */
	public HistoricoSituacaoCota obterUltimoHistorico(Integer numeroCota, SituacaoCadastro situacaoCadastro){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select h from HistoricoSituacaoCota h JOIN h.cota cota ")
		   .append(" where cota.numeroCota = :numeroCota ")
		   .append(" and h.processado = true ")
		   .append(" and cota.situacaoCadastro = h.novaSituacao ");
		
		if (situacaoCadastro != null){
			
			hql.append(" and cota.situacaoCadastro = :situacaoCadastro ");
		}
		
		hql.append(" order by cota.inicioAtividade, h.dataEdicao desc ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("numeroCota", numeroCota);
		
		if (situacaoCadastro != null){
			
			query.setParameter("situacaoCadastro", situacaoCadastro);
		}
		
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
		criteria.add(Restrictions.eq("processado", true));
		return (Date) criteria.uniqueResult();
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.HistoricoSituacaoCotaRepository#buscarDataUltimaSuspensaoCotas()
	 */
	@Override
	public Date buscarDataUltimaSuspensaoCotas() {
		Criteria criteria = getSession().createCriteria(HistoricoSituacaoCota.class);
		criteria.setProjection(Projections.max("dataEdicao"));
		criteria.add(Restrictions.eq("processado", true));
		return (Date) criteria.uniqueResult();
	}
	
	@Override
	public List<HistoricoSituacaoCotaVO> obterUltimoHistoricoStatusCota(FiltroStatusCotaDTO filtro) {
		
		return obterHistorico(filtro, false, true);
		
	}
	
	@Override
	public Long obterTotalUltimoHistoricoStatusCota(FiltroStatusCotaDTO filtro) {
		
		return obterTotalHistorico(filtro, true, true);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<HistoricoSituacaoCota> obterNaoProcessadosComInicioEm(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select h from HistoricoSituacaoCota h ")
		   .append(" where h.processado = :processado ")
		   .append(" and h.dataInicioValidade = :data ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("data", data);
		query.setParameter("processado", false);
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<HistoricoSituacaoCota> obterNaoRestauradosComTerminoEm(Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select h from HistoricoSituacaoCota h ")
		   .append(" where h.processado = :processado ")
		   .append(" and h.dataFimValidade = :data ")
		   .append(" and h.restaurado = :restaurado ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("data", data);
		query.setParameter("processado", true);
		query.setParameter("restaurado", false);
		
		return query.list();
	}
	
}
