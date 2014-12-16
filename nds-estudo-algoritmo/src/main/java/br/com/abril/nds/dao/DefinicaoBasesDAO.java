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

import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;


@Repository
public class DefinicaoBasesDAO {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Value("#{query_estudo.queryEdicoesLancamentosBasesIcd}")
	private String queryEdicoesLancamentosBasesIcd;
	
	@Value("#{query_estudo.queryEdicoesLancamentosBasesParcialConsolidado}")
	private String queryEdicoesLancamentosBasesParcialConsolidado;

	@Value("#{query_estudo.queryLancamentosAnosAnterioresMesmoMes}")
	private String queryLancamentosAnosAnterioresMesmoMes;

	@Value("#{query_estudo.queryLancamentosAnosAnterioresVeraneio}")
	private String queryLancamentosAnosAnterioresVeraneio;

	private static final String PRODUTO_COLECIONAVEL = "COLECIONAVEL";
	private static final String STATUS_FECHADO = "FECHADO";
	private static final String STATUS_RECOLHIDO = "RECOLHIDO";
	private static final String STATUS_EM_RECOLHIMENTO = "EM_RECOLHIMENTO";

	public LinkedList<ProdutoEdicaoEstudo> getEdicoesBases(ProdutoEdicaoEstudo edicao) {

		Map<String, Object> params = new HashMap<>();
		params.put("CODIGO_ICD", edicao.getProduto().getCodigoICD());
		params.put("TIPO_CLASSIFICACAO_ID", edicao.getTipoClassificacaoProduto().getId());
		params.put("CODIGO_PRODUTO", edicao.getProduto().getCodigo());

		String query;
		
		if(edicao.getPeriodo() != null && edicao.getPeriodo() > 1) {
			
			query = queryEdicoesLancamentosBasesIcd;
		} else {
			
			query = queryEdicoesLancamentosBasesParcialConsolidado;
		}
		
		List<ProdutoEdicaoEstudo> listaProdutoEdicao = jdbcTemplate.query(query, params, new RowMapper<ProdutoEdicaoEstudo>() {
			@Override
			public ProdutoEdicaoEstudo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return produtoEdicaoMapper(rs);
			}
		});
		return new LinkedList<>(listaProdutoEdicao); 
	}

	public List<ProdutoEdicaoEstudo> listaEdicoesAnosAnterioresMesmoMes(ProdutoEdicaoEstudo edicao) {
		return this.listaEdicoesAnosAnteriores(edicao, true, null);
	}

	public List<ProdutoEdicaoEstudo> listaEdicoesAnosAnterioresVeraneio(ProdutoEdicaoEstudo edicao, List<LocalDate> periodoVeraneio) {
		return this.listaEdicoesAnosAnteriores(edicao, false, periodoVeraneio);
	}

	private List<ProdutoEdicaoEstudo> listaEdicoesAnosAnteriores(ProdutoEdicaoEstudo edicao, boolean mesmoMes, List<LocalDate> dataReferencias) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("CODIGO_PRODUTO", edicao.getProduto().getCodigo());
		if (mesmoMes) {
			params.put("DATA_LANCAMENTO", edicao.getDataLancamento());
		} else {
			for (int i = 0; i < dataReferencias.size(); i++) {
				params.put("DATA" + i, dataReferencias.get(i).toString());
			}
		}
		return jdbcTemplate.query(mesmoMes ? queryLancamentosAnosAnterioresMesmoMes : queryLancamentosAnosAnterioresVeraneio, params,
				new RowMapper<ProdutoEdicaoEstudo>() {
			@Override
			public ProdutoEdicaoEstudo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return produtoEdicaoMapper(rs);
			}
		});
	}

	private ProdutoEdicaoEstudo produtoEdicaoMapper(ResultSet rs) throws SQLException {

		ProdutoEdicaoEstudo produtoEdicao = new ProdutoEdicaoEstudo();
		produtoEdicao.setId(rs.getLong("PRODUTO_EDICAO_ID"));
		produtoEdicao.setIdLancamento(rs.getLong("ID"));
		produtoEdicao.setEdicaoAberta(traduzStatus(rs.getString("STATUS")));
		produtoEdicao.setStatus(rs.getString("STATUS"));
		produtoEdicao.setDataLancamento(rs.getDate("DATA_LCTO_DISTRIBUIDOR"));
		produtoEdicao.setColecao(traduzColecionavel(rs.getString("GRUPO_PRODUTO")));
		produtoEdicao.setParcial(rs.getBoolean("PARCIAL"));
		produtoEdicao.setPeriodo(rs.getInt("NUMERO_PERIODO"));
		produtoEdicao.setNumeroEdicao(rs.getLong("NUMERO_EDICAO"));
		produtoEdicao.setProduto(new Produto());
		produtoEdicao.getProduto().setId(rs.getLong("PRODUTO_ID"));
		produtoEdicao.getProduto().setCodigo(rs.getString("CODIGO"));
		produtoEdicao.getProduto().setCodigoICD(rs.getString("CODIGO_ICD"));
        produtoEdicao.setTipoClassificacaoProduto(getTipoClassificacaoProduto(rs.getLong("TIPO_CLASSIFICACAO_PRODUTO_ID")));

		return produtoEdicao;
	}

    private TipoClassificacaoProduto getTipoClassificacaoProduto(long tipo_classificacao_produto_id) {
        TipoClassificacaoProduto classificacaoProduto = new TipoClassificacaoProduto();
        classificacaoProduto.setId(tipo_classificacao_produto_id);
        return classificacaoProduto;
    }

	private boolean traduzColecionavel(String grupoProduto) {
		if (grupoProduto != null && grupoProduto.equalsIgnoreCase(PRODUTO_COLECIONAVEL)) {
			return true;
		}
		return false;
	}

	public boolean traduzStatus(String status) {
		if (status != null && ((status.equalsIgnoreCase(STATUS_FECHADO)) || (status.equalsIgnoreCase(STATUS_RECOLHIDO) || (status.equalsIgnoreCase(STATUS_EM_RECOLHIMENTO))))) {
			return false;
		}
		return true;
	}
}
