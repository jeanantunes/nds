package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaAusenteDTO;
import br.com.abril.nds.dto.CotaAusenteDTO.ColunaOrdenacao;
import br.com.abril.nds.model.movimentacao.CotaAusente;
import br.com.abril.nds.repository.CotaAusenteRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Repository
public class CotaAusenteRepositoryImpl extends AbstractRepository<CotaAusente, Long> implements CotaAusenteRepository { 
	
	/**
	 * Construtor.
	 */
	public CotaAusenteRepositoryImpl() {
		
		super(CotaAusente.class);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<CotaAusenteDTO> obterCotasAusentes(Date data, Long idCota, CotaAusenteDTO cotaAusenteDTO) {
						
		StringBuilder queryNative = new StringBuilder();
		
		queryNative.append("SELECT ");		
		queryNative.append("ca.DATA as data, ");	
		queryNative.append("box.NOME as box, ");	
		queryNative.append("cota.NUMERO_COTA as cota, ");	
		queryNative.append("pessoa.NOME as nome, ");
		queryNative.append("SUM(mec.QTDE*pe.PRECO_CUSTO) as valorNE ");

		queryNative.append("FROM ");

		queryNative.append("MOVIMENTO_ESTOQUE_COTA mec "); 
		queryNative.append("LEFT JOIN COTA cota ON (mec.COTA_ID = cota.ID) "); 
		queryNative.append("LEFT JOIN COTA_AUSENTE ca ON (ca.COTA_ID=cota.ID) ");   
		queryNative.append("LEFT JOIN PRODUTO_EDICAO pe ON (mec.PRODUTO_EDICAO_ID=pe.ID) ");
		queryNative.append("LEFT JOIN BOX box ON (cota.BOX_ID=box.ID) ");
		queryNative.append("LEFT JOIN PESSOA pessoa ON (cota.PESSOA_ID=pessoa.ID) ");
		queryNative.append("WHERE ");
		queryNative.append("ca.DATA = :data ");
		queryNative.append("cota.ID = :idCota ");
		
		PaginacaoVO paginacao = cotaAusenteDTO.getPaginacao();
		
		ColunaOrdenacao colunaOrdenacao = cotaAusenteDTO.getColunaOrdenacao();
		if (colunaOrdenacao != null) {
			if (ColunaOrdenacao.box == colunaOrdenacao) {
				queryNative.append("order by box.NOME ");
			} else if (ColunaOrdenacao.data == colunaOrdenacao) {
				queryNative.append("order by ca.DATA ");
			} else if (ColunaOrdenacao.cota == colunaOrdenacao) {
				queryNative.append("order by cota.NUMERO_COTA ");
			} else if (ColunaOrdenacao.nome == colunaOrdenacao) {
				queryNative.append("order by pessoa.NOME ");
			} else if (ColunaOrdenacao.valorNE == colunaOrdenacao) {
				queryNative.append("order by valorNE ");
			}	
			
			String ordenacao = "asc";
			if (paginacao != null) {
				if (paginacao.getOrdenacao().equals(Ordenacao.DESC)) {
					ordenacao = "desc";
				}
			}
			queryNative.append(ordenacao);
		}
		
		

		Query query  = getSession().createSQLQuery(queryNative.toString());
		
		ResultTransformer resultTransformer = new AliasToBeanResultTransformer(CotaAusenteDTO.class);
		
		query.setResultTransformer(resultTransformer);
		
		query.setParameter("data", data);
		
		query.setParameter("idCota", idCota);
				
		return query.list();
		
	}
}
