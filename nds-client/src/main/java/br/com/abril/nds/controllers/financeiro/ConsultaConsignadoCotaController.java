package br.com.abril.nds.controllers.financeiro;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaConsignadoCotaDTO;
import br.com.abril.nds.dto.ConsultaConsignadoCotaPeloFornecedorDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaConsignadoCotaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.service.ConsultaConsignadoCotaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/financeiro/consultaConsignadoCota")
public class ConsultaConsignadoCotaController {
	
	private static final String FILTRO_SESSION_ATTRIBUTE_CONSIGNADO_COTA = "filtroConsultaConsignadoCotaController";
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private ConsultaConsignadoCotaService consultaConsignadoCota;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Path("/")
	public void index(){
		this.carregarComboFornecedores();		
	}
	
	
	@Post
	@Path("pesquisarConsignadoCota")
	public void pesquisarConsignadoCota(FiltroConsultaConsignadoCotaDTO filtro, String sortorder, String sortname, int page, int rp){
		
		if(filtro.getIdFornecedor() == -1 || filtro.getIdFornecedor() == 0){
			filtro.setIdFornecedor(null);
		}

		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.validarEntrada(filtro);
		
		this.tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<ConsultaConsignadoCotaDTO>> tableModel = efetuarConsultaConsignadoCota(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	private TableModel<CellModelKeyValue<ConsultaConsignadoCotaDTO>> efetuarConsultaConsignadoCota(
			FiltroConsultaConsignadoCotaDTO filtro) {
		
		List<ConsultaConsignadoCotaDTO> listaConsignadoCota = this.consultaConsignadoCota.buscarConsignadoCota(filtro, "limitar");
		
		TableModel<CellModelKeyValue<ConsultaConsignadoCotaDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaConsignadoCotaDTO>>();
		
		Integer totalRegistros = this.consultaConsignadoCota.buscarTodasMovimentacoesPorCota(filtro, "nao limitar");
		if(totalRegistros == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
		}

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaConsignadoCota));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(totalRegistros);
		
		return tableModel;
	}
	
	@Post
	@Path("pesquisarMovimentoCotaPeloFornecedor")
	public void pesquisarMovimentoCotaPeloFornecedor(FiltroConsultaConsignadoCotaDTO filtro, String sortorder, String sortname, int page, int rp){
		
		if(filtro.getIdFornecedor() == -1 || filtro.getIdFornecedor() == 0){
			filtro.setIdFornecedor(null);
		}

		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.validarEntrada(filtro);
		
		this.tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<ConsultaConsignadoCotaPeloFornecedorDTO>> tableModel = efetuarConsultaConsignadoCotaSemCota(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	private TableModel<CellModelKeyValue<ConsultaConsignadoCotaPeloFornecedorDTO>> efetuarConsultaConsignadoCotaSemCota(FiltroConsultaConsignadoCotaDTO filtro) {
		
		List<ConsultaConsignadoCotaPeloFornecedorDTO> listaConsignadoCota = this.consultaConsignadoCota.buscarMovimentosCotaPeloFornecedor(filtro, "limitar");
		
		TableModel<CellModelKeyValue<ConsultaConsignadoCotaPeloFornecedorDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaConsignadoCotaPeloFornecedorDTO>>();
		
		Integer totalRegistros = this.consultaConsignadoCota.buscarTodasMovimentacoesPorCota(filtro, "nao limitar");
		if(totalRegistros == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
		}

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaConsignadoCota));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(totalRegistros);
		
		return tableModel;
	}
	
	private void carregarComboFornecedores() {
		
		List<Fornecedor> listaFornecedor = fornecedorService.obterFornecedoresAtivos();
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo = new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaFornecedoresCombo.add(new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial()));
		}
		
		result.include("listaFornecedores",listaFornecedoresCombo );
	}
	
	@Post
	@Path("/buscarCotaPorNumero")
	public void buscarCotaPorNumero(Integer numeroCota) {
		
		Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);
		
		if (cota == null) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Cota inexistente.");
		}

		Pessoa pessoa = cota.getPessoa();

		String nomeCota = null;

		if (pessoa instanceof PessoaFisica) {

			nomeCota = ((PessoaFisica) pessoa).getNome();

		} else if (pessoa instanceof PessoaJuridica) {

			nomeCota = ((PessoaJuridica) pessoa).getRazaoSocial();
		}

		this.result.use(Results.json()).from(nomeCota, "result").recursive().serialize();
	}
	
	private void validarEntrada(FiltroConsultaConsignadoCotaDTO filtro) {		
		
		if(filtro.getIdCota() == null && filtro.getIdFornecedor() == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Pelo menos um filtro deve ser preenchido!");			
		}
		
	}
	
	private void tratarFiltro(FiltroConsultaConsignadoCotaDTO filtroAtual) {

		FiltroConsultaConsignadoCotaDTO filtroSession = (FiltroConsultaConsignadoCotaDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE_CONSIGNADO_COTA);
		
		if (filtroSession != null && filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE_CONSIGNADO_COTA, filtroAtual);
	}

}
