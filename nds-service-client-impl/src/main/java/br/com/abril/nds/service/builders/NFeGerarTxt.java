package br.com.abril.nds.service.builders;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.fiscal.nota.DetalheNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.Identificacao;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoDestinatario;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente;
import br.com.abril.nds.model.fiscal.nota.InformacaoTransporte;
import br.com.abril.nds.model.fiscal.nota.InformacaoValoresTotais;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscalReferenciada;
import br.com.abril.nds.model.fiscal.nota.PISOutrWrapper;
import br.com.abril.nds.model.fiscal.nota.PISWrapper;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalFatura;
import br.com.abril.nds.model.fiscal.notafiscal.NotaFiscalValorCalculado;
import br.com.abril.nds.util.Util;

@Component
public class NFeGerarTxt {
	 public static StringBuilder gerarNfeTxt(NotaFiscal novaNfe) throws Exception {
	        
		 StringBuilder txt = new StringBuilder();

	        txt.append("NOTA FISCAL|1|").append(System.getProperty("line.separator"))
	                .append(dadosNfe(novaNfe))
	                .append(dadosDeIdentificacao(novaNfe.getNotaFiscalInformacoes().getIdentificacao()));
	                
	                if(novaNfe.getNotaFiscalInformacoes().getIdentificacao().getListReferenciadas() != null) {
	                	txt.append(notasReferenciadas(novaNfe));
	                }
	                
	                txt.append(dadosDoEmitente(novaNfe.getNotaFiscalInformacoes().getIdentificacaoEmitente()))
	                .append(dadosDoDestinatario(novaNfe.getNotaFiscalInformacoes().getIdentificacaoDestinatario()));
	               

	        for (DetalheNotaFiscal det : novaNfe.getNotaFiscalInformacoes().getDetalhesNotaFiscal()) {
	            txt.append(dadosDosProdutosServicos(det));
	        }

	        txt.append(totaisDaNFe(novaNfe))
	                .append(dadosDoTransporte(novaNfe.getNotaFiscalInformacoes().getInformacaoTransporte()))
	                .append(dadosDeCobranca(new NotaFiscalFatura()));

	        // aqui o arquivo o arquivo está pronto para um próximo destino, salvar ou gerar o txt.
	       return txt = new StringBuilder(txt.toString().replaceAll("null", ""));

	    }

	    /**
	     * A - Dados da Nota Fiscal eletrônica.
	     */
	    private static String dadosNfe(NotaFiscal inf) throws ValidacaoException {
	    	
	        try {
	            StringBuilder a = new StringBuilder();
	            a.append("A|")
	                    .append(obterVersao(inf)).append("|")
	                    .append(inf.getNotaFiscalInformacoes().getIdNFe()).append("|")
	                    .append(System.getProperty("line.separator"));
	            return a.toString();
	        } catch (Exception e) {	        	
	        	throw new ValidacaoException(TipoMensagem.WARNING, "Erro ao lançar informações iniciais da NFe ao arquivo texto.");
	        }
	    }

		@SuppressWarnings("static-access")
		private static String obterVersao(NotaFiscal inf) {
			return inf.getNotaFiscalInformacoes().getVersao().toString();
		}

	    /**
	     * B - Identificação da Nota Fiscal eletrônica.
	     */
	    private static String dadosDeIdentificacao(Identificacao ide) throws Exception {
	        try {
	            StringBuilder b = new StringBuilder();
	            b.append("B|")
	                    .append(ide.getCodigoUf()).append("|")
	                    .append(ide.getCodigoNF()).append("|")
	                    .append(ide.getNaturezaOperacao().getDescricao()).append("|");
	                    
	                    String indPag = recuperarIndPag(ide);
	                    
	                    b.append(indPag).append("|")
	                    .append(ide.getModeloDocumentoFiscal()).append("|")
	                    .append(ide.getSerie()).append("|")
	                    .append(ide.getNumeroDocumentoFiscal()).append("|")
	                    .append(Util.retornaDataNfe(ide.getDataEmissao())).append("|")
	                    .append(ide.getDataSaidaEntrada()).append(ide.getHoraSaidaEntrada()).append("|")
	                    .append(ide.getTipoOperacao().getTipoOperacaoNumerico()).append("|")
	                    .append(ide.getLocalDestinoOperacao().getIntValue()).append("|")
	                    .append(ide.getCodigoMunicipio()).append("|")
	                    .append(ide.getFormatoImpressao().getIntValue()).append("|")
	                    .append(ide.getTipoEmissao().getIntValue()).append("|")
	                    .append(ide.getDigitoVerificadorChaveAcesso()).append("|")
	                    .append(ide.getTipoAmbiente().getIntValue()).append("|")
	                    .append(ide.getFinalidadeEmissaoNFe().getIntValue()).append("|")
	                    //ind final
	                    .append(ide.getOperacaoConsumidorFinal().getIntValue()).append("|")
	                    // indPress
	                    .append(ide.getPresencaConsumidor().getIntValue()).append("|")
	                    .append(ide.getProcessoEmissao().getIntValue()).append("|")
	                    .append("3.1.0").append("|")
	                    .append(ide.getDataEntradaContigencia()).append("|")
	                    .append(ide.getJustificativaEntradaContigencia()).append("|")
	                    .append(System.getProperty("line.separator"));
	            return b.toString();
	        } catch (Exception e) {
	        	throw new ValidacaoException(TipoMensagem.WARNING, "Erro ao lançar informações de identificação da nota ao arquivo texto.");
	        }
	    }
	    
	    private static String notasReferenciadas(NotaFiscal notaFiscal) {  
	    	
	    	StringBuilder b = new StringBuilder();
	    	
	    	for(NotaFiscalReferenciada referenciada : notaFiscal.getNotaFiscalInformacoes().getIdentificacao().getListReferenciadas()) {
	            b.append("BA|").append(System.getProperty("line.separator"));
	            b.append("BA02|").append(referenciada.getChaveAcessoCTe()).append("|");
	            b.append(System.getProperty("line.separator"));
	    	}
	    	
	    	return b.toString();
	    }
	    
	    
		private static String recuperarIndPag(Identificacao ide) {
			String indPag = null;
			
			if(ide.getFormaPagamento().equals("A_VISTA")) {
				indPag = "0";
			} else if(ide.getFormaPagamento().equals("A_PRAZO")) {
				indPag = "1";
			}  else {
				indPag = "2";
			}
			return indPag;
		}

