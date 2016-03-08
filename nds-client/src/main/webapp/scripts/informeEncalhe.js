/**
 * 
 */
function InformeEncalhe() {
	
	this.initDialogImprimir();
	this.initGrid();
	this.bindEvents();
	this.imprimirParans = {"idFornecedor": null,
		"semanaRecolhimento": null,
		"dataRecolhimento": null,
		'sortname':'nomeProduto',
		'sortorder':'asc'
		};
	$("#checkTipoImpressaoDados", this.workspace).button();
	$("#buttonsetTipoImpressaoCapas", this.workspace).buttonset();
	
	var _this = this;
	
	$("#sugerirSemana", this.workspace).click(function(e){
		
		if($(this).is(":checked")){
			_this.carregarDiaSemana();
		}else{
			$("#semanaRecolhimentoBox", _this.workspace).val("");
		 }
	});
};

InformeEncalhe.prototype.path = contextPath + "/devolucao/informeEncalhe/";

InformeEncalhe.prototype.initGrid = function() {
	var workspace = "#this.workspace div.ui-tabs-panel:not(.ui-tabs-hide)";
	var _this = this;
	$("#consultaInformeEncalheGrid", this.workspace).flexigrid(
			{
				preProcess : function(data) {
					if (typeof data.mensagens == "object") {

						exibirMensagem(data.mensagens.tipoMensagem,
								data.mensagens.listaMensagens);
						
						$("#consultaInformeEncalheGrid", this.workspace).children().remove();
						
					} else {
						$.each(data.rows, function(index, value) {
							var acao = "";
							
							if(typeof value.cell.sequenciaMatriz == 'undefined' || value.cell.sequenciaMatriz == null ) {
								value.cell.sequenciaMatriz = '';
							} 
							
							value.cell.acao = acao;
							if (!value.cell.chamadaCapa) {
								value.cell.chamadaCapa = '';
							}
							
							if (!value.cell.codigoDeBarras){
								value.cell.codigoDeBarras = '';
							}
						});
						return data;
					}
				},
				onSuccess: function() {
					bloquearItensEdicao(_this.workspace);
				},
				dataType : 'json',
				colModel : [ {
					display : 'Seq',
					name : 'sequenciaMatriz',
					width : 20,
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
					width : 120,
					sortable : true,
					align : 'left'
				}, {

					display : 'Edição',
					name : 'numeroEdicao',
					width : 40,
					sortable : true,
					align : 'left'
				}, {

					display : 'Chamada de Capa',
					name : 'chamadaCapa',
					width : 85,
					sortable : true,
					align : 'left'
				}, {

					display : 'Código Barras',
					name : 'codigoDeBarras',
					width : 110,
					sortable : true,
					align : 'left'
				}, {

					display : 'Preço de Capa R$',
					name : 'precoVendaFormatado',
					width : 90,
					sortable : true,
					align : 'right'
				}, {

					display : 'Preço Desconto R$',
					name : 'precoDescontoFormatado',
					width : 90,
					sortable : true,
					align : 'right'
				}, {

					display : 'Data de Lançamento',
					name : 'dataLancamento',
					width : 90,
					sortable : true,
					align : 'center'
				}, {
					display : 'Data Recolhimento',
					name : 'dataRecolhimento',
					width : 90,
					sortable : true,
					align : 'center'
				}, {

					display : 'Ação',
					name : 'idProdutoEdicao',
					width : 30,
					sortable : false,
					align : 'center',
					process : _this.processAcao
				} ],
				sortname : "sequenciaMatriz",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 'auto',
				onChangeSort:function(sortname, sortorder){
					console.log("sort");
					_this.imprimirParans.sortname = sortname;
					_this.imprimirParans.sortorder = sortorder;
					$("input[name='sortname']", this.workspace).val(sortname);
					$("input[name='sortorder']", this.workspace).val(sortorder);
					$(this).flexReload();
				}
			});

	$("#consultaInformeEncalheGrid", this.workspace).flexOptions({
		"url" : this.path + 'busca.json',
		params : [ {
			name : "idFornecedor",
			value : $("#idFornecdorSelect", this.workspace).val()
		}, {
			name : "semanaRecolhimento",
			value : $("#semanaRecolhimentoBox", this.workspace).val()
		}, {
			name : "dataRecolhimento",
			value : $("#dataRecolhimentoBox", this.workspace).val()
		} ]
	});

};

