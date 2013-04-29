var ConferenciaEncalheCont = $.extend(true, {
	
	modalAberta: false,
	
	verificarReabertura: false,
	
	idProdutoEdicaoNovoEncalhe: "",

	init : function() {
		
		$(".outrosVlrsGrid", ConferenciaEncalheCont.workspace).flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Data',
				name : 'data',
				width : 100,
				sortable : false,
				align : 'left'
			}, {
				display : 'Tipo de Lançamento',
				name : 'tipoLancamentoDescricao',
				width : 140,
				sortable : false,
				align : 'left'
			},{
				display : 'Observações',
				name : 'observacoes',
				width : 140,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 100,
				sortable : false,
				align : 'right'
			} ],
			width : 540,
			height : 250
		});
		
		//hack para message dialog com tamanho fixo
		$(".message-dialog-encalhe > div", ConferenciaEncalheCont.workspace).css('width', '93%');
		
		$("#numeroCota", ConferenciaEncalheCont.workspace).numeric();
		$("#numeroCota", ConferenciaEncalheCont.workspace).focus();
		$("#exemplaresNovoEncalhe", ConferenciaEncalheCont.workspace).numeric();
		
		$("#vlrCE", ConferenciaEncalheCont.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
		
		$("#qtdCE", ConferenciaEncalheCont.workspace).numeric();
		
		$("#dataNotaFiscal", ConferenciaEncalheCont.workspace).mask("99/99/9999");
		
		$("#numeroCota", ConferenciaEncalheCont.workspace).keyup(function(e) {
			
			if (e.keyCode == 13) {

				if (ConferenciaEncalheCont.verificarReabertura){
				    
				    ConferenciaEncalheCont.pesquisarCota();
			    }
			    else{
			
			    	ConferenciaEncalheCont.verificarReabertura = true;
		    	}
			}
		});
		
		$("#lstProdutos", ConferenciaEncalheCont.workspace).keyup(function(e){
			
			ConferenciaEncalheCont.pesquisarProdutoPorCodigoNome();
		});
		
		ConferenciaEncalheCont.criarComboBoxEncalhe();

		ConferenciaEncalheCont.removerAtalhos();
		
		ConferenciaEncalheCont.atribuirAtalhos();
	},

	removerAtalhos: function() {
		
		$(document.body).unbind('keydown.adicionarProduto');
		$(document.body).unbind('keydown.popUpNotaFiscal');
		$(document.body).unbind('keydown.salvarConferencia');
		$(document.body).unbind('keydown.finalizarConferencia');
		
	},
	
	atribuirAtalhos: function(){
		$(document.body).unbind();
		
		$(document.body).bind('keydown.adicionarProduto', jwerty.event('F2',function() {
			
			if (!ConferenciaEncalheCont.modalAberta){
				
				ConferenciaEncalheCont.limparCamposNovoEncalhe();
				ConferenciaEncalheCont.popup_novo_encalhe();
			}
		}));
		
		$(document.body).bind('keydown.popUpNotaFiscal', jwerty.event('F6',function() {
			
			if (!ConferenciaEncalheCont.modalAberta){
				
				ConferenciaEncalheCont.popup_notaFiscal();
			}
			
		}));
		
		
		$(document.body).bind('keydown.salvarConferencia', jwerty.event('F8',function() {
			
			if (!ConferenciaEncalheCont.modalAberta){
				
				ConferenciaEncalheCont.popup_salvarInfos();
			}
			
		}));
		
		$(document.body).bind('keydown.finalizarConferencia', jwerty.event('F9',function() {
			
			if (!ConferenciaEncalheCont.modalAberta){
				
				ConferenciaEncalheCont.veificarCobrancaGerada();
			}
			
		}));
		
	},
	
	pesquisarCota : function(){
		
		var data = [
		            {name: 'numeroCota', value : $("#numeroCota", ConferenciaEncalheCont.workspace).val()}, 
		            {name: 'indObtemDadosFromBD', value : true},
		            {name: 'indConferenciaContingencia', value: true}
		           ];
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/verificarReabertura", data,
			function(result){
				
				if (typeof result.IND_REABERTURA != 'undefined' && result.IND_REABERTURA == 'S'){
					
					ConferenciaEncalheCont.modalAberta = true;
					
					$("#dialog-reabertura", ConferenciaEncalheCont.workspace).dialog({
						resizable : false,
						height : 200,
						width : 360,
						modal : true,
						buttons : {
							"Sim" : function() {
								
								$("#dialog-reabertura", ConferenciaEncalheCont.workspace).dialog("close");
								
								ConferenciaEncalheCont.carregarListaConferencia(data);
								
								ConferenciaEncalheCont.ifCotaEmiteNfe(data, ConferenciaEncalheCont.popup_alert);
							},
							"Não" : function() {
								
								$("#dialog-reabertura", ConferenciaEncalheCont.workspace).dialog("close");
							}
						}, close : function(){
							
							ConferenciaEncalheCont.modalAberta = false;
							
							ConferenciaEncalheCont.verificarReabertura = false;
							
							$("#numeroCota", ConferenciaEncalheCont.workspace).focus();
						},
						form: $("#dialog-reabertura", this.workspace).parents("form")
					});
				} else {
					
					if(typeof result.IND_COTA_RECOLHE_NA_DATA != undefined && result.IND_COTA_RECOLHE_NA_DATA == 'N' ) {
						
						exibirMensagem('WARNING', [result.msg]);
						
					} 
					
					ConferenciaEncalheCont.carregarListaConferencia(data);
					
					$("#dialog-reabertura", ConferenciaEncalheCont.workspace).dialog("close");
					ConferenciaEncalheCont.ifCotaEmiteNfe(data, ConferenciaEncalheCont.popup_alert);
					
				}
			}
		);
	},
	ifCotaEmiteNfe :  function(data, fnCotaEmiteNfe) {
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/verificarCotaEmiteNFe", data, 
		function(result){
			if(result.IND_COTA_EMITE_NFE) {
				fnCotaEmiteNfe();
			} 
		});
	},
	
	recalcularValoresFinalizar : function(index){
		
		var data = [
            {name: "idConferencia", value: $("#idConferenciaEncalheHiddenFinalizarConf_" + index, ConferenciaEncalheCont.workspace).val()},
            {name: "qtdInformada", value: $('#qtdeInformadaFinalizarConf_' + index, ConferenciaEncalheCont.workspace).val()},
            {name: "valorCapaInformado", value : $('#precoCapaFinalizarConf_' + index, ConferenciaEncalheCont.workspace).val()}
		];
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/alterarQtdeValorInformado", 
			data, 
			function(result){
				
				$("#valorTotalConferenciaFinalizar_" + index, ConferenciaEncalheCont.workspace).text(parseFloat(result.conf.valorTotal).toFixed(2));
				
				$("#somatorioQtdInformada", ConferenciaEncalheCont.workspace).text(parseFloat(result.qtdInformada).toFixed(2));
				$("#somatorioQtdRecebida", ConferenciaEncalheCont.workspace).text(parseFloat(result.qtdRecebida).toFixed(2));
				$("#somatorioTotal", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorPagarAtualizado).toFixed(2));
			}
		);
	},
	
	criarComboBoxEncalhe : function() {
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/carregarComboBoxEncalheContingencia", null,
				
			function(result){
				
				var opcoesBox = '';
				
				$.each(result, function(key, value) {
					opcoesBox = opcoesBox + "<option value="+key+">"+value+"</option>"; 
				});
				
				$('#boxLogado', ConferenciaEncalheCont.workspace).html(opcoesBox);
				
				ConferenciaEncalheCont.popup_logado();
				
			}
		);
		
		
	},
	
	popup_logado : function() {
		
		ConferenciaEncalheCont.modalAberta = true;
		
		$("#dialog-logado", ConferenciaEncalheCont.workspace).dialog({
			resizable : false,
			height : 180,
			width : 460,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/salvarIdBoxSessao", {idBox:$("#boxLogado").val()}, 
						function(){
							
							$("#dialog-logado", ConferenciaEncalheCont.workspace).dialog("close");
							$('#numeroCota', ConferenciaEncalheCont.workspace).focus();
						}, null, true, "idModalBoxRecolhimento"
					);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
					$('#pesq_cota', ConferenciaEncalheCont.workspace).focus();
				}
			}, open : function(){
				
				$("#boxLogado", ConferenciaEncalheCont.workspace).focus();
			}, close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			},
			form: $("#dialog-logado", this.workspace).parents("form")			
		});
	},
	
	gerarDocumentosConferenciaEncalhe : function(tiposDocumento) {
		var cont = 1;

		//Imprime todos os documentos recebidos
		for(i=0;i < tiposDocumento.length;i++){
			
			var data = [{name: 'tipo_documento_impressao_encalhe', value: tiposDocumento[i]}];
			$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/imprimirDocumentosCobranca', 
					data,
					function(resultado){
				
				if(resultado != "" && resultado.resultado!=""){
					
					var callApplet = '';
					callApplet+='<applet archive="scripts/applet/ImpressaoFinalizacaoEncalheApplet.jar" code="br.com.abril.nds.matricial.ImpressaoFinalizacaoEncalheApplet.class" width="10" height="10">'
						callApplet+='	<param name="tipo_documento_impressao_encalhe" value="'+resultado.tipo_documento_impressao_encalhe+'">';
					callApplet+='	<param name="conteudoImpressao" value="'+resultado.resultado+'">';
					callApplet+='</applet>';						
					
					$('#replaceAppletFinal'+cont).html(callApplet);
					$('#idImpressaoFinalizacaoApplet'+cont, ConferenciaEncalhe.workspace).show();		
					
					$('#replaceAppletFinal'+cont).html("");
					$('#idImpressaoFinalizacaoApplet'+cont, ConferenciaEncalhe.workspace).hide();		
					cont++;
				}
			}
			); 	
		}
	},
	
	verificarValorTotalNotaFiscal : function() {
		var data = [{name: 'indConferenciaContingencia', value: true}];
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/verificarValorTotalNotaFiscal', data,
				
				function(result){
				
					if(result.tipoMensagem == 'SUCCESS') {
						
						if(result.indGeraDocumentoConfEncalheCota == true) {

							ConferenciaEncalheCont.gerarDocumentosConferenciaEncalhe(result.tipos_documento_impressao_encalhe);
							
						}
						
						ConferenciaEncalheCont.limpaTela();
						
					} else {

						$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/carregarListaConferencia', 
							null, 
							function(result){
								
								ConferenciaEncalheCont.abrirDialogNotaFiscalDivergente(result);
							}
						);
					}
				
					exibirMensagem(result.tipoMensagem, result.listaMensagens);
				}, null , true, "idModalDadosNotaFiscal"
		);
		
	},
	
	verificarValorTotalCE : function() {
		
		var data = [{name: "valorCEInformado", value: parseFloat($("#vlrCE", ConferenciaEncalheCont.workspace).val())},
		            {name: "qtdCEInformado", value: parseFloat($("#qtdCE", ConferenciaEncalheCont.workspace).val())}];
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/verificarValorTotalCE', data, 
		
			function(conteudo) {
				
				if(conteudo.valorCEInformadoValido == true) {
					
					ConferenciaEncalheCont.verificarValorTotalNotaFiscal();
					
				} else {
					
					$("#msgConfirmar", ConferenciaEncalheCont.wokspace).text(conteudo.mensagemConfirmacao);
					
					$("#dialog-confirmar", ConferenciaEncalheCont.workspace).dialog({
						resizable : false,
						height : 160,
						width : 400,
						modal : true,
						buttons : {
							"Sim" : function() {
								
								ConferenciaEncalheCont.verificarValorTotalNotaFiscal();
								$("#dialog-confirmar", ConferenciaEncalheCont.workspace).dialog("close");
							},
							"Não" : function() {
								
								$("#dialog-confirmar", ConferenciaEncalheCont.workpace).dialog("close");
							}
						},
						form: $("#dialog-confirmar", ConferenciaEncalheCont.workspace).parents("form")
					});
				}
			
			}
		);
	},	
	
	carregarListaConferencia: function(data){
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/carregarListaConferencia', 
				data, 
				function(result){
					
					ConferenciaEncalheCont.preProcessarConsultaConferenciaEncalhe(result);
					
					bloquearItensEdicao(ConferenciaEncalheCont.workspace);
				}
		);
	},
	indDistribuidorAceitaJuramentado:null,
	preProcessarConsultaConferenciaEncalhe : function(result){
		
		if (result.mensagens){
			
			exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
			return;
		}
		
		var innerTable = '';
		
		var modeloConferenciaEncalhe = result.listaConferenciaEncalhe;
		
		var totalExemplaresFooter = 0;
		
		ConferenciaEncalheCont.indDistribuidorAceitaJuramentado = result.indDistribuidorAceitaJuramentado;
		
		$("._dadosConfEncalhe", ConferenciaEncalheCont.workspace).remove();
		
		if (modeloConferenciaEncalhe){
		
			$.each(modeloConferenciaEncalhe, 
				function(index, value) {
				
					var parcial = value.parcial;
				
					var _class;
					
					if (index % 2 == 0){
						_class = "class_linha_1 _dadosConfEncalhe";
					} else {
						_class = "class_linha_2 _dadosConfEncalhe";
					}
					
					innerTable += "<tr class='" + _class + " _dados' name='linhaConfEncalhe'>";
					
					innerTable += "<input type='hidden' id='idProdutoEdicaoGrid_"+index+"' value='" + value.idProdutoEdicao + "'/>";
					
					innerTable += "<td nowrap='nowrap'>" + value.codigo + "</td>";
					
					innerTable += "<td nowrap='nowrap'>" + value.nomeProduto + "</td>";
					
					innerTable += "<td nowrap='nowrap' style='text-align: center;'>" + value.numeroEdicao + "</td>";
					
					if (value.dia || value.dataRecolhimento){
						
						if (value.dia && value.dia > 0){
						
							innerTable += "<td style='text-align: center;' nowrap='nowrap'>" + value.dia + "º" + "</td>";
						} else {
							
							innerTable += "<td style='text-align: center;' nowrap='nowrap' style='width: 20px;'>" + value.dataRecolhimento + "</td>";
						}
					} else {
						
						innerTable += "<td></td>";
					}
					
					
					innerTable += "<td style='text-align: right;' nowrap='nowrap'>" + parseFloat(value.precoCapa).toFixed(2) + "</td>";
					
					innerTable += "<td style='text-align: right;' nowrap='nowrap'>" + parseFloat(value.desconto).toFixed(2) + "</td>";
					
					var valorExemplares = parseInt(value.qtdExemplar);
					
					totalExemplaresFooter += valorExemplares;
					
					innerTable += "<td style='text-align: center;' nowrap='nowrap'>" + value.qtdReparte + "</td>";
					
					innerTable += "<td nowrap='nowrap' style='text-align: center;'>";
					
					var inputExemplares = '<input isEdicao="true" name="inputValorExemplares" tabindex="' + (++index) + '" onkeydown="ConferenciaEncalheCont.nextInputExemplares('+index+', window.event);" id="qtdExemplaresGrid_' + index + '" maxlength="255" onkeyup="ConferenciaEncalheCont.redefinirValorTotalExemplaresFooter()" onchange="ConferenciaEncalheCont.atualizarValores('+ index +','+value.qtdInformada+');" style="width:90px; text-align: center;" value="' + value.qtdInformada + '"/>' +
						'<input id="idConferenciaEncalheHidden_' + index + '" type="hidden" value="' + value.idConferenciaEncalhe + '"/>';
					
					innerTable += inputExemplares + "</td>";
					
					innerTable += "<td align='right' nowrap='nowrap' id='valorTotalConferencia_" + index + "'>" + parseFloat(value.valorTotal).toFixed(2) + "</td>";
					
					var inputCheckBoxJuramentada = '';
					
					if (ConferenciaEncalheCont.indDistribuidorAceitaJuramentado == true) {

						if(parcial == true) {
							
							inputCheckBoxJuramentada = '<input isEdicao="true" type="checkbox" ' + (value.juramentada == true ? 'checked="checked"' : '')
							+ ' onchange="ConferenciaEncalheCont.atualizarValores('+ index +');" id="checkGroupJuramentada_' + index + '"/>';
							
							
						} else {
							
							inputCheckBoxJuramentada = '<input isEdicao="true" type="checkbox" disabled="disabled" id="checkGroupJuramentada_' + index + '"/>';
							
						}
						
						innerTable += "<td style='text-align: center;' nowrap='nowrap'>" + inputCheckBoxJuramentada + "</td>";
					
					} 
					
					innerTable += "</tr>";
					
					$(innerTable).appendTo("#dadosGridConferenciaEncalheContingencia", ConferenciaEncalheCont.workspace);
					
					innerTable = '';
				}
			);
			
			if (!ConferenciaEncalheCont.indDistribuidorAceitaJuramentado) {
				
				$("#colunaJuramentada", ConferenciaEncalheCont.workspace).hide();
			}
			
			$("#totalExemplaresFooter", ConferenciaEncalheCont.workspace).html(totalExemplaresFooter);
			
			$('input[id*="qtdExemplaresGrid"]', ConferenciaEncalheCont.workspace).numericE();
		}
		
		$(".outrosVlrsGrid", ConferenciaEncalheCont.workspace).flexAddData({
			page: result.listaDebitoCredito.page, total: result.listaDebitoCredito.total, rows: ConferenciaEncalheCont.arredondarValorDebitoCredito(result.listaDebitoCredito.rows)
		});
		
		$("#totalReparte", ConferenciaEncalheCont.workspace).text(parseFloat(result.reparte).toFixed(2));
		$("#totalEncalhe", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorEncalhe).toFixed(2));
		$("#valorVendaDia", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorVendaDia).toFixed(2));
		$("#totalOutrosValores", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorDebitoCredito).toFixed(2));
		$("#valorAPagar", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorPagar).toFixed(2));
		
		$(".dadosFiltro", ConferenciaEncalheCont.workspace).show();
		$("#nomeCota", ConferenciaEncalheCont.workspace).text(result.razaoSocial);
		$("#statusCota", ConferenciaEncalheCont.workspace).text(result.situacao);
		
		focusSelectRefField($("[name=inputValorExemplares]", ConferenciaEncalhe.workspace).first());
	},
	
	arredondarValorDebitoCredito : function(listaDebitoCredito) {
		
		$.each(listaDebitoCredito, function(index, value){
				value.cell.valor = parseFloat(value.cell.valor).toFixed(2);
		});
		
		return listaDebitoCredito;
		
	},
	
	atualizarValores: function(index, valorReal) {
		
		var juramentado = false;
		
		if( $("#checkGroupJuramentada_" + index, ConferenciaEncalheCont.workspace).attr("checked") == 'checked' ) {
			juramentado = true;
		}
		
		var data = [
            {name: "idConferencia", value: $("#idConferenciaEncalheHidden_" + index, ConferenciaEncalheCont.workspace).val()},
            {name: "qtdExemplares", value: $("#qtdExemplaresGrid_" + index, ConferenciaEncalheCont.workspace).val()},
            {name: "juramentada", value: juramentado}, {name: 'indConferenciaContingencia', value: true}
		];
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/atualizarValores", 
			data, 
			function(result){
				
				$("#valorTotalConferencia_" + index, ConferenciaEncalheCont.workspace).text(parseFloat(result.conf.valorTotal).toFixed(2));
				
				$("#totalReparte", ConferenciaEncalheCont.workspace).text(parseFloat(result.reparte).toFixed(2));
				$("#totalEncalhe", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorEncalhe).toFixed(2));
				$("#valorVendaDia", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorVendaDia).toFixed(2));
				$("#totalOutrosValores", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorDebitoCredito).toFixed(2));
				$("#valorAPagar", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorPagar).toFixed(2));
			},
			function(result) {

				if (result.mensagens){

					$("#qtdExemplaresGrid_" + index, ConferenciaEncalheCont.workspace).val(valorReal);
				}
			}
		);
	},
	
	pesquisarProdutoPorCodigoNome: function(){
		
		var codigoNomeProduto = $("#lstProdutos", ConferenciaEncalheCont.workspace).val();
		
		if (codigoNomeProduto && codigoNomeProduto.length > 0){
			$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/pesquisarProdutoPorCodigoNome', 
					{codigoNomeProduto:codigoNomeProduto}, 
				function(result){
					
					if (result[0]){
						
						$("#lstProdutos", ConferenciaEncalheCont.workspace).autocomplete({
							source: result,
							select: function(event, ui){
								
								$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/pesquisarProdutoEdicaoPorId",
									[{name: "idProdutoEdicao", value: ui.item.chave.long}],
									function(result2){
									
										if (result2){
											ConferenciaEncalheCont.idProdutoEdicaoNovoEncalhe = ui.item.chave.long;
											$("#lstProdutos", ConferenciaEncalheCont.workspace).val(ui.item.chave.string);
											$("#numEdicaoNovoEncalhe", ConferenciaEncalheCont.workspace).val(result2.numeroEdicao);
											$("#precoCapaNovoEncalhe", ConferenciaEncalheCont.workspace).val(parseFloat(result2.precoVenda).toFixed(2));
											$("#descontoNovoEncalhe", ConferenciaEncalheCont.workspace).val(parseFloat(result2.desconto).toFixed(2));
										}
											
										
									}, function() {
									
										ConferenciaEncalheCont.limparCamposNovoEncalhe();
										$("#lstProdutos", ConferenciaEncalheCont.workspace).focus();
									}, 
									true, "idModalNovoEncalhe"
								);
							}
						});
					}
				}, null, true, "idModalNovoEncalhe"
			);
		}
	},
	
	adicionarEncalhe: function(){	

		var existeProduto = false;
		
		$("input[id*='idProdutoEdicaoGrid']", ConferenciaEncalheCont.workspace).each(function(){
			
			if (ConferenciaEncalheCont.idProdutoEdicaoNovoEncalhe == $(this).val()) {
				
				exibirMensagem("WARNING", ["Este produto já está sendo utilizado."]);
				
				existeProduto = true;
				
				return false;
			}
		});

		if (existeProduto) {
		
			return;
		}
		
		var data = [{name: "idProdutoEdicao", value: ConferenciaEncalheCont.idProdutoEdicaoNovoEncalhe}, 
		            {name: "quantidade", value: $("#exemplaresNovoEncalhe", ConferenciaEncalheCont.workspace).val()},
		            {name:"juramentada", value:$('#checkboxJueramentadaNovoEncalhe', ConferenciaEncalheCont.workspace).attr('checked') == 'checked' }];
		
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/adicionarProdutoConferido', data,
			function(result){
				
				ConferenciaEncalheCont.preProcessarConsultaConferenciaEncalhe(result);
				
				ConferenciaEncalheCont.limparCamposNovoEncalhe();
				
				$("#dialog-encalhe", ConferenciaEncalheCont.workspace).dialog("close");
				
				$("#lstProdutos", ConferenciaEncalheCont.workspace).focus();
				
				bloquearItensEdicao(ConferenciaEncalheCont.workspace);

			}, null, true, "idModalNovoEncalhe"
		);
	},
	
	limparCamposNovoEncalhe: function(){
		
		$("#lstProdutos", ConferenciaEncalheCont.workspace).val("");
		$("#numEdicaoNovoEncalhe", ConferenciaEncalheCont.workspace).val("");
		$("#precoCapaNovoEncalhe", ConferenciaEncalheCont.workspace).val("");
		$("#descontoNovoEncalhe", ConferenciaEncalheCont.workspace).val("");
		$("#exemplaresNovoEncalhe", ConferenciaEncalheCont.workspace).val("");
		$("#valorTotalNovoEncalhe", ConferenciaEncalheCont.workspace).val("");
		
		if(ConferenciaEncalheCont.indDistribuidorAceitaJuramentado){
			$("#checkboxJueramentadaNovoEncalhe", ConferenciaEncalheCont.workspace).removeAttr('disabled');
		}else{
			$("#checkboxJueramentadaNovoEncalhe", ConferenciaEncalheCont.workspace).attr('disabled', true);
		}
		$("#checkboxJueramentadaNovoEncalhe", ConferenciaEncalheCont.workspace).removeAttr("checked");
	},
	
	calcularValorTotalNovoEncalhe: function() {
		
		$("#valorTotalNovoEncalhe", ConferenciaEncalheCont.workspace).val(parseFloat(($("#precoCapaNovoEncalhe", ConferenciaEncalheCont.workspace).val() - $("#descontoNovoEncalhe", ConferenciaEncalheCont.workspace).val()) * $("#exemplaresNovoEncalhe", ConferenciaEncalheCont.workspace).val()).toFixed(2));
	},
	
	
	carregarGridItensNotaFiscal : function(modeloConferenciaEncalhe) {
	
		$("._dadosConfEncalheFinalizar", ConferenciaEncalheCont.workspace).remove();
		
		var innerTable = '';
		
		$.each(modeloConferenciaEncalhe, 
				function(index, value) {
					
					var _class;
					
					if (index % 2 == 0){
						_class = "class_linha_1 _dadosConfEncalheFinalizar";
					} else {
						_class = "class_linha_2 _dadosConfEncalheFinalizar";
					}
					
					innerTable += "<tr class='" + _class + "'>";
					
					innerTable += "<td>" + value.codigo + "<input id='idConferenciaEncalheHiddenFinalizarConf_"+ index +"' type='hidden' value='" + value.idConferenciaEncalhe + "'/></td>";
					
					innerTable += "<td>" + value.nomeProduto + "</td>";
					
					innerTable += "<td style='text-align: center;'>" + value.numeroEdicao + "</td>";
					
					if (value.dia || value.dataRecolhimento){
						
						if (value.dia && value.dia > 0){
						
							innerTable += "<td style='text-align: center;' nowrap='nowrap'>" + value.dia + "º" + "</td>";
						} else {
							
							innerTable += "<td style='text-align: center;' nowrap='nowrap' style='width: 20px;'>" + value.dataRecolhimento + "</td>";
						}
					} else {
						
						innerTable += "<td></td>";
					}
					
					innerTable +=
						'<td style="text-align: center"><input id="qtdeInformadaFinalizarConf_'+ index +'" onchange="ConferenciaEncalheCont.recalcularValoresFinalizar('+ index +');" type="text" maxlength="255" style="width:50px; text-align: center;" value="' + parseInt(value.qtdInformada) + '"/></td>';
					
					innerTable += "<td style='text-align: center;'>" + (value.qtdExemplar ? parseInt(value.qtdExemplar) : "0") + "</td>";
				
					innerTable +=
						'<td style="text-align: center;"><input id="precoCapaFinalizarConf_'+ index +'" onchange="ConferenciaEncalheCont.recalcularValoresFinalizar('+ index +');" maxlength="255" style="width:50px; text-align: right;" value="' + parseFloat(value.precoCapaInformado).toFixed(2) + '"/></td>';
					
					innerTable += "<td style='text-align: right;'>" + parseFloat(value.desconto).toFixed(2) + "</td>";
					
					innerTable += "<td style='text-align: right;' id='valorTotalConferenciaFinalizar_" + index + "'>" + parseFloat(value.valorTotal).toFixed(2) + "</td>";
					
					var imgExclusao = '<img src="' + contextPath + '/images/ico_excluir.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />';
					innerTable += '<td style="text-align: center;"><a href="javascript:;" onclick="ConferenciaEncalheCont.excluirConferencia(' + value.idConferenciaEncalhe + ');">' + imgExclusao + '</a></td>';
					
					innerTable += "</tr>";
					
					$(innerTable).appendTo("#dadosGridConferenciaEncalheFinalizar", ConferenciaEncalheCont.workspace);
					
					innerTable = '';
				}
			);
	},
	
	excluirConferencia : function(idConferenciaEncalhe){
		
		ConferenciaEncalheCont.modalAberta = true;
		
		$("#dialog-excluir-conferencia", ConferenciaEncalheCont.workspace).dialog({
			resizable : false,
			height : 180,
			width : 460,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					var data = [{name : "idConferenciaEncalhe", value : idConferenciaEncalhe}];
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/excluirConferencia', data,
							
						function (result){
							
							ConferenciaEncalheCont.preProcessarConsultaConferenciaEncalhe(result);
							
							ConferenciaEncalheCont.carregarGridItensNotaFiscal(result.listaConferenciaEncalhe);
							
							bloquearItensEdicao(ConferenciaEncalheCont.workspace);
							
							$("#dialog-excluir-conferencia", ConferenciaEncalheCont.workspace).dialog("close");
						}
					
					);
				},
				"Cancelar" : function() {
					
					$(this).dialog("close");
					$('#pesq_cota', ConferenciaEncalheCont.workspace).focus();
				}
			}, close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			},
			form: $("#dialog-excluir-conferencia", this.workspace).parents("form")			
		});
	},
	
	mostrarChaveAcesso : function() {
		
		if($('input:radio[name=radioNFE]:checked').val() == 'S') {
			
			$("#divForChaveAcessoNFE", ConferenciaEncalheCont.workspace).show();
			
		} else {

			$("#divForChaveAcessoNFE", ConferenciaEncalheCont.workspace).hide();
			
		}
	},
	
	redefinirValorTotalExemplaresFooter: function() {
		
		var totalAtualizado = 0;
		
		$("input[name='inputValorExemplares']", ConferenciaEncalheCont.workspace).each(function() {
			
			var parsedValue = parseInt($(this, ConferenciaEncalheCont.workspace).val());
			
			var valor = isNaN(parsedValue) ? 0 : parsedValue;

			totalAtualizado += valor;
		});
		
		$("#totalExemplaresFooter", ConferenciaEncalheCont.workspace).html(totalAtualizado);
	},

	validarInputExemplares :function(curIndex, valorRealExemplares) {
		
		var input = $('[tabindex='+ curIndex + ']');
		
		var valorInputado = input.val();

		if (valorInputado > valorRealExemplares) {
			
			exibirMensagem('WARNING', [ "O valor digitado deve ser menor ou igual ao total do reparte." ]);
			
			input.val(valorRealExemplares);
			
			this.redefinirValorTotalExemplaresFooter();
		}
	},

	nextInputExemplares : function(curIndex, evt) {
	
		if (evt.keyCode == 13 || evt.keyCode == 40) {
			var nextElement = $('[tabindex=' + (curIndex + 1) + ']');
			nextElement.focus();
			nextElement.select();
		}else if (event.keyCode == 38) {
			var nextElement = $('[tabindex=' + (curIndex - 1) + ']');
			nextElement.focus();
			nextElement.select();  
		} 
	},
	
	abrirDialogNotaFiscalDivergente: function(result){
		$(".message-dialog-encalhe > div", ConferenciaEncalheCont.workspace).hide();
		if (result.mensagens){
			
			exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
			return;
		}
		
		ConferenciaEncalheCont.modalAberta = true;
		
		$("#dialog-dadosNotaFiscalContingencia", ConferenciaEncalheCont.workspace).dialog({
			
			resizable : false,
			height : 'auto',
			width : 877,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					var data = [{name: 'indConferenciaContingencia', value: true}];
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/finalizarConferencia', data,
						
						function(conteudo){
							
						if(conteudo.tipoMensagem == 'SUCCESS') {
							
							$("#dialog-dadosNotaFiscalContingencia", ConferenciaEncalheCont.workspace).dialog("close");
							
							if(conteudo.indGeraDocumentoConfEncalheCota == true) {

								ConferenciaEncalheCont.gerarDocumentosConferenciaEncalhe(conteudo.tipos_documento_impressao_encalhe);
								
							}
							
						}

						exibirMensagem(conteudo.tipoMensagem, conteudo.listaMensagens);
						
						
						}, function(conteudo) {
							
							ConferenciaEncalheCont.limpaTela();
							
							
						}, true, "idModalDadosNotaFiscal"
						
					);
					
				},
				"Cancelar" : function() {
					
					$(this).dialog("close");
				}
			}, close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			},
			form: $("#dialog-dadosNotaFiscalContingencia", this.workspace).parents("form")			
		});
		
		var notaFiscal = result.notaFiscal;
		
		if (notaFiscal){
		
			$("#numeroNotaFiscalExibir", ConferenciaEncalheCont.workspace).text(notaFiscal.numero);
			$("#serieExibir", ConferenciaEncalheCont.workspace).text(notaFiscal.serie);
			$("#dataExibir", ConferenciaEncalheCont.workspace).text(notaFiscal.dataEmissao);
			$("#valorTotalNotaFiscalExibir", ConferenciaEncalheCont.workspace).text(parseFloat(notaFiscal.valorProdutos).toFixed(2));
			$("#chaveAcessoExibir", ConferenciaEncalheCont.workspace).text(notaFiscal.chaveAcesso);
		}
		
		
		
		var modeloConferenciaEncalhe = result.listaConferenciaEncalhe;
		
		$("._dadosConfEncalheFinalizar", ConferenciaEncalheCont.workspace).remove();
		
		if (modeloConferenciaEncalhe){
		
			ConferenciaEncalheCont.carregarGridItensNotaFiscal(modeloConferenciaEncalhe);
			
			$('input[id*="qtdeInformadaFinalizarConf"]', ConferenciaEncalheCont.workspace).numeric();
			$('input[id*="precoCapaFinalizarConf"]', ConferenciaEncalheCont.workspace).numeric();
		}
		
		$("#somatorioQtdInformada", ConferenciaEncalheCont.workspace).text(parseInt(result.qtdInformada));
		$("#somatorioQtdRecebida", ConferenciaEncalheCont.workspace).text(parseInt(result.qtdRecebida));
		$("#somatorioTotal", ConferenciaEncalheCont.workspace).text(parseFloat(result.valorPagar).toFixed(2));
		
		bloquearItensEdicao(ConferenciaEncalheCont.workspace);
	},
	
	popup_novo_encalhe: function () {
		
		var _this = this;
		
		ConferenciaEncalheCont.modalAberta = true;
		$(".message-dialog-encalhe > div", ConferenciaEncalheCont.workspace).hide();
		$("#dialog-encalhe", ConferenciaEncalheCont.workspace).dialog({
			resizable : false,
			height : 'auto',
			width : 450,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					_this.adicionarEncalhe();
				},
				"Cancelar" : function() {
					
					$(this).dialog("close");
				}
			}, close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			},
			form: $("#dialog-encalhe", this.workspace).parents("form")			
		});

	},
	
	popup_alert : function() {
		
		ConferenciaEncalheCont.modalAberta = true;
		$(".message-dialog-encalhe > div", ConferenciaEncalheCont.workspace).hide();
		$("#dialog-alert", ConferenciaEncalheCont.workspace).dialog({
			resizable : false,
			height : 190,
			width : 460,
			modal : true,
			buttons : {
				"Sim" : function() {
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/carregarNotaFiscal', null, 
						function(result){
							
							$("#numNotaFiscal", ConferenciaEncalheCont.workspace).val(result.numero);
							$("#serieNotaFiscal", ConferenciaEncalheCont.workspace).val(result.serie);
							$("#dataNotaFiscal", ConferenciaEncalheCont.workspace).val(result.dataEmissao);
						    $("#chaveAcessoNFE", ConferenciaEncalheCont.workspace).val(result.chaveAcesso);
							
						    if (result.valorProdutos){
							    
						    	$("#valorNotaFiscal", ConferenciaEncalheCont.workspace).val(parseFloat(result.valorProdutos).toFixed(2));
								
						    }
							
							$("#dialog-alert", ConferenciaEncalheCont.workspace).dialog("close");
							ConferenciaEncalheCont.popup_notaFiscal();
						}
					);
				},
				"Não" : function() {
					
					$("#dialog-alert", ConferenciaEncalheCont.workspace).dialog("close");
					
					$("#vlrCE", ConferenciaEncalheCont.workspace).focus();
					
					$("#qtdCE", ConferenciaEncalheCont.workspace).focus();
					
				}
			}, open : function(){
				
				$(this).parent('div', ConferenciaEncalheCont.workspace).find('button:contains("Sim")').focus();
			}, close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			},
			form: $("#dialog-alert", this.workspace).parents("form")			
		});
		
		$("#dialog-alert", ConferenciaEncalheCont.workspace).show();
	},
	
	popup_notaFiscal: function () {
		
		ConferenciaEncalheCont.modalAberta = true;
		$(".message-dialog-encalhe > div", ConferenciaEncalheCont.workspace).hide();
		$("#dialog-notaFiscal", ConferenciaEncalheCont.workspace).dialog({
			resizable : false,
			height : 360,
			width : 750,
			modal : true,
			buttons : {
				
				"Confirmar" : function() {
					
					var data = []; 
					
					if($('input:radio[name=radioNFE]:checked', ConferenciaEncalheCont.workspace).val() == 'S') {
						
						data = [
								{name : "notaFiscal.numero", value : $("#numNotaFiscal", ConferenciaEncalheCont.workspace).val()},
								{name : "notaFiscal.serie", value : $("#serieNotaFiscal", ConferenciaEncalheCont.workspace).val()},
								{name : "notaFiscal.dataEmissao", value : $("#dataNotaFiscal", ConferenciaEncalheCont.workspace).val()},
								{name : "notaFiscal.valorProdutos", value : $("#valorNotaFiscal", ConferenciaEncalheCont.workspace).val()},
								{name : "notaFiscal.chaveAcesso", value : $("#chaveAcessoNFE", ConferenciaEncalheCont.workspace).val()}
						];
						
					} else {
						
						data = [
								{name : "notaFiscal.numero", value : $("#numNotaFiscal", ConferenciaEncalheCont.workspace).val()},
								{name : "notaFiscal.serie", value : $("#serieNotaFiscal", ConferenciaEncalheCont.workspace).val()},
								{name : "notaFiscal.dataEmissao", value : $("#dataNotaFiscal", ConferenciaEncalheCont.workspace).val()},
								{name : "notaFiscal.valorProdutos", value : $("#valorNotaFiscal", ConferenciaEncalheCont.workspace).val()}
						];
						
					}
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/salvarNotaFiscal', data, 
						function(result){
							
							$("#dialog-notaFiscal", ConferenciaEncalheCont.workspace).dialog("close");
							
							$("#vlrCE", ConferenciaEncalheCont.workspace).val(parseFloat($("#valorNotaFiscal", ConferenciaEncalheCont.workspace).val()).toFixed(2));
							
							$("#vlrCE", ConferenciaEncalheCont.workspace).focus();

							$("#qtdCE", ConferenciaEncalheCont.workspace).focus();

							
					}, null, true, "dialog-notaFiscal"
					);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
					$("#vlrCE", ConferenciaEncalheCont.workspace).focus();
					$("#qtdCE", ConferenciaEncalheCont.workspace).focus();
				}
			}, close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			},
			form: $("#dialog-notaFiscal", this.workspace).parents("form")			
		});

	},

	popup_outros_valores: function () {
		
		ConferenciaEncalheCont.modalAberta = true;
		$(".message-dialog-encalhe > div", ConferenciaEncalheCont.workspace).hide();
		$("#dialog-outros-valores", ConferenciaEncalheCont.workspace).dialog({
			resizable : false,
			height : 430,
			width : 600,
			modal : true,
			buttons : {
				"Fechar" : function() {
					
					$(this).dialog("close");
				}
			}, close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			},
			form: $("#dialog-outros-valores", this.workspace).parents("form")			
		});

	},
	
	popup_salvarInfos : function() {
		
		ConferenciaEncalheCont.modalAberta = true;
		$(".message-dialog-encalhe > div", ConferenciaEncalheCont.workspace).hide();
		$("#dialog-salvar", ConferenciaEncalheCont.workspace).dialog({
			resizable : false,
			height : 190,
			width : 460,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					var data = [{name: 'indConferenciaContingencia', value: true}];					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/salvarConferencia', data,
						function(result){
					
							if(result.tipoMensagem == 'SUCCESS') {
								
								ConferenciaEncalheCont.limpaTela();
								
							}						
							
							exibirMensagem(result.tipoMensagem, result.listaMensagens);
								
							$("#dialog-salvar", ConferenciaEncalheCont.workspace).dialog("close");
					
						}, function(conteudo) {
							
							ConferenciaEncalheCont.limpaTela();
							
							
						}, true, "idModalConfirmarSalvarConf"
					);
				},
				"Cancelar" : function() {
					
					$(this).dialog("close");
				}

			}, close : function(){
				
				ConferenciaEncalheCont.modalAberta = false;
			},
			form: $("#dialog-salvar", this.workspace).parents("form")			
		});

	},
	
	veificarCobrancaGerada: function(){
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/veificarCobrancaGerada', null,
		
			function(conteudo){
			
				if(conteudo && conteudo.tipoMensagem == 'WARNING') {
					
					$("#msgRegerarCobranca", ConferenciaEncalheCont.workspace).text(conteudo.listaMensagens[0]);
					
					$("#dialog-confirmar-regerar-cobranca", ConferenciaEncalheCont.workspace).dialog({
						resizable : false,
						height : 'auto',
						width : 680,
						modal : true,
						buttons : {
							"Confirmar" : function() {
								
								$("#dialog-confirmar-regerar-cobranca", ConferenciaEncalheCont.workspace).dialog("close");
								ConferenciaEncalheCont.verificarValorTotalCE();
							},
							"Cancelar" : function(){
							
								$("#dialog-confirmar-regerar-cobranca", ConferenciaEncalheCont.workspace).dialog("close");
							}
						},
						form: $("#dialog-confirmar-regerar-cobranca", ConferenciaEncalheCont.workspace).parents("form")
					});
					
				} else {
					
					ConferenciaEncalheCont.verificarValorTotalCE();
				}
				
			}, null, true, "dialog-confirmar-regerar-cobranca"
		);
	},
	limpaTela:function(){
		ConferenciaEncalheCont.preProcessarConsultaConferenciaEncalhe({reparte:0,valorEncalhe:0,valorVendaDia:0,valorDebitoCredito:0,valorPagar:0, listaDebitoCredito:{page:0,total:0, rows:null}});
		$(".dadosFiltro", ConferenciaEncalheCont.workspace).hide();
		$("#totalExemplaresFooter", ConferenciaEncalheCont.workspace).html(0);
		
		$("#numeroCota", ConferenciaEncalheCont.workspace).val("");
		
		$("#numeroCota", ConferenciaEncalheCont.workspace).select();
		
		$("#numeroCota", ConferenciaEncalheCont.workspace).focus();
		
		bloquearItensEdicao(ConferenciaEncalheCont.workspace);
	}
}, BaseController);

//@ sourceURL=scriptConferenciaEncalheCont.js