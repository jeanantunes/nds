package br.com.abril.nds.service.builders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
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
import br.com.abril.nds.model.fiscal.nota.pk.ProdutoServicoPK;

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
			if(notaFiscal2.getNotaFiscalInformacoes().getDetalhesNotaFiscal() == null) {
				notaFiscal2.getNotaFiscalInformacoes().setDetalhesNotaFiscal(new ArrayList<DetalheNotaFiscal>());
			}
		}
		
		if(notaFiscal2.getNotaFiscalInformacoes().getDetalhesNotaFiscal().size() == 0) {
			
			montarItem(movimentoEstoqueCota, detalheNotaFiscal, notaFiscal2);
			
		} else {
			
			for(DetalheNotaFiscal dnf : notaFiscal2.getNotaFiscalInformacoes().getDetalhesNotaFiscal()) {
				
				boolean notFound = true;

				if(dnf.getProdutoServico() == null) {
					throw new ValidacaoException(TipoMensagem.ERROR, "Item de nota fiscal nulo.");
				}
				
				for(OrigemItemNotaFiscal oinf : dnf.getProdutoServico().getOrigemItemNotaFiscal()) {
					
					MovimentoEstoqueCota mec = ((OrigemItemNotaFiscalMovimentoEstoqueCota) oinf).getMovimentoEstoqueCota();
					if(mec.getProdutoEdicao().getId().equals(movimentoEstoqueCota.getProdutoEdicao().getId())) {
						
						notFound = false;
						
						montarItem(movimentoEstoqueCota, detalheNotaFiscal, notaFiscal2);
						
					}
					
				}
				
				if(notFound) {
					
					montarItem(movimentoEstoqueCota, detalheNotaFiscal, notaFiscal2);
					
				}
			}
		}
		
		// popular os itens das notas fiscais
		// notaFiscalItem.setNotaFiscal(notaFiscal2);
		notaFiscal2.getNotaFiscalInformacoes().getDetalhesNotaFiscal().add(detalheNotaFiscal);
		
	}

	private static void montarItem(MovimentoEstoqueCota movimentoEstoqueCota,
			DetalheNotaFiscal detalheNotaFiscal, NotaFiscal notaFiscal) {
		
		detalheNotaFiscal.getProdutoServico().setCodigoProduto(movimentoEstoqueCota.getProdutoEdicao().getProduto().getCodigo());
		detalheNotaFiscal.getProdutoServico().setDescricaoProduto(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getDescricao());
		
		if(detalheNotaFiscal.getProdutoServicoPK() == null){
			detalheNotaFiscal.setProdutoServicoPK(new ProdutoServicoPK());
			detalheNotaFiscal.getProdutoServicoPK().setSequencia(notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().size()+1);
			detalheNotaFiscal.getProdutoServicoPK().setNotaFiscal(notaFiscal);
		}
		
		Long codigoBarras = null;
		try {
			String cb = movimentoEstoqueCota.getProdutoEdicao().getCodigoDeBarras();
			codigoBarras = Long.parseLong(StringUtils.leftPad(cb, 13, '0').substring(0, 13));
		} catch (Exception e) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Código de barras inválido: "+ movimentoEstoqueCota.getProdutoEdicao().getProduto().getCodigo() +" / "+ movimentoEstoqueCota.getProdutoEdicao().getNumeroEdicao());
		}
		
		detalheNotaFiscal.getProdutoServico().setCodigoBarras(codigoBarras);
		detalheNotaFiscal.getProdutoServico().setNcm(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getCodigo());
		detalheNotaFiscal.getProdutoServico().setQuantidade(movimentoEstoqueCota.getQtde());
		detalheNotaFiscal.getProdutoServico().setUnidade(movimentoEstoqueCota.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
		detalheNotaFiscal.getProdutoServico().setValorTotalBruto(movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto().multiply(new BigDecimal(movimentoEstoqueCota.getQtde())));
		detalheNotaFiscal.getProdutoServico().setValorUnitario(movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto());
		detalheNotaFiscal.getProdutoServico().setProdutoEdicao(new ProdutoEdicao(movimentoEstoqueCota.getProdutoEdicao().getId()));
		
		//FIXME: Ajustar o codigo Excessao do ipi
		detalheNotaFiscal.getProdutoServico().setExtipi(0L);
		//FIXME: Ajustar CFOP
		detalheNotaFiscal.getProdutoServico().setCfop(0);
		
		movimentoEstoqueCota.setNotaFiscalEmitida(true);
		
		// if(detalheNotaFiscal.getEncargoFinanceiro() == null) {
			// detalheNotaFiscal.setEncargoFinanceiro(new EncargoFinanceiro() {
				// private static final long serialVersionUID = 1L;
				
			// });
		// }
		COFINS cofins= new COFINS();
		cofins.setCst(1);
		cofins.setPercentualAliquota(new BigDecimal("3.0"));
		// detalheNotaFiscal.getEncargoFinanceiro().setCofins(cofins);
		
		List<OrigemItemNotaFiscal> origemItens = detalheNotaFiscal.getProdutoServico().getOrigemItemNotaFiscal() != null ? detalheNotaFiscal.getProdutoServico().getOrigemItemNotaFiscal() : new ArrayList<OrigemItemNotaFiscal>();
		
		OrigemItemNotaFiscal oinf = new OrigemItemNotaFiscalMovimentoEstoqueCota();
		((OrigemItemNotaFiscalMovimentoEstoqueCota) oinf).setMovimentoEstoqueCota(movimentoEstoqueCota);
		origemItens.add(oinf);
		detalheNotaFiscal.getProdutoServico().setOrigemItemNotaFiscal(origemItens);
	}
	
}