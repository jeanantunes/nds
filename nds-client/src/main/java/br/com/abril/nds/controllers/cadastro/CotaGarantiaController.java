package br.com.abril.nds.controllers.cadastro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.DebitoCreditoDTO;
import br.com.abril.nds.dto.FormaCobrancaCaucaoLiquidaDTO;
import br.com.abril.nds.dto.ImovelDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.NotaPromissoriaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.CaucaoLiquida;
import br.com.abril.nds.model.cadastro.Cheque;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.GarantiaCotaOutros;
import br.com.abril.nds.model.cadastro.Imovel;
import br.com.abril.nds.model.cadastro.NotaPromissoria;
import br.com.abril.nds.model.cadastro.TipoCobrancaCotaGarantia;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.service.CotaGarantiaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DebitoCreditoCotaService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.TipoMovimentoFinanceiroService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.ByteArrayDownload;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/garantia")
public class CotaGarantiaController extends BaseController {

	@Autowired
	private CotaGarantiaService cotaGarantiaService;
	
	@Autowired
	private DebitoCreditoCotaService debitoCreditoCotaService;
	
	@Autowired
	private TipoMovimentoFinanceiroService tipoMovimentoFinanceiroService;
	
	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private BancoService bancoService;
	
	@Autowired
	private Result result;
	
    public CotaGarantiaController() {
    	
    	super();
	}

    @Post
	@Path("/carregarBancos")
	public void carregarBancos(){
    	
		result.use(Results.json()).from(bancoService.getComboBancos(true), "result").recursive().serialize();
	}
    
	@Post
	@Path("/salvaNotaPromissoria.json")
	public void salvaNotaPromissoria(NotaPromissoria notaPromissoria,
			Long idCota) throws Exception {

		validaNotaPromissoria(notaPromissoria);
		cotaGarantiaService.salvaNotaPromissoria(notaPromissoria, idCota);

		result.use(Results.json())
				.from(new ValidacaoVO(TipoMensagem.SUCCESS,
						"Nota Promissoria salva com Sucesso."), "result")
				.recursive().serialize();
	}

	@Post("/salvaChequeCaucao.json")
	public void salvaChequeCaucao(Cheque chequeCaucao, Long idCota) throws Exception  {

		validaChequeCaucao(chequeCaucao);

		cotaGarantiaService.salvaChequeCaucao(chequeCaucao, idCota);

		result.use(Results.json())
				.from(new ValidacaoVO(TipoMensagem.SUCCESS,
						"Cheque Caução salvo com Sucesso."), "result")
				.recursive().serialize();
	}

	@Post("/salvaImovel.json")
	public void salvaImovel(List<Imovel> listaImoveis, Long idCota) throws Exception  {

		cotaGarantiaService.salvaImovel(listaImoveis, idCota);

		result.use(Results.json())
				.from(new ValidacaoVO(TipoMensagem.SUCCESS,
						"Imóveis salvos com Sucesso."), "result").recursive()
				.serialize();
	}

	@Post("/salvaOutros.json")
	public void salvaOutros(List<GarantiaCotaOutros> listaOutros, Long idCota) throws Exception  {

		cotaGarantiaService.salvaOutros(listaOutros, idCota);

		result.use(Results.json())
				.from(new ValidacaoVO(TipoMensagem.SUCCESS,
						"Outras garantias salvas com Sucesso."), "result").recursive()
				.serialize();
	}
	
