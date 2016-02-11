package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.DividaComissaoDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.StatusDividaDTO;
import br.com.abril.nds.dto.fechamentodiario.SumarizacaoDividasDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaInadimplenteDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.Negociacao;
import br.com.abril.nds.model.financeiro.ParcelaNegociacao;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BaixaCobrancaRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.NegociacaoDividaRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.DividaService;
import br.com.abril.nds.service.FormaCobrancaService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO;

@Service
public class DividaServiceImpl implements DividaService {

	@Autowired
	private DividaRepository dividaRepository;
	
	@Autowired
	private CobrancaRepository cobrancaRepository;

	protected MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	@Autowired
	private BaixaCobrancaRepository baixaCobrancaRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private NegociacaoDividaRepository negociacaoRepository;
		
	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
	
	@Autowired
	private CobrancaService cobrancaService;
	
	@Autowired
	private DistribuidorService distribuidorService;

	@Autowired
	private FormaCobrancaService formaCobrancaService;

	private long calculaDiasEmAtraso(StatusDividaDTO item, Date dataOperacao){
		
		long qntDias = 0L;
		
		if (StatusDivida.QUITADA.getDescricao().equals(item.getSituacao())){
			
			qntDias = DateUtil.obterDiferencaDias(DateUtil.parseDataPTBR(item.getDataVencimento()), 
												  DateUtil.parseDataPTBR(item.getDataPagamento()));
		}
		else{
			
			qntDias = DateUtil.obterDiferencaDias(DateUtil.parseDataPTBR(item.getDataVencimento()), dataOperacao);
		}
			
		return ( (qntDias <= 0) ? 0L : qntDias);
	}

	@Override
	@Transactional
	public List<StatusDividaDTO> obterInadimplenciasCota(
			FiltroCotaInadimplenteDTO filtro) {
		
		List<StatusDividaDTO> dividas = dividaRepository.obterInadimplenciasCota(filtro);

		if(!dividas.isEmpty()){
		
			Date dataOperacao = filtro.getDataOperacaoDistribuidor();
			
			if(dataOperacao == null){
				
				dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
			}
			
			for(StatusDividaDTO item : dividas){
					
				item.setDiasAtraso(this.calculaDiasEmAtraso(item, dataOperacao));
			}
		}
		
		return dividas;
	}

	@Override
	@Transactional
	public Long obterTotalInadimplenciasCota(FiltroCotaInadimplenteDTO filtro) {
		return dividaRepository.obterTotalInadimplenciasCota(filtro);
	}

	@Override
	@Transactional
	public Long obterTotalCotasInadimplencias(FiltroCotaInadimplenteDTO filtro) {
		return dividaRepository.obterTotalCotasInadimplencias(filtro);
	}

	@Override
	@Transactional
	public Double obterSomaDividas(FiltroCotaInadimplenteDTO filtro) {
		return dividaRepository.obterSomaDividas(filtro);
	}

	@Override
	@Transactional
	public List<Divida> obterDividasAcumulo(Long idDivida) {
		
		List<Divida> dividas = new ArrayList<Divida>();
		
		Divida dividaAtual = this.dividaRepository.buscarPorId(idDivida);
		
		if (dividaAtual == null){
			
			return null;
		}
		    
		dividas.add(dividaAtual);
			
		this.adicionarDividasRaiz(dividas, dividaAtual);
		
		return dividas;
	}
	
