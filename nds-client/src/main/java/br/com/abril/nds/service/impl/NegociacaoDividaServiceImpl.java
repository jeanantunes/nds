package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.NegociacaoDividaDTO;
import br.com.abril.nds.dto.NegociacaoDividaPaginacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacaoDivida;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.CobrancaCheque;
import br.com.abril.nds.model.financeiro.CobrancaDeposito;
import br.com.abril.nds.model.financeiro.CobrancaDinheiro;
import br.com.abril.nds.model.financeiro.CobrancaTransferenciaBancaria;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.Negociacao;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.NegociacaoDividaRepository;
import br.com.abril.nds.service.NegociacaoDividaService;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class NegociacaoDividaServiceImpl implements NegociacaoDividaService{
	
	@Autowired
	private NegociacaoDividaRepository negociacaoDividaRepository;
	
	@Autowired
	private CobrancaRepository cobrancaRepository;
	
	@Autowired
	private DividaRepository dividaRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	@Autowired
	private ConsolidadoFinanceiroRepository consolidadoFinanceiroRepository;
	
	@Override
	@Transactional(readOnly = true)
	public NegociacaoDividaPaginacaoDTO obterDividasPorCotaPaginado(FiltroConsultaNegociacaoDivida filtro) {
		
		NegociacaoDividaPaginacaoDTO retorno = new NegociacaoDividaPaginacaoDTO();
		
		retorno.setListaNegociacaoDividaDTO(this.negociacaoDividaRepository.obterCotaPorNumero(filtro));
		retorno.setQuantidadeRegistros(this.negociacaoDividaRepository.obterCotaPorNumeroCount(filtro));
		
		return retorno;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<NegociacaoDividaDTO> obterDividasPorCota(FiltroConsultaNegociacaoDivida filtro) {
		
		return this.negociacaoDividaRepository.obterCotaPorNumero(filtro);
	}

	@Override
	@Transactional
	public void criarNegociacao(Long idCota, List<MovimentoFinanceiroCota> parcelas, Long idCobrancaOriginaria, 
			Usuario usuarioResponsavel, boolean negociacaoAvulsa, Integer ativarCotaAposParcela,
			BigDecimal comissaoParaSaldoDivida, boolean isentaEncargos, FormaCobranca formaCobranca) {
		
		List<String> msgs = new ArrayList<String>();
		Date dataAtual = new Date();
		
		this.validarDadosEntrada(msgs, dataAtual, parcelas, usuarioResponsavel, 
				ativarCotaAposParcela, comissaoParaSaldoDivida, formaCobranca);
		
		Cota cota = null;
		if (idCota == null){
			
			msgs.add("Cota da negociação não encontrada.");
		} else {
			
			cota = this.cotaRepository.buscarPorId(idCota);
			
			if (cota == null){
				
				msgs.add("Cota da negociação não encontrada.");
			}
		}
		
		Cobranca cobrancaOriginaria = null;
		if (idCobrancaOriginaria == null){
			
			msgs.add("Cobrança originária não encontrada.");
		} else {
			
			cobrancaOriginaria = this.cobrancaRepository.buscarPorId(idCobrancaOriginaria);
			
			if (cobrancaOriginaria == null){
				
				msgs.add("Cobrança originária não encontrada.");
			}
		}
		
		if (!msgs.isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, msgs);
		}
		
		cobrancaOriginaria.setStatusCobranca(StatusCobranca.PAGO);
		cobrancaOriginaria.getDivida().setStatus(StatusDivida.NEGOCIADA);
		
		BigDecimal totalNegociacao = BigDecimal.ZERO;
		for (MovimentoFinanceiroCota movimentoFinanceiroCota : parcelas){
			
			movimentoFinanceiroCota.setCota(cobrancaOriginaria.getCota());
			movimentoFinanceiroCota.setUsuario(usuarioResponsavel);
			movimentoFinanceiroCota.setData(dataAtual);
			movimentoFinanceiroCota.setDataCriacao(dataAtual);
			movimentoFinanceiroCota.setStatus(StatusAprovacao.APROVADO);
			movimentoFinanceiroCota.setAprovador(usuarioResponsavel);
			
			TipoMovimentoFinanceiro tipoMovimentoFinanceiro = new TipoMovimentoFinanceiro();
			tipoMovimentoFinanceiro.setGrupoMovimentoFinaceiro(GrupoMovimentoFinaceiro.POSTERGADO_NEGOCIACAO);
			tipoMovimentoFinanceiro.setOperacaoFinaceira(OperacaoFinaceira.DEBITO);
			movimentoFinanceiroCota.setTipoMovimento(tipoMovimentoFinanceiro);
			
			totalNegociacao = totalNegociacao.add(movimentoFinanceiroCota.getValor());
			
			this.movimentoFinanceiroCotaRepository.adicionar(movimentoFinanceiroCota);
		}
		
		this.dividaRepository.merge(cobrancaOriginaria.getDivida());
		this.cobrancaRepository.merge(cobrancaOriginaria);
		
		if (negociacaoAvulsa){
			
			ConsolidadoFinanceiroCota consolidado = new ConsolidadoFinanceiroCota();
			consolidado.setCota(cota);
			consolidado.setDataConsolidado(dataAtual);
			consolidado.setMovimentos(parcelas);
			consolidado.setValorPostergado(totalNegociacao);
			
			for (MovimentoFinanceiroCota movimentoFinanceiroCota : parcelas){
				
				Divida divida = new Divida();
				divida.setData(dataAtual);
				divida.setResponsavel(usuarioResponsavel);
				divida.setCota(cota);
				divida.setValor(movimentoFinanceiroCota.getValor());
				divida.setStatus(StatusDivida.EM_ABERTO);
				divida.setConsolidado(consolidado);
				
				Cobranca cobranca = null;
				
				switch(formaCobranca.getTipoCobranca()){
					case BOLETO:
						cobranca = new Boleto();
					break;
					case BOLETO_EM_BRANCO:
						cobranca = new Boleto();
					break;
					case CHEQUE:
						cobranca = new CobrancaCheque();
					break;
					case DEPOSITO:
						cobranca = new CobrancaDeposito();
					break;
					case DINHEIRO:
						cobranca = new CobrancaDinheiro();
					break;
					case OUTROS:
						//TODO vixi...
						cobranca = new CobrancaDinheiro();
					break;
					case TRANSFERENCIA_BANCARIA:
						cobranca = new CobrancaTransferenciaBancaria();
					break;
				}
				
				cobranca.setDivida(divida);
				cobranca.setDataEmissao(dataAtual);
				cobranca.setStatusCobranca(StatusCobranca.NAO_PAGO);
				
				this.dividaRepository.adicionar(divida);
				this.cobrancaRepository.adicionar(cobranca);
			}
			
			this.consolidadoFinanceiroRepository.adicionar(consolidado);
		}
		
		Negociacao negociacao = new Negociacao();
		negociacao.setAtivarCotaAposParcela(ativarCotaAposParcela);
		negociacao.setCobrancaOriginaria(cobrancaOriginaria);
		negociacao.setComissaoParaSaldoDivida(comissaoParaSaldoDivida);
		negociacao.setIsentaEncargos(isentaEncargos);
		negociacao.setNegociacaoAvulsa(negociacaoAvulsa);
		negociacao.setFormaCobranca(formaCobranca);
		negociacao.setParcelas(parcelas);
		
		this.negociacaoDividaRepository.adicionar(negociacao);
	}

	private void validarDadosEntrada(List<String> msgs, Date dataAtual,
			List<MovimentoFinanceiroCota> parcelas, Usuario usuarioResponsavel,
			Integer ativarCotaAposParcela, BigDecimal comissaoParaSaldoDivida,
			FormaCobranca formaCobranca) {
		
		for (MovimentoFinanceiroCota mov : parcelas){
			
			if (mov.getValor() == null || mov.getValor().equals(BigDecimal.ZERO)){
				
				msgs.add("Valor da parcela " + parcelas.indexOf(mov) + 1 + " inválido.");
			}
			
			if (mov.getData() == null || mov.getData().compareTo(dataAtual) < 0){
				
				msgs.add("Data de vencimento da parcela " + parcelas.indexOf(mov) + 1 + " inválido.");
			}
		}
	}
}