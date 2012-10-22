package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.repository.ResumoSuplementarFecharDiaRepository;

@Repository
public class ResumoSuplementarFecharDiaRepositoryImpl extends AbstractRepository implements
		ResumoSuplementarFecharDiaRepository {

	@Override
	public BigDecimal obterValorEstoqueLogico(Date dataOperacao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT SUM (");
			hql.append(" ( ");
				hql.append("  (SELECT me.qtde from TipoMovimento as tm ");       
				hql.append(" WHERE tm.grupoMovimentoEstoque = :entradaSuplementarEnvioReparte");
				hql.append(" AND me.dataCriacao = :dataOperacao)");
			hql.append(" + ");
				hql.append("  (SELECT me.qtde from TipoMovimento as tm ");       
				hql.append(" WHERE tm.grupoMovimentoEstoque = :suplementarCotaAusente");
				hql.append(" AND me.dataCriacao = :dataOperacao)");
			hql.append(" ) ");
			hql.append(" - ");
			hql.append(" (");
				hql.append("  (SELECT me.qtde from TipoMovimento as tm ");       
				hql.append(" WHERE tm.grupoMovimentoEstoque = :reparteCotaAusente");
				hql.append(" AND me.dataCriacao = :dataOperacao)");
			hql.append(" )");
		hql.append(" ) * pe.precoVenda");
		hql.append(" FROM MovimentoEstoque as me ");
		hql.append(" JOIN me.estoqueProduto as ep ");
		hql.append(" JOIN ep.produtoEdicao as pe ");
		hql.append(" WHERE me.dataCriacao = :dataOperacao");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("entradaSuplementarEnvioReparte", GrupoMovimentoEstoque.ENTRADA_SUPLEMENTAR_ENVIO_REPARTE);
		query.setParameter("suplementarCotaAusente", GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE);
		query.setParameter("reparteCotaAusente", GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE);
		
		
		BigDecimal total =  (BigDecimal) query.uniqueResult();
		
		return total != null ? total : BigDecimal.ZERO ;
	}

}
