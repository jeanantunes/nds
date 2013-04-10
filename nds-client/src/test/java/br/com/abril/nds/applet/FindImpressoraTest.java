package br.com.abril.nds.applet;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import javax.print.PrintException;
import javax.print.PrintService;

import org.apache.poi.util.IOUtils;

import br.com.abril.nds.matricial.ConstantesImpressao;
import br.com.abril.nds.matricial.EmissorNotaFiscalMatricial;
import br.com.abril.nds.service.impl.ConferenciaEncalheServiceImpl;


public class FindImpressoraTest {

	public static void main(String[] args) {
		
		getBoletoTest();  
		
	}


	private static void getBoletoTest()  {
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader("C:\\arquivos_cobranca_boleto.pdf"));
			String line;  
			while ((line = in.readLine()) != null) {  

				System.out.println(line);  
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}


	private static void imprimirDuasImpressorasTest() {
		
		PrintService Gneric = getImpressoraByName("\\\\10.37.28.166\\Generic / Text Only");
		PrintService HDU16400 = getImpressoraByName("\\\\abdcw2k305\\HDU16400");
		
		
		if(HDU16400 != null){
			EmissorNotaFiscalMatricial emissorNotaFiscalMatricial = new EmissorNotaFiscalMatricial(null);
			try {
				
				byte[] boleto = IOUtils.toByteArray(new FileInputStream(new File("C:\\arquivos_cobranca_boleto.pdf")));
				byte[] bytesSlip = getDadosSlipMock().getBytes();
				
				emissorNotaFiscalMatricial.imprimir(bytesSlip, Gneric);
				emissorNotaFiscalMatricial.imprimir(boleto, HDU16400);
				
			} catch (PrintException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("Impressora não localizada");
		}
	}

	
	public static String getDadosSlipMock() {
		ConferenciaEncalheServiceImpl conf = new ConferenciaEncalheServiceImpl();
		conf.setSlipDTO(new ImpressaoFinalizacaoEncalheTest().getSlipDtoMock());
		String saida = conf.gerarSlipTxtMatricial().toString();
		saida = saida.replaceAll(ConstantesImpressao.CARACTER_INDENT_LINEFEED_SCAPE, ConstantesImpressao.CR + ConstantesImpressao.LINE_FEED);
		return saida;
	}
	
	public static PrintService getImpressoraByName(String nomeImpressora){
		PrintService[] lookupPrintServices = PrinterJob.lookupPrintServices();
		for(PrintService service : lookupPrintServices){
			
			if(service!=null && nomeImpressora != null && nomeImpressora.equals(service.getName())){
				return  service;
			}
		}
		return null;
	}

}
