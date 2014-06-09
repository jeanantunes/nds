package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.VendaFechamentoDiaDTO;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioMovimentoVendaEncalhe;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FechamentoDiarioMovimentoVendaEncalheRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class FechamentoDiarioMovimentoVendaEncalheRepositoryImpl extends 
			 AbstractRepositoryModel<FechamentoDiarioMovimentoVendaEncalhe, Long> implements FechamentoDiarioMovimentoVendaEncalheRepository{
	
	public FechamentoDiarioMovimentoVendaEncalheRepositoryImpl() {
		super(FechamentoDiarioMovimentoVendaEncalhe.class);
	}
	
	public List<VendaFechamentoDiaDTO> obterDadosVendaEncalhe(Date dataFechamento, PaginacaoVO paginacao){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select v.quantidade as qtde, ")
			.append(" v.valor as valor, ")
			.append(" v.dataRecebimento as dataRecolhimento, ")
			.append(" p.codigo as codigo, ")
			.append(" p.nome as nomeProduto, ")
			.append(" pe.numeroEdicao as numeroEdicao, ")
			.append(" pe.precoVenda as precoVenda ")
			.append(" from FechamentoDiarioMovimentoVendaEncalhe v join v.produtoEdicao pe join pe.produto p ")
			.append(" where v.fechamentoDiarioConsolidadoEncalhe.fechamentoDiario.dataFechamento =:dataFechamento ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataFechamento", dataFechamento);
		
		if (paginacao != null) {
		    query.setFirstResult(paginacao.getPosicaoInicial());
	        query.setMaxResults(paginacao.getQtdResultadosPorPagina());
	    }
		
		query.setResultTransformer(new AliasToBeanResultTransformer(VendaFechamentoDiaDTO.class));
		
		return query.list();
	}
	
	public Long countDadosVendaEncalhe(Date dataFechamento){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(v.id) ")
			.append(" from FechamentoDiarioMovimentoVendaEncalhe v ")
			.append(" where v.fechamentoDiarioConsolidadoEncalhe.fechamentoDiario.dataFechamento =:dataFechamento ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataFechamento", dataFechamento);
		
		return (Long) query.uniqueResult();
	}
}
