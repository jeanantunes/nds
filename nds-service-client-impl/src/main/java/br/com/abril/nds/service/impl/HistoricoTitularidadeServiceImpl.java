package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.TipoDescontoCotaDTO;
import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoCotaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Cheque;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCaucaoLiquida;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.model.cadastro.ContaBancariaDeposito;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.EnderecoFiador;
import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaCobrancaCaucaoLiquida;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Garantia;
import br.com.abril.nds.model.cadastro.Imovel;
import br.com.abril.nds.model.cadastro.LicencaMunicipal;
import br.com.abril.nds.model.cadastro.MaterialPromocional;
import br.com.abril.nds.model.cadastro.NotaPromissoria;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.ParametroDistribuicaoCota;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ReferenciaCota;
import br.com.abril.nds.model.cadastro.SocioCota;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TelefoneFiador;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantia;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaCaucaoLiquida;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaChequeCaucao;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaFiador;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaImovel;
import br.com.abril.nds.model.cadastro.garantia.CotaGarantiaNotaPromissoria;
import br.com.abril.nds.model.cadastro.garantia.pagamento.PagamentoBoleto;
import br.com.abril.nds.model.cadastro.garantia.pagamento.PagamentoCaucaoLiquida;
import br.com.abril.nds.model.cadastro.garantia.pagamento.PagamentoDescontoCota;
import br.com.abril.nds.model.cadastro.pdv.AreaInfluenciaPDV;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.PeriodoFuncionamentoPDV;
import br.com.abril.nds.model.cadastro.pdv.TelefonePDV;
import br.com.abril.nds.model.cadastro.pdv.TipoEstabelecimentoAssociacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoGeradorFluxoPDV;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaBanco;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaCaucaoLiquida;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaChequeCaucao;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaCodigoDescricao;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaConcentracaoCobranca;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaDesconto;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaDescontoCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaDescontoProduto;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaDistribuicao;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaEndereco;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFiador;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFiadorGarantia;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFinanceiro;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFormaPagamento;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFornecedor;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFuncionamentoPDV;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaGarantia;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaImovel;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaNotaPromissoria;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaPDV;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaPagamentoCaucaoLiquida;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaPessoaFisica;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaPessoaJuridica;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaReferenciaCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaSocio;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaTelefone;
import br.com.abril.nds.repository.DescontoCotaRepository;
import br.com.abril.nds.repository.DescontoProdutoRepository;
import br.com.abril.nds.repository.ParametroCobrancaCotaRepository;
import br.com.abril.nds.service.HistoricoTitularidadeService;

/**
 * Implementação do componente que gera histórico de titularidade da cota.
 * 
 * @author Discover Technology
 *
 */
@Service
public class HistoricoTitularidadeServiceImpl implements HistoricoTitularidadeService {

	@Autowired
	private DescontoProdutoRepository descontoProdutoRepository;
	
	@Autowired
	private DescontoCotaRepository descontoCotaRepository;
	
	@Autowired
	private ParametroCobrancaCotaRepository parametroCobrancaCotaRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public HistoricoTitularidadeCota gerarHistoricoTitularidadeCota(Cota cota) {

		if (cota == null) {

			throw new ValidacaoException(TipoMensagem.ERROR, "Cota inválida!");

		} else if (cota.getPessoa() == null) {

			throw new ValidacaoException(TipoMensagem.ERROR, "Pessoa da cota é inválida!");
		}

		HistoricoTitularidadeCota historicoTitularidadeCota = new HistoricoTitularidadeCota();

		historicoTitularidadeCota.setCota(cota);
		
		historicoTitularidadeCota.setDataInclusao(cota.getInicioAtividade());
		
		historicoTitularidadeCota.setInicio(cota.getInicioTitularidade());
		historicoTitularidadeCota.setEmail(cota.getPessoa().getEmail());
		historicoTitularidadeCota.setSituacaoCadastro(cota.getSituacaoCadastro());
		historicoTitularidadeCota.setNumeroCota(cota.getNumeroCota());
		historicoTitularidadeCota.setFim(new Date());
		historicoTitularidadeCota.setClassificacaoExpectativaFaturamento(cota.getClassificacaoEspectativaFaturamento());
		historicoTitularidadeCota.setTelefones(gerarHistoricoTitularidadeCotaTelefoneCota(cota.getTelefones()));
		historicoTitularidadeCota.setEnderecos(gerarHistoricoTitularidadeCotaEnderecoCota(cota.getEnderecos()));
		historicoTitularidadeCota.setFornecedores(gerarHistoricoTitularidadeCotaFornecedor(cota.getFornecedores()));
		historicoTitularidadeCota.setPdvs(gerarHistoricoTitularidadeCotaPDV(historicoTitularidadeCota, cota.getPdvs()));
		historicoTitularidadeCota.setSocios(
			gerarHistoricoTitularidadeCotaSocio(cota.getSociosCota())
		);
		historicoTitularidadeCota.setDistribuicao(
			gerarHistoricoTitularidadeCotaDistribuicao(cota.getParametroDistribuicao())
		);
		historicoTitularidadeCota.setDescontos(
			gerarHistoricoTitularidadeCotaDesconto(historicoTitularidadeCota, cota.getId(), cota.getNumeroCota())
		);
		historicoTitularidadeCota.setGarantias(
			(List<HistoricoTitularidadeCotaGarantia>) gerarHistoricoTitularidadeCotaGarantias(historicoTitularidadeCota, cota.getCotaGarantia())
		);
		historicoTitularidadeCota.setFinanceiro(
			gerarHistoricoTitularidadeCotaFinanceiro(cota.getParametroCobranca())
		);
		
		if (cota.getBaseReferenciaCota() != null) {

			historicoTitularidadeCota.setInicioPeriodoCotaBase(
				cota.getBaseReferenciaCota().getInicioPeriodo()
			);
			
			historicoTitularidadeCota.setFimPeriodoCotaBase(
				cota.getBaseReferenciaCota().getFinalPeriodo()
			);
			
			historicoTitularidadeCota.setReferencias(
				gerarHistoricoTitularidadeCotaReferenciaCota(cota.getBaseReferenciaCota().getReferenciasCota())
			);
		}

		Pessoa pessoa = cota.getPessoa();

		if (pessoa instanceof PessoaJuridica) {
			
			historicoTitularidadeCota.setPessoaJuridica(
				gerarHistoricoTitularidadeCotaPessoaJuridica((PessoaJuridica) pessoa)
			);
			
		} else if (pessoa instanceof PessoaFisica) {
			
			historicoTitularidadeCota.setPessoaFisica(
				gerarHistoricoTitularidadeCotaPessoaFisica((PessoaFisica) pessoa)
			);
		}

		return historicoTitularidadeCota;
	}

	/*
	 * Método que gera um histórico para os telefones.
	 * 
	 * @param telefone - Telefone.
	 * 
	 * @return HistoricoTitularidadeCotaTelefone - histórico gerado
	 */
	private HistoricoTitularidadeCotaTelefone gerarHistoricoTitularidadeCotaTelefone(Telefone telefone) {
		
		if (telefone == null) {
			
			return null;
		}
		
		HistoricoTitularidadeCotaTelefone historicoTitularidadeCotaTelefone = 
				new HistoricoTitularidadeCotaTelefone();
		
		historicoTitularidadeCotaTelefone.setDdd(telefone.getDdd());
		historicoTitularidadeCotaTelefone.setNumero(telefone.getNumero());
		historicoTitularidadeCotaTelefone.setRamal(telefone.getRamal());

		return historicoTitularidadeCotaTelefone;
	}

