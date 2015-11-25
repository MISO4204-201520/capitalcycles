package com.sofactory.capitalcycles.cycletrip.Utils.Parser;

import android.content.Context;
import android.content.res.Resources;

import com.sofactory.capitalcycles.cycletrip.R;
import com.sofactory.capitalcycles.cycletrip.VO.Feature;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LuisSebastian on 11/23/15.
 */
public class ParseFile {


    public static List<Feature> parseFile(Context context){
        List<Feature> featureList = new ArrayList<>();
        String result;
        try {
            Resources res = context.getResources();
            InputStream in_s = res.openRawResource(R.raw.feature_excludes);

            byte[] b = new byte[in_s.available()];
            in_s.read(b);
            result = new String(b);
        } catch (Exception e) {
            // e.printStackTrace();
            result = "Error: can't show file.";
        }
        String[] parts = result.split("\n");
        for(String a : parts){
            String[] parts2 = a.split("=");
            Feature feature = new Feature(parts2[0],parts2[1]);
            featureList.add(feature);
        }
        return featureList;
    }





}
