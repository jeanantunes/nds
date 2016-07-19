function GeracaoArquivos() {
	this.init();
}

GeracaoArquivos.prototype.path = contextPath + '/administracao/geracaoArquivos/';
GeracaoArquivos.prototype.init = function() {
	var _this = this;

	$("#datepickerDe")
			.datepicker(
					{
						showOn : "button",
						buttonImage : contextPath
								+ "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
						buttonImageOnly : true
					});


	$("#btnGerar", this.workspace).click(function() {
		_this.btnGerarOnClick();
	});
	
	$("#btnOpenCRUD", this.workspace).click(function() {
		_this.btnOpenOnClick();
	});
	
	$("#btnUnificar", this.workspace).click(function() {
		_this.btnUnificarOnClick();
	});
	
	
	
	$("#btnUnificarCotasCRUD", this.workspace).click(function() {
		_this.btnUnificarCotasCRUDOnClick();
	});
	
	

	$("#tipoArquivo", this.workspace).change(function() {
		_this.tipoArquivoGerarOnChange();
	});

	_this.tipoArquivoGerarOnChange();
	
	
	
	this.criarCotasCentralizadasGrid();
};


GeracaoArquivos.prototype.criarCotasCentralizadasGrid = function() {
	$(".cotasCentralizadas", this.workspace).flexigrid({
		preProcess: this.getDataCotasCentralizadas,
		dataType : 'json',
		colModel :[{
			display : '',
			name : 'id',
			width : 1,
			sortable : false,
			align : 'center',
			hide:true,
			style:'display{none}',
		}, {
			display : 'Cota Master',
			name : 'numeroCotaMaster',
			width : 100,
			sortable : false,
			align : 'center'
		}, {
			display : 'Cotas',
			name : 'numeroCota',
			width : 100,
			sortable : false,
			align : 'center'
		}, {
			display : 'Situação',
			name : 'situacao',
			width : 50,
			sortable : false,
			align : 'center'
		},
		{
			display : 'Ação',
			name : 'acao',
			width : 120,
			sortable : false,
			align : 'center'
		}
		],
		width : 420,
		height : 255
	});
},

GeracaoArquivos.prototype.getParams = function() {
	var params = {
		"dataLctoPrevisto" : $("#datepickerDe", this.workspace).val(),
		"operacao" : $("#tipoArquivo", this.workspace).val(),
		"nomeArquivo" : $("#nomeArquivo", this.workspace).val()
	};
	return params;
};


GeracaoArquivos.prototype.btnOpenOnClick = function() {
	window.open(this.path + "getFile", "_blank");
}

GeracaoArquivos.prototype.btnGerarOnClick = function() {

	var params = this.getParams();
	
if($("#tipoArquivo", this.workspace).val() == "VENDA") {
		
		$.postJSON(this.path + 'gerarVenda',
				params, 
				function(data) {
					$("#resultado_unificacao", this.workspace).show();
					$("#qtdArquivosUnificados", this.workspace).html(data);
					
					window.open(contextPath+ "/administracao/geracaoArquivos/getFile", "_blank");
				},
				function(result) {
					$("#resultado_unificacao", this.workspace).show();
					$("#qtdArquivosUnificados", this.workspace).html(data);
					
				}
		);
		
	} else
	
	if($("#tipoArquivo", this.workspace).val() == "PICKING") {
		
		$.postJSON(this.path + 'gerar',
				params, 
				function(data) {
					$("#resultado", this.workspace).show();
					$("#qtdArquivosGerados", this.workspace).html(data.int);
				},
				function(result) {
					$("#resultado", this.workspace).show();
					if(result.mensagens.listaMensagens[0].match("endereço LED")){
						$("#qtdArquivosGerados", this.workspace).html(1);
					}else{
						$("#qtdArquivosGerados", this.workspace).html(0);
					}
				}
		);
	} else 
		if($("#tipoArquivo", this.workspace).val() == "UNIFICAR") {
			
			$.postJSON(this.path + 'unificar',
					params, 
					function(data) {
				      
						$("#resultado_unificacao", this.workspace).show();
						$("#qtdArquivosUnificados", this.workspace).html(data);
						
						window.open(contextPath+ "/administracao/geracaoArquivos/getFile", "_blank");
					},
					function(result) {
						
						$("#resultado_unificacao", this.workspace).show();
						$("#qtdArquivosUnificados", this.workspace).html(0);
						
					}
					
			);
		}
		else
	{
		
		$.fileDownload(this.path + 'gerar', {
			httpMethod : "POST",
			data : params,
			successCallback: function (data) {
				$("#resultado", this.workspace).show();
				$("#qtdArquivosGerados", this.workspace).html(data.int);
            },
			failCallback : function(arg) {
				result = $.parseJSON($(arg).text());

				if((typeof result != "undefined") && result.mensagens) {
					result = result.mensagens;
					var tipoMensagem = result.tipoMensagem;
					var listaMensagens = result.listaMensagens;
					
					if (tipoMensagem && listaMensagens) {
						exibirMensagem("WARNING", ["Erro ao gerar Arquivo! "+listaMensagens]);
						}
				}
				else
					exibirMensagem("WARNING", ["Erro ao gerar Arquivo!"+arg]);
					
			}
		});
	}
	
};


