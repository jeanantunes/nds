function disableEnterKey(e) {
	
	var key;
     if(window.event)
          key = window.event.keyCode;     //IE
     else
          key = e.which;     //firefox
     if(key == 13)
    	 
    	 
         return false;
     else
          return true;
}

var ConferenciaEncalhe = $.extend(true, {
	
	modalAberta: false, 
	
	idProdutoEdicao: "",
	
	idConferenciaEncalheVisualizacao: "",

	outrosVlrsGridModel : [ {
		display : 'Data',
		name : 'dataLancamento',
		width : 100,
		sortable : true,
		align : 'left'
	}, {
		display : 'Tipo de Lançamento',
		name : 'tipoLancamentoDescricao',
		width : 140,
		sortable : true,
		align : 'left'
	}, {
		display : 'Observações',
		name : 'observacoes',
		width : 140,
		sortable : true,
		align : 'left'
	},{
		display : 'Valor R$',
		name : 'valor',
		width : 100,
		sortable : true,
		align : 'right'
	} ],

	ultimoCodeBar : "",
	ultimoSM : "",
	ultimoIdProdutoEdicao : "",

	init : function() {
	
		$("#pesq_prod", ConferenciaEncalhe.workspace).autocomplete({source: []});
		
		$('#cod_barras_conf_encalhe', ConferenciaEncalhe.workspace).autocomplete({source: []}); 
		
		$('#qtdeExemplar', ConferenciaEncalhe.workspace).focus();
	
		$("#dataNotaFiscal", ConferenciaEncalhe.workspace).datepicker({
			showOn : "button",
			buttonImage : contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly : true,
			dateFormat: "dd/mm/yy"
		});
	
		$(".outrosVlrsGrid", ConferenciaEncalhe.workspace).flexigrid({
			dataType : 'json',
			colModel : ConferenciaEncalhe.outrosVlrsGridModel,
			width : 540,
			height : 250,
			disableSelect: true
		});
		
		$("#numeroCota", ConferenciaEncalhe.workspace).numeric();
		                                           
		$("#qtdeExemplar", ConferenciaEncalhe.workspace).numericE();
		
		
		$("#vlrCE", this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2,
			 defaultZero:false
		});
		
		$("#dataNotaFiscal", ConferenciaEncalhe.workspace).mask("99/99/9999");
		
		$("#valorNotaFiscal", ConferenciaEncalhe.workspace).numeric();
		
		$("#sm", ConferenciaEncalhe.workspace).numeric();
		
		$("#numeroCota", ConferenciaEncalhe.workspace).keypress(function(e) {
			
			if(e.keyCode == 13) {
				
				ConferenciaEncalhe.pesquisarCota();
			}
		});
		
		$("#vlrCE", ConferenciaEncalhe.workspace).keypress(function(e) {
			
			if (e.keyCode == 13) {
				
				$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).focus();
			}
		});
		
		$('#cod_barras_conf_encalhe', ConferenciaEncalhe.workspace).keyup(function(e) {
			
			if (e.keyCode == 13) {
				
				ConferenciaEncalhe.autoCompletarPorCodigoDeBarras();
			}
		});
		
		$('#sm', ConferenciaEncalhe.workspace).keypress(function(e) {
			
			if (e.keyCode == 13) {
				
				if (ConferenciaEncalhe.ultimoSM != "" && 
						ConferenciaEncalhe.ultimoSM == $("#sm", ConferenciaEncalhe.workspace).val()){
					
					var qtd = $("#qtdeExemplar", ConferenciaEncalhe.workspace).val() == "" 
						? 0 
						: parseInt($("#qtdeExemplar", ConferenciaEncalhe.workspace).val());
					
					$("#qtdeExemplar", ConferenciaEncalhe.workspace).val(qtd + 1);
				} else {
					
					var data = [{name: "sm", value: $("#sm", ConferenciaEncalhe.workspace).val()}, 
					            {name: "idProdutoEdicaoAnterior", value: ConferenciaEncalhe.ultimoIdProdutoEdicao}];
					
					if ($("#qtdeExemplar", ConferenciaEncalhe.workspace).val().trim() != "") {
						data.push({name: "quantidade", value: $("#qtdeExemplar", ConferenciaEncalhe.workspace).val()});
					}
					
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/pesquisarProdutoEdicaoCodigoSM', data,
						function(result){
						
							ConferenciaEncalhe.setarValoresPesquisados(result);
							$("#sm", ConferenciaEncalhe.workspace).focus();
						}, null, false, "idTelaConferenciaEncalhe"
					);
				}
			}
		});
		
		$("#pesq_prod", ConferenciaEncalhe.workspace).keyup(function (e){
			
			if (e.keyCode == 13) {
				
				if (ConferenciaEncalhe.ultimoIdProdutoEdicao != "" && 
						ConferenciaEncalhe.ultimoIdProdutoEdicao == $("#pesq_prod", ConferenciaEncalhe.workspace).val()){
					
					var qtd = $("#qtdeExemplar", ConferenciaEncalhe.workspace).val() == "" 
						? 0 
						: parseInt($("#qtdeExemplar", ConferenciaEncalhe.workspace).val());
					
					$("#qtdeExemplar", ConferenciaEncalhe.workspace).val(qtd + 1);
					
					$("#dialog-pesquisar", ConferenciaEncalhe.workspace).dialog("close");
					$('#cod_barras_conf_encalhe', ConferenciaEncalhe.workspace).focus();
				} else {
					
					var data = [{name: "idProdutoEdicaoAnterior", value: ConferenciaEncalhe.ultimoIdProdutoEdicao}];
					
					if ($("#qtdeExemplar", ConferenciaEncalhe.workspace).val().trim() != "") {
						data.push({name: "quantidade", value: $("#qtdeExemplar", ConferenciaEncalhe.workspace).val()});
					}
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/pesquisarProdutoEdicao', data,
						function(result){
							
							ConferenciaEncalhe.setarValoresPesquisados(result);
							$("#dialog-pesquisar", ConferenciaEncalhe.workspace).dialog("close");
							$('#cod_barras_conf_encalhe', ConferenciaEncalhe.workspace).focus();
						},
						function(){
							
							$("#codProduto", ConferenciaEncalhe.workspace).val("");
						}, true, "idModalPesquisarProdutos"
					);
				}
			} else if (e.keyCode != 38 && e.keyCode != 40){
					
				ConferenciaEncalhe.mostrar_produtos();
			}
		});
		
		$('#codProduto', ConferenciaEncalhe.workspace).keypress(function(e) {
			
			$("#pesq_prod", ConferenciaEncalhe.workspace).val("");
			ConferenciaEncalhe.popup_pesquisar();
		});
		
		$('#observacao', ConferenciaEncalhe.workspace).keypress(function(e) {
			if (e.keyCode == 13) {
				ConferenciaEncalhe.gravaObs();
			}
		});
		
		ConferenciaEncalhe.popup_logado();

		ConferenciaEncalhe.removerAtalhos();
		
		ConferenciaEncalhe.atribuirAtalhos();
	},

	getProdutoEdicaoPorCodigoDeBarras: function() {

		var data = [{name: "idProdutoEdicaoAnterior", value: ConferenciaEncalhe.ultimoIdProdutoEdicao}];
		
		if ($("#qtdeExemplar", ConferenciaEncalhe.workspace).val().trim() != "") {
			data.push({name: "quantidade", value: $("#qtdeExemplar", ConferenciaEncalhe.workspace).val()});
		}
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/pesquisarProdutoEdicao', data,
			function(result){
			
				ConferenciaEncalhe.setarValoresPesquisados(result);
				
				ConferenciaEncalhe.adicionarProdutoConferido();
				
				$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).focus();
			
			}, function() {
				
				$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).val("");
				$("#qtdeExemplar", ConferenciaEncalhe.workspace).val("");
				$("#sm", ConferenciaEncalhe.workspace).val("");
				$("#codProduto", ConferenciaEncalhe.workspace).val("");
				
			}, false, "idTelaConferenciaEncalhe"
		);
	},
	
	removerAtalhos: function() {
		
		$(document.body).unbind('keydown.adicionarProduto');
		
		$(document.body).unbind('keydown.popUpNotaFiscal');
		
		$(document.body).unbind('keydown.salvarConferencia');
		
		$(document.body).unbind('keydown.finalizarConferencia');

	},

	/*
	 * ALTERACAO INICIO
	 */
	
	
    /**
     * Verifica se houve alteração na conferencia de encalhe da cota. 
     * Caso positivo será informado ao usuario antes de fechar a 
     * aba de conferência de encalhe da cota.
     * 
     * @param self
     * @param index
     */
	verificarAlteracoesConferenciaEncalheParaFecharAba : function(self, index) {
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/verificarConferenciaEncalheCotaStatus", null,
		
		function(result){
	
			if(result.CONFERENCIA_ENCALHE_COTA_STATUS == 'INICIADA_NAO_SALVA') {
		
				ConferenciaEncalhe.modalAberta = true;
				
				$("#dialog-conferencia-nao-salva", ConferenciaEncalhe.workspace).dialog({
					resizable : false,
					height : 180,
					width : 460,
					modal : true,
					buttons : {
						"Sim" : function() {
							
							$("#dialog-conferencia-nao-salva", ConferenciaEncalhe.workspace).dialog("close");
							
							$(self).tabs("remove", index);
							
							ConferenciaEncalhe.modalAberta = false;
							
						},
						"Não" : function() {
							
							$("#dialog-conferencia-nao-salva", ConferenciaEncalhe.workspace).dialog("close");
							
							ConferenciaEncalhe.modalAberta = false;
							
						}
					},
					
					form: $("#dialog-conferencia-nao-salva", this.workspace).parents("form")
				});
				
				
			} else {
				
				$(self).tabs("remove", index);
				
			}
	
		});	
	    	
	 },	
	
	/*
	 * ALTERACAO FINAL
	 */
	
	
	
	atribuirAtalhos: function(){
		
		$(document.body).bind('keydown.adicionarProduto', jwerty.event('F2',function() {
			
			if (!ConferenciaEncalhe.modalAberta){
			
				ConferenciaEncalhe.adicionarProdutoConferido();
			}
		}));

		$(document.body).bind('keydown.popUpNotaFiscal', jwerty.event('F6',function() {
			
			if (!ConferenciaEncalhe.modalAberta){
				
				ConferenciaEncalhe.popup_notaFiscal();
			}
			
		}));

		$(document.body).bind('keydown.salvarConferencia', jwerty.event('F8',function() {
			
			if (!ConferenciaEncalhe.modalAberta){
				
				ConferenciaEncalhe.popup_salvarInfos();
			}
			
		}));

		$(document.body).bind('keydown.finalizarConferencia', jwerty.event('F9',function() {
			
			if (!ConferenciaEncalhe.modalAberta){
				
				ConferenciaEncalhe.veificarCobrancaGerada();
				
			}
			
		}));
		
		
	},
	
	
	ifCotaEmiteNfe :  function(data, fnCotaEmiteNfe) {
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/verificarCotaEmiteNFe", data, 
		function(result){
			if(result.IND_COTA_EMITE_NFE) {
				fnCotaEmiteNfe();
			} 
		});
	},
	
	_pesquisarCota : function() {
		
		var data = [
		            {name: 'numeroCota', value : $("#numeroCota", ConferenciaEncalhe.workspace).val()}, 
		            {name: 'indObtemDadosFromBD', value : true},
		            {name: 'indConferenciaContingencia', value: false}
		           ];
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/verificarReabertura", data,
				
		function(result){
			
			if (result.listaMensagens && result.listaMensagens[0] == "REABERTURA"){
				
				ConferenciaEncalhe.modalAberta = true;
				
				$("#dialog-reabertura", ConferenciaEncalhe.workspace).dialog({
					resizable : false,
					height : 200,
					width : 360,
					modal : true,
					buttons : {
						"Sim" : function() {
							
							ConferenciaEncalhe.carregarListaConferencia(data);
							
							$("#dialog-reabertura", ConferenciaEncalhe.workspace).dialog("close");
							
							ConferenciaEncalhe.modalAberta = false;
							
							ConferenciaEncalhe.ifCotaEmiteNfe(data, ConferenciaEncalhe.popup_alert);
							
						},
						"Não" : function() {
							
							$("#dialog-reabertura", ConferenciaEncalhe.workspace).dialog("close");
							
							ConferenciaEncalhe.modalAberta = false;
							
						}
					},
					form: $("#dialog-reabertura", this.workspace).parents("form")
				});
				
			} else {
				
				ConferenciaEncalhe.carregarListaConferencia(data);
				
				ConferenciaEncalhe.ifCotaEmiteNfe(data, ConferenciaEncalhe.popup_alert);
				
			}
			
			ConferenciaEncalhe.limparDadosProduto();
			
		});
		
			
	},
	
	pesquisarCota : function() {
		
		var data = [
		            {name: 'numeroCota', value : $("#numeroCota", ConferenciaEncalhe.workspace).val()}, 
		            {name: 'indObtemDadosFromBD', value : true},
		            {name: 'indConferenciaContingencia', value: false}
		           ];
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/verificarConferenciaEncalheCotaStatus", data,
			function(result){
			
				if(	result.CONFERENCIA_ENCALHE_COTA_STATUS == 'INICIADA_NAO_SALVA') {
					
					ConferenciaEncalhe.modalAberta = true;
					
					$("#dialog-conferencia-nao-salva-troca-de-cota", ConferenciaEncalhe.workspace).dialog({
						resizable : false,
						height : 200,
						width : 360,
						modal : true,
						buttons : {
							
							"Sim" : function() {
								
								$("#dialog-conferencia-nao-salva-troca-de-cota", ConferenciaEncalhe.workspace).dialog("close");
								
								ConferenciaEncalhe.modalAberta = false;
								
								ConferenciaEncalhe._pesquisarCota();
								
							},
							"Não" : function() {
								
								$("#dialog-conferencia-nao-salva-troca-de-cota", ConferenciaEncalhe.workspace).dialog("close");
								
								ConferenciaEncalhe.modalAberta = false;
								
							}
						},
						
						form: $("#dialog-conferencia-nao-salva-troca-de-cota", this.workspace).parents("form")
						
					});
					
					
					
				} else {
					
					ConferenciaEncalhe._pesquisarCota(result);		
					
				}
			
				
			}
		);
	},
	
	verificarValorTotalNotaFiscal : function() {
		
		var data = [{name: 'indConferenciaContingencia', value: false}];
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/verificarValorTotalNotaFiscal', data,
				
				function(result){
				
					if(result.tipoMensagem == 'SUCCESS') {
						
						if(result.indGeraDocumentoConfEncalheCota == true) {

							ConferenciaEncalhe.gerarDocumentosConferenciaEncalhe(result.tiposDocumento);
							
						}
						
						ConferenciaEncalhe.limparDadosConferenciaEncalheCotaFinalizada();
						
					} else if(result.tipoMensagem == 'WARNING') {
						
						$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/carregarListaConferencia', 
								null, 
								function(result){
									
									ConferenciaEncalhe.abrirDialogNotaFiscalDivergente(result);
								}
						);
						
						playSound();
					}
				
					exibirMensagem(result.tipoMensagem, result.listaMensagens);
				},
				null, true, "idModalDadosNotaFiscal"
		);
		
	},
	
	replaceAll : function(string, token, newtoken) {
		while (string.indexOf(token) != -1) {
	 		string = string.replace(token, newtoken);
		}
		return string;
	},
	
	preparaValor : function(vr){
		
		if(vr.substr(vr.length-3,1)==","){
			vr = this.replaceAll(vr,".","");
			vr = this.replaceAll(vr,",",".");
		}
		if(vr.substr(vr.length-3,1)=="."){
			vr = this.replaceAll(vr,",","");
		}
		return vr;
	},
	
	verificarValorTotalCE : function() {
		
		var data = [{name: "valorCEInformado", value: ConferenciaEncalhe.preparaValor($("#vlrCE", ConferenciaEncalhe.workspace).val())}];
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/verificarValorTotalCE', data, 
		
				function(conteudo) {
					
					if(conteudo.valorCEInformadoValido == true) {
						
						ConferenciaEncalhe.verificarValorTotalNotaFiscal();
						
					} else {
						
						$("#msgConfirmar", ConferenciaEncalhe.wokspace).text(conteudo.mensagemConfirmacao);
						
						$("#dialog-confirmar", ConferenciaEncalhe.workspace).dialog({
							resizable : false,
							height : 160,
							width : 400,
							modal : true,
							buttons : {
								"Sim" : function() {
									
									ConferenciaEncalhe.verificarValorTotalNotaFiscal();
									$("#dialog-confirmar", ConferenciaEncalhe.workspace).dialog("close");
								},
								"Não" : function() {
									
									$("#dialog-confirmar", ConferenciaEncalhe.workspace).dialog("close");
								}
							},
							form: $("#dialog-confirmar", ConferenciaEncalhe.workspace).parents("form")
						});
					}
				}
		);
	},
	
	carregarListaConferencia : function(data){
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/carregarListaConferencia', 
				data, 
				function(result){
					
					ConferenciaEncalhe.preProcessarConsultaConferenciaEncalhe(result);
				}
		);
	},

	limparDadosConferenciaEncalheCotaFinalizada : function() {
		
		$("._dadosConfEncalhe", ConferenciaEncalhe.workspace).remove();
		
		$(".outrosVlrsGrid", ConferenciaEncalhe.workspace).flexAddData({
			page: 0, total: 0, rows: {}
		});
		
		$("#totalReparte", ConferenciaEncalhe.workspace).text("");
		
		$("#totalEncalhe", ConferenciaEncalhe.workspace).text("");
		
		$("#valorVendaDia", ConferenciaEncalhe.workspace).text("");
		
		$("#totalOutrosValores", ConferenciaEncalhe.workspace).text("");
		
		$("#valorAPagar", ConferenciaEncalhe.workspace).text("");
		
		$(".dadosFiltro", ConferenciaEncalhe.workspace).hide();
		
		$("#nomeCota", ConferenciaEncalhe.workspace).text("");
		
		$("#statusCota", ConferenciaEncalhe.workspace).text("");
		
	},
	
	preProcessarConsultaConferenciaEncalhe : function(result) {
		
		if (result.mensagens){
			
			exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
			
			return;
			
		}
		
		var innerTable = '';
		
		var modeloConferenciaEncalhe = result.listaConferenciaEncalhe;
		
		var indDistribuidorAceitaJuramentado = result.indDistribuidorAceitaJuramentado;
		
		$("._dadosConfEncalhe", ConferenciaEncalhe.workspace).remove();
		
		if (modeloConferenciaEncalhe){
		
			$.each(modeloConferenciaEncalhe, 
				function(index, value) {
					
					var parcial =  value.parcial;
				
					var _class;
					
					if (index % 2 == 0){
						_class = "class_linha_1 _dadosConfEncalhe";
					} else {
						_class = "class_linha_2 _dadosConfEncalhe";
					}
				
					innerTable += "<tr class='" + _class + " _dados'><td nowrap='nowrap' style='text-align: center;'>";
					
					innerTable += "<input type='hidden' id='idProdutoEdicaoGrid_"+index+"' value='" + value.idProdutoEdicao + "'/>";
					
					var valorExemplares = parseInt(value.qtdExemplar);
					
					var inputExemplares = '<input id="qtdExemplaresGrid_' + index + '" class="input-numericE" onchange="ConferenciaEncalhe.atualizarValores('+ index +');" style="width:50px; text-align: center;" maxlength="255" value="' + valorExemplares + '"/>' +
						'<input id="idConferenciaEncalheHidden_' + index + '" type="hidden" value="' + value.idConferenciaEncalhe + '"/>';
					
					innerTable += inputExemplares + "</td>";
					
					innerTable += "<td nowrap='nowrap'>" + value.codigoDeBarras + "</td>";
					
					innerTable += "<td nowrap='nowrap'>" + value.codigoSM + "</td>";
					
					innerTable += "<td nowrap='nowrap'>" + value.codigo + "</td>";
					
					innerTable += "<td nowrap='nowrap'>" + value.nomeProduto + "</td>";
					
					innerTable += "<td nowrap='nowrap'>" + value.numeroEdicao + "</td>";
					
					innerTable += "<td style='text-align: right;' nowrap='nowrap'>" + parseFloat(value.precoCapa).toFixed(2) + "</td>";
					
					innerTable += "<td style='text-align: right;' nowrap='nowrap'>" + parseFloat(value.desconto).toFixed(2) + "</td>";
					
					innerTable += "<td align='right' nowrap='nowrap' id='valorTotalConferencia_" + index + "'>" + parseFloat(value.valorTotal).toFixed(2) + "</td>";
					
					if (value.dia || value.dataRecolhimento){
					
						if (value.dia && value.dia > 0){
						
							innerTable += "<td style='text-align: center;' nowrap='nowrap'>" + value.dia + "º" + "</td>";
						} else {
							
							innerTable += "<td style='text-align: center;' nowrap='nowrap' style='width: 20px;'>" + value.dataRecolhimento + "</td>";
						}
					} else {
						
						innerTable += "<td></td>";
					}
					
					var inputCheckBoxJuramentada = '';
					
					if (indDistribuidorAceitaJuramentado == true) {
					
						if(parcial == true) {
							
							inputCheckBoxJuramentada = '<input type="checkbox" ' + (value.juramentada == true ? 'checked="checked"' : '')
							+ ' onchange="ConferenciaEncalhe.atualizarValores('+ index +');" id="checkGroupJuramentada_' + index + '"/>';
							
						} else {
							
							inputCheckBoxJuramentada = '<input type="checkbox" disabled="disabled" id="checkGroupJuramentada_' + index + '"/>';
							
						}
						
						innerTable += "<td style='text-align: center;' nowrap='nowrap'>" + inputCheckBoxJuramentada + "</td>";
					
					} 
					
					var imgDetalhar = '<img src="' + contextPath + '/images/ico_detalhes.png" border="0" hspace="3"/>';
					innerTable += '<td style="text-align: center;" nowrap="nowrap"><a href="javascript:;" onclick="ConferenciaEncalhe.exibirDetalhesConferencia(' + value.idConferenciaEncalhe + ');">' + imgDetalhar + '</a></td>';
					
					var imgExclusao = '<img src="' + contextPath + '/images/ico_excluir.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />';
					innerTable += '<td style="text-align: center;" nowrap="nowrap"><a href="javascript:;" onclick="ConferenciaEncalhe.excluirConferencia(' + value.idConferenciaEncalhe + ');">' + imgExclusao + '</a></td>';
					
					innerTable += "</tr>";
					
					$(innerTable).appendTo("#dadosGridConferenciaEncalhe", ConferenciaEncalhe.workspace);
					
					innerTable = '';
				}
			);
			
			if (!indDistribuidorAceitaJuramentado) {
				
				$("#colunaJuramentada", ConferenciaEncalhe.workspace).hide();
			}
			
			$('input[id*="qtdExemplaresGrid"]', ConferenciaEncalhe.workspace).numericE();
			
		}
		
		$(".outrosVlrsGrid", ConferenciaEncalhe.workspace).flexAddData({
			page: result.listaDebitoCredito.page, total: result.listaDebitoCredito.total, rows: result.listaDebitoCredito.rows
		});
		
		$("#totalReparte", ConferenciaEncalhe.workspace).text(parseFloat(result.reparte).toFixed(2));
		$("#totalEncalhe", ConferenciaEncalhe.workspace).text(parseFloat(result.valorEncalhe).toFixed(2));
		$("#valorVendaDia", ConferenciaEncalhe.workspace).text(parseFloat(result.valorVendaDia).toFixed(2));
		$("#totalOutrosValores", ConferenciaEncalhe.workspace).text(parseFloat(result.valorDebitoCredito).toFixed(2));
		$("#valorAPagar", ConferenciaEncalhe.workspace).text(parseFloat(result.valorPagar).toFixed(2));
		
		$(".dadosFiltro", ConferenciaEncalhe.workspace).show();
		$("#nomeCota", ConferenciaEncalhe.workspace).text(result.razaoSocial);
		$("#statusCota", ConferenciaEncalhe.workspace).text(result.situacao);
	},
	
	gerarDocumentosConferenciaEncalhe : function(tiposDocumento) {
		
		/*$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/imprimirDocumentosCobranca',
				null,
				function(resultado){
					var w = window.open();
					self.focus();
					w.document.open();
					w.document.write('<html>');
					w.document.write('<body>');
					w.document.write('<pre>');
					w.document.write(resultado);
					w.document.write('</pre>');
					w.document.write('</body>');
					w.document.write('</html>');
					w.document.close();
					w.print();
					w.close();
				}
		);	*/	
		
		var file = contextPath + '/devolucao/conferenciaEncalhe/imprimirDocumentosCobranca';
		$('#download-iframe', ConferenciaEncalhe.workspace).attr('src', file);		
	},
	
	carregarGridItensNotaFiscal : function (modeloConferenciaEncalhe) {
		
		$("._dadosConfEncalheFinalizar", ConferenciaEncalhe.workspace).remove();
		
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
						'<td style="text-align: center"><input id="qtdeInformadaFinalizarConf_'+ index +'" onchange="ConferenciaEncalhe.recalcularValoresFinalizar('+ index +');" type="text" maxlength="255" style="width:50px; text-align: center;" value="' + parseInt(value.qtdInformada) + '"/></td>';
					
					innerTable += "<td style='text-align: center;'>" + (value.qtdExemplar ? parseInt(value.qtdExemplar) : "0") + "</td>";
				
					innerTable +=
						'<td style="text-align: center;"><input id="precoCapaFinalizarConf_'+ index +'" onchange="ConferenciaEncalhe.recalcularValoresFinalizar('+ index +');" maxlength="255" style="width:50px; text-align: right;" value="' + parseFloat(value.precoCapaInformado).toFixed(2) + '"/></td>';
					
					innerTable += "<td style='text-align: right;'>" + parseFloat(value.desconto).toFixed(2) + "</td>";
					
					innerTable += "<td style='text-align: right;' id='valorTotalConferenciaFinalizar_" + index + "'>" + parseFloat(value.valorTotal).toFixed(2) + "</td>";
					
					var imgExclusao = '<img src="' + contextPath + '/images/ico_excluir.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />';
					innerTable += '<td style="text-align: center;"><a href="javascript:;" onclick="ConferenciaEncalhe.excluirConferencia(' + value.idConferenciaEncalhe + ');">' + imgExclusao + '</a></td>';
					
					innerTable += "</tr>";
					
					$(innerTable).appendTo("#dadosGridConferenciaEncalheFinalizar", ConferenciaEncalhe.workspace);
					
					innerTable = '';
				}
			);
		
	},
	
	abrirDialogNotaFiscalDivergente : function(result) {
		
		ConferenciaEncalhe.modalAberta = true;
		
		$("#dialog-dadosNotaFiscal", ConferenciaEncalhe.workspace).dialog({
			resizable : false,
			height : 'auto',
			width : 877,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					var data = [{name: 'indConferenciaContingencia', value: false}];
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/finalizarConferencia', data,
						
						function(conteudo){
						
							if(conteudo.tipoMensagem == 'SUCCESS') {
								
								$("#dialog-dadosNotaFiscal", ConferenciaEncalhe.workspace).dialog("close");

								
								if(conteudo.indGeraDocumentoConfEncalheCota == true) {
									ConferenciaEncalhe.gerarDocumentosConferenciaEncalhe(conteudo.tiposDocumento);
								}
								
								ConferenciaEncalhe.limparDadosConferenciaEncalheCotaFinalizada();
								
							}

							exibirMensagem(conteudo.tipoMensagem, conteudo.listaMensagens);
							
						}, 

						function(conteudo) {
							
							var data = [
										  {name: 'numeroCota', 			value : $("#numeroCota", ConferenciaEncalhe.workspace).val()}, 
										  {name: 'indObtemDadosFromBD', value : false},
										  {name: 'indConferenciaContingencia', value: false}
										 ];
										
							ConferenciaEncalhe.carregarListaConferencia(data);
							
							
						}, true, "idModalDadosNotaFiscal"
					);
				},
				"Cancelar" : function() {
					
					$(this).dialog("close");
				}
			},
			form: $("#dialog-dadosNotaFiscal", this.workspace).parents("form"),
			close : function(){
				
				ConferenciaEncalhe.modalAberta = false;
			}
		});
		
		var notaFiscal = result.notaFiscal;
		
		if (notaFiscal){
		
			$("#numeroNotaFiscalExibir", ConferenciaEncalhe.workspace).text(notaFiscal.numero);
			$("#serieExibir", ConferenciaEncalhe.workspace).text(notaFiscal.serie);
			$("#dataExibir", ConferenciaEncalhe.workspace).text(notaFiscal.dataEmissao);
			$("#valorTotalNotaFiscalExibir", ConferenciaEncalhe.workspace).text(parseFloat(notaFiscal.valorProdutos).toFixed(2));
			$("#chaveAcessoExibir", ConferenciaEncalhe.workspace).text(notaFiscal.chaveAcesso);
		}
		
		var modeloConferenciaEncalhe = result.listaConferenciaEncalhe;
		
		$("._dadosConfEncalheFinalizar", ConferenciaEncalhe.workspace).remove();
		
		if (modeloConferenciaEncalhe){
		
			ConferenciaEncalhe.carregarGridItensNotaFiscal(modeloConferenciaEncalhe);
			
			$('input[id*="qtdeInformadaFinalizarConf"]', ConferenciaEncalhe.workspace).numeric();
			$('input[id*="precoCapaFinalizarConf"]', ConferenciaEncalhe.workspace).numeric();
		}
		
		$("#somatorioQtdInformada", ConferenciaEncalhe.workspace).text(parseInt(result.qtdInformada));
		$("#somatorioQtdRecebida", ConferenciaEncalhe.workspace).text(parseInt(result.qtdRecebida));
		$("#somatorioTotal", ConferenciaEncalhe.workspace).text(parseFloat(result.valorPagar).toFixed(2));
	},
	
	atualizarValores : function(index){
		
		var juramentado = false;
		
		if( $("#checkGroupJuramentada_" + index, ConferenciaEncalhe.workspace).attr("checked") == 'checked' ) {
			juramentado = true;
		}
		
		var data = [
            {name: "idConferencia", value: $("#idConferenciaEncalheHidden_" + index, ConferenciaEncalhe.workspace).val()},
            {name: "qtdExemplares", value: $("#qtdExemplaresGrid_" + index, ConferenciaEncalhe.workspace).val()},
            {name: "juramentada", value: juramentado}
		];
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/atualizarValores", 
			data, 
			function(result){
				
				$("#valorTotalConferencia_" + index, ConferenciaEncalhe.workspace).text(parseFloat(result.conf.valorTotal).toFixed(2));
				
				$("#totalReparte", ConferenciaEncalhe.workspace).text(parseFloat(result.reparte).toFixed(2));
				$("#totalEncalhe", ConferenciaEncalhe.workspace).text(parseFloat(result.valorEncalhe).toFixed(2));
				$("#valorVendaDia", ConferenciaEncalhe.workspace).text(parseFloat(result.valorVendaDia).toFixed(2));
				$("#totalOutrosValores", ConferenciaEncalhe.workspace).text(parseFloat(result.valorDebitoCredito).toFixed(2));
				$("#valorAPagar", ConferenciaEncalhe.workspace).text(parseFloat(result.valorPagar).toFixed(2));
			}, function(){
				
				var data = [
							  {name: 'numeroCota', 			value : $("#numeroCota", ConferenciaEncalhe.workspace).val()}, 
							  {name: 'indObtemDadosFromBD', value : false},
							  {name: 'indConferenciaContingencia', value: false}
							 ];
							
				ConferenciaEncalhe.carregarListaConferencia(data);				
			}
		
		);
	},
	
	recalcularValoresFinalizar : function(index){
		
		var data = [
            {name: "idConferencia", value: $("#idConferenciaEncalheHiddenFinalizarConf_" + index, ConferenciaEncalhe.workspace).val()},
            {name: "qtdInformada", value: $('#qtdeInformadaFinalizarConf_' + index, ConferenciaEncalhe.workspace).val()},
            {name: "valorCapaInformado", value : $('#precoCapaFinalizarConf_' + index, ConferenciaEncalhe.workspace).val()}
		];
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/alterarQtdeValorInformado", 
			data, 
			function(result){
				
				$("#valorTotalConferenciaFinalizar_" + index, ConferenciaEncalhe.workspace).text(parseFloat(result.conf.valorTotal).toFixed(2));
				
				$("#somatorioQtdInformada", ConferenciaEncalhe.workspace).text(parseFloat(result.qtdInformada).toFixed(2));
				$("#somatorioQtdRecebida", ConferenciaEncalhe.workspace).text(parseFloat(result.qtdRecebida).toFixed(2));
				$("#somatorioTotal", ConferenciaEncalhe.workspace).text(parseFloat(result.valorPagar).toFixed(2));
			}
		);
	},
	
	exibirDetalhesConferencia : function(idConferenciaEncalhe){
		
		ConferenciaEncalhe.idConferenciaEncalheVisualizacao = idConferenciaEncalhe;
		
		var data = [{name: "idConferenciaEncalhe", value: idConferenciaEncalhe}];
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/buscarDetalhesProduto', data,
			function (result){
				
				$("#nomeProdutoDetalhe", ConferenciaEncalhe.workspace).text(result.nomeProduto);
				$("#precoCapaDetalhe", ConferenciaEncalhe.workspace).text(result.precoCapa);
				$("#chamadaCapa", ConferenciaEncalhe.workspace).text(result.chamadaCapa);
				$("#fornecedor", ConferenciaEncalhe.workspace).text(result.nomeFornecedor);
				$("#brinde", ConferenciaEncalhe.workspace).text(result.possuiBrinde == "true" ? "Sim" : "Não");
				$("#editor", ConferenciaEncalhe.workspace).text(result.nomeEditor);
				$("#pacotePadrao", ConferenciaEncalhe.workspace).text(result.pacotePadrao);
				
				$("#precoDesconto", ConferenciaEncalhe.workspace).text((parseFloat(result.precoCapa) - parseFloat(result.desconto)).toFixed(2));
				
				$("#observacaoReadOnly", ConferenciaEncalhe.workspace).text(result.observacao ? result.observacao : "");
				$("#observacao", ConferenciaEncalhe.workspace).val(result.observacao ? result.observacao : "");
				$("#observacaoReadOnly", ConferenciaEncalhe.workspace).show();
				$("#observacao", ConferenciaEncalhe.workspace).show();
				$("#btObs", ConferenciaEncalhe.workspace).show();
				
				var imgPath = (result.idProdutoEdicao == null || result.idProdutoEdicao == undefined)
				? "" :  contextPath + '/capa/' + result.idProdutoEdicao + '?' + Math.random();
				var img = $("<img />").attr('src', imgPath).attr('width', '117').attr('height', '145').attr('alt', 'Capa');
				$("#imagemProduto", ConferenciaEncalhe.workspace).empty();
				$("#imagemProduto", ConferenciaEncalhe.workspace).append(img);				
				img.load(function() {
					if (!(!this.complete || typeof this.naturalWidth == "undefined" || this.naturalWidth == 0)) {
						$("#imagemProduto", ConferenciaEncalhe.workspace).empty();
						$("#imagemProduto", ConferenciaEncalhe.workspace).append(img);
					}
				});
				
				ConferenciaEncalhe.popup_detalhe_publicacao();
			}
		);
	},
	
	excluirConferencia : function(idConferenciaEncalhe){
		
		ConferenciaEncalhe.modalAberta = true;
		
		$("#dialog-excluir-conferencia", ConferenciaEncalhe.workspace).dialog({
			resizable : false,
			height : 180,
			width : 460,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					var data = [{name : "idConferenciaEncalhe", value : idConferenciaEncalhe}];
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/excluirConferencia', data,
							
						function (result){
							
							ConferenciaEncalhe.preProcessarConsultaConferenciaEncalhe(result);
							
							ConferenciaEncalhe.carregarGridItensNotaFiscal(result.listaConferenciaEncalhe);
							
							$("#dialog-excluir-conferencia", ConferenciaEncalhe.workspace).dialog("close");
						}
					
					);
				},
				"Cancelar" : function() {
					
					$(this).dialog("close");
					$('#pesq_cota', ConferenciaEncalhe.workspace).focus();
				}
			},
			form: $("#dialog-excluir-conferencia", this.workspace).parents("form"),
			close : function(){
				
				ConferenciaEncalhe.modalAberta = false;
			}
		});
	},
	
	popup_logado : function(){
		
		ConferenciaEncalhe.modalAberta = true;
		
		$("#dialog-logado", ConferenciaEncalhe.workspace).dialog({
			resizable : false,
			height : 180,
			width : 460,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/salvarIdBoxSessao", "idBox=" + $("#boxLogado", ConferenciaEncalhe.workspace).val(), 
						function(){
							
							$("#dialog-logado", ConferenciaEncalhe.workspace).dialog("close");
							$('#numeroCota', ConferenciaEncalhe.workspace).focus();
						}, null, true, "idModalBoxRecolhimento"
					);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
					$('#pesq_cota', ConferenciaEncalhe.workspace).focus();
				}
			},
			form: $("#dialog-logado", this.workspace).parents("form"),
			open : function(){
				
				$("#boxLogado", ConferenciaEncalhe.workspace).focus();
			}, close : function(){
				
				ConferenciaEncalhe.modalAberta = false;
			}
		});
	},
	
	setarValoresPesquisados : function(result){
		
		if (ConferenciaEncalhe.ultimoCodeBar && ConferenciaEncalhe.ultimoCodeBar != result.codigoDeBarras){
		
			ConferenciaEncalhe.carregarListaConferencia(null);
			
		}
		
		ConferenciaEncalhe.ultimoCodeBar = result.codigoDeBarras;
		ConferenciaEncalhe.ultimoSM = result.codigoSM;
		ConferenciaEncalhe.ultimoIdProdutoEdicao = result.idProdutoEdicao;
		
		$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).val(result.codigoDeBarras);
		$("#sm", ConferenciaEncalhe.workspace).val(result.codigoSM);
		$("#codProduto", ConferenciaEncalhe.workspace).val(result.codigo);
		
		$("#idProdutoEdicaoHidden", ConferenciaEncalhe.workspace).val(result.idProdutoEdicao);
		
		$("#nomeProduto", ConferenciaEncalhe.workspace).text(result.nomeProduto);
		$("#edicaoProduto", ConferenciaEncalhe.workspace).text(result.numeroEdicao);
		$("#precoCapa", ConferenciaEncalhe.workspace).text(result.precoCapa);
		$("#desconto", ConferenciaEncalhe.workspace).text(parseFloat(result.desconto).toFixed(2));
		
		$("#valorTotal", ConferenciaEncalhe.workspace).text(((parseFloat(result.precoCapa) - parseFloat(result.desconto)) * parseFloat(result.qtdExemplar)).toFixed(2));
		
		$("#qtdeExemplar", ConferenciaEncalhe.workspace).val(parseInt(result.qtdExemplar));
	},
	
	adicionarProdutoConferido : function(){
		
		var idProdutoEdicao = $("#idProdutoEdicaoHidden", ConferenciaEncalhe.workspace).val();
		
		var data = [{name: "idProdutoEdicao", value: idProdutoEdicao}, 
		            {name: "quantidade", value: $("#qtdeExemplar", ConferenciaEncalhe.workspace).val()}];
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/adicionarProdutoConferido', data,
			function(result){
				
				ConferenciaEncalhe.preProcessarConsultaConferenciaEncalhe(result);
				
				ConferenciaEncalhe.limparDadosProduto();
				
				$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).focus();
				
				$("#qtdeExemplar", ConferenciaEncalhe.workspace).val(1);
			},
			function(){
				
				ConferenciaEncalhe.limparDadosProduto();
				
                $("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).focus();
				
				$("#qtdeExemplar", ConferenciaEncalhe.workspace).val(1);
			}	
		);
	},
	
	limparDadosProduto : function(){
		
		$("#qtdeExemplar", ConferenciaEncalhe.workspace).val("");
		$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).val("");
		$("#sm", ConferenciaEncalhe.workspace).val("");
		$("#codProduto", ConferenciaEncalhe.workspace).val("");
		
		$("#nomeProduto", ConferenciaEncalhe.workspace).text("");
		$("#edicaoProduto", ConferenciaEncalhe.workspace).text("");
		$("#precoCapa", ConferenciaEncalhe.workspace).text("");
		$("#desconto", ConferenciaEncalhe.workspace).text("");
		$("#valorTotal", ConferenciaEncalhe.workspace).text("");
		$("#vlrCE", ConferenciaEncalhe.workspace).val("");
		
		ConferenciaEncalhe.ultimoCodeBar = "";
		ConferenciaEncalhe.ultimoSM = "";
		ConferenciaEncalhe.ultimoIdProdutoEdicao = "";
	},

	popup_alert : function() {
		
		ConferenciaEncalhe.modalAberta = true;
		
		$("#dialog-alert", ConferenciaEncalhe.workspace).dialog({
			resizable : false,
			height : 190,
			width : 460,
			modal : true,
			buttons : {
				"Sim" : function() {
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/carregarNotaFiscal', null, 
						function(result){
							
							$("#numNotaFiscal", ConferenciaEncalhe.workspace).val(result.numero);
							$("#serieNotaFiscal", ConferenciaEncalhe.workspace).val(result.serie);
							$("#dataNotaFiscal", ConferenciaEncalhe.workspace).val(result.dataEmissao);
						    $("#chaveAcessoNFE", ConferenciaEncalhe.workspace).val(result.chaveAcesso);
							
						    if (result.valorProdutos){
							    
						    	$("#valorNotaFiscal", ConferenciaEncalhe.workspace).val(parseFloat(result.valorProdutos).toFixed(2));
								
						    }
							
							$("#dialog-alert", ConferenciaEncalhe.workspace).dialog("close");
							ConferenciaEncalhe.popup_notaFiscal();
						}
					);
				},
				"Não" : function() {
					
					$("#dialog-alert", ConferenciaEncalhe.workspace).dialog("close");
					$("#vlrCE", ConferenciaEncalhe.workspace).focus();
				}
			}, open : function(){
				
				$(this).parent('div').find('button:contains("Sim")').focus();
			}, close : function(){
				
				ConferenciaEncalhe.modalAberta = false;
			},
			form: $("#dialog-alert", this.workspace).parents("form")
		});
		
		$("#dialog-alert", ConferenciaEncalhe.workspace).show();
	},

	mostrarChaveAcesso : function() {
		
		if($('input:radio[name=radioNFE]:checked', ConferenciaEncalhe.workspace).val() == 'S') {
			
			$("#divForChaveAcessoNFE", ConferenciaEncalhe.workspace).show();
			
		} else {

			$("#divForChaveAcessoNFE", ConferenciaEncalhe.workspace).hide();
			
		}
		
	},
	
	popup_notaFiscal : function() {
		
		ConferenciaEncalhe.modalAberta = true;
		
		$("#dialog-notaFiscal", ConferenciaEncalhe.workspace).dialog({
			resizable : false,
			height : 360,
			width : 750,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					var data = []; 
					
					if($('input:radio[name=radioNFE]:checked').val() == 'S') {
						
						data = [
								{name : "notaFiscal.numero", value : $("#numNotaFiscal", ConferenciaEncalhe.workspace).val()},
								{name : "notaFiscal.serie", value : $("#serieNotaFiscal", ConferenciaEncalhe.workspace).val()},
								{name : "notaFiscal.dataEmissao", value : $("#dataNotaFiscal", ConferenciaEncalhe.workspace).val()},
								{name : "notaFiscal.valorProdutos", value : $("#valorNotaFiscal", ConferenciaEncalhe.workspace).val()},
								{name : "notaFiscal.chaveAcesso", value : $("#chaveAcessoNFE", ConferenciaEncalhe.workspace).val()}
						];
						
					} else {
						
						data = [
								{name : "notaFiscal.numero", value : $("#numNotaFiscal", ConferenciaEncalhe.workspace).val()},
								{name : "notaFiscal.serie", value : $("#serieNotaFiscal", ConferenciaEncalhe.workspace).val()},
								{name : "notaFiscal.dataEmissao", value : $("#dataNotaFiscal", ConferenciaEncalhe.workspace).val()},
								{name : "notaFiscal.valorProdutos", value : $("#valorNotaFiscal", ConferenciaEncalhe.workspace).val()}
						];
						
					}
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/salvarNotaFiscal', data, 
						function(result){
							
							$("#dialog-notaFiscal", ConferenciaEncalhe.workspace).dialog("close");
							
							$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).focus();
							
						}, null, true, "dialog-notaFiscal"
					);
				},
				"Cancelar" : function() {
					
					$(this).dialog("close");
					$("#vlrCE", ConferenciaEncalhe.workspace).focus();
				}
			}, open : function(){
				
				$("#numNotaFiscal", ConferenciaEncalhe.workspace).focus();
			}, close : function(){
				
				ConferenciaEncalhe.modalAberta = false;
			},
			form: $("#dialog-notaFiscal", this.workspace).parents("form")
		});

	},

	popup_pesquisar : function() {
		
		ConferenciaEncalhe.modalAberta = true;
		
		$("#dialog-pesquisar", ConferenciaEncalhe.workspace).dialog({
			resizable : false,
			height : 470,
			width : 560,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					$(this).dialog("close");
				},
				"Cancelar" : function() {
					
					$(this).dialog("close");
				}
			}, close : function(){
				
				ConferenciaEncalhe.modalAberta = false;
			},
			form: $("#dialog-pesquisar", this.workspace).parents("form")
		});

	},

	popup_detalhe_publicacao : function() {
		
		ConferenciaEncalhe.modalAberta = true;
		
		$('#observacao', ConferenciaEncalhe.workspace).focus();
		
		$("#dialog-detalhe-publicacao", ConferenciaEncalhe.workspace).dialog({
			resizable : false,
			height : 420,
			width : 755,
			modal : true,
			buttons : {
				"Fechar" : function() {
					
					$(this).dialog("close");
				}
			}, close : function(){
				
				ConferenciaEncalhe.modalAberta = false;
			},
			form: $("#dialog-detalhe-publicacao", this.workspace).parents("form")
		});

	},
	
	gravaObs : function() {
		
		var data = [{name: "idConferenciaEncalhe", value: ConferenciaEncalhe.idConferenciaEncalheVisualizacao},
		            {name: "observacao", value: $("#observacao", ConferenciaEncalhe.workspace).val()}];
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/gravarObservacaoConferecnia', data, 
			function(result){
			
				$("#observacao", ConferenciaEncalhe.workspace).fadeOut();
				$(".obs", ConferenciaEncalhe.workspace).fadeIn("slow");
				$(".tit", ConferenciaEncalhe.workspace).fadeOut("slow");
				$("#btObs", ConferenciaEncalhe.workspace).fadeOut("slow");
				
				$("#observacaoReadOnly", ConferenciaEncalhe.workspace).text($("#observacao", ConferenciaEncalhe.workspace).val());
			}
		);
	},

	popup_outros_valores : function() {
		
		ConferenciaEncalhe.modalAberta = true;
		
		$("#dialog-outros-valores", ConferenciaEncalhe.workspace).dialog({
			resizable : false,
			height : 430,
			width : 600,
			modal : true,
			buttons : {
				"Fechar" : function() {
					
					$(this).dialog("close");
				}
			}, close : function(){
				
				ConferenciaEncalhe.modalAberta = false;
			},
			form: $("#dialog-outros-valores", this.workspace).parents("form")
		});

	},

	popup_salvarInfos : function() {
		
		ConferenciaEncalhe.modalAberta = true;
		
		$("#dialog-salvar", ConferenciaEncalhe.workspace).dialog({
			resizable : false,
			height : 190,
			width : 460,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					var data = [{name: 'indConferenciaContingencia', value: false}];
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/salvarConferencia', data,
						
						function(result){
						
							if(result.tipoMensagem == 'SUCCESS') {
								
								var data = [
								      {name : 'numeroCota', value : $("#numeroCota", ConferenciaEncalhe.workspace).val()}, 
								      {name: 'indObtemDadosFromBD', value : true},
								      {name: 'indConferenciaContingencia', value: false}
								];
								
								ConferenciaEncalhe.carregarListaConferencia(data);
								
							}
						
							exibirMensagem(result.tipoMensagem, result.listaMensagens);
							
							$("#dialog-salvar", ConferenciaEncalhe.workspace).dialog("close");
						}, function(conteudo) {
							
							var data = [
										  {name: 'numeroCota', 			value : $("#numeroCota", ConferenciaEncalhe.workspace).val()}, 
										  {name: 'indObtemDadosFromBD', value : false},
										  {name: 'indConferenciaContingencia', value: false}
										 ];
										
							ConferenciaEncalhe.carregarListaConferencia(data);
							
							
						}, true, "idModalConfirmarSalvarConf"
					);
				},
				"Cancelar" : function() {
					
					$(this).dialog("close");
				}

			}, close : function(){
				
				ConferenciaEncalhe.modalAberta = false;
			},
			form: $("#dialog-salvar", this.workspace).parents("form")
		});

	},

	autoCompletarPorCodigoDeBarras: function() {

		var codBarra = $("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).val().trim();

		if (codBarra && codBarra.length > 5) {
			
			$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/autoCompleteProdutoEdicaoCodigoDeBarras", {"codigoBarra" : codBarra},

				function(result){
				
				    //EXIBE AUTOCOMPLETE SOMENTE SE HOUVER MAIS DE UM RESULTADO
				    if (result.length > 1){
				
						$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).autocomplete({
							source: result,
							select: function(event, ui){

								ConferenciaEncalhe.ultimoIdProdutoEdicao = ui.item.chave.long;
								
								ConferenciaEncalhe.getProdutoEdicaoPorCodigoDeBarras();
							},
							delay : 0,
						});
						
						$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).autocomplete("search", codBarra);
				    }
				    else{
				    	
				    	$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).autocomplete({});
				    	
				    	ConferenciaEncalhe.ultimoIdProdutoEdicao = result[0].chave.long;
				    	
            		    ConferenciaEncalhe.getProdutoEdicaoPorCodigoDeBarras();
				    }    
				}
			);
		}
	},
	
	mostrar_produtos : function() {
		
		var codigoNomeProduto = $("#pesq_prod", ConferenciaEncalhe.workspace).val();
		
		if (codigoNomeProduto && codigoNomeProduto.length > 0){
			$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/pesquisarProdutoPorCodigoNome', 
					{codigoNomeProduto:codigoNomeProduto}, 
					function(result){
							
						$("#pesq_prod", ConferenciaEncalhe.workspace).autocomplete({
							source: result,
							select: function(event, ui){
								
								$("#codProduto", ConferenciaEncalhe.workspace).val(ui.item.chave.string);
								ConferenciaEncalhe.ultimoIdProdutoEdicao = ui.item.chave.long;
							},
							delay : 0,
						});
						
						$("#pesq_prod", ConferenciaEncalhe.workspace).autocomplete(
							"search", codigoNomeProduto
						);
					}
			);
		}
	},

	fechar_produtos : function() {
		$('.pesqProdutosGrid', ConferenciaEncalhe.workspace).keypress(function(e) {
			if (e.keyCode == 13) {
				$(".itensPesquisados", ConferenciaEncalhe.workspace).show();
			}
		});
	},

	pesqMostraCota : function() {
		$('#pesq_cota', ConferenciaEncalhe.workspace).keypress(function(e) {
			if (e.keyCode == 13) {
				$('.dadosFiltro', ConferenciaEncalhe.workspace).fadeIn('fast');
				ConferenciaEncalhe.popup_alert();
			}
		});
	},
	
	irParaContigencia: function(){
		$('#workspace').tabs('remove', $('#workspace').tabs('option','selected'));
		$(".tipsy").hide();
		$('#workspace').tabs('addTab', "Conferência Encalhe Cota Contingência",
				contextPath + "/devolucao/conferenciaEncalheContingencia" + "?random=" + Math.random());
	},
	
	veificarCobrancaGerada: function(){
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/veificarCobrancaGerada', null,
		
			function(conteudo){
			
				if(conteudo && conteudo.tipoMensagem == 'WARNING') {
					
					$("#msgRegerarCobranca", ConferenciaEncalhe.workspace).text(conteudo.listaMensagens[0]);
						
					ConferenciaEncalhe.modalAberta = true;
					
					$("#dialog-confirmar-regerar-cobranca", ConferenciaEncalhe.workspace).dialog({
						resizable : false,
						height : 'auto',
						width : 680,
						modal : true,
						buttons : {
							"Confirmar" : function() {
								
								$("#dialog-confirmar-regerar-cobranca", ConferenciaEncalhe.workspace).dialog("close");
								ConferenciaEncalhe.verificarValorTotalCE();
							},
							"Cancelar" : function(){
							
								$("#dialog-confirmar-regerar-cobranca", ConferenciaEncalhe.workspace).dialog("close");
							}
						}, 
						
						close : function(){
							
							ConferenciaEncalhe.modalAberta = false;
						},
						
						
						form: $("#dialog-confirmar-regerar-cobranca", ConferenciaEncalhe.workspace).parents("form")
					});
					
				} else {
					
					ConferenciaEncalhe.verificarValorTotalCE();
				}
				
			}, null, true, "dialog-confirmar-regerar-cobranca"
		);
	}

}, BaseController);

//@ sourceURL=scriptConferenciaEncalhe.js
