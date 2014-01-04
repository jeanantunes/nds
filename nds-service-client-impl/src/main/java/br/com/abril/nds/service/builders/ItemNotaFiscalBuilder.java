package br.com.abril.nds.service.builders;

import java.math.BigDecimal;
import java.util.ArrayList;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.fiscal.nfe.NotaFiscal;
import br.com.abril.nds.fiscal.nfe.NotaFiscalItem;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscalMovimentoEstoqueCota;

public class ItemNotaFiscalBuilder  {
	
	public static NotaFiscal montaItemNotaFiscal(NotaFiscal nota, MovimentoEstoqueCota movimentoEstoqueCota){
		
		NotaFiscalItem notaFiscalItem = new NotaFiscalItem();
		
		if(nota == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Problemas ao gerar Nota Fiscal. Objeto nulo.");
		} else {
			if(nota.getNotaFiscalItens() == null) {
				nota.setNotaFiscalItens(new ArrayList<NotaFiscalItem>());
			}
		}
		
		if(nota.getNotaFiscalItens().size() == 0) {
		
			notaFiscalItem.setCodigoItem(Long.valueOf(movimentoEstoqueCota.getProdutoEdicao().getProduto().getCodigo()));
			notaFiscalItem.setDescricao(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getDescricao());
			notaFiscalItem.setNCM(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getDescricao());
			notaFiscalItem.setQuantidade(movimentoEstoqueCota.getQtde());
			notaFiscalItem.setUnidade(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
			notaFiscalItem.setValorTotal(movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto().multiply(new BigDecimal(movimentoEstoqueCota.getQtde())));
			notaFiscalItem.setValorUnitario(movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto());
			notaFiscalItem.setOrigemItemNotaFiscal(new OrigemItemNotaFiscalMovimentoEstoqueCota());
			
			if(((OrigemItemNotaFiscalMovimentoEstoqueCota) notaFiscalItem.getOrigemItemNotaFiscal()).getListaMovimentoEstoqueCotas() == null) {
				((OrigemItemNotaFiscalMovimentoEstoqueCota) notaFiscalItem.getOrigemItemNotaFiscal()).setListaMovimentoEstoqueCotas(new ArrayList<MovimentoEstoqueCota>());
				((OrigemItemNotaFiscalMovimentoEstoqueCota) notaFiscalItem.getOrigemItemNotaFiscal()).getListaMovimentoEstoqueCotas().add(movimentoEstoqueCota);
			}
			
		} else {
			
			for(NotaFiscalItem nfi : nota.getNotaFiscalItens()) {
				
				boolean notFound = true;
				//if(nfi.getOrigemItemNotaFiscal().getOrigem().equals(OrigemItem.MOVIMENTO_ESTOQUE_COTA)) {
				for(MovimentoEstoqueCota mec : ((OrigemItemNotaFiscalMovimentoEstoqueCota) nfi.getOrigemItemNotaFiscal()).getListaMovimentoEstoqueCotas()) {
					if(mec.getProdutoEdicao().getId().equals(movimentoEstoqueCota.getProdutoEdicao().getId())) {
						
						notFound = false;
						
						notaFiscalItem.setCodigoItem(Long.valueOf(movimentoEstoqueCota.getProdutoEdicao().getProduto().getCodigo()));
						notaFiscalItem.setDescricao(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getDescricao());
						notaFiscalItem.setNCM(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getDescricao());
						notaFiscalItem.setQuantidade(notaFiscalItem.getQuantidade().add(movimentoEstoqueCota.getQtde()));
						notaFiscalItem.setUnidade(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
						
						BigDecimal valorTotalTemp = notaFiscalItem.getValorTotal();
						BigDecimal valorTotalNovoMec = movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto().multiply(new BigDecimal(movimentoEstoqueCota.getQtde()));
						
						notaFiscalItem.setValorTotal(valorTotalTemp.add(valorTotalNovoMec));
						
						notaFiscalItem.setValorUnitario(notaFiscalItem.getValorTotal().divide(new BigDecimal(notaFiscalItem.getQuantidade())));
						
					}
					
				}
				
				if(notFound) {
					notaFiscalItem.setCodigoItem(Long.valueOf(movimentoEstoqueCota.getProdutoEdicao().getProduto().getCodigo()));
					notaFiscalItem.setDescricao(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getDescricao());
					notaFiscalItem.setNCM(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getDescricao());
					notaFiscalItem.setQuantidade(movimentoEstoqueCota.getQtde());
					notaFiscalItem.setUnidade(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
					notaFiscalItem.setValorTotal(movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto().multiply(new BigDecimal(movimentoEstoqueCota.getQtde())));
					notaFiscalItem.setValorUnitario(movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto());
					notaFiscalItem.setOrigemItemNotaFiscal(new OrigemItemNotaFiscalMovimentoEstoqueCota());
					
					if(((OrigemItemNotaFiscalMovimentoEstoqueCota) notaFiscalItem.getOrigemItemNotaFiscal()).getListaMovimentoEstoqueCotas() == null) {
						((OrigemItemNotaFiscalMovimentoEstoqueCota) notaFiscalItem.getOrigemItemNotaFiscal()).setListaMovimentoEstoqueCotas(new ArrayList<MovimentoEstoqueCota>());
						((OrigemItemNotaFiscalMovimentoEstoqueCota) notaFiscalItem.getOrigemItemNotaFiscal()).getListaMovimentoEstoqueCotas().add(movimentoEstoqueCota);
					}
				}
			}
		}
		
		// popular os itens das notas fiscais
		nota.getNotaFiscalItens().add(notaFiscalItem);
		
		return nota;
	}
	
}