GeracaoArquivos.prototype.btnUnificarOnClick = function() {

	
	
	var params = this.getParams();
		
		$.postJSON(this.path + 'unificaror',
				params, 
				function(data) {
					$("#resultado_unificacao", this.workspace).show();
					$("#qtdArquivosUnificados", this.workspace).html(data);
				},
				function(result) {
					$("#resultado_unificacao", this.workspace).show();
					$("#qtdArquivosUnificados", this.workspace).html(0);
				}
		);
	
	
};




GeracaoArquivos.prototype.btnUnificarCotasCRUDOnClick = function() {
 
	
	$(".cotasCentralizadas", this.workspace).flexOptions({
		url: contextPath+'/cadastro/cotaUnificacao/consultarCotasUnificadasMaster',
		newp : 1
	});
	
	$(".cotasCentralizadas", this.workspace).flexReload();
	
	
	
};
GeracaoArquivos.prototype.tipoArquivoGerarOnChange = function() {

	$("#resultado", this.workspace).hide();
	
	var reparte = $("#dtLancto");
	var encalhe = $("#dtRecolhimento");
	var venda = $("#dtVenda");
	var arqDinap = $("#arqDinap");
	var tipoArquivo = $("#tipoArquivo").val();
	var arquivo = $("#arquivo");
	var dtEscolha = $("#dtEscolha");

    switch (tipoArquivo) {  
            case 'REPARTE':    
        		reparte.show();
                encalhe.hide();
                venda.hide();
                arqDinap.hide();
                this.alterarDataCalendario(tipoArquivo);
                arquivo.hide();
                dtEscolha.show();
            break;  
            case 'ENCALHE':
            	this.alterarDataCalendario(tipoArquivo);
            	encalhe.show();
                reparte.hide();
                venda.hide();
                arqDinap.hide();
                arquivo.hide();
                dtEscolha.show();
            break;
            case 'PICKING':
        		reparte.show();
                encalhe.hide();
                venda.hide();
                arqDinap.hide();
                arquivo.hide();
                dtEscolha.show();
            break; 
            case 'VENDA':
            	reparte.hide();
                encalhe.hide();
                venda.show();
                arqDinap.hide();
                arquivo.hide();
                dtEscolha.show();
            	break;
            case 'UNIFICAR':
        		venda.hide();
                encalhe.hide();
                reparte.hide();
                arqDinap.show();
                this.alterarDataCalendario('PICKING');
                arquivo.show();
                dtEscolha.hide();
              
            break;  
            default:
                reparte.hide();
            	encalhe.show();
            	venda.hide();
            	 arqDinap.hide();
            	 arquivo.hide();
            break;  
    }

};

GeracaoArquivos.prototype.alterarDataCalendario = function(tipoArquivoGeracao){
	$.postJSON(this.path + 'alterarDataCalendario',
			   {tipoArquivo:tipoArquivoGeracao}, 
			   function(result){
				   $("#datepickerDe", this.workspace).val(result.data);
			   });
};

GeracaoArquivos.prototype.mostrarUnificacaoCotas = function(){
	
	$(".cotasCentralizadas", this.workspace).flexOptions({
		url: contextPath+'/cadastro/cotaUnificacao/consultarCotasUnificadasMaster',
		newp : 1
	});
	
	$(".cotasCentralizadas", this.workspace).flexReload();
};

