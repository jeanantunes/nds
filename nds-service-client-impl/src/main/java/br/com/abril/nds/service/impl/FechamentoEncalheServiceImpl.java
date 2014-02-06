package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.AnaliticoEncalheDTO;
import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDtoOrdenaPorCodigo;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDtoOrdenaPorEdicao;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDtoOrdenaPorPrecoCapa;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDtoOrdenaPorPrecoDesconto;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDtoOrdenaPorProduto;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDtoOrdenaPorSequencia;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDtoOrdenaPorTotalDevolucao;
import br.com.abril.nds.dto.MovimentoEstoqueCotaGenericoDTO;
import br.com.abril.nds.dto.fechamentoencalhe.GridFechamentoEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ObrigacaoFiscal;
import br.com.abril.nds.model.cadastro.ParametrosRecolhimentoDistribuidor;
import br.com.abril.nds.model.cadastro.Processo;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.estoque.ControleFechamentoEncalhe;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.FechamentoEncalhe;
import br.com.abril.nds.model.estoque.FechamentoEncalheBox;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalheBoxPK;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalhePK;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.ItemNotaFiscalSaida;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscalReferenciada;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.repository.ConferenciaEncalheRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.CotaUnificacaoRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EstudoRepository;
import br.com.abril.nds.repository.FechamentoEncalheBoxRepository;
import br.com.abril.nds.repository.FechamentoEncalheRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoServicoRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.TipoNotaFiscalRepository;
import br.com.abril.nds.service.BoletoEmailService;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.EstudoCotaService;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.service.FechamentoEncalheService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.NegociacaoDividaService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.ParciaisService;
import br.com.abril.nds.service.exception.AutenticacaoEmailException;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.SemanaUtil;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class FechamentoEncalheServiceImpl implements FechamentoEncalheService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FechamentoEncalheServiceImpl.class);
		
	@Autowired
	private FechamentoEncalheRepository fechamentoEncalheRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private GerarCobrancaService gerarCobrancaService;

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;

	@Autowired
	private ChamadaEncalheRepository chamadaEncalheRepository;
	
	@Autowired
	private ChamadaEncalheCotaRepository chamadaEncalheCotaRepository;
	
	@Autowired
	private FechamentoEncalheBoxRepository fechamentoEncalheBoxRepository;
	
	@Autowired
	private TipoNotaFiscalRepository tipoNotaFiscalRepository;
	
	@Autowired
	private NotaFiscalService notaFiscalService;
	
	@Autowired
	private NotaFiscalRepository notaFiscalRepository;
	
	@Autowired
	private ProdutoServicoRepository produtoServicoRepository;
	
	@Autowired
	private MovimentoEstoqueService movimentoEstoqueService;
	
	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private DiferencaEstoqueService diferencaEstoqueService;
	
	@Autowired 
	private ProdutoEdicaoRepository edicaoRepository;
	
	@Autowired
	private ConferenciaEncalheRepository conferenciaEncalheRepository;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private EstudoService estudoService;
	
	@Autowired
	private EstudoRepository estudoRepository;
	
	@Autowired
	private EstudoCotaService estudoCotaService;
	
	@Autowired
	private ParciaisService parciaisService;
	
	@Autowired
	protected BoletoEmailService boletoEmailService;
	
	@Autowired
	private NegociacaoDividaService negociacaoDividaService;
	
	@Autowired
	private CotaUnificacaoRepository cotaUnificacaoRepository;
	
	@Override
	@Transactional
	public List<FechamentoFisicoLogicoDTO> buscarFechamentoEncalhe(FiltroFechamentoEncalheDTO filtro,
			String sortorder, String sortname, Integer page, Integer rp) {
		
		Integer startSearch = null;
		if ( page != null || rp != null ){
			startSearch = page * rp - rp;
		}
		String sort = sortname;
		if ("total".equals(sortname)) {
			sort = null;
		}
		
		Boolean fechado = fechamentoEncalheRepository.buscaControleFechamentoEncalhe(filtro.getDataEncalhe());
				
		Date dataAtual = DateUtil.removerTimestamp(new Date());
		
		List<FechamentoFisicoLogicoDTO> listaConferencia = 
				fechamentoEncalheRepository.buscarConferenciaEncalheNovo(filtro, sortorder, sort, startSearch, rp);
		
		if(! listaConferencia.isEmpty()) {
			ArrayList<Long> listaDeIdsProdutoEdicao = new ArrayList<Long>();
			for(FechamentoFisicoLogicoDTO conferencia : listaConferencia) {
				conferencia.setDataRecolhimento(filtro.getDataEncalhe());
				listaDeIdsProdutoEdicao.add(conferencia.getProdutoEdicao());
			}
			
			List<FechamentoFisicoLogicoDTO> listaMovimentoEstoqueCota = fechamentoEncalheRepository.buscarMovimentoEstoqueCota(filtro, listaDeIdsProdutoEdicao);
			List<FechamentoFisicoLogicoDTO> listaMovimentoEstoqueCotaVendaProduto = null;
			
			//So ira apurar os exemplares de venda de encalhe se não informar box para consulta
			if(filtro.getBoxId() == null) {
				listaMovimentoEstoqueCotaVendaProduto = fechamentoEncalheRepository.buscarMovimentoEstoqueCotaVendaProduto(filtro, listaDeIdsProdutoEdicao);
			}
		
			for(FechamentoFisicoLogicoDTO conferencia : listaConferencia) {

				//Soma as quantidades para os exemplares de devolucao
				for(FechamentoFisicoLogicoDTO movimentoEstoqueCota : listaMovimentoEstoqueCota){
					
					if(conferencia.getProdutoEdicao().equals(movimentoEstoqueCota.getProdutoEdicao()) 
							&& movimentoEstoqueCota.getExemplaresDevolucao() != null 
							&& movimentoEstoqueCota.getDataRecolhimento().equals(conferencia.getDataRecolhimento())){
						
						if(conferencia.getExemplaresDevolucao() == null){
							
							conferencia.setExemplaresDevolucao(BigInteger.valueOf(0));
						}
						
						conferencia.setExemplaresDevolucao(conferencia.getExemplaresDevolucao().add(movimentoEstoqueCota.getExemplaresDevolucao()));
					}
				}
				
				if(filtro.getBoxId() == null){
					
					//Subtrai as quantidades para os exemplares de devolucao
					for(FechamentoFisicoLogicoDTO movimentoEstoqueCotaVendaProduto : listaMovimentoEstoqueCotaVendaProduto) {
						if(conferencia.getProdutoEdicao().equals(movimentoEstoqueCotaVendaProduto.getProdutoEdicao()) 
								&& movimentoEstoqueCotaVendaProduto.getExemplaresDevolucao() != null){
							
							BigInteger exemplaresDevolucaoConferencia =
								(conferencia.getExemplaresDevolucao() == null) ? BigInteger.ZERO : conferencia.getExemplaresDevolucao();
							
							conferencia.setExemplaresDevolucao(exemplaresDevolucaoConferencia.subtract(movimentoEstoqueCotaVendaProduto.getExemplaresDevolucao()));
						}
					}	
				}
			}
						
			int inicioDiaSemana = distribuidorRepository.buscarInicioSemana().getCodigoDiaSemana();

			Date dataInicioSemana = SemanaUtil.obterDataInicioSemana(inicioDiaSemana, dataAtual);
			
			Date dataFimSemana = DateUtil.adicionarDias(dataInicioSemana, 6);
			
			if (filtro.getBoxId() == null ){ 
				List<FechamentoEncalhe> listaFechamento = fechamentoEncalheRepository.buscarFechamentoEncalhe(filtro.getDataEncalhe());
				for (FechamentoFisicoLogicoDTO conferencia : listaConferencia) {
					
					this.setarInfoComumFechamentoFisicoLogicoDTO(conferencia, fechado, dataAtual, dataFimSemana);
					
					for (FechamentoEncalhe fechamento : listaFechamento) {
						if (conferencia.getProdutoEdicao().equals(fechamento.getFechamentoEncalhePK().getProdutoEdicao().getId())) {
							conferencia.setFisico(fechamento.getQuantidade());
							conferencia.setDiferenca(calcularDiferenca(conferencia));
							break;
						}
					}
				}
				
			} else {
				List<FechamentoEncalheBox> listaFechamentoBox = fechamentoEncalheBoxRepository.buscarFechamentoEncalheBox(filtro);
				for (FechamentoFisicoLogicoDTO conferencia : listaConferencia) {
					
					this.setarInfoComumFechamentoFisicoLogicoDTO(conferencia, fechado, dataAtual, dataFimSemana);
					
					for (FechamentoEncalheBox fechamento : listaFechamentoBox) {
						
						if (conferencia.getProdutoEdicao().equals(fechamento.getFechamentoEncalheBoxPK().getFechamentoEncalhe().getFechamentoEncalhePK().getProdutoEdicao().getId())) {
													
							conferencia.setFisico(fechamento.getQuantidade());
													
							conferencia.setDiferenca(calcularDiferenca(conferencia));
							
							break;
						}
						
					}
				}
			}
			
			if (sort == null) {
				listaConferencia = this.retornarListaOrdenada(listaConferencia, "sequencia", sortorder);	
			} 
			else {
				listaConferencia = this.retornarListaOrdenada(listaConferencia, sort, sortorder);
			}
		}
		
		return listaConferencia;
		
	}

	private List<FechamentoFisicoLogicoDTO> retornarListaOrdenada(
			List<FechamentoFisicoLogicoDTO> listaConferencia, String sort, String sortorder) {
		
		if (sortorder == null) {
			return listaConferencia;
		}
		
		if(sort.equals("sequencia"))
		{
			FechamentoFisicoLogicoDtoOrdenaPorSequencia ordenacao = new FechamentoFisicoLogicoDtoOrdenaPorSequencia(sortorder);
			Collections.sort(listaConferencia, ordenacao);
			return listaConferencia;
		}
		if(sort.equals("codigo"))
		{
			FechamentoFisicoLogicoDtoOrdenaPorCodigo ordenacao = new FechamentoFisicoLogicoDtoOrdenaPorCodigo(sortorder);
			Collections.sort(listaConferencia, ordenacao);
			return listaConferencia;
		}
		else if(sort.equals("produto"))
		{
			FechamentoFisicoLogicoDtoOrdenaPorProduto ordenacao = new FechamentoFisicoLogicoDtoOrdenaPorProduto(sortorder);
			Collections.sort(listaConferencia, ordenacao);
			return listaConferencia;
		}
		else if(sort.equals("edicao"))
		{
			FechamentoFisicoLogicoDtoOrdenaPorEdicao ordenacao = new FechamentoFisicoLogicoDtoOrdenaPorEdicao(sortorder);
			Collections.sort(listaConferencia, ordenacao);
			return listaConferencia;
		}
		else if(sort.equals("precoCapa"))
		{
			FechamentoFisicoLogicoDtoOrdenaPorPrecoCapa ordenacao = new FechamentoFisicoLogicoDtoOrdenaPorPrecoCapa(sortorder);
			Collections.sort(listaConferencia, ordenacao);
			return listaConferencia;
		}
		else if(sort.equals("precoCapaDesc"))
		{
			FechamentoFisicoLogicoDtoOrdenaPorPrecoDesconto ordenacao = new FechamentoFisicoLogicoDtoOrdenaPorPrecoDesconto(sortorder);
			Collections.sort(listaConferencia, ordenacao);
			return listaConferencia;
		}
		else if(sort.equals("exemplaresDevolucao"))
		{
			FechamentoFisicoLogicoDtoOrdenaPorTotalDevolucao ordenacao = new FechamentoFisicoLogicoDtoOrdenaPorTotalDevolucao(sortorder);
			Collections.sort(listaConferencia, ordenacao);
			return listaConferencia;
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public int buscarQuantidadeConferenciaEncalhe(FiltroFechamentoEncalheDTO filtro){
	
		return this.fechamentoEncalheRepository.buscarQuantidadeConferenciaEncalheNovo(filtro);
	}
	
	private Long calcularDiferenca(FechamentoFisicoLogicoDTO conferencia) {
		 
		if (conferencia.getFisico() == null) {
			conferencia.setFisico(0L);
		}
		
		if(conferencia.getExemplaresDevolucao() == null) {
			conferencia.setExemplaresDevolucao(BigInteger.ZERO);
		}
		
		return conferencia.getFisico().longValue() - conferencia.getExemplaresDevolucao().longValue() ;
		
	}
	
	private void setarInfoComumFechamentoFisicoLogicoDTO(
			FechamentoFisicoLogicoDTO conferencia, 
			boolean fechado,
			Date dataAtual,
			Date dataFimSemana){
		
		if(conferencia.getExemplaresDevolucao() == null)
		{
			conferencia.setExemplaresDevolucao(BigInteger.valueOf(0));
		}
		conferencia.setTotal(new BigDecimal(conferencia.getExemplaresDevolucao()).multiply(conferencia.getPrecoCapaDesconto()));
		conferencia.setFechado(fechado);
		
		if (conferencia.isSuplementar() || conferencia.isChamadao()) {
			  
			conferencia.setEstoque(TipoEstoque.SUPLEMENTAR.getDescricao());
			  
		} else if ("P".equals(conferencia.getTipo()) 
				&& (conferencia.getRecolhimento() != null 
					&& TipoLancamentoParcial.PARCIAL.name().equals(conferencia.getRecolhimento()))) {
			  
			conferencia.setEstoque(TipoEstoque.LANCAMENTO.getDescricao());
  
		} else {
			  
			conferencia.setEstoque("Encalhe");
		}
	}
	
	@Override
	@Transactional
	public void salvarFechamentoEncalhe(FiltroFechamentoEncalheDTO filtro, List<FechamentoFisicoLogicoDTO> listaFechamento, 
										List<FechamentoFisicoLogicoDTO> listaNaoReplicados) {

		FechamentoFisicoLogicoDTO fechamento;
		Long qtd;
		
		for (int i=0; i < listaFechamento.size(); i++) {
			
			fechamento = listaFechamento.get(i);

			FechamentoFisicoLogicoDTO fechamentoNaoReplicado =
				this.selecionarFechamentoPorProdutoEdicao(listaNaoReplicados, fechamento.getProdutoEdicao());
			
			if (fechamentoNaoReplicado != null) {
				
				qtd = fechamentoNaoReplicado.getFisico();

			} else if (filtro.isCheckAll()) {

				qtd = fechamento.getExemplaresDevolucao().longValue();
			
			} else {
				
				qtd = 0l;
			}
			
			FechamentoEncalhePK id = new FechamentoEncalhePK();
			id.setDataEncalhe(filtro.getDataEncalhe());
			ProdutoEdicao pe = new ProdutoEdicao();
			pe.setId(fechamento.getProdutoEdicao());
			id.setProdutoEdicao(pe);
			
			FechamentoEncalhe fechamentoEncalhe = fechamentoEncalheRepository.buscarPorId(id);
			
			if (fechamentoEncalhe == null) {
				
				fechamentoEncalhe = new FechamentoEncalhe();
				fechamentoEncalhe.setFechamentoEncalhePK(id);
				fechamentoEncalhe.setQuantidade(qtd);
				
				fechamentoEncalheRepository.adicionar(fechamentoEncalhe);
			
			} else {
				
				fechamentoEncalhe.setQuantidade(qtd);
				fechamentoEncalheRepository.alterar(fechamentoEncalhe);
			}
			
			fechamento.setFisico(qtd); // retorna valor pra tela
		}
		
		fechamentoEncalheRepository.flush();
	}

	private Integer obterDiaRecolhimento(Date dataRecolhimento) {
		
		if(dataRecolhimento == null) {
			return null;
		}
		
		return DateUtil.obterDiaDaSemana(dataRecolhimento);
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<CotaAusenteEncalheDTO> buscarCotasAusentes(Date dataEncalhe,
			boolean isSomenteCotasSemAcao, String sortorder, String sortname, int page, int rp) {
		
		int startSearch = 0;
		
		if (page >= 0) {
			startSearch = page * rp - rp;	
		} 
		
		Integer diaRecolhimento = obterDiaRecolhimento(dataEncalhe);
		
		List<CotaAusenteEncalheDTO> listaCotaAusenteEncalhe = 
			this.fechamentoEncalheRepository.obterCotasAusentes(dataEncalhe, diaRecolhimento, isSomenteCotasSemAcao, sortorder, sortname, startSearch, rp);

		if (!isSomenteCotasSemAcao){
			
		    for (CotaAusenteEncalheDTO cotaAusenteEncalheDTO : listaCotaAusenteEncalhe) {

				carregarDescricaoCotaAusente(cotaAusenteEncalheDTO);
			}
		}
		
		return listaCotaAusenteEncalhe;
	}

	private void carregarDescricaoCotaAusente(CotaAusenteEncalheDTO cotaAusenteEncalheDTO) {
	
		if(!cotaAusenteEncalheDTO.isIndPossuiChamadaEncalheCota()) {
			
			if (cotaAusenteEncalheDTO.isIndMFCNaoConsolidado()) {
				
				cotaAusenteEncalheDTO.setAcao(" Sem C.E.-Cota com Divida ");
			} 
			else{
				
				cotaAusenteEncalheDTO.setAcao(" Sem C.E.-Cobrado ");
			}
		} else if (cotaAusenteEncalheDTO.isUnificacao()){
			
			cotaAusenteEncalheDTO.setAcao(" Cota com unificação ");
		} else {
			
			if (cotaAusenteEncalheDTO.isFechado()) {
				
				cotaAusenteEncalheDTO.setAcao("Cobrado");
				
			} else if (cotaAusenteEncalheDTO.isPostergado()) {

				Date dataPostergacao = 
					this.fechamentoEncalheRepository.obterChamdasEncalhePostergadas(
						cotaAusenteEncalheDTO.getIdCota(), cotaAusenteEncalheDTO.getDataEncalhe());
				
				cotaAusenteEncalheDTO.setAcao(
					"Postergado, " + DateUtil.formatarData(dataPostergacao, "dd/MM/yyyy"));
			} 
			
		}
		
		if(cotaAusenteEncalheDTO.isOperacaoDiferenciada()) {
			cotaAusenteEncalheDTO.setAcao( 
					(cotaAusenteEncalheDTO.getAcao()==null || cotaAusenteEncalheDTO.getAcao().trim().isEmpty()) ?  
					"Operação Diferenciada" : cotaAusenteEncalheDTO.getAcao()+ " / Operação Diferenciada" 
			);
		}
		
		
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public Integer buscarTotalCotasAusentes(Date dataEncalhe, boolean isSomenteCotasSemAcao) {
		
		Integer diaRecolhimento = obterDiaRecolhimento(dataEncalhe);
		
		return this.fechamentoEncalheRepository.obterTotalCotasAusentes(dataEncalhe, diaRecolhimento, isSomenteCotasSemAcao, null, null, 0, 0);
	}


	@Override
	@Transactional
	public void postergarCotas(Date dataEncalhe, Date dataPostergacao, List<Long> idsCotas) {
	
		if (idsCotas == null || idsCotas.isEmpty()) {
			throw new IllegalArgumentException("Lista de ids das cotas não pode ser nula e nem vazia.");
		}
		
		if (dataEncalhe == null) {
			throw new IllegalArgumentException("Data de encalhe não pode ser nula.");
		}
		
		List<CotaAusenteEncalheDTO> listaCotasAusentes = this.buscarCotasAusentes(dataEncalhe, true, null, null, 0, 0);
		
		Map<String, ChamadaEncalhe> chamadasEncalheAPostergar = obterChamadasEncalheAPostergar(
				dataEncalhe, dataPostergacao, listaCotasAusentes);
		
		for (Long idCota : idsCotas) {
		
			this.postergar(dataEncalhe, dataPostergacao, idCota, chamadasEncalheAPostergar);
		}
	}
	
	@Override
	@Transactional
	public void postergarTodasCotas(Date dataEncalhe, Date dataPostergacao, List<CotaAusenteEncalheDTO> listaCotaAusenteEncalhe) {
	
		if (dataEncalhe == null) {
			throw new IllegalArgumentException("Data de encalhe não pode ser nula.");
		}
		
		Map<String, ChamadaEncalhe> chamadasEncalheAPostergar = obterChamadasEncalheAPostergar(
				dataEncalhe, dataPostergacao, listaCotaAusenteEncalhe);
		
		for (CotaAusenteEncalheDTO cotaAusente : listaCotaAusenteEncalhe) {
		
			this.postergar(dataEncalhe, dataPostergacao, cotaAusente.getIdCota(), chamadasEncalheAPostergar);
		}
	}

	private Map<String, ChamadaEncalhe> obterChamadasEncalheAPostergar(
			Date dataEncalhe, Date dataPostergacao,
			List<CotaAusenteEncalheDTO> listaCotaAusenteEncalhe) {
		
		Integer sequencia = this.chamadaEncalheRepository.obterMaiorSequenciaPorDia(dataPostergacao);
		
		Set<ChamadaEncalhe> chamadasEncalheAPostergarSet = new HashSet<>();
		for (CotaAusenteEncalheDTO cotaAusente : listaCotaAusenteEncalhe) {
			chamadasEncalheAPostergarSet.addAll(chamadaEncalheRepository.obterChamadasEncalhePor(dataEncalhe, cotaAusente.getIdCota()));			
		}
		
		List<ChamadaEncalhe> chamadasEncalheAPostergarList = new ArrayList<>(chamadasEncalheAPostergarSet);
		Collections.sort(chamadasEncalheAPostergarList, new Comparator<ChamadaEncalhe>() {
		    public int compare(ChamadaEncalhe o1, ChamadaEncalhe o2) {
		        return (o1.getSequencia() == o2.getSequencia() ? 0 : (o1.getSequencia() < o2.getSequencia() ? -1 : 1));
		    }
		});
		
		Map<String, ChamadaEncalhe> chamadasEncalheAPostergar = new HashMap<>();
		for(ChamadaEncalhe ce : chamadasEncalheAPostergarList) {
			ce.setSequencia(++sequencia);
			chamadasEncalheAPostergar.put(ce.getProdutoEdicao().getId().toString(), ce);
		}
		
		return chamadasEncalheAPostergar;
		
	}

	@Override
	@Transactional(noRollbackFor = GerarCobrancaValidacaoException.class)
	public void cobrarCota(Date dataOperacao, Usuario usuario, Long idCota) throws GerarCobrancaValidacaoException {

		Cota cota = this.cotaRepository.buscarCotaPorID(idCota);
		
		this.realizarCobrancaCotas(dataOperacao, usuario, null, cota);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, 
		noRollbackFor={GerarCobrancaValidacaoException.class, AutenticacaoEmailException.class})
	public void realizarCobrancaCotas(Date dataOperacao, Usuario usuario, 
			List<CotaAusenteEncalheDTO> listaCotasAusentes, Cota cotaAusente) throws GerarCobrancaValidacaoException {

		ValidacaoVO validacaoVO = new ValidacaoVO();

		if (cotaAusente != null){
			
			listaCotasAusentes = new ArrayList<>();
			CotaAusenteEncalheDTO cotaAusenteEncalheDTO = new CotaAusenteEncalheDTO();
			cotaAusenteEncalheDTO.setIdCota(cotaAusente.getId());
			listaCotasAusentes.add(cotaAusenteEncalheDTO);
		}
		
		for (CotaAusenteEncalheDTO cotaAusenteEncalheDTO : listaCotasAusentes) {

			this.realizarCobrancaCota(dataOperacao,
									  usuario, 
									  cotaAusenteEncalheDTO.getIdCota(),
									  validacaoVO);
		}

		// Se um dia precisar tratar as mensagens de erro de e-mail, elas estão nesta lista
		/*if (validacaoEmails.getListaMensagens() != null && !validacaoEmails.getListaMensagens().isEmpty()) {
		}*/
		
		if (validacaoVO.getListaMensagens() != null && !validacaoVO.getListaMensagens().isEmpty()){
			
			throw new GerarCobrancaValidacaoException(validacaoVO);
		}		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, 
				   noRollbackFor={GerarCobrancaValidacaoException.class, AutenticacaoEmailException.class})
	public void realizarCobrancaCota(Date dataOperacao,
									                 Usuario usuario,
									                 Long idCota,
									                 ValidacaoVO validacaoVO) { 
		
		Date dataOperacaoDistribuidor = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		Set<String> nossoNumeroEnvioEmail = new HashSet<String>();
		
		Cota cota = this.cotaRepository.buscarCotaPorID(idCota);
		
		if (cota == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Cota inexistente.");
		}
		
		BigDecimal reparte =
				this.chamadaEncalheCotaRepository.obterReparteDaChamaEncalheCota(cota.getNumeroCota(), dataOperacao, false, false);
		
		reparte = reparte != null ? reparte : BigDecimal.ZERO;
		
		this.negociacaoDividaService.abaterNegociacaoPorComissao(idCota, reparte, usuario);
		
		//COTA COM TIPO ALTERADO NA DATA DE OPERAÇÃO AINDA É TRATADA COMO CONSIGNADA ATÉ FECHAMENTO DO DIA
        boolean isAlteracaoTipoCotaNaDataAtual = this.cotaService.isCotaAlteradaNaData(cota,dataOperacao);
		
		this.gerarMovimentosFinanceiros(cota, dataOperacao, dataOperacaoDistribuidor, usuario, isAlteracaoTipoCotaNaDataAtual);
		
		if (!this.cotaUnificacaoRepository.verificarCotaUnificada(cota.getNumeroCota()) &&
			!this.cotaUnificacaoRepository.verificarCotaUnificadora(cota.getNumeroCota())){
			
			if (cota.getTipoCota().equals(TipoCota.CONSIGNADO) || isAlteracaoTipoCotaNaDataAtual){
	
				try {
					
					boolean existeBoletoAntecipado =  this.boletoService.existeBoletoAntecipadoCotaDataRecolhimento(cota.getId(), dataOperacaoDistribuidor);
	
					if (existeBoletoAntecipado){
					
					    this.gerarCobrancaService.gerarDividaPostergada(cota.getId(), usuario.getId());
					}
					else{
					
					    this.gerarCobrancaService.gerarCobranca(cota.getId(), usuario.getId(), nossoNumeroEnvioEmail);
					}    
				} 
				catch (GerarCobrancaValidacaoException e) {
	
					if (validacaoVO.getListaMensagens() == null){
						
						validacaoVO.setListaMensagens(new ArrayList<String>());
					}
	
					validacaoVO.getListaMensagens().addAll(e.getValidacaoVO().getListaMensagens());
				}
			}
		}
			
		List<ChamadaEncalheCota> listaChamadaEncalheCota = 
			this.chamadaEncalheCotaRepository.obterListChamadaEncalheCota(
				cota.getId(), dataOperacao);
		
		for (ChamadaEncalheCota chamadaEncalheCota : listaChamadaEncalheCota) {
			
			chamadaEncalheCota.setFechado(true);
			
			this.chamadaEncalheCotaRepository.merge(chamadaEncalheCota);
		}
	}

	private void gerarMovimentosFinanceiros(Cota cota, Date dataOperacao, Date dataOperacaoDistribuidor,
			Usuario usuario, boolean isAlteracaoTipoCotaNaDataAtual) {
		
		if (cota.getTipoCota().equals(TipoCota.CONSIGNADO) || isAlteracaoTipoCotaNaDataAtual){
		
			//CANCELA DIVIDA EXCLUI CONSOLIDADO E MOVIMENTOS FINANCEIROS DE REPARTE X ENCALHE (RECEBIMENTO_REPARTE E ENVIO_ENCALHE) PARA QUE SEJAM RECRIADOS
			this.gerarCobrancaService.cancelarDividaCobranca(null, 
															 cota.getId(), 
					                                         dataOperacaoDistribuidor, 
					                                         true);
		}
		else if (cota.getTipoCota().equals(TipoCota.A_VISTA)){

			//EXLUI MOVIMENTOS FINANCEIROS COTA PARA CRIÁ-LOS NOVAMENTE
			this.movimentoFinanceiroCotaService.removerMovimentosFinanceirosCotaConferenciaNaoConsolidados(cota.getNumeroCota(), dataOperacaoDistribuidor);	
		}	
	
		//CRIA MOVIMENTOS FINANCEIROS DE REPARTE X ENCALHE (RECEBIMENTO_REPARTE E ENVIO_ENCALHE)
		movimentoFinanceiroCotaService.gerarMovimentoFinanceiroCota(cota, 
																	dataOperacaoDistribuidor,
																	usuario,
																	null);
	}

	@Override
	@Transactional(readOnly=true)
	public BigDecimal buscarValorTotalEncalhe(Date dataEncalhe, Long idCota) {
		
		List<FechamentoFisicoLogicoDTO> list = fechamentoEncalheRepository.buscarValorTotalEncalhe(dataEncalhe, idCota);
        BigDecimal soma = BigDecimal.ZERO;
        
        for (FechamentoFisicoLogicoDTO dto : list) {
               soma = soma.add(new BigDecimal(dto.getExemplaresDevolucao()).multiply(dto.getPrecoCapa()));
        }
        
        return soma;
	}
	

	/**
	 * Ao realizar o encerramento do encalhe serão gerados
	 * registros em MovimentoEstoqueCota e MovimentoEstoque
	 * que correspondem ao produtos que as cotas devolveram
	 * ao distribuidor de forma juramentada.
	 * 
	 */
	private void processarMovimentosProdutosJuramentados(Date dataEncalhe, Usuario usuario, Date dataOperacao){
		
		List<MovimentoEstoqueCotaGenericoDTO> listaMovimentoEstoqueCota = 
				movimentoEstoqueCotaRepository.obterListaMovimentoEstoqueCotaDevolucaoJuramentada(dataEncalhe);
		
		TipoMovimentoEstoque tipoMovEstoqueEnvioJornaleiroJuramentado = tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(GrupoMovimentoEstoque.ENVIO_JORNALEIRO_JURAMENTADO);
		
		for(MovimentoEstoqueCotaGenericoDTO item : listaMovimentoEstoqueCota) {
						
			movimentoEstoqueService.gerarMovimentoEstoque(
					null, 
					item.getIdProdutoEdicao(), 
					usuario.getId(), 
					item.getQtde(), 
					tipoMovEstoqueEnvioJornaleiroJuramentado);
			
			this.processarEstudoCotaLancamentoParcial(item);
		}
		
	}
	
	/*
	 Cria estudo e estudo cota para os proximos lançamentos parciais juramentado
	 */
	private void processarEstudoCotaLancamentoParcial(MovimentoEstoqueCotaGenericoDTO item) {
		
		Lancamento lancamentoParcial = lancamentoRepository.obterLancamentoParcialChamadaEncalhe(item.getIdChamadaEncalhe());
		
		if(lancamentoParcial == null){
			return;
		}
		
		Lancamento proximoLancamentoPeriodo = parciaisService.getProximoLancamentoPeriodo(lancamentoParcial);
		
		if(proximoLancamentoPeriodo == null){
			return;
		}
		
		Estudo estudo = proximoLancamentoPeriodo.getEstudo();
		
		if (estudo == null) {
			
			EstudoGerado estudoGerado = 
				estudoService.criarEstudo(
					proximoLancamentoPeriodo.getProdutoEdicao(), 
						item.getQtde(), proximoLancamentoPeriodo.getDataLancamentoDistribuidor());
			
			estudo = estudoService.liberar(estudoGerado.getId());
			
		} else {
			
			BigInteger reparteEstudo = estudo.getQtdeReparte().add(item.getQtde());
			estudo.setQtdeReparte(reparteEstudo);
			
			estudo = estudoRepository.merge((Estudo)estudo);
		}

		estudoCotaService.criarEstudoCotaJuramentado(
			proximoLancamentoPeriodo.getProdutoEdicao(), 
				estudo, item.getQtde(), new Cota(item.getIdCota()));	
	}

	@Override
	@Transactional
	public List<CotaDTO> obterListaCotaConferenciaNaoFinalizada(Date dataOperacao) {
		return conferenciaEncalheRepository.obterListaCotaConferenciaNaoFinalizada(dataOperacao);
	}
	
	@Override
	@Transactional
	public Set<String> encerrarOperacaoEncalhe(Date dataEncalhe, Usuario usuario, FiltroFechamentoEncalheDTO filtroSessao, 
			List<FechamentoFisicoLogicoDTO> listaEncalheSessao, boolean cobrarCotas)  {

		Integer totalCotasAusentes = this.buscarTotalCotasAusentesSemPostergado(dataEncalhe, true, true);
		
		if (totalCotasAusentes > 0) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Cotas ausentes existentes!");
		}
		
		ControleFechamentoEncalhe controleFechamentoEncalhe = new ControleFechamentoEncalhe();
		controleFechamentoEncalhe.setDataEncalhe(dataEncalhe);
		controleFechamentoEncalhe.setUsuario(usuario);
		
		this.fechamentoEncalheRepository.salvarControleFechamentoEncalhe(controleFechamentoEncalhe);
		
		//TODO: Refatorar a parte de fechamento de encalhe para melhor desempenho
		List<FechamentoFisicoLogicoDTO> listaEncalhe = this.buscarFechamentoEncalhe(filtroSessao, null, null, null, null);
		for (FechamentoFisicoLogicoDTO itemSessao : listaEncalheSessao) {
			for(FechamentoFisicoLogicoDTO itemFechamento : listaEncalhe) {
				if(itemSessao.getCodigo().equals(itemFechamento.getCodigo())
						&& itemSessao.getEdicao().equals(itemFechamento.getEdicao())) {
					
					if(itemSessao.getFisico()==null)
						itemSessao.setFisico(itemFechamento.getExemplaresDevolucao().longValue());
						
					itemFechamento.setFisico(itemSessao.getFisico());
					itemFechamento.setDiferenca(itemSessao.getFisico().longValue() - itemFechamento.getExemplaresDevolucao().longValue());
				}
			}
		}
		
		if(!listaEncalhe.isEmpty()) {
			
			for(FechamentoFisicoLogicoDTO item : listaEncalhe) {
				
				gerarMovimentoFaltasSobras(item, usuario);
				
				this.tratarAtualizacaoProximoLancamentoParcial(item, usuario, item.getFisico());
			}
		}
		
		this.processarMovimentosProdutosJuramentados(dataEncalhe, usuario, this.distribuidorRepository.obterDataOperacaoDistribuidor());
		
		if(ObrigacaoFiscal.COTA_TOTAL.equals(distribuidorRepository.obrigacaoFiscal())
				|| ObrigacaoFiscal.COTA_NFE_VENDA.equals(distribuidorRepository.obrigacaoFiscal())) {
			this.gerarNotaFiscal(dataEncalhe);
		}
		
		//cobra cotas as demais cotas, no caso, as não ausentes e com unificação
		Set<String> nossoNumero = new HashSet<String>();
		
		if (cobrarCotas){
			try {
				
				this.gerarCobrancaService.gerarCobranca(null, usuario.getId(), nossoNumero);
			} catch (GerarCobrancaValidacaoException e) {
				
				throw new ValidacaoException(e.getValidacaoVO());
			}
		}
		
		return nossoNumero;
	}

	private void tratarAtualizacaoProximoLancamentoParcial(FechamentoFisicoLogicoDTO item,
														   Usuario usuario,
														   Long encalheFisico) {
		
		if (!item.isParcial()) {
			
			return;
		}
		
		Lancamento lancamentoParcial =
			lancamentoRepository.obterLancamentoParcialChamadaEncalhe(item.getChamadaEncalheId());
		
		if (lancamentoParcial != null) {
			
			this.parciaisService.atualizarReparteDoProximoLancamentoPeriodo(
				lancamentoParcial, usuario, BigInteger.valueOf(encalheFisico));
		}
	}

	private void gerarMovimentoFaltasSobras(FechamentoFisicoLogicoDTO item, Usuario usuarioLogado) {
		
		BigInteger qntDiferenca = BigInteger.ZERO;
		if(item != null && item.getDiferenca() != null) {
			qntDiferenca = new BigInteger(item.getDiferenca().toString());
		}
		
		if(qntDiferenca.compareTo(BigInteger.ZERO) == 0){
			return;
		}
		
		ProdutoEdicao produtoEdicao = edicaoRepository.buscarPorId(item.getProdutoEdicao());
		
		Diferenca diferenca = new Diferenca();
	
		diferenca.setQtde(qntDiferenca.abs());
		diferenca.setResponsavel(usuarioLogado);
		diferenca.setProdutoEdicao(produtoEdicao);

		if( qntDiferenca.compareTo(BigInteger.ZERO ) < 0 ){
		
			diferenca.setTipoDiferenca(TipoDiferenca.PERDA_EM);

			diferencaEstoqueService.lancarDiferencaAutomatica(diferenca, TipoEstoque.RECOLHIMENTO, 
					                                          StatusAprovacao.PERDA, 
					                                          Origem.TRANSFERENCIA_LANCAMENTO_FALTA_E_SOBRA_FECHAMENTO_ENCALHE);
			
		} else if(qntDiferenca.compareTo(BigInteger.ZERO) > 0){						
			
			diferenca.setTipoDiferenca(TipoDiferenca.GANHO_EM);

			diferencaEstoqueService.lancarDiferencaAutomatica(diferenca,TipoEstoque.RECOLHIMENTO, 
					                                          StatusAprovacao.GANHO, 
					                                          Origem.TRANSFERENCIA_LANCAMENTO_FALTA_E_SOBRA_FECHAMENTO_ENCALHE);
			
		}
	}

	@Transactional(rollbackFor=Exception.class)
	public void gerarNotaFiscal(Date dataEncalhe)  {
		
		List<TipoNotaFiscal> listaTipoNotaFiscal = this.tipoNotaFiscalRepository.obterTiposNotaFiscal(GrupoNotaFiscal.NF_DEVOLUCAO_REMESSA_CONSIGNACAO);
		ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor = 
				this.distribuidorRepository.parametrosRecolhimentoDistribuidor();
		List<Cota> cotas = fechamentoEncalheRepository.buscarCotaFechamentoChamadaEncalhe(dataEncalhe);
		for (Cota cota : cotas) {
		
			try {

				TipoNotaFiscal tipoNotaFiscal = obterTipoNotaFiscal(listaTipoNotaFiscal, cota);
				
				if (tipoNotaFiscal != null) {
				
					List<ItemNotaFiscalSaida> listItemNotaFiscal = 
							this.notaFiscalService.obterItensNotaFiscalPor(
									parametrosRecolhimentoDistribuidor,
									cota, null, null, null, tipoNotaFiscal);
					
					if (listItemNotaFiscal == null || listItemNotaFiscal.isEmpty()) 
						continue;
					
					List<NotaFiscalReferenciada> listaNotasFiscaisReferenciadas = this.notaFiscalService.obterNotasReferenciadas(listItemNotaFiscal);
					
					InformacaoTransporte transporte = this.notaFiscalService.obterTransporte(cota.getId());
					
					Set<Processo> processos = new HashSet<Processo>();
					processos.add(Processo.GERACAO_NF_E);
					
					Long idNotaFiscal = this.notaFiscalService.emitiNotaFiscal(tipoNotaFiscal.getId(), dataEncalhe, cota, 
							listItemNotaFiscal, transporte, null, listaNotasFiscaisReferenciadas, processos, null);
				
					NotaFiscal notaFiscal = this.notaFiscalRepository.buscarPorId(idNotaFiscal);

					this.produtoServicoRepository.atualizarProdutosQuePossuemNota(notaFiscal.getProdutosServicos(), listItemNotaFiscal);
				
				}
				
			} catch (ValidacaoException e) {
				throw e;
			} catch (Exception exception) {
				LOGGER.error(exception.getLocalizedMessage(), exception);
				continue;
			}
		}
	}

	private TipoNotaFiscal obterTipoNotaFiscal(
			List<TipoNotaFiscal> listaTipoNotaFiscal, Cota cota) {
		TipoNotaFiscal  tipoNotaFiscal = null;
		
		Boolean contribuinte = Boolean.FALSE;
		
		if ( cota.getParametrosCotaNotaFiscalEletronica() != null && cota.getParametrosCotaNotaFiscalEletronica().getEmiteNotaFiscalEletronica() != null  ){
			
			contribuinte = cota.getParametrosCotaNotaFiscalEletronica().getEmiteNotaFiscalEletronica();
		}
		
		for (TipoNotaFiscal tipo : listaTipoNotaFiscal){
			if (tipo.isContribuinte() == contribuinte){
				tipoNotaFiscal = tipo;
				break;
			}
		}
		return tipoNotaFiscal;
	}

	@Transactional
	private boolean validarEncerramentoOperacao(Date dataEncalhe) {
		
		int countFechamento = 0;
		int countConferencia = 0;
		boolean porBox;
		
		List<FechamentoEncalhe> listFechamentoEncalhe = this.fechamentoEncalheRepository.buscarFechamentoEncalhe(dataEncalhe);
		
		if (listFechamentoEncalhe.isEmpty()) {
			// Não tem nada salvo
			return false;
		}
		
		if (listFechamentoEncalhe.get(0).getListFechamentoEncalheBox() == null || listFechamentoEncalhe.get(0).getListFechamentoEncalheBox().isEmpty()) {
			// consolidado
			porBox = false;
			countFechamento = listFechamentoEncalhe.size();
		} else {
			// por box
			porBox = true;
			for (FechamentoEncalhe fechamentoEncalhe : listFechamentoEncalhe) {
				countFechamento += fechamentoEncalhe.getListFechamentoEncalheBox().size();
			}
		}
		
		countConferencia = this.fechamentoEncalheRepository.buscaQuantidadeConferencia(dataEncalhe, porBox);
		
		return countFechamento == countConferencia;
	}
	
	@Transactional
	private void postergar(Date dataEncalhe, Date dataPostergacao, Long idCota, Map<String, ChamadaEncalhe> chamadasEncalheAPostergar) {
		
		if(chamadasEncalheAPostergar == null) throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao obter sequência.");
		
		List<ChamadaEncalheCota> listChamadaEncalheCota = this.fechamentoEncalheRepository.buscarChamadaEncalheCota(dataEncalhe, idCota);

		for (ChamadaEncalheCota chamadaEncalheCota : listChamadaEncalheCota) {
			
			// Atualizando para postergado
			chamadaEncalheCota.setPostergado(true);
			this.chamadaEncalheCotaRepository.merge(chamadaEncalheCota);
			
			// Criando chamada de encalhe
			ChamadaEncalhe chamadaEncalhe = this.chamadaEncalheRepository.obterPorNumeroEdicaoEDataRecolhimento(
					chamadaEncalheCota.getChamadaEncalhe().getProdutoEdicao(), 
					dataPostergacao, 
					chamadaEncalheCota.getChamadaEncalhe().getTipoChamadaEncalhe());
			
			ChamadaEncalhe chamadaEncalheOriginal = chamadaEncalheCota.getChamadaEncalhe();
			
			if (chamadaEncalhe == null) {
				
				chamadaEncalhe = new ChamadaEncalhe();
				chamadaEncalhe.setDataRecolhimento(dataPostergacao);
				chamadaEncalhe.setProdutoEdicao(chamadaEncalheCota.getChamadaEncalhe().getProdutoEdicao());
				chamadaEncalhe.setTipoChamadaEncalhe(chamadaEncalheCota.getChamadaEncalhe().getTipoChamadaEncalhe());
				
				Set<Lancamento> lancamentos = chamadaEncalheRepository.obterLancamentos(chamadaEncalheOriginal.getId());
				
				chamadaEncalhe.setLancamentos(lancamentos);
				if(chamadaEncalheCota.getChamadaEncalhe() != null 
						&& chamadaEncalheCota.getChamadaEncalhe().getProdutoEdicao() != null
							&& chamadaEncalheCota.getChamadaEncalhe().getProdutoEdicao().getId() != null) {
					
					chamadaEncalhe.setSequencia(chamadasEncalheAPostergar.get(chamadaEncalheCota.getChamadaEncalhe().getProdutoEdicao().getId().toString()).getSequencia());
				}
				
				this.chamadaEncalheRepository.adicionar(chamadaEncalhe);
			} 
			
			if(BigInteger.ZERO.compareTo(chamadaEncalheCota.getQtdePrevista()) < 0 ) {

				// Criando novo chamadaEncalheCota
				ChamadaEncalheCota cce = new ChamadaEncalheCota();
				cce.setChamadaEncalhe(chamadaEncalhe);
				cce.setCota(chamadaEncalheCota.getCota());
				cce.setQtdePrevista(chamadaEncalheCota.getQtdePrevista());
				this.chamadaEncalheCotaRepository.adicionar(cce);
				
			}
			
		}
	}

	private FechamentoFisicoLogicoDTO selecionarFechamentoPorProdutoEdicao(List<FechamentoFisicoLogicoDTO> fechamentos, 
																		   Long codigoProdutoEdicao) {
		
		if (fechamentos == null 
				|| fechamentos.isEmpty()
				|| codigoProdutoEdicao == null) {
			
			return null;
		}
		
		for (FechamentoFisicoLogicoDTO fechamento : fechamentos) {
			
			if (codigoProdutoEdicao.equals(fechamento.getProdutoEdicao())) {
				
				return fechamento;
			}
		}
		
		return null;
	}
	
	@Override
	@Transactional
	public void salvarFechamentoEncalheBox(FiltroFechamentoEncalheDTO filtro, 
			                           	   List<FechamentoFisicoLogicoDTO> listaFechamento, 
										   List<FechamentoFisicoLogicoDTO> listaNaoReplicados) {
		
		
		FechamentoFisicoLogicoDTO fechamento;
		Long qtd;
		
		for (int i=0; i < listaFechamento.size(); i++) {
			
			fechamento = listaFechamento.get(i);
			
			FechamentoFisicoLogicoDTO fechamentoNaoReplicado =
				this.selecionarFechamentoPorProdutoEdicao(listaNaoReplicados, fechamento.getProdutoEdicao());
			
			if (fechamentoNaoReplicado != null) {
				
				qtd = fechamentoNaoReplicado.getFisico();

			} else if (filtro.isCheckAll()) {

				qtd = fechamento.getExemplaresDevolucao().longValue();
			
			} else {
				
				qtd = 0l;
			}
			
			FechamentoEncalhePK id = new FechamentoEncalhePK();
			id.setDataEncalhe(filtro.getDataEncalhe());
			ProdutoEdicao pe = new ProdutoEdicao();
			pe.setId(fechamento.getProdutoEdicao());
			id.setProdutoEdicao(pe);
			FechamentoEncalheBox fechamentoEncalheBox = new FechamentoEncalheBox();
			
			FechamentoEncalheBoxPK fechamentoEncalheBoxPK =  new  FechamentoEncalheBoxPK();
			Box box = new Box();
			box.setId(filtro.getBoxId());
			fechamentoEncalheBoxPK.setBox(box);
			
			
			FechamentoEncalhe fechamentoEncalhe = fechamentoEncalheRepository.buscarPorId(id);
			if (fechamentoEncalhe == null) {
				fechamentoEncalhe = new FechamentoEncalhe();
				fechamentoEncalhe.setFechamentoEncalhePK(id);
				fechamentoEncalheRepository.adicionar(fechamentoEncalhe);
				fechamentoEncalheBox.setQuantidade(qtd);
				fechamentoEncalheBoxPK.setFechamentoEncalhe(fechamentoEncalhe);
				fechamentoEncalheBox.setFechamentoEncalheBoxPK(fechamentoEncalheBoxPK);
				fechamentoEncalheBoxRepository.adicionar(fechamentoEncalheBox);
				
			} else {
		
				fechamentoEncalheBox =	fechamentoEncalheBoxRepository.buscarPorId(fechamentoEncalheBoxPK);
				if ( fechamentoEncalheBox == null  ) {
					fechamentoEncalheBoxPK.setFechamentoEncalhe(fechamentoEncalhe);
					fechamentoEncalheBox = new FechamentoEncalheBox();
					fechamentoEncalheBox.setFechamentoEncalheBoxPK(fechamentoEncalheBoxPK);
					fechamentoEncalheBox.setQuantidade(qtd);
					
				} else {
					fechamentoEncalheBox.setQuantidade(qtd);
				}
				fechamentoEncalheBoxRepository.merge(fechamentoEncalheBox);
				
			}
			
			fechamento.setFisico(qtd); // retorna valor pra tela
		}
		
	}
	
	/**
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.FechamentoEncalheService#existeFechamentoEncalheDetalhado(br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO)
	 */
	@Override
	@Transactional(readOnly=true)
	public Boolean existeFechamentoEncalheDetalhado(FiltroFechamentoEncalheDTO filtro) {
		
		return this.fechamentoEncalheBoxRepository.verificarExistenciaFechamentoEncalheDetalhado(filtro.getDataEncalhe());
	 
	}

	/**
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.FechamentoEncalheService#existeFechamentoEncalheConsolidado(br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO)
	 */
	@Override
	@Transactional(readOnly=true)
	public Boolean existeFechamentoEncalheConsolidado(FiltroFechamentoEncalheDTO filtro) {
		
		return this.fechamentoEncalheRepository.verificarExistenciaFechamentoEncalheConsolidado(filtro.getDataEncalhe());

	}
	
	
	@Transactional
	public void converteFechamentoDetalhadoEmConsolidado(FiltroFechamentoEncalheDTO filtro) {
		List<FechamentoFisicoLogicoDTO> listaConferencia = this.buscarFechamentoEncalhe(filtro, null, "codigo", null, null);
		FechamentoFisicoLogicoDTO fechamento;
		for (int i=0; i < listaConferencia.size(); i++) {
			fechamento = listaConferencia.get(i);
			FechamentoEncalhePK id = new FechamentoEncalhePK();
			id.setDataEncalhe(filtro.getDataEncalhe());
			ProdutoEdicao pe = new ProdutoEdicao();
			pe.setId(fechamento.getProdutoEdicao());
			id.setProdutoEdicao(pe);
			FechamentoEncalhe fechamentoEncalhe = fechamentoEncalheRepository.buscarPorId(id);
			if ( fechamentoEncalhe == null){
				continue;
			}
			
			for(FechamentoEncalheBox encalheBox: fechamentoEncalhe.getListFechamentoEncalheBox() ) {
				if (fechamentoEncalhe.getQuantidade() == null  ){
					fechamentoEncalhe.setQuantidade(encalheBox.getQuantidade());
				} else {
					fechamentoEncalhe.setQuantidade(fechamentoEncalhe.getQuantidade()+encalheBox.getQuantidade());
				}
				fechamentoEncalheBoxRepository.remover(encalheBox);
			}
			
			fechamentoEncalhe.setListFechamentoEncalheBox(null);
			fechamentoEncalheRepository.alterar(fechamentoEncalhe);
		
		}
		
	}
	

	@Override
	@Transactional
	public ControleFechamentoEncalhe buscaControleFechamentoEncalhePorData(Date dataFechamentoEncalhe) {
		return fechamentoEncalheRepository.buscaControleFechamentoEncalhePorData(dataFechamentoEncalhe);
	}

	@Override
	@Transactional
	public Date buscaDataUltimoControleFechamentoEncalhe() {
		return fechamentoEncalheRepository.buscaDataUltimoControleFechamentoEncalhe();
	}

	@Override
	@Transactional
	public Date buscarUltimoFechamentoEncalheDia(Date dataFechamentoEncalhe) {
		return fechamentoEncalheRepository.buscarUltimoFechamentoEncalheDia(dataFechamentoEncalhe);
	}

	@Override
	@Transactional
	public List<AnaliticoEncalheDTO> buscarAnaliticoEncalhe(FiltroFechamentoEncalheDTO filtro, String sortorder, String sortname, Integer page, Integer rp) {
		
		Integer startSearch = null;
		if ( page != null || rp != null ){
			startSearch = page * rp - rp;
		}
		
		return fechamentoEncalheRepository.buscarAnaliticoEncalhe(filtro,  sortorder,  sortname,  startSearch,  rp);
	}
	
	@Transactional
	public BigDecimal obterValorTotalAnaliticoEncalhe(FiltroFechamentoEncalheDTO filtro) {
		
		return fechamentoEncalheRepository.obterValorTotalAnaliticoEncalhe(filtro);
		
	}
	
	@Override
	@Transactional
	public Integer buscarTotalAnaliticoEncalhe(FiltroFechamentoEncalheDTO filtro) {

		return fechamentoEncalheRepository.buscarTotalAnaliticoEncalhe(filtro);
	}

	@Override
	@Transactional(readOnly=true)
	public Date buscarUtimoDiaDaSemanaRecolhimento() {
		
		int codigoInicioSemana = this.distribuidorService.inicioSemana().getCodigoDiaSemana();
		
		Date dataInicioSemana = SemanaUtil.obterDataInicioSemana(codigoInicioSemana, new Date());
			
		Date dataFimSemana = DateUtil.adicionarDias(dataInicioSemana, 6);

		return dataFimSemana;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Boolean validarEncerramentoOperacaoEncalhe(Date data) {
		
		return this.fechamentoEncalheRepository.validarEncerramentoOperacaoEncalhe(data);
	}

	@Override
	public List<GridFechamentoEncalheDTO> listaEncalheTotalParaGrid(
			List<FechamentoFisicoLogicoDTO> listaEncalheSessao) {
		
		List<GridFechamentoEncalheDTO> listaGrid = new ArrayList<GridFechamentoEncalheDTO>();
		for(FechamentoFisicoLogicoDTO encalhe : listaEncalheSessao)
		{
			GridFechamentoEncalheDTO gridFechamento = new GridFechamentoEncalheDTO();
			gridFechamento.setCodigo(Long.parseLong(encalhe.getCodigo()));
			gridFechamento.setFisico(encalhe.getFisico());
			listaGrid.add(gridFechamento);
		}
		return listaGrid;
	}

	@Override
	public Integer buscarTotalCotasAusentesSemPostergado(Date dataEncalhe, boolean isSomenteCotasSemAcao, 
			boolean ignorarUnificacao) {
		
		Integer diaRecolhimento = obterDiaRecolhimento(dataEncalhe);
		
		return this.fechamentoEncalheRepository.obterTotalCotasAusentesSemPostergado(
				dataEncalhe, diaRecolhimento, isSomenteCotasSemAcao, null, null, 0, 0, ignorarUnificacao);
	}
}
