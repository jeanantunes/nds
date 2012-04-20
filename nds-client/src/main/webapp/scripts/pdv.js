var PDV = {
		
		popup_novoPdv:function() {
			
			$( "#dialog-pdv" ).dialog({
				resizable: false,
				height:600,
				width:890,
				modal: true
			});
		},
		
		pesquisarPdvs: function (idCota){
			
			var param = [{name:"idCota",value:idCota}];
			
			$(".PDVsGrid").flexOptions({
				url: contextPath + "/cadastro/pdv/consultar",
				params: param
			});
			
			$(".PDVsGrid").flexReload();
		},
		
		executarPreProcessamento : function (resultado){

			// Monta as colunas com os inputs do grid
			$.each(resultado.rows, function(index, row) {
				
				var param = '\'' + row.cell.idPdv +'\','+'\''+ row.cell.idCota + '\',' + index;
				
				var linkEdicao = '<a href="javascript:;" onclick="PDV.editarPDV('+ param +');" style="cursor:pointer">' +
					 '<img src="'+ contextPath +'/images/ico_editar.gif" hspace="5" border="0px" title="Editar PDV" />' +
					 '</a>';			
				 
				var linkExclusao ='<a href="javascript:;" onclick="PDV.excluirPDV('+param +' );" style="cursor:pointer">' +
	                 '<img src="'+ contextPath +'/images/ico_excluir.gif" hspace="5" border="0px" title="Excluir PDV" />' +
	                  '</a>';		 					 
				
                 row.cell.acao = linkEdicao + linkExclusao; 
			});
				
			return resultado;
		},
		
		editarPDV:function (idPdv,idCota,index){
			
			$.postJSON(contextPath + "/cadastro/pdv/editar",
					[{name:"idPdv",value:idPdv},
					 {name:"idCota",value:idCota}
					 ], function(result){
				
				PDV.carregarDadosEdicao(result);
				
			},PDV.errorEditarPDV,true); 
		},
		
		excluirPDV:function(idPdv,idCota,index){
			
			$.postJSON(contextPath + "/cadastro/pdv/excluir",
					[{name:"idPdv",value:idPdv},
					 {name:"idCota",value:idCota},
					 {name:"id",value:index}], function(result){
				
				PDV.pesquisarPdvs(idCota);
				
			},PDV.errorExcluirPDV,true);
		},
		
		errorEditarPDV: function (){
			
		},
		
		errorExcluirPDV: function (){
			
		},
		carregarDadosEdicao:function (result){
			
			PDV.popup_novoPdv();
		}
};