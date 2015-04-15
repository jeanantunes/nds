package br.com.abril.nds.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;

@Repository
public class ProdutoEdicaoDAO {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoEdicaoDAO.class);


	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Value("#{query_estudo.queryProdutoEdicaoEstudo}")
	private String queryProdutoEdicaoEstudo;

	@Value("#{query_estudo.queryEdicoesRecebidas}")
	private String queryEdicoesRecebidas;

	@Value("#{query_estudo.queryQtdeVezesReenviadas}")
	private String queryQtdeVezesReenviadas;
	
	private static final String PRODUTO_COLECIONAVEL = "COLECIONAVEL";


	public List<ProdutoEdicaoEstudo> getEdicaoRecebidas(CotaEstudo cota) {
		return getEdicaoRecebidas(cota, null);
	}

	public List<ProdutoEdicaoEstudo> getEdicaoRecebidas(CotaEstudo cota, ProdutoEdicaoEstudo produto) {

		List<ProdutoEdicaoEstudo> edicoes = new ArrayList<>();
		if (produto != null) {
			queryEdicoesRecebidas += " AND PE.ID = :PRODUTO_EDICAO_ID ";
		}

		try {
			Map<String, Object> params = new HashMap<>();
			params.put("COTA_ID", cota.getId());
			if (produto != null) {
				params.put("PRODUTO_EDICAO_ID", produto.getId());
			}
			SqlRowSet rs = jdbcTemplate.queryForRowSet(queryEdicoesRecebidas, params);

			while (rs.next()) {
				ProdutoEdicaoEstudo edicao = new ProdutoEdicaoEstudo();
				edicao.setId(rs.getLong("pedId"));
				edicao.setNumeroEdicao(rs.getLong("NUMERO_EDICAO"));
				edicao.setReparte(rs.getBigDecimal("QTDE_RECEBIDA"));
				edicao.setVenda(edicao.getReparte().subtract(rs.getBigDecimal("QTDE_DEVOLVIDA")));
				edicao.setParcial(rs.getBoolean("PARCIAL"));
				edicao.setIndicePeso(rs.getBigDecimal("PESO"));
				edicao.setColecao(rs.getInt("IS_COLECAO") == 1);
				edicoes.add(edicao);
			}
		} catch (Exception ex) {
            LOGGER.error("Ocorreu um erro ao tentar consultar as edições recebidas por essa cota", ex);

		}
		return edicoes;
	}

	public int getQtdeVezesReenviadas(CotaEstudo cota, ProdutoEdicaoEstudo produtoEdicao) {
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("COTA_ID", cota.getId());
			params.put("PRODUTO_EDICAO_ID", produtoEdicao.getId());

			SqlRowSet rs = jdbcTemplate.queryForRowSet(queryQtdeVezesReenviadas, params);
			if (rs.next()) {
				return rs.getInt(1);
			}
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao tentar consultar as edições recebidas por essa cota", e);
		}
		return 0;
	}

	public ProdutoEdicaoEstudo getProdutoEdicaoEstudo(String codigoProduto, Long numeroEdicao, Long idLancamento) {
		Map<String, Object> params = new HashMap<>();
		params.put("CODIGO_PRODUTO", codigoProduto);
		params.put("NUMERO_EDICAO", numeroEdicao);
		params.put("LANCAMENTO_ID", idLancamento);

        try {
            return jdbcTemplate.queryForObject(queryProdutoEdicaoEstudo, params, new RowMapper<ProdutoEdicaoEstudo>() {
                @Override
                public ProdutoEdicaoEstudo mapRow(ResultSet rs, int rowNum) throws SQLException {
                    ProdutoEdicaoEstudo produtoEdicaoBase = new ProdutoEdicaoEstudo();
                    produtoEdicaoBase.setId(rs.getLong("ID"));
                    produtoEdicaoBase.setProduto(new Produto());
                    produtoEdicaoBase.getProduto().setId(rs.getLong("PRODUTO_ID"));
                    produtoEdicaoBase.getProduto().setCodigoICD(rs.getString("CODIGO_ICD"));
                    produtoEdicaoBase.setNumeroEdicao(rs.getLong("NUMERO_EDICAO"));
                    produtoEdicaoBase.setPacotePadrao(rs.getInt("PACOTE_PADRAO"));
                    produtoEdicaoBase.getProduto().setCodigo(rs.getString("CODIGO"));
                    produtoEdicaoBase.setDataLancamento(rs.getDate("DATA_LCTO_DISTRIBUIDOR"));
                    produtoEdicaoBase.setIdLancamento(rs.getLong("LANCAMENTO_ID"));
                    produtoEdicaoBase.setPeriodo(rs.getInt("NUMERO_PERIODO"));
                    if (rs.getString("GRUPO_PRODUTO") != null && rs.getString("GRUPO_PRODUTO").equalsIgnoreCase(PRODUTO_COLECIONAVEL)) {
                        produtoEdicaoBase.setColecao(true);
                    }
                    produtoEdicaoBase.getProduto().setTipoSegmentoProduto(getTipoSegmentoProduto(rs.getLong("TIPO_SEGMENTO_PRODUTO_ID")));
                    produtoEdicaoBase.setTipoClassificacaoProduto(getTipoClassificacaoProduto(rs.getLong("TIPO_CLASSIFICACAO_PRODUTO_ID")));
                    return produtoEdicaoBase;
                }

                private TipoClassificacaoProduto getTipoClassificacaoProduto(long tipo_classificacao_produto_id) {
                    TipoClassificacaoProduto classificacaoProduto = new TipoClassificacaoProduto();
                    classificacaoProduto.setId(tipo_classificacao_produto_id);
                    return classificacaoProduto;
                }

                private TipoSegmentoProduto getTipoSegmentoProduto(long idTipoSegmentoProduto) {
                    TipoSegmentoProduto tipoSegmentoProduto = new TipoSegmentoProduto();
                    tipoSegmentoProduto.setId(idTipoSegmentoProduto);
                    return tipoSegmentoProduto;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            String msg = "Não encontrou o produto[" + codigoProduto + "] edição[" + numeroEdicao + "] do estudo.";
            LOGGER.error(msg, e);
            throw new ValidacaoException(TipoMensagem.ERROR, msg);
        }
    }
}
