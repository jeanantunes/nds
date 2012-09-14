package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.repository.VisaoEstoqueRepository;

@Repository
public class VisaoEstoqueRepositoryImpl implements VisaoEstoqueRepository{

	@Override
	public VisaoEstoqueDTO obterLancamento() {
		VisaoEstoqueDTO lancamento = new VisaoEstoqueDTO();
		lancamento.setEstoque("Lançamento");
		return lancamento;
	}

	@Override
	public VisaoEstoqueDTO obterLancamentoJuramentado() {
		VisaoEstoqueDTO lancamentoJuramentado = new VisaoEstoqueDTO();
		lancamentoJuramentado.setEstoque("Lançamento Juramentado");
		return lancamentoJuramentado;
	}

	@Override
	public VisaoEstoqueDTO obterSuplementar() {
		VisaoEstoqueDTO suplementar = new VisaoEstoqueDTO();
		suplementar.setEstoque("Suplementar");
		return suplementar;
	}

	@Override
	public VisaoEstoqueDTO obterRecolhimento() {
		VisaoEstoqueDTO recolhimento = new VisaoEstoqueDTO();
		recolhimento.setEstoque("Recolhimento");
		return recolhimento;
	}

	@Override
	public VisaoEstoqueDTO obterProdutosDanificados() {
		VisaoEstoqueDTO produtosDanificados = new VisaoEstoqueDTO();
		produtosDanificados.setEstoque("Produtos Danificados");
		return produtosDanificados;
	}
	
}
