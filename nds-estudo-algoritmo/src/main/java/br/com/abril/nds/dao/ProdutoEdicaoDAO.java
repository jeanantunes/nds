package br.com.abril.nds.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.model.ProdutoEdicaoBase;

@Repository
public class ProdutoEdicaoDAO {

    private static final Logger log = LoggerFactory.getLogger(ProdutoEdicaoDAO.class);
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<ProdutoEdicao> getEdicaoRecebidas(Cota cota) {
	return getEdicaoRecebidas(cota, null);
    }

    public List<ProdutoEdicao> getEdicaoRecebidas(Cota cota,
	    ProdutoEdicao produto) {
	List<ProdutoEdicao> edicoes = new ArrayList<ProdutoEdicao>();

	StringBuilder sb = new StringBuilder();
	sb.append(" SELECT P.NOME, PE.NUMERO_EDICAO, EPC.QTDE_RECEBIDA, EPC.QTDE_DEVOLVIDA, PE.PARCIAL, PE.PACOTE_PADRAO, PE.PESO, PE.ID as pedId ");
	sb.append(" , (CASE WHEN EXISTS(SELECT '.' FROM TIPO_PRODUTO TP WHERE TP.ID = P.TIPO_PRODUTO_ID AND TP.GRUPO_PRODUTO = 'COLECIONAVEL') THEN 1 ELSE 0 END) IS_COLECAO ");
	sb.append(" FROM ESTOQUE_PRODUTO_COTA EPC JOIN PRODUTO_EDICAO PE ON PE.ID = EPC.PRODUTO_EDICAO_ID JOIN PRODUTO P ON P.ID = PE.PRODUTO_ID ");
	sb.append(" WHERE EPC.COTA_ID = ? ");
	if (produto != null) {
	    sb.append(" AND PE.ID = ? ");
	}

	try {
	    PreparedStatement psmt = Conexao.getConexao().prepareStatement(
		    sb.toString());
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
		edicao.setVenda(edicao.getReparte().subtract(
			rs.getBigDecimal("QTDE_DEVOLVIDA")));
		edicao.setParcial(rs.getInt("PARCIAL") == 1);
		edicao.setPeso(rs.getInt("PESO"));
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
			PreparedStatement psmt = Conexao.getConexao().prepareStatement(
					"" + "SELECT count(EPC.id) " + "  FROM ESTOQUE_PRODUTO_COTA EPC "
							+ "  JOIN PRODUTO_EDICAO PE ON PE.ID = EPC.PRODUTO_EDICAO_ID " + " WHERE EPC.COTA_ID = ? AND " + " PE.ID = ?"
							+ " and PE.parcial=1" + "");
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

	public ProdutoEdicaoBase getLastProdutoEdicaoByIdProduto(long idProduto) {
		ProdutoEdicaoBase produtoEdicaoBase = new ProdutoEdicaoBase();
		try {
			PreparedStatement ps = Conexao.getConexao().prepareStatement("select pe.ID, pe.PRODUTO_ID, pe.NUMERO_EDICAO, p.CODIGO " 
					+ " from produto_edicao pe,     "
					+ " produto p                   "
					+ " where p.CODIGO = ?          "
					+ " and pe.PRODUTO_ID = p.ID    "
					+ " order by NUMERO_EDICAO desc "
					+ " limit 1                     ");
			ps.setLong(1, idProduto);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				produtoEdicaoBase.setId(rs.getLong("ID"));
				produtoEdicaoBase.setIdProduto(rs.getLong("PRODUTO_ID"));
				produtoEdicaoBase.setNumeroEdicao(rs.getLong("NUMERO_EDICAO"));
				produtoEdicaoBase.setCodigoProduto(rs.getLong("CODIGO"));
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return produtoEdicaoBase;
	}
}
