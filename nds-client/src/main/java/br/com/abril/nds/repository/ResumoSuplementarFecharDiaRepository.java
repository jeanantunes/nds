package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;


public interface ResumoSuplementarFecharDiaRepository {

	BigDecimal obterValorEstoqueLogico(Date dataOperacao);

}
