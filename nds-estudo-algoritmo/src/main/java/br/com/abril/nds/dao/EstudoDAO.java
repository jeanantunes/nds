package br.com.abril.nds.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.Cota;
import br.com.abril.nds.model.Estudo;
import br.com.abril.nds.model.ProdutoEdicao;
import br.com.abril.nds.model.ProdutoEdicaoBase;

@Repository
public class EstudoDAO {

    @Autowired
    private DataSource dataSource;
    
    @Value("#{query_estudo.insertEstudo}")
    private String insertEstudo;

    @Value("#{query_estudo.insertEstudoCotas}")
    private String insertEstudoCotas;
    
    @Value("#{query_estudo.insertProdutoEdicao}")
    private String insertProdutoEdicao;
    
    @Value("#{query_estudo.insertProdutoEdicaoBase}")
    private String insertProdutoEdicaoBase;

    public void gravarEstudo(Estudo estudo) {
	List<Estudo> estudos = new ArrayList<Estudo>();
	estudos.add(estudo);
	Long estudoId = null;
	try {
	    KeyHolder keyHolder = new GeneratedKeyHolder();
	    SqlParameterSource paramSource = new BeanPropertySqlParameterSource(estudo);
	    new NamedParameterJdbcTemplate(dataSource).update(insertEstudo, paramSource, keyHolder);
	    estudoId = keyHolder.getKey().longValue();
	    System.out.println(estudoId);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	if (estudoId != null) {
	    List<ProdutoEdicao> listaProdutoEdicao = new ArrayList<>();
	    for (Cota cota : estudo.getCotas()) {
		cota.setIdEstudo(estudoId);
		for (ProdutoEdicao produto : cota.getEdicoesRecebidas()) {
		    produto.setIdEstudo(estudoId);
		    produto.setIdCota(cota.getId());
		    listaProdutoEdicao.add(produto);
		}
	    }
	    gravarCotas(estudo.getCotas());
	    
	    for (ProdutoEdicaoBase prod : estudo.getEdicoesBase()) {
		prod.setIdEstudo(estudoId);
	    }
	    gravarProdutoEdicaoBase(estudo.getEdicoesBase());
	    gravarProdutoEdicao(listaProdutoEdicao);
	}
    }

    public void gravarCotas(final List<Cota> cotas) {
	SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(cotas.toArray());
	new NamedParameterJdbcTemplate(dataSource).batchUpdate(insertEstudoCotas, batch);
    }

    public void gravarProdutoEdicao(List<ProdutoEdicao> produtosEdicao) {
	SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(produtosEdicao.toArray());
	new NamedParameterJdbcTemplate(dataSource).batchUpdate(insertProdutoEdicao, batch);
    }

    public void gravarProdutoEdicaoBase(List<ProdutoEdicaoBase> produtosEdicaoBase) {
	SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(produtosEdicaoBase.toArray());
	new NamedParameterJdbcTemplate(dataSource).batchUpdate(insertProdutoEdicaoBase, batch);
    }

    public void carregarParametros(Estudo estudo) {
	// TODO: carregar edições base do estudo
	//	PreparedStatement psmt = Conexao.getConexao().prepareStatement(SQL_CONSULTA_EDICOES_BASE);
	//	psmt.setBigDecimal(1, estudo.getId());

	// TODO: carregar parâmetros do estudo
	//	PreparedStatement psmt = Conexao.getConexao().prepareStatement(SQL_CONSULTA_EDICOES_BASE);
	//	psmt.setBigDecimal(1, estudo.getId());
    }

    //    private final static String SQL_CONSULTA_PARAMETROS = ""
    //	    + "select distribuicao_por_multiplos "
    //	    + "  from tela_parametros "
    //	    + " where estudo = ? ";
    //    private final static String SQL_CONSULTA_EDICOES_BASE = ""
    //	    + "select * "
    //	    + "  from produto_edicao_base "
    //	    + " where estudo = ? ";
}