InformeEncalhe.prototype.processAcao = function(tdDiv, pid) {
	var capaPopup = new CapaPopup(tdDiv.innerHTML);
	$(tdDiv, this.workspace).empty();
	tdDiv.appendChild(capaPopup.panel);
};

InformeEncalhe.prototype.busca = function() {
	$("#consultaInformeEncalheGrid").flexOptions({
		"url" : this.path + 'busca.json',
		params : [ {
			name : "idFornecedor",
			value : $("#idFornecdorSelect", this.workspace).val()
		}, {
			name : "semanaRecolhimento",
			value : $("#semanaRecolhimentoBox", this.workspace).val()
		}, {
			name : "dataRecolhimento",
			value : $("#dataRecolhimentoBox", this.workspace).val()
		} ],
		newp : 1
	});
	$("#consultaInformeEncalheGrid", this.workspace).flexReload();
	
	this.imprimirParans.idFornecedor = $("#idFornecdorSelect", this.workspace).val();
	this.imprimirParans.semanaRecolhimento = $("#semanaRecolhimentoBox", this.workspace).val();
	this.imprimirParans.dataRecolhimento = $("#dataRecolhimentoBox", this.workspace).val();
};

InformeEncalhe.prototype.bindEvents = function() {
	var _this = this;

	$("#semanaRecolhimento", this.workspace).mask("99");
	$("#dataRecolhimentoBox", this.workspace).mask("99/99/9999");
	$("#dataRecolhimentoBox", this.workspace)
			.datepicker(
					{
						showOn : "button",
						buttonImage : contextPath
								+ "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
						buttonImageOnly : true
					});

	$("#btnPesquisar", this.workspace).click(function() {
		_this.busca();
		$(".grids", this.workspace).show();
	});
	$("#btnImprimir", this.workspace).click(function() {
		_this.$dialogImprimir.dialog('open');
	});
	$('#dataRecolhimentoBox,#semanaRecolhimentoBox', this.workspace).bind('keypress', function(e) {
		if(e.keyCode == 13) {
			_this.busca();
			$(".grids").show();
		}
	});
	$('#dataRecolhimentoBox,', this.workspace).change(function() {
		_this.carregarDiaSemana();
	});
	$('#semanaRecolhimentoBox', this.workspace).change(function() {
		_this.carregarDataSemana();
	});

};

InformeEncalhe.prototype.carregarDataSemana = function() {

	var _this = this;
	
	var numeroSemana = $("#semanaRecolhimentoBox", _this.workspace).val();

	if (!numeroSemana) {

		return;
	}
	
	var data = [{ name: 'numeroSemana', value: numeroSemana }];
	
	$.getJSON(
		contextPath + "/cadastro/distribuidor/obterDataDaSemana", 
		data,
		function(result) {

			if (result) {
				
				$("#dataRecolhimentoBox", _this.workspace).val(result);
			}
		});
};

InformeEncalhe.prototype.carregarDiaSemana = function() {
	
	if($("#sugerirSemana:checked").size() < 1)
		return;

	var _this = this;
	
	var dataPesquisa = $("#dataRecolhimentoBox", _this.workspace).val();

	if (!dataPesquisa) {

		return;
	}

	var data = [{ name: 'data', value: $("#dataRecolhimentoBox", _this.workspace).val() }];
	
	$.getJSON(
		contextPath + "/cadastro/distribuidor/obterNumeroSemana", 
		data,
		function(result) {

			if (result) {

				$("#semanaRecolhimentoBox", _this.workspace).val(result.int);
			}
		});
};

