package br.com.abril.nds.repository;

import java.util.Date;

public interface FecharDiaRepository {
	
	boolean existeCobrancaParaFecharDia(Date diaDeOperaoMenosUm);

	boolean existeNotaFiscalSemRecebimentoFisico();

}
