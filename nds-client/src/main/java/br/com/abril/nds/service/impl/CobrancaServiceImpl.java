package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.MathUtil;

@Service
public class CobrancaServiceImpl implements CobrancaService {

	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public BigDecimal calcularJuros(Distribuidor distribuidor, Cota cota,
									BigDecimal valor, Date dataVencimento, Date dataCalculoJuros) {

		// TODO: distribuidor set de formas de cobrança?

		BigDecimal taxaJurosMensal = BigDecimal.ZERO;

		BigDecimal valorCalculadoJuros = null;

		if (cota.getParametroCobranca() != null
				&& cota.getParametroCobranca().getFormaCobranca() != null
				&& cota.getParametroCobranca().getFormaCobranca().getTaxaJurosMensal() != null) {

			taxaJurosMensal = cota.getParametroCobranca().getFormaCobranca().getTaxaJurosMensal();

		} else if (distribuidor.getPoliticaCobranca() != null
				&& distribuidor.getPoliticaCobranca().getFormaCobranca() != null
				&& distribuidor.getPoliticaCobranca().getFormaCobranca().getTaxaJurosMensal() != null) {

			taxaJurosMensal = distribuidor.getPoliticaCobranca().getFormaCobranca().getTaxaJurosMensal();
		}

		long quantidadeDias = DateUtil.obterDiferencaDias(dataVencimento, dataCalculoJuros);

		BigDecimal taxaJurosDiaria = MathUtil.divide(taxaJurosMensal, new BigDecimal(30));

		valorCalculadoJuros = valor.multiply(MathUtil.divide(taxaJurosDiaria, new BigDecimal(100)));

		return valorCalculadoJuros.multiply(new BigDecimal(quantidadeDias));
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public BigDecimal calcularMulta(Distribuidor distribuidor, Cota cota, BigDecimal valor) {

		BigDecimal taxaMulta = BigDecimal.ZERO;

		BigDecimal valorCalculadoMulta = null;

		if (cota.getParametroCobranca() != null
				&& cota.getParametroCobranca().getFormaCobranca() != null
				&& cota.getParametroCobranca().getFormaCobranca().getTaxaMulta() != null) {

			taxaMulta = cota.getParametroCobranca().getFormaCobranca().getTaxaMulta();

		} else if (distribuidor.getPoliticaCobranca() != null
				&& distribuidor.getPoliticaCobranca().getFormaCobranca() != null
				&& distribuidor.getPoliticaCobranca().getFormaCobranca().getTaxaMulta() != null) {

			taxaMulta = distribuidor.getPoliticaCobranca().getFormaCobranca().getTaxaMulta();
		}

		valorCalculadoMulta = valor.multiply(MathUtil.divide(taxaMulta, new BigDecimal(100)));

		return valorCalculadoMulta;
	}

}
