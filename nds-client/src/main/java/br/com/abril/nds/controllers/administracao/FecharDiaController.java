package br.com.abril.nds.controllers.administracao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoLancamentoFaltaESobraFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoRecebimentoFisicoFecharDiaDTO;
import br.com.abril.nds.dto.filtro.FecharDiaDTO;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.FecharDiaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/administracao/fecharDia")
public class FecharDiaController {
	
	@Autowired
	private FecharDiaService fecharDiaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private Result result;
	
	private static Distribuidor distribuidor;
	
	@Path("/")
	@Rules(Permissao.ROLE_ADMINISTRACAO_FECHAR_DIA)
	public void index(){
		distribuidor = this.distribuidorService.obter();
		result.include("dataOperacao", DateUtil.formatarData(distribuidor.getDataOperacao(), Constantes.DATE_PATTERN_PT_BR));
	}
	
	@Post
	public void inicializarValidacoes(){		
		FecharDiaDTO dto = new FecharDiaDTO();
		dto.setBaixaBancaria(this.fecharDiaService.existeCobrancaParaFecharDia(distribuidor.getDataOperacao()));
		dto.setRecebimentoFisico(this.fecharDiaService.existeNotaFiscalSemRecebimentoFisico(distribuidor.getDataOperacao()));
		dto.setConfirmacaoDeExpedicao(this.fecharDiaService.existeConfirmacaoDeExpedicao(distribuidor.getDataOperacao()));
		dto.setLancamentoFaltasESobras(this.fecharDiaService.existeLancamentoFaltasESobrasPendentes(distribuidor.getDataOperacao()));
		
		result.use(Results.json()).withoutRoot().from(dto).recursive().serialize();
	}
	
	@Post
	@Path("/obterRecebimentoFisicoNaoConfirmado")
	public void obterRecebimentoFisicoNaoConfirmado(){
		
		List<ValidacaoRecebimentoFisicoFecharDiaDTO> listaRecebimentoFisicoNaoConfirmado = this.fecharDiaService.obterNotaFiscalComRecebimentoFisicoNaoConfirmado(distribuidor.getDataOperacao());
		
		TableModel<CellModelKeyValue<ValidacaoRecebimentoFisicoFecharDiaDTO>> tableModel = new TableModel<CellModelKeyValue<ValidacaoRecebimentoFisicoFecharDiaDTO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaRecebimentoFisicoNaoConfirmado));
		
		tableModel.setTotal(listaRecebimentoFisicoNaoConfirmado.size());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	@Path("/obterConfirmacaoDeExpedicao")
	public void obterConfirmacaoDeExpedicao(){
		
		List<ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO> listaConfirmacaoDeExpedicao = this.fecharDiaService.obterConfirmacaoDeExpedicao(distribuidor.getDataOperacao());
		
		TableModel<CellModelKeyValue<ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO>> tableModel = new TableModel<CellModelKeyValue<ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaConfirmacaoDeExpedicao));
		
		tableModel.setTotal(listaConfirmacaoDeExpedicao.size());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	@Post
	@Path("/obterLancamentoFaltaESobra")
	public void obterLancamentoFaltaESobra(){
		
		List<ValidacaoLancamentoFaltaESobraFecharDiaDTO> listaLancamentoFaltaESobra = this.fecharDiaService.obterLancamentoFaltasESobras(distribuidor.getDataOperacao());
		
		TableModel<CellModelKeyValue<ValidacaoLancamentoFaltaESobraFecharDiaDTO>> tableModel = new TableModel<CellModelKeyValue<ValidacaoLancamentoFaltaESobraFecharDiaDTO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaLancamentoFaltaESobra));
		
		tableModel.setTotal(listaLancamentoFaltaESobra.size());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}

}