	/*
	 * Método que gera um histórico para os endereços.
	 * 
	 * @param endereco - Endereço.
	 * 
	 * @return HistoricoTitularidadeCotaEndereco - histórico gerado
	 */	
	private HistoricoTitularidadeCotaEndereco gerarHistoricoTitularidadeCotaEndereco(Endereco endereco) {
		
		if (endereco == null) {
			
			return null;
		}
		
		HistoricoTitularidadeCotaEndereco historicoTitularidadeCotaEndereco = new HistoricoTitularidadeCotaEndereco();
		
		historicoTitularidadeCotaEndereco.setBairro(endereco.getBairro());
		historicoTitularidadeCotaEndereco.setCep(endereco.getCep());
		historicoTitularidadeCotaEndereco.setCidade(endereco.getCidade());
		historicoTitularidadeCotaEndereco.setCodigoCidadeIBGE(endereco.getCodigoCidadeIBGE());
		historicoTitularidadeCotaEndereco.setCodigoUf(endereco.getCodigoUf());
		historicoTitularidadeCotaEndereco.setComplemento(endereco.getComplemento());
		historicoTitularidadeCotaEndereco.setLogradouro(endereco.getLogradouro());
		historicoTitularidadeCotaEndereco.setNumero(endereco.getNumero());
		historicoTitularidadeCotaEndereco.setTipoLogradouro(endereco.getTipoLogradouro());
		historicoTitularidadeCotaEndereco.setUf(endereco.getUf());
		
		return historicoTitularidadeCotaEndereco;
	}
	
	/*
	 * Método que gera um histórico para os telefones referentes a cota utilizada na alteração de titularidade.
	 * 
	 * @param telefoneCotas - Coleção de telefones da cota.
	 * 
	 * @return Set<HistoricoTitularidadeCotaTelefone> - histórico gerado
	 */
	private Set<HistoricoTitularidadeCotaTelefone> gerarHistoricoTitularidadeCotaTelefoneCota(Set<TelefoneCota> telefoneCotas) {

		if (telefoneCotas == null || telefoneCotas.isEmpty()) {
			
			return null;
		}
		
		Set<HistoricoTitularidadeCotaTelefone> historicosCotaTelefone = new HashSet<HistoricoTitularidadeCotaTelefone>();

		for (TelefoneCota telefoneCota : telefoneCotas) {

			HistoricoTitularidadeCotaTelefone historicoTitularidadeCotaTelefone = 
					gerarHistoricoTitularidadeCotaTelefone(telefoneCota.getTelefone());
			
			historicoTitularidadeCotaTelefone.setTipoTelefone(telefoneCota.getTipoTelefone());
			historicoTitularidadeCotaTelefone.setPrincipal(telefoneCota.isPrincipal());

			historicosCotaTelefone.add(historicoTitularidadeCotaTelefone);
		}

		return historicosCotaTelefone;
	}

	/*
	 * Método que gera um histórico para os endereços referentes a cota utilizada na alteração de titularidade.
	 * 
	 * @param enderecoCotas - Coleção de endereços da cota.
	 * 
	 * @return Set<HistoricoTitularidadeCotaEndereco> - histórico gerado
	 */
	private Set<HistoricoTitularidadeCotaEndereco> gerarHistoricoTitularidadeCotaEnderecoCota(Set<EnderecoCota> enderecoCotas) {
		
		if (enderecoCotas == null || enderecoCotas.isEmpty()) {
			
			return null;
		}
		
		Set<HistoricoTitularidadeCotaEndereco> historicosTitularidadeCotaEndereco = new HashSet<HistoricoTitularidadeCotaEndereco>();
		
		for (EnderecoCota enderecoCota : enderecoCotas) {

			HistoricoTitularidadeCotaEndereco historicoTitularidadeCotaEndereco = 
					gerarHistoricoTitularidadeCotaEndereco(enderecoCota.getEndereco());
			
			historicoTitularidadeCotaEndereco.setPrincipal(enderecoCota.isPrincipal());
			historicoTitularidadeCotaEndereco.setTipoEndereco(enderecoCota.getTipoEndereco());
			
			historicosTitularidadeCotaEndereco.add(historicoTitularidadeCotaEndereco);
			
		}

		return historicosTitularidadeCotaEndereco;
	}
	
	/*
	 * Método que gera um histórico para os fornecedores referentes a cota utilizada na alteração de titularidade.
	 * 
	 * @param fornecedores - Coleção com os fornecedores.
	 * 
	 * @return Set<HistoricoTitularidadeCotaFornecedor> - histórico gerado.
	 */
	private Set<HistoricoTitularidadeCotaFornecedor> gerarHistoricoTitularidadeCotaFornecedor(Set<Fornecedor> fornecedores) {
		
		if (fornecedores == null) {
			
			return null;
		}
		
		Set<HistoricoTitularidadeCotaFornecedor> historicosTitularidadeCotaFornecedor = new HashSet<HistoricoTitularidadeCotaFornecedor>();

		for (Fornecedor fornecedor : fornecedores) {
		
			HistoricoTitularidadeCotaPessoaJuridica historicoTitularidadeCotaPessoaJuridica =
					gerarHistoricoTitularidadeCotaPessoaJuridica(fornecedor.getJuridica());

			HistoricoTitularidadeCotaFornecedor historicoTitularidadeCotaFornecedor = new HistoricoTitularidadeCotaFornecedor();
			historicoTitularidadeCotaFornecedor.setPessoaJuridica(historicoTitularidadeCotaPessoaJuridica);
			
			historicosTitularidadeCotaFornecedor.add(historicoTitularidadeCotaFornecedor);
		}
			
		return historicosTitularidadeCotaFornecedor;
	}
	
	/*
	 * Método que gera um histórico para pessoa jurídica referente a cota utilizada na alteração de titularidade.
	 * 
	 * @param juridica - Pessoa Jurídica
	 * 
	 * @return HistoricoTitularidadeCotaPessoaJuridica - histórico gerado.
	 */
	private HistoricoTitularidadeCotaPessoaJuridica gerarHistoricoTitularidadeCotaPessoaJuridica(PessoaJuridica juridica) {
		
		if (juridica == null) {
			
			return null;
		}
		
		HistoricoTitularidadeCotaPessoaJuridica historicoTitularidadeCotaPessoaJuridica = new HistoricoTitularidadeCotaPessoaJuridica();
		
		historicoTitularidadeCotaPessoaJuridica.setCnpj(juridica.getCnpj());
		historicoTitularidadeCotaPessoaJuridica.setInscricaoEstadual(juridica.getInscricaoEstadual());
		historicoTitularidadeCotaPessoaJuridica.setInscricaoMunicipal(juridica.getInscricaoMunicipal());
		historicoTitularidadeCotaPessoaJuridica.setNomeFantasia(juridica.getNomeFantasia());
		historicoTitularidadeCotaPessoaJuridica.setRazaoSocial(juridica.getRazaoSocial());
	
		return historicoTitularidadeCotaPessoaJuridica;
	}
	
	/*
	 * Método que gera um histórico para pessoa física referente a cota utilizada na alteração de titularidade.
	 *  
	 * @param fisica - Pessoa fisica.
	 * 
	 * @return HistoricoTitularidadeCotaPessoaFisica - histórico gerado.
	 */
	private HistoricoTitularidadeCotaPessoaFisica gerarHistoricoTitularidadeCotaPessoaFisica(PessoaFisica fisica) {
		
		if (fisica == null) {
			
			return null;
		}
		
		HistoricoTitularidadeCotaPessoaFisica historicoTitularidadeCotaPessoaFisica = new HistoricoTitularidadeCotaPessoaFisica();
		
		historicoTitularidadeCotaPessoaFisica.setApelido(fisica.getApelido());
		historicoTitularidadeCotaPessoaFisica.setCpf(fisica.getCpf());
		historicoTitularidadeCotaPessoaFisica.setDataNascimento(fisica.getDataNascimento());
		historicoTitularidadeCotaPessoaFisica.setEstadoCivil(fisica.getEstadoCivil());
		historicoTitularidadeCotaPessoaFisica.setNacionalidade(fisica.getNacionalidade());
		historicoTitularidadeCotaPessoaFisica.setNatural(fisica.getNatural());
		historicoTitularidadeCotaPessoaFisica.setNome(fisica.getNome());
		historicoTitularidadeCotaPessoaFisica.setOrgaoEmissor(fisica.getOrgaoEmissor());
		historicoTitularidadeCotaPessoaFisica.setRg(fisica.getRg());
		historicoTitularidadeCotaPessoaFisica.setSexo(fisica.getSexo());
		historicoTitularidadeCotaPessoaFisica.setUfOrgaoEmissor(fisica.getUfOrgaoEmissor());
		
		return historicoTitularidadeCotaPessoaFisica;
	}
	
