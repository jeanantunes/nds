package br.com.abril.nds.controllers.cadastro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.PdvVO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.PeriodoFuncionamentoDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.TipoLicencaMunicipalDTO;
import br.com.abril.nds.dto.TipoPontoPDVDTO;
import br.com.abril.nds.dto.filtro.FiltroPdvDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.ParametroSistemaService;
import br.com.abril.nds.model.cadastro.CodigoDescricao;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.cadastro.pdv.StatusPDV;
import br.com.abril.nds.model.cadastro.pdv.TamanhoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoCaracteristicaSegmentacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoEstabelecimentoAssociacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPeriodoFuncionamentoPDV;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.PdvService;
import br.com.abril.nds.service.exception.EnderecoUniqueConstraintViolationException;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/pdv")
public class PdvController {
	
	public static final String LISTA_TELEFONES_SALVAR_SESSAO = "listaTelefonesSalvarSessaoPDV";
	
	public static final String LISTA_TELEFONES_REMOVER_SESSAO = "listaTelefonesRemoverSessaoPDV";
	
	public static final String LISTA_TELEFONES_EXIBICAO = "listaTelefonesExibicaoPDV";
	
	public static String LISTA_ENDERECOS_SALVAR_SESSAO = "listaEnderecosPDVSalvos";

	public static String LISTA_ENDERECOS_REMOVER_SESSAO = "listaEnderecosPDVRemovidos";

	public static String LISTA_ENDERECOS_EXIBICAO = "listaEnderecosPDVExibidos";
	
	public static final String SUCESSO_UPLOAD  = "Upload realizado com sucesso.";
			
	private static final String SUCESSO_EXCLUSAO_ARQUIVO = "Imagem excluída  com sucesso.";
	
	private static final String IMAGEM_PDV = "imagemPdv";
	
	
	@Autowired
	private Result result;
	
	@Autowired
	private PdvService pdvService;
			
	@Autowired
	private HttpSession httpSession;

	@Autowired
	private ParametroSistemaService parametroSistemaService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private ServletContext servletContext;

	public PdvController() {}

	@Path("/")
	public void index(){
	
	}
	
	/**
	 * Carrega dados básicos para abrir tela de PDV
	 */
	public void preCarregamento() {
		
		String dataAtual = new SimpleDateFormat("dd/MM/yyyy").format(new Date());		
		result.include("dataAtual",dataAtual);
		result.include("listaStatus",gerarItemStatus(StatusPDV.values()));
		result.include("listaTamanhoPDV",gerarTamanhosPDV(TamanhoPDV.values()));
		result.include("listaTipoEstabelecimento",getListaDescricao(pdvService.obterTipoEstabelecimentoAssociacaoPDV()));
		result.include("listaTipoLicencaMunicipal",getListaDescricao(pdvService.obterTipoLicencaMunicipal()));
		
		result.include("listaTipoPontoPDV",getListaDescricao(pdvService.obterTiposPontoPDV()));
		result.include("listaCaracteristicaPDV",getListaCaracteristica());
		result.include("listaAreaInfluenciaPDV",getListaDescricao(pdvService.obterAreasInfluenciaPDV()));
		
	}
	
	@Post
	@Path("/carregarMaterialPromocional")
	public void carregarMaterialPromocional(List<Long> codigos){
		
		Long[] cod = (codigos == null)? new Long[]{} : codigos.toArray(new Long[]{});
		
		result.use(Results.json()).from(getListaDescricao(pdvService.obterMateriaisPromocionalPDV(cod)), "result").recursive().serialize();
	}
	
	@Post
	@Path("/carregarMaterialPromocionalNotIn")
	public void carregarMaterialPromocionalNotIn(List<Long> codigos){
		
		Long[] cod = (codigos == null)? new Long[]{} : codigos.toArray(new Long[]{});
		
		result.use(Results.json()).from(getListaDescricao(pdvService.obterMateriaisPromocionalPDVNotIn(cod)), "result").recursive().serialize();
	}

