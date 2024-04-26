package yuzhou.gits.graph;

import yuzhou.gits.graph.GraphPaper.BoundsGraphRevalidatedListener;

public interface CommonPainterTarget<C,B,G> extends BoundsGraphRevalidatedListener<C,  B, G> {
	public void paint(G g);
}