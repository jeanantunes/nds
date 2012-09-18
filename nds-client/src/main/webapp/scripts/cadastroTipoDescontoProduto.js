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

	novoDescontoProduto:function() {
		
		var data = descontoProdutoController.obterParametrosNovoDescontoProduto();

		$.postJSON(contextPath+"/financeiro/tipoDescontoCota/novoDescontoProduto",
				   data,
				   function(result) {

					   tipoDescontoController.pesquisarDescontoProduto();

					   $( "#dialog-produto",this.workspace ).dialog( "close" );

					   var tipoMensagem = result.tipoMensagem;
					   var listaMensagens = result.listaMensagens;
					   if (tipoMensagem && listaMensagens) {
					       exibirMensagem(tipoMensagem, listaMensagens);
				       }
	               },
				   null,
				   true);
	},

	obterParametrosNovoDescontoProduto: function() {
		
		var indProdutoEdicao = $("#mostrarEdicao", this.workspace).attr("checked") == 'checked';
		var codigoProduto = $("#pCodigoProduto",this.workspace).val();

		var edicaoProduto = null;
		var quantidadeEdicoes = null;
		
		if(indProdutoEdicao) {
			edicaoProduto 		= $("#edicaoProduto",this.workspace).val();
			quantidadeEdicoes 	= $("#quantidadeEdicoes",this.workspace).val();
		}
		
		var descontoProduto = $("#descontoProduto",this.workspace).val();
		var descontoPredominante = $("#descontoPredominante",this.workspace).attr("checked") ? true : false;
		var hasCotaEspecifica = document.getElementById("radioCotasEspecificas",this.workspace).checked;
		var isTodasCotas = document.getElementById("radioTodasCotas",this.workspace).checked;
		
		var data = new Array();
		
		data.push({name:'desconto.indProdutoEdicao' , value: indProdutoEdicao});
		data.push({name:'desconto.codigoProduto' , value: codigoProduto});
		data.push({name:'desconto.edicaoProduto' , value: edicaoProduto});
		data.push({name:'desconto.descontoProduto' , value: descontoProduto});
		data.push({name:'desconto.quantidadeEdicoes' , value: quantidadeEdicoes});
		data.push({name:'desconto.descontoPredominante' , value: descontoPredominante});
		data.push({name:'desconto.hasCotaEspecifica' , value: hasCotaEspecifica});
		data.push({name:'desconto.isTodasCotas' , value: isTodasCotas});
		
		$("input[id^=cotaInput]",this.workspace).each(function(index, value) {
			if ($(this).val()) {
				data.push({name:'cotas' , value: $(this).val()});
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
			'onblur="descontoProdutoController.pesquisaCota.pesquisarPorNumeroCota(\'#cotaInput1\', \'#nomeInput1\', true);"/>' +
			'</td>' +
			'<td>' +
			'<input type="text" name="nomeInput" id="nomeInput1" style="width:245px;" maxlength="255"' +
			'onkeyup="descontoProdutoController.pesquisaCota.autoCompletarPorNome(\'#nomeInput1\');" ' +
			'onblur="descontoProdutoController.pesquisaCota.pesquisarPorNomeCota(\'#cotaInput1\', \'#nomeInput1\',descontoProdutoController.adicionarLinhaCota(1));"/>' +
			'</td>' +
			'</tr>'
		);
	},

	adicionarLinhaCota:function(linhaAtual){
		
		if ($('#trCota' + (linhaAtual + 1),this.workspace).length == 0 && $('#cotaInput' + (linhaAtual),this.workspace).val() != ""){
			
			var tr = $('<tr class="trCotas" id="trCota'+ (linhaAtual + 1) +'" style="'+ ((linhaAtual + 1) % 2 == 0 ? "background: #F5F5F5;" : "") +'">' +
					'<td><input type="text" name="cotaInput" maxlength="255" id="cotaInput'+ (linhaAtual + 1) +'" onblur="descontoProdutoController.pesquisaCota.pesquisarPorNumeroCota(cotaInput'+ (linhaAtual + 1) +', nomeInput'+ (linhaAtual + 1) +', true);" style="width:120px;" /></td>' +
					'<td>'+
						 '<input type="text" name="nomeInput" maxlength="255" id="nomeInput'+ (linhaAtual + 1) +'" style="width:245px;" '+
							 ' onkeyup="descontoProdutoController.pesquisaCota.autoCompletarPorNome(nomeInput'+ (linhaAtual + 1) +');" ' +
							 ' onblur="descontoProdutoController.pesquisaCota.pesquisarPorNomeCota(cotaInput'+ (linhaAtual + 1) +', nomeInput'+ (linhaAtual + 1) +', descontoProdutoController.adicionarLinhaCota('+ (linhaAtual + 1) +'));" ' +
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
	
	init:function(pesquisaCota){
		
		descontoProdutoController.pesquisaCota = pesquisaCota;
		
		$("#descontoProduto",this.workspace).mask("99.99");
		
		$(".tiposDescProdutoGrid",this.workspace).flexigrid({
			preProcess: tipoDescontoController.executarPreProcessamento,
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigoProduto',
				width : 70,
				sortable : true,
				align : 'left'
			},{
				display : 'Produto',
				name : 'nomeProduto',
				width : 228,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Desconto %',
				name : 'desconto',
				width : 150,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Alteração',
				name : 'dataAlteracao',
				width : 120,
				sortable : true,
				align : 'center'			
			}, {
				display : 'Usuário',
				name : 'nomeUsuario',
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : false,
				align : 'center'
			}],
			sortname : "codigoProduto",
			sortorder : "asc",
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
	