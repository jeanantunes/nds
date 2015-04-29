package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.hibernate.Hibernate;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.assembler.HistoricoTitularidadeCotaDTOAssembler;
import br.com.abril.nds.dto.CaucaoLiquidaDTO;
import br.com.abril.nds.dto.ChequeCaucaoDTO;
import br.com.abril.nds.dto.CotaGarantiaDTO;
import br.com.abril.nds.dto.FormaCobrancaCaucaoLiquidaDTO;
import br.com.abril.nds.dto.ImovelDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.NotaPromissoriaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.CaucaoLiquida;
import br.com.abril.nds.model.cadastro.Cheque;
import br.com.abril.nds.model.cadastro.ChequeImage;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCaucaoLiquida;
import br.com.abril.nds.model.cadastro.ContaBancariaDeposito;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.FormaCobrancaCaucaoLiquida;
import br.com.abril.nds.model.cadastro.GarantiaCotaOutros;
import br.com.abril.nds.model.cadastro.Imovel;
import br.com.abril.nds.model.cadastro.NotaPromissoria;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.TipoCobrancaCotaGarantia;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaCaucaoLiquida;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaChequeCaucao;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaFiador;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaImovel;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaNotaPromissoria;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaOutros;
import br.com.abril.nds.model.cadastro.garantia.pagamento.PagamentoBoleto;
import br.com.abril.nds.model.cadastro.garantia.pagamento.PagamentoCaucaoLiquida;
import br.com.abril.nds.model.cadastro.garantia.pagamento.PagamentoDepositoTransferencia;
import br.com.abril.nds.model.cadastro.garantia.pagamento.PagamentoDescontoCota;
import br.com.abril.nds.model.cadastro.garantia.pagamento.PagamentoDinheiro;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaCaucaoLiquida;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaChequeCaucao;
import br.com.abril.nds.repository.BancoRepository;
import br.com.abril.nds.repository.ChequeImageRepository;
import br.com.abril.nds.repository.ConcentracaoCobrancaCaucaoLiquidaRepository;
import br.com.abril.nds.repository.CotaGarantiaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EnderecoCotaRepository;
import br.com.abril.nds.repository.EnderecoFiadorRepository;
import br.com.abril.nds.repository.FiadorRepository;
import br.com.abril.nds.repository.FormaCobrancaCaucaoLiquidaRepository;
import br.com.abril.nds.service.CotaGarantiaService;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.ValidacaoVO;

/**
 * 
 * @author Diego Fernandes
 * 
 */
@Service
public class CotaGarantiaServiceImpl implements CotaGarantiaService {

	@Autowired
	private CotaGarantiaRepository cotaGarantiaRepository;

	@Autowired
	private CotaRepository cotaRepository;

	@Autowired
	private DistribuidorRepository distribuidorRepository;

	@Autowired
	private FiadorRepository fiadorRepository;
	
	@Autowired
	private EnderecoFiadorRepository enderecoFiadorRepository;
	
	@Autowired
	private ChequeImageRepository chequeImageRepository;
	
	@Autowired 
	private EnderecoCotaRepository enderecoCotaRepository;

	@Autowired 
	private ConcentracaoCobrancaCaucaoLiquidaRepository concentracaoCobrancaRepository;
	
	@Autowired 
	private FormaCobrancaCaucaoLiquidaRepository formaCobrancaRepository;
	
	@Autowired
	private DescontoService descontoService;
	
	@Autowired
	private BancoRepository bancoRepository;
	