	/*
	 * Método que gera um histórico para os pdvs referentes a cota utilizada na alteração de titularidade.
	 * 
	 * @param pdvs - Coleção de pdvs.
	 * 
	 * @return List<HistoricoTitularidadeCotaPDV> - histórico gerado.
	 * 
	 */
	private List<HistoricoTitularidadeCotaPDV> gerarHistoricoTitularidadeCotaPDV(HistoricoTitularidadeCota historicoTitularidadeCota, List<PDV> pdvs) {
		
		if (pdvs == null || pdvs.isEmpty()) {
			
			return null;
		}
		
		List<HistoricoTitularidadeCotaPDV> historicosTitularidadeCotaPDV = new ArrayList<HistoricoTitularidadeCotaPDV>();
		
		for (PDV pdv : pdvs) {
			
			HistoricoTitularidadeCotaPDV historicoTitularidadeCotaPDV = new HistoricoTitularidadeCotaPDV();
			
			historicoTitularidadeCotaPDV.setArrendatario(pdv.isArrendatario());
			historicoTitularidadeCotaPDV.setCaracteristicas(pdv.getCaracteristicas());
			historicoTitularidadeCotaPDV.setContato(pdv.getContato());
			historicoTitularidadeCotaPDV.setDataInclusao(pdv.getDataInclusao());
			historicoTitularidadeCotaPDV.setDentroOutroEstabelecimento(pdv.isDentroOutroEstabelecimento());
			historicoTitularidadeCotaPDV.setEmail(pdv.getEmail());
			historicoTitularidadeCotaPDV.setEnderecos(gerarHistoricoTitularidadeCotaEnderecoPDV(pdv.getEnderecos()));
			
			LicencaMunicipal licencaMunicipal = pdv.getLicencaMunicipal();
			if (licencaMunicipal != null) {
			    historicoTitularidadeCotaPDV.setNomeLicencaMunicipal(licencaMunicipal.getNomeLicenca());
			    historicoTitularidadeCotaPDV.setNumeroLicencaMunicipal(licencaMunicipal.getNumeroLicenca());
                HistoricoTitularidadeCotaCodigoDescricao tipoLicencaMunicipal = new HistoricoTitularidadeCotaCodigoDescricao(
                        licencaMunicipal.getTipoLicencaMunicipal().getCodigo(),
                        licencaMunicipal.getTipoLicencaMunicipal()
                                .getDescricao());
                historicoTitularidadeCotaPDV.setTipoLicencaMunicipal(tipoLicencaMunicipal);
			}
			
			historicoTitularidadeCotaPDV.setNome(pdv.getNome());
			historicoTitularidadeCotaPDV.setPontoReferencia(pdv.getPontoReferencia());
			historicoTitularidadeCotaPDV.setPorcentagemFaturamento(pdv.getPorcentagemFaturamento());
			historicoTitularidadeCotaPDV.setPossuiSistemaIPV(pdv.isPossuiSistemaIPV());
			historicoTitularidadeCotaPDV.setQtdeFuncionarios(pdv.getQtdeFuncionarios());
			historicoTitularidadeCotaPDV.setSite(pdv.getSite());
			historicoTitularidadeCotaPDV.setStatus(pdv.getStatus());
			historicoTitularidadeCotaPDV.setTamanhoPDV(pdv.getTamanhoPDV());
			historicoTitularidadeCotaPDV.setTelefones(gerarHistoricoTitularidadeCotaTelefonePDV(pdv.getTelefones()));
			
			historicoTitularidadeCotaPDV.setMateriais(
				gerarHistoricoTitularidadeCotaMateriaisPDV(pdv.getMateriais())
			);
			historicoTitularidadeCotaPDV.setPeriodos(
				gerarHistoricoTitularidadeCotaPeriodoFuncionamento(pdv.getPeriodos())
			);
			historicoTitularidadeCotaPDV.setTipoPonto(
				gerarHistoricoTitularidadeCotaTipoPonto(pdv.getTipoEstabelecimentoPDV())
			);
			
			if (pdv.getSegmentacao() != null) {

				historicoTitularidadeCotaPDV.setTipoCaracteristica(pdv.getSegmentacao().getTipoCaracteristica());

				historicoTitularidadeCotaPDV.setAreaInfluencia(
					gerarHistoricoTitularidadeCotaAreaInfluencia(pdv.getSegmentacao().getAreaInfluenciaPDV())
				);
			}

			if (pdv.getGeradorFluxoPDV() != null) {
			
				historicoTitularidadeCotaPDV.setGeradorFluxoPrincipal(
					gerarHistoricoTitularidadeCotaGeradorFluxoPrincipal(pdv.getGeradorFluxoPDV().getPrincipal())
				);
				historicoTitularidadeCotaPDV.setGeradoresFluxoSecundarios(
					gerarHistoricoTitularidadeCotaGeradorFluxoSecundario(pdv.getGeradorFluxoPDV().getSecundarios())
				);
			}
			historicoTitularidadeCotaPDV.setExpositor(pdv.getExpositor());
			historicoTitularidadeCotaPDV.setTipoExpositor(pdv.getTipoExpositor());

			historicoTitularidadeCotaPDV.setHistoricoTitularidadeCota(historicoTitularidadeCota);
			historicosTitularidadeCotaPDV.add(historicoTitularidadeCotaPDV);
		}
		
		return historicosTitularidadeCotaPDV;
	}
	
	/*
	 * Método que gera um histórico para a área de influência do pdv da cota.
	 * 
	 * @param areaInfluenciaPDV - área de influência do PDV.
	 * 
	 * @return HistoricoTitularidadeCotaCodigoDescricao - histórico gerado. 
	 */
	private HistoricoTitularidadeCotaCodigoDescricao gerarHistoricoTitularidadeCotaAreaInfluencia(AreaInfluenciaPDV areaInfluenciaPDV) {
		
		if (areaInfluenciaPDV == null) {
			
			return null;
		}
		
		HistoricoTitularidadeCotaCodigoDescricao historicoAreaInfluencia = 
				new HistoricoTitularidadeCotaCodigoDescricao();
		
		historicoAreaInfluencia.setCodigo(areaInfluenciaPDV.getCodigo());
		historicoAreaInfluencia.setDescricao(areaInfluenciaPDV.getDescricao());
		
		return historicoAreaInfluencia;
	}
	
	/*
	 * Método que gera um histórico para o tipo de ponto do pdv da cota.
	 * 
	 * @param tipoPonto - tipo de ponto do pdv.
	 * 
	 * @return HistoricoTitularidadeCotaCodigoDescricao - histórico gerado. 
	 */
	private HistoricoTitularidadeCotaCodigoDescricao gerarHistoricoTitularidadeCotaTipoPonto(TipoEstabelecimentoAssociacaoPDV tipoPonto) {
		
		if (tipoPonto == null) {
			
			return null;
		}
		
		HistoricoTitularidadeCotaCodigoDescricao historicoTipoPonto = 
				new HistoricoTitularidadeCotaCodigoDescricao();
		
		historicoTipoPonto.setCodigo(tipoPonto.getCodigo());
		historicoTipoPonto.setDescricao(tipoPonto.getDescricao());
		
		return historicoTipoPonto;
	}
	
	/*
	 * Método que gera um histórico para os geradores de fluxo principal do pdv da cota.
	 * 
	 * @param geradorFluxoPrincipal - Gerador de fluxo principal do pdv.
	 * 
	 * @return HistoricoTitularidadeCotaCodigoDescricao - histórico gerado. 
	 */
	private HistoricoTitularidadeCotaCodigoDescricao gerarHistoricoTitularidadeCotaGeradorFluxoPrincipal(TipoGeradorFluxoPDV geradorFluxoPrincipal) {
		
		if (geradorFluxoPrincipal == null) {
			
			return null;
		}
		
		HistoricoTitularidadeCotaCodigoDescricao historicoGeradorFluxoPrincipal = new HistoricoTitularidadeCotaCodigoDescricao();
		
		historicoGeradorFluxoPrincipal.setCodigo(geradorFluxoPrincipal.getCodigo());
		historicoGeradorFluxoPrincipal.setDescricao(geradorFluxoPrincipal.getDescricao());
		
		return historicoGeradorFluxoPrincipal;
	}
	
