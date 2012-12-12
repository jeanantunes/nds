package br.com.abril.nds.controllers.cadastro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.client.vo.CotaAtendidaTransportadorVO;
import br.com.abril.nds.dto.AssociacaoVeiculoMotoristaRotaDTO;
import br.com.abril.nds.dto.ConsultaTransportadorDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.RotaRoteiroDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaTransportadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaTransportadorDTO.OrdenacaoColunaTransportador;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.AssociacaoVeiculoMotoristaRota;
import br.com.abril.nds.model.cadastro.Motorista;
import br.com.abril.nds.model.cadastro.ParametroCobrancaTransportador;
import br.com.abril.nds.model.cadastro.ParametroCobrancaTransportador.ModalidadeCobranca;
import br.com.abril.nds.model.cadastro.PeriodicidadeCobranca;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Transportador;
import br.com.abril.nds.model.cadastro.Veiculo;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.PessoaService;
import br.com.abril.nds.service.TransportadorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/transportador")
public class TransportadorController {

	public static final String LISTA_TELEFONES_SALVAR_SESSAO = "listaTelefonesSalvarSessaoTransportador";
	
	public static final String LISTA_TELEFONES_REMOVER_SESSAO = "listaTelefonesRemoverSessaoTransportador";
	
	public static final String LISTA_TELEFONES_EXIBICAO = "listaTelefonesExibicaoTransportador";
	
	public static final String LISTA_ENDERECOS_SALVAR_SESSAO = "listaEnderecosSalvarSessaoTransportador";
	
	public static final String LISTA_ENDERECOS_REMOVER_SESSAO = "listaEnderecosRemoverSessaoTransportador";
	
	public static final String LISTA_ENDERECOS_EXIBICAO = "listaEnderecosExibicaoTransportador";
	
	private static final String ID_TRANSPORTADORA_EDICAO = "idTransportadoraEdicao";
	
	private static final String LISTA_VEICULOS_SALVAR_SESSAO = "listaVeiculosSalvarSessao";
	
	private static final String LISTA_VEICULOS_REMOVER_SESSAO = "listaVeiculosRemoverSessao";
	
	private static final String LISTA_MOTORISTAS_SALVAR_SESSAO = "listaMotoristasSalvarSessao";
	
	private static final String LISTA_MOTORISTAS_REMOVER_SESSAO = "listaMotoristasRemoverSessao";
	
	private static final String LISTA_ASSOCIACOES_SALVAR_SESSAO = "listaAssociacoesSalvarSessao";
	
	private static final String LISTA_ASSOCIACOES_REMOVER_SESSAO = "listaAssociacoesRemoverSessao";
	
	@Autowired
	private TransportadorService transportadorService;
	
	@Autowired
	PessoaService pessoaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	private final String FILTRO_PESQUISA_TRANSPORTADORES = "filtroPesquisaTransportadores";
	
	private Result result;
	
	private HttpSession httpSession;
	
	private HttpServletResponse httpResponse;
	
	public TransportadorController(Result result, HttpSession httpSession, HttpServletResponse httpResponse){
		
		this.result = result;
		this.httpSession = httpSession;
		this.httpResponse = httpResponse;
	}
	
	@Path("/")
	@Rules(Permissao.ROLE_CADASTRO_TRANSPORTADOR)
	public void index(){
		
		this.limparDadosSessao();
	}
	
	private void limparDadosSessao() {
		
		this.httpSession.removeAttribute(LISTA_TELEFONES_EXIBICAO);
		this.httpSession.removeAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
		this.httpSession.removeAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		this.httpSession.removeAttribute(LISTA_ENDERECOS_EXIBICAO);
		this.httpSession.removeAttribute(LISTA_ENDERECOS_REMOVER_SESSAO);
		this.httpSession.removeAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
		
		this.httpSession.removeAttribute(EnderecoController.ENDERECO_PENDENTE);
		
		this.httpSession.removeAttribute(ID_TRANSPORTADORA_EDICAO);
		this.httpSession.removeAttribute(LISTA_ASSOCIACOES_SALVAR_SESSAO);
		this.httpSession.removeAttribute(LISTA_ASSOCIACOES_REMOVER_SESSAO);
		this.httpSession.removeAttribute(LISTA_VEICULOS_SALVAR_SESSAO);
		this.httpSession.removeAttribute(LISTA_VEICULOS_REMOVER_SESSAO);
		this.httpSession.removeAttribute(LISTA_MOTORISTAS_REMOVER_SESSAO);
		this.httpSession.removeAttribute(LISTA_MOTORISTAS_SALVAR_SESSAO);
	}

