package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ReparteFecharDiaDTO;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioLancamentoReparte;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FechamentoDiarioLancamentoReparteRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class FechamentoDiarioLancamentoReparteRepositoryImpl extends 
			 AbstractRepositoryModel<FechamentoDiarioLancamentoReparte, Long> implements FechamentoDiarioLancamentoReparteRepository {

	public FechamentoDiarioLancamentoReparteRepositoryImpl() {
		super(FechamentoDiarioLancamentoReparte.class);
	}
	
	
	public List<ReparteFecharDiaDTO> obterLancametosReparte(Date data, PaginacaoVO paginacao){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select f.quantidadeADistribuir as qtdeDistribuir,")
			.append(" f.quantidadeDiferenca as qtdeDiferenca , ")
			.append(" f.quantidadeDistribuido as qtdeDistribuido,")
			.append(" f.quantidadeFaltaEM as qtdeFalta , ")
			.append(" f.quantidadeReparte as qtdeReparte, ")
			.append(" f.quantidadeSobraDistribuido as qtdeSobraDistribuicao,  ")
			.append(" f.quantidadeSobraEM as qtdeSobra,  ")
			.append(" f.quantidadeTranferencia as qtdeTransferencia, ")
			.append(" p.nome as nomeProduto, ")
			.append(" p.codigo as codigo, ")
			.append(" pe.numeroEdicao as numeroEdicao, ")
			.append(" pe.precoVenda as precoVenda ")
			.append(" from FechamentoDiarioLancamentoReparte f join f.produtoEdicao pe join pe.produto p ")
			.append(" where f.fechamentoDiarioConsolidadoReparte.fechamentoDiario.dataFechamento=:dataFechamento  ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataFechamento", data);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ReparteFecharDiaDTO.class));
		
		if (paginacao != null) {
	            query.setFirstResult(paginacao.getPosicaoInicial());
	            query.setMaxResults(paginacao.getQtdResultadosPorPagina());
	    }
		
		return query.list();
	}
	
	public Long countLancametosReparte(Date data){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(f.id)")
			.append(" from FechamentoDiarioLancamentoReparte f ")
			.append(" where f.fechamentoDiarioConsolidadoReparte.fechamentoDiario.dataFechamento=:dataFechamento  ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataFechamento", data);
		
		return (Long) query.uniqueResult();
	}

}
