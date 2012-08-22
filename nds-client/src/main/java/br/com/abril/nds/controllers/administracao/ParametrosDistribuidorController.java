package br.com.abril.nds.controllers.administracao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.ParametrosDistribuidorVO;
import br.com.abril.nds.dto.GrupoCotaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ObrigacaoFiscal;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.DistribuicaoFornecedorService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.GrupoService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.util.TipoMensagem;
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
public class ParametrosDistribuidorController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private DistribuicaoFornecedorService distribuicaoFornecedorService;

	@Autowired
	private FornecedorService fornecedorService;

	@Autowired
	private ParametrosDistribuidorService parametrosDistribuidorService;
	
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
	
	@Autowired 
	private GrupoService grupoService;
	
	@Path("/")
	@Rules(Permissao.ROLE_ADMINISTRACAO_PARAMETROS_DISTRIBUIDOR)
	public void index() {
		
		this.limparLogoSessao();
		
		result.include("parametrosDistribuidor", parametrosDistribuidorService.getParametrosDistribuidor());
		result.include("listaDiaOperacaoFornecedor", distribuicaoFornecedorService.buscarDiasOperacaoFornecedor());
		result.include("fornecedores", fornecedorService.obterFornecedores());
		result.include("listaRegimeTributario", this.carregarComboRegimeTributario());
		result.include("listaObrigacaoFiscal", this.carregarComboObrigacaoFiscal());
		
		this.buscarLogoArmazenarSessao();
	}
	
	private List<ItemDTO<TipoAtividade, String>> carregarComboRegimeTributario() {

		List<ItemDTO<TipoAtividade, String>> listaRegimeTributario =
			new ArrayList<ItemDTO<TipoAtividade, String>>();
		
		listaRegimeTributario.add(
			new ItemDTO<TipoAtividade, String>(TipoAtividade.PRESTADOR_SERVICO,
											   TipoAtividade.PRESTADOR_SERVICO.getDescricao()));
		
		listaRegimeTributario.add(
			new ItemDTO<TipoAtividade, String>(TipoAtividade.MERCANTIL,
											   TipoAtividade.MERCANTIL.getDescricao()));
		
		return listaRegimeTributario;
	}
	
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
	}
	
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

	public void salvarLogo(UploadedFile logo) {
		
		this.gravarArquivoTemporario(logo.getFile());
		
		session.setAttribute(ATRIBUTO_SESSAO_LOGOTIPO_CONTENT_TYPE, logo.getContentType());
		
		result.use(PlainJSONSerialization.class).from("", "result").serialize();
	}
	
	/**buscarDiasOperacaoFornecedor
	 * Grava as alterações de parametros realizadas para o distribuidor
	 * @param distribuidor
	 */
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
	public void excluirDiasDistribuicaoFornecedor(long codigoFornecedor) {
		distribuicaoFornecedorService.excluirDadosFornecedor(codigoFornecedor);
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Dias de Distribuição do Fornecedor excluido com sucesso"),"result").recursive().serialize();
	}

	/**
	 * Retorna via json a lista de dias de distribuição e recolhimento do fornecedor
	 */
	public void recarregarDiasDistribuidorFornecedorGrid() {
		result.use(FlexiGridJson.class).from(distribuicaoFornecedorService.buscarDiasOperacaoFornecedor()).serialize();
	}
	
	/**
	 * Grava os dias de distribuição de recolhimento do fornecedor
	 * @param distribuidor
	 */
	@Post
	@Path("/gravarDiasDistribuidorFornecedor")
	public void gravarDiasDistribuidorFornecedor(String selectFornecedoresLancamento, String selectDiasLancamento, String selectDiasRecolhimento) throws Exception {
		
		List<String> listaFornecedoresLancamento = Arrays.asList(selectFornecedoresLancamento.split(","));
		List<String> listaDiasLancamento		 = Arrays.asList(selectDiasLancamento.split(","));
		List<String> listaDiasRecolhimento		 = Arrays.asList(selectDiasRecolhimento.split(","));
		
		validarDadosDiasDistribuidorFornecedor(listaFornecedoresLancamento, listaDiasLancamento, listaDiasRecolhimento);
		Distribuidor distribuidor = distribuidorService.obter();
		distribuicaoFornecedorService.gravarAtualizarDadosFornecedor(listaFornecedoresLancamento, listaDiasLancamento, listaDiasRecolhimento, distribuidor);
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Dias de Distribuição do Fornecedor cadastrado com sucesso"),"result").recursive().serialize();
	}
	
	@Post
	public void cadastrarOperacaoDiferenciada(String nomeDiferenca, List<DiaSemana> diasSemana){
		
		result.use(Results.json()).from("").serialize();
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
	    if (vo.getRegimeTributario() == null) {
	        erros.add("É necessário informar o campo Regime Tributário!");
	    }
	    if (vo.getObrigacaoFiscal() == null) {
	        erros.add("É necessário informar o campo Obrigação Fiscal!");
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
	    
	    if (vo.isUtilizaAntecedenciaValidade() && vo.getValidadeAntecedenciaValidade() == null) {
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
	public void obterGrupos() {
			
		List<GrupoCotaDTO> grupos = this.grupoService.obterTodosGrupos();
		
		result.use(FlexiGridJson.class).from(grupos).page(1).total(grupos.size()).serialize();
				
	}
	
	/**
	 * Excluir Grupo
	 * @param idGrupo
	 */
	public void excluirGrupo(Long idGrupo)  {
		
		grupoService.excluirGrupo(idGrupo);
		
		result.use(Results.json()).withoutRoot().from("").recursive().serialize();	
	}
	
	public void obterDadosGrupo(Long idGrupo) {
		//TODO
		//grupoService.excluirGrupo(idGrupo);
		
		result.use(Results.json()).withoutRoot().from("").recursive().serialize();	
	}
	
	public void obterMunicipios(Long idGrupo) {
		//TODO
		//grupoService.excluirGrupo(idGrupo);
		
		result.use(Results.json()).withoutRoot().from("").recursive().serialize();	
	}
	
	public void obterCotas(Long idGrupo) {
		//TODO
		//grupoService.excluirGrupo(idGrupo);
		
		result.use(Results.json()).withoutRoot().from("").recursive().serialize();	
	}
}
