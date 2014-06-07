package br.com.abril.nds.service.builders;

import java.math.BigDecimal;
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
import br.com.abril.nds.model.cadastro.TributoAliquota;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscal;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscal;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscalMovimentoEstoqueCota;
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

		final DetalheNotaFiscal detalheNotaFiscal = new DetalheNotaFiscal();
		if(notaFiscal == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Problemas ao gerar Nota Fiscal. Objeto nulo.");
		} else {
			if(notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal() == null) {
				notaFiscal.getNotaFiscalInformacoes().setDetalhesNotaFiscal(new ArrayList<DetalheNotaFiscal>());
			}
		}
		
		if(notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().size() == 0) {
			
			montarItem(movimentoEstoqueCota, detalheNotaFiscal, notaFiscal, tributoAliquota);
			
		} else {
			
			for(DetalheNotaFiscal dnf : notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal()) {
				
				boolean notFound = true;

				if(dnf.getProdutoServico() == null) {
					throw new ValidacaoException(TipoMensagem.ERROR, "Item de nota fiscal nulo.");
				}
				
				for(OrigemItemNotaFiscal oinf : dnf.getProdutoServico().getOrigemItemNotaFiscal()) {
					
					MovimentoEstoqueCota mec = ((OrigemItemNotaFiscalMovimentoEstoqueCota) oinf).getMovimentoEstoqueCota();
					if(mec.getProdutoEdicao().getId().equals(movimentoEstoqueCota.getProdutoEdicao().getId())) {
						
						notFound = false;
						
						montarItem(movimentoEstoqueCota, dnf, notaFiscal, tributoAliquota);
						
					}
					
				}
				
				if(notFound) {
					
					montarItem(movimentoEstoqueCota, detalheNotaFiscal, notaFiscal, tributoAliquota);
					
				}
			}
		}
		
		if(detalheNotaFiscal != null && detalheNotaFiscal.getProdutoServico() != null){ 
			// popular os itens das notas fiscais
			// notaFiscalItem.setNotaFiscal(notaFiscal2);
			notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().add(detalheNotaFiscal);
		}
		
		
	}

	private static void montarItem(AbstractMovimentoEstoque movimentoEstoque,
			DetalheNotaFiscal detalheNotaFiscal, NotaFiscal notaFiscal, Map<String, TributoAliquota> tributoAliquota) {
		
		ProdutoServico produtoServico = new ProdutoServico();
		
		produtoServico.setCodigoProduto(movimentoEstoque.getProdutoEdicao().getProduto().getCodigo());
		produtoServico.setDescricaoProduto(movimentoEstoque.getProdutoEdicao().getProduto().getNome());
		
		Long codigoBarras = null;
		try {
			if(movimentoEstoque.getProdutoEdicao().getCodigoDeBarras() == null){
				codigoBarras = Long.parseLong(StringUtils.leftPad("0", 13, '0').substring(0, 13));
			}else{
				
				String cb = movimentoEstoque.getProdutoEdicao().getCodigoDeBarras();
				codigoBarras = Long.parseLong(StringUtils.leftPad(cb, 13, '0').substring(0, 13));
			}
			
		} catch (Exception e) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Código de barras inválido: "+ movimentoEstoque.getProdutoEdicao().getProduto().getCodigo() +" / "+ movimentoEstoque.getProdutoEdicao().getNumeroEdicao());
		}
		
		produtoServico.setCodigoBarras(codigoBarras);
		produtoServico.setNcm(movimentoEstoque.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getCodigo());
		produtoServico.setQuantidade(movimentoEstoque.getQtde());
		produtoServico.setUnidade(movimentoEstoque.getProdutoEdicao().getProduto().getTipoProduto().getNcm().getUnidadeMedida());
		
		BigDecimal valorTotalBruto = BigDecimal.ZERO;
		BigDecimal valorUnitario = BigDecimal.ZERO;
		BigDecimal valorDesconto = BigDecimal.ZERO;
		if(movimentoEstoque instanceof MovimentoEstoqueCota) {
			valorTotalBruto = CurrencyUtil.arredondarValorParaDuasCasas(((MovimentoEstoqueCota) movimentoEstoque).getValoresAplicados().getPrecoComDesconto().multiply(new BigDecimal(movimentoEstoque.getQtde())));
			valorTotalBruto = valorUnitario = CurrencyUtil.arredondarValorParaQuatroCasas(((MovimentoEstoqueCota) movimentoEstoque).getValoresAplicados().getPrecoComDesconto());
			valorDesconto = ((MovimentoEstoqueCota) movimentoEstoque).getValoresAplicados().getValorDesconto();
		} else if(movimentoEstoque instanceof MovimentoEstoque) {
			
			BigDecimal precoVenda = ((MovimentoEstoque) movimentoEstoque).getProdutoEdicao().getPrecoVenda();
			BigDecimal precoComDesconto = BigDecimal.ZERO;
			
			if(((MovimentoEstoque) movimentoEstoque).getProdutoEdicao().getOrigem().equals(Origem.MANUAL)) {
				valorDesconto = ((MovimentoEstoque) movimentoEstoque).getProdutoEdicao().getDesconto();
			} 
			
			precoComDesconto = precoVenda.multiply(valorDesconto.divide(BigDecimal.valueOf(100)));
			
			valorTotalBruto = CurrencyUtil.arredondarValorParaDuasCasas(precoComDesconto.multiply(new BigDecimal(movimentoEstoque.getQtde())));
			valorUnitario = CurrencyUtil.arredondarValorParaQuatroCasas(precoComDesconto);
		} else {
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de movimento não suportado para geração da NF-e.");
		}
		produtoServico.setValorTotalBruto(valorTotalBruto);
		produtoServico.setValorUnitario(valorUnitario);
		
		produtoServico.setValorDesconto(BigDecimal.ZERO);
		
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
			produtoServico.setValorAliquotaIPI(CurrencyUtil.arredondarValorParaDuasCasas(ipiProduto.getValorAliquota()));
			
			IPI ipi = new IPI();
			
			ipi.setCst(ipiProduto.getCst().toString());
			ipi.setAliquota(ipiProduto.getValorAliquota());
			ipi.setValorBaseCalculo(ipiProduto.getBaseCalculo());
			ipi.setCodigoEnquadramento(ipiProduto.getCst());
			ipi.setIPITrib(new TribIPI());
			ipi.getIPITrib().setCst(ipiProduto.getCst());
			ipi.getIPITrib().setValorAliquota(CurrencyUtil.arredondarValorParaDuasCasas(ipiProduto.getValorAliquota()));
			ipi.getIPITrib().setValorBaseCalculo(CurrencyUtil.arredondarValorParaDuasCasas(ipiProduto.getBaseCalculo()));
			
			// FIX ME ajustar a classe de valor do ipi
			ipi.getIPITrib().setValorIPI(CurrencyUtil.arredondarValorParaDuasCasas(ipiProduto.getBaseCalculo()));
			detalheNotaFiscal.getImpostos().setIpi(ipi);
		} else {
			StringBuilder sb = new StringBuilder().append("IPI não encontrado para o Produto: ")
					.append(produtoServico.getCodigoProduto())
					.append(" / ")
					.append(produtoServico.getProdutoEdicao().getNumeroEdicao());
			
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
			
			if(tributacaoProduto.get("PIS") != null) {
				
				pis.setCst(tributacaoProduto.get("PIS").getCst());
				
				if(tributacaoProduto.get("PIS").isIsentoOuNaoTributado()) {
					pis.setValorBaseCalculo(BigDecimal.valueOf(0));
					pis.setValorAliquota(BigDecimal.valueOf(0));
					pis.setPercentualAliquota(BigDecimal.valueOf(0));	
				} else {
					pis.setValorBaseCalculo(tributacaoProduto.get("PIS").getBaseCalculo());
					pis.setValorAliquota(tributoPis.getValor());
					pis.setPercentualAliquota(tributoPis.getValor());
				}
			}
			pis.setValor(produtoServico.getValorTotalBruto().multiply(tributoPis.getValor().divide(BigDecimal.valueOf(100))));
			
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
			
			if(tributacaoProduto.get("COFINS") != null) {
				
				cofins.setCst(tributacaoProduto.get("COFINS").getCst());
				
				if(tributacaoProduto.get("COFINS").isIsentoOuNaoTributado()) {
					cofins.setValorBaseCalculo(BigDecimal.valueOf(0));
					cofins.setValorAliquota(BigDecimal.valueOf(0));
					cofins.setPercentualAliquota(BigDecimal.valueOf(0));	
				} else {
					cofins.setValorBaseCalculo(tributacaoProduto.get("COFINS").getBaseCalculo());
					cofins.setValorAliquota(tributoCofins.getValor());
					cofins.setPercentualAliquota(tributoCofins.getValor());
				}
			}
			cofins.setValor(produtoServico.getValorTotalBruto().multiply(tributoCofins.getValor().divide(BigDecimal.valueOf(100))));
			
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
		
		produtoServico.setCfop(Integer.valueOf(cfop));
		
		movimentoEstoque.setNotaFiscalEmitida(true);
		
		List<OrigemItemNotaFiscal> origemItens = produtoServico.getOrigemItemNotaFiscal() != null ? produtoServico.getOrigemItemNotaFiscal() : new ArrayList<OrigemItemNotaFiscal>();
		
		if(movimentoEstoque instanceof MovimentoEstoqueCota) {
			
			OrigemItemNotaFiscal oinf = new OrigemItemNotaFiscalMovimentoEstoqueCota();
			((OrigemItemNotaFiscalMovimentoEstoqueCota) oinf).setMovimentoEstoqueCota((MovimentoEstoqueCota) movimentoEstoque);
			origemItens.add(oinf);
		}
		produtoServico.setOrigemItemNotaFiscal(origemItens);
		
		if(detalheNotaFiscal.getProdutoServicoPK() == null){
			detalheNotaFiscal.setProdutoServicoPK(new ProdutoServicoPK());
			detalheNotaFiscal.getProdutoServicoPK().setSequencia(notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().size()+1);
			detalheNotaFiscal.getProdutoServicoPK().setNotaFiscal(notaFiscal);
		}
	}

	public static void montaItemNotaFiscal(NotaFiscal notaFiscal,
			MovimentoFechamentoFiscal movimentoFechamentoFiscal,
			Map<String, TributoAliquota> tributoRegimeTributario) {
		// TODO Auto-generated method stub
		
	}
	
	public static void montaItemNotaFiscal(NotaFiscal notaFiscal, MovimentoEstoque movimentoEstoque, Map<String, TributoAliquota> tributoAliquota) {

		final DetalheNotaFiscal detalheNotaFiscal = new DetalheNotaFiscal();
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
					
					MovimentoEstoqueCota mec = ((OrigemItemNotaFiscalMovimentoEstoqueCota) oinf).getMovimentoEstoqueCota();
					if(mec.getProdutoEdicao().getId().equals(movimentoEstoque.getProdutoEdicao().getId())) {
						
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