	/**
	 * Lança Débitos e Créditos no lançamento ou Resgate de Caução Líquida
	 * @param valorParcela
	 * @param qtdParcelas
	 * @param idCota
	 * @param grupoFinanceiro
	 * @param operacaoFinaceira
	 */
	private void lancarDebitoCreditoCaucaoLiquida(BigDecimal valorParcela, 
			                                      Integer qtdParcelas, 
			                                      Long idCota, 
			                                      GrupoMovimentoFinaceiro grupoFinanceiro, 
			                                      OperacaoFinaceira operacaoFinaceira){
		
		Cota cota = cotaService.obterPorId(idCota);
		
		for (int i = 1; i<=qtdParcelas; i++){
			
			DebitoCreditoDTO debitoCreditoDTO = new DebitoCreditoDTO();
			
			Date dataAtual = Calendar.getInstance().getTime();
			
			debitoCreditoDTO.setDataLancamento(DateUtil.formatarDataPTBR(dataAtual));
			
			debitoCreditoDTO.setDataVencimento(DateUtil.formatarDataPTBR(DateUtil.adicionarDias(dataAtual, i)));
			
			debitoCreditoDTO.setValor(String.valueOf(valorParcela.floatValue()));

			TipoMovimentoFinanceiro tipoMovimento = this.tipoMovimentoFinanceiroService.obterTipoMovimentoFincanceiroPorGrupoFinanceiroEOperacaoFinanceira(grupoFinanceiro, operacaoFinaceira);
			
			debitoCreditoDTO.setTipoMovimentoFinanceiro(tipoMovimento);

			debitoCreditoDTO.setPermiteAlteracao(false);
			
			debitoCreditoDTO.setObservacao("Caucao Liquida");
			
			debitoCreditoDTO.setNomeCota(cota.getPessoa().getNome());
			
			debitoCreditoDTO.setNumeroCota(cota.getNumeroCota());
			
			debitoCreditoDTO.setIdUsuario(usuarioService.getUsuarioLogado().getId());
			
			MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO = this.debitoCreditoCotaService.gerarMovimentoFinanceiroCotaDTO(debitoCreditoDTO);
			
            movimentoFinanceiroCotaDTO.setTipoEdicao(TipoEdicao.INCLUSAO);
			
			this.movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(movimentoFinanceiroCotaDTO);
		}
	}
	
