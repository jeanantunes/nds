package br.com.abril.nds.util.export.fiscal.nota;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoSecao;

public class NFEExporterTest {

	/**
	 * Testa todo o fluxo da classe NFEExport
	 * 
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@Test
	@Ignore
	public void notaFiscalToString() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		ClasseTest classeTest = preencheObjeto();
		
		NFEExporter nfeExporter = new NFEExporter();
		
		nfeExporter.clear();
		nfeExporter.execute(classeTest);
		String conteudoTXT = nfeExporter.gerarArquivo();  
		System.out.println(conteudoTXT);
		StringBuilder conteudoTXTEsperado = new StringBuilder();
		
		conteudoTXTEsperado.append("A|1,003.432|1002|2012-12|||2|\n")
				.append("C02|TESTE DE LISTA 1|2012-05-03|\n")
				.append("C02|TESTE DE LISTA 3|1992-01-25|\n")
				.append("C02|TESTE DE LISTA 3|1992-01-25|\n")
				.append("B|2012-12-15|S|1001.4321||1005|10|\n")
				.append("B13|2011-06-01|\n");

		Assert.assertEquals(conteudoTXTEsperado.toString(), conteudoTXT);
		
	}
	
	/**
	 * Preenche o Objeto de teste.
	 * 
	 * @return
	 */
	private ClasseTest preencheObjeto(){
		ClasseTest classeTest = new ClasseTest();
		SubClasseTest subClasseTest = new SubClasseTest();
		
		classeTest.setVarBigDecimal(new BigDecimal("1001.4321"));
		classeTest.setVarBigInteger(new BigInteger("1002"));
		classeTest.setVarDouble(new Double("1003.4321"));
		classeTest.setVarInteger(1004);
		classeTest.setVarLong(1005L);

		Date date = DateUtil.parseData("01/06/2011", "dd/MM/yyyy");
		subClasseTest.setVarDate(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtil.parseData("15/12/2012", "dd/MM/yyyy"));
		subClasseTest.setVarCalendar(calendar);
		subClasseTest.setVarCharacter('S');
		subClasseTest.setVarString("TESTE| DE GERACAO DE ARQUIVO TXT DA NFE");
		subClasseTest.setVarShort(new Short("10"));
		
		classeTest.setSubClasseTest(subClasseTest);
		
		ClasseListaTest classeListaTest = new ClasseListaTest();
		
		classeListaTest.setVarString("TESTE DE LISTA 1");
		classeListaTest.setVarDate(DateUtil.parseData("03/05/2012", "dd/MM/yyyy"));
		
		List<ClasseListaTest> listaClasseListaTests = new ArrayList<NFEExporterTest.ClasseListaTest>();
		
		listaClasseListaTests.add(classeListaTest);
		
		classeListaTest = new ClasseListaTest();
		
		classeListaTest.setVarString("TESTE DE LISTA 2");
		classeListaTest.setVarDate(DateUtil.parseData("03/06/1990", "dd/MM/yyyy"));
		
		listaClasseListaTests.add(classeListaTest);
		
		classeTest.setListaClasseListaTests(listaClasseListaTests);
		
		classeListaTest = new ClasseListaTest();
	
		classeListaTest.setVarString("TESTE DE LISTA 3");
		classeListaTest.setVarDate(DateUtil.parseData("25/01/1992", "dd/MM/yyyy"));
		
		listaClasseListaTests.add(classeListaTest);
		
		classeTest.setListaClasseListaTests(listaClasseListaTests);

		return classeTest;
	}

	/**
	 * Classe principal específica para testes.
	 * 
	 * @author Discover Technology
	 * 
	 */
	public class ClasseTest {

		 
		
		@NFEExport(secao = TipoSecao.B, posicao = 4)
		private Long varLong;

		@NFEExport(secao = TipoSecao.A, posicao = 0, mascara = "#,##0.000")
		private Double varDouble;

		@NFEExport(secao = TipoSecao.B, posicao = 2)
		private BigDecimal varBigDecimal;

		@NFEExport(secao = TipoSecao.A, posicao = 1)
		private BigInteger varBigInteger;

		@NFEExport(secao = TipoSecao.B13, posicao = 0, mascara = "00000")
		private Integer varInteger;

		private SubClasseTest subClasseTest;
		
		private List<ClasseListaTest> listaClasseListaTests;

		/** Getters and Setters **/

		/**
		 * @return a verção atual do layout
		 */
		@NFEExport(secao = TipoSecao.E05, posicao = 3)
		public Integer getVercao() {
			return 2;
		}

		/**
		 * @return the varLong
		 */
		public Long getVarLong() {
			return varLong;
		}

		/**
		 * @param varLong the varLong to set
		 */
		public void setVarLong(Long varLong) {
			this.varLong = varLong;
		}

		/**
		 * @return the varDouble
		 */
		public Double getVarDouble() {
			return varDouble;
		}

		/**
		 * @param varDouble the varDouble to set
		 */
		public void setVarDouble(Double varDouble) {
			this.varDouble = varDouble;
		}

		/**
		 * @return the varBigDecimal
		 */
		public BigDecimal getVarBigDecimal() {
			return varBigDecimal;
		}

		/**
		 * @param varBigDecimal the varBigDecimal to set
		 */
		public void setVarBigDecimal(BigDecimal varBigDecimal) {
			this.varBigDecimal = varBigDecimal;
		}

