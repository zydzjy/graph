package yuzhou.gits.graph;

import java.util.List;

public interface GraphObjectList<G> {
	public List<G>[] getGroup(int group);
	public void init(int groups);
	public void add(int pIdx);
	public void addGraphObject(G graphObject,int pIdx,int sIdx);
	public void clearGroup(int group,int items);
	public void put(int group, List<G>[] overlaps);
	public void clear();
}