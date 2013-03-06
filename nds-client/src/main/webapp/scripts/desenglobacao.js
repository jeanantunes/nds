var desenglobacaoController = $.extend(true, {
	
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
	errorCallBack : function errorCallBack(){
		$('#statusCota').val('');
		$('#filtroPrincipalNumeroCota').val('');
		$('#filtroPrincipalNomePessoa').val('');
	},
	
	sucessCallBack : function errorCallBack(result){
		if (result) {
			$('#statusCota').val(result.status);
		}
	},
	
	init : function() {
	
		// #### ASSOCIANDO OS EVENTOS NO DOM ####
		
		// PESQUISAS PRINCIPAIS
		$('#pesquisaPorCota').click(function (){
			desenglobacaoController.porCota();
		});
		
		// FILTRO PRINCIPAL - POR COTA
		$('#filtroPrincipalNumeroCota').change(function (){
			pesquisaCota.pesquisarPorNumeroCota('#filtroPrincipalNumeroCota','#filtroPrincipalNomePessoa', false, desenglobacaoController.sucessCallBack, desenglobacaoController.errorCallBack);
		});
		
		$('#filtroPrincipalNomePessoa').keyup(function (){
			pesquisaCota.autoCompletarPorNome('#filtroPrincipalNomePessoa');
		});
		//pesquisarPorNomeCota
		
		$('#filtroPrincipalNomePessoa').blur(function (){
			pesquisaCota.pesquisarPorNomeCota('#filtroPrincipalNumeroCota','#filtroPrincipalNomePessoa', false, desenglobacaoController.sucessCallBack, desenglobacaoController.errorCallBack);
		});
		
		// EXPORTAÇÃO
		//$('#gerarPDFPorCota').attr('href', contextPath + "/distribuicao/excecaoSegmentoParciais/exportar?fileType=PDF&porCota=true");
		
		//$('#gerarXLSPorCota').attr('href', contextPath + "/distribuicao/excecaoSegmentoParciais/exportar?fileType=XLS&porCota=true");
		
		// URLs usadas para requisições post (Inserção e Deleção)
		desenglobacaoController.Url = {
		},
		
		desenglobacaoController.Grids = {
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
				EnglobadosGrid : {
					reload : {},
					lastParams : []
				},
				DesenglobadosGrid : {
					reload : {},
					lastParams : [],
				},
		};
		
		desenglobacaoController.Util = {
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
		
		desenglobacaoController.Grids = {
			EnglobadosGrid : {
				gridName : "englobadosGrid",
				Url : {
					urlDefault : contextPath + "/distribuicao/desenglobacao/pesquisaPrincipal",
				},
				comments : "GRID PRINCIPAL DA TELA DESENGLOBAÇÃO",
				reload : desenglobacaoController.Grids.Util.reload,
				PreProcess : {
					_default : function(result){
						
						if (result.mensagens) {

							exibirMensagem(result.mensagens.tipoMensagem,
									result.mensagens.listaMensagens);

							$(".grids").hide();

							return result;
						}

						$.each(result.rows, function(index, row) {
							// onclick="desenglobacaoController.excluirExcecaoProduto('+row.cell.idExcecaoProdutoCota+');"
							var link = '<a href="javascript:;" style="cursor:pointer">' +
					   	 				   '<img title="Excluir Exceção" src="' + contextPath + '/images/ico_editar.gif" hspace="5" border="0px" />' +
					   	 				   '</a>';
							
							row.cell.acao = link;
						});
						
						$(".grids").show();

						return result;
					}
				},
				init : 
					$(".englobadosGrid").flexigrid({
						colModel : [ {
							display : 'Cota',
							name : 'cota',
							width : 60,
							sortable : true,
							align : 'left'
						},{
							display : 'Nome',
							name : 'nome',
							width : 220,
							sortable : true,
							align : 'left'
						},{
							display : 'Tipo de PDV',
							name : 'tipoPdv',
							width : 220,
							sortable : true,
							align : 'left'
						},{
							display : 'Usuário',
							name : 'usuario',
							width : 160,
							sortable : true,
							align : 'left'
						},{
							display : 'Data Alteração',
							name : 'dtAlteracao',
							width : 100,
							sortable : true,
							align : 'center'
						},{
							display : 'Hora',
							name : 'hora',
							width : 50,
							sortable : true,
							align : 'center'
						},{
							display : 'Ação',
							name : 'acao',
							width : 40,
							sortable : true,
							align : 'center'
						}],
						sortname : "cota",
						sortorder : "asc",
						usepager : true,
						useRp : true,
						rp : 15,
						showTableToggleBtn : true,
						width : 960,
						height : 255
					})
				},
			DesenglobadosGrid : {
				gridName : "desenglobadosGrid",
				Url : {
					urlDefault : contextPath + "/distribuicao/desenglobacao/englobarCotas",
				},
				comments : "Grid para englobar cotas",
				reload : desenglobacaoController.Grids.Util.reload,
				PreProcess : {
					_default : function(result){
						
						if (result.mensagens) {

							exibirMensagem(result.mensagens.tipoMensagem,
									result.mensagens.listaMensagens);

							$(".grids").hide();

							return result;
						}

						$(".grids").show();

						return result;
					}
				},
				init : 
					$(".desenglobadosGrid").flexigrid({
						colModel : [ {
							display : 'Cota',
							name : 'cota',
							width : 60,
							sortable : true,
							align : 'left'
						}, {
							display : 'Nome',
							name : 'nome',
							width : 200,
							sortable : true,
							align : 'left'
						}, {
							display : '% da Cota',
							name : 'percDaCota',
							width : 85,
							sortable : true,
							align : 'center'
						}, {
							display : 'Data Início',
							name : 'dtInicio',
							width : 85,
							sortable : true,
							align : 'center'
						}, {
							display : 'Data Final',
							name : 'dtFinal',
							width : 85,
							sortable : true,
							align : 'center'
						}],
						width : 600,
						height : 240
					})
			}
		};
	},
	
	porCota : function (){
		var filtroPrincipalCota = [],
			util = desenglobacaoController.Util,
			grids = desenglobacaoController.Grids;
		
		filtroPrincipalCota = util.getFiltroByForm("filtroPrincipal");
		
		grids.EnglobadosGrid.reload({
			dataType : 'json',
			params : filtroPrincipalCota,
		});
		
	},
	
	
}, BaseController);
//@ sourceURL=desenglobacaoController.js