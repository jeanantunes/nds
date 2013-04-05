
package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaResumoDTO;
import br.com.abril.nds.dto.EncalheFecharDiaDTO;
import br.com.abril.nds.dto.ReparteFecharDiaDTO;
import br.com.abril.nds.dto.ResumoEncalheFecharDiaDTO;
import br.com.abril.nds.dto.ResumoFechamentoDiarioConsignadoDTO;
import br.com.abril.nds.dto.ResumoFechamentoDiarioConsignadoDTO.ResumoAVista;
import br.com.abril.nds.dto.ResumoFechamentoDiarioConsignadoDTO.ResumoConsignado;
import br.com.abril.nds.dto.ResumoFechamentoDiarioCotasDTO;
import br.com.abril.nds.dto.ResumoSuplementarFecharDiaDTO;
import br.com.abril.nds.dto.SuplementarFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoLancamentoFaltaESobraFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoRecebimentoFisicoFecharDiaDTO;
import br.com.abril.nds.dto.VendaFechamentoDiaDTO;
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
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ParametrosAprovacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.fechar.dia.FechamentoDiario;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoCota;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoDivida;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioConsolidadoDivida.TipoDividaFechamentoDia;
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
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoAvista;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoConsignado;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoConsolidadoDivida;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoEstoque;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.movimentacao.Movimento;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ConferenciaEncalheParcialRepository;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DiferencaEstoqueRepository;
import br.com.abril.nds.repository.DistribuicaoFornecedorRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
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
import br.com.abril.nds.repository.FechamentoDiarioResumoAvistaRepository;
import br.com.abril.nds.repository.FechamentoDiarioResumoConsignadoRepository;
import br.com.abril.nds.repository.FechamentoDiarioResumoConsolidadoDividaRepository;
import br.com.abril.nds.repository.FechamentoDiarioResumoEstoqueRepository;
import br.com.abril.nds.repository.FecharDiaRepository;
import br.com.abril.nds.repository.FormaCobrancaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.MovimentoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.DividaService;
import br.com.abril.nds.service.FecharDiaService;
import br.com.abril.nds.service.ImpressaoDividaService;
import br.com.abril.nds.service.ResumoEncalheFecharDiaService;
import br.com.abril.nds.service.ResumoReparteFecharDiaService;
import br.com.abril.nds.service.ResumoSuplementarFecharDiaService;
import br.com.abril.nds.service.exception.FechamentoDiarioException;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;

@Service
public class FecharDiaServiceImpl implements FecharDiaService {
	
	@Autowired
	private FecharDiaRepository fecharDiaRepository;
	
	@Autowired
	private ImpressaoDividaService impressaoDividaService;
	
	@Autowired
	private FormaCobrancaRepository formaCobrancaRepository;
	
	@Autowired
	private DividaService dividaService;

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
	private FechamentoDiarioResumoAvistaRepository fechamentoDiarioResumoAvistaRepository;
	
	@Autowired
	private FechamentoDiarioResumoEstoqueRepository fechamentoDiarioResumoEstoqueRepository;
	
	@Autowired
	private FechamentoDiarioConsolidadoEncalheRepository fechamentoDiarioConsolidadoEncalheRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
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
	private ConferenciaEncalheParcialRepository conferenciaEncalheParcialRepository;
	
	@Autowired
	private ConsolidadoFinanceiroRepository consolidadoFinanceiroRepository;
	
	@Override
	@Transactional
	public boolean existeCobrancaParaFecharDia(Date dataOperacaoDistribuidor) {
		Date diaDeOperaoMenosUm = DateUtil.subtrairDias(dataOperacaoDistribuidor, 1);
		return this.fecharDiaRepository.existeCobrancaParaFecharDia(diaDeOperaoMenosUm);
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
			}
			
