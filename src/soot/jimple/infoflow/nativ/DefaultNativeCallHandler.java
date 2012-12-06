package soot.jimple.infoflow.nativ;

import java.util.LinkedList;
import java.util.List;

import soot.EquivalentValue;
import soot.PrimType;
import soot.SootMethod;
import soot.Value;
import soot.jimple.Stmt;
import soot.jimple.infoflow.data.Abstraction;

public class DefaultNativeCallHandler extends NativeCallHandler {

	public List<Abstraction> getTaintedValues(Stmt call, Abstraction source, List<Value> params, SootMethod m){
		LinkedList<Abstraction> list = new LinkedList<Abstraction>();
		
		//check some evaluated methods:
		
		//arraycopy:
		//arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
        //Copies an array from the specified source array, beginning at the specified position, to the specified position of the destination array.
		if(call.getInvokeExpr().getMethod().toString().contains("arraycopy")){
			if(params.get(0).equals(source.getTaintedObject().getValue())){
				list.add(new Abstraction(new EquivalentValue(params.get(2)), source.getSource(), m));
			}
		}else{
			//generic case: add taint to all non-primitive datatypes:
			for (int i = 0; i < params.size(); i++) {
				Value argValue = params.get(i);
				if (!(argValue.getType() instanceof PrimType)) {
					list.add(new Abstraction(new EquivalentValue(argValue), source.getSource(), m));
				}
			}
			
			
		}
		
		
		return list;
	}
}