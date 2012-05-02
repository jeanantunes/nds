package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ConsultaTransportadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaTransportadorDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Motorista;
import br.com.abril.nds.model.cadastro.Transportador;
import br.com.abril.nds.model.cadastro.Veiculo;
import br.com.abril.nds.service.MotoristaService;
import br.com.abril.nds.service.TransportadorService;
import br.com.abril.nds.service.VeiculoService;
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

	@Autowired
	private MotoristaService motoristaService;
	
	@Autowired
	private VeiculoService veiculoService;
	
	@Autowired
	private TransportadorService transportadorService;
	
	private final String FILTRO_PESQUISA_TRANSPORTADORES = "filtroPesquisaTransportadores";
	
	private Result result;
	
	private HttpSession httpSession;
	
	public TransportadorController(Result result, HttpSession httpSession){
		
		this.result = result;
		this.httpSession = httpSession;
	}
	
	@Path("/")
	public void index(){
		
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
		
		ConsultaTransportadorDTO consulta = this.transportadorService.consultarTransportadores(filtro);
		
		this.result.use(Results.json()).from(consulta.getTransportadores(), "result").recursive().serialize();
	}
	
	public void cadastrarTransportador(Transportador transportador){
		
	}
	
	public void editarTransportador(Long referencia){
		
	}
	
	public void excluirTransportador(Long referencia){
		
	}
	
	@Post
	public void carregarTelaAssociacao(){
		
		Object[] dados = new Object[3];
		
		dados[0] = "";
		
		dados[1] = this.getTableModelVeiculos(this.pesquisarVeiculos());
		dados[2] = this.getTableModelMotoristas(this.carregarMotoristas());
		
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
	
	public void buscarPessoaTransportadorPorCNPJ(String cnpj){
		
	}
	
	private TableModel<CellModelKeyValue<Transportador>> getTableModelTransportadores(
			List<Transportador> lista, Integer page){
		
		TableModel<CellModelKeyValue<Transportador>> table = new TableModel<CellModelKeyValue<Transportador>>();
		
		List<CellModelKeyValue<Transportador>> listaCell = new ArrayList<CellModelKeyValue<Transportador>>();
		for (Transportador transportador : lista){
			
			CellModelKeyValue<Transportador> cell = 
					new CellModelKeyValue<Transportador>(transportador.getId().intValue(), transportador);
			
			listaCell.add(cell);
		}
		
		table.setRows(listaCell);
		table.setTotal(listaCell.size());
		table.setPage(page == null ? 1 : page);
		
		return table;
	}
	
	private TableModel<CellModelKeyValue<Veiculo>> getTableModelVeiculos(List<Veiculo> lista){
		
		TableModel<CellModelKeyValue<Veiculo>> table = new TableModel<CellModelKeyValue<Veiculo>>();
		
		List<CellModelKeyValue<Veiculo>> listaCell = new ArrayList<CellModelKeyValue<Veiculo>>();
		for (Veiculo veiculo : lista){
			
			CellModelKeyValue<Veiculo> cell = 
					new CellModelKeyValue<Veiculo>(veiculo.getId().intValue(), veiculo);
			
			listaCell.add(cell);
		}
		
		table.setRows(listaCell);
		table.setTotal(listaCell.size());
		table.setPage(1);
		
		return table;
	}
	
	private TableModel<CellModelKeyValue<Motorista>> getTableModelMotoristas(List<Motorista> lista){
		
		TableModel<CellModelKeyValue<Motorista>> table = new TableModel<CellModelKeyValue<Motorista>>();
		
		List<CellModelKeyValue<Motorista>> listaCell = new ArrayList<CellModelKeyValue<Motorista>>();
		for (Motorista motorista : lista){
			
			CellModelKeyValue<Motorista> cell = 
					new CellModelKeyValue<Motorista>(motorista.getId().intValue(), motorista);
			
			listaCell.add(cell);
		}
		
		table.setRows(listaCell);
		table.setTotal(listaCell.size());
		table.setPage(1);
		
		return table;
	}
}