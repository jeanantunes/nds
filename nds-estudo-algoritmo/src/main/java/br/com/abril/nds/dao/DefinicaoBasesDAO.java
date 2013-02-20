package br.com.abril.nds.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.MonthDay;
import org.joda.time.YearMonth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.abril.nds.enumerators.DataReferencia;
import br.com.abril.nds.model.ProdutoEdicao;

public class DefinicaoBasesDAO {

    private static final Logger log = LoggerFactory.getLogger(DefinicaoBasesDAO.class);

    private static final String LANCAMENTO_PARCIAL = "PARCIAL";
    private static final String PRODUTO_COLECIONAVEL = "COLECIONAVEL";
    private static final String STATUS_FECHADO = "FECHADO";
    
    private static final String CAMPOS_MODEL_PRODUTO_EDICAO = " select l.ID, l.PRODUTO_EDICAO_ID, l.STATUS, l.DATA_LCTO_DISTRIBUIDOR, tp.GRUPO_PRODUTO, " 
	    + " l.TIPO_LANCAMENTO, pe.NUMERO_EDICAO, p.CODIGO "
	    + " from lancamento l "
	    + " left join produto_edicao pe on pe.ID = l.PRODUTO_EDICAO_ID "
	    + " left outer join produto p on p.ID = pe.PRODUTO_ID "
	    + " left outer join tipo_produto tp on tp.ID = p.TIPO_PRODUTO_ID "
	    + " where pe.PRODUTO_ID = ( select ID from produto where CODIGO = ? ) ";
//    	    + " where pe.PRODUTO_ID = ( select PRODUTO_ID from produto_edicao where ID = ? ) ";

    private static final String SQL_EDICOES_VS_LANCAMENTOS = CAMPOS_MODEL_PRODUTO_EDICAO 
	    //	    + " and l.DATA_LCTO_DISTRIBUIDOR > date_sub(curdate(),INTERVAL 2 YEAR) "
	    + " order by l.DATA_LCTO_DISTRIBUIDOR desc "
	    + " limit 6 ";

    private static final String SQL_LANCAMENTOS_ANOS_ANTERIORES_MESMO_MES = CAMPOS_MODEL_PRODUTO_EDICAO
	    + " and (l.DATA_LCTO_DISTRIBUIDOR like ? or l.DATA_LCTO_DISTRIBUIDOR like ?) "
	    + " order by l.DATA_LCTO_DISTRIBUIDOR desc "
	    + " limit 2 ";

    private static final String SQL_LANCAMENTOS_ANOS_ANTERIORES_VERANEIO = CAMPOS_MODEL_PRODUTO_EDICAO
	    + " and (l.DATA_LCTO_DISTRIBUIDOR between ? and ? " 	//periodo de 20/12 a 28/02 um ano antes
	    + "      or l.DATA_LCTO_DISTRIBUIDOR between ? and ?) "	//periodo de 20/12 a 28/02 dois anos antes
	    + " order by l.DATA_LCTO_DISTRIBUIDOR desc "
	    + " limit 2 ";

    private static final String SQL_COTA_VS_EDICAO = "select "
	    + "    c.id as COTA_ID "
	    + "    ,p.id as PRODUTO_ID "
	    + "    ,pe.id as PRODUTO_EDICAO_ID "
	    + "    ,pe.PARCIAL "
	    + "    ,l.id as LANCAMENTO_ID "
	    + "    ,l.STATUS "
	    + "    ,tp.GRUPO_PRODUTO "
	    + "    ,epc.QTDE_DEVOLVIDA "
	    + "    ,epc.QTDE_RECEBIDA "
	    + " from "
	    + "    produto_edicao pe "
	    + "    ,produto p "
	    + "    ,lancamento l "
	    + "    ,tipo_produto tp "
	    + "    ,cota c "
	    + "    ,estoque_produto_cota epc "
	    + " where "
	    + "    pe.id = ? "
	    + "    and pe.PRODUTO_ID = p.id "
	    + "    and pe.ID = l.PRODUTO_EDICAO_ID "
	    + "    and tp.ID = p.TIPO_PRODUTO_ID "
	    + "    and pe.ID = epc.PRODUTO_EDICAO_ID "
	    + "    and c.ID = epc.COTA_ID ";

