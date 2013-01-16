var segmentoNaoRecebidoController = $.extend(true,	{

					init : function() {
						
						$(function() {
							var availableTags = [
								"1234 - Antonio Carlos Pereira",
								"2345 - Antonio Pereira",
								"3456 - Paulo Roberto",
								"1234 - Antonio Carlos Pereira",
								"2345 - Antonio Pereira",
								"3456 - Paulo Roberto",
								"5678 - Roberto Carlos"
							];
							$( "#lstCotas" ).autocomplete({
								source: availableTags
							});
						});
						
						$(function() {
							var availableTags = [
								"Erótico",
								"Aventura",
								"Atualidades",
								"Artesanato"		];
							$( "#lstSegmento" ).autocomplete({
								source: availableTags
							});
						});
						
						$(".segmentoCotaGrid").flexigrid({
							url : '../xml/segmentoCotaGrid-xml.xml',
							dataType : 'xml',
							colModel : [ {
								display : 'Segmento',
								name : 'segmento',
								width : 260,
								sortable : true,
								align : 'left'
							}, {
								display : 'Usuário',
								name : 'usuario',
								width : 100,
								sortable : true,
								align : 'left'
							}, {
								display : 'Data',
								name : 'data',
								width : 80,
								sortable : true,
								align : 'center'
							}, {
								display : 'Hora',
								name : 'hora',
								width : 80,
								sortable : true,
								align : 'center'
							},  {
								display : 'Ação',
								name : 'acao',
								width : 30,
								sortable : true,
								align : 'center'
							}],
							sortname : "cota",
							sortorder : "asc",
							usepager : true,
							useRp : true,
							rp : 15,
							showTableToggleBtn : true,
							width : 630,
							height : 250
						});
						
					$(".pesqBancasGrid").flexigrid({
							url : '../xml/pesqBancas-xml.xml',
							dataType : 'xml',
							colModel : [ {
								display : 'Cota',
								name : 'cota',
								width : 110,
								sortable : true,
								align : 'left'
							}, {
								display : 'Nome',
								name : 'Nome',
								width : 400,
								sortable : true,
								align : 'left'
							},  {
								display : '',
								name : 'sel',
								width : 30,
								sortable : true,
								align : 'center'
							}],
							sortname : "cota",
							sortorder : "asc",
							usepager : true,
							useRp : true,
							rp : 15,
							showTableToggleBtn : true,
							width : 600,
							height : 200
						});
						
						$(".segmentosBGrid").flexigrid({
							url : '../xml/segmentosB-xml.xml',
							dataType : 'xml',
							colModel : [ {
								display : 'Segmento',
								name : 'segmento',
								width : 225,
								sortable : true,
								align : 'left'
							},  {
								display : '',
								name : 'sel',
								width : 30,
								sortable : true,
								align : 'center'
							}],
							width : 300,
							height : 240
						});
						
						$(".segmentosGrid").flexigrid({
							url : '../xml/segmentos-xml.xml',
							dataType : 'xml',
							colModel : [ {
								display : 'Cota',
								name : 'cota',
								width : 50,
								sortable : true,
								align : 'left'
							}, {
								display : 'Nome',
								name : 'Nome',
								width : 160,
								sortable : true,
								align : 'left'
							},  {
								display : '',
								name : 'sel',
								width : 30,
								sortable : true,
								align : 'center'
							}],
							sortname : "cota",
							sortorder : "asc",
							width : 300,
							height : 235
						});
						
					$(".segmentoNaoRecebidaGrid").flexigrid({
						preProcess : segmentoNaoRecebidoController.executarPreProcessamento,
							dataType : 'json',
							colModel : [ {
								display : 'Cota',
								name : 'numeroCota',
								width : 60,
								sortable : true,
								align : 'left'
							}, {
								display : 'Status',
								name : 'statusCota',
								width : 60,
								sortable : true,
								align : 'left'
							}, {
								display : 'Nome',
								name : 'nomeCota',
								width : 130,
								sortable : true,
								align : 'left'
							}, {
								display : 'Usuário',
								name : 'nomeUsuario',
								width : 115,
								sortable : true,
								align : 'left'
							}, {
								display : 'Data',
								name : 'dataAlteracaoFormatada',
								width : 70,
								sortable : true,
								align : 'center'
							}, {
								display : 'Hora',
								name : 'horaAlteracaoFormatada',
								width : 60,
								sortable : true,
								align : 'center'
							},  {
								display : 'Ação',
								name : 'acao',
								width : 30,
								sortable : true,
								align : 'center'
							}],
							sortname : "cota",
							sortorder : "asc",
							usepager : true,
							useRp : true,
							rp : 15,
							showTableToggleBtn : true,
							width : 630,
							height : 250
						});
					},

					incluirSegmento : function () {
						//$( "#dialog:ui-dialog" ).dialog( "destroy" );

						$( "#dialog-novo" ).dialog({
							resizable: false,
							height:500,
							width:650,
							modal: true,
							buttons: {
								"Confirmar": function() {
									$( this ).dialog( "close" );
									$("#effect").show("highlight", {}, 1000, callback);
								},
								"Cancelar": function() {
									$( this ).dialog( "close" );
								}
							}
						});
					},

					excluirSegmento : function () {
						//$( "#dialog:ui-dialog" ).dialog( "destroy" );

						$( "#dialog-excluir" ).dialog({
							resizable: false,
							height:170,
							width:380,
							modal: true,
							buttons: {
								"Confirmar": function() {
									$( this ).dialog( "close" );
									$("#effect").show("highlight", {}, 1000, callback);
								},
								"Cancelar": function() {
									$( this ).dialog( "close" );
								}
							}
						});
					},

					porCota : function(){
						$('.porCota').show();
						$('.porSegmento').hide();
					},
					
					porSegmento : function(){
						$('.porCota').hide();
						$('.porSegmento').show();
						
						
						$(".segmentoNaoRecebidaGrid", segmentoNaoRecebidoController.workspace).flexOptions(
								{
									url : contextPath + "/distribuicao/segmentoNaoRecebido/pesquisarCotasNaoRecebemSegmento",
									dataType : 'json',
									params : segmentoNaoRecebidoController.getFiltroSegmento(),
								});

							$(".segmentoNaoRecebidaGrid", segmentoNaoRecebidoController.workspace).flexReload();

						
					},
					
					getFiltroSegmento : function() {

						var data = [];

						data.push({
							name : 'filtro.tipoSegmentoProdutoId',
							value : $('#tipoSegmentoProduto option:selected',
									segmentoNaoRecebidoController.workspace).val()
						});
						data.push({
							name : 'filtro.cotasAtivas',
							value : segmentoNaoRecebidoController.get("cotasAtivas")
						});

						return data;
					},
					
					filtroPorCota : function(){
						$('.filtroPorCota').show();
						$('.filtroPorSegmento').hide();
						$('.porSegmento').hide();
					},
					
					filtroPorSegmento : function (){
						$('.filtroPorCota').hide();
						$('.filtroPorSegmento').show();
						$('.porCota').hide();
					},
					
					
					
					
					executarPreProcessamento : function(resultado) {

						if (resultado.mensagens) {

							exibirMensagem(resultado.mensagens.tipoMensagem,
									resultado.mensagens.listaMensagens);

							$(".grids", segmentoNaoRecebidoController.workspace).hide();

							return resultado;
						}

						$(".grids", segmentoNaoRecebidoController.workspace).show();

						return resultado;

					},

					get : function(campo) {

						var elemento = $("#" + campo, segmentoNaoRecebidoController.workspace);

						if (elemento.attr('type') == 'checkbox') {
							return (elemento.attr('checked') == 'checked');
						} 
						else if (elemento.attr('type') == 'radio') {
							return (elemento.attr('checked') == 'checked');
						}
						else {
							return elemento.val();
						}

					},

				}, BaseController);
 //@ sourceURL=segmentoNaoRecebido.js
