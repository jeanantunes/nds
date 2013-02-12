package br.com.abril.nds.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;

public class ProdutoEdicaoDAO {
    
    private static final Logger log = LoggerFactory.getLogger(ProdutoEdicaoDAO.class);

    public List<ProdutoEdicao> getEdicaoRecebidas(Cota cota) {
	List<ProdutoEdicao> edicoes = new ArrayList<ProdutoEdicao>();
	try {
	    PreparedStatement psmt = Conexao.getConexao().prepareStatement("" +
		    "SELECT PE.NUMERO_EDICAO, EPC.QTDE_RECEBIDA, EPC.QTDE_DEVOLVIDA, PE.PARCIAL, PE.PACOTE_PADRAO, PE.PESO " +
		    "  FROM ESTOQUE_PRODUTO_COTA EPC " +
		    "  JOIN PRODUTO_EDICAO PE ON PE.ID = EPC.PRODUTO_EDICAO_ID " +
		    " WHERE EPC.COTA_ID = ? ");
	    psmt.setLong(1, cota.getId());
	    ResultSet rs = psmt.executeQuery();
	    while (rs.next()) {
		ProdutoEdicao edicao = new ProdutoEdicao();
		edicao.setNumeroEdicao(rs.getLong("NUMERO_EDICAO"));
		edicao.setReparte(rs.getBigDecimal("QTDE_RECEBIDA"));
		edicao.setVenda(edicao.getReparte().subtract(rs.getBigDecimal("QTDE_DEVOLVIDA")));
		edicao.setParcial(rs.getInt("PARCIAL") == 1);
		edicao.setPacotePadrao(rs.getInt("PACOTE_PADRAO"));
		edicao.setPeso(rs.getInt("PESO"));

		edicoes.add(edicao);
	    }
	} catch (Exception ex) {
	    log.error("Ocorreu um erro ao tentar consultar as edições recebidas por essa cota", ex);
	}
	return edicoes;
    }
}
