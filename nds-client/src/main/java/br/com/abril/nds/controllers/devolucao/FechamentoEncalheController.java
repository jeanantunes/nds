package br.com.abril.nds.controllers.devolucao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.AnaliticoEncalheVO;
import br.com.abril.nds.dto.AnaliticoEncalheDTO;
import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.FechamentoEncalheService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Classe responsável pelo controle das ações referentes à
 * tela de chamadão de publicações.
 * 
 * @author Discover Technology
 */
@Resource
@Path("devolucao/fechamentoEncalhe")
public class FechamentoEncalheController {

	@Autowired
	private Result result;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private BoxService boxService;
	
	@Autowired
	private FechamentoEncalheService fechamentoEncalheService;

	@Autowired
	private HttpServletResponse response;
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Path("/")
	@Rules(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE)
	public void index() {
		
		Distribuidor dist = distribuidorService.obter();
		List<Fornecedor> listaFornecedores = fornecedorService.obterFornecedores();
		List<Box> listaBoxes = boxService.buscarPorTipo(TipoBox.ENCALHE);
		
		result.include("dataOperacao", DateUtil.formatarDataPTBR(dist.getDataOperacao()));
		result.include("listaFornecedores", listaFornecedores);
		result.include("listaBoxes", listaBoxes);
	}
	
	@Path("/pesquisar")
	public void pesquisar(String dataEncalhe, Long fornecedorId, Long boxId, Boolean aplicaRegraMudancaTipo,
			String sortname, String sortorder, int rp, int page) {
		
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		filtro.setDataEncalhe(DateUtil.parseDataPTBR(dataEncalhe));
		filtro.setFornecedorId(fornecedorId);
		filtro.setBoxId(boxId);
		
		if (aplicaRegraMudancaTipo){
			if (boxId == null) {
				fechamentoEncalheService.converteFechamentoDetalhadoEmConsolidado(filtro);
			} else {
				FiltroFechamentoEncalheDTO filtroRevomecao = new FiltroFechamentoEncalheDTO(); 
				filtroRevomecao.setDataEncalhe(DateUtil.parseDataPTBR(dataEncalhe));
				fechamentoEncalheService.removeFechamentoDetalhado(filtroRevomecao);
			}
			
		} 
		
		
		List<FechamentoFisicoLogicoDTO> listaEncalhe = fechamentoEncalheService.buscarFechamentoEncalhe(filtro, sortorder, this.resolveSort(sortname), page, rp);
		
		this.result.use(FlexiGridJson.class).from(listaEncalhe).total(listaEncalhe.size()).page(page).serialize();
	}
	
	
	@Path("/salvar")
	public void salvar(List<FechamentoFisicoLogicoDTO> listaFechamento, String dataEncalhe, Long fornecedorId, Long boxId) {
		
		gravaFechamentoEncalhe(listaFechamento, dataEncalhe, fornecedorId,
				boxId);
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Informação gravada com sucesso!"), "result").recursive().serialize();
	}
	
	@Path("/cotasAusentes")
	public void cotasAusentes(Date dataEncalhe,
			String sortname, String sortorder, int rp, int page) {
		
		List<CotaAusenteEncalheDTO> listaCotasAusenteEncalhe =
			this.fechamentoEncalheService.buscarCotasAusentes(dataEncalhe, sortorder, sortname, page, rp);
		
		int total = this.fechamentoEncalheService.buscarTotalCotasAusentes(dataEncalhe);
		
		if (listaCotasAusenteEncalhe == null || listaCotasAusenteEncalhe.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma cota ausente!");
		}
		
		this.result.use(FlexiGridJson.class).from(listaCotasAusenteEncalhe).total(total).page(page).serialize();
	}
	
	@Path("carregarDataPostergacao")
	public void carregarDataPostergacao(Date dataEncalhe, Date dataPostergacao) {
		
		try {
			
			int quantidadeDias = 0;
			
			if (dataPostergacao == null) {
				quantidadeDias = 1;
				dataPostergacao = dataEncalhe;
			}
			
			
			dataPostergacao = 
				this.calendarioService.adicionarDiasRetornarDiaUtil(dataPostergacao, quantidadeDias);
			
			if (dataPostergacao != null) {
				String dataFormatada = DateUtil.formatarData(dataPostergacao, "dd/MM/yyyy");
				this.result.use(Results.json()).from(dataFormatada, "result").recursive().serialize();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Path("/postergarCotas")
	public void postergarCotas(Date dataPostergacao, Date dataEncalhe, List<Long> idsCotas) {
		
		if (dataEncalhe != null && dataEncalhe.after(dataPostergacao)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Postergação não pode ser realizada antes da data atual!");
		}
		
		try {
			
			this.fechamentoEncalheService.postergarCotas(dataEncalhe, dataPostergacao, idsCotas);
			
		} catch (Exception e) {
			this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.ERROR, "Erro ao tentar postergar!"), "result").recursive().serialize();
			throw new ValidacaoException();
		}
		
		this.result.use(Results.json()).from(
			new ValidacaoVO(TipoMensagem.SUCCESS, "Cotas postergadas com sucesso!"), "result").recursive().serialize();
	}

	@Path("/cobrarCotas")
	public void cobrarCotas(Date dataOperacao, List<Long> idsCotas) {

		if (idsCotas == null || idsCotas.isEmpty()) {
			this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.WARNING, "Selecine pelo menos uma Cota para cobrar!"), "result").recursive().serialize();
			throw new ValidacaoException();
		}
		