InformeEncalhe.prototype.initDialogImprimir = function() {
	
	var _this = this;
	$(tpObservacao).hide();
	this.$dialogImprimir = $( "#dialog-imprimir", this.workspace ).dialog({
		autoOpen : false,
		resizable: false,
		height:'auto',
		width:'auto',
		modal: true,
		buttons: {
			"Visualizar impressão": function() {
				
				$("input[name='idFornecedor']", this.workspace).val($("#idFornecdorSelect").val());
				$("input[name='semanaRecolhimento']", this.workspace).val($("#semanaRecolhimentoBox").val());
				$("input[name='dataRecolhimento']", this.workspace).val($("#dataRecolhimentoBox").val());
				$("input[name='somenteVisualizarImpressao']", this.workspace).val(true);
				$("input[name='tpObservacao']", this.workspace).val("#observacao").val();
				
				$( this ).dialog( "close" );
				$("#form-imprimir", this.workspace).attr("action", contextPath + '/devolucao/informeEncalhe/relatorioInformeEncalhe');
				$("#form-imprimir", this.workspace).attr("method", "POST");
				$("#form-imprimir", this.workspace).attr("target", "_blank");
				$("#form-imprimir", this.workspace).submit();

			},
			"Imprimir": function() {
				
				$("input[name='idFornecedor']", this.workspace).val($("#idFornecdorSelect").val());
				$("input[name='semanaRecolhimento']", this.workspace).val($("#semanaRecolhimentoBox").val());
				$("input[name='dataRecolhimento']", this.workspace).val($("#dataRecolhimentoBox").val());
				$("input[name='somenteVisualizarImpressao']", this.workspace).val(false);
				$("input[name='tpObservacao']", $('#tpObservacao', this.workspace).val());
				
				$( this ).dialog( "close" );
				$("#form-imprimir", this.workspace).attr("action", contextPath + '/devolucao/informeEncalhe/relatorioInformeEncalhe');
				$("#form-imprimir", this.workspace).attr("method", "POST");
				$("#form-imprimir", this.workspace).attr("target", "_blank");
				$("#form-imprimir", this.workspace).submit();

			},
			"Fechar": function() {
				$( this ).dialog( "close" );
			}
		},
		form: $("#dialog-imprimir", this.workspace).parents("form")
	});
};

InformeEncalhe.prototype.imprimir =  function(){
	var _this = this;	
	
	
//	 $('#form-imprimir').ajaxSubmit({        	
//	        success:function(responseText, statusText, xhr, $form)  { 
//	        	var mensagens = (responseText.mensagens)?responseText.mensagens:responseText.result;   
//	        	var tipoMensagem = mensagens.tipoMensagem;
//	    		var listaMensagens = mensagens.listaMensagens;
//
//	    		if (tipoMensagem && listaMensagens) {
//	    			exibirMensagemDialog(tipoMensagem, listaMensagens,"");
//	    		} 
//	    	} ,
//	        url:  _this.path + 'imprimir', 
//	        dataType:  'json',
//	        data: _this.imprimirParans,
//	        type:'POST'
//	        
//	    });
	
	
	
};

function CapaPopup(idProdutoEdicao) {

	var _this = this;
	this.statusEnum = {
		LOADING : {
			title : "Carregando...",
			img : contextPath + '/scripts/flexigrid-1.1/css/images/load.gif'
		},
		CAPA : {
			title : "Capa do produto",
			img : contextPath + '/images/ico_detalhes.png',
			events : [ {
				event : 'mouseover',
				handler : _this.openDialogCapa
			}, {
				event : 'mouseout',
				handler : _this.closeDialogCapa
			} ]
		},
		NO_CAPA : {
			title : "Este Produto não tem capa no momento, clique para incluir uma.",
			img : contextPath + '/images/ico_xml.gif',
			events : [ {
				event : 'click',
				handler : function(event) { 

					var isPermissaoAlteracao = $('#permissaoAlteracao',_this.workspace).val()=="true";
					
					if (isPermissaoAlteracao) {
						_this.openDialogUpload(event);
					} else {
						exibirAcessoNegado();
					}
				}
			} ]
		}
	};

	this.idProdutoEdicao = idProdutoEdicao;
	this.panel = document.createElement('div');

	this.panel.style.padding = "0px";
	this.anchor = document.createElement('a');
	this.anchor.href = "javascript:;";
	this.panel.appendChild(this.anchor);
	this.img = document.createElement('img');
	this.img.border = 0;
	this.img.height = 15;
	this.anchor.appendChild(this.img);
	this.changeStatus(this.statusEnum.LOADING);
	this.$dialogCapa = $('<div></div>', this.workspace).dialog({
		autoOpen : false,
		title : 'Visualizando Produto',
		resizable : false,
		height : 'auto',
		width : 'auto',
		modal : false,
		escondeHeader:false
	});

	this.loadCapa();
}

