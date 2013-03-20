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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.model.ProdutoEdicaoBase;
import br.com.abril.nds.model.TipoSegmentoProduto;

@Repository
public class ProdutoEdicaoDAO {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Value("#{query_estudo.queryUltimoProdutoEdicao}")
    private String queryUltimoProdutoEdicao;

    @Value("#{query_estudo.queryEdicoesRecebidas}")
    private String queryEdicoesRecebidas;

    @Value("#{query_estudo.queryQtdeVezesReenviadas}")
    private String queryQtdeVezesReenviadas;

    private static final Logger log = LoggerFactory.getLogger(ProdutoEdicaoDAO.class);

    public List<ProdutoEdicao> getEdicaoRecebidas(Cota cota) {
	return getEdicaoRecebidas(cota, null);
    }

    public List<ProdutoEdicao> getEdicaoRecebidas(Cota cota, ProdutoEdicao produto) {

	List<ProdutoEdicao> edicoes = new ArrayList<ProdutoEdicao>();
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
		ProdutoEdicao edicao = new ProdutoEdicao();
		edicao.setId(rs.getLong("pedId"));
		edicao.setNumeroEdicao(rs.getLong("NUMERO_EDICAO"));
		edicao.setReparte(rs.getBigDecimal("QTDE_RECEBIDA"));
		edicao.setVenda(edicao.getReparte().subtract(rs.getBigDecimal("QTDE_DEVOLVIDA")));
		edicao.setParcial(rs.getBoolean("PARCIAL"));
		edicao.setPeso(rs.getBigDecimal("PESO"));
		edicao.setColecao(rs.getInt("IS_COLECAO") == 1);
		edicoes.add(edicao);
	    }
	} catch (Exception ex) {
	    log.error("Ocorreu um erro ao tentar consultar as edições recebidas por essa cota", ex);
	    ex.printStackTrace();
	}
	return edicoes;
    }

    public int getQtdeVezesReenviadas(Cota cota, ProdutoEdicaoBase produtoEdicao) {
	try {
	    Map<String, Object> params = new HashMap<>();
	    params.put("COTA_ID", cota.getId());
	    params.put("PRODUTO_EDICAO_ID", produtoEdicao.getId());
	    
	    SqlRowSet rs = jdbcTemplate.queryForRowSet(queryQtdeVezesReenviadas, params);
	    if (rs.next()) {
		return rs.getInt(1);
	    }
	    return 0;
	} catch (Exception ex) {
	    ex.printStackTrace();
	    System.out.println("Ocorreu um erro ao tentar consultar as edições recebidas por essa cota");
	}
	return 0;
    }

    public ProdutoEdicaoBase getLastProdutoEdicaoByIdProduto(String codigoProduto) {
	Map<String, Object> params = new HashMap<>();
	params.put("CODIGO_PRODUTO", codigoProduto);
	return jdbcTemplate.queryForObject(queryUltimoProdutoEdicao, params, new RowMapper<ProdutoEdicaoBase>() {
	    @Override
	    public ProdutoEdicaoBase mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProdutoEdicaoBase produtoEdicaoBase = new ProdutoEdicaoBase();
		produtoEdicaoBase.setId(rs.getLong("ID"));
		produtoEdicaoBase.setIdProduto(rs.getLong("PRODUTO_ID"));
		produtoEdicaoBase.setNumeroEdicao(rs.getLong("NUMERO_EDICAO"));
		produtoEdicaoBase.setPacotePadrao(rs.getBigDecimal("PACOTE_PADRAO"));
		produtoEdicaoBase.setCodigoProduto(rs.getString("CODIGO"));
		produtoEdicaoBase.setDataLancamento(rs.getDate("DATA_LCTO_DISTRIBUIDOR"));
		produtoEdicaoBase.setIdLancamento(rs.getLong("LANCAMENTO_ID"));
		produtoEdicaoBase.setTipoSegmentoProduto(new TipoSegmentoProduto(rs.getLong("TIPO_SEGMENTO_PRODUTO_ID"), null));
		return produtoEdicaoBase;
	    }
	});
    }
}
