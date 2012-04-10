package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaEncalheDTO;
import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class MovimentoEstoqueCotaRepositoryImpl extends AbstractRepository<MovimentoEstoqueCota, Long> implements MovimentoEstoqueCotaRepository {

	public MovimentoEstoqueCotaRepositoryImpl() {
		super(MovimentoEstoqueCota.class);
	}

	public BigDecimal obterQtdProdutoEdicaoEncalhe(FiltroConsultaEncalheDTO filtro, Long idTipoMovimento, boolean indQtdEncalheAposPrimeiroDia) {

		StringBuffer sql = new StringBuffer();

		sql.append("	select count(*) from  ( select	");

		sql.append("	distinct(lancamento.produto_edicao_id)	");
		
		sql.append("	from	");
		
		sql.append("	lancamento ");

		sql.append("	where	");
		
		sql.append("	lancamento.data_rec_distrib = :dataRecolhimentoDistribuidor  	 ");
		
		sql.append(" ) as idEdicoes ");
		
		SQLQuery sqlquery = getSession().createSQLQuery(sql.toString());
		
		sqlquery.setParameter("dataRecolhimentoDistribuidor", filtro.getDataRecolhimento());

		BigDecimal qtde = (BigDecimal) sqlquery.uniqueResult();
		
		return ((qtde == null) ? BigDecimal.ZERO : qtde);
		
	}

	public BigDecimal obterQtdItemProdutoEdicaoEncalhe(FiltroConsultaEncalheDTO filtro, Long idTipoMovimento, boolean indQtdEncalheAposPrimeiroDia) {

		StringBuffer sql = new StringBuffer();

		sql.append("	select	");

		sql.append("	sum(movimento_estoque_cota.qtde) ");

		sql.append("	from	");
		
		sql.append("	lancamento left join movimento_estoque_cota on ");
		sql.append("	(lancamento.produto_edicao_id = movimento_estoque_cota.produto_edicao_id) ");

		sql.append("	where	");
		
		sql.append("	lancamento.data_rec_distrib = :dataRecolhimentoDistribuidor and ");
		sql.append("	( movimento_estoque_cota.tipo_movimento_id = :idTipoMovimento or  movimento_estoque_cota.tipo_movimento_id is null ) ");
		
		if(filtro.getIdCota()!=null) {
			sql.append(" and ( movimento_estoque_cota.cota_id is null or movimento_estoque_cota.cota_id = :idCota )  ");
		}

		SQLQuery sqlquery = getSession().createSQLQuery(sql.toString());
		
		sqlquery.setParameter("idTipoMovimento", idTipoMovimento);

		if(filtro.getIdCota()!=null) {
			sqlquery.setParameter("idCota", filtro.getIdCota());
		}
		
		sqlquery.setParameter("dataRecolhimentoDistribuidor", filtro.getDataRecolhimento());
		
		BigDecimal qtde = (BigDecimal) sqlquery.uniqueResult();
		
		return ((qtde == null) ? BigDecimal.ZERO : qtde);		
	}

	
	
	/**
	 * Obtém a quantidade de resultados para a consulta encalhe.
	 * 
	 * @param filtro
	 * @param idTipoMovimento
	 * 
	 * @return qtde
	 */
	public Integer obterQtdConsultaEncalhe(FiltroConsultaEncalheDTO filtro, Long idTipoMovimento) {
		
		StringBuffer sql = new StringBuffer();

		sql.append("	select count(*) from  ( select	");

		sql.append("	lancamento.data_rec_distrib as dataDoRecolhimentoDistribuidor,  ");
		sql.append("	movimento_estoque_cota.data as dataMovimento,                   ");
		sql.append("	estoque_produto_cota.qtde_recebida as reparte                   ");

		sql.append("	from	");
		sql.append("	lancamento left join movimento_estoque_cota on                              ");
		sql.append("	(lancamento.produto_edicao_id = movimento_estoque_cota.produto_edicao_id)   ");

		sql.append("	left join estoque_produto_cota on                                           ");
		sql.append("	(movimento_estoque_cota.estoque_prod_cota_id =  estoque_produto_cota.id)    ");

		sql.append("	where	");
		sql.append("	lancamento.data_rec_distrib = :dataRecolhimentoDistribuidor and             ");
		sql.append("	( movimento_estoque_cota.tipo_movimento_id = :idTipoMovimento or  movimento_estoque_cota.tipo_movimento_id is null ) ");
		
		if(filtro.getIdCota()!=null) {
			sql.append(" and ( movimento_estoque_cota.cota_id is null or movimento_estoque_cota.cota_id = :idCota )  ");
		}
		
		sql.append("	group by  ");
		sql.append("	lancamento.data_rec_distrib,             ");
		sql.append("	movimento_estoque_cota.data,             ");
		sql.append("	estoque_produto_cota.qtde_recebida ) as recolhimentos ");

		SQLQuery sqlquery = getSession().createSQLQuery(sql.toString());
		
		sqlquery.setParameter("idTipoMovimento", idTipoMovimento);
		
		if(filtro.getIdCota()!=null) {
			sqlquery.setParameter("idCota", filtro.getIdCota());
		}
		
		sqlquery.setParameter("dataRecolhimentoDistribuidor", filtro.getDataRecolhimento());

		BigInteger qtde = (BigInteger) sqlquery.uniqueResult();
		
		return ((qtde == null) ? 0 : qtde.intValue());
		
	}
	
	/**
	 * Obtém uma lista de consulta encalhe
	 * 
	 * @param filtro
	 * @param idTipoMovimento
	 * 
	 * @return List - ConsultaEncalheDTO
	 */
	public List<ConsultaEncalheDTO> obterListaConsultaEncalhe(FiltroConsultaEncalheDTO filtro, Long idTipoMovimento) {

		StringBuffer sql = new StringBuffer();

		sql.append("	select	");

		sql.append("	lancamento.data_rec_distrib as dataDoRecolhimentoDistribuidor,    ");
		sql.append("	movimento_estoque_cota.data as dataMovimento,                     ");
		sql.append("	produto.codigo as codigoProduto,                                  ");
		sql.append("	produto.nome as nomeProduto,                                      ");
		sql.append("	produto_edicao.numero_edicao as numeroEdicao,                     ");
		sql.append("	produto_edicao.preco_venda as precoVenda,                         ");
		sql.append("	(produto_edicao.preco_venda - produto_edicao.desconto ) as precoComDesconto,  ");
		sql.append("	estoque_produto_cota.qtde_recebida as reparte, ");
		sql.append("	sum(movimento_estoque_cota.qtde) as encalhe,   ");
		
		//sql.append(" ( select   ) as fornecedor  ");
		
		sql.append("	sum(		");
		sql.append("	    movimento_estoque_cota.qtde * (produto_edicao.preco_venda -  produto_edicao.desconto)      		");
		sql.append("	) as total, ");
		sql.append("	((TO_DAYS(movimento_estoque_cota.data) - TO_DAYS(lancamento.data_rec_distrib)) + 1) as recolhimento ");

		sql.append("	from	");
		
		sql.append("	produto_edicao,  ");
		sql.append("	produto,         ");
		sql.append("	lancamento left join movimento_estoque_cota on ");
		sql.append("	(lancamento.produto_edicao_id = movimento_estoque_cota.produto_edicao_id) ");
		sql.append("	left join estoque_produto_cota on                                         ");
		sql.append("	(movimento_estoque_cota.estoque_prod_cota_id =  estoque_produto_cota.id)  ");

		sql.append("	where	");
		
		sql.append("	lancamento.data_rec_distrib = :dataRecolhimentoDistribuidor and  ");
		sql.append("	lancamento.produto_edicao_id = produto_edicao.id and             ");
		sql.append("	produto_edicao.produto_id = produto.id and                       ");
		sql.append("	( movimento_estoque_cota.tipo_movimento_id = :idTipoMovimento or  movimento_estoque_cota.tipo_movimento_id is null ) ");
		
		if(filtro.getIdCota()!=null) {
			sql.append(" and ( movimento_estoque_cota.cota_id is null or movimento_estoque_cota.cota_id = :idCota )  ");
		}
		
		sql.append("	group by	");

		sql.append("	lancamento.data_rec_distrib,       ");
		sql.append("	movimento_estoque_cota.data,       ");
		sql.append("	produto.codigo,                    ");
		sql.append("	produto.nome,                      ");
		sql.append("	produto_edicao.numero_edicao,      ");
		sql.append("	produto_edicao.preco_venda,        ");
		sql.append("	produto_edicao.desconto,           ");
		sql.append("	estoque_produto_cota.qtde_recebida ");
		
		
		PaginacaoVO paginacao = filtro.getPaginacao();

		if (filtro.getOrdenacaoColuna() != null) {

			sql.append(" order by ");
			
			String orderByColumn = "";
			
				switch (filtro.getOrdenacaoColuna()) {
				
					case CODIGO_PRODUTO:
						orderByColumn = " codigoProduto ";
						break;
					case NOME_PRODUTO:
						orderByColumn = " nomeProduto ";
						break;
					case NUMERO_EDICAO:
						orderByColumn = " numeroEdicao ";
						break;
					case PRECO_CAPA:
						orderByColumn = " precoVenda ";
						break;
					case PRECO_COM_DESCONTO:
						orderByColumn = " precoComDesconto ";
						break;
					case REPARTE:
						orderByColumn = " reparte ";
						break;
					case ENCALHE:
						orderByColumn = " encalhe ";
						break;
					case FORNECEDOR:
						orderByColumn = " fornecedor ";
						break;
					case TOTAL:
						orderByColumn = " total ";
						break;
					case RECOLHIMENTO:
						orderByColumn = " recolhimento ";
						break;
					default:
						break;
				}
			
			sql.append(orderByColumn);
			
			if (paginacao.getOrdenacao() != null) {
				
				sql.append(paginacao.getOrdenacao().toString());
				
			}
			
		}

		SQLQuery sqlquery = getSession().createSQLQuery(sql.toString());
		
		sqlquery.addScalar("codigoProduto");
		sqlquery.addScalar("nomeProduto");
		sqlquery.addScalar("numeroEdicao");
		sqlquery.addScalar("precoVenda");
		sqlquery.addScalar("precoComDesconto");
		sqlquery.addScalar("reparte");
		sqlquery.addScalar("encalhe");
		sqlquery.addScalar("total");
		sqlquery.addScalar("recolhimento");
		
		sqlquery.setResultTransformer(new AliasToBeanResultTransformer(ConsultaEncalheDTO.class));
		
		sqlquery.setParameter("idTipoMovimento", idTipoMovimento);
		
		if(filtro.getIdCota()!=null) {
			sqlquery.setParameter("idCota", filtro.getIdCota());
		}

		sqlquery.setParameter("dataRecolhimentoDistribuidor", filtro.getDataRecolhimento());

		return sqlquery.list();
		
	}	
	
	
	private String getConsultaListaContagemDevolucao(FiltroDigitacaoContagemDevolucaoDTO filtro, boolean indBuscaTotalParcial, boolean indBuscaQtd) {
		
		StringBuffer hqlEdicoes = getSubQueryEdicoesDeFornecedor();
		
		StringBuffer hqlControleContagemDevolucaoConcluido = getSubQueryControleContagemDevolucaoConcluido();
		
		StringBuffer hqlConfEncParcial = getSubQueryConfEncParc();
		
		StringBuffer hql = new StringBuffer("");
		
		if (indBuscaQtd) {

			hql.append(" select count(mov.id) from MovimentoEstoqueCota mov where mov.id in ( select max(movimento.id) " );		
			
		} else {

			hql.append(" select ");		
			
			hql.append(" movimento.produtoEdicao.produto.codigo as codigoProduto,  	");		
			hql.append(" movimento.produtoEdicao.produto.nome as nomeProduto, 		");
			hql.append(" movimento.produtoEdicao.numeroEdicao as numeroEdicao, 		");
			hql.append(" movimento.produtoEdicao.precoVenda as precoVenda, 			");
			hql.append(" sum(movimento.qtde) as qtdDevolucao, 						");
			
			if(indBuscaTotalParcial) {
				hql.append( hqlConfEncParcial.toString() + "  as qtdNota, 			");
			}
			
			hql.append(" movimento.data as dataMovimento  							");
			
		}
		
		hql.append(" from ");		
		
		hql.append(" MovimentoEstoqueCota movimento ");		
		
		hql.append(" where ");	
		
		hql.append(" ( movimento.data between :dataInicial and :dataFinal ) and 		");		

		hql.append( hqlControleContagemDevolucaoConcluido.toString() + " is null and 	");
		
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
						orderByColumn = " qtdDevolucao ";
						break;
					case QTD_NOTA:
						orderByColumn = " qtdNota ";
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
		
		Query query = null;
		
		if(indBuscaQtd) {
			query = getSession().createQuery(hql.toString());
		} else {
			query = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(ContagemDevolucaoDTO.class));
		}
		
		query.setParameter("dataInicial", filtro.getPeriodo().getDataInicial());
		
		query.setParameter("dataFinal", filtro.getPeriodo().getDataFinal());
		
		query.setParameter("tipoMovimentoEstoque", tipoMovimentoEstoque);
		
		query.setParameter("statusOperacao", StatusOperacao.CONCLUIDO);
		
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
			boolean indBuscaTotalParcial) {
		
		String hql = getConsultaListaContagemDevolucao(filtro, indBuscaTotalParcial, false);
		
		Query query = criarQueryComParametrosObterListaContagemDevolucao(hql, filtro, tipoMovimentoEstoque, indBuscaTotalParcial, false);
		
		if(filtro.getPaginacao()!=null) {
			
			if(filtro.getPaginacao().getPosicaoInicial()!=null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if(filtro.getPaginacao().getQtdResultadosPorPagina()!=null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
			
		}
		
		
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
	 * Descreve subquery que retorna o id do registro de controleDevolucao caso encontra-lo 
	 * com status CONCLUIDO
	 * 
	 * @return SubQuery
	 */
	private StringBuffer getSubQueryControleContagemDevolucaoConcluido() {
		
		StringBuffer hqlConfEncParcial = new StringBuffer("")
		.append(" ( select													")
		.append(" controleContagemDevolucao.id									")
		.append(" from ControleContagemDevolucao controleContagemDevolucao 	")
		.append(" where 													")
		.append(" controleContagemDevolucao.status = :statusOperacao and	")
		.append(" controleContagemDevolucao.produtoEdicao.id =  movimento.produtoEdicao.id and 	")
		.append(" controleContagemDevolucao.data = movimento.data )  							");
		
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
		
		StringBuffer hqlControleContagemDevolucaoConcluido = getSubQueryControleContagemDevolucaoConcluido();
		
		StringBuffer hql = new StringBuffer("");
		
		hql.append(" select sum( movimento.qtde * movimento.produtoEdicao.precoVenda ) ");		
		
		hql.append(" from MovimentoEstoqueCota movimento ");
		
		hql.append(" where ");

		if( filtro.getIdFornecedor() != null ) {
			hql.append(" movimento.produtoEdicao.id in " + getSubQueryEdicoesDeFornecedor() + " and ");		
		}
		
		hql.append(" ( movimento.data between :dataInicial and :dataFinal ) and	");
		
		hql.append(" movimento.tipoMovimento = :tipoMovimentoEstoque and ");
		
		hql.append( hqlControleContagemDevolucaoConcluido.toString() + " is null " );
		
		Query query = getSession().createQuery(hql.toString());

		query.setParameter("dataInicial", filtro.getPeriodo().getDataInicial());
		
		query.setParameter("dataFinal", filtro.getPeriodo().getDataFinal());

		query.setParameter("tipoMovimentoEstoque", tipoMovimentoEstoque);
		
		query.setParameter("statusOperacao", StatusOperacao.CONCLUIDO);
		
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
	
	/**
	 * Obtém o Movimento de Estoque da cota pelo Tipo de Movimento 
	 * @param data
	 * @param idCota
	 * @param grupoMovimentoEstoque
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MovimentoEstoqueCota> obterMovimentoCotaPorTipoMovimento(Date data, Long idCota, GrupoMovimentoEstoque grupoMovimentoEstoque){
		
		StringBuffer hql = new StringBuffer("");
		
		hql.append(" from MovimentoEstoqueCota movimento");			
		
		hql.append(" where movimento.cota.id = :idCota ");
		
		hql.append(" and movimento.data = :data ");
		
		hql.append(" and movimento.tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEstoque ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("data", data);
		
		query.setParameter("idCota", idCota);
		
		query.setParameter("grupoMovimentoEstoque", grupoMovimentoEstoque);
		
		return query.list();
		
	}

	
}