	@Post
	@Path("/carregarGeradorFluxo")
	public void carregarGeradorFluxo(List<Long> codigos){
		
		Long[] cod = (codigos == null)? new Long[]{} : codigos.toArray(new Long[]{});
		
		result.use(Results.json()).from(getListaDescricao(pdvService.obterTiposGeradorFluxo(cod)), "result").recursive().serialize();
	}
	
	@Post
	@Path("/carregarGeradorFluxoNotIn")
	public void carregarGeradorFluxoNotIn(List<Long> codigos){
		
		Long[] cod = (codigos == null)? new Long[]{} : codigos.toArray(new Long[]{});
		
		result.use(Results.json()).from(getListaDescricao(pdvService.obterTiposGeradorFluxoNotIn(cod)), "result").recursive().serialize();
	}
	
	
	@Post
	@Path("/carregarPeriodoFuncionamento")
	public void carregarPeriodoFuncionamento(){
		
		result.use(Results.json()).from(gerarDiasFuncionamento(TipoPeriodoFuncionamentoPDV.values()), "result").recursive().serialize();
	}

	/**
	 * Retorna uma lista de caracteristicas de segmentação do PDV
	 * 
	 * @return List<ItemDTO<TipoCaracteristicaSegmentacaoPDV, String>>
	 */
	private List<ItemDTO<TipoCaracteristicaSegmentacaoPDV, String>> getListaCaracteristica(){
		
		List<ItemDTO<TipoCaracteristicaSegmentacaoPDV, String>> itens = new ArrayList<ItemDTO<TipoCaracteristicaSegmentacaoPDV,String>>();
		
		for( TipoCaracteristicaSegmentacaoPDV item: TipoCaracteristicaSegmentacaoPDV.values()) {
			itens.add(new ItemDTO<TipoCaracteristicaSegmentacaoPDV, String>(item, item.getDescricao()));
		}
		
		return itens;
	}
	
	/**
	 * Retorn uma lista de ItemDTO referente ao tipo especializado de CodigoDescricao
	 * 
	 * @param listaDados - lista de dados derivado de CodigoDescricao
	 * 
	 * @return List<ItemDTO<Long, String>>
	 */
	private List<ItemDTO<Long, String>> getListaDescricao(List< ? extends CodigoDescricao> listaDados){
		
		List<ItemDTO<Long, String>> itens = new ArrayList<ItemDTO<Long,String>>();
		
		for(CodigoDescricao item: listaDados) {
			itens.add(new ItemDTO<Long, String>(item.getCodigo(), item.getDescricao()));
		}
		
		return itens;
	}
	
	/**
	 * Retorna uma lista de Tipo de Periodo Funcionamento do PDV
	 * 
	 * @param tipos - tipos de periodo de funcionamento do pdv
	 * 
	 * @return Object
	 */
	private List<ItemDTO<String, String>> gerarDiasFuncionamento(TipoPeriodoFuncionamentoPDV[] tipos) {
		
		List<ItemDTO<String, String>> itens = new ArrayList<ItemDTO<String,String>>();
				
		for(TipoPeriodoFuncionamentoPDV item: tipos) {
			itens.add(new ItemDTO<String, String>(item.name(), item.getDescricao()));
		}
		
		return itens;
	}
	
	/**
	 * Retorna uma lista de ItemDTO de Status do PDV
	 * 
	 * @param statusPDVs - status de PDV
	 * 
	 * @return List<ItemDTO<String, String>>
	 */
	private List<ItemDTO<String, String>> gerarItemStatus(StatusPDV[] statusPDVs) {
		
		List<ItemDTO<String, String>> itens = new ArrayList<ItemDTO<String,String>>();
		
		for(StatusPDV item: statusPDVs) {
			itens.add(new ItemDTO<String, String>(item.name(), item.getDescricao()));
		}
		
		return itens;
	}
	
