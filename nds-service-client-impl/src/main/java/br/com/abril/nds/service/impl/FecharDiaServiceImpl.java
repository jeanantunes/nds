
package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaResumoDTO;
import br.com.abril.nds.dto.EncalheFecharDiaDTO;
import br.com.abril.nds.dto.ReparteFecharDiaDTO;
import br.com.abril.nds.dto.ResumoEncalheFecharDiaDTO;
import br.com.abril.nds.dto.ResumoFechamentoDiarioConsignadoDTO;
import br.com.abril.nds.dto.ResumoFechamentoDiarioConsignadoDTO.ResumoConsignado;
import br.com.abril.nds.dto.ResumoFechamentoDiarioCotasDTO;
import br.com.abril.nds.dto.ResumoFechamentoDiarioCotasDTO.TipoResumo;
import br.com.abril.nds.dto.ResumoSuplementarFecharDiaDTO;
import br.com.abril.nds.dto.SuplementarFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoLancamentoFaltaESobraFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoRecebimentoFisicoFecharDiaDTO;
import br.com.abril.nds.dto.VendaFechamentoDiaDTO;
import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.fechamentodiario.DiferencaDTO;
import br.com.abril.nds.dto.fechamentodiario.DividaDTO;
import br.com.abril.nds.dto.fechamentodiario.FechamentoDiarioDTO;
import br.com.abril.nds.dto.fechamentodiario.FechamentoDiarioDTO.Builder;
import br.com.abril.nds.dto.fechamentodiario.ResumoEstoqueDTO;
import br.com.abril.nds.dto.fechamentodiario.ResumoEstoqueDTO.ResumoEstoqueExemplar;
import br.com.abril.nds.dto.fechamentodiario.ResumoEstoqueDTO.ResumoEstoqueProduto;
import br.com.abril.nds.dto.fechamentodiario.ResumoEstoqueDTO.ValorResumoEstoque;
import br.com.abril.nds.dto.fechamentodiario.SumarizacaoDividasDTO;
import br.com.abril.nds.dto.fechamentodiario.SumarizacaoReparteDTO;
import br.com.abril.nds.dto.fechamentodiario.TipoDivida;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.model.cadastro.ParametrosAprovacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.EstoqueProdutoDTO;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.HistoricoEstoqueProduto;
import br.com.abril.nds.model.estoque.LancamentoDiferenca;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fechar.dia.FechamentoDiario;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoCota;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoDivida;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoEncalhe;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoReparte;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoSuplementar;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioCota;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioCota.TipoSituacaoCota;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioDiferenca;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioDivida;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioLancamentoEncalhe;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioLancamentoReparte;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioLancamentoSuplementar;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioMovimentoVendaEncalhe;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioMovimentoVendaSuplementar;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoConsignado;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoConsolidadoDivida;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoEstoque;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.movimentacao.Movimento;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ConferenciaEncalheParcialRepository;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.repository.ConsultaConsignadoCotaRepository;
import br.com.abril.nds.repository.CotaAusenteRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DiferencaEstoqueRepository;
import br.com.abril.nds.repository.DistribuicaoFornecedorRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.repository.FechamentoDiarioConsolidadoCotaRepository;
import br.com.abril.nds.repository.FechamentoDiarioConsolidadoDividaRepository;
import br.com.abril.nds.repository.FechamentoDiarioConsolidadoEncalheRepository;
import br.com.abril.nds.repository.FechamentoDiarioConsolidadoReparteRepository;
import br.com.abril.nds.repository.FechamentoDiarioConsolidadoSuplementarRepository;
import br.com.abril.nds.repository.FechamentoDiarioCotaRepository;
import br.com.abril.nds.repository.FechamentoDiarioDividaRepository;
import br.com.abril.nds.repository.FechamentoDiarioLancamentoEncalheRepository;
import br.com.abril.nds.repository.FechamentoDiarioLancamentoReparteRepository;
import br.com.abril.nds.repository.FechamentoDiarioLancamentoSuplementarRepository;
import br.com.abril.nds.repository.FechamentoDiarioMovimentoVendaEncalheRepository;
import br.com.abril.nds.repository.FechamentoDiarioMovimentoVendaSuplementarRepository;
import br.com.abril.nds.repository.FechamentoDiarioRepository;
import br.com.abril.nds.repository.FechamentoDiarioResumoConsignadoRepository;
import br.com.abril.nds.repository.FechamentoDiarioResumoConsolidadoDividaRepository;
import br.com.abril.nds.repository.FechamentoDiarioResumoEstoqueRepository;
import br.com.abril.nds.repository.FecharDiaRepository;
import br.com.abril.nds.repository.HistoricoEstoqueProdutoRepository;
import br.com.abril.nds.repository.HistoricoSituacaoCotaRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.MovimentoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.repository.VisaoEstoqueRepository;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.DebitoCreditoCotaService;
import br.com.abril.nds.service.DescontoLogisticaService;
import br.com.abril.nds.service.DividaService;
import br.com.abril.nds.service.FecharDiaService;
import br.com.abril.nds.service.FixacaoReparteService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.ResumoEncalheFecharDiaService;
import br.com.abril.nds.service.ResumoReparteFecharDiaService;
import br.com.abril.nds.service.ResumoSuplementarFecharDiaService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.exception.FechamentoDiarioException;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.SemanaUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;

