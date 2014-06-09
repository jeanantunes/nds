package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.SuplementarFecharDiaDTO;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioLancamentoSuplementar;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FechamentoDiarioLancamentoSuplementarRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class FechamentoDiarioLancamentoSuplementarRepositoryImpl extends AbstractRepositoryModel<FechamentoDiarioLancamentoSuplementar, Long> implements FechamentoDiarioLancamentoSuplementarRepository {
	
	public FechamentoDiarioLancamentoSuplementarRepositoryImpl() {
		
		super(FechamentoDiarioLancamentoSuplementar.class);
	}
	
	public List<SuplementarFecharDiaDTO> obterDadosGridSuplementar(Date dataFechamento, PaginacaoVO paginacao){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select p.codigo as codigo, ")
			.append(" p.nome as nomeProduto, ")
			.append(" pe.numeroEdicao as numeroEdicao, ")
			.append(" pe.precoVenda as precoVenda, ")
			.append(" COALESCE(f.quantidadeContabilizada,0) as quantidadeContabil, ")
			.append(" COALESCE(f.quantidadeLogico,0) as quantidadeLogico, ")
			.append(" COALESCE(f.quantidadeTransferenciaEntrada,0) as quantidadeTransferenciaEntrada, ")
			.append(" COALESCE(f.quantidadeTransferenciaSaida,0) as quantidadeTransferenciaSaida, ")
			.append(" COALESCE(f.quantidadeVenda,0) as quantidadeVenda, ")
			.append(" COALESCE(f.saldo,0) as saldo ")
			.append(" from FechamentoDiarioLancamentoSuplementar f join f.produtoEdicao pe join pe.produto p  ")
			.append(" where f.fechamentoDiarioConsolidadoSuplementar.fechamentoDiario.dataFechamento=:dataFechamento ")
			.append(" and saldo <> 0 ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataFechamento", dataFechamento);
		
		if (paginacao != null) {
            query.setFirstResult(paginacao.getPosicaoInicial());
            query.setMaxResults(paginacao.getQtdResultadosPorPagina());
        }
		
        query.setResultTransformer(new AliasToBeanResultTransformer(SuplementarFecharDiaDTO.class));
        
        return query.list();
	}
	
	public Long countDadosGridSuplementar(Date dataFechamento){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(f.id) ")
			.append(" from FechamentoDiarioLancamentoSuplementar f ")
			.append(" where f.fechamentoDiarioConsolidadoSuplementar.fechamentoDiario.dataFechamento=:dataFechamento and coalesce(f.saldo, 0) <> 0 ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataFechamento", dataFechamento);
		
        return (Long) query.uniqueResult();
	}
}
