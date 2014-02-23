package br.com.abril.nds.service.builders;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.abril.nds.dto.filtro.FiltroNFeDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.nfe.NotaFiscalNds;
import br.com.abril.nds.model.fiscal.nota.CNPJEmitente;
import br.com.abril.nds.model.fiscal.nota.Identificacao;
import br.com.abril.nds.model.fiscal.nota.Identificacao.FinalidadeEmissaoNFe;
import br.com.abril.nds.model.fiscal.nota.Identificacao.FormaPagamento;
import br.com.abril.nds.model.fiscal.nota.Identificacao.FormatoImpressao;
import br.com.abril.nds.model.fiscal.nota.Identificacao.LocalDestinoOperacao;
import br.com.abril.nds.model.fiscal.nota.Identificacao.OperacaoConsumidorFinal;
import br.com.abril.nds.model.fiscal.nota.Identificacao.PresencaConsumidor;
import br.com.abril.nds.model.fiscal.nota.Identificacao.ProcessoEmissao;
import br.com.abril.nds.model.fiscal.nota.Identificacao.TipoAmbiente;
import br.com.abril.nds.model.fiscal.nota.Identificacao.TipoEmissao;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente.RegimeTributario;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscalInformacoes;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalEndereco;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalTelefone;

public class NotaFiscalBuilder implements Serializable {
	
