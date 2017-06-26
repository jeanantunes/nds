var manutencaoPublicacaoController = $.extend(true, {
	
	url:contextPath + "/financeiro/manutencaoPublicacao",
	codigoProduto:null,
	numeroEdicao:null,
	nomeProduto:null,
	
	init : function() {
		
		$("#manut-publicacao-edicaoProduto", manutencaoPublicacaoController.workspace).numeric();
		
		$('#manut-publicacao-novoPrecoProduto,#novoDescontoInput', manutencaoPublicacaoController.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
		
		focusSelectRefField($("#codigoProduto"));
	},
	
	pesquisar: function (){

        manutencaoPublicacaoController.codigoProduto =  $("#manut-publicacao-codigoProduto", manutencaoPublicacaoController.workspace).val() ;
        manutencaoPublicacaoController.numeroEdicao = $("#manut-publicacao-edicaoProduto", manutencaoPublicacaoController.workspace).val();
        manutencaoPublicacaoController.nomeProduto = $("#manut-publicacao-nomeProduto", manutencaoPublicacaoController.workspace).val();


        var data = {
            codigo: $("#manut-publicacao-codigoProduto", manutencaoPublicacaoController.workspace).val(),
            numeroEdicao: $("#manut-publicacao-edicaoProduto", manutencaoPublicacaoController.workspace).val()
        };

        if($('#opcoesVisualizacao2').is(':checked')){

        	var target = null;
        	if($('#descontoOpcoesProduto').is(':checked')){
                target = "/pesquisarDescontosProduto";
			}else if($('#descontoOpcoesCota').is(':checked')){
                target = "/pesquisarDescontosPorCota";
                data={cotaNumero:cota.value}
			}

        	$.postJSON(manutencaoPublicacaoController.url + target,data,function(result){

        		console.log(result);
        		var descontos = result.list;
                $('#descontoAtualSel').empty();
        		for(var x=0;x < descontos.length;x++){
                    descontos[x] = parseFloat(descontos[x]).toFixed(2).toString().replace('.',',');

                    $('#descontoAtualSel').append('<option value="'+descontos[x]+'">'+descontos[x]+'</option>')
				}


                var nomePublicacao = "" ;
                if($('#descontoOpcoesProduto').is(':checked')){
                    nomePublicacao = "Publicação " ;
                    nomePublicacao = nomePublicacao.concat(manutencaoPublicacaoController.codigoProduto,
                        " - ",manutencaoPublicacaoController.nomeProduto,
                        " - ",manutencaoPublicacaoController.numeroEdicao);
                }else  if($('#descontoOpcoesCota').is(':checked')){
                    nomePublicacao = "Cota " +(cota.value) + " - "+ (nomeCota.value);
				}

                $('#novoDesconto-txtLegenda').text(nomePublicacao);
                $('#novoDescontoFields').show();

			},manutencaoPublicacaoController.tratraErroProduto);
        }else{



            $.postJSON(manutencaoPublicacaoController.url + "/pesquisarProduto",
                data,
                manutencaoPublicacaoController.renderizarDaodosProduto,
                manutencaoPublicacaoController.tratraErroProduto);
        }

	},
	
	renderizarDaodosProduto:function(precoProduto){

        $('#novoDescontoFields').hide();

        if($('#opcoesVisualizacao2').is(':checked')){
            $('#novoDescontoFields').show()

        }else{
            $("#manut-publicacao-resultado",manutencaoPublicacaoController.workspace).show();
            $("#manut-publicacao-txtPrecoProduto",manutencaoPublicacaoController.workspace).text(precoProduto);
            $("#manut-publicacao-txtLegenda",manutencaoPublicacaoController.workspace).text(manutencaoPublicacaoController.montarTituloFieldPublicacao());
            $("#manut-publicacao-novoPrecoProduto",manutencaoPublicacaoController.workspace).val("");
            $("#manut-publicacao-novoPrecoProduto",manutencaoPublicacaoController.workspace).focus();

		}




	},

	pesquisarDescontoCota:function(){

	},
	
	montarTituloFieldPublicacao:function(){
		
		var nomePublicacao = "Publicação: " ;
		
		return nomePublicacao.concat(manutencaoPublicacaoController.codigoProduto,
									" - ",manutencaoPublicacaoController.nomeProduto,
									" - ",manutencaoPublicacaoController.numeroEdicao);
	},
	
	tratraErroProduto:function(){
		
		$("#manut-publicacao-resultado",manutencaoPublicacaoController.workspace).hide();
		manutencaoPublicacaoController.codigoProduto = null;
		manutencaoPublicacaoController.numeroEdicao = null;
	},
	
	confirmarAlteracaoPreco:function(){
		
		var data = {codigo:manutencaoPublicacaoController.codigoProduto,
				    numeroEdicao:manutencaoPublicacaoController.numeroEdicao,
				    precoProduto:$("#manut-publicacao-novoPrecoProduto", manutencaoPublicacaoController.workspace).val()};
		
		$.postJSON(manutencaoPublicacaoController.url + "/confirmarAlteracaoPreco", 
				   data,
				   manutencaoPublicacaoController.precoAlteradoComSucesso);
	},
	
	precoAlteradoComSucesso:function(result){

		console.log(result);
		if(result.listaMensagens){
			exibirMensagem(result.tipoMensagem,result.listaMensagens);
		}
		
		$("#manut-publicacao-resultado",manutencaoPublicacaoController.workspace).hide();
		$("#manut-publicacao-codigoProduto", manutencaoPublicacaoController.workspace).val("") ;
		$("#manut-publicacao-edicaoProduto", manutencaoPublicacaoController.workspace).val("");
		$("#manut-publicacao-nomeProduto", manutencaoPublicacaoController.workspace).val("");
		$("#manut-publicacao-novoPrecoProduto",manutencaoPublicacaoController.workspace).val("");
        $('#novoDescontoInput').val('');
		manutencaoPublicacaoController.codigoProduto = null;
		manutencaoPublicacaoController.numeroEdicao = null;
        $('#novoDescontoFields').hide()
	},
	
	isPublicacaoInformada:function(){
		
		return (manutencaoPublicacaoController.codigoProduto != null
		&& manutencaoPublicacaoController.numeroEdicao != null);
	},
	
	popupConfirmacao:function(){

		if( ($('#opcoesVisualizacao1').is(':checked') && !manutencaoPublicacaoController.isPublicacaoInformada()) ||
            ($('#opcoesVisualizacao2').not(':checked') && !manutencaoPublicacaoController.isPublicacaoInformada() )  ||
			($('#opcoesVisualizacao2').is(':checked') && $('#descontoOpcoesProduto').is(':checked') && !manutencaoPublicacaoController.isPublicacaoInformada() )
			){
			exibirMensagem("WARNING",["Informe uma publicação para alteração."]);
			return;
		}
		
		$("#dialog-confirmacao-alteracao-preco", manutencaoPublicacaoController.workspace).dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$(this).dialog("close");

                    if($('#opcoesVisualizacao1').is(':checked')){
                        manutencaoPublicacaoController.confirmarAlteracaoPreco();
                    }else if($('#opcoesVisualizacao2').is(':checked')){

                        if($('#descontoOpcoesProduto').is(':checked')){
                            manutencaoPublicacaoController.atualizarDesconto();
						}else  if($('#descontoOpcoesCota').is(':checked')){
                            manutencaoPublicacaoController.atualizarDesconto();
                        }

					}
				},
				"Cancelar": function() {
					$(this).dialog("close");
				}
			},
			form: $("#dialog-confirmacao-alteracao-preco", this.workspace).parents("form")
		});
	},

	atualizarDesconto:function(){

        var data = {
            codigoProduto: manutencaoPublicacaoController.codigoProduto,
            numeroEdicao: manutencaoPublicacaoController.numeroEdicao,
            cotaNumero:cota.value,
			descontoAtual:$("#descontoAtualSel", manutencaoPublicacaoController.workspace).val(),
            novoDesconto: $("#novoDescontoInput", manutencaoPublicacaoController.workspace).val()
        };

        $.postJSON(manutencaoPublicacaoController.url + "/atualizarDescontos",
            data,
            manutencaoPublicacaoController.precoAlteradoComSucesso);

	}


}, BaseController);
//@ sourceURL=manutencaoPublicacao.js
