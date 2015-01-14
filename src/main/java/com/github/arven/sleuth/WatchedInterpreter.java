/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.sleuth;

import bsh.Interpreter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.arven.sleuth.commands.json;
import static com.google.common.base.Ascii.BS;
import static com.google.common.base.Ascii.CR;
import static com.google.common.base.Ascii.ESC;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author brian.becker
 */
public class WatchedInterpreter implements Runnable {
    
    public static final String COLOR_RESET = "\033[0m";
    public static final String COLOR_BRIGHT_WHITE = "\033[1;37;40m";
    public static final String COLOR_BRIGHT_RED = "\033[1;31;40m";
    public static final String COLOR_BRIGHT_YELLOW = "\033[1;33;40m";
    public static final String COLOR_BRIGHT_CYAN = "\033[1;36;40m";
    public static final String COLOR_BRIGHT_MAGENTA = "\033[1;35;40m";
    public static final String COLOR_BRIGHT_GREEN = "\033[1;32;40m";
    public static final String COLOR_GREEN = "\033[2;32;40m";
    
    private final Interpreter in;
    private final BufferedReader is;
    private final PrintStream os;
    private final ObjectMapper map;
    
    public boolean lastEOF;
    
    public int seeking = -1;
    public int cursor = 0;
    public ArrayList<String> history;
    public List<String> historyReverse;
    public boolean closing = false;
    public boolean promptWasLast = false;
          
    public WatchedInterpreter(Interpreter in, InputStream is, OutputStream os) {
        this.in = in;
        this.is = new BufferedReader(new InputStreamReader(is));
        this.os = new PrintStream(os);
        this.in.setOut(this.os);
        this.history = new ArrayList<String>();
        this.historyReverse = Lists.reverse(this.history);
        this.map = new ObjectMapper();
    }
    
    public void run() {       
        try {           
            this.in.getNameSpace().getClassManager().addClassPath(json.class.getProtectionDomain().getCodeSource().getLocation());
            this.in.getNameSpace().importCommands("com.github.arven.sleuth.commands");
            this.in.getNameSpace().importClass("com.github.fge.jsonschema.main.JsonSchema");
            this.in.getNameSpace().importClass("com.github.fge.jsonschema.main.JsonSchemaFactory");
            //this.in.getNameSpace().importStatic(JavaDebugCommands.class);
            
            //this.os.print(COLOR_BRIGHT_WHITE + "java$ " + COLOR_RESET);
            this.prompt();
            StringBuilder bld = new StringBuilder();
            int lastChar = 'x';
            while(lastChar != CR) {
                //System.out.println(seeking);
                if(lastChar != -1 && lastChar != 4) {
                    lastChar = this.is.read();
                    if(lastChar == (char)ESC) {
                        lastChar = this.is.read();
                        if(lastChar == 79) {
                            lastChar = this.is.read();
                            if(lastChar == 68) {
                                // System.out.println("LEFT");
                                if(cursor > 0) {
                                    this.os.print((char)ESC + "[D");
                                    cursor--;
                                }
                            } else if(lastChar == 67) {
                                // RIGHT
                                if(cursor < bld.length()) {
                                    this.os.print((char)ESC + "[C");
                                    cursor++;
                                }
                            }
                        } else if(lastChar == 91) {
                            // 49 126
                            lastChar = this.is.read();
                            if(lastChar == 49) {
                                lastChar = this.is.read();
                                if(lastChar == 126) {
                                    if(cursor != 0) {
                                        this.os.print((char)ESC + "[" + this.cursor + "D");
                                        cursor = 0;
                                    }
                                }
                            } else if(lastChar == 52) {
                                lastChar = this.is.read();
                                if(lastChar == 126) {
                                    if(cursor != bld.length()) {
                                        this.os.print((char)ESC + "[" + (bld.length() - this.cursor) + "C");
                                        cursor = bld.length();      
                                    }
                                }
                            }
                        }
                    } else if(lastChar == 127) {
                        if(bld.length() > 0) {
                            bld.deleteCharAt(bld.length() - 1);
                            this.os.print((char)BS);
                            this.os.print((char)ESC + "[K");
                            cursor --;
                        }
                    } else if(lastChar == 16) {
                        if(this.seeking == -1)
                            this.seeking = 0;
                        
                        this.prompt();
                        if(this.seeking + 1 <= this.historyReverse.size()) {
                            bld = new StringBuilder(this.historyReverse.get(this.seeking));
                            this.seeking++;
                        }
                        this.os.print(bld.toString());
                        this.os.print((char)ESC + "[K");
                        cursor = bld.toString().length();
                    } else if(lastChar == 14) {
                        if(this.seeking > 0) {
                            this.prompt();
                            this.seeking--;
                            bld = new StringBuilder(this.historyReverse.get(this.seeking));
                            this.os.print(bld.toString());
                            this.os.print((char)ESC + "[K");
                            cursor = bld.toString().length();
                        }
                    } else {
                        this.seeking = -1;
                        this.os.write(lastChar);
                        // System.out.println(lastChar);
                        if(lastChar != CR) {
                            if(cursor == bld.length()) {
                                bld.append((char) lastChar);
                                cursor ++;
                            } else {
                                if(bld.length() > 0) {
                                    bld.setCharAt(cursor, (char)lastChar);
                                    cursor ++;
                                }
                            }
                        } else {
                            cursor = 0;
                        }
                    }
                } else {
                    //System.exit(0);
                    closing = true;
                    return;
                }
            }
            
            this.os.println();
            this.promptWasLast = false;            
            
            if(bld.toString().equals("history")) {
                this.os.println("Showing history:");
                int i = 0;
                for(String s : this.history) {
                    this.os.print(++i + "\t: ");
                    this.os.println(s);
                }
            } else if(bld.toString().equals("next")) {
                this.os.println((char) CR + COLOR_BRIGHT_YELLOW + "[ Next Test ]" + COLOR_RESET);
                this.setClosing(true);
            } else {
                if(this.seeking == -1) {
                    this.history.add(bld.toString());
                }

                Object v = this.in.eval(bld.toString());
                this.prettyPrint(v);

            }
        } catch (Exception ex) {
            this.os.print(COLOR_BRIGHT_RED);
            ex.printStackTrace(this.os);
            this.os.print(COLOR_RESET);
        }
    }
    
