package br.com.abril.nds.controllers.cadastro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.ConsultaRoteirizacaoSumarizadoPorCotaVO;
import br.com.abril.nds.dto.BoxRoteirizacaoDTO;
import br.com.abril.nds.dto.ConsultaRoteirizacaoDTO;
import br.com.abril.nds.dto.CotaDisponivelRoteirizacaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.PdvRoteirizacaoDTO;
import br.com.abril.nds.dto.RotaRoteirizacaoDTO;
import br.com.abril.nds.dto.RoteirizacaoDTO;
import br.com.abril.nds.dto.RoteiroRoteirizacaoDTO;
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
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.service.PdvService;
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
	private PdvService pdvService;
	
	@Autowired
	private EnderecoService enderecoService;
	
	@Autowired
	private HttpSession session;

	private static final String FILTRO_PESQUISA_ROTEIRIZACAO_SESSION_ATTRIBUTE="filtroPesquisa";
	
	/**
	 * Chave para armazenamento do DTO de roteirização na sessão
	 */
	private static final String ROTEIRIZACAO_DTO_SESSION_KEY = "ROTEIRIZACAO_DTO_SESSION_KEY";
	
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
		
		this.validarCamposRoteiro(ordem, nome, tipoRoteiro);
		
		this.adicionarRoteiro(ordem, nome);
		
		result.use(Results.json()).from(this.getDTO(), "result").recursive().serialize();
	}
	
	@Path("/iniciaTelaRoteiro")
	public void iniciaTelaRoteiro() {
		Integer ordem = roteirizacaoService.buscarMaiorOrdemRoteiro();
		
		if (ordem == null){
			
			ordem = 0;
		}
		
		ordem++;
		
		for (RoteiroRoteirizacaoDTO dto : this.getDTO().getRoteiros()){
			
			if (ordem <= dto.getOrdem()){
				
				ordem = dto.getOrdem() + 1;
			}
		}
		
		result.use(Results.json()).from(ordem).recursive().serialize();
	}
	
	private void validarCamposRoteiro(Integer ordem, String nome, TipoRoteiro tipoRoteiro) {
		
		List<String> mensagens = new ArrayList<String>();
		
		if(ordem == null){
			
			mensagens.add("O campo Ordem é obrigatório.");
		}
		
		if(nome == null || nome.isEmpty()){
		
			mensagens.add("O campo Nome é obrigatório.");
		}
		
		if (ordem != null){
			
			for (RoteiroRoteirizacaoDTO dto : this.getDTO().getRoteiros()){
				
				if (ordem.equals(dto.getOrdem())){
					
					mensagens.add("O roteiro " + dto.getNome() + " já esta na ordem " + ordem);
					break;
				}
			}
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
					roteiro.getRoteirizacao().setRoteiros(null);
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
	
	/**
	 * Obtém lista de rotas do roteiro
	 * @param idRoteiro
	 */
	@Post
	public void buscaRotasPorRoteiro(Long roteiroId, int rp, int page) {
		
		List<RotaRoteirizacaoDTO> dtosRota = this.getDTO().getRoteiro(roteiroId).getRotas();
		
		result.use(Results.json()).from(dtosRota, "result").recursive().serialize();
	}
	
	@Post
	public void buscaRoteiros() {
		
		result.use(Results.json()).from(this.getDTO().getRoteiros(), "result").recursive().serialize();
	}

	@Path("/incluirRota")
	public void incluirRota(Long roteiroId, Integer ordem, String nome) {
		
		this.validarCamposRota(roteiroId, ordem, nome);
		
		this.adicionarRota(roteiroId, ordem, nome);
		
		result.use(Results.json()).from(this.getDTO().getRoteiro(roteiroId), "result").recursive().serialize();
	}
	
	@Path("/iniciaTelaRota")
	public void iniciaTelaRota(Long idRoteiro) {
		Integer ordem = roteirizacaoService.buscarMaiorOrdemRota(idRoteiro);
		
		if (ordem == null){
			
			ordem = 0;
		}
		
		ordem++;
		
		for (RotaRoteirizacaoDTO dto : this.getDTO().getRoteiro(idRoteiro).getRotas()){
			
			if (ordem <= dto.getOrdem()){
				
				ordem = dto.getOrdem() + 1;
			}
		}
		
		result.use(Results.json()).from(ordem).recursive().serialize();
	}
	
	private void validarCamposRota(Long idRoteiro, Integer ordem, String nome) {
		
		List<String> mensagens = new ArrayList<String>();
		
		if(ordem == null){
			
			mensagens.add("O campo Ordem é obrigatório.");
		}
		
		if(nome == null || nome.isEmpty()){
			
			mensagens.add("O campo Nome é obrigatório.");
		}
		
		if (ordem != null){
			
			for (RotaRoteirizacaoDTO dto : this.getDTO().getRoteiro(idRoteiro).getRotas()){
				
				if (ordem.equals(dto.getOrdem())){
					
					mensagens.add("A rota " + dto.getNome() + " já esta na ordem " + ordem);
					break;
				}
			}
		}
		
		if (!mensagens.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagens));
		}
	}
	
	@Path("/excluirRota")
	public void excluirRota(Long rotaId, Long roteiroId) {
		
		this.getDTO().getRoteiro(roteiroId).removerRota(rotaId);
		
		result.use(Results.json()).from("", "result").serialize();
	}
	
	@Path("/excluirRoteiro")
	public void excluirRoteiro(Long roteiroId) {
		
		this.getDTO().removerRoteiro(roteiroId);
		
		result.use(Results.json()).from("", "result").serialize();
	}
	
