package br.com.abril.nds.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.model.ProdutoEdicaoBase;

public class CotaDAO {
    
    public Cota getCotaEquivalenteByCota(Cota cota) {

	List<Cota> listEquivalente = new ArrayList<Cota>();

	try {

	    StringBuilder query = new StringBuilder(" SELECT CB.INDICE_AJUSTE, C.* FROM COTA_BASE_COTA CBC ");
	    query.append(" INNER JOIN COTA_BASE CB ON (CB.ID = CBC.COTA_BASE_ID) ");
	    query.append(" INNER JOIN COTA C ON (C.ID = CBC.COTA_ID) ");
	    query.append(" INNER JOIN PESSOA P ON (P.ID = C.PESSOA_ID) ");
	    query.append(" WHERE CB.COTA_ID = ? ");
	    query.append(" AND CB.DATA_INICIO >= ? AND CB.DATA_FIM <= ? ");

	    PreparedStatement psmt = Conexao.getConexao().prepareStatement(query.toString());
	    psmt.setLong(1, cota.getId());
	    psmt.setString(2, new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
	    psmt.setString(3, new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));

	    ResultSet rs = psmt.executeQuery();
	    while (rs.next()) {
		
		if(rs.isFirst()) {
		    cota.setIndiceAjusteEquivalente(rs.getBigDecimal("INDICE_AJUSTE"));
		}
		
		Cota cotaEquivalente = new Cota();
		cotaEquivalente.setId(rs.getLong("ID"));
		listEquivalente.add(cotaEquivalente);
	    }

	    cota.setEquivalente(listEquivalente);
	    
	} catch (Exception ex) {
	    ex.printStackTrace();
	}

	return cota;
    }

    public List<Cota> getCotas(int limit) {
	List<Cota> cotas = new ArrayList<Cota>();
	try {
	    StringBuffer sb = new StringBuffer("SELECT C.*, P.NOME FROM COTA C INNER JOIN PESSOA P ON (P.ID = C.PESSOA_ID) ORDER BY C.ID");
	    if (limit > 0) {
		sb.append(" limit ");
		sb.append(limit);
	    }
	    PreparedStatement psmt = Conexao.getConexao().prepareStatement(sb.toString());
	    ResultSet rs = psmt.executeQuery();
	    while (rs.next()) {
		Cota cota = new Cota();
		cota.setId(rs.getLong("ID"));
		// cota.setNumero(rs.getLong("NUMERO_COTA"));
		// cota.setNomePessoa(rs.getString("NOME"));

		cotas.add(cota);
	    }
	} catch (Exception ex) {
	    System.out.println("Ocorreu um erro ao tentar consultar as cotas");
	    ex.printStackTrace();
	}
	return cotas;
    }

    public List<Cota> getCotas() {
	return this.getCotas(-1);
    }

    public List<Cota> getCotaWithEstoqueProdutoCota() {
	List<Cota> cotas = new ArrayList<Cota>();
	try {
	    StringBuilder query = new StringBuilder("SELECT C.*, P.NOME FROM COTA C ");
	    query.append(" INNER JOIN PESSOA P ON (P.ID = C.PESSOA_ID) ");
	    query.append(" WHERE EXISTS (SELECT EPC.* FROM ESTOQUE_PRODUTO_COTA EPC WHERE EPC.COTA_ID = C.ID) ");
	    query.append(" ORDER BY C.ID ");

	    PreparedStatement psmt = Conexao.getConexao().prepareStatement(query.toString());
	    ResultSet rs = psmt.executeQuery();
	    while (rs.next()) {
		Cota cota = new Cota();
		cota.setId(rs.getLong("ID"));
		// cota.setNumero(rs.getLong("NUMERO_COTA"));
		// cota.setNomePessoa(rs.getString("NOME"));

		cotas.add(cota);
	    }
	} catch (Exception ex) {
	    System.out.println("Ocorreu um erro ao tentar consultar as cotas");
	    ex.printStackTrace();
	}
	return cotas;
    }

    public Cota getAjustesReparteCota(Cota cota) {
	try {
	    PreparedStatement psmt = Conexao.getConexao().prepareStatement("SELECT AJUSTE_APLICADO, FORMA_AJUSTE FROM AJUSTE_REPARTE WHERE COTA_ID = ?");
	    psmt.setLong(1, cota.getId());
	    ResultSet rs = psmt.executeQuery();
	    while (rs.next()) {
		if (rs.getString("FORMA_AJUSTE").equals("vendaMedia")) {
		    cota.setVendaMediaMaisN(rs.getBigDecimal("AJUSTE_APLICADO"));
		} else if (rs.getString("FORMA_AJUSTE").equals("encalheMaximo")) {
		    cota.setPercentualEncalheMaximo(rs.getBigDecimal("AJUSTE_APLICADO"));
		}
		// TODO: ainda faltam carregar os parâmetros para ajuste de segmento e histórico (estarão gravados no banco respectivamente: 'segmento' e 'histórico')
	    }
	} catch (Exception ex) {
	    System.out.println("Ocorreu um erro ao consultar os parâmetros das cotas");
	    ex.printStackTrace();
	}
	return cota;
    }