	    /**
	     * C - Identificação do Emitente da Nota Fiscal eletrônica
	     */
	    private static String dadosDoEmitente(IdentificacaoEmitente emit) throws Exception {
	        try {
	            StringBuilder c = new StringBuilder();

	            c.append("C|")
	                    .append(emit.getNome()).append("|")
	                    .append(emit.getNomeFantasia()).append("|")
	                    .append(emit.getInscricaoEstadual()).append("|")
	                    .append(emit.getInscricaoEstadualSubstituto()).append("|")
	                    .append(emit.getInscricaoMunicipal()).append("|")
	                    .append(emit.getCnae()).append("|")
	                    .append(emit.getRegimeTributario().getIntValue()).append("|").append(System.getProperty("line.separator"));

	            if (emit.getDocumento().getDocumento() != null && !emit.getDocumento().getDocumento().isEmpty()) {
	                c.append("C02|").append(emit.getDocumento().getDocumento()).append("|").append(System.getProperty("line.separator"));
	            } else {
	                c.append("C02a|").append(emit.getDocumento().getDocumento()).append("|").append(System.getProperty("line.separator"));
	            }

	            c.append("C05|")
	                    .append(emit.getEndereco().getLogradouro()).append("|")
	                    .append(emit.getEndereco().getNumero()).append("|")
	                    .append(emit.getEndereco().getComplemento()).append("|")
	                    .append(emit.getEndereco().getBairro()).append("|")
	                    .append(emit.getEndereco().getCodigoCidadeIBGE()).append("|")
	                    .append(emit.getEndereco().getCidade()).append("|")
	                    .append(emit.getEndereco().getUf()).append("|")
	                    .append(emit.getEndereco().getCep()).append("|")
	                    .append(emit.getEndereco().getCodigoPais()).append("|")
	                    .append(emit.getEndereco().getPais()).append("|")
	                    .append(emit.getTelefone().getNumero())
	                    .append("|").append(System.getProperty("line.separator"));
	            return c.toString();
	        } catch (Exception e) {
	        	throw new ValidacaoException(TipoMensagem.WARNING, "Erro ao lançar informações do emitente ao arquivo texto. ");
	        }
	    }

	    /**
	     * E - Identificação do Destinatário da Nota Fiscal eletrônica
	     */
	    private static String dadosDoDestinatario(IdentificacaoDestinatario dest) throws Exception {
	        try {
	            StringBuilder e = new StringBuilder();
	            e.append("E|")
	                    .append(dest.getNome()).append("|")
	                    .append(dest.getIndicadorDestinatario() == 0 ? 2 : dest.getIndicadorDestinatario()).append("|")
	                    .append(dest.getInscricaoEstadual()).append("|")
	                    .append(dest.getInscricaoSuframa() == null ? "" : dest.getInscricaoSuframa()).append("|")
	                    .append("").append("|")
	                    .append("").append("|")
	                    .append("").append("|")
	                    .append(System.getProperty("line.separator"));

	            if (dest.getDocumento() != null && !dest.getDocumento().getDocumento().isEmpty()) {
	                e.append("E02|").append(dest.getDocumento().getDocumento()).append("|").append(System.getProperty("line.separator"));
	            } else {
	                e.append("E03|").append(dest.getDocumento().getDocumento()).append("|").append(System.getProperty("line.separator"));
	            }

	            e.append("E05|")
	                    .append(dest.getEndereco().getLogradouro()).append("|")
	                    .append(dest.getEndereco().getNumero()).append("|")
	                    .append(dest.getEndereco().getComplemento()).append("|")
	                    .append(dest.getEndereco().getBairro()).append("|")
	                    .append(dest.getEndereco().getCodigoCidadeIBGE()).append("|")
	                    .append(dest.getEndereco().getCidade()).append("|")
	                    .append(dest.getEndereco().getUf()).append("|")
	                    .append(dest.getEndereco().getCep()).append("|")
	                    .append(dest.getEndereco().getCodigoPais()).append("|")
	                    .append(dest.getEndereco().getPais()).append("|");
	            
	            		if(dest.getTelefone() != null) {
	            			
	            			e.append(dest.getTelefone().getNumero() == null ? "" : dest.getTelefone().getNumero()).append(System.getProperty("line.separator"));
	            		} else {
	            			e.append("").append(System.getProperty("line.separator"));
	            		}
	            		
	            return e.toString();
	        } catch (Exception e) {
	        	throw new ValidacaoException(TipoMensagem.WARNING, "Erro ao lançar informações do destinatário ao arquivo texto. ");
	        }
	    }

	    /**
	     * I - Produtos e Serviços da NF-e.
	     */
	    private static String dadosDosProdutosServicos(DetalheNotaFiscal det) throws Exception {
	        try {
	            StringBuilder h = new StringBuilder();

	            if (det.getSequencia() != null && !det.getSequencia().equals("")) {
	                h.append("H|")
	                        .append(det.getSequencia()).append("|")
	                        .append(det.getInfAdProd()).append("|")
	                        .append(System.getProperty("line.separator"));

	                if (det.getProdutoServico() != null && !det.getProdutoServico().getCodigoProduto().isEmpty()) {
	                    h.append("I|")
	                            .append(det.getProdutoServico().getCodigoProduto()).append("|")
	                            .append("").append("|")
	                            .append(det.getProdutoServico().getDescricaoProduto()).append("|")
	                            .append(det.getProdutoServico().getNcm()).append("|")
	                            .append(det.getProdutoServico().getExtipi()).append("|")
	                            .append(det.getProdutoServico().getCfop()).append("|")
	                            .append(det.getProdutoServico().getUnidade()).append("|")
	                            .append(det.getProdutoServico().getQuantidade()).append("|")
	                            .append(det.getProdutoServico().getValorUnitario()).append("|")
	                            .append(det.getProdutoServico().getValorTotalBruto()).append("|")
	                            .append("").append("|")
	                            .append(det.getProdutoServico().getUnidade()).append("|")
	                            // qTrib
	                            .append(det.getProdutoServico().getQuantidade()).append("|")
	                            .append(det.getProdutoServico().getValorUnitario().toString()).append("|")
	                            .append(det.getProdutoServico().getValorFrete()).append("|")
	                            .append(det.getProdutoServico().getValorSeguro()).append("|")
	                            .append(det.getProdutoServico().getValorDesconto()).append("|")
	                            .append(det.getProdutoServico().getValorOutros()).append("|")
	                            .append(det.getProdutoServico().isValorCompoeValorNF() ? 1L : 0L).append("|")
	                            .append("").append("|")
	                            .append("").append(System.getProperty("line.separator"));

	                    // impostos do produto
	                    h.append(dadosIcms(det));
	                    if(det.getImpostos().getIpi() != null ){	                    	
	                    	h.append(dadosIpi(det));
	                    }
	                    h.append(dadosPis(det));
	                    h.append(dadosCofins(det));
	                }
	            }

	            return h.toString();
	        } catch (Exception e) {
	        	throw new ValidacaoException(TipoMensagem.WARNING, "Erro ao lançar informações dos produtos ao arquivo texto. ");
	        }
	    }

