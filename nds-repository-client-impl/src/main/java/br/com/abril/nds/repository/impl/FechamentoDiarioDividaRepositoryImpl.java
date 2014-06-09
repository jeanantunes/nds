package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.fechamentodiario.DividaDTO;
import br.com.abril.nds.dto.fechamentodiario.TipoDivida;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioDivida;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FechamentoDiarioDividaRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class FechamentoDiarioDividaRepositoryImpl extends AbstractRepositoryModel<FechamentoDiarioDivida, Long> implements FechamentoDiarioDividaRepository {

	public FechamentoDiarioDividaRepositoryImpl() {
		super(FechamentoDiarioDivida.class);
	}
	
	public List<DividaDTO> obterDividas(Date dataFechamento, TipoDivida tipoDivida, PaginacaoVO paginacao){
		
		StringBuilder hql  = new StringBuilder();
		
		hql.append(" select d.banco as nomeBanco, ")
			.append(" d.dataVencimento as dataVencimento, ")
			.append(" d.tipoCobranca as formaPagamento, ")
			.append(" d.nomeCota as nomeCota, ")
			.append(" d.numeroCota as numeroCota, ")
			.append(" d.nossoNumero as nossoNumero, ")
			.append(" d.numeroConta as contaCorrente, ")
			.append(" d.valor as valor ")
			.append(" from FechamentoDiarioDivida d join d.fechamentoDiarioConsolidadoDivida fd ")
			.append(" where fd.fechamentoDiario.dataFechamento=:dataFechamento ")
			.append(" and fd.tipoDivida=:tipoDivida ");
		
		Query query  = getSession().createQuery(hql.toString());
		
		query.setParameter("dataFechamento", dataFechamento);
		query.setParameter("tipoDivida", tipoDivida);
		
		if (paginacao != null) {
			query.setFirstResult(paginacao.getPosicaoInicial());
			query.setMaxResults(paginacao.getQtdResultadosPorPagina());
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(DividaDTO.class));
		
		return query.list();
	}
	
	public Long countDividas(Date dataFechamento, TipoDivida tipoDivida){
		
		StringBuilder hql  = new StringBuilder();
		
		hql.append(" select count(d.id) ")
			.append(" from FechamentoDiarioDivida d join d.fechamentoDiarioConsolidadoDivida fd ")
			.append(" where fd.fechamentoDiario.dataFechamento=:dataFechamento ")
			.append(" and fd.tipoDivida=:tipoDivida ");
		
		Query query  = getSession().createQuery(hql.toString());
		
		query.setParameter("dataFechamento", dataFechamento);
		query.setParameter("tipoDivida", tipoDivida);
	
		return (Long) query.uniqueResult();
	}

}
