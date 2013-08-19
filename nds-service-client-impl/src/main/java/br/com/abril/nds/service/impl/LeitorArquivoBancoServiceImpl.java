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
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.service.LeitorArquivoBancoService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.ValidacaoVO;

/**
 * Classe de implementação de serviços referentes 
 * a leitura de arquivo de retorno do banco.
 * 
 * @author Discover Technology
 */
@Service
public class LeitorArquivoBancoServiceImpl implements LeitorArquivoBancoService {
	
	
	private static final String BANCO_HSBC = "399";
	
	private static final String BANCO_BRADESCO = "237";
	
	private static final String BANCO_ITAU = "341";
	
	private static final String BANCO_DO_BRASIL = "001";
	
	private static final String[] EXTENSOES_ARQUIVO_VALIDAS = {"dat", "ret"};
	
	private static final String REGISTRO_TIPO_HEADER = "0";
	
	private static final String REGISTRO_TIPO_DETALHE = "1";
	
	private static final String REGISTRO_TIPO_TRAILER = "9";
	
	
    /**
     * Subclasse com os atributos contendo as posições dos campos do arquivo CNAB
     * @author luiz.marcili
     */
    private class Cnab{
    	
		public final int PADRAO_ARQUIVO_CNAB_240 = 240;
		public final int PADRAO_ARQUIVO_CNAB_400 = 400;
		
		public final int INDEX_CNAB_400_CODIGO_BANCO_INICIO = 76;
		public final int INDEX_CNAB_400_CODIGO_BANCO_FIM = 79;

		public int IndexCnab400DataPagamentoInicio;
		public int IndexCnab400DataPagamentoFim;
		
		public int IndexCnab400NossoNumeroInicio;
		public int IndexCnab400NossoNumeroFim;
		
		public int IndexCnab400ValorPagamentoInicio;
		public int IndexCnab400ValorPagamentoFim;
		
		public int IndexCnab400NumeroRegistroInicio;
		public int IndexCnab400NumeroRegistroFim;

		public int IndexCnab400NumeroAgenciaInicio;
		public int IndexCnab400NumeroAgenciaFim;
		
		public int IndexCnab400NumeroContaInicio;
		public int IndexCnab400NumeroContaFim;
    }
    
	/**
	 * Retorna Cnab configurado para o banco HSBC 
	 * @param cnab
	 */
    private void getCnab400Hsbc(Cnab cnab){
		
		cnab.IndexCnab400DataPagamentoInicio = 110;
		cnab.IndexCnab400DataPagamentoFim = 116;
		
		cnab.IndexCnab400NossoNumeroInicio = 37;
		cnab.IndexCnab400NossoNumeroFim = 53;
		
		cnab.IndexCnab400ValorPagamentoInicio = 253;
		cnab.IndexCnab400ValorPagamentoFim = 266;
		
		cnab.IndexCnab400NumeroRegistroInicio = 394;
		cnab.IndexCnab400NumeroRegistroFim = 400;
		
		cnab.IndexCnab400NumeroAgenciaInicio = 26;
		cnab.IndexCnab400NumeroAgenciaFim = 31;
		
		cnab.IndexCnab400NumeroContaInicio = 33;
		cnab.IndexCnab400NumeroContaFim = 44;
	}
	
    /**
	 * Retorna Cnab configurado para o banco Bradesco
	 * @param cnab
	 */
    private void getCnab400Bradesco(Cnab cnab){
    	
    	cnab.IndexCnab400DataPagamentoInicio = 110;
    	cnab.IndexCnab400DataPagamentoFim = 116;
		
    	cnab.IndexCnab400NossoNumeroInicio = 70;
    	cnab.IndexCnab400NossoNumeroFim = 81;
		
    	cnab.IndexCnab400ValorPagamentoInicio = 256;
    	cnab.IndexCnab400ValorPagamentoFim = 266;
		
    	cnab.IndexCnab400NumeroRegistroInicio = 395;
    	cnab.IndexCnab400NumeroRegistroFim = 400;
		
    	cnab.IndexCnab400NumeroAgenciaInicio = 26;
    	cnab.IndexCnab400NumeroAgenciaFim = 29;
		
    	cnab.IndexCnab400NumeroContaInicio = 31;
    	cnab.IndexCnab400NumeroContaFim = 37;
	}
    