	    /**
	     * M - Tributos incidentes no Produto ou Serviço<br>
	     * N - ICMS Normal e ST.
	     */
	    private static String dadosIcms(DetalheNotaFiscal det) {
	        StringBuilder m = new StringBuilder();

	        m.append("M|").append(System.getProperty("line.separator"))
	                .append("N|").append(System.getProperty("line.separator"));

	        if (det.getImpostos().getIcms() != null) {
	            if (det.getImpostos().getIcms() != null && det.getImpostos().getIcms().getCst().equals("00")) {
	                m.append("N02|")
	                        .append(det.getImpostos().getIcms().getAOrig()).append("|")
	                        .append(det.getImpostos().getIcms().getCst()).append("|")
	                        .append(det.getImpostos().getIcms().getModelidade()).append("|")
	                        .append(det.getImpostos().getIcms().getValorBaseCalculo()).append("|")
	                        .append(det.getImpostos().getIcms().getPercentualReducao()).append("|")
	                        .append(det.getImpostos().getIcms().getValor()).append(System.getProperty("line.separator"));
	            } else if (det.getImpostos().getIcms() != null && det.getImpostos().getIcms().getCst().equals("10")) {
	                m.append("N03|")
	                        .append(det.getImpostos().getIcms().getAOrig()).append("|")
	                        .append(det.getImpostos().getIcms().getCst()).append("|")
	                        .append(det.getImpostos().getIcms().getModelidade()).append("|")
	                        .append(det.getImpostos().getIcms().getValor()).append("|")
	                        .append(det.getImpostos().getIcms().getPercentualReducao()).append("|")
	                        .append(det.getImpostos().getIcms().getValor()).append("|")
	                        .append(det.getImpostos().getIcms().getModelidade()).append("|")
	                        .append(det.getImpostos().getIcms().getPercentualReducao()).append("|")
	                        .append(det.getImpostos().getIcms().getPercentualReducao()).append("|")
	                        .append(det.getImpostos().getIcms().getValorBaseCalculo()).append("|")
	                        .append(det.getImpostos().getIcms().getPercentualReducao()).append("|")
	                        .append(det.getImpostos().getIcms().getValorBaseCalculo()).append(System.getProperty("line.separator"));
	            } else if (det.getImpostos().getIcms() != null && det.getImpostos().getIcms().getCst().equals("20")) {
	                m.append("N04|")
	                        .append(det.getImpostos().getIcms().getOrigem()).append("|")
	                        .append(det.getImpostos().getIcms().getCst()).append("|")
	                        .append(det.getImpostos().getIcms().getModelidade()).append("|")
	                        .append(det.getImpostos().getIcms().getPercentualReducao()).append("|")
	                        .append(det.getImpostos().getIcms().getValorBaseCalculo()).append("|")
	                        .append(det.getImpostos().getIcms().getPercentualReducao()).append("|")
	                        .append(det.getImpostos().getIcms().getValor()).append(System.getProperty("line.separator"));
	            } else if (det.getImpostos().getIcms() != null && det.getImpostos().getIcms().getCst().equals("30")) {
	                m.append("N05|")
	                        .append(det.getImpostos().getIcms().getOrigem()).append("|")
	                        .append(det.getImpostos().getIcms().getCst()).append("|")
	                        .append(det.getImpostos().getIcms().getModelidade()).append("|")
	                        .append(det.getImpostos().getIcms().getIcmsst().getPercentualAdicionado()).append("|")
	                        .append(det.getImpostos().getIcms().getIcmsst().getPercentualReducao()).append("|")
	                        .append(det.getImpostos().getIcms().getIcmsst().getValorBaseCalculo()).append("|")
	                        .append(det.getImpostos().getIcms().getIcmsst().getPercentualAdicionado()).append("|")
	                        .append(det.getImpostos().getIcms().getIcmsst().getValor()).append(System.getProperty("line.separator"));
	            } else if (det.getImpostos().getIcms() != null
	                    && det.getImpostos().getIcms().getCst().equals("40")
	                    || det.getImpostos().getIcms().getCst().equals("41")
	                    || det.getImpostos().getIcms().getCst().equals("50")) {
	                m.append("N06|")
	                        .append(det.getImpostos().getIcms().getOrigem().getId()).append("|")
	                        .append(det.getImpostos().getIcms().getCst()).append("|")
	                        .append(det.getImpostos().getIcms().getValor()).append("|")
	                        .append(det.getImpostos().getIcms().getMotivoDesoneracao()).append(System.getProperty("line.separator"));
	            }
	        }

	        return m.toString();

//	        if ((n.getN12Cst().equals("40")) || (n.getN12Cst().equals("41")) || (n.getN12Cst().equals("50"))) {
//	            linha = linha + "N06|" + n.getN11Orig() + "|" + n.getN12Cst() + "|" + n.getN17Vicms() + "|" + n.getN28Motdesicms() + System.getProperty("line.separator");
//	        } else if (n.getN12Cst().equals("51")) {
//	            linha = linha + "N07|" + n.getN11Orig() + "|" + n.getN12Cst() + "|" + n.getN13Modbc() + "|" + n.getN14Predbc() + "|" + n.getN15Vbc() + "|" + n.getN16Picms() + "|" + n.getN17Vicms() + System.getProperty("line.separator");
//	        } else if (n.getN12Cst().equals("60")) {
//	            linha = linha + "N08|" + n.getN11Orig() + "|" + n.getN12Cst() + "|" + n.getN21Vbcst() + "|" + n.getN23Vicmsst() + System.getProperty("line.separator");
//	        } else if (n.getN12Cst().equals("70")) {
//	            linha = linha + "N09|" + n.getN11Orig() + "|" + n.getN12Cst() + "|" + n.getN13Modbc() + "|" + n.getN14Predbc() + "|" + n.getN15Vbc() + "|" + n.getN16Picms() + "|" + n.getN17Vicms() + "|" + n.getN18Modbcst() + "|" + n.getN19Pmvast() + "|" + n.getN20Predbcst() + "|" + n.getN21Vbcst() + "|" + n.getN22Picmsst() + "|" + n.getN23Vicmsst() + System.getProperty("line.separator");
//	        } else if (n.getN12Cst().equals("90")) {
//	            linha = linha + "N10|" + n.getN11Orig() + "|" + n.getN12Cst() + "|" + n.getN13Modbc() + "|" + n.getN14Predbc() + "|" + n.getN15Vbc() + "|" + n.getN16Picms() + "|" + n.getN17Vicms() + "|" + n.getN18Modbcst() + "|" + n.getN19Pmvast() + "|" + n.getN20Predbcst() + "|" + n.getN21Vbcst() + "|" + n.getN22Picmsst() + "|" + n.getN23Vicmsst() + System.getProperty("line.separator");
//	        } else if ((n.getN12Cst().toUpperCase().equals("10A")) || (n.getN12Cst().toUpperCase().equals("90A"))) {
//	            linha = linha + "N10a|" + n.getN11Orig() + "|" + n.getN12Cst() + "|" + n.getN13Modbc() + "|" + n.getN14Predbc() + "|" + n.getN15Vbc() + "|" + n.getN16Picms() + "|" + n.getN17Vicms() + "|" + n.getN18Modbcst() + "|" + n.getN19Pmvast() + "|" + n.getN20Predbcst() + "|" + n.getN21Vbcst() + "|" + n.getN22Picmsst() + "|" + n.getN23Vicmsst() + "|" + n.getN25Pbcop() + "|" + n.getN24Ufst() + System.getProperty("line.separator");
//	        } else if (n.getN12Cst().toUpperCase().equals("41B")) {
//	            linha = linha + "N10b|" + n.getN11Orig() + "|" + n.getN12Cst() + "|" + n.getN26Vbcstret() + "|" + n.getN27Vicmsstret() + "|" + n.getN31Vbcstdest() + "|" + n.getN32Vicmsstdest() + System.getProperty("line.separator");
//	        } else if (n.getN12ACSosn().equals("101")) {
//	            linha = linha + "N10c|" + n.getN11Orig() + "|" + n.getN12ACSosn() + "|" + n.getN29Pcredsn() + "|" + n.getN30Vcredicmssn() + System.getProperty("line.separator");
//	        } else if ((n.getN12ACSosn().equals("102")) || (n.getN12ACSosn().equals("103")) || (n.getN12ACSosn().equals("300")) || (n.getN12ACSosn().equals("400"))) {
//	            linha = linha + "N10d|" + n.getN11Orig() + "|" + n.getN12ACSosn() + System.getProperty("line.separator");
//	        } else if (n.getN12ACSosn().equals("201")) {
//	            linha = linha + "N10e|" + n.getN11Orig() + "|" + n.getN12ACSosn() + "|" + n.getN18Modbcst() + "|" + n.getN19Pmvast() + "|" + n.getN20Predbcst() + "|" + n.getN21Vbcst() + "|" + n.getN22Picmsst() + "|" + n.getN23Vicmsst() + "|" + n.getN29Pcredsn() + "|" + n.getN30Vcredicmssn() + System.getProperty("line.separator");
//	        } else if ((n.getN12ACSosn().equals("202")) || (n.getN12ACSosn().equals("203"))) {
//	            linha = linha + "N10f|" + n.getN11Orig() + "|" + n.getN12ACSosn() + "|" + n.getN18Modbcst() + "|" + n.getN19Pmvast() + "|" + n.getN20Predbcst() + "|" + n.getN21Vbcst() + "|" + n.getN22Picmsst() + "|" + n.getN23Vicmsst() + System.getProperty("line.separator");
//	        } else if (n.getN12ACSosn().equals("500")) {
//	            linha = linha + "N10g|" + n.getN11Orig() + "|" + n.getN12ACSosn() + "|" + n.getN18Modbcst() + "|" + n.getN26Vbcstret() + "|" + n.getN27Vicmsstret() + System.getProperty("line.separator");
//	        } else if (n.getN12ACSosn().equals("900")) {
//	            linha = linha + "N10h|" + n.getN11Orig() + "|" + n.getN12ACSosn() + "|" + n.getN13Modbc() + "|" + n.getN15Vbc() + "|" + n.getN14Predbc() + "|" + n.getN16Picms() + "|" + n.getN17Vicms() + "|" + n.getN18Modbcst() + "|" + n.getN19Pmvast() + "|" + n.getN20Predbcst() + "|" + n.getN21Vbcst() + "|" + n.getN22Picmsst() + "|" + n.getN23Vicmsst() + "|" + n.getN29Pcredsn() + "|" + n.getN30Vcredicmssn() + System.getProperty("line.separator");
//	        }
	//
//	        return linha;
	    }

