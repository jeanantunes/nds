package br.com.abril.nds.controllers.nfe;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.NotasCotasImpressaoNfeDTO;
import br.com.abril.nds.dto.ProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroImpressaoNFEDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.fiscal.TipoDestinatario;
import br.com.abril.nds.model.fiscal.TipoEmissaoNfe;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ImpressaoNFEService;
import br.com.abril.nds.service.RotaService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.service.exception.AutenticacaoEmailException;
import br.com.abril.nds.util.AnexoEmail;
import br.com.abril.nds.util.AnexoEmail.TipoAnexo;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path(value="/nfe/impressaoNFE")
@Rules(Permissao.ROLE_NFE_IMPRESSAO_NFE)
public class ImpressaoNFEController extends BaseController {

	@Autowired
	private HttpServletResponse httpResponse;

	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private ImpressaoNFEService impressaoNFEService;

	@Autowired
	private RoteirizacaoService roteirizacaoService;

	@Autowired
	private RotaService rotaService;

	@Autowired
	private Result result;

	@Autowired
	private EmailService emailSerice;
	
	@Autowired
	private CotaService cotaService;
	
	@Path("/")
	public void index() {

		this.obterFornecedoresDestinatarios();
		this.obterTodosFornecedoresAtivos();
		this.iniciarComboRoteiro();
		this.iniciarComboRota();
		this.obterTiposDestinatarios();
		this.iniciarComboBox();
		
		this.result.include("tipoEmissao", TipoEmissaoNfe.values());
		
	}
	
	private void obterTiposDestinatarios() {
		result.include("tiposDestinatarios", new TipoDestinatario[] {TipoDestinatario.COTA, TipoDestinatario.DISTRIBUIDOR, TipoDestinatario.FORNECEDOR});
	}
	
	private void obterFornecedoresDestinatarios() {
		result.include("fornecedoresDestinatarios", fornecedorService.obterFornecedoresDestinatarios(SituacaoCadastro.ATIVO));
	}

	private void obterTodosFornecedoresAtivos() {
		result.include("fornecedores", fornecedorService.obterFornecedoresIdNome(SituacaoCadastro.ATIVO, true));
	}
	
	/**
     * Inicia o combo Box
     */
    private void iniciarComboBox() {

    	result.include("listaBox", this.roteirizacaoService.getComboTodosBoxes());
    }

	private void iniciarComboRoteiro() {
		
		List<Roteiro> roteiros = this.roteirizacaoService.buscarRoteiro(null, null);
		
		List<ItemDTO<Long, String>> listRoteiro = new ArrayList<ItemDTO<Long,String>>();
		
		for (Roteiro roteiro : roteiros){
			
			listRoteiro.add(new ItemDTO<Long, String>(roteiro.getId(), roteiro.getDescricaoRoteiro()));
		}
		
		result.include("roteiros", listRoteiro);
	}
	
	private void iniciarComboRota() {
		List<Rota> rotas = this.roteirizacaoService.buscarRota(null, null);
		
		List<ItemDTO<Long, String>> listRota = new ArrayList<ItemDTO<Long,String>>();
		
		for (Rota rota : rotas){
			
			listRota.add(new ItemDTO<Long, String>(rota.getId(), rota.getDescricaoRota()));
		}
		
		result.include("rotas", listRota);
	}
		
