package zz.yy.xx.loc;

public class LocationTest {
	 
    
		/**
		 * 根据经纬度偏移计算出实际的行驶距离
		 * 
		 * @param latitude		新的纬度
		 * @param longitude 	新的经度
		 * @param oldLatitude 	上一次纬度
		 * @param oldLongitude 	上一次经度
		 * @return 偏移距离(单位:米)
		 */
		public static double algorithmDistance(double latitude, double longitude,
				double oldLatitude, double oldLongitude) {

			double lat1 = Math.toRadians(longitude);
			double lat2 = Math.toRadians(oldLongitude);

			double p = Math.cos(lat1) * Math.cos(lat2);

			double y = lat1 - lat2;
			double x = Math.toRadians(latitude) - Math.toRadians(oldLatitude);

			double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(y / 2), 2) 
					+ p * Math.pow(Math.sin(x / 2), 2)));

			distance *= 6378137.0;

			return Math.round(distance * 10000D) / 10000D;
		}
		
		private long mLastRecordTime;
		
		
		/**
		 * 计算速度
		 * @param distance 当前地理偏移
		 * @param currentTime 当前时间(毫秒时间戳)
		 * @return 时速(单位: m/s)
		 */
		public float algorithmSpeed(double distance, long currentTime) {
			float speed = -1.0F;
			/*|利用上一次记录时间得出时间差|*/
			long time = (mLastRecordTime - currentTime) / 1000L;

			speed = (float) (distance / time);

			mLastRecordTime = currentTime;

			return speed;

		}
		
}
