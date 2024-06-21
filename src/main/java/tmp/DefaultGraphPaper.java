package yuzhou.gits.graph;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import yuzhou.gits.graph.DefaultBoundsGraph.BoundsGraphChangedEvent;
import yuzhou.gits.graph.DefaultBoundsGraph.BoundsGraphChangedEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DefaultGraphPaper<C, Dim, B, G> implements GraphPaper<C, Dim, B, G>, BoundsGraphChangedEventListener<C,B,G> {
	BoundsManager<Dim, B> boundsManager;
	Map<B, DefaultBoundsGraph<C, B, G>> boundsGraphs = new HashMap<>();
	DefaultBoundsGraph<C,B,G> root = new DefaultBoundsGraph<C,B,G>();
	private C background;
	Dim dim;
	private GraphParametersContext context = new GraphParametersContext();
	protected List<BoundsGraphRevalidatedListener<C, B, G>> revalidatedListeners = new ArrayList<>();
	public DefaultGraphPaper(C background) {
		this.background = background;
	}
	public GraphParametersContext getContext() {
		return context;
	}

	public void addBoundsGraphRevalidatedListener(BoundsGraphRevalidatedListener<C,B, G> l) {
		this.revalidatedListeners.add(l);
	}

	@Override
	public void onResized(Dim newDim) {
		this.dim = newDim;
		
		this.revalidate();
	}

	@Override
	public C getBackground() {
		return this.background;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void revalidate() {
			this.dirtyBoundsGraphs.clear();
			List<B> dirtyBoundsList = this.boundsManager.layout(this.dim, this);
			if(dirtyBoundsList == null) {
				return;
			}
			// TODO recalculate all bounds dimension and recalculate all bounds
			// graphs,repaint all bounds
			for (B dirtyBounds : dirtyBoundsList) {
				DefaultBoundsGraph<C, B, G> boundsGraph = this.boundsGraphs.get(dirtyBounds);
				BoundsGraphPainter<C,G,B> painter = boundsGraph.getBoundsGraphPainter();
				if(painter != null) {
					if(painter instanceof ComposeBoundsGraphPainter) {
						((ComposeBoundsGraphPainter<C,G,B>)painter).choose(boundsGraph);
					}
					BoundsGraphCreator<B, C,G> creator = painter.getCreator();
					if (creator != null) {
						if(!creator.calculateGraphs(boundsGraph)) {
								//continue;
						}
					}
				}
				if(boundsGraph.isVisible()) {
					dirtyBoundsGraphs.add(boundsGraph);
				}
			}
			for (BoundsGraphRevalidatedListener<C,  B, G> reavalidatedListeners : this.revalidatedListeners) {
				reavalidatedListeners.onRevalidated(dirtyBoundsGraphs);
			}
			dirtyBoundsGraphs.clear();
	}
	protected List<DefaultBoundsGraph<C, B, G>> dirtyBoundsGraphs = new ArrayList<>();
	public void repaint() {
		for (BoundsGraphRevalidatedListener<C,  B, G> reavalidatedListeners : this.revalidatedListeners) {
			reavalidatedListeners.onRevalidated(dirtyBoundsGraphs);
		}
		this.dirtyBoundsGraphs.clear();
	}
	
	@Override
	public void setBoundsManager(BoundsManager<Dim, B> boundsManager) {
		this.boundsManager = boundsManager;
	}

	@Override
	public Dim getDim() {
		return this.dim;
	}

	@Override
	public Bounds<B> getBounds(B id) {
		DefaultBoundsGraph<C, B, G> graph = this.boundsGraphs.get(id);
		if(graph == null) return null;
		return graph.getBounds();
	}

	public void printAllBoundsInfos() {
		for (Entry<B, DefaultBoundsGraph<C, B, G>> bounds : this.boundsGraphs.entrySet()) {
			System.out.println(bounds.getValue().getBounds());
		}
	}

	@Override
	public void addNewBoundsGraph(DefaultBoundsGraph<C, B, G> boundsGraph) {
		boundsGraph.addChangedListener(this);
		this.boundsGraphs.put(boundsGraph.getBounds().getId(),boundsGraph);
		this.insertBoundsGraph(this.root,boundsGraph);
	}
	
	public void addNewBoundsGraph(DefaultBoundsGraph<C, B, G> parentGraph,DefaultBoundsGraph<C, B, G> boundsGraph) {
		boundsGraph.addChangedListener(this);
		this.boundsGraphs.put(boundsGraph.getBounds().getId(),boundsGraph);
		this.insertBoundsGraph(parentGraph == null? this.root:parentGraph, boundsGraph);
	}
	
	public void insertBoundsGraph(DefaultBoundsGraph<C, B, G> parentNode,DefaultBoundsGraph<C, B, G> node) {
		DefaultBoundsGraph<C, B, G> _parent = parentNode;
		if(parentNode==null) {
			_parent  = this.root;
		}
		_parent.children.add(node);
		node.parent = parentNode;
	}
	public void removeBoundsGraph(DefaultBoundsGraph<C, B, G> node) {
		this.removeChildren(node);
		this.boundsGraphs.remove(node.getBounds().getId());
		node.parent.children.remove(node);
	}
	public void removeChildren(DefaultBoundsGraph<C, B, G> parentNode) {
		for (Iterator<DefaultBoundsGraph<C, B, G>> i = parentNode.children.iterator(); i.hasNext();) {
			DefaultBoundsGraph<C, B, G> n = i.next();
			this.boundsGraphs.remove(n.getBounds().getId());
	        i.remove();
	    }
	}
	public interface GraphTreeVisitor<T,R,C,B,G> {
		public R visit(DefaultBoundsGraph<C,B,G> graph,T param);
	}
	public <T,R> void visitSubTree(DefaultBoundsGraph<C, B, G> node,GraphTreeVisitor<T,R,C,B,G> visitor,T param) {
		visitor.visit(node,param);
		for(DefaultBoundsGraph<C, B, G> child:node.children) {
			visitSubTree(child,visitor,param);
		}
	}
	public <T,R> void visitSubTree(GraphTreeVisitor<T,R,C,B,G> visitor,T param) {
		visitor.visit(this.root,param);
		for(DefaultBoundsGraph<C, B, G> child:this.root.children) {
			visitSubTree(child,visitor,param);
		}
	}
	
	@Override
	public void onChanged(BoundsGraphChangedEvent<C,B,G> event) {
		Object paramChanged = event.getParamChanged();
		DefaultBoundsGraph<C, B, G> boundsGraph = event.getBoundsGraph();
		  
		List<DefaultBoundsGraph<C, B, G>> list = new ArrayList<>();
		//list.add(boundsGraph);
		BoundsGraphVisitor visitor = new BoundsGraphVisitor();
		this.visitSubTree(boundsGraph,visitor,null);
		list = visitor.getDirtyBoundsGraphs();
		this.dirtyBoundsGraphs.addAll(list);
	}
	
	public List<DefaultBoundsGraph<C,B,G>> getSelectedGraphsByMouse(double[] mousePosition){
		BoundsMouseEventVisitor visitor = new BoundsMouseEventVisitor();
		this.visitSubTree(this.root,visitor,mousePosition);
		return visitor.getSelectedBoundsGraphs();
	}
	class BoundsMouseEventVisitor implements GraphTreeVisitor<double[],Void,C,B,G> {
		List<DefaultBoundsGraph<C, B, G>> selectedBoundsGraphs = new ArrayList<>();
		public List<DefaultBoundsGraph<C,B,G>> getSelectedBoundsGraphs(){
			return this.selectedBoundsGraphs;
		}
		@Override
		public Void visit(DefaultBoundsGraph<C,B,G> graph,double[] mousePosition) {
			if(graph == null||graph.getBounds()==null) return null;
			Bounds<B> bounds = graph.getBounds();
			if(mousePosition[0]>= bounds.getX() && mousePosition[0] <= bounds.getMaxX() &&
					mousePosition[1]>=bounds.getY() && mousePosition[1] <= bounds.getMaxY()) {
				selectedBoundsGraphs.add(graph);
			}
			return null;
		}
	}
	
	
	class BoundsGraphVisitor implements GraphTreeVisitor<Void,Void,C,B,G> {
		List<DefaultBoundsGraph<C, B, G>> dirtyBoundsGraphs = new ArrayList<>();
		public List<DefaultBoundsGraph<C,B,G>> getDirtyBoundsGraphs(){
			return this.dirtyBoundsGraphs;
		}
		@Override
		public Void visit(DefaultBoundsGraph<C,B,G> graph,Void param) {
			if(graph == null) return null;
			BoundsGraphPainter<C,G,B> painter = graph.getBoundsGraphPainter();
			if(painter != null) {
				BoundsGraphCreator<B, C,G> creator = painter.getCreator();
				if (creator != null) {
					if(creator.calculateGraphs(graph) && graph.isVisible()) {
							this.dirtyBoundsGraphs.add(graph);
					}
				}
			}
			return null;
		}
	}
	
	public BoundsManager<Dim, B> getBoundsManager() {
		return boundsManager;
	}

	@Override
	public DefaultBoundsGraph<C, B, G> getBoundsGraph(B boundsKey) {
		return this.boundsGraphs.get(boundsKey);
	}
}
