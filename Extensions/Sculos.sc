Sculos {

	*start { arg interp, shellPort = NetAddr.langPort, commandPort = 7001,
		commNew = "/sc/new", commRemove = "/sc/remove";

		var n,q;
		n = NetAddr("127.0.0.1", shellPort);
		q = NetAddr("127.0.0.1", commandPort);

		~coll = List();
		OSCdef(\scShellNew, {|msg, time, addr, recvPort|
			var f,coord;
			f = interp.compileFile( msg[1].asString );
			coord = msg[2];

			if ( f.notNil )
			//returning size, pretend numbers one-based
			{ var ret; ret = f.value; ~coll.add(ret);
				q.sendMsg(commNew, coord, ~coll.size ) }
			{ q.sendMsg(commNew, coord, 0) }}, commNew, nil, shellPort);

		OSCdef(\scShellRem, {|msg, time, addr, recvPort|
			var ind; ind = msg[1]-1;
			if ( msg[1] > 0 && {~coll[ind] != nil} )
			{ var ind; ind = msg[1]-1; //actual indices
				~coll[ind].stop;
				~coll[ind] = nil;
				q.sendMsg(commRemove, msg[1]) }
			{ q.sendMsg(commRemove, 0) }}, commRemove, nil, shellPort);

		[OSCdef(\scShellNew), OSCdef(\scShellRem)]
	}
}

