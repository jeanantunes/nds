package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaGarantiaDTO;
import br.com.abril.nds.dto.FormaCobrancaCaucaoLiquidaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.NotaPromissoriaDTO;
import br.com.abril.nds.dto.ParametroCobrancaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.CaucaoLiquida;
import br.com.abril.nds.model.cadastro.Cheque;
import br.com.abril.nds.model.cadastro.ChequeImage;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCaucaoLiquida;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.model.cadastro.ContaBancariaDeposito;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaCobrancaCaucaoLiquida;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GarantiaCotaOutros;
import br.com.abril.nds.model.cadastro.Imovel;
import br.com.abril.nds.model.cadastro.NotaPromissoria;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
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
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.util.TipoMensagem;
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.abril.nds.service.CotaGarantiaService#getByCota(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public CotaGarantiaDTO getByCota(Long idCota) {
		
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

		return new CotaGarantiaDTO(tipo, cotaGarantia);
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

		cotaGarantiaNota.setData(Calendar.getInstance());

		cotaGarantiaNota.setNotaPromissoria(notaPromissoria);

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

		cotaGarantiaCheque.setData(Calendar.getInstance());
		
		cotaGarantiaCheque.setCheque(cheque);

		return (CotaGarantiaChequeCaucao) cotaGarantiaRepository
				.merge(cotaGarantiaCheque);
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
	

		if (cotaGarantiaImovel.getImoveis() != null
				&& !cotaGarantiaImovel.getImoveis().isEmpty()) {
			this.cotaGarantiaRepository
					.deleteListaImoveis(cotaGarantiaImovel.getId());
		}
		
		cotaGarantiaImovel.setData(Calendar.getInstance());
		

		cotaGarantiaImovel.setImoveis(listaImoveis);
		
		cotaGarantiaImovel = (CotaGarantiaImovel) cotaGarantiaRepository
				.merge(cotaGarantiaImovel);
		
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
	}

	@Transactional
	@Override
	public CotaGarantiaFiador salvaFiador(Long idFiador, Long idCota)
			throws ValidacaoException, InstantiationException, IllegalAccessException {
		CotaGarantiaFiador cotaGarantiaFiador = prepareCotaGarantia(idCota,
				CotaGarantiaFiador.class);
	
		Fiador fiador = fiadorRepository.buscarPorId(idFiador);

		if (fiador == null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,
					"Fiador " + idFiador + " não existe."));
		}
		cotaGarantiaFiador.setFiador(fiador);
		cotaGarantiaFiador.setData(Calendar.getInstance());

		return (CotaGarantiaFiador) cotaGarantiaRepository
				.merge(cotaGarantiaFiador);

	}

	/**
	 * @param idCota
	 * @return
	 * @throws ValidacaoException
	 */
	private Cota getCota(Long idCota) throws ValidacaoException {
		Cota cota = this.cotaRepository.buscarPorId(idCota);

		if (cota == null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,
					"Cota " + idCota + " não encontrada."));
		}
		return cota;
	}
	
	@SuppressWarnings("unchecked")
	private <T extends CotaGarantia> T prepareCotaGarantia(Long idCota,
			Class<T> type) throws InstantiationException,
			IllegalAccessException {

		CotaGarantia cotaGarantia = cotaGarantiaRepository.getByCota(idCota);
		
		if(cotaGarantia != null && cotaGarantia.getClass() != type){
			cotaGarantiaRepository.remover(cotaGarantia);
			cotaGarantia = null;
		}
		
		if (cotaGarantia == null) {			
			cotaGarantia = type.newInstance();
			cotaGarantia.setCota(getCota(idCota));			
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
		
		cotaGarantiaCaucaoLiquida.setData(Calendar.getInstance());		
		
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
	
	
	@Override
	@Transactional(readOnly=true)
	public NotaPromissoriaDTO getDadosImpressaoNotaPromissoria(long idCota){
		NotaPromissoriaDTO dto = new NotaPromissoriaDTO();
		
		CotaGarantiaNotaPromissoria cotaGarantiaNotaPromissoria = cotaGarantiaRepository.getByCota(idCota, CotaGarantiaNotaPromissoria.class);
		if(cotaGarantiaNotaPromissoria == null){
			throw new RuntimeException("Nota Promissória não cadastrada para esta cota.");
		}
		if(cotaGarantiaNotaPromissoria.getCota().getPessoa() instanceof PessoaJuridica){
			dto.setDocumentoEmitente(Util.adicionarMascaraCNPJ(cotaGarantiaNotaPromissoria.getCota().getPessoa().getDocumento()));
		}else{
			dto.setDocumentoEmitente(Util.adicionarMascaraCPF(cotaGarantiaNotaPromissoria.getCota().getPessoa().getDocumento()));
		}
		
		dto.setNomeEmitente(cotaGarantiaNotaPromissoria.getCota().getPessoa().getNome());
		dto.setNotaPromissoria(cotaGarantiaNotaPromissoria.getNotaPromissoria());
		
		EnderecoCota enderecoCota =  enderecoCotaRepository.getPrincipal(idCota);
		
		if(enderecoCota==null  || enderecoCota.getEndereco() == null){
			throw new RuntimeException( "Endereço não cadastrado para esta cota.");
		}
		dto.setEnderecoEmitente(enderecoCota.getEndereco());
		Distribuidor distribuidor= distribuidorRepository.obter();
		
		if(distribuidor==null){
			throw new RuntimeException( "Distribuidor não cadastrado.");
		}
		dto.setNomeBeneficiario(distribuidor.getJuridica().getNome());
		dto.setDocumentoBeneficiario(Util.adicionarMascaraCNPJ( distribuidor.getJuridica().getDocumento()));
		
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
		
		cotaGarantiaOutros.setCota(getCota(idCota));
		cotaGarantiaOutros.setData(Calendar.getInstance());
		cotaGarantiaOutros.setOutros(listaOutros);
		
		cotaGarantiaOutros = (CotaGarantiaOutros) cotaGarantiaRepository
				.merge(cotaGarantiaOutros);
		
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
		

		//COTA GARANTIA CAUCAO LIQUIDA
		CotaGarantiaCaucaoLiquida cotaGarantiaCaucaoLiquida =  cotaGarantiaRepository.getByCota(idCota,CotaGarantiaCaucaoLiquida.class);
		FormaCobrancaCaucaoLiquida formaCobranca = null;
		
        if(cotaGarantiaCaucaoLiquida==null){
        	
        	cotaGarantiaCaucaoLiquida = new CotaGarantiaCaucaoLiquida();
        	Cota cota = cotaRepository.buscarPorId(idCota);
        	cotaGarantiaCaucaoLiquida.setCota(cota);
        }
        else{
        	
        	if (cotaGarantiaCaucaoLiquida.getTipoCobranca().compareTo(TipoCobranca.BOLETO)==0){
        		PagamentoBoleto pb = (PagamentoBoleto) cotaGarantiaCaucaoLiquida.getFormaPagamento(); 
        		formaCobranca = pb.getFormaCobrancaCaucaoLiquida();
        	}
        }
		
        
        if (formaCobranca == null){
        	formaCobranca = new FormaCobrancaCaucaoLiquida();
        }
		
		
		//CONCENTRACAO COBRANCA (DIAS DA SEMANA)
		Set<ConcentracaoCobrancaCaucaoLiquida> concentracoesCobranca = new HashSet<ConcentracaoCobrancaCaucaoLiquida>();
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

		if (formaCobranca.getId()==null){
		    formaCobrancaRepository.adicionar(formaCobranca);
		}
		else{
			formaCobrancaRepository.merge(formaCobranca);
		}

		
		//FORMA DE PAGAMENTO
		PagamentoCaucaoLiquida pagamento = null;
		PagamentoBoleto pagamentoBoleto = null;
		if (formaCobrancaDTO.getTipoCobranca() == TipoCobranca.BOLETO){
			
			pagamentoBoleto = new PagamentoBoleto();
			pagamentoBoleto.setQuantidadeParcelas(formaCobrancaDTO.getQtdeParcelas());
			pagamentoBoleto.setValor(formaCobrancaDTO.getValor());
			pagamentoBoleto.setValorParcela(formaCobrancaDTO.getValorParcela());

			pagamentoBoleto.setFormaCobrancaCaucaoLiquida(formaCobranca);
		} else if (formaCobrancaDTO.getTipoCobranca() == TipoCobranca.DEPOSITO){
			
			pagamento = new PagamentoDepositoTransferencia();
			pagamento.setValor(formaCobrancaDTO.getValor());
		} else{
			
			pagamento = new PagamentoCaucaoLiquida();
			pagamento.setValor(formaCobrancaDTO.getValor());
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
		
		cotaGarantiaCaucaoLiquida.setData(Calendar.getInstance());	

		cotaGarantiaCaucaoLiquida.setContaBancariaDeposito(contaDeposito);
		
		cotaGarantiaCaucaoLiquida.setFormaPagamento(pagamentoBoleto!=null?pagamentoBoleto:pagamento);
		
		cotaGarantiaCaucaoLiquida.setTipoCobranca(formaCobrancaDTO.getTipoCobranca());

		
		return (CotaGarantiaCaucaoLiquida) this.cotaGarantiaRepository.merge(cotaGarantiaCaucaoLiquida);
	}
	
	/**
	 * Método responsável por obter os dados de caucao liquida com forma de pagamento Boleto.
	 * @param idCota: ID da cota
	 * @return Data Transfer Object com os dados de caucao liquida
	 */
	@Override
	@Transactional(readOnly = true)
	public FormaCobrancaCaucaoLiquidaDTO obterDadosCaucaoLiquida(Long idCota) {
		
		CotaGarantiaCaucaoLiquida cotaGarantiaCaucaoLiquida = null;
		FormaCobrancaCaucaoLiquidaDTO formaCobrancaDTO = null;	
		
		cotaGarantiaCaucaoLiquida =  cotaGarantiaRepository.getByCota(idCota,CotaGarantiaCaucaoLiquida.class);
		FormaCobrancaCaucaoLiquida formaCobranca = null;
		PagamentoBoleto pagamentoBoleto = null;
		
		
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
			
			
			if (cotaGarantiaCaucaoLiquida.getTipoCobranca().compareTo(TipoCobranca.BOLETO)==0){
				
				pagamentoBoleto = (PagamentoBoleto) cotaGarantiaCaucaoLiquida.getFormaPagamento(); 
				
				if (pagamentoBoleto!=null){
					
					formaCobrancaDTO.setValor(pagamentoBoleto.getValor());
					formaCobrancaDTO.setQtdeParcelas(pagamentoBoleto.getQuantidadeParcelas());
					formaCobrancaDTO.setValorParcela(pagamentoBoleto.getValorParcela());
					
	    		    formaCobranca = pagamentoBoleto.getFormaCobrancaCaucaoLiquida();
				}
	    	}
			
			
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
		
		return formaCobrancaDTO;
	}
	
}
