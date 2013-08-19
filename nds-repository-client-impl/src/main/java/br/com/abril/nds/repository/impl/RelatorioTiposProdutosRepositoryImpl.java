package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.RelatorioTiposProdutosDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioTiposProdutos;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.RelatorioTiposProdutosRepository;

@Repository
public class RelatorioTiposProdutosRepositoryImpl extends AbstractRepository implements RelatorioTiposProdutosRepository {

	@SuppressWarnings("unchecked")
	@Override
	public List<RelatorioTiposProdutosDTO> gerarRelatorio(FiltroRelatorioTiposProdutos filtro) {
		
		SQLQuery query = getSession().createSQLQuery(this.obterHql(filtro,false));
		
		query.addScalar("codigo");
		query.addScalar("produto");
		query.addScalar("edicao", StandardBasicTypes.LONG);
		query.addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("faturamento", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("tipoProduto");
		query.addScalar("recolhimento");
		query.addScalar("lancamento");		
		
		this.aplicarFiltroQuery(query, filtro);
		
		this.aplicarParametros(query);
		
		this.aplicarParametrosList(query);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(RelatorioTiposProdutosDTO.class));
		
		Integer paginaAtual = filtro.getPaginacaoVO().getPaginaAtual();
		Integer qtdResultadosPorPagina = filtro.getPaginacaoVO().getQtdResultadosPorPagina();
		
		if(paginaAtual!= null && qtdResultadosPorPagina!= null ){
			query.setFirstResult((paginaAtual - 1) * qtdResultadosPorPagina);
		}
		
		if(qtdResultadosPorPagina!= null){
			query.setMaxResults(qtdResultadosPorPagina);
		}
		
		return query.list();
	}

	private void aplicarParametros(Query query) {
		
		query.setParameter("operacaoEstoqueEntrada", OperacaoEstoque.ENTRADA.name());
		query.setParameter("operacaoEstoqueSaida", OperacaoEstoque.SAIDA.name());
	}
	
	private void aplicarParametrosList(Query query) {
		
		List<String> lista = new ArrayList<>();
		
		lista.add(GrupoMovimentoEstoque.ENVIO_JORNALEIRO.name());
		lista.add(GrupoMovimentoEstoque.ENVIO_JORNALEIRO_JURAMENTADO.name());
		lista.add(GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE.name());
		lista.add(GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE_JURAMENTADO.name());
		lista.add(GrupoMovimentoEstoque.ESTORNO_REPARTE_FURO_PUBLICACAO.name());
		lista.add(GrupoMovimentoEstoque.SUPLEMENTAR_COTA_AUSENTE.name());
		lista.add(GrupoMovimentoEstoque.SUPLEMENTAR_ENVIO_ENCALHE_ANTERIOR_PROGRAMACAO.name());
		lista.add(GrupoMovimentoEstoque.REPARTE_COTA_AUSENTE.name());
		lista.add(GrupoMovimentoEstoque.VENDA_ENCALHE.name());
		lista.add(GrupoMovimentoEstoque.VENDA_ENCALHE_SUPLEMENTAR.name());
		lista.add(GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE.name());
		lista.add(GrupoMovimentoEstoque.ESTORNO_VENDA_ENCALHE_SUPLEMENTAR.name());
		
		query.setParameterList("gruposMovimentoEstoque", lista);
	}
	
	public Long obterQunatidade(FiltroRelatorioTiposProdutos filtro){
		
 		SQLQuery query = getSession().createSQLQuery(obterHql(filtro,true));
		
		query.addScalar("total", StandardBasicTypes.LONG);
		
		this.aplicarFiltroQuery(query, filtro);
		
		return (Long) query.uniqueResult();
	}
	
