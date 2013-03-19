Pbar : Pattern {
	*new { arg listPat = nil; var bar;
	   bar = Pfunc{TempoClock.default.bar};
		if( listPat.notNil )
		{ ^Pindex(listPat, bar) } { ^bar }}
}

Pd : Penvir {
	*new { ^Penvir( (d:0, clock:TempoClock.default), Pfunc {
		if( ~lastBar.notNil and: { ~clock.bar > ~lastBar } )
	    { ~d = 0; ~lastBar = ~clock.bar; ~d }
		{ ~d = ~d + 1; ~lastBar = ~clock.bar; ~d }})}
}

Pcontour : Pbinop {
	*new { arg cpat, modal=0, degrees = [0,2,4], drag = 1, octave = 7;
	var offset, s, wrap, fixedPt;
	wrap = { arg a, deg, oct = 7;
	deg.wrapAt(a) + (oct*floor(a/deg.size))};
	fixedPt = { arg m; var s0,s;
		s0 = ((m/octave)+1)*(0-degrees.size);
		{ (m + wrap.value(s0,degrees,octave)) < degrees[0] }.while({ s0=s0+1 });
		s0 };
	if( modal.isKindOf(Pattern),
			{ offset = modal.collect( fixedPt ) },
			{ offset = fixedPt.value( modal ) });
	s = floor(offset*drag) + cpat;

	^Pindex( degrees, s ) +
		(octave*floor(s/degrees.size)) + modal}}

Parp : Pcontour {}

Ploop : Pmono {

	*new { arg ... args;
		^ Pmono(\looper, pairs: [\group, Group.after(1), \buf, Buffer.aok, \amp, 0.5]++args) }
}
