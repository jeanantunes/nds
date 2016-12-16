package br.com.abril.nds.util.export.cobranca.registrada.builders;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.ValidationException;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.cobranca.registrada.CobRegEnvTipoRegistro01;
import br.com.abril.nds.util.export.cobranca.registrada.CobRegEnvTipoRegistroBradesco01;
import br.com.abril.nds.util.export.cobranca.registrada.CobRegEnvTipoRegistroItau01;

@Component
public class DetalheRegistroRegistroPorBancoBuilder implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public static CobRegEnvTipoRegistro01 detalheRegistroPorBanco(Boleto boleto, Banco banco, int count, CobRegEnvTipoRegistro01 registro01) throws ValidationException {
		registro01.setTipoRegistro("1");
		registro01.setFiller("");
		registro01.setAgenciaCedente(String.valueOf(banco.getAgencia()));
		registro01.setFiller1("");
		registro01.setContaCliente(String.valueOf(banco.getConta()));
		registro01.setDigitoConta(String.valueOf(banco.getDvConta()));
		registro01.setTaxa(String.valueOf(banco.getJuros().intValue()));
		registro01.setFiller2("");
		
		// não sei o que setar
		registro01.setNumeroControle("00");
		registro01.setNossoNumero(boleto.getNossoNumero());;
		registro01.setDigitoNossoNumero(boleto.getDigitoNossoNumero());
		registro01.setNumeroContrato("0");
		registro01.setDataSegundoDesconto("0");
		registro01.setValorSegundoDesconto("0");;
		registro01.setFiller3("");
		registro01.setCarteira(String.valueOf(banco.getCarteira()));
		
		// não sei o que setar
		registro01.setCodigoServico("01");
		
		registro01.setNumero(boleto.getNossoNumero());
		
		try {
			registro01.setDataVencimento(Util.formataData(boleto.getDataVencimento()));
			registro01.setDataEmissao(Util.formataData(boleto.getDataEmissao()));
		} catch (Exception e) {
			throw new ValidationException("Erro ao Formatar a Data Vencimento / Emissão");
		}
		
		registro01.setValorTitulo(CurrencyUtil.formatarValor(boleto.getValor()).replace(".", ""));
		registro01.setNumeroBanco(banco.getNumeroBanco());
		registro01.setAgencia(String.valueOf(banco.getAgencia()));
		registro01.setFiller4(" ");
		registro01.setEspecie("01");
		registro01.setAceite("A");
		registro01.setJurosDia("0");;
		registro01.setDataDesconto("0");
		registro01.setValorDesconto("0");
		registro01.setValorIOC("0");;
		registro01.setValorAbatimento("0");
		
		
		
		registro01.setSequencialRegistro(String.valueOf(count));
		
