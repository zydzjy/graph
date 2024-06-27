package yuzhou.gits.graph.javafx;

import javafx.scene.canvas.Canvas;

public abstract class XYAxisGraph<T> extends Graph<T> {

	public XYAxisGraph(double topGap, double bottomGap, double leftGap, double rightGap, double xGap, double yGap,
			Canvas canvas) {
		super(topGap, bottomGap, leftGap, rightGap, canvas);
		this.xMinGap = xGap;
		this.xGap = xMinGap;
		this.yGap = yGap;
	}
	protected double xGap;
	protected double yGap;
	protected double xTickSize;
	protected double yTickSize;
	protected abstract void calXTickSize();
	protected abstract void calYTickSize();
}