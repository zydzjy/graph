package yuzhou.gits.graph;

import java.util.Map;
import java.util.HashMap;

public class GraphParametersContext {
	private Map<String,Object> parameters = new HashMap<String,Object>();
	public void put(String key,Object value) {
		parameters.put(key, value);
	}
	public Object get(String key) {
		return parameters.get(key);
	}
}
