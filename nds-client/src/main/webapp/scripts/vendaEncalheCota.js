var vendaEncalhe = $.extend(true, {
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
								totalFonecedor[nomeFornecedor] = parseFloat(value.cell.total);
							}else{
								totalFonecedor[nomeFornecedor] += parseFloat(value.cell.total);
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
					 var count = 0;
					 for(var fornecedor in totalFonecedor){		
					 	  count++;				     
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
					      cell2.innerHTML=formatMoneyValue(totalFonecedor[fornecedor]);
					 }
					 //$(gridId).flexToggleCol(6,count>1);
					
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
		loadData:function(url, idConsolidado, dataEscolhida,numeroCota){
			$(this.grid).flexOptions({"url": url, params:[{name:"idConsolidado",value:idConsolidado}, {name:"dataEscolhida", value: dataEscolhida}, {name:"numeroCota", value:numeroCota}]});	
			$(this.grid).flexReload();
			
		},
		showDialog:function(idConsolidado,dataEscolhida,numeroCota,numeroBox){			
			this.dialog(this.dialogId);
			this.loadData(this.url, idConsolidado, dataEscolhida,numeroCota);
			this.exportButtons(idConsolidado,dataEscolhida,numeroCota);
			
			
			 $("#datacotanome-venda-encalhe").html(dataEscolhida+" Cota: "+$("#cotaHidden").val()+" - "+$("#nomeCotaHidden").val());
			 
			 if (numeroBox){
				 
				 $("#datacotanome-venda-encalhe").html($("#datacotanome-venda-encalhe").html() + " - Box: " + numeroBox);
			 }
		},
		exportButtons: function(idConsolidado,dataEscolhida,numeroCota) {
			$("#dialog-venda-encalhe-export-pdf").attr('href', this.urlExport + "?fileType=PDF" + "&idConsolidado=" + idConsolidado + "&dataEscolhida=" + dataEscolhida + "&numeroCota=" + numeroCota);
			$("#dialog-venda-encalhe-export-xls").attr('href', this.urlExport + "?fileType=XLS" + "&idConsolidado=" + idConsolidado + "&dataEscolhida=" + dataEscolhida + "&numeroCota=" + numeroCota);
		}	
		
}, BaseController);
//@sourceURL=vendaEncalheCota.js