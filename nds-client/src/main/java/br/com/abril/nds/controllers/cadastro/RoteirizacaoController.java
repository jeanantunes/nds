package br.com.abril.nds.controllers.cadastro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.ConsultaRoteirizacaoSumarizadoPorCotaVO;
import br.com.abril.nds.dto.ConsultaRoteirizacaoDTO;
import br.com.abril.nds.dto.CotaDisponivelRoteirizacaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaRoteirizacaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.LogBairro;
import br.com.abril.nds.model.LogLocalidade;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
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
@Path("cadastro/roteirizacao")
public class RoteirizacaoController {

	@Autowired
	private BoxService boxService;
	
	@Autowired
	private RoteirizacaoService roteirizacaoService;

	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpServletResponse response;
		
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private HttpSession session;

	private static final String FILTRO_PESQUISA_ROTEIRIZACAO_SESSION_ATTRIBUTE="filtroPesquisa";


	@Path("/")
	@Rules(Permissao.ROLE_CADASTRO_ROTEIRIZACAO)
	public void index() {
		carregarComboBox();
	}

	private void carregarComboBox() {
		List<Box> boxs = boxService.buscarPorTipo(TipoBox.LANCAMENTO);
		
			List<ItemDTO<Long, String>> lista =
				new ArrayList<ItemDTO<Long,String>>();
		
		for (Box box : boxs) {
			
			lista.add(
				new ItemDTO<Long, String>(box.getId(), box.getNome()));
		}
		
		result.include("listaBox", lista);
	}
	
	@Path("/carregarComboRoteiro")
	public void carregarComboRoteiro(Long boxId) {
		List<Roteiro> roteiros = roteirizacaoService.buscarRoteiroDeBox(boxId);
		result.use(Results.json()).from(roteiros, "result").serialize();
	}
	
	@Path("/carregarComboRota")
	public void carregarComboRota(Long roteiroId) {
		List<Rota> rotas = roteirizacaoService.buscarRotaPorRoteiro(roteiroId);
		result.use(Results.json()).from(rotas, "result").serialize();
	
	}
	
	@Path("/carregarComboRoteiroEspecial")
	public void carregarComboRoteiroEspecial() {
		List<Roteiro> roteiros = roteirizacaoService.buscarRoteiroEspecial();
		result.use(Results.json()).from(roteiros, "result").serialize();
	}
	
	@Path("/incluirRoteiro")
	public void incluirRoteiro(Long idBox, Integer ordem, String nome, TipoRoteiro tipoRoteiro) {
		

		Roteiro roteiro = populaRoteiro(idBox, ordem, nome, tipoRoteiro);
		validarCampoObrigatoriosRoteiro(roteiro);
		roteirizacaoService.incluirRoteiro(roteiro);
		result.use(Results.json()).from(
		new ValidacaoVO(TipoMensagem.SUCCESS, "Roreito cadastrado com sucesso."),
						"result").recursive().serialize();

	}
	
	@Path("/iniciaTelaRoteiro")
	public void iniciaTelaRoteiro() {
		Integer ordem = roteirizacaoService.buscarMaiorOrdemRoteiro();
		ordem++;
		result.use(Results.json()).from(ordem).recursive().serialize();
	}
	
