package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.EnderecoDTO;
import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.TelefoneDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFornecedorDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoFornecedor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneFornecedor;
import br.com.abril.nds.model.cadastro.desconto.DescontoProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DescontoProdutoEdicaoRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EnderecoFornecedorRepository;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.TelefoneFornecedorRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.TelefoneService;
import br.com.abril.nds.service.validation.CobrancaFornecedorValidator;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class FornecedorServiceImpl implements FornecedorService {

	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	@Autowired
	private TelefoneFornecedorRepository telefoneFornecedorRepository;
	
	@Autowired
	private CotaRepository cotaRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private EnderecoFornecedorRepository enderecoFornecedorRepository;

	@Autowired
	private DescontoProdutoEdicaoRepository descontoProdutoEdicaoRepository;
	
	@Autowired
	private DescontoService descontoService;
	
	@Autowired
	private TelefoneService telefoneService;
	
	@Autowired
	private CobrancaFornecedorValidator cobrancaFornecedorValidator;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Transactional
	public Fornecedor obterFornecedorUnico(String codigoProduto) {
		
		List<Fornecedor> fornecedores =  fornecedorRepository.obterFornecedoresDeProduto(codigoProduto, null);
		
		if(fornecedores == null || fornecedores.size()!=1) {
			throw new IllegalStateException("O produto não possui um único fornecedor");
		}
		
		return fornecedores.get(0);
		
	}
	
	@Transactional(readOnly=true)
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
	public List<Fornecedor> obterFornecedores(SituacaoCadastro... situacoes) {
		return fornecedorRepository.obterFornecedores(situacoes);
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
		
		this.validarIntegridadeFornecedor(fornecedores,idCota);
		
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

		processarDescontosFornecedorCota(cota, listaFonecedores);
	}
	
	@Transactional(readOnly = true)
	public ValidacaoVO validarFormaCobrancaFornecedoresCota(List<Long> fornecedores, Long idCota) {
		
		ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso.");
		
		Fornecedor fornecedor = null;
		
		for(Long id :  fornecedores ){
			
			try {
			
				fornecedor = this.fornecedorRepository.buscarPorId(id);
				
				this.cobrancaFornecedorValidator.filter(
					null,
					this.cotaRepository.buscarCotaPorID(idCota), 
					fornecedor, 
					null
				).validate();
			
			} catch (ValidacaoException e) {
				
				validacaoVO.setTipoMensagem(TipoMensagem.WARNING);
				validacaoVO.addMensagem(
						String.format("Não existem formas de cobrança cadastradas para o fornecedor %s.", 
									  fornecedor.getJuridica().getNomeFantasia()));
			}
		}
		
		return validacaoVO;
	}

	private void validarIntegridadeFornecedor(List<Long> fornecedores,Long idCota) {
		
		Cota cota  = cotaRepository.buscarPorId(idCota);
		
		if(cota.getParametroCobranca()!= null 
				&& cota.getFornecedores()!= null && !cota.getFornecedores().isEmpty()){
			
			Set<FormaCobranca> formasCobranca = cota.getParametroCobranca().getFormasCobrancaCota();
			
			Set<Fornecedor>fornecedoresCobranca = null;
			
			if(formasCobranca!= null && !formasCobranca.isEmpty()){
				
				for(FormaCobranca forCob : formasCobranca){
					
					if(!forCob.isAtiva()){
						continue;
					}
					
					fornecedoresCobranca = forCob.getFornecedores();
					
					if(fornecedoresCobranca!= null && !fornecedoresCobranca.isEmpty()){
						
						for(Fornecedor forn : fornecedoresCobranca){
							
							if(fornecedores == null){
								
								verificarExistenciaFornecedorCota(cota.getFornecedores(),forn);
							}
							else{
								
								verificarExistenciaFornecedorCotaPorCodigo(fornecedores, forn,cota.getFornecedores());
							}
						}
					}
				}
			}
		}
	}
	
	private void verificarExistenciaFornecedorCota(Set<Fornecedor> fornecedores, Fornecedor fornecedor){
		
		for(Fornecedor forn: fornecedores){
			
			if(forn.getId().equals(fornecedor.getId())){
			
				throw new ValidacaoException(TipoMensagem.WARNING,"Operação não permitida! Registro possui dependências!");
			}
		}
	}
	
	private void verificarExistenciaFornecedorCotaPorCodigo(List<Long> fornecedores, Fornecedor fornecedor,Set<Fornecedor> fornecedoresAssCota){
		
		for(Fornecedor fr : fornecedoresAssCota){
			
			if(!isFornecedorAssociadoCota(fornecedores, fr.getId())){
				
				if(fr.getId().equals(fornecedor.getId())){
					
					throw new ValidacaoException(TipoMensagem.WARNING,"Operação não permitida! Registro possui dependências!");
				}
			}
		}
	}
	
	private boolean isFornecedorAssociadoCota(List<Long> fornecedores,Long fornecedor){
		
		for(Long fr : fornecedores){
			
			if(fr.equals(fornecedor)){
				return true;
			}
		}
		
		return false;
	}

	private void processarDescontosFornecedorCota(Cota cota, Set<Fornecedor> fornecedores) {

		for (Fornecedor fornecedor : fornecedores) {
		
			DescontoProdutoEdicao descontoProdutoEdicao = 
				this.descontoProdutoEdicaoRepository.buscarDescontoProdutoEdicao(
					TipoDesconto.GERAL, fornecedor, null, null
				);
			
			if (descontoProdutoEdicao != null) {

				Set<Fornecedor> setFornecedores = new HashSet<Fornecedor>();

				setFornecedores.add(fornecedor);

				this.descontoService.processarDescontoCota(cota, setFornecedores, descontoProdutoEdicao.getDesconto());
			}
		}
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public List<FornecedorDTO> obterFornecedoresPorFiltro(FiltroConsultaFornecedorDTO filtroConsultaFornecedor) {

		return this.fornecedorRepository.obterFornecedoresPorFiltro(filtroConsultaFornecedor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public Long obterContagemFornecedoresPorFiltro(FiltroConsultaFornecedorDTO filtroConsultaFornecedor) {

		return this.fornecedorRepository.obterContagemFornecedoresPorFiltro(filtroConsultaFornecedor);
	}
	

	@Transactional(readOnly=true)
	@Override
	public List<ItemDTO<Long, String>> obterFornecedoresIdNome(
			SituacaoCadastro situacao, Boolean inferface) {
		return fornecedorRepository
				.obterFornecedoresIdNome(situacao, inferface);
	}

	@Override
	@Transactional
	public void removerFornecedor(Long idFornecedor) {

		if (idFornecedor == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Fornecedor inexistente."); 
		}
		
		Fornecedor fornecedor = this.fornecedorRepository.buscarPorId(idFornecedor);
		
		if (fornecedor == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Fornecedor inexistente."); 
		}
		
		try {
		
			this.fornecedorRepository.remover(fornecedor);
		
		} catch (DataIntegrityViolationException e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Este fornecedor não pode ser removido.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void salvarFornecedor(Fornecedor fornecedor) {

		if (fornecedor == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Fornecedor não pode estar nulo.");
		}
		
		this.fornecedorRepository.adicionar(fornecedor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public Fornecedor merge(Fornecedor fornecedor) {

		if (fornecedor == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Fornecedor não pode estar nulo.");
		}
		
		return this.fornecedorRepository.merge(fornecedor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void processarEnderecos(Long idFornecedor,
								   List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar,
								   List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoRemover) {

		if (idFornecedor == null){

			throw new ValidacaoException(TipoMensagem.ERROR, "Id do fornecedor é obrigatório.");
		}
		
		Fornecedor fornecedor = this.fornecedorRepository.buscarPorId(idFornecedor);
		
		if (fornecedor == null){

			throw new ValidacaoException(TipoMensagem.ERROR, "Fornecedor não encontrado.");
		}
		
		if (listaEnderecoAssociacaoSalvar != null && !listaEnderecoAssociacaoSalvar.isEmpty()) {

			this.salvarEnderecosFornecedor(fornecedor, listaEnderecoAssociacaoSalvar);
		}

		if (listaEnderecoAssociacaoRemover != null && !listaEnderecoAssociacaoRemover.isEmpty()) {
			
			this.removerEnderecosFornecedor(fornecedor, listaEnderecoAssociacaoRemover);
		}
	}

	/**
	 * ENDERECO
	 * 
	 * Retorna um Endereco à ser editado ou cadastrado
	 * @param enderecoDTO
	 * @param pessoa
	 * @param novo
	 * @return Endereco
	 */
	private Endereco obterEndereco(EnderecoDTO enderecoDTO, Pessoa pessoa, boolean novo){
		
		Endereco endereco = new Endereco();
		
		if (!novo){
			
			endereco = this.enderecoRepository.buscarPorId(enderecoDTO.getId());
		}

		endereco.setBairro(enderecoDTO.getBairro());
		endereco.setCep(enderecoDTO.getCep());
		endereco.setCodigoCidadeIBGE(enderecoDTO.getCodigoCidadeIBGE());
		endereco.setCidade(enderecoDTO.getCidade());
		endereco.setComplemento(enderecoDTO.getComplemento());
		endereco.setTipoLogradouro(enderecoDTO.getTipoLogradouro());
		endereco.setLogradouro(enderecoDTO.getLogradouro());
		endereco.setNumero(enderecoDTO.getNumero());
		endereco.setUf(enderecoDTO.getUf());
		endereco.setCodigoUf(enderecoDTO.getCodigoUf());
		endereco.setPessoa(pessoa);
		
	    return endereco;
	}
	
	/**
	 * ENDERECO
	 * 
	 * Persiste EnderecoFornecedor e Endereco
	 * @param fornecedor
	 * @param listaEnderecoAssociacao
	 */
	private void salvarEnderecosFornecedor(Fornecedor fornecedor, 
										   List<EnderecoAssociacaoDTO> listaEnderecoAssociacao) {
		
		Pessoa pessoa = fornecedor.getJuridica();
		
		for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao) {

			EnderecoDTO enderecoDTO = enderecoAssociacao.getEndereco();
			
			EnderecoFornecedor enderecoFornecedor = this.enderecoFornecedorRepository.buscarPorId(enderecoAssociacao.getId());

			Endereco endereco = null;
			
			boolean novoEnderecoFornecedor = false;
			
			if (enderecoFornecedor == null) {

				novoEnderecoFornecedor = true;
				
				enderecoFornecedor = new EnderecoFornecedor();

				enderecoFornecedor.setFornecedor(fornecedor);
			}
			
			
			boolean novoEndereco = (novoEnderecoFornecedor && !enderecoAssociacao.isEnderecoPessoa());
			
			endereco = this.obterEndereco(enderecoDTO, pessoa, novoEndereco);

			enderecoFornecedor.setEndereco(endereco);

			enderecoFornecedor.setPrincipal(enderecoAssociacao.isEnderecoPrincipal());

			enderecoFornecedor.setTipoEndereco(enderecoAssociacao.getTipoEndereco());
				
			this.enderecoFornecedorRepository.merge(enderecoFornecedor);
		}
	}

	/*
	 * Método responsável por remover os endereços referentes ao fornecedor em questão.
	 */
	private void removerEnderecosFornecedor(Fornecedor fornecedor, 
			   								List<EnderecoAssociacaoDTO> listaEnderecoAssociacao) {

		List<EnderecoDTO> listaEndereco = new ArrayList<EnderecoDTO>();
		
		List<Long> idsEndereco = new ArrayList<Long>();

		for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao) {

			if (enderecoAssociacao.getEndereco() == null) {
				
				continue;
			}
			
			listaEndereco.add(enderecoAssociacao.getEndereco());

			EnderecoFornecedor enderecoFornecedor = this.enderecoFornecedorRepository.buscarPorId(enderecoAssociacao.getId());
			
			if(enderecoFornecedor!= null){
				
				idsEndereco.add(enderecoAssociacao.getEndereco().getId());
	
				this.enderecoFornecedorRepository.remover(enderecoFornecedor);
			}
		}

		if (!idsEndereco.isEmpty()) {
			
			this.enderecoRepository.removerEnderecos(idsEndereco);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void processarTelefones(Long idFornecedor,
								   List<TelefoneAssociacaoDTO> listaTelefonesAdicionar,
								   Collection<Long> listaTelefonesRemover) {

		if (idFornecedor == null){

			throw new ValidacaoException(TipoMensagem.ERROR, "Fornecedor é obrigatório.");
		}
		
		Fornecedor fornecedor = this.fornecedorRepository.buscarPorId(idFornecedor);
		
		if (fornecedor == null){
		
			throw new ValidacaoException(TipoMensagem.ERROR, "Fornecedor não encontrado.");
		}
		
		this.salvarTelefonesFornecedor(listaTelefonesAdicionar, fornecedor);
	
		this.removerTelefonesFornecedor(listaTelefonesRemover);
	}
	
	private void salvarTelefonesFornecedor(List<TelefoneAssociacaoDTO> listaTelefoneFornecedor,
										   Fornecedor fornecedor) {
		        
		PessoaJuridica juridica = fornecedor.getJuridica();
		
		for (TelefoneAssociacaoDTO dto : listaTelefoneFornecedor) {
			
			TelefoneDTO telefoneDTO = dto.getTelefone();
			
			this.telefoneService.validarTelefone(telefoneDTO, dto.getTipoTelefone());
			
            TelefoneFornecedor telefoneFornecedor =
				this.telefoneFornecedorRepository.obterTelefoneFornecedor(
					telefoneDTO.getId(), fornecedor.getId());
			
			if (telefoneFornecedor == null){
				
				telefoneFornecedor = new TelefoneFornecedor();
				
				telefoneFornecedor.setFornecedor(fornecedor);
				telefoneFornecedor.setPrincipal(dto.isPrincipal());
				Telefone telefone = new Telefone(telefoneDTO.getId(), telefoneDTO.getNumero(), telefoneDTO.getRamal(), telefoneDTO.getDdd(), juridica);
				telefoneFornecedor.setTelefone(telefone);
				telefoneFornecedor.setTipoTelefone(dto.getTipoTelefone());
				
				this.telefoneFornecedorRepository.adicionar(telefoneFornecedor);
				
			} else {
				Telefone telefone = telefoneFornecedor.getTelefone();
				telefone.setDdd(telefoneDTO.getDdd());
				telefone.setNumero(telefoneDTO.getNumero());
				telefone.setRamal(telefoneDTO.getRamal());
				telefoneFornecedor.setPrincipal(dto.isPrincipal());
				telefoneFornecedor.setTipoTelefone(dto.getTipoTelefone());
				
				this.telefoneFornecedorRepository.alterar(telefoneFornecedor);
			}
		}
	}

	private void removerTelefonesFornecedor(Collection<Long> listaTelefonesFornecedor) {
		
		if (listaTelefonesFornecedor != null && !listaTelefonesFornecedor.isEmpty()){
			
			this.telefoneFornecedorRepository.removerTelefonesFornecedor(listaTelefonesFornecedor);
			
			this.telefoneService.removerTelefones(listaTelefonesFornecedor);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public List<EnderecoAssociacaoDTO> obterEnderecosFornecedor(Long idFornecedor) {
		
		if (idFornecedor == null) {

			throw new ValidacaoException(TipoMensagem.ERROR, "Id do Fornecedor é obrigatório");
		}
		
		return this.enderecoFornecedorRepository.obterEnderecosFornecedor(idFornecedor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public List<TelefoneAssociacaoDTO> obterTelefonesFornecedor(Long idFornecedor) {

		if (idFornecedor == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Id do Fornecedor é obrigatório");
		}
		
		return this.telefoneFornecedorRepository.buscarTelefonesFornecedor(idFornecedor, null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public boolean isPessoaJaCadastrada(Long idPessoa, Long idFornecedor) {
	
		Integer contagem = this.fornecedorRepository.obterQuantidadeFornecedoresPorIdPessoa(idPessoa, idFornecedor);
		
		return contagem > 0;
	}

	@Override
	@Transactional(readOnly=true)
	public List<Fornecedor> obterFornecedorLikeNomeFornecedor(String nomeFornecedor) {
		
		return this.fornecedorRepository.obterFornecedorLikeNomeFantasia(nomeFornecedor);
	}
	
	@Transactional
	public List<Fornecedor> obterFornecedoresPorId(List<Long> idsFornecedores) {
		return fornecedorRepository.obterFornecedoresPorId(idsFornecedores);
	}

	/**
	 * @return
	 * @see br.com.abril.nds.repository.FornecedorRepository#obterMaxCodigoInterface()
	 */
	@Override
	@Transactional(readOnly=true)
	public Integer obterMaxCodigoInterface() {
		return fornecedorRepository.obterMaxCodigoInterface();
	}
	
	@Override
	@Transactional(readOnly=true)
	public Integer obterMinCodigoInterfaceDisponivel() {
		
		return fornecedorRepository.obterMinCodigoInterfaceDisponivel();
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public Fornecedor obterFornecedorPorCodigoInterface(Integer codigoInterface) {
		
		return fornecedorRepository.obterFornecedorPorCodigoInterface(codigoInterface);
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Pessoa> obterFornecedorPorNome(String nomeFornecedor) {
		
		return fornecedorRepository.obterFornecedorPorNome(nomeFornecedor);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Pessoa> obterFornecedorPorNome(String nomeFornecedor, Integer qtdMaxResult) {
		
		return fornecedorRepository.obterFornecedorPorNome(nomeFornecedor, qtdMaxResult);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Pessoa> obterFornecedorPorNomeFantasia(String nomeFantasia) {
		
		return fornecedorRepository.obterFornecedorPorNomeFantasia(nomeFantasia);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public Fornecedor obterPorId(Long idFornecedor) {
		
		return this.fornecedorRepository.buscarPorId(idFornecedor);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Fornecedor> obterFornecedoresPorSituacaoEOrigem(SituacaoCadastro situacaoCadastro, 
																Origem origem) {
		
		return this.fornecedorRepository.obterFornecedoresPorSituacaoEOrigem(situacaoCadastro, origem);
	}

	@Override
	@Transactional(readOnly = true)
	public Origem obterOrigemCadastroFornecedor(Long idFornecedor) {
		
		if (idFornecedor == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Id Fornecedor é obrigatório.");
		}
		
		return this.fornecedorRepository.obterOrigemCadastroFornecedor(idFornecedor);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Fornecedor> obterFornecedoresDesc() {
		
		return this.fornecedorRepository.obterFornecedoresDesc();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ItemDTO<Long, String>> obterFornecedoresUnificados(){
		
		List<Fornecedor> listaFornecedor = fornecedorRepository.obterFornecedoresUnificados();
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo = new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			
			listaFornecedoresCombo.add(new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial()));
		}
		
		return listaFornecedoresCombo;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Fornecedor> obterFornecedoresNaoUnificados(){
		
		List<Fornecedor> listaFornecedor = fornecedorRepository.obterFornecedoresAtivos();
		
		List<Fornecedor> listaFornecedorUnificados = fornecedorRepository.obterFornecedoresUnificados();
		
		listaFornecedor.removeAll(listaFornecedorUnificados);
		
		return listaFornecedor;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ItemDTO<Long, String>> obterFornecedoresFcDinap(){
		
		String codigoDinap = distribuidorRepository.codigoDistribuidorDinap();
		
		String codigoFC = distribuidorRepository.codigoDistribuidorFC();
		
		List<Fornecedor> listaFornecedor = fornecedorRepository.obterFornecedoresFcDinap(codigoDinap,codigoFC);
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo = new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			
			listaFornecedoresCombo.add(new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getNomeFantasia()));
		}
		
		return listaFornecedoresCombo;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Integer obterCodigoInterfacePorID(Long idFornecedor) {
		
		return fornecedorRepository.obterCodigoInterface(idFornecedor);
	}
	
}