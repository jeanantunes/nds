<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>

<link href="css/menu_superior.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/novo_distrib.css" />
<link rel="stylesheet" type="text/css" href="css/NDS.css" />
<link rel="stylesheet"
	href="scripts/jquery-ui-1.8.16.custom/development-bundle/themes/redmond/jquery.ui.all.css" />
<link rel="stylesheet" type="text/css"
	href="scripts/flexigrid-1.1/css/flexigrid.pack.css" />

<script language="javascript" type="text/javascript"
	src="scripts/jquery-ui-1.8.16.custom/development-bundle/jquery-1.6.2.js"></script>
<script language="javascript" type="text/javascript"
	src="scripts/jquery-ui-1.8.16.custom/js/jquery-ui-1.8.16.custom.min.js"></script>
<script language="javascript" type="text/javascript"
	src="scripts/NDS.js"></script>
<script language="javascript" type="text/javascript"
	src="scripts/flexigrid-1.1/js/flexigrid.pack.js"></script>

<base href="<c:url value="/"/>" />

<script type="text/javascript">
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
									tabTemplate : "<li><a href='#\{href\}'>#\{label\}</a> <span class='ui-icon ui-icon-close'>Remove Tab</span></li>",
									add : function(event, ui) {
										self.tabs('select', '#' + ui.panel.id);
									},
									ajaxOptions : {
										error : function(xhr, status, index,
												anchor) {
											$(anchor.hash)
													.html(
															"pagina não encontrada :</br> Mensagem de Erro: </br>xhr["
																	+ JSON
																			.stringify(xhr)
																	+ "] </br>status["
																	+ xhr.status
																	+ "] </br>index ["
																	+ index
																	+ "] </br>anchor ["
																	+ anchor);
										}
									},
									cache : true
								};

								$.fn.extend(this.options, tabOpt);
								this.addCloseTab();
							},
							addTab : function(title, url) {
								var self = this.element, o = this.options, add = true;
								$("li", self).each(function() {
									if ($("a", this).html() == title) {
										var index = $("li", self).index(this);
										self.tabs('select', index);
										add = false;
									}
								});
								if (add) {
									self.tabs('add', url, title);
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
	})(jQuery);

	$(function() {

		$('#workspace').tabs();

		// Dinamicaly add tabs from menu
		$("#menu_principal ul li ul li").click(
				function() {
					$('#workspace').tabs('addTab', $("a", this).html(),
							$("a", this).prop("href"));
					return false;
				});

	});
</script>

<style>
#workspace {
	margin-top: 0px;
}
</style>
</head>
<body>

	<div class="corpo">
		<div class="header">
			<div class="sub-header">
				<div class="logo">&nbsp;</div>

				<div class="titAplicacao">
					<h1>Treelog S/A. Logística e Distribuição - SP</h1>
					<h2>CNPJ: 00.000.000/00001-00</h2>
					<h3>Distrib vs.1</h3>
				</div>

				<div class="usuario">
					<div class="bt_novos">

						<label title="Usuário Logado no Sistema">Usuário: Junior
							Fonseca</label>
					</div>
					<div class="bt_novos">
						<label> <script type="text/javascript"
								language="JavaScript">
							diaSemana();
						</script> </label>
					</div>
					<div class="bt_novos">
						<a href="javascript:;" title="Sair do Sistema" class="sair">Sair</a>
					</div>

				</div>
			</div>
		</div>
		<div id="menu_principal">
			<ul>
				<li><a href="index.htm"><span class="classHome">&nbsp;</span>Home</a>
				</li>
					<c:forEach items="${menus}" var="menu">
						<li><a href="javascript:;" class="trigger"><span
							class="${menu.key.permissao.classeExibicao}">&nbsp;</span>${menu.key.permissao.descricao}</a>
							<ul>
								<c:forEach items="${menus[menu.key]}" var="submenu">
									<li><a href="<c:url value='${submenu.key.url}' />">${submenu.key.permissao.descricao}</a>
									</li>
								</c:forEach>						
							</ul>
						</li>
					</c:forEach>
				<li><a href="help.htm"><span class="classHelp">&nbsp;</span>Help</a>
				</li>
			</ul>
			<br class="clearit">
		</div>


		<div id="workspace">
			<ul></ul>
		</div>

	</div>

</body>
</html>