package br.com.abril.nds.controllers.financeiro;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ConsultaNegociacaoDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacoesDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.service.NegociacaoDividaService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/financeiro/consultaNegociacoes")
@Rules(Permissao.ROLE_FINANCEIRO_CONSULTA_NEGOCIACOES)
public class ConsultaNegociacoesController extends BaseController {
	
	
	@Autowired
	private NegociacaoDividaService negociacaoDividaService;
	
	@Autowired
    private Result result;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private BancoService bancoService;
	
	@Path("/")
	public void index(){
		
		Integer qntdParcelas = this.distribuidorService.negociacaoAteParcelas();
		
		qntdParcelas = (qntdParcelas == null ? 0 : qntdParcelas);
		
		List<Integer> parcelas = new ArrayList<Integer>();
		
		for(int i = 1; i <= qntdParcelas; i++){
			parcelas.add(i);
		}
		
		FiltroConsultaBancosDTO  filtro = new FiltroConsultaBancosDTO();
		filtro.setAtivo(true);
		
		List<Banco> bancos = this.bancoService.obterBancos(filtro);

		this.result.include("qntdParcelas", parcelas);
		this.result.include("bancos", bancos);

		
	}
	
	@Post
	@Path("/buscarNegociacoes")
	public void buscarNegociacaoDivida(FiltroConsultaNegociacoesDTO filtro, String sortorder, String sortname, int page, int rp){

		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
		TableModel<CellModelKeyValue<ConsultaNegociacaoDividaDTO>> tableModel = efetuarConsultaNegociacaoDivida(filtro);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	
	private TableModel<CellModelKeyValue<ConsultaNegociacaoDividaDTO>> efetuarConsultaNegociacaoDivida(FiltroConsultaNegociacoesDTO filtro) {
		
		List<ConsultaNegociacaoDividaDTO> listaConsulta = negociacaoDividaService.buscarNegociacoesDividas(filtro);
		
		
		if (listaConsulta == null || listaConsulta.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		TableModel<CellModelKeyValue<ConsultaNegociacaoDividaDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaNegociacaoDividaDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaConsulta));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());

		return tableModel;

	}
	
}
