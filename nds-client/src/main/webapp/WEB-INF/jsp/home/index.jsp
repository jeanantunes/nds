<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>

<link href="css/menu_superior.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/novo_distrib.css" />
<link rel="stylesheet" type="text/css" href="css/NDS.css" />
<link rel="stylesheet" href="scripts/jquery-ui-1.8.16.custom/development-bundle/themes/redmond/jquery.ui.all.css" />
<link rel="stylesheet" type="text/css" href="scripts/jquery-ui-1.8.16.custom/development-bundle/themes/redmond/jquery.ui.theme.css"/>
<link rel="stylesheet" type="text/css" href="scripts/flexigrid-1.1/css/flexigrid.pack.css" />
<link rel="stylesheet" type="text/css" href="scripts/tooltip/tipsy.css" />
<link rel="stylesheet" type="text/css" href="scripts/tooltip/tipsy-docs.css" />

<script language="javascript" type="text/javascript" src="scripts/jquery-ui-1.8.16.custom/js/jquery-1.7.1.min.js"></script>
<script language="javascript" type="text/javascript" src="scripts/jquery-ui-1.8.16.custom/js/jquery-ui-1.8.16.custom.min.js"></script>
<script language="javascript" type="text/javascript" src="scripts/NDS.js"></script>
<script language="javascript" type="text/javascript" src="scripts/utils.js"></script>
<script language="javascript" type="text/javascript" src="scripts/flexigrid-1.1/js/flexigrid.pack.js"></script>
<script language="javascript" type="text/javascript" src="scripts/tooltip/jquery.tipsy.js"></script>

<script language="javascript" type="text/javascript" src="scripts/jquery.json-2.3.min.js"></script>
<script language="javascript" type="text/javascript" src="scripts/flexigrid-1.1/js/flexigrid.js"></script>
<script language="javascript" type="text/javascript" src="scripts/jquery.ui.datepicker-pt-BR.js"></script>
<script language="javascript" type="text/javascript" src="scripts/jquery.maskmoney.js"></script>
<script language="javascript" type="text/javascript" src="scripts/jquery.maskedinput.js"></script>
<script language="javascript" type="text/javascript" src="scripts/jquery.justLetter.js"></script>
<script language="javascript" type="text/javascript" src="scripts/jquery.interval.js"></script>

<script type="text/javascript" src="scripts/tools-1.2.6/js/jquery.tools.min.js"></script>
<script type="text/javascript" src="scripts/jquery.formatCurrency-1.4.0.min.js"></script>
<script type="text/javascript" src="scripts/jquery.calculation.min.js"></script> 		
<script type="text/javascript" src="scripts/jquery.dateFormat-1.0.js"></script>

<script type="text/javascript" src="scripts/jshashtable-2.1.js"></script>
<script type="text/javascript" src="scripts/jquery.numberformatter-1.2.3.js"></script>

<script type="text/javascript" src="scripts/commonsbehaviour.js"></script>

<link rel="stylesheet" type="text/css" href="scripts/tools-1.2.6/css/tools.css" />

<base href="<c:url value="/"/>" />

