var areaInfluenciaGeradorFluxoController = $
		.extend(
				true,
				{

					// pesquisaRealizada : false,

					// descricaoAtribuida : true,

					// intervalo : null,

					init : function() {
        
						$(".areaInfluenciaGrid", areaInfluenciaGeradorFluxoController.workspace).flexigrid(	{
											preProcess : areaInfluenciaGeradorFluxoController.executarPreProcessamento,
											dataType : 'json',
											colModel : [
													{
														display : 'Cota',
														name : 'numeroCota',
														width : 60,
														sortable : true,
														align : 'left'
													},
													{
														display : 'Status',
														name : 'statusCota',
														width : 60,
														sortable : true,
														align : 'left'
													},
													{
														display : 'Nome',
														name : 'nomeCota',
														width : 120,
														sortable : true,
														align : 'left'
													},
													{
														display : 'Tipo PDV',
														name : 'tipoPdv',
														width : 80,
														sortable : true,
														align : 'left'
													},
													{
														display : 'Bairro',
														name : 'bairro',
														width : 70,
														sortable : true,
														align : 'left'
													},
													{
														display : 'Cidade',
														name : 'cidade',
														width : 80,
														sortable : true,
														align : 'left'
													},
													{
														display : 'Faturamento R$',
														name : 'faturamento',
														width : 80,
														sortable : true,
														align : 'right'
													},
													{
														display : 'Área de Influência',
														name : 'areaInfluencia',
														width : 90,
														sortable : true,
														align : 'left'
													}, {
														display : 'Gerador 1',
														name : 'geradorFluxoPrincipal',
														width : 90,
														sortable : true,
														align : 'left'
													}, {
														display : 'Gerador 2',
														name : 'geradorFluxoSecundario',
														width : 90,
														sortable : true,
														align : 'left'
													} ],
											sortname : "nomeCota",
											sortorder : "asc",
											usepager : true,
											useRp : true,
											rp : 15,
											showTableToggleBtn : true,
											width : 960,
											height : 255
										});
					},

					executarPreProcessamento : function(resultado) {

						if (resultado.mensagens) {

							exibirMensagem(resultado.mensagens.tipoMensagem,
									resultado.mensagens.listaMensagens);

							$(".grids", areaInfluenciaGeradorFluxoController.workspace).hide();

							return resultado;
						}

						$(".grids", areaInfluenciaGeradorFluxoController.workspace).show();

						return resultado;

					},

					incluirSegmento : function() {
						// $( "#dialog:ui-dialog" ).dialog( "destroy" );

						$("#dialog-novo").dialog(
								{
									resizable : false,
									height : 500,
									width : 650,
									modal : true,
									buttons : {
										"Confirmar" : function() {
											$(this).dialog("close");
											$("#effect").show("highlight", {},
													1000, callback);
										},
										"Cancelar" : function() {
											$(this).dialog("close");
										}
									}
								});
					},

					excluirSegmento : function() {
						// $( "#dialog:ui-dialog" ).dialog( "destroy" );

						$("#dialog-excluir").dialog(
								{
									resizable : false,
									height : 170,
									width : 380,
									modal : true,
									buttons : {
										"Confirmar" : function() {
											$(this).dialog("close");
											$("#effect").show("highlight", {},
													1000, callback);
										},
										"Cancelar" : function() {
											$(this).dialog("close");
										}
									}
								});
					},

					pesquisarPorCota : function() {
						$('.porCota').show();
						$('.porArea').hide();

						$(".areaInfluenciaGrid",areaInfluenciaGeradorFluxoController.workspace).flexOptions(
							{
								url : contextPath + "/distribuicao/areaInfluenciaGeradorFluxo/pesquisarPorCota",
								dataType : 'json',
								params : areaInfluenciaGeradorFluxoController.getDadosCota(),
							});

						$(".areaInfluenciaGrid",
								areaInfluenciaGeradorFluxoController.workspace)
								.flexReload();

					},
					
					pesquisarPorArea : function() {
						$('.porCota').hide();
						$('.porArea').show();
						
						$(".areaInfluenciaGrid",areaInfluenciaGeradorFluxoController.workspace).flexOptions(
								{
									url : contextPath + "/distribuicao/areaInfluenciaGeradorFluxo/pesquisarPorArea",
									dataType : 'json',
									params : areaInfluenciaGeradorFluxoController.getDadosAreaInfluencia(),
								});

							$(".areaInfluenciaGrid",
									areaInfluenciaGeradorFluxoController.workspace)
									.flexReload();
					},
					
					getDadosCota : function() {

						var data = [];

						data.push({
							name : 'filtro.numeroCota',
							value : areaInfluenciaGeradorFluxoController.get("numeroCota")
						});
						data.push({
							name : 'filtro.nomeCota',
							value : areaInfluenciaGeradorFluxoController.get("nomeCota")
						});
						
						return data;
					},

					getDadosAreaInfluencia : function() {

						var data = [];

						data.push({
							name : 'filtro.areaInfluenciaId',
							value : $('#areaInfluencia option:selected',
									areaInfluenciaGeradorFluxoController.workspace).val()
									
						});
						data.push({
							name : 'filtro.geradorFluxoPrincipalId',
							value : $('#geradorFluxoPrincipal option:selected',
									areaInfluenciaGeradorFluxoController.workspace).val()
						});
						data.push({
							name : 'filtro.geradorFluxoSecundarioId',
							value : $('#geradorFluxoSecundario option:selected',
									areaInfluenciaGeradorFluxoController.workspace).val()
						});
						data.push({
							name : 'filtro.cotasAtivas',
							value : areaInfluenciaGeradorFluxoController.get("cotasAtivas")
						});

						return data;
					},

					
					get : function(campo) {

						var elemento = $("#" + campo, areaInfluenciaGeradorFluxoController.workspace);

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

					filtroPorCota : function() {
						$('.filtroPorCota').show();
						$('.filtroPorArea').hide();
						$('.porArea').hide();
					},

					filtroPorArea : function() {
						$('.filtroPorCota').hide();
						$('.filtroPorArea').show();
						$('.porCota').hide();
					}

				}, BaseController);
 //@ sourceURL=areaInfluenciaGeradorFluxo.js
