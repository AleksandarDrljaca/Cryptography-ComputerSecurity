package org.unibl.etf.Exceptions;

public class UserExistsException extends Exception {

		public UserExistsException() {
			super();
		}
		public UserExistsException(String msg) {
			super(msg);
		}
}
