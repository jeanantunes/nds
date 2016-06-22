package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.BancoVO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Moeda;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.repository.BancoRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.FornecedorRepository;
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
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private FornecedorRepository fornecedorRepository;
	
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
		return bancoRepository.obterBancoPorNumero(numero);
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
		
		BancoVO bancoVO = null;
		Banco banco = bancoRepository.buscarPorId(idBanco);
		
		if (banco != null) {
			bancoVO = new BancoVO();
			bancoVO.setIdBanco(banco.getId());
			bancoVO.setNumero(banco.getNumeroBanco());
			bancoVO.setNome(banco.getNome());
			bancoVO.setApelido(banco.getApelido());
			bancoVO.setIdPessoaCedente(banco.getPessoaJuridicaCedente().getId());
			bancoVO.setCodigoCedente(banco.getCodigoCedente());
			bancoVO.setDigitoCodigoCedente(banco.getDigitoCodigoCedente());
			bancoVO.setAgencia(banco.getAgencia());
			bancoVO.setDigitoAgencia(banco.getDvAgencia());
			bancoVO.setConta(banco.getConta());
			bancoVO.setDigito(banco.getDvConta());
			bancoVO.setCarteira(banco.getCarteira()!=null?banco.getCarteira():0);
			bancoVO.setJuros(banco.getJuros());
			bancoVO.setAtivo(banco.isAtivo());
			bancoVO.setMulta(banco.getMulta());
			bancoVO.setVrMulta(banco.getVrMulta());
			bancoVO.setInstrucoes1(banco.getInstrucoes1());
			bancoVO.setInstrucoes2(banco.getInstrucoes2());
			bancoVO.setInstrucoes3(banco.getInstrucoes3());
			bancoVO.setInstrucoes4(banco.getInstrucoes4());
			bancoVO.setConvenio(banco.getConvenio());
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
	 * Método responsável por excluir banco
	 * @param idbanco
	 */
	@Transactional
	@Override
	public void excluirBanco(long idBanco) throws DataIntegrityViolationException {
		
		bancoRepository.removerPorId(idBanco);
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
	 * Método responsável por obter bancos para preencher combo da camada view
	 * @param ativo - Define se a busca retornará Bancos ativos(true), inativos(false) ou todos (null).
	 * @return comboBancos: bancos cadastrados
	 */
	@Transactional(readOnly=true)
	@Override
	public List<ItemDTO<Integer, String>> getComboBancos(Boolean ativo) {
		List<ItemDTO<Integer,String>> comboBancos =  new ArrayList<ItemDTO<Integer,String>>();
		List<Banco> bancos;
		
		if(ativo == null)
			bancos = bancoRepository.buscarTodos();
		else
			bancos = bancoRepository.obterBancosPorStatus(ativo);
		
		for (Banco itemBanco : bancos){

			String descricaoBanco = 
					 (itemBanco.getNumeroBanco() == null ? "" : itemBanco.getNumeroBanco() + "-") 
					 + (itemBanco.getApelido() == null ? "" :  itemBanco.getApelido() + " ")
					 + (itemBanco.getConta() == null ? "" : itemBanco.getConta())
					 + (itemBanco.getDvConta() == null ? "" : "-" + itemBanco.getDvConta());

			comboBancos.add(new ItemDTO<Integer,String>(itemBanco.getId().intValue(), descricaoBanco));
		}
		return comboBancos;
	}

	@Override
	@Transactional(readOnly=true)
	public Banco obterBancoPorApelido(String apelido) {
		return bancoRepository.obterbancoPorApelido(apelido);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Banco> obterBancosPorNome(String nomeBanco) {
		
		return bancoRepository.obterBancosPorNome(nomeBanco);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Banco> obterBancosPorNome(String nomeBanco, Integer qtdMaxResult) {
		
		return bancoRepository.obterBancosPorNome(nomeBanco, qtdMaxResult);
	}

	@Override
	public List<ItemDTO<Long, String>> obterPessoasDisponiveisParaCedente() {
		
		List<ItemDTO<Long, String>> pessoasCedente =  new ArrayList<ItemDTO<Long, String>>();
		
		Pessoa pessoDistribuidor = distribuidorRepository.obter().getJuridica(); 
		ItemDTO<Long, String> pessoa = new ItemDTO<Long, String>(pessoDistribuidor.getId(), pessoDistribuidor.getNome());
		pessoasCedente.add(pessoa);
		
		List<Fornecedor> fornecedores = fornecedorRepository.obterFornecedoresPorSituacaoEOrigem(SituacaoCadastro.ATIVO, null);
		for(Fornecedor f : fornecedores) {
			pessoasCedente.add(new ItemDTO<Long, String>(f.getJuridica().getId(), f.getJuridica().getNome()));
		}
		
		return pessoasCedente;
	}

}