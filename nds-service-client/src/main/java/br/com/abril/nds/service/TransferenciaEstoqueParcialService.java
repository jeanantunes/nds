package br.com.abril.nds.service;

import java.math.BigInteger;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

public interface TransferenciaEstoqueParcialService {

	BigInteger buscarQuantidadeParaTransferencia(String codigoProduto, Long numeroEdicao);
	
	void transferir(String codigoProduto, Long numeroEdicaoOrigem, Long numeroEdicaoDestino, Long idUsuario);
	
	void temLancamentoOrigemBalanceado(String produto , Long edicao);
	
}
