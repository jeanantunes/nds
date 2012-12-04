package br.com.abril.nds.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.jsoup.helper.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.assembler.HistoricoTitularidadeCotaDTOAssembler;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaDTO.TipoPessoa;
import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.DistribuicaoDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.EnderecoDTO;
import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ProcuracaoImpressaoDTO;
import br.com.abril.nds.dto.RegistroCurvaABCCotaDTO;
import br.com.abril.nds.dto.ResultadoCurvaABCCotaDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.TelefoneDTO;
import br.com.abril.nds.dto.TermoAdesaoDTO;
import br.com.abril.nds.dto.TipoDescontoCotaDTO;
import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.dto.TitularidadeCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCCotaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.BaseReferenciaCota;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.Entregador;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.HistoricoNumeroCota;
import br.com.abril.nds.model.cadastro.HistoricoNumeroCotaPK;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.ParametroDistribuicaoCota;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.ParametrosCotaNotaFiscalEletronica;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ReferenciaCota;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.cadastro.desconto.DescontoProdutoEdicao;
import br.com.abril.nds.model.cadastro.pdv.CaracteristicasPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaDescontoCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaDescontoProduto;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaDistribuicao;
import br.com.abril.nds.repository.BaseReferenciaCotaRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EnderecoCotaRepository;
import br.com.abril.nds.repository.EnderecoPDVRepository;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.repository.EntregadorRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.HistoricoNumeroCotaRepository;
import br.com.abril.nds.repository.HistoricoSituacaoCotaRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.repository.PessoaFisicaRepository;
import br.com.abril.nds.repository.PessoaJuridicaRepository;
import br.com.abril.nds.repository.RankingRepository;
import br.com.abril.nds.repository.ReferenciaCotaRepository;
import br.com.abril.nds.repository.RotaRepository;
import br.com.abril.nds.repository.TelefoneCotaRepository;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DividaService;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.service.FileService;
import br.com.abril.nds.service.HistoricoTitularidadeService;
import br.com.abril.nds.service.SituacaoCotaService;
import br.com.abril.nds.service.TelefoneService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Cota}  
 * 
 * @author Discover Technology
 *
 */
@Service
public class CotaServiceImpl implements CotaService {
	
	@Autowired
	private SituacaoCotaService situacaoCotaService; 
	
	@Autowired
	private CotaRepository cotaRepository;
		
	@Autowired
	private EnderecoCotaRepository enderecoCotaRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private HistoricoSituacaoCotaRepository historicoSituacaoCotaRepository;
	
	@Autowired
	private CobrancaRepository cobrancaRepository;
	
	@Autowired
	private TelefoneCotaRepository telefoneCotaRepository;
	
	@Autowired
	private TelefoneService telefoneService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private BaseReferenciaCotaRepository baseReferenciaCotaRepository;
	
	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;
	
	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;
	
	@Autowired
	private PdvRepository pdvRepository;
	
	@Autowired
	private ReferenciaCotaRepository referenciaCotaRepository;
	
	@Autowired
	private HistoricoNumeroCotaRepository historicoNumeroCotaRepository;
	
	@Autowired
	private EnderecoService enderecoService;
	
	@Autowired
	private DividaService dividaService;
	
	@Autowired
	private EnderecoPDVRepository enderecoPDVRepository;
	
	@Autowired
	TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
	@Autowired
	EstoqueProdutoCotaRepository estoqueProdutoCotaRepository;
	
	@Autowired
	ParametroSistemaRepository parametroSistemaRepository;
	
	@Autowired
	FileService fileService;
		
	@Autowired
	private RotaRepository rotaRepository;
	
	@Autowired
	private EntregadorRepository entregadorRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private HistoricoTitularidadeService historicoTitularidadeService;
	
	@Autowired
	private RankingRepository rankingRepository;
	
	@Transactional(readOnly = true)
	@Override
	public List<CotaDTO> obterCotas(FiltroCotaDTO filtro) {

		return cotaRepository.obterCotas(filtro);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Long obterQuantidadeCotasPesquisadas(FiltroCotaDTO filtro) {

		return cotaRepository.obterQuantidadeCotasPesquisadas(filtro);
	}
	
	@Transactional(readOnly = true)
	public Cota obterPorNumeroDaCota(Integer numeroCota) {
		return this.cotaRepository.obterPorNumerDaCota(numeroCota);
	}
	
	@Transactional(readOnly = true)
	public Cota obterPorNumeroDaCotaAtiva(Integer numeroCota) {
		
		return this.cotaRepository.obterPorNumerDaCotaAtiva(numeroCota);
	}

	@Transactional(readOnly = true)
	public List<Cota> obterCotasPorNomePessoa(String nome) {
		
		return this.cotaRepository.obterCotasPorNomePessoa(nome);
	}

	@Transactional(readOnly = true)
	public Cota obterPorNome(String nome) {
		
		List<Cota> listaCotas = this.cotaRepository.obterPorNome(nome);
		
		if  (listaCotas == null || listaCotas.isEmpty()) {
			
			return null;
		}
		
		if (listaCotas.size() > 1) {
			
			throw new ValidacaoException(
				TipoMensagem.ERROR, "Mais de um resultado encontrado para a cota com nome \"" + nome + "\"");
		}
		
		return listaCotas.get(0);
	}
	
	/**
	 * @see br.com.abril.nds.service.CotaService#obterPorId(java.lang.Long)
	 */
	@Override
	@Transactional
	public Cota obterPorId(Long idCota) {

		if (idCota == null) {

			throw new ValidacaoException(TipoMensagem.ERROR, "Id da cota não pode ser nulo.");
		}
		
		return this.cotaRepository.buscarPorId(idCota);
	}
	
	/**
	 * ENDERECO
	 * 
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public EnderecoCota obterEnderecoPrincipal(long idCota) {

		return this.cotaRepository.obterEnderecoPrincipal(idCota);
	}
	
	/**
	 * ENDERECO
	 * 
	 * @see br.com.abril.nds.service.CotaService#obterEnderecosPorIdCota(java.lang.Long)
	 */
	@Transactional(readOnly = true)
	public List<EnderecoAssociacaoDTO> obterEnderecosPorIdCota(Long idCota) {
		
		Cota cota = cotaRepository.buscarPorId(idCota);
		
		if(cota == null)
			throw new ValidacaoException(TipoMensagem.ERROR, "IdCota é obrigatório");
		
		Long idPessoa = cota.getPessoa().getId();
		
		Set<Long> endRemover = new HashSet<Long>();
		
		List<EnderecoAssociacaoDTO> listRetorno = new ArrayList<EnderecoAssociacaoDTO>();
		
		List<EnderecoAssociacaoDTO> listaEnderecolAssoc = cotaRepository.obterEnderecosPorIdCota(idCota);
			
		if(listaEnderecolAssoc!= null && !listaEnderecolAssoc.isEmpty()){
			
			listRetorno.addAll(listaEnderecolAssoc);

			for (EnderecoAssociacaoDTO dto : listaEnderecolAssoc){
				
				endRemover.add(dto.getEndereco().getId());
			}
		}
		
		List<EnderecoAssociacaoDTO> lista = this.enderecoService.buscarEnderecosPorIdPessoa(idPessoa, endRemover);
		
		if (lista!= null && !lista.isEmpty()){
			
			listRetorno.addAll(lista);
		}
		
		return listRetorno;
	}	
	
	/**
	 * ENDERECO
	 * 
	 * @see br.com.abril.nds.service.CotaService#processarEnderecos(java.util.Long, java.util.List, java.util.List)
	 */
	@Override
	@Transactional
	public void processarEnderecos(Long idCota,
								   List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar,
								   List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoRemover
								   ) {

		if (idCota == null){
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Cota é obrigatório.");
		}
		
		Cota cota = this.cotaRepository.buscarPorId(idCota);
		
		if (cota == null){
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Cota não encontrada.");
		}
		
		if (listaEnderecoAssociacaoSalvar != null && !listaEnderecoAssociacaoSalvar.isEmpty()) {

			this.salvarEnderecosCota(cota, listaEnderecoAssociacaoSalvar);
		}

		if (listaEnderecoAssociacaoRemover != null && !listaEnderecoAssociacaoRemover.isEmpty()) {
			
			this.removerEnderecosCota(cota, listaEnderecoAssociacaoRemover);
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
	 * Persiste EnderecoCota e Endereco
	 * @param cota
	 * @param listaEnderecoAssociacao
	 */
	private void salvarEnderecosCota(Cota cota, List<EnderecoAssociacaoDTO> listaEnderecoAssociacao) {
		
		Pessoa pessoa = cota.getPessoa();
		
		for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao) {

			EnderecoDTO enderecoDTO = enderecoAssociacao.getEndereco();
			
			this.enderecoService.validarEndereco(enderecoDTO, enderecoAssociacao.getTipoEndereco());
			
			
			EnderecoCota enderecoCota = this.enderecoCotaRepository.buscarPorId(enderecoAssociacao.getId());
			
			Endereco endereco = null;
			
			boolean novoEnderecoCota = false;
			
			if (enderecoCota == null) {

				novoEnderecoCota = true;
				
				enderecoCota = new EnderecoCota();

				enderecoCota.setCota(cota);
			}
			
			
			boolean novoEndereco = (novoEnderecoCota && !enderecoAssociacao.isEnderecoPessoa());
			
			endereco = this.obterEndereco(enderecoDTO, pessoa, novoEndereco);

            enderecoCota.setEndereco(endereco);

			enderecoCota.setPrincipal(enderecoAssociacao.isEnderecoPrincipal());

			enderecoCota.setTipoEndereco(enderecoAssociacao.getTipoEndereco());
				
			this.enderecoCotaRepository.merge(enderecoCota);
		}
	}

	/**
	 * ENDERECO
	 * 
	 * Remove lista de EnderecoCota
	 * @param cota
	 * @param listaEnderecoAssociacao
	 */
	private void removerEnderecosCota(Cota cota,
									  List<EnderecoAssociacaoDTO> listaEnderecoAssociacao) {
		
		List<EnderecoDTO> listaEndereco = new ArrayList<EnderecoDTO>();
		
		List<Long> idsEndereco = new ArrayList<Long>();

		for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao) {
			
			if(enderecoAssociacao!= null){
				
				listaEndereco.add(enderecoAssociacao.getEndereco());

				EnderecoCota enderecoCota = this.enderecoCotaRepository.buscarPorId(enderecoAssociacao.getId());
				
				if(enderecoCota!= null){
	
					idsEndereco.add(enderecoAssociacao.getEndereco().getId());
	
					this.enderecoCotaRepository.remover(enderecoCota);
				}
			}
		}
		
		if(!idsEndereco.isEmpty()){
			
			this.enderecoService.removerEnderecos(idsEndereco);
		}
	}
	
	/**
	 * TELEFONE
	 * 
	 * Obtém telefones da Cota
	 * @param idCota
	 * @param idsIgnorar
	 * @return List<TelefoneAssociacaoDTO>
	 */
	@SuppressWarnings("unused")
	@Transactional(readOnly = true)
	@Override
	public List<TelefoneAssociacaoDTO> buscarTelefonesCota(Long idCota, Set<Long> idsIgnorar) {
		
		if (idCota == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "IdCota é obrigatório");
		}

		Cota cota = cotaRepository.buscarPorId(idCota);
		
		Long idPessoa = cota.getPessoa().getId();
		
		Set<Long> telRemover = new HashSet<Long>();
		
		List<TelefoneAssociacaoDTO> listaTelAssoc =
				this.telefoneCotaRepository.buscarTelefonesCota(idCota, idsIgnorar);
			
		for (TelefoneAssociacaoDTO dto : listaTelAssoc){
				
			telRemover.add(dto.getTelefone().getId());
		}
		
		List<TelefoneAssociacaoDTO> lista = this.telefoneService.buscarTelefonesPorIdPessoa(idPessoa, telRemover);
		
		if (lista!= null && !lista.isEmpty()){
			
			if(listaTelAssoc==null)
				listaTelAssoc = new ArrayList<TelefoneAssociacaoDTO>();
			
			listaTelAssoc.addAll(lista);
		}
		
		return listaTelAssoc;
	}
	
