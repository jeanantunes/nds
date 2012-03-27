package br.com.abril.nds.util;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

public abstract class Util {
	
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
	
	public static String gerarNossoNumero(Integer numeroCota, Date dtGeracao){
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
		
		String auxData = DateUtil.formatarData(dtGeracao, "ddMMyyyy");
		
		n3 = (x % 11);
		if ((n3 == 0) || (n3 == 10)){
			n3 = 0;
		}
		
		return codSacado + auxData + n1 + n2 + n3;
	}
	
	private static String padLeft(String valor, String caractere, int tamanho){
		while (valor.length() < tamanho){
			valor = caractere + valor;
		}
		
		return valor;
	}
}
