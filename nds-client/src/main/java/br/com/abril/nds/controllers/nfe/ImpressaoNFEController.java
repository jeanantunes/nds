package br.com.abril.nds.controllers.nfe;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.NfeVO;
import br.com.abril.nds.dto.CotasImpressaoNfeDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroImpressaoNFEDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoEmissaoNfe;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.TipoUsuarioNotaFiscal;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ImpressaoNFEService;
import br.com.abril.nds.service.MonitorNFEService;
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
import br.com.caelum.vraptor.Get;
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
	private MonitorNFEService monitorNFEService; 

	@Autowired
	ImpressaoNFEService impressaoNFEService;

	@Autowired
	DistribuidorService distribuidorService;

	@Autowired
	RoteiroService roteiroService;

	@Autowired
	RotaService rotaService;

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

		TableModel<CellModelKeyValue<CotasImpressaoNfeDTO>> tableModel = new TableModel<CellModelKeyValue<CotasImpressaoNfeDTO>>();

		List<CotasImpressaoNfeDTO> listaCotasImpressaoNFe = impressaoNFEService.buscarCotasParaImpressaoNFe(filtro);
		
		//TODO: Sérgio - Retirar - usado apenas para marcar a tela
		for(CotasImpressaoNfeDTO nnnn : listaCotasImpressaoNFe) {
			if(nnnn.getIdCota().longValue() > 3)
				nnnn.setNotaImpressa(true);
		}
		
		tableModel.setTotal(impressaoNFEService.buscarNFeParaImpressaoTotalQtd(filtro));

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaCotasImpressaoNFe));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}

	@Post
	public void pesquisarProdutosImpressaoNFE(String sortname, String sortorder, String codigoProduto, String nomeProduto, int page, int rp) {

		List<Fornecedor> fornecedores = this.fornecedorService.obterFornecedores(true, SituacaoCadastro.ATIVO);

		TableModel<CellModelKeyValue<ProdutoLancamentoDTO>> tableModel = new TableModel<CellModelKeyValue<ProdutoLancamentoDTO>>();

		Calendar c = Calendar.getInstance();
		c.set(Calendar.AM_PM, 0);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		List<ProdutoLancamentoDTO> listaProdutoLancamentoUnordered = impressaoNFEService.obterProdutosExpedicaoConfirmada(fornecedores, c.getTime()); // c.getTime()

		if(listaProdutoLancamentoUnordered == null) {
			result.use(Results.nothing());
			return;
		}
			
		ordenarLista(sortname, sortorder, listaProdutoLancamentoUnordered);
		
		List<ProdutoLancamentoDTO> listaProdutoLancamentoRefinada = buscarProdutosNaLista(listaProdutoLancamentoUnordered, codigoProduto, nomeProduto);

		List<ProdutoLancamentoDTO> listaProdutoLancamento = removerItensDuplicados(listaProdutoLancamentoRefinada);
		
		ordenarLista(sortname, sortorder, listaProdutoLancamento);

		tableModel.setTotal(listaProdutoLancamento != null ? listaProdutoLancamento.size() : 0);

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaProdutoLancamento));

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
	@Get
	public void exportar(FileType fileType) throws IOException {

		FiltroImpressaoNFEDTO filtro = (FiltroImpressaoNFEDTO) session.getAttribute("filtroPesquisaNFe");
		
		List<CotasImpressaoNfeDTO> listaNFeDTO = impressaoNFEService.buscarCotasParaImpressaoNFe(filtro);
		
		List<NfeVO> listaNFeVO = new ArrayList<NfeVO>();
		/*for(CotasImpressaoNfeDTO nfeDTO : listaNFeDTO) {
			
			List<NfeDTO> listaNotaFisal = notaFiscalService.pesquisarNotaFiscal(filtro);
			NfeVO nfe = new NfeVO();
			nfe.setIdNotaFiscal(nfeDTO.getIdNotaFiscal());
			listaNFeVO.add(nfe);
		}
		*/
		FileExporter.to("nfe", fileType).inHTTPResponse(
				this.getNDSFileHeader(), 
				filtro, 
				null, 
				listaNFeVO,
				NfeVO.class, this.httpResponse);
		
	}
	
	@Get
	public void imprimirNFe(String sortorder, String sortname) {
		
		FiltroImpressaoNFEDTO filtro = (FiltroImpressaoNFEDTO) session.getAttribute("filtroPesquisaNFe");
		
		List<CotasImpressaoNfeDTO> listaNFeDTO = impressaoNFEService.buscarCotasParaImpressaoNFe(filtro);
		
		List<NfeVO> listaNFeVO = new ArrayList<NfeVO>();
		for(CotasImpressaoNfeDTO nfeDTO : listaNFeDTO) {
			NfeVO nfe = new NfeVO();
			nfe.setIdNotaFiscal(nfeDTO.getIdNotaFiscal());
			listaNFeVO.add(nfe);
		}
		
		byte[] danfeBytes = monitorNFEService.obterDanfes(listaNFeVO, false);
		
		try {
			
			escreverArquivoParaResponse(danfeBytes, "danfes");
			
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
	 * @param sortname
	 * @param sortorder
	 * @param listaProdutoLancamento
	 */
	@SuppressWarnings("unchecked")
	private void ordenarLista(String sortname, String sortorder, List<ProdutoLancamentoDTO> listaProdutoLancamento) {
		
		if(listaProdutoLancamento == null)
			return;
		
		if(sortname != null && !sortname.isEmpty())
			Collections.sort(listaProdutoLancamento, new BeanComparator(sortname, new NullComparator()));

		if(sortorder != null && !sortorder.isEmpty() && sortorder.equals("desc"))
			Collections.reverse(listaProdutoLancamento);
	}

	private List<ProdutoLancamentoDTO> removerItensDuplicados(List<ProdutoLancamentoDTO> listaProdutoLancamentoUnordered) {
		
		if(listaProdutoLancamentoUnordered == null)
			return listaProdutoLancamentoUnordered;
		
		Set<ProdutoLancamentoDTO> s = new TreeSet<ProdutoLancamentoDTO>(new Comparator<ProdutoLancamentoDTO>() {
			@Override
			public int compare(ProdutoLancamentoDTO o1, ProdutoLancamentoDTO o2) {
				if(o1.getCodigoProduto().equalsIgnoreCase(o2.getCodigoProduto()))
					return 0;
				
				return -1;
			}
		});

		s.addAll(listaProdutoLancamentoUnordered);

		List<ProdutoLancamentoDTO> listaProdutoLancamento = new ArrayList<ProdutoLancamentoDTO>(s);

		return listaProdutoLancamento;
	}

	private List<ProdutoLancamentoDTO> buscarProdutosNaLista(List<ProdutoLancamentoDTO> listaProdutoLancamento, String codigoProduto, String nomeProduto) {

		if( StringUtils.isEmpty(codigoProduto) && StringUtils.isEmpty(nomeProduto) ) {
			return listaProdutoLancamento;
		}
		
		StringUtils.isEmpty(nomeProduto);
		
		List<ProdutoLancamentoDTO> result = new ArrayList<ProdutoLancamentoDTO>();
		
		for( ProdutoLancamentoDTO pl : listaProdutoLancamento ) {
			if( !StringUtils.isEmpty(codigoProduto) && !StringUtils.isEmpty(nomeProduto) ) {
				if( StringUtils.startsWithIgnoreCase(pl.getCodigoProduto(), codigoProduto) 
						&& StringUtils.startsWithIgnoreCase(pl.getNomeProduto(), nomeProduto) ) { 
					result.add( pl );
				}
			} else if( !StringUtils.isEmpty(codigoProduto) && StringUtils.isEmpty(nomeProduto) ) { 
				if( StringUtils.startsWithIgnoreCase(pl.getCodigoProduto(), codigoProduto) ) {
					result.add( pl );
				}
			} else if( !StringUtils.isEmpty(nomeProduto) && StringUtils.isEmpty(codigoProduto) ) { 
				if( StringUtils.startsWithIgnoreCase(pl.getNomeProduto(), nomeProduto) ) {
					result.add( pl );
				}
			}
		}
		return result;
	}
	
	private void escreverArquivoParaResponse(byte[] arquivo, String nomeArquivo) throws IOException {
		
		this.httpResponse.setContentType("application/pdf");
		
		this.httpResponse.setHeader("Content-Disposition", "attachment; filename="+nomeArquivo +".pdf");

		OutputStream output = this.httpResponse.getOutputStream();
		
		output.write(arquivo);

		httpResponse.getOutputStream().close();
		
		result.use(Results.nothing());
		
	}

}
