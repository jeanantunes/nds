package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.VendaFechamentoDiaDTO;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioMovimentoVendaSuplementar;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FechamentoDiarioMovimentoVendaSuplementarRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class FechamentoDiarioMovimentoVendaSuplementarRepositoryImpl extends AbstractRepositoryModel<FechamentoDiarioMovimentoVendaSuplementar, Long> implements FechamentoDiarioMovimentoVendaSuplementarRepository{
	
	public FechamentoDiarioMovimentoVendaSuplementarRepositoryImpl() {
		super(FechamentoDiarioMovimentoVendaSuplementar.class);
	}
	
	public List<VendaFechamentoDiaDTO> obterDadosVendaSuplementar(Date dataFechamento, PaginacaoVO paginacao){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select v.quantidade as qtde, ")
			.append(" v.valor as valor, ")
			.append(" v.dataRecebimento as dataRecolhimento, ")
			.append(" p.codigo as codigo, ")
			.append(" p.nome as nomeProduto, ")
			.append(" pe.numeroEdicao as numeroEdicao, ")
			.append(" pe.precoVenda as precoVenda ")
			.append(" from FechamentoDiarioMovimentoVendaSuplementar v join v.produtoEdicao pe join pe.produto p ")
			.append(" where v.fechamentoDiarioConsolidadoSuplementar.fechamentoDiario.dataFechamento =:dataFechamento ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataFechamento", dataFechamento);
		
		if (paginacao != null) {
		    query.setFirstResult(paginacao.getPosicaoInicial());
	        query.setMaxResults(paginacao.getQtdResultadosPorPagina());
	    }
		
		query.setResultTransformer(new AliasToBeanResultTransformer(VendaFechamentoDiaDTO.class));
		
		return query.list();
	}
	
	public Long countDadosVendaSuplementar(Date dataFechamento){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(v.id) ")
			.append(" from FechamentoDiarioMovimentoVendaSuplementar v ")
			.append(" where v.fechamentoDiarioConsolidadoSuplementar.fechamentoDiario.dataFechamento =:dataFechamento ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataFechamento", dataFechamento);
		
		return (Long) query.uniqueResult();
	}
}
