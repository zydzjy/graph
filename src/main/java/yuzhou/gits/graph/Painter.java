package yuzhou.gits.graph;

import java.util.List;

public interface Painter<C,G,B> {

	public void paint(G g,List<DefaultBoundsGraph<C,B,G>> dirtyBounds);
}
