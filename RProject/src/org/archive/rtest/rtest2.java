package org.archive.rtest;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.awt.*;

import javax.swing.*;

import org.rosuda.JRI.RVector;
import org.rosuda.JRI.Rengine;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.RConsoleOutputStream;
import org.rosuda.JRI.REXP; 
import org.rosuda.JRI.RList; 
import org.rosuda.JRI.RVector; 
import org.rosuda.JRI.Rengine;


public class rtest2 {
	private static DecimalFormat resultFormat = new DecimalFormat("#.####");

	//2
	public static void test_2(){
		Rengine r = new Rengine(new String[]{"--no-save"}, false, null);
	    r.eval("library(Hmisc)");
	    r.eval("yy <- describe(rnorm(200))");
	    REXP exp = r.eval("zz <- yy$counts[5:11]");
	    //REXP names = r.eval("names(zz)");
	   // String[] strExp = exp.asStringArray();
	    //System.out.println("result:" + exp);
	 
	    r.eval("histval <- hist(rnorm(100), plot=FALSE)");
	    REXP xvalExp = r.eval("histval$mids");
	    REXP yvalExp = r.eval("histval$counts");
	    System.out.println("histval$mids:" + xvalExp);
	    System.out.println("histval$counts:" + yvalExp);
	}
	
	//3
	public static void test_3(){
		//Rengine class is the interface between an instance of R and the Java VM
		/**
		 * args - arguments to be passed to R. Please note that R requires the presence of certain arguments 
		 * (e.g. --save or --no-save or equivalents), so passing an empty list usually doesn't work.
		 * runMainLoop - if set to true the the event loop will be started as soon as possible, otherwise no event loop is started. 
		 * Running loop requires initialCallbacks to be set correspondingly as well.
		 * initialCallbacks - an instance implementing the RMainLoopCallbacks interface that provides methods to be called by R
		 * **
		 */
		
		Rengine re = new Rengine(new String[] { "--vanilla" }, false, null);
        System.out.println("Rengine created, waiting for R");
 
        // the engine creates R is a new thread, so we should wait until it's
        // ready
        if (!re.waitForR()) {
            System.out.println("Cannot load R");
            return;
        }
 
        re.eval("data(iris)", false);
        REXP x = re.eval("iris");
        // generic vectors are RVector to accomodate names
        RVector v = x.asVector();
        System.out.println("has names:");
        for (Enumeration e = v.getNames().elements(); e.hasMoreElements();) {
            System.out.println(e.nextElement());
        }
 
        // for compatibility with Rserve we allow casting of vectors to lists
        RList vl = x.asList();
        String[] k = vl.keys();
        System.out.println("and once again from the list:");
        int i = 0;
        while (i < k.length)
            System.out.println(k[i++]);
 
        // get boolean array
        System.out.println(x = re.eval("iris[[1]]>mean(iris[[1]])"));
 
        // R knows about TRUE/FALSE/NA, so we cannot use boolean[] this way
        // instead, we use int[] which is more convenient (and what R uses
        // internally anyway)
        int[] bi = x.asIntArray();
        for (int j : bi) {
            System.out.print(j == 0 ? "F " : (j == 1 ? "T " : "NA "));
        }
        System.out.println("");
 
        // push a boolean array
        boolean by[] = { true, false, false };
        re.assign("bool", by);
        System.out.println(x = re.eval("bool"));
 
        // asBool returns the first element of the array as RBool
        // (mostly useful for boolean arrays of the length 1). is should
        // return true
        System.out.println("isTRUE? " + x.asBool().isTRUE());
 
        // now for a real dotted-pair list:
        System.out.println(x = re.eval("pairlist(a=1,b='foo',c=1:5)"));
        RList l = x.asList();
 
        int idx = 0;
        String[] a = l.keys();
        System.out.println("Keys:");
        while (idx < a.length)
            System.out.println(a[idx++]);
 
        System.out.println("Contents:");
        idx = 0;
        while (idx < a.length)
            System.out.println(l.at(idx++));
 
        System.out.println(re.eval("sqrt(36)"));
	}
	
	//4
	public static void test4(){
		//-1
		Rengine re = new Rengine(new String[] { "--vanilla" }, false, null);
        //System.out.println("Rengine created, waiting for R"); 
        // the engine creates R is a new thread, so we should wait until it's ready
        if (!re.waitForR()) {
            System.out.println("Cannot load R");
            return;
        }
        REXP t = re.eval("2*4", true);
        System.out.println(t.asDouble());
       
        //-2
        re.eval("source('"+"C:/T/WorkBench/Bench_R/OpensourceScripts/apcorr.r"+"')");        
        
        //-3        
        int [] L_3 = {1,2,3,4};
        int [] L_4 = {1,1,1,1};
        re.assign("L_3", L_3);
        re.assign("L_4", L_4);
        REXP apTieCorr = re.eval("apcorr(L_3, L_4)");
        System.out.println(resultFormat.format(apTieCorr.asDouble()));
        
        int [] L_1 = {1,2,3,4};
        int [] L_2 = {1,1,1,2};
        re.assign("L_1", L_1);
        re.assign("L_2", L_2);
        REXP apCorr = re.eval("apcorr.nosampling(L_1, L_2)");
        System.out.println(apCorr.asDouble());		
	}
	
	///////////////
	
    public static void main(String[] args) {
        //1
    	//rtest2.test_1(args);
    	
    	//2
    	//rtest2.test_2();
    	
    	//3
    	//rtest2.test_3();
    	
    	//4
    	rtest2.test4();
    }
}