	/**
	 * TELEFONE
	 * 
	 * Processa Telefones para Alteracao e Remocao
	 * @param idCota
	 * @param listaTelefonesAdicionar
	 * @param listaTelefonesRemover
	 */
	@Transactional
	public void processarTelefones(Long idCota, 
			List<TelefoneAssociacaoDTO> listaTelefonesAdicionar, 
			Collection<Long> listaTelefonesRemover){
		
		if (idCota == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Cota é obrigatório.");
		}
		
		Cota cota = this.cotaRepository.buscarPorId(idCota);
		
		if (cota == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Cota não encontrada.");
		}
		
		this.salvarTelefonesCota(cota, listaTelefonesAdicionar);
		
		this.removerTelefonesCota(listaTelefonesRemover);
		
		List<Telefone> listaTelefone = new ArrayList<Telefone>();
		
		for (TelefoneAssociacaoDTO telefoneCota : listaTelefonesAdicionar){
		    TelefoneDTO dto = telefoneCota.getTelefone();
		    Telefone telefone = new Telefone(dto.getId(), dto.getNumero(), dto.getRamal(), dto.getDdd(), cota.getPessoa());
            listaTelefone.add(telefone);
		}
		
		cota.getPessoa().setTelefones(listaTelefone);
		
		this.cotaRepository.alterar(cota);
		
	}

	/**
	 * TELEFONE
	 * 
	 * Persiste Telefones
	 * @param cota
	 * @param listaTelefonesCota
	 */
	private void salvarTelefonesCota(Cota cota, List<TelefoneAssociacaoDTO> listaTelefonesCota) {
		
		Pessoa pessoa = cota.getPessoa();
        
		if (listaTelefonesCota != null){
			
			for (TelefoneAssociacaoDTO dto : listaTelefonesCota) {
				
				TelefoneCota telefoneCota = null;
				
				TelefoneDTO telefoneDTO = dto.getTelefone();
				
				this.telefoneService.validarTelefone(telefoneDTO, dto.getTipoTelefone());
				
                if(telefoneDTO!= null && telefoneDTO.getId()!= null){
					telefoneCota = cotaRepository.obterTelefonePorTelefoneCota(telefoneDTO.getId(), cota.getId());
				}
				
				if(telefoneCota == null){
					telefoneCota = new TelefoneCota();
					telefoneCota.setCota(cota);
				}
				Telefone telefone = new Telefone(telefoneDTO.getId(), telefoneDTO.getNumero(), telefoneDTO.getRamal(), telefoneDTO.getDdd(), pessoa);
				telefoneCota.setPrincipal(dto.isPrincipal());
				telefoneCota.setTelefone(telefone);
				telefoneCota.setTipoTelefone(dto.getTipoTelefone());
				
				this.telefoneCotaRepository.merge(telefoneCota);
			}
		}
	}

	/**
	 * TELEFONE
	 * 
	 * Remove Telefones
	 * @param listaTelefonesCota
	 */
	private void removerTelefonesCota(Collection<Long> listaTelefonesCota) {
		
		if (listaTelefonesCota != null && !listaTelefonesCota.isEmpty()){
			this.telefoneCotaRepository.removerTelefonesCota(listaTelefonesCota);
			
			this.telefoneService.removerTelefones(listaTelefonesCota);
		}
	}

	@Transactional
	public List<Cobranca> obterCobrancasDaCotaEmAberto(Long idCota) {	
		return cobrancaRepository.obterCobrancasDaCotaEmAberto(idCota);
	}

	@Override
	@Transactional
	public List<CotaSuspensaoDTO> suspenderCotasGetDTO(List<Long> idCotas, Long idUsuario) {

		List<Cota> cotasSuspensas =  suspenderCotas(idCotas, idUsuario, MotivoAlteracaoSituacao.INADIMPLENCIA);
		
		List<CotaSuspensaoDTO> cotasDTO = new ArrayList<CotaSuspensaoDTO>();
		
		for(Cota cota : cotasSuspensas) {
			
			Pessoa pessoa = cota.getPessoa();
			
			String nome = pessoa instanceof PessoaFisica ? 
					((PessoaFisica)pessoa).getNome() : ((PessoaJuridica)pessoa).getRazaoSocial();
			
			if(cota.getContratoCota().isExigeDocumentacaoSuspencao()) {
				
				cotasDTO.add(new CotaSuspensaoDTO(
					cota.getId(), 
					cota.getNumeroCota(),
					nome, 
					null,
					null,
					null,
					null,
					null));	
			}
		}
		
		return cotasDTO;
	}
	
	@Override
	@Transactional
	public List<Cota> suspenderCotas(List<Long> idCotas, Long idUsuario, MotivoAlteracaoSituacao motivoAlteracaoSituacao) {

		List<Cota> cotasSuspensas = new ArrayList<Cota>();
		
		Usuario usuario = usuarioRepository.buscarPorId(idUsuario);
		
		for(Long id:idCotas) {	
			
			cotasSuspensas.add(suspenderCota(id, usuario, motivoAlteracaoSituacao));
		}	
		
		return cotasSuspensas;
	}

