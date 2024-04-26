package yuzhou.gits.graph;

public class Bounds<T> {
	double x;
	double y;
	double w;
	double h;
	double[] gaps = {0,0,0,0};
	T id;
	BoundsPropsListener listener;
	public void setListener(BoundsPropsListener listener) {
		this.listener = listener;
	}
	public Bounds(T id,double x, double y, double w, double h, double[] gaps) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		if(gaps == null || gaps.length != 4) {
			throw new IllegalArgumentException("gaps length MUST be equals to 4!");
		}
		if(listener != null && this.w > 0 && this.h > 0) {
			this.listener.onBoundPropsChanged(this);
		}
//		if(gaps[0]+gaps[2] >= this.w) {
//			throw new IllegalArgumentException("left gap(gap[0]) plus right gap(gap[2) MUST be less than width!");
//		}
//		if(gaps[0]+gaps[2] >= this.h) {
//			throw new IllegalArgumentException("top gap(gap[1]) plus bottom gap(gap[3) MUST be less than height!");
//		}
		this.gaps = gaps;
	}
 
	public void setBoundsRect(double x,double y,double w,double h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		if(listener != null && this.w > 0 && this.h > 0) {
			this.listener.onBoundPropsChanged(this);
		}
	}
	public void setBoundsRectAndGaps(double x,double y,double w,double h,double[] gaps) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.gaps = gaps;
		if(listener != null && this.w > 0 && this.h > 0) {
			this.listener.onBoundPropsChanged(this);
		}
	}
	public double[] getGaps() {
		return gaps;
	}

	public void setGaps(double[] gaps) {
		this.gaps = gaps;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getWidth() {
		return w;
	}

	public double getHeight() {
		return h;
	}
	
	public double getLeftGap() {
		return this.gaps[0];
	}
	
	public double getRightGap() {
		return this.gaps[2];
	}
	
	public double getTopGap() {
		return this.gaps[1];
	}
	
	public double getBottomGap() {
		return this.gaps[3];
	}

	public double getContentWidth() {
		return this.w - this.gaps[0] - this.gaps[2];
	}

	public double getContentHeight() {
		return this.h - this.gaps[1] - this.gaps[3];
	}

	public double getContentX() {
		return this.x + this.gaps[0];
	}

	public double getContentY() {
		return this.y + this.gaps[1];
	}
	public T getId() {
		return this.id;
	}
	public void setId(T id) {
		this.id = id;
	}
	 
	public void setDim(double w,double h) {
		this.w = w;
		this.h = h;
		if(listener != null && this.w > 0 && this.h > 0) {
			this.listener.onBoundPropsChanged(this);
		}
	}
	public double getMaxY() {
		return this.y+this.h;
	}
	public double[] mapCoorInBounds(double x,double y) {
		double[] coorInBounds = {-1.0,-1.0};
		coorInBounds[0] = x < this.getContentX() || x > this.getContentX() + this.getContentWidth() ? -1 : x - this.getContentX();
		coorInBounds[1] = y < this.getContentY() || y > this.getContentY() + this.getContentHeight() ? -1 : y - this.getContentY();
		return coorInBounds;
	}
	public String toString() {
		return "id="+this.id+",x="+this.x+",y="+this.y+",w="+this.w+",h="+this.h+",gaps={"+gaps[0]+","+gaps[1]+","+gaps[2]+","+gaps[3]+"}";
	}
	public boolean equals(Bounds<T> other) {
		return this.id == other.id;
	}

	public double getMaxX() {
		return this.x+this.w;
	}
	
	public interface BoundsPropsListener {
		public void onBoundPropsChanged(Bounds<?> bounds);
	}
}
