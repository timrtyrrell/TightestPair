import java.awt.geom.Point2D;
import java.util.ArrayList;

public class CurrentTightest {
	
	private double d;
	private ArrayList<Point2D.Double> values;
	
	public CurrentTightest(double distance, ArrayList<Point2D.Double> vals) {
		d = distance;
		values = vals;
	}
	
	
	public double getD() {
		return d;
	}
	
	public ArrayList<Point2D.Double> getValues() {
		return values;
	}
}
