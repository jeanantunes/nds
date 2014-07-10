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
	
	processandoConferenciaEncalhe: false,
	
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
	
	valorAnteriorInput : undefined,
	
	resetValue : true,
	
	numeroCotaEditavel : function(r){
		
		if (r==false){
			
			$("#numeroCota",ConferenciaEncalhe.workspace).attr('readonly', true);
		}
		else{
			
			$("#numeroCota",ConferenciaEncalhe.workspace).attr('readonly', false);
		}
	},
	
	pesquisaProduto : function(){
		
		if (ConferenciaEncalhe.ultimoIdProdutoEdicao != "" && 
				ConferenciaEncalhe.ultimoIdProdutoEdicao == $("#pesq_prod", ConferenciaEncalhe.workspace).val()){
			
			var qtd = $("#qtdeExemplar", ConferenciaEncalhe.workspace).val() == "" 
				? 0 
				: parseInt($("#qtdeExemplar", ConferenciaEncalhe.workspace).val());
			
			$("#qtdeExemplar", ConferenciaEncalhe.workspace).val(qtd + 1);
			
			$("#dialog-pesquisar", ConferenciaEncalhe.workspace).dialog("close");
			
			focusSelectRefField($("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace));
			
			ConferenciaEncalhe.verificarPermissaoSuperVisor();
			
		} else {
			
			var data = [{name: "idProdutoEdicaoAnterior", value: ConferenciaEncalhe.ultimoIdProdutoEdicao}];
			
			if ($("#qtdeExemplar", ConferenciaEncalhe.workspace).val().trim() != "") {
				data.push({name: "quantidade", value: $("#qtdeExemplar", ConferenciaEncalhe.workspace).val()});
			}
			
			$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/pesquisarProdutoEdicao', data,
				function(result){
					
					ConferenciaEncalhe.setarValoresPesquisados(result);
					
					$("#dialog-pesquisar", ConferenciaEncalhe.workspace).dialog("close");
					
					focusSelectRefField($("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace));

					ConferenciaEncalhe.verificarPermissaoSuperVisor();
				},
				function(){
					
					$("#codProduto", ConferenciaEncalhe.workspace).val("");
					
				}, 
				true, 
				"idModalPesquisarProdutos",
				false
			);
		}
	},

	init : function() {
		
		$("#pesq_prod", ConferenciaEncalhe.workspace).autocomplete({source: []});
		
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
		                                           
		$("#qtdeExemplar", ConferenciaEncalhe.workspace).numericPacotePadrao();
		
		$("#vlrCE", this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2,
			 defaultZero:false
		});
		
		$("#valorNotaFiscal", this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
		
		$("#numNotaFiscal", ConferenciaEncalhe.workspace).numeric();
		
		$("#qtdCE", ConferenciaEncalhe.workspace).numeric();
		
		$("#chaveAcessoNFE", ConferenciaEncalhe.workspace).numeric();

		$("#dataNotaFiscal", ConferenciaEncalhe.workspace).mask("99/99/9999");
		
		$("#valorNotaFiscal", ConferenciaEncalhe.workspace).numeric();
		
		$("#sm", ConferenciaEncalhe.workspace).numeric();
		
		$("#numeroCota", ConferenciaEncalhe.workspace).keypress(function(e) {
			
			if(e.keyCode == 13 && !visibleOverlay()) {
				
				ConferenciaEncalhe.pesquisarCota();
			}
		});
		
		$("#vlrCE", ConferenciaEncalhe.workspace).keypress(function(e) {
			
			if (e.keyCode == 13) {
				
				focusSelectRefField($("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace));
			}
		});
		
		$("#qtdCE", ConferenciaEncalhe.workspace).keypress(function(e) {
			
			if (e.keyCode == 13) {
				
				$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).focus();
			}
		});

		ConferenciaEncalhe.inicializarAutoCompleteSugestaoProdutoEdicao();
		
		$("#pesq_prod", ConferenciaEncalhe.workspace).keyup(function (e){
			
			if (e.keyCode == 13) {

				ConferenciaEncalhe.pesquisaProduto();
				
			} 
			
		});
		
		$('#codProduto', ConferenciaEncalhe.workspace).keydown(function(e) {
			
			var codPesquisado = $(this).val();
			
			$("#pesq_prod", ConferenciaEncalhe.workspace).val(codPesquisado);
			
			ConferenciaEncalhe.popup_pesquisar();
			
		});
		
		$('#observacao', ConferenciaEncalhe.workspace).keypress(function(e) {
			if (e.keyCode == 13) {
				ConferenciaEncalhe.gravaObs();
			}
		});
		
		$("#dialog-notaFiscal").keypress(function (event) {
		    if (event.keyCode == 13) {
		    	confirmarPopup_notaFiscal();
		    }
		});
		
		//Navegação principais campos encalhe
		$("#qtdeExemplar").keydown(function (event) {
			
			//Pressionar tecla seta para frente ">" ou "enter" - Move foco
			if (event.keyCode == 39 || event.keyCode == 13) {
		    	focusSelectRefField($("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace));
		    }
		});

		$("#cod_barras_conf_encalhe").keydown(function (event) {
			
			ConferenciaEncalhe.tratarEventoTeclaEspaco();
			
			//Pressionar tecla seta para frente ">" - Move foco
		    if (event.keyCode == 39) {
		    	$('#sm', ConferenciaEncalhe.workspace).focus();
		    	setTimeout (function () {$('#sm', ConferenciaEncalhe.workspace).select();}, 1);
		    }else if (event.keyCode == 37) {//"<"
		    	$('#qtdeExemplar', ConferenciaEncalhe.workspace).focus();
		    	setTimeout (function () {$('#qtdeExemplar', ConferenciaEncalhe.workspace).select();}, 1);
		    }else if (event.keyCode == 27){ //"ESC"
		    	$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).val("");
		    }
		});
		
		$("#sm").keydown(function (event) {			
		    
			ConferenciaEncalhe.tratarEventoTeclaEspaco();
			
			if (event.keyCode == 37) {//"<"
		    	$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).focus();
		    	setTimeout (function () {$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).select();}, 1);
		    }
		});
		
		
		this.bindkeypressCodigoSM();
		
		$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).autocomplete({source: []}); 
		
		PesquisaConferenciaEncalhe.bindkeypressCodigoBarras();

		
		ConferenciaEncalhe.removerAtalhos();
		$(".atalhosCE", ConferenciaEncalhe.workspace).hide();
		
		ConferenciaEncalhe.abrirModalLogadoDoBotao();
		
	},
	
	tratarEventoTeclaEspaco : function() {
		
		if (event.keyCode == 32 && visibleOverlay()) {
			 
			focusSelectRefField($("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace));
		}
	},
	
	
	bindkeypressCodigoSM : function(){
		
		$('#sm', ConferenciaEncalhe.workspace).keypress(function(e) {
			
			$("#sm", ConferenciaEncalhe.workspace).autocomplete({
				source: []
			});
			
			if (e.keyCode == 13) {
				
				ConferenciaEncalhe.autoCompletarPorCodigoSM();
			}
		});
	},

	
	getProdutoEdicao: function() {

		var data = [{name: "idProdutoEdicaoAnterior", value: ConferenciaEncalhe.ultimoIdProdutoEdicao}];
		
		if ($("#qtdeExemplar", ConferenciaEncalhe.workspace).val().trim() != "") {
			data.push({name: "quantidade", value: $("#qtdeExemplar", ConferenciaEncalhe.workspace).val()});
		}
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/pesquisarProdutoEdicao', data,
			function(result){
			
				ConferenciaEncalhe.setarValoresPesquisados(result);
				
				ConferenciaEncalhe.verificarPermissaoSuperVisor();
				
				focusSelectRefField($("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace));
			
			}, function() {
				
				$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).val("");
				$("#qtdeExemplar", ConferenciaEncalhe.workspace).val("1");
				$("#sm", ConferenciaEncalhe.workspace).val("");
				$("#codProduto", ConferenciaEncalhe.workspace).val("");
				
			}, 
			false, 
			"idTelaConferenciaEncalhe",
			false
		);
	},
	
	removerAtalhos: function() {
		
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
							
							window.event.preventDefault();
							
							ConferenciaEncalhe.removerTravaConferenciaEncalheCotaUsuario();
							
							$("#dialog-conferencia-nao-salva", ConferenciaEncalhe.workspace).dialog("close");
							
							$(self).tabs("remove", index);
							
							ConferenciaEncalhe.modalAberta = false;
							
							ConferenciaEncalhe.numeroCotaEditavel(true);
							
						},
						"Não" : function() {
							
							window.event.preventDefault();
							
							$("#dialog-conferencia-nao-salva", ConferenciaEncalhe.workspace).dialog("close");
							
							ConferenciaEncalhe.modalAberta = false;
							
							ConferenciaEncalhe.numeroCotaEditavel(false);
							
						}
					},
					
					form: $("#dialog-conferencia-nao-salva", this.workspace).parents("form")
				});
				
				
			} else {
				
				ConferenciaEncalhe.removerTravaConferenciaEncalheCotaUsuario();
				
				$(self).tabs("remove", index);
				
			}
		});	
		
		
	 },	
	
	/*
	 * ALTERACAO FINAL
	 */
	
	configurarAtalhos : function() {
		
		ConferenciaEncalhe.removerAtalhos();
		ConferenciaEncalhe.atribuirAtalhos();
	},
	
	buscarProdutoConferencia : function(){
		
		if ($("#sm").val() != ""){
			
			ConferenciaEncalhe.autoCompletarPorCodigoSM();
		}
		else if ($("#cod_barras_conf_encalhe").val() != ""){
			
			PesquisaConferenciaEncalhe.pesquisarPorCodigoDeBarras();
		}
		else{
				
			return;
		}
	},
	
	atribuirAtalhos: function(){

		$(document.body).bind('keydown.popUpNotaFiscal', jwerty.event('F6',function() {
			
			if (!ConferenciaEncalhe.modalAberta){
				ConferenciaEncalhe.popup_notaFiscal();
			}
			
		}));

		$(document.body).bind('keydown.salvarConferencia', jwerty.event('F8',function() {
			
			if (!ConferenciaEncalhe.modalAberta){
				
				ConferenciaEncalhe.processandoConferenciaEncalhe = true;
				
				$("#numeroCota", ConferenciaEncalhe.workspace).focus();
				
				setTimeout(function() {
					ConferenciaEncalhe.atualizarValoresGridInteira(ConferenciaEncalhe.popup_salvarInfos);
				}, 1000);
				
			}
			
		}));

		$(document.body).bind('keydown.finalizarConferencia', jwerty.event('F9',function() {
			if (!ConferenciaEncalhe.modalAberta){
				
				ConferenciaEncalhe.processandoConferenciaEncalhe = true;
				
				$("#numeroCota", ConferenciaEncalhe.workspace).focus();
				
				setTimeout(function() {
					ConferenciaEncalhe.atualizarValoresGridInteira(ConferenciaEncalhe.verificarCobrancaGerada);
				}, 1000);
				
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
	
	pesquisarCota : function() {
		
		var numeroCotaDipopnivelPesquisa = $("#numeroCota",ConferenciaEncalhe.workspace).attr('readonly');
		
		if (numeroCotaDipopnivelPesquisa == 'readonly'){
			return;
		}
		
		var data = [
		            {name: 'numeroCota', value : $("#numeroCota", ConferenciaEncalhe.workspace).val()}, 
		            {name: 'indObtemDadosFromBD', value : true},
		            {name: 'indConferenciaContingencia', value: false}
		           ];
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/iniciarConferenciaEncalhe", data,
				
		function(result){
			
			if (typeof result.IND_REABERTURA != 'undefined' && result.IND_REABERTURA == 'S'){
				
				ConferenciaEncalhe.modalAberta = true;
				
				$("#dialog-reabertura", ConferenciaEncalhe.workspace).dialog({
					resizable : false,
					height : 200,
					width : 360,
					modal : true,
					buttons : {
						"Sim" : function() {
							
							window.event.preventDefault();
							
							ConferenciaEncalhe.carregarListaConferencia(data);
							
							$("#dialog-reabertura", ConferenciaEncalhe.workspace).dialog("close");
							
							ConferenciaEncalhe.modalAberta = false;
							
							ConferenciaEncalhe.ifCotaEmiteNfe(data, ConferenciaEncalhe.popup_alert);
							
							focusSelectRefField($("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace));
							
							ConferenciaEncalhe.numeroCotaEditavel(false);
						},
						"Não" : function() {
							
							window.event.preventDefault();
							
							ConferenciaEncalhe.removerTravaConferenciaEncalheCotaUsuario();
							
							$("#dialog-reabertura", ConferenciaEncalhe.workspace).dialog("close");
							
							ConferenciaEncalhe.modalAberta = false;
							
							ConferenciaEncalhe.limparDadosConferenciaEncalheCota();
							
							$("#numeroCota", ConferenciaEncalhe.workspace).focus();
						}
					},
					form: $("#dialog-reabertura", this.workspace).parents("form"),
					close : function(){
						
						if( $("#vlrCE", ConferenciaEncalhe.workspace).length != 0 ){
							
							focusSelectRefField($("#vlrCE", ConferenciaEncalhe.workspace));
						
						} else if( $("#qtdCE", ConferenciaEncalhe.workspace).length != 0 ) {
							
							focusSelectRefField($("#qtdCE", ConferenciaEncalhe.workspace));
						
						}
					}					
				});
				
				$('.ui-button-text').last().parent().focus();
				
			} else {
				
				if(typeof result.IND_COTA_RECOLHE_NA_DATA != undefined && result.IND_COTA_RECOLHE_NA_DATA == 'N' ) {
					
					exibirMensagem('WARNING', [result.msg]);
					
				} 
				
				ConferenciaEncalhe.carregarListaConferencia(data);
				
				ConferenciaEncalhe.ifCotaEmiteNfe(data, ConferenciaEncalhe.popup_alert);
				
				if( $("#vlrCE", ConferenciaEncalhe.workspace).length != 0 ){
					
					focusSelectRefField($("#vlrCE", ConferenciaEncalhe.workspace));
				
				} else if( $("#qtdCE", ConferenciaEncalhe.workspace).length != 0 ) {
					
					focusSelectRefField($("#qtdCE", ConferenciaEncalhe.workspace));
				
				} else {
					
					focusSelectRefField($("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace));
				}
				
				ConferenciaEncalhe.numeroCotaEditavel(true);
			}
			
			ConferenciaEncalhe.limparDadosProduto();
		},
		function() {
			
			$("#numeroCota", ConferenciaEncalhe.workspace).val("");
			
			focusSelectRefField($("#numeroCota", ConferenciaEncalhe.workspace));
		});	
	},
	
	verificarValorTotalNotaFiscal : function() {
		var data = [{name: 'indConferenciaContingencia', value: false}];
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/verificarValorTotalNotaFiscal', data,
				
				function(result){
				
					if(result.tipoMensagem == 'SUCCESS') {
						
						if(result.indGeraDocumentoConfEncalheCota == true) {
							ConferenciaEncalhe.gerarDocumentosConferenciaEncalhe(result.tipos_documento_impressao_encalhe);
						}
						
						ConferenciaEncalhe.limparDadosConferenciaEncalheCota();
						
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
		
		if( typeof vr == 'undefined' || vr == '' || vr == null ) {
			return null;
		}
		
		vr = replaceAll(vr, ".", "");
		
		return vr;
	},
	
	verificarValorTotalCE : function() {
		var data = [{name: "valorCEInformado", value: ConferenciaEncalhe.preparaValor($("#vlrCE", ConferenciaEncalhe.workspace).val())},
		            {name: "qtdCEInformado", value: $("#qtdCE", ConferenciaEncalhe.workspace).val()}];
		
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
									
									window.event.preventDefault();
									
									ConferenciaEncalhe.verificarValorTotalNotaFiscal();
									
									$("#dialog-confirmar", ConferenciaEncalhe.workspace).dialog("close");
								},
								"Não" : function() {
									
									window.event.preventDefault();
									
									$("#dialog-confirmar", ConferenciaEncalhe.workspace).dialog("close");
								}
							},
							form: $("#dialog-confirmar", ConferenciaEncalhe.workspace).parents("form")
						});
					}
				}
		);
	},
	
	removerTravaConferenciaEncalheCotaUsuario : function() {
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/removerTravaConferenciaEncalheCotaUsuario');
		
	},
	
	carregarListaConferencia : function(data){
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/carregarListaConferencia', 
				data, 
				function(result){
					
					ConferenciaEncalhe.preProcessarConsultaConferenciaEncalhe(result);
					
					ConferenciaEncalhe.configurarAtalhos();
					
					$(".atalhosCE", ConferenciaEncalhe.workspace).show();
				}
		);
	},

	limparDadosConferenciaEncalheCota : function() {
		
		ConferenciaEncalhe.removerAtalhos();
		
		$(".atalhosCE", ConferenciaEncalhe.workspace).hide();
		
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
		
		$("#numeroCota", ConferenciaEncalhe.workspace).val("");
		
		$("#totalExemplaresFooter", ConferenciaEncalhe.workspace).text("");
		
		$("#numeroCota", ConferenciaEncalhe.workspace).select();
		
		$("#numeroCota", ConferenciaEncalhe.workspace).focus();
		
		ConferenciaEncalhe.setarValoresPesquisados();
	},
	
	preProcessarConsultaConferenciaEncalhe : function(result, indNaoAlteraFoco) {
		
		if (result.mensagens){
			
			exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
			
			return;
			
		}
		
		var innerTable = '';
		
		var modeloConferenciaEncalhe = result.listaConferenciaEncalhe;
		
		var indDistribuidorAceitaJuramentado = result.indDistribuidorAceitaJuramentado;
		
		var cotaAVista = result.cotaAVista;
		
		$("._dadosConfEncalhe", ConferenciaEncalhe.workspace).remove();
		
		if (modeloConferenciaEncalhe){
		
			$.each(modeloConferenciaEncalhe, 
				function(index, value) {
					
					var parcialNaoFinal =  value.parcialNaoFinal;
				
					var _class;
					
					if (index % 2 == 0){
						_class = "class_linha_1 _dadosConfEncalhe";
					} else {
						_class = "class_linha_2 _dadosConfEncalhe";
					}
				
					innerTable += "<tr class='" + _class + " _dados'><td nowrap='nowrap' style='text-align: center;'>";
					
					innerTable += "<input type='hidden' id='idProdutoEdicaoGrid_"+index+"' value='" + value.idProdutoEdicao + "'/>";
					
					var valorExemplares = parseInt(value.qtdExemplar);
					
					valorExemplares = isNaN(valorExemplares) ? 0 : valorExemplares;
					
					var inputExemplares = '<input isEdicao="true" id="qtdExemplaresGrid_' + index + 
						'" class="input-numericPacotePadrao" onchange="ConferenciaEncalhe.valorAnteriorInput = this.defaultValue;ConferenciaEncalhe.verificarPermissaoSuperVisor('+ 
						index +');" style="width:50px; text-align: center;" maxlength="255" value="' + valorExemplares + '"/>' +
						'<input id="idConferenciaEncalheHidden_' + index + '" type="hidden" value="' + value.idConferenciaEncalhe + '"/>';
					
					innerTable += inputExemplares + "</td>";
					
					innerTable += "<td nowrap='nowrap'>" + (value.codigoDeBarras ? value.codigoDeBarras : '') + "</td>";
					
					if(typeof value.codigoSM == 'undefined' || value.codigoSM == null) {
						value.codigoSM = '';
					}
					
					innerTable += "<td nowrap='nowrap'>" + value.codigoSM + "</td>";
					
					innerTable += "<td nowrap='nowrap'>" + value.codigo + "</td>";
					
					innerTable += "<td nowrap='nowrap'>" + value.nomeProduto + "</td>";
					
					innerTable += "<td nowrap='nowrap'>" + value.numeroEdicao + "</td>";
					
					innerTable += "<td style='text-align: right;' nowrap='nowrap'>" + parseFloat(value.precoCapa).toFixed(2) + "</td>";
					
					innerTable += "<td style='text-align: right;' nowrap='nowrap'>" + parseFloat(value.precoComDesconto).toFixed(4) + "</td>";
					
					innerTable += "<td align='right' nowrap='nowrap' id='valorTotalConferencia_" + index + "'>" + parseFloat(value.valorTotal).toFixed(4) + "</td>";
					
					if (value.dia || value.dataRecolhimento){
					
						if (value.dia && value.dia > 0){
						
							innerTable += "<td style='text-align: center;' nowrap='nowrap'>" + value.dia + "º" + "</td>";
						} else {
							
							innerTable += "<td style='text-align: center;' nowrap='nowrap' style='width: 20px;'>" + $.datepicker.formatDate("dd/mm/yy",$.datepicker.parseDate("yy-mm-dd", value.dataRecolhimento)) + "</td>";
						}
					} else {
						
						innerTable += "<td></td>";
					}
					
					var inputCheckBoxJuramentada = '';
					
					if (indDistribuidorAceitaJuramentado == true) {
					
						if(parcialNaoFinal == true && !cotaAVista) {
							
							inputCheckBoxJuramentada = '<input isEdicao="true" type="checkbox" ' + (value.juramentada == true ? 'checked="checked"' : '')
							+ ' onchange="ConferenciaEncalhe.valorAnteriorInput = this.defaultValue; ConferenciaEncalhe.verificarPermissaoSuperVisor('+ 
							index +');" id="checkGroupJuramentada_' + index + '"/>';
							
						} else {
							
							inputCheckBoxJuramentada = '<input isEdicao="true" type="checkbox" disabled="disabled" id="checkGroupJuramentada_' + index + '"/>';
							
						}
						
						innerTable += "<td style='text-align: center;' nowrap='nowrap'>" + inputCheckBoxJuramentada + "</td>";
					
					} 
					
					var imgDetalhar = '<img src="' + contextPath + '/images/ico_detalhes.png" border="0" hspace="3"/>';
					innerTable += '<td style="text-align: center;" nowrap="nowrap"><a href="javascript:;" onclick="ConferenciaEncalhe.exibirDetalhesConferencia(' + value.idConferenciaEncalhe + ');">' + imgDetalhar + '</a></td>';
					
					var imgExclusao = '<img src="' + contextPath + '/images/ico_excluir.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />';
					innerTable += '<td style="text-align: center;" nowrap="nowrap"><a isEdicao="true" href="javascript:;" onclick="ConferenciaEncalhe.excluirConferencia(' + value.idConferenciaEncalhe + ');">' + imgExclusao + '</a></td>';
					
					innerTable += "</tr>";
					
					$(innerTable).appendTo("#dadosGridConferenciaEncalhe", ConferenciaEncalhe.workspace);
					
					innerTable = '';
				}
			);
			
			if (!indDistribuidorAceitaJuramentado) {
				
				$("#colunaJuramentada", ConferenciaEncalhe.workspace).hide();
			}
			
			$('input[id*="qtdExemplaresGrid"]', ConferenciaEncalhe.workspace).numericPacotePadrao();
			
		}
		
		ConferenciaEncalhe.processandoConferenciaEncalhe = false;
		
		$(".outrosVlrsGrid", ConferenciaEncalhe.workspace).flexAddData({
			page: result.listaDebitoCredito.page, total: result.listaDebitoCredito.total, rows: ConferenciaEncalhe.formatarDadosDebitoCredito(result.listaDebitoCredito.rows)
		});
		
		$("#totalReparte", ConferenciaEncalhe.workspace).text(parseFloat(result.reparte).toFixed(4));
		$("#totalEncalhe", ConferenciaEncalhe.workspace).text(parseFloat(result.valorEncalhe).toFixed(4));
		$("#valorVendaDia", ConferenciaEncalhe.workspace).text(parseFloat(result.valorVendaDia).toFixed(4));
		$("#totalOutrosValores", ConferenciaEncalhe.workspace).text(parseFloat(result.valorDebitoCredito).toFixed(2));
		$("#valorAPagar", ConferenciaEncalhe.workspace).text(parseFloat(result.valorPagar).toFixed(2));
		$("#totalExemplaresFooter", ConferenciaEncalhe.workspace).text(result.qtdRecebida);
		
		$(".dadosFiltro", ConferenciaEncalhe.workspace).show();
		$("#nomeCota", ConferenciaEncalhe.workspace).text(result.razaoSocial);
		$("#statusCota", ConferenciaEncalhe.workspace).text(result.situacao);
		
		
		if(!indNaoAlteraFoco) {

			if( $("#vlrCE", ConferenciaEncalhe.workspace).length != 0 ){
				
				focusSelectRefField($("#vlrCE", ConferenciaEncalhe.workspace));
			
			} else if( $("#qtdCE", ConferenciaEncalhe.workspace).length != 0 ) {
				
				focusSelectRefField($("#qtdCE", ConferenciaEncalhe.workspace));
			
			} else {
				
				focusSelectRefField($("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace));
				
			}
			
		}
		
		
		bloquearItensEdicao(ConferenciaEncalhe.workspace);
	},
	
	
	formatarDadosDebitoCredito : function(listaDebitoCredito) {
		
		$.each(listaDebitoCredito, function(index, value){
			value.cell.valor = parseFloat(value.cell.valor).toFixed(2);
			
			var strDataArr;
			if(value.cell.dataLancamento.indexOf('-') > -1) {
				strDataArr = value.cell.dataLancamento.split('-');
			
				value.cell.dataLancamento = strDataArr[2] +'/'+ strDataArr[1] +'/'+ strDataArr[0];
			}
			
		});
		
		return listaDebitoCredito;
		
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
						'<td style="text-align: center"><input isEdicao="true" id="qtdeInformadaFinalizarConf_'+ index +'" onchange="ConferenciaEncalhe.recalcularValoresFinalizar('+ index +');" type="text" maxlength="255" style="width:50px; text-align: center;" value="' + parseInt(value.qtdInformada) + '"/></td>';
					
					innerTable += "<td style='text-align: center;'>" + (value.qtdExemplar ? parseInt(value.qtdExemplar) : "0") + "</td>";
				
					innerTable +=
						'<td style="text-align: center;"><input isEdicao="true" id="precoCapaFinalizarConf_'+ index +'" onchange="ConferenciaEncalhe.recalcularValoresFinalizar('+ index +');" maxlength="255" style="width:50px; text-align: right;" value="' + parseFloat(value.precoCapaInformado).toFixed(2) + '"/></td>';
					
					innerTable += "<td style='text-align: right;'>" + parseFloat(value.precoComDesconto).toFixed(2) + "</td>";
					
					innerTable += "<td style='text-align: right;' id='valorTotalConferenciaFinalizar_" + index + "'>" + parseFloat(value.valorTotal).toFixed(2) + "</td>";
					
					var imgExclusao = '<img src="' + contextPath + '/images/ico_excluir.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />';
					innerTable += '<td style="text-align: center;"><a isEdicao="true" href="javascript:;" onclick="ConferenciaEncalhe.excluirConferencia(' + value.idConferenciaEncalhe + ');">' + imgExclusao + '</a></td>';
					
					innerTable += "</tr>";
					
					$(innerTable).appendTo("#dadosGridConferenciaEncalheFinalizar", ConferenciaEncalhe.workspace);
					
					innerTable = '';
				}
			);
		
		bloquearItensEdicao(ConferenciaEncalhe.workspace);
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
					
					window.event.preventDefault();
					
					var data = [{name: 'indConferenciaContingencia', value: false}];
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/finalizarConferencia', data,
						
						function(conteudo){
						
							if(conteudo.tipoMensagem == 'SUCCESS') {
								
								$("#dialog-dadosNotaFiscal", ConferenciaEncalhe.workspace).dialog("close");

								
								if(conteudo.indGeraDocumentoConfEncalheCota == true) {
									ConferenciaEncalhe.gerarDocumentosConferenciaEncalhe(conteudo.tipos_documento_impressao_encalhe);
								}
								
								ConferenciaEncalhe.limparDadosConferenciaEncalheCota();
								
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
					
					window.event.preventDefault();
					
					$(this).dialog("close");
				}
			},
			form: $("#dialog-dadosNotaFiscal", this.workspace).parents("form"),
			close : function(){
				
				ConferenciaEncalhe.modalAberta = false;
				focusSelectRefField($("#numeroCota", ConferenciaEncalhe.workspace));
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
	
	atualizarValoresGridInteira : function(executarPosAtualizacaoGrid) {
		
		var listaItemGrid = $("._dadosConfEncalhe", ConferenciaEncalhe.workspace);
		
		var data = [];
		
		$(listaItemGrid).each(function(index, element){
			
			var valorIdConferenciaEncalhe = $(element).find("[id^='idConferenciaEncalheHidden_']").val();
			var valorQtdExemplares = $(element).find("[id^='qtdExemplaresGrid_']").val();
			var valorJuramentado = ( $(element).find("[id^='checkGroupJuramentada_']").attr("checked") == 'checked' );
			
			data.push({
				
				idConferenciaEncalhe: valorIdConferenciaEncalhe,
				qtdExemplarDaGrid: valorQtdExemplares,
	            juramentada: valorJuramentado
			
			});
			
			
		});
		
		var param = serializeArrayToPost('listaConferenciaEncalhe', 
				data);

		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/atualizarValoresGridInteira", 
				param, 
				function(result){
			
					executarPosAtualizacaoGrid();
			
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
	
	atualizarValores : function(index){
		
		if(ConferenciaEncalhe.processandoConferenciaEncalhe){
			return;
		}
		
		$("#qtdExemplaresGrid_" + index, ConferenciaEncalhe.workspace).prop("defaultValue",
				$("#qtdExemplaresGrid_" + index, ConferenciaEncalhe.workspace).val());
		
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
			
				$("#qtdExemplaresGrid_" + index, ConferenciaEncalhe.workspace).val(result.conf.qtdExemplar);
			
				$("#valorTotalConferencia_" + index, ConferenciaEncalhe.workspace).text(parseFloat(result.conf.valorTotal).toFixed(4));
				
				$("#totalReparte", ConferenciaEncalhe.workspace).text(parseFloat(result.reparte).toFixed(4));
				$("#totalEncalhe", ConferenciaEncalhe.workspace).text(parseFloat(result.valorEncalhe).toFixed(4));
				$("#valorVendaDia", ConferenciaEncalhe.workspace).text(parseFloat(result.valorVendaDia).toFixed(4));
				$("#totalOutrosValores", ConferenciaEncalhe.workspace).text(parseFloat(result.valorDebitoCredito).toFixed(2));
				$("#valorAPagar", ConferenciaEncalhe.workspace).text(parseFloat(result.valorPagar).toFixed(2));
				$("#totalExemplaresFooter", ConferenciaEncalhe.workspace).text(result.qtdRecebida);
				
				ConferenciaEncalhe.numeroCotaEditavel(false);
				
			}, function(){
				
				var data = [
							  {name: 'numeroCota', 			value : $("#numeroCota", ConferenciaEncalhe.workspace).val()}, 
							  {name: 'indObtemDadosFromBD', value : false},
							  {name: 'indConferenciaContingencia', value: false}
							 ];
							
				ConferenciaEncalhe.carregarListaConferencia(data);				
			}, null, null, false
		
		);
	},
	
	verificarPermissaoSuperVisor : function(index){
		
		if(ConferenciaEncalhe.processandoConferenciaEncalhe){
			return;
		}
		
		var data;
		
		if (index || index == 0){
			
			data = [
	            {name: "idConferencia", value: $("#idConferenciaEncalheHidden_" + index, ConferenciaEncalhe.workspace).val()},
	            {name: "qtdExemplares", value: $("#qtdExemplaresGrid_" + index, ConferenciaEncalhe.workspace).val()},
	            {name: 'indConferenciaContingencia', value: false},
	            {name: 'indPesquisaProduto', value: false}
			];
		} else {
			
			data = [
	            {name: "qtdExemplares", value: $("#qtdeExemplar", ConferenciaEncalhe.workspace).val()},
	            {name: "produtoEdicaoId", value: $("#idProdutoEdicaoHidden", ConferenciaEncalhe.workspace).val()},
	            {name: 'indConferenciaContingencia', value: false},
	            {name: 'indPesquisaProduto', value: true}
			];
		}
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/verificarPermissaoSupervisor", 
			data, 
			function(result){
				
				if (result && result.result != ""){
					
					ConferenciaEncalhe.resetValue = true;
					
					if (result[0]){
						
						exibirMensagem('WARNING', [result[1]]);
						ConferenciaEncalhe.resetValue = false;
						
						if (index || index == 0){
							
							ConferenciaEncalhe.atualizarValores(index);
						} else {
							
							ConferenciaEncalhe.adicionarProdutoConferido();
						}
						
					} else {
						
						$("#msgSupervisor", ConferenciaEncalhe.workspace).text(result[1]);
						
						$("#dialog-autenticar-supervisor", ConferenciaEncalhe.workspace).dialog({
							resizable: false,
							height:'auto',
							width:400,
							modal: true,
							buttons: {
								"Ok": function() {
									
									ConferenciaEncalhe.resetValue = false;
									ConferenciaEncalhe.autenticarSupervisor(index);
									
								},
								"Cancelar": function() {
									
									if (index || index == 0){
										
										$("#qtdExemplaresGrid_" + index, ConferenciaEncalhe.workspace).val(ConferenciaEncalhe.valorAnteriorInput);
									} else {
										
										ConferenciaEncalhe.limparDadosProduto(true);
									}
									
									$(this).dialog("close");
								}
							},
							form: $("#dialog-autenticar-supervisor", this.workspace).parents("form"),
							close: function(){
								
								if (ConferenciaEncalhe.resetValue){
									if (index || index == 0){
										
										$("#qtdExemplaresGrid_" + index, ConferenciaEncalhe.workspace).val(ConferenciaEncalhe.valorAnteriorInput);
									} else {
										
										ConferenciaEncalhe.limparDadosProduto(true);
									}
								}
							},
							open: function(){
								
								focusSelectRefField($("#inputUsuarioSup", ConferenciaEncalhe.workspace));
							}
						});
					}
					
				} else {
					
					if (index || index == 0){
						
						ConferenciaEncalhe.atualizarValores(index);
					} else {
						
						ConferenciaEncalhe.adicionarProdutoConferido();
					}
				}
			},null, null,null, false
		);
	},
	
	autenticarSupervisor : function(index){
		var paramUsuario = [
			{name:"usuario", value:$("#inputUsuarioSup", ConferenciaEncalhe.workspace).val()},
			{name:"senha",value:$("#inputSenha", ConferenciaEncalhe.workspace).val()}
		];
		
		if (paramUsuario.usuario == '' || paramUsuario.senha == ''){
			
			exibirMensagem(
				'WARNING', 
				['Usuário e senha são obrigatórios.']
			);
			
			ConferenciaEncalhe.resetValue = true;
			
			return;
		}
		
		$.postJSON(
			contextPath + "/devolucao/conferenciaEncalhe/verificarPermissaoSupervisor", 
			paramUsuario,
			function(result) {
				
				$("#inputUsuarioSup", ConferenciaEncalhe.workspace).val("");
				$("#inputSenha", ConferenciaEncalhe.workspace).val("");
				
				if (result.tipoMensagem){
					
					exibirMensagem(
						result.tipoMensagem, 
						result.listaMensagens
					);
					
					ConferenciaEncalhe.resetValue = true;
					
					return;
				}
				
				if (index || index == 0){
					
					ConferenciaEncalhe.atualizarValores(index);
				} else {
					
					ConferenciaEncalhe.adicionarProdutoConferido();
				}
				
				$("#dialog-autenticar-supervisor", ConferenciaEncalhe.workspace).dialog("close");
				return;
			},
			function (){
				ConferenciaEncalhe.resetValue = true;
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
				
				$("#precoDesconto", ConferenciaEncalhe.workspace).text((parseFloat(result.precoCapa) - parseFloat(result.desconto)).toFixed(4));
				
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
					
					window.event.preventDefault();
					
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
					
					window.event.preventDefault();
					
					$(this).dialog("close");
					$('#pesq_cota', ConferenciaEncalhe.workspace).focus();
				}
			},
			form: $("#dialog-excluir-conferencia", this.workspace).parents("form"),
			close : function(){
				
				ConferenciaEncalhe.modalAberta = false;
				focusSelectRefField($("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace));
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
					
					window.event.preventDefault();
					
					confirmarPopup_logado();
				},
				"Cancelar" : function() {
					
					window.event.preventDefault();
					
					$(this).dialog("close");
					
					$('#pesq_cota', ConferenciaEncalhe.workspace).focus();
					
					var indexAbaConferenciaEnc = $("li", $('.ui-tabs-nav')).index($(".ui-tabs-nav").find('.conferencia_encalhe').parent());
					if(!VEIO_DO_BT_BOX_ENCALHE && indexAbaConferenciaEnc > -1){
						//Aba conferencia aberta
						
						$("#workspace").tabs("remove", indexAbaConferenciaEnc);
					}
					VEIO_DO_BT_BOX_ENCALHE = false;
				}
			},
			form: $("#dialog-logado", this.workspace).parents("form"),
			
			open : function(){
				setTimeout (function () {$("#boxLogado").focus();}, 1);
			},
			
			close : function(){
				ConferenciaEncalhe.modalAberta = false;
				$("#numeroCota", ConferenciaEncalhe.workspace).focus();
			}
		});
	},
	
	setarValoresPesquisados : function(result){
		
		if (!result){
			
			$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).val("");
			$("#sm", ConferenciaEncalhe.workspace).val("");
			$("#codProduto", ConferenciaEncalhe.workspace).val("");
			
			$("#idProdutoEdicaoHidden", ConferenciaEncalhe.workspace).val("");
			
			$("#nomeProduto", ConferenciaEncalhe.workspace).text("");
			$("#edicaoProduto", ConferenciaEncalhe.workspace).text("");
			$("#precoCapa", ConferenciaEncalhe.workspace).text("");
			$("#desconto", ConferenciaEncalhe.workspace).text("");
			
			$("#valorTotal", ConferenciaEncalhe.workspace).text("");
			
			$("#qtdeExemplar", ConferenciaEncalhe.workspace).val("");
			
			return;
		}
		
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
		$("#precoCapa", ConferenciaEncalhe.workspace).text(parseFloat(result.precoCapa).toFixed(2));
		$("#desconto", ConferenciaEncalhe.workspace).text(parseFloat(result.desconto).toFixed(4));
		
		$("#valorTotal", ConferenciaEncalhe.workspace).text(((parseFloat(result.precoCapa) - parseFloat(result.desconto)) * parseFloat(result.qtdExemplar)).toFixed(2));
		
		$("#qtdeExemplar", ConferenciaEncalhe.workspace).val(parseInt(result.qtdExemplar));
	},

	adicionarProdutoConferido : function(){
		
		var idProdutoEdicao = $("#idProdutoEdicaoHidden", ConferenciaEncalhe.workspace).val();
		
		var data = [{name: "produtoEdicaoId", value: idProdutoEdicao}, 
		            {name: "qtdExemplares", value: $("#qtdeExemplar", ConferenciaEncalhe.workspace).val()}];
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/adicionarProdutoConferido', data,
			function(result){
				
				ConferenciaEncalhe.preProcessarConsultaConferenciaEncalhe(result);	
				
                ConferenciaEncalhe.limparDadosProduto(true);
				
				focusSelectRefField($("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace));
				
				$("#qtdeExemplar", ConferenciaEncalhe.workspace).val(1);
			},
			function(){
				
				ConferenciaEncalhe.limparDadosProduto(true);
				
				focusSelectRefField($("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace));
				
				$("#qtdeExemplar", ConferenciaEncalhe.workspace).val(1);
			}, null, null, false	
		);
		
		ConferenciaEncalhe.numeroCotaEditavel(false);
	},

	limparDadosProduto : function(keepQtdValorCE){
		
		$("#qtdeExemplar", ConferenciaEncalhe.workspace).val("1");
		$("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace).val("");
		$("#sm", ConferenciaEncalhe.workspace).val("");
		$("#codProduto", ConferenciaEncalhe.workspace).val("");
		
		$("#nomeProduto", ConferenciaEncalhe.workspace).text("");
		$("#edicaoProduto", ConferenciaEncalhe.workspace).text("");
		$("#precoCapa", ConferenciaEncalhe.workspace).text("");
		$("#desconto", ConferenciaEncalhe.workspace).text("");
		$("#valorTotal", ConferenciaEncalhe.workspace).text("");

		if(keepQtdValorCE != true) {

			$("#vlrCE", ConferenciaEncalhe.workspace).val("");
			$("#qtdCE", ConferenciaEncalhe.workspace).val("");
			
		}
		
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
					
					window.event.preventDefault();
					
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
					
					window.event.preventDefault();
					
					$("#dialog-alert", ConferenciaEncalhe.workspace).dialog("close");
					$("#vlrCE", ConferenciaEncalhe.workspace).focus();
					$("#qtdCE", ConferenciaEncalhe.workspace).focus();
					
				}
			}, open : function(){
				
				$(this).parent('div').find('button:contains("Sim")').focus();
			}, close : function(){
				
				ConferenciaEncalhe.modalAberta = false;
				focusSelectRefField($("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace));
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
					
					window.event.preventDefault();
					
					confirmarPopup_notaFiscal();
				},
				"Cancelar" : function() {
					
					window.event.preventDefault();
					
					$(this).dialog("close");
					$("#vlrCE", ConferenciaEncalhe.workspace).focus();
					$("#qtdCE", ConferenciaEncalhe.workspace).focus();
					
				}
			}, open : function(){
				
				setTimeout (function () {$("#numNotaFiscal").focus();}, 1);
			}, close : function(){
				
				ConferenciaEncalhe.modalAberta = false;
				
				focusSelectRefField($("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace));
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
					
					window.event.preventDefault();
					
					if ( $("#pesq_prod", ConferenciaEncalhe.workspace).val() != "" ){

					    ConferenciaEncalhe.pesquisaProduto();
					}
					
					$(this).dialog("close");
				},
				"Cancelar" : function() {
					
					window.event.preventDefault();
					
					ConferenciaEncalhe.limparDadosProduto();
					
					$(this).dialog("close");
				}
			}, 
			open : function (){
				setTimeout (function () {$("#pesq_prod").focus();}, 1);
			},
			close : function(){
				
				ConferenciaEncalhe.modalAberta = false;
				
				focusSelectRefField($("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace));
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
					
					window.event.preventDefault();
					
					$(this).dialog("close");
				}
			}, close : function(){
				
				ConferenciaEncalhe.modalAberta = false;
				focusSelectRefField($("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace));
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
					
					window.event.preventDefault();
					
					$(this).dialog("close");
				}
			}, close : function(){
				
				ConferenciaEncalhe.modalAberta = false;
				focusSelectRefField($("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace));
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
					
					window.event.preventDefault();
					
					var data = [{name: 'indConferenciaContingencia', value: false}];
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/salvarConferencia', data,
						
						function(result){
						
							if(result.tipoMensagem == 'SUCCESS') {
								
						        ConferenciaEncalhe.limparDadosConferenciaEncalheCota();	
							}
						
							exibirMensagem(result.tipoMensagem, result.listaMensagens);
							
							$("#dialog-salvar", ConferenciaEncalhe.workspace).dialog("close");
							
							ConferenciaEncalhe.numeroCotaEditavel(true);
							
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
					
					window.event.preventDefault();
					
					$(this).dialog("close");
				}

			}, close : function(){
				
				ConferenciaEncalhe.modalAberta = false;
				
				focusSelectRefField($("#numeroCota", ConferenciaEncalhe.workspace));
			},
			
			form: $("#dialog-salvar", this.workspace).parents("form")
		});

	},
	
	autoCompletarPorCodigoSM: function() {

		var codSM = $("#sm", ConferenciaEncalhe.workspace).val().trim();
		
		var data = 
			[{name: 'numeroCota', value: $("#numeroCota", ConferenciaEncalhe.workspace).val()}, 
			 {name: 'sm', value: codSM}];

		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/autoCompleteProdutoEdicaoCodigoSM", data,
			function(result){
				//EXIBE AUTOCOMPLETE SOMENTE SE HOUVER MAIS DE UM RESULTADO
			    if (result.length > 1){				
			    	
					$("#sm", ConferenciaEncalhe.workspace).autocomplete({
						source: result,
						select: function(event, ui){			
							
							ConferenciaEncalhe.ultimoIdProdutoEdicao = ui.item.chave.$;	
							
							ConferenciaEncalhe.getProdutoEdicao();								
						},
						
						delay : 100,
					});	
					
					$("#sm", ConferenciaEncalhe.workspace).autocomplete("search", codSM);
					
				}else{
			    	
			    	//$("#sm", ConferenciaEncalhe.workspace).autocomplete({});
			    	
			    	ConferenciaEncalhe.ultimoIdProdutoEdicao = result[0].chave.$;
			    	
        		    ConferenciaEncalhe.getProdutoEdicao();
			    }    
			}, 
			function() {
		
				$("#qtdeExemplar", ConferenciaEncalhe.workspace).val("1");
					
				$('#sm', ConferenciaEncalhe.workspace).val("");
				
				focusSelectRefField($("#sm", ConferenciaEncalhe.workspace));
			}, null, null, false
		);
	},
	
	inicializarAutoCompleteSugestaoProdutoEdicao : function() {
		
		
		
		$("#pesq_prod", ConferenciaEncalhe.workspace).autocomplete({
			source: function(request, response) {
				
				$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/pesquisarProdutoPorCodigoNome', 
					{codigoNomeProduto: request.term}, 
					function(result){
						
						response(result);
						
					});
				},
			
			select: function(event, ui){
				
				$("#codProduto", ConferenciaEncalhe.workspace).val(ui.item.chave.string);
				ConferenciaEncalhe.ultimoIdProdutoEdicao = ui.item.chave.long;
			},
			delay : 500,
		});
		
		
		
		
			
		
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
									
									window.event.preventDefault();
									
									ConferenciaEncalhe.removerTravaConferenciaEncalheCotaUsuario();
									
									$("#dialog-conferencia-nao-salva", ConferenciaEncalhe.workspace).dialog("close");
									
									$('#workspace').tabs('remove', $('#workspace').tabs('option','selected'));
									
									$(".tipsy").hide();
									
									$('#workspace').tabs('addTab', "Conferência Encalhe Cota Contingência",
											contextPath + "/devolucao/conferenciaEncalhe/contingencia" + "?random=" + Math.random());
									
									ConferenciaEncalhe.modalAberta = false;
									
									ConferenciaEncalhe.numeroCotaEditavel(true);
									
								},
								"Não" : function() {
									
									window.event.preventDefault();
									
									$("#dialog-conferencia-nao-salva", ConferenciaEncalhe.workspace).dialog("close");
									
									ConferenciaEncalhe.modalAberta = false;
									
									ConferenciaEncalhe.numeroCotaEditavel(false);
									
								}
							},
							
							form: $("#dialog-conferencia-nao-salva", this.workspace).parents("form")
						});
						
						
					} else {
						
						ConferenciaEncalhe.removerTravaConferenciaEncalheCotaUsuario();
						
						$('#workspace').tabs('remove', $('#workspace').tabs('option','selected'));
						
						$(".tipsy").hide();
						
						$('#workspace').tabs('addTab', "Conferência Encalhe Cota Contingência",
								contextPath + "/devolucao/conferenciaEncalhe/contingencia" + "?random=" + Math.random());

						ConferenciaEncalhe.modalAberta = false;
						
						ConferenciaEncalhe.numeroCotaEditavel(true);

						
					}
				});	
	
	},
	

	abrirModalLogadoDoBotao : function (){
		VEIO_DO_BT_BOX_ENCALHE = true;
		ConferenciaEncalhe.popup_logado();
	},
	
	verificarCobrancaGerada: function(){
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/verificarCobrancaGerada', null,
		
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
								
								window.event.preventDefault();
								
								confirmarVeificacaoCobrancaGerada();
								
								ConferenciaEncalhe.numeroCotaEditavel(true);
							},
							"Cancelar" : function(){
							
								window.event.preventDefault();
								
								$("#dialog-confirmar-regerar-cobranca", ConferenciaEncalhe.workspace).dialog("close");
							}
						}, 
						
						close : function(){
							
							ConferenciaEncalhe.modalAberta = false;
							focusSelectRefField($("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace));
						},
						
						
						form: $("#dialog-confirmar-regerar-cobranca", ConferenciaEncalhe.workspace).parents("form")
					});
					
				} else {
					
					ConferenciaEncalhe.verificarValorTotalCE();
					
					ConferenciaEncalhe.numeroCotaEditavel(true);
				}
				
			}, null, true, "dialog-confirmar-regerar-cobranca"
		);
	}

}, BaseController);

