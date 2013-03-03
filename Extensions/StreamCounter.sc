StreamCounter {
	classvar <counter;

	*initClass {
		counter =
		{ var ret; ret = ~count; ~count=~count+1; ret }
		   .inEnvir( (count: 0) );
	}
}