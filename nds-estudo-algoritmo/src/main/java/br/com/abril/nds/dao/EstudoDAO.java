package br.com.abril.nds.dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.model.ProdutoEdicaoBase;

@Repository
public class EstudoDAO {

    @Autowired
    private DataSource dataSource;
    
    public void setDataSource(DataSource dataSource) {
	this.dataSource = dataSource;
    }
    
    public void gravarEstudo(Estudo estudo) {
	List<Estudo> estudos = new ArrayList<Estudo>();
	estudos.add(estudo);
	
	SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(estudos.toArray());
	String sql = "INSERT INTO estudo (produto_edicao_id, lancamento_id, liberado, reparte_distribuir, sobra, distribuicao_por_multiplos, "
		+ " pacote_padrao, percentual_proporcao_excedente_pdv, percentual_proporcao_excedente_venda) "
		+ " VALUES (:produto.id, :produto.idLancamento, false, :reparteDistribuirInicial, :reparteDistribuir, :distribuicaoPorMultiplos, "
		+ " :pacotePadrao, :percentualProporcaoExcedentePDV, :percentualProporcaoExcedenteVenda)";
	new NamedParameterJdbcTemplate(dataSource).batchUpdate(sql, batch);
    }
    
    public void gravarCotas(final List<Cota> cotas) {
	SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(cotas.toArray());
	String sql = "INSERT INTO estudo_cota (cota_id, estudo_id, classificacao, reparte, venda_corrigida, venda_media_nominal, "
		+ " reparte_juramentado_a_faturar, quantidade_pdvs, reparte_minimo, reparte_maximo, venda_media_mais_n, indice_correcao_tendencia, "
		+ " indice_venda_crescente, percentual_encalhe_maximo, mix) "
		+ " VALUES (:id, :idEstudo, :classificacao, :reparteCalculado, :vendaCorrigida, :vendaMediaNominal, :reparteJuramentadoAFaturar, "
		+ " :quantidadePDVs, :reparteMinimo, :reparteMaximo, :vendaMediaMaisN, :indiceCorrecaoTendencia, :indiceVendaCrescente, "
		+ " :percentualEncalheMaximo, :mix)";
	new NamedParameterJdbcTemplate(dataSource).batchUpdate(sql, batch);
    }
    
    public void gravarProdutoEdicao(List<ProdutoEdicao> produtosEdicao) {
	SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(produtosEdicao.toArray());
	String sql = "INSERT INTO estudo_produto_edicao (estudo_id, cota_id, produto_edicao_id, reparte, venda, pacote_padrao, reparte_minimo, "
		+ " reparte_maximo, indice_correcao, venda_corrigida) "
		+ " VALUES (:idEstudo, :idCota, :id, :reparte, :venda, :pacotePadrao, :reparteMinimo, :reparteMaximo, "
		+ " :indiceCorrecao, :vendaCorrigida)";
	new NamedParameterJdbcTemplate(dataSource).batchUpdate(sql, batch);
    }
    
    public void gravarProdutoEdicaoBase(List<ProdutoEdicaoBase> produtosEdicaoBase) {
	SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(produtosEdicaoBase.toArray());
	String sql = "INSERT INTO estudo_produto_edicao_base (estudo_id, produto_edicao_id, numero_edicao, codigo_produto, "
		+ " lancamento_id, colecao, parcial, edicao_aberta, peso) "
		+ " VALUES (:idEstudo, :id, :numero_edicao, :idProduto, :idLancamento, :colecao, :parcial, :edicaoAberta, :peso) ";
	new NamedParameterJdbcTemplate(dataSource).batchUpdate(sql, batch);
    }
    
    public void carregarParametros(Estudo estudo) {
//	PreparedStatement psmt = Conexao.getConexao().prepareStatement(SQL_CONSULTA_EDICOES_BASE);
//	psmt.setBigDecimal(1, estudo.getId());
    }
    
    private final static String SQL_CONSULTA_EDICOES_BASE = ""
	    + "select * "
	    + "  from produto_edicao_base "
	    + " where estudo = ? ";
}
