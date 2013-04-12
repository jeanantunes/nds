package br.com.abril.nds.applet;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.print.PrintException;
import javax.print.PrintService;

import org.apache.poi.util.IOUtils;

import br.com.abril.nds.dto.ProdutoEdicaoSlipDTO;
import br.com.abril.nds.dto.SlipDTO;
import br.com.abril.nds.service.impl.ConferenciaEncalheServiceImpl;
import br.com.abril.nds.util.ImpressaoConstantes;
import br.com.abril.nds.util.ImpressaoMatricialUtil;
import br.com.abril.nds.util.ImpressoraUtil;

public class ImpressaoFinalizacaoEncalheTest {

	public static void main(String[] args) {
		try {
			new ImpressaoFinalizacaoEncalheTest().gerarSlipTxtMatricialTest();
		} catch (PrintException | IOException e) {
			e.printStackTrace();
		}
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
		
		PrintService Gneric = ImpressoraUtil.getImpressoraByName("\\\\10.37.28.166\\Generic / Text Only");
		PrintService HDU16400 = ImpressoraUtil.getImpressoraByName("\\\\abdcw2k305\\HDU16400");
		
		
		if(HDU16400 != null){
			ImpressaoMatricialUtil emissorNotaFiscalMatricial = new ImpressaoMatricialUtil(null);
			try {
				
				byte[] boleto = IOUtils.toByteArray(new FileInputStream(new File("C:\\arquivos_cobranca_boleto.pdf")));
				byte[] bytesSlip = getDadosSlipMock().getBytes();
				
				ImpressoraUtil impressoraUtil = new ImpressoraUtil();
				impressoraUtil.imprimir(bytesSlip, Gneric);
				impressoraUtil.imprimir(boleto, HDU16400);
				
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
		saida = saida.replaceAll(ImpressaoConstantes.CARACTER_INDENT_LINEFEED_SCAPE, ImpressaoConstantes.CR + ImpressaoConstantes.LINE_FEED);
		return saida;
	}
	
	public void gerarSlipTxtMatricialTest() throws PrintException, IOException{
        new ImpressaoMatricialUtil(null).imprimirImpressoraPadrao(getDadosSlipMock());
	}

	public SlipDTO getSlipDtoMock() {
		SlipDTO slipDTO = new SlipDTO();
		slipDTO.setNumeroCota(2);
		slipDTO.setNomeCota("ANGELO FALSARELLA - ME");
		slipDTO.setDataConferencia(new Date());
		slipDTO.setCodigoBox("800");
		slipDTO.setNumSlip(null);
		
		
		ProdutoEdicaoSlipDTO dto1 = new ProdutoEdicaoSlipDTO();
		dto1.setOrdinalDiaConferenciaEncalhe("0");
		dto1.setNomeProduto("NOVA ESCOLA");
		dto1.setNumeroEdicao(259l);
		dto1.setReparte(new BigInteger("7"));
		dto1.setEncalhe(new BigInteger("6"));
		dto1.setPrecoVenda(new BigDecimal("3.4645458"));
		dto1.setValorTotal(new BigDecimal("20.7988588"));
		dto1.setDataRecolhimento(new Date());
		

		ProdutoEdicaoSlipDTO dto2 = new ProdutoEdicaoSlipDTO();
		dto2.setOrdinalDiaConferenciaEncalhe("0");
		dto2.setNomeProduto("RECREIO");
		dto2.setNumeroEdicao(677l);
		dto2.setReparte(new BigInteger("10"));
		dto2.setEncalhe(new BigInteger("10"));
		dto2.setPrecoVenda(new BigDecimal("7.7045458"));
		dto2.setValorTotal(new BigDecimal("77.00"));
		dto2.setDataRecolhimento(new Date());
		
		List<ProdutoEdicaoSlipDTO> lista = new ArrayList<ProdutoEdicaoSlipDTO>();
		lista.add(dto1);
		lista.add(dto2);
		
		slipDTO.setListaProdutoEdicaoSlipDTO(lista);
		
		return slipDTO;
	}
}
