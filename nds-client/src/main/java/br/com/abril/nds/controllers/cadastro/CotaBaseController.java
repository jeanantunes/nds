package br.com.abril.nds.controllers.cadastro;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CotaBaseDTO;
import br.com.abril.nds.dto.CotaBaseHistoricoDTO;
import br.com.abril.nds.dto.ResumoCotaBasesDTO;
import br.com.abril.nds.dto.SegmentoNaoRecebeCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaBaseDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.CotaBase;
import br.com.abril.nds.model.cadastro.CotaBaseCota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoAlteracao;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CotaBaseCotaService;
import br.com.abril.nds.service.CotaBaseService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.SegmentoNaoRecebidoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/cotaBase")
@Rules(Permissao.ROLE_CADASTRO_COTA_BASE)
public class CotaBaseController extends BaseController {
	
	private static final String COTA_BASE_DETALHES = "cotaBaseDetalhes";
	
	private static final String COTA_BASE_DTO = "cotaBaseDTO";
	
	private static final String COTA_BASE_HISTORICO = "cotaBaseHistorico";
	
	private static final String COTA_FILTRO = "cotaFiltro";
	
	@Autowired
	private CotaBaseService cotaBaseService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private Result result;
	
	@Autowired
	private CotaBaseCotaService cotaBaseCotaService;
	
	@Autowired
	private SegmentoNaoRecebidoService segmentoNaoRecebidoService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Path("/")
	public void index(){
		
	}
	
	@Post
	@Path("/pesquisarCotaNova")
	public void pesquisarCotaNova(Integer numeroCota){
		tratarFiltroPesquisaCota(numeroCota);
		
		CotaBase cotaBase = this.cotaBaseService.obterCotaNova(numeroCota, true);
		
		boolean existeCotaBase = false;
		if(cotaBase != null){
			existeCotaBase = this.cotaBaseCotaService.isCotaBaseAtiva(cotaBase);			
		}
		
		FiltroCotaBaseDTO filtro = (existeCotaBase)?this.cotaBaseService.obterCotaDoFiltro(cotaBase):
			this.cotaBaseService.obterDadosFiltro(cotaBase, false, true, numeroCota);
			
		if(filtro == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota \"" + numeroCota + "\" não encontrada!");
		}
		
