var ConsultaEncalhe = $
		.extend(
				true,
				{

					init : function() {

						var colunas = ConsultaEncalhe.obterColModel();
						var colunasDetalhes = ConsultaEncalhe
								.obterColModelDetalhes();
						var colunasReparte = ConsultaEncalhe
						.obterColModelDetalhesReparte();
						var colunasOutrosValores = ConsultaEncalhe
								.obterColModelOutrosValores();

						 $("#consulta-encalhe-selectRota").attr('disabled', 'disabled');
		        		 $("#consulta-encalhe-selectRoteiro").attr('disabled', 'disabled');   
		        		 $("#consulta-encalhe-selectRota").val();
		        		 $("#consulta-encalhe-selectRoteiro").val();
		        		 
						$("#consulta-encalhe-cota", ConsultaEncalhe.workspace)
								.numeric();

						$("#gridConsultaEncalhe", ConsultaEncalhe.workspace)
								.flexigrid(
										{

											dataType : 'json',
											preProcess : ConsultaEncalhe.executarPreProcessamento,
											colModel : colunas,
											sortname : "dataDoRecolhimentoDistribuidor",
											sortorder : "asc",
											usepager : true,
											useRp : true,
											rp : 15,
											showTableToggleBtn : true,
											width : 960,
											height : 180
										});

						$("#dadosDetalheEncalheGrid", ConsultaEncalhe.workspace)
								.flexigrid(
										{

											dataType : 'json',
											preProcess : ConsultaEncalhe.executarPreProcessamentoDetalhe,
											colModel : colunasDetalhes,
											sortname : "cota",
											sortorder : "asc",
											usepager : true,
											useRp : true,
											rp : 15,
											showTableToggleBtn : true,
											width : 600,
											height : 180
										});
						
						$("#dadosDetalheReparteGrid", ConsultaEncalhe.workspace)
						.flexigrid(
								{

									dataType : 'json',
									preProcess : ConsultaEncalhe.executarPreProcessamentoDetalhe,
									colModel : colunasReparte,
									sortname : "numeroCota",
									sortorder : "asc",
									usepager : true,
									useRp : true,
									rp : 15,
									showTableToggleBtn : true,
									width : 600,
									height : 180
								});

						$("#outrosValoresGrid", ConsultaEncalhe.workspace)
								.flexigrid({
									dataType : 'json',
									colModel : colunasOutrosValores,
									width : 540,
									height : 250,
									disableSelect : true
								});

						$('.datePicker', ConsultaEncalhe.workspace)
								.datepicker(
										{
											showOn : "button",
											buttonImage : contextPath
													+ "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
											buttonImageOnly : true,
											dateFormat : "dd/mm/yy"
										});

						$('.datePicker', ConsultaEncalhe.workspace).mask(
								"99/99/9999");
						
						
						
						$('#linkConsultaEncalhePDF', ConsultaEncalhe.workspace).click(function() {
							var params = {"fileType": 'PDF'};
							
							$.fileDownload(contextPath + '/devolucao/consultaEncalhe/exportar', {
								httpMethod : "GET",
								data : params,
								failCallback: function (result) {
									
									result = $.parseJSON($(result).text());

									if((typeof result != "undefined") && result.mensagens) {
										
										result = result.mensagens;
										var tipoMensagem = result.tipoMensagem;
										var listaMensagens = result.listaMensagens;
										
										if (tipoMensagem && listaMensagens) {
											exibirMensagemDialog(tipoMensagem, listaMensagens, "");
										}
									}
							    }
							});
						});
						
						$('#linkConsultaEncalheXLS', ConsultaEncalhe.workspace).click(function() {
							var params = {"fileType": 'XLS'};
							
							$.fileDownload(contextPath + '/devolucao/consultaEncalhe/exportar', {
								httpMethod : "GET",
								data : params,
								failCallback: function (result) {
									
									result = $.parseJSON($(result).text());

									if((typeof result != "undefined") && result.mensagens) {
										
										result = result.mensagens;
										var tipoMensagem = result.tipoMensagem;
										var listaMensagens = result.listaMensagens;
										
										if (tipoMensagem && listaMensagens) {
											exibirMensagemDialog(tipoMensagem, listaMensagens, "");
										}
									}
							    }
							});
						});
						
						
						$('#linkConsultaEncalheDetalheXLS', ConsultaEncalhe.workspace).click(function() {
							var params = {"fileType": 'XLS'};
							
							$.fileDownload(contextPath + '/devolucao/consultaEncalhe/exportarDetalhe', {
								httpMethod : "GET",
								data : params,
								failCallback: function (result) {
									
									result = $.parseJSON($(result).text());

									if((typeof result != "undefined") && result.mensagens) {
										
										result = result.mensagens;
										var tipoMensagem = result.tipoMensagem;
										var listaMensagens = result.listaMensagens;
										
										if (tipoMensagem && listaMensagens) {
											exibirMensagemDialog(tipoMensagem, listaMensagens, "");
										}
									}
							    }
							});
						});
						
						$('#linkConsultaEncalheDetalhePDF', ConsultaEncalhe.workspace).click(function() {
							var params = {"fileType": 'PDF'};
							
							$.fileDownload(contextPath + '/devolucao/consultaEncalhe/exportarDetalhe', {
								httpMethod : "GET",
								data : params,
								failCallback: function (result) {
									
									result = $.parseJSON($(result).text());

									if((typeof result != "undefined") && result.mensagens) {
										
										result = result.mensagens;
										var tipoMensagem = result.tipoMensagem;
										var listaMensagens = result.listaMensagens;
										
										if (tipoMensagem && listaMensagens) {
											exibirMensagemDialog(tipoMensagem, listaMensagens, "");
										}
									}
							    }
							});
						});
						
						$('#consultaEncalhe-btnEnviarEmail', ConsultaEncalhe.workspace).click(function() {

							var dataRecolhimentoInicial = $("#dataRecolhimentoInicial", ConsultaEncalhe.workspace).val();
							var dataRecolhimentoFinal = $("#dataRecolhimentoFinal", ConsultaEncalhe.workspace).val();
							var idFornecedor = $("#idFornecedor", ConsultaEncalhe.workspace).val();
							var numeroCota = $("#consulta-encalhe-cota", ConsultaEncalhe.workspace).val();
							var idBoxEncalhe = $("#consulta-encalhe-boxEncalhe", ConsultaEncalhe.workspace).val();
							var idBox = $("#consulta-encalhe-box", ConsultaEncalhe.workspace).val();
							var idRota = $("#consulta-encalhe-selectRota", ConsultaEncalhe.workspace).val();
							var idRoteiro = $("#consulta-encalhe-selectRoteiro", ConsultaEncalhe.workspace).val();
							
							var params = [];
							
							params.push({'name' : "dataRecolhimentoInicial", 'value' : dataRecolhimentoInicial});
							params.push({'name' : "dataRecolhimentoFinal", 'value' : dataRecolhimentoFinal});
							params.push({'name' : "idFornecedor", 'value' : idFornecedor});
							params.push({'name' : "numeroCota", 'value' : numeroCota});
							params.push({'name' : "idBoxEncalhe", 'value' : idBoxEncalhe});
							params.push({'name' : "idBox", 'value' : idBox});
							params.push({'name' : "idRota", 'value' : idRota});
							params.push({'name' : "idRoteiro", 'value' : idRoteiro});
							
							this.confirmDialog = new ConfirmDialog('Confirmar Envio de email?', function() {
								
								$.postJSON(contextPath + '/devolucao/consultaEncalhe/enviarEmail', params), 
									function(data){
										var tipoMensagem = data.tipoMensagem;
										var listaMensagens = data.listaMensagens;
										
										if (tipoMensagem && listaMensagens) {
											exibirMensagemDialog(tipoMensagem, listaMensagens, "");
										}
									};
								
								return true;
							}, function() {
								
							});
							this.confirmDialog.open();						
									
						});
						
					},

					pesquisar : function() {

						var dataRecolhimentoInicial = $("#dataRecolhimentoInicial", ConsultaEncalhe.workspace).val();
						var dataRecolhimentoFinal = $("#dataRecolhimentoFinal", ConsultaEncalhe.workspace).val();
						var idFornecedor = $("#idFornecedor", ConsultaEncalhe.workspace).val();
						var numeroCota = $("#consulta-encalhe-cota", ConsultaEncalhe.workspace).val();
						var codigoProduto = $("#consulta-encalhe-codigoProduto", ConsultaEncalhe.workspace).val();
						var idBoxEncalhe = $("#consulta-encalhe-boxEncalhe", ConsultaEncalhe.workspace).val();
						var idBox = $("#consulta-encalhe-box", ConsultaEncalhe.workspace).val();
						var numeroEdicao = $("#consulta-encalhe-edicao", ConsultaEncalhe.workspace).val();
						var idRota = $("#consulta-encalhe-selectRota", ConsultaEncalhe.workspace).val();
						var idRoteiro = $("#consulta-encalhe-selectRoteiro", ConsultaEncalhe.workspace).val();
						
						var formData = [

						{
							name : 'dataRecolhimentoInicial',
							value : dataRecolhimentoInicial
						}, {
							name : 'dataRecolhimentoFinal',
							value : dataRecolhimentoFinal
						}, {
							name : 'idFornecedor',
							value : idFornecedor
						}, {
							name : 'numeroCota',
							value : numeroCota
						}, {
							name : 'codigoProduto',
							value : codigoProduto
						}, {
							name : 'idBoxEncalhe',
							value : idBoxEncalhe
						}, {
							name : 'idBox',
							value : idBox	
						}, {
							name : 'numeroEdicao',
							value : numeroEdicao
						}, {
							name : 'idRota',
							value : idRota
						}, {
							name : 'idRoteiro',
							value : idRoteiro
						}];

						$("#gridConsultaEncalhe", ConsultaEncalhe.workspace)
								.flexOptions(
										{
											url : contextPath
													+ "/devolucao/consultaEncalhe/pesquisar",
											params : formData
										});

						$.each($("#gridConsultaEncalhe",
								ConsultaEncalhe.workspace), function(index,
								value) {
							if (value && value.p) {
								value.p.page = 1;
								value.p.newp = 1;
							}
						});
						$("#gridConsultaEncalhe", ConsultaEncalhe.workspace)
								.flexReload();

					},

					gerarSlip : function() {
						
						$.getJSON(contextPath + '/devolucao/consultaEncalhe/isDistribImprimeSlip', 
					 			null,
					 			function(result) {
					 				
					 				if (result) {
					 		
					 					this.confirmDialog = new ConfirmDialog('Confirmar Geracao do Slip ?', function() {
					 						
					 						var dataRecolhimentoInicial = $("#dataRecolhimentoInicial", ConsultaEncalhe.workspace).val();
					 						var dataRecolhimentoFinal = $("#dataRecolhimentoFinal", ConsultaEncalhe.workspace).val();
					 						var idFornecedor = $("#idFornecedor", ConsultaEncalhe.workspace).val();
					 						var numeroCota = $("#consulta-encalhe-cota", ConsultaEncalhe.workspace).val();
					 						var idBoxEncalhe = $("#consulta-encalhe-boxEncalhe", ConsultaEncalhe.workspace).val();
					 						var idBox = $("#consulta-encalhe-box", ConsultaEncalhe.workspace).val();
					 						var idRota = $("#consulta-encalhe-selectRota", ConsultaEncalhe.workspace).val();
					 						var idRoteiro = $("#consulta-encalhe-selectRoteiro", ConsultaEncalhe.workspace).val();
					 						
					 						var link = contextPath
					 						+ '/devolucao/consultaEncalhe/gerarSlip'
					 						+ '?dataRecolhimentoInicial='+ dataRecolhimentoInicial
					 						+ '&dataRecolhimentoFinal='+ dataRecolhimentoFinal 
					 						+ '&idFornecedor='+ idFornecedor 
					 						+ '&numeroCota='+ numeroCota 
					 						+ '&idBoxEncalhe=' + idBoxEncalhe
					 						+ '&idBox=' + idBox
					 						+ '&idRota=' + idRota
					 						+ '&idRoteiro=' + idRoteiro;
					 						
					 						$("#download-iframe", ConsultaEncalhe.workspace).attr('src', link);
					 						return true;
					 					},function() {
					 						
					 					});
					 					this.confirmDialog.open();
					 				} else {
					 					exibirMensagem("WARNING", ["Slip's não podem ser impressos, distribuidor não aceita a impressão destes documentos."]);
					 				}
					 			}
					 		);	
					},
					
					/**
					 * Recarregar combos por Box
					 */
			        changeBox : function(){
						
			        	var boxDe = $("#consulta-encalhe-box").val();
			        	
			        	if ( boxDe == undefined || boxDe.length == 0 ) {
			        		 $("#consulta-encalhe-selectRota").attr('disabled', 'disabled');
			        		 $("#consulta-encalhe-selectRota").val();
			        		 $("#consulta-encalhe-selectRoteiro").attr('disabled', 'disabled');  
			        		 $("#consulta-encalhe-selectRoteiro").val();
			        		 return;
			        	} else {
			        		$("#consulta-encalhe-selectRota").removeAttr('disabled');
			        	}
			        	
			        	var boxAte = $("#consulta-encalhe-box").val();
			        	
			        	var idRota = $("#consulta-encalhe-selectRota").val();
			        	
			        	var idRoteiro = $("#consulta-encalhe-selectRoteiro").val();
			        	
			        	var params = [{
							            name : "codigoBoxDe",
							            value : boxDe	
									  },{
										name : "codigoBoxAte",
										value : boxAte
									  }];
			        	
			        	$.postJSON(contextPath + '/cadastro/roteirizacao/carregarCombosPorBox', params, 
							function(result) {
			        		
			        		    var listaRota = result[0];
			        		    
			        		    var listaRoteiro = result[1];
			        		    
			        		    var listaBox = result[2];
			        		
			        		    ConsultaEncalhe.recarregarCombo($("#consulta-encalhe-selectRota", ConsultaEncalhe.workspace), listaRota ,idRota);
			     		    
			        		    ConsultaEncalhe.recarregarCombo($("#consulta-encalhe-selectRoteiro", ConsultaEncalhe.workspace), listaRoteiro ,idRoteiro); 
			        		    
			        		    ConsultaEncalhe.recarregarCombo($("#consulta-encalhe-box", ConsultaEncalhe.workspace), listaBox ,boxDe);
			         		    
			        	    }    
						);
					},
					
					/**
					 * Recarregar combos por Rota
					 */
			        changeRota : function(){
			        	
			            var boxDe = $("#consulta-encalhe-box").val();
			        	
			        	var idRota = $("#consulta-encalhe-selectRota").val();
			        	if ( idRota == undefined || idRota.length == 0 ) {	
			        		 $("#consulta-encalhe-selectRoteiro").attr('disabled', 'disabled');     	
			        		 $("#consulta-encalhe-selectRoteiro").val();
			        		 return;
			        	} else {
			        		 $("#consulta-encalhe-selectRoteiro").removeAttr('disabled');	
			        	}
			        	
			        	var idRoteiro = $("#consulta-encalhe-selectRoteiro").val();
			        	
			        	var params = [{
							            name : "idRota",
							            value : idRota	
									  }];
					    
			        	$.postJSON(contextPath + '/cadastro/roteirizacao/carregarCombosPorRota', params, 
							function(result) {
			        		
			        		    var listaRoteiro = result[0];
			        		 
			        		    var listaBox = result[1];
			        		    
			        		    var listaRota = result[2];

			        		    ConsultaEncalhe.recarregarCombo($("#consulta-encalhe-box", ConsultaEncalhe.workspace), listaBox ,boxDe);
			     		    
			        		    ConsultaEncalhe.recarregarCombo($("#consulta-encalhe-selectRoteiro", ConsultaEncalhe.workspace), listaRoteiro ,idRoteiro); 
			        		    
			        		    ConsultaEncalhe.recarregarCombo($("#consulta-encalhe-selectRota", ConsultaEncalhe.workspace), listaRota ,idRota);
			        	    }    
						);
					},
					
					/**
					 * Recarregar combos por Roteiro
					 */
			        changeRoteiro : function(){
			        	
			            var boxDe = $("#consulta-encalhe-box").val();
			        	
			        	var idRota = $("#consulta-encalhe-selectRota").val();
			        	
			        	var idRoteiro = $("#consulta-encalhe-selectRoteiro").val();
			         	
			         	var params = [{
			 				            name : "idRoteiro",
			 				            value : idRoteiro	
			 						  }];
			         	
			         	$.postJSON(contextPath + '/cadastro/roteirizacao/carregarCombosPorRoteiro', params, 
							function(result) {
			        		
			        		    var listaRota = result[0];
			        		 
			        		    var listaBox = result[1];
			        		    
			        		    var listaRoteiro = result[2];
			     		    
			        		    ConsultaEncalhe.recarregarCombo($("#consulta-encalhe-selectRota", ConsultaEncalhe.workspace), listaRota ,idRota);  
			        		    
			        		    ConsultaEncalhe.recarregarCombo($("#consulta-encalhe-box", ConsultaEncalhe.workspace), listaBox ,boxDe);
			         		    
			        		    ConsultaEncalhe.recarregarCombo($("#consulta-encalhe-selectRoteiro", ConsultaEncalhe.workspace), listaRoteiro ,idRoteiro); 
			        	    }    
						);
					},
					
					/**
					 * Recarregar combo
					 */
					recarregarCombo : function (comboNameComponent, content, valSelected){
						
						comboNameComponent.empty();

						comboNameComponent.append(new Option('Selecione...', '', true, true));
						
					    $.each(content, function(index, row) {
						    	
					    	comboNameComponent.append(new Option(row.value.$, row.key.$, true, true));
						});

					    if (valSelected) {
					    	
					        $(comboNameComponent).val(valSelected);
					    } else {
					    	
					        $(comboNameComponent).val('');
					    }
					},
					
					popupOutrosValores : function() {

						$("#dialog-outros-valores", ConsultaEncalhe.workspace)
								.dialog(
										{
											resizable : false,
											height : 430,
											width : 600,
											modal : true,
											buttons : [ {
												id : "bt_fechar",
												text : "Fechar",
												click : function() {
													$(this).dialog("close");
												}
											} ],
											form : $("#dialog-outros-valores",
													ConsultaEncalhe.workspace)
													.parents("form")
										});

					},

					executarPreProcessamento : function(resultado) {

						// Verifica mensagens de erro do retorno da chamada ao
						// controller.
						if (resultado.mensagens) {

							exibirMensagem(resultado.mensagens.tipoMensagem,
									resultado.mensagens.listaMensagens);

							$(".grids", ConsultaEncalhe.workspace).hide();

							return resultado.tableModel;
						}

						$
								.each(
										resultado.tableModel.rows,
										function(index, row) {
											var detalhes = '<a href="javascript:;" '
													+ (!row.cell.indPossuiObservacaoConferenciaEncalhe ? 'style="opacity: 0.5;"'
															: 'onclick="ConsultaEncalhe.popupDetalhe(\''
																	+ row.cell.idCota
																	+ '\', \''
																	+ row.cell.idFornecedor
																	+ '\', \''
																	+ row.cell.idProdutoEdicao
																	+ '\', null, \''
																	+ row.cell.recolhimento
																	+ '\');" style="cursor:pointer"')
													+ '>'
													+ '<img title="Detalhes do Encalhe" src="'
													+ contextPath
													+ '/images/ico_detalhes.png" hspace="5" border="0px" />'
													+ '</a>';
											detalhes += '  <a href="javascript:;" '
													+ ('onclick="ConsultaEncalhe.popupDetalheReparte(\''
															+ row.cell.idCota
															+ '\', \''
															+ row.cell.idFornecedor
															+ '\', \''
															+ row.cell.idProdutoEdicao
															+ '\', null, \''
															+ row.cell.recolhimento + '\',\'' +
															 row.cell.codigoProduto + '\',\''+
															 row.cell.nomeProduto + '\',\''+
															 row.cell.numeroEdicao + '\',\''+
															 row.cell.reparte + '\',\''+
															 row.cell.encalhe + '\''
															
													+ ');" style="cursor:pointer"')
													+ '>'
													+ '<img title="Detalhes do Produto" src="'
													+ contextPath
													+ '/images/ico_negociar.png" hspace="5" border="0px" />'
													+ '</a>';

											row.cell.acao = detalhes;

										});

						$("#outrosValoresGrid", ConsultaEncalhe.workspace)
								.flexAddData(
										{
											page : resultado.tableModelDebitoCredito.page,
											total : resultado.tableModelDebitoCredito.total,
											rows : resultado.tableModelDebitoCredito.rows
										});

						$(".grids", ConsultaEncalhe.workspace).show();

						$("#consulta-encalhe-valorVendaDia",
								ConsultaEncalhe.workspace)
								.text(
										floatToPrice(parseFloat(
												resultado.valorVendaDia)
												.toFixed(2)));
						$("#totalOutrosValores", ConsultaEncalhe.workspace)
								.text(
										floatToPrice(parseFloat(
												resultado.valorDebitoCredito)
												.toFixed(2)));
						$("#consulta-encalhe-valorAPagar",
								ConsultaEncalhe.workspace).text(
								floatToPrice(parseFloat(resultado.valorPagar)
										.toFixed(2)));

						if (resultado.valorReparte) {

							$("#totalReparte", ConsultaEncalhe.workspace)
									.text(
											floatToPrice(parseFloat(
													resultado.valorReparte)
													.toFixed(2)));
						}

						if (resultado.valorEncalhe) {

							$("#totalEncalhe", ConsultaEncalhe.workspace)
									.text(
											floatToPrice(parseFloat(
													resultado.valorEncalhe)
													.toFixed(2)));
						}

						return resultado.tableModel;

					},

					// Pesquisa por código de produto
					pesquisarPorCodigoProduto : function(idCodigo, idProduto,
							idGeracao, isFromModal) {

						var codigoProduto = $(idCodigo,
								ConsultaEncalhe.workspace).attr("value");

						if (codigoProduto != "") {

							codigoProduto = $.trim(codigoProduto);

							$(idCodigo, ConsultaEncalhe.workspace).val(
									codigoProduto);

							$(idProduto, ConsultaEncalhe.workspace).val("");
							$(idGeracao, ConsultaEncalhe.workspace).val(-1);

							if (codigoProduto && codigoProduto.length > 0) {

								$.postJSON(contextPath
										+ "/produto/pesquisarPorCodigoProduto",
										{
											codigoProduto : codigoProduto
										}, function(result) {
											ConsultaEncalhe.successCallBack(
													result, idProduto,
													idGeracao);
										}, function() {
											ConsultaEncalhe
													.errorCallBack(idCodigo);
										}, isFromModal);

							}

						} else {
							ConsultaEncalhe.limparCamposFiltro();
						}
					},

					errorCallBack : function(idCodigo) {
						ConsultaEncalhe.limparCamposFiltro();
						$(idCodigo, ConsultaEncalhe.workspace).focus();
					},

					limparCamposFiltro : function() {
						$("#consulta-encalhe-produto",
								ConsultaEncalhe.workspace).val("");
						$("#consulta-encalhe-codigoProduto",
								ConsultaEncalhe.workspace).val("");
					},

					successCallBack : function(result, idProduto, idGeracao,
							idCodigo, isFromModal) {
						$(idProduto, ConsultaEncalhe.workspace)
								.val(result.nome);
						$(idCodigo, ConsultaEncalhe.workspace).val(result.id);
					},

					executarPreProcessamentoDetalhe : function(resultado) {

						// Verifica mensagens de erro do retorno da chamada ao
						// controller.
						if (resultado.mensagens) {

							$("#dialog-detalhes-encalhe",
									ConsultaEncalhe.workspace).dialog("close");
							exibirMensagem(resultado.mensagens.tipoMensagem,
									resultado.mensagens.listaMensagens);

							return resultado.tableModel;
						}

						$("#dataOperacao", ConsultaEncalhe.workspace).html(
								resultado.dataOperacao);
						$("#codigoProduto", ConsultaEncalhe.workspace).html(
								resultado.codigoProduto);
						$("#nomeProduto", ConsultaEncalhe.workspace).html(
								resultado.nomeProduto);
						$("#edicaoProduto", ConsultaEncalhe.workspace).html(
								resultado.numeroEdicao);

						$(".grids", ConsultaEncalhe.workspace).show();

						return resultado.tableModel;

					},

					popupDetalhe : function(idCota, idFornecedor,
							idProdutoEdicao, dataMovimento, dataRecolhimento) {

						
					
						ConsultaEncalhe.obterDetalhesEncalhe(idCota,
								idFornecedor, idProdutoEdicao, dataMovimento,
								dataRecolhimento);

						$("#dialog-detalhes-encalhe", ConsultaEncalhe.workspace)
								.dialog(
										{
											resizable : false,
											height : 450,
											width : 650,
											modal : true,
											buttons : [ {
												id : "bt_fechar",
												text : "Fechar",
												click : function() {

													$(this).dialog("close");
												}
											} ],
											form : $(
													"#dialog-detalhes-encalhe",
													ConsultaEncalhe.workspace)
													.parents("form")
										});
					},
					
					popupDetalheReparte : function(idCota, idFornecedor,
							idProdutoEdicao, dataMovimento, dataRecolhimento,codigoProduto,nomeProduto,edicaoProduto,reparte,encalhe) {
                       
						$("#codigoProdutoreparte", ConsultaEncalhe.workspace).html(
								codigoProduto);
						$("#nomeProdutoreparte", ConsultaEncalhe.workspace).html(
								nomeProduto);
						$("#edicaoProdutoreparte", ConsultaEncalhe.workspace).html(
								edicaoProduto);
						$("#repartereparte", ConsultaEncalhe.workspace).html(
								reparte);
						$("#encalhereparte", ConsultaEncalhe.workspace).html(
								encalhe);
						ConsultaEncalhe.obterDetalhesReparte(idCota,
								idFornecedor, idProdutoEdicao, dataMovimento,
								dataRecolhimento);

						$("#dialog-detalhes-reparte", ConsultaEncalhe.workspace)
								.dialog(
										{
											resizable : false,
											height : 450,
											width : 650,
											modal : true,
											buttons : [ {
												id : "bt_fechar",
												text : "Fechar",
												click : function() {

													$(this).dialog("close");
												}
											} ],
											form : $(
													"#dialog-detalhes-reparte",
													ConsultaEncalhe.workspace)
													.parents("form")
										});
					},

					// POPULA GRADE DE DETALHES DA ENCALHE
					obterDetalhesEncalhe : function(numeroCota, idFornecedor,
							idProdutoEdicao, dataMovimento, dataRecolhimento) {

						if (!numeroCota) {

							numeroCota = $("#consulta-encalhe-cota",
									ConsultaEncalhe.workspace).val();
						}

						$("#dadosDetalheEncalheGrid", ConsultaEncalhe.workspace)
								.flexOptions(
										{
											url : contextPath
													+ "/devolucao/consultaEncalhe/pesquisarDetalhe",
											params : [ {
												name : 'idProdutoEdicao',
												value : idProdutoEdicao
											}, {
												name : 'idFornecedor',
												value : idFornecedor
											}, {
												name : 'numeroCota',
												value : numeroCota
											}, {
												name : 'dataRecolhimento',
												value : dataRecolhimento
											}, {
												name : 'dataMovimento',
												value : dataMovimento
											} ],
											newp : 1
										});
						$("#dadosDetalheEncalheGrid", ConsultaEncalhe.workspace)
								.flexReload();
						$(".grids", ConsultaEncalhe.workspace).show();
					},
					
					
					
					// POPULA GRADE DE DETALHES DA ENCALHE POR PRODUTO
					obterDetalhesReparte : function(numeroCota, idFornecedor,
							idProdutoEdicao, dataMovimento, dataRecolhimento) {

						if (!numeroCota) {

							numeroCota = $("#consulta-encalhe-cota",
									ConsultaEncalhe.workspace).val();
						}
						
						$("#dadosDetalheReparteGrid", ConsultaEncalhe.workspace).clear();
						$("#dadosDetalheReparteGrid", ConsultaEncalhe.workspace)
								.flexOptions(
										{
											url : contextPath
													+ "/devolucao/consultaEncalhe/pesquisarDetalheReparte",
											params : [ {
												name : 'idProdutoEdicao',
												value : idProdutoEdicao
											}, {
												name : 'idFornecedor',
												value : idFornecedor
											}, {
												name : 'numeroCota',
												value : numeroCota
											}, {
												name : 'dataRecolhimento',
												value : dataRecolhimento
											}, {
												name : 'dataMovimento',
												value : dataMovimento
											} ],
											newp : 1
										});
						$("#dadosDetalheReparteGrid", ConsultaEncalhe.workspace)
								.flexReload();
						$(".grids", ConsultaEncalhe.workspace).show();
					},

					obterColModel : function() {

						var colModel = [ {
							display : 'Código',
							name : 'codigoProduto',
							width : 50,
							sortable : true,
							align : 'left'
						}, {
							display : 'Produto',
							name : 'nomeProduto',
							width : 80,
							sortable : true,
							align : 'left'
						}, {
							display : 'Edição',
							name : 'numeroEdicao',
							width : 40,
							sortable : true,
							align : 'center'
						}, {
							display : 'Preço Capa R$',
							name : 'precoVenda',
							width : 80,
							sortable : true,
							align : 'right'
						}, {
							display : 'Preço com Desc. R$',
							name : 'precoComDesconto',
							width : 110,
							sortable : true,
							align : 'right'
						}, {
							display : 'Reparte',
							name : 'reparte',
							width : 50,
							sortable : true,
							align : 'center'
						}, {
							display : 'Encalhe',
							name : 'encalhe',
							width : 50,
							sortable : true,
							align : 'center'
						}, {
							display : 'Fornecedor',
							name : 'fornecedor',
							width : 85,
							sortable : true,
							align : 'left'
						}, {
							display : 'Valor R$',
							name : 'valor',
							width : 60,
							sortable : true,
							align : 'right'
						}, {
							display : 'Valor c/ Desc.',
							name : 'valorComDesconto',
							width : 70,
							sortable : true,
							align : 'right'
						}, {
							display : 'Recolhimento',
							name : 'recolhimento',
							width : 70,
							sortable : false,
							align : 'center'
						}, {
							display : 'Detalhes',
							name : 'acao',
							width : 50,
							sortable : false,
							align : 'center',
						} ];

						return colModel;
					},

					obterColModelDetalhes : function() {

						var colModel = [ {
							display : 'Cota',
							name : 'numeroCota',
							width : 60,
							sortable : true,
							align : 'left'
						}, {
							display : 'Nome',
							name : 'nomeCota',
							width : 150,
							sortable : true,
							align : 'left'
						}, {
							display : 'Observação',
							name : 'observacao',
							width : 330,
							sortable : true,
							align : 'left'
						} ];

						return colModel;
					},
					
					obterColModelDetalhesReparte : function() {

						var colModel = [ {
							display : 'Cota',
							name : 'numeroCota',
							width : 40,
							sortable : true,
							align : 'left'
						}, {
							display : 'Nome Cota',
							name : 'nomeCota',
							width : 200,
							sortable : true,
							align : 'left'
						},  {
							display : 'Box',
							name : 'nomeBox',
							width : 150,
							sortable : true,
							align : 'left'
						},{
							display : 'Reparte',
							name : 'reparte',
							width : 50,
							sortable : true,
							align : 'left'
						},{
							display : 'Encalhe',
							name : 'encalhe',
							width : 50,
							sortable : true,
							align : 'left'
						}  ];

						return colModel;
					},

					obterColModelOutrosValores : function() {

						var colModel = [ {
							display : 'Data',
							name : 'dataLancamento',
							width : 60,
							sortable : true,
							align : 'left'
						}, {
							display : 'Tipo de Lançamento',
							name : 'tipoLancamento',
							width : 130,
							sortable : true,
							align : 'left'
						}, {
							display : 'Observações',
							name : 'observacoes',
							width : 190,
							sortable : true,
							align : 'left',
							colResize : true
						}, {
							display : 'Valor R$',
							name : 'valor',
							width : 100,
							sortable : true,
							align : 'right'
						} ];

						return colModel;
					}

				}, BaseController);
//@ sourceURL=consultaEncalhe.js
