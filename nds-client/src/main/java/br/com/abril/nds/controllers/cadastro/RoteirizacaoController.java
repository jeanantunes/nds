package br.com.abril.nds.controllers.cadastro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.ConsultaRoteirizacaoSumarizadoPorCotaVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.BoxRoteirizacaoDTO;
import br.com.abril.nds.dto.ConsultaRoteirizacaoDTO;
import br.com.abril.nds.dto.CotaDisponivelRoteirizacaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.PdvRoteirizacaoDTO;
import br.com.abril.nds.dto.RotaRoteirizacaoDTO;
import br.com.abril.nds.dto.RoteirizacaoDTO;
import br.com.abril.nds.dto.RoteiroRoteirizacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaRoteirizacaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.OrdenacaoUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
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
@Rules(Permissao.ROLE_CADASTRO_ROTEIRIZACAO)
public class RoteirizacaoController extends BaseController {

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
	private EnderecoService enderecoService;
	
	@Autowired
	private HttpSession session;

	private static final String FILTRO_PESQUISA_ROTEIRIZACAO_SESSION_ATTRIBUTE="filtroPesquisa";
		
	/**
	 * Chave para armazenamento do DTO de roteirização na sessão
	 */
	private static final String ROTEIRIZACAO_DTO_SESSION_KEY = "ROTEIRIZACAO_DTO_SESSION_KEY";
	
	
	@Path("/")
	public void index() {
		
		carregarComboBox();
	}
	
	/**
	 * Carrega o combo de pesquisa por Box
	 */
	private void carregarComboBox() {
		List<Box> boxs = boxService.buscarPorTipo(TipoBox.LANCAMENTO);
		
		List<ItemDTO<Long, String>> lista = new ArrayList<ItemDTO<Long,String>>();
		
		lista.add(new ItemDTO<Long, String>(-1L, "Especial"));
		
		for (Box box : boxs) {
			lista.add(new ItemDTO<Long, String>(box.getId(), box.getNome()));
		}
		
		result.include("listaBox", lista);
	}
	
	
	/**
	 * Carrega o combo de pesquisa por roteiro a partir de um Box
	 * @param boxId
	 */
	@Path("/carregarComboRoteiro")
	public void carregarComboRoteiro(Long boxId) {
		
		List<Roteiro> roteiros = roteirizacaoService.buscarRoteiroDeBox(boxId);
		result.use(Results.json()).from(roteiros, "result").serialize();
	}
	
	/**
	 * Carrega o combo de pesquisa por roteiro a partir de um Box
	 * @param boxId
	 */
	@Path("/carregarComboRoteiroCodigoBox")
	public void carregarComboRoteiroCodigoBox(Long codigoBoxDe, Long codigoBoxAte) {
		
		List<Roteiro> roteiros = roteirizacaoService.buscarRoteiroCodigoBox(codigoBoxDe, codigoBoxAte);
		result.use(Results.json()).from(roteiros, "result").serialize();
	}
	
	
	/**
	 * Carrega o combo de pesquisa por rota a partir de um Roteiro
	 * @param roteiroId
	 */
	@Path("/carregarComboRota")
	public void carregarComboRota(Long roteiroId) {
		
		List<Rota> rotas = roteirizacaoService.buscarRotasPorRoteiro(roteiroId);
		result.use(Results.json()).from(rotas, "result").serialize();
	}
	
	
	@Path("/carregarComboRoteiroEspecial")
	public void carregarComboRoteiroEspecial() {
	
		List<Roteiro> roteiros = roteirizacaoService.buscarRoteiroEspecial();
		result.use(Results.json()).from(roteiros, "result").serialize();
	}
	
	@Post("/carregarRotasEspeciais")
	public void carregarRotasEspeciais() {
		
		List<RotaRoteirizacaoDTO> rotasEspeciais = this.roteirizacaoService.buscarRotasEspeciais();
		
		result.use(Results.json()).from(rotasEspeciais, "result").serialize();
	}
	
	@Post("/carregarBoxTransferenciaRoteiro")
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
	
	@Post("/carregarRoteirosTransferenciaRota")
	public void carregarRoteirosTransferenciaRota(Long idBox){
		
		List<RoteiroRoteirizacaoDTO> listaRoteirosTransferencia = new ArrayList<>();
		
		listaRoteirosTransferencia.addAll(this.getRoteirizacaoDTOSessao().getRoteiros());
		
		listaRoteirosTransferencia.addAll(this.roteirizacaoService.buscarRoteirosNaoAssociadosAoBox(idBox));
		
		this.result.use(Results.json()).from(listaRoteirosTransferencia, "result").serialize();
	}
	
	@Post("/carregarTodasRotas")
    public void carregarTodasRotas(Long idRoteiro){
		
		List<RotaRoteirizacaoDTO> listaRotas = new ArrayList<RotaRoteirizacaoDTO>();
		
		listaRotas.addAll(this.getRoteiroDTOSessao(idRoteiro).getTodasRotas());
		
		listaRotas.addAll(this.roteirizacaoService.obterRotasNaoAssociadasAoRoteiro(idRoteiro));
		
        this.result.use(Results.json()).from(listaRotas, "result").serialize();
    }
	
	/**
	 * Carrega o combos Roteiro e Rota por intervalo de Box
	 * @param codigoBoxDe
	 * @param codigoBoxAte
	 */
	@Post
	@Path("/carregarCombosPorBox")
	public void carregarCombosPorBox(Integer codigoBoxDe, Integer codigoBoxAte) {
		
		List<ItemDTO<Long, String>> boxes = this.roteirizacaoService.getComboTodosBoxes();
		
		List<ItemDTO<Long, String>> rotas = this.roteirizacaoService.getComboRotaPorBox(codigoBoxDe, codigoBoxAte);
		
		List<ItemDTO<Long, String>> roteiros = this.roteirizacaoService.getComboRoteiroPorBox(codigoBoxDe, codigoBoxAte);
		
		result.use(Results.json()).from(Arrays.asList(rotas, roteiros, boxes),"result").recursive().serialize();
	}
	
