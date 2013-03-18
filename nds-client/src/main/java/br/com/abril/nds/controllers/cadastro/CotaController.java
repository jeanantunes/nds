package br.com.abril.nds.controllers.cadastro;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.client.vo.CotaVO;
import br.com.abril.nds.client.vo.DadosCotaVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ArquivoDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaDTO.TipoPessoa;
import br.com.abril.nds.dto.DistribuicaoDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.TipoDescontoCotaDTO;
import br.com.abril.nds.dto.TipoDescontoDTO;
import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.BaseCalculo;
import br.com.abril.nds.model.cadastro.ClassificacaoEspectativaFaturamento;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.FileService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.PessoaFisicaService;
import br.com.abril.nds.service.PessoaJuridicaService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.service.integracao.ParametroSistemaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.TableModel;
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
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.validator.Message;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/cota")
public class CotaController extends BaseController {
	
	public static final String LISTA_TELEFONES_SALVAR_SESSAO = "listaTelefonesSalvarSessaoCota";
	
	public static final String LISTA_TELEFONES_REMOVER_SESSAO = "listaTelefonesRemoverSessaoCota";
	
	public static final String LISTA_TELEFONES_EXIBICAO = "listaTelefonesExibicaoCota";

	public static final String LISTA_ENDERECOS_SALVAR_SESSAO = "listaEnderecoSalvarSessaoCota";

	public static final String LISTA_ENDERECOS_REMOVER_SESSAO = "listaEnderecoRemoverSessaoCota";

	public static final String LISTA_ENDERECOS_EXIBICAO = "listaEnderecoExibicaoCota";
	
	public static final String TERMO_ADESAO = "imgTermoAdesao";
	
	public static final int[] vetor =  {1};
	
	public static final FileType[] extensoesAceitas = 
		{FileType.DOC, FileType.DOCX, FileType.BMP, FileType.GIF, FileType.PDF, FileType.JPEG, FileType.JPG, FileType.PNG};
	
	@Autowired
	private Result result;
	
	@Autowired
	private br.com.caelum.vraptor.Validator validator;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private DescontoService descontoService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private ParametroCobrancaCotaController financeiroController;
	
	@Autowired
	private PdvController pdvController;
	
	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;
	
	@Autowired
	private PessoaFisicaService pessoaFisicaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private ParametroSistemaService parametroSistemaService; 

	@Autowired
	private FileService fileService;
	
	private static final String FILTRO_SESSION_ATTRIBUTE="filtroCadastroCota";

	private static final String NOME_DEFAULT_TERMO_ADESAO = "termo_adesao.pdf";

	private static final String NOME_DEFAULT_PROCURACAO = "procuracao.pdf";

	@Path("/")
	@Rules(Permissao.ROLE_CADASTRO_COTA)
	public void index() {
		
		this.financeiroController.preCarregamento();
		this.pdvController.preCarregamento();
		this.limparDadosSession();
	}
	
	private boolean isNotEnderecoPendente(){
		
        Boolean enderecoPendente = (Boolean) this.session.getAttribute(EnderecoController.ENDERECO_PENDENTE);
		
		return (enderecoPendente==null || !enderecoPendente);
	}
	
	/**
	 * Obtem e seta os dados de endereço e telefone na sessão.
	 * 
	 * @param idCota - identificador da cota
	 */
	private void carregarDadosEnderecoETelefone(Long idCota) { 
		
		limparDadosSession();
		
		if (idCota == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Ocorreu um erro: Cota inexistente.");
		}

		obterEndereco(idCota);
		obterTelefones(idCota);
	}
	
	public void historicoTitularidade(Long idCota, Long idHistorico) {
	    CotaDTO cotaDTO = cotaService.obterHistoricoTitularidade(idCota, idHistorico);
	    carregarEnderecosHistoricoTitularidade(idCota, idHistorico);
	    carregarTelefonesHistoricoTitularidade(idCota, idHistorico);
	    result.use(Results.json()).from(cotaDTO, "result").recursive().serialize();
	}
	
	public void verificarTipoConvencional(Long idCota) {
		
		boolean isTipoConvencional = cotaService.isTipoCaracteristicaSegmentacaoConvencional(idCota);
		
		result.use(Results.json()).from(isTipoConvencional, "result").recursive().serialize();
	}

    private void carregarEnderecosHistoricoTitularidade(Long idCota, Long idHistorico) {
        List<EnderecoAssociacaoDTO> enderecos = cotaService.obterEnderecosHistoricoTitularidade(idCota, idHistorico);
	    session.setAttribute(LISTA_ENDERECOS_EXIBICAO, enderecos);
    }
    
    private void carregarTelefonesHistoricoTitularidade(Long idCota, Long idHistorico) {
        List<TelefoneAssociacaoDTO> telefones = cotaService.obterTelefonesHistoricoTitularidade(idCota, idHistorico);
        session.setAttribute(LISTA_TELEFONES_EXIBICAO, telefones);
    }

	/**
	 * Processa os dados de endereço, obtem os dados da sessão, grava os dados no banco de dados e atualiza os dados na sessão
	 * 
	 * @param idCota - identificador da cota
	 */
    @SuppressWarnings("unchecked")
	private boolean processarEnderecosCota(Long idCota) {

		List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar = 
				(List<EnderecoAssociacaoDTO>) this.session.getAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
		
		List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoRemover = 
				(List<EnderecoAssociacaoDTO>) this.session.getAttribute(LISTA_ENDERECOS_REMOVER_SESSAO);
		
		if ((listaEnderecoAssociacaoSalvar == null || listaEnderecoAssociacaoSalvar.size()<=0)&&(listaEnderecoAssociacaoRemover == null || listaEnderecoAssociacaoRemover.size()<=0)){
			
			return false;
		}
		
		this.cotaService.processarEnderecos(idCota, listaEnderecoAssociacaoSalvar, listaEnderecoAssociacaoRemover);
		
		this.session.setAttribute(EnderecoController.ENDERECO_PENDENTE, Boolean.FALSE);
		this.session.removeAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
		this.session.removeAttribute(LISTA_ENDERECOS_REMOVER_SESSAO);
		
		obterEndereco(idCota);
		
		return true;
	}

	/**
	 * Processa os dados de telefone, obtem os dados da sessão, grava os dados no banco de dados e atualiza os dados na sessão
	 * 
	 * @param idCota - identificador da cota
	 */
	private boolean processarTelefonesCota(Long idCota){
		
		Map<Integer, TelefoneAssociacaoDTO> map = this.obterTelefonesSalvarSessao();
		
		List<TelefoneAssociacaoDTO> lista = new ArrayList<TelefoneAssociacaoDTO>();
		for (Integer key : map.keySet()){
			TelefoneAssociacaoDTO telefoneAssociacaoDTO = map.get(key);
			
			if(telefoneAssociacaoDTO.getTipoTelefone()!= null){
				lista.add(telefoneAssociacaoDTO);
			}
		}
		
		Set<Long> telefonesRemover = this.obterTelefonesRemoverSessao();
		
		if ((map == null || map.size() <= 0)&&(telefonesRemover == null || telefonesRemover.size() <=0 )){
			
			return false;
		}
		
		this.cotaService.processarTelefones(idCota, lista, telefonesRemover);
		
		this.session.removeAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		this.session.removeAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
		
		obterTelefones(idCota);
		
		return true;
	}
	
