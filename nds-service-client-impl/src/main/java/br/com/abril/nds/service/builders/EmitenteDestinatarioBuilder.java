package br.com.abril.nds.service.builders;

import java.util.Date;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.fiscal.nfe.NotaFiscal;

public class EmitenteDestinatarioBuilder {
	
	public static NotaFiscal montarEnderecoEmitenteDestinatario (NotaFiscal notaFiscal, Cota cota){
		
		if (cota.getPessoa() instanceof PessoaJuridica) {
			PessoaJuridica pessoaJuridica = (PessoaJuridica) cota.getPessoa();
			notaFiscal.getEmissor().setRazaoSocial(pessoaJuridica.getRazaoSocial());
			notaFiscal.getEmissor().setCnpj(pessoaJuridica.getCnpj());
			
		} else if (cota.getPessoa() instanceof PessoaFisica) {			
			PessoaFisica pessoaFisica = (PessoaFisica) cota.getPessoa();
			notaFiscal.getEmissor().setRazaoSocial(pessoaFisica.getNome());
			notaFiscal.getEmissor().setCnpj(pessoaFisica.getCpf());
		}
		
		notaFiscal.setDataEmissao(new Date());			
		notaFiscal.getEmissor().getNotaFicalEndereco().setBairro(cota.getEnderecoPrincipal().getEndereco().getBairro());
		notaFiscal.getEmissor().getNotaFicalEndereco().setCidade(cota.getEnderecoPrincipal().getEndereco().getCidade());
		notaFiscal.getEmissor().getNotaFicalEndereco().setUf(cota.getEnderecoPrincipal().getEndereco().getUf());
		notaFiscal.getEmissor().getNotaFicalEndereco().setLogradouro(cota.getEnderecoPrincipal().getEndereco().getLogradouro());
		notaFiscal.getEmissor().getNotaFicalEndereco().setCEP(cota.getEnderecoPrincipal().getEndereco().getCep());
		notaFiscal.getEmissor().getNotaFicalEndereco().setComplemento(cota.getEnderecoPrincipal().getEndereco().getComplemento());
		notaFiscal.getEmissor().getNotaFicalEndereco().setNumero(cota.getEnderecoPrincipal().getEndereco().getNumero());
		
		return notaFiscal;

	}
	
}