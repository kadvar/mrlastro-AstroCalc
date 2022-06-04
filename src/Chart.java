/*
 *This class represents a chart with
 * the planetary & house positions. */

import swisseph.*;

import java.util.HashMap;
import static swisseph.SweConst.SEFLG_TRUEPOS;

public class Chart {
    private String birthName;
    private double birthLat;
    private double birthLon;
    private SweDate birthDate;
    public static SwissEph sw = new SwissEph();
    private HashMap planetsHousesMap = new HashMap<>();
    private SwissLib sl = new SwissLib();

    public double getBirthLat() {
        return birthLat;
    }

    public void setBirthLat(double birthLat) {
        this.birthLat = birthLat;
    }

    public double getBirthLon() {
        return birthLon;
    }

    public void setBirthLon(double birthLon) {
        this.birthLon = birthLon;
    }

    public SweDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(SweDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthName() {
        return birthName;
    }

    public void setBirthName(String birthName) {
        this.birthName = birthName;
    }

    /* Populates planet and house positions into hashmap */
    public void calcPlanetAndHousePositions() {
        //Setup preparers for swe_calc_ut()
        double[] od = new double[6];
        StringBuffer sb = new StringBuffer();

        //convert the birthdate to Julian
        double jd = birthDate.getJulDay();
        double deltaT = birthDate.getDeltaT();
        double curr_jd = jd + deltaT;

        //calc planetary positions
        for (int i = 0; i < 11; i++) {
            sw.swe_calc_ut(jd, i, SEFLG_TRUEPOS, od, sb);
            double obj_lon = od[0];
            //Store in HashMap
            planetsHousesMap.put(sw.swe_get_planet_name(i), obj_lon);

            IntObj ideg = new IntObj();
            IntObj imin = new IntObj();
            IntObj isec = new IntObj();
            DblObj dsecfr = new DblObj();
            IntObj isgn = new IntObj();

            //Display in d,m,s
            sl.swe_split_deg(obj_lon, SweConst.SE_SPLIT_DEG_ROUND_MIN|SweConst.SE_SPLIT_DEG_ZODIACAL, ideg, imin, isec, dsecfr, isgn);
            System.out.println(sw.swe_get_planet_name(i)+" "+ideg.val+" "+imin.val+" "+isec.val+" sign"+isgn.val);

        //calc house cusps
            double[] house_cusps = new double[13];
            double[] ascmc_data = new double[10];
            sw.swe_houses(curr_jd, 0, birthLat, birthLon, 'P', house_cusps, ascmc_data);

            //store asc and other results
            double asc = ascmc_data[0];
            double mc = ascmc_data[1];

            //get all house cusps
            for (int j = 1; j < 13; j++) {
                //System.out.println("House " + j + ":" + house_cusps[j]);
                planetsHousesMap.put("house"+(j), house_cusps[j]%30);
            }
        }
        System.out.println(planetsHousesMap);
    }
}
