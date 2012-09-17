package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;
import br.com.abril.nds.repository.VisaoEstoqueRepository;
import br.com.abril.nds.util.Util;

@Repository
public class VisaoEstoqueRepositoryImpl extends AbstractRepository implements VisaoEstoqueRepository{

	@Override
	public VisaoEstoqueDTO obterLancamento(FiltroConsultaVisaoEstoque filtro) {
		VisaoEstoqueDTO lancamento = new VisaoEstoqueDTO();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ")
		   .append(" COUNT(estoque_produto.qtde) as produtos, ")
		   .append(" SUM(estoque_produto.qtde) as exemplares, ")
		   .append(" SUM(produto_edicao.preco_venda * estoque_produto.qtde) as valor ")
		   .append(" FROM produto_fornecedor ")
		   .append(" INNER JOIN produto_edicao ")
		   .append(" ON produto_fornecedor.produto_id = produto_edicao.produto_id ")
		   .append(" INNER JOIN estoque_produto  ")
		   .append(" ON produto_edicao.id = estoque_produto.produto_edicao_id ")
		   .append(" INNER JOIN movimento_estoque ")
		   .append(" ON movimento_estoque.produto_edicao_id = estoque_produto.produto_edicao_id ")
		   .append(" AND movimento_estoque.estoque_produto_id = estoque_produto.id ")
		   .append(" WHERE produto_fornecedor.fornecedores_id = :idFornecedor ")
		   .append(" AND movimento_estoque.data_criacao = :dataMovimentacao ");

	
		Query query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("idFornecedor", filtro.getIdFornecedor());
		query.setParameter("dataMovimentacao", filtro.getDataMovimentacao());
		
		Object[] result = (Object[]) query.uniqueResult();
		
		lancamento.setEstoque("Lançamento");
		lancamento.setProdutos(Util.nvl((BigInteger)result[0], BigInteger.ZERO).longValue());
		lancamento.setExemplares(Util.nvl((BigDecimal) result[1], BigDecimal.ZERO));
		lancamento.setValor(Util.nvl((BigDecimal)result[2], BigDecimal.ZERO));
		
		return lancamento;
	}

	@Override
	public VisaoEstoqueDTO obterLancamentoJuramentado(FiltroConsultaVisaoEstoque filtro) {
		VisaoEstoqueDTO lancamentoJuramentado = new VisaoEstoqueDTO();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ")
		   .append(" COUNT(estoque_produto.qtde_juramentado) as produtos, ")
		   .append(" SUM(estoque_produto.qtde_juramentado) as exemplares, ")
		   .append(" SUM(produto_edicao.preco_venda * estoque_produto.qtde_juramentado) as valor ")
		   .append(" FROM produto_fornecedor ")
		   .append(" INNER JOIN produto_edicao ")
		   .append(" ON produto_fornecedor.produto_id = produto_edicao.produto_id ")
		   .append(" INNER JOIN estoque_produto  ")
		   .append(" ON produto_edicao.id = estoque_produto.produto_edicao_id ")
		   .append(" INNER JOIN movimento_estoque ")
		   .append(" ON movimento_estoque.produto_edicao_id = estoque_produto.produto_edicao_id ")
		   .append(" AND movimento_estoque.estoque_produto_id = estoque_produto.id ")
		   .append(" WHERE produto_fornecedor.fornecedores_id = :idFornecedor ")
		   .append(" AND movimento_estoque.data_criacao = :dataMovimentacao ");
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("idFornecedor", filtro.getIdFornecedor());
		query.setParameter("dataMovimentacao", filtro.getDataMovimentacao());
		
		Object[] result = (Object[]) query.uniqueResult();
		
		lancamentoJuramentado.setEstoque("Lançamento Juramentado");
		lancamentoJuramentado.setProdutos(Util.nvl((BigInteger)result[0], BigInteger.ZERO).longValue());
		lancamentoJuramentado.setExemplares(Util.nvl((BigDecimal) result[1], BigDecimal.ZERO));
		lancamentoJuramentado.setValor(Util.nvl((BigDecimal)result[2], BigDecimal.ZERO));

		return lancamentoJuramentado;
	}

