package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.BancoVO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Carteira;
import br.com.abril.nds.model.cadastro.Moeda;
import br.com.abril.nds.repository.BancoRepository;
import br.com.abril.nds.service.BancoService;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Boleto}
 * 
 * @author Discover Technology
 */
@Service
public class BancoServiceImpl implements BancoService {
	
	@Autowired
	private BancoRepository bancoRepository;
	
	/**
	 * Método responsavel por obter bancos cadastrados
	 * @param filtro: parametros de busca
	 * @return {@link List<Banco>}
	 */
	@Transactional(readOnly=true)
	@Override
	public List<Banco> obterBancos(FiltroConsultaBancosDTO filtro) {
		return bancoRepository.obterBancos(filtro);
	}

	/**
	 * Método responsavel por obter a quantidade bancos cadastrados
	 * @param filtro: parametros de busca
	 * @return Quantidade de bancos para filtro
	 */
	@Transactional(readOnly=true)
	@Override
	public long obterQuantidadeBancos(FiltroConsultaBancosDTO filtro) {
		return bancoRepository.obterQuantidadeBancos(filtro);
	}
	
	/**
	 * Método responsável por obter banco por numero
	 * @param numero
	 * @return  {@link br.com.abril.nds.model.cadastro.Banco} 
	 */
	@Transactional(readOnly=true)
	@Override
	public Banco obterbancoPorNumero(String numero) {
		return bancoRepository.obterbancoPorNumero(numero);
	}

	/**
	 * Método responsável por obter banco por nome
	 * @param nome
	 * @return  {@link br.com.abril.nds.model.cadastro.Banco} 
	 */
	@Transactional(readOnly=true)
	@Override
	public Banco obterbancoPorNome(String nome) {
		return bancoRepository.obterbancoPorNome(nome);
	}
	
	/**
	 * Método responsável por obter banco por id
	 * @param idbanco
	 * @return  {@link br.com.abril.nds.model.cadastro.Banco} 
	 */
	@Transactional(readOnly=true)
	@Override
	public Banco obterBancoPorId(long idBanco) {
		return bancoRepository.buscarPorId(idBanco);
	}

	/**
	 * Método responsável por obter dados do banco
	 * @param idBanco
	 * @return  Value Object com os dados do banco
	 */
	@Transactional(readOnly=true)
	@Override
	public BancoVO obterDadosBanco(long idBanco) {
		BancoVO bancoVO=null;
		Banco banco = bancoRepository.buscarPorId(idBanco);
		if (banco!=null){
			bancoVO = new BancoVO();
			bancoVO.setIdBanco(banco.getId());
			bancoVO.setNumero(banco.getNumeroBanco());
			bancoVO.setNome(banco.getNome());
			bancoVO.setApelido(banco.getApelido());
			bancoVO.setCodigoCedente(banco.getCodigoCedente());
			bancoVO.setAgencia(banco.getAgencia());
			bancoVO.setConta(banco.getConta());
			bancoVO.setDigito(banco.getDvConta());
			bancoVO.setCarteira(banco.getCarteira()!=null?banco.getCarteira():0);
			bancoVO.setJuros(banco.getJuros());
			bancoVO.setAtivo(banco.isAtivo());
			bancoVO.setMulta(banco.getMulta());
			bancoVO.setVrMulta(banco.getVrMulta());
			bancoVO.setInstrucoes(banco.getInstrucoes());
		}
		return bancoVO; 
	}
	
	/**
	 * Método responsável por incluir banco
	 * @param {@link br.com.abril.nds.model.cadastro.Banco} 
	 */
	@Transactional
	@Override
	public void incluirBanco(Banco banco) {
		bancoRepository.adicionar(banco);
	}
	
	/**
	 * Método responsável por alterar banco
	 * @param {@link br.com.abril.nds.model.cadastro.Banco} 
	 */
	@Transactional
	@Override
	public void alterarBanco(Banco banco) {
		bancoRepository.merge(banco);
	}
	
	/**
	 * Método responsável por verificar pendencias do banco
	 * @param idBanco
	 * @return boolean: true, caso o banco esteja relacionado com alguma cobranca em aberto
	 */
	@Transactional(readOnly=true)
	@Override
	public boolean verificarPendencias(long idBanco) {
		return bancoRepository.verificarPedencias(idBanco);
	}
	
	/**
	 * Método responsável por desativar banco
	 * @param idbanco
	 */
	@Transactional
	@Override
	public void dasativarBanco(long idBanco) {
		bancoRepository.desativarBanco(idBanco);
	}
	
	/**
	 * Método responsável por obter carteiras para preencher combo da camada view
	 * @return comboCarteiras: carteiras cadastradas
	 */
	@Transactional(readOnly=true)
	@Override
	public List<ItemDTO<Integer, String>> getComboCarteiras() {
		List<ItemDTO<Integer,String>> comboCarteiras =  new ArrayList<ItemDTO<Integer,String>>();
		List<Carteira> carteiras = bancoRepository.obterCarteiras();
		for (Carteira itemCarteira : carteiras){
			comboCarteiras.add(new ItemDTO<Integer,String>(itemCarteira.getCodigo(),itemCarteira.getTipoRegistroCobranca().toString()));
		}
		return comboCarteiras;
	}
	
	/**
	 * Método responsável por obter moedas para preencher combo da camada view
	 * @return comboMoedas: moedas parametrizadas
	 */
	@Override
	public List<ItemDTO<Moeda, String>> getComboMoedas() {
		List<ItemDTO<Moeda,String>> comboMoedas = new ArrayList<ItemDTO<Moeda,String>>();
		for(Moeda itemMoeda: Moeda.values()){
		    comboMoedas.add(new ItemDTO<Moeda,String>(itemMoeda,itemMoeda.toString()));
		}
		return comboMoedas;
	}

	/**
	 * Método responsável por obter carteira por id.
	 * @return {@link br.com.abril.nds.model.cadastro.Carteira} 
	 */
	@Transactional(readOnly=true)
	@Override
	public Carteira obterCarteiraPorCodigo(Integer codigoCarteira) {
		return this.bancoRepository.obterCarteiraPorCodigo(codigoCarteira);
	}

	
	/**
	 * Método responsável por obter bancos para preencher combo da camada view
	 * @return comboBancos: bancos cadastrados
	 */
	@Transactional(readOnly=true)
	@Override
	public List<ItemDTO<Integer, String>> getComboBancos() {
		List<ItemDTO<Integer,String>> comboBancos =  new ArrayList<ItemDTO<Integer,String>>();
		List<Banco> bancos = bancoRepository.buscarTodos();
		for (Banco itemBanco : bancos){
			comboBancos.add(new ItemDTO<Integer,String>(itemBanco.getId().intValue(), itemBanco.getNumeroBanco()+"-"+itemBanco.getApelido()+" "+itemBanco.getConta()+"-"+itemBanco.getDvConta()));
		}
		return comboBancos;
	}
	
}
