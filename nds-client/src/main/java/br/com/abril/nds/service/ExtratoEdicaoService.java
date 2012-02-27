package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.InfoGeralExtratoEdicaoDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;

public interface ExtratoEdicaoService {
	
	public InfoGeralExtratoEdicaoDTO obterInfoGeralExtratoEdicao(Long numeroEdicao);

	public ProdutoEdicao obterProdutoEdicao(String codigoProduto, Long numeroEdicao);
	
	public Fornecedor obterFornecedorDeProduto(String codigoProduto);
	
}
