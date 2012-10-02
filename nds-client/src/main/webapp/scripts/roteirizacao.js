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
	ROTA : {value: 'ROTA'}
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
        idRoteiro: "",
        idRota: "",
        nomeBox: "",
        nomeRoteiro : "",
        nomeRota : "",
        tipoInclusao: TipoInclusao.ROTEIRO,
        tipoExclusao: null,
        idsCotas: [],
        
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
            
        	roteirizacao.limparCamposNovaInclusao();
            
        	$("#trNomeRoteiro").hide();
        	
            var metodoObterOrdem = '';
            
            if (roteirizacao.tipoInclusao == TipoInclusao.ROTEIRO){
                
                metodoObterOrdem = 'iniciaTelaRoteiro';
                $("#checkRoteiroEspecial", roteirizacao.workspace).show();
            } else {
                
                metodoObterOrdem = 'iniciaTelaRota';
                $("#trNomeRoteiro").show();
                $("#nomeRoteiro").text(roteirizacao.nomeRoteiro);
            }
            
            $.postJSON(contextPath + '/cadastro/roteirizacao/' + metodoObterOrdem, 
            	[{name: 'idRoteiro', value: roteirizacao.idRoteiro}],
                function(result) {
                    
                    roteirizacao.limparTelaRoteiro();
                    $('#inputOrdem', roteirizacao.workspace).numeric();
                    $("#inputOrdem", roteirizacao.workspace).val(result.int);
                    
                    $("#dialog-novo-dado", roteirizacao.workspace ).dialog({
                        resizable: false,
                        height:240,
                        width:420,
                        modal: true,
                        title: (roteirizacao.tipoInclusao == TipoInclusao.ROTEIRO ? "Novo Roteiro" : "Nova Rota"),
                        buttons: {
                            "Confirmar": function() {
                                
                                if (roteirizacao.tipoInclusao == TipoInclusao.ROTEIRO){
                                    
                                    roteirizacao.confirmarInclusaoRoteiro();
                                } else {
                                    
                                    roteirizacao.confirmarInclusaoRota();
                                }
                            },
                            "Cancelar": function() {
                                $( this ).dialog( "close" );
                            }
                        },
                        form: $("#dialog-novo-dado", this.workspace).parents("form")
                    });
                },
                null,
                true
            );
            
            this.init();
        },
        
        
        confirmarInclusaoRoteiro : function() {
           var tipoRoteiro = 'NORMAL';
           if ( $('input[name=tipoRoteiro]').is(':checked') ){
               tipoRoteiro = 'ESPECIAL';
           } 
           
            $.postJSON(contextPath + '/cadastro/roteirizacao/incluirRoteiro',
                {
                    'idBox' :  roteirizacao.idBox,
                    'ordem' :  $("#inputOrdem", roteirizacao.workspace).val(),
                    'nome' :  $("#inputNome", roteirizacao.workspace).val(),
                    'tipoRoteiro' : tipoRoteiro
                 },
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
                                $('#dialog-rota', roteirizacao.workspace).dialog( "close" );
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
                        var id = value.cell.id;
                        var selecione = '<input type="radio" value="' + id +'" name="rotaRadio" ';
                        selecione += 'onclick="roteirizacao.rotaSelecionadaListener(\'' +  id  + '\', \''+ value.cell.nome +'\');"';
                        if (id == roteirizacao.idRota) {
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

        rotaSelecionadaListener : function(idRota, nomeRota) {
            roteirizacao.idRota = idRota;
            roteirizacao.nomeRota = nomeRota;
            roteirizacao.popularGridCotasRota();
            roteirizacao.definirTransferenciaRota();
            roteirizacao.tipoExclusao = TipoExclusao.ROTA;
            roteirizacao.popularInfoCotasRota();
            
        },
        
        popularInfoCotasRota : function() {
        	var info = '<strong>Box: </strong>' + roteirizacao.idBox;
        	info += ' <strong>- Roteiro Selecionado: </strong>' + roteirizacao.nomeRoteiro;
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
            $(".boxGrid", roteirizacao.workspace).flexigrid({
                preProcess: function(data) {
                    $.each(data.rows, function(index, value) {
                        
                        var id = value.cell.id;
                        var selecione = '<input type="radio" value="' + id +'" name="boxRadio" ';
                        selecione += 'onclick="roteirizacao.boxSelecionadoListener(\'' +  id  + '\');"';
                        if (id == roteirizacao.idBox) {
                            selecione += 'checked';
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
                    $('.boxGrid').flexOptions({params: [{name: 'nomeBox', value: $('#nomeBox').val()}]});
                    return true;
                }
            });
            roteirizacao.limparGridBox();
        },

        popularGridBox : function(data) {
            if (data) {
                $(".boxGrid", roteirizacao.workspace).flexAddData({rows: toFlexiGridObject(data), page : 1, total : data.length});
            } else {
                roteirizacao.limparGridBox();
            }
        },
        
        limparGridBox : function() {
           $(".boxGrid", roteirizacao.workspace).flexAddData({rows: [], page : 0, total : 0});
        },

        pesquisarBox : function() {
            roteirizacao.limparInfoCotasRota();
            $(".boxGrid", roteirizacao.workspace).flexReload();
            roteirizacao.idBox = "";
            roteirizacao.limparGridRoteiros();
            roteirizacao.limparGridRotas();
            roteirizacao.limparGridCotasRota();
        },

        boxSelecionadoListener : function(idBox) {
            var isBoxSelecionado = roteirizacao.idBox != "";
            if (isBoxSelecionado) {
              var dialog = new ConfirmDialog("Ao alterar o Box selecionado as informações não confirmadas serão perdidas.<br/>Confirma?", function() {
                 roteirizacao.processarAlteracaoBox(idBox);
                 return true;
              }, function() {
              });
              dialog.open();
            } else {
                roteirizacao.processarAlteracaoBox(idBox);
            }
        },

        processarAlteracaoBox : function(idBox) {
            roteirizacao.idBox = idBox;
            roteirizacao.idRoteiro = "";
            roteirizacao.idRota = "";
            roteirizacao.limparInfoCotasRota();

            roteirizacao.tipoInclusao = TipoInclusao.ROTEIRO;
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
        },

        popularGrids : function(data) {
            if (data.box) {
                roteirizacao.idBox = data.box.id;
            }
            roteirizacao.popularGridBox(data.boxDisponiveis);
            if (data.roteiros && data.roteiros.length > 0) {
                    if (roteirizacao.idRoteiro == "") {
                        roteirizacao.idRoteiro = data.roteiros[0].id;
                        roteirizacao.nomeRoteiro = data.roteiros[0].nome;
                    }
                    roteirizacao.popularGridRoteiros(data.roteiros);
                    if (data.roteiros[0].rotas) {
                        if (roteirizacao.idRota == "") {
                            roteirizacao.idRota = data.roteiros[0].rotas[0].id;
                            roteirizacao.nomeRota = data.roteiros[0].rotas[0].nome;
                        }
                        roteirizacao.popularGridRotas(data.roteiros[0].rotas);
                        if (data.roteiros[0].rotas[0].pdvs) {
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
                        var selecione = '<input type="radio" value="' + id +'" name="roteirosRadio" ';
                        selecione += 'onclick="roteirizacao.roteiroSelecionadoListener(\'' +  id  + '\', \'' + value.cell.nome + '\');"';
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
                $.postJSON(contextPath + '/cadastro/roteirizacao/buscaRoteiros',
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

        roteiroSelecionadoListener : function(idRoteiro, descricaoRoteiro) {
            roteirizacao.idRoteiro = idRoteiro;
            roteirizacao.nomeRoteiro = descricaoRoteiro;
            roteirizacao.idRota = "";
            roteirizacao.nomeRota = "";
            roteirizacao.definirTransferenciaRoteiro();
            roteirizacao.popularGridRotas();
            roteirizacao.tipoInclusao = TipoInclusao.ROTA;
            roteirizacao.tipoExclusao = TipoExclusao.ROTEIRO;
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
        
        confirmarInclusaoRota : function() {
            $.postJSON(contextPath + '/cadastro/roteirizacao/incluirRota',
                 {
                    'roteiroId' :  roteirizacao.idRoteiro,
                    'ordem' :  $("#inputOrdem", roteirizacao.worksapce).val(),
                    'nome' :  $("#inputNome", roteirizacao.workspace).val()
                    
                 },
                   function(result) {
                        
                        var tipoMensagem = result.tipoMensagem;
                        var listaMensagens = result.listaMensagens;
                        
                        if (tipoMensagem && listaMensagens) {
                            
                            exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialog-novo-dado');
                        } else {
                            roteirizacao.popularGridRotas(result.rotas);
                            $('#dialog-novo-dado', roteirizacao.workspace).dialog("close");
                        }
                   },
                   null,
                   true
            );
        },
            
        buscaRotasSelecionadas : function(){
             rotasSelecinadas = new Array();
            $("input[type=checkbox][name='rotaCheckbox']:checked", roteirizacao.workspace).each(function(){
                rotasSelecinadas.push(parseInt($(this).val()));
            });
            
            return rotasSelecinadas;
        },
        
        excluirRota : function() {
        	$.postJSON(contextPath + '/cadastro/roteirizacao/excluirRota',
        		{
			    	'rotaId' : roteirizacao.idRota,
			    	'roteiroId' :  roteirizacao.idRoteiro
        		},
        		function(result) {
        			var tipoMensagem = result.tipoMensagem;
        			var listaMensagens = result.listaMensagens;
        			$('#dialog-rota', roteirizacao.workspace).dialog( "close" );
        			
        			if (tipoMensagem && listaMensagens) {
        				exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
        			}
			     
        			roteirizacao.popularGridRotas();
        		},
        		null,
        		true
        	);
	    },
	    
	    excluirRoteiro : function(){
	    	
	    	$.postJSON(contextPath + '/cadastro/roteirizacao/excluirRoteiro',
		        {
		           'roteiroId' :  roteirizacao.idRoteiro
		        },
		        function(result) {
		        	var tipoMensagem = result.tipoMensagem;
				    var listaMensagens = result.listaMensagens;
				    $('#dialog-rota', roteirizacao.workspace).dialog( "close" );
				    
				    if (tipoMensagem && listaMensagens) {
				    	exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
				    }
				    
				    roteirizacao.popularGridRoteiros();
		        },
		        null,
		        true
	       );
	    },
	    
	    popupExcluirRotaRoteiro : function() {
	    	
	    	if (roteirizacao.tipoExclusao == TipoExclusao.ROTA){
	    		
	    		$("#msgConfExclusaoRotaRoteiro", roteirizacao.workspace).text(
	    				"Confirma a exclusão da rota " + roteirizacao.nomeRota + "?");
	    		
	    		$("#dialog-excluir-rota-roteiro", roteirizacao.workspace).attr("title", "Rotas");
	    	} else if (roteirizacao.tipoExclusao == TipoExclusao.ROTEIRO){
	    		
	    		$("#msgConfExclusaoRotaRoteiro", roteirizacao.workspace).text(
	    				"Confirma a exclusão do roteiro " + roteirizacao.nomeRoteiro + "?");
	    		
	    		$("#dialog-excluir-rota-roteiro", roteirizacao.workspace).attr("title", "Roteiros");
	    	} else {
	    		
	    		//TODO tratar exclusão de associações
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
	                		
	                		//TODO tratar exclusão de associações
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

        excluiRotas : function() {
            $.postJSON(contextPath + '/cadastro/roteirizacao/excluiRotas',
                     {
                        'rotasId' : roteirizacao.buscaRotasSelecionadas(),
                        'roteiroId' :  $("#idRoteiroSelecionado", roteirizacao.workspace).val()
                        
                     },
                       function(result) {
                            var tipoMensagem = result.tipoMensagem;
                            var listaMensagens = result.listaMensagens;
                            $('#dialog-rota', roteirizacao.workspace).dialog( "close" );
                            if (tipoMensagem && listaMensagens) {
                                exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
                            }
                            $(".rotasGrid", roteirizacao.workspace).flexReload();
                            
                       },
                       null,
                       true
            );
        },
    
	    popupExcluirRotas : function() {
	    $( "#dialog-excluir-rotas", roteirizacao.workspace ).dialog({
	            resizable: false,
	            height:'auto',
	            width:400,
	            modal: true,
	            buttons: {
	                "Confirmar": function() {
	                    roteirizacao.excluiRotas();
	                    $( this ).dialog( "close" );
	                },
	                "Cancelar": function() {
	                    $( this ).dialog( "close" );
	                }
	            },
	            form: $("#dialog-excluir-rotas", this.workspace).parents("form")
	        }); 
	              
	    },
	    
	    popupDetalhesCota : function(title, box, roteiro, rota) {
	        
	        $('#legendDetalhesCota').html(title);
	        
	        $( "#dialog-detalhes" ).dialog({
	            resizable: false,
	            height:'auto',
	            width:420,
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
	        //$( "#dialog:ui-dialog" ).dialog( "destroy" );
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
	    
	    transferirRotas : function() {
	        $('#roteiroTranferenciaSelecionadoId', roteirizacao.workspace).val(roteiro.id);
	        
	        var roteiroId = null;
	        
	        if ( $('#roteiroTranferenciaSelecionadoId', roteirizacao.workspace).val() != null &&   $.trim( $('#roteiroTranferenciaNome', roteirizacao.workspace).val()) == $('#roteiroTranferenciaSelecionadoNome', roteirizacao.workspace).val()) {
	            roteiroId =  $('#roteiroTranferenciaSelecionadoId', roteirizacao.workspace).val();
	        }
	        
	        $.postJSON(contextPath + '/cadastro/roteirizacao/transferirRotas',
	                 {
	                    'rotasId' : roteirizacao.buscaRotasSelecionadas(),
	                    'roteiroId' : roteiroId,
	                    'roteiroNome' : $('#roteiroTranferenciaNome', roteirizacao.workspace).val()
	                    
	                 },
	                   function(result) {
	                        var tipoMensagem = result.tipoMensagem;
	                        var listaMensagens = result.listaMensagens;
	                        $('#dialog-rota', roteirizacao.workspace).dialog( "close" );
	                        if (tipoMensagem && listaMensagens) {
	                            exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
	                        }
	                        $(".rotasGrid").flexReload();
	                        
	                   },
	                   null,
	                   true
	        );
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
                            $('#dialog-rota', roteirizacao.workspace).dialog( "close" );
                            if (tipoMensagem && listaMensagens) {
                                exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
                            }
                            $(".rotasGrid", roteirizacao.workspace).flexReload();
                            
                       },
                       null,
                       true
            );
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
                    var selecione = '<input type="checkbox" class="checkboxCotasRota" name="checkboxCotasRota" value="'+ id +'"/>';
                    value.cell.selecione = selecione;
                    var ordem = '<input type="text" onchange="roteirizacao.ordemPdvChangeListener(this, \''+ id + '\');" class="inputGridCotasRota" value="'+ value.cell.ordem  +'" style="width:30px; text-align:center;">';
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
                $('.cotasRotaGrid').flexOptions({params: [
                    {name:'idRota', value: roteirizacao.idRota}
                ]});
                return true;
            },
            width : 875,
            height : 150
        });
        roteirizacao.limparGridCotasRota();
    },
    
    selecionarTodosPdvs : function() {
    	if ($('#selecionarTodosPdv', roteirizacao.workspace).is(':checked')) {
    		$(".checkboxCotasRota", roteirizacao.workspace).prop('checked', true);
    	} else {
    		$(".checkboxCotasRota", roteirizacao.workspace).prop('checked', false);
    	}
    },

    ordemPdvChangeListener : function(element, idPdv) {
    	var ordemAntiga = element.defaultValue;
        var ordem = $(element).val();
    	var param = [{name: 'idRota', value: roteirizacao.idRota}, 
    	             {name: 'idPdv', value: idPdv}, 
    	             {name: 'ordem',  value: ordem}];
    	
    	 $.postJSON(contextPath + '/cadastro/roteirizacao/ordemPdvChangeListener', param,
                function(result) {
    		 	    if (result) {
                         element.defaultValue = ordem;
                    }
                    if (!result) {
    		 	    	exibirMensagemDialog("WARNING", ["Ordem já utilizada!"],'dialogRoteirizacao');
                        $(element).val(ordemAntiga);
                    }
                },
                null,
                true
             );
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
            $("#cepPesquisa", roteirizacao.workspace).mask("99999-999");
            $("#numeroCotaPesquisa", roteirizacao.workspace).val('');
            $.postJSON(contextPath + '/cadastro/roteirizacao/iniciaTelaCotas',null,
                    function(result) {
                        roteirizacao.populaComboUf(result);
                        roteirizacao.iniciaCotasDisponiveisGrid();
                        $( "#dialog-cotas-disponiveis", roteirizacao.workspace ).dialog({
                            resizable: false,
                            height:470,
                            width:870,
                            modal: true,
                            buttons: {
                                "Confirmar": function() {
                                    roteirizacao.confirmaRoteirizacao();
                                    $( this ).dialog( "close" );
                                    
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
            $.postJSON(contextPath + '/cadastro/roteirizacao/buscalistaMunicipio',
                    { 
                        'uf' :$('#comboUf', roteirizacao.workspace).val()
                    }
                    ,
                    function(result) {
                        roteirizacao.populaMunicipio(result);
                    },
                    null,
                    true
                );

        },
        
        buscalistaBairro : function () {
            $.postJSON(contextPath + '/cadastro/roteirizacao/buscalistaBairro',
                    { 
                        'uf' :$('#comboUf', roteirizacao.workspace).val(),
                        'municipio' :$('#comboMunicipio', roteirizacao.workspace).val()
                    }
                    ,
                    function(result) {
                        roteirizacao.populaBairro(result);
                    },
                    null,
                    true
                );

        },
        
        populaComboUf : function(result) {
            $('#comboUf > option', roteirizacao.workspace).remove();
            $('#comboUf', roteirizacao.workspace).append('<option value=""> Selecione...</option>');
            roteirizacao.resetComboBairro();
            roteirizacao.resetComboMunicipio();
            
            $.each(result, function(index, row){
                $('#comboUf', roteirizacao.workspace).append('<option>'+row+'</option>');
                }
            );
            
        },
        
        populaMunicipio : function(result) {
            roteirizacao.resetComboBairro();
            roteirizacao.resetComboMunicipio();
            $.each(result, function(index, row){
                $('#comboMunicipio', roteirizacao.workspace).append('<option value="'+row.locNu+'">'+row.locNo+'</option>');
                }
            );
            
        },
        
        populaBairro : function(result) {
            roteirizacao.resetComboBairro();
            $.each(result, function(index, row){
                $('#comboBairro', roteirizacao.workspace).append('<option>'+row.baiNo+'</option>');
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
        
        iniciaCotasDisponiveisGrid : function(){
            $(".cotasDisponiveisGrid", roteirizacao.workspace).clear();
            $(".cotasDisponiveisGrid", roteirizacao.workspace).flexigrid({
                preProcess:roteirizacao.callCotasDisponiveisGrid,
                dataType : 'json',
                colModel : [ {
                    display : 'Pto. Venda',
                    name : 'pontoVenda',
                    width : 95,
                    sortable : true,
                    align : 'left'
                }, {
                    display : 'Orig. End',
                    name : 'origemEndereco',
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
                    name : 'numeroCota',
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
                    name : 'selecione',
                    width : 20,
                    sortable : true,
                    align : 'center'
                }],
                sortname : "cota",
                width : 800,
                height : 200
            });
        },
        
        callCotasDisponiveisGrid :  function (data){
            
            if  ( data.rows.length == 0   ){
                 exibirMensagemDialog("WARNING", ["Não exitem cota disponíveis."],'dialogRoteirizacaoCotaDisponivel');
                
            }
            
            $.each(data.rows, function(index, value) {
                var idPontoVenda = value.cell.idPontoVenda;
                var selecione = '<input type="checkbox" value="'+idPontoVenda +'" name="pdvCheckbox" id="pdvCheckbox" />';
                var ordem ='<input type="input" value="'+index +'" name="pdvOrdem'+idPontoVenda+'" id="pdvOrdem'+idPontoVenda+'" size="6" length="6" />';
                value.cell.selecione = selecione;
                value.cell.ordem = ordem;
                
            });
            
            $(".grids", roteirizacao.workspace).show();
            
            return data;
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
    

	    confirmaRoteirizacao : function () {
	        var params = roteirizacao.populaParamentrosContaSelecionadas();
	        $.postJSON(contextPath + '/cadastro/roteirizacao/confirmaRoteirizacao', params,
	                function(result) {
	                   // roteirizacao.populaBairro(result);
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
	        //$( "#dialog:ui-dialog" ).dialog( "destroy" );
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

		carregarComboRoteiro : function () {
		    roteirizacao.resetComboRoteiroPesquisa();
		    roteirizacao.resetComboRotaPesquisa();
		    $.postJSON(contextPath + '/cadastro/roteirizacao/carregarComboRoteiro',
		            { 
		                'boxId' :$('#boxPesquisa', roteirizacao.workspace).val()
		            }
		            ,
		            function(result) {
		                    $.each(result, function(index, row){
		                        $('#roteiroPesquisa', roteirizacao.workspace).append('<option value="'+row.id+'">'+row.descricaoRoteiro+'</option>');
		                        }
		                    );
		            },
		            null,
		            true
		        );
		
		},

		carregarComboRota : function () {
		    roteirizacao.resetComboRotaPesquisa();
		    $.postJSON(contextPath + '/cadastro/roteirizacao/carregarComboRota',
		            { 
		                'roteiroId' :$('#roteiroPesquisa', roteirizacao.workspace).val()
		            }
		            ,
		            function(result) {
		                $.each(result, function(index, row){
		                    $('#rotaPesquisa', roteirizacao.workspace).append('<option value="'+row.id+'">'+row.descricaoRota+'</option>');
		                    }
		                );
		        
		            },
		            null,
		            true
		        );
		
		},

		carregarComboRoteiroEspecial : function () {
		    roteirizacao.resetComboRoteiroPesquisa();
		    roteirizacao.resetComboRotaPesquisa();
		    $.postJSON(contextPath + '/cadastro/roteirizacao/carregarComboRoteiroEspecial',null ,
		            function(result) {
		                    $.each(result, function(index, row){
		                        $('#roteiroPesquisa', roteirizacao.workspace).append('<option value="'+row.id+'">'+row.descricaoRoteiro+'</option>');
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
		
		resetComboRotaPesquisa : function(){
		    $('#rotaPesquisa > option', roteirizacao.workspace).remove();
		    $('#rotaPesquisa', roteirizacao.workspace).append('<option value="" >Selecione...</option>');
		},

		pesquisaComRoteiroEspecial : function() {
		    
		     if ($("#tipoRoteiroPesquisa", roteirizacao.workspace).is(":checked") ) {
		         roteirizacao.resetComboRoteiroPesquisa();
		         roteirizacao.resetComboRotaPesquisa();
		         $('#boxPesquisa', roteirizacao.workspace).attr("disabled", "disabled");
		         $('#boxPesquisa', roteirizacao.workspace).val("");
		         roteirizacao.carregarComboRoteiroEspecial();
		          
		     } else {
		         roteirizacao.resetComboRoteiroPesquisa();
		         roteirizacao.resetComboRotaPesquisa();
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

		roteiroEspecialNovo : function() {
		    
		     if ($("#tipoRoteiro", roteirizacao.workspace).is(":checked") ) {
		         $('#boxInclusaoRoteiro', roteirizacao.workspace).attr("disabled", "disabled");
		         $('#boxInclusaoRoteiro', roteirizacao.workspace).val("");
		          
		     } else {
		         $('#boxInclusaoRoteiro', roteirizacao.workspace).val("");
		         $('#boxInclusaoRoteiro', roteirizacao.workspace).removeAttr("disabled");
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
		                display : 'Qtd. Cotas',
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
	
	            var idBox       = value.cell.idBox;
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

            roteirizacao.prepararPopupRoteirizacao();

	    },
	    
	    detalharRotaRoteiroCotasSumarizadas : function(title, idBox, idRota, idRoteiro) {
	        
	        var data = [];
	        
	        data.push({name:'idBox',        value: idBox });
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
            roteirizacao.idRoteiro = "";
            roteirizacao.idRota = "";
            roteirizacao.definirTipoEdicao(TipoEdicao.NOVO);
	        roteirizacao.prepararPopupRoteirizacao();
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
	             roteirizacao.habilitaBotao('botaoTransfereciaRota', function(){roteirizacao.popupTransferirRota()});
	             roteirizacao.habilitaBotao('botaoExcluirRota',function(){roteirizacao.popupExcluirRotaRoteiro()});
	         }
	    },

	    habilitaBotoesRoteirizacao : function() {
	        
	        listaRoteirizacao = roteirizacao.buscaRoteirizacaoSelecionadas()
	        
	         if (listaRoteirizacao.length == 0 ) {
	             roteirizacao.desabilitaBotao('botaoTransferenciaRoteiro', roteirizacao.workspace);
	                roteirizacao.desabilitaBotao('botaoExcluirRoteirizacao', roteirizacao.workspace);
	             
	         } else {
	             roteirizacao.habilitaBotao('botaoTransferenciaRoteiro', function(){roteirizacao.popupTransferirCota()});
	             roteirizacao.habilitaBotao('botaoExcluirRoteirizacao',function(){roteirizacao.popupExcluirRoteirizacao()});
	         }
	    },
    
	    prepararPopupRoteirizacao : function(){
  	        roteirizacao.iniciarGridBox();
            if (roteirizacao.isNovo()) {
                $('#nomeBox', roteirizacao.workspace).prop('disabled', false);
                $('#lnkPesquisarBox', roteirizacao.workspace).click(function() {roteirizacao.pesquisarBox()});
            } else {
                $('#nomeBox', roteirizacao.workspace).prop('disabled', true);
                $('#lnkPesquisarBox', roteirizacao.workspace).unbind('click');
            }

	        roteirizacao.iniciarGridRoteiros();
	        roteirizacao.iniciarGridRotas();
	        roteirizacao.iniciarGridCotasRota();
	
	        var method = '';
	        var param = [];
	        if (roteirizacao.isNovo()) {
	            method = 'novaRoteirizacao';
	        } else {
	            method = 'editarRoteirizacao';
	            param.push({name: 'idRoteirizacao', value: roteirizacao.idRoteirizacao});
	        }
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
                        }
	                }
	                $( "#dialog-roteirizacao", roteirizacao.workspace ).dialog({
	                    resizable: false,
	                    height:610,
	                    width:955,
	                    modal: true,
                        title : roteirizacao.isNovo() ? 'Nova Roteirização' : 'Editar Roteirização',
	                    buttons: {
	                        "Confirmar": function() {
	                            roteirizacao.confirmarRoteirizacao();
                                $( this ).dialog( "close" );

	
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
        	 $.postJSON(contextPath + '/cadastro/roteirizacao/confirmarRoteirizacao',
                     null,
                     null,
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
                    width : 100,
                    sortable : true,
                    align : 'left'
                }, {
                    display : 'Nome',
                    name : 'nome',
                    width : 250,
                    sortable : true,
                    align : 'left'
                }],
                sortname : "numeroCota",
                width : 380,
                height : 140
            });
            
            $(".cotasDisponiveisGrid",roteirizacao.workspace).flexigrid({
				preProcess : roteirizacao.preProcessamentoPdvs,
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
					sortable : true,
					align : 'center'
				}],
				sortname : "ordem",
				width : 800,
				height : 200
			});

        },
        
        limparCamposNovaInclusao : function(){
            
            $("#selectTipoNovoDado", roteirizacao.workspace).val("ROTEIRO");
            $("#inputOrdem", roteirizacao.workspace).val("");
            $("#inputNome", roteirizacao.workspace).val("");
            $("#checkRoteiroEspecial", roteirizacao.workspace).hide();

		    $(".cotasDisponiveisGrid", roteirizacao.workspace).clear();
		    $('#spanDadosRoteiro', roteirizacao.workspace).html('<strong>Roteiro Selecionado:</strong>&nbsp;&nbsp; <strong>Box: </strong>&nbsp;&nbsp; <strong>Ordem: </strong>&nbsp;');
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
				
		

		
		
		//PDVS	
        buscaPdvsDisponiveis : function() {
	    	
			$(".cotasDisponiveisGrid", this.workspace).flexOptions({

				url: contextPath + "/cadastro/roteirizacao/obterPdvsDisponiveis",
				params: [
				         {name:'numCota', value:$("#idRotaSelecionado", this.workspace).val()},
				         {name:'municipio', value:$("#idRotaSelecionado", this.workspace).val()},
				         {name:'uf', value:$("#idRotaSelecionado", this.workspace).val()},
				         {name:'bairro', value:$("#idRotaSelecionado", this.workspace).val()},
				         {name:'cep', value:$("#idRotaSelecionado", this.workspace).val()}
				        ] ,
				        newp: 1
			});

			$(".cotasDisponiveisGrid", this.workspace).flexReload();
			
			$(".grids", this.workspace).show();
		},
		
		preProcessamentoPdvs : function(result){
			
			$.each(result.rows, function(index, value) {

					value.cell.ordem 	=  '<input name="ordem" id="ordem'+ value.cell.id +'" style="width: 45px;" type="text" value="'+value.cell.ordem+'"/>'

					value.cell.selecionado = '<input title="Selecionar Item" type="checkbox" id="selecionado'+value.cell.id+'" name="selecionado" />';

			});

			return result;
		},

		popupsPdvs : function () {
		    	
			roteirizacao.buscaPdvsDisponiveis();
			
			$( "#dialog-pdvs", roteirizacao.workspace ).dialog({
				resizable: false,
				height:540,
				width:850,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						$( this ).dialog( "close" );
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				form: $("#dialog-pdvs", this.workspace).parents("form")
	        });
		},
		
		confirmarInclusaoPdvs : function() {
		 	$.postJSON(contextPath + '/cadastro/roteirizacao/incluirRota',
				 {
					'roteiroId' :  $("#idRoteiroSelecionado").val(),
					'ordem' :  $("#ordemRotaInclusao").val(),
					'nome' :  $("#nomeRotaInclusao").val()
					
				 },
				   function(result) {
						var tipoMensagem = result.tipoMensagem;
						var listaMensagens = result.listaMensagens;
						$('#dialog-rota', roteirizacao.workspace).dialog( "close" );
						if (tipoMensagem && listaMensagens) {
							exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
						}
						$(".rotasGrid", roteirizacao.workspace).flexReload();
						
				   },
				   null,
				   true
			);
		},
			
		buscaPdvsSelecionados : function(){
			 rotasSelecinadas = new Array();
			$("input[type=checkbox][name='rotaCheckbox']:checked", roteirizacao.workspace).each(function(){
				rotasSelecinadas.push(parseInt($(this).val()));
			});
			
			return rotasSelecinadas;
		},
		
		excluiPdvs : function() {
		 	$.postJSON(contextPath + '/cadastro/roteirizacao/excluiRotas',
					 {
						'rotasId' : roteirizacao.buscaRotasSelecionadas(),
						'roteiroId' :  $("#idRoteiroSelecionado", roteirizacao.workspace).val()
						
					 },
					   function(result) {
							var tipoMensagem = result.tipoMensagem;
							var listaMensagens = result.listaMensagens;
							$('#dialog-rota', roteirizacao.workspace).dialog( "close" );
							if (tipoMensagem && listaMensagens) {
								exibirMensagemDialog(tipoMensagem, listaMensagens,'dialogRoteirizacao');
							}
							$(".rotasGrid", roteirizacao.workspace).flexReload();
							
					   },
					   null,
					   true
			);
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
			      
		}
		
		
}, BaseController);

$(function() {
    roteirizacao.init();
});

//@ sourceURL=roteirizacao.js
