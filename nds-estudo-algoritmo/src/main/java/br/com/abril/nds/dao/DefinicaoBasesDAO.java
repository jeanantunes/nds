package br.com.abril.nds.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.ProdutoEdicaoBase;

@Repository
public class DefinicaoBasesDAO {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Value("#{query_estudo.queryEdicoesLancamentos}")
	private String queryEdicoesLancamentos;

	@Value("#{query_estudo.queryLancamentosAnosAnterioresMesmoMes}")
	private String queryLancamentosAnosAnterioresMesmoMes;

	@Value("#{query_estudo.queryLancamentosAnosAnterioresVeraneio}")
	private String queryLancamentosAnosAnterioresVeraneio;

	private static final String LANCAMENTO_PARCIAL = "PARCIAL";
	private static final String PRODUTO_COLECIONAVEL = "COLECIONAVEL";
	private static final String STATUS_FECHADO = "FECHADO";

	public LinkedList<ProdutoEdicaoBase> listaEdicoesPorLancamento(ProdutoEdicaoBase edicao) {

		Map<String, Object> params = new HashMap<>();
		params.put("CODIGO_PRODUTO", edicao.getCodigoProduto());

		List<ProdutoEdicaoBase> listaProdutoEdicao = jdbcTemplate.query(queryEdicoesLancamentos, params, new RowMapper<ProdutoEdicaoBase>() {
			@Override
			public ProdutoEdicaoBase mapRow(ResultSet rs, int rowNum) throws SQLException {
				return produtoEdicaoMapper(rs);
			}
		});
		return new LinkedList<>(listaProdutoEdicao); 
	}

	public List<ProdutoEdicaoBase> listaEdicoesAnosAnterioresMesmoMes(ProdutoEdicaoBase edicao) {
		return this.listaEdicoesAnosAnteriores(edicao, true, null);
	}

	public List<ProdutoEdicaoBase> listaEdicoesAnosAnterioresVeraneio(ProdutoEdicaoBase edicao, List<LocalDate> periodoVeraneio) {
		return this.listaEdicoesAnosAnteriores(edicao, false, periodoVeraneio);
	}

	private List<ProdutoEdicaoBase> listaEdicoesAnosAnteriores(ProdutoEdicaoBase edicao, boolean mesmoMes, List<LocalDate> dataReferencias) {
		Map<String, Object> params = new HashMap<>();
		params.put("CODIGO_PRODUTO", edicao.getCodigoProduto());
		if (mesmoMes) {
			params.put("DATA_LANCAMENTO", edicao.getDataLancamento());
		} else {
			for (int i = 0; i < dataReferencias.size(); i++) {
				params.put("DATA" + i, dataReferencias.get(i).toString());
			}
		}
		return jdbcTemplate.query(mesmoMes ? queryLancamentosAnosAnterioresMesmoMes : queryLancamentosAnosAnterioresVeraneio, params,
				new RowMapper<ProdutoEdicaoBase>() {
			@Override
			public ProdutoEdicaoBase mapRow(ResultSet rs, int rowNum) throws SQLException {
				return produtoEdicaoMapper(rs);
			}
		});
	}

	private ProdutoEdicaoBase produtoEdicaoMapper(ResultSet rs) throws SQLException {

		ProdutoEdicaoBase produtoEdicao = new ProdutoEdicaoBase();
		produtoEdicao.setId(rs.getLong("PRODUTO_EDICAO_ID"));
		produtoEdicao.setIdLancamento(rs.getLong("ID"));
		produtoEdicao.setEdicaoAberta(traduzStatus(rs.getNString("STATUS")));
		produtoEdicao.setDataLancamento(rs.getDate("DATA_LCTO_DISTRIBUIDOR"));
		produtoEdicao.setColecao(traduzColecionavel(rs.getNString("GRUPO_PRODUTO")));
		produtoEdicao.setParcial(rs.getString("TIPO_LANCAMENTO").equalsIgnoreCase(LANCAMENTO_PARCIAL));
		produtoEdicao.setNumeroEdicao(rs.getLong("NUMERO_EDICAO"));
		produtoEdicao.setCodigoProduto(rs.getString("CODIGO"));

		return produtoEdicao;
	}

	private boolean traduzColecionavel(String grupoProduto) {
		if (grupoProduto != null && grupoProduto.equalsIgnoreCase(PRODUTO_COLECIONAVEL)) {
			return true;
		}
		return false;
	}

	private boolean traduzStatus(String status) {
		if (status != null && !status.equalsIgnoreCase(STATUS_FECHADO)) {
			return true;
		}
		return false;
	}
}
