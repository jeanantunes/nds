package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
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
		
		
		
		queryNative.append("SELECT 																				"); 		

		queryNative.append("ca.DATA as data, 																	");
		queryNative.append("box.NOME as box, 																	");
		queryNative.append("cota.NUMERO_COTA as cota,															");
		queryNative.append("pessoa.nome as nome,																	");
	    
		queryNative.append("( SELECT SUM(movEstoque.QTDE*pe.PRECO_CUSTO) FROM MOVIMENTO_ESTOQUE_COTA movEstoque ");
		queryNative.append("JOIN PRODUTO_EDICAO pe ON (movEstoque.PRODUTO_EDICAO_ID=pe.ID)						");
		queryNative.append("WHERE movEstoque.COTA_ID = cota.ID ) as valorNE 									");
		
		queryNative.append("FROM COTA cota																		");

		queryNative.append("LEFT JOIN COTA_AUSENTE ca ON (ca.COTA_ID=cota.ID)									");
		queryNative.append("LEFT JOIN BOX box ON (cota.BOX_ID=box.ID)											");
		queryNative.append("LEFT JOIN PESSOA pessoa ON (cota.PESSOA_ID=pessoa.ID)								");
		
		queryNative.append("WHERE 																				");
		
		queryNative.append("ca.DATA = :data 																	");
		
		if(idCota != null){
			
			queryNative.append("and cota.ID = :idCota 															");
		}
		
		queryNative.append("group by 		");
		queryNative.append("ca.DATA, 			");
		queryNative.append("box.NOME, 			");
		queryNative.append("cota.NUMERO_COTA,			");
		queryNative.append("pessoa.nome			");
		
				
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
				
		Query query  = getSession().createSQLQuery(queryNative.toString()).addScalar("data").addScalar("box")
				.addScalar("cota")
				.addScalar("nome")
				.addScalar("valorNe").setResultTransformer(Transformers.aliasToBean(CotaAusenteDTO.class));
			
		query.setParameter("data", data);
		
		if(idCota != null){
			query.setParameter("idCota", idCota);
		}
		
		
				
		return query.list();
		
	}
}
