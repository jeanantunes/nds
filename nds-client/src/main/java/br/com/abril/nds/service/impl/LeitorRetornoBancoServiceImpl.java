package br.com.abril.nds.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.dto.PagamentoDTO;
import br.com.abril.nds.service.LeitorRetornoBancoService;
import br.com.abril.nds.util.DateUtil;

/**
 * Classe de implementação de serviços referentes 
 * a leitura de arquivo de retorno do banco.
 * 
 * @author Discover Technology
 */
@Service
public class LeitorRetornoBancoServiceImpl implements LeitorRetornoBancoService {

	public static final String FORMATO_DATA_ARQUIVO = "ddMMyy";
	
	public ArquivoPagamentoBancoDTO obterPagamentosBanco(File file,
														 String nomeArquivo) throws IOException,
														 							ParseException {
		
		List<String> lines = FileUtils.readLines(file, "UTF8");
		
		BigDecimal somaPagamentos = BigDecimal.ZERO;
		
		BigDecimal valorPagamento = BigDecimal.ZERO;
		
		List<PagamentoDTO> listaPagamento = new ArrayList<PagamentoDTO>();
		
		PagamentoDTO pagamento = null;
		
		String line = null;
		
		for (int i = 0; i < lines.size(); i++) {
			
			//TODO: validar leitura dos dados
			
			line = lines.get(i);
		
			if (i > 0 && i < lines.size() - 1) {
				
				pagamento = new PagamentoDTO();
				
				pagamento.setDataPagamento(DateUtil.parseData(line.substring(110, 116),
															  FORMATO_DATA_ARQUIVO));
				
				pagamento.setNossoNumero(line.substring(37, 53));
				
				valorPagamento = formatarValor(line.substring(253, 266));
				
				pagamento.setValorPagamento(valorPagamento);
				
				somaPagamentos = somaPagamentos.add(valorPagamento);
				
				/*System.out.print("Vencimento: " + DateUtil.parseData(line.substring(146, 152),
																	 FORMATO_DATA_ARQUIVO));
				
				System.out.print("\t Data Recebimento: " + DateUtil.parseData(line.substring(110, 116),
																			  FORMATO_DATA_ARQUIVO));
				
				System.out.print("\t Identificação: " + line.substring(37, 53));
				
				System.out.print("\t Valor Nominal: " + formatarValor(line.substring(152, 165)));
				
				System.out.print("\t Valor Pago: " + formatarValor(line.substring(253, 266)));
				
				System.out.print("\t Juros Pago: " + formatarValor(line.substring(266, 279)));
				
				System.out.println("\t Desconto: " + formatarValor(line.substring(240, 253)));*/
				
				listaPagamento.add(pagamento);
			}
		}
		
		ArquivoPagamentoBancoDTO arquivoPagamentoBanco = new ArquivoPagamentoBancoDTO();
		
		arquivoPagamentoBanco.setListaPagemento(listaPagamento);
		arquivoPagamentoBanco.setSomaPagamentos(somaPagamentos);
		arquivoPagamentoBanco.setNomeArquivo(nomeArquivo);
		
		return arquivoPagamentoBanco;
	}
	
	private void validarValorPagamentos() {
		//TODO:
	}
	
	private BigDecimal formatarValor(String valor) {
		
		StringBuilder sb = new StringBuilder(valor);
		
		sb.insert(valor.length() - 2, ".");
		
		return new BigDecimal(sb.toString());
	}
	
}
