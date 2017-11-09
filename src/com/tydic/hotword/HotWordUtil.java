package com.tydic.hotword;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class HotWordUtil {

    public static List<Map<String, Integer>> getTopN(List<String> src, int top) throws IOException{

        IndexDocs indexDocs = new IndexDocs();
        try {
            indexDocs.dealDocs(src);
        } catch (IOException e) {
            throw e;
        }

        Map<String, Integer> map = new HashMap<String, Integer>();
        try {
            Directory directory = FSDirectory.open(Paths.get("indexdir"));
            IndexReader reader = DirectoryReader.open(directory);
            // 因为只索引了一个文档，所以DocID为0，通过getTermVector获取content字段的词项
            Terms terms = reader.getTermVector(0, "content");
            // 遍历词项
            TermsEnum termsEnum = terms.iterator();
            BytesRef thisTerm = null;
            while ((thisTerm = termsEnum.next()) != null) {
                // 词项
                String termText = thisTerm.utf8ToString();
                // 通过totalTermFreq()方法获取词项频率
                map.put(termText, (int) termsEnum.totalTermFreq());
            }
        } catch (IOException e) {
        	throw e;
        }

        // 按value排序
        List<Map.Entry<String, Integer>> sortedMap = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
        Collections.sort(sortedMap, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o2.getValue() - o1.getValue());
            }
        });

        return getN(sortedMap, top);
    }

    // 获取top-n
    private static List<Map<String, Integer>> getN(List<Entry<String, Integer>> map, int n) {
        List<Map<String, Integer>> topN = new ArrayList<Map<String, Integer>>();
        int i = 0;
        for (Entry<String, Integer> e : map) {
            if (i >= n) {
                break;
            }
            Map<String, Integer> top = new HashMap<String, Integer>();
            top.put(e.getKey(), e.getValue());
            topN.add(top);
            i++;
        }
        return topN;
    }

}
