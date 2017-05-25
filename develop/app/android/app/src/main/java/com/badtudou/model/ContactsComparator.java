package com.badtudou.model;

import android.util.Log;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by badtudou on 25/05/2017.
 */

public class ContactsComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        Map<String,String> map1 = ( Map<String,String>)o1;
        Map<String,String> map2 = ( Map<String,String>)o2;
        String name1 = map1.get("name");
        String name2 = map2.get("name");
        String[] pinyin1 = PinyinHelper.toHanyuPinyinStringArray(name1.charAt(0));
        String[] pinyin2 = PinyinHelper.toHanyuPinyinStringArray(name2.charAt(0));
        if(pinyin1 == null){
            return -1;
        }
        if(pinyin2 == null){
            return 1;
        }
        Log.d("Test", "p1:"+ pinyin1[0].charAt(0)+" p2:"+ pinyin2[0].charAt(0));

        return pinyin1[0].charAt(0) - pinyin2[0].charAt(0);
    }
}
