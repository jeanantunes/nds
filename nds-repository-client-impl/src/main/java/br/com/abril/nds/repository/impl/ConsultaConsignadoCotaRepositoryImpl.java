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
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.StatusLancamento;
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
		
		if (filtro.getIdCota() != null){
		
			sql.append(" 	COALESCE(MEC.PRECO_VENDA, PE.PRECO_VENDA, 0) AS precoCapa, ");
			
			sql.append(" 	COALESCE(MEC.VALOR_DESCONTO, 0) AS desconto, ");
			
			sql.append(" 	COALESCE(MEC.PRECO_COM_DESCONTO, PE.PRECO_VENDA, 0) AS precoDesconto, ");
			
			sql.append(" 	SUM(CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE MEC.QTDE * -1 END ");
			sql.append("	  ) AS reparte, ");
			
			sql.append(" 	COALESCE(MEC.PRECO_VENDA, PE.PRECO_VENDA, 0) ");
			sql.append("		*SUM(CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE MEC.QTDE * -1 END ");
			sql.append("		   ) AS total, ");
			
			sql.append(" 	COALESCE(MEC.PRECO_COM_DESCONTO, PE.PRECO_VENDA, 0) ");
			sql.append("		*SUM(CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE MEC.QTDE * -1 END ");
			sql.append("		   ) AS totalDesconto ");
		}
		else{
			
            sql.append(" 	COALESCE(MEC.PRECO_VENDA, PE.PRECO_VENDA, 0) AS precoCapa, ");
			
			sql.append(" 	COALESCE((COALESCE(MEC.PRECO_VENDA, PE.PRECO_VENDA, 0) * "+this.getSQLDescontoLogistica()+")/100, 0) AS desconto, ");
			
			sql.append(" 	COALESCE(MEC.PRECO_VENDA, PE.PRECO_VENDA, 0) - COALESCE((COALESCE(MEC.PRECO_VENDA, PE.PRECO_VENDA, 0) * "+this.getSQLDescontoLogistica()+")/100, 0) AS precoDesconto, ");
			
			sql.append(" 	SUM(CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE MEC.QTDE * -1 END ");
			sql.append("	  ) AS reparte, ");
			
			sql.append(" 	COALESCE(MEC.PRECO_VENDA, PE.PRECO_VENDA, 0) ");
			sql.append("		*SUM(CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE MEC.QTDE * -1 END ");
			sql.append("		   ) AS total, ");
			
			sql.append(" 	COALESCE(MEC.PRECO_VENDA, PE.PRECO_VENDA, 0) - COALESCE((MEC.PRECO_VENDA * "+this.getSQLDescontoLogistica()+")/100, 0) ");
			sql.append("		*SUM(CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE MEC.QTDE * -1 END ");
			sql.append("		   ) AS totalDesconto ");
		}
		
		this.setarFromWhereConsultaConsignado(sql, filtro);
		
		sql.append(" GROUP BY ");
		sql.append(" PE.ID, C.ID ");
		
		sql.append(" HAVING ");
		sql.append(" SUM((CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE 0 END)" +
				   "    -(CASE WHEN TM.OPERACAO_ESTOQUE='SAIDA' then MEC.QTDE ELSE 0 END))>0 "); 

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
		
		parameters.put("tipoCotaAVista", TipoCota.A_VISTA.name());
		
		parameters.put("tipoCotaConsignado", TipoCota.CONSIGNADO.name());
		
		parameters.put("statusConferenciaEncalhe", StatusOperacao.CONCLUIDO.name());
		
		parameters.put("statusRecolhido", StatusLancamento.RECOLHIDO.name());

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
	private StringBuilder getSqlTuplasCotaAVista(){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("AND (");

		sql.append("        ((c.TIPO_COTA = :tipoCotaConsignado) AND (MEC.STATUS_ESTOQUE_FINANCEIRO is null OR MEC.STATUS_ESTOQUE_FINANCEIRO = :statusEstoqueFinanceiro)) OR ");
		
		sql.append("        ( ");
		
		sql.append("          (c.TIPO_COTA = :tipoCotaAVista) AND ");
		
		sql.append("          (   ");
		
		sql.append("              ((c.ALTERACAO_TIPO_COTA IS NOT NULL AND MEC.DATA <= c.ALTERACAO_TIPO_COTA) AND (MEC.STATUS_ESTOQUE_FINANCEIRO is null OR MEC.STATUS_ESTOQUE_FINANCEIRO = :statusEstoqueFinanceiro)) OR ");
		
		sql.append("              (");
		
		sql.append("                  ((c.ALTERACAO_TIPO_COTA IS NOT NULL AND MEC.DATA > c.ALTERACAO_TIPO_COTA)) AND ");
		
		sql.append("                  (");
		
		sql.append("                      ((SELECT PCC.DEVOLVE_ENCALHE FROM PARAMETRO_COBRANCA_COTA PCC WHERE PCC.COTA_ID = c.ID) IS NULL) OR ");
		
		sql.append("                      ((SELECT PCC.DEVOLVE_ENCALHE FROM PARAMETRO_COBRANCA_COTA PCC WHERE PCC.COTA_ID = c.ID) = TRUE) ");
		
		sql.append("                  )");
		
		sql.append("              ) ");
		
		sql.append("          ) AND ");
		
		sql.append("          MEC.ID NOT IN (SELECT CONFE.MOVIMENTO_ESTOQUE_COTA_ID ");
		
		sql.append("          		         FROM CONFERENCIA_ENCALHE CONFE ");
		
		sql.append("          		         INNER JOIN CONTROLE_CONFERENCIA_ENCALHE_COTA CCEC ON CCEC.ID = CONFE.CONTROLE_CONFERENCIA_ENCALHE_COTA_ID ");
		
		sql.append("          		         INNER JOIN CONTROLE_CONFERENCIA_ENCALHE CCE ON CCE.ID = CCEC.CTRL_CONF_ENCALHE_ID ");
		
		sql.append("          		         WHERE CCE.STATUS = :statusConferenciaEncalhe ) ");
		
		sql.append("        ) ");

		sql.append("    )");
		
		return sql;
	}
	
	private void setarFromWhereConsultaConsignado(StringBuilder sql, FiltroConsultaConsignadoCotaDTO filtro){
		
		sql.append(" FROM ");
		
		sql.append(" MOVIMENTO_ESTOQUE_COTA MEC ");
		
		sql.append(" LEFT OUTER JOIN LANCAMENTO LCTO ON (MEC.LANCAMENTO_ID=LCTO.ID AND LCTO.STATUS <> :statusRecolhido) ");
		
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
		
		sql.append(" AND TM.GRUPO_MOVIMENTO_ESTOQUE not in (:tipoMovimentoEstorno) ");
		
		if(filtro.getIdCota() != null ) { 
			
			sql.append(" AND C.ID = :idCota ");
		}

		if(filtro.getIdFornecedor() != null) { 
			
			sql.append(" AND fornecedor8_.ID = :idFornecedor ");
		}
		
		sql.append(this.getSqlTuplasCotaAVista());
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaConsignadoCotaPeloFornecedorDTO> buscarMovimentosCotaPeloFornecedor(
			FiltroConsultaConsignadoCotaDTO filtro, boolean limitar) {
		
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
		sql.append(" LEFT OUTER JOIN LANCAMENTO LCTO ON (MEC.LANCAMENTO_ID=LCTO.ID AND LCTO.STATUS <> :statusRecolhido) ");
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
		
		if(filtro.getIdFornecedor()!=null) {
		
			sql.append(" AND forn.ID = :idFornecedor ");
		}
		
		if(filtro.getIdCota()!=null) {
		
			sql.append(" AND c.ID = :idCota ");
		}
		
		sql.append(this.getSqlTuplasCotaAVista());

		sql.append(" GROUP BY pe.ID, c.id ");

		sql.append(" HAVING SUM((CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE 0 END)-(CASE WHEN TM.OPERACAO_ESTOQUE='SAIDA' THEN MEC.QTDE ELSE 0 END))>0 ");

		sql.append(" ) AS consignadoDistribuidor ");
		sql.append(" GROUP BY numeroCota, idFornecedor ");

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
		
		if(filtro.getIdCota()!=null) {
			query.setParameter("idCota", filtro.getIdCota());
		}

		if(filtro.getIdFornecedor() != null ) { 
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}

		query.setParameterList("tipoMovimentoEstorno", Arrays.asList(GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_FURO_PUBLICACAO.name()));

		query.setParameter("statusEstoqueFinanceiro", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO.name());
		
		query.setParameter("tipoCotaAVista", TipoCota.A_VISTA.name());
		
		query.setParameter("statusConferenciaEncalhe", StatusOperacao.CONCLUIDO.name());
		
		query.setParameter("statusRecolhido", StatusLancamento.RECOLHIDO.name());

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
		
		sql.append(" SELECT count(consignadoDistribuidor.numeroCota) AS contador ");
		
		sql.append(" FROM ( ");
		
		sql.append(" SELECT c.numero_cota AS numeroCota, forn.ID AS idFornecedor ");
		sql.append(" FROM MOVIMENTO_ESTOQUE_COTA MEC ");
		sql.append(" LEFT OUTER JOIN LANCAMENTO LCTO ON (MEC.LANCAMENTO_ID=LCTO.ID AND LCTO.STATUS <> :statusRecolhido) ");
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
		
		if(filtro.getIdFornecedor()!=null) {
		
			sql.append(" AND forn.ID = :idFornecedor ");
		}
		
		if(filtro.getIdCota()!=null) {
		
			sql.append(" AND c.ID = :idCota ");
		}

		sql.append(this.getSqlTuplasCotaAVista());

		sql.append(" GROUP BY pe.ID, c.id ");

		sql.append(" HAVING SUM((CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE 0 END)-(CASE WHEN TM.OPERACAO_ESTOQUE='SAIDA' THEN MEC.QTDE ELSE 0 END))>0 ");

		sql.append(" ) AS consignadoDistribuidor ");
		
		Query query =  getSession().createSQLQuery(sql.toString());
		
		if(filtro.getIdCota()!=null) {
			query.setParameter("idCota", filtro.getIdCota());
		}

		if(filtro.getIdFornecedor() != null ) { 
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}

		query.setParameterList("tipoMovimentoEstorno", Arrays.asList(GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_FURO_PUBLICACAO.name()));

		query.setParameter("statusEstoqueFinanceiro", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO.name());
		
		query.setParameter("tipoCotaAVista", TipoCota.A_VISTA.name());
		
		query.setParameter("statusConferenciaEncalhe", StatusOperacao.CONCLUIDO.name());
		
		query.setParameter("statusRecolhido", StatusLancamento.RECOLHIDO.name());
		
		((SQLQuery) query).addScalar("contador", StandardBasicTypes.LONG);

		return (Long) query.uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public BigDecimal buscarTotalGeralDaCota(FiltroConsultaConsignadoCotaDTO filtro) {
		
		StringBuilder sql = new StringBuilder();

		if (filtro.getIdCota() == null) {

			sql.append(" SELECT (totalGeral.total) AS totalDesconto FROM (  ");
			sql.append(" SELECT	sum(   COALESCE(MEC.PRECO_VENDA, PE.PRECO_VENDA, 0) + COALESCE((COALESCE(MEC.PRECO_VENDA, PE.PRECO_VENDA, 0) * "+this.getSQLDescontoLogistica()+")/100, 0)   ");
			sql.append("		*(CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE MEC.QTDE * -1 END ");
			sql.append("		   )) AS total ");
		} else {
		
			sql.append(" SELECT (totalGeral.totalComDesconto) AS totalDesconto FROM (  ");
			sql.append(" SELECT	sum(COALESCE(MEC.PRECO_COM_DESCONTO, PE.PRECO_VENDA, 0) ");
			sql.append("		*(CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE MEC.QTDE * -1 END ");
			sql.append("		   )) AS totalComDesconto ");
		}

		this.setarFromWhereConsultaConsignado(sql, filtro);
		
		sql.append(" ) AS totalGeral ");

		Map<String, Object> parameters = new HashMap<String, Object>();
		
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = 
			new NamedParameterJdbcTemplate(dataSource);
		
		if(filtro.getIdCota()!=null) {
			parameters.put("idCota", filtro.getIdCota());
		}

		if(filtro.getIdFornecedor() != null ) { 
			parameters.put("idFornecedor", filtro.getIdFornecedor());
		}

		parameters.put("tipoMovimentoEstorno", GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_FURO_PUBLICACAO.name());

		parameters.put("statusEstoqueFinanceiro", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO.name());
		
        parameters.put("tipoCotaAVista", TipoCota.A_VISTA.name());
        
        parameters.put("tipoCotaConsignado", TipoCota.CONSIGNADO.name());
        
        parameters.put("statusConferenciaEncalhe", StatusOperacao.CONCLUIDO.name());
        
        parameters.put("statusRecolhido", StatusLancamento.RECOLHIDO.name());

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
	@SuppressWarnings("unchecked")
	public List<TotalConsultaConsignadoCotaDetalhado> buscarTotalDetalhado(FiltroConsultaConsignadoCotaDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		if (filtro.getIdCota() == null) {

			sql.append(" SELECT	sum(COALESCE(MEC.PRECO_VENDA, PE.PRECO_VENDA, 0) + COALESCE((COALESCE(MEC.PRECO_VENDA, PE.PRECO_VENDA, 0) * " + this.getSQLDescontoLogistica() + ")/100, 0) ");
			sql.append(" *(CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE MEC.QTDE * -1 END ");
			sql.append(" )) AS total ");
			
		} else {
		
			sql.append(" SELECT	sum(COALESCE(MEC.PRECO_COM_DESCONTO, PE.PRECO_VENDA, 0) ");
			sql.append(" *(CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE MEC.QTDE * -1 END ");
			sql.append(" )) AS total ");
		}
		
		sql.append(" , PJ.RAZAO_SOCIAL AS nomeFornecedor ");
		
		this.setarFromWhereConsultaConsignado(sql, filtro);
		
		sql.append(" GROUP BY fornecedor8_.id ");
		
		sql.append(" HAVING ");
		sql.append(" SUM((CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE 0 END)" +
				   "    -(CASE WHEN TM.OPERACAO_ESTOQUE='SAIDA' then MEC.QTDE ELSE 0 END))>0 "); 
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		
		if (filtro.getIdCota()!=null) {
			
			parameters.put("idCota", filtro.getIdCota());
		}

		if (filtro.getIdFornecedor() != null ) {
			
			parameters.put("idFornecedor", filtro.getIdFornecedor());
		}

		parameters.put("tipoMovimentoEstorno", GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_FURO_PUBLICACAO.name());

		parameters.put("statusEstoqueFinanceiro", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO.name());
		
		parameters.put("tipoCotaAVista", TipoCota.A_VISTA.name());
		
		parameters.put("tipoCotaConsignado", TipoCota.CONSIGNADO.name());
		
		parameters.put("statusConferenciaEncalhe", StatusOperacao.CONCLUIDO.name());
		
		parameters.put("statusRecolhido", StatusLancamento.RECOLHIDO.name());

		@SuppressWarnings("rawtypes")
		RowMapper cotaRowMapper = new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {

				TotalConsultaConsignadoCotaDetalhado dto = new TotalConsultaConsignadoCotaDetalhado();
				
				dto.setNomeFornecedor(rs.getString("nomeFornecedor"));
				dto.setTotal(rs.getBigDecimal("total"));
				
				return dto;
			}
		};
		
		return (List<TotalConsultaConsignadoCotaDetalhado>) namedParameterJdbcTemplate.query(sql.toString(), parameters, cotaRowMapper);
	}
		
}
