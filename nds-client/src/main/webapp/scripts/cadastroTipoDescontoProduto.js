var descontoProdutoController = $.extend(true,{
	
	pesquisaCota:null,
	
	inicializarModal: function() {
		
		$("#formTipoDescontoProduto",this.workspace)[0].reset();
		
		descontoProdutoController.esconderGridCota();
		
		descontoProdutoController.mostraEdicao();
	},

	popup_produto:function() {
		
		descontoProdutoController.inicializarModal();

		$( "#dialog-produto",this.workspace ).dialog({
			resizable: false,
			height:550,
			width:450,
			modal: true,
			buttons:[{
						id:"id_confirmar_produto",text:"Confirmar",
						click: function() {
							descontoProdutoController.novoDescontoProduto();
						}
					},{
						id:"id_close_produto",text:"Cancelar",
						click: function() {
							$( this ).dialog( "close" );
						}
					}
				],
				form: $("#dialog-produto", this.workspace).parents("form")
		});	
	},

	novoDescontoProduto: function() {
		
		var data = descontoProdutoController.obterParametrosNovoDescontoProduto();

		$.postJSON(contextPath+"/financeiro/tipoDescontoCota/novoDescontoProduto",
				   data,
				   function(result) {

					   $( "#dialog-produto",this.workspace ).dialog( "close" );

					   var tipoMensagem = result.tipoMensagem;
					   var listaMensagens = result.listaMensagens;
					   if (tipoMensagem && listaMensagens) {
					       exibirMensagem(tipoMensagem, listaMensagens);
				       }
					   
					   $("#pCodigoProduto", this.workspace).val('');
					   $("#pNomeProduto", this.workspace).val('');
					   $("#descontoProduto", this.workspace).val('');
					   $("#edicaoProduto", this.workspace).val('');
					   $("#quantidadeEdicoes", this.workspace).val('');
					   
					   $("#mostrarEdicao", this.workspace).prop('checked', false);
					   $("#radioCotasEspecificas", this.workspace).prop('checked', false);
					   $("#radioTodasCotas", this.workspace).prop('checked', false);
					   
					   tipoDescontoController.pesquisarDescontoProduto();
	               },
				   null,
				   true);

	},

	obterParametrosNovoDescontoProduto: function() {
		
		var data = new Array();
		
		var indProdutoEdicao = $("#mostrarEdicao", this.workspace).attr("checked") == 'checked';
		var codigoProduto = $("#pCodigoProduto",this.workspace).val();
		var descontoProduto = $("#descontoProduto",this.workspace).justPercent("stringValue");
		var descontoPredominante = $("#descontoPredominante",this.workspace).attr("checked") ? true : false;
		var hasCotaEspecifica = document.getElementById("radioCotasEspecificas",this.workspace).checked;
		var isTodasCotas = document.getElementById("radioTodasCotas",this.workspace).checked;
		
		data.push({name:'descontoDTO.indProdutoEdicao' , value: indProdutoEdicao});
		data.push({name:'descontoDTO.codigoProduto' , value: codigoProduto});
		data.push({name:'descontoDTO.descontoProduto' , value: descontoProduto});
		data.push({name:'descontoDTO.descontoPredominante' , value: descontoPredominante});
		data.push({name:'descontoDTO.hasCotaEspecifica' , value: hasCotaEspecifica});
		data.push({name:'descontoDTO.isTodasCotas' , value: isTodasCotas});
		
		if(indProdutoEdicao) {
			var edicaoProduto 		= $("#edicaoProduto",this.workspace).val();
			var quantidadeEdicoes 	= $("#quantidadeEdicoes",this.workspace).val();
			
			data.push({name:'descontoDTO.edicaoProduto' , value: edicaoProduto});
			data.push({name:'descontoDTO.quantidadeEdicoes' , value: quantidadeEdicoes});
		}
			
		$("input[id^=cotaInput]",this.workspace).each(function(index, value) {
			if ($(this).val()) {
				data.push({name:'descontoDTO.cotas' , value: $(this).val()});
			}
		});
		
		return data;
	},
	
	mostrarGridCota:function(){
		$('.especificaCota',this.workspace).show();
	},

	esconderGridCota:function(){
		
		$('.especificaCota',this.workspace).hide();
		
		descontoProdutoController.resetGridCota();
	},

	mostraEdicao:function() {

		$("#mostrarEdicao",this.workspace).attr("checked") ? $('.aEdicao',this.workspace).show() : $('.aEdicao',this.workspace).hide();
	},

	resetGridCota:function() {
		
		$("tr[id^='trCota']",this.workspace).remove();
		
		$("#gridCotas",this.workspace).append(
			'<tr id="trCota1">' +
			'<td>' +
			'<input type="text" name="cotaInput" id="cotaInput1" style="width:120px;" maxlength="255" ' +
			'onblur="descontoProdutoController.pesquisaCota.pesquisarPorNumeroCota(\'#cotaInput1\', \'#nomeInput1\', true,function(){descontoProdutoController.adicionarLinhaCota(1)});"/>' +
			'</td>' +
			'<td>' +
			'<input type="text" name="nomeInput" id="nomeInput1" style="width:245px;" maxlength="255"' +
			'onkeyup="descontoProdutoController.pesquisaCota.autoCompletarPorNome(\'#nomeInput1\');" ' +
			'onblur="descontoProdutoController.pesquisaCota.pesquisarPorNomeCota(\'#cotaInput1\', \'#nomeInput1\',true, function(){descontoProdutoController.adicionarLinhaCota(1);});"/>' +
			'</td>' +
			'</tr>'
		);
	},

	adicionarLinhaCota:function(linhaAtual){
		
		if ($('#trCota' + (linhaAtual + 1),this.workspace).length == 0 && $('#cotaInput' + (linhaAtual),this.workspace).val() != ""){
			
			var tr = $('<tr class="trCotas" id="trCota'+ (linhaAtual + 1) +'" style="'+ ((linhaAtual + 1) % 2 == 0 ? "background: #F5F5F5;" : "") +'">' +
					'<td><input type="text" name="cotaInput" maxlength="255" id="cotaInput'+ (linhaAtual + 1) +'" onblur="descontoProdutoController.pesquisaCota.pesquisarPorNumeroCota(cotaInput'+ (linhaAtual + 1) +', nomeInput'+ (linhaAtual + 1) +', true,function(){descontoProdutoController.adicionarLinhaCota('+ (linhaAtual + 1) +')});" style="width:120px;" /></td>' +
					'<td>'+
						 '<input type="text" name="nomeInput" maxlength="255" id="nomeInput'+ (linhaAtual + 1) +'" style="width:245px;" '+
							 ' onkeyup="descontoProdutoController.pesquisaCota.autoCompletarPorNome(nomeInput'+ (linhaAtual + 1) +');" ' +
							 ' onblur="descontoProdutoController.pesquisaCota.pesquisarPorNomeCota(cotaInput'+ (linhaAtual + 1) +', nomeInput'+ (linhaAtual + 1) +', true, function(){descontoProdutoController.adicionarLinhaCota('+ (linhaAtual + 1) +');});" ' +
						 '/>'+
					'</td>' +
					'</tr>'
			);
			
			$("#gridCotas",this.workspace).append(tr);
			
			$("#cotaInput" + (linhaAtual + 1),this.workspace).focus();
			
			$("#cotaInput"+ (linhaAtual + 1),this.workspace).numeric();
		}
	},
	
	exibirDialogCotasProdutoEdicao: function(idTipoDesconto) {
		
		$(".lstCotaGrid",this.workspace).flexOptions({
			url: contextPath +"/financeiro/tipoDescontoCota/exibirCotasTipoDescontoProduto",
			params: [{name: 'idTipoDescontoProduto', value: idTipoDesconto}]
		});

		$(".lstCotaGrid",this.workspace).flexReload();

		$( "#dialog-cotas",this.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons:[ {id:"btn_close_cotas",
				   text:"Fechar",
				   click: function() {
						$( this ).dialog( "close" );
					},
				}],
			form: $("#dialog-cotas", this.workspace).parents("form")
		});	
	},
	
	init: function(pesquisaCota){
		
		$("#quantidadeEdicoes",this.workspace).numeric();

		$("#edicaoProduto",this.workspace).numeric();
		
		descontoProdutoController.pesquisaCota = pesquisaCota;
		
		$("#descontoProduto",this.workspace).justPercent();
		
		$(".tiposDescProdutoGrid",this.workspace).flexigrid({
			preProcess: tipoDescontoController.executarPreProcessamento,
			onSuccess: function(){bloquearItensEdicao(tipoDescontoController.workspace);},
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigoProduto',
				width : 70,
				sortable : false,
				align : 'left'
			},{
				display : 'Produto',
				name : 'nomeProduto',
				width : 228,
				sortable : false,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 90,
				sortable : false,
				align : 'center'
			}, {
				display : 'Desconto %',
				name : 'desconto',
				width : 90,
				sortable : false,
				align : 'center'
			}, {
				display : 'Predominante',
				name : 'predominante',
				width : 110,
				sortable : false,
				align : 'center'
			}, {
				display : 'Data Alteração',
				name : 'dataAlteracao',
				width : 100,
				sortable : true,
				align : 'center'			
			}, {
				display : 'Usuário',
				name : 'nomeUsuario',
				width : 130,
				sortable : false,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : false,
				align : 'center'
			}],
			sortname : "dataAlteracao",
			sortorder : "desc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
		
		$(".lstCotaGrid",this.workspace).flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			},{
				display : 'Nome',
				name : 'nome',
				width : 245,
				sortable : false,
				align : 'left'
			}],
			width : 350,
			height : 155,
			sortorder : "asc",
		});
	},
	
}, BaseController);
//@ sourceURL=cadastroTipoDescontoProduto.js