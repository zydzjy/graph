package yuzhou.gits.graph;

import java.util.List;

public interface BoundsManager<Dim,B> {

	public List<B> layout(Dim paperDim, GraphPaper<?, Dim,B, ?> graphPaper);
}
