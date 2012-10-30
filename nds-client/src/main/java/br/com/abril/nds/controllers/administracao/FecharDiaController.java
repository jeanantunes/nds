package br.com.abril.nds.controllers.administracao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.FecharDiaDTO;
import br.com.abril.nds.dto.ReparteFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoControleDeAprovacaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoLancamentoFaltaESobraFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoRecebimentoFisicoFecharDiaDTO;
import br.com.abril.nds.dto.VendaSuplementarDTO;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.FecharDiaService;
import br.com.abril.nds.service.ResumoEncalheFecharDiaService;
import br.com.abril.nds.service.ResumoReparteFecharDiaService;
import br.com.abril.nds.service.ResumoSuplementarFecharDiaService;
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
	private ResumoReparteFecharDiaService resumoFecharDiaService;
	
	@Autowired
	private ResumoEncalheFecharDiaService resumoEncalheFecharDiaService;
	
	@Autowired
	private ResumoSuplementarFecharDiaService resumoSuplementarFecharDiaService;
	
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
	
	@SuppressWarnings("static-access")
	@Post
	public void inicializarValidacoes(){		
		FecharDiaDTO dto = new FecharDiaDTO();
		dto.setBaixaBancaria(this.fecharDiaService.existeCobrancaParaFecharDia(distribuidor.getDataOperacao()));
		dto.setGeracaoDeCobranca(this.fecharDiaService.existeGeracaoDeCobranca(distribuidor.getDataOperacao()));
		dto.setRecebimentoFisico(this.fecharDiaService.existeNotaFiscalSemRecebimentoFisico(distribuidor.getDataOperacao()));
		dto.setConfirmacaoDeExpedicao(this.fecharDiaService.existeConfirmacaoDeExpedicao(distribuidor.getDataOperacao()));
		dto.setLancamentoFaltasESobras(this.fecharDiaService.existeLancamentoFaltasESobrasPendentes(distribuidor.getDataOperacao()));
		dto.setControleDeAprovacao(this.distribuidor.isUtilizaControleAprovacao());		
		
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
	
	@Post
	@Path("validacoesDoCotroleDeAprovacao")
	public void validacoesDoCotroleDeAprovacao(){
		
		List<ValidacaoControleDeAprovacaoFecharDiaDTO> listaLancamentoFaltaESobra = this.fecharDiaService.obterPendenciasDeAprovacao(distribuidor.getDataOperacao(), StatusAprovacao.PENDENTE);
		Boolean pendencia = false;
		for (ValidacaoControleDeAprovacaoFecharDiaDTO dto : listaLancamentoFaltaESobra) {
			if(dto.getDescricaoTipoMovimento().equals("Falta DE") || dto.getDescricaoTipoMovimento().equals("Falta EM") 
					|| dto.getDescricaoTipoMovimento().equals("Sobra DE") || dto.getDescricaoTipoMovimento().equals("Sobra EM")) {
				pendencia = true;
			}
			if(dto.getDescricaoTipoMovimento().equals("Crédito") || dto.getDescricaoTipoMovimento().equals("Débito")){
				pendencia = true;
			}
			if(dto.getDescricaoTipoMovimento().equals("Negociação")){
				pendencia = true;
			}
			if(dto.getDescricaoTipoMovimento().equals("Ajuste de estoque")){
				pendencia = true;
			}
			if(dto.getDescricaoTipoMovimento().equals("Postergação de cobrança")){
				pendencia = true;
			}
		}
		
		result.use(Results.json()).from(pendencia).recursive().serialize();
		
	}
	
	@Post
	@Path("obterResumoQuadroReparte")
	public void obterResumoQuadroReparte(){
		
		List<BigDecimal> listaDeResultados = new ArrayList<BigDecimal>();

		List<ReparteFecharDiaDTO> lista = this.resumoFecharDiaService.obterValorReparte(distribuidor.getDataOperacao(), true);
		
		BigDecimal totalReparte = lista.get(0).getValorTotalReparte();
		listaDeResultados.add(totalReparte);
		
		lista = this.resumoFecharDiaService.obterValorDiferenca(distribuidor.getDataOperacao(), true, "sobra");
		BigDecimal totalSobras = lista.get(0).getSobras();
		listaDeResultados.add(totalSobras);
		
		
		lista = this.resumoFecharDiaService.obterValorDiferenca(distribuidor.getDataOperacao(), true, "falta");
		BigDecimal totalFaltas = lista.get(0).getFaltas();
		listaDeResultados.add(totalFaltas);
		
		
		lista = this.resumoFecharDiaService.obterValorTransferencia(distribuidor.getDataOperacao(), true);
		BigDecimal totalTranferencia = BigDecimal.ZERO;
		if(lista.get(0).getTransferencias() != null){
			totalTranferencia = lista.get(0).getTransferencias();			
		}
		listaDeResultados.add(totalTranferencia);
		
		
		BigDecimal totalADistribuir = (totalReparte.add(totalSobras)).subtract(totalFaltas);
		listaDeResultados.add(totalADistribuir);
		
		lista = this.resumoFecharDiaService.obterValorDistribuido(distribuidor.getDataOperacao(), true);
		BigDecimal totalDistribuido = lista.get(0).getDistribuidos();
		listaDeResultados.add(totalDistribuido);
		
		BigDecimal sobraDistribuido = totalADistribuir.subtract(totalDistribuido);
		listaDeResultados.add(sobraDistribuido);
		BigDecimal diferenca = totalDistribuido.subtract(sobraDistribuido);
		listaDeResultados.add(diferenca);
		
		result.use(Results.json()).from(listaDeResultados, "result").serialize();
	}
	
	@Post
	@Path("obterResumoQuadroEncalhe")
	public void obterResumoQuadroEncalhe(){
		
		List<BigDecimal> listaDeEncalhes = new ArrayList<BigDecimal>();
		
		BigDecimal totalLogico = this.resumoEncalheFecharDiaService.obterValorEncalheLogico(distribuidor.getDataOperacao());
		listaDeEncalhes.add(totalLogico);
		
		BigDecimal totalFisico = this.resumoEncalheFecharDiaService.obterValorEncalheFisico(distribuidor.getDataOperacao(), false);
		listaDeEncalhes.add(totalFisico);
		
		BigDecimal totalJuramentado = this.resumoEncalheFecharDiaService.obterValorEncalheFisico(distribuidor.getDataOperacao(), true);;
		listaDeEncalhes.add(totalJuramentado);
		
		List<ReparteFecharDiaDTO> lista = this.resumoFecharDiaService.obterValorReparte(distribuidor.getDataOperacao(), true);
		BigDecimal venda = lista.get(0).getValorTotalReparte().subtract(totalFisico) ;
		listaDeEncalhes.add(venda);
		
		result.use(Results.json()).from(listaDeEncalhes, "result").recursive().serialize();
		
	}
	

	@Post
	@Path("obterResumoQuadroSuplementar")
	public void obterResumoQuadroSuplementar(){
		
		List<BigDecimal> listaDeSuplementares = new ArrayList<BigDecimal>();
		
		BigDecimal totalEstoqueLogico = this.resumoSuplementarFecharDiaService.obterValorEstoqueLogico(distribuidor.getDataOperacao());
		listaDeSuplementares.add(totalEstoqueLogico);
		
		BigDecimal totalTransferencia = this.resumoSuplementarFecharDiaService.obterValorTransferencia(distribuidor.getDataOperacao());
		listaDeSuplementares.add(totalTransferencia);
		
		BigDecimal totalVenda = this.resumoSuplementarFecharDiaService.obterValorVenda(distribuidor.getDataOperacao());
		listaDeSuplementares.add(totalVenda);
		
		BigDecimal totalFisico = this.resumoSuplementarFecharDiaService.obterValorFisico(distribuidor.getDataOperacao());
		listaDeSuplementares.add(totalEstoqueLogico.subtract(totalFisico));
		
		result.use(Results.json()).from(listaDeSuplementares, "result").recursive().serialize();
	}
	
	@Post
	@Path("/obterGridReparte")
	public void obterGridReparte(){
		
		List<ReparteFecharDiaDTO> listaReparte = this.resumoFecharDiaService.obterResumoReparte(distribuidor.getDataOperacao());
		
		TableModel<CellModelKeyValue<ReparteFecharDiaDTO>> tableModel = new TableModel<CellModelKeyValue<ReparteFecharDiaDTO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaReparte));
		
		tableModel.setTotal(listaReparte.size());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	@Post
	@Path("/obterGridVendaSuplemntar")
	public void obterGridVendaSuplemntar(){
		
		List<VendaSuplementarDTO> listaReparte = resumoSuplementarFecharDiaService.obterVendasSuplementar(distribuidor.getDataOperacao());
		
		TableModel<CellModelKeyValue<VendaSuplementarDTO>> tableModel = new TableModel<CellModelKeyValue<VendaSuplementarDTO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaReparte));
		
		tableModel.setTotal(listaReparte.size());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}

}
