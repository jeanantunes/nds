package br.com.abril.nds.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.dto.BoletoDTO;
import br.com.abril.nds.dto.PagamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.BaixaAutomatica;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.StatusBaixa;
import br.com.abril.nds.model.financeiro.TipoBaixa;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BaixaAutomaticaRepository;
import br.com.abril.nds.repository.BoletoRepository;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.ControleBaixaBancariaService;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.util.BoletoImpressao;

@Service
public class BoletoServiceImpl implements BoletoService {

	@Autowired
	private BoletoRepository boletoRepository;
	
	@Autowired
	private PoliticaCobrancaRepository politicaCobrancaRepository;
	
	@Autowired
	private BaixaAutomaticaRepository baixaAutomaticaRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ControleBaixaBancariaService controleBaixaService;
	
	@Transactional
	public List<Boleto> obterBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro) {
		return this.boletoRepository.obterBoletosPorCota(filtro);
	}
	
	@Transactional
	public long obterQuantidadeBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro) {
		return this.boletoRepository.obterQuantidadeBoletosPorCota(filtro);
	}
	
	@Override
	@Transactional
	public void baixarBoletos(ArquivoPagamentoBancoDTO arquivoPagamento, Usuario usuario) {
		
		Date dataOperacao = obterDataOperacao();
		
		//TODO: verificar se já foi feita uma baixa automática no dia
		
		PoliticaCobranca politicaCobranca =
			politicaCobrancaRepository.obterPorTipoCobranca(TipoCobranca.BOLETO);
		
		controleBaixaService.alterarControleBaixa(StatusControle.INICIADO,
												  dataOperacao, usuario);
		
		try {
			if (arquivoPagamento != null && arquivoPagamento.getListaPagemento() != null) {
				
				for (PagamentoDTO pagamento : arquivoPagamento.getListaPagemento()) {
				
					baixarBoleto(pagamento, dataOperacao, TipoBaixa.AUTOMATICA,
								 arquivoPagamento.getNomeArquivo(), politicaCobranca);
				}
				
				controleBaixaService.alterarControleBaixa(StatusControle.CONCLUIDO_SUCESSO,
						  								  dataOperacao, usuario);
				
			} else {
				
				controleBaixaService.alterarControleBaixa(StatusControle.CONCLUIDO_ERROS,
						  								  dataOperacao, usuario);
			}
		} catch (Exception e) {
			
			controleBaixaService.alterarControleBaixa(StatusControle.CONCLUIDO_ERROS,
													  dataOperacao, usuario);
		}
	}
	
	@Override
	@Transactional
	public void baixarBoleto(PagamentoDTO pagamento, Date dataOperacao, TipoBaixa tipoBaixa,
							 String nomeArquivo, PoliticaCobranca politicaCobranca) {
		
		Boleto boleto = boletoRepository.obterPorNossoNumero(pagamento.getNossoNumero());
		
		if (boleto != null) {
			
			//Não paga o boleto o gera baixa com status de boleto pago anteriormente
			if (boleto.getStatusCobranca().equals(StatusCobranca.PAGO)) {
				
				gerarBaixaAutomatica(StatusBaixa.PAGO_ANTERIORMENTE, null, tipoBaixa,
									 dataOperacao, nomeArquivo);
				
				return;
			}
			
			//Não paga o boleto o gera baixa com status de boleto fora da data
			if (boleto.getDataVencimento().compareTo(pagamento.getDataPagamento()) > 0) {
				
				gerarBaixaAutomatica(StatusBaixa.NAO_PAGO_DATA, null, tipoBaixa,
									 dataOperacao, nomeArquivo);
				
				return;
			}
			
			if (!pagamento.getValorPagamento().equals(boleto.getValor())) {
				
				//Não paga o boleto o gera baixa com status de não pago por divergência
				if (politicaCobranca == null || !politicaCobranca.isAceitaPagamentoDivergente()) {
					
					gerarBaixaAutomatica(StatusBaixa.NAO_PAGO_DIVERGENCIA, null, tipoBaixa,
							 			 dataOperacao, nomeArquivo);
					
					return;
				} else {
				
					//Paga o boleto o gera baixa com status de pago com divergência
					gerarBaixaAutomatica(StatusBaixa.PAGO_DIVERGENCIA, null, tipoBaixa,
				 			 dataOperacao, nomeArquivo);
				}
			} else {
			
				//Paga o boleto o gera baixa com status de pago
				gerarBaixaAutomatica(StatusBaixa.PAGO, boleto, tipoBaixa,
						 			 dataOperacao, nomeArquivo);
			}
			
			boleto.setDataPagamento(dataOperacao);
			boleto.setStatusCobranca(StatusCobranca.PAGO);
			
			boletoRepository.alterar(boleto);
			
		} else {
			
			gerarBaixaAutomatica(StatusBaixa.PAGO, null, tipoBaixa,
								 dataOperacao, nomeArquivo);
		}
	}
	
	private void gerarBaixaAutomatica(StatusBaixa statusBaixa, Boleto boleto, TipoBaixa tipoBaixa,
									  Date dataBaixa, String nomeArquivo) {
		
		BaixaAutomatica baixaAutomatica = new BaixaAutomatica();
		
		baixaAutomatica.setDataBaixa(dataBaixa);
		baixaAutomatica.setTipoBaixa(tipoBaixa);
		baixaAutomatica.setNomeArquivo(nomeArquivo);
		baixaAutomatica.setStatus(statusBaixa);
		baixaAutomatica.setBoleto(boleto);
		
		baixaAutomaticaRepository.adicionar(baixaAutomatica);
	}
	
	private Date obterDataOperacao() {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		return distribuidor.getDataOperacao();
	}

	@Override
	public byte[] gerarImpressaoBoleto(String nossoNumero) throws IOException {

        BoletoImpressao boleto = new BoletoImpressao();
        BoletoDTO dadosBoleto = boletoRepository.obterDadosBoleto(nossoNumero);

        boleto.setCedenteNome(dadosBoleto.getCedenteNome());
		boleto.setCedenteDocumento(dadosBoleto.getCedenteDocumento());

		boleto.setSacadoNome(dadosBoleto.getSacadoNome());
		boleto.setSacadoDocumento(dadosBoleto.getSacadoDocumento());

        boleto.setEnderecoSacadoUf(dadosBoleto.getEnderecoSacadoUf());//!!
        boleto.setEnderecoSacadoLocalidade(dadosBoleto.getEnderecoSacadoLocalidade());
        boleto.setEnderecoSacadoCep(dadosBoleto.getEnderecoSacadoCep());
        boleto.setEnderecoSacadoBairro(dadosBoleto.getEnderecoSacadoBairro());
        boleto.setEnderecoSacadoLogradouro(dadosBoleto.getEnderecoSacadoLogradouro());
        boleto.setEnderecoSacadoNumero(dadosBoleto.getEnderecoSacadoNumero());
 
        boleto.setSacadorAvalistaNome(dadosBoleto.getSacadorAvalistaNome());
		boleto.setSacadorAvalistaDocumento(dadosBoleto.getSacadorAvalistaDocumento());
		
		boleto.setEnderecoSacadorAvalistaUf(dadosBoleto.getEnderecoSacadorAvalistaUf());//!!
        boleto.setEnderecoSacadorAvalistaLocalidade(dadosBoleto.getEnderecoSacadorAvalistaLocalidade());
        boleto.setEnderecoSacadorAvalistaCep(dadosBoleto.getEnderecoSacadorAvalistaCep());
        boleto.setEnderecoSacadorAvalistaBairro(dadosBoleto.getEnderecoSacadorAvalistaBairro());
        boleto.setEnderecoSacadorAvalistaLogradouro(dadosBoleto.getEnderecoSacadorAvalistaLogradouro());
        boleto.setEnderecoSacadorAvalistaNumero(dadosBoleto.getEnderecoSacadorAvalistaNumero());

        boleto.setContaBanco(dadosBoleto.getContaBanco());
        boleto.setContaNumero(dadosBoleto.getContaNumero());
        boleto.setContaCarteira(dadosBoleto.getContaCarteira());
        boleto.setContaAgencia(dadosBoleto.getContaAgencia());
        
        boleto.setTituloNumeroDoDocumento(dadosBoleto.getTituloNumeroDoDocumento());
        boleto.setTituloNossoNumero(dadosBoleto.getTituloNossoNumero());
        boleto.setTituloDigitoDoNossoNumero(dadosBoleto.getTituloDigitoDoNossoNumero());
        boleto.setTituloValor(dadosBoleto.getTituloValor());
        boleto.setTituloDataDoDocumento(dadosBoleto.getTituloDataDoDocumento());
        boleto.setTituloDataDoVencimento(dadosBoleto.getTituloDataDoVencimento());
        boleto.setTituloTipoDeDocumento(dadosBoleto.getTituloTipoDeDocumento());//!!!
        boleto.setTituloAceite(dadosBoleto.getTituloAceite());//!!!
        boleto.setTituloDesconto(dadosBoleto.getTituloDesconto());
        boleto.setTituloDeducao(dadosBoleto.getTituloDeducao());
        boleto.setTituloMora(dadosBoleto.getTituloMora());
        boleto.setTituloAcrecimo(dadosBoleto.getTituloAcrecimo());
        boleto.setTituloValorCobrado(dadosBoleto.getTituloValorCobrado());

        boleto.setBoletoLocalPagamento(dadosBoleto.getBoletoLocalPagamento());
        boleto.setBoletoInstrucaoAoSacado(dadosBoleto.getBoletoInstrucaoAoSacado());
        boleto.setBoletoInstrucao1(dadosBoleto.getBoletoInstrucao1());
        boleto.setBoletoInstrucao2(dadosBoleto.getBoletoInstrucao2());
        boleto.setBoletoInstrucao3(dadosBoleto.getBoletoInstrucao3());
        boleto.setBoletoInstrucao4(dadosBoleto.getBoletoInstrucao4());
        boleto.setBoletoInstrucao5(dadosBoleto.getBoletoInstrucao5());
        boleto.setBoletoInstrucao6(dadosBoleto.getBoletoInstrucao6());
        boleto.setBoletoInstrucao7(dadosBoleto.getBoletoInstrucao7());
		boleto.setBoletoInstrucao8(dadosBoleto.getBoletoInstrucao8());

		byte[] b = boleto.obterArrayByte();
		
        return b;
	}
	
}
