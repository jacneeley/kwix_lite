package local.payrollapp.simplepayroll.utility;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import local.payrollapp.simplepayroll.GlobalConstants;
import local.payrollapp.simplepayroll.view.AppController;

@Component
public class PageUtility {
	private LinkedList<String> pageNav = new LinkedList<String>();
	private List<Object> currentNode;
	private List<Object> prevNode;
	
	private static final Logger log = LoggerFactory.getLogger(PageUtility.class);
		
	/**
	 * @return the current node
	 */
	public String getCurr() {
		return this.currentNode.get(1).toString();
	}

	/**
	 * @param curr the curr to set
	 */
	public void setCurr(int loc ,String curr) {
		pageNav.add(curr);
		currentNode = node(loc, curr);
	}
	
	/**
	 * @return the previous node
	 */
	public String getPrev() {
		return this.prevNode.get(1).toString();
	}

	/**
	 * @param prev the prev to set
	 */
	public void setPrev(int loc, String prev) {
		if(loc >= 0) {
			prevNode = node(loc - 1, prev);
		}
		else {
			//fallback
			prevNode = node(0, prev);
		}
	}
	
	private void insert(String page) {
		if(this.pageNav.size() < 1) {
			setCurr(0, page);
		}
		else {
			//page will become curr and curr will become prev
			int loc = pageNav.size();
			String prev = currentNode.get(1).toString();
			setPrev(loc, prev);
			setCurr(loc, page);
		}
	}
	
	private void remove(int loc) {
		try {
			if(loc == -1) {
				prevNode = this.prev(prevNode);
				currentNode = this.prev(currentNode);
				pageNav.removeLast();
			}
			else if(loc > -1) {
				pageNav.remove(loc);
			}
		} catch(IndexOutOfBoundsException  ex) {
			ex.printStackTrace();
			log.error("Index Error in remove()");
		}
	}

	/**
	 * find last index of url and remove it.
	 * @param url
	 */
	public void removeByUrl(String url) {
		int i = pageNav.lastIndexOf(url);
		if(i != -1) {
			this.remove(i);
		}
	}
	
	public void visit(String page) {
		this.insert(page);
	}
  
	/**
	 * go to the previous page and re-assign pageNav nodes.
	 * @return prev page string
	 * @throws IndexOutOfBoundsException
	 */
	public String moveBackToPrev() throws IndexOutOfBoundsException {
		String curr = getCurr();
		int prevPrevLoc = (int) this.prev(prevNode).get(0);
		String prevPrev = pageNav.get(prevPrevLoc);
		
		if(curr.equals(prevPrev)) {
			prevPrevLoc = pageNav.indexOf(prevPrev);
			return pageNav.get(prevPrevLoc - 1);
		}
		
		int target = pageNav.indexOf(curr) - 1;
		return this.prevBs(target);
	}
		
	/**
	 * create a new node object
	 * @param loc
	 * @param page
	 * @return new node
	 */
	private List<Object> node(int loc, String page){
		return Arrays.asList(loc, page);
	}
	
	/**
	 * clear the page navigation tool.
	 */
	public void clear() {
		this.pageNav.clear();
	}
	
	/**
	 * get the length of pageNav
	 * @return size as int
	 */
	public int length() {
		return pageNav.size();
	}
	
	private List<Object> next(List<Object> node) throws IndexOutOfBoundsException {
		int next = (int) node.get(0) + 1;
		
		if(next <= pageNav.size() - 1) {
			String nodeStr = pageNav.get(next);
			return node(next, nodeStr);
		}
		else
		{
			//fallback
			log.warn("Fallback occurred on setting next node.");
			return node(next, GlobalConstants.NONE);
		}
	}
	
	private List<Object> prev(List<Object> node) throws IndexOutOfBoundsException {
		int prev = (int) node.get(0) -1;
		
		if(prev >= 0) {
			String nodeStr = pageNav.get(prev);
			return node(prev, nodeStr);
		}
		else {
			//fallback
			log.warn("Fallback occurred on setting prev node.");
			return prevNode;
		}
	}
	
	/**
	 * @param target
	 * @return getPrev()
	 */
	private String prevBs(int target) {
		//good enough...
		int l = 0;
		int r = pageNav.size() - 2;
		while(l <= r) {
			int m = (l + r) / 2;
			
			if(m == target) {
				String returnTo = pageNav.get(m);
				prevNode = node(m, returnTo);
				return getPrev();
			}
			else if(target != m && r > m) {
				r = m - 1;
			}
			else {
				l = m + 1;
			}
		}
		
		prevNode = node(target,getPrev());
		return getPrev();
	}
}