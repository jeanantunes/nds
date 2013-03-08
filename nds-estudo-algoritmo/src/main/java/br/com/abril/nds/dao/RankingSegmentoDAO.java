package br.com.abril.nds.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.model.ProdutoEdicaoBase;

public class RankingSegmentoDAO {
	
	/*final String SQL_RETURN_ULTIMO_ID_RANKING_GERADO = "SELECT max(id) from ranking_segmento_gerados";
	
	public Long getUltimoIDRankingSegmentoGerado(){
		
		Long d = null;
		try {
			StringBuffer sb = new StringBuffer(SQL_RETURN_ULTIMO_ID_RANKING_GERADO);
			
			PreparedStatement psmt = Conexao.getConexao().prepareStatement(
					sb.toString());
			ResultSet rs = psmt.executeQuery();
			while (rs.next()) {
				d = rs.getLong(1);
			}
		} catch (Exception ex) {
			System.out.println("Ocorreu um erro ao tentar consultar");
			ex.printStackTrace();
		}
		
		return d;
	}
	
*/	
	private String sqlRankingSegmentoEdicoesBase(int qtde){
		
		StringBuilder SQL_RANKING_SEGMENTO_EDICOES_BASE = new StringBuilder()
				.append(" ( select cota_id,produto_edicao_id from ranking_segmento rs ") 
				.append(" where rs.RANKING_SEGMENTO_GERADOS_ID=(select max(rsg.id) from ranking_segmento_gerados rsg) ")
				.append(" and rs.produto_edicao_id in (:idBase1) ")
				.append(" ) ")
				.append(" union ")
				.append(" ( ")
				.append(" select ") 
				.append(" COTA_ID,produto_edicao_id ")
				.append(" from  movimento_estoque_cota mec ")
				.append(" where ") 
				.append(" mec.tipo_movimento_id=21 ")
				.append(" and mec.produto_edicao_id in (:idBase2) ");
		
		if(qtde>=3){
			SQL_RANKING_SEGMENTO_EDICOES_BASE.append(" and mec.qtde >= ").append(qtde).append(")");
			
		}else{
			SQL_RANKING_SEGMENTO_EDICOES_BASE.append(" and and mec.qtde = ").append(qtde).append(")");
			
		}
		return SQL_RANKING_SEGMENTO_EDICOES_BASE.toString();
		
	}

	public List<Long> getCotasOrdenadaPorSegmentoEdicaoAberta(List<Cota> cotaList,ProdutoEdicao produtoEdicao,List<Long>idEdicoesBase) {

		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT distinct COTA_ID FROM RANKING_SEGMENTO WHERE RANKING_SEGMENTO_GERADOS_ID = (select max(id) from ranking_segmento_gerados) and COTA_ID in (?) ");
		
		List<Long> idList = new ArrayList<Long>();
		for (Cota c : cotaList) {
			idList.add(c.getId());
		}
		
		try {
			
			PreparedStatement psmt = Conexao.getConexao().prepareStatement(
					sb.toString());
			
			psmt.setString(1, StringUtils.join(idList,","));
			
			idList.clear();
			
			ResultSet rs = psmt.executeQuery();
			while (rs.next()) {
				idList.add(rs.getLong(1));
			}
			
			
		} catch (Exception ex) {
			System.out.println("Ocorreu um erro ao tentar consultar as cotas");
			ex.printStackTrace();
		}
		
		return idList;
	}

	public List<Long> getCotasOrdenadaPorSegmentoSemEdicaoBase(List<Cota> cotaList,List<ProdutoEdicaoBase> edicoesBase) {

		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT distinct COTA_ID FROM RANKING_SEGMENTO WHERE RANKING_SEGMENTO_GERADOS_ID = (select max(id) from ranking_segmento_gerados) and COTA_ID in (?)")
		.append("and produto_edicao_id not in(?) ");
		
		List<Long> idList = new ArrayList<Long>();
		List<Long> idListEdicoesBase = new ArrayList<Long>();
		
		for (Cota c : cotaList) {
			idList.add(c.getId());
		}
		
		for (ProdutoEdicaoBase c : edicoesBase) {
			idListEdicoesBase.add(c.getId());
		}
		
		try {
			
			PreparedStatement psmt = Conexao.getConexao().prepareStatement(
					sb.toString());
			
			psmt.setString(1, StringUtils.join(idList,","));
			psmt.setString(1, StringUtils.join(idListEdicoesBase,","));
			
			idList.clear();
			
			ResultSet rs = psmt.executeQuery();
			while (rs.next()) {
				idList.add(rs.getLong(1));
			}
			
			
		} catch (Exception ex) {
			System.out.println("Ocorreu um erro ao tentar consultar as cotas");
			ex.printStackTrace();
		}
		
		return idList;
	}
	
	public List<Long> getCotasOrdenadaPorSegmento(List<Cota> cList,List<ProdutoEdicaoBase> edicoesBase,int qtde) {
		
		List<Long> idList = new ArrayList<Long>();
		for (ProdutoEdicaoBase c : edicoesBase) {
			idList.add(c.getId());
		}
		
		try {
			
			PreparedStatement psmt = Conexao.getConexao().prepareStatement(
					sqlRankingSegmentoEdicoesBase(qtde));
			
			int idx = 1;
			String join = StringUtils.join(idList,",");
			psmt.setString(idx++, join);
			psmt.setString(idx++, join);
			
			
			ResultSet rs = psmt.executeQuery();
			idList.clear();
			
			while (rs.next()) {
				idList.add(rs.getLong(idx));
			}
		} catch (Exception ex) {
			System.out.println("Ocorreu um erro ao tentar consultar");
			ex.printStackTrace();
		}
		
		return idList;
	}

}
