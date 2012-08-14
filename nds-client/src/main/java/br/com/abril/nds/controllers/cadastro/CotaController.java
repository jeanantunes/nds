package br.com.abril.nds.controllers.cadastro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.client.vo.CotaVO;
import br.com.abril.nds.client.vo.DadosCotaVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaDTO.TipoPessoa;
import br.com.abril.nds.dto.DistribuicaoDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ClassificacaoEspectativaFaturamento;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoDesconto;
import br.com.abril.nds.model.cadastro.TipoEntrega;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DividaService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.PessoaFisicaService;
import br.com.abril.nds.service.PessoaJuridicaService;
import br.com.abril.nds.service.TipoEntregaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Message;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/cota")
public class CotaController {
	
	public static final String LISTA_TELEFONES_SALVAR_SESSAO = "listaTelefonesSalvarSessaoCota";
	
	public static final String LISTA_TELEFONES_REMOVER_SESSAO = "listaTelefonesRemoverSessaoCota";
	
	public static final String LISTA_TELEFONES_EXIBICAO = "listaTelefonesExibicaoCota";

	public static final String LISTA_ENDERECOS_SALVAR_SESSAO = "listaEnderecoSalvarSessaoCota";

	public static final String LISTA_ENDERECOS_REMOVER_SESSAO = "listaEnderecoRemoverSessaoCota";

	public static final String LISTA_ENDERECOS_EXIBICAO = "listaEnderecoExibicaoCota";
	
	@Autowired
	private Result result;
	
	@Autowired
	private br.com.caelum.vraptor.Validator validator;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private DividaService dividaService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private ParametroCobrancaCotaController financeiroController;
	
	@Autowired
	private TipoEntregaService tipoEntregaService;
	
	@Autowired
	private PdvController pdvController;
	
	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;
	
	@Autowired
	private PessoaFisicaService pessoaFisicaService;

	private static final String FILTRO_SESSION_ATTRIBUTE="filtroCadastroCota";

	@Path("/")
	public void index() {
		
		this.financeiroController.preCarregamento();
		this.pdvController.preCarregamento();
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

	@SuppressWarnings("unchecked")
	/**
	 * Processa os dados de endereço, obtem os dados da sessão, grava os dados no banco de dados e atualiza os dados na sessão
	 * 
	 * @param idCota - identificador da cota
	 */
	private void processarEnderecosCota(Long idCota) {

		List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar = 
				(List<EnderecoAssociacaoDTO>) this.session.getAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
		
		List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoRemover = 
				(List<EnderecoAssociacaoDTO>) this.session.getAttribute(LISTA_ENDERECOS_REMOVER_SESSAO);
		
		this.cotaService.processarEnderecos(idCota, listaEnderecoAssociacaoSalvar, listaEnderecoAssociacaoRemover);
		
		this.session.removeAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
		this.session.removeAttribute(LISTA_ENDERECOS_REMOVER_SESSAO);
		
		obterEndereco(idCota);
	}

	/**
	 * Processa os dados de telefone, obtem os dados da sessão, grava os dados no banco de dados e atualiza os dados na sessão
	 * 
	 * @param idCota - identificador da cota
	 */
	private void processarTelefonesCota(Long idCota){
		
		
		Map<Integer, TelefoneAssociacaoDTO> map = this.obterTelefonesSalvarSessao();
		
		List<TelefoneAssociacaoDTO> lista = new ArrayList<TelefoneAssociacaoDTO>();
		for (Integer key : map.keySet()){
			TelefoneAssociacaoDTO telefoneAssociacaoDTO = map.get(key);
			
			if(telefoneAssociacaoDTO.getTipoTelefone()!= null){
				lista.add(telefoneAssociacaoDTO);
			}
		}
		
		Set<Long> telefonesRemover = this.obterTelefonesRemoverSessao();
		this.cotaService.processarTelefones(idCota, lista, telefonesRemover);
		
		this.session.removeAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		this.session.removeAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
		
		obterTelefones(idCota);
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
			throw new ValidacaoException(TipoMensagem.WARNING, "Número da cota inválido!");
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
	
				listaCotasAutoComplete.add(new ItemAutoComplete(nomeExibicao, null, cotaVO));
			}
		}
		