	    /**
	     * O - Imposto sobre Produtos Industrializados.
	     */
	    private static String dadosIpi(DetalheNotaFiscal det) {
	        StringBuilder o = new StringBuilder();

	        o.append("O|")
	                .append(det.getImpostos().getIpi().getClasseEnquadramento()).append("|")
	                .append(det.getImpostos().getIpi().getCnpjProdutor()).append("|")
	                .append(det.getImpostos().getIpi().getCodigoSelo()).append("|")
	                .append(det.getImpostos().getIpi().getQuantidadeSelo()).append("|")
	                .append(det.getImpostos().getIpi().getCodigoEnquadramento()).append(System.getProperty("line.separator"));

	        if (det.getImpostos().getIpi().getIPITrib() != null && det.getImpostos().getIpi().getCst().equals("00")
	                || det.getImpostos().getIpi().getIPITrib() != null && det.getImpostos().getIpi().getCst().equals("49")
	                || det.getImpostos().getIpi().getIPITrib() != null && det.getImpostos().getIpi().getCst().equals("50")
	                || det.getImpostos().getIpi().getIPITrib() != null && det.getImpostos().getIpi().getCst().equals("99")) {

	            o.append("O07|")
	                    .append(det.getImpostos().getIpi().getIPITrib().getCst()).append("|")
	                    .append(det.getImpostos().getIpi().getIPITrib().getValorIPI()).append(System.getProperty("line.separator"));

	            if (det.getImpostos().getIpi().getIPITrib().getValorBaseCalculo() != null) {
	                o.append("O10|")
	                        .append(det.getImpostos().getIpi().getIPITrib().getValorBaseCalculo()).append("|")
	                        .append(det.getImpostos().getIpi().getIPITrib().getValorAliquota()).append(System.getProperty("line.separator"));
	            } else {
	                o.append("O11|")
	                        .append(det.getImpostos().getIpi().getQuantidadeUnidades()).append("|")
	                        .append(det.getImpostos().getIpi().getValorUnidade()).append(System.getProperty("line.separator"));
	            }
	        } else if (det.getImpostos().getIpi().getIPITrib() != null && det.getImpostos().getIpi().getIPITrib().getCst().equals("01")
	                || det.getImpostos().getIpi().getIPITrib().getCst().equals("02")
	                || det.getImpostos().getIpi().getIPITrib().getCst().equals("03")
	                || det.getImpostos().getIpi().getIPITrib().getCst().equals("04")
	                || det.getImpostos().getIpi().getIPITrib().getCst().equals("51")
	                || det.getImpostos().getIpi().getIPITrib().getCst().equals("52")
	                || det.getImpostos().getIpi().getIPITrib().getCst().equals("53")
	                || det.getImpostos().getIpi().getIPITrib().getCst().equals("54")
	                || det.getImpostos().getIpi().getIPITrib().getCst().equals("55")) {
	            o.append("O08|")
	                    .append(det.getImpostos().getIpi().getIPITrib().getCst()).append(System.getProperty("line.separator"));
	        }

	        return o.toString();
	    }

