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

	StringBuilder sql = new StringBuilder();
	sql.append("select pe.id, ");
	sql.append("       pe.numero_edicao numeroEdicao, ");
	sql.append("       p.codigo codigoProduto, ");
	sql.append("       p.nome nome, ");
	sql.append("       plp.numero_periodo periodo, ");
	sql.append("       l.data_lcto_distribuidor dataLancamento, ");
	sql.append("       round(sum(epc.qtde_recebida), 0) reparte, ");
	sql.append("       round((sum(epc.qtde_recebida) - sum(epc.qtde_devolvida)), 0) venda, ");
	sql.append("       round((sum(epc.qtde_recebida) - sum(epc.qtde_devolvida)) / sum(epc.qtde_recebida), 1) percentualVenda, ");
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
	sql.append(" group by pe.numero_edicao, pe.id ");
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
	query.setResultTransformer(Transformers.aliasToBean(ProdutoEdicaoVendaMediaDTO.class));
	return query.list();
    }
}
