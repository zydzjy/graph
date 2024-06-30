package yuzhou.gits.graph.javafx;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public abstract class Graph<M> {
	protected double topGap;
	protected double bottomGap;
	protected double leftGap;
	protected double rightGap;
	protected double xMinGap;
	protected Canvas canvas;
	protected double w;
	protected double h;
	protected double graphW;
	protected double graphH;
	protected GraphicsContext gCxt;
	public Graph(double topGap, double bottomGap, double leftGap, double rightGap, Canvas canvas) {
		super();
		this.topGap = topGap;
		this.bottomGap = bottomGap;
		this.leftGap = leftGap;
		this.rightGap = rightGap;
		this.canvas = canvas;
		this.gCxt = canvas.getGraphicsContext2D();
		this.w = canvas.getWidth();
		this.h = canvas.getHeight();
		this.graphW = w-leftGap-rightGap;
		this.graphH = h-topGap-bottomGap;
	}
	
	protected M model;
	public M getModel() {
		return this.model;
	}
	public void draw() {
		this.calCoors();
		Platform.runLater(()->{
				this._drawGraph();
		});
	}
	protected abstract void sort();
	protected abstract void calCoors();
	protected abstract void _drawGraph();
	public abstract void clear();
}