	/*
	 * Método que gera um histórico para os geradores de fluxo secundários do pdv da cota.
	 * 
	 * @param geradoresFluxoSecundario - Gerador de fluxo secundário do pdv.
	 * 
	 * @return Set<HistoricoTitularidadeCotaCodigoDescricao> - histórico gerado. 
	 */
	private Set<HistoricoTitularidadeCotaCodigoDescricao> gerarHistoricoTitularidadeCotaGeradorFluxoSecundario(Set<TipoGeradorFluxoPDV> geradoresFluxoSecundario) {
		
		if (geradoresFluxoSecundario == null || geradoresFluxoSecundario.isEmpty()) {
			
			return null;
		}
		
		Set<HistoricoTitularidadeCotaCodigoDescricao> historicoGeradoresFluxoSecundario = 
				new HashSet<HistoricoTitularidadeCotaCodigoDescricao>();
		
		for (TipoGeradorFluxoPDV tipoGeradorFluxoPDV : geradoresFluxoSecundario) {

			HistoricoTitularidadeCotaCodigoDescricao historicoGeradorFluxoSecundario = 
					new HistoricoTitularidadeCotaCodigoDescricao();
			
			historicoGeradorFluxoSecundario.setCodigo(tipoGeradorFluxoPDV.getCodigo());
			historicoGeradorFluxoSecundario.setDescricao(tipoGeradorFluxoPDV.getDescricao());

			historicoGeradoresFluxoSecundario.add(historicoGeradorFluxoSecundario);
		}
		
		return historicoGeradoresFluxoSecundario;
	}

	/*
	 * Método que gera um histórico para os materiais do pdv referentes a cota utilizada na alteração de titularidade.
	 * 
	 * @param materiaisPromocionais - Coleção com os materiais referentes ao PDV.
	 * 
	 * @return Set<HistoricoTitularidadeCotaCodigoDescricao> - histórico gerado.
	 */
	private Set<HistoricoTitularidadeCotaCodigoDescricao> gerarHistoricoTitularidadeCotaMateriaisPDV(Set<MaterialPromocional> materiaisPromocionais) {
		
		if (materiaisPromocionais == null || materiaisPromocionais.isEmpty()) {
			
			return null;
		}
		
		Set<HistoricoTitularidadeCotaCodigoDescricao> historicoMateriais = 
				new HashSet<HistoricoTitularidadeCotaCodigoDescricao>();
		
		for (MaterialPromocional materialPromocional : materiaisPromocionais) {
			
			HistoricoTitularidadeCotaCodigoDescricao historicoMaterial = new HistoricoTitularidadeCotaCodigoDescricao();
			
			historicoMaterial.setCodigo(materialPromocional.getCodigo());
			historicoMaterial.setDescricao(materialPromocional.getDescricao());
			
			historicoMateriais.add(historicoMaterial);
		}
		
		return historicoMateriais;
	}
	
	/*
	 * Método que gera um histórico para os periodos de funcionamento de pdv referentes a cota utilizada na alteração de titularidade.
	 * 
	 * @param periodos - Coleção com os periodos de funcionamento do PDV.
	 * 
	 * @return Set<HistoricoTitularidadeCotaFuncionamentoPDV> - histórico gerado.
	 */
	private Set<HistoricoTitularidadeCotaFuncionamentoPDV> gerarHistoricoTitularidadeCotaPeriodoFuncionamento(Set<PeriodoFuncionamentoPDV> periodos) {
		
		if (periodos == null || periodos.isEmpty()) {
			
			return null;
		}
		
		Set<HistoricoTitularidadeCotaFuncionamentoPDV> listaFuncionamento = 
				new HashSet<HistoricoTitularidadeCotaFuncionamentoPDV>();
		
		for (PeriodoFuncionamentoPDV periodo : periodos) {
			
			HistoricoTitularidadeCotaFuncionamentoPDV historicoPeriodo = 
					new HistoricoTitularidadeCotaFuncionamentoPDV();
			
			historicoPeriodo.setHorarioFim(periodo.getHorarioFim());
			historicoPeriodo.setHorarioInicio(periodo.getHorarioInicio());
			historicoPeriodo.setTipoPeriodoFuncionamentoPDV(periodo.getTipoPeriodoFuncionamentoPDV());
			
			listaFuncionamento.add(historicoPeriodo);
		}
		
		return listaFuncionamento;
	}
	
	/*
	 * Método que gera um histórico para os endereços de pdv referentes a cota utilizada na alteração de titularidade.
	 * 
	 * @param enderecosPDV - Coleção com os enderecos do PDV.
	 * 
	 * @return Set<HistoricoTitularidadeCotaEndereco> - histórico gerado.
	 */
	private Set<HistoricoTitularidadeCotaEndereco> gerarHistoricoTitularidadeCotaEnderecoPDV(Set<EnderecoPDV> enderecosPDV) {
		
		if (enderecosPDV == null || enderecosPDV.isEmpty()) {
			
			return null;
		}
		
		Set<HistoricoTitularidadeCotaEndereco> historicosTitularidadeCotaEndereco = 
				new HashSet<HistoricoTitularidadeCotaEndereco>();
		
		for (EnderecoPDV enderecoPDV : enderecosPDV) {
			
			HistoricoTitularidadeCotaEndereco historicoTitularidadeCotaEndereco = 
					gerarHistoricoTitularidadeCotaEndereco(enderecoPDV.getEndereco());
			
			historicoTitularidadeCotaEndereco.setPrincipal(enderecoPDV.isPrincipal());
			historicoTitularidadeCotaEndereco.setTipoEndereco(enderecoPDV.getTipoEndereco());
			
			historicosTitularidadeCotaEndereco.add(historicoTitularidadeCotaEndereco);
		}
		
		return historicosTitularidadeCotaEndereco;
	}
	
	/*
	 * Método que gera um histórico para os telefones de pdv referentes a cota utilizada na alteração de titularidade.
	 * 
	 * @param telefonesPDV - Coleção com os telefones do PDV.
	 * 
	 * @return Set<HistoricoTitularidadeCotaTelefone> - histórico gerado.
	 */
	private Set<HistoricoTitularidadeCotaTelefone> gerarHistoricoTitularidadeCotaTelefonePDV(Set<TelefonePDV> telefonesPDV) {
		
		if (telefonesPDV == null || telefonesPDV.isEmpty()) {
			
			return null;
		}

		Set<HistoricoTitularidadeCotaTelefone> historicosCotaTelefone = new HashSet<HistoricoTitularidadeCotaTelefone>();

		for (TelefonePDV telefonePDV : telefonesPDV) {

			HistoricoTitularidadeCotaTelefone historicoTitularidadeCotaTelefone = 
					gerarHistoricoTitularidadeCotaTelefone(telefonePDV.getTelefone());
			
			historicoTitularidadeCotaTelefone.setTipoTelefone(telefonePDV.getTipoTelefone());
			historicoTitularidadeCotaTelefone.setPrincipal(telefonePDV.isPrincipal());

			historicosCotaTelefone.add(historicoTitularidadeCotaTelefone);
		}

		return historicosCotaTelefone;
	}
	
	/*
	 * Método que gera um histórico para os socios referentes a cota utilizada na alteração de titularidade.
	 * 
	 * @param sociosCota - Coleção com os sócios da cota.
	 * 
	 * @return Set<HistoricoTitularidadeCotaSocio> - histórico gerado.
	 */
	private Set<HistoricoTitularidadeCotaSocio> gerarHistoricoTitularidadeCotaSocio(Set<SocioCota> sociosCota) {
		
		if (sociosCota == null || sociosCota.isEmpty()) {
			
			return null;
		}
		
		Set<HistoricoTitularidadeCotaSocio> historicosTitularidadeCotaSocio = new HashSet<HistoricoTitularidadeCotaSocio>();
		
		for (SocioCota socioCota : sociosCota) {
		
			HistoricoTitularidadeCotaSocio historicoTitularidadeCotaSocio = new HistoricoTitularidadeCotaSocio();
			
			historicoTitularidadeCotaSocio.setCargo(socioCota.getCargo());
			historicoTitularidadeCotaSocio.setNome(socioCota.getNome());
			historicoTitularidadeCotaSocio.setPrincipal(socioCota.getPrincipal());
			historicoTitularidadeCotaSocio.setEndereco(gerarHistoricoTitularidadeCotaEndereco(socioCota.getEndereco()));
			historicoTitularidadeCotaSocio.setTelefone(gerarHistoricoTitularidadeCotaTelefone(socioCota.getTelefone()));
			
			historicosTitularidadeCotaSocio.add(historicoTitularidadeCotaSocio);
		}
		
		return historicosTitularidadeCotaSocio;
	}

