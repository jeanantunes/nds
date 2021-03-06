package br.com.abril.nds.integracao.service.impl;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.integracao.model.canonic.EMS2021Input;
import br.com.abril.nds.integracao.model.canonic.EMS2021InputItem;
import br.com.abril.nds.integracao.repository.EstrategiaRepository;
import br.com.abril.nds.model.integracao.icd.*;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.integracao.model.canonic.EMS0128Input;
import br.com.abril.nds.integracao.model.canonic.EMS0128InputItem;
import br.com.abril.nds.integracao.repository.SolicitacaoFaltasSobrasRepository;
import br.com.abril.nds.integracao.service.IcdObjectService;
import br.com.abril.nds.model.integracao.icd.pks.DfsPK;
import br.com.abril.nds.model.integracao.icd.pks.MfsPK;
import br.com.abril.nds.model.integracao.icd.pks.SfsPK;
import br.com.abril.nds.util.Constantes;


@Service
public class IcdObjectServiceImpl implements IcdObjectService {
	
	@Autowired
	private SolicitacaoFaltasSobrasRepository solicitacaoFaltasSobrasRepository;

	@Autowired
	private EstrategiaRepository estrategiaRepository;

	public IcdObjectServiceImpl() {
		
	}
	
	@Override
	public Set<Integer> recuperaSolicitacoesSolicitadas(Long distribuidor) {

		return solicitacaoFaltasSobrasRepository.recuperaSolicitacoesSolicitadas(distribuidor) ;
	
	}

	@Override
	public Set<Integer> recuperaSolicitacoesAcertadas(Long distribuidor) {
	
		return solicitacaoFaltasSobrasRepository.recuperaSolicitacoesAcertadas(distribuidor);

	}

	@Override
	public SolicitacaoFaltaSobra recuperaSolicitacao(Long distribuidor, EMS0128Input doc) {
		return solicitacaoFaltasSobrasRepository.recuperaSolicitacao(distribuidor, doc.getDataSolicitacao(), DateFormatUtils.format(doc.getHoraDeCriacao(), "hh:mm:ss", Constantes.LOCALE_EN_US) );
	}

	@Override
	public void insereSolicitacao(EMS0128Input doc) {
		
		SolicitacaoFaltaSobra sfs = new SolicitacaoFaltaSobra();

		SfsPK sfsPK = new SfsPK();		
		sfsPK.setCodigoDistribuidor( Long.valueOf( doc.getCodigoDistribuidor() ) );
		sfsPK.setDataSolicitacao(doc.getDataSolicitacao());
		sfsPK.setHoraSolicitacao(DateFormatUtils.format(doc.getHoraDeCriacao(), "hh:mm:ss") );
		sfs.setSfsPK(sfsPK);
		
		sfs.setCodigoForma(doc.getFormaSolicitacao());
		sfs.setCodigoSituacao(doc.getSituacaoSolicitacao());
				
		for (EMS0128InputItem item : doc.getItens()) {
			
			DetalheFaltaSobra dfs = new DetalheFaltaSobra();
			
			//pk
			DfsPK dfsPK = new DfsPK();
			dfsPK.setCodigoDistribuidor( Long.valueOf( doc.getCodigoDistribuidor() ) );
			dfsPK.setDataSolicitacao(doc.getDataSolicitacao());
			dfsPK.setHoraSolicitacao(DateFormatUtils.format(doc.getHoraDeCriacao(), "hh:mm:ss") );
			dfsPK.setNumeroSequencia(item.getNumSequenciaDetalhe());
			dfs.setDfsPK(dfsPK);
						
			dfs.setCodigoTipoAcerto(item.getTipoAcerto());
			dfs.setCodigoAcerto(doc.getSituacaoSolicitacao());
			dfs.setCodigoPublicacaoAdabas(item.getCodigoProduto());			
			dfs.setNumeroEdicao(item.getNumeroEdicao());
			
			if(item.getQtd() != null)
			dfs.setQtdSolicitada(item.getQtd().longValue());
			
			if(item.getPrecoCapa() != null)
				dfs.setValorUnitario(item.getPrecoCapa().doubleValue());

			if(item.getPercentualDesconto() != null)
				dfs.setPctDesconto(item.getPercentualDesconto().doubleValue());			

			dfs.setNumeroDocumentoOrigem(null);
			dfs.setNumeroDocumentoAcerto(null);
			dfs.setDataEmissaoDocumentoAcerto(null);
			dfs.setCodigoUsuarioAcerto(null);
			
			
			MotivoSituacaoFaltaSobra mfs = new MotivoSituacaoFaltaSobra();
			
			MfsPK mfsPK = new MfsPK();
			mfsPK.setCodigoDistribuidor( Long.valueOf( doc.getCodigoDistribuidor() ) );
			mfsPK.setDataSolicitacao(doc.getDataSolicitacao());
			mfsPK.setHoraSolicitacao(DateFormatUtils.format(doc.getHoraDeCriacao(), "hh:mm:ss")  );
			mfsPK.setNumeroSequencia(item.getNumSequenciaDetalhe());
			mfs.setMfsPK(mfsPK);			
			
			dfs.setMotivoSituacaoFaltaSobra(mfs);
			
			sfs.getItens().add(dfs);			
		}		
		
		solicitacaoFaltasSobrasRepository.save(sfs);
		
	}

	@Override
	public MotivoSituacaoFaltaSobra recuperaMotivoPorDetalhe(
			DfsPK pkItem) {
		return solicitacaoFaltasSobrasRepository.recuperaMotivoPorDetalhe(pkItem);
	}

	@Override
	public List<EMS2021Input> obterEstrategias(Long codigoDistribuidor) {
		return estrategiaRepository.obterEstrategias(codigoDistribuidor);
	}

	@Override
	public List<EMS2021InputItem> obterEdicaoBaseEstrategia(Integer codigoPraca, BigInteger codigoLancamentoEdicao) {
		return estrategiaRepository.obterEdicaoBaseEstrategia(codigoPraca, codigoLancamentoEdicao);
	}

}
