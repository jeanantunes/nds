var segmentoNaoRecebidoController = $.extend(true,	{

					init : function() {
						
						// URLs utilizadas nas requisições
						segmentoNaoRecebidoController.urlExcluirSegmentoNaoRecebido = contextPath + "/distribuicao/segmentoNaoRecebido/excluirSegmentoNaoRecebido";
						
						segmentoNaoRecebidoController.urlPesquisarCotasNaoRecebemSegmento = contextPath + "/distribuicao/segmentoNaoRecebido/pesquisarCotasNaoRecebemSegmento";
						
						segmentoNaoRecebidoController.urlPesquisarCotasNaoEstaoNoSegmento = contextPath + "/distribuicao/segmentoNaoRecebido/pesquisarCotasNaoEstaoNoSegmento";
						
						segmentoNaoRecebidoController.urlIncluirCotasSegmentoNaoRecebido = contextPath + "/distribuicao/segmentoNaoRecebido/incluirCotasSegmentoNaoRecebido";
						
						segmentoNaoRecebidoController.urlIncluirSegmentosNaCota = contextPath + "/distribuicao/segmentoNaoRecebido/incluirSegmentosNaCota";
						
						segmentoNaoRecebidoController.urlPesquisarSegmentosCadastradosNaCota = contextPath + "/distribuicao/segmentoNaoRecebido/pesquisarSegmentosCadastradosNaCota";
						
						segmentoNaoRecebidoController.urlPesquisarSegmentosElegiveisParaInclusao = contextPath + "/distribuicao/segmentoNaoRecebido/pesquisarSegmentosElegiveisParaInclusao";
						
						segmentoNaoRecebidoController.urlCarregarComboboxInclusaoDeSegmentoNaCota = contextPath + "/distribuicao/segmentoNaoRecebido/carregarComboboxInclusaoDeSegmentoNaCota";
						
						// GRID NA TELA PRINCIPAL LADO ESQUERDO PESQUISAS POR COTA (numero ou nome)
						$(".segmentoCotaGrid").flexigrid({
							preProcess : segmentoNaoRecebidoController.preProcessIncluirBotaoExcluirCota ,
							dataType : 'json',
							colModel : [ {
								display : 'Segmento',
								name : 'nomeSegmento',
								width : 260,
								sortable : true,
								align : 'left'
							}, {
								display : 'Usuário',
								name : 'nomeUsuario',
								width : 100,
								sortable : true,
								align : 'left'
							}, {
								display : 'Data',
								name : 'dataAlteracaoFormatada',
								width : 80,
								sortable : true,
								align : 'center'
							}, {
								display : 'Hora',
								name : 'horaAlteracaoFormatada',
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
							sortname : "nomeSegmento",
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
						
						// Grid que mostra ao usuário os segmentos disponíveis para incluir na cota
						$(".segmentosBGrid").flexigrid({
							preProcess : segmentoNaoRecebidoController.preProcessIncluirCheckBoxSegmentosBGrid,
							dataType : 'json',
							colModel : [ {
								display : 'Segmento',
								name : 'descricao',
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
							preProcess : segmentoNaoRecebidoController.preProcessIncluirCheckBox,
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
						
					// GRID NA TELA PRINCIPAL LADO ESQUERDO PESQUISAS POR SEGMENTO
					$(".segmentoNaoRecebidaGrid").flexigrid({
						preProcess : segmentoNaoRecebidoController.preProcessIncluirBotaoExcluirSegmento,
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

						$( "#dialog-novo").dialog({
							resizable: false,
							height:170,
							width:380,
							modal: true,
							buttons: {
								"Confirmar": function() {
									$( this ).dialog( "close" );
									//$("#effect").show("highlight", {}, 1000, callback);
									
									data = [];

									$("input[type=checkbox][name='idTipoSegmento']:checked").each(function(){
										data.push({name : "idTipoSegmentos", value : $(this).val()});
									});
									data.push({
										name : "numeroCota",
										value : segmentoNaoRecebidoController.get("numeroCotaFiltro1")
									});
									data.push({
										name : "nomeCota",
										value : segmentoNaoRecebidoController.get("nomeCotaFiltro1")
									});
									
									// Inclusão do segmento
									$.postJSON(
											segmentoNaoRecebidoController.urlIncluirSegmentosNaCota,
											data,
											function(result){
										
												 var tipoMensagem = result.tipoMensagem;
										         var listaMensagens = result.listaMensagens;
										         
										         if (tipoMensagem && listaMensagens) {
										        	 exibirMensagem(tipoMensagem, listaMensagens);
										         }
										      
										         // Faz o reload no grid on lista os segmentos disponíveis para inserção 
										         $(".segmentosBGrid", segmentoNaoRecebidoController.workspace).flexOptions(
															{
																url : segmentoNaoRecebidoController.urlPesquisarSegmentosElegiveisParaInclusao,
																dataType : 'json',
																params : segmentoNaoRecebidoController.getFiltroCotaParaReload()
															});

												 $(".segmentosBGrid", segmentoNaoRecebidoController.workspace).flexReload();
												 
										         segmentoNaoRecebidoController.reloadFlexGrid("segmentoCotaGrid");
										         
										         // Reload no combobox
											     segmentoNaoRecebidoController.carregarSelectComSegmentosParaInclusao();
											  
											     $('#lstSegmento', segmentoNaoRecebidoController.workspace).val('');
											     
										    },
											null,
									        true);
									// limpa a caixa de texto do autoComplete por segmento
							         
									
								},
								"Cancelar": function() {
									$( this ).dialog( "close" );
								}
							}
						});
					},
					
					carregarSelectComSegmentosParaInclusao : function carregarSelectComSegmentosParaInclusao(){
						
					      $.ajax({
					         type: "POST",
					         url: segmentoNaoRecebidoController.urlCarregarComboboxInclusaoDeSegmentoNaCota,
					         data: segmentoNaoRecebidoController.getFiltroCota(),
					         dataType: "json",
					         success: function(data){
					            var options = "<option selected=selected value=0>Selecione...</option>";
					            $.each(data.listaTipoSegmentoProduto, function(key, tipoSegmentoProduto){
					               options += '<option value="' + tipoSegmentoProduto.id + '">' + tipoSegmentoProduto.descricao + '</option>';
					            });
					            $("#tipoSegmentoProdutoInclusao").html(options);
					         }
					      });
						
					},

					excluirSegmentoNaoRecebidoSegmento : function (segmentoNaoRecebidoId) {

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
										         
										         $(".segmentoNaoRecebidaGrid", segmentoNaoRecebidoController.workspace).flexOptions(
															{
																url : segmentoNaoRecebidoController.urlPesquisarCotasNaoRecebemSegmento,
																dataType : 'json',
																params : segmentoNaoRecebidoController.getFiltroSegmentoParaReload(),
															});
										         
										         $(".segmentoNaoRecebidaGrid", segmentoNaoRecebidoController.workspace).flexReload();
										         
										    },
											null,
									        true);
									
									$( this ).dialog( "close" );
									//$("#effect").show("highlight", {}, 1000, callback);
									
								},
								"Cancelar": function() {
									$( this ).dialog( "close" );
								}
							}
						});
					},

					
					excluirSegmentoNaoRecebidoCota : function (segmentoNaoRecebidoId) {

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
										         
										         segmentoNaoRecebidoController.carregarSelectComSegmentosParaInclusao();
 												 segmentoNaoRecebidoController.reloadFlexGrid("segmentosBGrid");
										    },
											null,
									        true);
									
									$( this ).dialog( "close" );
									//$("#effect").show("highlight", {}, 1000, callback);
									
								},
								"Cancelar": function() {
									$( this ).dialog( "close" );
								}
							}
						});
					},
					
					preProcessIncluirBotaoExcluirSegmento : function(resultado) {

						segmentoNaoRecebidoController.messagesFeedBack(resultado);

						// Adicionar a imagem para exclusão da linha
						$.each(resultado.rows, function(index, row) {
							
							var linkExcluirSegmento = '<a href="javascript:;" onclick="segmentoNaoRecebidoController.excluirSegmentoNaoRecebidoSegmento('+row.cell.segmentoNaoRecebidoId+');" style="cursor:pointer">' +
											   	 '<img src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" />' +
											   '</a>';
							
							row.cell.acao = linkExcluirSegmento;
						});
						
						$(".grids", segmentoNaoRecebidoController.workspace).show();

						return resultado;

					},
					
					preProcessIncluirBotaoExcluirCota : function(resultado) {

						segmentoNaoRecebidoController.messagesFeedBack(resultado);

						// Adicionar a imagem para exclusão da linha
						$.each(resultado.rows, function(index, row) {
							
							var linkExcluirSegmento = '<a href="javascript:;" onclick="segmentoNaoRecebidoController.excluirSegmentoNaoRecebidoCota('+row.cell.segmentoNaoRecebidoId+');" style="cursor:pointer">' +
											   	 '<img src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" />' +
											   '</a>';
							
							row.cell.acao = linkExcluirSegmento;
						});
						
						$(".grids", segmentoNaoRecebidoController.workspace).show();

						return resultado;

					},
					
					
					//preProcessSegmentosGrid
					preProcessIncluirCheckBoxSegmentosBGrid : function(resultado){
						
						segmentoNaoRecebidoController.messagesFeedBack(resultado);
						
						// Adicionar a imagem para exclusão da linha
						$.each(resultado.rows, function(index, row) {
							
							var checkBoxIdTipoSegmentoProduto = '<input type="checkbox" name="idTipoSegmento" value="' + row.cell.id + '" />';
							
							row.cell.sel = checkBoxIdTipoSegmentoProduto;
						});
						
						$(".grids", segmentoNaoRecebidoController.workspace).show();
						
						return resultado;
						
					},
					
					preProcessIncluirCheckBox : function(resultado){
						
						segmentoNaoRecebidoController.messagesFeedBack(resultado);
						
						// Adicionar a imagem para exclusão da linha
						$.each(resultado.rows, function(index, row) {
							
							var checkBoxIdCota = '<input type="checkbox" name="numeroCota" value="' + row.cell.idCota + '" />';
							
							row.cell.sel = checkBoxIdCota;
						});
						
						$(".grids", segmentoNaoRecebidoController.workspace).show();
						
						return resultado;
						
					},
					
					// popularSegmentoNaoRecebidaGrid 
					// Pesquisar cotas que não recebem o segmento selecionado
					pesquisarCotasNaoRecebemSegmento : function() {
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
									params : segmentoNaoRecebidoController.getFiltroCota2(),
								});

						$(".segmentosGrid", segmentoNaoRecebidoController.workspace).flexReload();
						
					},

					/**
					* Inicia o processo de inclusão da cota no Segmento Não Recebido.
					* @method incluirCotaSegmentoNaoRecebido
					* @param nenhum
					* @return nenhum
					*/
					incluirSegmentosNaCota : function(){
						data = [];

						$("input[type=checkbox][name='idTipoSegmento']:checked").each(function(){
							data.push({name : "idTipoSegmentos", value : $(this).val()});
						});
						data.push({
							name : "numeroCota",
							value : segmentoNaoRecebidoController.get("numeroCotaFiltro1")
						});
						data.push({
							name : "nomeCota",
							value : segmentoNaoRecebidoController.get("nomeCotaFiltro1")
						});
						
							
						this.sendPostJsonToController(
								segmentoNaoRecebidoController.urlIncluirSegmentosNaCota,
								data,
								["segmentosBGrid","segmentoCotaGrid"],
								this.callBackOnSucess 
						);
					},
					
					/**
					* Inicia o processo de inclusão da cota no Segmento Não Recebido.
					* @method incluirCotaSegmentoNaoRecebido
					* @param nenhum
					* @return nenhum
					*/
					incluirCotaSegmentoNaoRecebido : function(){
						data = [];

						$("input[type=checkbox][name='numeroCota']:checked").each(function(){
							data.push({name : "idCotas", value : $(this).val()});
						});

							data.push({
							name : "idTipoSegmento",
							value : $('#tipoSegmentoProduto option:selected',	segmentoNaoRecebidoController.workspace).val()
						});
						
						this.sendPostJsonToController(
								segmentoNaoRecebidoController.urlIncluirCotasSegmentoNaoRecebido,
								data,
								["segmentoNaoRecebidaGrid","segmentosGrid"],
								this.callBackOnSucess
						);
					},
					
					/**
					 *  Faz uma requisição a controller via post
					 * 
					 * @param {String} url - URL do método na controller
					 * @param {Object} data - Parâmetros da requisição
					 * @param (Array} flexGridClass - um array contendo todos os grids que serão atualizados
					 * @param {function} callBackOnSucess - function executada no sucesso da requisição
					 * @return sem retorno
					 */
					sendPostJsonToController : function(url, data, flexGridClass, callBackOnSucess) {
						
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
				         
				         if (flexGridClass != null) {
					         for (var i in flexGridClass){
					        	 
					        	// Fazendo uma nova requisição quando for reload Segundo filtro
					        	//TODO
					        	 if (flexGridClass[i] === "segmentosBGrid") {
					        	 
					        		 $(".segmentosBGrid", segmentoNaoRecebidoController.workspace).flexOptions(
												{
													url : segmentoNaoRecebidoController.urlPesquisarSegmentosElegiveisParaInclusao,
													dataType : 'json',
													params : segmentoNaoRecebidoController.getFiltroCotaParaReload()
												});

										$(".segmentosBGrid", segmentoNaoRecebidoController.workspace).flexReload();
					        		 
					        	 }
					        	 // Fazendo uma nova requisição quando for reload Primeiro filtro
					        	if (flexGridClass[i] === "segmentosGrid") {
					        		
					        		$(".segmentosGrid", segmentoNaoRecebidoController.workspace).flexOptions(
											{
												url : segmentoNaoRecebidoController.urlPesquisarCotasNaoEstaoNoSegmento,
												dataType : 'json',
												params : segmentoNaoRecebidoController.getFiltroCota2ParaReload(),
											});

									$(".segmentosGrid", segmentoNaoRecebidoController.workspace).flexReload();
									
								}else{
									segmentoNaoRecebidoController.reloadFlexGrid(flexGridClass[i]);
								}
					     	}
				         }
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
					
					getFiltroSegmentoParaReload : function() {
						
						var data = segmentoNaoRecebidoController.getFiltroSegmento();
						
						data.push({
							name : 'isReload',
							value : true
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
					
					getFiltroCotaParaReload : function() {

						var data = segmentoNaoRecebidoController.getFiltroCota();

						data.push({
							name : 'isReload',
							value : true
						});

						return data;
					},
					
					
					
					getFiltroCota2 : function() {

						var data = [];

						data.push({
							name : 'filtro.numeroCota',
							value : segmentoNaoRecebidoController.get("numeroCotaFiltro2")
						});
						data.push({
							name : 'filtro.nomeCota',
							value : segmentoNaoRecebidoController.get("nomeCotaFiltro2")
						});
						data.push({
							name : 'filtro.tipoSegmentoProdutoId',
							value : $('#tipoSegmentoProduto option:selected', segmentoNaoRecebidoController.workspace).val()
						});
						

						return data;
					},
					
					getFiltroCota2ParaReload : function() {

						var data = segmentoNaoRecebidoController.getFiltroCota2();

						data.push({
							name : 'isReload',
							value : true
						});

						return data;
					},
					
					
					porSegmento : function(){
						$('.porCota').hide();
						$('.porSegmento').show();
						
						segmentoNaoRecebidoController.pesquisarCotasNaoRecebemSegmento();
					},
					
					porCota : function(){
						$('.porCota').show();
						$('.porSegmento').hide();
						
						segmentoNaoRecebidoController.popularSegmentosNaoRecebemCota();
						segmentoNaoRecebidoController.popularSegmentosBGrid();
						segmentoNaoRecebidoController.carregarSelectComSegmentosParaInclusao();
					},
					
					//GRID .segmentoCotaGrid
					popularSegmentosNaoRecebemCota : function() {
						
						$(".segmentoCotaGrid", segmentoNaoRecebidoController.workspace).flexOptions(
								{
									url : segmentoNaoRecebidoController.urlPesquisarSegmentosCadastradosNaCota,
									dataType : 'json',
									params : segmentoNaoRecebidoController.getFiltroCota()
								});

						$(".segmentoCotaGrid", segmentoNaoRecebidoController.workspace).flexReload();
					},
					
					
					//GRID .segmentosBGrid
					popularSegmentosBGrid : function( ) {
						
						$(".segmentosBGrid", segmentoNaoRecebidoController.workspace).flexOptions(
								{
									url : segmentoNaoRecebidoController.urlPesquisarSegmentosElegiveisParaInclusao,
									dataType : 'json',
									params : segmentoNaoRecebidoController.getFiltroCota()
								});

						$(".segmentosBGrid", segmentoNaoRecebidoController.workspace).flexReload();
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

					},
					
					//Busca dados para o auto complete do nome da cota
					autoCompletarSegmentoPorNome : function(idCampoNomeSegmento, isFromModal) {
						
						segmentoNaoRecebidoController.pesquisaRealizada = false;
						
						var nomeSegmento = $(idCampoNomeSegmento, segmentoNaoRecebidoController.workspace).val();
						
						nomeSegmento = $.trim(nomeSegmento);
						
						$(idCampoNomeSegmento, segmentoNaoRecebidoController.workspace).autocomplete({source: [""]});
						
						if (nomeSegmento && nomeSegmento.length > 2) {

							data = segmentoNaoRecebidoController.getFiltroCota();
							
							data.push({
								name : "filtro.nomeSegmento", 
								value : nomeSegmento
							});
							
							$.postJSON(
								contextPath + "/distribuicao/segmentoNaoRecebido/autoCompletarPorNome", data,
								function(result) { 
									segmentoNaoRecebidoController.exibirAutoComplete(result, idCampoNomeSegmento);
											
									selectedNomeSegmento = $(idCampoNomeSegmento).val();
									
									for ( var index in result) {
										if (result[index].value === selectedNomeSegmento) {
											
										returnFromController = segmentoNaoRecebidoController.getFiltroCota();
										
										returnFromController.push({
											name : "filtro.nomeSegmento", 
											value : selectedNomeSegmento
										});
										
										returnFromController.push({
											name : "filtro.autoComplete", 
											value : true
										});
										
										$(".segmentosBGrid", segmentoNaoRecebidoController.workspace).flexOptions(
												{
													url : segmentoNaoRecebidoController.urlPesquisarSegmentosElegiveisParaInclusao,
													dataType : 'json',
													params : returnFromController
												});
	
										$(".segmentosBGrid", segmentoNaoRecebidoController.workspace).flexReload();
										
										}
									}
									
								},
								null, 
								isFromModal
							);
						}
					},
					
					descricaoAtribuida : true,
					
					pesquisaRealizada : false,
					
					intervalo : null,
					
					//Exibe o auto complete no campo
					exibirAutoComplete : function(result, idCampoNomeSegmento) {
						
						$(idCampoNomeSegmento, segmentoNaoRecebidoController.workspace).autocomplete({
							source: result,
							focus : function(event, ui) {
								segmentoNaoRecebidoController.descricaoAtribuida = false;
							},
							close : function(event, ui) {
								segmentoNaoRecebidoController.descricaoAtribuida = true;
							},
							select : function(event, ui) {
								segmentoNaoRecebidoController.descricaoAtribuida = true;
							},
							minLength: 3,
							delay : 0,
						});
					},

				}, BaseController);
 //@ sourceURL=segmentoNaoRecebido.js