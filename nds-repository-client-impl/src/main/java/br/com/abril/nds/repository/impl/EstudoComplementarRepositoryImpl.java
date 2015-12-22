package br.com.abril.nds.repository.impl;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.EstudoComplementarVO;
import br.com.abril.nds.model.planejamento.EstudoCotaGerado;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstudoComplementarRepository;

@Repository
public class EstudoComplementarRepositoryImpl extends AbstractRepositoryModel<EstudoCotaGerado, Long> implements EstudoComplementarRepository {

    public EstudoComplementarRepositoryImpl() {
	super(EstudoCotaGerado.class);
    }

    @Override
    @Transactional(readOnly = true)
    public LinkedList<EstudoCotaGerado> selecionarBancas(EstudoComplementarVO estudoComplementarVO) {

	LinkedHashSet<EstudoCotaGerado> lista = new LinkedHashSet<>();

	for (int i = 0; i < 4; i++) {
	    
		StringBuilder sql = new StringBuilder();
	    
		sql.append("select distinct ec.* ");
	    sql.append("  from estudo_cota_gerado ec ");
	    sql.append("  join cota c on c.id = ec.cota_id and c.situacao_cadastro = 'ATIVO' ");
	    
	    if (estudoComplementarVO.getTipoSelecao().equals("RANKING_FATURAMENTO")) {
	    	sql.append("  join ranking_faturamento rs on rs.cota_id = c.id ");
	    } else {
		
	    	sql.append("  join estudo_gerado e on e.id = ec.estudo_id ");
			sql.append("  join produto_edicao pe on pe.id = e.produto_edicao_id ");
			sql.append("  join produto p on p.id = pe.produto_id ");
			sql.append("  join ranking_segmento rs on rs.cota_id = c.id ");
			sql.append("   and rs.tipo_segmento_produto_id = p.tipo_segmento_produto_id ");
	    }
	    
	    sql.append(" where (ec.reparte = 0 or ec.reparte is null) ");
	    sql.append("   and ec.estudo_id = :estudoId ");
	    sql.append("   and ec.classificacao = ");
	    
	    if (i == 0) {
	    	sql.append(" 'SH' ");
	    } else {
	    	sql.append(" 'VZ' ");
	    }
	    
	    sql.append("   and ");
	    sql.append(i);
	    
	    if (i < 3) {
	    	sql.append(" = ");
	    } else {
	    	sql.append(" >= ");
	    }
	    
	    sql.append(" (select count(epe.produto_edicao_id) soma ");
	    sql.append("    from estudo_produto_edicao epe ");
	    sql.append("   where epe.cota_id = c.id ");
	    sql.append("     and epe.estudo_id = ec.estudo_id) ");
	    
	    if (estudoComplementarVO.getTipoSelecao().equals("RANKING_FATURAMENTO")) {
	    	sql.append(" order by rs.faturamento desc ");
	    } else {
	    	sql.append(" order by rs.qtde desc ");
	    }

	    Query query = super.getSession().createSQLQuery(sql.toString()).addEntity(EstudoCotaGerado.class);
	    query.setParameter("estudoId", estudoComplementarVO.getCodigoEstudo());
	    
	    List<EstudoCotaGerado> temp = query.list();
	    
	    if (temp != null) {
	    	lista.addAll(new LinkedHashSet<EstudoCotaGerado>(temp));
	    }
	}
	return new LinkedList<EstudoCotaGerado>(lista);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstudoCotaGerado> getCotasOrdenadas(EstudoComplementarVO estudoComplementarVO) {
	
	StringBuilder sql = new StringBuilder();
	sql.append("select distinct ec.* ");
	sql.append("  from estudo_cota_gerado ec ");
	sql.append("  join cota c on c.id = ec.cota_id and c.situacao_cadastro = 'ATIVO' ");
	if (estudoComplementarVO.getTipoSelecao().equals("RANKING_FATURAMENTO")) {
	    sql.append("  join ranking_faturamento rs on rs.cota_id = c.id ");
	} else {
	    sql.append("  join estudo_gerado e on e.id = ec.estudo_id ");
	    sql.append("  join produto_edicao pe on pe.id = e.produto_edicao_id ");
	    sql.append("  join produto p on p.id = pe.produto_id ");
	    sql.append("  join ranking_segmento rs on rs.cota_id = c.id ");
	    sql.append("   and rs.tipo_segmento_produto_id = p.tipo_segmento_produto_id ");
	}
	sql.append(" where (ec.reparte = 0 or ec.reparte is null) ");
	sql.append("   and ec.estudo_id = :estudoId ");
	sql.append("   and ((ec.classificacao = 'VZ' ");
	sql.append("   and (select count(epe.produto_edicao_id) soma ");
	sql.append("          from estudo_produto_edicao epe ");
	sql.append("         where epe.cota_id = c.id ");
	sql.append("           and epe.estudo_id = ec.estudo_id) >= 1) ");
	sql.append("    or (ec.classificacao = 'SH' ");
	sql.append("   and (select count(epe.produto_edicao_id) soma ");
	sql.append("          from estudo_produto_edicao epe ");
	sql.append("         where epe.cota_id = c.id ");
	sql.append("           and epe.estudo_id = ec.estudo_id) = 0)) ");
	if (estudoComplementarVO.getTipoSelecao().equals("RANKING_FATURAMENTO")) {
	    sql.append(" order by rs.faturamento desc ");
	} else {
	    sql.append(" order by rs.qtde desc ");
	}

	Query query = super.getSession().createSQLQuery(sql.toString()).addEntity(EstudoCotaGerado.class);
	query.setParameter("estudoId", estudoComplementarVO.getCodigoEstudo());
	return query.list();
    }
}