	/**
	 * Carrega o combo Roteiro e Box por Rota
	 * @param idRota
	 */
	@Post
	@Path("/carregarCombosPorRota")
	public void carregarCombosPorRota(Long idRota) {
		
		List<ItemDTO<Long, String>> rotas = this.roteirizacaoService.getComboTodosRotas();
		
		List<ItemDTO<Long, String>> roteiros = this.roteirizacaoService.getComboRoteiroPorRota(idRota);
		
		List<ItemDTO<Long, String>> boxes = this.roteirizacaoService.getComboBoxPorRota(idRota);
		
		result.use(Results.json()).from(Arrays.asList(roteiros, boxes, rotas),"result").recursive().serialize();
	}
	
	/**
	 * Carrega o combo Rota e Box por Roteiro
	 * @param idRoteiro
	 */
	@Post
	@Path("/carregarCombosPorRoteiro")
	public void carregarCombosPorRoteiro(Long idRoteiro) {
		
		List<ItemDTO<Long, String>> roteiros = this.roteirizacaoService.getComboTodosRoteiros();
		
		List<ItemDTO<Long, String>> rotas = this.roteirizacaoService.getComboRotaPorRoteiro(idRoteiro);
		
		List<ItemDTO<Long, String>> boxes = this.roteirizacaoService.getComboBoxPorRoteiro(idRoteiro);
		
		result.use(Results.json()).from(Arrays.asList(rotas, boxes, roteiros),"result").recursive().serialize();
	}
	
	/**
	 * Carrega o combo Rota por Roteiro
	 * @param idRoteiro
	 */
	@Post
	@Path("/carregarRotasPorRoteiro")
	public void carregarRotasPorRoteiro(Long idRoteiro) {
				
		List<ItemDTO<Long, String>> rotas = this.roteirizacaoService.getComboRotaPorRoteiro(idRoteiro);
				
		result.use(Results.json()).from(rotas,"result").recursive().serialize();
	}
	
	@Path("/obterProximaOrdemRoteiro")
	public void obterProximaOrdemRoteiro() {
		RoteirizacaoDTO roteirizacao = getRoteirizacaoDTOSessao();
	    Integer ordem = roteirizacao.getMaiorOrdemRoteiro();
		ordem++;
		result.use(Results.json()).from(ordem).recursive().serialize();
	}
	
	
	@Path("/obterProximaOrdemRota")
	public void iniciaTelaRota(Long idRoteiro) {
		
		RoteiroRoteirizacaoDTO roteiro = this.getRoteiroDTOSessao(idRoteiro);

		int ordem = roteiro.getMaiorOrdemRota();
		
		ordem++;
		
		result.use(Results.json()).from(ordem).recursive().serialize();
	}
	
	
	@Path("/iniciaTelaCotas")
	public void iniciaTelaCotas(boolean boxEspecial) {
		List<String> uf;
		if(!boxEspecial) {
			uf = enderecoService.obterUnidadeFederativaPDVSemRoteirizacao();
		} else {
			uf = enderecoService.obterUnidadeFederacaoBrasil();
		}
		
		result.use(Results.json()).from(uf, "result").serialize();
	}
	
	
	/**
	 * Inclui um novo roteiro na roteitização
	 * 
	 * @param idBox
	 * @param ordem
	 * @param nome
	 * @param tipoRoteiro
	 */
	@Path("/incluirRoteiro")
	public void incluirRoteiro(Long idBox, Integer ordem, String nome, TipoRoteiro tipoRoteiro) {
		
		if(idBox == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Selecione um Box para adicionar um roteiro.");
		}
		
		this.validarDadosInclusao(ordem, nome);
		
		this.adicionarRoteiro(ordem, nome);
		
		OrdenacaoUtil.sortList(this.getRoteirizacaoDTOSessao().getRoteiros());
		
		result.use(Results.json()).from(this.getRoteirizacaoDTOSessao(), "result").recursive().serialize();
	}
	
	
	/**
	 * Inclui uma nova rota na roteirização
	 * 
	 * @param roteiroId
	 * @param ordem
	 * @param nome
	 */
	@Path("/incluirRota")
	public void incluirRota(Long roteiroId, Integer ordem, String nome) {
		
		this.validarDadosInclusao(ordem, nome);
		
		this.adicionarRota(roteiroId, ordem, nome);
		
		OrdenacaoUtil.sortList(this.getRoteirizacaoDTOSessao().getRoteiro(roteiroId).getRotas());
		
		result.use(Results.json()).from(this.getRoteirizacaoDTOSessao().getRoteiro(roteiroId), "result").recursive().serialize();
	}
	
	
	/**
	 * Remove uma rota de um roteiro
	 * 
	 * @param rotaId
	 * @param roteiroId
	 */
	@Post("/excluirRota")
	public void excluirRota(Long rotaId, Long roteiroId, Integer ordemRota) {
		
		roteirizacaoService.validarAssociacaoRotaTransportador(rotaId, roteiroId);
		
		this.getRoteiroDTOSessao(roteiroId).removerRota(ordemRota);
		
		result.use(Results.json()).from("", "result").serialize();
	}

