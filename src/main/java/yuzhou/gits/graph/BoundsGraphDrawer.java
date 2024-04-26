package yuzhou.gits.graph;

import java.util.List;

public interface BoundsGraphDrawer<C,G,B> {
	public default void fillShapes(G g2d, List<GraphShape> shapes) {
		for (GraphShape s : shapes) {
			this.fillShape(g2d,s);
		}
	}
	public default void drawShapes(G g2d, List<GraphShape> shapes) {
		for (GraphShape s : shapes) {
			this.drawShape(g2d,s);
		}
	}
	public default void fillTexts(G g2d, List<SimpleGraphText> texts) { 
		for (SimpleGraphText s : texts) {
			this.fillText(g2d,s);
		}
	}
	
	public void fillText(G g2d, SimpleGraphText s);
	public void fillShape(G g,GraphShape shape);
	public void drawShape(G g,GraphShape shape);
	public void draw(G g2d, DefaultBoundsGraph<C,B,G> graph, BoundsGraphCreator<B,C,G> creator);
}
