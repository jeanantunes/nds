function Distribuicao(tela) {
	
	var D = this;

    var modoTela = null;

    var idHistorico = "";


    this.inicializar = function() {
		
    	D.verificarTipoConvencional();
		
	},
    
	this.verificarTipoConvencional = function(idCota) {
		
        var param = [{name: 'idCota', value: idCota}];

		$.postJSON(contextPath + "/cadastro/cota/verificarTipoConvencional",
				param,
				function(result) {

					if (result){
						console.log(result);
						if(result.tipoDistricuicao=='CONVENCIONAL'){
							D.$('recebeComplementar').removeAttr('disabled');
							if(result.recebeComplementar==true)
								D.$('recebeComplementar').attr('checked','checked');
							else
								D.$('recebeComplementar').removeAttr('checked');
						}else if(result.tipoDistricuicao=='ALTERNATIVO'){
							D.$('recebeComplementar').removeAttr('checked').attr('disabled', true);
						}
						
					}
				},
				
				null, true);
		
	},
	
    /**
     * Define o modo da tela de distribuição conforme os
     * valores ModoTela.CADASTRO_COTA ou ModoTela.HISTORICO_TITULARIDADE
     *
     */
    this.definirModoTela = function(modoTela, idHistorico) {
        D.modoTela = modoTela;
        D.idHistorico = idHistorico;
    },

    /**
     * Verifica se a tela esta em modo de cadastro de cota
     */
    this.isModoTelaCadastroCota = function() {
        return ModoTela.CADASTRO_COTA == D.modoTela;
    },
	
	/**
	 * Envia dados da tela para serem salvos no controller
	 * 
	 * @return false - define que a não sairá da tela, responsabilidade deixada para o método de callback
	 */
	this.salvar = function(callback) {
				
		$.postJSON(contextPath + "/cadastro/cota/salvarDistribuicaoCota",
				D.getDados(),
				callback,
				null,
				true,
				"dialog-cota");
		
		return false;		
	},
	
	/**
	 * Retorna todos os dados da tela no padrão utilizado pelo VRaptor
	 * @return Espelho de DistribuicaoDTO (br.com.abril.nds.dto) 
	 */
	this.getDados = function() {
	
		var data = [];
		
		data.push({name:'distribuicao.numCota',					value: D.get("numCota")});
		data.push({name:'distribuicao.qtdePDV',					value: D.get("qtdePDV")});
		data.push({name:'distribuicao.box',						value: D.get("box")});
		data.push({name:'distribuicao.assistComercial',			value: D.get("assistComercial")});
		data.push({name:'distribuicao.gerenteComercial',    	value: D.get("gerenteComercial")});
		data.push({name:'distribuicao.descricaoTipoEntrega',	value: D.get("tipoEntrega")});
		data.push({name:'distribuicao.observacao',				value: D.get("observacao")});
		data.push({name:'distribuicao.arrendatario',			value: D.get("arrendatario")});
		data.push({name:'distribuicao.repPorPontoVenda',		value: D.get("repPorPontoVenda")});
		data.push({name:'distribuicao.solNumAtras',				value: D.get("solNumAtras")});
		data.push({name:'distribuicao.recebeRecolhe',			value: D.get("recebeRecolhe")});
		data.push({name:'distribuicao.recebeComplementar',		value: D.get("recebeComplementar")});
		data.push({name:'distribuicao.neImpresso',				value: D.get("neImpresso")});
		data.push({name:'distribuicao.neEmail',					value: D.get("neEmail")});
		data.push({name:'distribuicao.ceImpresso',				value: D.get("ceImpresso")});
		data.push({name:'distribuicao.ceEmail',					value: D.get("ceEmail")});
		data.push({name:'distribuicao.slipImpresso',			value: D.get("slipImpresso")});
		data.push({name:'distribuicao.slipEmail',				value: D.get("slipEmail")});
		data.push({name:'distribuicao.boletoImpresso',			value: D.get("boletoImpresso")});
		data.push({name:'distribuicao.boletoEmail',				value: D.get("boletoEmail")});
		data.push({name:'distribuicao.boletoSlipImpresso',		value: D.get("boletoSlipImpresso")});
		data.push({name:'distribuicao.boletoSlipEmail',			value: D.get("boletoSlipEmail")});
		data.push({name:'distribuicao.reciboImpresso',			value: D.get("reciboImpresso")});
		data.push({name:'distribuicao.reciboEmail',				value: D.get("reciboEmail")});
		
		var tipoEntrega = D.get('tipoEntrega');
		
		if (tipoEntrega == 'ENTREGA_EM_BANCA') {
			
			data.push({name:'distribuicao.utilizaTermoAdesao',		value: D.get("utilizaTermoAdesao")});
			data.push({name:'distribuicao.termoAdesaoRecebido',		value: D.get("termoAdesaoRecebido")});
			data.push({name:'distribuicao.percentualFaturamento',	value: D.get("percentualFaturamentoEntregaBanca")});
			data.push({name:'distribuicao.taxaFixa',				value: floatValue(D.get("taxaFixaEntregaBanca"))});
			data.push({name:'distribuicao.inicioPeriodoCarencia',	value: D.get("inicioPeriodoCarenciaEntregaBanca")});
			data.push({name:'distribuicao.fimPeriodoCarencia',		value: D.get("fimPeriodoCarenciaEntregaBanca")});
			data.push({name:'distribuicao.baseCalculo',	            value: D.get("baseCalculo")});
			
		} else if (tipoEntrega == 'ENTREGADOR') {
			
			data.push({name:'distribuicao.utilizaProcuracao',		value: D.get("utilizaProcuracao")});
			data.push({name:'distribuicao.procuracaoRecebida',		value: D.get("procuracaoRecebida")});
			data.push({name:'distribuicao.percentualFaturamento',	value: D.get("percentualFaturamentoEntregador")});
			data.push({name:'distribuicao.inicioPeriodoCarencia',	value: D.get("inicioPeriodoCarenciaEntregador")});
			data.push({name:'distribuicao.fimPeriodoCarencia',		value: D.get("fimPeriodoCarenciaEntregador")});
		}
		
		return data;
	},
	
	/**
	 * Preenche a tela
	 * 
	 * @param dto - objeto Espelho de DistribuicaoDTO esperado (br.com.abril.nds.dto) 
	 */
	this.setDados = function(dto) {
				
		if(dto.tiposEntrega)
			D.montarComboTipoEntrega(dto.tiposEntrega);
		
		if(dto.basesCalculo)
			D.montarComboBaseCalculo(dto.basesCalculo);
				
		$("input[name='numCotaUpload']").val(dto.numCota);
		console.log(dto.recebeComplementar);
		
		D.set('numCota',				dto.numCota);
		D.set('qtdePDV',				dto.qtdePDV ? dto.qtdePDV.toString() : '' );
		D.set('box',					dto.box);
		D.set('assistComercial',		dto.assistComercial);
		D.set('gerenteComercial',		dto.gerenteComercial);
		D.set('tipoEntrega',			dto.descricaoTipoEntrega);
		D.set('tipoEntregaHidden',		dto.descricaoTipoEntrega);
		D.set('arrendatario',			dto.arrendatario);
		D.set('observacao',				dto.observacao);
		D.set('repPorPontoVenda',	    dto.repPorPontoVenda);
		D.set('solNumAtras',			dto.solNumAtras);
		D.set('recebeRecolhe',			dto.recebeRecolhe);
		D.set('recebeComplementar',		(dto.recebeComplementar || false));
		D.set('neImpresso',				dto.neImpresso);
		D.set('neEmail',				dto.neEmail);
		D.set('ceImpresso',				dto.ceImpresso);
		D.set('ceEmail',				dto.ceEmail);
		D.set('slipImpresso',			dto.slipImpresso);
		D.set('slipEmail',				dto.slipEmail);
		D.set('boletoImpresso',			dto.boletoImpresso);
		D.set('boletoEmail',			dto.boletoEmail);
		D.set('boletoSlipImpresso',		dto.boletoSlipImpresso);
		D.set('boletoSlipEmail',		dto.boletoSlipEmail);
		D.set('reciboImpresso',			dto.reciboImpresso);
		D.set('reciboEmail',			dto.reciboEmail);
		
		var tipoEntrega = D.get('tipoEntrega');
		
		if (tipoEntrega == 'ENTREGA_EM_BANCA') {
			
			D.set('utilizaTermoAdesao',					dto.utilizaTermoAdesao);
			D.set('termoAdesaoRecebido',				dto.termoAdesaoRecebido);
			D.set('percentualFaturamentoEntregaBanca',	dto.percentualFaturamento);
			D.set('taxaFixaEntregaBanca',				dto.taxaFixa);
			D.set('inicioPeriodoCarenciaEntregaBanca',	dto.inicioPeriodoCarencia);
			D.set('fimPeriodoCarenciaEntregaBanca',		dto.fimPeriodoCarencia);
			D.set('baseCalculo',			            dto.baseCalculo);
			
			D.$('taxaFixaEntregaBanca').priceFormat({
		        allowNegative : false,
		        centsSeparator : ',',
		        thousandsSeparator : '.'
		    });
			
			D.setNomeTermoAdesao(dto.nomeTermoAdesao);
			
		} else if (tipoEntrega == 'ENTREGADOR') {
		
			D.set('utilizaProcuracao',					dto.utilizaProcuracao);
			D.set('procuracaoRecebida',					dto.procuracaoRecebida);
			D.set('percentualFaturamentoEntregador',	dto.percentualFaturamento);
			D.set('inicioPeriodoCarenciaEntregador',	dto.inicioPeriodoCarencia);
			D.set('fimPeriodoCarenciaEntregador',		dto.fimPeriodoCarencia);
			
			D.setNomeProcuracao(dto.nomeProcuracao);
		}
		
		D.carregarConteudoTipoEntrega(tipoEntrega, false, false);
		
		if(D.isModoTelaCadastroCota()){
             if (dto.qtdeAutomatica) {
		    	D.$('qtdePDV').attr('disabled', true);
		    } else {
			    D.$('qtdePDV').removeAttr('disabled');
	    	}
        } else {
            D.$('qtdePDV', this.workspace).prop('disabled', true);
        }
		
		D.$('numCota').attr('disabled', true);
		D.$('box').attr('disabled', true);
	},
	
	/**
	 * Limpa campos da tela
	 */
	this.limparDados = function() {
		
		D.setDados(null);
	},
	
	/**
	 * Busca no banco os dados de Distribuição da Cota e preenche a tela
	 * @param idCota - Código da cota
	 */
	this.carregarDadosDistribuicaoCota = function(idCota) {
        var param = [{name: 'idCota', value: idCota},
                     {name: 'modoTela', value:D.modoTela.value},
                     {name: 'idHistorico', value:D.idHistorico}];

		$.postJSON(contextPath + "/cadastro/cota/carregarDistribuicaoCota",
				param,
				function(result) {
					D.setDados(result);
				},
				null, true);
	},
	
	/**
	 * Gera combo de de Tipo de Entrega
	 * @param itensDTO - objeto espelho de ItemDTO (br.com.abril.nds.dto)
	 */
	this.montarComboTipoEntrega = function(itensDTO) {
		
		itensDTO.splice(0,0,{"key": {"@class": "string","$": ""},"value": {"@class": "string","$": ""}});
		
		var combo =  montarComboBox(itensDTO, false);
		
		D.$("tipoEntrega").html(combo);
		D.$("tipoEntrega").sortOptions();
	},
	
	/**
	 * Gera combo de de Base de Calculo
	 * @param itensDTO - objeto espelho de ItemDTO (br.com.abril.nds.dto)
	 */
	this.montarComboBaseCalculo = function(itensDTO) {
		
		itensDTO.splice(0,0,{"key": {"@class": "string","$": ""},"value": {"@class": "string","$": ""}});
		
		var combo =  montarComboBox(itensDTO, false);
		
		D.$("baseCalculo").html(combo);
		D.$("baseCalculo").sortOptions();
	},
	
	this.submitForm = function(idForm) {
		
		$('#' + idForm).submit();
	},
		
	this.downloadTermoAdesao = function() {
		
		var valorTaxa = floatValue(D.get("taxaFixaEntregaBanca"));

		var params = {taxa:valorTaxa, percentual:D.get("percentualFaturamentoEntregaBanca")};
		
		$.postJSON(contextPath + "/cadastro/cota/validarValoresParaDownload",
				params,
				function() {
					document.location.assign(contextPath + "/cadastro/cota/downloadTermoAdesao?termoAdesaoRecebido="+D.get("termoAdesaoRecebido")+"&numeroCota="+D.get("numCota")+"&taxa="+valorTaxa+"&percentual="+D.get("percentualFaturamentoEntregaBanca"));
				},
				null,
				true,
				"dialog-cota");
	},
	
	this.downloadProcuracao = function() {
		
		document.location.assign(contextPath + "/cadastro/cota/downloadProcuracao?procuracaoRecebida="+D.get("procuracaoRecebida")+"&numeroCota="+D.get("numCota"));
	},
	
	this.tratarRetornoUpload = function(data) {
		
		data = replaceAll(data, "<pre>", "");
		data = replaceAll(data, "</pre>", "");
		
		data = replaceAll(data, "<PRE>", "");
		data = replaceAll(data, "</PRE>", "");
		
		var responseJson = jQuery.parseJSON(data);
		
		if (responseJson.mensagens) {

			exibirMensagemDialog(
				responseJson.mensagens.tipoMensagem, 
				responseJson.mensagens.listaMensagens, "dialog-cota"
			);
		}
			
		var fileName = responseJson.result;
		
		var tipoEntrega = D.get('tipoEntrega');
		
		if (tipoEntrega == 'ENTREGA_EM_BANCA') {

			D.setNomeTermoAdesao(fileName);
			
		} else if (tipoEntrega == 'ENTREGADOR') {
			
			D.setNomeProcuracao(fileName);
		}
	},
	
	/**
	 * Atribui valor a um campo da tela
	 * Obs: Checkboxs devem ser atribuidos com o valor de true ou false
	 * 
	 * @param campo - Campo a ser alterado
	 * @param value - valor
	 */
	this.set = function(campo,value) {
				
		var elemento = D.$(campo);
		
		if(elemento.attr('type') == 'checkbox') {
			
			if(value) {
				elemento.attr('checked','checked');
			} else {
				elemento.removeAttr('checked');
			}
						
		} else {
			elemento.val(value);
		}
	},
	
	/**
	 * Obtém valor de elemento da tela
	 * @param campo - de onde o valor será obtido
	 */
	this.get = function(campo) {
		
		var elemento = D.$(campo);
		
		if(elemento.attr('type') == 'checkbox') {
			return (elemento.attr('checked') == 'checked') ;
		} else {
			return elemento.val();
		}
		
	},
	
	/**
	 * Obtém elemento da tela
	 */
	this.$ = function(campo) {
		
		 return $("#" + tela + campo);
	},
	
	this.mostrarEsconderDiv = function(classDiv, exibir) {
		
		$("." + classDiv).toggle(exibir);
	};
	
	this.mostarPopUpAteracaoTipoEntrega = function(value) {
		
		var tipoEntregaHidden = D.get('tipoEntregaHidden');
		
		if (tipoEntregaHidden == "" || tipoEntregaHidden == 'COTA_RETIRA') {
			
			D.set('tipoEntregaHidden', value);
			
			D.carregarConteudoTipoEntrega(value, false, true);
			
			return ;
		}
		
		$("#dialogMudancaTipoEntrega").dialog({
			resizable: false,
			height:'auto',
			width:600,
			modal: true,
			buttons: [
			    {
			    	id: "mudancaTipoEntregaBtnConfirmar",
			    	text: "Confirmar",
			    	click: function() {
					
			    		D.carregarConteudoTipoEntrega(value, true, true);
						
						D.set('tipoEntregaHidden', value);
						
						$(this).dialog("close");
			    	}
			    },
			    {
			    	id: "mudancaTipoEntregaBtnCancelar",
			    	text: "Cancelar",
			    	click: function() {
			    
			    		D.set("tipoEntrega", D.get("tipoEntregaHidden"));
						
						$(this).dialog("close");
			    	}
				}
			]
		});
	};
	
	this.carregarConteudoTipoEntrega = function(value, limparCampos, carregarValores) {
		
		if (value == "COTA_RETIRA") {
			
			D.mostrarEsconderConteudoEntregaBanca(false, limparCampos);
			
			D.mostrarEsconderConteudoEntregador(false, limparCampos);
			
		} else if (value == "ENTREGA_EM_BANCA") {
			
			D.mostrarEsconderConteudoEntregador(false, limparCampos);
			
			D.mostrarEsconderConteudoEntregaBanca(true, limparCampos);
			
			if (carregarValores) {
				
				D.carregarValoresCamposEntregaBanca();	
			}
			
		} else if (value == "ENTREGADOR") {
			
			D.mostrarEsconderConteudoEntregaBanca(false, limparCampos);
			
			D.mostrarEsconderConteudoEntregador(true, limparCampos);
			
		} else {
			
			D.mostrarEsconderConteudoEntregaBanca(false, limparCampos);
			
			D.mostrarEsconderConteudoEntregador(false, limparCampos);
		}
	};
	
	this.carregarValoresCamposEntregaBanca = function() {
		
		$.postJSON(contextPath + "/cadastro/cota/carregarValoresEntregaBanca",
			{numCota:D.get("numCota")},
			function (result) {
			
				D.set('percentualFaturamentoEntregaBanca',	result.percentualFaturamento);
				D.set('taxaFixaEntregaBanca',				result.taxaFixa);
			},
			null,
			true);
	};
	
    this.distribuidorUtilizaTermoAdesao = function() {
		
		$.postJSON(contextPath + "/cadastro/cota/distribuidorUtilizaTermoAdesao",
			null,
			function (result) {
			
				if (result.boolean){
					
					$(".divImpressaoTermoAdesao").show();
					$(".divUtilizaTermoAdesao").show();
					$(".divTermoAdesaoRecebido").show();
				}
				else{
				
				    $(".divImpressaoTermoAdesao").hide();
				    $(".divUtilizaTermoAdesao").hide();
					$(".divTermoAdesaoRecebido").hide();
				}
			},
			null,
			true);
	};
	
    this.distribuidorUtilizaProcuracao = function() {
		
		$.postJSON(contextPath + "/cadastro/cota/distribuidorUtilizaProcuracao",
			null,
			function (result) {
			
				if (result.boolean){
					
					$("#cotaTemEntregador").show();
				}
				else{
				
				    $("#cotaTemEntregador").hide();
				}
			},
			null,
			true);
	};
	
	this.mostrarEsconderConteudoEntregaBanca = function(exibirDiv, limparCampos) {
		
		D.mostrarEsconderConteudoTipoEntrega(exibirDiv, "divConteudoEntregaBanca",
											 "divUtilizaTermoAdesao", "divTermoAdesaoRecebido",
											 "utilizaTermoAdesao", "termoAdesaoRecebido", limparCampos);
		
		D.distribuidorUtilizaTermoAdesao();
	};
	
	this.mostrarEsconderConteudoEntregador = function(exibirDiv, limparCampos) {
		
		D.mostrarEsconderConteudoTipoEntrega(exibirDiv, "divConteudoEntregador",
											 "divUtilizaProcuracao", "divProcuracaoRecebida",
											 "utilizaProcuracao", "procuracaoRecebida", limparCampos);
		
		D.distribuidorUtilizaProcuracao();
	};

	this.mostrarEsconderConteudoTipoEntrega = function(exibirDiv, divConteudoTipoEntrega,
													   divUtilizaArquivo, divArquivoRecebido,
													   campoUtilizaArquivo, campoArquivoRecebido, limparCampos) {
		
		D.mostrarEsconderDiv(divConteudoTipoEntrega, exibirDiv);

		if (!exibirDiv && limparCampos) {
			
			D.set(campoUtilizaArquivo, false);
			
			D.limparCampos();
		}
		
		D.mostrarEsconderDivUtilizaArquivo(divUtilizaArquivo, divArquivoRecebido,
										   campoUtilizaArquivo, campoArquivoRecebido);
	};
	
	this.mostrarEsconderDivUtilizaArquivo = function(divUtilizaArquivo, divArquivoRecebido,
													 campoUtilizaArquivo, campoArquivoRecebido) {
		
		var exibirDiv = D.get(campoUtilizaArquivo);
		
		if (D.isModoTelaCadastroCota()) {
            D.mostrarEsconderDiv(divUtilizaArquivo, exibirDiv);
        }

		if (!exibirDiv) {
			
			D.set(campoArquivoRecebido,	false);
		}
		
		D.mostrarEsconderDivArquivoRecebido(divArquivoRecebido, campoArquivoRecebido);
	};
	
	this.mostrarEsconderDivArquivoRecebido = function(divArquivoRecebido, campoArquivoRecebido) {
		
		var exibirDiv = D.get(campoArquivoRecebido);
		
		D.mostrarEsconderDiv(divArquivoRecebido, exibirDiv);
	};
	
	this.limparCampos = function() {
		
		var tipoEntrega = D.get('tipoEntregaHidden');
		
		if (tipoEntrega == "ENTREGA_EM_BANCA") {
			
			D.set('percentualFaturamentoEntregaBanca',	"");
			D.set('taxaFixaEntregaBanca',				"");
			D.set('inicioPeriodoCarenciaEntregaBanca',	"");
			D.set('fimPeriodoCarenciaEntregaBanca',		"");
			
			$("#nomeArquivoTermoAdesao").html("");
			
			$.postJSON(contextPath + "/cadastro/cota/excluirTermoAdesao",
					{numCota:D.get("numCota")} ,
					null,
					null,
					true);
			
		} else if (tipoEntrega == "ENTREGADOR") {
		
			D.set('percentualFaturamentoEntregador',	"");
			D.set('inicioPeriodoCarenciaEntregador',	"");
			D.set('fimPeriodoCarenciaEntregador',		"");
			
			$("#nomeArquivoProcuracao").html("");
			
			$.postJSON(contextPath + "/cadastro/cota/excluirProcuracao",
					{numCota:D.get("numCota")},
					null,
					null,
					true);
		}
	};
	
	this.setNomeTermoAdesao = function(nomeTermoAdesao) {
		
		$("#nomeArquivoTermoAdesao").html(nomeTermoAdesao);
	};
	
	this.setNomeProcuracao = function(nomeProcuracao) {
		
		$("#nomeArquivoProcuracao").html(nomeProcuracao);
	};
	
	$(function() {
		D.$("numCota").numeric();
		D.$("qtdePDV").numeric();
		
		$("input[name='inicioPeriodoCarencia']").datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true,
			defaultDate: new Date()
		});
		
		$("input[name='inicioPeriodoCarencia']").mask("99/99/9999");
		
		$("input[name='fimPeriodoCarencia']").datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true,
			defaultDate: new Date()
		});
		
		$("input[name='fimPeriodoCarencia']").mask("99/99/9999");
		
		D.$("taxaFixaEntregaBanca").maskMoney({decimal:",", thousands:"."});
		
		
		$("input[name='percentualFaturamento']").mask("99.99");
		
		var options = {
			success: D.tratarRetornoUpload
	    };
		
		$('#formUploadTermoAdesao').ajaxForm(options);
		
		$('#formUploadProcuracao').ajaxForm(options);
	});
}

//@ sourceURL=distribuicao.js
