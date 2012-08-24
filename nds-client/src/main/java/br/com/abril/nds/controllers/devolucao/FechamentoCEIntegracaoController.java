package br.com.abril.nds.controllers.devolucao;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.FechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.FechamentoCEIntegracaoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("devolucao/fechamentoCEIntegracao")
public class FechamentoCEIntegracaoController {
	
	private static final String FILTRO_SESSION_ATTRIBUTE_FECHAMENTO_CE_INTEGRACAO = "filtroFechamentoCEIntegracao";
	
	private Result result;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private FechamentoCEIntegracaoService fechamentoCEIntegracaoService;
	
	@Autowired
	private HttpSession session;
	
	
	public FechamentoCEIntegracaoController(Result result) {
		 this.result = result;
	}
	
	@Path("/")
	@Rules(Permissao.ROLE_DEVOLUCAO_FECHAMENTO_INTEGRACAO)
	public void index(){
		this.carregarComboFornecedores();
		
	}
	
	@Post
	@Path("/pesquisaPrincipal")
	public void pesquisaPrincipal(FiltroFechamentoCEIntegracaoDTO filtro, String sortorder, String sortname, int page, int rp){
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.validarEntrada(filtro);
		
		this.tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<FechamentoCEIntegracaoDTO>> tableModel = efetuarConsultaFechamentoCEIntegracao(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	private TableModel<CellModelKeyValue<FechamentoCEIntegracaoDTO>> efetuarConsultaFechamentoCEIntegracao(FiltroFechamentoCEIntegracaoDTO filtro) {
		
		List<FechamentoCEIntegracaoDTO> listaFechamento = this.fechamentoCEIntegracaoService.buscarFechamentoEncalhe(filtro);
		
		listaFechamento = calcularVenda(listaFechamento);
		
		TableModel<CellModelKeyValue<FechamentoCEIntegracaoDTO>> tableModel = new TableModel<CellModelKeyValue<FechamentoCEIntegracaoDTO>>();
//		
//		Integer totalRegistros = this.romaneioService.buscarTotalDeRomaneios(filtro);
//		if(totalRegistros == 0){
//			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
//		}
//
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaFechamento));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(15);
		
		return tableModel;
	}
	
	private List<FechamentoCEIntegracaoDTO> calcularVenda(List<FechamentoCEIntegracaoDTO> listaFechamento) {
		List<FechamentoCEIntegracaoDTO> lista = new ArrayList<FechamentoCEIntegracaoDTO>();
		for(FechamentoCEIntegracaoDTO dto: listaFechamento){
			dto.setVenda(dto.getReparte().subtract(dto.getEncalhe()));
			double valorDaVenda = dto.getVenda().doubleValue() * dto.getPrecoCapa().doubleValue();
			dto.setvalorVendaFormatado(CurrencyUtil.formatarValor(valorDaVenda));
			lista.add(dto);
		}
		return lista;		
	}

	private void validarEntrada(FiltroFechamentoCEIntegracaoDTO filtro) {
		boolean validar = false;
		
		if(filtro.getIdFornecedor() == 0 && filtro.getSemana() == 0){
			validar = true;
		}
		
		if(validar){
			throw new ValidacaoException(TipoMensagem.WARNING, "Pelo menos um filtro deve ser preenchido!");
		}
		
	}
	
	private void tratarFiltro(FiltroFechamentoCEIntegracaoDTO filtroAtual) {

		FiltroFechamentoCEIntegracaoDTO filtroSession = (FiltroFechamentoCEIntegracaoDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE_FECHAMENTO_CE_INTEGRACAO);
		
		if (filtroSession != null && filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE_FECHAMENTO_CE_INTEGRACAO, filtroAtual);
	}
	
	private void carregarComboFornecedores() {
		
		List<Fornecedor> listaFornecedor = fornecedorService.obterFornecedoresAtivos();
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo = new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaFornecedoresCombo.add(new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial()));
		}
		
		result.include("listaFornecedores",listaFornecedoresCombo );
	}

}
