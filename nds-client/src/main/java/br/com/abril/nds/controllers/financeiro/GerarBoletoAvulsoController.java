package br.com.abril.nds.controllers.financeiro;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.log.LogFuncional;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.BoletoAvulsoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.MixCotaDTO;
import br.com.abril.nds.dto.MixCotaProdutoDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.dto.RegiaoDTO;
import br.com.abril.nds.dto.filtro.FiltroBoletoAvulsoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.DebitoCreditoCotaService;
import br.com.abril.nds.service.DocumentoCobrancaService;
import br.com.abril.nds.service.FormaCobrancaService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.RegiaoService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.service.TipoMovimentoFinanceiroService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.upload.XlsUploaderUtils;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/financeiro/boletoAvulso")
@Rules(Permissao.ROLE_FINANCEIRO_GERAR_BOLETO_AVULSO)
public class GerarBoletoAvulsoController extends BaseController {

	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private Result result;
	
	@Autowired
	private RegiaoService regiaoService;
	
	@Autowired
    private RoteirizacaoService roteirizacaoService;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private DistribuidorService distribuidorService;

	@Autowired
	private DebitoCreditoCotaService debitoCreditoCotaService;
	
	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
	
	@Autowired
	private GerarCobrancaService gerarCobrancaService;
	
	@Autowired
	private DocumentoCobrancaService documentoCobrancaService;
	
	@Autowired
	private BancoService bancoService;
	
