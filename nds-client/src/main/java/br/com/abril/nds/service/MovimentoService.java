package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;

public interface MovimentoService {

	void gerarMovimentoEstoqueDeExpedicao(Date dataLancamento, Long idProdutoEdicao, Long idUsuario);
	
	void gerarMovimentoEstoque(Date dataLancamento, Long idProdutoEdicao,Long idUsuario,BigDecimal quantidade);
	
	void gerarMovimentoCota(Date dataLancamento, Long idProdutoEdicao, Long idCota, Long idUsuario, BigDecimal quantidade);
}
