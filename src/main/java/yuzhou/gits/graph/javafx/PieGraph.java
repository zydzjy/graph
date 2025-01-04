package yuzhou.gits.graph.javafx;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class PieGraph<P extends Number,M,T extends Iterable<M>> extends Graph<T> {
	double pieRadius;
	public PieGraph(double topGap, double bottomGap, double leftGap, double rightGap,Canvas canvas,PropertyGetter<P,M> valGetter) {
		super(topGap, bottomGap, leftGap, rightGap, canvas);
		this.pieRadius =  this.graphW > this.graphH ? this.graphH : this.graphW;
		centerX = this.graphW/2-pieRadius/2;
		centerY = this.graphH/2-pieRadius/2;
		this.getter = valGetter;
	}
	PropertyGetter<P,M> getter;
	double[][] pieArcs; 
	@Override
	protected void calCoors() {
		this.pieArcs = new double[totalModels][2];
		this.colors = new Color[totalModels];
		Iterator<M> it = this.model.iterator();
		int idx = 0;
		double startAngles = 0.0d;
		while(it.hasNext()) {
			M item = it.next();
			double val = this.getter.get(item).doubleValue();
			double percent = val/totalVals;
			pieArcs[idx][0] = startAngles;
			pieArcs[idx][1] = percent*360.0d;
			//System.out.println(pieArcs[idx][0]+","+pieArcs[idx][1]);
			startAngles += pieArcs[idx][1];
			colors[idx] = this.colorsFun.apply(item,idx);
			idx++;
		}
	}
	double centerX;
	double centerY;
	@Override
	protected void _drawGraph() {
		this.gCxt.clearRect(0, 0, this.w, this.h);
		for(int i=0;i<this.pieArcs.length;i++) {
			double[] arcAngles = pieArcs[i];
			this.gCxt.setFill(this.colors[i]);
			this.gCxt.fillArc(this.centerX, this.centerY, this.pieRadius, this.pieRadius, arcAngles[0], arcAngles[1], ArcType.ROUND);
		}
	}
	
	public void setModel(T m,BiFunction<M,Integer,Color> colorsFun) {
		this.model = m;
		this.colorsFun = colorsFun;
		if(model==null) {
			this.gCxt.clearRect(0, 0, w, h);
		}
		this.sort();
		this.draw();
	}

	@Override
	public void clear() {
		this.gCxt.clearRect(0, 0,w,h);
		this.model = null;
	}
	BiFunction<M,Integer,Color> colorsFun;
	Color[] colors;
	double[] percents;
	int totalModels = 0;
	double totalVals = 0;
	@Override
	protected void sort() {
		Iterator<M> it = this.model.iterator();
		totalVals = 0.0d;
		while(it.hasNext()) {
			M item = it.next();
			totalVals += this.getter.get(item).doubleValue();
			totalModels++;
		}
		if(this.totalModels == 0) {
			this.gCxt.clearRect(0, 0, w, h);
		}
	}
}
