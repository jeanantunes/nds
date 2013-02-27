package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.BoletoDistribuidor;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.CobrancaBoletoEmBranco;
import br.com.abril.nds.model.financeiro.CobrancaCheque;
import br.com.abril.nds.model.financeiro.CobrancaDeposito;
import br.com.abril.nds.model.financeiro.CobrancaDinheiro;
import br.com.abril.nds.model.financeiro.CobrancaOutros;
import br.com.abril.nds.model.financeiro.CobrancaTransferenciaBancaria;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.ControleBaixaBancaria;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.HistoricoAcumuloDivida;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.StatusInadimplencia;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BoletoDistribuidorRepository;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.CobrancaControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.repository.ControleBaixaBancariaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.repository.HistoricoAcumuloDividaRepository;
import br.com.abril.nds.repository.ItemChamadaEncalheFornecedorRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.DocumentoCobrancaService;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.FormaCobrancaService;
import br.com.abril.nds.service.GeradorArquivoCobrancaBancoService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.exception.AutenticacaoEmailException;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.AnexoEmail;
import br.com.abril.nds.util.AnexoEmail.TipoAnexo;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class GerarCobrancaServiceImpl implements GerarCobrancaService {

	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	@Autowired
	private ConsolidadoFinanceiroRepository consolidadoFinanceiroRepository;
	
	@Autowired
	private ControleBaixaBancariaRepository controleBaixaBancariaRepository;
	
	@Autowired
	private DividaRepository dividaRepository;
	
	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	@Autowired
	private CobrancaRepository cobrancaRepository;
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Autowired
	private HistoricoAcumuloDividaRepository historicoAcumuloDividaRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private DocumentoCobrancaService documentoCobrancaService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private CobrancaService cobrancaService;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private ChamadaEncalheCotaRepository chamadaEncalheCotaRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private GeradorArquivoCobrancaBancoService geradorArquivoCobrancaBancoService;
	
	@Autowired
	private CobrancaControleConferenciaEncalheCotaRepository cobrancaControleConferenciaEncalheCotaRepository;
	
	@Autowired
	private BoletoDistribuidorRepository boletoDistribuidorRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ItemChamadaEncalheFornecedorRepository itemChamadaEncalheFornecedorRepository;
	
	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
	
	
	@Autowired
	private FormaCobrancaService formaCobrancaService;
	
	/**
	 * Obtém a situação da cota
	 * @param idCota
	 * @return SituacaoCadastro
	 */
	private SituacaoCadastro obterSitiacaoCadastroCota(Long idCota){
		Cota cota  = this.cotaRepository.buscarPorId(idCota);
		return cota.getSituacaoCadastro();
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean verificarCobrancasGeradas(List<Long> idsCota){
		
		return this.consolidadoFinanceiroRepository.obterQuantidadeDividasGeradasData(idsCota) > 0;
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean verificarCobrancasGeradasNaDataVencimentoDebito(Date dataVencimentoDebito,Long... idsCota ){
		
		return this.consolidadoFinanceiroRepository.obterQuantidadeDividasGeradasData(dataVencimentoDebito,idsCota) > 0;
	}

	@Override
	@Transactional(noRollbackFor = GerarCobrancaValidacaoException.class)
	public void gerarCobranca(Long idCota, Long idUsuario, Set<String> setNossoNumero)
		throws GerarCobrancaValidacaoException {
		
		this.gerarCobrancaCota(idCota, idUsuario, setNossoNumero);
		
		this.geradorArquivoCobrancaBancoService.prepararGerarArquivoCobrancaCnab();
	}
	
	private void gerarCobrancaCota(Long idCota, Long idUsuario, Set<String> setNossoNumero) throws GerarCobrancaValidacaoException {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		Date dataOperacao = distribuidor.getDataOperacao();
		
		//cancela cobrança gerada para essa data de operação para efetuar recalculo
		this.cancelarDividaCobranca(null, idCota);

		FormaCobranca fc = this.formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();
			
		PoliticaCobranca politicaPrincipal = fc.getPoliticaCobranca();

		if (politicaPrincipal == null){
			throw new GerarCobrancaValidacaoException(
					new ValidacaoException(TipoMensagem.WARNING, "Politica de cobrança não encontrada."));
		} else if (politicaPrincipal.getFormaCobranca() == null){
			throw new GerarCobrancaValidacaoException(
					new ValidacaoException(TipoMensagem.WARNING, "Forma de cobrança não encontrada."));
		}

		//Caso o principal modo de cobrança seja boleto a baixa automática deve ter sido executada
		if (TipoCobranca.BOLETO.equals(politicaPrincipal.getFormaCobranca().getTipoCobranca())){
			
			List<ControleBaixaBancaria> listaControleBaixaBancaria =
				this.controleBaixaBancariaRepository.obterListaControleBaixaBancaria(
					dataOperacao, StatusControle.CONCLUIDO_SUCESSO);
			
			
			//TODO: VERIFICAR NECESSIDADE DESTA VALIDAÇÃO, POIS VOLTOU COMO ERRO NO TRAC 171
			/*
			if (listaControleBaixaBancaria == null || listaControleBaixaBancaria.isEmpty()) {
				
				throw new GerarCobrancaValidacaoException(
					new ValidacaoException(TipoMensagem.WARNING, "Baixa Automática ainda não executada."));
			}
			*/
			
		}
		
		List<String> msgs = new ArrayList<String>();
		
		// buscar movimentos financeiros da cota, se informada, caso contrario de todas as cotas
		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterMovimentoFinanceiroCota(idCota);
		
		if (listaMovimentoFinanceiroCota != null && !listaMovimentoFinanceiroCota.isEmpty()){
			
			//Varre todos os movimentos encontrados, agrupando por cota e por fornecedor
			Cota ultimaCota = listaMovimentoFinanceiroCota.get(0).getCota();
			
			Fornecedor ultimoFornecedor = null;
			
			if (!politicaPrincipal.isUnificaCobranca() && 
					listaMovimentoFinanceiroCota.get(0).getMovimentos() != null && 
					!listaMovimentoFinanceiroCota.get(0).getMovimentos().isEmpty() &&
					listaMovimentoFinanceiroCota.get(0).getMovimentos().get(0) != null){
				
				ultimoFornecedor = listaMovimentoFinanceiroCota.get(0).getMovimentos().get(0).getProdutoEdicao().getProduto().getFornecedor();
			}
			
			TipoCobranca tipoCobranca = politicaPrincipal.getFormaCobranca().getTipoCobranca();
			
			List<MovimentoFinanceiroCota> movimentos = new ArrayList<MovimentoFinanceiroCota>();
			
			String nossoNumero = null;
			
			for (MovimentoFinanceiroCota movimentoFinanceiroCota : listaMovimentoFinanceiroCota){
				
				//verifica se cota esta suspensa, se estiver verifica se existe chamada de encalhe na data de operação
				if (SituacaoCadastro.SUSPENSO.equals(ultimaCota.getSituacaoCadastro())){
					
					if (!movimentoFinanceiroCota.getCota().equals(ultimaCota)){
						
						if (this.chamadaEncalheCotaRepository.obterQtdListaChamaEncalheCota(ultimaCota.getNumeroCota(), 
								dataOperacao, null, false, false, false) <= 0){
							
							continue;
						}
					}
				}
				
				MovimentoEstoqueCota mec = (movimentoFinanceiroCota.getMovimentos() != null && 
						!movimentoFinanceiroCota.getMovimentos().isEmpty() && 
						movimentoFinanceiroCota.getMovimentos().get(0) != null ?
								movimentoFinanceiroCota.getMovimentos().get(0) : 
								null);
				
				Fornecedor fornecedorProdutoMovimento = null;
				if (mec != null){
					
					if (mec.getProdutoEdicao() != null &&
							mec.getProdutoEdicao().getProduto() != null){
						
						fornecedorProdutoMovimento = 
								mec.getProdutoEdicao().getProduto().getFornecedor();
					} else if (mec.getEstoqueProdutoCota() != null && 
							mec.getEstoqueProdutoCota().getProdutoEdicao() != null && 
							mec.getEstoqueProdutoCota().getProdutoEdicao().getProduto() != null) {
						
						fornecedorProdutoMovimento = 
								mec.getEstoqueProdutoCota().getProdutoEdicao().getProduto().getFornecedor();
					}
				}
				
				if (politicaPrincipal.isUnificaCobranca() || 
						(movimentoFinanceiroCota.getCota().equals(ultimaCota) &&
						(fornecedorProdutoMovimento != null &&
								fornecedorProdutoMovimento.equals(ultimoFornecedor) ||
								fornecedorProdutoMovimento == ultimoFornecedor))){
					
					movimentos.add(movimentoFinanceiroCota);
				} else {
					
					//Decide se gera movimento consolidado ou postergado para a cota
					nossoNumero = this.inserirConsolidadoFinanceiro(ultimaCota, movimentos,
							politicaPrincipal.getFormaCobranca().getValorMinimoEmissao(), politicaPrincipal.isAcumulaDivida(), idUsuario, 
							tipoCobranca != null ? tipoCobranca : politicaPrincipal.getFormaCobranca().getTipoCobranca(),
							distribuidor.getNumeroDiasNovaCobranca(),
							dataOperacao, msgs, ultimoFornecedor);
					
					if (nossoNumero != null){
						
						setNossoNumero.add(nossoNumero);
					}
					
					//Limpa dados para contabilizar próxima cota
					ultimaCota = movimentoFinanceiroCota.getCota();
					
					if (!politicaPrincipal.isUnificaCobranca() && 
							movimentoFinanceiroCota.getMovimentos() != null && 
							!movimentoFinanceiroCota.getMovimentos().isEmpty() &&
							movimentoFinanceiroCota.getMovimentos().get(0) != null){
						
						ultimoFornecedor = movimentoFinanceiroCota.getMovimentos().get(0).getProdutoEdicao().getProduto().getFornecedor();
					} else {
						
						ultimoFornecedor = null;
					}
					
					movimentos = new ArrayList<MovimentoFinanceiroCota>();
					
					movimentos.add(movimentoFinanceiroCota);
				}
			}
			
			//Decide se gera movimento consolidado ou postergado para a ultima cota
			nossoNumero = this.inserirConsolidadoFinanceiro(ultimaCota, movimentos, politicaPrincipal.getFormaCobranca().getValorMinimoEmissao(),
					politicaPrincipal.isAcumulaDivida(), idUsuario, 
					tipoCobranca != null ? tipoCobranca : politicaPrincipal.getFormaCobranca().getTipoCobranca(),
						distribuidor.getNumeroDiasNovaCobranca(), dataOperacao, msgs, ultimoFornecedor);
			
			if (nossoNumero != null){
				
				setNossoNumero.add(nossoNumero);
			}
 
		}
		
		if (!msgs.isEmpty()){
			
			throw new GerarCobrancaValidacaoException(
					new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, msgs)));
		}
	}
	
	/**
	 * Retorna a data de vencimento para o boleto, sendo esta calculada 
	 * da seguinte forma:
	 * 
	 * É recuperada a data da Terça-feira dentro da semana utilizada na pesquisa 
	 * principal do fechamentoCEIntegração. A esta data são adicionados 2 dias
	 * úteis.
	 * 
	 * @param semana
	 * 
	 * @return Date
	 */
	private Date obterDataVencimentoBoletoDistribuidor(int semana) {
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		Date dataFechamentoSemana = DateUtil.obterDataDaSemanaNoAno(semana, DiaSemana.TERCA_FEIRA.getCodigoDiaSemana(), distribuidor.getDataOperacao());
		
		Date dataVencimento = this.calendarioService.adicionarDiasUteis(dataFechamentoSemana, 2);
				
		return dataVencimento;
	}
	
	/**
	 * Retorna o valor total do boleto com desconto.
	 * 
	 * @param idChamadaEncalheFornecedor
	 * @param valorBrutoBoleto
	 * 
	 * @return BigDecimal
	 */
	private BigDecimal obterValorBoleto(Long idChamadaEncalheFornecedor, BigDecimal valorBrutoBoleto) {
		
		BigDecimal valorTotalDesconto = 
				itemChamadaEncalheFornecedorRepository.obterTotalDoDescontoItensChamadaEncalheFornecedor(idChamadaEncalheFornecedor);
		
		if(valorTotalDesconto == null) {
			valorTotalDesconto = BigDecimal.ZERO;
		}
		
		return valorBrutoBoleto.subtract(valorTotalDesconto);
		
	}
	
	@Transactional
	public List<BoletoDistribuidor> gerarCobrancaBoletoDistribuidor(
			List<ChamadaEncalheFornecedor> listaChamadaEncalheFornecedor, 
			TipoCobranca tipoCobranca, int semana){
		
		List<BoletoDistribuidor> listaBoletoDistribuidor = new ArrayList<BoletoDistribuidor>();
		
		Distribuidor distribuidor = distribuidorRepository.obter();
		
		Date dataOperacao = distribuidor.getDataOperacao();
		
		Date dataAtual = new Date();
		
		Integer codigoDistribuidor = distribuidor.getCodigo();
		
		for(ChamadaEncalheFornecedor chamadaEncalheFornecedor : listaChamadaEncalheFornecedor) {
			
			BoletoDistribuidor boletoDistribuidor = 
					boletoDistribuidorRepository.obterBoletoDistribuidorPorChamadaEncalheFornecedor(chamadaEncalheFornecedor.getId());
			
			if(boletoDistribuidor!=null) {
				
				Integer vias = boletoDistribuidor.getVias();
				
				vias+=1;
				
				boletoDistribuidor.setVias(vias);
				
				if(TipoCobranca.BOLETO.equals(tipoCobranca)) {
					
					BigDecimal valorLiquidoBoleto = obterValorBoleto(chamadaEncalheFornecedor.getId(), chamadaEncalheFornecedor.getTotalVendaApurada());
					
					boletoDistribuidor.setValor(valorLiquidoBoleto);
					
					boletoDistribuidor.setTipoCobranca(tipoCobranca);
					
					boletoDistribuidorRepository.alterar(boletoDistribuidor);
					
				}
				
			} else {
				
				BigDecimal valorLiquidoBoleto = obterValorBoleto(chamadaEncalheFornecedor.getId(), chamadaEncalheFornecedor.getTotalVendaApurada());
				
				Fornecedor fornecedor = chamadaEncalheFornecedor.getFornecedor();
				
				if (chamadaEncalheFornecedor.getFornecedor().getBanco() == null) {
					throw new ValidacaoException(TipoMensagem.ERROR, "Fornecedor selecionado não possui banco vinculado!");
				}
				
				Banco banco = chamadaEncalheFornecedor.getFornecedor().getBanco();
				
				String nossoNumeroDistribuidor = Util.gerarNossoNumeroDistribuidor(
						codigoDistribuidor, 
						dataOperacao, 
						banco.getNumeroBanco(), 
						fornecedor.getId(), 
						chamadaEncalheFornecedor.getId());
				
				boletoDistribuidor = new BoletoDistribuidor();
				
				boletoDistribuidor.setBanco(banco);
				boletoDistribuidor.setChamadaEncalheFornecedor(chamadaEncalheFornecedor);
				
				boletoDistribuidor.setDataEmissao(dataAtual);
				
				boletoDistribuidor.setDataVencimento(obterDataVencimentoBoletoDistribuidor(semana));
				
				boletoDistribuidor.setNossoNumeroDistribuidor(nossoNumeroDistribuidor);
				boletoDistribuidor.setStatus(null);
				
				boletoDistribuidor.setFornecedor(chamadaEncalheFornecedor.getFornecedor());
				
				boletoDistribuidor.setTipoCobranca(tipoCobranca);
				
				if(TipoCobranca.BOLETO.equals(tipoCobranca)) {
					boletoDistribuidor.setValor(valorLiquidoBoleto);
				}
				
				boletoDistribuidor.setVias(1);
				
				boletoDistribuidorRepository.adicionar(boletoDistribuidor);
				
			}
			
			listaBoletoDistribuidor.add(boletoDistribuidor);
			
		}
		
		
		return listaBoletoDistribuidor;
		

	}
	
	private BigDecimal obterValorMinino(Cota cota, BigDecimal valorMininoDistribuidor){
		BigDecimal valorMinimo = 
				(cota.getParametroCobranca() != null && cota.getParametroCobranca().getValorMininoCobranca() != null) ?
						cota.getParametroCobranca().getValorMininoCobranca() :
							valorMininoDistribuidor;
						
		return valorMinimo;
	}
	
	private String inserirConsolidadoFinanceiro(Cota cota, List<MovimentoFinanceiroCota> movimentos, BigDecimal valorMininoDistribuidor,
			boolean acumulaDivida, Long idUsuario, TipoCobranca tipoCobranca, int qtdDiasNovaCobranca, Date dataOperacao, List<String> msgs,
			Fornecedor fornecedor){
		
		ConsolidadoFinanceiroCota consolidadoFinanceiroCota = new ConsolidadoFinanceiroCota();
		consolidadoFinanceiroCota.setCota(cota);
		consolidadoFinanceiroCota.setDataConsolidado(dataOperacao);
		consolidadoFinanceiroCota.setMovimentos(movimentos);
		consolidadoFinanceiroCota.setPendente(this.obterValorPendenteCobrancaConsolidado(cota.getNumeroCota()));
		
		BigDecimal vlMovFinanTotal = BigDecimal.ZERO;
		BigDecimal vlMovFinanDebitoCredito = BigDecimal.ZERO;
		BigDecimal vlMovFinanEncalhe = BigDecimal.ZERO;
		BigDecimal vlMovFinanEncargos = BigDecimal.ZERO;
		BigDecimal vlMovFinanVendaEncalhe = BigDecimal.ZERO;
		BigDecimal vlMovPostergado = BigDecimal.ZERO;
		BigDecimal vlMovConsignado = BigDecimal.ZERO;

		for (MovimentoFinanceiroCota movimentoFinanceiroCota : movimentos){
			
			if (!movimentoFinanceiroCota.getCota().getId().equals(cota.getId())) {
				continue;
			}

			switch (((TipoMovimentoFinanceiro) movimentoFinanceiroCota.getTipoMovimento()).getGrupoMovimentoFinaceiro()){
				case CREDITO:
					vlMovFinanTotal = vlMovFinanTotal.add(movimentoFinanceiroCota.getValor());
					vlMovFinanDebitoCredito = vlMovFinanDebitoCredito.add(movimentoFinanceiroCota.getValor());
				break;
				case COMPRA_NUMEROS_ATRAZADOS:
				case DEBITO:
					vlMovFinanTotal = 
							vlMovFinanTotal.add(
								movimentoFinanceiroCota.getValor() != null ? 
										movimentoFinanceiroCota.getValor().negate() : 
											BigDecimal.ZERO);
					
					vlMovFinanDebitoCredito = 
							vlMovFinanDebitoCredito.add(movimentoFinanceiroCota.getValor() != null ? 
									movimentoFinanceiroCota.getValor().negate() : 
										BigDecimal.ZERO);
				break;
				
				case ENVIO_ENCALHE:
					vlMovFinanTotal = vlMovFinanTotal.add(movimentoFinanceiroCota.getValor());
					vlMovFinanEncalhe = vlMovFinanEncalhe.add(movimentoFinanceiroCota.getValor());
				break;
				
				case ESTORNO_REPARTE_COTA_AUSENTE:
					vlMovFinanTotal = vlMovFinanTotal.add(movimentoFinanceiroCota.getValor());
				break;
				
				case JUROS:
					vlMovFinanTotal = 
							vlMovFinanTotal.add(
								movimentoFinanceiroCota.getValor() != null ? 
										movimentoFinanceiroCota.getValor().negate() : 
											BigDecimal.ZERO);
					
					vlMovFinanEncargos = vlMovFinanEncargos.add(
							movimentoFinanceiroCota.getValor() != null ? 
									movimentoFinanceiroCota.getValor().negate() : 
										BigDecimal.ZERO);
				break;
				
				case MULTA:
					vlMovFinanTotal = 
							vlMovFinanTotal.add(
								movimentoFinanceiroCota.getValor() != null ? 
										movimentoFinanceiroCota.getValor().negate() : 
											BigDecimal.ZERO);
					
					vlMovFinanEncargos = vlMovFinanEncargos.add(
							movimentoFinanceiroCota.getValor() != null ? 
									movimentoFinanceiroCota.getValor().negate() : 
										BigDecimal.ZERO);
				break;
				
				case POSTERGADO_DEBITO:
					vlMovFinanTotal = 
							vlMovFinanTotal.add(
								movimentoFinanceiroCota.getValor() != null ? 
										movimentoFinanceiroCota.getValor().negate() : 
											BigDecimal.ZERO);
					
					vlMovPostergado = vlMovPostergado.add(
							movimentoFinanceiroCota.getValor() != null ? 
									movimentoFinanceiroCota.getValor().negate() : 
										BigDecimal.ZERO);
				break;
				
				case POSTERGADO_CREDITO:
					vlMovFinanTotal = 
						vlMovFinanTotal.add(
							movimentoFinanceiroCota.getValor() != null ? 
									movimentoFinanceiroCota.getValor() : 
										BigDecimal.ZERO);
					
					vlMovPostergado = vlMovPostergado.add(
							movimentoFinanceiroCota.getValor() != null ? 
									movimentoFinanceiroCota.getValor() : 
										BigDecimal.ZERO);
				break;
				
				case RECEBIMENTO_REPARTE:
					vlMovFinanTotal = 
							vlMovFinanTotal.add(
								movimentoFinanceiroCota.getValor() != null ? 
										movimentoFinanceiroCota.getValor().negate() : 
											BigDecimal.ZERO);
					vlMovConsignado = 
							vlMovConsignado.add(movimentoFinanceiroCota.getValor() != null ?
									movimentoFinanceiroCota.getValor() :
										BigDecimal.ZERO);
				break;
				
				case RECUPERACAO_REPARTE_COTA_AUSENTE:
					vlMovFinanTotal = 
							vlMovFinanTotal.add(
								movimentoFinanceiroCota.getValor() != null ? 
										movimentoFinanceiroCota.getValor().negate() : 
											BigDecimal.ZERO);
				break;
				case COMPRA_ENCALHE_SUPLEMENTAR:
					vlMovFinanTotal = 
						vlMovFinanTotal.add(
							movimentoFinanceiroCota.getValor() != null ? 
									movimentoFinanceiroCota.getValor().negate() : 
										BigDecimal.ZERO);
				break;
				case DEBITO_SOBRE_FATURAMENTO:
					vlMovFinanTotal = 
						vlMovFinanTotal.add(
							movimentoFinanceiroCota.getValor() != null ? 
									movimentoFinanceiroCota.getValor().negate() : 
										BigDecimal.ZERO);
				break;
				case POSTERGADO_NEGOCIACAO:
					vlMovFinanTotal = 
						vlMovFinanTotal.add(
							movimentoFinanceiroCota.getValor() != null ? 
									movimentoFinanceiroCota.getValor().negate() : 
										BigDecimal.ZERO);
				break;
				case VENDA_TOTAL:
					vlMovFinanTotal = 
						vlMovFinanTotal.add(
							movimentoFinanceiroCota.getValor() != null ? 
									movimentoFinanceiroCota.getValor().negate() : 
										BigDecimal.ZERO);
				break;
			}
		}
		
		consolidadoFinanceiroCota.setTotal(vlMovFinanTotal);
		consolidadoFinanceiroCota.setDebitoCredito(vlMovFinanDebitoCredito);
		consolidadoFinanceiroCota.setEncalhe(vlMovFinanEncalhe);
		consolidadoFinanceiroCota.setEncargos(vlMovFinanEncargos);
		consolidadoFinanceiroCota.setVendaEncalhe(vlMovFinanVendaEncalhe);
		consolidadoFinanceiroCota.setValorPostergado(vlMovPostergado);
		consolidadoFinanceiroCota.setConsignado(vlMovConsignado);
		
		Usuario usuario = this.usuarioRepository.buscarPorId(idUsuario);
		
		FormaCobranca formaCobrancaPrincipal = this.formaCobrancaService.obterFormaCobrancaPrincipalCota(cota.getId());

		if (formaCobrancaPrincipal == null){

			// Obtém a forma de cobrança principal
			formaCobrancaPrincipal = formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();

			if (formaCobrancaPrincipal == null) {
				msgs.add("Forma de cobrança principal para cota de número: " + cota.getNumeroCota() + " não encontrada. Também não encontrada forma de cobrança padrão principal.");
				return null;
			}
			
		}

		Date dataVencimento = null;
		
		List<Integer> diasSemanaConcentracaoPagamento = null;
		
		//obtem a data de vencimento de acordo com o dia em que se concentram os pagamentos da cota
		int fatorVencimento = 0;
		
		ParametroCobrancaCota parametroCobrancaCota = cota.getParametroCobranca();
		
		if(parametroCobrancaCota!=null && parametroCobrancaCota.getFatorVencimento()!=null) {
			fatorVencimento = parametroCobrancaCota.getFatorVencimento();
		}
		
		boolean cobrarHoje = false;
		
		switch(formaCobrancaPrincipal.getTipoFormaCobranca()){

			case DIARIA:
				dataVencimento = 
				this.calendarioService.adicionarDiasUteis(consolidadoFinanceiroCota.getDataConsolidado(), 
														  fatorVencimento, 
														  null, 
														  null);
				cobrarHoje = true;
			break;
			
			case QUINZENAL:
				dataVencimento = 
				this.calendarioService.adicionarDiasUteis(consolidadoFinanceiroCota.getDataConsolidado(), 
														  fatorVencimento,
														  null, 
														  formaCobrancaPrincipal.getDiasDoMes());
				cobrarHoje = 
						formaCobrancaPrincipal.getDiasDoMes().contains(
								Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
			break;
			
			case MENSAL:
				dataVencimento = 
				this.calendarioService.adicionarDiasUteis(consolidadoFinanceiroCota.getDataConsolidado(), 
														  fatorVencimento,
														  null, 
														  formaCobrancaPrincipal.getDiasDoMes());
				cobrarHoje =
						formaCobrancaPrincipal.getDiasDoMes().get(0).equals(
								Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
			break;
			
			case SEMANAL:
				diasSemanaConcentracaoPagamento = this.cotaRepository.obterDiasConcentracaoPagamentoCota(cota.getId());
				
				dataVencimento = 
				this.calendarioService.adicionarDiasUteis(consolidadoFinanceiroCota.getDataConsolidado(), 
														  fatorVencimento,
														  diasSemanaConcentracaoPagamento, 
														  null);
				
				cobrarHoje = 
						diasSemanaConcentracaoPagamento.contains(
								Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
			break;
		}
		
		if (dataVencimento == null){
			
			msgs.add("Não foi possível calcular data de vencimento da cobrança, verifique os parâmetros de cobrança da cota número: " + cota.getNumeroCota());
			
			return null;
		}
		
		Divida novaDivida = null;
		
		HistoricoAcumuloDivida historicoAcumuloDivida = null;
		
		MovimentoFinanceiroCota movimentoFinanceiroCota = null;
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = null;
			
		boolean cotaSuspensa = SituacaoCadastro.SUSPENSO.equals(this.obterSitiacaoCadastroCota(cota.getId()));

		BigDecimal valorMinino = 
				this.obterValorMinino(cota, valorMininoDistribuidor);
		
		//caso tenha alcançado o valor minino de cobrança e seja um dia de concentração de cobrança, ou a cota esteja suspensa
		if ( (vlMovFinanTotal.compareTo(BigDecimal.ZERO) < 0) &&
				(vlMovFinanTotal.abs().compareTo(valorMinino) > 0 && cobrarHoje) || 
				(vlMovFinanTotal.abs().compareTo(valorMinino) > 0 && cotaSuspensa)){

			if (formaCobrancaPrincipal.getBanco() == null) {
				
				return null;
			}
			
			novaDivida = new Divida();
			novaDivida.setValor(vlMovFinanTotal.abs());
			novaDivida.setData(consolidadoFinanceiroCota.getDataConsolidado());
			novaDivida.setConsolidado(consolidadoFinanceiroCota);
			novaDivida.setCota(cota);
			novaDivida.setStatus(StatusDivida.EM_ABERTO);
			novaDivida.setResponsavel(usuario);
			
			BigDecimal valorCalculadoJuros = BigDecimal.ZERO;
			
			//se o distribuidor acumula divida
			if (acumulaDivida){
				Calendar diaDivida = Calendar.getInstance();
				diaDivida.setTime(new Date());
				diaDivida.add(Calendar.DAY_OF_MONTH, qtdDiasNovaCobranca * -1);
				
				Divida divida = this.dividaRepository.obterDividaParaAcumuloPorCota(cota.getId(), diaDivida.getTime());
				
				//caso não tenha divida anterior, ou tenha sido quitada
				if (divida == null || StatusDivida.QUITADA.equals(divida.getStatus())){
					divida = novaDivida;
				} else {
					
					ConsolidadoFinanceiroCota consolidadoDivida = divida.getConsolidado();
					
					BigDecimal valorMulta = BigDecimal.ZERO;
					
					if (consolidadoDivida != null){
						List<MovimentoFinanceiroCota> movimentoFinanceiroDivida = consolidadoDivida.getMovimentos();
						for (MovimentoFinanceiroCota m : movimentoFinanceiroDivida){
							if (((TipoMovimentoFinanceiro) m.getTipoMovimento()).getGrupoMovimentoFinaceiro().equals(GrupoMovimentoFinaceiro.MULTA)){
								valorMulta = m.getValor();
								break;
							}
						}
					}
					
					valorCalculadoJuros = 
							this.cobrancaService.calcularJuros(
									null,
									cota,
									vlMovFinanTotal.add(novaDivida.getValor()).subtract(valorMulta), 
									divida.getCobranca().getDataVencimento(),
									new Date());
					
					divida.setAcumulada(true);
					novaDivida.setDividaRaiz(divida);
					
					historicoAcumuloDivida = new HistoricoAcumuloDivida();
					historicoAcumuloDivida.setDataInclusao(new Date());
					historicoAcumuloDivida.setDivida(divida);
					historicoAcumuloDivida.setResponsavel(usuario);
					historicoAcumuloDivida.setStatus(StatusInadimplencia.ATIVA);
					
					novaDivida.setValor(valorCalculadoJuros.abs());
				}
			}
		} else if (vlMovFinanTotal.compareTo(valorMinino) != 0) {

			//gerar postergado
			consolidadoFinanceiroCota.setValorPostergado(vlMovFinanTotal);
			
			//gera movimento financeiro cota
			movimentoFinanceiroCota = new MovimentoFinanceiroCota();
			
			Calendar diaPostergado = Calendar.getInstance();
			diaPostergado.setTime(new Date());
			diaPostergado.add(Calendar.DAY_OF_MONTH, qtdDiasNovaCobranca);
			
			movimentoFinanceiroCota.setData(diaPostergado.getTime());
			movimentoFinanceiroCota.setDataCriacao(new Date());
			movimentoFinanceiroCota.setUsuario(usuario);
			movimentoFinanceiroCota.setValor(vlMovFinanTotal);
			movimentoFinanceiroCota.setLancamentoManual(false);
			movimentoFinanceiroCota.setCota(cota);
			
			if (vlMovFinanTotal.compareTo(BigDecimal.ZERO) > 0){
				
				tipoMovimentoFinanceiro = 
						this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
								GrupoMovimentoFinaceiro.POSTERGADO_CREDITO);
			} else {
				
				tipoMovimentoFinanceiro = 
						this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
								GrupoMovimentoFinaceiro.POSTERGADO_DEBITO);
			}
			
			if (tipoMovimentoFinanceiro == null) {
				
				msgs.add("Tipo de movimento para postergação não encontrado!");
				
				return null;
			}
			
			String descPostergado = null;
			
			if (diasSemanaConcentracaoPagamento != null && 
					!diasSemanaConcentracaoPagamento.contains(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))){
				
				descPostergado = "Não existe acúmulo de pagamento para este dia (" + 
						new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()) + ")";
			} else {
				
				descPostergado = "Valor mínimo para dívida não atingido";
			}
			
			movimentoFinanceiroCota.setMotivo(descPostergado);
			
			
			movimentoFinanceiroCota.setTipoMovimento(tipoMovimentoFinanceiro);
		}
		
		this.consolidadoFinanceiroRepository.adicionar(consolidadoFinanceiroCota);
		
		Cobranca cobranca = null;
		
		if (novaDivida != null){
			if (novaDivida.getId() == null){
				this.dividaRepository.adicionar(novaDivida);
			} else {
				this.dividaRepository.alterar(novaDivida);
			}
			
			if (historicoAcumuloDivida != null){
				this.historicoAcumuloDividaRepository.adicionar(historicoAcumuloDivida);
			}
			
			switch (tipoCobranca){
				case BOLETO:
					cobranca = new Boleto();
				break;
				case CHEQUE:
					cobranca = new CobrancaCheque();
				break;
				case DINHEIRO:
					cobranca = new CobrancaDinheiro();
				break;
				case DEPOSITO:
					cobranca = new CobrancaDeposito();
				case TRANSFERENCIA_BANCARIA:
					cobranca = new CobrancaTransferenciaBancaria();
				break;
				case BOLETO_EM_BRANCO:
					cobranca = new CobrancaBoletoEmBranco();
				break;
				case OUTROS:
					cobranca = new CobrancaOutros();
				break;
			}
			
			cobranca.setBanco(formaCobrancaPrincipal.getBanco());
			cobranca.setCota(cota);
			cobranca.setDataEmissao(new Date());
			cobranca.setDivida(novaDivida);
			cobranca.setStatusCobranca(StatusCobranca.NAO_PAGO);
			cobranca.setDataVencimento(dataVencimento);
			cobranca.setVias(0);
			
			Banco banco = formaCobrancaPrincipal.getBanco();
			
			String nossoNumero =
				Util.gerarNossoNumero(
					cota.getNumeroCota(), 
					cobranca.getDataEmissao(), 
					banco.getNumeroBanco(),
					fornecedor != null ? fornecedor.getId() : null,
					movimentos.get(0).getId(),
					banco.getAgencia(),
					banco.getConta(),
					banco.getCarteira());
			
			cobranca.setNossoNumero(nossoNumero);
			
			String digitoVerificador =
				Util.calcularDigitoVerificador(
					nossoNumero, banco.getCodigoCedente(), cobranca.getDataVencimento());
			
			cobranca.setDigitoNossoNumero(digitoVerificador);
			
			cobranca.setNossoNumeroCompleto(
				nossoNumero + ((digitoVerificador != null) ? digitoVerificador : ""));
			
			cobranca.setValor(novaDivida.getValor());
			
			this.cobrancaRepository.adicionar(cobranca);
			
			if (formaCobrancaPrincipal.isRecebeCobrancaEmail()){
				
				try {
					byte[]anexo = this.documentoCobrancaService.gerarDocumentoCobranca(cobranca.getNossoNumero());
					
					this.emailService.enviar(
							"Cobrança", 
							"Segue documento de cobrança em anexo.", 
							new String[]{cota.getPessoa().getEmail()}, 
							new AnexoEmail("Cobranca",anexo,TipoAnexo.PDF));
					
					this.cobrancaRepository.incrementarVia(cobranca.getNossoNumero());
				} catch (AutenticacaoEmailException e) {
					
					msgs.add("Erro ao enviar email de cobrança para cota número: " + cota.getNumeroCota());
				} catch (ValidacaoException e) {
					
					msgs.add("Erro ao enviar email de cobrança para cota número: " + cota.getNumeroCota() + " - " + e.getValidacao().getListaMensagens().get(0));
				}
			}
		}
		
		if (movimentoFinanceiroCota != null){
			
			this.movimentoFinanceiroCotaRepository.adicionar(movimentoFinanceiroCota);
		}
		
		if (cobranca != null){
			
			return cobranca.getNossoNumero();
		}
		
		return null;
	}
	
	@Transactional(readOnly=true)
	@Override
	public Boolean validarDividaGeradaDataOperacao() {
		
		Distribuidor distribuidor = distribuidorRepository.obter();
		
		Long quantidadeRegistro = movimentoFinanceiroCotaRepository.obterQuantidadeMovimentoFinanceiroDataOperacao(distribuidor.getDataOperacao()); 
		
		return (quantidadeRegistro == null || quantidadeRegistro == 0) ? Boolean.FALSE : Boolean.TRUE;
	}

	@Transactional
	@Override
	public void cancelarDividaCobranca(Set<Long> idsMovimentoFinanceiroCota) {
		
		if (idsMovimentoFinanceiroCota != null && !idsMovimentoFinanceiroCota.isEmpty()){
			
			for (Long idMovFinCota : idsMovimentoFinanceiroCota){
				
				this.cancelarDividaCobranca(idMovFinCota, null);
			}
		}
	}
	
	@Transactional
	@Override
	public void cancelarDividaCobranca(Long idMovimentoFinanceiroCota, Long idCota) {
		
		List<ConsolidadoFinanceiroCota> consolidados = null;
		
		if (idMovimentoFinanceiroCota != null){

			consolidados = 
					this.consolidadoFinanceiroRepository.obterConsolidadoPorIdMovimentoFinanceiro(
							idMovimentoFinanceiroCota);
		} else {
			
			consolidados =
					this.consolidadoFinanceiroRepository.obterConsolidadosDataOperacao(idCota);
		}
		
		if (consolidados != null){
			
			for (ConsolidadoFinanceiroCota consolidado : consolidados){
				
				//a cobrança (divida/cobranca/consolidado) não pode ser apagada caso pertença a uma negociação
				Divida divida = this.dividaRepository.obterDividaPorIdConsolidado(consolidado.getId());
				
				if (divida != null){
				
					this.cobrancaControleConferenciaEncalheCotaRepository.excluirPorCobranca(
							divida.getCobranca().getId());
					this.cobrancaRepository.remover(divida.getCobranca());
					this.dividaRepository.remover(divida);
					
					this.consolidadoFinanceiroRepository.remover(consolidado);
					
					List<TipoMovimentoFinanceiro> listaPostergados = Arrays.asList(
						this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
								GrupoMovimentoFinaceiro.POSTERGADO_CREDITO),
								
						this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
								GrupoMovimentoFinaceiro.POSTERGADO_DEBITO)
					);
					
					this.movimentoFinanceiroCotaService.removerPostergadosDia(
							consolidado.getCota().getId(), 
							listaPostergados);
				}
			}
		}
	}
	
	/**
	 * 
	 * Retorna o valor de cobrança em aberto que a cota não pagou até a presente data de geração do novo consolidado em questão.
	 * 
	 * @param numeroCota - número da cota
	 * 
	 * @return BigDecimal - valor pendente de cobrança do sonsolidado
	 */
	private BigDecimal obterValorPendenteCobrancaConsolidado(Integer numeroCota){
		
		return cobrancaRepository.obterValorCobrancaNaoPagoDaCota(numeroCota);
	}
}