package br.com.abril.nds.controllers.nfe;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.NotasCotasImpressaoNfeDTO;
import br.com.abril.nds.dto.ProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroImpressaoNFEDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoEmissaoNfe;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.TipoUsuarioNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ImpressaoNFEService;
import br.com.abril.nds.service.NFeService;
import br.com.abril.nds.service.RotaService;
import br.com.abril.nds.service.RoteiroService;
import br.com.abril.nds.service.TipoNotaFiscalService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
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
public class ImpressaoNFEController {

	@Autowired
	private HttpSession session;

	@Autowired
	private HttpServletRequest httpRequest;

	@Autowired
	private HttpServletResponse httpResponse;

	@Autowired
	private FornecedorService fornecedorService;

	@Autowired
	private TipoNotaFiscalService tipoNotaFiscalService;

	@Autowired
	private NFeService nfeService; 

	@Autowired
	private ImpressaoNFEService impressaoNFEService;

	@Autowired
	private DistribuidorService distribuidorService;

	@Autowired
	private RoteiroService roteiroService;

	@Autowired
	private RotaService rotaService;

	@Autowired
	private Result result;

	@Path("/")
	@Rules(Permissao.ROLE_NFE_IMPRESSAO_NFE)
	public void index() {

		List<Fornecedor> fornecedores = this.fornecedorService.obterFornecedores(true, SituacaoCadastro.ATIVO);

		GrupoNotaFiscal[] gnf = {GrupoNotaFiscal.NF_REMESSA_CONSIGNACAO, GrupoNotaFiscal.NF_VENDA};
		this.result.include("tipoNotas", tipoNotaFiscalService.carregarComboTiposNotasFiscais(TipoOperacao.SAIDA, TipoUsuarioNotaFiscal.COTA, TipoUsuarioNotaFiscal.DISTRIBUIDOR, gnf));
		this.result.include("fornecedores", fornecedores);
		this.result.include("roteiros", roteiroService.obterRoteiros());
		this.result.include("rotas", rotaService.obterRotas());
		this.result.include("tipoEmissao", TipoEmissaoNfe.values());
		this.result.include("dataAtual", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

	}

	@Post
	public void pesquisarImpressaoNFE(FiltroImpressaoNFEDTO filtro, String sortorder, String sortname, int page, int rp){

		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));

		if(!filtro.isFiltroValido()) {

			List<String> listaMensagemValidacao = new ArrayList<String>();

			HashMap<String, String> erros = filtro.validarFiltro();
			for(Iterator<String> i = erros.keySet().iterator(); i.hasNext(); ) {
				listaMensagemValidacao.add( erros.get( i.next() ) );
			}

			if (!listaMensagemValidacao.isEmpty()) {
				ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR, listaMensagemValidacao);
				throw new ValidacaoException(validacaoVO);
			}

