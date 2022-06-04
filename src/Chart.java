/*
 *This class represents a chart with
 * the planetary & house positions. */

import swisseph.SweDate;
import swisseph.SwissEph;

import static swisseph.SweConst.SEFLG_TRUEPOS;

public class Chart {
    private String birthName;
    private double birthLat;
    private double birthLon;
    private SweDate birthDate;
    public static SwissEph sw = new SwissEph();

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

    public void calcPlanetAndHousePositions() {
        //Setup preparers for swe_calc_ut()
        double[] od = new double[6];
        StringBuffer sb = new StringBuffer();

        //convert the birthdate to Julian
        double jd = birthDate.getJulDay();
        double deltaT = birthDate.getDeltaT();
        double curr_jd = jd + deltaT;

        //calc planetary positions and store in obj_lon
        for (int i = 0; i < 11; i++) {
            sw.swe_calc_ut(jd, i, SEFLG_TRUEPOS, od, sb);
            double obj_lon = od[0];
            //Store in hash
            System.out.println(sw.swe_get_planet_name(i) + " " + obj_lon % 30);
        }
    }
}