		/**
		 * @return the varBigInteger
		 */
		public BigInteger getVarBigInteger() {
			return varBigInteger;
		}

		/**
		 * @param varBigInteger the varBigInteger to set
		 */
		public void setVarBigInteger(BigInteger varBigInteger) {
			this.varBigInteger = varBigInteger;
		}

		/**
		 * @return the varInteger
		 */
		public Integer getVarInteger() {
			return varInteger;
		}

		/**
		 * @param varInteger the varInteger to set
		 */
		public void setVarInteger(Integer varInteger) {
			this.varInteger = varInteger;
		}

		/**
		 * @return the subClasseTest
		 */
		public SubClasseTest getSubClasseTest() {
			return subClasseTest;
		}

		/**
		 * @param subClasseTest the subClasseTest to set
		 */
		public void setSubClasseTest(SubClasseTest subClasseTest) {
			this.subClasseTest = subClasseTest;
		}

		/**
		 * @return the listaClasseListaTests
		 */
		public List<ClasseListaTest> getListaClasseListaTests() {
			return listaClasseListaTests;
		}

		/**
		 * @param listaClasseListaTests the listaClasseListaTests to set
		 */
		public void setListaClasseListaTests(List<ClasseListaTest> listaClasseListaTests) {
			this.listaClasseListaTests = listaClasseListaTests;
		}

	}

	/**
	 * Classe secundária específica para testes, e utilizada em um atributo da
	 * ClasseTest.
	 * 
	 * @author Discover Technology
	 * 
	 */
	public class SubClasseTest {

		@NFEExport(secao = TipoSecao.C02, posicao = 0)
		private Date varDate;

		@NFEExports(
				value = { 
						@NFEExport(secao = TipoSecao.B, posicao = 0),
						@NFEExport(secao = TipoSecao.D, posicao = 2, mascara = "yyyy-MM") 
						}
				)
		private Calendar varCalendar;

		@NFEExport(secao = TipoSecao.E05, posicao = 1, tamanho = 15)
		private String varString;

		@NFEExport(secao =TipoSecao.B, posicao = 1)
		private Character varCharacter;

		@NFEExports(
				value = { 
						@NFEExport(secao =TipoSecao.E05, posicao = 5),
						@NFEExport(secao = TipoSecao.D, posicao = 3) 
						}
				)
		private Byte varByte; 
		
		@NFEExport(secao = TipoSecao.B, posicao = 5)
		private Short varShort;
		
		@NFEExport(secao = TipoSecao.D, posicao = 4)
		private Float varFloat;

		/** Getters and Setters **/

		/**
		 * @return Tipo do Ambiente
		 */
		@NFEExport(secao = TipoSecao.D, posicao = 5)
		public Character getTipoAmbiente() {
			return '2';
		}

		/**
		 * @return the varDate
		 */
		public Date getVarDate() {
			return varDate;
		}

		/**
		 * @param varDate the varDate to set
		 */
		public void setVarDate(Date varDate) {
			this.varDate = varDate;
		}

		/**
		 * @return the varCalendar
		 */
		public Calendar getVarCalendar() {
			return varCalendar;
		}

		/**
		 * @param varCalendar the varCalendar to set
		 */
		public void setVarCalendar(Calendar varCalendar) {
			this.varCalendar = varCalendar;
		}

		/**
		 * @return the varString
		 */
		public String getVarString() {
			return varString;
		}

		/**
		 * @param varString the varString to set
		 */
		public void setVarString(String varString) {
			this.varString = varString;
		}

		/**
		 * @return the varCharacter
		 */
		public Character getVarCharacter() {
			return varCharacter;
		}

		/**
		 * @param varCharacter the varCharacter to set
		 */
		public void setVarCharacter(Character varCharacter) {
			this.varCharacter = varCharacter;
		}

		/**
		 * @return the varByte
		 */
		public Byte getVarByte() {
			return varByte;
		}

		/**
		 * @param varByte the varByte to set
		 */
		public void setVarByte(Byte varByte) {
			this.varByte = varByte;
		}

		/**
		 * @return the varShort
		 */
		public Short getVarShort() {
			return varShort;
		}

		/**
		 * @param varShort the varShort to set
		 */
		public void setVarShort(Short varShort) {
			this.varShort = varShort;
		}

		/**
		 * @return the varFloat
		 */
		public Float getVarFloat() {
			return varFloat;
		}

		/**
		 * @param varFloat the varFloat to set
		 */
		public void setVarFloat(Float varFloat) {
			this.varFloat = varFloat;
		}

	}
	
	public class ClasseListaTest{
		
		@NFEExport(secao =TipoSecao.C, posicao = 0)
		private String varString;
		
		@NFEExport(secao = TipoSecao.C, posicao = 1)
		private Date varDate;

		/**
		 * @return the varString
		 */
		public String getVarString() {
			return varString;
		}

		/**
		 * @param varString the varString to set
		 */
		public void setVarString(String varString) {
			this.varString = varString;
		}

		/**
		 * @return the varDate
		 */
		public Date getVarDate() {
			return varDate;
		}

		/**
		 * @param varDate the varDate to set
		 */
		public void setVarDate(Date varDate) {
			this.varDate = varDate;
		}
		
	}

}
