package br.inf.portalfiscal.nfe.util;

import java.util.Calendar;
import java.util.Date;

public abstract class Util {
	// a diverenca sera retornada em {anos, meses, dias}
    public static Integer[] diferencaEntreDatas(final Calendar date1,
                                                 final Calendar date2)
    {
        if (date2.compareTo(date1) < 0) {
            throw new RuntimeException("Data 2 deve ser igual ou maior que data 1");
        }
        Calendar d1 = (Calendar) date1.clone();
        Calendar d2 = (Calendar) date2.clone();

        // desconsidera a hora contida nos objetos
        d1.set(Calendar.HOUR_OF_DAY, 0);
        d1.set(Calendar.MINUTE, 0);
        d1.set(Calendar.SECOND, 0);
        d1.set(Calendar.MILLISECOND, 0);

        d2.set(Calendar.HOUR_OF_DAY, 0);
        d2.set(Calendar.MINUTE, 0);
        d2.set(Calendar.SECOND, 0);
        d2.set(Calendar.MILLISECOND, 0);

        int anos = 0, meses = 0, dias = 0;
        Calendar tmp = (Calendar) date1.clone();
        tmp.setLenient(true);
        while (true) {
            tmp.add(Calendar.YEAR, 1);
            if (tmp.compareTo(d2) > 0) {
                tmp.add(Calendar.YEAR, -1);
                anos = tmp.get(Calendar.YEAR) - d1.get(Calendar.YEAR);
                break;
            }
        }

        while (true) {
            tmp.add(Calendar.MONTH, 1);
            if (tmp.compareTo(d2) > 0) {
                tmp.add(Calendar.MONTH, -1);
                meses = tmp.get(Calendar.MONTH) - d1.get(Calendar.MONTH);
                break;
            }
        }
        tmp.add(Calendar.DAY_OF_MONTH,
            d1.get(Calendar.DAY_OF_MONTH) - tmp.get(Calendar.DAY_OF_MONTH));
        long d2time = d2.getTimeInMillis() + d2.get(Calendar.DST_OFFSET);
        long d1time = tmp.getTimeInMillis() + tmp.get(Calendar.DST_OFFSET);
        
        dias = (int) ((d2time - d1time) / (24 * 60 * 60 * 1000));
        return new Integer[]{anos, meses, dias};
    }

    public static Integer diferencaEntreDatasEmDias(final Date date1,final Date date2){
    	
    	if (date2.compareTo(date1) < 0) {
    		throw new RuntimeException("Data 2 deve ser igual ou maior que data 1");
    	}
    	Calendar cal=Calendar.getInstance();
    	cal.add(Calendar.DAY_OF_MONTH, 1);
    	
    	Calendar cal2=Calendar.getInstance();
    	cal2.setTime(date2);
    	
        
        // calcula a diferenca em milisegs
        return (int) ((cal.getTime().getTime() - cal2.getTime().getTime()) / (24 * 60 * 60 * 1000));
    }
    
	
	
}
