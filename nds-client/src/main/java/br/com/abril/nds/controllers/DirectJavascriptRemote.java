package br.com.abril.nds.controllers;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import net.vidageek.mirror.dsl.Mirror;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.http.route.Route;
import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.view.Results;

import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;

@Resource
@Path("/djr")
public final class DirectJavascriptRemote {

	@Autowired
	private final Router router;

	@Autowired
	private final Result result;

	public DirectJavascriptRemote(Router router, Result result) {
		this.router = router;
		this.result = result;
	}

	final String controllerTemplate = "function #{controller_name}(){DJR.call(this);}; #{controller_name}.prototype.routes = {};";
	final String actionsTemplate    = "\n #{controller_name}.prototype.routes['#{action}'] = {url: \"#{uri}\", method: \"#{method}\" };";


	@SuppressWarnings({ "unchecked"})
	private Set<String> filterControllerNames(List<Route> routes) {

		Collection<String> names = CollectionUtils.collect(routes, new Transformer(){
			@Override
			public Object transform(Object obj) {
				Route route = (Route) obj;
				ResourceMethod resourceMethod = (ResourceMethod) new Mirror().on(route).get().field("resourceMethod");
				return  resourceMethod.getMethod().getDeclaringClass().getSimpleName();
			}});

		Comparator<String> comparator = new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };

        Multimap<String, String> groups = TreeMultimap.create(comparator, Ordering.arbitrary());
        for(String controllerName : names) {
            groups.put(controllerName, controllerName);
        }

        return groups.asMap().keySet();

	}


	@Get
	@Path("/")
	public void getControllers() {

		StringBuilder builder = new StringBuilder();
		String routesText = "";
		List<Route> routes = router.allRoutes();

		Set<String> controllerNames = filterControllerNames(routes);
		for(String name : controllerNames) {
			routesText = StringUtils.replace(controllerTemplate, "#{controller_name}", name);
			builder.append(routesText).append("\n");
		}
		for (Route route : routes) {
			String verb = route.allowedMethods().toArray()[0].toString();
			String uri = route.getOriginalUri();
			ResourceMethod resourceMethod = (ResourceMethod) new Mirror().on(route).get().field("resourceMethod");
			String controllerName = resourceMethod.getMethod().getDeclaringClass().getSimpleName();
			String action = resourceMethod.getMethod().getName();
			routesText = StringUtils.replace(actionsTemplate, "#{controller_name}", controllerName);
			routesText = StringUtils.replace(routesText, "#{action}", action);
			routesText = StringUtils.replace(routesText, "#{uri}", uri);
			routesText = StringUtils.replace(routesText, "#{method}", verb);
			builder.append(routesText).append("\n");
		}
		result.use(Results.http()).body(builder.toString());
	}

}