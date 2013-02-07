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
	
		// #### ASSOCIANDO OS EVENTOS NO DOM ####
		
		$('#pesquisarPorCota').click(function (){
			classificacaoNaoRecebidaController.porCota();
		});
		
		$('#pesquisarPorClassificacao').click(function (){
			classificacaoNaoRecebidaController.porClassificacao();
		});
		
		// EXPORTAÇÃO
//		$('#gerarPDFPorClassificacao').attr('href', contextPath + "/distribuicao/excecaoSegmentoParciais/exportar?fileType=PDF&porCota=false");
//		
//		$('#gerarXLSPorClassificacao').attr('href', contextPath + "/distribuicao/excecaoSegmentoParciais/exportar?fileType=XLS&porCota=false");
//		
//		$('#gerarPDFPorCota').attr('href', contextPath + "/distribuicao/excecaoSegmentoParciais/exportar?fileType=PDF&porCota=true");
//		
//		$('#gerarXLSPorCota').attr('href', contextPath + "/distribuicao/excecaoSegmentoParciais/exportar?fileType=XLS&porCota=true");
		
		// URLs usadas para requisições post (Inserção e Deleção)
		classificacaoNaoRecebidaController.Url = {
				excluirClassificacaoNaoRecebida : contextPath + "/distribuicao/classificacaoNaoRecebida/excluirClassificacaoNaoRecebida"
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

								$(".grids").hide();

								return result;
							}

							$(".grids").show();

							return result;
						};
						
						// GUARDA O ULTIMO PARÂMETRO UTILIZADO
						this.lastParams = options.params;
						
						if (options !== undefined) {
							if (options.workspace === undefined) {
								$("." + this.gridName).flexOptions(options);
								$("." + this.gridName).flexReload();
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
				classificacaoBGrid : {
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
					filtro = $('#' + idForm).serializeArray();
					
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
					urlDefault : contextPath + "/distribuicao/classificacaoNaoRecebida/pesquisarClassificacaoNaoRecebida",
				},
				comments : "Por Classificação - GRID DA ESQUERDA",
				reload : classificacaoNaoRecebidaController.Grids.Util.reload,
				PreProcess : {
					_default : function(result){
						
						if (result.mensagens) {

							exibirMensagem(result.mensagens.tipoMensagem,
									result.mensagens.listaMensagens);

							$(".grids").hide();

							return result;
						}

						$.each(result.rows, function(index, row) {
							var link = '<a href="javascript:;" onclick="classificacaoNaoRecebidaController.excluirClassificacaoNaoRecebidaPorCota('+row.cell.idClassificacaoNaoRecebida+');" style="cursor:pointer">' +
					   	 				   '<img title="Excluir Classificação" src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" />' +
					   	 				   '</a>';
							
							row.cell.acao = link;
						});
						
						$(".grids").show();

						return result;
					}
				},
				init : 
					$(".classificaNaoRecebidaGrid").flexigrid({
						colModel : [ {
							display : 'Cota',
							name : 'numeroCota',
							width : 120,
							sortable : true,
							align : 'left'
						}, {
							display : 'Nome',
							name : 'nomePessoa',
							width : 420,
							sortable : true,
							align : 'left'
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
					})
			},
			ClassificacaoGrid : {
				gridName : "classificacaoGrid",
				Url : {
					urlDefault : contextPath + "/distribuicao/excecaoSegmentoParciais/pesquisarProdutosRecebidosPelaCota",
				},
				comments : "Por Classificação - GRID DA DIREITA",
				reload : classificacaoNaoRecebidaController.Grids.Util.reload,
				PreProcess : {
					_default : function(result){
						
						if (result.mensagens) {

							exibirMensagem(result.mensagens.tipoMensagem,
									result.mensagens.listaMensagens);

							$(".grids").hide();

							return result;
						}

						$.each(result.rows, function(index, row) {
							
							var link = '<a href="javascript:;" onclick="excecaoSegmentoParciaisController.excluirExcecaoProduto('+row.cell.idExcecaoProdutoCota+');" style="cursor:pointer">' +
					   	 				   '<img title="Excluir Exceção" src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" />' +
					   	 				   '</a>';
							
							row.cell.acao = link;
						});
						
						$(".grids").show();

						return result;
					}
				},
				init : 
					$(".classificacaoGrid").flexigrid({
						colModel : [ {
							display : 'Cota',
							name : 'cota',
							width : 60,
							sortable : true,
							align : 'left'
						}, {
							display : 'Nome',
							name : 'Nome',
							width : 150,
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
						height : 235
					})
			},
			ClassificaCotaGrid : {
				gridName : "classificaCotaGrid",
				Url : {
					urlDefault : contextPath + "/distribuicao/excecaoSegmentoParciais/pesquisarCotasQueRecebemExcecao"
				},
				comments : "POR COTA - GRID ESQUERDA",
				reload : classificacaoNaoRecebidaController.Grids.Util.reload,
				PreProcess : {
					_default : function(result){
						
						if (result.mensagens) {

							exibirMensagem(result.mensagens.tipoMensagem,
									result.mensagens.listaMensagens);

							$(".grids").hide();

							return result;
						}

						$.each(result.rows, function(index, row) {
							
							var link = '<a href="javascript:;" onclick="excecaoSegmentoParciaisController.excluirExcecaoCota('+row.cell.idExcecaoProdutoCota+');" style="cursor:pointer">' +
					   	 				   '<img title="Excluir Exceção" src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" />' +
					   	 				   '</a>';
							
							row.cell.acao = link;
						});
						
						$(".grids").show();

						return result;
					}
				},
				init : 
					$(".classificaCotaGrid").flexigrid({
						colModel : [ {
							display : 'Classificação',
							name : 'classificacao',
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
					})
				
			},
			classificacaoBGrid : {
				gridName : "classificacaoBGrid",
				Url : {
					urlDefault : contextPath + "/distribuicao/excecaoSegmentoParciais/pesquisarCotasQueNaoRecebemExcecao",
				},
				comments : "POR COTA - GRIDA DA DIREITA",
				reload : classificacaoNaoRecebidaController.Grids.Util.reload,
				PreProcess : {
					_default : function(result){
						
						if (result.mensagens) {

							exibirMensagem(result.mensagens.tipoMensagem,
									result.mensagens.listaMensagens);

							$(".grids").hide();

							return result;
						}

						$.each(result.rows, function(index, row) {
							
							var checkBox = '<input type="checkbox" name="cotaNaoRecebeExcecao" value="' + row.cell.numeroCota + '" />';
							
							row.cell.sel = checkBox;
						});
						
						$(".grids").show();

						return result;
					}
				},
				init : 
					$(".classificacaoBGrid").flexigrid({
						colModel : [ {
							display : 'Classificação',
							name : 'classificacao',
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
	},
	
	excluirClassificacaoNaoRecebidaPorCota : function excluirClassificacaoNaoRecebidaPorCota(id){
		
		var grids = classificacaoNaoRecebidaController.Grids;
		
		$( "#dialog-excluirClassificacaoPorCota" ).dialog({
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
					         
					         params.push({
					        	 name : "filtro.reload",
					        	 value : true
					         });
					         
					         grids.ClassificaNaoRecebidaGrid.reload({
					        	 params : params
					         });
				        	 
					         //grids.ClassificacaoGrid.reload();
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
		$('.porCota').show();
		$('.porClassificacao').hide();
	},
	
	porClassificacao : function porClassificacao(){
		$('.porCota').hide();
		$('.porClassificacao').show();
		
		var grids = classificacaoNaoRecebidaController.Grids,
		params = [];
	
		params = classificacaoNaoRecebidaController.Util.getFiltroByForm('filtroPrincipalClassificacao');
	
		grids.ClassificaNaoRecebidaGrid.reload({
			dataType : 'json',
			params : params
		});
	},
	
}, BaseController);
//@ sourceURL=classificacaoNaoRecebidaController.js