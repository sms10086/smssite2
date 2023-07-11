package com.note;


public class NoteException extends Exception {
	private static final long serialVersionUID = 0x1L;
	
	public NoteException() {
	}

	public NoteException(String message) {
		super(message);
	}
	
	public NoteException(String message, Throwable thr) {
		super(message, thr);
	}
	
	public NoteException(Throwable thr) {
		super(thr);
	}
}
