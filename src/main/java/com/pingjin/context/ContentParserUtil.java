package com.pingjin.context;


import com.pingjin.common.StringUtil;
import com.pingjin.model.ContentWithTags;
import com.pingjin.model.TypedID;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentParserUtil {
    private final static String[] illegalTags = new String[]{"mktsymbol"};
    private final static Pattern pattern = Pattern.compile("<mktsymbol(?:\\s+.)*?\\s+symbol=\"([^\"]*?)\"");

    public static List<TypedID> matchSymbolXml(String content) {
        List<TypedID> relatedIds = new ArrayList<>();
        if(!StringUtil.hasText(content)) {
            return relatedIds;
        }
        ContentWithTags cnt = parse(content);
        for (ContentWithTags.TagElement ele : cnt.getTags()) {
            if(!ele.getTagName().equals("mktsymbol")) {
                continue;
            }
            for (String type : ele.getAttributes().keySet()) {
                TypedID id = new TypedID();
                id.setId(ele.getAttributes().get(type));
                id.setType(type);
                relatedIds.add(id);
            }
        }
        return relatedIds;
    }

    public static List<TypedID> matchSymbolRegex(String content) {
        List<TypedID> relatedIds = new ArrayList<>();
        if(!StringUtil.hasText(content)) {
            return relatedIds;
        }
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            TypedID id = new TypedID();
            id.setId(matcher.group(1));
            id.setType("symbol");
            relatedIds.add(id);
        }
        return relatedIds;
    }


    private static ContentWithTags parse(String content) {
        Document doc = Jsoup.parse(content);
        doc.outputSettings().prettyPrint(false);
        List<String> texts = new ArrayList<>();
        List<ContentWithTags.TagElement> tags = new ArrayList<>();
        for (String tag : illegalTags) {
            Elements elements = doc.getElementsByTag(tag);
            for (Element ele : elements) {
                ContentWithTags.TagElement te;
                te = new ContentWithTags.TagElement();
                te.setTagName(tag);
                tags.add(te);
                ele.text(ele.text().trim());
                texts.add(ele.outerHtml());
                ele.replaceWith(TextNode.createFromEncoded("%s"));
                Attributes attrs = ele.attributes();
                for (Attribute attr : attrs) {
                    if (StringUtil.hasText(attr.getValue())) {
                        te.getAttributes().put(attr.getKey(), attr.getValue());
                    }
                }
            }
        }
        String formatted;
        if (!texts.isEmpty()) {
            formatted = String.format(Entities.escape(doc.text()), texts.toArray());
        } else {
            formatted = Entities.escape(doc.body().html());
        }
        ContentWithTags contentWithTags = new ContentWithTags();
        contentWithTags.setContent(formatted);
        contentWithTags.setOriginContent(content);
        contentWithTags.getTags().addAll(tags);
        return contentWithTags;
    }

    public static void main(String[] args) {
        String str = "哈哈哈<mktsymbol symbol=\"BABA.US\">$阿里巴巴$</mktsymbol><123445><mktsymbol symbol=\"FAMI.US\">$农米良品$</mktsymbol>FDSF<mktsymbol symbol=\"00031.HK\">$航天控股$</mktsymbol>GFDGD<mktsymbol symbol=\"300001.SZ\">$特锐德$</mktsymbol>";
//        ContentWithTags cp = ContentParserUtil.parse(str);
//        System.out.println(JSON.toJSONString(cp));

        List<TypedID> idList = ContentParserUtil.matchSymbolXml(str);
        System.out.println(idList);
        System.out.println(ContentParserUtil.matchSymbolRegex(str)); //正则 双引号

        long start = System.currentTimeMillis();
        for(int i = 0; i < 100000; i++) {
            ContentParserUtil.matchSymbolXml(str);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);

        start = System.currentTimeMillis();
        for(int i = 0; i < 100000; i++) {
            ContentParserUtil.matchSymbolRegex(str);
        }
        end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
