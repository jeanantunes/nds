package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
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
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.model.cadastro.CanalDistribuicao;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.movimentacao.ProdutoEdicaoSlip;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ConferenciaEncalheRepository;
import br.com.abril.nds.util.ItemAutoComplete;

@Repository
public class ConferenciaEncalheRepositoryImpl extends AbstractRepositoryModel<ConferenciaEncalhe, Long> implements ConferenciaEncalheRepository {

	public ConferenciaEncalheRepositoryImpl() {
		super(ConferenciaEncalhe.class);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Long> obterIdConferenciasExcluidas(Long idControleConfEncalheCota, List<Long> listaIdProdutoEdicaoConferidos) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT CONF.ID	AS id ");
		
		sql.append(" FROM CONTROLE_CONFERENCIA_ENCALHE_COTA CCEC ");
		
		sql.append(" INNER JOIN CONFERENCIA_ENCALHE CONF ON ");
		sql.append(" (CONF.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID = CCEC.ID)	");
		
		if(listaIdProdutoEdicaoConferidos!=null && !listaIdProdutoEdicaoConferidos.isEmpty()) {
			sql.append(" INNER JOIN PRODUTO_EDICAO PRODEDICAO ON 	");
			sql.append(" (PRODEDICAO.ID = CONF.PRODUTO_EDICAO_ID)	");
		}
		
		sql.append(" WHERE CCEC.ID = :idControleConfEncalheCota		");
		
		if(listaIdProdutoEdicaoConferidos!=null && !listaIdProdutoEdicaoConferidos.isEmpty()) {
			sql.append(" AND PRODEDICAO.ID NOT IN (:listaIdProdutoEdicaoConferidos)");
		}
		
		sql.append(" GROUP BY CONF.ID ");
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		((SQLQuery)query).addScalar("id", StandardBasicTypes.LONG);
		
		query.setParameter("idControleConfEncalheCota", idControleConfEncalheCota);
		
		if(listaIdProdutoEdicaoConferidos!=null && !listaIdProdutoEdicaoConferidos.isEmpty()) {
			query.setParameterList("listaIdProdutoEdicaoConferidos", listaIdProdutoEdicaoConferidos);
		}
		
		return query.list();
		
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
	@Override
	public List<CotaDTO> obterListaCotaConferenciaPendenciaErro(Date dataOperacao) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append("	 select 	");
		
		hql.append("	coalesce(pessoa.nome, pessoa.razaoSocial) as nomePessoa, ");
		
		hql.append("	cota.numeroCota as numeroCota	");
		
		hql.append("	from Semaforo semaforo,Cota cota ");
		
		hql.append("	join cota.pessoa pessoa	");
		
		hql.append("	where  cota.numeroCota = semaforo.numeroCota and semaforo.statusProcessoEncalhe != 'FINALIZADO' ");
		
		hql.append("	and semaforo.dataOperacao = :dataOperacao ");
		
		
		Query query = getSession().createQuery(hql.toString());
			
		query.setParameter("dataOperacao", dataOperacao); 
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<ProdutoEdicaoSlip> obterDadosSlipProdutoEdicaoAusenteConferenciaEncalhe(
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
		
		Query query =  this.getSession().createSQLQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(ProdutoEdicaoSlip.class));
		
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
	public List<ProdutoEdicaoSlip> obterDadosSlipConferenciaEncalhe(Long idControleConferenciaEncalheCota) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		
		hql.append(" conferencia.chamadaEncalheCota.chamadaEncalhe.id as idChamadaEncalhe, ");
		hql.append(" conferencia.movimentoEstoqueCota.produtoEdicao.produto.nome as nomeProduto,	");
		hql.append(" conferencia.movimentoEstoqueCota.produtoEdicao.produto.codigo as codigoProduto,	");
		hql.append(" conferencia.movimentoEstoqueCota.produtoEdicao.numeroEdicao as numeroEdicao,	");
		hql.append(" conferencia.movimentoEstoqueCota.produtoEdicao.id as idProdutoEdicao,			");
		hql.append(" conferencia.diaRecolhimento as dia,								");
		
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
		
		Query query =  this.getSession().createQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(ProdutoEdicaoSlip.class));
		
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
	