	    /**
	     * Q – PIS.
	     */
	    private static String dadosPis(DetalheNotaFiscal det) {
	        StringBuilder q = new StringBuilder();

	        q.append("Q|").append(System.getProperty("line.separator"));

	        PISOutrWrapper pisOutrWrapper = null;
	        
	        PISWrapper pisWrapper = null;
	        
	        if(det.getImpostos().getPisOutr() != null ) {
	        	pisOutrWrapper = det.getImpostos().getPisOutr();
	        	
	        	if (pisOutrWrapper != null) {
		            if (det.getImpostos().getPisOutr().getPis().getCst().equals("01")
		                    || det.getImpostos().getPisOutr().getPis().getCst().equals("02")) {
		                q.append("Q02|")
		                        .append(det.getImpostos().getPisOutr().getPis().getCst()).append("|")
		                        .append(det.getImpostos().getPisOutr().getPis().getValorBaseCalculo()).append("|")
		                        .append(det.getImpostos().getPisOutr().getPis().getPercentualAliquota()).append("|")
		                        .append(det.getImpostos().getPisOutr().getPis().getValor()).append(System.getProperty("line.separator"));
		            } else if (det.getImpostos().getPisOutr().getPis().getQuantidadeVendida() != null) {
			            if (det.getImpostos().getPisOutr().getPis().getCst().equals("03")) {
			                q.append("Q03|")
			                        .append(det.getImpostos().getPisOutr().getPis().getCst()).append("|")
			                        .append(det.getImpostos().getPisOutr().getPis().getValorBaseCalculo()).append("|")
			                        .append(det.getImpostos().getPisOutr().getPis().getValorAliquota()).append("|")
			                        .append(det.getImpostos().getPisOutr().getPis().getValor()).append(System.getProperty("line.separator"));
			            }
			        } else if (det.getImpostos().getPisOutr().getPis() != null) {
			            if (det.getImpostos().getPisOutr().getPis().getCst().equals("04")
			                    || det.getImpostos().getPisOutr().getPis().getCst().equals("06")
			                    || det.getImpostos().getPisOutr().getPis().getCst().equals("07")
			                    || det.getImpostos().getPisOutr().getPis().getCst().equals("08")
			                    || det.getImpostos().getPisOutr().getPis().getCst().equals("09")
			                    || det.getImpostos().getPisOutr().getPis().getCst().equals("49")) {
			                q.append("Q04|")
			                        .append(det.getImpostos().getPisOutr().getPis().getCst()).append("|").append(System.getProperty("line.separator"));
			            }
			        } else if (det.getImpostos().getPisOutr().getPis() != null) {
			            if (det.getImpostos().getPisOutr().getPis().getCst().equals("99")) {
			                q.append("Q05|")
			                        .append(det.getImpostos().getPisOutr().getPis().getCst()).append("|")
			                        .append(det.getImpostos().getPisOutr().getPis().getValor()).append(System.getProperty("line.separator"));
			            }

			            if (det.getImpostos().getPisOutr().getPis().getDValorBaseCalculo() != null && !det.getImpostos().getPisOutr().getPis().getValorAliquota().equals("")) {
			                q.append("Q07")
			                        .append(det.getImpostos().getPisOutr().getPis().getValorBaseCalculo()).append("|")
			                        .append(det.getImpostos().getPisOutr().getPis().getValor()).append(System.getProperty("line.separator"));
			            } else {
			                q.append("Q10")
			                        .append(det.getImpostos().getPisOutr().getPis().getQuantidadeVendida()).append("|")
			                        .append(det.getImpostos().getPisOutr().getPis().getValorAliquota()).append(System.getProperty("line.separator"));
			            }

			        }
		        } 
	        	
	        } else if (det.getImpostos().getPis() != null) {
	        	pisWrapper = det.getImpostos().getPis();
	        	
	        	if (pisWrapper != null) {
		            if (det.getImpostos().getPis().getPis().getCst().equals("01")
		                    || det.getImpostos().getPis().getPis().getCst().equals("02")) {
		                q.append("Q02|")
		                        .append(det.getImpostos().getPis().getPis().getCst()).append("|")
		                        .append(det.getImpostos().getPis().getPis().getValorBaseCalculo()).append("|")
		                        .append(det.getImpostos().getPis().getPis().getPercentualAliquota()).append("|")
		                        .append(det.getImpostos().getPis().getPis().getValor()).append(System.getProperty("line.separator"));
		            }else if (det.getImpostos().getPis().getPis().getQuantidadeVendida() != null) {
			            if (det.getImpostos().getPis().getPis().getCst().equals("03")) {
			                q.append("Q03|")
			                        .append(det.getImpostos().getPis().getPis().getCst()).append("|")
			                        .append(det.getImpostos().getPis().getPis().getValorBaseCalculo()).append("|")
			                        .append(det.getImpostos().getPis().getPis().getValorAliquota()).append("|")
			                        .append(det.getImpostos().getPis().getPis().getValor()).append(System.getProperty("line.separator"));
			            }
			        } else if (det.getImpostos().getPis().getPis() != null) {
			            if (det.getImpostos().getPis().getPis().getCst().equals("04")
			                    || det.getImpostos().getPis().getPis().getCst().equals("06")
			                    || det.getImpostos().getPis().getPis().getCst().equals("07")
			                    || det.getImpostos().getPis().getPis().getCst().equals("08")
			                    || det.getImpostos().getPis().getPis().getCst().equals("09")) {
			                q.append("Q04|")
			                        .append(det.getImpostos().getPis().getPis().getCst()).append(System.getProperty("line.separator"));
			            }
			        } else if (det.getImpostos().getPis().getPis() != null) {
			            if (det.getImpostos().getPis().getPis().getCst().equals("99")) {
			                q.append("Q05|")
			                        .append(det.getImpostos().getPis().getPis().getCst()).append("|")
			                        .append(det.getImpostos().getPis().getPis().getValor()).append(System.getProperty("line.separator"));
			            }

			            if (det.getImpostos().getPis().getPis().getDValorBaseCalculo() != null && !det.getImpostos().getPis().getPis().getValorAliquota().equals("")) {
			                q.append("Q07")
			                        .append(det.getImpostos().getPis().getPis().getValorBaseCalculo()).append("|")
			                        .append(det.getImpostos().getPis().getPis().getValor()).append(System.getProperty("line.separator"));
			            } else {
			                q.append("Q10")
			                        .append(det.getImpostos().getPis().getPis().getQuantidadeVendida()).append("|")
			                        .append(det.getImpostos().getPis().getPis().getValorAliquota()).append(System.getProperty("line.separator"));
			            }

			        }
		        } 
	        }
	        
	        return q.toString();
	    }