	/**
	 * Remove um roteiro de uma roteirização
	 * 
	 * @param roteiroId
	 */
	@Post("/excluirRoteiro")
	public void excluirRoteiro(Long roteiroId, Integer ordemRoteiro) {
		
		roteirizacaoService.validarAssociacaoRoteiroTransportador(roteiroId);
		
		this.getRoteirizacaoDTOSessao().removerRoteiro(roteiroId);
		
		result.use(Results.json()).from("", "result").serialize();
	}
	
	
	/**
	 * Remove uma roteirização
	 * 
	 * @param roteirizacaoId
	 */
	@Path("/excluirRoteirizacao")
	public void excluirRoteirizacao(List<Long> roteirizacaoId) {
		roteirizacaoService.excluirRoteirizacao(roteirizacaoId);
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Roteirização excluida com sucesso."),"result").recursive().serialize();
	}	
		
		
	@Post("/buscaRoteiroPorDescricao")
	public void buscaRoteiroPorDescricao(String descricao) {
		
		List<Roteiro> listaRoteiro = roteirizacaoService.buscarRoteiroPorDescricao(descricao, MatchMode.EXACT);
		
		if (listaRoteiro.isEmpty()){
			
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
	@Post("/buscaRotasPorRoteiro")
	public void buscaRotasPorRoteiro(Long roteiroId, int rp, int page) {
		
		List<RotaRoteirizacaoDTO> listaRotasDTO = this.getRoteiroDTOSessao(roteiroId).getRotas();
		
		result.use(Results.json()).from(listaRotasDTO, "result").recursive().serialize();
	}
	
	
	@Post("/buscarRoteirosSessao")
	public void buscarRoteirosSessao() {
		result.use(Results.json()).from(this.getRoteirizacaoDTOSessao().getRoteiros(), "result").recursive().serialize();
	}
	
	
	@Path("/buscalistaMunicipio")
	public void buscalistaMunicipio(String uf, boolean boxEspecial) {
		List<String> lista;
		if(!boxEspecial) {
			lista = enderecoService.obterLocalidadesPorUFPDVSemRoteirizacao(uf);
		} else {
			lista = enderecoService.obterLocalidadesPorUFPDVBoxEspecial(uf);
		}
		result.use(Results.json()).from(lista, "result").serialize();
	}
	
	
	@Path("/buscalistaBairro")
	public void buscalistaBairro(String uf, String municipio, boolean boxEspecial) {
		List<String> bairros;
		if(!boxEspecial) {
			bairros = enderecoService.obterBairrosPDVSemRoteirizacao(uf, municipio);
		} else {
			bairros = enderecoService.obterBairrosPDVBoxEspecial(uf, municipio);
		}	
		result.use(Results.json()).from(bairros, "result").serialize();
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

	
	@Path("/buscarPvsPorEndereco")
	public void buscarPvsPorEndereco(String CEP, String uf, String municipio, String bairro , Long rotaId, Long roteiroId,  String sortname, String sortorder, int rp, int page) {
		List<CotaDisponivelRoteirizacaoDTO> lista = roteirizacaoService.buscarRoteirizacaoPorEndereco(CEP, uf, municipio, bairro,  rotaId , roteiroId );
		int quantidade = lista.size();
		result.use(FlexiGridJson.class).from(lista).total(quantidade).page(page).serialize();
	}
	
	
	@Post("/autoCompletarRotaPorDescricao")
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
	
	
	@Post("/autoCompletarRoteiroPorDescricao")
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
					this.getNDSFileHeader(), filtroSessao, 
					lista, ConsultaRoteirizacaoDTO.class, this.response);
		
		} else {
			
			List<ConsultaRoteirizacaoSumarizadoPorCotaVO> listaConsultaRoteirizacaoSumarizado =
				this.getConsultaRoteirizacaoSumarizadoPorCotaVO(lista);
			
			FileExporter.to("roteirizacao", fileType)
				.inHTTPResponse(
					this.getNDSFileHeader(), filtroSessao, 
					listaConsultaRoteirizacaoSumarizado, 
					ConsultaRoteirizacaoSumarizadoPorCotaVO.class, this.response);
		}
		
		this.result.nothing();
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


	private List<ConsultaRoteirizacaoDTO> realizarPesquisaRoteirizacao(FiltroConsultaRoteirizacaoDTO filtroSessao, boolean isPesquisaSumarizadaPorCota) {
		
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
		try {
			
			List<ConsultaRoteirizacaoDTO> lista = roteirizacaoService.buscarRoteirizacaoPorNumeroCota(numeroCota, tipoRoteiro, sortname, Ordenacao.valueOf(sortorder.toUpperCase()), page*rp - rp , rp);
			
			FileExporter.to("roteirizacao", fileType).inHTTPResponse(this.getNDSFileHeader(), null, 
					lista, ConsultaRoteirizacaoDTO.class, this.response);
			
		} catch (Exception e) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Erro ao gerar o arquivo!"));
		}

		this.result.use(Results.nothing());
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
	
	
	/**
	 * Obtém dados das cotas sumarizados (Detalhamento)
	 * 
	 * @param idBox
	 * @param idRota
	 * @param idRoteiro
	 */
	public void obterCotasSumarizadas(Long idBox, Long idRota, Long idRoteiro,
			String sortname, String sortorder) {
		
		List<ConsultaRoteirizacaoDTO> lista = roteirizacaoService.obterCotasParaBoxRotaRoteiro(idBox,idRota,idRoteiro,
				sortname, sortorder);
		
		result.use(FlexiGridJson.class).from(lista).total(lista.size()).page(1).serialize();
	}
	
	//NOVA ROTEIRIZAÇÃO
	@Post
	@Path("/boxSelecionado")
	public void boxSelecionado(Long idBox, String nomeBox) {
	   
	   RoteirizacaoDTO roteirizacaoDTOExistente = null; 
	   
	   RoteirizacaoDTO roteirizacaoDTO = getRoteirizacaoDTOSessao();
	   
	   roteirizacaoDTOExistente = roteirizacaoService.obterRoteirizacaoPorBox(idBox);
	   
	   if (roteirizacaoDTOExistente != null) {
	       roteirizacaoDTO = setRoteirizacaoDTOSessao(roteirizacaoDTOExistente);
	       roteirizacaoDTO.filtarBox(nomeBox);
	   } else {
	       roteirizacaoDTO.reset(idBox);
	   }
	   
	   roteirizacaoDTO.addAllRoteiro(this.obterRoteirosTransferidos(idBox));
	   	   	   	   
	   result.use(CustomJson.class).from(roteirizacaoDTO).serialize();
	}

	@Post
	@Path("/recarregarCotasRota")
	public void recarregarCotasRota(Long idRoteiro, Long idRota, Integer ordemRota, String sortname, String sortorder) {
	   
		RotaRoteirizacaoDTO rota = this.getRotaDTOSessaoPelaOrdem(ordemRota, idRoteiro);
	    
		List<PdvRoteirizacaoDTO> pdvs = rota.getPdvs();
	    
	    if (pdvs != null){
	    	ordenarPdvsPeloIndiceDaLista(rota);
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
	    RoteirizacaoDTO roteirizacao = getRoteirizacaoDTOSessao();
	    roteirizacao.filtarBox(nomeBox);
	    List<BoxRoteirizacaoDTO> boxes = roteirizacao.getBoxDisponiveis();
        result.use(FlexiGridJson.class).from(boxes).total(boxes.size()).page(1).serialize();
	}
	
	@Post
    @Path("/recarregarRoteiros")
    public void recarregarRoteiros(String descricaoRoteiro) {
        RoteirizacaoDTO roteirizacao = getRoteirizacaoDTOSessao();
        roteirizacao.filtarRoteiros(descricaoRoteiro);
        List<RoteiroRoteirizacaoDTO> roteiros = roteirizacao.getRoteiros();
        result.use(FlexiGridJson.class).from(roteiros).total(roteiros.size()).page(1).serialize();
    }
	
	@Post
    @Path("/recarregarRotas")
    public void recarregarRotas(Long idRoteiro, String descricaoRota) {
        
		RoteiroRoteirizacaoDTO roteiro = this.getRoteiroDTOSessao(idRoteiro);
        
		roteiro.filtarRotas(descricaoRota);
        List<RotaRoteirizacaoDTO> roteiros = roteiro.getRotas();
        result.use(FlexiGridJson.class).from(roteiros).total(roteiros.size()).page(1).serialize();
    }
	
	@Post
    @Path("/ordemPdvChangeListener")
	public void ordemPdvChangeListener(Long idRoteiro, Long idRota, Long idPdv, Integer ordem) {
	   
	    if (ordem == null) {
	    
	    	result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.ERROR, "Ordem é obrigatória!"), "result").recursive().serialize();
	  
	    } else if (ordem < 1) { 
	    
	    	result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.ERROR, "Ordem inválida!"), "result").recursive().serialize();
	    
	    } else {
	      	        
	    	RotaRoteirizacaoDTO rota = this.getRotaDTOSessaoPeloID(idRota, idRoteiro);	    	
	        
	    	List<PdvRoteirizacaoDTO> pdvsAtuais = rota.getPdvs();
	    	
	        PdvRoteirizacaoDTO pdv = rota.getPdv(idPdv);
	        
	        pdvsAtuais.remove(pdv);
	        
	        pdv.setOrdem(ordem);
	     
	       OrdenacaoUtil.reordenarLista(pdv, pdvsAtuais);
	       
	       pdvsAtuais.add(pdv.getOrdem() - 1, pdv);
	       
	       ordenarPdvsPeloIndiceDaLista(rota);
	       
	       result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Ordem válida!"), "result").recursive().serialize(); 
	    }
	}

	/**
	 * Método responsável pela obtenção dos dados que irão preencher o combo de UF's.
	 * @param tela
	 */
	@Post
    @Path("/obterDadosComboUF")
	public void obterDadosComboUF() {
		
		List<String> ufs = this.enderecoService.obterUnidadeFederativaPDVSemRoteirizacao();
		
		this.result.use(Results.json()).from(ufs, "result").serialize();
	}
	
	/**
	 * Obtém PDV's para a inclusão de rota pdv na roteirização
	 */
	@Post
	@Path("/obterPdvsDisponiveis")
	public void obterPdvsDisponiveis(Integer numCota, String municipio, String uf, String bairro, 
			String cep, boolean pesquisaPorCota, Long idRoteiro, Long idRota, Long boxID, String sortname, String sortorder){
        
		List<PdvRoteirizacaoDTO> lista = 
			this.roteirizacaoService.obterPdvsDisponiveis(numCota, municipio, uf, bairro, cep, pesquisaPorCota, boxID);
		
		verificarExistenciaPDVsRotaAtual(idRoteiro, idRota, lista);
		
		Ordenacao ordenacao = Util.getEnumByStringValue(Ordenacao.values(), sortorder);
		PaginacaoUtil.ordenarEmMemoria(lista, ordenacao, sortname);
		
		result.use(FlexiGridJson.class).from(lista).total(lista.size()).page(1).serialize();
	}

	private void verificarExistenciaPDVsRotaAtual(Long idRoteiro, Long idRota, List<PdvRoteirizacaoDTO> lista) {
		
		RoteirizacaoDTO roteirizacao = (RoteirizacaoDTO) session.getAttribute(ROTEIRIZACAO_DTO_SESSION_KEY);
		
		if (roteirizacao != null) {
			
			List<RoteiroRoteirizacaoDTO> roteiros = roteirizacao.getRoteiros();
			
			if (roteiros != null && !roteiros.isEmpty()) {
				
				RoteiroRoteirizacaoDTO roteiroSelecionado = null;
				
				for (RoteiroRoteirizacaoDTO roteiro : roteiros) {
					
					if (roteiro.getId().equals(idRoteiro)) {
						
						roteiroSelecionado = roteiro;
						break;
					}
				}
				
				if (roteiroSelecionado != null) {
					
					List<RotaRoteirizacaoDTO> rotas = roteiroSelecionado.getRotas();
					
					if (rotas != null && !rotas.isEmpty()) {
						
						RotaRoteirizacaoDTO rotaSelecionada = null;
						
						for (RotaRoteirizacaoDTO rota : rotas) {
							
							if (rota.getId().equals(idRota)) {
								
								rotaSelecionada = rota;
								break;
							}
						}
						
						if (rotaSelecionada != null) {
							
							List<PdvRoteirizacaoDTO> pdvs = rotaSelecionada.getPdvs();
							
							if (pdvs != null) {
								
								lista.removeAll(pdvs);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Recupera o DTO de rota da Sessão, pertencente a um roteiro, pela Ordem
	 * 
	 * @param idRota
	 * @param idRoteiro
	 * @return
	 */
	private RotaRoteirizacaoDTO getRotaDTOSessaoPelaOrdem(Integer ordemRota, Long idRoteiro) {
	
		RoteiroRoteirizacaoDTO roteiroDTO = this.getRoteiroDTOSessao(idRoteiro);
		RotaRoteirizacaoDTO rotaDTO = roteiroDTO.getRotaByOrdem(ordemRota);
		
		if (rotaDTO == null) {
			throw new ValidacaoException(new ValidacaoVO(
				TipoMensagem.ERROR, "Falha ao obter Rota da sessão com a Ordem: "+ordemRota));
		}
		
		return rotaDTO;
	}
	
	
	/**
	 * Recupera o DTO de rota da Sessão, pertencente a um roteiro, pelo ID
	 * 
	 * @param idRota
	 * @param idRoteiro
	 * @return
	 */
	private RotaRoteirizacaoDTO getRotaDTOSessaoPeloID(Long idRota, Long idRoteiro) {
	
		RoteiroRoteirizacaoDTO roteiroDTO = this.getRoteiroDTOSessao(idRoteiro);
		RotaRoteirizacaoDTO rotaDTO = roteiroDTO.getRota(idRota);
		
		if (rotaDTO == null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Falha ao obter Rota da sessão com o ID: "+idRota));
		}
		
		return rotaDTO;
	}
	
	/**
	 * Recupera o DTO de roteiro da sessão pelo ID
	 * 
	 * @param idRoteiro
	 * @return 
	 */
	private RoteiroRoteirizacaoDTO getRoteiroDTOSessao(Long idRoteiro) {
		
		RoteirizacaoDTO roteirizacaoDTOSessao = this.getRoteirizacaoDTOSessao();
		
		RoteiroRoteirizacaoDTO roteiroDTO = roteirizacaoDTOSessao.getRoteiro(idRoteiro);
		
		if (roteiroDTO == null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Falha ao obter roteiro da sessão com o ID: "+idRoteiro));
		}
		
		return roteiroDTO;
	}
	
	/**
	 * Recupera o DTO de roteirização da sessão
	 * @return DTO armazenado na sessão
	 */
	private RoteirizacaoDTO getRoteirizacaoDTOSessao() {
		
		RoteirizacaoDTO roteirizacao = (RoteirizacaoDTO) session.getAttribute(ROTEIRIZACAO_DTO_SESSION_KEY);
		
		if (roteirizacao == null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Falha ao obter roteirização da sessão"));
		}
		
	    return roteirizacao;
	}
	
	/**
	 * Armazena o DTO de roteirização na sessão
	 * @param dto DTO com as informações da roteirização 
	 */
	private RoteirizacaoDTO setRoteirizacaoDTOSessao(RoteirizacaoDTO dto) {
	    session.setAttribute(ROTEIRIZACAO_DTO_SESSION_KEY, dto);
	    return dto;
	}
	
	/**
	 * Remove o DTO de roteirização da sessão da sessão
	 */
	private void limparRoteirizacaoDTOSessao() {
	    setRoteirizacaoDTOSessao(null);
	}
	
	
	@Post
    @Path("/novaRoteirizacao")
	@Rules(Permissao.ROLE_CADASTRO_ROTEIRIZACAO_ALTERACAO)
	public void novaRoteirizacao() {
	    List<Box> disponiveis = roteirizacaoService.obterListaBoxLancamento(null);
	    List<BoxRoteirizacaoDTO> dtos = BoxRoteirizacaoDTO.toDTOs(disponiveis);
	    RoteirizacaoDTO dto = RoteirizacaoDTO.novaRoteirizacao(dtos);
	    setRoteirizacaoDTOSessao(dto);
	    
	    result.use(CustomJson.class).from(dto).serialize();
	}
	
	
	@Post
	@Path("/editarRoteirizacao")
	public void editarRoteirizacao(Long idRoteirizacao) {
	    RoteirizacaoDTO dto = roteirizacaoService.obterRoteirizacaoPorId(idRoteirizacao);
	    setRoteirizacaoDTOSessao(dto);
	    result.use(CustomJson.class).from(dto).serialize();
	}
	
	@Post
    @Path("/confirmarRoteirizacao")
	public void confirmarRoteirizacao() {
	    
		RoteirizacaoDTO roteirizacaoDTO = getRoteirizacaoDTOSessao();
	    		    
		roteirizacaoService.confirmarRoteirizacao(roteirizacaoDTO);
	    
		limparRoteirizacaoDTOSessao();
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Roteirização confirmada com sucesso."),"result").recursive().serialize();
	}
	
    @Post
    @Path("/cancelarRoteirizacao")
	public void cancelarRoteirizacao() {
	    limparRoteirizacaoDTOSessao();
	    result.nothing();
	}
	
    
	/**
	 * Verifica se PDV's podem ser adicionados na Roteirização
	 * @param pdvs
	 */
	private void validaNovosPdvs(List<PdvRoteirizacaoDTO> pdvs, List<PdvRoteirizacaoDTO> pdvsAtual){
		
		RoteirizacaoDTO roteirizacao = getRoteirizacaoDTOSessao();
		
		Long idBox = null;
		
		if (!roteirizacao.isBoxEspecial()) {
		    idBox = roteirizacao.getBox().getId();
		}
		
		for(PdvRoteirizacaoDTO itemPdvDTO:pdvs){
			
			if (!this.roteirizacaoService.verificaDisponibilidadePdv(itemPdvDTO.getId(), idBox)){
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "O [PDV "+itemPdvDTO.getId()+" "+itemPdvDTO.getPdv()+"] já pertence à um [Box] roteirizado !"));
			}
		}
	}
	
	private void validarDadosInclusao(Integer ordem, String nome) {
		
		List<String> mensagens = new ArrayList<String>();
		
		if(ordem == null){
			
			mensagens.add("O campo Ordem é obrigatório.");
		} else if (ordem <= 0) {
            mensagens.add("O campo Ordem deve ser maior que 0");
        } 
		
		if(nome == null || nome.isEmpty()){
			
			mensagens.add("O campo Nome é obrigatório.");
		}
			
		if (!mensagens.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagens));
		}
	}
	
	/**
	 * Verifica o campo Ordem dos PDV's
	 * @param pdvs
	 * @param pdvsAtual
	 */
	private void verificaOrdemPdvs(List<PdvRoteirizacaoDTO> pdvs, List<PdvRoteirizacaoDTO> pdvsAtual){
		
		List<PdvRoteirizacaoDTO> pdvsAux = pdvs;
		
		for (int i=0; i < pdvs.size(); i++) {
			for (int j = i+1; j < pdvsAux.size(); j++) {
	            if (pdvsAux.get(j).getOrdem().equals(pdvs.get(i).getOrdem())) {
	            	throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "[Ordem] inválida !"));
	            }
	        }
		}
		
		if(pdvsAtual != null) {
			
			OrdenacaoUtil.reordenarListas(pdvs, pdvsAtual);
		}
		
	}
	
	
	/**
	 * Trata PDV's repetidos adicionados
	 * @param pdvs
	 * @param pdvsAtual
	 * @return List<PdvRoteirizacaoDTO>
	 */
    private List<PdvRoteirizacaoDTO> trataPdvsRepetidos(List<PdvRoteirizacaoDTO> pdvs, List<PdvRoteirizacaoDTO> pdvsAtual){
		
		if (pdvsAtual != null && pdvs != null) {

			for (PdvRoteirizacaoDTO itemPdvDTOAtual : pdvsAtual) {

				pdvs.remove(itemPdvDTOAtual);

			}

		}
    
    	return pdvs;
	}
	
	/**
	 * Adiciona PDV's selecionados no "popup de PSV's disponíveis" na lista principal de PDV's
	 */
	@Post
	@Path("/adicionarNovosPdvs")
	public void adicionarNovosPdvs(Long idRoteiro, Long idRota, Integer ordemRota, List<PdvRoteirizacaoDTO> pdvs){
		
		RotaRoteirizacaoDTO rota = this.getRotaDTOSessaoPelaOrdem(ordemRota, idRoteiro);
		
		List<PdvRoteirizacaoDTO> pdvsAtual = rota.getPdvs();
				
		pdvs = this.trataPdvsRepetidos(pdvs, pdvsAtual);
		
		this.validaNovosPdvs(pdvs, pdvsAtual);
		
		this.verificaOrdemPdvs(pdvs, pdvsAtual);
		
		for(PdvRoteirizacaoDTO pdv : pdvs) {
			
			if(pdv.getOrdem() < 0) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Ordem inválida. Valor inferior ao primeiro elemento da lista.");
			}
			
			rota.getPdvs().add(pdv);
		}
		
		Collections.sort(rota.getPdvs());
		
		ordenarPdvsPeloIndiceDaLista(rota);
			
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "PDV adicionado com sucesso."), "result").recursive().serialize(); 

	}

	private void ordenarPdvsPeloIndiceDaLista(RotaRoteirizacaoDTO rota) {
		
		if(rota != null && rota.getPdvs() != null) {
			
			for(int i = 0; i < rota.getPdvs().size(); i++ ) {
				rota.getPdvs().get(i).setOrdem(i+1);
			}
		}
		
	}
	
	/**
	 * Remove PDV's selecionados da lista principal de PDV's
	 */
	@Post("/removerPdvs")
	public void removerPdvs(Long idRoteiro, Long idRota, Integer ordemRota, List<Long> pdvs){
        
		RotaRoteirizacaoDTO rota = this.getRotaDTOSessaoPelaOrdem(ordemRota, idRoteiro);
		
		if (pdvs != null){
			
			for (Long cotaId : pdvs) {
				
				rota.removerPdv(cotaId);
			}
		}	
		
		ordenarPdvsPeloIndiceDaLista(rota);

		result.use(CustomJson.class).from("").serialize();
	}
	
	@Post("/copiarCotasRota")
	public void copiarCotasRota(Long idRoteiro, RotaRoteirizacaoDTO rotaCopia) {
		
		Rota rota = this.roteirizacaoService.buscarRotaPorId(rotaCopia.getId());
		
		RotaRoteirizacaoDTO rotaDTO = RotaRoteirizacaoDTO.getDTOFrom(rota);
		
		rotaDTO.addPdvsAposMaiorOrdem(rotaCopia.getPdvs());
		
		this.getRoteirizacaoDTOSessao().addRotaNovosPDVsTransferidos(rotaDTO);
		
		ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, "Cópia realizada com sucesso.");
		
		result.use(Results.json()).from(validacao, "result").recursive().serialize();
	}
	
	private void adicionarRoteiro(Integer ordem, String nome){
		
		Long novoId = -1L;
						
		for (RoteiroRoteirizacaoDTO dto : this.getRoteirizacaoDTOSessao().getRoteiros()){
			
			if (dto.getId() <= novoId){
				
				novoId = dto.getId() - 1;
			}
		}
		
		RoteiroRoteirizacaoDTO roteiroDTO = new RoteiroRoteirizacaoDTO(novoId, ordem, nome);
		
		this.roteirizacaoService.carregarRotasEntregadores(roteiroDTO);
		
		OrdenacaoUtil.reordenarLista(roteiroDTO, this.getRoteirizacaoDTOSessao().getRoteiros());
		
		this.getRoteirizacaoDTOSessao().addRoteiro(roteiroDTO);
		
	}
	
	private RotaRoteirizacaoDTO adicionarRota(Long roteiroId, Integer ordem, String nome){
		
		List<RotaRoteirizacaoDTO> rotasDto = this.getRoteiroDTOSessao(roteiroId).getRotas();
		
		Long novoId = -1L;
		
		for (RotaRoteirizacaoDTO rota : rotasDto){
			
			if (rota.getId() != null && rota.getId() <= novoId){
				
				novoId = rota.getId() - 1;
			}
		}
		
		RotaRoteirizacaoDTO rota = new RotaRoteirizacaoDTO(novoId, ordem, nome);
        this.getRoteirizacaoDTOSessao().getRoteiro(roteiroId).addRota(rota);
        return rota;
	}

	/**
	 * Transfere o roteiro de um box para um novo box
	 * 
	 * @param idBoxAnterior
	 * @param idRoteiro
	 * @param idBoxNovo
	 */
	@Post("/transferirRoteiro")
	public void transferirRoteiro(Long idBoxAnterior, Long idRoteiro, Long idBoxNovo){
		
		RoteirizacaoDTO roteirizacaoDTO = getRoteirizacaoDTOSessao();
	    
		Map<Long, Set<RoteiroRoteirizacaoDTO>> mapRoteirosTransferidos = roteirizacaoDTO.getRoteirosTransferidos();
		Set<RoteiroRoteirizacaoDTO> roteirosTransferidos = mapRoteirosTransferidos.get(idBoxNovo);
		
		if (roteirosTransferidos == null){
			
			roteirosTransferidos = new HashSet<RoteiroRoteirizacaoDTO>();
			mapRoteirosTransferidos.put(idBoxNovo, roteirosTransferidos);
		}
	
		RoteiroRoteirizacaoDTO roteiroDTO = roteirizacaoDTO.getRoteiro(idRoteiro);
		
		OrdenacaoUtil.reordenarLista(roteiroDTO, new ArrayList<RoteiroRoteirizacaoDTO>(roteirosTransferidos));
		
		roteirosTransferidos.add(roteiroDTO);
				
		this.getRoteirizacaoDTOSessao().removerRoteiroTransferido(idRoteiro);
		result.use(CustomJson.class).from("").serialize();
	
	}
	
	private Collection<RoteiroRoteirizacaoDTO> obterRoteirosTransferidos(Long idBox) {
		
		Map<Long, Set<RoteiroRoteirizacaoDTO>> transf = this.getRoteirizacaoDTOSessao().getRoteirosTransferidos();
		
		if (transf != null){
			
			return transf.get(idBox);
		}
		
		return new ArrayList<RoteiroRoteirizacaoDTO>();
	}
	
	@Path("/transferirRotasComNovoRoteiro")
	public void transferirRotasComNovoRoteiro(List<Long> rotasId, Long idBox, Integer ordem, String roteiroNome, TipoRoteiro tipoRoteiro) {
		
		if(idBox == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Selecione um Box para adicionar um roteiro.");
		}
		
		Roteiro roteiro = populaRoteiro(idBox, ordem, roteiroNome, tipoRoteiro);
		
		validarDadosInclusao(ordem, roteiroNome);
		
		roteirizacaoService.transferirListaRotaComNovoRoteiro(rotasId, roteiro);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Rotas transferidas com sucesso."),"result").recursive().serialize();
	}

	
	/**
	 * Transfere a rota de um roteiro para um novo roteiro
	 * 
	 * @param idRoteiroAnterior
	 * @param idRota
	 * @param idRoteiroNovo
	 */
	@Post("/transferirRota")
	public void transferirRota(Long idRoteiroAnterior, Long idRota,  Long idRoteiroNovo){
		
		RoteirizacaoDTO roteirizacaoDTO = this.getRoteirizacaoDTOSessao();
		
		RoteiroRoteirizacaoDTO roteiroDTOAnterior = this.getRoteirizacaoDTOSessao().getRoteiro(idRoteiroAnterior);
		
		RotaRoteirizacaoDTO rotaDTO = this.getRotaDTOSessaoPeloID(idRota, idRoteiroAnterior);
		
		roteiroDTOAnterior.removerRotaTransferida(rotaDTO.getOrdem());
		
		RoteiroRoteirizacaoDTO roteiroDTONovo = this.getRoteirizacaoDTOSessao().getRoteiro(idRoteiroNovo);
		
		if (roteiroDTONovo == null) {
			
			Roteiro roteiro = this.roteirizacaoService.buscarRoteiroPorId(idRoteiroNovo);
			
			List<Rota> listaRotas = this.roteirizacaoService.buscarRotasPorRoteiro(roteiro.getId());
			
			roteiroDTONovo = roteirizacaoDTO.obterRoteirosNovasRotasTransferidas(roteiro, listaRotas);
			
		}
	
		OrdenacaoUtil.reordenarLista(rotaDTO, roteiroDTONovo.getRotas());
				
		roteiroDTONovo.addRota(rotaDTO);
		
		this.result.use(Results.json()).from("").serialize();
	}
	
	@Post("/transferirPDVs")
    public void transferirPDVs(Long idRoteiro, Long idRotaAnterior, String _idRotaNova, Integer ordemRota, List<Long> pdvs){
		
		Long idRotaNova = null;
		Integer novaOrdem = null;
		
		if (_idRotaNova != null){
			if (_idRotaNova.contains("_")){
				
				novaOrdem = Integer.parseInt(_idRotaNova.replaceFirst("_", ""));
			} else {
				
				idRotaNova = Long.parseLong(_idRotaNova);
			}
		}
		
        RotaRoteirizacaoDTO rotaAnterior = this.getRotaDTOSessaoPeloID(idRotaAnterior, idRoteiro);
        
        List<PdvRoteirizacaoDTO> pdvsTransferencia = new ArrayList<PdvRoteirizacaoDTO>(pdvs.size());
        
        for (Long idPdv : pdvs) {
            PdvRoteirizacaoDTO pdv = rotaAnterior.getPdv(idPdv);
            pdvsTransferencia.add(pdv);
            rotaAnterior.removerPdv(idPdv);
        }
        
        RotaRoteirizacaoDTO rotaNovaDTO;
        
        try {
        	if (idRotaNova == null){
        		
        		rotaNovaDTO = this.getRotaDTOSessaoPelaOrdem(novaOrdem, idRoteiro);
        	} else {
        		
        		rotaNovaDTO = this.getRotaDTOSessaoPeloID(idRotaNova, idRoteiro);
        	}
        
        } catch (ValidacaoException ve) {
        	
        	Rota rota = this.roteirizacaoService.buscarRotaPorId(idRotaNova);

        	RoteirizacaoDTO roteirizacaoDTO = this.getRoteirizacaoDTOSessao();
        	
        	rotaNovaDTO = roteirizacaoDTO.obterRotaNovosPDVsTransferidos(rota);
        
        }
       
        rotaNovaDTO.addPdvsAposMaiorOrdem(pdvsTransferencia);
        
        ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, "Transferência realizada com sucesso!");
        
        result.use(Results.json()).from(validacao, "result").recursive().serialize();
    }
	
	@Post("/recarregarListaRotas")
	public void recarregarListaRotas(Long roteiro,Long idBox) {

		List<Rota> rotas = null;

		if (roteiro != null) {

			rotas = roteirizacaoService.buscarRotasPorRoteiro(roteiro);
		} 
		else if (idBox!= null) {

			rotas = roteirizacaoService.buscarRotaDeBox(idBox);
		}
		else{
			
			rotas = roteirizacaoService.buscarRotas();
		}

		result.use(Results.json()).from(getRotas(rotas), "result").recursive()
				.serialize();
	}
	
	@Post("/recarregarRoteiroRota")
	public void recarregarRoteiroRota(Long idBox) {

		List<Roteiro> roteirosBox = null;

		List<Rota> rotas = null;

		if (idBox != null) {

			roteirosBox = roteirizacaoService.buscarRoteiroDeBox(idBox);
			rotas = roteirizacaoService.buscarRotaDeBox(idBox);
		} else {

			roteirosBox = roteirizacaoService.buscarRoteiros();
			rotas = roteirizacaoService.buscarRotas();
		}

		Map<String, Object> mapa = new TreeMap<String, Object>();
		mapa.put("rotas", getRotas(rotas));
		mapa.put("roteiros", getRoteiros(roteirosBox));

		result.use(CustomJson.class).from(mapa).serialize();
	}
	
	/**
	 * Retorna uma lista de roteiros no formato ItemDTO
	 * 
	 * @param roteiros
	 *            - lista de roteiros
	 * @return List<ItemDTO<Long, String>>
	 */
	private List<ItemDTO<Long, String>> getRoteiros(List<Roteiro> roteiros) {

		List<ItemDTO<Long, String>> listaRoteiros = new ArrayList<ItemDTO<Long, String>>();

		for (Roteiro roteiro : roteiros) {

			listaRoteiros.add(new ItemDTO<Long, String>(roteiro.getId(),
					roteiro.getDescricaoRoteiro()));
		}
		return listaRoteiros;
	}
	
	/**
	 * Retorna uma lista de Rota no formato ItemDTO
	 * 
	 * @param rotas
	 * @return List<ItemDTO<Long, String>>
	 */
	private List<ItemDTO<Long, String>> getRotas(List<Rota> rotas) {

		List<ItemDTO<Long, String>> listaRotas = new ArrayList<ItemDTO<Long, String>>();

		for (Rota rota : rotas) {

			listaRotas.add(new ItemDTO<Long, String>(rota.getId(), rota
					.getDescricaoRota()));
		}

		return listaRotas;
	}
}