	public boolean isParcialNaoFinal(Long idProdutoEdicao) {
        
        String sql = this.getQueryIsLancamentoParcialFinal();
        
        Query query = this.getSession().createSQLQuery(sql);
        
        query.setParameter("idProdutoEdicao", idProdutoEdicao);
        query.setParameterList("statusEmRecolhimento", Arrays.asList(
                StatusLancamento.BALANCEADO_RECOLHIMENTO.name(),
                StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO.name(), 
                StatusLancamento.EM_RECOLHIMENTO.name()));

        String parcial = (String) query.uniqueResult();

        if(parcial == null || parcial.trim().isEmpty()) {
            return false;
        }
        
        return TipoLancamentoParcial.PARCIAL.name().equals(parcial) ? true : false;
    }
	
	private String getQueryIsLancamentoParcialFinal() {

		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT plp.TIPO as tipoLancamento ");
		sql.append(" FROM LANCAMENTO lanc "); 
		sql.append(" JOIN periodo_lancamento_parcial plp ON (plp.ID = lanc.PERIODO_LANCAMENTO_PARCIAL_ID) ");
		sql.append(" JOIN produto_edicao pe ON (lanc.PRODUTO_EDICAO_ID = pe.ID) ");
		sql.append(" JOIN produto p ON (p.ID = pe.PRODUTO_ID) ");
		sql.append(" WHERE lanc.PRODUTO_EDICAO_ID = :idProdutoEdicao ");
		
		sql.append(" AND lanc.status in (:statusEmRecolhimento)  ");
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
			List<Date> datasRecolhimento,
			boolean indFechado,
			boolean indPostergado,
			Set<Long> listaIdProdutoEdicao, boolean isOpDiferenciada) {

		StringBuffer hql = new StringBuffer();
		
		hql.append(" select	");
		
		hql.append(" PROD_EDICAO.ID as idProdutoEdicao,	");
		
		hql.append(" PROD_EDICAO.CODIGO_DE_BARRAS as codigoDeBarras, ");
		
		hql.append(" PROD_EDICAO.PACOTE_PADRAO as pacotePadrao, ");
		
		hql.append(" CH_ENCALHE.SEQUENCIA AS codigoSM, ");

		hql.append(" 0 AS qtdExemplar, ");
		
		hql.append(" CH_ENCALHE_COTA.PROCESSO_UTILIZA_NFE AS processoUtilizaNfe, ");
		
		hql.append(" CASE WHEN (PROD_EDICAO.GRUPO_PRODUTO in (:grupoProdutoCromo) OR TIPO_PRODUTO.GRUPO_PRODUTO in (:grupoProdutoCromo)) THEN true ELSE false END as isContagemPacote, ");
		
		hql.append(" CH_ENCALHE_COTA.QTDE_PREVISTA AS qtdReparte, ");
		
		hql.append(" 0 AS qtdInformada, ");
		hql.append(" 0 AS valorTotal, ");
		
		hql.append(" COALESCE(MEC.PRECO_VENDA, PROD_EDICAO.PRECO_VENDA, 0) AS precoCapaInformado, ");

		hql.append(" COALESCE(MEC.PRECO_COM_DESCONTO, 0) AS precoComDesconto, ");

		hql.append(" CASE WHEN ");
		hql.append(" (SELECT DISTINCT plp.TIPO ");
        hql.append(" FROM LANCAMENTO lanc  ");
        hql.append(" JOIN periodo_lancamento_parcial plp ON (plp.ID = lanc.PERIODO_LANCAMENTO_PARCIAL_ID) ");
        hql.append(" JOIN produto_edicao pe ON (lanc.PRODUTO_EDICAO_ID = pe.ID) ");
        hql.append(" WHERE lanc.PRODUTO_EDICAO_ID = PROD_EDICAO.ID ");
        hql.append(" AND plp.TIPO = 'PARCIAL' ");
        hql.append(" AND lanc.status IN (:statusEmRecolhimento)) IS NOT NULL THEN TRUE ELSE FALSE END AS parcialNaoFinal, ");
        
        hql.append(" LANCAM.DATA_LCTO_DISTRIBUIDOR AS dataLancamento,  	 ");
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

		hql.append(" 	LEFT JOIN CHAMADA_ENCALHE_LANCAMENTO CH_ENC_LANCAMENTO ON (CH_ENCALHE.ID = CH_ENC_LANCAMENTO.CHAMADA_ENCALHE_ID) ");
		hql.append(" 	LEFT JOIN (SELECT ID AS LANCAMENTO_ID, DATA_LCTO_DISTRIBUIDOR FROM LANCAMENTO LANCAM ) LANCAM ON LANCAM.LANCAMENTO_ID = CH_ENC_LANCAMENTO.LANCAMENTO_ID ");
		
		hql.append("	inner join PRODUTO_EDICAO as PROD_EDICAO ON ");
		hql.append("	(PROD_EDICAO.ID = CH_ENCALHE.PRODUTO_EDICAO_ID)	");

		hql.append("	inner join PRODUTO as PROD ON ");
		hql.append("	(PROD_EDICAO.PRODUTO_ID = PROD.ID)	");
		
		hql.append("	inner join TIPO_PRODUTO ON ");
		hql.append("	(TIPO_PRODUTO.ID = PROD.TIPO_PRODUTO_ID)	");
		
		hql.append("	inner join MOVIMENTO_ESTOQUE_COTA MEC ON ");
		hql.append("	MEC.COTA_ID = CH_ENCALHE_COTA.COTA_ID AND MEC.PRODUTO_EDICAO_ID = PROD_EDICAO.ID ");
		
		hql.append("	inner join TIPO_MOVIMENTO TIPO_MOV ON ");
		hql.append("	MEC.TIPO_MOVIMENTO_ID = TIPO_MOV.ID AND  TIPO_MOV.GRUPO_MOVIMENTO_ESTOQUE IN (:grupoMovimentoEstoque )");
		
		hql.append("  inner join CHAMADA_ENCALHE_LANCAMENTO CEL ON ");
        hql.append("    CEL.CHAMADA_ENCALHE_ID = CH_ENCALHE.ID ");
		
        hql.append("  inner join LANCAMENTO L ON ");
        hql.append("    L.ID = CEL.LANCAMENTO_ID ");
        
		hql.append("	WHERE   ");
		
		hql.append("    L.STATUS <> :lancamentoFechado  ");
		hql.append("	AND COTA.NUMERO_COTA = :numeroCota  ");
		hql.append("	AND CH_ENCALHE.DATA_RECOLHIMENTO IN (:datasRecolhimento)  ");
		
		if(!isOpDiferenciada){
			hql.append("	AND CH_ENCALHE_COTA.FECHADO = :indFechado 	");
			hql.append("	AND CH_ENCALHE_COTA.POSTERGADO = :indPostergado 	");
		}
		
		hql.append("	AND MEC.DATA = (SELECT 	MAX(MEC.DATA) ");
		hql.append("					FROM    MOVIMENTO_ESTOQUE_COTA MEC, TIPO_MOVIMENTO TIPO_MOV ");
		hql.append("					WHERE   MEC.COTA_ID = CH_ENCALHE_COTA.COTA_ID ");
		hql.append("					AND     MEC.PRODUTO_EDICAO_ID = PROD_EDICAO.ID ");
		hql.append("					AND 	MEC.TIPO_MOVIMENTO_ID = TIPO_MOV.ID ");
		hql.append("					AND     TIPO_MOV.GRUPO_MOVIMENTO_ESTOQUE IN( :grupoMovimentoEstoque)) ");
		
		if(listaIdProdutoEdicao!=null && !listaIdProdutoEdicao.isEmpty()) {
			
			hql.append(" AND CH_ENCALHE.PRODUTO_EDICAO_ID NOT IN (:listaIdProdutoEdicao) ");
			
		}
		
		hql.append("  	GROUP BY PROD_EDICAO.ID ");
		hql.append("  	ORDER BY CH_ENCALHE.DATA_RECOLHIMENTO, CH_ENCALHE.SEQUENCIA ");
		
		Query query =  this.getSession().createSQLQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(ConferenciaEncalheDTO.class));
		
