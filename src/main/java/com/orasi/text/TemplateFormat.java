package com.orasi.text;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The TemplateFormat class formats and parses strings with variables in them.
 * The search strings are in typical ${variable} notation used by quite a
 * few template engines. This is not a full template engine, just a variable
 * expression replacement format.
 * 
 * NOTE: The TemplateFormat processor uses a full regular expression search,
 * for parsing, so it may become slow.
 * 
 * @author Brian Becker
 */
public class TemplateFormat extends Format {

    private final List<TemplateElement> elements;
    private static final String fieldLeft = "${";
    private static final String fieldRight = "}";
    private static final char fieldEscape = '$';
    private final Pattern regexPattern;
        
    public static class TemplateElement {
        public final String text;
        public String pattern;
        public boolean variable;

        public TemplateElement(String text) { this.text = text; }
        
        public static TemplateElement createTextElement(String text) {
            TemplateElement te = new TemplateElement(text);
            te.variable = false;
            te.pattern = Pattern.quote(text);
            return te;
        }
        
        public static TemplateElement createVariableElement(String text) {
            TemplateElement te = new TemplateElement(text);
            te.variable = true;
            te.pattern = "(.*)";
            return te;
        }
    }

    public TemplateFormat(String pattern) throws Exception {
        this.elements = new LinkedList<TemplateElement>();
        StringBuilder temp = new StringBuilder();
        int idx = -1, lidx = 0, vidx, cidx;
        while ((idx = pattern.indexOf(fieldLeft, idx)) >= 0) {
            if(idx == 0 || pattern.charAt(idx - 1) != fieldEscape) {
                if(temp.length() > 0) {
                    this.elements.add(TemplateElement.createTextElement(temp.toString() + pattern.substring(lidx, idx)));
                    temp = new StringBuilder();
                } else {
                    this.elements.add(TemplateElement.createTextElement(pattern.substring(lidx, idx)));
                }
                idx = idx + fieldLeft.length();
                vidx = pattern.indexOf(fieldRight, idx);
                if(vidx < 0) {
                    throw new Exception("Parser error, no closing field identifier.");
                }
                String var = pattern.substring(idx, vidx);
                this.elements.add(TemplateElement.createVariableElement(var));
                idx = vidx + fieldRight.length();
            } else {
                temp.append(pattern.substring(lidx, idx));
                idx++;
            }
            lidx = idx;
        }
        this.elements.add(TemplateElement.createTextElement(pattern.substring(lidx, pattern.length()))); 
        
        StringBuilder regex = new StringBuilder();
        for (TemplateElement te : this.elements) {
            regex.append(te.pattern);
        }
        System.out.println(regex.toString());
        regexPattern = Pattern.compile(regex.toString());
    }

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        Map m = (Map)obj;
        for(TemplateElement e : this.elements) {
            if(e.variable && m.containsKey(e.text)) {
                toAppendTo.append(m.get(e.text));
            } else if(e.variable) {
                toAppendTo.append(fieldLeft.concat(e.text).concat(fieldRight));
            } else {
                toAppendTo.append(e.text);
            }
        }
        return toAppendTo;
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        Map map = new HashMap();
        Matcher m = regexPattern.matcher(source);
        m.matches();
        int group = 1;
        for(TemplateElement e : this.elements) {
            if(e.variable) {
                map.put(e.text, m.group(group++));
            }
        }
        
        pos.setIndex(source.length());
        return (Map)map;
    }
    
    public Map parse(String source) {
        return (Map) this.parseObject(source, new ParsePosition(0));
    }

}
