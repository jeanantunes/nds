package br.com.abril.nds;

import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class GenericTest {

	@Test
	public void test() {
		LocalDate date = LocalDate.parse("2012-03-29");
		date.plus(Years.ONE);
		date.plus(Years.ONE).isAfter(LocalDate.now());
	}
	
}
