package br.com.abril.nds.util.export;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por gerenciar os dados de exportação que serão utilizados como rodapé. <br/>
 * <br/>
 * A partir dos dados que serão exportados, este componente realiza os devidos cálculos de acordo 
 * com o tipo definido na classe a ser exportada.
 * 
 * @author Discover Technology
 *
 */
public class FooterHandler {
	
	private ArrayList<FooterHelper> footerHelperList = new ArrayList<FooterHelper>();
	
	private ArrayList<ExportFooter> exportFooterList;
	
	private int offset = 1;
	
	private int limit;
	
	public FooterHandler(int limit) {
		
		this.limit = limit;
	}

	/**
	 * Realiza os cálculos das colunas exportadas, conforme definição do Bean.
	 * 
	 * @param exportColumns
	 */
	public void process(List<ExportColumn> exportColumns) {
		
		this.exportFooterList = new ArrayList<ExportFooter>();
		
		for (ExportColumn column : exportColumns) {
			
			if (column instanceof ExportFooterColumn) {
				
				ExportFooterColumn footerColumn = (ExportFooterColumn) column;
				
				FooterHelper helper = null;
				
				FooterHelper helperToCompare = new FooterHelper(footerColumn.getName());
				
				if (this.footerHelperList.contains(helperToCompare)) {

					int index = this.footerHelperList.indexOf(helperToCompare);
					
					helper = this.switchOperation(footerColumn, this.footerHelperList.get(index));
					
					this.footerHelperList.set(index, helper);
				
				} else {
					
					helper = this.switchOperation(footerColumn, new FooterHelper(footerColumn.getName()));
					
					this.footerHelperList.add(helper);
				}
				
				this.exportFooterList.add(toExportFooter(footerColumn, helper));
			}
		}
		
		this.offset++;
	}
	
	/**
	 * Retorna a lista com os footers já contabilizados.
	 * 
	 * @return List<ExportFooter>
	 */
	public List<ExportFooter> get() {

		return this.exportFooterList;
	}

	/**
	 * Realiza uma conversão para preparar os dados que serão utilizados na exportação.
	 * 
	 * @param column
	 * @param helper
	 * @return
	 */
	private ExportFooter toExportFooter(ExportFooterColumn column, FooterHelper helper) {
		
		ExportFooter exportFooter = new ExportFooter();
		
		exportFooter.setAlignment(column.getAlignment());
		exportFooter.setColumnType(column.getColumnType());
		exportFooter.setFontSize(column.getFontSize());
		exportFooter.setHeaderToAlign(column.getHeaderToAlign());
		exportFooter.setLabel(column.getLabel());
		exportFooter.setVerticalPrinting(column.isVerticalPrinting());
		exportFooter.setValue(ExportHandler.getExportValue(helper.getValue(), column.getColumnType()));

		return exportFooter;
	}
	
	/**
	 * Realiza o chaveamento do cálculo a ser feito na coluna, com base no tipo definido.
	 * 
	 * @param footerColumn
	 * @param helper
	 * @return
	 */
	private FooterHelper switchOperation(ExportFooterColumn footerColumn, FooterHelper helper) {
		
		String value = footerColumn.getValue();
		
		switch(footerColumn.getFooterType()) {

		case AVG:
			if (value == null || value.isEmpty()) {
				this.limit--;
				this.offset--;
			}
			return helper.avg(value, this.offset, this.limit);
		case COUNT:
			return helper.count(value);
		case SUM:
			return helper.sum(value);
		default:
			return helper.sum(value);
		}
	}
}
