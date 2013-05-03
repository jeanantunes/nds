package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.AbastecimentoDTO;
import br.com.abril.nds.dto.ConsultaEncalheDTO;
import br.com.abril.nds.dto.ConsultaEncalheDetalheDTO;
import br.com.abril.nds.dto.ConsultaEncalheRodapeDTO;
import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.dto.MovimentoEstoqueCotaDTO;
import br.com.abril.nds.dto.MovimentoEstoqueCotaGenericoDTO;
import br.com.abril.nds.dto.ProdutoAbastecimentoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDetalheDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO.ColunaOrdenacao;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO.ColunaOrdenacaoDetalhes;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO.ColunaOrdenacaoEntregador;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ParametrosRecolhimentoDistribuidor;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.StatusEstoqueFinanceiro;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.model.estoque.ValoresAplicados;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.Status;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamentoInterno;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class MovimentoEstoqueCotaRepositoryImpl extends AbstractRepositoryModel<MovimentoEstoqueCota, Long> implements MovimentoEstoqueCotaRepository {

	public MovimentoEstoqueCotaRepositoryImpl() {
		super(MovimentoEstoqueCota.class);
	}
	
	@Autowired
	private DataSource dataSource;

	@SuppressWarnings("unchecked")
	public List<MovimentoEstoqueCotaGenericoDTO> obterListaMovimentoEstoqueCotaDevolucaoJuramentada(Date dataOperacao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select   ");			

		hql.append(" produtoEdicao.id as idProdutoEdicao,	");
		hql.append(" cota.id as idCota,						");
		hql.append(" sum(mec.qtde) as qtde					");
		
		hql.append(" from ConferenciaEncalhe conferenciaEncalhe	");	
		
		hql.append(" inner join conferenciaEncalhe.movimentoEstoqueCota mec		");
		hql.append(" inner join mec.cota as cota 								");
		hql.append(" inner join mec.produtoEdicao as produtoEdicao 				");
		hql.append(" inner join conferenciaEncalhe.controleConferenciaEncalheCota 	");
		hql.append(" controlConfEncalheCota");
		
		hql.append(" where ");	
		
		hql.append(" controlConfEncalheCota.dataOperacao = :dataOperacao and 	");
		hql.append(" conferenciaEncalhe.juramentada = :juramentada  			");

		hql.append(" group by ");
		
		hql.append(" produtoEdicao.id,	");
		hql.append(" cota.id			");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(MovimentoEstoqueCotaGenericoDTO.class));
		
		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("juramentada", true);
		
		
		
		return query.list();
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.MovimentoEstoqueCotaRepository#obterListaMovimentoEstoqueCotaParaOperacaoConferenciaEncalhe(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<MovimentoEstoqueCota> obterListaMovimentoEstoqueCotaParaOperacaoConferenciaEncalhe(Long idControleConferenciaEncalheCota) {
		
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select movimentoEstoqueCota  ");			
		
		hql.append(" from ConferenciaEncalhe conferenciaEncalhe ");
		
		hql.append(" join conferenciaEncalhe.movimentoEstoqueCota movimentoEstoqueCota ");
		
		hql.append(" WHERE conferenciaEncalhe.controleConferenciaEncalheCota.id = :idControleConferenciaEncalheCota ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idControleConferenciaEncalheCota", idControleConferenciaEncalheCota);
		
		
		return query.list();
		
	}
	
	/**
	 * Obtém movimentos de estoque da cota que ainda não geraram movimento financeiro
	 * Considera movimentos de estoque provenientes dos fluxos de Expedição e Conferência de Encalhe
	 * @param idCota
	 * @return List<MovimentoEstoqueCota>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MovimentoEstoqueCota> obterMovimentosPendentesGerarFinanceiro(Long idCota, Date dataControleConferencia) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select mec ");
		
		hql.append(" from MovimentoEstoqueCota mec ");
		
		hql.append(" join mec.tipoMovimento tipoMovimento ");
		
		hql.append(" join mec.cota cota ");
		
		hql.append(" join mec.produtoEdicao produtoEdicao ");
		
		hql.append(" where tipoMovimento.grupoMovimentoEstoque in (:gruposMovimento) ");
		
		hql.append(" and ((mec.statusEstoqueFinanceiro is null) or (mec.statusEstoqueFinanceiro = :statusFinanceiro )) ");
		
		hql.append(" and mec.status = :statusAprovacao ");
		
		hql.append(" and cota.id = :idCota ");
		
		hql.append(" and produtoEdicao.id in (");
		
		hql.append("     select pe.id ");
		
		hql.append("     from ChamadaEncalhe c ");
		
		hql.append("     join c.chamadaEncalheCotas cc ");
		
		hql.append("     join cc.cota cota ");
		
		hql.append("     join c.produtoEdicao pe ");
		
		hql.append("     where c.dataRecolhimento = :dataControleConferencia ");
		
		hql.append("     and cota.id = :idCota ");
		
		hql.append(")");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameterList("gruposMovimento", Arrays.asList(GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR, 
				                                                GrupoMovimentoEstoque.COMPRA_ENCALHE, 
				                                                GrupoMovimentoEstoque.RECEBIMENTO_REPARTE));
		
		query.setParameter("statusFinanceiro", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO);
		query.setParameter("statusAprovacao", StatusAprovacao.APROVADO);
		query.setParameter("idCota", idCota);
		query.setParameter("dataControleConferencia", dataControleConferencia);
		
		return query.list();
		
	}
	
	/**
	 * Obtém movimentos de estoque da cota que forão estornados
	 * Considera movimentos de estoque provenientes dos fluxos de Venda de Encalhe e Suplementar
	 * @param idCota
	 * @return List<MovimentoEstoqueCota>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MovimentoEstoqueCota> obterMovimentosEstornados(Long idCota) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select mec  ");			
		
		hql.append(" from MovimentoEstoqueCota mec ");
		
		hql.append(" join mec.tipoMovimento tipoMovimento ");
		
		hql.append(" join mec.cota cota ");
		
		hql.append(" where tipoMovimento.grupoMovimentoEstoque in (:gruposMovimento) ");

		hql.append(" and cota.id = :idCota ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameterList("gruposMovimento", Arrays.asList(GrupoMovimentoEstoque.ESTORNO_COMPRA_ENCALHE, 
				                                                GrupoMovimentoEstoque.ESTORNO_COMPRA_SUPLEMENTAR));
		
		query.setParameter("idCota", idCota);
		
		return query.list();
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.MovimentoEstoqueCotaRepository#obterQtdeMovimentoEstoqueCotaParaProdutoEdicaoNoPeriodo(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date, br.com.abril.nds.model.estoque.OperacaoEstoque)
	 */
	public BigInteger obterQtdeMovimentoEstoqueCotaParaProdutoEdicaoNoPeriodo(
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
		
		return (BigInteger) query.uniqueResult();
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.MovimentoEstoqueCotaRepository#obterQtdeMovimentoEstoqueCotaParaProdutoEdicaoNoPeriodo(java.lang.Long, java.lang.Long, java.util.Date, java.util.Date, br.com.abril.nds.model.estoque.OperacaoEstoque)
	 */
	@Override
	public BigDecimal obterValorTotalMovimentoEstoqueCotaParaProdutoEdicaoNoPeriodo(
			Long idCota,
			Long idFornecedor,
			Long idProdutoEdicao,
			Date dataInicial, 
			Date dataFinal,
			OperacaoEstoque operacaoEstoque) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select sum(movimentoEstoqueCota.qtde * produtoEdicao.precoVenda) ");			
		
		hql.append(" from MovimentoEstoqueCota movimentoEstoqueCota ");
		
		hql.append(" join movimentoEstoqueCota.produtoEdicao produtoEdicao  ");
		
		hql.append(" join produtoEdicao.produto produto  ");
		
		hql.append(" join produto.fornecedores fornecedor  ");
		
		hql.append(" where movimentoEstoqueCota.data between :dataInicial and :dataFinal and ");

		hql.append(" movimentoEstoqueCota.cota.id = :idCota and ");
		
		hql.append(" fornecedor.id = :idFornecedor and ");
		
		hql.append(" movimentoEstoqueCota.produtoEdicao.id = :idProdutoEdicao and ");
		
		hql.append(" movimentoEstoqueCota.tipoMovimento.operacaoEstoque = :operacaoEstoque ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataInicial", dataInicial);
		query.setParameter("dataFinal", dataFinal);
		query.setParameter("idCota", idCota);
		query.setParameter("idFornecedor", idFornecedor);
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setParameter("operacaoEstoque", operacaoEstoque);
		
		return (BigDecimal) query.uniqueResult();
		
	}
	
	public Integer obterQtdeConsultaEncalhe(FiltroConsultaEncalheDTO filtro) {

		
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("	SELECT COUNT(1) FROM ( ");
		
		sql.append("	SELECT	");

		sql.append("	CHAMADA_ENCALHE.DATA_RECOLHIMENTO, 		");
		sql.append("	PRODUTO.ID as idProduto, 				");
		sql.append("	PRODUTO_EDICAO.ID as idProdutoEdicao,	");

		sql.append("	COALESCE(MEC_REPARTE.PRECO_VENDA, PRODUTO_EDICAO.PRECO_VENDA, 0)	as precoVenda, ");
		sql.append("    COALESCE(MEC_REPARTE.PRECO_COM_DESCONTO, PRODUTO_EDICAO.PRECO_VENDA, 0) as precoComDesconto, ");
		sql.append("	COALESCE(MEC_REPARTE.VALOR_DESCONTO, 0) ").append(" as valorDesconto, ");
		
		sql.append("	( SUM( COALESCE(MOVIMENTO_ESTOQUE_COTA.QTDE, 0)) * COALESCE(MEC_REPARTE.PRECO_VENDA, PRODUTO_EDICAO.PRECO_VENDA, 0) ) as valor, ");
		sql.append("	( SUM( COALESCE(MOVIMENTO_ESTOQUE_COTA.QTDE, 0)) * COALESCE(MEC_REPARTE.PRECO_COM_DESCONTO , PRODUTO_EDICAO.PRECO_VENDA, 0) ) as valorComDesconto, ");
		
		sql.append("	FORNECEDOR.ID as idFornecedor,			");
		sql.append("	CHAMADA_ENCALHE_COTA.COTA_ID as idCota, ");
		sql.append(" 	PESSOA.ID idPessoa,  					");
		
		sql.append("	CONTROLE_CONF_ENC_COTA.DATA_OPERACAO ");
		
		
		sql.append(getFromWhereConsultaEncalhe(filtro));
		
		sql.append(" )	as encalhes ");
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		
		if(filtro.getIdCota()!=null) {
			parameters.put("idCota", filtro.getIdCota());
		}
		
		if(filtro.getIdFornecedor() != null) {
			parameters.put("idFornecedor", filtro.getIdFornecedor());
		}
		
		parameters.put("grupoMovimentoEstoque", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name());
		parameters.put("dataRecolhimentoInicial", filtro.getDataRecolhimentoInicial());
		parameters.put("dataRecolhimentoFinal", filtro.getDataRecolhimentoFinal());
		parameters.put("isPostergado", false);
		
		Integer qtde = namedParameterJdbcTemplate.queryForInt(sql.toString(), parameters);
		
		qtde = (qtde == null) ? 0 : qtde;
		
		return qtde;
	}
	
	public BigDecimal obterValorTotalEncalhe(FiltroConsultaEncalheDTO filtro) {

		StringBuffer sql = new StringBuffer();
		
		sql.append("	SELECT	");

		sql.append("	SUM( MOVIMENTO_ESTOQUE_COTA.QTDE * COALESCE(MOVIMENTO_ESTOQUE_COTA.PRECO_COM_DESCONTO, PRODUTO_EDICAO.PRECO_VENDA, 0) ) as totalEncalhe ");
		
		sql.append("	from	");
		
		sql.append("	CONTROLE_CONFERENCIA_ENCALHE_COTA CONTROLE_CONF_ENC_COTA, MOVIMENTO_ESTOQUE_COTA inner join CONFERENCIA_ENCALHE on ");
		sql.append("	( CONFERENCIA_ENCALHE.MOVIMENTO_ESTOQUE_COTA_ID = MOVIMENTO_ESTOQUE_COTA.ID	) ");

		sql.append("	inner join ESTOQUE_PRODUTO_COTA on                                         ");
		sql.append("	( MOVIMENTO_ESTOQUE_COTA.ESTOQUE_PROD_COTA_ID = ESTOQUE_PRODUTO_COTA.ID )  ");
		
		sql.append("	inner join COTA on                                         ");
		sql.append("	( MOVIMENTO_ESTOQUE_COTA.COTA_ID = COTA.ID )  ");
		
		
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
		
		sql.append("	CONTROLE_CONF_ENC_COTA.ID = CONFERENCIA_ENCALHE.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID ");
		
		sql.append("	AND (CHAMADA_ENCALHE.DATA_RECOLHIMENTO BETWEEN :dataRecolhimentoInicial AND :dataRecolhimentoFinal) ");
		
		sql.append("	AND CHAMADA_ENCALHE_COTA.FECHADO = :isPostergado ");
		
		if(filtro.getIdCota()!=null) {
			sql.append(" and MOVIMENTO_ESTOQUE_COTA.COTA_ID = :idCota  ");
		}
		
		if(filtro.getIdFornecedor() != null) {
			sql.append(" and FORNECEDOR.ID =  :idFornecedor ");
		}

		Map<String, Object> parameters = new HashMap<String, Object>();
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		
		if(filtro.getIdCota()!=null) {
			parameters.put("idCota", filtro.getIdCota());
		}
		
		if(filtro.getIdFornecedor() != null) {
			parameters.put("idFornecedor", filtro.getIdFornecedor());
		}
		
		parameters.put("dataRecolhimentoInicial", filtro.getDataRecolhimentoInicial());
		parameters.put("dataRecolhimentoFinal", filtro.getDataRecolhimentoFinal());
		parameters.put("isPostergado", false);
		
		Map<String, Object> queryForMap = namedParameterJdbcTemplate.queryForMap(sql.toString(), parameters);
		Object totalEncalhe = queryForMap.get("totalEncalhe");

		return (BigDecimal) (totalEncalhe == null ? BigDecimal.ZERO : totalEncalhe);
	}
	
	public StringBuffer getFromWhereConsultaEncalhe(FiltroConsultaEncalheDTO filtro) {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("	from	");

		sql.append("	CHAMADA_ENCALHE  ");
		
		sql.append("	inner join CHAMADA_ENCALHE_COTA on ");
		sql.append("	( CHAMADA_ENCALHE.ID = CHAMADA_ENCALHE_COTA.CHAMADA_ENCALHE_ID ) ");
 		
		sql.append("	inner join PRODUTO_EDICAO on ");
		sql.append("	( PRODUTO_EDICAO.ID = CHAMADA_ENCALHE.PRODUTO_EDICAO_ID ) ");
		
		sql.append("	inner join PRODUTO on ");
		sql.append("	( PRODUTO_EDICAO.PRODUTO_ID = PRODUTO.ID ) ");
		
		sql.append("	inner join PRODUTO_FORNECEDOR on ");
		sql.append("	( PRODUTO_FORNECEDOR.PRODUTO_ID = PRODUTO.ID ) ");
		
		sql.append("	inner join FORNECEDOR on ");
		sql.append("	( PRODUTO_FORNECEDOR.FORNECEDORES_ID = FORNECEDOR.ID ) ");
		
		sql.append("	inner join PESSOA on                   	");
		sql.append("	( PESSOA.ID = FORNECEDOR.JURIDICA_ID )	");
		
		sql.append("	left join CONTROLE_CONFERENCIA_ENCALHE_COTA CONTROLE_CONF_ENC_COTA on ");
		sql.append("	( CONTROLE_CONF_ENC_COTA.DATA_OPERACAO = CHAMADA_ENCALHE.DATA_RECOLHIMENTO 	");
		sql.append("	AND  CONTROLE_CONF_ENC_COTA.COTA_ID = CHAMADA_ENCALHE_COTA.COTA_ID ) ");
		
		sql.append("	left join CONFERENCIA_ENCALHE on ");
		sql.append("	( CONFERENCIA_ENCALHE.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID = CONTROLE_CONF_ENC_COTA.ID AND ");
		sql.append("	CONFERENCIA_ENCALHE.PRODUTO_EDICAO_ID = CHAMADA_ENCALHE.PRODUTO_EDICAO_ID   )");
		
		sql.append("	left join MOVIMENTO_ESTOQUE_COTA on ");
		sql.append("	( CONFERENCIA_ENCALHE.MOVIMENTO_ESTOQUE_COTA_ID = MOVIMENTO_ESTOQUE_COTA.ID	) ");
		
		sql.append("	inner join MOVIMENTO_ESTOQUE_COTA MEC_REPARTE on ");
		sql.append("	( MEC_REPARTE.COTA_ID = CHAMADA_ENCALHE_COTA.COTA_ID AND ");
		sql.append("  	  MEC_REPARTE.PRODUTO_EDICAO_ID = CHAMADA_ENCALHE.PRODUTO_EDICAO_ID )");
		
		sql.append("	inner join TIPO_MOVIMENTO on ( ");
		sql.append("	MEC_REPARTE.TIPO_MOVIMENTO_ID = TIPO_MOVIMENTO.ID AND 	");
		sql.append("	TIPO_MOVIMENTO.GRUPO_MOVIMENTO_ESTOQUE = :grupoMovimentoEstoque ) ");
		
		sql.append("	where	");
		
		sql.append("	(CHAMADA_ENCALHE.DATA_RECOLHIMENTO BETWEEN :dataRecolhimentoInicial AND :dataRecolhimentoFinal) ");
		
		sql.append("	AND CHAMADA_ENCALHE_COTA.POSTERGADO = :isPostergado ");

		
		if(filtro.getIdCota()!=null) {
			sql.append(" and CHAMADA_ENCALHE_COTA.COTA_ID = :idCota  ");
		}
		
		if(filtro.getIdFornecedor() != null) {
			sql.append(" and FORNECEDOR.ID =  :idFornecedor ");
		}

		sql.append("	group by	");
	
		sql.append("    CHAMADA_ENCALHE.DATA_RECOLHIMENTO,  	");
		sql.append("	PRODUTO.ID,                     		");
		sql.append("	PRODUTO_EDICAO.ID,                  	");
		sql.append(" 	precoVenda,								");
		sql.append(" 	precoComDesconto,						");
		sql.append("	valorDesconto, 							");
		sql.append("	FORNECEDOR.ID, 							");
		sql.append("	PESSOA.ID, 								");
		sql.append("    CONTROLE_CONF_ENC_COTA.DATA_OPERACAO	");
		

		return sql;
		
	}
	
	private StringBuffer getSqlConferenciaEncalheComObservacao() {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" SELECT COUNT(1)	");
		
		hql.append(" FROM CONFERENCIA_ENCALHE CONFERENCIA_ENCALHE_0 ");
		
		hql.append(" INNER JOIN MOVIMENTO_ESTOQUE_COTA  MOVIMENTO_ESTOQUE_COTA_0 ON	");
		hql.append(" ( MOVIMENTO_ESTOQUE_COTA_0.ID = CONFERENCIA_ENCALHE_0.MOVIMENTO_ESTOQUE_COTA_ID ) ");
		
		hql.append(" INNER JOIN CHAMADA_ENCALHE_COTA CHAMADA_ENCALHE_COTA_0 ON		");
		hql.append(" ( CHAMADA_ENCALHE_COTA_0.ID = CONFERENCIA_ENCALHE_0.CHAMADA_ENCALHE_COTA_ID ) ");
		
		hql.append(" INNER JOIN CHAMADA_ENCALHE CHAMADA_ENCALHE_0 ON  ");
		hql.append(" ( CHAMADA_ENCALHE_0.ID = CHAMADA_ENCALHE_COTA_0.CHAMADA_ENCALHE_ID ) ");
		
		hql.append(" WHERE ");

		hql.append(" CHAMADA_ENCALHE_0.ID = CHAMADA_ENCALHE.ID AND 					");
		hql.append(" CONFERENCIA_ENCALHE_0.DATA = CONFERENCIA_ENCALHE.DATA AND 		");
		hql.append(" CONFERENCIA_ENCALHE_0.OBSERVACAO IS NOT NULL ");
		
		return hql;
		
	}
	
	
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.MovimentoEstoqueCotaRepository#obterListaConsultaEncalhe(br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO)
	 */
	@SuppressWarnings("unchecked")
	public List<ConsultaEncalheDTO> obterListaConsultaEncalhe(FiltroConsultaEncalheDTO filtro) {
		
		StringBuilder subSqlVendaProduto = new StringBuilder();
			
		subSqlVendaProduto.append(" select COALESCE(sum( vp.QNT_PRODUTO ),0) ");
		subSqlVendaProduto.append(" from venda_produto vp ");
		subSqlVendaProduto.append(" where vp.ID_PRODUTO_EDICAO = PRODUTO_EDICAO.ID ");
		subSqlVendaProduto.append(" and vp.DATA_OPERACAO BETWEEN :dataRecolhimentoInicial AND :dataRecolhimentoFinal ");
		subSqlVendaProduto.append(" and vp.TIPO_VENDA_ENCALHE = :tipoVendaProduto");
		
		StringBuffer sql = new StringBuffer();

		sql.append("	select	");

		sql.append("	CHAMADA_ENCALHE.DATA_RECOLHIMENTO		as dataDoRecolhimentoDistribuidor, 	");
		sql.append("	PRODUTO.CODIGO 							as codigoProduto, 					");
		sql.append("	PRODUTO.NOME 							as nomeProduto,   					");
		sql.append("	PRODUTO_EDICAO.ID 						as idProdutoEdicao,  				");
		sql.append("	PRODUTO_EDICAO.NUMERO_EDICAO 			as numeroEdicao,  					");
		
		sql.append("	COALESCE(MEC_REPARTE.PRECO_VENDA, PRODUTO_EDICAO.PRECO_VENDA, 0)	as precoVenda, ");
		sql.append("    COALESCE(MEC_REPARTE.PRECO_COM_DESCONTO, PRODUTO_EDICAO.PRECO_VENDA, 0) as precoComDesconto, ");
		sql.append("	COALESCE(MEC_REPARTE.VALOR_DESCONTO, 0) ").append(" as valorDesconto, ");
		
		sql.append("	( SUM( COALESCE(MOVIMENTO_ESTOQUE_COTA.QTDE, 0)) * COALESCE(MEC_REPARTE.PRECO_VENDA, PRODUTO_EDICAO.PRECO_VENDA, 0) ) as valor, ");
		sql.append("	( SUM( COALESCE(MOVIMENTO_ESTOQUE_COTA.QTDE, 0)) * COALESCE(MEC_REPARTE.PRECO_COM_DESCONTO , PRODUTO_EDICAO.PRECO_VENDA, 0) ) as valorComDesconto, ");
		
		sql.append("	SUM(CHAMADA_ENCALHE_COTA.QTDE_PREVISTA) as reparte, ");
		
		sql.append("	SUM(COALESCE(MOVIMENTO_ESTOQUE_COTA.QTDE, 0)) - ( "+ subSqlVendaProduto.toString()  +" )  	as encalhe,     		");
		
		sql.append("	FORNECEDOR.ID						as idFornecedor,		");
		
		sql.append(" 	PESSOA.RAZAO_SOCIAL 				as fornecedor,  		");
		
		sql.append("    CONTROLE_CONF_ENC_COTA.DATA_OPERACAO as dataMovimento,		");

		
		sql.append(	" CASE WHEN ( ").append( getSqlConferenciaEncalheComObservacao() ).append( 
					" ) > 0 THEN 'S' ELSE 'N' END AS indPossuiObservacaoConferenciaEncalhe ");
		
		sql.append(getFromWhereConsultaEncalhe(filtro));
		
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
					case VALOR:
						orderByColumn = " valor ";
						break;
					case VALOR_COM_DESCONTO:
						orderByColumn = " valorComDesconto ";
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

		Map<String, Object> parameters = new HashMap<String, Object>();
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		
		if(filtro.getIdCota()!=null) {
			parameters.put("idCota", filtro.getIdCota());
		}

		if(filtro.getIdFornecedor() != null) {
			parameters.put("idFornecedor", filtro.getIdFornecedor());
		}
		
		parameters.put("grupoMovimentoEstoque", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name());
		parameters.put("dataRecolhimentoInicial", filtro.getDataRecolhimentoInicial());
		parameters.put("dataRecolhimentoFinal", filtro.getDataRecolhimentoFinal());
		parameters.put("isPostergado", false);
		parameters.put("tipoVendaProduto",TipoVendaEncalhe.ENCALHE.name());

		if(filtro.getPaginacao()!=null) {
			
			if(filtro.getPaginacao().getPosicaoInicial()!=null && filtro.getPaginacao().getQtdResultadosPorPagina()!=null) {
				sql.append(" limit :posicaoInicial, :posicaoFinal");
				parameters.put("posicaoInicial",filtro.getPaginacao().getPosicaoInicial());
				parameters.put("posicaoFinal",filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		@SuppressWarnings("rawtypes")
		RowMapper cotaRowMapper = new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {

				ConsultaEncalheDTO dto = new ConsultaEncalheDTO();
				dto.setDataDoRecolhimentoDistribuidor(rs.getDate("dataDoRecolhimentoDistribuidor"));
				dto.setCodigoProduto(rs.getString("codigoProduto"));
				dto.setNomeProduto(rs.getString("nomeProduto"));
				dto.setIdProdutoEdicao(rs.getLong("idProdutoEdicao"));
				dto.setNumeroEdicao(rs.getLong("numeroEdicao"));
				dto.setPrecoVenda(rs.getBigDecimal("precoVenda"));
				dto.setPrecoComDesconto(rs.getBigDecimal("precoComDesconto"));
				dto.setValor(rs.getBigDecimal("valor"));
				dto.setValorComDesconto(rs.getBigDecimal("valorComDesconto"));
				dto.setRecolhimento(rs.getInt("recolhimento"));
				dto.setReparte(rs.getBigDecimal("reparte"));
				dto.setEncalhe(rs.getBigDecimal("encalhe"));
				dto.setIdFornecedor(rs.getLong("idFornecedor"));
				dto.setFornecedor(rs.getString("fornecedor"));
				dto.setDataMovimento(rs.getDate("dataMovimento"));
				dto.setIndPossuiObservacaoConferenciaEncalhe(rs.getString("indPossuiObservacaoConferenciaEncalhe"));
				
				return dto;
			}
		};

		return (List<ConsultaEncalheDTO>) namedParameterJdbcTemplate.query(sql.toString(), parameters, cotaRowMapper);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ConsultaEncalheDetalheDTO> obterListaConsultaEncalheDetalhe(FiltroConsultaEncalheDetalheDTO filtro) {

		StringBuffer sql = new StringBuffer();

		sql.append("	select distinct	");

		sql.append("	COTA.NUMERO_COTA						  as numeroCota,  ");
		sql.append("	coalesce(PESSOA.NOME,PESSOA.RAZAO_SOCIAL) as nomeCota,    ");
		sql.append(" 	CONFERENCIA_ENCALHE.OBSERVACAO			  as observacao  ");
		
		sql.append("	from	");
		
		sql.append("	MOVIMENTO_ESTOQUE_COTA inner join CONFERENCIA_ENCALHE on ");
		sql.append("	( CONFERENCIA_ENCALHE.MOVIMENTO_ESTOQUE_COTA_ID = MOVIMENTO_ESTOQUE_COTA.ID	) ");

		sql.append("	inner join ESTOQUE_PRODUTO_COTA on                                         ");
		sql.append("	( MOVIMENTO_ESTOQUE_COTA.ESTOQUE_PROD_COTA_ID = ESTOQUE_PRODUTO_COTA.ID )  ");
		
		sql.append("	inner join COTA on                                         ");
		sql.append("	( MOVIMENTO_ESTOQUE_COTA.COTA_ID = COTA.ID )  ");
		
		sql.append("	inner join CHAMADA_ENCALHE_COTA on ");
		sql.append("	( CHAMADA_ENCALHE_COTA.ID = CONFERENCIA_ENCALHE.CHAMADA_ENCALHE_COTA_ID ) ");
		
		sql.append("	inner join CHAMADA_ENCALHE on ");
		sql.append("	( CHAMADA_ENCALHE.ID = CHAMADA_ENCALHE_COTA.CHAMADA_ENCALHE_ID ) ");
 		
		sql.append("	inner join PRODUTO_EDICAO on ");
		sql.append("	( PRODUTO_EDICAO.ID = MOVIMENTO_ESTOQUE_COTA.PRODUTO_EDICAO_ID ) ");
		
		sql.append("	inner join PRODUTO_FORNECEDOR on ");
		sql.append("	( PRODUTO_FORNECEDOR.PRODUTO_ID = PRODUTO_EDICAO.PRODUTO_ID ) ");
		
		sql.append("	inner join PESSOA on                   	");
		sql.append("	( PESSOA.ID = COTA.PESSOA_ID )	");

		sql.append("	where	");
		
		sql.append("	MOVIMENTO_ESTOQUE_COTA.DATA = :dataMovimento ");
		
		sql.append("	and CHAMADA_ENCALHE.DATA_RECOLHIMENTO = :dataRecolhimento ");
		
		if(filtro.getNumeroCota()!=null) {
			sql.append(" and COTA.NUMERO_COTA = :numeroCota  ");
		}
		
		if(filtro.getIdFornecedor() != null) {
			sql.append(" and PRODUTO_FORNECEDOR.FORNECEDORES_ID =  :idFornecedor ");
		}
		
		sql.append(" and PRODUTO_EDICAO.ID =  :idProdutoEdicao ");

		PaginacaoVO paginacao = filtro.getPaginacao();

		if (filtro.getOrdenacaoColunaDetalhe() != null) {

			sql.append(" order by ");
			
			String orderByColumn = "";
			
				switch (filtro.getOrdenacaoColunaDetalhe()) {
				
					case NUMERO_COTA:
						orderByColumn = " numeroCota ";
						break;
					case NOME_COTA:
						orderByColumn = " nomeCota ";
						break;
					case OBSERVACAO:
						orderByColumn = " observacao ";
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

		.addScalar("numeroCota")
		.addScalar("nomeCota")
		.addScalar("observacao");
		
		sqlquery.setResultTransformer(new AliasToBeanResultTransformer(ConsultaEncalheDetalheDTO.class));
		
		if(filtro.getNumeroCota()!=null) {
			sqlquery.setParameter("numeroCota", filtro.getNumeroCota());
		}

		if(filtro.getIdFornecedor() != null) {
			sqlquery.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		sqlquery.setParameter("idProdutoEdicao", filtro.getIdProdutoEdicao());
		
		sqlquery.setParameter("dataMovimento", filtro.getDataMovimento());

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
	
	public Integer obterQtdeConsultaEncalheDetalhe(FiltroConsultaEncalheDetalheDTO filtro) {

		StringBuffer sql = new StringBuffer();

		sql.append("	select count(1) from ( ");
		sql.append("	select distinct	");

		sql.append("	COTA.NUMERO_COTA				as numeroCota,  ");
		sql.append("	PESSOA.RAZAO_SOCIAL				as nomeCota,    ");
		sql.append(" 	CONFERENCIA_ENCALHE.OBSERVACAO	as observacao  ");
		
		sql.append("	from	");
		
		sql.append("	MOVIMENTO_ESTOQUE_COTA inner join CONFERENCIA_ENCALHE on ");
		sql.append("	( CONFERENCIA_ENCALHE.MOVIMENTO_ESTOQUE_COTA_ID = MOVIMENTO_ESTOQUE_COTA.ID	) ");

		sql.append("	inner join ESTOQUE_PRODUTO_COTA on                                         ");
		sql.append("	( MOVIMENTO_ESTOQUE_COTA.ESTOQUE_PROD_COTA_ID = ESTOQUE_PRODUTO_COTA.ID )  ");
		
		sql.append("	inner join COTA on                                         ");
		sql.append("	( MOVIMENTO_ESTOQUE_COTA.COTA_ID = COTA.ID )  ");
		
		sql.append("	inner join CHAMADA_ENCALHE_COTA on ");
		sql.append("	( CHAMADA_ENCALHE_COTA.ID = CONFERENCIA_ENCALHE.CHAMADA_ENCALHE_COTA_ID ) ");
		
		sql.append("	inner join CHAMADA_ENCALHE on ");
		sql.append("	( CHAMADA_ENCALHE.ID = CHAMADA_ENCALHE_COTA.CHAMADA_ENCALHE_ID ) ");
 		
		sql.append("	inner join PRODUTO_EDICAO on ");
		sql.append("	( PRODUTO_EDICAO.ID = MOVIMENTO_ESTOQUE_COTA.PRODUTO_EDICAO_ID ) ");
		
		sql.append("	inner join PRODUTO_FORNECEDOR on ");
		sql.append("	( PRODUTO_FORNECEDOR.PRODUTO_ID = PRODUTO_EDICAO.PRODUTO_ID ) ");
		
		sql.append("	inner join PESSOA on                   	");
		sql.append("	( PESSOA.ID = COTA.PESSOA_ID )	");
		
		sql.append("	where	");
		
		sql.append("	MOVIMENTO_ESTOQUE_COTA.DATA = :dataMovimento ");
		
		sql.append("	and CHAMADA_ENCALHE.DATA_RECOLHIMENTO = :dataRecolhimento ");
		
		if(filtro.getNumeroCota()!=null) {
			sql.append(" and COTA.NUMERO_COTA = :numeroCota  ");
		}
		
		if(filtro.getIdFornecedor() != null) {
			sql.append(" and PRODUTO_FORNECEDOR.FORNECEDORES_ID =  :idFornecedor ");
		}
		
		sql.append(" and PRODUTO_EDICAO.ID =  :idProdutoEdicao ");
		
		sql.append(" ) as consultaEncalheDetalhe ");

		SQLQuery sqlquery = getSession().createSQLQuery(sql.toString());
		
		if(filtro.getNumeroCota()!=null) {
			sqlquery.setParameter("numeroCota", filtro.getNumeroCota());
		}

		if(filtro.getIdFornecedor() != null) {
			sqlquery.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		sqlquery.setParameter("idProdutoEdicao", filtro.getIdProdutoEdicao());
		
		sqlquery.setParameter("dataMovimento", filtro.getDataMovimento());

		sqlquery.setParameter("dataRecolhimento", filtro.getDataRecolhimento());

		BigInteger qtde = (BigInteger) sqlquery.uniqueResult();
		
		return ((qtde == null) ? 0 : qtde.intValue());
		
	}	
	
	@Override
	public ConsultaEncalheRodapeDTO obterValoresTotais(FiltroConsultaEncalheDTO filtro) {

		StringBuffer sql = new StringBuffer();

		sql.append("	select ");

		sql.append("	coalesce(sum( ESTOQUE_PRODUTO_COTA.QTDE_RECEBIDA * ");
			
		sql.append("    (COALESCE(MOVIMENTO_ESTOQUE_COTA.PRECO_COM_DESCONTO,PRODUTO_EDICAO.PRECO_VENDA)) ), 0) as valorReparte,");
			
		sql.append("	coalesce(sum( ESTOQUE_PRODUTO_COTA.QTDE_DEVOLVIDA * ");
			
		sql.append("    (COALESCE(MOVIMENTO_ESTOQUE_COTA.PRECO_COM_DESCONTO,PRODUTO_EDICAO.PRECO_VENDA)) ), 0) as valorEncalhe ");

		sql.append("	from	");
		
		sql.append("	MOVIMENTO_ESTOQUE_COTA inner join CONFERENCIA_ENCALHE on ");
		sql.append("	( CONFERENCIA_ENCALHE.MOVIMENTO_ESTOQUE_COTA_ID = MOVIMENTO_ESTOQUE_COTA.ID	) ");

		sql.append("	inner join ESTOQUE_PRODUTO_COTA on                                         ");
		sql.append("	( MOVIMENTO_ESTOQUE_COTA.ESTOQUE_PROD_COTA_ID = ESTOQUE_PRODUTO_COTA.ID )  ");
		
		sql.append("	inner join COTA on                                         ");
		sql.append("	( MOVIMENTO_ESTOQUE_COTA.COTA_ID = COTA.ID )  ");
		
		sql.append("	inner join CHAMADA_ENCALHE_COTA on ");
		sql.append("	( CHAMADA_ENCALHE_COTA.ID = CONFERENCIA_ENCALHE.CHAMADA_ENCALHE_COTA_ID ) ");
		
		sql.append("	inner join CHAMADA_ENCALHE on ");
		sql.append("	( CHAMADA_ENCALHE.ID = CHAMADA_ENCALHE_COTA.CHAMADA_ENCALHE_ID ) ");
 		
		sql.append("	inner join PRODUTO_EDICAO on ");
		sql.append("	( PRODUTO_EDICAO.ID = MOVIMENTO_ESTOQUE_COTA.PRODUTO_EDICAO_ID ) ");
		
		sql.append("	inner join PRODUTO_FORNECEDOR on ");
		sql.append("	( PRODUTO_FORNECEDOR.PRODUTO_ID = PRODUTO_EDICAO.PRODUTO_ID ) ");
		
		sql.append("	where	");
		
		sql.append("	CHAMADA_ENCALHE.DATA_RECOLHIMENTO BETWEEN :dataRecolhimentoInicial AND :dataRecolhimentoFinal ");
		
		if(filtro.getIdCota()!=null) {
			sql.append(" and MOVIMENTO_ESTOQUE_COTA.COTA_ID = :idCota  ");
		}
		
		if(filtro.getIdFornecedor() != null) {
			sql.append(" and PRODUTO_FORNECEDOR.FORNECEDORES_ID =  :idFornecedor ");
		}
		
		SQLQuery sqlquery = getSession().createSQLQuery(sql.toString())

				.addScalar("valorReparte")
				.addScalar("valorEncalhe");
		
		sqlquery.setResultTransformer(new AliasToBeanResultTransformer(ConsultaEncalheRodapeDTO.class));
		
		if(filtro.getIdCota()!=null) {
			sqlquery.setParameter("idCota", filtro.getIdCota());
		}

		if(filtro.getIdFornecedor() != null) {
			sqlquery.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		sqlquery.setParameter("dataRecolhimentoInicial", filtro.getDataRecolhimentoInicial());

		sqlquery.setParameter("dataRecolhimentoFinal", filtro.getDataRecolhimentoFinal());

		return (ConsultaEncalheRodapeDTO) sqlquery.uniqueResult();
		
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
		
		StringBuffer sql = new StringBuffer("");
		
		sql.append(" SELECT "); 

		if(indBuscaQtd) {
			
			sql.append(" COUNT(PROD_EDICAO.ID) ");
			
		} else {
		
			sql.append(" PROD_EDICAO.ID, ");
			
			sql.append(" ( SELECT SUM(  		");
			sql.append(" COALESCE(ESTOQUE_PROD.QTDE, 0) +	");
			sql.append(" COALESCE(ESTOQUE_PROD.QTDE_SUPLEMENTAR, 0) + 		");
			sql.append(" COALESCE(ESTOQUE_PROD.QTDE_DEVOLUCAO_ENCALHE, 0))	");
			sql.append(" FROM ESTOQUE_PRODUTO ESTOQUE_PROD ");
			sql.append(" WHERE ESTOQUE_PROD.PRODUTO_EDICAO_ID = PROD_EDICAO.ID ) as qtdDevolucao, ");
			
			if(indBuscaTotalParcial) {
				sql.append(" ( ");
				sql.append(" SELECT SUM( COALESCE(PARCIAL.QTDE, 0)) ");
				sql.append(" FROM CONFERENCIA_ENC_PARCIAL PARCIAL ");
				sql.append(" WHERE PROD_EDICAO.ID = PARCIAL.PRODUTOEDICAO_ID AND  ");
				sql.append(" PARCIAL.STATUS_APROVACAO = :statusAprovacao  ");
				sql.append(" ) AS qtdNota, ");
				
			} else {
				
				sql.append(" NULL AS QTDE_PARCIAL, ");
				
			}
			
			sql.append(" PROD.CODIGO as codigoProduto,  				");		
			sql.append(" PROD.NOME as nomeProduto, 						");
			sql.append(" PROD_EDICAO.NUMERO_EDICAO as numeroEdicao, 	");
			sql.append(" PROD_EDICAO.PRECO_VENDA as precoVenda, 		");
			
			sql.append(" (case when desconto_logistica_pe.ID is not null then 			");
      		sql.append("  		desconto_logistica_pe.PERCENTUAL_DESCONTO            	");
      	    sql.append("  else												 		 	");
			sql.append(" 		case when desconto_logistica_prod.ID is not null then 	");	
			sql.append(" 			 desconto_logistica_prod.PERCENTUAL_DESCONTO      	");	
			sql.append("         else 0 end         	");		
			sql.append("   end) as desconto           	");	
			
		}

		sql.append(" FROM ");

		
		sql.append(" (	              ");
		
		sql.append(" SELECT CE.PRODUTO_EDICAO_ID AS CE_PRODUTO_EDICAO_ID		");
		sql.append(" FROM CHAMADA_ENCALHE CE		");
		sql.append(" WHERE CE.DATA_RECOLHIMENTO BETWEEN :dataInicial and :dataFinal		");
		sql.append(" GROUP BY CE.PRODUTO_EDICAO_ID		");
		
		sql.append(" ) AS RECOLHIMENTOS	              ");
		
		sql.append(" INNER JOIN PRODUTO_EDICAO PROD_EDICAO ON ( 					");
		sql.append(" 	RECOLHIMENTOS.CE_PRODUTO_EDICAO_ID = PROD_EDICAO.ID 	");
		sql.append(" ) ");
		
		sql.append("  LEFT JOIN  ");
		sql.append("  	desconto_logistica desconto_logistica_pe  ");
		sql.append("  	on ( PROD_EDICAO.DESCONTO_LOGISTICA_ID = desconto_logistica_pe.id ) ");
		
		sql.append(" INNER JOIN PRODUTO PROD ON (			");
		sql.append(" 	PROD_EDICAO.PRODUTO_ID = PROD.ID	");
		sql.append(" ) 	");
		
		sql.append("  LEFT JOIN  ");
		sql.append("  	desconto_logistica desconto_logistica_prod  ");
		sql.append("  	on ( PROD.DESCONTO_LOGISTICA_ID = desconto_logistica_prod.id ) ");
		
		sql.append(" INNER JOIN PRODUTO_FORNECEDOR PROD_FORNEC ON (	");
		sql.append(" 	PROD.ID = PROD_FORNEC.PRODUTO_ID 			");
		sql.append(" ) ");
		
		

		if( filtro.getIdFornecedor() != null ) {
			
			sql.append(" WHERE ");
			
			sql.append(" PROD_FORNEC.FORNECEDORES_ID = :idFornecedor ");
			
		}
		
		
		PaginacaoVO paginacao = filtro.getPaginacao();

		if (!indBuscaQtd && filtro.getOrdenacaoColuna() != null) {

			sql.append(" order by ");
			
			String orderByColumn = "";
			
				switch (filtro.getOrdenacaoColuna()) {
				
					case CODIGO_PRODUTO:
						orderByColumn = "codigoProduto ";
						break;
					case NOME_PRODUTO:
						orderByColumn = "nomeProduto ";
						break;
					case NUMERO_EDICAO:
						orderByColumn = "numeroEdicao ";
						break;
					case PRECO_CAPA:
						orderByColumn = "precoVenda ";
						break;
					case QTD_DEVOLUCAO:
						orderByColumn = "qtdDevolucao ";
						break;
					case QTD_NOTA:
						orderByColumn = "qtdNota ";
						break;
						
					default:
						break;
				}
			
			sql.append(orderByColumn);
			
			if (paginacao.getOrdenacao() != null) {
				
				sql.append(paginacao.getOrdenacao().toString());
				
			}
			
		}
		
		return sql.toString();
		
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
	private Query criarQueryComParametrosObterListaContagemDevolucao(String hql, FiltroDigitacaoContagemDevolucaoDTO filtro, boolean indBuscaTotalParcial, boolean indBuscaQtd) {
		
		Query query = null;
		
		if(indBuscaQtd) {
		
			query = getSession().createSQLQuery(hql.toString());
		
		} else {
			
			query = getSession().createSQLQuery(hql.toString())
					.addScalar("codigoProduto", StandardBasicTypes.STRING)
					.addScalar("nomeProduto", StandardBasicTypes.STRING)
					.addScalar("numeroEdicao", StandardBasicTypes.LONG)
					.addScalar("precoVenda", StandardBasicTypes.BIG_DECIMAL)
					.addScalar("desconto", StandardBasicTypes.BIG_DECIMAL)
					.addScalar("qtdDevolucao", StandardBasicTypes.BIG_INTEGER);
			

			if(indBuscaTotalParcial) {
			
				((SQLQuery) query).addScalar("qtdNota", StandardBasicTypes.BIG_INTEGER);
			}
			
			query.setResultTransformer(Transformers.aliasToBean(ContagemDevolucaoDTO.class));
		}
		
		query.setParameter("dataInicial", filtro.getPeriodo().getDe());
		
		query.setParameter("dataFinal", filtro.getPeriodo().getAte());
		
		if(indBuscaTotalParcial) {
			query.setParameter("statusAprovacao", StatusAprovacao.PENDENTE.name());
		}
		
		if(filtro.getIdFornecedor() != null) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		return query;
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.MovimentoEstoqueCotaRepository#obterListaContagemDevolucao(br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO, boolean)
	 */
	@SuppressWarnings("unchecked")
	public List<ContagemDevolucaoDTO> obterListaContagemDevolucao(
			FiltroDigitacaoContagemDevolucaoDTO filtro,
			boolean indBuscaTotalParcial) {
		
		String hql = getConsultaListaContagemDevolucao(filtro, indBuscaTotalParcial, false);
		
		Query query = criarQueryComParametrosObterListaContagemDevolucao(hql, filtro, indBuscaTotalParcial, false);
		
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
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.MovimentoEstoqueCotaRepository#obterValorTotalGeralContagemDevolucao(br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO, br.com.abril.nds.model.estoque.TipoMovimentoEstoque)
	 */
	public BigDecimal obterValorTotalGeralContagemDevolucao(
			FiltroDigitacaoContagemDevolucaoDTO filtro) {
		
		StringBuffer sql = new StringBuffer("");

		sql.append(" SELECT ")
		
		.append(" SUM(MOV_ESTOQUE_COTA.QTDE * PRODUTO_EDICAO.PRECO_VENDA) ")
		
		.append(" FROM ")

		.append(" MOVIMENTO_ESTOQUE_COTA MOV_ESTOQUE_COTA ")

		.append(" INNER JOIN CONFERENCIA_ENCALHE ON ( ")
		.append("	CONFERENCIA_ENCALHE.movimento_estoque_cota_id = MOV_ESTOQUE_COTA.ID ")
		.append(" ) ")
		
		.append(" INNER JOIN PRODUTO_EDICAO ON (	")
		.append("	PRODUTO_EDICAO.ID = MOV_ESTOQUE_COTA.PRODUTO_EDICAO_ID ")
		.append(" ) ")
		
		.append(" INNER JOIN PRODUTO PROD ON (			")
		.append(" 	PRODUTO_EDICAO.PRODUTO_ID = PROD.ID	")
		.append(" ) 	")
		
		.append(" INNER JOIN PRODUTO_FORNECEDOR PROD_FORNEC ON (	")
		.append(" 	PROD.ID = PROD_FORNEC.PRODUTO_ID 				")
		.append(" ) ")
		
		.append(" LEFT JOIN CONTROLE_CONTAGEM_DEVOLUCAO ON ( 		")
		.append(" 	CONTROLE_CONTAGEM_DEVOLUCAO.PRODUTO_EDICAO_ID  	=  MOV_ESTOQUE_COTA.PRODUTO_EDICAO_ID AND ")
	    .append("   CONTROLE_CONTAGEM_DEVOLUCAO.DATA     			=  MOV_ESTOQUE_COTA.DATA AND ")
	    .append("   CONTROLE_CONTAGEM_DEVOLUCAO.STATUS = :statusOperacao ) ")
		
		.append(" WHERE ")

		.append(" MOV_ESTOQUE_COTA.DATA BETWEEN :dataInicial AND :dataFinal AND ")
		
		.append(" CONTROLE_CONTAGEM_DEVOLUCAO.ID IS NULL  ")
		
		.append(" AND MOV_ESTOQUE_COTA.PRODUTO_EDICAO_ID IN ( ")
	    
		.append("	SELECT ")
		.append("	ITEM_CH_ENC_FORNECEDOR.PRODUTO_EDICAO_ID  ")
	    	
		.append("	from ")
		.append("	ITEM_CHAMADA_ENCALHE_FORNECEDOR ITEM_CH_ENC_FORNECEDOR ")
	    	
		.append("	where ");
		
		if( filtro.getIdFornecedor() != null ) {
			sql.append(" PROD_FORNEC.FORNECEDORES_ID = :idFornecedor AND ");		
		}
		
		sql.append(" ITEM_CH_ENC_FORNECEDOR.DATA_RECOLHIMENTO BETWEEN :dataInicial AND :dataFinal ")
	    	
		.append("	GROUP BY ITEM_CH_ENC_FORNECEDOR.PRODUTO_EDICAO_ID   ) ");
		
		Query query = getSession().createSQLQuery(sql.toString());

		query.setParameter("dataInicial", filtro.getPeriodo().getDe());
		
		query.setParameter("dataFinal", filtro.getPeriodo().getAte());
		
		query.setParameter("statusOperacao", StatusOperacao.CONCLUIDO);
		
		if(filtro.getIdFornecedor() != null) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		BigDecimal valor = (BigDecimal) query.uniqueResult();
		
		return ((valor == null) ? BigDecimal.ZERO : valor);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.MovimentoEstoqueCotaRepository#obterQuantidadeContagemDevolucao(br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO)
	 */
	public Integer obterQuantidadeContagemDevolucao(FiltroDigitacaoContagemDevolucaoDTO filtro) {
		
		String hql = getConsultaListaContagemDevolucao(filtro, false, true);
		
		Query query = criarQueryComParametrosObterListaContagemDevolucao(hql, filtro, false, true);
		
		BigInteger qtde = (BigInteger) query.uniqueResult();
		
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
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.MovimentoEstoqueCotaRepository#obterQtdMovimentoCotaPorTipoMovimento(java.util.Date, java.lang.Long, br.com.abril.nds.model.estoque.GrupoMovimentoEstoque)
	 */
	@Override
	public Map<Long, BigInteger> obterQtdMovimentoCotaPorTipoMovimento(Intervalo<Date> periodo, 
			                                                           Long idCota, 
			                                                           GrupoMovimentoEstoque... gruposMovimentoEstoque){
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select sum( case when (movimento.tipoMovimento.grupoMovimentoEstoque.operacaoEstoque = :operacaoEntrada) ");
		
		hql.append(	" 			 then  movimento.qtde else - movimento.qtde end ) as quantidade, ");
		
		hql.append(" produtoEdicao.id as idProdutoEdicao ");
		
		hql.append(" from MovimentoEstoqueCota movimento join movimento.produtoEdicao produtoEdicao ");			
		
		hql.append(" where movimento.cota.id = :idCota ");
		
		hql.append(" and movimento.data between :inicio and :fim ");
		
		hql.append(" and movimento.tipoMovimento.grupoMovimentoEstoque  in (:gruposMovimento) ");
		
		hql.append(" group by produtoEdicao.id ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("inicio", periodo.getDe());
		
		query.setParameter("fim", periodo.getAte());
		
		query.setParameter("idCota", idCota);
		
		query.setParameter("operacaoEntrada", OperacaoEstoque.ENTRADA);
		
		query.setParameterList("gruposMovimento", gruposMovimentoEstoque);
		
		@SuppressWarnings("unchecked")
		List<Object[]> listaResultados = query.list();

		Map<Long, BigInteger> mapResult = new HashMap<>();
		
		for (Object[] item : listaResultados) {

			BigInteger quantidade = (BigInteger) item[0];
			
			Long id = (Long) item[1];
			
			mapResult.put(id,quantidade);
		}
		
		return mapResult;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<MovimentoEstoqueCota> obterMovimentoCotaLancamentoPorTipoMovimento(Date dataLancamento, 
																				   Long idCota, 
																				   GrupoMovimentoEstoque grupoMovimentoEstoque) {
		
		StringBuffer hql = new StringBuffer("");
		
		hql.append(" select movimento from MovimentoEstoqueCota movimento ");			
		
		hql.append(" inner join movimento.lancamento lancamento ");
		
		hql.append(" where movimento.cota.id = :idCota ");
		
		hql.append(" and movimento.data = :data ");
		
		hql.append(" and lancamento.dataLancamentoDistribuidor = :data ");
		
		hql.append(" and movimento.tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEstoque ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("data", dataLancamento);
		
		query.setParameter("idCota", idCota);
		
		query.setParameter("grupoMovimentoEstoque", grupoMovimentoEstoque);
		
		return query.list();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<MovimentoEstoqueCotaDTO> obterMovimentoCotasPorTipoMovimento(Date data, List<Integer> numCotas, GrupoMovimentoEstoque grupoMovimentoEstoque){
		
		StringBuffer hql = new StringBuffer();
				
		hql.append(" select  cota.id as idCota, ");
		
		hql.append(" 		 produtoEdicao.id as idProdEd, ");
		
		hql.append(" 		 produto.codigo as codigoProd, ");
		
		hql.append(" 		 produtoEdicao.numeroEdicao as edicaoProd, ");
		
		hql.append(" 		 produto.nomeComercial as nomeProd, ");
		
		hql.append(" 		 sum(movimento.qtde) as qtdeReparte ");
		
		hql.append(" from MovimentoEstoqueCota movimento");	
		
		hql.append(" join movimento.produtoEdicao produtoEdicao ");
		
		hql.append(" join produtoEdicao.produto produto ");
		
		hql.append(" join movimento.cota cota ");
		
		hql.append(" join movimento.tipoMovimento tipoMovimento ");
		
		hql.append(" join movimento.lancamento lancamento ");
				
		hql.append(" where cota.numeroCota in (:numCotas) ");
		
		hql.append(" and movimento.data = :data ");
		
		hql.append(" and lancamento.dataLancamentoDistribuidor = :data ");
		
		hql.append(" and tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEstoque ");
		
		hql.append(" group by produtoEdicao.id ");
				
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("data", data);
		
		query.setParameterList("numCotas", numCotas);
		
		query.setParameter("grupoMovimentoEstoque", grupoMovimentoEstoque);
		
		query.setResultTransformer(Transformers.aliasToBean(MovimentoEstoqueCotaDTO.class));
		
		return query.list();
		
	}

	@SuppressWarnings("unchecked")
	public List<AbastecimentoDTO> obterDadosAbastecimento(FiltroMapaAbastecimentoDTO filtro) {
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		HashMap<String, List<String>> paramList = new HashMap<String, List<String>>();
		
		List<StatusLancamento> statusLancamento = new ArrayList<StatusLancamento>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select box.id as idBox, ");
		hql.append(" 		box.codigo || '-'|| box.nome as box, ");
		hql.append(" 		cota.numeroCota as codigoCota, ");
		hql.append("       case when (pessoa.nome is not null) then ( pessoa.nome ) ");
		hql.append("       when (pessoa.razaoSocial is not null) then ( pessoa.razaoSocial ) ");
		hql.append("       else null end as nomeCota, ");
		hql.append(" 		count(distinct  produtoEdicao.id) as totalProduto, ");
		hql.append(" 		lancamento.repartePromocional as materialPromocional, ");
		hql.append(" 		sum(estudoCota.qtdeEfetiva) as totalReparte, ");
		hql.append(" 		sum(estudoCota.qtdeEfetiva * produtoEdicao.precoVenda) as totalBox ");
			
		gerarFromWhereDadosAbastecimento(filtro, hql, param, paramList, statusLancamento);
		
		hql.append(" group by box.id ");
		
		if (filtro.getQuebraPorCota() != null && filtro.getQuebraPorCota()) {
			hql.append(" , cota.id ");
		}
		
		if (filtro.getExcluirProdutoSemReparte()!= null && filtro.getExcluirProdutoSemReparte()) {

			hql.append(" having sum(estudoCota.qtdeEfetiva) > 0 ");
		}
				
		gerarOrdenacaoDadosAbastecimento(filtro, hql);		

		Query query =  getSession().createQuery(hql.toString());
				
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		for(String key : paramList.keySet()){
			query.setParameterList(key, paramList.get(key));
		}
		
		query.setParameterList("status", statusLancamento);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(AbastecimentoDTO.class));
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
	
	}

	private void gerarFromWhereDadosAbastecimento(FiltroMapaAbastecimentoDTO filtro, StringBuilder hql, HashMap<String, Object> param, HashMap<String, List<String>> paramList, List<StatusLancamento> statusLancamento) {
		
		hql.append(" from EstudoCota estudoCota ");
		hql.append("	join estudoCota.estudo estudo ");
		hql.append("	join estudoCota.cota cota ");
		hql.append("	join estudo.produtoEdicao produtoEdicao ");
		hql.append("	join produtoEdicao.produto produto ");		

		hql.append("    join cota.pdvs pdv ");
		hql.append("    join cota.pessoa pessoa ");
		hql.append("    join pdv.rotas rotaPDV  ");
		hql.append("    join rotaPDV.rota rota ");
		hql.append("    join rota.roteiro roteiro ");
		
		if(filtro.getIdEntregador() != null)
			hql.append("    join rota.entregador entregador ");
		
		hql.append("    join roteiro.roteirizacao roteirizacao ");
		
		hql.append("    join cota.box box ");
		
		// Criado pelo Eduardo Punk Rock - Comentado para realizar a busca através da data de lançamento do distribuidor e não a data de movimento que foi gerada
		/*if(filtro.getUseSM() != null && filtro.getUseSM() == true) {
			hql.append("	join movimentoCota.estudoCota estudoCota ");
			hql.append("	join estudoCota.estudo estudo ");
			hql.append("	join estudo.lancamentos lancamento ");
		}*/
		hql.append("	join estudo.lancamentos lancamento ");
		
		hql.append(" where 1=1 ");

		if(filtro.getDataDate() != null) {
			// Criado pelo Eduardo Punk Rock - Comentado para realizar a busca através da data de lançamento do distribuidor e não a data de movimento que foi gerada
			//hql.append(" and movimentoCota.data=:data ");
			hql.append(" and lancamento.dataLancamentoDistribuidor=:data ");
			param.put("data", filtro.getDataDate());
		}
		
		if(filtro.getBox() != null) {
			
			hql.append(" and box.id =:box ");
			param.put("box", filtro.getBox());
		}
		
		if(filtro.getRota() != null) {
			
			hql.append(" and rota.id =:rota ");
			param.put("rota", filtro.getRota());
		}
		
		if(filtro.getRoteiro() != null) {
			
			hql.append(" and roteiro.id =:roteiro ");
			param.put("roteiro", filtro.getRoteiro());
		}
		
		if(filtro.getCodigosProduto() != null && !filtro.getCodigosProduto().isEmpty()) {

			hql.append(" and produto.codigo in (:codigosProduto) ");
			paramList.put("codigosProduto", filtro.getCodigosProduto());
		}
		
		if(filtro.getEdicaoProduto() != null) {
			
			hql.append(" and produtoEdicao.numeroEdicao =:numeroEdicao ");
			param.put("numeroEdicao", filtro.getEdicaoProduto());
		}
		
		if(filtro.getCodigoCota() != null ) {
			
			hql.append(" and cota.numeroCota =:codigoCota ");
			param.put("codigoCota", filtro.getCodigoCota());
		}
		
		if(filtro.getIdEntregador() != null ) {
			
			hql.append(" and entregador.id =:idEntregador ");
			param.put("idEntregador", filtro.getIdEntregador());
		}
		
		hql.append(" and lancamento.status in (:status) ");
		statusLancamento.add(StatusLancamento.BALANCEADO);
		statusLancamento.add(StatusLancamento.EXPEDIDO);
		
	}
	
	private void gerarOrdenacaoDadosAbastecimento(FiltroMapaAbastecimentoDTO filtro, StringBuilder hql) {
		
		String sortOrder = filtro.getPaginacao().getOrdenacao().name();
		ColunaOrdenacao coluna = ColunaOrdenacao.getPorDescricao(filtro.getPaginacao().getSortColumn());

		String nome = null;

		switch(coluna) {
			case BOX:
				nome = " box ";
				break;
			case TOTAL_PRODUTO: 
				nome = " totalProduto ";
				break;
			case TOTAL_REPARTE:
				nome = " totalReparte ";
				break;
			case TOTAL_BOX:
				nome = " totalBox ";
				break;
			case CODIGO_COTA:
				nome = " codigoCota ";
				break;
			case CODIGO_ROTA:
				nome = " codigoRota ";
				break;
			case NOME_COTA:
				nome = " nomeCota ";
				break;
			case CODIGO_PRODUTO:
				nome = " codigoProduto ";
				break;
			case NOME_PRODTO: 
				nome = " nomeProduto ";
				break;
			case NUMERO_EDICAO:
				nome = " numeroEdicao ";
				break;
			case REPARTE:
				nome = " reparte ";
				break;
			case PRECO_CAPA:
				nome = " precoCapa ";
				break;
			case TOTAL:
				nome = " total ";
				break;
			case CODIGO_BOX:
				nome = " codigoBox ";
				break;
			case MATERIAL_PROMOCIONAL:
				nome = " materialPromocional ";
				break;
				case NOME_EDICAO:
					nome = " nomeProduto,numeroEdicao ";
					break;
		
		}
		hql.append( " order by " + nome + sortOrder + " ");
	}
	

	private void gerarOrdenacaoEntregador(FiltroMapaAbastecimentoDTO filtro, StringBuilder hql) {
		
		String sortOrder = filtro.getPaginacao().getOrdenacao().name();
		
		ColunaOrdenacaoEntregador coluna = ColunaOrdenacaoEntregador.getPorDescricao(filtro.getPaginacao().getSortColumn());
		
		String nome = null;
		
		switch(coluna) {
			case CODIGO_PRODUTO:
				nome = " codigoProduto ";
				break;
			case NOME_PRODTO: 
				nome = " nomeProduto ";
				break;
			case NUMERO_EDICAO:
				nome = " numeroEdicao ";
				break;
			case CODIGO_BARRA:
				nome = " codigoBarra ";
				break;
			case PACOTE_PADRAO:
				nome = " pacotePadrao ";
				break;
			case REPARTE:
				nome = " reparte ";
				break;
			case PRECO_CAPA:
				nome = " precoCapa ";
				break;
			case CODIGO_COTA:
				nome = " codigoCota ";
				break;
			case NOME_COTA: 
				nome = " nomeCota ";
				break;
			case QTDE_EXEMPLARES:
				nome = " qtdeExms ";
				break;
		}
		
		hql.append( " order by " + nome + sortOrder + " ");
	}

	@Override
	public Long countObterDadosAbastecimento(FiltroMapaAbastecimentoDTO filtro) {
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		HashMap<String, List<String>> paramList = new HashMap<String, List<String>>();
		
		List<StatusLancamento> statusLancamento = new ArrayList<StatusLancamento>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select count(*) ");
			
		gerarFromWhereDadosAbastecimento(filtro, hql, param, paramList, statusLancamento);
		
		hql.append(" group by box.id ");
		
		if (filtro.getQuebraPorCota() != null && filtro.getQuebraPorCota()) {
			hql.append(" , cota.id ");
		}
		
		if (filtro.getExcluirProdutoSemReparte()!= null && filtro.getExcluirProdutoSemReparte()) {

			hql.append(" having sum(estudoCota.qtdeEfetiva) > 0 ");
		}
		
		Query query =  getSession().createQuery(hql.toString());
				
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
	
		for(String key : paramList.keySet()){
			query.setParameterList(key, paramList.get(key));
		}
		
		query.setParameterList("status", statusLancamento);
		
		@SuppressWarnings("unchecked")
		List<Long> count = query.list();
		
		return (long) ((count == null || count.isEmpty()) ? 0 : count.size());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoAbastecimentoDTO> obterDetlhesDadosAbastecimento(
			Long idBox, FiltroMapaAbastecimentoDTO filtro) {
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		HashMap<String, List<String>> paramList = new HashMap<String, List<String>>();
		
		List<StatusLancamento> statusLancamento = new ArrayList<StatusLancamento>();
		
		StringBuilder hql = new StringBuilder();
				
		hql.append(" select produto.codigo as codigoProduto, ");
		hql.append(" 		produto.nome as nomeProduto, ");
		hql.append(" 		produtoEdicao.numeroEdicao as numeroEdicao, ");		
		hql.append(" 		sum(estudoCota.qtdeEfetiva) as reparte, ");
		hql.append(" 		produtoEdicao.precoVenda as precoCapa, ");
		hql.append(" 		sum(estudoCota.qtdeEfetiva * produtoEdicao.precoVenda) as total ");
		
		filtro.setBox(idBox);
						
		gerarFromWhereDadosAbastecimento(filtro, hql, param, paramList, statusLancamento);
		
		hql.append(" group by produtoEdicao.id ");
		
		if (filtro.getExcluirProdutoSemReparte()!= null && filtro.getExcluirProdutoSemReparte()) {

			hql.append(" having sum(estudoCota.qtdeEfetiva) > 0 ");
		}
		
		gerarOrdenacaoDetalhesAbastecimento(filtro, hql);		

		Query query =  getSession().createQuery(hql.toString());
				
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		for(String key : paramList.keySet()){
			query.setParameterList(key, paramList.get(key));
		}
		
		query.setParameterList("status", statusLancamento);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoAbastecimentoDTO.class));
		
		return query.list();
	}      
	
	private void gerarOrdenacaoDetalhesAbastecimento(FiltroMapaAbastecimentoDTO filtro, StringBuilder hql) {
		
		String sortOrder = filtro.getPaginacaoDetalhes().getOrdenacao().name();
		ColunaOrdenacaoDetalhes coluna = ColunaOrdenacaoDetalhes.getPorDescricao(filtro.getPaginacaoDetalhes().getSortColumn());
		
		String nome = null;
		
		switch(coluna) {
			case CODIGO_PRODUTO:
				nome = " codigoProduto ";
				break;
			case NOME_PRODTO: 
				nome = " nomeProduto ";
				break;
			case NUMERO_EDICAO:
				nome = " numeroEdicao ";
				break;
			case REPARTE:
				nome = " reparte ";
				break;
			case PRECO_CAPA:
				nome = " precoCapa ";
				break;
			case TOTAL:
				nome = " total ";
				break;
			case CODIGO_BOX:
				nome = " codigoBox ";
				break;
		}
		hql.append( " order by " + nome + sortOrder + " ");
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoAbastecimentoDTO> obterMapaAbastecimentoPorBox(FiltroMapaAbastecimentoDTO filtro) {
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		HashMap<String, List<String>> paramList = new HashMap<String, List<String>>();
		
		List<StatusLancamento> statusLancamento = new ArrayList<StatusLancamento>();
		
		StringBuilder hql = new StringBuilder();
				
		hql.append(" select box.codigo as codigoBox, ");
	    hql.append(" 		produto.codigo as codigoProduto, ");
		hql.append(" 		produto.nome as nomeProduto, ");
		hql.append(" 		produtoEdicao.numeroEdicao as numeroEdicao, ");		
		hql.append(" 		sum(estudoCota.qtdeEfetiva) as reparte, ");
		hql.append(" 		produtoEdicao.precoVenda as precoCapa, ");
		hql.append(" 		pessoa.nome as nomeCota ");
								
		gerarFromWhereDadosAbastecimento(filtro, hql, param, paramList, statusLancamento);
		
		hql.append(" group by produtoEdicao.id, box.id ");
		
		if (filtro.getQuebraPorCota() != null && filtro.getQuebraPorCota()) {
			hql.append(" , cota.id ");
		}
		
		if (filtro.getExcluirProdutoSemReparte()!= null && filtro.getExcluirProdutoSemReparte()) {

			hql.append(" having sum(estudoCota.qtdeEfetiva) > 0 ");
		}
				
		gerarOrdenacaoDadosAbastecimento(filtro, hql);

		Query query =  getSession().createQuery(hql.toString());
				
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		for(String key : paramList.keySet()){
			query.setParameterList(key, paramList.get(key));
		}
		
		query.setParameterList("status", statusLancamento);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoAbastecimentoDTO.class));
		
		return query.list();
	}   

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoAbastecimentoDTO> obterMapaAbastecimentoPorBoxRota(FiltroMapaAbastecimentoDTO filtro) {

		HashMap<String, Object> param = new HashMap<String, Object>();

		HashMap<String, List<String>> paramList = new HashMap<String, List<String>>();
		
		List<StatusLancamento> statusLancamento = new ArrayList<StatusLancamento>();

		StringBuilder hql = new StringBuilder();

		hql.append(" select box.codigo as codigoBox, ");
		hql.append(" 		rota.descricaoRota as codigoRota, ");
		hql.append(" 		cota.numeroCota as codigoCota, ");
		hql.append(" 		pessoa.nome as nomeCota, ");
		hql.append(" 		produto.codigo as codigoProduto, ");
		hql.append(" 		produto.nome as nomeProduto, ");
		hql.append(" 		produtoEdicao.numeroEdicao as numeroEdicao, ");
		hql.append(" 		lancamento.repartePromocional as materialPromocional, ");
		hql.append(" 		sum(estudoCota.qtdeEfetiva) as reparte, ");
		hql.append(" 		sum(estudoCota.qtdeEfetiva * produtoEdicao.precoVenda) as totalBox, ");
		hql.append("        count(distinct produtoEdicao.id) as totalProduto, ");
		hql.append(" 		produtoEdicao.precoVenda as precoCapa ");

		gerarFromWhereDadosAbastecimento(filtro, hql, param, paramList, statusLancamento);
		
		//hql.append(" group by produtoEdicao.id, box.id, rota.id ");
		hql.append(" group by box.id, rota.id ");
		
		if (filtro.getExcluirProdutoSemReparte()!=null && filtro.getExcluirProdutoSemReparte()) {

			hql.append(" having sum(estudoCota.qtdeEfetiva) > 0 ");
		}
		
		gerarOrdenacaoDadosAbastecimento(filtro, hql);
				
		Query query =  getSession().createQuery(hql.toString());
				
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		for(String key : paramList.keySet()){
			query.setParameterList(key, paramList.get(key));
		}
		
		query.setParameterList("status", statusLancamento);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoAbastecimentoDTO.class));
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
	}

	@Override
	public Long countObterMapaAbastecimentoPorBoxRota(FiltroMapaAbastecimentoDTO filtro) {
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		HashMap<String, List<String>> paramList = new HashMap<String, List<String>>();
		
		List<StatusLancamento> statusLancamento = new ArrayList<StatusLancamento>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select count(*) ");
			
		gerarFromWhereDadosAbastecimento(filtro, hql, param, paramList, statusLancamento);

		hql.append(" group by produtoEdicao.id, box.id, rota.descricaoRota ");
		
		if (filtro.getExcluirProdutoSemReparte()!= null && filtro.getExcluirProdutoSemReparte()) {

			hql.append(" having sum(estudoCota.qtdeEfetiva) > 0 ");
		}

		Query query =  getSession().createQuery(hql.toString());
				
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
	
		for(String key : paramList.keySet()){
			query.setParameterList(key, paramList.get(key));
		}
		
		query.setParameterList("status", statusLancamento);

		@SuppressWarnings("unchecked")
		List<Long> count = query.list();
		
		return (long) ((count == null || count.isEmpty()) ? 0 : count.size());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoAbastecimentoDTO> obterMapaAbastecimentoPorProdutoEdicao(
			FiltroMapaAbastecimentoDTO filtro) {

		HashMap<String, Object> param = new HashMap<String, Object>();
		
		HashMap<String, List<String>> paramList = new HashMap<String, List<String>>();
		
		List<StatusLancamento> statusLancamento = new ArrayList<StatusLancamento>();
		
		StringBuilder hql = new StringBuilder();
				
		hql.append(" select box.codigo as codigoBox, ");
		hql.append(" 		rota.descricaoRota as codigoRota, ");
		hql.append(" 		produto.codigo as codigoProduto, ");
		hql.append(" 		produto.nome as nomeProduto, ");
		hql.append(" 		produtoEdicao.numeroEdicao as numeroEdicao, ");		
		hql.append(" 		lancamento.repartePromocional as materialPromocional, ");
		hql.append(" 		sum(estudoCota.qtdeEfetiva) as reparte, ");
		hql.append(" 		sum(estudoCota.qtdeEfetiva * produtoEdicao.precoVenda) as totalBox, ");
		hql.append(" 		produtoEdicao.precoVenda as precoCapa ");
		
								
		gerarFromWhereDadosAbastecimento(filtro, hql, param, paramList, statusLancamento);
		
		hql.append(" group by box.id, rota.descricaoRota ");
		
		if (filtro.getExcluirProdutoSemReparte()!= null && filtro.getExcluirProdutoSemReparte()) {

			hql.append(" having sum(estudoCota.qtdeEfetiva) > 0 ");
		}
		
		gerarOrdenacaoDadosAbastecimento(filtro, hql);
				
		Query query =  getSession().createQuery(hql.toString());
				
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		for(String key : paramList.keySet()){
			query.setParameterList(key, paramList.get(key));
		}
		
		query.setParameterList("status", statusLancamento);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoAbastecimentoDTO.class));

		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long countObterMapaAbastecimentoPorProdutoEdicao(FiltroMapaAbastecimentoDTO filtro) {
		
		HashMap<String, Object> param = new HashMap<String, Object>();

		HashMap<String, List<String>> paramList = new HashMap<String, List<String>>();
		
		List<StatusLancamento> statusLancamento = new ArrayList<StatusLancamento>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select count(*) ");
		
		gerarFromWhereDadosAbastecimento(filtro, hql, param, paramList, statusLancamento);
		
		hql.append(" group by box.id, rota.descricaoRota ");

		if (filtro.getExcluirProdutoSemReparte()!= null && filtro.getExcluirProdutoSemReparte()) {

			hql.append(" having sum(estudoCota.qtdeEfetiva) > 0 ");
		}
		
		Query query =  getSession().createQuery(hql.toString());
				
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
	
		for(String key : paramList.keySet()){
			query.setParameterList(key, paramList.get(key));
		}
		
		query.setParameterList("status", statusLancamento);
		
		@SuppressWarnings("unchecked")
		List<Long> count = query.list();
		
		return (long) ((count == null || count.isEmpty()) ? 0 : count.size());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoAbastecimentoDTO> obterMapaAbastecimentoPorCota(
			FiltroMapaAbastecimentoDTO filtro) {
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		HashMap<String, List<String>> paramList = new HashMap<String, List<String>>();
		
		List<StatusLancamento> statusLancamento = new ArrayList<StatusLancamento>();
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select produtoEdicao.id as idProdutoEdicao, ");
		hql.append(" 		produto.codigo as codigoProduto, ");
		hql.append(" 		produto.nome as nomeProduto, ");
		hql.append(" 		produtoEdicao.numeroEdicao as numeroEdicao, ");		
		hql.append(" 		sum(estudoCota.qtdeEfetiva) as reparte, ");
		hql.append(" 		sum(estudoCota.qtdeEfetiva * produtoEdicao.precoVenda) as totalBox, ");
		hql.append(" 		lancamento.sequenciaMatriz as sequenciaMatriz ");
		
		filtro.setUseSM(true);
		
		gerarFromWhereDadosAbastecimento(filtro, hql, param, paramList, statusLancamento);
		
		hql.append(" group by produtoEdicao.id ");
		
		if (filtro.getExcluirProdutoSemReparte()!=null && filtro.getExcluirProdutoSemReparte()) {

			hql.append(" having sum(estudoCota.qtdeEfetiva) > 0 ");
		}
		
		gerarOrdenacaoDadosAbastecimento(filtro, hql);
				
		Query query =  getSession().createQuery(hql.toString());
				
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		for(String key : paramList.keySet()){
			query.setParameterList(key, paramList.get(key));
		}
		
		query.setParameterList("status", statusLancamento);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoAbastecimentoDTO.class));

		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());

		return query.list();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long countObterMapaAbastecimentoPorCota(FiltroMapaAbastecimentoDTO filtro) {

		HashMap<String, Object> param = new HashMap<String, Object>();

		HashMap<String, List<String>> paramList = new HashMap<String, List<String>>();
		
		List<StatusLancamento> statusLancamento = new ArrayList<StatusLancamento>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select count(*) ");
		
		filtro.setUseSM(true);

		gerarFromWhereDadosAbastecimento(filtro, hql, param, paramList, statusLancamento);
		
		hql.append(" group by produtoEdicao.id ");
		
		if (filtro.getExcluirProdutoSemReparte()!=null && filtro.getExcluirProdutoSemReparte()) {

			hql.append(" having sum(estudoCota.qtdeEfetiva) > 0 ");
		}
		
		Query query =  getSession().createQuery(hql.toString());
				
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
	
		for(String key : paramList.keySet()){
			query.setParameterList(key, paramList.get(key));
		}
		
		query.setParameterList("status", statusLancamento);
		
		@SuppressWarnings("unchecked")
		List<Long> count = query.list();
		
		return (long) ((count == null || count.isEmpty()) ? 0 : count.size());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoAbastecimentoDTO> obterMapaDeImpressaoPorProdutoQuebrandoPorCota(
			FiltroMapaAbastecimentoDTO filtro) {

		HashMap<String, Object> param = new HashMap<String, Object>();
		
		HashMap<String, List<String>> paramList = new HashMap<String, List<String>>();
		
		List<StatusLancamento> statusLancamento = new ArrayList<StatusLancamento>();
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select cota.numeroCota as codigoCota, ");
		hql.append(" 		(CASE WHEN pessoa.class = 'J' THEN pessoa.razaoSocial ELSE pessoa.nome END) as nomeCota,");
		hql.append(" 		produto.codigo as codigoProduto, ");
		hql.append(" 		produto.nome as nomeProduto, ");
		hql.append(" 		produtoEdicao.numeroEdicao as numeroEdicao, ");		
		hql.append(" 		lancamento.repartePromocional as materialPromocional, ");
		hql.append(" 		sum(estudoCota.qtdeEfetiva) as reparte, ");
		hql.append(" 		sum(estudoCota.qtdeEfetiva * produtoEdicao.precoVenda) as totalBox, ");
		hql.append(" 		produtoEdicao.precoVenda as precoCapa ");

		gerarFromWhereDadosAbastecimento(filtro, hql, param, paramList, statusLancamento);
		
		hql.append(" group by cota.id ");
		
		if (filtro.getExcluirProdutoSemReparte()!= null && filtro.getExcluirProdutoSemReparte()) {

			hql.append(" having sum(estudoCota.qtdeEfetiva) > 0 ");
		}
		
		gerarOrdenacaoDadosAbastecimento(filtro, hql);
				
		Query query =  getSession().createQuery(hql.toString());
				
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		for(String key : paramList.keySet()){
			query.setParameterList(key, paramList.get(key));
		}
		
		query.setParameterList("status", statusLancamento);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoAbastecimentoDTO.class));

		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
	}  
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long countObterMapaDeImpressaoPorProdutoQuebrandoPorCota(FiltroMapaAbastecimentoDTO filtro) {

		HashMap<String, Object> param = new HashMap<String, Object>();

		HashMap<String, List<String>> paramList = new HashMap<String, List<String>>();
		
		List<StatusLancamento> statusLancamento = new ArrayList<StatusLancamento>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select count(*) ");

		gerarFromWhereDadosAbastecimento(filtro, hql, param, paramList, statusLancamento);
		
		hql.append(" group by cota.id ");

		if (filtro.getExcluirProdutoSemReparte()!= null && filtro.getExcluirProdutoSemReparte()) {

			hql.append(" having sum(estudoCota.qtdeEfetiva) > 0 ");
		}
		
		Query query =  getSession().createQuery(hql.toString());
				
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
	
		for(String key : paramList.keySet()){
			query.setParameterList(key, paramList.get(key));
		}
		
		query.setParameterList("status", statusLancamento);
		
		@SuppressWarnings("unchecked")
		List<Long> count = query.list();
		
		return (long) ((count == null || count.isEmpty()) ? 0 : count.size());
	}

	@SuppressWarnings("unchecked")
	public List<MovimentoEstoqueCota> obterMovimentoEstoqueCotaPor(ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor, 
			Long idCota, GrupoNotaFiscal grupoNotaFiscal, List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoques, 
			Intervalo<Date> periodo, List<Long> listaFornecedores, List<Long> listaProdutos) {
		
		List<MovimentoEstoqueCota> result = new ArrayList<MovimentoEstoqueCota>();
		
		int qtdeIteracao = 
				(GrupoNotaFiscal.NF_DEVOLUCAO_SIMBOLICA.equals(grupoNotaFiscal) 
						|| GrupoNotaFiscal.NF_VENDA.equals(grupoNotaFiscal)) 
							? 2 : 1 ;
		int i = 0;
		while (i < qtdeIteracao) {
			StringBuffer sql = new StringBuffer("");
			
			sql.append(" SELECT DISTINCT movimentoEstoqueCota ")
			   .append(" FROM Lancamento lancamento ");
			
			if (i == 1 || GrupoNotaFiscal.NF_DEVOLUCAO_REMESSA_CONSIGNACAO.equals(grupoNotaFiscal)) {
				//MovimentoEstoqueCota dos lançamentos relacionados ao ChamadaEncalhe
				sql.append("   INNER JOIN lancamento.chamadaEncalhe chamadaEncalhe ")
				   .append("   INNER JOIN chamadaEncalhe.chamadaEncalheCotas chamadaEncalheCota ")
				   .append("   INNER JOIN chamadaEncalheCota.conferenciasEncalhe conferenciaEncalhe ")
				   .append("   INNER JOIN conferenciaEncalhe.movimentoEstoqueCota movimentoEstoqueCota ");
			} else {
				//MovimentoEstoqueCota dos lançamentos relacionados ao Estudo
				sql.append("   INNER JOIN lancamento.movimentoEstoqueCotas movimentoEstoqueCota ");
			}
			
			sql.append("   LEFT JOIN movimentoEstoqueCota.cota.fornecedores fornecedor ")
			   .append("   LEFT JOIN movimentoEstoqueCota.listaProdutoServicos produtoServico ")
			   .append("   LEFT JOIN produtoServico.produtoServicoPK.notaFiscal notaFiscal ")
			   .append("   LEFT JOIN notaFiscal.informacaoEletronica informacaoEletronica ")
			   .append("   LEFT JOIN informacaoEletronica.retornoComunicacaoEletronica retornoComunicacaoEletronica ")
			   .append("   LEFT JOIN notaFiscal.identificacao.tipoNotaFiscal tipoNotaFiscal ");
			
			sql.append(" WHERE movimentoEstoqueCota.status = :status ")
			   .append("   AND (retornoComunicacaoEletronica IS NULL OR retornoComunicacaoEletronica.status = :statusNFe)")
			   .append("   AND (notaFiscal IS NULL OR notaFiscal.statusProcessamentoInterno != :statusInterno)")
			   .append("   AND movimentoEstoqueCota.cota.id = :idCota ")
			   .append("   AND (tipoNotaFiscal IS NULL OR tipoNotaFiscal.grupoNotaFiscal != :grupoNotaFiscal) ");
	
			if (i == 1 || GrupoNotaFiscal.NF_DEVOLUCAO_REMESSA_CONSIGNACAO.equals(grupoNotaFiscal)) {
				sql.append("   AND (chamadaEncalhe.dataRecolhimento + :diasAMais) >= :diaAtual ")
				   .append("   AND (chamadaEncalheCota.fechado IS NULL OR chamadaEncalheCota.fechado = :fechado) ")
				   .append("   AND (chamadaEncalheCota.postergado IS NULL OR chamadaEncalheCota.postergado = :postergado) ");
			}
			
			if (periodo != null && periodo.getDe() != null && periodo.getAte() != null) {
				sql.append("   AND lancamento.dataLancamentoDistribuidor BETWEEN :dataInicio AND :dataFim ");
			}
			
			if (listaGrupoMovimentoEstoques != null && !listaGrupoMovimentoEstoques.isEmpty()) {
				sql.append("   AND movimentoEstoqueCota.tipoMovimento.grupoMovimentoEstoque IN (:listaGrupoMoviementoEstoque) ");
			}
			
			if (listaProdutos != null && !listaProdutos.isEmpty()) {
				sql.append("   AND movimentoEstoqueCota.produtoEdicao.produto.id IN (:listaProdutos) ");
			}
			
			if (listaFornecedores != null && !listaFornecedores.isEmpty()) {
				sql.append("   AND (fornecedor IS NULL OR fornecedor.id IN (:listaFornecedores)) ");
			}
			
			Query query = getSession().createQuery(sql.toString());
			
			query.setParameter("status", StatusAprovacao.APROVADO);
			query.setParameter("statusNFe", Status.CANCELAMENTO_HOMOLOGADO);
			query.setParameter("statusInterno", StatusProcessamentoInterno.NAO_GERADA);
			query.setParameter("idCota", idCota);
			query.setParameter("grupoNotaFiscal", grupoNotaFiscal);
			
			if (i == 1 || GrupoNotaFiscal.NF_DEVOLUCAO_REMESSA_CONSIGNACAO.equals(grupoNotaFiscal)) {
				double diasAMais;
				if (parametrosRecolhimentoDistribuidor.isDiaRecolhimentoQuinto()) {
					diasAMais = 4;
				} else if (parametrosRecolhimentoDistribuidor.isDiaRecolhimentoQuarto()) {
					diasAMais = 3;
				} else if (parametrosRecolhimentoDistribuidor.isDiaRecolhimentoTerceiro()) {
					diasAMais = 2;
				} else if (parametrosRecolhimentoDistribuidor.isDiaRecolhimentoSegundo()) {
					diasAMais = 1;
				} else {
					diasAMais = 0;
				}
				query.setParameter("diasAMais", diasAMais);
				query.setParameter("diaAtual", new Date());
				query.setParameter("fechado", false);
				query.setParameter("postergado", false);
			}

			if (listaProdutos != null && !listaProdutos.isEmpty()) {
				query.setParameterList("listaProdutos", listaProdutos);
			}
		
			if (listaFornecedores != null && !listaFornecedores.isEmpty()) {
				query.setParameterList("listaFornecedores", listaFornecedores);
			}
			
			if (periodo != null && periodo.getDe() != null && periodo.getAte() != null) {
				query.setParameter("dataInicio", periodo.getDe());
				query.setParameter("dataFim", periodo.getAte());
			}
			
			if (listaGrupoMovimentoEstoques != null && !listaGrupoMovimentoEstoques.isEmpty()) {
				query.setParameterList("listaGrupoMoviementoEstoque", listaGrupoMovimentoEstoques);
			}
			
			result.addAll(query.list());
			i++;
		}

		return result;
	}

	@Override
	public Long obterQuantidadeProdutoEdicaoMovimentadoPorCota(Long idCota, Long idProdutoEdicao, Long idTipoMovimento) {
		

		StringBuffer hql = new StringBuffer();
		
		hql.append(" select sum(movimentoEstoqueCota.qtde) ");			
		
		hql.append(" from MovimentoEstoqueCota movimentoEstoqueCota ");
		
		hql.append(" where movimentoEstoqueCota.produtoEdicao.id = :idProdutoEdicao and ");
		
		if(idCota != null)
			hql.append("  movimentoEstoqueCota.cota.id = :idCota and ");
				
		hql.append(" movimentoEstoqueCota.tipoMovimento.id = :idTipoMovimento ");
		
		Query query = getSession().createQuery(hql.toString());
		
		if(idCota != null)
			query.setParameter("idCota", idCota);
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		query.setParameter("idTipoMovimento", idTipoMovimento);
		
		BigInteger sum = (BigInteger) (query.uniqueResult() == null ? BigInteger.ZERO : query.uniqueResult());
		
		return sum.longValue();
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public  List<MovimentoEstoqueCota> obterMovimentoEstoqueCotaPor(Distribuidor distribuidor, Long idCota, List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoques, Intervalo<Date> periodo, List<Long> listaFornecedores){
		StringBuffer sql = new StringBuffer("SELECT DISTINCT movimentoEstoqueCota ");		
		sql.append(" FROM MovimentoEstoqueCota movimentoEstoqueCota ");
	
		sql.append("   JOIN movimentoEstoqueCota.lancamento lancamento ");
		sql.append("   JOIN movimentoEstoqueCota.cota cota ");
		
		
		sql.append("   JOIN movimentoEstoqueCota.produtoEdicao produtoEdicao ");
		sql.append("   JOIN produtoEdicao.produto produto ");
		sql.append("   JOIN produto.fornecedores fornecedor ");
		
		
		sql.append("   JOIN movimentoEstoqueCota.tipoMovimento tipoMovimento ");
		sql.append("   LEFT JOIN movimentoEstoqueCota.movimentoEstoqueCotaFuro movimentoEstoqueCotaFuro ");
		
		sql.append(" WHERE movimentoEstoqueCota.status = :status ")
		   .append("   AND cota.id = :idCota ")
		   .append(" AND movimentoEstoqueCotaFuro.id is null ");
		
		if (periodo != null && periodo.getDe() != null && periodo.getAte() != null) {
			sql.append("   AND lancamento.dataLancamentoDistribuidor BETWEEN :dataInicio AND :dataFim ");
		}
		
		if (listaGrupoMovimentoEstoques != null && !listaGrupoMovimentoEstoques.isEmpty()) {
			sql.append("   AND tipoMovimento.grupoMovimentoEstoque IN (:listaGrupoMoviementoEstoque) ");
		}		
		
		if (listaFornecedores != null && !listaFornecedores.isEmpty()) {
			sql.append("   AND (fornecedor IS NULL OR fornecedor.id IN (:listaFornecedores)) ");
		}		
		
		Query query = getSession().createQuery(sql.toString());
		query.setParameter("status", StatusAprovacao.APROVADO);
		query.setParameter("idCota", idCota);
			
	
		if (listaFornecedores != null && !listaFornecedores.isEmpty()) {
			query.setParameterList("listaFornecedores", listaFornecedores);
		}
		
		if (periodo != null && periodo.getDe() != null && periodo.getAte() != null) {
			query.setParameter("dataInicio", periodo.getDe());
			query.setParameter("dataFim", periodo.getAte());
		}
		
		if (listaGrupoMovimentoEstoques != null && !listaGrupoMovimentoEstoques.isEmpty()) {
			query.setParameterList("listaGrupoMoviementoEstoque", listaGrupoMovimentoEstoques);
		}		
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoAbastecimentoDTO> obterMapaDeAbastecimentoPorEntregador(
			FiltroMapaAbastecimentoDTO filtro) {

		HashMap<String, Object> param = new HashMap<String, Object>();
		
		HashMap<String, List<String>> paramList = new HashMap<String, List<String>>();
		
		List<StatusLancamento> statusLancamento = new ArrayList<StatusLancamento>();
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select ");
		
		hql.append(" 		produto.codigo as codigoProduto, ");
		hql.append(" 		produto.nome as nomeProduto, ");
		hql.append(" 		produtoEdicao.numeroEdicao as numeroEdicao, ");
		hql.append(" 		produtoEdicao.codigoDeBarras as codigoBarra, ");
		hql.append(" 		produtoEdicao.pacotePadrao as pacotePadrao, ");		
		hql.append(" 		sum(estudoCota.qtdeEfetiva) as reparte, ");
		hql.append(" 		produtoEdicao.precoVenda as precoCapa, ");
		hql.append(" 		cota.numeroCota as codigoCota, ");
		hql.append(" 		(CASE WHEN pessoa.class = 'J' THEN pessoa.razaoSocial ELSE pessoa.nome END) as nomeCota,");
	
		hql.append(" 		entregador.id as qtdeExms ");
		

		gerarFromWhereDadosAbastecimento(filtro, hql, param, paramList, statusLancamento);
		
		hql.append(" group by cota.id ");
		
		if (filtro.getExcluirProdutoSemReparte() != null && filtro.getExcluirProdutoSemReparte()) {

			hql.append(" having sum(estudoCota.qtdeEfetiva) > 0 ");
		}
		
		gerarOrdenacaoEntregador(filtro, hql);
				
		Query query =  getSession().createQuery(hql.toString());
				
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		for(String key : paramList.keySet()){
			query.setParameterList(key, paramList.get(key));
		}
		
		query.setParameterList("status", statusLancamento);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoAbastecimentoDTO.class));

		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
	}

	@Override
	public Long countObterMapaDeAbastecimentoPorEntregador(
			FiltroMapaAbastecimentoDTO filtro) {
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		HashMap<String, List<String>> paramList = new HashMap<String, List<String>>();
		
		List<StatusLancamento> statusLancamento = new ArrayList<StatusLancamento>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select count(*) ");
			
		gerarFromWhereDadosAbastecimento(filtro, hql, param, paramList, statusLancamento);
		
		hql.append(" group by cota.id ");
		
		if (filtro.getExcluirProdutoSemReparte() != null && filtro.getExcluirProdutoSemReparte()) {

			hql.append(" having sum(estudoCota.qtdeEfetiva) > 0 ");
		}
						
		Query query =  getSession().createQuery(hql.toString());
				
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		for(String key : paramList.keySet()){
			query.setParameterList(key, paramList.get(key));
		}
		
		query.setParameterList("status", statusLancamento);
		
		@SuppressWarnings("unchecked")
		List<Long> count = query.list();
		
		return (long) ((count == null || count.isEmpty()) ? 0 : count.size());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProdutoAbastecimentoDTO> obterMapaDeImpressaoPorEntregador(
			FiltroMapaAbastecimentoDTO filtro) {

		HashMap<String, Object> param = new HashMap<String, Object>();
		
		HashMap<String, List<String>> paramList = new HashMap<String, List<String>>();
		
		List<StatusLancamento> statusLancamento = new ArrayList<StatusLancamento>();
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select cota.numeroCota as codigoCota, ");
		hql.append(" 		pessoa.nome as nomeCota,");
		hql.append(" 		produto.codigo as codigoProduto, ");
		hql.append(" 		produto.nome as nomeProduto, ");
		hql.append(" 		produtoEdicao.numeroEdicao as numeroEdicao, ");		
		hql.append(" 		sum(estudoCota.qtdeEfetiva) as reparte, ");
		hql.append(" 		sum(estudoCota.qtdeEfetiva * produtoEdicao.precoVenda) as totalBox, ");
		hql.append(" 		produtoEdicao.precoVenda as precoCapa ");

		gerarFromWhereDadosAbastecimento(filtro, hql, param, paramList, statusLancamento);
		
		hql.append(" group by produto.id, cota.id ");
		
		if (filtro.getExcluirProdutoSemReparte()!=null && filtro.getExcluirProdutoSemReparte()) {

			hql.append(" having sum(estudoCota.qtdeEfetiva) > 0 ");
		}
		
		gerarOrdenacaoDadosAbastecimento(filtro, hql);
				
		Query query =  getSession().createQuery(hql.toString());
				
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		for(String key : paramList.keySet()){
			query.setParameterList(key, paramList.get(key));
		}
		
		query.setParameterList("status", statusLancamento);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutoAbastecimentoDTO.class));

		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MovimentoEstoqueCota> obterPorLancamento(Long idLancamento) {
		
		String hql = " select movimento from MovimentoEstoqueCota movimento "
				   + " join movimento.lancamento lancamento "
				   + " where lancamento.id = :idLancamento "; 
		
		Query query = super.getSession().createQuery(hql);
		
		query.setParameter("idLancamento", idLancamento);
		
		return query.list();
	}

	@Override
	public ValoresAplicados obterValoresAplicadosProdutoEdicao(Integer numeroCota,
			Long idProdutoEdicao, Date dataOperacao) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select mec.valoresAplicados ");
		
		hql.append(" from MovimentoEstoqueCota mec  ");
		
		hql.append(" where ");
		
		hql.append(" mec.cota.numeroCota = :numeroCota ");
		
		hql.append(" and mec.produtoEdicao.id = :idProdutoEdicao ");
		
		hql.append(" and mec.tipoMovimento.grupoMovimentoEstoque = :grupoMovimentoEstoque ");
		
		hql.append(" and mec.data <= :dataOperacao   ");

		hql.append(" order by mec.data desc ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("numeroCota", numeroCota);
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		query.setParameter("grupoMovimentoEstoque", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		
		query.setParameter("dataOperacao", dataOperacao);
		
		return (ValoresAplicados) query.setMaxResults(1).uniqueResult();
		
	}  
	
	@Override
	public Long obterIdProdutoEdicaoPorControleConferenciaEncalhe(Long idControleConferenciaEncalheCota){
		
		StringBuilder hql = new StringBuilder(" select produtoEdicao.id  ");
		
		hql.append(" from ConferenciaEncalhe conferenciaEncalhe ")
		   .append(" join conferenciaEncalhe.movimentoEstoqueCota movimentoEstoqueCota ")
		   .append(" join movimentoEstoqueCota.produtoEdicao produtoEdicao ")
		   .append(" WHERE conferenciaEncalhe.controleConferenciaEncalheCota.id = :idControleConferenciaEncalheCota ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setMaxResults(1);
		
		query.setParameter("idControleConferenciaEncalheCota", idControleConferenciaEncalheCota);
		
		return (Long) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MovimentoEstoqueCota> obterMovimentoEstoqueCotaSemEstudoPor(
			Long idCota, Intervalo<Date> periodo,
			List<Long> listaIdFornecedores,
			List<GrupoMovimentoEstoque> listaGruposMovimentoEstoqueCota) {

		StringBuilder sql = new StringBuilder();
		
		sql.append("   select mec from MovimentoEstoqueCota mec   ");
		sql.append("   join mec.cota cota   ");
		sql.append("   join mec.lancamento lancamento   ");
		sql.append("   join mec.tipoMovimento tm   ");
		sql.append("   join mec.produtoEdicao pe   ");
		sql.append("   join pe.produto p   ");
		sql.append("   join p.fornecedores f   ");
		sql.append("   where mec.estudoCota is null   ");
		
		if(listaGruposMovimentoEstoqueCota != null && !listaGruposMovimentoEstoqueCota.isEmpty())
			sql.append("  and tm.grupoMovimentoEstoque in (:gruposMovimentosEstoque)  ");
		
		if(idCota != null)
			sql.append("  and mec.cota.id = :cotaID  ");
		
		if(periodo != null)
			sql.append("  and lancamento.dataLancamentoDistribuidor between :dataInicial and :dataFinal ");
		
		if(listaIdFornecedores != null && !listaIdFornecedores.isEmpty())
			sql.append(" and f.id in (:fornecedoresID) ");
		
		Query query = this.getSession().createQuery(sql.toString());
		
		query.setParameterList("gruposMovimentosEstoque", listaGruposMovimentoEstoqueCota);
		query.setParameter("cotaID", idCota);
		query.setParameter("dataInicial", periodo.getDe());
		query.setParameter("dataFinal", periodo.getAte());
		query.setParameterList("fornecedoresID", listaIdFornecedores);
		
		return query.list();
	}

	@Override
	public List<MovimentoEstoqueCota> obterMovimentoEstoqueCotaSemEstudoPor(
			Long idCota, Intervalo<Date> periodo,
			List<Long> listaIdFornecedores,
			GrupoMovimentoEstoque grupoMovimentoEstoque) {
		
		List<GrupoMovimentoEstoque> listaGruposMovimentoEstoqueCota = new ArrayList<GrupoMovimentoEstoque>(); 
		listaGruposMovimentoEstoqueCota.add(grupoMovimentoEstoque);
		
		return this.obterMovimentoEstoqueCotaSemEstudoPor(idCota, periodo, listaIdFornecedores, listaGruposMovimentoEstoqueCota);
	}
}