	@Autowired
	private TipoMovimentoFinanceiroService tipoMovimentoFinanceiroService;
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Autowired
	private FormaCobrancaService formaCobrancaService; 
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GerarBoletoAvulsoController.class);

	@Path("/")
    @Rules(Permissao.ROLE_FINANCEIRO_GERAR_BOLETO_AVULSO)
	public void index() {
		
		LOGGER.debug("inicio da geração do boleto avulso");
		this.carregarComboRegiao();
		
		this.carregarComboBox();
		
		this.carregarrComboRota(); 

	    this.carregarComboRoteiro();
	    
	    this.carregarComboBanco();
	}
  
	private void carregarComboBanco() {
		
		List<ItemDTO<Integer,String>> comboBancos = this.bancoService.getComboBancosBoletoAvulso();
		
		result.include("bancos",comboBancos);
	}

	private void carregarComboRegiao() {

		List<ItemDTO<Long,String>> comboRegiao =  new ArrayList<ItemDTO<Long,String>>();
		List<RegiaoDTO> regioes = regiaoService.buscarRegiao();

		for (RegiaoDTO itemRegiao : regioes) {
			comboRegiao.add(new ItemDTO<Long,String>(itemRegiao.getIdRegiao() , itemRegiao.getNomeRegiao()));
		}

		result.include("listaRegiao",comboRegiao);
	}
	
	/**
     * Inicia o combo Roteiro
     */
    private void carregarComboRoteiro() {
    	
        result.include("roteiros", this.roteirizacaoService.getComboTodosRoteiros());
    }

    /**
     * Inicia o combo Rota
     */
    private void carregarrComboRota() {

        result.include("rotas", this.roteirizacaoService.getComboTodosRotas());
    }

    /**
     * Inicia o combo Box
     */
    private void carregarComboBox() {

        result.include("listaBox", this.roteirizacaoService.getComboTodosBoxes());
    }
    
    @Post
   	@Path("/obterInformacoesParaBoleto")
   	public void obterInformacoesParaBoleto(FiltroBoletoAvulsoDTO filtro){
    	
    	Date dataDistrib = this.distribuidorService.obterDataOperacaoDistribuidor();
    	
    	if (!this.calendarioService.isDiaUtil(filtro.getDataVencimento())) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Data vencimento informada não é um dia util ");
		}
    	
    	if (dataDistrib.getTime() >  filtro.getDataVencimento().getTime()) {
    		throw new ValidacaoException(TipoMensagem.WARNING, "O campo [Data] não pode ser menor que a [Data de Operação ");
		}
    	
    	List<BoletoAvulsoDTO> listaCotasBoletos = this.boletoService.obterDadosBoletoAvulso(filtro);
        
		if (listaCotasBoletos==null || listaCotasBoletos.size()<=0){
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma informação encontrada para os filtros escolhidos.");
		}
		
		int qtd = listaCotasBoletos.size();
        
		TableModel<CellModelKeyValue<BoletoAvulsoDTO>> tableModel = new TableModel<CellModelKeyValue<BoletoAvulsoDTO>>();
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaCotasBoletos));
		tableModel.setTotal(qtd);
		tableModel.setPage(1);
		
		this.result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
    	
    }
    
    @Post
	@Rules(Permissao.ROLE_FINANCEIRO_GERAR_BOLETO_AVULSO)
	public void gerarBoletoAvulsoCota(List<BoletoAvulsoDTO> listaBoletosAvulso, Long idTipoMovimento) {
    	
		validarPreenchimentoCampos(listaBoletosAvulso, 235L);
		
		Map<Integer, Long> cotasBanco = new HashMap<Integer, Long>();
		
		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = this.gerarMovimentacaoFinanceira(listaBoletosAvulso, idTipoMovimento, cotasBanco);
		
		this.gerarCobrancaBoletoAvulso(listaMovimentoFinanceiroCota, cotasBanco);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS," sucesso."),Constantes.PARAM_MSGS).recursive().serialize();
		
    }
    
	@Path("/imprimirBoleto")
	public void imprimirBoleto(List<BoletoAvulsoDTO> listaBoletosAvulso) {
    	
    	byte[] arquivo = this.documentoCobrancaService.gerarDocumentoCobrancaBoletoAvulso(listaBoletosAvulso);
    	
    	String nomeArquivo = "Boleto-Avulso-"; 
    	
    	try {
			
    		this.httpResponse.setContentType("application/pdf");
    		this.httpResponse.setHeader("Content-Disposition", "attachment; filename=" + nomeArquivo + new Date()+ ".pdf");

    		OutputStream output = this.httpResponse.getOutputStream();
    		output.write(arquivo);

    		this.httpResponse.getOutputStream().close();

    		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS," sucesso."),Constantes.PARAM_MSGS).recursive().serialize();
	        
		} catch (Exception e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma informação encontrada para os filtros escolhidos.");
		}

    }
    
	private List<MovimentoFinanceiroCota> gerarMovimentacaoFinanceira(List<BoletoAvulsoDTO> listaNovosDebitoCredito, Long idTipoMovimento, Map<Integer, Long> cotasBanco) {
		
		Date dataCriacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		List<MovimentoFinanceiroCota> movimentosFinanceirosCota = new ArrayList<MovimentoFinanceiroCota>();
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = this.tipoMovimentoFinanceiroService.obterTipoMovimentoFincanceiroPorGrupoFinanceiroEOperacaoFinanceira(GrupoMovimentoFinaceiro.BOLETO_AVULSO, OperacaoFinaceira.DEBITO);
		
		if(tipoMovimentoFinanceiro == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Não foi encontrado tipo de movimento Boleto Avulso ");
		}
		
		for (BoletoAvulsoDTO debitoCredito : listaNovosDebitoCredito) {
			
			debitoCredito.setTipoMovimentoFinanceiro(tipoMovimentoFinanceiro);

			debitoCredito.setValor(debitoCredito.getValor());

			debitoCredito.setIdUsuario(this.getUsuarioLogado().getId());
			
			debitoCredito.setId(null);
			
			debitoCredito.setDataCriacao(dataCriacao);
			
			MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO = debitoCreditoCotaService.gerarMovimentoFinanceiroBoletoAvulsoDTO(debitoCredito);
			
			movimentoFinanceiroCotaDTO.setTipoEdicao(TipoEdicao.INCLUSAO);
			
			movimentoFinanceiroCotaDTO.setObservacao(debitoCredito.getObservacao());
			
			movimentosFinanceirosCota.addAll(this.movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(movimentoFinanceiroCotaDTO));
			
			cotasBanco.put(debitoCredito.getNumeroCota(), debitoCredito.getIdBanco());
			
		}
		
		return movimentosFinanceirosCota;
	}
	
    private void validarPreenchimentoCampos(List<BoletoAvulsoDTO> listaNovosDebitoCredito, Long idTipoMovimento) {
		
		if (idTipoMovimento == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "O preenchimento do campo [Tipo Movimento] é obrigatório.");
		}
		
		if (listaNovosDebitoCredito==null || listaNovosDebitoCredito.size()<=0){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não há movimentos para serem lançados.");
		}
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = this.tipoMovimentoFinanceiroService.obterTipoMovimentoFincanceiroPorGrupoFinanceiroEOperacaoFinanceira(GrupoMovimentoFinaceiro.BOLETO_AVULSO, OperacaoFinaceira.DEBITO);
		
		if(tipoMovimentoFinanceiro == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Não foi encontrado tipo de movimento Boleto Avulso ");
		}
		
		FormaCobranca formaCobrancaDiferenciada = this.formaCobrancaService.obterFormaCobrancaBoletoAvulso(TipoCobranca.BOLETO_AVULSO);
		
		if(formaCobrancaDiferenciada == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Forma de cobrança (boleto Avulso) não cadastrada.");
		}
		
		List<Long> linhasComErro = new ArrayList<Long>();
		
		String msgsErros = "";
		
		for (BoletoAvulsoDTO debitoCredito : listaNovosDebitoCredito) {
			
			long linha = (debitoCredito.getId()+1l);
			
			String valor = null;
			
			if(debitoCredito.getValor() == null || debitoCredito.getValor().equals("")) {
				valor = "0,00";
				debitoCredito.setValor("000");
			} else {
				valor = Util.getValorSemMascara(debitoCredito.getValor());				
				debitoCredito.setValor(valor);
			}
			
			Date dataVencimento = DateUtil.parseDataPTBR(debitoCredito.getDataVencimento());
			
			if (debitoCredito.getNumeroCota() == null) {
				
				linhasComErro.add(debitoCredito.getId());
				
				msgsErros += ("\nInforme o [número] da [Cota] na linha ["+linha+"] !");
			}
			
			Date dataDistrib = this.distribuidorService.obterDataOperacaoDistribuidor();
			
			if (dataVencimento == null) {

				linhasComErro.add(debitoCredito.getId());
			
			} 
			
			if (dataDistrib.getTime() >  dataVencimento.getTime()) {

				linhasComErro.add(debitoCredito.getId());
				
				msgsErros += ("\nO campo [Data] não pode ser menor que a [Data de Operação: "+DateUtil.formatarDataPTBR(dataDistrib)+"] na linha ["+linha+"] !");
			}
						
			if (!this.calendarioService.isDiaUtil(dataVencimento)) {

				linhasComErro.add(debitoCredito.getId());
				
				msgsErros += ("\nO campo [Data] não é um dia util : "+DateUtil.formatarDataPTBR(dataVencimento)+"] na linha ["+linha+"] !");
			}
			
			if (debitoCredito.getValor() == null) {
				
				linhasComErro.add(debitoCredito.getId());
				
				msgsErros += ("\nInforme o [Valor] na linha ["+linha+"] !");
			
			} else {

				try {

					new BigDecimal(Util.getValorSemMascara(debitoCredito.getValor()));

				} catch(NumberFormatException e) {

					linhasComErro.add(debitoCredito.getId());
					
					msgsErros += ("\nInforme um [Valor] válido na linha ["+linha+"] !");
				}
			}		
			
			if (debitoCredito.getDataVencimento()==null){
				
                linhasComErro.add(debitoCredito.getId());
				
                msgsErros += ("\nInforme a [Data] na linha ["+linha+"] !");
			}
		}

		if (!linhasComErro.isEmpty()) {
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, "Existe(m) movimento(s) preenchido(s) incorretamente.\n"+msgsErros);
					
			validacao.setDados(linhasComErro);
			
			throw new ValidacaoException(validacao);
		}
	}
    
    private void gerarCobrancaBoletoAvulso(List<MovimentoFinanceiroCota> movimentosFinanceirosCota, Map<Integer, Long> cotasBanco) {
    	
    	for (MovimentoFinanceiroCota movimentoFinanceiroCota : movimentosFinanceirosCota) {
			
    		try {
				this.gerarCobrancaService.gerarCobrancaBoletoAvulso(getUsuarioLogado().getId(), movimentoFinanceiroCota, cotasBanco);
			} catch (GerarCobrancaValidacaoException e) {
				throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao gerar cobrança por Boleto Avulso.");
			}
		}
    }
    
    @Post
	@Path("/addBoletoAvulsoEmLote")
	public void addBoletoAvulsoEmLote(UploadedFile xls) throws IOException {  

		List<BoletoAvulsoDTO> listaCotasBoletos = XlsUploaderUtils.getBeanListFromXls(BoletoAvulsoDTO.class, xls);
		
		TableModel<CellModelKeyValue<BoletoAvulsoDTO>> tableModel = new TableModel<CellModelKeyValue<BoletoAvulsoDTO>>();
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaCotasBoletos));
		int qtd = listaCotasBoletos.size();
		tableModel.setTotal(qtd);
		tableModel.setPage(1);
		
		this.result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
    
    @Post
	@Path("/uploadArquivoLote")
	public void uploadExcel(UploadedFile excelFile) throws FileNotFoundException, IOException{

		List<BoletoAvulsoDTO> listBoletoAvulsoExcel = XlsUploaderUtils.getBeanListFromXls(BoletoAvulsoDTO.class, excelFile);
		
		TableModel<CellModelKeyValue<BoletoAvulsoDTO>> tableModel = new TableModel<CellModelKeyValue<BoletoAvulsoDTO>>();
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listBoletoAvulsoExcel));
		int qtd = listBoletoAvulsoExcel.size();
		tableModel.setTotal(qtd);
		tableModel.setPage(1);
		
		this.result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
}