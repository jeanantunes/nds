package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaEncalheDTO;
import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class MovimentoEstoqueCotaRepositoryImpl extends AbstractRepository<MovimentoEstoqueCota, Long> implements MovimentoEstoqueCotaRepository {

	public MovimentoEstoqueCotaRepositoryImpl() {
		super(MovimentoEstoqueCota.class);
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.MovimentoEstoqueCotaRepository#obterListaMovimentoEstoqueCotaParaOperacaoConferenciaEncalhe(java.lang.Long)
	 */
	public List<MovimentoEstoqueCota> obterListaMovimentoEstoqueCotaParaOperacaoConferenciaEncalhe(Long idControleConferenciaEncalheCota) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select conferenciaEncalhe.movimentoEstoqueCota  ");			
		
		hql.append(" from ConferenciaEncalhe conferenciaEncalhe ");
		
		hql.append(" where conferenciaEncalhe.controleConferenciaEncalheCota.id = :idControleConferenciaEncalheCota ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idControleConferenciaEncalheCota", idControleConferenciaEncalheCota);
		
		return query.list();
		
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.MovimentoEstoqueCotaRepository#obterQtdeMovimentoEstoqueCotaParaProdutoEdicaoNoPeriodo(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date, br.com.abril.nds.model.estoque.OperacaoEstoque)
	 */
	public BigDecimal obterQtdeMovimentoEstoqueCotaParaProdutoEdicaoNoPeriodo(
			Long idCota,
			Long idProdutoEdicao,
			Date dataInicial, 
			Date dataFinal,
			OperacaoEstoque operacaoEstoque) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select sum(movimentoEstoqueCota.qtde) ");			
		
		hql.append(" from MovimentoEstoqueCota movimentoEstoqueCota ");
		
		hql.append(" where movimentoEstoqueCota.data between :dataInicial and :dataFinal and ");

		hql.append(" movimentoEstoqueCota.cota.id = :idCota and ");
		
		hql.append(" movimentoEstoqueCota.produtoEdicao.id = :idProdutoEdicao and ");
		
		hql.append(" movimentoEstoqueCota.tipoMovimento.operacaoEstoque = :operacaoEstoque ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataInicial", dataInicial);
		query.setParameter("dataFinal", dataFinal);
		query.setParameter("idCota", idCota);
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setParameter("operacaoEstoque", operacaoEstoque);
		
		return (BigDecimal) query.uniqueResult();
		
	}

	
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.MovimentoEstoqueCotaRepository#obterQtdProdutoEdicaoEncalhe(br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO, boolean)
	 */
	public Integer obterQtdProdutoEdicaoEncalhe(FiltroConsultaEncalheDTO filtro, boolean indQtdEncalheAposPrimeiroDia) {

		StringBuffer sql = new StringBuffer();

		sql.append("	select count(*) from  ( select	");

		sql.append("	distinct(MOVIMENTO_ESTOQUE_COTA.PRODUTO_EDICAO_ID)	");
		
		sql.append("	from	");

		sql.append("	MOVIMENTO_ESTOQUE_COTA inner join CONFERENCIA_ENCALHE on ");
		sql.append("	( MOVIMENTO_ESTOQUE_COTA.ID = CONFERENCIA_ENCALHE.MOVIMENTO_ESTOQUE_COTA_ID ) ");
		
		sql.append("	inner join CHAMADA_ENCALHE_COTA on ");
		sql.append("	( CHAMADA_ENCALHE_COTA.ID = CONFERENCIA_ENCALHE.CHAMADA_ENCALHE_COTA_ID ) ");
		
		sql.append("	inner join CHAMADA_ENCALHE on ");
		sql.append("	( CHAMADA_ENCALHE.ID = CHAMADA_ENCALHE_COTA.CHAMADA_ENCALHE_ID ) ");
		
		sql.append("	inner join PRODUTO_EDICAO on ");
		sql.append("	( PRODUTO_EDICAO.ID = MOVIMENTO_ESTOQUE_COTA.PRODUTO_EDICAO_ID ) ");
		
		sql.append("	inner join PRODUTO on ");
		sql.append("	( PRODUTO_EDICAO.PRODUTO_ID = PRODUTO.ID ) ");
		
		sql.append("	inner join PRODUTO_FORNECEDOR on ");
		sql.append("	( PRODUTO_FORNECEDOR.PRODUTO_ID = PRODUTO.ID ) ");
		
		sql.append("	inner join FORNECEDOR on ");
		sql.append("	( PRODUTO_FORNECEDOR.FORNECEDORES_ID = FORNECEDOR.ID ) ");
		
		sql.append("	where	");
		
		sql.append("	MOVIMENTO_ESTOQUE_COTA.DATA = :dataRecolhimento ");
		
		if(indQtdEncalheAposPrimeiroDia) {
			sql.append(" and MOVIMENTO_ESTOQUE_COTA.DATA > CHAMADA_ENCALHE.DATA_RECOLHIMENTO 	");
		} else {
			sql.append(" and MOVIMENTO_ESTOQUE_COTA.DATA = CHAMADA_ENCALHE.DATA_RECOLHIMENTO	");
		}
		
		if(filtro.getIdFornecedor() != null) {
			sql.append(" and FORNECEDOR.ID =  :idFornecedor ");
		}
		
		if(filtro.getIdCota()!=null) {
			sql.append(" and  MOVIMENTO_ESTOQUE_COTA.COTA_ID = :idCota ");
		}
		
		sql.append(" ) as idEdicoes ");
		
		SQLQuery sqlquery = getSession().createSQLQuery(sql.toString());

		sqlquery.setParameter("dataRecolhimento", filtro.getDataRecolhimento());
		
		if(filtro.getIdCota()!=null) {
			sqlquery.setParameter("idCota", filtro.getIdCota());
		}
		
		if(filtro.getIdFornecedor() != null) {
			sqlquery.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		BigInteger qtde = (BigInteger) sqlquery.uniqueResult();
		
		return ((qtde == null) ? 0 : qtde.intValue());
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.MovimentoEstoqueCotaRepository#obterQtdItemProdutoEdicaoEncalhe(br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO, boolean)
	 */
	public BigDecimal obterQtdItemProdutoEdicaoEncalhe(FiltroConsultaEncalheDTO filtro, boolean indQtdEncalheAposPrimeiroDia) {

		StringBuffer sql = new StringBuffer();
		
		sql.append("	select	");

		sql.append("	sum(MOVIMENTO_ESTOQUE_COTA.QTDE) ");

		sql.append("	from	");
		
		sql.append("	MOVIMENTO_ESTOQUE_COTA inner join CONFERENCIA_ENCALHE on ");
		sql.append("	( MOVIMENTO_ESTOQUE_COTA.ID = CONFERENCIA_ENCALHE.MOVIMENTO_ESTOQUE_COTA_ID ) ");
		
		sql.append("	inner join CHAMADA_ENCALHE_COTA on ");
		sql.append("	( CHAMADA_ENCALHE_COTA.ID = CONFERENCIA_ENCALHE.CHAMADA_ENCALHE_COTA_ID ) ");
		
		sql.append("	inner join CHAMADA_ENCALHE on ");
		sql.append("	( CHAMADA_ENCALHE.ID = CHAMADA_ENCALHE_COTA.CHAMADA_ENCALHE_ID ) ");
		
		sql.append("	inner join PRODUTO_EDICAO on ");
		sql.append("	( PRODUTO_EDICAO.ID = MOVIMENTO_ESTOQUE_COTA.PRODUTO_EDICAO_ID ) ");
		
		sql.append("	inner join PRODUTO on ");
		sql.append("	( PRODUTO_EDICAO.PRODUTO_ID = PRODUTO.ID ) ");
		
		sql.append("	inner join PRODUTO_FORNECEDOR on ");
		sql.append("	( PRODUTO_FORNECEDOR.PRODUTO_ID = PRODUTO.ID ) ");
		
		sql.append("	inner join FORNECEDOR on ");
		sql.append("	( PRODUTO_FORNECEDOR.FORNECEDORES_ID = FORNECEDOR.ID ) ");
		
		sql.append("	where	");

		sql.append("	MOVIMENTO_ESTOQUE_COTA.DATA = :dataRecolhimento ");
		
		if(indQtdEncalheAposPrimeiroDia) {
			sql.append(" and MOVIMENTO_ESTOQUE_COTA.DATA > CHAMADA_ENCALHE.DATA_RECOLHIMENTO 	");
		} else {
			sql.append(" and MOVIMENTO_ESTOQUE_COTA.DATA = CHAMADA_ENCALHE.DATA_RECOLHIMENTO	");
		}
		
		if(filtro.getIdFornecedor() != null) {
			sql.append(" and FORNECEDOR.ID =  :idFornecedor ");
		}
		
		if(filtro.getIdCota()!=null) {
			sql.append(" and  MOVIMENTO_ESTOQUE_COTA.COTA_ID = :idCota ");
		}
		
		SQLQuery sqlquery = getSession().createSQLQuery(sql.toString());

		sqlquery.setParameter("dataRecolhimento", filtro.getDataRecolhimento());

		
		if(filtro.getIdCota()!=null) {
			sqlquery.setParameter("idCota", filtro.getIdCota());
		}
		
		if(filtro.getIdFornecedor() != null) {
			sqlquery.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		BigDecimal qtde = (BigDecimal) sqlquery.uniqueResult();
		
		return ((qtde == null) ? BigDecimal.ZERO : qtde);		
	}

	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.MovimentoEstoqueCotaRepository#obterQtdConsultaEncalhe(br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO)
	 */
	public Integer obterQtdConsultaEncalhe(FiltroConsultaEncalheDTO filtro) {
		
		StringBuffer sql = new StringBuffer();

		sql.append("	select count(*) from  ( ");

		sql.append("	select	");

		sql.append("	PRODUTO.CODIGO as codigo, 						");
		sql.append("	PRODUTO_EDICAO.NUMERO_EDICAO as numeroEdicao, 	");
		sql.append("	ESTOQUE_PRODUTO_COTA.ID as cotaId, 				");
		sql.append(" 	PESSOA.ID as pessoaId 							");

		sql.append("	from	");
		
		sql.append("	MOVIMENTO_ESTOQUE_COTA inner join CONFERENCIA_ENCALHE on ");
		sql.append("	( CONFERENCIA_ENCALHE.MOVIMENTO_ESTOQUE_COTA_ID = MOVIMENTO_ESTOQUE_COTA.ID	) ");

		sql.append("	inner join ESTOQUE_PRODUTO_COTA on                                         ");
		sql.append("	( MOVIMENTO_ESTOQUE_COTA.ESTOQUE_PROD_COTA_ID = ESTOQUE_PRODUTO_COTA.ID )  ");
		
		sql.append("	inner join CHAMADA_ENCALHE_COTA on ");
		sql.append("	( CHAMADA_ENCALHE_COTA.ID = CONFERENCIA_ENCALHE.CHAMADA_ENCALHE_COTA_ID ) ");
		
		sql.append("	inner join CHAMADA_ENCALHE on ");
		sql.append("	( CHAMADA_ENCALHE.ID = CHAMADA_ENCALHE_COTA.CHAMADA_ENCALHE_ID ) ");
 		
		sql.append("	inner join PRODUTO_EDICAO on ");
		sql.append("	( PRODUTO_EDICAO.ID = MOVIMENTO_ESTOQUE_COTA.PRODUTO_EDICAO_ID ) ");
		
		sql.append("	inner join PRODUTO on ");
		sql.append("	( PRODUTO_EDICAO.PRODUTO_ID = PRODUTO.ID ) ");
		
		sql.append("	inner join PRODUTO_FORNECEDOR on ");
		sql.append("	( PRODUTO_FORNECEDOR.PRODUTO_ID = PRODUTO.ID ) ");
		
		sql.append("	inner join FORNECEDOR on ");
		sql.append("	( PRODUTO_FORNECEDOR.FORNECEDORES_ID = FORNECEDOR.ID ) ");
		
		sql.append("	inner join PESSOA on                   	");
		sql.append("	( PESSOA.ID = FORNECEDOR.JURIDICA_ID )	");
		
		sql.append("	where	");
		
		sql.append("	MOVIMENTO_ESTOQUE_COTA.DATA = :dataRecolhimento ");
		
		if(filtro.getIdCota()!=null) {
			sql.append(" and MOVIMENTO_ESTOQUE_COTA.COTA_ID = :idCota  ");
		}
		
		if(filtro.getIdFornecedor() != null) {
			sql.append(" and FORNECEDOR.ID =  :idFornecedor ");
		}
		

		sql.append("	group by	");
		
		sql.append("	PRODUTO.CODIGO, 				");
		sql.append("	PRODUTO_EDICAO.NUMERO_EDICAO, 	");
		sql.append("	ESTOQUE_PRODUTO_COTA.ID, 	    ");
		sql.append(" 	PESSOA.ID 						");
		
		sql.append(" )	as encalhes ");
		
		SQLQuery sqlquery = getSession().createSQLQuery(sql.toString());
		
		if(filtro.getIdCota()!=null) {
			sqlquery.setParameter("idCota", filtro.getIdCota());
		}
		
		if(filtro.getIdFornecedor() != null) {
			sqlquery.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		sqlquery.setParameter("dataRecolhimento", filtro.getDataRecolhimento());

		BigInteger qtde = (BigInteger) sqlquery.uniqueResult();
		
		return ((qtde == null) ? 0 : qtde.intValue());
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.MovimentoEstoqueCotaRepository#obterListaConsultaEncalhe(br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO)
	 */
	@SuppressWarnings("unchecked")
	public List<ConsultaEncalheDTO> obterListaConsultaEncalhe(FiltroConsultaEncalheDTO filtro) {

		StringBuffer sql = new StringBuffer();

		sql.append("	select	");

		sql.append("	CHAMADA_ENCALHE.DATA_RECOLHIMENTO		as dataDoRecolhimentoDistribuidor, ");
		sql.append("	PRODUTO.CODIGO 					as codigoProduto, ");
		sql.append("	PRODUTO.NOME 					as nomeProduto,   ");
		sql.append("	PRODUTO_EDICAO.NUMERO_EDICAO 	as numeroEdicao,  ");
		sql.append("	PRODUTO_EDICAO.PRECO_VENDA 		as precoVenda,    ");
		
		sql.append("	(PRODUTO_EDICAO.PRECO_VENDA - PRODUTO_EDICAO.DESCONTO ) as precoComDesconto,  ");

		sql.append("	ESTOQUE_PRODUTO_COTA.QTDE_RECEBIDA 	as reparte,  	");
		sql.append("	sum(MOVIMENTO_ESTOQUE_COTA.QTDE) 	as encalhe,     ");
		sql.append(" 	PESSOA.RAZAO_SOCIAL 				as fornecedor,  ");
		
		sql.append("	sum(		");
		sql.append("	    MOVIMENTO_ESTOQUE_COTA.QTDE * (PRODUTO_EDICAO.PRECO_VENDA -  PRODUTO_EDICAO.DESCONTO)      		");
		sql.append("	) as total, ");
		
		sql.append("	((TO_DAYS(MOVIMENTO_ESTOQUE_COTA.DATA) - TO_DAYS(CHAMADA_ENCALHE.DATA_RECOLHIMENTO)) + 1) as recolhimento ");

		sql.append("	from	");
		
		sql.append("	MOVIMENTO_ESTOQUE_COTA inner join CONFERENCIA_ENCALHE on ");
		sql.append("	( CONFERENCIA_ENCALHE.MOVIMENTO_ESTOQUE_COTA_ID = MOVIMENTO_ESTOQUE_COTA.ID	) ");

		sql.append("	inner join ESTOQUE_PRODUTO_COTA on                                         ");
		sql.append("	( MOVIMENTO_ESTOQUE_COTA.ESTOQUE_PROD_COTA_ID = ESTOQUE_PRODUTO_COTA.ID )  ");
		
		sql.append("	inner join CHAMADA_ENCALHE_COTA on ");
		sql.append("	( CHAMADA_ENCALHE_COTA.ID = CONFERENCIA_ENCALHE.CHAMADA_ENCALHE_COTA_ID ) ");
		
		sql.append("	inner join CHAMADA_ENCALHE on ");
		sql.append("	( CHAMADA_ENCALHE.ID = CHAMADA_ENCALHE_COTA.CHAMADA_ENCALHE_ID ) ");
 		
		sql.append("	inner join PRODUTO_EDICAO on ");
		sql.append("	( PRODUTO_EDICAO.ID = MOVIMENTO_ESTOQUE_COTA.PRODUTO_EDICAO_ID ) ");
		
		sql.append("	inner join PRODUTO on ");
		sql.append("	( PRODUTO_EDICAO.PRODUTO_ID = PRODUTO.ID ) ");
		
		sql.append("	inner join PRODUTO_FORNECEDOR on ");
		sql.append("	( PRODUTO_FORNECEDOR.PRODUTO_ID = PRODUTO.ID ) ");
		
		sql.append("	inner join FORNECEDOR on ");
		sql.append("	( PRODUTO_FORNECEDOR.FORNECEDORES_ID = FORNECEDOR.ID ) ");
		
		sql.append("	inner join PESSOA on                   	");
		sql.append("	( PESSOA.ID = FORNECEDOR.JURIDICA_ID )	");
		
		sql.append("	where	");
		
		sql.append("	MOVIMENTO_ESTOQUE_COTA.DATA = :dataRecolhimento ");
		
		if(filtro.getIdCota()!=null) {
			sql.append(" and MOVIMENTO_ESTOQUE_COTA.COTA_ID = :idCota  ");
		}
		
		if(filtro.getIdFornecedor() != null) {
			sql.append(" and FORNECEDOR.ID =  :idFornecedor ");
		}
		

		sql.append("	group by	");
		
		sql.append("    MOVIMENTO_ESTOQUE_COTA.DATA,		");
		sql.append("	CHAMADA_ENCALHE.DATA_RECOLHIMENTO,	");
		sql.append("	PRODUTO.CODIGO,                     ");
		sql.append("	PRODUTO.NOME,                       ");
		sql.append("	PRODUTO_EDICAO.NUMERO_EDICAO,       ");
		sql.append("	PRODUTO_EDICAO.PRECO_VENDA,         ");
		sql.append("	PRODUTO_EDICAO.DESCONTO,            ");
		sql.append("	ESTOQUE_PRODUTO_COTA.QTDE_RECEBIDA, ");
		sql.append("    PESSOA.RAZAO_SOCIAL 				");
		
		
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

		SQLQuery sqlquery = getSession().createSQLQuery(sql.toString())

		.addScalar("dataDoRecolhimentoDistribuidor")
		.addScalar("codigoProduto")
		.addScalar("nomeProduto")
		.addScalar("numeroEdicao", StandardBasicTypes.LONG)
		.addScalar("precoVenda")
		.addScalar("precoComDesconto")
		.addScalar("reparte")
		.addScalar("encalhe")
		.addScalar("fornecedor")
		.addScalar("total")
		.addScalar("recolhimento");
		
		sqlquery.setResultTransformer(new AliasToBeanResultTransformer(ConsultaEncalheDTO.class));
		
		if(filtro.getIdCota()!=null) {
			sqlquery.setParameter("idCota", filtro.getIdCota());
		}

		if(filtro.getIdFornecedor() != null) {
			sqlquery.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		sqlquery.setParameter("dataRecolhimento", filtro.getDataRecolhimento());

		if(filtro.getPaginacao()!=null) {
			
			if(filtro.getPaginacao().getPosicaoInicial()!=null) {
				sqlquery.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if(filtro.getPaginacao().getQtdResultadosPorPagina()!=null) {
				sqlquery.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
			
		}
		
		return sqlquery.list();
		
	}	
	
	/**
	 * Obtém hql para pesquisa de ContagemDevolucao.
	 * 
	 * @param filtro
	 * @param indBuscaTotalParcial
	 * @param indBuscaQtd
	 * 
	 * @return String - hql
	 */
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
						orderByColumn = " sum(movimento.qtde) ";
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
	
	/**
	 * Carrega os parâmetros da pesquisa de ContagemDevolucao e retorna
	 * o objeto Query.
	 * 
	 * @param hql
	 * @param filtro
	 * @param tipoMovimentoEstoque
	 * @param indBuscaTotalParcial
	 * @param indBuscaQtd
	 * 
	 * @return Query
	 */
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
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.MovimentoEstoqueCotaRepository#obterListaContagemDevolucao(br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO, br.com.abril.nds.model.estoque.TipoMovimentoEstoque, boolean)
	 */
	@SuppressWarnings("unchecked")
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
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.MovimentoEstoqueCotaRepository#obterValorTotalGeralContagemDevolucao(br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO, br.com.abril.nds.model.estoque.TipoMovimentoEstoque)
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
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.MovimentoEstoqueCotaRepository#obterQuantidadeContagemDevolucao(br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO, br.com.abril.nds.model.estoque.TipoMovimentoEstoque)
	 */
	public Integer obterQuantidadeContagemDevolucao(FiltroDigitacaoContagemDevolucaoDTO filtro, TipoMovimentoEstoque tipoMovimentoEstoque) {
		
		String hql = getConsultaListaContagemDevolucao(filtro, false, true);
		
		Query query = criarQueryComParametrosObterListaContagemDevolucao(hql, filtro, tipoMovimentoEstoque, false, true);
		
		Long qtde = (Long) query.uniqueResult();
		
		return ((qtde == null) ? 0 : qtde.intValue());
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.MovimentoEstoqueCotaRepository#obterMovimentoCotaPorTipoMovimento(java.util.Date, java.lang.Long, br.com.abril.nds.model.estoque.GrupoMovimentoEstoque)
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
