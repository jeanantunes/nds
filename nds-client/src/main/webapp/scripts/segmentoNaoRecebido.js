var segmentoNaoRecebidoController = $.extend(true,	{

					init : function() {
						
						// URLs utilizadas nas requisições
						segmentoNaoRecebidoController.urlExcluirSegmentoNaoRecebido = contextPath + "/distribuicao/segmentoNaoRecebido/excluirSegmentoNaoRecebido";
						segmentoNaoRecebidoController.urlPesquisarCotasNaoRecebemSegmento = contextPath + "/distribuicao/segmentoNaoRecebido/pesquisarCotasNaoRecebemSegmento";
						segmentoNaoRecebidoController.urlPesquisarCotasNaoEstaoNoSegmento = contextPath + "/distribuicao/segmentoNaoRecebido/pesquisarCotasNaoEstaoNoSegmento";
						segmentoNaoRecebidoController.urlIncluirCotasSegmentoNaoRecebido = contextPath + "/distribuicao/segmentoNaoRecebido/incluirCotasSegmentoNaoRecebido";
						
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
						
						// GRID COTA PARA SELEÇÃO E INCLUSÃO NO SEGMENTO NÃO RECEBIDO
						$(".segmentosGrid").flexigrid({
							preProcess : this.preProcessSegmentosGrid,
							dataType : 'json',
							colModel : [ {
								display : 'Cota',
								name : 'numeroCota',
								width : 50,
								sortable : true,
								align : 'left'
							}, {
								display : 'Nome',
								name : 'nomePessoa',
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
							sortname : "numeroCota",
							sortorder : "asc",
							width : 300,
							height : 235
						});
						
					$(".segmentoNaoRecebidaGrid").flexigrid({
						preProcess : segmentoNaoRecebidoController.preProcessamentoCotasNaoRecebemSegmentoGrid,
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

					excluirSegmento : function (segmentoNaoRecebidoId) {
						//$( "#dialog:ui-dialog" ).dialog( "destroy" );

						
						
						$( "#dialog-excluir" ).dialog({
							resizable: false,
							height:170,
							width:380,
							modal: true,
							buttons: {
								"Confirmar": function() {
									
									// Exclusão do segmento
									$.postJSON(
											segmentoNaoRecebidoController.urlExcluirSegmentoNaoRecebido,
											{segmentoNaoRecebidoId:segmentoNaoRecebidoId},
											function(result){
										
												 var tipoMensagem = result.tipoMensagem;
										         var listaMensagens = result.listaMensagens;
										         
										         if (tipoMensagem && listaMensagens) {
										        	 exibirMensagem(tipoMensagem, listaMensagens);
										         }
										    },
											null,
									        true);
									
									// Recarrega o grid
									segmentoNaoRecebidoController.reloadFlexGrid("segmentoNaoRecebidaGrid");
									
									$( this ).dialog( "close" );
									//$("#effect").show("highlight", {}, 1000, callback);
								},
								"Cancelar": function() {
									$( this ).dialog( "close" );
								}
							}
						});
					},

					preProcessamentoCotasNaoRecebemSegmentoGrid : function(resultado) {

						segmentoNaoRecebidoController.messagesFeedBack(resultado);

						// Adicionar a imagem para exclusão da linha
						$.each(resultado.rows, function(index, row) {
							
							var linkExcluirSegmento = '<a href="javascript:;" onclick="segmentoNaoRecebidoController.excluirSegmento('+row.cell.segmentoNaoRecebidoId+');" style="cursor:pointer">' +
											   	 '<img src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" />' +
											   '</a>';
							
							row.cell.acao = linkExcluirSegmento;
						});
						
						$(".grids", segmentoNaoRecebidoController.workspace).show();

						return resultado;

					},
					
					preProcessSegmentosGrid : function(resultado){
						
						segmentoNaoRecebidoController.messagesFeedBack(resultado);
						
						// Adicionar a imagem para exclusão da linha
						$.each(resultado.rows, function(index, row) {
							
							var checkBoxIdCota = '<input type="checkbox" name="numeroCota" value="' + row.cell.idCota + '" />';
							
							row.cell.sel = checkBoxIdCota;
						});
						
						$(".grids", segmentoNaoRecebidoController.workspace).show();
						
						return resultado;
						
					},
					
					popularSegmentoNaoRecebidaGrid : function() {
						$(".segmentoNaoRecebidaGrid", segmentoNaoRecebidoController.workspace).flexOptions(
								{
									url : segmentoNaoRecebidoController.urlPesquisarCotasNaoRecebemSegmento,
									dataType : 'json',
									params : segmentoNaoRecebidoController.getFiltroSegmento(),
								});

						$(".segmentoNaoRecebidaGrid", segmentoNaoRecebidoController.workspace).flexReload();
					},
					
					/**
					* Faz a pesquisa da cota por numero ou nome para popular o grid .segmentosGrid.
					* 
					* Usuário irá escolher essas cotas para inserir no segmento não recebido
					* @method pesquisarCota
					*/
					pesquisarCotasNaoEstaoNoSegmento : function(){
						
						$(".segmentosGrid", segmentoNaoRecebidoController.workspace).flexOptions(
								{
									url : segmentoNaoRecebidoController.urlPesquisarCotasNaoEstaoNoSegmento,
									dataType : 'json',
									params : segmentoNaoRecebidoController.getFiltroSegmento(),
								});

						$(".segmentosGrid", segmentoNaoRecebidoController.workspace).flexReload();
						
					},

					/**
					* Inicia o processo de inclusão da cota no Segmento Não Recebido.
					* @method incluirCotaSegmentoNaoRecebido
					* @param nenhum
					* @return nenhum
					*/
					incluirCotaSegmentoNaoRecebido : function(){
						data = new Array();

						$("input[type=checkbox][name='numeroCota']:checked").each(function(){
							data.push({name : "idCotas", value : $(this).val()});
						});

							data.push({
							name : "idTipoSegmento",
							value : $('#tipoSegmentoProduto option:selected',	segmentoNaoRecebidoController.workspace).val()
						});
						
						this.sendJsonToControllerByPost(
								segmentoNaoRecebidoController.urlIncluirCotasSegmentoNaoRecebido,
								data,
								this.callBackOnSucess,
								"segmentoNaoRecebidaGrid"
						);
						
						segmentoNaoRecebidoController.reloadFlexGrid("segmentosGrid");
					},
					
					/**
					 *  Faz uma requisição a controller via post
					 * 
					 * @param {String} url - URL do método na controller
					 * @param {Object} data - Parâmetros da requisição
					 * @param (String} flexGridClass - valor do atributo class da table que representa o flexGrid
					 * @param {function} callBackOnSucess - function executada no sucesso da requisição
					 * @param {function} callBackOnError - function executada no erro da requisição
					 * @return sem retorno
					 * 
					 */
					sendJsonToControllerByPost : function(url, data, callBackOnSucess, flexGridClass) {
						
						$.postJSON(
								url, 
								data,
								function(result) {
									callBackOnSucess(result, flexGridClass);
								}
						);
					},
					
					callBackOnSucess : function (result, flexGridClass){
						 var tipoMensagem = result.tipoMensagem;
				         var listaMensagens = result.listaMensagens;
				         
				         if (tipoMensagem && listaMensagens) {
				        	 exibirMensagem(tipoMensagem, listaMensagens);
				         }
				         
//				         if(flexGridClass != "" && flexGridClass != undefined ){
				        	 segmentoNaoRecebidoController.reloadFlexGrid(flexGridClass);	 
//				         }
					},
					
					/**
					 * Faz o reload no flexGrid passado.
					 * 
					 * @method reloadFlexGrid
					 * @param {String} flexGridClass - o valor do atributo class no DOM.
					 * @return sem retorno.
					 */
					reloadFlexGrid : function(flexGridClass) {
						$("."+flexGridClass, segmentoNaoRecebidoController.workspace).flexReload();
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
						data.push({
							name : 'filtro.numeroCota',
							value : segmentoNaoRecebidoController.get("numeroCotaFiltro2")
						});
						data.push({
							name : 'filtro.nomeCota',
							value : segmentoNaoRecebidoController.get("nomeCotaFiltro2")
						});
						

						return data;
					},
					
					
					getFiltroCota : function() {

						var data = [];

						data.push({
							name : 'filtro.numeroCota',
							value : segmentoNaoRecebidoController.get("numeroCotaFiltro1")
						});
						data.push({
							name : 'filtro.nomeCota',
							value : segmentoNaoRecebidoController.get("nomeCotaFiltro1")
						});
						

						return data;
					},
					
					porSegmento : function(){
						$('.porCota').hide();
						$('.porSegmento').show();
						
						segmentoNaoRecebidoController.popularSegmentoNaoRecebidaGrid();
					},
					
					porCota : function(){
						$('.porCota').show();
						$('.porSegmento').hide();
						
						
					},
					
					//GRID .segmentoCotaGrid
					popularSegmentosNaoRecebemCota : function() {
						
						this.sendJsonToControllerByPost(url, data, callBackOnSucess, flexGridClass)
						
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
					
					// Processa e apresenta ao usuário as mensagens de erro, sucesso
					messagesFeedBack : function(resultado){
						if (resultado.mensagens) {

							exibirMensagem(resultado.mensagens.tipoMensagem,
									resultado.mensagens.listaMensagens);

							$(".grids", segmentoNaoRecebidoController.workspace).hide();

							return resultado;
						}
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

					}

				}, BaseController);
 //@ sourceURL=segmentoNaoRecebido.js