<script type="text/javascript">

	(function($) {
		$.fn
				.extend(
						$.ui.dialog.prototype,
						{
							_original_init : $.ui.dialog.prototype._init,
							_original_open : $.ui.dialog.prototype.open,				
							_original_close : $.ui.dialog.prototype.close,	
						    						
							_init : function() {
								var self = this.element;
	
								var diaOpt = {
									form: this.options.form,
									escondeHeader:this.options.escondeHeader,	
								};
	
								$.fn.extend(true, this.options, diaOpt);
								this._original_init();
	
								
								//this.addForm();
							},
							open : function() {
								var self = this.element, o = this.options;
								self.parent().appendTo(o.form);
								//self.parent().css("top", "58px");
								if(o.escondeHeader || o.escondeHeader == undefined){
									escondeHeader();
									redimensionarWorkspace();
								}
								this._original_open();
							},
							close : function() {
								if(this.options.escondeHeader || this.options.escondeHeader == undefined){
									mostraHeader();
									redimensionarWorkspace();
								}
								this._original_close();
							}
							
						});
		
	})(jQuery);

	(function($) {
		$.fn
				.extend(
						$.ui.tabs.prototype,
						{
							_original_init : $.ui.tabs.prototype._init,
							_init : function() {
								var self = this.element;

								this._original_init();
								this.element
										.children('.ui-tabs-nav')
										.after(
												'<div class="ui-tabs-strip-spacer"></div>');

								var tabOpt = {
									tabTemplate : "<li> <a href='#\{href\}'>#\{label\}</a> <span class='ui-icon ui-icon-close'>Remove Tab</span></li>",
									add : function(event, ui) {
										self.tabs('select', '#' + ui.panel.id);
										//console.log($('#'+ui.panel.id) );
									},
									ajaxOptions : {
										error : function(xhr, status, index,
												anchor) {
											$(anchor.hash)
													.html(
															"pagina não encontrada :</br> Mensagem de Erro: </br>xhr["
																	+ JSON.stringify(xhr)
																	+ "] </br>status["
																	+ xhr.status
																	+ "] </br>index ["
																	+ index
																	+ "] </br>anchor ["
																	+ anchor);
										},
										complete : function(xhr, status, index,
												anchor) {
											if( $('#logout_true').length ) {
												// Logout por fim ou perda da sessão
												logout();
											};
											focarPrimeiroElemento();
										}
									},
									cache : true
								};

								$.fn.extend(this.options, tabOpt);
								this.addCloseTab();
							},
							addTab : function(title, url, className) {
								var self = this.element, o = this.options, add = true;
								
								var ulTabs = $("ul.ui-tabs-nav", self)[0];
								
								$("li", ulTabs).each(function() {
									if ($("a", this).html() == title) {
										var index = $("li", self).index(this);
										self.tabs('select', index);
										add = false;
									}
								});
								if (add) {									
									tab = self.tabs('add', url, title);									
									$span = $("<span>").addClass(className);
									$('a:contains(' + title + ')', ulTabs).parent().prepend($span);
									
								}
							},
							addCloseTab : function() {
								var self = this.element, o = this.options;
								$("span.ui-icon-close", self).live(
										'click',
										function() {
											var index = $("li", $(self)).index(
													$(this).parent());
											if (index > -1)
												$(self).tabs("remove", index);
										});
							}
						});
		$.fn.serializeObject = function(){
		    var o = {};
		    var a = this.serializeArray();
		    $.each(a, function() {
		        if (o[this.name] !== undefined) {
		            if (!o[this.name].push) {
		                o[this.name] = [o[this.name]];
		            }
		            o[this.name].push(this.value || '');
		        } else {
		            o[this.name] = this.value || '';
		        }
		    });
		    return o;
		};
	})(jQuery);

	$(function() {

		$('#workspace').tabs();

		// Dinamicaly add tabs from menu
		$("#menu_principal ul li ul li").click(function() {
			//S2
			$('#workspace').tabs('addTab', 										
					$("a", this).html()
					, $("a", this).prop("href") + "?random=" + Math.random()
					, $("span", $(this).parents("li")).attr('class')
			);
			return false;
		});

		$('#linkHome').click(function() {
			$('#workspace').tabs('addTab', $('#linkHome').html(),
					$('#linkHome').prop("href"));
			return false;
		});

		$('#linkHome').click();
	});
	
	$(document).ready(function() {
		
		$("#ajaxLoading").ajaxStart(function() {
			$(this).fadeIn(200);
		});
		$("#ajaxLoading").ajaxStop(function() {
			$(this).fadeOut(200);
			redimensionarWorkspace();
		});
		
		redimensionarWorkspace();

		window.addEventListener('blur', function() {
			
			$().clearAllInterval();
		});		
		
	});
	
	var pressedCtrl = false; 
	var pressedAlt = false; 
	var pressedShift = false;
	$(document).keyup(function (e) {
		if(e.which == 17)
			pressedCtrl=false; 
		
		if (e.which == 18)
			pressedAlt=false;
		
		if (e.shiftKey==1)
			pressedShift = false;
	})

	$(document).keydown(function (e) {
		if(e.which == 17) 
			pressedCtrl = true; 

		if (e.shiftKey==1)
			pressedShift = true;

		if (e.which==18)
			pressedAlt=true;

		if(e.which == 38 && pressedCtrl == true) { 
			//Aqui vai o cÃ³digo e chamadas de funÃ§Ãµes para o ctrl+s
			escondeHeader();
		}
		else if(e.which == 40 && pressedCtrl == true) { 
			//Aqui vai o cÃ³digo e chamadas de funÃ§Ãµes para o ctrl+s
			mostraHeader();
		} else if (e.which == 69 && pressedShift == true) {
			// CÃ³digo para shift+e (XLS)
			if ($('#bt_xls_excell').length != 0) {
				$('#bt_xls_excell')[0].click();
			}
			
		} else if (e.which == 73 && pressedShift == true) {
			// CÃ³digo para shift+i (Imprimir PDF)
			if ($('#bt_imprimir_pdf').length != 0) {
				$('#bt_imprimir_pdf')[0].click();
			}
			
		} else if(e.which == 13) {
			// CÃ³digo para Enter (Pesquisas e Novo)
			if ($('#bt_pesquisar_cadastrar').length != 0) {
				$('#bt_pesquisar_cadastrar')[0].click();
			} else if ($('#btnPesquisar').length != 0) {
				$('#btnPesquisar')[0].click();
			}
		}
	});	
	
	var contextPath = "${pageContext.request.contextPath}";
	
	//Changelog
	
	$(document).ready(function() {		
		$("#changes").dialog({
			resizable : true,
			height : 400,
			width : 800
		});
		$("#changes").dialog("close");
		
		$("#btnVersao").click(function() {
			$("#changes")
			.dialog( "option" ,  "title", "Changelog" )
			.dialog( "open" );
		});
		
		
	});
	
	
	
