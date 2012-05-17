package br.com.abril.nds.service.impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.CotaGarantiaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.CaucaoLiquida;
import br.com.abril.nds.model.cadastro.Cheque;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.Imovel;
import br.com.abril.nds.model.cadastro.NotaPromissoria;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaCaucaoLiquida;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaChequeCaucao;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaFiador;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaImovel;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaNotaPromissoria;
import br.com.abril.nds.repository.CotaGarantiaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.FiadorRepository;
import br.com.abril.nds.service.CotaGarantiaService;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.util.TipoMensagem;

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
		} else if (cotaGarantia instanceof CotaGarantiaChequeCaucao) {
			tipo = TipoGarantia.CHEQUE_CAUCAO;
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

		return (CotaGarantiaImovel) cotaGarantiaRepository
				.merge(cotaGarantiaImovel);
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
	
	private <T extends CotaGarantia> T prepareCotaGarantia(Long idCota,
			Class<T> type) throws InstantiationException,
			IllegalAccessException {

		T cotaGarantia = cotaGarantiaRepository.getByCota(idCota, type);

		if (cotaGarantia == null) {			
			cotaGarantiaRepository.deleteByCota(idCota);
			
			cotaGarantia = type.newInstance();
			cotaGarantia.setCota(getCota(idCota));
		}

		return cotaGarantia;
	}
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.CotaGarantiaService#salvarCaucaoLiquida(br.com.abril.nds.model.cadastro.CaucaoLiquida, java.lang.Long)
	 */
	@Transactional
	public CotaGarantiaCaucaoLiquida salvarCaucaoLiquida(List<CaucaoLiquida> listaCaucaoLiquida, Long idCota) throws ValidacaoException {
		
		CotaGarantiaCaucaoLiquida cotaGarantiaCaucaoLiquida =  (CotaGarantiaCaucaoLiquida) cotaGarantiaRepository.getByCota(idCota);
		
		if (cotaGarantiaCaucaoLiquida == null) {
		
			cotaGarantiaCaucaoLiquida = new CotaGarantiaCaucaoLiquida();
			cotaGarantiaCaucaoLiquida.setCota(getCota(idCota));
		}
		
		cotaGarantiaCaucaoLiquida.setData(Calendar.getInstance());
		
		cotaGarantiaCaucaoLiquida.setCaucaoLiquidas(listaCaucaoLiquida);
		
		return (CotaGarantiaCaucaoLiquida) this.cotaGarantiaRepository.merge(cotaGarantiaCaucaoLiquida);
	}
}
