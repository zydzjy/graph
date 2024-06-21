package yuzhou.gits.graph;

public abstract class AbstractBoundsGraphCreator<B,C,G> implements BoundsGraphCreator<B,C,G> {
	protected GraphObjectList<GraphShape> shapes = new DefaultGraphObjectList<GraphShape>();
	protected GraphObjectList<Object> images = new DefaultGraphObjectList<>();
	protected GraphObjectList<SimpleGraphText> texts = new DefaultGraphObjectList<>();
	public GraphObjectList<GraphShape> getShapes() {return this.shapes;}
	public GraphObjectList<Object> getImages()  {return this.images;}
	public GraphObjectList<SimpleGraphText> getTexts() {return this.texts;}
	protected GraphShapeFactory<?> graphShapeFactory;
	public void preparePaprameter(DefaultBoundsGraph<C, B, G> boundsGraph) {}
	public AbstractBoundsGraphCreator(GraphShapeFactory<?> graphShapeFactory){
		this.graphShapeFactory = graphShapeFactory;
	}
	public boolean calculateGraphs(DefaultBoundsGraph<C,B,G> boundsGraph) {
		this.shapes.clear();
		this.texts.clear();
		this.images.clear();
		return _calculateGraphs(boundsGraph);
	}
	protected abstract boolean _calculateGraphs(DefaultBoundsGraph<C,B,G> boundsGraph);
	
}
