package br.com.abril.nds.service.builders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscal;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscalMovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.nfe.NotaFiscalItem;
import br.com.abril.nds.model.fiscal.nfe.NotaFiscalNds;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.ProdutoServico;

public class ItemNotaFiscalBuilder  {
	
	public static NotaFiscalNds montaItemNotaFiscal(NotaFiscalNds notaFiscal, MovimentoEstoqueCota movimentoEstoqueCota){
		
		NotaFiscalItem notaFiscalItem = new NotaFiscalItem();
		
		if(notaFiscal == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Problemas ao gerar Nota Fiscal. Objeto nulo.");
		} else {
			if(notaFiscal.getNotaFiscalItens() == null) {
				notaFiscal.setNotaFiscalItens(new ArrayList<NotaFiscalItem>());
			}
		}
		
		if(notaFiscal.getNotaFiscalItens().size() == 0) {
		
			notaFiscalItem.setCodigoItem(Long.valueOf(movimentoEstoqueCota.getProdutoEdicao().getProduto().getCodigo()));
			notaFiscalItem.setDescricao(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getDescricao());
			notaFiscalItem.setNCM(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getDescricao());
			notaFiscalItem.setQuantidade(movimentoEstoqueCota.getQtde());
			notaFiscalItem.setUnidade(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
			notaFiscalItem.setValorTotal(movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto().multiply(new BigDecimal(movimentoEstoqueCota.getQtde())));
			notaFiscalItem.setValorUnitario(movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto());
			notaFiscalItem.setCST(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTributacaoFiscal().getCST());
			movimentoEstoqueCota.setNotaFiscalEmitida(true);
			
			List<OrigemItemNotaFiscal> origemItens = notaFiscalItem.getOrigemItemNotaFiscal() != null ? notaFiscalItem.getOrigemItemNotaFiscal() : new ArrayList<OrigemItemNotaFiscal>();
			
			OrigemItemNotaFiscal oinf = new OrigemItemNotaFiscalMovimentoEstoqueCota();
			((OrigemItemNotaFiscalMovimentoEstoqueCota) oinf).setMovimentoEstoqueCota(movimentoEstoqueCota);
			origemItens.add(oinf);
			notaFiscalItem.setOrigemItemNotaFiscal(origemItens);
			
		} else {
			
			for(NotaFiscalItem nfi : notaFiscal.getNotaFiscalItens()) {
				
				boolean notFound = true;

				for(OrigemItemNotaFiscal oinf : nfi.getOrigemItemNotaFiscal()) {
					
					MovimentoEstoqueCota mec = ((OrigemItemNotaFiscalMovimentoEstoqueCota) oinf).getMovimentoEstoqueCota();
					if(mec.getProdutoEdicao().getId().equals(movimentoEstoqueCota.getProdutoEdicao().getId())) {
						
						notFound = false;
						
						notaFiscalItem.setCodigoItem(Long.valueOf(movimentoEstoqueCota.getProdutoEdicao().getProduto().getCodigo()));
						notaFiscalItem.setDescricao(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getDescricao());
						notaFiscalItem.setNCM(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getDescricao());
						notaFiscalItem.setQuantidade(notaFiscalItem.getQuantidade().add(movimentoEstoqueCota.getQtde()));
						notaFiscalItem.setUnidade(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
						notaFiscalItem.setCST(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTributacaoFiscal().getCST());
						
						BigDecimal valorTotalTemp = notaFiscalItem.getValorTotal();
						BigDecimal valorTotalNovoMec = movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto().multiply(new BigDecimal(movimentoEstoqueCota.getQtde()));
						
						notaFiscalItem.setValorTotal(valorTotalTemp.add(valorTotalNovoMec));
						
						notaFiscalItem.setValorUnitario(notaFiscalItem.getValorTotal().divide(new BigDecimal(notaFiscalItem.getQuantidade())));
						
						mec.setNotaFiscalEmitida(true);
						
					}
					
				}
				
				if(notFound) {
					notaFiscalItem.setCodigoItem(Long.valueOf(movimentoEstoqueCota.getProdutoEdicao().getProduto().getCodigo()));
					notaFiscalItem.setDescricao(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getDescricao());
					notaFiscalItem.setNCM(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getDescricao());
					notaFiscalItem.setQuantidade(movimentoEstoqueCota.getQtde());
					notaFiscalItem.setUnidade(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
					notaFiscalItem.setCST(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTributacaoFiscal().getCST());
					notaFiscalItem.setValorTotal(movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto().multiply(new BigDecimal(movimentoEstoqueCota.getQtde())));
					notaFiscalItem.setValorUnitario(movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto());
					
					movimentoEstoqueCota.setNotaFiscalEmitida(true);
					
					List<OrigemItemNotaFiscal> origemItens = notaFiscalItem.getOrigemItemNotaFiscal() != null ? notaFiscalItem.getOrigemItemNotaFiscal() : new ArrayList<OrigemItemNotaFiscal>();
					
					OrigemItemNotaFiscal oinf = new OrigemItemNotaFiscalMovimentoEstoqueCota();
					((OrigemItemNotaFiscalMovimentoEstoqueCota) oinf).setMovimentoEstoqueCota(movimentoEstoqueCota);
					origemItens.add(oinf);
					notaFiscalItem.setOrigemItemNotaFiscal(origemItens);
				}
			}
		}
		
		// popular os itens das notas fiscais
		notaFiscalItem.setNotaFiscal(notaFiscal);
		notaFiscal.getNotaFiscalItens().add(notaFiscalItem);
		
		return notaFiscal;
	}

	public static void montaItemNotaFiscal(NotaFiscal notaFiscal2, MovimentoEstoqueCota movimentoEstoqueCota) {

		ProdutoServico notaFiscalItem = new ProdutoServico();
		
		if(notaFiscal2 == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Problemas ao gerar Nota Fiscal. Objeto nulo.");
		} else {
			if(notaFiscal2.getProdutosServicos() == null) {
				notaFiscal2.setProdutosServicos(new ArrayList<ProdutoServico>());
			}
		}
		
		if(notaFiscal2.getProdutosServicos().size() == 0) {
		
			notaFiscalItem.setCodigoProduto(movimentoEstoqueCota.getProdutoEdicao().getProduto().getCodigo());
			notaFiscalItem.setDescricaoProduto(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getDescricao());
			notaFiscalItem.setNcm(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getCodigo());
			notaFiscalItem.setQuantidade(movimentoEstoqueCota.getQtde());
			notaFiscalItem.setUnidade(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
			notaFiscalItem.setValorTotalBruto(movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto().multiply(new BigDecimal(movimentoEstoqueCota.getQtde())));
			notaFiscalItem.setValorUnitario(movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto());
			movimentoEstoqueCota.setNotaFiscalEmitida(true);
			
			List<OrigemItemNotaFiscal> origemItens = notaFiscalItem.getOrigemItemNotaFiscal() != null ? notaFiscalItem.getOrigemItemNotaFiscal() : new ArrayList<OrigemItemNotaFiscal>();
			
			OrigemItemNotaFiscal oinf = new OrigemItemNotaFiscalMovimentoEstoqueCota();
			((OrigemItemNotaFiscalMovimentoEstoqueCota) oinf).setMovimentoEstoqueCota(movimentoEstoqueCota);
			origemItens.add(oinf);
			notaFiscalItem.setOrigemItemNotaFiscal(origemItens);
			
		} else {
			
			for(ProdutoServico nfi : notaFiscal2.getProdutosServicos()) {
				
				boolean notFound = true;

				for(OrigemItemNotaFiscal oinf : nfi.getOrigemItemNotaFiscal()) {
					
					MovimentoEstoqueCota mec = ((OrigemItemNotaFiscalMovimentoEstoqueCota) oinf).getMovimentoEstoqueCota();
					if(mec.getProdutoEdicao().getId().equals(movimentoEstoqueCota.getProdutoEdicao().getId())) {
						
						notFound = false;
						
						notaFiscalItem.setCodigoProduto(movimentoEstoqueCota.getProdutoEdicao().getProduto().getCodigo());
						notaFiscalItem.setDescricaoProduto(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getDescricao());
						notaFiscalItem.setNcm(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getCodigo());
						notaFiscalItem.setQuantidade(notaFiscalItem.getQuantidade().add(movimentoEstoqueCota.getQtde()));
						notaFiscalItem.setUnidade(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
						
						BigDecimal valorTotalTemp = notaFiscalItem.getValorTotalBruto();
						BigDecimal valorTotalNovoMec = movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto().multiply(new BigDecimal(movimentoEstoqueCota.getQtde()));
						
						notaFiscalItem.setValorTotalBruto(valorTotalTemp.add(valorTotalNovoMec));
						
						notaFiscalItem.setValorUnitario(notaFiscalItem.getValorTotalBruto().divide(new BigDecimal(notaFiscalItem.getQuantidade())));
						
						mec.setNotaFiscalEmitida(true);
						
					}
					
				}
				
				if(notFound) {
					
					notaFiscalItem.setCodigoProduto(movimentoEstoqueCota.getProdutoEdicao().getProduto().getCodigo());
					notaFiscalItem.setDescricaoProduto(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getDescricao());
					notaFiscalItem.setNcm(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getCodigo());
					notaFiscalItem.setQuantidade(movimentoEstoqueCota.getQtde());
					notaFiscalItem.setUnidade(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
					notaFiscalItem.setValorTotalBruto(movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto().multiply(new BigDecimal(movimentoEstoqueCota.getQtde())));
					notaFiscalItem.setValorUnitario(movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto());
					movimentoEstoqueCota.setNotaFiscalEmitida(true);
					
					List<OrigemItemNotaFiscal> origemItens = notaFiscalItem.getOrigemItemNotaFiscal() != null ? notaFiscalItem.getOrigemItemNotaFiscal() : new ArrayList<OrigemItemNotaFiscal>();
					
					OrigemItemNotaFiscal oinf = new OrigemItemNotaFiscalMovimentoEstoqueCota();
					((OrigemItemNotaFiscalMovimentoEstoqueCota) oinf).setMovimentoEstoqueCota(movimentoEstoqueCota);
					origemItens.add(oinf);
					notaFiscalItem.setOrigemItemNotaFiscal(origemItens);
					
				}
			}
		}
		
		// popular os itens das notas fiscais
		// notaFiscalItem.setNotaFiscal(notaFiscal2);
		notaFiscal2.getProdutosServicos().add(notaFiscalItem);
		
	}
	
}