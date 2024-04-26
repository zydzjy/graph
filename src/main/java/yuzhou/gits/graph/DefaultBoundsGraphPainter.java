package yuzhou.gits.graph;

public class DefaultBoundsGraphPainter<C, G, B> implements BoundsGraphPainter<C, G, B> {
	private BoundsGraphDrawer<C, G, B> drawer;
	private BoundsGraphCreator<B, C, G> creator;
	@Override
	public BoundsGraphDrawer<C, G, B> getDrawer() {
		return this.drawer;
	}

	@Override
	public BoundsGraphCreator<B, C, G> getCreator() {
		return this.creator;
	}
	public DefaultBoundsGraphPainter(BoundsGraphDrawer<C, G, B> drawer, BoundsGraphCreator<B, C, G> creator) {
		super();
		this.drawer = drawer;
		this.creator = creator;
	}
}
