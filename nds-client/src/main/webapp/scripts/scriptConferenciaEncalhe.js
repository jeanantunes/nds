var ConferenciaEncalhe = {
	
	modalAberta: false, 
	
	idProdutoEdicao: "",
	
	idConferenciaEncalheVisualizacao: "",
	
	pesquisarCota : function() {
		
		var data = [
		            {name: 'numeroCota', value : $("#numeroCota").val()}, 
		            {name: 'indObtemDadosFromBD', value : true},
		            {name: 'indConferenciaContingencia', value: false}
		           ];
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/verificarReabertura", data,
			function(result){
				
				if (result.listaMensagens && result.listaMensagens[0] == "REABERTURA"){
					
					ConferenciaEncalhe.modalAberta = true;
					
					$("#dialog-reabertura").dialog({
						resizable : false,
						height : 200,
						width : 360,
						modal : true,
						buttons : {
							"Sim" : function() {
								
								ConferenciaEncalhe.carregarListaConferencia(data);
								ConferenciaEncalhe.popup_alert();
								$("#dialog-reabertura").dialog("close");
							},
							"Não" : function() {
								$("#dialog-reabertura").dialog("close");
								ConferenciaEncalhe.modalAberta = false;
							}
						}
					});
				} else {
					
					ConferenciaEncalhe.carregarListaConferencia(data);
					$("#dialog-reabertura").dialog("close");
					ConferenciaEncalhe.popup_alert();
				}
			}
		);
	},
	
	verificarValorTotalNotaFiscal : function() {
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/verificarValorTotalNotaFiscal',null,
				
				function(result){
				
					if(result.tipoMensagem == 'SUCCESS') {
						
						if(result.indGeraDocumentoConfEncalheCota == true) {

							ConferenciaEncalhe.gerarDocumentosConferenciaEncalhe(result.tiposDocumento);
							
						}
						
						var data = [
						     
						     {name: 'numeroCota', value : $("#numeroCota").val()}, 
						     {name: 'indObtemDadosFromBD', value : true},
						     {name: 'indConferenciaContingencia', value: false}
						];
						
						ConferenciaEncalhe.carregarListaConferencia(data);
						
					}
				
					exibirMensagem(result.tipoMensagem, result.listaMensagens);
				},
				
				function(){
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/carregarListaConferencia', 
							null, 
							function(result){
								
								ConferenciaEncalhe.abrirDialogNotaFiscalDivergente(result);
							}
					);
				}, true, "idModalDadosNotaFiscal"
		);
		
	},
	
	verificarValorTotalCE : function() {
		
		var data = [{name: "valorCEInformado", value: $("#vlrCE").val()}];
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/verificarValorTotalCE', data, 
		
				function(conteudo) {
					
					if(conteudo.valorCEInformadoValido == true) {
						
						ConferenciaEncalhe.verificarValorTotalNotaFiscal();
						
					} else {
						
						var indConfirmacaoValorCE = confirm(conteudo.mensagemConfirmacao);
						
						if(indConfirmacaoValorCE == true) {
							
							ConferenciaEncalhe.verificarValorTotalNotaFiscal();
							
						} else {
							
							return;
							
						}
						
						
						
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

	preProcessarConsultaConferenciaEncalhe : function(result) {
		
		if (result.mensagens){
			
			exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
			return;
		}
		
		var innerTable = '';
		
		var modeloConferenciaEncalhe = result.listaConferenciaEncalhe;
		
		var indDistribuidorAceitaJuramentado = result.indDistribuidorAceitaJuramentado;
		
		$("._dadosConfEncalhe").remove();
		
		if (modeloConferenciaEncalhe){
		
			$.each(modeloConferenciaEncalhe, 
				function(index, value) {
					
					var _class;
					
					if (index % 2 == 0){
						_class = "class_linha_1 _dadosConfEncalhe";
					} else {
						_class = "class_linha_2 _dadosConfEncalhe";
					}
				
					innerTable += "<tr class='" + _class + " _dados'><td nowrap='nowrap' style='text-align: center;'>";
					
					var valorExemplares = parseInt(value.qtdExemplar);
					
					var inputExemplares = '<input id="qtdExemplaresGrid_' + index + '" onchange="ConferenciaEncalhe.atualizarValores('+ index +');" style="width:50px; text-align: center;" maxlength="255" value="' + valorExemplares + '"/>' +
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
					
					if(indDistribuidorAceitaJuramentado == true) {
						
						inputCheckBoxJuramentada = '<input type="checkbox" ' + (value.juramentada == true ? 'checked="checked"' : '')
						+ ' onchange="ConferenciaEncalhe.atualizarValores('+ index +');" id="checkGroupJuramentada_' + index + '"/>';
						
					} else {
						
						inputCheckBoxJuramentada = '<input type="checkbox" disabled="disabled" id="checkGroupJuramentada_' + index + '"/>';
						
					}
					
					innerTable += "<td style='text-align: center;' nowrap='nowrap'>" + inputCheckBoxJuramentada + "</td>";
					
					var imgDetalhar = '<img src="' + contextPath + '/images/ico_detalhes.png" border="0" hspace="3"/>';
					innerTable += '<td style="text-align: center;" nowrap="nowrap"><a href="javascript:;" onclick="ConferenciaEncalhe.exibirDetalhesConferencia(' + value.idConferenciaEncalhe + ');">' + imgDetalhar + '</a></td>';
					
					var imgExclusao = '<img src="' + contextPath + '/images/ico_excluir.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />';
					innerTable += '<td style="text-align: center;" nowrap="nowrap"><a href="javascript:;" onclick="ConferenciaEncalhe.excluirConferencia(' + value.idConferenciaEncalhe + ');">' + imgExclusao + '</a></td>';
					
					innerTable += "</tr>";
					
					$(innerTable).appendTo("#dadosGridConferenciaEncalhe");
					
					innerTable = '';
				}
			);
			
			$('input[id*="qtdExemplaresGrid"]').numeric();
			
		}
		
		$(".outrosVlrsGrid").flexAddData({
			page: result.listaDebitoCredito.page, total: result.listaDebitoCredito.total, rows: result.listaDebitoCredito.rows
		});
		
		$("#totalReparte").text(parseFloat(result.reparte).toFixed(2));
		$("#totalEncalhe").text(parseFloat(result.valorEncalhe).toFixed(2));
		$("#valorVendaDia").text(parseFloat(result.valorVendaDia).toFixed(2));
		$("#totalOutrosValores").text(parseFloat(result.valorDebitoCredito).toFixed(2));
		$("#valorAPagar").text(parseFloat(result.valorPagar).toFixed(2));
		
		$(".dadosFiltro").show();
		$("#nomeCota").text(result.razaoSocial);
		$("#statusCota").text(result.situacao);
	},
	
	gerarDocumentosConferenciaEncalhe : function(tiposDocumento) {
		
		var fileArray = [];
		
		 $.each(tiposDocumento, function(index, value){
			 
			 if(value == 'SLIP') {
				 fileArray[index] = contextPath + '/devolucao/conferenciaEncalhe/gerarSlip';
			 } 
			 
			 if(value == 'BOLETO_OU_RECIBO') {
				 fileArray[index] = contextPath + '/devolucao/conferenciaEncalhe/gerarBoletoOuRecibo';
			 }
			 
		 });
		
		 var fileIndex = 0;

		 $('#download-iframe').attr('src', fileArray[fileIndex]);
		 
		 fileIndex++;

		 var interval = setInterval(function() {
			 
		        if(fileIndex < fileArray.length) {
		            
		        	$('#download-iframe').attr('src', fileArray[fileIndex]);
		            
		        	fileIndex++;
		            
		        } else {
		            clearInterval(interval);
		        }
		        
		    }, 100 );
		
	},
	
	carregarGridItensNotaFiscal : function (modeloConferenciaEncalhe) {
		
		$("._dadosConfEncalheFinalizar").remove();
		
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
					
					$(innerTable).appendTo("#dadosGridConferenciaEncalheFinalizar");
					
					innerTable = '';
				}
			);
		
	},
	
	abrirDialogNotaFiscalDivergente : function(result){
		
		if (result.mensagens){
			
			exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
			return;
		}
		
		ConferenciaEncalhe.modalAberta = true;
		
		$("#dialog-dadosNotaFiscal").dialog({
			resizable : false,
			height : 'auto',
			width : 877,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/finalizarConferencia', null,
						
						function(conteudo){
						
							if(conteudo.tipoMensagem == 'SUCCESS') {
								
								$("#dialog-dadosNotaFiscal").dialog("close");

								
								if(conteudo.indGeraDocumentoConfEncalheCota == true) {
									ConferenciaEncalhe.gerarDocumentosConferenciaEncalhe(conteudo.tiposDocumento);
								}
								
								var data = [
								  {name: 'numeroCota', 			value : $("#numeroCota").val()}, 
								  {name: 'indObtemDadosFromBD', value : true},
								  {name: 'indConferenciaContingencia', value: false}
								 ];
								
								ConferenciaEncalhe.carregarListaConferencia(data);
								
							}

							exibirMensagem(conteudo.tipoMensagem, conteudo.listaMensagens);
							
						}, null, true, "idModalDadosNotaFiscal"
					);
				},
				"Cancelar" : function() {
					
					$(this).dialog("close");
				}
			}, close : function(){
				
				ConferenciaEncalhe.modalAberta = false;
			}
		});
		
		var notaFiscal = result.notaFiscal;
		
		if (notaFiscal){
		
			$("#numeroNotaFiscalExibir").text(notaFiscal.numero);
			$("#serieExibir").text(notaFiscal.serie);
			$("#dataExibir").text(notaFiscal.dataEmissao);
			$("#valorTotalNotaFiscalExibir").text(parseFloat(notaFiscal.valorProdutos).toFixed(2));
			$("#chaveAcessoExibir").text(notaFiscal.chaveAcesso);
		}
		
		var modeloConferenciaEncalhe = result.listaConferenciaEncalhe;
		
		$("._dadosConfEncalheFinalizar").remove();
		
		if (modeloConferenciaEncalhe){
		
			ConferenciaEncalhe.carregarGridItensNotaFiscal(modeloConferenciaEncalhe);
			
			$('input[id*="qtdeInformadaFinalizarConf"]').numeric();
			$('input[id*="precoCapaFinalizarConf"]').numeric();
		}
		
		$("#somatorioQtdInformada").text(parseInt(result.qtdInformada));
		$("#somatorioQtdRecebida").text(parseInt(result.qtdRecebida));
		$("#somatorioTotal").text(parseFloat(result.valorPagar).toFixed(2));
	},
	
	atualizarValores : function(index){
		
		var juramentado = false;
		
		if( $("#checkGroupJuramentada_" + index).attr("checked") == 'checked' ) {
			juramentado = true;
		}
		
		var data = [
            {name: "idConferencia", value: $("#idConferenciaEncalheHidden_" + index).val()},
            {name: "qtdExemplares", value: $("#qtdExemplaresGrid_" + index).val()},
            {name: "juramentada", value: juramentado}
		];
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/atualizarValores", 
			data, 
			function(result){
				
				$("#valorTotalConferencia_" + index).text(parseFloat(result.conf.valorTotal).toFixed(2));
				
				$("#totalReparte").text(parseFloat(result.reparte).toFixed(2));
				$("#totalEncalhe").text(parseFloat(result.valorEncalhe).toFixed(2));
				$("#valorVendaDia").text(parseFloat(result.valorVendaDia).toFixed(2));
				$("#totalOutrosValores").text(parseFloat(result.valorDebitoCredito).toFixed(2));
				$("#valorAPagar").text(parseFloat(result.valorPagar).toFixed(2));
			}
		);
	},
	
	recalcularValoresFinalizar : function(index){
		
		var data = [
            {name: "idConferencia", value: $("#idConferenciaEncalheHiddenFinalizarConf_" + index).val()},
            {name: "qtdInformada", value: $('#qtdeInformadaFinalizarConf_' + index).val()},
            {name: "valorCapaInformado", value : $('#precoCapaFinalizarConf_' + index).val()}
		];
		
		$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/alterarQtdeValorInformado", 
			data, 
			function(result){
				
				$("#valorTotalConferenciaFinalizar_" + index).text(parseFloat(result.conf.valorTotal).toFixed(2));
				
				$("#somatorioQtdInformada").text(parseFloat(result.qtdInformada).toFixed(2));
				$("#somatorioQtdRecebida").text(parseFloat(result.qtdRecebida).toFixed(2));
				$("#somatorioTotal").text(parseFloat(result.valorPagar).toFixed(2));
			}
		);
	},
	
	exibirDetalhesConferencia : function(idConferenciaEncalhe){
		
		ConferenciaEncalhe.idConferenciaEncalheVisualizacao = idConferenciaEncalhe;
		
		var data = [{name: "idConferenciaEncalhe", value: idConferenciaEncalhe}];
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/buscarDetalhesProduto', data,
			function (result){
				
				$("#nomeProdutoDetalhe").text(result.nomeProduto);
				$("#precoCapaDetalhe").text(result.precoCapa);
				$("#chamadaCapa").text(result.chamadaCapa);
				$("#fornecedor").text(result.nomeFornecedor);
				$("#brinde").text(result.possuiBrinde == "true" ? "Sim" : "Não");
				$("#editor").text(result.nomeEditor);
				$("#pacotePadrao").text(result.pacotePadrao);
				//TODO ???
				$("#imagemProduto").attr("src", "");
				
				$("#precoDesconto").text((parseFloat(result.precoCapa) - parseFloat(result.desconto)).toFixed(2));
				
				$("#observacaoReadOnly").text(result.observacao ? result.observacao : "");
				$("#observacao").val(result.observacao ? result.observacao : "");
				$("#observacaoReadOnly").show();
				$("#observacao").show();
				$("#btObs").show();
				
				ConferenciaEncalhe.popup_detalhe_publicacao();
			}
		);
	},
	
	excluirConferencia : function(idConferenciaEncalhe){
		
		ConferenciaEncalhe.modalAberta = true;
		
		$("#dialog-excluir-conferencia").dialog({
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
							
							$("#dialog-excluir-conferencia").dialog("close");
						}
					
					);
				},
				"Cancelar" : function() {
					
					$(this).dialog("close");
					$('#pesq_cota').focus();
				}
			}, close : function(){
				
				ConferenciaEncalhe.modalAberta = false;
			}
		});
	},
	
	popup_logado : function(){
		
		ConferenciaEncalhe.modalAberta = true;
		
		$("#dialog-logado").dialog({
			resizable : false,
			height : 180,
			width : 460,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					$.postJSON(contextPath + "/devolucao/conferenciaEncalhe/salvarIdBoxSessao", "idBox=" + $("#boxLogado").val(), 
						function(){
							
							$("#dialog-logado").dialog("close");
							$('#numeroCota').focus();
						}, null, true, "idModalBoxRecolhimento"
					);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
					$('#pesq_cota').focus();
				}
			}, open : function(){
				
				$("#boxLogado").focus();
			}, close : function(){
				
				ConferenciaEncalhe.modalAberta = false;
			}
		});
	},
	
	setarValoresPesquisados : function(result){
		
		if (ultimoCodeBar && ultimoCodeBar != result.codigoDeBarras){
		
			ConferenciaEncalhe.carregarListaConferencia(null);
			
		}
		
		ultimoCodeBar = result.codigoDeBarras;
		ultimoSM = result.codigoSM;
		ultimoCodigo = result.idProdutoEdicao;
		
		$("#cod_barras").val(result.codigoDeBarras);
		$("#sm").val(result.codigoSM);
		$("#codProduto").val(result.idProdutoEdicao);
		
		$("#nomeProduto").text(result.nomeProduto);
		$("#edicaoProduto").text(result.numeroEdicao);
		$("#precoCapa").text(result.precoCapa);
		$("#desconto").text(parseFloat(result.desconto).toFixed(2));
		
		$("#valorTotal").text(((parseFloat(result.precoCapa) - parseFloat(result.desconto)) * parseFloat(result.qtdExemplar)).toFixed(2));
		
		$("#qtdeExemplar").val(parseInt(result.qtdExemplar));
	},
	
	adicionarProdutoConferido : function(){
		
		var data = [{name: "idProdutoEdicao", value: $("#codProduto").val()}, 
		            {name: "quantidade", value: $("#qtdeExemplar").val()}];
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/adicionarProdutoConferido', data,
			function(result){
				
				ConferenciaEncalhe.preProcessarConsultaConferenciaEncalhe(result);
				
				ConferenciaEncalhe.limparDadosProduto();
				
				$("#cod_barras").focus();
			}
		);
	},
	
	limparDadosProduto : function(){
		
		$("#qtdeExemplar").val("");
		$("#cod_barras").val("");
		$("#sm").val("");
		$("#codProduto").val("");
		
		$("#nomeProduto").text("");
		$("#edicaoProduto").text("");
		$("#precoCapa").text("");
		$("#desconto").text("");
		$("#valorTotal").text("");
		
		ultimoCodeBar = "";
		ultimoSM = "";
		ultimoCodigo = "";
	},

	popup_alert : function() {
		
		ConferenciaEncalhe.modalAberta = true;
		
		$("#dialog-alert").dialog({
			resizable : false,
			height : 190,
			width : 460,
			modal : true,
			buttons : {
				"Sim" : function() {
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/carregarNotaFiscal', null, 
						function(result){
							
							$("#numNotaFiscal").val(result.numero);
							$("#serieNotaFiscal").val(result.serie);
							$("#dataNotaFiscal").val(result.dataEmissao);
						    $("#chaveAcessoNFE").val(result.chaveAcesso);
							
						    if (result.valorProdutos){
							    
						    	$("#valorNotaFiscal").val(parseFloat(result.valorProdutos).toFixed(2));
								
						    }
							
							$("#dialog-alert").dialog("close");
							ConferenciaEncalhe.popup_notaFiscal();
						}
					);
				},
				"Não" : function() {
					
					$("#dialog-alert").dialog("close");
					$("#vlrCE").focus();
				}
			}, open : function(){
				
				$(this).parent('div').find('button:contains("Sim")').focus();
			}, close : function(){
				
				ConferenciaEncalhe.modalAberta = false;
			}
		});
		
		$("#dialog-alert").show();
	},

	mostrarChaveAcesso : function() {
		
		if($('input:radio[name=radioNFE]:checked').val() == 'S') {
			
			$("#divForChaveAcessoNFE").show();
			
		} else {

			$("#divForChaveAcessoNFE").hide();
			
		}
		
	},
	
	popup_notaFiscal : function() {
		
		ConferenciaEncalhe.modalAberta = true;
		
		$("#dialog-notaFiscal").dialog({
			resizable : false,
			height : 360,
			width : 750,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					var data = []; 
					
					if($('input:radio[name=radioNFE]:checked').val() == 'S') {
						
						data = [
								{name : "notaFiscal.numero", value : $("#numNotaFiscal").val()},
								{name : "notaFiscal.serie", value : $("#serieNotaFiscal").val()},
								{name : "notaFiscal.dataEmissao", value : $("#dataNotaFiscal").val()},
								{name : "notaFiscal.valorProdutos", value : $("#valorNotaFiscal").val()},
								{name : "notaFiscal.chaveAcesso", value : $("#chaveAcessoNFE").val()}
						];
						
					} else {
						
						data = [
								{name : "notaFiscal.numero", value : $("#numNotaFiscal").val()},
								{name : "notaFiscal.serie", value : $("#serieNotaFiscal").val()},
								{name : "notaFiscal.dataEmissao", value : $("#dataNotaFiscal").val()},
								{name : "notaFiscal.valorProdutos", value : $("#valorNotaFiscal").val()}
						];
						
					}
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/salvarNotaFiscal', data, 
						function(result){
							
							$("#dialog-notaFiscal").dialog("close");
							
							$("#cod_barras").focus();
							
						}, null, true, "dialog-notaFiscal"
					);
				},
				"Cancelar" : function() {
					
					$(this).dialog("close");
					$("#vlrCE").focus();
				}
			}, open : function(){
				
				$("#numNotaFiscal").focus();
			}, close : function(){
				
				ConferenciaEncalhe.modalAberta = false;
			}
		});

	},

	popup_pesquisar : function() {
		
		ConferenciaEncalhe.modalAberta = true;
		
		$("#dialog-pesquisar").dialog({
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
			}
		});

	},

	popup_detalhe_publicacao : function() {
		
		ConferenciaEncalhe.modalAberta = true;
		
		$('#observacao').focus();
		
		$("#dialog-detalhe-publicacao").dialog({
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
			}
		});

	},
	
	gravaObs : function() {
		
		var data = [{name: "idConferenciaEncalhe", value: ConferenciaEncalhe.idConferenciaEncalheVisualizacao},
		            {name: "observacao", value: $("#observacao").val()}];
		
		$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/gravarObservacaoConferecnia', data, 
			function(result){
			
				$("#observacao").fadeOut();
				$(".obs").fadeIn("slow");
				$(".tit").fadeOut("slow");
				$("#btObs").fadeOut("slow");
				
				$("#observacaoReadOnly").text($("#observacao").val());
			}
		);
	},

	popup_outros_valores : function() {
		
		ConferenciaEncalhe.modalAberta = true;
		
		$("#dialog-outros-valores").dialog({
			resizable : false,
			height : 430,
			width : 460,
			modal : true,
			buttons : {
				"Fechar" : function() {
					
					$(this).dialog("close");
				}
			}, close : function(){
				
				ConferenciaEncalhe.modalAberta = false;
			}
		});

	},

	popup_salvarInfos : function() {
		
		ConferenciaEncalhe.modalAberta = true;
		
		$("#dialog-salvar").dialog({
			resizable : false,
			height : 190,
			width : 460,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/salvarConferencia', null,
						
						function(result){
						
							if(result.tipoMensagem == 'SUCCESS') {
								
								var data = [
								      {name : 'numeroCota', value : $("#numeroCota").val()}, 
								      {name: 'indObtemDadosFromBD', value : true},
								      {name: 'indConferenciaContingencia', value: false}
								];
								
								ConferenciaEncalhe.carregarListaConferencia(data);
								
							}
						
							exibirMensagem(result.tipoMensagem, result.listaMensagens);
							
							$("#dialog-salvar").dialog("close");
						},
						null, true, "idModalConfirmarSalvarConf"
					);
				},
				"Cancelar" : function() {
					
					$(this).dialog("close");
				}

			}, close : function(){
				
				ConferenciaEncalhe.modalAberta = false;
			}
		});

	},
	
	mostrar_produtos : function() {
		
		var codigoNomeProduto = $("#pesq_prod").val();
		
		if (codigoNomeProduto && codigoNomeProduto.length > 0){
			$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/pesquisarProdutoPorCodigoNome', 
					"codigoNomeProduto=" + codigoNomeProduto, 
					function(result){
						
						if (result[0]){
							
							$("#pesq_prod").autocomplete({
								source: result,
								select: function(event, ui){
									
									$("#codProduto").val(ui.item.chave.string);
									ConferenciaEncalhe.idProdutoEdicao = ui.item.chave.long;
								}
							});
						}
					}
			);
		}
	},

	fechar_produtos : function() {
		$('.pesqProdutosGrid').keypress(function(e) {
			if (e.keyCode == 13) {
				$(".itensPesquisados").show();
			}
		});
	},

	pesqMostraCota : function() {
		$('#pesq_cota').keypress(function(e) {
			if (e.keyCode == 13) {
				$('.dadosFiltro').fadeIn('fast');
				popup_alert();
			}
		});
	},

	outrosVlrsGridModel : [ {
		display : 'Data',
		name : 'dataLancamento',
		width : 100,
		sortable : true,
		align : 'left'
	}, {
		display : 'Tipo de Lançamento',
		name : 'tipoLancamento',
		width : 140,
		sortable : true,
		align : 'left'
	}, {
		display : 'Valor R$',
		name : 'valor',
		width : 100,
		sortable : true,
		align : 'right'
	} ]

};

