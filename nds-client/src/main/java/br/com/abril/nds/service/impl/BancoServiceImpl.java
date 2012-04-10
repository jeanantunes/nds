package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.BancoVO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO;
import br.com.abril.nds.model.cadastro.Banco;
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
	 */
	@Transactional(readOnly=true)
	@Override
	public List<Banco> obterBancos(FiltroConsultaBancosDTO filtro) {
		return bancoRepository.obterBancos(filtro);
	}

	/**
	 * Método responsavel por obter a quantidade bancos cadastrados
	 * @param filtro: parametros de busca
	 */
	@Transactional(readOnly=true)
	@Override
	public long obterQuantidadeBancos(FiltroConsultaBancosDTO filtro) {
		return bancoRepository.obterQuantidadeBancos(filtro);
	}
	
	@Transactional(readOnly=true)
	@Override
	public Banco obterbancoPorNumero(String numero) {
		return bancoRepository.obterbancoPorNumero(numero);
	}

	@Transactional(readOnly=true)
	@Override
	public Banco obterbancoPorNome(String nome) {
		return bancoRepository.obterbancoPorNome(nome);
	}
	
	@Transactional(readOnly=true)
	@Override
	public Banco obterBancoPorId(long idBanco) {
		return bancoRepository.buscarPorId(idBanco);
	}

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
			bancoVO.setCodigoCedente(banco.getCodigoCedente());
			bancoVO.setAgencia(banco.getAgencia());
			bancoVO.setConta(banco.getConta());
			bancoVO.setDigito(banco.getDvConta());
			bancoVO.setMoeda(banco.getMoeda().name());
			bancoVO.setCarteira(banco.getCarteira().getTipoRegistroCobranca().name());
			bancoVO.setJuros(BigDecimal.ZERO);
			bancoVO.setAtivo(banco.isAtivo());
			bancoVO.setMulta(BigDecimal.ZERO);
			bancoVO.setInstrucoes(banco.getInstrucoes());
		}
		return bancoVO; 
	}
	
	@Transactional
	@Override
	public void incluirBanco(Banco banco) {
		bancoRepository.adicionar(banco);
	}
	
	@Transactional
	@Override
	public void alterarBanco(Banco banco) {
		bancoRepository.merge(banco);
	}
	
	

	
	//VERIFICAR SE BANCO POSSUI PENDENCIAS (DIVIDAS, BOLETOS...ETC)
	@Transactional(readOnly=true)
	@Override
	public boolean verificarPendencias(long idBanco) {
		return bancoRepository.verificarPedencias(idBanco);
	}
	
	

	@Transactional
	@Override
	public void dasativarBanco(long idBanco) {
		bancoRepository.desativarBanco(idBanco);
	}
	
}
