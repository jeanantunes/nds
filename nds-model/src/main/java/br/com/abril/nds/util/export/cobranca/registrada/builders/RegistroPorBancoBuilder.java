package br.com.abril.nds.util.export.cobranca.registrada.builders;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.ValidationException;

import org.springframework.stereotype.Component;

import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.util.TirarAcento;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.cobranca.registrada.CobRegEnvTipoRegistro00;
import br.com.abril.nds.util.export.cobranca.registrada.CobRegEnvTipoRegistroBradesco00;
import br.com.abril.nds.util.export.cobranca.registrada.CobRegEnvTipoRegistroItau00;

@Component
public class RegistroPorBancoBuilder implements Serializable {

	private static final long serialVersionUID = 1L;

	public static CobRegEnvTipoRegistro00 registroPorBanco(Banco banco, Pessoa pessoaCedente) throws ValidationException {
		
		CobRegEnvTipoRegistro00 registro00 = new CobRegEnvTipoRegistro00();
		
		registro00.setTipoRegistro("0");
		registro00.setIdentificacaoArquivoRemessa("1");
		registro00.setIdentificacaoExtenso("REMESSA");
		registro00.setCodigoServico("01");
		registro00.setLiteralServicos("COBRANCA");
		registro00.setAgenciaCedente(String.valueOf(banco.getAgencia()));
		registro00.setFiller("");
		registro00.setContaCliente(String.valueOf(banco.getConta()));
		registro00.setDigitoConta(banco.getDvConta());
		registro00.setFiller2("");
		
		String nomeCliente = obterNomeCliente(pessoaCedente);
		
		registro00.setNomeCliente(nomeCliente);
		registro00.setNumeroBanco(banco.getNumeroBanco());
		registro00.setNomeBanco(banco.getNome());
		
		registro00.setDataGravacaoArquivo(setarDataFomatada());
		
		registro00.setCodigoUsuario("");
		registro00.setFiller3("");
		registro00.setSequencial("01");
		
		 return registro00;
		 
		 
//		 switch (banco.getNumeroBanco()) {
//			
//				case UtilitarioCNAB.BANCO_HSBC:
//				
//				case UtilitarioCNAB.BANCO_BRADESCO:
//					return RegistroPorBancoBuilder.registroPorBanco(banco, pessoaCedente, registro00);
//					
//				case UtilitarioCNAB.BANCO_DO_BRASIL:
//					return RegistroPorBancoBuilder.registroPorBanco(banco, pessoaCedente, registro00);
//					
//				case UtilitarioCNAB.BANCO_CAIXA_ECONOMICA_FEDERAL:
//					return RegistroPorBancoBuilder.registroPorBanco(banco, pessoaCedente, registro00);
//					
//				case UtilitarioCNAB.BANCO_SANTANDER:
//					return RegistroPorBancoBuilder.registroPorBanco(banco, pessoaCedente, registro00);
//			
//			}
//			return registro00;
	}
	
	public static CobRegEnvTipoRegistroItau00 registroPorBancoItau(Banco banco, Pessoa pessoaCedente) throws ValidationException {
		
		CobRegEnvTipoRegistroItau00 registro00 = new CobRegEnvTipoRegistroItau00();
		
		registro00.setTipoRegistro("0");
		registro00.setIdentificacaoArquivoRemessa("1");
		registro00.setIdentificacaoExtenso("REMESSA");
		registro00.setCodigoServico("01");
		registro00.setLiteralServicos("COBRANCA");
		registro00.setAgenciaCedente(String.valueOf(banco.getAgencia()));
		registro00.setContaCliente(String.valueOf(banco.getConta()));
		registro00.setDigitoConta(banco.getDvConta());
		registro00.setFiller2("");
		
		String nomeCliente = obterNomeCliente(pessoaCedente);
		
		registro00.setNomeCliente(TirarAcento.removerAcentuacao(nomeCliente));
		registro00.setNumeroBanco(banco.getNumeroBanco());
		registro00.setNomeBanco("BANCO ITAU SA");
		
		registro00.setDataGravacaoArquivo(setarDataFomatada());
		registro00.setCodigoUsuario("");
		registro00.setFiller3("");
		registro00.setSequencial("01");
		
		return registro00;
		
	}
	
	public static CobRegEnvTipoRegistroBradesco00 registroPorBancoBradesco(Banco banco, Pessoa pessoaCedente) throws ValidationException {
		
		CobRegEnvTipoRegistroBradesco00 registro00 = new CobRegEnvTipoRegistroBradesco00();
		
		registro00.setTipoRegistro("0");
		registro00.setIdentificacaoArquivoRemessa("1");
		registro00.setIdentificacaoExtenso("REMESSA");
		registro00.setCodigoServico("01");
		registro00.setLiteralServicos("COBRANCA");
		registro00.setAgenciaCedente(String.valueOf(banco.getAgencia()));
		registro00.setContaCliente(String.valueOf(banco.getConta()));
		registro00.setDigitoConta(banco.getDvConta());
		registro00.setFiller2("");
		
		String nomeCliente = obterNomeCliente(pessoaCedente);
		
		registro00.setNomeCliente(TirarAcento.removerAcentuacao(nomeCliente));
		registro00.setNumeroBanco(banco.getNumeroBanco());
		registro00.setNomeBanco("BRADESCO SA");
		
		registro00.setDataGravacaoArquivo(setarDataFomatada());
		registro00.setCodigoUsuario("");
		registro00.setFiller3("");
		registro00.setSequencial("01");
		
		return registro00;
		
	}
	
	private static String obterNomeCliente(Pessoa pessoaCedente) {
			
		if(pessoaCedente instanceof PessoaJuridica) {
		    
		   return ((PessoaJuridica) pessoaCedente).getRazaoSocial();
		   
		} else {
		    
			return ((PessoaFisica) pessoaCedente).getNome();
		   
		}
	}
	
	private static String setarDataFomatada() throws ValidationException {
		try {
			return Util.formataData(new Date());
		} catch (Exception e) {
			throw new ValidationException("Não formatar a data do arquivo");
		}
	}
}
