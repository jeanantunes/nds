package br.com.abril.nds.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.dto.PagamentoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.service.LeitorArquivoBancoService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.cnab.CNAB;
import br.com.abril.nds.util.cnab.UtilitarioCNAB;
import br.com.abril.nds.util.cnab.UtilitarioCNAB.PadraoCNAB;
import br.com.abril.nds.vo.ValidacaoVO;
/**
 * Classe de implementação de serviços referentes 
 * a leitura de arquivo de retorno do banco.
 * 
 * @author Discover Technology
 */
@Service
public class LeitorArquivoBancoServiceImpl implements LeitorArquivoBancoService {
	
	
	private static final String[] EXTENSOES_ARQUIVO_VALIDAS = {"dat", "ret"};
	
	public ArquivoPagamentoBancoDTO obterPagamentosBanco(File file, String nomeArquivo) {
	    
		this.validarDadosEntrada(file, nomeArquivo);
		
		ArquivoPagamentoBancoDTO arquivoPagamentoBanco = null;
		
		List<String> lines = null;
		
		try {
			
			lines = FileUtils.readLines(file, "UTF8");
			
		} catch (IOException e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Falha ao processar o arquivo!");
		}

		PadraoCNAB padraoCNAB = obterPadraoCNAB(lines);
		
		validarConteudoLinhasCNAB(padraoCNAB, lines);
		
		arquivoPagamentoBanco = this.lerLinhasCNAB(padraoCNAB, lines, nomeArquivo);
		
		return arquivoPagamentoBanco;
	}
	
	/**
	 * Obtem o padrão CNAB a ser utilizado verificando
	 * o tamanho da primeira linha.
	 * 
	 * @param lines
	 * 
	 * @return PadraoCNAB
	 */
	private PadraoCNAB obterPadraoCNAB(List<String> lines) {
		
		if(lines == null || lines.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Arquivo vazio!");
		}
		
		int sizePrimeiraLinha = lines.get(0).length();
		
		if(PadraoCNAB.CNAB240.getSize() == sizePrimeiraLinha) {
			
			validarTodasLinhasSeguemPadraoCNAB(PadraoCNAB.CNAB240, lines);
			
			return PadraoCNAB.CNAB240;
			
		}
		
		if(PadraoCNAB.CNAB400.getSize() == sizePrimeiraLinha) {

			validarTodasLinhasSeguemPadraoCNAB(PadraoCNAB.CNAB400, lines);

			return PadraoCNAB.CNAB400;
		}
		
		throw new ValidacaoException(TipoMensagem.WARNING, "Arquivo não está no padrão CNAB!");
		
	}
	
	private void validarTodasLinhasSeguemPadraoCNAB(PadraoCNAB padraoCNAB, List<String> lines) {
		
		for(String line : lines) {
			
			if(line == null ||  padraoCNAB.getSize() != line.length() ) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Arquivo não está no padrão CNAB!");
			}
			
		}
		
	}

	
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
		BigDecimal taxaBancaria = BigDecimal.ZERO;
		
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
				
				if(bancoCNAB.containsValorTaxaBancaria(line)) {
					taxaBancaria 		= this.parseBigDecimal(bancoCNAB.obterTarifaCobranca(line));
					pagamento.setTaxaBancaria(taxaBancaria);
				}
				
			}
			
		}
		
		List<PagamentoDTO> listaPagamento = new ArrayList<PagamentoDTO>(mapaPagamento.values());
		
		for(PagamentoDTO p : listaPagamento) {
			validarLeituraLinha(p);
			somaPagamentos = somaPagamentos.add(p.getValorPagamento()).add(p.getTaxaBancaria());
		}
		
		arquivoPagamentoBanco.setListaPagemento(listaPagamento);
		arquivoPagamentoBanco.setSomaPagamentos(somaPagamentos);
		arquivoPagamentoBanco.setNomeArquivo(nomeArquivo);

		return arquivoPagamentoBanco;
	}
	
	private void validarDadosEntrada(File file, String nomeArquivo) {
		
		if (nomeArquivo == null || nomeArquivo.trim().length() == 0) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nome do arquivo é obrigatório!");
		}
		
		if (nomeArquivo.trim().length() > 255) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,"O nome do arquivo deve possuir até 255 caracteres!");
		}
		
		if (!isExtensaoArquivoValida(nomeArquivo)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Extensão do arquivo inválida!");
		}
		
		if (file == null || !file.isFile()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Arquivo inválido!");
		}
	}
	
	private void validarConteudoLinhasCNAB(PadraoCNAB padraoCNAB, List<String> lines) {
		
		int qtdLinhas = lines.size();
		
		if (!padraoCNAB.possuiQuantidadeMinimaLinhas(qtdLinhas)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Arquivo não está no padrão " + padraoCNAB.getDescricao());
		}
		
		String primeiraLinha =  lines.get(0);
		String ultimaLinha = lines.get(lines.size() - 1);
		
		
		if (!padraoCNAB.isHeader(primeiraLinha) || !padraoCNAB.isTrailer(ultimaLinha)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Arquivo não está no padrão " + padraoCNAB.getDescricao());
		}
		
		for(String line : lines) {
			
			if (padraoCNAB.isDetalhe(line)) {
				return;
			}
			
		}
		
		throw new ValidacaoException(TipoMensagem.WARNING, "Arquivo não está no padrão " + padraoCNAB.getDescricao());
		
	}
	
	private void validarLeituraLinha(PagamentoDTO pagamento) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		boolean ignorarDataPagamento = true;
		try {
			if (pagamento != null && pagamento.getNossoNumero() != null && Integer.parseInt(pagamento.getNossoNumero()) > 0) {
				ignorarDataPagamento = false;
			}
		} catch (NumberFormatException nfe) {
			
		}
		
		if (!ignorarDataPagamento && pagamento.getDataPagamento() == null) {
			
			listaMensagens.add("Falha ao processar o arquivo: data de pagamento inválida!");
		}
		
		if (pagamento.getNossoNumero() == null) {

			listaMensagens.add("Falha ao processar o arquivo: nosso número inválido!");
		}
		
		if (pagamento.getNumeroRegistro() == null) {

			listaMensagens.add("Falha ao processar o arquivo: número do registro inválido!");
		}
		
		if (pagamento.getValorPagamento() == null) {

			listaMensagens.add("Falha ao processar o arquivo: valor de pagameno inválido!");
		}
		
		if (!listaMensagens.isEmpty()) {
			
			ValidacaoVO validacao = new ValidacaoVO();
			
			validacao.setTipoMensagem(TipoMensagem.WARNING);
			
			validacao.setListaMensagens(listaMensagens);
			
			throw new ValidacaoException(validacao);
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

	private Long parseLong(String valor) {
		
		try {
			
			return Long.valueOf(valor);
		} catch(NumberFormatException e) {
			
			return null;
		}
	}
	
	private BigDecimal parseBigDecimal(String valor) {
		
		StringBuilder sb = new StringBuilder(valor);
		
		sb.insert(valor.length() - 2, ".");
		
		try {
			
			return new BigDecimal(sb.toString());
		} catch(NumberFormatException e) {
			
			return null;
		}
	}
	
}