GeracaoArquivos.prototype.getDataCotasCentralizadas = function(data){
	
	$.each(data.rows, function(index, row){
		
		row.cell.numeroCotaMaster = row.cell.numeroCotaMaster;
		
		
		
		row.cell.numeroCota = row.cell.numeroCota;
		row.cell.id = row.cell.id;
		
		var linkAEditar = '<a isEdicao="true" href="javascript:;" onclick="GeracaoArquivos.prototype.editarUnificacao(' + 
		row.cell.id+ ');"' +
		'style="cursor:pointer; margin-right:10px;">' +
		'<img title="Editar" src="' + contextPath + '/images/ico_editar.gif" border="0px" />' +
		'</a>';

		var linkExcluir = '<a isEdicao="true" href="javascript:;" onclick="GeracaoArquivos.prototype.excluirUnificacao(' + 
		row.cell.id + ');" style="cursor:pointer">' +
		'<img title="Excluir" src="' + contextPath + '/images/ico_excluir.gif" border="0px" />' +
		'</a>';
		
		row.cell.acao =  linkExcluir;
	});
	var permissaoAlteracao = ($('#permissaoAlteracao',workspace).val()=="true");
	
	$("#dialog_cota_unificacao", this.workspace).dialog({
		resizable: false,
		height:390,
		width:450,
		modal: true,
		buttons:[{
			id:"bt_inc_nova",
	        text:"Incluir Nova", 
	        click: function() {
	        	if(!permissaoAlteracao){
					exibirAcessoNegado();
					return;
				}
	        	$("#parametro-cobranca-numeroCota", this.workspace).val("");
	        	$("#parametro-cobranca-nomeCota", this.workspace).val("");
	        	GeracaoArquivos.prototype.exibirModalNovaUnificacao();
	        	GeracaoArquivos.prototype.adicionarLinhaCota();
	        }
		}],
		form: $("#dialog_cota_unificacao", this.workspace).parent("form")
	});
	
	return data;
};

GeracaoArquivos.prototype.exibirModalNovaUnificacao = function(){
	
	$("#parametro-cobranca-numeroCota", this.workspace).removeAttr("readonly");
	$("#parametro-cobranca-nomeCota", this.workspace).removeAttr("readonly");
	this.limparCamposCentralizacaoCotas();
	
	$("#dialog_nova_cota_unificacao", this.workspace).dialog({
		resizable: false,
		height:390,
		width:600,
		modal: true,
		buttons:[
		{
			id:"bt_confirmar",
			text:"Confirmar",
			click: function(){
				
				var data = [
				            {name:'numeroCotaCentralizadora', value:$("#parametro-cobranca-numeroCota", this.workspace).val()}
				            ];
				
				$.each($("#cotasCentralizadas [class=numCota]", this.workspace),
					function(index, item){
						
						if (item.value && item.value != ""){
							
							data.push({name:'numeroCotasCentralizadas['+index+']', value:item.value});
						}
					}
				);
				
				$.postJSON(
					contextPath+"/cadastro/cotaUnificacao/cadastrarCotaUnificacaoMaster",
					data,
					function(result) {
						if (result){
							
							if (result.tipoMensagem){
								
								exibirMensagem(result.tipoMensagem, result.listaMensagens);
							}
							
							GeracaoArquivos.prototype.limparCamposCentralizacaoCotas();
				        	$("#dialog_nova_cota_unificacao", this.workspace).dialog("close");
				        	
				        	GeracaoArquivos.prototype.mostrarUnificacaoCotas();
						}
					},
					null,
					true
				);
			}
		}, {
			id:"bt_cancelar",
	        text:"Cancelar", 
	        click: function() {
	        	
	        	GeracaoArquivos.prototype.limparCamposCentralizacaoCotas();
	        	$("#dialog_nova_cota_unificacao", this.workspace).dialog("close");
	        }
		}],
		form: $("#dialog_nova_cota_unificacao", this.workspace).parent("form")
	});
};

GeracaoArquivos.prototype.buscarCotaPorNumero = function(index){
	
	var numeroCota = $("#parametro-cobranca-numeroCota" + index, this.workspace).val();

	
	var data = [
        {name:'numeroCota', value: numeroCota},
        
	];
	
	$.postJSON(contextPath+"/cadastro/cotaUnificacao/buscarCota",
	   data,
	   function(result) {
    	   if (result.tipoMensagem){
			   
    		   exibirMensagem(result.tipoMensagem, result.listaMensagens);
    		   $("#parametro-cobranca-numeroCota" + index, this.workspace).val("");
    		   $("#parametro-cobranca-nomeCota" + index, this.workspace).val("");
    		   
		       return;
    	   } else {
    		 
    		   $("#parametro-cobranca-numeroCota" + index, this.workspace).val(result.numero);
    		   $("#parametro-cobranca-nomeCota" + index, this.workspace).val(result.nome);
    	   }
       },
       function(){
    	   
    	   $("#parametro-cobranca-numeroCota" + index, this.workspace).val("");
    	   $("#parametro-cobranca-nomeCota" + index, this.workspace).val("");
       },
	   true
	);
};


