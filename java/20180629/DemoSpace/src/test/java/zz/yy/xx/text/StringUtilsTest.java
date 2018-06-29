package zz.yy.xx.text;

import java.util.Arrays;

import org.junit.Test;

public class StringUtilsTest {
	
	
	private static Boolean checkLocationBounds(double[] location,String[] bounds) {
        if (bounds == null || bounds.length < 4) {
            return null;
        }

        double[] values = new double[4];
        values[0] = Double.valueOf(bounds[0]);
        values[1] = Double.valueOf(bounds[1]);
        values[2] = Double.valueOf(bounds[2]);
        values[3] = Double.valueOf(bounds[3]);

        
        System.err.println("--------------------------------1");
        /*|大于最大纬度|*/
        if (location[0]> Math.max(values[0], values[2])) {
            return Boolean.FALSE;
        }
        System.err.println("--------------------------------2");
        /*|大于最大经度|*/
        if (location[1] > Math.max(values[1], values[3])) {
            return Boolean.FALSE;
        }

        System.err.println("--------------------------------3");
        /*|小于最小纬度|*/
        if (location[0] < Math.min(values[0], values[2])) {
            return Boolean.FALSE;
        }
        
        System.err.println("--------------------------------4");
        /*|小于最小经度|*/
        if (location[1] < Math.min(values[1], values[3])) {
            return Boolean.FALSE;
        }

        /*|否则成功|*/
        return Boolean.TRUE;
    }
	
	@Test
	public void demo01() throws Exception {
		String[] result;
		
		
		System.err.println("===========================");
		System.err.println("===========================");

		String regex = "(\\d+(?:\\.\\d+)?)";

		String source = "30.6320530000,121.0024700000|31.7033910000,121.9701250000";

		result = StringUtils.findString(source, regex);

		System.err.println(Arrays.toString(result));
		
		
		double[] location= new double[]{31.1655140000,120.8133550000};
		
		if(checkLocationBounds(location,null)!=Boolean.FALSE) {
			System.err.println("=====================#");
		}else {
			System.err.println("=====================$");
		}
		
		System.err.println("===========================");
		System.err.println("===========================");
	}

}
