package yuzhou.gits.graph.javafx;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public abstract class Graph<Observable> {
	protected double topGap;
	protected double bottomGap;
	protected double leftGap;
	protected double rightGap;
	protected double xMinGap;
	protected double xGap;
	protected double yGap;
	protected Canvas canvas;
	protected double w;
	protected double h;
	protected double graphW;
	protected double graphH;
	protected GraphicsContext gCxt;
	public Graph(double topGap, double bottomGap, double leftGap, double rightGap, double xGap, double yGap,Canvas canvas) {
		super();
		this.topGap = topGap;
		this.bottomGap = bottomGap;
		this.leftGap = leftGap;
		this.rightGap = rightGap;
		this.xMinGap = xGap;
		this.xGap = xMinGap;
		this.yGap = yGap;
		this.canvas = canvas;
		this.gCxt = canvas.getGraphicsContext2D();
		this.w = canvas.getWidth();
		this.h = canvas.getHeight();
		this.graphW = w-leftGap-rightGap;
		this.graphH = h-topGap-bottomGap;
	}
	protected double xTickSize;
	protected double yTickSize;
	protected abstract void calXTickSize();
	protected abstract void calYTickSize();
	
	protected Observable model;
	public Observable getModel() {
		return this.model;
	}
	public void draw() {
		this.calCoors();

		if(Platform.isFxApplicationThread()) {
			this._drawGraph();
		}else {
			Platform.runLater(()->{
				this._drawGraph();
			});
		}
	}
	protected abstract void calCoors();
	protected abstract void _drawGraph();
	public abstract void clear();
}
