package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.ProdutoEdicaoSlipDTO;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ConferenciaEncalheRepository;
import br.com.abril.nds.util.ItemAutoComplete;

@Repository
public class ConferenciaEncalheRepositoryImpl extends
		AbstractRepositoryModel<ConferenciaEncalhe, Long> implements
		ConferenciaEncalheRepository {

	public ConferenciaEncalheRepositoryImpl() {
		super(ConferenciaEncalhe.class);
	}
	
	public BigInteger obterQtdeEncalhe(Long idConferenciaEncalhe) {
		

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select conf.qtde from ConferenciaEncalhe conf ")
			.append(" where conf.id = :idConferenciaEncalhe ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idConferenciaEncalhe", idConferenciaEncalhe);
		
	
		return (BigInteger) query.uniqueResult();
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.ConferenciaEncalheRepository#obterListaCotaConferenciaNaoFinalizada(java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CotaDTO> obterListaCotaConferenciaNaoFinalizada(Date dataOperacao) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append("	select 	");
		
		hql.append("	coalesce(pessoa.nome, pessoa.razaoSocial) as nomePessoa, ");
		
		hql.append("	cota.numeroCota as numeroCota	");
		
		hql.append("	from ControleConferenciaEncalheCota ccec ");
		
		hql.append("	join ccec.cota cota		");

		hql.append("	join cota.pessoa pessoa	");
		
		hql.append("	where ccec.status = :statusOperacao	");
		
		hql.append("	and ccec.dataOperacao = :dataOperacao ");
		
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("statusOperacao", StatusOperacao.EM_ANDAMENTO); 
		
		query.setParameter("dataOperacao", dataOperacao); 
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<ProdutoEdicaoSlipDTO> obterDadosSlipProdutoEdicaoAusenteConferenciaEncalhe(
			Long idCota,
			Date dataOperacao,
			boolean indPostergado,
			Set<Long> listaIdProdutoEdicao) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select	");

		hql.append(" CH_ENCALHE.ID AS idChamadaEncalhe, 			");
		hql.append(" PROD.NOME as nomeProduto,						");
		hql.append(" PROD_EDICAO.NUMERO_EDICAO as numeroEdicao,		");
		hql.append(" PROD_EDICAO.ID as idProdutoEdicao,				");
		hql.append(" COALESCE(MEC.PRECO_COM_DESCONTO, PROD_EDICAO.PRECO_VENDA, 0) AS precoVenda, ");
		hql.append(" CH_ENCALHE_COTA.QTDE_PREVISTA AS reparte, 		");
		hql.append(" 0 AS encalhe, 				");
		hql.append(" 0 AS valorTotal, 				");
		hql.append(" CH_ENCALHE.DATA_RECOLHIMENTO AS dataOperacao, ");
		hql.append(" CH_ENCALHE.DATA_RECOLHIMENTO AS dataRecolhimento	");
		
		hql.append("    FROM    ");
		
		hql.append("    CHAMADA_ENCALHE_COTA AS CH_ENCALHE_COTA 	");
		
		hql.append("	inner join COTA AS COTA ON 					");
		hql.append("	(COTA.ID = CH_ENCALHE_COTA.COTA_ID)			");
		
		hql.append("	inner join CHAMADA_ENCALHE AS CH_ENCALHE ON 			");
		hql.append("	(CH_ENCALHE_COTA.CHAMADA_ENCALHE_ID = CH_ENCALHE.ID)	");
		
		hql.append("	inner join PRODUTO_EDICAO as PROD_EDICAO ON 	");
		hql.append("	(PROD_EDICAO.ID = CH_ENCALHE.PRODUTO_EDICAO_ID)	");

		hql.append("	inner join PRODUTO as PROD ON ");
		hql.append("	(PROD_EDICAO.PRODUTO_ID = PROD.ID)	");
		
		hql.append("	inner join MOVIMENTO_ESTOQUE_COTA MEC on MEC.COTA_ID = CH_ENCALHE_COTA.COTA_ID	");
		hql.append("	        AND       MEC.PRODUTO_EDICAO_ID = PROD_EDICAO.ID	");
		hql.append("	inner join TIPO_MOVIMENTO TIPO_MOV on MEC.TIPO_MOVIMENTO_ID = TIPO_MOV.ID	"); 
		hql.append("	        AND  TIPO_MOV.GRUPO_MOVIMENTO_ESTOQUE = :grupoMovimentoEstoque	");

		hql.append("	WHERE   ");
		
		hql.append("	COTA.ID = :idCota AND ");
		hql.append("	CH_ENCALHE.DATA_RECOLHIMENTO = :dataOperacao AND 	");
		hql.append("	CH_ENCALHE_COTA.POSTERGADO = :indPostergado 		");
		
		if(listaIdProdutoEdicao!=null && !listaIdProdutoEdicao.isEmpty()) {
			
			hql.append(" AND CH_ENCALHE.PRODUTO_EDICAO_ID NOT IN (:listaIdProdutoEdicao) ");
			
		}
		
		hql.append("  	ORDER BY CH_ENCALHE.DATA_RECOLHIMENTO ");
		
		Query query =  this.getSession().createSQLQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(ProdutoEdicaoSlipDTO.class));
		
		((SQLQuery)query).addScalar("idChamadaEncalhe", StandardBasicTypes.LONG);
		((SQLQuery)query).addScalar("nomeProduto");
		((SQLQuery)query).addScalar("numeroEdicao", StandardBasicTypes.LONG);
		((SQLQuery)query).addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
		((SQLQuery)query).addScalar("precoVenda", StandardBasicTypes.BIG_DECIMAL);
		((SQLQuery)query).addScalar("encalhe", StandardBasicTypes.BIG_INTEGER);
		((SQLQuery)query).addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
		((SQLQuery)query).addScalar("valorTotal", StandardBasicTypes.BIG_DECIMAL);
		((SQLQuery)query).addScalar("dataOperacao");
		((SQLQuery)query).addScalar("dataRecolhimento");
		
		
		query.setParameter("idCota", idCota);
		query.setParameter("dataOperacao", dataOperacao);
		query.setParameter("indPostergado", indPostergado);
		query.setParameter("grupoMovimentoEstoque", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name());
		
		if(listaIdProdutoEdicao!=null && !listaIdProdutoEdicao.isEmpty()) {
			query.setParameterList("listaIdProdutoEdicao", listaIdProdutoEdicao);
		}
		
		return query.list();
		
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.ConferenciaEncalheRepository#obterDadosSlipConferenciaEncalhe(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<ProdutoEdicaoSlipDTO> obterDadosSlipConferenciaEncalhe(Long idControleConferenciaEncalheCota) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		
		hql.append(" conferencia.chamadaEncalheCota.chamadaEncalhe.id as idChamadaEncalhe, ");
		hql.append(" conferencia.movimentoEstoqueCota.produtoEdicao.produto.nome as nomeProduto,	");
		hql.append(" conferencia.movimentoEstoqueCota.produtoEdicao.numeroEdicao as numeroEdicao,	");
		hql.append(" conferencia.movimentoEstoqueCota.produtoEdicao.id as idProdutoEdicao,			");
		hql.append(" conferencia.diaRecolhimento as diaRecolhimento,								");
		
		hql.append(" coalesce(conferencia.movimentoEstoqueCota.valoresAplicados.precoComDesconto,  conferencia.movimentoEstoqueCota.produtoEdicao.precoVenda, 0) as precoVenda,	");
		
		hql.append(" conferencia.movimentoEstoqueCota.qtde as encalhe, ");
		
		hql.append(" conferencia.chamadaEncalheCota.qtdePrevista as reparte, ");
		
		hql.append(" ( coalesce(conferencia.movimentoEstoqueCota.valoresAplicados.precoComDesconto,  ");
		
		hql.append(" conferencia.movimentoEstoqueCota.produtoEdicao.precoVenda, 0) ");
		hql.append("  * conferencia.movimentoEstoqueCota.qtde ) as valorTotal,  ");
		
		hql.append(" conferencia.controleConferenciaEncalheCota.dataOperacao as dataOperacao,");
		hql.append(" conferencia.chamadaEncalheCota.chamadaEncalhe.dataRecolhimento as dataRecolhimento ");
		
		hql.append(" from ConferenciaEncalhe conferencia	");
		
		hql.append(" join conferencia.movimentoEstoqueCota.produtoEdicao.produto.fornecedores fornecedor ");

		hql.append(" where	");
		
		hql.append(" conferencia.controleConferenciaEncalheCota.id = :idControleConferenciaEncalheCota ");
		
		hql.append(" order by conferencia.chamadaEncalheCota.chamadaEncalhe.sequencia ");
		
		Query query =  this.getSession().createQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(ProdutoEdicaoSlipDTO.class));
		
		query.setParameter("idControleConferenciaEncalheCota", idControleConferenciaEncalheCota);
		
		return query.list();
	
	}
	
	/**
	 * Obtém a chamada de encalhe 'fechada' relacionada à um movimento financeiro
	 * @param idMovimentoDevolucao
	 * @return ChamadaEncalheCota
	 */
	@Override
	public ChamadaEncalheCota obterChamadaEncalheDevolucao(Long idMovimentoDevolucao){
		
		StringBuilder hql = new StringBuilder("");
		
		hql.append(" select cec ");

		hql.append(" from ConferenciaEncalhe ce, MovimentoFinanceiroCota mfc ");
		
		hql.append(" join ce.movimentoEstoqueCota mec ");
		
		hql.append(" join mfc.movimentos mov ");
		
		hql.append(" join ce.chamadaEncalheCota cec ");
		
		hql.append(" where mec.id = mov.id ");
		
		hql.append(" and cec.fechado = true ");
		
		hql.append(" and mfc.id = :idMov ");
		
		hql.append(" order by cec.id ");
		
		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("idMov", idMovimentoDevolucao);
		
		query.setMaxResults(1);
		
		return (ChamadaEncalheCota) query.uniqueResult();
	}
	

	public boolean isLancamentoParcial(Long idProdutoEdicao) {
		
		String sql = this.getQueryIsLancamentoParcialFinal();
		
		Query query = this.getSession().createSQLQuery(sql);
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setParameter("tipoLancamento", TipoLancamentoParcial.PARCIAL.toString());
		query.setParameter("grupoProdutoCromo", GrupoProduto.CROMO.toString());

		String parcial = (String) query.uniqueResult();

		return parcial != null ? TipoLancamentoParcial.PARCIAL.toString().equals(parcial) ? true : false : false;
	}
	
	private String getQueryIsLancamentoParcialFinal() {

		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT plp.TIPO as tipoLancamento");
		sql.append(" FROM LANCAMENTO lanc "); 
		sql.append(" JOIN periodo_lancamento_parcial plp ON (plp.ID = lanc.PERIODO_LANCAMENTO_PARCIAL_ID) ");
		sql.append(" JOIN produto_edicao pe ON (lanc.PRODUTO_EDICAO_ID = pe.ID) ");
		sql.append(" JOIN produto p ON (p.ID = pe.PRODUTO_ID) ");
		sql.append(" JOIN tipo_produto tp ON (tp.ID = p.TIPO_PRODUTO_ID) ");
		sql.append(" WHERE lanc.PRODUTO_EDICAO_ID = :idProdutoEdicao ");
		sql.append(" AND plp.TIPO = :tipoLancamento "); 
		sql.append(" AND tp.GRUPO_PRODUTO = :grupoProdutoCromo ");
		sql.append(" GROUP BY lanc.PRODUTO_EDICAO_ID ");

		return sql.toString();
	}

	/*
	 * (non-Javadoc) 
	 * @see br.com.abril.nds.repository.ConferenciaEncalheRepository#obterListaConferenciaEncalheDTOContingencia(java.lang.Long, java.lang.Integer, java.util.Date, java.util.Date, boolean, boolean, java.util.Set)
	 */
	@SuppressWarnings("unchecked")
	public List<ConferenciaEncalheDTO> obterListaConferenciaEncalheDTOContingencia(
			Integer numeroCota,
			Date dataRecolhimento,
			boolean indFechado,
			boolean indPostergado,
			Set<Long> listaIdProdutoEdicao) {

		StringBuffer hql = new StringBuffer();
		
		hql.append(" select	");
		
		hql.append(" PROD_EDICAO.ID as idProdutoEdicao,	");
		
		hql.append(" PROD_EDICAO.CODIGO_DE_BARRAS as codigoDeBarras, ");
		
		hql.append(" PROD_EDICAO.PACOTE_PADRAO as pacotePadrao, ");
		
		hql.append(" CH_ENCALHE.SEQUENCIA AS codigoSM, ");

		hql.append(" 0 AS qtdExemplar, ");
		
		hql.append(" CASE WHEN ");
		hql.append(" (SELECT plp.TIPO ");
		hql.append(" FROM LANCAMENTO lanc  ");
		hql.append(" JOIN periodo_lancamento_parcial plp ON (plp.ID = lanc.PERIODO_LANCAMENTO_PARCIAL_ID) ");
		hql.append(" JOIN produto_edicao pe ON (lanc.PRODUTO_EDICAO_ID = pe.ID) ");
		hql.append(" JOIN produto p ON (p.ID = pe.PRODUTO_ID) ");
		hql.append(" JOIN tipo_produto tp ON (tp.ID = p.TIPO_PRODUTO_ID) ");
		hql.append(" WHERE lanc.PRODUTO_EDICAO_ID = PROD_EDICAO.ID ");
		hql.append(" AND tp.GRUPO_PRODUTO = :grupoProdutoCromo ");
		hql.append(" AND plp.TIPO = 'FINAL' ");
		hql.append(" GROUP BY lanc.PRODUTO_EDICAO_ID) IS NULL THEN "); 
		hql.append(" CASE WHEN (SELECT plp.TIPO ");
		hql.append(" FROM LANCAMENTO lanc "); 
		hql.append(" JOIN periodo_lancamento_parcial plp ON (plp.ID = lanc.PERIODO_LANCAMENTO_PARCIAL_ID) ");
		hql.append(" JOIN produto_edicao pe ON (lanc.PRODUTO_EDICAO_ID = pe.ID) ");
		hql.append(" JOIN produto p ON (p.ID = pe.PRODUTO_ID) ");
		hql.append(" JOIN tipo_produto tp ON (tp.ID = p.TIPO_PRODUTO_ID) ");
		hql.append(" WHERE lanc.PRODUTO_EDICAO_ID = PROD_EDICAO.ID ");
		hql.append(" AND tp.GRUPO_PRODUTO = :grupoProdutoCromo ");
		hql.append(" AND plp.TIPO = 'PARCIAL' ");
		hql.append(" GROUP BY lanc.PRODUTO_EDICAO_ID) IS NOT NULL THEN true ");
		hql.append(" ELSE false END "); 
		hql.append(" ELSE false END AS isContagemPacote, ");
		
		hql.append(" CH_ENCALHE_COTA.QTDE_PREVISTA AS qtdReparte, ");
		
		hql.append(" 0 AS qtdInformada, ");
		hql.append(" 0 AS valorTotal, ");
		
		hql.append(" COALESCE(MEC.PRECO_COM_DESCONTO, PROD_EDICAO.PRECO_VENDA, 0) AS precoCapaInformado, ");

		hql.append(" COALESCE(MEC.PRECO_COM_DESCONTO, 0) AS precoComDesconto, ");

		hql.append(" PROD_EDICAO.PARCIAL AS parcial,						 ");
		
		hql.append(" CH_ENCALHE.DATA_RECOLHIMENTO AS dataRecolhimento,  	 ");
		hql.append(" CH_ENCALHE.TIPO_CHAMADA_ENCALHE AS tipoChamadaEncalhe,	 ");
		hql.append(" PROD.CODIGO AS codigo,");
		hql.append(" PROD.NOME AS nomeProduto,                  ");
		
		hql.append(" PROD_EDICAO.NUMERO_EDICAO AS numeroEdicao, ");
		
		hql.append(" COALESCE(MEC.PRECO_VENDA, PROD_EDICAO.PRECO_VENDA, 0) AS precoCapa, ");
		
		hql.append(" COALESCE(MEC.PRECO_VENDA - MEC.PRECO_COM_DESCONTO, 0) AS desconto ");
		
		hql.append("    FROM    ");
		
		hql.append("    CHAMADA_ENCALHE_COTA AS CH_ENCALHE_COTA 	");
		
		hql.append("	inner join COTA AS COTA ON ");
		hql.append("	(COTA.ID = CH_ENCALHE_COTA.COTA_ID)	");
		
		hql.append("	inner join CHAMADA_ENCALHE AS CH_ENCALHE ON ");
		hql.append("	(CH_ENCALHE_COTA.CHAMADA_ENCALHE_ID = CH_ENCALHE.ID)	");
		
		hql.append("	inner join PRODUTO_EDICAO as PROD_EDICAO ON ");
		hql.append("	(PROD_EDICAO.ID = CH_ENCALHE.PRODUTO_EDICAO_ID)	");

		hql.append("	inner join PRODUTO as PROD ON ");
		hql.append("	(PROD_EDICAO.PRODUTO_ID = PROD.ID)	");
		
		hql.append("	inner join MOVIMENTO_ESTOQUE_COTA MEC ON ");
		hql.append("	MEC.COTA_ID = CH_ENCALHE_COTA.COTA_ID AND MEC.PRODUTO_EDICAO_ID = PROD_EDICAO.ID ");
		
		hql.append("	inner join TIPO_MOVIMENTO TIPO_MOV ON ");
		hql.append("	MEC.TIPO_MOVIMENTO_ID = TIPO_MOV.ID AND  TIPO_MOV.GRUPO_MOVIMENTO_ESTOQUE = :grupoMovimentoEstoque ");
		

		hql.append("	WHERE   ");
		
		hql.append("	COTA.NUMERO_COTA = :numeroCota AND ");
		hql.append("	CH_ENCALHE.DATA_RECOLHIMENTO = :dataRecolhimento AND ");
		hql.append("	CH_ENCALHE_COTA.FECHADO = :indFechado AND	");
		hql.append("	CH_ENCALHE_COTA.POSTERGADO = :indPostergado 	");
		hql.append("	AND MEC.DATA = (SELECT 	MAX(MEC.DATA) ");
		hql.append("					FROM    MOVIMENTO_ESTOQUE_COTA MEC, TIPO_MOVIMENTO TIPO_MOV ");
		hql.append("					WHERE   MEC.COTA_ID = CH_ENCALHE_COTA.COTA_ID ");
		hql.append("					AND     MEC.PRODUTO_EDICAO_ID = PROD_EDICAO.ID ");
		hql.append("					AND 	MEC.TIPO_MOVIMENTO_ID = TIPO_MOV.ID ");
		hql.append("					AND     TIPO_MOV.GRUPO_MOVIMENTO_ESTOQUE = :grupoMovimentoEstoque) ");
		
		if(listaIdProdutoEdicao!=null && !listaIdProdutoEdicao.isEmpty()) {
			
			hql.append(" AND CH_ENCALHE.PRODUTO_EDICAO_ID NOT IN (:listaIdProdutoEdicao) ");
			
		}
		
		hql.append("  	ORDER BY codigoSM ");
		
		Query query =  this.getSession().createSQLQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(ConferenciaEncalheDTO.class));
		
		((SQLQuery)query).addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
		((SQLQuery)query).addScalar("codigoDeBarras");
		((SQLQuery)query).addScalar("codigoSM", StandardBasicTypes.INTEGER);
		
		((SQLQuery)query).addScalar("qtdExemplar", StandardBasicTypes.BIG_INTEGER);
		((SQLQuery)query).addScalar("isContagemPacote", StandardBasicTypes.BOOLEAN);
		
		((SQLQuery)query).addScalar("qtdReparte", StandardBasicTypes.BIG_INTEGER);
		
		((SQLQuery)query).addScalar("qtdInformada", StandardBasicTypes.BIG_INTEGER);
		((SQLQuery)query).addScalar("precoCapaInformado", StandardBasicTypes.BIG_DECIMAL);
		((SQLQuery)query).addScalar("precoComDesconto", StandardBasicTypes.BIG_DECIMAL);
		((SQLQuery)query).addScalar("valorTotal", StandardBasicTypes.BIG_DECIMAL);
		
		((SQLQuery)query).addScalar("pacotePadrao");

		((SQLQuery)query).addScalar("dataRecolhimento");
		((SQLQuery)query).addScalar("tipoChamadaEncalhe");
		((SQLQuery)query).addScalar("codigo");
		((SQLQuery)query).addScalar("nomeProduto");
		((SQLQuery)query).addScalar("numeroEdicao", StandardBasicTypes.LONG);
		((SQLQuery)query).addScalar("precoCapa");
		((SQLQuery)query).addScalar("parcial");
		((SQLQuery)query).addScalar("desconto");

		query.setParameter("numeroCota", numeroCota);
		query.setParameter("dataRecolhimento", dataRecolhimento);
		query.setParameter("indFechado", indFechado);
		query.setParameter("indPostergado", indPostergado);
		query.setParameter("grupoMovimentoEstoque", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name());
		query.setParameter("grupoProdutoCromo", GrupoProduto.CROMO.toString());
		
		
		if(listaIdProdutoEdicao!=null && !listaIdProdutoEdicao.isEmpty()) {
			
			query.setParameterList("listaIdProdutoEdicao", listaIdProdutoEdicao);
			
		}
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemAutoComplete> obterListaProdutoEdicaoParaRecolhimentoPorCodigoBarras(Integer numeroCota, String codigoBarras) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select	 ");
		
		sql.append(" PRODUTO_EDICAO.ID as chave,	");
		sql.append(" PRODUTO_EDICAO.CODIGO_DE_BARRAS as value, ");
		sql.append(" CONCAT(PRODUTO_EDICAO.CODIGO_DE_BARRAS, ' - ', PRODUTO.NOME,' - Ed.:', PRODUTO_EDICAO.NUMERO_EDICAO) as label ");
		
		sql.append("    FROM COTA, MOVIMENTO_ESTOQUE_COTA, TIPO_MOVIMENTO, PRODUTO_EDICAO ");
		
		sql.append("	inner join PRODUTO ON ");
		sql.append("	(PRODUTO_EDICAO.PRODUTO_ID = PRODUTO.ID)	");
		
		sql.append("	WHERE   ");

		sql.append("	MOVIMENTO_ESTOQUE_COTA.COTA_ID = COTA.ID AND  							");
		
		sql.append("	MOVIMENTO_ESTOQUE_COTA.PRODUTO_EDICAO_ID = PRODUTO_EDICAO.ID AND		");

		sql.append("	MOVIMENTO_ESTOQUE_COTA.TIPO_MOVIMENTO_ID = TIPO_MOVIMENTO.ID AND  		");
		
		sql.append("	TIPO_MOVIMENTO.GRUPO_MOVIMENTO_ESTOQUE = :grupoMovimentoEstoque  AND	");
		
		sql.append("	UPPER(PRODUTO_EDICAO.CODIGO_DE_BARRAS) like :codigoBarras AND			");

		sql.append("	COTA.NUMERO_COTA = :numeroCota ");

		sql.append(" GROUP BY PRODUTO_EDICAO.ID ");
		
		sql.append(" ORDER BY label ");
		
		Query query =  this.getSession().createSQLQuery(sql.toString());

		query.setParameter("codigoBarras", codigoBarras.toUpperCase() + "%");
		query.setParameter("grupoMovimentoEstoque", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name());
		query.setParameter("numeroCota", numeroCota);

		query.setResultTransformer(Transformers.aliasToBean(ItemAutoComplete.class));
		
		return query.list();
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.ConferenciaEncalheRepository#obterListaConferenciaEncalheDTO(java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<ConferenciaEncalheDTO> obterListaConferenciaEncalheDTO(Long idControleConferenciaEncalheCota) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT                                             		");
		
		hql.append(" CONF_ENCALHE.ID AS idConferenciaEncalhe,           		");
		
		hql.append(" CONF_ENCALHE.DIA_RECOLHIMENTO AS dia,           			");
		
		hql.append(" CONF_ENCALHE.QTDE AS qtdExemplar,                  		");
		
		hql.append(" CASE WHEN ");
		
		hql.append(" (SELECT plp.TIPO ");
		hql.append(" FROM LANCAMENTO lanc  ");
		hql.append(" JOIN periodo_lancamento_parcial plp ON (plp.ID = lanc.PERIODO_LANCAMENTO_PARCIAL_ID) ");
		hql.append(" JOIN produto_edicao pe ON (lanc.PRODUTO_EDICAO_ID = pe.ID) ");
		hql.append(" JOIN produto p ON (p.ID = pe.PRODUTO_ID) ");
		hql.append(" JOIN tipo_produto tp ON (tp.ID = p.TIPO_PRODUTO_ID) ");
		hql.append(" WHERE lanc.PRODUTO_EDICAO_ID = PROD_EDICAO.ID ");
		hql.append(" AND tp.GRUPO_PRODUTO = :grupoProdutoCromo ");
		hql.append(" AND plp.TIPO = 'FINAL' ");
		hql.append(" GROUP BY lanc.PRODUTO_EDICAO_ID) IS NULL THEN "); 
		
		hql.append(" CASE WHEN (SELECT plp.TIPO ");
		hql.append(" FROM LANCAMENTO lanc "); 
		hql.append(" JOIN periodo_lancamento_parcial plp ON (plp.ID = lanc.PERIODO_LANCAMENTO_PARCIAL_ID) ");
		hql.append(" JOIN produto_edicao pe ON (lanc.PRODUTO_EDICAO_ID = pe.ID) ");
		hql.append(" JOIN produto p ON (p.ID = pe.PRODUTO_ID) ");
		hql.append(" JOIN tipo_produto tp ON (tp.ID = p.TIPO_PRODUTO_ID) ");
		hql.append(" WHERE lanc.PRODUTO_EDICAO_ID = PROD_EDICAO.ID ");
		hql.append(" AND tp.GRUPO_PRODUTO = :grupoProdutoCromo ");
		hql.append(" AND plp.TIPO = 'PARCIAL' ");
		hql.append(" GROUP BY lanc.PRODUTO_EDICAO_ID) IS NOT NULL THEN true ");
		
		hql.append(" ELSE false END "); 
		hql.append(" ELSE false END AS isContagemPacote, ");
		
		hql.append(" COALESCE(CH_ENCALHE_COTA.QTDE_PREVISTA, 0) AS qtdReparte, 				");
		
		hql.append(" CONF_ENCALHE.QTDE_INFORMADA AS qtdInformada,       		");
		hql.append(" CONF_ENCALHE.PRECO_CAPA_INFORMADO AS precoCapaInformado,   ");
		hql.append(" CONF_ENCALHE.PRODUTO_EDICAO_ID AS idProdutoEdicao, 		");
		hql.append(" PROD_EDICAO.CODIGO_DE_BARRAS AS codigoDeBarras,    		");
		
		hql.append(" PROD_EDICAO.CHAMADA_CAPA AS chamadaCapa,					");			
		hql.append(" PESSOA_EDITOR.RAZAO_SOCIAL AS nomeEditor,					");			
		hql.append(" PESSOA_FORNECEDOR.RAZAO_SOCIAL AS nomeFornecedor,			");			
		
		hql.append(" CH_ENCALHE.SEQUENCIA AS codigoSM, ");
		
		hql.append(" CH_ENCALHE.DATA_RECOLHIMENTO AS dataRecolhimento,  	 ");
		hql.append(" CH_ENCALHE.TIPO_CHAMADA_ENCALHE AS tipoChamadaEncalhe,	 ");
		hql.append(" PROD.CODIGO AS codigo,                                  ");
		hql.append(" PROD.NOME AS nomeProduto,                               ");
		hql.append(" PROD_EDICAO.NUMERO_EDICAO AS numeroEdicao,              ");
		
		hql.append(" COALESCE(MOV_ESTOQUE_COTA.PRECO_VENDA, PROD_EDICAO.PRECO_VENDA, 0) AS precoCapa, ");
		
		hql.append(" PROD_EDICAO.PARCIAL AS parcial, 						 ");
		hql.append(" PROD_EDICAO.PACOTE_PADRAO AS pacotePadrao,              ");
		
		hql.append(" COALESCE(MOV_ESTOQUE_COTA.PRECO_COM_DESCONTO, 0) AS precoComDesconto, ");
		
		hql.append(" COALESCE( ( COALESCE(MOV_ESTOQUE_COTA.PRECO_VENDA, 0) - COALESCE(MOV_ESTOQUE_COTA.PRECO_COM_DESCONTO, 0)), 0 ) AS desconto, ");
		
		hql.append(" CONF_ENCALHE.QTDE * ( ");
		hql.append(" COALESCE(MOV_ESTOQUE_COTA.PRECO_COM_DESCONTO, PROD_EDICAO.PRECO_VENDA, 0)  ");
		hql.append(" ) AS valorTotal, ");
		
		hql.append(" CONTROLE_CONF_ENC_COTA.DATA_OPERACAO AS dataConferencia,  ");
		hql.append(" CONF_ENCALHE.OBSERVACAO AS observacao, 	");
		hql.append(" CONF_ENCALHE.JURAMENTADA AS juramentada 	");

		hql.append(" FROM ");

		hql.append(" CONFERENCIA_ENCALHE CONF_ENCALHE ");
		
		hql.append(" LEFT JOIN CHAMADA_ENCALHE_COTA CH_ENCALHE_COTA ON (CH_ENCALHE_COTA.ID = CONF_ENCALHE.CHAMADA_ENCALHE_COTA_ID)	");
		hql.append(" LEFT JOIN CHAMADA_ENCALHE CH_ENCALHE ON (CH_ENCALHE.ID = CH_ENCALHE_COTA.CHAMADA_ENCALHE_ID) ");
		hql.append(" INNER JOIN MOVIMENTO_ESTOQUE_COTA MOV_ESTOQUE_COTA ON (MOV_ESTOQUE_COTA.ID = CONF_ENCALHE.MOVIMENTO_ESTOQUE_COTA_ID) ");
		hql.append(" INNER JOIN PRODUTO_EDICAO PROD_EDICAO ON ( CONF_ENCALHE.PRODUTO_EDICAO_ID=PROD_EDICAO.ID )           						");
		hql.append(" INNER JOIN PRODUTO PROD ON (PROD_EDICAO.PRODUTO_ID=PROD.ID)                         						");
		hql.append(" INNER JOIN CONTROLE_CONFERENCIA_ENCALHE_COTA CONTROLE_CONF_ENC_COTA ON (CONTROLE_CONF_ENC_COTA.ID = CONF_ENCALHE.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID)	");
		hql.append(" INNER JOIN PRODUTO_FORNECEDOR PROD_FORNEC ON (PROD.ID = PROD_FORNEC.PRODUTO_ID)	");
		hql.append(" INNER JOIN FORNECEDOR FORNECEDOR_0 ON (FORNECEDOR_0.ID = PROD_FORNEC.FORNECEDORES_ID) 	");
		hql.append(" INNER JOIN EDITOR EDITOR_0 ON (PROD.EDITOR_ID = EDITOR_0.ID)			");
		hql.append(" INNER JOIN PESSOA PESSOA_FORNECEDOR ON (FORNECEDOR_0.JURIDICA_ID = PESSOA_FORNECEDOR.ID) 	");
		hql.append(" INNER JOIN PESSOA PESSOA_EDITOR ON (EDITOR_0.JURIDICA_ID = PESSOA_EDITOR.ID) 		");
		
		hql.append(" WHERE ");
		hql.append(" CONF_ENCALHE.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID = :idControleConferenciaEncalheCota   ");
		hql.append(" ORDER BY dataRecolhimento desc,  codigoSM ");
		
		Query query =  this.getSession().createSQLQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(ConferenciaEncalheDTO.class));
		
		((SQLQuery)query).addScalar("idConferenciaEncalhe", StandardBasicTypes.LONG);
		((SQLQuery)query).addScalar("dia", StandardBasicTypes.INTEGER);
		((SQLQuery)query).addScalar("qtdExemplar", StandardBasicTypes.BIG_INTEGER);
		((SQLQuery)query).addScalar("isContagemPacote", StandardBasicTypes.BOOLEAN);
		((SQLQuery)query).addScalar("qtdReparte", StandardBasicTypes.BIG_INTEGER);
		((SQLQuery)query).addScalar("qtdInformada", StandardBasicTypes.BIG_INTEGER);
		((SQLQuery)query).addScalar("juramentada");
		((SQLQuery)query).addScalar("precoCapaInformado");
		((SQLQuery)query).addScalar("tipoChamadaEncalhe");
		((SQLQuery)query).addScalar("chamadaCapa");			
		((SQLQuery)query).addScalar("nomeEditor");			
		((SQLQuery)query).addScalar("nomeFornecedor");	
		((SQLQuery)query).addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
		((SQLQuery)query).addScalar("codigoDeBarras");
		((SQLQuery)query).addScalar("codigoSM", StandardBasicTypes.INTEGER);
		((SQLQuery)query).addScalar("dataRecolhimento");
		((SQLQuery)query).addScalar("codigo");
		((SQLQuery)query).addScalar("nomeProduto");
		((SQLQuery)query).addScalar("observacao");
		((SQLQuery)query).addScalar("numeroEdicao", StandardBasicTypes.LONG);
		((SQLQuery)query).addScalar("precoCapa");
		((SQLQuery)query).addScalar("parcial");
		((SQLQuery)query).addScalar("pacotePadrao");
		((SQLQuery)query).addScalar("desconto");
		((SQLQuery)query).addScalar("precoComDesconto");
		((SQLQuery)query).addScalar("valorTotal");
		((SQLQuery)query).addScalar("dataConferencia");

		
		query.setParameter("idControleConferenciaEncalheCota", idControleConferenciaEncalheCota);
		query.setParameter("grupoProdutoCromo", GrupoProduto.CROMO.toString());
		
		return query.list();
		        		
	}

	/**
	 * Obtém o valorTotal de uma operação de conferência de encalhe. Para o calculo do valor
	 * é levado em conta o preco com desconto de acordo com a regra de comissão que verifica 
	 * desconto no níveis de produtoedicao, cota.
	 * 
	 * @param idControleConferenciaEncalhe
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal obterValorTotalEncalheOperacaoConferenciaEncalhe(Long idControleConferenciaEncalhe) {
	
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select sum( ");
		hql.append(" conferencia.qtde * coalesce(mec.valoresAplicados.precoComDesconto, 0) ");
		hql.append(" ) ");
		
		hql.append(" from ConferenciaEncalhe conferencia  ");
		hql.append(" join conferencia.movimentoEstoqueCota mec ");
		
		hql.append(" WHERE conferencia.controleConferenciaEncalheCota.id = :idControleConferenciaEncalhe  ");
		
		Query query =  this.getSession().createQuery(hql.toString());
		
		query.setParameter("idControleConferenciaEncalhe", idControleConferenciaEncalhe);
		
		
		return (BigDecimal) query.uniqueResult();
	}

	@Override
	public BigInteger obterReparteConferencia(Long idCota, Long idControleConferenciaEncCota, Long produtoEdicaoId) {
		
		StringBuilder hql = new StringBuilder("select sum(movEst.qtde) ");
		hql.append(" from ControleConferenciaEncalheCota contConfEncCota ")
		   .append(" join contConfEncCota.conferenciasEncalhe confEnc ")
		   .append(" join confEnc.produtoEdicao produtoEdicao ")
		   .append(" join confEnc.chamadaEncalheCota chamEncCota ")
		   .append(" join chamEncCota.chamadaEncalhe chamEnc ")
		   .append(" join chamEnc.lancamentos lanc ")
		   .append(" join lanc.movimentoEstoqueCotas movEst ")
		   .append(" join movEst.cota cota ")
		   .append(" where cota.id = :idCota ")
		   .append(" and contConfEncCota.id = :idControleConferenciaEncCota ")
		   .append(" and produtoEdicao.id = :produtoEdicaoId ")
		   .append(" and movEst.tipoMovimento.grupoMovimentoEstoque = :grupoMovimento");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idCota", idCota);
		query.setParameter("idControleConferenciaEncCota", idControleConferenciaEncCota);
		query.setParameter("produtoEdicaoId", produtoEdicaoId);
		query.setParameter("grupoMovimento", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
		
		return (BigInteger) query.uniqueResult();
	}
}