	@Override
	public VisaoEstoqueDTO obterSuplementar(FiltroConsultaVisaoEstoque filtro) {
		VisaoEstoqueDTO suplementar = new VisaoEstoqueDTO();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ")
		   .append(" COUNT(estoque_produto.qtde_suplementar) as produtos, ")
		   .append(" SUM(estoque_produto.qtde_suplementar) as exemplares, ")
		   .append(" SUM(produto_edicao.preco_venda * estoque_produto.qtde_suplementar) as valor ")
		   .append(" FROM produto_fornecedor ")
		   .append(" INNER JOIN produto_edicao ")
		   .append(" ON produto_fornecedor.produto_id = produto_edicao.produto_id ")
		   .append(" INNER JOIN estoque_produto  ")
		   .append(" ON produto_edicao.id = estoque_produto.produto_edicao_id ")
		   .append(" INNER JOIN movimento_estoque ")
		   .append(" ON movimento_estoque.produto_edicao_id = estoque_produto.produto_edicao_id ")
		   .append(" AND movimento_estoque.estoque_produto_id = estoque_produto.id ")
		   .append(" WHERE produto_fornecedor.fornecedores_id = :idFornecedor ")
		   .append(" AND movimento_estoque.data_criacao = :dataMovimentacao ");
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("idFornecedor", filtro.getIdFornecedor());
		query.setParameter("dataMovimentacao", filtro.getDataMovimentacao());
		
		Object[] result = (Object[]) query.uniqueResult();
		
		suplementar.setEstoque("Suplementar");
		suplementar.setProdutos(Util.nvl((BigInteger)result[0], BigInteger.ZERO).longValue());
		suplementar.setExemplares(Util.nvl((BigDecimal) result[1], BigDecimal.ZERO));
		suplementar.setValor(Util.nvl((BigDecimal)result[2], BigDecimal.ZERO));
		
		return suplementar;
	}

	@Override
	public VisaoEstoqueDTO obterRecolhimento(FiltroConsultaVisaoEstoque filtro) {
		VisaoEstoqueDTO recolhimento = new VisaoEstoqueDTO();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ")
		   .append(" COUNT(estoque_produto.qtde_devolucao_encalhe) as produtos, ")
		   .append(" SUM(estoque_produto.qtde_devolucao_encalhe) as exemplares, ")
		   .append(" SUM(produto_edicao.preco_venda * estoque_produto.qtde_devolucao_encalhe) as valor ")
		   .append(" FROM produto_fornecedor ")
		   .append(" INNER JOIN produto_edicao ")
		   .append(" ON produto_fornecedor.produto_id = produto_edicao.produto_id ")
		   .append(" INNER JOIN estoque_produto  ")
		   .append(" ON produto_edicao.id = estoque_produto.produto_edicao_id ")
		   .append(" INNER JOIN movimento_estoque ")
		   .append(" ON movimento_estoque.produto_edicao_id = estoque_produto.produto_edicao_id ")
		   .append(" AND movimento_estoque.estoque_produto_id = estoque_produto.id ")
		   .append(" WHERE produto_fornecedor.fornecedores_id = :idFornecedor ")
		   .append(" AND movimento_estoque.data_criacao = :dataMovimentacao ");
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("idFornecedor", filtro.getIdFornecedor());
		query.setParameter("dataMovimentacao", filtro.getDataMovimentacao());
		
		Object[] result = (Object[]) query.uniqueResult();
		
		recolhimento.setEstoque("Recolhimento");		
		recolhimento.setProdutos(Util.nvl((BigInteger)result[0], BigInteger.ZERO).longValue());
		recolhimento.setExemplares(Util.nvl((BigDecimal) result[1], BigDecimal.ZERO));
		recolhimento.setValor(Util.nvl((BigDecimal)result[2], BigDecimal.ZERO));

		return recolhimento;
	}

	@Override
	public VisaoEstoqueDTO obterProdutosDanificados(FiltroConsultaVisaoEstoque filtro) {
		VisaoEstoqueDTO produtosDanificados = new VisaoEstoqueDTO();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ")
		   .append(" COUNT(estoque_produto.qtde_danificado) as produtos, ")
		   .append(" SUM(estoque_produto.qtde_danificado) as exemplares, ")
		   .append(" SUM(produto_edicao.preco_venda * estoque_produto.qtde_danificado) as valor ")
		   .append(" FROM produto_fornecedor ")
		   .append(" INNER JOIN produto_edicao ")
		   .append(" ON produto_fornecedor.produto_id = produto_edicao.produto_id ")
		   .append(" INNER JOIN estoque_produto  ")
		   .append(" ON produto_edicao.id = estoque_produto.produto_edicao_id ")
		   .append(" INNER JOIN movimento_estoque ")
		   .append(" ON movimento_estoque.produto_edicao_id = estoque_produto.produto_edicao_id ")
		   .append(" AND movimento_estoque.estoque_produto_id = estoque_produto.id ")
		   .append(" WHERE produto_fornecedor.fornecedores_id = :idFornecedor ")
		   .append(" AND movimento_estoque.data_criacao = :dataMovimentacao ");
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("idFornecedor", filtro.getIdFornecedor());
		query.setParameter("dataMovimentacao", filtro.getDataMovimentacao());
		
		Object[] result = (Object[]) query.uniqueResult();
		
		produtosDanificados.setEstoque("Produtos Danificados");		
		produtosDanificados.setProdutos(Util.nvl((BigInteger)result[0], BigInteger.ZERO).longValue());
		produtosDanificados.setExemplares(Util.nvl((BigDecimal) result[1], BigDecimal.ZERO));
		produtosDanificados.setValor(Util.nvl((BigDecimal)result[2], BigDecimal.ZERO));
		
		
		return produtosDanificados;
	}
	
}