	private static final long serialVersionUID = 176874569807919538L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NotaFiscalBuilder.class);
	
	// builder header Nota fiscal
	public static NotaFiscalNds montarHeaderNotaFiscal(NotaFiscalNds notaFiscal, Cota cota){
		
		/** -- Natureza da Operação -- PROT. DE AUTORIZAÇÃO -- CRT(Codigo Regime Tributario)
	     *  -- Inscricao Estadual   -- INSCRIÇÃO ESTADUAL DO SUBSTITUTO TRIBUTÁRIO -- CNPJ/CPF
		 */
		if (cota.getPessoa() instanceof PessoaJuridica) {
			
			PessoaJuridica pessoaJuridica = (PessoaJuridica) cota.getPessoa();
			
			notaFiscal.getEmissor().setNome(pessoaJuridica.getRazaoSocial());
			notaFiscal.getEmissor().setEmail(pessoaJuridica.getEmail()); 
//			notaFiscal.getEmissor().setNomeFantasia(pessoaJuridica.getNomeFantasia() == null ?  cota.getPessoa().getNome() : pessoaJuridica.getNomeFantasia());
//			notaFiscal.getEmissor().setInscricaoEstadual(pessoaJuridica.getInscricaoEstadual());
//			notaFiscal.getEmissor().setCnpj(pessoaJuridica.getCnpj());
			
		} else if (cota.getPessoa() instanceof PessoaFisica) {
			PessoaFisica pessoaFisica = (PessoaFisica) cota.getPessoa();
			pessoaFisica.getCpf();
			pessoaFisica.getRg();
			pessoaFisica.getEmail();
		}
				
		return notaFiscal;
	}

	public TipoOperacao tipoOperacaoNotaFiscal (){
		
		return null;
	}


	public static void popularDadosDistribuidor(NotaFiscal notaFiscal, Distribuidor distribuidor, FiltroNFeDTO filtro) {
		
		if(notaFiscal.getNotaFiscalInformacoes() == null) {
			notaFiscal.setNotaFiscalInformacoes(new NotaFiscalInformacoes());
		}
		
		if(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente() == null) {
			notaFiscal.getNotaFiscalInformacoes().setIdentificacaoEmitente(new IdentificacaoEmitente());
		}
		
		if(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco() == null) {
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setEndereco(new NotaFiscalEndereco());
		}
		
		if(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getTelefone() == null){			
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setTelefone(new NotaFiscalTelefone());
		}
		
		// verificar o telefone do distribuidor
		
		for (TelefoneDistribuidor telefone : distribuidor.getTelefones()) {
			
			if(telefone.getTelefone() != null) {
				notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getTelefone().setNumero(telefone.getTelefone().getNumero());
				notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getTelefone().setDDD(telefone.getTelefone().getDdd());
				
				if(telefone.getTelefone().getRamal() != null) {
					try {
						notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getTelefone().setRamal(telefone.getTelefone().getRamal());
					} catch (Exception e) {
						LOGGER.error(new StringBuilder("Erro ao fazer parse do ramal.").append(telefone.getTelefone().getDdd()).append(" ").append(telefone.getTelefone().getNumero()).toString());
					}
				}
				
				break;
			}
		}
		
		// Dados do Distribuidor
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setNome(distribuidor.getJuridica().getRazaoSocial());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setNomeFantasia(distribuidor.getJuridica().getNomeFantasia());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setInscricaoEstadual(distribuidor.getJuridica().getInscricaoEstadual());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setInscricaoMunicipal(distribuidor.getJuridica().getInscricaoMunicipal());
		
		//FIXME: Obter o valor cnae
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setCnae("1234567");
		
		//FIXME: Obter o valor crt
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setRegimeTributario(RegimeTributario.REGIME_NORMAL);
		
		CNPJEmitente cnpj = new CNPJEmitente();
		cnpj.setDocumento(distribuidor.getJuridica().getCnpj().replaceAll("/", "").replaceAll("\\.", "").replaceAll("-", ""));
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setDocumento(cnpj);
		
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setLogradouro(distribuidor.getEnderecoDistribuidor().getEndereco().getLogradouro());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setNumero(distribuidor.getEnderecoDistribuidor().getEndereco().getNumero());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setBairro(distribuidor.getEnderecoDistribuidor().getEndereco().getBairro());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setCidade(distribuidor.getEnderecoDistribuidor().getEndereco().getCidade());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setUf(distribuidor.getEnderecoDistribuidor().getEndereco().getUf());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setCep(distribuidor.getEnderecoDistribuidor().getEndereco().getCep().replaceAll("-", ""));
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setCodigoPais(1058L);
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setCodigoCidadeIBGE(3550308L);
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setPais("Brasil");
		
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getTelefone();
		try {
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setCodigoCidadeIBGE(3550308L); //distribuidor.getEnderecoDistribuidor().getEndereco().getCodigoCidadeIBGE().longValue());
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setCodigoUf(35L); //Long.parseLong(distribuidor.getEnderecoDistribuidor().getEndereco().getCodigoUf()));
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
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setFinalidadeEmissaoNFe(FinalidadeEmissaoNFe.NORMAL);
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setOperacaoConsumidorFinal(OperacaoConsumidorFinal.NAO);
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setPresencaConsumidor(PresencaConsumidor.NAO_SE_APLICA);
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setVersaoSistemaEmissao("2.2.21");
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setLocalDestinoOperacao(LocalDestinoOperacao.INTERNA);
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setFormatoImpressao(FormatoImpressao.PAISAGEM);
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setModeloDocumentoFiscal("55");
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setProcessoEmissao(ProcessoEmissao.EMISSAO_NFE_APLICATIVO_FORNECIDO_PELO_FISCO);
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setNumeroDocumentoFiscal(Long.parseLong(new Random().nextInt(10000000)+""));
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setDataEmissao(new Date());
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setCodigoUf(35L); //notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().getCodigoUf());
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setCodigoMunicipio(3550308L); //notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().getCodigoCidadeIBGE());
		
		//FIXME: Ajustar o valor do codigoNF
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setCodigoNF(Integer.toString(new Random().nextInt(90000000 - 10000000) + 10000000));
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setDigitoVerificadorChaveAcesso(0L);
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao().setTipoOperacao(TipoOperacao.SAIDA);
		
		notaFiscal2.getNotaFiscalInformacoes().getIdentificacao();
		
	}

	public static void popularDadosTransportadora(NotaFiscal notaFiscal2, Distribuidor distribuidor, FiltroNFeDTO filtro) {
		
		if(notaFiscal2.getNotaFiscalInformacoes().getInformacaoTransporte() == null) {
			notaFiscal2.getNotaFiscalInformacoes().setInformacaoTransporte(new InformacaoTransporte());
		}
		

		if(notaFiscal2.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco() == null) {
			notaFiscal2.getNotaFiscalInformacoes().getInformacaoTransporte().setEndereco(new NotaFiscalEndereco());
		}
		
		notaFiscal2.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setBairro("Osasco");
		notaFiscal2.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setCep("08250000");
		notaFiscal2.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setNumero("158");
		notaFiscal2.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setCodigoCidadeIBGE(3550308L);
		notaFiscal2.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setCidade("Sãp Paulo");
		notaFiscal2.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setCodigoPais(0L);
		notaFiscal2.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setCodigoUf(0L);
		notaFiscal2.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setComplemento("XXXX");
		notaFiscal2.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setPais("Brasil");
		notaFiscal2.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setTipoLogradouro("Rua");
		notaFiscal2.getNotaFiscalInformacoes().getInformacaoTransporte().getEndereco().setUf("SP");
		notaFiscal2.getNotaFiscalInformacoes().getInformacaoTransporte().setModalidadeFrente(1);

		
	}
}