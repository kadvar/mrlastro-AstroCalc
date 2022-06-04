import swisseph.*;
import static swisseph.SweConst.*;

public class Main {
    public static void main(String[] args) {
        SwissEph sw = new SwissEph();

        //Convert date to julDay
        SweDate sd = new SweDate();

        //get planetary positions
        double[] planet_lon = new double [6];
        StringBuffer sb = new StringBuffer();
        sw.swe_calc_ut(sd.getJulDay(), 1, SEFLG_TRUEPOS, planet_lon, sb);
        System.out.println(planet_lon[0] % 30);
    }
}