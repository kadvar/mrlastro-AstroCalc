/*
 *This class represents a chart with
 * the planetary & house positions. */

import swisseph.*;
import java.util.HashMap;
import static swisseph.SweConst.SEFLG_TRUEPOS;
import swisseph.SDate;

public class Chart {
    private String birthName;
    private double birthLat;
    private double birthLon;
    private SweDate birthDate;
    public static SwissEph sw = new SwissEph();
    private HashMap planetsHousesMap = new HashMap<>();
    private SwissLib sl = new SwissLib();

    static HashMap zodiacSigns = new HashMap<>();
    static {
        zodiacSigns.put(0, "aries");
        zodiacSigns.put(1, "taurus");
        zodiacSigns.put(2, "gemini");
        zodiacSigns.put(3, "cancer");
        zodiacSigns.put(4, "leo");
        zodiacSigns.put(5, "virgo");
        zodiacSigns.put(6, "libra");
        zodiacSigns.put(7, "scorpio");
        zodiacSigns.put(8, "sagittarius");
        zodiacSigns.put(9, "capricorn");
        zodiacSigns.put(10, "aquarius");
        zodiacSigns.put(11, "pisces");
    }

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
            sl.swe_split_deg(obj_lon, SweConst.SE_SPLIT_DEG_ROUND_MIN | SweConst.SE_SPLIT_DEG_ZODIACAL, ideg, imin, isec, dsecfr, isgn);
            System.out.println(sw.swe_get_planet_name(i) + " " + ideg.val + " " + imin.val + " " + isec.val +" "+zodiacSigns.get(isgn.val));
        }
        //calc house cusps
        double[] house_cusps = new double[13];
        double[] ascmc_data = new double[10];
        sw.swe_houses(curr_jd, 0, birthLat, birthLon, 'P', house_cusps, ascmc_data);

        //store asc and other results
        double asc = ascmc_data[0];
        double mc = ascmc_data[1];

        //Calculate all house cusps
        for (int j = 1; j < 13; j++) {

            //initialize outputs vars to store split d,m,s
            IntObj ideg1 = new IntObj();
            IntObj imin1 = new IntObj();
            IntObj isec1 = new IntObj();
            DblObj dsecfr1 = new DblObj();
            IntObj isgn1 = new IntObj();

            //System.out.println("House " + j + ":" + house_cusps[j]);
            planetsHousesMap.put("house" + (j), house_cusps[j] % 30);
            sl.swe_split_deg(house_cusps[j], SweConst.SE_SPLIT_DEG_ROUND_MIN | SweConst.SE_SPLIT_DEG_ZODIACAL, ideg1, imin1, isec1, dsecfr1, isgn1);
            System.out.println("House" + j + " " + ideg1.val + " " + imin1.val + " " + isec1.val + " "+zodiacSigns.get(isgn1.val));
        }

        System.out.println(planetsHousesMap);
    }

    public void convertLocalToUTC(int year, int month, int day, double hour) {
        SDate sd = new SDate(2022, 1, 1, 10);
        //SDate sd = new SDate(year, month, day, hour);
    }

}
