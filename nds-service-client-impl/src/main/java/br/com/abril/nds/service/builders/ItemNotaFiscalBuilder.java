package br.com.abril.nds.service.builders;

import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nfe.model.NaturezaOperacao;
import br.com.abril.nfe.model.NotaFiscal;
import br.com.abril.nfe.model.NotaFiscalItem;

public class ItemNotaFiscalBuilder  {
	
	public static NotaFiscal montaItemNotaFiscal (NotaFiscal nota, MovimentoEstoqueCota movimentosEstoqueCota){
		
		NotaFiscalItem notaFiscalItem = new NotaFiscalItem();
		
		// if(nota.getNaturezaOperacao()){
						
			notaFiscalItem.setCodigoItem(Long.valueOf(movimentosEstoqueCota.getProdutoEdicao().getProduto().getCodigo()));
			notaFiscalItem.setDescricao(movimentosEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getDescricao());
			notaFiscalItem.setNCM(movimentosEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getDescricao());
			notaFiscalItem.setQuantidade(movimentosEstoqueCota.getQtde());
			notaFiscalItem.setUnidade(movimentosEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
			notaFiscalItem.setValorTotal(movimentosEstoqueCota.getProdutoEdicao().getPrecoCusto());
			notaFiscalItem.setValorUnitario(movimentosEstoqueCota.getProdutoEdicao().getPrecoCusto());
		// }
		
		// popular os itens das notas fiscais
		nota.getNotaFiscalitens().add(notaFiscalItem);
		return nota;
	}
	
}
