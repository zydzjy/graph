package yuzhou.gits.graph.javafx;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.stage.PopupWindow.AnchorLocation;

public class MultiBarsGraph<M, T extends List<M>> extends XYAxisGraph<T> {
	protected Color[][] barColors;
	int currIdx=-1;
	String tipMsg = null;
	boolean showTip=false;
	BiFunction<Integer,List<M>,String> tipMsgFun;
	public MultiBarsGraph(double topGap, double bottomGap, double leftGap, double rightGap, double xGap, double yGap,
			Canvas canvas, Color[][] barColors,boolean showTip,BiFunction<Integer,List<M>,String> tipMsgFun,BiFunction<PropertyGetter<Double,M>,M,Integer> colorFun) {
		super(topGap, bottomGap, leftGap, rightGap, xGap, yGap, canvas);
		this.barColors = barColors;
		this.showTip = showTip;
		this.tipMsgFun = tipMsgFun;
		this.colorFun = colorFun;
		
		if(showTip) {
			Platform.runLater(()->{
				final Tooltip tooltip = new Tooltip();
				tooltip.setAutoHide(true);
				this.canvas.setOnMouseExited(e->{
					tooltip.hide();
				});
				tooltip.setAnchorLocation(AnchorLocation.WINDOW_TOP_LEFT);
				this.canvas.setOnMouseMoved(e->{
					if(this.model == null || this.model.size()==0)return;
					int idx = (int) Math.floor((e.getX()-this.leftGap)/(this.xTickSize+this.xMinGap));
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
			});
		}
	}

	protected double maxBarYVal = -Double.MIN_VALUE;
	protected double minBarYVal = 0;
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
		
		this.xGetter = xGetter;
		this.barGetters = barGetters == null ? new ArrayList<>() : barGetters;
		this.lineGetters = lineGetters == null ? new ArrayList<>() : lineGetters;
		bars = this.barGetters.size();
		lines = this.lineGetters.size();
//		this.model.addListener(new ListChangeListener<M>() {
//			@Override
//			public void onChanged(Change<? extends M> c) {
//				MultiBarsGraph.this.sort();
//				MultiBarsGraph.this.calXTickSize();
//				MultiBarsGraph.this.calYTickSize();
//				MultiBarsGraph.this.draw();
//			}
//		});
		if(this.model.size()>0) {
			reset();
			this.sort();
			this.calXTickSize();
			this.calYTickSize();
			this.draw();
		}
	}

	double maxBarRange;
	double yLineTickSize;
	double maxLineRange;
	double barZeroAxisY;
	@Override
	protected void calYTickSize() {
		barZeroAxisY = this.h-this.bottomGap;
		maxBarRange = this.maxBarYVal;
		if(this.maxBarYVal<0 && this.minBarYVal<0) {
			barZeroAxisY = this.topGap;
			maxBarRange = Math.abs(this.minBarYVal);
		}else if(this.maxBarYVal>0&&this.minBarYVal<0) {
			maxBarRange = this.maxBarYVal + Math.abs(this.minBarYVal);
			barZeroAxisY = maxBarYVal/maxBarRange*this.graphH+this.topGap;
		}
		//maxBarRange = Math.max(Math.abs(this.maxBarYVal), this.minBarYVal) * 2;
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
			this.xGap = (this.graphW- xTickSize*this.model.size())/(this.model.size()-1);
		}
	}
	
	public void resize() {
		this.w = canvas.getWidth();
		this.h = canvas.getHeight();
		this.graphW = w-leftGap-rightGap;
		this.graphH = h-topGap-bottomGap;
		if(this.model.size()>0) {
			MultiBarsGraph.this.sort();
			MultiBarsGraph.this.calXTickSize();
			MultiBarsGraph.this.calYTickSize();
			MultiBarsGraph.this.draw();
		}
		
	}

	double barSize;

	@Override
	protected void _drawGraph() {
		if(this.bars==0) {
			return;
		}
		this.gCxt.clearRect(0, 0, w, h);
		for (int i = 0; i < this.bars; i++) {
			Color[] _barColors = this.barColors[i];
			for (int j = 0; j < xBaseCoors.length; j++) {
				gCxt.setFill(_barColors[this.colors[j][i]]);
				gCxt.fillRect(xBaseCoors[j] + i * barSize, yBarCoors[j][i], this.barSize, heightCoors[j][i]);
			}
		}
		
		if(this.lines>0) {
			this.gCxt.setLineWidth(5.0d);
			for(int i=0;i<this.lines;i++) {
				this.gCxt.setStroke(Color.GRAY);
				this.gCxt.strokePolyline(xLineCoors, this.yLineCoors[i], this.model.size());
			}
		}
	}

	double[] xBaseCoors;
	double[] xLineCoors;
	double[][] yBarCoors;
	double[][] yLineCoors;
	double[][] heightCoors;
	int[][] colors;
	BiFunction<PropertyGetter<Double,M>,M,Integer> colorFun;
	@Override
	protected void calCoors() {
		if(this.bars==0 || this.model.size() == 0) {
			return ;
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
		this.colors = new int[this.model.size()][this.bars];
		this.heightCoors = new double[this.model.size()][this.bars];
		//maxBarYVal = this.maxBarRange / 2.0d;
		//double zeroAxisBarY = (maxBarYVal*this.yTickSize);
		for (int j = 0; j < this.model.size(); j++) {
			for (int i = 0; i < this.bars; i++) {
				double yVal = (double) this.barGetters.get(i).get(this.model.get(j));
				double yCoor = 0;
				heightCoors[j][i] = Math.abs(yVal * this.yTickSize);
				if (yVal >= 0) {
					//yCoor = (maxBarYVal - yVal) * this.yTickSize;
					yCoor = this.barZeroAxisY-heightCoors[j][i];
				} else {
					yCoor = this.barZeroAxisY;
				}
				int color = colorFun.apply( this.barGetters.get(i),this.model.get(j));
				colors[j][i] = color;
				yBarCoors[j][i] = yCoor;
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
	void reset() {
		this.gCxt.clearRect(0, 0,w,h);
		maxBarYVal = -Double.MIN_VALUE;
		minBarYVal = 0;
		maxLineYVal = -Double.MIN_VALUE;
		minLineYVal = Double.MAX_VALUE;
		this.maxBarRange = 0;
		this.maxLineRange = 0;
		this.xBaseCoors = null;
		this.yBarCoors = null;
		this.xLineCoors = null;
		this.yLineCoors = null;
		this.xGap = this.xMinGap;
		this.barGetters = barGetters == null?new ArrayList<>():barGetters;
		this.lineGetters = lineGetters == null?new ArrayList<>():lineGetters;
		this.bars=this.barGetters.size();
		this.lines=this.lineGetters.size();
		tipMsg=null;
		this.currIdx=-1;
	}
	public void bindYGetters(List<PropertyGetter<Double,M>> barGetters,List<PropertyGetter<Double,M>> lineGetters,BiFunction<Integer,List<M>,String> tipMsgFun,
			Color[][] barColors,BiFunction<PropertyGetter<Double,M>,M,Integer> colorFun) {
		reset();
		this.gCxt.clearRect(0, 0,w,h);
		 
		this.barColors = barColors;
		this.colorFun = colorFun;
		 
		this.barGetters = barGetters == null?new ArrayList<>():barGetters;
		this.lineGetters = lineGetters == null?new ArrayList<>():lineGetters;
		this.bars=this.barGetters.size();
		this.lines=this.lineGetters.size();
		 
		this.tipMsgFun = tipMsgFun;
		 
		this.sort();
		this.calXTickSize();
		this.calYTickSize();
		this.draw();
	}
}