	private String obterHql(FiltroRelatorioTiposProdutos filtro, boolean isCount){
		
		boolean hasTipoProduto = filtro.getTipoProduto() != null && filtro.getTipoProduto().longValue() != -1L;
		boolean hasLancamentoDe = filtro.getDataLancamentoDe() != null;
		boolean hasLancamentoAte = filtro.getDataLancamentoAte() != null;
		boolean hasRecolhimentoDe = filtro.getDataRecolhimentoDe() != null;
		boolean hasRecolhimentoAte = filtro.getDataRecolhimentoAte() != null;
		
		boolean hasFilter = hasTipoProduto || hasLancamentoDe || hasLancamentoAte || hasRecolhimentoDe || hasRecolhimentoAte;
		
		StringBuilder hql = new StringBuilder();
		
		if(isCount){
			
			hql.append(" SELECT COUNT(*) AS total ");
			
		}else{
			
			hql.append(" SELECT ");
			hql.append(" produto1_.CODIGO AS codigo, ");
			hql.append(" produto1_.NOME AS produto, ");
			hql.append(" produtoedi0_.NUMERO_EDICAO AS edicao, ");
			hql.append(" produtoedi0_.PRECO_VENDA AS precoCapa, ");
	        
			hql.append(" (( ");
			hql.append("   		SELECT ");
			hql.append("   				SUM( ");
			hql.append("   					COALESCE (CASE WHEN (tipomovime7_.OPERACAO_ESTOQUE=:operacaoEstoqueSaida AND fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) THEN movimentoe6_.QTDE when (tipomovime7_.OPERACAO_ESTOQUE=:operacaoEstoqueEntrada and fechamentoEncalhe.DATA_ENCALHE IS NOT NULL) then - movimentoe6_.QTDE ELSE 0 END, 0) ");
			hql.append("             ) ");
			hql.append("         FROM ");
			hql.append("             MOVIMENTO_ESTOQUE movimentoe6_, ");
			hql.append("             TIPO_MOVIMENTO tipomovime7_ ");
			hql.append("         WHERE ");
			hql.append("             movimentoe6_.TIPO_MOVIMENTO_ID=tipomovime7_.ID ");
			hql.append("             AND movimentoe6_.PRODUTO_EDICAO_ID = produtoedi0_.ID ");
			hql.append(" 				AND tipomovime7_.GRUPO_MOVIMENTO_ESTOQUE IN (:gruposMovimentoEstoque) ");
			hql.append("   ) * produtoedi0_.PRECO_VENDA) AS faturamento, ");
	            
			hql.append(" tipoprodut2_.DESCRICAO AS tipoProduto, ");
			hql.append(" lancamento3_.DATA_REC_DISTRIB AS recolhimento, ");
			hql.append(" lancamento3_.DATA_LCTO_DISTRIBUIDOR AS lancamento ");
		}
		
		hql.append(" FROM ");
		hql.append("     PRODUTO_EDICAO produtoedi0_ ");
		hql.append(" INNER JOIN ");
		hql.append("     PRODUTO produto1_ ");
		hql.append("         ON produtoedi0_.PRODUTO_ID=produto1_.ID ");
		hql.append(" INNER JOIN ");
		hql.append("     TIPO_PRODUTO tipoprodut2_ ");
		hql.append("         ON produto1_.TIPO_PRODUTO_ID=tipoprodut2_.ID ");
		hql.append(" INNER JOIN ");
		hql.append("     LANCAMENTO lancamento3_ ");
		hql.append("         ON produtoedi0_.ID=lancamento3_.PRODUTO_EDICAO_ID ");
		hql.append(" LEFT JOIN ");
		hql.append(" 	 FECHAMENTO_ENCALHE fechamentoEncalhe ");
		hql.append(" 		 ON (fechamentoEncalhe.DATA_ENCALHE = lancamento3_.DATA_REC_DISTRIB ");
		hql.append(" 			 AND fechamentoEncalhe.PRODUTO_EDICAO_ID = produtoedi0_.ID) ");
		hql.append(" WHERE ");
		hql.append("     produtoedi0_.ATIVO = :verdadeiro ");

		if(hasFilter) {

			if(hasTipoProduto) {
				hql.append(" and tipoprodut2_.ID = :idTipoProduto");
			}
			if(hasLancamentoDe) {
				hql.append(" and lancamento3_.DATA_LCTO_DISTRIBUIDOR >= :dataLancamentoDe");
			}
			if(hasLancamentoAte) {
				hql.append(" and lancamento3_.DATA_LCTO_DISTRIBUIDOR <= :dataLancamentoAte");
			}
			if(hasRecolhimentoDe) {
				hql.append(" and lancamento3_.DATA_REC_DISTRIB >= :dataRecolhimentoDe");
			}
			if(hasRecolhimentoAte) {
				hql.append(" and lancamento3_.DATA_REC_DISTRIB <= :dataRecolhimentoAte");
			}
			
			if (hasRecolhimentoDe && hasRecolhimentoAte) {
				
				hql.append(" and lancamento3_.STATUS in (:statusLancamentoAposRecolhimento)");
			}
		}
		
		if(filtro.getPaginacaoVO()!= null && !isCount){
			
			hql.append(aplicarOrdenacao(filtro));
		}
		
		return hql.toString();

	}
	
