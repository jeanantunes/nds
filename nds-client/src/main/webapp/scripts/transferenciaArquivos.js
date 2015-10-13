var transferenciaArquivosController = $.extend(true, {
	init : function() {
		
		$('#configuracaoDiretorios').click(function(){
			transferenciaArquivosController.exibirConfiguracaoDiretorios();
		});
		
		$('#comboDiretorios').change(function (){
			$("#div-upload").hide();
		});
		
		$(document).ready(function(){
			
			var obj = $("#dragandrophandler");
				
				obj.on('dragenter', function (e){
	    			e.stopPropagation();
		    		e.preventDefault();
		    		$(this).css('border', '2px solid #0B85A1');
		    		});
		
				obj.on('dragover', function (e){
				    e.stopPropagation();
				    e.preventDefault();
				});
				
				obj.on('drop', function (e){
				 
				     $(this).css('border', '2px dotted #0B85A1');
				     e.preventDefault();
				     
				     var files = e.originalEvent.dataTransfer.files;
				 
				     //We need to send dropped files to Server
				     transferenciaArquivosController.handleFileUpload(files,obj);
				});
				
				$(document).on('dragenter', function (e){
				    e.stopPropagation();
				    e.preventDefault();
				});
				
				$(document).on('dragover', function (e){
				  e.stopPropagation();
				  e.preventDefault();
				  obj.css('border', '2px dotted #0B85A1');
				});
				
				$(document).on('drop', function (e){
				    e.stopPropagation();
				    e.preventDefault();
				});
		 
		});
		
		$("#gridDiretorios").flexigrid({
			preProcess : transferenciaArquivosController.preProssGridDiretorios,
			dataType : 'json',
			colModel : [ {
				display : 'Nome diretório',
				name : 'nomeDiretorio',
				width : 200,
				sortable : true,
				align : 'left',
			}, {
				display : 'Diretório',
				name : 'pastaDiretorio',
				width : 300,
				sortable : true,
				align : 'left'
			},{
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : true,
				align : 'center'
			} ],
			sortname : "nomeDiretorio",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 10,
			width : 600,
			height : 200
			
		});
		
		$("#gridDownload").flexigrid({
			preProcess : transferenciaArquivosController.preProssGridDownload,
			dataType : 'json',
			colModel : [ {
				display : 'Nome Arquivo',
				name : 'nomeArquivo',
				width : 150,
				sortable : true,
				align : 'left',
			}, {
				display : 'Diretório',
				name : 'pathArquivo',
				width : 215,
				sortable : true,
				align : 'left'
			},{
				display : 'Tamanho',
				name : 'tamanhoArquivo',
				width : 50,
				sortable : true,
				align : 'left'
			},{
				display : 'Modificado',
				name : 'dataUltimaModificacaoFormatada',
				width : 95,
				sortable : true,
				align : 'left'
			},{
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : true,
				align : 'center'
			} ],
			sortname : "nomeArquivo",
			sortorder : "asc",
			usepager : false,
			useRp : false,
			width : 650,
			height : 300
			
		});
		
	},
	
	preProssGridDiretorios : function (resultado){
		
		if (resultado.mensagens) {
			exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
			);
			
			$("#gridDiretorios", transferenciaArquivosController.workspace).hide();
			
			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {
			
//			var editar = '<a href="javascript:;"  onclick="transferenciaArquivosController.editarAjuste('+row.cell.idDiretorio+');" style="cursor:pointer">' +
//		   	 			 '<img title="Alterar diretório" src="' + contextPath + '/images/ico_editar.gif" hspace="5" border="0px" />' +
//		   	 			 '</a>';
			var excluir = '<a href="javascript:;" isEdicao="true" onclick="transferenciaArquivosController.excluirDiretorio('+row.cell.idDiretorio+');" style="cursor:pointer">' +
						  '<img title="Excluir diretório" src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" />' +
						  '</a>';

//			row.cell.acao = editar + excluir;
			row.cell.acao = excluir;
			
		});
		
		$("#gridDiretorios", transferenciaArquivosController.workspace).show();
		
		return resultado;
	},
	
	preProssGridDownload : function (resultado){
		
		if (resultado.mensagens) {
			exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
			);
			
			$("#gridDownload", transferenciaArquivosController.workspace).hide();
			
			return resultado;
		}

		$.each(resultado.rows, function(index, row) {
			
			var download = '<a href="javascript:;"  onclick="transferenciaArquivosController.downloadArquivo(this);" nomeArquivo="'+row.cell.nomeArquivo+'" pathArquivo="'+row.cell.pathArquivo+'" style="cursor:pointer">' +
		   	 			 '<img title="Download" src="' + contextPath + '/images/ico_salvar.gif" hspace="5" border="0px" />' +
		   	 			 '</a>';
			var excluir = '<a href="javascript:;" isEdicao="true" onclick="transferenciaArquivosController.excluirArquivo(this);" nomeArquivo="'+row.cell.nomeArquivo+'" pathArquivo="'+row.cell.pathArquivo+'" style="cursor:pointer">' +
						  '<img title="Excluir arquivo" src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0px" />' +
						  '</a>';

			row.cell.acao = download + excluir;
			
			row.cell.tamanhoArquivo = row.cell.tamanhoArquivo +" KB"; 
			
		});
		
		$("#gridDownload", transferenciaArquivosController.workspace).show();
		
		return resultado;
	},
	
	exibirConfiguracaoDiretorios : function() {
		
		$("#nomeDiretorio").val("");
		$("#nomePasta").val("");
		
		$("#dialog-diretorios").dialog({
			resizable : false,
			height : 500,
			width : 650,
			modal : true,
			open:function(){

				$("#gridDiretorios").flexOptions({
					url: contextPath + "/administracao/transferenciaArquivos/carregarDiretorios",
					dataType : 'json',
				});
				
				$("#gridDiretorios").flexReload();
			},
		});
	},
	
	exibirDownload : function() {
		
		$("#div-upload").hide();
		
		var dir = $("#comboDiretorios").val();
		
		if(dir == "Selecione..."){
	    	 exibirMensagem("WARNING", ["Escolha o diretório do arquivo."]);
	    	 return;
	     }
				
		$("#dialog-download").dialog({
			resizable : false,
			height : 500,
			width : 725,
			modal : true,
			open:function(){
				
				$("#gridDownload").flexOptions({
					url: contextPath + "/administracao/transferenciaArquivos/buscarArquivosDownload",
					dataType : 'json',
					params:[{name:'idDiretorio', value:dir}]
				});
				
				$("#gridDownload").flexReload();
				
			},
		});
	},
	
	exibirUpload : function() {
		
		if($("#comboDiretorios").val() == "Selecione..."){
	    	 exibirMensagem("WARNING", ["Escolha o diretório do arquivo."]);
	    	 return;
	     }
    
		$("#status1").text("");
		$("#div-upload").show();
	},
	
	excluirDiretorio : function(id){
		
		$("#dialog-excluir").dialog({
			
			resizable : false,
			height : 170,
			width : 380,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					$("#dialog-excluir").dialog("close");
					
					var data = [{name : 'idDiretorio', value : id}];
					
					$.postJSON(contextPath + "/administracao/transferenciaArquivos/excluirDiretorio", 
							data,
							function(result) {

									var tipoMensagem = result.tipoMensagem;
									var listaMensagens = result.listaMensagens;
									
									if (tipoMensagem && listaMensagens) {
										exibirMensagem(tipoMensagem, listaMensagens);
									}
									
									$("#gridDiretorios").flexReload();
							   },null,true
							 );
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			},
		});
	},
	
	salvarNovoDiretorio : function(){
		
		var nome = $("#nomeDiretorio").val();
		var pasta = $("#nomePasta").val();
		
		if(nome == undefined  || nome == ""){
			exibirMensagem("WARNING", ["Nome do diretório inválido."]);
			return;
		}
		
		if(pasta == undefined  || pasta == ""){
			exibirMensagem("WARNING", ["Pasta do diretório inválido."]);
			return;
		}
		
		var data = [{name : 'nomeDiretorio', value : nome}, 
		            {name : 'pastaDiretorio', value : pasta}];
		
		$.postJSON(contextPath + "/administracao/transferenciaArquivos/adicionarDiretorio", 
				data,
				function(result) {

						var tipoMensagem = result.tipoMensagem;
						var listaMensagens = result.listaMensagens;
						
						if (tipoMensagem && listaMensagens) {
							exibirMensagem(tipoMensagem, listaMensagens);
						}
						
//						$('#comboDiretorios').append($('<option>', {
//						    value: 10,
//						    text: 'My option'
//						}));
						
						$("#nomeDiretorio").val("");
						$("#nomePasta").val("");
						
						$("#gridDiretorios").flexReload();
				   },null,true
				 );
	},
	
	excluirArquivo : function(pathArquivo){
		
		$("#dialog-excluir").dialog({
			
			resizable : false,
			height : 170,
			width : 380,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					$("#dialog-excluir").dialog("close");
					var data = [{name : 'pathFile', value:$(pathArquivo).attr('pathArquivo')},
					            {name : 'nomeArquivo', value:$(pathArquivo).attr('nomeArquivo')}];
					
					$.postJSON(contextPath + "/administracao/transferenciaArquivos/excluirArquivo", 
							data,
							function(result) {
									var tipoMensagem = result.tipoMensagem;
									var listaMensagens = result.listaMensagens;
									
									if (tipoMensagem && listaMensagens) {
										exibirMensagem(tipoMensagem, listaMensagens);
									}
									$("#gridDownload").flexReload();
							   },null,true
							 );
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			},
		});
	},
	
	downloadArquivo : function(pathArquivo){
		
		var data = [{name : 'pathFile', value:$(pathArquivo).attr('pathArquivo')},
		            {name : 'nomeArquivo', value:$(pathArquivo).attr('nomeArquivo')}];

		exibirMensagem("SUCCESS", ["Aguarde enquanto seu download é preparado."]);
		
		$.fileDownload(contextPath + "/administracao/transferenciaArquivos/downloadArquivo", {
            httpMethod : "GET",
            data : data,
            failCallback : function(arg) {
                exibirMensagem("WARNING", ["Erro ao efetuar o download!"]);
            },
            
		});
		
		return false;
		
	},
	
	sendFileToServer : function(formData,status){
    	
		var idDiret =  $("#comboDiretorios option:selected").val();
		
		formData.append('idDiretorio', idDiret);
		
		var uploadURL = contextPath + "/administracao/transferenciaArquivos/uploadArquivo" //Upload URL
	    var extraData ={name : 'idDiretorio', value:idDiret}; //Extra Data.
	    var jqXHR=$.ajax({
	            xhr: function() {
	            var xhrobj = $.ajaxSettings.xhr();
	            if (xhrobj.upload) {
	                    xhrobj.upload.addEventListener('progress', function(event) {
	                        var percent = 0;
	                        var position = event.loaded || event.position;
	                        var total = event.total;
	                        if (event.lengthComputable) {
	                            percent = Math.ceil(position / total * 100);
	                        }
	                        //Set progress
	                        status.setProgress(percent);
	                    }, false);
	                }
	            return xhrobj;
	        },
	    url: uploadURL,
	    type: "POST",
	    contentType:false,
	    processData: false,
	        cache: false,
	        data: formData,
	        success: function(data){
	            status.setProgress(100);
	 
	            $("#status1").append(data.result.listaMensagens[0]+"<br>");         
	        }
	    }); 
	 
	    status.setAbort(jqXHR);
	},
	 
	
	createStatusbar : function(obj){
	     
		 var rowCount=0;
		 rowCount++;
	     var row="odd";
	     if(rowCount %2 ==0) row ="even";
	     
	     this.statusbar = $("<div class='statusbar "+row+"'></div>");
	     this.filename = $("<div class='filename'></div>").appendTo(this.statusbar);
	     this.size = $("<div class='filesize'></div>").appendTo(this.statusbar);
	     this.progressBar = $("<div class='progressBar'><div></div></div>").appendTo(this.statusbar);
	     this.abort = $("<div class='abort'>Abort</div>").appendTo(this.statusbar);
	     obj.after(this.statusbar);
	 
	    this.setFileNameSize = function(name,size){
	        var sizeStr="";
	        var sizeKB = size/1024;
	        if(parseInt(sizeKB) > 1024){
	            var sizeMB = sizeKB/1024;
	            sizeStr = sizeMB.toFixed(2)+" MB";
	        }else{
	            sizeStr = sizeKB.toFixed(2)+" KB";
	        }
	 
	        this.filename.html(name);
	        this.size.html(sizeStr);
	    }
	    
	    this.setProgress = function(progress){       
	        var progressBarWidth =progress*this.progressBar.width()/ 100;  
	        this.progressBar.find('div').animate({ width: progressBarWidth }, 10).html(progress + "% ");
	        
	        if(parseInt(progress) >= 100){
	            this.abort.hide();
	        }
	    }
	    
	    this.setAbort = function(jqxhr){
	        var sb = this.statusbar;
	        this.abort.click(function(){
	            jqxhr.abort();
	            sb.hide();
	        });
	    }
	},
	
	handleFileUpload : function(files,obj){
	   for (var i = 0; i < files.length; i++){
	        var fd = new FormData();
	        fd.append('file', files[i]);
	 
	        var status = new transferenciaArquivosController.createStatusbar(obj); //Using this we can set progress.
	        	status.setFileNameSize(files[i].name,files[i].size);
	        	transferenciaArquivosController.sendFileToServer(fd,status);
	   }
	},

}, BaseController);
//@ sourceURL=transferenciaArquivos.js
