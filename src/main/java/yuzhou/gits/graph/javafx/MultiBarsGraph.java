package yuzhou.gits.graph.javafx;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.stage.PopupWindow.AnchorLocation;

public class MultiBarsGraph<M, T extends ObservableList<M>> extends Graph<T> {
	protected Color[][] barColors;
	int currIdx=-1;
	String tipMsg = null;
	boolean showTip=false;
	BiFunction<Integer,List<M>,String> tipMsgFun;
	public MultiBarsGraph(double topGap, double bottomGap, double leftGap, double rightGap, double xGap, double yGap,
			Canvas canvas, Color[][] barColors,boolean showTip,BiFunction<Integer,List<M>,String> tipMsgFun) {
		super(topGap, bottomGap, leftGap, rightGap, xGap, yGap, canvas);
		this.barColors = barColors;
		this.showTip = showTip;
		this.tipMsgFun = tipMsgFun;
	}

	protected double maxBarYVal = -Double.MIN_VALUE;
	protected double minBarYVal = Double.MAX_VALUE;
	protected double maxLineYVal = -Double.MIN_VALUE;
	protected double minLineYVal = Double.MAX_VALUE;

	protected void sort() {
		for (int j = 0; j < this.model.size(); j++) {
			M item = model.get(j);
			for (int i = 0; i < barGetters.size(); i++) {
				Double y = (Double) barGetters.get(i).get(item);
				if (y > maxBarYVal) {
					maxBarYVal = y;
				}
				if (y < minBarYVal) {
					minBarYVal = y;
				}
			}
			
			for (int i = 0; i < lineGetters.size(); i++) {
				Double y = (Double) lineGetters.get(i).get(item);
				if (y > maxLineYVal) {
					maxLineYVal = y;
				}
				if (y < minLineYVal) {
					minLineYVal = y;
				}
			}
		}
	}

	PropertyGetter<Double, M> xGetter = null;
	List<PropertyGetter<Double, M>> barGetters = null;
	List<PropertyGetter<Double, M>> lineGetters = null;
	int bars;
	int lines;
	public void setModel(T model, PropertyGetter<Double, M> xGetter, List<PropertyGetter<Double, M>> barGetters,List<PropertyGetter<Double,M>> lineGetters) {
		this.model = model;
		if(showTip) {
			final Tooltip tooltip = new Tooltip();
			tooltip.setAutoHide(true);
			this.canvas.setOnMouseExited(e->{
				tooltip.hide();
			});
			tooltip.setAnchorLocation(AnchorLocation.WINDOW_TOP_LEFT);
			this.canvas.setOnMouseMoved(e->{
				int idx = (int) Math.floor((e.getX()-this.leftGap)/(this.xTickSize+this.xGap));
				if(idx>=this.model.size()) {
					idx = -1;
				}else if(idx<0) {
					idx=0;
				}
				if(currIdx != idx) {
					currIdx = idx;
					tipMsg = tipMsgFun.apply(idx,this.model);
					tooltip.setText(tipMsg);
				}
				//canvas.localToScreen(new Point2D(e.getX(), e.getY()));
				if(tipMsg!=null) {
					tooltip.show(canvas, e.getScreenX()+10, e.getScreenY()+10);
				}else {
					tooltip.hide();
				}
			});
		}
		this.xGetter = xGetter;
		this.barGetters = barGetters == null ? new ArrayList<>() : barGetters;
		this.lineGetters = lineGetters == null ? new ArrayList<>() : lineGetters;
		bars = this.barGetters.size();
		lines = this.lineGetters.size();
		this.model.addListener(new ListChangeListener<M>() {
			@Override
			public void onChanged(Change<? extends M> c) {
				MultiBarsGraph.this.sort();
				MultiBarsGraph.this.calXTickSize();
				MultiBarsGraph.this.calYTickSize();
				MultiBarsGraph.this.draw();
			}
		});
		this.sort();
		this.calXTickSize();
		this.calYTickSize();
		this.draw();
	}

	double maxBarRange;
	double yLineTickSize;
	double maxLineRange;
	@Override
	protected void calYTickSize() {
		maxBarRange = Math.max(Math.abs(this.maxBarYVal), this.minBarYVal) * 2;
		yTickSize = (this.graphH) / (maxBarRange);
		
		maxLineRange = Math.max(Math.abs(this.maxLineYVal), this.minLineYVal) * 2;
		maxLineRange = maxLineRange<200.0d?200.0d:maxLineRange;
		this.yLineTickSize = (this.graphH) / (maxLineRange);
	}

	@Override
	protected void calXTickSize() {
		xTickSize = (this.graphW - ((this.model.size() - 1) * this.xGap)) / this.model.size();
		barSize = xTickSize / this.bars;
		if (barSize > 30) {
			this.barSize = 30;
			xTickSize = this.barSize * (this.bars);
		}
	}

