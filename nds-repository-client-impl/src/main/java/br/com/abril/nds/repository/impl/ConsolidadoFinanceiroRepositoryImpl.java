package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.ContaCorrenteCotaVO;
import br.com.abril.nds.dto.ConsignadoCotaDTO;
import br.com.abril.nds.dto.ConsultaVendaEncalheDTO;
import br.com.abril.nds.dto.EncalheCotaDTO;
import br.com.abril.nds.dto.FiltroConsolidadoConsignadoCotaDTO;
import br.com.abril.nds.dto.ViewContaCorrenteCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoEncalheCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoVendaCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroViewContaCorrenteCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.StatusBaixa;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class ConsolidadoFinanceiroRepositoryImpl extends
		AbstractRepositoryModel<ConsolidadoFinanceiroCota, Long> implements
		ConsolidadoFinanceiroRepository {

	public ConsolidadoFinanceiroRepositoryImpl() {

		super(ConsolidadoFinanceiroCota.class);

	}

	@SuppressWarnings("unchecked")
	public List<ViewContaCorrenteCotaDTO> buscarListaDeConsolidado(
			Integer numeroCota) {

		StringBuffer hql = new StringBuffer();

		hql.append(" select ");

		hql.append("    consolidadoFinanceiroCota.dataConsolidado as dataConsolidado, 		");
		hql.append(" 	consolidadoFinanceiroCota.valorPostergado as valorPostergado,		");
		hql.append(" 	consolidadoFinanceiroCota.numeroAtrasados as numeroAtradao,	    	");
		hql.append(" 	consolidadoFinanceiroCota.consignado as consignado,       	        ");
		hql.append("  	consolidadoFinanceiroCota.encalhe as encalhe,               		");
		hql.append("  	consolidadoFinanceiroCota.vendaEncalhe as vendaEncalhe,     		");
		hql.append("  	consolidadoFinanceiroCota.debitoCredito as debCred,	            	");
		hql.append(" 	consolidadoFinanceiroCota.encargos as encargos,             		");
		hql.append(" 	consolidadoFinanceiroCota.pendente as pendente, 		        	");
		hql.append(" 	consolidadoFinanceiroCota.total as total ,          				");
		hql.append(" 	consolidadoFinanceiroCota.id as id            				        ");

		hql.append(" from ");

		hql.append(" ConsolidadoFinanceiroCota consolidadoFinanceiroCota ");

		hql.append(" where ");

		hql.append(" consolidadoFinanceiroCota.cota.numeroCota = :numeroCota ");

		Query query = getSession().createQuery(hql.toString());

		ResultTransformer resultTransformer = new AliasToBeanResultTransformer(
				ViewContaCorrenteCotaDTO.class);

		query.setResultTransformer(resultTransformer);

		query.setParameter("numeroCota", numeroCota);

		return query.list();
	}

	/**
	 * Método que obtém uma lista de encalhe por produto e cota
	 */
	@SuppressWarnings("unchecked")
	public List<EncalheCotaDTO> obterMovimentoEstoqueCotaEncalhe(
			FiltroConsolidadoEncalheCotaDTO filtro) {

		StringBuffer hql = new StringBuffer("select ");
		
		hql.append("	produto8_.CODIGO as codigoProduto, ")
           .append("	produto8_.NOME as nomeProduto, ")
           .append("	pessoajuri11_.RAZAO_SOCIAL as nomeFornecedor, ")
           .append("	produtoedi7_.NUMERO_EDICAO as numeroEdicao, ")
           .append("	produtoedi7_.PRECO_VENDA as precoCapa, ")
           .append("	movimentos4_.VALOR_DESCONTO as desconto, ")
           .append("	coalesce(movimentos4_.PRECO_COM_DESCONTO,produtoedi7_.PRECO_VENDA) as precoComDesconto, ")
           .append("	sum(movimentos4_.QTDE) * coalesce(movimentos4_.PRECO_COM_DESCONTO,produtoedi7_.PRECO_VENDA) as total,")
           .append("	sum(movimentos4_.QTDE) as encalhe  ")
           .append("from ")
           .append("	CONSOLIDADO_FINANCEIRO_COTA consolidad0_  ")
           .append("left outer join ")
           .append("	COTA cota1_  ")
           .append("		on consolidad0_.COTA_ID=cota1_.ID  ")
           .append("left outer join ")
           .append("	CONSOLIDADO_MVTO_FINANCEIRO_COTA movimentos2_  ")
           .append("		on consolidad0_.ID=movimentos2_.CONSOLIDADO_FINANCEIRO_ID  ")
           .append("left outer join ")
           .append("	MOVIMENTO_FINANCEIRO_COTA movimentof3_  ")
           .append("		on movimentos2_.MVTO_FINANCEIRO_COTA_ID=movimentof3_.ID  ")
           .append("left outer join ")
           .append("	MOVIMENTO_ESTOQUE_COTA movimentos4_  ")
           .append("		on movimentof3_.ID=movimentos4_.MOVIMENTO_FINANCEIRO_COTA_ID  ")
           .append("left outer join ")
           .append("	ESTOQUE_PRODUTO_COTA estoquepro6_  ")
           .append("		on movimentos4_.ESTOQUE_PROD_COTA_ID=estoquepro6_.ID  ")
           .append("left outer join ")
           .append("	PRODUTO_EDICAO produtoedi7_  ")
           .append("		on estoquepro6_.PRODUTO_EDICAO_ID=produtoedi7_.ID  ")
           .append("left outer join ")
           .append("	PRODUTO produto8_  ")
           .append("		on produtoedi7_.PRODUTO_ID=produto8_.ID  ")
           .append("left outer join ")
           .append("	PRODUTO_FORNECEDOR fornecedor9_  ")
           .append("		on produto8_.ID=fornecedor9_.PRODUTO_ID  ")
           .append("left outer join ")
           .append("	FORNECEDOR fornecedor10_  ")
           .append("		on fornecedor9_.fornecedores_ID=fornecedor10_.ID  ")
           .append("left outer join ")
           .append("	PESSOA pessoajuri11_  ")
           .append("		on fornecedor10_.JURIDICA_ID=pessoajuri11_.ID  ")
           .append("left outer join ")
           .append("	TIPO_MOVIMENTO tipomovime5_  ")
           .append("		on movimentof3_.TIPO_MOVIMENTO_ID=tipomovime5_.ID  ")
           .append("where ")
           .append("	cota1_.NUMERO_COTA = :numeroCota ")
           .append("	and consolidad0_.DT_CONSOLIDADO = :dataConsolidado ")
           .append("	and tipomovime5_.GRUPO_MOVIMENTO_FINANCEIRO in (:grupoMovimentoFinanceiro) ")
           .append("    and movimentos4_.QTDE != 0 ")
           .append("group by ")
           .append("	produto8_.CODIGO , ")
           .append("	produto8_.NOME , ")
           .append("	produtoedi7_.NUMERO_EDICAO , ")
           .append("	produtoedi7_.PRECO_VENDA , ")
           .append("	pessoajuri11_.RAZAO_SOCIAL ")
           
           .append("union all ")
           
           .append("select ")
           .append("	produto6_.CODIGO as codigoProduto, ")
           .append("	produto6_.NOME as nomeProduto, ")
           .append("	pessoajuri9_.RAZAO_SOCIAL as nomeFornecedor, ")
           .append("	produtoedi5_.NUMERO_EDICAO as numeroEdicao, ")
           .append("	produtoedi5_.PRECO_VENDA as precoCapa, ")
           .append("	movimentos2_.VALOR_DESCONTO as desconto, ")
           .append("	coalesce(movimentos2_.PRECO_COM_DESCONTO,produtoedi5_.PRECO_VENDA) as precoComDesconto, ")
           .append("	sum(movimentos2_.QTDE) * coalesce(movimentos2_.PRECO_COM_DESCONTO,produtoedi5_.PRECO_VENDA) as total,")
           .append("	sum(movimentos2_.QTDE) as encalhe  ")
           .append("from ")
           .append("	MOVIMENTO_FINANCEIRO_COTA movimentof0_  ")
           .append("left outer join ")
           .append("	COTA cota1_  ")
           .append("		on movimentof0_.COTA_ID=cota1_.ID  ")
           .append("left outer join ")
           .append("	MOVIMENTO_ESTOQUE_COTA movimentos2_  ")
           .append("		on movimentof0_.ID=movimentos2_.MOVIMENTO_FINANCEIRO_COTA_ID  ")
           .append("left outer join ")
           .append("	ESTOQUE_PRODUTO_COTA estoquepro4_  ")
           .append("		on movimentos2_.ESTOQUE_PROD_COTA_ID=estoquepro4_.ID  ")
           .append("left outer join ")
           .append("	PRODUTO_EDICAO produtoedi5_  ")
           .append("		on estoquepro4_.PRODUTO_EDICAO_ID=produtoedi5_.ID  ")
           .append("left outer join ")
           .append("	PRODUTO produto6_  ")
           .append("		on produtoedi5_.PRODUTO_ID=produto6_.ID  ")
           .append("left outer join ")
           .append("	PRODUTO_FORNECEDOR fornecedor7_  ")
           .append("		on produto6_.ID=fornecedor7_.PRODUTO_ID  ")
           .append("left outer join ")
           .append("	FORNECEDOR fornecedor8_  ")
           .append("		on fornecedor7_.fornecedores_ID=fornecedor8_.ID  ")
           .append("left outer join ")
           .append("	PESSOA pessoajuri9_  ")
           .append("		on fornecedor8_.JURIDICA_ID=pessoajuri9_.ID  ")
           .append("left outer join ")
           .append("	TIPO_MOVIMENTO tipomovime3_  ")
           .append("		on movimentof0_.TIPO_MOVIMENTO_ID=tipomovime3_.ID  ")
           .append("where ")
           .append("	cota1_.NUMERO_COTA = :numeroCota ")
           .append("	and movimentof0_.DATA = :dataConsolidado ")
           .append("	and tipomovime3_.GRUPO_MOVIMENTO_FINANCEIRO in (:grupoMovimentoFinanceiro) ")
           .append("	and ( ")
           .append("		movimentof0_.ID not in  ( ")
           .append("			select ")
           .append("				movimentof12_.ID  ")
           .append("			from ")
           .append("				CONSOLIDADO_FINANCEIRO_COTA consolidad10_  ")
           .append("			inner join ")
           .append("				CONSOLIDADO_MVTO_FINANCEIRO_COTA movimentos11_  ")
           .append("					on consolidad10_.ID=movimentos11_.CONSOLIDADO_FINANCEIRO_ID  ")
           .append("			inner join ")
           .append("				MOVIMENTO_FINANCEIRO_COTA movimentof12_  ")
           .append("					on movimentos11_.MVTO_FINANCEIRO_COTA_ID=movimentof12_.ID ")
           .append("			) ")
           .append("		)  ")
           .append("    and movimentos2_.QTDE != 0 ")
           .append("group by ")
           .append("	produto6_.CODIGO , ")
           .append("	produto6_.NOME , ")
           .append("	produtoedi5_.NUMERO_EDICAO , ")
           .append("	produtoedi5_.PRECO_VENDA , ")
           .append("	pessoajuri9_.RAZAO_SOCIAL ");

		PaginacaoVO paginacao = filtro.getPaginacao();

		if (filtro.getOrdenacaoColuna() != null) {

			hql.append(" order by ");

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
				orderByColumn = " precoCapa ";
				break;
			case PRECO_COM_DESCONTO:
				orderByColumn = " precoComDesconto ";
				break;
			case ENCALHE:
				orderByColumn = " encalhe ";
				break;
			case FORNECEDOR:
				orderByColumn = " nomeFornecedor ";
				break;
			case TOTAL:
				orderByColumn = " total ";
				break;
			default:
				break;
			}

			hql.append(orderByColumn);

			if (paginacao.getOrdenacao() != null) {

				hql.append(paginacao.getOrdenacao().toString());

			}
		}

		SQLQuery query = getSession().createSQLQuery(hql.toString());
		
		query.addScalar("codigoProduto", StandardBasicTypes.STRING);
		query.addScalar("nomeProduto", StandardBasicTypes.STRING);
		query.addScalar("nomeFornecedor", StandardBasicTypes.STRING);
		query.addScalar("numeroEdicao", StandardBasicTypes.LONG);
		query.addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("desconto", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("precoComDesconto", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("total", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("encalhe", StandardBasicTypes.BIG_INTEGER);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(EncalheCotaDTO.class));
		
		query.setParameter("numeroCota", filtro.getNumeroCota());
		query.setParameter("dataConsolidado", filtro.getDataConsolidado());
		query.setParameterList("grupoMovimentoFinanceiro", 
				Arrays.asList(
					GrupoMovimentoFinaceiro.ENVIO_ENCALHE.toString(), 
					GrupoMovimentoFinaceiro.ESTORNO_REPARTE_COTA_AUSENTE.toString()
				));
		
		if (paginacao != null &&
				paginacao.getQtdResultadosPorPagina() != null &&
				paginacao.getPaginaAtual() != null){
			
			query.setMaxResults(paginacao.getQtdResultadosPorPagina());
			query.setFirstResult(paginacao.getPaginaAtual() - 1 * paginacao.getQtdResultadosPorPagina());
		}
		
		return query.list();
	}

	@SuppressWarnings(value = "unchecked")
	public List<ConsultaVendaEncalheDTO> obterMovimentoVendaEncalhe(
			FiltroConsolidadoVendaCotaDTO filtro) {
		
		StringBuffer hql = new StringBuffer("select ");
		hql.append("	produto9_.CODIGO as codigoProduto, ")
		   .append("	produto9_.NOME as nomeProduto, ")
		   .append("	pessoajuri12_.RAZAO_SOCIAL as nomeFornecedor, ")
		   .append("	produtoedi8_.NUMERO_EDICAO as numeroEdicao, ")
		   .append("	produtoedi8_.PRECO_VENDA as precoCapa, ")
		   .append("	movimentos5_.VALOR_DESCONTO as desconto, ")
		   .append("	produtoedi8_.PRECO_VENDA-(produtoedi8_.PRECO_VENDA*movimentos5_.VALOR_DESCONTO/100) as precoComDesconto, ")
		   .append("	sum(movimentos5_.QTDE*(produtoedi8_.PRECO_VENDA-(produtoedi8_.PRECO_VENDA*movimentos5_.VALOR_DESCONTO/100))) as total, ")
		   .append("	movimentos5_.QTDE as exemplares ") 
		   .append("from ")
		   .append("	CONSOLIDADO_FINANCEIRO_COTA consolidad0_ ") 
		   .append("left outer join ")
		   .append("	COTA cota1_  ")
		   .append("		on consolidad0_.COTA_ID=cota1_.ID ") 
		   .append("left outer join ")
		   .append("	CONSOLIDADO_MVTO_FINANCEIRO_COTA movimentos3_  ")
		   .append("		on consolidad0_.ID=movimentos3_.CONSOLIDADO_FINANCEIRO_ID  ")
		   .append("left outer join ")
		   .append("	MOVIMENTO_FINANCEIRO_COTA movimentof4_  ")
		   .append("		on movimentos3_.MVTO_FINANCEIRO_COTA_ID=movimentof4_.ID  ")
		   .append("left outer join ")
		   .append("	MOVIMENTO_ESTOQUE_COTA movimentos5_  ")
		   .append("		on movimentof4_.ID=movimentos5_.MOVIMENTO_FINANCEIRO_COTA_ID  ")
		   .append("left outer join ")
		   .append("	TIPO_MOVIMENTO tipomovime6_  ")
		   .append("		on movimentos5_.TIPO_MOVIMENTO_ID=tipomovime6_.ID  ")
		   .append("left outer join ")
		   .append("	ESTOQUE_PRODUTO_COTA estoquepro7_ ") 
		   .append("		on movimentos5_.ESTOQUE_PROD_COTA_ID=estoquepro7_.ID  ")
		   .append("left outer join ")
		   .append("	PRODUTO_EDICAO produtoedi8_  ")
		   .append("		on estoquepro7_.PRODUTO_EDICAO_ID=produtoedi8_.ID  ")
		   .append("left outer join ")
		   .append("	PRODUTO produto9_  ")
		   .append("		on produtoedi8_.PRODUTO_ID=produto9_.ID  ")
		   .append("left outer join ")
		   .append("	PRODUTO_FORNECEDOR fornecedor10_  ")
		   .append("		on produto9_.ID=fornecedor10_.PRODUTO_ID  ")
		   .append("left outer join ")
		   .append("	FORNECEDOR fornecedor11_ ")
		   .append("		on fornecedor10_.fornecedores_ID=fornecedor11_.ID  ")
		   .append("left outer join ")
		   .append("	PESSOA pessoajuri12_ ")
		   .append("		on fornecedor11_.JURIDICA_ID=pessoajuri12_.ID  ") 
		   .append("join ")
		   .append("	TIPO_MOVIMENTO tipomovime13_ ") 
		   .append("where ")
		   .append("	movimentof4_.TIPO_MOVIMENTO_ID=tipomovime13_.ID ")
		   .append("	and cota1_.NUMERO_COTA = :numeroCota ")
		   .append("	and consolidad0_.DT_CONSOLIDADO = :dataConsolidado ")
		   .append("	and tipomovime13_.GRUPO_MOVIMENTO_FINANCEIRO = :grupoMovimentoFinanceiro ") 
		   .append("group by ")
		   .append("	produto9_.CODIGO , ")
		   .append("	produto9_.NOME , ")
		   .append("	produtoedi8_.NUMERO_EDICAO , ")
		   .append("	produtoedi8_.PRECO_VENDA , ")
		   .append("	pessoajuri12_.RAZAO_SOCIAL ")
		   
		   .append("union all ")
		   
		   .append("select ")
		   .append("	produto7_.CODIGO as codigoProduto, ")
		   .append("	produto7_.NOME as nomeProduto, ")
		   .append("	pessoajuri10_.RAZAO_SOCIAL as nomeFornecedor, ")
		   .append("	produtoedi6_.NUMERO_EDICAO as numeroEdicao, ")
		   .append("	produtoedi6_.PRECO_VENDA as precoCapa, ")
		   .append("	movimentos3_.VALOR_DESCONTO as desconto, ")
		   .append("	CASE WHEN (produtoedi6_.PRECO_VENDA-(produtoedi6_.PRECO_VENDA*movimentos3_.VALOR_DESCONTO/100)) IS NOT NULL THEN (produtoedi6_.PRECO_VENDA-(produtoedi6_.PRECO_VENDA*movimentos3_.VALOR_DESCONTO/100)) ELSE (vendaProduto.VALOR_TOTAL_VENDA / vendaProduto.QNT_PRODUTO) END as precoComDesconto, ")
		   .append("	sum(CASE WHEN movimentos3_.QTDE*(produtoedi6_.PRECO_VENDA-(produtoedi6_.PRECO_VENDA*movimentos3_.VALOR_DESCONTO/100)) IS NOT NULL THEN movimentos3_.QTDE*(produtoedi6_.PRECO_VENDA-(produtoedi6_.PRECO_VENDA*movimentos3_.VALOR_DESCONTO/100)) ELSE vendaProduto.VALOR_TOTAL_VENDA END) as total, ")
		   .append("	CASE WHEN movimentos3_.QTDE IS NOT NULL THEN movimentos3_.QTDE ELSE vendaProduto.QNT_PRODUTO END as exemplares  ")
		   
		   .append("from ")
		   .append("	MOVIMENTO_FINANCEIRO_COTA movimentof0_  ")
		   
		   .append("left outer join ")
		   .append("	COTA cota1_  ")
		   .append("		on movimentof0_.COTA_ID=cota1_.ID  ")
		   
		   .append("left outer join ")
		   .append("	MOVIMENTO_ESTOQUE_COTA movimentos3_  ")
		   .append("		on movimentof0_.ID=movimentos3_.MOVIMENTO_FINANCEIRO_COTA_ID  ")
		   
		   .append("left outer join ")
		   .append("	VENDA_PRODUTO_MOVIMENTO_FINANCEIRO vendaProdutoMF  ")
		   .append("		on movimentof0_.ID=vendaProdutoMF.ID_MOVIMENTO_FINANCEIRO ")
		   
		   .append("left outer join ")
		   .append("	VENDA_PRODUTO vendaProduto  ")
		   .append("		on vendaProdutoMF.ID_VENDA_PRODUTO=vendaProduto.ID ")
		   
		   .append("left outer join ")
		   .append("	PRODUTO_EDICAO produtoedi6_  ")
		   .append("		on vendaProduto.ID_PRODUTO_EDICAO=produtoedi6_.ID  ")

		   .append("left outer join ")
		   .append("	TIPO_MOVIMENTO tipomovime4_  ")
		   .append("		on movimentos3_.TIPO_MOVIMENTO_ID=tipomovime4_.ID  ")
		   
		   .append("left outer join ")
		   .append("	ESTOQUE_PRODUTO_COTA estoquepro5_  ")
		   .append("		on movimentos3_.ESTOQUE_PROD_COTA_ID=estoquepro5_.ID  ")
		   
		   .append("left outer join ")
		   .append("	PRODUTO produto7_  ")
		   .append("		on produtoedi6_.PRODUTO_ID=produto7_.ID  ")
		   
		   .append("left outer join ")
		   .append("	PRODUTO_FORNECEDOR fornecedor8_  ")
		   .append("		on produto7_.ID=fornecedor8_.PRODUTO_ID  ")
		   
		   .append("left outer join ")
		   .append("	FORNECEDOR fornecedor9_  ")
		   .append("		on fornecedor8_.fornecedores_ID=fornecedor9_.ID  ")
		   
		   .append("left outer join ")
		   .append("	PESSOA pessoajuri10_  ")
		   .append("		on fornecedor9_.JURIDICA_ID=pessoajuri10_.ID ")
		   .append("join ")
		   .append("	TIPO_MOVIMENTO tipomovime11_  ")
		   .append("where ")
		   .append("	movimentof0_.TIPO_MOVIMENTO_ID=tipomovime11_.ID  ")
		   .append("	and cota1_.NUMERO_COTA = :numeroCota ")
		   .append("	and movimentof0_.DATA= :dataConsolidado ")
		   .append("	and tipomovime11_.GRUPO_MOVIMENTO_FINANCEIRO= :grupoMovimentoFinanceiro ")
		   .append("	and ( ")
		   .append("		movimentof0_.ID not in  ( ")
		   .append("			select ")
		   .append("				movimentof14_.ID  ")
		   .append("			from ")
		   .append("				CONSOLIDADO_FINANCEIRO_COTA consolidad12_  ")
		   .append("			inner join ")
		   .append("				CONSOLIDADO_MVTO_FINANCEIRO_COTA movimentos13_  ")
		   .append("					on consolidad12_.ID=movimentos13_.CONSOLIDADO_FINANCEIRO_ID  ")
		   .append("			inner join ")
		   .append("				MOVIMENTO_FINANCEIRO_COTA movimentof14_  ")
		   .append("					on movimentos13_.MVTO_FINANCEIRO_COTA_ID=movimentof14_.ID ")
		   .append("			) ")
		   .append("		)  ")
		   .append("group by ")
		   .append("	produto7_.CODIGO , ")
		   .append("	produto7_.NOME , ")
		   .append("	produtoedi6_.NUMERO_EDICAO , ")
		   .append("	produtoedi6_.PRECO_VENDA , ")
		   .append("	pessoajuri10_.RAZAO_SOCIAL  ");
		
		if (filtro.getOrdenacaoColuna() != null) {
			hql.append(" order by ");			
			hql.append(filtro.getOrdenacaoColuna().toString());			
			
			if (filtro.getPaginacao() != null && filtro.getPaginacao().getOrdenacao() != null) {
				hql.append(" ").append(filtro.getPaginacao().getOrdenacao().toString());
			}
			
		}

		SQLQuery query = getSession().createSQLQuery(hql.toString());
		
		query.addScalar("codigoProduto", StandardBasicTypes.STRING);
		query.addScalar("nomeProduto", StandardBasicTypes.STRING);
		query.addScalar("nomeFornecedor", StandardBasicTypes.STRING);
		query.addScalar("numeroEdicao", StandardBasicTypes.LONG);
		query.addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("desconto", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("precoComDesconto", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("total", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("exemplares", StandardBasicTypes.BIG_INTEGER);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ConsultaVendaEncalheDTO.class));
		
		query.setParameter("numeroCota", filtro.getNumeroCota());
		query.setParameter("dataConsolidado", filtro.getDataConsolidado());
		query.setParameter("grupoMovimentoFinanceiro", GrupoMovimentoFinaceiro.COMPRA_ENCALHE_SUPLEMENTAR.name());
		
		if(filtro.getPaginacao() != null && 
				filtro.getPaginacao().getPosicaoInicial() != null && 
				filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
			
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		}
		
		return query.list();
	}

	/**
	 * Método que obtem os movimentos de Envio ao Jornaleiro (Consignado) para conta corrente da Cota
	 */
	@SuppressWarnings("unchecked")
	public List<ConsignadoCotaDTO> obterMovimentoEstoqueCotaConsignado(FiltroConsolidadoConsignadoCotaDTO filtro){
		
		StringBuffer hql = new StringBuffer("select ");
		
		hql.append("	produto11_.CODIGO as codigoProduto, ")
		   .append("	produto11_.NOME as nomeProduto, ")
		   .append("	pessoajuri14_.RAZAO_SOCIAL as nomeFornecedor, ")
		   .append("	produtoedi8_.NUMERO_EDICAO as numeroEdicao, ")
		   .append("	produtoedi8_.PRECO_VENDA as precoCapa, ")
		   .append("	movimentos4_.VALOR_DESCONTO as desconto, ")
		   .append("	coalesce(movimentos4_.PRECO_COM_DESCONTO,produtoedi8_.PRECO_VENDA) as precoComDesconto, ")
		   .append("	coalesce(estudocota7_.QTDE_PREVISTA,0) as reparteSugerido, ")
		   .append("	coalesce(estudocota7_.QTDE_EFETIVA,0) as reparteFinal, ")
		   .append("	coalesce(estudocota7_.QTDE_PREVISTA-estudocota7_.QTDE_EFETIVA,0) as diferenca, ")
		   .append("	diferenca10_.TIPO_DIFERENCA as motivo, ")
		   .append("	sum(movimentos4_.QTDE) * coalesce(movimentos4_.PRECO_COM_DESCONTO,produtoedi8_.PRECO_VENDA) as total ")
		   .append("from ")
		   .append("	CONSOLIDADO_FINANCEIRO_COTA consolidad0_  ")
		   .append("inner join ")
		   .append("	COTA cota1_  ")
		   .append("		on consolidad0_.COTA_ID=cota1_.ID  ")
		   .append("inner join ")
		   .append("	CONSOLIDADO_MVTO_FINANCEIRO_COTA movimentos2_  ")
		   .append("		on consolidad0_.ID=movimentos2_.CONSOLIDADO_FINANCEIRO_ID  ")
		   .append("inner join ")
		   .append("	MOVIMENTO_FINANCEIRO_COTA movimentof3_  ")
		   .append("		on movimentos2_.MVTO_FINANCEIRO_COTA_ID=movimentof3_.ID  ")
		   .append("inner join ")
		   .append("	MOVIMENTO_ESTOQUE_COTA movimentos4_  ")
		   .append("		on movimentof3_.ID=movimentos4_.MOVIMENTO_FINANCEIRO_COTA_ID  ")
		   .append("inner join ")
		   .append("	ESTOQUE_PRODUTO_COTA estoquepro6_  ")
		   .append("		on movimentos4_.ESTOQUE_PROD_COTA_ID=estoquepro6_.ID  ")
		   .append("inner join ")
		   .append("	PRODUTO_EDICAO produtoedi8_  ")
		   .append("		on estoquepro6_.PRODUTO_EDICAO_ID=produtoedi8_.ID  ")
		   .append("inner join ")
		   .append("	PRODUTO produto11_  ")
		   .append("		on produtoedi8_.PRODUTO_ID=produto11_.ID  ")
		   .append("inner join ")
		   .append("	PRODUTO_FORNECEDOR fornecedor12_  ")
		   .append("		on produto11_.ID=fornecedor12_.PRODUTO_ID  ")
		   .append("inner join ")
		   .append("	FORNECEDOR fornecedor13_  ")
		   .append("		on fornecedor12_.fornecedores_ID=fornecedor13_.ID  ")
		   .append("inner join ")
		   .append("	PESSOA pessoajuri14_  ")
		   .append("		on fornecedor13_.JURIDICA_ID=pessoajuri14_.ID  ")
		   .append("left outer join ")
		   .append("	ESTUDO_COTA estudocota7_  ")
		   .append("		on movimentos4_.ESTUDO_COTA_ID=estudocota7_.ID  ")
		   .append("left outer join ")
		   .append("	RATEIO_DIFERENCA rateiosdif9_  ")
		   .append("		on estudocota7_.ID=rateiosdif9_.ESTUDO_COTA_ID  ")
		   .append("left outer join ")
		   .append("	DIFERENCA diferenca10_  ")
		   .append("		on rateiosdif9_.DIFERENCA_ID=diferenca10_.id  ")
		   .append("inner join ")
		   .append("	TIPO_MOVIMENTO tipomovime5_  ")
		   .append("		on movimentof3_.TIPO_MOVIMENTO_ID=tipomovime5_.ID  ")
		   .append("where ")
		   .append("	cota1_.NUMERO_COTA = :numeroCota ")
		   .append("	and consolidad0_.DT_CONSOLIDADO = :dataConsolidado ")
		   .append("	and tipomovime5_.GRUPO_MOVIMENTO_FINANCEIRO = :grupoMovimentoFinanceiro ")
		   .append("group by ")
		   .append("	produto11_.CODIGO , ")
		   .append("	produto11_.NOME , ")
		   .append("	produtoedi8_.NUMERO_EDICAO , ")
		   .append("	produtoedi8_.PRECO_VENDA , ")
		   .append("	pessoajuri14_.RAZAO_SOCIAL ")
		   
		   .append("union all ")
		   
		   .append("select ")
		   .append("	produto9_.CODIGO as codigoProduto, ")
		   .append("	produto9_.NOME as nomeProduto, ")
		   .append("	pessoajuri12_.RAZAO_SOCIAL as nomeFornecedor, ")
		   .append("	produtoedi6_.NUMERO_EDICAO as numeroEdicao, ")
		   .append("	produtoedi6_.PRECO_VENDA as precoCapa, ")
		   .append("	movimentos2_.VALOR_DESCONTO as desconto, ")
		   .append("	coalesce(movimentos2_.PRECO_COM_DESCONTO,produtoedi6_.PRECO_VENDA) as precoComDesconto, ")
		   .append("	estudocota5_.QTDE_PREVISTA as reparteSugerido, ")
		   .append("	estudocota5_.QTDE_EFETIVA as reparteFinal, ")
		   .append("	estudocota5_.QTDE_PREVISTA-estudocota5_.QTDE_EFETIVA as diferenca, ")
		   .append("	diferenca8_.TIPO_DIFERENCA as motivo, ")
		   .append("	sum(movimentos2_.QTDE) * coalesce(movimentos2_.PRECO_COM_DESCONTO,produtoedi6_.PRECO_VENDA) as total ")
		   .append("from ")
		   .append("	MOVIMENTO_FINANCEIRO_COTA movimentof0_  ")
		   .append("inner join ")
		   .append("	COTA cota1_  ")
		   .append("		on movimentof0_.COTA_ID=cota1_.ID  ")
		   .append("inner join ")
		   .append("	MOVIMENTO_ESTOQUE_COTA movimentos2_  ")
		   .append("		on movimentof0_.ID=movimentos2_.MOVIMENTO_FINANCEIRO_COTA_ID  ")
		   .append("inner join ")
		   .append("	ESTOQUE_PRODUTO_COTA estoquepro4_  ")
		   .append("		on movimentos2_.ESTOQUE_PROD_COTA_ID=estoquepro4_.ID  ")
		   .append("inner join ")
		   .append("	PRODUTO_EDICAO produtoedi6_  ")
		   .append("		on estoquepro4_.PRODUTO_EDICAO_ID=produtoedi6_.ID  ")
		   .append("inner join ")
		   .append("	PRODUTO produto9_  ")
		   .append("		on produtoedi6_.PRODUTO_ID=produto9_.ID  ")
		   .append("inner join ")
		   .append("	PRODUTO_FORNECEDOR fornecedor10_  ")
		   .append("		on produto9_.ID=fornecedor10_.PRODUTO_ID  ")
		   .append("inner join ")
		   .append("	FORNECEDOR fornecedor11_  ")
		   .append("		on fornecedor10_.fornecedores_ID=fornecedor11_.ID  ")
		   .append("inner join ")
		   .append("	PESSOA pessoajuri12_  ")
		   .append("		on fornecedor11_.JURIDICA_ID=pessoajuri12_.ID  ")
		   .append("left outer join ")
		   .append("	ESTUDO_COTA estudocota5_  ")
		   .append("		on movimentos2_.ESTUDO_COTA_ID=estudocota5_.ID  ")
		   .append("left outer join ")
		   .append("	RATEIO_DIFERENCA rateiosdif7_  ")
		   .append("		on estudocota5_.ID=rateiosdif7_.ESTUDO_COTA_ID  ")
		   .append("left outer join ")
		   .append("	DIFERENCA diferenca8_  ")
		   .append("		on rateiosdif7_.DIFERENCA_ID=diferenca8_.id  ")
		   .append("inner join ")
		   .append("	TIPO_MOVIMENTO tipomovime3_  ")
		   .append("		on movimentof0_.TIPO_MOVIMENTO_ID=tipomovime3_.ID  ")
		   .append("where ")
		   .append("	cota1_.NUMERO_COTA = :numeroCota ")
		   .append("	and movimentof0_.DATA = :dataConsolidado ")
		   .append("	and tipomovime3_.GRUPO_MOVIMENTO_FINANCEIRO = :grupoMovimentoFinanceiro ")
		   .append("	and ( ")
		   .append("		movimentof0_.ID not in  ( ")
		   .append("			select ")
		   .append("				movimentof15_.ID  ")
		   .append("			from ")
		   .append("				CONSOLIDADO_FINANCEIRO_COTA consolidad13_  ")
		   .append("			inner join ")
		   .append("				CONSOLIDADO_MVTO_FINANCEIRO_COTA movimentos14_  ")
		   .append("					on consolidad13_.ID=movimentos14_.CONSOLIDADO_FINANCEIRO_ID  ")
		   .append("			inner join ")
		   .append("				MOVIMENTO_FINANCEIRO_COTA movimentof15_  ")
		   .append("					on movimentos14_.MVTO_FINANCEIRO_COTA_ID=movimentof15_.ID ")
		   .append("			) ")
		   .append("		)  ")
		   .append("group by ")
		   .append("	produto9_.CODIGO , ")
		   .append("	produto9_.NOME , ")
		   .append("	produtoedi6_.NUMERO_EDICAO , ")
		   .append("	produtoedi6_.PRECO_VENDA , ")
		   .append("	pessoajuri12_.RAZAO_SOCIAL ");
		
		PaginacaoVO paginacao = filtro.getPaginacao();

		if (filtro.getOrdenacaoColuna() != null) {

			hql.append(" order by ");
			
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
						orderByColumn = " precoCapa ";
						break;
					case PRECO_COM_DESCONTO:
						orderByColumn = " precoComDesconto ";
						break;				
					case REPARTE_SUGERIDO:
						orderByColumn = " reparteSugerido ";
						break;
					case REPARTE_FINAL:
						orderByColumn = " reparteFinal ";
						break;
					case DIFERENCA:
						orderByColumn = " diferenca ";
						break;
					case MOTIVO:
						orderByColumn = " motivo ";
						break;						
					case FORNECEDOR:
						orderByColumn = " nomeFornecedor ";
						break;
					case TOTAL:
						orderByColumn = " total ";
						break;
					default:
						break;
				}
			
			hql.append(orderByColumn);
			
			if (paginacao.getOrdenacao() != null) {
				
				hql.append(paginacao.getOrdenacao().toString());
				
			}			
		}
		
		Session session = getSession();
					
		SQLQuery query = session.createSQLQuery(hql.toString());
		
		query.addScalar("codigoProduto", StandardBasicTypes.STRING);
		query.addScalar("nomeProduto", StandardBasicTypes.STRING);
		query.addScalar("nomeFornecedor", StandardBasicTypes.STRING);
		query.addScalar("numeroEdicao", StandardBasicTypes.LONG);
		query.addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("desconto", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("precoComDesconto", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("reparteSugerido", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("reparteFinal", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("diferenca", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("motivo", StandardBasicTypes.STRING);
		query.addScalar("total", StandardBasicTypes.BIG_DECIMAL);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ConsignadoCotaDTO.class));
		
		query.setParameter("numeroCota", filtro.getNumeroCota());
		query.setParameter("dataConsolidado", filtro.getDataConsolidado());		
		query.setParameter("grupoMovimentoFinanceiro", GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE.toString());
		
		if (paginacao != null &&
				paginacao.getQtdResultadosPorPagina() != null &&
				paginacao.getPaginaAtual() != null){
			
			query.setMaxResults(paginacao.getQtdResultadosPorPagina());
			query.setFirstResult(paginacao.getPaginaAtual() - 1 * paginacao.getQtdResultadosPorPagina());
		}
		
		return query.list();
 	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsolidadoFinanceiroCota> obterConsolidadoPorIdMovimentoFinanceiro(Long idMovimentoFinanceiro) {
		
		StringBuilder hql = new StringBuilder("select c from ConsolidadoFinanceiroCota c join c.movimentos mov ");
		hql.append(" where mov.id = :idMovimentoFinanceiro ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idMovimentoFinanceiro", idMovimentoFinanceiro);
		
		return query.list();
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.ConsolidadoFinanceiroRepository#buscarUltimaBaixaAutomaticaDia(java.util.Date)
	 */
	@Override
	public Date buscarUltimaDividaGeradaDia(Date dataOperacao) {
		Criteria criteria = getSession().createCriteria(ConsolidadoFinanceiroCota.class);
		criteria.add(Restrictions.eq("dataConsolidado", dataOperacao));
		criteria.setProjection(Projections.max("dataConsolidado"));
		return (Date) criteria.uniqueResult();
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.ConsolidadoFinanceiroRepository#buscarDiaUltimaBaixaAutomatica()
	 */
	@Override
	public Date buscarDiaUltimaDividaGerada() {
		Criteria criteria = getSession().createCriteria(ConsolidadoFinanceiroCota.class);
		criteria.setProjection(Projections.max("dataConsolidado"));
		return (Date) criteria.uniqueResult();
	}

	@Override
	public Long obterQuantidadeDividasGeradasData(List<Long> idsCota) {
		
		StringBuilder hql = new StringBuilder("select count(c.id) ");
		hql.append(" from ConsolidadoFinanceiroCota c, Distribuidor d, Divida divida ")
		   .append(" where c.dataConsolidado = d.dataOperacao ")
		   .append(" and c.id = divida.consolidado.id ")
		   .append(" and divida.data = d.dataOperacao ")
		   .append(" and divida.origemNegociacao = false ");
		
		if (idsCota != null) {
			
			hql.append("and c.cota.id in (:idsCota)");
		}
		
		Query query = 
				this.getSession().createQuery(hql.toString());
		
		if (idsCota != null) {
			
			query.setParameterList("idsCota", idsCota);
		}
		
		return (Long) query.uniqueResult();
	}
	
	@Override
	public Long obterQuantidadeDividasGeradasData(Date dataVencimentoDebito, Long... idsCota) {
		
		StringBuilder hql = new StringBuilder("select count(c.id) ");
		hql.append(" from ConsolidadoFinanceiroCota c, Divida divida ")
		   .append(" where c.dataConsolidado = :dataVencimentoDebito ")
		   .append(" and c.id = divida.consolidado.id ")
		   .append(" and divida.data = :dataVencimentoDebito ");
		
		if (idsCota != null) {
			
			hql.append("and c.cota.id in (:idsCota)");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (idsCota != null) {
			
			query.setParameterList("idsCota", idsCota);
		}
		
		query.setParameter("dataVencimentoDebito", dataVencimentoDebito);
		
		return (Long) query.uniqueResult();
	}

	@Override
	public ConsolidadoFinanceiroCota buscarPorCotaEData(Cota cota,
			java.sql.Date data) {
		
		Criteria criteria = getSession().createCriteria(ConsolidadoFinanceiroCota.class);
		
		criteria.add(Restrictions.eq("cota", cota));
		criteria.add(Restrictions.eq("dataConsolidado", data));
		
		criteria.setMaxResults(1);
		
		return (ConsolidadoFinanceiroCota) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsolidadoFinanceiroCota> obterConsolidadosDataOperacao(Long idCota) {
		
		StringBuilder hql = new StringBuilder("select c from ConsolidadoFinanceiroCota c, Distribuidor d ");
		
		if (idCota != null){
			
			hql.append(" join c.cota cota ");
		}
		
		hql.append(" where c.dataConsolidado = d.dataOperacao ");
		
		if (idCota != null){
			
			hql.append(" and cota.id = :idCota ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (idCota != null){
			
			query.setParameter("idCota", idCota);
		}
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ContaCorrenteCotaVO> obterContaCorrente(FiltroViewContaCorrenteCotaDTO filtro,
			List<Long> tiposMovimentoCredito, List<Long> tiposMovimentoDebito,
			List<Long> tipoMovimentoEncalhe, List<Long> tiposMovimentoEncargos,
			List<Long> tiposMovimentoPostergadoCredito, List<Long> tiposMovimentoPostergadoDebito,
			List<Long> tipoMovimentoVendaEncalhe, List<Long> tiposMovimentoConsignado){
		
		StringBuilder sql = new StringBuilder("select ");
		sql.append(" cfc.ID as id, ")
		   .append(" cfc.COTA_ID as cotaId, ")
		   .append(" COTA.NUMERO_COTA as numeroCota, ")
		   .append(" concat(box.codigo, ' - ', box.nome) as nomeBox, ")
		   .append(" cfc.CONSIGNADO as consignado, ")
		   .append(" cfc.DT_CONSOLIDADO as dataConsolidado, ")
		   .append(" cfc.DEBITO_CREDITO as debitoCredito, ")
		   .append(" cfc.ENCALHE as encalhe, ")
		   .append(" cfc.ENCARGOS as encargos, ")
		   .append(" cfc.PENDENTE as pendente, ")
		   .append(" cfc.TOTAL as total, ")
		   .append(" cfc.VALOR_POSTERGADO as valorPostergado, ")
		   .append(" cfc.VENDA_ENCALHE as vendaEncalhe, ")
		   .append(" 'CONSOLIDADO' as tipo, ")
		   .append("(")
		   .append(" select case when divida.DATA is not null then max(divida.DATA) else max(acumulada.DATA) end as dataRaiz ")
		   .append(" from DIVIDA acumulada ")
		   .append(" left join DIVIDA divida ")
		   .append("      ON divida.ID = acumulada.DIVIDA_RAIZ_ID ")
		   .append(" where divida.DATA < (select max(d.DATA) from DIVIDA d where d.COTA_ID = divida.COTA_ID) ")
		   .append(" or acumulada.DATA < (select max(a.DATA) from DIVIDA a where a.COTA_ID = acumulada.COTA_ID) ")
		   .append(" and acumulada.CONSOLIDADO_ID = cfc.ID ")
		   .append(" )AS dataRaiz, ")
		   .append(" coalesce((select sum(bc.VALOR_PAGO) ")
		   .append("           from BAIXA_COBRANCA bc ")
		   .append("           inner join COBRANCA cobranca ")
		   .append("                 ON cobranca.ID = bc.COBRANCA_ID ")
		   .append("           inner join DIVIDA divida ")
		   .append("                 on divida.ID = cobranca.DIVIDA_ID ")
		   .append("           where bc.STATUS not in (:statusBaixaCobranca) ")
		   .append("           and cota.ID = cobranca.COTA_ID ")
		   .append("           and divida.CONSOLIDADO_ID = cfc.ID ")
		   .append("           and cfc.ID),0) as valorPago, ")
		   .append(" total - coalesce((select sum(bc.VALOR_PAGO) ")
		   .append("           from BAIXA_COBRANCA bc ")
		   .append("           inner join COBRANCA cobranca ")
		   .append("                 on cobranca.ID = bc.COBRANCA_ID ")
		   .append("           inner join DIVIDA divida ")
		   .append("                 on divida.ID = cobranca.DIVIDA_ID ")
		   .append("           where bc.STATUS not in (:statusBaixaCobranca) ")
		   .append("           and cota.ID = cobranca.COTA_ID ")
		   .append("           and divida.CONSOLIDADO_ID = cfc.ID ")
		   .append("           and cfc.ID),0) as saldo, ")
		   .append(" coalesce(cfc.CONSIGNADO,0) - coalesce(cfc.ENCALHE,0) as valorVendaDia ")
		   .append(" from CONSOLIDADO_FINANCEIRO_COTA cfc ")
		   .append(" inner join COTA cota on cota.ID = cfc.COTA_ID")
		   .append(" inner join BOX box on cota.BOX_ID = box.ID ")
		   .append(" where cota.NUMERO_COTA = :numeroCota ");
		
		if (filtro.getInicioPeriodo() != null && filtro.getFimPeriodo() != null){
			
			sql.append(" and cfc.DT_CONSOLIDADO between :inicioPeriodo and :fimPeriodo ");
		}
		
		sql.append(" union all ")
		   
		   .append(" select ")
		   .append(" null as id, ")
		   .append(" mfc.COTA_ID as cotaId, ")
		   .append(" null as numeroCota, ")
		   .append(" concat(box.codigo, ' - ', box.nome) as nomeBox, ")
		   
		   //consignado
		   .append("coalesce((select sum(m.VALOR) ")
		   .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
		   .append(" inner join COTA on COTA.ID = m.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoConsignado) ")
		   .append(" and m.ID not in (")
		   .append("     select MVTO_FINANCEIRO_COTA_ID ")
		   .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
		   .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
		   .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
		   .append(") and m.DATA = mfc.DATA ")
		   .append("),0) as consignado, ")
		   
		   .append(" mfc.DATA as dataConsolidado, ")
		   
		   //crédito
		   .append("(coalesce((select sum(m.VALOR) ")
		   .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
		   .append(" inner join COTA on COTA.ID = m.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoCredito) ")
		   .append(" and m.ID not in (")
		   .append("     select MVTO_FINANCEIRO_COTA_ID ")
		   .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
		   .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
		   .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
		   .append(") and m.DATA = mfc.DATA ")
		   .append("),0) - ")
		   //débito
		   .append(" coalesce((select sum(m.VALOR) ")
		   .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
		   .append(" inner join COTA on COTA.ID = m.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoDebito) ")
		   .append(" and m.ID not in (")
		   .append("     select MVTO_FINANCEIRO_COTA_ID ")
		   .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
		   .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
		   .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
		   .append(") and m.DATA = mfc.DATA ")
		   .append("),0)")
		   .append(") as debitoCredito, ")
		   
		   //encalhe
		   .append("coalesce((select sum(m.VALOR) ")
		   .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
		   .append(" inner join COTA on COTA.ID = m.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and m.TIPO_MOVIMENTO_ID in (:tipoMovimentoEncalhe) ")
		   .append(" and m.ID not in (")
		   .append("     select MVTO_FINANCEIRO_COTA_ID ")
		   .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
		   .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
		   .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
		   .append(") and m.DATA = mfc.DATA ")
		   .append("),0) as encalhe, ")
		   
		   //encargos
		   .append("coalesce((select sum(m.VALOR) ")
		   .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
		   .append(" inner join COTA on COTA.ID = m.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoEncargos) ")
		   .append(" and m.ID not in (")
		   .append("     select MVTO_FINANCEIRO_COTA_ID ")
		   .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
		   .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
		   .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
		   .append(") and m.DATA = mfc.DATA ")
		   .append("),0) as encargos, ")
		   
		   //pendente
		   .append(" 0 as pendente, ")
		   
		   //total
		   .append("coalesce((select sum(m.VALOR) ")
		   .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
		   .append(" inner join COTA on COTA.ID = m.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and m.ID not in (")
		   .append("     select MVTO_FINANCEIRO_COTA_ID ")
		   .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
		   .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
		   .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
		   .append(") and m.DATA = mfc.DATA ")
		   .append("),0) as total, ")
		   
		   //valorPostergado
		   //valorPostergado credito
		   .append("(coalesce((select sum(m.VALOR) ")
		   .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
		   .append(" inner join COTA on COTA.ID = m.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoPostergadoCredito) ")
		   .append(" and m.ID not in (")
		   .append("     select MVTO_FINANCEIRO_COTA_ID ")
		   .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
		   .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
		   .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
		   .append(") and m.DATA = mfc.DATA ")
		   .append("),0) - ")
		   //valorPostergado débito
		   .append(" coalesce((select sum(m.VALOR) ")
		   .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
		   .append(" inner join COTA on COTA.ID = m.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoPostergadoDebito) ")
		   .append(" and m.ID not in (")
		   .append("     select MVTO_FINANCEIRO_COTA_ID ")
		   .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
		   .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
		   .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
		   .append(") and m.DATA = mfc.DATA ")
		   .append("),0)")
		   .append(") as valorPostergado, ")
		   
		   //vendaEncalhe
		   .append("coalesce((select sum(m.VALOR) ")
		   .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
		   .append(" inner join COTA on COTA.ID = m.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and m.TIPO_MOVIMENTO_ID in (:tipoMovimentoVendaEncalhe) ")
		   .append(" and m.ID not in (")
		   .append("     select MVTO_FINANCEIRO_COTA_ID ")
		   .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
		   .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
		   .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
		   .append(") and m.DATA = mfc.DATA ")
		   .append("),0) as vendaEncalhe, ")
		   
		   //tipo
		   .append(" 'MOVIMENTO FINAN' as tipo, ")
		   
		   //data raiz
		   .append(" mfc.DATA as dataRaiz, ")
		   
		   //valor pago
		   .append(" 0 as valorPago, ")
		   
		   //saldo
		   .append("coalesce((select sum(m.VALOR) ")
		   .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
		   .append(" inner join COTA on COTA.ID = m.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and m.ID not in (")
		   .append("     select MVTO_FINANCEIRO_COTA_ID ")
		   .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
		   .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
		   .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
		   .append(") and m.DATA = mfc.DATA ")
		   .append("),0) as saldo, ")
		   
		   //valorVendaDia (consignado - encalhe)
		   .append("coalesce((select sum(m.VALOR) ")
		   .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
		   .append(" inner join COTA on COTA.ID = m.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and m.TIPO_MOVIMENTO_ID in (:tiposMovimentoConsignado) ")
		   .append(" and m.ID not in (")
		   .append("     select MVTO_FINANCEIRO_COTA_ID ")
		   .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
		   .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
		   .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
		   .append(") and m.DATA = mfc.DATA ")
		   .append("),0) ")
		   .append(" - ")
		   .append("coalesce((select sum(m.VALOR) ")
		   .append(" from MOVIMENTO_FINANCEIRO_COTA m ")
		   .append(" inner join COTA on COTA.ID = m.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and m.TIPO_MOVIMENTO_ID in (:tipoMovimentoEncalhe) ")
		   .append(" and m.ID not in (")
		   .append("     select MVTO_FINANCEIRO_COTA_ID ")
		   .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
		   .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
		   .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
		   .append(") and m.DATA = mfc.DATA ")
		   .append("),0) as valorVendaDia ")
		   
		   .append(" from MOVIMENTO_FINANCEIRO_COTA mfc ")
		   .append(" inner join COTA on COTA.ID = mfc.COTA_ID")
		   .append(" inner join BOX box on cota.BOX_ID = box.ID ")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and mfc.ID not in (")
		   .append("     select MVTO_FINANCEIRO_COTA_ID ")
		   .append("     from CONSOLIDADO_MVTO_FINANCEIRO_COTA CCC ")
		   .append("     inner join CONSOLIDADO_FINANCEIRO_COTA CON on CON.ID = CCC.CONSOLIDADO_FINANCEIRO_ID ")
		   .append("     inner join COTA on COTA.ID = CON.COTA_ID ")
		   .append(")");
		
		if (filtro.getInicioPeriodo() != null && filtro.getFimPeriodo() != null){
			
			sql.append(" and mfc.DATA between :inicioPeriodo and :fimPeriodo ");
		}
		
		sql.append(" group by mfc.DATA ");
		
		if (filtro.getColunaOrdenacao() != null) {
			sql.append(" order by ").append(filtro.getColunaOrdenacao()).append(" ");
			
			if (filtro.getPaginacao() != null) {
				
				sql.append(filtro.getPaginacao().getOrdenacao());
			}else{
				
				sql.append("asc");
			}
		}
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.addScalar("id", StandardBasicTypes.LONG);
		query.addScalar("cotaId", StandardBasicTypes.LONG);
		query.addScalar("numeroCota", StandardBasicTypes.INTEGER);
		query.addScalar("nomeBox", StandardBasicTypes.STRING);
		query.addScalar("consignado", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("dataConsolidado", StandardBasicTypes.DATE);
		query.addScalar("debitoCredito", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("encalhe", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("encargos", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("pendente", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("total", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("valorPostergado", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("vendaEncalhe", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("tipo", StandardBasicTypes.STRING);
		query.addScalar("dataRaiz", StandardBasicTypes.DATE);
		query.addScalar("valorPago", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("saldo", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("valorVendaDia", StandardBasicTypes.BIG_DECIMAL);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ContaCorrenteCotaVO.class));
		
		query.setParameter("numeroCota", filtro.getNumeroCota());
		
		List<StatusBaixa> statusBaixaCobranca = 
				Arrays.asList(StatusBaixa.NAO_PAGO_BAIXA_JA_REALIZADA,
						StatusBaixa.NAO_PAGO_DIVERGENCIA_DATA,
						StatusBaixa.NAO_PAGO_DIVERGENCIA_VALOR,
						StatusBaixa.NAO_PAGO_POSTERGADO);
		
		query.setParameterList("statusBaixaCobranca", statusBaixaCobranca);
		
		if(filtro.getInicioPeriodo()!= null && filtro.getFimPeriodo()!= null){
			
			query.setParameter("inicioPeriodo", filtro.getInicioPeriodo());
			query.setParameter("fimPeriodo", filtro.getFimPeriodo());
		}
		
		query.setParameterList("tiposMovimentoCredito", tiposMovimentoCredito);
		query.setParameterList("tiposMovimentoDebito", tiposMovimentoDebito);
		query.setParameterList("tipoMovimentoEncalhe", tipoMovimentoEncalhe);
		query.setParameterList("tiposMovimentoEncargos", tiposMovimentoEncargos);
		query.setParameterList("tiposMovimentoPostergadoCredito", tiposMovimentoPostergadoCredito);
		query.setParameterList("tiposMovimentoPostergadoDebito", tiposMovimentoPostergadoDebito);
		query.setParameterList("tipoMovimentoVendaEncalhe", tipoMovimentoVendaEncalhe);
		query.setParameterList("tiposMovimentoConsignado", tiposMovimentoConsignado);
		
		PaginacaoVO paginacao = filtro.getPaginacao();
		if (paginacao != null) {
			
			if (paginacao.getPosicaoInicial() != null) {
				
				query.setFirstResult(paginacao.getPosicaoInicial());
			}
			
			if (paginacao.getQtdResultadosPorPagina() != null) {
				
				query.setMaxResults(paginacao.getQtdResultadosPorPagina());
			}
		}
		
		return query.list();
	}
	
	@Override
	public BigInteger countObterContaCorrente(FiltroViewContaCorrenteCotaDTO filtro){
		
		StringBuilder sql = new StringBuilder("select count(cotaId) from (");
		sql.append(" select cfc.COTA_ID as cotaId, ")
		   .append(" cfc.DT_CONSOLIDADO as dataConsolidado ")
		   .append(" from CONSOLIDADO_FINANCEIRO_COTA cfc ")
		   .append(" inner join COTA on COTA.ID = cfc.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ");
		
		if (filtro.getInicioPeriodo() != null && filtro.getFimPeriodo() != null){
			
			sql.append(" and cfc.DT_CONSOLIDADO between :inicioPeriodo and :fimPeriodo ");
		}
		
		sql.append(" union all ")
		   
		   .append(" select mfc.COTA_ID as cotaId, ")
		   .append(" mfc.DATA as dataConsolidado ")
		   .append(" from MOVIMENTO_FINANCEIRO_COTA mfc ")
		   .append(" inner join COTA on COTA.ID = mfc.COTA_ID")
		   .append(" where COTA.NUMERO_COTA = :numeroCota ")
		   .append(" and mfc.DATA not in (")
		   .append("     select DT_CONSOLIDADO ")
		   .append("     from CONSOLIDADO_FINANCEIRO_COTA ")
		   .append("     inner join COTA on COTA.ID = CONSOLIDADO_FINANCEIRO_COTA.COTA_ID")
		   .append(")");
		
		if (filtro.getInicioPeriodo() != null && filtro.getFimPeriodo() != null){
			
			sql.append(" and mfc.DATA between :inicioPeriodo and :fimPeriodo ");
		}
		
		sql.append(" group by dataConsolidado ")
		   .append(") as tmp ");
		
		Query query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("numeroCota", filtro.getNumeroCota());
		
		if(filtro.getInicioPeriodo()!= null && filtro.getFimPeriodo()!= null){
			
			query.setParameter("inicioPeriodo", filtro.getInicioPeriodo());
			query.setParameter("fimPeriodo", filtro.getFimPeriodo());
		}
		
		return (BigInteger) query.uniqueResult();
	}
}