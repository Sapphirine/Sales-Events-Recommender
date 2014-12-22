package servlet;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.Application;
public class App extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(retailer.class);
		classes.add(account.class);
		return classes;
	}
}
