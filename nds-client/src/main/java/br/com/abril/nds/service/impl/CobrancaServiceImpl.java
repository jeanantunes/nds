package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.CobrancaVO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDividasCotaDTO;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.ParametroCobrancaCotaService;
import br.com.abril.nds.service.PoliticaCobrancaService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.MathUtil;

@Service
public class CobrancaServiceImpl implements CobrancaService {
	

	@Autowired
	private ParametroCobrancaCotaService financeiroService;
	
	@Autowired
	private PoliticaCobrancaService politicaCobrancaService;
	
	@Autowired
	private CobrancaRepository cobrancaRepository;


	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public BigDecimal calcularJuros(Banco banco, Cota cota, Distribuidor distribuidor,
									BigDecimal valor, Date dataVencimento, Date dataCalculoJuros) {

		BigDecimal taxaJurosMensal = BigDecimal.ZERO;

		BigDecimal valorCalculadoJuros = null;

		FormaCobranca formaCobrancaPrincipal = this.financeiroService.obterFormaCobrancaPrincipalCota(cota.getId());
        
		PoliticaCobranca politicaPrincipal = this.politicaCobrancaService.obterPoliticaCobrancaPrincipal();
		
		if (banco != null && banco.getJuros() != null ) {
			
			taxaJurosMensal = banco.getJuros();
		
		} else if (formaCobrancaPrincipal != null 
					&& formaCobrancaPrincipal.getTaxaJurosMensal() != null) {

			taxaJurosMensal = formaCobrancaPrincipal.getTaxaJurosMensal();
			
		} else if (politicaPrincipal != null
					&& politicaPrincipal.getFormaCobranca() != null
					&& politicaPrincipal.getFormaCobranca().getTaxaJurosMensal() != null) {

			taxaJurosMensal = politicaPrincipal.getFormaCobranca().getTaxaJurosMensal();
		}

		long quantidadeDias = DateUtil.obterDiferencaDias(dataVencimento, dataCalculoJuros);

		BigDecimal taxaJurosDiaria = MathUtil.divide(taxaJurosMensal, new BigDecimal(30));

		valorCalculadoJuros = valor.multiply(MathUtil.divide(taxaJurosDiaria, new BigDecimal(100)));

		return valorCalculadoJuros.multiply(new BigDecimal(quantidadeDias));
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public BigDecimal calcularMulta(Banco banco, Cota cota,
									Distribuidor distribuidor, BigDecimal valor) {
		
		FormaCobranca formaCobrancaPrincipal = this.financeiroService.obterFormaCobrancaPrincipalCota(cota.getId());

		BigDecimal taxaMulta = BigDecimal.ZERO;

		BigDecimal valorCalculadoMulta = null;

		PoliticaCobranca politicaPrincipal = this.politicaCobrancaService.obterPoliticaCobrancaPrincipal();
		
		if (banco != null && banco.getVrMulta() != null) {
		
			valorCalculadoMulta = banco.getVrMulta();
		
		} else {
			
			if (banco != null && banco.getMulta() != null) {
			
				taxaMulta = banco.getMulta();
			
			} else if (formaCobrancaPrincipal != null
						&& formaCobrancaPrincipal.getTaxaMulta() != null) {
	
				taxaMulta = formaCobrancaPrincipal.getTaxaMulta();
	
			} else if (politicaPrincipal != null
						&& politicaPrincipal.getFormaCobranca() != null
						&& politicaPrincipal.getFormaCobranca().getTaxaMulta() != null) {
	
				taxaMulta = politicaPrincipal.getFormaCobranca().getTaxaMulta();
			}
	
			valorCalculadoMulta = valor.multiply(MathUtil.divide(taxaMulta, new BigDecimal(100)));
		}
		
		return valorCalculadoMulta;
	}
	
	
	/**
	 * Método responsável por obter cobranças por numero da cota e vencimento
	 * @param filtro
	 * @return Lista de cobrancas encontradas
	 */
	@Override
	@Transactional(readOnly=true)
	public List<Cobranca> obterCobrancasPorCota(FiltroConsultaDividasCotaDTO filtro) {
		return this.cobrancaRepository.obterCobrancasPorCota(filtro);
	}
	
	/**
	 * Método responsável por obter quantidade cobranças por numero da cota e vencimento
	 * @param filtro
	 * @return int
	 */
	@Override
	@Transactional(readOnly=true)
	public int obterQuantidadeCobrancasPorCota(FiltroConsultaDividasCotaDTO filtro) {
		return (int) this.cobrancaRepository.obterQuantidadeCobrancasPorCota(filtro);
	}
	
	/**
	 * Método responsável por obter dados de cobranças por numero da cota e vencimento
	 * @param filtro
	 * @return Lista de value objects com dados de cobrancas encontradas
	 */
	@Override
	@Transactional(readOnly=true)
	public List<CobrancaVO> obterDadosCobrancasPorCota(FiltroConsultaDividasCotaDTO filtro) {
		List<Cobranca> cobrancas = this.cobrancaRepository.obterCobrancasPorCota(filtro);
		List<CobrancaVO> listaCobrancaVO = new ArrayList<CobrancaVO>();
		CobrancaVO cobrancaVO;
		for (Cobranca itemCobranca:cobrancas){
			cobrancaVO = new CobrancaVO();
			cobrancaVO.setCodigo(itemCobranca.getId()!=null?itemCobranca.getId().toString():"");
			cobrancaVO.setNome(itemCobranca.getCota()!=null?(itemCobranca.getCota().getPessoa()!=null?itemCobranca.getCota().getPessoa().getNome():""):"");
			cobrancaVO.setDataEmissao(itemCobranca.getDataEmissao()!=null?itemCobranca.getDataEmissao().toString():"");
			cobrancaVO.setDataVencimento(itemCobranca.getDataVencimento()!=null?itemCobranca.getDataVencimento().toString():"");
			cobrancaVO.setValor(itemCobranca.getValor()!=null?itemCobranca.getValor().toString():"");
			listaCobrancaVO.add(cobrancaVO);
		}
		return listaCobrancaVO;
	}
	
}
