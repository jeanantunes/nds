package br.com.abril.nds.applet;


import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.print.PrintException;

import br.com.abril.nds.dto.ProdutoEdicaoSlipDTO;
import br.com.abril.nds.dto.SlipDTO;
import br.com.abril.nds.matricial.ConstantesImpressao;
import br.com.abril.nds.matricial.EmissorNotaFiscalMatricial;
import br.com.abril.nds.service.impl.ConferenciaEncalheServiceImpl;

public class ImpressaoFinalizacaoEncalheTest {

	public static void main(String[] args) {
		try {
			new ImpressaoFinalizacaoEncalheTest().gerarSlipTxtMatricialTest();
		} catch (PrintException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void gerarSlipTxtMatricialTest() throws PrintException, IOException{
		ConferenciaEncalheServiceImpl conf = new ConferenciaEncalheServiceImpl();
		conf.setSlipDTO(getSlipDtoMock());
		String saida = conf.gerarSlipTxtMatricial();
		saida = saida.replaceAll(ConstantesImpressao.CARACTER_INDENT_LINEFEED_SCAPE, ConstantesImpressao.CR + ConstantesImpressao.LINE_FEED);
		
        new EmissorNotaFiscalMatricial(null).imprimir(saida);
		
//		System.out.println("#########>>>>>>>>>>>>>>>");
//		System.out.println(saida);
//		System.out.println("<<<<<<<<<<<<<<<<###############");
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