	    /**
	     * S - COFINS.
	     */
	    private static String dadosCofins(DetalheNotaFiscal det) {
	        StringBuilder s = new StringBuilder();

	        s.append("S|").append(System.getProperty("line.separator"));
	        
	        if(det.getImpostos() != null) {
	        	if (det.getImpostos().getCofins() != null && det.getImpostos().getCofins().getCofins() != null) {
		            if (det.getImpostos().getCofins().getCofins().getCst().equals("01")
		                    || det.getImpostos().getCofins().getCofins().getCst().equals("02")) {

		                s.append("S02|")
		                        .append(det.getImpostos().getCofins().getCofins().getCst()).append("|")
		                        .append(det.getImpostos().getCofins().getCofins().getValorBaseCalculo()).append("|")
		                        .append(det.getImpostos().getCofins().getCofins().getPercentualAliquota()).append("|")
		                        .append(det.getImpostos().getCofins().getCofins().getValor()).append(System.getProperty("line.separator"));
		            }else if (det.getImpostos().getCofins().getCofins().getCst().equals("03")) {
			                s.append("S03|")
			                        .append(det.getImpostos().getCofins().getCofins().getCst()).append("|")
			                        .append(det.getImpostos().getCofins().getCofins().getQuantidadeVendida()).append("|")
			                        .append(det.getImpostos().getCofins().getCofins().getValorAliquota()).append("|")
			                        .append(det.getImpostos().getCofins().getCofins().getValor()).append(System.getProperty("line.separator"));
			            
			        } else if (det.getImpostos().getCofins().getCofins().getCst().equals("04")
			                    || det.getImpostos().getCofins().getCofins().getCst().equals("06")
			                    || det.getImpostos().getCofins().getCofins().getCst().equals("07")
			                    || det.getImpostos().getCofins().getCofins().getCst().equals("08")
			                    || det.getImpostos().getCofins().getCofins().getCst().equals("09")
			                    || det.getImpostos().getCofins().getCofins().getCst().equals("49")) {

			                s.append("S04|")
			                        .append(det.getImpostos().getCofins().getCofins().getCst()).append("|").append(System.getProperty("line.separator"));
			            
			        } else if (det.getImpostos().getCofins().getCofins().getCst().equals("99")) {
			        	s.append("S05|")
                    	.append(det.getImpostos().getCofins().getCofins().getCst()).append("|")
                    	.append(det.getImpostos().getCofins().getCofins().getValor()).append(System.getProperty("line.separator"));
		            
			            if (det.getImpostos().getCofins().getCofins().getValorBaseCalculo() != null && !det.getImpostos().getCofins().getCofins().getValorBaseCalculo().equals(BigDecimal.ZERO)) {
			                s.append("S07|")
			                        .append(det.getImpostos().getCofins().getCofins().getValorBaseCalculo()).append("|")
			                        .append(det.getImpostos().getCofins().getCofins().getPercentualAliquota()).append(System.getProperty("line.separator"));
			            } else {
			                s.append("S09|")
			                        .append(det.getImpostos().getCofins().getCofins().getQuantidadeVendida()).append("|")
			                        .append(det.getImpostos().getCofins().getCofins().getValorAliquota()).append(System.getProperty("line.separator"));
			            }
		            }
		            	
	        	 } else if (det.getImpostos().getCofinsOutr() != null){
			            if (det.getImpostos().getCofinsOutr().getCofins().getCst().equals("01")
			                    || det.getImpostos().getCofinsOutr().getCofins().getCst().equals("02")) {

			                s.append("S02|")
			                        .append(det.getImpostos().getCofinsOutr().getCofins().getCst()).append("|")
			                        .append(det.getImpostos().getCofinsOutr().getCofins().getValorBaseCalculo()).append("|")
			                        .append(det.getImpostos().getCofinsOutr().getCofins().getPercentualAliquota()).append("|")
			                        .append(det.getImpostos().getCofinsOutr().getCofins().getValor()).append(System.getProperty("line.separator"));
			                
			            } else if (det.getImpostos().getCofinsOutr().getCofins().getCst().equals("03")) {
				                s.append("S03|")
				                        .append(det.getImpostos().getCofinsOutr().getCofins().getCst()).append("|")
				                        .append(det.getImpostos().getCofinsOutr().getCofins().getQuantidadeVendida()).append("|")
				                        .append(det.getImpostos().getCofinsOutr().getCofins().getValorAliquota()).append("|")
				                        .append(det.getImpostos().getCofinsOutr().getCofins().getValor()).append(System.getProperty("line.separator"));
				                
				        } else if (det.getImpostos().getCofinsOutr().getCofins().getCst().equals("04")
				                    || det.getImpostos().getCofinsOutr().getCofins().getCst().equals("06")
				                    || det.getImpostos().getCofinsOutr().getCofins().getCst().equals("07")
				                    || det.getImpostos().getCofinsOutr().getCofins().getCst().equals("08")
				                    || det.getImpostos().getCofinsOutr().getCofins().getCst().equals("09")
				                    || det.getImpostos().getCofinsOutr().getCofins().getCst().equals("49")) {

				                s.append("S04|")
				                        .append(det.getImpostos().getCofinsOutr().getCofins().getCst()).append("|").append(System.getProperty("line.separator"));
				        } else if (det.getImpostos().getCofinsOutr().getCofins().getCst().equals("99")) {
			                s.append("S05|")
	                        .append(det.getImpostos().getCofinsOutr().getCofins().getCst()).append("|")
	                        .append(det.getImpostos().getCofinsOutr().getCofins().getValor()).append(System.getProperty("line.separator"));
			                
				            if (det.getImpostos().getCofinsOutr().getCofins().getValorBaseCalculo() != null && !det.getImpostos().getCofinsOutr().getCofins().getValorBaseCalculo().equals(BigDecimal.ZERO)) {
				                s.append("S07|")
				                		.append(det.getImpostos().getCofinsOutr().getCofins().getCst()).append("|")
				                        .append(det.getImpostos().getCofinsOutr().getCofins().getValorBaseCalculo()).append("|")
				                        .append(det.getImpostos().getCofinsOutr().getCofins().getPercentualAliquota()).append(System.getProperty("line.separator"));
				            } else {
				                s.append("S09|")
				                        .append(det.getImpostos().getCofinsOutr().getCofins().getQuantidadeVendida()).append("|")
				                        .append(det.getImpostos().getCofinsOutr().getCofins().getValorAliquota()).append(System.getProperty("line.separator"));
				            }

				        }    
			            
			        } else {
			        	s.append("S04|").append("41").append("|").append(System.getProperty("line.separator"));
			        } 
	        	
		       
	        } else {
	        	throw new ValidacaoException(TipoMensagem.WARNING, "Erro ao realizar o parser da nota para o TXT de notas: ");
	        }
	        
	        return s.toString();
	    }

