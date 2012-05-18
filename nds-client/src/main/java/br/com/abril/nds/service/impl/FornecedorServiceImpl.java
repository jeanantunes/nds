package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.TelefoneFornecedorRepository;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class FornecedorServiceImpl implements FornecedorService {

	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	@Autowired
	private TelefoneFornecedorRepository telefoneFornecedorRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Transactional
	public Fornecedor obterFornecedorUnico(String codigoProduto) {
		
		List<Fornecedor> fornecedores =  fornecedorRepository.obterFornecedoresDeProduto(codigoProduto, null);
		
		if(fornecedores == null || fornecedores.size()!=1) {
			throw new IllegalStateException("O produto não possui um único fornecedor");
		}
		
		return fornecedores.get(0);
		
	}
	
	@Transactional
	public List<Fornecedor> obterFornecedores() {
		return fornecedorRepository.obterFornecedores();
	}
	
	@Transactional
	public List<Fornecedor> obterFornecedoresAtivos() {
		return fornecedorRepository.obterFornecedoresAtivos();
	}
	
	@Transactional
	public List<Fornecedor> obterFornecedores(String cnpj) {
		return fornecedorRepository.obterFornecedores(cnpj);
	}

	@Transactional(readOnly = true)
	public List<Fornecedor> obterFornecedores(boolean permiteBalanceamento,
			SituacaoCadastro... situacoes) {
		return fornecedorRepository.obterFornecedores(permiteBalanceamento,
				situacoes);
	}
	
	@Transactional
	public List<Fornecedor> obterFornecedoresPorProduto(String codigoProduto,
														GrupoFornecedor grupoFornecedor) {
		
		return fornecedorRepository.obterFornecedoresDeProduto(codigoProduto, grupoFornecedor);
	}
	
	@Transactional
	public Fornecedor obterFornecedorPorId(Long id) {
		
		return this.fornecedorRepository.buscarPorId(id);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<TelefoneAssociacaoDTO> buscarTelefonesFornecedor(Long idFornecedor, Set<Long> idsIgnorar) {
		
		if (idFornecedor == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "IdFornecedor é obrigatório");
		}
		
		List<TelefoneAssociacaoDTO> listaTelAssoc =
				this.telefoneFornecedorRepository.buscarTelefonesFornecedor(idFornecedor, idsIgnorar);
		
		List<Telefone> listaTel = this.telefoneFornecedorRepository.buscarTelefonesPessoaPorFornecedor(idFornecedor);
		
		for (TelefoneAssociacaoDTO tDto : listaTelAssoc){
			listaTel.remove(tDto.getTelefone());
		}
		
		for (Telefone telefone : listaTel){
			TelefoneAssociacaoDTO telefoneAssociacaoDTO = new TelefoneAssociacaoDTO(false, telefone, null, null);
			listaTelAssoc.add(telefoneAssociacaoDTO);
		}
		
		return listaTelAssoc;
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Fornecedor> obterFornecedores(Long idCota){
		
		if(idCota == null){
			return fornecedorRepository.buscarTodos();
		}
		
		return fornecedorRepository.obterFornecedoresNaoReferenciadosComCota(idCota);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Fornecedor> obterFornecedoresCota(Long idCota){
		
		return fornecedorRepository.obterFornecedoresCota(idCota);
	}
	
	@Transactional
	@Override
	public void salvarFornecedorCota(List<Long> fornecedores, Long idCota){
		
		if(idCota == null ){
			throw new ValidacaoException(TipoMensagem.ERROR,"Parâmetro Cota invalido!");
		}
		
		validarIntegridadeFornecedor(fornecedores,idCota);
		
		Set<Fornecedor> listaFonecedores = new HashSet<Fornecedor>();
		
		if(fornecedores != null && !fornecedores.isEmpty()){
			
			Fornecedor fornecedor = null;
			
			for(Long  fn :  fornecedores ){
				
				fornecedor = fornecedorRepository.buscarPorId(fn) ;
				
				if(fornecedor != null){
					listaFonecedores.add( fornecedor );
				}

			}
		}

		Cota cota = cotaRepository.buscarPorId(idCota);
		cota.setFornecedores(listaFonecedores);
		
		cotaRepository.alterar(cota);

	}
	private void validarIntegridadeFornecedor(List<Long> fornecedores,Long idCota) {
		
		//TODO Implementar validação de integridade
		
		/*Cota cota  = cotaRepository.buscarPorId(idCota);
		
		if(cota.getParametroCobranca()!= null){
			
			Set<FormaCobranca> formasCobranca = cota.getParametroCobranca().getFormasCobrancaCota();
			
			if(formasCobranca!= null && !formasCobranca.isEmpty()){
				
				for(FormaCobranca forCob : formasCobranca){
				}
			}
		}*/
	}

	/**
	 * Método responsável por obter fornecedores para preencher combo da camada view
	 * @return comboFornecedores: fornecedores cadastrados
	 */
	@Transactional(readOnly=true)
	@Override
	public List<ItemDTO<Long, String>> buscarComboFornecedores() {
		List<ItemDTO<Long,String>> comboFornecedores =  new ArrayList<ItemDTO<Long,String>>();
		List<Fornecedor> fornecedores = fornecedorRepository.buscarTodos();
		for (Fornecedor itemFornecedor : fornecedores){
			comboFornecedores.add(new ItemDTO<Long,String>(itemFornecedor.getId(), itemFornecedor.getJuridica().getRazaoSocial()));
		}
		return comboFornecedores;
	}
}