		((SQLQuery)query).addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
		((SQLQuery)query).addScalar("codigoDeBarras");
		((SQLQuery)query).addScalar("codigoSM", StandardBasicTypes.INTEGER);
		
		((SQLQuery)query).addScalar("qtdExemplar", StandardBasicTypes.BIG_INTEGER);
		((SQLQuery)query).addScalar("processoUtilizaNfe", StandardBasicTypes.BOOLEAN);
		((SQLQuery)query).addScalar("isContagemPacote", StandardBasicTypes.BOOLEAN);
		
		((SQLQuery)query).addScalar("qtdReparte", StandardBasicTypes.BIG_INTEGER);
		
		((SQLQuery)query).addScalar("qtdInformada", StandardBasicTypes.BIG_INTEGER);
		((SQLQuery)query).addScalar("precoCapaInformado", StandardBasicTypes.BIG_DECIMAL);
		((SQLQuery)query).addScalar("precoComDesconto", StandardBasicTypes.BIG_DECIMAL);
		((SQLQuery)query).addScalar("valorTotal", StandardBasicTypes.BIG_DECIMAL);
		
		((SQLQuery)query).addScalar("pacotePadrao");

		((SQLQuery)query).addScalar("dataLancamento");
		((SQLQuery)query).addScalar("dataRecolhimento");
		((SQLQuery)query).addScalar("tipoChamadaEncalhe");
		((SQLQuery)query).addScalar("codigo");
		((SQLQuery)query).addScalar("nomeProduto");
		((SQLQuery)query).addScalar("numeroEdicao", StandardBasicTypes.LONG);
		((SQLQuery)query).addScalar("precoCapa");
		((SQLQuery)query).addScalar("parcialNaoFinal", StandardBasicTypes.BOOLEAN);
		((SQLQuery)query).addScalar("desconto");

