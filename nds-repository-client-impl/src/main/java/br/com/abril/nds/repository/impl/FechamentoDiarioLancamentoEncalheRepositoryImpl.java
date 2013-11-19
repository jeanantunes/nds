package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.EncalheFecharDiaDTO;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioLancamentoEncalhe;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FechamentoDiarioLancamentoEncalheRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class FechamentoDiarioLancamentoEncalheRepositoryImpl extends 
			 AbstractRepositoryModel<FechamentoDiarioLancamentoEncalhe, Long> implements FechamentoDiarioLancamentoEncalheRepository {

	public FechamentoDiarioLancamentoEncalheRepositoryImpl() {
		super(FechamentoDiarioLancamentoEncalhe.class);
	}
	
	public List<EncalheFecharDiaDTO> obterDadosGridEncalhe(Date dataFechamento,PaginacaoVO paginacao){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select p.codigo as codigo, ")
			.append(" p.nome as nomeProduto, ")
			.append(" pe.numeroEdicao as numeroEdicao, ")
			.append(" pe.precoVenda as precoVenda, ")
			.append(" coalesce(f.quantidadeDiferenca,0) as qtdeDiferenca, ")
			.append(" coalesce(f.quantidadeVendaEncalhe,0) as qtdeVendaEncalhe, ")
			.append(" coalesce(f.quantidade,0) as qtdeLogico, ")
			.append(" coalesce(f.quantidadeLogicoJuramentado,0) as qtdeLogicoJuramentado, ")
			.append(" coalesce(f.quantidadeFisico,0) as qtdeFisico ")
			.append(" from FechamentoDiarioLancamentoEncalhe f join f.produtoEdicao pe join pe.produto p ")
			.append(" where f.fechamentoDiarioConsolidadoEncalhe.fechamentoDiario.dataFechamento =:dataFechamento ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataFechamento", dataFechamento);
		
		if (paginacao != null) {
			query.setFirstResult(paginacao.getPosicaoInicial());
			query.setMaxResults(paginacao.getQtdResultadosPorPagina());
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(EncalheFecharDiaDTO.class));
		
		return query.list();
	}
	
	public Long countDadosGridEncalhe(Date dataFechamento){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(f.id) ")
			.append(" from FechamentoDiarioLancamentoEncalhe f ")
			.append(" where f.fechamentoDiarioConsolidadoEncalhe.fechamentoDiario.dataFechamento =:dataFechamento ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataFechamento", dataFechamento);
		
		return (Long) query.uniqueResult();
	}
}