			return this.fecharDiaRepository.existePendenciasDeAprovacao(
					dataOperacao, StatusAprovacao.PENDENTE, movimentosVerificaAprovacao);
		} else {
			
			return false;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean existeGeracaoDeCobranca(Date dataOperacao) {
		
		Long qtd = 
				this.consolidadoFinanceiroRepository.obterQuantidadeConsolidadosDia(dataOperacao);
		
		return qtd > 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public ResumoFechamentoDiarioCotasDTO obterResumoCotas(Date dataFechamento) {

		Long quantidadeTotal = this.cotaRepository.obterQuantidadeCotas(null);
		
		Long quantidadeAtivas = this.cotaRepository.obterQuantidadeCotas(SituacaoCadastro.ATIVO);
		
		List<CotaResumoDTO> ausentesExpedicaoReparte = 
			this.cotaRepository.obterCotasAusentesNaExpedicaoDoReparteEm(dataFechamento);
		
		List<CotaResumoDTO> ausentesRecolhimentoEncalhe = 
			this.cotaRepository.obterCotasAusentesNoRecolhimentoDeEncalheEm(dataFechamento);
		
		List<CotaResumoDTO> novas = this.cotaRepository.obterCotasComInicioAtividadeEm(dataFechamento);
		
		List<CotaResumoDTO> inativas = this.cotaRepository.obterCotas(SituacaoCadastro.INATIVO);
		
		return new ResumoFechamentoDiarioCotasDTO(
			quantidadeTotal, quantidadeAtivas, ausentesExpedicaoReparte, 
				ausentesRecolhimentoEncalhe, novas, inativas);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public ResumoFechamentoDiarioConsignadoDTO obterResumoConsignado(Date dataFechamento) {
		
		ResumoFechamentoDiarioConsignadoDTO resumoFechamentoDiarioConsignado = 
			new ResumoFechamentoDiarioConsignadoDTO();
		
		ResumoFechamentoDiarioConsignadoDTO.ResumoConsignado resumoConsignado = 
			resumoFechamentoDiarioConsignado.new ResumoConsignado();

		resumoConsignado.setSaldoAnterior(
			this.movimentoFinanceiroCotaRepository.obterSaldoDistribuidor(
				DateUtil.subtrairDias(dataFechamento, 1), TipoCota.CONSIGNADO, null));

		resumoConsignado.setValorEntradas(
			this.movimentoFinanceiroCotaRepository.obterSaldoDistribuidor(
				dataFechamento, TipoCota.CONSIGNADO, OperacaoFinaceira.DEBITO));
		
		resumoConsignado.setValorSaidas(
			this.movimentoFinanceiroCotaRepository.obterSaldoDistribuidor(
				dataFechamento, TipoCota.CONSIGNADO, OperacaoFinaceira.CREDITO));
		
		resumoConsignado.setSaldoAtual(
			resumoConsignado.getSaldoAnterior().add(
				resumoConsignado.getValorEntradas()).subtract(resumoConsignado.getValorSaidas()));
		
		resumoFechamentoDiarioConsignado.setResumoConsignado(resumoConsignado);
		
		ResumoFechamentoDiarioConsignadoDTO.ResumoAVista resumoAVista = 
			resumoFechamentoDiarioConsignado.new ResumoAVista();
		
		resumoAVista.setSaldoAnterior(
			this.movimentoFinanceiroCotaRepository.obterSaldoDistribuidor(
				DateUtil.subtrairDias(dataFechamento, 1), TipoCota.A_VISTA, null));

		resumoAVista.setValorEntradas(
			this.movimentoFinanceiroCotaRepository.obterSaldoDistribuidor(
				dataFechamento, TipoCota.A_VISTA, OperacaoFinaceira.DEBITO));
		
		resumoAVista.setValorSaidas(
			this.movimentoFinanceiroCotaRepository.obterSaldoDistribuidor(
				dataFechamento, TipoCota.A_VISTA, OperacaoFinaceira.CREDITO));
		
		resumoAVista.setSaldoAtual(
			resumoAVista.getSaldoAnterior().add(
				resumoAVista.getValorEntradas()).subtract(resumoAVista.getValorSaidas()));
		
		resumoFechamentoDiarioConsignado.setResumoAVista(resumoAVista);
		
		return resumoFechamentoDiarioConsignado;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	@Transactional(readOnly = true)
    public List<SumarizacaoDividasDTO> sumarizacaoDividasReceberEm(Date data) {
        return dividaService.sumarizacaoDividasReceberEm(data);
    }

	/**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<SumarizacaoDividasDTO> sumarizacaoDividasVencerApos(Date data) {
        return dividaService.sumarizacaoDividasVencerApos(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Divida> obterDividasReceberEm(Date data, PaginacaoVO paginacao) {
        return dividaService.obterDividasReceberEm(data, paginacao);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Divida> obterDividasVencerApos(Date data, PaginacaoVO paginacao) {
        return dividaService.obterDividasVencerApos(data, paginacao);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public long contarDividasReceberEm(Date data) {
        return dividaService.contarDividasReceberEm(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public long contarDividasVencerApos(Date data) {
        return dividaService.contarDividasVencerApos(data);
    }
    
    private FechamentoDiarioDTO salvarResumoFechamentoDiario(Usuario usuario, Date dataFechamento) throws FechamentoDiarioException{
    	
    	validarDadosFechamentoDiario(dataFechamento, "Data de fechamento inválida!");
    	validarDadosFechamentoDiario(usuario, "Usúario informado inválido!");
    	
    	validarDadosFechamentoDiario(usuario, "Usúario não identificado para operação de fechamento do dia!");
    	
    	FechamentoDiario fechamento = new FechamentoDiario();
    	FechamentoDiarioDTO.Builder builder = new FechamentoDiarioDTO.Builder(dataFechamento);
    	
    	fechamento.setDataFechamento(dataFechamento);
    	fechamento.setUsuario(usuario);
    	
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
    	
    	liberarNovaDataOperacionalParaDistribuidor(dataFechamento);
    	
    	return builder.build();
    }

    private void liberarNovaDataOperacionalParaDistribuidor(Date dataFechamento) {
    	
		Distribuidor distribuidor = distribuidorRepository.obter();		
		
		List<Integer> diasSemanaDistribuidorOpera = this.distribuicaoFornecedorRepository.obterCodigosDiaDistribuicaoFornecedor(null, null);
		
		if(diasSemanaDistribuidorOpera == null || diasSemanaDistribuidorOpera.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Não é possível realizar fechamento diário. Nenhum dia da semana com operação cadastradado para o Distribuidor."); 
		}
		
		Date novaData = obterDataValida(distribuidor.getDataOperacao(), diasSemanaDistribuidorOpera);
				
		distribuidor.setDataOperacao(novaData);
		
		distribuidorRepository.alterar(distribuidor);
		
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
		
		int codigoDiaCorrente = DateUtil.obterDiaDaSemana(novaData);

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
     * @param fechamento
     *            Fechamento diário em processamento
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
		
		incluirResumoValorAvista(fechamento, resumoConsignado);
		
		return resumoConsignado;
	}

	private void incluirResumoValorAvista(FechamentoDiario fechamento,ResumoFechamentoDiarioConsignadoDTO resumoConsignado)
										 throws FechamentoDiarioException {
		
		ResumoAVista resumoAvista = resumoConsignado.getResumoAVista();
		
		validarDadosFechamentoDiario(resumoAvista, "Erro na obtenção dos dados de Resumo Consignado!");
		
		FechamentoDiarioResumoAvista valorResumoAvista = new FechamentoDiarioResumoAvista();
		
		valorResumoAvista.setFechamentoDiario(fechamento);
		valorResumoAvista.setSaldoAnterior(resumoAvista.getSaldoAnterior());
		valorResumoAvista.setSaldoAtual(resumoAvista.getSaldoAtual());
		valorResumoAvista.setValorEntradas(resumoAvista.getValorEntradas());
		valorResumoAvista.setValorSaidas(resumoAvista.getValorSaidas());
		
		fechamentoDiarioResumoAvistaRepository.adicionar(valorResumoAvista);
	}

	private void incluirResumoValorConsignado(FechamentoDiario fechamento,ResumoFechamentoDiarioConsignadoDTO resumoConsignado)
											 throws FechamentoDiarioException {
		
		ResumoConsignado rmConsignado = resumoConsignado.getResumoConsignado();
		
		validarDadosFechamentoDiario(rmConsignado, "Erro na obtenção dos dados de Resumo Consignado!");
		
		FechamentoDiarioResumoConsignado valorResumoConsignado = new FechamentoDiarioResumoConsignado();
		
		valorResumoConsignado.setFechamentoDiario(fechamento);
		valorResumoConsignado.setSaldoAnterior(rmConsignado .getSaldoAnterior());
		valorResumoConsignado.setSaldoAtual(rmConsignado .getSaldoAtual());
		valorResumoConsignado.setValorEntradas(rmConsignado .getValorEntradas());
		valorResumoConsignado.setValorSaidas(rmConsignado .getValorSaidas());
		
		fechamentoDiarioResumoConsignadoRepository.adicionar(valorResumoConsignado);
	}

	private ResumoEstoqueDTO incluirResumoEstoque(FechamentoDiario fechamento) throws FechamentoDiarioException {
		
		ResumoEstoqueDTO resumoEstoque = obterResumoEstoque(fechamento.getDataFechamento());
		
		validarDadosFechamentoDiario(resumoEstoque, "Erro na obtenção dos dados de Resumo de Estoque!");
		
		ResumoEstoqueProduto estoqueProduto = resumoEstoque.getResumoEstoqueProduto();
		
		validarDadosFechamentoDiario(estoqueProduto, "Erro na obtenção dos dados referente aos Produtos do Resumo de Estoque!");
		
		ResumoEstoqueExemplar estoqueExemplar = resumoEstoque.getResumoEstoqueExemplar();
		
		validarDadosFechamentoDiario(estoqueExemplar, "Erro na obtenção dos dados referente aos Exemplares do Resumo de Estoque!");
		
		ValorResumoEstoque valorResumo = resumoEstoque.getValorResumoEstoque();
		
		validarDadosFechamentoDiario(valorResumo, "Erro na obtenção dos dados referente aos Valores do Resumo de Estoque!");
		
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
		
		validarDadosFechamentoDiario(resumoCotas,"Erro na obtenção dos dados de Resumo de Cotas!");
		
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
		
		incluirCotasFechamentoDiario(resumoCotas.getAusentesExpedicaoReparte(),TipoSituacaoCota.AUSENTE_REPARTE,consolidadoCota);
		
		incluirCotasFechamentoDiario(resumoCotas.getAusentesRecolhimentoEncalhe(),TipoSituacaoCota.AUSENTE_ENCALHE,consolidadoCota);
		
		incluirCotasFechamentoDiario(resumoCotas.getInativas(),TipoSituacaoCota.INATIVAS,consolidadoCota);
		
		incluirCotasFechamentoDiario(resumoCotas.getNovas(),TipoSituacaoCota.NOVAS,consolidadoCota);
		
		return resumoCotas;
		
	}

	private void incluirCotasFechamentoDiario(List<CotaResumoDTO> cotas, TipoSituacaoCota tipoDetalheCota,FechamentoDiarioConsolidadoCota fechamentoDiarioConsolidadoCota) {
		
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
		
		List<Divida> dividas = dividaService.obterDividasVencerApos(fechamento.getDataFechamento(), null);
		
		incluirDividas(fechamento, resumoDividas, dividas,TipoDividaFechamentoDia.A_VENCER);
		
		return resumoDividas;
	}

	private List<SumarizacaoDividasDTO> incluirResumoDividaReceber(FechamentoDiario fechamento) throws FechamentoDiarioException {
		
		List<SumarizacaoDividasDTO> resumoDividas = dividaService.sumarizacaoDividasReceberEm(fechamento.getDataFechamento());
		
		validarDadosFechamentoDiario(resumoDividas, "Erro na obtenção dos dados de Resumo de Dividas A Receber!");
		
		List<Divida> dividas = dividaService.obterDividasReceberEm(fechamento.getDataFechamento(), null);
		
		incluirDividas(fechamento, resumoDividas, dividas,TipoDividaFechamentoDia.A_RECEBER);
		
		return resumoDividas;
	}

	private void incluirDividas(FechamentoDiario fechamento,
			List<SumarizacaoDividasDTO> resumoDividas, List<Divida> dividas,TipoDividaFechamentoDia tipoDividaFechamentoDia) throws FechamentoDiarioException {
		
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
			
			for(Divida item : dividas ){
				
				FechamentoDiarioDivida dividaFechamentoDiario = new FechamentoDiarioDivida();
				
				DividaDTO dividaDTO = DividaDTO.fromDivida(item);
				
				dividaFechamentoDiario.setBanco(dividaDTO.getNomeBanco());
				dividaFechamentoDiario.setDataVencimento(dividaDTO.getDataVencimento());
				dividaFechamentoDiario.setTipoCobranca(dividaDTO.getFormaPagamento());
				dividaFechamentoDiario.setIdntificadorDivida(dividaDTO.getIdDivida());
				dividaFechamentoDiario.setNomeCota(dividaDTO.getNomeCota());
				dividaFechamentoDiario.setNossoNumero(dividaDTO.getNossoNumero());
				dividaFechamentoDiario.setNumeroConta(dividaDTO.getNumeroCota());
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
				lancamentoSuplementar.setQuantidadeContabilizada(item.getQuantidadeContabil().longValue());
				
				fechamentoDiarioLancamentoSuplementarRepository.adicionar(lancamentoSuplementar);
			}
		}
		return listaSuplementar;
	}

	private void incluirVendaSuplementar(FechamentoDiario fechamento, FechamentoDiarioConsolidadoSuplementar consolidadoSuplementar) {
		
		List<VendaFechamentoDiaDTO> vendasSuplementares = resumoSuplementarFecharDiaService.obterVendasSuplementar(fechamento.getDataFechamento());
		
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
		
		List<EncalheFecharDiaDTO> listaEncalhe = 
		        this.resumoEncalheFecharDiaService.obterDadosGridEncalhe(fechamento.getDataFechamento(), null);
		
		if(listaEncalhe!= null && !listaEncalhe.isEmpty()){
			
			for(EncalheFecharDiaDTO item : listaEncalhe){
				
				FechamentoDiarioLancamentoEncalhe lancamentoEncalhe = new FechamentoDiarioLancamentoEncalhe();
				
				lancamentoEncalhe.setProdutoEdicao(new ProdutoEdicao(item.getIdProdutoEdicao()));
				lancamentoEncalhe.setQuantidadeDiferenca(item.getQtdeDiferenca().intValue());
				lancamentoEncalhe.setQuantidadeVendaEncalhe(Util.nvl(item.getQtdeVendaEncalhe(),0).intValue());
				lancamentoEncalhe.setQuantidade(Util.nvl(item.getQtdeLogico(),0).intValue());
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
		
		validarDadosFechamentoDiario(resumoReparte,"Erro na obtenção dos dados de Resumo de Reparte!");
		
		FechamentoDiarioConsolidadoReparte consolidadoReparte = new FechamentoDiarioConsolidadoReparte();
		
		consolidadoReparte.setValorSobraDistribuida(resumoReparte.getTotalSobraDistribuicao());
		consolidadoReparte.setValorDiferenca(resumoReparte.getTotalDiferenca());
		consolidadoReparte.setValorDistribuido(resumoReparte.getTotalDistribuido());
	    consolidadoReparte.setValorFaltas(resumoReparte.getTotalFaltas());
	    consolidadoReparte.setValorReparte(resumoReparte.getTotalReparte());
	    consolidadoReparte.setValorSobras(resumoReparte.getTotalSobras());
	    consolidadoReparte.setValorTransferido(resumoReparte.getTotalTransferencias());
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
		    	movimentoReparte.setQuantidadeADistribuir(item.getQtdeDistribuir().intValue());
		    	movimentoReparte.setQuantidadeDiferenca(item.getQtdeDiferenca().intValue());
		    	movimentoReparte.setQuantidadeDistribuido(Util.nvl(item.getQtdeDistribuido(),0).intValue());
		    	movimentoReparte.setQuantidadeFaltaEM(Util.nvl(item.getQtdeFalta(),0).intValue());
		    	movimentoReparte.setQuantidadeReparte(Util.nvl(item.getQtdeReparte(),0).intValue());
		    	movimentoReparte.setQuantidadeSobraDistribuido(item.getQtdeSobraDistribuicao().intValue());
		    	movimentoReparte.setQuantidadeSobraEM(Util.nvl(item.getQtdeSobra(),0).intValue());
		    	movimentoReparte.setQuantidadeTranferencia(Util.nvl(item.getQtdeTransferencia(),0).intValue());
		    	movimentoReparte.setFechamentoDiarioConsolidadoReparte(consolidadoReparte);
		    	
		    	fechamentoLancamentoReparteRepository.adicionar(movimentoReparte);
		    }
	    }
	    
	    return listaReparte;
	}

	
	private void processarControleDeAprovacao() {

		List<GrupoMovimentoFinaceiro> gruposMovimentoFinanceiro = obterGruposMovimentoFinaceiro();

		List<Movimento> movimentosPendentes = this.fecharDiaRepository.obterMovimentosPorStatusData(
				null, gruposMovimentoFinanceiro, this.distribuidorRepository.obterDataOperacaoDistribuidor(), 
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
		return  this.fecharDiaRepository.obterResumoEstoque(dataOperacao);
	}
	
	private void validarDadosFechamentoDiario(Object objeto, String mensagem) throws FechamentoDiarioException{
		
		if(mensagem == null){
			mensagem =  "Erro na gravação do Resumo de Fechamento do Dia!";
		}
		
		if(objeto == null){
			throw new FechamentoDiarioException(mensagem);
		}
	}
	
	@Transactional
	@Override
	public FechamentoDiarioDTO processarFechamentoDoDia(Usuario usuario, Date dataFechamento){
		
		processarControleDeAprovacao();
				
		try {
		
			return salvarResumoFechamentoDiario(usuario, dataFechamento);
		
		} catch (FechamentoDiarioException e) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, e.getMessage());
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
        return diferencaRepository.obterDiferencas(data);
    }
  
}
