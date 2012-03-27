package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class MovimentoEstoqueCotaRepositoryImpl extends AbstractRepository<MovimentoEstoqueCota, Long> implements MovimentoEstoqueCotaRepository {

	public MovimentoEstoqueCotaRepositoryImpl() {
		super(MovimentoEstoqueCota.class);
	}
	
	
	private String getConsultaListaContagemDevolucao(FiltroDigitacaoContagemDevolucaoDTO filtro, boolean indBuscaTotalParcial, boolean indBuscaQtd) {
		
		StringBuffer hqlEdicoes = new StringBuffer("");
		
		if(filtro.getIdFornecedor() != null) {
			hqlEdicoes = getSubQueryEdicoesDeFornecedor();
		}
		
		StringBuffer hqlConfEncParcial = getSubQueryConfEncParc();
		
		StringBuffer hql = new StringBuffer("");
		
		if (indBuscaQtd) {

			hql.append(" select count(mov.id) from MovimentoEstoqueCota mov where mov.id in ( select max(movimento.id) " );		
			
		} else {

			hql.append(" select new " + ContagemDevolucaoDTO.class.getCanonicalName() 	);		
			
			hql.append(" ( 	movimento.produtoEdicao.produto.codigo,  					");		
			hql.append(" 	movimento.produtoEdicao.produto.nome, 						");
			hql.append(" 	movimento.produtoEdicao.numeroEdicao, 						");
			hql.append(" 	movimento.produtoEdicao.precoVenda, 						");
			hql.append(" 	sum(movimento.qtde) as qtdMovimento, 						");
			
			if(indBuscaTotalParcial) {
				hql.append( hqlConfEncParcial.toString() + "  as qtdParcial, 			");
			}
			
			hql.append(" movimento.data ) ");
			
		}
		
		hql.append(" from ");		
		
		hql.append(" MovimentoEstoqueCota movimento ");		
		
		hql.append(" where ");	
		
		hql.append(" ( movimento.data between :dataInicial and :dataFinal ) and ");		

		hql.append(" movimento.tipoMovimento = :tipoMovimentoEstoque ");		
		
		if( filtro.getIdFornecedor() != null ) {
			hql.append(" and movimento.produtoEdicao.id in " + hqlEdicoes.toString() );		
		}

		if(indBuscaQtd){
    		hql.append(" group by 									");		
    		hql.append(" movimento.data, 							");		
    		hql.append(" movimento.produtoEdicao.id				  	");		
        }else{
    		hql.append(" group by 									");		
    		hql.append(" movimento.data, 							");		
    		hql.append(" movimento.produtoEdicao.produto.codigo,  	");		
    		hql.append(" movimento.produtoEdicao.produto.nome, 		");
    		hql.append(" movimento.produtoEdicao.numeroEdicao, 		");
    		hql.append(" movimento.produtoEdicao.precoVenda 		");
        }


		
		PaginacaoVO paginacao = filtro.getPaginacao();

		if (!indBuscaQtd && filtro.getOrdenacaoColuna() != null) {

			hql.append(" order by ");
			
			String orderByColumn = "";
			
				switch (filtro.getOrdenacaoColuna()) {
				
					case CODIGO_PRODUTO:
						orderByColumn = " movimento.produtoEdicao.produto.codigo ";
						break;
					case NOME_PRODUTO:
						orderByColumn = " movimento.produtoEdicao.produto.codigo.nome ";
						break;
					case NUMERO_EDICAO:
						orderByColumn = " movimento.produtoEdicao.numeroEdicao ";
						break;
					case PRECO_CAPA:
						orderByColumn = " movimento.produtoEdicao.precoVenda ";
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
		
		if(indBuscaQtd){
    		hql.append(" ) ");		
        }
		
		return hql.toString();
	}
	
	private Query criarQueryComParametrosObterListaContagemDevolucao(String hql, FiltroDigitacaoContagemDevolucaoDTO filtro, TipoMovimentoEstoque tipoMovimentoEstoque, boolean indBuscaTotalParcial, boolean indBuscaQtd) {
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataInicial", filtro.getPeriodo().getDataInicial());

		query.setParameter("dataFinal", filtro.getPeriodo().getDataFinal());

		query.setParameter("tipoMovimentoEstoque", tipoMovimentoEstoque);
		
		if(indBuscaTotalParcial && !indBuscaQtd) {
			query.setParameter("statusAprovacao", StatusAprovacao.PENDENTE);
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
	 * Descreve subquery que retorna a somatórias das qtds de devolução parciais.
	 * 
	 * @return SubQuery
	 */
	private StringBuffer getSubQueryConfEncParc() {
		
		StringBuffer hqlConfEncParcial = new StringBuffer("")
		.append(" ( select sum(parcial.qtde) 								")
		.append(" from ConferenciaEncalheParcial parcial					")
		.append(" where 													")
		.append(" parcial.statusAprovacao = :statusAprovacao and			")
		.append(" movimento.produtoEdicao.id = parcial.produtoEdicao.id and ")
		.append(" movimento.data = parcial.dataMovimento )  	");
		
		return hqlConfEncParcial;
		
	}
	
	/**
	 * Descreve subquery que retorna uma lista de idProdutoEdicao pertencentes a um fornecedor.
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
		
		hql.append(" select sum( movimento.qtde * movimento.produtoEdicao.precoVenda ) ");		
		
		hql.append(" from MovimentoEstoqueCota movimento ");
		
		hql.append(" where ");

		if( filtro.getIdFornecedor() != null ) {
			hql.append(" movimento.produtoEdicao.id in " + getSubQueryEdicoesDeFornecedor() + " and ");		
		}
		
		hql.append(" ( movimento.data between :dataInicial and :dataFinal ) and	");
		
		hql.append(" movimento.tipoMovimento = :tipoMovimentoEstoque ");
		
		Query query = getSession().createQuery(hql.toString());

		query.setParameter("dataInicial", filtro.getPeriodo().getDataInicial());
		
		query.setParameter("dataFinal", filtro.getPeriodo().getDataFinal());

		query.setParameter("tipoMovimentoEstoque", tipoMovimentoEstoque);
		
		if(filtro.getIdFornecedor() != null) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		BigDecimal valor = (BigDecimal) query.uniqueResult();
		
		return ((valor == null) ? BigDecimal.ZERO : valor);
		
	}
	
	/**
	 * Obtém a quantidade de registro referente a contagem devolução de acordo com 
	 * parâmetros de pesquisa.
	 * 
	 * @param filtro
	 * @param tipoMovimentoEstoque
	 * 
	 * @return qtdRegistro
	 */
	public Integer obterQuantidadeContagemDevolucao(FiltroDigitacaoContagemDevolucaoDTO filtro, TipoMovimentoEstoque tipoMovimentoEstoque) {
		
		String hql = getConsultaListaContagemDevolucao(filtro, false, true);
		
		Query query = criarQueryComParametrosObterListaContagemDevolucao(hql, filtro, tipoMovimentoEstoque, false, true);
		
		Long qtde = (Long) query.uniqueResult();
		
		return ((qtde == null) ? 0 : qtde.intValue());
		
	}

	
}
