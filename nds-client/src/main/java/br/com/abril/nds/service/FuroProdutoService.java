package br.com.abril.nds.service;

import java.util.Date;

public interface FuroProdutoService {

	void validarFuroProduto(String codigoProduto, Long idProdutoEdicao, Long idLancamento, Date novaData, Long idUsuario);
	
	void efetuarFuroProduto(String codigoProduto, Long idProdutoEdicao, Long idLancamento, Date novaData, Long idUsuario);
}
