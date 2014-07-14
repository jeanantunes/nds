package br.com.abril.nds.service;

import java.math.BigInteger;

public interface TransferenciaEstoqueParcialService {

	BigInteger buscarQuantidadeParaTransferencia(String codigoProduto, Long numeroEdicao);
	
	void transferir(String codigoProduto, Long numeroEdicaoOrigem, Long numeroEdicaoDestino, Long idUsuario);
	
}
