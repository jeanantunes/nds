package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFiadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaTransportadorDTO;
import br.com.abril.nds.model.cadastro.Motorista;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Transportador;
import br.com.abril.nds.model.cadastro.Veiculo;
import br.com.abril.nds.service.MotoristaService;
import br.com.abril.nds.service.VeiculoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/cadastro/transportador")
public class TransportadorController {

	@Autowired
	private MotoristaService motoristaService;
	
	@Autowired
	private VeiculoService veiculoService;
	
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
	
	public void pesquisarTransportadores(FiltroConsultaTransportadorDTO filtro, String sortorder, 
			String sortname, Integer page, Integer rp, ValidacaoVO validacaoVO){
		
	}
	
	public void cadastrarTransportador(Transportador transportador){
		
	}
	
	public void editarTransportador(Long referencia){
		
	}
	
	public void excluirTransportador(Long referencia){
		
	}
	
	public void cadastrarVeiculo(Veiculo veiculo){
		
		this.veiculoService.cadastarVeiculo(veiculo);
	}
	
	public void editarVeiculo(Long referencia){
		
	}
	
	public void excluirVeiculo(Long referencia){
		
		this.excluirVeiculo(referencia);
	}
	
	public void cadastrarMotorista(Motorista motorista){
		
		this.motoristaService.cadastarMotorista(motorista);
	}
	
	public void editarMotorista(Long referencia){
		
	}
	
	public void excluirMotorista(Long referencia){
		
		this.motoristaService.excluirMotorista(referencia);
	}
	
	public void cadastrarAssociacao(Long referenciaVeiculo, Long referenciaMotorista, List<Long> listaRotaRoteiro){
		
	}
	
	public void excluirAssociacao(Long referencia){
		
	}
	
	public void buscarPessoaTransportadorPorCNPJ(String cnpj){
		
	}
	
	private TableModel<Object> getTableModelTransportadores(List<Object> lista){
		
		TableModel<Object> table = new TableModel<Object>();
		List<Object> listaCell = new ArrayList<Object>();
		for (Object o : lista){
			CellModelKeyValue<Object> cell = new CellModelKeyValue<Object>(0, o);
			
			listaCell.add(cell);
		}
		
		table.setRows(listaCell);
		table.setTotal(1);
		table.setPage(1);
		
		return table;
	}
}