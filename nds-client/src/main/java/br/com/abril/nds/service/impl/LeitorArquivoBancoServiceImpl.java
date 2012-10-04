package br.com.abril.nds.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.dto.PagamentoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.service.LeitorArquivoBancoService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;
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
	
	private static final String FORMATO_DATA_ARQUIVO = "ddMMyy";
	
	private static final String REGISTRO_TIPO_HEADER = "0";
	private static final String REGISTRO_TIPO_DETALHE = "1";
	private static final String REGISTRO_TIPO_TRAILER = "9";
	
	private static final int PADRAO_ARQUIVO_CNAB_400 = 400;
	private static final int PADRAO_ARQUIVO_CNAB_240 = 240;
	
	private static final int INDEX_CNAB_400_NUMERO_AGENCIA_INICIO = 26;
	private static final int INDEX_CNAB_400_NUMERO_AGENCIA_FIM = 31;
	
	private static final int INDEX_CNAB_400_NUMERO_CONTA_INICIO = 33;
	private static final int INDEX_CNAB_400_NUMERO_CONTA_FIM = 44;
	
	private static final int INDEX_CNAB_400_CODIGO_BANCO_INICIO = 76;
	private static final int INDEX_CNAB_400_CODIGO_BANCO_FIM = 79;
	
	private static final int INDEX_CNAB_400_DATA_PAGAMENTO_INICIO = 110;
	private static final int INDEX_CNAB_400_DATA_PAGAMENTO_FIM = 116;
	
	private static final int INDEX_CNAB_400_NOSSO_NUMERO_INICIO = 37;
	private static final int INDEX_CNAB_400_NOSSO_NUMERO_FIM = 53;
	
	private static final int INDEX_CNAB_400_VALOR_PAGAMENTO_INICIO = 253;
	private static final int INDEX_CNAB_400_VALOR_PAGAMENTO_FIM = 266;
	
	private static final int INDEX_CNAB_400_NUMERO_REGISTRO_INICIO = 394;
	private static final int INDEX_CNAB_400_NUMERO_REGISTRO_FIM = 400;
	
	public ArquivoPagamentoBancoDTO obterPagamentosBanco(File file, String nomeArquivo) {
		
		this.validarDadosEntrada(file, nomeArquivo);
		
		ArquivoPagamentoBancoDTO arquivoPagamentoBanco = null;
		
		List<String> lines = null;
		
		try {
			lines = FileUtils.readLines(file, "UTF8");
			
		} catch (IOException e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Falha ao processar o arquivo!");
		}
		
		validarConteudoLinhas(lines);
		
		//Verifica a quantidade de caractres da primeira linha para determinar o padrão do arquivo
		if (lines.get(0).length() == PADRAO_ARQUIVO_CNAB_400) {
			
			arquivoPagamentoBanco = this.lerLinhasCNAB400(lines, nomeArquivo);
			
		} else if (lines.get(1).length() == PADRAO_ARQUIVO_CNAB_240) {
			
			//TODO: ler arquivo no padrão CNAB 240
		} else {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Arquivo não está no padrão CNAB!");
		}
		
		return arquivoPagamentoBanco;
	}

	private ArquivoPagamentoBancoDTO lerLinhasCNAB400(List<String> lines, String nomeArquivo) {
		
		ArquivoPagamentoBancoDTO arquivoPagamentoBanco = new ArquivoPagamentoBancoDTO();
		
		BigDecimal somaPagamentos = BigDecimal.ZERO;
		
		BigDecimal valorPagemento = BigDecimal.ZERO;
		
		List<PagamentoDTO> listaPagamento = new ArrayList<PagamentoDTO>();
		
		PagamentoDTO pagamento = null;
		
		String line = null;
		
		for (int i = 0; i < lines.size(); i++) {
			
			line = lines.get(i);
			
			if (line.startsWith(REGISTRO_TIPO_HEADER)) {
				
				arquivoPagamentoBanco.setNumeroAgencia(
					this.parseLong(line.substring(INDEX_CNAB_400_NUMERO_AGENCIA_INICIO,
												  INDEX_CNAB_400_NUMERO_AGENCIA_FIM)));
				
				arquivoPagamentoBanco.setNumeroConta(
					this.parseLong(line.substring(INDEX_CNAB_400_NUMERO_CONTA_INICIO,
												  INDEX_CNAB_400_NUMERO_CONTA_FIM)));
				
				arquivoPagamentoBanco.setCodigoBanco(
					line.substring(INDEX_CNAB_400_CODIGO_BANCO_INICIO, INDEX_CNAB_400_CODIGO_BANCO_FIM));
				
			} else if (line.startsWith(REGISTRO_TIPO_DETALHE)) {
				
				if (line.length() == PADRAO_ARQUIVO_CNAB_400) {
		
					pagamento = new PagamentoDTO();
					
					pagamento.setNumeroRegistro(
						this.parseLong(line.substring(INDEX_CNAB_400_NUMERO_REGISTRO_INICIO,
													  INDEX_CNAB_400_NUMERO_REGISTRO_FIM)));
					
					pagamento.setDataPagamento(
						DateUtil.parseData(
							line.substring(INDEX_CNAB_400_DATA_PAGAMENTO_INICIO, INDEX_CNAB_400_DATA_PAGAMENTO_FIM),
							FORMATO_DATA_ARQUIVO));
					
					pagamento.setNossoNumero(line.substring(INDEX_CNAB_400_NOSSO_NUMERO_INICIO,
															INDEX_CNAB_400_NOSSO_NUMERO_FIM));
					
					valorPagemento =
						this.parseBigDecimal(line.substring(INDEX_CNAB_400_VALOR_PAGAMENTO_INICIO,
				  			 								INDEX_CNAB_400_VALOR_PAGAMENTO_FIM));
					
					pagamento.setValorPagamento(valorPagemento);
					
					validarLeituraLinha(pagamento);
					
					somaPagamentos = somaPagamentos.add(valorPagemento);
					
					listaPagamento.add(pagamento);
				
				} else {
					
					throw new ValidacaoException(TipoMensagem.WARNING,
						"Falha ao processar o arquivo: todas as linhas devem estar no padrão CNAB 400!");
				}
			}
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
			throw new ValidacaoException(TipoMensagem.WARNING,
				"O nome do arquivo deve possuir até 255 caracteres!");
		}
		
		if (!isExtensaoArquivoValida(nomeArquivo)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Extensão do arquivo inválida!");
		}
		
		if (file == null || !file.isFile()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Arquivo inválido!");
		}
	}
	
	private void validarConteudoLinhas(List<String> lines) {
		
		//O arquivo deve possuir ao menos 3 linhas para estar no padrão CNAB
		// 0 - Header
		// 1 - Detalhe
		// 9 - Trailer
		
		if (lines.size() < 3) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Arquivo não está no padrão CNAB!");
		}
		
		if (!lines.get(0).startsWith(REGISTRO_TIPO_HEADER)
				|| !lines.get(lines.size() - 1).startsWith(REGISTRO_TIPO_TRAILER)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Arquivo não está no padrão CNAB!");
		}
		
		String line = null;
		
		boolean existeLinhaDetalhe = false;
		
		for (int i = 0; i < lines.size(); i++) {
			
			line = lines.get(i);
		
			if (line.startsWith(REGISTRO_TIPO_DETALHE)) {
				
				existeLinhaDetalhe = true;
				
				break;
			}
		}
		
		if (!existeLinhaDetalhe) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Arquivo não está no padrão CNAB!");
		}
	}
	
	private void validarLeituraLinha(PagamentoDTO pagamento) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		if (pagamento.getDataPagamento() == null) {
			
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
