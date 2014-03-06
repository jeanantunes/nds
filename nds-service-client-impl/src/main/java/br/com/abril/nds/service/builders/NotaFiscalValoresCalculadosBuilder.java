package br.com.abril.nds.service.builders;

import java.math.BigDecimal;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.fiscal.nota.DetalheNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.service.impl.NFeCalculatorImpl;
import br.com.abril.nds.util.CurrencyUtil;

public class NotaFiscalValoresCalculadosBuilder {
	
	public static NotaFiscal montarValoresCalculados(NotaFiscal notaFiscal, Cota cota){
		
		BigDecimal valorTotalItens = calcularValorTotalItens(notaFiscal);
		BigDecimal valorICMS = calcularImpostosICMS(notaFiscal);
		BigDecimal valorIPI = calcularImpostosIPI(notaFiscal);
		BigDecimal valorCofins = calcularImpostosIPI(notaFiscal);
		BigDecimal valorPIS = calcularImpostosIPI(notaFiscal);
		
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setISSQNBase(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setISSQNTotal(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setISSQNValor(BigDecimal.valueOf(0));
		
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorDesconto(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorFrete(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorNF(valorTotalItens);
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorProdutos(valorTotalItens);
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorSeguro(BigDecimal.valueOf(0));
		
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorIPI(CurrencyUtil.arredondarValorParaQuatroCasas(valorIPI));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorBaseICMS(valorTotalItens);
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorBaseICMSSubstituto(valorTotalItens);
		
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorICMS(CurrencyUtil.arredondarValorParaQuatroCasas(valorICMS));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorICMSSubstituto(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorOutro(BigDecimal.valueOf(0));
		
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorPIS(CurrencyUtil.arredondarValorParaQuatroCasas(valorPIS));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorCOFINS(CurrencyUtil.arredondarValorParaQuatroCasas(valorCofins));
		return notaFiscal;
	}

	private static BigDecimal calcularValorTotalItens(NotaFiscal notaFiscal) {
		
		BigDecimal valorTotalItens = BigDecimal.ZERO;
		for(DetalheNotaFiscal dnf : notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal()) {
			valorTotalItens = valorTotalItens.add(dnf.getProdutoServico().getValorTotalBruto());
		}
		
		return valorTotalItens;
	}

	private static BigDecimal calcularImpostosICMS(NotaFiscal notaFiscal) {
		
		BigDecimal valorICMS = BigDecimal.ZERO;
		for(DetalheNotaFiscal dnf : notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal()) {
			valorICMS = NFeCalculatorImpl.calculate(dnf.getImpostos().getIcms());
		}
		
		return valorICMS;
	}
	
	private static BigDecimal calcularImpostosIPI(NotaFiscal notaFiscal) {
		
		BigDecimal valorIPI = BigDecimal.ZERO;
		for(DetalheNotaFiscal dnf : notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal()) {
			valorIPI = NFeCalculatorImpl.calculate(dnf.getImpostos().getIpi());
		}
		
		return valorIPI;
	}
	
	/**
	 * NOT TODO Não retirar será utilizada para nota 3.0 
	 */
	@SuppressWarnings("unused")
	private static BigDecimal calcularImpostosICMSSubstituto(NotaFiscal notaFiscal) {
		
		BigDecimal valorICMSSubstituto = BigDecimal.ZERO;
		for(DetalheNotaFiscal dnf : notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal()) {
			valorICMSSubstituto = NFeCalculatorImpl.calculate(dnf.getImpostos().getIcms());
		}
		
		return valorICMSSubstituto;
	}
	
	public static NotaFiscal montarValoresCalculadosEstoqueProduto(NotaFiscal notaFiscal, EstoqueProduto estoque) {
		
		BigDecimal valorTotalItens = calcularValorTotalItens(notaFiscal);
		BigDecimal valorICMS = calcularImpostosICMS(notaFiscal);
		BigDecimal valorIPI = calcularImpostosIPI(notaFiscal);
		
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setISSQNBase(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setISSQNTotal(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setISSQNValor(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorBaseICMS(valorTotalItens);
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorBaseICMSSubstituto(valorTotalItens);
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorDesconto(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorFrete(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorICMS(valorICMS);
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorICMSSubstituto(valorICMS);
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorIPI(valorIPI);
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorNF(valorTotalItens);
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorOutro(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorProdutos(valorTotalItens);
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValorSeguro(BigDecimal.valueOf(0));
		
		return notaFiscal;
		
	}
	
}