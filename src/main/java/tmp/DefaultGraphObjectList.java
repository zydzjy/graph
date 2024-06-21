package yuzhou.gits.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultGraphObjectList<G> implements GraphObjectList<G> {
	Map<Integer,List<G>[]> shapeContainer = new HashMap<>();
	@SuppressWarnings("unchecked")
	public void init(int groups) {
		shapeContainer.clear();
		for(int i=0;i<groups;i++) {
			List<G>[] listArr = new List[1];
			listArr[0] = new ArrayList<>();
			shapeContainer.put(i,listArr);
		}
	}
	@Override
	public void add(int pIdx) {
		 
	}
	@Override
	public void addGraphObject(G g,int pIdx, int subIdx) {
		this.shapeContainer.get(pIdx)[subIdx].add(g);
	}

	@Override
	public List<G>[] getGroup(int group) {
		return this.shapeContainer.get(group);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void clearGroup(int group,int items) {
		if(items <=0 ) {
			throw new IllegalArgumentException("At least one item in a group(items="+items+")");
		}
		List<G>[] newArr = new List[items];
		for(int i=0;i<items;i++) {
			newArr[i] = new ArrayList<>();
		}
		this.shapeContainer.put(group, newArr);
	}
	@Override
	public void put(int group, List<G>[] listArr) {
		this.shapeContainer.put(group, listArr);
	}
	@Override
	public void clear() {
		this.shapeContainer.clear();
	}
}
