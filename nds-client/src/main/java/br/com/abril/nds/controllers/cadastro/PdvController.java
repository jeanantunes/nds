package br.com.abril.nds.controllers.cadastro;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.PdvVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.PeriodoFuncionamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroPdvDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.CodigoDescricao;
import br.com.abril.nds.model.cadastro.TipoLicencaMunicipal;
import br.com.abril.nds.model.cadastro.pdv.StatusPDV;
import br.com.abril.nds.model.cadastro.pdv.TamanhoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoCaracteristicaSegmentacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoEstabelecimentoAssociacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPeriodoFuncionamentoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.PdvService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/pdv")
public class PdvController {
	
	public static final String SUCESSO_UPLOAD  = "Upload realizado com sucesso.";
	
	@Autowired
	private Result result;
	
	@Autowired
	private PdvService pdvService;
	
	@Autowired
	private ServletContext servletContext;
	
	private static final String FORMATO_DATA_DIRETORIO = "yyyy-MM-dd";
	
	private static final String DIRETORIO_TEMPORARIO_ARQUIVO_BANCO = "images/temp/pdv/";

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
		result.include("listaClusterPDV",getListaDescricao(pdvService.obterClustersPDV()));
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
	@Path("/carregarEspecialidades")
	public void carregarEspecialidades(List<Long> codigos){
		
		Long[] cod = (codigos == null)? new Long[]{} : codigos.toArray(new Long[]{});
		
		result.use(Results.json()).from(getListaDescricao(pdvService.obterEspecialidadesPDV(cod)), "result").recursive().serialize();
	}
	