	@Override
	@Transactional
	public Cota suspenderCota(Long idCota, Usuario usuario, MotivoAlteracaoSituacao motivoAlteracaoSituacao) {
				
		Cota cota = obterPorId(idCota);
		
		if(SituacaoCadastro.SUSPENSO.equals(cota.getSituacaoCadastro())) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "A cota já está suspensa!");
		}
		
		HistoricoSituacaoCota historico = new HistoricoSituacaoCota();
		historico.setCota(cota);
		historico.setDataEdicao(new Date());
		historico.setNovaSituacao(SituacaoCadastro.SUSPENSO);
		historico.setSituacaoAnterior(cota.getSituacaoCadastro());
		historico.setResponsavel(usuario);
		historico.setMotivo(motivoAlteracaoSituacao);
		historico.setTipoEdicao(TipoEdicao.ALTERACAO);
		
		historicoSituacaoCotaRepository.adicionar(historico);
		
		cota.setSituacaoCadastro(SituacaoCadastro.SUSPENSO);
		
		cotaRepository.alterar(cota);
		
		situacaoCotaService.removerAgendamentosAlteracaoSituacaoCota(cota.getId());
		
		return cota;
	}
	
	@Override
	@Transactional
	public List<CotaSuspensaoDTO> obterDTOCotasSujeitasSuspensao(String sortOrder, String sortColumn, Integer inicio, Integer rp) {
		
		List<CotaSuspensaoDTO> lista = cotaRepository.obterCotasSujeitasSuspensao(sortOrder,sortColumn, inicio, rp);
		
		for(CotaSuspensaoDTO dto : lista) {
			Integer perc = (int) ((dto.getDoubleDividaAcumulada() / dto.getDoubleConsignado() ) * 100);
			dto.setPercDivida(perc.toString() + "%");
			dto.setFaturamento(CurrencyUtil.formatarValor(estoqueProdutoCotaRepository.obterFaturamentoCota(dto.getIdCota())));
		}
		
		return lista;		
	}

	@Override
	@Transactional
	public Long obterTotalCotasSujeitasSuspensao() {
		return cotaRepository.obterTotalCotasSujeitasSuspensao();
	}
	
	@Override
	@Transactional
	public String obterNomeResponsavelPorNumeroDaCota(Integer numeroCota){
		
		if (numeroCota == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Número cota é obrigatório");
		}
		
		Cota cota = this.obterPorNumeroDaCota(numeroCota);
		
		if (cota != null){
			if (cota.getPessoa() instanceof PessoaFisica){
				return ((PessoaFisica) cota.getPessoa()).getNome();
			} else {
				return ((PessoaJuridica) cota.getPessoa()).getRazaoSocial();
			}
		}
		
		return null;
	}


	@Override
	@Transactional(readOnly = true)
	public List<Cota> obterCotaAssociadaFiador(Long idFiador){
		return this.cotaRepository.obterCotaAssociadaFiador(idFiador);
	}

	/**
	 * @see br.com.abril.nds.service.CotaService#obterCotaPDVPorNumeroDaCota(java.lang.Integer)
	 */
	@Override
	@Transactional
	public Cota obterCotaPDVPorNumeroDaCota(Integer numeroCota) {
		return this.cotaRepository.obterCotaPDVPorNumeroDaCota(numeroCota);
	}

	@Override
	@Transactional
	public DistribuicaoDTO obterDadosDistribuicaoCota(Long idCota) {
		
		if(idCota == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Id da cota não informado.");
		}
		
		Cota cota = cotaRepository.buscarPorId(idCota);
		
		if(cota == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota não encontrada.");
		}
		
		ParametroDistribuicaoCota parametro = cota.getParametroDistribuicao();
		
		boolean qtdePDVAutomatico = distribuidorService.obter().isPreenchimentoAutomaticoPDV();
				
		DistribuicaoDTO dto = new DistribuicaoDTO();
		
		dto.setNumCota(cota.getNumeroCota());
		
		if(cota.getBox()!= null){
			dto.setBox(cota.getBox().getNome());
		}
		
		if(qtdePDVAutomatico) {	
			dto.setQtdePDV( (cota.getPdvs()!=null) ? cota.getPdvs().size() : 0);
			dto.setQtdeAutomatica(true);
		}
		
		if (parametro == null) {
			
			return dto;
		}
		
		if(!qtdePDVAutomatico) {
			dto.setQtdePDV(parametro.getQtdePDV());
		}
		
		dto.setAssistComercial(parametro.getAssistenteComercial());
		dto.setGerenteComercial(parametro.getGerenteComercial());
		
		dto.setDescricaoTipoEntrega(parametro.getDescricaoTipoEntrega());
		
		dto.setObservacao(parametro.getObservacao());
		dto.setRepPorPontoVenda(parametro.getRepartePorPontoVenda());
		dto.setSolNumAtras(parametro.getSolicitaNumAtras());
		dto.setRecebeRecolhe(parametro.getRecebeRecolheParciais());
		dto.setNeImpresso(parametro.getNotaEnvioImpresso());
		dto.setNeEmail(parametro.getNotaEnvioEmail());
		dto.setCeImpresso(parametro.getChamadaEncalheImpresso());
		dto.setCeEmail(parametro.getChamadaEncalheEmail());
		dto.setSlipImpresso(parametro.getSlipImpresso());
		dto.setSlipEmail(parametro.getSlipEmail());
		dto.setBoletoImpresso(parametro.getBoletoImpresso());
		dto.setBoletoEmail(parametro.getBoletoEmail());
		dto.setBoletoSlipImpresso(parametro.getBoletoSlipImpresso());
		dto.setBoletoSlipEmail(parametro.getBoletoSlipEmail());
		dto.setReciboImpresso(parametro.getReciboImpresso());
		dto.setReciboEmail(parametro.getReciboEmail());
		dto.setUtilizaTermoAdesao(parametro.getUtilizaTermoAdesao());
		dto.setTermoAdesaoRecebido(parametro.getTermoAdesaoRecebido());
		dto.setUtilizaProcuracao(parametro.getUtilizaProcuracao());
		dto.setProcuracaoRecebida(parametro.getProcuracaoRecebida());
		dto.setTaxaFixa(MathUtil.round(parametro.getTaxaFixa(), 2));
		dto.setPercentualFaturamento(MathUtil.round(parametro.getPercentualFaturamento(), 2));
		dto.setInicioPeriodoCarencia(DateUtil.formatarDataPTBR(parametro.getInicioPeriodoCarencia()));
		dto.setFimPeriodoCarencia(DateUtil.formatarDataPTBR(parametro.getFimPeriodoCarencia()));
		
		return dto;
	}
	
	@Override
	@Transactional
	public void salvarDistribuicaoCota(DistribuicaoDTO dto) throws FileNotFoundException, IOException {
		
		if(dto==null || dto.getNumCota()==null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Número da Cota não deve ser nulo.");
		}
		
		Cota cota = cotaRepository.obterPorNumerDaCota(dto.getNumCota());		
		
		if( cota == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota não encontrada.");
		}
		
		ParametroDistribuicaoCota parametros = new ParametroDistribuicaoCota();
		parametros.setQtdePDV(dto.getQtdePDV());
		parametros.setAssistenteComercial(dto.getAssistComercial());
		parametros.setGerenteComercial(dto.getGerenteComercial());
		parametros.setDescricaoTipoEntrega(dto.getDescricaoTipoEntrega());
		parametros.setObservacao(dto.getObservacao());
		parametros.setRepartePorPontoVenda(dto.getRepPorPontoVenda());
		parametros.setSolicitaNumAtras(dto.getSolNumAtras());
		parametros.setRecebeRecolheParciais(dto.getRecebeRecolhe());
		parametros.setNotaEnvioImpresso(dto.getNeImpresso());
		parametros.setNotaEnvioEmail(dto.getNeEmail());
		parametros.setChamadaEncalheImpresso(dto.getCeImpresso());
		parametros.setChamadaEncalheEmail(dto.getCeEmail());
		parametros.setSlipImpresso(dto.getSlipImpresso());
		parametros.setSlipEmail(dto.getSlipEmail());
		parametros.setBoletoImpresso(dto.getBoletoImpresso());
		parametros.setBoletoEmail(dto.getBoletoEmail());
		parametros.setBoletoSlipImpresso(dto.getBoletoSlipImpresso());
		parametros.setBoletoSlipEmail(dto.getBoletoSlipEmail());
		parametros.setReciboImpresso(dto.getReciboImpresso());
		parametros.setReciboEmail(dto.getReciboEmail());
		parametros.setUtilizaTermoAdesao(dto.getUtilizaTermoAdesao());
		parametros.setTermoAdesaoRecebido(dto.getTermoAdesaoRecebido());
		parametros.setUtilizaProcuracao(dto.getUtilizaProcuracao());
		parametros.setProcuracaoRecebida(dto.getProcuracaoRecebida());
		parametros.setTaxaFixa(dto.getTaxaFixa());
		parametros.setPercentualFaturamento(dto.getPercentualFaturamento());
		
		if (dto.getInicioPeriodoCarencia() != null) {
			parametros.setInicioPeriodoCarencia(DateUtil.parseDataPTBR(dto.getInicioPeriodoCarencia()));
		} else {
			parametros.setInicioPeriodoCarencia(null);	
		}
		
		if (dto.getFimPeriodoCarencia() != null) {
			parametros.setFimPeriodoCarencia(DateUtil.parseDataPTBR(dto.getFimPeriodoCarencia()));
		} else {
			parametros.setFimPeriodoCarencia(null);			
		}
				
		cota.setParametroDistribuicao(parametros);
		
		cotaRepository.merge(cota);		

		this.atualizaTermoAdesao(
			cota.getNumeroCota().toString(), DescricaoTipoEntrega.ENTREGA_EM_BANCA);
		
		this.atualizaTermoAdesao(
			cota.getNumeroCota().toString(), DescricaoTipoEntrega.ENTREGADOR);
	}
	

	@Override
	@Transactional(readOnly = true)
	public CotaDTO obterDadosCadastraisCota(Long idCota){
		
		if (idCota == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Número da Cota não deve ser nulo.");
		}
		
		Cota cota  = cotaRepository.buscarPorId(idCota);
		
		if (cota == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota não encontrada.");
		}
		
		CotaDTO cotaDTO = new CotaDTO();
		cotaDTO.setIdCota(cota.getId());
		cotaDTO.setNumeroCota(cota.getNumeroCota());
		cotaDTO.setClassificacaoSelecionada(cota.getClassificacaoEspectativaFaturamento());
		cotaDTO.setDataInclusao(cota.getInicioAtividade());
		cotaDTO.setEmailNF((cota.getParametrosCotaNotaFiscalEletronica()!= null)
				?cota.getParametrosCotaNotaFiscalEletronica().getEmailNotaFiscalEletronica():"");
		cotaDTO.setEmiteNFE((cota.getParametrosCotaNotaFiscalEletronica()!= null)
				?cota.getParametrosCotaNotaFiscalEletronica().getEmiteNotaFiscalEletronica():false);
		cotaDTO.setStatus(cota.getSituacaoCadastro());
		
		this.atribuirDadosPessoaCota(cotaDTO, cota.getPessoa());
		this.atribuirDadosBaseReferencia(cotaDTO, cota.getBaseReferenciaCota());
		
		processarTitularidadeCota(cota, cotaDTO);
		
		return cotaDTO;
	}

    /**
     * Processa os registros de titularidade da cota para criação dos titulares
     * no DTO
     * 
     * @param cota
     *            cota com as informações de titularidade
     * @param cotaDTO
     *            DTO com as incormações da cota
     */
	private void processarTitularidadeCota(Cota cota, CotaDTO cotaDTO) {
        List<HistoricoTitularidadeCota> titulares = new ArrayList<HistoricoTitularidadeCota>();
		if (cota.getTitularesCota() != null) {
		    titulares.addAll(cota.getTitularesCota());
		}
		Collections.sort(titulares, new Comparator<HistoricoTitularidadeCota>() {

            @Override
            public int compare(HistoricoTitularidadeCota o1, HistoricoTitularidadeCota o2) {
                return o2.getFim().compareTo(o1.getFim());
            }
        });
		
        for (HistoricoTitularidadeCota historico : titulares) {
            cotaDTO.addProprietario(new TitularidadeCotaDTO(historico.getId(),
                    cota.getId(), historico.getInicio(), historico.getFim(),
                    historico.getPessoa().getNome(), historico.getPessoa()
                            .getDocumento()));
        }
    }
	
	/**
	 *  Atribui os dados da pessoa relacionada a cota ao objeto CotaDTO
	 * @param cotaDTO
	 * @param pessoa
	 */
	private void atribuirDadosPessoaCota(CotaDTO cotaDTO, Pessoa pessoa){
		
		if(pessoa == null){
			return;
		}
		
		if(pessoa instanceof PessoaJuridica){
			
			PessoaJuridica pessoaJ = (PessoaJuridica) pessoa;
			cotaDTO.setInscricaoEstadual(pessoaJ.getInscricaoEstadual());
			cotaDTO.setInscricaoMunicipal(pessoaJ.getInscricaoMunicipal());
			cotaDTO.setEmail(pessoaJ.getEmail());
			cotaDTO.setNumeroCnpj(pessoaJ.getCnpj());
			cotaDTO.setNomeFantasia(pessoaJ.getNomeFantasia());
			cotaDTO.setRazaoSocial(pessoaJ.getRazaoSocial());
			cotaDTO.setTipoPessoa(TipoPessoa.JURIDICA);
		}
		
		else if (pessoa instanceof PessoaFisica){
			
			PessoaFisica pessoaF = (PessoaFisica) pessoa;
			cotaDTO.setNomePessoa(pessoaF.getNome());
			cotaDTO.setEmail(pessoaF.getEmail());
			cotaDTO.setNumeroRG(pessoaF.getRg());
			cotaDTO.setDataNascimento(pessoaF.getDataNascimento());
			cotaDTO.setOrgaoEmissor(pessoaF.getOrgaoEmissor());
			cotaDTO.setEstadoSelecionado(pessoaF.getUfOrgaoEmissor());
			cotaDTO.setEstadoCivilSelecionado(pessoaF.getEstadoCivil());
			cotaDTO.setSexoSelecionado(pessoaF.getSexo());
			cotaDTO.setNacionalidade(pessoaF.getNacionalidade());
			cotaDTO.setNatural(pessoaF.getNatural());
			cotaDTO.setNumeroCPF(pessoaF.getCpf());
			cotaDTO.setTipoPessoa(TipoPessoa.FISICA);
		}
	}
	
	/**
	 * Atribui os dados das referencias histórico cota base ao objeto CotaDTO
	 * @param cotaDTO
	 * @param baseReferenciaCota
	 */
	private void atribuirDadosBaseReferencia(CotaDTO cotaDTO, BaseReferenciaCota baseReferenciaCota){
		
		if(baseReferenciaCota == null){
			return;
		}
		
		cotaDTO.setInicioPeriodo(baseReferenciaCota.getInicioPeriodo());
		cotaDTO.setFimPeriodo(baseReferenciaCota.getFinalPeriodo());
		
		if(baseReferenciaCota.getReferenciasCota()!= null && !baseReferenciaCota.getReferenciasCota().isEmpty()){
			
			List<ReferenciaCota> referenicasCota = new ArrayList<ReferenciaCota>();
			referenicasCota.addAll(baseReferenciaCota.getReferenciasCota());
			
			if(referenicasCota.size() > 0){
				cotaDTO.setHistoricoPrimeiraCota(referenicasCota.get(0).getCota().getNumeroCota());
				cotaDTO.setHistoricoPrimeiraPorcentagem(referenicasCota.get(0).getPercentual());
			}
			
			if(referenicasCota.size() > 1){
				cotaDTO.setHistoricoSegundaCota(referenicasCota.get(1).getCota().getNumeroCota());
				cotaDTO.setHistoricoSegundaPorcentagem(referenicasCota.get(1).getPercentual());
			}
			
			if(referenicasCota.size() > 2){
				cotaDTO.setHistoricoTerceiraCota(referenicasCota.get(2).getCota().getNumeroCota());
				cotaDTO.setHistoricoTerceiraPorcentagem(referenicasCota.get(2).getPercentual());
			}
		}
	}
	
	@Override
	@Transactional
	public void excluirCota(Long idCota){
		
		if(idCota == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota informada para exclusão inválida!");
		}
		
		if (cotaComDebitos(idCota)){
			throw new ValidacaoException(TipoMensagem.WARNING, "A [Cota] possui dívidas em aberto e não pode ser excluída!");
		}
		
		Cota cota  = cotaRepository.buscarPorId(idCota);
	
		try{	
			
			cotaRepository.remover(cota);
			
		}catch (RuntimeException e) {
			
			if( e instanceof org.springframework.dao.DataIntegrityViolationException){
				throw new ValidacaoException(TipoMensagem.ERROR,"Exclusão não permitida, registro possui dependências!");
			}
		}
	}
	
	@Override
	@Transactional(readOnly=true)
	public Integer gerarNumeroSugestaoCota(){
	
		return cotaRepository.gerarSugestaoNumeroCota();
	}
	
	@Override
	@Transactional
	public Long salvarCota(CotaDTO cotaDto){
		
		validarParametrosObrigatoriosCota(cotaDto);
		
		validarFormatoDados(cotaDto);
		
		validarHistoricoCotaBase(cotaDto);
		
		Cota cota  = null;
		
		if(cotaDto.getIdCota()!= null){
			cota = cotaRepository.buscarPorId(cotaDto.getIdCota());
		}
				
		boolean incluirPDV = false;
		//Flag indica criação de uma nova cota
		boolean newCota = false;
		if(cota == null){
			cota = new Cota();
			cota.setInicioAtividade(new Date());
			cota.setSituacaoCadastro(SituacaoCadastro.PENDENTE);
			incluirPDV = true;
			newCota = true;
		}
		
		//Flag indica a mudança de número da cota
		boolean mudancaNumero = false;
		if (!newCota) {
		    Integer numeroCota = cota.getNumeroCota();
		    Integer novoNumeroCota = cotaDto.getNumeroCota();
		    mudancaNumero = !numeroCota.equals(novoNumeroCota);
		}
		//Se é uma nova cota ou alteração de número, processa o novo número
		if (newCota || mudancaNumero) {
		    processarNovoNumeroCota(cotaDto.getNumeroCota(),cota.getId());
		}
		
	    cota.setNumeroCota(cotaDto.getNumeroCota());
	    
	    cota.setParametrosCotaNotaFiscalEletronica(getParamNFE(cota, cotaDto));
	    
	    cota.setClassificacaoEspectativaFaturamento(cotaDto.getClassificacaoSelecionada());
	    
	    cota.setPessoa(persistePessoaCota(cota, cotaDto));
	    
	    cota  = cotaRepository.merge(cota);
	    
	    BaseReferenciaCota baseReferenciaCota = processarDadosBaseReferenciaCota(cota, cotaDto);
	    
	    processarDadosReferenciaCota(baseReferenciaCota, cotaDto);
	    
	    if(incluirPDV){
	    	persisteDadosPDV(cota, cotaDto);
	    }
	   
	    return cota.getId();
	}
	
	@Transactional
	public void atualizaTermoAdesao(String numCota, DescricaoTipoEntrega descricaoTipoEntrega) throws FileNotFoundException, IOException {
		
		ParametroSistema pathDocumento = null;
		
		switch(descricaoTipoEntrega) {
		case ENTREGA_EM_BANCA:
			pathDocumento = this.parametroSistemaRepository.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_TERMO_ADESAO);								
			break;
		case ENTREGADOR:
			pathDocumento = this.parametroSistemaRepository.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_PROCURACAO);								
			break;
		default:
			return;
		}
		
		ParametroSistema raiz = 
				this.parametroSistemaRepository.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_ARQUIVOS_DISTRIBUICAO_COTA);					
		
		if(	raiz == null || raiz.getValor() == null || 
			pathDocumento == null || pathDocumento.getValor() == null) {
			
			return;
			
		}
		
		String path = (raiz.getValor() + pathDocumento.getValor() + numCota).replace("\\", "/");
		
		fileService.persistirTemporario(path);
	}

	/**
	 * Valida os dados referente histórico cota base
	 * @param cotaDto
	 */
	private void validarHistoricoCotaBase(CotaDTO cotaDto) {
		
		if(cotaDto.getInicioPeriodo() != null && cotaDto.getFimPeriodo() != null ){
			
			if(DateUtil.removerTimestamp(cotaDto.getInicioPeriodo()).compareTo(DateUtil.removerTimestamp(cotaDto.getDataInclusao()))!=0){

				throw new ValidacaoException(TipoMensagem.WARNING,"Campo [Período] referente à cota base deve ser igual ao campo [Início de Atividade]!");
			}
		}
		
		if(cotaDto.getInicioPeriodo() == null && cotaDto.getFimPeriodo() == null ){
			return;
		}
		
		if(cotaDto.getInicioPeriodo() != null && cotaDto.getFimPeriodo() == null ){
			throw new ValidacaoException(TipoMensagem.WARNING,"O preenchimento do campo [Até] referente à  cota base é obrigatório!");
		}
		
		if(cotaDto.getInicioPeriodo() == null && cotaDto.getFimPeriodo() != null ){
			throw new ValidacaoException(TipoMensagem.WARNING,"O preenchimento do campo [Período] referente à  cota base é obrigatório!");
		}
		
		if(DateUtil.isDataInicialMaiorDataFinal(cotaDto.getInicioPeriodo(), cotaDto.getFimPeriodo())){
			throw new ValidacaoException(TipoMensagem.WARNING,"O período preenchido nos campos [Período] [Até] referente à  cota base está inválido!");
		}
	}
	
	/**
	 * Valida o formato das inforamções referente ao cadastro de uma cota
	 * @param cotaDto
	 */
	private void validarFormatoDados(CotaDTO cotaDto) {
		
		if(TipoPessoa.JURIDICA.equals(cotaDto.getTipoPessoa())){
			
			CNPJValidator cnpjValidator = new CNPJValidator(true);
			try{
				cnpjValidator.assertValid(cotaDto.getNumeroCnpj());
				
			}catch(InvalidStateException e){
				throw new ValidacaoException(TipoMensagem.WARNING,"O preenchimento do campo [Número CNPJ] está inválido!");
			}
		}
		
		if(TipoPessoa.FISICA.equals(cotaDto.getTipoPessoa())){
			
			CPFValidator cpfValidator = new CPFValidator(true);
			try{
				cpfValidator.assertValid(cotaDto.getNumeroCPF());
				
			}catch(InvalidStateException e){
				throw new ValidacaoException(TipoMensagem.WARNING,"O preenchimento do campo [Número CPF] está inválido!");
			}
		}
		
		if( cotaDto.getEmail()!= null && !cotaDto.getEmail().isEmpty() && !Util.validarEmail(cotaDto.getEmail())){
			throw new ValidacaoException(TipoMensagem.WARNING,"O preenchimento do campo [E-mail] está inválido!");
		}
		
		if( cotaDto.getEmailNF()!= null && !cotaDto.getEmailNF().isEmpty() && !Util.validarEmail(cotaDto.getEmailNF())){
			throw new ValidacaoException(TipoMensagem.WARNING," O preenchimento do campo [E-mail NF-e] está inválido!");
		}
		
	}
	
	/**
	 * Persiste dados básicos de um PDV referente a nova cota
	 * @param cota
	 * @param cotaDTO
	 */
	private void persisteDadosPDV(Cota cota,CotaDTO cotaDTO) {
		
		if(cota.getPdvs() == null || cota.getPdvs().isEmpty()){
			
			PDV pdv = new PDV();
			pdv.setCaracteristicas(new CaracteristicasPDV());
			pdv.getCaracteristicas().setPontoPrincipal(Boolean.TRUE);
			pdv.setDataInclusao(new Date());
			pdv.setCota(cota);
			
			if( TipoPessoa.JURIDICA.equals(cotaDTO.getTipoPessoa())){
				
				pdv.setNome(cotaDTO.getRazaoSocial());
			}
			else {
				
				pdv.setNome(cotaDTO.getNomePessoa());
			}
			
			pdvRepository.adicionar(pdv);
		}
	}
	
	/**
	 * Retorna os parâmetros de notas fiscais eletrônicas referente a uma cota 
	 * @param cota
	 * @param cotaDto
	 * @return ParametrosCotaNotaFiscalEletronica
	 */
	private ParametrosCotaNotaFiscalEletronica getParamNFE(Cota cota, CotaDTO cotaDto){
		
		ParametrosCotaNotaFiscalEletronica paramNFE = cota.getParametrosCotaNotaFiscalEletronica(); 
	    
	    if(paramNFE == null){
	    	paramNFE = new ParametrosCotaNotaFiscalEletronica();
	    }
	    		
	    paramNFE.setEmailNotaFiscalEletronica(cotaDto.getEmailNF());
	    paramNFE.setEmiteNotaFiscalEletronica(cotaDto.isEmiteNFE());
	    
	    return paramNFE;
	}
	
	/**
	 * Validas as informações referente ao cadasto de uma nova cota.
	 * @param cotaDto
	 */
	private void validarParametrosObrigatoriosCota(CotaDTO cotaDto) {
		
		List<String> mensagensValidacao = new ArrayList<String>();
		
		if(cotaDto.getNumeroCota() == null){
			mensagensValidacao.add("O preenchimento do campo [Cota] é obrigatório!");
		}
	
		if(TipoPessoa.JURIDICA.equals(cotaDto.getTipoPessoa())){
			
			if(cotaDto.getRazaoSocial() == null || cotaDto.getRazaoSocial().isEmpty()){
				mensagensValidacao.add("O preenchimento do campo [Razão Social] é obrigatório!");
			}
			
			if(cotaDto.getNumeroCnpj() == null || cotaDto.getNumeroCnpj().trim().isEmpty()){
				mensagensValidacao.add("O preenchimento do campo [CNPJ] é obrigatório!");
	    	}
			
			if(cotaDto.getInscricaoEstadual() == null || cotaDto.getInscricaoEstadual().isEmpty() ){
				mensagensValidacao.add("O preenchimento do campo [Inscrição Estadual] é obrigatório!");
			}
		}
		
		if(TipoPessoa.FISICA.equals(cotaDto.getTipoPessoa())){
			
			if(cotaDto.getNomePessoa() == null || cotaDto.getNomePessoa().isEmpty()){
				mensagensValidacao.add("O preenchimento do campo [Nome] é obrigatório!");
			}
			
			if(cotaDto.getNumeroCPF() == null || cotaDto.getNumeroCPF().isEmpty()){
				mensagensValidacao.add("O preenchimento do campo [CPF] é obrigatório!");
			}
			
			if(cotaDto.getNumeroRG() == null || cotaDto.getNumeroRG().isEmpty()){
				mensagensValidacao.add("O preenchimento do campo [R. G.] é obrigatório!");
			}
			
			if(cotaDto.getDataNascimento() == null){
				mensagensValidacao.add("O preenchimento do campo [Data Nascimento] é obrigatório!");
			}
			
			if(cotaDto.getOrgaoEmissor() == null ||  cotaDto.getOrgaoEmissor().isEmpty()){
				mensagensValidacao.add("O preenchimento do campo [Orgão Emissor] é obrigatório!");
			}
			
			if(cotaDto.getEstadoSelecionado() == null || cotaDto.getEstadoSelecionado().isEmpty()){
				mensagensValidacao.add("O preenchimento do campo [UF] é obrigatório!");
			}
			
			if(cotaDto.getEstadoCivilSelecionado() == null){
				mensagensValidacao.add("O preenchimento do campo [Estado Civil] é obrigatório!");
			}
		}
		
		if(cotaDto.isEmiteNFE()){
			if((cotaDto.getEmailNF() == null || cotaDto.getEmailNF().isEmpty())){
				mensagensValidacao.add("O preenchimento do campo [E-mail NF-e] é obrigatório!");
			}
		}
				
		if (!mensagensValidacao.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagensValidacao));
		}
	}
	
	/**
	 * Persiste e retorna os dados da pessoa referente a cota Física ou Jurídica
	 * @param cota
	 * @param cotaDto
	 * @return Pessoa
	 */
	private Pessoa persistePessoaCota(Cota cota, CotaDTO cotaDto){
		
		Pessoa pessoa = null;
		
		if (cotaDto.isAlteracaoTitularidade()) {
			
			pessoa = getPessoa(cotaDto, null) ;
		
		} else {
		
			pessoa = getPessoa(cotaDto, cota.getPessoa()) ;
		}
	    
	    pessoa.setEmail(cotaDto.getEmail());
	    
    	if ( pessoa instanceof  PessoaJuridica  ){
    		
    		((PessoaJuridica) pessoa).setCnpj(cotaDto.getNumeroCnpj().replace(".", "").replace("-", "").replace("/", "").trim());
    		((PessoaJuridica) pessoa).setInscricaoEstadual(cotaDto.getInscricaoEstadual());
    		((PessoaJuridica) pessoa).setInscricaoMunicipal(cotaDto.getInscricaoMunicipal());
    		((PessoaJuridica) pessoa).setNomeFantasia(cotaDto.getNomeFantasia());
    		((PessoaJuridica) pessoa).setRazaoSocial(cotaDto.getRazaoSocial());
    		
    		pessoa = pessoaJuridicaRepository.merge(((PessoaJuridica) pessoa));
    	}
    	else if (pessoa instanceof  PessoaFisica){
    		
    		((PessoaFisica) pessoa).setNome(cotaDto.getNomePessoa());
    		((PessoaFisica) pessoa).setEmail(cotaDto.getEmail());
    		((PessoaFisica) pessoa).setRg(cotaDto.getNumeroRG());
    		((PessoaFisica) pessoa).setDataNascimento(cotaDto.getDataNascimento());
    		((PessoaFisica) pessoa).setOrgaoEmissor(cotaDto.getOrgaoEmissor());
    		((PessoaFisica) pessoa).setUfOrgaoEmissor(cotaDto.getEstadoSelecionado());
    		((PessoaFisica) pessoa).setEstadoCivil(cotaDto.getEstadoCivilSelecionado());
    		((PessoaFisica) pessoa).setSexo(cotaDto.getSexoSelecionado());
    		((PessoaFisica) pessoa).setNacionalidade(cotaDto.getNacionalidade());
    		((PessoaFisica) pessoa).setNatural(cotaDto.getNatural());
    		((PessoaFisica) pessoa).setCpf(cotaDto.getNumeroCPF().replace(".", "").replace("-", "").trim());
    		
    		pessoa = pessoaFisicaRepository.merge(((PessoaFisica) pessoa));
    	}
	    
    	return pessoa;
	}
	
	/**
	 * Retona uma instância de Pessoa Jurídica ou Física
	 * @param cotaDTO
	 * @param pessoa
	 * @return Pessoa
	 */
	private Pessoa getPessoa(CotaDTO cotaDTO, Pessoa pessoa){
		
		if(pessoa == null){
	    	
	    	if( TipoPessoa.JURIDICA.equals(cotaDTO.getTipoPessoa())){
	    		
	    		pessoa  = pessoaJuridicaRepository.buscarPorCnpj(cotaDTO.getNumeroCnpj().replace(".", "").replace("-", "").replace("/", "").trim());
	    		
	    		if(pessoa == null){
	    			pessoa = new PessoaJuridica();
	    		}
	    	}
	    	else if (TipoPessoa.FISICA.equals(cotaDTO.getTipoPessoa())) {
	    		
	    		pessoa  = pessoaFisicaRepository.buscarPorCpf(cotaDTO.getNumeroCPF().replace(".", "").replace("-", "").trim());
	    		
	    		if(pessoa == null){
	    			pessoa = new PessoaFisica();
	    		}
	    	}
	    }
		return pessoa;
	}
	
	/**
	 * Persiste e retorna os dados referente a entidade BaseReferenciaCota.
	 * @param cota
	 * @param cotaDto
	 * @return BaseReferenciaCota
	 */
	private BaseReferenciaCota processarDadosBaseReferenciaCota(Cota cota , CotaDTO cotaDto){
		
		BaseReferenciaCota baseReferenciaCota  = cota.getBaseReferenciaCota();
		
		if(cotaDto.getInicioPeriodo() == null && cotaDto.getFimPeriodo() == null ){
			
			if(baseReferenciaCota!= null){
				baseReferenciaCotaRepository.remover(baseReferenciaCota);
				return null;
			}
		}
		
		if(cotaDto.getInicioPeriodo() != null && cotaDto.getFimPeriodo() != null ){
			
			if (baseReferenciaCota == null ){
				baseReferenciaCota = new BaseReferenciaCota();    
			}
			
			baseReferenciaCota.setInicioPeriodo(cotaDto.getInicioPeriodo());
		    baseReferenciaCota.setFinalPeriodo(cotaDto.getFimPeriodo());
		    baseReferenciaCota.setCota(cota);
		    
		    baseReferenciaCota = baseReferenciaCotaRepository.merge(baseReferenciaCota);
		}
		
	    return  baseReferenciaCota;
		
	}
	
	/**
	 * Persiste um conjunto de dados referente a entidade ReferenciaCota, associados a entidade BaseReferenciaCota
	 * @param baseReferenciaCota
	 * @param cotaDto
	 */
	private void processarDadosReferenciaCota(BaseReferenciaCota baseReferenciaCota, CotaDTO cotaDto){
		
		if(baseReferenciaCota == null){
			
			validarParametrosBaseReferenciaCota(cotaDto);
		}
		else{
			
			validarCotaBaseIgual(cotaDto);
			
			validarPorcentagemCotaBase(cotaDto);
			
			Set<ReferenciaCota> referenciasCota = baseReferenciaCota.getReferenciasCota();
			
			if(referenciasCota != null && !referenciasCota.isEmpty()){
		    	
				referenciaCotaRepository.excluirReferenciaCota(baseReferenciaCota.getId());
		    }
			
			referenciasCota = getReferenciasCota(baseReferenciaCota, cotaDto);
			
			if(!referenciasCota.isEmpty()){
	
				for(ReferenciaCota ref : referenciasCota){
		    		referenciaCotaRepository.merge(ref);
		    	}
			}
		}
	}
	
	/**
	 * Valida os parâmetros referente ao objeto BaseReferenciaCota
	 * @param cotaDto
	 */
	private void validarParametrosBaseReferenciaCota(CotaDTO cotaDto){
		
		if(tratarValorReferenciaCota(cotaDto.getHistoricoPrimeiraCota(),cotaDto.getHistoricoPrimeiraPorcentagem())){
			throw new ValidacaoException(TipoMensagem.WARNING,"O preenchimento dos campos [Período] [Até] referente à  cota base é obrigatório!");
		}
		
		if(tratarValorReferenciaCota(cotaDto.getHistoricoSegundaCota(),cotaDto.getHistoricoSegundaPorcentagem())){
			throw new ValidacaoException(TipoMensagem.WARNING,"O preenchimento dos campos [Período] [Até] referente à  cota base é obrigatório!");
		}
		
		if(tratarValorReferenciaCota(cotaDto.getHistoricoTerceiraCota(),cotaDto.getHistoricoTerceiraPorcentagem())){
			throw new ValidacaoException(TipoMensagem.WARNING,"O preenchimento dos campos [Período] [Até] referente à  cota base é obrigatório!");
		}
	}
	
	/**
	 * Retorna as Refencias de uma base referencia cota associada a uma cota
	 * @param baseReferenciaCota
	 * @param cotaDto
	 * @return Set<ReferenciaCota>
	 */
	private Set<ReferenciaCota> getReferenciasCota(BaseReferenciaCota baseReferenciaCota, CotaDTO cotaDto){
		
		Set<ReferenciaCota> referenciasCota = new HashSet<ReferenciaCota>();
	    
	    if(tratarValorReferenciaCota(cotaDto.getHistoricoPrimeiraCota(), cotaDto.getHistoricoPrimeiraPorcentagem())){
			
	    	referenciasCota.add( getReferencaiCota(cotaDto.getHistoricoPrimeiraCota(),
					   							   cotaDto.getHistoricoPrimeiraPorcentagem(),
					   							   baseReferenciaCota) );
		}
		
		if(tratarValorReferenciaCota(cotaDto.getHistoricoSegundaCota(), cotaDto.getHistoricoSegundaPorcentagem())){
			
			referenciasCota.add( getReferencaiCota(cotaDto.getHistoricoSegundaCota(),
					   							   cotaDto.getHistoricoSegundaPorcentagem(),
					   							   baseReferenciaCota) );
		}
		
		if(tratarValorReferenciaCota(cotaDto.getHistoricoTerceiraCota(), cotaDto.getHistoricoTerceiraPorcentagem())){
			
			referenciasCota.add( getReferencaiCota(cotaDto.getHistoricoTerceiraCota(),
												   cotaDto.getHistoricoTerceiraPorcentagem(),
												   baseReferenciaCota) );

		}
		
		return referenciasCota;
	}
	
	/**
	 * Retorna um objeto do tipo ReferenciaCota
	 * @param numeroCota - número da cota
	 * @param porcentagem - porcentagem
	 * @param baseReferenciaCota - objeto BaseReferenciaCota
	 * @return ReferenciaCota
	 */
	private ReferenciaCota getReferencaiCota(Integer numeroCota,BigDecimal porcentagem, BaseReferenciaCota baseReferenciaCota){
		
		Cota cota = null;
		
		if(numeroCota != null){
			cota = cotaRepository.obterPorNumerDaCota(numeroCota);
		}
		
		ReferenciaCota referenciaCota = new ReferenciaCota();
		referenciaCota.setCota(cota);
		referenciaCota.setPercentual(porcentagem);
		referenciaCota.setBaseReferenciaCota(baseReferenciaCota);
		
		return referenciaCota;
	}
	
	/**
	 * Valida se os números de cota informado para histórico cota base são iguais
	 * @param cotaDTO
	 */
	private void validarCotaBaseIgual(CotaDTO cotaDTO){
		
		validarCotaIguais(cotaDTO.getHistoricoPrimeiraCota(), cotaDTO.getHistoricoSegundaCota(), cotaDTO.getHistoricoTerceiraCota());
		
		validarCotaIguais(cotaDTO.getHistoricoSegundaCota(),cotaDTO.getHistoricoPrimeiraCota(), cotaDTO.getHistoricoTerceiraCota());
		
		validarCotaIguais(cotaDTO.getHistoricoTerceiraCota(),cotaDTO.getHistoricoSegundaCota(),cotaDTO.getHistoricoPrimeiraCota());
	}
	
	/**
	 * Verifica números de cotas iguais
	 * @param param
	 * @param param2
	 * @param param3
	 */
	private void validarCotaIguais(Integer param,Integer param2, Integer param3){
		
		if(param!= null){
			if(param.equals(param2)|| param.equals(param3)){
				throw new ValidacaoException(TipoMensagem.WARNING,"A cota " + param + "está incorreta! A cota " + param + " está duplicada!");
			}
		}
	}
	
	/**
	 * Valida se a porcentagem informada nas cotas histórico base obedecem 100 porcento.
	 * @param cotaDto
	 */
	private void validarPorcentagemCotaBase(CotaDTO cotaDto) {
		
		boolean existeValor = false;
		BigDecimal valor  = BigDecimal.ZERO;
		
		if(tratarValorReferenciaCota(cotaDto.getHistoricoPrimeiraCota(),
				cotaDto.getHistoricoPrimeiraPorcentagem())){
			
			valor = valor.add(cotaDto.getHistoricoPrimeiraPorcentagem());
			existeValor = true;
		}
		
		if(tratarValorReferenciaCota(cotaDto.getHistoricoSegundaCota(),
				cotaDto.getHistoricoSegundaPorcentagem())){
			
			valor = valor.add(cotaDto.getHistoricoSegundaPorcentagem());
			existeValor = true;
		}
		
		if(tratarValorReferenciaCota(cotaDto.getHistoricoTerceiraCota(),
				cotaDto.getHistoricoTerceiraPorcentagem())){
			
			valor = valor.add(cotaDto.getHistoricoTerceiraPorcentagem());
			existeValor = true;
		}
		
		if(existeValor && (valor.intValue() != 100)){
			throw new ValidacaoException(TipoMensagem.WARNING,"Porcentagem histórico cota base inválido! A porcentagem do histórico cota base deve ser 100%  ");
		}
		
	}

	private boolean tratarValorReferenciaCota(Integer numeroCota, BigDecimal porcentagem){
		return (numeroCota != null && porcentagem != null);
	}
	
	/**
	 * Verifica se a cota possui dividas em aberto
	 * @param idCota
	 * @return boolean
	 */
	private boolean cotaComDebitos(Long idCota){
		
        BigDecimal dividasEmAberto = dividaService.obterTotalDividasAbertoCota(idCota);
		
		return (dividasEmAberto!=null && dividasEmAberto.floatValue() > 0);
	}
	
	/**
	 * Verifica se o número da cota existente pode ser utilizado por uma nova cota
	 * 
	 * @param numeroCota - número da nova cota
	 */
	private void processarNovoNumeroCota(Integer numeroCota, Long idCota){
		
		Cota cota  = cotaRepository.obterPorNumerDaCota(numeroCota);
		
		if(cota!= null){
			
			if(SituacaoCadastro.INATIVO.equals(cota.getSituacaoCadastro())){
				
				if(!isParametroDistribuidoNumeroCotaValido(numeroCota)){

					throw new ValidacaoException(TipoMensagem.WARNING,"Número da cota está inativo mas não pode ser utilizado.");
				}
				else{
				   //Alterar Numero Cota e registra histoico
					alteraNumeroCota(cota);
				}
			}
			else{
				//Verifica se é edicao da cota
				if(!cota.getId().equals(idCota)){
					throw new ValidacaoException(TipoMensagem.WARNING,"Número da cota não pode ser utilizado.");
				}
			}	
			
			//Verifica se cota possui dívidas em aberto
			BigDecimal dividasEmAberto = dividaService.obterTotalDividasAbertoCota(cota.getId());
	        if (dividasEmAberto!=null && dividasEmAberto.floatValue() > 0){
	        	throw new ValidacaoException(TipoMensagem.WARNING, "O [Número] pertence à uma [Cota] que possui dívidas em aberto e não pode ser utilizado!");
	        }
		}
	}
	
	/**
	 * Altera o número da cota e gera um histporico com o número antigo
	 * @param cota - objeto que sofrera a alteração de número de cota
	 */
	private void alteraNumeroCota(Cota cota){
		
		Integer novoNumeroCota = getNovoNumeroCota(cota.getNumeroCota(), null,1);
		
		Integer numeroCotaAntigo = cota.getNumeroCota();
		
		cota.setNumeroCota(novoNumeroCota);
		cotaRepository.merge(cota);
		
		HistoricoNumeroCotaPK pk = new HistoricoNumeroCotaPK();
		pk.setDataAlteracao(new Date());
		pk.setIdCota(cota.getId());
		

		HistoricoNumeroCota historicoNumeroCota = new HistoricoNumeroCota();
		historicoNumeroCota.setNumeroCota(numeroCotaAntigo);
		historicoNumeroCota.setPk(pk);
		
		historicoNumeroCotaRepository.merge(historicoNumeroCota);
		
	}
	
	/**
	 * Retorna um número de sugestão para o cadastro de uma nova cota
	 * @param numeroCota
	 * @param novoNumeroCota
	 * @param numero
	 * @return Integer
	 */
	private Integer getNovoNumeroCota(Integer numeroCota, Integer novoNumeroCota ,Integer numero){
		
		Cota cota  = cotaRepository.obterPorNumerDaCota( (novoNumeroCota == null) ?numeroCota :novoNumeroCota);
		
		if(cota != null){
			novoNumeroCota = numero * 10000 + numeroCota;
			return getNovoNumeroCota(numeroCota,novoNumeroCota, ++numero);
		}
		
		return novoNumeroCota;
	}
	
	/**
	 * Verifica se o numero da cota informado pode ser reaproveitado por outra cota em um novo cadastro
	 * 
	 * @param numeroCota
	 * 
	 * @return boolean
	 */
	private boolean isParametroDistribuidoNumeroCotaValido(Integer  numeroCota){
		
		HistoricoSituacaoCota histCota  = historicoSituacaoCotaRepository.obterUltimoHistoricoInativo(numeroCota);
		
		if(histCota == null){
			return true;
		}
		
	    Distribuidor distribuidor = distribuidorService.obter();
	    
	    Long qntDiasInativo =  DateUtil.obterDiferencaDias(histCota.getDataInicioValidade(), new Date());
	    
	    Long qntDiasDistribuidor =  (distribuidor.getQntDiasReutilizacaoCodigoCota() == null)?0:distribuidor.getQntDiasReutilizacaoCodigoCota();
	    
		return (qntDiasInativo > qntDiasDistribuidor );
	}

	/**
	 * Método responsável por obter tipos de cota para preencher combo da camada view
	 * @return comboTiposCota: Tipos de cota padrão.
	 */
	@Override
	public List<ItemDTO<TipoCota, String>> getComboTiposCota() {
		List<ItemDTO<TipoCota,String>> comboTiposCota =  new ArrayList<ItemDTO<TipoCota,String>>();
		for (TipoCota itemTipoCota: TipoCota.values()){
			comboTiposCota.add(new ItemDTO<TipoCota,String>(itemTipoCota, itemTipoCota.getDescTipoCota()));
		}
		return comboTiposCota;
	}
		
	@Transactional
	@Override
	public void alterarCota(Cota cota) {
		this.cotaRepository.alterar(cota);
	}

	@Override
	@Transactional(readOnly = true)
	public ResultadoCurvaABCCotaDTO obterCurvaABCCotaTotal(FiltroCurvaABCCotaDTO filtroCurvaABCCotaDTO) {
		return cotaRepository.obterCurvaABCCotaTotal(filtroCurvaABCCotaDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RegistroCurvaABCCotaDTO> obterCurvaABCCota(FiltroCurvaABCCotaDTO filtroCurvaABCCotaDTO) {
		
		List<RegistroCurvaABCCotaDTO> lista = cotaRepository.obterCurvaABCCota(filtroCurvaABCCotaDTO);
		
		if(!lista.isEmpty()){
			
			for(RegistroCurvaABCCotaDTO dto : lista){
				dto.setRkProduto(rankingRepository.obterRankingProdutoCota(dto.getIdCota(),dto.getIdProduto()));
			}
		}
		
		return lista;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.CotaService#obterCotasEntre(br.com.abril.nds.util.Intervalo, br.com.abril.nds.util.Intervalo, br.com.abril.nds.model.cadastro.SituacaoCadastro)
	 */
	@Override
	@Transactional
	public List<Cota> obterCotasEntre(Intervalo<Integer> intervaloCota,
			Intervalo<Integer> intervaloBox, SituacaoCadastro situacao) {
		
		List<Cota> listaCotas = new ArrayList<Cota>();
		List<SituacaoCadastro> situacoesCadastro = new ArrayList<SituacaoCadastro>();
		situacoesCadastro.add(situacao);
		
		Set<Long> idCotas = this.cotaRepository.obterIdCotasEntre(intervaloCota, intervaloBox, situacoesCadastro, null, null, null, null, null, null);
		
		for(Long idCota : idCotas ) {
			
			Cota cota = this.cotaRepository.buscarPorId(idCota);
			
			if (cota != null) 
				listaCotas.add(cota);
		}
		
		return listaCotas;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public CotaDTO criarCotaTitularidade(CotaDTO cotaDTO) {

		Cota cotaAntiga = this.cotaRepository.buscarPorId(cotaDTO.getIdCota());

		HistoricoTitularidadeCota historicoCota = this.historicoTitularidadeService.gerarHistoricoTitularidadeCota(cotaAntiga);
		
		List<PDV> pdvs = cotaAntiga.getPdvs();
		Set<Fornecedor> fornecedores = cotaAntiga.getFornecedores();
		Set<DescontoProdutoEdicao> descontosProdutoEdicao = cotaAntiga.getDescontosProdutoEdicao();
		ParametroCobrancaCota parametrosCobrancaCota = cotaAntiga.getParametroCobranca();
		ParametroDistribuicaoCota parametroDistribuicaoCota = cotaAntiga.getParametroDistribuicao();

		Set<HistoricoTitularidadeCota> titularesCota = cotaAntiga.getTitularesCota();
		titularesCota.add(historicoCota);

		Long idCotaNova = this.salvarCota(cotaDTO);

		Cota cotaNova = this.cotaRepository.buscarPorId(idCotaNova);
		cotaNova.setInicioTitularidade(new Date());
		cotaNova.setPdvs(pdvs);
		cotaNova.setFornecedores(fornecedores);
		cotaNova.setDescontosProdutoEdicao(descontosProdutoEdicao);
		cotaNova.setParametroCobranca(parametrosCobrancaCota);
		cotaNova.setParametroDistribuicao(parametroDistribuicaoCota);
		cotaNova.setTitularesCota(titularesCota);

		this.cotaRepository.merge(cotaNova);
		processarTitularidadeCota(cotaAntiga, cotaDTO);
		
		return cotaDTO;
	}
	
	@Override
	@Transactional
	public byte[] getDocumentoProcuracao(Integer numeroCota) throws Exception {

		Cota cota = this.cotaRepository.obterPorNumerDaCota(numeroCota);
		
		ProcuracaoImpressaoDTO dto = new ProcuracaoImpressaoDTO();
		
		if (cota.getPessoa() instanceof PessoaFisica){
			
			PessoaFisica pessoa = (PessoaFisica) cota.getPessoa();
			
			dto.setNomeJornaleiro(pessoa.getNome());
			dto.setNacionalidade(pessoa.getNacionalidade());
			dto.setEstadoCivil(pessoa.getEstadoCivil() == null ? "" : pessoa.getEstadoCivil().getDescricao());
			dto.setRgJornaleiro(pessoa.getRg());
			dto.setCpfJornaleiro(pessoa.getCpf());
		}
		
		dto.setBoxCota(cota.getBox().getNome());
		
		EnderecoCota enderecoDoProcurado = this.enderecoCotaRepository.getPrincipal(cota.getId());
		
		if (enderecoDoProcurado != null){
			
			dto.setEnderecoDoProcurado(enderecoDoProcurado.getEndereco().getLogradouro());
			dto.setBairroProcurado(enderecoDoProcurado.getEndereco().getBairro());
		}
		
		PDV pdv = this.pdvRepository.obterPDVPrincipal(cota.getId());
		
		if (pdv != null){
			
			dto.setNumeroPermissao(pdv.getLicencaMunicipal() != null ? 
					pdv.getLicencaMunicipal().getNumeroLicenca() : "");
			
			Endereco enderecoPDV = this.enderecoPDVRepository.buscarEnderecoPrincipal(pdv.getId());
			
			if (enderecoPDV != null){
				
				dto.setEnderecoPDV(enderecoPDV.getLogradouro());
				dto.setCidadePDV(enderecoPDV.getCidade());
			}
			
			Rota rota = this.rotaRepository.obterRotaPorPDV(pdv.getId(), cota.getId());
			
			if (rota != null){
				
				Entregador entregador = this.entregadorRepository.obterEntregadorPorRota(rota.getId());
				
				if (entregador != null){
					
					if (entregador.getPessoa() instanceof PessoaFisica){
						
						PessoaFisica pessoaFisica = (PessoaFisica) entregador.getPessoa();
						
						dto.setEstadoCivilProcurador(pessoaFisica.getEstadoCivil() == null ? "" : pessoaFisica.getEstadoCivil().getDescricao());
						dto.setNacionalidadeProcurador(pessoaFisica.getNacionalidade());
						dto.setNomeProcurador(pessoaFisica.getNome());
						dto.setRgProcurador(pessoaFisica.getRg());
					}
				}
			}
		}
		
		List<ProcuracaoImpressaoDTO> listaDTO = new ArrayList<ProcuracaoImpressaoDTO>();
		listaDTO.add(dto);
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("cidade", dto.getCidadePDV());
		
		parameters.put("infoComp", this.distribuidorRepository.obterInformacoesComplementaresProcuracao());
		
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(listaDTO);
		
		URL url = 
			Thread.currentThread().getContextClassLoader().getResource("/reports/procuracao_subreport1.jasper");
		
		String path = url.toURI().getPath();
		 
		return JasperRunManager.runReportToPdf(path, parameters, jrDataSource);
	}
	
	@Override
	@Transactional(readOnly = true)
	public byte[] getDocumentoTermoAdesao(Integer numeroCota, BigDecimal valorDebito, BigDecimal percentualDebito) throws Exception {
		
		TermoAdesaoDTO dto = new TermoAdesaoDTO();
		dto.setNumeroCota(numeroCota);
		
		Cota cota = this.cotaRepository.obterPorNumerDaCota(numeroCota);
		
		dto.setNomeCota(cota.getPessoa().getNome());
		dto.setNomeDistribuidor(this.distribuidorRepository.obterRazaoSocialDistribuidor());
		dto.setValorDebito(valorDebito);
		dto.setPorcentagemDebito(percentualDebito);
		
		EnderecoCota enderecoCota = 
				this.enderecoCotaRepository.obterEnderecoPorTipoEndereco(
						cota.getId(), TipoEndereco.LOCAL_ENTREGA);
		
		if (enderecoCota != null){
			
			dto.setLogradouroEntrega(enderecoCota.getEndereco().getLogradouro() + ", nº " + enderecoCota.getEndereco().getNumero());
			dto.setBairroEntrega(enderecoCota.getEndereco().getBairro());
			dto.setCEPEntrega(enderecoCota.getEndereco().getCep());
			dto.setCidadeEntrega(enderecoCota.getEndereco().getCidade());
		} else {
			
			PDV pdv = this.pdvRepository.obterPDVPrincipal(cota.getId());
			
			if (pdv != null){
				
				dto.setReferenciaEndereco(pdv.getPontoReferencia());
				dto.setHorariosFuncionamento(pdv.getPeriodos());
				
				Endereco enderecoPDV = this.enderecoPDVRepository.buscarEnderecoPrincipal(pdv.getId());
				
				if (enderecoPDV != null){
					
					dto.setLogradouroEntrega(enderecoPDV.getLogradouro() + ", nº " + enderecoPDV.getNumero());
					dto.setBairroEntrega(enderecoPDV.getBairro());
					dto.setCEPEntrega(enderecoPDV.getCep());
					dto.setCidadeEntrega(enderecoPDV.getCidade());
				}
			}
		}
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("SUBREPORT_DIR",
				Thread.currentThread().getContextClassLoader().getResource("/reports/").getPath());
		
		parameters.put("infoComp", this.distribuidorRepository.obterInformacoesComplementaresTermoAdesao());
		
		List<TermoAdesaoDTO> listaDTO = new ArrayList<TermoAdesaoDTO>();
		listaDTO.add(dto);
		
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(listaDTO);
		
		URL url = 
			Thread.currentThread().getContextClassLoader().getResource("/reports/termo_adesao.jasper");
		
		String path = url.toURI().getPath();
		 
		return JasperRunManager.runReportToPdf(path, parameters, jrDataSource);
	}
	
	@Transactional(readOnly = true)
	public DistribuicaoDTO carregarValoresEntregaBanca(Integer numCota) {
		
		DistribuicaoDTO dto = new DistribuicaoDTO();
		
		Cota cota = cotaRepository.obterPorNumerDaCota(numCota);
		
		if (cota == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Cota não encontrada!");
		}
		
		this.obterPercentualFaturamentoTaxaFixa(cota.getId(), dto);
		
		return dto;
	}
	
	private DistribuicaoDTO obterPercentualFaturamentoTaxaFixa(Long idCota, DistribuicaoDTO dto) {
		
		PDV pdv = this.pdvRepository.obterPDVPrincipal(idCota);
		
		if (pdv != null) {
		
			Rota rota = this.rotaRepository.obterRotaPorPDV(pdv.getId(), idCota);
			
			if (rota != null) {
				
				Entregador entregador = this.entregadorRepository.obterEntregadorPorRota(rota.getId());
				
				if (entregador != null) {
				
					dto.setTaxaFixa(entregador.getTaxaFixa());
					dto.setPercentualFaturamento(entregador.getPercentualFaturamento());
				}
			}
		}
		
		return dto;
	}
	
	@Override
	@Transactional
	public List<Fornecedor> obterFornecedoresCota(Long idCota) {
		Cota cota = this.obterPorId(idCota);
		Set<Fornecedor> fornecedores = cota.getFornecedores();
		List<Fornecedor> listaFornecedores = new ArrayList<Fornecedor>();
		for(Fornecedor itemFornecedor:fornecedores){
			listaFornecedores.add(itemFornecedor);
		}
		return listaFornecedores;
	}

	/**
    * {@inheritDoc}
    */
	@Override
    @Transactional(readOnly = true)
    public CotaDTO obterHistoricoTitularidade(Long idCota, Long idHistorico) {
        Validate.notNull(idCota, "Identificador da Cota não deve ser nulo");
        Validate.notNull(idHistorico, "Identificador do Histórico não deve ser nulo!");
	    HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        CotaDTO dto = HistoricoTitularidadeCotaDTOAssembler.toCotaDTO(historico);
        return dto;
    }
    
    /**
     * {@inheritDoc}
     */
	@Override
    @Transactional(readOnly = true)
    public List<EnderecoAssociacaoDTO> obterEnderecosHistoricoTitularidade(Long idCota, Long idHistorico) {
        Validate.notNull(idCota, "Identificador da Cota não deve ser nulo");
        Validate.notNull(idHistorico, "Identificador do Histórico não deve ser nulo!");
	    HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        return new ArrayList<EnderecoAssociacaoDTO>(HistoricoTitularidadeCotaDTOAssembler.toEnderecoAssociacaoDTOCollection(historico.getEnderecos()));
    }

    /**
     * {@inheritDoc}
     */
	@Override
    @Transactional(readOnly = true)
    public List<TelefoneAssociacaoDTO> obterTelefonesHistoricoTitularidade(Long idCota, Long idHistorico) {
        Validate.notNull(idCota, "Identificador da Cota não deve ser nulo");
        Validate.notNull(idHistorico, "Identificador do Histórico não deve ser nulo!");
        HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        return new ArrayList<TelefoneAssociacaoDTO>(HistoricoTitularidadeCotaDTOAssembler.toTelefoneAssociacaoDTOCollection(historico.getTelefones()));
    }

    /**
     * {@inheritDoc}
     */
	@Override
    @Transactional(readOnly = true)
    public List<FornecedorDTO> obterFornecedoresHistoricoTitularidadeCota(Long idCota, Long idHistorico) {
        Validate.notNull(idCota, "Identificador da Cota não deve ser nulo");
        Validate.notNull(idHistorico, "Identificador do Histórico não deve ser nulo!");
        HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        return new ArrayList<FornecedorDTO>(HistoricoTitularidadeCotaDTOAssembler.toFornecedorDTOCollection(historico.getFornecedores()));
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public List<TipoDescontoProdutoDTO> obterDescontosProdutoHistoricoTitularidadeCota(Long idCota, Long idHistorico) {
	    Validate.notNull(idCota, "Identificador da Cota não deve ser nulo");
        Validate.notNull(idHistorico, "Identificador do Histórico não deve ser nulo!");
        HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        List<TipoDescontoProdutoDTO> dtos = new ArrayList<TipoDescontoProdutoDTO>();
        for (HistoricoTitularidadeCotaDescontoProduto desconto : historico.getDescontosProduto()) {
            dtos.add(new TipoDescontoProdutoDTO(desconto.getCodigo(), desconto
                    .getNome(), desconto.getNumeroEdicao(), desconto
                    .getDesconto(), desconto.getAtualizacao(), null));
        }
	    return dtos;
	}

	/**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<TipoDescontoCotaDTO> obterDescontosCotaHistoricoTitularidadeCota(Long idCota, Long idHistorico) {
        Validate.notNull(idCota, "Identificador da Cota não deve ser nulo");
        Validate.notNull(idHistorico, "Identificador do Histórico não deve ser nulo!");
        HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        List<TipoDescontoCotaDTO> dtos = new ArrayList<TipoDescontoCotaDTO>();
        for (HistoricoTitularidadeCotaDescontoCota desconto : historico.getDescontosCota()) {
            dtos.add(new TipoDescontoCotaDTO(desconto.getDesconto(), desconto
                    .getFornecedor(), desconto.getAtualizacao(), desconto
                    .getTipoDesconto().getDescricao()));
        }
        return dtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public DistribuicaoDTO obterDistribuicaoHistoricoTitularidade(Long idCota, Long idHistorico) {
        Validate.notNull(idCota, "Identificador da Cota não deve ser nulo");
        Validate.notNull(idHistorico, "Identificador do Histórico não deve ser nulo!");
        
        HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        HistoricoTitularidadeCotaDistribuicao distribuicao = historico.getDistribuicao();
        if (distribuicao != null) {
            distribuicao.setHistoricoTitularidadeCota(historico);
            return HistoricoTitularidadeCotaDTOAssembler.toDistribuicaoDTO(distribuicao);
        }
        return new DistribuicaoDTO(); 
    }

	@Override
	@Transactional(readOnly = true)
	public Long obterQuantidadeCotas(SituacaoCadastro situacaoCadastro) {

		return this.cotaRepository.obterQuantidadeCotas(situacaoCadastro);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Cota> obterCotas(SituacaoCadastro situacaoCadastro) {

		return this.cotaRepository.obterCotas(situacaoCadastro);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Cota> obterCotasComInicioAtividadeEm(Date dataInicioAtividade) {

		return this.cotaRepository.obterCotasComInicioAtividadeEm(dataInicioAtividade);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Cota> obterCotasAusentesNaExpedicaoDoReparteEm(Date dataExpedicaoReparte) {

		return this.cotaRepository.obterCotasAusentesNaExpedicaoDoReparteEm(dataExpedicaoReparte);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Cota> obterCotasAusentesNoRecolhimentoDeEncalheEm(Date dataRecolhimentoEncalhe) {

		return this.cotaRepository.obterCotasAusentesNoRecolhimentoDeEncalheEm(dataRecolhimentoEncalhe);
	}

}

