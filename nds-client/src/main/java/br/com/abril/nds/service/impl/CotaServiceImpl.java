package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.CotaSuspensaoDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.ProdutoValorDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EnderecoCotaRepository;
import br.com.abril.nds.repository.HistoricoSituacaoCotaRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.TipoMensagem;

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
	private CotaRepository cotaRepository;
	
	@Autowired
	private EnderecoCotaRepository enderecoCotaRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private HistoricoSituacaoCotaRepository historicoSituacaoCotaRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private CobrancaRepository cobrancaRepository;

	@Transactional(readOnly = true)
	public Cota obterPorNumeroDaCota(Integer numeroCota) {
		
		return this.cotaRepository.obterPorNumerDaCota(numeroCota);
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
	 * @see br.com.abril.nds.service.CotaService#obterEnderecosPorIdCota(java.lang.Long)
	 */
	@Transactional(readOnly = true)
	public List<EnderecoAssociacaoDTO> obterEnderecosPorIdCota(Long idCota) {

		return this.cotaRepository.obterEnderecosPorIdCota(idCota);
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
	 * @see br.com.abril.nds.service.CotaService#processarEnderecos(br.com.abril.nds.model.cadastro.Cota, java.util.List, java.util.List)
	 */
	@Override
	@Transactional
	public void processarEnderecos(Cota cota,
								   List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar,
								   List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoRemover) {

		if (listaEnderecoAssociacaoSalvar != null && !listaEnderecoAssociacaoSalvar.isEmpty()) {

			this.salvarEnderecosCota(cota, listaEnderecoAssociacaoSalvar);
		}

		if (listaEnderecoAssociacaoRemover != null && !listaEnderecoAssociacaoRemover.isEmpty()) {
			
			this.removerEnderecosCota(cota, listaEnderecoAssociacaoRemover);
		}
	}
	
	private void salvarEnderecosCota(Cota cota, List<EnderecoAssociacaoDTO> listaEnderecoAssociacao) {

		for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao) {

			EnderecoCota enderecoCota = this.enderecoCotaRepository.buscarPorId(enderecoAssociacao.getId());

			if (enderecoCota == null) {

				enderecoCota = new EnderecoCota();

				enderecoCota.setCota(cota);
			}

			enderecoCota.setEndereco(enderecoAssociacao.getEndereco());

			enderecoCota.setPrincipal(enderecoAssociacao.isEnderecoPrincipal());

			enderecoCota.setTipoEndereco(enderecoAssociacao.getTipoEndereco());

			this.enderecoCotaRepository.merge(enderecoCota);
		}
	}

	private void removerEnderecosCota(Cota cota,
									  List<EnderecoAssociacaoDTO> listaEnderecoAssociacao) {
		
		List<Endereco> listaEndereco = new ArrayList<Endereco>();

		for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao) {

			listaEndereco.add(enderecoAssociacao.getEndereco());

			EnderecoCota enderecoCota = this.enderecoCotaRepository.buscarPorId(enderecoAssociacao.getId());
			
			this.enderecoCotaRepository.remover(enderecoCota);
		}
	}

	@Transactional
	public List<Cobranca> obterCobrancasDaCotaEmAberto(Long idCota) {	
		return cobrancaRepository.obterCobrancasDaCotaEmAberto(idCota);
	}

	@Override
	@Transactional
	public List<CotaSuspensaoDTO> suspenderCotasGetDTO(List<Long> idCotas, Long idUsuario) {

		List<Cota> cotasSuspensas =  suspenderCotas(idCotas, idUsuario);
		
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
	public List<Cota> suspenderCotas(List<Long> idCotas, Long idUsuario) {

		List<Cota> cotasSuspensas = new ArrayList<Cota>();
		
		Usuario usuario = usuarioRepository.buscarPorId(idUsuario);
		
		for(Long id:idCotas) {	
			cotasSuspensas.add(suspenderCota(id, usuario));			
		}		
		return cotasSuspensas;
	}

	@Override
	@Transactional
	public Cota suspenderCota(Long idCota, Usuario usuario) {
		
		Cota cota = obterPorId(idCota);
		
		HistoricoSituacaoCota historico = new HistoricoSituacaoCota();
		historico.setCota(cota);
		historico.setData(new Date());
		historico.setNovaSituacao(SituacaoCadastro.SUSPENSO);
		historico.setSituacaoAnterior(cota.getSituacaoCadastro());
		historico.setResponsavel(usuario);
		historico.setMotivo(MotivoAlteracaoSituacao.INADIMPLENCIA);
		historicoSituacaoCotaRepository.adicionar(historico);
		
		cota.setSituacaoCadastro(SituacaoCadastro.SUSPENSO);
		cotaRepository.alterar(cota);
		return cota;
	}
	@Transactional
	public List<Cota> obterCotasSujeitasSuspensao() {

		PoliticaCobranca politicaCobranca = distribuidorRepository.obter().getPoliticaCobranca();
		
		Integer limiteInadimplencias = politicaCobranca.getInadimplenciasSuspencao();
		
		return cotaRepository.obterCotasSujeitasSuspensao(null,null, limiteInadimplencias);
	}

	@Override
	@Transactional
	public List<CotaSuspensaoDTO> obterDTOCotasSujeitasSuspensao(String sortOrder, String sortColumn) {
		
		PoliticaCobranca politicaCobranca = distribuidorRepository.obter().getPoliticaCobranca();
		
		Integer limiteInadimplencias = politicaCobranca.getInadimplenciasSuspencao();
		
		List<Cota> cotasInadimplentes =  cotaRepository.obterCotasSujeitasSuspensao(sortOrder,sortColumn, limiteInadimplencias);
		
		List<CotaSuspensaoDTO> cotasDTO = new ArrayList<CotaSuspensaoDTO>();
		
		for(Cota cota : cotasInadimplentes) {
			
			Pessoa pessoa = cota.getPessoa();
			
			String nome = pessoa instanceof PessoaFisica ? 
					((PessoaFisica)pessoa).getNome() : ((PessoaJuridica)pessoa).getRazaoSocial();
			
			CotaSuspensaoDTO cotaDTO = montarCotaSuspensaoDTO(
					cota.getId(), 
					cota.getNumeroCota(),
					nome, 
					cotaRepository.obterValorConsignadoDaCota(cota.getId()), 
					cotaRepository.obterReparteDaCotaNoDia(cota.getId(), new Date()), 
					cobrancaRepository.obterDividaAcumuladaCota(cota.getId()),
					cobrancaRepository.obterDataAberturaDividas(cota.getId()),
					false);			
			cotasDTO.add(cotaDTO);
		}
				
		return cotasDTO;
	}	
	
	private CotaSuspensaoDTO montarCotaSuspensaoDTO(
			Long idcota, Integer numeroCota, String nome, List<ProdutoValorDTO> valoresConsignadoDaCota, 
			List<ProdutoValorDTO> repartesDaCotaNoDia, Double dividaAcumuladaCota, 
			Date dataInicioDivida,boolean b) {
			
		Long diasEmAberto = dataInicioDivida==null? 
				0L : (((new Date()).getTime() - dataInicioDivida.getTime()) / 86400000L);
			
		CotaSuspensaoDTO dto =  new CotaSuspensaoDTO(
				idcota,
				numeroCota, 
				nome,
				CurrencyUtil.formatarValor(obterValorPrecoQuantidade(valoresConsignadoDaCota)), 
				CurrencyUtil.formatarValor(obterValorPrecoQuantidade(repartesDaCotaNoDia)), 
				CurrencyUtil.formatarValor(dividaAcumuladaCota), 
				diasEmAberto, 
				false);
		
		return dto;		
	}

	private Double obterValorPrecoQuantidade(List<ProdutoValorDTO> itens) {
		
		Double total = 0.0;
		
		for(ProdutoValorDTO pv : itens) {
			total+=pv.getTotal();
		}				
		return total;
	}
	
}
