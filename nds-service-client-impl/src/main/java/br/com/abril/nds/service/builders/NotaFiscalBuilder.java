package br.com.abril.nds.service.builders;

import java.io.Serializable;

import br.com.abril.nds.dto.filtro.FiltroViewNotaFiscalDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.nfe.NotaFiscalNds;

public class NotaFiscalBuilder implements Serializable {
	
	private static final long serialVersionUID = 176874569807919538L;
	
	// builder header Nota fiscal
	public static NotaFiscalNds montarHeaderNotaFiscal(NotaFiscalNds notaFiscal, Cota cota){
		
		/** -- Natureza da Operação -- PROT. DE AUTORIZAÇÃO -- CRT(Codigo Regime Tributario)
	     * -- Inscricao Estadual -- INSCRIÇÃO ESTADUAL DO SUBSTITUTO TRIBUTÁRIO -- CNPJ/CPF
		 */
		if (cota.getPessoa() instanceof PessoaJuridica) {
			
			PessoaJuridica pessoaJuridica = (PessoaJuridica) cota.getPessoa();
			notaFiscal.getEmissor().setRazaoSocial(pessoaJuridica.getRazaoSocial());
			notaFiscal.getEmissor().setNomeFantasia(pessoaJuridica.getNomeFantasia() == null ?  cota.getPessoa().getNome() : pessoaJuridica.getNomeFantasia());
			notaFiscal.getEmissor().setInscricaoEstadual(pessoaJuridica.getInscricaoEstadual());
			notaFiscal.getEmissor().setCnpj(pessoaJuridica.getCnpj());

			
		} else if (cota.getPessoa() instanceof PessoaFisica) {
			PessoaFisica pessoaFisica = (PessoaFisica) cota.getPessoa();
			pessoaFisica.getCpf();
			pessoaFisica.getRg();
			pessoaFisica.getEmail();
		}
				
		return notaFiscal;
	}


	// metodo responsavel pelo dados do distribuidor da nota
	public static NotaFiscalNds popularDadosDistribuidor(NotaFiscalNds notaFiscal, Distribuidor distribuidor, FiltroViewNotaFiscalDTO filtro){
		
		// Dados do Distribuidor
		notaFiscal.getEmissor().setNomeFantasia(distribuidor.getJuridica().getRazaoSocial());
		notaFiscal.getEmissor().setInscricaoEstadual(distribuidor.getJuridica().getInscricaoEstadual());
		notaFiscal.getEmissor().setCnpj(distribuidor.getJuridica().getCnpj());
		
		// Endereço
		notaFiscal.getEmissor().getNotaFicalEndereco().setUf(distribuidor.getEnderecoDistribuidor().getEndereco().getUf());
		notaFiscal.getEmissor().getNotaFicalEndereco().setCidade(distribuidor.getEnderecoDistribuidor().getEndereco().getCidade());
		notaFiscal.getEmissor().getNotaFicalEndereco().setBairro(distribuidor.getEnderecoDistribuidor().getEndereco().getBairro());
		notaFiscal.getEmissor().getNotaFicalEndereco().setLogradouro(distribuidor.getEnderecoDistribuidor().getEndereco().getLogradouro());
		
		return notaFiscal;
	}
	
	public TipoOperacao tipoOperacaoNotaFiscal (){
		
		return null;
	}
}