@Service
public class FecharDiaServiceImpl implements FecharDiaService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FecharDiaServiceImpl.class);
	
	@Autowired
	private FecharDiaRepository fecharDiaRepository;
	
	@Autowired
	private DividaService dividaService;
	
	@Autowired
	private ConsultaConsignadoCotaRepository consultaConsignadoCotaRepository;

	@Autowired
	private CotaRepository cotaRepository;

	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private MovimentoRepository movimentoRepository;
	
	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	@Autowired
	private ResumoReparteFecharDiaService resumoReparteFecharDiaService;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private ResumoEncalheFecharDiaService resumoEncalheFecharDiaService;
	
	@Autowired
	private FechamentoDiarioRepository fechamentoDiarioRepository;
	
	@Autowired
	private FechamentoDiarioConsolidadoReparteRepository fechamentoConsolidadoReparteRepository;
	
	@Autowired
	private FechamentoDiarioLancamentoReparteRepository  fechamentoLancamentoReparteRepository;
	
	@Autowired
	private FechamentoDiarioConsolidadoDividaRepository  fechamentoDiarioConsolidadoDividaRepository;
	
	@Autowired
	private FechamentoDiarioResumoConsolidadoDividaRepository fechamentoDiarioResumoConsolidadoDividaRepository;
	
	@Autowired
	private FechamentoDiarioDividaRepository fechamentoDiarioDividaRepository;
	
	@Autowired
	private FechamentoDiarioConsolidadoCotaRepository fechamentoDiarioConsolidadoCotaRepository;
	
	@Autowired
	private FechamentoDiarioCotaRepository fechamentoDiarioCotaRepository;
	
	@Autowired
	private FechamentoDiarioLancamentoEncalheRepository  fechamentoDiarioLancamentoEncalheRepository;
	
	@Autowired
	private FechamentoDiarioMovimentoVendaEncalheRepository fechamentoDiarioMovimentoVendaEncalheRepository;
	
	@Autowired
	private ResumoSuplementarFecharDiaService resumoSuplementarFecharDiaService;
	
	@Autowired
	private FechamentoDiarioConsolidadoSuplementarRepository fechamentoDiarioConsolidadoSuplementarRepository;
	
	@Autowired
	private FechamentoDiarioMovimentoVendaSuplementarRepository fechamentoDiarioMovimentoVendaSuplementarRepository;
	
	@Autowired
	private FechamentoDiarioLancamentoSuplementarRepository fechamentoDiarioLancamentoSuplementarRepository;
	
	@Autowired
	private FechamentoDiarioResumoConsignadoRepository fechamentoDiarioResumoConsignadoRepository;
	
	@Autowired
	private FechamentoDiarioResumoEstoqueRepository fechamentoDiarioResumoEstoqueRepository;
	
	@Autowired
	private FechamentoDiarioConsolidadoEncalheRepository fechamentoDiarioConsolidadoEncalheRepository;
	
	@Autowired
	private DiferencaEstoqueRepository diferencaRepository;
	
	@Autowired 
	private CalendarioService calendarioService;
	
	@Autowired
	private DistribuicaoFornecedorRepository distribuicaoFornecedorRepository;
	
	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	@Autowired
	private MovimentoEstoqueRepository movimentoEstoqueRepository;
	
	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private ConferenciaEncalheParcialRepository conferenciaEncalheParcialRepository;
	
	@Autowired
	private ConsolidadoFinanceiroRepository consolidadoFinanceiroRepository;
	
	@Autowired
	private MovimentoEstoqueService movimentoEstoqueService;
	
	@Autowired
	private GerarCobrancaService gerarCobrancaService;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private EstoqueProdutoRespository estoqueProdutoRepository;
	
	@Autowired
	private HistoricoEstoqueProdutoRepository historicoEstoqueProdutoRepository;
	
	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
	
	@Autowired
	private HistoricoSituacaoCotaRepository historicoSituacaoCotaRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private VisaoEstoqueRepository visaoEstoqueRepository;
	
	@Autowired
	private DescontoLogisticaService descontoLogisticaService;
	
	@Autowired
	private FixacaoReparteService fixacaoReparteService;
	
	@Autowired
	private DebitoCreditoCotaService debitoCreditoCotaService;
	
	@Autowired
	private CotaAusenteRepository cotaAusenteRepository;

	
	@Autowired
	private BoletoService boletoService;
	private static final Logger LOG = LoggerFactory.getLogger("fecharDiaLogger");
	
	@Override
	@Transactional
	public boolean existeCobrancaParaFecharDia(Date dataOperacaoDistribuidor) {
		
		return false;
		
        // TODO verificar com o cesar com ficara a regra para validação de baixa
        // de cobrança no fechamento diario
		
		//Date diaDeOperaoMenosUm = DateUtil.subtrairDias(dataOperacaoDistribuidor, 1);
		//return this.fecharDiaRepository.existeCobrancaParaFecharDia(diaDeOperaoMenosUm);
	}

	@Override
	@Transactional
	public boolean existeNotaFiscalSemRecebimentoFisico(Date dataOperacaoDistribuidor) {		 
		return this.fecharDiaRepository.existeNotaFiscalSemRecebimentoFisico(dataOperacaoDistribuidor);
	}

	@Override
	@Transactional
	public List<ValidacaoRecebimentoFisicoFecharDiaDTO> obterNotaFiscalComRecebimentoFisicoNaoConfirmado(Date dataOperacaoDistribuidor) {
		
		return this.fecharDiaRepository.obterNotaFiscalComRecebimentoFisicoNaoConfirmado(dataOperacaoDistribuidor);
	}

	@Override
	@Transactional
	public Boolean existeConfirmacaoDeExpedicao(Date dataOperacao) {		 
		return this.fecharDiaRepository.existeConfirmacaoDeExpedicao(dataOperacao);
	}

	@Override
	@Transactional
	public List<ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO> obterConfirmacaoDeExpedicao(Date dataOperacaoDistribuidor) {		 
		return this.fecharDiaRepository.obterConfirmacaoDeExpedicao(dataOperacaoDistribuidor);
	}

	@Override
	@Transactional
	public Boolean existeLancamentoFaltasESobrasPendentes(Date dataOperacaoDistribuidor) {		
		return this.fecharDiaRepository.existeLancamentoFaltasESobrasPendentes(dataOperacaoDistribuidor).isEmpty() ? true : false;
	}

	@Override
	@Transactional
	public List<ValidacaoLancamentoFaltaESobraFecharDiaDTO> obterLancamentoFaltasESobras(Date dataOperacaoDistribuidor) {
		return this.fecharDiaRepository.existeLancamentoFaltasESobrasPendentes(dataOperacaoDistribuidor);
	}
	
	@Override
	@Transactional
	public Boolean existeMatrizRecolhimentoSalva(Date dataOperacaoDistribuidor) {
		return this.fecharDiaRepository.existeMatrizRecolhimentoSalva(dataOperacaoDistribuidor);
	}

	/**
	 * Verifica se cotas a vista tiveram seus movimentos financeiros consolidados
	 * @param data
	 * @return boolean
	 */
	@Override
	@Transactional
	public boolean isConsolidadoCotaAVista(Date data){

	    BigDecimal saldo = this.movimentoFinanceiroCotaRepository.obterSaldoCotasAVista(null, data); 
	    
	    if (saldo != null && saldo.compareTo(BigDecimal.ZERO) > 0){
	    	
	    	return false;
	    }
		
		return true;
	}
	
	@Override
	@Transactional
	public boolean existePendenciasDeAprovacao(Date dataOperacao) {
		
		if (this.distribuidorRepository.utilizaControleAprovacao()){
			
			ParametrosAprovacaoDistribuidor parametrosAprovacaoDistribuidor =
					this.distribuidorRepository.parametrosAprovacaoDistribuidor();
			
			List<TipoMovimento> movimentosVerificaAprovacao = new ArrayList<TipoMovimento>();
			
			if (parametrosAprovacaoDistribuidor != null){
				
				if (parametrosAprovacaoDistribuidor.isDevolucaoFornecedor()){
					
					if (this.conferenciaEncalheParcialRepository.verificarDevolucao(
							dataOperacao, StatusAprovacao.PENDENTE)){
						
						return true;
					}
				}
				
				if (parametrosAprovacaoDistribuidor.isAjusteEstoque()){
					
					movimentosVerificaAprovacao.addAll(
						
						this.tipoMovimentoEstoqueRepository.buscarTiposMovimentoEstoque(
							Arrays.asList(
								GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_LANCAMENTO,
								GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR,
								GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_SUPLEMENTAR,
								GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_RECOLHIMENTO,
								GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_RECOLHIMENTO,
								GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DANIFICADOS,
								GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DANIFICADOS
							)
						)
					);
				}
				
				if (parametrosAprovacaoDistribuidor.isDebitoCredito()){
					
					movimentosVerificaAprovacao.addAll(
						
						this.tipoMovimentoFinanceiroRepository.buscarTiposMovimentoFinanceiro(
							Arrays.asList(
								GrupoMovimentoFinaceiro.CREDITO,
								GrupoMovimentoFinaceiro.DEBITO_SOBRE_FATURAMENTO,
								GrupoMovimentoFinaceiro.CREDITO_SOBRE_FATURAMENTO
							)
						)
					);
				}
				
				if (parametrosAprovacaoDistribuidor.isFaltasSobras()){
					
					movimentosVerificaAprovacao.addAll(
							
						this.tipoMovimentoEstoqueRepository.buscarTiposMovimentoEstoque(
							Arrays.asList(
								GrupoMovimentoEstoque.FALTA_EM,
								GrupoMovimentoEstoque.FALTA_DE,
								GrupoMovimentoEstoque.SOBRA_EM,
								GrupoMovimentoEstoque.SOBRA_DE
							)
						)
					);
				}
				
				if (parametrosAprovacaoDistribuidor.isNegociacao()){
					
					movimentosVerificaAprovacao.addAll(
						this.tipoMovimentoFinanceiroRepository.buscarTiposMovimentoFinanceiro(
							Arrays.asList(GrupoMovimentoFinaceiro.POSTERGADO_NEGOCIACAO)
						)
					);
				}
				
				if (parametrosAprovacaoDistribuidor.isPostergacaoCobranca()){
					
					movimentosVerificaAprovacao.addAll(
						this.tipoMovimentoFinanceiroRepository.buscarTiposMovimentoFinanceiro(
							Arrays.asList(
								GrupoMovimentoFinaceiro.POSTERGADO_DEBITO,
								GrupoMovimentoFinaceiro.POSTERGADO_CREDITO
							)
						)
					);
				}

                if (parametrosAprovacaoDistribuidor.isConsolidadoCota()){
            	   
            	   if (this.isConsolidadoCotaAVista(dataOperacao) == false){
            		   
            		   return true;
            	   }
				}	
			}
			
			return this.fecharDiaRepository.existePendenciasDeAprovacao(
					dataOperacao, StatusAprovacao.PENDENTE, movimentosVerificaAprovacao);
		} else {
			
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public ResumoFechamentoDiarioCotasDTO obterResumoCotas(Date dataFechamento) {
		
		if (this.isDiaComFechamentoRealizado(dataFechamento)){
			
			return obterResumoCotaComFechamentoDiario(dataFechamento);
		}
			
		return obterResumoCotaComFechamentoDiarioNaoRealizado(dataFechamento);
	}

	private ResumoFechamentoDiarioCotasDTO obterResumoCotaComFechamentoDiarioNaoRealizado(Date dataFechamento) {
		
		Long quantidadeTotal = this.cotaRepository.obterQuantidadeCotas(null);
		
		Long quantidadeAtivas = this.cotaRepository.obterQuantidadeCotas(SituacaoCadastro.ATIVO);
		
		Long quantidadeAusentesExpedicaoReparte = 
			this.cotaRepository.countCotasAusentesNaExpedicaoDoReparteEm(dataFechamento);
		
		Long quantidadeAusentesRecolhimentoEncalhe = 
			this.cotaRepository.countCotasAusentesNoRecolhimentoDeEncalheEm(dataFechamento);
		
		Long quantidadeNovas = this.cotaRepository.countCotasComInicioAtividadeEm(dataFechamento);
		
		Long quantidadeInativas = this.cotaRepository.countCotas(SituacaoCadastro.INATIVO);
		
		return new ResumoFechamentoDiarioCotasDTO(quantidadeTotal, quantidadeAtivas, 
												  quantidadeAusentesExpedicaoReparte, 
												  quantidadeAusentesRecolhimentoEncalhe, 
												  quantidadeNovas, quantidadeInativas);
	}

	private ResumoFechamentoDiarioCotasDTO obterResumoCotaComFechamentoDiario(Date dataFechamento) {
		
		FechamentoDiarioConsolidadoCota consolidadoCota = 
				fechamentoDiarioConsolidadoCotaRepository.obterResumoConsolidadoCotas(dataFechamento);
		
		if(consolidadoCota == null){
			return new ResumoFechamentoDiarioCotasDTO(0L, 0L,0L,0L,0L,0L);
		}
		
		Long quantidadeTotal = Util.nvl(consolidadoCota.getQuantidadeTotal(),0L).longValue();
		
		Long quantidadeAtivas = Util.nvl(consolidadoCota.getQuantidadeAtivos(),0L).longValue();
		
		Long quantidadeAusentesExpedicaoReparte = Util.nvl(consolidadoCota.getQuantidadeAusenteReparte(),0L).longValue();
				
		Long quantidadeAusentesRecolhimentoEncalhe = Util.nvl(consolidadoCota.getQuantidadeAusenteEncalhe(),0L).longValue();
		
		Long quantidadeNovas = Util.nvl(consolidadoCota.getQuantidadeNovos(),0L).longValue();
		
		Long quantidadeInativas = Util.nvl(consolidadoCota.getQuantidadeInativas(),0L).longValue();
		
		return new ResumoFechamentoDiarioCotasDTO(quantidadeTotal, quantidadeAtivas, 
												  quantidadeAusentesExpedicaoReparte, 
												  quantidadeAusentesRecolhimentoEncalhe, 
												  quantidadeNovas, quantidadeInativas);
	}
	
	public List<CotaResumoDTO> obterDetalheCotaFechamentoDiario(Date dataFechamento,TipoResumo tipoResumo){
	
		boolean diaFechadoParaData = this.isDiaComFechamentoRealizado(dataFechamento);
		
		switch (tipoResumo) {
		
			case AUSENTES_REPARTE:
				
				return this.obterCotasAusentesNaExpedicaoDoReparteEm(dataFechamento,diaFechadoParaData);
				
			case AUSENTES_ENCALHE:
				
				return this.obterCotasAusentesNoRecolhimentoDeEncalheEm(dataFechamento,diaFechadoParaData);
				
			case NOVAS:
				
				return this.obterCotasComInicioAtividadeEm(dataFechamento,diaFechadoParaData);
				
			case INATIVAS:
				
				return this.obterCotas(dataFechamento,diaFechadoParaData);
		}
		
		return Collections.EMPTY_LIST;
	}
	
	private List<CotaResumoDTO> obterCotasAusentesNaExpedicaoDoReparteEm(Date dataFechamento, boolean diaFechadoParaData){
		
		if (diaFechadoParaData){
			
			return fechamentoDiarioCotaRepository.obterCotas(dataFechamento, TipoSituacaoCota.AUSENTE_REPARTE );
		}
		
		return this.cotaRepository.obterCotasAusentesNaExpedicaoDoReparteEm(dataFechamento);
	}
	
	private List<CotaResumoDTO> obterCotasAusentesNoRecolhimentoDeEncalheEm(Date dataFechamento, boolean diaFechadoParaData){
		
		if (diaFechadoParaData){
			
			return fechamentoDiarioCotaRepository.obterCotas(dataFechamento, TipoSituacaoCota.AUSENTE_ENCALHE );
		}
		
		return this.cotaRepository.obterCotasAusentesNoRecolhimentoDeEncalheEm(dataFechamento);
	}
	
	private List<CotaResumoDTO> obterCotasComInicioAtividadeEm(Date dataFechamento, boolean diaFechadoParaData){
		
		if (diaFechadoParaData){
			
			return fechamentoDiarioCotaRepository.obterCotas(dataFechamento, TipoSituacaoCota.NOVAS );
		}
		
		return this.cotaRepository.obterCotasComInicioAtividadeEm(dataFechamento);
	}
	
	private List<CotaResumoDTO> obterCotas(Date dataFechamento, boolean diaFechadoParaData){
		
		if (diaFechadoParaData){
			
			return fechamentoDiarioCotaRepository.obterCotas(dataFechamento, TipoSituacaoCota.INATIVAS );
		}
		
		return this.cotaRepository.obterCotas(SituacaoCadastro.INATIVO);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public ResumoFechamentoDiarioConsignadoDTO obterResumoConsignado(Date dataFechamento) {
		
		if(this.isDiaComFechamentoRealizado(dataFechamento)){
			
			return this.obterResumoConsignadoComFechamentoProcessado(dataFechamento);
		}
		
		return obterResumoConsignadoComFechamentoNaoProcessado(dataFechamento);
	}
	
	private ResumoFechamentoDiarioConsignadoDTO obterResumoConsignadoComFechamentoNaoProcessado(Date dataFechamento) {
	    
		ResumoFechamentoDiarioConsignadoDTO resumoFechamentoDiarioConsignado = new ResumoFechamentoDiarioConsignadoDTO();

        ResumoFechamentoDiarioConsignadoDTO.ResumoConsignado resumoConsignado = resumoFechamentoDiarioConsignado.new ResumoConsignado();

        this.processarValoresConsignado(dataFechamento, resumoFechamentoDiarioConsignado, resumoConsignado);

        return resumoFechamentoDiarioConsignado;
	}

	private void processarValoresConsignado(
			Date dataFechamento,
			ResumoFechamentoDiarioConsignadoDTO resumoFechamentoDiarioConsignado,
			ResumoFechamentoDiarioConsignadoDTO.ResumoConsignado resumoConsignado) {
		
		resumoConsignado.setSaldoAnterior(Util.nvl(
				fechamentoDiarioResumoConsignadoRepository.obterSaldoConsignadoFechamentoDiarioAnterior(dataFechamento),BigDecimal.ZERO));
		
        //Valores de Entrada no consignado do Distribuidor
        BigDecimal valorCE = Util.nvl(
        		this.movimentoEstoqueCotaRepository.obterSaldoEntradaNoConsignado(dataFechamento, TipoCota.CONSIGNADO), BigDecimal.ZERO);
        
        BigDecimal valorCEAvista = Util.nvl(
        		this.movimentoEstoqueCotaRepository.obterSaldoEntradaNoConsignado(dataFechamento, TipoCota.A_VISTA), BigDecimal.ZERO);
        
        BigDecimal valorDiferencasEntrada = Util.nvl(
        		diferencaRepository.obterSaldoDaDiferencaDeEntradaDoConsignadoDoDistribuidor(dataFechamento),BigDecimal.ZERO);
        
        BigDecimal valorCotaAusenteEntrada = Util.nvl(
        		cotaAusenteRepository.obterSaldoDeEntradaDoConsignadoDasCotasAusenteNoDistribuidor(dataFechamento),BigDecimal.ZERO);
        
        resumoConsignado.setValorCE(valorCE);
        
        resumoConsignado.setValorOutrosValoresEntrada(valorDiferencasEntrada.add(valorCotaAusenteEntrada));
        
        resumoConsignado.setValorEntradas(valorCE.add(valorCEAvista).add(resumoConsignado.getValorOutrosValoresEntrada()));
        
       //Valores de Saida no consignado do Distribuidor
        
        BigDecimal valorExpedido = Util.nvl(
        		movimentoEstoqueRepository.obterSaldoDeReparteExpedido(dataFechamento),BigDecimal.ZERO);
        
        valorExpedido = valorExpedido.add(Util.nvl(
                diferencaRepository.obterSaldoDaDiferencaDeSaidaDoConsignadoDoDistribuidorNoDia(dataFechamento),BigDecimal.ZERO));
        
        BigDecimal valorSaidaDoConsignadoAVista =  Util.nvl(
        		this.movimentoEstoqueCotaRepository.obterValorConsignadoCotaAVista(dataFechamento),BigDecimal.ZERO);
        
        resumoConsignado.setValorAVista(valorSaidaDoConsignadoAVista);
        
        resumoConsignado.setValorAVistaCE(valorCEAvista);
        
        resumoConsignado.setValorExpedicao(valorExpedido.subtract(valorSaidaDoConsignadoAVista));
        
        resumoConsignado.setValorOutrosValoresSaidas(this.obterValorSaidaOutros(dataFechamento));
        
        resumoConsignado.setValorSaidas(valorExpedido.add(resumoConsignado.getValorOutrosValoresSaidas()));
       
        resumoConsignado.setSaldoAtual(
                resumoConsignado.getSaldoAnterior().subtract(
                        resumoConsignado.getValorEntradas()).add(resumoConsignado.getValorSaidas()));
        
        resumoFechamentoDiarioConsignado.setResumoConsignado(resumoConsignado);
	}
	
	private BigDecimal obterValorSaidaOutros(final Date dataFechamento){
		
		BigDecimal valorDiferencasSaida =  Util.nvl(
        		diferencaRepository.obterSaldoDaDiferencaDeSaidaDoConsignadoDoDistribuidor(dataFechamento),BigDecimal.ZERO);
        
        BigDecimal valorCotaAusenteSaida = Util.nvl(
        		cotaAusenteRepository.obterSaldoDeSaidaDoConsignadoDasCotasAusenteNoDistribuidor(dataFechamento),BigDecimal.ZERO);
        
        BigDecimal valorVendaSuplementar = Util.nvl(
        		movimentoEstoqueService.obterValorConsignadoDeVendaSuplementar(dataFechamento), BigDecimal.ZERO);
        
        return (valorDiferencasSaida.add(valorVendaSuplementar).add(valorCotaAusenteSaida));
	}

	private ResumoFechamentoDiarioConsignadoDTO obterResumoConsignadoComFechamentoProcessado(Date dataFechamento){
		
		ResumoFechamentoDiarioConsignadoDTO resumoFechamentoDiarioConsignado = new ResumoFechamentoDiarioConsignadoDTO();
			
		ResumoFechamentoDiarioConsignadoDTO.ResumoConsignado resumoConsignado = resumoFechamentoDiarioConsignado.new ResumoConsignado();

		//Consignado
		FechamentoDiarioResumoConsignado resumoConsignadoDia = fechamentoDiarioResumoConsignadoRepository.obterResumoConsignado(dataFechamento);
		
		if(resumoConsignadoDia != null){
			
			resumoConsignado.setSaldoAnterior(resumoConsignadoDia.getSaldoAnterior());
			
			resumoConsignado.setValorEntradas(resumoConsignadoDia.getValorEntradas());
			
			resumoConsignado.setValorSaidas(resumoConsignadoDia.getValorSaidas());
			
			resumoConsignado.setSaldoAtual(resumoConsignadoDia.getSaldoAtual());
			
			resumoConsignado.setValorCE(resumoConsignadoDia.getValorCE());
			
			resumoConsignado.setValorExpedicao(resumoConsignadoDia.getValorExpedicao());
			
			resumoConsignado.setValorOutrosValoresEntrada(resumoConsignadoDia.getValorOutrasMovimentacoesEntrada());
			
			resumoConsignado.setValorOutrosValoresSaidas(resumoConsignadoDia.getValorOutrasMovimentacoesSaida());
			
			resumoConsignado.setValorAVista(resumoConsignadoDia.getValorAVista());
			
			resumoFechamentoDiarioConsignado.setResumoConsignado(resumoConsignado);
		}
				
		return resumoFechamentoDiarioConsignado;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@Transactional(readOnly = true)
    public List<SumarizacaoDividasDTO> sumarizacaoDividasReceberEm(Date data) {
        
		if(this.isDiaComFechamentoRealizado(data)){
			
			return  fechamentoDiarioResumoConsolidadoDividaRepository.sumarizacaoDividas(data, TipoDivida.DIVIDA_A_RECEBER);
		}
			
		return dividaService.sumarizacaoDividasReceberEm(data);
    }

	/**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<SumarizacaoDividasDTO> sumarizacaoDividasVencerApos(Date data) {
        
    	if(this.isDiaComFechamentoRealizado(data)){
    		
    		return  fechamentoDiarioResumoConsolidadoDividaRepository.sumarizacaoDividas(data, TipoDivida.DIVIDA_A_VENCER);
    	}
    		
    	return dividaService.sumarizacaoDividasVencerApos(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<DividaDTO> obterDividasReceberEm(Date data, PaginacaoVO paginacao) {
        
    	if(this.isDiaComFechamentoRealizado(data)){
    		
    		return fechamentoDiarioDividaRepository.obterDividas(data, TipoDivida.DIVIDA_A_RECEBER, paginacao);
    	}
    	
		List<Cobranca> dividas = dividaService.obterDividasReceberEm(data, paginacao);
		
		List<DividaDTO> dividasDTO = new ArrayList<DividaDTO>();
	    
		for (Cobranca divida : dividas) {
	        dividasDTO.add(DividaDTO.fromDivida(divida));
	    }
		
		return dividasDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<DividaDTO> obterDividasVencerApos(Date data, PaginacaoVO paginacao) {
        
    	if(this.isDiaComFechamentoRealizado(data)){
    		
    		return fechamentoDiarioDividaRepository.obterDividas(data, TipoDivida.DIVIDA_A_VENCER, paginacao);
    	}
    	
		List<Cobranca> dividas = dividaService.obterDividasVencerApos(data, paginacao);
		
		List<DividaDTO> dividasDTO = new ArrayList<DividaDTO>();
        
		for (Cobranca divida : dividas) {
            dividasDTO.add(DividaDTO.fromDivida(divida));
        }
		
		return dividasDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public long contarDividasReceberEm(Date data) {
        
    	if(this.isDiaComFechamentoRealizado(data)){
    		
    		return fechamentoDiarioDividaRepository.countDividas(data, TipoDivida.DIVIDA_A_RECEBER);
    	}
    		
    	return dividaService.contarDividasReceberEm(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public long contarDividasVencerApos(Date data) {
        
    	if(this.isDiaComFechamentoRealizado(data)){
    		
    		return fechamentoDiarioDividaRepository.countDividas(data, TipoDivida.DIVIDA_A_VENCER);
    	}
    		
    	return dividaService.contarDividasVencerApos(data);
    }
    
    private FechamentoDiarioDTO salvarResumoFechamentoDiario(Usuario usuario, Date dataFechamento) throws FechamentoDiarioException{
    	
        validarDadosFechamentoDiario(dataFechamento, "Data de fechamento inválida!");
        validarDadosFechamentoDiario(usuario, "Usuário informado inválido!");
    	
        validarDadosFechamentoDiario(usuario, "Usuário não identificado para operação de fechamento do dia!");
        
    	FechamentoDiario fechamento = new FechamentoDiario();
    	FechamentoDiarioDTO.Builder builder = new FechamentoDiarioDTO.Builder(dataFechamento);
    	
    	fechamento.setDataFechamento(dataFechamento);
    	fechamento.setUsuario(usuario);
    	fechamento.setDataCriacao(new Date());
    	
    	fechamento = fechamentoDiarioRepository.merge(fechamento);
    	
    	validarDadosFechamentoDiario(fechamento,null);
    	
    	incluirResumoReparte(fechamento, builder); 
    	
    	incluirResumoEncalhe(fechamento, builder); 
    	
    	incluirResumoSuplementar(fechamento, builder); 
    	
    	List<SumarizacaoDividasDTO> dividasReceber = incluirResumoDividaReceber(fechamento);  
    	builder.dividasReceber(dividasReceber);
    	
    	List<SumarizacaoDividasDTO> dividasVencer = incluirResumoDividaVencer(fechamento); 
    	builder.dividasVencer(dividasVencer);
    	
    	ResumoFechamentoDiarioCotasDTO resumoCotas = incluirResumoCotas(fechamento); 
    	builder.resumoCotas(resumoCotas);
    	
    	ResumoEstoqueDTO resumoEstoque = incluirResumoEstoque(fechamento);
    	builder.resumoEstoque(resumoEstoque);
    	
    	ResumoFechamentoDiarioConsignadoDTO resumoConsignado = incluirResumoConsignado(fechamento); 
    	builder.resumoConsignado(resumoConsignado);
    	
    	List<DiferencaDTO> diferencasDTO = incluirFaltasSobras(fechamento);
    	builder.faltasSobras(diferencasDTO);
    	
    	Date novaDataOperacaoDistribuidor =
    		liberarNovaDataOperacionalParaDistribuidor(dataFechamento);
    	
    	processarSituacoesCota(novaDataOperacaoDistribuidor, dataFechamento);
    	
    	return builder.build();
    }

    private void processarSituacoesCota(Date novaDataOperacaoDistribuidor, 
    									Date antigaDataOperacaoDistribuidor) {

    	List<HistoricoSituacaoCota> situacoesARestaurar = new ArrayList<>();
    	
    	situacoesARestaurar.addAll(
    		this.historicoSituacaoCotaRepository.obterNaoRestauradosComTerminoEm(
    			antigaDataOperacaoDistribuidor));
    	
    	situacoesARestaurar.addAll(
        	this.historicoSituacaoCotaRepository.obterNaoRestauradosComTerminoEm(
        		novaDataOperacaoDistribuidor));
    	
    	for (HistoricoSituacaoCota historicoSituacaoCota : situacoesARestaurar) {
    		
    		Cota cota = this.cotaRepository.buscarPorId(historicoSituacaoCota.getCota().getId());
    		
    		if (cota == null) {
    			
    			throw new RuntimeException("Cota inexistente!");
    		}
    		
    		historicoSituacaoCota.setRestaurado(true);
    		
    		this.historicoSituacaoCotaRepository.alterar(historicoSituacaoCota);
    		
    		HistoricoSituacaoCota novoHistoricoSituacaoCota = new HistoricoSituacaoCota();
    		
    		novoHistoricoSituacaoCota.setDataInicioValidade(novaDataOperacaoDistribuidor);
    		novoHistoricoSituacaoCota.setDataFimValidade(null);
    		novoHistoricoSituacaoCota.setNovaSituacao(historicoSituacaoCota.getSituacaoAnterior());
    		novoHistoricoSituacaoCota.setDescricao(null);
    		novoHistoricoSituacaoCota.setMotivo(null);
    		novoHistoricoSituacaoCota.setSituacaoAnterior(cota.getSituacaoCadastro());
			novoHistoricoSituacaoCota.setDataEdicao(new Date());    		
			novoHistoricoSituacaoCota.setProcessado(false);
			novoHistoricoSituacaoCota.setRestaurado(false);
			novoHistoricoSituacaoCota.setResponsavel(this.usuarioService.getUsuarioLogado());
			novoHistoricoSituacaoCota.setTipoEdicao(TipoEdicao.INCLUSAO);
			novoHistoricoSituacaoCota.setCota(cota);

    		this.historicoSituacaoCotaRepository.adicionar(novoHistoricoSituacaoCota);
    	}
    	
    	List<HistoricoSituacaoCota> situacoesAProcessar = new ArrayList<>();
    	
    	situacoesAProcessar.addAll(
    		this.historicoSituacaoCotaRepository.obterNaoProcessadosComInicioEm(
    			novaDataOperacaoDistribuidor));
    	
    	for (HistoricoSituacaoCota historicoSituacaoCota : situacoesAProcessar) {
    		
    		Cota cota = this.cotaRepository.buscarPorId(historicoSituacaoCota.getCota().getId());
    		
    		if (cota == null) {
    			
    			throw new RuntimeException("Cota inexistente!");
    		}

    		historicoSituacaoCota.setDataEdicao(new Date());    		
    		
			historicoSituacaoCota.setProcessado(true);
			
			cota.setSituacaoCadastro(historicoSituacaoCota.getNovaSituacao());
			
			this.cotaRepository.alterar(cota);

    		this.historicoSituacaoCotaRepository.alterar(historicoSituacaoCota);
    	}
    }
    
    private Date liberarNovaDataOperacionalParaDistribuidor(Date dataFechamento) {
    	
		Distribuidor distribuidor = distribuidorRepository.obter();		
		
		List<Integer> diasSemanaDistribuidorOpera = this.distribuicaoFornecedorRepository.obterCodigosDiaDistribuicaoFornecedor(null);
		
		if(diasSemanaDistribuidorOpera == null || diasSemanaDistribuidorOpera.isEmpty()) {
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Não é possível realizar fechamento diário. Nenhum dia da semana com operação cadastradado para o Distribuidor.");
		}
		
		Date novaData = obterDataValida(distribuidor.getDataOperacao(), diasSemanaDistribuidorOpera);
				
		distribuidor.setDataOperacao(novaData);
		
		distribuidorRepository.alterar(distribuidor);
		
		LOGGER.info("DATA DE OPERAÇÃO DO DISTRIBUIDRO ALTERADA");
		
		return novaData;
	}
	
    /**
     * Retorna a próxima data em que o distribuidor opera.
     * 
     * @param dataAtual
     * @param diasSemanaDistribuidorOpera
     * 
     * @return Date
     */
	public Date obterDataValida(Date dataAtual, List<Integer> diasSemanaDistribuidorOpera) {
		
		Date novaData = DateUtil.adicionarDias(dataAtual, 1);
		
		int codigoDiaCorrente = SemanaUtil.obterDiaDaSemana(novaData);

		if ( 	diasSemanaDistribuidorOpera.contains(codigoDiaCorrente) && 
				!calendarioService.isFeriadoSemOperacao(novaData) && 
				!calendarioService.isFeriadoMunicipalSemOperacao(novaData) 	) {
			
			return novaData;
			
		} else {

			return obterDataValida(novaData, diasSemanaDistribuidorOpera);
			
		}
			
		
	}

	    /**
     * Inclui as diferenças nas informações do fechamento diário
     * 
     * @param fechamento Fechamento diário em processamento
     * @return Lista de diferenças lançadas na data em fechamento
     */
    protected List<DiferencaDTO> incluirFaltasSobras(FechamentoDiario fechamento) {
        Date dataFechamento = fechamento.getDataFechamento();
        
        List<Diferenca> diferencas = obterDiferencas(dataFechamento);
    	List<DiferencaDTO> diferencasDTO = new ArrayList<>(diferencas.size());
    	
    	for (Diferenca diferenca : diferencas) {
    	    fechamento.addDiferenca(FechamentoDiarioDiferenca.fromDiferenca(diferenca));
    	    DiferencaDTO dto = DiferencaDTO.fromDiferenca(diferenca);
    	    diferencasDTO.add(dto);
    	}
        
    	return diferencasDTO;
    }

	private ResumoFechamentoDiarioConsignadoDTO incluirResumoConsignado(FechamentoDiario fechamento) throws FechamentoDiarioException {
		
		ResumoFechamentoDiarioConsignadoDTO resumoConsignado = obterResumoConsignado(fechamento.getDataFechamento());
		
        validarDadosFechamentoDiario(resumoConsignado, "Erro na obtenção dos dados de Resumo Consignado!");
		
		incluirResumoValorConsignado(fechamento, resumoConsignado);
		
		return resumoConsignado;
	}

	private void incluirResumoValorConsignado(FechamentoDiario fechamento,ResumoFechamentoDiarioConsignadoDTO resumoConsignado)
											 throws FechamentoDiarioException {
		
		ResumoConsignado rmConsignado = resumoConsignado.getResumoConsignado();
		
        validarDadosFechamentoDiario(rmConsignado, "Erro na obtenção dos dados de Resumo Consignado!");
		
		FechamentoDiarioResumoConsignado valorResumoConsignado = new FechamentoDiarioResumoConsignado();
		
		valorResumoConsignado.setFechamentoDiario(fechamento);
		valorResumoConsignado.setSaldoAnterior(rmConsignado.getSaldoAnterior());
		valorResumoConsignado.setSaldoAtual(rmConsignado.getSaldoAtual());
		valorResumoConsignado.setValorEntradas(rmConsignado.getValorEntradas());
		valorResumoConsignado.setValorSaidas(rmConsignado.getValorSaidas());
		valorResumoConsignado.setValorCE(rmConsignado.getValorCE());
		valorResumoConsignado.setValorExpedicao(rmConsignado.getValorExpedicao());
		valorResumoConsignado.setValorOutrasMovimentacoesEntrada(rmConsignado.getValorOutrosValoresEntrada());
		valorResumoConsignado.setValorOutrasMovimentacoesSaida(rmConsignado.getValorOutrosValoresSaidas());
		valorResumoConsignado.setValorAVista(rmConsignado.getValorAVista());
		
		fechamentoDiarioResumoConsignadoRepository.adicionar(valorResumoConsignado);
	}

	private ResumoEstoqueDTO incluirResumoEstoque(FechamentoDiario fechamento) throws FechamentoDiarioException {
		
		ResumoEstoqueDTO resumoEstoque = obterResumoEstoque(fechamento.getDataFechamento());
		
        validarDadosFechamentoDiario(resumoEstoque, "Erro na obtenção dos dados de Resumo de Estoque!");
		
		ResumoEstoqueProduto estoqueProduto = resumoEstoque.getResumoEstoqueProduto();
		
        validarDadosFechamentoDiario(estoqueProduto,
                "Erro na obtenção dos dados referente aos Produtos do Resumo de Estoque!");
		
		ResumoEstoqueExemplar estoqueExemplar = resumoEstoque.getResumoEstoqueExemplar();
		
        validarDadosFechamentoDiario(estoqueExemplar,
                "Erro na obtenção dos dados referente aos Exemplares do Resumo de Estoque!");
		
		ValorResumoEstoque valorResumo = resumoEstoque.getValorResumoEstoque();
		
        validarDadosFechamentoDiario(valorResumo,
                "Erro na obtenção dos dados referente aos Valores do Resumo de Estoque!");
		
		incluirResumoEstoque(fechamento,TipoEstoque.LANCAMENTO, estoqueExemplar.getQuantidadeLancamento(), 
				estoqueProduto.getQuantidadeLancamento() ,valorResumo.getValorLancamento());
		
		incluirResumoEstoque(fechamento,TipoEstoque.JURAMENTADO, estoqueExemplar.getQuantidadeJuramentado(), 
				estoqueProduto.getQuantidadeJuramentado() ,valorResumo.getValorJuramentado());
		
		incluirResumoEstoque(fechamento,TipoEstoque.SUPLEMENTAR, estoqueExemplar.getQuantidadeSuplementar(), 
				estoqueProduto.getQuantidadeSuplementar() ,valorResumo.getValorSuplementar());
		
		incluirResumoEstoque(fechamento,TipoEstoque.RECOLHIMENTO, estoqueExemplar.getQuantidadeRecolhimento(), 
				estoqueProduto.getQuantidadeRecolhimento() ,valorResumo.getValorRecolhimento());
		
		incluirResumoEstoque(fechamento,TipoEstoque.DANIFICADO, estoqueExemplar.getQuantidadeDanificados(), 
				estoqueProduto.getQuantidadeDanificados() ,valorResumo.getValorDanificados());
		
		return resumoEstoque;
	}

	private void incluirResumoEstoque(FechamentoDiario fechamentoDiario ,TipoEstoque tipoEstoque, Integer qntExemplares, Integer qntProdutos, BigDecimal valorTotal ) {
		
		FechamentoDiarioResumoEstoque resumoLancamento = new FechamentoDiarioResumoEstoque();
		
		resumoLancamento.setQuantidadeExemplares(qntExemplares);
		resumoLancamento.setQuantidadeProduto(qntProdutos);
		resumoLancamento.setValorTotal(valorTotal);
		resumoLancamento.setTipoEstoque(tipoEstoque);
		resumoLancamento.setFechamentoDiario(fechamentoDiario);
		
		fechamentoDiarioResumoEstoqueRepository.adicionar(resumoLancamento);
	}

	private ResumoFechamentoDiarioCotasDTO incluirResumoCotas(FechamentoDiario fechamento) throws FechamentoDiarioException {
		
		ResumoFechamentoDiarioCotasDTO resumoCotas = obterResumoCotas(fechamento.getDataFechamento());
		
        validarDadosFechamentoDiario(resumoCotas, "Erro na obtenção dos dados de Resumo de Cotas!");
		
		FechamentoDiarioConsolidadoCota consolidadoCota = new FechamentoDiarioConsolidadoCota();
		
		consolidadoCota.setFechamentoDiario(fechamento);
		consolidadoCota.setQuantidadeAtivos(Util.nvl(resumoCotas.getQuantidadeAtivas(),0).intValue());
		consolidadoCota.setQuantidadeAusenteEncalhe(Util.nvl(resumoCotas.getQuantidadeAusentesRecolhimentoEncalhe(),0).intValue());
		consolidadoCota.setQuantidadeAusenteReparte(Util.nvl(resumoCotas.getQuantidadeAusentesExpedicaoReparte(),0) .intValue());
		consolidadoCota.setQuantidadeNovos(Util.nvl(resumoCotas.getQuantidadeNovas(),0).intValue());
		consolidadoCota.setQuantidadeInativas(Util.nvl(resumoCotas.getQuantidadeInativas(),0).intValue());
		consolidadoCota.setQuantidadeTotal(Util.nvl(resumoCotas.getQuantidadeTotal(),0).intValue());
		
		consolidadoCota = fechamentoDiarioConsolidadoCotaRepository.merge(consolidadoCota);
		
		validarDadosFechamentoDiario(consolidadoCota,null);
		
		incluirCotasFechamentoDiario(TipoSituacaoCota.AUSENTE_REPARTE,TipoResumo.AUSENTES_REPARTE,consolidadoCota,fechamento.getDataFechamento());
		
		incluirCotasFechamentoDiario(TipoSituacaoCota.AUSENTE_ENCALHE,TipoResumo.AUSENTES_ENCALHE,consolidadoCota,fechamento.getDataFechamento());
		
		incluirCotasFechamentoDiario(TipoSituacaoCota.INATIVAS,TipoResumo.INATIVAS,consolidadoCota,fechamento.getDataFechamento());
		
		incluirCotasFechamentoDiario(TipoSituacaoCota.NOVAS,TipoResumo.NOVAS,consolidadoCota,fechamento.getDataFechamento());
		
		return resumoCotas;
		
	}

	private void incluirCotasFechamentoDiario(TipoSituacaoCota tipoDetalheCota,
											  TipoResumo tipoResumo,
											  FechamentoDiarioConsolidadoCota fechamentoDiarioConsolidadoCota,
											  Date dataFechamento) {
		
		List<CotaResumoDTO> cotas = obterDetalheCotaFechamentoDiario(dataFechamento, tipoResumo);
		
		if(cotas!= null && !cotas.isEmpty()){
			
			for(CotaResumoDTO item : cotas ){
				
				FechamentoDiarioCota cotaFechamentoDiario = new FechamentoDiarioCota();
				
				cotaFechamentoDiario.setNomeCota(item.getNome());
				cotaFechamentoDiario.setNumeroCota(item.getNumero());
				cotaFechamentoDiario.setTipoSituacaoCota(tipoDetalheCota);
				cotaFechamentoDiario.setFechamentoDiarioConsolidadoCota(fechamentoDiarioConsolidadoCota);
				
				fechamentoDiarioCotaRepository.adicionar(cotaFechamentoDiario);
			}
		}
	}

	private List<SumarizacaoDividasDTO> incluirResumoDividaVencer(FechamentoDiario fechamento) throws FechamentoDiarioException {
		
		List<SumarizacaoDividasDTO> resumoDividas = dividaService.sumarizacaoDividasVencerApos(fechamento.getDataFechamento());
		
        validarDadosFechamentoDiario(resumoDividas, "Erro na obtenção dos dados de Resumo de Dividas A Vencer!");
		
		List<Cobranca> dividas = dividaService.obterDividasVencerApos(fechamento.getDataFechamento(), null);
		
		incluirDividas(fechamento, resumoDividas, dividas,TipoDivida.DIVIDA_A_VENCER);
		
		return resumoDividas;
	}

	private List<SumarizacaoDividasDTO> incluirResumoDividaReceber(FechamentoDiario fechamento) throws FechamentoDiarioException {
		
		List<SumarizacaoDividasDTO> resumoDividas = dividaService.sumarizacaoDividasReceberEm(fechamento.getDataFechamento());
		
        validarDadosFechamentoDiario(resumoDividas, "Erro na obtenção dos dados de Resumo de Dividas A Receber!");
		
		List<Cobranca> dividas = dividaService.obterDividasReceberEm(fechamento.getDataFechamento(), null);
		
		incluirDividas(fechamento, resumoDividas, dividas,TipoDivida.DIVIDA_A_RECEBER);
		
		return resumoDividas;
	}

	private void incluirDividas(FechamentoDiario fechamento,
			List<SumarizacaoDividasDTO> resumoDividas, List<Cobranca> dividas,TipoDivida tipoDividaFechamentoDia) throws FechamentoDiarioException {
		
		FechamentoDiarioConsolidadoDivida resumoConsolidadoDivida = new FechamentoDiarioConsolidadoDivida();
		
		resumoConsolidadoDivida.setFechamentoDiario(fechamento);
		resumoConsolidadoDivida.setTipoDivida(tipoDividaFechamentoDia);
		
		resumoConsolidadoDivida = fechamentoDiarioConsolidadoDividaRepository.merge(resumoConsolidadoDivida);
		
		validarDadosFechamentoDiario(resumoConsolidadoDivida,null);
		
		if(resumoDividas!= null && !resumoDividas.isEmpty()){
			
			for(SumarizacaoDividasDTO item : resumoDividas ){
				
				FechamentoDiarioResumoConsolidadoDivida resumo = new FechamentoDiarioResumoConsolidadoDivida();
				
				resumo.setTipoCobranca(item.getTipoCobranca());
				resumo.setValorInadimplencia(item.getInadimplencia());
				resumo.setValorPago(item.getValorPago());
				resumo.setValorTotal(item.getTotal());
				resumo.setFechamentoDiarioConsolidadoDivida(resumoConsolidadoDivida);
				
				fechamentoDiarioResumoConsolidadoDividaRepository.adicionar(resumo);
			}
		}
		
		if(dividas!= null && !dividas.isEmpty()){
			
			for(Cobranca item : dividas ){
				
				FechamentoDiarioDivida dividaFechamentoDiario = new FechamentoDiarioDivida();
				
				DividaDTO dividaDTO = DividaDTO.fromDivida(item);
				
				dividaFechamentoDiario.setBanco(dividaDTO.getNomeBanco());
				dividaFechamentoDiario.setDataVencimento(dividaDTO.getDataVencimento());
				dividaFechamentoDiario.setTipoCobranca(dividaDTO.getFormaPagamento());
				dividaFechamentoDiario.setIdntificadorDivida(dividaDTO.getIdDivida());
				dividaFechamentoDiario.setNomeCota(dividaDTO.getNomeCota());
				dividaFechamentoDiario.setNossoNumero(dividaDTO.getNossoNumero());
				dividaFechamentoDiario.setNumeroConta(dividaDTO.getContaCorrente());
				dividaFechamentoDiario.setNumeroCota(dividaDTO.getNumeroCota());
				dividaFechamentoDiario.setValor(dividaDTO.getValor());
				dividaFechamentoDiario.setFechamentoDiarioConsolidadoDivida(resumoConsolidadoDivida);
				
				fechamentoDiarioDividaRepository.adicionar(dividaFechamentoDiario);
			}
		}
	}

	private ResumoSuplementarFecharDiaDTO incluirResumoSuplementar(FechamentoDiario fechamento, Builder builder) throws FechamentoDiarioException {
		
		ResumoSuplementarFecharDiaDTO resumoSuplementar = resumoSuplementarFecharDiaService.obterResumoGeralSuplementar(fechamento.getDataFechamento());
		builder.resumoSuplementar(resumoSuplementar);
		
        validarDadosFechamentoDiario(resumoSuplementar, "Erro na obtenção dos dados de Resumo de Suplementar!");
		
		FechamentoDiarioConsolidadoSuplementar consolidadoSuplementar = new FechamentoDiarioConsolidadoSuplementar();
		
		consolidadoSuplementar.setValorEstoqueLogico(resumoSuplementar.getTotalEstoqueLogico());
		consolidadoSuplementar.setValorSaldo(resumoSuplementar.getSaldo());
		consolidadoSuplementar.setValorTransferencia(resumoSuplementar.getTotalTransferencia());
		consolidadoSuplementar.setValorVendas(resumoSuplementar.getTotalVenda());
		consolidadoSuplementar.setFechamentoDiario(fechamento);
		
		consolidadoSuplementar = fechamentoDiarioConsolidadoSuplementarRepository.merge(consolidadoSuplementar);
		
		validarDadosFechamentoDiario(consolidadoSuplementar,null);
		
		List<SuplementarFecharDiaDTO> lancamentosSuplementar = incluirLancamentosSuplementar(consolidadoSuplementar);
		builder.suplementar(lancamentosSuplementar);
	
		incluirVendaSuplementar(fechamento,consolidadoSuplementar);
		
		return resumoSuplementar;
	}

	private List<SuplementarFecharDiaDTO> incluirLancamentosSuplementar(FechamentoDiarioConsolidadoSuplementar consolidadoSuplementar)
			throws FechamentoDiarioException {
		
	    FechamentoDiario fechamentoDiario = consolidadoSuplementar.getFechamentoDiario();
	    
		List<SuplementarFecharDiaDTO> listaSuplementar = this.resumoSuplementarFecharDiaService.obterDadosGridSuplementar(fechamentoDiario.getDataFechamento(), null);
		
		if(listaSuplementar!= null && !listaSuplementar.isEmpty()){
			
			for(SuplementarFecharDiaDTO item : listaSuplementar){
				
				FechamentoDiarioLancamentoSuplementar lancamentoSuplementar = new FechamentoDiarioLancamentoSuplementar();
				
				lancamentoSuplementar.setProdutoEdicao(new ProdutoEdicao(item.getIdProdutoEdicao()));
				lancamentoSuplementar.setFechamentoDiarioConsolidadoSuplementar(consolidadoSuplementar);
				lancamentoSuplementar.setQuantidadeContabilizada(item.getQuantidadeContabil());
				lancamentoSuplementar.setQuantidadeLogico(item.getQuantidadeLogico());
				lancamentoSuplementar.setQuantidadeTransferenciaEntrada(item.getQuantidadeTransferenciaEntrada());
				lancamentoSuplementar.setQuantidadeTransferenciaSaida(item.getQuantidadeTransferenciaSaida());
				lancamentoSuplementar.setQuantidadeVenda(item.getQuantidadeVenda());
				lancamentoSuplementar.setSaldo(item.getSaldo());
				
				fechamentoDiarioLancamentoSuplementarRepository.adicionar(lancamentoSuplementar);
			}
		}
		return listaSuplementar;
	}

	private void incluirVendaSuplementar(FechamentoDiario fechamento, FechamentoDiarioConsolidadoSuplementar consolidadoSuplementar) {
		
		List<VendaFechamentoDiaDTO> vendasSuplementares = resumoSuplementarFecharDiaService.obterVendasSuplementar(fechamento.getDataFechamento(),null);
		
		if(vendasSuplementares != null && !vendasSuplementares.isEmpty()){
			
			for(VendaFechamentoDiaDTO item : vendasSuplementares){
				
				FechamentoDiarioMovimentoVendaSuplementar vendaSuplementar = new FechamentoDiarioMovimentoVendaSuplementar();
				
				vendaSuplementar.setProdutoEdicao(new ProdutoEdicao(item.getIdProdutoEdicao()));
				vendaSuplementar.setQuantidade(item.getQtde());
				vendaSuplementar.setValor(item.getValor());
				vendaSuplementar.setDataRecebimento(DateUtil.parseDataPTBR(item.getDataRecolhimento()));
				vendaSuplementar.setFechamentoDiarioConsolidadoSuplementar(consolidadoSuplementar);
				
				fechamentoDiarioMovimentoVendaSuplementarRepository.adicionar(vendaSuplementar);
			}
		}
	}

	private void incluirResumoEncalhe(FechamentoDiario fechamento, Builder builder) throws FechamentoDiarioException {
		
		ResumoEncalheFecharDiaDTO resumoEncalhe = this.resumoEncalheFecharDiaService.obterResumoGeralEncalhe(fechamento.getDataFechamento());
		builder.resumoEncalhe(resumoEncalhe);
		
        validarDadosFechamentoDiario(resumoEncalhe, "Erro na obtenção dos dados de Resumo de Encalhe!");
		
		FechamentoDiarioConsolidadoEncalhe consolidadoEncalhe = new FechamentoDiarioConsolidadoEncalhe();
		
		consolidadoEncalhe.setSaldo(resumoEncalhe.getSaldo());
		consolidadoEncalhe.setValorFaltaEM(resumoEncalhe.getTotalFaltas());
		consolidadoEncalhe.setValorSobraEM(resumoEncalhe.getTotalSobras());
		consolidadoEncalhe.setValorFisico(resumoEncalhe.getTotalFisico());
		consolidadoEncalhe.setValorJuramentado(resumoEncalhe.getTotalJuramentado());
		consolidadoEncalhe.setValorLogico(resumoEncalhe.getTotalLogico());
		consolidadoEncalhe.setValorVenda(resumoEncalhe.getVenda());
		consolidadoEncalhe.setFechamentoDiario(fechamento);
		
		consolidadoEncalhe = fechamentoDiarioConsolidadoEncalheRepository.merge(consolidadoEncalhe);
		
		validarDadosFechamentoDiario(consolidadoEncalhe,null);
		
		List<EncalheFecharDiaDTO> lancamentosEncalhe = incluirLancamentosEncalhe(fechamento, consolidadoEncalhe);
		builder.encalhe(lancamentosEncalhe);
		
		incluirVendaEncalhe(fechamento, consolidadoEncalhe);
		
	}

	private List<EncalheFecharDiaDTO> incluirLancamentosEncalhe(FechamentoDiario fechamento,FechamentoDiarioConsolidadoEncalhe consolidadoEncalhe)
			throws FechamentoDiarioException {
		
		List<EncalheFecharDiaDTO> listaEncalhe = this.resumoEncalheFecharDiaService.obterDadosGridEncalhe(fechamento.getDataFechamento(), null);
		
		if(listaEncalhe!= null && !listaEncalhe.isEmpty()){
			
			for(EncalheFecharDiaDTO item : listaEncalhe){
				
				FechamentoDiarioLancamentoEncalhe lancamentoEncalhe = new FechamentoDiarioLancamentoEncalhe();
				
				lancamentoEncalhe.setProdutoEdicao(new ProdutoEdicao(item.getIdProdutoEdicao()));
				lancamentoEncalhe.setQuantidadeDiferenca(item.getQtdeDiferenca());
				lancamentoEncalhe.setQuantidadeVendaEncalhe(item.getQtdeVendaEncalhe());
				lancamentoEncalhe.setQuantidade(item.getQtdeLogico());
				lancamentoEncalhe.setQuantidadeFisico(item.getQtdeFisico());
				lancamentoEncalhe.setQuantidadeLogicoJuramentado(item.getQtdeLogicoJuramentado());
				lancamentoEncalhe.setFechamentoDiarioConsolidadoEncalhe(consolidadoEncalhe);
				
				fechamentoDiarioLancamentoEncalheRepository.adicionar(lancamentoEncalhe);
			}
		}
		return listaEncalhe;
	}

	private void incluirVendaEncalhe(FechamentoDiario fechamento,FechamentoDiarioConsolidadoEncalhe consolidadoEncalhe) {
		
		List<VendaFechamentoDiaDTO> vendasEncalhe = resumoEncalheFecharDiaService.obterDadosVendaEncalhe(fechamento.getDataFechamento(), null);

		if(vendasEncalhe!= null && !vendasEncalhe.isEmpty() ){
	
			for(VendaFechamentoDiaDTO item : vendasEncalhe){
				
				FechamentoDiarioMovimentoVendaEncalhe movimentoVendaEncalhe = new FechamentoDiarioMovimentoVendaEncalhe();
				
				movimentoVendaEncalhe.setProdutoEdicao(new ProdutoEdicao(item.getIdProdutoEdicao()));
				movimentoVendaEncalhe.setQuantidade(item.getQtde());
				movimentoVendaEncalhe.setValor(item.getValor());
				movimentoVendaEncalhe.setDataRecebimento(DateUtil.parseDataPTBR(item.getDataRecolhimento()));
				movimentoVendaEncalhe.setFechamentoDiarioConsolidadoEncalhe(consolidadoEncalhe);
				
				fechamentoDiarioMovimentoVendaEncalheRepository.adicionar(movimentoVendaEncalhe);
			}
		}
	}
	
	private void incluirResumoReparte(FechamentoDiario fechamento, Builder builder) throws FechamentoDiarioException {
		
	    SumarizacaoReparteDTO resumoReparte = resumoReparteFecharDiaService.obterSumarizacaoReparte(fechamento.getDataFechamento());
		builder.resumoReparte(resumoReparte);
		
        validarDadosFechamentoDiario(resumoReparte, "Erro na obtenção dos dados de Resumo de Reparte!");
		
		FechamentoDiarioConsolidadoReparte consolidadoReparte = new FechamentoDiarioConsolidadoReparte();
		
		consolidadoReparte.setValorSobraDistribuida(resumoReparte.getTotalSobraDistribuicao());
		consolidadoReparte.setValorDiferenca(resumoReparte.getTotalDiferenca());
		consolidadoReparte.setValorDistribuido(resumoReparte.getTotalDistribuido());
	    consolidadoReparte.setValorFaltas(resumoReparte.getTotalFaltas());
	    consolidadoReparte.setValorReparte(resumoReparte.getTotalReparte());
	    consolidadoReparte.setValorSobras(resumoReparte.getTotalSobras());
	    consolidadoReparte.setValorTransferido(resumoReparte.getTotalTransferencias());
	    consolidadoReparte.setValorADistribuir(resumoReparte.getTotalDistribuir());
	    consolidadoReparte.setFechamentoDiario(fechamento);
	    
	    consolidadoReparte = fechamentoConsolidadoReparteRepository.merge(consolidadoReparte);
	    
	    validarDadosFechamentoDiario(consolidadoReparte,null);
	    
	    List<ReparteFecharDiaDTO> lancamentosReparte = incluirLancamentosReparte(fechamento, consolidadoReparte);
	    builder.reparte(lancamentosReparte);
	}

	private List<ReparteFecharDiaDTO> incluirLancamentosReparte(FechamentoDiario fechamento,FechamentoDiarioConsolidadoReparte consolidadoReparte)
			throws FechamentoDiarioException {
		
		List<ReparteFecharDiaDTO> listaReparte = resumoReparteFecharDiaService.obterResumoReparte(fechamento.getDataFechamento(), null);
		
	    if(listaReparte!= null && !listaReparte.isEmpty()){
	    	
		    for(ReparteFecharDiaDTO item : listaReparte){
		    	
		    	FechamentoDiarioLancamentoReparte movimentoReparte = new FechamentoDiarioLancamentoReparte();
		    	
		    	movimentoReparte.setProdutoEdicao(new ProdutoEdicao(item.getIdProdutoEdicao()));
		    	movimentoReparte.setQuantidadeADistribuir(item.getQtdeDistribuir());
		    	movimentoReparte.setQuantidadeDiferenca(item.getQtdeDiferenca());
		    	movimentoReparte.setQuantidadeDistribuido(item.getQtdeDistribuido());
		    	movimentoReparte.setQuantidadeFaltaEM(item.getQtdeFalta());
		    	movimentoReparte.setQuantidadeReparte(item.getQtdeReparte());
		    	movimentoReparte.setQuantidadeSobraDistribuido(item.getQtdeSobraDistribuicao());
		    	movimentoReparte.setQuantidadeSobraEM(item.getQtdeSobra());
		    	movimentoReparte.setQuantidadeTranferencia(item.getQtdeTransferencia());
		    	movimentoReparte.setFechamentoDiarioConsolidadoReparte(consolidadoReparte);
		    	
		    	fechamentoLancamentoReparteRepository.adicionar(movimentoReparte);
		    }
	    }
	    
	    return listaReparte;
	}

	private void processarControleDeAprovacao() {

		List<GrupoMovimentoFinaceiro> gruposMovimentoFinanceiro = obterGruposMovimentoFinaceiro();

		List<Movimento> movimentosPendentes = this.fecharDiaRepository.obterMovimentosPorStatusData(gruposMovimentoFinanceiro, this.distribuidorRepository.obterDataOperacaoDistribuidor(), 
				StatusAprovacao.PENDENTE);

		for (Movimento movimento : movimentosPendentes) {

			movimento.setData(DateUtil.adicionarDias(movimento.getData(), 1));

			this.movimentoRepository.merge(movimento);
		}
	}

	private List<GrupoMovimentoFinaceiro> obterGruposMovimentoFinaceiro() {
		
		List<GrupoMovimentoFinaceiro> grupos = new ArrayList<GrupoMovimentoFinaceiro>();
		
		grupos.add(GrupoMovimentoFinaceiro.CREDITO);
		grupos.add(GrupoMovimentoFinaceiro.DEBITO);
		grupos.add(GrupoMovimentoFinaceiro.POSTERGADO_DEBITO);
		grupos.add(GrupoMovimentoFinaceiro.POSTERGADO_CREDITO);
		grupos.add(GrupoMovimentoFinaceiro.POSTERGADO_NEGOCIACAO);
		
		return grupos;
	}
	
	@Override
	@Transactional(readOnly=true)
	public ResumoEstoqueDTO obterResumoEstoque(Date dataOperacao){		
		
		if(this.isDiaComFechamentoRealizado(dataOperacao)){
			
			return this.obterResumoEstoqueComFechamentoProcessado(dataOperacao);
		}
		
		return obterResumoComFechamentoNaoProcessado(dataOperacao);
	}

	private ResumoEstoqueDTO obterResumoComFechamentoNaoProcessado(Date dataOperacao) {
		
		ResumoEstoqueDTO resumoDTO = new ResumoEstoqueDTO();
		ResumoEstoqueDTO.ResumoEstoqueExemplar exemplar = resumoDTO.new ResumoEstoqueExemplar();
		ResumoEstoqueDTO.ResumoEstoqueProduto produto = resumoDTO.new ResumoEstoqueProduto();
		ResumoEstoqueDTO.ValorResumoEstoque valor = resumoDTO.new ValorResumoEstoque();
		
		FiltroConsultaVisaoEstoque filtro = new FiltroConsultaVisaoEstoque();
		
		filtro.setDataMovimentacao(dataOperacao);
		
		VisaoEstoqueDTO visaoEstoqueDTO = null;
		
		filtro.setTipoEstoque(TipoEstoque.LANCAMENTO.toString());
		visaoEstoqueDTO = visaoEstoqueRepository.obterVisaoEstoque(filtro);
		
		exemplar.setQuantidadeLancamento(visaoEstoqueDTO.getExemplares().intValue());
		produto.setQuantidadeLancamento(visaoEstoqueDTO.getProdutos().intValue());
		valor.setValorLancamento(visaoEstoqueDTO.getValor());
		
		filtro.setTipoEstoque(TipoEstoque.LANCAMENTO_JURAMENTADO.toString());
		visaoEstoqueDTO = visaoEstoqueRepository.obterVisaoEstoque(filtro);
		
		exemplar.setQuantidadeJuramentado(visaoEstoqueDTO.getExemplares().intValue());
		produto.setQuantidadeJuramentado(visaoEstoqueDTO.getProdutos().intValue());
		valor.setValorJuramentado(visaoEstoqueDTO.getValor());
		
		filtro.setTipoEstoque(TipoEstoque.SUPLEMENTAR.toString());
		visaoEstoqueDTO = visaoEstoqueRepository.obterVisaoEstoque(filtro);
		
		exemplar.setQuantidadeSuplementar(visaoEstoqueDTO.getExemplares().intValue());
		produto.setQuantidadeSuplementar(visaoEstoqueDTO.getProdutos().intValue());
		valor.setValorSuplementar(visaoEstoqueDTO.getValor());
		
		filtro.setTipoEstoque(TipoEstoque.RECOLHIMENTO.toString());
		visaoEstoqueDTO = visaoEstoqueRepository.obterVisaoEstoque(filtro);
		
		exemplar.setQuantidadeRecolhimento(visaoEstoqueDTO.getExemplares().intValue());
		produto.setQuantidadeRecolhimento(visaoEstoqueDTO.getProdutos().intValue());
		valor.setValorRecolhimento(visaoEstoqueDTO.getValor());
		
		filtro.setTipoEstoque(TipoEstoque.PRODUTOS_DANIFICADOS.toString());
		visaoEstoqueDTO = visaoEstoqueRepository.obterVisaoEstoque(filtro);
		
		exemplar.setQuantidadeDanificados(visaoEstoqueDTO.getExemplares().intValue());
		produto.setQuantidadeDanificados(visaoEstoqueDTO.getProdutos().intValue());
		valor.setValorDanificados(visaoEstoqueDTO.getValor());
		
		resumoDTO.setResumEstoqueExemplar(exemplar);
		resumoDTO.setResumoEstoqueProduto(produto);
		resumoDTO.setValorResumoEstoque(valor);
		
		return resumoDTO;
	}
	
	private ResumoEstoqueDTO obterResumoEstoqueComFechamentoProcessado(Date dataFechamento){
		
		ResumoEstoqueDTO resumoDTO = new ResumoEstoqueDTO();
		ResumoEstoqueDTO.ResumoEstoqueExemplar exemplar = resumoDTO.new ResumoEstoqueExemplar();
		ResumoEstoqueDTO.ResumoEstoqueProduto produto = resumoDTO.new ResumoEstoqueProduto();
		ResumoEstoqueDTO.ValorResumoEstoque valor = resumoDTO.new ValorResumoEstoque();
		
		FechamentoDiarioResumoEstoque resumoEstoque = 
				fechamentoDiarioResumoEstoqueRepository.obterResumoEstoque(dataFechamento, TipoEstoque.LANCAMENTO);
		
		if(resumoEstoque != null){
			exemplar.setQuantidadeLancamento(resumoEstoque.getQuantidadeExemplares());
			produto.setQuantidadeLancamento(resumoEstoque.getQuantidadeProduto());
			valor.setValorLancamento(resumoEstoque.getValorTotal());
		}
	
		resumoEstoque = 
				fechamentoDiarioResumoEstoqueRepository.obterResumoEstoque(dataFechamento, TipoEstoque.LANCAMENTO_JURAMENTADO);
		
		if(resumoEstoque != null){
			exemplar.setQuantidadeJuramentado(resumoEstoque.getQuantidadeExemplares());
			produto.setQuantidadeJuramentado(resumoEstoque.getQuantidadeProduto());
			valor.setValorJuramentado(resumoEstoque.getValorTotal());			
		}
		
		resumoEstoque = 
				fechamentoDiarioResumoEstoqueRepository.obterResumoEstoque(dataFechamento, TipoEstoque.SUPLEMENTAR);
		
		if(resumoEstoque != null){
			exemplar.setQuantidadeSuplementar(resumoEstoque.getQuantidadeExemplares());
			produto.setQuantidadeSuplementar(resumoEstoque.getQuantidadeProduto());
			valor.setValorSuplementar(resumoEstoque.getValorTotal());
		}
		
		resumoEstoque = 
				fechamentoDiarioResumoEstoqueRepository.obterResumoEstoque(dataFechamento, TipoEstoque.RECOLHIMENTO);
		
		if(resumoEstoque != null){
			exemplar.setQuantidadeRecolhimento(resumoEstoque.getQuantidadeExemplares());
			produto.setQuantidadeRecolhimento(resumoEstoque.getQuantidadeProduto());
			valor.setValorRecolhimento(resumoEstoque.getValorTotal());
		}
		
		resumoEstoque = 
				fechamentoDiarioResumoEstoqueRepository.obterResumoEstoque(dataFechamento, TipoEstoque.PRODUTOS_DANIFICADOS);
		
		if(resumoEstoque != null){
			exemplar.setQuantidadeDanificados(resumoEstoque.getQuantidadeExemplares());
			produto.setQuantidadeDanificados(resumoEstoque.getQuantidadeProduto());
			valor.setValorDanificados(resumoEstoque.getValorTotal());
		}
		
		resumoDTO.setResumEstoqueExemplar(exemplar);
		resumoDTO.setResumoEstoqueProduto(produto);
		resumoDTO.setValorResumoEstoque(valor);
		
		return resumoDTO;
	}
	
	private void validarDadosFechamentoDiario(Object objeto, String mensagem) throws FechamentoDiarioException{
		
		if(mensagem == null){
            mensagem = "Erro na gravação do Resumo de Fechamento do Dia!";
		}
		
		if(objeto == null){
			throw new FechamentoDiarioException(mensagem);
		}
	}

	@Transactional
	@Override
	public FechamentoDiarioDTO processarFechamentoDoDia(Usuario usuario, Date dataFechamento){
		
		LOGGER.info("FECHAMENTO DIARIO - ATUALIZADO DESCONTO LOGISTICA");
		
		processarControleDeAprovacao();

		LOGGER.info("FECHAMENTO DIARIO - PROCESSO CONTROLE DE APROVACAO CONCLUIDO");
		
		try {

			FechamentoDiarioDTO fechamentoDiarioDTO = salvarResumoFechamentoDiario(usuario, dataFechamento);

			LOGGER.info("FECHAMENTO DIARIO - SALVO RESUMO FECHAMENTO DIARIO");
			
			atualizarHistoricoEstoqueProduto(dataFechamento);

			LOGGER.info("FECHAMENTO DIARIO - ATUALIZADO HISTORICO ESTOQUE PRODUTO ");
			
			this.processarLancamentosRecolhimento(usuario);

			LOGGER.info("FECHAMENTO DIARIO - PROCESSADOS LANCAMENTO RECOLHIMENTO");
			
			processarDividasNaoPagas(usuario, dataFechamento);
			
			LOGGER.info("FECHAMENTO DIARIO - PROCESSADA DIVIDAS NAO PAGAS");
			
			fixacaoReparteService.verificarFixacao(dataFechamento);
			
			LOGGER.info("FECHAMENTO DIARIO - PROCESSADA DEBITOS COTAS TAXA DE ENTREGA");
			
			processarDebitosCotaTaxasEntrega(dataFechamento);
			
			return fechamentoDiarioDTO;
		
		} catch (FechamentoDiarioException e) {
			
			LOGGER.error("FALHA AO PROCESSAR FECHAMENTO DO DIA", e);
			
			throw new ValidacaoException(TipoMensagem.ERROR, e.getMessage());
		}
	}
	
	private void processarDebitosCotaTaxasEntrega(Date dataFechamento){
		
		this.debitoCreditoCotaService.processarDebitoDeDistribuicaoDeEntregaDaCota(dataFechamento);
	}
	
	private void processarDividasNaoPagas(Usuario usuario, Date dataPagamento) {

		this.boletoService.adiarDividaBoletosNaoPagos(usuario, dataPagamento);
	}

	private void atualizarHistoricoEstoqueProduto(Date dataFechamento) {
		
		List<EstoqueProdutoDTO> estoquesProdutos = estoqueProdutoRepository.buscarEstoquesProdutos();
		
		for(EstoqueProdutoDTO ep : estoquesProdutos) {
			
			HistoricoEstoqueProduto hep = new HistoricoEstoqueProduto();
			hep.setData(dataFechamento);
			hep.setProdutoEdicao(new ProdutoEdicao(ep.getProdutoEdicaoId()));
			hep.setQtde(ep.getQtde() != null ? ep.getQtde() : BigInteger.ZERO);
			hep.setQtdeDanificado(ep.getQtdeDanificado() != null ? ep.getQtdeDanificado() : BigInteger.ZERO);
			hep.setQtdeDevolucaoEncalhe(ep.getQtdeDevolucaoEncalhe() != null ? ep.getQtdeDevolucaoEncalhe() : BigInteger.ZERO);
			hep.setQtdeSuplementar(ep.getQtdeSuplementar() != null ? ep.getQtdeSuplementar() : BigInteger.ZERO);
			hep.setQtdeJuramentado(ep.getQtdeJuramentado() != null ? ep.getQtdeJuramentado() : BigInteger.ZERO);
			
			historicoEstoqueProdutoRepository.adicionar(hep);
		}
	}

	private void processarLancamentosRecolhimento(Usuario usuario) {
		
		Date dataOperacao = this.distribuidorRepository.obterDataOperacaoDistribuidor();
		
		LOGGER.info("Data de Operação após alteração no distribuidor: " + dataOperacao);
		
		this.processarLancamentosEmRecolhimento(dataOperacao, usuario);
		
		this.processarLancamentosVencidos(dataOperacao, usuario);
		
		//TODO Habilitar esse processo quando o NDSBKLOG 9 for concluido e testado
		//this.processarLancamentosFechados(dataOperacao, usuario);
	}

	private void processarLancamentosEmRecolhimento(Date dataOperacao, Usuario usuario) {
		
		List<Lancamento> lancamentos = this.lancamentoRepository.obterLancamentosBalanceadosPorDataRecolhimentoDistrib(dataOperacao);
		
		for (Lancamento lancamento : lancamentos) {
			
			ProdutoEdicao produtoEdicao = lancamento.getProdutoEdicao();
			
			LOGGER.info(String.format("Lançamento alterado para \"EM_RECOLHIMENTO\": %s, Cod.: %s, Ed.: %s", 
					produtoEdicao.getNomeComercial(), 
					produtoEdicao.getProduto().getCodigo(), 
					produtoEdicao.getNumeroEdicao()));
			
			lancamento.setStatus(StatusLancamento.EM_RECOLHIMENTO);
			lancamento.setUsuario(usuario);
			
			this.lancamentoRepository.merge(lancamento);
		}
	}
	
	private void processarLancamentosVencidos(Date dataOperacao, Usuario usuario) {
		
		Integer ultimoDiaRecolhimento = this.obterUltimoDiaRecolhimento();
		
		Date dataBase = calendarioService.subtrairDiasUteisComOperacao(dataOperacao, ultimoDiaRecolhimento - 1);
		
		List<Lancamento> lancamentos = this.lancamentoRepository.obterLancamentosEmRecolhimentoVencidos(dataBase);
		
		for (Lancamento lancamento : lancamentos) {
			
			lancamento.setStatus(StatusLancamento.RECOLHIDO);
			lancamento.setUsuario(usuario);
			
			this.lancamentoRepository.merge(lancamento);
		}
	}
	
	private void processarLancamentosFechados(Date dataOperacao, Usuario usuario) {
		
		//TODO Habilitar esse processo quando o NDSBKLOG 9 for concluido e testado
		
		List<Lancamento> lancamentos = this.lancamentoRepository.obterLancamentosEmRecolhimentoParaFechamento(dataOperacao);
		
		if(lancamentos!= null && !lancamentos.isEmpty()){

			for (Lancamento lancamento : lancamentos) {
				
				lancamento.setStatus(StatusLancamento.FECHADO);
				lancamento.setUsuario(usuario);
				
				this.lancamentoRepository.merge(lancamento);
			}
		}
		
	}

	private Integer obterUltimoDiaRecolhimento() {
		
		Distribuidor distribuidor = this.distribuidorRepository.obter();
		
		boolean primeiro = distribuidor.getParametrosRecolhimentoDistribuidor().isDiaRecolhimentoPrimeiro();
		boolean segundo = distribuidor.getParametrosRecolhimentoDistribuidor().isDiaRecolhimentoSegundo();
		boolean terceiro = distribuidor.getParametrosRecolhimentoDistribuidor().isDiaRecolhimentoTerceiro();
		boolean quarto = distribuidor.getParametrosRecolhimentoDistribuidor().isDiaRecolhimentoQuarto();
		boolean quinto = distribuidor.getParametrosRecolhimentoDistribuidor().isDiaRecolhimentoQuinto();
		
		if (quinto) {
			return 5;
		} else if (quarto) {
			return 4;
		} else if (terceiro) {
			return 3;
		} else if (segundo) {
			return 2;
		} else if (primeiro) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	@Transactional
	public void setLockBancoDeDados(boolean lockBancoDeDados) {
		Distribuidor distribuidor = this.distribuidorRepository.obter();
		
		distribuidor.setFechamentoDiarioEmAndamento(lockBancoDeDados);
		
		distribuidorRepository.alterar(distribuidor);
		
	}
	
    /**
     * {@inheritDoc}
     */
	@Override
    @Transactional(readOnly = true)
    public List<Diferenca> obterDiferencas(Date data) {
        Objects.requireNonNull(data, "Data para recuperação das diferenças não deve ser nula!");
        return diferencaRepository.obterDiferencas(data,StatusConfirmacao.CONFIRMADO);
    }
	
	 /**
     * {@inheritDoc}
     */
	@Override
	@Transactional
	public void transferirDiferencasParaEstoqueDePerdaGanho(Date dataOperacao, Long idUsuario) {
		
		List<Diferenca> diferencasATransferir = this.diferencaRepository.obterDiferencas(dataOperacao, StatusConfirmacao.PENDENTE);
		
		for (Diferenca diferenca : diferencasATransferir) {
			
			diferenca.setStatusConfirmacao(StatusConfirmacao.CONFIRMADO);
			
			TipoDiferenca novoTipoDiferenca = null;
			
			LancamentoDiferenca lancamentoDiferenca = diferenca.getLancamentoDiferenca();
			
			switch (diferenca.getTipoDiferenca()) {
			
				case FALTA_DE: 
					
					novoTipoDiferenca = TipoDiferenca.PERDA_DE;
					
					break;
				
				case FALTA_EM: 
					
					novoTipoDiferenca = TipoDiferenca.PERDA_EM;
					
					break;
					
				case SOBRA_DE:
					
					novoTipoDiferenca = TipoDiferenca.GANHO_DE;
					
					break;
				
				case SOBRA_EM:
					
					novoTipoDiferenca = TipoDiferenca.GANHO_EM;
					
					break;

				case ALTERACAO_REPARTE_PARA_SUPLEMENTAR:

				    novoTipoDiferenca = TipoDiferenca.PERDA_EM;
                    
                    break;
                    
				default:
					
                throw new RuntimeException("Tipo de Diferença não identificado");
			}
			
			if (lancamentoDiferenca != null) {
				
				if (novoTipoDiferenca.isPerda()) {
					
					lancamentoDiferenca.setStatus(StatusAprovacao.PERDA);
					
				} else if (novoTipoDiferenca.isGanho()) {
					
					lancamentoDiferenca.setStatus(StatusAprovacao.GANHO);
				}
			}
			
			diferenca.setTipoDiferenca(novoTipoDiferenca);
			
			this.diferencaRepository.merge(diferenca);
			
			TipoMovimentoEstoque tipoMovimentoEstoque = 
				this.tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(
					novoTipoDiferenca.getTipoMovimentoEstoque());
			
			this.movimentoEstoqueService.gerarMovimentoEstoque(
				null, diferenca.getProdutoEdicao().getId(), idUsuario, 
					diferenca.getQtde(), tipoMovimentoEstoque);
		}
	}
	
	@Transactional(readOnly=true)
	public boolean isDiaComFechamentoRealizado(Date dataFechamento){
		
		Date dataEmOperacao = distribuidorRepository.obterDataOperacaoDistribuidor();
		
		return (dataEmOperacao == null) ? false : (dataFechamento.compareTo(dataEmOperacao) < 0) ;
	}
  
}
