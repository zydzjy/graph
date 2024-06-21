package yuzhou.gits.graph;

import java.util.List;
import java.util.LinkedList;


import java.util.ArrayList;

public class DefaultBoundsGraph<C,B,G> implements BoundsGraph {
	protected Bounds<B> bounds;
	protected C background;
	protected List<DefaultBoundsGraph<C,B,G>> children = new LinkedList<>();
	protected boolean visible = true;
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public static class BoundsGraphChangedEvent<C, B, G>{
		Object paramChanged;
		DefaultBoundsGraph<C,B,G> boundsGraph;
		boolean isRecalculated;
		public Object getParamChanged() {
			return this.paramChanged;
		}
		public boolean isRecalculated() {
			return this.isRecalculated;
		}
		public DefaultBoundsGraph<C, B, G> getBoundsGraph() {
			return this.boundsGraph;
		}
		public BoundsGraphChangedEvent(Object paramChanged, DefaultBoundsGraph<C, B, G> boundsGraph, boolean isRecalculated) {
			super();
			this.paramChanged = paramChanged;
			this.boundsGraph = boundsGraph;
			this.isRecalculated = isRecalculated;
		}
	}
	public interface BoundsGraphChangedEventListener<C,B,G> {
		public void onChanged(BoundsGraphChangedEvent<C,B,G> event);
	}
	protected List<BoundsGraphChangedEventListener<C,B,G>> listeners = new ArrayList<>();
	//BoundsGraphProperties<C> graphProps;
	protected BoundsGraphPainter<C,G,B> painter;
	protected GraphParametersContext context;
	public Bounds<B> getBounds() {
		return bounds;
	}
	public BoundsGraphPainter<C,G,B> getBoundsGraphPainter() {
		return painter;
	}
//	public BoundsGraphProperties<C> getGraphProps() {
//		return graphProps;
//	}
	public void addChangedListener(BoundsGraphChangedEventListener<C,B,G> l) {
		this.listeners.add(l);
	}
	protected DefaultBoundsGraph<C,B,G> parent;
	public DefaultBoundsGraph() {}
	public DefaultBoundsGraph(DefaultBoundsGraph<C,B,G> parent,Bounds<B> bounds,BoundsGraphPainter<C,G,B> painter,
			C background,GraphParametersContext context) {
		super();
		this.parent = parent;
		this.bounds = bounds;
		this.painter = painter;
		this.background = background;
		this.context = context;
	}
	public DefaultBoundsGraph(Bounds<B> bounds,BoundsGraphPainter<C,G,B> painter,
			C background,GraphParametersContext context) {
		super();
		this.parent = null;
		this.bounds = bounds;
		this.painter = painter;
		this.background = background;
		this.context = context;
	}
	public C getBackground() {
		return this.background;
	}
	public void fireChangedEvent(Object paramChanged,boolean isRecalculated) {
		BoundsGraphChangedEvent<C, B, G> event =  new BoundsGraphChangedEvent<C, B, G>(paramChanged,this,isRecalculated);
		for(BoundsGraphChangedEventListener<C,B,G> l:this.listeners) {
			l.onChanged(event);
		}
	}
	public GraphParametersContext getContext() {
		return this.context;
	}
	
	@Override
	public void revalidate() {
	}
	
	protected double[] dirtyArea = null;
	public double[] getDirtyArea() {
		return dirtyArea;
	}
}
