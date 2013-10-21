var cotaBloqueadaController = $.extend(true, {
	
	init : function() {
		
		$(".cotasBloqueadasGrid", cotaBloqueadaController.workspace).flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Usuario',
				name : 'nomeUsuario',
				width : 170,
				sortable : false,
				align : 'left'
			},{
				display : 'Cota',
				name : 'numeroCota',
				width : 80,
				sortable : false,
				align : 'left'
			},{
				display : 'Nome',
				name : 'nomeCota',
				width : 380,
				sortable : false,
				align : 'left'
			}],
			sortname : "numeroCota",
			sortorder : "asc",
			usepager : false,
			useRp : false,
			width : 680,
			height : 250
		});
	},
	
	popupCotasBloqueadas : function(){

		$(".cotasBloqueadasGrid", cotaBloqueadaController.workspace).flexOptions({
			url: contextPath + "/cotaBloqueada/obterCotasBloqueadas",
			params: [],
			newp: 1,
		});
		
		$(".cotasBloqueadasGrid", cotaBloqueadaController.workspace).flexReload();
		
		$(".grids", cotaBloqueadaController.workspace).show();
		
		$( "#dialog-cotas-bloqueadas", cotaBloqueadaController.workspace).dialog({
   			resizable: false,
   			height:'auto',
   			width:700,
   			modal: true,
   			buttons: {
   				
   				"Fechar": function() {
   					
   					$(this).dialog( "close" );
   				}
   			},
   			form: $("#dialog-cotas-bloqueadas", cotaBloqueadaController.workspace).parents("form")
		});
	},
	
	existeConferenciaEncalheEmAndamento : function(callbackFalse, callbackTrue){
		
		$.postJSON(contextPath + "/cotaBloqueada/existeConferenciaEncalheEmAndamento",
			null,
			function (result) {
                
			    if (result.boolean == false){
			        
			    	callbackFalse();
                }
			    else{
			    	
			    	callbackTrue();
			    	
			    	cotaBloqueadaController.popupCotasBloqueadas();
			    }
			}
		);
	},
	
	verificaBloqueioCotaEncalhe : function(callbackFalse, callbackTrue){

        cotaBloqueadaController.existeConferenciaEncalheEmAndamento(callbackFalse, callbackTrue);
	}
	
}, BaseController);
//@ sourceURL=cotaBloqueada.js