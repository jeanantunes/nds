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
import br.com.abril.nds.model.cadastro.TipoCobranca;
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
import br.com.abril.nds.model.financeiro.ParcelaNegociacao;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.repository.FormaCobrancaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.NegociacaoDividaRepository;
import br.com.abril.nds.repository.ParcelaNegociacaoRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.service.NegociacaoDividaService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;

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
	
	@Autowired
	private ParcelaNegociacaoRepository parcelaNegociacaoRepository;
	
	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	@Autowired
	private FormaCobrancaRepository formaCobrancaRepository;
	
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
	public void criarNegociacao(Integer numeroCota, List<ParcelaNegociacao> parcelas, Long idCobrancaOriginaria, 
			Usuario usuarioResponsavel, boolean negociacaoAvulsa, Integer ativarCotaAposParcela,
			BigDecimal comissaoParaSaldoDivida, boolean isentaEncargos, FormaCobranca formaCobranca) {
		
		List<String> msgs = new ArrayList<String>();
		Date dataAtual = new Date();
		
		this.validarDadosEntrada(msgs, dataAtual, parcelas, usuarioResponsavel, 
				ativarCotaAposParcela, comissaoParaSaldoDivida, formaCobranca);
		
		Cota cota = null;
		if (numeroCota == null){
			
			msgs.add("Cota da negociação não encontrada.");
		} else {
			
			cota = this.cotaRepository.obterPorNumerDaCota(numeroCota);
			
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
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = new TipoMovimentoFinanceiro();
		tipoMovimentoFinanceiro.setGrupoMovimentoFinaceiro(GrupoMovimentoFinaceiro.POSTERGADO_NEGOCIACAO);
		tipoMovimentoFinanceiro.setOperacaoFinaceira(OperacaoFinaceira.DEBITO);
		tipoMovimentoFinanceiro.setDescricao("TESTE NEGOCIAÇÃO DIVIDA");
		this.tipoMovimentoFinanceiroRepository.adicionar(tipoMovimentoFinanceiro);
		
		BigDecimal totalNegociacao = BigDecimal.ZERO;
		for (ParcelaNegociacao parcelaNegociacao : parcelas){
			
			parcelaNegociacao.getMovimentoFinanceiroCota().setCota(cobrancaOriginaria.getCota());
			parcelaNegociacao.getMovimentoFinanceiroCota().setUsuario(usuarioResponsavel);
			parcelaNegociacao.getMovimentoFinanceiroCota().setData(dataAtual);
			parcelaNegociacao.getMovimentoFinanceiroCota().setDataCriacao(dataAtual);
			parcelaNegociacao.getMovimentoFinanceiroCota().setStatus(StatusAprovacao.APROVADO);
			parcelaNegociacao.getMovimentoFinanceiroCota().setAprovador(usuarioResponsavel);
			parcelaNegociacao.getMovimentoFinanceiroCota().setTipoMovimento(tipoMovimentoFinanceiro);
			
			totalNegociacao = totalNegociacao.add(parcelaNegociacao.getMovimentoFinanceiroCota().getValor());
			
			this.movimentoFinanceiroCotaRepository.adicionar(parcelaNegociacao.getMovimentoFinanceiroCota());
			this.parcelaNegociacaoRepository.adicionar(parcelaNegociacao);
		}
		
		this.dividaRepository.merge(cobrancaOriginaria.getDivida());
		this.cobrancaRepository.merge(cobrancaOriginaria);
		this.formaCobrancaRepository.adicionar(formaCobranca);
		
		if (negociacaoAvulsa){
			
			for (ParcelaNegociacao parcelaNegociacao : parcelas){
				
				ConsolidadoFinanceiroCota consolidado = new ConsolidadoFinanceiroCota();
				consolidado.setCota(cota);
				consolidado.setDataConsolidado(dataAtual);
				List<MovimentoFinanceiroCota> movs = new ArrayList<MovimentoFinanceiroCota>();
				movs.add(parcelaNegociacao.getMovimentoFinanceiroCota());
				consolidado.setMovimentos(movs);
				consolidado.setTotal(totalNegociacao);
				
				this.consolidadoFinanceiroRepository.adicionar(consolidado);
				
				Divida divida = new Divida();
				divida.setData(dataAtual);
				divida.setResponsavel(usuarioResponsavel);
				divida.setCota(cota);
				divida.setValor(parcelaNegociacao.getMovimentoFinanceiroCota().getValor());
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
				cobranca.setDataVencimento(parcelaNegociacao.getDataVencimento());
				cobranca.setValor(totalNegociacao);
				cobranca.setNossoNumero(String.valueOf((System.currentTimeMillis())));
				
				this.dividaRepository.adicionar(divida);
				this.cobrancaRepository.adicionar(cobranca);
			}
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
			List<ParcelaNegociacao> parcelas, Usuario usuarioResponsavel,
			Integer ativarCotaAposParcela, BigDecimal comissaoParaSaldoDivida,
			FormaCobranca formaCobranca) {
		
		if (parcelas == null || parcelas.isEmpty() && comissaoParaSaldoDivida == null){
			
			msgs.add("Forma de pagamento é obrigatória.");
		}
		
		if (parcelas != null && comissaoParaSaldoDivida == null){
			
			for (ParcelaNegociacao parcelaNegociacao : parcelas){
				
				if (parcelaNegociacao.getDataVencimento() == null){
					
					msgs.add("Data de vencimento da parcela " + parcelas.indexOf(parcelaNegociacao) + 1 + " inválido.");
				}
				
				MovimentoFinanceiroCota mov = parcelaNegociacao.getMovimentoFinanceiroCota();
				
				if (mov == null){
					
					msgs.add("Valor da parcela " + parcelas.indexOf(parcelaNegociacao) + 1 + " inválido.");
				} else {
				
					if (mov.getValor() == null || mov.getValor().equals(BigDecimal.ZERO)){
						
						msgs.add("Valor da parcela " + parcelas.indexOf(parcelaNegociacao) + 1 + " inválido.");
					}
				}
			}
		}
		
		if (usuarioResponsavel == null || usuarioResponsavel.getId() == null){
			
			msgs.add("Usuário responsável pela negociação inválido.");
		}
		
		if (formaCobranca == null){
			
			msgs.add("Parâmetro Forma Cobrança inválido.");
		} else {
			
			if (comissaoParaSaldoDivida == null){
				
				if (formaCobranca.getTipoCobranca() == null){
					
					msgs.add("Parâmetro Tipo de Cobrança inválido.");
				}
				
				if (formaCobranca.getTipoFormaCobranca() == null){
					
					msgs.add("Parâmetro Tipo de Forma de Cobrança inválido.");
				}
				
				
			} else {
				
				if (formaCobranca.getTipoCobranca() != null || formaCobranca.getTipoFormaCobranca() != null){
					
					msgs.add("Apenas uma forma de cobrança é permitida.");
				}
			}
		}
	}
}