var ultimoCodeBar = "";
var ultimoSM = "";
var ultimoCodigo = "";

$(function() {

	$('#qtdeExemplar').focus();

	$("#dataNotaFiscal").datepicker({
		showOn : "button",
		buttonImage : contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
		buttonImageOnly : true,
		dateFormat: "dd/mm/yy"
	});

	$(".outrosVlrsGrid").flexigrid({
		dataType : 'json',
		colModel : ConferenciaEncalhe.outrosVlrsGridModel,
		width : 400,
		height : 250,
		disableSelect: true
	});
	
	$("#numeroCota").numeric();
	
	$("#qtdeExemplar").numeric();
	
	$("#vlrCE").numeric();
	
	$("#dataNotaFiscal").mask("99/99/9999");
	
	$("#valorNotaFiscal").numeric();
	
	$("#sm").numeric();
	
	$("#numeroCota").keypress(function(e) {
		
		if(e.keyCode == 13) {
			
			ConferenciaEncalhe.pesquisarCota();
		}
	});
	
	$("#vlrCE").keypress(function(e) {
		
		if (e.keyCode == 13) {
			
			$("#cod_barras").focus();
		}
	});
	
	$('#cod_barras').keypress(function(e) {
		
		if (e.keyCode == 13) {
			
			if (ultimoCodeBar != "" && ultimoCodeBar == $("#cod_barras").val()){
				
				var qtd = $("#qtdeExemplar").val() == "" ? 0 : parseInt($("#qtdeExemplar").val());
				
				$("#qtdeExemplar").val(qtd + 1);
			} else {
				
				var data = [{name: "codigoBarra", value: $("#cod_barras").val()}, 
				            {name: "sm", value: ""}, 
				            {name: "idProdutoEdicao", value: ""},
				            {name: "codigoAnterior", value: ultimoCodigo},
				            {name: "quantidade", value: $("qtdeExemplar").val()}];
				
				$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/pesquisarProdutoEdicao', data,
					function(result){
					
						ConferenciaEncalhe.setarValoresPesquisados(result);
						$("#cod_barras").focus();
					}, null, false, "idTelaConferenciaEncalhe"
				);
			}
		}
	});
	
	$('#sm').keypress(function(e) {
		
		if (e.keyCode == 13) {
			
			if (ultimoSM != "" && ultimoSM == $("#sm").val()){
				
				var qtd = $("#qtdeExemplar").val() == "" ? 0 : parseInt($("#qtdeExemplar").val());
				
				$("#qtdeExemplar").val(qtd + 1);
			} else {
				
				var data = [{name: "codigoBarra", value: ""}, 
				            {name: "sm", value: $("#sm").val()}, 
				            {name: "idProdutoEdicao", value: ""},
				            {name: "codigoAnterior", value: ultimoCodigo},
				            {name: "quantidade", value: $("qtdeExemplar").val()}];
				
				$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/pesquisarProdutoEdicao', data,
					function(result){
					
						ConferenciaEncalhe.setarValoresPesquisados(result);
						$("#sm").focus();
					}, null, false, "idTelaConferenciaEncalhe"
				);
			}
		}
	});
	
	$("#pesq_prod").keyup(function (e){
		
		if (e.keyCode == 13) {
			
			if (ultimoCodigo != "" && ultimoCodigo == $("#pesq_prod").val()){
				
				var qtd = $("#qtdeExemplar").val() == "" ? 0 : parseInt($("#qtdeExemplar").val());
				
				$("#qtdeExemplar").val(qtd + 1);
				
				$("#dialog-pesquisar").dialog("close");
				$('#cod_barras').focus();
			} else {
				
				var data = [{name: "codigoBarra", value: ""}, 
				            {name: "sm", value: ""}, 
				            {name: "idProdutoEdicao", value: ConferenciaEncalhe.idProdutoEdicao},
				            {name: "codigoAnterior", value: ultimoCodigo},
				            {name: "quantidade", value: $("qtdeExemplar").val()}];
				
				$.postJSON(contextPath + '/devolucao/conferenciaEncalhe/pesquisarProdutoEdicao', data,
					function(result){
						
						ConferenciaEncalhe.setarValoresPesquisados(result);
						$("#dialog-pesquisar").dialog("close");
						$('#cod_barras').focus();
					},
					function(){
						
						$("#codProduto").val("");
					}, true, "idModalPesquisarProdutos"
				);
			}
		} else {
			
			if (e.keyCode != 38 && e.keyCode != 40){
				
				ConferenciaEncalhe.mostrar_produtos();
			}
		}
	});
	
	$('#codProduto').keypress(function(e) {
		
		$("#pesq_prod").val("");
		ConferenciaEncalhe.popup_pesquisar();
	});
	
	$('#observacao').keypress(function(e) {
		if (e.keyCode == 13) {
			ConferenciaEncalhe.gravaObs();
		}
	});
	
	ConferenciaEncalhe.popup_logado();
});

shortcut.add("F2", function() {
	
	if (!ConferenciaEncalhe.modalAberta){
	
		ConferenciaEncalhe.adicionarProdutoConferido();
	}
});

shortcut.add("F6", function() {
	
	if (!ConferenciaEncalhe.modalAberta){
		
		ConferenciaEncalhe.popup_notaFiscal();
	}
});

shortcut.add("F8", function() {
	
	if (!ConferenciaEncalhe.modalAberta){
		
		ConferenciaEncalhe.verificarValorTotalCE();
		
	}
});

shortcut.add("F9", function() {
	
	if (!ConferenciaEncalhe.modalAberta){
		
		ConferenciaEncalhe.popup_salvarInfos();
	}
});