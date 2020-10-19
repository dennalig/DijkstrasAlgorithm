
// see http://www.movable-type.co.uk/scripts/latlong.html
public class ConvertGPSCoordinantes {
// calcDist take two latitude / longitude pairs and returns the
// distance between them in KILOMETERS
	double calcDist(double lat1, double lon1, double lat2, double lon2){
		double R = 6371; //km
		double radLat1 = Math.toRadians(lat1);
		double radLat2 = Math.toRadians(lat2);
		double radDiffLat = Math.toRadians(lat2-lat1);
		double radDiffLon = Math.toRadians(lon2-lon1);
		
		double a = Math.sin(radDiffLat/2) * Math.sin(radDiffLat/2) +
				Math.cos(radLat1) * Math.cos(radLat2) *
				Math.sin(radDiffLon/2) * Math.sin(radDiffLon/2);
		double c = 2 * Math.atan2(Math.sqrt(a),  Math.sqrt(1-a));
		
		return R * c;
	}
	public ConvertGPSCoordinantes() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ConvertGPSCoordinantes convertGPSCoordinantes = new ConvertGPSCoordinantes();
		System.out.println(convertGPSCoordinantes.calcDist(33.796676,-84.394168,35.004133,-85.214009 ));
		System.out.println(convertGPSCoordinantes.calcDist(35.004133, -85.214009,34.971162, -85.449792 ));

	}

}
