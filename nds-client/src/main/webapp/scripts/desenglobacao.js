var desenglobacaoController = $.extend(true, {

	errorCallBack : function errorCallBack(){
		$('#statusCota').val('');
		$('#filtroPrincipalNumeroCota').val('');
		$('#filtroPrincipalNomePessoa').val('');
		
		$('#filtroDesenglobaNumeroCota').val('');
		$('#filtroDesenglobaNomePessoa').val('');
	},
	
	sucessCallBack : function errorCallBack(result){
		if (result) {
			$('#statusCota').val(result.status);
		}
	},
	
	init : function() {
		
		// ###### INICIO FILTRO DA TELA PRINCIPAL ######
		
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
		
		//###### FIM FILTRO DA TELA PRINCIPAL ######
		
		
		
		
		//###### INICIO FILTRO DA POPUP INSERT ######
		
		//FILTRO DA TELA DE DESENGLOBACAO
		// FILTRO POR COTA
		$('#filtroDesenglobaNumeroCota').change(function (){
			pesquisaCota.pesquisarPorNumeroCota('#filtroDesenglobaNumeroCota','#filtroDesenglobaNomePessoa', false, desenglobacaoController.sucessCallBack, desenglobacaoController.errorCallBack);
		});
		
		$('#filtroDesenglobaNomePessoa').keyup(function (){
			pesquisaCota.autoCompletarPorNome('#filtroDesenglobaNomePessoa');
		});
		
		//FILTRO POR NOME
		$('#filtroDesenglobaNomePessoa').blur(function (){
			pesquisaCota.pesquisarPorNomeCota('#filtroDesenglobaNumeroCota','#filtroDesenglobaNomePessoa', false, desenglobacaoController.sucessCallBack, desenglobacaoController.errorCallBack);
		});
		
		
		
		//FILTRO POPUP INSERIR COTA
		// FILTRO POR COTA
		$('#inserirEnglobadaNumeroCota').change(function (){
			pesquisaCota.pesquisarPorNumeroCota('#inserirEnglobadaNumeroCota','#inserirEnglobadaNomePessoa', false, desenglobacaoController.sucessCallBack, desenglobacaoController.errorCallBack);
		});
		
		$('#inserirEnglobadaNomePessoa').keyup(function (){
			pesquisaCota.autoCompletarPorNome('#inserirEnglobadaNomePessoa');
		});
		
		//FILTRO POR NOME
		$('#inserirEnglobadaNomePessoa').blur(function (){
			pesquisaCota.pesquisarPorNomeCota('#inserirEnglobadaNumeroCota','#inserirEnglobadaNomePessoa', false, desenglobacaoController.sucessCallBack, desenglobacaoController.errorCallBack);
		});
		
		//###### FIM FILTRO DA POPUP INSERT ######
		
		
		
		//###### AÇÕES DOS BOTÕES DA TELA ######
		
		//PESQUISAR DA TELA PRINCIPAL
		$('#pesquisaPorCota').click(function (){
			desenglobacaoController.porCota();
		});
		
		$('#btnInserirCotaEnglobada').click(function(e){
			e.preventDefault();
			
			var indexInput = 0;
			
			var html = '<tr>';
			html+= "<td><input type='text' name='desenglobaDTO["+indexInput+"].englobadaNumeroCota' value='"+ $('#inserirEnglobadaNumeroCota').val() +"' style='width: 30px;' class='filtroDefaultCota'></td>";
			html+= "<td style='width: 450px;'><input type='text' name='desenglobaDTO["+indexInput+"].englobadaNomePessoa' value='"+ $('#inserirEnglobadaNomePessoa').val() +"' style='width: 400px;' class='filtroDefaultPessoa'></td>";
			html+= "<td><input type='text' name='desenglobaDTO["+indexInput+"].englobadaPorcentagemCota' value='"+ $('#inserirEnglobadaPorcentagemCota').val() +"' style='width: 30px;'></td>";
			html+= '</tr>';
			
			if (indexInput != 0) {
				html+= "<input type='hidden' name='desenglobaDTO["+indexInput+"].desenglobaNumeroCota' value='"+$('#filtroDesenglobaNumeroCota').val()+"'>";
				html+= "<input type='hidden' name='desenglobaDTO["+indexInput+"].desenglobaNomePessoa' value='"+$('#filtroDesenglobaNomePessoa').val()+"'>";
			}
			
			$('#tableCotasEnglobadas').append(html);
			
			clearFormEnglobada();
			indexInput++;
		});
		
		function clearFormEnglobada() {
			$('#inserirEnglobadaNumeroCota').val('');
			$('#inserirEnglobadaNomePessoa').val('');
			$('#inserirEnglobadaPorcentagemCota').val('');
		}

	desenglobacaoController.Url = {},
	
	desenglobacaoController.Grids = {
		Util : {
			reload : function reload(options) {
	
				// Inicializa o objeto caso não
				// exista
				options = options || {};
	
				// obtendo a url padrão caso o
				// usuário não tenha informado
				options.url = this.Url.urlDefault || options.url;
	
				options.preProcess = options.preProcess || this.PreProcess._default || function(result) {
							if (result.mensagens) {
								exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
								$(".grids").hide();
								return result;
							}
	
							$(".grids").show();
							return result;
						};
	
				// GUARDA O ULTIMO PARÂMETRO
				// UTILIZADO
				this.lastParams = options.params;
	
				if (options !== undefined) {
					if (options.workspace === undefined) {
						$("." + this.gridName).flexOptions(options);
						$("." + this.gridName).flexReload();
					} else {
						$("." + this.gridName, options.workspace).flexOptions(options);
						$("." + this.gridName, options.workspace).flexReload();
					}
				}
			}
		},
		EnglobadosGrid : {
			reload : {},
			lastParams : []
		}
	};
	
	desenglobacaoController.Util = {
		getFiltroByForm : function(idForm) {
			var filtro;
	
			if (idForm === undefined) {
				return null;
			} else {
				filtro = $('#' + idForm).serializeArray();
	
				for ( var index in filtro) {
					if (filtro[index].value === "on") {
						filtro[index].value = true;
					} else if (filtro[index].value === "off") {
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
				_default : function(result) {
	
					if (result.mensagens) {
						exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
						$(".grids").hide();
						return result;
					}
	
					$.each(
						result.rows,
							function(index,
									row) {
								// onclick="desenglobacaoController.excluirExcecaoProduto('+row.cell.idExcecaoProdutoCota+');"
								var link = '<a href="javascript:;" style="cursor:pointer">'
										+ '<img title="Excluir Exceção" src="'
										+ contextPath
										+ '/images/ico_editar.gif" hspace="5" border="0px" />'
										+ '</a>';

								row.cell.acao = link;
							});
	
					$(".grids").show();
					return result;
				}
			},
			init : $(".englobadosGrid").flexigrid({
				colModel : [ {
					display : 'Cota',
					name : 'numeroCota',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : 'Nome',
					name : 'nomeCota',
					width : 220,
					sortable : true,
					align : 'left'
				}, {
					display : 'Tipo de PDV',
					name : 'nomePDV',
					width : 220,
					sortable : true,
					align : 'left'
				}, {
					display : '% da Cota',
					name : 'porcentagemCota',
					width : 63,
					sortable : true,
					align : 'left'
				}, {
					display : 'Usuário',
					name : 'nomeUsuario',
					width : 160,
					sortable : true,
					align : 'left'
				}, {
					display : 'Data Alteração',
					name : 'dataAlteracao',
					width : 100,
					sortable : true,
					align : 'center'
				}, {
					display : 'Hora',
					name : 'hora',
					width : 50,
					sortable : true,
					align : 'center'
				}],
				sortname : "numeroCota",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 255
			})
		}
		};
	},
	
	porCota : function() {
		var filtroPrincipalCota = [], util = desenglobacaoController.Util, grids = desenglobacaoController.Grids;
	
		filtroPrincipalCota = util.getFiltroByForm("filtroPrincipal");
	
		grids.EnglobadosGrid.reload({
			dataType : 'json',
			params : filtroPrincipalCota,
		});
	},
	
	novaEnglobacao : function(listaEnglobadas){
		var formData = $("#formInserirEnglobada").serialize();		
        $.post(contextPath + "/distribuicao/desenglobacao/inserirEnglobacao", formData, function(result) {
			
        	if (result.mensagens) {
				exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
				return result;
			}
        	
        	var filtroPrincipalCota = [], util = desenglobacaoController.Util, grids = desenglobacaoController.Grids;
        	
    		filtroPrincipalCota = util.getFiltroByForm("filtroPrincipal");
    		
    		grids.EnglobadosGrid.reload({
    			dataType : 'json',
    			params : filtroPrincipalCota,
    		});
        });
    },
    
    popup: function popup() {
    	
    	$('#tableCotasEnglobadas').empty();
    	$('#filtroDesenglobaNumeroCota').val('');
    	$('#filtroDesenglobaNomePessoa').val('');
    	
		$("#dialog-novo").dialog({
			resizable: false,
			height:500,
			width:650,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$("#effect").show("highlight", {}, 1000, callback);
					$(".grids").show();
					desenglobacaoController.novaEnglobacao();
					$(this).dialog("destroy");
				},
				"Cancelar": function() {
					$(this).dialog("destroy");
				}
			}
		});
	},
	
}, BaseController);
//@ sourceURL=desenglobacaoController.js