	double barSize;

	@Override
	protected void _drawGraph() {
		if(this.bars==0) {
			return;
		}
		this.gCxt.clearRect(0, 0, w, h);
		for (int j = 0; j < xBaseCoors.length; j++) {
			for (int i = 0; i < this.bars; i++) {
				if (this.colors[j][i] == 'P') {
					gCxt.setFill(barColors[i][0]);
				} else {
					gCxt.setFill(barColors[i][1]);
				}
				gCxt.fillRect(xBaseCoors[j] + i * barSize, yBarCoors[j][i], this.barSize, heightCoors[j][i]);
			}
		}
		
		if(this.lines>0) {
			this.gCxt.setLineWidth(5.0d);
			for(int i=0;i<this.lines;i++) {
				this.gCxt.strokePolyline(xLineCoors, this.yLineCoors[i], this.model.size());
			}
		}
	}

	double[] xBaseCoors;
	double[] xLineCoors;
	double[][] yBarCoors;
	double[][] yLineCoors;
	double[][] heightCoors;
	char[][] colors;

	@Override
	protected void calCoors() {
		if(this.bars==0) {
			return;
		}
		this.xBaseCoors = new double[this.model.size()];
		for (int i = 0; i < this.model.size(); i++) {
			this.xBaseCoors[i] = this.leftGap + this.xTickSize * i+this.xGap*i;
		}
		if(lines>0) {
			xLineCoors = new double[this.model.size()];
			for(int i=0;i<xLineCoors.length;i++) {
				xLineCoors[i] = this.xBaseCoors[i]+(this.xTickSize)/2.0d;
			}
		}
		this.yBarCoors = new double[this.model.size()][this.bars];
		this.colors = new char[this.model.size()][this.bars];
		this.heightCoors = new double[this.model.size()][this.bars];
		this.maxBarYVal = this.maxBarRange / 2.0d;
		double zeroAxisBarY = (this.maxBarYVal*this.yTickSize);
		for (int j = 0; j < this.model.size(); j++) {
			for (int i = 0; i < this.bars; i++) {
				double yVal = (double) this.barGetters.get(i).get(this.model.get(j));
				double yCoor = 0;
				if (yVal >= 0) {
					yCoor = (this.maxBarYVal - yVal) * this.yTickSize;
					colors[j][i] = 'P';
				} else {
					yCoor = zeroAxisBarY;
					colors[j][i] = 'N';
				}
				yBarCoors[j][i] = yCoor+this.topGap;
				heightCoors[j][i] = Math.abs(yVal * this.yTickSize);
			}
		}
		
		if(lines>0) {
			this.maxLineYVal = this.maxLineRange /2.0d;
			this.yLineCoors = new double[this.lines][this.model.size()];
			double zeroAxisLineY = (this.maxLineYVal*this.yLineTickSize);
			for (int i = 0; i < this.lines; i++) {
				PropertyGetter<Double,M> getter = this.lineGetters.get(i);
				for(int j=0;j<this.model.size();j++) {
					double yVal = (double)getter.get(this.model.get(j));
					double yCoor = 0;
					if (yVal >= 0) {
						yCoor = (this.maxLineYVal - yVal) * this.yLineTickSize;
					} else {
						yCoor = -yVal * this.yLineTickSize+zeroAxisLineY;
					}
					yLineCoors[i][j] = yCoor+this.topGap;
				}
			}
		}
	}

	@Override
	public void clear() {
		this.gCxt.clearRect(0, 0,w,h);
		this.model = null;
		this.xBaseCoors = null;
		this.yBarCoors = null;
		this.xLineCoors = null;
		this.yLineCoors = null;
	}
	
	public void bindYGetters(List<PropertyGetter<Double,M>> barGetters,List<PropertyGetter<Double,M>> lineGetters,BiFunction<Integer,List<M>,String> tipMsgFun) {
		this.gCxt.clearRect(0, 0,w,h);
		maxBarYVal = -Double.MIN_VALUE;
		minBarYVal = Double.MAX_VALUE;
		maxLineYVal = -Double.MIN_VALUE;
		minLineYVal = Double.MAX_VALUE;
		this.maxBarRange = 0;
		this.maxLineRange = 0;
		this.yBarCoors = null;
		this.xLineCoors = null;
		this.yLineCoors = null;
		this.barGetters = barGetters == null?new ArrayList<>():barGetters;
		this.lineGetters = lineGetters == null?new ArrayList<>():lineGetters;
		this.bars=this.barGetters.size();
		this.lines=this.lineGetters.size();
		tipMsg=null;
		this.tipMsgFun = tipMsgFun;
		this.currIdx=-1;
		this.sort();
		this.calYTickSize();
		this.draw();
	}
}