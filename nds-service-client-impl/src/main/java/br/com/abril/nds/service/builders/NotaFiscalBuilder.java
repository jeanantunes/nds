package br.com.abril.nds.service.builders;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;

import br.com.abril.nds.dto.filtro.FiltroViewNotaFiscalDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.nfe.NotaFiscalNds;
import br.com.abril.nds.model.fiscal.nota.Identificacao;
import br.com.abril.nds.model.fiscal.nota.Identificacao.FormaPagamento;
import br.com.abril.nds.model.fiscal.nota.Identificacao.FormatoImpressao;
import br.com.abril.nds.model.fiscal.nota.Identificacao.ProcessoEmissao;
import br.com.abril.nds.model.fiscal.nota.Identificacao.TipoAmbiente;
import br.com.abril.nds.model.fiscal.nota.Identificacao.TipoEmissao;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscalInformacoes;
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
		notaFiscal.getEmissor().getNotaFicalEndereco().setLogradouro(distribuidor.getEnderecoDistribuidor().getEndereco().getLogradouro());
		notaFiscal.getEmissor().getNotaFicalEndereco().setNumero(distribuidor.getEnderecoDistribuidor().getEndereco().getNumero());
		notaFiscal.getEmissor().getNotaFicalEndereco().setBairro(distribuidor.getEnderecoDistribuidor().getEndereco().getBairro());
		notaFiscal.getEmissor().getNotaFicalEndereco().setCidade(distribuidor.getEnderecoDistribuidor().getEndereco().getCidade());
		notaFiscal.getEmissor().getNotaFicalEndereco().setUf(distribuidor.getEnderecoDistribuidor().getEndereco().getUf());
		
		return notaFiscal;
	}
	
	public TipoOperacao tipoOperacaoNotaFiscal (){
		
		return null;
	}


	public static void popularDadosDistribuidor(NotaFiscal notaFiscal2, Distribuidor distribuidor, FiltroViewNotaFiscalDTO filtro) {
		
		if(notaFiscal2.getNotaFiscalInformacoes() == null) {
			notaFiscal2.setNotaFiscalInformacoes(new NotaFiscalInformacoes());
		}
		
		if(notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente() == null) {
			notaFiscal2.getNotaFiscalInformacoes().setIdentificacaoEmitente(new IdentificacaoEmitente());
		}
		
		if(notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco() == null) {
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().setEndereco(new NotaFicalEndereco());
		}
		
		// Dados do Distribuidor
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().setNome(distribuidor.getJuridica().getRazaoSocial());
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().setNomeFantasia(distribuidor.getJuridica().getNomeFantasia());
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().setInscricaoEstadual(distribuidor.getJuridica().getInscricaoEstadual());
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().setDocumento(distribuidor.getJuridica().getCnpj().replaceAll("/", "").replaceAll("\\.", "").replaceAll("-", ""));
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setId(distribuidor.getEnderecoDistribuidor().getEndereco().getId());
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setLogradouro(distribuidor.getEnderecoDistribuidor().getEndereco().getLogradouro());
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setNumero(distribuidor.getEnderecoDistribuidor().getEndereco().getNumero());
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setBairro(distribuidor.getEnderecoDistribuidor().getEndereco().getBairro());
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setCidade(distribuidor.getEnderecoDistribuidor().getEndereco().getCidade());
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setUf(distribuidor.getEnderecoDistribuidor().getEndereco().getUf());
		
		try {
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setCodigoCidadeIBGE(3550308L); //distribuidor.getEnderecoDistribuidor().getEndereco().getCodigoCidadeIBGE().longValue());
			notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setCodigoUf(35L); //Long.parseLong(distribuidor.getEnderecoDistribuidor().getEndereco().getCodigoUf()));
		} catch (Exception e) {
			//FIXME: Colocar um logger
		}
		
	}


	public static void montarHeaderNotaFiscal(NotaFiscal notaFiscal2, Cota cota) {
		
		if(notaFiscal2.getNotaFiscalInformacoes().getIdentificacao() == null) {
			notaFiscal2.getNotaFiscalInformacoes().setIdentificacao(new Identificacao());
		}
		
		if(notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario() == null) {
			notaFiscal2.getNotaFiscalInformacoes().setIdentificacaoDestinatario(new IdentificacaoDestinatario());
		}

		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setFormaPagamento(FormaPagamento.A_VISTA);
		
		//FIXME: Ajustar para variavel global
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setTipoAmbiente(TipoAmbiente.HOMOLOGACAO);
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setTipoEmissao(TipoEmissao.NORMAL);
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setFormatoImpressao(FormatoImpressao.PAISAGEM);
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setModeloDocumentoFiscal("55");
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setProcessoEmissao(ProcessoEmissao.EMISSAO_NFE_APLICATIVO_FORNECIDO_PELO_FISCO);
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setNumeroDocumentoFiscal(Long.parseLong(new Random().nextInt(10000000)+""));
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setDataEmissao(new Date());
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setCodigoUf(35L); //notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().getCodigoUf());
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setCodigoMunicipio(3550308L); //notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().getCodigoCidadeIBGE());
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setCodigoNF(0L);
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setDigitoVerificadorChaveAcesso(0L);
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setSerie(1);
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setTipoOperacao(TipoOperacao.SAIDA);
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao();
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoDestinatario().setDocumento("123");
	}


	public static void popularDadosTransportadora(NotaFiscal notaFiscal2, Distribuidor distribuidor, FiltroViewNotaFiscalDTO filtro) {
		
		if(notaFiscal2.getNotaFiscalInformacoes().getInformacaoTransporte() == null) {
			notaFiscal2.getNotaFiscalInformacoes().setInformacaoTransporte(new InformacaoTransporte());
		}
		
		notaFiscal2.getNotaFiscalInformacoes().getInformacaoTransporte().setModalidadeFrente(1);
	}
}