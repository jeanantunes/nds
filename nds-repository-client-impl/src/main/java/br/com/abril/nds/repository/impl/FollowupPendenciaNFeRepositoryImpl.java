package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaFollowupPendenciaNFeDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupPendenciaNFeDTO;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FollowupPendenciaNFeRepository;

@Repository
public class FollowupPendenciaNFeRepositoryImpl extends AbstractRepositoryModel<NotaFiscalEntrada,Long> implements FollowupPendenciaNFeRepository {

	public FollowupPendenciaNFeRepositoryImpl() {
		super(NotaFiscalEntrada.class);		 
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaFollowupPendenciaNFeDTO> obterPendencias(FiltroFollowupPendenciaNFeDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT cota.numeroCota as numeroCota, ");
		hql.append(" pessoa.nome as nomeJornaleiro, ");
		hql.append(" conf.data as dataEntrada, ");		
		hql.append(" notaCota.statusNotaFiscal as tipoPendencia, ");		
		hql.append(" ((conf.qtdeInformada * conf.precoComDesconto) -  (conf.qtde * conf.precoComDesconto)) as valorDiferenca, ");
		hql.append(" concat(telefone.ddd, ' ', telefone.numero)  as numeroTelefone, ");
		hql.append(" notaCota.serie as serie, ");
		hql.append(" notaCota.chaveAcesso as chaveAcesso, ");
		hql.append(" notaCota.numero as numeroNfe, ");
		hql.append(" notaCota.id as idNotaFiscalEntrada ");
		
		hql.append(getSqlFromEWhereNotaPendente(filtro));
		
		hql.append(getOrderByNotasPendentes(filtro, false, false, false));

		Query query =  getSession().createQuery(hql.toString());		
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ConsultaFollowupPendenciaNFeDTO.class));
		
		if(filtro.getPaginacao() != null) {
			if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			
			if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());			
		}
		
		return query.list();
	}
	
	private String getSqlFromEWhereNotaPendente(FiltroFollowupPendenciaNFeDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
	
		hql.append(" from ControleConferenciaEncalheCota as controleCota ");
		hql.append(" LEFT JOIN controleCota.notaFiscalEntradaCota as notaCota ");
		hql.append(" LEFT JOIN notaCota.cota as cota ");
		hql.append(" LEFT JOIN notaCota.naturezaOperacao as no ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN controleCota.conferenciasEncalhe as conf ");
		hql.append(" LEFT JOIN pessoa.telefones as telefone ");
		
		hql.append(" where no.tipoOperacao in ('ENTRADA',  'SAIDA')");
		hql.append(" and ((conf.qtdeInformada * conf.precoComDesconto) - (conf.qtde * notaCota.valorInformado)) <> 0 ");
		
		return hql.toString();
	}
	
	private String getOrderByNotasPendentes(FiltroFollowupPendenciaNFeDTO filtro, boolean isCount, boolean isPagination, boolean isGroup){
		
		if(filtro.getPaginacao() == null || filtro.getPaginacao().getSortColumn() == null){
			return "";
		}
		StringBuilder hql = new StringBuilder();
		
		hql.append(" order by cota.numeroCota ");
		
		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append( filtro.getPaginacao().getOrdenacao().toString());
		}
		
		
		if(!isGroup){
			hql.append(" GROUP BY cota.numeroCota");
		} else {
			hql.append(" GROUP BY cota ");
		}

		if(!isCount && !isPagination){
			if(filtro.getPaginacao()!=null && filtro.getPaginacao().getSortOrder() != null && filtro.getPaginacao().getSortColumn() != null) {
				hql.append(" ORDER BY  ").append(filtro.getPaginacao().getSortColumn()).append(" ").append(filtro.getPaginacao().getSortOrder());
			}
		}
		
		return hql.toString();
	}

	@Override
	public Long totalPendenciaNFEEncalhe(FiltroFollowupPendenciaNFeDTO filtro) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT COUNT(cota.id) ");
		hql.append(getSqlFromEWhereNotaPendente(filtro));
		hql.append(getOrderByNotasPendentes(filtro, true, true, false));

		Query query =  getSession().createQuery(hql.toString());		
		
		return (long) query.list().size();
	}

}