    public List<Cota> getCotasComEdicoesBase(List<ProdutoEdicaoBase> edicoesBase) {
	List<Cota> returnListCota = new ArrayList<>();
	try {
	    Map<Long, Integer> idsPesos = new HashMap<Long, Integer>();
	    for (ProdutoEdicaoBase edicao : edicoesBase) {
		idsPesos.put(edicao.getId(), edicao.getPeso());
	    }
	    String idsString = idsPesos.keySet().toString().replaceAll("\\]|\\[", "");
	    PreparedStatement ps = Conexao.getConexao().prepareStatement(SQL_PRODUTO_EDICAO_POR_COTA.replaceAll("\\?", idsString));
	    ResultSet rs = ps.executeQuery();

	    long prevIdCota = 0;
	    while (rs.next()) {
		long idCota = rs.getLong("COTA_ID");

		if (prevIdCota != idCota) {
		    Cota cota = new Cota();
		    cota.setId(idCota);
		    cota.setNumero(rs.getLong("NUMERO_COTA"));
		    cota.setEdicoesRecebidas(getEdicoes(rs, idsPesos, false));
		    returnListCota.add(cota);
		} else {
		    Cota cota = returnListCota.get(returnListCota.size() - 1);
		    cota.getEdicoesRecebidas().addAll(getEdicoes(rs, idsPesos, true));
		}
		prevIdCota = idCota;
	    }

	} catch (ClassNotFoundException | SQLException e) {
	    e.printStackTrace();
	}
	return returnListCota;
    }

    private List<ProdutoEdicao> getEdicoes(ResultSet rs, Map<Long, Integer> idsPesos, boolean forceEdicaoFechada) throws SQLException {
	List<ProdutoEdicao> edicoes = new ArrayList<ProdutoEdicao>();
	ProdutoEdicao produtoEdicao = new ProdutoEdicao();

	produtoEdicao.setIdProduto(rs.getLong("PRODUTO_ID"));
	produtoEdicao.setId(rs.getLong("PRODUTO_EDICAO_ID"));
	produtoEdicao.setIdLancamento(rs.getLong("LANCAMENTO_ID"));

	// FIXME - gambeta loca remover!!!!!!!
	if (forceEdicaoFechada) {
	    produtoEdicao.setEdicaoAberta(false);
	} else {
	    produtoEdicao.setEdicaoAberta(traduzStatus(rs.getNString("STATUS")));
	}

	produtoEdicao.setParcial(rs.getString("TIPO_LANCAMENTO").equalsIgnoreCase(LANCAMENTO_PARCIAL));
	produtoEdicao.setColecao(traduzColecionavel(rs.getNString("GRUPO_PRODUTO")));
	produtoEdicao.setDataLancamento(rs.getDate("DATA_LCTO_DISTRIBUIDOR"));
	produtoEdicao.setReparte(rs.getBigDecimal("QTDE_RECEBIDA"));
	produtoEdicao.setVenda(rs.getBigDecimal("QTDE_VENDA"));

	produtoEdicao.setPeso(idsPesos.get(produtoEdicao.getId()));

	produtoEdicao.setNumeroEdicao(rs.getLong("NUMERO_EDICAO"));
	produtoEdicao.setCodigoProduto(rs.getLong("CODIGO"));

	edicoes.add(produtoEdicao);
	return edicoes;
    }

    private boolean traduzStatus(String status) {
	if (status != null && !status.equalsIgnoreCase(STATUS_FECHADO)) {
	    return true;
	}
	return false;
    }

    private boolean traduzColecionavel(String grupoProduto) {
	if (grupoProduto != null && grupoProduto.equalsIgnoreCase(PRODUTO_COLECIONAVEL)) {
	    return true;
	}
	return false;
    }

    private static final String LANCAMENTO_PARCIAL = "PARCIAL";
    private static final String PRODUTO_COLECIONAVEL = "COLECIONAVEL";
    private static final String STATUS_FECHADO = "FECHADO";

    private static final String SQL_PRODUTO_EDICAO_POR_COTA = " select "
	    + " c.id as COTA_ID "
	    + " ,c.NUMERO_COTA "
	    + " ,p.id as PRODUTO_ID "
	    + " ,pe.id as PRODUTO_EDICAO_ID "
	    + " ,l.id as LANCAMENTO_ID "
	    + " ,l.STATUS " // -- edicao aberta
	    + " ,l.TIPO_LANCAMENTO " // -- se parcial
	    + " ,tp.GRUPO_PRODUTO " // -- se colecionavel
	    + " ,l.DATA_LCTO_DISTRIBUIDOR " + " ,epc.QTDE_RECEBIDA " + " ,(epc.QTDE_RECEBIDA - epc.QTDE_DEVOLVIDA) as QTDE_VENDA " + " ,pe.PACOTE_PADRAO "
	    + " ,pe.NUMERO_EDICAO " + " ,p.CODIGO " + " from " + " produto_edicao pe " + " ,produto p " + " ,lancamento l " + " ,tipo_produto tp " + " ,cota c "
	    + " ,estoque_produto_cota epc " + " where " + " pe.id in (?) " + " and pe.PRODUTO_ID = p.id " + " and pe.ID = l.PRODUTO_EDICAO_ID "
	    + " and tp.ID = p.TIPO_PRODUTO_ID " + " and pe.ID = epc.PRODUTO_EDICAO_ID " + " and c.ID = epc.COTA_ID " + " order by c.ID, pe.NUMERO_EDICAO desc ";
}
