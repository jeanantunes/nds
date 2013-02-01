var excecaoSegmentoParciaisController = $.extend(true, {
	
	
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
	
		excecaoSegmentoParciaisController.Grids = {
				Util : {
					reload : function reload(options) {
						
						// obtendo a url padrão caso o usuário não tenha informado
						options.url = this.Url.urlDefault || options.url;
						
						options.preProcess = options.preProcess || function(result){
							
							if (result.mensagens) {

								exibirMensagem(result.mensagens.tipoMensagem,
										result.mensagens.listaMensagens);

								$(".grids").hide();

								return result;
							}

							$(".grids").show();

							return result;
						};
						
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
				ProdutosNaoRecebidosGrid : { },
				ExcessaoCotaGrid : { },
				ExcessaoNaoRecebidaGrid : { },
				ExcessaoGrid : { }
		};
		
//		excecaoSegmentoParciaisController.preProcess = function(){
//			alert("estou no preprocess");
//		},
		
		excecaoSegmentoParciaisController.Util = {
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
		
		excecaoSegmentoParciaisController.Grids = {
				ProdutosNaoRecebidosGrid : {
					gridName : "excessaoBGrid",
					comments : "Grid (Produtos Não Recebidos) que fica na parte direita da tela que é responsável pela listagem dos produtos para inserção na cota",
					reload : excecaoSegmentoParciaisController.Grids.Util.reload,
					Url : {
						urlDefault : contextPath + "/distribuicao/excecaoSegmentoParciais/pesquisarProdutosNaoRecebidosPelaCota",
					},
					init : 
						$(".excessaoBGrid").flexigrid({
						dataType : 'json',
						colModel : [ {
							display : 'Código',
							name : 'codigoProduto',
							width : 40,
							sortable : true,
							align : 'left'
						},{
							display : 'Produto',
							name : 'nomeProduto',
							width : 90,
							sortable : true,
							align : 'left'
						},{
							display : 'Segmento',
							name : 'nomeSegmento',
							width : 50,
							sortable : true,
							align : 'left'
						},{
							display : 'Fornecedor',
							name : 'nomeFornecedor',
							width : 50,
							sortable : true,
							align : 'left'
						},  {
							display : '',
							name : 'sel',
							width : 20,
							sortable : true,
							align : 'center'
						}],
						usepager : true,
						useRp : true,
						rp : 15,
						showTableToggleBtn : true,
						width : 330,
						height : 235
					})
				},
				ExcessaoCotaGrid : {
					gridName : "excessaoCotaGrid",
					comments : "Grid (Produtos Recebidos) que fica na parte esquerda da tela que é responsável pela listagem de produtos que a cota possui como exceção",
					reload : excecaoSegmentoParciaisController.Grids.Util.reload,
					init : 
						$(".excessaoCotaGrid").flexigrid({
							dataType : 'json',
							colModel : [ {
								display : 'Código',
								name : 'codigo',
								width : 60,
								sortable : true,
								align : 'left'
							}, {
								display : 'Produto',
								name : 'produto',
								width : 170,
								sortable : true,
								align : 'left'
							}, {
								display : 'Usuário',
								name : 'usuario',
								width : 105,
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
							sortname : "codigo",
							sortorder : "asc",
							usepager : true,
							useRp : true,
							rp : 15,
							showTableToggleBtn : true,
							width : 600,
							height : 250
						})
				},
				ExcessaoNaoRecebidaGrid : {
					gridName : "excessaoNaoRecebidaGrid",
					comments : "Grid (Cotas que recebem) que fica na parte esquerda da tela que é responsável pela listagem de cotas onde existe a exceção para o produto",
					reload : excecaoSegmentoParciaisController.Grids.Util.reload,
					init : 
						$(".excessaoNaoRecebidaGrid").flexigrid({
							dataType : 'json',
							colModel : [ {
								display : 'Cota',
								name : 'cota',
								width : 50,
								sortable : true,
								align : 'left'
							},{
								display : 'Status',
								name : 'status',
								width : 60,
								sortable : true,
								align : 'left'
							}, {
								display : 'Nome',
								name : 'nome',
								width : 115,
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
							width : 600,
							height : 250
						})
					
				},
				ExcessaoGrid : {
					gridName : "excessaoGrid",
					comments : "Grid (Cotas que não recebem) que fica na parte direita da tela que é responsável pela listagem de cotas para inserção no produto cota",
					reload : excecaoSegmentoParciaisController.Grids.Util.reload,
					init : 
						$(".excessaoGrid").flexigrid({
							dataType : 'xml',
							colModel : [ {
								display : 'Cota',
								name : 'cota',
								width : 50,
								sortable : true,
								align : 'left'
							},{
								display : 'Status',
								name : 'status',
								width : 60,
								sortable : true,
								align : 'left'
							}, {
								display : 'Nome',
								name : 'Nome',
								width : 130,
								sortable : true,
								align : 'left'
							},  {
								display : '',
								name : 'sel',
								width : 20,
								sortable : true,
								align : 'center'
							}],
							width : 330,
							height : 235
						})
					
				},
		};
	},
	
	porCota : function (){
		$('.porCota').show();
		$('.porExcessao').hide();
		
		var filtroPrincipalCota,
			util = excecaoSegmentoParciaisController.Util,
			ProdutosNaoRecebidosGrid = excecaoSegmentoParciaisController.Grids.ProdutosNaoRecebidosGrid;
		
		filtroPrincipalCota = util.getFiltroByForm("filtroPrincipalCota");
		
		ProdutosNaoRecebidosGrid.reload({
			dataType : 'json',
			params : filtroPrincipalCota,
			preProcess : function(result){
			
				if (result.mensagens) {

					exibirMensagem(result.mensagens.tipoMensagem,
							result.mensagens.listaMensagens);

					$(".grids").hide();

					return result;
				}

				$.each(result.rows, function(index, row) {
					
					var checkBox = '<input type="checkbox" name="produtoNaoRecebido" value="' + row.cell.idProduto + '" />';
					
					row.cell.sel = checkBox;
				});
				
				$(".grids").show();

				return result;
				
			}
		});
		
//		excecaoSegmentoParciaisController.Grid.ExcessaoBGrid.reload({
//			url : contextPath + "/distribuicao/areaInfluenciaGeradorFluxo/pesquisarPorCota",
//			dataType : 'json',
//			params : [{ name : 'text', value : 'UHUUUUUUUUUUUUUUU!!!'}],
//			//workspace : excecaoSegmentoParciaisController.workspace,
//			preProcess : function(resultado){
//				if (resultado.mensagens) {
//
//					exibirMensagem(resultado.mensagens.tipoMensagem,
//							resultado.mensagens.listaMensagens);
//
//					alert("UHUUUUUUUUUUUUUU!");
//					
//					return resultado;
//				}
//			}
//		});
	},
	
	/**
	 * 
	 * @param idProduto
	 * @param isFromModal
	 */
	autoCompletarPorNomeProdutoNaoRecebidoPelaCota : function(idNomeProduto, isFromModal) {
		
		excecaoSegmentoParciaisController.pesquisaRealizada = false;
		
		var util = excecaoSegmentoParciaisController.Util,
			filtro = [],
			ProdutosNaoRecebidosGrid = excecaoSegmentoParciaisController.Grids.ProdutosNaoRecebidosGrid,
			nomeProduto = $(idNomeProduto, excecaoSegmentoParciaisController.workspace).val();
		
		if (nomeProduto && nomeProduto.length > 2) {
			
			filtro = util.getFiltroByForm("filtroPrincipalCota");
			
			filtro.push({
				name : "filtro.nomeProduto", 
				value : nomeProduto
			});
			
			filtro.push({
				 name : "filtro.selectFromAutoComplete", 
				 value : false
			});
			
			$.postJSON(
					contextPath + "/distribuicao/excecaoSegmentoParciais/autoCompletarPorNomeProdutoNaoRecebidoPelaCota",
					filtro,
					function(result) { 
						excecaoSegmentoParciaisController.exibirAutoComplete(result, idNomeProduto);
						
						selectedNomeProduto = $(idNomeProduto).val();
						
						for ( var index in result) {
							if (result[index].value === selectedNomeProduto) {
								
								returnFromController = util.getFiltroByForm("filtroPrincipalCota");
								
								returnFromController.push({
									name : "filtro.nomeProduto", 
									value : selectedNomeProduto
								});
								
								returnFromController.push({
									name : "filtro.selectFromAutoComplete", 
									value : true
								});
								
								ProdutosNaoRecebidosGrid.reload({
											dataType : 'json',
											params : returnFromController
								});
							}
						}
					}
			);
		}
	},
	
	descricaoAtribuida : true,
	
	pesquisaRealizada : false,
	
	intervalo : null,
	
	exibirAutoComplete : function(result, idNomeProduto) {
		
		$(idNomeProduto, excecaoSegmentoParciaisController.workspace).autocomplete({
			source : result,
			focus : function(event, ui) {
				excecaoSegmentoParciaisController.descricaoAtribuida = false;
			},
			close : function(event, ui) {
				excecaoSegmentoParciaisController.descricaoAtribuida = true;
			},
			select : function(event, ui) {
				excecaoSegmentoParciaisController.descricaoAtribuida = true;
			},
			minLength: 4,
			delay : 0,
		});
	},
	
	
	
	
}, BaseController);
//@ sourceURL=excecaoSegmentoParciaisController.js