	@Override
	@Transactional
	public List<MovimentoFinanceiroCota> obterDividasNegociacao(Long idDivida) {
		
		List<MovimentoFinanceiroCota> movimentosEstoqueCota = new ArrayList<>();
		
		Divida dividaAtual = this.dividaRepository.buscarPorId(idDivida);
		
		if (dividaAtual == null){
			
			return null;
		}
		
		Cobranca cobranca = dividaAtual.getCobranca();
		
		if (cobranca == null){
			
			return null;
		}
		
		List<Negociacao> listaNegociacao = cobranca.getNegociacao();
		
		if (listaNegociacao != null && !listaNegociacao.isEmpty()) {
		
			Negociacao negociacao = listaNegociacao.get(0);
			
			List<ParcelaNegociacao> parcelas = negociacao.getParcelas();
			
			if (parcelas != null) {
				
				for (ParcelaNegociacao parcelaNegociacao : negociacao.getParcelas()) {
					
					MovimentoFinanceiroCota mec = parcelaNegociacao.getMovimentoFinanceiroCota();
					
					movimentosEstoqueCota.add(mec);
				}
			}
		}
		
		return movimentosEstoqueCota;
	}

	private void adicionarDividasRaiz(List<Divida> dividas, Divida divida) {
		
		Divida dividaRaiz = divida.getDividaRaiz();
		
		if (dividaRaiz != null) {
			
			// dividas.add(dividaRaiz);
			
			// this.adicionarDividasRaiz(dividas, dividaRaiz);
		}
	}

	@Override
	@Transactional
	public Divida obterDividaPorId(Long idDivida) {
		return dividaRepository.buscarPorId(idDivida);
	}
	
	/**
	 * Verifica se a data informada para postergação é maior que a data de operação corrente do sistema.
	 * 
	 * @param dataPostergação - data de postergação
	 */
	private void validarDataPostergacao(Date dataPostergacao){
		
		if(dataPostergacao.compareTo(this.distribuidorService.obterDataOperacaoDistribuidor()) < 1){
			
			throw new ValidacaoException(TipoMensagem.WARNING,
					"A nova data para postergação da dívida tem que ser no mínimo a data da operação seguinte (D+1)");
		}
	}

	@Override
	@Transactional
	public void postergarCobrancaCota(List<Long> listaIdsCobranca, Date dataPostergacao, Long idUsuario, boolean isIsento) {
		
		this.validarDataPostergacao(dataPostergacao);
		
		List<Cobranca> listaCobranca = 
			this.cobrancaRepository.obterCobrancasPorIDS(listaIdsCobranca);

		Date dataAtual = Calendar.getInstance().getTime();

		Usuario currentUser = this.usuarioRepository.buscarPorId(idUsuario);
		
		TipoMovimentoFinanceiro postergadoNegociacao =
				this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
					GrupoMovimentoFinaceiro.POSTERGADO_NEGOCIACAO);
			
		if (postergadoNegociacao == null) {
			
			throw new ValidacaoException(
					TipoMensagem.ERROR, 
					"Tipo de Movimento " + GrupoMovimentoFinaceiro.POSTERGADO_NEGOCIACAO + " não encontrado.");
		}
		