		if(filtro.getTpDistribCota() != null && filtro.getTpDistribCota().getDescTipoDistribuicaoCota().equalsIgnoreCase("Alternativo")){
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota \"" + numeroCota + "\" não é do tipo Convencional!!");
		}
		
		if (existeCotaBase) {
			filtro.setDiasRestantes(calcularDiasRestantes(filtro.getDataFinal(), 
			        this.distribuidorService.obterDataOperacaoDistribuidor()));
		}
		else {
			filtro.setDataInicial(null);
			filtro.setDataFinal(null);
		}		
		
		session.setAttribute(COTA_FILTRO, filtro);
		
		this.result.use(Results.json()).from(filtro, "result").recursive().serialize();		
	}

	private String calcularDiasRestantes(Date dataFinal, Date dataOperacao) {		
		
		Calendar dtFinal = Calendar.getInstance();
		dtFinal.setTime(dataFinal);
		
		Calendar dtInicial = Calendar.getInstance();
		dtInicial.setTime(dataOperacao);
		
		long m1 = dtFinal.getTimeInMillis();
		long m2 = dtInicial.getTimeInMillis();
		
		Integer diasRestantes = (int) ((m1 - m2) / (24*60*60*1000));
		
		return (diasRestantes > 0)?diasRestantes.toString():"0";
	}

	private void tratarFiltroPesquisaCota(Integer numeroCota) {
		if(numeroCota == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Número da cota inválido!");
		}
		
		Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);
		
		if (cota == null) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Cota \"" + numeroCota + "\" não encontrada!");
			
		}
	}
	
	@Post
	@Path("/pesquisarCotasBase")
	public void pesquisarCotasBase(Integer numeroCota){
		
		if(numeroCota == null){
			throw new ValidacaoException(TipoMensagem.WARNING,"O Filtro deve ser preenchido.");
		}
		
		List<CotaBaseDTO> listaCotaBase = obterListaDeCotasBase(numeroCota, null);
		
		
		int qtdeInicialPadrao = 3;
		
		for (int indice = listaCotaBase.size(); indice < 3 ; indice++) {
			
			CotaBaseDTO produtoVO = new CotaBaseDTO();

			listaCotaBase.add(produtoVO);
		}
		
		TableModel<CellModelKeyValue<CotaBaseDTO>> tableModel =
				new TableModel<CellModelKeyValue<CotaBaseDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaCotaBase));
		
		tableModel.setTotal(qtdeInicialPadrao);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	

	/**
	 * 
	 * @param dto
	 * @return
	 */
	private List<CotaBaseDTO> obterListaCotaBaseFormatada(CotaBaseDTO dto) {
		
		final List<CotaBaseDTO> listaCotaBase = this.cotaBaseService.obterListaCotaPesquisaGeral(dto);
		
		if(!listaCotaBase.isEmpty()){
			
			final Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
			
			for(CotaBaseDTO cotaBase : listaCotaBase){
				cotaBase.setDiasRestantes(this.calcularDiasRestantes(cotaBase.getDtFinal(), dataOperacao));
				if(cotaBase.getDtFinal().after(dataOperacao)){
					cotaBase.setSituacao("Ativo");
				}else{
					cotaBase.setSituacao("Inativo");
				}
			}
			
		}

		return listaCotaBase;
	}

	
	@Post
	@Path("/pesquisarCotasBasePesquisaGeral")
	public void pesquisarCotasBasePesquisaGeral(Integer numeroCota, String sortorder, String sortname, int page, int rp){
		
		CotaBaseDTO dto = new CotaBaseDTO();
		dto.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		dto.setNumeroCota(numeroCota);
		
		tratarPaginacao(dto);
		
		List<CotaBaseDTO> listaFormatada = obterListaCotaBaseFormatada(dto);
		
		TableModel<CellModelKeyValue<CotaBaseDTO>> tableModel =
				new TableModel<CellModelKeyValue<CotaBaseDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaFormatada));
		
		tableModel.setPage(dto.getPaginacao().getPaginaAtual());

		tableModel.setTotal(dto.getPaginacao().getQtdResultadosTotal());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	private void tratarPaginacao(CotaBaseDTO dto) {

		CotaBaseDTO cotaBaseSession = (CotaBaseDTO) session
				.getAttribute(COTA_BASE_DTO);
		
		if (cotaBaseSession != null && !cotaBaseSession.equals(dto)) {

			dto.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(COTA_BASE_DTO, dto);
	
		
	}
	
	private void tratarPaginacaoHistorico(CotaBaseDTO dto) {

		CotaBaseDTO cotaBaseSession = (CotaBaseDTO) session
				.getAttribute(COTA_BASE_HISTORICO);
		
		if (cotaBaseSession != null && !cotaBaseSession.equals(dto)) {

			dto.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(COTA_BASE_HISTORICO, dto);
	
		
	}
	
	

	private List<CotaBaseDTO> obterListaDeCotasBase(Integer numeroCota, CotaBaseDTO dto) {
		CotaBase cotaBase = this.cotaBaseService.obterCotaNova(numeroCota, true);
		List<CotaBaseDTO> listaCotaBase = new ArrayList<CotaBaseDTO>();
		if(cotaBase != null){
			listaCotaBase = this.cotaBaseService.obterCotasBases(this.cotaBaseService.obterCotaNova(numeroCota, true),dto );	
		}
		return listaCotaBase;
	}
	
	private void validarCota(Cota cota, CotaBase cotaBase, Integer[] numerosDeCotasBase) {
		
		if(cota.getSituacaoCadastro() == SituacaoCadastro.INATIVO) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota \"" + cota.getNumeroCota() + "\" não está ativa!");
		} 
		
		if(cotaBase != null && numerosDeCotasBase != null && this.cotaBaseCotaService.isCotaBaseAtiva(cotaBase, numerosDeCotasBase)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota \"" + cota.getNumeroCota() + "\" tem cota base ativa!");
		}
	}
	
	@Post
	@Path("/obterCota")
	public void obterCota(Integer numeroCota){
		
		Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);
		
		if (cota == null) {
		    
		    throw new ValidacaoException(TipoMensagem.WARNING, "Cota \"" + numeroCota + "\" não encontrada");
		}
		
		CotaBase cotaBase = this.cotaBaseService.obterCotaNova(numeroCota, true);
		
		validarCota(cota, cotaBase, null);
		
		FiltroCotaBaseDTO filtroPrincipal = (FiltroCotaBaseDTO) session.getAttribute(COTA_FILTRO);
		
		FiltroCotaBaseDTO filtro = this.cotaBaseService.obterDadosFiltro(cotaBase, true, true, numeroCota);
		
		if(filtro == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota \"" + numeroCota + "\" não pode ser adicionada!");
		}
		
		else if(filtro.getTpDistribCota() != null && filtro.getTpDistribCota().getDescTipoDistribuicaoCota() != null 
				&& filtro.getTpDistribCota().getDescTipoDistribuicaoCota().equalsIgnoreCase("Alternativo")) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota \"" + numeroCota + "\" não é do tipo Convencional!!");
		}
		
		else if(filtroPrincipal.getNumeroCota().equals(filtro.getNumeroCota()) ) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota \"" + filtro.getNumeroCota() + "\" não pode ser adicionada!");
		} else {
			this.result.use(Results.json()).from(filtro, "result").recursive().serialize();			
		}
		
	}

	@Post
	@Path("/excluirCotaBase")
	public void excluirCotaBase(Integer numeroCotaNova, Long idCotaBase){
		
		CotaBase cotaBase = this.cotaBaseService.obterCotaNova(numeroCotaNova, true);
		
		Cota cotaParaDesativar = this.cotaService.obterPorId(idCotaBase);
		
		cotaBaseCotaService.desativarCotaBase(cotaBase, cotaParaDesativar);			
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Cota excluida com sucesso."), "result").recursive().serialize();
		
	}
	
	@Post
	@Path("/confirmarCotasBase")
	public void confirmarCotasBase(Integer[] numerosDeCotasBase, Integer idCotaNova, BigDecimal indiceAjuste, String cotasBaseCadastradas){
		
		Cota cotaNova = this.cotaService.obterPorNumeroDaCota(idCotaNova);	
		
		CotaBase cotaBase = this.cotaBaseService.obterCotaNova(idCotaNova, false);
		
		Integer []numerosDeCotasBaseValidacao =  null;
		
		if (!StringUtils.isEmpty(cotasBaseCadastradas)) {

			String[] cotasBaseCadastradasArr = cotasBaseCadastradas.split(",");
			
			numerosDeCotasBaseValidacao = new Integer[cotasBaseCadastradasArr.length];
			
			for (int i=0; i < cotasBaseCadastradasArr.length; i++) {
				
				numerosDeCotasBaseValidacao[i] = Integer.valueOf(cotasBaseCadastradasArr[i]);
			}
			
		}
		
		validarCota(cotaNova, cotaBase, numerosDeCotasBaseValidacao);
		
		if(cotaBase != null) {
			
			List<CotaBaseDTO> listaCotaBase = this.cotaBaseService.obterCotasBases(cotaBase, null);		
			
			validarCotasDigitadas(numerosDeCotasBase, listaCotaBase, idCotaNova);
		}
		
		salvar(numerosDeCotasBase, indiceAjuste, cotaNova, cotaBase);			
		
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Cota base cadastrada com sucesso!"), "result").recursive().serialize();
		
	}
	
	

	@Post
	@Path("/obterCotasDoHistorico")
	public void obterCotasDoHistorico(Integer numeroCota, String sortorder, String sortname, int page, int rp){
		
		CotaBaseDTO dto = new CotaBaseDTO();
		dto.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		dto.setNumeroCota(numeroCota);
		
		tratarPaginacaoHistorico(dto);
		
		CotaBase cotaBase = this.cotaBaseService.obterCotaNova(numeroCota, false);
		
		if(cotaBase == null){
			throw new ValidacaoException(TipoMensagem.WARNING,"Essa cota ainda não possui histórico");
		}
		List<CotaBaseHistoricoDTO> listaDeHistorico = this.cotaBaseService.obterCotasHistorico(cotaBase,  dto);			
		
		if(listaDeHistorico.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING,"Nenhum registro encontrado.");
		}
		
		TableModel<CellModelKeyValue<CotaBaseHistoricoDTO>> tableModel =
				new TableModel<CellModelKeyValue<CotaBaseHistoricoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaDeHistorico));
		
		tableModel.setPage(dto.getPaginacao().getPaginaAtual());

		tableModel.setTotal(dto.getPaginacao().getQtdResultadosTotal());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	@Post
	@Path("/segmentosRecebidos")
	public void segmentosRecebidos(Integer numeroCota){
		Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);
		
		List<SegmentoNaoRecebeCotaDTO> listaSegmentosNaoRecebidos = this.segmentoNaoRecebidoService.
				obterSegmentosNaoRecebidosCadastradosNaCota(cota);		
		
		listaSegmentosNaoRecebidos.addAll(this.segmentoNaoRecebidoService.obterSegmentosNaoRecebidosCadastradosCotaBase(cota.getId()));
		
		TableModel<CellModelKeyValue<SegmentoNaoRecebeCotaDTO>> tableModel =
				new TableModel<CellModelKeyValue<SegmentoNaoRecebeCotaDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaSegmentosNaoRecebidos));
		
		tableModel.setPage(1);

		tableModel.setTotal(listaSegmentosNaoRecebidos.size());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	
	private List<CotaBaseDTO> obterListaAuxiliar(CotaBaseDTO cotaBaseDTO) {
		
		List<CotaBaseDTO> listaDetalheCota = this.cotaBaseService.obterListaTelaDetalhe(this.cotaBaseService.obterCotaNova(cotaBaseDTO.getNumeroCota(), false));
		
		if (listaDetalheCota == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum detalhe encontrado.");
		}
		
		List<CotaBaseDTO> listaAuxiliar = new ArrayList<CotaBaseDTO>();
		CotaBaseDTO dtoParaVisao = new CotaBaseDTO();
		
		int cont = 0;
		for(CotaBaseDTO dto : listaDetalheCota){
			Double indice = null;
			if(dto.getIndiceAjuste() == null){
				indice = 0.0;
			}else{
				indice = Double.parseDouble(dto.getIndiceAjuste().replace(',', '.') );				
			}
			if(cont == 0){
				dtoParaVisao.setEquivalente01(dto.getNumeroCota() + " - " + dto.getNomeCota());			
			}else if(cont == 1){
				dtoParaVisao.setEquivalente02(dto.getNumeroCota() + " - " + dto.getNomeCota());
			}else if(cont == 2){
				dtoParaVisao.setEquivalente03(dto.getNumeroCota() + " - " + dto.getNomeCota());
			}
			cont++;
			dtoParaVisao.setIndiceAjuste(new BigDecimal(indice));
			
			listaAuxiliar.add(dtoParaVisao);			
		}
		
		return listaAuxiliar;
	}
	
	@Post
	@Path("/obterTelaDetalhes")
	public void obterTelaDetalhes(Integer numeroCota) {
		
		CotaBaseDTO cotaBaseDTO = new CotaBaseDTO();
		
		cotaBaseDTO.setNumeroCota(numeroCota);
		
		session.setAttribute(COTA_BASE_DETALHES, cotaBaseDTO);
		
		List<CotaBaseDTO> listaAuxiliar = obterListaAuxiliar(cotaBaseDTO);
		
		List<CotaBaseDTO> listaFinal = new ArrayList<CotaBaseDTO>();
		
		if((listaAuxiliar != null) && (!listaAuxiliar.isEmpty())){
			listaFinal.add(listaAuxiliar.get(0));
		}
		
		TableModel<CellModelKeyValue<CotaBaseDTO>> tableModel = new TableModel<CellModelKeyValue<CotaBaseDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaFinal));
		
		tableModel.setPage(1);

		tableModel.setTotal(listaFinal.size());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Get
	public void exportar(FileType fileType, String tipoDeLista) throws IOException {
		
		if(tipoDeLista.equals("pesquisaGeral")) {
			
			CotaBaseDTO dto = (CotaBaseDTO) session.getAttribute(COTA_BASE_DTO);
			
			List<CotaBaseDTO> listaFormatada = obterListaCotaBaseFormatada(dto);
			
			FileExporter.to("PESQUISA_GERAL_COTA_BASE", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, 
					listaFormatada, CotaBaseDTO.class, this.httpResponse);
			
		} else if(tipoDeLista.equals("pesquisaHistorico")) {
			
			CotaBaseDTO dto = (CotaBaseDTO) session.getAttribute(COTA_BASE_HISTORICO);
			
			List<CotaBaseHistoricoDTO> listaCotaBase = this.cotaBaseService.obterCotasHistorico(this.cotaBaseService.obterCotaNova(dto.getNumeroCota(), false),  dto);
			
			FileExporter.to("PESQUISA_HISTORICO_COTA_BASE", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, 
					listaCotaBase, CotaBaseHistoricoDTO.class, this.httpResponse);			
		
		} else if(tipoDeLista.equals("pesquisaDetalhes")) {
			
			CotaBaseDTO dto = (CotaBaseDTO) session.getAttribute(COTA_BASE_DETALHES);
			
			List<ResumoCotaBasesDTO> listResumo = new ArrayList<ResumoCotaBasesDTO>();
			
			for (CotaBaseDTO cotaBaseDTO:obterListaAuxiliar(dto)) {
				
				ResumoCotaBasesDTO resumoCotaBasesDTO = new ResumoCotaBasesDTO();
				resumoCotaBasesDTO.setIndiceAjuste(cotaBaseDTO.getIndiceAjuste());
				resumoCotaBasesDTO.setEquivalente01(cotaBaseDTO.getEquivalente01());
				resumoCotaBasesDTO.setEquivalente02(cotaBaseDTO.getEquivalente02());
				resumoCotaBasesDTO.setEquivalente03(cotaBaseDTO.getEquivalente03());
				listResumo.add(resumoCotaBasesDTO);
				break;
			}
			
			FileExporter.to("PESQUISA_DETALHES_COTA_BASE", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, 
						listResumo, ResumoCotaBasesDTO.class, this.httpResponse);		
				
		}
		
		result.nothing();
	}
	
	@Post
	@Path("/obterIdPDVPrincipal")
	public void obterIdPDVPrincipal(Integer numeroCota){
		
		Cota cota = this.cotaService.obterCotaPDVPorNumeroDaCota(numeroCota);
		Long idPdvPrincipal = null;
		for(PDV pdv : cota.getPdvs()){
			if(pdv.getCaracteristicas().isPontoPrincipal()){
				idPdvPrincipal = pdv.getId();
			}
		}
		
		result.use(Results.json()).withoutRoot().from(idPdvPrincipal).recursive().serialize();
	}

	private void salvar(Integer[] numerosDeCotasBase, BigDecimal indiceAjuste, Cota cotaNova, CotaBase cotaBaseJaSalva) {
		if(cotaBaseJaSalva == null){
			CotaBase cotaBase = new CotaBase();
			cotaBase.setDataInicio(new Date());
			cotaBase.setDataFim(DateUtil.adicionarDias(new Date(), 180));
			cotaBase.setIndiceAjuste(indiceAjuste);
			cotaBase.setCota(cotaNova);
			this.cotaBaseService.salvar(cotaBase);
			for(Integer cotabBase: numerosDeCotasBase){
				CotaBaseCota cotaBaseCota = new CotaBaseCota();
				Cota cotaBaseParaSalvar = this.cotaService.obterPorNumeroDaCota(cotabBase);
				cotaBaseCota.setCota(cotaBaseParaSalvar);
				cotaBaseCota.setCotaBase(cotaBase);
				cotaBaseCota.setAtivo(true);
				cotaBaseCota.setDtInicioVigencia(cotaBase.getDataInicio());
				cotaBaseCota.setDtFimVigencia(cotaBase.getDataFim());
				cotaBaseCota.setTipoAlteracao(TipoAlteracao.INCLUSAO);
				this.cotaBaseCotaService.salvar(cotaBaseCota);
			}			
		}else{			
			cotaBaseJaSalva.setIndiceAjuste(indiceAjuste);			
			this.cotaBaseService.atualizar(cotaBaseJaSalva);
			for(Integer cotabBase: numerosDeCotasBase){
				CotaBaseCota cotaBaseCota = new CotaBaseCota();
				Cota cotaBaseParaSalvar = this.cotaService.obterPorNumeroDaCota(cotabBase);
				cotaBaseCota.setCota(cotaBaseParaSalvar);
				cotaBaseCota.setCotaBase(cotaBaseJaSalva);
				cotaBaseCota.setAtivo(true);
				cotaBaseCota.setDtInicioVigencia(new Date());
				cotaBaseCota.setDtFimVigencia(cotaBaseJaSalva.getDataFim());
				cotaBaseCota.setTipoAlteracao(TipoAlteracao.INCLUSAO);
				this.cotaBaseCotaService.salvar(cotaBaseCota);
			}		
			
		}
	}

	private void validarCotasDigitadas(Integer[] numerosDeCotasBase, List<CotaBaseDTO> listaCotaBase, Integer idCotaNova) {
		if(listaCotaBase.size() != 0){
			for(CotaBaseDTO dto : listaCotaBase){
				for(Integer cotaDigitada: numerosDeCotasBase){
					if(dto.getNumeroCota().equals(cotaDigitada)){
						throw new ValidacaoException(TipoMensagem.WARNING, "Cota já cadastrada.");
					}
				}
			}			
		}else{
			for(Integer cotaDigitada: numerosDeCotasBase){
				if(cotaDigitada.equals(idCotaNova)){
					throw new ValidacaoException(TipoMensagem.WARNING, "Cota já cadastrada.");
				}
			}
		}
	}
	
}
