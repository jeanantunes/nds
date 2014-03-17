package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroCadastroTipoNotaDTO;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.TipoUsuarioNotaFiscal;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.SerieRepository;
import br.com.abril.nds.repository.NaturezaOperacaoRepository;
import br.com.abril.nds.service.TipoNotaFiscalService;

//TODO: Renomear a classe para NAturezaOperacao
@Service
public class TipoNotaFiscalServiceImpl implements TipoNotaFiscalService {

	@Autowired
	private NaturezaOperacaoRepository naturezaOperacaoRepository;

	@Autowired
	private DistribuidorRepository distribuidorRepository;

	@Autowired 
	private SerieRepository serieRepository;

	@Override
	@Transactional
	public NaturezaOperacao obterPorId(Long id) {

		return this.naturezaOperacaoRepository.buscarPorId(id);
	}

	@Override
	@Transactional(readOnly=true)
	public Integer obterQuantidadeTiposNotasFiscais(FiltroCadastroTipoNotaDTO filtro) {
		return naturezaOperacaoRepository.obterQuantidadeTiposNotasFiscais(filtro);
	}


	/**
	 * 
	 */
	@Override
	@Transactional
	public Long proximoNumeroDocumentoFiscal(int serie) {		
		return serieRepository.next(serie);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.TipoNotaFiscalService#carregarComboTiposNotasFiscaisPorFornecedores(java.util.List)
	 */
	@Override
	@Transactional
	public List<ItemDTO<Long, String>> carregarComboTiposNotasFiscais(TipoAtividade tipoAtividade) {

		List<NaturezaOperacao> listaTipoNotaFiscal = this.obterTiposNotaFiscal(tipoAtividade);

		List<ItemDTO<Long, String>> listaItensNotasFiscais = new ArrayList<ItemDTO<Long,String>>();

		for (NaturezaOperacao tipoNotaFiscal : listaTipoNotaFiscal) {

			ItemDTO<Long, String> itemNotaFiscal = new ItemDTO<Long, String>();

			itemNotaFiscal.setKey(tipoNotaFiscal.getId());
			itemNotaFiscal.setValue(tipoNotaFiscal.getDescricao()); // + " - " + tipoNotaFiscal.getEmitente());

			listaItensNotasFiscais.add(itemNotaFiscal);
		}

		return listaItensNotasFiscais;
	}
	
	@Override
	@Transactional
	public List<NaturezaOperacao> obterTiposNotaFiscal(TipoAtividade tipoAtividade){
		
		if (tipoAtividade == null){
			
			tipoAtividade = this.distribuidorRepository.tipoAtividade();
		}
		
		return this.naturezaOperacaoRepository.obterTiposNotasFiscaisCotasNaoContribuintesPor(tipoAtividade);
	}

	@Override
	@Transactional
	public List<ItemDTO<Long, String>> carregarComboTiposNotasFiscais(TipoOperacao tipoOperacao) {//, TipoUsuarioNotaFiscal tipoDestinatario, TipoUsuarioNotaFiscal tipoEmitente, GrupoNotaFiscal[] grupoNotaFiscal) {

		if(this.distribuidorRepository.obrigacaoFiscal() == null){
			return null;
		}

		List<NaturezaOperacao> listaTipoNotaFiscal = this.naturezaOperacaoRepository.obterNaturezasOperacoes(tipoOperacao);

		List<ItemDTO<Long, String>> listaItensNotasFiscais = new ArrayList<ItemDTO<Long,String>>();

		for (NaturezaOperacao tipoNotaFiscal : listaTipoNotaFiscal) {

			ItemDTO<Long, String> itemNotaFiscal = new ItemDTO<Long, String>();

			itemNotaFiscal.setKey(tipoNotaFiscal.getId());
			itemNotaFiscal.setValue(tipoNotaFiscal.getDescricao());

			listaItensNotasFiscais.add(itemNotaFiscal);
		}

		return listaItensNotasFiscais;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public List<NaturezaOperacao> obterTiposNotasFiscaisPorTipoAtividadeDistribuidor() {

		return naturezaOperacaoRepository.obterTiposNotasFiscaisPorTipoAtividadeDistribuidor(
				this.distribuidorRepository.tipoAtividade());
	}

	@Transactional
	@Override
	public List<NaturezaOperacao> consultarTipoNotaFiscal(FiltroCadastroTipoNotaDTO filtro){
		return naturezaOperacaoRepository.consultarTipoNotaFiscal(filtro);
	}

	@Override
	@Transactional
	public List<ItemDTO<Long, String>> carregarComboTiposNotasFiscais(TipoOperacao tipoOperacao,
			TipoUsuarioNotaFiscal tipoDestinatario, TipoUsuarioNotaFiscal tipoEmitente,
			GrupoNotaFiscal[] grupoNotaFiscal) {

		List<NaturezaOperacao> listaTipoNotaFiscal = this.naturezaOperacaoRepository.obterTiposNotasFiscais(tipoOperacao, tipoDestinatario, tipoEmitente, grupoNotaFiscal);

		List<ItemDTO<Long, String>> listaItensNotasFiscais = new ArrayList<ItemDTO<Long,String>>();

		for (NaturezaOperacao tipoNotaFiscal : listaTipoNotaFiscal) {

			ItemDTO<Long, String> itemNotaFiscal = new ItemDTO<Long, String>();

			itemNotaFiscal.setKey(tipoNotaFiscal.getId());
			itemNotaFiscal.setValue(tipoNotaFiscal.getDescricao());

			listaItensNotasFiscais.add(itemNotaFiscal);
		}

		return listaItensNotasFiscais;

	}

}