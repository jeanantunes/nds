package br.com.abril.nds.repository;

import java.util.Date;

import br.com.abril.nds.model.movimentacao.ControleContagemDevolucao;

public interface ControleContagemDevolucaoRepository  extends Repository<ControleContagemDevolucao,Long> { 

	public ControleContagemDevolucao obterControleContagemDevolucao(Date dataOperacao, Long idProdutoEdicao);
	
}