	/*
	 * Método que gera um histórico referente aos dados de distribuição da cota.
	 * 
	 * @param parametroDistribuicaoCota - Parametro de distribuição da cota atual.
	 * 
	 * @return HistoricoTitularidadeCotaDistribuicao - histórico gerado. 
	 */
	private HistoricoTitularidadeCotaDistribuicao gerarHistoricoTitularidadeCotaDistribuicao(ParametroDistribuicaoCota parametroDistribuicaoCota) {

		if (parametroDistribuicaoCota == null) {

			return null;
		}

		HistoricoTitularidadeCotaDistribuicao historicoTitularidadeCotaDistribuicao = new HistoricoTitularidadeCotaDistribuicao();

		historicoTitularidadeCotaDistribuicao.setAssistenteComercial(parametroDistribuicaoCota.getAssistenteComercial());
		historicoTitularidadeCotaDistribuicao.setGerenteComercial(parametroDistribuicaoCota.getGerenteComercial());
		historicoTitularidadeCotaDistribuicao.setChamadaEncalheEmail(parametroDistribuicaoCota.getChamadaEncalheEmail());
		historicoTitularidadeCotaDistribuicao.setChamadaEncalheImpresso(parametroDistribuicaoCota.getChamadaEncalheImpresso());
		historicoTitularidadeCotaDistribuicao.setNotaEnvioEmail(parametroDistribuicaoCota.getNotaEnvioEmail());
		historicoTitularidadeCotaDistribuicao.setNotaEnvioImpresso(parametroDistribuicaoCota.getNotaEnvioImpresso());
		historicoTitularidadeCotaDistribuicao.setObservacao(parametroDistribuicaoCota.getObservacao());
		historicoTitularidadeCotaDistribuicao.setQtdePDV(parametroDistribuicaoCota.getQtdePDV());
		historicoTitularidadeCotaDistribuicao.setRecebeRecolheParcias(parametroDistribuicaoCota.getRecebeRecolheParciais());
		historicoTitularidadeCotaDistribuicao.setEntregaReparteVenda(parametroDistribuicaoCota.getRepartePorPontoVenda());
		historicoTitularidadeCotaDistribuicao.setSlipEmail(parametroDistribuicaoCota.getSlipEmail());
		historicoTitularidadeCotaDistribuicao.setSlipImpresso(parametroDistribuicaoCota.getSlipImpresso());
		historicoTitularidadeCotaDistribuicao.setBoletoImpresso(parametroDistribuicaoCota.getBoletoImpresso());
		historicoTitularidadeCotaDistribuicao.setBoletoEmail(parametroDistribuicaoCota.getBoletoEmail());
		historicoTitularidadeCotaDistribuicao.setBoletoSlipImpresso(parametroDistribuicaoCota.getBoletoSlipImpresso());
		historicoTitularidadeCotaDistribuicao.setBoletoSlipEmail(parametroDistribuicaoCota.getBoletoSlipEmail());
		historicoTitularidadeCotaDistribuicao.setReciboImpresso(parametroDistribuicaoCota.getReciboImpresso());
		historicoTitularidadeCotaDistribuicao.setReciboEmail(parametroDistribuicaoCota.getReciboEmail());
		historicoTitularidadeCotaDistribuicao.setSolicitaNumAtrasados(parametroDistribuicaoCota.getSolicitaNumAtras());
		historicoTitularidadeCotaDistribuicao.setPossuiProcuracao(parametroDistribuicaoCota.getUtilizaProcuracao());
		historicoTitularidadeCotaDistribuicao.setProcuracaoAssinada(parametroDistribuicaoCota.getProcuracaoRecebida());
		historicoTitularidadeCotaDistribuicao.setPossuiTermoAdesao(parametroDistribuicaoCota.getUtilizaTermoAdesao());
		historicoTitularidadeCotaDistribuicao.setTermoAdesaoAssinado(parametroDistribuicaoCota.getTermoAdesaoRecebido());
		historicoTitularidadeCotaDistribuicao.setInicioPeriodoCarencia(parametroDistribuicaoCota.getInicioPeriodoCarencia());
		historicoTitularidadeCotaDistribuicao.setFimPeriodoCarencia(parametroDistribuicaoCota.getFimPeriodoCarencia());
		historicoTitularidadeCotaDistribuicao.setTipoEntrega(parametroDistribuicaoCota.getDescricaoTipoEntrega());

		return historicoTitularidadeCotaDistribuicao;
	}
	
	/*
	 * Método para geração de histórico referente a descontos da cota e de produtos, a partir de dados da cota atual.
	 * 
	 * @param idCota - Identificador da cota atual.
	 * 
	 * @param numeroCota - Número da cota atual.
	 * 
	 * @return List<HistoricoTitularidadeCotaDesconto> - lista com o histórico gerado.
	 */
	private List<HistoricoTitularidadeCotaDesconto> gerarHistoricoTitularidadeCotaDesconto(HistoricoTitularidadeCota historicoTitularidadeCota, Long idCota, Integer numeroCota) {

		List<HistoricoTitularidadeCotaDesconto> listaDesconto = new ArrayList<HistoricoTitularidadeCotaDesconto>();
		
		List<HistoricoTitularidadeCotaDescontoProduto> descontosProduto = gerarHistoricoTitularidadeCotaDescontoProduto(historicoTitularidadeCota, idCota);
		List<HistoricoTitularidadeCotaDescontoCota> descontosCota = gerarHistoricoTitularidadeCotaDescontoCota(historicoTitularidadeCota, numeroCota);
		
		if (descontosProduto != null) {
		
			listaDesconto.addAll(descontosProduto);
		}
		
		if (descontosCota != null) {
		
			listaDesconto.addAll(descontosCota);
		}
		
		return listaDesconto;
	}

	/*
	 * Método para geração de histórico referente a descontos de produtos, através da cota.
	 * 
	 * @param idCota - Identificador da cota atual.
	 * 
	 * @return List<HistoricoTitularidadeCotaDescontoProduto> - lista com o histórico gerado.
	 */
	private List<HistoricoTitularidadeCotaDescontoProduto> gerarHistoricoTitularidadeCotaDescontoProduto(HistoricoTitularidadeCota historicoTitularidadeCota, Long idCota) {

		List<TipoDescontoProdutoDTO> listaDescontoProduto =  this.descontoProdutoRepository.obterTiposDescontoProdutoPorCota(idCota, null, null);
		
		if (listaDescontoProduto == null || listaDescontoProduto.isEmpty()) {
			
			return null;
		}
		
		List<HistoricoTitularidadeCotaDescontoProduto> listaCotaDescontoProduto = 
				new ArrayList<HistoricoTitularidadeCotaDescontoProduto>();

		for (TipoDescontoProdutoDTO descontoProduto : listaDescontoProduto) {
			
			HistoricoTitularidadeCotaDescontoProduto historicoTitularidadeCotaDescontoProduto =
					new HistoricoTitularidadeCotaDescontoProduto();
			
			historicoTitularidadeCotaDescontoProduto.setCodigo(descontoProduto.getCodigoProduto());
			historicoTitularidadeCotaDescontoProduto.setNome(descontoProduto.getNomeProduto());
			historicoTitularidadeCotaDescontoProduto.setDesconto(descontoProduto.getDesconto());
			historicoTitularidadeCotaDescontoProduto.setAtualizacao(descontoProduto.getDataAlteracao());
			historicoTitularidadeCotaDescontoProduto.setNumeroEdicao(descontoProduto.getNumeroEdicao());
			historicoTitularidadeCotaDescontoProduto.setHistoricoTitularidadeCota(historicoTitularidadeCota);
			
			listaCotaDescontoProduto.add(historicoTitularidadeCotaDescontoProduto);
		}
		
		return listaCotaDescontoProduto;
	}
	
