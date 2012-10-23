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
		
		hql.append("SELECT ");
				hql.append("( ");
				hql.append(" coalesce( (SELECT me2.qtde from MovimentoEstoque as me2 ");       
				hql.append(" JOIN me2.tipoMovimento as tm2 ");       
				hql.append(" WHERE tm2.grupoMovimentoEstoque = :entradaSuplementarEnvioReparte");
				hql.append(" AND me2.data = :dataOperacao), 0)");
			hql.append(" + ");
				hql.append(" coalesce( (SELECT me2.qtde from MovimentoEstoque as me2");       
				hql.append(" JOIN me2.tipoMovimento as tm2 ");       
				hql.append(" WHERE tm2.grupoMovimentoEstoque = :suplementarCotaAusente");
				hql.append(" AND me2.data = :dataOperacao),0) ");
			hql.append(" - ");							
				hql.append(" coalesce( (SELECT me2.qtde from MovimentoEstoque as me2");       
				hql.append(" JOIN me2.tipoMovimento as tm2 ");       
				hql.append(" WHERE tm2.grupoMovimentoEstoque = :reparteCotaAusente");
				hql.append(" AND me2.data = :dataOperacao),0)");
				hql.append(" ) ");
		hql.append("  * pe.precoVenda");
		hql.append(" FROM MovimentoEstoque as me ");
		hql.append(" JOIN me.produtoEdicao as pe ");		
		hql.append(" WHERE me.data = :dataOperacao");
		hql.append(" GROUP BY me.data");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("entradaSuplementarEnvioReparte", GrupoMovimentoEstoque.ENTRADA_SUPLEMENTAR_ENVIO_REPARTE);
		query.setParameter("suplementarCotaAusente", GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE);
		query.setParameter("reparteCotaAusente", GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE);
		
		BigDecimal total =  (BigDecimal) query.uniqueResult();
		
		return total != null ? total : BigDecimal.ZERO ;
	}

	@Override
	public BigDecimal obterValorTransferencia(Date dataOperacao) {
		
		StringBuilder hql = new StringBuilder();
		
			hql.append("SELECT ");
			hql.append("( ");
				hql.append(" coalesce( (SELECT me2.qtde from MovimentoEstoque as me2 ");       
				hql.append(" JOIN me2.tipoMovimento as tm2 ");       
				hql.append(" WHERE tm2.grupoMovimentoEstoque = :transferenciaEntradaSuplementar");
				hql.append(" AND me2.data = :dataOperacao), 0)");
			hql.append(" - ");
				hql.append(" coalesce( (SELECT me2.qtde from MovimentoEstoque as me2");       
				hql.append(" JOIN me2.tipoMovimento as tm2 ");       
				hql.append(" WHERE tm2.grupoMovimentoEstoque = :suplementarCotaAusente");
				hql.append(" AND me2.data = :dataOperacao),0) ");			
			hql.append(" ) ");
			hql.append("  * pe.precoVenda");
			hql.append(" FROM MovimentoEstoque as me ");
			hql.append(" JOIN me.produtoEdicao as pe ");		
			hql.append(" WHERE me.data = :dataOperacao");
			hql.append(" GROUP BY me.data");
		

		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("transferenciaEntradaSuplementar", GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR );
		query.setParameter("suplementarCotaAusente", GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_SUPLEMENTAR);
		
		BigDecimal total =  (BigDecimal) query.uniqueResult();
		
		return total != null ? total : BigDecimal.ZERO ;
	}

	@Override
	public BigDecimal obterValorVenda(Date dataOperacao) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT (me.qtde * pe.precoVenda) as venda  ");			
		hql.append(" FROM MovimentoEstoque as me ");
		hql.append(" JOIN me.tipoMovimento as tm "); 
		hql.append(" JOIN me.produtoEdicao as pe ");		
		hql.append(" WHERE me.data = :dataOperacao");
		hql.append(" AND tm.grupoMovimentoEstoque = :vendaEncalheSuplementar");		
		hql.append(" GROUP BY me.data");
	

	Query query = super.getSession().createQuery(hql.toString());
	
	query.setParameter("dataOperacao", dataOperacao);
	
	query.setParameter("vendaEncalheSuplementar", GrupoMovimentoEstoque.VENDA_ENCALHE_SUPLEMENTAR );
	
	BigDecimal total =  (BigDecimal) query.uniqueResult();
	
	return total != null ? total : BigDecimal.ZERO ;
	}

}
