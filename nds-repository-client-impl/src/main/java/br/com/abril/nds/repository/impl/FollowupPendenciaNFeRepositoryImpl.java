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

	@Override
	public Long qtdeRegistrosPendencias(FiltroFollowupPendenciaNFeDTO filtro) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT count(cota.id) ");
		hql.append(getSqlFromEWhereNotaPendente(filtro));
		hql.append(getOrderByNotasPendentes(filtro, true, true));

		Query query =  getSession().createQuery(hql.toString());		
		
		return (long) query.list().size();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaFollowupPendenciaNFeDTO> consultaPendenciaNFEEncalhe(FiltroFollowupPendenciaNFeDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT cota.numeroCota as numeroCota, ");
		hql.append(" pessoa.nome as nomeJornaleiro, ");
		hql.append(" confCota.dataOperacao as dataEntrada, ");		
		hql.append(" nf.statusNotaFiscal as tipoPendencia, ");
		hql.append(" ( ");
		hql.append("  	item.preco - (SELECT SUM(notaFiscalEntradaCota.valorDesconto) ");
		hql.append("  	FROM ControleConferenciaEncalheCota controleConferenciaDesconto ");
		hql.append("  	LEFT JOIN controleConferenciaDesconto.notaFiscalEntradaCota as notaFiscalEntradaCota ");
		hql.append("  	WHERE controleConferenciaDesconto.processoUtilizaNfe = true and controleConferenciaDesconto = confCota ");
		hql.append("  )) as valorDiferenca, ");
		hql.append(" concat(telefone.ddd, ' ', telefone.numero)  as numeroTelefone, ");
		hql.append(" nf.serie as serie, ");
		hql.append(" nf.chaveAcesso as chaveAcesso, ");
		hql.append(" nf.numero as numeroNfe, ");
		hql.append(" nf.id as idNotaFiscalEntrada, ");
		hql.append(" ( ");
		hql.append("  SELECT SUM(notaFiscalEntradaCota.valorDesconto) ");
		hql.append("  	FROM ControleConferenciaEncalheCota controleConferenciaDesconto ");
		hql.append("  	LEFT JOIN controleConferenciaDesconto.notaFiscalEntradaCota as notaFiscalEntradaCota ");
		hql.append("  	WHERE controleConferenciaDesconto.processoUtilizaNfe = true and controleConferenciaDesconto = confCota ");
		hql.append("  ) as valorNota, ");
		hql.append(" confCota.dataOperacao as dataEncalhe, ");
		hql.append(" confCota.id as idControleConferenciaEncalheCota ");
		
		hql.append(getSqlFromEWhereNotaPendente(filtro));
		
		hql.append(getOrderByNotasPendentes(filtro, false, false));

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
	
		hql.append(" from ItemNotaFiscalEntrada as item ");
		hql.append(" LEFT JOIN item.notaFiscal as nf ");
		hql.append(" LEFT JOIN nf.controleConferenciaEncalheCota as confCota ");
		hql.append(" LEFT JOIN confCota.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN pessoa.telefones as telefone ");
		hql.append(" where confCota.processoUtilizaNfe = true ");
		hql.append(" and ");
		hql.append(" ( ");
		hql.append("  	item.preco - (SELECT SUM(notaFiscalEntradaCota.valorDesconto) ");
		hql.append("  	FROM ControleConferenciaEncalheCota controleConferenciaDesconto ");
		hql.append("  	LEFT JOIN controleConferenciaDesconto.notaFiscalEntradaCota as notaFiscalEntradaCota ");
		hql.append("  	WHERE controleConferenciaDesconto.processoUtilizaNfe = true and controleConferenciaDesconto = confCota ");
		hql.append("  ) <> 0 )");
		
		hql.append(" GROUP BY cota.numeroCota");
		
		return hql.toString();
	}
	
	private String getOrderByNotasPendentes(FiltroFollowupPendenciaNFeDTO filtro, boolean isCount, boolean isPagination){
		
		StringBuilder hql = new StringBuilder();
		
		if(filtro.getPaginacao() == null || filtro.getPaginacao().getSortColumn() == null){
			return "";
		}
		
		hql.append(" order by cota.numeroCota ");
		
		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append( filtro.getPaginacao().getOrdenacao().toString());
		}

		if(!isCount && !isPagination){
			if(filtro.getPaginacao()!=null && filtro.getPaginacao().getSortOrder() != null && filtro.getPaginacao().getSortColumn() != null) {
				hql.append(" ORDER BY  ").append(filtro.getPaginacao().getSortColumn()).append(" ").append(filtro.getPaginacao().getSortOrder());
			}
		}
		
		return hql.toString();
	}

}
