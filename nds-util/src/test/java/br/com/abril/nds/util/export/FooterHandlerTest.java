package br.com.abril.nds.util.export;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

public class FooterHandlerTest {
	
	private List<ExportRow> rows;
	
	@Before
	public void setup() {
		
		this.rows = new ArrayList<>();
		
		//Simulando um relatório com 10 linhas. 
		//E 3 colunas que serão contabilizadas, cada uma com um tipo diferente de contabilização. 
		for (int i = 0; i < 10; i++) {
			
			ExportRow row = new ExportRow();
			
			List<ExportColumn> columns = new ArrayList<>();

			//Sumarização
			//resultado esperado = 20
			ExportFooterColumn column = new ExportFooterColumn();
			column.setValue("2");
			column.setName("sum");
			column.setLabel("somatorio");
			column.setFooterType(FooterType.SUM);
			columns.add(column);
			
			//Contagem
			//resultado esperado = 10
			column = new ExportFooterColumn();
			column.setValue("2");
			column.setName("count");
			column.setLabel("contagem");
			column.setFooterType(FooterType.COUNT);
			columns.add(column);

			//Media
			//resultado esperado = 2
			column = new ExportFooterColumn();
			column.setValue("2");
			column.setName("avg");
			column.setLabel("media");
			column.setFooterType(FooterType.AVG);
			columns.add(column);

			row.setColumns(columns);
			this.rows.add(row);
		} 
	}

	@Test
	public void processTestFull() {
		
		FooterHandler footerHandler = new FooterHandler(this.rows.size());
		
		 for (ExportRow row : this.rows) {
			 
			 footerHandler.process(row.getColumns());
		 }
		 
		 for (ExportFooter footer : footerHandler.get()) {
		 
			 switch(footer.getLabel()) {
			 
			 case "somatorio":
				 Assert.assertEquals("20", footer.getValue());
				 break;
			 case "contagem":
				 Assert.assertEquals("10", footer.getValue());
				 break;
			 case "media":
				 Assert.assertEquals("2", footer.getValue());
				 break;
			 }
		 }
	}
	
	@Test 
	public void processTestCountWithNullEmptyValues() {

		//Coluna com campo vazio não será contabilizado
		this.rows.get(0).getColumns().get(1).setValue(StringUtils.EMPTY);
		
		//Coluna com campo null não será contabilizado
		this.rows.get(1).getColumns().get(1).setValue(null);

		FooterHandler footerHandler = new FooterHandler(this.rows.size());
		
		for (ExportRow row : this.rows) {

			footerHandler.process(row.getColumns());
		}

		for (ExportFooter footer : footerHandler.get()) {

			switch (footer.getLabel()) {

			case "somatorio":
				Assert.assertEquals("20", footer.getValue());
				break;
			case "contagem":
				//2 itens a menos na contabilização da coluna.
				//Resultado esperado: 8.
				Assert.assertEquals("8", footer.getValue());
				break;
			case "media":
				Assert.assertEquals("2", footer.getValue());
				break;
			}
		}
	}
	
	@Test 
	public void processTestSumAvgPreventNullEmptyValues() {

		//Coluna com campo vazio não será sumarizado ou utilizado para gerar a média
		//Não ocorrerá problema de conversão, pois há o tratamento necessário.
		this.rows.get(0).getColumns().get(0).setValue(StringUtils.EMPTY);
		this.rows.get(0).getColumns().get(2).setValue(StringUtils.EMPTY);
		
		//Coluna com campo null não será contabilizado ou utilizado para gerar a média
		//Não ocorrerá problema de NullPointerException, pois há o tratamento necessário.
		this.rows.get(1).getColumns().get(0).setValue(null);
		this.rows.get(1).getColumns().get(2).setValue(null);

		FooterHandler footerHandler = new FooterHandler(this.rows.size());
		
		for (ExportRow row : this.rows) {

			footerHandler.process(row.getColumns());
		}

		for (ExportFooter footer : footerHandler.get()) {

			switch (footer.getLabel()) {

			case "somatorio":
				//2 itens a menos na sumarização da coluna.
				//O valor de cada coluna é 2, portanto o resultado esperado: 16.
				Assert.assertEquals("16", footer.getValue());
				break;
			case "contagem":
				Assert.assertEquals("10", footer.getValue());
				break;
			case "media":
				Assert.assertEquals("2", footer.getValue());
				break;
			}
		}
	}
}
