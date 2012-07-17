package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaFollowupStatusCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupStatusCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.repository.FollowupStatusCotaRepository;

@Repository
public class FollowupStatusCotaRepositoryImpl  extends AbstractRepositoryModel<Cota,Long> implements FollowupStatusCotaRepository {

	public FollowupStatusCotaRepositoryImpl() {
		super(Cota.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaFollowupStatusCotaDTO> obterConsignadosParaChamadao(
			FiltroFollowupStatusCotaDTO filtro) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT cota.numeroCota as numeroCota, ");
		hql.append("pessoa.nome as nomeJornaleiro, ");
		hql.append("historico.dataInicioValidade as dataInicioPeriodo, ");
		hql.append("historico.dataFimValidade as dataFimPeriodo, ");
		hql.append("historico.situacaoAnterior as statusAtual, ");
		hql.append("historico.novaSituacao as statusNovo ");			
		
		hql.append(getSqlFromEWhereStatusCota(filtro));
		
		hql.append(getOrderByStatusCota(filtro));

		Query query =  getSession().createQuery(hql.toString());		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaFollowupStatusCotaDTO.class));
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());			
		 
		return query.list();
	}
	
	private Object getSqlFromEWhereStatusCota(FiltroFollowupStatusCotaDTO filtro) {
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from HistoricoSituacaoCota as historico, Distribuidor as distribuidor ");		
		hql.append(" LEFT JOIN historico.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		
		hql.append(" where distribuidor.dataOperacao = historico.dataInicioValidade ");	
		
		return hql.toString();
	}

	private Object getOrderByStatusCota(FiltroFollowupStatusCotaDTO filtro) {
		
		if(filtro.getPaginacao() == null || filtro.getPaginacao().getSortColumn() == null){
			return "";
		}
		StringBuilder hql = new StringBuilder();
		
		hql.append(" order by cota.numeroCota ");
		
		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append( filtro.getPaginacao().getOrdenacao().toString());
		}
		
		return hql.toString();
	}

	

}
