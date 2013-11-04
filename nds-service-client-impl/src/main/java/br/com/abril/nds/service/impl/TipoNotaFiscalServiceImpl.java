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
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.TipoUsuarioNotaFiscal;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.SerieRepository;
import br.com.abril.nds.repository.TipoNotaFiscalRepository;
import br.com.abril.nds.service.TipoNotaFiscalService;

@Service
public class TipoNotaFiscalServiceImpl implements TipoNotaFiscalService {

	@Autowired
	private TipoNotaFiscalRepository tipoNotaFiscalRepository;

	@Autowired
	private DistribuidorRepository distribuidorRepository;

	@Autowired 
	private SerieRepository serieRepository;

	@Override
	@Transactional
	public TipoNotaFiscal obterPorId(Long id) {

		return this.tipoNotaFiscalRepository.buscarPorId(id);
	}

	@Override
	@Transactional(readOnly=true)
	public Integer obterQuantidadeTiposNotasFiscais(FiltroCadastroTipoNotaDTO filtro) {
		return tipoNotaFiscalRepository.obterQuantidadeTiposNotasFiscais(filtro);
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

		List<TipoNotaFiscal> listaTipoNotaFiscal = this.tipoNotaFiscalRepository.obterTiposNotasFiscaisCotasNaoContribuintesPor(tipoAtividade);

		List<ItemDTO<Long, String>> listaItensNotasFiscais = new ArrayList<ItemDTO<Long,String>>();

		for (TipoNotaFiscal tipoNotaFiscal : listaTipoNotaFiscal) {

			ItemDTO<Long, String> itemNotaFiscal = new ItemDTO<Long, String>();

			itemNotaFiscal.setKey(tipoNotaFiscal.getId());
			itemNotaFiscal.setValue(tipoNotaFiscal.getDescricao());

			listaItensNotasFiscais.add(itemNotaFiscal);
		}

		return listaItensNotasFiscais;
	}

	@Override
	@Transactional
	public List<ItemDTO<Long, String>> carregarComboTiposNotasFiscais(TipoOperacao tipoOperacao) {//, TipoUsuarioNotaFiscal tipoDestinatario, TipoUsuarioNotaFiscal tipoEmitente, GrupoNotaFiscal[] grupoNotaFiscal) {

		if(this.distribuidorRepository.obrigacaoFiscal() == null){
			return null;
		}

		List<TipoNotaFiscal> listaTipoNotaFiscal = this.tipoNotaFiscalRepository.obterTiposNotasFiscais(tipoOperacao);

		List<ItemDTO<Long, String>> listaItensNotasFiscais = new ArrayList<ItemDTO<Long,String>>();

		for (TipoNotaFiscal tipoNotaFiscal : listaTipoNotaFiscal) {

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
	public List<TipoNotaFiscal> obterTiposNotasFiscaisPorTipoAtividadeDistribuidor() {

		return tipoNotaFiscalRepository.obterTiposNotasFiscaisPorTipoAtividadeDistribuidor(
				this.distribuidorRepository.tipoAtividade());
	}

	@Transactional
	@Override
	public List<TipoNotaFiscal> consultarTipoNotaFiscal(FiltroCadastroTipoNotaDTO filtro){

		return tipoNotaFiscalRepository.consultarTipoNotaFiscal(filtro);

	}

	@Override
	@Transactional
	public List<ItemDTO<Long, String>> carregarComboTiposNotasFiscais(TipoOperacao tipoOperacao,
			TipoUsuarioNotaFiscal tipoDestinatario, TipoUsuarioNotaFiscal tipoEmitente,
			GrupoNotaFiscal[] grupoNotaFiscal) {

		if(this.distribuidorRepository.obrigacaoFiscal() == null){
			return null;
		}

		List<TipoNotaFiscal> listaTipoNotaFiscal = this.tipoNotaFiscalRepository.obterTiposNotasFiscais(tipoOperacao, tipoDestinatario, tipoEmitente, grupoNotaFiscal);

		List<ItemDTO<Long, String>> listaItensNotasFiscais = new ArrayList<ItemDTO<Long,String>>();

		for (TipoNotaFiscal tipoNotaFiscal : listaTipoNotaFiscal) {

			ItemDTO<Long, String> itemNotaFiscal = new ItemDTO<Long, String>();

			itemNotaFiscal.setKey(tipoNotaFiscal.getId());
			itemNotaFiscal.setValue(tipoNotaFiscal.getDescricao());

			listaItensNotasFiscais.add(itemNotaFiscal);
		}

		return listaItensNotasFiscais;

	}

}
