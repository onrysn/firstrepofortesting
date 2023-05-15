package com.mobit;


public class Operation {

	public int nodeId;
	public String message;
	public int yesNodeId;
	public int noNodeId;
	public int windowId;
	public Callback clb;
	public IForm form;
	
	public static final int Yes = -1;
	public static final int No = 2;
	public static final int Next = Yes;
	public static final int Cancel = 0;
	
	public interface Callback {
		
		boolean run(int stage, IForm form, Object extra);
		
	};
	
	public Operation(int nodeId, String message, int windowId, int yesNodeId, int noNodeId, Callback clb) {
		
		if(nodeId == yesNodeId || nodeId == noNodeId)
			throw new MobitRuntimeException(String.format("%d işlemde işlem ID'si ile cevap ID'leri aynı olamaz!", nodeId));
		this.nodeId = nodeId;
		this.message = message;
		this.yesNodeId = yesNodeId;
		this.noNodeId = noNodeId;
		this.windowId = windowId;
		this.clb = clb;
	}

	public Operation(int nodeId, String message, int windowId, int yesNodeId, int noNodeId) {
		this(nodeId, message, windowId, yesNodeId, noNodeId, null);
	}
	
	public Operation(Operation oper) {
		this(oper.nodeId, oper.message, oper.windowId, oper.yesNodeId, oper.noNodeId, oper.clb);
	}

	@Override
	public String toString() {
		return message;
	}
		
	public Operation next(Operation[] operations, IForm form, int resultCode) {
		
		
		//if(clb != null) clb.run((ok) ? 22 : 32, form, null);
		for (Operation oper : operations) {
			if (resultCode == Yes) {
				if (oper.nodeId == yesNodeId){
					if(oper.clb != null) oper.clb.run(11, form, null);
					return oper;
				}
			} else if (resultCode == No) {
				if (oper.nodeId == noNodeId){
					if(oper.clb != null) oper.clb.run(12, form, null);
					return oper;
				}
			}
			else if (resultCode == Next) {
				if (oper.nodeId == yesNodeId){
					if(oper.clb != null) oper.clb.run(11, form, null);
					return oper;
				}
			} 
		}
		return null;
	}

	public boolean isFinished(int resultCode) {
		if (resultCode == Yes) {
			if (yesNodeId < 0)
				return true;
		} else if(noNodeId == No){
			if (noNodeId < 0)
				return true;
		}
		return false;
	}
	
	public static Operation first(Operation[] operations, IForm form) {
		int nodeId = Integer.MAX_VALUE;
		int index = -1;
		Operation oper = null;
		for (int i = 0; i < operations.length; i++) {
			oper = operations[i];
			if (oper.nodeId < nodeId) {
				nodeId = oper.nodeId;
				index = i;
			}
		}
		oper = (index > -1) ? operations[index] : null;
		if(oper != null && oper.clb != null) oper.clb.run(11, form, null);
		return oper;
	}
	
	public static Operation [] clone(Operation [] operations)
	{
		Operation [] _operations = new Operation[operations.length];
		for(int i = 0; i < operations.length; i++) _operations[i] = new Operation(operations[i]);
		return _operations;
	}

};