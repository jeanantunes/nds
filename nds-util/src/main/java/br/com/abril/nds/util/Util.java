package br.com.abril.nds.util;

import java.util.Date;
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
	
	public static String gerarNossoNumero(Integer numeroCota, Date dtGeracao, String numeroBanco){
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
			return codSacado + auxData + n1 + n2 + n3;
			
		case BANCO_BRADESCO:
			return codSacado + auxData + n1 + n2 + n3;
		
		case BANCO_DO_BRASIL:
			return codSacado + auxData + n1 + n2 + n3;
			
		case BANCO_DO_ESTADO_DO_ESPIRITO_SANTO:
			return codSacado + auxData + n1 + n2 + n3;
		
		case BANCO_DO_ESTADO_DO_RIO_GRANDE_DO_SUL:
			return codSacado + auxData + n1 + n2 + n3;
			
		case BANCO_DO_NORDESTE_DO_BRASIL:
			return codSacado + auxData + n1 + n2 + n3;
			
		case BANCO_INTEMEDIUM:
			return codSacado + auxData + n1 + n2 + n3;
			
		case BANCO_ITAU:
			return codSacado + auxData + n1 + n2 + n3;
			
		case BANCO_RURAL:
			return codSacado + auxData + n1 + n2 + n3;
			
		case BANCO_SAFRA:
			return codSacado + auxData + n1 + n2 + n3;
			
		case BANCO_SANTANDER:
			return codSacado + auxData + n1 + n2 + n3;
			
		case BANCO_SICREDI:
			return codSacado + auxData + n1 + n2 + n3;
			
		case BANCOOB:
			return codSacado + auxData + n1 + n2 + n3;
			
		case CAIXA_ECONOMICA_FEDERAL:
			return codSacado + auxData + n1 + n2 + n3;
			
		case HSBC:
			return Util.padLeft(codSacado + auxData + n1 + n2 + n3, "0", 13);
			
		case MERCANTIL_DO_BRASIL:
			return codSacado + auxData + n1 + n2 + n3;
			
		case NOSSA_CAIXA:
			return codSacado + auxData + n1 + n2 + n3;
			
		case UNIBANCO:
			return codSacado + auxData + n1 + n2 + n3;

		default:
			return codSacado + auxData + n1 + n2 + n3;
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
}
