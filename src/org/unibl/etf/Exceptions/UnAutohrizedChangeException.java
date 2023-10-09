package org.unibl.etf.Exceptions;

public class UnAutohrizedChangeException extends Exception{
	public UnAutohrizedChangeException() {
		super();
	}
	public UnAutohrizedChangeException(String msg) {
		super(msg);
	}
}