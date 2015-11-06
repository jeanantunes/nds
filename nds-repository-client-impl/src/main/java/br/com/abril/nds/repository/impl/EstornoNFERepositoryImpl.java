package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.NotaFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstornoNFERepository;

@Repository
public class EstornoNFERepositoryImpl extends AbstractRepositoryModel<NotaFiscal, Long> implements EstornoNFERepository {

	public EstornoNFERepositoryImpl() {
		super(NotaFiscal.class);
	}

	@Override
	public Long quantidade(FiltroMonitorNfeDTO filtro) {
		
		// OBTER Quantidade de registros
		StringBuilder hql = new StringBuilder("SELECT ");
		hql.append(" COUNT(distinct notaFiscal.id) ");
		
		Query query = createFiltroQuery(queryEstornoNFE(filtro, hql, true, true, false), filtro);
		
		return (long) query.list().size();
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<NotaFiscalDTO> obterListaNotasFiscaisEstorno(FiltroMonitorNfeDTO filtro) {
		
		StringBuilder hql = new StringBuilder("");

		hql.append(" SELECT ")
		.append(" notaFiscal.id as id,")
		.append(" ident.numeroDocumentoFiscal as numeroNota,")
		.append(" ident.serie as serieNota,")
		.append(" ident.dataEmissao as dataEmissao,")
		.append(" infElet.chaveAcesso as chaveAcesso ");

		Query query = createFiltroQuery(queryEstornoNFE(filtro, hql, false, false, false), filtro);

		if(filtro.getPaginacao() != null) {
			if(filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}

			if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(NotaFiscalDTO.class));
		
		return query.list();
	}
	
	private StringBuilder queryEstornoNFE(FiltroMonitorNfeDTO filtro, StringBuilder hql, boolean isCount, boolean isPagination, boolean isGroup){

		hql.append(" FROM NotaFiscal as notaFiscal")
		.append(" JOIN notaFiscal.notaFiscalInformacoes as nfi ")
		.append(" JOIN nfi.identificacao as ident ")
		.append(" JOIN nfi.identificacaoEmitente as identEmit ")
		.append(" JOIN nfi.identificacaoDestinatario as identDest")
		.append(" JOIN nfi.informacaoEletronica as infElet ")
		.append(" JOIN ident.naturezaOperacao as natOp ")
		.append(" LEFT JOIN natOp.processo as proc ")
		.append(" JOIN identEmit.documento as doc ")
		.append(" JOIN identDest.documento as docDest ");

		hql.append(" WHERE 1=1 ");

		if(filtro.getDataInicial() != null) {
			hql.append(" AND ident.dataEmissao between :dataInicial and :dataFinal ");
		}

		if(filtro.getNumeroNotaInicial() != null) {
			hql.append(" AND ident.numeroDocumentoFiscal = :numeroInicial ");
		}

		if(filtro.getChaveAcesso() != null && !filtro.getChaveAcesso().isEmpty()) {
			hql.append(" AND infElet.chaveAcesso = :chaveAcesso ");
		}
		
		if(filtro.getSerie() != null) {
			hql.append(" AND ident.serie = :serie ");
		}
		
		if(!isGroup) {
			hql.append(" GROUP BY notaFiscal.id ");
		}
		
		if(!isCount && !isPagination){
			if(filtro.getPaginacao() !=null && filtro.getPaginacao().getSortOrder() != null && filtro.getPaginacao().getSortColumn() != null) {
				hql.append(" ORDER BY  ").append(filtro.getPaginacao().getSortColumn()).append(" ").append(filtro.getPaginacao().getSortOrder());
			}
		}

		return hql;
	}
	
	private Query createFiltroQuery(StringBuilder hql, FiltroMonitorNfeDTO filtro) {

		Query query = this.getSession().createQuery(hql.toString());

		if(filtro.getDataInicial()!=null) {
			query.setParameter("dataInicial", filtro.getDataInicial());
			query.setParameter("dataFinal", filtro.getDataFinal());
		}

		if(filtro.getNumeroNotaInicial()!=null) {
			query.setParameter("numeroInicial", Long.valueOf(filtro.getNumeroNotaInicial()));
		}

		if(filtro.getChaveAcesso()!=null && !filtro.getChaveAcesso().isEmpty()) {
			query.setParameter("chaveAcesso", filtro.getChaveAcesso());
		}
		
		if(filtro.getSerie() !=null) {
			query.setParameter("serie", Long.valueOf(filtro.getSerie()));
		}

		if(filtro.getSituacaoNfe()!=null && !filtro.getSituacaoNfe().isEmpty()) {
			query.setParameter("situacaoNfe", filtro.getSituacaoNfe());
		}
		
		return query;
	}
}
