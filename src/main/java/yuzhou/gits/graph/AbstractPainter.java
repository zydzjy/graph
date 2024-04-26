//package yuzhou.gits.graph;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.concurrent.ConcurrentLinkedQueue;
//import java.util.function.Function;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import yuzhou.gits.graph.AbstractBoundsGraphCreator.BoundsGraphCreatorEvent;
//import yuzhou.gits.graph.AbstractBoundsGraphCreator.BoundsGraphCreatorEventListener;
//
//public abstract class AbstractPainter<G, Dim> implements Painter<C,G,String>,BoundsGraphCreatorEventListener {
//	static Logger logger = LoggerFactory.getLogger(AbstractPainter.class);
//	Function<G,Void> globalPainterGraphicsSetup;
//	public void setGlobalPainterGraphicsSetup(Function<G,Void> globalPainterGraphicsSetup) {
//		this.globalPainterGraphicsSetup = globalPainterGraphicsSetup;
//	}
//	public static class PaintEvent<G> {
//		private G graphics;
//		public PaintEvent(G graphics) {
//			this.graphics = graphics;
//		}
//		public G getGraphics() {
//			return this.graphics;
//		}
//	}
//	public interface PaintEventListener<G> {
//		public void paintBegin(PaintEvent<G> event);
//		public void paintEnd(PaintEvent<G> event);
//	}
//	List<PaintEventListener<G>> listeners = new ArrayList<>();
//	 
//	CommonPainterTarget target;
//	public AbstractPainter() {
//		 
//	}
//	public void setTarget(CommonPainterTarget target) {
//		this.target = target;
//	}
//	public void addPaintEventListener(PaintEventListener<G> listener) {
//		this.listeners.add(listener);
//	}
//	 
//	protected void painBegin(PaintEvent<G> event) {
//		for(PaintEventListener<G> l:this.listeners) {
//			l.paintBegin(event);
//		}
//	}
//	
//	public void paint(G g){
//		if(globalPainterGraphicsSetup != null) {
//			this.globalPainterGraphicsSetup.apply(g);
//		}
//		this.painBegin(new PaintEvent<G>(g));
//		AbstractBoundsGraphCreator boundsGraphCreator = null; 
//		while((boundsGraphCreator = boundsGraphList.poll()) != null) {
//			Bounds<Integer> bounds = boundsGraphCreator.getBounds();
//			BoundsGraphDrawer<G> boundsGraphPainter = this.paper.getBoundsGraphPainter(bounds);
//			BoundsGraphProperties  graphProps = boundsGraphCreator.getBoundsGraphProperties();
//			//TODO:clear bounds
//			this.clearBounds(g, bounds,graphProps);
//			boundsGraphPainter.draw(g, graphProps, boundsGraphCreator);
//		}
//		this.painEnd(new PaintEvent<G>(g));
//	}
//	
//	private void painEnd(PaintEvent<G> paintEvent) {
//		for(PaintEventListener<G> l:this.listeners) {
//			l.paintEnd(paintEvent);
//		}
//	}
//	
//	protected abstract void clearBounds(G g2d,Bounds<Integer> bounds,BoundsGraphProperties graphProps);
//	ConcurrentLinkedQueue<AbstractBoundsGraphCreator> boundsGraphList = new ConcurrentLinkedQueue<>();
//	public void createDone(BoundsGraphCreatorEvent event) {
//		this.boundsGraphList.add(event.getCreator());
//		if(target != null) {
//			this.target.requestPaint(event.getCreator().getBounds());
//		}
//	}
//}
