package br.com.abril.nds.repository;

import java.util.Collection;
import java.util.List;

import br.com.abril.nds.model.cadastro.DistribuicaoDistribuidor;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
import br.com.abril.nds.model.cadastro.TipoGarantia;

public interface DistribuidorRepository extends Repository<Distribuidor, Long> {
	
	Distribuidor obter();
	
	List<DistribuicaoFornecedor> buscarDiasDistribuicaoFornecedor(
															Collection<Long> idsForncedores,
													    	OperacaoDistribuidor operacaoDistribuidor);
	
	List<DistribuicaoDistribuidor> buscarDiasDistribuicaoDistribuidor(
															Long idDistruibuidor,
															OperacaoDistribuidor operacaoDistribuidor);
	
	/**
	 * Recupera o endereço principal do distribuidor.
	 * @return endereço principal do distribuidor.
	 */
	public abstract EnderecoDistribuidor obterEnderecoPrincipal();
	
	/**
	 * Recupera os tipos de garantias aceitas pelo distribuidor.
	 * @return
	 */
	public List<TipoGarantia> obtemTiposGarantiasAceitas();

	/**
	 * Recupera o telefone principal do distribuidor.
	 * @return telefone principal do distribuidor.
	 */
	public abstract TelefoneDistribuidor obterTelefonePrincipal();

	List<String> obterNomeCNPJDistribuidor();
	
	String obterInformacoesComplementaresProcuracao();

	String obterRazaoSocialDistribuidor();

	String obterInformacoesComplementaresTermoAdesao();
}