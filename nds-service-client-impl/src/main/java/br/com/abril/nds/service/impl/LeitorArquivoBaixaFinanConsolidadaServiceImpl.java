package br.com.abril.nds.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import br.com.abril.nds.dto.BaixaBancariaConsolidadaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.service.LeitorArquivoBaixaFinanConsolidadaService;
import br.com.abril.nds.util.BaixaBancConsolidada;
import br.com.abril.nds.util.DateUtil;

@Service
public class LeitorArquivoBaixaFinanConsolidadaServiceImpl implements LeitorArquivoBaixaFinanConsolidadaService {
	
	
	private static final String[] EXTENSOES_ARQUIVO_VALIDAS = {"txt"};
	private static final String idLinhaDetalhe = "1";
	
	@Override
	public List<BaixaBancariaConsolidadaDTO> obterPagamentosParaBaixa(File arquivo, String nomeArquivo){
		
		this.validarDadosEntrada(arquivo, nomeArquivo);
		
		List<BaixaBancariaConsolidadaDTO> baixas = new ArrayList<>();
		
		List<String> lines = null;
		
		try {
			
			lines = FileUtils.readLines(arquivo, "UTF8");
			
		} catch (IOException e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Falha ao processar o arquivo!");
		}
		
		baixas = this.processarLinhasArquivo(lines, nomeArquivo);
		
		
		return baixas;
	}
	
	
	protected List<BaixaBancariaConsolidadaDTO> processarLinhasArquivo(List<String> linhas, String nomeArquivo){
		
		List<BaixaBancariaConsolidadaDTO> baixas = new ArrayList<>();
		
		BaixaBancConsolidada baixaConsolidada = BaixaBancConsolidada.obterBaixaBancariaConsolidada();
		
		String idLinha;
		
		for (String linha : linhas) {
			idLinha = linha.substring(0, 1);
			BaixaBancariaConsolidadaDTO baixa = new BaixaBancariaConsolidadaDTO();
			
			if(idLinha.equalsIgnoreCase(idLinhaDetalhe)){
				baixa.setCodJornaleiro(Integer.parseInt(baixaConsolidada.obterCodJornaleiro(linha)));
				baixa.setDataVencimento(DateUtil.parseDataPTBR(baixaConsolidada.obterDataVencimentoBoleto(linha)));
				baixa.setDataPagamento(DateUtil.parseDataPTBR(baixaConsolidada.obterDataPagamentoBoleto(linha)));
				baixa.setValorDoBoleto(BigDecimal.valueOf(Double.parseDouble(baixaConsolidada.obterValorBoleto(linha))/100.0));
				baixa.setValorPago(BigDecimal.valueOf(Double.parseDouble(baixaConsolidada.obterValorPago(linha))/100.0));
				baixa.setNossoNumeroConsolidado(baixaConsolidada.obterNossoNumeroConsolidado(linha));

				baixas.add(baixa);
			}
			
		}

		return baixas;
	}
	
