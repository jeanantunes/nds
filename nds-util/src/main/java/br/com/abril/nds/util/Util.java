package br.com.abril.nds.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public abstract class Util {
	
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	public static boolean isNumeric(String valor){
		try {
			Double.parseDouble(valor);
		} catch (NumberFormatException n){
			return false;
		}
		
		return true;
	}
	
	public static boolean isLong(String valor){
		try {
			Long.parseLong(valor);
		} catch (NumberFormatException n){
			return false;
		}
		
		return true;
	}
	
	public static <E extends Enum<E>> E getEnumByStringValue(E[] values, String stringValue) {

		for (E enumConstant : values) {

			if (enumConstant.toString().equals(stringValue)) {

				return enumConstant;
			}
		}

		return null;
	}
	
	public static boolean isAjaxRequest(HttpServletRequest request) {
		
		if (request == null) {
		
			return false;
		}
		
		return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
	}
	
	public static boolean isAjaxUpload(HttpServletRequest request) {
		
		if (request == null) {
		
			return false;
		}
		
		boolean isUploadAjax = false;
		
		String formUploadAjax = request.getParameter(Constantes.UPLOAD_AJAX_REQUEST_ATTRIBUTE);
		
		if (formUploadAjax != null) {
			isUploadAjax = new Boolean(formUploadAjax);
		}
		
		return isUploadAjax;
	}
	
	public static String gerarNossoNumero(Integer numeroCota, Date dtGeracao, String numeroBanco, Long idFornecedor, Long idMovimentoFinanceiro){
		long n1;
		long n2 = 4;
		long n3;
		
		int[] pesos = {9, 8, 7, 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
		long x = 0;
		
		String codSacado = Util.padLeft(numeroCota.toString(), "0", 5);
		
		for (int i = codSacado.length(); i > 0; i--){
			x += Double.parseDouble(codSacado.substring(i - 1, i)) * pesos[codSacado.length() - i];
		}
		
		n1 = (x % 11);
		if ((n1 == 0) || (n1 == 10)){
			n1 = 0;
		}
		
		String auxData = DateUtil.formatarData(dtGeracao, "ddMMyy");
		
		n3 = (x % 11);
		if ((n3 == 0) || (n3 == 10)){
			n3 = 0;
		}
		
		NomeBanco nomeBanco = NomeBanco.getByNumeroBanco(numeroBanco);
		
		switch (nomeBanco) {
			case BANCO_ABN_AMRO_REAL:
				return codSacado + auxData + n1 + n2 + n3 + idMovimentoFinanceiro + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCO_BRADESCO:
				return codSacado + auxData + n1 + n2 + n3 + idMovimentoFinanceiro + (idFornecedor == null ? "0" : idFornecedor);
			
			case BANCO_DO_BRASIL:
				return codSacado + auxData + n1 + n2 + n3 + idMovimentoFinanceiro + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCO_DO_ESTADO_DO_ESPIRITO_SANTO:
				return codSacado + auxData + n1 + n2 + n3 + idMovimentoFinanceiro + (idFornecedor == null ? "0" : idFornecedor);
			
			case BANCO_DO_ESTADO_DO_RIO_GRANDE_DO_SUL:
				return codSacado + auxData + n1 + n2 + n3 + idMovimentoFinanceiro + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCO_DO_NORDESTE_DO_BRASIL:
				return codSacado + auxData + n1 + n2 + n3 + idMovimentoFinanceiro + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCO_INTEMEDIUM:
				return codSacado + auxData + n1 + n2 + n3 + idMovimentoFinanceiro + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCO_ITAU:
				return codSacado + auxData + n1 + n2 + n3 + idMovimentoFinanceiro + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCO_RURAL:
				return codSacado + auxData + n1 + n2 + n3 + idMovimentoFinanceiro + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCO_SAFRA:
				return codSacado + auxData + n1 + n2 + n3 + idMovimentoFinanceiro + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCO_SANTANDER:
				return codSacado + auxData + n1 + n2 + n3 + idMovimentoFinanceiro + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCO_SICREDI:
				return codSacado + auxData + n1 + n2 + n3 + idMovimentoFinanceiro + (idFornecedor == null ? "0" : idFornecedor);
				
			case BANCOOB:
				return codSacado + auxData + n1 + n2 + n3 + idMovimentoFinanceiro + (idFornecedor == null ? "0" : idFornecedor);
				
			case CAIXA_ECONOMICA_FEDERAL:
				return codSacado + auxData + n1 + n2 + n3 + idMovimentoFinanceiro + (idFornecedor == null ? "0" : idFornecedor);
				
			case HSBC:
				return Util.padLeft(codSacado + auxData + n1 + n2 + n3 + idMovimentoFinanceiro + (idFornecedor == null ? "0" : idFornecedor), "0", 13);
				
			case MERCANTIL_DO_BRASIL:
				return codSacado + auxData + n1 + n2 + n3 + idMovimentoFinanceiro + (idFornecedor == null ? "0" : idFornecedor);
				
			case NOSSA_CAIXA:
				return codSacado + auxData + n1 + n2 + n3 + idMovimentoFinanceiro + (idFornecedor == null ? "0" : idFornecedor);
				
			case UNIBANCO:
				return codSacado + auxData + n1 + n2 + n3 + idMovimentoFinanceiro + (idFornecedor == null ? "0" : idFornecedor);
	
			default:
				return codSacado + auxData + n1 + n2 + n3 + idMovimentoFinanceiro + (idFornecedor == null ? "0" : idFornecedor);
		}
	}
	
	private static String padLeft(String valor, String caractere, int tamanho){
		while (valor.length() < tamanho - 1){
			valor = caractere + valor;
		}
		
		return valor;
	}
	
	public static String adicionarMascaraCNPJ(String cnpj){
		
		if (cnpj == null){
			return "";
		}
		
		cnpj = cnpj.replace("-", "").replace(".", "").replace("/", "");
		
		if (cnpj.length() < 12){
			cnpj = Util.padLeft(cnpj, "0", 12);
		}
		
		StringBuilder formatado = new StringBuilder();
		formatado.append(cnpj.substring(0, 2)).append(".").append(cnpj.substring(2, 5)).append(".").append(cnpj.substring(5, 8)).append("/").append(cnpj.substring(8, 12)).append("-").append(cnpj.substring(12, 14));
		
		return formatado.toString();
	}
	
	public static String adicionarMascaraCPF(String cpf){
		
		if (cpf == null){
			return "";
		}
		
		cpf = cpf.replace("-", "").replace(".", "");
		
		if (cpf.length() < 11){
			cpf = Util.padLeft(cpf, "0", 11);
		}
		
		StringBuilder formatado = new StringBuilder();
		formatado.append(cpf.substring(0, 3)).append(".").append(cpf.substring(3, 6)).append(".").append(cpf.substring(6, 9)).append("-").append(cpf.substring(9, 11));
		
		return formatado.toString();
	}
	
	public static boolean validarEmail(final String email){
		
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		
		Matcher matcher = pattern.matcher(email);
		
		return matcher.matches();
	}
	
	
	/** Classe que representa a forma de agrupamento das UFs brasileiras. */
	public enum TipoAgrupamentoUf {
		ORDEM_ALFABETICA,
		DIVISAO_GEOGRAFICA;
	}
	
	/**
	 * Retorna a lista de UFs brasileiras.<br>
	 * A lista pode vir ordenada em:<br>
	 * <ul>
	 * <li>Ordem Alfabética; (Ordenação padrão);</li>
	 * <li>Divisão Geográfica (norte, nordestes, centro-oeste, sudeste e sul);</li>
	 * </ul>
	 * 
	 * @param tpUf Determina o tipo de ordenação.<br>
	 * Se for passado <i>null</i> será utilizado a ordenação padrão (ordem alfabética);
	 * 
	 * @return
	 */
	public static List<UfEnum> getUfs(TipoAgrupamentoUf tpUf) {
		
		List<UfEnum> lst = new ArrayList<UfEnum>();
		
		// Região Norte:
		lst.add(UfEnum.AC);	// Acre
		lst.add(UfEnum.AM);	// Amazonas	
		lst.add(UfEnum.AP);	// Amapá
		lst.add(UfEnum.PA);	// Pará
		lst.add(UfEnum.RO);	// Rondônia
		lst.add(UfEnum.RR);	// Roraima
		lst.add(UfEnum.TO);	// Tocantins
		
		// Região Nordeste:
		lst.add(UfEnum.AL);	// Alagoas
		lst.add(UfEnum.BA);	// Bahia
		lst.add(UfEnum.CE);	// Ceará
		lst.add(UfEnum.MA);	// Maranhão
		lst.add(UfEnum.PB);	// Paraíba
		lst.add(UfEnum.PE);	// Pernambuco
		lst.add(UfEnum.PI);	// Piauí
		lst.add(UfEnum.RN);	// Rio Grande do Norte
		lst.add(UfEnum.SE);	// Sergipe
		
		// Região Centro-Oeste:
		lst.add(UfEnum.DF);	// Distrito Federal
		lst.add(UfEnum.GO);	// Goiás
		lst.add(UfEnum.MS);	// Mato Grosso do Sul
		lst.add(UfEnum.MT);	// Mato Grosso
		
		// Região Sudeste:
		lst.add(UfEnum.ES);	// Espírito Santo
		lst.add(UfEnum.MG);	// Minas Gerais
		lst.add(UfEnum.RJ);	// Rio de Janeiro
		lst.add(UfEnum.SP);	// São Paulo
		
		// Região Sul:
		lst.add(UfEnum.PR);	// Paraná
		lst.add(UfEnum.RS);	// Rio Grande do Sul
		lst.add(UfEnum.SC);	// Santa Catarina
		
		
		// Ordena por ordem alabética as siglas se for diferente de divisão geográfica:
		if (!TipoAgrupamentoUf.DIVISAO_GEOGRAFICA.equals(tpUf)) {
			Collections.sort(lst, new Comparator<UfEnum>() {
				@Override
				public int compare(UfEnum o1, UfEnum o2) {
					return o1.getSigla().compareTo(o2.getSigla());
				}
			});
		}
		
		return lst;
	}

	/**
	 * Retorna a string enviada criptografada em MD5
	 * @param original
	 * @return
	 */
	public static String md5(String original) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		BigInteger hash = new BigInteger(1, md.digest(original.getBytes()));  
		return hash.toString(16);
	}
	
	/**
	 * Remove a mascara do CNPJ
	 * @param cnpj
	 * @return
	 */
	public static String removerMascaraCnpj(String cnpj){
		if(cnpj == null){
			return null;
		}		
		return cnpj.replaceAll("\\.", "").replaceAll("-", "").replaceAll("/", "");
	}

}
