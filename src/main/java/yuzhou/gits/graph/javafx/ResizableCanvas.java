package yuzhou.gits.graph.javafx;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

public class ResizableCanvas extends Canvas  {
	protected GraphicsContext g;
	public ResizableCanvas() {
		this.g = this.getGraphicsContext2D();
	}
	
	public void paint() {};
	
	public void attachParent(Pane pane) {
		if(pane.getChildren().contains(this)) {
			return;
		}
		this.widthProperty().bind(pane.widthProperty());
		this.heightProperty().bind(pane.heightProperty());
		pane.getChildren().add(this);
	}
	
	@Override
	public boolean isResizable() {
		return true;
	}

	@Override
	public double prefWidth(double height) {
		return getWidth();
	}

	@Override
	public double prefHeight(double width) {
		return getHeight();
	}
	
	@Override
	public String toString() {
		return "("+this.hashCode()+")"+super.toString();
	}
}