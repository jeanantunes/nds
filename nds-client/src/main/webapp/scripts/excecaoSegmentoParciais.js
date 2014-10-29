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
		
		$('#limparExcecaoDeProdutos').click(function (){
			excecaoSegmentoParciaisController.reload_ProdutosNaoRecebidosGrid();
		});
		
		
		$('#limparCotaNaExcecao').click(function (){
			excecaoSegmentoParciaisController.reload_CotasQueNaoRecebemExcecaoGrid();
		});
		
		$('#radio3').click(function (){
			excecaoSegmentoParciaisController.limparCampos();
		});
		
		$('#2005_pesquisaPorCota').click(function (){
			excecaoSegmentoParciaisController.porCota();
		});
		
		$('#pesquisaPorExcecao').click(function (){
			
			if($('#codigoProdutoPrincipal').val() == '' || $('#codigoProdutoPrincipal').val() == null )
			{
				alert('Você ainda não escolheu um produto');
			}
			else
			{
				excecaoSegmentoParciaisController.porExcecao();
			}	
		});
		
		// FILTRO PRINCIPAL - POR COTA
		$('#numeroCotaFiltroPrincipal').blur(function (){
			autoComplete.pesquisarPorCodigo("/cadastro/cota/pesquisarPorNumeroAutoComplete",'#numeroCotaFiltroPrincipal','#nomeCotaFiltroPrincipal');
		});
		
		$('#nomeCotaFiltroPrincipal').keyup(function (){
			autoComplete.autoCompletar("/cadastro/cota/autoCompletarPorNomeAutoComplete",'#numeroCotaFiltroPrincipal','#nomeCotaFiltroPrincipal');
		});
		
		autoComplete.limparCampoOnChange('#nomeCotaFiltroPrincipal', new Array('#numeroCotaFiltroPrincipal'));
		
		$('#2005_codigoProduto').blur(function (){
			
			autoComplete.pesquisarPorCodigo("/produto/pesquisarPorCodigoProdutoAutoComplete",'#2005_codigoProduto','#2005_nomeProduto');
		});
		
		autoComplete.limparCampoOnChange('#2005_nomeProduto', new Array('#2005_codigoProduto'));
		
		$('#2005_nomeProduto').keyup(function (){
			autoComplete.autoCompletar("/produto/autoCompletarPorNomeProdutoAutoComplete", '#2005_codigoProduto', '#2005_nomeProduto');
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
					
						if (item != null) {				
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
							
							if($('#codigoProdutoPrincipal').val().length == 6){
								$('#codigoProdutoPrincipal').val(result[0].codigoICD);
							}else{
								$('#codigoProdutoPrincipal').val(result[0].codigo);
							}
							
							$('#nomeProdutoPrincipal').val(result[0].nome);
							$('#fornecedorPrincipal').val(result[1].nomeFantasia || result[1].razaoSocial);
							$('#segmentoProdutoPrincipal').val(result[2].descricao || "Segmento Não Informado");
							$('#2005_comboClassificacao').val(result[3]);
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
			if ($('#2005_codigoProduto').val() == ''  ||  $('#comboClassificacaoProdNRec').val() == '') {
				exibirMensagem('WARNING', ["Informe o filtro para a pesquisa."]);
			} else {
				excecaoSegmentoParciaisController.pesquisarProdutoNaoRecebidoPeloNomeOuCodigo({
					codigo: $('#2005_codigoProduto').val(), 
					classifProduto: $('#comboClassificacaoProdNRec').val()
				});
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
								$(".areaBts").hide();

								return result;
							}

							$(".grids").show();
							$(".areaBts").show();

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
		excecaoSegmentoParciaisController.tempArray=null ,
		excecaoSegmentoParciaisController.tempArrayCota=null,
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
							$(".areaBts").hide();

							return result;
						}

						if(result.rows.length==0){
							result.rows = 0;
							exibirMensagem("WARNING",
									["Produto já incluso ou não cadastrado para não receber este Segmento/Classificação ou Parcial"]);
							return result;
							
						}
						$.each(result.rows, function(index, row) {
							
							var checkBox = '<input type="checkbox" name="produtoNaoRecebido" disabled="disabled" value="' + row.cell.idProduto + '" checked />';
							
							row.cell.sel = checkBox;
						});
						
						$(".grids").show();
						$(".areaBts").show();
						

						return result;
					}
				},
				init : 
					$(".excessaoBGrid").flexigrid({
					colModel : [ {
						display : 'Código',
						name : 'codigoProduto',
						width : 60,
						sortable : false,
						align : 'left'
					},{
						display : 'Produto',
						name : 'nomeProduto',
						width : 90,
						sortable : false,
						align : 'left'
					},{
						display : 'Segmento',
						name : 'nomeSegmento',
						width : 115,
						sortable : false,
						align : 'left'
					},{
						display : 'Fornecedor',
						name : 'nomeFornecedor',
						width : 60,
						sortable : false,
						align : 'left'
					},  {
						display : 'Add',
						name : 'sel',
						width : 20,
						sortable : false,
						align : 'center'
					}],
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
							$(".areaBts").hide();

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
							
							var link = '<img style="cursor:pointer" title="Excluir Exceção" src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" isEdicao="true" onclick="excecaoSegmentoParciaisController.excluirExcecaoProduto('+row.cell.idExcecaoProdutoCota+');" />';
							
							row.cell.acao = link;
						});
						
						$(".grids").show();
						$(".areaBts").show();

						return result;
					}
				},
				init : 
					$(".excessaoCotaGrid").flexigrid({
						colModel : [{
							display : 'Ação',
							name : 'acao',
							width : 30,
							sortable : false,
							align : 'center'
						},
						{
							display : 'Código',
							name : 'codigoProduto',
							width : 60,
							sortable : true,
							align : 'left'
						},
						{
							display : 'Classificação',
							name : 'classificacaoProduto',
							width : 80,
							sortable : true,
							align : 'left'
						},
						
						{
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
							sortable : false,
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
							$(".areaBts").hide();

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
							
							var link = '<img style="cursor:pointer" title="Excluir Exceção" src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" onclick="excecaoSegmentoParciaisController.excluirExcecaoCota('+row.cell.idExcecaoProdutoCota+');" />';
							
							row.cell.acao = link;
						});
						
						$(".grids").show();
						$(".areaBts").show();

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
							sortable : false,
							align : 'center'
						},  {
							display : 'Ação',
							name : 'acao',
							width : 30,
							sortable : false,
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
							$(".areaBts").hide();

							return result;
						}
						
						var isExistCota = false;

						if(excecaoSegmentoParciaisController.tempArrayCota!=null){
							
							if(excecaoSegmentoParciaisController.tempArrayCota.length != 0){
								
								$.each(excecaoSegmentoParciaisController.tempArrayCota, function(index, row) {
									
									if(result.rows.length > 0){
										if(result.rows[0].cell.numeroCota == row.cell.numeroCota){
											isExistCota = true;
										}
									}
								});
								
								if(isExistCota == false){
									if(result.rows.length == 0){
										
										exibirMensagem("WARNING",
												["Cota já inclusa ou não cadastrada para não receber este Segmento/Classificação ou Parcial."]);
										
										result.total = excecaoSegmentoParciaisController.tempArrayCota.length;
									}
									
									$.each(excecaoSegmentoParciaisController.tempArrayCota, function(index, row) {
										result.rows.unshift(row);
									});
									excecaoSegmentoParciaisController.tempArrayCota = result.rows;
								}else{
									
									result.rows = excecaoSegmentoParciaisController.tempArrayCota;
								}
								
							}else{
								result.rows = 0;
								excecaoSegmentoParciaisController.tempArrayCota = null;
								return result;
							}
							
						}else{
							if(result.rows.length > 0){
								excecaoSegmentoParciaisController.tempArrayCota = result.rows;
							}
						}
						
						if(result.rows.length==0){
							result.rows = 0;
							exibirMensagem("WARNING",
									["Cota já inclusa ou não cadastrada para não receber este Segmento/Classificação ou Parcial."]);
							return result;
							
						}
						
						

						$.each(result.rows, function(index, row) {
							
							var checkBox = '<input type="checkbox" name="cotaNaoRecebeExcecao" value="' + row.cell.numeroCota + '" />';
							
							row.cell.sel = checkBox;
						});
						
						$(".grids").show();
						$(".areaBts").show();

						return result;
					}
				},
				init : 
					$(".excessaoGrid").flexigrid({
						colModel : [ {
							display : 'Cota',
							name : 'numeroCota',
							width : 50,
							sortable : false,
							align : 'left'
						},{
							display : 'Status',
							name : 'statusCota',
							width : 60,
							sortable : false,
							align : 'left'
						}, {
							display : 'Nome',
							name : 'nomePessoa',
							width : 130,
							sortable : false,
							align : 'left'
						},  {
							display : 'Add',
							name : 'sel',
							width : 20,
							sortable : false,
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
			
		});
		
	},
	
	excluirExcecaoProduto : function excluirExcecaoProduto(excecaoId){
		
		if(!verificarPermissaoAcesso(excecaoSegmentoParciaisController.workspace)){
			return;
		}
		
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
				        	 
					         $("#nomeCotaFiltroPrincipal", excecaoSegmentoParciaisController.workspace).focus();
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
		
		if(!verificarPermissaoAcesso(excecaoSegmentoParciaisController.workspace)){
			return;
		}
		
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
				        	 
					         $("#nomeProdutoPrincipal", excecaoSegmentoParciaisController.workspace).focus();
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
		
		if($(".excessaoBGrid tr").length == 0){
			exibirMensagem('WARNING', ["Não há produtos para inserção."]);
		}else{
			
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
						
						params.push({
							name : "listaIdProduto",
							value : $("#2005_codigoProduto").val()
						});
						
						params.push({
							name : "filtro.excecaoSegmento",
							value : $('#tipoExcecaoSegmento').is(':checked')
						});
						
						params.push({
							name : "filtro.produtoDto.idClassificacaoProduto",
							value : $('#comboClassificacaoProdNRec').val()
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
									
									$('.excessaoBGrid').find('[id^=row]').remove();
									
									grids.ProdutosRecebidosGrid.reload({
										params : params
									});
									
									$("#nomeCotaFiltroPrincipal", excecaoSegmentoParciaisController.workspace).focus();
									
									excecaoSegmentoParciaisController.tempArray = null;
									
									$("#2005_codigoProduto").val('');
									$("#2005_nomeProduto").val('');
									
								}
						);
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		}
		
	},
	
	inserirCotaNaExcecao : function inserirCotaNaExcecao(){
		
		if($(".excessaoGrid tr").length == 0){
			exibirMensagem('WARNING', ["Não há cotas para inserção."]);
		}else{
			
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
									
									$('.excessaoGrid').find('[id^=row]').remove();
									
									excecaoSegmentoParciaisController.porExcecao();
									
									excecaoSegmentoParciaisController.tempArrayCota = null;
									
									$("#cotasQueNaoRecebemNumeroCota").val('');
									$("#cotasQueNaoRecebemNomeCota").val('');					         
									
								}
						);
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		}
 	},
	
	porCota : function (){
		$('.porCota').show();
		$('.porExcessao').hide();
		
		if($(".pesquisaPorSegmento").is(":checked")){
			excecaoSegmentoParciaisController.alteraLegendResultadoSegmento();
		}else if($(".pesquisaPorParcial").is(":checked")){
			excecaoSegmentoParciaisController.alteraLegendResultadoParcial();
		}
	
		
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
		
		$('.porCota').hide();
		$('.porExcessao').show();
		
		
		if($(".pesquisaPorSegmento").is(":checked")){
			excecaoSegmentoParciaisController.alteraLegendResultadoSegmento();
		}else if($(".pesquisaPorParcial").is(":checked")){
			excecaoSegmentoParciaisController.alteraLegendResultadoParcial();
		}
		
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
		
		$("#nomeProdutoPrincipal", excecaoSegmentoParciaisController.workspace).focus();
		
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
				[{name : "2005_nomeProduto", value : produto.nome}, {name : "2005_codigoProduto", value : produto.codigo}],
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
		
			$("#2005_codigoProduto").val('');
			$("#2005_nomeProduto").val('');
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
			
			$("#cotasQueNaoRecebemNumeroCota,#cotasQueNaoRecebemNomeCota").val('');
		
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
				name : "filtro.produtoDto.idClassificacaoProduto", 
				value : produto.classifProduto || '' 
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
		$('.porExcessao').hide();
		
		$('#numeroCotaFiltroPrincipal').val('');
	    $('#nomeCotaFiltroPrincipal').val('');
	    $('.excessaoGrid').find('[id^=row]').remove();
	    $('.excessaoBGrid').find('[id^=row]').remove();
	    
	},
	filtroPorProduto: function filtroPorProduto(){
		$('#excecaoSegmentoParciais_filtroPorCota').hide();
		$('#excecaoSegmentoParciais_filtroPorProduto').show();
		$('.porCota').hide();
		
		$('#codigoProdutoPrincipal').val('');
	    $('#nomeProdutoPrincipal').val('');
	    $('#fornecedorPrincipal').val('');
	    $('#segmentoProdutoPrincipal').val('');
	    $("#2005_comboClassificacao").val('');
	    $('.excessaoGrid').find('[id^=row]').remove();
	    $('.excessaoBGrid').find('[id^=row]').remove();
	},
	
	limparCampos: function limparCampos() {
		$('#excecaoSegmentoParciais_filtroPorProduto').hide();
		$('.porCota').hide();
	    $('#numeroCotaFiltroPrincipal').val('');
	    $('#nomeCotaFiltroPrincipal').val('');
	    $('#codigoProdutoPrincipal').val('');
	    $('#nomeProdutoPrincipal').val('');
	    $('#fornecedorPrincipal').val('');
	    $('#segmentoProdutoPrincipal').val('');
	    $("#2005_comboClassificacao").val('');
	    $("#2005_codigoProduto").val('');
	    $("#2005_nomeProduto").val('');
	    $("#comboClassificacaoProdNRec").val('');
	    
	    $('#radio').check();
	    
	    excecaoSegmentoParciaisController.tempArrayCota = null;
		excecaoSegmentoParciaisController.tempArray = null;
	    
		excecaoSegmentoParciaisController.filtroPorCota();
	},
	alteraLegendResultadoSegmento:function(){
		$("#legendPorCotaRecebe").text("Produtos Recebidos");
		$("#legendPorCotaNaoRecebe").text("Produtos não Recebidos");
		
		$("#legendPorExcecaoRecebe").text("Cotas que recebem o segmento");
		$("#legendPorExcecaoNaoRecebe").text("Cotas que não recebem o segmento");
		
		
	},
	alteraLegendResultadoParcial:function(){
		
		$("#legendPorCotaRecebe").text("Produtos Parciais Recebidos");
		$("#legendPorCotaNaoRecebe").text("Produtos Parciais não Recebidos");
		
		$("#legendPorExcecaoRecebe").text("Cotas que recebem Publicação parcial");
		$("#legendPorExcecaoNaoRecebe").text("Cotas que não recebem Publicação parcial");
	},
	
	reload_ProdutosNaoRecebidosGrid:function(options){
		$(".excessaoBGrid").flexReload();
		excecaoSegmentoParciaisController.tempArray = new Array();
		
	},
	
	reload_CotasQueNaoRecebemExcecaoGrid:function(options){
		$(".excessaoGrid").flexReload();
		excecaoSegmentoParciaisController.tempArrayCota = new Array();
		
	}
	
}, BaseController);

//@ sourceURL=excecaoSegmentoParciaisController.js
