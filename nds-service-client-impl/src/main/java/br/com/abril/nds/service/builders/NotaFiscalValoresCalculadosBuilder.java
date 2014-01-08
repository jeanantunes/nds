package br.com.abril.nds.service.builders;

import java.math.BigDecimal;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.fiscal.nfe.NotaFiscalNds;

public class NotaFiscalValoresCalculadosBuilder {
	
	public static NotaFiscalNds montarValoresCalculados(NotaFiscalNds notaFiscal, Cota cota){
		
		notaFiscal.getNotaFiscalValoresCalculados().setISSQNBase(new BigDecimal("10"));
		notaFiscal.getNotaFiscalValoresCalculados().setISSQNTotal(new BigDecimal("10"));
		notaFiscal.getNotaFiscalValoresCalculados().setISSQNValor(new BigDecimal("10"));
		notaFiscal.getNotaFiscalValoresCalculados().setValorBaseICMS(new BigDecimal("10"));
		notaFiscal.getNotaFiscalValoresCalculados().setValorBaseICMSSubstituto(new BigDecimal("10"));
		notaFiscal.getNotaFiscalValoresCalculados().setValorDesconto(new BigDecimal("10"));
		notaFiscal.getNotaFiscalValoresCalculados().setValorFrete(new BigDecimal("10"));
		notaFiscal.getNotaFiscalValoresCalculados().setValorICMS(new BigDecimal("10"));
		notaFiscal.getNotaFiscalValoresCalculados().setValorICMSSubstituto(new BigDecimal("10"));
		notaFiscal.getNotaFiscalValoresCalculados().setValorIPI(new BigDecimal("10"));
		notaFiscal.getNotaFiscalValoresCalculados().setValorNF(new BigDecimal("10"));
		notaFiscal.getNotaFiscalValoresCalculados().setValorOutro(new BigDecimal("10"));
		notaFiscal.getNotaFiscalValoresCalculados().setValorProdutos(new BigDecimal("10"));
		notaFiscal.getNotaFiscalValoresCalculados().setValorSeguro(new BigDecimal("10"));
		
		
		
		
		
		return notaFiscal;
	}
	
}
