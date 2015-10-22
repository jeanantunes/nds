package br.com.abril.nds.service.builders;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Tributacao;
import br.com.abril.nds.model.cadastro.Tributacao.TributacaoTipoOperacao;
import br.com.abril.nds.model.cadastro.Tributo;
import br.com.abril.nds.model.cadastro.TributoAliquota;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscal;
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscalCota;
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscalFornecedor;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscal;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscalMovimentoEstoque;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscalMovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscalMovimentoFechamentoFiscalCota;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscalMovimentoFechamentoFiscalFornecedor;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.nota.COFINS;
import br.com.abril.nds.model.fiscal.nota.CofinsWrapper;
import br.com.abril.nds.model.fiscal.nota.DetalheNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.ICMS;
import br.com.abril.nds.model.fiscal.nota.IPI;
import br.com.abril.nds.model.fiscal.nota.Impostos;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.OrigemProduto;
import br.com.abril.nds.model.fiscal.nota.PIS;
import br.com.abril.nds.model.fiscal.nota.PISWrapper;
import br.com.abril.nds.model.fiscal.nota.ProdutoServico;
import br.com.abril.nds.model.fiscal.nota.TribIPI;
import br.com.abril.nds.model.fiscal.nota.pk.ProdutoServicoPK;
import br.com.abril.nds.model.movimentacao.AbstractMovimentoEstoque;
import br.com.abril.nds.util.CurrencyUtil;

