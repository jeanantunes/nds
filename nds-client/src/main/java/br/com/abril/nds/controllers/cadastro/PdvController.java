package br.com.abril.nds.controllers.cadastro;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.PdvVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.CaracteristicaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.PeriodoFuncionamentoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.LicencaMunicipal;
import br.com.abril.nds.model.cadastro.TipoLicencaMunicipal;
import br.com.abril.nds.model.cadastro.pdv.StatusPDV;
import br.com.abril.nds.model.cadastro.pdv.TamanhoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoEstabelecimentoAssociacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPeriodoFuncionamentoPDV;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.PdvService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
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
	
	private ServletContext servletContext;
	
	private static final String FORMATO_DATA_DIRETORIO = "yyyy-MM-dd";
	
	private static final String DIRETORIO_TEMPORARIO_ARQUIVO_BANCO = "images/temp/pdv/";
	
	private static final Logger LOG = LoggerFactory
			.getLogger(PdvController.class);
	
	public PdvController(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

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
		result.include("listaDiasFuncionamento",gerarDiasFuncionamento(TipoPeriodoFuncionamentoPDV.values()));
		result.include("listaTamanhoPDV",gerarTamanhosPDV(TamanhoPDV.values()));
		result.include("listaTipoEstabelecimento",gerarTiposEstabelecimento());
		result.include("listaTipoLicencaMunicipal",gerarTiposLicencaMunicipal());
				
	}
	
	private Object gerarDiasFuncionamento(TipoPeriodoFuncionamentoPDV[] tipos) {
		
		List<ItemDTO<String, String>> itens = new ArrayList<ItemDTO<String,String>>();
		
		for(TipoPeriodoFuncionamentoPDV item: tipos) {
			itens.add(new ItemDTO<String, String>(item.name(), item.getDescricao()));
		}
		
		return itens;
	}

	private List<ItemDTO<Long, String>> gerarTiposEstabelecimento() {
		List<ItemDTO<Long, String>> itens = new ArrayList<ItemDTO<Long,String>>();
		
		//TODO obter lista do banco de dados
		List<TipoEstabelecimentoAssociacaoPDV> licencas = getFakeTiposEstabelecimento();
		
		for(TipoEstabelecimentoAssociacaoPDV licenca: licencas) {
			itens.add(new ItemDTO<Long, String>(licenca.getCodigo(), licenca.getDescricao()));
		}
		
		return itens;
	}

	private List<TipoEstabelecimentoAssociacaoPDV> getFakeTiposEstabelecimento() {
		
		List<TipoEstabelecimentoAssociacaoPDV> estabelecimentos = new ArrayList<TipoEstabelecimentoAssociacaoPDV>();
		 
		 estabelecimentos.add(new TipoEstabelecimentoAssociacaoPDV());
		 estabelecimentos.get(0).setCodigo(1L);
		 estabelecimentos.get(0).setDescricao("Tipo Estab 1");
		 
		 estabelecimentos.add(new TipoEstabelecimentoAssociacaoPDV());
		 estabelecimentos.get(1).setCodigo(1L);
		 estabelecimentos.get(1).setDescricao("Tipo Estab 2");
		 
		return estabelecimentos;
	}

	private List<ItemDTO<String, String>> gerarItemStatus(StatusPDV[] statusPDVs) {
		
		List<ItemDTO<String, String>> itens = new ArrayList<ItemDTO<String,String>>();
		
		for(StatusPDV item: statusPDVs) {
			itens.add(new ItemDTO<String, String>(item.name(), item.getDescricao()));
		}
		
		return itens;
	}
	
	private List<ItemDTO<String, String>> gerarTamanhosPDV(TamanhoPDV[] tamanhoPDVs) {
		
		List<ItemDTO<String, String>> itens = new ArrayList<ItemDTO<String,String>>();
		
		for(TamanhoPDV item: tamanhoPDVs) {
			itens.add(new ItemDTO<String, String>(item.name(), item.getDescricao()));
		}
		
		return itens;
	}
	
	/**
	 * Gera dados do combo de Dias de Funcionamento (Aba - Dados Básicos)
	 * @return
	 */
	private List<ItemDTO<String, String>> gerarTiposLicencaMunicipal() {
		
		List<ItemDTO<String, String>> itens = new ArrayList<ItemDTO<String,String>>();
		
		//TODO obter lista do banco de dados
		List<LicencaMunicipal> licencas = getFakelicencas();
		
		for(LicencaMunicipal licenca: licencas) {
			itens.add(new ItemDTO<String, String>(licenca.getNumeroLicenca(), licenca.getNomeLicenca()));
		}
		
		return itens;
	}
	

	//TODO remover após implementação real
	private List<LicencaMunicipal> getFakelicencas() {
		
		 List<LicencaMunicipal> licencas = new ArrayList<LicencaMunicipal>();
		 
		 licencas.add(new LicencaMunicipal());
		 licencas.get(0).setNumeroLicenca("1");
		 licencas.get(0).setNomeLicenca("Licenca 1");
		 
		 licencas.add(new LicencaMunicipal());
		 licencas.get(1).setNumeroLicenca("2");
		 licencas.get(1).setNomeLicenca("Licenca 2");
		 
		return licencas;
	}


	@Post
	@Path("/consultar")
	public void consultarPDVs(Long idCota,String sortname, String sortorder){
		
		List<PdvVO>  listaPdvs = getMock();
		
		ordenarListaPdvs(sortname, sortorder, listaPdvs);
		
		TableModel<CellModelKeyValue<PdvVO>> tableModel = new TableModel<CellModelKeyValue<PdvVO>>();
			
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaPdvs));
		
		tableModel.setTotal(listaPdvs.size());
		
		tableModel.setPage(1);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	private void ordenarListaPdvs(String sortname, String sortorder, List<PdvVO> listaPdvs){
		
		if (sortname != null && !listaPdvs.isEmpty()) {

			sortorder = sortorder == null ? "asc" : sortorder;

			Ordenacao ordenacao = Util.getEnumByStringValue(Ordenacao.values(), sortorder);

			PaginacaoUtil.ordenarEmMemoria(listaPdvs, ordenacao, sortname);
		}
	}
	
	@Post
	@Path("/excluir")
	public void excluirPDV(Long idPdv, Long idCota){
		
		if(!pdvService.isExcluirPdv(idPdv)){
			throw new ValidacaoException(TipoMensagem.WARNING, "Pdv não pode ser excluido!");
		}
		
		//TODO Chamar componente para exclusão
	
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "PDV excluido com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	public List<PdvVO> getMock(){
		
		List<PdvVO> listaPdvs = new ArrayList<PdvVO>();
		
		for(int x=0; x<6 ; x++){
			listaPdvs.add(new PdvVO(x,Long.valueOf(x),Long.valueOf(x),"Nome PDV" + x,"Tipo PDV","CONTATo",
									"3689-25444","Rua XPTO","Bairro","Cidade",true,"Status","10"));
		}
		
		return listaPdvs;
		
		
	}

		
	@Post
	@Path("/editar")
	public void editarPDV(Long idPdv, Long idCota){
		
		PdvDTO dto = new PdvDTO();
		dto.setContato("asasaa");
		dto.setDentroOutroEstabelecimento(Boolean.TRUE);
		
		CaracteristicaDTO caracteristicaDTO = new CaracteristicaDTO();
		caracteristicaDTO.setLuminoso(Boolean.TRUE);
		
		dto.setCaracteristicaDTO(caracteristicaDTO);
		
		simularDadosBasicos(dto);
				
		result.use(Results.json()).from(dto).recursive().serialize();
	}
	
	private void simularDadosBasicos(PdvDTO dto) {
		dto.setStatusPDV(StatusPDV.SUSPENSO);
		dto.setNomePDV("Guilherme de Morais");
		dto.setContato("35561553");
		dto.setDataInicio(new Date());
		dto.setSite("www.google.com");
		dto.setEmail("guilherme@email.com");
		dto.setPontoReferencia("Ponto de Referencia");
		dto.setDentroDeOutroEstabelecimento(true);
		TipoEstabelecimentoAssociacaoPDV tipo= new TipoEstabelecimentoAssociacaoPDV();
		tipo.setCodigo(123L);
		tipo.setDescricao("Tipo");
		dto.setTipoEstabelecimentoAssociacaoPDV(tipo);
		dto.setTamanhoPDV(TamanhoPDV.SG);
		dto.setSistemaIPV(true);
		dto.setQtdeFuncionarios(10);
		dto.setNumeroLicenca("2234");
		dto.setPorcentagemFaturamento(new BigDecimal(10));
		dto.setNomeLicenca("licenca x");
		TipoLicencaMunicipal tipoLicenca = new TipoLicencaMunicipal();
		tipo.setCodigo(123L);
		tipo.setDescricao("TipoLicensa");
		dto.setTipoLicencaMunicipal(tipoLicenca);
		
		
		List<PeriodoFuncionamentoDTO> listaPeriodos = new ArrayList<PeriodoFuncionamentoDTO>();
		listaPeriodos.add(new PeriodoFuncionamentoDTO(TipoPeriodoFuncionamentoPDV.QUARTA_FEIRA,"10:10","10:10"));
		listaPeriodos.add(new PeriodoFuncionamentoDTO(TipoPeriodoFuncionamentoPDV.TERCA_FEIRA,"10:10","10:10"));
		listaPeriodos.add(new PeriodoFuncionamentoDTO(TipoPeriodoFuncionamentoPDV.SEGUNDA_FEIRA,"10:10","10:10"));
		
		dto.setPeriodosFuncionamentoDTO(listaPeriodos);
	}

	@Post
	@Path("/salvar")
	public void salvarPDV(PdvDTO pdvDTO){		
		//TODO implementar a logica de validação e salvar os dados na sessão do usuario
		
		pdvDTO.setNumeroCota(123);
		
		pdvService.salvar(pdvDTO);
		
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