	/**
	 * Obtem os dados de telefone da sessão para atuaização e inclusão
	 * 
	 * @return Map<Integer, TelefoneAssociacaoDTO> 
	 */
	@SuppressWarnings("unchecked")
	private Map<Integer, TelefoneAssociacaoDTO> obterTelefonesSalvarSessao(){
		Map<Integer, TelefoneAssociacaoDTO> telefonesSessao = (Map<Integer, TelefoneAssociacaoDTO>) 
				this.session.getAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		
		if (telefonesSessao == null){
			telefonesSessao = new LinkedHashMap<Integer, TelefoneAssociacaoDTO>();
		}
		
		return telefonesSessao;
	}

	/**
	 * 
	 * Obtem os dados de telefone da sessão para serem removidos
	 * 
	 * @return Set<Long> 
	 */
	@SuppressWarnings("unchecked")
	private Set<Long> obterTelefonesRemoverSessao(){
		Set<Long> telefonesSessao = (Set<Long>) 
				this.session.getAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
		
		if (telefonesSessao == null){
			telefonesSessao = new HashSet<Long>();
		}
		
		return telefonesSessao;
	}
	
	/**
	 * Efetua consulta de cota pelo número informado
	 * 
	 * @param numeroCota - número da cota
	 */
	@Post
	public void pesquisarPorNumero(Integer numeroCota) {
		
		if(numeroCota == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Número da cota deve ser informado!");
		}
		
		Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);

