package br.com.abril.nds.service.builders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Tributacao;
import br.com.abril.nds.model.cadastro.TributoAliquota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscal;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscalMovimentoEstoqueCota;
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
import br.com.abril.nds.util.CurrencyUtil;

public class ItemNotaFiscalBuilder  {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ItemNotaFiscalBuilder.class);

	public static void montaItemNotaFiscal(NotaFiscal notaFiscal, MovimentoEstoqueCota movimentoEstoqueCota, Map<String, TributoAliquota> tributoAliquota) {

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
						
						montarItem(movimentoEstoqueCota, detalheNotaFiscal, notaFiscal, tributoAliquota);
						
					}
					
				}
				
				if(notFound) {
					
					montarItem(movimentoEstoqueCota, detalheNotaFiscal, notaFiscal, tributoAliquota);
					
				}
			}
		}
		
		// popular os itens das notas fiscais
		// notaFiscalItem.setNotaFiscal(notaFiscal2);
		notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal().add(detalheNotaFiscal);
		
	}

	private static void montarItem(MovimentoEstoqueCota movimentoEstoqueCota,
			DetalheNotaFiscal detalheNotaFiscal, NotaFiscal notaFiscal, Map<String, TributoAliquota> tributoAliquota) {
		
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
				detalheNotaFiscal.getProdutoServico().setValorAliquotaICMS(CurrencyUtil.arredondarValorParaDuasCasas(t.getValorAliquota()));
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
				icms.setAliquota(CurrencyUtil.arredondarValorParaDuasCasas(t.getValorAliquota()));
				icms.setValorBaseCalculo(CurrencyUtil.arredondarValorParaDuasCasas(t.getBaseCalculo()));
				
				detalheNotaFiscal.getImpostos().setIcms(icms);
			}
			
			if("IPI".equals(t.getTributo())) {
				detalheNotaFiscal.getProdutoServico().setValorAliquotaIPI(CurrencyUtil.arredondarValorParaDuasCasas(t.getValorAliquota()));
				
				IPI ipi = new IPI();
				
				ipi.setCst(t.getCst().toString());
				ipi.setAliquota(t.getValorAliquota());
				ipi.setValorBaseCalculo(t.getBaseCalculo());
				ipi.setCodigoEnquadramento(t.getCst());
				ipi.setIPITrib(new TribIPI());
				ipi.getIPITrib().setCst(t.getCst());
				ipi.getIPITrib().setValorAliquota(CurrencyUtil.arredondarValorParaDuasCasas(t.getValorAliquota()));
				ipi.getIPITrib().setValorBaseCalculo(CurrencyUtil.arredondarValorParaDuasCasas(t.getBaseCalculo()));
				
				// FIX ME ajustar a classe de valor do ipi
				ipi.getIPITrib().setValorIPI(CurrencyUtil.arredondarValorParaDuasCasas(t.getBaseCalculo()));
				detalheNotaFiscal.getImpostos().setIpi(ipi);
			}
		}
		
		TributoAliquota tributoSimples = tributoAliquota.get("SIMPLES");
		
		if(tributoSimples != null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Regime tributario simples não suportado.");
		}
		
		TributoAliquota tributoPis = tributoAliquota.get("PIS");
		TributoAliquota tributoCofins = tributoAliquota.get("COFINS");
		
		if(tributoPis != null){
			PISWrapper pisWrapper = new PISWrapper();
			PIS pis = new PIS();
			// FIXME Ajustar CST
			pisWrapper.setPis(pis);
			pisWrapper.getPis().setCst("01");
			pisWrapper.getPis().setValorBaseCalculo(tributoPis.getValor());
			pisWrapper.getPis().setPercentualAliquota(tributoPis.getValor());			
			pisWrapper.getPis().setValor(detalheNotaFiscal.getProdutoServico().getValorTotalBruto().multiply(tributoPis.getValor().divide(BigDecimal.valueOf(100))));
			detalheNotaFiscal.getImpostos().setPis(pisWrapper);
		} else {
			PISWrapper pisWrapper = new PISWrapper();
			PIS pis = new PIS();
			pisWrapper.setPis(pis);
			// FIXME Ajustar CST
			pisWrapper.getPis().setCst("01");
			pisWrapper.getPis().setValorBaseCalculo(BigDecimal.valueOf(0));
			pisWrapper.getPis().setValor(BigDecimal.valueOf(0));
			pisWrapper.getPis().setPercentualAliquota(BigDecimal.valueOf(0));			
			detalheNotaFiscal.getImpostos().setPis(pisWrapper);
		}
		
		if(tributoCofins != null){
			
			CofinsWrapper cofinsWrapper = new CofinsWrapper();	
			COFINS cofins = new COFINS();
			
			cofinsWrapper.setCofins(cofins);
			
			// FIXME Ajustar CST
			cofinsWrapper.getCofins().setCst("01");
			cofinsWrapper.getCofins().setValorBaseCalculo(tributoCofins.getValor());
			cofinsWrapper.getCofins().setValorAliquota(detalheNotaFiscal.getProdutoServico().getValorTotalBruto().multiply(tributoCofins.getValor().divide(BigDecimal.valueOf(100))));
			
			detalheNotaFiscal.getImpostos().setCofins(cofinsWrapper);
		}else{
			CofinsWrapper cofinsWrapper = new CofinsWrapper();	
			COFINS cofins = new COFINS();
			
			cofinsWrapper.setCofins(cofins);		
			// FIXME Ajustar CST
			cofinsWrapper.getCofins().setCst("01");
			cofinsWrapper.getCofins().setValorBaseCalculo(BigDecimal.valueOf(0));
			cofinsWrapper.getCofins().setValor(BigDecimal.valueOf(0));
			cofinsWrapper.getCofins().setValorAliquota(BigDecimal.valueOf(0));
			
			detalheNotaFiscal.getImpostos().setCofins(cofinsWrapper);
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