		TipoMovimentoFinanceiro tipoMovimentoJuros =
				this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
					GrupoMovimentoFinaceiro.JUROS);
		
		TipoMovimentoFinanceiro tipoMovimentoMulta =
				this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
					GrupoMovimentoFinaceiro.MULTA);
		
		FormaCobranca formaCobrancaPrincipal = 
			this.formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();
		
		for (Cobranca cobranca : listaCobranca) {
			
			Date backupDataVencimento = cobranca.getDataVencimento();
			
			cobranca.getDivida().setStatus(StatusDivida.POSTERGADA);
			
			Cobranca cobrancaAtualizada = this.cobrancaRepository.merge(cobranca);
			
			MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO = new MovimentoFinanceiroCotaDTO();
			movimentoFinanceiroCotaDTO.setAprovacaoAutomatica(false);
			movimentoFinanceiroCotaDTO.setCota(cobrancaAtualizada.getCota());
			movimentoFinanceiroCotaDTO.setDataCriacao(dataAtual);
			movimentoFinanceiroCotaDTO.setDataVencimento(dataPostergacao);
			movimentoFinanceiroCotaDTO.setValor(cobrancaAtualizada.getValor());
			movimentoFinanceiroCotaDTO.setUsuario(currentUser);
			
			movimentoFinanceiroCotaDTO.setTipoEdicao(TipoEdicao.INCLUSAO);
			movimentoFinanceiroCotaDTO.setTipoMovimentoFinanceiro(postergadoNegociacao);
			movimentoFinanceiroCotaDTO.setLancamentoManual(true);
			movimentoFinanceiroCotaDTO.setObservacao("Dívda postergada na Baixa Financeira");
			
			
			Fornecedor fornecedor = cobranca.getFornecedor() != null ?
			        cobranca.getFornecedor() : cobranca.getCota().getParametroCobranca() != null ?
			                cobranca.getCota().getParametroCobranca().getFornecedorPadrao() : null;
			
			if (fornecedor == null){
				
				throw new ValidacaoException(
				        TipoMensagem.WARNING, 
				        "A [Cota "+cobranca.getCota().getNumeroCota()+"] necessita de um [Fornecedor Padrão] em [Parâmetros] Financeiros !");
			}
			
			movimentoFinanceiroCotaDTO.setFornecedor(fornecedor);
			
			
			this.movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(movimentoFinanceiroCotaDTO);
				
			if (!isIsento) {
			
				BigDecimal juros = this.cobrancaService.calcularJuros(
					cobrancaAtualizada.getBanco(), cobrancaAtualizada.getCota().getId(), 
					cobrancaAtualizada.getValor(), backupDataVencimento, 
					dataPostergacao, formaCobrancaPrincipal);
					
				movimentoFinanceiroCotaDTO.setValor(juros);
				movimentoFinanceiroCotaDTO.setTipoMovimentoFinanceiro(tipoMovimentoJuros);
				movimentoFinanceiroCotaDTO.setObservacao("Juros de dívda postergada na Baixa Financeira");
				
				this.movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(movimentoFinanceiroCotaDTO);
				
				BigDecimal multa = this.cobrancaService.calcularMulta(
					cobrancaAtualizada.getBanco(), cobrancaAtualizada.getCota(),
					cobrancaAtualizada.getValor(),
					formaCobrancaPrincipal);
				
				movimentoFinanceiroCotaDTO.setValor(multa);
				movimentoFinanceiroCotaDTO.setTipoMovimentoFinanceiro(tipoMovimentoMulta);
				movimentoFinanceiroCotaDTO.setObservacao("Multa de dívda postergada na Baixa Financeira");
				
				this.movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(movimentoFinanceiroCotaDTO);
				
			}
		}
	}

	@Override
	@Transactional(readOnly=true)
	public BigDecimal calcularEncargosPostergacao(List<Long> listaIdsCobranca, Date dataPostergacao) {
		
		List<Cobranca> listaCobrancas = 
			this.cobrancaRepository.obterCobrancasPorIDS(listaIdsCobranca);
		
		for(Cobranca cobranca : listaCobrancas) {
		
				if (!cobranca.getDataVencimento().before(dataPostergacao)) {

					throw new ValidacaoException(
					TipoMensagem.WARNING, "A data da postergação tem de ser maior que a data de vencimento.");
				}
		}
		
		BigDecimal encargos = BigDecimal.ZERO;
		
		FormaCobranca formaCobrancaPrincipal = 
			this.formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();
		
		for (Cobranca cobranca : listaCobrancas) {
			
			BigDecimal juros = 
				this.cobrancaService.calcularJuros(
					cobranca.getBanco(), cobranca.getCota().getId(), 
					cobranca.getValor(), cobranca.getDataVencimento(), 
					dataPostergacao, formaCobrancaPrincipal);
			
			BigDecimal multa = 
				this.cobrancaService.calcularMulta(
					cobranca.getBanco(), cobranca.getCota(), 
					cobranca.getValor(), formaCobrancaPrincipal);
			
			encargos = encargos.add(juros).add(multa);
		}
		
		return encargos;
	}

	@Override
	@Transactional(readOnly=true)
	public BigDecimal obterTotalDividasAbertoCota(Long idCota) {
		return this.dividaRepository.obterTotalDividasAbertoCota(idCota);
	}

	/**
	 * Verifica se cota possui dividas vencidas em aberto
	 * 
	 * @param idCota
	 * @return boolean
	 */
	@Override
	@Transactional(readOnly=true)
	public boolean cotaPossuiDividasVencidasEmAberto(Long idCota) {
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		BigDecimal valorPendencias = this.dividaRepository.obterTotalDividasVencidasCota(idCota, dataOperacao);
		
		return (valorPendencias!=null && valorPendencias.compareTo(BigDecimal.ZERO) > 0);
	}
	
	/**
	 * Obtem valor total de dividas vencidas em aberto da cota
	 * 
	 * @param idCota
	 * @return boolean
	 */
	@Override
	@Transactional(readOnly=true)
	public BigDecimal obterTotalDividasVencidasEmAberto(Long idCota) {
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		BigDecimal valorPendencias = this.dividaRepository.obterTotalDividasVencidasCota(idCota, dataOperacao);
		
		return valorPendencias;
	}
	
	@Override
	@Transactional(readOnly=true)
	public DividaComissaoDTO obterDadosDividaComissao(Long idDivida) {

		Negociacao negociacao = negociacaoRepository.obterNegociacaoPorDivida(idDivida);

		if(negociacao == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não há negociação associada a essa dívida");
		}
		
		BigDecimal valorOriginal = negociacao.getValorOriginal();
		
		if(valorOriginal == null) {
			
			valorOriginal = BigDecimal.ZERO;
		}
		
		BigDecimal valorPago = valorOriginal.subtract(negociacao.getValorDividaPagaComissao());
		
		valorPago = valorPago == null ? BigDecimal.ZERO : valorPago;
		
		BigDecimal valorResidual = valorOriginal.subtract(valorPago);
		
		DividaComissaoDTO resultado = new DividaComissaoDTO();
		resultado.setPorcentagem(CurrencyUtil.formatarValor(negociacao.getComissaoParaSaldoDivida()));
		resultado.setValorPago(CurrencyUtil.formatarValor(valorPago));
		resultado.setValorDivida(CurrencyUtil.formatarValor(valorOriginal));
		resultado.setValorResidual(CurrencyUtil.formatarValor(valorResidual));
		
		return resultado;
	}

    /**
     * {@inheritDoc}
     */
	@Override
    @Transactional(readOnly = true)
    public List<SumarizacaoDividasDTO> sumarizacaoDividasReceberEm(Date data) {
	    
		return  dividaRepository.sumarizacaoDividasReceberEm(data);
    }


    /**
     * {@inheritDoc}
     */
	@Override
    @Transactional(readOnly = true)
    public List<SumarizacaoDividasDTO> sumarizacaoDividasVencerApos(Date data) {
        
		return dividaRepository.sumarizacaoDividasVencerApos(data);
    }

    /**
     * {@inheritDoc}
     */
	@Override
    @Transactional(readOnly = true)
    public List<Cobranca> obterDividasReceberEm(Date data, PaginacaoVO paginacao) {
	    return dividaRepository.obterDividasReceberEm(data, paginacao);
    }

    /**
     * {@inheritDoc}
     */
	@Override
    @Transactional(readOnly = true)
    public List<Cobranca> obterDividasVencerApos(Date data, PaginacaoVO paginacao) {
	    return dividaRepository.obterDividasVencerApos(data, paginacao);
    }

	 /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public long contarDividasReceberEm(Date data) {
        return dividaRepository.contarDividasReceberEm(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public long contarDividasVencerApos(Date data) {
        return dividaRepository.contarDividasVencerApos(data);
    }
}