		this.result.use(Results.json()).from(listaCotasAutoComplete, "result").include("value", "chave").serialize();
	}

	/**
	 * Efetua consulta pelo nome da cota informado
	 * 
	 * @param nomeCota - nome da cota
	 */
	@Post
	public void pesquisarPorNome(String nomeCota) {
		
		nomeCota = PessoaUtil.removerSufixoDeTipo(nomeCota);
		
		Cota cota = this.cotaService.obterPorNome(nomeCota);
		
		if (cota == null) {
		
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota \"" + nomeCota + "\" não encontrada!");
		}
		
		String nomeExibicao = PessoaUtil.obterNomeExibicaoPeloTipo(cota.getPessoa());
				
		CotaVO cotaVO = new CotaVO(cota.getNumeroCota(), nomeExibicao);
			
		this.result.use(Results.json()).from(cotaVO, "result").serialize();
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
		
		validarFormatoData();
		
		cotaDTO.setTipoPessoa(TipoPessoa.JURIDICA);
		
		cotaDTO = salvarDadosCota(cotaDTO);
		
		cotaDTO.setNumeroCnpj(Util.adicionarMascaraCNPJ(cotaDTO.getNumeroCnpj()));
		
		carregarDadosEnderecoETelefone(cotaDTO.getIdCota());
		
		result.use(Results.json()).from(cotaDTO, "result").recursive().serialize();
	}
	
	/**
	 * Salva os dados de uma cota do tipo CPF
	 * 
	 * @param cotaDTO
	 */
	@Post
	@Path("/salvarCotaCPF")
	public void salvarCotaPessoaFisica(CotaDTO cotaDTO){
		
		validarFormatoData();
		
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
		
		Long idCota = cotaService.salvarCota(cotaDTO);
		
		return cotaDTO = cotaService.obterDadosCadastraisCota(idCota);
	}
	
	/**
	 * Valida o formato das datas utilizadas na tela de cadastro de cota
	 */
	private void validarFormatoData(){
		
		List<String> mensagensValidacao = new ArrayList<String>();
		
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
			
			if (!mensagensValidacao.isEmpty()){
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagensValidacao));
			}
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
	 * Verifica se a cota possui dividas em aberto
	 * @param idCota
	 * @return boolean
	 */
	private boolean cotaComDebitos(Long idCota){
		
        BigDecimal dividasEmAberto = dividaService.obterTotalDividasAbertoCota(idCota);
		
		return (dividasEmAberto!=null && dividasEmAberto.floatValue() > 0);
	}
	
	/**
	 * Valida numero da cota, verificando se existem dívidas em aberto.
	 * @param numeroCota
	 */
	@Post
	@Path("/verificarPendenciasCota")
	public void verificarPendenciasCota(Integer numeroCota){
        if(numeroCota != null) {
			
			Cota cota = this.cotaService.obterPorNumeroDaCotaAtiva(numeroCota);

			if (cota != null) {
			    if (cotaComDebitos(cota.getId())){
		            throw new ValidacaoException(TipoMensagem.WARNING, "O [Número] pertence à uma [Cota] que possui dívidas em aberto e não pode ser utilizado!");
			    }
			}
        }	
        result.nothing();
	}
	
	/**
	 * Exclui uma cota, informada pelo usúario
	 * 
	 * @param idCota - identificador da cota
	 */
	@Post
	@Path("/excluir")
	public void excluir(Long idCota){

		if (cotaComDebitos(idCota)){
			throw new ValidacaoException(TipoMensagem.WARNING, "A [Cota] possui dívidas em aberto e não pode ser excluída!");
		}
		
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
		
		if(fornecedores == null){
			
			List<Fornecedor> list = fornecedorService.obterFornecedoresCota(idCota);
	
			if(list!= null && !list.isEmpty()){
				
				fornecedorService.salvarFornecedorCota(fornecedores, idCota);
			}
		}else {
			
			fornecedorService.salvarFornecedorCota(fornecedores, idCota);
		}
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	/**
	 * Salva os dados referente a tipo de desconto, associa os tipos de desconto a cota informada.
	 * 
	 * @param descontos  - tipos de desconto selecionados
	 * @param idCota - identificador da cota
	 */
	@Post
	@Path("/salvarDescontos")
	public void salvarDescontos(List<Long> descontos, Long idCota){
		
		if(descontos == null){
			
			List<TipoDesconto> list = cotaService.obterDescontosCota(idCota);
	
			if(list!= null && !list.isEmpty()){
				
				cotaService.salvarDescontosCota(descontos, idCota);
			}
		}else {
			
			cotaService.salvarDescontosCota(descontos, idCota);
		}
		
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
		
		processarEnderecosCota(idCota);
	
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	/**
	 * Salva os telefones da cota informada
	 * 
	 * @param idCota - identificador da cota
	 */
	@Post
	@Path("/salvarTelefones")
	public void salvarTelefones(Long idCota){
		
		processarTelefonesCota(idCota);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	/**
	 * Obtem os fornecedores que não possui associação com a cota informada
	 * 
	 * @param idCota -identificador da cota
	 */
	@Post
	@Path("/obterFornecedores")
	public void obterFornecedores(Long idCota){
		
		List<Fornecedor> fornecedores =   fornecedorService.obterFornecedores(idCota);
		
		result.use(Results.json()).from(this.getFornecedores(fornecedores),"result").recursive().serialize();
	}
	
	/**
	 * Obtem os fornecedores associados a cota informada
	 * 
	 * @param idCota -identificador da cota
	 */
	@Post
	@Path("/obterFornecedoresSelecionados")
	public void obterFornecedoresSelecionados(Long idCota){
		
		List<Fornecedor> fornecedores =   fornecedorService.obterFornecedoresCota(idCota);
		
		result.use(Results.json()).from(this.getFornecedores(fornecedores),"result").recursive().serialize();
	
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
	 * Obtem os tipos de desconto que não possui associação com a cota informada
	 * 
	 * @param idCota -identificador da cota
	 */
	@Post
	@Path("/obterDescontos")
	public void obterDescontos(Long idCota){
		
		List<TipoDesconto> descontos =   cotaService.obterDescontos(idCota);
		
		result.use(Results.json()).from(this.getDescontos(descontos),"result").recursive().serialize();
	}
	
	/**
	 * Retorna uma lista de tipo de desconto para exibição na tela
	 * 
	 * @param descontos - lista de descontos
	 * 
	 * @return List<ItemDTO<Long, String>>
	 */
	private List<ItemDTO<Long, String>> getDescontos(List<TipoDesconto> descontos){
		
		List<ItemDTO<Long, String>> itensDesconto = new ArrayList<ItemDTO<Long,String>>();
		
		for(TipoDesconto desconto : descontos){
			
			itensDesconto.add(new ItemDTO<Long, String>(desconto.getId(), desconto.getCodigo()+" - " +desconto.getDescricao()));
		}
		return itensDesconto;
	}
	
	/**
	 * Obtem os tipos de desconto associados a cota informada
	 * 
	 * @param idCota -identificador da cota
	 */
	@Post
	@Path("/obterDescontosSelecionados")
	public void obterDescontosSelecionados(Long idCota){
		
		List<TipoDesconto> descontos =   cotaService.obterDescontosCota(idCota);
		
		result.use(Results.json()).from(this.getDescontos(descontos),"result").recursive().serialize();
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
	public void pesquisarCotas(Integer numCota,String nomeCota,String numeroCpfCnpj, String sortorder, 
			 				   String sortname, int page, int rp){
		
		numeroCpfCnpj = numeroCpfCnpj.replace(".", "").replace("-", "").replace("/", "");
		
		validarParametrosPesquisa(numCota,nomeCota,numeroCpfCnpj);
		
		nomeCota = PessoaUtil.removerSufixoDeTipo(nomeCota);
		
		FiltroCotaDTO filtro = new FiltroCotaDTO( numCota,nomeCota,numeroCpfCnpj );
		
		configurarPaginacaoPesquisa(filtro, sortorder, sortname, page, rp);
		
		tratarFiltro(filtro);
		
		efetuarConsulta(filtro);
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
	 * Valida os parâmetros de pesquisa da consulta de cotas cadastradas
	 * 
	 * @param numCota - número da cota
	 * @param nomeCota - nome da cota
	 * @param numeroCpfCnpj - número do CNPJ ou CPF
	 * 
	 */
	private void validarParametrosPesquisa(Integer numCota,String nomeCota, String numeroCpfCnpj) {
		
		if(numCota == null 
				&& (nomeCota == null || nomeCota.isEmpty())
				&& (numeroCpfCnpj == null || numeroCpfCnpj.isEmpty())){
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Pelomenos um dos filtros deve ser informado!");
		}
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
	 */
	@Post
	public void carregarDistribuicaoCota(Long idCota) {
		
		DistribuicaoDTO dto = cotaService.obterDadosDistribuicaoCota(idCota);
		
		dto.setTiposEntrega(gerarTiposEntrega());
		
		this.result.use(Results.json()).from(dto, "result").recursive().serialize();
	}
	
	/**
	 * Persiste no banco os dados de Distribuição da cota
	 * @param distribuicao - DTO que representa os dados de distribuição da cota
	 */
	@Post
	public void salvarDistribuicaoCota(DistribuicaoDTO distribuicao) {
		
		cotaService.salvarDistribuicaoCota(distribuicao);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
	}

	/**
	 * Gera combos de Tipo de Entrega
	 * 
	 * @return List<ItemDTO<Long, String>> 
	 */
	private List<ItemDTO<Long, String>> gerarTiposEntrega() {
		
		List<ItemDTO<Long, String>> itens = new ArrayList<ItemDTO<Long,String>>();
				
		for(TipoEntrega item: tipoEntregaService.obterTodos()) {
			itens.add(new ItemDTO<Long, String>(item.getId(), item.getDescricao()));
		}
		
		return itens;
	}
	
	/**
	 * Recarrega os dados de endereço referente a cota.
	 * 
	 * @param idCota - identificador da cota
	 */
	@Post
	public void recarregarEndereco(Long idCota){
		
		obterEndereco(idCota);
		
		this.result.use(Results.json()).from("", "result").serialize();
	}
	
	/**
	 * Recarrega os dados de telefone referente a cota.
	 * 
	 * @param idCota - identificador da cota
	 */
	@Post
	public void recarregarTelefone(Long idCota){
		
		obterTelefones(idCota);
		
		this.result.use(Results.json()).from("", "result").serialize();
	}
	
	/**
	 * Obtem os endereços da sessão referente a cota informada
	 * 
	 * @param idCota - identificador da cota
	 */
	private void obterEndereco(Long idCota){
		
		List<EnderecoAssociacaoDTO> listaEnderecoAssociacao = this.cotaService.obterEnderecosPorIdCota(idCota);
		
		this.session.setAttribute(LISTA_ENDERECOS_EXIBICAO, listaEnderecoAssociacao);
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
	}
}

