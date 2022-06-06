/*
 *This class represents a chart with
 * the planetary & house positions. */

import swisseph.*;

import java.util.*;


import swisseph.SDate;

import static swisseph.SweConst.*;

public class Chart {
    private String birthName;
    private double birthLat;
    private double birthLon;
    private SweDate birthDate;
    public static SwissEph sw = new SwissEph();
    private HashMap<Object, ArrayList> planetsHousesMap = new HashMap<>();
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

    static HashMap nakshatras = new HashMap<>();

    static {
        nakshatras.put(1, "Aśvinī");
        nakshatras.put(2, "Bharaṇī");
        nakshatras.put(3, "Kṛttikā");
        nakshatras.put(4, "Rohiṇī");
        nakshatras.put(5, "Mṛgaśirā");
        nakshatras.put(6, "Ārdrā");
        nakshatras.put(7, "Punarvasū");
        nakshatras.put(8, "Puṣya");
        nakshatras.put(9, "Āśleṣā");
        nakshatras.put(10, "Maghā");
        nakshatras.put(11, "Pūrvaphalgunī");
        nakshatras.put(12, "Uttaraphalgunī");
        nakshatras.put(13, "Hasta");
        nakshatras.put(14, "Cittā");
        nakshatras.put(15, "Svāti");
        nakshatras.put(16, "Viśākhā");
        nakshatras.put(17, "Anurādhā");
        nakshatras.put(18, "Jyeṣṭhā");
        nakshatras.put(19, "Mūlā");
        nakshatras.put(20, "Pūrvāṣāḍhā");
        nakshatras.put(21, "Uttarāṣāḍhā");
        nakshatras.put(22, "Śravaṇā");
        nakshatras.put(23, "Dhaniṣṭhā");
        nakshatras.put(24, "Śatabhiṣā");
        nakshatras.put(25, "Pūrvābhādrā");
        nakshatras.put(26, "Uttarābhādrā");
        nakshatras.put(27, "Revatī");
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
    public AbstractMap<Object, ArrayList> calcPlanetAndHousePositions() {
        //Setup preparers for swe_calc_ut()
        double[] od = new double[6];
        StringBuffer sb = new StringBuffer();

        //convert the birthdate to Julian
        double jd = birthDate.getJulDay();
        double deltaT = birthDate.getDeltaT();
        double curr_jd = jd + deltaT;

        /**/
        /*calculate planetary positions*/
        /**/
        boolean isRetrogade = false;

        for (int i = 0; i < 11; i++) {
            //set the speed flag (for lonspeed)
            sw.swe_calc_ut(curr_jd, i, SEFLG_TRUEPOS | SEFLG_SPEED, od, sb);
            double obj_lon = od[0];
            double lon_speed = od[3];
            //check if planet is retrogade
            if (lon_speed < 0) {
                isRetrogade = true;
            }

            //initialize vars to store [d,m,s,sign] values
            IntObj ideg = new IntObj();
            IntObj imin = new IntObj();
            IntObj isec = new IntObj();
            DblObj dsecfr = new DblObj();
            IntObj isgn = new IntObj();

            //Convert lon to d,m,s,sign
            sl.swe_split_deg(obj_lon, SweConst.SE_SPLIT_DEG_ROUND_MIN | SweConst.SE_SPLIT_DEG_ZODIACAL, ideg, imin, isec, dsecfr, isgn);

            //Store planet details in a local list
            ArrayList<Object> plList = new ArrayList<Object>();
            //plList.add((sw.swe_get_planet_name(i)).toLowerCase());
            plList.add(obj_lon);
            plList.add(ideg.val + "\"" + imin.val + "'" + isec.val);
            plList.add(zodiacSigns.get(isgn.val));
            plList.add(isRetrogade);

            //Now store it in the hashmap
            planetsHousesMap.put((sw.swe_get_planet_name(i)).toLowerCase(), plList);
            //System.out.println(sw.swe_get_planet_name(i) + " " + ideg.val + " " + imin.val + " " + isec.val +" "+zodiacSigns.get(isgn.val));
        }
        //calc house cusps
        double[] house_cusps = new double[13];
        double[] ascmc_data = new double[10];
        sw.swe_houses(curr_jd, 0, birthLat, birthLon, 'P', house_cusps, ascmc_data);

        //store asc and other results,
        //iterate over results and get house cusp d,m,s
        double asc = ascmc_data[0];
        double mc = ascmc_data[1];
        for (int j = 1; j < 13; j++) {

            //initialize outputs vars to store split d,m,s
            IntObj ideg1 = new IntObj();
            IntObj imin1 = new IntObj();
            IntObj isec1 = new IntObj();
            DblObj dsecfr1 = new DblObj();
            IntObj isgn1 = new IntObj();

            sl.swe_split_deg(house_cusps[j], SweConst.SE_SPLIT_DEG_ROUND_MIN | SweConst.SE_SPLIT_DEG_ZODIACAL, ideg1, imin1, isec1, dsecfr1, isgn1);

            //Store house cusp details in a local list
            ArrayList<Object> hList = new ArrayList<Object>();
            hList.add(house_cusps[j]);
            hList.add(ideg1.val + "\"" + imin1.val + "'" + isec1.val);
            hList.add(zodiacSigns.get(isgn1.val));
            //we don't care if house is retrogade
            hList.add(false);
            //store house data into map
            planetsHousesMap.put("house" + j, hList);
        }

        System.out.println(planetsHousesMap);
        for (int i = 0; i < 11; i++) {
            getNakshatraOfPlanet(i, 0);
        }

        return planetsHousesMap;
    }

    /*Returns the Nakshatra and pada for a given planet*/
    /*supports lahiri ayanamsa, */
    /*if no valid ayanamsa is given, calculations are tropical*/

    public int getNakshatraOfPlanet(int planet_name, int ayanamsa_type) {

        //Setup vars for swe_calc_ut
        double[] od = new double[6];
        StringBuffer sb = new StringBuffer();
        //convert the birthdate to Julian
        double jd = birthDate.getJulDay();
        double deltaT = birthDate.getDeltaT();
        double curr_jd = jd + deltaT;

        //calculate planet's position depending on ayanamsa
        if (ayanamsa_type == SE_SIDM_LAHIRI) {
            sw.swe_set_sid_mode(SweConst.SE_SIDM_LAHIRI);
            sw.swe_calc_ut(jd, planet_name, SEFLG_SIDEREAL, od, sb);

        } else {
            sw.swe_calc_ut(curr_jd, planet_name, SEFLG_TRUEPOS | SEFLG_SPEED, od, sb);
        }
        //There are 27 nakshatras, so each nakshatra is
        double one_nak = 360.00 / 27;
        //Number of nakshatras elapsed
        int naks_elapsed = (int) (od[0] / one_nak);
        int curr_nakshatra = (naks_elapsed + 1) % 27;

        System.out.println("Calculating nakshatra for " + sw.swe_get_planet_name(planet_name) + " at longitude: " + od[0] + " nakshatra is:" + nakshatras.get(curr_nakshatra));

//        plList.add(obj_lon);
//        plList.add(ideg.val + "\"" + imin.val + "'" + isec.val);
//        plList.add(zodiacSigns.get(isgn.val));


        return 1;
    }

}
