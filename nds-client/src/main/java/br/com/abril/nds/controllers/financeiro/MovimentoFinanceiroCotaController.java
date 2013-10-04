package br.com.abril.nds.controllers.financeiro;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.ProcessamentoFinanceiroCotaVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
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
	private GerarCobrancaService gerarCobrancaService;
	
	@Path("/")
	public void index(){
		
	}

	@Post
	@Path("/buscarCotas")
	public void buscaDividasBaixadas(Integer numeroCota,
					                 String sortorder, 
					                 String sortname,
					                 int page, 
					                 int rp){
		
		Date data = this.distribuidorService.obterDataOperacaoDistribuidor();

		List<ProcessamentoFinanceiroCotaVO> processamentoFinanceiroCotaVO = this.movimentoFinanceiroCotaService.obterProcessamentoFinanceiroCota(numeroCota, 
				                                                                                                                                 data, 
				                                                                                                                                 sortorder, 
				                                                                                                                                 sortname,
				                                                                                                                                 page,
				                                                                                                                                 rp);

		if (processamentoFinanceiroCotaVO==null || processamentoFinanceiroCotaVO.isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma pendência financeira encontrada na data["+DateUtil.formatarDataPTBR(data)+"].");
		}
		
		int qtdRegistros = this.movimentoFinanceiroCotaService.obterQuantidadeProcessamentoFinanceiroCota(numeroCota, data);
			
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

			e.printStackTrace();
		}
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Financeiro processado com sucesso."), "result").recursive().serialize();
	}
}
