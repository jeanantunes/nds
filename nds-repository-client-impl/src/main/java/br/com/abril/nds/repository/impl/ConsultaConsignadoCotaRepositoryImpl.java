package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaConsignadoCotaDTO;
import br.com.abril.nds.dto.ConsultaConsignadoCotaPeloFornecedorDTO;
import br.com.abril.nds.dto.TotalConsultaConsignadoCotaDetalhado;
import br.com.abril.nds.dto.filtro.FiltroConsultaConsignadoCotaDTO;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.StatusEstoqueFinanceiro;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ConsultaConsignadoCotaRepository;

@Repository
public class ConsultaConsignadoCotaRepositoryImpl extends AbstractRepositoryModel<MovimentoEstoqueCota, Long> implements
		ConsultaConsignadoCotaRepository {

	public ConsultaConsignadoCotaRepositoryImpl() {
		super(MovimentoEstoqueCota.class);
	}

	@Autowired
	private DataSource dataSource;
	
	private StringBuilder getSQLDescontoLogistica(){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" ( ");
		
		sql.append("    CASE WHEN PE.DESCONTO_LOGISTICA_ID IS NOT NULL THEN ");
		
		sql.append("    ( ");
		
		sql.append("       SELECT DL.PERCENTUAL_DESCONTO ");
		
		sql.append("       FROM DESCONTO_LOGISTICA DL");
		
		sql.append("       WHERE DL.ID = PE.DESCONTO_LOGISTICA_ID ");
		
		sql.append("    ) ");
		
		sql.append("    ELSE ");
		
		sql.append("    ( ");
		
        sql.append("       CASE WHEN PR.DESCONTO_LOGISTICA_ID IS NOT NULL THEN ");
		
        sql.append("       ( ");
		
		sql.append("          SELECT DL.PERCENTUAL_DESCONTO ");
		
		sql.append("          FROM DESCONTO_LOGISTICA DL");
		
		sql.append("          WHERE DL.ID = PR.DESCONTO_LOGISTICA_ID ");
		
		sql.append("       ) ");
		
		sql.append("       ELSE 0 END");
		
		sql.append("    ) ");
		
		sql.append("    END ");
		
		sql.append(" ) ");
		
		return sql;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaConsignadoCotaDTO> buscarConsignadoCota(
			FiltroConsultaConsignadoCotaDTO filtro, boolean limitar) {
		 
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ");
		sql.append(" 	PR.CODIGO AS codigoProduto, ");
		sql.append(" 	PR.NOME AS nomeProduto, ");
		sql.append(" 	C.ID AS cotaId, ");
		sql.append(" 	PE.ID AS produtoEdicaoId, ");
		sql.append(" 	PE.NUMERO_EDICAO AS numeroEdicao, ");
		sql.append(" 	PJ.RAZAO_SOCIAL AS nomeFornecedor, ");
		sql.append(" 	CASE ");
		sql.append("			WHEN LCTO.ID IS NOT NULL ");
		sql.append("			THEN LCTO.DATA_LCTO_DISTRIBUIDOR ");
		sql.append("			ELSE MEC.DATA END AS dataLancamento, ");
		
		sql.append(" 	CASE ");
		sql.append("			WHEN LCTO.ID IS NOT NULL ");
		sql.append("			THEN LCTO.DATA_REC_DISTRIB ");
		sql.append("			ELSE MEC.DATA END AS dataRecolhimento, ");
		
		sql.append(" 	COALESCE(MEC.PRECO_VENDA, PE.PRECO_VENDA, 0) AS precoCapa, ");
		
		if (filtro.getIdCota() == null){

            sql.append(" 	COALESCE((COALESCE(MEC.PRECO_COM_DESCONTO, PE.PRECO_VENDA, 0) * "+this.getSQLDescontoLogistica()+")/100, 0) AS desconto, ");
			
			sql.append(" 	COALESCE(MEC.PRECO_COM_DESCONTO, PE.PRECO_VENDA, 0) - COALESCE((COALESCE(MEC.PRECO_COM_DESCONTO, PE.PRECO_VENDA, 0) * "+this.getSQLDescontoLogistica()+")/100, 0) AS precoDesconto, ");
			
			sql.append(" 	SUM(CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE MEC.QTDE * -1 END ");
			sql.append("	  ) AS reparte, ");

			sql.append(" 	(COALESCE(MEC.PRECO_VENDA, PE.PRECO_VENDA, 0) ");
			sql.append("	 *SUM(CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE MEC.QTDE * -1 END) ");
			sql.append("    ) AS total, ");
			
			sql.append(" 	(COALESCE(MEC.PRECO_COM_DESCONTO, PE.PRECO_VENDA, 0) - COALESCE((COALESCE(MEC.PRECO_COM_DESCONTO, PE.PRECO_VENDA, 0) * "+this.getSQLDescontoLogistica()+")/100, 0) ");
			sql.append("	 *SUM(CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE MEC.QTDE * -1 END) ");
			sql.append("    ) AS totalDesconto ");		
		}
		else{
			
            sql.append(" 	COALESCE(MEC.VALOR_DESCONTO, 0) AS desconto, ");
			
			sql.append(" 	COALESCE(MEC.PRECO_COM_DESCONTO, PE.PRECO_VENDA, 0) AS precoDesconto, ");
			
			sql.append(" 	SUM(CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE MEC.QTDE * -1 END ");
			sql.append("	  ) AS reparte, ");

			sql.append(" 	(COALESCE(MEC.PRECO_VENDA, PE.PRECO_VENDA, 0) ");
			sql.append("	 *SUM(CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE MEC.QTDE * -1 END) ");
			sql.append("	) AS total, ");
			
			sql.append("    (COALESCE(MEC.PRECO_COM_DESCONTO, PE.PRECO_VENDA, 0) ");
			sql.append("	 *SUM(CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE MEC.QTDE * -1 END) ");
			sql.append("    ) AS totalDesconto ");	
		}
		
		this.setarFromWhereConsultaConsignado(sql, filtro);
		
		sql.append(" GROUP BY ");
		sql.append(" PE.ID, C.ID ");
		
		sql.append(" HAVING ");
		sql.append(" SUM((CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE 0 END)" +
				   "    -(CASE WHEN TM.OPERACAO_ESTOQUE='SAIDA' then MEC.QTDE ELSE 0 END))<>0 "); 

		if(filtro.getPaginacao()!=null){
			
			if (filtro.getPaginacao().getSortColumn() != null && 
				!filtro.getPaginacao().getSortColumn().trim().isEmpty()) {
				
				sql.append(" ORDER BY ");
				sql.append(filtro.getPaginacao().getSortColumn());		
			
				if ( filtro.getPaginacao().getOrdenacao() != null ) {
					
					sql.append(" ");
					sql.append( filtro.getPaginacao().getOrdenacao().toString());
				}
			}
		}
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		
		if(filtro.getIdCota()!=null) {
			parameters.put("idCota", filtro.getIdCota());
		}

		if(filtro.getIdFornecedor() != null ) { 
			parameters.put("idFornecedor", filtro.getIdFornecedor());
		}

		parameters.put("tipoMovimentoEstorno", GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_FURO_PUBLICACAO.name());

		parameters.put("statusEstoqueFinanceiro", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO.name());
		
		parameters.put("tipoCotaConsignado", TipoCota.CONSIGNADO.name());

		if(filtro.getPaginacao()!=null && limitar){
			
			if(filtro.getPaginacao().getPosicaoInicial()!=null && filtro.getPaginacao().getQtdResultadosPorPagina()!=null) {
				sql.append(" LIMIT :posicaoInicial, :posicaoFinal");
				parameters.put("posicaoInicial",filtro.getPaginacao().getPosicaoInicial());
				parameters.put("posicaoFinal",filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}

		@SuppressWarnings("rawtypes")
		RowMapper cotaRowMapper = new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {

				ConsultaConsignadoCotaDTO dto = new ConsultaConsignadoCotaDTO();
				dto.setCodigoProduto(rs.getString("codigoProduto"));
				dto.setNomeProduto(rs.getString("nomeProduto"));
				dto.setCotaId(rs.getLong("cotaId"));
				dto.setProdutoEdicaoId(rs.getLong("produtoEdicaoId"));
				dto.setNumeroEdicao(rs.getLong("numeroEdicao"));
				dto.setNomeFornecedor(rs.getString("nomeFornecedor"));
				dto.setDataLancamento(rs.getDate("dataLancamento"));
				dto.setDataRecolhimento(rs.getDate("dataRecolhimento"));
				dto.setPrecoCapa(rs.getBigDecimal("precoCapa"));
				dto.setDesconto(rs.getBigDecimal("desconto"));
				dto.setPrecoDesconto(rs.getBigDecimal("precoDesconto"));
				dto.setReparte(BigInteger.valueOf(rs.getLong("reparte")));
				dto.setTotal(rs.getBigDecimal("total"));
				dto.setTotalDesconto(rs.getBigDecimal("totalDesconto"));
				
				return dto;
			}
		};
		
		return (List<ConsultaConsignadoCotaDTO>) namedParameterJdbcTemplate.query(sql.toString(), parameters, cotaRowMapper);
	}
	
	/**
	 * Obtem tuplas de cotas do tipo à vista ou consignado para a consulta de consignado
	 * 
	 * @return StringBuilder
	 */
	private StringBuilder getSqlTuplasCotaAVista(boolean addOutrasCotas){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("AND (");

		if(addOutrasCotas) {
			
		    sql.append("        ((c.TIPO_COTA = :tipoCotaConsignado) AND (MEC.STATUS_ESTOQUE_FINANCEIRO is null OR MEC.STATUS_ESTOQUE_FINANCEIRO = :statusEstoqueFinanceiro)) ");
		}   

		sql.append("    )");
		
		return sql;
	}
	
	private void setarFromWhereConsultaConsignado(StringBuilder sql, FiltroConsultaConsignadoCotaDTO filtro){
		
		sql.append(" FROM ");
		
		sql.append(" MOVIMENTO_ESTOQUE_COTA MEC ");
		
		sql.append(" INNER JOIN LANCAMENTO LCTO ON (MEC.LANCAMENTO_ID=LCTO.ID) ");
		
		sql.append(" INNER JOIN COTA C ON MEC.COTA_ID=C.ID ");
		
		sql.append(" INNER JOIN PESSOA P ON C.PESSOA_ID=P.ID ");
		
		sql.append(" INNER JOIN TIPO_MOVIMENTO TM ON MEC.TIPO_MOVIMENTO_ID=TM.ID ");
		
		sql.append(" INNER JOIN PRODUTO_EDICAO PE ON MEC.PRODUTO_EDICAO_ID = PE.ID ");
		
		sql.append(" INNER JOIN PRODUTO PR ON PE.PRODUTO_ID=PR.ID ");
		
		sql.append(" INNER JOIN PRODUTO_FORNECEDOR F ON PR.ID=F.PRODUTO_ID ");
		
		sql.append(" INNER JOIN FORNECEDOR fornecedor8_ ON F.fornecedores_ID=fornecedor8_.ID ");
		
		sql.append(" INNER JOIN PESSOA PJ ON fornecedor8_.JURIDICA_ID=PJ.ID ");
		
		sql.append(" WHERE ");
		
		sql.append(" MEC.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null ");
	
		sql.append(" AND LCTO.STATUS not in ('FECHADO', 'RECOLHIDO', 'EM_RECOLHIMENTO')");
		
		sql.append(" AND (MEC.STATUS_ESTOQUE_FINANCEIRO IS NULL ");
       
		sql.append(" OR MEC.STATUS_ESTOQUE_FINANCEIRO =:statusEstoqueFinanceiro) ");
		
		sql.append(" AND TM.GRUPO_MOVIMENTO_ESTOQUE not in (:tipoMovimentoEstorno) ");
		
		if(filtro.getDataInicio() != null)
		    sql.append(" AND MEC.DATA >=:dataInicio ");
		
		if(filtro.getDataFim() != null)
            sql.append(" AND MEC.DATA <=:dataFim ");
		
		if(filtro.getIdCota() != null ) { 
			
			sql.append(" AND C.ID = :idCota ");
		}
		
		if(filtro.getTipoOperacao() != null)
		    sql.append(" AND TM.OPERACAO_ESTOQUE=:tipoEstoque ");
		

		if(filtro.getIdFornecedor() != null) { 
			
			sql.append(" AND fornecedor8_.ID = :idFornecedor ");
		}
		
		if(filtro.getAddCotaVista() == null || filtro.getAddOutrasCotas() == null)
		    sql.append(this.getSqlTuplasCotaAVista(true));
		else {
		    sql.append(this.getSqlTuplasCotaAVista(filtro.getAddOutrasCotas()));
		}
	}
	
	/**
	 * Monta query de busca de consignados da cota por fornecedor
	 * 
	 * @param filtro
	 * @return StringBuilder
	 */
	private StringBuilder getQueryMovimentosCotaPeloFornecedor(FiltroConsultaConsignadoCotaDTO filtro){
		
        StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT "); 
		sql.append(" consignadoDistribuidor.numeroCota as numeroCota, ");
		sql.append(" consignadoDistribuidor.nomeCota as nomeCota, ");
		sql.append(" sum(consignadoDistribuidor.reparte) as consignado, ");
		sql.append(" sum(consignadoDistribuidor.total) as total, ");
		sql.append(" sum(consignadoDistribuidor.totalDesconto) as totalDesconto, ");
		sql.append(" consignadoDistribuidor.nomeFornecedor as nomeFornecedor, ");
		sql.append(" consignadoDistribuidor.idFornecedor as idFornecedor ");

		sql.append(" FROM ( ");

		sql.append(" SELECT  ");
		sql.append(" c.NUMERO_COTA AS numeroCota, "); 

		sql.append(" CASE WHEN p.NOME IS NOT NULL THEN p.NOME "); 
		sql.append(" WHEN p.RAZAO_SOCIAL IS NOT NULL THEN p.RAZAO_SOCIAL ELSE NULL END AS nomeCota, "); 

		sql.append(" SUM(CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE 0 END-(CASE WHEN TM.OPERACAO_ESTOQUE='SAIDA' THEN MEC.QTDE ELSE 0 END)) AS reparte, "); 
		sql.append(" SUM(COALESCE(MEC.PRECO_VENDA, PE.PRECO_VENDA, 0) * (CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE MEC.QTDE *-1 END)) AS total, "); 
		sql.append(" SUM(COALESCE(MEC.PRECO_COM_DESCONTO, PE.PRECO_VENDA, 0) * (CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE MEC.QTDE * -1 END)) AS totalDesconto, ");

		sql.append(" PJ.RAZAO_SOCIAL AS nomeFornecedor, "); 
		sql.append(" forn.ID AS idFornecedor ");
		sql.append(" FROM MOVIMENTO_ESTOQUE_COTA MEC ");
		sql.append(" INNER JOIN LANCAMENTO LCTO ON (MEC.LANCAMENTO_ID=LCTO.ID) ");
		sql.append(" INNER JOIN COTA C ON MEC.COTA_ID=C.ID ");
		sql.append(" INNER JOIN PESSOA P ON C.PESSOA_ID=P.ID ");
		sql.append(" INNER JOIN TIPO_MOVIMENTO TM ON MEC.TIPO_MOVIMENTO_ID=TM.ID ");
		sql.append(" INNER JOIN PRODUTO_EDICAO PE ON MEC.PRODUTO_EDICAO_ID = PE.ID ");
		sql.append(" INNER JOIN PRODUTO PR ON PE.PRODUTO_ID=PR.ID ");
		sql.append(" INNER JOIN PRODUTO_FORNECEDOR F ON PR.ID=F.PRODUTO_ID ");
		sql.append(" INNER JOIN FORNECEDOR forn ON F.fornecedores_ID=forn.ID ");
		sql.append(" INNER JOIN PESSOA PJ ON forn.JURIDICA_ID=PJ.ID ");
		sql.append(" WHERE MEC.MOVIMENTO_ESTOQUE_COTA_FURO_ID IS NULL  ");
		sql.append(" AND TM.GRUPO_MOVIMENTO_ESTOQUE NOT IN (:tipoMovimentoEstorno) ");
		sql.append(" AND LCTO.STATUS not in ('FECHADO', 'RECOLHIDO', 'EM_RECOLHIMENTO')");
		
		if(filtro.getIdFornecedor()!=null) {
		
			sql.append(" AND forn.ID = :idFornecedor ");
		}
		
		if(filtro.getIdCota()!=null) {
		
			sql.append(" AND c.ID = :idCota ");
		}
		
		sql.append(this.getSqlTuplasCotaAVista(true));

		sql.append(" GROUP BY pe.ID, c.id ");

		sql.append(" HAVING SUM((CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE 0 END)-(CASE WHEN TM.OPERACAO_ESTOQUE='SAIDA' THEN MEC.QTDE ELSE 0 END))>0 ");

		sql.append(" ) AS consignadoDistribuidor ");
		sql.append(" GROUP BY numeroCota, idFornecedor ");

		return sql;
	}

	/**
	 * Atribui parametros em query de consulta de consignados da cota por fornecedor
	 * 
	 * @param query
	 * @param filtro
	 */
	private Query setParametrosBuscaMovimentoCotaPeloFornecedor(Query query, FiltroConsultaConsignadoCotaDTO filtro){
		
		if(filtro.getIdCota()!=null) {
			query.setParameter("idCota", filtro.getIdCota());
		}

		if(filtro.getIdFornecedor() != null ) { 
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}

		query.setParameterList("tipoMovimentoEstorno", Arrays.asList(GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_FURO_PUBLICACAO.name()));

		query.setParameter("statusEstoqueFinanceiro", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO.name());
		
		query.setParameter("tipoCotaConsignado", TipoCota.CONSIGNADO.name());

		return query;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaConsignadoCotaPeloFornecedorDTO> buscarMovimentosCotaPeloFornecedor(
			FiltroConsultaConsignadoCotaDTO filtro, boolean limitar) {
		
		StringBuilder sql = this.getQueryMovimentosCotaPeloFornecedor(filtro);

		if (filtro.getPaginacao() != null) {
			if (filtro.getPaginacao().getSortColumn() != null) {
				sql.append(" ORDER BY ");
				sql.append(filtro.getPaginacao().getSortColumn());		
			
				if (filtro.getPaginacao().getOrdenacao() != null) {
					sql.append(" ");
					sql.append( filtro.getPaginacao().getOrdenacao().toString());
				}
			}
		}
		
		Query query =  getSession().createSQLQuery(sql.toString());

		query = this.setParametrosBuscaMovimentoCotaPeloFornecedor(query, filtro);

		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaConsignadoCotaPeloFornecedorDTO.class));
		
		((SQLQuery) query).addScalar("numeroCota", StandardBasicTypes.INTEGER);
		((SQLQuery) query).addScalar("nomeCota", StandardBasicTypes.STRING);
		((SQLQuery) query).addScalar("consignado", StandardBasicTypes.BIG_INTEGER);
		((SQLQuery) query).addScalar("total", StandardBasicTypes.BIG_DECIMAL);
		((SQLQuery) query).addScalar("totalDesconto", StandardBasicTypes.BIG_DECIMAL);
		((SQLQuery) query).addScalar("nomeFornecedor", StandardBasicTypes.STRING);
		((SQLQuery) query).addScalar("idFornecedor", StandardBasicTypes.LONG);
		
		if (filtro.getPaginacao() != null) {
			if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			
			if(filtro.getPaginacao().getQtdResultadosPorPagina() != null && limitar) 
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		}
		
		return query.list();
	}

	@Override
	public Long buscarTodosMovimentosCotaPeloFornecedor(FiltroConsultaConsignadoCotaDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT count(*) AS contador FROM  ");
		
		sql.append(" ( SELECT consignadoDistribuidor.numeroCota, consignadoDistribuidor.idFornecedor FROM ( ");
		
		sql.append(" SELECT c.numero_cota AS numeroCota, forn.ID AS idFornecedor ");
		sql.append(" FROM MOVIMENTO_ESTOQUE_COTA MEC ");
		sql.append(" INNER JOIN LANCAMENTO LCTO ON (MEC.LANCAMENTO_ID=LCTO.ID) ");
		sql.append(" INNER JOIN COTA C ON MEC.COTA_ID=C.ID ");
		sql.append(" INNER JOIN PESSOA P ON C.PESSOA_ID=P.ID ");
		sql.append(" INNER JOIN TIPO_MOVIMENTO TM ON MEC.TIPO_MOVIMENTO_ID=TM.ID ");
		sql.append(" INNER JOIN PRODUTO_EDICAO PE ON MEC.PRODUTO_EDICAO_ID = PE.ID ");
		sql.append(" INNER JOIN PRODUTO PR ON PE.PRODUTO_ID=PR.ID ");
		sql.append(" INNER JOIN PRODUTO_FORNECEDOR F ON PR.ID=F.PRODUTO_ID ");
		sql.append(" INNER JOIN FORNECEDOR forn ON F.fornecedores_ID=forn.ID ");
		sql.append(" INNER JOIN PESSOA PJ ON forn.JURIDICA_ID=PJ.ID ");
		sql.append(" WHERE MEC.MOVIMENTO_ESTOQUE_COTA_FURO_ID IS NULL  ");
		sql.append(" AND TM.GRUPO_MOVIMENTO_ESTOQUE NOT IN (:tipoMovimentoEstorno) ");
		sql.append(" AND LCTO.STATUS not in ('FECHADO', 'RECOLHIDO', 'EM_RECOLHIMENTO') ");
		
		if(filtro.getIdFornecedor()!=null) {
		
			sql.append(" AND forn.ID = :idFornecedor ");
		}
		
		if(filtro.getIdCota()!=null) {
		
			sql.append(" AND c.ID = :idCota ");
		}

		sql.append(this.getSqlTuplasCotaAVista(true));

		sql.append(" GROUP BY pe.ID, c.id ");

		sql.append(" HAVING SUM((CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE 0 END)-(CASE WHEN TM.OPERACAO_ESTOQUE='SAIDA' THEN MEC.QTDE ELSE 0 END))>0 ");

		sql.append(" ) AS consignadoDistribuidor GROUP BY consignadoDistribuidor.numeroCota, consignadoDistribuidor.idFornecedor ) as total ");
		
		Query query =  getSession().createSQLQuery(sql.toString());
		
		if(filtro.getIdCota()!=null) {
			query.setParameter("idCota", filtro.getIdCota());
		}

		if(filtro.getIdFornecedor() != null ) { 
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}

		query.setParameterList("tipoMovimentoEstorno", Arrays.asList(GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_FURO_PUBLICACAO.name()));

		query.setParameter("statusEstoqueFinanceiro", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO.name());
		
		query.setParameter("tipoCotaConsignado", TipoCota.CONSIGNADO.name());
		
		((SQLQuery) query).addScalar("contador", StandardBasicTypes.LONG);

		return (Long) query.uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public BigDecimal buscarTotalGeralDaCota(FiltroConsultaConsignadoCotaDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT SUM(totalGeral.total) AS totalDesconto ");
		
		sql.append(" FROM (  ");
		
        sql.append("	SELECT ");
        
        sql.append("	PE.ID as produtoEdicaoId, ");
        
        sql.append("	C.ID as cotaId,  ");

        if (filtro.getIdCota() != null) {
        
        	sql.append("    SUM( COALESCE(MEC.PRECO_COM_DESCONTO, PE.PRECO_VENDA, 0) * (CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE MEC.QTDE * -1 END) ) AS total ");
        	
        } else {
        	
        	sql.append("    SUM( COALESCE(MEC.PRECO_VENDA, PE.PRECO_VENDA, 0) * (CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE MEC.QTDE * -1 END) ) AS total ");
        }
        
        this.setarFromWhereConsultaConsignado(sql, filtro);
		
		sql.append(" GROUP BY ");
		sql.append(" PE.ID, C.ID ");
		
		sql.append(" HAVING ");
		sql.append(" SUM((CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE 0 END)" +
				   "    -(CASE WHEN TM.OPERACAO_ESTOQUE='SAIDA' then MEC.QTDE ELSE 0 END))<>0 "); 

		sql.append(" ) AS totalGeral ");
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		
		if(filtro.getIdCota()!=null) {
			parameters.put("idCota", filtro.getIdCota());
		}

		if(filtro.getIdFornecedor() != null ) { 
			parameters.put("idFornecedor", filtro.getIdFornecedor());
		}

		parameters.put("tipoMovimentoEstorno", GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_FURO_PUBLICACAO.name());

		parameters.put("statusEstoqueFinanceiro", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO.name());
		
		parameters.put("tipoCotaConsignado", TipoCota.CONSIGNADO.name());

		@SuppressWarnings("rawtypes")
		RowMapper cotaRowMapper = new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {

				return rs.getBigDecimal("totalDesconto");
			}
		};
		
		return (BigDecimal) namedParameterJdbcTemplate.queryForObject(
				sql.toString(), parameters, cotaRowMapper);
	}
	
	@Override
	public BigDecimal buscarTotalDetalhadoSomado(FiltroConsultaConsignadoCotaDTO filtro) {
	    
	    List<TotalConsultaConsignadoCotaDetalhado> lista = this.buscarTotalDetalhado(filtro);
        
        BigDecimal total = BigDecimal.ZERO;
        
        for(TotalConsultaConsignadoCotaDetalhado dto : lista) {
            total = total.add(dto.getTotal());
        }
        
        return total;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TotalConsultaConsignadoCotaDetalhado> buscarTotalDetalhado(FiltroConsultaConsignadoCotaDTO filtro) {
		
		StringBuilder sql = new StringBuilder();

        sql.append(" SELECT SUM(totalGeral.total) AS total, ");
        
        sql.append(" totalGeral.nomeFornecedor as nomeFornecedor ");
		
		sql.append(" FROM (  ");
		
		sql.append( this.getQueryMovimentosCotaPeloFornecedor(filtro) );
		
		sql.append("      ) as totalGeral ");
		
		sql.append(" GROUP BY totalGeral.nomeFornecedor ");
		
		Query query =  getSession().createSQLQuery(sql.toString());

		query = this.setParametrosBuscaMovimentoCotaPeloFornecedor(query, filtro);

		query.setResultTransformer(new AliasToBeanResultTransformer(TotalConsultaConsignadoCotaDetalhado.class));
		
		((SQLQuery) query).addScalar("total", StandardBasicTypes.BIG_DECIMAL);
		
		((SQLQuery) query).addScalar("nomeFornecedor", StandardBasicTypes.STRING);
		
		return (List<TotalConsultaConsignadoCotaDetalhado>) query.list();
	}
	
}