	    /**
	     * W - Valores Totais da NF-e.
	     */
	    private static String totaisDaNFe(NotaFiscal notafiscal) throws Exception {
	    	
	    	NotaFiscalValorCalculado total =  notafiscal.getNotaFiscalInformacoes().getNotaFiscalValoresCalculados();
	    	
	        try {
	            StringBuilder w = new StringBuilder();
	            w.append("W|").append(System.getProperty("line.separator"));

	            if (total != null && total.getValoresCalculados() != null) {
	                w.append("W02|")
	                		.append(total.getValoresCalculados().getValorBaseICMS()).append("|")
	                        .append(total.getValoresCalculados().getValorICMS()).append("|")
	                        .append(total.getValoresCalculados().getvICMSDeson()).append("|")
	                        .append(total.getValoresCalculados().getValorBaseICMS()).append("|")
	                        .append(total.getValoresCalculados().getValorBaseICMSSubstituto()).append("|")
	                        .append(total.getValoresCalculados().getValorProdutos()).append("|")
	                        .append(total.getValoresCalculados().getValorFrete()).append("|")
	                        .append(total.getValoresCalculados().getValorSeguro()).append("|")
	                        .append(total.getValoresCalculados().getValorDesconto()).append("|")
	                        .append(total.getValoresCalculados().getValorImpostoImportacao()).append("|")
	                        .append(total.getValoresCalculados().getValorIPI()).append("|")
	                        .append(total.getValoresCalculados().getValorPIS()).append("|")
	                        .append(total.getValoresCalculados().getValorCOFINS()).append("|")
	                        .append(total.getValoresCalculados().getValorOutro()).append("|")
	                        .append(total.getValoresCalculados().getValorNF()).append("|")
	                        .append(total.getValoresCalculados().getValorOutro()).append("|").append(System.getProperty("line.separator"));
	            }
	            
	            InformacaoValoresTotais totalSSQN =  notafiscal.getNotaFiscalInformacoes().getInformacaoValoresTotais();
	            
	            if (totalSSQN != null && totalSSQN.getTotaisISSQN() != null && !totalSSQN.getTotaisISSQN().getValorBaseCalculo().equals("")) {
	                w.append("W17|")
	                        .append(totalSSQN.getTotaisISSQN().getValorServicos()).append("|")
	                        .append(totalSSQN.getTotaisISSQN().getValorBaseCalculo()).append("|")
	                        .append(totalSSQN.getTotaisISSQN().getValorISS()).append("|")
	                        .append(totalSSQN.getTotaisISSQN().getValorPIS()).append("|")
	                        .append(totalSSQN.getTotaisISSQN().getValorCOFINS()).append(System.getProperty("line.separator"));
	            }

	            if (totalSSQN != null && totalSSQN.getRetencoesTributos() != null) {
	                w.append("W23|")
	                        .append(totalSSQN.getRetencoesTributos().getValorRetidoPIS()).append("|")
	                        .append(totalSSQN.getRetencoesTributos().getValorRetidoCOFINS()).append("|")
	                        .append(totalSSQN.getRetencoesTributos().getValorRetidoCSLL()).append("|")
	                        .append(totalSSQN.getRetencoesTributos().getValorRetidoIRRF()).append("|")
	                        .append(totalSSQN.getRetencoesTributos().getValorRetidoIRRF()).append("|")
	                        .append(totalSSQN.getRetencoesTributos().getValorBaseCalculoPrevidencia()).append("|")
	                        .append(totalSSQN.getRetencoesTributos().getValorRetidoPrevidencia()).append(System.getProperty("line.separator"));
	            }

	            return w.toString();

	        } catch (Exception e) {
	        	throw new ValidacaoException(TipoMensagem.WARNING, "Erro ao lançar os totais da nota ao arquivo: ");
	        }
	    }

