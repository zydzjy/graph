package yuzhou.gits.graph;

public interface BoundsGraphPainter<C,G,B> {

	public BoundsGraphDrawer<C,G,B> getDrawer();
	public BoundsGraphCreator<B,C,G> getCreator();
}
