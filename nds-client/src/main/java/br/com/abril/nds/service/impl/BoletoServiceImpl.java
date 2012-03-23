package br.com.abril.nds.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.dto.PagamentoDTO;
import br.com.abril.nds.dto.ResumoBaixaBoletosDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.financeiro.BaixaAutomatica;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.ControleBaixaBancaria;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.StatusBaixa;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BaixaAutomaticaRepository;
import br.com.abril.nds.repository.BoletoRepository;
import br.com.abril.nds.repository.ControleBaixaBancariaRepository;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.ControleBaixaBancariaService;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.GeradorBoleto;
import br.com.abril.nds.util.TipoMensagem;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Boleto}
 * 
 * @author Discover Technology
 */
@Service
public class BoletoServiceImpl implements BoletoService {

	@Autowired
	private BoletoRepository boletoRepository;
	
	@Autowired
	private PoliticaCobrancaRepository politicaCobrancaRepository;
	
	@Autowired
	private BaixaAutomaticaRepository baixaAutomaticaRepository;
	
	@Autowired
	private ControleBaixaBancariaRepository controleBaixaRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ControleBaixaBancariaService controleBaixaService;
	
	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
	
	@Override
	@Transactional(readOnly=true)
	public List<Boleto> obterBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro) {
		return this.boletoRepository.obterBoletosPorCota(filtro);
	}
	
	@Override
	@Transactional(readOnly=true)
	public long obterQuantidadeBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro) {
		return this.boletoRepository.obterQuantidadeBoletosPorCota(filtro);
	}
	
	@Override
	@Transactional
	public ResumoBaixaBoletosDTO baixarBoletos(ArquivoPagamentoBancoDTO arquivoPagamento,
							  				   BigDecimal valorFinanceiro, Usuario usuario) {
		
		Date dataOperacao = obterDataOperacao();
		
		ControleBaixaBancaria controleBaixa =
				controleBaixaRepository.obterPorData(dataOperacao);
		
		if (controleBaixa != null
			&& controleBaixa.getStatus().equals(StatusControle.CONCLUIDO_SUCESSO)) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, 
				"Já foi realizada baixa automática na data de operação atual!");
		}
		
		if (valorFinanceiro == null
				|| !valorFinanceiro.equals(arquivoPagamento.getSomaPagamentos())) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, 
				"Valor financeiro inválido! A soma dos valores dos boletos pagos " +
				"deve ser igual ao valor informado!");
		}
		
		PoliticaCobranca politicaCobranca =
			politicaCobrancaRepository.obterPorTipoCobranca(TipoCobranca.BOLETO);
		
		controleBaixaService.alterarControleBaixa(StatusControle.INICIADO,
												  dataOperacao, usuario);
		
		ResumoBaixaBoletosDTO resumoBaixaBoletos = new ResumoBaixaBoletosDTO();
		
		try {
			if (arquivoPagamento != null && arquivoPagamento.getListaPagemento() != null) {
				
				for (PagamentoDTO pagamento : arquivoPagamento.getListaPagemento()) {
				
					baixarBoleto(resumoBaixaBoletos, pagamento, dataOperacao, usuario,
								 arquivoPagamento.getNomeArquivo(), politicaCobranca);
				}
				
				controleBaixaService.alterarControleBaixa(StatusControle.CONCLUIDO_SUCESSO,
						  								  dataOperacao, usuario);
				
			} else {
				
				controleBaixaService.alterarControleBaixa(StatusControle.CONCLUIDO_ERROS,
						  								  dataOperacao, usuario);
			}
			
			resumoBaixaBoletos.setNomeArquivo(arquivoPagamento.getNomeArquivo());
			resumoBaixaBoletos.setDataCompetencia(DateUtil.formatarDataPTBR(dataOperacao));
			resumoBaixaBoletos.setSomaPagamentos(arquivoPagamento.getSomaPagamentos());
			
			return resumoBaixaBoletos;
			
		} catch (Exception e) {
			
			controleBaixaService.alterarControleBaixa(StatusControle.CONCLUIDO_ERROS,
					  								  dataOperacao, usuario);
			
			if (e instanceof ValidacaoException) {
				
				throw new ValidacaoException(((ValidacaoException) e).getValidacao());
			
			} else {
			
				throw new ValidacaoException(TipoMensagem.ERROR, 
											 "Falha ao processar a baixa automática: " + e.getMessage());
			}
		}
	}
	
	@Override
	@Transactional
	public void baixarBoleto(ResumoBaixaBoletosDTO resumoBaixaBoletos, PagamentoDTO pagamento,
							 Date dataOperacao, Usuario usuario,
							 String nomeArquivo, PoliticaCobranca politicaCobranca) {
		
		incrementarBoletosLidos(resumoBaixaBoletos);
		
		validarDadosEntrada(pagamento);
		
		Boleto boleto = boletoRepository.obterPorNossoNumero(pagamento.getNossoNumero());
		
		if (boleto == null) {
		
			//Gera baixa com status de pago, porém o nosso número 
			//referente ao pagamento não existe na base
			gerarBaixaAutomatica(StatusBaixa.PAGO_BOLETO_NAO_ENCONTRADO, null,
								 dataOperacao, nomeArquivo,
								 pagamento.getNumeroRegistro(),
								 pagamento.getValorPagamento());

			incrementarBoletosBaixados(resumoBaixaBoletos);
			
			return;
			
		} else {
			
			//Não paga o boleto o gera baixa com status de boleto pago anteriormente
			if (boleto.getStatusCobranca().equals(StatusCobranca.PAGO)) {
				
				gerarBaixaAutomatica(StatusBaixa.NAO_PAGO_BAIXA_JA_REALIZADA, null,
									 dataOperacao, nomeArquivo,
									 pagamento.getNumeroRegistro(),
									 pagamento.getValorPagamento());
				
				incrementarBoletosRejeitados(resumoBaixaBoletos);
				
				movimentoFinanceiroCotaService
					.gerarMovimentoFinanceiroDebitoCredito(boleto.getCota(),
													   	   GrupoMovimentoFinaceiro.CREDITO,
													   	   usuario, pagamento.getValorPagamento(),
													   	   dataOperacao);
				
				return;
			}
			
			if (boleto.getDataVencimento().compareTo(pagamento.getDataPagamento()) < 0) {
				
				//Não paga o boleto o gera baixa com status de não pago por divergência de data
				if (politicaCobranca == null || !politicaCobranca.isAceitaPagamentoDivergente()) {
					
					gerarBaixaAutomatica(StatusBaixa.NAO_PAGO_DIVERGENCIA_DATA, null,
										 dataOperacao, nomeArquivo,
										 pagamento.getNumeroRegistro(),
										 pagamento.getValorPagamento());

					incrementarBoletosRejeitados(resumoBaixaBoletos);
					
					movimentoFinanceiroCotaService
						.gerarMovimentoFinanceiroDebitoCredito(boleto.getCota(),
															   GrupoMovimentoFinaceiro.CREDITO,
														   	   usuario, pagamento.getValorPagamento(),
														   	   dataOperacao);
					
					return;
					
				} else {
					
					//Paga o boleto o gera baixa com status de pago com divergência de data
					gerarBaixaAutomatica(StatusBaixa.PAGO_DIVERGENCIA_DATA, null,
										 dataOperacao, nomeArquivo,
										 pagamento.getNumeroRegistro(),
										 pagamento.getValorPagamento());
			
					efetivarBaixaCobranca(boleto, dataOperacao);
					
					incrementarBoletosBaixadosComDivergencia(resumoBaixaBoletos);
					
					//TODO: verificar valor
					
					return;
				}				
			} else {
				
				if (pagamento.getValorPagamento().compareTo(boleto.getValor()) == 0) {
					
					//Paga o boleto o gera baixa com status de pago
					gerarBaixaAutomatica(StatusBaixa.PAGO, boleto,
							 			 dataOperacao, nomeArquivo,
										 pagamento.getNumeroRegistro(),
										 pagamento.getValorPagamento());
					
					efetivarBaixaCobranca(boleto, dataOperacao);
					
					incrementarBoletosBaixados(resumoBaixaBoletos);
					
					return;
					
				} else {
					
					//Não paga o boleto e gera baixa com status de não pago por divergência de valor
					if (politicaCobranca == null || !politicaCobranca.isAceitaPagamentoDivergente()) {
						
						gerarBaixaAutomatica(StatusBaixa.NAO_PAGO_DIVERGENCIA_VALOR, null,
								 			 dataOperacao, nomeArquivo,
											 pagamento.getNumeroRegistro(),
											 pagamento.getValorPagamento());
						
						incrementarBoletosRejeitados(resumoBaixaBoletos);
						
						movimentoFinanceiroCotaService
							.gerarMovimentoFinanceiroDebitoCredito(boleto.getCota(),
																   GrupoMovimentoFinaceiro.CREDITO,
															   	   usuario, pagamento.getValorPagamento(),
															   	   dataOperacao);
						
						return;
						
					} else {
					
						//Paga o boleto o gera baixa com status de pago com divergência de valor
						gerarBaixaAutomatica(StatusBaixa.PAGO_DIVERGENCIA_VALOR, boleto,
					 			 			 dataOperacao, nomeArquivo,
											 pagamento.getNumeroRegistro(),
											 pagamento.getValorPagamento());
						
						efetivarBaixaCobranca(boleto, dataOperacao);
						
						incrementarBoletosBaixadosComDivergencia(resumoBaixaBoletos);
						
						BigDecimal valor = pagamento.getValorPagamento();
						GrupoMovimentoFinaceiro grupoMovimento = null;
						
						if (boleto.getValor().compareTo(pagamento.getValorPagamento()) == 1) {
							
							valor = boleto.getValor().subtract(pagamento.getValorPagamento());
							
							grupoMovimento = GrupoMovimentoFinaceiro.DEBITO;
							
						} else {
							
							valor = pagamento.getValorPagamento().subtract(boleto.getValor());
							
							grupoMovimento = GrupoMovimentoFinaceiro.CREDITO;
						}
						
						movimentoFinanceiroCotaService
							.gerarMovimentoFinanceiroDebitoCredito(boleto.getCota(), grupoMovimento,
																   usuario, valor, dataOperacao);
						
						return;
					}
				}				
			}
		}
	}
	
	private void validarDadosEntrada(PagamentoDTO pagamento) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		if (pagamento.getDataPagamento() == null) {
			
			listaMensagens.add("Data de pagmento é obrigatória!");
		}
		
		if (pagamento.getNossoNumero() == null) {

			listaMensagens.add("Nosso número é obrigatória!");
		}
		
		if (pagamento.getNumeroRegistro() == null) {

			listaMensagens.add("Número de registro do arquivo é obrigatório!");
		}
		
		if (pagamento.getValorPagamento() == null) {

			listaMensagens.add("Valor do pagmento é obrigatório!");
		}
		
		if (!listaMensagens.isEmpty()) {
			
			ValidacaoVO validacao = new ValidacaoVO();
			
			validacao.setTipoMensagem(TipoMensagem.ERROR);
			validacao.setListaMensagens(listaMensagens);
			
			throw new ValidacaoException(validacao);
		}
	}

	private void gerarBaixaAutomatica(StatusBaixa statusBaixa, Boleto boleto,
									  Date dataBaixa, String nomeArquivo,
									  Integer numeroLinhaArquivo, BigDecimal valoPago) {
		
		BaixaAutomatica baixaAutomatica = new BaixaAutomatica();
		
		baixaAutomatica.setDataBaixa(dataBaixa);
		baixaAutomatica.setNomeArquivo(nomeArquivo);
		baixaAutomatica.setStatus(statusBaixa);
		baixaAutomatica.setNumeroRegistroArquivo(numeroLinhaArquivo);
		baixaAutomatica.setValorPago(valoPago);
		baixaAutomatica.setBoleto(boleto);
		
		baixaAutomaticaRepository.adicionar(baixaAutomatica);
	}
	
	private void efetivarBaixaCobranca(Boleto boleto, Date dataOperacao) {
		
		boleto.setDataPagamento(dataOperacao);
		boleto.setStatusCobranca(StatusCobranca.PAGO);
		boleto.getDivida().setStatus(StatusDivida.QUITADA);
		
		boletoRepository.alterar(boleto);
	}
	
	private void incrementarBoletosLidos(ResumoBaixaBoletosDTO resumoBaixaBoletos) {
		
		resumoBaixaBoletos.setQuantidadeLidos(
				resumoBaixaBoletos.getQuantidadeLidos() + 1);
	}
	
	private void incrementarBoletosBaixados(ResumoBaixaBoletosDTO resumoBaixaBoletos) {
		
		resumoBaixaBoletos.setQuantidadeBaixados(
				resumoBaixaBoletos.getQuantidadeBaixados() + 1);
	}
	
	private void incrementarBoletosRejeitados(ResumoBaixaBoletosDTO resumoBaixaBoletos) {
		
		resumoBaixaBoletos.setQuantidadeRejeitados(
				resumoBaixaBoletos.getQuantidadeRejeitados() + 1);
	}

	private void incrementarBoletosBaixadosComDivergencia(
												ResumoBaixaBoletosDTO resumoBaixaBoletos) {
	
		resumoBaixaBoletos.setQuantidadeBaixadosComDivergencia(
				resumoBaixaBoletos.getQuantidadeBaixadosComDivergencia() + 1);
	}
	
	private Date obterDataOperacao() {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		return distribuidor.getDataOperacao();
	}

    @Override
	@Transactional(readOnly=true)
	public GeradorBoleto geraBoleto(String nossoNumero){
    	
		Boleto boleto = boletoRepository.obterPorNossoNumero(nossoNumero);		
		Pessoa pessoa = boleto.getCota().getPessoa();

		Endereco endereco=new Endereco();
		Set<EnderecoCota> enderecosCota = boleto.getCota().getEnderecos();
		for(EnderecoCota enderecoCota : enderecosCota){
			endereco = enderecoCota.getEndereco();
			if (enderecoCota.getTipoEndereco() == TipoEndereco.COBRANCA){
			    break;
			}
		}

		String nomeSacado="";
		String documentoSacado="";
		if (pessoa instanceof PessoaFisica){
			nomeSacado = ((PessoaFisica) pessoa).getNome();
			documentoSacado = ((PessoaFisica) pessoa).getCpf();
		}
		if (pessoa instanceof PessoaJuridica){
			nomeSacado = ((PessoaJuridica) pessoa).getNomeFantasia();
			documentoSacado = ((PessoaJuridica) pessoa).getCnpj();
		}
	    
		GeradorBoleto geradorBoleto = new GeradorBoleto();
		geradorBoleto.setCedenteNome(distribuidorService.obter().getJuridica().getRazaoSocial());         
		geradorBoleto.setCedenteDocumento(distribuidorService.obter().getJuridica().getCnpj());
		geradorBoleto.setSacadoNome(nomeSacado);          
		geradorBoleto.setSacadoDocumento(documentoSacado); 

		if (endereco!=null){
			geradorBoleto.setEnderecoSacadoUf(endereco.getUf());            
			geradorBoleto.setEnderecoSacadoLocalidade(endereco.getCidade());     
			geradorBoleto.setEnderecoSacadoCep(endereco.getCep());         
			geradorBoleto.setEnderecoSacadoBairro(endereco.getBairro()); 
			geradorBoleto.setEnderecoSacadoLogradouro(endereco.getLogradouro()); 
			geradorBoleto.setEnderecoSacadoNumero(Integer.toString(endereco.getNumero())); 
		}
		else{
			geradorBoleto.setEnderecoSacadoUf("SP");
			geradorBoleto.setEnderecoSacadoLocalidade("Endereco nao cadastrado.");
			geradorBoleto.setEnderecoSacadoCep("");
			geradorBoleto.setEnderecoSacadoBairro("");
			geradorBoleto.setEnderecoSacadoLogradouro("");
			geradorBoleto.setEnderecoSacadoNumero("");
		}
                                            
        String nomeBanco="BANCO_BRADESCO";
        String contaNumero=boleto.getBanco().getConta().toString();
        String contaNumeroDocumento="12345678910";
        String contaNossoNumero=boleto.getNossoNumero().toString();
        
        if ("399".equals(boleto.getBanco().getNumeroBanco())){
            nomeBanco="HSBC";
            if (contaNumero.length() > 6){
                contaNumero=contaNumero.substring(1, 6);                   //HSBC = 6 DIGITOS 
            }
            if (contaNumeroDocumento.length() > 6){
                contaNumeroDocumento=contaNumeroDocumento.substring(1, 6); //HSBC = 6 DIGITOS 
            }
            if (contaNossoNumero.length() > 9){
                contaNossoNumero=contaNossoNumero.substring(1, 9);         //HSBC = 9 DIGITOS 
            }    
        }
        
        geradorBoleto.setContaBanco(nomeBanco);                  
        geradorBoleto.setContaCarteira(boleto.getBanco().getCarteira().getCodigo());
        geradorBoleto.setContaAgencia(boleto.getBanco().getAgencia().intValue());    
        geradorBoleto.setContaNumero(Integer.parseInt(contaNumero));   
        geradorBoleto.setTituloNumeroDoDocumento(contaNumeroDocumento);                      
        geradorBoleto.setTituloNossoNumero(contaNossoNumero);                    
        
        //PARAMETROS ?
        geradorBoleto.setTituloDigitoDoNossoNumero("4");  
        geradorBoleto.setTituloTipoDeDocumento("DM_DUPLICATA_MERCANTIL");
        geradorBoleto.setTituloAceite("A");
        
        geradorBoleto.setTituloValor(boleto.getValor());   
        geradorBoleto.setTituloDataDoDocumento(boleto.getDataEmissao());   
        geradorBoleto.setTituloDataDoVencimento(boleto.getDataVencimento());  
        geradorBoleto.setTituloDesconto(BigDecimal.ZERO);
        geradorBoleto.setTituloDeducao(BigDecimal.ZERO);
        geradorBoleto.setTituloMora(BigDecimal.ZERO);
        geradorBoleto.setTituloAcrecimo(BigDecimal.ZERO);
        geradorBoleto.setTituloValorCobrado(BigDecimal.ZERO);

        //PARAMETROS ?
        geradorBoleto.setBoletoLocalPagamento("Local do pagamento.");
        geradorBoleto.setBoletoInstrucaoAoSacado("Instrução so Sacado");
        geradorBoleto.setBoletoInstrucao1(boleto.getBanco().getInstrucoes());
        geradorBoleto.setBoletoInstrucao2("");
        geradorBoleto.setBoletoInstrucao3("");
        geradorBoleto.setBoletoInstrucao4("");
        geradorBoleto.setBoletoInstrucao5("");
        geradorBoleto.setBoletoInstrucao6("");
        geradorBoleto.setBoletoInstrucao7("");
        geradorBoleto.setBoletoInstrucao8("");
        
        return geradorBoleto;
	}
	
	@Override
	@Transactional(readOnly=true)
	public byte[] gerarImpressaoBoleto(String nossoNumero) throws IOException {
		GeradorBoleto boleto = this.geraBoleto(nossoNumero);
		byte[] b = boleto.getBytePdf();
        return b;
	}

	@Override
	@Transactional(readOnly=true)
	public File gerarAnexoBoleto(String nossoNumero) throws IOException {
		GeradorBoleto boleto = this.geraBoleto(nossoNumero);
		File f = boleto.getFilePdf();
        return f;
	}

	@Override
	@Transactional(readOnly=true)
	public String obterEmailCota(String nossoNumero) {
		Boleto boleto = boletoRepository.obterPorNossoNumero(nossoNumero);
		String email=boleto.getCota().getPessoa().getEmail();
		return email;
	}

}
