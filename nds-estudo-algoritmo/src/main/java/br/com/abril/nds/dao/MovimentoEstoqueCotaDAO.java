package br.com.abril.nds.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicaoBase;

@Repository
public class MovimentoEstoqueCotaDAO {

    @Autowired
    private DataSource dataSource;
    
    public BigDecimal retornarReparteJuramentadoAFaturar(Cota cota, ProdutoEdicaoBase produtoEdicao) {
	BigDecimal valorJuramentado = BigDecimal.ZERO;

	try {
	    PreparedStatement psmt = dataSource.getConnection().prepareStatement(
		    "select  sum(QTDE) from movimento_estoque_cota mec where TIPO_MOVIMENTO_ID=? " + " and mec.cota_id=?"
			    + " and mec.produto_edica_id=?" + "order by ID");

	    int idx = 1;
	    psmt.setLong(idx++, 21);
	    psmt.setLong(idx++, cota.getId());
	    psmt.setLong(idx++, produtoEdicao.getId());

	    ResultSet rs = psmt.executeQuery();

	    while (rs.next()) {
		valorJuramentado = rs.getBigDecimal(1);
	    }

	} catch (SQLException e) {
	    e.printStackTrace();
	}

	return valorJuramentado;

    }
}