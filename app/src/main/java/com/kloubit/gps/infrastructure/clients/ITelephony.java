package com.kloubit.gps.infrastructure.clients;
public interface ITelephony
{
	boolean endCall();
	void answerRingingCall();
	void silenceRinger();
}
