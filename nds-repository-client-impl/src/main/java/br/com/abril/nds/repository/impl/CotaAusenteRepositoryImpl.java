package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaAusenteDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaAusenteDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaAusenteDTO.ColunaOrdenacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.movimentacao.CotaAusente;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.CotaAusenteRepository;

@Repository
public class CotaAusenteRepositoryImpl extends AbstractRepositoryModel<CotaAusente, Long> implements CotaAusenteRepository { 
	
	/**
	 * Construtor.
	 */
	public CotaAusenteRepositoryImpl() {
		
		super(CotaAusente.class);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<CotaAusenteDTO> obterCotasAusentes(FiltroCotaAusenteDTO filtro) {
						
		StringBuilder queryNative = new StringBuilder();
		
		queryNative.append("SELECT 																				"); 		
		queryNative.append("ca.ID as idCotaAusente, 															");
		queryNative.append("ca.DATA as data, 																	");
		queryNative.append("box.NOME as box, 																	");
		queryNative.append("cota.NUMERO_COTA as cota,															");
	    queryNative.append("(case when (pessoa.TIPO = 'F') then pessoa.NOME else pessoa.RAZAO_SOCIAL end) AS nome, ");
		queryNative.append("( SELECT SUM(movEstoque.QTDE*pe.PRECO_VENDA) FROM MOVIMENTO_ESTOQUE_COTA movEstoque ");
		queryNative.append("JOIN PRODUTO_EDICAO pe ON (movEstoque.PRODUTO_EDICAO_ID=pe.ID)						");
		queryNative.append("JOIN TIPO_MOVIMENTO tm ON (movEstoque.TIPO_MOVIMENTO_ID=tm.ID)						");
		queryNative.append("WHERE movEstoque.COTA_ID = cota.ID 									");
		queryNative.append("AND tm.GRUPO_MOVIMENTO_ESTOQUE = :grupoMovimentoEstoque ");
		queryNative.append("AND movEstoque.DATA = ca.DATA ");
		queryNative.append(") as valorNE 									");
		
		queryNative.append(getFromWhereBuscaCotaAusente(filtro));
		
		ColunaOrdenacao colunaOrdenacao = filtro.getColunaOrdenacao();
		if (colunaOrdenacao != null) {
			if (ColunaOrdenacao.box == colunaOrdenacao) {
				queryNative.append("order by box.NOME ");
			} else if (ColunaOrdenacao.data == colunaOrdenacao) {
				queryNative.append("order by ca.DATA ");
			} else if (ColunaOrdenacao.cota == colunaOrdenacao) {
				queryNative.append("order by cota.NUMERO_COTA ");
			} else if (ColunaOrdenacao.nome == colunaOrdenacao) {
				queryNative.append("order by pessoa.NOME ");
			} else if (ColunaOrdenacao.valorNe == colunaOrdenacao) {
				queryNative.append("order by valorNE ");
			}	
			
			
			if (filtro.getPaginacao() != null && filtro.getPaginacao().getOrdenacao() != null) {
				if ("DESC".equalsIgnoreCase(filtro.getPaginacao().getOrdenacao().name())) {
					queryNative.append(" DESC ");
				} else {
					queryNative.append(" ASC ");
				}
			}
		}
		
		if(filtro.getPaginacao() != null && filtro.getPaginacao().getPosicaoInicial() != null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
			queryNative.append(" limit :inicio,:qtdeResult ");
		}
				
		Query query  = getSession().createSQLQuery(queryNative.toString())
				.addScalar("idCotaAusente")
				.addScalar("data")
				.addScalar("box")
				.addScalar("cota")
				.addScalar("nome")
				.addScalar("valorNe").setResultTransformer(Transformers.aliasToBean(CotaAusenteDTO.class));
		
		setParametersBuscaCotaAusente(filtro, query);
		
		query.setParameter("grupoMovimentoEstoque", GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_AUSENTE.name());
		
		if(filtro.getPaginacao() != null && filtro.getPaginacao().getPosicaoInicial() != null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
			query.setParameter("inicio", filtro.getPaginacao().getPosicaoInicial());
			query.setParameter("qtdeResult", filtro.getPaginacao().getQtdResultadosPorPagina());
		}
				
		return query.list();
		
	}
	private void setParametersBuscaCotaAusente(FiltroCotaAusenteDTO filtro,
			Query query) {
		query.setParameter("ativo", true);
		
		if(filtro.getData() != null){			
			query.setParameter("data", filtro.getData());
		}
		
		if(filtro.getNumCota() != null){
			query.setParameter("numCota", filtro.getNumCota());
		}
		
		if(filtro.getBox() != null){
			query.setParameter("box", filtro.getBox());
		}
		
		if(filtro.getIdRoteiro() != null) {
			query.setParameter("idRoteiro", filtro.getIdRoteiro());
		}
	
		if(filtro.getIdRota() != null) {
			query.setParameter("idRota", filtro.getIdRota());
		}
	}
	private StringBuilder getFromWhereBuscaCotaAusente(FiltroCotaAusenteDTO filtro) {
		
		StringBuilder queryNative = new StringBuilder();
		
		queryNative.append("FROM COTA cota																		");

		queryNative.append("LEFT JOIN COTA_AUSENTE ca ON (ca.COTA_ID=cota.ID)									");
		queryNative.append("LEFT JOIN BOX box ON (cota.BOX_ID=box.ID)											");
		queryNative.append("LEFT JOIN ROTEIRIZACAO roteirizacao ON (box.ID = roteirizacao.BOX_ID)               ");
		queryNative.append("LEFT JOIN ROTEIRO roteiro ON (roteirizacao.ID = roteiro.ROTEIRIZACAO_ID)            ");
		queryNative.append("LEFT JOIN ROTA rota ON (roteiro.ID = rota.ROTEIRO_ID)					            ");
		queryNative.append("LEFT JOIN PESSOA pessoa ON (cota.PESSOA_ID=pessoa.ID)								");
		
		queryNative.append("WHERE ca.ativo =:ativo 											 					");
				
		if(filtro.getData() != null){	
			queryNative.append("AND ca.DATA = :data  											");
		}
		
		
		if(filtro.getNumCota() != null){			
			queryNative.append("and cota.NUMERO_COTA = :numCota 												");
		}
		
		if(filtro.getBox() != null){
			
			queryNative.append("and box.NOME = :box 															");
		}
		
		if(filtro.getIdRoteiro() != null) {
			queryNative.append(" and roteiro.ID = :idRoteiro ");
		}
	
		if(filtro.getIdRota() != null) {
			queryNative.append(" and rota.ID = :idRota ");
		}
		
		queryNative.append("group by 		");
		queryNative.append("ca.DATA, 			");
		queryNative.append("box.NOME, 			");
		queryNative.append("cota.NUMERO_COTA,			");
		queryNative.append("pessoa.nome			");
		
		return queryNative;
	}
	
	
	public CotaAusente obterCotaAusentePor(Long idCota, Date data) {
		
		Criteria criteria = this.getSession().createCriteria(CotaAusente.class);
		
		criteria.add(Restrictions.eq("cota.id", idCota));
		criteria.add(Restrictions.eq("data", data));
		
		CotaAusente cotaAusente = null;
		
		try {
		  	cotaAusente = (CotaAusente) criteria.uniqueResult();
		} catch(HibernateException e) {
			e.printStackTrace();
		}
		return  cotaAusente;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Long obterCountCotasAusentes(FiltroCotaAusenteDTO filtro) {

		StringBuilder queryNative = new StringBuilder();
		
		queryNative.append("SELECT COUNT(*)																		"); 	
		queryNative.append(getFromWhereBuscaCotaAusente(filtro));
		
		Query query  = getSession().createSQLQuery(queryNative.toString());
		
		setParametersBuscaCotaAusente(filtro, query);
		
		List<BigInteger> result = query.list();
		
		return (long) result.size();
	}
}
