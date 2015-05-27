var EmissaoCEController = $.extend(true, {
	
	emissoes : [],
	fornecedoresSelecionados : [],
	
	init : function() {
		
		var _this = this;
		
		$("#emissaoCE-dataDe", this.workspace).mask("99/99/9999");
		
		$( "#emissaoCE-dataDe" , this.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#emissaoCE-dataAte", this.workspace).mask("99/99/9999");
		
		$( "#emissaoCE-dataAte" , this.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});		
		
		$("#emissaoCE-cotaDe", this.workspace).numeric();
		
		$("#emissaoCE-cotaAte", this.workspace).numeric();
		
		
		$("#imprimirCE").click(function(){
			
			_this.imprimirCE();
		});
		
        $("#imprimirBoletosEmBranco").click(function(){
			
			_this.obterDadosImpressaoBoletosEmBranco(true);
		});
		
		this.inicializarGrids();
	},

	popupConfirmacaoReemissaoBoletosAntecipados : function(){

		$( "#dialog-reemissao-boleto-antecipado", this.workspace).dialog({
			resizable: false,
			height:120,
			width:720,
			modal: true,
			buttons: {
				"Sim": function() {

					EmissaoCEController.obterDadosImpressaoBoletosEmBranco(false);
					
					$( this ).dialog( "close" );
				},
				"Não": function() {
					
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-reemissao-boleto-antecipado", this.workspace).parents("form")
		});	
	},

	obterDadosImpressaoBoletosEmBranco : function(verificarReemissao){
		
		var _this = this;
		
        var data = [{name:'verificarReemissao', value: verificarReemissao}];
		
		$.postJSON(
				contextPath + "/emissaoCE/obterDadosImpressaoBoletosEmBranco", data,
				function(result) { 

                    if (result && result.existemBoletosEmBranco == true){
					    
                    	if(result.msgCotaSemOperacaoDiferenciada) {
                    		
                    		exibirMensagem("WARNING", [result.msgCotaSemOperacaoDiferenciada]);
                    	}
                    	
						_this.imprimirBoletoEmBranco();
						
					} else {
                    	
                    	if(result.existemBoletosEmBranco == false){
                    	
                    	    _this.popupConfirmacaoReemissaoBoletosAntecipados();
                    	}
                    }
				},
				null
		);
	},

	imprimirCE : function() {
		
		doGet("emissaoCE/imprimirCE",
				function(result) {
					if (result.mensagens) {
		
						exibirMensagem(
								result.mensagens.tipoMensagem, 
								result.mensagens.listaMensagens
						);
					}			
				},
				"_blank"
			);
	},

	imprimirCEPDF : function() {
		
		var data = this.getFiltro();
		
		var path = contextPath + "/emissaoCE/imprimirCEPDF";
		
		$.fileDownload(path, {
			httpMethod : "POST",
			data : data,
			successCallback: function(result) {
				if (result.mensagens) {
	
					exibirMensagem(
							result.mensagens.tipoMensagem, 
							result.mensagens.listaMensagens
					);
				}			
			},
            failCallback: function(result) {
	
        		res = $.parseJSON($(result).text());
        		if ((typeof res != "undefined") && (typeof res.mensagens != "undefined")) {
        			
					exibirMensagem(
							res.mensagens.tipoMensagem, 
							res.mensagens.listaMensagens
					);
				}	
			}
		});
		
	},
	
	
	imprimirBoletoEmBranco : function() {
		
		var path = contextPath + "/emissaoCE/imprimeBoletoEmBranco";
		
		$.fileDownload(path, {
			httpMethod : "GET"
		});
		
	},

	cliquePesquisar : function() {
		
		var data = this.getFiltro();
		
		$(".ceEmissaoGrid", this.workspace).flexOptions({			
			url : contextPath + "/emissaoCE/pesquisar",
			dataType : 'json',
			params:data,
			preProcess: this.processaRetornoPesquisa,
		});
		
		$(".ceEmissaoGrid", this.workspace).flexReload();
		
	},
	
	getFiltro : function() {
		
		var data = [];
		
		data.push({name:'filtro.dtRecolhimentoDe',		value: this.get("emissaoCE-dataDe")});
		data.push({name:'filtro.dtRecolhimentoAte',		value: this.get("emissaoCE-dataAte")});
		data.push({name:'filtro.codigoBoxDe',			value: this.get("emissaoCE-boxDe")});
		data.push({name:'filtro.codigoBoxAte',			value: this.get("emissaoCE-boxAte")});
		data.push({name:'filtro.numCotaDe',				value: this.get("emissaoCE-cotaDe")});
		data.push({name:'filtro.numCotaAte',			value: this.get("emissaoCE-cotaAte")});
		data.push({name:'filtro.idRoteiro',				value: this.get("emissaoCE-Roteiro")});
		data.push({name:'filtro.idRota',				value: this.get("emissaoCE-Rota")});
		data.push({name:'filtro.capa',					value: this.get("capa")});
		data.push({name:'filtro.personalizada',			value: this.get("personalizada")});
		
		$.each(this.fornecedoresSelecionados, function(index, row) {
			data.push({name:'filtro.fornecedores[' + index + ']',	value: row.id});
		});
		
		return data;
	},
	
	processaRetornoPesquisa : function(result) {
		
		if (result.mensagens) {

			exibirMensagem(
					result.mensagens.tipoMensagem, 
					result.mensagens.listaMensagens
			);
			
			$(".grids", this.workspace).hide();

			return result;
		}
		
		$.each(result.rows, function(index, row) {
	
			if(!row.cell.nomeCota){
				row.cell.nomeCota = "";
			}
		});
		
		$(".grids", this.workspace).show();
		
		return result;
	},
		
	/**
	 * Atribui valor a um campo da tela
	 * Obs: Checkboxs devem ser atribuidos com o valor de true ou false
	 * 
	 * @param campo - Campo a ser alterado
	 * @param value - valor
	 */
	set : function(campo,value) {
				
		var elemento = $("#" + campo);
		
		if(elemento.attr('type') == 'checkbox') {
			
			if(value) {
				elemento.attr('checked','checked');
			} else {
				elemento.removeAttr('checked');
			}
						
		} else {
			elemento.val(value);
		}
	},
	
	/**
	 * Obtém valor de elemento da tela
	 * @param campo - de onde o valor será obtido
	 */
	get : function(campo) {
		
		var elemento = $("#" + campo , this.workspace);
		
		if(elemento.attr('type') == 'checkbox') {
			return (elemento.attr('checked') == 'checked') ;
		} else {
			return elemento.val();
		}
		
	},
	
	gerarFornecedoresSelecionados : function() {
		
		var cellFornecedores = '';
				
		$.each(this.fornecedoresSelecionados, function(index,fornecedor){
			
			cellFornecedores += '<div class="forncedoresSel">' +
								   '<label>'+ fornecedor.nome +'</label>' +
								   '<a href="javascript:;" onclick="EmissaoCEController.removerFornecedor('+index+');">' +
								   		'<img src="'+ contextPath +'/images/ico_excluir.gif" width="15" height="15" alt="Excluir" border="0" />' +
								   '</a>' +
							   '</div>';
		});
		
		$('#fornecedoresSelecionados', this.workspace).html(cellFornecedores);
		
	},
	
	getFornecedoresSelecionados : function() {
		
		$.each( $('#selectFornecedores', this.workspace).val(), function(index, row) {
			EmissaoCEController.fornecedoresSelecionados.push({id: row.split('_')[0] , nome: row.split('_')[1]});
		});
	},
	
	removerFornecedor : function(index) {
		this.fornecedoresSelecionados.splice(index,1);
		this.gerarFornecedoresSelecionados();
	},
	
	ativarPersonalizada : function(elemento) {
		$('.personalizada', this.workspace).toggle(elemento.checked);
	},
	
	popup_pesq_fornecedor : function() {
		
		 	$("#selectFornecedores", this.workspace).val(this.fornecedoresSelecionados);
		
			$( "#dialog-pesq-fornecedor", this.workspace).dialog({
				resizable: false,
				height:300,
				width:500,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						EmissaoCEController.getFornecedoresSelecionados();
						EmissaoCEController.gerarFornecedoresSelecionados();
						
						$( this ).dialog( "close" );
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				form: $("#dialog-pesq-fornecedor", this.workspace).parents("form")
			});
	},
	
	inicializarGrids : function() {
		
		$(".ceEmissaoGrid", this.workspace).flexigrid({
			colModel : [ {
				display : 'Cota',
				name : 'numCota',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 530,
				sortable : true,
				align : 'left'
			}, {
				display : 'Qtde. Exemplares',
				name : 'qtdeExemplares',
				width : 130,
				sortable : true,
				align : 'center'
			}, {
				display : 'Valor Total CE R$',
				name : 'vlrTotalCe',
				width : 130,
				sortable : true,
				align : 'right'
			}],
				width : 960,
				height : 400,
				sortname : "numCota",
				sortorder : "asc"
			});
			
	},
	
	carregarComboRoteiro : function () {

		var codigoBoxDe, codigoBoxAte;
		codigoBoxDe = $('#emissaoCE-boxDe', this.workspace).val();
		codigoBoxAte= $('#emissaoCE-boxAte', this.workspace).val();
		
		if(codigoBoxAte != '' && codigoBoxDe == '') {
			$('#emissaoCE-boxAte', this.workspace).val('');
        	return;
		}
		
		if(codigoBoxAte != '' && codigoBoxDe > codigoBoxAte) {
			$('#emissaoCE-boxAte', this.workspace).val('');
			tipoMensagem = 'WARNING';
			listaMensagens = [];
			listaMensagens[0] = "Favor escolher o código de box 'De' antes do 'Até'.";
			exibirMensagem(tipoMensagem, listaMensagens);
        	return;
		}
        
        var params = [];
        params.push({name: 'codigoBoxDe', value: codigoBoxDe});
        params.push({name: 'codigoBoxAte', value: codigoBoxAte});
        
        $.postJSON(contextPath + '/cadastro/roteirizacao/carregarComboRoteiroCodigoBox', params,
            function(result) {

				var tipoMensagem = result.tipoMensagem;
				var listaMensagens = result.listaMensagens;
		          
		        if (tipoMensagem && listaMensagens) {
		        	exibirMensagem(tipoMensagem, listaMensagens);
		        	return;
		        } 
		        
		        $('#emissaoCE-Rota', this.workspace).empty();
		        $("#emissaoCE-Roteiro > option", this.workspace).remove();
		        $('#emissaoCE-Roteiro', this.workspace)
            	.append('<option value=""></option>');
                $.each(result, function(index, row){
                        $('#emissaoCE-Roteiro', this.workspace)
                        	.append('<option value="'+row.id+'">'+row.descricaoRoteiro+'</option>');
                    }
                );
            },
            null,
            true
        );
    },
    
    carregarComboRota : function () {

    	$('#emissaoCE-Rota', this.workspace).empty();
    	idRoteiro = $('#emissaoCE-Roteiro', this.workspace).val();

        $.postJSON(contextPath + '/cadastro/roteirizacao/carregarComboRota', { 'roteiroId' : idRoteiro },
            function(result) {
        		$('#emissaoCE-Rota', this.workspace).append('<option value=""></option>');
                $.each(result, function(index, row){
                        $('#emissaoCE-Rota', this.workspace).append('<option value="'+row.id+'">'+row.descricaoRota+'</option>');
                    }
                );

            },
            null,
            true
        );

    },
	
}, BaseController);

$(function() {
	EmissaoCEController.init();
				
});
//@ sourceURL=emissaoCE.js
