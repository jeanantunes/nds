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
import br.com.abril.nds.model.fiscal.nota.COFINS;
import br.com.abril.nds.model.fiscal.nota.DetalheNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.EncargoFinanceiro;
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

		ProdutoServico produtoServico = new ProdutoServico();
		DetalheNotaFiscal detalheNotaFiscal = new DetalheNotaFiscal(produtoServico);
		
		if(notaFiscal2 == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Problemas ao gerar Nota Fiscal. Objeto nulo.");
		} else {
			if(notaFiscal2.getDetalhesNotaFiscal() == null) {
				notaFiscal2.setDetalhesNotaFiscal(new ArrayList<DetalheNotaFiscal>());
			}
		}
		
		if(notaFiscal2.getDetalhesNotaFiscal().size() == 0) {
		
			detalheNotaFiscal.getProdutoServico().setCodigoProduto(movimentoEstoqueCota.getProdutoEdicao().getProduto().getCodigo());
			detalheNotaFiscal.getProdutoServico().setDescricaoProduto(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getDescricao());
			detalheNotaFiscal.getProdutoServico().setNcm(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getCodigo());
			detalheNotaFiscal.getProdutoServico().setQuantidade(movimentoEstoqueCota.getQtde());
			detalheNotaFiscal.getProdutoServico().setUnidade(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
			detalheNotaFiscal.getProdutoServico().setValorTotalBruto(movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto().multiply(new BigDecimal(movimentoEstoqueCota.getQtde())));
			detalheNotaFiscal.getProdutoServico().setValorUnitario(movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto());
			movimentoEstoqueCota.setNotaFiscalEmitida(true);
			
			if(detalheNotaFiscal.getEncargoFinanceiro() == null) {
				detalheNotaFiscal.setEncargoFinanceiro(new EncargoFinanceiro() {
					private static final long serialVersionUID = 1L;
					
				});
			}
			COFINS cofins= new COFINS();
			cofins.setCst(1);
			cofins.setPercentualAliquota(new BigDecimal("3.0"));
			detalheNotaFiscal.getEncargoFinanceiro().setCofins(cofins);
			
			List<OrigemItemNotaFiscal> origemItens = detalheNotaFiscal.getProdutoServico().getOrigemItemNotaFiscal() != null ? detalheNotaFiscal.getProdutoServico().getOrigemItemNotaFiscal() : new ArrayList<OrigemItemNotaFiscal>();
			
			OrigemItemNotaFiscal oinf = new OrigemItemNotaFiscalMovimentoEstoqueCota();
			((OrigemItemNotaFiscalMovimentoEstoqueCota) oinf).setMovimentoEstoqueCota(movimentoEstoqueCota);
			origemItens.add(oinf);
			detalheNotaFiscal.getProdutoServico().setOrigemItemNotaFiscal(origemItens);
			
		} else {
			
			for(DetalheNotaFiscal dnf : notaFiscal2.getDetalhesNotaFiscal()) {
				
				boolean notFound = true;

				if(dnf.getProdutoServico() == null) {
					throw new ValidacaoException(TipoMensagem.ERROR, "Item de nota fiscal nulo.");
				}
				
				for(OrigemItemNotaFiscal oinf : dnf.getProdutoServico().getOrigemItemNotaFiscal()) {
					
					MovimentoEstoqueCota mec = ((OrigemItemNotaFiscalMovimentoEstoqueCota) oinf).getMovimentoEstoqueCota();
					if(mec.getProdutoEdicao().getId().equals(movimentoEstoqueCota.getProdutoEdicao().getId())) {
						
						notFound = false;
						
						detalheNotaFiscal.getProdutoServico().setCodigoProduto(movimentoEstoqueCota.getProdutoEdicao().getProduto().getCodigo());
						detalheNotaFiscal.getProdutoServico().setDescricaoProduto(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getDescricao());
						detalheNotaFiscal.getProdutoServico().setNcm(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getCodigo());
						detalheNotaFiscal.getProdutoServico().setQuantidade(detalheNotaFiscal.getProdutoServico().getQuantidade().add(movimentoEstoqueCota.getQtde()));
						detalheNotaFiscal.getProdutoServico().setUnidade(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
						
						BigDecimal valorTotalTemp = detalheNotaFiscal.getProdutoServico().getValorTotalBruto();
						BigDecimal valorTotalNovoMec = movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto().multiply(new BigDecimal(movimentoEstoqueCota.getQtde()));
						
						detalheNotaFiscal.getProdutoServico().setValorTotalBruto(valorTotalTemp.add(valorTotalNovoMec));
						
						detalheNotaFiscal.getProdutoServico().setValorUnitario(detalheNotaFiscal.getProdutoServico().getValorTotalBruto().divide(new BigDecimal(detalheNotaFiscal.getProdutoServico().getQuantidade())));
						
						mec.setNotaFiscalEmitida(true);
						
						if(detalheNotaFiscal.getEncargoFinanceiro() == null) {
							detalheNotaFiscal.setEncargoFinanceiro(new EncargoFinanceiro() {
								
							});
						}
						COFINS cofins= new COFINS();
						cofins.setCst(1);
						cofins.setPercentualAliquota(new BigDecimal("3.0"));
						detalheNotaFiscal.getEncargoFinanceiro().setCofins(cofins);
						
					}
					
				}
				
				if(notFound) {
					
					detalheNotaFiscal.getProdutoServico().setCodigoProduto(movimentoEstoqueCota.getProdutoEdicao().getProduto().getCodigo());
					detalheNotaFiscal.getProdutoServico().setDescricaoProduto(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getDescricao());
					detalheNotaFiscal.getProdutoServico().setNcm(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getCodigo());
					detalheNotaFiscal.getProdutoServico().setQuantidade(movimentoEstoqueCota.getQtde());
					detalheNotaFiscal.getProdutoServico().setUnidade(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
					detalheNotaFiscal.getProdutoServico().setValorTotalBruto(movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto().multiply(new BigDecimal(movimentoEstoqueCota.getQtde())));
					detalheNotaFiscal.getProdutoServico().setValorUnitario(movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto());
					movimentoEstoqueCota.setNotaFiscalEmitida(true);
					
					if(detalheNotaFiscal.getEncargoFinanceiro() == null) {
						detalheNotaFiscal.setEncargoFinanceiro(new EncargoFinanceiro() {
							
						});
					}
					COFINS cofins= new COFINS();
					cofins.setCst(1);
					cofins.setPercentualAliquota(new BigDecimal("3.0"));
					detalheNotaFiscal.getEncargoFinanceiro().setCofins(cofins);
					
					List<OrigemItemNotaFiscal> origemItens = detalheNotaFiscal.getProdutoServico().getOrigemItemNotaFiscal() != null ? detalheNotaFiscal.getProdutoServico().getOrigemItemNotaFiscal() : new ArrayList<OrigemItemNotaFiscal>();
					
					OrigemItemNotaFiscal oinf = new OrigemItemNotaFiscalMovimentoEstoqueCota();
					((OrigemItemNotaFiscalMovimentoEstoqueCota) oinf).setMovimentoEstoqueCota(movimentoEstoqueCota);
					origemItens.add(oinf);
					detalheNotaFiscal.getProdutoServico().setOrigemItemNotaFiscal(origemItens);
					
				}
			}
		}
		
		// popular os itens das notas fiscais
		// notaFiscalItem.setNotaFiscal(notaFiscal2);
		notaFiscal2.getDetalhesNotaFiscal().add(detalheNotaFiscal);
		
	}
	
}