		if (cota == null) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Cota \"" + numeroCota + "\" não encontrada!");
			
		} else {
			
			String nomeExibicao = PessoaUtil.obterNomeExibicaoPeloTipo(cota.getPessoa());
			
			CotaVO cotaVO = new CotaVO(cota.getNumeroCota(), nomeExibicao);
			
			if (cota.getBox() != null) {
			
				cotaVO.setCodigoBox(cota.getBox().getCodigo() + " - "+cota.getBox().getNome());
			}
			
			cotaVO.setSituacaoCadastro(cota.getSituacaoCadastro());
			if (cota.getSituacaoCadastro() != null) {

				cotaVO.setStatus(cota.getSituacaoCadastro().toString());

			}

			this.result.use(Results.json()).from(cotaVO, "result").recursive().serialize();
		}		
	}

	/**
	 * Efetua consulta pelo nome da cota informado, utilizado para auto complete da tela
	 * 
	 * @param nomeCota - nome da cota
	 */
	@Post
	public void autoCompletarPorNome(String nomeCota) {
		
		nomeCota = PessoaUtil.removerSufixoDeTipo(nomeCota);
		
		List<Cota> listaCotas = this.cotaService.obterCotasPorNomePessoa(nomeCota);
		
		List<ItemAutoComplete> listaCotasAutoComplete = new ArrayList<ItemAutoComplete>();
		
		if (listaCotas != null && !listaCotas.isEmpty()) {
			
			for (Cota cota : listaCotas) {
				
				String nomeExibicao = PessoaUtil.obterNomeExibicaoPeloTipo(cota.getPessoa());
					
				CotaVO cotaVO = new CotaVO(cota.getNumeroCota(), nomeExibicao);
				
				if (cota.getSituacaoCadastro() != null) {
					cotaVO.setStatus(cota.getSituacaoCadastro().toString());
				}
	
				listaCotasAutoComplete.add(new ItemAutoComplete(nomeExibicao, null, cotaVO));
			}
		}
		
		this.result.use(Results.json()).from(listaCotasAutoComplete, "result").include("value", "chave").serialize();
	}

	/**
	 * Auto complete para numeroCota. Casos de pesquisa por nome onde existem cotas com nomes iguais.
	 * @param cotasVO
	 * @return List<ItemAutoComplete>
	 */
	private List<ItemAutoComplete> getAutocompleteNumeroCota(List<CotaVO> cotasVO){
		
		List<ItemAutoComplete> listaCotasAutoComplete = new ArrayList<ItemAutoComplete>();
		
		for (CotaVO cotaVO : cotasVO){
			
			String numeroExibicao = cotaVO.getNumero().toString();

			listaCotasAutoComplete.add(new ItemAutoComplete(numeroExibicao, null, cotaVO));
		}
		
		return listaCotasAutoComplete;
	}
	
	/**
	 * Efetua consulta pelo nome da cota informado
	 * 
	 * @param nomeCota - nome da cota
	 */
	@Post
	public void pesquisarPorNome(String nomeCota) {
		
		nomeCota = PessoaUtil.removerSufixoDeTipo(nomeCota);
		
		List<Cota> cotas = this.cotaService.obterPorNome(nomeCota);
		
		if (cotas == null) {
		
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota \"" + nomeCota + "\" não encontrada!");
		}
		
		List<CotaVO> cotasVO = new ArrayList<CotaVO>();
		
		for (Cota cota : cotas){
		
		    String nomeExibicao = PessoaUtil.obterNomeExibicaoPeloTipo(cota.getPessoa());
		    
		    CotaVO cotaVO = new CotaVO(cota.getNumeroCota(), nomeExibicao);
		    if (cota.getSituacaoCadastro() != null) {
		    	cotaVO.setStatus(cota.getSituacaoCadastro().toString());	
			}
		    
		    cotasVO.add(cotaVO);
		}
		
		if (cotasVO.size() > 1){
			
			this.result.use(Results.json()).from(this.getAutocompleteNumeroCota(cotasVO), "result").include("value", "chave").serialize();
		}
		else{
		
		    this.result.use(Results.json()).from(cotasVO.get(0), "result").recursive().serialize();
		}
	}
	
	/**
	 * Limpa os dados da sessão após o usúario fechar ou cancelar a operação de cadastro ou edição de uma cota
	 */
	@Post
	public void cancelar(){
		
		limparDadosSession();
		
		this.result.use(Results.json()).from("", "result").serialize();
	}
	
	/**
	 * Retorna os dados de uma pessoa/cota referente o número do CNPJ informado
	 * 
	 * @param numeroCnpj - número CNPJ
	 */
	@Post
	@Path("/obterDadosCNPJ")
	public void obterDadosCNPJ(String numeroCnpj){
		
		if(numeroCnpj!= null){
			
			numeroCnpj = numeroCnpj.replace(".", "").replace("-", "").replace("/", "");
			
			PessoaJuridica pessoaJ = pessoaJuridicaService.buscarPorCnpj(numeroCnpj.trim());
			
			CotaDTO cotaDTO = new CotaDTO();
			
			if(pessoaJ!= null){
				cotaDTO.setInscricaoEstadual(pessoaJ.getInscricaoEstadual());
				cotaDTO.setInscricaoMunicipal(pessoaJ.getInscricaoMunicipal());
				cotaDTO.setEmail(pessoaJ.getEmail());
				cotaDTO.setNomeFantasia(pessoaJ.getNomeFantasia());
				cotaDTO.setRazaoSocial(pessoaJ.getRazaoSocial());	
			}
			
			result.use(Results.json()).from(cotaDTO, "result").recursive().serialize();
		}
		else{
			result.use(Results.json()).from("", "result").recursive().serialize();
		}

	}
	
	/**
	 * Retorna os dados de uma pessoa/cota referente o número do CPF informado
	 * 
	 * @param numeroCPF - número CPF
	 */
	@Post
	@Path("/obterDadosCPF")
	public void obterDadosCPF(String numeroCPF){
		
		if(numeroCPF!= null){
			
			numeroCPF = numeroCPF.replace(".", "").replace("-", "");
			
			PessoaFisica pessoaF = pessoaFisicaService.buscarPorCpf(numeroCPF.trim());
			
			CotaDTO cotaDTO = new CotaDTO();
			
			if(pessoaF!= null){
				
				cotaDTO.setNomePessoa(pessoaF.getNome());
				cotaDTO.setEmail(pessoaF.getEmail());
				cotaDTO.setNumeroRG(pessoaF.getRg());
				cotaDTO.setDataNascimento(pessoaF.getDataNascimento());
				cotaDTO.setOrgaoEmissor(pessoaF.getOrgaoEmissor());
				cotaDTO.setEstadoSelecionado(pessoaF.getUfOrgaoEmissor());
				cotaDTO.setEstadoCivilSelecionado(pessoaF.getEstadoCivil());
				cotaDTO.setSexoSelecionado(pessoaF.getSexo());
				cotaDTO.setNacionalidade(pessoaF.getNacionalidade());
				cotaDTO.setNatural(pessoaF.getNatural());
			}
			
			result.use(Results.json()).from(cotaDTO, "result").recursive().serialize();
		}
		else{
			result.use(Results.json()).from("", "result").recursive().serialize();
		}

	}

	/**
	 * Retorna os dados default para inclusão de uma nova cota.
	 * 
	 * @return DadosCotaVO
	 */
	private DadosCotaVO getDadosInclusaoCota(){
		
		limparDadosSession();
		
		DadosCotaVO dadosCotaVO = new DadosCotaVO();
	
		dadosCotaVO.setNumeroSugestaoCota(cotaService.gerarNumeroSugestaoCota());	
		dadosCotaVO.setDataInicioAtividade(DateUtil.formatarDataPTBR(new Date()));
		dadosCotaVO.setStatus(SituacaoCadastro.PENDENTE.toString());
		dadosCotaVO.setListaClassificacao(getListaClassificacao());
		
		return dadosCotaVO;
	}

	/**
	 * Prepara os dados default para inclusão de uma nova cota para CNPJ
	 */
	@Post
	@Path("/incluirNovoCNPJ")
	public void prepararDadosInclusaoCotaCNPJ(){
		
		result.use(Results.json()).from(getDadosInclusaoCota(), "result").recursive().serialize();
	}
	
	/**
	 * Prepara os dados default para inclusão de uma nova cota para CPF
	 */
	@Post
	@Path("/incluirNovoCPF")
	public void prepararDadosInclusaoCota(){
		
		result.use(Results.json()).from(getDadosInclusaoCota(), "result").recursive().serialize();
	}

	/**
	 * Retorna uma lista de classificação de espectativa de faturamento da cota
	 * 
	 * @return List<ItemDTO<String, String>>
	 */
	private List<ItemDTO<String, String>> getListaClassificacao(){
		
		List<ItemDTO<String, String>> listaClassificacao = new ArrayList<ItemDTO<String,String>>();
		
		for(ClassificacaoEspectativaFaturamento clazz : ClassificacaoEspectativaFaturamento.values()){
			
			listaClassificacao.add(new ItemDTO<String, String>(clazz.toString(), clazz.getDescricao()));
		}
		
		Collections.reverse(listaClassificacao);
		
		return listaClassificacao;
	}
	
	/**
	 * Salva os dados de uma cota do tipo CNPJ
	 * 
	 * @param cotaDTO
	 */
	@Post
	@Path("/salvarCotaCNPJ")
	public void salvarCotaPessoaJuridica(CotaDTO cotaDTO){

		cotaDTO.setTipoPessoa(TipoPessoa.JURIDICA);

		cotaDTO = salvarDadosCota(cotaDTO);
		
		cotaDTO.setNumeroCnpj(Util.adicionarMascaraCNPJ(cotaDTO.getNumeroCnpj()));
		
		carregarDadosEnderecoETelefone(cotaDTO.getIdCota());
		
		result.use(Results.json()).from(cotaDTO, "result").recursive().serialize();
	}
	
	private void validar() {
		
		List<String> mensagensValidacao = new ArrayList<String>();

		validarEnderecos(mensagensValidacao);
		
		validarTelefones(mensagensValidacao);
			
		validarFormatoData(mensagensValidacao);

	}
	
	private void validarEnderecos(List<String> mensagensValidacao) {
		
		@SuppressWarnings("unchecked")
		List<EnderecoAssociacaoDTO> listaEnderecosSalvar = (List<EnderecoAssociacaoDTO>) this.session.getAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
		
		@SuppressWarnings("unchecked")
		List<EnderecoAssociacaoDTO> listaEnderecosExibir = (List<EnderecoAssociacaoDTO>) this.session.getAttribute(LISTA_ENDERECOS_EXIBICAO);

		List<EnderecoAssociacaoDTO> listaEnderecos = new ArrayList<EnderecoAssociacaoDTO>();
		if (listaEnderecosSalvar != null && !listaEnderecosSalvar.isEmpty()) {
			listaEnderecos.addAll(listaEnderecosSalvar);
		}
		
		if (listaEnderecosExibir != null && !listaEnderecosExibir.isEmpty()) {
			listaEnderecos.addAll(listaEnderecosExibir);
		}
		
		if (listaEnderecos.isEmpty()) {
			mensagensValidacao.add("Pelo menos um endereço deve ser cadastrado para a cota.");
		} else {
			boolean temPrincipal = false;
			for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecos) {
				if (enderecoAssociacao.isEnderecoPrincipal()) {
					temPrincipal = true;
					break;
				}
			}
			if (!temPrincipal) {
				mensagensValidacao.add("Deve haver ao menos um endereço principal para a cota.");
			}
		}
	}

	private void validarTelefones(List<String> mensagensValidacao) {
		@SuppressWarnings("unchecked")
		Map<Integer, TelefoneAssociacaoDTO> mapaTelefones = (Map<Integer, TelefoneAssociacaoDTO>) this.session.getAttribute(LISTA_TELEFONES_SALVAR_SESSAO);

		@SuppressWarnings("unchecked")
		List<TelefoneAssociacaoDTO> telefonesExibicao = (List<TelefoneAssociacaoDTO>) this.session.getAttribute(LISTA_TELEFONES_EXIBICAO);
		
		List<TelefoneAssociacaoDTO> listaTelefones = new ArrayList<TelefoneAssociacaoDTO>();
		
		if (mapaTelefones != null) {
			listaTelefones.addAll(mapaTelefones.values());
		}
		
		if (telefonesExibicao != null && telefonesExibicao.size() > 0){
			listaTelefones.addAll(telefonesExibicao);
		}
		
 		if (listaTelefones == null || listaTelefones.isEmpty()) {
			mensagensValidacao.add("Pelo menos um telefone deve ser cadastrado para a cota.");
		} else {
			boolean temPrincipal = false;
			
			for (TelefoneAssociacaoDTO telefoneAssociacao : listaTelefones){

				if (telefoneAssociacao.isPrincipal()) {
					
					temPrincipal = true;
					
					break;
				}
			}
			
			if (!temPrincipal) {
				mensagensValidacao.add("Deve haver ao menos um telefone principal para a cota.");
			}
		}
	}
	
	/**
	 * Salva os dados de uma cota do tipo CPF
	 * 
	 * @param cotaDTO
	 */
	@Post
	@Path("/salvarCotaCPF")
	public void salvarCotaPessoaFisica(CotaDTO cotaDTO){
		
		cotaDTO.setTipoPessoa(TipoPessoa.FISICA);
		
		cotaDTO = salvarDadosCota(cotaDTO);
		
		cotaDTO.setNumeroCPF(Util.adicionarMascaraCPF(cotaDTO.getNumeroCPF()));
		
		carregarDadosEnderecoETelefone(cotaDTO.getIdCota());
		
		result.use(Results.json()).from(cotaDTO, "result").recursive().serialize();
	}
	
	/**
	 * 
	 * Salva os dados de uma cota e retorna os dados da cota atualizados.
	 * 
	 * @param cotaDTO
	 * 
	 * @return CotaDTO
	 */
	private CotaDTO salvarDadosCota(CotaDTO cotaDTO){
		
		if(cotaDTO.getDataInclusao() == null){
			cotaDTO.setDataInclusao(new Date());
		}

		if (cotaDTO.isAlteracaoTitularidade()) {

			cotaDTO = this.cotaService.criarCotaTitularidade(cotaDTO);

			return cotaDTO;
		}
		
		Long idCota = cotaService.salvarCota(cotaDTO);
						
		return cotaDTO = cotaService.obterDadosCadastraisCota(idCota);
	}
	
	/**
	 * Valida o formato das datas utilizadas na tela de cadastro de cota
	 */
	private void validarFormatoData(List<String> mensagensValidacao){

		if (validator.hasErrors()) {
			
			for (Message message : validator.getErrors()) {
				
				if (message.getCategory().equals("dataNascimento")){
					mensagensValidacao.add("O campo [Data Nascimento] está inválido");
				}
				
				if(message.getCategory().equals("inicioPeriodo")){
					mensagensValidacao.add("O campo [Périodo] está inválido");
				}
				
				if(message.getCategory().equals("fimPeriodo")){
					mensagensValidacao.add("O campo [Até] está inválido");
				}
			}
			
		}

		if (!mensagensValidacao.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagensValidacao));
		}
	
	}
	
	/**
	 *
	 *Prepara os dados de uma cota para edição.
	 * 
	 * @param idCota
	 */
	@Post
	@Path("/editar")
	public void editar(Long idCota){
		
		carregarDadosEnderecoETelefone(idCota);
		
		CotaDTO cotaDTO = cotaService.obterDadosCadastraisCota(idCota);
		cotaDTO.setListaClassificacao(getListaClassificacao());
	
		result.use(Results.json()).from(cotaDTO, "result").recursive().serialize();
	}
	
	/**
	 * Exclui uma cota, informada pelo usúario
	 * 
	 * @param idCota - identificador da cota
	 */
	@Post
	@Path("/excluir")
	public void excluir(Long idCota){
		
		cotaService.excluirCota(idCota); 
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	/**
	 * Salva os dados dos fornecedores, associa os fornecedores a cota informada.
	 * 
	 * @param fornecedores  - fornecedores selecionados
	 * @param idCota - identificador da cota
	 */
	@Post
	@Path("/salvarFornecedores")
	public void salvarFornecedores(List<Long> fornecedores, Long idCota){
		
		fornecedorService.salvarFornecedorCota(fornecedores, idCota);

		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	/**
	 * Salva os endereços da cota informada
	 * 
	 * @param idCota - identificador da cota
	 */
	@Post
	@Path("/salvarEnderecos")
	public void salvarEnderecos(Long idCota){
	
		validar();
		
		boolean alteracoesEndereco = processarEnderecosCota(idCota);
		
		boolean alteracoesTelefone = processarTelefonesCota(idCota);
		
		if (alteracoesEndereco || alteracoesTelefone){
	
		    result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
			    	Constantes.PARAM_MSGS).recursive().serialize();
		}
		else{
			
			result.nothing();
		}
	}
	
	/**
	 * Salva os telefones da cota informada
	 * 
	 * @param idCota - identificador da cota
	 */
	@Post
	@Path("/salvarTelefones")
	public void salvarTelefones(Long idCota){
		
		validar();
		
		boolean alteracoesTelefone = processarTelefonesCota(idCota);
		
		boolean alteracoesEndereco = processarEnderecosCota(idCota);
		
		if (alteracoesEndereco || alteracoesTelefone){
			
		    result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
			    	Constantes.PARAM_MSGS).recursive().serialize();
		}
		else{
			
			result.nothing();
		}
	}
	
	/**
	 * Obtem os fornecedores que não possui associação com a cota informada
	 * 
	 * @param idCota -identificador da cota
	 */
	@Post
	@Path("/obterFornecedores")
	public void obterFornecedores(Long idCota, ModoTela modoTela){
	    List<ItemDTO<Long, String>> dtos = new ArrayList<ItemDTO<Long,String>>();
	    if (ModoTela.CADASTRO_COTA == modoTela) {
	        List<Fornecedor> fornecedores = fornecedorService.obterFornecedores(idCota);
	        dtos = getFornecedores(fornecedores);
	    } 
        result.use(Results.json()).from(dtos,"result").recursive().serialize();
	}
	
	/**
	 * Obtem os fornecedores associados a cota informada
	 * 
	 * @param idCota -identificador da cota
	 */
	@Post
	@Path("/obterFornecedoresSelecionados")
	public void obterFornecedoresSelecionados(Long idCota, ModoTela modoTela, Long idHistorico){
	    List<ItemDTO<Long, String>> dtos = new ArrayList<ItemDTO<Long,String>>();
	    if (ModoTela.CADASTRO_COTA ==  modoTela) {
	        List<Fornecedor> fornecedores = fornecedorService.obterFornecedoresCota(idCota);
	        dtos = getFornecedores(fornecedores);
	    } else {
	        List<FornecedorDTO> fornecedores = cotaService.obterFornecedoresHistoricoTitularidadeCota(idCota, idHistorico);
	        for (FornecedorDTO dto : fornecedores) {
	            dtos.add(new ItemDTO<Long, String>(dto.getIdFornecedor(), dto.getRazaoSocial()));
	        }
	    }
		result.use(Results.json()).from(dtos,"result").recursive().serialize();
	}
	
	/**
	 * Retorna uma lista de fornecedores para exibição na tela
	 * 
	 * @param fornecedores - lista de fornecedores
	 * 
	 * @return List<ItemDTO<Long, String>>
	 */
	private List<ItemDTO<Long, String>> getFornecedores(List<Fornecedor> fornecedores){
		
		List<ItemDTO<Long, String>> itensFornecedor = new ArrayList<ItemDTO<Long,String>>();
		
		for(Fornecedor fornecedor : fornecedores){
			
			itensFornecedor.add(new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial()));
		}
		
		return itensFornecedor;
	}
	
	
	/**
	 * Descontos da cota: Obtem os tipos de desconto por produto relacionados à cota
	 * @param idCota
	 */
	private List<TipoDescontoProdutoDTO> obterDescontosProduto(Cota cota, String sortorder, String sortname){

		List<TipoDescontoProdutoDTO> descontos = descontoService.obterTiposDescontoProdutoPorCota(cota.getId(), sortorder, sortname);
		
		return descontos;
	}

	@Post
	@Path("/obterTiposDescontoProduto")
	public void obterTiposDescontoProduto(Long idCota, ModoTela modoTela, Long idHistorico, String sortorder, String sortname){
	    Cota cota = cotaService.obterPorId(idCota);
	    List<TipoDescontoProdutoDTO> descontosProduto = null;
		if (ModoTela.CADASTRO_COTA == modoTela) {
		    descontosProduto = obterDescontosProduto(cota, sortorder, sortname);
		} else {
	        Ordenacao ordenacao = Util.getEnumByStringValue(Ordenacao.values(), sortorder);
            descontosProduto = new ArrayList<TipoDescontoProdutoDTO>(
                    PaginacaoUtil.ordenarEmMemoria(cotaService
                            .obterDescontosProdutoHistoricoTitularidadeCota(
                                    idCota, idHistorico), ordenacao, sortname));
		}

	    result.use(FlexiGridJson.class).from(descontosProduto).page(1).total(1).serialize();
	}
	
	@Post
	@Path("/obterTiposDescontoCota")
	public void obterTiposDescontoCota(Long idCota, ModoTela modoTela, Long idHistorico, String sortorder, String sortname) {
		
	    Cota cota = cotaService.obterPorId(idCota);
		
	    if (ModoTela.CADASTRO_COTA == modoTela) {
	        if (cota!=null){
				
	            List<TipoDescontoDTO> descontos =  descontoService.obterMergeDescontosEspecificosEGerais(cota,sortorder, sortname);
			
	            if (descontos!=null && descontos.size() > 0){
			
	                result.use(FlexiGridJson.class).from(descontos).page(1).total(1).serialize();
			
	            } else {
	            	result.nothing();
	            }
			
	        } else {

	            result.nothing();

	        }
		
	    } else {
	        Ordenacao ordenacao = Util.getEnumByStringValue(Ordenacao.values(), sortorder);
            List<TipoDescontoCotaDTO> descontosCota = new ArrayList<TipoDescontoCotaDTO>(PaginacaoUtil
                    .ordenarEmMemoria(cotaService
                            .obterDescontosCotaHistoricoTitularidadeCota(
                                    idCota, idHistorico), ordenacao, sortname));
            
            result.use(FlexiGridJson.class).from(descontosCota).page(1).total(1).serialize();
	    }
	}
	
	List<Long> obterIdFornecedoresCota(Long idCota) {
		
		List<Fornecedor> fornecedores = cotaService.obterFornecedoresCota(idCota);
		
		List<Long> idFornecedores = null;
		
		if(fornecedores!=null && !fornecedores.isEmpty()) {
			
			idFornecedores = new ArrayList<Long>();
			
			for(Fornecedor fornecedor : fornecedores) {
				
				idFornecedores.add(fornecedor.getId());
				
			}
			
		}
		
		return idFornecedores;
		
	}

	/**
	 * Valida se o número da cota informada para histórico base é ativo.
	 * 
	 * @param numeroCota - número da cota
	 */
	@Post
	@Path("/validarNumeroCotaHistoricoBase")
	public void validarNumeroCotaHistoricoBase(Integer numeroCota){
		
		if(numeroCota != null) {
			
			Cota cota = this.cotaService.obterPorNumeroDaCotaAtiva(numeroCota);

			if (cota == null) {

				throw new ValidacaoException(TipoMensagem.WARNING, "Cota \"" + numeroCota + "\" não encontrada!");
			} 
		}
					
		this.result.use(Results.json()).from("", "result").recursive().serialize();
	}
	
	/**
	 * Efetua a pesquisa das cotas cadastradas no sistema, possibiltando a exclusão, visualização e edição.
	 * 
	 * @param numCota - número da cota
	 * @param nomeCota - nome da cota
	 * @param numeroCpfCnpj - número do CNPJ ou CPF
	 * @param sortorder  - tipo de oredenação
	 * @param sortname - campo a ser ordenado
	 * @param page - página atual
	 * @param rp - quantidade de páginas visualizadas
	 */
	@Post
	@Path("/pesquisarCotas")
	public void pesquisarCotas(BigInteger numCota,String nomeCota,String numeroCpfCnpj, String sortorder, 
							   String logradouro, String bairro, String municipio,String status,
			 				   String sortname, int page, int rp){
		
		if (numeroCpfCnpj != null) {
			numeroCpfCnpj = numeroCpfCnpj.replace(".", "").replace("-", "").replace("/", "");
		}
		
		nomeCota = PessoaUtil.removerSufixoDeTipo(nomeCota);
		
		Integer numeroCota = (numCota!= null)?numCota.intValue():null;
		    
		FiltroCotaDTO filtro = new FiltroCotaDTO(numeroCota ,nomeCota,numeroCpfCnpj, logradouro, bairro, municipio, status);
		
		configurarPaginacaoPesquisa(filtro, sortorder, sortname, page, rp);
		
		tratarFiltro(filtro);
		
		efetuarConsulta(filtro);
	}
	
	@Get
	public void exportar(FileType fileType) throws IOException {
		
		FiltroCotaDTO filtro = (FiltroCotaDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
				
		filtro.getPaginacao().setQtdResultadosPorPagina(null);
		
		List<CotaDTO> listaCotas = null;
		
		if (filtro != null){
			
			listaCotas = cotaService.obterCotas(filtro);
		}
		
		if (listaCotas == null || listaCotas.isEmpty()){
			
			listaCotas = new ArrayList<CotaDTO>();
		}
		
		List<CotaVO> listaCotasVO = getListaCotaVO(listaCotas);
		
		FileExporter.to("cotas", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
				listaCotasVO, CotaVO.class, this.httpResponse);
		
		result.nothing();
	}
	
	/**
	 * Efetua a consulta e monta a estrutura do grid de dividas geradas.
	 * @param filtro
	 */
	private void efetuarConsulta(FiltroCotaDTO filtro) {
		
		List<CotaDTO> listaCotas = cotaService.obterCotas(filtro);
		
		if (listaCotas == null || listaCotas.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		Long totalRegistros = cotaService.obterQuantidadeCotasPesquisadas(filtro);
		
		List<CotaVO> listaCotasVO = getListaCotaVO(listaCotas);

		TableModel<CellModelKeyValue<CotaVO>> tableModel = new TableModel<CellModelKeyValue<CotaVO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaCotasVO));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal( (totalRegistros == null)?0:totalRegistros.intValue());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	/**
	 * Retorna uma lista de cotaVO para renderização das informaçoes do grid na tela
	 * 
	 * @param listaCotas - lista de cotas
	 * 
	 * @return List<CotaVO> 
	 */
	private List<CotaVO> getListaCotaVO(List<CotaDTO> listaCotas) {
		
		List<CotaVO> listaRetorno  =  new ArrayList<CotaVO>();
		CotaVO cotaVO = null;
		
		for(CotaDTO dto : listaCotas){
			
			cotaVO = new CotaVO();
			cotaVO.setIdCota(dto.getIdCota());
			cotaVO.setNumero(dto.getNumeroCota());
			cotaVO.setNome(dto.getNomePessoa());
			cotaVO.setContato( tratarValor( dto.getContato() ));
			cotaVO.setEmail(tratarValor( dto.getEmail()));
			cotaVO.setNumeroCpfCnpj( formatarNumeroCPFCNPJ(dto.getNumeroCpfCnpj()));
			cotaVO.setStatus( tratarValor(dto.getStatus()));
			cotaVO.setTelefone( tratarValor( dto.getTelefone()));
			cotaVO.setDescricaoBox(tratarValor(dto.getDescricaoBox()));
			
			listaRetorno.add(cotaVO);
		}
		
		return listaRetorno;
	}
	
	/**
	 * Retorna o número do CPF ou CNPJ com mascara.
	 * 
	 * @param numeroCpfCnpj - número do CPF ou CNPJ
	 * 
	 * @return String
	 */
	private String formatarNumeroCPFCNPJ(String numeroCpfCnpj){
		
		if(numeroCpfCnpj!= null && !numeroCpfCnpj.isEmpty()){
			
			if(numeroCpfCnpj.length() > 11){

				return Util.adicionarMascaraCNPJ(numeroCpfCnpj);
			}
			else{
				
				return Util.adicionarMascaraCPF(numeroCpfCnpj);
			}
		}
		return "";
	} 
	
	/**
	 * Retorna um objeto com valor vazio caso seja nulo 
	 * 
	 * @param valor - valor a ser tratado
	 * 
	 * @return String
	 */
	private String tratarValor(Object valor){
		
		return (valor == null)?"":valor.toString();
	}
	
	/**
	 * Configura paginação do grid de pesquisa.
	 * @param filtro
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	private void configurarPaginacaoPesquisa(FiltroCotaDTO filtro,String sortorder,String sortname,int page, int rp) {

		if (filtro != null) {
		
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			
			filtro.setPaginacao(paginacao);
			
			filtro.setOrdemColuna(Util.getEnumByStringValue(FiltroCotaDTO.OrdemColuna.values(),sortname));
		}
	}
	
	/**
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
	 * 
	 * @param filtroResumoExpedicao
	 */
	private void tratarFiltro(FiltroCotaDTO filtro) {

		FiltroCotaDTO filtroSession = (FiltroCotaDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtro)) {

			filtro.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	}
	
	/**
	 * Carrega dados de Distribuição da cota
	 * 
	 * @param idCota - Código da cota
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@Post
	public void carregarDistribuicaoCota(Long idCota, ModoTela modoTela, Long idHistorico) throws FileNotFoundException, IOException {
	    DistribuicaoDTO dto = null;
	    if (ModoTela.CADASTRO_COTA == modoTela) {
		    dto = cotaService.obterDadosDistribuicaoCota(idCota);
		    
		    dto.setTiposEntrega(this.gerarTiposEntrega());
		    
		    dto.setBasesCalculo(this.gerarBasesCalculo());
		    
		    this.tratarDocumentoTipoEntrega(dto);
		} else {
		    dto = cotaService.obterDistribuicaoHistoricoTitularidade(idCota, idHistorico);
		}
				
		this.result.use(Results.json()).from(dto, "result").recursive().serialize();
	}	
	
	private void tratarDocumentoTipoEntrega(DistribuicaoDTO dto) throws FileNotFoundException, IOException {

		TipoParametroSistema parametroPath = null;
		
		if(DescricaoTipoEntrega.ENTREGA_EM_BANCA.equals(dto.getDescricaoTipoEntrega())) {
			parametroPath = TipoParametroSistema.PATH_TERMO_ADESAO;
		} else if(DescricaoTipoEntrega.ENTREGADOR.equals(dto.getDescricaoTipoEntrega())) {
			parametroPath = TipoParametroSistema.PATH_PROCURACAO;
		} else {
			return;
		}
		
		ParametroSistema raiz = 
				this.parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_ARQUIVOS_DISTRIBUICAO_COTA);
		
		ParametroSistema path = 
				this.parametroSistemaService.buscarParametroPorTipoParametro(parametroPath);								
		
		String baseDir = (raiz.getValor() + path.getValor() + dto.getNumCota().toString() ).replace("\\", "/");
				
		fileService.resetTemp(baseDir);
		
		String fileName = fileService.obterNomeArquivoTemp(baseDir);
		
		if(DescricaoTipoEntrega.ENTREGA_EM_BANCA.equals(dto.getDescricaoTipoEntrega())) {
			dto.setNomeTermoAdesao(fileName);
		} else if(DescricaoTipoEntrega.ENTREGADOR.equals(dto.getDescricaoTipoEntrega())) {
			dto.setNomeProcuracao(fileName);
		}
	}

	/**
	 * Persiste no banco os dados de Distribuição da cota
	 * @param distribuicao - DTO que representa os dados de distribuição da cota
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@Post
	public void salvarDistribuicaoCota(DistribuicaoDTO distribuicao) throws FileNotFoundException, IOException {
		
		this.validarDadosDistribuicaoCotaSalvar(distribuicao);

		processarTipoEntrega(distribuicao);
		
		cotaService.salvarDistribuicaoCota(distribuicao);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	private void processarTipoEntrega (DistribuicaoDTO distribuicao) {
		
		if(distribuicao.getDescricaoTipoEntrega() == null) {
			return;
		}
		
		switch(distribuicao.getDescricaoTipoEntrega()) {
			case ENTREGA_EM_BANCA:
				if(!distribuicao.getTermoAdesaoRecebido())
					excluirArquivo(distribuicao.getNumCota(), TipoParametroSistema.PATH_TERMO_ADESAO);
				break;
			case ENTREGADOR:
				if(!distribuicao.getProcuracaoRecebida())
					excluirArquivo(distribuicao.getNumCota(), TipoParametroSistema.PATH_PROCURACAO);
				break;
			default:
				break;
		}	
	}

	/**
	 * Valida os dados de Distribuição da Cota ao salvar.
	 * 
	 * @param distribuicao - DTO que representa os dados de distribuição da cota
	 */
	private void validarDadosDistribuicaoCotaSalvar(DistribuicaoDTO distribuicao) {
		
		if (DescricaoTipoEntrega.ENTREGA_EM_BANCA.equals(distribuicao.getDescricaoTipoEntrega())) {
			
			this.validarPercentualTaxa(
				distribuicao.getPercentualFaturamento(), distribuicao.getTaxaFixa());
			
			this.validarPeriodoCarencia(
				distribuicao.getInicioPeriodoCarencia(), distribuicao.getFimPeriodoCarencia());
			
		} else if (DescricaoTipoEntrega.ENTREGADOR.equals(distribuicao.getDescricaoTipoEntrega())) {
			
			this.validarPeriodoCarencia(
				distribuicao.getInicioPeriodoCarencia(), distribuicao.getFimPeriodoCarencia());
		}
	}

	/**
	 * Valida os valores de percentual de faturamento e taxa fixa.
	 * 
	 * @param percentualFaturamento - percentual de faturamento
	 * @param taxaFixa - taxa fixa
	 */
	private void validarPercentualTaxa(BigDecimal percentualFaturamento, BigDecimal taxaFixa) {
		
		if ((percentualFaturamento == null && taxaFixa == null)
				|| (percentualFaturamento != null && taxaFixa != null)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"O Percentual de Faturamento ou a Taxa Fixa devem ser preenchidos!");
		}
	}

	/**
	 * Valida o período de carência informado.
	 * 
	 * @param inicioPeriodoCarenciaFormatado - início do período de carência formatado
	 * @param fimPeriodoCarenciaFormatado - fim do período de carência formatado
	 */
	private void validarPeriodoCarencia(String inicioPeriodoCarenciaFormatado,
									   	String fimPeriodoCarenciaFormatado) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		if ((inicioPeriodoCarenciaFormatado == null || inicioPeriodoCarenciaFormatado.trim().isEmpty())
				&& (fimPeriodoCarenciaFormatado == null || fimPeriodoCarenciaFormatado.trim().isEmpty())) {
			
			return;
		}
		
		if (inicioPeriodoCarenciaFormatado == null || inicioPeriodoCarenciaFormatado.trim().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "O início do Período de Carência deve ser preenchido!");
		}
		
		if (fimPeriodoCarenciaFormatado == null || fimPeriodoCarenciaFormatado.trim().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "O fim do Período de Carência deve ser preenchido!");
		}
				
		if (!DateUtil.isValidDatePTBR(inicioPeriodoCarenciaFormatado)) {
			
			listaMensagens.add("Início do Período de Carência inválido!");
		}
		
		if (!DateUtil.isValidDatePTBR(fimPeriodoCarenciaFormatado)) {
			
			listaMensagens.add("Fim do Período de Carência inválido!");
		}
		
		if (!listaMensagens.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, listaMensagens);	
		}
		
		Date inicioPeriodoCarencia = DateUtil.parseDataPTBR(inicioPeriodoCarenciaFormatado);
		Date fimPeriodoCarencia = DateUtil.parseDataPTBR(fimPeriodoCarenciaFormatado);
		
		if (DateUtil.isDataInicialMaiorDataFinal(
				inicioPeriodoCarencia, fimPeriodoCarencia)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"O início do Período de Carência deve ser menor que o fim do Período de Carência!");
		}
	}

	/**
	 * Gera combos de Tipo de Entrega
	 * 
	 * @return List<ItemDTO<DescricaoTipoEntrega, String>> 
	 */
	private List<ItemDTO<DescricaoTipoEntrega, String>> gerarTiposEntrega() {
		
		List<ItemDTO<DescricaoTipoEntrega, String>> itens =
			new ArrayList<ItemDTO<DescricaoTipoEntrega,String>>();
				
		for(DescricaoTipoEntrega item: DescricaoTipoEntrega.values()) {
			
			itens.add(new ItemDTO<DescricaoTipoEntrega, String>(
				item, item.getValue() ));
		}
		
		return itens;
	}
	
	/**
	 * Gera combos de Base de Calculo
	 * 
	 * @return List<ItemDTO<BaseCalculo, String>> 
	 */
	private List<ItemDTO<BaseCalculo,String>> gerarBasesCalculo(){
		
		List<ItemDTO<BaseCalculo,String>> comboBaseCalculo =  new ArrayList<ItemDTO<BaseCalculo,String>>();
		for (BaseCalculo itemBaseCalculo: BaseCalculo.values()){
			comboBaseCalculo.add(new ItemDTO<BaseCalculo,String>(itemBaseCalculo, itemBaseCalculo.getValue()));
		}
		
		return comboBaseCalculo;
	}
	
	/**
	 * Recarrega os dados de endereço referente a cota.
	 * 
	 * @param idCota - identificador da cota
	 * @param idHistorico - identificador do histórico de titularidade da cota
	 * @param modoTela - Modo em que a tela está operando
	 */
	@Post
	public void recarregarEndereco(Long idCota, Long idHistorico, ModoTela modoTela){
		if (modoTela == ModoTela.CADASTRO_COTA) {
		
		    obterEndereco(idCota);
		} else {
			
		    carregarEnderecosHistoricoTitularidade(idCota, idHistorico);
		}
		
		this.result.use(Results.json()).from("", "result").serialize();
	}
	
	/**
	 * Recarrega os dados de telefone referente a cota.
	 * 
	 * @param idCota - identificador da cota
	 * @param idHistorico - identificador do histórico de titularidade da cota
	 * @param modoTela - Modo em que a tela está operando
	 */
	@Post
	public void recarregarTelefone(Long idCota, Long idHistorico, ModoTela modoTela){
		if (modoTela == ModoTela.CADASTRO_COTA) {
			
		    obterTelefones(idCota);
		} else {
			
		    carregarTelefonesHistoricoTitularidade(idCota, idHistorico);
		}
		
		this.result.use(Results.json()).from("", "result").serialize();
	}
	
	/**
	 * Obtem os endereços da sessão referente a cota informada
	 * 
	 * @param idCota - identificador da cota
	 */
	private void obterEndereco(Long idCota){
		
		if (this.isNotEnderecoPendente()){
			
			List<EnderecoAssociacaoDTO> listaEnderecoAssociacao = this.cotaService.obterEnderecosPorIdCota(idCota);
		
			this.session.setAttribute(LISTA_ENDERECOS_EXIBICAO, listaEnderecoAssociacao);
		}
	}
	
	/**
	 * Obtem os telefones da sessão referente a cota informada
	 * 
	 * @param idCota - identificador da cota
	 */
	private void obterTelefones(Long idCota){
		
		List<TelefoneAssociacaoDTO> listaTelefoneAssociacao = this.cotaService.buscarTelefonesCota(idCota, null);
		
		this.session.setAttribute(LISTA_TELEFONES_EXIBICAO, listaTelefoneAssociacao);
	}
	
	/**
	 * Limpa os dados da sessão referente a telefone e endereço.
	 */
	private void limparDadosSession(){
		
		this.session.removeAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		this.session.removeAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
		this.session.removeAttribute(LISTA_TELEFONES_EXIBICAO);
		
		this.session.removeAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
		this.session.removeAttribute(LISTA_ENDERECOS_REMOVER_SESSAO);
		this.session.removeAttribute(LISTA_ENDERECOS_EXIBICAO);
		
		this.session.removeAttribute(EnderecoController.ENDERECO_PENDENTE);
		
		this.session.removeAttribute(FILTRO_SESSION_ATTRIBUTE);
	}
	
	@Post
	public void uploadTermoAdesao(UploadedFile uploadedFileTermo, Integer numCotaUpload) throws IOException {		
		upload(uploadedFileTermo, numCotaUpload, TipoParametroSistema.PATH_TERMO_ADESAO);
	}
	
	@Post
	public void uploadProcuracao(UploadedFile uploadedFileProcuracao, Integer numCotaUpload) throws IOException {		
		upload(uploadedFileProcuracao, numCotaUpload, TipoParametroSistema.PATH_PROCURACAO);
	}
	
	private void upload(UploadedFile uploadedFile, Integer numCota, TipoParametroSistema parametroPath ) throws IOException {		
		
		String fileName = "";
		
		if(uploadedFile != null) {
			
			this.fileService.validarArquivo(1, uploadedFile, extensoesAceitas);
			
			ParametroSistema raiz = 
					this.parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_ARQUIVOS_DISTRIBUICAO_COTA);
			
			ParametroSistema path = 
					this.parametroSistemaService.buscarParametroPorTipoParametro(parametroPath);								
			
			String dirBase = (raiz.getValor() + path.getValor() + numCota.toString() ).replace("\\", "/");
			
			fileService.setArquivoTemp(dirBase, uploadedFile.getFileName(), uploadedFile.getFile());
			
			fileName = uploadedFile.getFileName();
		}
		
		this.result.use(PlainJSONSerialization.class)
			.from(fileName, "result").recursive().serialize();
	}
	
	@Post
	public void validarValoresParaDownload(BigDecimal taxa, BigDecimal percentual) {
		
		this.validarPercentualTaxa(percentual, taxa);
		
		this.result.use(Results.json()).from("", "result").serialize();
	}
	
	@Get
	public void downloadTermoAdesao(Boolean termoAdesaoRecebido, Integer numeroCota, BigDecimal taxa, BigDecimal percentual) throws Exception {
		
		download(termoAdesaoRecebido, numeroCota, TipoParametroSistema.PATH_TERMO_ADESAO, taxa, percentual);
	}
	
	@Get
	public void downloadProcuracao(Boolean procuracaoRecebida, Integer numeroCota) throws Exception {
		
		download(procuracaoRecebida, numeroCota, TipoParametroSistema.PATH_PROCURACAO, null, null);
	}
	
	private void download(Boolean documentoRecebido, Integer numeroCota, TipoParametroSistema parametroPath, BigDecimal taxa, BigDecimal percentual) throws Exception {
		
		ParametroSistema raiz = 
				this.parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_ARQUIVOS_DISTRIBUICAO_COTA);
		
		ParametroSistema path = 
				this.parametroSistemaService.buscarParametroPorTipoParametro(parametroPath);		
				
		String dirBase = (raiz.getValor() + path.getValor() + numeroCota.toString() ).replace("\\", "/");
				
		ArquivoDTO dto = fileService.obterArquivoTemp(dirBase);
		
		byte[] arquivo = null;
		
		String contentType = null;
		String nomeArquivo = null;
		
		if(dto == null || !documentoRecebido) {
			
			if(TipoParametroSistema.PATH_TERMO_ADESAO.equals(parametroPath)) {
			
				arquivo = this.cotaService.getDocumentoTermoAdesao(numeroCota, taxa, percentual);
			
				nomeArquivo = NOME_DEFAULT_TERMO_ADESAO;
				
			} else {
				
				arquivo = this.cotaService.getDocumentoProcuracao(numeroCota);
				
				nomeArquivo = NOME_DEFAULT_PROCURACAO;
			}
			
			contentType = "application/pdf";
			
		} else {
		
			arquivo = IOUtils.toByteArray(dto.getArquivo());
			
			((FileInputStream)dto.getArquivo()).close();
			
			contentType = dto.getContentType();
			
			nomeArquivo = dto.getNomeArquivo();
		}
		
		this.httpResponse.setContentType(contentType);
		this.httpResponse.setHeader("Content-Disposition", "attachment; filename=" + nomeArquivo);

		OutputStream output = this.httpResponse.getOutputStream();
		output.write(arquivo);

		httpResponse.flushBuffer();
		
	}
	
	public void excluirTermoAdesao(Integer numCota) {
		
		this.excluirArquivo(numCota, TipoParametroSistema.PATH_TERMO_ADESAO);
		
		this.result.use(PlainJSONSerialization.class)
			.from("", "result").recursive().serialize();
	}
	
	
	public void excluirProcuracao(Integer numCota) {
		
		this.excluirArquivo(numCota, TipoParametroSistema.PATH_PROCURACAO);
		
		this.result.use(PlainJSONSerialization.class)
			.from("", "result").recursive().serialize();
	}
	
	private void excluirArquivo(Integer numCota, TipoParametroSistema parametroPath) {		
		
		ParametroSistema raiz = 
			this.parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_ARQUIVOS_DISTRIBUICAO_COTA);
		
		ParametroSistema pathTermoAdesao = 
			this.parametroSistemaService.buscarParametroPorTipoParametro(parametroPath);		
		
		if (raiz == null || pathTermoAdesao == null) {
			
			return;
		}
		
		String path = (raiz.getValor() + pathTermoAdesao.getValor() + numCota.toString() ).replace("\\", "/");
		
		fileService.esvaziarTemp(path);
	}
	
	@Post
	public void carregarValoresEntregaBanca(Integer numCota) {
		
		DistribuicaoDTO dto = cotaService.carregarValoresEntregaBanca(numCota);
		
		this.result.use(Results.json()).from(dto, "result").serialize();
	}
	
	@Post
	public void distribuidorUtilizaTermoAdesao(){
		
		Boolean utilizaTermo = this.distribuidorService.utilizaTermoAdesao();
	
		this.result.use(Results.json()).from(utilizaTermo, "result").serialize();
	}
	
	@Post
	public void distribuidorUtilizaProcuracao(){
		
		Boolean utilizaTermo = this.distribuidorService.utilizaProcuracaoEntregadores();
	
		this.result.use(Results.json()).from(utilizaTermo, "result").serialize();
	}
	
}