	@Post
	public void pesquisarTransportadores(FiltroConsultaTransportadorDTO filtro, String sortorder, 
			String sortname, Integer page, Integer rp){
		
		if (filtro == null){
			
			filtro = (FiltroConsultaTransportadorDTO) this.httpSession.getAttribute(FILTRO_PESQUISA_TRANSPORTADORES);
		}
		
		if (filtro == null){
			
			filtro = new FiltroConsultaTransportadorDTO();
		}
		
		if (filtro.getPaginacaoVO() == null){
			
			filtro.setPaginacaoVO(new PaginacaoVO(page, rp, Ordenacao.ASC.getOrdenacao()));
		}
		
		if (page != null){
			
			filtro.getPaginacaoVO().setPaginaAtual(page);
		}
		
		if (rp != null){
			
			filtro.getPaginacaoVO().setQtdResultadosPorPagina(rp);
		}
		
		if (sortorder != null) {
			
			filtro.getPaginacaoVO().setOrdenacao(Util.getEnumByStringValue(Ordenacao.values(), sortorder));
		}
		
		if (sortname != null){
			
			filtro.setOrdenacaoColunaTransportador(Util.getEnumByStringValue(OrdenacaoColunaTransportador.values(), sortname));
		}
		
		if (filtro.getCnpj() != null){
			
			filtro.setCnpj(filtro.getCnpj().replace(".", "").replace("/", "").replace("-", ""));
		}
		
		filtro.setNomeFantasia(PessoaUtil.removerSufixoDeTipo(filtro.getNomeFantasia()));
		filtro.setRazaoSocial(PessoaUtil.removerSufixoDeTipo(filtro.getRazaoSocial()));
		
		ConsultaTransportadorDTO consulta = this.transportadorService.consultarTransportadores(filtro);
		
		if (consulta.getTransportadores().isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		this.result.use(FlexiGridJson.class)
			.from(consulta.getTransportadores())
			.total(consulta.getQuantidadeRegistros().intValue())
			.page(page == null ? 1 : page).serialize();
	}
	
	@SuppressWarnings("unchecked")
	@Post
	public void cadastrarTransportador(Transportador transportador){
		
		
		this.validarDadosEntrada(transportador);
		
		
		//Processa Endereços
		List<EnderecoAssociacaoDTO> listaEnderecosSalvar = (List<EnderecoAssociacaoDTO>) 
				this.httpSession.getAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
		
		List<EnderecoAssociacaoDTO> lisEndRemover = (List<EnderecoAssociacaoDTO>) 
				this.httpSession.getAttribute(LISTA_ENDERECOS_REMOVER_SESSAO);
		
		List<EnderecoAssociacaoDTO> listaEnderecosRemover = new ArrayList<EnderecoAssociacaoDTO>();
		
		if (lisEndRemover != null){
			for (EnderecoAssociacaoDTO dto : lisEndRemover){
				
				if (dto.getEndereco() != null && dto.getEndereco().getId() != null){
					
					listaEnderecosRemover.add(dto);
				}
			}
		}
		
		
		//Processa Telefones
		Map<Integer, TelefoneAssociacaoDTO> listTelSalvar = (Map<Integer, TelefoneAssociacaoDTO>) 
				this.httpSession.getAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		
		Set<Long> listaTelefonesRemover = (Set<Long>) 
				this.httpSession.getAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
		
		List<TelefoneAssociacaoDTO> listaTelefonesSalvar = new ArrayList<TelefoneAssociacaoDTO>();
		
		if (listTelSalvar != null){
			for (TelefoneAssociacaoDTO dto : listTelSalvar.values()){
				
				listaTelefonesSalvar.add(dto);
			}
		}
		
		
		//Processa Veículos
		List<AssociacaoVeiculoMotoristaRotaDTO> listaAssocAdd = this.obterAssociacoesSalvarSessao();
		
		List<Veiculo> listaVeiculosSalvar = this.obterVeiculosSessao();
		for (Veiculo v : listaVeiculosSalvar){
			
			for (AssociacaoVeiculoMotoristaRotaDTO assoc : listaAssocAdd){
				
				if (v.getId() != null && v.getId().equals(assoc.getVeiculo().getId())){
					
					assoc.setVeiculo(v);
				}
			}
			
			if (v.getId() != null && v.getId() < 0){
				v.setId(null);
			}
		}
		
		
		//Processa Motoristas
		List<Motorista> listaMotoristasSalvar = this.obterMotoristasSessao();
		for (Motorista v : listaMotoristasSalvar){
			
			for (AssociacaoVeiculoMotoristaRotaDTO assoc : listaAssocAdd){
				
				if (v.getId() != null && v.getId().equals(assoc.getMotorista().getId())){
					
					assoc.setMotorista(v);
				}
			}
			
			if (v.getId() != null && v.getId() < 0){
				v.setId(null);
			}
		}
				
		
		//Processa Transportadores
		transportador.setId((Long) this.httpSession.getAttribute(ID_TRANSPORTADORA_EDICAO));

		try {
			
			this.transportadorService.cadastrarTransportador(transportador, 
					listaEnderecosSalvar, 
					listaEnderecosRemover, 
					listaTelefonesSalvar,
					listaTelefonesRemover, 
					listaVeiculosSalvar,
					this.obterVeiculosSessaoRemover(),
					listaMotoristasSalvar,
					this.obterMotoristasSessaoRemover(),
					listaAssocAdd,
					this.obterAssociacoesRemoverSessao());
			
			this.limparDadosSessao();
			
		} finally {
			
			this.reatribuirIds();
		}
		
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."), "result").recursive().serialize();
	}
	
	private void reatribuirIds() {
		
		List<Motorista> motoristas = this.obterMotoristasSessao();
		
		for (Motorista motorista : motoristas){
			
			if (motorista.getId() == null){
				
				int id = (int) (Math.random() * -10000);
				
				if (id > 0){
					id *= -1;
				}
				
				motorista.setId(new Long(id));
			}
		}
		
		List<Veiculo> veiculos = this.obterVeiculosSessao();
		
		for (Veiculo veiculo : veiculos){
			
			if (veiculo.getId() == null){
				
				int id = (int) (Math.random() * -10000);
				
				if (id > 0){
					id *= -1;
				}
				
				veiculo.setId(new Long(id));
			}
		}
	}

	private void validarDadosEntrada(Transportador transportador) {
		
		if (transportador == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Transportador é obrigatório.");
		}
		
		PessoaJuridica pessoaJuridica = transportador.getPessoaJuridica();
		
		if (pessoaJuridica == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Preencha os dados referentes a pessoa jurídica.");
		}
		
		List<String> msgs = new ArrayList<String>();
		
		if (pessoaJuridica.getRazaoSocial() == null || pessoaJuridica.getRazaoSocial().trim().isEmpty()){
			
			msgs.add("Razão social é obrigatório.");
		}
		
		if (pessoaJuridica.getCnpj() == null || pessoaJuridica.getCnpj().trim().isEmpty()){
			
			msgs.add("CNPJ é obrigatório.");

		} else {
			
			CNPJValidator cnpjValidator = new CNPJValidator(true);
			
			try {

				cnpjValidator.assertValid(pessoaJuridica.getCnpj());
				
			} catch(InvalidStateException e) {
			
				msgs.add("CNPJ inválido.");
			}			
			
		}
		
		if (pessoaJuridica.getInscricaoEstadual() == null || pessoaJuridica.getInscricaoEstadual().trim().isEmpty()){
			
			msgs.add("Insc. Estadual é obrigatório.");
		}
		
		//
		if (transportador.getParametroCobrancaTransportador().getModalidadeCobranca() == null) {
			
			msgs.add("Modalidade de Cobrança é obrigatório.");
		} else {
			
			boolean isPercentual = ModalidadeCobranca.PERCENTUAL.equals(
					transportador.getParametroCobrancaTransportador().getModalidadeCobranca());
			
			if (isPercentual) {
				
				if (transportador.getParametroCobrancaTransportador().getValor() == null) {
					msgs.add("É necessário informar um percentual.");
				} else {
					boolean isMaiorCem = 100D < transportador.getParametroCobrancaTransportador().getValor().doubleValue();
					if (isPercentual && isMaiorCem) {
						msgs.add("O percentual não deve ser maior que 100%.");
					}
				}
			}
		}
		
		if (transportador.getParametroCobrancaTransportador() != null){
			
			ParametroCobrancaTransportador param = transportador.getParametroCobrancaTransportador();
			
			if (PeriodicidadeCobranca.SEMANAL == param.getPeriodicidadeCobranca() && 
					(param.getDiasSemanaCobranca() == null || param.getDiasSemanaCobranca().isEmpty())){
				
				msgs.add("Selecione os dias da semana para cobrança semanal.");
			} else if ((PeriodicidadeCobranca.QUINZENAL == param.getPeriodicidadeCobranca() ||
					PeriodicidadeCobranca.MENSAL == param.getPeriodicidadeCobranca()) && 
					(param.getDiaCobranca() == null || param.getDiaCobranca() <= 0 || param.getDiaCobranca() > 31)){
				
				msgs.add("Dia da cobrança inválido");
			}
		}
		
		validarEnderecos(msgs);
		
		validarTelefones(msgs);
		
		if (!msgs.isEmpty()){
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, msgs));
		}
	}

	private void validarEnderecos(List<String> listaMensagens) {
		
		@SuppressWarnings("unchecked")
		List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar = 
				(List<EnderecoAssociacaoDTO>) this.httpSession.getAttribute(
						LISTA_ENDERECOS_SALVAR_SESSAO);
		
		@SuppressWarnings("unchecked")
		List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoExibir = 
				(List<EnderecoAssociacaoDTO>) this.httpSession.getAttribute(
						LISTA_ENDERECOS_EXIBICAO);
		
		List<EnderecoAssociacaoDTO> listaEnderecoAssociacao = new ArrayList<EnderecoAssociacaoDTO>();
		
		if (listaEnderecoAssociacaoSalvar != null) {
			listaEnderecoAssociacao.addAll(listaEnderecoAssociacaoSalvar);
		}
		
		if (listaEnderecoAssociacaoExibir != null) {
			listaEnderecoAssociacao.addAll(listaEnderecoAssociacaoExibir);
		}
		
		if (listaEnderecoAssociacao == null || listaEnderecoAssociacao.isEmpty()) {
			
			listaMensagens.add("Pelo menos um endereço deve ser cadastrado para o entregador.");
		
		} else {
			
			boolean temPrincipal = false;
			
			for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao) {
				
				if (enderecoAssociacao.isEnderecoPrincipal()) {
					
					temPrincipal = true;
					
					break;
				}
			}

			if (!temPrincipal) {
				
				listaMensagens.add("Deve haver ao menos um endereço principal para o entregador.");
			}
		}
	}

	private void validarTelefones(List<String> mensagensValidacao) {
		@SuppressWarnings("unchecked")
		Map<Integer, TelefoneAssociacaoDTO> mapaTelefones = (Map<Integer, TelefoneAssociacaoDTO>) this.httpSession.getAttribute(LISTA_TELEFONES_SALVAR_SESSAO);

		@SuppressWarnings("unchecked")
		List<TelefoneAssociacaoDTO> telefonesExibicao = (List<TelefoneAssociacaoDTO>) this.httpSession.getAttribute(LISTA_TELEFONES_EXIBICAO);
		
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
	
	/*
	 * Método que obtém os telefones a serem salvos, que estão na sessão.
	 */
	@SuppressWarnings("unchecked")
	private Map<Integer, TelefoneAssociacaoDTO> obterTelefonesSalvarSessao(){
		
		Map<Integer, TelefoneAssociacaoDTO> telefonesSessao = (Map<Integer, TelefoneAssociacaoDTO>) 
				this.httpSession.getAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		
		if (telefonesSessao == null){

			telefonesSessao = new LinkedHashMap<Integer, TelefoneAssociacaoDTO>();
		}
		
		return telefonesSessao;
	}

	
	@Post
	public void editarTransportador(Long referencia){
		
		this.httpSession.setAttribute(ID_TRANSPORTADORA_EDICAO, referencia);
		
		Transportador transportador = this.transportadorService.buscarTransportadorPorId(referencia);
		
		List<String> dados = new ArrayList<String>();
		
		if (transportador != null) {
			
			dados.add(transportador.getPessoaJuridica().getRazaoSocial());
			dados.add(transportador.getPessoaJuridica().getNomeFantasia());
			dados.add(transportador.getPessoaJuridica().getEmail());
			dados.add(transportador.getResponsavel());
			dados.add(Util.adicionarMascaraCNPJ(transportador.getPessoaJuridica().getCnpj()));
			dados.add(transportador.getPessoaJuridica().getInscricaoEstadual());
			
			if (transportador.getParametroCobrancaTransportador() != null){
				
				ParametroCobrancaTransportador param = transportador.getParametroCobrancaTransportador();
				
				dados.add(param.getModalidadeCobranca() != null ? param.getModalidadeCobranca().toString() : "");
				dados.add(param.getValor() != null ? String.format("%.2f", param.getValor()) : "0,00");
				dados.add(String.valueOf(param.isPorEntrega()));
				dados.add(param.getPeriodicidadeCobranca().toString());
				dados.add(param.getDiaCobranca() != null ? param.getDiaCobranca().toString() : "");
				
				if (param.getDiasSemanaCobranca() != null){
					
					for (DiaSemana dia : param.getDiasSemanaCobranca()){
						
						dados.add(dia.toString());
					}
				}
			}
			
			this.carregarTelefonesEnderecosPessoa(transportador.getPessoaJuridica().getId());
		}
		
		this.result.use(Results.json()).from(dados, "result").serialize();
	}
	
	@Post
	public void excluirTransportador(Long referencia){
		
		this.transportadorService.excluirTransportador(referencia);
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso.")).recursive().serialize();
	}
	
	@Post
	public void cancelarCadastro(){
		
		this.limparDadosSessao();
		
		result.use(Results.json()).from("", "result").serialize();
	}
	
	@Post
	public void carregarTelaAssociacao(){
		
		Object[] dados = new Object[4];
		
		dados[0] = this.getTableModelVeiculos(this.pesquisarVeiculos(null, null));
		
		dados[1] = this.getTableModelMotoristas(this.pesquisarMotoristas(null, null));
		
		dados[2] = this.getTableModelRotaRoteiro(this.processarRotaRoteiroDisponivel(null, null));
		
		dados[3] = this.getTableModelAssociacoes(this.pesquisarAssociacoes(null, null));
		
		this.result.use(Results.json()).from(dados, "result").serialize();
	}
	
	@Post
	public List<Veiculo> pesquisarVeiculos(String sortname, String sortorder){
		
		Long idTransportadorEdicao = (Long) this.httpSession.getAttribute(ID_TRANSPORTADORA_EDICAO);
		
		if (idTransportadorEdicao == null){
			
			return this.obterVeiculosSessao();
		} else {
			
			List<Veiculo> veiculosSalvar = this.obterVeiculosSessao();
			
			Set<Long> idsIgnorar = new HashSet<Long>();
			
			for (Veiculo veiculoSalvar : veiculosSalvar){
				
				if (veiculoSalvar.getId() != null){
					
					idsIgnorar.add(veiculoSalvar.getId());
				}
			}
			
			idsIgnorar.addAll(this.obterVeiculosSessaoRemover());
			
			List<Veiculo> lista = 
					this.transportadorService.buscarVeiculosPorTransportador(
							idTransportadorEdicao, 
							idsIgnorar,
							sortname,
							sortorder);
			
			if (lista == null){
				
				lista = new ArrayList<Veiculo>();
			}
			
			for (Veiculo veiculo : lista){
				
				veiculo.setTransportador(null);
			}
			
			lista.addAll(this.obterVeiculosSessao());
			
			return lista;
		}
	}
	
	@Post
	public void carregarVeiculos(String sortname, String sortorder){
		
		this.result.use(
				Results.json()).from(
						this.getTableModelVeiculos(this.pesquisarVeiculos(sortname, sortorder)), "result").recursive().serialize();
	}
	
	@Post
	public void adicionarVeiculo(Veiculo veiculo){
		
		this.validarDadosEntradaVeiculo(veiculo);
		
		List<Veiculo> lista = this.obterVeiculosSessao();
		
		boolean add = true;
		
		for (int i = 0 ; i < lista.size() ; i++){
			
			if (lista.get(i).equals(veiculo)){
				
				lista.set(i, veiculo);
				add = false;
				break;
			}
		}
		
		if (veiculo.getId() == null){
			
			Long id = new Long((int) System.currentTimeMillis());
			
			veiculo.setId(id < 0 ? id : id *-1);
		}
		
		if (add){
		
			lista.add(veiculo);
		}
		
		this.httpSession.setAttribute(LISTA_VEICULOS_SALVAR_SESSAO, lista);
		
		this.result.use(Results.json()).from("", "result").serialize();
	}
	
	private void validarDadosEntradaVeiculo(Veiculo veiculo) {
		
		if (veiculo == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Veículo é obrigatório.");
		}
		
		List<String> msgs = new ArrayList<String>();
		
		if (veiculo.getTipoVeiculo() == null || veiculo.getTipoVeiculo().trim().isEmpty()){
			
			msgs.add("Tipo de Veículo é obrigatório.");
		}
		
		if (veiculo.getPlaca() == null || veiculo.getPlaca().trim().isEmpty()){
			
			msgs.add("Placa é obrigatório.");
		}
		
		if (!msgs.isEmpty()){
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, msgs));
		}
	}

	@Post
	public void editarVeiculo(Long referencia){
		
		Veiculo veiculo = null;
		
		List<Veiculo> listaVeiculos = this.obterVeiculosSessao();
		
		for (Veiculo v : listaVeiculos){
			
			if ((v.getId().equals(referencia))){
				
				veiculo = v;
				break;
			}
		}
		
		if (veiculo == null){
			
			veiculo = this.transportadorService.buscarVeiculoPorId(referencia);
			
			if (veiculo == null){
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Veículo de Id: " + referencia + " não encontrado.");
			}
		}
		
		this.result.use(Results.json()).from(veiculo, "result").serialize();
	}
	
	@Post
	public void excluirVeiculo(Long referencia){
		
		if (referencia != null){
			
			List<AssociacaoVeiculoMotoristaRotaDTO> assocSalvar = this.obterAssociacoesSalvarSessao();
			
			for (AssociacaoVeiculoMotoristaRotaDTO asc : assocSalvar){
				
				if (referencia.equals(asc.getVeiculo().getId())){
					
					this.result.use(Results.json()).from(
							new ValidacaoVO(
									TipoMensagem.WARNING, 
									"Exclusão não permitida, esse motorista ja faz parte de uma associação."), "result").recursive().serialize();
					return;
				}
			}
			
			Set<Long> assocRemover = this.obterAssociacoesRemoverSessao();
			
			if (this.transportadorService.verificarAssociacaoVeiculo(referencia, assocRemover)){
				
				this.result.use(Results.json()).from(
						new ValidacaoVO(
								TipoMensagem.WARNING, 
								"Exclusão não permitida, esse motorista ja faz parte de uma associação."), "result").recursive().serialize();
				return;
			}
			
			Set<Long> set = this.obterVeiculosSessaoRemover();
			
			set.add(referencia);
			
			this.httpSession.setAttribute(LISTA_VEICULOS_REMOVER_SESSAO, set);
			
			List<Veiculo> listaSalvar = this.obterVeiculosSessao();
			
			Veiculo v = new Veiculo();
			v.setId(referencia);
			
			listaSalvar.remove(v);
			
			this.httpSession.setAttribute(LISTA_VEICULOS_SALVAR_SESSAO, listaSalvar);
		}
		
		this.result.use(Results.json()).from("", "result").serialize();
	}
	
	@SuppressWarnings("unchecked")
	private Set<Long> obterVeiculosSessaoRemover() {
		
		Set<Long> set = (Set<Long>) this.httpSession.getAttribute(LISTA_VEICULOS_REMOVER_SESSAO);
		
		return set == null ? new HashSet<Long>() : set;
	}

	@SuppressWarnings("unchecked")
	private List<Veiculo> obterVeiculosSessao(){
		
		List<Veiculo> listaVeiculos = (List<Veiculo>) 
				this.httpSession.getAttribute(LISTA_VEICULOS_SALVAR_SESSAO);
		
		return listaVeiculos == null ? new ArrayList<Veiculo>() : listaVeiculos;
	}
	
	@Post
	public List<Motorista> pesquisarMotoristas(String sortname, String sortorder){
		
		Long idTransportadorEdicao = (Long) this.httpSession.getAttribute(ID_TRANSPORTADORA_EDICAO);
		
		if (idTransportadorEdicao == null){
			
			return this.obterMotoristasSessao();
		} else {
			
			List<Motorista> motoristasSalvar = this.obterMotoristasSessao();
			
			Set<Long> idsIgnorar = new HashSet<Long>();
			
			for (Motorista motoristaSalvar : motoristasSalvar){
				
				if (motoristaSalvar.getId() != null){
					
					idsIgnorar.add(motoristaSalvar.getId());
				}
			}
			
			idsIgnorar.addAll(this.obterMotoristasSessaoRemover());
			
			List<Motorista> lista = 
					this.transportadorService.buscarMotoristasPorTransportador(
							idTransportadorEdicao, 
							idsIgnorar,
							sortname,
							sortorder);
			
			if (lista == null){
				lista =  new ArrayList<Motorista>();
			}
			
			for (Motorista motorista : lista){
				
				motorista.setTransportador(null);
			}
			
			lista.addAll(this.obterMotoristasSessao());
			
			return lista;
		}
	}
	
	@Post
	public void carregarMotoristas(String sortname, String sortorder){
		
		this.result.use(
				Results.json()).from(
						this.getTableModelMotoristas(
								this.pesquisarMotoristas(sortname, sortorder)), "result").recursive().serialize();
	}
	
	@Post
	public void adicionarMotorista(Motorista motorista){
		
		this.validarDadosEntradaMotorista(motorista);
		
		List<Motorista> lista = this.obterMotoristasSessao();
		
		boolean add = true;
		
		for (int i = 0 ; i < lista.size() ; i++){
			
			if (lista.get(i).equals(motorista)){
				
				add = false;
				lista.set(i, motorista);
				break;
			}
		}
		
		if (motorista.getId() == null){
			
			Long id = new Long((int) System.currentTimeMillis());
			
			motorista.setId(id < 0 ? id : id * -1);
		}
		
		if (add){
		
			lista.add(motorista);
		}
		
		this.httpSession.setAttribute(LISTA_MOTORISTAS_SALVAR_SESSAO, lista);
		
		this.result.use(Results.json()).from("", "result").serialize();
	}
	
	private void validarDadosEntradaMotorista(Motorista motorista) {
		
		if (motorista == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Motorista é obrigatório.");
		}
		
		List<String> msgs = new ArrayList<String>();
		
		if (motorista.getNome() == null || motorista.getNome().trim().isEmpty()){
			
			msgs.add("Nome motorista é obrigatório.");
		}
		
		if (motorista.getCnh() == null || motorista.getCnh().trim().isEmpty()){
			
			msgs.add("CNH é obrigatório.");
		}
		
		if (!msgs.isEmpty()){
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, msgs));
		}
	}

	@SuppressWarnings("unchecked")
	private List<Motorista> obterMotoristasSessao() {
		
		List<Motorista> listaVeiculos = (List<Motorista>) 
				this.httpSession.getAttribute(LISTA_MOTORISTAS_SALVAR_SESSAO);
		
		return listaVeiculos == null ? new ArrayList<Motorista>() : listaVeiculos;
	}

	@Post
	public void editarMotorista(Long referencia){
		
		Motorista motorista = null;
		
		List<Motorista> lista = this.obterMotoristasSessao();
		
		for (Motorista m : lista){
			
			if (m.getId().equals(referencia)){
				
				motorista = m;
				break;
			}
		}
		
		if (motorista == null){
			
			motorista = this.transportadorService.buscarMotoristaPorId(referencia);
			
			if (motorista == null){
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Motorista de Id: " + referencia + " não encontrado.");
			}
		}
		
		this.result.use(Results.json()).from(motorista, "result").serialize();
	}
	
	@Post
	public void excluirMotorista(Long referencia){
		
		if (referencia != null){
			
			List<AssociacaoVeiculoMotoristaRotaDTO> assocSalvar = this.obterAssociacoesSalvarSessao();
			
			for (AssociacaoVeiculoMotoristaRotaDTO asc : assocSalvar){
				
				if (referencia.equals(asc.getMotorista().getId())){
					
					this.result.use(Results.json()).from(
							new ValidacaoVO(
									TipoMensagem.WARNING, 
									"Exclusão não permitida, esse motorista ja faz parte de uma associação."), "result").recursive().serialize();
					return;
				}
			}
			
			Set<Long> assocRemover = this.obterAssociacoesRemoverSessao();
			
			if (this.transportadorService.verificarAssociacaoMotorista(referencia, assocRemover)){
				
				this.result.use(Results.json()).from(
						new ValidacaoVO(
								TipoMensagem.WARNING, 
								"Exclusão não permitida, esse motorista ja faz parte de uma associação."), "result").recursive().serialize();
				return;
			}
			
			Set<Long> set = this.obterMotoristasSessaoRemover();
			
			set.add(referencia);
			
			this.httpSession.setAttribute(LISTA_MOTORISTAS_REMOVER_SESSAO, set);
			
			List<Motorista> listaSalvar = this.obterMotoristasSessao();
			
			Motorista v = new Motorista();
			v.setId(referencia);
			
			listaSalvar.remove(v);
			
			this.httpSession.setAttribute(LISTA_MOTORISTAS_SALVAR_SESSAO, listaSalvar);
		}
		
		this.result.use(Results.json()).from("", "result").serialize();
	}
	
	@SuppressWarnings("unchecked")
	private Set<Long> obterMotoristasSessaoRemover() {
		
		Set<Long> set = (Set<Long>) this.httpSession.getAttribute(LISTA_MOTORISTAS_REMOVER_SESSAO);
		
		return set == null ? new HashSet<Long>() : set;
	}

	@Post
	public List<RotaRoteiroDTO> pesquisarRotas(String sortname, String sortorder){
		
		List<RotaRoteiroDTO> lista = this.transportadorService.buscarRotasRoteiroAssociacao(sortname, sortorder);
		
		if (lista == null){
			
			lista = new ArrayList<RotaRoteiroDTO>();
		}
		
		return lista;
	}
	
	@Post
	public void carregarRotas(String sortname, String sortorder){
		
		this.result.use(
				Results.json()).from(
						this.getTableModelRotaRoteiro(
								this.processarRotaRoteiroDisponivel(sortname, sortorder)), "result").recursive().serialize();
	}
	
	private List<RotaRoteiroDTO> processarRotaRoteiroDisponivel(String sortname, String sortorder){
		
		List<RotaRoteiroDTO> rotas = this.pesquisarRotas(sortname, sortorder);
		
		Set<Long> assocRemovidas = this.obterAssociacoesRemoverSessao();
		
		List<Long> idsRotasRemovidas = null;
		
		if (assocRemovidas != null && !assocRemovidas.isEmpty()){
			
			idsRotasRemovidas = this.transportadorService.buscarIdsRotasPorAssociacao(assocRemovidas);
		}
		
		for (RotaRoteiroDTO rota : rotas){
			
			for (AssociacaoVeiculoMotoristaRotaDTO dto : this.obterAssociacoesSalvarSessao()){
				
				if (dto.getRota().getIdRota().equals(rota.getIdRota())){
					
					rota.setDisponivel(false);
					break;
				} else {
					
					rota.setDisponivel(true);
					continue;
				}
			}
			
			if (idsRotasRemovidas != null && idsRotasRemovidas.contains(rota.getIdRota())){
				
				rota.setDisponivel(true);
			} else if (this.transportadorService.verificarAssociacaoRotaRoteiro(rota.getIdRota())) {
				
				rota.setDisponivel(false);
			}
		}
		
		return rotas;
	}
	
	@Post
	public void cadastrarAssociacao(Veiculo veiculo, Motorista motorista, List<RotaRoteiroDTO> rotas){
		
		List<AssociacaoVeiculoMotoristaRotaDTO> lista = this.obterAssociacoesSalvarSessao();
		
		if (rotas != null && !rotas.isEmpty()){
			
			for (RotaRoteiroDTO dto : rotas){
				
				lista.add(new AssociacaoVeiculoMotoristaRotaDTO(null, veiculo, motorista, dto));
			}
			
			this.httpSession.setAttribute(LISTA_ASSOCIACOES_SALVAR_SESSAO, lista);
		}
		
		this.result.use(Results.json()).from(this.getTableModelAssociacoes(this.pesquisarAssociacoes(null, null)), "result").recursive().serialize();
	}
	
	@SuppressWarnings("unchecked")
	private List<AssociacaoVeiculoMotoristaRotaDTO> obterAssociacoesSalvarSessao() {
		
		List<AssociacaoVeiculoMotoristaRotaDTO> lista = (List<AssociacaoVeiculoMotoristaRotaDTO>) 
				this.httpSession.getAttribute(LISTA_ASSOCIACOES_SALVAR_SESSAO);
		
		return lista == null ? new ArrayList<AssociacaoVeiculoMotoristaRotaDTO>() : lista ;
	}
	
	@Post
	public List<AssociacaoVeiculoMotoristaRotaDTO> pesquisarAssociacoes(String sortname, String sortorder){
		
		Long idTransportador = (Long) this.httpSession.getAttribute(ID_TRANSPORTADORA_EDICAO);
		
		List<AssociacaoVeiculoMotoristaRotaDTO> listaExibir = new ArrayList<AssociacaoVeiculoMotoristaRotaDTO>();
		
		List<AssociacaoVeiculoMotoristaRotaDTO> listaSalvar = this.obterAssociacoesSalvarSessao();
		
		if (idTransportador != null){
			
			Set<Long> idsIgnorar = new HashSet<Long>();
			
			idsIgnorar.addAll(this.obterAssociacoesRemoverSessao());
			
			for (AssociacaoVeiculoMotoristaRotaDTO assoc : listaSalvar){
				
				if (assoc.getId() != null && assoc.getId() > 0){
					
					idsIgnorar.add(assoc.getId());
				}
			}
			
			List<AssociacaoVeiculoMotoristaRota> listaAssocsBanco =
					this.transportadorService.buscarAssociacoesTransportador(idTransportador, idsIgnorar, sortname, sortorder);
			
			for (AssociacaoVeiculoMotoristaRota assocBanco : listaAssocsBanco){
				
				assocBanco.getMotorista().setTransportador(null);
				assocBanco.getVeiculo().setTransportador(null);
				assocBanco.setTransportador(null);
				
				AssociacaoVeiculoMotoristaRotaDTO dto = 
						new AssociacaoVeiculoMotoristaRotaDTO(assocBanco.getId(), 
								this.obterVeiculoEditado(assocBanco.getVeiculo()), 
								this.obterMotoristaEditado(assocBanco.getMotorista()), 
								new  RotaRoteiroDTO(1L, assocBanco.getRota().getDescricaoRota(), assocBanco.getRota().getRoteiro().getDescricaoRoteiro()));
				
				listaExibir.add(dto);
			}
		}
		
		for (AssociacaoVeiculoMotoristaRotaDTO dto : listaSalvar){
			
			dto.setVeiculo(this.obterVeiculoEditado(dto.getVeiculo()));
			dto.setMotorista(this.obterMotoristaEditado(dto.getMotorista()));
		}
		
		listaExibir.addAll(listaSalvar);
		
		return listaExibir;
	}
	
	private Veiculo obterVeiculoEditado(Veiculo veiculoParam){
		
		List<Veiculo> listaVeiculoSalvar = this.obterVeiculosSessao();
		
		for (Veiculo veiculo : listaVeiculoSalvar){
			
			if (veiculo.getId().equals(veiculoParam.getId())){
				
				return veiculo;
			}
		}
		
		return veiculoParam;
	}
	
	private Motorista obterMotoristaEditado(Motorista motoristaParam){
		
		List<Motorista> listaMotoristaSalvar = this.obterMotoristasSessao();
		
		for (Motorista motorista : listaMotoristaSalvar){
			
			if (motorista.getId().equals(motoristaParam.getId())){
				
				return motorista;
			}
		}
		
		return motoristaParam;
	}
	
	@Post
	public void carregarAssociacoes(String sortname, String sortorder){
		
		this.result.use(
				Results.json()).from(
						this.getTableModelAssociacoes(
								this.pesquisarAssociacoes(sortname, sortorder)), "result").recursive().serialize();
	}
	
	@Post
	public void excluirAssociacao(Long referencia){
		
		Set<Long> removerAssoc = this.obterAssociacoesRemoverSessao();
		
		if (referencia != null){
			
			removerAssoc.add(referencia);
		}
		
		this.httpSession.setAttribute(LISTA_ASSOCIACOES_REMOVER_SESSAO, removerAssoc);
		
		List<AssociacaoVeiculoMotoristaRotaDTO> listaAssocSalvar = this.obterAssociacoesSalvarSessao();
		
		AssociacaoVeiculoMotoristaRotaDTO assoc = new AssociacaoVeiculoMotoristaRotaDTO(referencia, null, null, null);
		
		listaAssocSalvar.remove(assoc);
		
		this.httpSession.setAttribute(LISTA_ASSOCIACOES_SALVAR_SESSAO, listaAssocSalvar);
		
		this.processarRotaRoteiroDisponivel(null, null);
		
		this.result.use(Results.json()).from("", "result").serialize();
	}
	
	@SuppressWarnings("unchecked")
	private Set<Long> obterAssociacoesRemoverSessao() {
		
		Set<Long> remover = (Set<Long>) this.httpSession.getAttribute(LISTA_ASSOCIACOES_REMOVER_SESSAO);
		
		return remover == null ? new HashSet<Long>() : remover;
	}

	@Post
	public void buscarPessoaCNPJ(String cnpj){
		
		List<String> dados = null;
		
		if (cnpj != null){
			
			cnpj = cnpj.replace("-", "").replace(".", "").replace("/", "");
			
			Transportador transportador = this.transportadorService.obterTransportadorPorCNPJ(cnpj);
			
			if (transportador != null){
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Já existe um transportador cadastrado com esse CNPJ.");
			}
			
			PessoaJuridica juridica = this.pessoaService.buscarPessoaPorCNPJ(cnpj);
			
			if (juridica != null){
				dados = new ArrayList<String>();
				
				dados.add(juridica.getRazaoSocial());
				dados.add(juridica.getNomeFantasia());
				dados.add(juridica.getEmail());
				dados.add(juridica.getInscricaoEstadual());
				
				this.carregarTelefonesEnderecosPessoa(juridica.getId());
			}
		}
		
		this.result.use(Results.json()).from(dados == null ? "" : dados, "result").recursive().serialize();
	}
	
	/**
	 * Obtem os endereços da sessão referente ao transportador informado
	 * 
	 * @param idTransportador - identificador do transportador
	 */
	private void obterEndereco(Long idTransportador){
		
		Boolean enderecoPendente = (Boolean) this.httpSession.getAttribute(EnderecoController.ENDERECO_PENDENTE);
		
		if (enderecoPendente==null || !enderecoPendente){
			
			List<EnderecoAssociacaoDTO> listaEnderecoAssociacao = this.transportadorService.buscarEnderecosTransportador(idTransportador, null);
		
			this.httpSession.setAttribute(LISTA_ENDERECOS_EXIBICAO, listaEnderecoAssociacao);
		}
	}
	
	/**
	 * Obtem os telefones da sessão referente ao transportador informado
	 * 
	 * @param idTransportador - identificador do transportador
	 */
	private void obterTelefones(Long idTransportador){
		
		List<TelefoneAssociacaoDTO> listaTelefoneAssociacao = this.transportadorService.buscarTelefonesTransportador(idTransportador, null);
		
		this.httpSession.setAttribute(LISTA_TELEFONES_EXIBICAO, listaTelefoneAssociacao);
	}
	
	/**
	 * Carrega telefones e endereços do transportador
	 * 
	 * @param id
	 */
	private void carregarTelefonesEnderecosPessoa(Long id) {
		
		this.obterTelefones(id);
		
        this.obterEndereco(id);
	}
	
	@Post
	public void carregarCotasAtendidas(String sortorder, String sortname){
		
		List<CotaAtendidaTransportadorVO> cotasAtendidas;
		
		Long idTransportador = (Long) this.httpSession.getAttribute(ID_TRANSPORTADORA_EDICAO);
		
		if (idTransportador == null){
			
			cotasAtendidas = new ArrayList<CotaAtendidaTransportadorVO>();
		} else {
			
			cotasAtendidas = 
					this.transportadorService.buscarCotasAtendidadas(idTransportador, sortorder, sortname);
		}
		
		result.use(FlexiGridJson.class).from(cotasAtendidas).page(1).total(cotasAtendidas.size()).serialize();
	}
	
	@Get
	public void exportarCotasAtendidas(FileType fileType, String sortorder, String sortname) throws IOException{
		
		List<CotaAtendidaTransportadorVO> cotasAtendidas;
		
		Long idTransportador = (Long) this.httpSession.getAttribute(ID_TRANSPORTADORA_EDICAO);
		
		if (idTransportador == null){
			
			cotasAtendidas = new ArrayList<CotaAtendidaTransportadorVO>();
		} else {
			
			cotasAtendidas = 
					this.transportadorService.buscarCotasAtendidadas(idTransportador, sortorder, sortname);
		}
		
		FileExporter.to("cotas_atendidas", fileType)
			.inHTTPResponse(this.getNDSFileHeader(), null, null, 
					cotasAtendidas, CotaAtendidaTransportadorVO.class, this.httpResponse);
	}
	
	private TableModel<CellModelKeyValue<Veiculo>> getTableModelVeiculos(List<Veiculo> listaVeiculos) {
		
		TableModel<CellModelKeyValue<Veiculo>> tableModel = new TableModel<CellModelKeyValue<Veiculo>>();

		List<CellModelKeyValue<Veiculo>> listaCellModel = new ArrayList<CellModelKeyValue<Veiculo>>();

		for (Veiculo veiculo : listaVeiculos) {
			
			if (veiculo.getId() == null){
				
				int id  = (int)System.currentTimeMillis() * -1;
				
				veiculo.setId(new Long(id));
			}
			
			CellModelKeyValue<Veiculo> cellModel = new CellModelKeyValue<Veiculo>(
				veiculo.getId().intValue(),
				veiculo
			);

			listaCellModel.add(cellModel);
		}

		tableModel.setPage(1);
		tableModel.setRows(listaCellModel);
		tableModel.setTotal(listaVeiculos.size()); 
		
		return tableModel;
	}
	
	private TableModel<CellModelKeyValue<Motorista>> getTableModelMotoristas(List<Motorista> listaMotoristas) {
		
		TableModel<CellModelKeyValue<Motorista>> tableModel = new TableModel<CellModelKeyValue<Motorista>>();

		List<CellModelKeyValue<Motorista>> listaCellModel = new ArrayList<CellModelKeyValue<Motorista>>();

		for (Motorista motorista : listaMotoristas) {
			
			if (motorista.getId() == null){
				
				int id  = (int)System.currentTimeMillis() * -1;
				
				motorista.setId(new Long(id));
			}
			
			CellModelKeyValue<Motorista> cellModel = new CellModelKeyValue<Motorista>(
				motorista.getId().intValue(),
				motorista
			);

			listaCellModel.add(cellModel);
		}

		tableModel.setPage(1);
		tableModel.setRows(listaCellModel);
		tableModel.setTotal(listaMotoristas.size()); 
		
		return tableModel;
	}
	
	private TableModel<CellModelKeyValue<RotaRoteiroDTO>> getTableModelRotaRoteiro(List<RotaRoteiroDTO> listaRotas) {
		
		TableModel<CellModelKeyValue<RotaRoteiroDTO>> tableModel = new TableModel<CellModelKeyValue<RotaRoteiroDTO>>();

		List<CellModelKeyValue<RotaRoteiroDTO>> listaCellModel = new ArrayList<CellModelKeyValue<RotaRoteiroDTO>>();

		for (RotaRoteiroDTO rota : listaRotas) {
			
			int id  = rota.getIdRota() == null ? (int)System.currentTimeMillis() * -1 : rota.getIdRota().intValue();
			
			CellModelKeyValue<RotaRoteiroDTO> cellModel = new CellModelKeyValue<RotaRoteiroDTO>(
				id,
				rota
			);

			listaCellModel.add(cellModel);
		}

		tableModel.setPage(1);
		tableModel.setRows(listaCellModel);
		tableModel.setTotal(listaRotas.size()); 
		
		return tableModel;
	}
	
	private TableModel<CellModelKeyValue<AssociacaoVeiculoMotoristaRotaDTO>> getTableModelAssociacoes(List<AssociacaoVeiculoMotoristaRotaDTO> listaRotas) {
		
		TableModel<CellModelKeyValue<AssociacaoVeiculoMotoristaRotaDTO>> tableModel = 
				new TableModel<CellModelKeyValue<AssociacaoVeiculoMotoristaRotaDTO>>();

		List<CellModelKeyValue<AssociacaoVeiculoMotoristaRotaDTO>> listaCellModel = 
				new ArrayList<CellModelKeyValue<AssociacaoVeiculoMotoristaRotaDTO>>();

		for (AssociacaoVeiculoMotoristaRotaDTO rota : listaRotas) {
			
			CellModelKeyValue<AssociacaoVeiculoMotoristaRotaDTO> cellModel = 
					new CellModelKeyValue<AssociacaoVeiculoMotoristaRotaDTO>(
				
				rota.getId().intValue(),
				rota
			);

			listaCellModel.add(cellModel);
		}

		tableModel.setPage(1);
		tableModel.setRows(listaCellModel);
		tableModel.setTotal(listaRotas.size()); 
		
		return tableModel;
	}
	
	private NDSFileHeader getNDSFileHeader() {
		
		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		List<String> dados = this.distribuidorService.obterNomeCNPJDistribuidor();
		
		if (dados.size() == 2) {
			
			ndsFileHeader.setNomeDistribuidor(dados.get(0));
			ndsFileHeader.setCnpjDistribuidor(dados.get(1));
		}
		
		ndsFileHeader.setData(new Date());
		ndsFileHeader.setNomeUsuario(this.obterUsuario().getNome());
		return ndsFileHeader;
	}
	
	private Usuario obterUsuario() {
		//TODO: obter usuário
		Usuario usuario = new Usuario();
		usuario.setNome("João");
		usuario.setId(1L);
		return usuario;
	}
}