	/**
	 * Salva CaucaoLiquida
	 * @param listaCaucaoLiquida
	 * @param idCota
	 * @param formaCobranca
	 * @throws Exception
	 */
	@Post("/salvaCaucaoLiquida.json")
	public void salvaCaucaoLiquida(List<CaucaoLiquida> listaCaucaoLiquida, Long idCota, FormaCobrancaCaucaoLiquidaDTO formaCobranca) throws Exception {
		
		if(idCota==null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota não informada.");
		}
		
		if(formaCobranca.getTipoCobranca()==null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Escolha uma Forma de Pagamento.");
		}
		
		if (listaCaucaoLiquida == null){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,"Nenhum valor informado."));
		}	
		
		for(CaucaoLiquida caucaoLiquida: listaCaucaoLiquida){			
		    caucaoLiquida.setAtualizacao(Calendar.getInstance().getTime());
			validaCaucaoLiquida(caucaoLiquida);
		}
		
		validarFormaCobranca(formaCobranca);
		
		formaCobranca = formatarFormaCobranca(formaCobranca);
		
		cotaGarantiaService.salvarCaucaoLiquida(listaCaucaoLiquida, idCota, formaCobranca);

		this.lancarDebitoCreditoCaucaoLiquida(formaCobranca.getValorParcela(), 
				                              formaCobranca.getQtdeParcelas(), 
				                              idCota,GrupoMovimentoFinaceiro.LANCAMENTO_CAUCAO_LIQUIDA, 
				                              OperacaoFinaceira.DEBITO);

		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS,"Caução Líquida salva com Sucesso."), "result").recursive().serialize();
	}

	/**
	 * Resgata valor CaucaoLiquida
	 * @param valor
	 * @param idCota
	 * @throws Exception
	 */
	@Post("/resgataCaucaoLiquida.json")
	public void resgataCaucaoLiquida(BigDecimal valor, Long idCota) throws Exception {
		
		if(idCota==null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota não informada.");
		}

		List<CaucaoLiquida> caucaoLiquidasCota = cotaGarantiaService.obterCaucaoLiquidasCota(idCota);
		
		int indexCaucaoLiquidaAtual = caucaoLiquidasCota.size() - 1; 
		
		CaucaoLiquida caucaoLiquidaAtual = caucaoLiquidasCota.get(indexCaucaoLiquidaAtual);
		
		BigDecimal valorAtual = caucaoLiquidaAtual.getValor();
		
        if(valorAtual.compareTo(BigDecimal.ZERO) == 0){
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,"Não há resgate à ser feito !"));
		}
		
		if(valorAtual.compareTo(valor) < 0){
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,"Valor do resgate ultrapassa o valor do Caução Líquida !"));
		}
		
		BigDecimal novoValor = valorAtual.subtract(valor);
		
		CaucaoLiquida novaCaucaoLiquida = new CaucaoLiquida();
		novaCaucaoLiquida.setAtualizacao(new Date());
		novaCaucaoLiquida.setValor(novoValor);
		
		this.validaCaucaoLiquida(novaCaucaoLiquida);
		
		caucaoLiquidasCota.add(novaCaucaoLiquida);
		
		FormaCobrancaCaucaoLiquidaDTO formaCobrancaDTO = cotaGarantiaService.obterDadosCaucaoLiquida(idCota);
		
		formaCobrancaDTO.setIdCaucaoLiquida(null);
		
		formaCobrancaDTO.setIdFormaCobrancaCaucaoLiquida(null);
		
		formaCobrancaDTO.setValor(novoValor);
		
		if (novoValor.compareTo(BigDecimal.ZERO) == 0){
		
			formaCobrancaDTO.setQtdeParcelas(0);
		
		    formaCobrancaDTO.setValorParcela(BigDecimal.ZERO);
		}

		cotaGarantiaService.salvarCaucaoLiquida(Arrays.asList(novaCaucaoLiquida), idCota, formaCobrancaDTO);

		this.lancarDebitoCreditoCaucaoLiquida(valor, 
				                              1, 
				                              idCota, 
                                              GrupoMovimentoFinaceiro.RESGATE_CAUCAO_LIQUIDA, 
                                              OperacaoFinaceira.CREDITO);

		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS,"Valor de "+CurrencyUtil.formatarValorComSimbolo(valor)+" resgatado com Sucesso."), "result").recursive().serialize();
	}
	
	@SuppressWarnings("unchecked")
	@Post("/getByCota.json")
	public void getByCota(Long idCota, ModoTela modoTela, Long idHistorico) {
		
		
		this.result.use(Results.json()).from("OK").serialize();
		
		
//	    if (ModoTela.CADASTRO_COTA == modoTela) {
//
//	        CotaGarantiaDTO<CotaGarantia> cotaGarantia = cotaGarantiaService.getByCota(idCota);
//	        
//	        if (cotaGarantia != null && cotaGarantia.getCotaGarantia() != null) {	
//	        	
//	            this.result.use(PlainJSONSerialization.class).from(cotaGarantia, "result").serialize();
//	        
//	        } else {
//	        	
//	        	this.result.use(Results.json()).from("OK").serialize();
//	        }	
//	    } 
//	    else {
//	    	
//	        CotaGarantiaDTO<?> cotaGarantia = cotaGarantiaService.obterGarantiaHistoricoTitularidadeCota(idCota, idHistorico);
//	        
//	        if (cotaGarantia != null) {
//	        	
//	        	if(cotaGarantia.getCotaGarantia().getClass().getName().equals("br.com.abril.nds.dto.FiadorDTO")) {
//	        		CotaGarantiaDTO<FiadorDTO> cotaGarantiaFiadorDTO = (CotaGarantiaDTO<FiadorDTO>) cotaGarantia;
//	        		
//	        		if( cotaGarantiaFiadorDTO.getCotaGarantia().getEnderecoPrincipal().getLogradouro() == null &&
//	        				cotaGarantiaFiadorDTO.getCotaGarantia().getEnderecoPrincipal().getNumero() == null) {
//	        			this.result.use(Results.json()).from("OK").serialize();
//	        		} else {
//		        		this.result.use(PlainJSONSerialization.class).from(cotaGarantia, "result").serialize();
//		        	}
//	        		
//	        	} 
//	        	
//	        	
//	        	
//	        	
//	        } else {
//	        	
//	        	this.result.use(Results.json()).from("OK").serialize();      
//	        }
//	    }
	}
	
	/**
	 * Obtem Cota garantia do tipo Caução Liquida
	 * @param idCota
	 */
	@Post("/getCaucaoLiquidaByCota.json")
	public void getCaucaoLiquidaByCota(Long idCota, ModoTela modoTela, Long idHistorico) {

        if (ModoTela.CADASTRO_COTA == modoTela) {
        	
            FormaCobrancaCaucaoLiquidaDTO dadosCaucaoLiquida = cotaGarantiaService.obterDadosCaucaoLiquida(idCota);
            
            if (dadosCaucaoLiquida != null) {
            	
            	this.result.use(CustomJson.class).from(dadosCaucaoLiquida).serialize();
            } else {
            	
            	this.result.use(CustomJson.class).from("OK").serialize();
            }

        } else {
        	
            FormaCobrancaCaucaoLiquidaDTO dto = cotaGarantiaService.obterCaucaoLiquidaHistoricoTitularidadeCota(idCota, idHistorico);
            
            this.result.use(CustomJson.class).from(dto).serialize();
        }
	}
	
	@Post("/getDescontoAtualCaucaoLiquida.json")
	public void getDescontoAtualCaucaoLiquida(Long idCota) {
		
		BigDecimal descontoAtual = this.cotaGarantiaService.obterValorComissaoCaucaoLiquida(idCota);
		
		this.result.use(CustomJson.class).from(descontoAtual).serialize();
	}
	
	/**
	 * Obtem Cota garantia do tipo Fiador
	 * @param idCota
	 */
	@Post("/getFiadorByCota.json")
	public void getFiadorByCota(Long idCota) {
	
    	Cota cota = cotaService.obterPorId(idCota);
        
    	Fiador fiador = cota.getFiador();	

        result.use(CustomJson.class).from(fiador).serialize();
	}

	/**
	 * Obtem Cota garantia do tipo Imóvel
	 * @param idCota
	 */
	@Post("/getImoveisGarantiaByCota.json")
	public void getImoveisGarantiaByCota(Long idCota, ModoTela modoTela) {

        if (ModoTela.CADASTRO_COTA == modoTela) {
        	
            List<ImovelDTO> dadosImoveis = cotaGarantiaService.obterDadosImoveisDTO(idCota);
            
            if (dadosImoveis != null) {

            	result.use(Results.json()).withoutRoot().from(dadosImoveis).recursive().serialize();
            } else {
            	
                result.use(CustomJson.class).from("OK").serialize();
            }
        }    
	}
	
	/**
	 * Obtem Cota garantia do tipo Cheque Caucao
	 * @param idCota
	 */
	@Post("/getChequeCaucaoByCota.json")
	public void getChequeCaucaoByCota(Long idCota, ModoTela modoTela) {

        if (ModoTela.CADASTRO_COTA == modoTela) {
            
            Cheque dadosChequeCaucao= cotaGarantiaService.obterDadosChequeCaucao(idCota);
            
            if (dadosChequeCaucao != null) {

            	result.use(Results.json()).from(dadosChequeCaucao,"cheque").serialize();
            } else {
            	
                result.use(CustomJson.class).from("OK").serialize();
            }
        }    
	}
	
	/**
	 * Obtem Cota garantia do tipo Nota Promissória
	 * @param idCota
	 */
	@Post("/getNotaPromissoriaByCota.json")
	public void getNotaPromissoriaByCota(Long idCota, ModoTela modoTela) {
    	
		if (ModoTela.CADASTRO_COTA == modoTela || ModoTela.HISTORICO_TITULARIDADE == modoTela) {
            
            NotaPromissoria dadosNotaPromissoria= cotaGarantiaService.obterDadosNotaPromissoria(idCota);
            
            if (dadosNotaPromissoria != null) {

            	result.use(Results.json()).from(dadosNotaPromissoria,"notaPromissoria").serialize();
            } else {
            	
                result.use(CustomJson.class).from("OK").serialize();
            }
        } else {
        	result.use(CustomJson.class).from("OK").serialize();
        }		
		
	}

	/**
	 * Obtem Cota garantia do tipo Imóvel
	 * @param idCota
	 */
	@Post("/getGarantiaOutrosByCota.json")
	public void getGarantiaOutrosByCota(Long idCota, ModoTela modoTela) {

        if (ModoTela.CADASTRO_COTA == modoTela) {
        	
            List<GarantiaCotaOutros> dadosOutros = cotaGarantiaService.obterDadosGarantiaOutrosDTO(idCota);
            
            if (dadosOutros != null) {

            	this.result.use(Results.json()).from(dadosOutros, "data").serialize();
            	
            } else {
            	
            	this.result.use(CustomJson.class).from("OK").serialize();
            }
        }    
	}
	
	/**
	 * Obtem Cota garantia do tipo Imóvel
	 * @param idCota
	 */
	@Post("/getCotaGarantiaByCota.json")
	public void getCotaGarantiaByCota(Long idCota) {
        	
        CotaGarantia cg = cotaService.obterPorId(idCota).getCotaGarantia();
        
        if (cg != null) {

        	result.use(Results.json()).from(cg, "cotaGarantia").serialize();
        } else {
        	
            result.use(CustomJson.class).from("OK").serialize();
        }    
	}

	@Post("/getTipoGarantiaCadastrada.json")
	public void getTipoGarantiaCadastrada(Long idCota){
		
	}

	@Post("/validarDadosCotaPreImpressao.json")
	public void validarDadosCotaPreImpressao(Long idCota){
		
		List<String> msgs = this.cotaGarantiaService.validarDadosCotaPreImpressao(idCota);
		
		if (msgs != null && !msgs.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, msgs);
		}
		
		result.use(Results.json()).from("OK").serialize();
	}
	
	@Post("/verificarValorCaucaoLiquida")
	public void verificarValorCaucaoLiquida(Long idCota){
		
		boolean existeCL= this.cotaGarantiaService.existeCaucaoLiquidasCota(idCota);
		
		if (existeCL){
			
			throw new ValidacaoException(TipoMensagem.WARNING, 
				"É necessário resgatar a caução líquida antes de alterar o tipo de garantia.");
		}
		
		result.use(Results.json()).from("OK").serialize();
	}
	
	@Get("/impriNotaPromissoria/{id}")
	public void impriNotaPromissoria(Long id) {
		NotaPromissoriaDTO nota = cotaGarantiaService.getDadosImpressaoNotaPromissoria(id);
		
		result.include("nota",nota);
	}

	@Get("/getTiposGarantia.json")
	public void getTiposGarantia() {
		List<TipoGarantia> cotaGarantias = cotaGarantiaService
				.obtemTiposGarantiasAceitas();
		result.use(Results.json()).withoutRoot().from(cotaGarantias)
				.recursive().serialize();
	}

	/**
	 * Método responsável por obter tipos de cobrança de Garantia para preencher combo da camada view
	 * @return comboTiposPagamento: Tipos de cobrança de Garantia padrão.
	 */
	@Get("/getTiposCobrancaCotaGarantia.json")
	public void getTiposCobrancaCotaGarantia() {
		
		List<ItemDTO<TipoCobrancaCotaGarantia,String>> listaTiposCobranca =  new ArrayList<ItemDTO<TipoCobrancaCotaGarantia,String>>();
		
		for (TipoCobrancaCotaGarantia itemTipoCobranca: TipoCobrancaCotaGarantia.values()){
			
			listaTiposCobranca.add(new ItemDTO<TipoCobrancaCotaGarantia,String>(itemTipoCobranca, itemTipoCobranca.getDescTipoCobranca()));
		}
		
		result.use(Results.json()).withoutRoot().from(listaTiposCobranca).recursive().serialize();
	}

	@Post("/buscaFiador.json")
	public void buscaFiador(String nome, int maxResults) {
		List<ItemDTO<Long, String>> listFiador = cotaGarantiaService
				.buscaFiador(nome, maxResults);
		result.use(Results.json()).from(listFiador, "items").recursive()
				.serialize();
	}

	@Post("/incluirImovel.json")
	public void incluirImovel(Imovel imovel) {
		validaImovel(imovel);
						
		result.use(Results.json()).from(imovel, "imovel").serialize();
	}

	@Post("/incluirOutro.json")
	public void incluirOutro(GarantiaCotaOutros garantiaCotaOutros) {
		
		validaGarantiaCotaOutros(garantiaCotaOutros);
						
		result.use(Results.json()).from(garantiaCotaOutros, "outro").serialize();
	}

	/**
	 * @param caucaoLiquida para ser validado
	 */
	private void validaCaucaoLiquida(CaucaoLiquida caucaoLiquida) {
				
		if (caucaoLiquida.getValor() == null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,
					"Parametros inválidos"));
		}
		
		if (caucaoLiquida.getAtualizacao() == null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,
					"Parametros inválidos"));
		}
	}
	
	/**
	 * Método responsável pela validação dos dados da Forma de Cobranca.
	 * @param formaCobranca
	 */
	public void validarFormaCobranca(FormaCobrancaCaucaoLiquidaDTO formaCobranca){
		
		if (formaCobranca.getTipoCobranca() == TipoCobrancaCotaGarantia.BOLETO){
			
			if (formaCobranca.getTipoFormaCobranca()==null){
				throw new ValidacaoException(TipoMensagem.WARNING, "Selecione um tipo de concentração de Pagamentos.");
			}
			
	        if (formaCobranca.getValorParcela() == null){
				throw new ValidacaoException(TipoMensagem.WARNING, "Informe o valor da parcela.");
			}
	
			if (formaCobranca.getQtdeParcelas() == null){
		        throw new ValidacaoException(TipoMensagem.WARNING, "Informe a quantidade de parcelas.");
			}
		
			if(formaCobranca.getTipoFormaCobranca()==TipoFormaCobranca.MENSAL){
				if (formaCobranca.getDiaDoMes()==null){
					throw new ValidacaoException(TipoMensagem.WARNING, "Para o tipo de cobrança Mensal é necessário informar o dia do mês.");
				}
				else{
					if ((formaCobranca.getDiaDoMes()>31)||(formaCobranca.getDiaDoMes()<1)){
						throw new ValidacaoException(TipoMensagem.WARNING, "Dia do mês inválido.");
					}
				}
				
			}
			
			if(formaCobranca.getTipoFormaCobranca()==TipoFormaCobranca.QUINZENAL){
				if ((formaCobranca.getPrimeiroDiaQuinzenal()==null) || (formaCobranca.getSegundoDiaQuinzenal()==null)){
					throw new ValidacaoException(TipoMensagem.WARNING, "Para o tipo de cobrança Quinzenal é necessário informar dois dias do mês.");
				}
				else{
					if ((formaCobranca.getPrimeiroDiaQuinzenal()>31)||(formaCobranca.getPrimeiroDiaQuinzenal()<1)||(formaCobranca.getSegundoDiaQuinzenal()>31)||(formaCobranca.getSegundoDiaQuinzenal()<1)){
						throw new ValidacaoException(TipoMensagem.WARNING, "Dia do mês inválido.");
					}
				}
			}
			
			if(formaCobranca.getTipoFormaCobranca()==TipoFormaCobranca.SEMANAL){
				if((!formaCobranca.isDomingo())&&
				   (!formaCobranca.isSegunda())&&
				   (!formaCobranca.isTerca())&&
				   (!formaCobranca.isQuarta())&&
				   (!formaCobranca.isQuinta())&&
				   (!formaCobranca.isSexta())&&
				   (!formaCobranca.isSabado())){
					throw new ValidacaoException(TipoMensagem.WARNING, "Para o tipo de cobrança Semanal é necessário marcar ao menos um dia da semana.");      	
				}
			}
		}
		
		if (formaCobranca.getValor() == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Informe o valor.");
		}
	}
	
	/**
	 *Formata os dados de FormaCobranca, apagando valores que não são compatíveis com o Tipo de Cobranca escolhido.
	 * @param formaCobranca
	 */
	private FormaCobrancaCaucaoLiquidaDTO formatarFormaCobranca(FormaCobrancaCaucaoLiquidaDTO formaCobranca){
		
		if (formaCobranca.getTipoFormaCobranca()==TipoFormaCobranca.SEMANAL){
			formaCobranca.setDiaDoMes(null);
			formaCobranca.setPrimeiroDiaQuinzenal(null);
			formaCobranca.setSegundoDiaQuinzenal(null);
		}
		
		if (formaCobranca.getTipoFormaCobranca()==TipoFormaCobranca.MENSAL){
			formaCobranca.setDomingo(false);
			formaCobranca.setSegunda(false);
			formaCobranca.setTerca(false);
			formaCobranca.setQuarta(false);
			formaCobranca.setQuinta(false);
			formaCobranca.setSexta(false);
			formaCobranca.setSabado(false);
			formaCobranca.setPrimeiroDiaQuinzenal(null);
			formaCobranca.setSegundoDiaQuinzenal(null);
		}
		
		if (formaCobranca.getTipoFormaCobranca()==TipoFormaCobranca.DIARIA){
			formaCobranca.setDomingo(false);
			formaCobranca.setSegunda(false);
			formaCobranca.setTerca(false);
			formaCobranca.setQuarta(false);
			formaCobranca.setQuinta(false);
			formaCobranca.setSexta(false);
			formaCobranca.setSabado(false);
			formaCobranca.setDiaDoMes(null);
			formaCobranca.setPrimeiroDiaQuinzenal(null);
			formaCobranca.setSegundoDiaQuinzenal(null);
		}
		
		if (formaCobranca.getTipoFormaCobranca()==TipoFormaCobranca.QUINZENAL){
			formaCobranca.setDomingo(false);
			formaCobranca.setSegunda(false);
			formaCobranca.setTerca(false);
			formaCobranca.setQuarta(false);
			formaCobranca.setQuinta(false);
			formaCobranca.setSexta(false);
			formaCobranca.setSabado(false);
			formaCobranca.setDiaDoMes(null);
		}
		
		return formaCobranca;
	}
	
	/**
	 * @param notaPromissoria para ser validado
	 */
	private void validaNotaPromissoria(NotaPromissoria notaPromissoria) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		if (notaPromissoria.getValor() == null || notaPromissoria.getValor().doubleValue() <= 0) {
			listaMensagens.add("O preenchimento do campo [Valor R$] é obrigatório");
		}
		
		if (StringUtil.isEmpty(notaPromissoria.getValorExtenso())) {
			listaMensagens
					.add("O preenchimento do campo [Valor Extenso] é obrigatório");
		}
		
		if (notaPromissoria.getVencimento() == null) {
			listaMensagens
					.add("O preenchimento do campo [Vencimento] é obrigatório");
		}else if(notaPromissoria.getVencimento().compareTo(new Date()) <= 0  ) {
			listaMensagens
			.add("O campo [Vencimento] deve ser uma data no futuro.");
		}
		
		if (!listaMensagens.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,
					listaMensagens));
		}
	}

	/**
	 * @param cheque para ser validado
	 */
	private void validaChequeCaucao(Cheque cheque) {

		List<String> listaMensagens = new ArrayList<String>();

		if (StringUtil.isEmpty(cheque.getNumeroBanco())) {
			listaMensagens
					.add("O preenchimento do campo [Num. Banco] é obrigatório");
		}

		if (StringUtil.isEmpty(cheque.getNomeBanco())) {
			listaMensagens.add("O preenchimento do campo [Nome] é obrigatório");
		}

		if (cheque.getAgencia() == null
				|| StringUtil.isEmpty(cheque.getDvAgencia())) {
			listaMensagens
					.add("O preenchimento do campo [Agência] é obrigatório");
		}

		if (cheque.getConta() == null
				|| StringUtil.isEmpty(cheque.getDvConta())) {
			listaMensagens
					.add("O preenchimento do campo [Conta] é obrigatório");
		}

		if (StringUtil.isEmpty(cheque.getNumeroCheque())) {
			listaMensagens
					.add("O preenchimento do campo [Nº Cheque] é obrigatório");
		}

		if (cheque.getValor() == null) {
			listaMensagens
					.add("O preenchimento do campo [Valor R$] é obrigatório");
		}

		if (cheque.getEmissao() == null) {
			listaMensagens
					.add("O preenchimento do campo [Data do Cheque] é obrigatório");
		}

		if (cheque.getValidade() == null) {
			listaMensagens
					.add("O preenchimento do campo [Validade] é obrigatório");
		}

		if (StringUtil.isEmpty(cheque.getCorrentista())) {
			listaMensagens
					.add("O preenchimento do campo [Nome Correntista] é obrigatório");
		}

		if (!listaMensagens.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,
					listaMensagens));
		}
	}

	/**
	 * @param outra garantia para ser validada.
	 */
	private void validaGarantiaCotaOutros(GarantiaCotaOutros garantiaCotaOutros) {

		List<String> listaMensagens = new ArrayList<String>();

		if (StringUtil.isEmpty(garantiaCotaOutros.getDescricao())) {
			listaMensagens
					.add("O preenchimento do campo [Descrição] é obrigatório");
		}

		if (garantiaCotaOutros.getValor() == null) {
			listaMensagens
					.add("O preenchimento do campo [Valor] é obrigatório");
		}

		if (garantiaCotaOutros.getValidade() == null) {
			listaMensagens
					.add("O preenchimento do campo [Validade] é obrigatório");
		}

		if (!listaMensagens.isEmpty()) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, listaMensagens));
			
		}
	}

	/**
	 * @param imóvel para ser validado.
	 */
	private void validaImovel(Imovel imovel) {

		List<String> listaMensagens = new ArrayList<String>();

		if (StringUtil.isEmpty(imovel.getProprietario())) {
			listaMensagens
					.add("O preenchimento do campo [Proprietário] é obrigatório");
		}

		if (StringUtil.isEmpty(imovel.getEndereco())) {
			listaMensagens
					.add("O preenchimento do campo [Endereço] é obrigatório");
		}

		if (StringUtil.isEmpty(imovel.getNumeroRegistro())) {
			listaMensagens
					.add("O preenchimento do campo [Número Registro] é obrigatório");
		}

		if (imovel.getValor() == null) {
			listaMensagens
					.add("O preenchimento do campo [Valor R$] é obrigatório");
		}

		if (StringUtil.isEmpty(imovel.getObservacao())) {
			listaMensagens
					.add("O preenchimento do campo [Observação] é obrigatório");
		}

		if (!listaMensagens.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,
					listaMensagens));
		}
	}
	
    @Post("/getFiador.json")
    public void getFiador(Long idFiador, String documento) {
        Fiador fiador = cotaGarantiaService.getFiador(idFiador, documento);
        if (fiador != null) {
            result.use(Results.json()).from(fiador).include(
            	"pessoa", "enderecoFiador", "enderecoFiador.endereco", "telefonesFiador",
            	"telefonesFiador.telefone", "garantias").serialize();
        } else {
            result.use(CustomJson.class).from("NotFound").serialize();
        }
    }

	@Post("/salvaFiador.json")
	public void getFiador(Long idFiador, Long idCota)throws Exception  {
		cotaGarantiaService.salvaFiador(idFiador, idCota);
		result.use(Results.json())
				.from(new ValidacaoVO(TipoMensagem.SUCCESS,
						"Fiador salvo com Sucesso."), "result").recursive()
				.serialize();
	}
	
	public Download getImageCheque(long idCheque, ModoTela modoTela, Long idCota, Long idHistorico) {		
	    byte[] buff = new byte[0];
	    if (ModoTela.CADASTRO_COTA == modoTela) {
	        buff = cotaGarantiaService.getImageCheque(idCheque);
	    } else {
	        buff = cotaGarantiaService.getImagemChequeCaucaoHistoricoTitularidade(idCota, idHistorico);
	    }
	    
	    if (buff == null) {
	        buff = new byte[0];
	    }
		return new ByteArrayDownload(buff, "image/jpeg", "cheque.jpg");
	}
	
	public void uploadCheque(Long idCheque, UploadedFile image) throws Exception{		
		
		if(idCheque != null){
			byte[] bytes = IOUtils.toByteArray(image.getFile());
			cotaGarantiaService.salvaChequeImage(idCheque, bytes);
			
			result.use(PlainJSONSerialization.class)
			.from(new ValidacaoVO(TipoMensagem.SUCCESS,
					"Upload feito com Sucesso."), "result").recursive()
			.serialize();
		}else{
			result.use(PlainJSONSerialization.class)
			.from(new ValidacaoVO(TipoMensagem.ERROR,
					"Cheque deve estar salvo."), "result").recursive()
			.serialize();
		}
		
	}
	
}