	    /**
	     * X - Informações do Transporte da NF-e.
	     */
	    private static String dadosDoTransporte(InformacaoTransporte transp) throws Exception {
	        try {
	            StringBuilder x = new StringBuilder();

	            x.append("X|")
	                    .append(transp.getModalidadeFrete())
	                    .append(System.getProperty("line.separator"));

	            if (transp != null && transp.getTransportadorWrapper() != null) {
	                if (transp.getTransportadorWrapper().getDocumento() != null && !transp.getTransportadorWrapper().getDocumento().equals("")
	                        || (transp.getTransportadorWrapper().getDocumento() != null && !transp.getTransportadorWrapper().getDocumento().equals(""))
	                        || (transp.getTransportadorWrapper().getInscricaoEstadual() != null && !transp.getTransportadorWrapper().getInscricaoEstadual().equals(""))
	                        || (transp.getTransportadorWrapper().getEndereco() != null && !transp.getTransportadorWrapper().getEndereco().getLogradouro().equals(""))
	                        || (transp.getTransportadorWrapper().getEndereco() != null && !transp.getTransportadorWrapper().getEndereco().getCidade().equals(""))
	                        || (transp.getTransportadorWrapper().getEndereco() != null && transp.getTransportadorWrapper().getEndereco().getUf() != null)) {
	                    x.append("X03|")
	                            .append(transp.getTransportadorWrapper().getNome()).append("|")
	                            .append(transp.getTransportadorWrapper().getInscricaoEstadual()).append("|")
	                            .append(transp.getTransportadorWrapper().getEndereco().getLogradouro()).append("|")
	                            .append(transp.getTransportadorWrapper().getEndereco().getUf()).append("|")
	                            .append(transp.getTransportadorWrapper().getEndereco().getCidade()).append(System.getProperty("line.separator"));

	                    if (transp.getTransportadorWrapper().getDocumento() != null && !transp.getTransportadorWrapper().getDocumento().equals("")) {
	                        x.append("X04|").append(transp.getTransportadorWrapper().getDocumento()).append(System.getProperty("line.separator"));
	                    } else {
	                        x.append("X04|").append(transp.getTransportadorWrapper().getDocumento()).append(System.getProperty("line.separator"));
	                    }
	                }

	                if (transp.getTransportadorWrapper().getRetencaoICMS() != null && transp.getTransportadorWrapper().getRetencaoICMS().getValorServico() != null && !transp.getTransportadorWrapper().getRetencaoICMS().getValorServico().equals("")) {
	                    x.append("X11|")
	                            .append(transp.getTransportadorWrapper().getRetencaoICMS().getValorServico()).append("|")
	                            .append(transp.getTransportadorWrapper().getRetencaoICMS().getValorBaseCalculo()).append("|")
	                            .append(transp.getTransportadorWrapper().getRetencaoICMS().getPercentualAliquota()).append("|")
	                            .append(transp.getTransportadorWrapper().getRetencaoICMS().getValorICMS()).append("|")
	                            .append("").append("|")
	                            .append(transp.getTransportadorWrapper().getMunicipio()).append(System.getProperty("line.separator"));
	                }

	                if (transp.getTransportadorWrapper().getVeiculo() != null && transp.getTransportadorWrapper().getVeiculo().getPlaca() != null && !transp.getTransportadorWrapper().getVeiculo().getPlaca().equals("")) {
	                    x.append("X18|")
	                            .append(transp.getTransportadorWrapper().getVeiculo().getPlaca()).append("|")
	                            .append(transp.getTransportadorWrapper().getVeiculo().getUf()).append("|")
	                            .append(transp.getTransportadorWrapper().getVeiculo().getRegistroTransCarga()).append(System.getProperty("line.separator"));
	                }

	                
	            }
	            return x.toString();
	        } catch (Exception e) {
	        	throw new ValidacaoException(TipoMensagem.WARNING, "Erro ao lançar informações de transporte ao arquivo: ");
	        }
	    }

	    /**
	     * Y – Dados da Cobrança.
	     */
	    private static String dadosDeCobranca(NotaFiscalFatura fat) throws Exception {
	        try {
	            StringBuilder y = new StringBuilder();
	            y.append("Y|").append(System.getProperty("line.separator"));

	            if (fat != null) {
	                y.append("Y02|")
	                        .append(fat.getNumero()).append("|")
	                        .append(fat.getValor()).append("|")
	                        .append(fat.getValor()).append("|")
	                        .append(fat.getValor()).append(System.getProperty("line.separator"));
	            }

	            return y.toString();

	        } catch (Exception e) {
	        	throw new ValidacaoException(TipoMensagem.WARNING, "Erro ao lançar informações de cobrança ao arquivo: ");
	        }
	    }
	    
     
}