    public void prompt() {
        this.os.print((char)CR + COLOR_BRIGHT_WHITE + "java$ " + COLOR_RESET);
        this.promptWasLast = true;
    }
    
    public String ifprompt(String str) {
        if(this.promptWasLast) {
            return str;
        } else {
            return "";
        }
    }
    
    public String ifnotprompt(String str) {
        if(!this.promptWasLast) {
            return str;
        } else {
            return "";
        }
    }

    public void print(Object o) {
        if(o instanceof String) {
            this.os.print(this.ifprompt((char) CR + "\n") + o.toString().replace("\n", "\n" + (char) CR));
        } else {
            this.os.print(this.ifprompt((char) CR + "\n"));
            this.prettyPrint(o);
            this.os.print(this.ifnotprompt("\n" + (char) CR));
        }
        this.promptWasLast = false;
    }
    
    public void error(Object o) {
        this.os.print(this.ifprompt((char) CR + "\n") + COLOR_BRIGHT_RED);
        if(o instanceof Throwable) {
            Throwable t = (Throwable)o;
            t.printStackTrace(this.os);
        } else {
            this.os.println(o);
        }
        this.os.print(COLOR_RESET);
        this.prompt();
    }
    
    public void pass(Object o) {
        this.os.print(this.ifprompt("\n") + (char) CR + COLOR_BRIGHT_GREEN);
        this.os.println(o);
        this.os.print(COLOR_RESET);
        this.prompt();
    }
    
    public boolean isClosing() {
        return this.closing;
    }
    
    public void setClosing(boolean value) {
        this.closing = value;
    }
    
    public void prettyPrint(Object v) {
        if(v != null) {
            try {                      
                if(v.getClass().equals(String.class)) {
                    this.os.print(COLOR_BRIGHT_YELLOW);
                } else if(v instanceof Number) {
                    this.os.print(COLOR_BRIGHT_CYAN);
                } else if(v.getClass().isArray() || v instanceof List || v.getClass().equals(Character.class)) {
                    this.os.print(COLOR_BRIGHT_MAGENTA);
                } else if(v instanceof Map || v instanceof Class) {
                    this.os.print(COLOR_BRIGHT_GREEN);
                } else if(v instanceof JsonNode) {
                    this.os.print(COLOR_GREEN);
                }

                if(v instanceof Character) {
                    this.os.print("'" + v.toString() + "'");
                } else if(v instanceof Class) {
                    this.os.print(v.toString());
                } else {
                    this.os.print(this.map.writerWithDefaultPrettyPrinter().writeValueAsString(v));
                }
                this.os.print(COLOR_RESET);
            } catch (Exception nv) {
                this.os.print(COLOR_GREEN + v.toString() + COLOR_RESET);
            }
            // this.os.println();
        }
    }
    
}
