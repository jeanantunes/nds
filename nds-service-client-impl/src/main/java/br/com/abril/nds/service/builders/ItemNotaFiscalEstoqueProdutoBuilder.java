package br.com.abril.nds.service.builders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TributoAliquota;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscal;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscalEstoque;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscalMovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.nfe.NotaFiscalItem;
import br.com.abril.nds.model.fiscal.nfe.NotaFiscalNds;
import br.com.abril.nds.model.fiscal.nota.DetalheNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.ProdutoServico;

public class ItemNotaFiscalEstoqueProdutoBuilder  {
	
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
	
	
	public static void montaItemNotaFiscal(NotaFiscal notaFiscal, EstoqueProduto estoqueProduto, Map<String, TributoAliquota> tributoRegimeTributario) {

		ProdutoEdicao produtoEdicao;
		ProdutoServico produtoServico = new ProdutoServico();
		DetalheNotaFiscal detalheNotaFiscal = new DetalheNotaFiscal(produtoServico);
		
		if(notaFiscal == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Problemas ao gerar Nota Fiscal. Objeto nulo.");
		} else {
			if(notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal() == null) {
				notaFiscal.getNotaFiscalInformacoes().setDetalhesNotaFiscal(new ArrayList<DetalheNotaFiscal>());
			}
		}
		
		if(notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().size() == 0) {
		
			detalheNotaFiscal.getProdutoServico().setCodigoProduto(estoqueProduto.getProdutoEdicao().getProduto().getCodigo());
			detalheNotaFiscal.getProdutoServico().setDescricaoProduto(estoqueProduto.getProdutoEdicao().getProduto().getTipoProduto().getDescricao());
			detalheNotaFiscal.getProdutoServico().setNcm(estoqueProduto.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getCodigo());
			detalheNotaFiscal.getProdutoServico().setQuantidade(estoqueProduto.getQtde());
			detalheNotaFiscal.getProdutoServico().setUnidade(estoqueProduto.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
			detalheNotaFiscal.getProdutoServico().setValorTotalBruto(estoqueProduto.getProdutoEdicao().getDesconto().multiply(new BigDecimal(estoqueProduto.getQtde())));
			detalheNotaFiscal.getProdutoServico().setValorUnitario(estoqueProduto.getProdutoEdicao().getDesconto());
			
			List<OrigemItemNotaFiscal> origemItens = detalheNotaFiscal.getProdutoServico().getOrigemItemNotaFiscal() != null ? detalheNotaFiscal.getProdutoServico().getOrigemItemNotaFiscal() : new ArrayList<OrigemItemNotaFiscal>();
			
			OrigemItemNotaFiscal oinf = new OrigemItemNotaFiscalEstoque();
			((OrigemItemNotaFiscalEstoque) oinf).setProdutoEdicao(estoqueProduto.getProdutoEdicao());
			origemItens.add(oinf);
			detalheNotaFiscal.getProdutoServico().setOrigemItemNotaFiscal(origemItens);
			
		} else {
			
			for(DetalheNotaFiscal nfi : notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal()) {
				
				boolean notFound = true;

				for(OrigemItemNotaFiscal oinf : nfi.getProdutoServico().getOrigemItemNotaFiscal()) {
					
					produtoEdicao = ((OrigemItemNotaFiscalEstoque) oinf).getProdutoEdicao();
					if(produtoEdicao.getId().equals(estoqueProduto.getProdutoEdicao().getId())) {
						
						notFound = false;
						
						detalheNotaFiscal.getProdutoServico().setCodigoProduto(produtoEdicao.getProduto().getCodigo());
						detalheNotaFiscal.getProdutoServico().setDescricaoProduto(produtoEdicao.getProduto().getTipoProduto().getDescricao());
						detalheNotaFiscal.getProdutoServico().setNcm(produtoEdicao.getProduto().getTipoProduto().getNcm().getCodigo());
						detalheNotaFiscal.getProdutoServico().setQuantidade(detalheNotaFiscal.getProdutoServico().getQuantidade().add(estoqueProduto.getQtde()));
						detalheNotaFiscal.getProdutoServico().setUnidade(estoqueProduto.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
						
						BigDecimal valorTotalTemp = detalheNotaFiscal.getProdutoServico().getValorTotalBruto();
						BigDecimal valorTotalNovoMec = estoqueProduto.getProdutoEdicao().getPrecoVenda().multiply(new BigDecimal(estoqueProduto.getQtde()));
						
						detalheNotaFiscal.getProdutoServico().setValorTotalBruto(valorTotalTemp.add(valorTotalNovoMec));
						
						detalheNotaFiscal.getProdutoServico().setValorUnitario(detalheNotaFiscal.getProdutoServico().getValorTotalBruto().divide(new BigDecimal(detalheNotaFiscal.getProdutoServico().getQuantidade())));
					}
					
				}
				
				if(notFound) {
					
					detalheNotaFiscal.getProdutoServico().setCodigoProduto(estoqueProduto.getProdutoEdicao().getProduto().getCodigo());
					detalheNotaFiscal.getProdutoServico().setDescricaoProduto(estoqueProduto.getProdutoEdicao().getProduto().getTipoProduto().getDescricao());
					detalheNotaFiscal.getProdutoServico().setNcm(estoqueProduto.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getCodigo());
					detalheNotaFiscal.getProdutoServico().setQuantidade(estoqueProduto.getQtde());
					detalheNotaFiscal.getProdutoServico().setUnidade(estoqueProduto.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
					detalheNotaFiscal.getProdutoServico().setValorTotalBruto(estoqueProduto.getProdutoEdicao().getPrecoCusto().multiply(new BigDecimal(estoqueProduto.getQtde())));
					detalheNotaFiscal.getProdutoServico().setValorUnitario(estoqueProduto.getProdutoEdicao().getPrecoCusto());
					// movimentoEstoqueCota.setNotaFiscalEmitida(true);
					
					List<OrigemItemNotaFiscal> origemItens = detalheNotaFiscal.getProdutoServico().getOrigemItemNotaFiscal() != null ? detalheNotaFiscal.getProdutoServico().getOrigemItemNotaFiscal() : new ArrayList<OrigemItemNotaFiscal>();
					
					OrigemItemNotaFiscal oinf = new OrigemItemNotaFiscalEstoque();
					((OrigemItemNotaFiscalEstoque) oinf).setProdutoEdicao(estoqueProduto.getProdutoEdicao());
					origemItens.add(oinf);
					detalheNotaFiscal.getProdutoServico().setOrigemItemNotaFiscal(origemItens);
					
				}
			}
		}
		
		// popular os itens das notas fiscais
		// notaFiscalItem.setNotaFiscal(notaFiscal2);
		notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().add(detalheNotaFiscal);
		
	}
	
}