    private static final  Logger LOGGER = LoggerFactory.getLogger(CotaGarantiaServiceImpl.class);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.abril.nds.service.CotaGarantiaService#getByCota(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
	public CotaGarantiaDTO<CotaGarantia> getByCota(Long idCota) {
		
		CotaGarantia cotaGarantia = cotaGarantiaRepository.getByCota(idCota);
		
		TipoGarantia tipo = null;
		
		if (cotaGarantia instanceof CotaGarantiaFiador) {
			
			tipo = TipoGarantia.FIADOR;
		
			initFiador(((CotaGarantiaFiador) cotaGarantia).getFiador());
		
		} else if (cotaGarantia instanceof CotaGarantiaImovel) {
			
			tipo = TipoGarantia.IMOVEL;			
			
			CotaGarantiaImovel cotaGarantiaImovel = (CotaGarantiaImovel) cotaGarantia;			
		
			cotaGarantiaImovel.getImoveis().size();

		} else if (cotaGarantia instanceof CotaGarantiaNotaPromissoria) {
		
			tipo = TipoGarantia.NOTA_PROMISSORIA;
		
		} else if (cotaGarantia instanceof CotaGarantiaCaucaoLiquida) {
			
			tipo = TipoGarantia.CAUCAO_LIQUIDA;
			
			CotaGarantiaCaucaoLiquida cotaGarantiaCaucaoLiquida = (CotaGarantiaCaucaoLiquida) cotaGarantia;
			
			cotaGarantiaCaucaoLiquida.getCaucaoLiquidas().size();
			
		} else if (cotaGarantia instanceof CotaGarantiaChequeCaucao) {
			
			tipo = TipoGarantia.CHEQUE_CAUCAO;
			
		} else if (cotaGarantia instanceof CotaGarantiaOutros) {
			
			tipo = TipoGarantia.OUTROS;			
			
			CotaGarantiaOutros cotaGarantiaOutros = (CotaGarantiaOutros) cotaGarantia;

			cotaGarantiaOutros.getOutros().size();
		}

		return new CotaGarantiaDTO<CotaGarantia>(tipo, cotaGarantia);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Long getQtdCotaGarantiaByCota(Long idCota){
		
		return this.cotaGarantiaRepository.getQtdCotaGarantiaByCota(idCota);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.abril.nds.service.CotaGarantiaService#salvaNotaPromissoria(br.
	 * com.abril.nds.model.cadastro.NotaPromissoria, java.lang.Long)
	 */
	@Override
	@Transactional
	public CotaGarantiaNotaPromissoria salvaNotaPromissoria(
			NotaPromissoria notaPromissoria, Long idCota)
			throws ValidacaoException, InstantiationException, IllegalAccessException {

		CotaGarantiaNotaPromissoria cotaGarantiaNota = prepareCotaGarantia(
				idCota, CotaGarantiaNotaPromissoria.class);
		
		cotaGarantiaNota.setTipoGarantia(TipoGarantia.NOTA_PROMISSORIA);

		cotaGarantiaNota.setData(new Date());

		cotaGarantiaNota.setNotaPromissoria(notaPromissoria);
		
		this.setFiadorCota(idCota, null);

		return (CotaGarantiaNotaPromissoria) cotaGarantiaRepository
				.merge(cotaGarantiaNota);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.nds.service.CotaGarantiaService#salvaChequeCaucao
	 * (br.com.abril.nds.model.cadastro.Cheque, java.lang.Long)
	 */
	@Override
	@Transactional
	public CotaGarantiaChequeCaucao salvaChequeCaucao(Cheque cheque, Long idCota)
			throws ValidacaoException, InstantiationException, IllegalAccessException {

		CotaGarantiaChequeCaucao cotaGarantiaCheque = prepareCotaGarantia(
				idCota, CotaGarantiaChequeCaucao.class);
		
		cotaGarantiaCheque.setTipoGarantia(TipoGarantia.CHEQUE_CAUCAO);
		
		if(cheque != null && cheque.getChequeImage() == null && cotaGarantiaCheque != null && cotaGarantiaCheque.getCheque() != null)
			cheque.setChequeImage(cotaGarantiaCheque.getCheque().getChequeImage());
		
		cotaGarantiaCheque.setData(new Date());
		    
		cotaGarantiaCheque.setCheque(cheque);

		this.setFiadorCota(idCota, null);
			
		cotaGarantiaRepository.merge(cotaGarantiaCheque);

		return (CotaGarantiaChequeCaucao) cotaGarantiaCheque;
	}

	/**
	 * @return
	 * @see br.com.abril.nds.repository.DistribuidorRepository#obtemTiposGarantiasAceitas()
	 */
	@Override
	@Transactional(readOnly = true)
	public List<TipoGarantia> obtemTiposGarantiasAceitas() {
		return distribuidorRepository.obtemTiposGarantiasAceitas();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ImovelDTO> obterDadosImoveisDTO(Long idCota){
		
		List<ImovelDTO> imoveisDTO = new ArrayList<ImovelDTO>();
		
	    CotaGarantia cotaGarantia = cotaGarantiaRepository.getByCota(idCota);
		
		if (cotaGarantia instanceof CotaGarantiaImovel) {	
			
			CotaGarantiaImovel cotaGarantiaImovel = (CotaGarantiaImovel) cotaGarantia;			
			
			List<Imovel> imoveis = cotaGarantiaImovel.getImoveis();
			for (Imovel item : imoveis){
				
				imoveisDTO.add(this.convertImovelToImovelDTO(item));
			}
		}
		
		return imoveisDTO;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public List<GarantiaCotaOutros> obterDadosGarantiaOutrosDTO(Long idCota) {
		
		List<GarantiaCotaOutros> garantiaOutros = new ArrayList<GarantiaCotaOutros>();
		
	    CotaGarantia cotaGarantia = cotaGarantiaRepository.getByCota(idCota);
		
		if (cotaGarantia instanceof CotaGarantiaOutros) {	
			
			CotaGarantiaOutros cotaGarantiaOutros = (CotaGarantiaOutros) cotaGarantia;

			if (cotaGarantiaOutros.getOutros() != null) {

				cotaGarantiaOutros.getOutros().size();
				
				garantiaOutros = cotaGarantiaOutros.getOutros();
			}
		}
		
		return garantiaOutros;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Imovel> obterDadosImoveis(Long idCota){
		
		List<Imovel> imoveis = null;
		
	    CotaGarantia cotaGarantia = cotaGarantiaRepository.getByCota(idCota);
		
		if (cotaGarantia instanceof CotaGarantiaImovel) {	
			
			CotaGarantiaImovel cotaGarantiaImovel = (CotaGarantiaImovel) cotaGarantia;			
			
			imoveis = cotaGarantiaImovel.getImoveis();
		}
		
		return imoveis;
	}

	private ImovelDTO convertImovelToImovelDTO(Imovel imovel){
		
		ImovelDTO imovelDTO = new ImovelDTO(imovel.getProprietario(),
											imovel.getEndereco(),
											imovel.getNumeroRegistro(),
											imovel.getValor(),
											imovel.getObservacao());
		return imovelDTO;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.abril.nds.service.CotaGarantiaService#salvaImovel
	 * (br.com.abril.nds.model.cadastro.Imovel, java.lang.Long)
	 */
	@Override
	@Transactional
	public CotaGarantiaImovel salvaImovel(List<Imovel> listaImoveis, Long idCota)
			throws ValidacaoException, InstantiationException, IllegalAccessException {

		CotaGarantiaImovel cotaGarantiaImovel = prepareCotaGarantia(idCota,
				CotaGarantiaImovel.class);
		
		cotaGarantiaImovel.setTipoGarantia(TipoGarantia.IMOVEL);
	
		if (cotaGarantiaImovel.getImoveis() != null
				&& !cotaGarantiaImovel.getImoveis().isEmpty()) {
			this.cotaGarantiaRepository
					.deleteListaImoveis(cotaGarantiaImovel.getId());
		}
		
		cotaGarantiaImovel.setData(new Date());

		cotaGarantiaImovel.setImoveis(listaImoveis);
		
		cotaGarantiaImovel = (CotaGarantiaImovel) cotaGarantiaRepository
				.merge(cotaGarantiaImovel);
		
		this.setFiadorCota(idCota, null);
		
		return cotaGarantiaImovel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.abril.nds.service.CotaGarantiaService#buscaFiador(java.lang.String
	 * , int)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ItemDTO<Long, String>> buscaFiador(String nome, int maxResults) {
		return fiadorRepository.buscaFiador(nome, maxResults);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.abril.nds.service.CotaGarantiaService#getFiador(java.lang.Long,
	 * java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public Fiador getFiador(Long idFiador, String doc) {

		Fiador fiador;

		if (idFiador != null) {
			fiador = fiadorRepository.buscarPorId(idFiador);
		} else if (!StringUtil.isEmpty(doc)) {
			fiador = fiadorRepository.obterPorCpfCnpj(doc);
		} else {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,
					"Ao menos um parametro deve ser diferente de null."));
		}
		if (fiador != null) {
			initFiador(fiador);
		}
		return fiador;
	}

	/**
	 * @param fiador
	 */
	private void initFiador(Fiador fiador) {
		fiador.getTelefonesFiador().size();
		fiador.getGarantias().size();
		fiador.getPessoa().getEnderecos().size();
		Hibernate.initialize(fiador.getEnderecoFiador());
	}
	
	/**
	 * Remove ou adiciona referencia de Fiador na Cota
	 * @param idCota
	 * @param fiador
	 */
	private void setFiadorCota(Long idCota, Fiador fiador){
		
		Cota cota = this.cotaRepository.buscarPorId(idCota);
		cota.setFiador(fiador);
		this.cotaRepository.alterar(cota);
	}

	@Transactional
	@Override
	public CotaGarantiaFiador salvaFiador(Long idFiador, Long idCota) throws ValidacaoException, InstantiationException, IllegalAccessException {
		CotaGarantiaFiador cotaGarantiaFiador = prepareCotaGarantia(idCota, CotaGarantiaFiador.class);
		
		cotaGarantiaFiador.setTipoGarantia(TipoGarantia.FIADOR);
	
		Fiador fiador = fiadorRepository.buscarPorId(idFiador);

		if (fiador == null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Fiador " + idFiador + " não existe."));
		}
		cotaGarantiaFiador.setFiador(fiador);
		cotaGarantiaFiador.setData(new Date());
		
		this.setFiadorCota(idCota, fiador);
		
		return (CotaGarantiaFiador) cotaGarantiaRepository.merge(cotaGarantiaFiador);

	}

	/**
	 * @param idCota
	 * @return
	 * @throws ValidacaoException
	 */
	private Cota getCota(Long idCota) throws ValidacaoException {
		
		Cota cota = this.cotaRepository.buscarPorId(idCota);

		if (cota == null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Cota " + idCota + " não encontrada."));
		}
		
		return cota;
	}
	
	@SuppressWarnings("unchecked")
	private <T extends CotaGarantia> T prepareCotaGarantia(Long idCota,
			Class<T> type) throws InstantiationException,
			IllegalAccessException {

		CotaGarantia cotaGarantia = cotaGarantiaRepository.getByCota(idCota);
		Cota cota = getCota(idCota);
		
		if(cotaGarantia != null && cotaGarantia.getClass() != type) {
			cotaGarantiaRepository.remover(cotaGarantia);
			cotaGarantia = null;
		}
		
		if (cotaGarantia == null) {			
			cotaGarantia = type.newInstance();
			cota.setCotaGarantia(cotaGarantia);	
		}

		return (T) cotaGarantia;
	}
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.CotaGarantiaService#salvarCaucaoLiquida(br.com.abril.nds.model.cadastro.CaucaoLiquida, java.lang.Long)
	 */
	@Transactional
	@Deprecated
	public CotaGarantiaCaucaoLiquida salvarCaucaoLiquida(List<CaucaoLiquida> listaCaucaoLiquida, Long idCota) throws ValidacaoException, InstantiationException, IllegalAccessException {
		
		//TODO retirar esse metodo após a alteração de Front End
		
		return mergeCaucaoLiquida(listaCaucaoLiquida, idCota,null,null);
	}

	private CotaGarantiaCaucaoLiquida mergeCaucaoLiquida(List<CaucaoLiquida> listaCaucaoLiquida, Long idCota,
														PagamentoCaucaoLiquida pagamento, ContaBancariaDeposito contaDepositoCaucaoLiquida) 
														throws InstantiationException, IllegalAccessException {
		
		CotaGarantiaCaucaoLiquida cotaGarantiaCaucaoLiquida = prepareCotaGarantia(idCota, CotaGarantiaCaucaoLiquida.class);
		
		if (cotaGarantiaCaucaoLiquida.getCaucaoLiquidas() == null) {
			cotaGarantiaCaucaoLiquida.setCaucaoLiquidas(new ArrayList<CaucaoLiquida>(listaCaucaoLiquida.size()));
		}
		
		cotaGarantiaCaucaoLiquida.getCaucaoLiquidas().addAll(listaCaucaoLiquida);
		
		cotaGarantiaCaucaoLiquida.setData(new Date());		
		
		cotaGarantiaCaucaoLiquida.setContaBancariaDeposito(contaDepositoCaucaoLiquida);
		
		cotaGarantiaCaucaoLiquida.setFormaPagamento(pagamento);
		
		return (CotaGarantiaCaucaoLiquida) this.cotaGarantiaRepository.merge(cotaGarantiaCaucaoLiquida);
	}
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.CotaGarantiaService#salvarCaucaoLiquida(br.com.abril.nds.model.cadastro.CaucaoLiquida, java.lang.Long)
	 */
	@Transactional
	public CotaGarantiaCaucaoLiquida salvarCaucaoLiquida(List<CaucaoLiquida> listaCaucaoLiquida, Long idCota, PagamentoCaucaoLiquida pagamento, ContaBancariaDeposito conta) throws ValidacaoException, InstantiationException, IllegalAccessException {
	
		return mergeCaucaoLiquida(listaCaucaoLiquida, idCota,pagamento,conta);
	}

	/**
	 * @param idCheque
	 * @return
	 * @see br.com.abril.nds.repository.CotaGarantiaRepository#getCheque(long)
	 */
	@Override
	@Transactional(readOnly=true)
	public byte[] getImageCheque(long idCheque) {
		return chequeImageRepository.getImageCheque(idCheque);
	}
	@Override
	@Transactional
	public void salvaChequeImage(long idCheque, byte[] image){
		ChequeImage chequeImage  = chequeImageRepository.get(idCheque);
		
		if(chequeImage == null){
			chequeImage = new  ChequeImage();
			chequeImage.setId(idCheque);
		}
		chequeImage.setImagem(image);
		
		chequeImageRepository.merge(chequeImage);
		
	}
	
	private ChequeCaucaoDTO convertChequeToChequeCaucaoDTO(Cheque cheque){
		
		ChequeCaucaoDTO chequeCaucaoDTO = new ChequeCaucaoDTO(cheque.getNumeroBanco(),
															  cheque.getNomeBanco(),
															  cheque.getAgencia(),
															  cheque.getDvAgencia(),
															  cheque.getConta(),
															  cheque.getDvConta(),
															  cheque.getValor(),
															  cheque.getNumeroCheque(),
															  cheque.getEmissao(),
															  cheque.getValidade(),
															  cheque.getCorrentista());
		return chequeCaucaoDTO;
	}
	
	@Override
	@Transactional(readOnly = true)
	public ChequeCaucaoDTO obterDadosChequeCaucaoDTO(Long idCota){
		
		ChequeCaucaoDTO chequeCaucaoDTO = new ChequeCaucaoDTO();
		
	    CotaGarantia cotaGarantia = cotaGarantiaRepository.getByCota(idCota);
		
		if (cotaGarantia instanceof CotaGarantiaChequeCaucao) {	
			
			CotaGarantiaChequeCaucao cotaGarantiaChequeCaucao = (CotaGarantiaChequeCaucao) cotaGarantia;			
			
			Cheque cheque = cotaGarantiaChequeCaucao.getCheque();
			
			chequeCaucaoDTO = this.convertChequeToChequeCaucaoDTO(cheque);
		}
		
		return chequeCaucaoDTO;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Cheque obterDadosChequeCaucao(Long idCota){
		
		Cheque cheque = null;
		
	    CotaGarantia cotaGarantia = cotaGarantiaRepository.getByCota(idCota);
		
		if (cotaGarantia instanceof CotaGarantiaChequeCaucao) {	
			
			CotaGarantiaChequeCaucao cotaGarantiaChequeCaucao = (CotaGarantiaChequeCaucao) cotaGarantia;			
			
			cheque = cotaGarantiaChequeCaucao.getCheque();
		}
		
		return cheque;
	}

	@Override
	@Transactional(readOnly = true)
	public NotaPromissoria obterDadosNotaPromissoria(Long idCota){
		
		NotaPromissoria notaPromissoria = null;
		
	    CotaGarantia cotaGarantia = cotaGarantiaRepository.getByCota(idCota);
		
		if (cotaGarantia instanceof CotaGarantiaNotaPromissoria) {	
			
			CotaGarantiaNotaPromissoria cotaGarantiaNotaPromissoria = (CotaGarantiaNotaPromissoria) cotaGarantia;			
			
			notaPromissoria = cotaGarantiaNotaPromissoria.getNotaPromissoria();
		}
		
		return notaPromissoria;
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<String> validarDadosCotaPreImpressao(Long idCota){
		
		List<String> msgs = new ArrayList<String>();
		
		CotaGarantiaNotaPromissoria cotaGarantiaNotaPromissoria = 
			this.cotaGarantiaRepository.getByCota(idCota, CotaGarantiaNotaPromissoria.class);
		
		if(cotaGarantiaNotaPromissoria == null){
			
			msgs.add("Nota Promissória não cadastrada para esta cota.");
		}
		
		EnderecoCota enderecoCota =  enderecoCotaRepository.getPrincipal(idCota);
		
		if(enderecoCota==null  || enderecoCota.getEndereco() == null){
			
			msgs.add( "Endereço não cadastrado para esta cota.");
		}
		
		EnderecoDistribuidor enderecoDistribuidor =  distribuidorRepository.obterEnderecoPrincipal();
		
		if(enderecoDistribuidor==null  || enderecoDistribuidor.getEndereco() == null){
			msgs.add("Endereço não cadastrada para este distribuidor.");
		}
		
		return msgs;
	}

	@Override
	@Transactional(readOnly=true)
	public NotaPromissoriaDTO getDadosImpressaoNotaPromissoria(long idCota) {
		
		NotaPromissoriaDTO dto = new NotaPromissoriaDTO();
		
		Cota cota = cotaRepository.buscarPorId(idCota);
		CotaGarantiaNotaPromissoria cotaGarantiaNotaPromissoria = cotaGarantiaRepository.getByCota(idCota, CotaGarantiaNotaPromissoria.class);
		if(cotaGarantiaNotaPromissoria == null) {
			
			throw new RuntimeException("Nota Promissória não cadastrada para esta cota.");
		}
		
		if(cota.getPessoa() instanceof PessoaJuridica) {
			dto.setDocumentoEmitente(Util.adicionarMascaraCNPJ(cota.getPessoa().getDocumento()));
		} else {
			dto.setDocumentoEmitente(Util.adicionarMascaraCPF(cota.getPessoa().getDocumento()));
		}
		
		dto.setNomeEmitente(cota.getPessoa().getNome());
		dto.setNotaPromissoria(cotaGarantiaNotaPromissoria.getNotaPromissoria());
		
		EnderecoCota enderecoCota =  enderecoCotaRepository.getPrincipal(idCota);
		
		if(enderecoCota==null  || enderecoCota.getEndereco() == null){
			throw new RuntimeException( "Endereço não cadastrado para esta cota.");
		}
		dto.setEnderecoEmitente(enderecoCota.getEndereco());
		
		dto.setNomeBeneficiario(this.distribuidorRepository.obterRazaoSocialDistribuidor());
		dto.setDocumentoBeneficiario(Util.adicionarMascaraCNPJ(this.distribuidorRepository.cnpj()));
		
		EnderecoDistribuidor enderecoDistribuidor =  distribuidorRepository.obterEnderecoPrincipal();
		if(enderecoDistribuidor==null  || enderecoDistribuidor.getEndereco() == null){
			throw new RuntimeException( "Endereço não cadastrada para este distribuidor.");
		}
		dto.setPracaPagamento(enderecoDistribuidor.getEndereco().getCidade());
		
		return dto;
	}

	/**
	 * @param idFiador
	 * @return
	 * @see br.com.abril.nds.repository.EnderecoFiadorRepository#buscaPrincipal(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
	public Endereco buscaEnderecoFiadorPrincipal(Long idFiador) {
		return enderecoFiadorRepository.buscaPrincipal(idFiador);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.CotaGarantiaService#salvaOutros(java.util.List, java.lang.Long)
	 */
	@Override
	@Transactional
	public CotaGarantiaOutros salvaOutros(List<GarantiaCotaOutros> listaOutros, Long idCota) throws ValidacaoException, InstantiationException, IllegalAccessException {
	
		CotaGarantia cotaGarantia = cotaGarantiaRepository.getByCota(idCota);
		
		if(cotaGarantia != null){
			cotaGarantiaRepository.remover(cotaGarantia);
		}
		
		CotaGarantiaOutros cotaGarantiaOutros = new CotaGarantiaOutros();
		
		cotaGarantiaOutros.setData(new Date());
		cotaGarantiaOutros.setOutros(listaOutros);
		
		cotaGarantiaOutros = (CotaGarantiaOutros) cotaGarantiaRepository.merge(cotaGarantiaOutros);
		
		cotaGarantiaOutros.setTipoGarantia(TipoGarantia.OUTROS);
		
		this.setFiadorCota(idCota, null);
		Cota cota = getCota(idCota);
		cota.setCotaGarantia(cotaGarantiaOutros);
		cotaRepository.merge(cota);
		
		return cotaGarantiaOutros;
		
	}
	
	/**
	 * Grava no banco Calção Liquida do tipo Boleto
	 * @param listaCaucaoLiquida
	 * @param idCota
	 * @param formaCobrancaDTO
	 * @return CotaGarantiaCaucaoLiquida
	 * @throws ValidacaoException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@Override
	@Transactional
	public CotaGarantiaCaucaoLiquida salvarCaucaoLiquida(List<CaucaoLiquida> listaCaucaoLiquida, Long idCota, FormaCobrancaCaucaoLiquidaDTO formaCobrancaDTO) throws ValidacaoException, InstantiationException, IllegalAccessException {
		
	    CotaGarantiaCaucaoLiquida cotaGarantiaCaucaoLiquida = prepareCotaGarantia(idCota,
	            CotaGarantiaCaucaoLiquida.class);
		
		FormaCobrancaCaucaoLiquida formaCobranca = null;
		
        if (TipoCobrancaCotaGarantia.BOLETO == cotaGarantiaCaucaoLiquida.getTipoCobranca()){
        	PagamentoBoleto pb = (PagamentoBoleto) cotaGarantiaCaucaoLiquida.getFormaPagamento(); 
        	formaCobranca = pb.getFormaCobrancaCaucaoLiquida();
        }
        
        //FORMA DE PAGAMENTO
        PagamentoCaucaoLiquida pagamento = null;
		PagamentoBoleto pagamentoBoleto = null;
		PagamentoDepositoTransferencia pagamentoDepositoTransferencia = null;
		PagamentoDinheiro pagamentoDinheiro = null;
		PagamentoDescontoCota pagamentoDescontoCota = null;
		
        switch (formaCobrancaDTO.getTipoCobranca()){
        
	        case BOLETO:
	        	
	        	Set<ConcentracaoCobrancaCaucaoLiquida> concentracoesCobranca = null;
	        	boolean novaForma=false;
	        	
	        	if (formaCobranca != null){
	        		novaForma=false;
	        		concentracoesCobranca = formaCobranca.getConcentracaoCobrancaCaucaoLiquida();
	        		
	        		//APAGA CONCENTRACOES COBRANCA DA FORMA DE COBRANCA
					if ((concentracoesCobranca!=null)&&(concentracoesCobranca.size() > 0)){
						formaCobranca.setConcentracaoCobrancaCaucaoLiquida(null);
						for(ConcentracaoCobrancaCaucaoLiquida itemConcentracaoCobranca:concentracoesCobranca){
							this.concentracaoCobrancaRepository.remover(itemConcentracaoCobranca);
						}
					}  
	        	}
	            else{
	            	novaForma=true;
	            	formaCobranca = new FormaCobrancaCaucaoLiquida();
	            }
	    		
	    		//CONCENTRACAO COBRANCA (DIAS DA SEMANA)
	    		concentracoesCobranca = new HashSet<ConcentracaoCobrancaCaucaoLiquida>();
	    		ConcentracaoCobrancaCaucaoLiquida concentracaoCobranca;
	    		if (formaCobrancaDTO.isDomingo()){
	    			
	    			concentracaoCobranca = new ConcentracaoCobrancaCaucaoLiquida();
	    			concentracaoCobranca.setDiaSemana(DiaSemana.DOMINGO);
	    			concentracaoCobranca.setFormaCobrancaCaucaoLiquida(formaCobranca);
	    			
	    			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
	    			concentracoesCobranca.add(concentracaoCobranca);
	    		}
	    		if (formaCobrancaDTO.isSegunda()){
	    			
	    			concentracaoCobranca=new ConcentracaoCobrancaCaucaoLiquida();
	    			concentracaoCobranca.setDiaSemana(DiaSemana.SEGUNDA_FEIRA);
	    			concentracaoCobranca.setFormaCobrancaCaucaoLiquida(formaCobranca);
	    			
	    			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
	    			concentracoesCobranca.add(concentracaoCobranca);
	    		}
	    		if (formaCobrancaDTO.isTerca()){
	    			
	    			concentracaoCobranca=new ConcentracaoCobrancaCaucaoLiquida();
	    			concentracaoCobranca.setDiaSemana(DiaSemana.TERCA_FEIRA);
	    			concentracaoCobranca.setFormaCobrancaCaucaoLiquida(formaCobranca);
	    			
	    			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
	    			concentracoesCobranca.add(concentracaoCobranca);
	    		}
	    		if (formaCobrancaDTO.isQuarta()){
	    			
	    			concentracaoCobranca=new ConcentracaoCobrancaCaucaoLiquida();
	    			concentracaoCobranca.setDiaSemana(DiaSemana.QUARTA_FEIRA);
	    			concentracaoCobranca.setFormaCobrancaCaucaoLiquida(formaCobranca);
	    			
	    			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
	    			concentracoesCobranca.add(concentracaoCobranca);
	    		}
	    		if (formaCobrancaDTO.isQuinta()){
	    			
	    			concentracaoCobranca=new ConcentracaoCobrancaCaucaoLiquida();
	    			concentracaoCobranca.setDiaSemana(DiaSemana.QUINTA_FEIRA);
	    			concentracaoCobranca.setFormaCobrancaCaucaoLiquida(formaCobranca);
	    			
	    			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
	    			concentracoesCobranca.add(concentracaoCobranca);
	    		}
	    		if (formaCobrancaDTO.isSexta()){
	    			
	    			concentracaoCobranca=new ConcentracaoCobrancaCaucaoLiquida();
	    			concentracaoCobranca.setDiaSemana(DiaSemana.SEXTA_FEIRA);
	    			concentracaoCobranca.setFormaCobrancaCaucaoLiquida(formaCobranca);
	    			
	    			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
	    			concentracoesCobranca.add(concentracaoCobranca);
	    		}
	    		if (formaCobrancaDTO.isSabado()){
	    			
	    			concentracaoCobranca=new ConcentracaoCobrancaCaucaoLiquida();
	    			concentracaoCobranca.setDiaSemana(DiaSemana.SABADO);
	    			concentracaoCobranca.setFormaCobrancaCaucaoLiquida(formaCobranca);
	    			
	    			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
	    			concentracoesCobranca.add(concentracaoCobranca);
	    		}
	    		
	    		if(concentracoesCobranca.size()>0){
	    		    formaCobranca.setConcentracaoCobrancaCaucaoLiquida(concentracoesCobranca);
	    		}
	    		
	    		List<Integer> diasdoMes = new ArrayList<Integer>();
	    		diasdoMes.add(formaCobrancaDTO.getDiaDoMes());
	    		diasdoMes.add(formaCobrancaDTO.getPrimeiroDiaQuinzenal());
	    		diasdoMes.add(formaCobrancaDTO.getSegundoDiaQuinzenal());
	    		formaCobranca.setDiasDoMes(diasdoMes);
	    		formaCobranca.setTipoFormaCobranca(formaCobrancaDTO.getTipoFormaCobranca());

	    		if(novaForma){
	    		    formaCobrancaRepository.adicionar(formaCobranca);
	    		}
	    		else{
	    			formaCobrancaRepository.merge(formaCobranca);
	    		}
	    		
	    		pagamentoBoleto = new PagamentoBoleto();
				pagamentoBoleto.setValor(formaCobrancaDTO.getValor());
				pagamentoBoleto.setQuantidadeParcelas(formaCobrancaDTO.getQtdeParcelas());
				pagamentoBoleto.setValorParcela(formaCobrancaDTO.getValorParcela());
				pagamentoBoleto.setFormaCobrancaCaucaoLiquida(formaCobranca);
				
	        break;
	        
            case DESCONTO_COTA:
				
				pagamentoDescontoCota = new PagamentoDescontoCota();
				pagamentoDescontoCota.setValor(formaCobrancaDTO.getValor());
				pagamentoDescontoCota.setDescontoAtual(formaCobrancaDTO.getValorDescontoAtual());
				pagamentoDescontoCota.setPorcentagemUtilizada(formaCobrancaDTO.getUtilizarDesconto());
				pagamentoDescontoCota.setDescontoCota(formaCobrancaDTO.getDescontoCotaDesconto());
				
			break;
			
			case DINHEIRO:
				
				pagamentoDinheiro = new PagamentoDinheiro();
				pagamentoDinheiro.setValor(formaCobrancaDTO.getValor());
				
			break;
			
            case DEPOSITO_TRANSFERENCIA:
				
            	Banco bancoCedente = this.bancoRepository.buscarPorId(formaCobrancaDTO.getIdBanco());
            	
            	pagamentoDepositoTransferencia = new PagamentoDepositoTransferencia();
            	pagamentoDepositoTransferencia.setValor(formaCobrancaDTO.getValor());
            	pagamentoDepositoTransferencia.setBanco(bancoCedente);
				
			break;
        }

    	//CONTA DEPOSITO
        ContaBancariaDeposito contaDeposito = cotaGarantiaCaucaoLiquida.getContaBancariaDeposito();
        if (cotaGarantiaCaucaoLiquida.getContaBancariaDeposito()==null){
		    contaDeposito = new ContaBancariaDeposito();
        }
        contaDeposito.setNomeBanco(formaCobrancaDTO.getNomeBanco());
        contaDeposito.setNomeCorrentista(formaCobrancaDTO.getNomeCorrentista());
        contaDeposito.setAgencia(formaCobrancaDTO.getAgencia());
        contaDeposito.setNumeroBanco(formaCobrancaDTO.getNumBanco());
        contaDeposito.setConta(formaCobrancaDTO.getConta());

        
		if (cotaGarantiaCaucaoLiquida.getCaucaoLiquidas() == null) {
			cotaGarantiaCaucaoLiquida.setCaucaoLiquidas(new ArrayList<CaucaoLiquida>(listaCaucaoLiquida.size()));
		}
		
		cotaGarantiaCaucaoLiquida.getCaucaoLiquidas().addAll(listaCaucaoLiquida);
		
		cotaGarantiaCaucaoLiquida.setData(new Date());	

		cotaGarantiaCaucaoLiquida.setContaBancariaDeposito(contaDeposito);
		
		cotaGarantiaCaucaoLiquida.setFormaPagamento(pagamentoBoleto!=null?pagamentoBoleto:pagamentoDescontoCota!=null?pagamentoDescontoCota:pagamentoDepositoTransferencia!=null?pagamentoDepositoTransferencia:pagamentoDinheiro!=null?pagamentoDinheiro:pagamento);
		
		cotaGarantiaCaucaoLiquida.setTipoCobranca(formaCobrancaDTO.getTipoCobranca());
		
		cotaGarantiaCaucaoLiquida.setTipoGarantia(TipoGarantia.CAUCAO_LIQUIDA);

		this.setFiadorCota(idCota, null);
		
		return (CotaGarantiaCaucaoLiquida) this.cotaGarantiaRepository.merge(cotaGarantiaCaucaoLiquida);
	}
	
	/**
	 * Método responsável por obter lista de CaucaoLiquida da cota
	 * @param idCota: ID da cota
	 * @return List<CaucaoLiquida>
	 */
	@Override
	@Transactional(readOnly = true)
	public List<CaucaoLiquida> obterCaucaoLiquidasCota(Long idCota){

		List<CaucaoLiquida> caucaoLiquidas = cotaGarantiaRepository.getCaucaoLiquidasCota(idCota);
				
		return caucaoLiquidas;
	}
	
	/**
	 * Método responsável por obter os dados de caucao liquida com forma de pagamento Boleto.
	 * @param idCota: ID da cota
	 * @return Data Transfer Object com os dados de caucao liquida
	 */
	@Override
	@Transactional(readOnly = true)
	public FormaCobrancaCaucaoLiquidaDTO obterDadosCaucaoLiquida(Long idCota) {
		
		CotaGarantiaCaucaoLiquida cotaGarantiaCaucaoLiquida = cotaGarantiaRepository.getByCota(idCota,CotaGarantiaCaucaoLiquida.class);
		FormaCobrancaCaucaoLiquidaDTO formaCobrancaDTO = null;	
		
		FormaCobrancaCaucaoLiquida formaCobranca = null;
		PagamentoBoleto pagamentoBoleto = null;
		PagamentoDepositoTransferencia pagamentoDepositoTransferencia = null;
		PagamentoDinheiro pagamentoDinheiro = null;
		PagamentoDescontoCota pagamentoDescontoCota = null;
		
		
		if (cotaGarantiaCaucaoLiquida!=null){

			formaCobrancaDTO = new FormaCobrancaCaucaoLiquidaDTO();
			
			formaCobrancaDTO.setTipoCobranca(cotaGarantiaCaucaoLiquida.getTipoCobranca());
			formaCobrancaDTO.setIdCaucaoLiquida(cotaGarantiaCaucaoLiquida.getId());
			formaCobrancaDTO.setIdCota(idCota);
			
			
			ContaBancariaDeposito contaDeposito = cotaGarantiaCaucaoLiquida.getContaBancariaDeposito();
			if(contaDeposito!=null){
			    formaCobrancaDTO.setAgencia(contaDeposito.getAgencia());
			    formaCobrancaDTO.setConta(contaDeposito.getConta());
			    formaCobrancaDTO.setNumBanco(contaDeposito.getNumeroBanco());
			    formaCobrancaDTO.setNomeBanco(contaDeposito.getNomeBanco());
			    formaCobrancaDTO.setNomeCorrentista(contaDeposito.getNomeCorrentista());
			}
			
			switch (cotaGarantiaCaucaoLiquida.getTipoCobranca()){
			
				case BOLETO:
					
					pagamentoBoleto = (PagamentoBoleto) cotaGarantiaCaucaoLiquida.getFormaPagamento(); 
					
					if (pagamentoBoleto!=null){
						
						formaCobrancaDTO.setValor(pagamentoBoleto.getValor());
						formaCobrancaDTO.setQtdeParcelas(pagamentoBoleto.getQuantidadeParcelas());
						formaCobrancaDTO.setValorParcela(pagamentoBoleto.getValorParcela());
						
		    		    formaCobranca = pagamentoBoleto.getFormaCobrancaCaucaoLiquida();
		    		    
		    		    if (formaCobranca!=null){

		    				Set<ConcentracaoCobrancaCaucaoLiquida> concentracoesCobranca = null;
		    				Integer diaDoMes = null;
		    				Integer primeiroDiaQuinzenal = null;
		    				Integer segundoDiaQuinzenal = null;
		    			
		    				if (formaCobranca.getTipoFormaCobranca() == TipoFormaCobranca.SEMANAL){
		    				    concentracoesCobranca = formaCobranca.getConcentracaoCobrancaCaucaoLiquida();
		    				}		
		    				if (formaCobranca.getTipoFormaCobranca() == TipoFormaCobranca.MENSAL){		
		    				    diaDoMes = (formaCobranca.getDiasDoMes().size()>0)?formaCobranca.getDiasDoMes().get(0):null;
		    				}
		    				if (formaCobranca.getTipoFormaCobranca() == TipoFormaCobranca.QUINZENAL){		
		    					primeiroDiaQuinzenal = (formaCobranca.getDiasDoMes().size()>0)?formaCobranca.getDiasDoMes().get(0):null;
		    					segundoDiaQuinzenal = (formaCobranca.getDiasDoMes().size()>1)?formaCobranca.getDiasDoMes().get(1):null;
		    				}

		    				formaCobrancaDTO.setTipoFormaCobranca(formaCobranca.getTipoFormaCobranca());
		    				formaCobrancaDTO.setDiaDoMes(diaDoMes);
		    				formaCobrancaDTO.setPrimeiroDiaQuinzenal(primeiroDiaQuinzenal);
		    				formaCobrancaDTO.setSegundoDiaQuinzenal(segundoDiaQuinzenal);
		    	
		    				if ((concentracoesCobranca!=null)&&(concentracoesCobranca.size() > 0)){
		    					for (ConcentracaoCobrancaCaucaoLiquida itemConcentracaoCobranca:concentracoesCobranca){
		    						
		    						DiaSemana dia = itemConcentracaoCobranca.getDiaSemana();
		    						if (dia==DiaSemana.DOMINGO){
		    							formaCobrancaDTO.setDomingo(true);
		    						}
		    		
		    						if (dia==DiaSemana.SEGUNDA_FEIRA){
		    							formaCobrancaDTO.setSegunda(true);
		    						}    
		    						
		    						if (dia==DiaSemana.TERCA_FEIRA){
		    							formaCobrancaDTO.setTerca(true);
		    						}    
		    						
		    						if (dia==DiaSemana.QUARTA_FEIRA){
		    							formaCobrancaDTO.setQuarta(true);
		    						}
		    						
		    					    if (dia==DiaSemana.QUINTA_FEIRA){
		    					    	formaCobrancaDTO.setQuinta(true);
		    					    }    
		    					    
		    						if (dia==DiaSemana.SEXTA_FEIRA){
		    							formaCobrancaDTO.setSexta(true);
		    						}    
		    						
		    						if (dia==DiaSemana.SABADO){
		    							formaCobrancaDTO.setSabado(true);
		    						}
		    						
		    					}
		    				}
		    			}
					}
					
				break;
			
				case DEPOSITO_TRANSFERENCIA:
					
					pagamentoDepositoTransferencia = (PagamentoDepositoTransferencia) cotaGarantiaCaucaoLiquida.getFormaPagamento();
					
					if (pagamentoDepositoTransferencia != null){
						
						formaCobrancaDTO.setValor(pagamentoDepositoTransferencia.getValor());
						
						formaCobrancaDTO.setIdBanco(pagamentoDepositoTransferencia.getBanco().getId());
					}
					
				break;
				
				case DINHEIRO:
					
					pagamentoDinheiro = (PagamentoDinheiro) cotaGarantiaCaucaoLiquida.getFormaPagamento();
					
					if (pagamentoDinheiro != null){
						
						formaCobrancaDTO.setValor(pagamentoDinheiro.getValor());
					}
					
				break;
				
				case DESCONTO_COTA:
	
					pagamentoDescontoCota = (PagamentoDescontoCota) cotaGarantiaCaucaoLiquida.getFormaPagamento();
					
					if (pagamentoDescontoCota != null){
						
						formaCobrancaDTO.setValor(pagamentoDescontoCota.getValor());
						formaCobrancaDTO.setValorDescontoAtual(pagamentoDescontoCota.getDescontoAtual());
						formaCobrancaDTO.setUtilizarDesconto(pagamentoDescontoCota.getPorcentagemUtilizada());
						formaCobrancaDTO.setDescontoCotaDesconto(pagamentoDescontoCota.getDescontoCota());
					
					} else {
						
						BigDecimal descontoAtual = this.obterValorComissaoCaucaoLiquida(idCota);

						formaCobrancaDTO.setValorDescontoAtual(descontoAtual);
						formaCobrancaDTO.setDescontoCotaDesconto(descontoAtual);
					}
					
				break;
			}
			
			List<CaucaoLiquida> caucaoLiquidas = cotaGarantiaCaucaoLiquida.getCaucaoLiquidas();
			
			Collection<CaucaoLiquidaDTO> caucaoLiquidasDTO =  new ArrayList<CaucaoLiquidaDTO>();
			
			for (CaucaoLiquida caucaoLiquida : caucaoLiquidas){
				
				caucaoLiquidasDTO.add(new CaucaoLiquidaDTO(caucaoLiquida.getValor(),caucaoLiquida.getAtualizacao()));
			}
			
			formaCobrancaDTO.setCaucoes(caucaoLiquidasDTO);
		}
		
		return formaCobrancaDTO;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public BigDecimal obterValorComissaoCaucaoLiquida(Long idCota) {
		
		Integer numeroCota = this.cotaRepository.buscarNumeroCotaPorId(idCota);

		BigDecimal descontoAtual = this.descontoService.obterComissaoCota(numeroCota);
		
		return descontoAtual;
	}
	
    /**
     * {@inheritDoc}
     */
	@Override
	@Transactional(readOnly = true)
    public CotaGarantiaDTO<?> obterGarantiaHistoricoTitularidadeCota(Long idCota, Long idHistorico) {
        Validate.notNull(idCota, "Identificador da cota não deve ser nulo!");
        Validate.notNull(idHistorico, "Identificador do histórico não deve ser nulo!");
        
	    HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
	    TipoGarantia tipoGarantia = historico.getTipoGarantia();
	    if (tipoGarantia == null) {
	        return null;
	    }
        if (TipoGarantia.FIADOR == tipoGarantia) {
	        return HistoricoTitularidadeCotaDTOAssembler.toCotaGarantiaDTO(historico.getGarantiaFiador());
	    } else if (TipoGarantia.CHEQUE_CAUCAO == tipoGarantia) {
	        return HistoricoTitularidadeCotaDTOAssembler.toCotaGarantiaDTO(historico.getGarantiaChequeCaucao());
	    } else if (TipoGarantia.IMOVEL == tipoGarantia) {
	        return HistoricoTitularidadeCotaDTOAssembler.toCotaGarantiaDTOImovel(historico.getGarantiasImovel());
	    } else if (TipoGarantia.NOTA_PROMISSORIA == tipoGarantia) {
	        return HistoricoTitularidadeCotaDTOAssembler.toCotaGarantiaDTO(historico.getGarantiaNotaPromissoria());
	    } else if (TipoGarantia.CAUCAO_LIQUIDA == tipoGarantia) {
	        return HistoricoTitularidadeCotaDTOAssembler.toCotaGarantiaDTO(historico.getGarantiaCaucaoLiquida());
	    } else if (TipoGarantia.OUTROS == tipoGarantia) {
	        return HistoricoTitularidadeCotaDTOAssembler.toCotaGarantiaDTOOutros(historico.getGarantiasOutros());
	    } else {
	        LOGGER.error("Tipo de garantia não tratado: " + tipoGarantia);
	        throw new UnsupportedOperationException("Tipo de garantia não tratado: " + tipoGarantia);
	    }
    }

    /**
     * {@inheritDoc}
     */
	@Override
	@Transactional(readOnly = true)
    public byte[] getImagemChequeCaucaoHistoricoTitularidade(Long idCota, Long idHistorico) {
	    Validate.notNull(idCota, "Identificador da cota não deve ser nulo!");
        Validate.notNull(idHistorico, "Identificador do histórico não deve ser nulo!");
        
        HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        HistoricoTitularidadeCotaChequeCaucao cheque = historico.getGarantiaChequeCaucao();
        return cheque == null ? null : cheque.getImagem();
    }

	 /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public FormaCobrancaCaucaoLiquidaDTO obterCaucaoLiquidaHistoricoTitularidadeCota(Long idCota, Long idHistorico) {
        Validate.notNull(idCota, "Identificador da cota não deve ser nulo!");
        Validate.notNull(idHistorico, "Identificador do histórico não deve ser nulo!");
        
        HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        HistoricoTitularidadeCotaCaucaoLiquida caucao = historico.getGarantiaCaucaoLiquida();
        return caucao == null ? null : HistoricoTitularidadeCotaDTOAssembler.toFormaCobrancaCaucaoLiquidaDTO(caucao);
    }

	@Override
	@Transactional(readOnly=true)
	public boolean existeCaucaoLiquidasCota(Long idCota) {
		
		if (this.cotaGarantiaRepository.existeCaucaoLiquidasCota(idCota)){
			
			return this.cotaGarantiaRepository.verificarQuitacaoCaucaoLiquida(idCota);
		}
		
		return this.cotaGarantiaRepository.existeCaucaoLiquidasCota(idCota);
	}
}