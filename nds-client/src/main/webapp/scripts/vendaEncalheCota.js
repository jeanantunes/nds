var vendaEncalhe = {
		url:null,
		urlExport:null,
		grid:null,
		dialogId:null,
		initGrid: function(gridId) {
			this.grid = $(gridId).flexigrid({
				preProcess: function(data) {
					if(typeof data.mensagens == "object") {
						
						$(this.dialogId).hide();
					
						exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
						
					}else{			
						var totalFonecedor = {};
						$.each(data.rows, function(index, value) {						
							var nomeFornecedor = value.cell.nomeFornecedor;	
							
							if(!totalFonecedor[nomeFornecedor]){
								totalFonecedor[nomeFornecedor] = value.cell.total;
							}else{
								totalFonecedor[nomeFornecedor] += value.cell.total;
							}
									
						});
					 var table=document.getElementById("totaisFornecedores-venda-encalhe");
					 
					 for(var i= 1; i<table.rows.length;i++){
						 table.deleteRow(i);
					 }
					 var row = table.rows[0];
					 if(row.cells.length>1){
				    	  row.deleteCell(2);
					      row.deleteCell(1);
				      }
					 
					 var primeira = true;
					 for(var fornecedor in totalFonecedor){						     
					      if(primeira){
					    	  primeira = false;
					    	  
					      }else{					    	 
					    	  row = table.insertRow(table.rows.length);
					    	  row.insertCell(0);
					      }
					      var cell1=row.insertCell(1);
					      var cell2=row.insertCell(2);
					      cell1.innerHTML='<strong>' + fornecedor + ':</strong>';
					      cell2.align="right";
					      cell2.innerHTML=totalFonecedor[fornecedor];
					 }
					 
					
					 return data;
					}	
				},
				dataType : 'json',
				colModel : [ {
					display : 'Código',
					name : 'codigoProduto',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : 'Produto',
					name : 'nomeProduto',
					width : 150,
					sortable : true,
					align : 'left'
				}, {
					display : 'Edição',
					name : 'numeroEdicao',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Preço de Capa R$',
					name : 'precoCapa',
					width : 90,
					sortable : true,
					align : 'right'
				}, {
					display : 'Preço c/ Desc. R$',
					name : 'precoComDesconto',
					width : 60,
					sortable : true,
					align : 'right'
				}, {
					display : 'Box',
					name : 'box',
					width : 40,
					sortable : true,
					align : 'center'
				}, {
					display : 'Exemplares',
					name : 'exemplares',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Fornecedor',
					name : 'nomeFornecedor',
					width : 80,
					sortable : true,
					align : 'left'
				}, {
					display : 'Total R$',
					name : 'total',
					width : 70,
					sortable : true,
					align : 'right',
				}],
				sortname : "codigoProduto",
				sortorder : "asc",
				width : 800,
				height : 200
			});
		},
		
		dialog: function(dialogId) {
			$(dialogId).dialog({
				resizable: false,
				height:460,
				width:860,
				modal: true,
				buttons: {
					"Fechar": function() {
						$( this ).dialog( "close" );
						
						$(".grids").show();
						
					}
				}
			});
		},
		loadData:function(url, idConsolidado){
			$(this.grid).flexOptions({"url": url, params:[{name:"idConsolidado",value:idConsolidado}]});	
			$(this.grid).flexReload();
			
		},
		showDialog:function(idConsolidado,dataEscolhida){			
			this.dialog(this.dialogId);
			this.loadData(this.url, idConsolidado);
			this.exportButtons(idConsolidado);
			
			
			 $("#datacotanome-venda-encalhe").html(dataEscolhida+" Cota: "+$("#cota").val()+" - "+$("#nomeCota").val());
		},
		exportButtons: function(idConsolidado) {
			$("#dialog-venda-encalhe-export-pdf").attr('href', this.urlExport + "?fileType=PDF" + "&idConsolidado=" + idConsolidado);
			$("#dialog-venda-encalhe-export-xls").attr('href', this.urlExport + "?fileType=XLS" + "&idConsolidado=" + idConsolidado);
		}
		
		
		
		
		
};