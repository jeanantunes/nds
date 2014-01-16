package br.com.abril.nds.service.builders;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.dto.filtro.FiltroViewNotaFiscalDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.nfe.NotaFiscalNds;
import br.com.abril.nds.model.fiscal.nota.Identificacao;
import br.com.abril.nds.model.fiscal.nota.Identificacao.FormaPagamento;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFicalEndereco;;

public class NotaFiscalBuilder implements Serializable {
	
	private static final long serialVersionUID = 176874569807919538L;
	
	// builder header Nota fiscal
	public static NotaFiscalNds montarHeaderNotaFiscal(NotaFiscalNds notaFiscal, Cota cota){
		
		/** -- Natureza da Operação -- PROT. DE AUTORIZAÇÃO -- CRT(Codigo Regime Tributario)
	     *  -- Inscricao Estadual   -- INSCRIÇÃO ESTADUAL DO SUBSTITUTO TRIBUTÁRIO -- CNPJ/CPF
		 */
		if (cota.getPessoa() instanceof PessoaJuridica) {
			
			PessoaJuridica pessoaJuridica = (PessoaJuridica) cota.getPessoa();
			notaFiscal.getEmissor().setRazaoSocial(pessoaJuridica.getRazaoSocial());
			notaFiscal.getEmissor().setNomeFantasia(pessoaJuridica.getNomeFantasia() == null ?  cota.getPessoa().getNome() : pessoaJuridica.getNomeFantasia());
			notaFiscal.getEmissor().setInscricaoEstadual(pessoaJuridica.getInscricaoEstadual());
			notaFiscal.getEmissor().setCnpj(pessoaJuridica.getCnpj());
			notaFiscal.getEmissor().setEmail(pessoaJuridica.getEmail());
			
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


	public static void popularDadosDistribuidor(NotaFiscal notaFiscal2,
			Distribuidor distribuidor, FiltroViewNotaFiscalDTO filtro) {
		
		if(notaFiscal2.getIdentificacaoEmitente() == null) {
			notaFiscal2.setIdentificacaoEmitente(new IdentificacaoEmitente());
		}
		
		if(notaFiscal2.getIdentificacaoEmitente().getEndereco() == null) {
			notaFiscal2.getIdentificacaoEmitente().setEndereco(new NotaFicalEndereco());
		}
		
		// Dados do Distribuidor
		notaFiscal2.getIdentificacaoEmitente().setNome(distribuidor.getJuridica().getRazaoSocial());
		notaFiscal2.getIdentificacaoEmitente().setNomeFantasia(distribuidor.getJuridica().getNomeFantasia());
		notaFiscal2.getIdentificacaoEmitente().setInscricaoEstadual(distribuidor.getJuridica().getInscricaoEstadual());
		notaFiscal2.getIdentificacaoEmitente().setDocumento("1233");
		
		notaFiscal2.getIdentificacaoEmitente().getEndereco().setId(distribuidor.getEnderecoDistribuidor().getEndereco().getId());
		notaFiscal2.getIdentificacaoEmitente().getEndereco().setUf(distribuidor.getEnderecoDistribuidor().getEndereco().getUf());
		notaFiscal2.getIdentificacaoEmitente().getEndereco().setCidade(distribuidor.getEnderecoDistribuidor().getEndereco().getCidade());
		notaFiscal2.getIdentificacaoEmitente().getEndereco().setBairro(distribuidor.getEnderecoDistribuidor().getEndereco().getBairro());
		notaFiscal2.getIdentificacaoEmitente().getEndereco().setLogradouro(distribuidor.getEnderecoDistribuidor().getEndereco().getLogradouro());
		try {
			notaFiscal2.getIdentificacaoEmitente().getEndereco().setCodigoCidadeIBGE(35L); //distribuidor.getEnderecoDistribuidor().getEndereco().getCodigoCidadeIBGE().longValue());
			notaFiscal2.getIdentificacaoEmitente().getEndereco().setCodigoUf(50308L); //Long.parseLong(distribuidor.getEnderecoDistribuidor().getEndereco().getCodigoUf()));
		} catch (Exception e) {
			//FIXME: Colocar um logger
		}
		
	}


	public static void montarHeaderNotaFiscal(NotaFiscal notaFiscal2, Cota cota) {
		
		if(notaFiscal2.getIdentificacao() == null) {
			notaFiscal2.setIdentificacao(new Identificacao());
		}
		
		if(notaFiscal2.getIdentificacaoDestinatario() == null) {
			notaFiscal2.setIdentificacaoDestinatario(new IdentificacaoDestinatario());
		}

		notaFiscal2.getIdentificacao().setFormaPagamento(FormaPagamento.A_VISTA);
		
		notaFiscal2.getIdentificacao().setNumeroDocumentoFiscal(1L);
		
		notaFiscal2.getIdentificacao().setDataEmissao(new Date());
		
		notaFiscal2.getIdentificacao().setCodigoUf(35L); //notaFiscal2.getIdentificacaoEmitente().getEndereco().getCodigoUf());
		
		notaFiscal2.getIdentificacao().setCodigoMunicipio(50308L); //notaFiscal2.getIdentificacaoEmitente().getEndereco().getCodigoCidadeIBGE());
		
		notaFiscal2.getIdentificacao().setSerie(1);
		
		notaFiscal2.getIdentificacao().setTipoOperacao(TipoOperacao.SAIDA);
		
		notaFiscal2.getIdentificacao();
		
		notaFiscal2.getIdentificacaoDestinatario().setDocumento("123");
	}


	public static void popularDadosTransportadora(NotaFiscal notaFiscal2, Distribuidor distribuidor, FiltroViewNotaFiscalDTO filtro) {
		
		if(notaFiscal2.getInformacaoTransporte() == null) {
			notaFiscal2.setInformacaoTransporte(new InformacaoTransporte());
		}
		
		notaFiscal2.getInformacaoTransporte().setModalidadeFrente(1);
	}
}
