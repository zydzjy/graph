package yuzhou.gits.graph;

import java.util.ArrayList;
import java.util.List;

public class BoundsGraphProperties<C> {
	public BoundsGraphProperties(C background) {
		this.background = background;
	}
	protected C background;
	public C getBackground() {
		return background;
	}
	public void setBackground(C background) {
		this.background = background;
		this.firePropsChangedEvent(new PropertiesChangedEvent(this,"background"));
	}
	public static class PropertiesChangedEvent<C> {
		BoundsGraphProperties<C> src;
		String[] propsNames;
		public PropertiesChangedEvent(BoundsGraphProperties src,String...propsNames){
			this.src = src;
			this.propsNames = propsNames;
		}
		public BoundsGraphProperties getSrc() {
			return src;
		}
		public void setSrc(BoundsGraphProperties src) {
			this.src = src;
		}
		public String[] getPropsNames() {
			return propsNames;
		}
		public void setPropsName(String[] propsNames) {
			this.propsNames = propsNames;
		}
	}
	public interface PropertiesChangedEventListener<C> {
		public void propsChanged(PropertiesChangedEvent<C> event);
	}
	List<PropertiesChangedEventListener> listeners = new ArrayList<>();
	public void addListener(PropertiesChangedEventListener l) {
		this.listeners.add(l);
	}
	protected void firePropsChangedEvent(PropertiesChangedEvent event) {
		for(PropertiesChangedEventListener l:this.listeners) {
			l.propsChanged(event);
		}
	}
}
