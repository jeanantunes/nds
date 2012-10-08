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
import br.com.abril.nds.dto.filtro.FiltroCotaInadimplenteDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.financeiro.BaixaCobranca;
import br.com.abril.nds.model.financeiro.BaixaManual;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.Negociacao;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
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
import br.com.abril.nds.util.TipoMensagem;

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
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		if(dataPostergacao.compareTo(distribuidor.getDataOperacao()) < 1){
			throw new ValidacaoException(TipoMensagem.WARNING,"A nova data para postergação da dívida tem que ser no mínimo a data da operação seguinte (D+1)");
		}
	}

	@Override
	@Transactional
	public void postergarCobrancaCota(List<Long> listaIdsCobranca, Date dataPostergacao, Long idUsuario, boolean isIsento) {
		
		validarDataPostergacao(dataPostergacao);
		
		try {
			
			List<Cobranca> listaCobranca = 
				this.cobrancaRepository.obterCobrancasPorIDS(listaIdsCobranca);
	
			Date dataAtual = Calendar.getInstance().getTime();

			Usuario currentUser = this.usuarioRepository.buscarPorId(idUsuario);
			
			for (Cobranca cobranca : listaCobranca) {
				
				Date backupDataVencimento = cobranca.getDataVencimento();
				
				cobranca.getDivida().setStatus(StatusDivida.POSTERGADA);
				cobranca.setDataPagamento(dataAtual);
				cobranca.setDataVencimento(dataPostergacao);
	
				if (cobranca.getBaixaCobranca() == null) {
					
					BaixaCobranca baixaCobranca = new BaixaManual();
					
					baixaCobranca.setStatus(StatusBaixa.NAO_PAGO_POSTERGADO);
					baixaCobranca.setDataBaixa(dataAtual);
					baixaCobranca.setValorPago(cobranca.getValor());
					
					baixaCobranca = this.baixaCobrancaRepository.merge(baixaCobranca);
					
					cobranca.setBaixaCobranca(baixaCobranca);
					
				} else {
				
					cobranca.getBaixaCobranca().setStatus(StatusBaixa.NAO_PAGO_POSTERGADO);
					cobranca.getBaixaCobranca().setDataBaixa(dataAtual);
					cobranca.getBaixaCobranca().setValorPago(cobranca.getValor());
				}
				
				Cobranca cobrancaAtualizada = this.cobrancaRepository.merge(cobranca);
				
				MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO = new MovimentoFinanceiroCotaDTO();
				
				movimentoFinanceiroCotaDTO.setAprovacaoAutomatica(false);
				movimentoFinanceiroCotaDTO.setCota(cobrancaAtualizada.getCota());
				movimentoFinanceiroCotaDTO.setBaixaCobranca(cobrancaAtualizada.getBaixaCobranca());
				movimentoFinanceiroCotaDTO.setDataCriacao(dataAtual);
				movimentoFinanceiroCotaDTO.setDataVencimento(dataPostergacao);
				movimentoFinanceiroCotaDTO.setValor(cobrancaAtualizada.getValor());
				movimentoFinanceiroCotaDTO.setUsuario(currentUser);
				
				movimentoFinanceiroCotaDTO.setTipoEdicao(TipoEdicao.INCLUSAO);

				TipoMovimentoFinanceiro movimentoFinanceiro =
					this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
						GrupoMovimentoFinaceiro.POSTERGADO_NEGOCIACAO);
				
				if (movimentoFinanceiro == null) {
					
					movimentoFinanceiro = new TipoMovimentoFinanceiro();
					
					movimentoFinanceiro.setAprovacaoAutomatica(true);
					movimentoFinanceiro.setDescricao("POSTERGAÇÃO");
					movimentoFinanceiro.setGrupoMovimentoFinaceiro(GrupoMovimentoFinaceiro.POSTERGADO_NEGOCIACAO);
					movimentoFinanceiro.setOperacaoFinaceira(OperacaoFinaceira.CREDITO);
					
					movimentoFinanceiro = 
						this.tipoMovimentoFinanceiroRepository.merge(movimentoFinanceiro);
				}
				
				movimentoFinanceiroCotaDTO.setTipoMovimentoFinanceiro(movimentoFinanceiro);
				movimentoFinanceiroCotaDTO.setLancamentoManual(true);
				
				this.movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(movimentoFinanceiroCotaDTO);
					
				if (!isIsento) {
				
					Distribuidor distribuidor = this.distribuidorService.obter();
					
					BigDecimal juros = this.cobrancaService.calcularJuros(
						cobrancaAtualizada.getBanco(), cobrancaAtualizada.getCota(), distribuidor, 
						cobrancaAtualizada.getValor(), backupDataVencimento, dataPostergacao);
						
					movimentoFinanceiro =
						this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
							GrupoMovimentoFinaceiro.JUROS);

					movimentoFinanceiroCotaDTO.setValor(juros);
					
					movimentoFinanceiroCotaDTO.setTipoMovimentoFinanceiro(movimentoFinanceiro);
					
					this.movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(movimentoFinanceiroCotaDTO);
					
					BigDecimal multa = this.cobrancaService.calcularMulta(
						cobrancaAtualizada.getBanco(), cobrancaAtualizada.getCota(), distribuidor, 
						cobrancaAtualizada.getValor());
					
					movimentoFinanceiro =
						this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
							GrupoMovimentoFinaceiro.MULTA);

					movimentoFinanceiroCotaDTO.setValor(multa);
					
					movimentoFinanceiroCotaDTO.setTipoMovimentoFinanceiro(movimentoFinanceiro);
					
					this.movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(movimentoFinanceiroCotaDTO);
					
				}
			}
			
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional(readOnly=true)
	public BigDecimal calcularEncargosPostergacao(List<Long> listaIdsCobranca, Date dataPostergacao) {
		
		List<Cobranca> listaCobrancas = 
			this.cobrancaRepository.obterCobrancasPorIDS(listaIdsCobranca);
		
		BigDecimal encargos = BigDecimal.ZERO;
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		for (Cobranca cobranca : listaCobrancas) {
			
			BigDecimal juros = 
				this.cobrancaService.calcularJuros(
					cobranca.getBanco(), cobranca.getCota(), distribuidor, 
					cobranca.getValor(), cobranca.getDataVencimento(), dataPostergacao);
			
			BigDecimal multa = 
				this.cobrancaService.calcularMulta(
					cobranca.getBanco(), cobranca.getCota(), 
					distribuidor, cobranca.getValor());
			
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

}
