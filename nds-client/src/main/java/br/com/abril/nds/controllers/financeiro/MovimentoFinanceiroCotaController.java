package br.com.abril.nds.controllers.financeiro;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.ProcessamentoFinanceiroCotaVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FechamentoEncalheService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/financeiro/movimentoFinanceiroCota")
@Rules(Permissao.ROLE_MOVIMENTO_FINANCEIRO_COTA)
public class MovimentoFinanceiroCotaController extends BaseController{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MovimentoFinanceiroCotaController.class);
	
	@Autowired
	private Result result;
	
	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private FechamentoEncalheService fechamentoEncalheService;
	
	@Autowired
	private GerarCobrancaService gerarCobrancaService;
	
	@Autowired
	private LancamentoService lancamentoService;
	
    @Autowired
    private ProdutoEdicaoService produtoEdicaoService;
	
	@Path("/")
	public void index(){
		
	}
	
	/**
	 * Verifica se houve Fechamento de Encalhe e Confirmação de Expedição para 
	 * liberar o processamento Financeiro da Cota à Vista na data de operação atual
	 * 
	 * @param dataOperacao
	 */
	private void verificaLiberacaoProcessamentoFinanceiro(Date dataOperacao){
		
        boolean fechamentoRealizado = this.fechamentoEncalheService.validarEncerramentoOperacaoEncalhe(dataOperacao);
		
		if (!fechamentoRealizado){
			
			 throw new ValidacaoException(TipoMensagem.WARNING, "Fechamento de Encalhe ainda não realizado !");
		}
		
		List<Lancamento> lcto = this.lancamentoService.obterLancamentoDataDistribuidorInStatus(dataOperacao, Arrays.asList(StatusLancamento.BALANCEADO));
		
		if (lcto!=null && !lcto.isEmpty()){
			
			 throw new ValidacaoException(TipoMensagem.WARNING, "Expedição pendente de Confirmação !");
		}
	}

	@Post
	@Path("/buscarCotas")
	public void buscaDividasBaixadas(Integer numeroCota, String codigoProduto, String numeroEdicao,
					                 String sortorder, 
					                 String sortname,
					                 int page, 
					                 int rp){
		
		Date data = this.distribuidorService.obterDataOperacaoDistribuidor();

		this.verificaLiberacaoProcessamentoFinanceiro(data);
		
		ProdutoEdicao produtoEdicao = null;
        
        if(codigoProduto != null && numeroEdicao != null) {
        	
        	 produtoEdicao = produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, numeroEdicao);
             
             if(produtoEdicao == null) {
             	throw new ValidacaoException(TipoMensagem.WARNING, "Produto Edição não encontrado !");
             }
        	
        }
		
		List<ProcessamentoFinanceiroCotaVO> processamentoFinanceiroCotaVO = this.movimentoFinanceiroCotaService.obterProcessamentoFinanceiroCota(numeroCota,
																																				 produtoEdicao,
				                                                                                                                                 data, 
				                                                                                                                                 sortorder, 
				                                                                                                                                 sortname,
				                                                                                                                                 page,
				                                                                                                                                 rp);

		if (processamentoFinanceiroCotaVO==null || processamentoFinanceiroCotaVO.isEmpty()){
			
            throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma pendência financeira encontrada na data["
                    + DateUtil.formatarDataPTBR(data) + "].");
		}
		
		//int qtdRegistros = this.movimentoFinanceiroCotaService.obterQuantidadeProcessamentoFinanceiroCota(numeroCota, produtoEdicao);
		
		int qtdRegistros = processamentoFinanceiroCotaVO.size();
		
		TableModel<CellModelKeyValue<ProcessamentoFinanceiroCotaVO>> tableModel = new TableModel<CellModelKeyValue<ProcessamentoFinanceiroCotaVO>>();
			
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(processamentoFinanceiroCotaVO));
		tableModel.setPage(page);
		tableModel.setTotal(qtdRegistros);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	@Path("/processarFinanceiroCota")
	public void processarFinanceiroCota(List<Integer> numerosCota){
		
		if (numerosCota==null){
		    
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma cota selecionada.");
		}
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		Usuario usuario = this.usuarioService.getUsuarioLogado();
		
        List<Cota> cotas = this.cotaService.obterCotasPorNumeros(numerosCota);
		
		this.movimentoFinanceiroCotaService.gerarMovimentoFinanceiroCota(cotas, dataOperacao, usuario);
	
		try {
			
			this.gerarCobrancaService.gerarCobranca(cotas, 
												    usuario.getId(),
												    false);
			
		} catch (GerarCobrancaValidacaoException e) {

			LOGGER.error(e.getMessage(), e);
		}
		
        result.use(Results.json())
                .from(new ValidacaoVO(TipoMensagem.SUCCESS, "Financeiro processado e cobrança gerada com sucesso."),
                        "result").recursive().serialize();
	}
	
	@Post
	@Path("/postergarFinanceiroCota")
	public void postergarFinanceiroCota(List<Integer> numerosCota){
		
		if (numerosCota==null){
		    
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma cota selecionada.");
		}
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		Usuario usuario = this.usuarioService.getUsuarioLogado();
		
        List<Cota> cotas = this.cotaService.obterCotasPorNumeros(numerosCota);

		if (cotas!=null && !cotas.isEmpty()){
			
			try {
				
				this.movimentoFinanceiroCotaService.gerarMovimentoFinanceiroCota(cotas, dataOperacao, usuario);
				
				this.gerarCobrancaService.gerarDividaPostergadaCotas(cotas, usuario.getId());
				
			} catch (GerarCobrancaValidacaoException e) {

				LOGGER.error(e.getMessage(), e);
			}
		}

        result.use(Results.json())
                .from(new ValidacaoVO(TipoMensagem.SUCCESS, "Financeiro processado e dívida postergada com sucesso."),
                        "result").recursive().serialize();
	}
}
