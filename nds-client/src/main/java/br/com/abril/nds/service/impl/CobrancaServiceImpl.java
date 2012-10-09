package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.CobrancaDividaVO;
import br.com.abril.nds.client.vo.CobrancaVO;
import br.com.abril.nds.client.vo.DetalhesDividaVO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.PagamentoDividasDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDividasCotaDTO;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.BaixaCobranca;
import br.com.abril.nds.model.financeiro.BaixaManual;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.financeiro.StatusBaixa;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.movimentacao.AbstractMovimentoFinanceiro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BaixaCobrancaRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
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
	
	@Autowired
	protected MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
	
	@Autowired
	protected CotaRepository cotaRepository;
	
	@Autowired
	protected MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	@Autowired
	protected TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	@Autowired
	protected BaixaCobrancaRepository baixaCobrancaRepository;



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
		
		List<CobrancaVO> listaCobrancaVO = null;
		boolean acumulaDivida = false;
		
	    Cota cota = this.cotaRepository.obterPorNumerDaCota(filtro.getNumeroCota());
	    
	    if (cota!=null){
	    	
	    	if (cota.getParametroCobranca()!=null){
	    		if(cota.getParametroCobranca().getPoliticaSuspensao()!=null){
	    			acumulaDivida = (cota.getParametroCobranca().getPoliticaSuspensao().getNumeroAcumuloDivida() > 0);
	    		}
	    	}
		
			filtro.setAcumulaDivida(acumulaDivida);
			
		    List<Cobranca> cobrancas = this.cobrancaRepository.obterCobrancasPorCota(filtro);
		    if ((cobrancas!=null)&&(cobrancas.size() > 0)){
		    	
				listaCobrancaVO = new ArrayList<CobrancaVO>();
				CobrancaVO cobrancaVO;
				for (Cobranca itemCobranca:cobrancas){
					
					cobrancaVO = new CobrancaVO();
					cobrancaVO.setCodigo(itemCobranca.getId()!=null?itemCobranca.getId().toString():"");
					cobrancaVO.setNome(itemCobranca.getCota()!=null?(itemCobranca.getCota().getPessoa()!=null?itemCobranca.getCota().getPessoa().getNome():""):"");
					cobrancaVO.setDataEmissao(itemCobranca.getDataEmissao()!=null?DateUtil.formatarData(itemCobranca.getDataEmissao(),"dd/MM/yyyy"):"");
					cobrancaVO.setDataVencimento(itemCobranca.getDataVencimento()!=null?DateUtil.formatarData(itemCobranca.getDataVencimento(),"dd/MM/yyyy"):"");
					cobrancaVO.setValor(itemCobranca.getValor()!=null?CurrencyUtil.formatarValor(itemCobranca.getValor()):"");
					cobrancaVO.setCheck(false);
					listaCobrancaVO.add(cobrancaVO);
					
				}
				
		    }
		    
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
			cobranca.setBanco((cob.getBanco()!=null?cob.getBanco().getNome():""));
			cobranca.setDataVencimento((cob.getDataVencimento()!=null?DateUtil.formatarDataPTBR(cob.getDataVencimento()):""));
			cobranca.setDataEmissao((cob.getDataEmissao()!=null?DateUtil.formatarDataPTBR(cob.getDataEmissao()):""));
			cobranca.setValor(CurrencyUtil.formatarValor(cob.getValor()));
			cobranca.setDividaTotal(CurrencyUtil.formatarValor(cob.getDivida().getValor()));	
			
			//CALCULO DE JUROS E MULTA
			BigDecimal valorJurosCalculado = BigDecimal.ZERO;
			BigDecimal valorMultaCalculado = BigDecimal.ZERO;
			Date dataVencimentoUtil = calendarioService.adicionarDiasUteis(cob.getDataVencimento(), 0);
			
			//CALCULA VALOR DO SALDO DA DIVIDA(MOVIMENTOS DE PAGAMENTO PARCIAL)
			BigDecimal saldoDivida = this.obterSaldoDivida(cob.getId());
			cobranca.setValorSaldo(CurrencyUtil.formatarValor(saldoDivida));
			
			if (dataVencimentoUtil.compareTo(dataOperacao) < 0) {
				
				//CALCULA JUROS
				valorJurosCalculado =
					this.calcularJuros(cob.getBanco(), cob.getCota(), distribuidor,
							           cob.getValor().subtract(saldoDivida), cob.getDataVencimento(),
									   dataOperacao);
				//CALCULA MULTA
				valorMultaCalculado =
					this.calcularMulta(cob.getBanco(), cob.getCota(), distribuidor,
							           cob.getValor().subtract(saldoDivida));
			}
			
			cobranca.setDataPagamento( DateUtil.formatarDataPTBR(dataOperacao) );
			cobranca.setDesconto( CurrencyUtil.formatarValor(BigDecimal.ZERO) );
			cobranca.setJuros( CurrencyUtil.formatarValor(valorJurosCalculado) );
            cobranca.setMulta( CurrencyUtil.formatarValor(valorMultaCalculado) );
            
            //CALCULA VALOR TOTAL
            BigDecimal valorTotal = cob.getValor().add(valorJurosCalculado).add(valorMultaCalculado);
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
	public CobrancaDividaVO obterDadosCobrancas(List<Long> idCobrancas) {
		
		CobrancaDividaVO pagamento = new CobrancaDividaVO();
	
		BigDecimal totalJuros = new BigDecimal(0);
		BigDecimal totalMulta = new BigDecimal(0);
		BigDecimal totalDividas = new BigDecimal(0);
		BigDecimal totalSaldoDividas = new BigDecimal(0);
		
		for (int i = 0; i<idCobrancas.size(); i++){
        
			CobrancaVO cobranca = this.obterDadosCobranca(idCobrancas.get(i));

	        totalJuros = totalJuros.add(CurrencyUtil.converterValor(cobranca.getJuros()));
	        totalMulta = totalMulta.add(CurrencyUtil.converterValor(cobranca.getMulta()));
	        totalDividas = totalDividas.add(CurrencyUtil.converterValor(cobranca.getValor()));
	        totalSaldoDividas = totalSaldoDividas.add(CurrencyUtil.converterValor(cobranca.getValorSaldo())); 
	        
		}
		
		pagamento.setValorJuros(CurrencyUtil.formatarValor(totalJuros));
		pagamento.setValorMulta(CurrencyUtil.formatarValor(totalMulta));
		pagamento.setValorDividas(CurrencyUtil.formatarValor(totalDividas.subtract(totalSaldoDividas)));
		pagamento.setValorPagamento(CurrencyUtil.formatarValor(totalDividas.add(totalJuros).add(totalMulta).subtract(totalSaldoDividas)));
		pagamento.setValorDesconto(CurrencyUtil.formatarValor(BigDecimal.ZERO));
		pagamento.setValorSaldo(CurrencyUtil.formatarValor(BigDecimal.ZERO));

		return pagamento;
	}
	
	
	/**
	 * Obtém saldo da Cota
	 * 
	 * @param idCobranca
	 * @return
	 */
	@Override
	@Transactional
	public BigDecimal obterSaldoCota(Integer numeroCota) {
		
		BigDecimal debito = this.movimentoFinanceiroCotaRepository.obterSaldoCotaPorOperacao(numeroCota, OperacaoFinaceira.DEBITO);
		BigDecimal credito = this.movimentoFinanceiroCotaRepository.obterSaldoCotaPorOperacao(numeroCota, OperacaoFinaceira.CREDITO);
        BigDecimal saldo = credito.subtract(debito);

		return saldo;
	}
	
	
	/**
	 * Obtém detalhes da Cobranca(Dívida)
	 * 
	 * @param idCobranca
	 * @return
	 */
	@Override
	@Transactional
	public List<DetalhesDividaVO> obterDetalhesDivida(Long idCobranca){

		DetalhesDividaVO detalhe;
		List<DetalhesDividaVO> detalhes = new ArrayList<DetalhesDividaVO>();
       
		Cobranca cobranca = this.cobrancaRepository.buscarPorId(idCobranca);
		detalhe = new DetalhesDividaVO();
		detalhe.setData(cobranca.getDataEmissao()!=null?DateUtil.formatarData(cobranca.getDataEmissao(),"dd/MM/yyyy"):"");
		detalhe.setObservacao("");
		detalhe.setValor(cobranca.getValor()!=null?CurrencyUtil.formatarValor(cobranca.getValor().multiply(new BigDecimal(-1))):"");
		detalhe.setTipo("Dívida");
		detalhes.add(detalhe);
		
		List<MovimentoFinanceiroCota> movimentos = this.movimentoFinanceiroCotaRepository.obterMovimentosFinanceirosPorCobranca(idCobranca);
        
		for(MovimentoFinanceiroCota item:movimentos){
			
			
			detalhe = new DetalhesDividaVO();
			detalhe.setData(item.getData()!=null?DateUtil.formatarData(item.getData(),"dd/MM/yyyy"):"");
			detalhe.setObservacao(item.getObservacao()!=null?item.getObservacao():"");
			detalhe.setValor(item.getValor()!=null?CurrencyUtil.formatarValor(item.getValor()):"");
			detalhe.setTipo("Pagamento");
			detalhes.add(detalhe);
			
			
			BaixaManual baixaManual = (BaixaManual) item.getBaixaCobranca();
			if (baixaManual!=null){
				
				if (baixaManual.getValorJuros().floatValue() > 0){
					detalhe = new DetalhesDividaVO();
					detalhe.setData(item.getData()!=null?DateUtil.formatarData(item.getData(),"dd/MM/yyyy"):"");
					detalhe.setObservacao(item.getObservacao()!=null?item.getObservacao():"");
					detalhe.setValor(baixaManual.getValorJuros()!=null?CurrencyUtil.formatarValor(baixaManual.getValorJuros().multiply(new BigDecimal(-1))):"");
					detalhe.setTipo("Juros");
					detalhes.add(detalhe);
				}
				
				if (baixaManual.getValorMulta().floatValue() > 0){
					detalhe = new DetalhesDividaVO();
					detalhe.setData(item.getData()!=null?DateUtil.formatarData(item.getData(),"dd/MM/yyyy"):"");
					detalhe.setObservacao(item.getObservacao()!=null?item.getObservacao():"");
					detalhe.setValor(baixaManual.getValorMulta()!=null?CurrencyUtil.formatarValor(baixaManual.getValorMulta().multiply(new BigDecimal(-1))):"");
					detalhe.setTipo("Multa");
					detalhes.add(detalhe);
			    }
				
			    if (baixaManual.getValorDesconto().floatValue() > 0){
					detalhe = new DetalhesDividaVO();
					detalhe.setData(item.getData()!=null?DateUtil.formatarData(item.getData(),"dd/MM/yyyy"):"");
					detalhe.setObservacao(item.getObservacao()!=null?item.getObservacao():"");
					detalhe.setValor(baixaManual.getValorDesconto()!=null?CurrencyUtil.formatarValor(baixaManual.getValorDesconto()):"");
					detalhe.setTipo("Desconto");
					detalhes.add(detalhe);
				}
			}
			
		}

	    return detalhes;
	}
	
	
	/**
	 * Obtém saldo da Cobranca(Dívida)
	 * 
	 * @param idCobranca
	 * @return
	 */
	@Override
	@Transactional
	public BigDecimal obterSaldoDivida(Long idCobranca){

		BigDecimal saldo = BigDecimal.ZERO;
		
		BigDecimal debito = this.movimentoFinanceiroCotaRepository.obterSaldoCobrancaPorOperacao(idCobranca, OperacaoFinaceira.DEBITO);
		if (debito==null){
			debito = BigDecimal.ZERO;
		}
		BigDecimal credito = this.movimentoFinanceiroCotaRepository.obterSaldoCobrancaPorOperacao(idCobranca, OperacaoFinaceira.CREDITO);
		if (credito==null){
			credito = BigDecimal.ZERO;
		}
        saldo = credito.subtract(debito);
        
		return saldo;
	}
	
	
	/**
	 *Método responsável por baixar dividas manualmente 
	 * @param pagamento
	 * @param idCobrancas
	 * @param manterPendente
	 */
	@Override
	@Transactional
	public void baixaManualDividas(PagamentoDividasDTO pagamento,List<Long> idCobrancas,Boolean manterPendente) {
		
		StatusAprovacao statusAprovacao = StatusAprovacao.APROVADO;
		if (manterPendente){
			statusAprovacao = StatusAprovacao.PENDENTE;
		}
		
		BigDecimal valorJuros = pagamento.getValorJuros();
		BigDecimal valorMulta = pagamento.getValorMulta();
		BigDecimal valorDesconto = pagamento.getValorDesconto();
		
		BigDecimal valorPagamentoCobranca = pagamento.getValorPagamento().subtract(valorJuros).subtract(valorMulta).add(valorDesconto);
		
		BigDecimal saldoDivida = BigDecimal.ZERO;
		BigDecimal valorPagar = BigDecimal.ZERO;
		
		
		List<Cobranca> cobrancasOrdenadas = this.cobrancaRepository.obterCobrancasOrdenadasPorVencimento(idCobrancas);
		
		Cobranca cobrancaParcial = null;
		for (Cobranca itemCobranca:cobrancasOrdenadas){
			
			saldoDivida = this.obterSaldoDivida(itemCobranca.getId());
			valorPagar = itemCobranca.getValor().subtract(saldoDivida);
			
			valorPagamentoCobranca = valorPagamentoCobranca.subtract(valorPagar);
			
			if (valorPagamentoCobranca.floatValue() >=0 ){
		    	itemCobranca.setDataPagamento(pagamento.getDataPagamento());
		    	itemCobranca.setStatusCobranca(StatusCobranca.PAGO);
		    	itemCobranca.getDivida().setStatus(StatusDivida.QUITADA);
		    	itemCobranca.setBanco(pagamento.getBanco());
		    	this.cobrancaRepository.merge(itemCobranca);
		    	this.lancamentoBaixaParcial(itemCobranca,pagamento,valorPagar,StatusBaixa.PAGO,  statusAprovacao);
		    }
		    else{
		    	
		    	valorPagamentoCobranca = valorPagamentoCobranca.add(valorPagar);
		    	
		    	cobrancaParcial = itemCobranca;
		    	break;
		    }
		}
		
		valorPagamentoCobranca = valorPagamentoCobranca.add(valorJuros).add(valorMulta).subtract(valorDesconto);
		
		if ((valorPagamentoCobranca!=null)&&(valorPagamentoCobranca.floatValue()>0)&&(cobrancaParcial!=null)){
	        this.lancamentoBaixaParcial(cobrancaParcial,pagamento,valorPagamentoCobranca,StatusBaixa.PAGAMENTO_PARCIAL,  statusAprovacao);
		}
	}
	
	private void lancamentoBaixaParcial(Cobranca cobrancaParcial,PagamentoDividasDTO pagamento,BigDecimal valor, StatusBaixa status, StatusAprovacao statusAprovacao){

		//BAIXA COBRANCA
		BaixaManual baixaManual = new BaixaManual();
		
		baixaManual.setDataBaixa(pagamento.getDataPagamento());
		baixaManual.setValorPago(valor);
		baixaManual.setCobranca(cobrancaParcial);
		baixaManual.setResponsavel(pagamento.getUsuario());
		baixaManual.setValorJuros(pagamento.getValorJuros());
		baixaManual.setValorMulta(pagamento.getValorMulta());
		baixaManual.setValorDesconto(pagamento.getValorDesconto());
		baixaManual.setStatus(status);
		baixaManual.setStatusAprovacao(statusAprovacao);
		baixaManual.setObservacao(pagamento.getObservacoes());
		baixaManual.setBanco(pagamento.getBanco());
		
		baixaCobrancaRepository.adicionar(baixaManual);

		//MOVIMENTO FINANCEIRO
		if (status == StatusBaixa.PAGAMENTO_PARCIAL){

			gerarMovimentoFinanceiroCota(
				baixaManual, cobrancaParcial.getCota(), pagamento.getUsuario(), valor, 
				cobrancaParcial.getDataVencimento(), pagamento.getDataPagamento(), 
				pagamento.getObservacoes(), GrupoMovimentoFinaceiro.CREDITO
			);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void reverterBaixaManualDividas(List<Long> idCobrancas) {

		List<Cobranca> cobrancasOrdenadas = this.cobrancaRepository.obterCobrancasOrdenadasPorVencimento(idCobrancas);

		for (Cobranca itemCobranca : cobrancasOrdenadas) {

	    	itemCobranca.setStatusCobranca(StatusCobranca.NAO_PAGO);
	    	itemCobranca.getDivida().setStatus(StatusDivida.EM_ABERTO);
	    	itemCobranca.setDataPagamento(null);

	    	this.cobrancaRepository.merge(itemCobranca);

	    	BaixaCobranca baixaCobranca = this.baixaCobrancaRepository.obterUltimaBaixaCobranca(itemCobranca.getId());

	    	this.processarReversaoUltimaBaixaCobranca(baixaCobranca);
		}
	}
	
	private void processarReversaoUltimaBaixaCobranca(BaixaCobranca baixaCobranca) {
		
		if (baixaCobranca.getStatus() == StatusBaixa.PAGAMENTO_PARCIAL) {

			processarReversaoMovimentosFinanceirosBaixaCobranca(baixaCobranca.getMovimentosFinanceiros());
		}

		baixaCobrancaRepository.remover(baixaCobranca);
	}
	
	private void processarReversaoMovimentosFinanceirosBaixaCobranca(List<AbstractMovimentoFinanceiro> movimentosFinanceiros) {

		for (AbstractMovimentoFinanceiro movimento : movimentosFinanceiros) {
			
			if (!(movimento instanceof MovimentoFinanceiroCota)) {
				
				continue;
			}

			MovimentoFinanceiroCota movimentoFinanceiroCota = (MovimentoFinanceiroCota) movimento;

			gerarMovimentoFinanceiroCota(
				movimentoFinanceiroCota.getBaixaCobranca(), movimentoFinanceiroCota.getCota(), 
				movimentoFinanceiroCota.getUsuario(), movimentoFinanceiroCota.getValor(), 
				movimentoFinanceiroCota.getData(), null, 
				movimentoFinanceiroCota.getObservacao(), GrupoMovimentoFinaceiro.DEBITO
			);
		}
	}

	private void gerarMovimentoFinanceiroCota(BaixaCobranca baixaCobranca, Cota cota, Usuario usuario,
											  BigDecimal valor, Date dataVencimento, Date dataPagamento,
											  String observacoes, GrupoMovimentoFinaceiro grupoMovimentoFinaceiro) {

		TipoMovimentoFinanceiro tipoMovimento = 
				this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(grupoMovimentoFinaceiro);
		
		MovimentoFinanceiroCotaDTO movimento = new MovimentoFinanceiroCotaDTO();
		movimento.setCota(cota);
		movimento.setTipoMovimentoFinanceiro(tipoMovimento);
		movimento.setUsuario(usuario);
		movimento.setDataOperacao(dataPagamento);
		movimento.setBaixaCobranca(baixaCobranca);
        movimento.setValor(valor);
        movimento.setDataCriacao(Calendar.getInstance().getTime());
		movimento.setTipoEdicao(TipoEdicao.INCLUSAO);
		movimento.setDataVencimento(dataVencimento);
		movimento.setObservacao(observacoes);
		
		this.movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(movimento);
	}
	
	/**
	 *Método responsável por validar baixa de dividas, verificando se existem boletos envolvidos 
	 * @param idCobrancas
	 */
	@Override
	@Transactional
	public boolean validaBaixaManualDividas(List<Long> idCobrancas) {
		boolean res=true;
		for (Long id:idCobrancas){
			Cobranca cobranca = this.cobrancaRepository.buscarPorId(id);
			if (cobranca.getTipoCobranca()==TipoCobranca.BOLETO){
				res=false;
				break;
			}
		}
		return res;
	}
	
	
	/**
	 *Método responsável por validar negociação, verificando se as datas de vencimento das dividas estão de acordo com a configuração do Distribuidor
	 * @param idCobrancas
	 */
	@Override
	@Transactional
	public boolean validaNegociacaoDividas(List<Long> idCobrancas) {
		
		boolean res=true;
		Distribuidor distribuidor = distribuidorService.obter();
		Integer diasNegociacao = (distribuidor.getParametroCobrancaDistribuidor()!=null?distribuidor.getParametroCobrancaDistribuidor().getDiasNegociacao():null);
		
		if (diasNegociacao!=null){
			
			for (Long id:idCobrancas){
				Cobranca cobranca = this.cobrancaRepository.buscarPorId(id);
				
				if (  distribuidor.getDataOperacao().getTime() >  DateUtil.adicionarDias(cobranca.getDataVencimento(), diasNegociacao).getTime()){
					res=false;
					break;
				}

			}
	    }
		return res;
	}
	
}
