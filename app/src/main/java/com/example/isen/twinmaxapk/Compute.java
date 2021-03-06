package com.example.isen.twinmaxapk;

import com.example.isen.twinmaxapk.database.Measure;
import com.example.isen.twinmaxapk.database.historic.Maintenance;
import com.example.isen.twinmaxapk.database.historic.Moto;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

public class Compute {

    private static ArrayList<Measure> measures;
    private static Realm realm;
    private ArrayList<Measure> MeasuresList;
    private LineData pointsDur;

    public Compute() {
        measures = new ArrayList<>();
    }


    /**
     * @param rangeMin should be 0 most of the times
     * @param rangeMax users choose
     * @return
     */
    private static boolean wasFirstRemovingMeasures = true;
    private static int[] range = {0, 0};

    public static synchronized List<Measure> getMeasures(int rangeMin, int rangeMax) {
        if (!wasFirstRemovingMeasures) {
            for (int i = range[1]; i >= range[0]; i--) {
                measures.remove(i);
            }
        }
        if (rangeMin >= 0 && rangeMax < measures.size()) {
            wasFirstRemovingMeasures = false;
            range[0] = rangeMin;
            range[1] = rangeMax;
            return measures.subList(rangeMin, rangeMax);
        }
        return null;
    }

    public void setMeasures(ArrayList<Measure> measures) {
        this.measures = measures;
    }

    public static Realm getRealm() {
        return realm;
    }

    public static void setRealm(Realm realm) {
        Compute.realm = realm;
    }

    public static synchronized void addMeasure(Measure measure) {
        measures.add(measure);
    }

    public void deleteOneCycleItems(int numberPoints){

        if (measures.size() > 3*numberPoints) {
            measures.subList(0, numberPoints - 1).clear();
        }
    }

    public void rpm(){

    }

    public void emptyDatabase(){
        if(realm !=null){
            realm.beginTransaction();
            realm.clear(Moto.class);
            realm.commitTransaction();
        }
    }