		return registro01;
	}
	
	public static CobRegEnvTipoRegistroItau01 detalheRegistroPorBancoItau(Boleto boleto, Banco banco, int count) throws ValidationException {
		
		CobRegEnvTipoRegistroItau01 registro01 = new CobRegEnvTipoRegistroItau01();
		
		registro01.setTipoRegistro("1");
		registro01.setCodigoInscricao("02");
		
		Pessoa pessoaCedente = banco.getPessoaJuridicaCedente();
		
		
		String nomeCedente = "";
        String documentoCedente = "";
        
        if(pessoaCedente instanceof PessoaJuridica) {
            
            nomeCedente = ((PessoaJuridica) pessoaCedente).getRazaoSocial();
            documentoCedente = ((PessoaJuridica) pessoaCedente).getCnpj();
            
        } else {
            
            nomeCedente = ((PessoaFisica) pessoaCedente).getNome();
            documentoCedente = ((PessoaFisica) pessoaCedente).getCpf();
            
        }
		
		registro01.setNumeroInscricao(documentoCedente.replace(".", "").replace("/", "").replace("-", ""));
		registro01.setAgenciaCedente(String.valueOf(banco.getAgencia()));
		registro01.setZeros("00");
		registro01.setContaCliente(String.valueOf(banco.getConta()));
		registro01.setDigitoConta(String.valueOf(banco.getDvConta()));
		registro01.setBrancos("");
		registro01.setInstrucao("0000");
		registro01.setUsoDaEmpresa("");
		registro01.setNossoNumero(boleto.getNossoNumero());
		registro01.setQtdeMoeda("0");
		registro01.setNumeroCarteira(String.valueOf(banco.getCarteira()));
		registro01.setUsoBanco("");
		registro01.setCarteira("I");
		registro01.setCodOcerrencia("01");
		registro01.setNumeroDocumento(boleto.getNossoNumero());
		
		try {
			registro01.setDataVencimento(Util.formataData(boleto.getDataVencimento()));
			registro01.setDataDeMora(Util.formataData(boleto.getDataVencimento()));
			registro01.setDataEmissao(Util.formataData(boleto.getDataEmissao()));
		} catch (Exception e) {
			throw new ValidationException("Erro ao Formatar a Data Vencimento / Emissão");
		}
		
		BigDecimal valorDuasCadasDecimais = CurrencyUtil.truncateDecimal(boleto.getValor(), 2);
        
		registro01.setValorTitulo(Util.getValorString(valorDuasCadasDecimais.toEngineeringString()));
		
		registro01.setNumeroBanco(banco.getNumeroBanco());
		
		registro01.setEspecie("01");
		
		registro01.setAceite("N");
		
		registro01.setAgencia(String.valueOf("00000"));
		
		registro01.setCodigoInstrucao("09");
		
		registro01.setCodigoInstrucao2("00");
		
		BigDecimal taxaJurosDiaria = MathUtil.divide(banco.getJuros(), new BigDecimal(30));
		
		BigDecimal valorJurosCalculado = CurrencyUtil.truncateDecimal((boleto.getValor().multiply(MathUtil.divide(taxaJurosDiaria, new BigDecimal(100)))), 2);
		
		registro01.setJurosDia(Util.getValorString(valorJurosCalculado.toEngineeringString()));
		
		registro01.setDataDesconto("0");
		
		registro01.setValorDesconto("0");
		
		registro01.setValorIOC("0");
		
		registro01.setValorAbatimento("0");
		
		registro01.setPrazo("10");
		
		registro01.setBranco2("");
		
		PopularSacadoBuilder.popularSacadoCobrana(registro01, boleto);
		
		registro01.setSequencialRegistro(String.valueOf(count));
		
		return registro01;
	}
	
	public static CobRegEnvTipoRegistroBradesco01 detalheRegistroPorBancoBradesco(Boleto boleto, Banco banco, int count) throws ValidationException {
		
		CobRegEnvTipoRegistroBradesco01 registro01 = new CobRegEnvTipoRegistroBradesco01();
		
		registro01.setTipoRegistro("1");
		registro01.setCodigoInscricao("02");
		
		Pessoa pessoaCedente = banco.getPessoaJuridicaCedente();
		
		String nomeCedente = "";
        String documentoCedente = "";
        
        if(pessoaCedente instanceof PessoaJuridica) {
            
            nomeCedente = ((PessoaJuridica) pessoaCedente).getRazaoSocial();
            documentoCedente = ((PessoaJuridica) pessoaCedente).getCnpj();
            
        } else {
            
            nomeCedente = ((PessoaFisica) pessoaCedente).getNome();
            documentoCedente = ((PessoaFisica) pessoaCedente).getCpf();
            
        }
		
		registro01.setNumeroInscricao(documentoCedente.replace(".", "").replace("/", "").replace("-", ""));
		registro01.setAgenciaCedente(String.valueOf(banco.getAgencia()));
		registro01.setZeros("00");
		registro01.setContaCliente(String.valueOf(banco.getConta()));
		registro01.setDigitoConta(String.valueOf(banco.getDvConta()));
		registro01.setBrancos("");
		registro01.setInstrucao("0000");
		registro01.setUsoDaEmpresa("");
		registro01.setNossoNumero(boleto.getNossoNumero());
		registro01.setQtdeMoeda("0");
		registro01.setNumeroCarteira(String.valueOf(banco.getCarteira()));
		registro01.setUsoBanco("");
		registro01.setCarteira("I");
		registro01.setCodOcerrencia("01");
		registro01.setNumeroDocumento(boleto.getNossoNumero());
		
		try {
			registro01.setDataVencimento(Util.formataData(boleto.getDataVencimento()));
			registro01.setDataDeMora(Util.formataData(boleto.getDataVencimento()));
			registro01.setDataEmissao(Util.formataData(boleto.getDataEmissao()));
		} catch (Exception e) {
			throw new ValidationException("Erro ao Formatar a Data Vencimento / Emissão");
		}
		
		BigDecimal valorDuasCadasDecimais = CurrencyUtil.truncateDecimal(boleto.getValor(), 2);
        
		registro01.setValorTitulo(Util.getValorString(valorDuasCadasDecimais.toEngineeringString()));
		
		registro01.setNumeroBanco(banco.getNumeroBanco());
		
		registro01.setEspecie("01");
		
		registro01.setAceite("N");
		
		registro01.setAgencia(String.valueOf("00000"));
		
		registro01.setCodigoInstrucao("09");
		
		registro01.setCodigoInstrucao2("00");
		
		BigDecimal taxaJurosDiaria = MathUtil.divide(banco.getJuros(), new BigDecimal(30));
		
		BigDecimal valorJurosCalculado = CurrencyUtil.truncateDecimal((boleto.getValor().multiply(MathUtil.divide(taxaJurosDiaria, new BigDecimal(100)))), 2);
		
		registro01.setJurosDia(Util.getValorString(valorJurosCalculado.toEngineeringString()));
		
		registro01.setDataDesconto("0");
		
		registro01.setValorDesconto("0");
		
		registro01.setValorIOC("0");
		
		registro01.setValorAbatimento("0");
		
		registro01.setPrazo("10");
		
		registro01.setBranco2("");
		
		registro01.setSequencialRegistro(String.valueOf(count));
		
		PopularSacadoBuilder.popularSacadoCobrana(registro01, boleto);
		
		return registro01;
	}
	
}
