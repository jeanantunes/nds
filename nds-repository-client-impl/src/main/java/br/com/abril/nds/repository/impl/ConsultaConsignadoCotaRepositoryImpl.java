package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaConsignadoCotaDTO;
import br.com.abril.nds.dto.ConsultaConsignadoCotaPeloFornecedorDTO;
import br.com.abril.nds.dto.TotalConsultaConsignadoCotaDetalhado;
import br.com.abril.nds.dto.filtro.FiltroConsultaConsignadoCotaDTO;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque.Dominio;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
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
		
		sql.append(" 	CASE " +
				"			WHEN LCTO.ID IS NOT NULL " +
				"			THEN LCTO.DATA_LCTO_DISTRIBUIDOR " +
				"			ELSE MEC.DATA END AS dataLancamento, ");
		
		sql.append(" 	COALESCE(MEC.PRECO_VENDA, PE.PRECO_VENDA, 0) AS precoCapa, ");
		
		sql.append(" 	COALESCE(MEC.VALOR_DESCONTO, 0) AS desconto, ");
		
		sql.append(" 	COALESCE(MEC.PRECO_COM_DESCONTO, PE.PRECO_VENDA, 0) AS precoDesconto, ");
		
		sql.append(" 	SUM(CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE 0 END " +
				   "	  -(CASE WHEN TM.OPERACAO_ESTOQUE='SAIDA' THEN MEC.QTDE ELSE 0 END)) AS reparte, ");
		
		sql.append(" 	COALESCE(MEC.PRECO_VENDA, PE.PRECO_VENDA, 0)" +
				   "		*SUM(CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE 0 END" +
				   "		   -(CASE WHEN TM.OPERACAO_ESTOQUE='SAIDA' THEN MEC.QTDE ELSE 0 END)) AS total, ");
		
		sql.append(" 	COALESCE(MEC.PRECO_COM_DESCONTO, PE.PRECO_VENDA, 0)" +
				   "		*SUM(CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE 0 END" +
				   "		   -(CASE WHEN TM.OPERACAO_ESTOQUE='SAIDA' then MEC.QTDE ELSE 0 END)) AS totalDesconto ");

		
		sql.append(" FROM ");
		
		sql.append(" MOVIMENTO_ESTOQUE_COTA MEC ");
		
		sql.append(" LEFT OUTER JOIN LANCAMENTO LCTO ON MEC.LANCAMENTO_ID=LCTO.ID");
		
		sql.append(" INNER JOIN COTA C ON MEC.COTA_ID=C.ID ");
		
		sql.append(" LEFT OUTER JOIN PARAMETRO_COBRANCA_COTA PCC ON C.ID=PCC.COTA_ID ");
       	
		sql.append(" INNER JOIN PESSOA P ON C.PESSOA_ID=P.ID ");
		
		sql.append(" INNER JOIN TIPO_MOVIMENTO TM ON MEC.TIPO_MOVIMENTO_ID=TM.ID ");
		
		sql.append(" INNER JOIN PRODUTO_EDICAO PE ON MEC.PRODUTO_EDICAO_ID = PE.ID ");
		
		sql.append(" INNER JOIN PRODUTO PR ON PE.PRODUTO_ID=PR.ID ");
		
		sql.append(" INNER JOIN PRODUTO_FORNECEDOR F ON PR.ID=F.PRODUTO_ID ");
		
		sql.append(" INNER JOIN FORNECEDOR fornecedor8_ ON F.fornecedores_ID=fornecedor8_.ID ");
		
		sql.append(" INNER JOIN PESSOA PJ ON fornecedor8_.JURIDICA_ID=PJ.ID ");
		
		
		sql.append(" WHERE ");
		
		sql.append(" ( MEC.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null ) ");
		
		sql.append(" AND ( TM.GRUPO_MOVIMENTO_ESTOQUE not in (:tipoMovimentoEstorno) ");
		sql.append(" ) "); 

		if(filtro.getIdCota() != null ) { 
			sql.append(" AND C.ID = :idCota ");
		}

		if(filtro.getIdFornecedor() != null) { 
			sql.append(" AND fornecedor8_.ID = :idFornecedor ");
		}

		sql.append(" AND (MEC.STATUS_ESTOQUE_FINANCEIRO is null OR MEC.STATUS_ESTOQUE_FINANCEIRO = :statusEstoqueFinanceiro) "); 
		
		sql.append(" GROUP BY ");
		
		sql.append(" PE.ID ");
		
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

		parameters.put("statusEstoqueFinanceiro", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO.ordinal());

		if(filtro.getPaginacao()!=null){
			
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

	private String createFromConsultaConsignadoCota(FiltroConsultaConsignadoCotaDTO filtro) {
		StringBuilder hql = new StringBuilder();
		
		hql.append(" FROM MovimentoEstoqueCota movimento, ProdutoEdicao pe ");
		hql.append("  LEFT JOIN movimento.lancamento lancamento ");
		hql.append("  JOIN movimento.cota as cota ");
		hql.append("  JOIN movimento.tipoMovimento as tipoMovimento ");
		hql.append("  JOIN pe.produto as produto ");
		hql.append("  JOIN cota.parametroCobranca parametroCobranca ");
		hql.append("  JOIN produto.fornecedores as fornecedor ");
		hql.append("  JOIN fornecedor.juridica as pessoa ");		
		hql.append("  JOIN cota.pessoa as pessoaCota ");
		
		hql.append(" WHERE movimento.produtoEdicao.id = pe.id " );
		
		hql.append(" AND movimento.movimentoEstoqueCotaFuro is null " );
		
		hql.append(" AND movimento.tipoMovimento.grupoMovimentoEstoque not in (:tipoMovimentoEstorno) " );
		
		 if(filtro.getIdCota() != null ) { 
			hql.append(" AND cota.id = :idCota ");			
		}
		if(filtro.getIdFornecedor() != null) { 
			hql.append(" AND fornecedor.id = :idFornecedor ");
		}
		
		hql.append(" AND ( movimento.statusEstoqueFinanceiro is null or movimento.statusEstoqueFinanceiro = :statusEstoqueFinanceiro ) ");
		
		hql.append(" GROUP BY pe.id ");
		
		hql.append(" HAVING sum( (case when tipoMovimento.operacaoEstoque = 'ENTRADA'  then movimento.qtde else 0 end)  ");
		hql.append("	-  (case when tipoMovimento.operacaoEstoque = 'SAIDA' then movimento.qtde else 0 end) ) <> 0 ");
		  
		
		return hql.toString();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaConsignadoCotaPeloFornecedorDTO> buscarMovimentosCotaPeloFornecedor(
			FiltroConsultaConsignadoCotaDTO filtro, boolean limitar) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT cota.numeroCota as numeroCota, 		")
		   .append(" (CASE WHEN (pessoaCota.nome is not null)	")
		   .append(" THEN ( pessoaCota.nome ) 					")
		   .append(" WHEN (pessoaCota.razaoSocial is not null) 	")
		   .append(" THEN (pessoaCota.razaoSocial)  			")
		   .append(" ELSE null END) as nomeCota, 			    ")
		   
		   .append(" SUM(movimento.qtde) as consignado, 				")
		   
		   .append(" SUM( coalesce(movimento.valoresAplicados.precoVenda, pe.precoVenda, 0)  * movimento.qtde) as total, ")
		   
		   .append(" SUM( coalesce(movimento.valoresAplicados.precoComDesconto, pe.precoVenda, 0) * movimento.qtde )  as totalDesconto, ")
		   
		   .append(" pessoa.razaoSocial as nomeFornecedor,  ")
		   
		   .append(" fornecedor.id as idFornecedor  ");
		
		hql.append(getHQLFromEWhereConsignadoCota(filtro));
		
		hql.append(getGroupBy(filtro));

		if (filtro.getPaginacao() != null) {
			if (filtro.getPaginacao().getSortColumn() != null) {
				hql.append(" ORDER BY ");
				hql.append(filtro.getPaginacao().getSortColumn());		
			
				if (filtro.getPaginacao().getOrdenacao() != null) {
					hql.append(" ");
					hql.append( filtro.getPaginacao().getOrdenacao().toString());
				}
			}
		}
		
		Query query =  getSession().createQuery(hql.toString());
		
		buscarParametrosConsignadoCota(query, filtro);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ConsultaConsignadoCotaPeloFornecedorDTO.class));
		
		if (filtro.getPaginacao() != null) {
			if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			
			if(filtro.getPaginacao().getQtdResultadosPorPagina() != null && limitar) 
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		}
		
		return query.list();
		 
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Long buscarTodasMovimentacoesPorCota(
			FiltroConsultaConsignadoCotaDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT count(movimento)  ");
		
		hql.append(createFromConsultaConsignadoCota(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		if(filtro.getIdCota() != null ) { 
			query.setParameter("idCota", filtro.getIdCota());
		}
		
		if(filtro.getIdFornecedor() != null ) { 
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		query.setParameter("tipoMovimentoEstorno", GrupoMovimentoEstoque.ESTORNO_REPARTE_COTA_FURO_PUBLICACAO);
		
		query.setParameter("statusEstoqueFinanceiro", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO);
		
		List<Long> totalRegistros = query.list();
		
		return (totalRegistros == null) ? 0L : totalRegistros.size();

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Long buscarTodosMovimentosCotaPeloFornecedor(
			FiltroConsultaConsignadoCotaDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT count(movimento)  ");
		
		hql.append(getHQLFromEWhereConsignadoCota(filtro));
		
		hql.append(getGroupBy(filtro));

		Query query =  getSession().createQuery(hql.toString());
		
		buscarParametrosConsignadoCota(query, filtro);
		
		List<Long> totalRegistros = query.list();
		
		return (totalRegistros == null) ? 0L : totalRegistros.size();
	}

	@Override
	@SuppressWarnings("unchecked")
	public BigDecimal buscarTotalGeralDaCota(FiltroConsultaConsignadoCotaDTO filtro) {

//		StringBuilder hql = new StringBuilder();
//		
//		hql.append(" SELECT SUM( coalesce(movimento.valoresAplicados.precoComDesconto, pe.precoVenda, 0)  * movimento.qtde) ");
//		
//		hql.append(getHQLFromEWhereConsignadoCota(filtro));
//		
//		Query query =  getSession().createQuery(hql.toString());
//		
//		buscarParametrosConsignadoCota(query, filtro);
//		
//		BigDecimal totalRegistros = (BigDecimal) query.uniqueResult();
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ");

		sql.append(" 	COALESCE(MEC.PRECO_COM_DESCONTO, PE.PRECO_VENDA, 0)" +
				   "		*SUM(CASE WHEN TM.OPERACAO_ESTOQUE='ENTRADA' THEN MEC.QTDE ELSE 0 END" +
				   "		   -(CASE WHEN TM.OPERACAO_ESTOQUE='SAIDA' then MEC.QTDE ELSE 0 END)) AS totalDesconto ");
		
		sql.append(" FROM ");
		
		sql.append(" MOVIMENTO_ESTOQUE_COTA MEC ");
		
		sql.append(" LEFT OUTER JOIN LANCAMENTO LCTO ON MEC.LANCAMENTO_ID=LCTO.ID");
		
		sql.append(" INNER JOIN COTA C ON MEC.COTA_ID=C.ID ");
		
		sql.append(" LEFT OUTER JOIN PARAMETRO_COBRANCA_COTA PCC ON C.ID=PCC.COTA_ID ");
       	
		sql.append(" INNER JOIN PESSOA P ON C.PESSOA_ID=P.ID ");
		
		sql.append(" INNER JOIN TIPO_MOVIMENTO TM ON MEC.TIPO_MOVIMENTO_ID=TM.ID ");
		
		sql.append(" INNER JOIN PRODUTO_EDICAO PE ON MEC.PRODUTO_EDICAO_ID = PE.ID ");
		
		sql.append(" INNER JOIN PRODUTO PR ON PE.PRODUTO_ID=PR.ID ");
		
		sql.append(" INNER JOIN PRODUTO_FORNECEDOR F ON PR.ID=F.PRODUTO_ID ");
		
		sql.append(" INNER JOIN FORNECEDOR fornecedor8_ ON F.fornecedores_ID=fornecedor8_.ID ");
		
		sql.append(" INNER JOIN PESSOA PJ ON fornecedor8_.JURIDICA_ID=PJ.ID ");
		
		sql.append(" WHERE ");
		
		sql.append(" ( MEC.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null ) ");
		
		sql.append(" AND ( TM.GRUPO_MOVIMENTO_ESTOQUE not in (:tipoMovimentoEstorno) ");
		sql.append(" ) "); 

		if(filtro.getIdCota() != null ) { 
			sql.append(" AND C.ID = :idCota ");
		}

		if(filtro.getIdFornecedor() != null) { 
			sql.append(" AND fornecedor8_.ID = :idFornecedor ");
		}

		sql.append(" AND (MEC.STATUS_ESTOQUE_FINANCEIRO is null OR MEC.STATUS_ESTOQUE_FINANCEIRO = :statusEstoqueFinanceiro) "); 

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

		parameters.put("statusEstoqueFinanceiro", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO.ordinal());

		@SuppressWarnings("rawtypes")
		RowMapper cotaRowMapper = new RowMapper() {

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {

				return rs.getBigDecimal("totalDesconto");
			}
		};
		
		return (BigDecimal) namedParameterJdbcTemplate.queryForObject(
				sql.toString(), parameters, cotaRowMapper);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TotalConsultaConsignadoCotaDetalhado> buscarTotalDetalhado(
			FiltroConsultaConsignadoCotaDTO filtro) {
		 
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT SUM(coalesce(movimento.valoresAplicados.precoComDesconto, pe.precoVenda, 0) * movimento.qtde) as total, ");
		
		hql.append("pessoa.razaoSocial as nomeFornecedor");
		
		hql.append(getHQLFromEWhereConsignadoCota(filtro));
		
		hql.append(getGroupByTotalDetalhado(filtro));

		Query query =  getSession().createQuery(hql.toString());
		
		buscarParametrosConsignadoCota(query, filtro);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				TotalConsultaConsignadoCotaDetalhado.class));
		
		return query.list();
	}
	
	private String getHQLFromEWhereConsignadoCota(FiltroConsultaConsignadoCotaDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
	
		hql.append(" FROM MovimentoEstoqueCota movimento ");
		hql.append(" LEFT join movimento.lancamento lancamento ");
		hql.append("  JOIN movimento.cota as cota ");
		hql.append("  JOIN movimento.tipoMovimento as tipoMovimento ");
		hql.append("  JOIN movimento.produtoEdicao as pe ");
		hql.append("  JOIN pe.produto as produto ");
		hql.append("  JOIN cota.parametroCobranca parametroCobranca ");
		hql.append("  JOIN produto.fornecedores as fornecedor ");
		hql.append("  JOIN fornecedor.juridica as pessoa ");		
		hql.append("  JOIN cota.pessoa as pessoaCota ");
		hql.append("  LEFT JOIN movimento.movimentoEstoqueCotaFuro as movimentoEstoqueCotaFuro ");
		
		hql.append(" WHERE tipoMovimento.grupoMovimentoEstoque IN (:tipoMovimentoEntrada) " );
		
		hql.append(" AND (movimento.statusEstoqueFinanceiro is null ");
		hql.append(" or movimento.statusEstoqueFinanceiro = :statusEstoqueFinanceiro ) " );
		
		hql.append(" AND tipoMovimento.operacaoEstoque = :tipoOperacaoEntrada ");
		
		hql.append(" AND movimentoEstoqueCotaFuro.id is null ");
		
		if(filtro.getIdCota() != null ) { 
			hql.append("   AND cota.id = :idCota");			
		}
		if(filtro.getIdFornecedor() != null) { 
			hql.append("   AND fornecedor.id = :idFornecedor");
		}

		return hql.toString();
	}

	private String getGroupBy(FiltroConsultaConsignadoCotaDTO filtro){
		StringBuilder hql = new StringBuilder();
		
	    hql.append("  GROUP BY cota.numeroCota,  ")
		   .append("          fornecedor.id ");

		return hql.toString();	
	}
	
	private String getGroupByTotalDetalhado(FiltroConsultaConsignadoCotaDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
	    hql.append("  GROUP BY fornecedor.id ");

		return hql.toString();	
	}
	
	private List<GrupoMovimentoEstoque> obterGruposMovimentoEstoqueDeEntradaNaCota() {
		
		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoquesEntrada = new ArrayList<GrupoMovimentoEstoque>();
		
		for(GrupoMovimentoEstoque grupoMovimentoEstoque: GrupoMovimentoEstoque.values()) {
			
			if(Dominio.COTA.equals(grupoMovimentoEstoque.getDominio()) && 
			   OperacaoEstoque.ENTRADA.equals(grupoMovimentoEstoque.getOperacaoEstoque())) {
				
				listaGrupoMovimentoEstoquesEntrada.add(grupoMovimentoEstoque);
			
			}
			
		}
		
		return listaGrupoMovimentoEstoquesEntrada;
		
	}
	
	private void buscarParametrosConsignadoCota(Query query, FiltroConsultaConsignadoCotaDTO filtro){

		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoquesEntrada = new ArrayList<GrupoMovimentoEstoque>();
		
		if (filtro.getIdCota() == null) {
			
			listaGrupoMovimentoEstoquesEntrada = obterGruposMovimentoEstoqueDeEntradaNaCota();
			

		} else {
			
			listaGrupoMovimentoEstoquesEntrada.add(GrupoMovimentoEstoque.RECEBIMENTO_REPARTE);
			
		}
		
		query.setParameter("tipoOperacaoEntrada", OperacaoEstoque.ENTRADA);
		
		query.setParameterList("tipoMovimentoEntrada", listaGrupoMovimentoEstoquesEntrada);
		
		query.setParameter("statusEstoqueFinanceiro", StatusEstoqueFinanceiro.FINANCEIRO_NAO_PROCESSADO);
		
		if(filtro.getIdCota() != null ) { 
			query.setParameter("idCota", filtro.getIdCota());
		}
		
		if(filtro.getIdFornecedor() != null ) { 
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
	
	}
		
}
