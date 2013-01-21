package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.NegociacaoDividaDetalheVO;
import br.com.abril.nds.dto.ImpressaoNegociacaoDTO;
import br.com.abril.nds.dto.ImpressaoNegociacaoParecelaDTO;
import br.com.abril.nds.dto.NegociacaoDividaDTO;
import br.com.abril.nds.dto.NegociacaoDividaPaginacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacaoDivida;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
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
import br.com.abril.nds.model.financeiro.ParcelaNegociacao;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BancoRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.repository.FormaCobrancaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.NegociacaoDividaRepository;
import br.com.abril.nds.repository.ParcelaNegociacaoRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.DocumentoCobrancaService;
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
	
	@Autowired
	private DocumentoCobrancaService documentoCobrancaService;
	
	@Autowired
	private BancoRepository bancoRepository;
	
	@Autowired
	private DescontoService descontoService;
	
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
	public Long criarNegociacao(Integer numeroCota, List<ParcelaNegociacao> parcelas, BigDecimal valorDividaParaComissao, 
			List<Long> idsCobrancasOriginarias, Usuario usuarioResponsavel, boolean negociacaoAvulsa, Integer ativarCotaAposParcela,
			BigDecimal comissaoParaSaldoDivida, boolean isentaEncargos, FormaCobranca formaCobranca, Long idBanco) {
		
		//lista para mensagens de validação
		List<String> msgs = new ArrayList<String>();
		Date dataAtual = new Date();
		
		if (formaCobranca != null){
		
			Banco banco = this.bancoRepository.buscarPorId(idBanco);
			formaCobranca.setBanco(banco);
		}
		
		//valida dados de entrada
		this.validarDadosEntrada(msgs, dataAtual, parcelas, valorDividaParaComissao, usuarioResponsavel, 
				ativarCotaAposParcela, comissaoParaSaldoDivida, formaCobranca);
		
		//Cota e Cobrança originária não são validados no método acima
		//para evitar que se faça duas vezes a mesma consulta
		Cota cota = null;
		if (numeroCota == null){
			
			msgs.add("Cota da negociação não encontrada.");
		} else {
			
			cota = this.cotaRepository.obterPorNumerDaCota(numeroCota);
			
			if (cota == null){
				
				msgs.add("Cota da negociação não encontrada.");
			}
		}
		
		List<Cobranca> cobrancasOriginarias = new ArrayList<Cobranca>();

		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = 
				this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.POSTERGADO_NEGOCIACAO);

		//Cobrança da onde se originou a negociação
		for (Long idCobranca : idsCobrancasOriginarias){
			
			Cobranca cobrancaOriginaria = this.cobrancaRepository.buscarPorId(idCobranca);
			
			if (cobrancaOriginaria == null){
				
				msgs.add("Cobrança de ID "+ idCobranca +" não encontrada.");
			} else {
			
				//Cobrança original deve ter seu status modificado para pago
				//e sua divida deve ter seus status modificado para negociada
				cobrancaOriginaria.setStatusCobranca(StatusCobranca.PAGO);
				cobrancaOriginaria.getDivida().setStatus(StatusDivida.NEGOCIADA);
				this.dividaRepository.merge(cobrancaOriginaria.getDivida());
				this.cobrancaRepository.merge(cobrancaOriginaria);
				
				cobrancasOriginarias.add(cobrancaOriginaria);
			}
		}
		
		if (!msgs.isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, msgs);
		}
		
		//caso a negociacão seja feita em parcelas
		if (parcelas != null){
			
			BigDecimal totalNegociacao = BigDecimal.ZERO;
			//Popula o movimento financeiro de cada parcela
			//Caso seja uma negociação avulsa o movimento financeiro servirá
			//para rastreabilidade da negociação, caso não seja uma negociação avulsa
			//será insumo para próxima geração de cobrança
			for (ParcelaNegociacao parcelaNegociacao : parcelas){
				
				parcelaNegociacao.getMovimentoFinanceiroCota().setCota(cota);
				parcelaNegociacao.getMovimentoFinanceiroCota().setUsuario(usuarioResponsavel);
				parcelaNegociacao.getMovimentoFinanceiroCota().setData(dataAtual);
				parcelaNegociacao.getMovimentoFinanceiroCota().setDataCriacao(dataAtual);
				parcelaNegociacao.getMovimentoFinanceiroCota().setStatus(StatusAprovacao.APROVADO);
				parcelaNegociacao.getMovimentoFinanceiroCota().setAprovador(usuarioResponsavel);
				parcelaNegociacao.getMovimentoFinanceiroCota().setTipoMovimento(tipoMovimentoFinanceiro);
				
				totalNegociacao = totalNegociacao.add(parcelaNegociacao.getMovimentoFinanceiroCota().getValor());
				
				this.movimentoFinanceiroCotaRepository.adicionar(parcelaNegociacao.getMovimentoFinanceiroCota());
			}
			
			//Caso essa seja uma negociação avulsa as parcelas não devem entrar nas próximas
			//gerações de cobrança, para isso é necessário criar um consolidado financeiro para
			//os movimentos financeiros das parcelas
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
							cobranca = new CobrancaDinheiro();
						break;
						case TRANSFERENCIA_BANCARIA:
							cobranca = new CobrancaTransferenciaBancaria();
						break;
					}
					
					cobranca.setCota(cota);
					cobranca.setBanco(formaCobranca.getBanco());
					cobranca.setDivida(divida);
					cobranca.setDataEmissao(dataAtual);
					cobranca.setStatusCobranca(StatusCobranca.NAO_PAGO);
					cobranca.setDataVencimento(parcelaNegociacao.getDataVencimento());
					cobranca.setValor(totalNegociacao);
					
					Banco banco = formaCobranca.getBanco();
					
					cobranca.setNossoNumero(
							Util.gerarNossoNumero(
									numeroCota, 
									dataAtual, 
									banco.getNumeroBanco(),
									null, 
									parcelaNegociacao.getMovimentoFinanceiroCota().getId(),
									banco.getAgencia(),
									banco.getConta(),
									banco.getCarteira()
							)
					);
					
					this.dividaRepository.adicionar(divida);
					this.cobrancaRepository.adicionar(cobranca);
				}
			}
			
			this.formaCobrancaRepository.adicionar(formaCobranca);
		}
		
		//cria registro da negociação
		Negociacao negociacao = new Negociacao();
		negociacao.setAtivarCotaAposParcela(ativarCotaAposParcela);
		negociacao.setCobrancasOriginarias(cobrancasOriginarias);
		negociacao.setComissaoParaSaldoDivida(comissaoParaSaldoDivida);
		negociacao.setIsentaEncargos(isentaEncargos);
		negociacao.setNegociacaoAvulsa(negociacaoAvulsa);
		negociacao.setFormaCobranca(formaCobranca);
		negociacao.setParcelas(parcelas);
		negociacao.setValorDividaPagaComissao(valorDividaParaComissao);
		
		if (negociacao.getParcelas() != null){
			
			for (ParcelaNegociacao parcelaNegociacao : negociacao.getParcelas()){
				
				parcelaNegociacao.setNegociacao(negociacao);
				
				this.parcelaNegociacaoRepository.adicionar(parcelaNegociacao);
			}
		}
		
		this.negociacaoDividaRepository.adicionar(negociacao);
		
		return negociacao.getId();
	}

	private void validarDadosEntrada(List<String> msgs, Date dataAtual,
			List<ParcelaNegociacao> parcelas, BigDecimal valorDividaParaComissao, Usuario usuarioResponsavel,
			Integer ativarCotaAposParcela, BigDecimal comissaoParaSaldoDivida,
			FormaCobranca formaCobranca) {
		
		//caso não tenha parcelas e nem comissão para saldo preenchidos
		if ((parcelas == null || parcelas.isEmpty()) && comissaoParaSaldoDivida == null){
			
			msgs.add("Forma de pagamento é obrigatória.");
		}
		
		//caso tenha parcelas
		if (parcelas != null && comissaoParaSaldoDivida == null){
			
			//data de vencimento e valor são campos obrigatórios
			for (ParcelaNegociacao parcelaNegociacao : parcelas){
				
				if (parcelaNegociacao.getDataVencimento() == null){
					
					msgs.add("Data de vencimento da parcela " + (parcelas.indexOf(parcelaNegociacao) + 1) + " inválido.");
				}
				
				MovimentoFinanceiroCota mov = parcelaNegociacao.getMovimentoFinanceiroCota();
				
				if (mov == null){
					
					msgs.add("Valor da parcela " + (parcelas.indexOf(parcelaNegociacao) + 1) + " inválido.");
				} else {
				
					if (mov.getValor() == null || mov.getValor().equals(BigDecimal.ZERO)){
						
						msgs.add("Valor da parcela " + (parcelas.indexOf(parcelaNegociacao) + 1) + " inválido.");
					}
				}
			}
		} else {
			
			if (valorDividaParaComissao == null){
				
				msgs.add("Valor total da negociação inválido.");
			}
		}
		
		//usuário responsável por fazer a negociação é obrigatório
		if (usuarioResponsavel == null || usuarioResponsavel.getId() == null){
			
			msgs.add("Usuário responsável pela negociação inválido.");
		}
		
		//se a comissão não foi preenchida deve haver forma de cobrança e tipo de cobrança
		//para as parcelas
		if ((comissaoParaSaldoDivida == null && formaCobranca == null) ||
				(comissaoParaSaldoDivida != null && formaCobranca != null)){
			
			msgs.add("A negociação deve ter saldo ou parcelas.");
		} else {
			
			if (formaCobranca != null){
				
				if (formaCobranca.getTipoCobranca() == null){
					
					msgs.add("Parâmetro Tipo de Cobrança inválido.");
				}
				
				if (formaCobranca.getBanco() == null || 
						formaCobranca.getBanco().getId() == null){
					
					msgs.add("Banco é obrigatório.");
				}
				
				if (formaCobranca.getTipoFormaCobranca() == null){
					
					msgs.add("Frequência da cobrança é obrigatório.");
				} else {
				
					switch (formaCobranca.getTipoFormaCobranca()){
						case DIARIA:
						break;
						case MENSAL:
							if (formaCobranca.getDiasDoMes() == null || 
									formaCobranca.getDiasDoMes().isEmpty() || 
									formaCobranca.getDiasDoMes().size() != 2){
							
								msgs.add("Parâmetro dias da cobrança inválidos.");
							}
						break;
						case QUINZENAL:
							if (formaCobranca.getDiasDoMes() == null || 
									formaCobranca.getDiasDoMes().isEmpty() || 
									formaCobranca.getDiasDoMes().size() != 2){
								
								msgs.add("Parâmetro dias da cobrança inválidos.");
							}
						break;
						case SEMANAL:
							if (formaCobranca.getConcentracaoCobrancaCota() == null || formaCobranca.getConcentracaoCobrancaCota().isEmpty() ){
								msgs.add("Selecione pelo menos um dia da semana.");
							}
						break;
					}
				}
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Negociacao obterNegociacaoPorId(Long idNegociacao) {
		
		Negociacao negociacao = this.negociacaoDividaRepository.buscarPorId(idNegociacao);
		
		Hibernate.initialize(negociacao.getParcelas());
		Hibernate.initialize(negociacao.getCobrancasOriginarias());
		
		return negociacao;
	}

	@Override
	@Transactional
	public List<byte[]> gerarBoletosNegociacao(Long idNegociacao) {
		
		List<byte[]> boletos = new ArrayList<byte[]>();
		
		Negociacao negociacao = this.negociacaoDividaRepository.buscarPorId(idNegociacao);
		
		if (negociacao != null){
			
			if (negociacao.isNegociacaoAvulsa()){
				
				for (ParcelaNegociacao parcelaNegociacao : negociacao.getParcelas()){
					
					String nossoNumero = this.cobrancaRepository.obterNossoNumeroPorMovimentoFinanceiroCota(
							parcelaNegociacao.getMovimentoFinanceiroCota().getId());
					
					boletos.add(this.documentoCobrancaService.gerarDocumentoCobranca(nossoNumero));
				}
			}
		}
		
		return boletos;
	}

	@Override
	@Transactional(readOnly = true)
	public byte[] imprimirNegociacao(Long idNegociacao) throws Exception {
		
		Negociacao negociacao = this.obterNegociacaoPorId(idNegociacao);
		
		if (negociacao == null){
			
			throw new ValidacaoException(
					TipoMensagem.WARNING, "Negociação não encontrada.");
		}
		
		Cota cota = negociacao.getCobrancasOriginarias().get(0).getCota();
		
		BigDecimal totalDividaSelecionada = BigDecimal.ZERO;
		
		for (Cobranca cobranca : negociacao.getCobrancasOriginarias()){
			
			totalDividaSelecionada = totalDividaSelecionada.add(cobranca.getValor());
		}
		
		ImpressaoNegociacaoDTO impressaoNegociacaoDTO = new ImpressaoNegociacaoDTO();
		//campo cota(numero)
		impressaoNegociacaoDTO.setNumeroCota(cota.getNumeroCota());
		//campo nome
		impressaoNegociacaoDTO.setNomeCota(cota.getPessoa().getNome());
		//campo divida selecionada
		impressaoNegociacaoDTO.setTotalDividaSelecionada(totalDividaSelecionada);
		
		//campo Comissão da Cota para pagamento da dívida
		impressaoNegociacaoDTO.setComissaoParaPagamento(negociacao.getComissaoParaSaldoDivida());
		
		if (negociacao.getParcelas() == null || negociacao.getParcelas().isEmpty()){
		
			BigDecimal comissaoAtual = this.descontoService.obterComissaoCota(cota.getNumeroCota());
			
			if (comissaoAtual == null){
				
				comissaoAtual = BigDecimal.ZERO;
			}
			
			//campo Comissão da Cota
			impressaoNegociacaoDTO.setComissaoAtualCota(comissaoAtual);
			
			//campo Comissão da Cota enquanto houver saldo de dívida
			impressaoNegociacaoDTO.setComissaoCotaEnquantoHouverSaldo(
					comissaoAtual.subtract(negociacao.getComissaoParaSaldoDivida()));
		}
		
		//campo frequencia de pagamento
		if (negociacao.getFormaCobranca() != null){
			
			String aux = "";
			
			TipoFormaCobranca tipoFormaCobranca = negociacao.getFormaCobranca().getTipoFormaCobranca();
			
			switch (tipoFormaCobranca){
				case DIARIA:
					
				break;
				case MENSAL:
					aux = "Todo dia " + negociacao.getFormaCobranca().getDiasDoMes().get(0);
				break;
				case QUINZENAL:
					aux = "Todo dia " + negociacao.getFormaCobranca().getDiasDoMes().get(0) +
							" e " + negociacao.getFormaCobranca().getDiasDoMes().get(1);
				break;
				case SEMANAL:
					for (ConcentracaoCobrancaCota concen : negociacao.getFormaCobranca().getConcentracaoCobrancaCota()){
						
						aux = aux + concen.getDiaSemana().getDescricaoDiaSemana();
					}
				break;
			}
			
			impressaoNegociacaoDTO.setFrequenciaPagamento(
					negociacao.getFormaCobranca().getTipoFormaCobranca().getDescricao() + 
					(!negociacao.getFormaCobranca().getTipoFormaCobranca().equals(TipoFormaCobranca.DIARIA) ? ": " : " ") + aux);
			
			Banco banco = negociacao.getFormaCobranca().getBanco();
			
			impressaoNegociacaoDTO.setNumeroBanco(banco.getNumeroBanco());
			impressaoNegociacaoDTO.setNomeBanco(banco.getNome());
			impressaoNegociacaoDTO.setAgenciaBanco(banco.getAgencia());
			impressaoNegociacaoDTO.setContaBanco(banco.getConta());
		}
		
		//campo negociacao avulsa
		impressaoNegociacaoDTO.setNegociacaoAvulsa(negociacao.isNegociacaoAvulsa());
		//campo isenta encargos
		impressaoNegociacaoDTO.setIsentaEncargos(negociacao.isIsentaEncargos());
		
		impressaoNegociacaoDTO.setParcelasCheques(new ArrayList<ImpressaoNegociacaoParecelaDTO>());
		
		BigDecimal totalParcelas = BigDecimal.ZERO;
		for (ParcelaNegociacao parcela : negociacao.getParcelas()){
			
			ImpressaoNegociacaoParecelaDTO vo = new ImpressaoNegociacaoParecelaDTO();
			
			int nParcela = negociacao.getParcelas().indexOf(parcela) + 1;
			
			vo.setNumeroParcela(nParcela);
			
			if (negociacao.getAtivarCotaAposParcela() != null && 
					negociacao.getAtivarCotaAposParcela() == nParcela){
				
				vo.setAtivarAoPagar(true);
			}
			
			vo.setDataVencimento(parcela.getDataVencimento());
			vo.setNumeroCheque(parcela.getNumeroCheque());
			vo.setValor(parcela.getMovimentoFinanceiroCota().getValor());
			
			BigDecimal encargos = parcela.getEncargos() == null ? BigDecimal.ZERO : parcela.getEncargos();
			
			vo.setEncagos(encargos);
			vo.setParcelaTotal(
					parcela.getMovimentoFinanceiroCota().getValor().add(encargos));
			
			totalParcelas = totalParcelas.add(vo.getParcelaTotal());
			
			impressaoNegociacaoDTO.getParcelasCheques().add(vo);
		}
		
		List<ImpressaoNegociacaoDTO> listaJasper = new ArrayList<ImpressaoNegociacaoDTO>();
		listaJasper.add(impressaoNegociacaoDTO);
		
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(listaJasper);
		
		URL diretorioReports = Thread.currentThread().getContextClassLoader().getResource("reports/");
		
		String path = diretorioReports.toURI().getPath();
		
		if (impressaoNegociacaoDTO.getParcelasCheques().isEmpty()){
			
			path += "/negociacao_divida_comissao.jasper";
		} else if (impressaoNegociacaoDTO.getParcelasCheques().get(0).getNumeroCheque() == null){
			
			path += "/negociacao_divida_boleto.jasper";
			
		} else {
			
			path += "/negociacao_divida_cheque.jasper";
		}
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("TOTAL_PARCELAS", totalParcelas.setScale(2, RoundingMode.HALF_EVEN).toString());
		parameters.put("SUBREPORT_DIR", diretorioReports.toURI().getPath());
		
		return JasperRunManager.runReportToPdf(path, parameters, jrDataSource);
	}

	@Override
	@Transactional
	public List<NegociacaoDividaDetalheVO> obterDetalhesCobranca(Long idCobranca) {
		
		if (idCobranca == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Id da cobrança inválido.");
		}
		
		return this.cobrancaRepository.obterDetalhesCobranca(idCobranca);
	}
}