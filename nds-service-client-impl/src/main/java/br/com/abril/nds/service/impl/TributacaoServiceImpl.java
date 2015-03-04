package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.nota.COFINS;
import br.com.abril.nds.model.fiscal.nota.EncargoFinanceiroProduto;
import br.com.abril.nds.model.fiscal.nota.ICMS;
import br.com.abril.nds.model.fiscal.nota.IPI;
import br.com.abril.nds.model.fiscal.nota.NotaFiscalTributacao;
import br.com.abril.nds.model.fiscal.nota.OrigemProduto;
import br.com.abril.nds.model.fiscal.nota.PIS;
import br.com.abril.nds.repository.TributacaoRepository;
import br.com.abril.nds.service.TributacaoService;

/**
 * 
 * @author Diego Fernandes
 * 
 */
@Service
public class TributacaoServiceImpl implements TributacaoService {
	private static final String IND_SIM = "S";

	private static final BigDecimal CEM = new BigDecimal("100.00");

	private static final MathContext NFE_DECIMAL_MC = new MathContext(5,
			RoundingMode.HALF_EVEN);

	@Autowired
	private TributacaoRepository tributacaoRepository;

	@Override
	@Transactional
	public EncargoFinanceiroProduto calcularTributoProduto(
			String codigoEmpresa, TipoOperacao tipoOperacao, String ufOrigem,
			String ufDestino, int naturezaOperacao,
			String codigoNaturezaOperacao, String codigoNBM, Date dataVigencia,
			String cstICMS, BigDecimal valorItem) {
		
		NotaFiscalTributacao tributacao = tributacaoRepository.buscar(codigoEmpresa,
				tipoOperacao, ufOrigem, ufDestino, naturezaOperacao,
				codigoNaturezaOperacao, codigoNBM, dataVigencia, cstICMS);

		if (tributacao == null) {
			tributacao = tributacaoRepository.buscar(codigoEmpresa,
					tipoOperacao, ufOrigem, "TE", naturezaOperacao,
					codigoNaturezaOperacao, codigoNBM, dataVigencia, cstICMS);
		}

		if (tributacao == null) {
			tributacao = tributacaoRepository.buscar(codigoEmpresa,
					tipoOperacao, "TE", "TE", naturezaOperacao,
					codigoNaturezaOperacao, codigoNBM, dataVigencia, cstICMS);
		}
		if (tributacao == null) {
			List<String> ufs = new ArrayList<String>();
			ufs.add(ufDestino);
			ufs.add(ufOrigem);
			ufs.add("TE");
			ufs.add("TETE");

			tributacao = tributacaoRepository.buscar(codigoEmpresa,
					tipoOperacao, ufs, naturezaOperacao,
					codigoNaturezaOperacao, codigoNBM, dataVigencia, cstICMS);
		}
		if (tributacao == null) {
			tributacao = tributacaoRepository.tributacaoDefault(codigoEmpresa,
					tipoOperacao, ufOrigem, ufDestino, naturezaOperacao,
					codigoNaturezaOperacao, codigoNBM, dataVigencia);
		}

		EncargoFinanceiroProduto encargoFinanceiroProduto = new EncargoFinanceiroProduto();

		encargoFinanceiroProduto.setIcms(calculaICMS(tributacao, valorItem));
		encargoFinanceiroProduto.setIpi(calculaIPI(tributacao, valorItem));
		encargoFinanceiroProduto.setPis(calculaPIS(tributacao, valorItem));
		encargoFinanceiroProduto
				.setCofins(calculaCOFINS(tributacao, valorItem));

		return encargoFinanceiroProduto;
	}

	private ICMS calculaICMS(NotaFiscalTributacao tributacao, BigDecimal valorItem) {
		ICMS icms = new ICMS();
		String cstICMS = tributacao.getCstICMS();

		int origem = Integer.valueOf(cstICMS.substring(0, 1));
		icms.setOrigem(OrigemProduto.values()[origem]);
		icms.setCst(cstICMS.substring(1, 3));

		if (IND_SIM.equals(tributacao.getIndicadorBaseCalculoICMS())) {
			icms.setValorBaseCalculo(valorItem);
			icms.setValor(valorItem.multiply(tributacao.getAliquotaICMS())
					.divide(CEM, NFE_DECIMAL_MC));
		}
		icms.setAliquota(tributacao.getAliquotaICMS());
		return icms;
	}

	private IPI calculaIPI(NotaFiscalTributacao tributacao, BigDecimal valorItem) {
		IPI ipi = new IPI();

		ipi.setCst(tributacao.getCstIPI());

		if (IND_SIM.equals(tributacao.getIndicadorBaseCalculoIPI())) {
			ipi.setValorBaseCalculo(valorItem);
			ipi.setValor(valorItem.multiply(tributacao.getAliquotaIPI()).divide(CEM, NFE_DECIMAL_MC));
		}
		ipi.setAliquota(tributacao.getAliquotaIPI());
		return ipi;
	}

	private PIS calculaPIS(NotaFiscalTributacao tributacao, BigDecimal valorItem) {
		PIS pis = new PIS();

		pis.setCst(tributacao.getCstPIS());

		pis.setValorBaseCalculo(valorItem);
		pis.setValor(valorItem.multiply(tributacao.getAliquotaIPI()).divide(CEM, NFE_DECIMAL_MC));

		pis.setPercentualAliquota(tributacao.getAliquotaPIS());
		return pis;
	}

	private COFINS calculaCOFINS(NotaFiscalTributacao tributacao, BigDecimal valorItem) {
		COFINS cofins = new COFINS();

		cofins.setCst(tributacao.getCstCOFINS());

		cofins.setValorBaseCalculo(valorItem);
		cofins.setValor(valorItem.multiply(tributacao.getAliquotaCOFINS()).divide(CEM, NFE_DECIMAL_MC));

		cofins.setPercentualAliquota(tributacao.getAliquotaCOFINS());
		return cofins;
	}

}