	private String aplicarOrdenacao(FiltroRelatorioTiposProdutos filtro){
		
		StringBuilder hql = new StringBuilder();
		
		if (filtro.getOrdenacaoColuna() != null ){
			
			switch (filtro.getOrdenacaoColuna()) {
				
				case CODIGO:
					hql.append(" order by codigo " + filtro.getPaginacaoVO().getSortOrder());
					break;
				case DATA_LANCAMENTO:
					hql.append(" order by lancamento " + filtro.getPaginacaoVO().getSortOrder());
					break;
				case DATA_RECOLHIMENTO:
					hql.append(" order by recolhimento " + filtro.getPaginacaoVO().getSortOrder());
					break;
				case FATURAMENTO:
					hql.append(" order by faturamento " + filtro.getPaginacaoVO().getSortOrder());
					break;
				case NOME_PRODUTO:
					hql.append(" order by  produto " + filtro.getPaginacaoVO().getSortOrder());
					break;
				case NUMERO_EDICAO:
					hql.append(" order by edicao " + filtro.getPaginacaoVO().getSortOrder());
					break;
				case PRECO_CAPA:
					hql.append(" order by precoCapa " + filtro.getPaginacaoVO().getSortOrder());
					break;
				case TIPO_PRODUTO:
					hql.append(" order by tipoProduto " + filtro.getPaginacaoVO().getSortOrder());
					break;
				default:
					hql.append(" order by edicao " + filtro.getPaginacaoVO().getSortOrder());
			}
		}
		
		return hql.toString();
	}
	
	private void aplicarFiltroQuery(Query query, FiltroRelatorioTiposProdutos filtro){

		query.setParameter("verdadeiro", true);

		boolean hasTipoProduto = filtro.getTipoProduto() != null && filtro.getTipoProduto().longValue() != -1L;
		boolean hasLancamentoDe = filtro.getDataLancamentoDe() != null;
		boolean hasLancamentoAte = filtro.getDataLancamentoAte() != null;
		boolean hasRecolhimentoDe = filtro.getDataRecolhimentoDe() != null;
		boolean hasRecolhimentoAte = filtro.getDataRecolhimentoAte() != null;
		
		boolean hasFilter = hasTipoProduto || hasLancamentoDe || hasLancamentoAte || hasRecolhimentoDe || hasRecolhimentoAte;

		if(hasFilter) {
			
			if(hasTipoProduto) {
				query.setParameter("idTipoProduto", filtro.getTipoProduto());
			}
			if(hasLancamentoDe) {
				query.setParameter("dataLancamentoDe", filtro.getDataLancamentoDe());
			}
			if(hasLancamentoAte) {
				query.setParameter("dataLancamentoAte", filtro.getDataLancamentoAte());
			}
			if(hasRecolhimentoDe) {
				query.setParameter("dataRecolhimentoDe", filtro.getDataRecolhimentoDe());
			}
			if(hasRecolhimentoAte) {
				query.setParameter("dataRecolhimentoAte", filtro.getDataRecolhimentoAte());
			}
			
			if (hasRecolhimentoDe && hasRecolhimentoAte) {
				
				List<String> listaStatusAposRecolhimento = new ArrayList<>();
				
				listaStatusAposRecolhimento.add(StatusLancamento.BALANCEADO_RECOLHIMENTO.name());
				listaStatusAposRecolhimento.add(StatusLancamento.RECOLHIDO.name());
				
				query.setParameterList(
					"statusLancamentoAposRecolhimento", listaStatusAposRecolhimento);
			}
		}
	}
}
