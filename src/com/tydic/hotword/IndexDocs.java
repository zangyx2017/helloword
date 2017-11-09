package com.tydic.hotword;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.tydic.hotword.lucene.ik.IKAnalyzer6x;

public class IndexDocs {

    public void dealDocs(List<String> textList) throws IOException {
        if (textList == null || textList.isEmpty()) {
            return;
        }
        Analyzer smcAnalyzer = new IKAnalyzer6x(true);

        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(smcAnalyzer);
        indexWriterConfig.setOpenMode(OpenMode.CREATE);
        // 索引的存储路径
        Directory directory = null;
        // 索引的增删改由indexWriter创建
        IndexWriter indexWriter = null;
        directory = FSDirectory.open(Paths.get("indexdir"));
        indexWriter = new IndexWriter(directory, indexWriterConfig);

        // 新建FieldType,用于指定字段索引时的信息
        FieldType type = new FieldType();
        // 索引时保存文档、词项频率、位置信息、偏移信息
        type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        type.setStored(true);// 原始字符串全部被保存在索引中
        type.setStoreTermVectors(true);// 存储词项量
        type.setTokenized(true);// 词条化
        Document doc1 = new Document();
        String text1 = textToString(textList);
        Field field1 = new Field("content", text1, type);
        doc1.add(field1);
        indexWriter.addDocument(doc1);
        indexWriter.close();
        directory.close();
    }

    public static String textToString(List<String> textList) {
        StringBuilder result = new StringBuilder();
        for (String str : textList) {
            result.append(System.lineSeparator() + str);
        }
        return result.toString();
    }
}
