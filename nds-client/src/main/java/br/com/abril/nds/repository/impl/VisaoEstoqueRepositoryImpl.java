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
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ")
		   .append(" COUNT(EstoqueProduto.qtde) as produtos, ")
		   .append(" SUM(EstoqueProduto.qtde) as exemplares, ")
		   .append(" SUM(ProdutoEdicao.precoVenda * EstoqueProduto.qtde) as valor ")
		   .append(" FROM Produto p ")
		   .append(" INNER JOIN Produto.fornecedores fornecedor")
		   .append(" INNER JOIN ProdutoEdicao ")
		   .append(" ON p.id = ProdutoEdicao.produto.id")
		   .append(" INNER JOIN EstoqueProduto ")
		   .append(" ON ProdutoEdicao.id = EstoqueProduto.produtoEdicao.id")
		   .append(" INNER JOIN MovimentoEstoque ")
		   .append(" ON MovimentoEstoque.produtoEdicao.id = EstoqueProduto.produtoEdicao.id ")
		   .append(" AND MovimentoEstoque.estoqueProduto.id = EstoqueProduto.id ")
		   .append(" WHERE fornecedor.id = :idFornecedor ")
		   .append(" AND MovimentoEstoque.dataCriacao = :dataMovimentacao ");
	
		Query query = getSession().createQuery(hql.toString());
		
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
