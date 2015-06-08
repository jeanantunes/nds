package br.com.abril.nds.service.builders;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
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
import br.com.abril.nds.model.fiscal.nota.TransportadorWrapper;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalEndereco;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalTelefone;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.repository.ParametroSistemaRepository;

@Component
public class NotaFiscalBuilder implements Serializable {
	
	private static final long serialVersionUID = 176874569807919538L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NotaFiscalBuilder.class);
	
	@Autowired
	private static ParametroSistemaRepository parametroSistemaRepository;
	
	public static void popularDadosEmissor(NotaFiscal notaFiscal, Distribuidor distribuidor) {
		
		if(notaFiscal.getNotaFiscalInformacoes() == null) {
			notaFiscal.setNotaFiscalInformacoes(new NotaFiscalInformacoes());
		}
		
		if(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente() == null) {
			notaFiscal.getNotaFiscalInformacoes().setIdentificacaoEmitente(new IdentificacaoEmitente());
		}
		
		if(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco() == null) {
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setEndereco(new NotaFiscalEndereco());
		}
		
		// verificar o telefone do distribuidor
		
		for (TelefoneDistribuidor telefone : distribuidor.getTelefones()) {
			
			if(telefone.getTelefone() != null) {
				
				NotaFiscalTelefone tel = null;
				if(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getTelefone() == null){			
					tel = new NotaFiscalTelefone();
				}
				
				tel.setNumero(telefone.getTelefone().getNumero());
				tel.setDDD(telefone.getTelefone().getDdd());
				
				notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setTelefone(tel);
				
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
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setNome(distribuidor.getJuridica().getRazaoSocial().trim());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setNomeFantasia(distribuidor.getJuridica().getNomeFantasia());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setInscricaoEstadual(distribuidor.getJuridica().getInscricaoEstadual().trim());
		// notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setInscricaoEstadualSubstituto(distribuidor.getJuridica().getInscricaoMunicipal());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setInscricaoMunicipal(distribuidor.getJuridica().getInscricaoMunicipal() != null ? distribuidor.getJuridica().getInscricaoMunicipal() : "0");
		
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setCnae(distribuidor.getCnae());
		
		//FIXME: Obter o valor crt
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setRegimeTributario(RegimeTributario.REGIME_NORMAL);
		
		CNPJEmitente cnpj = new CNPJEmitente();
		cnpj.setDocumento(distribuidor.getJuridica().getCnpj().replaceAll("/", "").replaceAll("\\.", "").replaceAll("-", ""));
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setDocumento(cnpj);
		
		StringBuilder logradouro = new StringBuilder(distribuidor.getEnderecoDistribuidor().getEndereco().getTipoLogradouro())
			.append(" ")
			.append(distribuidor.getEnderecoDistribuidor().getEndereco().getLogradouro());
		
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setLogradouro(logradouro.toString());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setNumero(distribuidor.getEnderecoDistribuidor().getEndereco().getNumero());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setBairro(distribuidor.getEnderecoDistribuidor().getEndereco().getBairro());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setCidade(distribuidor.getEnderecoDistribuidor().getEndereco().getCidade());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setUf(distribuidor.getEnderecoDistribuidor().getEndereco().getUf());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setCep(distribuidor.getEnderecoDistribuidor().getEndereco().getCep().replaceAll("-", ""));
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setCodigoPais(1058);
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setCodigoCidadeIBGE(distribuidor.getEnderecoDistribuidor().getEndereco().getCodigoCidadeIBGE());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setCodigoUf(distribuidor.getEnderecoDistribuidor().getEndereco().getCodigoUf());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setPais("Brasil");
		
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getTelefone();
	}

	public static void popularDadosDistribuidor(NotaFiscal notaFiscal, Distribuidor distribuidor) {
		
		if(notaFiscal.getNotaFiscalInformacoes() == null) {
			notaFiscal.setNotaFiscalInformacoes(new NotaFiscalInformacoes());
		}
		
		if(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente() == null) {
			notaFiscal.getNotaFiscalInformacoes().setIdentificacaoEmitente(new IdentificacaoEmitente());
		}
		
		if(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco() == null) {
			notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setEndereco(new NotaFiscalEndereco());
		}
		
		// verificar o telefone do distribuidor
		
		for (TelefoneDistribuidor telefone : distribuidor.getTelefones()) {
			
			if(telefone.getTelefone() != null) {
				
				NotaFiscalTelefone tel = null;
				if(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getTelefone() == null){			
					tel = new NotaFiscalTelefone();
				}
				
				tel.setNumero(telefone.getTelefone().getNumero());
				tel.setDDD(telefone.getTelefone().getDdd());
				
				notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setTelefone(tel);
				
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
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setNome(distribuidor.getJuridica().getRazaoSocial().trim());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setNomeFantasia(distribuidor.getJuridica().getNomeFantasia());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setInscricaoEstadual(distribuidor.getJuridica().getInscricaoEstadual());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setInscricaoEstadualSubstituto(distribuidor.getJuridica().getInscricaoMunicipal());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setInscricaoMunicipal(distribuidor.getJuridica().getInscricaoMunicipal() != null ? distribuidor.getJuridica().getInscricaoMunicipal() : "0");
		
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setCnae(distribuidor.getCnae());
		
		//FIXME: Obter o valor crt
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setRegimeTributario(RegimeTributario.REGIME_NORMAL);
		
		CNPJEmitente cnpj = new CNPJEmitente();
		cnpj.setDocumento(distribuidor.getJuridica().getCnpj().replaceAll("/", "").replaceAll("\\.", "").replaceAll("-", ""));
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().setDocumento(cnpj);
		
		StringBuilder logradouro = new StringBuilder(distribuidor.getEnderecoDistribuidor().getEndereco().getTipoLogradouro())
			.append(" ")
			.append(distribuidor.getEnderecoDistribuidor().getEndereco().getLogradouro());
		
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setLogradouro(logradouro.toString());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setNumero(distribuidor.getEnderecoDistribuidor().getEndereco().getNumero());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setBairro(distribuidor.getEnderecoDistribuidor().getEndereco().getBairro());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setCidade(distribuidor.getEnderecoDistribuidor().getEndereco().getCidade());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setUf(distribuidor.getEnderecoDistribuidor().getEndereco().getUf());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setCep(distribuidor.getEnderecoDistribuidor().getEndereco().getCep().replaceAll("-", ""));
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setCodigoCidadeIBGE(distribuidor.getEnderecoDistribuidor().getEndereco().getCodigoCidadeIBGE());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setCodigoUf(distribuidor.getEnderecoDistribuidor().getEndereco().getCodigoUf());
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setCodigoPais(1058);
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().setPais("Brasil");
		
		notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getTelefone();
		
	}

	public static void montarHeaderNotaFiscal(NotaFiscal notaFiscal, Map<String, ParametroSistema> parametrosSistema) {
		
		//FIXME: Obter forma de pagamento da cota
		FormaPagamento formaPagamento = FormaPagamento.OUTROS;
		
		if(notaFiscal.getNotaFiscalInformacoes().getIdentificacao() == null) {
			notaFiscal.getNotaFiscalInformacoes().setIdentificacao(new Identificacao());
		}
		
		if(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario() == null) {
			notaFiscal.getNotaFiscalInformacoes().setIdentificacaoDestinatario(new IdentificacaoDestinatario());
		}

		//FIXME: Ajustar para obter dos parametros enviado pela tela
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setFormaPagamento(formaPagamento);
		
		//FIXME: Ajustar para variavel global
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setTipoAmbiente(TipoAmbiente.valueOf(parametrosSistema.get("NFE_INFORMACOES_AMBIENTE").getValor()));
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setTipoEmissao(TipoEmissao.NORMAL);
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setFinalidadeEmissaoNFe(FinalidadeEmissaoNFe.NORMAL);
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setOperacaoConsumidorFinal(OperacaoConsumidorFinal.NAO);
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setPresencaConsumidor(PresencaConsumidor.NAO_SE_APLICA);
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setVersaoSistemaEmissao(parametrosSistema.get("NFE_INFORMACOES_VERSAO_EMISSOR").getValor());
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setLocalDestinoOperacao(LocalDestinoOperacao.INTERNA);
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setFormatoImpressao(FormatoImpressao.valueOf(parametrosSistema.get("NFE_INFORMACOES_FORMATO_IMPRESSAO").getValor()));
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setModeloDocumentoFiscal(parametrosSistema.get("NFE_INFORMACOES_MODELO_DOCUMENTO").getValor());
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setProcessoEmissao(ProcessoEmissao.valueOf(parametrosSistema.get("NFE_INFORMACOES_TIPO_EMISSOR").getValor()));
		
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setDataEmissao(new Date());
		
		//FIXME: Ajustar codigo de UF do cadastro
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setCodigoUf(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().getCodigoUf());
		
		//FIXME: Ajustar codigo de municipio do cadastro
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setCodigoMunicipio(Long.valueOf(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().getCodigoCidadeIBGE()));
		
	}

	public static void montarHeaderNotaFiscal(NotaFiscal notaFiscal, Distribuidor distribuidor, Map<String, ParametroSistema> parametrosSistema) {
		
		//FIXME: Obter forma de pagamento da cota
		FormaPagamento formaPagamento = FormaPagamento.OUTROS;
		
		if(notaFiscal.getNotaFiscalInformacoes().getIdentificacao() == null) {
			notaFiscal.getNotaFiscalInformacoes().setIdentificacao(new Identificacao());
		}
		
		if(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario() == null) {
			notaFiscal.getNotaFiscalInformacoes().setIdentificacaoDestinatario(new IdentificacaoDestinatario());
		}
		
		//FIXME: Ajustar para obter dos parametros enviado pela tela
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setFormaPagamento(formaPagamento);
		
		//FIXME: Ajustar para variavel global
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setTipoAmbiente(TipoAmbiente.valueOf(parametrosSistema.get("NFE_INFORMACOES_AMBIENTE").getValor()));
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setTipoEmissao(TipoEmissao.NORMAL);
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setFinalidadeEmissaoNFe(FinalidadeEmissaoNFe.NORMAL);
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setOperacaoConsumidorFinal(OperacaoConsumidorFinal.NAO);
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setPresencaConsumidor(PresencaConsumidor.NAO_SE_APLICA);
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setVersaoSistemaEmissao(parametrosSistema.get("NFE_INFORMACOES_VERSAO_EMISSOR").getValor());
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setLocalDestinoOperacao(LocalDestinoOperacao.INTERNA);
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setFormatoImpressao(FormatoImpressao.valueOf(parametrosSistema.get("NFE_INFORMACOES_FORMATO_IMPRESSAO").getValor()));
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setModeloDocumentoFiscal(parametrosSistema.get("NFE_INFORMACOES_MODELO_DOCUMENTO").getValor());
		
		//FIXME: Ajustar para variavel parametrizada
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setProcessoEmissao(ProcessoEmissao.valueOf(parametrosSistema.get("NFE_INFORMACOES_TIPO_EMISSOR").getValor()));
		
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setDataEmissao(new Date());
		
		//FIXME: Ajustar codigo de UF do cadastro
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setCodigoUf(distribuidor.getEnderecoDistribuidor().getEndereco().getCodigoUf()); //notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().getCodigoUf());
		
		//FIXME: Ajustar codigo de municipio do cadastro
		if(distribuidor.getEnderecoDistribuidor().isPrincipal()) {
			notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setCodigoMunicipio(0L); //notaFiscal2.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().getCodigoCidadeIBGE());
		}
		
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setDigitoVerificadorChaveAcesso(Long.valueOf(notaFiscal.getNotaFiscalInformacoes().getIdNFe().substring(46, 47)));
		
	}
	
	public static void popularDadosTransportadora(NotaFiscal notaFiscal, Distribuidor distribuidor) {
		
		if(notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte() == null) {
			notaFiscal.getNotaFiscalInformacoes().setInformacaoTransporte(new InformacaoTransporte());
		}
		
		if(notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper() == null) {
			notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().setTransportadorWrapper(new TransportadorWrapper());
		}

		if(notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco() == null) {
			notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().setEndereco(new NotaFiscalEndereco());
		}
		
		notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco().setBairro("Osasco");
		notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco().setLogradouro("Kenkiti Shinomoto");
		notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco().setCep("08250000");
		notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco().setNumero("1678");
		notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco().setCodigoCidadeIBGE(3550308);
		notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco().setCidade("Sao Paulo");
		notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco().setCodigoPais(30);
		notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco().setCodigoUf(20);
		notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco().setComplemento("XXXX");
		notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco().setPais("Brasil");
		notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco().setTipoLogradouro("Rua");
		notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().getTransportadorWrapper().getEndereco().setUf("SP");
		notaFiscal.getNotaFiscalInformacoes().getInformacaoTransporte().setModalidadeFrete(1);
	}
}