package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;

public interface ResumoFecharDiaService {
	
	BigDecimal obterValorReparte(Date dataOperacaoDistribuidor);

}