//	@Post
//	public void excluirCotas(Long roteiroId, Long idRota, List<Long> pdvs){
//		
//		RotaRoteirizacaoDTO rota = this.getDTO().getRoteiro(roteiroId).getRota(idRota);
//		
//		if (pdvs != null){
//			
//			for (Long cotaId : pdvs){
//				
//				rota.removerPdv(cotaId);
//			}
//		}
//		
//		result.use(Results.json()).from("", "result").serialize();
//	}
	
//	@Path("/transferirRotas")
//	public void transferirRotas(List<Long> rotasId, Long roteiroId, String roteiroNome) {
//		if ( roteiroId == null ) {
//			List<Roteiro> listaRoteiros  = roteirizacaoService.buscarRoteiroPorDescricao(roteiroNome, MatchMode.EXACT);
//			if (!listaRoteiros.isEmpty() ){
//				roteiroId = listaRoteiros.get(0).getId();
//			} 
//			
//		}
//		roteirizacaoService.transferirListaRota(rotasId, roteiroId) ;
//		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Rotas transferidas com sucesso."),"result").recursive().serialize();
//
//	}
	
	@Path("/transferirRotasComNovoRoteiro")
	public void transferirRotasComNovoRoteiro(List<Long> rotasId, Long idBox, Integer ordem, String roteiroNome, TipoRoteiro tipoRoteiro) {
		Roteiro roteiro = populaRoteiro(idBox, ordem, roteiroNome, tipoRoteiro);
		validarCamposRoteiro(ordem, roteiroNome, tipoRoteiro);
		roteirizacaoService.transferirListaRotaComNovoRoteiro(rotasId, roteiro);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Rotas transferidas com sucesso."),"result").recursive().serialize();

	}

	private Roteiro populaRoteiro(Long idBox, Integer ordem,
			String roteiroNome, TipoRoteiro tipoRoteiro) {
		Roteiro roteiro = new Roteiro();
		if ( idBox != null ){
			Box box = new Box();
			box.setId(idBox);
			
			Roteirizacao roteirizacao = new Roteirizacao();
			roteirizacao.setBox(box);
			roteiro.setRoteirizacao(roteirizacao);
		}	
		roteiro.setOrdem(ordem);
		roteiro.setDescricaoRoteiro(roteiroNome);
		roteiro.setTipoRoteiro(tipoRoteiro);
		return roteiro;
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
	
	@Post
	@Path("/boxSelecionado")
	public void boxSelecionado(Long idBox, String nomeBox) {
	   RoteirizacaoDTO existente = null; 
	   RoteirizacaoDTO dto = getDTO();
	   if (!Box.ESPECIAL.getId().equals(idBox)) {
	       existente = roteirizacaoService.obterRoteirizacaoPorBox(idBox);
	   }
	   if (existente != null) {
	       dto = setDTO(existente);
	       dto.filtarBox(nomeBox);
	   } else {
	       dto.reset(idBox);
	   }
	   
	   dto.addAllRoteiro(this.obterRoteirosTransferidos(idBox));
	   
	   result.use(CustomJson.class).from(dto).serialize();
	}

	@Post
	@Path("/recarregarCotasRota")
	public void recarregarCotasRota(Long idRota, String sortname, String sortorder) {
	    RoteirizacaoDTO roteirizacao = getDTO();
	    RotaRoteirizacaoDTO rota = roteirizacao.getRota(idRota);
	    List<PdvRoteirizacaoDTO> pdvs = rota.getPdvs();
	    
	    if (pdvs != null){
	    
		    Ordenacao ordenacao = Util.getEnumByStringValue(Ordenacao.values(), sortorder);
		    PaginacaoUtil.ordenarEmMemoria(pdvs, ordenacao, sortname);
		    result.use(FlexiGridJson.class).from(pdvs).total(pdvs.size()).page(1).serialize();
	    } else {
	    	
	    	result.use(Results.json()).from("").serialize();
	    }
	}
	
	@Post
	@Path("/recarregarBox")
	public void recarregarBox(String nomeBox) {
	    RoteirizacaoDTO roteirizacao = getDTO();
	    roteirizacao.filtarBox(nomeBox);
	    List<BoxRoteirizacaoDTO> boxes = roteirizacao.getBoxDisponiveis();
        result.use(FlexiGridJson.class).from(boxes).total(boxes.size()).page(1).serialize();
	}
	
	@Post
    @Path("/recarregarRoteiros")
    public void recarregarRoteiros(String descricaoRoteiro) {
        RoteirizacaoDTO roteirizacao = getDTO();
        roteirizacao.filtarRoteiros(descricaoRoteiro);
        List<RoteiroRoteirizacaoDTO> roteiros = roteirizacao.getRoteiros();
        result.use(FlexiGridJson.class).from(roteiros).total(roteiros.size()).page(1).serialize();
    }
	
	@Post
    @Path("/recarregarRotas")
    public void recarregarRotas(Long idRoteiro, String descricaoRota) {
        RoteirizacaoDTO roteirizacao = getDTO();
        RoteiroRoteirizacaoDTO roteiro = roteirizacao.getRoteiro(idRoteiro);
        roteiro.filtarRotas(descricaoRota);
        List<RotaRoteirizacaoDTO> roteiros = roteiro.getRotas();
        result.use(FlexiGridJson.class).from(roteiros).total(roteiros.size()).page(1).serialize();
    }
	
	@Post
    @Path("/ordemPdvChangeListener")
	public void ordemPdvChangeListener(Long idRota, Long idPdv, Integer ordem) {
        RoteirizacaoDTO roteirizacao = getDTO();
        RotaRoteirizacaoDTO rota = roteirizacao.getRota(idRota);
        boolean ordemValida = rota.alterarOrdemPdv(idPdv, ordem);
        result.use(Results.json()).withoutRoot().from(ordemValida).serialize();
        
	}
	
	/**
	 * Método responsável pela obtenção dos dados que irão preencher o combo de UF's.
	 * @param tela
	 */
	@Post
    @Path("/obterDadosComboUF")
	public void obterDadosComboUF() {
		
		List<String> ufs = this.enderecoService.obterUnidadeFederacaoBrasil();
		
		this.result.use(Results.json()).from(ufs, "result").serialize();
	}
	
	/**
	 * Obtém PDV's para a inclusão de rota pdv na roteirização
	 */
	@Post
	@Path("/obterPdvsDisponiveis")
	public void obterPdvsDisponiveis(Integer numCota, String municipio, String uf, String bairro, String cep, String sortname, String sortorder ){
        
		List<PdvRoteirizacaoDTO> lista = this.roteirizacaoService.obterPdvsDisponiveis(numCota, municipio, uf, bairro, cep);
		
		Ordenacao ordenacao = Util.getEnumByStringValue(Ordenacao.values(), sortorder);
		PaginacaoUtil.ordenarEmMemoria(lista, ordenacao, sortname);
		
		result.use(FlexiGridJson.class).from(lista).total(lista.size()).page(1).serialize();
	}
	
	/**
	 * Recupera o DTO de roteirização da sessão
	 * @return DTO armazenado na sessão ou null caso não exista
	 */
	public RoteirizacaoDTO getDTO() {
	    return (RoteirizacaoDTO) session.getAttribute(ROTEIRIZACAO_DTO_SESSION_KEY);
	}
	
	/**
	 * Armazena o DTO de roteirização na sessão
	 * @param dto DTO com as informações da roteirização 
	 */
	public RoteirizacaoDTO setDTO(RoteirizacaoDTO dto) {
	    session.setAttribute(ROTEIRIZACAO_DTO_SESSION_KEY, dto);
	    return dto;
	}
	
	/**
	 * Remove o DTO de roteirização da sessão da sessão
	 */
	public void clearDTO() {
	    setDTO(null);
	}
	
	
	@Post
    @Path("/novaRoteirizacao")
	public void novaRoteirizacao() {
	    List<Box> disponiveis = roteirizacaoService.obterListaBoxLancamento(null);
	    List<BoxRoteirizacaoDTO> dtos = BoxRoteirizacaoDTO.toDTOs(disponiveis);
	    RoteirizacaoDTO dto = RoteirizacaoDTO.novaRoteirizacao(dtos);
	    setDTO(dto);
	    
	    result.use(CustomJson.class).from(dto).serialize();
	}
	
	
	@Post
	@Path("/editarRoteirizacao")
	public void editarRoteirizacao(Long idRoteirizacao) {
	    RoteirizacaoDTO dto = roteirizacaoService.obterRoteirizacaoPorId(idRoteirizacao);
	    setDTO(dto);
	    result.use(CustomJson.class).from(dto).serialize();
	}
	
	@Post
    @Path("/confirmarRoteirizacao")
	public void confirmarRoteirizacao() {
	    RoteirizacaoDTO dto = getDTO();
	    validarRoteirizacao(dto);
	    roteirizacaoService.confirmarRoteirizacao(dto);
	    clearDTO();
	    result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Roteirização confirmada com sucesso."),"result").recursive().serialize();
	}
	
	/**
	 * Valida a roteirização para confirmação
	 * @param dto DTO com as informações de roteirização para confirmação
	 */
	private void validarRoteirizacao(RoteirizacaoDTO dto) {
        List<String> erros = new ArrayList<String>();
        if (dto.getBox() == null) {
            erros.add("É necessário selecionar um Box para Roteirização!");
        } else {
            if (dto.getTodosRoteiros().isEmpty()) {
                erros.add("É necessário ao menos um Roteiro para a Roteirização!");
            } else {
                for (RoteiroRoteirizacaoDTO roteiro : dto.getTodosRoteiros()) {
                    if (roteiro.getTodasRotas().isEmpty()) {
                        erros.add(String.format("Roteiro [%s] sem Rota associada!", roteiro.getNome()));
                    } else {
                        for (RotaRoteirizacaoDTO rota : roteiro.getTodasRotas()) {
                            if (rota.getPdvs().isEmpty()) {
                                erros.add(String.format("Rota [%s] sem PDV associado!", rota.getNome()));
                            }
                        }
                    }
                }
            }
        }
        if (!erros.isEmpty()) {
            ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.ERROR, erros);
            throw new ValidacaoException(validacao);
        }
        
    }

    @Post
    @Path("/cancelarRoteirizacao")
	public void cancelarRoteirizacao() {
	    clearDTO();
	    result.nothing();
	}
	
	/**
	 * Verifica se PDV's podem ser adicionados na Roteirização
	 * @param pdvs
	 */
	private void validaNovosPdvs(List<PdvRoteirizacaoDTO> pdvs, List<PdvRoteirizacaoDTO> pdvsAtual){
		
		for(PdvRoteirizacaoDTO itemPdvDTO:pdvs){
			if (!this.roteirizacaoService.verificaDisponibilidadePdv(itemPdvDTO.getId())){
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "O [PDV "+itemPdvDTO.getId()+"] já pertence à um [Box] roteirizado !"));
			}
		}
		
		this.verificaOrdemPdvs(pdvs, pdvsAtual);
	}
	
	/**
	 * Verifica o campo Ordem dos PDV's
	 * @param pdvs
	 * @param pdvsAtual
	 */
	private void verificaOrdemPdvs(List<PdvRoteirizacaoDTO> pdvs, List<PdvRoteirizacaoDTO> pdvsAtual){
		
		List<PdvRoteirizacaoDTO> pdvsAux = pdvs;
		
		for (int i=0; i < pdvs.size(); i++) {
			for (int j=i+1; j < pdvsAux.size(); j++) {
	            if (pdvsAux.get(j).getOrdem().equals(pdvs.get(i).getOrdem())) {
	            	throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "[Ordem] inválida !"));
	            }
	        }
		}
		
		if(pdvsAtual!=null){
			for(PdvRoteirizacaoDTO itemPdvDTOAtual:pdvsAtual){
				for(PdvRoteirizacaoDTO itemPdvDTONovo:pdvs){
					if (itemPdvDTOAtual.getOrdem().equals(itemPdvDTONovo.getOrdem())){
						throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Não é permitido mais de um campo [Ordem] com o mesmo valor !"));
					}
				}
			}
		}
	}
	
	/**
	 * Trata PDV's repetidos adicionados
	 * @param pdvs
	 * @param pdvsAtual
	 * @return List<PdvRoteirizacaoDTO>
	 */
    private List<PdvRoteirizacaoDTO> trataPdvsRepetidos(List<PdvRoteirizacaoDTO> pdvs, List<PdvRoteirizacaoDTO> pdvsAtual){
		
    	if(pdvsAtual!=null){
	    	for(PdvRoteirizacaoDTO itemPdvDTOAtual:pdvsAtual){
				for(PdvRoteirizacaoDTO itemPdvDTONovo:pdvs){
					if (itemPdvDTOAtual.getId().equals(itemPdvDTONovo.getId())){
						pdvs.remove(itemPdvDTONovo);
					}
				}
			}
    	}
    
    	return pdvs;
	}
	
	/**
	 * Adiciona PDV's selecionados no "popup de PSV's disponíveis" na lista principal de PDV's
	 */
	@Post
	@Path("/adicionarNovosPdvs")
	public void adicionarNovosPdvs(Long idRota, List<PdvRoteirizacaoDTO> pdvs){

		if (idRota==null){
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Nenhuma [Rota] foi selecionada para a inclusão dos [PDV's] !"));
		}
		
		RoteirizacaoDTO roteirizacaoDTO = this.getDTO();
		
		List<PdvRoteirizacaoDTO> pdvsAtual = null;
		
		if (roteirizacaoDTO==null || roteirizacaoDTO.getRota(idRota)==null){
		
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Cadastre [Box], [Roteiro] e [Rota] antes de cadastrar [PDV].")); 
		}
		else{
			
			pdvsAtual = roteirizacaoDTO.getRota(idRota).getPdvs();
				
			pdvs = this.trataPdvsRepetidos(pdvs, pdvsAtual);
			
			this.validaNovosPdvs(pdvs, pdvsAtual);
			
			roteirizacaoDTO.getRota(idRota).addAllPdv(pdvs);
			
			this.setDTO(roteirizacaoDTO);
			
			this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "PDV adicionado com sucesso."), "result").recursive().serialize(); 
		}    
	}
	
	/**
	 * Remove PDV's selecionados da lista principal de PDV's
	 */
	@Post
	@Path("/removerPdvs")
	public void removerPdvs(Long idRoteiro, Long idRota, List<Long> pdvs){
        
		RotaRoteirizacaoDTO rota = this.getDTO().getRoteiro(idRoteiro).getRota(idRota);
		
		if (pdvs != null){
			
			for (Long cotaId : pdvs){
				
				rota.removerPdv(cotaId);
			}
		}	

		result.use(CustomJson.class).from("").serialize();
	}
	
	@Post
	public void copiarCotasRota(RotaRoteirizacaoDTO rotaCopia) {

		RoteirizacaoDTO roteirizacao = this.getDTO();
		
		if (roteirizacao.getRotaCotasCopia() == null) {

			roteirizacao.setRotaCotasCopia(new ArrayList<RotaRoteirizacaoDTO>());
		}

		roteirizacao.getRotaCotasCopia().add(rotaCopia);

		setDTO(roteirizacao);

		ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, "Cópia realizada com sucesso.");
		
		this.result.use(Results.json()).from(validacao, "result").recursive().serialize();
	}
	
	private void adicionarRoteiro(Integer ordem, String nome){
		
		Long novoId = -1L;
		
		List<RoteiroRoteirizacaoDTO> dtos = this.getDTO().getRoteiros();
		
		for (RoteiroRoteirizacaoDTO dto : dtos){
			
			if (dto.getId() <= novoId){
				
				novoId = dto.getId() - 1;
			}
		}
		
		this.getDTO().addRoteiro(new RoteiroRoteirizacaoDTO(novoId, ordem, nome));
	}
	
	private void adicionarRota(Long roteiroId, Integer ordem, String nome){
		
		List<RotaRoteirizacaoDTO> rotasDto = this.getDTO().getRoteiro(roteiroId).getRotas();
		
		Long novoId = -1L;
		
		for (RotaRoteirizacaoDTO rota : rotasDto){
			
			if (rota.getId() <= novoId){
				
				novoId = rota.getId() - 1;
			}
		}
		
		this.getDTO().getRoteiro(roteiroId).addRota(new RotaRoteirizacaoDTO(novoId, ordem, nome));
	}
	
	@Post
	public void carregarBoxTransferenciaRoteiro(Long idBox){
		
		List<Box> boxs = this.boxService.buscarPorTipo(TipoBox.LANCAMENTO);
		
		List<ItemDTO<Long, String>> lista =
			new ArrayList<ItemDTO<Long,String>>();
	
		for (Box box : boxs) {
			
			if (!box.getId().equals(idBox)){
				lista.add(
					new ItemDTO<Long, String>(box.getId(), box.getNome()));
			}
		}
		
		this.result.use(Results.json()).from(lista, "result").recursive().serialize();
	}
	
	@Post
	public void carregarRoteirosTransferenciaRota(Long idRoteiro){
		
		this.result.use(Results.json()).from(this.getDTO().getRoteiros(), "result").serialize();
	}
	
	@Post
	public void transferirRoteiro(Long idBoxAnterior, Long idRoteiro, Long idBoxNovo){
		
		Map<Long, Set<RoteiroRoteirizacaoDTO>> roteirosTransferidos = getDTO().getRoteirosTransferidos();
		Set<RoteiroRoteirizacaoDTO> roteiros = roteirosTransferidos.get(idBoxNovo);
		
		if (roteiros == null){
			
			roteiros = new HashSet<RoteiroRoteirizacaoDTO>();
			roteirosTransferidos.put(idBoxNovo, roteiros);
		}
		
		long novoId = -1;
		int novaOrdem = 1;
		for (RoteiroRoteirizacaoDTO dto : roteiros){
			
			if (dto.getId() <= novoId){
				
				novoId = dto.getId() - 1;
			}
			
			if (dto.getOrdem() >= novaOrdem){
				
				novaOrdem = dto.getOrdem() + 1;
			}
		}
		
		RoteiroRoteirizacaoDTO novoDTO = new RoteiroRoteirizacaoDTO(novoId, novaOrdem, this.getDTO().getRoteiro(idRoteiro).getNome());
		novoDTO.addAllRota(this.getDTO().getRoteiro(idRoteiro).getRotas());
		
		roteiros.add(novoDTO);
		
		roteiros = roteirosTransferidos.get(idBoxAnterior);
		
		if (roteiros != null){
			
			for (RoteiroRoteirizacaoDTO dto : roteiros){
				
				if (dto.getId().equals(idRoteiro)){
					
					roteiros.remove(dto);
					break;
				}
			}
		}
		
		this.excluirRoteiro(idRoteiro);
	}
	
	private Collection<RoteiroRoteirizacaoDTO> obterRoteirosTransferidos(Long idBox) {
		
		Map<Long, Set<RoteiroRoteirizacaoDTO>> transf = this.getDTO().getRoteirosTransferidos();
		
		if (transf != null){
			
			return transf.get(idBox);
		}
		
		return new ArrayList<RoteiroRoteirizacaoDTO>();
	}
	
	@Post
	public void transferirRota(Long idRoteiroAnterior, Long idRota, Long idRoteiroNovo){
		
		RoteiroRoteirizacaoDTO roteiroDTO = this.getDTO().getRoteiro(idRoteiroAnterior);
		
		RotaRoteirizacaoDTO rotaDTO = null;
		
		if (roteiroDTO != null){
			
			rotaDTO = roteiroDTO.getRota(idRota);
			roteiroDTO.removerRota(idRota);
		}
		
		if (rotaDTO != null){
			
			roteiroDTO = this.getDTO().getRoteiro(idRoteiroNovo);
			
			if (roteiroDTO != null){
				
				roteiroDTO.addRota(rotaDTO);
			}
		}
		
		this.result.use(Results.json()).from("").serialize();
	}
}
