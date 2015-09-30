var analiseEstudoController = $.extend(true, {

    init : function() {
        $(".estudosGrid").flexigrid({
            preProcess : analiseEstudoController.executarPreProcessEstudosGrid,
            dataType : 'json',
            colModel : [ {
                display : 'Estudo',
                name : 'numeroEstudo',
                width : 80,
                sortable : true,
                align : 'left'
            }, {
                display : 'Código',
                name : 'codigoProduto',
                width : 60,
                sortable : true,
                align : 'left'
            }, {
                display : 'Produto',
                name : 'nomeProduto',
                width : 180,
                sortable : true,
                align : 'left'
            }, {
                display : 'Edição',
                name : 'numeroEdicaoProduto',
                width : 50,
                sortable : true,
                align : 'left'
            }, {
                display : 'Classificação',
                name : 'descicaoTpClassifProd',
                width : 100,
                sortable : true,
                align : 'left'
            },{
            	display : 'Dt Lançamento',
                name : 'dataLancamento',
                width : 60,
                sortable : true,
                align : 'center'            	
            } ,{
                display : 'Período',
                name : 'codPeriodoProd',
                width : 50,
                sortable : true,
                align : 'center'
            }, {
                display : 'Permite Parcial',
                name : 'permiteParcial',
                width : 160,
                sortable : true,
                align : 'left',
                hide: true
            }, {
                display : 'Tela de Análise',
                name : 'telaAnalise',
                width : 160,
                sortable : true,
                align : 'left'
            }, {
                display : 'Status',
                name : 'descricaoStatus',
                width : 100,
                sortable : true,
                align : 'left'
            }],
            sortname : "numeroEstudo",
            sortorder : "desc",
            usepager : true,
            useRp : true,
            rp : 15,
            showTableToggleBtn : true,
            width : 950,
            height : 200
        });
        
        $("#dataLancamento", analiseEstudoController.workspace).mask("99/99/9999");
        
        $('#analise-estudo-produto').keyup(function (){
			pesquisaProduto.autoCompletarPorNomeProduto('#analise-estudo-produto', false);
		});
		
		$('#analise-estudo-produto').blur(function (){
			pesquisaProduto.pesquisarPorNomeProduto('#codProduto', '#analise-estudo-produto', {}, false, undefined, analiseEstudoController.errorCallBack);
		});
		
		$('#dataLancamento').blur(function (){
			analiseEstudoController.validarData($("#dataLancamento", analiseEstudoController.workspace).val());
		});
		
		$("#dataLancamento", analiseEstudoController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		
    },

    executarPreProcessEstudosGrid : function(resultado){

        if (resultado.mensagens) {
            exibirMensagem(
                    resultado.mensagens.tipoMensagem, 
                    resultado.mensagens.listaMensagens
            );

            return resultado;
        }

        $.each(resultado.rows, function(index, row) {
            var analise = '';
            if (row.cell.permiteParcial) {
                analise = '<select name="select" id="select" style="width:140px;" onchange="analiseEstudoController.redirectToTelaAnalise('+ 
                    row.cell.numeroEstudo +',event);"> <option selected="selected">Selecione...</option> <option value="normal">Normal</option> <option value="parcial">Parcial</option>';
            } else {
                analise = '<select name="select" id="select" style="width:140px;" onchange="analiseEstudoController.redirectToTelaAnalise('+ 
                    row.cell.numeroEstudo +',event);"> <option selected="selected">Selecione...</option> <option value="normal">Normal</option>';
            }
            row.cell.telaAnalise = analise;
            
            if (!row.cell.codPeriodoProd) {
            	
            	row.cell.codPeriodoProd = "";
            }
        });

        $(".grids", analiseEstudoController.workspace).show();

        return resultado;
    },

    carregarEstudos : function() {
    	
        var data = [{name : 'filtro.numEstudo', value : $("#idEstudo").val()}, 
                    {name : 'filtro.codigoProduto', value : $("#codProduto").val()}, 
                    {name : 'filtro.nome', value : $("#analise-estudo-produto").val()}, 
                    {name : 'filtro.numeroEdicao', value : $("#edicaoProd").val()},
                    {name : 'filtro.idTipoClassificacaoProduto', value : $("#comboClassificacao").val()},
                    {name : 'filtro.dataLancamento', value :  $("#dataLancamento", analiseEstudoController.workspace).val()}];

        $(".estudosGrid").flexOptions({url: contextPath + "/distribuicao/analiseEstudo/buscarEstudos", 
            params: data});

        $(".estudosGrid").flexReload();	
    },

    redirectToTelaAnalise : function(numeroEstudo, event){

        // Obter matriz de distribuição
        var matriz = [],
        	url = contextPath + "/distribuicao/analiseEstudo/obterMatrizDistribuicaoPorEstudo",
        	dadosResumo ={};
        
        $.postJSON(url,
                [{name : "id" , value : numeroEstudo}],
                function(response){
            // CALLBACK
            // ONSUCESS
            matriz.push({name: "selecionado.classificacao",  value: response.classificacao});
            matriz.push({name: "selecionado.nomeProduto",    value: response.nomeProduto});
            matriz.push({name: "selecionado.codigoProduto",  value: response.codigoProduto});
            matriz.push({name: "selecionado.dataLcto",       value: response.dataLancto});
            matriz.push({name: "selecionado.edicao",         value: response.numeroEdicao});
            matriz.push({name: "selecionado.estudo",         value: response.idEstudo});
            matriz.push({name: "selecionado.idLancamento",   value: response.idLancamento});
            matriz.push({name: "selecionado.estudoLiberado", value: (response.liberado != "")});

            // CARREGAR TELA EMS 2022
            $.get(contextPath + '/distribuicao/histogramaPosEstudo/index', //url
                    null, // parametros
                    function(html){ // onSucessCallBack
                $('#AnaliseEstudoMainContent').hide();
                $('#histogramaPosEstudoContent').html(html).show();

                histogramaPosEstudoController.dadosResumo = dadosResumo;
                histogramaPosEstudoController.matrizSelecionado = matriz;
                histogramaPosEstudoController.popularFieldsetHistogramaPreAnalise(matriz, undefined, false);
                histogramaPosEstudoController.modoAnalise = $(event.target).val().toUpperCase();
            });
        }
        );
    },
    
    errorCallBack : function errorCallBack(){
		$('#codProduto').val('');
		$('#analise-estudo-produto').val('');
	},
    
	validarData : function validarData(pObj) {

		if(pObj != ""){
	  		
	  		var data_quebrada = pObj.split('/');
	  		
	  		dia = data_quebrada[0]; 
	  		mes = data_quebrada[1]; 
	  		ano = data_quebrada[2];
	  		
    		if (dia > 31) 
    			analiseEstudoController.exibirMensagem("Verifique o dia.");
    		else 
    		  if (mes > 12) 
    			  analiseEstudoController.exibirMensagem("Verifique o Mês.");	
    		  else
    			if (ano < 1970 || ano > 2070)
    				analiseEstudoController.exibirMensagem("Verifique o Ano.");
    	  }
	  	},
	  	
  	exibirMensagem : function exibirMensagem(mensagem){

  		var erros = new Array();
        erros[0] = mensagem;
        exibirMensagemDialog('WARNING',   erros,"");

        this.closeDialogPopUpSegmento1 = false;

        return;
  	},

}, BaseController);
//@ sourceURL=analiseEstudo.js


