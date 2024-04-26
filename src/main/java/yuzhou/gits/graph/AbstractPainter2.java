//package yuzhou.gits.graph;
//
//import java.awt.Color;
//import java.awt.Graphics2D;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Function;
//
//import javax.swing.JComponent;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import yuzhou.gits.graph.BoundsGraphCreator.BoundsGraphCreatorEvent;
//import yuzhou.gits.graph.BoundsGraphCreator.BoundsGraphCreatorEventListener;
//
//public abstract class AbstractPainter2 implements BoundsGraphCreatorEventListener,Painter<Object> {
//	static Logger logger = LoggerFactory.getLogger(AbstractPainter.class);
//	Function<Graphics2D,Void> globalPainterGraphicsSetup;
//	public void setGlobalPainterGraphicsSetup(Function<Graphics2D,Void> globalPainterGraphicsSetup) {
//		this.globalPainterGraphicsSetup = globalPainterGraphicsSetup;
//	}
//	boolean isOpaque = true;
//	public void setOpaque(boolean isOpaque) {
//		this.isOpaque = isOpaque;
//	}
//	public static class PaintEvent {
//		private Graphics2D graphics;
//		private JComponent target;
//		public PaintEvent(Graphics2D graphics,JComponent target) {
//			this.graphics = graphics;
//			this.target = target;
//		}
//		public Graphics2D getGraphics() {
//			return this.graphics;
//		}
//		public JComponent getTarget() {
//			return this.target;
//		}
//	}
//	public interface PaintEventListener {
//		public void paintBegin(PaintEvent event);
//		public void paintEnd(PaintEvent event);
//	}
//	List<PaintEventListener> listeners = new ArrayList<>();
//	List<BoundsGraph> boundsGraphList = new ArrayList<>();
//	List<Integer> newBounds = new ArrayList<>();
//	CommonPainterTarget target;
//	public AbstractPainter(CommonPainterTarget target) {
//		this.target = target;
//		needPaintedList.clear();
//	}
//	public void addPaintEventListener(PaintEventListener listener) {
//		this.listeners.add(listener);
//	}
//	public  void addBounds(Bounds<Integer> bounds, BoundsGraphProperties props, BoundsGraphCreator helper,BoundsGraphPainter painter) {
//		boundsGraphList.add(new BoundsGraph(props,helper,bounds,painter));
//		bounds.setId(boundsGraphList.size()-1);
//		helper.addBoundsGraphCreatorEventListener(this);
//		//this.needPaintedList.add(null);
//		this.newBounds.add(bounds.getId());
//	}
////	public  void addBounds(int positionIdx,Bounds<Integer> bounds, BoundsGraphProperties props, BoundsGraphCreator creator,BoundsGraphPainter painter) {
////		boundsGraphList.add(positionIdx,new BoundsGraph(props,creator,bounds,painter));
////		bounds.setId(boundsGraphList.size()-1);
////		creator.addBoundsGraphPaintEventListener(this);
////		this.needPaintedList.add(null);
////		this.newBounds.add(bounds.getId());
////	}
//	protected void _paint(Graphics2D g2d,BoundsGraph boundsGraph ) {
//		BoundsGraphProperties props = boundsGraph.getGraphProperties();
//		clearBounds(g2d,boundsGraph);
//		BoundsGraphPainter painter = boundsGraph.getBoundsGraphPainter();
//		BoundsGraphCreator helper = boundsGraph.getBoundsGraphCreator();
//		if (painter != null && helper != null) {
//			//creator.calulateGraphs(bounds, props);
//			painter.paint(g2d, props,helper);
//		}
//	}
//	protected void painBegin(PaintEvent event) {
//		for(PaintEventListener l:this.listeners) {
//			l.paintBegin(event);
//		}
//	}
//	
//	public void paint(Graphics2D g2d) {
//		if(globalPainterGraphicsSetup != null) {
//			this.globalPainterGraphicsSetup.apply(g2d);
//		}
//		this.painBegin(new PaintEvent(g2d,this.target));
//		
//		clearNewBounds(g2d);
//		boolean painted = false;
//		//int i=0;
//		for (Integer i : this.needPaintedList) {
////			if(bounds == null) {
////				i++;
////				continue;
////			}
//			BoundsGraph boundsGraph = this.boundsGraphList.get(i);
//			this._paint(g2d, boundsGraph);
//			//this.needPaintedList.set(i++, null);
//			painted = true;
//		}
//		this.needPaintedList.clear();
//		//SWING通常主动调用JComponent.paint???
//		if(!painted) {
//			for(BoundsGraph graph:this.boundsGraphList) {
//				this._paint(g2d, graph);
//			}
//		}
//	}
//	protected void clearNewBounds(Graphics2D g2d) {
//		if(!this.isOpaque) {
//			return;
//		}
//		while(this.newBounds.size()>0) {
//			clearBounds(g2d,this.boundsGraphList.get(newBounds.get(0)));
//			this.newBounds.remove(0);
//		}
//	}
//	protected void clearBounds(Graphics2D g2d,BoundsGraph boundsGraph) {
//		if(!this.isOpaque) {
//			return;
//		}
//		BoundsGraphProperties props = boundsGraph.getGraphProperties();
//		Color background = props.getBackground();
//		if (background != null) {
//			g2d.setColor(background);
//			Bounds<Integer> bounds = (Bounds<Integer>) boundsGraph.getBounds();
//			logger.debug("clear bounds:"+bounds+" by backgound:"+background);
//			g2d.fillRect((int) bounds .getX(), (int) bounds.getY(), (int) bounds.getWidth(),
//					(int) bounds.getHeight());
//		}
//	}
//	//protected List<Bounds<Integer>> needPaintedList = new ArrayList<>();
//	protected java.util.Set<Integer> needPaintedList = new java.util.HashSet<>();
//	 
//	public void createDone(BoundsGraphCreatorEvent event) {
//		if(event.repaint) {
//			Bounds<Integer> bounds = event.creator.bounds;
//			//this.needPaintedList.set(bounds.getId(),bounds);
//			needPaintedList.add(bounds.getId());
//			//更新需要更新的部分
//			this.target.repaint((int)bounds.x, (int)bounds.y, (int)bounds.w, (int)bounds.h);
//		}
//	}
//	public void addPainted(Bounds<Integer> testBounds) {
//		needPaintedList.add(testBounds.getId());
//		this.target.repaint((int)testBounds.x, (int)testBounds.y, (int)testBounds.w, (int)testBounds.h);
//	}
//}