    public List<ProdutoEdicao> listaEdicoesPorLancamento(ProdutoEdicao edicao) {
	List<ProdutoEdicao> edicoes = new ArrayList<>();
	try {
	    PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_EDICOES_VS_LANCAMENTOS);
	    ps.setLong(1, edicao.getCodigoProduto());
	    ResultSet rs = ps.executeQuery();
	    while(rs.next()) {
		edicoes.add(produtoEdicaoMapper(rs));
	    }
	} catch (ClassNotFoundException | SQLException e) {
	    log.error("Erro os listar edições por lançamento.", e);
	}
	return edicoes;
    }

    public List<ProdutoEdicao> listaEdicoesAnosAnteriores(ProdutoEdicao edicao, boolean mesmoMes, List<LocalDate> dataReferencias) {
	List<ProdutoEdicao> edicoes = new ArrayList<ProdutoEdicao>();
	try {
	    PreparedStatement ps = Conexao.getConexao().prepareStatement(
		    mesmoMes?
			    SQL_LANCAMENTOS_ANOS_ANTERIORES_MESMO_MES:
				SQL_LANCAMENTOS_ANOS_ANTERIORES_VERANEIO);
	    int index = 0;
	    ps.setLong(++index, edicao.getCodigoProduto());
	    if(mesmoMes) {
		ps.setString(++index, anoMesAnteriorSQL(edicao.getDataLancamento(), 1));
		ps.setString(++index, anoMesAnteriorSQL(edicao.getDataLancamento(), 2));
	    } else {
		
		ps.setString(++index, periodoVeraneio(edicao.getDataLancamento(), 2, DataReferencia.DEZEMBRO_20));
		ps.setString(++index, periodoVeraneio(edicao.getDataLancamento(), 1, DataReferencia.FEVEREIRO_28));
		ps.setString(++index, periodoVeraneio(edicao.getDataLancamento(), 4, DataReferencia.DEZEMBRO_20));
		ps.setString(++index, periodoVeraneio(edicao.getDataLancamento(), 3, DataReferencia.FEVEREIRO_28));
	    }
	    ResultSet rs = ps.executeQuery();
	    while (rs.next()) {
		edicoes.add(produtoEdicaoMapper(rs));
	    }
	} catch (ClassNotFoundException | SQLException e) {
	    LocalDate ld = new LocalDate(edicao.getDataLancamento());
	    log.error("Erro ao obter edições de veraneio dos anos anteriores. Data lançamento base:" + ld.toString(), e);
	}
	return edicoes;
    }

    public List<ProdutoEdicao> listaEdicoesPorCota(ProdutoEdicao edicao) {
	List<ProdutoEdicao> edicoes = new ArrayList<ProdutoEdicao>();
	try {
	    PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_COTA_VS_EDICAO);
	    ps.setLong(1, edicao.getId());
	    ResultSet rs = ps.executeQuery();
	    while(rs.next()) {
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		produtoEdicao.setId(rs.getLong("PRODUTO_EDICAO_ID"));
		produtoEdicao.setIdProduto(rs.getLong("PRODUTO_ID"));
		produtoEdicao.setIdLancamento(rs.getLong("LANCAMENTO_ID"));
		produtoEdicao.setReparte(rs.getBigDecimal("QTDE_RECEBIDA"));
		produtoEdicao.setVenda((produtoEdicao.getReparte().subtract(rs.getBigDecimal("QTDE_DEVOLVIDA"))));
		produtoEdicao.setEdicaoAberta(traduzStatus(rs.getNString("STATUS")));
		produtoEdicao.setColecao(traduzColecionavel(rs.getNString("GRUPO_PRODUTO")));
		produtoEdicao.setParcial(rs.getInt(LANCAMENTO_PARCIAL) == 1);

		edicoes.add(produtoEdicao);
	    }
	} catch (ClassNotFoundException | SQLException e) {
	    log.error("Error fetching objects from DB.", e);
	}
	return edicoes;
    }

    private boolean traduzColecionavel(String grupoProduto) {
	if(grupoProduto != null && grupoProduto.equalsIgnoreCase(PRODUTO_COLECIONAVEL)) {
	    return true;
	}
	return false;
    }

    private boolean traduzStatus(String status) {
	if(status != null && !status.equalsIgnoreCase(STATUS_FECHADO)) {
	    return true;
	}
	return false;
    }

    private String anoMesAnteriorSQL(Date dataLancamento, int anosSubtrair) {
	YearMonth ym = new YearMonth(dataLancamento);
	return ym.minusYears(anosSubtrair).toString().concat("-%");
    }

    private String periodoVeraneio(Date dataLancamento, int anosSubtrair, DataReferencia dataReferencia) {
	return MonthDay.parse(dataReferencia.getData()).toLocalDate(LocalDate.fromDateFields(dataLancamento).minusYears(anosSubtrair).getYear()).toString();
    }

    private ProdutoEdicao produtoEdicaoMapper(ResultSet rs) throws SQLException {
	ProdutoEdicao produtoEdicao = new ProdutoEdicao();
	produtoEdicao.setId(rs.getLong("PRODUTO_EDICAO_ID"));
	produtoEdicao.setIdLancamento(rs.getLong("ID"));
	produtoEdicao.setEdicaoAberta(traduzStatus(rs.getNString("STATUS")));
	produtoEdicao.setDataLancamento(rs.getDate("DATA_LCTO_DISTRIBUIDOR"));
	produtoEdicao.setColecao(traduzColecionavel(rs.getNString("GRUPO_PRODUTO")));
	produtoEdicao.setParcial(rs.getString("TIPO_LANCAMENTO").equalsIgnoreCase(LANCAMENTO_PARCIAL));
	produtoEdicao.setNumeroEdicao(rs.getLong("NUMERO_EDICAO"));
	produtoEdicao.setCodigoProduto(rs.getLong("CODIGO"));
	return produtoEdicao;
    }
}
