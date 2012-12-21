package br.com.abril.nds.controllers.expedicao;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.ConsultaNotaEnvioDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaEnvioDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.CotaAusenteService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.GeracaoNotaEnvioService;
import br.com.abril.nds.service.MovimentoEstoqueCotaService;
import br.com.abril.nds.service.NFeService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.Intervalo;
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
@Path("/expedicao/geracaoNotaEnvio")
public class GeracaoNotaEnvioController {

	@Autowired
	private Result result;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private GeracaoNotaEnvioService geracaoNotaEnvioService;
		
	@Autowired
	private RoteirizacaoService roteirizacaoService;
	
	@Autowired
	private MovimentoEstoqueCotaService movimentoEstoqueCotaService;
	
	@Autowired
	private NotaFiscalService notaFiscalService;
	
	@Autowired
	private CotaAusenteService cotaAusenteService;

	@Autowired
	private NFeService nfeService;

	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@Autowired
	private HttpSession session;

	@Autowired
	private HttpServletResponse httpResponse;

	private static final String FILTRO_CONSULTA_NOTA_ENVIO = "filtroConsultaNotaEnvio";
	
	
	@Path("/")
	@Rules(Permissao.ROLE_EXPEDICAO_GERACAO_NOTA_ENVIO)
	public void index() {
		
		result.include("fornecedores",  fornecedorService.obterFornecedoresIdNome(SituacaoCadastro.ATIVO, true));
		
		List<Roteiro> roteiros = this.roteirizacaoService.buscarRoteiro(null, null);
		
		List<ItemDTO<Long, String>> listRoteiro = new ArrayList<ItemDTO<Long,String>>();
		
		for (Roteiro roteiro : roteiros){
			
			listRoteiro.add(new ItemDTO<Long, String>(roteiro.getId(), roteiro.getDescricaoRoteiro()));
		}
		
		result.include("roteiros", listRoteiro);
		
		List<Rota> rotas = this.roteirizacaoService.buscarRota(null, null);
		
		List<ItemDTO<Long, String>> listRota = new ArrayList<ItemDTO<Long,String>>();
		
		for (Rota rota : rotas){
			
			listRota.add(new ItemDTO<Long, String>(rota.getId(), rota.getDescricaoRota()));
		}
		
		result.include("rotas", listRota);
	}
	
	@Post
	public void pesquisar(Integer intervaloBoxDe, Integer intervaloBoxAte,
			Integer intervaloCotaDe, Integer intervaloCotaAte,
			Date intervaloMovimentoDe, Date intervaloMovimentoAte, Date dataEmissao,
			List<Long> listaIdFornecedores, Long idRoteiro, Long idRota,
			String sortname, String sortorder, int rp, int page) {
				
		if(listaIdFornecedores==null || listaIdFornecedores.isEmpty())
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum fornecedor foi selecionado.");
		
		FiltroConsultaNotaEnvioDTO filtro = 
				this.setFiltroNotaEnvioSessao(intervaloBoxDe, intervaloBoxAte, intervaloCotaDe, 
						intervaloCotaAte, intervaloMovimentoDe, intervaloMovimentoAte, dataEmissao, 
						listaIdFornecedores, idRoteiro, idRota, sortname, sortorder, rp, page);
		
		List<ConsultaNotaEnvioDTO> listaCotaExemplares = 
				this.geracaoNotaEnvioService.busca(filtro);
		
		filtro.setPaginacaoVO(new PaginacaoVO());
		
		Integer qtdResult = geracaoNotaEnvioService.buscaCotasNotasDeEnvioQtd(filtro);
		
		result.use(FlexiGridJson.class).from(listaCotaExemplares).page(page).total(qtdResult).serialize();
	}
	
	@Post
	public void hasCotasAusentes() {
		
		boolean hasCotasAusentes = false;
		
		FiltroConsultaNotaEnvioDTO filtro = this.getFiltroNotaEnvioSessao();
		
		filtro.setCadastro(SituacaoCadastro.SUSPENSO);
		filtro.setPaginacaoVO(new PaginacaoVO());
		
		List<ConsultaNotaEnvioDTO> cotasAusentes =
				geracaoNotaEnvioService.busca(filtro);
		
		if (cotasAusentes != null && !cotasAusentes.isEmpty())
			hasCotasAusentes = true;
		
		result.use(CustomJson.class).from(hasCotasAusentes).serialize();
	}
	
	@Post
	public void exportar(FileType fileType) throws IOException {
		
		FiltroConsultaNotaEnvioDTO filtro = this.getFiltroNotaEnvioSessao();
		
		filtro.setPaginacaoVO(new PaginacaoVO());
		
		List<ConsultaNotaEnvioDTO> consultaNotaEnvioDTO =	
				geracaoNotaEnvioService.busca(filtro);
		
		FileExporter.to("consignado-encalhe", fileType).inHTTPResponse(
				this.getNDSFileHeader(), filtro, null,
				consultaNotaEnvioDTO, ConsultaNotaEnvioDTO.class,
				this.httpServletResponse);
		
		result.use(Results.nothing());
	}
	
