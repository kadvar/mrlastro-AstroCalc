/*
 *This class represents a chart with
 * the planetary & house positions. */

import swisseph.*;

import java.util.*;


import static swisseph.SweConst.*;

public class Chart {
    private String birthName;
    private double birthLat;
    private double birthLon;
    private SweDate birthDate;
    public static SwissEph sw = new SwissEph();
    private HashMap<Object, ArrayList> planetsHousesMap = new HashMap<>();
    private HashMap<Object, ArrayList> zodiacPlanetsHousesMap = new HashMap<>();
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

    List<String> zodiacSignsArray = Arrays.asList("aries", "taurus", "gemini", "cancer", "leo", "virgo", "libra", "scorpio", "sagittarius", "capricorn", "aquarius", "pisces");
    List<String> oddSigns = Arrays.asList("aries", "gemini", "leo", "libra", "sagittarius", "aquarius");

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

    /**
     * Populates planet and house positions into hashmap
     *
     * @param None:
     * @return
     */
    public AbstractMap<Object, ArrayList> calcPlanetAndHousePositions() {
        //Setup preparers for swe_calc_ut()
        double[] od = new double[6];
        StringBuffer sb = new StringBuffer();
        //convert the birthdate to Julian
        double jd = birthDate.getJulDay();
        double deltaT = birthDate.getDeltaT();
        double currJd = jd + deltaT;
        /**/
        /*calculate planetary & house positions*/
        /**/
        boolean isRetrogade = false;
        for (int i = 0; i < 11; i++) {
            //Calculate Nirayana nakshatras of all planets
            String currNakshatra = (getNakshatra(i, 1)).toLowerCase();
            //set the speed flag (for lonspeed)
            sw.swe_calc_ut(currJd, i, SEFLG_TRUEPOS | SEFLG_SPEED, od, sb);
            double objLon = od[0];
            double lonSpeed = od[3];
            //check if planet is retrogade
            if (lonSpeed < 0) {
                isRetrogade = true;
            }
            //Store planet details in a local list
            ArrayList<Object> plList = new ArrayList<Object>();
            plList = convertToDMSZ(objLon);
            plList.add(currNakshatra);
            plList.add(isRetrogade);
            //Now store it in the hashmap
            planetsHousesMap.put((sw.swe_get_planet_name(i)).toLowerCase(), plList);
            //System.out.println(sw.swe_get_planet_name(i) + " " + ideg.val + " " + imin.val + " " + isec.val +" "+zodiacSigns.get(isgn.val));
        }

        //calc house cusps
        double[] houseCusps = new double[13];
        double[] asmcData = new double[10];
        sw.swe_houses(currJd, 0, birthLat, birthLon, 'P', houseCusps, asmcData);
        for (int j = 1; j < 13; j++) {
            String currNakshatra = (getNakshatra(j, 1)).toLowerCase();
            ArrayList<Object> hList = new ArrayList<Object>();
            hList = convertToDMSZ(houseCusps[j]);
            hList.add(currNakshatra);
            //we don't care if house is retrogade
            hList.add(false);
            //store house data into map
            planetsHousesMap.put("house" + j, hList);
        }
        System.out.println(planetsHousesMap);
        return planetsHousesMap;
    }

    /*Returns longitude in d,m,s,z*/
    public ArrayList convertToDMSZ(double lon) {
        //initialize outputs vars to store split d,m,s
        IntObj ideg1 = new IntObj();
        IntObj imin1 = new IntObj();
        IntObj isec1 = new IntObj();
        DblObj dsecfr1 = new DblObj();
        IntObj isgn1 = new IntObj();
        sl.swe_split_deg(lon, SweConst.SE_SPLIT_DEG_ROUND_MIN | SweConst.SE_SPLIT_DEG_ZODIACAL, ideg1, imin1, isec1, dsecfr1, isgn1);
        //Store house cusp details in a local list
        ArrayList<Object> hList = new ArrayList<Object>();
        hList.add(lon);
        hList.add(ideg1.val + "\"" + imin1.val + "'" + isec1.val);
        hList.add(zodiacSigns.get(isgn1.val));
        return hList;
    }