			result.use(Results.nothing());
		}

		session.setAttribute("filtroPesquisaNFe", filtro);

		TableModel<CellModelKeyValue<NotasCotasImpressaoNfeDTO>> tableModel = new TableModel<CellModelKeyValue<NotasCotasImpressaoNfeDTO>>();

		List<NotasCotasImpressaoNfeDTO> listaCotasImpressaoNFe = impressaoNFEService.buscarCotasParaImpressaoNFe(filtro);

		tableModel.setTotal(impressaoNFEService.buscarNFeParaImpressaoTotalQtd(filtro));

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaCotasImpressaoNFe));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();

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
	public void exportar(FileType fileType) throws IOException {

		FiltroImpressaoNFEDTO filtro = (FiltroImpressaoNFEDTO) session.getAttribute("filtroPesquisaNFe");

		filtro.setPaginacao(null);

		List<NotasCotasImpressaoNfeDTO> listaNFeDTO = impressaoNFEService.buscarCotasParaImpressaoNFe(filtro);

		FileExporter.to("cotas", fileType).inHTTPResponse(
				this.getNDSFileHeader(), 
				null, 
				null, 
				listaNFeDTO,
				NotasCotasImpressaoNfeDTO.class, this.httpResponse);

	}

	@Post
	public void imprimirNFe(FiltroImpressaoNFEDTO filtro, String sortorder, String sortname) {


		FiltroImpressaoNFEDTO filtroPesquisa = (FiltroImpressaoNFEDTO) session.getAttribute("filtroPesquisaNFe");

		if(filtro.getNumerosNotas() != null) {
			filtroPesquisa.setNumerosNotas(filtro.getNumerosNotas());
		} else {
			ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR, "Devem ser informadas as cotas para impressão.");
			throw new ValidacaoException(validacaoVO);
		}

		filtroPesquisa.setPaginacao(null);
		//List<NotasCotasImpressaoNfeDTO> cotas = impressaoNFEService.buscarCotasParaImpressaoNFe(filtroPesquisa);

		Distribuidor distribuidor = this.distribuidorService.obter();

		byte[] arquivo = null; 
		String nomeArquivo = "";

		if(distribuidor.getObrigacaoFiscal() != null) {

			List<NotaFiscal> nfs = impressaoNFEService.buscarNotasParaImpressaoNFe(filtroPesquisa);

			switch(distribuidor.getTipoImpressaoNENECADANFE()) {

				case DANFE:
					arquivo = nfeService.obterDanfesPDF(nfs, false);
					nomeArquivo = "danfes";
					break;
				default:
					throw new ValidationException("O tipo de impressão configurado no Distribuidor não está disponível.");
					
			}

			// Atualiza a flag que informa se a nota ja foi impressa
			for(NotaFiscal nf : nfs) {
				NotaFiscal nfOrig = nfeService.obterNotaFiscalPorId(nf);
				nfOrig.setNotaImpressa(true);
				nfeService.mergeNotaFiscal(nfOrig);
			}

		} else {

			List<NotaEnvio> nes = impressaoNFEService.buscarNotasEnvioParaImpressaoNFe(filtroPesquisa);

			switch(distribuidor.getTipoImpressaoNENECADANFE()) {

				case MODELO_1:
					arquivo = nfeService.obterNEsPDF(nes, false);
					nomeArquivo = "NEs";
					break;
				case MODELO_2:
					arquivo = nfeService.obterNEsPDF(nes, true);
					nomeArquivo = "NECAs";
					break;

			}

			// Atualiza a flag que informa se a nota ja foi impressa
			for(NotaEnvio ne : nes) {
				NotaEnvio neOrig = nfeService.obterNotaEnvioPorId(ne);
				neOrig.setNotaImpressa(true);
				nfeService.mergeNotaEnvio(neOrig);
			}

		}

		try {

			escreverArquivoParaResponse(arquivo, nomeArquivo);

		} catch(IOException e) {

			throw new ValidacaoException(TipoMensagem.ERROR, "Falha na geração do arquivo.");

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

	/*
	 * Obtém os dados do cabeçalho de exportação.
	 * 
	 * @return NDSFileHeader
	 */
	private NDSFileHeader getNDSFileHeader() {

		NDSFileHeader ndsFileHeader = new NDSFileHeader();

		Distribuidor distribuidor = this.distribuidorService.obter();

		if (distribuidor != null) {

			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica().getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica().getCnpj());
		}

		ndsFileHeader.setData(new Date());

		ndsFileHeader.setNomeUsuario(httpRequest.getRemoteUser());

		return ndsFileHeader;
	}

	/**
	 * Metodos utilitarios
	 * 
	 */

	private void escreverArquivoParaResponse(byte[] arquivo, String nomeArquivo) throws IOException {

		this.httpResponse.setContentType("application/pdf");

		this.httpResponse.setHeader("Content-Disposition", "attachment; filename="+nomeArquivo +".pdf");

		OutputStream output = this.httpResponse.getOutputStream();

		output.write(arquivo);

		httpResponse.getOutputStream().close();

		result.use(Results.nothing());

	}

}
