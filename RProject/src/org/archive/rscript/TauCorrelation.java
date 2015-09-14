package org.archive.rscript;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

public class TauCorrelation {
	
	private static Rengine _rRengine;
	
	
	public TauCorrelation(){
		_rRengine = new Rengine(new String[] { "--vanilla" }, false, null);
     
        if (!_rRengine.waitForR()) {
        	System.err.println("Cannot load R error!");
            System.exit(0);
        }
        
        //suppose it will be used
        _rRengine.eval("source('"+"C:/T/WorkBench/Bench_R/OpensourceScripts/apcorr.r"+"')"); 
	}
	
	/**
	 * tau_{ap}
	 * Note: be sure the scores at the same rank i are computed for the same item, otherwise it is nonsense!
	 * **/
	public double TauAP_CorrespondingID(double [] list_A, double [] list_B){
		_rRengine.assign("list_A", list_A);
		_rRengine.assign("list_B", list_B);
        REXP rResultExp = _rRengine.eval("apcorr.nosampling(list_A, list_B)");
        return rResultExp.asDouble();		
	}
	//
	public double TauAP_CorrespondingID(ArrayList<Double> list_A, ArrayList<Double> list_B){
		
		return TauAP_CorrespondingID(ArrayUtils.toPrimitive((Double [])list_A.toArray(new Double [0])),
				ArrayUtils.toPrimitive((Double [])list_B.toArray(new Double [0])));
	}
	/**
	 * Tau_AP_Tie: improved version of Tau_{ap} by considering the effect of ties
	 * Note: be sure the scores at the same rank i are computed for the same item, otherwise it is nonsense!
	 * http://www.mansci.uwaterloo.ca/~msmucker/
	 * **/
	public double TauAP_Tie_CorrespondingID(double [] list_A, double [] list_B){
		_rRengine.assign("list_A", list_A);
		_rRengine.assign("list_B", list_B);
        REXP rResultExp = _rRengine.eval("apcorr(list_A, list_B)");
        return rResultExp.asDouble();
	}
	//
	public double TauAP_Tie_CorrespondingID(ArrayList<Double> list_A, ArrayList<Double> list_B){
		
		return TauAP_Tie_CorrespondingID(ArrayUtils.toPrimitive((Double [])list_A.toArray(new Double [0])), 
				ArrayUtils.toPrimitive((Double [])list_B.toArray(new Double [0])));
		
	}
	
	//
	public static void main(String []args){
		//1
		TauCorrelation tauCorrelation = new TauCorrelation();
		double [] L_1 = {3.1, 2.1, 1.1};
		double [] L_2 = {1.1, 2.1, 3.1};
		System.out.println(tauCorrelation.TauAP_CorrespondingID(L_1, L_2));
		System.out.println(tauCorrelation.TauAP_Tie_CorrespondingID(L_1, L_2));		
	}

}
