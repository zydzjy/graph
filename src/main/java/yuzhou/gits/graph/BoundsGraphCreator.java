package yuzhou.gits.graph;

import java.util.List;


public interface BoundsGraphCreator<B, C, G> {
	public GraphObjectList<GraphShape> getShapes();
	public GraphObjectList<Object> getImages();
	public GraphObjectList<SimpleGraphText> getTexts();
	
	public void preparePaprameter(DefaultBoundsGraph<C, B, G> boundsGraph);
	public boolean calculateGraphs(DefaultBoundsGraph<C,B,G> boundsGraph);
	public default void cleanup() {}
}
