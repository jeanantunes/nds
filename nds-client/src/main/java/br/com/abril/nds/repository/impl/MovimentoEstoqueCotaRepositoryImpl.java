package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class MovimentoEstoqueCotaRepositoryImpl extends AbstractRepository<MovimentoEstoqueCota, Long> implements MovimentoEstoqueCotaRepository {

	public MovimentoEstoqueCotaRepositoryImpl() {
		super(MovimentoEstoqueCota.class);
	}
	
	
	private String getConsultaListaContagemDevolucao(FiltroDigitacaoContagemDevolucaoDTO filtro, boolean indBuscaTotalMovimentoEParcial, boolean indBuscaQtd) {
		
		StringBuffer hqlEdicoes = new StringBuffer("");
		
		if(filtro.getIdFornecedor() != null) {
			hqlEdicoes = getSubQueryEdicoesDeFornecedor();
		}
		
		StringBuffer hqlMovimento = getSubQueryMovimento();
		
		StringBuffer hqlConfEncParcial = getSubQueryConfEncParc();

		
		StringBuffer hql = new StringBuffer("");
		
		if (indBuscaQtd) {

			hql.append(" select count( lancamento.produtoEdicao.id ) " );		
			
		} else {

			hql.append(" select new " + ContagemDevolucaoDTO.class.getCanonicalName() );		
			
			hql.append(" ( 	lancamento.produtoEdicao.produto.codigo,  					");		
			hql.append(" 	lancamento.produtoEdicao.produto.nome, 						");
			hql.append(" 	lancamento.produtoEdicao.numeroEdicao, 						");

			hql.append(" 	lancamento.produtoEdicao.precoVenda, 						");
			
			if(indBuscaTotalMovimentoEParcial) {
				hql.append( 	hqlMovimento.toString() + " as qtdMovimento, 			");
				hql.append( 	hqlConfEncParcial.toString() + "  as qtdParcial, 		");
			}
			
			hql.append("  	lancamento.dataRecolhimentoDistribuidor )	 				");
			
		}
		
		hql.append(" from ");		
		
		hql.append(" Lancamento lancamento ");		
		
		hql.append(" where ");	
		
		hql.append(" ( lancamento.dataRecolhimentoDistribuidor between :dataRecolhimentoDistribuidorInicial and :dataRecolhimentoDistribuidorFinal ) ");		
		
		if( filtro.getIdFornecedor() != null ) {
			
			hql.append(" and lancamento.produtoEdicao.id in " + hqlEdicoes.toString() );		
		}
		
		PaginacaoVO paginacao = filtro.getPaginacao();

		if (!indBuscaQtd && filtro.getOrdenacaoColuna() != null) {

			hql.append(" order by ");
			
			String orderByColumn = "";
			
				switch (filtro.getOrdenacaoColuna()) {
				
					case CODIGO_PRODUTO:
						orderByColumn = " lancamento.produtoEdicao.produto.codigo ";
						break;
					case NOME_PRODUTO:
						orderByColumn = " lancamento.produtoEdicao.produto.codigo.nome ";
						break;
					case NUMERO_EDICAO:
						orderByColumn = " lancamento.produtoEdicao.numeroEdicao ";
						break;
					case PRECO_CAPA:
						orderByColumn = " lancamento.produtoEdicao.precoVenda ";
						break;
					case QTD_DEVOLUCAO:
						orderByColumn = " qtdMovimento ";
						break;
					case QTD_NOTA:
						orderByColumn = " qtdParcial ";
						break;
						
					default:
						break;
				}
			
			hql.append(orderByColumn);
			
			if (paginacao.getOrdenacao() != null) {
				
				hql.append(paginacao.getOrdenacao().toString());
				
			}
			
		}
		
		return hql.toString();
	}
	
	private Query criarQueryComParametrosObterListaContagemDevolucao(String hql, FiltroDigitacaoContagemDevolucaoDTO filtro, TipoMovimentoEstoque tipoMovimentoEstoque, boolean indBuscaTotalMovimentoEParcial, boolean indBuscaQtd) {
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataRecolhimentoDistribuidorInicial", filtro.getPeriodo().getDataInicial());

		query.setParameter("dataRecolhimentoDistribuidorFinal", filtro.getPeriodo().getDataFinal());
		
		if(indBuscaTotalMovimentoEParcial && !indBuscaQtd) {
			query.setParameter("tipoMovimentoEstoque", tipoMovimentoEstoque);
		}
		
		if(filtro.getIdFornecedor() != null) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
	
		return query;
		
	}
	
	/**
	 * Obtém a lista de contagem devolução.
	 */
	public List<ContagemDevolucaoDTO> obterListaContagemDevolucao(
			FiltroDigitacaoContagemDevolucaoDTO filtro, 
			TipoMovimentoEstoque tipoMovimentoEstoque,
			boolean indBuscaTotalMovimentoEParcial) {
		
		String hql = getConsultaListaContagemDevolucao(filtro, indBuscaTotalMovimentoEParcial, false);
		
		Query query = criarQueryComParametrosObterListaContagemDevolucao(hql, filtro, tipoMovimentoEstoque, indBuscaTotalMovimentoEParcial, false);
		
		query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());

		query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
		
	}
	
	/**
	 * Obtém subquery que retorna o total das qtds de movimento estoque cota.
	 * 
	 * @return SubQuery
	 */
	private StringBuffer getSubQueryMovimento() {
		
		StringBuffer hqlMovimento = new StringBuffer("")
		.append(" ( select sum(movimento.qtde) 											")
		.append(" from MovimentoEstoqueCota movimento 									")
		.append(" where 																")
		.append(" lancamento.produtoEdicao.id = movimento.produtoEdicao.id and 			")
		.append(" lancamento.dataRecolhimentoDistribuidor = movimento.data and 	")
		.append(" movimento.tipoMovimento = :tipoMovimentoEstoque )						");
		
		return hqlMovimento;
		
	}
	
	/**
	 * Obtém subquery que retorna o total das qtds de devolução parciais.
	 * 
	 * @return SubQuery
	 */
	private StringBuffer getSubQueryConfEncParc() {
		
		StringBuffer hqlConfEncParcial = new StringBuffer("")
		.append(" ( select sum(parcial.qtde) 														")
		.append(" from ConferenciaEncalheParcial parcial 											")
		.append(" where 																			")
		.append(" lancamento.produtoEdicao.id = parcial.produtoEdicao.id and 						")
		.append(" lancamento.dataRecolhimentoDistribuidor = parcial.dataRecolhimentoDistribuidor )  ");
		
		return hqlConfEncParcial;
		
	}
	
	/**
	 * Obtém subquery que retorna uma lista de idProdutoEdicao pertencentes a um fornecedor.
	 * 
	 * @return SubQuery
	 */
	private StringBuffer getSubQueryEdicoesDeFornecedor() {
	
		StringBuffer  hqlEdicoes = new StringBuffer()
		.append(" ( select pe.id 																	")
		.append(" from ProdutoEdicao pe join pe.produto.fornecedores as fornecedores   				")
		.append(" where 																			")
		.append(" ( select f from Fornecedor f where f.id = :idFornecedor ) in (fornecedores) ) 	");
		
		return hqlEdicoes;
		
	}
	
	/**
	 * Obtém a o valor total geral que é igual a somatória da seguinte expressão 
	 * (qtdMovimentoEncalhe * precoVendoProdutoEdicao) de todos
	 * os registros resultantes dos parâmetros de pesquisa utilizadoss.
	 * 
	 * @return valorTotalGeral
	 */
	public BigDecimal obterValorTotalGeralContagemDevolucao(
			
			FiltroDigitacaoContagemDevolucaoDTO filtro, 
			TipoMovimentoEstoque tipoMovimentoEstoque) {
		
		StringBuffer hql = new StringBuffer("");
		
		hql.append(" select sum( movimento.qtde * movimento.produtoEdicao.precoVenda ) 	");		
		
		hql.append(" from MovimentoEstoqueCota movimento ");
		
		hql.append(" where 																						");

		if( filtro.getIdFornecedor() != null ) {
			hql.append(" movimento.produtoEdicao.id in " + getSubQueryEdicoesDeFornecedor() + " and "			);		
		}

		hql.append(" ( movimento.data  																	");
		hql.append(" between :dataRecolhimentoDistribuidorInicial and :dataRecolhimentoDistribuidorFinal ) and	");
		hql.append(" movimento.tipoMovimento = :tipoMovimentoEstoque )											");
		
		hql.append(" group by movimento.data, 															");
		
		hql.append(" movimento.produtoEdicao.precoVenda, 														");
		
		hql.append(" movimento.produtoEdicao.id 																");
		
		Query query = getSession().createQuery(hql.toString());

		query.setParameter("dataRecolhimentoDistribuidorInicial", filtro.getPeriodo().getDataInicial());
		
		query.setParameter("dataRecolhimentoDistribuidorFinal", filtro.getPeriodo().getDataFinal());

		query.setParameter("tipoMovimentoEstoque", tipoMovimentoEstoque);
		
		if(filtro.getIdFornecedor() != null) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		return (BigDecimal) query.uniqueResult();
		
	}
	
	/**
	 * Obtém a quantidade de registro referente a contagem devolução de acordo com 
	 * parâmetros de pesquisa.
	 * 
	 * @return qtdRegistros.
	 */
	public Integer obterQuantidadeContagemDevolucao( 
			FiltroDigitacaoContagemDevolucaoDTO filtro) {
		
		String hql = getConsultaListaContagemDevolucao(filtro, false, true);
		
		Query query = criarQueryComParametrosObterListaContagemDevolucao(hql, filtro, null, false, true);
		
		return ((Long) query.uniqueResult()).intValue();
		
	}

	
}
