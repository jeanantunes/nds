package br.com.abril.nds.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.model.ProdutoEdicaoBase;

public class MovimentoEstoqueCotaDAO {
	
	public BigDecimal retornarReparteJuramentadoAFaturar(Cota cota, ProdutoEdicaoBase produtoEdicao){
		BigDecimal valorJuramentado = BigDecimal.ZERO;
		
		try {
			StringBuilder string = new StringBuilder("select  sum(QTDE) from movimento_estoque_cota mec where TIPO_MOVIMENTO_ID=? ") 
			.append("  and mec.cota_id=? ")  
			.append("  and mec.produto_edicao_id=? ") 
			.append(" order by mec.ID ");
			
			PreparedStatement psmt = Conexao.getConexao().prepareStatement(
					string.toString());
			
			int idx=1;        
			psmt.setLong(idx++, 21);
			psmt.setLong(idx++, cota.getId());
			psmt.setLong(idx++, produtoEdicao.getId());
			
			ResultSet rs = psmt.executeQuery();
			
			while(rs.next()){
				valorJuramentado = rs.getBigDecimal(1);
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		return valorJuramentado;
		
	}
	
	
	public BigDecimal retornarUltimaVendaFechada(ProdutoEdicao produtoEdicaoId){
		BigDecimal qtdeUltimaVenda = BigDecimal.ZERO;
		
		try {
			PreparedStatement psmt = Conexao
				    .getConexao()
				    .prepareStatement(
					    " select me.data,me.qtde-COALESCE(ep.QTDE_DEVOLUCAO_FORNECEDOR,0) AS QTDE_VENDA,ep.PRODUTO_EDICAO_ID  "+ 
							" from movimento_estoque me "+
							" join estoque_produto ep ON ep.ID = me.ESTOQUE_PRODUTO_ID "+
							" where me.TIPO_MOVIMENTO_ID= 13 "+
							" and ep.PRODUTO_EDICAO_ID = ? "+ 
							" order by me.data desc limit 1");			
			int idx=1;        
			psmt.setLong(idx++, produtoEdicaoId.getId());
			
			ResultSet rs = psmt.executeQuery();
			
			while(rs.next()){
				qtdeUltimaVenda = rs.getBigDecimal(2);
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		return qtdeUltimaVenda;
	}
}