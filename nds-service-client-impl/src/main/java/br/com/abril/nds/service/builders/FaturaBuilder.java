package br.com.abril.nds.service.builders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalFatura;


public class FaturaBuilder {
	
	public static void montarFaturaNotaFiscal(NotaFiscal notaFiscal) {
		
		List<NotaFiscalFatura> faturas = new ArrayList<NotaFiscalFatura>();
		
		NotaFiscalFatura fatura = new NotaFiscalFatura();
		
		if(notaFiscal == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Problemas ao gerar Nota Fiscal. Objeto nulo.");
		}
		
		if(notaFiscal.getNotaFiscalInformacoes() != null
				&& notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal() != null) {
			
			fatura.setValor(BigDecimal.ZERO);
			fatura.setVencimento(new Date());
			fatura.setNumero("");
			faturas.add(fatura);			
		}
		
		// popular os itens das notas fiscais
		//notaFiscal.setNotaFiscalFatura(faturas);
	}
	
}