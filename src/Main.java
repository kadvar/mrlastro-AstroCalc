import swisseph.*;
import static swisseph.SweConst.*;

public class Main {

    public static void main(String[] args) {

        //Initialize a new chart object
        Chart chart1 = new Chart();

        //initialize parameters
        int year = 2022;
        int month = 6;
        int day = 5;
        int hour = 12;
        double minute = 9;
        double sec = 0;
        double lat = 12.9716;
        double lon = 77.5946;
        double tz = 5.5;
        String birthName = "Mastergaru";

        //Create SweDate object
        SweDate sd = new SweDate(year, month, day, hour + (minute / 60), true);
        System.out.println("Local time is: "+sd.getHour());
        //convert to UTC time
        //double sd_utc_hour = sd.getHour() + sd.getDeltaT();

        /*UTC Conversion*/
        SDate sdate_utc = sd.getUTCFromLocalTime(year, month, day, hour, (int) minute, sec, tz);
        System.out.println(sdate_utc.hour);
//        System.out.println("UTC Time is: "+sdate_utc.+":"+sdate_utc.minute);

        //Set chart parameters and calculate chart
        chart1.setBirthDate(sd);
        chart1.setBirthLon(lon);
        chart1.setBirthLat(lat);
        chart1.setBirthName(birthName);

        //populate planetary positions
        chart1.calcPlanetAndHousePositions();
    }
}

    //    private static ArrayList<String> planetsList = new ArrayList<String>();
//    public static void setPlanetsList() {
//        planetsList.add("Sun");
//        planetsList.add("Moon");
//    }
    //Set Latitude & Longitude
//    public static double place_lat = 12.9716;
//    public static double place_lon = 77.5946;

    //Get today's date and convert to julday
//    static int hours = 14;
//    static double minutes = 46;
//    static SweDate sd = new SweDate(2022, 6, 4, hours + (minutes / 60), true);
//    public static double jd = sd.getJulDay();
//    public static double deltaT = sd.getDeltaT();
//    public static double curr_jd = jd + deltaT;
//
//    public static SwissEph sw = new SwissEph();

    //Main method begins here


        //Calculate house positions
//        double[] house_cusps = new double[13];
//        double[] ascmc_data = new double[10];
//        sw.swe_houses(curr_jd, 0, place_lat, place_lon, 'P', house_cusps, ascmc_data);
//
//        //store asc and other results
//        double asc = ascmc_data[0];
//        double mc = ascmc_data[1];
//
//        //get house cusps
//        for (int i = 1; i < 13; i++) {
//            System.out.println("House " + i + ":" + house_cusps[i] % 30);
//        }

    //Gets a specified object's longitude
//    private static double getObjLon(int obj, double jd) {
//        //Setup preparers for swe_calc_ut()
//        double[] obj_data = new double[6];
//        StringBuffer sb = new StringBuffer();
//
//        //calc position and store in obj_lon
//        sw.swe_calc_ut(jd, obj, SEFLG_TRUEPOS, obj_data, sb);
////        sw.swe_set_sid_mode(SweConst.SE_SIDM_LAHIRI);
////        sw.swe_calc_ut(jd, obj, SEFLG_SIDEREAL|SE_SPLIT_DEG_KEEP_SIGN, obj_data, sb);
//        //return obj lon only
//        //System.out.println("Returning obj lon: "+obj_data[0]);
//        return obj_data[0];