    /*Returns the Nakshatra and pada for a given planet*/
    /*supports lahiri ayanamsa, */
    /*if no valid ayanamsa is given, calculations are tropical*/
    public String getNakshatra(int planetNum, int ayanamsaType) {
        //Setup vars for swe_calc_ut
        double[] od = new double[6];
        StringBuffer sb = new StringBuffer();
        //convert the birthdate to Julian
        double jd = birthDate.getJulDay();
        double deltaT = birthDate.getDeltaT();
        double currJd = jd + deltaT;

        //calculate planet's position depending on ayanamsa
        if (ayanamsaType == SE_SIDM_LAHIRI) {
            sw.swe_set_sid_mode(SweConst.SE_SIDM_LAHIRI);
            sw.swe_calc_ut(jd, planetNum, SEFLG_SIDEREAL, od, sb);

        } else {
            sw.swe_calc_ut(currJd, planetNum, SEFLG_TRUEPOS | SEFLG_SPEED, od, sb);
        }
        //There are 27 nakshatras, so each nakshatra is
        double oneNak = 360.00 / 27;
        //Number of nakshatras elapsed
        int naksElapsed = (int) (od[0] / oneNak);
        int currNakshatra = (naksElapsed + 1) % 27;
        //calc nakshatra pada
        double onePada = 360.00 / 108;
        int padasElapsed = (int) (od[0] / onePada);
        int currPada = (padasElapsed + 1) % 4;
        //replace pada 0 with 4
        if (currPada == 0) {
            currPada = 4;
        }
        //System.out.println("Calculating nakshatra for " + sw.swe_get_planet_name(planetName) + " at longitude: " + od[0] + " nakshatra is:" + nakshatras.get(currNakshatra)+ "("+currPada+")");
        return nakshatras.get(currNakshatra) + "(" + currPada + ")";
    }

    /*Returns an HashMap object with a D2,D3,D9,D12,D30 calculations*/
    public void getDivisionalCharts() {
        HashMap<String, Object> allObjLonsMap = new HashMap<>();
        HashMap<Object, ArrayList> divChartsMapD2 = new HashMap<>();
        HashMap<Object, ArrayList> divChartsMapD3 = new HashMap<>();
        HashMap<Object, ArrayList> divChartsMapD9 = new HashMap<>();
        //Setup vars for swe_calc_ut
        double[] od = new double[6];
        StringBuffer sb = new StringBuffer();
        //convert the birthdate to Julian
        double jd = birthDate.getJulDay();
        double deltaT = birthDate.getDeltaT();
        double currJd = jd + deltaT;
        //Get Planet and House positions and cusps
        for (int i = 0; i < 11; i++) {
            //Calculate Nirayana nakshatras of all planets
            String currNakshatra = (getNakshatra(i, 1)).toLowerCase();
            //set the speed flag (for lonspeed)
            sw.swe_calc_ut(currJd, i, SEFLG_TRUEPOS | SEFLG_SPEED, od, sb);
            double objLon = od[0];
            String planetName = (sw.swe_get_planet_name(i)).toLowerCase();
            allObjLonsMap.put(planetName, objLon);
        }
        //Calc div charts
        getD2chart(allObjLonsMap);
    }

    //Calculate D2 (Hora) positions
    private HashMap<String, HashSet> getD2chart(HashMap<String, Object> posHash) {
        //declare hashMap to store D2 entries by zodiac sign
        HashMap<String, HashSet> d2Map = new HashMap<>();
        //iterate through obj, lon pairs in hashMap
        for (Map.Entry<String, Object> entry : posHash.entrySet()) {
            String obj = entry.getKey();
            double lon = (double) entry.getValue();
            double lonNorm = lon % 30;
            //retrieve dmsz information for lon into a new list
            ArrayList<Object> dmszList = new ArrayList<Object>();
            dmszList = convertToDMSZ(lon);
            //System.out.println(dmszList);
            //get the zodiac sign
            String zs = dmszList.get(2).toString();
            //create a list to contain objs
            //System.out.println("Key:"+entry.getKey()+" Value:"+entry.getValue());
            if (!obj.equals("sun") && !obj.equals("moon")) {
                //create new hashset to store aries = [pl1, pl2]..etc
                if (oddSigns.contains(zs) && lonNorm <= 15) {
                    //planet is in surya hora
                    obj = "+" + obj;
                    //add this obj (planet) to the d2Map
                    addObjToMap(zs, obj, d2Map);
                }
                else if (oddSigns.contains(zs) && lonNorm > 15){
                    //planet is in chandra hora
                    obj = "-" + obj;
                    addObjToMap(zs, obj, d2Map);
                }
                else if (!oddSigns.contains(zs) && lonNorm <= 15){
                    //planet is in chandra hora
                    obj = "-" + obj;
                    addObjToMap(zs, obj, d2Map);
                }
                else if (!oddSigns.contains(zs) && lonNorm > 15){
                    //planet is in chandra hora
                    obj = "+" + obj;
                    addObjToMap(zs, obj, d2Map);
                }
            }

        }
        System.out.println(d2Map);
        return d2Map;
    }

    /*Helper method - Adds a specified obj to given HashMap with key: zodiac sign, value: hashset with planets*/
    public boolean addObjToMap(String zs, String obj, HashMap<String, HashSet> hm) {
        HashSet hs;
        if (hm.containsKey(zs)) {
            hs = hm.get(zs);
        } else {
            hs = new HashSet();
        }
        hs.add(obj);
        hm.put(zs, hs);
    return true;
    }

}


