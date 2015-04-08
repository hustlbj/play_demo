package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;

import java.util.*;

import org.codehaus.groovy.control.customizers.SecureASTCustomizer;

@With(Secure.class)
public class Application extends Controller {

    public static void index() {
    	String user = Security.connected();
    	//默认渲染index.html模板
        render(user);
    }
    public static void sayHello(@Required String myName) {
    	//默认对sayHello模板进行渲染，所以转向sayHello.html
    	if (validation.hasErrors()) {
			flash.error("Oops, please enter your name!");
			//重定向到index
			index();
		}
    	render(myName);
    }
}