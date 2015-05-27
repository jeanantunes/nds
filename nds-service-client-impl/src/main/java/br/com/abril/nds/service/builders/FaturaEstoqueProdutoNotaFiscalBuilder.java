package br.com.abril.nds.service.builders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalFatura;

public class FaturaEstoqueProdutoNotaFiscalBuilder {
	public static void montarFaturaEstoqueProdutoNotaFiscal (NotaFiscal notaFiscal, List<EstoqueProduto> estoquesProdutos){
		
		List<NotaFiscalFatura> faturas = new ArrayList<NotaFiscalFatura>();
		
		NotaFiscalFatura fatura = new NotaFiscalFatura();
		
		if(notaFiscal == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Problemas ao gerar Nota Fiscal. Objeto nulo.");
		}
		
		for(EstoqueProduto estoqueProduto : estoquesProdutos) {
			fatura.setValor(estoqueProduto.getProdutoEdicao().getPrecoCusto().multiply(new BigDecimal(estoqueProduto.getQtde())));
			fatura.setVencimento(new Date());
			fatura.setNumero("");
			faturas.add(fatura);
		}
		
		// popular os itens das notas fiscais
		// notaFiscal.setNotaFiscalFatura(faturas);
	}
}

