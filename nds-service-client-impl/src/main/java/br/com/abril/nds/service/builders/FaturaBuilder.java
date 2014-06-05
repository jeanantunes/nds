package br.com.abril.nds.service.builders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalFatura;
import br.com.abril.nds.model.movimentacao.AbstractMovimentoEstoque;


public class FaturaBuilder {
	
	public static void montarFaturaNotaFiscal (NotaFiscal notaFiscal, List<? extends AbstractMovimentoEstoque> movimentosEstoque){
		
		List<NotaFiscalFatura> faturas = new ArrayList<NotaFiscalFatura>();
		
		NotaFiscalFatura fatura = new NotaFiscalFatura();
		
		if(notaFiscal == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Problemas ao gerar Nota Fiscal. Objeto nulo.");
		} else {
			/*if(notaFiscal.getNotaFiscalItens() == null) {
				notaFiscal.setNotaFiscalFatura(new ArrayList<NotaFiscalFatura>());
			}*/
		}
		
		for(AbstractMovimentoEstoque movimento : movimentosEstoque) {
			if(movimento instanceof MovimentoEstoqueCota) {
				fatura.setValor(((MovimentoEstoqueCota) movimento).getValoresAplicados().getPrecoComDesconto().multiply(new BigDecimal(movimento.getQtde())));
			}
			fatura.setVencimento(movimento.getData());
			fatura.setNumero("");
			faturas.add(fatura);
		}
		
		// popular os itens das notas fiscais
		//notaFiscal.setNotaFiscalFatura(faturas);
	}

	public static void montarFaturaNotaFiscal(NotaFiscal notaFiscal, Collection<MovimentoFechamentoFiscal> movimentosFechamentosFiscais) {
		// TODO Auto-generated method stub
		
	}
	
}
