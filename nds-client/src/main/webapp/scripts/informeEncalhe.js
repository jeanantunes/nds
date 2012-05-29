/**
 * 
 */

function InformeEncalhe() {
	this.initGrid();
	this.bindEvents();

};
InformeEncalhe.prototype.path = contextPath + "/devolucao/informeEncalhe/";

InformeEncalhe.prototype.initGrid = function() {

	var _this = this;
	$("#consultaInformeEncalheGrid").flexigrid(
			{
				preProcess : function(data) {
					if (typeof data.mensagens == "object") {

						exibirMensagem(data.mensagens.tipoMensagem,
								data.mensagens.listaMensagens);

					} else {
						$.each(data.rows, function(index, value) {
							var acao = "";
							value.cell.acao = acao;
							if (!value.cell.chamadaCapa) {
								value.cell.chamadaCapa = '';
							}
						});
						return data;
					}
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
					width : 40,
					sortable : true,
					align : 'left'
				}, {

					display : 'Produto',
					name : 'nomeProduto',
					width : 80,
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
					width : 105,
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
					name : 'precoVenda',
					width : 90,
					sortable : true,
					align : 'right'
				}, {

					display : 'Preço Desconto R$',
					name : 'precoDesconto',
					width : 90,
					sortable : true,
					align : 'right'
				}, {

					display : 'Data de Lançamento',
					name : 'dataLancamento',
					width : 110,
					sortable : true,
					align : 'center'
				}, {
					display : 'Data Recolhimento',
					name : 'dataRecolhimento',
					width : 90,
					sortable : true,
					align : 'left'
				}, {

					display : 'Ação',
					name : 'idProdutoEdicao',
					width : 30,
					sortable : false,
					align : 'center',
					process : _this.processAcao
				} ],
				sortname : "nomeProduto",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 180
			});

	$("#consultaInformeEncalheGrid").flexOptions({
		"url" : this.path + 'busca.json',
		params : [ {
			name : "idFornecedor",
			value : $("#idFornecdorSelect").val()
		}, {
			name : "semanaRecolhimento",
			value : $("#semanaRecolhimentoBox").val()
		}, {
			name : "dataRecolhimento",
			value : $("#dataRecolhimentoBox").val()
		} ]
	});

};

InformeEncalhe.prototype.processAcao = function(tdDiv, pid) {
	var capaPopup = new CapaPopup(tdDiv.innerHTML);
	$(tdDiv).empty();
	tdDiv.appendChild(capaPopup.panel);
};

InformeEncalhe.prototype.busca = function() {
	$("#consultaInformeEncalheGrid").flexOptions({
		"url" : this.path + 'busca.json',
		params : [ {
			name : "idFornecedor",
			value : $("#idFornecdorSelect").val()
		}, {
			name : "semanaRecolhimento",
			value : $("#semanaRecolhimentoBox").val()
		}, {
			name : "dataRecolhimento",
			value : $("#dataRecolhimentoBox").val()
		} ],
		newp : 1
	});
	$("#consultaInformeEncalheGrid").flexReload();
};

InformeEncalhe.prototype.bindEvents = function() {
	var _this = this;

	$("#semanaRecolhimento").mask("99");
	$("#dataRecolhimentoBox")
			.datepicker(
					{
						showOn : "button",
						buttonImage : contextPath
								+ "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
						buttonImageOnly : true
					});

	$("#btnPesquisar").click(function() {
		_this.busca();
		$(".grids").show();
	});
	$("#btnImprimir").click(function() {
		alert("TODO: função não implementada");
	});
	$('#dataRecolhimentoBox,#semanaRecolhimentoBox').bind('keypress', function(e) {
		if(e.keyCode == 13) {
			_this.busca();
			$(".grids").show();
		}
	});

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
				handler : _this.openDialogUpload
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
	this.$dialogCapa = $('<div></div>').dialog({
		autoOpen : false,
		title : 'Visualizando Produto',
		resizable : false,
		height : 'auto',
		width : 'auto',
		modal : false

	});

	this.loadCapa();
}

CapaPopup.prototype.changeStatus = function(newStatus) {
	$(this.anchor).unbind();
	var _this = this;
	var eventObj;
	this.anchor.title = newStatus.title;
	if (newStatus.events) {
		for ( var i in newStatus.events) {
			eventObj = newStatus.events[i];
			$(this.img).bind(eventObj.event, {
				CapaPopup : _this
			}, eventObj.handler);
		}
	}
	$(this.img).attr('src', newStatus.img);
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
						$(_this.$dialogCapa).append(img);
						_this.changeStatus(_this.statusEnum.CAPA);				
					}).error(function() {
				_this.changeStatus(_this.statusEnum.NO_CAPA);
			}).attr('src', contextPath + "/capa/" + this.idProdutoEdicao +"?" + Math.random());
};

CapaPopup.prototype.openDialogUpload = function(event) {
	var _this = event.data.CapaPopup;

	var $dialogUpload = $('<div></div>').dialog({
		autoOpen : true,
		title : 'Nova Imagem',
		resizable : false,
		height : 260,
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
		if (result.mensagens) {
			exibirMensagem(result.mensagens.tipoMensagem,
					result.mensagens.listaMensagens);

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
	this.panel = document.createElement('div');
	this.panel.innerHTML = "<p>Este Produto esta sem capa no momento, deseja incluir uma?</p><p><strong>Selecione a imagem desejada:</strong></p>";

	var inputUpload = $('<input type="file" name="image"/>');	
	$(this.panel).append(inputUpload);

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
							$(loadingPanel).hide();
							$(inputUpload).show();

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

							$(loadingPanel).show();
							$(inputUpload).hide();

						}

					});
}
