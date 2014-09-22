var TipoTransferencia = {
    COTA : {value: 'COTA'},
    ROTA : {value: 'ROTA'},
    ROTEIRO : { value: 'ROTEIRO'}
};

var TipoEdicao =  {
    NOVO : {value: 'NOVO'},
    ALTERACAO : {value: 'ALTERACAO'}
};

var TipoInclusao = {
    ROTEIRO : {value: 'ROTEIRO'},
    ROTA : {value: 'ROTA'}
};

var TipoExclusao = {
    ROTEIRO : {value: 'ROTEIRO'},
    ROTA : {value: 'ROTA'},
    COTA: {value: 'COTA'}
};

var roteiroSelecionadoAutoComplete = false;
var transferirRotasComNovoRoteiro = false;
var transferirRoteirizacaoComNovaRota= false;
var pesquisaPorCota = false;

var roteirizacao = $.extend(true, {

    tipoTransferencia: null,
    tipoEdicao: null,
    idRoteirizacao : "",
    idBox : "",
    nomeBox: "",
    idRoteiro: "",
    idRota: "",
    nomeBox: "",
    nomeRoteiro : "",
    ordemRotaSelecionada: "",
    ordemRoteiroSelecionado:"",
    pdvsSelecionados: [],
    nomeRota : "",
    tipoInclusao: TipoInclusao.ROTEIRO,
    tipoExclusao: null,
    idsCotas: [],
    boxReadonly : false,
    modificada: false,

    
    
    definirTransferenciaCota : function() {
        if (!roteirizacao.isTransferenciaCota()) {
            roteirizacao.tipoTransferencia = TipoTransferencia.COTA;
        }
    },

    definirTransferenciaRota : function() {
        if (!roteirizacao.isTransferenciaRota()) {
            roteirizacao.tipoTransferencia = TipoTransferencia.ROTA;
        }
    },

    definirTransferenciaRoteiro : function() {
        if (!roteirizacao.isTransferenciaRoteiro()) {
            roteirizacao.tipoTransferencia = TipoTransferencia.ROTEIRO;
        }
    },

    isTransferenciaCota : function() {
        return TipoTransferencia.COTA == roteirizacao.tipoTransferencia;
    },

    isTransferenciaRota : function() {
        return TipoTransferencia.ROTA == roteirizacao.tipoTransferencia;
    },

    isTransferenciaRoteiro : function() {
        return TipoTransferencia.ROTEIRO == roteirizacao.tipoTransferencia;
    },

    definirTipoEdicao : function(tipoEdicao) {
        roteirizacao.tipoEdicao = tipoEdicao;
    },

    isNovo : function() {
        return TipoEdicao.NOVO == roteirizacao.tipoEdicao;
    },

    isAlteracao : function() {
        return TipoEdicao.ALTERACAO == roteirizacao.tipoEdicao;
    },

    abrirTelaNovoRoteiroRota : function () {
        this.limparCamposNovaInclusao();
        this.popupNovoRoteiroRota();
        this.init();
    },
    
    obterProximaOrdemRoteiro : function() {
    	
    	$.postJSON(contextPath + '/cadastro/roteirizacao/obterProximaOrdemRoteiro', null,
                function(result) {

                    roteirizacao.limparTelaRoteiro();
                    $('#inputOrdem', roteirizacao.workspace).numeric();
                    $("#inputOrdem", roteirizacao.workspace).val(result.int);

                }, null, true);
    },
    
    obterProximaOrdemRota : function(value) {
    	
    	var idRoteiro = value ? value :  $("#selectIncluirEmRoteiro", roteirizacao.worksapce).val();
    
    	var params = [{name: 'idRoteiro', 
    				  value: idRoteiro}];
    	
    	$.postJSON(contextPath + '/cadastro/roteirizacao/obterProximaOrdemRota',
                params,
                function(result) {

                    roteirizacao.limparTelaRoteiro();
                    $('#inputOrdem', roteirizacao.workspace).numeric();
                    $("#inputOrdem", roteirizacao.workspace).val(result.int);
                }, null, true);
    },
    
    confirmarInclusaoRoteiro : function() {
        
    	var tipoRoteiro = 'NORMAL';
        
    	if ( $('input[name=tipoRoteiro]').is(':checked') ){
            tipoRoteiro = 'ESPECIAL';
        }
        
    	var params = {'idBox' :  roteirizacao.idBox,
    				  'ordem' :  $("#inputOrdem", roteirizacao.workspace).val(),
    				  'nome'  :  $("#inputNome", roteirizacao.workspace).val(),
    				  'tipoRoteiro' : tipoRoteiro};
    	
        $.postJSON(contextPath + '/cadastro/roteirizacao/incluirRoteiro', params,
            function(result) {
                
        		var tipoMensagem = result.tipoMensagem;
                var listaMensagens = result.listaMensagens;

                if (tipoMensagem && listaMensagens) {
                    exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
                } else {
                    roteirizacao.popularGridRoteiros(result.roteiros);
                    $('#dialog-novo-dado', roteirizacao.workspace).dialog("close");
                }

            },
            null,
            true
        );
        
        roteirizacao.modificada = true;
    },

    confirmarInclusaoRota : function() {
    	
    	var params = {'roteiroId' :  $("#selectIncluirEmRoteiro", roteirizacao.worksapce).val(),
                	  'ordem' 	  :  $("#inputOrdem", roteirizacao.worksapce).val(),
                	  'nome' 	  :  $("#inputNome", roteirizacao.workspace).val()};
    	
        $.postJSON(contextPath + '/cadastro/roteirizacao/incluirRota',params,
            function(result) {

                var tipoMensagem = result.tipoMensagem;
                var listaMensagens = result.listaMensagens;

                if (tipoMensagem && listaMensagens) {
                    exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialog-novo-dado');
                } else {
                	
                	if (roteirizacao.idRoteiro == $("#selectIncluirEmRoteiro", roteirizacao.worksapce).val()) {
                		roteirizacao.popularGridRotas(result.rotas);
                    }
                    $('#dialog-novo-dado', roteirizacao.workspace).dialog("close");
                }
            },
            null,
            true
        );
        
        roteirizacao.modificada = true;
    },
    
    popupNovoRoteiroRota : function() {
      	 $("#dialog-novo-dado", roteirizacao.workspace ).dialog({
               resizable: false,
               height:"auto",
               width:"auto",
               modal: true,
               form: $("#dialog-novo-dado", this.workspace).parents("form")
           });
      	 
      	 this.limparCamposNovaInclusao();
      	 this.switchNovoRoteiroRota();
      },
       
      switchNovoRoteiroRota : function(value) {
       	
       	var switchTo = value ? value : $("#selectNovoRoteiroRota", roteirizacao.workspace).val();
       	
       	if(switchTo == "rota") {
       		
       		roteirizacao.tipoInclusao == TipoInclusao.ROTA;
       		
       		this.atualizarPopupNovaRota();
       		
       		this.carregarComboRoteirosSessao();
       		
       		$("#selectIncluirEmRoteiro", roteirizacao.workspace).show();
       	
       	} else {
       		
       		roteirizacao.tipoInclusao == TipoInclusao.ROTEIRO;
       		
       		this.atualizarPopupNovoRoteiro();
       		
       		this.obterProximaOrdemRoteiro();
       		
       		$("#selectNovoRoteiroRota",  roteirizacao.workspace).val("roteiro");
       		$("#selectIncluirEmRoteiro", roteirizacao.workspace).html("");
       		$("#selectIncluirEmRoteiro", roteirizacao.workspace).hide();
       	
       	}
    }, 
    
    atualizarPopupNovoRoteiro : function() {
    	
    	$("#dialog-novo-dado", roteirizacao.workspace ).dialog('option', 'title', 'Novo Roteiro');
   		
   		$("#dialog-novo-dado", roteirizacao.workspace ).dialog('option', 'buttons', {
   			"Confirmar": function() {
   				 roteirizacao.confirmarInclusaoRoteiro();
               },
               "Cancelar": function() {
                   $( this ).dialog( "close" );
               }
   		 });
    },
    
    atualizarPopupNovaRota : function() {
    	
    	$("#dialog-novo-dado", roteirizacao.workspace ).dialog('option', 'title', 'Nova Rota');
    	
    	$("#dialog-novo-dado", roteirizacao.workspace ).dialog('option', 'buttons', {
   			"Confirmar": function() {
   				roteirizacao.confirmarInclusaoRota();
               },
               "Cancelar": function() {
                   $( this ).dialog( "close" );
               }
   		 });
    },
    
    //Busca dados para o auto complete do nome da cota
    autoCompletarRoteiroPorNome : function(idRoteiro, callBack) {

        var descricao = $(idRoteiro, roteirizacao.workspace).val();

        descricao = $.trim(descricao);

        $(idRoteiro, roteirizacao.workspace).autocomplete({source: ""});

        roteiroSelecionadoAutoComplete = false;
        if (descricao && descricao.length > 1) {


            $.postJSON(
                contextPath + "/cadastro/roteirizacao/autoCompletarRoteiroPorDescricao",
                {
                    'descricao' : descricao

                },
                function(result) {
                    roteirizacao.exibirAutoComplete(result, idRoteiro ,callBack);
                },
                null,
                true
            );
        }
    },
    
    
    carregarComboRoteiro : function () {
        roteirizacao.resetComboRoteiroPesquisa();
        roteirizacao.resetComboRotaPesquisa('#rotaPesquisa');
        
        var params = {'boxId' :$('#boxPesquisa', roteirizacao.workspace).val()};
        
        $.postJSON(contextPath + '/cadastro/roteirizacao/carregarComboRoteiro', params,
            function(result) {

				var tipoMensagem = result.tipoMensagem;
				var listaMensagens = result.listaMensagens;
		          
		        if (tipoMensagem && listaMensagens) {
		        	exibirMensagem(tipoMensagem, listaMensagens);
		        	return;
		        } 
		        $("#selectIncluirEmRoteiro > option", roteirizacao.workspace).remove();
                $.each(result, function(index, row){
                        $('#roteiroPesquisa', roteirizacao.workspace)
                        	.append('<option value="'+row.id+'">'+row.descricaoRoteiro+'</option>');
                    }
                );
            },
            null,
            true
        );
    },
    
    
    carregarComboRoteirosSessao : function() {

		var params = {"idBox": roteirizacao.idBox};
		
		var _this = this;
		
		$.postJSON(contextPath + '/cadastro/roteirizacao/buscarRoteirosSessao', params, 
			function(result) {
				
				var tipoMensagem = result.tipoMensagem;
				var listaMensagens = result.listaMensagens;
	          
		        if (tipoMensagem && listaMensagens) {
		        	exibirMensagem(tipoMensagem, listaMensagens);
		        	return;
		        } 
		        		        
		        if (!result.length > 0) {
		        	exibirMensagem("WARNING",["É necessario ter pelo menos um roteiro para incluir uma nova Rota"]);
		        	_this.limparCamposNovaInclusao();
		        	return;
		        }
		        
		        var idRoteiro = result[0].id;
		        
		        _this.obterProximaOrdemRota(idRoteiro);
		        
		       $("#selectIncluirEmRoteiro > option", roteirizacao.workspace).remove();
	           $.each(result, function(index, row){
	        	   
	               $('#selectIncluirEmRoteiro', roteirizacao.workspace)
	               		.append('<option value="'+row.id+'">'+row.nome+'</option>');
	           });

		       
			}, 
			null, 
			true
		);
    },
    
    
    carregarComboRoteiroEspecial : function () {
    	
        roteirizacao.resetComboRoteiroPesquisa();
        roteirizacao.resetComboRotaPesquisa('#rotaPesquisa');
        
        $.postJSON(contextPath + '/cadastro/roteirizacao/carregarComboRoteiroEspecial',null ,
            function(result) {
        	 		$("#selectIncluirEmRoteiro > option", roteirizacao.workspace).remove();
                $.each(result, function(index, row){
                        $('#roteiroPesquisa', roteirizacao.workspace)
                        	.append('<option value="'+row.id+'">'+row.descricaoRoteiro+'</option>');
                    }
                );
            },
            null,
            true
        );
    },
    
    carregarComboRotasEspeciais : function () {
        
        $.postJSON(contextPath + '/cadastro/roteirizacao/carregarRotasEspeciais',null ,
            
        		function(result) {
        	 		
        		$("#selectNovasRotas > option", roteirizacao.workspace).remove();
        	 		
        	 		$.each(result, function(index, row){
                        $('#selectNovasRotas', roteirizacao.workspace)
                        	.append('<option value="'+row.id+'">'+row.nome+'</option>');
                    }
                );
            },
            null,
            true
        );
    },
    
    //Exibe o auto complete no campo
    exibirAutoComplete : function(result, idCampo ,callBack) {
        $(idCampo, roteirizacao.workspace).autocomplete({
            source: result,
            focus : function(event, ui) {
                roteiroSelecionadoAutoComplete = true;
            },
            close : function(event, ui) {
                roteiroSelecionadoAutoComplete = true;
            },
            select : function(event, ui) {
                roteiroSelecionadoAutoComplete = true;
                if (callBack){
                    callBack(ui.item.chave,true);
                }
                //  roteirizacao.populaDadosRoteiro(ui.item.chave);

            },
            minLength: 2,
            delay : 0
        });
    },

    //Busca dados para o auto complete do nome da cota
    buscaRoteiroPorNome : function(campo) {
        var descricao = $(campo).val();
        descricao = $.trim(descricao);
        if ( !roteiroSelecionadoAutoComplete  && descricao != "") {

            $.postJSON(
                contextPath + "/cadastro/roteirizacao/buscaRoteiroPorDescricao",
                {
                    'descricao' : descricao

                },
                function(result) {

                    var tipoMensagem = result.tipoMensagem;
                    var listaMensagens = result.listaMensagens;
                    if (tipoMensagem && listaMensagens) {
                        exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
                        roteirizacao.prepararPopupRoteirizacao();
                    } else {
                        roteirizacao.populaDadosRoteiro(result[0], false);
                    }
                },
                null,
                true
            );
        }
        roteiroSelecionadoAutoComplete = false;
    },

    //Busca dados para o auto complete do nome da cota
    populaDadosRoteiro : function(roteiro, autoComplete) {
        roteiroSelecionadoAutoComplete = autoComplete;
        if ( roteiro.tipoRoteiro == "ESPECIAL" ){
            $('#spanDadosRoteiro', roteirizacao.workspace).html('<strong>Roteiro Selecionado:</strong> '+ roteiro.descricaoRoteiro+' - <strong>Ordem: </strong>'+roteiro.ordem);
        } else {
            $('#spanDadosRoteiro', roteirizacao.workspace).html('<strong>Roteiro Selecionado:</strong> '+ roteiro.descricaoRoteiro+' - <strong>Box: </strong>'+roteiro.box.nome+' - <strong>Ordem: </strong>'+roteiro.ordem);
        }
        $('#idRoteiroSelecionado', roteirizacao.workspace).val(roteiro.id);
        roteirizacao.populaListaCotasRoteiro(roteiro.id);
        roteirizacao.habilitaBotao("botaoNovaRota",function(){roteirizacao.abrirTelaRota();});
        $('#nomeRota', roteirizacao.workspace).removeAttr('readonly');
        roteirizacao.habilitaBotao('botaoNovaRotaNome', function(){roteirizacao.abrirTelaRotaComNome();});

    },

    //Busca dados para o auto complete do nome da cota
    populaListaCotasRoteiro : function(roteiroId) {
        $(".cotasDisponiveisGrid", roteirizacao.workspace).clear();
        $(".cotasRotaGrid", roteirizacao.workspace).clear();
        roteirizacao.iniciarGridRotas();
        $(".rotasGrid", roteirizacao.workspace).flexOptions({
            "url" : contextPath + '/cadastro/roteirizacao/pesquisarRotaPorNome',
            params : [{
                name : "roteiroId",
                value : roteiroId
            }, {
                name : "nomeRota",
                value :$('#nomeRota').val()
            }],
            newp:1
        });

        $(".rotasGrid", roteirizacao.workspace).flexReload();
    },

    iniciarGridRotas : function(){

        $(".rotasGrid", roteirizacao.workspace).flexigrid({
            preProcess: function(data) {
                $.each(data.rows, function(index, value) {

                	var rota = value.cell;
                	
                    var id = rota.id ? rota.id : null;
                                                          
                    var selecione = '<input id="rota'+id+'" type="radio" value="' + id +'" name="rotaRadio" ';
                    
                    if(!id && rota.pdvs.length < 1 ) {
                    	selecione += 'class = "entregador-rota"';
                    }
                    
                    selecione += 'onclick="roteirizacao.rotaSelecionadaListener(\'' +  id  + '\', \''+ rota.nome +'\', \''+rota.ordem+'\');"';
                    
                    if (id == roteirizacao.idRota) {
                        selecione += 'checked';
                    }
                    
                    selecione += '/>';
                    
                    value.cell.selecione = selecione;
                });
                return data;
            },
            onSuccess: function() {
            	$(".entregador-rota").parents('tr').css("color", "#999");
            },
            dataType : 'json',
            colModel : [{
                display : 'Ordem',
                name : 'ordem',
                width : 35,
                sortable : false,
                align : 'left'
            }, {
                display : 'Nome',
                name : 'nome',
                width : 160,
                sortable : false,
                align : 'left'
            }, {
                display : '',
                name : 'selecione',
                width : 20,
                sortable : false,
                align : 'center'
            }],
            width : 270,
            height : 140,
            disableSelect: true
        });
        roteirizacao.limparGridRotas();

    },

    popularGridRotas : function(data) {
        if (!data){
            $.postJSON(contextPath + '/cadastro/roteirizacao/recarregarRotas',
                {'idRoteiro' :  roteirizacao.idRoteiro},
                function(result) {
                    $(".rotasGrid", roteirizacao.workspace).flexAddData({rows: result.rows, page : result.page, total : result.total});
                    return;
                },
                null,
                true
            );
        } else {
            $(".rotasGrid", roteirizacao.workspace).flexAddData({rows: toFlexiGridObject(data), page : 1, total : data.length});
        }
        roteirizacao.limparGridCotasRota();
        roteirizacao.limparInfoCotasRota();
    },

    rotaSelecionadaListener : function(idRota, nomeRota, ordemRota) {
    	
        roteirizacao.idRota = idRota;
        roteirizacao.nomeRota = nomeRota;
        roteirizacao.ordemRotaSelecionada = ordemRota;
        roteirizacao.popularGridCotasRota();
        roteirizacao.definirTransferenciaRota();
        roteirizacao.tipoExclusao = TipoExclusao.ROTA;
        roteirizacao.popularInfoCotasRota();

    },

    roteiroSelecionadoListener : function(idRoteiro, descricaoRoteiro, ordemRoteiro) {
        roteirizacao.idRoteiro = idRoteiro;
        roteirizacao.nomeRoteiro = descricaoRoteiro;
        roteirizacao.ordemRoteiroSelecionado = ordemRoteiro;
        roteirizacao.idRota = "";
        roteirizacao.nomeRota = "";
        roteirizacao.ordemRotaSelecionada = "";
        roteirizacao.definirTransferenciaRoteiro();
        roteirizacao.popularGridRotas();
        roteirizacao.tipoInclusao = TipoInclusao.ROTA;
        roteirizacao.tipoExclusao = TipoExclusao.ROTEIRO;
    },
    
    boxSelecionadoListener : function(idBox, nomeBox) {
        roteirizacao.tipoInclusao = TipoInclusao.ROTEIRO;
        
        if (!roteirizacao.boxReadonly) {
            
        	var isBoxSelecionado = roteirizacao.idBox != "";
           
            var isBoxDiferente = roteirizacao.idBox != idBox;
            
            if (isBoxSelecionado && isBoxDiferente && roteirizacao.modificada) {
               
            	var dialog = new ConfirmDialog("Ao alterar o Box selecionado as informações não confirmadas serão perdidas.<br/>Confirma?", function() {
                
            		roteirizacao.processarAlteracaoBox(idBox, nomeBox);
                    return true;
                    
                }, null);
            	
                dialog.open();
                
            } else {
                if (isBoxDiferente) {
                    roteirizacao.processarAlteracaoBox(idBox, nomeBox);
                }
            }
        }
    },
    

    popularInfoCotasRota : function() {
        var info = '<strong>Box: </strong>' + roteirizacao.idBox + '-' + roteirizacao.nomeBox;
        info += ' <strong> - Roteiro Selecionado: </strong>' + roteirizacao.nomeRoteiro;
        info += ' <strong> - Rota: </strong>' + roteirizacao.nomeRota;
        $('#cotasRota', roteirizacao.workspace).html(info);
    },

    limparInfoCotasRota : function() {
        $('#cotasRota', roteirizacao.workspace).empty();
    },

    limparGridRotas : function() {
        roteirizacao.idRota = "";
        roteirizacao.nomeRota = "";
        roteirizacao.limparInfoCotasRota();
        $(".rotasGrid", roteirizacao.workspace).flexAddData({rows:[], page:0, total:0});
    },

    pesquisarRotas : function() {
        roteirizacao.idRota = "";
        roteirizacao.nomeRota = "";
        roteirizacao.limparInfoCotasRota();
        roteirizacao.limparGridCotasRota();

        $(".rotasGrid", roteirizacao.workspace).flexOptions({
            url : contextPath + "/cadastro/roteirizacao/recarregarRotas",
            params: [{name: 'idRoteiro', value: roteirizacao.idRoteiro},
                {name: 'descricaoRota', value: $('#descricaoRota', roteirizacao.workspace).val()}]
        });
        $(".rotasGrid", roteirizacao.workspace).flexReload();
    },

    iniciarGridBox : function(){
        $(".boxRoteirizacaoGrid", roteirizacao.workspace).flexigrid({
            preProcess: function(data) {
                $.each(data.rows, function(index, value) {

                    var id = value.cell.id;
                    var nome = value.cell.nome;
                    var selecione = '<input type="radio" value="' + id +'" name="boxRadio" id="radioBox'+nome+'" ';
                    selecione += 'onclick="roteirizacao.boxSelecionadoListener(\'' +  id  + '\',\'' + nome + '\');"';
                    if (id == roteirizacao.idBox) {
                        selecione += 'checked="checked"';
                    }
                    selecione += '/>';
                    value.cell.selecione = selecione;
                });
                return data;
            },
            dataType : 'json',
            colModel : [{
                display : 'Nome',
                name : 'nome',
                width : 190,
                sortable : false,
                align : 'left'
            }, {
                display : '',
                name : 'selecione',
                width : 20,
                sortable : false,
                align : 'center'
            }],
            autoload : false,
            disableSelect: true,
            width : 270,
            height : 140,

            url: contextPath + '/cadastro/roteirizacao/recarregarBox',
            onSubmit    : function(){
                $('.boxRoteirizacaoGrid').flexOptions({params: [{name: 'nomeBox', value: $('#nomeBox').val()}]});
                return true;
            }
        });
        roteirizacao.limparGridBox();
    },

    popularGridBox : function(data) {
        if (data) {
            $(".boxRoteirizacaoGrid", roteirizacao.workspace).flexAddData({rows: toFlexiGridObject(data), page : 1, total : data.length});
        } else {
            roteirizacao.limparGridBox();
        }
    },

    limparGridBox : function() {
        $(".boxRoteirizacaoGrid", roteirizacao.workspace).flexAddData({rows: [], page : 0, total : 0});
    },

    pesquisarBox : function() {
        roteirizacao.limparInfoCotasRota();
        $(".boxRoteirizacaoGrid", roteirizacao.workspace).flexReload();
        roteirizacao.idBox = "";
        roteirizacao.nomeBox = "";
        roteirizacao.limparGridRoteiros();
        roteirizacao.limparGridRotas();
        roteirizacao.limparGridCotasRota();
    },

   
    processarAlteracaoBox : function(idBox, nomeBox) {
        roteirizacao.idBox = idBox;
        roteirizacao.nomeBox = nomeBox;
        roteirizacao.idRoteiro = "";
        roteirizacao.idRota = "";
        roteirizacao.limparInfoCotasRota();
        roteirizacao.switchNovoRoteiroRota("roteiro");
        
        $.postJSON(contextPath + '/cadastro/roteirizacao/boxSelecionado',
            [{name: 'idBox', value: idBox}],
            function(result) {
                var tipoMensagem = result.tipoMensagem;
                var listaMensagens = result.listaMensagens;
                if (tipoMensagem && listaMensagens) {
                    exibirMensagem(tipoMensagem, listaMensagens);
                } else {
                    if (TipoEdicao.NOVO.value == result.tipoEdicao) {
                        roteirizacao.definirTipoEdicao(TipoEdicao.NOVO);
                    } else {
                        roteirizacao.definirTipoEdicao(TipoEdicao.ALTERACAO);
                        roteirizacao.idRoteirizacao = result.id;
                    }
                    roteirizacao.popularGrids(result);
                }
            },
            null,
            true
        );
       roteirizacao.modificada = false;
    },

    popularGrids : function(data) {
    	
        if (data.box) {
            roteirizacao.idBox = data.box.id;
            roteirizacao.nomeBox = data.box.nome;
        }
        roteirizacao.popularGridBox(data.boxDisponiveis);
        if (data.roteiros && data.roteiros.length > 0) {
            if (roteirizacao.idRoteiro == "") {
                roteirizacao.idRoteiro = data.roteiros[0].id;
                roteirizacao.nomeRoteiro = data.roteiros[0].nome;
            }
            roteirizacao.popularGridRoteiros(data.roteiros);
            if (data.roteiros[0].rotas) {
                if (roteirizacao.idRota == "" && data.roteiros[0].rotas[0]) {
                	
                    roteirizacao.idRota = data.roteiros[0].rotas[0].id;
                    roteirizacao.nomeRota = data.roteiros[0].rotas[0].nome;
                    roteirizacao.ordemRotaSelecionada = data.roteiros[0].rotas[0].ordem;
                }
                roteirizacao.popularGridRotas(data.roteiros[0].rotas);
                if (data.roteiros[0].rotas[0] && data.roteiros[0].rotas[0].pdvs) {
                    roteirizacao.popularGridCotasRota(data.roteiros[0].rotas[0].pdvs);
                }
            }
            roteirizacao.popularInfoCotasRota();
        } else {
            roteirizacao.limparGridRoteiros();
            roteirizacao.limparGridRotas();
            roteirizacao.limparGridCotasRota();
        }
    },

    iniciarGridRoteiros : function(){
        $(".roteirosGrid", roteirizacao.workspace).flexigrid({
            preProcess: function(data) {
                $.each(data.rows, function(index, value) {
                    var id = value.cell.id;
                    var selecione = '<input id="roteiro'+value.cell.id+'" type="radio" value="' + id +'" name="roteirosRadio" ';
                    selecione += 'onclick="roteirizacao.roteiroSelecionadoListener(\'' +  id  + '\', \'' + value.cell.nome + '\', \''+value.cell.ordem+'\');"';
                    if (id == roteirizacao.idRoteiro) {
                        selecione += 'checked';
                    }
                    selecione += '/>';
                    value.cell.selecione = selecione;
                });
                return data;
            },
            dataType : 'json',
            colModel : [{
                display : 'Ordem',
                name : 'ordem',
                width : 35,
                sortable : false,
                align : 'left'
            }, {
                display : 'Nome',
                name : 'nome',
                width : 160,
                sortable : false,
                align : 'left'
            }, {
                display : '',
                name : 'selecione',
                width : 20,
                sortable : false,
                align : 'center'
            }],
            width : 270,
            height : 140,
            disableSelect: true
        });
        roteirizacao.limparGridRoteiros();

    },

    popularGridRoteiros : function(data) {

        if (!data){
            $.postJSON(contextPath + '/cadastro/roteirizacao/buscarRoteirosSessao',
                null,
                function(result) {
                    $(".roteirosGrid", roteirizacao.workspace).flexAddData({rows: toFlexiGridObject(result), page : 1, total : result.length});
                    roteirizacao.limparGridRotas();
                    return;
                },
                null,
                true
            );
        } else {
            $(".roteirosGrid", roteirizacao.workspace).flexAddData({rows: toFlexiGridObject(data), page : 1, total : data.length});
        }
    },

    limparGridRoteiros : function() {
        roteirizacao.idRoteiro = "";
        $(".roteirosGrid", roteirizacao.workspace).flexAddData({rows:[], page:0, total:0});
    },

    pesquisarRoteiros : function() {
        $(".roteirosGrid", roteirizacao.workspace).flexOptions({
            url : contextPath + "/cadastro/roteirizacao/recarregarRoteiros",
            params: [{name: 'idBox', value: roteirizacao.idBox},
                {name: 'descricaoRoteiro', value: $('#descricaoRoteiro', roteirizacao.workspace).val()}]
        });

        $(".roteirosGrid", roteirizacao.workspace).flexReload();
        roteirizacao.idRoteiro = "";
        roteirizacao.limparGridRotas();
        roteirizacao.limparGridCotasRota();
    },

    abrirTelaRota : function () {


    },

    buscaRotasSelecionadas : function(){
        rotasSelecinadas = new Array();
        $("input[type=checkbox][name='rotaCheckbox']:checked", roteirizacao.workspace).each(function(){
            rotasSelecinadas.push(parseInt($(this).val()));
        });

        return rotasSelecinadas;
    },

    excluirRota : function() {
    	
    	var params = {'rotaId'   : roteirizacao.idRota,
                	  'roteiroId': roteirizacao.idRoteiro,
            		  'ordemRota': roteirizacao.ordemRotaSelecionada};
    	
        $.postJSON(contextPath + '/cadastro/roteirizacao/excluirRota',params,
            function(result) {
                var tipoMensagem = result.tipoMensagem;
                var listaMensagens = result.listaMensagens;

                if (tipoMensagem && listaMensagens) {
                    exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
                }
                roteirizacao.ordemRotaSelecionada = "";
                roteirizacao.idRota = "";
            
                roteirizacao.popularGridRotas();
            },
            null,
            true
        );
        roteirizacao.modificada = true;
    },

    excluirRoteiro : function(){
    	
    	var params =  {'roteiroId' :  roteirizacao.idRoteiro, 'ordemRoteiro':roteirizacao.ordemRoteiroSelecionado};
    	
        $.postJSON(contextPath + '/cadastro/roteirizacao/excluirRoteiro',params,
            function(result) {
                var tipoMensagem = result.tipoMensagem;
                var listaMensagens = result.listaMensagens;

                if (tipoMensagem && listaMensagens) {
                    exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
                }

                roteirizacao.popularGridRoteiros();
            },
            null,
            true
        );
        roteirizacao.modificada = true;
    },

    excluirCotas : function(){

        var params = [{name: 'idRota', value: roteirizacao.idRota}, 
                      {name: 'idRoteiro', value: roteirizacao.idRoteiro}, 
                      {name: 'ordemRota', value:roteirizacao.ordemRotaSelecionada}];
        
        var pdvsSelecionados = $(".checkboxCotasRota:checked", roteirizacao.workspace);
        
        if(pdvsSelecionados.length < 1) {
        	 exibirMensagemDialog("WARNING", ["É necessario selecionar os pdvs para a exclusão!"]);
        }
        
        $.each(pdvsSelecionados, function(index, value){

            params.push({name: 'pdvs['+ index +']', value: value.value});
        });
        
        $.postJSON(contextPath + '/cadastro/roteirizacao/removerPdvs',
            params,
            function(result) {

                if (result.tipoMensagem && result.listaMensagens){

                    exibirMensagemDialog(result.tipoMensagem, result.listaMensagens);
                    return;
                }

                roteirizacao.popularGridCotasRota();
            },
            null,
            true
        );
        roteirizacao.modificada = true;
    },

    popupExcluirRotaRoteiro : function() {

        if (roteirizacao.tipoExclusao == TipoExclusao.ROTA){
        	
        	if(!roteirizacao.idRota) {
        		exibirMensagemDialog("WARNING", ["É necessario selecionar a rota que deseja excluir!"]);
        		return;
        	}
        	
            $("#msgConfExclusaoRotaRoteiro", roteirizacao.workspace).text(
                "Confirma a exclusão da rota " + roteirizacao.nomeRota + "?");

            $("#dialog-excluir-rota-roteiro", roteirizacao.workspace).attr("title", "Rotas");
        } else if (roteirizacao.tipoExclusao == TipoExclusao.ROTEIRO){
        	
        	if(!roteirizacao.idRoteiro) {
        		exibirMensagemDialog("WARNING", ["É necessario selecionar o roteiro que deseja excluir!"]);
        		return;
        	}
        	
            $("#msgConfExclusaoRotaRoteiro", roteirizacao.workspace).text(
                "Confirma a exclusão do roteiro " + roteirizacao.nomeRoteiro + "?");

            $("#dialog-excluir-rota-roteiro", roteirizacao.workspace).attr("title", "Roteiros");
        } else {

            $("#msgConfExclusaoRotaRoteiro", roteirizacao.workspace).text(
                "Confirma a exclusão da(s) cota(s) selecionada(s)?");

            $("#dialog-excluir-rota-roteiro", roteirizacao.workspace).attr("title", "Cotas");
        }


        $( "#dialog-excluir-rota-roteiro", roteirizacao.workspace ).dialog({
            resizable: false,
            height:'auto',
            width:400,
            modal: true,
            buttons: {
                "Confirmar": function() {

                    if (roteirizacao.tipoExclusao == TipoExclusao.ROTA){

                        roteirizacao.excluirRota();
                    } else if (roteirizacao.tipoExclusao == TipoExclusao.ROTEIRO){

                        roteirizacao.excluirRoteiro();
                    } else {

                        roteirizacao.excluirCotas();
                    }

                    $(this).dialog("close");
                },
                "Cancelar": function() {
                    $(this).dialog("close");
                }
            },
            form: $("#dialog-excluir-rota-roteiro", this.workspace).parents("form")
        });

    },

    popupDetalhesCota : function(title, box, roteiro, rota) {

        $('#legendDetalhesCota').html(title);

        $( "#dialog-detalhes" ).dialog({
            resizable: false,
            height:'auto',
            width:'auto',
            modal: true,
            buttons: {
                "Fechar": function() {
                    $( this ).dialog( "close" );
                }
            },
            form: $("#dialog-detalhes", this.workspace).parents("form")
        });
    },

    popupTransferirRota : function() {
        $("#roteiroTranferenciaNome", roteirizacao.workspace).val('');
        $("#dialog-transfere-rota", roteirizacao.workspace ).dialog({
            resizable: false,
            height:'auto',
            width:410,
            modal: true,
            buttons: {
                "Confirmar": function() {
                    if ( transferirRotasComNovoRoteiro ){
                        roteirizacao.transferirRotasComNovoRoteiro();
                    } else {
                        roteirizacao.transferirRotas();
                    }

                    $( this ).dialog( "close" );

                },
                "Cancelar": function() {
                    $( this ).dialog( "close" );
                }
            },
            form: $("#dialog-transfere-rota", this.workspace).parents("form")
        });
        
    },


    transferirRotasComNovoRoteiro : function() {
        var tipoRoteiro = 'NORMAL';
        if ( $('input[name=tipoRoteiroTranferencia]').is(':checked') ){
            tipoRoteiro = 'ESPECIAL';
        }
        $.postJSON(contextPath + '/cadastro/roteirizacao/transferirRotasComNovoRoteiro',
            {

                'rotasId' : roteirizacao.buscaRotasSelecionadas(),
                'idBox' :  $("#boxRoteiroTranferencia", roteirizacao.workspace).val(),
                'ordem' :  $("#ordemRoteiroTranferencia", roteirizacao.workspace).val(),
                'roteiroNome' :  $("#roteiroTranferenciaNome", roteirizacao.workspace).val(),
                'tipoRoteiro' : tipoRoteiro


            },
            function(result) {
                var tipoMensagem = result.tipoMensagem;
                var listaMensagens = result.listaMensagens;
                if (tipoMensagem && listaMensagens) {
                    exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
                }
                $(".rotasGrid", roteirizacao.workspace).flexReload();

            },
            null,
            true
        );
        roteirizacao.modificada = true;
    },

    selecionaRoteiroTranferencia : function(roteiro) {
        $('#roteiroTranferenciaSelecionadoId', roteirizacao.workspace).val(roteiro.id);
        $('#roteiroTranferenciaSelecionadoNome', roteirizacao.workspace).val(roteiro.descricaoRoteiro);
    },

    exibiRoteiroNovoTranferencia : function (){
        $('.roteiroNovo', roteirizacao.workspace).show();
        transferirRotasComNovoRoteiro = true;
    },

    escondeRoteiroNovoTranferencia : function (){
        $('.roteiroNovo', roteirizacao.workspace).hide();
        transferirRotasComNovoRoteiro = false;
    },

    //Busca dados para o auto complete do nome da cota
    filtroGridRotasPorNome : function() {

        if ( $.trim($('#nomeRota', roteirizacao.workspace).val()) != ''){
            $(".rotasGrid", roteirizacao.workspace).flexOptions({
                'url' : contextPath + '/cadastro/roteirizacao/pesquisarRotaPorNome',
                params : [{
                    name : 'roteiroId',
                    value : $('#idRoteiroSelecionado', roteirizacao.workspace).val()
                }, {
                    name : 'nomeRota',
                    value :$('#nomeRota', roteirizacao.workspace).val()
                }],
                newp:1
            });

            $(".rotasGrid", roteirizacao.workspace).flexReload();
        }
    },

    iniciarGridCotasRota : function(){
        $(".cotasRotaGrid").flexigrid({
            preProcess : function(data) {
            	
                $.each(data.rows, function(index, value) {
                    var id = value.cell.id;
                    var selecione = '<input type="checkbox" class="checkboxCotasRota" onclick="roteirizacao.selecaoCota();" name="checkboxCotasRota" value="'+ id +'"/>';
                    value.cell.selecione = selecione;
                    var ordem = '<input type="text"  class="inputOrdemCotasRota" onchange="roteirizacao.ordemPdvChangeListener(this, \''+ id + '\');" class="inputGridCotasRota" value="'+ value.cell.ordem  +'" style="width:30px; text-align:center;">';
                    value.cell.ordem = ordem;
                });
                return data;
            },
            dataType : 'json',
            colModel : [ {
                display : 'PDV',
                name : 'pdv',
                width : 120,
                sortable : true,
                align : 'left'
            },{
                display : 'Origem',
                name : 'origem',
                width : 50,
                sortable : true,
                align : 'left'
            }, {
                display : 'Endereço',
                name : 'endereco',
                width : 325,
                sortable : true,
                align : 'left'
            }, {
                display : 'Cota',
                name : 'cota',
                width : 50,
                sortable : true,
                align : 'left'
            }, {
                display : 'Nome',
                name : 'nome',
                width : 170,
                sortable : true,
                align : 'left'
            }, {
                display : 'Ordem',
                name : 'ordem',
                width : 40,
                sortable : true,
                align : 'left'
            }, {
                display : '',
                name : 'selecione',
                width : 15,
                sortable : false,
                align : 'center'
            }],
            autoload : false,
            sortname : "ordem",
            sortorder: "asc",
            url: contextPath + '/cadastro/roteirizacao/recarregarCotasRota',
            onSubmit    : function(){
                $('.cotasRotaGrid', roteirizacao.workspace).flexOptions({params: [
                    {name:'idRoteiro', value: roteirizacao.idRoteiro},
                    {name:'idRota', value: roteirizacao.idRota},
                    {name:'ordemRota', value: roteirizacao.ordemRotaSelecionada}
                ]});
                return true;
            },
            onSuccess : function() {
               $('.inputOrdemCotasRota', roteirizacao.workspace).numeric();
            },
            width : 875,
            height : 150
        });
        roteirizacao.limparGridCotasRota();
    },

    selecionarTodosPdvs : function() {
        if ($('#selecionarTodosPdv', roteirizacao.workspace).is(':checked')) {
            $(".checkboxCotasRota", roteirizacao.workspace).prop('checked', true);
            this.selecaoCota();
        } else {
            $(".checkboxCotasRota", roteirizacao.workspace).prop('checked', false);
        }
    },

    ordemPdvChangeListener : function(element, idPdv) {
        var ordemAntiga = element.defaultValue;
        var ordem = $(element).val();
        var param = [{name: 'idRoteiro', value: roteirizacao.idRoteiro},
            {name: 'idRota', value: roteirizacao.idRota},
            {name: 'idPdv', value: idPdv},
            {name: 'ordem',  value: ordem}];

        $.postJSON(contextPath + '/cadastro/roteirizacao/ordemPdvChangeListener', param,
            function(result) {
                var tipoMensagem = result.tipoMensagem;
                var listaMensagens = result.listaMensagens;
                if (tipoMensagem == "SUCCESS") {
                    element.defaultValue = ordem;
                    $(".cotasRotaGrid", roteirizacao.workspace).flexReload();
                } else {
                    exibirMensagemDialog(tipoMensagem, listaMensagens);
                    $(element).val(ordemAntiga);
                }
            },
            null,
            true
        );
        roteirizacao.modificada = true;
    },

    limparGridCotasRota : function() {
        roteirizacao.idsCotas = [];
        roteirizacao.limparInfoCotasRota();
        $(".cotasRotaGrid", roteirizacao.workspace).flexAddData({rows: [], page : 0, total : 0});
    },

    popularGridCotasRota : function(data) {
        if (!data) {
            $(".cotasRotaGrid", roteirizacao.workspace).flexReload();
        } else {
            $(".cotasRotaGrid", roteirizacao.workspace).flexAddData({rows: toFlexiGridObject(data), page : 1, total : data.length});
        }
        roteirizacao.idsCotas = [];
    },

    limparGridCotasRota : function() {
        $(".cotasRotaGrid", roteirizacao.workspace).flexAddData({rows: [], page : 0, total : 0});
    },

    populaDadosCota : function(rotaId) {
        $.postJSON(contextPath + '/cadastro/roteirizacao/buscarRotaPorId',
            {
                'rotaId' :rotaId
            }
            ,
            function(result) {

                $('#spanDadosRota', roteirizacao.workspace).html('<strong>Rota Selecionada:</strong>&nbsp;'+result.descricaoRota+ '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>Ordem:'+result.ordem +'</strong>&nbsp;');
            },
            null,
            true
        );

    },

    cotaSelecionada : function(rotaId) {
        roteirizacao.populaDadosCota(rotaId);
        roteirizacao.populaCotasRotaGrid(rotaId);
        roteirizacao.habilitaBotao('botaoCotaAusentes', function(){roteirizacao.abrirTelaCotas();}); // desabilitaBotao('botaoCotaAusentes');

    },

    abrirTelaCotas : function () {
        if (roteirizacao.idRota == "") {
            exibirMensagemDialog("WARNING", ["Selecione uma Rota para adicionar PDV's"]);
            return;
        }
        
        if ($("input[name=boxRadio]:checked", roteirizacao.workspace).val() == -1){
        	
        	$("#tipoPesquisa", roteirizacao.workspace).val("pdv");
        } else {
        	
        	$("#tipoPesquisa", roteirizacao.workspace).val("cota");
        }
        
        roteirizacao.iniciaCotasDisponiveisGrid();

        $("#cepPesquisa", roteirizacao.workspace).mask("99999-999");
        $("#cotaPesquisaPdv", roteirizacao.workspace).val('');
        $("#cotaPesquisaPdv", roteirizacao.workspace).justInput(/[0-9]/);
        $("#nomeCotaPesquisaPdv", roteirizacao.workspace).val('');
        $("#selTodos", roteirizacao.workspace).uncheck();

        var params = {};
        if($('#radioBoxEspecial').attr('checked') == 'checked')
        	params = [{name:'boxEspecial', value: true}];
        
        $.postJSON(contextPath + '/cadastro/roteirizacao/iniciaTelaCotas', params,
            function(result) {

                roteirizacao.populaComboUf(result);

                $( "#dialog-cotas-disponiveis", roteirizacao.workspace ).dialog({
                    resizable: false,
                    height:550,
                    width:900,
                    modal: true,
                    buttons: {
                        "Confirmar": function() {
                            var pdvs = [];
                            $('input:checkbox[name=pdvCheckbox]:checked', roteirizacao.workspace).each(function() {
                                pdvs.push({name: 'pdvs', value: $(this).val()});
                            });
                            if (pdvs.length > 0) {
                                roteirizacao.adicionaPdvs();
                                $( this ).dialog( "close" );
                            } else {
                                exibirMensagem('WARNING', ['Selecione pelo menos um PDV!']);
                            }


                        },
                        "Cancelar": function() {
                            $( this ).dialog( "close" );
                        }
                    },
                    form: $("#dialog-cotas-disponiveis", this.workspace).parents("form")
                });
            },
            null,
            true
        );
    },

    buscalistaMunicipio : function () {
    	
    	params = [{name:'uf', value: $('#comboUf', roteirizacao.workspace).val()}];
    	if($('#radioBoxEspecial').attr('checked') == 'checked') {
    		params.push({name:'boxEspecial', value: true});
    	}    		
    		
        $.postJSON(contextPath + '/cadastro/roteirizacao/buscalistaMunicipio',
            params,
            function(result) {
                roteirizacao.populaMunicipio(result);
            },
            null,
            true
        );

    },

    buscalistaBairro : function () {
    	
    	params = [
    	          	{name:'uf', value: $('#comboUf', roteirizacao.workspace).val()},
    	          	{name:'municipio', value: $('#comboMunicipio', roteirizacao.workspace).val()}
    	         ];
    	if($('#radioBoxEspecial').attr('checked') == 'checked') {
    		params.push({name:'boxEspecial', value: true});
    	}   
    	
        $.postJSON(contextPath + '/cadastro/roteirizacao/buscalistaBairro',
        	params,
            function(result) {
                roteirizacao.populaBairro(result);
            },
            null,
            true
        );

    },

    populaComboUf : function(result) {
        $('#comboUf > option', roteirizacao.workspace).remove();
        $('#comboUf', roteirizacao.workspace).append('<option value="">Todos</option>');
        roteirizacao.resetComboBairro();
        roteirizacao.resetComboMunicipio();

        $.each(result, function(index, row){
                $('#comboUf', roteirizacao.workspace).append('<option>'+row+'</option>');
            }
        );

    },

    populaMunicipio : function(result) {
        $('#comboMunicipio > option', roteirizacao.workspace).remove();
        $('#comboMunicipio', roteirizacao.workspace).append('<option value="">Todos</option>');
        roteirizacao.resetComboBairro();
        roteirizacao.resetComboMunicipio();
        $.each(result, function(index, row){
                $('#comboMunicipio', roteirizacao.workspace).append('<option value="'+row+'">'+row+'</option>');
            }
        );

    },

    populaBairro : function(result) {
        $('#comboBairro > option', roteirizacao.workspace).remove();
        $('#comboBairro', roteirizacao.workspace).append('<option value="">Todos</option>');
        roteirizacao.resetComboBairro();
        $.each(result, function(index, row){
                $('#comboBairro', roteirizacao.workspace).append('<option>'+row+'</option>');
            }
        );

    },

    resetComboMunicipio : function(){
        $('#comboMunicipio > option', roteirizacao.workspace).remove();
        $('#comboMunicipio', roteirizacao.workspace).append('<option value="" >Todos</option>');
    },

    resetComboBairro : function(){
        $('#comboBairro > option', roteirizacao.workspace).remove();
        $('#comboBairro', roteirizacao.workspace).append('<option value="" >Todos</option>');
    },

    buscaPdvsDisponiveis : function() {

        var numCota = $("#cotaPesquisaPdv", this.workspace).val();
        var municipio = $("#comboMunicipio", this.workspace).val();
        var uf = $("#comboUf", this.workspace).val();
        var bairro = $("#comboBairro", this.workspace).val();
        var cep = $("#cepPesquisa", this.workspace).val();
        var pesquisaPorCota = $("#tipoPesquisa", this.workspace).val() == "cota";
        
        var params = [
                      {name:'numCota', value:numCota},
                      {name:'municipio', value:municipio},
                      {name:'uf', value:uf},
                      {name:'bairro', value:bairro},
                      {name:'cep', value:cep},
                      {name:'boxID', value: roteirizacao.idBox},
                      {name: 'pesquisaPorCota', value:pesquisaPorCota},
                      {name: 'idRoteiro', value:roteirizacao.idRoteiro},
                      {name: 'idRota', value:roteirizacao.idRota}
                     ];
        
        $(".cotasDisponiveisGrid", this.workspace).flexOptions({
            url: contextPath + "/cadastro/roteirizacao/obterPdvsDisponiveis",
            params: params ,
            newp: 1
        });

        $(".cotasDisponiveisGrid", this.workspace).flexReload();

        $(".grids", this.workspace).show();
    },

    iniciaCotasDisponiveisGrid : function(){
        $(".cotasDisponiveisGrid", roteirizacao.workspace).clear();
        $(".cotasDisponiveisGrid", roteirizacao.workspace).flexigrid({
            preProcess:roteirizacao.callCotasDisponiveisGrid,
            dataType : 'json',
            colModel : [ {
                display : 'PDV',
                name : 'pdv',
                width : 95,
                sortable : true,
                align : 'left'
            }, {
                display : 'Origem',
                name : 'origem',
                width : 60,
                sortable : true,
                align : 'left'
            }, {
                display : 'Endereço',
                name : 'endereco',
                width : 270,
                sortable : true,
                align : 'left'
            }, {
                display : 'Cota',
                name : 'cota',
                width : 40,
                sortable : true,
                align : 'left'
            }, {
                display : 'Nome',
                name : 'nome',
                width : 150,
                sortable : true,
                align : 'left'
            }, {
                display : 'Ordem',
                name : 'ordem',
                width : 55,
                sortable : true,
                align : 'center'
            }, {
                display : '',
                name : 'selecionado',
                width : 20,
                sortable : false,
                align : 'center'
            }],
            sortname : "cota",
            sortorder : "asc",
            width : 800,
            height : 250
        });
    },

    callCotasDisponiveisGrid :  function (data){

        if  ( data.rows.length == 0   ){
            exibirMensagemDialog("WARNING", ["Não exitem cota disponíveis."],'dialogRoteirizacaoCotaDisponivel');

        }

        $.each(data.rows, function(index, value) {
            var idPontoVenda = value.cell.id;
            var valOrdem = value.cell.ordem;
            var selecionado = '<input type="checkbox" value="'+idPontoVenda+'" name="pdvCheckbox" id="pdvCheckbox'+idPontoVenda+'"class=" checkboxNovosPdvs" />';
            var ordem ='<input type="input" value="'+valOrdem+'" name="pdvOrdem" id="pdvOrdem" size="6" length="6" />';
            value.cell.selecionado = selecionado;
            value.cell.ordem = ordem;

        });

        $(".grids", roteirizacao.workspace).show();

        return data;
    },

    selecionarTodosNovosPdvs : function(checked){

        $(".checkboxNovosPdvs", roteirizacao.workspace).prop('checked', checked);

        var elem = document.getElementById("textoSelTodos", roteirizacao.workspace);

        if (checked){
            elem.innerHTML = "Desmarcar todos";
        }
        else{
            elem.innerHTML = "Marcar todos";
        }
    },

    pesquisarPvsPorCota : function(){
        $('#cotaDisponivelPesquisa', roteirizacao.workspace).html('');
        roteirizacao.carregarNomeCotasPesquisa('cotaDisponivelPesquisa',  $('#numeroCotaPesquisa', roteirizacao.workspace).val(), function(){roteirizacao.buscarPvsPorCota();} );

    },

    buscarPvsPorCota : function() {
        pesquisaPorCota = true;
        $(".cotasDisponiveisGrid", roteirizacao.workspace).flexOptions({
            'url' : contextPath + '/cadastro/roteirizacao/buscarPvsPorCota',
            params : [{
                name : 'numeroCota',
                value : $('#numeroCotaPesquisa', roteirizacao.workspace).val()
            },
                {
                    name : "rotaId",
                    value : $('#rotaSelecionada', roteirizacao.workspace).val()
                },

                {
                    name : "roteiroId",
                    value : $('#idRoteiroSelecionado', roteirizacao.workspace).val()
                }
            ],
            newp:1
        });

        $(".cotasDisponiveisGrid", roteirizacao.workspace).flexReload();
    },

    buscaPdvsSelecionados : function(){

        var linhasDaGrid = $(".cotasDisponiveisGrid tr");

        var params = new Array();

        $.each(linhasDaGrid, function(index, value) {

            var linha = $(value);

            var colunaPdv = linha.find("td")[0];
            var colunaOrigem = linha.find("td")[1];
            var colunaEndereco = linha.find("td")[2];
            var colunaCota = linha.find("td")[3];
            var colunaNome = linha.find("td")[4];
            var colunaOrdem = linha.find("td")[5];
            var colunaSelecionado = linha.find("td")[6];

            var pdv =
                $(colunaPdv).find("div").html();

            var origem =
                $(colunaOrigem).find("div").html();

            var endereco =
                $(colunaEndereco).find("div").html();

            var cota =
                $(colunaCota).find("div").html();

            var nome =
                $(colunaNome).find("div").html();

            var ordem =
                $(colunaOrdem).find("div").find('input[name="pdvOrdem"]').val();

            var id =
                $(colunaSelecionado).find("div").find('input[name="pdvCheckbox"]').val();


            var checked = document.getElementById("pdvCheckbox"+id).checked;
            if (checked == true) {

                params.push({name: "pdvs["+index+"].id", value: id});
                params.push({name: "pdvs["+index+"].pdv", value: pdv});
                params.push({name: "pdvs["+index+"].origem", value: origem});
                params.push({name: "pdvs["+index+"].endereco", value: endereco});
                params.push({name: "pdvs["+index+"].cota", value: cota});
                params.push({name: "pdvs["+index+"].nome", value: nome});
                params.push({name: "pdvs["+index+"].ordem", value: ordem});
            }

        });

        params.push({name: "idRoteiro", value: roteirizacao.idRoteiro});
        params.push({name: "idRota", value: roteirizacao.idRota});
        params.push({name: "ordemRota", value: roteirizacao.ordemRotaSelecionada});

        return params;
    },

    adicionaPdvs : function() {
        var params = roteirizacao.buscaPdvsSelecionados();
        $.postJSON(contextPath + '/cadastro/roteirizacao/adicionarNovosPdvs',
            params,
            function(result) {

                if (result.tipoMensagem && result.listaMensagens){

                    exibirMensagemDialog(result.tipoMensagem, result.listaMensagens);
                }

                $(".cotasRotaGrid", roteirizacao.workspace).flexReload();
            },
            null,
            true
        );
        roteirizacao.modificada = true;
    },

    popupExcluirPdvs : function() {
        $( "#dialog-excluir-pdvs", roteirizacao.workspace ).dialog({
            resizable: false,
            height:'auto',
            width:400,
            modal: true,
            buttons: {
                "Confirmar": function() {

                    $( this ).dialog( "close" );
                },
                "Cancelar": function() {

                    $( this ).dialog( "close" );
                }
            },
            form: $("#dialog-excluir-pdvs", this.workspace).parents("form")
        });

    },

    confirmaRoteirizacao : function () {
    	
        var params = roteirizacao.populaParamentrosContaSelecionadas();
        $.postJSON(contextPath + '/cadastro/roteirizacao/confirmaRoteirizacao', params,
            function(result) {
                        		
        		$(".cotasRotaGrid", roteirizacao.workspace).flexReload();
                var tipoMensagem = result.tipoMensagem;
                var listaMensagens = result.listaMensagens;
                if (tipoMensagem && listaMensagens) {
                    exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
                }
                
            },
            null,
            true
        );
        roteirizacao.novaRoteirizacao();

    },

    populaParamentrosContaSelecionadas : function(){
        var dados ="";
        var index = 0;
        $("input[type=checkbox][name='pdvCheckbox']:checked", roteirizacao.workspace).each(function(){
            if (dados != ""){
                dados+=",";
            }

            var idPontoVenda =  $(this).val();
            var ordem = $('#pdvOrdem'+idPontoVenda).val();
            dados+='{name:"lista['+index+'].idPontoVenda",value:'+idPontoVenda+'}, {name:"lista['+index+'].ordem",value:'+ordem+'}';
            index++;
        });
        dados+=',{name:"idRota",value:'+$('#rotaSelecionada', roteirizacao.workspace).val()+'}';
        var params = '['+dados+ ']';

        return eval(params);
    },

    limparTelaRoteirizacao:function(){
        $("#lstRoteiros", roteirizacao.workspace).val('');
        $(".rotasGrid", roteirizacao.workspace).clear();
        $(".cotasDisponiveisGrid", roteirizacao.workspace).clear();
        $(".cotasRotaGrid", roteirizacao.workspace).clear();
        $('#spanDadosRoteiro', roteirizacao.workspace).html('<strong>Roteiro Selecionado:</strong>&nbsp;&nbsp; <strong>Box: </strong>&nbsp;&nbsp; <strong>Ordem: </strong>&nbsp;');
        $('#spanDadosRota', roteirizacao.workspace).html('<strong>Rota Selecionada:</strong>&nbsp;&nbsp;&nbsp;&nbsp; <strong>Ordem: </strong>&nbsp;');

    },

    limparTelaRoteiro:function(){
        $('#boxInclusaoRoteiro', roteirizacao.workspace).val('');
        $('#inputNome', roteirizacao.workspace).val('');
        $("#tipoRoteiro", roteirizacao.workspace).attr("checked",false);

    },

    popupTransferirCota : function() {
        $('#lstRotaTranferencia', roteirizacao.workspace).val('');
        $("#dialog-transfere-cotas", roteirizacao.workspace ).dialog({
            resizable: false,
            height:250,
            width:410,
            modal: true,
            buttons: {
                "Confirmar": function() {

                    if ( transferirRoteirizacaoComNovaRota) {
                        roteirizacao.transferirRoteirizacaoComNovaRota();
                    } else {
                        roteirizacao.transferirRoteirizacao();
                    }
                    roteirizacao.escondeRotaNovaTranferencia();
                    $( this ).dialog( "close" );
                    $("#effect", roteirizacao.workspace).hide("highlight", {}, 1000, callback);

                },
                "Cancelar": function() {
                    roteirizacao.escondeRotaNovaTranferencia();
                    $( this ).dialog( "close" );
                }
            },
            form: $("#dialog-transfere-cotas", this.workspace).parents("form")
        });

    },

    //Busca dados para o auto complete do nome da cota
    autoCompletarRotaPorNome : function(idRota, callBack) {

        var descricao = $(idRota).val();

        descricao = $.trim(descricao);

        $(idRota).autocomplete({source: ""});

        if (descricao && descricao.length > 1) {

            $.postJSON(
                contextPath + "/cadastro/roteirizacao/autoCompletarRotaPorDescricao",
                {
                    'roteiroId' : $('#idRoteiroSelecionado').val(),
                    'nomeRota' : descricao

                },
                function(result) {
                    roteirizacao.exibirAutoComplete(result, idRota ,callBack);
                },
                null,
                true
            );
        }
    },

    //Busca dados para o auto complete do nome da cota
    autoCompletarRotaPorNome : function(idRota, callBack) {

        var descricao = $(idRota).val();

        descricao = $.trim(descricao);

        $(idRota).autocomplete({source: ""});

        if (descricao && descricao.length > 1) {

            $.postJSON(
                contextPath + "/cadastro/roteirizacao/autoCompletarRotaPorDescricao",
                {
                    'roteiroId' : $('#idRoteiroSelecionado', roteirizacao.workspace).val(),
                    'nomeRota' : descricao

                },
                function(result) {
                    roteirizacao.exibirAutoComplete(result, idRota ,callBack);
                },
                null,
                true
            );
        }
    },

    transferirRoteirizacao : function() {
        var descricao = $('#lstRotaTranferencia', roteirizacao.workspace).val();
        descricao = $.trim(descricao);
        $.postJSON(
            contextPath + "/cadastro/roteirizacao/transferirRoteirizacao",
            {
                'roteirizacaoId' :roteirizacao.buscaRoteirizacaoSelecionadas(),
                'roteiroId' : $('#idRoteiroSelecionado', roteirizacao.workspace).val(),
                'rotaNome' : descricao

            },
            function(result) {
                $(".cotasRotaGrid", roteirizacao.workspace).flexReload();
                var tipoMensagem = result.tipoMensagem;
                var listaMensagens = result.listaMensagens;
                if (tipoMensagem && listaMensagens) {
                    exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
                }
            },
            null,
            true
        );
        roteirizacao.modificada = true;
    },

    transferirRoteirizacaoComNovaRota : function() {
        var descricao = $('#lstRotaTranferencia', roteirizacao.workspace).val();
        descricao = $.trim(descricao);
        $.postJSON(
            contextPath + "/cadastro/roteirizacao/transferirRoteirizacaoComNovaRota",
            {
                'roteirizacaoId' :roteirizacao.buscaRoteirizacaoSelecionadas(),
                'roteiroId' : $('#idRoteiroSelecionado', roteirizacao.workspace).val(),
                'rotaNome' : descricao,
                'ordem' : $('#ordemRotaTranferencia', roteirizacao.workspace).val()


            },
            function(result) {
                $(".cotasRotaGrid", roteirizacao.workspace).flexReload();
                var tipoMensagem = result.tipoMensagem;
                var listaMensagens = result.listaMensagens;
                if (tipoMensagem && listaMensagens) {
                    exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
                }
            },
            null,
            true
        );
        roteirizacao.modificada = true;
    },

    excluirRoteirizacao :  function(){
        $.postJSON(
            contextPath + "/cadastro/roteirizacao/excluirRoteirizacao",
            {
                'roteirizacaoId' :roteirizacao.buscaRoteirizacaoSelecionadas()

            },
            function(result) {
                $(".cotasRotaGrid", roteirizacao.workspace).flexReload();
                var tipoMensagem = result.tipoMensagem;
                var listaMensagens = result.listaMensagens;
                if (tipoMensagem && listaMensagens) {
                    exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
                }
            },
            null,
            true
        );
        roteirizacao.modificada = true;
    },

    popupExcluirRoteirizacao : function() {

        //$( "#dialog:ui-dialog" ).dialog( "destroy" );

        $( "#dialog-excluir-cotas", roteirizacao.workspace ).dialog({
            resizable: false,
            height:'auto',
            width:400,
            modal: true,
            buttons: {
                "Confirmar": function() {
                    roteirizacao.excluirRoteirizacao();
                    $( this ).dialog( "close" );
                },
                "Cancelar": function() {
                    $( this ).dialog( "close" );
                }
            },
            form: $("#dialog-excluir-cotas", this.workspace).parents("form")
        });

    },

    buscaRoteirizacaoSelecionadas : function(){
        roteirizacaoSelecionadas = new Array();
        $("input[type=checkbox][name='roteirizacaoCheckbox']:checked", roteirizacao.workspace).each(function(){
            roteirizacaoSelecionadas.push(parseInt($(this).val()));
        });

        return roteirizacaoSelecionadas;
    },

    buscarPvsPorEndereco : function() {
        pesquisaPorCota = false;
        $('#cotaDisponivelPesquisa', roteirizacao.workspace).html('');
        var municipio =  $('#comboMunicipio', roteirizacao.workspace).val() ;

        if ( municipio != "" ){
            municipio = $('#comboMunicipio option:selected', roteirizacao.workspace).text();
        }




        $(".cotasDisponiveisGrid", roteirizacao.workspace).flexOptions({
            'url' : contextPath + '/cadastro/roteirizacao/buscarPvsPorEndereco',
            params : [{
                name : 'CEP',
                value : $('#cepPesquisa', roteirizacao.workspace).val()
            },
                {
                    name : 'uf',
                    value : $('#comboUf', roteirizacao.workspace).val()
                },
                {
                    name : 'municipio',
                    value :municipio
                },
                {
                    name : 'bairro',
                    value : $('#comboBairro', roteirizacao.workspace).val()
                },
                {
                    name : "rotaId",
                    value : $('#rotaSelecionada', roteirizacao.workspace).val()
                },

                {
                    name : "roteiroId",
                    value : $('#idRoteiroSelecionado', roteirizacao.workspace).val()
                }
            ],
            newp:1
        });

        $(".cotasDisponiveisGrid", roteirizacao.workspace).flexReload();
    },

    checarTodasCotasGrid : function() {

        if ( $("#selecionaTodos", roteirizacao.workspace).is(":checked") ) {
            $("input[type=checkbox][name='pdvCheckbox']", roteirizacao.workspace).each(function(){
                $(this).attr('checked', true);
            });
            var elem = $("#textoCheckAllCotas", roteirizacao.workspace);
            elem.innerHTML = "Desmarcar todos";
        } else {
            $("input[type=checkbox][name='pdvCheckbox']", roteirizacao.workspace).each(function(){
                $(this).attr('checked', false);
            });
            var elem = document.getElementById("textoCheckAllCotas", roteirizacao.workspace);
            elem.innerHTML = "Marcar todos";
        }
    },

    carregarComboRota : function (idComboRota, idRoteiro) {
        roteirizacao.resetComboRotaPesquisa(idComboRota);
        idComboRota = "#" + idComboRota;
        if (!idRoteiro) {

            idRoteiro = $('#roteiroPesquisa', roteirizacao.workspace).val();
        }

        $.postJSON(contextPath + '/cadastro/roteirizacao/carregarComboRota',
            {
                'roteiroId' : idRoteiro
            }
            ,
            function(result) {
                $.each(result, function(index, row){
                        $(idComboRota, roteirizacao.workspace).append('<option value="'+row.id+'">'+row.descricaoRota+'</option>');
                    }
                );

            },
            null,
            true
        );

    },

    resetComboRoteiroPesquisa : function(){
        $('#roteiroPesquisa > option', roteirizacao.workspace).remove();
        $('#roteiroPesquisa', roteirizacao.workspace).append('<option value="" >Selecione...</option>');
    },

    resetComboRotaPesquisa : function(selector){
        $(selector + '> option', roteirizacao.workspace).remove();
        $(selector, roteirizacao.workspace).append('<option value="" >Selecione...</option>');
    },

    pesquisaComRoteiroEspecial : function() {

        if ($("#tipoRoteiroPesquisa", roteirizacao.workspace).is(":checked") ) {
            roteirizacao.resetComboRoteiroPesquisa();
            roteirizacao.resetComboRotaPesquisa('#rotaPesquisa');
            $('#boxPesquisa', roteirizacao.workspace).attr("disabled", "disabled");
            $('#boxPesquisa', roteirizacao.workspace).val("");
            roteirizacao.carregarComboRoteiroEspecial();

        } else {
            roteirizacao.resetComboRoteiroPesquisa();
            roteirizacao.resetComboRotaPesquisa('#rotaPesquisa');
            $('#boxPesquisa', roteirizacao.workspace).val("");
            $('#roteiroPesquisa', roteirizacao.workspace).removeAttr("disabled");
        }
    },

    roteiroEspecial : function() {

        if ($("#tipoRoteiroTranferencia", roteirizacao.workspace).is(":checked") ) {
            $('#boxRoteiroTranferencia', roteirizacao.workspace).attr("disabled", "disabled");
            $('#boxRoteiroTranferencia', roteirizacao.workspace).val("");

        } else {
            $('#boxRoteiroTranferencia', roteirizacao.workspace).val("");
            $('#boxRoteiroTranferencia', roteirizacao.workspace).removeAttr("disabled");
        }
    },

    iniciarPesquisaRoteirizacaoGrid : function () {

        $(".gridWrapper", this.workspace).empty();

        $(".gridWrapper", this.workspace).append($("<table>").attr("class", "rotaRoteirosGrid"));

        var rotaPesquisa    = $('#rotaPesquisa', roteirizacao.workspace).val();

        var cotaPesquisa    = $('#cotaPesquisa', roteirizacao.workspace).val();

        var indGridPorRotaOuCota = (rotaPesquisa != "" || cotaPesquisa != "");

        if(indGridPorRotaOuCota) {

            $(".rotaRoteirosGrid", roteirizacao.workspace).flexigrid({
                preProcess: roteirizacao.callBackPesquisaRoteirizacaoGrid,
                dataType : 'json',
                colModel : [ {
                    display : 'Box',
                    name : 'nomeBox',
                    width : 100,
                    sortable : true,
                    align : 'left'
                }, {
                    display : 'Roteiro',
                    name : 'descricaoRoteiro',
                    width : 180,
                    sortable : true,
                    align : 'left'
                }, {
                    display : 'Rota',
                    name : 'descricaoRota',
                    width : 180,
                    sortable : true,
                    align : 'left'
                }, {
                    display : 'Cota',
                    name : 'numeroCota',
                    width : 78,
                    sortable : true,
                    align : 'left'
                }, {
                    display : 'Nome',
                    name : 'nome',
                    width : 180,
                    sortable : true,
                    align : 'left'
                }, {
                    display : 'Ação',
                    name : 'acao',
                    width : 78,
                    sortable : true,
                    align : 'center'
                }],
                sortname : "nomeBox",
                sortorder : "asc",
                usepager : true,
                useRp : true,
                rp : 15,
                showTableToggleBtn : true,
                width : 960,
                height : 'auto',
                singleSelect : true

            });

        } else {

            $(".rotaRoteirosGrid", roteirizacao.workspace).flexigrid({
                preProcess: roteirizacao.callBackPesquisaRoteirizacaoGridCotasSumarizadas,
                dataType : 'json',
                colModel : [ {
                    display : 'Box',
                    name : 'nomeBox',
                    width : 100,
                    sortable : true,
                    align : 'left'
                }, {
                    display : 'Roteiro',
                    name : 'descricaoRoteiro',
                    width : 180,
                    sortable : true,
                    align : 'left'
                }, {
                    display : 'Rota',
                    name : 'descricaoRota',
                    width : 180,
                    sortable : true,
                    align : 'left'
                }, {
                    display : 'Qtd. PDVs',
                    name : 'qntCotas',
                    width : 78,
                    sortable : true,
                    align : 'left'
                }],
                sortname : "nomeBox",
                sortorder : "asc",
                usepager : true,
                useRp : true,
                rp : 15,
                showTableToggleBtn : true,
                width : 960,
                height : 'auto',
                singleSelect : true

            });

        }

    },

    callBackPesquisaRoteirizacaoGrid: function (data) {

        if (data.mensagens) {

            exibirMensagem(
                data.mensagens.tipoMensagem,
                data.mensagens.listaMensagens
            );

            $(".grids", roteirizacao.workspace).hide();

            roteirizacao.esconderBotoesExportacao();

            return data;
        }

        var imgEdicao = '<img src="'+contextPath+'/images/ico_editar.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />';

        
        $.each(data.rows, function(index, value) {

            var idRoteirizacao = value.cell.idRoteirizacao;
            var idRoteiro   = value.cell.idRoteiro;
            var idRota      = value.cell.idRota;
        
            var parametros = idRoteirizacao + ',' + idRoteiro + ',' + idRota;
            
            value.cell.acao =  '<a href="javascript:;" onclick="roteirizacao.editarRoteirizacao(' + parametros + ');">' + imgEdicao + '</a>';


        });

        $(".grids", roteirizacao.workspace).show();

        roteirizacao.mostrarBotoesExportacao();

        return data;
    },

    callBackPesquisaRoteirizacaoGridCotasSumarizadas: function (data) {

        if (data.mensagens) {

            exibirMensagem(
                data.mensagens.tipoMensagem,
                data.mensagens.listaMensagens
            );

            $(".grids", roteirizacao.workspace).hide();

            roteirizacao.esconderBotoesExportacao();

            return data;
        }


        $.each(data.rows, function(index, value) {

            var qntCotas = value.cell.qntCotas;

            var idBox       = "-1";
            if (value.cell.idBox) {
               idBox = value.cell.idBox;
            }
            var idRota      = value.cell.idRota;
            var idRoteiro   = value.cell.idRoteiro;

            var title = value.cell.descricaoRota + ' - ' + value.cell.descricaoRoteiro;

            value.cell.qntCotas =  '<a href="javascript:;" ' +
                'onclick="roteirizacao.detalharRotaRoteiroCotasSumarizadas(\''+title+'\','+idBox+','+idRota+','+idRoteiro+');">' + qntCotas + '</a>';
        });

        $(".grids", roteirizacao.workspace).show();

        roteirizacao.mostrarBotoesExportacao();

        return data;
    },

    editarRoteirizacao : function(idRoteirizacao, idRoteiro, idRota) {
        roteirizacao.definirTipoEdicao(TipoEdicao.ALTERACAO);
        roteirizacao.idRoteirizacao = idRoteirizacao;
        roteirizacao.idRoteiro = idRoteiro;
        roteirizacao.idRota = idRota;
        roteirizacao.boxReadonly = true;
        roteirizacao.limparCamposPesquisaGrids();
        roteirizacao.prepararPopupRoteirizacao();
        roteirizacao.modificada = false;

    },

    detalharRotaRoteiroCotasSumarizadas : function(title, idBox, idRota, idRoteiro) {

        var data = [];
        if (idBox != -1) {
            data.push({name:'idBox',        value: idBox });
        }
        data.push({name:'idRota',       value: idRota });
        data.push({name:'idRoteiro',    value: idRoteiro });

        $("#cotasGrid", this.workspace).flexOptions({ params:data });
        $("#cotasGrid", this.workspace).flexReload();

        roteirizacao.popupDetalhesCota(title, idBox, idRoteiro, idRota);
    },

    pesquisarRoteirizacao: function () {

        roteirizacao.iniciarPesquisaRoteirizacaoGrid();

        $(".rotaRoteirosGrid", roteirizacao.workspace).flexOptions({
            url : contextPath + '/cadastro/roteirizacao/pesquisarRoteirizacao',
            params : [{
                name : "boxId",
                value : $('#boxPesquisa', roteirizacao.workspace).val()
            }, {
                name : "roteiroId",
                value : $('#roteiroPesquisa', roteirizacao.workspace).val()
            }, {
                name : "rotaId",
                value : $('#rotaPesquisa', roteirizacao.workspace).val()
            },
                {
                    name : "numeroCota",
                    value : $('#cotaPesquisa', roteirizacao.workspace).val()
                }],

            newp:1
        });

        $(".rotaRoteirosGrid", roteirizacao.workspace).flexReload();
    },

    carregarNomeCotas : function (campoExibicao, numeroCota, callBack) {
        $('#'+campoExibicao).html('');
        var result = false;
        $.postJSON(contextPath + '/cadastro/roteirizacao/buscaCotaPorNumero',
            {
                'numeroCota': numeroCota
            } ,
            function(result) {
                var tipoMensagem = result.tipoMensagem;
                var listaMensagens = result.listaMensagens;
                if (tipoMensagem && listaMensagens) {
                    exibirMensagem(tipoMensagem, listaMensagens);
                } else {
                    $('#'+campoExibicao).html(result.pessoa.nome);
                    if (callBack){
                        callBack();
                    }

                }


                result = true;
            },
            null,
            true
        );
        return result;

    },

    novaRoteirizacao : function() {
        roteirizacao.idBox = "";
        roteirizacao.nomeBox = "";
        roteirizacao.idRoteiro = "";
        roteirizacao.nomeRoteiro = "";
        roteirizacao.ordemRoteiroSelecionado = "";
        roteirizacao.idRota = "";
        roteirizacao.nomeRota = "";
        roteirizacao.ordemRotaSelecionada = "";
        roteirizacao.boxReadonly = false;
        roteirizacao.limparCamposPesquisaGrids();
        roteirizacao.definirTipoEdicao(TipoEdicao.NOVO);
        roteirizacao.prepararPopupRoteirizacao();
        roteirizacao.modificada = false;
        $('#selecionarTodosPdv').uncheck();
        
    },

    limparCamposPesquisaGrids : function() {
        $('#nomeBox', roteirizacao.workspace).val('');
        $('#descricaoRoteiro', roteirizacao.workspace).val('');
        $('#descricaoRota', roteirizacao.workspace).val('');
    },

    habilitaBotao: function(idBotao, funcao){

        $('#'+idBotao, roteirizacao.workspace).css("cursor","");
        $('#'+idBotao, roteirizacao.workspace).css("opacity","1");
        $('#'+idBotao, roteirizacao.workspace).css("filter","alpha(opaity=100)");
        $('#'+idBotao, roteirizacao.workspace).click(funcao);

    },

    desabilitaBotao: function(idBotao){

        $('#'+idBotao, roteirizacao.workspace).css("cursor","default");
        $('#'+idBotao, roteirizacao.workspace).css("opacity","0.4");
        $('#'+idBotao, roteirizacao.workspace).css("filter","alpha(opaity=40)");
        $('#'+idBotao, roteirizacao.workspace).unbind('click');
    },

    habilitaBotoesRota : function() {

        listaRotas = roteirizacao.buscaRotasSelecionadas();

        if (listaRotas.length == 0 ) {
            roteirizacao.desabilitaBotao('botaoTransfereciaRota');
            roteirizacao.desabilitaBotao('botaoExcluirRota');

        } else {
            roteirizacao.habilitaBotao('botaoTransfereciaRota', function(){roteirizacao.popupTransferirRota();});
            roteirizacao.habilitaBotao('botaoExcluirRota',function(){roteirizacao.popupExcluirRotaRoteiro();});
        }
    },

    habilitaBotoesRoteirizacao : function() {

        listaRoteirizacao = roteirizacao.buscaRoteirizacaoSelecionadas();

        if (listaRoteirizacao.length == 0 ) {
            roteirizacao.desabilitaBotao('botaoTransferenciaRoteiro', roteirizacao.workspace);
            roteirizacao.desabilitaBotao('botaoExcluirRoteirizacao', roteirizacao.workspace);

        } else {
            roteirizacao.habilitaBotao('botaoTransferenciaRoteiro', function(){roteirizacao.popupTransferirCota();});
            roteirizacao.habilitaBotao('botaoExcluirRoteirizacao',function(){roteirizacao.popupExcluirRoteirizacao();});
        }
    },

    prepararPopupRoteirizacao : function(){
       
    	roteirizacao.iniciarGridBox();
        
    	 var method = '';
         
         var param = [];
         
         var idRoteiro = roteirizacao.idRoteiro;
         var idRota    = roteirizacao.idRota;
         
    	if (roteirizacao.isNovo()) {
            $('#nomeBox', roteirizacao.workspace).prop('disabled', false);
            $('#lnkPesquisarBox', roteirizacao.workspace).click(function() {roteirizacao.pesquisarBox();});
            method = 'novaRoteirizacao';
        } else {
            $('#nomeBox', roteirizacao.workspace).prop('disabled', true);
            $('#lnkPesquisarBox', roteirizacao.workspace).unbind('click');
            method = 'editarRoteirizacao';
            param.push({name: 'idRoteirizacao', value: roteirizacao.idRoteirizacao});
        }

        roteirizacao.iniciarGridRoteiros();
        roteirizacao.iniciarGridRotas();
        roteirizacao.iniciarGridCotasRota();
        
        $.postJSON(contextPath + '/cadastro/roteirizacao/' + method,
            param,
            function(result) {
                var tipoMensagem = result.tipoMensagem;
                var listaMensagens = result.listaMensagens;
                if (tipoMensagem && listaMensagens) {
                    exibirMensagem(tipoMensagem, listaMensagens);
                } else {
                    if (roteirizacao.isNovo()) {
                        roteirizacao.popularGridBox(result.boxDisponiveis);
                     
                    } else {
                    	
                        roteirizacao.popularGrids(result);
                        $("#roteiro"+idRoteiro).click();
                        roteirizacao.idRota = idRota;
                    }
                }
                
                $( "#dialog-roteirizacao", roteirizacao.workspace ).dialog({
                    resizable: false,
                    height:630,
                    width:955,
                    modal: true,
                    title : roteirizacao.isNovo() ? 'Nova Roteirização' : 'Editar Roteirização',
                    buttons: {
                        "Confirmar": function() {
                            roteirizacao.confirmarRoteirizacao();
                            

                        },
                        "Cancelar": function() {
                            $.postJSON(contextPath + '/cadastro/roteirizacao/cancelarRoteirizacao',
                                null,
                                null,
                                null,
                                true
                            );
                            $( this ).dialog( "close" );
                        }
                    },
                    form: $("#dialog-roteirizacao", this.workspace).parents("form")
                });
            },
            null,
            true
        );

        $(".cotasDisponiveisGrid", roteirizacao.workspace).clear();
        $('#spanDadosRoteiro', roteirizacao.workspace).html('<strong>Roteiro Selecionado:</strong>&nbsp;&nbsp; <strong>Box: </strong>&nbsp;&nbsp; <strong>Ordem: </strong>&nbsp;');
    },

    confirmarRoteirizacao : function() {
        
     	if(!verificarPermissaoAcesso(roteirizacao.workspace)){
    		return; 
    	}
    
    	$.postJSON(contextPath + '/cadastro/roteirizacao/confirmarRoteirizacao',
            null,
            function(result) {
        		
        		roteirizacao.novaRoteirizacao();
                var tipoMensagem = result.tipoMensagem;
                var listaMensagens = result.listaMensagens;
                if (tipoMensagem && listaMensagens) {
                 
                    exibirMensagem(tipoMensagem, listaMensagens);
                }
            },
            null,
            true
        );
    },

    abrirTelaRotaComNome :function(){

        $('#nomeRotaInclusao', roteirizacao.workspace).val($('botaoNovaRotaNome', roteirizacao.workspace).val());
        roteirizacao.abrirTelaRota();
    },

    exibiRotaNovaTranferencia : function (){
        $('.rotaNovaTransferencia', roteirizacao.workspace).show();
        transferirRoteirizacaoComNovaRota = true;
    },

    escondeRotaNovaTranferencia : function (){
        $('.rotaNovaTransferencia', roteirizacao.workspace).hide();
        transferirRoteirizacaoComNovaRota = false;
    },

    atualizaOrdenacaoAsc : function(roteirizacaoId, ordem, idPontoVenda){
        $.postJSON(contextPath + '/cadastro/roteirizacao/atualizaOrdenacaoAsc',
            {
                'roteirizacaoId' :roteirizacaoId,
                'rotaId':$('#rotaSelecionada', roteirizacao.workspace).val() ,
                'roteiroId' :$('#idRoteiroSelecionado', roteirizacao.workspace).val(),
                'ordem':ordem,
                'pontoVendaId':idPontoVenda,
                'ordenacao' : 'ASC'
            } ,
            function(result) {
                $(".cotasRotaGrid", roteirizacao.workspace).flexReload();
            },
            null,
            true
        );
    },

    atualizaOrdenacaoDesc : function(roteirizacaoId, ordem, idPontoVenda){
        $.postJSON(contextPath + '/cadastro/roteirizacao/atualizaOrdenacaoAsc',
            {
                'roteirizacaoId' :roteirizacaoId,
                'rotaId':$('#rotaSelecionada', roteirizacao.workspace).val() ,
                'roteiroId' :$('#idRoteiroSelecionado', roteirizacao.workspace).val(),
                'ordem':ordem,
                'pontoVendaId':idPontoVenda,
                'ordenacao' : 'DESC'
            } ,
            function(result) {
                $(".cotasRotaGrid", roteirizacao.workspace).flexReload();
            },
            null,
            true
        );
    },

    carregarNomeCotasPesquisa : function (campoExibicao, numeroCota, callBack) {
        $('#'+campoExibicao).html('');
        var result = false;
        $.postJSON(contextPath + '/cadastro/roteirizacao/buscaCotaPorNumero',
            {
                'numeroCota': numeroCota
            } ,
            function(result) {
                var tipoMensagem = result.tipoMensagem;
                var listaMensagens = result.listaMensagens;
                if (tipoMensagem && listaMensagens) {
                    exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacaoCotaDisponivel');
                } else {
                    $('#'+campoExibicao, roteirizacao.workspace).html(result.pessoa.nome);
                    if (callBack){
                        callBack();
                    }
                }


                result = true;
            },
            null,
            true
        );

        return  result;

    },

    exportar : function(fileType) {

        //TODO:
        var tipoRoteiro = "NORMAL";

        if ($("#tipoRoteiroTranferencia", roteirizacao.workspace).is(":checked") ) {
            tipoRoteiro = "ESPECIAL";
        }

        window.location =
            contextPath +
                "/cadastro/roteirizacao/exportar?fileType=" + fileType;

        return false;
    },

    mostrarBotoesExportacao : function() {

        $("#botoesExportacao").show();
    },

    esconderBotoesExportacao : function() {

        $("#botoesExportacao", roteirizacao.workspace).hide();
    },

    init : function() {
    	
    	$("#cotasGrid",roteirizacao.workspace).flexigrid({
            autoload : false,
            url : contextPath + '/cadastro/roteirizacao/obterCotasSumarizadas',
            dataType : 'json',
            colModel : [ {
                display : 'Cota',
                name : 'numeroCota',
                width : 120,
                sortable : true,
                align : 'left'
            }, {
                display : 'Nome',
                name : 'nome',
                width : 250,
                sortable : true,
                align : 'left'
            }],
            sortname : "",
            sortorder : "asc",
            width : 'auto',
            height : 200
        });

		$(document).ready(function(){
			
			focusSelectRefField($("#boxPesquisa"));
			
			$(document.body).keydown(function(e) {
				
				if(keyEventEnterAux(e)){
					roteirizacao.pesquisarRoteirizacao();
				}
				
				return true;
			});
		});
    },
    
    limparCamposNovaInclusao : function(){
    	this.switchNovoRoteiroRota("roteiro");
        $("#selectTipoNovoDado", roteirizacao.workspace).val("ROTEIRO");
        $("#inputOrdem", roteirizacao.workspace).val("");
        $("#inputNome", roteirizacao.workspace).val("");
    },
    
    validarPDVsSelecionados : function() {
    	roteirizacao.obterDadoPDVsSelecionados();
        
    	if (roteirizacao.pdvsSelecionados.length == 0 ) {
            exibirMensagemDialog("WARNING", ["Selecione pelo menos um PDV para transferência!"]);
            return false;
    	}
    	
    	return true;
    },
    
    popup_tranferir : function(){
        if (roteirizacao.isTransferenciaCota()){
            
        	if (!roteirizacao.validarPDVsSelecionados) return;

        	roteirizacao.carregarComboTodasRotas("#selectNovaRota");
        	  
        	roteirizacao.dialogTransferenciaCota();
        	
        } else if (roteirizacao.isTransferenciaRota()){

            $("#nomeRoteiroAtual").val(roteirizacao.nomeRoteiro);

            $.postJSON(
                contextPath + '/cadastro/roteirizacao/carregarRoteirosTransferenciaRota',
                [
                    {name: 'idBox', value: roteirizacao.idBox}
                ],
                function(result) {

                    var opts = '';
                    $("#selectNovoRoteiro", roteirizacao.workspace).html(opts);

                    $.each(result, function(index, value){
                        opts += "<option value='"+ value.id +"'>" + value.nome + "</option>";
                    });

                    $("#selectNovoRoteiro", roteirizacao.workspace).append(opts);

                    $("#dialog-transfere-rota", roteirizacao.workspace).dialog({
                        resizable: false,
                        height:'auto',
                        width:420,
                        modal: true,
                        buttons: {
                            "Confirmar": function() {

                                $.postJSON(
                                    contextPath + '/cadastro/roteirizacao/transferirRota',
                                    [
                                        {name: 'idRoteiroAnterior', value: roteirizacao.idRoteiro},
                                        {name: 'idRota', value: roteirizacao.idRota},
                                        {name: 'idRoteiroNovo', value: $("#selectNovoRoteiro", roteirizacao.workspace).val()}
                                    ],
                                    function(result) {

                                        roteirizacao.popularGridRotas();
                                        $("#dialog-transfere-rota", roteirizacao.workspace).dialog("close");
                                    },
                                    null,
                                    true
                                );
                                
                                roteirizacao.modificada = true;
                            },
                            "Cancelar": function() {
                                $("#dialog-transfere-rota", roteirizacao.workspace).dialog("close");
                            }
                        },
                        form: $("#dialog-transfere-rota", roteirizacao.workspace).parents("form")
                    });
                },
                null,
                true
            );

        } else {

            $("#nomeBoxAtual", roteirizacao.workspace).val(roteirizacao.nomeBox);

            $.postJSON(
                contextPath + '/cadastro/roteirizacao/carregarBoxTransferenciaRoteiro',
                [
                    {name: 'idBox', value: roteirizacao.idBox}
                ],
                function(result) {

                    var opts = '';
                    $("#selectNovoBox", roteirizacao.workspace).html(opts);

                    $.each(result, function(index, value){
                        opts += "<option value='"+ value.key.$ +"'>" + value.value.$ + "</option>";
                    });

                    $("#selectNovoBox", roteirizacao.workspace).append(opts);

                    $("#dialog-transfere-roteiro", roteirizacao.workspace).dialog({
                        resizable: false,
                        height:'auto',
                        width:420,
                        modal: true,
                        buttons: {
                            "Confirmar": function() {

                                $.postJSON(
                                    contextPath + '/cadastro/roteirizacao/transferirRoteiro',
                                    [
                                        {name: 'idBoxAnterior', value: roteirizacao.idBox},
                                        {name: 'idRoteiro', value: roteirizacao.idRoteiro},
                                        {name: 'idBoxNovo', value: $("#selectNovoBox", roteirizacao.workspace).val()}
                                    ],
                                    function(result) {

                                        roteirizacao.popularGridRoteiros();
                                        roteirizacao.tipoInclusao = TipoInclusao.ROTEIRO;
                                        $("#dialog-transfere-roteiro", roteirizacao.workspace).dialog("close");
                                    },
                                    null,
                                    true
                                );
                                
                                roteirizacao.modificada = true;
                            },
                            "Cancelar": function() {
                                $("#dialog-transfere-roteiro", roteirizacao.workspace).dialog("close");
                            }
                        },
                        form: $("#dialog-transfere-roteiro", roteirizacao.workspace).parents("form")
                    });
                },
                null,
                true
            );
        }
    },
    
    carregarComboTodasRotas : function(selector) {
    	
    	roteirizacao.resetComboRotaPesquisa(selector);
    	
    	var params = [{name: 'idRoteiro', value: roteirizacao.idRoteiro}];
    	
    	$.postJSON(
                contextPath + '/cadastro/roteirizacao/carregarTodasRotas', params,
                function(result) {
                	
                    $("#nomeRotaAtual").val(roteirizacao.nomeRota);
                    
                    var opts = '';
                    $(selector, roteirizacao.workspace).html(opts);
                    
                    opts += "<option value='' selected='selected'>Selecione... </option>";
                    
                    $.each(result, function(index, value){
                        opts += "<option value='"+ (value.id ? value.id : "_" + value.ordem) +"'>" + value.nome + "</option>";
                    });

                    $(selector, roteirizacao.workspace).append(opts);
              },
              null,
              true);
    },
    
    transferirPDVS : function() {
        
    	var params = [{name: 'idRotaAnterior', value: roteirizacao.idRota},
                      {name: 'idRoteiro', 	   value: roteirizacao.idRoteiro},
                      {name: '_idRotaNova',    value: $("#selectNovaRota", roteirizacao.workspace).val()},
                      {name: 'ordemRota',      value: roteirizacao.ordemRotaSelecionada}
                     ];
        
        $('input:checkbox[name=checkboxCotasRota]:checked', roteirizacao.workspace).each(
        		function() {
                      params.push({name: 'pdvs', value: $(this).val()
                });
        });

        $.postJSON(contextPath + '/cadastro/roteirizacao/transferirPDVs', params,
        		function(result) {
                	
        			roteirizacao.popularGridCotasRota();
                    	
                	$("#dialog-transfere-cotas", roteirizacao.workspace).dialog("close");
                    
                	if (result.tipoMensagem) {
                		exibirMensagemDialog(result.tipoMensagem, result.listaMensagens,'dialog-transfere-cotas');
                	}
                        
                },
       null, true);
                  
       roteirizacao.modificada = true;
    },
    
    dialogTransferenciaCota : function() {
    	 $("#dialog-transfere-cotas", roteirizacao.workspace).dialog({
             resizable: false,
             height:'auto',
             width:420,
             modal: true,
             buttons: {
                 "Confirmar": function() {
                	 roteirizacao.transferirPDVS();
                 },
                 "Cancelar": function() {
                     $("#dialog-transfere-cotas", roteirizacao.workspace).dialog("close");
                 }
             },
             form: $("#dialog-transfere-cotas", roteirizacao.workspace).parents("form")
    	 });
    },
    
    abrirTelaRotaComNome :function(){
        $('#nomeRotaInclusao', roteirizacao.workspace).val($('botaoNovaRotaNome', roteirizacao.workspace).val());
        roteirizacao.abrirTelaRota();


    },
    exibiRotaNovaTranferencia : function (){
        $('.rotaNovaTransferencia', roteirizacao.workspace).show();
        transferirRoteirizacaoComNovaRota = true;
    },
    escondeRotaNovaTranferencia : function (){
        $('.rotaNovaTransferencia', roteirizacao.workspace).hide();
        transferirRoteirizacaoComNovaRota = false;
    },

    atualizaOrdenacaoAsc : function(roteirizacaoId, ordem, idPontoVenda){
        $.postJSON(contextPath + '/cadastro/roteirizacao/atualizaOrdenacaoAsc',
            {
                'roteirizacaoId' :roteirizacaoId,
                'rotaId':$('#rotaSelecionada', roteirizacao.workspace).val() ,
                'roteiroId' :$('#idRoteiroSelecionado', roteirizacao.workspace).val(),
                'ordem':ordem,
                'pontoVendaId':idPontoVenda,
                'ordenacao' : 'ASC'
            } ,
            function(result) {
                $(".cotasRotaGrid", roteirizacao.workspace).flexReload();
            },
            null,
            true
        );
    },

    atualizaOrdenacaoDesc : function(roteirizacaoId, ordem, idPontoVenda){
        $.postJSON(contextPath + '/cadastro/roteirizacao/atualizaOrdenacaoAsc',
            {
                'roteirizacaoId' :roteirizacaoId,
                'rotaId':$('#rotaSelecionada', roteirizacao.workspace).val() ,
                'roteiroId' :$('#idRoteiroSelecionado', roteirizacao.workspace).val(),
                'ordem':ordem,
                'pontoVendaId':idPontoVenda,
                'ordenacao' : 'DESC'
            } ,
            function(result) {
                $(".cotasRotaGrid", roteirizacao.workspace).flexReload();
            },
            null,
            true
        );
    },

    carregarNomeCotasPesquisa : function (campoExibicao, numeroCota, callBack) {
        $('#'+campoExibicao).html('');
        var result = false;
        $.postJSON(contextPath + '/cadastro/roteirizacao/buscaCotaPorNumero',
            {
                'numeroCota': numeroCota
            } ,
            function(result) {
                var tipoMensagem = result.tipoMensagem;
                var listaMensagens = result.listaMensagens;
                if (tipoMensagem && listaMensagens) {
                    exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacaoCotaDisponivel');
                } else {
                    $('#'+campoExibicao, roteirizacao.workspace).html(result.pessoa.nome);
                    if (callBack){
                        callBack();
                    }
                }


                result = true;
            },
            null,
            true
        );

        return  result;

    },

    exportar : function(fileType) {

        //TODO:
        var tipoRoteiro = "NORMAL";

        if ($("#tipoRoteiroTranferencia", roteirizacao.workspace).is(":checked") ) {
            tipoRoteiro = "ESPECIAL";
        }

        window.location =
            contextPath +
                "/cadastro/roteirizacao/exportar?fileType=" + fileType;

        return false;
    },

    mostrarBotoesExportacao : function() {

        $("#botoesExportacao").show();
    },

    esconderBotoesExportacao : function() {

        $("#botoesExportacao", roteirizacao.workspace).hide();
    },

    obterDadoPDVsSelecionados: function () {

        roteirizacao.pdvsSelecionados = new Array();

        $("#cotasRotaGrid tbody", roteirizacao.workspace).children().map(function() {

            var child = $(this);

            if (child.is("tr")) {

                var checked = $(child).find(":checkbox").attr("checked") ? true : false;

                if (checked) {

                    var id =       $(child).find(":checkbox").val();
                    var pdv =      $(child).find("td[abbr='pdv'] div", this).html();
                    var cota =     $(child).find("td[abbr='cota'] div", this).html();
                    var nome =     $(child).find("td[abbr='nome'] div", this).html();
                    var ordem =    $(child).find("td[abbr='ordem'] div > input", this).val();
                    var origem =   $(child).find("td[abbr='origem'] div", this).html();
                    var endereco = $(child).find("td[abbr='endereco'] div", this).html();

                    roteirizacao.pdvsSelecionados.push({
                        id: id,
                        pdv: pdv,
                        cota: cota,
                        nome: nome,
                        ordem: ordem,
                        origem: origem,
                        endereco: endereco
                    });
                }
            }
        });
    },

    prepararPopupCopiarCotas: function () {

        $("#cotasParaCopiaGrid", roteirizacao.workspace).html(
            '<tr class="header_table"><td width="85">Cota</td><td width="255">Nome</td></tr>'
        );

        $.each(roteirizacao.pdvsSelecionados, function(index, value) {

            var tdNomeCota = "<td> " + value.nome + " </td>";
            var tdNumeroCota = "<td> " + value.cota + "</td>";

            var trClass = index % 2 == 0 ? "<tr class='class_linha_1'>" : "<tr class='class_linha_2'>";
            var linha = trClass + tdNumeroCota + tdNomeCota + "</tr>";

            $("#cotasParaCopiaGrid", roteirizacao.workspace).append(linha);
        });

        $("#rotaAtual").val(roteirizacao.idRota + " - " + roteirizacao.nomeRota);

        roteirizacao.carregarComboRotaCopiaPDV(roteirizacao.idRoteiro);
    },
    
    
    carregarComboRotaCopiaPDV : function (idRoteiro) {
      
        if (!idRoteiro) {
            idRoteiro = $('#roteiroPesquisa', roteirizacao.workspace).val();
        }

        roteirizacao.carregarComboRotasEspeciais();

    },

    abrirPopupCopiarCotas: function() {
        roteirizacao.obterDadoPDVsSelecionados();
        if (roteirizacao.pdvsSelecionados.length == 0 ) {
            exibirMensagemDialog("WARNING", ["Selecione pelo menos um PDV para copiar!"]);
            return;
        }

        this.prepararPopupCopiarCotas();

        $( "#dialogCopiarCota", roteirizacao.workspace ).dialog({
            resizable: false,
            height:'auto',
            width:420,
            modal: true,
            buttons: {
                "Confirmar": function() {
                    roteirizacao.copiarCotas();
                    $( this ).dialog( "close" );
                },
                "Cancelar": function() {
                    $( this ).dialog( "close" );
                }
            },
            form: $("#dialogCopiarCota", roteirizacao.workspace).parents("form")
        });
    },

    copiarCotas: function() {

        $.postJSON(contextPath + '/cadastro/roteirizacao/copiarCotasRota',
            roteirizacao.obterParametrosCopia(),
            function(result) {
                if (result.tipoMensagem && result.listaMensagens) {
                    exibirMensagemDialog(result.tipoMensagem, result.listaMensagens,'dialog-roteirizacao');
                }
            },
            function(result) {

                if (result.tipoMensagem && result.listaMensagens) {
                    exibirMensagemDialog(result.tipoMensagem, result.listaMensagens,'dialog-roteirizacao');
                }
            },
            true
        );
        roteirizacao.modificada = true;
    },

    obterParametrosCopia: function() {

        var rotaCopia = new Array();

        $.each(roteirizacao.pdvsSelecionados, function(index, value) {

            rotaCopia.push({name: "rotaCopia.pdvs["+index+"].id", value:value.id});
            rotaCopia.push({name: "rotaCopia.pdvs["+index+"].pdv", value: value.pdv});
            rotaCopia.push({name: "rotaCopia.pdvs["+index+"].origem", value: value.origem});
            rotaCopia.push({name: "rotaCopia.pdvs["+index+"].endereco", value: value.endereco});
            rotaCopia.push({name: "rotaCopia.pdvs["+index+"].cota", value: value.cota});
            rotaCopia.push({name: "rotaCopia.pdvs["+index+"].nome", value: value.nome});
            rotaCopia.push({name: "rotaCopia.pdvs["+index+"].ordem", value: value.ordem});
        });

        rotaCopia.push({name:"rotaCopia.id" , value: $("#selectNovasRotas").val()});
        rotaCopia.push({name:"rotaCopia.nome" , value: $("#selectNovasRotas > option:selected").html()});
        rotaCopia.push({name:"idRoteiro" , value: roteirizacao.idRoteiro});

        return rotaCopia;

    },

    selecaoCota : function () {

        roteirizacao.tipoExclusao = TipoExclusao.COTA;
        roteirizacao.tipoTransferencia = TipoTransferencia.COTA;
    }

}, BaseController);

$(function() {
    roteirizacao.init();
  
});

//@ sourceURL=roteirizacao.js