    public void fillMeasuresList() {
        Measure measure1 = new Measure(100);
        Measure measure2 = new Measure(100);
        Measure measure3 = new Measure(100);
        Measure measure4 = new Measure(100);
        Measure measure5 = new Measure(100);
        Measure measure6 = new Measure(100);
        Measure measure7 = new Measure(100);
        Measure measure8 = new Measure(101);
        Measure measure9 = new Measure(100);
        Measure measure10 = new Measure(100);
        Measure measure11 = new Measure(102);
        Measure measure12 = new Measure(101);
        Measure measure13 = new Measure(100);
        Measure measure14 = new Measure(100);
        Measure measure15 = new Measure(100);
        Measure measure16 = new Measure(100);
        Measure measure17 = new Measure(100);
        Measure measure18 = new Measure(100);
        Measure measure19 = new Measure(99);
        Measure measure20 = new Measure(100);
        Measure measure21 = new Measure(100);
        Measure measure22 = new Measure(100);
        Measure measure23 = new Measure(98);
        Measure measure24 = new Measure(100);
        Measure measure25 = new Measure(101);
        Measure measure26 = new Measure(100);
        Measure measure27 = new Measure(100);
        Measure measure28 = new Measure(101);
        Measure measure29 = new Measure(100);
        Measure measure30 = new Measure(100);
        Measure measure31 = new Measure(100);
        Measure measure32 = new Measure(100);
        Measure measure33 = new Measure(100);
        Measure measure34 = new Measure(100);
        Measure measure35 = new Measure(100);
        Measure measure36 = new Measure(100);
        Measure measure37 = new Measure(100);
        Measure measure38 = new Measure(101);
        Measure measure39 = new Measure(100);
        Measure measure40 = new Measure(99);
        Measure measure41 = new Measure(98);
        Measure measure42 = new Measure(100);
        Measure measure43 = new Measure(101);
        Measure measure44 = new Measure(4101);
        Measure measure45 = new Measure(100);
        Measure measure46 = new Measure(100);
        Measure measure47 = new Measure(100);
        Measure measure48 = new Measure(99);
        Measure measure49 = new Measure(100);
        Measure measure50 = new Measure(100);
        Measure measure51 = new Measure(99);
        Measure measure52 = new Measure(98);
        Measure measure53 = new Measure(97);
        Measure measure54 = new Measure(96);
        Measure measure55 = new Measure(95);
        Measure measure56 = new Measure(94);
        Measure measure57 = new Measure(92);
        Measure measure58 = new Measure(90);
        Measure measure59 = new Measure(85);
        Measure measure60 = new Measure(82);
        Measure measure61 = new Measure(77);
        Measure measure62 = new Measure(70);
        Measure measure63 = new Measure(65);
        Measure measure64 = new Measure(55);
        Measure measure65 = new Measure(50);
        Measure measure66 = new Measure(45);
        Measure measure67 = new Measure(40);
        Measure measure68 = new Measure(35);
        Measure measure69 = new Measure(30);
        Measure measure70 = new Measure(25);
        Measure measure71 = new Measure(23);
        Measure measure72 = new Measure(21);
        Measure measure73 = new Measure(19);
        Measure measure74 = new Measure(18);
        Measure measure75 = new Measure(17);
        Measure measure76 = new Measure(16);
        Measure measure77 = new Measure(15);
        Measure measure78 = new Measure(15);
        Measure measure79 = new Measure(15);
        Measure measure80 = new Measure(14);
        Measure measure81 = new Measure(14);
        Measure measure82 = new Measure(14);
        Measure measure83 = new Measure(14);
        Measure measure84 = new Measure(13);
        Measure measure85 = new Measure(13);
        Measure measure86 = new Measure(14);
        Measure measure87 = new Measure(14);
        Measure measure88 = new Measure(15);
        Measure measure89 = new Measure(15);
        Measure measure90 = new Measure(15);
        Measure measure91 = new Measure(16);
        Measure measure92 = new Measure(16);
        Measure measure93 = new Measure(17);
        Measure measure94 = new Measure(18);
        Measure measure95 = new Measure(20);
        Measure measure96 = new Measure(21);
        Measure measure97 = new Measure(23);
        Measure measure98 = new Measure(26);
        Measure measure99 = new Measure(30);
        Measure measure100 = new Measure(34);
        Measure measure101 = new Measure(38);
        Measure measure102 = new Measure(42);
        Measure measure103 = new Measure(46);
        Measure measure104 = new Measure(50);
        Measure measure105 = new Measure(54);
        Measure measure106 = new Measure(58);
        Measure measure107 = new Measure(62);
        Measure measure108 = new Measure(66);
        Measure measure109 = new Measure(70);
        Measure measure110 = new Measure(73);
        Measure measure111 = new Measure(76);
        Measure measure112 = new Measure(79);
        Measure measure113 = new Measure(83);
        Measure measure114 = new Measure(85);
        Measure measure115 = new Measure(87);
        Measure measure116 = new Measure(89);
        Measure measure117 = new Measure(91);
        Measure measure118 = new Measure(92);
        Measure measure119 = new Measure(93);
        Measure measure120 = new Measure(95);
        Measure measure121 = new Measure(96);
        Measure measure122 = new Measure(97);
        Measure measure123 = new Measure(98);
        Measure measure124 = new Measure(99);
        Measure measure125 = new Measure(100);
        Measure measure126 = new Measure(101);
        Measure measure127 = new Measure(102);
        Measure measure128 = new Measure(103);
        Measure measure129 = new Measure(104);
        Measure measure130 = new Measure(104);
        Measure measure131 = new Measure(103);
        Measure measure132 = new Measure(102);
        Measure measure133 = new Measure(101);
        Measure measure134 = new Measure(100);
        Measure measure135 = new Measure(99);
        Measure measure136 = new Measure(98);
        Measure measure137 = new Measure(99);
        Measure measure138 = new Measure(100);
        Measure measure139 = new Measure(100);
        Measure measure140 = new Measure(99);
        Measure measure141 = new Measure(100);
        Measure measure142 = new Measure(100);
        Measure measure143 = new Measure(100);
        Measure measure144 = new Measure(100);
        Measure measure145 = new Measure(100);
        Measure measure146 = new Measure(100);
        Measure measure147 = new Measure(100);
        Measure measure148 = new Measure(101);
        Measure measure149 = new Measure(100);
        Measure measure150 = new Measure(100);
        Measure measure151 = new Measure(100);
        Measure measure152 = new Measure(100);
        Measure measure153 = new Measure(99);
        Measure measure154 = new Measure(99);
        Measure measure155 = new Measure(100);
        Measure measure156 = new Measure(101);
        Measure measure157 = new Measure(100);
        Measure measure158 = new Measure(101);
        Measure measure159 = new Measure(100);
        Measure measure160 = new Measure(100);
        Measure measure161 = new Measure(102);
        Measure measure162 = new Measure(101);
        Measure measure163 = new Measure(100);
        Measure measure164 = new Measure(99);
        Measure measure165 = new Measure(100);
        Measure measure166 = new Measure(100);
        Measure measure167 = new Measure(100);
        Measure measure168 = new Measure(100);
        Measure measure169 = new Measure(99);
        Measure measure170 = new Measure(100);
        Measure measure171 = new Measure(100);
        Measure measure172 = new Measure(100);
        Measure measure173 = new Measure(99);
        Measure measure174 = new Measure(100);
        Measure measure175 = new Measure(101);
        Measure measure176 = new Measure(100);
        Measure measure177 = new Measure(100);
        Measure measure178 = new Measure(101);
        Measure measure179 = new Measure(100);
        Measure measure180 = new Measure(100);
        Measure measure181 = new Measure(100);
        Measure measure182 = new Measure(100);
        Measure measure183 = new Measure(98);
        Measure measure184 = new Measure(99);
        Measure measure185 = new Measure(100);
        Measure measure186 = new Measure(101);
        Measure measure187 = new Measure(100);
        Measure measure188 = new Measure(101);
        Measure measure189 = new Measure(100);
        Measure measure190 = new Measure(99);
        Measure measure191 = new Measure(98);
        Measure measure192 = new Measure(100);
        Measure measure193 = new Measure(101);
        Measure measure194 = new Measure(101);
        Measure measure195 = new Measure(4100);
        Measure measure196 = new Measure(-2100);
        Measure measure197 = new Measure(100);
        Measure measure198 = new Measure(99);
        Measure measure199 = new Measure(100);
        Measure measure200 = new Measure(100);
        Measure measure201 = new Measure(99);
        Measure measure202 = new Measure(98);
        Measure measure203 = new Measure(97);
        Measure measure204 = new Measure(96);
        Measure measure205 = new Measure(95);
        Measure measure206 = new Measure(93);
        Measure measure207 = new Measure(91);
        Measure measure208 = new Measure(90);
        Measure measure209 = new Measure(85);
        Measure measure210 = new Measure(82);
        Measure measure211 = new Measure(76);
        Measure measure212 = new Measure(70);
        Measure measure213 = new Measure(65);
        Measure measure214 = new Measure(60);
        Measure measure215 = new Measure(55);
        Measure measure216 = new Measure(55);
        Measure measure217 = new Measure(45);
        Measure measure218 = new Measure(35);
        Measure measure219 = new Measure(30);
        Measure measure220 = new Measure(25);
        Measure measure221 = new Measure(23);
        Measure measure222 = new Measure(21);
        Measure measure223 = new Measure(19);
        Measure measure224 = new Measure(18);
        Measure measure225 = new Measure(17);
        Measure measure226 = new Measure(16);
        Measure measure227 = new Measure(15);
        Measure measure228 = new Measure(15);
        Measure measure229 = new Measure(15);
        Measure measure230 = new Measure(14);
        Measure measure231 = new Measure(14);
        Measure measure232 = new Measure(14);
        Measure measure233 = new Measure(14);
        Measure measure234 = new Measure(13);
        Measure measure235 = new Measure(13);
        Measure measure236 = new Measure(12);
        Measure measure237 = new Measure(12);
        Measure measure238 = new Measure(13);
        Measure measure239 = new Measure(14);
        Measure measure240 = new Measure(15);
        Measure measure241 = new Measure(15);
        Measure measure242 = new Measure(16);
        Measure measure243 = new Measure(16);
        Measure measure244 = new Measure(17);
        Measure measure245 = new Measure(19);
        Measure measure246 = new Measure(21);
        Measure measure247 = new Measure(23);
        Measure measure248 = new Measure(26);
        Measure measure249 = new Measure(30);
        Measure measure250 = new Measure(33);
        Measure measure251 = new Measure(36);
        Measure measure252 = new Measure(41);
        Measure measure253 = new Measure(46);
        Measure measure254 = new Measure(50);
        Measure measure255 = new Measure(54);
        Measure measure256 = new Measure(58);
        Measure measure257 = new Measure(62);
        Measure measure258 = new Measure(66);
        Measure measure259 = new Measure(70);
        Measure measure260 = new Measure(73);
        Measure measure261 = new Measure(76);
        Measure measure262 = new Measure(79);
        Measure measure263 = new Measure(83);
        Measure measure264 = new Measure(85);
        Measure measure265 = new Measure(87);
        Measure measure266 = new Measure(89);
        Measure measure267 = new Measure(91);
        Measure measure268 = new Measure(93);
        Measure measure269 = new Measure(93);
        Measure measure270 = new Measure(94);
        Measure measure271 = new Measure(95);
        Measure measure272 = new Measure(96);
        Measure measure273 = new Measure(98);
        Measure measure274 = new Measure(99);
        Measure measure275 = new Measure(100);
        Measure measure276 = new Measure(101);
        Measure measure277 = new Measure(102);
        Measure measure278 = new Measure(103);
        Measure measure279 = new Measure(104);
        Measure measure280 = new Measure(104);
        Measure measure281 = new Measure(103);
        Measure measure282 = new Measure(102);
        Measure measure283 = new Measure(101);
        Measure measure284 = new Measure(100);
        Measure measure285 = new Measure(99);
        Measure measure286 = new Measure(98);
        Measure measure287 = new Measure(98);
        Measure measure288 = new Measure(99);
        Measure measure289 = new Measure(100);
        Measure measure290 = new Measure(101);
        Measure measure291 = new Measure(100);
        Measure measure292 = new Measure(100);
        Measure measure293 = new Measure(100);
        Measure measure294 = new Measure(100);
        Measure measure295 = new Measure(100);
        Measure measure296 = new Measure(100);
        Measure measure297 = new Measure(100);
        Measure measure298 = new Measure(101);
        Measure measure299 = new Measure(100);
        Measure measure300 = new Measure(4499);

        MeasuresList = new ArrayList<>();

        MeasuresList.add(measure1);
        MeasuresList.add(measure2);
        MeasuresList.add(measure3);
        MeasuresList.add(measure4);
        MeasuresList.add(measure5);
        MeasuresList.add(measure6);
        MeasuresList.add(measure7);
        MeasuresList.add(measure8);
        MeasuresList.add(measure9);
        MeasuresList.add(measure10);
        MeasuresList.add(measure11);
        MeasuresList.add(measure12);
        MeasuresList.add(measure13);
        MeasuresList.add(measure14);
        MeasuresList.add(measure15);
        MeasuresList.add(measure16);
        MeasuresList.add(measure17);
        MeasuresList.add(measure18);
        MeasuresList.add(measure19);
        MeasuresList.add(measure20);
        MeasuresList.add(measure21);
        MeasuresList.add(measure22);
        MeasuresList.add(measure23);
        MeasuresList.add(measure24);
        MeasuresList.add(measure25);
        MeasuresList.add(measure26);
        MeasuresList.add(measure27);
        MeasuresList.add(measure28);
        MeasuresList.add(measure29);
        MeasuresList.add(measure30);
        MeasuresList.add(measure31);
        MeasuresList.add(measure32);
        MeasuresList.add(measure33);
        MeasuresList.add(measure34);
        MeasuresList.add(measure35);
        MeasuresList.add(measure36);
        MeasuresList.add(measure37);
        MeasuresList.add(measure38);
        MeasuresList.add(measure39);
        MeasuresList.add(measure40);
        MeasuresList.add(measure41);
        MeasuresList.add(measure42);
        MeasuresList.add(measure43);
        MeasuresList.add(measure44);
        MeasuresList.add(measure45);
        MeasuresList.add(measure46);
        MeasuresList.add(measure47);
        MeasuresList.add(measure48);
        MeasuresList.add(measure49);
        MeasuresList.add(measure50);
        MeasuresList.add(measure51);
        MeasuresList.add(measure52);
        MeasuresList.add(measure53);
        MeasuresList.add(measure54);
        MeasuresList.add(measure55);
        MeasuresList.add(measure56);
        MeasuresList.add(measure57);
        MeasuresList.add(measure58);
        MeasuresList.add(measure59);
        MeasuresList.add(measure60);
        MeasuresList.add(measure61);
        MeasuresList.add(measure62);
        MeasuresList.add(measure63);
        MeasuresList.add(measure64);
        MeasuresList.add(measure65);
        MeasuresList.add(measure66);
        MeasuresList.add(measure67);
        MeasuresList.add(measure68);
        MeasuresList.add(measure69);
        MeasuresList.add(measure70);
        MeasuresList.add(measure71);
        MeasuresList.add(measure72);
        MeasuresList.add(measure73);
        MeasuresList.add(measure74);
        MeasuresList.add(measure75);
        MeasuresList.add(measure76);
        MeasuresList.add(measure77);
        MeasuresList.add(measure78);
        MeasuresList.add(measure79);
        MeasuresList.add(measure80);
        MeasuresList.add(measure81);
        MeasuresList.add(measure82);
        MeasuresList.add(measure83);
        MeasuresList.add(measure84);
        MeasuresList.add(measure85);
        MeasuresList.add(measure86);
        MeasuresList.add(measure87);
        MeasuresList.add(measure88);
        MeasuresList.add(measure89);
        MeasuresList.add(measure90);
        MeasuresList.add(measure91);
        MeasuresList.add(measure92);
        MeasuresList.add(measure93);
        MeasuresList.add(measure94);
        MeasuresList.add(measure95);
        MeasuresList.add(measure96);
        MeasuresList.add(measure97);
        MeasuresList.add(measure98);
        MeasuresList.add(measure99);
        MeasuresList.add(measure100);
        MeasuresList.add(measure101);
        MeasuresList.add(measure102);
        MeasuresList.add(measure103);
        MeasuresList.add(measure104);
        MeasuresList.add(measure105);
        MeasuresList.add(measure106);
        MeasuresList.add(measure107);
        MeasuresList.add(measure108);
        MeasuresList.add(measure109);
        MeasuresList.add(measure110);
        MeasuresList.add(measure111);
        MeasuresList.add(measure112);
        MeasuresList.add(measure113);
        MeasuresList.add(measure114);
        MeasuresList.add(measure115);
        MeasuresList.add(measure116);
        MeasuresList.add(measure117);
        MeasuresList.add(measure118);
        MeasuresList.add(measure119);
        MeasuresList.add(measure120);
        MeasuresList.add(measure121);
        MeasuresList.add(measure122);
        MeasuresList.add(measure123);
        MeasuresList.add(measure124);
        MeasuresList.add(measure125);
        MeasuresList.add(measure126);
        MeasuresList.add(measure127);
        MeasuresList.add(measure128);
        MeasuresList.add(measure129);
        MeasuresList.add(measure130);
        MeasuresList.add(measure131);
        MeasuresList.add(measure132);
        MeasuresList.add(measure133);
        MeasuresList.add(measure134);
        MeasuresList.add(measure135);
        MeasuresList.add(measure136);
        MeasuresList.add(measure137);
        MeasuresList.add(measure138);
        MeasuresList.add(measure139);
        MeasuresList.add(measure140);
        MeasuresList.add(measure141);
        MeasuresList.add(measure142);
        MeasuresList.add(measure143);
        MeasuresList.add(measure144);
        MeasuresList.add(measure145);
        MeasuresList.add(measure146);
        MeasuresList.add(measure147);
        MeasuresList.add(measure148);
        MeasuresList.add(measure149);
        MeasuresList.add(measure150);
        MeasuresList.add(measure151);
        MeasuresList.add(measure152);
        MeasuresList.add(measure153);
        MeasuresList.add(measure154);
        MeasuresList.add(measure155);
        MeasuresList.add(measure156);
        MeasuresList.add(measure157);
        MeasuresList.add(measure158);
        MeasuresList.add(measure159);
        MeasuresList.add(measure160);
        MeasuresList.add(measure161);
        MeasuresList.add(measure162);
        MeasuresList.add(measure163);
        MeasuresList.add(measure164);
        MeasuresList.add(measure165);
        MeasuresList.add(measure166);
        MeasuresList.add(measure167);
        MeasuresList.add(measure168);
        MeasuresList.add(measure169);
        MeasuresList.add(measure170);
        MeasuresList.add(measure171);
        MeasuresList.add(measure172);
        MeasuresList.add(measure173);
        MeasuresList.add(measure174);
        MeasuresList.add(measure175);
        MeasuresList.add(measure176);
        MeasuresList.add(measure177);
        MeasuresList.add(measure178);
        MeasuresList.add(measure179);
        MeasuresList.add(measure180);
        MeasuresList.add(measure181);
        MeasuresList.add(measure182);
        MeasuresList.add(measure183);
        MeasuresList.add(measure184);
        MeasuresList.add(measure185);
        MeasuresList.add(measure186);
        MeasuresList.add(measure187);
        MeasuresList.add(measure188);
        MeasuresList.add(measure189);
        MeasuresList.add(measure190);
        MeasuresList.add(measure191);
        MeasuresList.add(measure192);
        MeasuresList.add(measure193);
        MeasuresList.add(measure194);
        MeasuresList.add(measure195);
        MeasuresList.add(measure196);
        MeasuresList.add(measure197);
        MeasuresList.add(measure198);
        MeasuresList.add(measure199);
        MeasuresList.add(measure200);
        MeasuresList.add(measure201);
        MeasuresList.add(measure202);
        MeasuresList.add(measure203);
        MeasuresList.add(measure204);
        MeasuresList.add(measure205);
        MeasuresList.add(measure206);
        MeasuresList.add(measure207);
        MeasuresList.add(measure208);
        MeasuresList.add(measure209);
        MeasuresList.add(measure210);
        MeasuresList.add(measure211);
        MeasuresList.add(measure212);
        MeasuresList.add(measure213);
        MeasuresList.add(measure214);
        MeasuresList.add(measure215);
        MeasuresList.add(measure216);
        MeasuresList.add(measure217);
        MeasuresList.add(measure218);
        MeasuresList.add(measure219);
        MeasuresList.add(measure220);
        MeasuresList.add(measure221);
        MeasuresList.add(measure222);
        MeasuresList.add(measure223);
        MeasuresList.add(measure224);
        MeasuresList.add(measure225);
        MeasuresList.add(measure226);
        MeasuresList.add(measure227);
        MeasuresList.add(measure228);
        MeasuresList.add(measure229);
        MeasuresList.add(measure230);
        MeasuresList.add(measure231);
        MeasuresList.add(measure232);
        MeasuresList.add(measure233);
        MeasuresList.add(measure234);
        MeasuresList.add(measure235);
        MeasuresList.add(measure236);
        MeasuresList.add(measure237);
        MeasuresList.add(measure238);
        MeasuresList.add(measure239);
        MeasuresList.add(measure240);
        MeasuresList.add(measure241);
        MeasuresList.add(measure242);
        MeasuresList.add(measure243);
        MeasuresList.add(measure244);
        MeasuresList.add(measure245);
        MeasuresList.add(measure246);
        MeasuresList.add(measure247);
        MeasuresList.add(measure248);
        MeasuresList.add(measure249);
        MeasuresList.add(measure250);
        MeasuresList.add(measure251);
        MeasuresList.add(measure252);
        MeasuresList.add(measure253);
        MeasuresList.add(measure254);
        MeasuresList.add(measure255);
        MeasuresList.add(measure256);
        MeasuresList.add(measure257);
        MeasuresList.add(measure258);
        MeasuresList.add(measure259);
        MeasuresList.add(measure260);
        MeasuresList.add(measure261);
        MeasuresList.add(measure262);
        MeasuresList.add(measure263);
        MeasuresList.add(measure264);
        MeasuresList.add(measure265);
        MeasuresList.add(measure266);
        MeasuresList.add(measure267);
        MeasuresList.add(measure268);
        MeasuresList.add(measure269);
        MeasuresList.add(measure270);
        MeasuresList.add(measure271);
        MeasuresList.add(measure272);
        MeasuresList.add(measure273);
        MeasuresList.add(measure274);
        MeasuresList.add(measure275);
        MeasuresList.add(measure276);
        MeasuresList.add(measure277);
        MeasuresList.add(measure278);
        MeasuresList.add(measure279);
        MeasuresList.add(measure280);
        MeasuresList.add(measure281);
        MeasuresList.add(measure282);
        MeasuresList.add(measure283);
        MeasuresList.add(measure284);
        MeasuresList.add(measure285);
        MeasuresList.add(measure286);
        MeasuresList.add(measure287);
        MeasuresList.add(measure288);
        MeasuresList.add(measure289);
        MeasuresList.add(measure290);
        MeasuresList.add(measure291);
        MeasuresList.add(measure292);
        MeasuresList.add(measure293);
        MeasuresList.add(measure294);
        MeasuresList.add(measure295);
        MeasuresList.add(measure296);
        MeasuresList.add(measure297);
        MeasuresList.add(measure298);
        MeasuresList.add(measure299);
        MeasuresList.add(measure300);
    }



