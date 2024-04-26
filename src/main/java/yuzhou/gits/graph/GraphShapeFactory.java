package yuzhou.gits.graph;

public interface GraphShapeFactory<T> {
	public GraphShape createRect2D(double x,double y,double w,double h);
	public GraphShape createLine2D(double x0,double y0,double x1,double y1);
	public GraphShape createOval2D(double x,double y,double w,double h);
	public GraphShape createPolyline(double[] xPoints,double[] yPoints,int nPoints);
	public GraphShape createSimpleDashLine(double x0,double y0,double x1,double y1,double...dashArray);
	
	public GraphShape createShape(T type);
}