	/*
	 * Método para geração de histórico referente a descontos da cota.
	 * 
	 * @param numeroCota - Número da cota atual.
	 * 
	 * @return List<HistoricoTitularidadeCotaDescontoCota> - lista com o histórico gerado.
	 */
	private List<HistoricoTitularidadeCotaDescontoCota> gerarHistoricoTitularidadeCotaDescontoCota(HistoricoTitularidadeCota historicoTitularidadeCota, Integer numeroCota) {

		FiltroTipoDescontoCotaDTO filtro = new FiltroTipoDescontoCotaDTO();
		
		filtro.setNumeroCota(numeroCota);
		
		List<TipoDescontoCotaDTO> listaDescontoCota = this.descontoCotaRepository.obterDescontoCota(filtro);
		
		if (listaDescontoCota == null || listaDescontoCota.isEmpty()) {
			
			return null;
		}
		
		List<HistoricoTitularidadeCotaDescontoCota> listaHistoricoDescontoCota = 
				new ArrayList<HistoricoTitularidadeCotaDescontoCota>();

		for (TipoDescontoCotaDTO tipoDescontoCotaDTO : listaDescontoCota) {
			
			HistoricoTitularidadeCotaDescontoCota historicoTitularidadeCotaDescontoCota = new HistoricoTitularidadeCotaDescontoCota();
			
			historicoTitularidadeCotaDescontoCota.setTipoDesconto(TipoDesconto.ESPECIFICO);
			historicoTitularidadeCotaDescontoCota.setFornecedor(tipoDescontoCotaDTO.getFornecedor());
			historicoTitularidadeCotaDescontoCota.setDesconto(tipoDescontoCotaDTO.getDesconto());
			historicoTitularidadeCotaDescontoCota.setAtualizacao(tipoDescontoCotaDTO.getDataAlteracao());
			historicoTitularidadeCotaDescontoCota.setHistoricoTitularidadeCota(historicoTitularidadeCota);
			listaHistoricoDescontoCota.add(historicoTitularidadeCotaDescontoCota);
		}

		return listaHistoricoDescontoCota;
	}
	
	/*
	 * Método genérico para geração de histórico de garantia da cota.
	 * 
	 * A partir do tipo de garantia que a cota possui, será gerado um Histórico específico para o mesmo.
	 * 
	 * @param cotaGarantia - Garantia atual da cota.
	 * 
	 * @return List<? extends HistoricoTitularidadeCotaGarantia> - Uma lista com os dados do histórico da garantia, baseado em seu tipo. 
	 */
	private List<? extends HistoricoTitularidadeCotaGarantia> gerarHistoricoTitularidadeCotaGarantias(HistoricoTitularidadeCota historicoTitularidadeCota, CotaGarantia cotaGarantia) {

		if (cotaGarantia == null) {

			return null;
		
		} else if (cotaGarantia instanceof CotaGarantiaCaucaoLiquida) {
			
			CotaGarantiaCaucaoLiquida caucaoLiquida = (CotaGarantiaCaucaoLiquida) cotaGarantia;
			
			return gerarHistoricoTitularidadeCotaCaucaoLiquida(historicoTitularidadeCota, caucaoLiquida);
		
		} else if (cotaGarantia instanceof CotaGarantiaChequeCaucao) {
			
			CotaGarantiaChequeCaucao chequeCaucao = (CotaGarantiaChequeCaucao) cotaGarantia;

			List<HistoricoTitularidadeCotaChequeCaucao> listaChequeCaucao = new ArrayList<HistoricoTitularidadeCotaChequeCaucao>();
			
			listaChequeCaucao.add(gerarHistoricoTitularidadeCotaChequeCaucao(historicoTitularidadeCota, chequeCaucao.getCheque()));
			
			return listaChequeCaucao;
			
		} else if (cotaGarantia instanceof CotaGarantiaFiador) {
			
			CotaGarantiaFiador fiador = (CotaGarantiaFiador) cotaGarantia;
			 
			List<HistoricoTitularidadeCotaFiador> listaFiador = new ArrayList<HistoricoTitularidadeCotaFiador>();
			
			listaFiador.add(gerarHistoricoTitularidadeCotaFiador(historicoTitularidadeCota, fiador.getFiador()));
			
			return listaFiador;

		} else if (cotaGarantia instanceof CotaGarantiaImovel) {
			
			CotaGarantiaImovel imovel = (CotaGarantiaImovel) cotaGarantia;
			
			return gerarHistoricoTitularidadeCotaImovel(historicoTitularidadeCota, imovel.getImoveis());
		
		} else if (cotaGarantia instanceof CotaGarantiaNotaPromissoria) {
			
			CotaGarantiaNotaPromissoria notaPromissoria = (CotaGarantiaNotaPromissoria) cotaGarantia;
			
			List<HistoricoTitularidadeCotaNotaPromissoria> listaNotaPromissoria = 
					new ArrayList<HistoricoTitularidadeCotaNotaPromissoria>();
			
			listaNotaPromissoria.add(gerarHistoricoTitularidadeCotaNotaPromissorias(historicoTitularidadeCota, notaPromissoria.getNotaPromissoria()));
			
			return listaNotaPromissoria;
		}

		return null;
	}

	/*
	 * Método que gera um histórico para o fiador referente a cota utilizada na alteração de titularidade.
	 * 
	 * @param Fiador - fiador associado a cota.
	 * 
	 * @return HistoricoTitularidadeCotaFiador - histórico gerado.
	 */
	private HistoricoTitularidadeCotaFiador gerarHistoricoTitularidadeCotaFiador(HistoricoTitularidadeCota historicoTitularidadeCota, Fiador fiador) {
		
		if (fiador == null) {
			
			return null;
		} 

		HistoricoTitularidadeCotaFiador historicoTitularidadeCotaFiador = new HistoricoTitularidadeCotaFiador();
		
		if (fiador.getPessoa() != null) {

			historicoTitularidadeCotaFiador.setCpfCnpj(fiador.getPessoa().getDocumento());
			historicoTitularidadeCotaFiador.setNome(fiador.getPessoa().getNome());
		}

		historicoTitularidadeCotaFiador.setGarantias(gerarHistoricoTitularidadeCotaFiadorGarantia(fiador.getGarantias()));

		if (fiador.getEnderecoFiador() != null && !fiador.getEnderecoFiador().isEmpty()) {
			
			for (EnderecoFiador enderecoFiador : fiador.getEnderecoFiador()) {
				
				if (enderecoFiador.isPrincipal()) {
					
					historicoTitularidadeCotaFiador.setHistoricoTitularidadeCotaEndereco(
						gerarHistoricoTitularidadeCotaEndereco(enderecoFiador.getEndereco())
					);
				}
			}
		}
		
		if (fiador.getTelefonesFiador() != null && !fiador.getTelefonesFiador().isEmpty()) {
			
			for (TelefoneFiador telefoneFiador : fiador.getTelefonesFiador()) {
				
				if (telefoneFiador.isPrincipal()) {
					
					historicoTitularidadeCotaFiador.setHistoricoTitularidadeCotaTelefone(
						gerarHistoricoTitularidadeCotaTelefone(telefoneFiador.getTelefone())
					);
				}
			}
		}
		historicoTitularidadeCotaFiador.setHistoricoTitularidadeCota(historicoTitularidadeCota);
		
		return historicoTitularidadeCotaFiador;
	}
	
	/*
	 * Método que gera um histórico para as garantias do fiador referente a cota utilizada na alteração de titularidade.
	 * 
	 * @param List<Garantia> - garantias associadas ao Fiador.
	 * 
	 * @return List<HistoricoTitularidadeCotaFiadorGarantia> - lista com o histórico gerado.
	 */
	private List<HistoricoTitularidadeCotaFiadorGarantia> gerarHistoricoTitularidadeCotaFiadorGarantia(List<Garantia> garantias) {
		
		if (garantias == null) {
			
			return null;
		}
		
		List<HistoricoTitularidadeCotaFiadorGarantia> historicosTitularidadeCotaFiadorGarantia = 
				new ArrayList<HistoricoTitularidadeCotaFiadorGarantia>();
		
		for (Garantia garantia : garantias) {
			
			HistoricoTitularidadeCotaFiadorGarantia historicoTitularidadeCotaFiadorGarantia =
					new HistoricoTitularidadeCotaFiadorGarantia();
			
			historicoTitularidadeCotaFiadorGarantia.setDescricao(garantia.getDescricao());
			historicoTitularidadeCotaFiadorGarantia.setValor(garantia.getValor());
			
			historicosTitularidadeCotaFiadorGarantia.add(historicoTitularidadeCotaFiadorGarantia);
		}
		
		return historicosTitularidadeCotaFiadorGarantia;
	}

