package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.StatusDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaInadimplenteDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.financeiro.BaixaCobranca;
import br.com.abril.nds.model.financeiro.BaixaManual;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.financeiro.StatusBaixa;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.repository.BaixaCobrancaRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.DividaService;

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

	@Override
	@Transactional
	public void postergarCobrancaCota(List<Long> listaIdsCobranca, Date dataPostergacao, BigDecimal juros, BigDecimal multa) {
		
		try {
			
			List<Cobranca> listaCobranca = 
				this.cobrancaRepository.obterCobrancasPorIDS(listaIdsCobranca);
	
			Date dataAtual = Calendar.getInstance().getTime();
			
			MovimentoFinanceiroCota movimentoFinanceiroCota = null;
			
			for (Cobranca cobranca : listaCobranca) {
				
				cobranca.setDataPagamento(dataAtual);
				cobranca.getDivida().setStatus(StatusDivida.POSTERGADA);
	
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
				
				movimentoFinanceiroCota = new MovimentoFinanceiroCota();
	
				movimentoFinanceiroCota.setAprovadoAutomaticamente(false);
				movimentoFinanceiroCota.setBaixaCobranca(cobrancaAtualizada.getBaixaCobranca());
				movimentoFinanceiroCota.setCota(cobrancaAtualizada.getCota());
				movimentoFinanceiroCota.setDataCriacao(dataAtual);
				movimentoFinanceiroCota.setData(dataPostergacao);
				movimentoFinanceiroCota.setLancamentoManual(true);
				movimentoFinanceiroCota.setMotivo("NAO_PAGO_POSTERGADO");
				movimentoFinanceiroCota.setStatus(StatusAprovacao.PENDENTE);
				movimentoFinanceiroCota.setValor(cobrancaAtualizada.getValor());
				
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
				
				movimentoFinanceiroCota.setTipoMovimento(movimentoFinanceiro);
				
				// MOCK USUARIO
				movimentoFinanceiroCota.setUsuario(this.usuarioRepository.buscarTodos().get(0));
				
				movimentoFinanceiroCota.setDataAprovacao(dataAtual);
				
				movimentoFinanceiroCota = 
					this.movimentoFinanceiroCotaRepository.merge(movimentoFinanceiroCota);
				
				if (juros != null && BigDecimal.ZERO.compareTo(juros) > 0) {
	
					movimentoFinanceiro =
							this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
								GrupoMovimentoFinaceiro.JUROS);
	
					movimentoFinanceiroCota.setTipoMovimento(movimentoFinanceiro);
				
					movimentoFinanceiroCota = 
						this.movimentoFinanceiroCotaRepository.merge(movimentoFinanceiroCota);
				}
	
				if (multa != null && BigDecimal.ZERO.compareTo(multa) > 0) {
					
					movimentoFinanceiro =
							this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
								GrupoMovimentoFinaceiro.MULTA);
	
					movimentoFinanceiroCota.setTipoMovimento(movimentoFinanceiro);
					
					movimentoFinanceiroCota = 
						this.movimentoFinanceiroCotaRepository.merge(movimentoFinanceiroCota);
					
				}
			}
			
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

}
