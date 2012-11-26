package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.RelatorioTiposProdutosDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioTiposProdutos;
import br.com.abril.nds.repository.RelatorioTiposProdutosRepository;

@Repository
public class RelatorioTiposProdutosRepositoryImpl extends AbstractRepository implements RelatorioTiposProdutosRepository {

	@SuppressWarnings("unchecked")
	@Override
	public List<RelatorioTiposProdutosDTO> gerarRelatorio(FiltroRelatorioTiposProdutos filtro) {
		
		Query query = getSession().createQuery(obterHql(filtro,false));
		
		this.aplicarFiltroQuery(query, filtro);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(RelatorioTiposProdutosDTO.class));
		
		if(filtro.getPaginacaoVO().getQtdResultadosTotal()!= null){

			Long qntTotal = obterQunatidade(filtro);
		
			filtro.getPaginacaoVO().setQtdResultadosTotal( (qntTotal==null) ? 0 : qntTotal.intValue() );
		}
		
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
	
	private Long obterQunatidade(FiltroRelatorioTiposProdutos filtro){
		
		Query query = getSession().createQuery(obterHql(filtro,true));
		
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
			
			hql.append(" select count (p.codigo) ");
			
		}else{
			
			hql.append("SELECT p.codigo AS codigo, " +
					"p.nome AS produto, " +
					"pe.numeroEdicao AS edicao, " +
					"pe.precoVenda AS precoCapa, " +
					"( " +
					" (" +
					"	(select sum(mec.qtde) from MovimentoEstoqueCota mec where mec.tipoMovimento.operacaoEstoque = 'SAIDA' and mec.lancamento.id = l.id)" +
					" - " +
					"   (select sum(mec.qtde) from MovimentoEstoqueCota mec where mec.tipoMovimento.operacaoEstoque = 'ENTRADA' and mec.lancamento.id = l.id)" +
					" )" +
					" * pe.precoVenda" +
					") AS faturamento, " +
					"t.descricao AS tipoProduto, " +
					"l.dataRecolhimentoDistribuidor AS recolhimento, " +
					"l.dataLancamentoDistribuidor AS lancamento " );
		}
		
		hql.append(
				"FROM ProdutoEdicao pe " +
				"JOIN pe.produto p " +
				"JOIN p.tipoProduto t " +
				"JOIN pe.lancamentos l");
		
		if(hasFilter) {
			hql.append(" where true = true");
			
			if(hasTipoProduto) {
				hql.append(" and t.id = :idTipoProduto");
			}
			if(hasLancamentoDe) {
				hql.append(" and l.dataLancamentoDistribuidor >= :dataLancamentoDe");
			}
			if(hasLancamentoAte) {
				hql.append(" and l.dataLancamentoDistribuidor <= :dataLancamentoAte");
			}
			if(hasRecolhimentoDe) {
				hql.append(" and l.dataRecolhimentoDistribuidor >= :dataRecolhimentoDe");
			}
			if(hasRecolhimentoAte) {
				hql.append(" and l.dataRecolhimentoDistribuidor <= :dataRecolhimentoAte");
			}
		}
		
		if(filtro.getPaginacaoVO()!= null){
			
			hql.append(aplicarOrdenacao(filtro) + " " + filtro.getPaginacaoVO().getSortOrder());
		}
		
		return hql.toString();

	}
	
	private String aplicarOrdenacao(FiltroRelatorioTiposProdutos filtro){
		
		StringBuilder hql = new StringBuilder();
		
		if (filtro.getOrdenacaoColuna() != null ){
			
			switch (filtro.getOrdenacaoColuna()) {
				
				case CODIGO:
					hql.append(" order by p.codigo ");
					break;
				case DATA_LANCAMENTO:
					hql.append(" order by l.dataLancamentoDistribuidor ");
					break;
				case DATA_RECOLHIMENTO:
					hql.append(" order by l.dataRecolhimentoDistribuidor ");
					break;
				case FATURAMENTO:
					hql.append(" order by (select sum(mec.qtde) from MovimentoEstoqueCota mec where mec.tipoMovimento.operacaoEstoque = 'SAIDA' and mec.lancamento.id = l.id)" )
					.append(" - ")
					.append("   (select sum(mec.qtde) from MovimentoEstoqueCota mec where mec.tipoMovimento.operacaoEstoque = 'ENTRADA' and mec.lancamento.id = l.id)" )
					.append(" ) * pe.precoVenda ");
					
					break;
				case NOME_PRODUTO:
					hql.append(" order by  p.nome ");
					break;
				case NUMERO_EDICAO:
					hql.append(" order by pe.numeroEdicao ");
					break;
				case PRECO_CAPA:
					hql.append(" order by pe.precoVenda ");
					break;
				case TIPO_PRODUTO:
					hql.append(" order by t.descricao ");
					break;
				default:
					hql.append(" order by pe.numeroEdicao ");
			}
			
			if (filtro.getPaginacaoVO().getOrdenacao() != null) {
				hql.append( filtro.getPaginacaoVO().getOrdenacao().toString());
			}
		}
		
		return hql.toString();
	}
	
	private void aplicarFiltroQuery(Query query, FiltroRelatorioTiposProdutos filtro){
		
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
		}
	}
}