GeracaoArquivos.prototype.buscarCotaPorNumero_ = function(index){
	
	var numeroCota = $("#numeroCota_" + index, this.workspace).val();
  
	
	var data = [
        {name:'numeroCota', value: numeroCota},
        
	];
	
	$.postJSON(contextPath+"/cadastro/cotaUnificacao/buscarCota",
	   data,
	   function(result) {
    	   if (result.tipoMensagem){
			   
    		   exibirMensagem(result.tipoMensagem, result.listaMensagens);
    		   $("#numeroCota_" + index, this.workspace).val("");
    		   $("#nomeCota_" + index, this.workspace).val("");
    		   
		       return;
    	   } else {
    		 
    		   $("#numeroCota_" + index, this.workspace).val(result.numero);
    		   $("#nomeCota_" + index, this.workspace).val(result.nome);
    	   }
       },
       function(){
    	   
    	   $("#numeroCota_" + index, this.workspace).val("");
    	   $("#nomeCota_" + index, this.workspace).val("");
       },
	   true
	);
};

GeracaoArquivos.prototype.adicionarLinhaCota = function(indexAnterior){
	
	if ($("#parametro-cobranca-numeroCota"+indexAnterior, this.workspace).val() == "" ||
			$("#parametro-cobranca-nomeCota"+indexAnterior, this.workspace).val() == ""){
		
		return;
	}
	
	if (indexAnterior === ""){
		indexAnterior = -1;
	}
	
	if (!indexAnterior && indexAnterior != 0){
		indexAnterior = 0;
	} else {
		indexAnterior += 1;
	}
	
	if ($("#parametro-cobranca-numeroCota"+indexAnterior, this.workspace)[0]){
		return;
	}
	
	var template = '<tr class="addCota"><td style="width: 10%;">'+
		'<input type="text" class="numCota" id="numeroCota_'+ indexAnterior +'" style="width: 40px;"'+
		'onchange="GeracaoArquivos.prototype.buscarCotaPorNumero_('+ indexAnterior +')"/>'+
		'</td><td>'+
		'<input type="text" id="nomeCota_'+ indexAnterior +'" style="width: 475px;"'+
		'onkeyup="GeracaoArquivos.prototype.onkeyupCampoNome('+ indexAnterior +')"' +
		'onblur="GeracaoArquivos.prototype.onblurCampoNome('+ indexAnterior +')"'+
		'/></td></tr>';
	
	$("#cotasCentralizadas", this.workspace).append(template);
	
	//$(".numCota", this.workspace).numeric();
	
	$("#parametro-cobranca-numeroCota"+ indexAnterior, this.workspace).focus();
};

GeracaoArquivos.prototype.onkeyupCampoNome = function(index){
	
	pesquisaCota.autoCompletarPorNome("#parametro-cobranca-nomeCota" + index);
};

GeracaoArquivos.prototype.onblurCampoNome = function(index){
	
//	this.adicionarLinhaCota(index);
	
//	if ($("#parametro-cobranca-numeroCota" + index, this.workspace).val() == ""){
//		pesquisaCota.pesquisarPorNomeCota("#parametro-cobranca-numeroCota" + index, "#parametro-cobranca-nomeCota" + index);
//	}
};

GeracaoArquivos.prototype.limparCamposCentralizacaoCotas = function(){
	
	$("#parametro-cobranca-numeroCota", this.workspace).val("");
	$("#parametro-cobranca-nomeCota", this.workspace).val("");
	
	$("#cotasCentralizadas tr:[class=addCota]", this.workspace).remove();
};

GeracaoArquivos.prototype.excluirUnificacao = function(id){
	$.postJSON(
		contextPath+"/cadastro/cotaUnificacao/excluirCotaUnificacaoMaster",
											
		[{name:'id', value:id}],
		function(result){
			GeracaoArquivos.prototype.mostrarUnificacaoCotas();
		},
		null,
		true
	);
};

GeracaoArquivos.prototype.editarUnificacao = function(id){
	
	$.postJSON(
		contextPath+"/cadastro/cotaUnificacao/editarCotaUnificacao",
		[{name:'id', value:id}],
		function(result){
			
			this.exibirModalNovaUnificacao();
			
			$.each(result, function(index, row){
				
				if (!index){
					
					$("#parametro-cobranca-numeroCota", this.workspace).val(row.numero);
					$("#parametro-cobranca-nomeCota", this.workspace).val(row.nome);
					
					$("#parametro-cobranca-numeroCota", this.workspace).attr("readonly", "readonly");
					$("#parametro-cobranca-nomeCota", this.workspace).attr("readonly", "readonly");
				} else {
					
					this.adicionarLinhaCota(index-1);
					$("#parametro-cobranca-numeroCota" + index, this.workspace).val(row.numero);
					$("#parametro-cobranca-nomeCota" + index, this.workspace).val(row.nome);
				}
			});
			
			
		},
		null,
		true
	);

};
//@ sourceURL=geracaoArquivo.js