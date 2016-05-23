package de.tuberlin.aec.bg.sds.api;

import java.util.concurrent.Callable;
import java.util.function.Function;

import de.tuberlin.aec.bg.sds.Operation;

public abstract class APIService {
	Function<Operation,APIResultMessage> callback;
	public void registerOperationCallback(Function<Operation,APIResultMessage> operationCallback){
		this.callback = operationCallback;
	}
}
