package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;

public interface ResumoFecharDiaRepository {
	
	BigDecimal obterValorReparte(Date dataOperacaoDistribuidor);

}