    /**
	 * Retorna Cnab configurado para o banco Itau
	 * @param cnab
	 */
    private void getCnab400Itau(Cnab cnab){
    	
    	//TODO Configurar

    	cnab.IndexCnab400DataPagamentoInicio = 366;
    	cnab.IndexCnab400DataPagamentoFim = 391;
		
    	cnab.IndexCnab400NossoNumeroInicio = 63;
    	cnab.IndexCnab400NossoNumeroFim = 70;
		
    	cnab.IndexCnab400ValorPagamentoInicio = 127;
    	cnab.IndexCnab400ValorPagamentoFim = 139;
		
    	cnab.IndexCnab400NumeroRegistroInicio = 395;
		cnab.IndexCnab400NumeroRegistroFim = 400;

		cnab.IndexCnab400NumeroAgenciaInicio = 143;
		cnab.IndexCnab400NumeroAgenciaFim = 147;
		
		cnab.IndexCnab400NumeroContaInicio = 24;
		cnab.IndexCnab400NumeroContaFim = 28;
	}
    
    /**
	 * Retorna Cnab configurado para o banco BB
	 * @param cnab
	 */
    private void getCnab400BancoDoBrasil(Cnab cnab){
    	
    	//TODO Configurar
		
		cnab.IndexCnab400DataPagamentoInicio = 110;
		cnab.IndexCnab400DataPagamentoFim = 116;
		
		cnab.IndexCnab400NossoNumeroInicio = 37;
		cnab.IndexCnab400NossoNumeroFim = 53;
		
		cnab.IndexCnab400ValorPagamentoInicio = 253;
		cnab.IndexCnab400ValorPagamentoFim = 266;
		
		cnab.IndexCnab400NumeroRegistroInicio = 394;
		cnab.IndexCnab400NumeroRegistroFim = 400;
		
		cnab.IndexCnab400NumeroAgenciaInicio = 26;
		cnab.IndexCnab400NumeroAgenciaFim = 31;
		
		cnab.IndexCnab400NumeroContaInicio = 33;
		cnab.IndexCnab400NumeroContaFim = 44;
	}
    
    /**
     * Obtem padrão de leitura de Cnab por Banco
     * @param codigoBanco
     * @param cnab
     */
    private void obterPosicoesCnabPorBanco(String codigoBanco, Cnab cnab){
    	
    	switch (codigoBanco) {
		
			case BANCO_HSBC:
				
				this.getCnab400Hsbc(cnab);
				 
				break;
			case BANCO_BRADESCO:
				
				this.getCnab400Bradesco(cnab);
			
				break;
			case BANCO_ITAU:
				
				this.getCnab400Itau(cnab);
				
				break;
			case BANCO_DO_BRASIL:
				
				this.getCnab400BancoDoBrasil(cnab);
				
				break;			
			default:
				
				break;
		}
    }

	
	public ArquivoPagamentoBancoDTO obterPagamentosBanco(File file, String nomeArquivo) {
		
		Cnab padraoCnab = new Cnab();
	    
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
		if (lines.get(0).length() == padraoCnab.PADRAO_ARQUIVO_CNAB_400) {
			
			arquivoPagamentoBanco = this.lerLinhasCNAB400(lines, nomeArquivo);
			
		} else if (lines.get(1).length() == padraoCnab.PADRAO_ARQUIVO_CNAB_240) {
			
			//TODO: ler arquivo no padrão CNAB 240
			
			throw new UnsupportedOperationException("Não implementado para arquivo CNAB padrão 240.");
			
		} else {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Arquivo não está no padrão CNAB!");
		}
		
		return arquivoPagamentoBanco;
	}

