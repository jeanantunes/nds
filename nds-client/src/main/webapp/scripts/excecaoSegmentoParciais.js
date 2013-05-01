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
		
		var autoComplete = new AutoCompleteController(excecaoSegmentoParciaisController.workspace);
	
		// #### ASSOCIANDO OS EVENTOS NO DOM ####
		// PESQUISAS PRINCIPAIS
		
		$('#tipoExcecaoSegmento').click(function (){
			excecaoSegmentoParciaisController.limparCampos();
		});
		
		$('#radio3').click(function (){
			excecaoSegmentoParciaisController.limparCampos();
		});
		
		$('#pesquisaPorCota').click(function (){
			excecaoSegmentoParciaisController.porCota();
		});
		
		$('#pesquisaPorExcecao').click(function (){
			excecaoSegmentoParciaisController.porExcecao();
		});
		
		// FILTRO PRINCIPAL - POR COTA
		$('#numeroCotaFiltroPrincipal').blur(function (){
			autoComplete.pesquisarPorCodigo("/cadastro/cota/pesquisarPorNumeroAutoComplete",'#numeroCotaFiltroPrincipal','#nomeCotaFiltroPrincipal');
		});
		
		$('#nomeCotaFiltroPrincipal').keyup(function (){
			autoComplete.autoCompletar("/cadastro/cota/autoCompletarPorNomeAutoComplete",'#numeroCotaFiltroPrincipal','#nomeCotaFiltroPrincipal');
		});
		
		autoComplete.limparCampoOnChange('#nomeCotaFiltroPrincipal', new Array('#numeroCotaFiltroPrincipal'));
		
		$('#codigoProduto').blur(function (){
			
			autoComplete.pesquisarPorCodigo("/produto/pesquisarPorCodigoProdutoAutoComplete",'#codigoProduto','#nomeProduto');
		});
		
		autoComplete.limparCampoOnChange('#nomeProduto', new Array('#codigoProduto'));
		
		$('#codigoProduto').keyup(function (){
			
			autoComplete.autoCompletar("/cadastro/cota/autoCompletarPorCodigoProdutoAutoComplete",'#codigoProduto','#nomeProduto', 5);
		});
		
		$('#nomeProduto').keyup(function (){
			autoComplete.autoCompletar("/produto/autoCompletarPorNomeProdutoAutoComplete", '#codigoProduto', '#nomeProduto');
		});
		
		// INSERIR A EXCEÇÃO
		$('#inserirExcecaoDeProdutos').click(function (){
			excecaoSegmentoParciaisController.inserirExcecaoDeProdutos();
		});
		
		$('#inserirCotaNaExcecao').click(function (){
			excecaoSegmentoParciaisController.inserirCotaNaExcecao();
		});
		
		//FILTRO PRINCIPAL - POR PRODUTO (EXCEÇÃO)
		$('#nomeProdutoPrincipal').keyup(function (){
			autoComplete.autoCompletar("/distribuicao/excecaoSegmentoParciais/autoCompletarProduto",
					'#codigoProdutoPrincipal','#nomeProdutoPrincipal', 2,
					function(item) {
						
						if (item) {
							$('#segmentoProdutoPrincipal').val(item.chave.nomeFantasia || item.chave.razaoSocial);
							$('#fornecedorPrincipal').val(item.chave.tipoSegmentoProduto || "Segmento Não Informado");
						}
					}
			);
		});
		
		autoComplete.limparCampoOnChange('#nomeProdutoPrincipal', new Array('#codigoProdutoPrincipal','#fornecedorPrincipal','#segmentoProdutoPrincipal'));
		
		//pesquisarPorCodigoProduto
		$('#codigoProdutoPrincipal').blur(function (){
			
			autoComplete.pesquisarPorCodigo("/distribuicao/excecaoSegmentoParciais/pesquisarPorCodigoProdutoAutoComplete",
					'#codigoProdutoPrincipal','#nomeProdutoPrincipal',
					function(result) {
						if (result) {
							$('#codigoProdutoPrincipal').val(result[0].codigo);
							$('#nomeProdutoPrincipal').val(result[0].nome);
							$('#fornecedorPrincipal').val(result[1].nomeFantasia || result[1].razaoSocial);
							$('#segmentoProdutoPrincipal').val(result[2].descricao || "Segmento Não Informado");
						}
					}
			);
			
		});
		
		autoComplete.limparCampoOnChange('#codigoProdutoPrincipal', new Array('#nomeProdutoPrincipal','#fornecedorPrincipal','#segmentoProdutoPrincipal'));
		
		$('#codigoProdutoPrincipal').keyup(function (){
			autoComplete.autoCompletar("/distribuicao/excecaoSegmentoParciais/autoCompletarProduto",
					'#codigoProdutoPrincipal','#nomeProdutoPrincipal', 5,
					function(item) {
						
						if (item) {
							$('#segmentoProdutoPrincipal').val(item.chave.nomeFantasia || item.chave.razaoSocial);
							$('#fornecedorPrincipal').val(item.chave.tipoSegmentoProduto || "Segmento Não Informado");
						}
					}
			);
		});
		
		$('#cotasQueNaoRecebemNumeroCota').blur(function (){
			autoComplete.pesquisarPorCodigo("/cadastro/cota/pesquisarPorNumeroAutoComplete",'#cotasQueNaoRecebemNumeroCota','#cotasQueNaoRecebemNomeCota');
		});
		
		autoComplete.limparCampoOnChange('#cotasQueNaoRecebemNumeroCota', new Array('#cotasQueNaoRecebemNomeCota'));
		
		$('#cotasQueNaoRecebemNomeCota').keyup(function (){
			autoComplete.autoCompletar("/cadastro/cota/autoCompletarPorNomeAutoComplete",'#cotasQueNaoRecebemNumeroCota','#cotasQueNaoRecebemNomeCota');
		});
		
		autoComplete.limparCampoOnChange('#cotasQueNaoRecebemNomeCota', new Array('#cotasQueNaoRecebemNumeroCota'));
		
		$('#pesquisarProdutosParciaisNaoRecebidos').click(function (){
			if (($('#idProduto').val() != '') && ($('#nomeProduto').val() != '')) {
				excecaoSegmentoParciaisController.pesquisarProdutoNaoRecebidoPeloNomeOuCodigo({
					codigo:$('#codigoProduto').val(), nome:$('#nomeProduto').val()});
			} else {
				exibirMensagem('WARNING', ["Informe pelo menos um filtro para a pesquisa."]);
			}
		});
		
		$('#pesquisarCotaQueNaoRecebem').click(function (){
			if ($('#cotasQueNaoRecebemNumeroCota').val() != '') {
				excecaoSegmentoParciaisController.pesquisarCotasQueNaoRecebemExcessaoPeloNomeOuCodigo ({
					codigo: $('#cotasQueNaoRecebemNumeroCota').val()});
			} else {
				exibirMensagem('WARNING', ["Informe pelo menos um filtro para a pesquisa."]);
			}
		});
		
		// EXPORTAÇÃO
		$('#gerarPDFPorExcecao').attr('href', contextPath + "/distribuicao/excecaoSegmentoParciais/exportar?fileType=PDF&porCota=false");
		
		$('#gerarXLSPorExcecao').attr('href', contextPath + "/distribuicao/excecaoSegmentoParciais/exportar?fileType=XLS&porCota=false");
		
		$('#gerarPDFPorCota').attr('href', contextPath + "/distribuicao/excecaoSegmentoParciais/exportar?fileType=PDF&porCota=true");
		
		$('#gerarXLSPorCota').attr('href', contextPath + "/distribuicao/excecaoSegmentoParciais/exportar?fileType=XLS&porCota=true");
		
		// URLs usadas para requisições post (Inserção e Deleção)
		excecaoSegmentoParciaisController.Url = {
			inserirExcecaoDeProdutos : contextPath + "/distribuicao/excecaoSegmentoParciais/inserirExcecaoProdutoNaCota",
			inserirCotaNaExcecao : contextPath + "/distribuicao/excecaoSegmentoParciais/inserirCotaNaExcecao ",
			excluirExcecaoProduto : contextPath + "/distribuicao/excecaoSegmentoParciais/excluirExcecaoProduto",
			pesquisarProduto : contextPath + "/distribuicao/excecaoSegmentoParciais/pesquisarPorCodigoProduto",
			autoCompletarPorNomeProdutoNaoRecebidoPelaCota : contextPath + "/distribuicao/excecaoSegmentoParciais/autoCompletarPorNomeProdutoNaoRecebidoPelaCota",
			autoCompletarPorNomeCotaQueNaoRecebeExcecao : contextPath + "/distribuicao/excecaoSegmentoParciais/autoCompletarPorNomeCotaQueNaoRecebeExcecao"
		},
		
		excecaoSegmentoParciaisController.Grids = {
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
				ProdutosNaoRecebidosGrid : {
					reload : {},
					lastParams : [],
				},
				ProdutosRecebidosGrid : {
					reload : {},
					lastParams : []
				},
				CotasQueRecebemExcecaoGrid : {
					reload : {},
					lastParams : []
				},
				CotasQueNaoRecebemExcecaoGrid : {
					reload : {},
					lastParams : []
				}
		};
		
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
				Url : {
					urlDefault : contextPath + "/distribuicao/excecaoSegmentoParciais/pesquisarProdutosNaoRecebidosPelaCota",
				},
				comments : "Grid (Produtos Não Recebidos) que fica na parte direita da tela que é responsável pela listagem dos produtos para inserção na cota",
				reload : excecaoSegmentoParciaisController.Grids.Util.reload,
				PreProcess : {
					_default : function(result){
						
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
				},
				init : 
					$(".excessaoBGrid").flexigrid({
					colModel : [ {
						display : 'Código',
						name : 'codigoProduto',
						width : 60,
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
						width : 115,
						sortable : true,
						align : 'left'
					},{
						display : 'Fornecedor',
						name : 'nomeFornecedor',
						width : 60,
						sortable : true,
						align : 'left'
					},  {
						display : '',
						name : 'sel',
						width : 20,
						sortable : true,
						align : 'center'
					}],
//						usepager : true,
//						useRp : true,
//						rp : 15,
//						showTableToggleBtn : true,
					height : 235
				})
			},
			ProdutosRecebidosGrid : {
				gridName : "excessaoCotaGrid",
				Url : {
					urlDefault : contextPath + "/distribuicao/excecaoSegmentoParciais/pesquisarProdutosRecebidosPelaCota",
				},
				comments : "Grid (Produtos Recebidos) que fica na parte esquerda da tela que é responsável pela listagem de produtos que a cota possui como exceção",
				reload : excecaoSegmentoParciaisController.Grids.Util.reload,
				PreProcess : {
					_default : function(result){
						
						if (result.mensagens) {

							exibirMensagem(result.mensagens.tipoMensagem,
									result.mensagens.listaMensagens);

							$(".grids").hide();

							return result;
						}

						// esconder botões de impressão
						if (result.rows.length === 0) {
							$('#gerarXLSPorCota').hide();
							$('#gerarPDFPorCota').hide();
							
						}else{
							$('#gerarXLSPorCota').show();
							$('#gerarPDFPorCota').show();
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
					$(".excessaoCotaGrid").flexigrid({
						colModel : [ {
							display : 'Código',
							name : 'codigoProduto',
							width : 60,
							sortable : true,
							align : 'left'
						}, {
							display : 'Produto',
							name : 'nomeProduto',
							width : 90,
							sortable : true,
							align : 'left'
						}, {
							display : 'Usuário',
							name : 'nomeUsuario',
							width : 105,
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
						sortname : "codigoProduto",
						sortorder : "asc",
						usepager : true,
						useRp : true,
						rp : 15,
						showTableToggleBtn : true,
						height : 250
					})
			},
			CotasQueRecebemExcecaoGrid : {
				gridName : "excessaoNaoRecebidaGrid",
				Url : {
					urlDefault : contextPath + "/distribuicao/excecaoSegmentoParciais/pesquisarCotasQueRecebemExcecao"
				},
				comments : "Grid (Cotas que recebem) que fica na parte esquerda da tela que é responsável pela listagem de cotas onde existe a exceção para o produto",
				reload : excecaoSegmentoParciaisController.Grids.Util.reload,
				PreProcess : {
					_default : function(result){
						
						if (result.mensagens) {

							exibirMensagem(result.mensagens.tipoMensagem,
									result.mensagens.listaMensagens);

							$(".grids").hide();

							return result;
						}

						// esconder botões de impressão
						if (result.rows.length === 0) {
							$('#gerarXLSPorExcecao').hide();
							$('#gerarPDFPorExcecao').hide();
							
						}else{
							$('#gerarXLSPorExcecao').show();
							$('#gerarPDFPorExcecao').show();
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
					$(".excessaoNaoRecebidaGrid").flexigrid({
						dataType : 'json',
						colModel : [ {
							display : 'Cota',
							name : 'numeroCota',
							width : 50,
							sortable : true,
							align : 'left'
						},{
							display : 'Status',
							name : 'statusCota',
							width : 60,
							sortable : true,
							align : 'left'
						}, {
							display : 'Nome',
							name : 'nomePessoa',
							width : 115,
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
						sortname : "nomePessoa",
						sortorder : "asc",
						usepager : true,
						useRp : true,
						rp : 15,
						showTableToggleBtn : true,
						height : 250
					})
				
			},
			CotasQueNaoRecebemExcecaoGrid : {
				gridName : "excessaoGrid",
				Url : {
					urlDefault : contextPath + "/distribuicao/excecaoSegmentoParciais/pesquisarCotasQueNaoRecebemExcecao",
				},
				comments : "Grid (Cotas que não recebem) que fica na parte direita da tela que é responsável pela listagem de cotas para inserção no produto cota",
				reload : excecaoSegmentoParciaisController.Grids.Util.reload,
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
					$(".excessaoGrid").flexigrid({
						colModel : [ {
							display : 'Cota',
							name : 'numeroCota',
							width : 50,
							sortable : true,
							align : 'left'
						},{
							display : 'Status',
							name : 'statusCota',
							width : 60,
							sortable : true,
							align : 'left'
						}, {
							display : 'Nome',
							name : 'nomePessoa',
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
						height : 235
					})
				
			},
		};
		
		$(document).ready(function(){
			
			excecaoSegmentoParciaisController.filtroPorCota();
			focusSelectRefField($("#radio", excecaoSegmentoParciaisController.workspace));
			$("#radio", excecaoSegmentoParciaisController.workspace).attr("checked", true);
			
//			$(document.body).keydown(function(e) {
//				
//				if(keyEventEnterAux(e)){
//					excecaoSegmentoParciaisController.porCota();
//				}
//				
//				return true;
//			});
		});
		
	},
	
	excluirExcecaoProduto : function excluirExcecaoProduto(excecaoId){
		
		var grids = excecaoSegmentoParciaisController.Grids;
		
		$( "#dialog-excluirExcecao" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					
					$.postJSON(
						excecaoSegmentoParciaisController.Url.excluirExcecaoProduto,
						[{name : "id" , value : excecaoId}],
						function(result){
							 tipoMensagem = result.tipoMensagem;
					         listaMensagens = result.listaMensagens;
					         
					         if (tipoMensagem && listaMensagens) {
					        	 exibirMensagem(tipoMensagem, listaMensagens);
					         }
							
					         params = excecaoSegmentoParciaisController.Util.getFiltroByForm("filtroPrincipalCota");
					         
					         params.push({
					        	 name : "filtro.reload",
					        	 value : true
					         });
					         
					         params.push({
									name : "filtro.excecaoSegmento",
									value : $('#tipoExcecaoSegmento').is(':checked')
							 });
					         
					         grids.ProdutosRecebidosGrid.reload({
					        	 params : params
					         });
				        	 
					         grids.ProdutosNaoRecebidosGrid.reload();
						}
					);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},
	
	excluirExcecaoCota : function excluirExcecaoCota(excecaoId){
		
		var grids = excecaoSegmentoParciaisController.Grids;
		
		$( "#dialog-excluirCotaDaExcecao" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					
					$.postJSON(
						excecaoSegmentoParciaisController.Url.excluirExcecaoProduto,
						[{name : "id" , value : excecaoId}],
						function(result){
							 tipoMensagem = result.tipoMensagem;
					         listaMensagens = result.listaMensagens;
					         
					         if (tipoMensagem && listaMensagens) {
					        	 exibirMensagem(tipoMensagem, listaMensagens);
					         }
							
					         params = excecaoSegmentoParciaisController.Util.getFiltroByForm("filtroPrincipalProduto");
					         
					         params.push({
					        	 name : "filtro.reload",
					        	 value : true
					         });
					         
					         params.push({
									name : "filtro.excecaoSegmento",
									value : $('#tipoExcecaoSegmento').is(':checked')
							 });
					         
					         grids.CotasQueRecebemExcecaoGrid.reload({
					        	 params : params
					         });
				        	 
					         grids.CotasQueNaoRecebemExcecaoGrid.reload();
						}
					);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},
	
	inserirExcecaoDeProdutos : function inserirExcecaoDeProdutos(){
		
		var params = excecaoSegmentoParciaisController.Util.getFiltroByForm('filtroPrincipalCota'),
			grids = excecaoSegmentoParciaisController.Grids;
		
		$( "#dialog-incluirExcecao" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );

					$("input[type=checkbox][name='produtoNaoRecebido']:checked").each(function(){
						params.push({
							name : "listaIdProduto",
							value : this.value
						});
					});
					
					params.push({
						name : "filtro.excecaoSegmento",
						value : $('#tipoExcecaoSegmento').is(':checked')
					});
					
					// jQuery.post( url [, data ] [, success(data, textStatus, jqXHR) ] [, dataType ] )
					$.postJSON(
						excecaoSegmentoParciaisController.Url.inserirExcecaoDeProdutos,
						params,
						function(result){
							 tipoMensagem = result.tipoMensagem;
					         listaMensagens = result.listaMensagens;
					         
					         if (tipoMensagem && listaMensagens) {
					        	 exibirMensagem(tipoMensagem, listaMensagens);
					         }
							
					         params = excecaoSegmentoParciaisController.Util.getFiltroByForm("filtroPrincipalCota");
					         
					         params.push({
									name : "filtro.excecaoSegmento",
									value : $('#tipoExcecaoSegmento').is(':checked')
					         });
					         
					         params.push({
					        	 name : "filtro.reload",
					        	 value : true
					         });
					         
//					         grids.ProdutosNaoRecebidosGrid.reload({
//					        	 params : params
//					         });
					         $('.excessaoBGrid').find('[id^=row]').remove();
					         
					         grids.ProdutosRecebidosGrid.reload({
					        	 params : params
					         });
						}
					);
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
		
	},
	
	inserirCotaNaExcecao : function inserirCotaNaExcecao(){
		
		var params = excecaoSegmentoParciaisController.Util.getFiltroByForm('filtroPrincipalProduto'),
			grids = excecaoSegmentoParciaisController.Grids;
		
		$( "#dialog-incluirCotaNaExcecao" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );

					$("input[type=checkbox][name='cotaNaoRecebeExcecao']:checked").each(function(){
						params.push({
							name : "listaNumeroCota",
							value : this.value
						});
					});
					
					params.push({
						name : "filtro.excecaoSegmento",
						value : $('#tipoExcecaoSegmento').is(':checked')
					});
					
					// jQuery.post( url [, data ] [, success(data, textStatus, jqXHR) ] [, dataType ] )
					$.postJSON(
						excecaoSegmentoParciaisController.Url.inserirCotaNaExcecao,
						params,
						function(result){
							 tipoMensagem = result.tipoMensagem;
					         listaMensagens = result.listaMensagens;
					         
					         if (tipoMensagem && listaMensagens) {
					        	 exibirMensagem(tipoMensagem, listaMensagens);
					         }
							
					         params = excecaoSegmentoParciaisController.Util.getFiltroByForm("filtroPrincipalProduto");
					         
					         params.push({
									name : "filtro.excecaoSegmento",
									value : $('#tipoExcecaoSegmento').is(':checked')
					         });
					         
					         params.push({
					        	 name : "filtro.reload",
					        	 value : true
					         });
					         
//					         grids.CotasQueRecebemExcecaoGrid.reload({
//					        	 params : params
//					         });
					         $('.excessaoGrid').find('[id^=row]').remove();
					         
					         grids.CotasQueNaoRecebemExcecaoGrid.reload({
					        	 params : params
					         });
						}
					);
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
		
	},
	
	porCota : function (){
		$('#excecaoSegmentoParciais_porCota').show();
		$('#excecaoSegmentoParciais_porExcessao').hide();
		
		var filtroPrincipalCota = [],
			util = excecaoSegmentoParciaisController.Util,
			grids = excecaoSegmentoParciaisController.Grids;
		
		filtroPrincipalCota = util.getFiltroByForm("filtroPrincipalCota");
		
		filtroPrincipalCota.push({
			name : "filtro.excecaoSegmento",
			value : $('#tipoExcecaoSegmento').is(':checked')
		});
		
		grids.ProdutosRecebidosGrid.reload({
			dataType : 'json',
			params : filtroPrincipalCota,
		});
		// comentado para não carregar a grid da direita automaticamente
//		grids.ProdutosNaoRecebidosGrid.reload({
//			dataType : 'json',
//			params : filtroPrincipalCota,
//		});
	},
	
	porExcecao : function porExcessao(){
		$('#excecaoSegmentoParciais_porCota').hide();
		$('#excecaoSegmentoParciais_porExcessao').show();
		
		var filtroPrincipalProduto = [],
			util = excecaoSegmentoParciaisController.Util,
			grids = excecaoSegmentoParciaisController.Grids;
		
		filtroPrincipalProduto = util.getFiltroByForm("filtroPrincipalProduto");
		
		filtroPrincipalProduto.push({
			name : "filtro.excecaoSegmento",
			value : $('#tipoExcecaoSegmento').is(':checked')
		});
		
		grids.CotasQueRecebemExcecaoGrid.reload({
			dataType : 'json',
			params : filtroPrincipalProduto
		});
		// comentado para não carregar a grid da direita automaticamente
//		grids.CotasQueNaoRecebemExcecaoGrid.reload({
//			dataType : 'json',
//			params : filtroPrincipalProduto
//		});
		
	},
	
	// AUTO COMPLETE DOS CAMPOS COM O PRODUTO
	pesquisarProduto : function pesquisarProduto(produto,idCodigoProduto, idNomeProduto, idFornecedor, idSegmento){
		
		if (produto.nome.length > 0 || produto.codigo.length > 0) {
			$.postJSON(
				excecaoSegmentoParciaisController.Url.pesquisarProduto,
				[{name : "nomeProduto", value : produto.nome}, {name : "codigoProduto", value : produto.codigo}],
				function(result){
					  	if (result.length > 0) {
					  	$(idCodigoProduto).val(result[0].codigo);
						$(idNomeProduto).val(result[0].nome);
						$(idFornecedor).val(result[1].nomeFantasia || result[1].razaoSocial);
						$(idSegmento).val(result[2].descricao || "Segmento Não Informado");
					}
				},
				function(result){
					$('#codigoProdutoPrincipal').val('');
				});
		}else {
			$(idNomeProduto).val('');	
			$(idFornecedor).val('');
			$(idSegmento).val('');
		}
	},
	
	// Produtos não Recebidos - Secundário
	pesquisarCotasQueNaoRecebemExcessaoPeloNomeOuCodigo : function pesquisarProduto(cota){
		
			$("#codigoProduto").val('');
			$("#nomeProduto").val('');
			$("#cotasQueNaoRecebemNumeroCota").val('');
			$("#cotasQueNaoRecebemNomeCota").val('');
		
			var returnFromController = [],
				util = excecaoSegmentoParciaisController.Util,
				CotasQueNaoRecebemExcecaoGrid = excecaoSegmentoParciaisController.Grids.CotasQueNaoRecebemExcecaoGrid;
			
			returnFromController = util.getFiltroByForm("filtroPrincipalProduto");
			
			if (cota.codigo) {
				returnFromController.push({
					name : "filtro.cotaDto.numeroCota", 
					value : cota.codigo
				});
			}
	
			if (cota.nome) {
				returnFromController.push({
					name : "filtro.cotaDto.nomePessoa", 
					value : cota.nome
				});
			}
			
			returnFromController.push({
				name : "filtro.excecaoSegmento",
				value : $('#tipoExcecaoSegmento').is(':checked')
			});
			
			CotasQueNaoRecebemExcecaoGrid.reload({
						dataType : 'json',
						params : returnFromController
			});
	},
	
	// Produtos não Recebidos - Secundário
	pesquisarProdutoNaoRecebidoPeloNomeOuCodigo : function pesquisarProduto(produto){
			
			$("#codigoProduto").val('');
			$("#nomeProduto").val('');
			$("#cotasQueNaoRecebemNumeroCota").val('');
			$("#cotasQueNaoRecebemNomeCota").val('');
		
			var util = excecaoSegmentoParciaisController.Util,
			ProdutosNaoRecebidosGrid = excecaoSegmentoParciaisController.Grids.ProdutosNaoRecebidosGrid;
			
			returnFromController = util.getFiltroByForm("filtroPrincipalCota");
			
			returnFromController.push({
				name : "filtro.produtoDto.codigoProduto", 
				value : produto.codigo || ''
			});
	
			returnFromController.push({
				name : "filtro.produtoDto.nomeProduto", 
				value : produto.nome || ''
			});
			
			returnFromController.push({
				name : "filtro.excecaoSegmento",
				value : $('#tipoExcecaoSegmento').is(':checked')
			});
			
			ProdutosNaoRecebidosGrid.reload({
						dataType : 'json',
						params : returnFromController
			});
	},
	
	/**
	 * 
	 * @param idProduto
	 * @param isFromModal
	 */
	autoCompletarPorNomeProdutoNaoRecebidoPelaCota : function(idNomeProduto, isFromModal) {
		
		excecaoSegmentoParciaisController.pesquisaRealizada = false;
		pesquisaProduto.pesquisaRealizada = false;
		
		var util = excecaoSegmentoParciaisController.Util,
			filtro = [],
			nomeProduto = $(idNomeProduto, excecaoSegmentoParciaisController.workspace).val();
		if (nomeProduto && nomeProduto.length > 2) {
			
			filtro = util.getFiltroByForm("filtroPrincipalCota");
			
			filtro.push({
				name : "filtro.produtoDto.nomeProduto", 
				value : nomeProduto
			});
			
			filtro.push({
				 name : "filtro.autoComplete", 
				 value : true
			});
			
			$.postJSON(
					excecaoSegmentoParciaisController.Url.autoCompletarPorNomeProdutoNaoRecebidoPelaCota,
				filtro,
				function(result) { 
					excecaoSegmentoParciaisController.exibirAutoComplete(result, idNomeProduto);
				}
			);
		}
	},
	
	autoCompletarPorNomeCotaQueNaoRecebeExcecao : function(idNomeCota) {
		
		excecaoSegmentoParciaisController.pesquisaRealizada = false;
		
		var util = excecaoSegmentoParciaisController.Util,
			filtro = [],
			nomePessoa = $(idNomeCota, excecaoSegmentoParciaisController.workspace).val();
		if (nomePessoa && nomePessoa.length > 2) {
			
			filtro = util.getFiltroByForm("filtroPrincipalProduto");
			
			filtro.push({
				name : "filtro.cotaDto.nomePessoa", 
				value : nomePessoa
			});
			
			filtro.push({
				 name : "filtro.autoComplete", 
				 value : true
			});
			
			$.postJSON(
				excecaoSegmentoParciaisController.Url.autoCompletarPorNomeCotaQueNaoRecebeExcecao,
				filtro,
				function(result) { 
					excecaoSegmentoParciaisController.exibirAutoComplete(result, idNomeCota);
				}
			);
		}
	},
	
	descricaoAtribuida : true,
	
	pesquisaRealizada : false,
	
	intervalo : null,
	
	exibirAutoComplete : function(result, idCaixaTexto) {
		
		$(idCaixaTexto, excecaoSegmentoParciaisController.workspace).autocomplete({
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
	
	filtroPorCota : function filtroPorCota(){
		$('#excecaoSegmentoParciais_filtroPorCota').show();
		$('#excecaoSegmentoParciais_filtroPorProduto').hide();
		$('#excecaoSegmentoParciais_porExcessao').hide();
		
		$('#numeroCotaFiltroPrincipal').val('');
	    $('#nomeCotaFiltroPrincipal').val('');
	    $('.excessaoGrid').find('[id^=row]').remove();
	    $('.excessaoBGrid').find('[id^=row]').remove();
	},
	filtroPorProduto: function filtroPorProduto(){
		$('#excecaoSegmentoParciais_filtroPorCota').hide();
		$('#excecaoSegmentoParciais_filtroPorProduto').show();
		$('#excecaoSegmentoParciais_porCota').hide();
		
		$('#codigoProdutoPrincipal').val('');
	    $('#nomeProdutoPrincipal').val('');
	    $('#fornecedorPrincipal').val('');
	    $('#segmentoProdutoPrincipal').val('');
	    $('.excessaoGrid').find('[id^=row]').remove();
	    $('.excessaoBGrid').find('[id^=row]').remove();
	},
	
	limparCampos: function limparCampos() {
		$('#excecaoSegmentoParciais_filtroPorProduto').hide();
		$('#excecaoSegmentoParciais_porCota').hide();
	    $('#numeroCotaFiltroPrincipal').val('');
	    $('#nomeCotaFiltroPrincipal').val('');
	    $('#codigoProdutoPrincipal').val('');
	    $('#nomeProdutoPrincipal').val('');
	    $('#fornecedorPrincipal').val('');
	    $('#segmentoProdutoPrincipal').val('');
	    $('#radio').check();
	    excecaoSegmentoParciaisController.filtroPorCota();
	}
	
}, BaseController);

//@ sourceURL=excecaoSegmentoParciaisController.js