	private void validarCampoObrigatoriosRoteiro(Roteiro roteiro) {
		
		List<String> mensagens = new ArrayList<String>();
		
		if(TipoRoteiro.NORMAL.compareTo(roteiro.getTipoRoteiro()) == 0 &&  roteiro.getRoteirizacao().getBox() == null){
			mensagens.add("O campo Box é obrigatório.");
		}
		if(roteiro.getOrdem() == null){
			mensagens.add("O campo Ordem é obrigatório.");
		}
		if("".equals(roteiro.getDescricaoRoteiro())){
			mensagens.add("O campo Nome é obrigatório.");
		}
	
		
		if (!mensagens.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagens));
		}
	}
	
	@Post
	public void autoCompletarRoteiroPorDescricao(String descricao) {
		
		List<Roteiro> listaRoteiro = roteirizacaoService.buscarRoteiroPorDescricao(descricao, MatchMode.ANYWHERE);
		
		List<ItemAutoComplete> listaRoteiroAutoComplete = new ArrayList<ItemAutoComplete>();
		
		if (listaRoteiro != null && !listaRoteiro.isEmpty()) {
			
			for (Roteiro roteiro : listaRoteiro) {
				roteiro.setRotas(null);
				if ( roteiro.getRoteirizacao().getBox() != null ){ 
					roteiro.getRoteirizacao().getBox().setCotas(null);
					roteiro.getRoteirizacao().getBox().setRoteiros(null);
				}	
				listaRoteiroAutoComplete.add(new ItemAutoComplete(roteiro.getDescricaoRoteiro(), null,roteiro ));
			}
		}
		
		this.result.use(Results.json()).from(listaRoteiroAutoComplete, "result").include("chave").serialize();
	}
	
	@Post
	public void buscaRoteiroPorDescricao(String descricao) {
		
		
		List<Roteiro> listaRoteiro = roteirizacaoService.buscarRoteiroPorDescricao(descricao, MatchMode.EXACT);
		if (listaRoteiro.isEmpty()){
			//throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Não existe roteiro"));
			result.use(Results.json()).from(
					new ValidacaoVO(TipoMensagem.ERROR, "Roteiro inexistente."),
									"result").recursive().serialize();
		} else {
			this.result.use(Results.json()).from(listaRoteiro, "result").include("box").serialize();
		}	
	}
	
	@Post
	public void buscaRotasPorRoteiro(Long roteiroId, int rp, int page) {
		
		
		List<Rota> listaRota = roteirizacaoService.buscarRotaPorRoteiro(roteiroId);
		
		
		result.use(FlexiGridJson.class).from(listaRota).total(listaRota.size()).page(page).serialize();
	}
	
	@Path("/incluirRota")
	public void incluirRota(Long roteiroId, Integer ordem, String nome) {
		Roteiro roteiro = new Roteiro();
		roteiro.setId(roteiroId);
		Rota rota = new Rota();
		rota.setRoteiro(roteiro);
		rota.setOrdem(ordem);
		rota.setDescricaoRota(nome);
		validarCampoObrigatoriosRota(rota);
		roteirizacaoService.incluirRota(rota);
		result.use(Results.json()).from(
		new ValidacaoVO(TipoMensagem.SUCCESS, "Rota cadastrada com sucesso."),
						"result").recursive().serialize();

	}
	
	@Path("/iniciaTelaRota")
	public void iniciaTelaRota(Long idRoteiro) {
		Integer ordem = roteirizacaoService.buscarMaiorOrdemRota(idRoteiro);
		if (ordem == null){
			ordem = 0;
		}
		ordem++;
		result.use(Results.json()).from(ordem).recursive().serialize();
	}
	
	private void validarCampoObrigatoriosRota(Rota rota) {
		
		List<String> mensagens = new ArrayList<String>();
		
		if(rota.getOrdem() == null){
			mensagens.add("O campo Ordem é obrigatório.");
		}
		if("".equals(rota.getDescricaoRota())){
			mensagens.add("O campo Nome é obrigatório.");
		}
	
		
		if (!mensagens.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagens));
		}
	}
	
	@Path("/excluiRotas")
	public void excluiRotas(List<Long> rotasId, Long roteiroId) {
		roteirizacaoService.excluirListaRota(rotasId, roteiroId);
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Rotas excluidas com sucesso."),"result").recursive().serialize();

	}
	
	@Path("/transferirRotas")
	public void transferirRotas(List<Long> rotasId, Long roteiroId, String roteiroNome) {
		if ( roteiroId == null ) {
			List<Roteiro> listaRoteiros  = roteirizacaoService.buscarRoteiroPorDescricao(roteiroNome, MatchMode.EXACT);
			if (!listaRoteiros.isEmpty() ){
				roteiroId = listaRoteiros.get(0).getId();
			} 
			
		}
		roteirizacaoService.transferirListaRota(rotasId, roteiroId) ;
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Rotas transferidas com sucesso."),"result").recursive().serialize();

	}
	
	@Path("/transferirRotasComNovoRoteiro")
	public void transferirRotasComNovoRoteiro(List<Long> rotasId, Long idBox, Integer ordem, String roteiroNome, TipoRoteiro tipoRoteiro) {
		Roteiro roteiro = populaRoteiro(idBox, ordem, roteiroNome, tipoRoteiro);
		validarCampoObrigatoriosRoteiro(roteiro);
		roteirizacaoService.transferirListaRotaComNovoRoteiro(rotasId, roteiro);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Rotas transferidas com sucesso."),"result").recursive().serialize();

	}

	private Roteiro populaRoteiro(Long idBox, Integer ordem,
			String roteiroNome, TipoRoteiro tipoRoteiro) {
		Roteiro roteiro = new Roteiro();
		if ( idBox != null ){
			Box box = new Box();
			box.setId(idBox);
			roteiro.getRoteirizacao().setBox(box);
		}	
		roteiro.setOrdem(ordem);
		roteiro.setDescricaoRoteiro(roteiroNome);
		roteiro.setTipoRoteiro(tipoRoteiro);
		return roteiro;
	}
	
	@Path("/pesquisarRotaPorNome")
	public void pesquisarRotaPorNome(Long roteiroId, String nomeRota,
			String sortname, String sortorder, int rp, int page) {
		List<Rota> lista = roteirizacaoService.buscarRotaPorNome(roteiroId, nomeRota, MatchMode.ANYWHERE) ;
		int quantidade = lista.size();
		result.use(FlexiGridJson.class).from(lista).total(quantidade).page(page).serialize();

	}
	
	@Path("/iniciaTelaCotas")
	public void iniciaTelaCotas() {
		List<String> uf = roteirizacaoService.buscarUF();
		result.use(Results.json()).from(uf, "result").serialize();
	}
	
	@Path("/buscalistaMunicipio")
	public void buscalistaMunicipio(String uf) {
		List<LogLocalidade> lista = roteirizacaoService.buscarMunicipioPorUf(uf);
		result.use(Results.json()).from(lista, "result").serialize();
	}
	
	@Path("/buscalistaBairro")
	public void buscalistaBairro(String uf, Long municipio) {
		List<LogBairro> bairro = roteirizacaoService.buscarBairroPorMunicipio(municipio, uf);
		result.use(Results.json()).from(bairro, "result").serialize();
	}
	
	@Path("/buscarPvsPorCota")
	public void buscarPvsPorCota(Integer numeroCota,  Long rotaId, Long roteiroId,  String sortname, String sortorder, int rp, int page) {
		List<CotaDisponivelRoteirizacaoDTO> lista = roteirizacaoService.buscarPvsPorCota(numeroCota, rotaId , roteiroId );
		int quantidade = lista.size();
		result.use(FlexiGridJson.class).from(lista).total(quantidade).page(page).serialize();
		
	}

	@Path("/buscarRotaPorId")
	public void buscarRotaPorId(Long rotaId) {
		Rota rota =  roteirizacaoService.buscarRotaPorId(rotaId);
		result.use(Results.json()).from(rota, "result").serialize();
		
	}
	
	@Post
	public void autoCompletarRotaPorDescricao(Long roteiroId, String nomeRota) {
		List<Rota> lista = roteirizacaoService.buscarRotaPorNome(roteiroId, nomeRota, MatchMode.ANYWHERE) ;
		
		List<ItemAutoComplete> listaRotaoAutoComplete = new ArrayList<ItemAutoComplete>();
		
		if (lista != null && !lista.isEmpty()) {
			
			for (Rota rota : lista) {
				rota.setRoteiro(null);
				listaRotaoAutoComplete.add(new ItemAutoComplete(rota.getDescricaoRota(), null,rota ));
			}
		}
		
		this.result.use(Results.json()).from(listaRotaoAutoComplete, "result").include("chave").serialize();
	}
	
	@Path("/excluirRoteirizacao")
	public void excluirRoteirizacao(List<Long> roteirizacaoId) {
		roteirizacaoService.excluirRoteirizacao(roteirizacaoId);
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Roteirização excluida com sucesso."),"result").recursive().serialize();

	}	
	
	@Path("/buscarPvsPorEndereco")
	public void buscarPvsPorEndereco(String CEP, String uf, String municipio, String bairro , Long rotaId, Long roteiroId,  String sortname, String sortorder, int rp, int page) {
		List<CotaDisponivelRoteirizacaoDTO> lista = roteirizacaoService.buscarRoteirizacaoPorEndereco(CEP, uf, municipio, bairro,  rotaId , roteiroId );
		int quantidade = lista.size();
		result.use(FlexiGridJson.class).from(lista).total(quantidade).page(page).serialize();
	}
	
	@Post
	@Path("/pesquisarRoteirizacao")
	public void pesquisarRoteirizacao(Long boxId, Long roteiroId, Long rotaId,Integer numeroCota, String sortname,String sortorder, int rp, int page) {
		
		FiltroConsultaRoteirizacaoDTO filtro = 
			this.prepararFiltroConsulta(boxId,roteiroId, rotaId, numeroCota, sortname, sortorder, rp, page);
		
		boolean isPesquisaSumarizadaPorCota = this.isPesquisaSumarizadaPorCota(filtro);
		
		List<ConsultaRoteirizacaoDTO> lista =
			this.realizarPesquisaRoteirizacao(filtro, isPesquisaSumarizadaPorCota);
		
		if(lista == null || lista.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		Integer totalRegistros  = roteirizacaoService.buscarQuantidadeRoteirizacao(filtro);
		
		result.use(FlexiGridJson.class).from(lista).total(totalRegistros).page(page).serialize();	
	}

	private FiltroConsultaRoteirizacaoDTO prepararFiltroConsulta(Long boxId, Long roteiroId,
																 Long rotaId, Integer numeroCota,
																 String sortname, String sortorder,
																 int rp, int page) {
		
		FiltroConsultaRoteirizacaoDTO filtro = new FiltroConsultaRoteirizacaoDTO();

		filtro.setIdBox(boxId);
		filtro.setIdRoteiro(roteiroId);
		filtro.setIdRota(rotaId);
		filtro.setNumeroCota(numeroCota);
		
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			
		filtro.setPaginacao(paginacao);
		
		filtro.setOrdenacaoColuna((Util.getEnumByStringValue(FiltroConsultaRoteirizacaoDTO.OrdenacaoColunaConsulta.values(), sortname)));
		
		FiltroConsultaRoteirizacaoDTO filtroSessao = (FiltroConsultaRoteirizacaoDTO) 
				this.session.getAttribute(FILTRO_PESQUISA_ROTEIRIZACAO_SESSION_ATTRIBUTE);

		if (filtroSessao != null && !filtroSessao.equals(filtro)) {
			
			filtro.getPaginacao().setPaginaAtual(1);
		}
				
		session.setAttribute(FILTRO_PESQUISA_ROTEIRIZACAO_SESSION_ATTRIBUTE, filtro);
		
		return filtro;
	}
	
	private void carregarDescricoesFiltro(FiltroConsultaRoteirizacaoDTO filtro) {

		if (filtro == null) {
			
			return;
		}
		
		if (filtro.getIdBox() != null) {
			
			Box box = boxService.buscarPorId(filtro.getIdBox());
			
			if (box != null) {
				
				filtro.setNomeBox(box.getNome());
			}
		}
		
		if (filtro.getIdRoteiro() != null) {
			
			Roteiro roteiro = roteirizacaoService.buscarRoteiroPorId(filtro.getIdRoteiro());
			
			if (roteiro != null) {
				
				filtro.setNomeRoteiro(roteiro.getDescricaoRoteiro());
			}
		}

		if (filtro.getIdRota() != null) {
			
			Rota rota = roteirizacaoService.buscarRotaPorId(filtro.getIdRota());
			
			if (rota != null) {
				
				filtro.setNomeRota(rota.getDescricaoRota());
			}
		}
		
		if (filtro.getNumeroCota() != null) {
			
			Cota cota = cotaService.obterPorNumeroDaCota(filtro.getNumeroCota());
			
			if (cota != null) {
				
				String nomeCota = null;
				
				if (cota.getPessoa() instanceof PessoaJuridica) {
					
					PessoaJuridica pessoaJuridica = (PessoaJuridica) cota.getPessoa();
					
					nomeCota = pessoaJuridica.getRazaoSocial();
				
				} else if (cota.getPessoa() instanceof PessoaFisica) {
					
					PessoaFisica pessoaFisica = (PessoaFisica) cota.getPessoa();
					
					nomeCota = pessoaFisica.getNome();
				}
				
				filtro.setNomeCota(nomeCota);
			}
		}
	}

	@Path("/pesquisarRoteirizacaoPorCota")
	public void pesquisarRoteirizacaoPorCota(Integer numeroCota, Long roteiroId, Long rotaId, 
											 TipoRoteiro tipoRoteiro,  String sortname, String sortorder, 
											 int rp, int page) {
		
		List<ConsultaRoteirizacaoDTO> lista = roteirizacaoService.buscarRoteirizacaoPorNumeroCota(numeroCota, tipoRoteiro, sortname, Ordenacao.valueOf(sortorder.toUpperCase()), page*rp - rp , rp);
		if (lista == null || lista.isEmpty() ) {
			result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, "Nenhum registro encontrado."),"result").recursive().serialize();
		} else {
			int quantidade = lista.size();
			result.use(FlexiGridJson.class).from(lista).total(quantidade).page(page).serialize();
		}
	}
	
	@Path("/buscaCotaPorNumero")
	public void buscaCotaPorNumero(Integer numeroCota) {
		if ( numeroCota == null  ) {
			result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, "Informe o numero da cota."),"result").recursive().serialize();
		} else {
			Cota cota =  cotaService.obterPorNumeroDaCota(numeroCota);
			if (cota == null ){
				result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, "Nenhum registro encontrado."),"result").recursive().serialize();
			} else {
				result.use(Results.json()).from(cota, "result").include("pessoa").serialize();
			}		
		}
	}
	
	public void exportar(FileType fileType) throws IOException {
		
		if (fileType == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de arquivo não encontrado!");
		}
		
		FiltroConsultaRoteirizacaoDTO filtroSessao = this.obterFiltroParaExportacao();
		
		boolean isPesquisaSumarizadaPorCota = this.isPesquisaSumarizadaPorCota(filtroSessao);
		
		List<ConsultaRoteirizacaoDTO> lista =
			this.realizarPesquisaRoteirizacao(filtroSessao, isPesquisaSumarizadaPorCota);
		
		if (!isPesquisaSumarizadaPorCota) {
			
			FileExporter.to("roteirizacao", fileType)
				.inHTTPResponse(
					this.getNDSFileHeader(), filtroSessao, null, 
					lista, ConsultaRoteirizacaoDTO.class, this.response);
		
		} else {
			
			List<ConsultaRoteirizacaoSumarizadoPorCotaVO> listaConsultaRoteirizacaoSumarizado =
				this.getConsultaRoteirizacaoSumarizadoPorCotaVO(lista);
			
			FileExporter.to("roteirizacao", fileType)
				.inHTTPResponse(
					this.getNDSFileHeader(), filtroSessao, null, 
					listaConsultaRoteirizacaoSumarizado, ConsultaRoteirizacaoSumarizadoPorCotaVO.class, this.response);
		}
	}
	
	private List<ConsultaRoteirizacaoSumarizadoPorCotaVO> getConsultaRoteirizacaoSumarizadoPorCotaVO(
												List<ConsultaRoteirizacaoDTO> listaConsultaRoteirizacao) {
		
		ConsultaRoteirizacaoSumarizadoPorCotaVO vo = null;
		
		List<ConsultaRoteirizacaoSumarizadoPorCotaVO> listaConsultaRoteirizacaoSumarizado = 
			new ArrayList<ConsultaRoteirizacaoSumarizadoPorCotaVO>();
		
		for (ConsultaRoteirizacaoDTO dto : listaConsultaRoteirizacao) {
		
			vo = new ConsultaRoteirizacaoSumarizadoPorCotaVO();
		
			vo.setNomeBox(dto.getNomeBox());
			vo.setDescricaoRoteiro(dto.getDescricaoRoteiro());
			vo.setDescricaoRota(dto.getDescricaoRota());
			vo.setQntCotas(dto.getQntCotas());
			
			listaConsultaRoteirizacaoSumarizado.add(vo);
		}
		
		return listaConsultaRoteirizacaoSumarizado;
	}

	private List<ConsultaRoteirizacaoDTO> realizarPesquisaRoteirizacao(
															FiltroConsultaRoteirizacaoDTO filtroSessao,
															boolean isPesquisaSumarizadaPorCota) {
		
		List<ConsultaRoteirizacaoDTO> lista = null;
		
		if (isPesquisaSumarizadaPorCota) {
			
			lista = roteirizacaoService.buscarRoteirizacaoSumarizadoPorCota(filtroSessao);
			
		} else {
			
			lista = roteirizacaoService.buscarRoteirizacao(filtroSessao);
		}
		
		return lista;
	}

	private boolean isPesquisaSumarizadaPorCota(FiltroConsultaRoteirizacaoDTO filtroSessao) {
		
		boolean pesquisaSumarizadaPorCota =
			(filtroSessao.getIdRota() == null && filtroSessao.getNumeroCota() == null);
		
		return pesquisaSumarizadaPorCota;
	}
	
	/**
	 * Obtém o filtro de pesquisa para exportação.
	 * 
	 * @return filtro
	 */
	private FiltroConsultaRoteirizacaoDTO obterFiltroParaExportacao() {
		
		FiltroConsultaRoteirizacaoDTO filtroSessao =
			(FiltroConsultaRoteirizacaoDTO)
				this.session.getAttribute(FILTRO_PESQUISA_ROTEIRIZACAO_SESSION_ATTRIBUTE);
			
		if (filtroSessao == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Filtro não encontrado!");
		}
		
		if (filtroSessao.getPaginacao() != null) {
			
			filtroSessao.getPaginacao().setPaginaAtual(null);
			filtroSessao.getPaginacao().setQtdResultadosPorPagina(null);
		}
		
		this.carregarDescricoesFiltro(filtroSessao);
		
		return filtroSessao;
	}

	@Get
	@Path("/imprimirArquivo")
	public void imprimirArquivo(Long boxId, Long roteiroId, Long rotaId, TipoRoteiro  tipoRoteiro, Integer numeroCota, Boolean pesquisaRoteizicaoPorCota , String sortname, String sortorder, 
			int rp, int page, FileType fileType) {

		List<ConsultaRoteirizacaoDTO> lista  = new ArrayList<ConsultaRoteirizacaoDTO>();
		try {
			
			if (pesquisaRoteizicaoPorCota)	{
				lista = roteirizacaoService.buscarRoteirizacaoPorNumeroCota(numeroCota, tipoRoteiro, sortname, Ordenacao.valueOf(sortorder.toUpperCase()), page*rp - rp , rp);
			} else {
				//FIXME alterar a consult
				//lista = roteirizacaoService.buscarRoteirizacao( boxId,  roteiroId,  rotaId,  tipoRoteiro, sortname, Ordenacao.valueOf(sortorder.toUpperCase()), page*rp - rp , rp);
				
			}
			
			FileExporter.to("roteirizacao", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, lista,ConsultaRoteirizacaoDTO.class, this.response);
			
		} catch (Exception e) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Erro ao gerar o arquivo!"));
		}

		this.result.use(Results.nothing());
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
	
	/**
	 * Obtém dados das cotas sumarizados (Detalhamento)
	 * 
	 * @param idBox
	 * @param idRota
	 * @param idRoteiro
	 */
	public void obterCotasSumarizadas(Long idBox, Long idRota, Long idRoteiro) {
		
		List<ConsultaRoteirizacaoDTO> lista = roteirizacaoService.obterCotasParaBoxRotaRoteiro(idBox,idRota,idRoteiro);
		
		result.use(FlexiGridJson.class).from(lista).total(lista.size()).page(1).serialize();
	}
	

	
	//NOVA ROTEIRIZAÇÃO
	
	/**
	 * Obtém lista de box do tipo lançamento
	 */
	@Get
	@Path("/obterBoxLancamento")
	public void obterBoxLancamento(){
		List<Box> listaBox = this.roteirizacaoService.obterListaBoxLancamento();
		result.use(FlexiGridJson.class).from(listaBox).total(listaBox.size()).page(1).serialize();
	}
	
	/**
	 * Obtém lista de roteiros do box
	 * @param idBox
	 */
	@Get
	@Path("/obterRoteirosBox")
	public void obterRoteirosBox(Long idBox){
		List<Roteiro> listaRoteiro = this.roteirizacaoService.obterListaRoteiroPorBox(idBox);
		result.use(FlexiGridJson.class).from(listaRoteiro).total(listaRoteiro.size()).page(1).serialize();
	}
	
	/**
	 * Obtém lista de rotas do roteiro
	 * @param idRoteiro
	 */
	@Get
	@Path("/obterRotasRoteiro")
	public void obterRotasRoteiro(Long idRoteiro){
		List<Rota> listaRota = this.roteirizacaoService.obterListaRotaPorRoteiro(idRoteiro);
		result.use(FlexiGridJson.class).from(listaRota).total(listaRota.size()).page(1).serialize();
	}
	
	/**
	 * Obtém dados da roteirização para edição
	 * @param idCota
	 * @param idRoteirizacao - Utilizado para obter as listas de box, roteiro e rota
	 * @param idBox - Box Selecionado
	 * @param idRoteiro - Roteiro Selecionado
	 * @param idRota - Rota Selecionada
	 */
	@Get
	@Path("/editarRoteirizacao")
	public void editarRoteirizacao(FiltroConsultaRoteirizacaoDTO parametros){
		
		List<Rota> listaRota = this.roteirizacaoService.obterListaRotaPorRoteiro(parametros.getIdRoteiro());
		result.use(FlexiGridJson.class).from(listaRota).total(listaRota.size()).page(1).serialize();
		
	}
	
}
