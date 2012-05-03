package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ConsultaTransportadorDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaTransportadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaTransportadorDTO.OrdenacaoColunaTransportador;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Motorista;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Transportador;
import br.com.abril.nds.model.cadastro.Veiculo;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.MotoristaService;
import br.com.abril.nds.service.PessoaService;
import br.com.abril.nds.service.TransportadorService;
import br.com.abril.nds.service.VeiculoService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
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
	
	@Autowired
	private MotoristaService motoristaService;
	
	@Autowired
	private VeiculoService veiculoService;
	
	@Autowired
	private TransportadorService transportadorService;
	
	@Autowired
	PessoaService pessoaService;
	
	private final String FILTRO_PESQUISA_TRANSPORTADORES = "filtroPesquisaTransportadores";
	
	private Result result;
	
	private HttpSession httpSession;
	
	public TransportadorController(Result result, HttpSession httpSession){
		
		this.result = result;
		this.httpSession = httpSession;
	}
	
	@Path("/")
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
		this.httpSession.removeAttribute(ID_TRANSPORTADORA_EDICAO);
	}

	@Post
	public void pesquisarTransportadores(FiltroConsultaTransportadorDTO filtro, String sortorder, 
			String sortname, Integer page, Integer rp, ValidacaoVO validacaoVO){
		
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
		
		ConsultaTransportadorDTO consulta = this.transportadorService.consultarTransportadores(filtro);
		
		if (consulta.getTransportadores().isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} else {
		
			this.result.use(FlexiGridJson.class)
				.from(consulta.getTransportadores())
				.total(consulta.getTransportadores().size())
				.page(page == null ? 1 : page).serialize();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Post
	public void cadastrarTransportador(Transportador transportador){
		
		this.validarDadosEntrada(transportador);
		
		List<EnderecoAssociacaoDTO> listaEnderecosSalvar = (List<EnderecoAssociacaoDTO>) 
				this.httpSession.getAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
		
		List<EnderecoAssociacaoDTO> lisEndRemover = (List<EnderecoAssociacaoDTO>) 
				this.httpSession.getAttribute(LISTA_ENDERECOS_REMOVER_SESSAO);
		
		Set<Long> listaEnderecosRemover = new HashSet<Long>();
		
		for (EnderecoAssociacaoDTO dto : lisEndRemover){
			
			if (dto.getEndereco() != null && dto.getEndereco().getId() != null){
				
				listaEnderecosRemover.add(dto.getEndereco().getId());
			}
		}
		
		List<TelefoneAssociacaoDTO> listaTelefonesSalvar = (List<TelefoneAssociacaoDTO>) 
				this.httpSession.getAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		
		Set<Long> listaTelefonesRemover = (Set<Long>) 
				this.httpSession.getAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
		
		this.transportadorService.cadastrarTransportador(transportador, 
				listaEnderecosSalvar, 
				listaEnderecosRemover, 
				listaTelefonesSalvar, 
				listaTelefonesRemover, 
				null, 
				null);
	}
	
	private void validarDadosEntrada(Transportador transportador) {
		
		
	}

	public void editarTransportador(Long referencia){
		
	}
	
	public void excluirTransportador(Long referencia){
		
	}
	
	@Post
	public void cancelarCadastro(){
		
		this.limparDadosSessao();
		
		result.use(Results.json()).from("", "result").serialize();
	}
	
	@Post
	public void carregarTelaAssociacao(){
		
		Object[] dados = new Object[3];
		
		dados[0] = this.result.use(FlexiGridJson.class)
				.from(this.pesquisarVeiculos())
				.total(this.pesquisarVeiculos().size())
				.page(1).serialize();
		
		dados[1] = this.result.use(FlexiGridJson.class)
				.from(this.carregarMotoristas())
				.total(this.carregarMotoristas().size())
				.page(1).serialize();
		
		this.result.use(Results.json()).from(dados, "result");
	}
	
	@Post
	public void carregarVeiculos(){
		
		this.result.use(Results.json()).from(this.pesquisarVeiculos(), "result");
	}
	
	private List<Veiculo> pesquisarVeiculos(){
		
		List<Veiculo> lista = this.veiculoService.buscarVeiculos();
		
		return lista == null ? new ArrayList<Veiculo>() : lista;
	}
	
	@Post
	public void cadastrarVeiculo(Veiculo veiculo){
		
		this.veiculoService.cadastarVeiculo(veiculo);
		
		this.carregarVeiculos();
	}
	
	@Post
	public void editarVeiculo(Long referencia){
		
		Veiculo veiculo = this.veiculoService.buscarVeiculoPorId(referencia);
		
		if (veiculo == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Veículo de Id: " + referencia + " não encontrado.");
		}
		
		this.result.use(Results.json()).from(veiculo, "result");
	}
	
	@Post
	public void excluirVeiculo(Long referencia){
		
		this.excluirVeiculo(referencia);
		
		this.carregarVeiculos();
	}
	
	@Post
	public void pesquisarMotoristas(){
		
		this.result.use(Results.json()).from(this.carregarMotoristas(), "result");
	}
	
	private List<Motorista> carregarMotoristas(){
		
		List<Motorista> lista = this.motoristaService.buscarMotoristas();
		
		return lista == null ? new ArrayList<Motorista>() : lista;
	}
	
	@Post
	public void cadastrarMotorista(Motorista motorista){
		
		this.motoristaService.cadastarMotorista(motorista);
		
		this.carregarMotoristas();
	}
	
	@Post
	public void editarMotorista(Long referencia){
		
		Motorista motorista = this.motoristaService.buscarMotoristaPorId(referencia);
		
		if (motorista == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Motorista de Id: " + referencia + " não encontrado.");
		}
		
		this.result.use(Results.json()).from(motorista, "result");
	}
	
	@Post
	public void excluirMotorista(Long referencia){
		
		this.motoristaService.excluirMotorista(referencia);
		
		this.carregarMotoristas();
	}
	
	public void cadastrarAssociacao(Long referenciaVeiculo, Long referenciaMotorista, List<Long> listaRotaRoteiro){
		
	}
	
	public void excluirAssociacao(Long referencia){
		
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
	
	private void carregarTelefonesEnderecosPessoa(Long id) {
		// TODO Auto-generated method stub
		
	}
}