	/*
	 * Método que gera um histórico de garantias do tipo CaucaoLiquida
	 * 
	 * @param caucaoLiquida - Caução líquida
	 *  
	 * @return List<HistoricoTitularidadeCotaCaucaoLiquida> - lista com o histórico gerado.
	 */
	private List<HistoricoTitularidadeCotaCaucaoLiquida> gerarHistoricoTitularidadeCotaCaucaoLiquida(HistoricoTitularidadeCota historicoTitularidadeCota, CotaGarantiaCaucaoLiquida caucaoLiquida) {
		
		List<HistoricoTitularidadeCotaCaucaoLiquida> historicosTitularidadeCotaCaucaoLiquida = new ArrayList<HistoricoTitularidadeCotaCaucaoLiquida>();

		HistoricoTitularidadeCotaCaucaoLiquida historico = new HistoricoTitularidadeCotaCaucaoLiquida();
		
		ContaBancariaDeposito contaDeposito = caucaoLiquida.getContaBancariaDeposito();
        if (contaDeposito != null) {
            historico.setContaBancariaDeposito(new ContaBancariaDeposito(
                    contaDeposito.getNumeroBanco(), contaDeposito.getNomeBanco(),
                    contaDeposito.getAgencia(), contaDeposito.getDvAgencia(),
                    contaDeposito.getConta(), contaDeposito.getDvConta(),
                    contaDeposito.getNomeCorrentista()));
        }
        
        PagamentoCaucaoLiquida fp = caucaoLiquida.getFormaPagamento();
        if (fp != null) {
            historico.setValor(fp.getValor());
            HistoricoTitularidadeCotaPagamentoCaucaoLiquida historicoPagamento = new HistoricoTitularidadeCotaPagamentoCaucaoLiquida();
            historicoPagamento.setTipoCobranca(caucaoLiquida.getTipoCobranca());
            historico.setPagamento(historicoPagamento);
            if (fp instanceof PagamentoBoleto) {
                PagamentoBoleto fpBoleto = PagamentoBoleto.class.cast(fp);
                FormaCobrancaCaucaoLiquida cobrancaCaucaoLiquida = fpBoleto.getFormaCobrancaCaucaoLiquida();
                historicoPagamento.setPeriodicidadeBoleto(cobrancaCaucaoLiquida.getTipoFormaCobranca());
                historicoPagamento.setQtdeParcelasBoleto(fpBoleto.getQuantidadeParcelas());
                historicoPagamento.setValorParcelasBoleto(fpBoleto.getValorParcela());
                List<Integer> diasMes = cobrancaCaucaoLiquida.getDiasDoMes();
                if (diasMes != null) {
                    for (Integer diaMes : diasMes) {
                        historicoPagamento.addDiaMesBoleto(diaMes);
                    }
                }
                Set<ConcentracaoCobrancaCaucaoLiquida> concentracoes = cobrancaCaucaoLiquida.getConcentracaoCobrancaCaucaoLiquida();
                if (concentracoes != null) {
                    for (ConcentracaoCobrancaCaucaoLiquida concentracao : concentracoes) {
                        historicoPagamento.addDiaSemanaBoleto(concentracao.getDiaSemana());
                    }
                }
            } else if (fp instanceof PagamentoDescontoCota) {
                PagamentoDescontoCota fpDesconto = PagamentoDescontoCota.class.cast(fp);
                historicoPagamento.setDescontoNormal(fpDesconto.getDescontoAtual());
                historicoPagamento.setDescontoReduzido(fpDesconto.getDescontoCota());
                historicoPagamento.setPorcentagemUtilizada(fpDesconto.getPorcentagemUtilizada());
            }
        }
        historico.setHistoricoTitularidadeCota(historicoTitularidadeCota);
        historicosTitularidadeCotaCaucaoLiquida.add(historico);
        
		return historicosTitularidadeCotaCaucaoLiquida;
	}
	
	/*
	 * Método que gera um histórico de garantias do tipo ChequeCaucao 
	 * 
	 * @param cheque - cheque que a cota possui em sua garantia.
	 *  
	 * @return HistoricoTitularidadeCotaChequeCaucao - histórico gerado.
	 */
	private HistoricoTitularidadeCotaChequeCaucao gerarHistoricoTitularidadeCotaChequeCaucao(HistoricoTitularidadeCota historicoTitularidadeCota, Cheque cheque) {
		
		if (cheque == null) {
			
			return null;
		}
		
		HistoricoTitularidadeCotaChequeCaucao historicoTitularidadeCotaChequeCaucao = 
				new HistoricoTitularidadeCotaChequeCaucao();
		
		historicoTitularidadeCotaChequeCaucao.setAgencia(cheque.getAgencia());
		historicoTitularidadeCotaChequeCaucao.setConta(cheque.getConta());
		historicoTitularidadeCotaChequeCaucao.setCorrentista(cheque.getCorrentista());
		historicoTitularidadeCotaChequeCaucao.setDvAgencia(cheque.getDvAgencia());
		historicoTitularidadeCotaChequeCaucao.setDvConta(cheque.getDvConta());
		historicoTitularidadeCotaChequeCaucao.setNomeBanco(cheque.getNomeBanco());
		historicoTitularidadeCotaChequeCaucao.setNumeroBanco(cheque.getNumeroBanco());
		historicoTitularidadeCotaChequeCaucao.setNumeroCheque(cheque.getNumeroCheque());

		if (cheque.getEmissao() != null) {
		
			historicoTitularidadeCotaChequeCaucao.setEmissao(cheque.getEmissao());
		}

		if (cheque.getValidade() != null) {
		
			historicoTitularidadeCotaChequeCaucao.setValidade(cheque.getValidade());
		}
		
		if (cheque.getChequeImage() != null) {
		
			historicoTitularidadeCotaChequeCaucao.setImagem(cheque.getChequeImage().getImagem());
		}
		
		if (cheque.getValor() != null) {
		
			historicoTitularidadeCotaChequeCaucao.setValor(cheque.getValor());
		}
		historicoTitularidadeCotaChequeCaucao.setHistoricoTitularidadeCota(historicoTitularidadeCota);

		return historicoTitularidadeCotaChequeCaucao;
	}

	/*
	 * Método que gera um histórico de garantias do tipo Imovel 
	 * 
	 * @param imoveis - lista de imoveis que a cota possui em sua garantia.
	 *  
	 * @return List<HistoricoTitularidadeCotaImovel> - lista com o histórico gerado.
	 */
	private List<HistoricoTitularidadeCotaImovel> gerarHistoricoTitularidadeCotaImovel(HistoricoTitularidadeCota historicoTitularidadeCota, List<Imovel> imoveis) {
		
		if (imoveis == null || imoveis.isEmpty()) {

			return null;
		}
		
		List<HistoricoTitularidadeCotaImovel> listaHistoricoTitularidadeCotaImovel = 
				new ArrayList<HistoricoTitularidadeCotaImovel>();
		
		for (Imovel imovel : imoveis) {

			HistoricoTitularidadeCotaImovel historicoTitularidadeCotaImovel =
					new HistoricoTitularidadeCotaImovel();
			
			historicoTitularidadeCotaImovel.setEndereco(imovel.getEndereco());
			historicoTitularidadeCotaImovel.setNumeroRegistro(imovel.getNumeroRegistro());
			historicoTitularidadeCotaImovel.setObservacao(imovel.getObservacao());
			historicoTitularidadeCotaImovel.setProprietario(imovel.getProprietario());
			
			if (imovel.getValor() != null) {
			
				historicoTitularidadeCotaImovel.setValor(imovel.getValor());
			}

			historicoTitularidadeCotaImovel.setHistoricoTitularidadeCota(historicoTitularidadeCota);
			listaHistoricoTitularidadeCotaImovel.add(historicoTitularidadeCotaImovel);
		}
		
		return listaHistoricoTitularidadeCotaImovel;
	}
	