	@Post
	@Path("/carregarEspecialidadesNotIn")
	public void carregarEspecialidadesNotIn(List<Long> codigos){
		
		Long[] cod = (codigos == null)? new Long[]{} : codigos.toArray(new Long[]{});
		
		result.use(Results.json()).from(getListaDescricao(pdvService.obterEspecialidadesPDVNotIn(cod)), "result").recursive().serialize();
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
	private Object gerarDiasFuncionamento(TipoPeriodoFuncionamentoPDV[] tipos) {
		
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
	
	@Post
	@Path("/consultar")
	public void consultarPDVs(Long idCota,String sortname, String sortorder){
		
		FiltroPdvDTO filtro = new FiltroPdvDTO();
		filtro.setIdCota(idCota);
		
		PaginacaoVO paginacao = new PaginacaoVO(null, null, sortorder);
		
		filtro.setPaginacao(paginacao);
		
		filtro.setColunaOrdenacao(Util.getEnumByStringValue(FiltroPdvDTO.ColunaOrdenacao.values(),sortname));
		
		List<PdvVO>  listaPdvs = getListaPdvs(filtro);
		
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
		
		List<PdvVO> listaRetorno = new ArrayList<PdvVO>();
		
		PdvVO pdvVO = null;
		
		for(PdvDTO pdv : listDtos){
			
			pdvVO = new PdvVO();
			
			pdvVO.setIdPdv(pdv.getId());
			pdvVO.setIdCota(pdv.getIdCota());
			pdvVO.setNomePdv(pdv.getNomePDV());
			pdvVO.setTipoPonto( tratarCampo( pdv.getDescricaoTipoPontoPDV()));
			pdvVO.setContato(pdv.getContato());
			pdvVO.setTelefone( tratarCampo(pdv.getTelefone()));
			pdvVO.setEndereco( tratarCampo(pdv.getEndereco()));
			pdvVO.setPrincipal(pdv.isPrincipal());
			pdvVO.setStatus( tratarCampo(pdv.getStatusPDV()));
			pdvVO.setFaturamento((pdv.getPorcentagemFaturamento()==null
									?"":CurrencyUtil.formatarValor(pdv.getPorcentagemFaturamento())));
		   
			listaRetorno.add(pdvVO);
		}
		
		return listaRetorno;
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
	
		PdvDTO dto = pdvService.obterPDV(idCota, idPdv);
		
		if(dto!= null){
			
			if(dto.getTamanhoPDV() == null){
				dto.setTipoPontoPDV(new TipoPontoPDV());
			}
			
			if(dto.getTipoEstabelecimentoAssociacaoPDV() == null){
				dto.setTipoEstabelecimentoAssociacaoPDV(new TipoEstabelecimentoAssociacaoPDV());
			}
			
			if(dto.getTipoLicencaMunicipal()== null){
				dto.setTipoLicencaMunicipal(new TipoLicencaMunicipal());
			}
			
			if(dto.getTipoPontoPDV() == null){
				dto.setTipoPontoPDV(new TipoPontoPDV());
			}
		}
			
		result.use(Results.json()).from(dto).recursive().serialize();
	}

	@Post
	@Path("/salvar")
	public void salvarPDV(PdvDTO pdvDTO){		
		
		pdvService.salvar(pdvDTO);
		
		if(pdvDTO.isDentroOutroEstabelecimento() && pdvDTO.getTipoEstabelecimentoAssociacaoPDV().getCodigo() == -1){
			throw new ValidacaoException(TipoMensagem.WARNING,"Tipo de Estabelecimento deve ser informado!");
		}
		
		if(pdvDTO.isExpositor() && pdvDTO.getTipoExpositor().isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING,"Tipo Expositor deve ser informado!");
		}
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	@Post
	@Path("/adicionarPeriodo")
	public void adicionarPeriodo(List<PeriodoFuncionamentoDTO> periodos, PeriodoFuncionamentoDTO novoPeriodo){		
		
		TipoMensagem status = TipoMensagem.SUCCESS;
		
		List<String> mensagens = new ArrayList<String>();
		
		List<TipoPeriodoFuncionamentoPDV> tiposPeriodosPossiveis = null;
	
		if(periodos == null) {
			periodos = new ArrayList<PeriodoFuncionamentoDTO>();
		}
		
		try {
			
			pdvService.validarPeriodos(periodos);
			periodos.add(novoPeriodo);
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
	public void uploadImagem(UploadedFile uploadedFile, String valorFinanceiro) {
	
		TipoMensagem status = TipoMensagem.SUCCESS;
		List<String> mensagens = new ArrayList<String>();
			
		try {
			//validarEntradaDados(uploadedFile, valorFinanceiro);
			
			//Grava o arquivo em disco e retorna o File do arquivo
			File fileArquivoBanco = gravarArquivoTemporario(uploadedFile);
			
			mensagens.add(SUCESSO_UPLOAD);
						
		}catch(Exception e) {
			mensagens.clear();
			mensagens.add(e.getMessage());
			status=TipoMensagem.ERROR;
		} finally {
			
			//Deleta os arquivos dentro do diretório temporário
			//deletarArquivoTemporario();
		}
		
		
		Object[] retorno = new Object[2];
		retorno[0] = mensagens;
		retorno[1] = status.name();		
				
		result.use(PlainJSONSerialization.class)
			.from(retorno, "result").recursive().serialize();
		
	}
	
	private File gravarArquivoTemporario(UploadedFile uploadedFile) {

		String pathAplicacao = servletContext.getRealPath("");
		
		pathAplicacao = pathAplicacao.replace("\\", "/");
		
		String dirDataAtual = DateUtil.formatarData(new Date(), FORMATO_DATA_DIRETORIO);
		
		File fileDir = new File(pathAplicacao, DIRETORIO_TEMPORARIO_ARQUIVO_BANCO + dirDataAtual);
		
		fileDir.mkdirs();
		
		File fileArquivoBanco = new File(fileDir, uploadedFile.getFileName());
		
		FileOutputStream fos = null;
		
		try {
			
			fos = new FileOutputStream(fileArquivoBanco);
			
			IOUtils.copyLarge(uploadedFile.getFile(), fos);
		
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
		
		return fileArquivoBanco;
	}
}
