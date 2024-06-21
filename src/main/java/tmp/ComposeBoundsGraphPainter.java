package yuzhou.gits.graph;

import java.util.function.Function;

public class ComposeBoundsGraphPainter<C, G, B> implements BoundsGraphPainter<C, G, B> {
	private int currentIdx = 0;
	Function<DefaultBoundsGraph<C,B,G>, Integer> chooser;
	BoundsGraphPainter<C, G, B>[] subPainters;
	public ComposeBoundsGraphPainter(BoundsGraphPainter<C, G, B>[] subPainters,Function<DefaultBoundsGraph<C,B,G>, Integer> chooser) {
		this.subPainters = subPainters;
		this.chooser = chooser;
	}
	public void choose(DefaultBoundsGraph<C,B,G> boundsGraph) {
		this.currentIdx = chooser.apply(boundsGraph);
	}
	
	@Override
	public BoundsGraphDrawer<C, G, B> getDrawer() {
		return this.subPainters[this.currentIdx].getDrawer();
	}

	@Override
	public BoundsGraphCreator<B, C, G> getCreator() {
		return this.subPainters[this.currentIdx].getCreator();
	}
}