	/*
	
	protected ArquivoPagamentoBancoDTO lerLinhasCNAB(PadraoCNAB padraoCNAB, List<String> lines, String nomeArquivo) {
		
		String primeiraLinha = lines.get(0);
	
		String codigoBanco = padraoCNAB.obterCodigoBanco(primeiraLinha);
		
		CNAB bancoCNAB = CNAB.obterCNAB(padraoCNAB, codigoBanco);
		
		String strNumeroAgencia = null;
		String strNumeroConta = null;
		String strNumeroRegistro = null;
		String strDataPagamento = null;
		String strNossoNumero = null;
        BigDecimal somaPagamentos = BigDecimal.ZERO;
		BigDecimal valorPagamento = BigDecimal.ZERO;
		
		ArquivoPagamentoBancoDTO arquivoPagamentoBanco = new ArquivoPagamentoBancoDTO();
		arquivoPagamentoBanco.setCodigoBanco(codigoBanco);
		
		Map<Long, PagamentoDTO> mapaPagamento = new HashMap<Long, PagamentoDTO>();
		
		PagamentoDTO pagamento = null;
		
		for(String line: lines) {
			
			if (padraoCNAB.isHeader(line)) {
				
				if ( !UtilitarioCNAB.BANCO_BRADESCO.equals(codigoBanco)  &&
					 !UtilitarioCNAB.BANCO_CAIXA_ECONOMICA_FEDERAL.equals(codigoBanco) ){
					
					strNumeroAgencia = bancoCNAB.obterNumeroAgencia(line);
					strNumeroConta =   bancoCNAB.obterNumeroConta(line);
					arquivoPagamentoBanco.setNumeroAgencia(this.parseLong(strNumeroAgencia));
				    arquivoPagamentoBanco.setNumeroConta(this.parseLong(strNumeroConta));
				    
				}
			    
			} else if (padraoCNAB.isDetalhe(line)) {
				
				if ( UtilitarioCNAB.BANCO_BRADESCO.equals(codigoBanco) ||
					 UtilitarioCNAB.BANCO_CAIXA_ECONOMICA_FEDERAL.equals(codigoBanco)	){
					
					strNumeroAgencia 	= bancoCNAB.obterNumeroAgencia(line);
					strNumeroConta 		= bancoCNAB.obterNumeroConta(line);
					arquivoPagamentoBanco.setNumeroAgencia(this.parseLong(strNumeroAgencia));
					arquivoPagamentoBanco.setNumeroConta(this.parseLong(strNumeroConta));
					
				}	           
						                
				strNumeroRegistro 	= bancoCNAB.obterNumeroRegistro(line);
				
				if(mapaPagamento.containsKey(this.parseLong(strNumeroRegistro))) {
					pagamento = mapaPagamento.get(this.parseLong(strNumeroRegistro));
				} else {
					pagamento = new PagamentoDTO();
					pagamento.setNumeroRegistro(this.parseLong(strNumeroRegistro));
					mapaPagamento.put(this.parseLong(strNumeroRegistro), pagamento);
				}
				
				if(bancoCNAB.containsDataPagamento(line)) {
					strDataPagamento 	= bancoCNAB.obterDataPagamento(line);
					pagamento.setDataPagamento(DateUtil.parseData(strDataPagamento, padraoCNAB.getFormatoDataArquivoCNAB()));
				}
				
				if(bancoCNAB.containsNossoNumero(line)) {
					strNossoNumero 		= bancoCNAB.obterNossoNumero(line);
					pagamento.setNossoNumero(strNossoNumero);
				}
				
				if(bancoCNAB.containsValorPagamento(line)) {
					valorPagamento 		= this.parseBigDecimal(bancoCNAB.obterValorPagamento(line));
					pagamento.setValorPagamento(valorPagamento);
				}
				
			}
			
		}
		
		List<PagamentoDTO> listaPagamento = new ArrayList(mapaPagamento.values());
		
		for(PagamentoDTO p : listaPagamento) {
			validarLeituraLinha(p);
			somaPagamentos = somaPagamentos.add(p.getValorPagamento());
		}
		
		arquivoPagamentoBanco.setListaPagemento(listaPagamento);
		arquivoPagamentoBanco.setSomaPagamentos(somaPagamentos);
		arquivoPagamentoBanco.setNomeArquivo(nomeArquivo);

		return arquivoPagamentoBanco;
	}
	*/
	
	private void validarDadosEntrada(File file, String nomeArquivo) {

		if (nomeArquivo == null || nomeArquivo.trim().length() == 0) {

			throw new ValidacaoException(TipoMensagem.WARNING,"Nome do arquivo é obrigatório!");
		}

		if (nomeArquivo.trim().length() > 255) {

			throw new ValidacaoException(TipoMensagem.WARNING,"O nome do arquivo deve possuir até 255 caracteres!");
		}

		if (!isExtensaoArquivoValida(nomeArquivo)) {

			throw new ValidacaoException(TipoMensagem.WARNING,"Extensão do arquivo inválida!");
		}

		if (file == null || !file.isFile()) {

			throw new ValidacaoException(TipoMensagem.WARNING,"Arquivo inválido!");
		}
	}
	
	private boolean isExtensaoArquivoValida(String nomeArquivo) {

		int index = nomeArquivo.lastIndexOf(".");

		String extensao = nomeArquivo.substring(index + 1, nomeArquivo.length());

		for (String extensaoAceita : EXTENSOES_ARQUIVO_VALIDAS) {

			if (extensaoAceita.equalsIgnoreCase(extensao)) {
				return true;
			}
		}

		return false;
	}
	
}
