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
import org.joda.time.Years;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.abril.nds.enumerators.DataReferencia;
import br.com.abril.nds.model.ProdutoEdicaoBase;
import br.com.abril.nds.util.PropertyLoader;

public class DefinicaoBasesDAO {

    private static final Logger log = LoggerFactory.getLogger(DefinicaoBasesDAO.class);

    private static final String LANCAMENTO_PARCIAL = "PARCIAL";
    private static final String PRODUTO_COLECIONAVEL = "COLECIONAVEL";
    private static final String STATUS_FECHADO = "FECHADO";

    private String queryModelProdutoEdicao, queryEdicoesLancamentos, queryLancamentosAnosAnterioresMesmoMes, queryLancamentosAnosAnterioresVeraneio;

    public DefinicaoBasesDAO() {
	queryModelProdutoEdicao = PropertyLoader.getProperty("queryModelProdutoEdicao");
	queryEdicoesLancamentos = queryModelProdutoEdicao + " order by pe.NUMERO_EDICAO desc, plp.NUMERO_PERIODO desc limit 50 ";
	queryLancamentosAnosAnterioresMesmoMes = queryModelProdutoEdicao
		+ " and (l.DATA_LCTO_DISTRIBUIDOR like ? or l.DATA_LCTO_DISTRIBUIDOR like ?) "
		+ " order by pe.NUMERO_EDICAO desc, plp.NUMERO_PERIODO desc limit 2 ";
	queryLancamentosAnosAnterioresVeraneio = queryModelProdutoEdicao
		+ " and (l.DATA_LCTO_DISTRIBUIDOR between ? and ? " // periodo de 20/12 a 28/02 um ano antes
		+ "      or l.DATA_LCTO_DISTRIBUIDOR between ? and ?) " // periodo de 20/12 a 28/02 dois anos antes
		+ " order by pe.NUMERO_EDICAO desc, plp.NUMERO_PERIODO desc limit 2 ";
    }

    public List<ProdutoEdicaoBase> listaEdicoesPorLancamento(ProdutoEdicaoBase edicao) {
	List<ProdutoEdicaoBase> edicoes = new ArrayList<>();
	try {
	    PreparedStatement ps = Conexao.getConexao().prepareStatement(queryEdicoesLancamentos);
	    ps.setString(1, edicao.getCodigoProduto());
	    ResultSet rs = ps.executeQuery();
	    while (rs.next()) {
		edicoes.add(produtoEdicaoMapper(rs));
	    }
	} catch (ClassNotFoundException | SQLException e) {
	    log.error("Erro os listar edições por lançamento.", e);
	}
	return edicoes;
    }

    public List<ProdutoEdicaoBase> listaEdicoesAnosAnterioresMesmoMes(ProdutoEdicaoBase edicao) {
	return this.listaEdicoesAnosAnteriores(edicao, true, null);
    }

    public List<ProdutoEdicaoBase> listaEdicoesAnosAnterioresVeraneio(ProdutoEdicaoBase edicao, List<LocalDate> periodoVeraneio) {
	return this.listaEdicoesAnosAnteriores(edicao, false, periodoVeraneio);
    }

    private List<ProdutoEdicaoBase> listaEdicoesAnosAnteriores(ProdutoEdicaoBase edicao, boolean mesmoMes, List<LocalDate> dataReferencias) {
	List<ProdutoEdicaoBase> edicoes = new ArrayList<ProdutoEdicaoBase>();
	try {
	    PreparedStatement ps = Conexao.getConexao().prepareStatement(
		    mesmoMes ? queryLancamentosAnosAnterioresMesmoMes : queryLancamentosAnosAnterioresVeraneio);
	    int index = 0;
	    ps.setString(++index, edicao.getCodigoProduto());
	    if (mesmoMes) {
		ps.setString(++index, anoMesAnteriorSQL(edicao.getDataLancamento(), Years.ONE));
		ps.setString(++index, anoMesAnteriorSQL(edicao.getDataLancamento(), Years.TWO));
	    } else {
		for (LocalDate localDate : dataReferencias) {
		    ps.setString(++index, localDate.toString());
		}
		// FIXME
		// ps.setString(++index, periodoVeraneio(edicao.getDataLancamento(), 2, DataReferencia.DEZEMBRO_20));
		// ps.setString(++index, periodoVeraneio(edicao.getDataLancamento(), 1, DataReferencia.FEVEREIRO_15));
		// ps.setString(++index, periodoVeraneio(edicao.getDataLancamento(), 4, DataReferencia.DEZEMBRO_20));
		// ps.setString(++index, periodoVeraneio(edicao.getDataLancamento(), 3, DataReferencia.FEVEREIRO_15));
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

    private boolean traduzColecionavel(String grupoProduto) {
	if (grupoProduto != null && grupoProduto.equalsIgnoreCase(PRODUTO_COLECIONAVEL)) {
	    return true;
	}
	return false;
    }

    private boolean traduzStatus(String status) {
	if (status != null && !status.equalsIgnoreCase(STATUS_FECHADO)) {
	    return true;
	}
	return false;
    }

    private String anoMesAnteriorSQL(Date dataLancamento, Years anosSubtrair) {
	YearMonth ym = new YearMonth(dataLancamento);
	return ym.minus(anosSubtrair).toString().concat("-%");
    }

    @SuppressWarnings("unused")
    private String periodoVeraneio(Date dataLancamento, int anosSubtrair, DataReferencia dataReferencia) {
	return MonthDay.parse(dataReferencia.getData()).toLocalDate(LocalDate.fromDateFields(dataLancamento).minusYears(anosSubtrair).getYear())
		.toString();
    }

    private ProdutoEdicaoBase produtoEdicaoMapper(ResultSet rs) throws SQLException {

	ProdutoEdicaoBase produtoEdicao = new ProdutoEdicaoBase();

	produtoEdicao.setId(rs.getLong("PRODUTO_EDICAO_ID"));
	produtoEdicao.setIdLancamento(rs.getLong("ID"));
	produtoEdicao.setEdicaoAberta(traduzStatus(rs.getNString("STATUS")));
	produtoEdicao.setDataLancamento(rs.getDate("DATA_LCTO_DISTRIBUIDOR"));
	produtoEdicao.setColecao(traduzColecionavel(rs.getNString("GRUPO_PRODUTO")));
	produtoEdicao.setParcial(rs.getString("TIPO_LANCAMENTO").equalsIgnoreCase(LANCAMENTO_PARCIAL));
	produtoEdicao.setNumeroEdicao(rs.getLong("NUMERO_EDICAO"));
	produtoEdicao.setCodigoProduto(rs.getString("CODIGO"));

	return produtoEdicao;
    }
}