</script>

<script
	src="scripts/informeEncalhe.js"
	type="text/javascript"></script>
	
	

<style>
#ajaxLoading {
	position: absolute;	z-index: 99999;
	left: 0px; top: 0px; width: 100%; height: 100%; margin: 0;
}
#ajaxLoading #shadow {
	background: #D0D0D0;
	position: fixed; z-index: 1;
	filter: alpha(opacity=50); opacity: 0.5;
	left: 0px; top: 0px; width: 100%; height: 100%; margin: 0;
}
#ajaxLoading #panel {
	position: fixed; z-index: 2;
	top: 50%; left: 50%; width: 100px; height: 100px;
	margin-top: -50px; margin-left: -50px;
	text-align: center; vertical-align: 50%;
}
.ui-tabs .ui-tabs-panel {
    padding: 0px;
}
#btnVersao{font-size:10px; font-weight:normal;margin-left:5px; text-decoration:none!important;}
</style>
</head>
<body onresize="redimensionarWorkspace();">

	<div class="corpo" id="divCorpo">
		<div class="header">
			<div class="bts_header">
				<span class="bt_novos" style="display:none;">
					<a id="linkHome" href='<c:url value="/inicial/"/>' rel="tipsy" title="Voltar para Home"><span class="classROLE_HOME">&nbsp;</span>&nbsp;</a>
				</span>
				<div class="logo">&nbsp;</div>
				<a href="javascript:;" id="btnVersao">
						<label title="versao">Versão: ${versao}</label>								
					</a>
				<div class="usuario">
					
											
					<label title="Usuário Logado no Sistema">Usuário: ${nomeUsuario}</label>
								
					<label> <script type="text/javascript"
							language="JavaScript">
						diaSemana();
					</script> </label>
				
					<label>
						<a href="javascript:;" onclick="logout()" title="Sair do Sistema">Sair</a>
					</label>
	
				</div>
			</div>
			<div class="sub-header">
				<div id="menu_principal" style="float:left!important;">
					<ul>
						<c:forEach items="${menus}" var="menu">
							<li><a href="javascript:;" class="trigger"><span
								class="class${menu.key.permissao}">&nbsp;</span>${menu.key.permissao.descricao}</a>
								<ul>
									<c:forEach items="${menus[menu.key]}" var="submenu">
										<li><a href="<c:url value='${submenu.key.url}' />">${submenu.key.permissao.descricao}</a>
										</li>
									</c:forEach>						
								</ul>
							</li>
						</c:forEach>
						<li>
							<a href="help.htm"><span class="classROLE_HELP">&nbsp;</span>Help</a>
						</li>
					</ul>
					</div>
					<br clear="all"/>
			</div>
		</div>
		<jsp:include page="/WEB-INF/jsp/commons/loading.jsp" />
		<div id="changes" title="Changelog"><div style="padding: 10px">${changes}</div></div>
		
		<div id="workspace">
			<ul></ul>
		</div>

	</div>

	<div class="container">
		<div id="notify" style="display: none;"></div>
		<div id="effectSuccess" class="ui-state-default ui-corner-all" style="display: none; position: absolute; width: auto; z-index: 10002;">
			<p>
				<span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b id="idTextSuccess"></b>
				<span class="ui-state-default ui-corner-all" style="float:right; margin-right: 5px; margin-top: 5px;">
					<a href="javascript:;" onclick="esconde(false, $(this).closest('div'));" class="ui-icon ui-icon-close">&nbsp;</a>
				</span>					
			</p>
		</div>
		<div id="effectWarning" class="ui-state-highlight ui-corner-all" style="display: none; position: absolute; width: auto; z-index: 10002;">
			<p>
				<span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b id="idTextWarning"></b>
				<span class="ui-state-default ui-corner-all" style="float:right; margin-right: 5px; margin-top: 5px;">
					<a href="javascript:;" onclick="esconde(false, $(this).closest('div'));" class="ui-icon ui-icon-close">&nbsp;</a>
				</span>					
			</p>
		</div>
		<div id="effectError" class="ui-state-error ui-corner-all" style="display: none; position: absolute; width: auto; z-index: 10002;">
			<p>
				<span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b id="idTextError"></b>
				<span class="ui-state-default ui-corner-all" style="float:right; margin-right: 5px; margin-top: 5px;">
					<a href="javascript:;" onclick="esconde(false, $(this).closest('div'));" class="ui-icon ui-icon-close">&nbsp;</a>
				</span>					
			</p>
		</div>
	</div>			

</body>
</html>