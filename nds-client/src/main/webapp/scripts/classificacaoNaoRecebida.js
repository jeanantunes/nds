var classificacaoNaoRecebidaController = $.extend(true, {
	
	/**
	* @author InfoA2 - Samuel Mendes
	* 
	* @method reloadFlexGrid
	* 
	* @param {String} url para executar a requisição
	* <p> Ex.: url : contextPath + 'distribuicao/segmentoNaoRecebido/pesquisarSegmentos' </p>
	*  
	* @param {String} dataType : tipo da requisição  
	* <p> Ex.: dataType : 'json'</p>
	* 
	* @param {Array} params : parametros para enviar com a requisição 
	* <p> Ex.: params : '[{name : "numeroCota", value : "123"}]' </p>
	* 
	* @param {String} workspace : "workspace"
	* <p> Ex.: workspace : this.workspace;
	* 
	* @param {function} preProcess : função para executar antes da construção do grid 
	* <p> Ex.: preProcess : function(){ 
	* 				// código para construção de ELEMENTOS CHECKBOX ou IMG
	* 		   } 
	* </p>
	* 
	*/
	init : function() {
	
		var autoComplete = new AutoCompleteController(classificacaoNaoRecebidaController.workspace);
		
		// #### ASSOCIANDO OS EVENTOS NO DOM ####

		// ### POR COTA ###
		$('#pesquisarPorCota', classificacaoNaoRecebidaController.workspace).click(function (){
			classificacaoNaoRecebidaController.porCota();
		});
		
		$('#porCota_numeroCota', classificacaoNaoRecebidaController.workspace).change(function (){
			autoComplete.pesquisarPorCodigo("/cadastro/cota/pesquisarPorNumeroAutoComplete",'#porCota_numeroCota','#porCota_nomeCota');
			//pesquisaCota.pesquisarPorNumeroCota('#porCota_numeroCota','#porCota_nomeCota');
		});
		
		$('#porCota_nomeCota', classificacaoNaoRecebidaController.workspace).keyup(function (){
			autoComplete.autoCompletar("/cadastro/cota/autoCompletarPorNomeAutoComplete",'#porCota_numeroCota','#porCota_nomeCota');
			//consultaCota.autoCompletarPorNome(inputsCota);
			
		});
		
		autoComplete.limparCampoOnChange('#porCota_nomeCota', new Array('#porCota_numeroCota'));
		
		// INCLUIR CLASSIFICAÇÃO NA COTA
		$('#confirmarInclusaoDaClassificacaoNaCota', classificacaoNaoRecebidaController.workspace).click(function (){
			classificacaoNaoRecebidaController.inserirClassificacaoNaCota();
		});
		
		// ### POR CLASSIFICAÇÃO ###
		$('#pesquisarPorClassificacao', classificacaoNaoRecebidaController.workspace).click(function (){
			classificacaoNaoRecebidaController.porClassificacao();
		});
		
		$('#cotasQueRecebem_numeroCota', classificacaoNaoRecebidaController.workspace).change(function (){
			autoComplete.pesquisarPorCodigo("/cadastro/cota/pesquisarPorNumeroAutoComplete",'#cotasQueRecebem_numeroCota','#cotasQueRecebem_nomeCota');
			//pesquisaCota.pesquisarPorNumeroCota('#cotasQueRecebem_numeroCota','#cotasQueRecebem_nomeCota');
		});
		
		$('#cotasQueRecebem_nomeCota', classificacaoNaoRecebidaController.workspace).keyup(function (){
			autoComplete.autoCompletar("/cadastro/cota/autoCompletarPorNomeAutoComplete",'#cotasQueRecebem_numeroCota','#cotasQueRecebem_nomeCota');
			//pesquisaCota.autoCompletarPorNome('#cotasQueRecebem_nomeCota');
		});
		
		autoComplete.limparCampoOnChange('#cotasQueRecebem_nomeCota', new Array('#cotasQueRecebem_numeroCota'));
		
//		$('#cotasQueRecebem_nomeCota', classificacaoNaoRecebidaController.workspace).change(function (){
//			pesquisaCota.pesquisarPorNomeCota('#cotasQueRecebem_numeroCota','#cotasQueRecebem_nomeCota');
//		});
		
		// INCLUIR COTAS NA CLASSIFICAÇÃO NÃO RECEBIDA
		$('#confirmarInclusaoDaCotaNaClassificacaoNaoRecebida', classificacaoNaoRecebidaController.workspace).click(function (){
			classificacaoNaoRecebidaController.inserirCotaNaClassificacaoNaoRecebida();
		});
		
		$('#pesquisarCotaQueRecebeClassificacao', classificacaoNaoRecebidaController.workspace).click(function (){
			
			var grids = classificacaoNaoRecebidaController.Grids,
			params = [];
		
			params = classificacaoNaoRecebidaController.Util.getFiltroByForm('filtroPrincipalClassificacao');

			params.push({
				name : "filtro.cotaDto.numeroCota",
				value : $('#cotasQueRecebem_numeroCota', classificacaoNaoRecebidaController.workspace).val()
			});
			
			params.push({
				name : "filtro.cotaDto.nomePessoa",
				value : $('#cotasQueRecebem_nomeCota', classificacaoNaoRecebidaController.workspace).val()
			});
			
			params.push({
				name : "filtro.autoComplete",
				value : false
			});
			
			grids.ClassificacaoGrid.reload({
				dataType : 'json',
				params : params
			});
		});
		
		
		// EXPORTAÇÃO
		$('#porClassificacaoGerarPDF', classificacaoNaoRecebidaController.workspace).attr('href', contextPath + "/distribuicao/classificacaoNaoRecebida/exportar?fileType=PDF&porCota=false");
		
		$('#porClassificacaoGerarXLS', classificacaoNaoRecebidaController.workspace).attr('href', contextPath + "/distribuicao/classificacaoNaoRecebida/exportar?fileType=XLS&porCota=false");
		
		$('#porCotaGerarPDF', classificacaoNaoRecebidaController.workspace).attr('href', contextPath + "/distribuicao/classificacaoNaoRecebida/exportar?fileType=PDF&porCota=true");
		
		$('#porCotaGerarXLS', classificacaoNaoRecebidaController.workspace).attr('href', contextPath + "/distribuicao/classificacaoNaoRecebida/exportar?fileType=XLS&porCota=true");
		
		// URLs usadas para requisições post (Inserção e Deleção)
		classificacaoNaoRecebidaController.Url = {
				excluirClassificacaoNaoRecebida : contextPath + "/distribuicao/classificacaoNaoRecebida/excluirClassificacaoNaoRecebida",
				inserirCotaNaClassificacaoNaoRecebida : contextPath + "/distribuicao/classificacaoNaoRecebida/inserirCotaNaClassificacaoNaoRecebida",
				inserirClassificacaoNaCota : contextPath + "/distribuicao/classificacaoNaoRecebida/inserirClassificacaoNaCota",
				autoCompletarPorNomeCotaQueRecebeClassificacao : contextPath + "/distribuicao/classificacaoNaoRecebida/autoCompletarPorNomeCotaQueRecebeClassificacao"
		},
		
		classificacaoNaoRecebidaController.Grids = {
				Util : {
					reload : function reload(options) {
						
						// Inicializa o objeto caso não exista
						options = options || {};
						
						// obtendo a url padrão caso o usuário não tenha informado
						options.url = this.Url.urlDefault || options.url;
						
						options.preProcess = options.preProcess || this.PreProcess._default  || function(result){
							
							if (result.mensagens) {

								exibirMensagem(result.mensagens.tipoMensagem,
										result.mensagens.listaMensagens);

								$(".grids", classificacaoNaoRecebidaController.workspace).hide();

								return result;
							}

							$(".grids", classificacaoNaoRecebidaController.workspace).show();

							return result;
						};
						
						// GUARDA O ULTIMO PARÂMETRO UTILIZADO
						this.lastParams = options.params;
						
						if (options !== undefined) {
							if (options.workspace === undefined) {
								$("." + this.gridName, classificacaoNaoRecebidaController.workspace).flexOptions(options);
								$("." + this.gridName, classificacaoNaoRecebidaController.workspace).flexReload();
							}else {
								$("." + this.gridName, options.workspace).flexOptions(options);
								$("." + this.gridName, options.workspace).flexReload();
							}
						}
					}
				},
				ClassificaNaoRecebidaGrid : {
					reload : {},
					lastParams : [],
				},
				ClassificacaoGrid : {
					reload : {},
					lastParams : []
				},
				ClassificaCotaGrid : {
					reload : {},
					lastParams : []
				},
				ClassificacaoBGrid : {
					reload : {},
					lastParams : []
				}
		};
		
		classificacaoNaoRecebidaController.Util = {
			getFiltroByForm : function(idForm){
				var filtro;
				
				if(idForm === undefined){
					return null;
				}else {
					filtro = $('#' + idForm, classificacaoNaoRecebidaController.workspace).serializeArray();
					
					for ( var index in filtro) {
						if (filtro[index].value === "on") {
							filtro[index].value = true; 
						}else if (filtro[index].value === "off") {
							filtro[index].value = false; 
						}
					}
					
					return filtro;
				}
			},
		},
		
		
		classificacaoNaoRecebidaController.Grids = {
			ClassificaNaoRecebidaGrid : {
				gridName : "classificaNaoRecebidaGrid",
				Url : {
					urlDefault : contextPath + "/distribuicao/classificacaoNaoRecebida/pesquisarCotasQueNaoRecebemClassificacao",
				},
				comments : "Por Classificação - GRID DA ESQUERDA",
				reload : classificacaoNaoRecebidaController.Grids.Util.reload,
				PreProcess : {
					_default : function(result){
						
						if (result.mensagens) {

							exibirMensagem(result.mensagens.tipoMensagem,
									result.mensagens.listaMensagens);

							$(".grids", classificacaoNaoRecebidaController.workspace).hide();

							return result;
						}

						if (result.rows.length === 0) {
							$('#porClassificacaoGerarXLS', classificacaoNaoRecebidaController.workspace).hide();
							$('#porClassificacaoGerarPDF', classificacaoNaoRecebidaController.workspace).hide();
						}else {
							$('#porClassificacaoGerarXLS', classificacaoNaoRecebidaController.workspace).show();
							$('#porClassificacaoGerarPDF', classificacaoNaoRecebidaController.workspace).show();
						}
						
						$.each(result.rows, function(index, row) {
							var link = '<a href="javascript:;" onclick="classificacaoNaoRecebidaController.excluirCotaDaClassificacaoNaoRecebida('+row.cell.idClassificacaoNaoRecebida+');" style="cursor:pointer">' +
					   	 				   '<img title="Excluir Classificação" src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" />' +
					   	 				   '</a>';
							
							row.cell.acao = link;
						});
						
						$(".grids", classificacaoNaoRecebidaController.workspace).show();

						return result;
					}
				},
				init : 
					$(".classificaNaoRecebidaGrid", classificacaoNaoRecebidaController.workspace).flexigrid({
						colModel : [ {
							display : 'Cota',
							name : 'numeroCota',
							width : 60,
							sortable : true,
							align : 'left'
						}, {
							display : 'Nome',
							name : 'nomePessoa',
							width : 200,
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
							sortable : false,
							align : 'center'
						}, {
							display : 'Ação',
							name : 'acao',
							width : 30,
							sortable : false,
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
					})
			},
			ClassificacaoGrid : {
				gridName : "classificacaoGrid",
				Url : {
					urlDefault : contextPath + "/distribuicao/classificacaoNaoRecebida/pesquisarCotasQueRecebemClassificacao",
				},
				comments : "Por Classificação - GRID DA DIREITA",
				reload : classificacaoNaoRecebidaController.Grids.Util.reload,
				PreProcess : {
					_default : function(result){
						
						$('#porCota_numeroCota', classificacaoNaoRecebidaController.workspace).val('');
						$('#porCota_nomeCota', classificacaoNaoRecebidaController.workspace).val('');
						
						if (result.mensagens) {

							exibirMensagem(result.mensagens.tipoMensagem,
									result.mensagens.listaMensagens);

							$(".grids", classificacaoNaoRecebidaController.workspace).hide();

							return result;
						}
						
						$.each(result.rows, function(index, row) {
							
							var checkBox = '<input type="checkbox" name="cotaQueRecebeClassificacao" value="' + row.cell.numeroCota + '" />';
							
							row.cell.sel = checkBox;
						});
						
						$(".grids", classificacaoNaoRecebidaController.workspace).show();

						return result;
					}
				},
				init : 
					$(".classificacaoGrid", classificacaoNaoRecebidaController.workspace).flexigrid({
						colModel : [ {
							display : 'Cota',
							name : 'numeroCota',
							width : 60,
							sortable : false,
							align : 'left'
						}, {
							display : 'Nome',
							name : 'nomePessoa',
							width : 150,
							sortable : false,
							align : 'left'
						},  {
							display : '',
							name : 'sel',
							width : 30,
							sortable : false,
							align : 'center'
						}],
						width : 300,
						height : 235
					})
			},
			ClassificaCotaGrid : {
				gridName : "classificaCotaGrid",
				Url : {
					urlDefault : contextPath + "/distribuicao/classificacaoNaoRecebida/pesquisarClassificacoesNaoRecebidasPelaCota",
				},
				comments : "POR COTA - GRID ESQUERDA",
				reload : classificacaoNaoRecebidaController.Grids.Util.reload,
				PreProcess : {
					_default : function(result){
						
						if (result.mensagens) {

							exibirMensagem(result.mensagens.tipoMensagem,
									result.mensagens.listaMensagens);

							$(".grids", classificacaoNaoRecebidaController.workspace).hide();

							return result;
						}

						if (result.rows.length === 0) {
							$('#porCotaGerarXLS', classificacaoNaoRecebidaController.workspace).hide();
							$('#porCotaGerarPDF', classificacaoNaoRecebidaController.workspace).hide();
						}else {
							$('#porCotaGerarXLS', classificacaoNaoRecebidaController.workspace).show();
							$('#porCotaGerarPDF', classificacaoNaoRecebidaController.workspace).show();
						}
						
						$.each(result.rows, function(index, row) {
							
							var link = '<a href="javascript:;" onclick="classificacaoNaoRecebidaController.excluirClassificacaoNaoRecebidaDaCota('+row.cell.idClassificacaoNaoRecebida+');" style="cursor:pointer">' +
					   	 				   '<img title="Excluir Classificação" src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" />' +
					   	 				   '</a>';
							
							row.cell.acao = link;
						});
						
						$(".grids", classificacaoNaoRecebidaController.workspace).show();

						return result;
					}
				},
				init : 
					$(".classificaCotaGrid", classificacaoNaoRecebidaController.workspace).flexigrid({
						colModel : [ {
							display : 'Classificação',
							name : 'nomeClassificacao',
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
							sortable : false,
							align : 'center'
						},  {
							display : 'Ação',
							name : 'acao',
							width : 30,
							sortable : false,
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
					})
				
			},
			ClassificacaoBGrid : {
				gridName : "classificacaoBGrid",
				Url : {
					urlDefault : contextPath + "/distribuicao/classificacaoNaoRecebida/pesquisarClassificacoesRecebidasPelaCota",
				},
				comments : "POR COTA - GRIDA DA DIREITA",
				reload : classificacaoNaoRecebidaController.Grids.Util.reload,
				PreProcess : {
					_default : function(result){
						
						if (result.mensagens) {

							exibirMensagem(result.mensagens.tipoMensagem,
									result.mensagens.listaMensagens);

							$(".grids", classificacaoNaoRecebidaController.workspace).hide();

							return result;
						}

						$.each(result.rows, function(index, row) {
							
							var checkBox = '<input type="checkbox" name="classificacaoRecebida" value="' + row.cell.id + '" />';
							
							row.cell.sel = checkBox;
						});
						
						$(".grids", classificacaoNaoRecebidaController.workspace).show();

						return result;
					}
				},
				init : 
					$(".classificacaoBGrid", classificacaoNaoRecebidaController.workspace).flexigrid({
						colModel : [ {
							display : 'Classificação',
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
						height : 265
					})
				
			},
		};
		
		$(document).ready(function(){
			
			classificacaoNaoRecebidaController.mostraFiltroPorClassificacao();
			focusSelectRefField($("#radio", classificacaoNaoRecebidaController.workspace));
			$("#radio", classificacaoNaoRecebidaController.workspace).attr("checked", true);
			
//			$(document.body).keydown(function(e) {
//				
//				if(keyEventEnterAux(e)){
//					classificacaoNaoRecebidaController.porClassificacao();
//				}
//				
//				return true;
//			});
		});
	},
	
	excluirCotaDaClassificacaoNaoRecebida : function excluirCotaDaClassificacaoNaoRecebida(id){
		
		if(!verificarPermissaoAcesso(this.workspace)){
			return;
		}
		
		var grids = classificacaoNaoRecebidaController.Grids;
		
		$( "#dialog-excluirCotaDaClassificacaoNaoRecebida" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					
					$.postJSON(
							classificacaoNaoRecebidaController.Url.excluirClassificacaoNaoRecebida,
						[{name : "id" , value : id}],
						function(result){
							 tipoMensagem = result.tipoMensagem;
					         listaMensagens = result.listaMensagens;
					         
					         if (tipoMensagem && listaMensagens) {
					        	 exibirMensagem(tipoMensagem, listaMensagens);
					         }
							
					         params =  classificacaoNaoRecebidaController.Util.getFiltroByForm("filtroPrincipalClassificacao");
					         
					         grids.ClassificaNaoRecebidaGrid.reload({
					        	 params : params
					         });
				        	 
					         grids.ClassificacaoGrid.reload();
						}
					);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	
	},
	
	excluirClassificacaoNaoRecebidaDaCota : function excluirClassificacaoNaoRecebidaDaCota(id){
		
		if(!verificarPermissaoAcesso(this.workspace)){
			return;
		}
		
		
		var grids = classificacaoNaoRecebidaController.Grids;
		
		$( "#dialog-excluirCotaDaClassificacaoNaoRecebida" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					
					$.postJSON(
							classificacaoNaoRecebidaController.Url.excluirClassificacaoNaoRecebida,
						[{name : "id" , value : id}],
						function(result){
							 tipoMensagem = result.tipoMensagem;
					         listaMensagens = result.listaMensagens;
					         
					         if (tipoMensagem && listaMensagens) {
					        	 exibirMensagem(tipoMensagem, listaMensagens);
					         }
							
					         params =  classificacaoNaoRecebidaController.Util.getFiltroByForm("filtroPrincipalPorCota");
					         
					         grids.ClassificaCotaGrid.reload({
					        	 params : params
					         });
				        	 
					         grids.ClassificacaoBGrid.reload();
						}
					);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	
	},
	
	inserirCotaNaClassificacaoNaoRecebida : function inserirCotaNaClassificacaoNaoRecebida(){
		
		var grids = classificacaoNaoRecebidaController.Grids;
		
		$( "#dialog-incluirCotaNaClassificacaoNaoRecebida" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					
					var params = [];
					
					// obtendo o id da classificação escolhida pelo usuário
					params =  classificacaoNaoRecebidaController.Util.getFiltroByForm("filtroPrincipalClassificacao");
					
					$("input[type=checkbox][name='cotaQueRecebeClassificacao']:checked").each(function(){
						params.push({
							name : "numerosCota",
							value : this.value
						});
					});
					
					$.postJSON(
							classificacaoNaoRecebidaController.Url.inserirCotaNaClassificacaoNaoRecebida,
						params,
						function(result){
							 tipoMensagem = result.tipoMensagem;
					         listaMensagens = result.listaMensagens;
					         
					         if (tipoMensagem && listaMensagens) {
					        	 exibirMensagem(tipoMensagem, listaMensagens);
					         }
							
					         grids.ClassificaNaoRecebidaGrid.reload({
					        	 params : params
					         });
				        	 
					         grids.ClassificacaoGrid.reload();
						}
					);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	
	},
	
	inserirClassificacaoNaCota : function inserirClassificacaoNaCota(){
		
		var grids = classificacaoNaoRecebidaController.Grids;
		
		$( "#dialog-incluirClassificacaoNaCota" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					
					var params = [];
					
					params =  classificacaoNaoRecebidaController.Util.getFiltroByForm("filtroPrincipalPorCota");
					
					$("input[type=checkbox][name='classificacaoRecebida']:checked").each(function(){
						params.push({
							name : "idsTipoClassificacaoProduto",
							value : this.value
						});
					});
					
					$.postJSON(
							classificacaoNaoRecebidaController.Url.inserirClassificacaoNaCota,
						params,
						function(result){
							 tipoMensagem = result.tipoMensagem;
					         listaMensagens = result.listaMensagens;
					         
					         if (tipoMensagem && listaMensagens) {
					        	 exibirMensagem(tipoMensagem, listaMensagens);
					         }
							
					         grids.ClassificaCotaGrid.reload({
					        	 params : params
					         });
				        	 
					         grids.ClassificacaoBGrid.reload();
						}
					);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	
	},
	
	//pesquisarPorCota
	porCota : function porCota(){
		$('.porCota', classificacaoNaoRecebidaController.workspace).show();
		$('.porClassificacao', classificacaoNaoRecebidaController.workspace).hide();
		
		var grids = classificacaoNaoRecebidaController.Grids,
		params = [];
	
		params = classificacaoNaoRecebidaController.Util.getFiltroByForm('filtroPrincipalPorCota');
	
		grids.ClassificaCotaGrid.reload({
			dataType : 'json',
			params : params
		});
		
		grids.ClassificacaoBGrid.reload({
			dataType : 'json',
			params : params
		});
	},
	
	porClassificacao : function porClassificacao(){
		$('.porCota', classificacaoNaoRecebidaController.workspace).hide();
		$('.porClassificacao', classificacaoNaoRecebidaController.workspace).show();
		
		var grids = classificacaoNaoRecebidaController.Grids,
		params = [];
	
		params = classificacaoNaoRecebidaController.Util.getFiltroByForm('filtroPrincipalClassificacao');
	
		grids.ClassificaNaoRecebidaGrid.reload({
			dataType : 'json',
			params : params
		});
		
		grids.ClassificacaoGrid.reload({
			dataType : 'json',
			params : params
		});
	},
	
	// Autocomplete no nome da cota
	autoCompletarPorNomeCotaQueRecebeClassificacao : function(idNomeCota) {
		
	classificacaoNaoRecebidaController.pesquisaRealizada = false;
		
		var util = classificacaoNaoRecebidaController.Util,
			filtro = [],
			nomePessoa = $(idNomeCota).val();
		if (nomePessoa && nomePessoa.length > 2) {
			
			filtro = util.getFiltroByForm("filtroPrincipalClassificacao");
			
			filtro.push({
				name : "filtro.cotaDto.nomePessoa", 
				value : nomePessoa
			});
			
			filtro.push({
				 name : "filtro.autoComplete", 
				 value : true
			});
			
			$.postJSON(
					classificacaoNaoRecebidaController.Url.autoCompletarPorNomeCotaQueRecebeClassificacao,
				filtro,
				function(result) { 
					classificacaoNaoRecebidaController.exibirAutoComplete(result, idNomeCota);
				}
			);
		}
	},
	
	descricaoAtribuida : true,
	
	pesquisaRealizada : false,
	
	intervalo : null,
	
	exibirAutoComplete : function(result, idCaixaTexto) {
		
		$(idCaixaTexto, classificacaoNaoRecebidaController.workspace).autocomplete({
			source : result,
			focus : function(event, ui) {
				classificacaoNaoRecebidaController.descricaoAtribuida = false;
			},
			close : function(event, ui) {
				classificacaoNaoRecebidaController.descricaoAtribuida = true;
			},
			select : function(event, ui) {
				classificacaoNaoRecebidaController.descricaoAtribuida = true;
			},
			minLength: 4,
			delay : 0,
		});
	},
	
	mostraFiltroPorCota : function (){
		$('#porCota_numeroCota', classificacaoNaoRecebidaController.workspace).val(null);
		$('#porCota_nomeCota', classificacaoNaoRecebidaController.workspace).val(null);
		$('#classificacaoNaoRecebida_filtroPorCota', classificacaoNaoRecebidaController.workspace).show();
		$('#classificacaoNaoRecebida_filtroPorClassificacao', classificacaoNaoRecebidaController.workspace).hide();
		$('.porClassificacao', classificacaoNaoRecebidaController.workspace).hide();
		$('#cotasQueRecebem_numeroCota', classificacaoNaoRecebidaController.workspace).val('');
		$('#cotasQueRecebem_nomeCota', classificacaoNaoRecebidaController.workspace).val('');
	},

	mostraFiltroPorClassificacao : function(){
		$('#selectClassificacao', classificacaoNaoRecebidaController.workspace).val(null);
		$('#classificacaoNaoRecebida_filtroPorCota', classificacaoNaoRecebidaController.workspace).hide();
		$('#classificacaoNaoRecebida_filtroPorClassificacao', classificacaoNaoRecebidaController.workspace).show();
		$('.porCota', classificacaoNaoRecebidaController.workspace).hide();	
		$('#cotasQueRecebem_numeroCota', classificacaoNaoRecebidaController.workspace).val('');
		$('#cotasQueRecebem_nomeCota', classificacaoNaoRecebidaController.workspace).val('');
	},
	
	excluirClassificacao : function() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		
		if(!verificarPermissaoAcesso(this.workspace)){
			return;
		}
		
		
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
	}
	
}, BaseController);

//@ sourceURL=classificacaoNaoRecebidaController.js
