var params = new Array();

var certificadoNFEController  = $.extend(true, {

	path : contextPath +"/nfe/certificadoNFE/",
	
	init : function() {
		
		this.initDatas();
		this.initFlexiGrids();
		this.bindButtons();
		
		$('#certificado-upload').fileupload(
			{
				url : this.path + "uploadCertificado",
				sequentialUploads: false,
				dataType : 'json',
				paramName : 'uploadedFile',
				replaceFileInput: false,
				submit : function(e, data) {
					data = $("#pesquisar_certicado_form", this.workspace).serialize();
	
				},
				success : function(e, data) {$("#nomeCertificado").html(e.result);
			}
					 
		});
		
	},

	initDatas : function() {
		$(".input-date").datepicker({
			showOn : "button",
			buttonImage : contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly : true
		});
		
		$(".input-date").mask("99/99/9999");
		
		$("#certificado-data-inicio").val(formatDateToString(new Date()));
		
		$("#certificado-data-fim").val(formatDateToString(new Date()));
	},
	
	initButtons : function() {
		
		var _this = this;

		$("#certificadoNFEConfirmar", this.workspace).click(function() {
			_this.btnConfirmar();
		});
	},
	
	upload : function() {
		
	},
	
	btnConfirmar : function() {
		
		this.isEmptyOrNull();
		
		var parametros = this.param();

		$.postJSON(this.path + 'confirmar', parametros, function(data) {
			
			var tipoMensagem = data.tipoMensagem;
			var listaMensagens = data.listaMensagens;

			if (tipoMensagem && listaMensagens) {
				exibirMensagemDialog(tipoMensagem, listaMensagens, "");
			}
			exibirMensagem("SUCCESS", ["Operação realizada com sucesso!"]);
			
		});		
		
	},
	
	limparTabela : function() {
		
	},

	isEmptyOrNull : function () {
		
		var mensagens = []; 
		
		if($("#certificado-senha").val() == '') {
			mensagens.push("A ['Senha'] não pode ser nula");
		} 
		
		if($("#certificado-alias").val() == '') {
			mensagens.push("O ['Alias do Certificado'] não pode ser nulo");
		}
		
		if($("#certificado-dataIncio").val() == '') {
			mensagens.push("A ['Data Inicio'] não pode ser nula");
		}
		
		if($("#certificado-dataFim").val() == '') {
			mensagens.push("A ['Data Fim'] não pode ser nula");
		}
		
		if(mensagens.length > 0) {
			exibirMensagem('WARNING', mensagens);
			return false;
		}
	},
	
	param : function () {
    	
    	params.push({name:"filtro.senha" , value: $("#certificado-senha").val()});
    	params.push({name:"filtro.alias" , value: $("#certificado-alias").val()});
    	params.push({name:"filtro.dataInicio" , value: $("#certificado-data-inicio").val()});
    	params.push({name:"filtro.dataFim" , value: $("#certificado-data-fim").val()});
    	
    	return params;
	},
	
	
	exibirMensagemSucesso : function (result){
		
		var tipoMensagem = result.tipoMensagem;
		var listaMensagens = result.listaMensagens;
		if (tipoMensagem && listaMensagens) {
			exibirMensagem(tipoMensagem, listaMensagens);
		}
	},
	
	initFlexiGrids : function() {
		$(".certificadoGrid", this.workspace).flexigrid({
			preProcess : function(data) {
				if(typeof data.mensagens == "object") {

					exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);

				} else {
					$.each(data.rows, function(index, value) {
						var idCerticado = value.cell.id;
						var acao = '<a href="javascript:;" onclick="certificadoNFEController.editar(' + idCerticado + ');"><img src="' + contextPath + '/images/ico_editar.gif" border="0" style="margin-right:10px;" />';
						acao += '</a> <a href="javascript:;" onclick="certificadoNFEController.excluir(' + idCerticado + ');""><img src="' + contextPath + '/images/ico_excluir.gif" border="0" /></a>';
						
					});
					return data;
				}

			},
			dataType : 'json',
			colModel : [{
				display : 'Nome Arquivo',
				name : 'nomeArquivo',
				width : 220,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data Inicio',
				name : 'dataInicio',
				width : 380,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data Fim',
				name : 'dataFim',
				width : 250,
				sortable : true,
				align : 'left'
			}, {
				display : 'A&ccedil;&atilde;o',
				name : 'acao',
				width : 60,
				sortable : false,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 'auto'
		});

	},
	
	buscar : function() {
		
		certificadoNFEController.initFlexiGrids();
		
		var parametros = new Array();
		
		parametros.push({name:"filtro.nomeArquivo" , value: $("#nomeCertificado").val()});
		
		$(".certificadoGrid", certificadoNFEController.workspace).flexOptions({
			preProcess: certificadoNFEController.executarPreProcessamento,
			url: contextPath + "/nfe/certificadoNFE/buscar",
			dataType : 'json',
			params: parametros
		});
		
		$(".certificadoGrid").flexReload();
		$(".grids", certificadoNFEController.workspace).show();
		
	},
	
	executarPreProcessamento : function(resultado) {

		$.each(resultado.rows, function(index, value) {
			var idCerticado = value.cell.id;
			var acao = '<a href="javascript:;" onclick="certificadoNFEController.editar(' + idCerticado + ');"><img src="' + contextPath + '/images/ico_editar.gif" border="0" style="margin-right:10px;" />';
			acao += '</a> <a href="javascript:;" onclick="certificadoNFEController.excluir(' + idCerticado + ');""><img src="' + contextPath + '/images/ico_excluir.gif" border="0" /></a>';
			
			value.cell.acao = acao;
		});

		return resultado;
	},
	
	
	bindButtons : function() {
		
		var _this = this;

		$("#certificadoNFEConfirmar", this.workspace).click(function() {
			_this.buscar();
		});
		
		$("#btnPesquisar", this.workspace).click(function() {
			_this.buscar();
			$(".grids").show();
			// $("#fileExport").show();
		});
		
		$("#btnNovo", this.workspace).click(function() {
			_this.novo();
		});
	},
	
	novo : function() {
		
		
	},
	
	
}, BaseController);
//@ sourceURL=certificadoNFE.js