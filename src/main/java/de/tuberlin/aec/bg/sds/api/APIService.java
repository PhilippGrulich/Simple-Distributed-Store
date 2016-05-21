package de.tuberlin.aec.bg.sds.api;

import java.util.concurrent.Callable;
import java.util.function.Function;

import de.tuberlin.aec.bg.sds.Operation;

public abstract class APIService {
	Function<Operation,Boolean> callback;
	public void registerOperationCallback(Function<Operation,Boolean> operationCallback){
		this.callback = operationCallback;
	}
}
