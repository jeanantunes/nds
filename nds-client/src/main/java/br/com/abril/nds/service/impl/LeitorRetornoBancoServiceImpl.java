package br.com.abril.nds.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.dto.PagamentoDTO;
import br.com.abril.nds.service.LeitorRetornoBancoService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;

/**
 * Classe de implementação de serviços referentes 
 * a leitura de arquivo de retorno do banco.
 * 
 * @author Discover Technology
 */
@Service
public class LeitorRetornoBancoServiceImpl implements LeitorRetornoBancoService {

	public static final String FORMATO_DATA_ARQUIVO = "ddMMyy";
	
	public static final String REGISTRO_TIPO_HEADER = "0";
	public static final String REGISTRO_TIPO_DETALHE = "1";
	public static final String REGISTRO_TIPO_TRAILER = "9";
	
	public static final int PADRAO_ARQUIVO_CNAB_400 = 400;
	public static final int PADRAO_ARQUIVO_CNAB_240 = 240;
	
	public static final int INDEX_CNAB_400_DATA_PAGAMENTO_INICIO = 110;
	public static final int INDEX_CNAB_400_DATA_PAGAMENTO_FIM = 116;
	
	public static final int INDEX_CNAB_400_NOSSO_NUMERO_INICIO = 37;
	public static final int INDEX_CNAB_400_NOSSO_NUMERO_FIM = 53;
	
	public static final int INDEX_CNAB_400_VALOR_PAGAMENTO_INICIO = 253;
	public static final int INDEX_CNAB_400_VALOR_PAGAMENTO_FIM = 266;
	
	public ArquivoPagamentoBancoDTO obterPagamentosBanco(File file,
														 String nomeArquivo) {
		
		if (nomeArquivo == null || nomeArquivo.trim().length() == 0) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Nome do arquivo é obrigatório!");
		}
		
		if (file == null || !file.isFile()) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Arquivo inválido!");
		}
		
		List<String> lines = null;
		
		try {
			lines = FileUtils.readLines(file, "UTF8");
			
		} catch (IOException e) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha ao ler o arquivo!");
		}
		
		BigDecimal somaPagamentos = BigDecimal.ZERO;
		
		List<PagamentoDTO> listaPagamento = new ArrayList<PagamentoDTO>();
		
		PagamentoDTO pagamento = null;
		
		String line = null;
		
		for (int i = 0; i < lines.size(); i++) {
			
			line = lines.get(i);
		
			if (line.startsWith(REGISTRO_TIPO_DETALHE)) {
				
				if (line.length() == PADRAO_ARQUIVO_CNAB_400) {
					
					pagamento = lerLinhasCNAB400(line);
					
					somaPagamentos = somaPagamentos.add(pagamento.getValorPagamento());
					
					listaPagamento.add(pagamento);
				
				} else if (line.length() == PADRAO_ARQUIVO_CNAB_240) {
					
					//TODO: ler arquivo no padrão CNAB 240
				}
			}
		}
		
		ArquivoPagamentoBancoDTO arquivoPagamentoBanco = new ArquivoPagamentoBancoDTO();
		
		arquivoPagamentoBanco.setListaPagemento(listaPagamento);
		arquivoPagamentoBanco.setSomaPagamentos(somaPagamentos);
		arquivoPagamentoBanco.setNomeArquivo(nomeArquivo);
		
		return arquivoPagamentoBanco;
	}
	
	private PagamentoDTO lerLinhasCNAB400(String line) {
		
		PagamentoDTO pagamento = new PagamentoDTO();
		
		pagamento.setDataPagamento(
			DateUtil.parseData(line.substring(INDEX_CNAB_400_DATA_PAGAMENTO_INICIO,
											  INDEX_CNAB_400_DATA_PAGAMENTO_FIM),
							   FORMATO_DATA_ARQUIVO));
		
		pagamento.setNossoNumero(line.substring(INDEX_CNAB_400_NOSSO_NUMERO_INICIO,
												INDEX_CNAB_400_NOSSO_NUMERO_FIM));
		
		pagamento.setValorPagamento(formatarValor(line.substring(INDEX_CNAB_400_VALOR_PAGAMENTO_INICIO,
													  			 INDEX_CNAB_400_VALOR_PAGAMENTO_FIM)));

		return pagamento;
	}
	
	private BigDecimal formatarValor(String valor) {
		
		StringBuilder sb = new StringBuilder(valor);
		
		sb.insert(valor.length() - 2, ".");
		
		try {
			return new BigDecimal(sb.toString());
		
		} catch(NumberFormatException e) {
			
			return BigDecimal.ZERO;
		}
	}
	
}