	/*
	 * Método que gera um histórico de garantias do tipo NotaPromissoria 
	 * 
	 * @param notaPromissoria - nota promissoria que a cota possui em sua garantia.
	 *  
	 * @return HistoricoTitularidadeCotaNotaPromissoria - histórico gerado.
	 */
	private HistoricoTitularidadeCotaNotaPromissoria gerarHistoricoTitularidadeCotaNotaPromissorias(HistoricoTitularidadeCota historicoTitularidadeCota, NotaPromissoria notaPromissoria) {

		if (notaPromissoria == null) {
			
			return null;
		}
		
		HistoricoTitularidadeCotaNotaPromissoria historicoTitularidadeCotaNotaPromissoria = 
				new HistoricoTitularidadeCotaNotaPromissoria();

		historicoTitularidadeCotaNotaPromissoria.setValorExtenso(notaPromissoria.getValorExtenso());
		
		if (notaPromissoria.getValor() != null) {
		
			historicoTitularidadeCotaNotaPromissoria.setValor(notaPromissoria.getValor());
		}

		if (notaPromissoria.getVencimento() != null) {
		
			historicoTitularidadeCotaNotaPromissoria.setVencimento(notaPromissoria.getVencimento());
		}
		historicoTitularidadeCotaNotaPromissoria.setHistoricoTitularidadeCota(historicoTitularidadeCota);

		return historicoTitularidadeCotaNotaPromissoria;
	}

	/*
	 * Método que gera um histórico de referencias de cota base. 
	 * 
	 * @param referencias - referencias de cota base que a cota possui em sua garantia.
	 *  
	 * @return Set<HistoricoTitularidadeCotaReferenciaCota> - histórico gerado.
	 */
	private Set<HistoricoTitularidadeCotaReferenciaCota> gerarHistoricoTitularidadeCotaReferenciaCota(Set<ReferenciaCota> referencias) {
		
		if (referencias == null) {
			
			return null;
		}
		
		Set<HistoricoTitularidadeCotaReferenciaCota> historicoReferenciasCota = new HashSet<HistoricoTitularidadeCotaReferenciaCota>();
		
		for (ReferenciaCota referenciaCota : referencias) {
			
			HistoricoTitularidadeCotaReferenciaCota historicoReferenciaCota = 
					new HistoricoTitularidadeCotaReferenciaCota();
			
			historicoReferenciaCota.setPercentual(referenciaCota.getPercentual());

			if (referenciaCota.getCota() != null) {
			
				historicoReferenciaCota.setNumeroCota(referenciaCota.getCota().getNumeroCota());
			}
			
			historicoReferenciasCota.add(historicoReferenciaCota);
		}
		
		return historicoReferenciasCota;
	}
	
	/*
	 * Método que gera um histórico da parte financeiro da cota. 
	 * 
	 * @param parametroCobrancaCota - dados referentes a parte financeira da cota.
	 *  
	 * @return HistoricoTitularidadeCotaFinanceiro - histórico gerado.
	 */
	private HistoricoTitularidadeCotaFinanceiro gerarHistoricoTitularidadeCotaFinanceiro(ParametroCobrancaCota parametroCobrancaCota) {
		
		if (parametroCobrancaCota == null) {
			
			return null;
		}

		Cota cota = parametroCobrancaCotaRepository.obterCotaPorParametroCobranca(parametroCobrancaCota);
		HistoricoTitularidadeCotaFinanceiro historicoTitularidadeCotaFinanceiro = new HistoricoTitularidadeCotaFinanceiro();
		
		historicoTitularidadeCotaFinanceiro.setFatorVencimento(parametroCobrancaCota.getFatorVencimento());
		historicoTitularidadeCotaFinanceiro.setPoliticaSuspensao(cota.getPoliticaSuspensao());
		historicoTitularidadeCotaFinanceiro.setValorMininoCobranca(cota.getValorMinimoCobranca());
		historicoTitularidadeCotaFinanceiro.setFormasPagamento(gerarHistoricoTitularidadeCotaFormaPagamentos(parametroCobrancaCota.getFormasCobrancaCota()));
		
		return historicoTitularidadeCotaFinanceiro;
	}
	
	/*
	 * Método que gera um histórico das formas de pagamento. 
	 * 
	 * @param formasCobranca - formas de cobrança.
	 *  
	 * @return Set<HistoricoTitularidadeCotaFormaPagamento> - histórico gerado.
	 */
	private Set<HistoricoTitularidadeCotaFormaPagamento> gerarHistoricoTitularidadeCotaFormaPagamentos(Set<FormaCobranca> formasCobranca) {

		if (formasCobranca == null) {

			return null;
		}

		Set<HistoricoTitularidadeCotaFormaPagamento> historicoFormasPagamento =
				new HashSet<HistoricoTitularidadeCotaFormaPagamento>();
		
		for (FormaCobranca formaCobranca : formasCobranca) {

			HistoricoTitularidadeCotaFormaPagamento historicoFormaPagamento =
					new HistoricoTitularidadeCotaFormaPagamento();
	
			historicoFormaPagamento.setTipoCobranca(formaCobranca.getTipoCobranca());
			historicoFormaPagamento.setBanco(gerarHistoricoTitularidadeCotaBanco(formaCobranca.getBanco()));
			historicoFormaPagamento.setFornecedores(gerarHistoricoTitularidadeCotaFornecedor(formaCobranca.getFornecedores()));
			historicoFormaPagamento.setConcentracaoCobranca(gerarHistoricoTitularidadeCotaConcentracaoCobranca(formaCobranca.getConcentracaoCobrancaCota()));
		
			historicoFormasPagamento.add(historicoFormaPagamento);
		}
		
		return historicoFormasPagamento;
	}
	
	/*
	 * Método que gera um histórico de bancos. 
	 * 
	 * @param banco - banco.
	 *  
	 * @return HistoricoTitularidadeCotaBanco - histórico gerado.
	 */
	private HistoricoTitularidadeCotaBanco gerarHistoricoTitularidadeCotaBanco(Banco banco) {
		
		if (banco == null) {
			
			return null;
		}
		
		HistoricoTitularidadeCotaBanco historicoTitularidadeCotaBanco = new HistoricoTitularidadeCotaBanco();
		
		historicoTitularidadeCotaBanco.setAgencia(banco.getAgencia());
		historicoTitularidadeCotaBanco.setConta(banco.getConta());
		historicoTitularidadeCotaBanco.setDvAgencia(banco.getDvAgencia());
		historicoTitularidadeCotaBanco.setDvConta(banco.getDvConta());
		historicoTitularidadeCotaBanco.setNome(banco.getNome());
		historicoTitularidadeCotaBanco.setNumeroBanco(banco.getNumeroBanco());
		
		return historicoTitularidadeCotaBanco;
	}
	
	/*
	 * Método que gera um histórico de Concentrações de cobrança. 
	 * 
	 * @param concentracoesCobrancaCota - Coleção com as concentrações de cobrança da cota.
	 *  
	 * @return HistoricoTitularidadeCotaConcentracaoCobranca - histórico gerado.
	 */
	private HistoricoTitularidadeCotaConcentracaoCobranca gerarHistoricoTitularidadeCotaConcentracaoCobranca(
																	Set<ConcentracaoCobrancaCota> concentracoesCobrancaCota) {
		
		if (concentracoesCobrancaCota == null || concentracoesCobrancaCota.isEmpty()) {
			
			return null;
		}
		
		HistoricoTitularidadeCotaConcentracaoCobranca historicoConcentracaoCobranca = new HistoricoTitularidadeCotaConcentracaoCobranca();
		
		List<Integer> diasDoMes = new ArrayList<Integer>();
		List<DiaSemana> diasSemana = new ArrayList<DiaSemana>();
		
		for (ConcentracaoCobrancaCota concentracaoCobranca : concentracoesCobrancaCota) {
			
			diasSemana.add(concentracaoCobranca.getDiaSemana());

			FormaCobranca formaCobranca = concentracaoCobranca.getFormaCobranca();

			if (formaCobranca != null) {
			
				diasDoMes.addAll(formaCobranca.getDiasDoMes());
				
				historicoConcentracaoCobranca.setTipoFormaCobranca(formaCobranca.getTipoFormaCobranca());
			}
		}
		
		return historicoConcentracaoCobranca;
	}
}
