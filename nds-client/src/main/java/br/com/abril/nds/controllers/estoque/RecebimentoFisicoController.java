package br.com.abril.nds.controllers.estoque;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.ItemNotaRecebimentoFisicoDTO;
import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ItemNotaFiscalService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.PessoaJuridicaService;
import br.com.abril.nds.service.PessoaService;
import br.com.abril.nds.service.RecebimentoFisicoService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.view.Results;

@Resource
public class RecebimentoFisicoController {
	@Autowired
	private Result result;
	
	private static final String NOTA_FISCAL_ID = "notaFiscalId";
	
	private static final String GRID_RESULT = "gridResult";
	
	@Autowired
	private FornecedorService fornecedorService;
	@Autowired
	private NotaFiscalService notaFiscalService;
	@Autowired
	private RecebimentoFisicoService recebimentoFisicoService;
	@Autowired
	private PessoaService pessoaService;
	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;
	@Autowired
	private ItemNotaFiscalService itemNotaService;
	
	private HttpServletRequest request;
	
	@Autowired
	private Validator validator;
	

	public void recebimentoFisico(Result result, HttpServletRequest request,
			FornecedorService fornecedorService, PessoaService pessoaService,
			NotaFiscalService notaFiscalService,
			RecebimentoFisicoService recebimentoFisicoServer,
			PessoaJuridicaService pessoaJuridicaService,
			Validator validator) {
		this.result = result;
		this.request = request;
		this.fornecedorService = fornecedorService;
		this.pessoaService = pessoaService;
		this.notaFiscalService = notaFiscalService;
		this.recebimentoFisicoService = recebimentoFisicoServer;
		this.pessoaJuridicaService = pessoaJuridicaService;
		this.validator = validator;
	}

	@Path("/recebimentoFisico")
	public void index() throws ParseException {

		preencherCombos();
		preencherDataEmissao();
	
	}
	
	private void preencherDataEmissao() {
		// RecebimentoFisico recebimento = new RecebimentoFisico();
		Date data = new Date(System.currentTimeMillis());
		SimpleDateFormat formatarDate = new SimpleDateFormat("dd-MM-yyyy");

		result.include("dataAtual", formatarDate.format(data));

	}

	// metodo para prrencher combo fornecedor com o cnpj informado
	@Post
	public void buscaCnpj(String cnpj){
		
		PessoaJuridica pessoaJuridica = pessoaJuridicaService.buscarPorCnpj(cnpj);
		
		if(pessoaJuridica != null){
			result.use(Results.json()).from(pessoaJuridica, "result").serialize();			 
		}else{
			
			throw new ValidacaoException(TipoMensagem.ERROR,"CNPJ não encontrado!");
			
		}
	}
	
	// metodo para prrencher combo fornecedor com o cnpj informado
		@Post
		public void buscaCnpjPorFornecedor(String nomeFantasia) throws ParseException {
			
			PessoaJuridica pessoaJuridica = pessoaJuridicaService.buscarCnpjPorFornecedor(nomeFantasia);
			if(pessoaJuridica != null){
				result.use(Results.json()).from(pessoaJuridica, "result").serialize();			 
			 }else{
				 throw new ValidacaoException(TipoMensagem.ERROR,"Fornecedor não encontrado");
			 }
		
		}
		

	// metodo para gerar uma lista com sugestao
	
	@Post
	public void obterListaItemRecebimentoFisico() {
		
		//TODO
		
		
		
		TableModel<CellModel> tableModel = null;
		List<RecebimentoFisicoDTO> listaDTO = getListaItemNotaFromBD();
		tableModel = obterTableModelParaListItensNotaRecebimento(listaDTO);
		Map<String, Object> resultado = new HashMap<String, Object>();
		resultado.put(GRID_RESULT, tableModel);
		result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();
		
	}
	
	@Post
	public void verificarExisteNota(String numero) {				
		
		
				
	}
	
