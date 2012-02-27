package br.com.abril.nds.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.abril.nds.vo.PeriodoVO;

public class DateUtil {

	public static boolean isValidDate(String valor, String pattern){
		if (pattern == null || pattern.trim().isEmpty()){
			pattern = Constantes.DATE_PATTERN_PT_BR;
		}
		try {
			DateFormat f = new SimpleDateFormat(pattern);
			f.setLenient(false);
			f.parse(valor);
		} catch (ParseException n){
			return false;
		}
		
		return true;
	}
	
	public static String formatarData(Date data, String formato) {
		return new SimpleDateFormat(formato).format(data);
	}
	
	public static boolean isDataFinalMaiorDataInicial(PeriodoVO periodo) {
		
		Date dataInicial = periodo.getDataInicial();
		Date dataFinal = periodo.getDataFinal();
		
		return dataInicial.compareTo(dataFinal) > 0;
	}
}
