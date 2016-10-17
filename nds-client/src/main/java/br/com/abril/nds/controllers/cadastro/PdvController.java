package br.com.abril.nds.controllers.cadastro;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.PdvVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.GeradorFluxoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.MaterialPromocionalDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.PeriodoFuncionamentoDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroPdvDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.EnderecoUniqueConstraintViolationException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.CodigoDescricao;
import br.com.abril.nds.model.cadastro.pdv.StatusPDV;
import br.com.abril.nds.model.cadastro.pdv.TamanhoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPeriodoFuncionamentoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.PdvService;
import br.com.abril.nds.service.integracao.ParametroSistemaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.InputStreamDownload;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/pdv")
public class PdvController extends BaseController {
	
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
//		result.include("listaCaracteristicaPDV",getLicstaCaracteristica());
		result.include("listaAreaInfluenciaPDV",getListaDescricao(pdvService.obterTipoAreaInfluencia()));
		
	}
	
	@Post
	@Path("/carregarMaterialPromocional")
	public void carregarMaterialPromocional(List<Long> codigos, ModoTela modoTela, Long idPdv){
	    List<ItemDTO<Long, String>> listaDescricao;
		Long[] cod = (codigos == null)? new Long[]{} : codigos.toArray(new Long[]{});
		if (ModoTela.CADASTRO_COTA == modoTela) {
		    listaDescricao = getListaDescricao(pdvService.obterMateriaisPromocionalPDV(cod));
		} else {
		    Set<Long> codigosMateriais = new HashSet<Long>(Arrays.asList(cod));
            List<MaterialPromocionalDTO> dtos = pdvService.obterMateriaisPromocionaisHistoricoTitularidadePDV(idPdv, codigosMateriais);
            listaDescricao = new ArrayList<ItemDTO<Long,String>>(dtos.size());
            for (MaterialPromocionalDTO materialDTO : dtos) {
                listaDescricao.add(new ItemDTO<Long, String>(materialDTO.getCodigo(), materialDTO.getDescricao()));
            }
		}
        result.use(Results.json()).from(listaDescricao, "result").recursive().serialize();
	}
	
	@Post
	@Path("/carregarMaterialPromocionalNotIn")
	public void carregarMaterialPromocionalNotIn(List<Long> codigos, ModoTela modoTela){
	    List<ItemDTO<Long, String>> listaDescricao = new ArrayList<ItemDTO<Long,String>>();
	    if (ModoTela.CADASTRO_COTA == modoTela) {
	        Long[] cod = (codigos == null)? new Long[]{} : codigos.toArray(new Long[]{});
	        listaDescricao = getListaDescricao(pdvService.obterMateriaisPromocionalPDVNotIn(cod));
	    }
        result.use(Results.json()).from(listaDescricao, "result").recursive().serialize();
	}

	@Post
	@Path("/carregarGeradorFluxo")
	public void carregarGeradorFluxo(List<Long> codigos, ModoTela modoTela, Long idPdv){
	  	    List<ItemDTO<Long, String>> listaDescricao;
		    Long[] cod = (codigos == null)? new Long[]{} : codigos.toArray(new Long[]{});
		    if (ModoTela.CADASTRO_COTA == modoTela) {
		        listaDescricao = getListaDescricao(pdvService.obterTiposGeradorFluxo(cod));
		    } else {
		        Set<Long> codigosGeradores = new HashSet<Long>(Arrays.asList(cod));
		        List<GeradorFluxoDTO> dtos = pdvService.obterGeradoresFluxoHistoricoTitularidadePDV(idPdv, codigosGeradores);
		        listaDescricao = new ArrayList<ItemDTO<Long,String>>(dtos.size());
		        for (GeradorFluxoDTO geradorFluxoDTO : dtos) {
	                listaDescricao.add(new ItemDTO<Long, String>(geradorFluxoDTO.getCodigo(), geradorFluxoDTO.getDescricao()));
	            }
		    }
	        result.use(Results.json()).from(listaDescricao, "result").recursive().serialize();
	  
		   
	}
	
	@Post
	@Path("/carregarGeradorFluxoNotIn")
	public void carregarGeradorFluxoNotIn(List<Long> codigos, ModoTela modoTela){
	    List<ItemDTO<Long, String>> listaDescricao = new ArrayList<ItemDTO<Long,String>>();
	    if (ModoTela.CADASTRO_COTA ==  modoTela) {
	        Long[] cod = (codigos == null)? new Long[]{} : codigos.toArray(new Long[]{});
	        listaDescricao = getListaDescricao(pdvService.obterTiposGeradorFluxoNotIn(cod));
	    }		
        result.use(Results.json()).from(listaDescricao, "result").recursive().serialize();
        // TODO ALMIR VERIFICAR SE É PRINCIPAL PARA BLOQUER O GERADOR DE FLUXO
        
	}
	
	
	@Post
	@Path("/carregarPeriodoFuncionamento")
	public void carregarPeriodoFuncionamento(){
		
		result.use(Results.json()).from(gerarDiasFuncionamento(TipoPeriodoFuncionamentoPDV.values()), "result").recursive().serialize();
	}
	
	@Post
	@Path("/carregarTiposEstabelecimento")
	public void carregarTiposEstabelecimento() {
	    result.use(Results.json()).from(getListaDescricao(pdvService.obterTipoEstabelecimentoAssociacaoPDV()), "result").recursive().serialize();
	}
	
	@Post
    @Path("/carregarTiposLicencaMunicipal")
    public void carregarTiposLicencaMunicipal() {
        result.use(Results.json()).from(getListaDescricao(pdvService.obterTipoLicencaMunicipal()), "result").recursive().serialize();
    }
	
	@Post
    @Path("/carregarTiposPontoPdv")
    public void carregarTiposPontoPdv() {
        result.use(Results.json()).from(getListaDescricao(pdvService.obterTiposPontoPDV()), "result").recursive().serialize();
    }
	
	@Post
	@Path("/carregarCaracteristicasPdv")
	public void carregarCaracteristicasPdv() {
	      result.use(Results.json()).from(getListaCaracteristica(), "result").recursive().serialize();
	}
	
	@Post
    @Path("/carregarAreasInfluenciaPdv")
    public void carregarAreasInfluenciaPdv() {
        result.use(Results.json()).from(getListaDescricao(pdvService.obterTipoAreaInfluencia()), "result").recursive().serialize();
    }

	                /**
     * Retorna uma lista de caracteristicas de segmentação do PDV
     * 
     * @return List<ItemDTO<TipoCaracteristicaSegmentacaoPDV, String>>
     */
	private List<ItemDTO<String, String>> getListaCaracteristica(){
		
		//AJUSTE EMS-0159
        // • Características: características possíveis do PDV (banca, livraria,
        // revistaria, etc..)
		List<ItemDTO<String, String>> itens = new ArrayList<ItemDTO<String,String>>();
		
		for( TipoPontoPDV item: pdvService.obterTiposPontoPDV()) {
			itens.add(new ItemDTO<String, String>(item.getDescricao(), item.getDescricao()));
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
     * 
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
	public void editarPDV(Long idPdv, Long idCota, ModoTela modoTela){
		limparDadosSessao();

	    PdvDTO dto = null;
		
	    if (ModoTela.CADASTRO_COTA == modoTela) {
		    dto = pdvService.obterPDV(idCota, idPdv);
		    
		    carregarTelefonesPDV(idPdv, idCota);
		    
		    carregarEnderecosPDV(idPdv, idCota);
		    
		    tratarPathImagem(dto);
		    
		} else {
		    dto = pdvService.obterPdvHistoricoTitularidade(idPdv);
		  
		    carregarEnderecosHistoricoTitularidadePDV(idPdv);
		    
		    carregarTelefonesHistoricoTitularidade(idPdv);

		}
		
			
		result.use(Results.json()).from(dto).recursive().serialize();
	}
	
	@Path("/imagemPdvHistoricoTitularidade")
	public Download imagemPdvHistoricoTitularidade(Long idPdv) {
	    byte[] imagem = pdvService.obterImagemHistoricoTitularidadePDV(idPdv);
        if (imagem == null) {
            imagem = new byte[0];
        }
	    return new InputStreamDownload(new ByteArrayInputStream(imagem), null, null);
	}
	
	private void tratarPathImagem(PdvDTO dto) {
		
		ParametroSistema pathPDV = 
				this.parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_IMAGENS_PDV);
		
		
		File file = new File((servletContext.getRealPath("") + pathPDV.getValor()).replace("\\", "/"),"pdv_" + dto.getId() + ".jpeg");
		   		
		if(file.exists()){ 
		
			dto.setPathImagem(pathPDV.getValor() + "pdv_" + dto.getId() + ".jpeg" );
		}	
	}
	
	private void limparDadosSessao() {
		
		this.httpSession.removeAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
		this.httpSession.removeAttribute(LISTA_ENDERECOS_REMOVER_SESSAO);
		this.httpSession.removeAttribute(LISTA_ENDERECOS_EXIBICAO);
		
		this.httpSession.removeAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		this.httpSession.removeAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
		this.httpSession.removeAttribute(LISTA_TELEFONES_EXIBICAO);
		
		this.httpSession.removeAttribute(EnderecoController.ENDERECO_PENDENTE);
		
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
	@Rules(Permissao.ROLE_CADASTRO_COTA_ALTERACAO)
	public void salvarPDV(PdvDTO pdvDTO) throws Exception{		
		
	
		if(pdvDTO.isDentroOutroEstabelecimento() && pdvDTO.getTipoEstabelecimentoAssociacaoPDV().getCodigo() == -1){
			throw new ValidacaoException(TipoMensagem.WARNING,"Tipo de Estabelecimento deve ser informado!");
		}
		
		if(pdvDTO.isExpositor() && (pdvDTO.getTipoExpositor() == null  || pdvDTO.getTipoExpositor().isEmpty()))
		{
            throw new ValidacaoException(TipoMensagem.WARNING,"Tipo Expositor deve ser informado!");
		}

		
		 @SuppressWarnings("unchecked")
         List<EnderecoAssociacaoDTO> listaEnderecosExibicao = 
                         (List<EnderecoAssociacaoDTO>) httpSession.getAttribute(LISTA_ENDERECOS_EXIBICAO);
         
         if(listaEnderecosExibicao == null || listaEnderecosExibicao.isEmpty()){
                 
            throw new ValidacaoException(TipoMensagem.WARNING, "Cadastre ao menos um endereço para o PDV.");
         }    
         
         boolean enderecoPdvCadastrado = false;
         
         for (EnderecoAssociacaoDTO item : listaEnderecosExibicao){
                 
                 if (item.getTipoEndereco()!=null){
                         
                         enderecoPdvCadastrado = true;
                         
                         break;
                 }
         }
         
         if (!enderecoPdvCadastrado){
                 
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Altere ao menos um endereço e defina seu tipo para o PDV.");
         }
         
		
		pdvDTO.setImagem((FileInputStream) httpSession.getAttribute(IMAGEM_PDV));
		
		pdvDTO.setPathAplicacao(servletContext.getRealPath(""));
		
		preencherTelefones(pdvDTO);
		
		preencherEnderecos(pdvDTO);
		
		try{
		
			pdvService.salvar(pdvDTO);
			
			this.httpSession.setAttribute(EnderecoController.ENDERECO_PENDENTE, Boolean.FALSE);
		
            result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."),
					"result").serialize();
			
		}catch (EnderecoUniqueConstraintViolationException e) {
			
			tratarErroExclusaoEndereco();
			
			result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.ERROR,e.getMessage()),
					"result").serialize();
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
		
        if (novoPeriodo == null || novoPeriodo.getTipoPeriodoFuncionamentoPDV() == null)
            throw new ValidacaoException(TipoMensagem.WARNING, "Tipo de período deve ser selecionado.");
		
		TipoMensagem status = TipoMensagem.SUCCESS;
		
		List<String> mensagens = new ArrayList<String>();
		
		List<TipoPeriodoFuncionamentoPDV> tiposPeriodosPossiveis = null;
	
		if(periodos == null) {
			periodos = new ArrayList<PeriodoFuncionamentoDTO>();
		}
		
		if( !TipoPeriodoFuncionamentoPDV.VINTE_QUATRO_HORAS.getDescricao().equals(novoPeriodo.getNomeTipoPeriodo()) ) {
 		
			if(novoPeriodo.getInicio() == null || novoPeriodo.getInicio().trim().isEmpty()) {
                throw new ValidacaoException(TipoMensagem.WARNING, "Horário de início não foi preenchido corretamente.");
			}
	
			if(novoPeriodo.getFim() == null || novoPeriodo.getFim().trim().isEmpty()) {
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Horário de términio não foi preenchido corretamente.");
			}
			
			validarHorario(novoPeriodo);
		} else {
            novoPeriodo.setInicio(null);
            novoPeriodo.setFim(null);
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
		
		armazenarTelefonesSessao(lista);
	}

    private void armazenarTelefonesSessao(List<TelefoneAssociacaoDTO> lista) {
        httpSession.setAttribute(LISTA_TELEFONES_EXIBICAO, lista);
		httpSession.setAttribute(LISTA_TELEFONES_REMOVER_SESSAO, null);
		httpSession.setAttribute(LISTA_TELEFONES_SALVAR_SESSAO, null);
    }
    
    private boolean isNotEnderecoPendente(){
		
        Boolean enderecoPendente = (Boolean) this.httpSession.getAttribute(EnderecoController.ENDERECO_PENDENTE);
		
		return (enderecoPendente==null || !enderecoPendente);
	}
	
	                /**
     * Carrega endereços ligados ao PDV e Cota Dados serão utilizados pelo
     * componente de Endereço
     * 
     * @param idPdv - Id do PDV
     * @param idCota - Id da Cota
     */
	private void carregarEnderecosPDV(Long idPdv, Long idCota) {
		
		if (this.isNotEnderecoPendente()){
		
			List<EnderecoAssociacaoDTO> listaEnderecos = this.pdvService.buscarEnderecosPDV(idPdv,idCota);
			
			armazenarEnderecosSessao(listaEnderecos);
		}
	}

    private void armazenarEnderecosSessao(
            List<EnderecoAssociacaoDTO> listaEnderecos) {
        httpSession.setAttribute(LISTA_ENDERECOS_EXIBICAO, listaEnderecos);
		httpSession.setAttribute(LISTA_ENDERECOS_REMOVER_SESSAO, null);
		httpSession.setAttribute(LISTA_ENDERECOS_SALVAR_SESSAO, null);
    }
	
    /**
     * Carrega os endereços do histórico de titularidade do PDV
     * 
     * @param idPdv identificador do PDV
     */
	private void carregarEnderecosHistoricoTitularidadePDV(Long idPdv) {
	    List<EnderecoAssociacaoDTO> enderecos = pdvService.obterEnderecosHistoricoTitularidadePDV(idPdv);
	    armazenarEnderecosSessao(enderecos);
	}
	
	                 /**
     * Carrega os telefones do histórico de titularidade do PDV
     * 
     * @param idPdv identificador do PDV
     */
	private void carregarTelefonesHistoricoTitularidade(Long idPdv) {
	    List<TelefoneAssociacaoDTO> telefones = pdvService.obterTelefonesHistoricoTitularidadePDV(idPdv);
	    armazenarTelefonesSessao(telefones);
	}
		
	                /**
     * Carrega dados de Telefone e Endereço da cota para novo PDV
     * 
     * @param idCota - Id da Cota
     */
	@Post
	@Path("/novo")
	public void carregarDadosNovoPdv(Long idCota) {
		
		limparDadosSessao();
		
		carregarTelefonesPDV(null, idCota);
		
		carregarEnderecosPDV(null,idCota);

		result.use(Results.json()).withoutRoot().from("").recursive().serialize();
	}
	
	                /**
     * Recarrega os endereços e telefones adicionados pelo cadastro de pdv para
     * mesma pessoa.
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
		
		httpSession.removeAttribute(EnderecoController.ENDERECO_PENDENTE);
		
		List<EnderecoAssociacaoDTO> listaEnderecoAssociacao = cotaService.obterEnderecosPorIdCota(idCota);
		httpSession.setAttribute(CotaController.LISTA_ENDERECOS_EXIBICAO, listaEnderecoAssociacao);
		
		List<TelefoneAssociacaoDTO> listaTelefoneAssociacao = cotaService.buscarTelefonesCota(idCota, null);
		httpSession.setAttribute(CotaController.LISTA_TELEFONES_EXIBICAO, listaTelefoneAssociacao);
		
		result.use(Results.json()).withoutRoot().from("").serialize();
	}
	
	@Post
	public void verificarPontoPrincipal(Long idCota, Long idPdv){
		
		boolean existe = this.pdvService.existePDVPrincipal(idCota, idPdv);
		
		this.result.use(Results.json()).withoutRoot().from(existe).serialize();
	}
}