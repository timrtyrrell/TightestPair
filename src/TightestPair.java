import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;



public class TightestPair {

    /**
     * This is the method that should set off the computation
     * of closest pair.  It takes as input a list lines of input
     * as strings.  You should parse that input and then call a
     * subroutine that you write to compute the closest pair distance
     * and return that value from this method
     *
     * @return the distance between the closest pair 
     */
    public double findTightest(List<String> fileData) {
    	 
    	ArrayList<Point2D.Double> pts = new ArrayList<Point2D.Double>();
    	

    	
    	for(String s : fileData) {
    		String[] strs = s.split(" ", 2);

    		double x = Double.valueOf(strs[0]);
    		double y = Double.valueOf(strs[1]);
    		
    		Point2D.Double p = new Point2D.Double(x, y);
    		pts.add(p);
    		
    	}
    	

    	pts = sortByX(pts);
    	    	
    	CurrentTightest min = calculate(pts);
    	
    	double retVal = min.getD();
        return retVal;
    }
    
    private CurrentTightest calculate(ArrayList<Point2D.Double> list) {
    	// need base case
    	
    	if(list.size() < 3) {
    		// sort list by y
    		ArrayList<Point2D.Double> newList = new ArrayList<Point2D.Double>(sortByY(list));  
    		CurrentTightest r = new CurrentTightest(list.get(0).distance(list.get(1)), newList);
    		return r;
    	}
    	
    	if(list.size() == 3) {
    		// sort list by y
    		// find delta
    		double a = list.get(0).distance(list.get(1));
    		double b = list.get(1).distance(list.get(2));
    		double c = list.get(0).distance(list.get(2));

    		double delta = Math.min(Math.min(a, b), c);
    		
    		ArrayList<Point2D.Double> newList = new ArrayList<Point2D.Double>(sortByY(list));  
    		CurrentTightest r = new CurrentTightest(delta, newList);
    		return r;
    	}
    	    	
    	double medianX = list.get((list.size()/2) + 1).getX();
    	CurrentTightest left = calculate(new ArrayList<Point2D.Double>(list.subList(0, list.size()/2)));
    	CurrentTightest right = calculate(new ArrayList<Point2D.Double>(list.subList(list.size()/2, list.size())));
    	double delta = Math.min(left.getD(), right.getD());
    	
    	// merge our new ones
    	
    	ArrayList<Point2D.Double> sorted = merge(left.getValues(), right.getValues());
    	ArrayList<Point2D.Double> deltaList = new ArrayList<Point2D.Double>();  
    	
    	for(int i = 0; i < sorted.size(); i++) {
    		if(Math.abs(sorted.get(i).getX() - medianX) <= delta) {
    			deltaList.add(sorted.get(i));
    		}
    	}
    	
    	for(int i = 0; i < deltaList.size(); i++) {
    		for(int j = 0; j < 15; j++) {
    			if(j < deltaList.size()) {
        			if((deltaList.get(i).distance(deltaList.get(j)) < delta) && !(i == j)) {
        				delta = deltaList.get(i).distance(deltaList.get(j));
        			}
    			}
    		}
    	}
    	
    	for(int i = 0; i < deltaList.size(); i++) {
    		for(int j = i + 1; (j < deltaList.size()) && (j < i + 16); j++) {
    			if((deltaList.get(i).distance(deltaList.get(j)) < delta)) {
    				delta = deltaList.get(i).distance(deltaList.get(j));
    			}
    		}
    	}
    	
    	CurrentTightest r = new CurrentTightest(delta, sorted);
    	return r;

    	
    }
    
    
    private ArrayList<Point2D.Double> sortByX(ArrayList<Point2D.Double> list) {
    	Collections.sort(list, new Comparator<Point2D.Double>() {

    	public int compare(Point2D.Double o1, Point2D.Double o2) {
    	    if(o1.getX() < o2.getX()) {
    	    	return -1;
    	    } else {
    	    	return 1;
    	    }
    	}
    	});
    	
    	return list;
    }
    
    private ArrayList<Point2D.Double> sortByY(ArrayList<Point2D.Double> list) {
    	Collections.sort(list, new Comparator<Point2D.Double>() {

    	public int compare(Point2D.Double o1, Point2D.Double o2) {
    	    if(o1.getY() < o2.getY()) {
    	    	return -1;
    	    } else {
    	    	return 1;
    	    }
    	}
    	});
    	
    	return list;
    }
    
    private ArrayList<Point2D.Double> merge(ArrayList<Point2D.Double> left, ArrayList<Point2D.Double> right) {
    	
    	ArrayList<Point2D.Double> sorted = new ArrayList<Point2D.Double>();
    	
    	while(!(left.isEmpty() && right.isEmpty())) {
    		if(left.isEmpty()) {
    			sorted.addAll(right);
    			right.clear();
    		} else if(right.isEmpty()) {
    			sorted.addAll(left);
    			left.clear();
    		} else if(left.get(0).getY() < right.get(0).getY()) {
    			sorted.add(left.get(0));
    			left.remove(0);
    		} else {
    			sorted.add(right.get(0));
    			right.remove(0);
    		}
    	}
    	
    	return sorted;
    }
    
    public static void main(String[] args) {
    	List<String> data = new ArrayList<>();
    	
    	File file = new File(args[0]);
    	

    	try(BufferedReader br = new BufferedReader(new FileReader(file))){
    		String str; 
    		while ((str = br.readLine()) != null) {
    			data.add(str);
    		} 
    		
    		Long start = System.currentTimeMillis();
            TightestPair tp = new TightestPair();
			System.out.println(tp.findTightest(data));
            Long end = System.currentTimeMillis();
            System.out.println("time: " + ((end - start) / 1000.0));
    		
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error occurred when reading file");
		}
    }

}