    public void someItemsInDatabase(){


        Moto moto1 = new Moto("Kawazaki Z750", "10/03/2016");
        Moto moto2 = new Moto("Suzuki GSXR1000", "02/02/2016");
        Moto moto3 = new Moto("Pan European", "28/01/2016");
        Moto moto4 = new Moto("Le vélo de Brice", "01/01/2016");

        fillMeasuresList();

        RealmList<Maintenance> maintenances = new RealmList<>();
        //Maintenance maintenance = new Maintenance("22/02/2012","RAS",MeasuresList);
        Maintenance maintenance1 = new Maintenance("22/02/2011","Problème carbu");
        //maintenances.add(maintenance);
        maintenances.add(maintenance1);

        RealmList<Maintenance> maintenances1 = new RealmList<>();
        Maintenance maintenance2 = new Maintenance("22/02/2010","tension de chaine");

        moto1.setMaintenances(maintenances);
        moto2.setMaintenances(maintenances);
        moto3.setMaintenances(maintenances);
        moto3.setMaintenances(maintenances);
        moto4.setMaintenances(maintenances1);

        realm.beginTransaction();
        realm.copyToRealm(moto1);
        realm.copyToRealm(moto2);
        realm.copyToRealm(moto3);
        realm.copyToRealm(moto4);
        realm.commitTransaction();
    }

    public static void addMoto(Moto moto){

        realm.beginTransaction();
        realm.copyToRealm(moto);
        realm.commitTransaction();
    }
}

