package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.DescontoProdutoDTO;
import br.com.abril.nds.dto.TipoDescontoCotaDTO;
import br.com.abril.nds.dto.TipoDescontoDTO;
import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoProdutoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoDesconto;
import br.com.abril.nds.model.cadastro.desconto.DescontoCota;
import br.com.abril.nds.model.cadastro.desconto.DescontoDistribuidor;
import br.com.abril.nds.model.cadastro.desconto.DescontoProduto;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DescontoCotaRepository;
import br.com.abril.nds.repository.DescontoDistribuidorRepository;
import br.com.abril.nds.repository.DescontoProdutoRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoDescontoRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class DescontoServiceImpl implements DescontoService {

	@Autowired
	private TipoDescontoRepository tipoDescontoRepository;
	
	@Autowired
	private DescontoDistribuidorRepository descontoDistribuidorRepository;
	
	@Autowired
	private DescontoCotaRepository descontoCotaRepository;
	
	@Autowired
	private DescontoProdutoRepository descontoProdutoRepository;
	
	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private CotaRepository cotaRepository;

	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Override
	@Transactional(readOnly=true)
	public List<TipoDesconto> obterTodosTiposDescontos() {
		
		return this.tipoDescontoRepository.buscarTodos();
	}

	@Override
	@Transactional(readOnly=true)
	public TipoDesconto obterTipoDescontoPorID(Long id) {

		return this.tipoDescontoRepository.buscarPorId(id);
	}

	@Override
	@Transactional(readOnly=true)
	public List<TipoDescontoDTO> buscarTipoDesconto(FiltroTipoDescontoDTO filtro) {

		return descontoDistribuidorRepository.buscarDescontos(filtro);
	}

	@Override
	@Transactional(readOnly=true)
	public Integer buscarQntTipoDesconto(FiltroTipoDescontoDTO filtro) {
	
		return descontoDistribuidorRepository.buscarQuantidadeDescontos(filtro);
	}

	@Override
	@Transactional(readOnly=true)
	public List<TipoDescontoCotaDTO> buscarTipoDescontoCota(FiltroTipoDescontoCotaDTO filtro) {
		
		return this.descontoCotaRepository.obterDescontoCota(filtro);
	}

	@Override
	@Transactional(readOnly=true)
	public Integer buscarQuantidadeTipoDescontoCota(FiltroTipoDescontoCotaDTO filtro) {
		
		return this.descontoCotaRepository.obterQuantidadeDescontoCota(filtro);
	}

	@Override
	@Transactional(readOnly=true)
	public List<TipoDescontoProdutoDTO> buscarTipoDescontoProduto(FiltroTipoDescontoProdutoDTO filtro) {

		return this.descontoProdutoRepository.buscarTipoDescontoProduto(filtro);
	}

	@Override
	@Transactional(readOnly=true)
	public Integer buscarQuantidadeTipoDescontoProduto(FiltroTipoDescontoProdutoDTO filtro) {

		return this.descontoProdutoRepository.buscarQuantidadeTipoDescontoProduto(filtro);
	}
	
	@Override
	@Transactional
	public void excluirDesconto(Long idDesconto,br.com.abril.nds.model.cadastro.desconto.TipoDesconto tipoDesconto) {
		
		switch (tipoDesconto) {
			case GERAL:
				
				DescontoDistribuidor desconto = descontoDistribuidorRepository.buscarPorId(idDesconto);
				validarExclusaoDesconto(desconto.getDataAlteracao());
				desconto.setFornecedores(null);
				descontoDistribuidorRepository.remover(desconto);
				
				//FIXME Chamar metodo de tratamento de DESCONTO PRODUTO EDICAO
				
				break;
			case ESPECIFICO:
				
				DescontoCota descontoCota = descontoCotaRepository.buscarPorId(idDesconto);
				validarExclusaoDesconto(descontoCota.getDataAlteracao());
				descontoCota.setFornecedores(null);
				descontoCotaRepository.remover(descontoCota);
				
				//FIXME Chamar metodo de tratamento de DESCONTO PRODUTO EDICAO
				
				break;
			case PRODUTO:
		
				DescontoProduto descontoProduto = descontoProdutoRepository.buscarPorId(idDesconto);
				validarExclusaoDesconto(descontoProduto.getDataAlteracao());
				descontoProduto.setCotas(null);
				descontoProdutoRepository.remover(descontoProduto);
				
				//FIXME Chamar metodo de tratamento de DESCONTO PRODUTO EDICAO
				
				break;
			}
	}
	
	private void validarExclusaoDesconto(Date dataUltimaAlteracao){
		
		Distribuidor distribuidor = distribuidorRepository.obter();
		
		if(dataUltimaAlteracao.compareTo(distribuidor.getDataOperacao()) < 0){
			throw new ValidacaoException(TipoMensagem.WARNING,"Desconto não pode ser excluido fora da data vigente!");
		}
	}
	
	@Override
	@Transactional
	public void incluirDesconto(BigDecimal valorDesconto, List<Long> fornecedores,Usuario usuario) {
		
		if(fornecedores == null || fornecedores.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING,"O campo Fornecedores selecionados deve ser preenchido!");
		}
		
		if(valorDesconto == null ){
			throw new ValidacaoException(TipoMensagem.WARNING,"O campo Desconto deve ser preenchido!");
		}
		
		DescontoDistribuidor desconto = new DescontoDistribuidor();
		desconto.setDesconto(valorDesconto);
		desconto.setUsuario(usuarioRepository.buscarPorId(usuario.getId()));
		desconto.setDataAlteracao(new Date());
		desconto.setFornecedores(new HashSet<Fornecedor>());
		desconto.getFornecedores().addAll(fornecedorRepository.obterFornecedoresPorId(fornecedores));
		desconto.setDistribuidor(distribuidorRepository.obter());
		
		descontoDistribuidorRepository.adicionar(desconto);
		
		//FIXME chamar componente de geração de desconto PRODUTO EDIÇÂO
	}
	
	@Override
	@Transactional
	public void incluirDesconto(BigDecimal valorDesconto, List<Long> fornecedores,Integer numeroCota,Usuario usuario) {
		
		if(numeroCota == null ){
			throw new ValidacaoException(TipoMensagem.WARNING,"O campo Cota deve ser preenchido!");
		}
		
		if(fornecedores == null || fornecedores.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING,"O campo Fornecedores selecionados deve ser preenchido!");
		}
		
		if(valorDesconto == null ){
			throw new ValidacaoException(TipoMensagem.WARNING,"O campo Desconto deve ser preenchido!");
		}
		
		DescontoCota descontoCota = new DescontoCota();
		descontoCota.setDesconto(valorDesconto);
		descontoCota.setCota(cotaRepository.obterPorNumerDaCota(numeroCota));
		descontoCota.setUsuario(usuarioRepository.buscarPorId(usuario.getId()));
		descontoCota.setDataAlteracao(new Date());
		descontoCota.setFornecedores(new HashSet<Fornecedor>());
		descontoCota.getFornecedores().addAll(fornecedorRepository.obterFornecedoresPorId(fornecedores));
		descontoCota.setDistribuidor(distribuidorRepository.obter());
		
		descontoCotaRepository.adicionar(descontoCota);
		
		//FIXME chamar componente de geração de desconto PRODUTO EDIÇÂO
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void incluirDesconto(DescontoProdutoDTO desconto, Usuario usuario) {
		// FIXME Implementar a inclusão do desconto da cota

		validarEntradaDeDadosInclusaoDescontoPorProduto(desconto);

		Distribuidor distribuidor = this.distribuidorRepository.obter();

		Set<Cota> cotas = obterCotas(desconto.getCotas(), desconto.isTodasCotas());

		if (desconto.getEdicaoProduto() != null) {

			ProdutoEdicao produtoEdicao = this.produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(
				desconto.getCodigoProduto(), desconto.getEdicaoProduto()
			);

			DescontoProduto descontoProduto = new DescontoProduto();

			descontoProduto.setDesconto(desconto.getDescontoProduto());
			descontoProduto.setDataAlteracao(new Date());
			descontoProduto.setCotas(cotas);
			descontoProduto.setDistribuidor(distribuidor);
			descontoProduto.setProdutoEdicao(produtoEdicao);
			descontoProduto.setUsuario(usuarioRepository.buscarPorId(usuario.getId()));

			this.descontoProdutoRepository.adicionar(descontoProduto);

		} else if (desconto.getQuantidadeEdicoes() != null) {

			List<ProdutoEdicao> listaProdutoEdicao = 
				this.produtoEdicaoRepository.obterProdutosEdicoesPorCodigoProdutoLimitado(
					desconto.getCodigoProduto(), desconto.getQuantidadeEdicoes()
				);

			for (ProdutoEdicao produtoEdicao : listaProdutoEdicao) {

				DescontoProduto descontoProduto = new DescontoProduto();

				descontoProduto.setDesconto(desconto.getDescontoProduto());
				descontoProduto.setDataAlteracao(new Date());
				descontoProduto.setCotas(cotas);
				descontoProduto.setDistribuidor(distribuidor);
				descontoProduto.setProdutoEdicao(produtoEdicao);
				descontoProduto.setUsuario(usuarioRepository.buscarPorId(usuario.getId()));

				this.descontoProdutoRepository.adicionar(descontoProduto);
			}
		}
	}
	
	private void validarEntradaDeDadosInclusaoDescontoPorProduto(DescontoProdutoDTO desconto) {
	
		List<String> mensagens = new ArrayList<String>();
		
		if (desconto.getCodigoProduto() == null || desconto.getCodigoProduto().isEmpty()) {
			
			mensagens.add("O campo Código deve ser preenchido!");
		}
		
		if (desconto.getEdicaoProduto() == null && desconto.getQuantidadeEdicoes() == null) {
			
			mensagens.add("O campo Edição específica ou Edições deve ser preenchido!");
		}
	
		Integer quantidadeEdicoesExistentes = 
				this.produtoEdicaoRepository.obterQuantidadeEdicoesPorCodigoProduto(desconto.getCodigoProduto());
		
		if (desconto.getQuantidadeEdicoes() != null && 
				quantidadeEdicoesExistentes < desconto.getQuantidadeEdicoes()) {

			mensagens.add("O número máximo de edições que pode ser informado é: " + quantidadeEdicoesExistentes);
		}
		
		if (desconto.getDescontoProduto() == null) {
			
			mensagens.add("O campo Desconto deve ser preenchido!");
		}
		
		if (!desconto.isHasCotaEspecifica() && !desconto.isTodasCotas()) {

			mensagens.add("O campo de cotas deve ser preenchido!");
		}
		
		if (desconto.isHasCotaEspecifica() && desconto.getCotas() == null) {
			
			mensagens.add("Ao menos uma cota deve ser selecionada!");
		}
		
		if (!mensagens.isEmpty()) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagens));
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List<Fornecedor> busacarFornecedoresAssociadosADesconto(Long idDesconto, 
																	br.com.abril.nds.model.cadastro.desconto.TipoDesconto tipoDesconto) {
		List<Fornecedor> listaFornecedores = new ArrayList<Fornecedor>();
		
		switch (tipoDesconto) {
		case GERAL :
			
			DescontoDistribuidor desconto = descontoDistribuidorRepository.buscarPorId(idDesconto);
			
			if(desconto!= null){
				listaFornecedores.addAll(desconto.getFornecedores());
			}
			break;
			
		case ESPECIFICO :
			
			DescontoCota descontoCota = descontoCotaRepository.buscarPorId(idDesconto);
			
			if(descontoCota!= null){
				listaFornecedores.addAll(descontoCota.getFornecedores());
			}
			break;
		}
		
		return listaFornecedores;
	}
	
	private Set<Cota> obterCotas(List<Integer> idsCotas, boolean isTodasCotas) {

		Set<Cota> cotas = null;

		if (idsCotas != null) {

			cotas = new LinkedHashSet<Cota>();

			for (Integer numeroCota : idsCotas) {

				Cota cota = this.cotaRepository.obterPorNumerDaCota(numeroCota);

				cotas.add(cota);
			}

		} else if (isTodasCotas) {

			cotas = new HashSet<Cota>(this.cotaRepository.buscarTodos());
		}

		return cotas;
	}
}