	@Post
	public void pesquisarImpressaoNFE(FiltroImpressaoNFEDTO filtro, String sortorder, String sortname, int page, int rp) {
		
		// Paginação 
		PaginacaoVO paginacao = carregarPaginacao(sortname, sortorder, rp, page);
		
		filtro.setPaginacao(paginacao);
		
		verificarFiltro(filtro);

		List<NotasCotasImpressaoNfeDTO> listaCotasImpressaoNFe = new ArrayList<NotasCotasImpressaoNfeDTO>();
		
		TableModel<CellModelKeyValue<NotasCotasImpressaoNfeDTO>> tableModel = new TableModel<CellModelKeyValue<NotasCotasImpressaoNfeDTO>>();
		
		listaCotasImpressaoNFe = impressaoNFEService.obterNotafiscalImpressao(filtro);

		if(listaCotasImpressaoNFe == null || listaCotasImpressaoNFe.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		// tableModel.setTotal(impressaoNFEService.buscarNFeParaImpressaoTotalQtd(filtro));
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaCotasImpressaoNFe));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		result.use(FlexiGridJson.class).from(listaCotasImpressaoNFe).page(page).total(listaCotasImpressaoNFe.size()).serialize();

	}
	
	private PaginacaoVO carregarPaginacao(String sortname, String sortorder, int rp, int page) {
		
		PaginacaoVO paginacao = new PaginacaoVO();
		paginacao.setOrdenacao(Ordenacao.ASC);
	    paginacao.setPaginaAtual(page);
	    paginacao.setQtdResultadosPorPagina(rp);
	    paginacao.setSortOrder(sortorder);
	    paginacao.setSortColumn(sortname);
	
	    
	    
	    return paginacao;
	}
	
	private void verificarFiltro(FiltroImpressaoNFEDTO filtro) {
		
		if(!filtro.isFiltroValido()) {

			List<String> listaMensagemValidacao = new ArrayList<String>(filtro.validarFiltro().values());

			if (!listaMensagemValidacao.isEmpty()) {
				ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR, listaMensagemValidacao);
				throw new ValidacaoException(validacaoVO);
			}

			result.use(Results.nothing());
		}
	}

	@Post
	public void pesquisarProdutosImpressaoNFE(FiltroImpressaoNFEDTO filtro, String sortname, String sortorder, String codigoProduto, String nomeProduto, int page, int rp) {

		if(!filtro.isFiltroValido()) {
			ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR, "O filtro informado é inválido");
			throw new ValidacaoException(validacaoVO);
		}
		if(filtro.validarDataMovimentoInicial() != null || filtro.validarDataMovimentoFinal() != null ) {
			ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR, "As datas inicial e final do movimento devem ser informadas.");
			throw new ValidacaoException(validacaoVO);
		}

		TableModel<CellModelKeyValue<ProdutoDTO>> tableModel = new TableModel<CellModelKeyValue<ProdutoDTO>>();

		List<ProdutoDTO> listaProdutos = impressaoNFEService.obterProdutosExpedicaoConfirmada(filtro); // c.getTime()

		if(listaProdutos == null) {
			result.use(Results.nothing());
			return;
		}

		tableModel.setTotal(listaProdutos != null ? listaProdutos.size() : 0);

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaProdutos));

		tableModel.setPage(page);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}

	/**
	 * Exporta os dados da pesquisa.
	 * 
	 * @param fileType - tipo de arquivo
	 * 
	 * @throws IOException Exceção de E/S
	 */
	@Post
	@SuppressWarnings("deprecation")
	public void exportar(FiltroImpressaoNFEDTO filtro, FileType fileType) throws IOException {
		
		filtro.setPaginacao(null);
		
		List<NotasCotasImpressaoNfeDTO> listaNFeDTO = impressaoNFEService.obterNotafiscalImpressao(filtro);
		
		if(listaNFeDTO == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Não foi encontrado nenhum item");
		}
		
		FileExporter.to("nota-fiscal-impressa", fileType).inHTTPResponse(
				this.getNDSFileHeader(), 
				null, 
				null, 
				listaNFeDTO,
				NotasCotasImpressaoNfeDTO.class, this.httpResponse);

	}
	
	@Post
	public void imprimirNFe(FiltroImpressaoNFEDTO filtro, String sortorder, String sortname) {
		
		if(filtro.getNumerosNotas() == null) {
			
			ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR, "Devem ser informadas as cotas para impressão.");
			throw new ValidacaoException(validacaoVO);
		} 
		
		byte[] report = this.impressaoNFEService.imprimirNFe(filtro);
		
		try {
			this.escreverArquivoParaResponse(report, "danfe");
		} catch (ValidacaoException e) {
            
            result.use(Results.json()).from(e.getValidacao(), Constantes.PARAM_MSGS).recursive().serialize();
        } catch (Exception e) {
            
            result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.ERROR, e.getMessage()), Constantes.PARAM_MSGS).recursive().serialize();
        }
		
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Arquivo gerado com sucesso."), Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	@Post
	public void enviarEmail(FiltroImpressaoNFEDTO filtro, String sortorder, String sortname) {
		
		if(filtro.getNumerosNotas() == null) {
			
			ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR, "Devem ser informadas as cotas para impressão.");
			throw new ValidacaoException(validacaoVO);
		}
		
		Map<Integer, List<Long>> mapNumeroNotasAgrupadasPorCota = new HashMap<>();
		Map<Integer, Cota> mapCotaPesquisada = new HashMap<>();
		
		List<String> mensagens  = new ArrayList<>();
		mensagens.add("Emails enviado com sucesso!");
		
		filtro.setPesquisarCotasParaImpressao(true);
		List<NotasCotasImpressaoNfeDTO> listaCotasImpressaoNFe = impressaoNFEService.obterNotafiscalImpressao(filtro);
		
		for (NotasCotasImpressaoNfeDTO notaDTO : listaCotasImpressaoNFe) {
			Cota cota = this.cotaService.obterPorId(notaDTO.getIdCota());
			
			if(mapNumeroNotasAgrupadasPorCota.get(cota.getNumeroCota()) == null){
				
				List<Long> numeroIdNotas = new ArrayList<>();
				numeroIdNotas.add(notaDTO.getIdNota());
				
				mapNumeroNotasAgrupadasPorCota.put(cota.getNumeroCota(), numeroIdNotas);
				mapCotaPesquisada.put(cota.getNumeroCota(), cota);
				
			}else{
				mapNumeroNotasAgrupadasPorCota.get(cota.getNumeroCota()).add(notaDTO.getIdNota());
			}
			
		}
		
		for (Integer numeroCota : mapNumeroNotasAgrupadasPorCota.keySet()) {
			Cota cota = mapCotaPesquisada.get(numeroCota);
			
			if(cota.getParametrosCotaNotaFiscalEletronica() == null || cota.getParametrosCotaNotaFiscalEletronica().getEmailNotaFiscalEletronica() == null){
				mensagens.add("Erro ao enviar email para a cota: " + cota.getNumeroCota());
				continue;
			}

			List<Long> idNotas = new ArrayList<>();
			
			idNotas.addAll(mapNumeroNotasAgrupadasPorCota.get(numeroCota));
			
			filtro.setNumerosNotas(idNotas);
			
			String[] listaDeDestinatarios = {cota.getParametrosCotaNotaFiscalEletronica().getEmailNotaFiscalEletronica()};
			
			byte[] report = this.impressaoNFEService.imprimirNFe(filtro);
			
			String nomeAnexo = "NF-e - Data "+DateUtil.formatarDataPTBR(new Date())+" - Cota "+numeroCota;
			
			AnexoEmail anexoPDF = new AnexoEmail(nomeAnexo, report, TipoAnexo.PDF);
			
			try {
				emailSerice.enviar("[NDS] - Impressão "+nomeAnexo, "Olá, a NF-e segue anexo.", listaDeDestinatarios, anexoPDF);
				System.out.println("Email enviado, impressao NFE, cota - "+numeroCota);
				
			} catch (AutenticacaoEmailException e) {
				mensagens.add("Erro ao enviar email para a cota: " + cota.getNumeroCota());
			}
		}
		
		if (mensagens.isEmpty()) {
			result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Email enviado com sucesso."), Constantes.PARAM_MSGS).recursive().serialize();
		}else{
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagens));
		}
		
	}
	
	/**
	 * Metodos auxiliares
	 *
	 * @param idRoteiro
	 * @param sortname
	 * @param ordenacao
	 */
	@Post
	public void carregarRotasImpressaoNFE(Long idRoteiro, String sortname, Ordenacao ordenacao) {
		
		List<ItemDTO<Long, String>> listaItensRotas = new ArrayList<ItemDTO<Long,String>>();

		for (Rota rota : (idRoteiro != null && idRoteiro > -1) ? rotaService.buscarRotaPorRoteiro(idRoteiro) : rotaService.obterRotas()) {

			ItemDTO<Long, String> rotaVO = new ItemDTO<Long, String>();

			rotaVO.setKey(rota.getId());
			rotaVO.setValue(rota.getDescricaoRota());

			listaItensRotas.add(rotaVO);
		}

		this.result.include("rotas", (idRoteiro != null && idRoteiro > -1) ? rotaService.buscarRotaPorRoteiro(idRoteiro) : rotaService.obterRotas());

		result.use(Results.json()).withoutRoot().from(listaItensRotas).recursive().serialize();
	}
	
	/**
	 * Metodo utilitario para escrever arquivo em pdf
	 */
	private void escreverArquivoParaResponse(byte[] arquivo, String nomeArquivo) {
		
		this.httpResponse.setContentType("application/pdf");

		this.httpResponse.setHeader("Content-Disposition", "attachment; filename="+ nomeArquivo +".pdf");

		OutputStream output;
		try {
			
			output = this.httpResponse.getOutputStream();
			
			output.write(arquivo);

			this.httpResponse.getOutputStream().flush();
			
			this.httpResponse.getOutputStream().close();
			
			result.use(Results.nothing());
			
			if(!this.httpResponse.isCommitted()) {
				System.out.println("olá");
			}
			
		} catch (IOException e) {
			throw new ValidacaoException(TipoMensagem.WARNING,"Erro ao gerar relatorio");
		}
	}
}
