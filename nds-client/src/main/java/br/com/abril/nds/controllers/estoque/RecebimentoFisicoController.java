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

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.FornecedorService;
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
@Path("/estoque/recebimentoFisico")
public class RecebimentoFisicoController {
	
	private Result result;

	private HttpServletRequest request;
	
	private Validator validator;
	
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
	

	public RecebimentoFisicoController(
			Result result, 
			HttpServletRequest request,
			Validator validator) {
		this.result = result;
		this.request = request;
		this.validator = validator;
	}

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
		
	
	@Post
	public void obterListaItemRecebimentoFisico() {
		
		Long idNotaFiscal = (Long) request.getSession().getAttribute(NOTA_FISCAL_ID);
		
		//TODO: obter lista do bd apos testes;
		List<RecebimentoFisicoDTO> listaDTO = getListaItemNotaFromBD();
		
		TableModel<CellModel> tableModel =  obterTableModelParaListItensNotaRecebimento(listaDTO);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	/**
	 * TODO: remover apos testes...
	 * 
	 * @return NotaFiscal
	 */
	private NotaFiscal getNotaFromBD() {
		
		NotaFiscal notaFiscal = new NotaFiscalFornecedor();
		
		notaFiscal.setId(1L);
		
		return notaFiscal;
		
	}
	
	private void validarDadosNotaFiscal(String cnpj, String numeroNotaFiscal, String serie) {
		
		List<String> msgs = new ArrayList<String>();
		
		if(cnpj == null || cnpj.isEmpty()) {
			msgs.add("O campo CNPJ é obrigatório");
		} else if(cnpj.length()<12) {
			msgs.add("O campo cnpj esta inválido");
		}
		
		if(numeroNotaFiscal == null || numeroNotaFiscal.isEmpty()) {
			msgs.add("O campo Nota Fiscal é obrigatório");
		}

		if(serie == null || serie.isEmpty()) {
			msgs.add("O campo Série é obrigatório");
		}

		if(!msgs.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, msgs));
		}
		
	}
	

	@Post
	public void excluirItemNotaFiscal(String idItemNotaFiscal) {
		
		//TODO: code
		
		System.out.println("REMOVENDO ITEM NOTA FISCAL CODIGO: " + idItemNotaFiscal);
		
		List<String> msgs = new ArrayList<String>();
		msgs.add("Item Nota fiscal removido com sucesso.");
		ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, msgs);
		result.use(Results.json()).from(validacao, "result").include("listaMensagens").serialize();
		
	}

	@Post
	public void salvarDadosItensDaNotaFiscal(List<RecebimentoFisicoDTO> itensRecebimento) {
		
		if(itensRecebimento != null) {
			
			for(RecebimentoFisicoDTO item : itensRecebimento) {

				System.out.println("idItemNota " + item.getIdItemNota());
				System.out.println("qtdFisico " + item.getQtdFisico());
				
				System.out.println("\n\n" );
			}
			
		}
			
		List<String> msgs = new ArrayList<String>();
		msgs.add("Itens salvos com sucesso.");
		ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, msgs);
		result.use(Results.json()).from(validacao, "result").include("listaMensagens").serialize();	
	}
	
	
	
	@Post
	public void verificarNotaFiscalExistente(String cnpj, String numeroNotaFiscal, String serie, String chaveAcesso) {

		validarDadosNotaFiscal(cnpj, numeroNotaFiscal, serie);
		
		request.getSession().setAttribute(NOTA_FISCAL_ID, null);
		
		//TODO: CHAMAR PESQUISA DO BD
		NotaFiscal notaFiscal = getNotaFromBD();
		
		if(notaFiscal == null){	

			List<String> msgs = new ArrayList<String>();
			
			msgs.add("Nota fiscal não encontrada");
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, msgs);
			
			result.use(Results.json()).from(validacao, "result").include("listaMensagens").serialize();
		
		} else {
			
			request.getSession().setAttribute(NOTA_FISCAL_ID, notaFiscal.getId());
			
			List<String> msgs = new ArrayList<String>();
			
			msgs.add("Nota fiscal encontrada com sucesso");
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, msgs);
			
			result.use(Results.json()).from(validacao, "result").include("listaMensagens").serialize();

		}
				
	}
	
	
	/**
	 *	TODO : remover este mock apos testes 
	 */
	private List<RecebimentoFisicoDTO> getListaItemNotaFromBD() {
		
		List<RecebimentoFisicoDTO> listaDTO = new ArrayList<RecebimentoFisicoDTO>();
		
		RecebimentoFisicoDTO recebimentoFisicoDTO = null;
		
		recebimentoFisicoDTO = new RecebimentoFisicoDTO(1L, 1L,"54", "Michel", 12L, BigDecimal.TEN, BigDecimal.TEN, 
				BigDecimal.TEN, BigDecimal.TEN, TipoDiferenca.FALTA_DE);
		listaDTO.add(recebimentoFisicoDTO);

		recebimentoFisicoDTO = new RecebimentoFisicoDTO(2L, 2L, "54", "Ana", 12L, BigDecimal.TEN, BigDecimal.TEN, 
				BigDecimal.TEN, BigDecimal.TEN, TipoDiferenca.FALTA_DE);
		listaDTO.add(recebimentoFisicoDTO);

		recebimentoFisicoDTO = new RecebimentoFisicoDTO(3L, 3L,"54", "Jose", 12L, BigDecimal.TEN, BigDecimal.TEN, 
				BigDecimal.TEN, BigDecimal.TEN, TipoDiferenca.FALTA_DE);
		listaDTO.add(recebimentoFisicoDTO);

		recebimentoFisicoDTO = new RecebimentoFisicoDTO(4L, 4L,"54", "Marilda", 12L, BigDecimal.TEN, BigDecimal.TEN, 
				BigDecimal.TEN, BigDecimal.TEN, TipoDiferenca.FALTA_DE);
		listaDTO.add(recebimentoFisicoDTO);
		
		return listaDTO;

	}
	
	
	/**
	 * Obtem um tableModel com os dados da lista de itens de recebimento fisico.
	 * 
	 * @param listaExtratoEdicao
	 * 
	 * @return TableModel.
	 */
	private TableModel<CellModel> obterTableModelParaListItensNotaRecebimento(List<RecebimentoFisicoDTO> listaDTO) {
					
		TableModel<CellModel> tableModel = new TableModel<CellModel>();
		
		List<CellModel> listaModeloGenerico = new LinkedList<CellModel>();
		
		for(RecebimentoFisicoDTO dto : listaDTO) {
			
			String codigo 		     = dto.getCodigoProduto();
			String nomeProduto 	     = dto.getNomeProduto();
			String edicao 		     = (dto.getEdicao() 			== null) 	? "" 	: dto.getEdicao().toString();
			String precoCapa 	     = (dto.getPrecoCapa() 			== null) 	? "0.0" : dto.getPrecoCapa().toString();
			String repartePrevisto 	 = (dto.getRepartePrevisto() 	== null) 	? "0.0" : dto.getRepartePrevisto().toString();
			String qtdeFisica		 = (dto.getQtdFisico() 			== null) 	? "0.0" : dto.getQtdFisico().toString();
			String diferenca		 = (dto.getDiferenca() 			== null) 	? "0.0" : dto.getDiferenca().toString();
			String valorTotal		 = (dto.getValorTotal() 		== null) 	? "0.0" : dto.getValorTotal().toString() ;
			
			listaModeloGenerico.add(
					new CellModel( 	
							dto.getIdItemNota().intValue(), 
							codigo, 
							nomeProduto, 
							edicao, 
							precoCapa, 
							repartePrevisto,
							qtdeFisica,
							diferenca,
							valorTotal
					));
			
			
		}
		
		tableModel.setPage(1);
		tableModel.setTotal(listaModeloGenerico.size());
		tableModel.setRows(listaModeloGenerico);
		
		return tableModel;
		
	}
	
	

	
	/**
	 * Inclui na view os dados do combo de Fornecedor.
	 */
	private void carregarComboFornecedor() {
		
		List<Fornecedor> fornecedores = fornecedorService.obterFornecedoresAtivos();
		
		if (fornecedores != null) {
			result.include("listafornecedores", fornecedores);
		}
		
	}
	
	/**
	 * Inclui na view os dados do combo TipoLancamento.
	 */
	private void carregarComboTipoLancamento() {
		
		List<String> listaTipoLancamento = new ArrayList<String>();
		
		for(TipoLancamento obj: TipoLancamento.values()){
			
			listaTipoLancamento.add(obj.name());
			
		}
		
		result.include("listaTipoLancamento",listaTipoLancamento);
		
	}
	
	/**
	 * Inclui na view dados dos combos.
	 */
	public void preencherCombos() {
		
		carregarComboFornecedor();
		
		carregarComboTipoLancamento();
		
	}

	
	@Get
	public List<RecebimentoFisico> consulta() {
		return null;
	}

	@Post
	@Path("inserirNota")
	public void inserirNotaFiscal(NotaFiscalFornecedor notaFiscalFornecedor,
			RecebimentoFisico recebimentoFisico) throws ParseException {

		System.out.println("@@@@@@@@@@@@@@@@@@@@"
				+ notaFiscalFornecedor.getDataEmissao());
		/*
		 * validator.checking(new Validations() {{
		 * that(!"".equals(notaFiscalFornecedor.getDataEmissao()),
		 * "produto.nome", "nome.vazio"); //that(produto.getPreco() > 0,
		 * "produto.preco", "preco.invalido"); }});
		 * validator.onErrorUsePageOf(RecebimentoFisicoController
		 * .class).index();
		 */

		notaFiscalService.inserirNotaFiscal(notaFiscalFornecedor);

		notaFiscalFornecedor.setOrigem(Origem.MANUAL);

		recebimentoFisico.setNotaFiscal(notaFiscalFornecedor);

		recebimentoFisico.setStatusConfirmacao(StatusConfirmacao.PENDENTE);

		// receber o Usuario que inseriu a nota
		Usuario usuario = new Usuario();
		usuario.setId(1L);

		//recebimentoFisico.setUsuario(usuario);

		recebimentoFisicoService.adicionarRecebimentoFisico(recebimentoFisico);
		result.redirectTo("/recebimentoFisico");
	}


}