	/**
	 * Retorna uma lista de ItemDTO referente ao tamanho do PDV
	 * 
	 * @param statusPDVs - tamanho do PDV
	 * 
	 * @return List<ItemDTO<String, String>>
	 */
	private List<ItemDTO<String, String>> gerarTamanhosPDV(TamanhoPDV[] tamanhoPDVs) {
		
		List<ItemDTO<String, String>> itens = new ArrayList<ItemDTO<String,String>>();
		
		for(TamanhoPDV item: tamanhoPDVs) {
			itens.add(new ItemDTO<String, String>(item.name(), item.getDescricao()));
		}
		
		return itens;
	}
	
	/**
	 * Lista os PDVS associados à cota ou ao Histórico de titularidade da cota
	 * de acordo com o modo em que a tela está operando
	 * @param idCota Identificador da cota
	 * @param idHistorico identificador do histórico
	 * @param modoTela modo em que a tela está operando
	 * @param sortname nome da coluna para ordenação 
	 * @param sortorder sentido da ordenação
	 */
	@Post
	@Path("/consultar")
	public void consultarPDVs(Long idCota, Long idHistorico, ModoTela modoTela, String sortname, String sortorder){
		
		FiltroPdvDTO filtro = new FiltroPdvDTO();
		filtro.setIdCota(idCota);
		
		PaginacaoVO paginacao = new PaginacaoVO(null, null, sortorder);
		
		filtro.setPaginacao(paginacao);
		
		filtro.setColunaOrdenacao(Util.getEnumByStringValue(FiltroPdvDTO.ColunaOrdenacao.values(),sortname));
		
		List<PdvVO>  listaPdvs = null;
		
		if (ModoTela.CADASTRO_COTA == modoTela) {
		   listaPdvs = getListaPdvs(filtro);
		} else {
		    filtro.setIdHistorico(idHistorico);
		    listaPdvs = toPdvVO(pdvService.obterPdvsHistoricoTitularidade(filtro));
		}
		
		TableModel<CellModelKeyValue<PdvVO>> tableModel = new TableModel<CellModelKeyValue<PdvVO>>();
			
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaPdvs));
		
		tableModel.setTotal((listaPdvs.size()>0)? listaPdvs.size():1);
		
		tableModel.setPage(1);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	/**
	 * Retorna uma lista de PdvVO
	 * 
	 * @param filtro - filtro com os criterios de busca
	 * 
	 * @return List<PdvVO>
	 */
	private List<PdvVO> getListaPdvs(FiltroPdvDTO filtro){
		List<PdvDTO> listDtos = pdvService.obterPDVsPorCota(filtro);
		return  toPdvVO(listDtos);
	}

    private List<PdvVO> toPdvVO(List<PdvDTO> dtos) {
        List<PdvVO> vos = new ArrayList<PdvVO>(dtos.size());
      
        for(PdvDTO pdv : dtos){
			PdvVO pdvVO = new PdvVO();
			pdvVO.setIdPdv(pdv.getId());
			pdvVO.setIdCota(pdv.getIdCota());
			pdvVO.setNomePdv(pdv.getNomePDV());
			pdvVO.setTipoPonto( tratarCampo( pdv.getDescricaoTipoPontoPDV()));
			pdvVO.setContato(  tratarCampo( pdv.getContato()));
			pdvVO.setTelefone( tratarCampo(pdv.getTelefone()));
			pdvVO.setEndereco( tratarCampo(pdv.getEndereco()));
			pdvVO.setPrincipal(pdv.isPrincipal());
			pdvVO.setStatus( tratarCampo(pdv.getStatusPDV()));
			pdvVO.setFaturamento((pdv.getPorcentagemFaturamento()==null
									?"":CurrencyUtil.formatarValor(pdv.getPorcentagemFaturamento())));
			vos.add(pdvVO);
		}
        return vos;
    }
	
	private String tratarCampo(Object valor){
		
		return (valor == null)?"":valor.toString();
	}
	
	@Post
	@Path("/excluir")
	public void excluirPDV(Long idPdv, Long idCota){
		
		pdvService.excluirPDV(idPdv);
	
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
	}
		
	@Post
	@Path("/editar")
	public void editarPDV(Long idPdv, Long idCota){
		
		limparDadosSessao();

		PdvDTO dto = pdvService.obterPDV(idCota, idPdv);
		
		carregarTelefonesPDV(idPdv, idCota);
		
		carregarEndercosPDV(idPdv, idCota);
		
		tratarPathImagem(dto);
		
		if(dto!= null){
			
			if(dto.getTamanhoPDV() == null){
				dto.setTipoPontoPDV(new TipoPontoPDVDTO());
			}
			
			if(dto.getTipoEstabelecimentoAssociacaoPDV() == null){
				dto.setTipoEstabelecimentoAssociacaoPDV(new TipoEstabelecimentoAssociacaoPDV());
			}
			
			if(dto.getTipoLicencaMunicipal()== null){
				dto.setTipoLicencaMunicipal(new TipoLicencaMunicipalDTO());
			}
			
			if(dto.getTipoPontoPDV() == null){
				dto.setTipoPontoPDV(new TipoPontoPDVDTO());
			}
		}
			
		result.use(Results.json()).from(dto).recursive().serialize();
	}
	
	private void tratarPathImagem(PdvDTO dto) {
		
		ParametroSistema pathPDV = 
				this.parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_IMAGENS_PDV);
		
		
		File file = new File((servletContext.getRealPath("") + pathPDV.getValor()).replace("\\", "/"),"pdv_" + dto.getId() + ".jpeg");
		   		
		if(file.exists()) 
			dto.setPathImagem(pathPDV.getValor() + "pdv_" + dto.getId() + ".jpeg" );
	}
	private void limparDadosSessao() {
		
		this.httpSession.removeAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
		this.httpSession.removeAttribute(LISTA_ENDERECOS_REMOVER_SESSAO);
		this.httpSession.removeAttribute(LISTA_ENDERECOS_EXIBICAO);
		
		this.httpSession.removeAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		this.httpSession.removeAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
		this.httpSession.removeAttribute(LISTA_TELEFONES_EXIBICAO);
		
		httpSession.setAttribute(IMAGEM_PDV, null);
	}

	@Post
	@Path("/cancelarCadastro")
	public void cancelarCadastro(){
		
		limparDadosSessao();
		
		result.use(Results.json()).from("", "result").serialize();
	}

	@Post
	@Path("/salvar")
	public void salvarPDV(PdvDTO pdvDTO) throws Exception{		
		
	
		if(pdvDTO.isDentroOutroEstabelecimento() && pdvDTO.getTipoEstabelecimentoAssociacaoPDV().getCodigo() == -1){
			throw new ValidacaoException(TipoMensagem.WARNING,"Tipo de Estabelecimento deve ser informado!");
		}
		
		if(pdvDTO.isExpositor() && pdvDTO.getTipoExpositor().isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING,"Tipo Expositor deve ser informado!");
		}
		
		pdvDTO.setImagem((FileInputStream) httpSession.getAttribute(IMAGEM_PDV));
		
		pdvDTO.setPathAplicacao(servletContext.getRealPath(""));
		
		preencherTelefones(pdvDTO);
		
		preencherEnderecos(pdvDTO);
		
		try{
		
			pdvService.salvar(pdvDTO);
		
			result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."),
					"result").recursive().serialize();
			
		}catch (EnderecoUniqueConstraintViolationException e) {
			
			tratarErroExclusaoEndereco();
			
			result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.ERROR,e.getMessage()),
					"result").recursive().serialize();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void tratarErroExclusaoEndereco(){
		
		List<EnderecoAssociacaoDTO> listaEnderecosExcluidos = 
				(List<EnderecoAssociacaoDTO>) httpSession.getAttribute(LISTA_ENDERECOS_REMOVER_SESSAO);
		
		List<EnderecoAssociacaoDTO> listaEnderecosExibicao= 
				(List<EnderecoAssociacaoDTO>) httpSession.getAttribute(LISTA_ENDERECOS_EXIBICAO);
		
		if(listaEnderecosExibicao == null || listaEnderecosExibicao.isEmpty()){
			listaEnderecosExibicao = new ArrayList<EnderecoAssociacaoDTO>();
		}
			
		if(listaEnderecosExcluidos!= null && !listaEnderecosExcluidos.isEmpty()){
			listaEnderecosExibicao.addAll(listaEnderecosExcluidos);
		}
		
		httpSession.setAttribute(LISTA_ENDERECOS_REMOVER_SESSAO,null);
		
		httpSession.setAttribute(LISTA_ENDERECOS_EXIBICAO,listaEnderecosExibicao);
			
	}
	
	private void preencherTelefones(PdvDTO pdvDTO) {

		pdvDTO.setTelefonesAdicionar(new ArrayList<TelefoneAssociacaoDTO>());
		
		@SuppressWarnings("unchecked")
		Map<Integer, TelefoneAssociacaoDTO> listaTelefone = (Map<Integer, TelefoneAssociacaoDTO>) 
				httpSession.getAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		
		
		if (listaTelefone != null){
			for (TelefoneAssociacaoDTO telefoneAssociacaoDTO : listaTelefone.values()){
				
				if(telefoneAssociacaoDTO.getTipoTelefone()!= null){
					pdvDTO.getTelefonesAdicionar() .add(telefoneAssociacaoDTO);
				}
			}
		}
		
		@SuppressWarnings("unchecked")
		Set<Long> listaTelefoneRemover = (Set<Long>) 
				httpSession.getAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
		
		pdvDTO.setTelefonesRemover(listaTelefoneRemover);
		
	}
	
	@SuppressWarnings("unchecked")
	private void preencherEnderecos(PdvDTO pdvDTO) {
	
		List<EnderecoAssociacaoDTO> listaEnderecosSalvos = 
				(List<EnderecoAssociacaoDTO>) httpSession.getAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
		
		pdvDTO.setEnderecosAdicionar(listaEnderecosSalvos);
		
		List<EnderecoAssociacaoDTO> listaEnderecosExcluidos = 
				(List<EnderecoAssociacaoDTO>) httpSession.getAttribute(LISTA_ENDERECOS_REMOVER_SESSAO);
		
		if(listaEnderecosExcluidos!= null && !listaEnderecosExcluidos.isEmpty()){
			
			pdvDTO.setEnderecosRemover(new HashSet<Long>());
			
			for(EnderecoAssociacaoDTO endereco :  listaEnderecosExcluidos){
				
				pdvDTO.getEnderecosRemover().add(endereco.getId());
			}
		}
	}

	@Post
	@Path("/adicionarPeriodo")
	public void adicionarPeriodo(List<PeriodoFuncionamentoDTO> periodos, PeriodoFuncionamentoDTO novoPeriodo){		
		
		if(novoPeriodo.getTipoPeriodoFuncionamentoPDV() == null) 
			throw new ValidacaoException(TipoMensagem.WARNING, "Tipo de período deve ser selecionado.");
		
		TipoMensagem status = TipoMensagem.SUCCESS;
		
		List<String> mensagens = new ArrayList<String>();
		
		List<TipoPeriodoFuncionamentoPDV> tiposPeriodosPossiveis = null;
	
		if(periodos == null) {
			periodos = new ArrayList<PeriodoFuncionamentoDTO>();
		}
		
		if( !TipoPeriodoFuncionamentoPDV.VINTE_QUATRO_HORAS.getDescricao().equals(novoPeriodo.getNomeTipoPeriodo()) ) {
 		
			if(novoPeriodo.getInicio() == null || novoPeriodo.getInicio().trim().isEmpty()) {
				throw new ValidacaoException(
						TipoMensagem.WARNING,"Horário de início não foi preenchido corretamente.");
			}
	
			if(novoPeriodo.getFim() == null || novoPeriodo.getFim().trim().isEmpty()) {
				throw new ValidacaoException(
						TipoMensagem.WARNING,"Horário de términio não foi preenchido corretamente.");
			}
			
			validarHorario(novoPeriodo);
		}
		
		pdvService.validarPeriodos(periodos);
		periodos.add(novoPeriodo);
		tiposPeriodosPossiveis = pdvService.getPeriodosPossiveis(periodos);
					

		Object[] retorno = new Object[3];
		retorno[0] = getCombosPeriodos(tiposPeriodosPossiveis);
		retorno[1] = mensagens;
		retorno[2] = status.name();		
		
		result.use(Results.json()).withoutRoot().from(retorno).recursive().serialize();
	}
		
	@Post
	@Path("/atualizarComboDiasFuncionamento")
	public void atualizarComboDiasFuncionamento(List<PeriodoFuncionamentoDTO> periodos){		
		
		
		TipoMensagem status = TipoMensagem.SUCCESS;
		
		List<String> mensagens = new ArrayList<String>();
		
		List<TipoPeriodoFuncionamentoPDV> tiposPeriodosPossiveis = null;
	
		if(periodos == null) {
			periodos = new ArrayList<PeriodoFuncionamentoDTO>();
		}
		
		
		pdvService.validarPeriodos(periodos);
		
		tiposPeriodosPossiveis = pdvService.getPeriodosPossiveis(periodos);					

		Object[] retorno = new Object[3];
		retorno[0] = getCombosPeriodos(tiposPeriodosPossiveis);
		retorno[1] = mensagens;
		retorno[2] = status.name();		
		
		result.use(Results.json()).withoutRoot().from(retorno).recursive().serialize();
	}
	
	private void validarHorario(PeriodoFuncionamentoDTO novoPeriodo) {
		
		try {
			
			Date inicio = DateUtil.parseData(novoPeriodo.getInicio(), "HH:mm");
			Date fim = DateUtil.parseData(novoPeriodo.getFim(), "HH:mm");
						
			if(DateUtil.isDataInicialMaiorDataFinal(inicio, fim)) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Hora de início deve ser menor que de fim.");
			}
			
		} catch(ValidacaoException ve) {
			throw ve;
		}catch (Exception e) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Horário inválido.");
		}
		
		
	}

	@Post
	@Path("/obterPeriodosPossiveis")
	public void obterPeriodosPossiveis(List<PeriodoFuncionamentoDTO> periodos){		
				
		TipoMensagem status = TipoMensagem.SUCCESS;
		List<String> mensagens = new ArrayList<String>();
		
		List<TipoPeriodoFuncionamentoPDV> tiposPeriodosPossiveis = null;
	
		if(periodos==null) {
			periodos = new ArrayList<PeriodoFuncionamentoDTO>();
		}
		
		try {
		
			tiposPeriodosPossiveis = pdvService.getPeriodosPossiveis(periodos);
						
		}catch(Exception e) {
			mensagens.clear();
			mensagens.add(e.getMessage());
			status=TipoMensagem.ERROR;
		}
		
		Object[] retorno = new Object[3];
		retorno[0] = getCombosPeriodos(tiposPeriodosPossiveis);
		retorno[1] = mensagens;
		retorno[2] = status.name();		
		
		result.use(Results.json()).withoutRoot().from(retorno).recursive().serialize();
	}
	
	/**
	 * Retorna uma lista de ItemDTO de tipo periodo funcionamento PDV
	 * 
	 * @param tiposPeriodosPossiveis
	 * 
	 * @return List<ItemDTO<String, String>>
	 */
	private List<ItemDTO<String, String>> getCombosPeriodos(
			List<TipoPeriodoFuncionamentoPDV> tiposPeriodosPossiveis) {
		
		List<ItemDTO<String, String>> itens = new ArrayList<ItemDTO<String,String>>();
		
		for(TipoPeriodoFuncionamentoPDV tipo: tiposPeriodosPossiveis) {
			itens.add(new ItemDTO<String, String>(tipo.name(),tipo.getDescricao()));
		}
		
		return itens;
	}
	
	@Post
	public void uploadImagem(UploadedFile uploadedFile) {
	
		TipoMensagem status = TipoMensagem.SUCCESS;
		List<String> mensagens = new ArrayList<String>();
		
		String nomeArquivo = null; 
		
		String path = null;
		
		try {
			
			ParametroSistema pathPDV = 
					this.parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_IMAGENS_PDV);
			
			path = pathPDV.getValor().replace("\\", "/");
			
			if(((FileInputStream)uploadedFile.getFile()).getChannel().size() > (1024 * 1024 * 5)) {
				throw new Exception("O arquivo deve ser menor que 5MBs.");
			}
			 
			File fileArquivoBanco = gravarArquivo(uploadedFile);
			
			nomeArquivo = fileArquivoBanco.getName();
			
			mensagens.add(SUCESSO_UPLOAD);
						
		}catch(Exception e) {
			mensagens.clear();
			mensagens.add(e.getMessage());
			status=TipoMensagem.ERROR;
		}
		
		Object[] retorno = new Object[3];
		retorno[0] = mensagens;
		retorno[1] = status.name();		
		retorno[2] = path + nomeArquivo;
				
		result.use(PlainJSONSerialization.class)
			.from(retorno, "result").recursive().serialize();
		
	}
	
	private File gravarArquivo(UploadedFile uploadedFile) {
		
		ParametroSistema pathPDV = 
				this.parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_IMAGENS_PDV);
		
						
		File fileDir = new File((servletContext.getRealPath("") + pathPDV.getValor()).replace("\\", "/"));
		
		fileDir.mkdirs();

		String nomeArquivo = "pdv_temp.jpeg";
		
		File fileArquivo = new File(fileDir, nomeArquivo);
			
		if(fileArquivo.exists())
			fileArquivo.delete();
		
		FileOutputStream fos = null;
		
		try {
						
			fos = new FileOutputStream(fileArquivo);
						
			((FileInputStream)uploadedFile.getFile()).getChannel().size();
			IOUtils.copyLarge(((FileInputStream)uploadedFile.getFile()), fos);
			
			httpSession.setAttribute(IMAGEM_PDV, new FileInputStream(new File(fileDir, nomeArquivo)));
			
		} catch (Exception e) {
			
			throw new ValidacaoException(TipoMensagem.ERROR,
				"Falha ao gravar o arquivo em disco!");
		
		} finally {
			try { 
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				throw new ValidacaoException(TipoMensagem.ERROR,
					"Falha ao gravar o arquivo em disco!");
			}
		}
		
		return fileArquivo;
	}
	
	@Post
	@Path("/excluirImagem")
	public void excluirImagem(Long idPdv) {
	
		TipoMensagem status = TipoMensagem.SUCCESS;
		List<String> mensagens = new ArrayList<String>();
		
		try {
			excluirArquivo(idPdv);
						
			mensagens.add(SUCESSO_EXCLUSAO_ARQUIVO);
						
		}catch(Exception e) {
			mensagens.clear();
			mensagens.add(e.getMessage());
			status=TipoMensagem.ERROR;
		}
		
		Object[] retorno = new Object[2];
		retorno[0] = mensagens;
		retorno[1] = status.name();
				
		result.use(Results.json()).withoutRoot().from(retorno).recursive().serialize();
		
	}
	
	private void excluirArquivo(Long idPdv) {
		
		httpSession.setAttribute(IMAGEM_PDV, null);
		
		if(idPdv == null)
			return;
		
		ParametroSistema pathPDV = 
				this.parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_IMAGENS_PDV);
						
		File file = new File((servletContext.getRealPath("") + pathPDV.getValor()).replace("\\", "/"),"pdv_" + idPdv + ".jpeg");
		   		
		if(file.exists())
			file.delete();

	}

	private void carregarTelefonesPDV(Long idPdv, Long idCota) {
		
		List<TelefoneAssociacaoDTO> lista = this.pdvService.buscarTelefonesPdv(idPdv, idCota);
		
		httpSession.setAttribute(LISTA_TELEFONES_EXIBICAO, lista);
		httpSession.setAttribute(LISTA_TELEFONES_REMOVER_SESSAO, null);
		httpSession.setAttribute(LISTA_TELEFONES_SALVAR_SESSAO, null);
	}
	
	/**
	 * Carrega endereços ligados ao PDV e Cota
	 * Dados serão utilizados pelo componente de Endereço
	 * 
	 * @param idPdv - Id do PDV
	 * @param idCota - Id da Cota
	 */
	private void carregarEndercosPDV(Long idPdv, Long idCota) {
		
		List<EnderecoAssociacaoDTO> listaEnderecos = this.pdvService.buscarEnderecosPDV(idPdv,idCota);
		
		httpSession.setAttribute(LISTA_ENDERECOS_EXIBICAO, listaEnderecos);
		httpSession.setAttribute(LISTA_ENDERECOS_REMOVER_SESSAO, null);
		httpSession.setAttribute(LISTA_ENDERECOS_SALVAR_SESSAO, null);
	}
		
	/**
	 * Carrega dados  de Telefone e Endereço da cota para novo PDV
	 * 
	 * @param idCota - Id da Cota
	 */
	@Post
	@Path("/novo")
	public void carregarDadosNovoPdv(Long idCota) {
		
		limparDadosSessao();
		
		carregarTelefonesPDV(null, idCota);
		
		carregarEndercosPDV(null,idCota);

		result.use(Results.json()).withoutRoot().from("").recursive().serialize();
	}
	
	/**
	 * Recarrega os endereços e telefones adicionados pelo cadastro de pdv para mesma pessoa.
	 * 
	 * @param idCota
	 */
	@Post
	public void recarregarEnderecoTelefoneCota(Long idCota){
		
		httpSession.removeAttribute(CotaController.LISTA_ENDERECOS_EXIBICAO);
		httpSession.removeAttribute(CotaController.LISTA_ENDERECOS_REMOVER_SESSAO);
		httpSession.removeAttribute(CotaController.LISTA_ENDERECOS_SALVAR_SESSAO);
		httpSession.removeAttribute(CotaController.LISTA_TELEFONES_EXIBICAO);
		httpSession.removeAttribute(CotaController.LISTA_TELEFONES_REMOVER_SESSAO);
		httpSession.removeAttribute(CotaController.LISTA_TELEFONES_SALVAR_SESSAO);
		
		List<EnderecoAssociacaoDTO> listaEnderecoAssociacao = cotaService.obterEnderecosPorIdCota(idCota);
		httpSession.setAttribute(CotaController.LISTA_ENDERECOS_EXIBICAO, listaEnderecoAssociacao);
		
		List<TelefoneAssociacaoDTO> listaTelefoneAssociacao = cotaService.buscarTelefonesCota(idCota, null);
		httpSession.setAttribute(CotaController.LISTA_TELEFONES_EXIBICAO, listaTelefoneAssociacao);
		
		result.use(Results.json()).withoutRoot().from("").serialize();
	}
	
	//TODO getRealUsuario
	public Usuario getUsuario() {
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setLogin("fakeUsuario");
		usuario.setNome("Fake Usuario");
		return usuario;
	}
	
	@Post
	public void verificarPontoPrincipal(Long idCota, Long idPdv){
		
		boolean existe = this.pdvService.existePDVPrincipal(idCota, idPdv);
		
		this.result.use(Results.json()).withoutRoot().from(existe).serialize();
	}
}
