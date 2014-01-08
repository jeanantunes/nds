package br.com.abril.nds.service.builders;

import java.util.Date;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.fiscal.nfe.NotaFiscalNds;
import br.com.caelum.vraptor.Get;

public class EmitenteDestinatarioBuilder {
	
	public static NotaFiscalNds montarEnderecoEmitenteDestinatario (NotaFiscalNds notaFiscal, Cota cota){
		
		if (cota.getPessoa() instanceof PessoaJuridica) {
			PessoaJuridica pessoaJuridica = (PessoaJuridica) cota.getPessoa();
			notaFiscal.getEmitenteDestinario().setNome(pessoaJuridica.getRazaoSocial());
			notaFiscal.getEmitenteDestinario().setEmail(pessoaJuridica.getEmail());
			
		} else if (cota.getPessoa() instanceof PessoaFisica) {			
			PessoaFisica pessoaFisica = (PessoaFisica) cota.getPessoa();
			notaFiscal.getEmitenteDestinario().setNome(pessoaFisica.getNome());
			notaFiscal.getEmitenteDestinario().setEmail(pessoaFisica.getEmail());
		}
		
		notaFiscal.setDataEmissao(new Date());			
		
		return notaFiscal;

	}
	
}