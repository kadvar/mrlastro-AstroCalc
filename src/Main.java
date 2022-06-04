import swisseph.*;
import static swisseph.SweConst.*;

public class Main {
    public static void main(String[] args) {
        SwissEph sw = new SwissEph();

        //Get today's date and convert to julDay
        SweDate sd = new SweDate();

        //Testing getObjLon
        double curr_jd = sd.getJulDay();
        getObjLon(0, sd.getJulDay());

        //Print all planetary positions
        for (int i=0; i<11; i++) {
            double my_obj_lon = getObjLon(i, curr_jd) % 30;
            System.out.println(sw.swe_get_planet_name(i)+" "+my_obj_lon);
        }
    }

    //Gets a specified object's longitude
    private static double getObjLon(int obj, double jd) {

        //Setup preparers for swe_calc_ut()
        SwissEph sw = new SwissEph();
        double[] obj_data = new double [6];
        StringBuffer sb = new StringBuffer();

        //calc position and store in obj_lon

        sw.swe_calc_ut(jd, obj, SEFLG_TRUEPOS, obj_data, sb);
//        sw.swe_set_sid_mode(SweConst.SE_SIDM_LAHIRI);
//        sw.swe_calc_ut(jd, obj, SEFLG_SIDEREAL|SE_SPLIT_DEG_KEEP_SIGN, obj_data, sb);

        //return obj lon only
        //System.out.println("Returning obj lon: "+obj_data[0]);
        return obj_data[0];
    }

}