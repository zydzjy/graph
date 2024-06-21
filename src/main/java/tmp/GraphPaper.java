package yuzhou.gits.graph;

import java.util.List;

public interface GraphPaper<C,Dim,B,G>{
	public void setBoundsManager(BoundsManager<Dim,B> boundsManager);
	public BoundsManager<Dim,B> getBoundsManager();
	public void onResized(Dim newDim);
	public  C getBackground();
	public void revalidate();
	public void addNewBoundsGraph(DefaultBoundsGraph<C,B,G> boundsGraph);
	public Bounds<B> getBounds(B boundsId);
	public DefaultBoundsGraph<C,B,G> getBoundsGraph(B boundsId);
	public Dim getDim();
	public interface BoundsGraphRevalidatedListener<C,  B, G> {
		public void onRevalidated(List<DefaultBoundsGraph<C, B, G>> dirtyBounds);
	}
}