package br.com.abril.nds.service.builders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Tributacao;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscal;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscalMovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.nota.COFINS;
import br.com.abril.nds.model.fiscal.nota.DetalheNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.ICMS;
import br.com.abril.nds.model.fiscal.nota.IPI;
import br.com.abril.nds.model.fiscal.nota.Impostos;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.OrigemProduto;
import br.com.abril.nds.model.fiscal.nota.PIS;
import br.com.abril.nds.model.fiscal.nota.ProdutoServico;
import br.com.abril.nds.model.fiscal.nota.pk.ProdutoServicoPK;
import br.com.abril.nds.util.CurrencyUtil;

public class ItemNotaFiscalBuilder  {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ItemNotaFiscalBuilder.class);

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
		
		BigDecimal valorTotalBruto = CurrencyUtil.arredondarValorParaDuasCasas(movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto().multiply(new BigDecimal(movimentoEstoqueCota.getQtde())));
		detalheNotaFiscal.getProdutoServico().setValorTotalBruto(valorTotalBruto);
		
		BigDecimal valorUnitario = CurrencyUtil.arredondarValorParaQuatroCasas(movimentoEstoqueCota.getValoresAplicados().getPrecoComDesconto());
		detalheNotaFiscal.getProdutoServico().setValorUnitario(valorUnitario);
		
		//detalheNotaFiscal.getProdutoServico().setValorDesconto(BigDecimal.ZERO);
		
		//FIXME: Ajustar os produtos para sinalizarem a inclusao do frete na nf
		detalheNotaFiscal.getProdutoServico().setValorFreteCompoeValorNF(false);
		if(detalheNotaFiscal.getProdutoServico().isValorFreteCompoeValorNF()) {
			//FIXME: Ajustar os produtos para trazer os valores, se necessario
			detalheNotaFiscal.getProdutoServico().setValorFrete(BigDecimal.ZERO);
			detalheNotaFiscal.getProdutoServico().setValorSeguro(BigDecimal.ZERO);
			detalheNotaFiscal.getProdutoServico().setValorOutros(BigDecimal.ZERO);
		}
		
		detalheNotaFiscal.getProdutoServico().setProdutoEdicao(new ProdutoEdicao(movimentoEstoqueCota.getProdutoEdicao().getId()));
		if(detalheNotaFiscal.getImpostos() == null) {
			detalheNotaFiscal.setImpostos(new Impostos());
		}
		
		for(Tributacao t : movimentoEstoqueCota.getProdutoEdicao().getProduto().getProdutoTributacao()) {
			detalheNotaFiscal.getProdutoServico().setCst(t.getCstA().toString() + t.getCst().toString());
			if("ICMS".equals(t.getTributo())) {
				detalheNotaFiscal.getProdutoServico().setValorAliquotaICMS(t.getValorAliquota());
				Class<?> clazz;
				ICMS icms = null;
				try {
					clazz = Class.forName("br.com.abril.nds.model.fiscal.nota.ICMS"+ t.getCst().toString());
					icms = (ICMS) clazz.newInstance();
				} catch (ClassNotFoundException e) {
					LOGGER.error("Classe de tributo não encontrada: "+ "br.com.abril.nds.model.fiscal.nota.ICMS"+ t.getCst().toString(), e);
					throw new ValidacaoException(TipoMensagem.ERROR, "Tributo não encontrada: ICMS"+ t.getCst().toString());
				} catch (InstantiationException e) {
					LOGGER.error("Erro ao instanciar classe: "+ "br.com.abril.nds.model.fiscal.nota.ICMS"+ t.getCst().toString(), e);
					throw new ValidacaoException(TipoMensagem.ERROR, "Tributo não encontrada: ICMS"+ t.getCst().toString());
				} catch (IllegalAccessException e) {
					LOGGER.error("Erro ao acessar classe: "+ "br.com.abril.nds.model.fiscal.nota.ICMS"+ t.getCst().toString(), e);
					throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao acessar classe: ICMS"+ t.getCst().toString());
				}
				
				//FIXME: Ajustar o produto para trazer a origem (nacional / estrangeira)
				icms.setOrigem(OrigemProduto.NACIONAL);
				icms.setCst(t.getCst().toString());
				icms.setAliquota(t.getValorAliquota());
				icms.setValorBaseCalculo(t.getBaseCalculo());
				
				detalheNotaFiscal.getImpostos().setIcms(icms);
			}
			
			if("IPI".equals(t.getTributo())) {
				detalheNotaFiscal.getProdutoServico().setValorAliquotaIPI(t.getValorAliquota());
				
				IPI ipi = new IPI();
				
				ipi.setCst(t.getCst().toString());
				ipi.setAliquota(t.getValorAliquota());
				ipi.setValorBaseCalculo(t.getBaseCalculo());
				
				detalheNotaFiscal.getImpostos().setIpi(ipi);
			}
			
			if("PIS".equals(t.getTributo())) {
				detalheNotaFiscal.getProdutoServico().setValorAliquotaIPI(t.getValorAliquota());
				
				PIS pis = new PIS();
				
				pis.setCst(Integer.valueOf(t.getCst().toString()));
				pis.setValorAliquota(t.getValorAliquota());
				pis.setValorBaseCalculo(t.getBaseCalculo());
				
				detalheNotaFiscal.getImpostos().setPis(pis);
			}
			
			if("COFINS".equals(t.getTributo())) {
				detalheNotaFiscal.getProdutoServico().setValorAliquotaIPI(t.getValorAliquota());
				
				COFINS cofins = new COFINS();
				
				cofins.setCst(Integer.valueOf(t.getCst().toString()));
				cofins.setValorAliquota(t.getValorAliquota());
				cofins.setValorBaseCalculo(t.getBaseCalculo());
				
				detalheNotaFiscal.getImpostos().setCofins(cofins);
			}
			
		}
		
		//FIXME: Ajustar o codigo Excessao do ipi
		//detalheNotaFiscal.getProdutoServico().setExtipi(0L);

		String cfop = "";
		if(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().getPais()
				.equals(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().getPais())) {
			
			if(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().getUf()
					.equals(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().getUf())) {
				cfop = notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getNaturezaOperacao().getCfopEstado();
			} else {
				cfop = notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getNaturezaOperacao().getCfopOutrosEstados();
			}
			
		} else {
			
			cfop = notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getNaturezaOperacao().getCfopExterior();
		}
		
		detalheNotaFiscal.getProdutoServico().setCfop(Integer.valueOf(cfop));
		
		movimentoEstoqueCota.setNotaFiscalEmitida(true);
		
		List<OrigemItemNotaFiscal> origemItens = detalheNotaFiscal.getProdutoServico().getOrigemItemNotaFiscal() != null ? detalheNotaFiscal.getProdutoServico().getOrigemItemNotaFiscal() : new ArrayList<OrigemItemNotaFiscal>();
		
		OrigemItemNotaFiscal oinf = new OrigemItemNotaFiscalMovimentoEstoqueCota();
		((OrigemItemNotaFiscalMovimentoEstoqueCota) oinf).setMovimentoEstoqueCota(movimentoEstoqueCota);
		origemItens.add(oinf);
		detalheNotaFiscal.getProdutoServico().setOrigemItemNotaFiscal(origemItens);
	}
	
}