	private ArquivoPagamentoBancoDTO lerLinhasCNAB400(List<String> lines, String nomeArquivo) {
		
		Cnab padraoCnab = new Cnab();

        String line = null;
		
		String codigoBanco = null;
		
		String strNumeroAgencia = null;
		
		String strNumeroConta = null;
		
		String strNumeroRegistro = null;
		
		String strDataPagamento = null;
		
		String strNossoNumero = null;
		
        BigDecimal somaPagamentos = BigDecimal.ZERO;
		
		BigDecimal valorPagamento = BigDecimal.ZERO;
		
		ArquivoPagamentoBancoDTO arquivoPagamentoBanco = new ArquivoPagamentoBancoDTO();
		
		List<PagamentoDTO> listaPagamento = new ArrayList<PagamentoDTO>();
		
		PagamentoDTO pagamento = null;
		
		for (int i = 0; i < lines.size(); i++) {
			
			line = lines.get(i);
			
			if (line.startsWith(REGISTRO_TIPO_HEADER)) {
				
				codigoBanco = line.substring(padraoCnab.INDEX_CNAB_400_CODIGO_BANCO_INICIO, 
						                     padraoCnab.INDEX_CNAB_400_CODIGO_BANCO_FIM);
				
				this.obterPosicoesCnabPorBanco(codigoBanco,padraoCnab);
				
				
				

				strNumeroAgencia = line.substring(padraoCnab.IndexCnab400NumeroAgenciaInicio,
                                                  padraoCnab.IndexCnab400NumeroAgenciaFim);

				strNumeroConta =   line.substring(padraoCnab.IndexCnab400NumeroContaInicio,
				                                  padraoCnab.IndexCnab400NumeroContaFim);
				
				
				

				arquivoPagamentoBanco.setCodigoBanco(codigoBanco);
				
				arquivoPagamentoBanco.setNumeroAgencia(this.parseLong(strNumeroAgencia));
					
			    arquivoPagamentoBanco.setNumeroConta(this.parseLong(strNumeroConta));
					
			} else if (line.startsWith(REGISTRO_TIPO_DETALHE)) {
				
				if (line.length() == padraoCnab.PADRAO_ARQUIVO_CNAB_400) {
					
					this.obterPosicoesCnabPorBanco(codigoBanco,padraoCnab);
					
					if (codigoBanco.equals(BANCO_BRADESCO)){

						strNumeroAgencia = line.substring(padraoCnab.IndexCnab400NumeroAgenciaInicio,padraoCnab.IndexCnab400NumeroAgenciaFim);
	
						strNumeroConta =   line.substring(padraoCnab.IndexCnab400NumeroContaInicio,padraoCnab.IndexCnab400NumeroContaFim);
					}	           
							                
					strNumeroRegistro =line.substring(padraoCnab.IndexCnab400NumeroRegistroInicio,
					                                  padraoCnab.IndexCnab400NumeroRegistroFim);
					
					strDataPagamento = line.substring(padraoCnab.IndexCnab400DataPagamentoInicio, 
					                                  padraoCnab.IndexCnab400DataPagamentoFim);
					
					strNossoNumero =   line.substring(padraoCnab.IndexCnab400NossoNumeroInicio,
					                                 padraoCnab.IndexCnab400NossoNumeroFim);
					
					valorPagamento = this.parseBigDecimal(line.substring(padraoCnab.IndexCnab400ValorPagamentoInicio,
						                                                 padraoCnab.IndexCnab400ValorPagamentoFim));

					arquivoPagamentoBanco.setNumeroAgencia(this.parseLong(strNumeroAgencia));
					
					arquivoPagamentoBanco.setNumeroConta(this.parseLong(strNumeroConta));
					
					pagamento = new PagamentoDTO();
					
					pagamento.setNumeroRegistro(this.parseLong(strNumeroRegistro));
						
					pagamento.setDataPagamento(DateUtil.parseData(strDataPagamento,Constantes.FORMATO_DATA_ARQUIVO_CNAB));
					
					pagamento.setNossoNumero(strNossoNumero);
					
					pagamento.setValorPagamento(valorPagamento);
					
					validarLeituraLinha(pagamento);
					
					somaPagamentos = somaPagamentos.add(valorPagamento);
					
					listaPagamento.add(pagamento);
				
				} else {
					
					throw new ValidacaoException(TipoMensagem.WARNING,"Falha ao processar o arquivo: todas as linhas devem estar no padrão CNAB 400!");
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
			
			throw new ValidacaoException(TipoMensagem.WARNING,"O nome do arquivo deve possuir até 255 caracteres!");
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
		//0 - Header
		//1 - Detalhe
		//9 - Trailer
		
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