		query.setParameter("lancamentoFechado", StatusLancamento.FECHADO.name());
		query.setParameter("numeroCota", numeroCota);
		query.setParameterList("datasRecolhimento", datasRecolhimento);
		
		if(!isOpDiferenciada){
			query.setParameter("indFechado", indFechado);
			query.setParameter("indPostergado", indPostergado);
		}
		
		query.setParameterList("grupoMovimentoEstoque", this.grupoMovimentoEstoqueCota());
		
		query.setParameterList("statusEmRecolhimento", Arrays.asList(
				StatusLancamento.BALANCEADO_RECOLHIMENTO.name(),
				StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO.name(), 
				StatusLancamento.EM_RECOLHIMENTO.name()));

		query.setParameterList("grupoProdutoCromo", Arrays.asList(GrupoProduto.CROMO.name(),GrupoProduto.CARDS.name()));
		
		
		if(listaIdProdutoEdicao!=null && !listaIdProdutoEdicao.isEmpty()) {
			
			query.setParameterList("listaIdProdutoEdicao", listaIdProdutoEdicao);
			
		}
		
		return query.list();
	}
	
	private List<String> grupoMovimentoEstoqueCota(){
		
		return Arrays.asList(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.name(),
				GrupoMovimentoEstoque.ALTERACAO_REPARTE_COTA.name(),
				GrupoMovimentoEstoque.FALTA_DE_COTA.name(),
				GrupoMovimentoEstoque.FALTA_EM_COTA.name(),
				GrupoMovimentoEstoque.SOBRA_DE_COTA.name(),
				GrupoMovimentoEstoque.SOBRA_EM_COTA.name(),
				GrupoMovimentoEstoque.COMPRA_ENCALHE.name(),
				GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR.name(),
				GrupoMovimentoEstoque.ESTORNO_COMPRA_ENCALHE.name(),
				GrupoMovimentoEstoque.ESTORNO_COMPRA_SUPLEMENTAR.name());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemAutoComplete> obterListaProdutoEdicaoParaRecolhimentoPorCodigoBarras(final Integer numeroCota
			, final String codigoBarras, final Date dataOperacao, List<Date> datasRecolhimentoValidas) {
		
		final StringBuilder hql = new StringBuilder(" select produtoEdicao.id as chave, ");
		hql.append(" produtoEdicao.codigoDeBarras as value, ");
		hql.append(" CONCAT(produtoEdicao.codigoDeBarras, ' - ', produto.nome,' - Ed.:', produtoEdicao.numeroEdicao) as label ");
		   
		hql.append(" from ChamadaEncalheCota cec		")
			.append(" inner join cec.chamadaEncalhe ce	")
			.append(" inner join cec.cota cota					 		")
			.append(" inner join ce.produtoEdicao produtoEdicao 		")
			.append(" inner join produtoEdicao.produto produto			")
			.append(" inner join produtoEdicao.lancamentos lancamentos 	")
			
			.append(" where ");

			hql.append(" cota.numeroCota = :numeroCota 				");
			
			hql.append(" and lancamentos.status != :lancamentoFechado ");
			
			hql.append(" and upper(produtoEdicao.codigoDeBarras) like upper(:codigoBarras) ");
			
			
			hql.append(" and (ce.dataRecolhimento = :dataOperacao ");
			
			if(!datasRecolhimentoValidas.isEmpty()) {
				hql.append(" 	or ce.dataRecolhimento in (:datasRecolhimentoValidas) ");
			}
			hql.append(" ) ");
			
			hql.append(" group by produtoEdicao.id			")
			   .append(" order by produto.nome asc,			")
			   .append(" produtoEdicao.numeroEdicao desc	");
		
		final Query query = this.getSession().createQuery(hql.toString());
		
		if(codigoBarras!=null && !codigoBarras.trim().isEmpty()) {
			
			query.setParameter("codigoBarras", codigoBarras + "%" );
		}
		
		query.setParameter("numeroCota", numeroCota);
		query.setParameter("lancamentoFechado", StatusLancamento.FECHADO);
		query.setParameter("dataOperacao", dataOperacao);
		
		if(!datasRecolhimentoValidas.isEmpty()) {
			query.setParameterList("datasRecolhimentoValidas", datasRecolhimentoValidas);
		}
		
		query.setResultTransformer(Transformers.aliasToBean(ItemAutoComplete.class));
		
		return query.list();
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ItemAutoComplete> obterListaProdutoEdicaoParaRecolhimentoPorCodigoBarras_CotaVarejo(final String codigoBarras) {
		
		final StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT  ");
		sql.append("   pe.ID as chave, ");
		sql.append("   pe.CODIGO_DE_BARRAS as value, ");
		sql.append("   CONCAT(pe.CODIGO_DE_BARRAS, ' - ', pd.NOME,' - Ed.:', pe.NUMERO_EDICAO) as label ");
		sql.append(" FROM produto_edicao pe  ");
		sql.append(" JOIN produto pd ");
		sql.append("   ON pe.PRODUTO_ID = pd.ID ");
		sql.append(" JOIN produto_fornecedor pf ");
		sql.append("   ON pf.PRODUTO_ID = pd.ID ");
		sql.append(" JOIN fornecedor f ");
		sql.append("   ON pf.fornecedores_ID = f.ID ");
		sql.append(" WHERE f.CANAL_DISTRIBUICAO = :canalVarejo ");
		sql.append(" 	and upper(pe.CODIGO_DE_BARRAS) like upper(:codigoBarras)  ");

		final Query query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("codigoBarras", codigoBarras + "%" );
		query.setParameter("canalVarejo", CanalDistribuicao.VAREJO.toString());
		
		query.setResultTransformer(Transformers.aliasToBean(ItemAutoComplete.class));
		
		return query.list();
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ItemAutoComplete> obterListaProdutoEdicaoParaRecolhimentoPorCodigoOuNome_CotaVarejo(String codigoOuNome) {
		
		final StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT  ");
		sql.append("   pe.ID as chave, ");
		sql.append("   pe.CODIGO_DE_BARRAS as value, ");
		sql.append("   CONCAT(pe.CODIGO_DE_BARRAS, ' - ', pd.NOME,' - Ed.:', pe.NUMERO_EDICAO) as label ");
		sql.append(" FROM produto_edicao pe  ");
		sql.append(" JOIN produto pd ");
		sql.append("   ON pe.PRODUTO_ID = pd.ID ");
		sql.append(" JOIN produto_fornecedor pf ");
		sql.append("   ON pf.PRODUTO_ID = pd.ID ");
		sql.append(" JOIN fornecedor f ");
		sql.append("   ON pf.fornecedores_ID = f.ID ");
		sql.append(" JOIN lancamento l  ");
		sql.append("   ON l.PRODUTO_EDICAO_ID = pe.ID ");
		sql.append(" WHERE f.CANAL_DISTRIBUICAO = :canalVarejo ");
		sql.append(" 	and (pd.nome like :codigoOuNome or pd.CODIGO like :codigoOuNome)  ");
		sql.append("    and l.STATUS in ('EM_RECOLHIMENTO', 'RECOLHIDO', 'FECHADO') ");

		final Query query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("codigoOuNome", codigoOuNome+"%");
		query.setParameter("canalVarejo", CanalDistribuicao.VAREJO.toString());
		
		query.setResultTransformer(Transformers.aliasToBean(ItemAutoComplete.class));
		
		return query.list();
		
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.ConferenciaEncalheRepository#obterListaConferenciaEncalheDTO(java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<ConferenciaEncalheDTO> obterListaConferenciaEncalheDTO(Long idControleConferenciaEncalheCota, Boolean processoUtilizaNFe) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT                                             		");
		
		hql.append(" CONF_ENCALHE.ID AS idConferenciaEncalhe,           		");
		
		hql.append(" CONF_ENCALHE.DIA_RECOLHIMENTO AS dia,           			");
		
		hql.append(" CONF_ENCALHE.QTDE AS qtdExemplar,                  		");
		
		hql.append(" CH_ENCALHE_COTA.PROCESSO_UTILIZA_NFE AS processoUtilizaNfe, ");
		
		hql.append(" CASE WHEN (PROD_EDICAO.GRUPO_PRODUTO in (:grupoProdutoCromo) OR TIPO_PRODUTO.GRUPO_PRODUTO in (:grupoProdutoCromo) ) THEN true ELSE false END as isContagemPacote, ");
		
		hql.append(" COALESCE(CH_ENCALHE_COTA.QTDE_PREVISTA, 0) AS qtdReparte, 				");
		
		hql.append(" CONF_ENCALHE.QTDE_INFORMADA AS qtdInformada,       		");
		hql.append(" CONF_ENCALHE.PRECO_CAPA_INFORMADO AS precoCapaInformado,   ");
		hql.append(" CONF_ENCALHE.PRODUTO_EDICAO_ID AS idProdutoEdicao, 		");
		hql.append(" PROD_EDICAO.CODIGO_DE_BARRAS AS codigoDeBarras,    		");
		
		hql.append(" PROD_EDICAO.CHAMADA_CAPA AS chamadaCapa,					");			
		hql.append(" PESSOA_EDITOR.RAZAO_SOCIAL AS nomeEditor,					");			
		hql.append(" PESSOA_FORNECEDOR.RAZAO_SOCIAL AS nomeFornecedor,			");			
		
		hql.append(" CH_ENCALHE.SEQUENCIA AS codigoSM, ");
		
		hql.append(" LANCAM.DATA_LCTO_DISTRIBUIDOR AS dataLancamento,  	 ");
		hql.append(" CH_ENCALHE.DATA_RECOLHIMENTO AS dataRecolhimento,  	 ");
		hql.append(" CH_ENCALHE.TIPO_CHAMADA_ENCALHE AS tipoChamadaEncalhe,	 ");
		hql.append(" PROD.CODIGO AS codigo,                                  ");
		hql.append(" PROD.NOME AS nomeProduto,                               ");
		hql.append(" PROD_EDICAO.NUMERO_EDICAO AS numeroEdicao,              ");
		
		hql.append(" COALESCE(MOV_ESTOQUE_COTA.PRECO_VENDA, PROD_EDICAO.PRECO_VENDA, 0) AS precoCapa, ");
		
		hql.append(" CASE WHEN ");
        hql.append(" (SELECT DISTINCT plp.TIPO ");
        hql.append(" FROM LANCAMENTO lanc  ");
        hql.append(" JOIN periodo_lancamento_parcial plp ON (plp.ID = lanc.PERIODO_LANCAMENTO_PARCIAL_ID) ");
        hql.append(" JOIN produto_edicao pe ON (lanc.PRODUTO_EDICAO_ID = pe.ID) ");
        hql.append(" WHERE lanc.PRODUTO_EDICAO_ID = PROD_EDICAO.ID ");
        hql.append(" AND plp.TIPO = 'PARCIAL' ");
        hql.append(" AND lanc.status IN (:statusEmRecolhimento)) IS NOT NULL THEN TRUE ELSE FALSE END AS parcialNaoFinal, ");
		
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
		
		hql.append(" 	LEFT JOIN CHAMADA_ENCALHE_LANCAMENTO CH_ENC_LANCAMENTO ON (CH_ENCALHE.ID = CH_ENC_LANCAMENTO.CHAMADA_ENCALHE_ID) ");
		hql.append(" 	LEFT JOIN (SELECT ID AS LANCAMENTO_ID, DATA_LCTO_DISTRIBUIDOR FROM LANCAMENTO LANCAM ) LANCAM ON LANCAM.LANCAMENTO_ID = CH_ENC_LANCAMENTO.LANCAMENTO_ID ");
		
		hql.append(" INNER JOIN MOVIMENTO_ESTOQUE_COTA MOV_ESTOQUE_COTA ON (MOV_ESTOQUE_COTA.ID = CONF_ENCALHE.MOVIMENTO_ESTOQUE_COTA_ID) ");
		hql.append(" INNER JOIN PRODUTO_EDICAO PROD_EDICAO ON ( CONF_ENCALHE.PRODUTO_EDICAO_ID=PROD_EDICAO.ID )           						");
		hql.append(" INNER JOIN PRODUTO PROD ON (PROD_EDICAO.PRODUTO_ID=PROD.ID)                         						");
		hql.append(" INNER JOIN TIPO_PRODUTO ON (TIPO_PRODUTO.ID = PROD.TIPO_PRODUTO_ID ) ");
		hql.append(" INNER JOIN CONTROLE_CONFERENCIA_ENCALHE_COTA CONTROLE_CONF_ENC_COTA ON (CONTROLE_CONF_ENC_COTA.ID = CONF_ENCALHE.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID)	");
		hql.append(" INNER JOIN PRODUTO_FORNECEDOR PROD_FORNEC ON (PROD.ID = PROD_FORNEC.PRODUTO_ID)	");
		hql.append(" INNER JOIN FORNECEDOR FORNECEDOR_0 ON (FORNECEDOR_0.ID = PROD_FORNEC.FORNECEDORES_ID) 	");
		hql.append(" INNER JOIN EDITOR EDITOR_0 ON (PROD.EDITOR_ID = EDITOR_0.ID)			");
		hql.append(" INNER JOIN PESSOA PESSOA_FORNECEDOR ON (FORNECEDOR_0.JURIDICA_ID = PESSOA_FORNECEDOR.ID) 	");
		hql.append(" INNER JOIN PESSOA PESSOA_EDITOR ON (EDITOR_0.JURIDICA_ID = PESSOA_EDITOR.ID) 		");
		
		hql.append(" WHERE  ");
		hql.append(" CONF_ENCALHE.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID = :idControleConferenciaEncalheCota   ");
		
		if(processoUtilizaNFe != null) {
			
			hql.append(" AND CH_ENCALHE_COTA.PROCESSO_UTILIZA_NFE = :processoUtilizaNfe  ");
		}
		
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
		((SQLQuery)query).addScalar("parcialNaoFinal", StandardBasicTypes.BOOLEAN);
		((SQLQuery)query).addScalar("pacotePadrao");
		((SQLQuery)query).addScalar("desconto");
		((SQLQuery)query).addScalar("precoComDesconto");
		((SQLQuery)query).addScalar("valorTotal");
		((SQLQuery)query).addScalar("dataConferencia");
		((SQLQuery)query).addScalar("processoUtilizaNfe", StandardBasicTypes.BOOLEAN);

		query.setParameter("idControleConferenciaEncalheCota", idControleConferenciaEncalheCota);
		
		if(processoUtilizaNFe != null) {
			if(processoUtilizaNFe) {
				
				query.setParameter("processoUtilizaNfe", true);
			} else {
				
				query.setParameter("processoUtilizaNfe", false);
			}
		}
		
		query.setParameterList("statusEmRecolhimento", Arrays.asList(
				StatusLancamento.BALANCEADO_RECOLHIMENTO.name(),
				StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO.name(), 
				StatusLancamento.EM_RECOLHIMENTO.name()));
		
		query.setParameterList("grupoProdutoCromo", Arrays.asList(GrupoProduto.CROMO.name(), GrupoProduto.CARDS.name()));
		
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
	
	@Override
	public void update(Long id, boolean juramentada, String observacao,
			BigInteger qtdeInformada, BigDecimal precoCapaInformado,
			Long idMovimentoEstoqueCota, Long idMovimentoEstoque,
			BigInteger qtde) {
		final StringBuilder sql = new StringBuilder();
		sql.append("UPDATE conferencia_encalhe ");
		sql.append("SET JURAMENTADA = :juramentada,");
		sql.append("OBSERVACAO = :observacao,");
		sql.append("PRECO_CAPA_INFORMADO = :precoCapaInformado,");
		sql.append("QTDE = :qtde,");
		sql.append("QTDE_INFORMADA = :qtdeInformada,");
		sql.append("MOVIMENTO_ESTOQUE_ID = :idMovimentoEstoque,");
		sql.append("MOVIMENTO_ESTOQUE_COTA_ID = :idMovimentoEstoqueCota ");
		sql.append("WHERE id = :id");
		this.getSession().createSQLQuery(sql.toString())
		.setParameter("juramentada", juramentada)
		.setParameter("observacao", observacao)
		.setParameter("precoCapaInformado", precoCapaInformado)
		.setParameter("qtde", qtde)
		.setParameter("qtdeInformada", qtdeInformada)
		.setParameter("idMovimentoEstoque", idMovimentoEstoque)
		.setParameter("idMovimentoEstoqueCota", idMovimentoEstoqueCota)
		.setParameter("id", id)
		.executeUpdate();

	}

	@Override
	public boolean obterProcessoUtilizaNfeConferenciaEncalheCota(Integer numeroCota, Date dataOperacao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT COUNT(0) as totalUtilizaNfe ")
		.append(" FROM CHAMADA_ENCALHE_COTA CH_ENCALHE_COTA ")
		.append(" INNER JOIN CHAMADA_ENCALHE CH_ENCALHE ON (CH_ENCALHE.ID = CH_ENCALHE_COTA.CHAMADA_ENCALHE_ID) ")
		.append(" LEFT JOIN CHAMADA_ENCALHE_LANCAMENTO CH_ENC_LANCAMENTO ON (CH_ENCALHE.ID = CH_ENC_LANCAMENTO.CHAMADA_ENCALHE_ID) ") 
		.append(" LEFT JOIN (SELECT ID AS LANCAMENTO_ID, DATA_LCTO_DISTRIBUIDOR ")
		.append(" FROM LANCAMENTO LANCAM ) LANCAM ON LANCAM.LANCAMENTO_ID = CH_ENC_LANCAMENTO.LANCAMENTO_ID ")
		.append(" INNER JOIN PRODUTO_EDICAO PROD_EDICAO ON ( CH_ENCALHE.PRODUTO_EDICAO_ID=PROD_EDICAO.ID ) ")
		.append(" INNER JOIN PRODUTO PROD ON (PROD_EDICAO.PRODUTO_ID=PROD.ID) ")                         						
		.append(" INNER JOIN PRODUTO_FORNECEDOR PROD_FORNEC ON (PROD.ID = PROD_FORNEC.PRODUTO_ID) ")	
		.append(" INNER JOIN FORNECEDOR FORNECEDOR_0 ON (FORNECEDOR_0.ID = PROD_FORNEC.FORNECEDORES_ID) ") 	
		.append(" INNER JOIN EDITOR EDITOR_0 ON (PROD.EDITOR_ID = EDITOR_0.ID) ")
		.append(" INNER JOIN PESSOA PESSOA_FORNECEDOR ON (FORNECEDOR_0.JURIDICA_ID = PESSOA_FORNECEDOR.ID) ")
		.append(" INNER JOIN PESSOA PESSOA_EDITOR ON (EDITOR_0.JURIDICA_ID = PESSOA_EDITOR.ID) ")
		.append(" INNER JOIN COTA c ON c.id = CH_ENCALHE_COTA.COTA_ID ")
		.append(" WHERE CH_ENCALHE.DATA_RECOLHIMENTO = :dataOperacao ")
		.append(" AND CH_ENCALHE_COTA.PROCESSO_UTILIZA_NFE = true ")
		.append(" AND c.NUMERO_COTA = :numeroCota ");
		
		Query query =  this.getSession().createSQLQuery(hql.toString());
		query.setParameter("dataOperacao", dataOperacao);
		query.setParameter("numeroCota", numeroCota);
		
		BigInteger qtd = (BigInteger) query.uniqueResult();
		return (boolean) (qtd != null && qtd.longValue() > 0);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> obterCotasVarejoConferenciaEncalhe(FiltroConsultaEncalheDTO filtro){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select c.numero_cota ");
		sql.append(" from conferencia_encalhe conf  ");
		sql.append(" join controle_conferencia_encalhe_cota ccec  ");
		sql.append("   ON conf.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID = ccec.ID ");
		sql.append(" join cota_fornecedor cf ");
		sql.append("   ON cf.FORNECEDOR_ID in (select id from fornecedor where CANAL_DISTRIBUICAO = :distribuicaoVarejo) and cf.COTA_ID = ccec.COTA_ID ");
		sql.append(" join cota c ");
		sql.append(" ON c.id = cf.COTA_ID ");
		sql.append(" where conf.DATA BETWEEN :dataDe AND :dataAte  ");
		sql.append(" group by ccec.COTA_ID ");
		
		Query query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("dataDe", filtro.getDataRecolhimentoInicial());
		query.setParameter("dataAte", filtro.getDataRecolhimentoFinal());
		query.setParameter("distribuicaoVarejo", CanalDistribuicao.VAREJO.toString());
		
		
		return (List<Integer>)query.list();
		
	}
	
}
