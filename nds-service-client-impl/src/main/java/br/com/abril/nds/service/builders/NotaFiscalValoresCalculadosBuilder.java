package br.com.abril.nds.service.builders;

import java.math.BigDecimal;

import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.fiscal.nota.DetalheNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalValorCalculado;
import br.com.abril.nds.model.fiscal.notafiscal.ValoresCalculadosWrapper;
import br.com.abril.nds.service.impl.NFeCalculatorImpl;
import br.com.abril.nds.util.CurrencyUtil;

public class NotaFiscalValoresCalculadosBuilder {
	
	public static NotaFiscal montarValoresCalculados(NotaFiscal notaFiscal) {
		
		BigDecimal valorTotalItens = calcularValorTotalItens(notaFiscal);
		BigDecimal valorICMS = calcularImpostosICMS(notaFiscal);
		BigDecimal valorIPI = calcularImpostosIPI(notaFiscal);
		BigDecimal valorCofins = calcularImpostosCofins(notaFiscal);
		BigDecimal valorPIS = calcularImpostosPIS(notaFiscal);
		
		if(notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados() == null ){
			notaFiscal.getNotaFiscalInformacoes().setNotaFiscalValoresCalculados(new NotaFiscalValorCalculado());
			
		}
		
		if(notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados() == null ){
			notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValoresCalculados(new ValoresCalculadosWrapper());
			
		}
		
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setISSQNBase(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setISSQNTotal(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setISSQNValor(BigDecimal.valueOf(0));
		
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorDesconto(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorFrete(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorNF(valorTotalItens);
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorProdutos(valorTotalItens);
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorSeguro(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorImpostoImportacao(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorIPI(CurrencyUtil.arredondarValorParaDuasCasas(valorIPI));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorBaseICMS(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorBaseICMSSubstituto(BigDecimal.valueOf(0));
		
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorICMS(CurrencyUtil.arredondarValorParaDuasCasas(valorICMS));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setvICMSDeson(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorICMSSubstituto(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorOutro(BigDecimal.valueOf(0));
		
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorPIS(valorPIS);
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorCOFINS(valorCofins);
		return notaFiscal;
	}

	private static BigDecimal calcularValorTotalItens(NotaFiscal notaFiscal) {
		
		BigDecimal valorTotalItens = BigDecimal.ZERO;
		for(DetalheNotaFiscal dnf : notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal()) {
			valorTotalItens = valorTotalItens.add(CurrencyUtil.arredondarValorParaDuasCasas(dnf.getProdutoServico().getValorTotalBruto()));
		}
		
		return valorTotalItens;
	}

	private static BigDecimal calcularImpostosICMS(NotaFiscal notaFiscal) {
		
		BigDecimal valorICMS = BigDecimal.ZERO;
		for(DetalheNotaFiscal dnf : notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal()) {
			if(dnf.getImpostos().getIcms() != null) {
				valorICMS = NFeCalculatorImpl.calculate(dnf.getImpostos().getIcms());				
			} else {
				valorICMS = NFeCalculatorImpl.calculate(dnf.getImpostos().getIcmsst());		
			}
		}
		
		return valorICMS;
	}
	
	private static BigDecimal calcularImpostosIPI(NotaFiscal notaFiscal) {
		
		BigDecimal valorIPI = BigDecimal.ZERO;
		for(DetalheNotaFiscal dnf : notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal()) {
			
			if(dnf.getImpostos().getIpi() != null) {				
				valorIPI = NFeCalculatorImpl.calculate(dnf.getImpostos().getIpi());
			}
			
		}
		
		return valorIPI;
	}
	
	private static BigDecimal calcularImpostosCofins(NotaFiscal notaFiscal) {
		
		BigDecimal valorCofins = BigDecimal.ZERO;
		for(DetalheNotaFiscal dnf : notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal()) {
			
			if(dnf.getImpostos().getCofins() != null) {				
				valorCofins = valorCofins.add(NFeCalculatorImpl.calculate(dnf.getImpostos().getCofins().getCofins()));
			}
			
			if(dnf.getImpostos().getCofinsOutr() != null) {
				valorCofins = valorCofins.add(NFeCalculatorImpl.calculate(dnf.getImpostos().getCofinsOutr().getCofins()));
			}
			
		}
		
		return CurrencyUtil.arredondarValorParaDuasCasas(valorCofins);
	}
	
	private static BigDecimal calcularImpostosPIS(NotaFiscal notaFiscal) {
		
		BigDecimal valorPIS = BigDecimal.ZERO;
		
		for(DetalheNotaFiscal dnf : notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal()) {
			
			if(dnf.getImpostos().getPis() != null) {
				
				valorPIS = valorPIS.add(NFeCalculatorImpl.calculate(dnf.getImpostos().getPis().getPis()));
			}
			
			if(dnf.getImpostos().getPisOutr() != null) {
				valorPIS = valorPIS.add(NFeCalculatorImpl.calculate(dnf.getImpostos().getPisOutr().getPis()));
			}
			
		}
		
		return CurrencyUtil.arredondarValorParaDuasCasas(valorPIS);
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
		
		if(notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados() == null ){
			notaFiscal.getNotaFiscalInformacoes().setNotaFiscalValoresCalculados(new NotaFiscalValorCalculado());
			
		}
		
		if(notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados() == null ){
			notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().setValoresCalculados(new ValoresCalculadosWrapper());
			
		}
		
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setISSQNBase(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setISSQNTotal(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setISSQNValor(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorBaseICMS(valorTotalItens);
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorBaseICMSSubstituto(valorTotalItens);
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorDesconto(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorFrete(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorICMS(valorICMS);
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setvICMSDeson(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorICMSSubstituto(valorICMS);
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorImpostoImportacao(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorIPI(valorIPI);
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorNF(valorTotalItens);
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorOutro(BigDecimal.valueOf(0));
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorProdutos(valorTotalItens);
		notaFiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados().getValoresCalculados().setValorSeguro(BigDecimal.valueOf(0));
		
		return notaFiscal;
		
	}
	
}