	@Post
	public void transferirSuplementar(List<Long> listaIdCotas) {
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		FiltroConsultaNotaEnvioDTO filtro = this.getFiltroNotaEnvioSessao();
		
		this.movimentoEstoqueCotaService.transferirReparteParaSuplementar(distribuidor, listaIdCotas, 
				filtro.getIntervaloMovimento(), filtro.getIdFornecedores(), null, null);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, 
				"Transferência de reparte para suplementar realizada com sucesso."),
							Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	@Post
	public void gerarNotaEnvio(List<Long> listaIdCotas) {
		
		FiltroConsultaNotaEnvioDTO filtro = this.getFiltroNotaEnvioSessao();
		
		List<NotaEnvio> notasEnvio = this.geracaoNotaEnvioService.gerarNotasEnvio(filtro, listaIdCotas);

		byte[] notasGeradas = nfeService.obterNEsPDF(notasEnvio, false); 
	        
        if (notasGeradas != null) {
        	
        	DateFormat sdf = new SimpleDateFormat("yyyy-MM-ddhhmmss");
        	
        	this.httpResponse.setHeader("Content-Disposition", "attachment; filename=notas-envio" + sdf.format(new Date()) + ".pdf");

        	OutputStream output;
			try {
				output = this.httpResponse.getOutputStream();

	        	output.write(notasGeradas);

	        	httpResponse.getOutputStream().close();

	        	result.use(Results.nothing());

			} catch (IOException e) {
				throw new ValidacaoException(TipoMensagem.ERROR, e.getMessage());
			}

        }
	}
	
	/**
	 * Obtem o filtro da sessão
	 * 
	 * @return filtro
	 */
	private FiltroConsultaNotaEnvioDTO getFiltroNotaEnvioSessao() {
		
		FiltroConsultaNotaEnvioDTO filtro = 
				(FiltroConsultaNotaEnvioDTO) this.session.getAttribute(FILTRO_CONSULTA_NOTA_ENVIO);
		
		if (filtro == null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Não foram encontrado itens para exportar"));
		}
		
		return filtro;
	}
	
	
	private FiltroConsultaNotaEnvioDTO setFiltroNotaEnvioSessao(Integer intervaloBoxDe, Integer intervaloBoxAte,
			Integer intervaloCotaDe, Integer intervaloCotaAte,
			Date intervaloMovimentoDe, Date intervaloMovimentoAte, Date dataEmissao,
			List<Long> listaIdFornecedores, Long idRoteiro, Long idRota,
			String sortname, String sortorder, int rp, int page) {
		
		Intervalo<Integer> intervaloBox = new Intervalo<Integer>(intervaloBoxDe, intervaloBoxAte);
		
		Intervalo<Integer> intervaloCota = new Intervalo<Integer>(intervaloCotaDe, intervaloCotaAte);
		
		Intervalo<Date> intervaloDateMovimento = new Intervalo<Date>(intervaloMovimentoDe, intervaloMovimentoAte);
		
		PaginacaoVO paginacao = new PaginacaoVO();
		
		paginacao.setOrdenacao(Ordenacao.ASC);
		paginacao.setPaginaAtual(page);
		paginacao.setQtdResultadosPorPagina(rp);
		paginacao.setSortOrder(sortorder);
		paginacao.setSortColumn(sortname);
		
		FiltroConsultaNotaEnvioDTO filtroConsultaNotaEnvioDTO = new FiltroConsultaNotaEnvioDTO();
		
		filtroConsultaNotaEnvioDTO.setDataEmissao(dataEmissao);
		filtroConsultaNotaEnvioDTO.setIdFornecedores(listaIdFornecedores);
		filtroConsultaNotaEnvioDTO.setIdRota(idRota);
		filtroConsultaNotaEnvioDTO.setIdRoteiro(idRoteiro);
		filtroConsultaNotaEnvioDTO.setIntervaloBox(intervaloBox);
		filtroConsultaNotaEnvioDTO.setIntervaloCota(intervaloCota);
		filtroConsultaNotaEnvioDTO.setIntervaloMovimento(intervaloDateMovimento);
		filtroConsultaNotaEnvioDTO.setPaginacaoVO(paginacao);
		
		session.setAttribute(FILTRO_CONSULTA_NOTA_ENVIO, filtroConsultaNotaEnvioDTO);
		
		return filtroConsultaNotaEnvioDTO;
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

			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica()
					.getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica()
					.getCnpj());
		}

		ndsFileHeader.setData(new Date());

		ndsFileHeader.setNomeUsuario(this.getUsuario().getNome());

		return ndsFileHeader;
	}

	// TODO: não há como reconhecer usuario, ainda
	private Usuario getUsuario() {

		Usuario usuario = new Usuario();

		usuario.setId(1L);

		usuario.setNome("Jornaleiro da Silva");

		return usuario;
	}
	
	@Get
	public void visualizarNE(){
		
		FiltroConsultaNotaEnvioDTO filtro = this.getFiltroNotaEnvioSessao();
		
		NotaEnvio notaEnvio = geracaoNotaEnvioService.visualizar(filtro.getIntervaloCota().getDe(), 
				null, null, null, null, filtro.getDataEmissao(), filtro.getIntervaloMovimento(), filtro.getIdFornecedores());
		
		result.include("notaEnvio",notaEnvio);
		
	}
	
}
