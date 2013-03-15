package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.DividaComissaoDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.StatusDividaDTO;
import br.com.abril.nds.dto.fechamentodiario.SumarizacaoDividasDTO;
import br.com.abril.nds.dto.fechamentodiario.TipoDivida;
import br.com.abril.nds.dto.filtro.FiltroCotaInadimplenteDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.BaixaCobranca;
import br.com.abril.nds.model.financeiro.BaixaManual;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.Negociacao;
import br.com.abril.nds.model.financeiro.StatusBaixa;
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
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.integracao.DistribuidorService;
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
	
	@Override
	@Transactional
	public List<StatusDividaDTO> obterInadimplenciasCota(
			FiltroCotaInadimplenteDTO filtro) {
		return dividaRepository.obterInadimplenciasCota(filtro);
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
	public List<Divida> getDividasAcumulo(Long idDivida) {
		
		List<Divida> dividas = new ArrayList<Divida>(dividaRepository.buscarPorId(idDivida).getAcumulado());
		
		for(Divida divida:dividas) {
			divida.getCobranca();
		}
		return dividas; 
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
		
		for (Cobranca cobranca : listaCobranca) {
			
			Date backupDataVencimento = cobranca.getDataVencimento();
			
			cobranca.getDivida().setStatus(StatusDivida.POSTERGADA);
			cobranca.setDataPagamento(dataAtual);
			cobranca.setDataVencimento(dataPostergacao);

			BaixaCobranca baixaCobranca = new BaixaManual();
			baixaCobranca.setStatus(StatusBaixa.NAO_PAGO_POSTERGADO);
			baixaCobranca.setDataBaixa(dataAtual);
			baixaCobranca.setDataPagamento(dataAtual);
			baixaCobranca.setValorPago(cobranca.getValor());
			baixaCobranca.setCobranca(cobranca);
			
			baixaCobranca = this.baixaCobrancaRepository.merge(baixaCobranca);
			
			Cobranca cobrancaAtualizada = this.cobrancaRepository.merge(cobranca);
			
			MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO = new MovimentoFinanceiroCotaDTO();
			movimentoFinanceiroCotaDTO.setAprovacaoAutomatica(false);
			movimentoFinanceiroCotaDTO.setCota(cobrancaAtualizada.getCota());
			movimentoFinanceiroCotaDTO.setBaixaCobranca(baixaCobranca);
			movimentoFinanceiroCotaDTO.setDataCriacao(dataAtual);
			movimentoFinanceiroCotaDTO.setDataVencimento(dataPostergacao);
			movimentoFinanceiroCotaDTO.setValor(cobrancaAtualizada.getValor());
			movimentoFinanceiroCotaDTO.setUsuario(currentUser);
			
			movimentoFinanceiroCotaDTO.setTipoEdicao(TipoEdicao.INCLUSAO);
			movimentoFinanceiroCotaDTO.setTipoMovimentoFinanceiro(postergadoNegociacao);
			movimentoFinanceiroCotaDTO.setLancamentoManual(true);
			
			
			Fornecedor fornecedor = cobranca.getFornecedor()!=null?cobranca.getFornecedor():cobranca.getCota().getParametroCobranca()!=null?cobranca.getCota().getParametroCobranca().getFornecedorPadrao():null;
			
			if (fornecedor == null){
				
				throw new ValidacaoException(TipoMensagem.WARNING, "A [Cota "+cobranca.getCota().getNumeroCota()+"] necessita de um [Fornecedor Padrão] em [Parâmetros] Financeiros !");
			}
			
			movimentoFinanceiroCotaDTO.setFornecedor(fornecedor);
			
			
			this.movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(movimentoFinanceiroCotaDTO);
				
			if (!isIsento) {
			
				BigDecimal juros = this.cobrancaService.calcularJuros(
					cobrancaAtualizada.getBanco(), cobrancaAtualizada.getCota(), 
					cobrancaAtualizada.getValor(), backupDataVencimento, dataPostergacao);
					
				movimentoFinanceiroCotaDTO.setValor(juros);
				movimentoFinanceiroCotaDTO.setTipoMovimentoFinanceiro(tipoMovimentoJuros);
				
				this.movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(movimentoFinanceiroCotaDTO);
				
				BigDecimal multa = this.cobrancaService.calcularMulta(
					cobrancaAtualizada.getBanco(), cobrancaAtualizada.getCota(),
					cobrancaAtualizada.getValor());
				
				movimentoFinanceiroCotaDTO.setValor(multa);
				movimentoFinanceiroCotaDTO.setTipoMovimentoFinanceiro(tipoMovimentoMulta);
				
				movimentoFinanceiroCotaDTO.setFornecedor(cobrancaAtualizada.getCota().getParametroCobranca()!=null?cobrancaAtualizada.getCota().getParametroCobranca().getFornecedorPadrao():null);

				this.movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(movimentoFinanceiroCotaDTO);
				
			}
		}
	}

	@Override
	@Transactional(readOnly=true)
	public BigDecimal calcularEncargosPostergacao(List<Long> listaIdsCobranca, Date dataPostergacao) {
		
		List<Cobranca> listaCobrancas = 
			this.cobrancaRepository.obterCobrancasPorIDS(listaIdsCobranca);
		
		BigDecimal encargos = BigDecimal.ZERO;
		
		for (Cobranca cobranca : listaCobrancas) {
			
			BigDecimal juros = 
				this.cobrancaService.calcularJuros(
					cobranca.getBanco(), cobranca.getCota(), 
					cobranca.getValor(), cobranca.getDataVencimento(), dataPostergacao);
			
			BigDecimal multa = 
				this.cobrancaService.calcularMulta(
					cobranca.getBanco(), cobranca.getCota(), 
					cobranca.getValor());
			
			encargos = encargos.add(juros).add(multa);
		}
		
		return encargos;
	}

	@Override
	@Transactional(readOnly=true)
	public BigDecimal obterTotalDividasAbertoCota(Long idCota) {
		return this.dividaRepository.obterTotalDividasAbertoCota(idCota);
	}

	@Override
	@Transactional(readOnly=true)
	public DividaComissaoDTO obterDadosDividaComissao(Long idDivida) {
		Divida divida = dividaRepository.buscarPorId(idDivida);
		Cobranca cobranca = divida.getCobranca();

		Negociacao negociacao = negociacaoRepository.obterNegociacaoPorCobranca(cobranca.getId());
		
		if(negociacao == null)
			throw new ValidacaoException(TipoMensagem.WARNING, "Não há negociação associada a essa dívida");
		
		BigDecimal valorPago = negociacao.getValorDividaPagaComissao();
		if(valorPago == null) {
			valorPago = BigDecimal.ZERO;
		}
		BigDecimal valorOriginal = BigDecimal.ZERO;
		for(Cobranca c : negociacao.getCobrancasOriginarias()) {
			valorOriginal = valorOriginal.add(c.getValor());
		}
		
		
		DividaComissaoDTO resultado = new DividaComissaoDTO();
		resultado.setPorcentagem(negociacao.getComissaoParaSaldoDivida());
		resultado.setValorPago(valorPago);
		resultado.setValorDivida(valorOriginal);
		resultado.setValorResidual(valorOriginal.add(valorPago.multiply(new BigDecimal(-1))));
		
		return resultado;
	}

    /**
     * {@inheritDoc}
     */
	@Override
    @Transactional(readOnly = true)
    public List<SumarizacaoDividasDTO> sumarizacaoDividasReceberEm(Date data) {
        Map<TipoCobranca, SumarizacaoDividasDTO> mapaSumarizacao = criarMapaTiposCobrancaDistribuidor(
                data, TipoDivida.DIVIDA_A_RECEBER, dividaRepository.sumarizacaoDividasReceberEm(data));
	    return new ArrayList<SumarizacaoDividasDTO>(mapaSumarizacao.values());
    }


    /**
     * {@inheritDoc}
     */
	@Override
    @Transactional(readOnly = true)
    public List<SumarizacaoDividasDTO> sumarizacaoDividasVencerApos(Date data) {
        Map<TipoCobranca, SumarizacaoDividasDTO> mapaSumarizacao = criarMapaTiposCobrancaDistribuidor(data, TipoDivida.DIVIDA_A_VENCER,
                dividaRepository.sumarizacaoDividasVencerApos(data));
        return new ArrayList<SumarizacaoDividasDTO>(mapaSumarizacao.values());
    }

    /**
     * {@inheritDoc}
     */
	@Override
    @Transactional(readOnly = true)
    public List<Divida> obterDividasReceberEm(Date data, PaginacaoVO paginacao) {
	    return dividaRepository.obterDividasReceberEm(data, paginacao);
    }

    /**
     * {@inheritDoc}
     */
	@Override
    @Transactional(readOnly = true)
    public List<Divida> obterDividasVencerApos(Date data, PaginacaoVO paginacao) {
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

    /**
     * Cria um mapa de sumarização de dívidas com os tipos de cobranças
     * parametrizadas pelo distribuidor x sumarização calculada sobre as dívidas
     * existentes
     * 
     * @param data
     *            data base para sumarização
     * @param tipoDivida
     *            tipo da sumarização das dívidas, dividas à receber na data base ou dívidas
     *            à vencer após a data base
     * @param sumarizacao mapa com as sumarizações calculadas sobre dívidas existentes
     * @return mapa com os tipos de cobranças e sumarização de dívidas existentes 
     *         
     */
    private Map<TipoCobranca, SumarizacaoDividasDTO> criarMapaTiposCobrancaDistribuidor(Date data, TipoDivida tipoDivida,
            Map<TipoCobranca, SumarizacaoDividasDTO> sumarizacao) {
        Map<TipoCobranca, SumarizacaoDividasDTO> novaSumarizacao = new EnumMap<TipoCobranca, SumarizacaoDividasDTO>(sumarizacao);
        Set<PoliticaCobranca> pcs = this.distribuidorService.politicasCobranca();
        for (PoliticaCobranca pc : pcs) {
            FormaCobranca formaCobranca = pc.getFormaCobranca();
            if (formaCobranca.isAtiva()) {
                TipoCobranca tipoCobranca = formaCobranca.getTipoCobranca();
                if (!novaSumarizacao.containsKey(tipoCobranca)) {
                    novaSumarizacao.put(tipoCobranca, new SumarizacaoDividasDTO(data, tipoDivida, tipoCobranca));
                }
            }
        }
        return novaSumarizacao;
    }
}