CapaPopup.prototype.changeStatus = function(newStatus) {
	$(this.img, this.workspace).unbind();
	var _this = this;
	var eventObj;
	this.anchor.title = newStatus.title;
	if (newStatus.events) {
		for ( var i in newStatus.events) {
			eventObj = newStatus.events[i];
			$(this.img, this.workspace).bind(eventObj.event, {
				CapaPopup : _this
			}, eventObj.handler);
		}
	}
	$(this.img, this.workspace).attr('src', newStatus.img);
};

CapaPopup.prototype.openDialogCapa = function(event) {
	event.data.CapaPopup.$dialogCapa.dialog('open');
};

CapaPopup.prototype.closeDialogCapa = function(event) {
	event.data.CapaPopup.$dialogCapa.dialog("close");
};

CapaPopup.prototype.loadCapa = function() {
	var _this = this;
	var img = null;
	img = $("<img />")
			.load(
					function() {						
						$(_this.$dialogCapa, this.workspace).append(img);
						_this.changeStatus(_this.statusEnum.CAPA);				
					}).error(function() {
				_this.changeStatus(_this.statusEnum.NO_CAPA);
			}).attr('src', contextPath + "/capa/" + this.idProdutoEdicao +"?" + Math.random())
			  .attr('height', '300px')
			  .attr('width', '250px');
};

CapaPopup.prototype.openDialogUpload = function(event) {
	var _this = event.data.CapaPopup;

	var $dialogUpload = $('<div></div>').dialog({
		autoOpen : true,
		title : 'Nova Imagem',
		resizable : false,
		height : 230,
		width : 480,
		modal : true,
		buttons : {
			"Fechar" : function() {
				$(this).dialog("close");
			}
		}
	});

	var uploadComponente = new CapaUpload(_this.idProdutoEdicao, function(
			result) {
		$dialogUpload.dialog('close');
		if (result.listaMensagens) {
			exibirMensagem(result.tipoMensagem,
					result.listaMensagens);

		} else {
			_this.changeStatus(_this.statusEnum.LOADING);
			_this.loadCapa();
		}

	});

	$dialogUpload.append(uploadComponente.panel);
	this.$dialogUpload = $dialogUpload;
};

function CapaUpload(idProdutoEdicao, callBack) {

	this.idProdutoEdicao = idProdutoEdicao;

	var inputFieldset = document.createElement("fieldset");
	inputFieldset.style.width = '430px';
	this.panel = (inputFieldset);
	
	var inputDiv = document.createElement('div');
	this.panel.appendChild(inputDiv);
	//this.panel = document.createElement('div');

	this.panel.innerHTML = "<legend>Nova Imagem</legend><p>Este Produto esta sem capa no momento, deseja incluir uma?</p><br /><p><strong>Selecione a imagem desejada:</strong></p><br />";

	var inputUpload = $('<input type="file" name="image"/>');	
	$(this.panel, this.workspace).append(inputUpload);

	var loadingPanel = document.createElement("div");
	loadingPanel.style.display = 'none';
	loadingPanel.innerHTML = '<strong>Enviando...</strong>';
	var msgProgress = '<strong>Enviando... {progress} %</strong>';
	this.panel.appendChild(loadingPanel);

	this.$fileUpload = $(inputUpload)
			.fileupload(
					{
						acceptFileTypes : /(\.|\/)(gif|jpe?g|png)$/i,
						url : contextPath + "/capa/salvaCapa",
						dataType : 'json',
						paramName : 'image',
						submit : function(e, data) {
							data.formData = {
								'idProdutoEdicao' : idProdutoEdicao
							};

						},
						done : function(e, data) {
							$(loadingPanel, this.workspace).hide();
							$(inputUpload, this.workspace).show();

							if (callBack) {
								callBack(data.result);
							}
						},
						progressall : function(e, data) {
							var progress = parseInt(data.loaded / data.total
									* 100, 10);
							loadingPanel.innerHTML = msgProgress.replace(
									'{progress}', progress);
						},
						send : function(e, data) {

							$(loadingPanel, this.workspace).show();
							$(inputUpload, this.workspace).hide();

						}

					});
}

function habilitarDesabilitar() {
	
	var obs = $("input[name='tipoImpressao.observacao']:checked").val();
	
	if (obs) {	
		$('#tpObservacao', this.workspace).wysiwyg({
	        initialContent: function() {return "<p><br></p>";},
	        controls: "bold,italic,underline,|,undo,redo"
		});
	} else {
		$(tpObservacao).hide()
	}
}

//@ sourceURL=informeEncalhe.js