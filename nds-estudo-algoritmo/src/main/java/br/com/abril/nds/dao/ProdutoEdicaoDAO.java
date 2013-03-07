package br.com.abril.nds.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.model.ProdutoEdicaoBase;
import br.com.abril.nds.model.TipoSegmentoProduto;
import br.com.abril.nds.util.PropertyLoader;

public class ProdutoEdicaoDAO {

    private static final Logger log = LoggerFactory.getLogger(ProdutoEdicaoDAO.class);

    private String queryUltimoProdutoEdicao, queryEdicoesRecebidas, queryQtdeVezesReenviadas;

    public ProdutoEdicaoDAO() {
	queryUltimoProdutoEdicao = PropertyLoader.getProperty("queryUltimoProdutoEdicao");
	queryEdicoesRecebidas = PropertyLoader.getProperty("queryEdicoesRecebidas");
	queryQtdeVezesReenviadas = PropertyLoader.getProperty("queryQtdeVezesReenviadas");
    }

    public List<ProdutoEdicao> getEdicaoRecebidas(Cota cota) {
	return getEdicaoRecebidas(cota, null);
    }

    public List<ProdutoEdicao> getEdicaoRecebidas(Cota cota, ProdutoEdicao produto) {
	
	List<ProdutoEdicao> edicoes = new ArrayList<ProdutoEdicao>();
	if (produto != null) {
	    queryEdicoesRecebidas += " AND PE.ID = ? ";
	}

	try {
	    PreparedStatement psmt = Conexao.getConexao().prepareStatement(queryEdicoesRecebidas);
	    psmt.setLong(1, cota.getId());
	    if (produto != null) {
		psmt.setLong(2, produto.getId());
	    }
	    ResultSet rs = psmt.executeQuery();
	    while (rs.next()) {
		ProdutoEdicao edicao = new ProdutoEdicao();
		edicao.setId(rs.getLong("pedId"));
		edicao.setNumeroEdicao(rs.getLong("NUMERO_EDICAO"));
		edicao.setReparte(rs.getBigDecimal("QTDE_RECEBIDA"));
		edicao.setVenda(edicao.getReparte().subtract(rs.getBigDecimal("QTDE_DEVOLVIDA")));
		edicao.setParcial(rs.getInt("PARCIAL") == 1);
		edicao.setPeso(rs.getInt("PESO"));
		edicao.setColecao(rs.getInt("IS_COLECAO") == 1);

		edicao.setTipoSegmentoProduto(new TipoSegmentoProduto(rs.getLong("TIPO_SEGMENTO_PRODUTO_ID"), rs.getString("TIPO_SEGMENTO_PRODUTO_DESC")));
		
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
	    PreparedStatement psmt = Conexao.getConexao().prepareStatement(queryQtdeVezesReenviadas);
	    psmt.setLong(1, cota.getId());
	    psmt.setLong(2, produtoEdicao.getId());

	    ResultSet rs = psmt.executeQuery();
	    rs.next();
	    return rs.getInt(1);

	} catch (Exception ex) {
	    ex.printStackTrace();
	    System.out.println("Ocorreu um erro ao tentar consultar as edições recebidas por essa cota");
	}
	return 0;
    }

    public ProdutoEdicaoBase getLastProdutoEdicaoByIdProduto(String codigoProduto) {
	ProdutoEdicaoBase produtoEdicaoBase = new ProdutoEdicaoBase();
	try {
	    PreparedStatement ps = Conexao.getConexao().prepareStatement(queryUltimoProdutoEdicao);
	    ps.setString(1, codigoProduto);
	    ResultSet rs = ps.executeQuery();
	    while (rs.next()) {
		produtoEdicaoBase.setId(rs.getLong("ID"));
		produtoEdicaoBase.setIdProduto(rs.getLong("PRODUTO_ID"));
		produtoEdicaoBase.setNumeroEdicao(rs.getLong("NUMERO_EDICAO"));
		produtoEdicaoBase.setCodigoProduto(rs.getString("CODIGO"));
		produtoEdicaoBase.setDataLancamento(rs.getDate("DATA_LCTO_DISTRIBUIDOR"));
		produtoEdicaoBase.setIdLancamento(rs.getLong("LANCAMENTO_ID"));
	    }
	} catch (ClassNotFoundException | SQLException e) {
	    e.printStackTrace();
	}
	return produtoEdicaoBase;
    }
}
