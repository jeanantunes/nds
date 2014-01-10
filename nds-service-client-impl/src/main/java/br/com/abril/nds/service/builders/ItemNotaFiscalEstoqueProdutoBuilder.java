package br.com.abril.nds.service.builders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscal;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscalEstoque;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscalMovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.nfe.NotaFiscalItem;
import br.com.abril.nds.model.fiscal.nfe.NotaFiscalNds;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.ProdutoServico;

public class ItemNotaFiscalEstoqueProdutoBuilder  {
	
	private static ProdutoEdicao produtoEdicao;


	public static NotaFiscalNds montaItemNotaFiscalEstoqueProduto(NotaFiscalNds notaFiscal, EstoqueProduto estoqueProduto){
		
		NotaFiscalItem notaFiscalItem = new NotaFiscalItem();
		
		if(notaFiscal == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Problemas ao gerar Nota Fiscal. Objeto nulo.");
		} else {
			if(notaFiscal.getNotaFiscalItens() == null) {
				notaFiscal.setNotaFiscalItens(new ArrayList<NotaFiscalItem>());
			}
		}
		
		if(notaFiscal.getNotaFiscalItens().size() == 0) {
		
			notaFiscalItem.setCodigoItem(Long.valueOf(estoqueProduto.getProdutoEdicao().getProduto().getCodigo()));
			notaFiscalItem.setDescricao(estoqueProduto.getProdutoEdicao().getProduto().getTipoProduto().getDescricao());
			notaFiscalItem.setNCM(estoqueProduto.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getDescricao());
			notaFiscalItem.setQuantidade(estoqueProduto.getQtde());
			notaFiscalItem.setUnidade(estoqueProduto.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
			notaFiscalItem.setValorTotal(estoqueProduto.getProdutoEdicao().getDesconto().multiply(new BigDecimal(estoqueProduto.getQtde())));
			notaFiscalItem.setValorUnitario(estoqueProduto.getProdutoEdicao().getDesconto());
			notaFiscalItem.setCST(estoqueProduto.getProdutoEdicao().getProduto().getTributacaoFiscal().getCST());
			//estoqueProduto.setNotaFiscalEmitida(true);
			
			List<OrigemItemNotaFiscal> origemItens = notaFiscalItem.getOrigemItemNotaFiscal() != null ? notaFiscalItem.getOrigemItemNotaFiscal() : new ArrayList<OrigemItemNotaFiscal>();
			
			OrigemItemNotaFiscal oinf = new OrigemItemNotaFiscalEstoque();
			((OrigemItemNotaFiscalEstoque) oinf).setProdutoEdicao(estoqueProduto.getProdutoEdicao());
			origemItens.add(oinf);
			notaFiscalItem.setOrigemItemNotaFiscal(origemItens);
			
		} else {
			
			for(NotaFiscalItem nfi : notaFiscal.getNotaFiscalItens()) {
				
				boolean notFound = true;

				for(OrigemItemNotaFiscal oinf : nfi.getOrigemItemNotaFiscal()) {
					
					MovimentoEstoqueCota mec = ((OrigemItemNotaFiscalMovimentoEstoqueCota) oinf).getMovimentoEstoqueCota();
					if(mec.getProdutoEdicao().getId().equals(estoqueProduto.getProdutoEdicao().getId())) {
						
						notFound = false;
						
						notaFiscalItem.setCodigoItem(Long.valueOf(estoqueProduto.getProdutoEdicao().getProduto().getCodigo()));
						notaFiscalItem.setDescricao(estoqueProduto.getProdutoEdicao().getProduto().getTipoProduto().getDescricao());
						notaFiscalItem.setNCM(estoqueProduto.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getDescricao());
						notaFiscalItem.setQuantidade(notaFiscalItem.getQuantidade().add(estoqueProduto.getQtde()));
						notaFiscalItem.setUnidade(estoqueProduto.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
						notaFiscalItem.setCST(estoqueProduto.getProdutoEdicao().getProduto().getTributacaoFiscal().getCST());
						
						BigDecimal valorTotalTemp = notaFiscalItem.getValorTotal();
						BigDecimal valorTotalNovoMec = estoqueProduto.getProdutoEdicao().getPrecoCusto().multiply(new BigDecimal(estoqueProduto.getQtde()));
						
						notaFiscalItem.setValorTotal(valorTotalTemp.add(valorTotalNovoMec));
						
						notaFiscalItem.setValorUnitario(notaFiscalItem.getValorTotal().divide(new BigDecimal(notaFiscalItem.getQuantidade())));
						
						mec.setNotaFiscalEmitida(true);
						
					}
					
				}
				
				if(notFound) {
					notaFiscalItem.setCodigoItem(Long.valueOf(estoqueProduto.getProdutoEdicao().getProduto().getCodigo()));
					notaFiscalItem.setDescricao(estoqueProduto.getProdutoEdicao().getProduto().getTipoProduto().getDescricao());
					notaFiscalItem.setNCM(estoqueProduto.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getDescricao());
					notaFiscalItem.setQuantidade(estoqueProduto.getQtde());
					notaFiscalItem.setUnidade(estoqueProduto.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
					notaFiscalItem.setCST(estoqueProduto.getProdutoEdicao().getProduto().getTributacaoFiscal().getCST());
					notaFiscalItem.setValorTotal(estoqueProduto.getProdutoEdicao().getPrecoCusto().multiply(new BigDecimal(estoqueProduto.getQtde())));
					notaFiscalItem.setValorUnitario(estoqueProduto.getProdutoEdicao().getDesconto());
					
					List<OrigemItemNotaFiscal> origemItens = notaFiscalItem.getOrigemItemNotaFiscal() != null ? notaFiscalItem.getOrigemItemNotaFiscal() : new ArrayList<OrigemItemNotaFiscal>();
					
					OrigemItemNotaFiscal oinf = new OrigemItemNotaFiscalEstoque();
					((OrigemItemNotaFiscalEstoque) oinf).setProdutoEdicao(estoqueProduto.getProdutoEdicao());
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
	
	
	public static void montaItemNotaFiscal(NotaFiscal notaFiscal2, EstoqueProduto estoqueProduto) {

		ProdutoServico notaFiscalItem = new ProdutoServico();
		
		if(notaFiscal2 == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Problemas ao gerar Nota Fiscal. Objeto nulo.");
		} else {
			if(notaFiscal2.getProdutosServicos() == null) {
				notaFiscal2.setProdutosServicos(new ArrayList<ProdutoServico>());
			}
		}
		
		if(notaFiscal2.getProdutosServicos().size() == 0) {
		
			notaFiscalItem.setCodigoProduto(estoqueProduto.getProdutoEdicao().getProduto().getCodigo());
			notaFiscalItem.setDescricaoProduto(estoqueProduto.getProdutoEdicao().getProduto().getTipoProduto().getDescricao());
			notaFiscalItem.setNcm(estoqueProduto.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getCodigo());
			notaFiscalItem.setQuantidade(estoqueProduto.getQtde());
			notaFiscalItem.setUnidade(estoqueProduto.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
			notaFiscalItem.setValorTotalBruto(estoqueProduto.getProdutoEdicao().getDesconto().multiply(new BigDecimal(estoqueProduto.getQtde())));
			notaFiscalItem.setValorUnitario(estoqueProduto.getProdutoEdicao().getDesconto());
			
			List<OrigemItemNotaFiscal> origemItens = notaFiscalItem.getOrigemItemNotaFiscal() != null ? notaFiscalItem.getOrigemItemNotaFiscal() : new ArrayList<OrigemItemNotaFiscal>();
			
			OrigemItemNotaFiscal oinf = new OrigemItemNotaFiscalEstoque();
			((OrigemItemNotaFiscalEstoque) oinf).setProdutoEdicao(estoqueProduto.getProdutoEdicao());
			origemItens.add(oinf);
			notaFiscalItem.setOrigemItemNotaFiscal(origemItens);
			
		} else {
			
			for(ProdutoServico nfi : notaFiscal2.getProdutosServicos()) {
				
				boolean notFound = true;

				for(OrigemItemNotaFiscal oinf : nfi.getOrigemItemNotaFiscal()) {
					
					produtoEdicao = ((OrigemItemNotaFiscalEstoque) oinf).getProdutoEdicao();
					if(produtoEdicao.getId().equals(estoqueProduto.getProdutoEdicao().getId())) {
						
						notFound = false;
						
						notaFiscalItem.setCodigoProduto(produtoEdicao.getProduto().getCodigo());
						notaFiscalItem.setDescricaoProduto(produtoEdicao.getProduto().getTipoProduto().getDescricao());
						notaFiscalItem.setNcm(produtoEdicao.getProduto().getTipoProduto().getNcm().getCodigo());
						notaFiscalItem.setQuantidade(notaFiscalItem.getQuantidade().add(estoqueProduto.getQtde()));
						notaFiscalItem.setUnidade(estoqueProduto.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
						
						BigDecimal valorTotalTemp = notaFiscalItem.getValorTotalBruto();
						BigDecimal valorTotalNovoMec = estoqueProduto.getProdutoEdicao().getPrecoVenda().multiply(new BigDecimal(estoqueProduto.getQtde()));
						
						notaFiscalItem.setValorTotalBruto(valorTotalTemp.add(valorTotalNovoMec));
						
						notaFiscalItem.setValorUnitario(notaFiscalItem.getValorTotalBruto().divide(new BigDecimal(notaFiscalItem.getQuantidade())));
					}
					
				}
				
				if(notFound) {
					
					notaFiscalItem.setCodigoProduto(estoqueProduto.getProdutoEdicao().getProduto().getCodigo());
					notaFiscalItem.setDescricaoProduto(estoqueProduto.getProdutoEdicao().getProduto().getTipoProduto().getDescricao());
					notaFiscalItem.setNcm(estoqueProduto.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getCodigo());
					notaFiscalItem.setQuantidade(estoqueProduto.getQtde());
					notaFiscalItem.setUnidade(estoqueProduto.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
					notaFiscalItem.setValorTotalBruto(estoqueProduto.getProdutoEdicao().getPrecoCusto().multiply(new BigDecimal(estoqueProduto.getQtde())));
					notaFiscalItem.setValorUnitario(estoqueProduto.getProdutoEdicao().getPrecoCusto());
					// movimentoEstoqueCota.setNotaFiscalEmitida(true);
					
					List<OrigemItemNotaFiscal> origemItens = notaFiscalItem.getOrigemItemNotaFiscal() != null ? notaFiscalItem.getOrigemItemNotaFiscal() : new ArrayList<OrigemItemNotaFiscal>();
					
					OrigemItemNotaFiscal oinf = new OrigemItemNotaFiscalEstoque();
					((OrigemItemNotaFiscalEstoque) oinf).setProdutoEdicao(estoqueProduto.getProdutoEdicao());
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