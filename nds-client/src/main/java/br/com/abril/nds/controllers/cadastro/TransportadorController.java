package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.AssociacaoVeiculoMotoristaRotaDTO;
import br.com.abril.nds.dto.ConsultaTransportadorDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.RotaRoteiroDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaTransportadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaTransportadorDTO.OrdenacaoColunaTransportador;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Motorista;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Transportador;
import br.com.abril.nds.model.cadastro.Veiculo;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.PessoaService;
import br.com.abril.nds.service.TransportadorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
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
	
	private static final String LISTA_VEICULOS_SALVAR_SESSAO = "listaVeiculosSalvarSessao";
	
	private static final String LISTA_MOTORISTAS_SALVAR_SESSAO = "listaMotoristasSalvarSessao";
	
	private static final String LISTA_ASSOCIACOES_SALVAR_SESSAO = "listaAssociacoesSalvarSessao";
	
	private static final String LISTA_ASSOCIACOES_REMOVER_SESSAO = "listaAssociacoesRemoverSessao";
	
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
		this.httpSession.removeAttribute(LISTA_ASSOCIACOES_SALVAR_SESSAO);
		this.httpSession.removeAttribute(LISTA_ASSOCIACOES_REMOVER_SESSAO);
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
		
		List<EnderecoAssociacaoDTO> listaEnderecosSalvar = (List<EnderecoAssociacaoDTO>) 
				this.httpSession.getAttribute(LISTA_ENDERECOS_SALVAR_SESSAO);
		
		List<EnderecoAssociacaoDTO> lisEndRemover = (List<EnderecoAssociacaoDTO>) 
				this.httpSession.getAttribute(LISTA_ENDERECOS_REMOVER_SESSAO);
		
		Set<Long> listaEnderecosRemover = new HashSet<Long>();
		
		if (lisEndRemover != null){
			for (EnderecoAssociacaoDTO dto : lisEndRemover){
				
				if (dto.getEndereco() != null && dto.getEndereco().getId() != null){
					
					listaEnderecosRemover.add(dto.getEndereco().getId());
				}
			}
		}
		
		Map<Integer, TelefoneAssociacaoDTO> listTelSalvar = (Map<Integer, TelefoneAssociacaoDTO>) 
				this.httpSession.getAttribute(LISTA_TELEFONES_SALVAR_SESSAO);
		
		List<TelefoneAssociacaoDTO> listaTelefonesSalvar = new ArrayList<TelefoneAssociacaoDTO>();
		
		if (listTelSalvar != null){
			for (TelefoneAssociacaoDTO dto : listTelSalvar.values()){
				
				listaTelefonesSalvar.add(dto);
			}
		}
		
		Set<Long> listaTelefonesRemover = (Set<Long>) 
				this.httpSession.getAttribute(LISTA_TELEFONES_REMOVER_SESSAO);
		
		List<AssociacaoVeiculoMotoristaRotaDTO> listaAssocAdd = this.obterAssociacoesSalvarSessao();
		
		Set<Long> listaAssocRemover = this.obterAssociacoesRemoverSessao();
		
		this.transportadorService.cadastrarTransportador(transportador, 
				listaEnderecosSalvar, 
				listaEnderecosRemover, 
				listaTelefonesSalvar,
				listaTelefonesRemover, 
				listaAssocAdd, 
				listaAssocRemover);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."), "result").recursive().serialize();
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
		
		Object[] dados = new Object[4];
		
		dados[0] = this.getTableModelVeiculos(this.pesquisarVeiculos());
		
		dados[1] = this.getTableModelMotoristas(this.pesquisarMotoristas());
		
		dados[2] = this.getTableModelRotaRoteiro(this.processarRotaRoteiroDisponivel());
		
		dados[3] = this.getTableModelAssociacoes(this.pesquisarAssociacoes());
		
		this.result.use(Results.json()).from(dados, "result").serialize();
	}
	
	@Post
	public List<Veiculo> pesquisarVeiculos(){
		
		List<Veiculo> lista = this.transportadorService.buscarVeiculos();
		
		if (lista == null){
			lista =  new ArrayList<Veiculo>();
		}
		
		return lista;
	}
	
	@Post
	public void carregarVeiculos(){
		
		this.result.use(Results.json()).from(this.getTableModelVeiculos(this.pesquisarVeiculos()), "result").recursive().serialize();
	}
	
	@Post
	public void adicionarVeiculo(Veiculo veiculo){
		
		List<Veiculo> lista = this.obterVeiculosSessao();
		
		lista.add(veiculo);
		
		this.httpSession.setAttribute(LISTA_VEICULOS_SALVAR_SESSAO, lista);
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."), "result").recursive().serialize();
	}
	
	@Post
	public void cadastrarVeiculos(Veiculo veiculo){
		
		if (veiculo == null){
			
			this.transportadorService.cadastrarVeiculos(this.obterVeiculosSessao());
			
			this.httpSession.removeAttribute(LISTA_VEICULOS_SALVAR_SESSAO);
		} else {
			
			this.transportadorService.cadastrarVeiculo(veiculo);
		}
		
		this.carregarVeiculos();
	}
	
	@Post
	public void cancelarCadastroVeiculos(){
		
		this.httpSession.removeAttribute(LISTA_VEICULOS_SALVAR_SESSAO);
		
		this.carregarVeiculos();
	}
	
	@Post
	public void editarVeiculo(Long referencia){
		
		Veiculo veiculo = this.transportadorService.buscarVeiculoPorId(referencia);
		
		if (veiculo == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Veículo de Id: " + referencia + " não encontrado.");
		}
		
		this.result.use(Results.json()).from(veiculo, "result").serialize();
	}
	
	@Post
	public void excluirVeiculo(Long referencia){
		
		this.transportadorService.excluirVeiculo(referencia);
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."), "result").recursive().serialize();
	}
	
	@SuppressWarnings("unchecked")
	private List<Veiculo> obterVeiculosSessao(){
		
		List<Veiculo> listaVeiculos = (List<Veiculo>) 
				this.httpSession.getAttribute(LISTA_VEICULOS_SALVAR_SESSAO);
		
		return listaVeiculos == null ? new ArrayList<Veiculo>() : listaVeiculos;
	}
	
	@Post
	public List<Motorista> pesquisarMotoristas(){
		
		List<Motorista> lista = this.transportadorService.buscarMotoristas();
		
		if (lista == null){
			lista =  new ArrayList<Motorista>();
		}
		
		return lista;
	}
	
	@Post
	public void carregarMotoristas(){
		
		this.result.use(Results.json()).from(this.getTableModelMotoristas(this.pesquisarMotoristas()), "result").recursive().serialize();
	}
	
	@Post
	public void adicionarMotorista(Motorista motorista){
		
		List<Motorista> lista = this.obterMotoristasSessao();
		
		lista.add(motorista);
		
		this.httpSession.setAttribute(LISTA_MOTORISTAS_SALVAR_SESSAO, lista);
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."), "result").recursive().serialize();
	}
	
	@SuppressWarnings("unchecked")
	private List<Motorista> obterMotoristasSessao() {
		
		List<Motorista> listaVeiculos = (List<Motorista>) 
				this.httpSession.getAttribute(LISTA_MOTORISTAS_SALVAR_SESSAO);
		
		return listaVeiculos == null ? new ArrayList<Motorista>() : listaVeiculos;
	}

	@Post
	public void cadastrarMotoristas(Motorista motorista){
		
		if (motorista == null){
			
			this.transportadorService.cadastrarMotoristas(this.obterMotoristasSessao());
			
			this.httpSession.removeAttribute(LISTA_MOTORISTAS_SALVAR_SESSAO);
		} else {
			
			this.transportadorService.cadastarMotorista(motorista);
		}
		
		this.carregarMotoristas();
	}
	
	@Post
	public void cancelarCadastroMotoristas(){
		
		this.httpSession.removeAttribute(LISTA_MOTORISTAS_SALVAR_SESSAO);
		
		this.carregarMotoristas();
	}
	
	@Post
	public void editarMotorista(Long referencia){
		
		Motorista motorista = this.transportadorService.buscarMotoristaPorId(referencia);
		
		if (motorista == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Motorista de Id: " + referencia + " não encontrado.");
		}
		
		this.result.use(Results.json()).from(motorista, "result").serialize();
	}
	
	@Post
	public void excluirMotorista(Long referencia){
		
		this.transportadorService.excluirMotorista(referencia);
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."), "result").recursive().serialize();
	}
	
	@Post
	public List<RotaRoteiroDTO> pesquisarRotas(){
		
		List<Rota> lista = this.transportadorService.buscarRotas();
		
		if (lista == null || lista.isEmpty()){
			
			return new ArrayList<RotaRoteiroDTO>();
		} else {
			
			List<RotaRoteiroDTO> ret = new ArrayList<RotaRoteiroDTO>();
			
			for (Rota rota : lista){
				
				ret.add(new RotaRoteiroDTO(rota.getId(), rota.getDescricaoRota(), rota.getRoteiro().getDescricaoRoteiro()));
			}
			
			return ret;
		}
	}
	
	@Post
	public void carregarRotas(){
		
		this.result.use(Results.json()).from(this.getTableModelRotaRoteiro(this.processarRotaRoteiroDisponivel()), "result").recursive().serialize();
	}
	
	private List<RotaRoteiroDTO> processarRotaRoteiroDisponivel(){
		
		List<RotaRoteiroDTO> rotas = this.pesquisarRotas();
		
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
		
		this.result.use(Results.json()).from(this.getTableModelAssociacoes(this.obterAssociacoesSalvarSessao()), "result").recursive().serialize();
	}
	
	@SuppressWarnings("unchecked")
	private List<AssociacaoVeiculoMotoristaRotaDTO> obterAssociacoesSalvarSessao() {
		
		List<AssociacaoVeiculoMotoristaRotaDTO> lista = (List<AssociacaoVeiculoMotoristaRotaDTO>) 
				this.httpSession.getAttribute(LISTA_ASSOCIACOES_SALVAR_SESSAO);
		
		return lista == null ? new ArrayList<AssociacaoVeiculoMotoristaRotaDTO>() : lista ;
	}
	
	@Post
	public List<AssociacaoVeiculoMotoristaRotaDTO> pesquisarAssociacoes(){
		
		
		return this.obterAssociacoesSalvarSessao();
	}
	
	@Post
	public void carregarAssociacoes(){
		
		this.result.use(Results.json()).from(this.getTableModelAssociacoes(this.pesquisarAssociacoes()), "result").recursive().serialize();
	}
	
	@Post
	public void excluirAssociacao(Long referencia){
		
		Set<Long> removerAssoc = this.obterAssociacoesRemoverSessao();
		
		if (referencia != null){
			
			removerAssoc.add(referencia);
		}
		
		List<AssociacaoVeiculoMotoristaRotaDTO> listaAssocSalvar = this.obterAssociacoesSalvarSessao();
		
		AssociacaoVeiculoMotoristaRotaDTO assoc = new AssociacaoVeiculoMotoristaRotaDTO(referencia, null, null, null);
		
		listaAssocSalvar.remove(assoc);
		
		this.processarRotaRoteiroDisponivel();
		
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
	
	private void carregarTelefonesEnderecosPessoa(Long id) {
		// TODO Auto-generated method stub
		
	}
	
	private TableModel<CellModelKeyValue<Veiculo>> getTableModelVeiculos(List<Veiculo> listaVeiculos) {
		
		TableModel<CellModelKeyValue<Veiculo>> tableModel = new TableModel<CellModelKeyValue<Veiculo>>();

		List<CellModelKeyValue<Veiculo>> listaCellModel = new ArrayList<CellModelKeyValue<Veiculo>>();

		for (Veiculo veiculo : listaVeiculos) {
			
			int id  = veiculo.getId() == null ? (int)System.currentTimeMillis() : veiculo.getId().intValue();
			
			CellModelKeyValue<Veiculo> cellModel = new CellModelKeyValue<Veiculo>(
				id,
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
			
			int id  = motorista.getId() == null ? (int)System.currentTimeMillis() * -1 : motorista.getId().intValue();
			
			CellModelKeyValue<Motorista> cellModel = new CellModelKeyValue<Motorista>(
				id,
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
}