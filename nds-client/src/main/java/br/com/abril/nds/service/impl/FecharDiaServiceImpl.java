package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.EncalheFecharDiaDTO;
import br.com.abril.nds.dto.ReparteFecharDiaDTO;
import br.com.abril.nds.dto.ResumoEncalheFecharDiaDTO;
import br.com.abril.nds.dto.ResumoFechamentoDiarioConsignadoDTO;
import br.com.abril.nds.dto.ResumoFechamentoDiarioConsignadoDTO.ResumoAVista;
import br.com.abril.nds.dto.ResumoFechamentoDiarioConsignadoDTO.ResumoConsignado;
import br.com.abril.nds.dto.ResumoFechamentoDiarioCotasDTO;
import br.com.abril.nds.dto.ResumoReparteFecharDiaDTO;
import br.com.abril.nds.dto.ResumoSuplementarFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoControleDeAprovacaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoGeracaoCobrancaFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoLancamentoFaltaESobraFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoRecebimentoFisicoFecharDiaDTO;
import br.com.abril.nds.dto.VendaSuplementarDTO;
import br.com.abril.nds.dto.fechamentodiario.DividaDTO;
import br.com.abril.nds.dto.fechamentodiario.ResumoEstoqueDTO;
import br.com.abril.nds.dto.fechamentodiario.ResumoEstoqueDTO.ResumoEstoqueExemplar;
import br.com.abril.nds.dto.fechamentodiario.ResumoEstoqueDTO.ResumoEstoqueProduto;
import br.com.abril.nds.dto.fechamentodiario.ResumoEstoqueDTO.ValorResumoEstoque;
import br.com.abril.nds.dto.fechamentodiario.SumarizacaoDividasDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
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
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioDivida;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioLancamentoEncalhe;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioLancamentoReparte;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioMovimentoVendaSuplementar;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoAvista;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoConsignado;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoConsolidadoDivida;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoEstoque;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.movimentacao.Movimento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.FechamentoDiarioConsolidadoCotaRepository;
import br.com.abril.nds.repository.FechamentoDiarioConsolidadoDividaRepository;
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
import br.com.abril.nds.repository.MovimentoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.DividaService;
import br.com.abril.nds.service.FecharDiaService;
import br.com.abril.nds.service.ImpressaoDividaService;
import br.com.abril.nds.service.ResumoEncalheFecharDiaService;
import br.com.abril.nds.service.ResumoReparteFecharDiaService;
import br.com.abril.nds.service.ResumoSuplementarFecharDiaService;
import br.com.abril.nds.service.exception.FechamentoDiarioException;
import br.com.abril.nds.util.DateUtil;
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
	public List<ValidacaoControleDeAprovacaoFecharDiaDTO> obterPendenciasDeAprovacao(Date dataOperacao, StatusAprovacao pendente) {
		 
		return this.fecharDiaRepository.obterPendenciasDeAprovacao(dataOperacao,pendente);
	}

	@Override
	@Transactional
	public Boolean existeGeracaoDeCobranca(Date dataOperacao) {
		
		Calendar dataBase = Calendar.getInstance();
		dataBase.setTime(dataOperacao);
		int diaDaSemanaDaDataDeOperacao = dataBase.get(Calendar.DAY_OF_WEEK);
		int diaDaMesDaDataDeOperacao = dataBase.get(Calendar.DAY_OF_MONTH);
		
		List<ValidacaoGeracaoCobrancaFecharDiaDTO> listaDePoliticaCobranca = this.fecharDiaRepository.obterFormasDeCobranca();
		
		for(ValidacaoGeracaoCobrancaFecharDiaDTO dto: listaDePoliticaCobranca){
			FormaCobranca fc = this.formaCobrancaRepository.buscarPorId(dto.getFormaCobrancaId());
			if(dto.getTipoFormaCobranca().equals("Diária")){	
				return impressaoDividaService.validarDividaGerada(dataOperacao);				
			}
			if(dto.getTipoFormaCobranca().equals("Semanal")){
				List<ValidacaoGeracaoCobrancaFecharDiaDTO> lista = this.fecharDiaRepository.obterDiasDaConcentracao(fc);
				for(ValidacaoGeracaoCobrancaFecharDiaDTO con: lista){					
					if(con.getDiaDoMes() == diaDaSemanaDaDataDeOperacao){
						return impressaoDividaService.validarDividaGerada(dataOperacao);
					}					
				}
				
			}
			if(fc.getTipoFormaCobranca().getDescricao().equals("Mensal") || fc.getTipoFormaCobranca().getDescricao().equals("Quinzenal") ){
				for(Integer diaDeCobranca: fc.getDiasDoMes()){
					if(diaDeCobranca ==  diaDaMesDaDataDeOperacao){
						return impressaoDividaService.validarDividaGerada(dataOperacao);
					}
				}
			}
		}
		 
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public ResumoFechamentoDiarioCotasDTO obterResumoCotas(Date dataFechamento) {

		Long quantidadeTotal = this.cotaRepository.obterQuantidadeCotas(null);
		
		Long quantidadeAtivas = this.cotaRepository.obterQuantidadeCotas(SituacaoCadastro.ATIVO);
		
		List<Cota> ausentesExpedicaoReparte = 
			this.cotaRepository.obterCotasAusentesNaExpedicaoDoReparteEm(dataFechamento);
		
		List<Cota> ausentesRecolhimentoEncalhe = 
			this.cotaRepository.obterCotasAusentesNoRecolhimentoDeEncalheEm(dataFechamento);
		
		List<Cota> novas = this.cotaRepository.obterCotasComInicioAtividadeEm(dataFechamento);
		
		List<Cota> inativas = this.cotaRepository.obterCotas(SituacaoCadastro.INATIVO);
		
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
		
		resumoConsignado.setSaldoAnterior(BigDecimal.TEN);
		resumoConsignado.setSaldoAtual(BigDecimal.TEN);
		resumoConsignado.setValorEntradas(BigDecimal.TEN);
		resumoConsignado.setValorSaidas(BigDecimal.TEN);
		
		resumoFechamentoDiarioConsignado.setResumoConsignado(resumoConsignado);
		
		ResumoFechamentoDiarioConsignadoDTO.ResumoAVista resumoAVista = 
			resumoFechamentoDiarioConsignado.new ResumoAVista();
		
		resumoAVista.setSaldoAnterior(BigDecimal.TEN);
		resumoAVista.setSaldoAtual(BigDecimal.TEN);
		resumoAVista.setValorEntradas(BigDecimal.TEN);
		resumoAVista.setValorSaidas(BigDecimal.TEN);
		
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
        return obterDividasReceberEm(data, paginacao);
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
    public int contarDividasReceberEm(Date data) {
        return dividaService.contarDividasReceberEm(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public int contarDividasVencerApos(Date data) {
        return dividaService.contarDividasVencerApos(data);
    }
    
    @Override
    @Transactional
    public void salvarResumoFechamentoDiario(Usuario usuario, Date dataFechamento) throws FechamentoDiarioException{
    	
    	FechamentoDiario fechamento = new FechamentoDiario();
    	
    	fechamento.setDataFechamento(dataFechamento);
    	fechamento.setUsuario(usuario);
    	
    	fechamento = fechamentoDiarioRepository.merge(fechamento);
    	
    	incluirResumoReparte(fechamento); 
    	
    	incluirResumoEncalhe(fechamento); 
    	
    	incluirResumoSuplementar(fechamento); 
    	
    	incluirResumoDividaReceber(fechamento);  
    	
    	incluirResumoDividaVencer(fechamento); 
    	
    	incluirResumoCotas(fechamento); 
    	
    	incluirResumoEstoque(fechamento);
    	
    	incluirResumoConsignado(fechamento); 
    }

	private void incluirResumoConsignado(FechamentoDiario fechamento) throws FechamentoDiarioException {
		
		ResumoFechamentoDiarioConsignadoDTO resumoConsignado = obterResumoConsignado(fechamento.getDataFechamento());
		
		if(resumoConsignado == null){
			throw new FechamentoDiarioException("Erro na obtenção dos dados de Resumo Consignado!");
		}
		
		incluirResumoValorConsignado(fechamento, resumoConsignado);
		
		incluirResumoValorAvista(fechamento, resumoConsignado);
	}

	private void incluirResumoValorAvista(FechamentoDiario fechamento,ResumoFechamentoDiarioConsignadoDTO resumoConsignado)
										 throws FechamentoDiarioException {
		
		ResumoAVista resumoAvista = resumoConsignado.getResumoAVista();
		
		if(resumoAvista == null){
			throw new FechamentoDiarioException("Erro na obtenção dos dados de Resumo Consignado!");
		}
		
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
		
		if(rmConsignado == null){
			throw new FechamentoDiarioException("Erro na obtenção dos dados de Resumo Consignado!");
		}
		
		FechamentoDiarioResumoConsignado valorResumoConsignado = new FechamentoDiarioResumoConsignado();
		
		valorResumoConsignado.setFechamentoDiario(fechamento);
		valorResumoConsignado.setSaldoAnterior(rmConsignado .getSaldoAnterior());
		valorResumoConsignado.setSaldoAtual(rmConsignado .getSaldoAtual());
		valorResumoConsignado.setValorEntradas(rmConsignado .getValorEntradas());
		valorResumoConsignado.setValorSaidas(rmConsignado .getValorSaidas());
		
		fechamentoDiarioResumoConsignadoRepository.adicionar(valorResumoConsignado);
	}

	private void incluirResumoEstoque(FechamentoDiario fechamento) throws FechamentoDiarioException {
		
		ResumoEstoqueDTO resumoEstoque = obterResumoEstoque(fechamento.getDataFechamento());
		
		if(resumoEstoque == null){
			throw new FechamentoDiarioException("Erro na obtenção dos dados de Resumo de Estoque!");
		}
		
		ResumoEstoqueProduto estoqueProduto = resumoEstoque.getResumoEstoqueProduto();
		
		ResumoEstoqueExemplar estoqueExemplar = resumoEstoque.getResumoEstoqueExemplar();
		
		ValorResumoEstoque valorResumo = resumoEstoque.getValorResumoEstoque();
		
		incluirResumoEstoque(TipoEstoque.LANCAMENTO, estoqueExemplar.getQuantidadeLancamento(), 
				estoqueProduto.getQuantidadeLancamento() ,valorResumo.getValorLancamento());
		
		incluirResumoEstoque(TipoEstoque.JURAMENTADO, estoqueExemplar.getQuantidadeJuramentado(), 
				estoqueProduto.getQuantidadeJuramentado() ,valorResumo.getValorJuramentado());
		
		incluirResumoEstoque(TipoEstoque.SUPLEMENTAR, estoqueExemplar.getQuantidadeSuplementar(), 
				estoqueProduto.getQuantidadeSuplementar() ,valorResumo.getValorSuplementar());
		
		incluirResumoEstoque(TipoEstoque.RECOLHIMENTO, estoqueExemplar.getQuantidadeRecolhimento(), 
				estoqueProduto.getQuantidadeRecolhimento() ,valorResumo.getValorRecolhimento());
		
		incluirResumoEstoque(TipoEstoque.DANIFICADO, estoqueExemplar.getQuantidadeDanificados(), 
				estoqueProduto.getQuantidadeDanificados() ,valorResumo.getValorDanificados());
	}

	private void incluirResumoEstoque(TipoEstoque tipoEstoque, Integer qntExemplares, Integer qntProdutos, BigDecimal valorTotal ) {
		
		FechamentoDiarioResumoEstoque resumoLancamento = new FechamentoDiarioResumoEstoque();
		
		resumoLancamento.setQuantidadeExemplares(qntExemplares);
		resumoLancamento.setQuantidadeProduto(qntProdutos);
		resumoLancamento.setValorTotal(valorTotal);
		resumoLancamento.setTipoEstoque(tipoEstoque);
		
		fechamentoDiarioResumoEstoqueRepository.adicionar(resumoLancamento);
	}

	private void incluirResumoCotas(FechamentoDiario fechamento) throws FechamentoDiarioException {
		
		ResumoFechamentoDiarioCotasDTO resumoCotas = obterResumoCotas(fechamento.getDataFechamento());
		
		if(resumoCotas == null){
			throw new FechamentoDiarioException("Erro na obtenção dos dados de Resumo de Cotas!");
		}
		
		FechamentoDiarioConsolidadoCota consolidadoCota = new FechamentoDiarioConsolidadoCota();
		
		consolidadoCota.setFechamentoDiario(fechamento);
		consolidadoCota.setQuantidadeAtivos(resumoCotas.getQuantidadeAtivas().intValue());
		consolidadoCota.setQuantidadeAusenteEncalhe(resumoCotas.getQuantidadeAusentesRecolhimentoEncalhe().intValue());
		consolidadoCota.setQuantidadeAusenteReparte(resumoCotas.getQuantidadeAusentesExpedicaoReparte().intValue());
		consolidadoCota.setQuantidadeNovos(resumoCotas.getQuantidadeNovas().intValue());
		consolidadoCota.setQuantidadeInativas(resumoCotas.getQuantidadeInativas().intValue());
		consolidadoCota.setQuantidadeTotal(resumoCotas.getQuantidadeTotal().intValue());
		
		consolidadoCota = fechamentoDiarioConsolidadoCotaRepository.merge(consolidadoCota);
		
		incluirCotasFechamentoDiario(resumoCotas.getAusentesExpedicaoReparte(),TipoSituacaoCota.AUSENTE_REPARTE,consolidadoCota);
		
		incluirCotasFechamentoDiario(resumoCotas.getAusentesRecolhimentoEncalhe(),TipoSituacaoCota.AUSENTE_ENCALHE,consolidadoCota);
		
		incluirCotasFechamentoDiario(resumoCotas.getInativas(),TipoSituacaoCota.INATIVAS,consolidadoCota);
		
		incluirCotasFechamentoDiario(resumoCotas.getNovas(),TipoSituacaoCota.NOVAS,consolidadoCota);
		
	}

	private void incluirCotasFechamentoDiario(List<Cota> cotas, TipoSituacaoCota tipoDetalheCota,FechamentoDiarioConsolidadoCota fechamentoDiarioConsolidadoCota) {
		
		if(cotas!= null && !cotas.isEmpty()){
			
			for(Cota item : cotas ){
				
				FechamentoDiarioCota cotaFechamentoDiario = new FechamentoDiarioCota();
				
				cotaFechamentoDiario.setNomeCota(item.getPessoa().getNome());
				cotaFechamentoDiario.setNumeroCota(item.getNumeroCota());
				cotaFechamentoDiario.setTipoSituacaoCota(tipoDetalheCota);
				cotaFechamentoDiario.setFechamentoDiarioConsolidadoCota(fechamentoDiarioConsolidadoCota);
				
				fechamentoDiarioCotaRepository.adicionar(cotaFechamentoDiario);
			}
		}
	}

	private void incluirResumoDividaVencer(FechamentoDiario fechamento) throws FechamentoDiarioException {
		
		List<SumarizacaoDividasDTO> resumoDividas = dividaService.sumarizacaoDividasVencerApos(fechamento.getDataFechamento());
		
		if(resumoDividas == null){
			throw new FechamentoDiarioException("Erro na obtenção dos dados de Resumo de Dividas A Vencer!");
		}
		
		List<Divida> dividas = dividaService.obterDividasVencerApos(fechamento.getDataFechamento(), null);
		
		if(dividas == null){
			throw new FechamentoDiarioException("Erro na obtenção dos dados de Detalhemento do Resumo de Dividas A Vencer!");
		}
		
		incluirDividas(fechamento, resumoDividas, dividas,TipoDividaFechamentoDia.A_VENCER);
	}

	private void incluirResumoDividaReceber(FechamentoDiario fechamento) throws FechamentoDiarioException {
		
		List<SumarizacaoDividasDTO> resumoDividas = dividaService.sumarizacaoDividasReceberEm(fechamento.getDataFechamento());
		
		if(resumoDividas == null){
			throw new FechamentoDiarioException("Erro na obtenção dos dados de Resumo de Dividas A Receber!");
		}
		
		List<Divida> dividas = dividaService.obterDividasReceberEm(fechamento.getDataFechamento(), null);
		
		if(dividas == null){
			throw new FechamentoDiarioException("Erro na obtenção dos dados de Detalhemento do Resumo de Dividas A Receber!");
		}
		
		incluirDividas(fechamento, resumoDividas, dividas,TipoDividaFechamentoDia.A_RECEBER);
	}

	private void incluirDividas(FechamentoDiario fechamento,
			List<SumarizacaoDividasDTO> resumoDividas, List<Divida> dividas,TipoDividaFechamentoDia tipoDividaFechamentoDia) {
		
		FechamentoDiarioConsolidadoDivida resumoConsolidadoDivida = new FechamentoDiarioConsolidadoDivida();
		resumoConsolidadoDivida.setFechamentoDiario(fechamento);
		resumoConsolidadoDivida.setTipoDivida(tipoDividaFechamentoDia);
		
		resumoConsolidadoDivida = fechamentoDiarioConsolidadoDividaRepository.merge(resumoConsolidadoDivida);
		
		for(SumarizacaoDividasDTO item : resumoDividas ){
			
			FechamentoDiarioResumoConsolidadoDivida resumo = new FechamentoDiarioResumoConsolidadoDivida();
			
			resumo.setTipoCobranca(item.getTipoCobranca());
			resumo.setValorInadimplencia(item.getInadimplencia());
			resumo.setValorPago(item.getValorPago());
			resumo.setValorTotal(item.getTotal());
			resumo.setFechamentoDiarioConsolidadoDivida(resumoConsolidadoDivida);
			
			fechamentoDiarioResumoConsolidadoDividaRepository.adicionar(resumo);
		}
		
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

	private void incluirResumoSuplementar(FechamentoDiario fechamento) throws FechamentoDiarioException {
		
		ResumoSuplementarFecharDiaDTO resumoSuplementar = resumoSuplementarFecharDiaService.obterResumoGeralEncalhe(fechamento.getDataFechamento());
		
		if(resumoSuplementar == null){
			throw new FechamentoDiarioException("Erro na obtenção dos dados de Resumo de Suplementar!");
		}
		
		FechamentoDiarioConsolidadoSuplementar consolidadoSuplementar = new FechamentoDiarioConsolidadoSuplementar();
		
		consolidadoSuplementar.setValorEstoqueLogico(resumoSuplementar.getTotalEstoqueLogico());
		consolidadoSuplementar.setValorSaldo(resumoSuplementar.getSaldo());
		consolidadoSuplementar.setValorTransferencia(resumoSuplementar.getTotalTransferencia());
		consolidadoSuplementar.setValorVendas(resumoSuplementar.getTotalVenda());
		consolidadoSuplementar.setFechamentoDiario(fechamento);
		
		consolidadoSuplementar = fechamentoDiarioConsolidadoSuplementarRepository.merge(consolidadoSuplementar);
		
		//TODO Aguardando definição da estrutura para implementação do metodo abaixo.
		
		/*
		FechamentoDiarioLancamentoSuplementar lancamentoSuplementar = new FechamentoDiarioLancamentoSuplementar();
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(item.getCodigo(), item.getNumeroEdicao());
		
		lancamentoSuplementar.setProdutoEdicao(produtoEdicao);
		lancamentoSuplementar.setQuantidadeContabilizada(quantidadeContabilizada);
		lancamentoSuplementar.setQuantidadeDiferenca(quantidadeDiferenca);
		lancamentoSuplementar.setQuantidadeFisico(quantidadeFisico);
		lancamentoSuplementar.setFechamentoDiarioConsolidadoSuplementar(consolidadoSuplementar);
		
		fechamentoDiarioLancamentoSuplementarRepository.adicionar(lancamentoSuplementar)*/
		
		List<VendaSuplementarDTO> vendasSuplementares = resumoSuplementarFecharDiaService.obterVendasSuplementar(fechamento.getDataFechamento());
		
		if(vendasSuplementares != null && !vendasSuplementares.isEmpty()){
			
			for(VendaSuplementarDTO item : vendasSuplementares){
				
				FechamentoDiarioMovimentoVendaSuplementar vendaSuplementar = new FechamentoDiarioMovimentoVendaSuplementar();
				
				ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(item.getCodigo(), item.getNumeroEdicao());
				
				vendaSuplementar.setProdutoEdicao(produtoEdicao);
				vendaSuplementar.setQuantidade(item.getQtde());
				vendaSuplementar.setValor(item.getValor());
				vendaSuplementar.setDataRecebimento(DateUtil.parseDataPTBR(item.getDataRecolhimento()));
				
				fechamentoDiarioMovimentoVendaSuplementarRepository.adicionar(vendaSuplementar);
			}
		}
	}

	private void incluirResumoEncalhe(FechamentoDiario fechamento) throws FechamentoDiarioException {
		
		ResumoEncalheFecharDiaDTO resumoEncalhe = this.resumoEncalheFecharDiaService.obterResumoGeralEncalhe(fechamento.getDataFechamento());
		
		if(resumoEncalhe == null){
			throw new FechamentoDiarioException("Erro na obtenção dos dados de Resumo de Encalhe!");
		}
		
		FechamentoDiarioConsolidadoEncalhe consolidadoEncalhe = new FechamentoDiarioConsolidadoEncalhe();
		
		consolidadoEncalhe.setSaldo(resumoEncalhe.getSaldo());
		consolidadoEncalhe.setValorFaltaEM(resumoEncalhe.getTotalFaltas());
		consolidadoEncalhe.setValorSobraEM(resumoEncalhe.getTotalSobras());
		consolidadoEncalhe.setValorFisico(resumoEncalhe.getTotalFisico());
		consolidadoEncalhe.setValorJuramentado(resumoEncalhe.getTotalJuramentado());
		consolidadoEncalhe.setValorLogico(resumoEncalhe.getTotalLogico());
		consolidadoEncalhe.setValorVenda(resumoEncalhe.getVenda());
		consolidadoEncalhe.setFechamentoDiario(fechamento);
		
		List<EncalheFecharDiaDTO> listaEncalhe = this.resumoEncalheFecharDiaService.obterDadosGridEncalhe(fechamento.getDataFechamento());
		
		if(listaEncalhe == null){
			throw new FechamentoDiarioException("Erro na obtenção dos dados de detalhamento do Resumo de Encalhe!");
		}
		
		for(EncalheFecharDiaDTO item : listaEncalhe){
			
			FechamentoDiarioLancamentoEncalhe lancamentoEncalhe = new FechamentoDiarioLancamentoEncalhe();
			
			ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(item.getCodigo(), item.getNumeroEdicao());
			
			lancamentoEncalhe.setProdutoEdicao(produtoEdicao);
			lancamentoEncalhe.setQuantidadeDiferenca(item.getDiferenca());
			lancamentoEncalhe.setQuantidadeVendaEncalhe(item.getQtde().intValue());
			lancamentoEncalhe.setQuantidade(item.getQtde().intValue());
			
			fechamentoDiarioLancamentoEncalheRepository.adicionar(lancamentoEncalhe);
		}
		
		///TODO Aguardando definição da estrutura para implementação do metodo abaixo.
		
		/*for(){
			
			FechamentoDiarioMovimentoVendaEncalhe movimentoVendaEncalhe = new FechamentoDiarioMovimentoVendaEncalhe();
			
			ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(item.getCodigo(), item.getNumeroEdicao());
			
			movimentoVendaEncalhe.setProdutoEdicao(produtoEdicao);
			movimentoVendaEncalhe.setQuantidade(null);
			movimentoVendaEncalhe.setValor(null);
			movimentoVendaEncalhe.setDataRecebimento(null);
			
			fechamentoDiarioMovimentoVendaEncalheRepository.adicionar(movimentoVendaEncalhe);
		}*/
	}
	
	private void incluirResumoReparte(FechamentoDiario fechamento) throws FechamentoDiarioException {
		
		ResumoReparteFecharDiaDTO resumoReparte = resumoReparteFecharDiaService.obterResumoGeralReparte(fechamento.getDataFechamento());
		
		if(resumoReparte == null){
			throw new FechamentoDiarioException("Erro na obtenção dos dados de Resumo de Reparte!");
		}
		
		FechamentoDiarioConsolidadoReparte consolidadoReparte = new FechamentoDiarioConsolidadoReparte();
		
		consolidadoReparte.setValorSobraDistribuida(resumoReparte.getTotalADistribuir());
		consolidadoReparte.setValorDiferenca(resumoReparte.getDiferenca());
		consolidadoReparte.setValorDistribuido(resumoReparte.getTotalDistribuido());
	    consolidadoReparte.setValorFaltas(resumoReparte.getTotalFaltas());
	    consolidadoReparte.setValorReparte(resumoReparte.getTotalReparte());
	    consolidadoReparte.setValorSobras(resumoReparte.getTotalSobras());
	    consolidadoReparte.setValorTransferido(resumoReparte.getTotalTranferencia());
	    consolidadoReparte.setFechamentoDiario(fechamento);
	    
	    consolidadoReparte = fechamentoConsolidadoReparteRepository.merge(consolidadoReparte);
	    
	    List<ReparteFecharDiaDTO> listaReparte = resumoReparteFecharDiaService.obterResumoReparte(fechamento.getDataFechamento());
		
	    if(listaReparte == null){
			throw new FechamentoDiarioException("Erro na obtenção dos dados de detalhe do Resumo de Reparte!");
		}
	    
	    for(ReparteFecharDiaDTO item : listaReparte){
	    	
	    	FechamentoDiarioLancamentoReparte movimentoReparte = new FechamentoDiarioLancamentoReparte();
	    	
	    	ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(item.getCodigo(), item.getNumeroEdicao());
	    	
	    	movimentoReparte.setProdutoEdicao(produtoEdicao);
	    	movimentoReparte.setQuantidadeADistribuir(item.getQtdADistribuir());
	    	movimentoReparte.setQuantidadeDiferenca(item.getQtdDiferenca());
	    	movimentoReparte.setQuantidadeDistribuido(item.getQtdDistribuido().intValue());
	    	movimentoReparte.setQuantidadeFaltaEM(item.getQtdFaltas().intValue());
	    	movimentoReparte.setQuantidadeReparte(item.getQtdReparte().intValue());
	    	movimentoReparte.setQuantidadeSobraDistribuido(item.getQtdSobraDiferenca());
	    	movimentoReparte.setQuantidadeSobraEM(item.getQtdSobras().intValue());
	    	movimentoReparte.setQuantidadeTranferencia(item.getQtdTransferido().intValue());
	    	movimentoReparte.setFechamentoDiarioConsolidadoReparte(consolidadoReparte);
	    	
	    	fechamentoLancamentoReparteRepository.adicionar(movimentoReparte);
	    }
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void processarControleDeAprovacao() {

		Distribuidor distribuidor = this.distribuidorRepository.obter();

		List<GrupoMovimentoFinaceiro> gruposMovimentoFinanceiro = obterGruposMovimentoFinaceiro();

		List<Movimento> movimentosPendentes = this.fecharDiaRepository.obterMovimentosPorStatusData(
				null, gruposMovimentoFinanceiro, distribuidor.getDataOperacao(), StatusAprovacao.PENDENTE);

		for (Movimento movimento : movimentosPendentes) {

			movimento.setData(DateUtil.adicionarDias(movimento.getData(), 1));

			this.movimentoRepository.merge(movimento);
		}
	}

	private List<GrupoMovimentoFinaceiro> obterGruposMovimentoFinaceiro() {
		
		List<GrupoMovimentoFinaceiro> grupos = new ArrayList<GrupoMovimentoFinaceiro>();
		
		grupos.add(GrupoMovimentoFinaceiro.CREDITO);
		grupos.add(GrupoMovimentoFinaceiro.DEBITO);
		grupos.add(GrupoMovimentoFinaceiro.POSTERGADO);
		grupos.add(GrupoMovimentoFinaceiro.POSTERGADO_NEGOCIACAO);
		
		return grupos;
	}
	
	@Override
	@Transactional(readOnly=true)
	public ResumoEstoqueDTO obterResumoEstoque(Date dataOperacao){		
		return  this.fecharDiaRepository.obterResumoEstoque(dataOperacao);
	}
}
