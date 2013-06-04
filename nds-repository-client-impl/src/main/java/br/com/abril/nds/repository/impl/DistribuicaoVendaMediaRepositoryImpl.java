package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ProdutoEdicaoVendaMediaDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DistribuicaoVendaMediaRepository;

@Repository
public class DistribuicaoVendaMediaRepositoryImpl extends AbstractRepositoryModel<ProdutoEdicao, Long> implements DistribuicaoVendaMediaRepository {

    public DistribuicaoVendaMediaRepositoryImpl() {
	super(ProdutoEdicao.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoEdicaoVendaMediaDTO> pesquisar(String codigoProduto, String nomeProduto, Long edicao) {
    	
    	return pesquisar(codigoProduto, nomeProduto, edicao, null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProdutoEdicaoVendaMediaDTO> pesquisarEdicoesParciais(String codigoProduto, Integer periodo, Long edicao) {
    	
	StringBuilder sql = new StringBuilder();
	sql.append("select t.id, ");
	sql.append("       t.numero_edicao numeroEdicao, ");
	sql.append("       t.codigo codigoProduto, ");
	sql.append("       t.nome nome, ");
	sql.append("       t.numero_periodo periodo, ");
	sql.append("       t.parcial parcial, ");
	sql.append("       t.data_lcto_distribuidor dataLancamento, ");
	sql.append("       round(sum(t.reparte) - sum(t.encalhe)) / sum(t.reparte) * 100 percentualVenda, ");
	sql.append("       t.reparte, ");
	sql.append("       t.reparte - t.encalhe venda, ");
	sql.append("       t.status, ");
	sql.append("       t.classificacao ");
	sql.append("  from (select pe.id, ");
	sql.append("               pe.numero_edicao, ");
	sql.append("               p.codigo, ");
	sql.append("               p.nome, ");
	sql.append("               plp.numero_periodo, ");
	sql.append("               (case when plp.id is null then 0 else 1 end) parcial, ");
	sql.append("               l.data_lcto_distribuidor, ");
	sql.append("               sum(case when tm.grupo_movimento_estoque = 'ENVIO_JORNALEIRO' ");
	sql.append("                   then mec.qtde else 0 end) reparte, ");
	sql.append("               sum(case when tm.grupo_movimento_estoque = 'ENVIO_ENCALHE' ");
	sql.append("                   then mec.qtde else 0 end) encalhe, ");
	sql.append("               l.status status, ");
	sql.append("               tcp.descricao classificacao ");
	sql.append("          from movimento_estoque_cota mec ");
	sql.append("          join tipo_movimento tm ON tm.id = mec.tipo_movimento_id ");
	sql.append("          join lancamento l ON l.id = mec.lancamento_id ");
	sql.append("          join periodo_lancamento_parcial plp ON plp.lancamento_id = l.id ");
	sql.append("          join produto_edicao pe on pe.id = mec.produto_edicao_id ");
	sql.append("          join produto p on p.id = pe.produto_id ");
	sql.append("          join tipo_classificacao_produto tcp on tcp.id = p.tipo_classificacao_produto_id ");
	sql.append("         where 1 = 1 ");
	
	if (edicao != null) {
	    sql.append("   and pe.numero_edicao = :numero_edicao ");
	}
	if (codigoProduto != null) {
	    sql.append("   and p.codigo = :codigo_produto ");
	}
	if (periodo != null) {
	    sql.append("   and plp.numero_periodo = :periodo ");
	}
	sql.append("         group by pe.numero_edicao, plp.numero_periodo) t ");
	
	Query query = getSession().createSQLQuery(sql.toString());
	if (edicao != null) {
	    query.setLong("numero_edicao", edicao);
	}
	if (codigoProduto != null) {
	    query.setString("codigo_produto", codigoProduto);
	}
	if (periodo != null) {
	    query.setInteger("periodo", periodo);
	}
	
	query.setResultTransformer(Transformers.aliasToBean(ProdutoEdicaoVendaMediaDTO.class));
	return query.list();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProdutoEdicaoVendaMediaDTO> pesquisar(String codigoProduto, String nomeProduto, Long edicao, Long classificacao) {

	StringBuilder sql = new StringBuilder();
	sql.append("select pe.id, ");
	sql.append("       pe.numero_edicao numeroEdicao, ");
	sql.append("       p.codigo codigoProduto, ");
	sql.append("       p.nome nome, ");
	sql.append("       plp.numero_periodo periodo, ");
	sql.append("       (case when plp.id is null then 0 else 1 end) parcial, ");
	sql.append("       l.data_lcto_distribuidor dataLancamento, ");
	sql.append("       round(sum(epc.qtde_recebida), 0) reparte, ");
	sql.append("       round((sum(epc.qtde_recebida) - sum(epc.qtde_devolvida)), 0) venda, ");
	sql.append("       round(sum(epc.qtde_recebida) - sum(epc.qtde_devolvida)) / sum(epc.qtde_recebida) * 100 percentualVenda, ");
	sql.append("       l.status status, ");
	sql.append("       tcp.descricao classificacao ");
	sql.append("  from lancamento l ");
	sql.append("  join produto_edicao pe on pe.id = l.produto_edicao_id ");
	sql.append("  left join periodo_lancamento_parcial plp on plp.lancamento_id = l.id ");
	sql.append("  join produto p on p.id = pe.produto_id ");
	sql.append("  left join estoque_produto_cota epc on epc.produto_edicao_id = pe.id ");
	sql.append("  join tipo_classificacao_produto tcp on tcp.id = p.tipo_classificacao_produto_id ");
	sql.append(" where 1 = 1 ");
	
	if (edicao != null) {
	    sql.append("   and pe.numero_edicao = :numero_edicao ");
	}
	if (codigoProduto != null) {
	    sql.append("   and p.codigo = :codigo_produto ");
	}
	if (nomeProduto != null) {
	    sql.append("   and p.nome = :nome_produto ");
	}
	if (classificacao != null) {
		sql.append("   and tcp.id = :classificacao ");
	}
	
	sql.append(" group by pe.numero_edicao, pe.id, plp.numero_periodo ");
	sql.append(" order by pe.numero_edicao desc ");
	
	Query query = getSession().createSQLQuery(sql.toString());
	if (edicao != null) {
	    query.setLong("numero_edicao", edicao);
	}
	if (codigoProduto != null) {
	    query.setString("codigo_produto", codigoProduto);
	}
	if (nomeProduto != null) {
	    query.setString("nome_produto", nomeProduto);
	}
	if (classificacao != null) {
	    query.setLong("classificacao", classificacao);
	}
	
	query.setResultTransformer(Transformers.aliasToBean(ProdutoEdicaoVendaMediaDTO.class));
	return query.list();
    }
}