	//mockao nervoso
	private List<RecebimentoFisicoDTO> getListaItemNotaFromBD() {
		
		List<RecebimentoFisicoDTO> listaDTO = new ArrayList<RecebimentoFisicoDTO>();
		
		
				
		RecebimentoFisicoDTO recebimentoFisicoDTO = new RecebimentoFisicoDTO("54", "Monica", 12L, BigDecimal.TEN, BigDecimal.TEN, 
				BigDecimal.TEN, BigDecimal.TEN, TipoDiferenca.FALTA_DE,1L);
		RecebimentoFisicoDTO recebimentoFisicoDTO2 = new RecebimentoFisicoDTO("54", "Monica", 12L, BigDecimal.TEN, BigDecimal.TEN, 
				BigDecimal.TEN, BigDecimal.TEN, TipoDiferenca.FALTA_DE,2L);
		RecebimentoFisicoDTO recebimentoFisicoDTO3 = new RecebimentoFisicoDTO("54", "Monica", 12L, BigDecimal.TEN, BigDecimal.TEN, 
				BigDecimal.TEN, BigDecimal.TEN, TipoDiferenca.FALTA_DE,3L);
		RecebimentoFisicoDTO recebimentoFisicoDTO4 = new RecebimentoFisicoDTO("54", "Monica", 12L, BigDecimal.TEN, BigDecimal.TEN, 
				BigDecimal.TEN, BigDecimal.TEN, TipoDiferenca.FALTA_DE,4L);
		
		listaDTO.add(recebimentoFisicoDTO);
		listaDTO.add(recebimentoFisicoDTO2);
		listaDTO.add(recebimentoFisicoDTO3);
		listaDTO.add(recebimentoFisicoDTO4);
		
		return listaDTO;
		

	}
	
	
	/**
	 * Criar Grid na  tela com os dados de recebimentoFisicoDTO
	 * @param listaExtratoEdicao
	 * @return
	 */
	private TableModel<CellModel> obterTableModelParaListItensNotaRecebimento(List<RecebimentoFisicoDTO> listaDTO) {
					
		TableModel<CellModel> tableModel = new TableModel<CellModel>();
		
		List<CellModel> listaModeloGenerico = new LinkedList<CellModel>();
		
		for(RecebimentoFisicoDTO dto : listaDTO) {
			
			String codigo 		     =  dto.getCodigo();
			String nomeProduto 	     = dto.getNomeProduto();
			String edicao 		     = dto.getEdicao().toString();
			String precoCapa 	     = dto.getPrecoCapa().toString();
			String repartePrevisto 	 = dto.getRepartePrevisto().toString();
			String qtdeFisica		 = dto.getQtdFisico().toString();
			String diferenca		 = dto.getDiferenca().toString();
			String valorTotal		 = dto.getValorTotal().toString();
			listaModeloGenerico.add(new CellModel(dto.getIdItemNota().intValue(), codigo, nomeProduto, edicao, precoCapa, repartePrevisto,qtdeFisica,diferenca,valorTotal));
			
		}
		
		tableModel.setPage(1);
		tableModel.setTotal(listaModeloGenerico.size());
		tableModel.setRows(listaModeloGenerico);
		
		return tableModel;
		//
	}
		
	private List<String> getMensagemExisteNota(){
		
		
		List<String> listaMensagemValidacao = new ArrayList<String>();
		listaMensagemValidacao.add(Constantes.TIPO_MSG_SUCCESS);
		listaMensagemValidacao.add("Nota Fiscal encontrada!");
		
		return listaMensagemValidacao;
		
	}
	
	public void preencherCombos() {
		List<Fornecedor> fornecedores = fornecedorService
				.obterFornecedoresAtivos();
		if (fornecedores != null) {
			result.include("listafornecedores", fornecedores);
		}
		
		List<String> listaTipoLancamento = new ArrayList<String>();
		for(TipoLancamento obj: TipoLancamento.values()){
			listaTipoLancamento.add(obj.name());
		}			
		result.include("listaTipoLancamento",listaTipoLancamento);
	}

	
	@Get
	public List<RecebimentoFisico> consulta() {
		return null;
	}

	@Post
	@Path("/recebimentoFisico/inserirNota")
	public void inserirNotaFiscal(NotaFiscalFornecedor notaFiscalFornecedor) throws ParseException {
		
	
		if(	validaAtributosNecessariosParaInsercao(notaFiscalFornecedor)){
			notaFiscalService.inserirNotaFiscal(notaFiscalFornecedor);
		}
		
		result.redirectTo("/recebimentoFisico");
	}
	
	@Post
	@Path("/recebimentoFisico/inserirItemNota")
	public void inserirItemNota(ItemNotaRecebimentoFisicoDTO itemNotaRecebimentoFisicoDTO) {
		
		//recebimentoFisicoService.inserirItemNotaRecebimentoFisico(itemNotaRecebimentoFisicoDTO);
				
		result.redirectTo("/recebimentoFisico");
	}
	
	@Post
	@Path("/recebimentoFisico/alterarItemNota")
	public void alterarItemNota(RecebimentoFisicoDTO recebimentoFisicoDTO) {
		
		recebimentoFisicoService.alterarItemNotaRecebimentoFisico(recebimentoFisicoDTO);
				
		result.redirectTo("/recebimentoFisico");
	}

	private boolean validaAtributosNecessariosParaInsercao(
			NotaFiscalFornecedor notaFiscalFornecedor) {
		boolean isValido = true;
		
		if(notaFiscalFornecedor.getEmitente().getCnpj() == null 
				|| notaFiscalFornecedor.getNumero() == null
				|| notaFiscalFornecedor.getSerie() == null
				|| notaFiscalFornecedor.getDataEmissao() == null
				|| notaFiscalFornecedor.getValorBruto() == null
				|| notaFiscalFornecedor.getValorLiquido() == null){
			isValido = false;
		}
		FiltroConsultaNotaFiscalDTO dto = new FiltroConsultaNotaFiscalDTO();
		dto.setChave(notaFiscalFornecedor.getChaveAcesso());
		dto.setCnpj(notaFiscalFornecedor.getEmitente().getCnpj());
		dto.setNumeroNota(notaFiscalFornecedor.getNumero());
		dto.setSerie(notaFiscalFornecedor.getSerie());
		
		List<NotaFiscal> listaNota = notaFiscalService.obterNotaFiscalPorNumeroSerieCnpj(dto);
		if(listaNota.isEmpty()){
			isValido = false;
		}
				
		return isValido;
	}

}