		try {
			
			this.fechamentoEncalheService.cobrarCotas(dataOperacao, obterUsuario(), idsCotas);
			
		} catch (ValidacaoException e) {
			this.result.use(Results.json()).from(e.getValidacao(), "result").recursive().serialize();
			throw new ValidacaoException();
		} catch (Exception e) {
			this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.ERROR, "Erro ao tentar cobrar!"), "result").recursive().serialize();
			throw new ValidacaoException();
		}
		
		this.result.use(Results.json()).from(
			new ValidacaoVO(TipoMensagem.SUCCESS, "Cotas cobradas com sucesso!"), "result").recursive().serialize();		
	}

	@Get
	@Path("/exportarArquivo")
	public void exportarArquivo(Date dataEncalhe, String sortname, String sortorder, 
			int rp, int page, FileType fileType) {

		List<CotaAusenteEncalheDTO> listaCotasAusenteEncalhe =
			this.fechamentoEncalheService.buscarCotasAusentes(dataEncalhe, sortorder, sortname, page, rp);

		if (listaCotasAusenteEncalhe != null && !listaCotasAusenteEncalhe.isEmpty()) {
		
			try {
					
				FileExporter.to("cotas-ausentes", fileType).inHTTPResponse(
					this.getNDSFileHeader(), null, null, listaCotasAusenteEncalhe, 
				CotaAusenteEncalheDTO.class, this.response);
				
			} catch (Exception e) {
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Erro ao gerar o arquivo!"));
			}
		}
	
		this.result.use(Results.nothing());
	}

	@Get
	@Path("/imprimirArquivo")
	public void imprimirArquivo(Date dataEncalhe, Long fornecedorId, Long boxId,
			String sortname, String sortorder, int rp, int page, FileType fileType) {
		
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		filtro.setDataEncalhe(dataEncalhe);
		filtro.setFornecedorId(fornecedorId);
		filtro.setBoxId(boxId);
		
		List<FechamentoFisicoLogicoDTO> listaEncalhe = fechamentoEncalheService.buscarFechamentoEncalhe(
				filtro, sortorder, this.resolveSort(sortname), page, rp);
		
		if (listaEncalhe != null && !listaEncalhe.isEmpty()) {
		
			try {
				
				FileExporter.to("fechamentos-encalhe", fileType).inHTTPResponse(
					this.getNDSFileHeader(), null, null, listaEncalhe, 
					FechamentoFisicoLogicoDTO.class, this.response);
				
			} catch (Exception e) {
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Erro ao gerar o arquivo!"));
			}
		}
		
		this.result.use(Results.nothing());
	}

	@Path("/encerrarOperacaoEncalhe")
	public void encerrarOperacaoEncalhe(Date dataEncalhe) {
		
		try {
		
			if (dataEncalhe == null || Calendar.getInstance().getTime().before(dataEncalhe)) {
				this.result.use(Results.json()).from(
					new ValidacaoVO(TipoMensagem.WARNING, "Data de encalhe inválida!"), "result").recursive().serialize();
				throw new ValidacaoException();
			}
			
			this.fechamentoEncalheService.encerrarOperacaoEncalhe(dataEncalhe);
			
		} catch (ValidacaoException e) {
			this.result.use(Results.json()).from(e.getValidacao(), "result").recursive().serialize();
			throw new ValidacaoException();
		} catch (Exception e) {
			this.result.use(
				Results.json()).from(
					new ValidacaoVO(TipoMensagem.ERROR, "Erro ao tentar encerrar a operação de encalhe!"), "result").recursive().serialize();
			throw new ValidacaoException();
		}
		
		this.result.use(Results.json()).from(
			new ValidacaoVO(TipoMensagem.SUCCESS, "Operação de encalhe encerrada com sucesso!"), "result").recursive().serialize();
	}
	
	@Path("/verificarEncerrarOperacaoEncalhe")
	public void verificarEncerrarOperacaoEncalhe(Date dataEncalhe, String operacao) {
		if (dataEncalhe == null || Calendar.getInstance().getTime().before(dataEncalhe)) {
			this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.WARNING, "Data de encalhe inválida!"), "result").recursive().serialize();
			throw new ValidacaoException();
		}
		
		int totalCotasAusentes =
			this.fechamentoEncalheService.buscarQuantidadeCotasAusentes(dataEncalhe);
		
		if (totalCotasAusentes > 0 && ("VERIFICACAO").equalsIgnoreCase(operacao)) {
			this.result.use(Results.json()).from("NAO_ENCERRAR", "result").recursive().serialize();
			throw new ValidacaoException();
		} else if (totalCotasAusentes <= 0 && ("VERIFICACAO").equalsIgnoreCase(operacao)) {
			this.result.use(Results.json()).from("ENCERRAR", "result").recursive().serialize();
			throw new ValidacaoException();
		}
			
		try {
			
			this.fechamentoEncalheService.encerrarOperacaoEncalhe(dataEncalhe);
			
		} catch (ValidacaoException e) {
			this.result.use(Results.json()).from(e.getValidacao(), "result").recursive().serialize();
			throw new ValidacaoException();
		} catch (Exception e) {
			this.result.use(
				Results.json()).from(
					new ValidacaoVO(TipoMensagem.ERROR, "Erro ao tentar encerrar a operação de encalhe!"), "result").recursive().serialize();
			throw new ValidacaoException();
		}

		this.result.use(Results.json()).from(
			new ValidacaoVO(TipoMensagem.SUCCESS, "Operação de encalhe encerrada com sucesso!"), "result").recursive().serialize();
	}

	/**
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
		
		ndsFileHeader.setNomeUsuario(this.obterUsuario().getNome());
		
		return ndsFileHeader;
	}
	
	/**
	 * Obtém usuário logado.
	 * 
	 * @return usuário logado
	 */
	private Usuario obterUsuario() {
		
		//TODO: Aguardando definição de como será obtido o usuário logado
		
		Usuario usuario = new Usuario();
		
		usuario.setId(1L);
		usuario.setNome("Usuário da Silva");
		
		return usuario;
	}
	
	
	private String resolveSort(String sortname) {
		
		if (sortname.endsWith("Formatado")) {
			return sortname.substring(0, sortname.indexOf("Formatado"));
		} else {
			return sortname;
		}
	}
	
	
	@Path("/verificarMensagemConsistenciaDados")
	public void verificarMensagemConsistenciaDados(String dataEncalhe, Long fornecedorId, Long boxId) {
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		filtro.setDataEncalhe(DateUtil.parseDataPTBR(dataEncalhe));
		filtro.setFornecedorId(fornecedorId);
		filtro.setBoxId(boxId);
		if (boxId == null){
			if (fechamentoEncalheService.existeFechamentoEncalheDetalhado(filtro)){
				this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, "Você está tentando fazer uma pesquisa em modo consolidado (soma de todos os boxes). Já existem dados salvos em modo de pesquisa por box. Se você continuar, os dados serão sumarizados e não será possível desfazer a operação. Tem certeza que deseja continuar ?"), "result").recursive().serialize();
			} else {
				this.result.use(Results.json()).from("pesquisa","result").serialize() ;   
			}
		} else if ( fechamentoEncalheService.existeFechamentoEncalheConsolidado(filtro)){
			this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, "Você está tentando fazer uma pesquisa por box. Já existem dados salvos em modo de pesquisa consolidado (soma de todos os boxes). Se você continuar, os dados serão perdidos. Tem certeza que deseja continuar ?"), "result").recursive().serialize();
		} else {
			this.result.use(Results.json()).from("pesquisa","result").serialize() ;   
		 }
	}
	
	@Path("/salvarNoEncerrementoOperacao")
	public void salvarNoEncerrementoOperacao(List<FechamentoFisicoLogicoDTO> listaFechamento, String dataEncalhe, Long fornecedorId, Long boxId) {
		if (listaFechamento !=null && !listaFechamento.isEmpty()){
			gravaFechamentoEncalhe(listaFechamento, dataEncalhe, fornecedorId,
					boxId);
		}
		
		this.result.use(Results.json()).from("", "result").recursive().serialize();
	}

	private void gravaFechamentoEncalhe(
			List<FechamentoFisicoLogicoDTO> listaFechamento,
			String dataEncalhe, Long fornecedorId, Long boxId) {
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		filtro.setDataEncalhe(DateUtil.parseDataPTBR(dataEncalhe));
		filtro.setFornecedorId(fornecedorId);
		filtro.setBoxId(boxId);
		if (boxId == null){ 
			fechamentoEncalheService.salvarFechamentoEncalhe(filtro,listaFechamento);
		} else {
			fechamentoEncalheService.salvarFechamentoEncalheBox(filtro, listaFechamento);
		}
	}
	
	
	//------------------
	// Analítico Encalhe
	//------------------
	
	@Path("/analitico")
	public void analiticoEncalhe() {
		this.index();
	}
	
	
	@Path("/pesquisarAnalitico.json")
	public void pesquisarAnaliticoEncalhe(FiltroFechamentoEncalheDTO filtro) {
	
		List<AnaliticoEncalheDTO> listDTO = fechamentoEncalheService.buscarAnaliticoEncalhe(filtro);
		List<AnaliticoEncalheVO> listVO = new ArrayList<AnaliticoEncalheVO>();
		
		for (AnaliticoEncalheDTO dto : listDTO) {
			listVO.add(new AnaliticoEncalheVO(dto));
		}
		
		result.use(FlexiGridJson.class).from(listVO).total(listVO.size()).serialize();
	}
		
}
