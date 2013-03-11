package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ComposicaoCobrancaSlipDTO;
import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.ProdutoEdicaoSlipDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ConferenciaEncalheRepository;

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
		
		hql.append(" (conferencia.movimentoEstoqueCota.valoresAplicados.precoVenda -	");
		hql.append(" ( conferencia.movimentoEstoqueCota.valoresAplicados.precoVenda * (conferencia.movimentoEstoqueCota.valoresAplicados.valorDesconto) / 100 )) as precoVenda,	");
		
		hql.append(" conferencia.movimentoEstoqueCota.qtde as encalhe, ");
		
		hql.append(" ((conferencia.movimentoEstoqueCota.produtoEdicao.precoVenda -  			");
		hql.append(" ( conferencia.movimentoEstoqueCota.produtoEdicao.precoVenda * (conferencia.movimentoEstoqueCota.valoresAplicados.valorDesconto) / 100 ))  ");
		hql.append(" * conferencia.movimentoEstoqueCota.qtde) as valorTotal, ");
		hql.append(" conferencia.controleConferenciaEncalheCota.dataOperacao as dataOperacao,");
		hql.append(" conferencia.chamadaEncalheCota.chamadaEncalhe.dataRecolhimento as dataRecolhimento");
		
		
		hql.append(" from ConferenciaEncalhe conferencia	");
		
		hql.append(" join conferencia.movimentoEstoqueCota.produtoEdicao.produto.fornecedores fornecedor ");

		hql.append(" where	");
		
		hql.append(" conferencia.controleConferenciaEncalheCota.id = :idControleConferenciaEncalheCota ");
		
		hql.append(" order by dataRecolhimento ");
		
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
	
	/**
     * Obtém composição de cobrança da cota na data de operação para a exibição no Slip
     * @param numeroCota
     * @param dataOperacao
     * @param tiposMovimentoFinanceiroIgnorados
     * @return List<ComposicaoCobrancaSlipDTO>
     */
	@SuppressWarnings("unchecked")
	@Override
	public List<ComposicaoCobrancaSlipDTO> obterComposicaoCobrancaSlip(Integer numeroCota, Date dataOperacao, List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados){
		
		StringBuilder hql = new StringBuilder(" select ");

		hql.append(" mfc.id as idMovimentoFinanceiro, ");
		
		hql.append(" tipoMovimento.descricao as descricao, ");
		
		hql.append(" (case when tipoMovimento.operacaoFinaceira = 'CREDITO' then 'C' else 'D' end) as operacaoFinanceira, ");
		
		hql.append(" coalesce(mfc.valor,0) as valor ");
		
		hql.append(" from MovimentoFinanceiroCota mfc ");
		
		hql.append(" join mfc.tipoMovimento tipoMovimento ");
        
		hql.append(" where ");
		
		hql.append(" mfc.data = :dataOperacao ");
		
		hql.append(" and mfc.status = :statusAprovado ");
		
		hql.append(" and mfc.cota.numeroCota = :numeroCota ");
		
		if(tiposMovimentoFinanceiroIgnorados!=null && !tiposMovimentoFinanceiroIgnorados.isEmpty()) {
			hql.append(" and mfc.tipoMovimento not in (:tiposMovimentoFinanceiroIgnorados) ");
		}
		
		hql.append(" and mfc.id not in ");
		
		hql.append(" (   ");
		
		hql.append("     select distinct(movimentos.id) ");

		hql.append("     from ConsolidadoFinanceiroCota c join c.movimentos movimentos ");
		
		hql.append("     where ");
		
		hql.append("     c.cota.numeroCota = :numeroCota  ");
		
		hql.append(" ) ");
		
		hql.append(" order by mfc.data ");
		
		Query query = this.getSession().createQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(ComposicaoCobrancaSlipDTO.class));
		
		query.setParameter("statusAprovado", StatusAprovacao.APROVADO);
		
		query.setParameter("numeroCota", numeroCota);
		
		query.setParameter("dataOperacao", dataOperacao);
		
		if(tiposMovimentoFinanceiroIgnorados!=null && !tiposMovimentoFinanceiroIgnorados.isEmpty()) {
			query.setParameterList("tiposMovimentoFinanceiroIgnorados", tiposMovimentoFinanceiroIgnorados);
		}
		
		return query.list();
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

		StringBuffer subSqlPrecoVenda = new StringBuffer();
		subSqlPrecoVenda.append(" ( SELECT ");
		subSqlPrecoVenda.append("	MEC.PRECO_VENDA ");
		subSqlPrecoVenda.append(" FROM 	");
		subSqlPrecoVenda.append(" MOVIMENTO_ESTOQUE_COTA MEC, TIPO_MOVIMENTO TIPO_MOV	");
		subSqlPrecoVenda.append(" WHERE  	");
		subSqlPrecoVenda.append(" MEC.COTA_ID = CH_ENCALHE_COTA.COTA_ID AND 					");
		subSqlPrecoVenda.append(" MEC.PRODUTO_EDICAO_ID = PROD_EDICAO.ID AND 	");
		subSqlPrecoVenda.append(" MEC.TIPO_MOVIMENTO_ID = TIPO_MOV.ID AND ");
		subSqlPrecoVenda.append(" TIPO_MOV.GRUPO_MOVIMENTO_ESTOQUE = :grupoMovimentoEstoque ");
		subSqlPrecoVenda.append(" ORDER BY MEC.DATA DESC ");
		subSqlPrecoVenda.append(" LIMIT 1 ) ");

		
		StringBuffer subSqlPrecoComDesconto = new StringBuffer();
		subSqlPrecoComDesconto.append(" ( SELECT ");
		subSqlPrecoComDesconto.append("	MEC.PRECO_COM_DESCONTO ");
		subSqlPrecoComDesconto.append(" FROM 	");
		subSqlPrecoComDesconto.append(" MOVIMENTO_ESTOQUE_COTA MEC, TIPO_MOVIMENTO TIPO_MOV	");
		subSqlPrecoComDesconto.append(" WHERE  	");
		subSqlPrecoComDesconto.append(" MEC.COTA_ID = CH_ENCALHE_COTA.COTA_ID AND 					");
		subSqlPrecoComDesconto.append(" MEC.PRODUTO_EDICAO_ID = PROD_EDICAO.ID AND 	");
		subSqlPrecoComDesconto.append(" MEC.TIPO_MOVIMENTO_ID = TIPO_MOV.ID AND ");
		subSqlPrecoComDesconto.append(" TIPO_MOV.GRUPO_MOVIMENTO_ESTOQUE = :grupoMovimentoEstoque ");
		subSqlPrecoComDesconto.append(" ORDER BY MEC.DATA DESC ");
		subSqlPrecoComDesconto.append(" LIMIT 1 ) ");
		
		
		StringBuffer subSqlDesconto = new StringBuffer();
		subSqlDesconto.append(" ( SELECT ");
		subSqlDesconto.append("	( MEC.PRECO_VENDA -  MEC.PRECO_COM_DESCONTO ) ");
		subSqlDesconto.append(" FROM 	");
		subSqlDesconto.append(" MOVIMENTO_ESTOQUE_COTA MEC, TIPO_MOVIMENTO TIPO_MOV	");
		subSqlDesconto.append(" WHERE  	");
		subSqlDesconto.append(" MEC.COTA_ID = CH_ENCALHE_COTA.COTA_ID AND 					");
		subSqlDesconto.append(" MEC.PRODUTO_EDICAO_ID = PROD_EDICAO.ID AND 	");
		subSqlDesconto.append(" MEC.TIPO_MOVIMENTO_ID = TIPO_MOV.ID AND ");
		subSqlDesconto.append(" TIPO_MOV.GRUPO_MOVIMENTO_ESTOQUE = :grupoMovimentoEstoque ");
		subSqlDesconto.append(" ORDER BY MEC.DATA DESC ");
		subSqlDesconto.append(" LIMIT 1 ) ");
		
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select	");
		
		hql.append(" PROD_EDICAO.ID as idProdutoEdicao,	");
		
		hql.append(" PROD_EDICAO.CODIGO_DE_BARRAS as codigoDeBarras, ");
		
		hql.append(" CH_ENCALHE.SEQUENCIA AS codigoSM, ");

		hql.append(" 0 AS qtdExemplar, 		");
		
		hql.append(" CH_ENCALHE_COTA.QTDE_PREVISTA AS qtdReparte, 		");
		
		hql.append(" 0 AS qtdInformada, ");
		hql.append( " 0 AS valorTotal, ");
		
		hql.append( " COALESCE(" + subSqlPrecoComDesconto.toString() + ", PROD_EDICAO.PRECO_VENDA, 0) AS precoCapaInformado, ");
		
		hql.append(" PROD_EDICAO.PARCIAL AS parcial,						 ");
		
		hql.append(" CH_ENCALHE.DATA_RECOLHIMENTO AS dataRecolhimento,  	 ");
		hql.append(" CH_ENCALHE.TIPO_CHAMADA_ENCALHE AS tipoChamadaEncalhe,	 ");
		hql.append(" PROD.CODIGO AS codigo,");
		hql.append(" PROD.NOME AS nomeProduto,                  ");
		
		hql.append(" PROD_EDICAO.NUMERO_EDICAO AS numeroEdicao, ");
		
		hql.append( " COALESCE(" + subSqlPrecoVenda.toString() + ", PROD_EDICAO.PRECO_VENDA, 0) AS precoCapa, ");
		
		hql.append( " COALESCE(" + subSqlDesconto.toString() + ", 0) AS desconto ");
		
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

		hql.append("	WHERE   ");
		
		hql.append("	COTA.NUMERO_COTA = :numeroCota AND ");
		hql.append("	CH_ENCALHE.DATA_RECOLHIMENTO = :dataRecolhimento AND ");
		hql.append("	CH_ENCALHE_COTA.FECHADO = :indFechado AND	");
		hql.append("	CH_ENCALHE_COTA.POSTERGADO = :indPostergado 	");
		
		if(listaIdProdutoEdicao!=null && !listaIdProdutoEdicao.isEmpty()) {
			
			hql.append(" AND CH_ENCALHE.PRODUTO_EDICAO_ID NOT IN (:listaIdProdutoEdicao) ");
			
		}
		
		hql.append("  	ORDER BY codigoSM ");
		
		Query query =  this.getSession().createSQLQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(ConferenciaEncalheDTO.class));
		
		((SQLQuery)query).addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
		((SQLQuery)query).addScalar("codigoDeBarras");
		((SQLQuery)query).addScalar("codigoSM", StandardBasicTypes.INTEGER);
		
		((SQLQuery)query).addScalar("qtdExemplar", StandardBasicTypes.BIG_INTEGER);
		
		((SQLQuery)query).addScalar("qtdReparte", StandardBasicTypes.BIG_INTEGER);
		
		((SQLQuery)query).addScalar("qtdInformada", StandardBasicTypes.BIG_INTEGER);
		((SQLQuery)query).addScalar("precoCapaInformado", StandardBasicTypes.BIG_DECIMAL);
		((SQLQuery)query).addScalar("valorTotal", StandardBasicTypes.BIG_DECIMAL);
		
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
		
		
		
		if(listaIdProdutoEdicao!=null && !listaIdProdutoEdicao.isEmpty()) {
			
			query.setParameterList("listaIdProdutoEdicao", listaIdProdutoEdicao);
			
		}
		
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
		hql.append(" CONF_ENCALHE.QTDE AS qtdExemplar,                  		");
		hql.append(" CH_ENCALHE_COTA.QTDE_PREVISTA AS qtdReparte, 				");
		
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
		
		hql.append(" TO_DAYS(CONTROLE_CONF_ENC_COTA.DATA_OPERACAO)-TO_DAYS(CH_ENCALHE.DATA_RECOLHIMENTO) + 1 AS dia,  ");
		hql.append(" CONF_ENCALHE.OBSERVACAO AS observacao, 	");
		hql.append(" CONF_ENCALHE.JURAMENTADA AS juramentada 	");

		hql.append(" FROM ");

		hql.append(" CONFERENCIA_ENCALHE CONF_ENCALHE,     						");
		hql.append(" MOVIMENTO_ESTOQUE_COTA MOV_ESTOQUE_COTA, 					");
		hql.append(" PRODUTO_EDICAO PROD_EDICAO,           						");
		hql.append(" PRODUTO PROD,                         						");
		hql.append(" CHAMADA_ENCALHE_COTA CH_ENCALHE_COTA, 						");
		hql.append(" CHAMADA_ENCALHE CH_ENCALHE,            					");
		hql.append(" CONTROLE_CONFERENCIA_ENCALHE_COTA CONTROLE_CONF_ENC_COTA,	");
		hql.append(" FORNECEDOR FORNECEDOR_0, 	");
		hql.append(" PRODUTO_FORNECEDOR PROD_FORNEC,	");
		hql.append(" EDITOR EDITOR_0,			");
		hql.append(" PESSOA PESSOA_FORNECEDOR, 	");
		hql.append(" PESSOA PESSOA_EDITOR 		");

		hql.append(" WHERE ");
		
		hql.append(" CONF_ENCALHE.PRODUTO_EDICAO_ID=PROD_EDICAO.ID           	 ");
		hql.append(" AND MOV_ESTOQUE_COTA.ID = CONF_ENCALHE.MOVIMENTO_ESTOQUE_COTA_ID ");
		hql.append(" AND PROD_EDICAO.PRODUTO_ID=PROD.ID                          ");
		hql.append(" AND CONF_ENCALHE.CHAMADA_ENCALHE_COTA_ID=CH_ENCALHE_COTA.ID ");
		hql.append(" AND CH_ENCALHE_COTA.CHAMADA_ENCALHE_ID=CH_ENCALHE.ID        ");
		hql.append(" AND CONF_ENCALHE.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID = :idControleConferenciaEncalheCota   ");
		hql.append(" AND CONTROLE_CONF_ENC_COTA.ID = CONF_ENCALHE.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID			 ");
		hql.append(" AND FORNECEDOR_0.ID = PROD_FORNEC.FORNECEDORES_ID	");
		hql.append(" AND PROD.ID = PROD_FORNEC.PRODUTO_ID				");
		hql.append(" AND PROD.EDITOR_ID = EDITOR_0.ID 					");
		
		hql.append(" AND FORNECEDOR_0.JURIDICA_ID = PESSOA_FORNECEDOR.ID 	");
		hql.append(" AND EDITOR_0.JURIDICA_ID = PESSOA_EDITOR.ID			");
		
		
		hql.append("  ORDER BY codigoSM ");
		
		Query query =  this.getSession().createSQLQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(ConferenciaEncalheDTO.class));
		
		((SQLQuery)query).addScalar("idConferenciaEncalhe", StandardBasicTypes.LONG);
		((SQLQuery)query).addScalar("qtdExemplar", StandardBasicTypes.BIG_INTEGER);
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
		((SQLQuery)query).addScalar("dia", StandardBasicTypes.INTEGER);

		
		query.setParameter("idControleConferenciaEncalheCota", idControleConferenciaEncalheCota);
		
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
