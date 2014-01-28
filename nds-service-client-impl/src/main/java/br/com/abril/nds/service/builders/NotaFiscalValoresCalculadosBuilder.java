package br.com.abril.nds.service.builders;

import java.math.BigDecimal;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;

public class NotaFiscalValoresCalculadosBuilder {
	
	public static NotaFiscal montarValoresCalculados(NotaFiscal notaFiscal, Cota cota){
		
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setISSQNBase(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setISSQNTotal(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setISSQNValor(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorBaseICMS(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorBaseICMSSubstituto(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorDesconto(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorFrete(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorICMS(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorICMSSubstituto(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorIPI(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorNF(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorOutro(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorProdutos(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorSeguro(new BigDecimal("10"));
		
		return notaFiscal;
	}

	public static NotaFiscal montarValoresCalculadosEstoqueProduto(NotaFiscal notaFiscal, EstoqueProduto estoque) {
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setISSQNBase(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setISSQNTotal(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setISSQNValor(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorBaseICMS(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorBaseICMSSubstituto(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorDesconto(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorFrete(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorICMS(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorICMSSubstituto(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorIPI(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorNF(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorOutro(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorProdutos(new BigDecimal("10"));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorSeguro(new BigDecimal("10"));
		
		return notaFiscal;
		
	}
	
}