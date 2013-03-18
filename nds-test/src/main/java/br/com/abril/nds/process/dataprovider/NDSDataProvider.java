package br.com.abril.nds.process.dataprovider;

import java.util.ArrayList;
import java.util.List;

import org.testng.ITestContext;

public abstract class NDSDataProvider {
    
    protected static List<Long> getParamCotaId(ITestContext context) {

	String paramCotaId = context.getCurrentXmlTest().getParameter("cotaId");

	List<Long> listParamCotaId = new ArrayList<Long>();
	if (paramCotaId != null && !paramCotaId.equalsIgnoreCase("")) {

	    String[] arrayParamCotaId = paramCotaId.split(",");

	    if (arrayParamCotaId != null) {
		int iParamCotaid = 0;
		while (iParamCotaid < arrayParamCotaId.length) {
		    listParamCotaId.add(Long.parseLong(arrayParamCotaId[iParamCotaid].trim()));
		    iParamCotaid++;
		}
	    }
	}

	return listParamCotaId;
    }

}
