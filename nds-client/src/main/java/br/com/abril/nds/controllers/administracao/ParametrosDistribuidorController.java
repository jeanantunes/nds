package br.com.abril.nds.controllers.administracao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.ParametrosDistribuidorVO;
import br.com.abril.nds.client.vo.RegistroDiaOperacaoFornecedorVO;
import br.com.abril.nds.client.vo.TiposNotasFiscaisParametrosVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CotaTipoDTO;
import br.com.abril.nds.dto.GrupoCotaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.MunicipioDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.TipoGrupo;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.cadastro.pdv.TipoCaracteristicaSegmentacaoPDV;
import br.com.abril.nds.model.fiscal.notafiscal.RegimeTributario;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.repository.RegimeTributarioRepository;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.DistribuicaoFornecedorService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.GrupoService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.PessoaService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.service.integracao.RegimeTributarioService;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.InputStreamDownload;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.view.Results;

/**
 * @author infoA2
 * Controller de parâmetros do distribuidor
 */
@Resource
@Path("/administracao/parametrosDistribuidor")
@Rules(Permissao.ROLE_ADMINISTRACAO_PARAMETROS_DISTRIBUIDOR)
public class ParametrosDistribuidorController extends BaseController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private DistribuicaoFornecedorService distribuicaoFornecedorService;

	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private ParametrosDistribuidorService parametrosDistribuidorService;
	
	@Autowired
	private RegimeTributarioService regimeTributarioService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ServletContext servletContext;
	
	private static final String ATRIBUTO_SESSAO_LOGOTIPO_CONTENT_TYPE = "cadastroDistribuidorLogotipoContentType";
	
	private static final String ATRIBUTO_REQUEST_HAS_LOGOTIPO = "hasLogotipo";

	private static final String DIRETORIO_TEMPORARIO_PARAMETROS_DISTRIBUIDOR = "temp/parametros_distribuidor/";
	
	private static final String ATTACHMENT_LOGOTIPO = "imagem_logotipo";
	
	private static final String MUNICIPIOS_SELECIONADOS = "idsMunicipiosSelecionadosGrupo";
	
	private static final String COTAS_SELECIONADAS = "idsCotasSelecionadaGrupo";
	
	private static final String TIPO_COTA = "tipoCotaGrupo";
	
	@Autowired 
	private GrupoService grupoService;
	
	@Path("/")
	public void index() {
		
		this.limparLogoSessao();
		
		result.include("parametrosDistribuidor", parametrosDistribuidorService.getParametrosDistribuidor());
		result.include("listaDiaOperacaoFornecedor", distribuicaoFornecedorService.buscarDiasOperacaoFornecedor());
		result.include("fornecedores", fornecedorService.obterFornecedores());
		result.include("listaTipoPrestador", this.carregarComboTipoPrestador());
		result.include("listaRegimeTributario", this.carregarComboRegimeTributario());
//		result.include("listaObrigacaoFiscal", this.carregarComboObrigacaoFiscal());
		result.include("listaTiposNotaFiscal", parametrosDistribuidorService.obterTiposNotaFiscalDistribuidor());
		result.include("listaTiposEmissaoNotaFiscal", parametrosDistribuidorService.obterTiposEmissoesNotaFiscalDistribuidor());
		result.include("listaEstadosAnuencia", parametrosDistribuidorService.obterEstadosAtendidosPeloDistribuidor());
		
		boolean possuiDistribuicaoOutrosEstados = (parametrosDistribuidorService.obterEstadosAtendidosPeloDistribuidor()
														.contains(parametrosDistribuidorService.getParametrosDistribuidor().getEndereco().getUf()) 
														&& parametrosDistribuidorService.obterEstadosAtendidosPeloDistribuidor().size() > 1);
		result.include("possuiDistribuicaoOutrosEstados", possuiDistribuicaoOutrosEstados);
				
		this.buscarLogoArmazenarSessao();
		
		session.removeAttribute(COTAS_SELECIONADAS);
		session.removeAttribute(MUNICIPIOS_SELECIONADOS);
	}
	
	private List<ItemDTO<Long, String>>  carregarComboRegimeTributario() {
		
		List<RegimeTributario> regimesTributarios = regimeTributarioService.obterRegimesTributarios();
		List<ItemDTO<Long, String>> listaRegimesTributarios = new ArrayList<ItemDTO<Long, String>>();
		
		for(RegimeTributario re : regimesTributarios) {
			ItemDTO<Long, String> reKV = new ItemDTO<Long, String>();
			reKV.setKey(re.getCodigo());
			reKV.setValue(re.getDescricao());
			listaRegimesTributarios.add(reKV);
		}
		
		return listaRegimesTributarios;
	}

	private List<ItemDTO<TipoAtividade, String>> carregarComboTipoPrestador() {

		List<ItemDTO<TipoAtividade, String>> listaTipoPrestador =
			new ArrayList<ItemDTO<TipoAtividade, String>>();
		
		listaTipoPrestador.add(
			new ItemDTO<TipoAtividade, String>(TipoAtividade.PRESTADOR_SERVICO,
											   TipoAtividade.PRESTADOR_SERVICO.getDescricao()));
		
		listaTipoPrestador.add(
			new ItemDTO<TipoAtividade, String>(TipoAtividade.MERCANTIL,
											   TipoAtividade.MERCANTIL.getDescricao()));
		
		return listaTipoPrestador;
	}
	/*
	private List<ItemDTO<ObrigacaoFiscal, String>> carregarComboObrigacaoFiscal() {

		List<ItemDTO<ObrigacaoFiscal, String>> listaObrigacaoFiscal =
			new ArrayList<ItemDTO<ObrigacaoFiscal, String>>();
		
		listaObrigacaoFiscal.add(
			new ItemDTO<ObrigacaoFiscal, String>(ObrigacaoFiscal.COTA_TOTAL,
												 ObrigacaoFiscal.COTA_TOTAL.getDescricao()));
		
		listaObrigacaoFiscal.add(
			new ItemDTO<ObrigacaoFiscal, String>(ObrigacaoFiscal.COTA_NFE_VENDA,
												 ObrigacaoFiscal.COTA_NFE_VENDA.getDescricao()));
		
		listaObrigacaoFiscal.add(
			new ItemDTO<ObrigacaoFiscal, String>(ObrigacaoFiscal.DEVOLUCAO_FORNECEDOR,
												 ObrigacaoFiscal.DEVOLUCAO_FORNECEDOR.getDescricao()));
		
		return listaObrigacaoFiscal;
	}*/
	
	private void buscarLogoArmazenarSessao() {

		InputStream imgLogotipo = parametrosDistribuidorService.getLogotipoDistribuidor();
		
		if (imgLogotipo != null) {
		
			request.setAttribute(ATRIBUTO_REQUEST_HAS_LOGOTIPO, true);
			
			this.gravarArquivoTemporario(imgLogotipo);
			
		} else {
			
			request.setAttribute(ATRIBUTO_REQUEST_HAS_LOGOTIPO, false);
		}
	}

	public Download getLogo() {
		
		InputStream imgLogotipo = this.getInputStreamArquivoTemporario();;
		
		if (imgLogotipo != null) {
		
			return new InputStreamDownload(imgLogotipo, null, null);
		}
		
		return null;
	}

	@Rules(Permissao.ROLE_ADMINISTRACAO_PARAMETROS_DISTRIBUIDOR_ALTERACAO)
	public void salvarLogo(UploadedFile logo) {
		
		this.gravarArquivoTemporario(logo.getFile());
		
		session.setAttribute(ATRIBUTO_SESSAO_LOGOTIPO_CONTENT_TYPE, logo.getContentType());
		
		result.use(PlainJSONSerialization.class).from("", "result").serialize();
	}
	
	/**buscarDiasOperacaoFornecedor
	 * Grava as alterações de parametros realizadas para o distribuidor
	 * @param distribuidor
	 */
	@Rules(Permissao.ROLE_ADMINISTRACAO_PARAMETROS_DISTRIBUIDOR_ALTERACAO)
	public void gravar(ParametrosDistribuidorVO parametrosDistribuidor) {
	    
		InputStream imgLogotipo = this.getInputStreamArquivoTemporario();
		
		String contentType = (String) session.getAttribute(ATRIBUTO_SESSAO_LOGOTIPO_CONTENT_TYPE);
		
		
		validarCadastroDistribuidor(parametrosDistribuidor);
		parametrosDistribuidorService.salvarDistribuidor(
			parametrosDistribuidor, imgLogotipo, contentType);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Parâmetros do Distribuidor alterados com sucesso"),"result").recursive().serialize();
	}
	
	private void gravarArquivoTemporario(InputStream imgLogotipo) {

		File fileLogotipo = this.getFileLogo();
		
		FileOutputStream fos = null;
		
		try {
			
			fos = new FileOutputStream(fileLogotipo);
			
			IOUtils.copyLarge(imgLogotipo, fos);
		
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
	}

	private File getFileLogo() {
		
		String pathAplicacao = servletContext.getRealPath("");
		
		pathAplicacao = pathAplicacao.replace("\\", "/");
		
		File fileDir = new File(pathAplicacao, DIRETORIO_TEMPORARIO_PARAMETROS_DISTRIBUIDOR);
		
		fileDir.mkdirs();
		
		File fileLogotipo = new File(fileDir, ATTACHMENT_LOGOTIPO);
		
		return fileLogotipo;
	}
	
	private InputStream getInputStreamArquivoTemporario() {
		
		File fileLogotipo = this.getFileLogo();
		
		try {
			
			return new FileInputStream(fileLogotipo);
			
		} catch (FileNotFoundException e) {
			
			return null;
		}
	}
	
	private void limparLogoSessao() {
		
		session.removeAttribute(ATRIBUTO_REQUEST_HAS_LOGOTIPO);
		
		session.removeAttribute(ATRIBUTO_SESSAO_LOGOTIPO_CONTENT_TYPE);
	}
	
	/**
	 * Realiza a exclusão dos dias de distribuição e recolhimento do fornecedor
	 */
	@Rules(Permissao.ROLE_ADMINISTRACAO_PARAMETROS_DISTRIBUIDOR_ALTERACAO)
	public void excluirDiasDistribuicaoFornecedor(long codigoFornecedor) {
		distribuicaoFornecedorService.excluirDadosFornecedor(codigoFornecedor);
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Dias de Distribuição do Fornecedor excluido com sucesso"),"result").recursive().serialize();
	}

	/**
	 * Retorna via json a lista de dias de distribuição e recolhimento do fornecedor
	 */
	public void recarregarDiasDistribuidorFornecedorGrid() {
		
		List<RegistroDiaOperacaoFornecedorVO> registros = distribuicaoFornecedorService.buscarDiasOperacaoFornecedor();
		
		result.use(Results.json()).withoutRoot().from(registros).include("fornecedor","fornecedor.juridica").serialize();
	}
	
	/**
	 * Grava os dias de distribuição de recolhimento do fornecedor
	 * @param distribuidor
	 */
	@Post
	@Path("/gravarDiasDistribuidorFornecedor")
	@Rules(Permissao.ROLE_ADMINISTRACAO_PARAMETROS_DISTRIBUIDOR_ALTERACAO)
	public void gravarDiasDistribuidorFornecedor(String selectFornecedoresLancamento, String selectDiasLancamento, String selectDiasRecolhimento) throws Exception {
		
		this.validarAssociacaoDiasOperacao(
			selectFornecedoresLancamento, selectDiasLancamento, selectDiasRecolhimento);
		
		List<String> listaFornecedoresLancamento = Arrays.asList(selectFornecedoresLancamento.split(","));
		List<String> listaDiasLancamento		 = Arrays.asList(selectDiasLancamento.split(","));
		List<String> listaDiasRecolhimento		 = Arrays.asList(selectDiasRecolhimento.split(","));
		
		validarDadosDiasDistribuidorFornecedor(listaFornecedoresLancamento, listaDiasLancamento, listaDiasRecolhimento);
		Distribuidor distribuidor = distribuidorService.obter();
		distribuicaoFornecedorService.gravarAtualizarDadosFornecedor(listaFornecedoresLancamento, listaDiasLancamento, listaDiasRecolhimento, distribuidor);
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Dias de Distribuição do Fornecedor cadastrado com sucesso"),"result").recursive().serialize();
	}

	private void validarAssociacaoDiasOperacao(String selectFornecedoresLancamento,
											   String selectDiasLancamento,
											   String selectDiasRecolhimento) {
		
		List<String> mensagens = new ArrayList<>();
		
		if (selectFornecedoresLancamento == null) {
			
			mensagens.add("Fornecedor(es) deve(m) ser selecionado(s)!");
		}
		
		if (selectDiasLancamento == null) {
			
			mensagens.add("Dia(s) de lançamento deve(m) ser selecionado(s)!");
		}
		
		if (selectDiasRecolhimento == null) {
			
			mensagens.add("Dia(s) de recolhimento deve(m) ser selecionado(s)!");
		}
		
		if (!mensagens.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, mensagens);
		}
	}
	
	/**
	 * Valida os dados selecionados ao inserir dados de dias de distribuição por fornecedor
	 * @param selectFornecedoresLancamento
	 * @param selectDiasLancamento
	 * @param selectDiasRecolhimento
	 */
	private void validarDadosDiasDistribuidorFornecedor(List<String> selectFornecedoresLancamento, List<String> selectDiasLancamento, List<String> selectDiasRecolhimento) {
		List<String> listaMensagemValidacao = new ArrayList<String>();

		if (selectFornecedoresLancamento == null || selectFornecedoresLancamento.isEmpty()) {
			listaMensagemValidacao.add("É necessário selecionar no mínimo um Fornecedor!");
		}

		if (selectDiasLancamento == null || selectDiasLancamento.isEmpty()) {
			listaMensagemValidacao.add("É necessário selecionar no mínimo um dia de Lançamento!");
		}

		if (selectFornecedoresLancamento == null || selectFornecedoresLancamento.isEmpty()) {
			listaMensagemValidacao.add("É necessário selecionar no mínimo um dia de Recolhimento!");
		}

		verificarExistenciaErros(listaMensagemValidacao);
	
	}
	
	/**
	 * Valida as informações obrigatórias no cadastro do distribuidor
	 * @param vo Value Object com as informações preenchidas em tela
	 */
	private void validarCadastroDistribuidor(ParametrosDistribuidorVO vo) {
	    List<String> erros = new ArrayList<String>();
	    
	    if (vo.getRazaoSocial() == null || vo.getRazaoSocial().trim().isEmpty()) {
	        erros.add("É necessário informar a Razão Social!");
	    }
	    if (vo.getCnpj() == null || vo.getCnpj().trim().isEmpty()) {
	        erros.add("É necessário informar o CNPJ!");
	    }else{
	    	this.pessoaService.validarCNPJ(vo.getCnpj());
	    }
	    
	    if (vo.getInscricaoEstadual() == null || vo.getInscricaoEstadual().trim().isEmpty()) {
	        erros.add("É necessário informar a Insc. Estadual!");
	    }
	    if (vo.getEndereco().getTipoEndereco() == null) {
	        erros.add("É necessário informar o Tipo Endereço!");
	    }
	    if (vo.getEndereco().getCep() == null || vo.getEndereco().getCep().trim().isEmpty()) {
	        erros.add("É necessário informar o CEP!");
	    }
	    if (vo.getEndereco().getUf() == null || vo.getEndereco().getUf().trim().isEmpty()) {
	        erros.add("É necessário informar o campo UF!");
	    }
	    if (vo.getEndereco().getLocalidade() == null || vo.getEndereco().getLocalidade().trim().isEmpty()) {
	        erros.add("É necessário informar a Cidade!");
	    }
	    if (vo.getEndereco().getLogradouro() == null || vo.getEndereco().getLogradouro().trim().isEmpty()) {
	        erros.add("É necessário informar o Logradouro!");
	    }
	    
	    if (vo.getNumeroDDD() == null || vo.getNumeroDDD().trim().isEmpty()) {
	        erros.add("É necessário informar o DDD do Telefone!");
	    }
	    
	    if (vo.getNumeroTelefone() == null || vo.getNumeroTelefone().trim().isEmpty()) {
	        erros.add("É necessário informar o número do Telefone!");
	    }
	    
	    if (vo.getRegimeTributario() == null) {
	        erros.add("É necessário informar o campo Regime Tributário!");
	    }
	    
	    if (vo.isPossuiRegimeEspecialDispensaInterna()) {
	    	if(vo.getNumeroDispositivoLegal() == null) {
	    		erros.add("É necessário informar o Número do Dispositivo Legal!");
	    	}
	    	
	    	if(vo.getDataLimiteVigenciaRegimeEspecial() == null) {
	    		erros.add("É necessário informar a Data Limite de Vigência do Regime Especial!");
	    	}
	    	
	    	if(vo.getTiposNotasFiscais() == null || vo.getTiposNotasFiscais().isEmpty()) {
	    		erros.add("É necessário informar os Tipos de Emissões dos Tipos de Notas ficais!");
	    	}
	    	
	    	if(vo.getTiposNotasFiscais() != null && !vo.getTiposNotasFiscais().isEmpty()) {
	    		for(TiposNotasFiscaisParametrosVO tnfp : vo.getTiposNotasFiscais()) {
	    			if(Long.parseLong(tnfp.getValor()) < 0) {
	    				erros.add("É necessário informar os Tipos de Emissões!");
	    			}
	    		}
	    	}
	    }
	    
	    if (vo.getCapacidadeManuseioHomemHoraLancamento() == null) {
	        erros.add("É necessário informar a Capacidade de Manuseio no Lançamento!");
	    }
	    if (vo.getCapacidadeManuseioHomemHoraRecolhimento() == null) {
            erros.add("É necessário informar a Capacidade de Manuseio no Recolhimento!");
        }
	    
	    if (vo.isUtilizaContratoComCotas() && vo.getPrazoContrato() == null) {
	        erros.add("É necessário informar o Prazo do Contrato!");
	    }
	    
	    if (vo.isUtilizaGarantiaPdv() && !vo.isGarantiasUtilizadas()) {
	        erros.add("É necessário selecionar pelo menos uma Garantia!");
	    }
	    
	    if (vo.isUtilizaChequeCaucao() && vo.getValidadeChequeCaucao() == null) {
	        erros.add("É necessário informar a Validade da Garantia Cheque Caução!");
	    }
	    
	    if (vo.isUtilizaCaucaoLiquida() && vo.getValidadeCaucaoLiquida() == null) {
            erros.add("É necessário informar a Validade da Garantia Caução Líquida!");
        }
	    
	    if (vo.isUtilizaFiador() && vo.getValidadeFiador() == null) {
            erros.add("É necessário informar a Validade da Garantia Fiador!");
        }
	    
	    if (vo.isUtilizaNotaPromissoria() && vo.getValidadeNotaPromissoria() == null) {
            erros.add("É necessário informar a Validade da Garantia Nota Promissória!");
        }
	    
	    if (vo.isUtilizaImovel() && vo.getValidadeImovel() == null) {
            erros.add("É necessário informar a Validade da Garantia Imóvel!");
        }
	    
	    if (vo.getValidadeAntecedenciaValidade() == null) {
            erros.add("É necessário informar a Validade da Garantia Antecedência da Validade!");
        }
	    
	    if (vo.isUtilizaOutros() && vo.getValidadeOutros() == null) {
            erros.add("É necessário informar a Validade da Garantia Outros!");
        }
	    verificarExistenciaErros(erros);
	}

    /**
     * Verifica a existência de erros e executa a tratamento apropriado
     * @param erros lista com possíveis erros ocorridos
     */
	private void verificarExistenciaErros(List<String> erros) {
        if (!erros.isEmpty()) {
            ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR, erros);
            throw new ValidacaoException(validacaoVO);
        }
    }
	
	/**
	 * Busca todos os Grupos de Cota
	 */
	@Post
	public void obterGrupos(String sortname, String sortorder) {

		List<GrupoCotaDTO> grupos = this.grupoService.obterTodosGrupos(sortname, sortorder);
		
		result.use(FlexiGridJson.class).from(grupos).page(1).total(grupos.size()).serialize();
				
	}
	
	/**
	 * Excluir Grupo
	 * @param idGrupo
	 */
	@Rules(Permissao.ROLE_ADMINISTRACAO_PARAMETROS_DISTRIBUIDOR_ALTERACAO)
	public void excluirGrupo(Long idGrupo)  {
		
		grupoService.excluirGrupo(idGrupo);
		
		result.use(Results.json()).withoutRoot().from("").recursive().serialize();	
	}
		
		
	public void obterMunicipios(Long idGrupo, Integer page, Integer rp, String sortname, String sortorder) {
		
		int total =  grupoService.obterCountQtdeCotaMunicipio();
				
		List<MunicipioDTO> municipios =	grupoService.obterQtdeCotaMunicipio(page, rp, sortname, sortorder);
		
		Set<String> selecionados = getMunicipiosSelecionados();
				
		for(MunicipioDTO municipio : municipios) {
			municipio.setSelecionado(selecionados.contains(municipio.getMunicipio()));
		}
		
		result.use(FlexiGridJson.class).from(municipios).page(page).total(total).serialize();
	}

	public void obterCotas(Long idGrupo, TipoDistribuicaoCota tipoCota, Integer page, Integer rp, String sortname, String sortorder) {
		
		int total =  grupoService.obterCountCotaPorTipo(tipoCota);
		
		session.setAttribute(TIPO_COTA, tipoCota);
		
		Set<Long> selecionados = getCotasSelecionados();
		
		List<CotaTipoDTO> cotas =	grupoService.obterCotaPorTipo(tipoCota, page, rp, sortname, sortorder);
		
		for(CotaTipoDTO cota : cotas) {
			if(selecionados.contains(cota.getIdCota()))
				cota.setSelecionado(true);
		}
		
		result.use(FlexiGridJson.class).from(cotas).page(page).total(total).serialize();		
	}
	
	@SuppressWarnings("unchecked")
	private Set<Long> getCotasSelecionados() {
		
		return session.getAttribute(COTAS_SELECIONADAS) == null ?  new HashSet<Long>()
				: (Set<Long>)session.getAttribute(COTAS_SELECIONADAS);
	}
	
	@SuppressWarnings("unchecked")
	private Set<String> getMunicipiosSelecionados() {
		
		return session.getAttribute(MUNICIPIOS_SELECIONADOS) == null ?  
					new HashSet<String>() : (Set<String>) session.getAttribute(MUNICIPIOS_SELECIONADOS);
	}
	
	/**
	 * Adiciona ou remove um item da lista de item adicionado
	 * 
	 */
	@Post
	public void selecionarCota(Long idCota, Boolean selecionado, Boolean addResult) {
		
		Set<Long> selecionados = getCotasSelecionados();
		
		if (selecionado){
			selecionados.add(idCota);
		} else {
			selecionados.remove(idCota);
		}
		
		session.setAttribute(COTAS_SELECIONADAS, selecionados);
		
		if(addResult!=null && addResult==false)
			return;
		
		result.use(Results.json()).withoutRoot().from("").recursive().serialize();
	}
	
	/**
	 * Adiciona ou remove um item da lista de item adicionado
	 * 
	 */
	@Post
	public void selecionarMunicipio(String municipio, Boolean selecionado, Boolean addResult) {
				
		Set<String> selecionados = getMunicipiosSelecionados();		
		
		if(selecionado) {
			selecionados.add(municipio);
		} else {
			selecionados.remove(municipio);
		}
		
		session.setAttribute(MUNICIPIOS_SELECIONADOS, selecionados);
			
		if(addResult!=null && addResult==false)
			return;
			
		result.use(Results.json()).withoutRoot().from("").recursive().serialize();
	}
	
	/**
	 * Adiciona ou remove todos os itens da pesquisa a lista de itens selecionados da sessão.
	 * 
	 * @param selecionado - true(adiciona todos) false (remove todos)
	 */
	@Post
	public void selecionarTodasCotas(List<Long>selecionados, Boolean selecionado){
		
		for(Long id : selecionados)
			selecionarCota(id, selecionado, false);
						
		result.use(Results.json()).withoutRoot().from("").recursive().serialize();
	}
	
	/**
	 * Adiciona ou remove todos os itens da pesquisa a lista de itens selecionados da sessão.
	 * 
	 * @param selecionado - true(adiciona todos) false (remove todos)
	 */
	@Post
	public void selecionarTodosMunicipios(List<String>selecionados, Boolean selecionado){
		
		for(String municipio : selecionados)
			selecionarMunicipio(municipio, selecionado, false);
				
		result.use(Results.json()).withoutRoot().from("").recursive().serialize();
	}
	
	@Post
	public void limparSelecoes() {
		
		session.removeAttribute(COTAS_SELECIONADAS);
		session.removeAttribute(MUNICIPIOS_SELECIONADOS);
		
		result.use(Results.json()).withoutRoot().from("").recursive().serialize();
	}
	
	@Post
	@Rules(Permissao.ROLE_ADMINISTRACAO_PARAMETROS_DISTRIBUIDOR_ALTERACAO)
	public void cadastrarOperacaoDiferenciada(String nome,List<DiaSemana> diasSemana, Long idGrupo, TipoOperacaoDiferenciada tipoOperacaoDiferenciada){
		
		if (tipoOperacaoDiferenciada.equals(TipoOperacaoDiferenciada.TIPO_COTA)) {
			
			Set<Long> cotas = this.getCotasSelecionados();
			
			if (cotas == null || cotas.isEmpty()) {
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma cota foi selecionada!");
			}
			
			TipoCaracteristicaSegmentacaoPDV tipoCota = (TipoCaracteristicaSegmentacaoPDV) session.getAttribute(TIPO_COTA);
			
			grupoService.salvarGrupoCotas(idGrupo,cotas, nome, diasSemana, tipoCota);
			
		} else {
			
			Set<String> municipios = this.getMunicipiosSelecionados();
			
			if (municipios == null || municipios.isEmpty()) {
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum município foi selecionado!");
			}
			
			grupoService.salvarGrupoMunicipios(idGrupo,municipios, nome, diasSemana);
		}
		
		result.use(Results.json()).from("").serialize();
	}
	
	@Post
	public void carregarSelecoes(Long idGrupo, TipoGrupo tipoGrupo) {

		if(tipoGrupo.equals(TipoGrupo.MUNICIPIO)) {
		
			Set<String> municipios = grupoService.obterMunicipiosDoGrupo(idGrupo);
			
			session.removeAttribute(COTAS_SELECIONADAS);
			session.setAttribute(MUNICIPIOS_SELECIONADOS, municipios);
		} else {
			
			Set<Long> ids = grupoService.obterCotasDoGrupo(idGrupo);
			
			session.removeAttribute(MUNICIPIOS_SELECIONADOS);
			session.setAttribute(COTAS_SELECIONADAS, ids);
		}
		
		result.use(Results.json()).from("").serialize();
	}
	
	@Post
	public void mensagemControleAprovacao(){
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, "Ao desmarcar a opção [Utiliza Controle de Aprovação] não serão mais exibidos os avisos de pendências das funcionalidades abaixo !"),"result").recursive().serialize();
	}
	
	private enum TipoOperacaoDiferenciada {
		
		TIPO_COTA,
		MUNICIPIO;
	}
	
}
