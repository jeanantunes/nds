package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;



public interface ResumoSuplementarFecharDiaService {

	BigDecimal obterValorEstoqueLogico(Date dataOperacao);

	BigDecimal obterValorTransferencia(Date dataOperacao);

	BigDecimal obterValorVenda(Date dataOperacao);

}