public class ItemNotaFiscalBuilder  {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ItemNotaFiscalBuilder.class);

	public static void montaItemNotaFiscal(NotaFiscal notaFiscal, MovimentoEstoqueCota movimentoEstoqueCota, Map<String, TributoAliquota> tributoAliquota) {

		DetalheNotaFiscal detalheNotaFiscal = null;
		if(notaFiscal == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Problemas ao gerar Nota Fiscal. Objeto nulo.");
		} else {
			if(notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal() == null) {
				notaFiscal.getNotaFiscalInformacoes().setDetalhesNotaFiscal(new ArrayList<DetalheNotaFiscal>());
			}
		}
		
		if(notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().size() == 0) {
			
			detalheNotaFiscal = new DetalheNotaFiscal();
			detalheNotaFiscal = montarItem(movimentoEstoqueCota, new DetalheNotaFiscal(), notaFiscal, tributoAliquota);
			
		} else {
			
			boolean notFound = true;
			for(DetalheNotaFiscal dnf : notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal()) {
				
				if(dnf.getProdutoServico() == null) {
					
					throw new ValidacaoException(TipoMensagem.ERROR, "Item de nota fiscal nulo.");
				}
				
				if(movimentoEstoqueCota.getProdutoEdicao().getId().equals(dnf.getProdutoServico().getProdutoEdicao().getId())) {
					
					notFound = false;
					break;
				}
			}
			
			if(notFound) {
				
				detalheNotaFiscal = new DetalheNotaFiscal();
				detalheNotaFiscal = montarItem(movimentoEstoqueCota, detalheNotaFiscal, notaFiscal, tributoAliquota);
				
			} else {
				
				for(DetalheNotaFiscal dnf : notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal()) {
					
					if(movimentoEstoqueCota.getProdutoEdicao().getId().equals(dnf.getProdutoServico().getProdutoEdicao().getId())) {
						
						montarItem(movimentoEstoqueCota, dnf, notaFiscal, tributoAliquota);
					}
					
				}
			}
		}
		
		if(detalheNotaFiscal != null && detalheNotaFiscal.getProdutoServico() != null){ 

			notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().add(detalheNotaFiscal);
		}
		
		
	}

	private static DetalheNotaFiscal montarItem(AbstractMovimentoEstoque movimentoEstoque, DetalheNotaFiscal detalheNotaFiscal, NotaFiscal notaFiscal, Map<String, TributoAliquota> tributoAliquota) {
		
		ProdutoServico produtoServico = null;
		String codigoBarras = null;
		
		BigInteger quantidade = BigInteger.ZERO;
		
		if(detalheNotaFiscal.getProdutoServico() == null) {
			
			try {
				
				if(movimentoEstoque.getProdutoEdicao().getCodigoDeBarras() == null) {
					
					codigoBarras = StringUtils.leftPad("0", 13, '0').substring(0, 13);
				} else {
					
					String cb = movimentoEstoque.getProdutoEdicao().getCodigoDeBarras();
					codigoBarras = StringUtils.leftPad(cb, 13, '0').substring(0, 13);
				}
				
			} catch (Exception e) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Código de barras inválido: "+ movimentoEstoque.getProdutoEdicao().getProduto().getCodigo() +" / "+ movimentoEstoque.getProdutoEdicao().getNumeroEdicao());
			}
			
			produtoServico = new ProdutoServico();
			produtoServico.setCodigoBarras(codigoBarras);
			produtoServico.setNcm(movimentoEstoque.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getCodigo());
			produtoServico.setUnidade(movimentoEstoque.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
			produtoServico.setCodigoProduto(movimentoEstoque.getProdutoEdicao().getProduto().getCodigo());
			produtoServico.setDescricaoProduto(movimentoEstoque.getProdutoEdicao().getProduto().getNome());
			produtoServico.setValorTotalBruto(BigDecimal.ZERO);
			produtoServico.setQuantidade(BigInteger.ZERO);
		} else {
			
			produtoServico = detalheNotaFiscal.getProdutoServico();
		}
		BigDecimal pesoBrutoLiquido = BigDecimal.ZERO;
		BigDecimal valorUnitario = BigDecimal.ZERO;
		BigDecimal valorDesconto = BigDecimal.ZERO;
		if(movimentoEstoque instanceof MovimentoEstoqueCota) {
			
			valorUnitario = ((MovimentoEstoqueCota) movimentoEstoque).getValoresAplicados().getPrecoComDesconto();
			valorDesconto = ((MovimentoEstoqueCota) movimentoEstoque).getValoresAplicados().getValorDesconto();
			
		} else if(movimentoEstoque instanceof MovimentoEstoque) {
			
			BigDecimal precoVenda = ((MovimentoEstoque) movimentoEstoque).getProdutoEdicao().getPrecoVenda();
			BigDecimal precoComDesconto = BigDecimal.ZERO;
			
			if(((MovimentoEstoque) movimentoEstoque).getProdutoEdicao().getOrigem().equals(Origem.MANUAL)) {
				valorDesconto = ((MovimentoEstoque) movimentoEstoque).getProdutoEdicao().getDesconto();
			} else if (((MovimentoEstoque) movimentoEstoque).getProdutoEdicao().getOrigem().equals(Origem.PRODUTO_SEM_CADASTRO)) {
				valorDesconto = ((MovimentoEstoque) movimentoEstoque).getProdutoEdicao().getDesconto();
			} else {
				
				if(((MovimentoEstoque) movimentoEstoque).getProdutoEdicao().getDescontoLogistica() == null) {
					valorDesconto = ((MovimentoEstoque) movimentoEstoque).getProdutoEdicao().getDesconto();
				} else {
					valorDesconto = ((MovimentoEstoque) movimentoEstoque).getProdutoEdicao().getDescontoLogistica().getPercentualDesconto();
				}
				
			}
			
			precoComDesconto = precoVenda.subtract(precoVenda.multiply(valorDesconto.divide(BigDecimal.valueOf(100))));			
			valorUnitario = precoComDesconto;
		} else {
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de movimento não suportado para geração da NF-e.");
		}
		
		pesoBrutoLiquido = BigDecimal.valueOf(((MovimentoEstoque) movimentoEstoque).getProdutoEdicao().getProduto().getPeso());
		
		if(notaFiscal.getNotaFiscalInformacoes().getPesoBrutoLiquido() == null ) {
			notaFiscal.getNotaFiscalInformacoes().setPesoBrutoLiquido(BigDecimal.ZERO);
		}
		
		notaFiscal.getNotaFiscalInformacoes().setPesoBrutoLiquido(notaFiscal.getNotaFiscalInformacoes().getPesoBrutoLiquido().add(pesoBrutoLiquido));
		
		if(((TipoMovimentoEstoque) movimentoEstoque.getTipoMovimento()).getOperacaoEstoque().equals(OperacaoEstoque.ENTRADA)) {
			produtoServico.setQuantidade(produtoServico.getQuantidade().add(movimentoEstoque.getQtde()).abs());
		} else {
			
			quantidade = produtoServico.getQuantidade().add(movimentoEstoque.getQtde()).abs();
			
			LOGGER.error("Produto: "+produtoServico.getCodigoProduto());
			
			LOGGER.error("quantidade: "+quantidade);
			
			if(produtoServico.getQuantidade().intValue() > 0) {
				
				produtoServico.setQuantidade(quantidade);
				
			} else {
				
				if(!produtoServico.getCodigoProduto().equals(movimentoEstoque.getProdutoEdicao().getProduto().getCodigo())) {
					produtoServico.setQuantidade(movimentoEstoque.getQtde());
				} else {
					
					produtoServico.setQuantidade(quantidade);
				}
			}
		}
		produtoServico.setValorUnitario(valorUnitario);
		produtoServico.setValorTotalBruto(CurrencyUtil.truncateDecimal(valorUnitario.multiply(new BigDecimal(produtoServico.getQuantidade())), 2));
				
		produtoServico.setValorCompoeValorNF(true);
		
		//FIXME: Ajustar os produtos para sinalizarem a inclusao do frete na nf
		produtoServico.setValorFreteCompoeValorNF(false);
		if(produtoServico.isValorFreteCompoeValorNF()) {
			//FIXME: Ajustar os produtos para trazer os valores, se necessario
			produtoServico.setValorFrete(BigDecimal.ZERO);
			produtoServico.setValorSeguro(BigDecimal.ZERO);
			produtoServico.setValorOutros(BigDecimal.ZERO);
		}
		
		if(produtoServico.getProdutoEdicao() == null) {			
			produtoServico.setProdutoEdicao(movimentoEstoque.getProdutoEdicao());
		}
		
		detalheNotaFiscal.setImpostos(new Impostos());
		
		detalheNotaFiscal.setProdutoServico(produtoServico);
		
		Map<String, Tributacao> tributacaoProduto = new HashMap<String, Tributacao>();
		TipoOperacao to = notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getNaturezaOperacao().getTipoOperacao();
		for (Tributacao t : movimentoEstoque.getProdutoEdicao().getProduto().getTipoProduto().getProdutoTributacao()) {
			if(to.name().equals(t.getTipoOperacao().name()) || t.getTipoOperacao().equals(TributacaoTipoOperacao.ENTRADA_SAIDA)) {
				tributacaoProduto.put(t.getTributo(), t);
			}
		}
		
		// Aplica / calcula a tributacao do item
		aplicarTributacaoItem(detalheNotaFiscal, notaFiscal, tributoAliquota, produtoServico, tributacaoProduto);
		
		movimentoEstoque.setNotaFiscalEmitida(true);
		
		List<OrigemItemNotaFiscal> origemItens = produtoServico.getOrigemItemNotaFiscal() != null ? produtoServico.getOrigemItemNotaFiscal() : new ArrayList<OrigemItemNotaFiscal>();
		
		if(movimentoEstoque instanceof MovimentoEstoqueCota) {		
			
			OrigemItemNotaFiscal oinf = new OrigemItemNotaFiscalMovimentoEstoqueCota();
			((OrigemItemNotaFiscalMovimentoEstoqueCota) oinf).setMovimentoEstoqueCota((MovimentoEstoqueCota) movimentoEstoque);
			origemItens.add(oinf);
			
		} else if(movimentoEstoque instanceof MovimentoEstoque) {
			
			if(!movimentoEstoque.getProdutoEdicao().getId().equals(produtoServico.getProdutoEdicao().getId())) {
				OrigemItemNotaFiscal oinfME = new OrigemItemNotaFiscalMovimentoEstoque();
				((OrigemItemNotaFiscalMovimentoEstoque) oinfME).setMovimentoEstoque((MovimentoEstoque) movimentoEstoque);
				origemItens.add(oinfME);
			}
			
			
		}
		
		produtoServico.setOrigemItemNotaFiscal(origemItens);
		
		if(detalheNotaFiscal.getProdutoServicoPK() == null) {
			detalheNotaFiscal.setProdutoServicoPK(new ProdutoServicoPK());
			detalheNotaFiscal.getProdutoServicoPK().setSequencia(notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().size()+1);
			detalheNotaFiscal.getProdutoServicoPK().setNotaFiscal(notaFiscal);
		}
		
		return detalheNotaFiscal;
	}

	private static void aplicarTributacaoItem(DetalheNotaFiscal detalheNotaFiscal, NotaFiscal notaFiscal,
			Map<String, TributoAliquota> tributoAliquota, ProdutoServico produtoServico,
			Map<String, Tributacao> tributacaoProduto) {
		
		Tributacao icmsProduto = tributacaoProduto.get("ICMS");
		if(icmsProduto != null) {
			produtoServico.setCst(icmsProduto.getCstA().toString() + icmsProduto.getCst().toString());
			produtoServico.setValorAliquotaICMS(CurrencyUtil.arredondarValorParaDuasCasas(icmsProduto.getValorAliquota()));
			Class<?> clazz;
			ICMS icms = null;
			
			try {
				clazz = Class.forName("br.com.abril.nds.model.fiscal.nota.ICMS"+ icmsProduto.getCst().toString());
				icms = (ICMS) clazz.newInstance();
			} catch (ClassNotFoundException e) {
				LOGGER.error("Classe de tributo não encontrada: "+ "br.com.abril.nds.model.fiscal.nota.ICMS"+ icmsProduto.getCst().toString(), e);
				throw new ValidacaoException(TipoMensagem.ERROR, "Tributo não encontrada: ICMS"+ icmsProduto.getCst().toString());
			} catch (InstantiationException e) {
				LOGGER.error("Erro ao instanciar classe: "+ "br.com.abril.nds.model.fiscal.nota.ICMS"+ icmsProduto.getCst().toString(), e);
				throw new ValidacaoException(TipoMensagem.ERROR, "Tributo não encontrada: ICMS"+ icmsProduto.getCst().toString());
			} catch (IllegalAccessException e) {
				LOGGER.error("Erro ao acessar classe: "+ "br.com.abril.nds.model.fiscal.nota.ICMS"+ icmsProduto.getCst().toString(), e);
				throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao acessar classe: ICMS"+ icmsProduto.getCst().toString());
			}
			
			//FIXME: Ajustar o produto para trazer a origem (nacional / estrangeira)
			icms.setOrigem(OrigemProduto.NACIONAL);
			icms.setCst(icmsProduto.getCst().toString());
			icms.setAliquota(CurrencyUtil.arredondarValorParaDuasCasas(icmsProduto.getValorAliquota()));
			icms.setValorBaseCalculo(CurrencyUtil.arredondarValorParaDuasCasas(icmsProduto.getBaseCalculo()));
			
			detalheNotaFiscal.getImpostos().setIcms(icms);
		} else {
			StringBuilder sb = new StringBuilder().append("ICMS não encontrado para o Produto: ")
					.append(produtoServico.getCodigoProduto())
					.append(" / ")
					.append(produtoServico.getProdutoEdicao().getNumeroEdicao());
			
			LOGGER.error(sb.toString() );
			throw new ValidacaoException(TipoMensagem.ERROR, sb.toString());
		}
		
		Tributacao ipiProduto = tributacaoProduto.get("IPI");	
		
		if(ipiProduto != null) {
			
			if(!tributacaoProduto.get("IPI").isIsentoOuNaoTributado()) {
				produtoServico.setValorAliquotaIPI(CurrencyUtil.arredondarValorParaDuasCasas(ipiProduto.getValorAliquota()));
			
				IPI ipi = new IPI();
				
				ipi.setCst(ipiProduto.getCst().toString());
				ipi.setAliquota(CurrencyUtil.arredondarValorParaDuasCasas(ipiProduto.getValorAliquota()));
				ipi.setValorBaseCalculo(CurrencyUtil.arredondarValorParaDuasCasas(ipiProduto.getBaseCalculo()));
				ipi.setCodigoEnquadramento(ipiProduto.getCst());
				ipi.setIPITrib(new TribIPI());
				ipi.getIPITrib().setCst(ipiProduto.getCst());
				ipi.getIPITrib().setValorAliquota(CurrencyUtil.arredondarValorParaDuasCasas(ipiProduto.getValorAliquota()));
				ipi.getIPITrib().setValorBaseCalculo(CurrencyUtil.arredondarValorParaDuasCasas(ipiProduto.getBaseCalculo()));
				
				// FIX ME ajustar a classe de valor do ipi
				ipi.getIPITrib().setValorIPI(CurrencyUtil.arredondarValorParaDuasCasas(ipiProduto.getBaseCalculo()));
				detalheNotaFiscal.getImpostos().setIpi(ipi);
			} else {
				produtoServico.setValorAliquotaIPI(CurrencyUtil.arredondarValorParaDuasCasas(ipiProduto.getValorAliquota()));
				
				IPI ipi = new IPI();
				
				ipi.setCst(ipiProduto.getCst().toString());
				ipi.setAliquota(CurrencyUtil.arredondarValorParaDuasCasas(ipiProduto.getValorAliquota()));
				ipi.setValorBaseCalculo(CurrencyUtil.arredondarValorParaDuasCasas(ipiProduto.getBaseCalculo()));
				ipi.setCodigoEnquadramento("999");
				ipi.setIPITrib(new TribIPI());
				ipi.getIPITrib().setCst(ipiProduto.getCst());
				ipi.getIPITrib().setValorAliquota(CurrencyUtil.arredondarValorParaDuasCasas(ipiProduto.getValorAliquota()));
				ipi.getIPITrib().setValorBaseCalculo(CurrencyUtil.arredondarValorParaDuasCasas(ipiProduto.getBaseCalculo()));
				
				// FIX ME ajustar a classe de valor do ipi
				ipi.getIPITrib().setValorIPI(CurrencyUtil.arredondarValorParaDuasCasas(ipiProduto.getBaseCalculo()));
				detalheNotaFiscal.getImpostos().setIpi(ipi);
			}
			
		} else {
			StringBuilder sb = new StringBuilder().append("IPI não encontrado para o Produto: ").append(produtoServico.getCodigoProduto()).append(" / ").append(produtoServico.getProdutoEdicao().getNumeroEdicao());
			
			LOGGER.error(sb.toString() );
			throw new ValidacaoException(TipoMensagem.ERROR, sb.toString());
		}
		
		TributoAliquota tributoSimples = tributoAliquota.get("SIMPLES");
		
		if(tributoSimples != null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Regime tributario simples não suportado.");
		}
		
		TributoAliquota tributoPis = tributoAliquota.get("PIS");
		TributoAliquota tributoCofins = tributoAliquota.get("COFINS");
		
		if(tributoPis != null) {
			PISWrapper pisWrapper = new PISWrapper();
			PIS pis = new PIS();
			pis.setValorBaseCalculo(CurrencyUtil.arredondarValorParaDuasCasas(BigDecimal.valueOf(0)));
			pis.setValorAliquota(CurrencyUtil.arredondarValorParaDuasCasas(BigDecimal.valueOf(0)));
			pis.setPercentualAliquota(CurrencyUtil.arredondarValorParaDuasCasas(BigDecimal.valueOf(0)));	
			pis.setValor(CurrencyUtil.arredondarValorParaDuasCasas(BigDecimal.valueOf(0)));
			
			if(tributacaoProduto.get("PIS") != null) {
				
				pis.setCst(tributacaoProduto.get("PIS").getCst());
				
				if(notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getNaturezaOperacao().getTributosNaturezaOperacao() != null) {
					
					for(Tributo t : notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getNaturezaOperacao().getTributosNaturezaOperacao()) {
						if("PIS".equals(t.getNome())) {
							if(tributacaoProduto.get("PIS").isIsentoOuNaoTributado()) {
								pis.setValorBaseCalculo(CurrencyUtil.arredondarValorParaDuasCasas(BigDecimal.valueOf(0)));
								pis.setValorAliquota(CurrencyUtil.arredondarValorParaDuasCasas(BigDecimal.valueOf(0)));
								pis.setPercentualAliquota(CurrencyUtil.arredondarValorParaDuasCasas(BigDecimal.valueOf(0)));	
								pis.setValor(CurrencyUtil.arredondarValorParaDuasCasas(BigDecimal.valueOf(0)));
							} else {
								pis.setValorBaseCalculo(CurrencyUtil.arredondarValorParaDuasCasas(tributacaoProduto.get("PIS").getBaseCalculo()));
								pis.setValorAliquota(CurrencyUtil.arredondarValorParaDuasCasas(tributoPis.getValor()));
								pis.setPercentualAliquota(CurrencyUtil.arredondarValorParaDuasCasas(tributoPis.getValor()));
								pis.setValor(CurrencyUtil.arredondarValorParaDuasCasas(produtoServico.getValorTotalBruto().multiply(tributoPis.getValor().divide(BigDecimal.valueOf(100)))));
							}
							break;
						}
					}
				}
				
			}
			
			// FIXME Ajustar CST
			pisWrapper.setPis(pis);
			
			detalheNotaFiscal.getImpostos().setPis(pisWrapper);
			
		} else {
			StringBuilder sb = new StringBuilder().append("PIS não encontrado para o Produto: ")
					.append(produtoServico.getCodigoProduto())
					.append(" / ")
					.append(produtoServico.getProdutoEdicao().getNumeroEdicao());
			
			LOGGER.error(sb.toString() );
			throw new ValidacaoException(TipoMensagem.ERROR, sb.toString());
		}
		
		if(tributoCofins != null){
			
			CofinsWrapper cofinsWrapper = new CofinsWrapper();	
			COFINS cofins = new COFINS();
			cofins.setValorBaseCalculo(CurrencyUtil.arredondarValorParaDuasCasas(BigDecimal.valueOf(0)));
			cofins.setValorAliquota(CurrencyUtil.arredondarValorParaDuasCasas(BigDecimal.valueOf(0)));
			cofins.setPercentualAliquota(CurrencyUtil.arredondarValorParaDuasCasas(BigDecimal.valueOf(0)));
			cofins.setValor(CurrencyUtil.arredondarValorParaDuasCasas(BigDecimal.valueOf(0)));
			
			if(tributacaoProduto.get("COFINS") != null) {
				
				cofins.setCst(tributacaoProduto.get("COFINS").getCst());
				
				if(notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getNaturezaOperacao().getTributosNaturezaOperacao() != null) {
					
					for(Tributo t : notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getNaturezaOperacao().getTributosNaturezaOperacao()) {
						if("COFINS".equals(t.getNome())) {
							if(tributacaoProduto.get("COFINS").isIsentoOuNaoTributado()) {
								cofins.setValorBaseCalculo(CurrencyUtil.arredondarValorParaDuasCasas(BigDecimal.valueOf(0)));
								cofins.setValorAliquota(CurrencyUtil.arredondarValorParaDuasCasas(BigDecimal.valueOf(0)));
								cofins.setPercentualAliquota(CurrencyUtil.arredondarValorParaDuasCasas(BigDecimal.valueOf(0)));
								cofins.setValor(CurrencyUtil.arredondarValorParaDuasCasas(BigDecimal.valueOf(0)));
							} else {
								cofins.setValorBaseCalculo(CurrencyUtil.arredondarValorParaDuasCasas(tributacaoProduto.get("COFINS").getBaseCalculo()));
								cofins.setValorAliquota(CurrencyUtil.arredondarValorParaDuasCasas(tributoCofins.getValor()));
								cofins.setPercentualAliquota(CurrencyUtil.arredondarValorParaDuasCasas(tributoCofins.getValor()));
								cofins.setValor(CurrencyUtil.arredondarValorParaDuasCasas(produtoServico.getValorTotalBruto().multiply(tributoCofins.getValor().divide(BigDecimal.valueOf(100)))));
							}
							break;
						}
					}
				}
				
			}
			
			cofinsWrapper.setCofins(cofins);
			
			detalheNotaFiscal.getImpostos().setCofins(cofinsWrapper);
			
		} else {
			StringBuilder sb = new StringBuilder().append("COFINS não encontrado para o Produto: ")
					.append(produtoServico.getCodigoProduto())
					.append(" / ")
					.append(produtoServico.getProdutoEdicao().getNumeroEdicao());
			
			LOGGER.error(sb.toString() );
			throw new ValidacaoException(TipoMensagem.ERROR, sb.toString());
		}
		
		//FIXME: Ajustar o codigo Excessao do ipi
		//produtoServico.setExtipi(0L);
		String cfop = "";
		if(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().getPais().equals(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().getPais())) {
			
			if(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoDestinatario().getEndereco().getUf().equals(notaFiscal.getNotaFiscalInformacoes().getIdentificacaoEmitente().getEndereco().getUf())) {
				
				cfop = notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getNaturezaOperacao().getCfopEstado();
				
			} else {
				
				cfop = notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getNaturezaOperacao().getCfopOutrosEstados();
				
			}
			
		} else {
			
			cfop = notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getNaturezaOperacao().getCfopExterior();
			
		}
		
		produtoServico.setCfop(Integer.valueOf(cfop));
	}

	public static void montaItemNotaFiscal(NotaFiscal notaFiscal, MovimentoFechamentoFiscal movimentoFechamentoFiscal, Map<String, TributoAliquota> tributoAliquota) {

		if(!((movimentoFechamentoFiscal instanceof MovimentoFechamentoFiscalCota) || (movimentoFechamentoFiscal instanceof MovimentoFechamentoFiscalFornecedor))) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de Movimento Fiscal não suportado!");
		}
		
		DetalheNotaFiscal detalheNotaFiscal = null;
		if(notaFiscal == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Problemas ao gerar Nota Fiscal. Objeto nulo.");
		} else {
			if(notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal() == null) {
				notaFiscal.getNotaFiscalInformacoes().setDetalhesNotaFiscal(new ArrayList<DetalheNotaFiscal>());
			}
		}
		
		if(notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().size() == 0) {
			
			detalheNotaFiscal = new DetalheNotaFiscal();
			detalheNotaFiscal = montarItem(movimentoFechamentoFiscal, new DetalheNotaFiscal(), notaFiscal, tributoAliquota);
			
		} else {
			
			boolean notFound = true;
			for(DetalheNotaFiscal dnf : notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal()) {
				
				if(dnf.getProdutoServico() == null) {
					
					throw new ValidacaoException(TipoMensagem.ERROR, "Item de nota fiscal nulo.");
				}
				
				if(movimentoFechamentoFiscal instanceof MovimentoFechamentoFiscalCota || (movimentoFechamentoFiscal instanceof MovimentoFechamentoFiscalFornecedor)) {
					if(((MovimentoFechamentoFiscalCota) movimentoFechamentoFiscal).getProdutoEdicao().getId().equals(dnf.getProdutoServico().getProdutoEdicao().getId())) {
						
						notFound = false;
						break;
					}
				}
				
			}
			
			if(notFound) {
				
				detalheNotaFiscal = new DetalheNotaFiscal();
				detalheNotaFiscal = montarItem(movimentoFechamentoFiscal, detalheNotaFiscal, notaFiscal, tributoAliquota);
				
			} else {
				
				for(DetalheNotaFiscal dnf : notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal()) {
					
					if(movimentoFechamentoFiscal instanceof MovimentoFechamentoFiscalCota || (movimentoFechamentoFiscal instanceof MovimentoFechamentoFiscalFornecedor)) {
						
						if(movimentoFechamentoFiscal.getProdutoEdicao().getId().equals(dnf.getProdutoServico().getProdutoEdicao().getId())) {
							
							montarItem(movimentoFechamentoFiscal, dnf, notaFiscal, tributoAliquota);
						}
					}
					
				}
			}
		}
		
		if(detalheNotaFiscal != null && detalheNotaFiscal.getProdutoServico() != null){ 
			// popular os itens das notas fiscais
			notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().add(detalheNotaFiscal);
		}
		
	}
	
	private static DetalheNotaFiscal montarItem(MovimentoFechamentoFiscal movimentoFechamentoFiscal,
			DetalheNotaFiscal detalheNotaFiscal, NotaFiscal notaFiscal, Map<String, TributoAliquota> tributoAliquota) {
		
		String codigoBarras = null;
		ProdutoServico produtoServico = null;
		if(detalheNotaFiscal.getProdutoServico() == null) {
			
			try {
				
				if(movimentoFechamentoFiscal.getProdutoEdicao().getCodigoDeBarras() == null) {
					
					codigoBarras = StringUtils.leftPad("0", 13, '0').substring(0, 13);
				} else {
					
					String cb = movimentoFechamentoFiscal.getProdutoEdicao().getCodigoDeBarras();
					codigoBarras = StringUtils.leftPad(cb, 13, '0').substring(0, 13);
				}
				
			} catch (Exception e) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Código de barras inválido: "+ movimentoFechamentoFiscal.getProdutoEdicao().getProduto().getCodigo() +" / "+ movimentoFechamentoFiscal.getProdutoEdicao().getNumeroEdicao());
			}
			
			produtoServico = new ProdutoServico();
			produtoServico.setCodigoBarras(codigoBarras);
			produtoServico.setNcm(movimentoFechamentoFiscal.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getCodigo());
			produtoServico.setUnidade(movimentoFechamentoFiscal.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
			produtoServico.setCodigoProduto(movimentoFechamentoFiscal.getProdutoEdicao().getProduto().getCodigo());
			produtoServico.setDescricaoProduto(movimentoFechamentoFiscal.getProdutoEdicao().getProduto().getNome());
			produtoServico.setValorTotalBruto(BigDecimal.ZERO);
			produtoServico.setQuantidade(BigInteger.ZERO);
		} else {
			
			produtoServico = detalheNotaFiscal.getProdutoServico();
		}
		
		BigDecimal valorTotalBruto = BigDecimal.ZERO;
		BigDecimal valorUnitario = BigDecimal.ZERO;
		BigDecimal valorDesconto = BigDecimal.ZERO;
		if(movimentoFechamentoFiscal instanceof MovimentoFechamentoFiscalCota) {
			
			valorUnitario = ((MovimentoFechamentoFiscalCota) movimentoFechamentoFiscal).getValoresAplicados().getPrecoComDesconto();
			valorDesconto = ((MovimentoFechamentoFiscalCota) movimentoFechamentoFiscal).getValoresAplicados().getValorDesconto();
			valorTotalBruto = ((MovimentoFechamentoFiscalCota) movimentoFechamentoFiscal).getValoresAplicados().getPrecoComDesconto().multiply(new BigDecimal(movimentoFechamentoFiscal.getQtde()));
			
		} else if(movimentoFechamentoFiscal instanceof MovimentoFechamentoFiscalFornecedor) {
			
			BigDecimal precoVenda = movimentoFechamentoFiscal.getProdutoEdicao().getPrecoVenda();
			BigDecimal precoComDesconto = BigDecimal.ZERO;
			
			if(movimentoFechamentoFiscal.getProdutoEdicao().getOrigem().equals(Origem.MANUAL)) {
				valorDesconto = movimentoFechamentoFiscal.getProdutoEdicao().getDesconto();
			} 
			
			precoComDesconto = precoVenda.subtract(precoVenda.multiply(valorDesconto.divide(BigDecimal.valueOf(100))));			
			valorUnitario = precoComDesconto;
			valorTotalBruto = precoComDesconto.multiply(new BigDecimal(movimentoFechamentoFiscal.getQtde()));
		} else {
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de movimento não suportado para geração da NF-e.");
		}
		
		produtoServico.setQuantidade(produtoServico.getQuantidade().add(movimentoFechamentoFiscal.getQtde()));
		produtoServico.setValorTotalBruto(CurrencyUtil.arredondarValorParaDuasCasas(produtoServico.getValorTotalBruto().add(valorTotalBruto)));
		produtoServico.setValorUnitario(valorUnitario);
		
		// produtoServico.setValorDesconto(BigDecimal.ZERO);
		
		produtoServico.setValorCompoeValorNF(true);
		
		//FIXME: Ajustar os produtos para sinalizarem a inclusao do frete na nf
		produtoServico.setValorFreteCompoeValorNF(false);
		if(produtoServico.isValorFreteCompoeValorNF()) {
			//FIXME: Ajustar os produtos para trazer os valores, se necessario
			produtoServico.setValorFrete(BigDecimal.ZERO);
			produtoServico.setValorSeguro(BigDecimal.ZERO);
			produtoServico.setValorOutros(BigDecimal.ZERO);
		}
		
		if(produtoServico.getProdutoEdicao() == null) {			
			produtoServico.setProdutoEdicao(movimentoFechamentoFiscal.getProdutoEdicao());
		}
		
		detalheNotaFiscal.setImpostos(new Impostos());
		
		detalheNotaFiscal.setProdutoServico(produtoServico);
		
		Map<String, Tributacao> tributacaoProduto = new HashMap<String, Tributacao>();
		TipoOperacao to = notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getNaturezaOperacao().getTipoOperacao();
		for (Tributacao t : movimentoFechamentoFiscal.getProdutoEdicao().getProduto().getTipoProduto().getProdutoTributacao()) {
			if(to.name().equals(t.getTipoOperacao().name()) || t.getTipoOperacao().equals(TributacaoTipoOperacao.ENTRADA_SAIDA)) {
				tributacaoProduto.put(t.getTributo(), t);
			}
		}
		
		// Aplica / calcula a tributacao do item
		aplicarTributacaoItem(detalheNotaFiscal, notaFiscal, tributoAliquota, produtoServico, tributacaoProduto);
		
		List<OrigemItemNotaFiscal> origemItens = produtoServico.getOrigemItemNotaFiscal() != null ? produtoServico.getOrigemItemNotaFiscal() : new ArrayList<OrigemItemNotaFiscal>();
		
		if(movimentoFechamentoFiscal instanceof MovimentoFechamentoFiscalCota) {
			
			if(notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getNaturezaOperacao().isNotaFiscalDevolucaoSimbolica()) {
				
				movimentoFechamentoFiscal.setNotaFiscalDevolucaoSimbolicaEmitida(true);
				
			} else if(notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getNaturezaOperacao().isNotaFiscalVendaConsignado()) {
				
				((MovimentoFechamentoFiscalCota) movimentoFechamentoFiscal).setNotaFiscalVendaEmitida(true);
				
			} else {
				
				throw new ValidacaoException(TipoMensagem.ERROR, "Natureza de Operação incompatível com o Movimento Fiscal.");
			}
			
			OrigemItemNotaFiscal oinf = new OrigemItemNotaFiscalMovimentoFechamentoFiscalCota();
			((OrigemItemNotaFiscalMovimentoFechamentoFiscalCota) oinf).setMovimentoFechamentoFiscalCota((MovimentoFechamentoFiscalCota) movimentoFechamentoFiscal);
			origemItens.add(oinf);
		} else if(movimentoFechamentoFiscal instanceof MovimentoFechamentoFiscalFornecedor) {
			
			OrigemItemNotaFiscal oinf = new OrigemItemNotaFiscalMovimentoEstoqueCota();
			((OrigemItemNotaFiscalMovimentoFechamentoFiscalFornecedor) oinf).setMovimentoFechamentoFiscalFornecedor((MovimentoFechamentoFiscalFornecedor) movimentoFechamentoFiscal);
			origemItens.add(oinf);
		}
		
		produtoServico.setOrigemItemNotaFiscal(origemItens);
		
		if(detalheNotaFiscal.getProdutoServicoPK() == null) {
			detalheNotaFiscal.setProdutoServicoPK(new ProdutoServicoPK());
			detalheNotaFiscal.getProdutoServicoPK().setSequencia(notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().size()+1);
			detalheNotaFiscal.getProdutoServicoPK().setNotaFiscal(notaFiscal);
		}
		
		return detalheNotaFiscal;
	}

	public static void montaItemNotaFiscal(NotaFiscal notaFiscal, MovimentoEstoque movimentoEstoque, Map<String, TributoAliquota> tributoAliquota) {

		DetalheNotaFiscal detalheNotaFiscal = new DetalheNotaFiscal();
		if(notaFiscal == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Problemas ao gerar Nota Fiscal. Objeto nulo.");
		} else {
			if(notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal() == null) {
				notaFiscal.getNotaFiscalInformacoes().setDetalhesNotaFiscal(new ArrayList<DetalheNotaFiscal>());
			}
		}
		
		if(notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().size() == 0) {
			
			montarItem(movimentoEstoque, detalheNotaFiscal, notaFiscal, tributoAliquota);
			
		} else {
			
			for(DetalheNotaFiscal dnf : notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal()) {
				
				boolean notFound = true;

				if(dnf.getProdutoServico() == null) {
					throw new ValidacaoException(TipoMensagem.ERROR, "Item de nota fiscal nulo.");
				}
				
				for(OrigemItemNotaFiscal oinf : dnf.getProdutoServico().getOrigemItemNotaFiscal()) {
					
					MovimentoEstoque me = ((OrigemItemNotaFiscalMovimentoEstoque) oinf).getMovimentoEstoque();
					if(me.getProdutoEdicao().getId().equals(movimentoEstoque.getProdutoEdicao().getId())) {
						
						notFound = false;
						
						montarItem(movimentoEstoque, dnf, notaFiscal, tributoAliquota);

					}
					
				}
				
				if(notFound) {
					
					montarItem(movimentoEstoque, detalheNotaFiscal, notaFiscal, tributoAliquota);
					
				}
			}
			
		}
		
		// popular os itens das notas fiscais
		// notaFiscalItem.setNotaFiscal(notaFiscal2);
		notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().add(detalheNotaFiscal);
		
	}
}