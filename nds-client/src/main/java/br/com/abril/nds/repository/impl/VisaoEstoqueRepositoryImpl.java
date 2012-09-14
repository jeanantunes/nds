package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;
import br.com.abril.nds.repository.VisaoEstoqueRepository;

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
		
		query.setResultTransformer(Transformers.aliasToBean(VisaoEstoqueDTO.class));
		
		lancamento = (VisaoEstoqueDTO) query.uniqueResult();
		
		lancamento.setEstoque("Lançamento");
		return lancamento;
	}

	@Override
	public VisaoEstoqueDTO obterLancamentoJuramentado(FiltroConsultaVisaoEstoque filtro) {
		VisaoEstoqueDTO lancamentoJuramentado = new VisaoEstoqueDTO();
		lancamentoJuramentado.setEstoque("Lançamento Juramentado");
		return lancamentoJuramentado;
	}

	@Override
	public VisaoEstoqueDTO obterSuplementar(FiltroConsultaVisaoEstoque filtro) {
		VisaoEstoqueDTO suplementar = new VisaoEstoqueDTO();
		suplementar.setEstoque("Suplementar");
		
		return suplementar;
	}

	@Override
	public VisaoEstoqueDTO obterRecolhimento(FiltroConsultaVisaoEstoque filtro) {
		VisaoEstoqueDTO recolhimento = new VisaoEstoqueDTO();
		recolhimento.setEstoque("Recolhimento");
		return recolhimento;
	}

	@Override
	public VisaoEstoqueDTO obterProdutosDanificados(FiltroConsultaVisaoEstoque filtro) {
		VisaoEstoqueDTO produtosDanificados = new VisaoEstoqueDTO();
		produtosDanificados.setEstoque("Produtos Danificados");
		return produtosDanificados;
	}
	
}
