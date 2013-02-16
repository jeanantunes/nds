package br.com.abril.nds.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;

public class MovimentoEstoqueCotaDAO {
	
	public BigDecimal retornarReparteJuramentadoAFaturar(Cota cota,ProdutoEdicao produtoEdicao){
		BigDecimal valorJuramentado = BigDecimal.ZERO;
		
		try {
			PreparedStatement psmt = Conexao
				    .getConexao()
				    .prepareStatement(
					    "select  sum(QTDE) from movimento_estoque_cota mec where TIPO_MOVIMENTO_ID=? " +
					    " and mec.cota_id=?" +
					    " and mec.produto_edica_id=?" +
					    "order by ID");
			
			int idx=0;        
			psmt.setLong(idx++, 21);
			psmt.setLong(idx++, cota.getId());
			psmt.setLong(idx++, produtoEdicao.getId());
			
			ResultSet rs = psmt.executeQuery();
			
			while(rs.next()){
				valorJuramentado = rs.getBigDecimal(0);
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		return valorJuramentado;
		
	}

}