function confirmarVeificacaoCobrancaGerada(){
	$("#dialog-confirmar-regerar-cobranca", ConferenciaEncalhe.workspace).dialog("close");
	ConferenciaEncalhe.verificarValorTotalCE();
}

function confirmarPopup_logado(){
	$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/salvarIdBoxSessao", "idBox=" + $("#boxLogado", ConferenciaEncalhe.workspace).val(), 
			function(){
				$("#dialog-logado", ConferenciaEncalhe.workspace).dialog("close");
				$('#numeroCota', ConferenciaEncalhe.workspace).focus();
			}, null, true, "idModalBoxRecolhimento"
		);
}

function confirmarPopup_notaFiscal(){
	var data = []; 
	
	if($('input:radio[name=radioNFE]:checked').val() == 'S') {
		
		data = [
		        {name : "notaFiscal.numero", value : $("#numNotaFiscal", ConferenciaEncalhe.workspace).val()},
		        {name : "notaFiscal.serie", value : $("#serieNotaFiscal", ConferenciaEncalhe.workspace).val()},
		        {name : "notaFiscal.dataEmissao", value : $("#dataNotaFiscal", ConferenciaEncalhe.workspace).val()},
		        {name : "notaFiscal.valorProdutos", value : priceToFloat($("#valorNotaFiscal", ConferenciaEncalhe.workspace).val())},
		        {name : "notaFiscal.chaveAcesso", value : $("#chaveAcessoNFE", ConferenciaEncalhe.workspace).val()}
		        ];
		
	} else {
		
		data = [
		        {name : "notaFiscal.numero", value : $("#numNotaFiscal", ConferenciaEncalhe.workspace).val()},
		        {name : "notaFiscal.serie", value : $("#serieNotaFiscal", ConferenciaEncalhe.workspace).val()},
		        {name : "notaFiscal.dataEmissao", value : $("#dataNotaFiscal", ConferenciaEncalhe.workspace).val()},
		        {name : "notaFiscal.valorProdutos", value : priceToFloat($("#valorNotaFiscal", ConferenciaEncalhe.workspace).val())}
		        ];
		
	}


	$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/salvarNotaFiscal', data, 
			function(result){
		
		$("#dialog-notaFiscal", ConferenciaEncalhe.workspace).dialog("close");
		
		focusSelectRefField($("#cod_barras_conf_encalhe", ConferenciaEncalhe.workspace));
		
		$("#vlrCE", ConferenciaEncalhe.workspace).val($("#valorNotaFiscal", ConferenciaEncalhe.workspace).val());
		
	}, null, true, "dialog-notaFiscal"
	);
}

//@ sourceURL=scriptConferenciaEncalhe.js
