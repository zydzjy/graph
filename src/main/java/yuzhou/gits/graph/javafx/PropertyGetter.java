package yuzhou.gits.graph.javafx;

public interface PropertyGetter<P,M> {

	public Comparable<P> get(M model);
}
