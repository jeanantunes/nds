package br.com.abril.nds.integracao.engine.test;

import java.util.Date;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class TestFixedModelFooter {
	private Date date;
	
	@Field(offset=2, length=10)
	@FixedFormatPattern("dd/MM/yyyy")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
