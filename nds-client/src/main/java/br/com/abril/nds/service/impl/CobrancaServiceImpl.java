package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.FinanceiroService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.MathUtil;

@Service
public class CobrancaServiceImpl implements CobrancaService {
	

	@Autowired
	private FinanceiroService financeiroService;


	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public BigDecimal calcularJuros(Distribuidor distribuidor, Cota cota,
									BigDecimal valor, Date dataVencimento, Date dataCalculoJuros) {

		BigDecimal taxaJurosMensal = BigDecimal.ZERO;

		BigDecimal valorCalculadoJuros = null;

		FormaCobranca formaCobrancaPrincipal = this.financeiroService.obterFormaCobrancaPrincipalCota(cota.getId());
        
		if (cota.getParametroCobranca() != null
				&& formaCobrancaPrincipal != null
				&& formaCobrancaPrincipal.getTaxaJurosMensal() != null) {

			taxaJurosMensal = formaCobrancaPrincipal.getTaxaJurosMensal();
			
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
		
		FormaCobranca formaCobrancaPrincipal = this.financeiroService.obterFormaCobrancaPrincipalCota(cota.getId());

		BigDecimal taxaMulta = BigDecimal.ZERO;

		BigDecimal valorCalculadoMulta = null;

		if (cota.getParametroCobranca() != null
				&& formaCobrancaPrincipal != null
				&& formaCobrancaPrincipal.getTaxaMulta() != null) {

			taxaMulta = formaCobrancaPrincipal.getTaxaMulta();

		} else if (distribuidor.getPoliticaCobranca() != null
				&& distribuidor.getPoliticaCobranca().getFormaCobranca() != null
				&& distribuidor.getPoliticaCobranca().getFormaCobranca().getTaxaMulta() != null) {

			taxaMulta = distribuidor.getPoliticaCobranca().getFormaCobranca().getTaxaMulta();
		}

		valorCalculadoMulta = valor.multiply(MathUtil.divide(taxaMulta, new BigDecimal(100)));

		return valorCalculadoMulta;
	}
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public BigDecimal calcularJurosBanco(Banco banco, Distribuidor distribuidor, Cota cota,
									BigDecimal valor, Date dataVencimento, Date dataCalculoJuros) {

		FormaCobranca formaCobrancaPrincipal = this.financeiroService.obterFormaCobrancaPrincipalCota(cota.getId());

		BigDecimal taxaJurosMensal = BigDecimal.ZERO;

		BigDecimal valorCalculadoJuros = null;

		if ((banco !=null) && (banco.getJuros()!=null)){
			taxaJurosMensal = banco.getJuros();
		}
		else if (cota.getParametroCobranca() != null
				&& formaCobrancaPrincipal != null
				&& formaCobrancaPrincipal.getTaxaJurosMensal() != null) {

			taxaJurosMensal = formaCobrancaPrincipal.getTaxaJurosMensal();

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
	public BigDecimal calcularMultaBanco(Banco banco, Distribuidor distribuidor, Cota cota, BigDecimal valor) {

		FormaCobranca formaCobrancaPrincipal = this.financeiroService.obterFormaCobrancaPrincipalCota(cota.getId());

		BigDecimal taxaMulta = BigDecimal.ZERO;

		BigDecimal valorCalculadoMulta = null;
		
		if ((banco !=null) && (banco.getVrMulta()!=null)){
			valorCalculadoMulta = banco.getVrMulta();
		}
		else{
			if ((banco !=null) && (banco.getMulta()!=null)){
				taxaMulta = banco.getMulta();
			}
			else if (cota.getParametroCobranca() != null
					&& formaCobrancaPrincipal != null
					&& formaCobrancaPrincipal.getTaxaMulta() != null) {
	
				taxaMulta = formaCobrancaPrincipal.getTaxaMulta();
	
			}else if (distribuidor.getPoliticaCobranca() != null
					&& distribuidor.getPoliticaCobranca().getFormaCobranca() != null
					&& distribuidor.getPoliticaCobranca().getFormaCobranca().getTaxaMulta() != null) {
	
				taxaMulta = distribuidor.getPoliticaCobranca().getFormaCobranca().getTaxaMulta();
			}
			valorCalculadoMulta = valor.multiply(MathUtil.divide(taxaMulta, new BigDecimal(100)));
		}
		
		return valorCalculadoMulta;
	}

}
