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
import br.com.abril.nds.dto.PagamentoDividasDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDividasCotaDTO;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.ParametroCobrancaCotaService;
import br.com.abril.nds.service.PoliticaCobrancaService;
import br.com.abril.nds.util.CurrencyUtil;
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
	
	@Autowired
	protected DistribuidorService distribuidorService;
	
	@Autowired
	protected CalendarioService calendarioService;


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
			cobrancaVO.setDataEmissao(itemCobranca.getDataEmissao()!=null?DateUtil.formatarData(itemCobranca.getDataEmissao(),"dd/MM/yyyy"):"");
			cobrancaVO.setDataVencimento(itemCobranca.getDataVencimento()!=null?DateUtil.formatarData(itemCobranca.getDataVencimento(),"dd/MM/yyyy"):"");
			cobrancaVO.setValor(itemCobranca.getValor()!=null?CurrencyUtil.formatarValor(itemCobranca.getValor()):"");
			listaCobrancaVO.add(cobrancaVO);
		}
		return listaCobrancaVO;
	}

	
	/**
	 * Método responsável por obter dados de cobrança por código
	 * @param idCobranca
	 * @return value object com dados da cobranca encontrada
	 */
	@Override
	@Transactional(readOnly=true)
	public CobrancaVO obterDadosCobranca(Long idCobranca) {
		//PARAMETROS PARA CALCULO DE JUROS E MULTA
		Distribuidor distribuidor = distribuidorService.obter();
        Date dataOperacao = distribuidor.getDataOperacao();
		
		CobrancaVO cobranca=null;
		
		Cobranca cob = cobrancaRepository.buscarPorId(idCobranca);
		
		if ((cob!=null)&&(cob.getStatusCobranca()==StatusCobranca.NAO_PAGO)){
			
			cobranca = new CobrancaVO();
			
			cobranca.setNossoNumero(cob.getNossoNumero());	
			
			String cota = "";
			
			if ((cob.getCota().getPessoa()) instanceof PessoaFisica){
				cota = cob.getCota().getNumeroCota()+"-"+((PessoaFisica) cob.getCota().getPessoa()).getNome();
			}
			
			if ((cob.getCota().getPessoa()) instanceof PessoaJuridica){
				cota = cob.getCota().getNumeroCota()+"-"+((PessoaJuridica) cob.getCota().getPessoa()).getRazaoSocial();
			}
			
			cobranca.setCota(cota);
			cobranca.setBanco(cob.getBanco().getNome());
			cobranca.setDataVencimento((cob.getDataVencimento()!=null?DateUtil.formatarDataPTBR(cob.getDataVencimento()):""));
			cobranca.setDataEmissao((cob.getDataEmissao()!=null?DateUtil.formatarDataPTBR(cob.getDataEmissao()):""));
			cobranca.setValor(CurrencyUtil.formatarValor(cob.getValor()));
			cobranca.setDividaTotal(CurrencyUtil.formatarValor(cob.getDivida().getValor()));
			
			//CALCULO DE JUROS E MULTA
			BigDecimal valorJurosCalculado = BigDecimal.ZERO;
			BigDecimal valorMultaCalculado = BigDecimal.ZERO;
			Date dataVencimentoUtil = calendarioService.adicionarDiasUteis(cob.getDataVencimento(), 0);
			
			if (dataVencimentoUtil.compareTo(dataOperacao) < 0) {
				
				//CALCULA JUROS
				valorJurosCalculado =
					this.calcularJuros(cob.getBanco(), cob.getCota(), distribuidor,
							           cob.getValor(), cob.getDataVencimento(),
									   dataOperacao);
				//CALCULA MULTA
				valorMultaCalculado =
					this.calcularMulta(cob.getBanco(), cob.getCota(), distribuidor,
							           cob.getValor());
			}
			
			cobranca.setDataPagamento( DateUtil.formatarDataPTBR(dataOperacao) );
			cobranca.setDesconto( CurrencyUtil.formatarValor(BigDecimal.ZERO) );
			cobranca.setJuros( CurrencyUtil.formatarValor(valorJurosCalculado) );
            cobranca.setMulta( CurrencyUtil.formatarValor(valorMultaCalculado) );
            
            //CALCULA VALOR TOTAL
            BigDecimal valorTotal =
            		cob.getValor().add(valorJurosCalculado).add(valorMultaCalculado);
            
			cobranca.setValorTotal( CurrencyUtil.formatarValor(valorTotal) );
		}
		return cobranca;
	}
	
	
	/**
	 * Método responsável por obter dados somados de cobranças por códigos
	 * @param List<Long> idCobrancas
	 * @return Data Transfer object com dados somados das cobrancas encontradas e calculadas.
	 */
	@Override
	@Transactional(readOnly=true)
	public PagamentoDividasDTO obterDadosCobrancas(List<Long> idCobrancas) {
	
		PagamentoDividasDTO pagamento = new PagamentoDividasDTO();
	
		BigDecimal totalJuros = new BigDecimal(0);
		BigDecimal totalMulta = new BigDecimal(0);
		BigDecimal totalDividas = new BigDecimal(0);
		BigDecimal totalPagamento = new BigDecimal(0);
		
		for (int i = 0; i<idCobrancas.size(); i++){
        
			CobrancaVO cobranca = this.obterDadosCobranca(idCobrancas.get(i));
	        totalJuros = totalJuros.add(CurrencyUtil.converterValor(cobranca.getJuros()));
	        totalMulta = totalMulta.add(CurrencyUtil.converterValor(cobranca.getMulta()));
	        totalDividas = totalDividas.add(CurrencyUtil.converterValor(cobranca.getValor()));
	        totalPagamento = totalPagamento.add(CurrencyUtil.converterValor(cobranca.getValorTotal()));
	        
		}
		
		pagamento.setDataPagamento(new Date());
		pagamento.setValorJuros(totalJuros);
		pagamento.setValorMulta(totalMulta);
		pagamento.setValorDividas(totalDividas);
		pagamento.setValorPagamento(totalPagamento);
		pagamento.setValorTotalDividas(totalPagamento);
		
		pagamento.setValorDesconto(BigDecimal.ZERO);
		
		//saldo da cota
		pagamento.setValorSaldo(BigDecimal.TEN);//!!
		

		return pagamento;
	}
	
}
